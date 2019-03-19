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
import com.cybersoft4u.xian.iatoms.common.utils.ReportExporter;
import com.cybersoft4u.xian.iatoms.common.utils.mail.MailComponent;
import com.cybersoft4u.xian.iatoms.common.utils.mail.TemplateMailMessageDTO;
import com.cybersoft4u.xian.iatoms.services.IReportService;
import com.cybersoft4u.xian.iatoms.services.dao.IReportSettingDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO;
/**
 * Purpose: 未完修報表(大眾格式)
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/3/10
 * @MaintenancePersonnel CarrieDuan
 */
public class UnfinishedReportService extends AtomicService  implements IReportService {

	/**
	 * 記錄日誌
	 */
	private static final CafeLog LOGGER	= CafeLogFactory.getLog(UnfinishedReportService.class);
	
	/**
	 * 发送mail组件
	 */
	private MailComponent mailComponent = null;
	/**
	 * 注入報表發送功能設定DAO
	 */
	private IReportSettingDAO reportSettingDAO;
	/**
	 * SRM_案件處理資料檔DAO
	 */
	private ISrmCaseHandleInfoDAO srmCaseHandleInfoDAO;
	/**
	 * 案件類別列表
	 */
	private List<String> ticketTypeList;
	/**
	 * 案件狀態列表
	 */
	private List<String> caseStatusList;
	/**
	 * 
	 */
	private List<String> installedCaseCategorys;
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IUnfinishedReportService#sendMailUnfinishedReportToTCB()
	 */
	public void sendMailUnfinishedReportToTCB() throws ServiceException {
		sendReportMail(DateTimeUtils.getCurrentDate(), null, IAtomsConstants.LEAVE_CASE_STATUS_SIX);
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IReportService#sendReportMail(java.util.Date)
	 */
	public void sendReportMail(Date sendDate, String customerId, String reportCode) throws ServiceException {
		StringBuffer filePathBuffer = null;
		try {
			List<ReportSettingDTO> reportSettingDTOs = this.reportSettingDAO.listByReportCode(customerId, reportCode);
			if (!CollectionUtils.isEmpty(reportSettingDTOs)) {
				List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = null;
				JasperReportCriteriaDTO criteria = new JasperReportCriteriaDTO();
				Date acceptableFinishDate = DateTimeUtils.toTimestamp(DateTimeUtils.toString(sendDate, DateTimeUtils.DT_FMT_YYYYMMDD_SLASH));
				String[] attachments = null;
				String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD);
				String path = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH);
				StringBuffer pathBuffer = new StringBuffer();
				pathBuffer.append(path).append(File.separator).append(yearMonthDay).append(File.separator);
				path = pathBuffer.toString();
				//核檢路徑是否存在，不存在則創建路徑
				File fileDir = new File(path);
				//判斷儲存路徑是否存在，若不存在，則重新新建
				if (!fileDir.exists() || !fileDir.isDirectory()) {
					fileDir.mkdirs();
				}
				for (ReportSettingDTO reportSettingDTO : reportSettingDTOs) {
					if (StringUtils.hasText(reportSettingDTO.getRecipient())) {
						attachments = null;
						filePathBuffer = null;
						caseHandleInfoDTOs = this.srmCaseHandleInfoDAO.listUnfinishedReportToTCB(reportSettingDTO.getCompanyId(), acceptableFinishDate, ticketTypeList, caseStatusList, true, installedCaseCategorys);
						criteria.setAutoBuildJasper(false);
						criteria.setResult(caseHandleInfoDTOs);
						//設置所需報表的Name
						criteria.setJrxmlName(IAtomsConstants.UN_FINISHED_REPORT);
						//criteria.set
						//設置報表路徑
						criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
						//設置匯出格式
						criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
						criteria.setSheetName(i18NUtil.getName(IAtomsConstants.UN_FINISHED_REPORT_CH_NAME));
						//設置報表Name
						StringBuffer fileNameBuffer = new StringBuffer();
						fileNameBuffer.append(reportSettingDTO.getCustomerName());
						fileNameBuffer.append(IAtomsConstants.MARK_SPACE);
						fileNameBuffer.append(i18NUtil.getName(IAtomsConstants.UN_FINISHED_REPORT_CH_NAME));
						fileNameBuffer.append(IAtomsConstants.MARK_UNDER_LINE);
						fileNameBuffer.append(yearMonthDay);
						String fileName = fileNameBuffer.toString();
						criteria.setReportFileName(fileName);
						ReportExporter.exportReportToFile(criteria, path, null);
						filePathBuffer = new StringBuffer();
						filePathBuffer.append(path).append(File.separator);
						filePathBuffer.append(fileName).append(JasperReportCriteriaDTO.REPORT_EXT_NAME_MSEXCEL);
						attachments = new String[]{filePathBuffer.toString()};
						//郵件內容
						String mailContext = null;
						//郵件主題模板
						String subjectTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.UN_FINISHED_REPORT_TO_TCB_SUBJECT;
						//郵件內容模板
						String textTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.UN_FINISHED_REPORT_TO_TCB_CONTEXT;
						//郵件內容
						Map<String, Object> variables = new HashMap<String, Object>();
						variables.put("expectedCompletionDate", DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMMDD));
						variables.put("toMail", reportSettingDTO.getRecipient());
						variables.put("ccMail", reportSettingDTO.getCopy());
						variables.put("customerName", reportSettingDTO.getCustomerName());
						if (CollectionUtils.isEmpty(caseHandleInfoDTOs)) {
							variables.put("result", i18NUtil.getName(IAtomsConstants.REPORT_RESULT_IS_NULL));
						} else {
							variables.put("result", i18NUtil.getName(IAtomsConstants.REPORT_RESULT_IS_NOT_NULL));
						}
						try {
							this.mailComponent.sendMailTo(null, reportSettingDTO.getRecipient(), subjectTemplate, attachments, textTemplate, variables);
						} catch (Exception e) {
							LOGGER.error("sendMailUnfinishedReportToTCB()-->sendMailTo(): ", "DataAccess Exception:", e);
						}
						
					}
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error("sendMailUnfinishedReportToTCB() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("sendMailUnfinishedReportToTCB()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		} finally {
			try {
				FileUtils.removeFile(filePathBuffer.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
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
	 * @return the installedCaseCategorys
	 */
	public List<String> getInstalledCaseCategorys() {
		return installedCaseCategorys;
	}

	/**
	 * @param installedCaseCategorys the installedCaseCategorys to set
	 */
	public void setInstalledCaseCategorys(List<String> installedCaseCategorys) {
		this.installedCaseCategorys = installedCaseCategorys;
	}

	
	
}
