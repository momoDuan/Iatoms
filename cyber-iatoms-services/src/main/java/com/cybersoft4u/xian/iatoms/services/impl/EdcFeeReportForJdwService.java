package com.cybersoft4u.xian.iatoms.services.impl;

import java.io.File;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;

import cafe.core.config.SystemConfigManager;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.convert.SimpleDtoDmoTransformer;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BaseParameterItemDefDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ReportSettingDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;
import com.cybersoft4u.xian.iatoms.common.reportsetting.AssetMaintainFeeSetting;
import com.cybersoft4u.xian.iatoms.common.reportsetting.WorkFeeSetting;
import com.cybersoft4u.xian.iatoms.common.utils.FileUtils;
import com.cybersoft4u.xian.iatoms.common.utils.ReportExporter;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;
import com.cybersoft4u.xian.iatoms.common.utils.mail.MailComponent;
import com.cybersoft4u.xian.iatoms.common.utils.mail.TemplateMailMessageDTO;
import com.cybersoft4u.xian.iatoms.services.IReportService;
import com.cybersoft4u.xian.iatoms.services.dao.IBaseParameterManagerDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IContractDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryMonthlyDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IIAtomsBaseParameterTypeDefDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IReportSettingDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BaseParameterItemDef;
import com.cybersoft4u.xian.iatoms.services.dmo.BaseParameterItemDefId;
/**
 * Purpose: 维护费报表(捷达威格式)
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/8/22
 * @MaintenancePersonnel CarrieDuan
 */
public class EdcFeeReportForJdwService extends AtomicService implements IReportService,ApplicationContextAware {
	/**
	 * 記錄日誌
	 */
	private static final CafeLog LOGGER	= CafeLogFactory.getLog(EdcFeeReportForJdwService.class);
	
	/**
	 * 优惠合约
	 */
	private Map<String, List<String>> contractListFree;
	/**
	 * 旧合约
	 */
	private Map<String, List<String>> contractListOld;
	/**
	 * 案件狀態
	 */
	private List<String> caseStatus;
	/**
	 * 一般交易參數
	 */
	private List<String> commonTransactionType;
	/**
	 * 設備狀態
	 */
	private List<String> assetStatus;
	/**
	 * 
	 */
	private AssetMaintainFeeSetting assetMaintainFeeSetting;
	/**
	 * 
	 */
	private List<WorkFeeSetting> workFeeSetting;
	/**
	 * 案件正在處理中DAO
	 */
	private ISrmCaseHandleInfoDAO caseHandleInfoDAO;
	/**
	 * 发送mail组件
	 */
	private MailComponent mailComponent;
	/**
	 * 注入報表發送功能設定DAO
	 */
	private IReportSettingDAO reportSettingDAO;
	/**
	 * 公司DAO
	 */
	private ICompanyDAO companyDAO;
	/**
	 * 設備月檔
	 */
	private IDmmRepositoryHistoryMonthlyDAO dmmRepositoryHistoryMonthlyDAO;
	/**
	 * 系統參數維護DAO接口
	 */
	private IBaseParameterManagerDAO baseParameterManagerDAO;
	/**
	 * 
	 */
	private IIAtomsBaseParameterTypeDefDAO iAtomsBaseParameterTypeDefDAO;
	/**
	 * 合約編號
	 */
	private IContractDAO contractDAO;
	/**
	 * 
	 */
	private Map<String, String> cnMonth;
	/**
	 * 
	 */
	private ApplicationContext applicationContext;
	/**
	 * 
	 * Purpose:
	 * @author CarrieDuan
	 * @throws ServiceException
	 * @return void
	 */
	public void toReport() throws ServiceException {
		CompanyDTO companyDTO = this.companyDAO.getCompanyByCompanyCode(IAtomsConstants.PARAMTER_COMPANY_CODE_JDW_EDC);
		if (companyDTO != null) {
			sendReportMail(DateTimeUtils.getCurrentDate(), companyDTO.getCompanyId(), IAtomsConstants.REPORT_NAME_EDC_FEE_FOR_JDW);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IEdcFeeReportForJdwService#sendReportMail()
	 */
	@Override
	public void sendReportMail(Date sendDate, String customerId, String reportCode) throws ServiceException {
		StringBuffer filePathBuffer = new StringBuffer();
		try{
			//查詢EDC提示報表的信息
			List<ReportSettingDTO> reportSettingList = this.reportSettingDAO.getDetailList(customerId, reportCode);
			if (!CollectionUtils.isEmpty(reportSettingList)){
				//獲取查詢開始時間,上個月1號
				Calendar calStart=Calendar.getInstance();
				calStart.setTime(sendDate);
				calStart.add(Calendar.MONTH, -1);
				calStart.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
				Timestamp startDate = DateTimeUtils.toTimestamp(DateTimeUtils.toString(calStart.getTime(), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH));
				//獲取查詢截止時間,本個月1號
				Calendar calEnd=Calendar.getInstance();
				calEnd.setTime(sendDate);
				calEnd.set(Calendar.DAY_OF_MONTH, 1);
				Timestamp endDate = DateTimeUtils.toTimestamp(DateTimeUtils.toString(calEnd.getTime(), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH));
				//查詢作業明細
				List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = this.caseHandleInfoDAO.listComplateCaseList(customerId, caseStatus, commonTransactionType, startDate, endDate);
				List<JasperReportCriteriaDTO> criterias = new ArrayList<JasperReportCriteriaDTO>();
				if (!CollectionUtils.isEmpty(caseHandleInfoDTOs)) {
					//計算第一行台數
					Integer tempNumber = 0;
					Integer firstTmsNumber = 0;
					Integer number = 0;
					Integer freeTotal = 0;
					Integer srplusFreeFrequency = 0;
					for (SrmCaseHandleInfoDTO srmCaseHandleInfoDTO : caseHandleInfoDTOs) {
						if (IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())
								|| IAtomsConstants.CASE_CATEGORY.MERGE.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())
								|| IAtomsConstants.CASE_CATEGORY.UPDATE.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())){
							tempNumber += 1;
						}
						if (IAtomsConstants.YES.equals(srmCaseHandleInfoDTO.getIsFirstInstalled()) 
								&& IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())) {
							if (!CollectionUtils.isEmpty(contractListFree) && contractListFree.containsKey(IAtomsConstants.PARAMTER_COMPANY_CODE_JDW_EDC)) {
								if (!CollectionUtils.isEmpty(contractListFree.get(IAtomsConstants.PARAMTER_COMPANY_CODE_JDW_EDC))
										&& contractListFree.get(IAtomsConstants.PARAMTER_COMPANY_CODE_JDW_EDC).contains(srmCaseHandleInfoDTO.getContractCode())) {
									firstTmsNumber += 1;
								}
							}
						}
					}
					number = (tempNumber - firstTmsNumber < 0 ? 0 : tempNumber - firstTmsNumber);
					//計算第一行免费TMS次数
					String monthYear = DateTimeUtils.toString(startDate, DateTimeUtils.DT_FMT_YYYYMM);
					Integer tmsTotal = this.dmmRepositoryHistoryMonthlyDAO.getTmsTotal(monthYear, contractListFree.get(IAtomsConstants.PARAMTER_COMPANY_CODE_JDW_EDC), customerId);
					if (tmsTotal != null && tmsTotal != 0) {
						tmsTotal = tmsTotal * 2;
					}
					//計算第一行剩余免费次数
					Integer freeTimes = this.calculateFreeTimes(IATOMS_PARAM_TYPE.FREE_REMAIN_TIME.getCode(), number, startDate, "剩余免费次数");
					srplusFreeFrequency = freeTimes - number > 0 ? freeTimes - number : 0;
					freeTotal += srplusFreeFrequency;
					//獲取第一行的备注
					List<BimContractDTO> bimContractDTOs = this.contractDAO.listBy(customerId, null, null, null, null, -1, -1);
					StringBuffer contractInfo = new StringBuffer();
					StringBuffer remark = new StringBuffer();
					if (!CollectionUtils.isEmpty(bimContractDTOs)) {
						for (BimContractDTO bimContractDTO : bimContractDTOs) {
							if (StringUtils.hasText(bimContractDTO.getComment())) {
								contractInfo.append(bimContractDTO.getContractCode()).append(IAtomsConstants.MARK_COLON)
									.append(bimContractDTO.getComment()).append(IAtomsConstants.MARK_WRAP);
							}
						}
					}
					remark.append(contractInfo);
					remark.append(i18NUtil.getName(IAtomsMessageCode.EDC_FEE_REPORT_FOR_JDW_MESSAGE, 
							new String[]{Integer.toString(calStart.get(Calendar.MONTH))=="0"?"12":Integer.toString(calStart.get(Calendar.MONTH)), Integer.toString(freeTimes)}, null));
					remark.append(IAtomsConstants.MARK_WRAP);
					remark.append(i18NUtil.getName(IAtomsMessageCode.EDC_FEE_REPORT_FOR_JDW_IS_FIRST_INSTALL));
					//將第一行的值賦值到workFeeSetting列表中
					WorkFeeSetting feeSetting = workFeeSetting.get(0);
					feeSetting.setNumber(number);
					feeSetting.setFreeTmsFrequency(DecimalFormat.getNumberInstance().format(tmsTotal));
					feeSetting.setSurplusFreeFrequency(DecimalFormat.getNumberInstance().format(srplusFreeFrequency));
					feeSetting.setRemarks(remark.toString());
					workFeeSetting.set(0, feeSetting);
					firstTmsNumber = 0;
					number = 0;
					//計算第二行台數
					for (SrmCaseHandleInfoDTO srmCaseHandleInfoDTO : caseHandleInfoDTOs) {
						if (IAtomsConstants.CASE_CATEGORY.REPAIR.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())){
							if (!CollectionUtils.isEmpty(contractListOld)
									&& contractListOld.containsKey(IAtomsConstants.PARAMTER_COMPANY_CODE_JDW_EDC)) {
								if (!CollectionUtils.isEmpty(contractListOld.get(IAtomsConstants.PARAMTER_COMPANY_CODE_JDW_EDC))
										&& contractListOld.get(IAtomsConstants.PARAMTER_COMPANY_CODE_JDW_EDC).contains(srmCaseHandleInfoDTO.getContractCode()))
								number += 1;
								continue;
							}
						}
					}
					//計算第二行剩余免费次数
					freeTimes = this.calculateFreeTimes(IATOMS_PARAM_TYPE.OLD_REMAIN_TIME.getCode(), number, startDate, "剩余免费次数(舊合約)");
					srplusFreeFrequency = freeTimes - number > 0 ? freeTimes - number : 0;
					freeTotal += srplusFreeFrequency;
					//獲取第二行的备注
					remark = remark.delete(0, remark.length());
					remark.append(contractInfo);
					remark.append(i18NUtil.getName(IAtomsMessageCode.EDC_FEE_REPORT_FOR_JDW_MESSAGE, 
									new String[]{Integer.toString(calStart.get(Calendar.MONTH))=="0"?"12":Integer.toString(calStart.get(Calendar.MONTH)), Integer.toString(freeTimes)}, null));
					//將第二行的值賦值到workFeeSetting列表中
					feeSetting = workFeeSetting.get(1);
					feeSetting.setNumber(number);
					feeSetting.setFreeTmsFrequency(DecimalFormat.getNumberInstance().format(tmsTotal/2));
					feeSetting.setSurplusFreeFrequency(DecimalFormat.getNumberInstance().format(srplusFreeFrequency));
					feeSetting.setRemarks(remark.toString());
					workFeeSetting.set(1, feeSetting);
					//計算第三行台數
					number = 0;
					Integer total = 0;
					List<DmmRepositoryDTO> repositoryDTOs = this.dmmRepositoryHistoryMonthlyDAO.listFeeAssetList(customerId, assetStatus, monthYear, endDate, assetMaintainFeeSetting);
					if (!CollectionUtils.isEmpty(repositoryDTOs)) {
						number = repositoryDTOs.size();
					}
					//計算第三行總計
					String assetTotal = assetMaintainFeeSetting.getAssetTotal();
					String firstMaintainFee = assetMaintainFeeSetting.getFirstMaintainFee();
					String otherMaintainFee = assetMaintainFeeSetting.getOtherMaintainFee();
					if (number > Integer.valueOf(assetTotal)) {
						total = Integer.valueOf(assetTotal) * Integer.valueOf(firstMaintainFee) + (number - Integer.valueOf(assetTotal))
								* Integer.valueOf(otherMaintainFee);
					} else {
						total = number * Integer.valueOf(firstMaintainFee);
					}
					//獲取第三行備註
					contractInfo.delete(0, contractInfo.length());
					contractInfo.append(i18NUtil.getName(IAtomsMessageCode.EDC_FEE_REPORT_FOR_JDW_REMARKS, 
							new String[]{firstMaintainFee, assetTotal, otherMaintainFee, assetTotal}, null));
					//將第三行的值賦值到workFeeSetting列表中
					feeSetting = workFeeSetting.get(2);
					feeSetting.setNumber(number);
					feeSetting.setFreeTmsFrequency(IAtomsConstants.MARK_MIDDLE_LINE);
					feeSetting.setRemarks(contractInfo.toString());
					feeSetting.setTotal(total);
					workFeeSetting.set(2, feeSetting);
					//郵件內容
					JasperReportCriteriaDTO criteria = null;
					//創建第一個sheet（费用总表）
					criteria = new JasperReportCriteriaDTO();
					criteria.setAutoBuildJasper(false);
					criteria.setResult(workFeeSetting);
					//設置所需報表的Name
					criteria.setJrxmlName(IAtomsConstants.EDC_FEE_REPORT_FOR_JDW_WORK_FEE);
					//設置報表路徑
					criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
					//設置匯出格式
					criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
					criteria.setSheetName(i18NUtil.getName(IAtomsConstants.EDC_FEE_REPORT_FOR_JDW_WORK_FEE));
					Map titleNameMap = new HashMap();
					titleNameMap.put("month", i18NUtil.getName(IAtomsMessageCode.EDC_FEE_REPORT_FOR_JDW_TITLE, 
							new String[]{cnMonth.get(Integer.toString(calStart.get(Calendar.MONTH) + 1))}, null));
					titleNameMap.put("year", Integer.toString(calEnd.get(Calendar.YEAR)));
					titleNameMap.put("totalNumber", DecimalFormat.getNumberInstance().format(freeTotal));
					criteria.setParameters(titleNameMap);
					criterias.add(criteria);
					
					//創建第二個sheet（【帐务月】作业明细）
					criteria = new JasperReportCriteriaDTO();
					criteria.setAutoBuildJasper(false);
					criteria.setResult(caseHandleInfoDTOs);
					//設置所需報表的Name
					criteria.setJrxmlName(IAtomsConstants.EDC_FEE_REPORT_FOR_JDW_WORK_DETAILS);
					//設置報表路徑
					criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
					//設置匯出格式
					criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
					criteria.setSheetName(Integer.toString(calStart.get(Calendar.MONTH) + 1).concat(i18NUtil.getName(IAtomsConstants.EDC_FEE_REPORT_FOR_JDW_WORK_DETAILS)));
					criterias.add(criteria);
					
					//創建第三個sheet（维护费设备)
					criteria = new JasperReportCriteriaDTO();
					criteria.setAutoBuildJasper(false);
					criteria.setResult(repositoryDTOs);
					//設置所需報表的Name
					criteria.setJrxmlName(IAtomsConstants.EDC_FEE_REPORT_FOR_JDW_MAINTENANCE_EQUIPMENT);
					//設置報表路徑
					criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
					//設置匯出格式
					criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
					criteria.setSheetName(i18NUtil.getName(IAtomsConstants.EDC_FEE_REPORT_FOR_JDW_MAINTENANCE_EQUIPMENT));
					//設置報表Name
					StringBuffer fileNameBuffer = new StringBuffer();
					fileNameBuffer.append(monthYear);
					fileNameBuffer.append(IAtomsConstants.MARK_UNDER_LINE);
					fileNameBuffer.append(reportSettingList.get(0).getCustomerName());
					fileNameBuffer.append(i18NUtil.getName(IAtomsConstants.EDC_FEE_REPORT_FOR_JDW_NAME));
					String fileName = fileNameBuffer.toString();
					criteria.setReportFileName(fileName);
					criterias.add(criteria);
					//匯出報表至臨時目錄
					StringBuffer pathBuffer = new StringBuffer();
					pathBuffer.append(SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH)).append(File.separator).append(IAtomsConstants.UC_NO_AMM_06040);
					pathBuffer.append(File.separator);
					ReportExporter.exportReportForSheetsToFile(criterias, pathBuffer.toString());
					//發送郵件
					//郵件內容
					String mailContext = null;
					//郵件主題模板
					String subjectTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.EDC_FEE_REPORT_FOR_JDW_SUBJECT_TEMPLATE;
					//郵件內容模板
					String textTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.EDC_FEE_REPORT_FOR_JDW_TEXT_TEMPLATE;
					//郵件內容
					Map<String, Object> variables = new HashMap<String, Object>();
					
					filePathBuffer.append(pathBuffer.toString());
					filePathBuffer.append(fileName).append(JasperReportCriteriaDTO.REPORT_EXT_NAME_MSEXCEL);
					String[] attachments = new String[]{filePathBuffer.toString()};
					for (ReportSettingDTO reportSettingDTO : reportSettingList) {
						if (StringUtils.hasText(reportSettingDTO.getRecipient())) {
							variables.put("reportTime", DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMMDD));
							variables.put("shortName", reportSettingDTO.getCustomerName());
							variables.put("toMail", reportSettingDTO.getRecipient());
							variables.put("ccMail", reportSettingDTO.getCopy());
							try{
								this.mailComponent.sendMailTo(null, reportSettingDTO.getRecipient(), subjectTemplate, attachments, textTemplate, variables);
							} catch (Exception e) {
								LOGGER.error("sendMailOutstandingNumReport()--->sendMailTo(): ", "DataAccess Exception:", e);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(".sendReportMail(SessionContext sessionContext):", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} finally {
			try {
				FileUtils.removeFile(filePathBuffer.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * Purpose:計算第一行與第二行免費剩餘次數
	 * @author CarrieDuan
	 * @param bptdCode：code
	 * @param number：臺數
	 * @param startDate：effectiveＤate
	 * @param ptName：基礎參數type表的ptName與ptDesc
	 * @return Integer: 剩餘次數
	 */
	public Integer calculateFreeTimes (String bptdCode, Integer number, Timestamp startDate, String ptName) {
		Integer oldFreeTimes = 0;
		try {
			List<BaseParameterItemDefDTO> baseParameterItemDefDTOs = this.baseParameterManagerDAO.listBy(bptdCode, null, null, null, null, -1, -1);
			BaseParameterItemDefDTO baseParameterItemDefDTO = null;
			BaseParameterItemDefDTO newItemDefDTO = null;
			BaseParameterItemDef baseParameterItemDef = new BaseParameterItemDef();
			SimpleDtoDmoTransformer transformer = new SimpleDtoDmoTransformer();
			if (!CollectionUtils.isEmpty(baseParameterItemDefDTOs)) {
				/*if (baseParameterItemDefDTOs.size() != 2) {
					return 0;
				}
				String dateOne = baseParameterItemDefDTOs.get(0).getTextField1();
				String dateTwo = baseParameterItemDefDTOs.get(1).getTextField1();
				if (!StringUtils.hasText(dateOne) || !StringUtils.hasText(dateTwo)) {
					return 0;
				}
				if (ValidateUtils.checkDate(dateOne) || ValidateUtils.checkDate(dateTwo)) {
					return 0;
				}
				if (DateTimeUtils.addCalendar(startDate, 0, -1, 0).getTime() != DateTimeUtils.toDate(dateOne).getTime()
						&& DateTimeUtils.addCalendar(startDate, 0, -1, 0).getTime() != DateTimeUtils.toDate(dateTwo).getTime()) {
					return 0;
				}
				if (DateTimeUtils.addCalendar(DateTimeUtils.toDate(dateTwo), 0, 1, 0).getTime() != DateTimeUtils.toDate(dateOne).getTime()
						&& DateTimeUtils.addCalendar(DateTimeUtils.toDate(dateOne), 0, 1, 0).getTime() != DateTimeUtils.toDate(dateTwo).getTime()) {
					return 0;
				}*/
				if (baseParameterItemDefDTOs.size() == 1) {
					baseParameterItemDefDTO = baseParameterItemDefDTOs.get(0);
					Timestamp tempDate = DateTimeUtils.toTimestamp(baseParameterItemDefDTO.getTextField1()+"/01");
					if (DateTimeUtils.addCalendar(tempDate, 0, 1, 0).getTime() != startDate.getTime()) {
						return 0;
					}
					newItemDefDTO = new BaseParameterItemDefDTO();
				} else {
					String dateOne = baseParameterItemDefDTOs.get(0).getTextField1();
					String dateTwo = baseParameterItemDefDTOs.get(1).getTextField1();
					dateOne += "/01";
					dateTwo += "/01";
					if (!StringUtils.hasText(dateOne) || !StringUtils.hasText(dateTwo)) {
						return 0;
					}
					if (!ValidateUtils.checkDate(dateOne) || !ValidateUtils.checkDate(dateTwo)) {
						return 0;
					}
					if (DateTimeUtils.addCalendar(startDate, 0, -1, 0).getTime() != DateTimeUtils.toDate(dateOne).getTime()
							&& DateTimeUtils.addCalendar(startDate, 0, -1, 0).getTime() != DateTimeUtils.toDate(dateTwo).getTime()) {
						return 0;
					}
					if (DateTimeUtils.addCalendar(DateTimeUtils.toDate(dateTwo), 0, 1, 0).getTime() != DateTimeUtils.toDate(dateOne).getTime()
							&& DateTimeUtils.addCalendar(DateTimeUtils.toDate(dateOne), 0, 1, 0).getTime() != DateTimeUtils.toDate(dateTwo).getTime()) {
						return 0;
					}
					if (DateTimeUtils.toDate(dateOne).getTime() == startDate.getTime()) {
						baseParameterItemDefDTO = baseParameterItemDefDTOs.get(1);
						newItemDefDTO = baseParameterItemDefDTOs.get(0);
					} else if (DateTimeUtils.toDate(dateTwo).getTime() == startDate.getTime()){
						baseParameterItemDefDTO = baseParameterItemDefDTOs.get(0);
						newItemDefDTO = baseParameterItemDefDTOs.get(1);
					} else if (DateTimeUtils.toDate(dateOne).getTime() < DateTimeUtils.toDate(dateTwo).getTime()){
						baseParameterItemDefDTO = baseParameterItemDefDTOs.get(1);
						newItemDefDTO = baseParameterItemDefDTOs.get(0);
					} else {
						baseParameterItemDefDTO = baseParameterItemDefDTOs.get(0);
						newItemDefDTO = baseParameterItemDefDTOs.get(1);
					}
				}
				Integer freeTimes = 0;
				oldFreeTimes = Integer.valueOf(baseParameterItemDefDTO.getItemValue());
				if (Integer.valueOf(baseParameterItemDefDTO.getItemValue()) > 0) {
					freeTimes = Integer.valueOf(baseParameterItemDefDTO.getItemValue()) - number;
					if (freeTimes < 0) {
						freeTimes = 0;
					}
				}
				if (StringUtils.hasText(newItemDefDTO.getBpidId())) {
					newItemDefDTO.setItemValue(freeTimes.toString());
					//newItemDefDTO.setTextField1(DateTimeUtils.toString(startDate, DateTimeUtils.DT_FMT_YYYYMMDD_SLASH));
					baseParameterItemDef = (BaseParameterItemDef) transformer.transform(newItemDefDTO, baseParameterItemDef);
					baseParameterItemDef.setId(
							new BaseParameterItemDefId(newItemDefDTO.getBpidId(), newItemDefDTO.getBptdCode(), newItemDefDTO.getEffectiveDate()));
				} else {
					baseParameterItemDef.setId(
							new BaseParameterItemDefId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BASE_PARAMETER_ITEM_DEF), 
									bptdCode, baseParameterItemDefDTO.getEffectiveDate()));
					baseParameterItemDef.setItemName(baseParameterItemDefDTO.getItemName());
					baseParameterItemDef.setItemValue(freeTimes.toString());
					baseParameterItemDef.setItemOrder(2);
					//baseParameterItemDef.setTextField1(DateTimeUtils.toString(startDate, DateTimeUtils.DT_FMT_YYYYMMDD_SLASH));
				//	baseParameterItemDef.setApprovedFlag(IAtomsConstants.YES);
					baseParameterItemDef.setItemDesc("");
					baseParameterItemDef.setParentBpidId("");
					baseParameterItemDef.setDeleted(IAtomsConstants.NO);
				}
				baseParameterItemDef.setTextField1(DateTimeUtils.toString(startDate, DateTimeUtils.DT_FMT_YYYYMMDD_SLASH).substring(0, 7));
				HibernateTransactionManager transactionManager = (HibernateTransactionManager) this.applicationContext.getBean("transactionManager");
				DefaultTransactionDefinition def = new DefaultTransactionDefinition();
				def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务，这样会比较安全些。
				TransactionStatus status = transactionManager.getTransaction(def); // 获得事务状态
				try {
					this.baseParameterManagerDAO.getDaoSupport().saveOrUpdate(baseParameterItemDef);
					transactionManager.commit(status);
				} catch (Exception e) {
					transactionManager.rollback(status);
					LOGGER.error(".calculateFreeTimes() batchMessage：執行行事歷批次錯誤 Exception:" + e, e);
				}
			}
		} catch (Exception e) {
			LOGGER.error(".calculateFreeTimes(SessionContext sessionContext):", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return oldFreeTimes;
	}
	
	/**
	 * @return the contractListFree
	 */
	public Map<String, List<String>> getContractListFree() {
		return contractListFree;
	}
	/**
	 * @param contractListFree the contractListFree to set
	 */
	public void setContractListFree(Map<String, List<String>> contractListFree) {
		this.contractListFree = contractListFree;
	}
	/**
	 * @return the contractListOld
	 */
	public Map<String, List<String>> getContractListOld() {
		return contractListOld;
	}
	/**
	 * @param contractListOld the contractListOld to set
	 */
	public void setContractListOld(Map<String, List<String>> contractListOld) {
		this.contractListOld = contractListOld;
	}
	/**
	 * @return the caseStatus
	 */
	public List<String> getCaseStatus() {
		return caseStatus;
	}
	/**
	 * @param caseStatus the caseStatus to set
	 */
	public void setCaseStatus(List<String> caseStatus) {
		this.caseStatus = caseStatus;
	}
	/**
	 * @return the commonTransactionType
	 */
	public List<String> getCommonTransactionType() {
		return commonTransactionType;
	}
	/**
	 * @param commonTransactionType the commonTransactionType to set
	 */
	public void setCommonTransactionType(List<String> commonTransactionType) {
		this.commonTransactionType = commonTransactionType;
	}
	/**
	 * @return the assetStatus
	 */
	public List<String> getAssetStatus() {
		return assetStatus;
	}
	/**
	 * @param assetStatus the assetStatus to set
	 */
	public void setAssetStatus(List<String> assetStatus) {
		this.assetStatus = assetStatus;
	}
	/**
	 * @return the assetMaintainFeeSetting
	 */
	public AssetMaintainFeeSetting getAssetMaintainFeeSetting() {
		return assetMaintainFeeSetting;
	}
	/**
	 * @param assetMaintainFeeSetting the assetMaintainFeeSetting to set
	 */
	public void setAssetMaintainFeeSetting(
			AssetMaintainFeeSetting assetMaintainFeeSetting) {
		this.assetMaintainFeeSetting = assetMaintainFeeSetting;
	}
	/**
	 * @return the workFeeSetting
	 */
	public List<WorkFeeSetting> getWorkFeeSetting() {
		return workFeeSetting;
	}
	/**
	 * @param workFeeSetting the workFeeSetting to set
	 */
	public void setWorkFeeSetting(List<WorkFeeSetting> workFeeSetting) {
		this.workFeeSetting = workFeeSetting;
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
	 * @return the companyDAO
	 */
	public ICompanyDAO getCompanyDAO() {
		return companyDAO;
	}

	/**
	 * @param companyDAO the companyDAO to set
	 */
	public void setCompanyDAO(ICompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	/**
	 * @return the dmmRepositoryHistoryMonthlyDAO
	 */
	public IDmmRepositoryHistoryMonthlyDAO getDmmRepositoryHistoryMonthlyDAO() {
		return dmmRepositoryHistoryMonthlyDAO;
	}

	/**
	 * @param dmmRepositoryHistoryMonthlyDAO the dmmRepositoryHistoryMonthlyDAO to set
	 */
	public void setDmmRepositoryHistoryMonthlyDAO(
			IDmmRepositoryHistoryMonthlyDAO dmmRepositoryHistoryMonthlyDAO) {
		this.dmmRepositoryHistoryMonthlyDAO = dmmRepositoryHistoryMonthlyDAO;
	}
	
	

	/**
	 * @return the baseParameterManagerDAO
	 */
	public IBaseParameterManagerDAO getBaseParameterManagerDAO() {
		return baseParameterManagerDAO;
	}

	/**
	 * @param baseParameterManagerDAO the baseParameterManagerDAO to set
	 */
	public void setBaseParameterManagerDAO(
			IBaseParameterManagerDAO baseParameterManagerDAO) {
		this.baseParameterManagerDAO = baseParameterManagerDAO;
	}

	/**
	 * @return the contractDAO
	 */
	public IContractDAO getContractDAO() {
		return contractDAO;
	}

	/**
	 * @param contractDAO the contractDAO to set
	 */
	public void mailComponent(IContractDAO contractDAO) {
		this.contractDAO = contractDAO;
	}

	/**
	 * @return the cnMonth
	 */
	public Map<String, String> getCnMonth() {
		return cnMonth;
	}

	/**
	 * @param cnMonth the cnMonth to set
	 */
	public void setCnMonth(Map<String, String> cnMonth) {
		this.cnMonth = cnMonth;
	}

	/**
	 * @param contractDAO the contractDAO to set
	 */
	public void setContractDAO(IContractDAO contractDAO) {
		this.contractDAO = contractDAO;
	}

	/**
	 * @return the iAtomsBaseParameterTypeDefDAO
	 */
	public IIAtomsBaseParameterTypeDefDAO getiAtomsBaseParameterTypeDefDAO() {
		return iAtomsBaseParameterTypeDefDAO;
	}

	/**
	 * @param iAtomsBaseParameterTypeDefDAO the iAtomsBaseParameterTypeDefDAO to set
	 */
	public void setiAtomsBaseParameterTypeDefDAO(
			IIAtomsBaseParameterTypeDefDAO iAtomsBaseParameterTypeDefDAO) {
		this.iAtomsBaseParameterTypeDefDAO = iAtomsBaseParameterTypeDefDAO;
	}

	/**
	 * @return the applicationContext
	 */
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * @param applicationContext the applicationContext to set
	 */
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	

	
	
}
