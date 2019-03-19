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
 * Purpose: 完工回覆檔(大眾格式).
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2017/5/8
 * @MaintenancePersonnel HermanWang
 */
public class FinishedReplyReportTCBService  extends AtomicService implements IReportService {
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
	 * 無參的構造方法 
	 */
	public FinishedReplyReportTCBService() {
	}
	/**
	 * Purpose:系统需于每日3:30，发送提醒邮件，提醒完工回覆檔(大眾格式).
	 * @author HermanWang
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return void
	 */
	public void sendMailFinishedReplyReportToTCB() throws ServiceException {
		sendReportMail(DateTimeUtils.getCurrentDate(), null, IAtomsConstants.LEAVE_CASE_STATUS_EIGHT);
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IReportService#sendReportMail(java.util.Date)
	 */
	@Override
	public void sendReportMail(Date sendDate, String customerId, String reportCode) throws ServiceException {
		StringBuffer pathBuffer = null;
		try {
			List<ReportSettingDTO> reportSettingDTOs = this.reportSettingDAO.listByReportCode(customerId, reportCode);
			if (!CollectionUtils.isEmpty(reportSettingDTOs)) {
				//SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
				Date completeDate = DateTimeUtils.addCalendar(sendDate, 0, 0, -1);
				//String complete = sf.format(completeDate);
				List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = null;
				StringBuffer infoBuffer = new StringBuffer();
				String path = null;
				String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD);
				//郵件主題模板
				String subjectTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.FINISHED_REPLY_TO_TCB_SUBJECT_TEMPLATE;
				//郵件內容模板
				String textTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.FINISHED_REPLY_TO_TCB_TEXT_TEMPLATE;
				for (ReportSettingDTO reportSettingDTO : reportSettingDTOs) {
					path = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH);
					infoBuffer = new StringBuffer();
					caseHandleInfoDTOs = this.srmCaseHandleInfoDAO.listFinishedReportToTCB(reportSettingDTO.getCompanyId(), DateTimeUtils.toString(completeDate, DateTimeUtils.DT_FMT_YYYYMMDD_SLASH), ticketTypeList, caseStatusList);
					if(caseHandleInfoDTOs != null) {
						int i = 1;
						for (SrmCaseHandleInfoDTO srmCaseHandleInfoDTO : caseHandleInfoDTOs) {
							//序號，從1 開始 累加
							infoBuffer.append(i).append(IAtomsConstants.MARK_SEPARATOR);
							//特店名稱
							infoBuffer.append(StringUtils.hasText(srmCaseHandleInfoDTO.getMerchantName()) ? srmCaseHandleInfoDTO.getMerchantName().trim() : IAtomsConstants.MARK_EMPTY_STRING).append(IAtomsConstants.MARK_SEPARATOR);
							//案件類別
							infoBuffer.append(StringUtils.hasText(srmCaseHandleInfoDTO.getCaseCategoryName()) ? srmCaseHandleInfoDTO.getCaseCategoryName().trim() : IAtomsConstants.MARK_EMPTY_STRING).append(IAtomsConstants.MARK_SEPARATOR);
							//特店代號
							infoBuffer.append(StringUtils.hasText(srmCaseHandleInfoDTO.getMerchantCode()) ? srmCaseHandleInfoDTO.getMerchantCode().trim() : IAtomsConstants.MARK_EMPTY_STRING).append(IAtomsConstants.MARK_SEPARATOR);
							//DTID
							infoBuffer.append(StringUtils.hasText(srmCaseHandleInfoDTO.getDtid()) ? srmCaseHandleInfoDTO.getDtid().trim() : IAtomsConstants.MARK_EMPTY_STRING).append(IAtomsConstants.MARK_SEPARATOR);
							//完修日期
							infoBuffer.append(StringUtils.hasText(srmCaseHandleInfoDTO.getCompleteDate().toString()) ? DateTimeUtils.toString(srmCaseHandleInfoDTO.getCompleteDate(), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH) :IAtomsConstants.MARK_EMPTY_STRING);
							//回車換行
							infoBuffer.append(IAtomsConstants.RETURN_LINE_FEED);
							i++;
						}
					}
					String[] attachments = null;
					//if (infoBuffer.length() != 0) {
					StringBuffer fileNameBuffer = new StringBuffer();
					fileNameBuffer.append(reportSettingDTO.getCustomerName());
					fileNameBuffer.append(IAtomsConstants.MARK_SPACE);
					fileNameBuffer.append(i18NUtil.getName(IAtomsConstants.FINISHED_REPLY_TO_TCB_SUBJECT_CH_NAME));
					fileNameBuffer.append(IAtomsConstants.MARK_UNDER_LINE);
					fileNameBuffer.append(yearMonthDay);
					String fileName = fileNameBuffer.toString();
					pathBuffer = new StringBuffer();
					pathBuffer.append(path).append(File.separator).append(yearMonthDay).append(File.separator).append(fileName).append(JasperReportCriteriaDTO.REPORT_EXT_NAME_TXT);
					path = pathBuffer.toString();
					FileUtils.contentToTxt(path, infoBuffer.toString());
					attachments =  new String[]{pathBuffer.toString()};
					//}
					//郵件內容
					Map<String, Object> variables = new HashMap<String, Object>();
					variables.put("toMail", reportSettingDTO.getRecipient());
					variables.put("ccMail", reportSettingDTO.getCopy());
					variables.put("shortName", reportSettingDTO.getCustomerName());
					variables.put("yearMonthDay", yearMonthDay);
					if (CollectionUtils.isEmpty(caseHandleInfoDTOs)) {
						variables.put("result", i18NUtil.getName(IAtomsConstants.REPORT_RESULT_IS_NULL));
					} else {
						variables.put("result", i18NUtil.getName(IAtomsConstants.REPORT_RESULT_IS_NOT_NULL));
					}
					this.mailComponent.sendMailTo(null, reportSettingDTO.getRecipient(), subjectTemplate, attachments, textTemplate, variables);
					if (pathBuffer != null) {
						FileUtils.removeFile(pathBuffer.toString());
					}
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error("sendMailFinishedReplyReportToTCB() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("sendMailFinishedReplyReportToTCB()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		} finally {
			if(!StringUtils.isEmpty(pathBuffer)) {
				File tempFile = new File(pathBuffer.toString());
				if(tempFile.exists()) {
					tempFile.delete();
				}
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
