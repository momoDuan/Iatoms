package com.cybersoft4u.xian.iatoms.services.impl;

import java.io.File;
import java.text.ParseException;
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
 * Purpose:案件報修明細（環匯格式）報表
 * @author ElvaHe
 * @since jdk1.6
 * @date 2017年4月17日
 * @MaintenancePersonnel ElvaHe
 *
 */
public class CaseRepairDetailReportToGPService extends AtomicService implements IReportService {

	/**
	 * 記錄日誌
	 */
	private static final CafeLog LOGGER	= CafeLogFactory.getLog(CaseRepairDetailReportToGPService.class);
	
	/**
	 * 注入報表發送功能設定DAO
	 */
	private IReportSettingDAO reportSettingDAO;
	
	/**
	 * 注入SRM_案件處理記錄DAO
	 */
	private ISrmCaseTransactionDAO srmCaseTransactionDAO;
	
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
		List<SrmCaseTransactionDTO> srmCaseTransactionList = null;
		Date startDate = null;
		JasperReportCriteriaDTO criteria = new JasperReportCriteriaDTO();
		//郵件發送時間
		String mailCreateDate = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD);
		//附件的路徑
		String path = null;
		File fileDir = null;
		try {
			//查詢報表名稱為“案件報修明細（環匯格式）”的所有報表信息
			reportSettingList = this.reportSettingDAO.listByReportCode(customerId, reportCode);
			//不存在報表名稱為“案件報修明細（環匯格式）”的報表時返回，否則的話發送郵件
			if(CollectionUtils.isEmpty(reportSettingList)){
				return;
			} else {
				//郵件主題模板
				String subjectTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.CASE_REPAIR_DETAIL_REPORT_TO_GP_SUBJECT_TEMPLATE;
				//郵件內容模板
				String textTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.CASE_REPAIR_DETAIL_REPORT_TO_GP_TEXT_TEMPLATE;
				//附件的路徑
				path = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH);
				StringBuffer pathBuffer = new StringBuffer();
				//拼接存放附件的路徑
				pathBuffer.append(path).append(File.separator).append(mailCreateDate).append(File.separator);
				path = pathBuffer.toString();
				//核檢路徑是否存在，不存在則創建路徑
				fileDir = new File(path);
				//判斷儲存路徑是否存在，若不存在，則重新新建
				if (!fileDir.exists() || !fileDir.isDirectory()) {
					fileDir.mkdirs();
				}
				//郵件內容
				Map<String, Object> variables = new HashMap<String, Object>();
				//List<CaseManagerFormDTO> caseManagerFormDTOs = new ArrayList<CaseManagerFormDTO>();
				//案件formDTO
				//CaseManagerFormDTO caseManagerFormDTO = new CaseManagerFormDTO();
				//設置子報表的對象
				Map<String, String> subjrXmlNames = new HashMap<String, String>();
				//設置報表Name
				StringBuffer fileNameBuffer = new StringBuffer();
				StringBuffer filePathBuffer = null;
				String[] attachments = null;
				//收件人
				String toMail = null;
				//副本
				String ccMail = null;
				//新增map<caseId, SrmCaseTransactionDTO>用來清空重複的欄位
				Map<String, SrmCaseTransactionDTO> srmCaseTransactionDTOMap = null;
				//遍歷該報表的報表信息集合給相應的人員發送郵件
				for (ReportSettingDTO reportSettingDTO : reportSettingList) {
					attachments = null;
					filePathBuffer = null;
					//客戶編號
					customerId = reportSettingDTO.getCompanyId();
					//設置日期的day為01
					Date temDate = this.updateDay(sendDate);
					//存在發送時間
					if(temDate != null){
						//設置查詢條件的開始時間
						startDate = DateTimeUtils.addCalendar(temDate, 0, -1, 0);
						//存在客戶編號時對案件信息進行查詢
						if (!StringUtils.isEmpty(customerId)) {
							//查詢符合條件的案件信息
							srmCaseTransactionList = this.srmCaseTransactionDAO.listCaseRepairDetailReportToGP(customerId, startDate, temDate, IAtomsConstants.CASE_CATEGORY.REPAIR.getCode());
							srmCaseTransactionDTOMap = new HashMap<String, SrmCaseTransactionDTO>();
							//清空重複欄位--update by 2017-09-20
							for (SrmCaseTransactionDTO srmCaseTransactionDTO : srmCaseTransactionList) {
								if (srmCaseTransactionDTOMap.containsKey(srmCaseTransactionDTO.getCaseId())) {
									srmCaseTransactionDTO.setRequirementNo(null);
									srmCaseTransactionDTO.setCaseCategoryName(null);
									srmCaseTransactionDTO.setCaseStatus(null);
									srmCaseTransactionDTO.setMerchantCode(null);
									srmCaseTransactionDTO.setMerchantName(null);
									srmCaseTransactionDTO.setDtid(null);
									srmCaseTransactionDTO.setCreatedDate(null);
									srmCaseTransactionDTO.setDescription(null);
								} else {
									srmCaseTransactionDTOMap.put(srmCaseTransactionDTO.getCaseId(), srmCaseTransactionDTO);
								}
							}
							srmCaseTransactionDTOMap = null;
						}
					}
					//若存在符合條件的案件信息集合就將查詢結果設置到附件使用的集合中
					//caseManagerFormDTO.setSrmCaseTransactionDTOList(srmCaseTransactionList);
					//caseManagerFormDTOs.add(caseManagerFormDTO);
					//修改主報表中的子報表
					//subjrXmlNames.put(IAtomsConstants.CASE_REPAIR_DETAIL_REPORT, IAtomsConstants.CASE_REPAIR_DETAIL_REPORT_SUBREPORT_DIR);
					criteria.setAutoBuildJasper(false);
					criteria.setResult(srmCaseTransactionList);
					//設置所需報表的Name
					//update by hermanwang 2017/07/28 
					criteria.setJrxmlName(IAtomsConstants.CASE_REPAIR_DETAIL_REPORT_TO_GP_SUBREPORTS);
					//設置報表路徑
					criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
					//設置匯出格式
					criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
					criteria.setSheetName(i18NUtil.getName(IAtomsConstants.CASE_REPAIR_DETAIL_REPORT_TO_GP_NAME));
					//拼接郵件中附件的名字
					fileNameBuffer.append(reportSettingDTO.getCustomerName());
					fileNameBuffer.append(IAtomsConstants.MARK_SPACE);
					fileNameBuffer.append(i18NUtil.getName(IAtomsConstants.CASE_REPAIR_DETAIL_REPORT_TO_GP_NAME));
					fileNameBuffer.append(IAtomsConstants.MARK_UNDER_LINE);
					fileNameBuffer.append(mailCreateDate);
					String fileName = fileNameBuffer.toString();
					//設置附件名
					criteria.setReportFileName(fileName);
					//匯出附件
					ReportExporter.exportReportToFile(criteria, subjrXmlNames, path, null);
					//附件的格式
					filePathBuffer = new StringBuffer();
					filePathBuffer.append(path).append(File.separator);
					filePathBuffer.append(fileName).append(JasperReportCriteriaDTO.REPORT_EXT_NAME_MSEXCEL);
					attachments = new String[]{filePathBuffer.toString()};
					//獲取收件人和副本
					toMail = reportSettingDTO.getRecipient();
					ccMail = reportSettingDTO.getCopy();
					//當存在收件人或副本時，發送郵件；否則的話跳出循環
					if ((!StringUtils.isEmpty(toMail)) || (!StringUtils.isEmpty(ccMail))) {
						//將發送時間、收件人、副本及郵件內容設置到郵件組建中
						variables.put("mailCreateDate", DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD));
						variables.put("toMail", toMail);
						variables.put("ccMail", ccMail);
						variables.put("mailSubject", reportSettingDTO.getCustomerName());
						if (CollectionUtils.isEmpty(srmCaseTransactionList)) {
							variables.put("result", i18NUtil.getName(IAtomsConstants.REPORT_RESULT_IS_NULL));
						} else {
							variables.put("result", i18NUtil.getName(IAtomsConstants.REPORT_RESULT_IS_NOT_NULL));
						}
						try {
							this.mailComponent.sendMailTo(null, toMail, subjectTemplate, attachments, textTemplate, variables);
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
	 * Purpose:系统需于每月第一天 00:30，发送提醒邮件，提醒案件報修明細(環匯格式)
	 * @author ElvaHe
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return void
	 */
	public void sendMailCaseRepairDetailReportToGP() throws ServiceException {
		sendReportMail(DateTimeUtils.getCurrentDate(), null, IAtomsConstants.REPORT_NAME_CASE_REPAIR_DETAIL_REPORT);
	}
	
	/**
	 * Purpose:設置參數的日期為01
	 * @param sendDate：要設置的日期
	 * @return Date 修改后的日期
	 * @throws ServiceException:出錯時拋出ServiceException
	 */
	private Date updateDay(Date sendDate) throws ServiceException {
		Date updatedDate = null;
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sendDate);
			//設置日期為該月的第一天
			calendar.set(calendar.DAY_OF_MONTH, 1);
			//格式日期時間為yyyy/MM/dd
			String dateStr = DateTimeUtils.toString(calendar.getTime(), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
			updatedDate = DateTimeUtils.toDate(dateStr);
		} catch (ParseException e) {
			LOGGER.error("CaseRepairDetailReportToGPService.updateDay()", "is error ", e);
			e.printStackTrace();
		}
		return updatedDate;
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
