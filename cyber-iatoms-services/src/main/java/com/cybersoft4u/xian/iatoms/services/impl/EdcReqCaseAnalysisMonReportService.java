package com.cybersoft4u.xian.iatoms.services.impl;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import cafe.core.dao.DataAccessException;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ReportSettingDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.utils.mail.MailComponent;
import com.cybersoft4u.xian.iatoms.common.utils.mail.TemplateMailMessageDTO;
import com.cybersoft4u.xian.iatoms.services.IReportService;
import com.cybersoft4u.xian.iatoms.services.dao.IReportSettingDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO;
/**
 * Purpose: EDC報表服務需求案件(維修)分析月報
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/2/28
 * @MaintenancePersonnel CarrieDuan
 */
public class EdcReqCaseAnalysisMonReportService extends AtomicService implements IReportService {

	/**
	 * 記錄日誌
	 */
	private static final CafeLog LOGGER	= CafeLogFactory.getLog(EdcReqCaseAnalysisMonReportService.class);
	
	/**
	 * 
	 */
	private ISrmCaseHandleInfoDAO caseHandleInfoDAO;
	/**
	 * 注入報表發送功能設定DAO
	 */
	private IReportSettingDAO reportSettingDAO;
	/**
	 * 注入發送郵件
	 */
	private MailComponent mailComponent;
	/**
	 * 
	 */
	private List<String> orderByCodes;
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IEdcReqCaseAnalysisMonReportService#sendMailAnalysisMonthlyReport()
	 */
	public void sendMailAnalysisMonthlyReport() throws ServiceException {
		sendReportMail(DateTimeUtils.getCurrentDate(), null, IAtomsConstants.REPORT_NAME_MONTH_REPORT);
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IReportService#sendReportMail(java.util.Date)
	 */
	public void sendReportMail(Date sendDate, String customerId, String reportCode) throws ServiceException {
		try {
			String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD);
			String sendDateString = DateTimeUtils.toString(sendDate, DateTimeUtils.DT_FMT_YYYYMMDD);
			Calendar calStart=Calendar.getInstance();
			Calendar calEnd=Calendar.getInstance();
			calStart.setTime(sendDate);
			calEnd.setTime(sendDate);
			//獲取查詢截止時間
			Timestamp queryEndDate = DateTimeUtils.toTimestamp(sendDateString);
			//獲取查詢開始時間
			calStart.add(Calendar.MONTH, -1);
			calStart.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天 
			Timestamp startDate = DateTimeUtils.toTimestamp(DateTimeUtils.toString(calStart.getTime(), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH));
			//
			calEnd.add(Calendar.MONTH, -1);
			calEnd.set(Calendar.DAY_OF_MONTH, calEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
			Timestamp endDate = DateTimeUtils.toTimestamp(DateTimeUtils.toString(calEnd.getTime(), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH));
			//郵件內容
			String mailContext = null;
			//郵件主題模板
			String subjectTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.EDC_REQ_CASE_ANALYSIS_SUBJECT;
			//郵件內容模板
			String textTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.EDC_REQ_CASE_ANALYSIS_CONTEXT;
			//郵件內容
			Map<String, Object> variables = new HashMap<String, Object>();
			//查詢EDC提示報表的信息
			List<ReportSettingDTO> reportSettingList = this.reportSettingDAO.listByReportCode(customerId, reportCode);
			if (CollectionUtils.isEmpty(reportSettingList)) {
				return;
			} else {
 				List<SrmCaseHandleInfoDTO> caseHandleInfoCaseCategoryDTO = null;
				List<SrmCaseHandleInfoDTO> caseHandleInfoContractDTO = null;
				List<SrmCaseHandleInfoDTO> caseHandleInfoDeptCodeDTO = null;
				for (ReportSettingDTO reportSettingDTO : reportSettingList) {
					if (StringUtils.hasText(reportSettingDTO.getRecipient())) {
						for (String orderByCode : orderByCodes) {
							if (IAtomsConstants.ORDER_BY_CASE_CATEGORY.equals(orderByCode)) {
								caseHandleInfoCaseCategoryDTO = this.caseHandleInfoDAO.listEdcReqCaseAnalysisMonReport(reportSettingDTO.getCompanyId(), startDate, queryEndDate, orderByCode);
								if (!CollectionUtils.isEmpty(caseHandleInfoCaseCategoryDTO)) {
									variables.put("caseCategoryList", this.getMailSubjectContext(caseHandleInfoCaseCategoryDTO, orderByCode));
								} else {
									variables.put("caseCategoryList", this.getMailSubjectContext(new ArrayList<SrmCaseHandleInfoDTO>(), orderByCode));
								}
							} else if (IAtomsConstants.ORDER_BY_CONTRACT_CODE.equals(orderByCode)) {
								caseHandleInfoContractDTO = this.caseHandleInfoDAO.listEdcReqCaseAnalysisMonReport(reportSettingDTO.getCompanyId(), startDate, queryEndDate, orderByCode);
								if (!CollectionUtils.isEmpty(caseHandleInfoContractDTO)) {
									variables.put("contractCodeList", this.getMailSubjectContext(caseHandleInfoContractDTO, orderByCode));
								} else {
									variables.put("contractCodeList", this.getMailSubjectContext(new ArrayList<SrmCaseHandleInfoDTO>(), orderByCode));
								}
							} else {
								caseHandleInfoDeptCodeDTO = this.caseHandleInfoDAO.listEdcReqCaseAnalysisMonReport(reportSettingDTO.getCompanyId(), startDate, queryEndDate, orderByCode);
								if (!CollectionUtils.isEmpty(caseHandleInfoDeptCodeDTO)) {
									variables.put("deptCodeList", this.getMailSubjectContext(caseHandleInfoDeptCodeDTO, orderByCode));
								} else {
									variables.put("deptCodeList", this.getMailSubjectContext(new ArrayList<SrmCaseHandleInfoDTO>(), orderByCode));
								}
							}
						}
						variables.put("shortName", reportSettingDTO.getCustomerName());
						variables.put("startDate", DateTimeUtils.toString(startDate, DateTimeUtils.DT_FMT_YYYYMMDD_SLASH));
						variables.put("endDate", DateTimeUtils.toString(endDate, DateTimeUtils.DT_FMT_YYYYMMDD_SLASH));
						variables.put("currentTimestamp", DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_SLASH));
						variables.put("reportTime", yearMonthDay);
						variables.put("toMail", reportSettingDTO.getRecipient());
						variables.put("ccMail", reportSettingDTO.getCopy());
						if (CollectionUtils.isEmpty(caseHandleInfoCaseCategoryDTO)
								&& CollectionUtils.isEmpty(caseHandleInfoContractDTO)
								&& CollectionUtils.isEmpty(caseHandleInfoDeptCodeDTO)) {
							variables.put("result", i18NUtil.getName(IAtomsConstants.REPORT_RESULT_IS_NULL));
						} else {
							variables.put("result", "如下");
						}
						try{
							this.mailComponent.sendMailTo(null, reportSettingDTO.getRecipient(), subjectTemplate, textTemplate, variables);
						} catch (Exception e) {
							LOGGER.error("sendMailAnalysisMonthlyReport()-->sendMailTo() ", e);
						}
						
					}
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error("sendMailAnalysisMonthlyReport() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("sendMailAnalysisMonthlyReport()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	
	/**
	 * Purpose：拼接表格內容數據
	 * @author CarrieDuan
	 * @param caseHandleInfoDTOs：內容DTO
	 * @param orderBy：分組條件
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return String：返回內容String
	 */
	public String getMailSubjectContext (List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs, String orderBy) throws ServiceException {
		StringBuffer buffer = new StringBuffer();
		try {
			int i = 1;
			double createCount = 0;
			double closeCount = 0;
			double responseCount = 0;
			double finishCount = 0;
			DecimalFormat decimalFormat = new DecimalFormat("##.00%");
			DecimalFormat decimal = new DecimalFormat("###################.###########"); 
			for (SrmCaseHandleInfoDTO srmCaseHandleInfoDTO : caseHandleInfoDTOs) {
				createCount += srmCaseHandleInfoDTO.getCaseCount();
				closeCount += srmCaseHandleInfoDTO.getCloseCount();
				responseCount += srmCaseHandleInfoDTO.getResponseCount();
				finishCount += srmCaseHandleInfoDTO.getFinishCount();
				buffer.append("<tr>");
				buffer.append("<td style=\"border:1px solid black; width :10%; text-align:center; \">").append(i++).append("</td>");
				if (IAtomsConstants.ORDER_BY_CASE_CATEGORY.equals(orderBy)) {
					if(StringUtils.hasText(srmCaseHandleInfoDTO.getCaseCategoryName())){
						buffer.append("<td style=\"border:1px solid black; width :20%; text-align:center; \">").append(srmCaseHandleInfoDTO.getCaseCategoryName()).append("</td>");
					} else {
						buffer.append("<td style=\"border:1px solid black; width :20%; text-align:center; \">").append("0</td>");
					}
				} else if (IAtomsConstants.ORDER_BY_CONTRACT_CODE.equals(orderBy)) {
					if(StringUtils.hasText(srmCaseHandleInfoDTO.getContractCode())){
						buffer.append("<td style=\"border:1px solid black; width :20%; text-align:center; \">").append(srmCaseHandleInfoDTO.getContractCode()).append("</td>");
					} else {
						buffer.append("<td style=\"border:1px solid black; width :20%; text-align:center; \">").append("0</td>");
					}
				} else {
					if(StringUtils.hasText(srmCaseHandleInfoDTO.getDepartmentName())){
						buffer.append("<td style=\"border:1px solid black; width :20%; text-align:center; \">").append(srmCaseHandleInfoDTO.getDepartmentName()).append("</td>");
					} else {
						buffer.append("<td style=\"border:1px solid black; width :20%; text-align:center; \">").append("0</td>");
					}
				}
				if(srmCaseHandleInfoDTO.getCaseCount() != 0){
					buffer.append("<td style=\"border:1px solid black; width :10%; text-align:center;\">").append(srmCaseHandleInfoDTO.getCaseCount()).append("</td>");
				} else {
					buffer.append("<td style=\"border:1px solid black; width :10%; text-align:center;\">").append("0</td>");
				}
				if(srmCaseHandleInfoDTO.getCloseCount() != 0){
					buffer.append("<td style=\"border:1px solid black; width :10%; text-align:center;\">").append(srmCaseHandleInfoDTO.getCloseCount().toString()).append("</td>");
				} else {
					buffer.append("<td style=\"border:1px solid black; width :10%; text-align:center;\">").append("0</td>");
				}
				if(srmCaseHandleInfoDTO.getResponseCount() != 0){
					buffer.append("<td style=\"border:1px solid black; width :10%;text-align:center;\">").append(srmCaseHandleInfoDTO.getResponseCount()).append("</td>");
				} else {
					buffer.append("<td style=\"border:1px solid black; width :10%;text-align:center;\">").append("0</td>");
				}
				if(StringUtils.hasText(srmCaseHandleInfoDTO.getResponsePer())){
					buffer.append("<td style=\"border:1px solid black; width :15%;text-align:center;\">").append(srmCaseHandleInfoDTO.getResponsePer()).append("</td>");
				} else {
					buffer.append("<td style=\"border:1px solid black; width :15%;text-align:center;\">").append("0</td>");
				}
				if(srmCaseHandleInfoDTO.getFinishCount() != 0){
					buffer.append("<td style=\"border:1px solid black; width :10%;text-align:center;\">").append(srmCaseHandleInfoDTO.getFinishCount()).append("</td>");
				} else {
					buffer.append("<td style=\"border:1px solid black; width :10%;text-align:center;\">").append("0</td>");
				}
				
				if(StringUtils.hasText(srmCaseHandleInfoDTO.getFinishPer())){
					buffer.append("<td align=\"rigth\" style=\"border:1px solid black; width :15%;text-align:center;\">").append(srmCaseHandleInfoDTO.getFinishPer()).append("</td>");
				} else {
					buffer.append("<td align=\"rigth\" style=\"border:1px solid black; width :15%;text-align:center;\">").append("0</td>");
				}
				buffer.append("</tr>");
			}
			buffer.append("<tr>");
			buffer.append("<td style=\"background-color: #FFFACD;border:1px solid black; width :10%; text-align:center; \">").append("").append("</td>");
			buffer.append("<td style=\"background-color: #FFFACD;border:1px solid black; width :10%; text-align:center; \">").append("總計").append("</td>");
			if(createCount != 0){
				buffer.append("<td style=\"background-color: #FFFACD;border:1px solid black; width :10%; text-align:center;\">").append(decimal.format(createCount)).append("</td>");
			} else {
				buffer.append("<td style=\"background-color: #FFFACD;border:1px solid black; width :10%; text-align:center;\">").append("0</td>");
			}
			if(closeCount != 0){
				buffer.append("<td style=\"background-color: #FFFACD;border:1px solid black; width :10%; text-align:center;\">").append(decimal.format(closeCount)).append("</td>");
			} else {
				buffer.append("<td style=\"background-color: #FFFACD;border:1px solid black; width :10%; text-align:center;\">").append("0</td>");
			}
			if(responseCount != 0){
				buffer.append("<td style=\"background-color: #FFFACD;border:1px solid black; width :10%;text-align:center;\">").append(decimal.format(responseCount)).append("</td>");
			} else {
				buffer.append("<td style=\"background-color: #FFFACD;border:1px solid black; width :10%;text-align:center;\">").append("0</td>");
			}
			if(createCount != 0 && responseCount != 0){
				buffer.append("<td style=\"background-color: #FFFACD;border:1px solid black; width :10%;text-align:center;\">").append(decimalFormat.format(responseCount/closeCount)).append("</td>");
			} else {
				buffer.append("<td style=\"background-color: #FFFACD;border:1px solid black; width :10%;text-align:center;\">").append("0.00%</td>");
			}
			if(finishCount != 0){
				buffer.append("<td style=\"background-color: #FFFACD;border:1px solid black; width :10%;text-align:center;\">").append(decimal.format(finishCount)).append("</td>");
			} else {
				buffer.append("<td style=\"background-color: #FFFACD;border:1px solid black; width :10%;text-align:center;\">").append("0</td>");
			}
			if(createCount != 0 && finishCount != 0){
				buffer.append("<td style=\"background-color: #FFFACD;border:1px solid black; width :10%;text-align:center;\">").append(decimalFormat.format(finishCount/closeCount)).append("</td>");
			} else {
				buffer.append("<td style=\"background-color: #FFFACD;border:1px solid black; width :10%;text-align:center;\">").append("0.00%</td>");
			}
			buffer.append("</tr>");
		} catch (Exception e) {
			LOGGER.error("getMailSubjectContext()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return buffer.toString();
	} 
	
	/**
	 * @return the caseHandleInfoDAO
	 */
	public ISrmCaseHandleInfoDAO getCaseHandleInfoDAO() {
		return caseHandleInfoDAO;
	}
	/**
	 * @param caseHandleInfoDAO the caseHandleInfoDAO to set
	 */
	public void setCaseHandleInfoDAO(ISrmCaseHandleInfoDAO caseHandleInfoDAO) {
		this.caseHandleInfoDAO = caseHandleInfoDAO;
	}
	/**
	 * @return the reportSettingDAO
	 */
	public IReportSettingDAO getReportSettingDAO() {
		return reportSettingDAO;
	}
	/**
	 * @param reportSettingDAO the reportSettingDAO to set
	 */
	public void setReportSettingDAO(IReportSettingDAO reportSettingDAO) {
		this.reportSettingDAO = reportSettingDAO;
	}
	/**
	 * @return the mailComponent
	 */
	public MailComponent getMailComponent() {
		return mailComponent;
	}
	/**
	 * @param mailComponent the mailComponent to set
	 */
	public void setMailComponent(MailComponent mailComponent) {
		this.mailComponent = mailComponent;
	}
	/**
	 * @return the orderByCodes
	 */
	public List<String> getOrderByCodes() {
		return orderByCodes;
	}
	/**
	 * @param orderByCodes the orderByCodes to set
	 */
	public void setOrderByCodes(List<String> orderByCodes) {
		this.orderByCodes = orderByCodes;
	}

	
	
}
