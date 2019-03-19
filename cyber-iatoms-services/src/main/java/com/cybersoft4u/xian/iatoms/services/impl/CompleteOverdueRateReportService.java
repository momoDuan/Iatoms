package com.cybersoft4u.xian.iatoms.services.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
 * 
 * Purpose: 完修逾時率警示通知service
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/4/18
 * @MaintenancePersonnel CarrieDuan
 */
public class CompleteOverdueRateReportService extends AtomicService implements IReportService {

	/**
	 * 記錄日誌
	 */
	private static final CafeLog LOGGER	= CafeLogFactory.getLog(CompleteOverdueRateReportService.class);
	/**
	 * 发送mail组件
	 */
	private MailComponent mailComponent;
	/**
	 * 注入報表發送功能設定DAO
	 */
	private IReportSettingDAO reportSettingDAO;
	/**
	 * SRM_案件處理資料檔DAO
	 */
	private ISrmCaseHandleInfoDAO srmCaseHandleInfoDAO;
	/**
	 * Purpose:完修逾時率警示通知
	 * @author CarrieDuan
	 * @throws ServiceException
	 * @return void
	 */
	public void sendMailCompleteOverdueRateReport() throws ServiceException {
		sendReportMail(DateTimeUtils.getCurrentDate(), null, IAtomsConstants.LEAVE_CASE_STATUS_TWELVE);
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IReportService#sendReportMail(java.util.Date)
	 */
	public void sendReportMail(Date sendDate, String customerId, String reportCode) throws ServiceException {
		try {
			List<ReportSettingDTO> reportSettingDTOs = this.reportSettingDAO.listByReportCode(customerId, reportCode);
			if (!CollectionUtils.isEmpty(reportSettingDTOs)) {
				SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
				//獲取查詢截止日誌
				Date endDate = DateTimeUtils.toTimestamp(DateTimeUtils.toString(sendDate, DateTimeUtils.DT_FMT_YYYYMMDD_SLASH));
				Date queryEndDate = DateTimeUtils.addCalendar(endDate, 0, 0, 1);
				//獲取查詢開始日期
				//獲取查詢開始時間
				Calendar calStart=Calendar.getInstance();
				calStart.setTime(sendDate);
				calStart.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天 
				Date startDate = DateTimeUtils.toTimestamp(sf.format(calStart.getTime()));
				List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = null;
				//郵件主題模板
				String subjectTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.COMPLETE_OVERDUE_RATE_REPORT_SUBJECT;
				//郵件內容模板
				String textTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.COMPLETE_OVERDUE_RATE_REPORT_CONTEXT;
				//郵件內容
				Map<String, Object> variables = new HashMap<String, Object>();
				List<String> reportDetatilIds = null;
				String perNumber = null;
				double closeCount = 0;
				double finishCount = 0;
				boolean isEmpty = true;
				for (ReportSettingDTO reportSettingDTO : reportSettingDTOs) {
					closeCount = 0;
					finishCount = 0;
					if (StringUtils.hasText(reportSettingDTO.getRecipient())) {
						DecimalFormat decimalFormat = new DecimalFormat("##.00%");
						StringBuffer buffer = new StringBuffer();
						caseHandleInfoDTOs = this.srmCaseHandleInfoDAO.listCompleteOverdueRate(reportSettingDTO.getCompanyId(), startDate, queryEndDate);
						
						if (!CollectionUtils.isEmpty(caseHandleInfoDTOs)) {
							for (SrmCaseHandleInfoDTO srmCaseHandleInfoDTO : caseHandleInfoDTOs) {
								closeCount += srmCaseHandleInfoDTO.getCloseCount();
								finishCount += srmCaseHandleInfoDTO.getFinishCount();
							}
							if (finishCount == 0) {
								perNumber = "0.00%";
							} else {
								perNumber = decimalFormat.format(finishCount/closeCount);
							}
							variables.put("caseInfoDetails", this.getMailSubjectContext(caseHandleInfoDTOs));
						} else {
							perNumber = "0.00%";
							variables.put("caseInfoDetails", this.getMailSubjectContext(new ArrayList<SrmCaseHandleInfoDTO>()));
						}
						if (StringUtils.hasText(reportSettingDTO.getReportDetatilId())) {
							reportDetatilIds = StringUtils.toList(reportSettingDTO.getReportDetatilId(), IAtomsConstants.MARK_SEPARATOR);
							if (reportDetatilIds.contains(IAtomsConstants.REPORT_NAME_DAY_REPORT)) {
								caseHandleInfoDTOs = this.srmCaseHandleInfoDAO.listCreateCaseToOverdueReport(reportSettingDTO.getCompanyId(), startDate, queryEndDate);
								if (!CollectionUtils.isEmpty(caseHandleInfoDTOs)) {
									isEmpty = false;
									this.getMailSubjectContext(caseHandleInfoDTOs,"進件案件明細", buffer);
								} else {
									this.getMailSubjectContext(new ArrayList<SrmCaseHandleInfoDTO>(), "進件案件明細", buffer);
								}
							}
							if (reportDetatilIds.contains(IAtomsConstants.REPORT_NAME_MONTH_REPORT)) {
								caseHandleInfoDTOs = this.srmCaseHandleInfoDAO.listClosedCaseToOverdueReport(reportSettingDTO.getCompanyId(), startDate, queryEndDate, Boolean.FALSE);
								if (!CollectionUtils.isEmpty(caseHandleInfoDTOs)) {
									isEmpty = false;
									this.getMailSubjectContext(caseHandleInfoDTOs,"結案案件明細", buffer);
								} else {
									this.getMailSubjectContext(new ArrayList<SrmCaseHandleInfoDTO>(), "結案案件明細", buffer);
								}
							}
							if (reportDetatilIds.contains(IAtomsConstants.REPORT_NAME_EDC_OUTSTANDING_NUM_REPORT)) {
								caseHandleInfoDTOs = this.srmCaseHandleInfoDAO.listClosedCaseToOverdueReport(reportSettingDTO.getCompanyId(), startDate, queryEndDate, Boolean.TRUE);
								if (!CollectionUtils.isEmpty(caseHandleInfoDTOs)) {
									isEmpty = false;
									this.getMailSubjectContext(caseHandleInfoDTOs,"完修逾時案件明細", buffer);
								} else {
									this.getMailSubjectContext(new ArrayList<SrmCaseHandleInfoDTO>(), "完修逾時案件明細", buffer);
								}
							}
						}
						variables.put("list", buffer.toString());
						variables.put("yearMonthDay", DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD));
						variables.put("shortName", reportSettingDTO.getCustomerName());
						variables.put("startDate", DateTimeUtils.toString(startDate,  DateTimeUtils.DT_FMT_YYYYMMDD_SLASH));
						variables.put("endDate", DateTimeUtils.toString(endDate,  DateTimeUtils.DT_FMT_YYYYMMDD_SLASH));
						variables.put("dateTime", DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(),  DateTimeUtils.DT_FMT_YYYYMMDDHHMM_SLASH));
						variables.put("toMail", reportSettingDTO.getRecipient());
						variables.put("ccMail", reportSettingDTO.getCopy());
						variables.put("perNumber", perNumber);
						if (isEmpty) {
							variables.put("result", i18NUtil.getName(IAtomsConstants.REPORT_RESULT_IS_NULL));
						} else {
							isEmpty = true;
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
			LOGGER.error("sendMailFinishedReplyReportToTCB() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("sendMailFinishedReplyReportToTCB()", "DataAccess Exception:", e);
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
	public String getMailSubjectContext (List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs) throws ServiceException {
		StringBuffer buffer = new StringBuffer();
		try {
			int i = 1;
			double createCount = 0;
			double closeCount = 0;
			double finishCount = 0;
			DecimalFormat decimalFormat = new DecimalFormat("##.00%");
			DecimalFormat decimal = new DecimalFormat("###################.###########"); 
			for (SrmCaseHandleInfoDTO srmCaseHandleInfoDTO : caseHandleInfoDTOs) {
				createCount += srmCaseHandleInfoDTO.getCaseCount();
				closeCount += srmCaseHandleInfoDTO.getCloseCount();
				finishCount += srmCaseHandleInfoDTO.getFinishCount();
				buffer.append("<tr>");
				buffer.append("<td style=\"border:1px solid black; width :15%; text-align:center; \">").append(i++).append("</td>");
				if(StringUtils.hasText(srmCaseHandleInfoDTO.getCompanyName())){
					buffer.append("<td style=\"border:1px solid black; width :25%; text-align:center; \">").append(srmCaseHandleInfoDTO.getCompanyName()).append("</td>");
				} else {
					buffer.append("<td style=\"border:1px solid black; width :25%; text-align:center; \">").append("''</td>");
				}
				if(srmCaseHandleInfoDTO.getCaseCount() != 0){
					buffer.append("<td style=\"border:1px solid black; width :15%; text-align:center;\">").append(srmCaseHandleInfoDTO.getCaseCount()).append("</td>");
				} else {
					buffer.append("<td style=\"border:1px solid black; width :15%; text-align:center;\">").append("0</td>");
				}
				if(srmCaseHandleInfoDTO.getCloseCount() != 0){
					buffer.append("<td style=\"border:1px solid black; width :15%; text-align:center;\">").append(srmCaseHandleInfoDTO.getCloseCount().toString()).append("</td>");
				} else {
					buffer.append("<td style=\"border:1px solid black; width :15%; text-align:center;\">").append("0</td>");
				}
				if(srmCaseHandleInfoDTO.getFinishCount() != 0){
					buffer.append("<td style=\"border:1px solid black; width :15%;text-align:center;\">").append(srmCaseHandleInfoDTO.getFinishCount()).append("</td>");
				} else {
					buffer.append("<td style=\"border:1px solid black; width :15%;text-align:center;\">").append("0</td>");
				}
				
				if(StringUtils.hasText(srmCaseHandleInfoDTO.getFinishPer())){
					buffer.append("<td align=\"rigth\" style=\"border:1px solid black; width :15%;text-align:center;\">").append(srmCaseHandleInfoDTO.getFinishPer()).append("</td>");
				} else {
					buffer.append("<td align=\"rigth\" style=\"border:1px solid black; width :15%;text-align:center;\">").append("0</td>");
				}
				buffer.append("</tr>");
			}
			buffer.append("<tr>");
			buffer.append("<td style=\"background-color: #FFFACD;border:1px solid black; width :15%; text-align:center; \">").append("").append("</td>");
			buffer.append("<td style=\"background-color: #FFFACD;border:1px solid black; width :25%; text-align:center; \">").append("合計").append("</td>");
			if(createCount != 0){
				buffer.append("<td style=\"background-color: #FFFACD; border:1px solid black; width :15%; text-align:center;\">").append(decimal.format(createCount)).append("</td>");
			} else {
				buffer.append("<td style=\"background-color: #FFFACD;border:1px solid black; width :15%; text-align:center;\">").append("0</td>");
			}
			if(closeCount != 0){
				buffer.append("<td style=\"background-color: #FFFACD;border:1px solid black; width :15%; text-align:center;\">").append(decimal.format(closeCount)).append("</td>");
			} else {
				buffer.append("<td style=\"background-color: #FFFACD;border:1px solid black; width :15%; text-align:center;\">").append("0</td>");
			}
			if(finishCount != 0){
				buffer.append("<td style=\"background-color: #FFFACD;border:1px solid black; width :15%;text-align:center;\">").append(decimal.format(finishCount)).append("</td>");
			} else {
				buffer.append("<td style=\"background-color: #FFFACD;border:1px solid black; width :15%;text-align:center;\">").append("0</td>");
			}
			if(createCount != 0 && finishCount != 0){
				buffer.append("<td style=\"border:1px solid black; width :15%;text-align:center;\">").append(decimalFormat.format(finishCount/closeCount)).append("</td>");
			} else {
				buffer.append("<td style=\"border:1px solid black; width :15%;text-align:center;\">").append("0.00%</td>");
			}
			buffer.append("</tr>");
		} catch (Exception e) {
			LOGGER.error("getMailSubjectContext()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return buffer.toString();
	} 
	
	public void getMailSubjectContext (List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs, String title, StringBuffer buffer) throws ServiceException {
		try {
			int i = 1;
			buffer.append("<table cellspacing=0 cellpadding=0  cellspacing=\"0px\" style=\"font-family:PMingLiU;color:#000000;word-break:break-all; word-wrap:break-all; width :1150px; margin-left :30px;border:1px solid black;\"   >");
			buffer.append("<thead >");
			buffer.append("<tr><th colspan=\"12\" style=\"background-color: #DEFFAC; font-size: 20px; text-align: left;\">").append(title).append("</th></tr>");
			buffer.append("<tr style=\"text-align: center; background-color: #AAAAFF\">");
			buffer.append("<th style= \"border:1px solid black; width :5%; align:center \">序號</th>");
			buffer.append("<th style= \"border:1px solid black; width :5%; align:center\">案件類別</th>");
			buffer.append("<th style= \"border:1px solid black; width :10%; align:center\">案件編號</th>");
			buffer.append("<th style= \"border:1px solid black; width :10%; align:center \">進件時間</th>");
			buffer.append("<th style= \"border:1px solid black; width :10%; align:center \">特店名稱</th>");
			buffer.append("<th style= \"border:1px solid black; width :10%; align:center \">特店代號</th>");
			buffer.append("<th style= \"border:1px solid black; width :5%; align:center \">DTID</th>");
			buffer.append("<th style= \"border:1px solid black; width :10%; align:center \">設備編號</th>");
			buffer.append("<th style= \"border:1px solid black; width :10%; align:center \">需求描述</th>");
			buffer.append("<th style= \"border:1px solid black; width :10%; align:center \">預計完成日</th>");
			buffer.append("<th style= \"border:1px solid black; width :10%; align:center \">實際完成日</th>");
			buffer.append("<th style= \"border:1px solid black; width :5%; align:center \">處理狀態</th>");
			buffer.append("</thead >");
			buffer.append("<tbody>");
			for (SrmCaseHandleInfoDTO srmCaseHandleInfoDTO : caseHandleInfoDTOs) {
				SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
				buffer.append("<tr>");
				buffer.append("<td style=\"border:1px solid black; width :5%; text-align:center; \">").append(i++).append("</td>");
				if(StringUtils.hasText(srmCaseHandleInfoDTO.getCaseCategoryName())){
					buffer.append("<td style=\"border:1px solid black; width :5%; text-align:center; \">").append(srmCaseHandleInfoDTO.getCaseCategoryName()).append("</td>");
				} else {
					buffer.append("<td style=\"border:1px solid black; width :5%; text-align:center; \">").append(" </td>");
				}
				if(StringUtils.hasText(srmCaseHandleInfoDTO.getCaseId())){
					buffer.append("<td style=\"border:1px solid black; width :10%; text-align:center;\">").append(srmCaseHandleInfoDTO.getCaseId()).append("</td>");
				} else {
					buffer.append("<td style=\"border:1px solid black; width :10%; text-align:center;\">").append(" </td>");
				}
				if(srmCaseHandleInfoDTO.getCreatedDate() != null){
					buffer.append("<td style=\"border:1px solid black; width :10%; text-align:center;\">").append(sf.format(srmCaseHandleInfoDTO.getCreatedDate())).append("</td>");
				} else {
					buffer.append("<td style=\"border:1px solid black; width :10%; text-align:center;\">").append(" </td>");
				}
				if(StringUtils.hasText(srmCaseHandleInfoDTO.getMerchantName())){
					buffer.append("<td style=\"border:1px solid black; width :10%;text-align:center;\">").append(srmCaseHandleInfoDTO.getMerchantName()).append("</td>");
				} else {
					buffer.append("<td style=\"border:1px solid black; width :10%;text-align:center;\">").append(" </td>");
				}
				
				if(StringUtils.hasText(srmCaseHandleInfoDTO.getMerchantCode())){
					buffer.append("<td align=\"rigth\" style=\"border:1px solid black; width :10%;text-align:center;\">").append(srmCaseHandleInfoDTO.getMerchantCode()).append("</td>");
				} else {
					buffer.append("<td align=\"rigth\" style=\"border:1px solid black; width :10%;text-align:center;\">").append(" </td>");
				}
				if(StringUtils.hasText(srmCaseHandleInfoDTO.getDtid())){
					buffer.append("<td align=\"rigth\" style=\"border:1px solid black; width :5%;text-align:center;\">").append(srmCaseHandleInfoDTO.getDtid()).append("</td>");
				} else {
					buffer.append("<td align=\"rigth\" style=\"border:1px solid black; width :5%;text-align:center;\">").append(" </td>");
				}
				if(StringUtils.hasText(srmCaseHandleInfoDTO.getSerialNumber())){
					buffer.append("<td align=\"rigth\" style=\"border:1px solid black; width :10%;text-align:center;\">").append(srmCaseHandleInfoDTO.getSerialNumber()).append("</td>");
				} else {
					buffer.append("<td align=\"rigth\" style=\"border:1px solid black; width :10%;text-align:center;\">").append(" </td>");
				}
				if(StringUtils.hasText(srmCaseHandleInfoDTO.getDescription())){
					buffer.append("<td align=\"rigth\" style=\"border:1px solid black; width :10%;text-align:center;\">").append(srmCaseHandleInfoDTO.getDescription()).append("</td>");
				} else {
					buffer.append("<td align=\"rigth\" style=\"border:1px solid black; width :10%;text-align:center;\">").append(" </td>");
				}
				if(srmCaseHandleInfoDTO.getAcceptableFinishDate() != null){
					buffer.append("<td align=\"rigth\" style=\"border:1px solid black; width :10%;text-align:center;\">").append(sf.format(srmCaseHandleInfoDTO.getAcceptableFinishDate())).append("</td>");
				} else {
					buffer.append("<td align=\"rigth\" style=\"border:1px solid black; width :10%;text-align:center;\">").append(" </td>");
				}
				if(srmCaseHandleInfoDTO.getCompleteDate() != null){
					buffer.append("<td align=\"rigth\" style=\"border:1px solid black; width :10%;text-align:center;\">").append(sf.format(srmCaseHandleInfoDTO.getCompleteDate())).append("</td>");
				} else {
					buffer.append("<td align=\"rigth\" style=\"border:1px solid black; width :10%;text-align:center;\">").append(" </td>");
				}
				if(StringUtils.hasText(srmCaseHandleInfoDTO.getCaseStatusName())){
					buffer.append("<td align=\"rigth\" style=\"border:1px solid black; width :5%;text-align:center;\">").append(srmCaseHandleInfoDTO.getCaseStatusName()).append("</td>");
				} else {
					buffer.append("<td align=\"rigth\" style=\"border:1px solid black; width :5%;text-align:center;\">").append(" </td>");
				}
				buffer.append("</tr>");
			}
			buffer.append("</tbody>");
			buffer.append("</table>");
			buffer.append("<br>");
		} catch (Exception e) {
			LOGGER.error("getMailSubjectContext()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
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
	 * @return the srmCaseHandleInfoDAO
	 */
	public ISrmCaseHandleInfoDAO getSrmCaseHandleInfoDAO() {
		return srmCaseHandleInfoDAO;
	}

	/**
	 * @param srmCaseHandleInfoDAO the srmCaseHandleInfoDAO to set
	 */
	public void setSrmCaseHandleInfoDAO(ISrmCaseHandleInfoDAO srmCaseHandleInfoDAO) {
		this.srmCaseHandleInfoDAO = srmCaseHandleInfoDAO;
	}
}
