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
import com.cybersoft4u.xian.iatoms.common.utils.ReportExporter;
import com.cybersoft4u.xian.iatoms.common.utils.mail.MailComponent;
import com.cybersoft4u.xian.iatoms.common.utils.mail.TemplateMailMessageDTO;
import com.cybersoft4u.xian.iatoms.services.IReportService;
import com.cybersoft4u.xian.iatoms.services.dao.IReportSettingDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO;
/**
 * Purpose: 完工回覆檔(歐付寶格式)
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/4/25
 * @MaintenancePersonnel CarrieDuan
 */
public class FinishedReplyReportToOFBService extends AtomicService implements IReportService{

	/**
	 * 記錄日誌
	 */
	private static final CafeLog LOGGER	= CafeLogFactory.getLog(FinishedReplyReportToOFBService.class);
	
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
	 * Purpose:完工回覆檔(歐付寶格式)通知
	 * @author CarrieDuan
	 * @throws ServiceException
	 * @return void
	 */
	public void sendMailFinishedReplyReportToOFB() throws ServiceException {
		sendReportMail(DateTimeUtils.getCurrentDate(), null, IAtomsConstants.REPORT_NAME_FINISHED_REPLY_REPORT);
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IReportService#sendReportMail(java.util.Date)
	 */
	public void sendReportMail(Date sendDate, String customerId, String reportCode) throws ServiceException {
		try{
			//獲取報表集合
			List<ReportSettingDTO> reportSettingDTOs = this.reportSettingDAO.listByReportCode(customerId, reportCode);
			Date completeDate = DateTimeUtils.addCalendar(sendDate, 0, 0, -1);
			List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = null;
			//郵件內容
			String mailContext = null;
			//郵件主題模板
			String subjectTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.FINISHED_REPLY_TO_OFB_SUBJECT_TEMPLATE;
			//郵件內容模板
			String textTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.FINISHED_REPLY_TO_OFB_TEXT_TEMPLATE;
			//獲取臨時保存路徑-時間區
			String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD);
			//獲取臨時路徑
			String path = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH);
			JasperReportCriteriaDTO criteria = null;
			StringBuffer pathBuffer = null;
			if (!CollectionUtils.isEmpty(reportSettingDTOs)) {
				for (ReportSettingDTO reportSettingDTO : reportSettingDTOs) {
					String[] attachments = null;
					//如果收件者信息不為空
					if (StringUtils.hasText(reportSettingDTO.getRecipient())){
						caseHandleInfoDTOs = this.srmCaseHandleInfoDAO.listFinishedReportToOFB(reportSettingDTO.getCompanyId(), DateTimeUtils.toString(completeDate, DateTimeUtils.DT_FMT_YYYYMMDD_SLASH), ticketTypeList, caseStatusList);
						/*if (CollectionUtils.isEmpty(caseHandleInfoDTOs)) {
							caseHandleInfoDTOs= new ArrayList<SrmCaseHandleInfoDTO>();
							SrmCaseHandleInfoDTO srmCaseHandleInfoDTO = new SrmCaseHandleInfoDTO();
							caseHandleInfoDTOs.add(srmCaseHandleInfoDTO);
						}*/
						pathBuffer = new StringBuffer();
						pathBuffer.append(path).append(File.separator).append(yearMonthDay).append(File.separator).append(IAtomsConstants.UC_NO_BRM_08090);
						pathBuffer.append(File.separator);
						criteria = new JasperReportCriteriaDTO();
						criteria.setAutoBuildJasper(false);
						criteria.setResult(caseHandleInfoDTOs);
						//設置所需報表的Name
						criteria.setJrxmlName(IAtomsConstants.FINISHED_REPLY_TO_GP_SUBJECT_CH_NAME);
						//設置報表路徑
						criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
						//設置匯出格式
						criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
						//設置報表Name
						StringBuffer fileNameBuffer = new StringBuffer();
						fileNameBuffer.append(reportSettingDTO.getCustomerName());
						fileNameBuffer.append(IAtomsConstants.MARK_SPACE);
						fileNameBuffer.append(i18NUtil.getName(IAtomsConstants.FINISHED_REPLY_TO_TCB_SUBJECT_CH_NAME));
						fileNameBuffer.append(IAtomsConstants.MARK_UNDER_LINE);
						fileNameBuffer.append(yearMonthDay);
						String fileName = fileNameBuffer.toString();
						criteria.setReportFileName(fileName);
						criteria.setSheetName(i18NUtil.getName(IAtomsConstants.FINISHED_REPLY_TO_TCB_SUBJECT_CH_NAME));
						ReportExporter.exportReportToFile(criteria, pathBuffer.toString(), null);
						pathBuffer.append(fileName).append(JasperReportCriteriaDTO.REPORT_EXT_NAME_MSEXCEL);
						attachments = new String[]{pathBuffer.toString()};
						//郵件內容
						Map<String, Object> variables = new HashMap<String, Object>();
						variables.put("reportDate", DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD));
						variables.put("toMail", reportSettingDTO.getRecipient());
						variables.put("ccMail", reportSettingDTO.getCopy());
						variables.put("shortName", reportSettingDTO.getCustomerName());
						if (CollectionUtils.isEmpty(caseHandleInfoDTOs)) {
							variables.put("result", i18NUtil.getName(IAtomsConstants.REPORT_RESULT_IS_NULL));
						} else {
							variables.put("result", i18NUtil.getName(IAtomsConstants.REPORT_RESULT_IS_NOT_NULL));
						}
						try {
							this.mailComponent.sendMailTo(null, reportSettingDTO.getRecipient(), subjectTemplate, attachments, textTemplate, variables);
						} catch (Exception e) {
							LOGGER.error("sendReportMail()---> sendMailTo():", "DataAccess Exception:", e);
						}
					}
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error("sendReportMail() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("sendReportMail()", "DataAccess Exception:", e);
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
