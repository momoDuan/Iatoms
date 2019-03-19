package com.cybersoft4u.xian.iatoms.services.impl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import cafe.core.config.SystemConfigManager;
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
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;
import com.cybersoft4u.xian.iatoms.common.utils.FileUtils;
import com.cybersoft4u.xian.iatoms.common.utils.mail.MailComponent;
import com.cybersoft4u.xian.iatoms.common.utils.mail.TemplateMailMessageDTO;
import com.cybersoft4u.xian.iatoms.services.IReportService;
import com.cybersoft4u.xian.iatoms.services.dao.IReportSettingDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO;
/**
 * Purpose: 完工回覆檔(環匯格式).
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/3/16
 * @MaintenancePersonnel CarrieDuan
 */
public class FinishedReplyReportService extends AtomicService implements IReportService {

	/**
	 * 記錄日誌
	 */
	private static final CafeLog LOGGER	= CafeLogFactory.getLog(FinishedReplyReportService.class);
	
	/**
	 * 发送mail组件
	 */
	private MailComponent mailComponent = null;
	/**
	 * 注入報表發送功能設定DAO
	 */
	private IReportSettingDAO reportSettingDAO;
	/**
	 * 案件類別列表
	 */
	private List<String> ticketTypeList;
	/**
	 * 案件狀態列表
	 */
	private List<String> caseStatusList;
	/**
	 * SRM_案件處理資料檔DAO
	 */
	private ISrmCaseHandleInfoDAO srmCaseHandleInfoDAO;
	
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IFinishedReplyReportService#sendMailFinishedReplyReportToGP()
	 */
	public void sendMailFinishedReplyReportToGP() throws ServiceException {
		sendReportMail(DateTimeUtils.getCurrentDate(), null, IAtomsConstants.LEAVE_CASE_STATUS_SEVEN);
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IReportService#sendReportMail(java.util.Date)
	 */
	public void sendReportMail(Date sendDate, String customerId, String reportCode) throws ServiceException {
		try {
			List<ReportSettingDTO> reportSettingDTOs = this.reportSettingDAO.listByReportCode(customerId, reportCode);
			if (!CollectionUtils.isEmpty(reportSettingDTOs)) {
				Date completeDate = DateTimeUtils.addCalendar(sendDate, 0, 0, -1);
				String complete = DateTimeUtils.toString(completeDate, DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
				List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = null;
				StringBuffer infoBuffer = new StringBuffer();
				String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD);
				String path = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH);
				//郵件主題模板
				String subjectTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.FINISHED_REPLY_TO_GP_SUBJECT_TEMPLATE;
				//郵件內容模板
				String textTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.FINISHED_REPLY_TO_GP_TEXT_TEMPLATE;
				String temptMerchantCode = null;
				String tempSerialNumber= null;
				for (ReportSettingDTO reportSettingDTO : reportSettingDTOs) {
					if (StringUtils.hasText(reportSettingDTO.getRecipient())){
						caseHandleInfoDTOs = this.srmCaseHandleInfoDAO.listFinishedReportToGP(reportSettingDTO.getCompanyId(), complete, ticketTypeList, caseStatusList);
						for (SrmCaseHandleInfoDTO srmCaseHandleInfoDTO : caseHandleInfoDTOs) {
							infoBuffer = new StringBuffer();
							temptMerchantCode = srmCaseHandleInfoDTO.getMerchantCode();
							if (StringUtils.hasText(temptMerchantCode) 
									&& Integer.valueOf(temptMerchantCode.length()) > Integer.valueOf(IAtomsConstants.LEAVE_CASE_STATUS_ELEVEN)) {
								temptMerchantCode = temptMerchantCode.substring(0, 11);
							}
							tempSerialNumber = srmCaseHandleInfoDTO.getSerialNumber();
							/*if (StringUtils.hasText(tempSerialNumber) 
									&& Integer.valueOf(tempSerialNumber.length()) > Integer.valueOf(IAtomsConstants.LEAVE_CASE_STATUS_EIGHT)) {
								tempSerialNumber = tempSerialNumber.substring(0, 8);
							}*/
							//Bug #2443 產出之檔案，不符格式  2017-09-19
							infoBuffer.append(StringUtils.toFixString(17, srmCaseHandleInfoDTO.getRequirementNo(), false, null));
							infoBuffer.append(StringUtils.toFixString(8, srmCaseHandleInfoDTO.getDtid(), false, null));
							infoBuffer.append(StringUtils.toFixString(11, temptMerchantCode, false, null));
							infoBuffer.append(StringUtils.toFixString(10, DateTimeUtils.toString(srmCaseHandleInfoDTO.getCompleteDate(), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH), false, null));
							// Task #3062 設備序號占20
							infoBuffer.append(StringUtils.toFixString(20, tempSerialNumber, false, null));
							infoBuffer.append(IAtomsConstants.MARK_WRAP);
							srmCaseHandleInfoDTO.setDescription(infoBuffer.toString());
						}
						StringBuffer pathBuffer = null;
						String[] attachments = null;
						String fileName = "";
						if (IAtomsConstants.PARAM_GP.equals(reportSettingDTO.getCustomerCode())) {
							fileName = IAtomsConstants.PARAM_GP + i18NUtil.getName(IAtomsConstants.FINISHED_REPLY_TO_GP_SUBJECT_CH_NAME) + IAtomsConstants.MARK_UNDER_LINE + yearMonthDay;
						} else {
							fileName = reportSettingDTO.getCustomerName() + IAtomsConstants.MARK_SPACE  + i18NUtil.getName(IAtomsConstants.FINISHED_REPLY_TO_GP_SUBJECT_CH_NAME) + IAtomsConstants.MARK_UNDER_LINE + yearMonthDay;
						}
						pathBuffer = new StringBuffer();
						pathBuffer.append(path).append(File.separator).append(yearMonthDay).append(File.separator).append(fileName).append(JasperReportCriteriaDTO.REPORT_EXT_NAME_TXT);
						//tempPath = pathBuffer.toString();
						FileUtils.contentToTxt(pathBuffer.toString(), caseHandleInfoDTOs, "BIG5");
						attachments =  new String[]{pathBuffer.toString()};
						//郵件內容
						Map<String, Object> variables = new HashMap<String, Object>();
						variables.put("toMail", reportSettingDTO.getRecipient());
						variables.put("ccMail", reportSettingDTO.getCopy());
						if (IAtomsConstants.PARAM_GP.equals(reportSettingDTO.getCustomerCode())) {
							variables.put("shortName", reportSettingDTO.getCustomerCode());
						} else {
							variables.put("shortName", reportSettingDTO.getCustomerName());
						}
						if (CollectionUtils.isEmpty(caseHandleInfoDTOs)) {
							variables.put("result", i18NUtil.getName(IAtomsConstants.REPORT_RESULT_IS_NULL));
						} else {
							variables.put("result", i18NUtil.getName(IAtomsConstants.REPORT_RESULT_IS_NOT_NULL));
						}
						try{
							this.mailComponent.sendMailTo(null, reportSettingDTO.getRecipient(), subjectTemplate, attachments, textTemplate, variables);
						} catch (Exception e) {
							LOGGER.debug(".sendMailFinishedReplyReportToGP() --> sendMailFinishedReplyReportToGP() is error... ");
						}
						/*if (pathBuffer != null) {
							FileUtils.removeFile(pathBuffer.toString());
						}*/
						 
					}
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error("sendMailFinishedReplyReportToGP() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("sendMailFinishedReplyReportToGP()", "DataAccess Exception:", e);
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
	 * @return the ticketTypeList
	 */
	public List<String> getTicketTypeList() {
		return ticketTypeList;
	}

	/**
	 * @param ticketTypeList the ticketTypeList to set
	 */
	public void setTicketTypeList(List<String> ticketTypeList) {
		this.ticketTypeList = ticketTypeList;
	}

	/**
	 * @return the caseStatusList
	 */
	public List<String> getCaseStatusList() {
		return caseStatusList;
	}

	/**
	 * @param caseStatusList the caseStatusList to set
	 */
	public void setCaseStatusList(List<String> caseStatusList) {
		this.caseStatusList = caseStatusList;
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
