package com.cybersoft4u.xian.iatoms.services.impl;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cafe.core.config.SystemConfigManager;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ReportSettingDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTransactionDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;
import com.cybersoft4u.xian.iatoms.common.utils.ReportExporter;
import com.cybersoft4u.xian.iatoms.common.utils.mail.MailComponent;
import com.cybersoft4u.xian.iatoms.common.utils.mail.TemplateMailMessageDTO;
import com.cybersoft4u.xian.iatoms.services.IReportService;
import com.cybersoft4u.xian.iatoms.services.dao.IReportSettingDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseTransactionDAO;
/**
 * Purpose:案件設備明細（環匯格式）Service
 * @author ElvaHe
 * @since JDK1.6
 * @date 2017/04/26
 * @MaintenancePersonnel ElvaHe
 */ 
public class CaseAssetDetailReportToGPService extends AtomicService implements IReportService {

	/**
	 * 記錄日誌
	 */
	private static final CafeLog LOGGER	= CafeLogFactory.getLog(CaseAssetDetailReportToGPService.class);
	/**
	 * 注入SRM_案件處理記錄DAO
	 */
	private ISrmCaseTransactionDAO srmCaseTransactionDAO;
	/**
	 * 注入報表DAO
	 */
	private IReportSettingDAO reportSettingDAO;
	/**
	 * 注入發送郵件
	 */
	private MailComponent mailComponent;
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IReportService#sendReportMail(cafe.core.context.SessionContext)
	 */
	@Override
	public void sendReportMail(Date sendDate, String customerId, String reportCode) throws ServiceException {
		List<ReportSettingDTO> reportSettingList = null;
		List<SrmCaseTransactionDTO> srmCaseTransactionDTOs = null;
		Date startDate = null;
		//附件的地址
		String path = null;
		//附件路徑
		File fileDir = null;
		try {
			//根據報表編號查詢報表名稱為“11”的所有報表信息
			reportSettingList = this.reportSettingDAO.listByReportCode(customerId, reportCode);
			//若不存在符合的報表信息就結束，否則的話對需要的案件信息進行查詢並發郵件
			if (CollectionUtils.isEmpty(reportSettingList)) {
				return;
			} else {
				//郵件主題模板
				String subjectTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.CASE_ASSET_DETAIL_REPORT_TO_GP_SUBJECT_TEMPLATE;
				//郵件內容模板
				String textTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.CASE_ASSET_DETAIL_REPORT_TO_GP_TEXT_TEMPLATE;
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sendDate);
				int month = calendar.get(calendar.MONDAY)+1;
				int day = calendar.get(calendar.DAY_OF_MONTH);
				//若當前日期為X月1日，則startDate=（X-1）月16日，endDate=當前日；若當前日期為X月16日，則startDate=X月1日,endDate=當前日；
				//當前日為前半月的日期時，設置月份為上個月，日期為16；若當前日為下半月的日期時，設置月份為本月，日期為1
				if(day <= 15){
					calendar.add(calendar.MONTH, -1);
					calendar.set(calendar.DATE, 16);
				} else if (day >= 16){
					calendar.add(calendar.MONTH, 0);
					calendar.set(calendar.DATE, 1);
				}
				//格式化日期：yyyy/mm/dd
				String dateStr= DateTimeUtils.toString(calendar.getTime(), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
				//起始時間
				startDate = DateTimeUtils.toDate(dateStr);
				//修改SendDate的臨時變量
				String tempSendDate = null;
				//修改sendDate 
				if(day <= 15){
					//修改sendDate的時間
					tempSendDate = dateStr.substring(8);
					tempSendDate = String.valueOf(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
				} else if (day >= 16){
					tempSendDate = dateStr.substring(8);
					tempSendDate = "16";
				}
				dateStr = dateStr.substring(0, 8) + tempSendDate;
				sendDate = DateTimeUtils.toDate(dateStr);
				
				//郵件中附件的日期
				String mailCreateDate = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD);
				//附件的地址
				path = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH);
				StringBuffer pathBuffer = new StringBuffer();
				//拼接附件的名稱
				pathBuffer.append(path).append(File.separator).append(mailCreateDate).append(File.separator);
				path = pathBuffer.toString();
				StringBuffer filePathBuffer = null;
				//設置報表Name
				StringBuffer fileNameBuffer = new StringBuffer();
				String[] attachments = null;
				JasperReportCriteriaDTO criteria = new JasperReportCriteriaDTO();
				//List<CaseManagerFormDTO> caseManagerFormDTOs = new ArrayList<CaseManagerFormDTO>();
				//CaseManagerFormDTO caseManagerFormDTO = new CaseManagerFormDTO();
				Map<String, String> subjrXmlNames = new HashMap<String, String>();
				//核檢路徑是否存在，不存在則創建路徑
				fileDir = new File(path);
				//判斷儲存路徑是否存在，若不存在，則重新新建
				if (!fileDir.exists() || !fileDir.isDirectory()) {
					fileDir.mkdirs();
				}
				//郵件內容
				Map<String, Object> variables = new HashMap<String, Object>();
				//收件人
				String toMail = null;
				//副本
				String ccMail = null;
				//遍歷查詢出的報表集合，給相應的收件人和副本發送郵件fileName
				for (ReportSettingDTO reportSettingDTO : reportSettingList) {
					attachments = null;
					filePathBuffer = null;
					//客戶編號
					customerId = reportSettingDTO.getCompanyId();
					if (!StringUtils.isEmpty(customerId)) {
						srmCaseTransactionDTOs = this.srmCaseTransactionDAO.listCaseAssetDetailReportToGP(customerId, startDate, sendDate);
						//依次將查詢結果中的銀聯卡標註位改為漢字
						for (SrmCaseTransactionDTO srmCaseTransactionDTO : srmCaseTransactionDTOs) {
							if (IAtomsConstants.YES.equals(srmCaseTransactionDTO.getIsCup())) {
								srmCaseTransactionDTO.setIsCup("是");
							} else {
								srmCaseTransactionDTO.setIsCup("否");
							}
						}
					}
					//caseManagerFormDTO.setSrmCaseTransactionDTOList(srmCaseTransactionDTOs);
					//caseManagerFormDTOs.add(caseManagerFormDTO);
					//設置子報表跟主報表的關係
					//subjrXmlNames.put(IAtomsConstants.CASE_ASSET_DETAIL_REPORT, IAtomsConstants.CASE_ASSET_DETAIL_REPORT_SUBREPORT_DIR);
					criteria.setAutoBuildJasper(false);
					criteria.setResult(srmCaseTransactionDTOs);
					//設置所需報表的Name
					criteria.setJrxmlName(IAtomsConstants.CASE_ASSET_DETAIL_REPORT);
					//設置報表路徑
					criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
					//設置匯出格式
					criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
					//設置SheetName
					criteria.setSheetName(i18NUtil.getName(IAtomsConstants.CASE_ASSET_DETAIL_REPORT_TO_GP_NAME));
					//拼接附件的名稱
					fileNameBuffer.append(reportSettingDTO.getCustomerName());
					//Bug #2463  拼接附件的名稱 多了個 + ，移除 update by 2017/09/21
					fileNameBuffer.append(IAtomsConstants.MARK_SPACE);
					//根據日期拼接附件的名稱
					if(day <= 15){
						//前半月日期時附件名為X月份上半月
						fileNameBuffer.append(((month - 1)==0?12:(month - 1)) + i18NUtil.getName(IAtomsConstants.CASE_ASSET_DETAIL_REPORT_MAIL_MONTH));
						fileNameBuffer.append(i18NUtil.getName(IAtomsConstants.CASE_ASSET_DETAIL_REPORT_MAIL_SECOND_HALF));
					} else if (day >= 16){
						//後半月日期時附件名為X月份下半月
						fileNameBuffer.append(month +  i18NUtil.getName(IAtomsConstants.CASE_ASSET_DETAIL_REPORT_MAIL_MONTH));
						fileNameBuffer.append(i18NUtil.getName(IAtomsConstants.CASE_ASSET_DETAIL_REPORT_MAIL_FIRST_HALF));
					}
					fileNameBuffer.append(i18NUtil.getName(IAtomsConstants.CASE_ASSET_DETAIL_REPORT_TO_GP_NAME));
					fileNameBuffer.append(IAtomsConstants.MARK_UNDER_LINE);
					fileNameBuffer.append(mailCreateDate);
					String fileName = fileNameBuffer.toString();
					criteria.setReportFileName(fileName);
					//匯出附件
					ReportExporter.exportReportToFile(criteria, subjrXmlNames, path, null);
					filePathBuffer = new StringBuffer();
					filePathBuffer.append(path).append(File.separator);
					filePathBuffer.append(fileName).append(JasperReportCriteriaDTO.REPORT_EXT_NAME_MSEXCEL);
					attachments = new String[]{filePathBuffer.toString()};
					//獲取收件人和副本
					toMail = reportSettingDTO.getRecipient();
					ccMail = reportSettingDTO.getCopy();
					//當存在收件人或副本時，發送郵件；否則的話跳出循環
					if ((!StringUtils.isEmpty(toMail)) || (!StringUtils.isEmpty(ccMail))) {
						//設置的郵件的值：時間、收件人、副本、郵件主題
						variables.put("mailCreateDate", DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD));
						variables.put("toMail", toMail);
						variables.put("ccMail", ccMail);
						variables.put("mailSubject", reportSettingDTO.getCustomerName());
						if (CollectionUtils.isEmpty(srmCaseTransactionDTOs)) {
							variables.put("result", i18NUtil.getName(IAtomsConstants.REPORT_RESULT_IS_NULL));
						} else {
							variables.put("result", i18NUtil.getName(IAtomsConstants.REPORT_RESULT_IS_NOT_NULL));
						}
						try {
							this.mailComponent.sendMailTo(null, reportSettingDTO.getRecipient(), subjectTemplate, attachments, textTemplate, variables);
						} catch (Exception e) {
							LOGGER.error("sendReportMail()---> sendMailTo():", "DataAccess Exception:", e);
						}
					} else {
						break;
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("sendReportMail()", "is error :", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		} finally {
			//刪除本地匯出的附件
			if (!StringUtils.isEmpty(path)) {
				fileDir = new File(path);
				if (fileDir.exists()) {
					fileDir.delete();
				}
			}
		}
	}
	
	/**
	 * Purpose:系统需于每月1號和16號，01:30，发送提醒邮件，提醒案件設備明細(環匯格式)
	 * @author ElvaHe
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return void
	 */
	public void sendMailCaseAssetDetailReportToGP() throws ServiceException {
		sendReportMail(DateTimeUtils.getCurrentDate(), null, IAtomsConstants.REPORT_NAME_CASE_ASSET_DETAIL);
	}
	
	/**
	 * @return the srmCaseTransactionDAO
	 */
	public ISrmCaseTransactionDAO getSrmCaseTransactionDAO() {
		return srmCaseTransactionDAO;
	}

	/**
	 * @param srmCaseTransactionDAO the srmCaseTransactionDAO to set
	 */
	public void setSrmCaseTransactionDAO(
			ISrmCaseTransactionDAO srmCaseTransactionDAO) {
		this.srmCaseTransactionDAO = srmCaseTransactionDAO;
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
	
}
