package com.cybersoft4u.xian.iatoms.services.impl;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.util.CollectionUtils;

import cafe.core.config.SystemConfigManager;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.BeanUtils;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.MathUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ReportSettingDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAssetLinkDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;
import com.cybersoft4u.xian.iatoms.common.reportsetting.WorkFeeSetting;
import com.cybersoft4u.xian.iatoms.common.utils.ReportExporter;
import com.cybersoft4u.xian.iatoms.common.utils.mail.MailComponent;
import com.cybersoft4u.xian.iatoms.common.utils.mail.TemplateMailMessageDTO;
import com.cybersoft4u.xian.iatoms.services.IReportService;
import com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryMonthlyDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IReportSettingDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentInfoDAO;
/**
 * Purpose: 維護費報表(大眾格式)Service
 * @author Hermanwang
 * @since  JDK 1.6
 * @date   2017/8/22
 * @MaintenancePersonnel Hermanwang
 */
public class EdcFeeReportForTcbService extends AtomicService implements IReportService{
	/**
	 * 記錄日誌
	 */
	private static final CafeLog LOGGER	= CafeLogFactory.getLog(EdcFeeReportForTcbService.class);
	/**
	 * 注入報表發送功能設定DAO
	 */
	private IReportSettingDAO reportSettingDAO;
	private IDmmRepositoryHistoryMonthlyDAO dmmRepositoryHistoryMonthlyDAO;
	
	/**
	 * 发送mail组件
	 */
	private MailComponent mailComponent = null;
	/**
	 * 作业别費用單價
	 */
	private List<WorkFeeSetting> workFeeSetting;
	/**
	 * 案件類別列表
	 */
	private List<String> commonTransactionTypeList;
	/**
	 * 案件狀態列表
	 */
	private List<String> caseStatusList;
	/**
	 * 通訊模式
	 */
	private List<String> commModeIdList;
	/**
	 * 求償狀態
	 */
	private List<String> paymentStatusList;
	/**
	 * 求償類型
	 */
	private List<String> paymentTypeList;
	/**
	 * 客户DAO
	 */
	private ICompanyDAO companyDAO;
	/**
	 * SRM_案件處理資料檔DAO
	 */
	private ISrmCaseHandleInfoDAO srmCaseHandleInfoDAO;
	
	/**
	 * 案件處理中設備連接檔
	 */
	private ISrmCaseAssetLinkDAO srmCaseAssetLinkDAO;
	/**
	 * 求償資料檔DAO
	 */
	private ISrmPaymentInfoDAO paymentInfoDAO;
	/**
	 * Constructor: 無參構造
	 */
	public EdcFeeReportForTcbService() {
		super();
	}
	/**
	 * Purpose:維護費報表(大眾格式)通知
	 * @author HermanWang
	 * @throws ServiceException
	 * @return void
	 */
	public void toReport() throws ServiceException {
		//大眾格式
		String companyCode = IAtomsConstants.PARAMTER_COMPANY_CODE_TCB_EDC;
		CompanyDTO companyDTO = this.companyDAO.getCompanyByCompanyCode(companyCode);
		if(companyDTO != null) {
			sendReportMail(DateTimeUtils.getCurrentDate(), companyDTO.getCompanyId(), IAtomsConstants.REPORT_TCB_EDC_FIFTEEN);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IReportService#sendReportMail(java.util.Date, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendReportMail(Date sendDate, String customerId, String reportCode) throws ServiceException {
		StringBuffer pathBuffer = null;
		try {
			//獲取查詢開始時間,上個月1號
			Calendar calStart=Calendar.getInstance();
			calStart.setTime(sendDate);
			calStart.add(Calendar.MONTH, -1); 
			calStart.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天 
			Timestamp startDate = DateTimeUtils.toTimestamp(DateTimeUtils.toString(calStart.getTime(), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH));
			//獲取查詢開始時間,本個月1號
			Calendar calEnd=Calendar.getInstance();
			calEnd.setTime(sendDate);
			calEnd.set(Calendar.DAY_OF_MONTH, 1);
			Timestamp endDate = DateTimeUtils.toTimestamp(DateTimeUtils.toString(calEnd.getTime(), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH));
			
			String lastMonthyDate = DateTimeUtils.toString( DateTimeUtils.addCalendar(sendDate, -1, 0, 0),  DateTimeUtils.DT_FMT_YYYY);
			
			Calendar calLastYearEnd=Calendar.getInstance();
			calLastYearEnd.setTime(sendDate);
			calLastYearEnd.set(Calendar.YEAR, Integer.parseInt(lastMonthyDate));
			calLastYearEnd.set(Calendar.DAY_OF_MONTH, 1);
			Timestamp date = DateTimeUtils.toTimestamp(DateTimeUtils.toString(calLastYearEnd.getTime(), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH));
			
			List<ReportSettingDTO> reportSettingDTOs = this.reportSettingDAO.getDetailList(customerId, reportCode);
			if (!CollectionUtils.isEmpty(reportSettingDTOs)) {
				JasperReportCriteriaDTO criteria = null;
				//郵件主題模板
				String subjectTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.EDC_FEE_REPORT_FOR_TCB_SUBJECT_TEMPLATE;
				//郵件內容模板
				String textTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.EDC_FEE_REPORT_FOR_TCB_TEXT_TEMPLATE;
				//獲取臨時保存路徑-時間區
				String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD);
				//獲取臨時路徑
				String path = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH);
				List<JasperReportCriteriaDTO> criterias = new ArrayList<JasperReportCriteriaDTO>();
				pathBuffer = new StringBuffer();
				String[] attachments = null;
				pathBuffer.append(path).append(File.separator).append(yearMonthDay).append(File.separator).append(IAtomsConstants.UC_NO_AMM_06020);
				pathBuffer.append(File.separator);
				//案件信息
				List<SrmCaseHandleInfoDTO> SrmCaseHandleInfoDTOList =  this.srmCaseHandleInfoDAO.listComplateCaseList(caseStatusList, commonTransactionTypeList, customerId, startDate, endDate);
				//設備信息
				List<DmmRepositoryDTO> DmmRepositoryDTOList =  this.dmmRepositoryHistoryMonthlyDAO.assetInfoList(DateTimeUtils.toString(startDate, DateTimeUtils.DT_FMT_YYYYMM), customerId, startDate, endDate);
				//案件通過設備鏈接檔單價分組的信息
				List<SrmCaseAssetLinkDTO> srmCaseAssetLinkDTOList = this.srmCaseAssetLinkDAO.getCaseAssetLinkInFee(caseStatusList, customerId, startDate, endDate);
				//(4).	計算耗材單價數量
				List<SrmCaseAssetLinkDTO> srmCaseLinkSuppliesDTOList = this.srmCaseAssetLinkDAO.getCaseLinkSuppliesInScsb(caseStatusList, customerId, startDate, endDate);
				//求償維修耗材費用-AO已回覆扣款方式
				List<SrmPaymentInfoDTO> srmPaymentInfoDTOList = this.paymentInfoDAO.listFeePaymentInfoList(paymentTypeList, customerId);
				//維修耗材費用-AO未回覆扣款方式
				List<SrmPaymentInfoDTO> paymentInfoDTOList = this.paymentInfoDAO.listFeePaymentInfoListToTCBEDC(paymentStatusList, customerId);
				//(維護費)設備
				List<DmmRepositoryDTO> DmmRepositoryDTOLists =  this.dmmRepositoryHistoryMonthlyDAO.feeAssetList(DateTimeUtils.toString(startDate, DateTimeUtils.DT_FMT_YYYYMM), customerId);
				List<String> installCaseInfo = new ArrayList<String>();
				List<String> unInstallCaseInfo = new ArrayList<String>();
				List<String> updateCaseInfo = new ArrayList<String>();
				List<String> checkCaseInfo = new ArrayList<String>();
				int installCount = 0;
				int isNotFirstInstall = 0;
				int unInstallCount = 0;
				int arriveUnInstallCount = 0;
				int unInstallFeeCount = 0;
				int updateCount = 0;
				int checkCount = 0;
				//急件
				int fast = 0;
				//特急件
				int extra = 0;
				//軟排
				int updateSoftDispatch = 0;
				//門號月租費
				int commModeIdCount = 0;
				//實際啟用量
				int actualEnableCount = 0;
				//訂單數兩
				int OrderCount = 0;
				//報廢兩
				int ScrapCount = 0;
				//備品量
				Double readyCount = new Double(0);
				Map<String, List<DmmRepositoryDTO>> dmmRepositoryListMap = new HashMap<String, List<DmmRepositoryDTO>>();
				//非首裝費用總計
				Integer notFirstInstalledPriceSum = 0;
				//非首撞費用未稅 總計
				Integer notFirstInstalledNoTaxPriceSum = 0;
				//拆機作業費 總計
				Integer unInstallPriceSum = 0;
				Integer unInstallPriceNoTaxSum = 0;
				//設定費 總計
				Integer settingPriceSum = 0;
				Integer settingPriceNoTaxSum = 0;
				//急件 特急件 總計
				Integer fastSum = 0;
				Integer fastNoTaxSum = 0;
				//Pos費用總計
				Integer posSum = 0;
				//ECR線材總費用
				Integer ecrlineSumInCase = 0;
				Integer ecrlineNoTaxSumInCase = 0;
				//網路線費用總和
				Integer networkRouteSumInCase = 0;
				Integer networkRouteNoTaxSumInCase = 0;
				//ecr線map  key是 單價-
				Map<String, Integer> caseCategoryMap = new HashMap<String, Integer>();
				for (SrmCaseHandleInfoDTO srmCaseHandleInfoDTO : SrmCaseHandleInfoDTOList) {
					//作業明細中,更換案件類型,序號從1開始.
					if(caseCategoryMap.get(srmCaseHandleInfoDTO.getCaseCategory()) == null) {
						caseCategoryMap.put(srmCaseHandleInfoDTO.getCaseCategory(), 1);
						srmCaseHandleInfoDTO.setRowId(String.valueOf(1));
					} else {
						caseCategoryMap.put(srmCaseHandleInfoDTO.getCaseCategory(), caseCategoryMap.get(srmCaseHandleInfoDTO.getCaseCategory()) + 1);
						srmCaseHandleInfoDTO.setRowId(String.valueOf(caseCategoryMap.get(srmCaseHandleInfoDTO.getCaseCategory())));
					}
					//(作業明細)案件中【案件類別=裝機】,且非首裝，
					if(IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())) {
						installCount ++;
						//非首裝
						if(IAtomsConstants.NO.equals(srmCaseHandleInfoDTO.getIsFirstInstalled())) {
							isNotFirstInstall ++;
							List<String> groupList = new ArrayList<String>();
							groupList.add(SrmCaseHandleInfoDTO.ATTRIBUTE.ASSET_NAME.getValue());
							groupList.add(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
							groupList.add(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue());
							groupList.add(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue());
							String caseInfoString = getFeeInfoByCaseInfo(groupList, srmCaseHandleInfoDTO);
							//刷卡機(含週邊設備)裝/移機
							WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory(IAtomsConstants.EDC_FEE_REPORT_INSTALLED);
							if(!installCaseInfo.contains(caseInfoString)) {
								//非首裝費
								srmCaseHandleInfoDTO.setNotFirstInstalledPrice(workFeeDTO.getFirstPrice());
								//非首裝費(未稅)
								//srmCaseHandleInfoDTO.setNotFirstInstalledPriceNoTax(new Double(workFeeDTO.getFirstPrice()/1.05).intValue());
								double notFirstInstalledPriceNoTax = MathUtils.divide((double)(workFeeDTO.getFirstPrice()), 1.05);
								BigDecimal notFirstInstalledPriceNoTaxBig = new BigDecimal(String.valueOf(notFirstInstalledPriceNoTax));
						        double notFirstInstalledPriceNoTaxDou = notFirstInstalledPriceNoTaxBig.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
								srmCaseHandleInfoDTO.setNotFirstInstalledPriceNoTax((int)notFirstInstalledPriceNoTaxDou);
							} else {
								//非首裝費
								srmCaseHandleInfoDTO.setNotFirstInstalledPrice(workFeeDTO.getOtherPrice());
								//非首裝費(未稅)
								//srmCaseHandleInfoDTO.setNotFirstInstalledPriceNoTax(new Double(workFeeDTO.getOtherPrice()/1.05).intValue());
								double notFirstInstalledPriceNoTax = MathUtils.divide((double)(workFeeDTO.getOtherPrice()), 1.05);
								BigDecimal notFirstInstalledPriceNoTaxBig = new BigDecimal(String.valueOf(notFirstInstalledPriceNoTax));
						        double notFirstInstalledPriceNoTaxDou = notFirstInstalledPriceNoTaxBig.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
								srmCaseHandleInfoDTO.setNotFirstInstalledPriceNoTax((int)notFirstInstalledPriceNoTaxDou);
							}
							if(!installCaseInfo.contains(caseInfoString)) {
								installCaseInfo.add(caseInfoString);
							}
							if(srmCaseHandleInfoDTO.getNotFirstInstalledPrice() != null) {
								//srmCaseHandleInfoDTO.setNotFirstInstalledPrice(0);
								notFirstInstalledPriceSum = notFirstInstalledPriceSum + srmCaseHandleInfoDTO.getNotFirstInstalledPrice();
							}
							if(srmCaseHandleInfoDTO.getNotFirstInstalledPriceNoTax() != null) {
								//srmCaseHandleInfoDTO.setNotFirstInstalledPriceNoTax(0);
								notFirstInstalledNoTaxPriceSum = notFirstInstalledNoTaxPriceSum + srmCaseHandleInfoDTO.getNotFirstInstalledPriceNoTax();
							}
						}
					} else if(IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())) {
						//且【拆機類型=到場拆機、遺失報損】數量
						unInstallCount ++;
						if(IAtomsConstants.EDC_FEE_REPORT_ARRIVE_UNINSTALL.equals(srmCaseHandleInfoDTO.getUninstallType()) 
								|| IAtomsConstants.EDC_FEE_REPORT_LOSS_REPORT.equals(srmCaseHandleInfoDTO.getUninstallType())) {
							arriveUnInstallCount ++;
							List<String> groupList = new ArrayList<String>();
							groupList.add(SrmCaseHandleInfoDTO.ATTRIBUTE.ASSET_NAME.getValue());
							groupList.add(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
							groupList.add(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue());
							if(IAtomsConstants.YES.equals(srmCaseHandleInfoDTO.getContactIsBussinessAddress())) {
								groupList.add(SrmCaseHandleInfoDTO.ATTRIBUTE.BUSSINESS_ADDRESS.getValue());
							} else {
								groupList.add(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS.getValue());
							}
							String caseInfoString = getFeeInfoByCaseInfo(groupList, srmCaseHandleInfoDTO);
							//刷卡機(含週邊設備)拆/撤機
							WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory(IAtomsConstants.EDC_FEE_REPORT_UNINSTALLED);
							if(!unInstallCaseInfo.contains(caseInfoString)) {
								unInstallCaseInfo.add(caseInfoString);
								//拆機作業費
								srmCaseHandleInfoDTO.setUnInstallPrice(workFeeDTO.getFirstPrice());
								//拆機作業費(未稅)
								//srmCaseHandleInfoDTO.setUnInstallPriceNoTax(new Double(workFeeDTO.getFirstPrice()/1.05).intValue());
								double unInstallPriceNoTax = MathUtils.divide((double)(workFeeDTO.getFirstPrice()), 1.05);
								BigDecimal unInstallPriceNoTaxBig = new BigDecimal(String.valueOf(unInstallPriceNoTax));
						        double unInstallPriceNoTaxDou = unInstallPriceNoTaxBig.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
								srmCaseHandleInfoDTO.setUnInstallPriceNoTax((int)unInstallPriceNoTaxDou);
							} else {
								//拆機作業費
								srmCaseHandleInfoDTO.setUnInstallPrice(workFeeDTO.getOtherPrice());
								//拆機作業費(未稅)
								//srmCaseHandleInfoDTO.setUnInstallPriceNoTax(new Double(workFeeDTO.getOtherPrice()/1.05).intValue());
								double unInstallPriceNoTax = MathUtils.divide((double)(workFeeDTO.getOtherPrice()), 1.05);
								BigDecimal unInstallPriceNoTaxBig = new BigDecimal(String.valueOf(unInstallPriceNoTax));
						        double unInstallPriceNoTaxDou = unInstallPriceNoTaxBig.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
								srmCaseHandleInfoDTO.setUnInstallPriceNoTax((int)unInstallPriceNoTaxDou);
							}
						//且【拆機類型=業務自拆】數量
						} else if(IAtomsConstants.EDC_FEE_REPORT_SERVICE_SELF_UNINSTALL.equals(srmCaseHandleInfoDTO.getUninstallType())) {
							unInstallFeeCount++;
						}
						if(srmCaseHandleInfoDTO.getUnInstallPrice() != null) {
							//srmCaseHandleInfoDTO.setUnInstallPrice(0);
							unInstallPriceSum = unInstallPriceSum + srmCaseHandleInfoDTO.getUnInstallPrice();
						}
						if(srmCaseHandleInfoDTO.getUnInstallPriceNoTax() != null) {
							//srmCaseHandleInfoDTO.setUnInstallPriceNoTax(0);
							unInstallPriceNoTaxSum = unInstallPriceNoTaxSum + srmCaseHandleInfoDTO.getUnInstallPriceNoTax();
						}
					//並機或者異動
					} else if(IAtomsConstants.CASE_CATEGORY.MERGE.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory()) 
							|| IAtomsConstants.CASE_CATEGORY.UPDATE.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())) {
						if(StringUtils.hasText(srmCaseHandleInfoDTO.getPeripheralsUpdate())) {
							if(srmCaseHandleInfoDTO.getPeripheralsUpdate().equals(IAtomsConstants.YES)) {
								srmCaseHandleInfoDTO.setPeripheralsUpdate(IAtomsConstants.COLUMN_V);
							} else {
								srmCaseHandleInfoDTO.setPeripheralsUpdate(IAtomsConstants.MARK_EMPTY_STRING);
							}
						}
						//且【處理方式=到場處理 且 是否同裝機作業=否
						if((IAtomsConstants.EDC_FEE_REPORT_ARRIVE_PROCESS).equals(srmCaseHandleInfoDTO.getProcessType())) {
							if(IAtomsConstants.NO.equals(srmCaseHandleInfoDTO.getSameInstalled())) {
								//從作業別單價配置表取得，費用總表計算，刷卡機(含週邊設備)設定(到場)
								//和刷卡機/(含週邊設備)設定(遠端-軟派)取得參數設定的單價配置
								WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory(IAtomsConstants.EDC_FEE_REPORT_ARRIVED);
								updateCount ++;
								List<String> groupList = new ArrayList<String>();
								groupList.add(SrmCaseHandleInfoDTO.ATTRIBUTE.ASSET_NAME.getValue());
								groupList.add(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
								groupList.add(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue());
								if(IAtomsConstants.CASE_CATEGORY.MERGE.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())) {
									if(IAtomsConstants.YES.equals(srmCaseHandleInfoDTO.getContactIsBussinessAddress())) {
										groupList.add(SrmCaseHandleInfoDTO.ATTRIBUTE.BUSSINESS_ADDRESS.getValue());
									} else {
										groupList.add(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS.getValue());
									}
								} else {
									groupList.add(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue());
								}
								String caseInfoString = getFeeInfoByCaseInfo(groupList, srmCaseHandleInfoDTO);
								if(!updateCaseInfo.contains(caseInfoString)) {
									updateCaseInfo.add(caseInfoString);
									//設定費
									srmCaseHandleInfoDTO.setSettingPrice(workFeeDTO.getFirstPrice());
									//設定費(未稅)
									//srmCaseHandleInfoDTO.setSettingPriceNoTax(new Double(workFeeDTO.getFirstPrice()/1.05).intValue());
									double settingPriceNoTax = MathUtils.divide((double)(workFeeDTO.getFirstPrice()), 1.05);
									BigDecimal settingPriceNoTaxBig = new BigDecimal(String.valueOf(settingPriceNoTax));
							        double settingPriceNoTaxDou = settingPriceNoTaxBig.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
									srmCaseHandleInfoDTO.setSettingPriceNoTax((int)settingPriceNoTaxDou);
								} else {
									//設定費
									srmCaseHandleInfoDTO.setSettingPrice(workFeeDTO.getOtherPrice());
									//設定費(未稅)
									//srmCaseHandleInfoDTO.setSettingPriceNoTax(new Double(workFeeDTO.getOtherPrice()/1.05).intValue());
									double settingPriceNoTax = MathUtils.divide((double)(workFeeDTO.getOtherPrice()), 1.05);
									BigDecimal settingPriceNoTaxBig = new BigDecimal(String.valueOf(settingPriceNoTax));
							        double settingPriceNoTaxDou = settingPriceNoTaxBig.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
									srmCaseHandleInfoDTO.setSettingPriceNoTax((int)settingPriceNoTaxDou);
								}
							}
						//【處理方式 = 軟派】
						} else if((IAtomsConstants.EDC_FEE_REPORT_SOFT_DISPATCH).equals(srmCaseHandleInfoDTO.getProcessType())) {
							updateSoftDispatch++;
							//從作業別單價配置表取得，費用總表計算，刷卡機(含週邊設備)設定(到場)
							//和刷卡機/(含週邊設備)設定(遠端-軟派)取得參數設定的單價配置
							WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory(IAtomsConstants.EDC_FEE_REPORT_REMOTE);
							//設定費
							srmCaseHandleInfoDTO.setSettingPrice(workFeeDTO.getFirstPrice());
							//設定費(未稅)
							//srmCaseHandleInfoDTO.setSettingPriceNoTax(new Double(workFeeDTO.getFirstPrice()/1.05).intValue());
							double settingPriceNoTax = MathUtils.divide((double)(workFeeDTO.getFirstPrice()), 1.05);
							BigDecimal settingPriceNoTaxBig = new BigDecimal(String.valueOf(settingPriceNoTax));
					        double settingPriceNoTaxDou = settingPriceNoTaxBig.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
							srmCaseHandleInfoDTO.setSettingPriceNoTax((int)settingPriceNoTaxDou);
						}
						if(srmCaseHandleInfoDTO.getSettingPrice() != null) {
							//srmCaseHandleInfoDTO.setSettingPrice(0);
							settingPriceSum = settingPriceSum + srmCaseHandleInfoDTO.getSettingPrice();
						}
						if(srmCaseHandleInfoDTO.getSettingPriceNoTax() != null) {
							//srmCaseHandleInfoDTO.setSettingPriceNoTax(0);
							settingPriceNoTaxSum = settingPriceNoTaxSum + srmCaseHandleInfoDTO.getSettingPriceNoTax();
						}
					//查核
					} else if(IAtomsConstants.CASE_CATEGORY.CHECK.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())) {
						checkCount++;
						List<String> groupList = new ArrayList<String>();
						groupList.add(SrmCaseHandleInfoDTO.ATTRIBUTE.ASSET_NAME.getValue());
						groupList.add(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
						groupList.add(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue());
						if(IAtomsConstants.YES.equals(srmCaseHandleInfoDTO.getContactIsBussinessAddress())) {
							groupList.add(SrmCaseHandleInfoDTO.ATTRIBUTE.BUSSINESS_ADDRESS.getValue());
						} else {
							groupList.add(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS.getValue());
						}
						String caseInfoString = getFeeInfoByCaseInfo(groupList, srmCaseHandleInfoDTO);
						if(!checkCaseInfo.contains(caseInfoString)) {
							checkCaseInfo.add(caseInfoString);
						}
					}
					//案件類型=急件
					if(IAtomsConstants.EDC_FEE_REPORT_FAST.equals(srmCaseHandleInfoDTO.getCaseType())) {
						fast++;
						//急件(非都會區於甲方提供資料日次日起算2個工作日內完成)
						WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory(IAtomsConstants.EDC_FEE_REPORT_FAST);
						srmCaseHandleInfoDTO.setDispatchPrice(workFeeDTO.getFirstPrice());
						//srmCaseHandleInfoDTO.setDispatchPriceNoTax((new Double(workFeeDTO.getFirstPrice()/1.05)).intValue());
						double dispatchPriceNoTax = MathUtils.divide((double)(workFeeDTO.getFirstPrice()), 1.05);
						BigDecimal dispatchPriceNoTaxBig = new BigDecimal(String.valueOf(dispatchPriceNoTax));
				        double dispatchPriceNoTaxDou = dispatchPriceNoTaxBig.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
						srmCaseHandleInfoDTO.setDispatchPriceNoTax((int)dispatchPriceNoTaxDou);
						if(srmCaseHandleInfoDTO.getDispatchPrice() != null) {
							//srmCaseHandleInfoDTO.setDispatchPrice(0);
							fastSum = fastSum + srmCaseHandleInfoDTO.getDispatchPrice();
						}
						if(srmCaseHandleInfoDTO.getDispatchPriceNoTax() != null) {
							//srmCaseHandleInfoDTO.setDispatchPriceNoTax(0);
							fastNoTaxSum = fastNoTaxSum + srmCaseHandleInfoDTO.getDispatchPriceNoTax();
						}
					//案件類型=特急件
					} else if(IAtomsConstants.EDC_FEE_REPORT_EXTRA.equals(srmCaseHandleInfoDTO.getCaseType())) {
						extra++;
						//特急件(都會區、非都會區於甲方提供資料日次日起算少於24小時內完成)
						WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory(IAtomsConstants.EDC_FEE_REPORT_EXTRA);
						srmCaseHandleInfoDTO.setDispatchPrice(workFeeDTO.getFirstPrice());
						//srmCaseHandleInfoDTO.setDispatchPriceNoTax((new Double(workFeeDTO.getFirstPrice()/1.05)).intValue());
						double dispatchPriceNoTax = MathUtils.divide((double)(workFeeDTO.getFirstPrice()), 1.05);
						BigDecimal dispatchPriceNoTaxBig = new BigDecimal(String.valueOf(dispatchPriceNoTax));
				        double dispatchPriceNoTaxDou = dispatchPriceNoTaxBig.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
						srmCaseHandleInfoDTO.setDispatchPriceNoTax((int)dispatchPriceNoTaxDou);
						if(srmCaseHandleInfoDTO.getDispatchPrice() != null) {
							//srmCaseHandleInfoDTO.setDispatchPrice(0);
							fastSum = fastSum + srmCaseHandleInfoDTO.getDispatchPrice();
						}
						if(srmCaseHandleInfoDTO.getDispatchPriceNoTax() != null) {
							//srmCaseHandleInfoDTO.setDispatchPriceNoTax(0);
							fastNoTaxSum = fastNoTaxSum + srmCaseHandleInfoDTO.getDispatchPriceNoTax();
						}
					} 
					//若【案件類別=裝機、異動】使用之ECR線數量和單價，計算總價
					if(IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory()) 
							|| IAtomsConstants.CASE_CATEGORY.UPDATE.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())) {
						Integer ecrlineSum = 0;
						Integer networkRouteSum = 0;
						for (SrmCaseAssetLinkDTO srmCaseAssetLinkDTO : srmCaseAssetLinkDTOList) {
							if(srmCaseAssetLinkDTO.getCaseId().equals(srmCaseHandleInfoDTO.getCaseId())) {
								if(srmCaseAssetLinkDTO.getPrice() != null) {
									//ecr線材
									if(IAtomsConstants.PARAM_ECR_LINE.equals(srmCaseAssetLinkDTO.getItemCategory())) {
										ecrlineSum = ecrlineSum + srmCaseAssetLinkDTO.getPrice().intValue();
									} else {
										networkRouteSum = networkRouteSum + srmCaseAssetLinkDTO.getPrice().intValue();
									}
								}
							}
						}
						//ECR線材費用
						if(!ecrlineSum.equals(0)) {
							srmCaseHandleInfoDTO.setEcrLineInFee(ecrlineSum);
							//ECR線材費用(未稅)
							//srmCaseHandleInfoDTO.setEcrLineNoTax(new Double(ecrlineSum/1.05).intValue());
							double ecrLineNoTax = MathUtils.divide((double)(ecrlineSum), 1.05);
							BigDecimal ecrLineNoTaxBig = new BigDecimal(String.valueOf(ecrLineNoTax));
					        double ecrLineNoTaxDou = ecrLineNoTaxBig.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
							srmCaseHandleInfoDTO.setEcrLineNoTax((int)ecrLineNoTaxDou);
						}
						if(!networkRouteSum.equals(0)) {
							//網路線
							srmCaseHandleInfoDTO.setNetLineInFee(networkRouteSum);
							//網路線費用(未稅)
							//srmCaseHandleInfoDTO.setNetLineNoTax(new Double(networkRouteSum/1.05).intValue());
							double netLineNoTax = MathUtils.divide((double)(networkRouteSum), 1.05);
							BigDecimal netLineNoTaxBig = new BigDecimal(String.valueOf(netLineNoTax));
					        double netLineNoTaxDou = netLineNoTaxBig.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
							srmCaseHandleInfoDTO.setNetLineNoTax((int)netLineNoTaxDou);
						}
						//報表最下段的總和
						ecrlineSumInCase = ecrlineSumInCase + ecrlineSum;
						//ecrlineNoTaxSumInCase = ecrlineNoTaxSumInCase + new Double(ecrlineSum/1.05).intValue();
						double ecrlineSumNoTax = MathUtils.divide((double)(ecrlineSum), 1.05);
						BigDecimal ecrlineSumNoTaxBig = new BigDecimal(String.valueOf(ecrlineSumNoTax));
				        double ecrlineSumNoTaxDou = ecrlineSumNoTaxBig.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
						ecrlineNoTaxSumInCase = ecrlineNoTaxSumInCase + (int)ecrlineSumNoTaxDou;
						networkRouteSumInCase = networkRouteSumInCase + networkRouteSum;
						//networkRouteNoTaxSumInCase = networkRouteNoTaxSumInCase + new Double(networkRouteSum/1.05).intValue();
						double networkRouteSumTax = MathUtils.divide((double)(networkRouteSum), 1.05);
						BigDecimal networkRouteSumTaxBig = new BigDecimal(String.valueOf(networkRouteSumTax));
				        double networkRouteSumTaxDou = networkRouteSumTaxBig.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
						networkRouteNoTaxSumInCase = networkRouteNoTaxSumInCase + (int)networkRouteSumTaxDou;
					}
					if(srmCaseHandleInfoDTO.getPosPriceInFee() != null) {
						//srmCaseHandleInfoDTO.setPosPriceInFee(0);
						posSum = posSum + srmCaseHandleInfoDTO.getPosPriceInFee();
					}
					if(srmCaseHandleInfoDTO.getEcrLine() == IAtomsConstants.EDC_FEE_REPORT_PRICE_ZERO) {
						srmCaseHandleInfoDTO.setEcrLine(null);
					}
					if(srmCaseHandleInfoDTO.getNetLine() == IAtomsConstants.EDC_FEE_REPORT_PRICE_ZERO) {
						srmCaseHandleInfoDTO.setNetLine(null);
					}
					if(IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())) {
						if(StringUtils.hasText(srmCaseHandleInfoDTO.getIsFirstInstalled())) {
							srmCaseHandleInfoDTO.setIsFirstInstalled(i18NUtil.getName(srmCaseHandleInfoDTO.getIsFirstInstalled()));
						}
					} else {
						srmCaseHandleInfoDTO.setIsFirstInstalled(IAtomsConstants.MARK_EMPTY_STRING);
					}
					srmCaseHandleInfoDTO.setCaseCategory(i18NUtil.getName(srmCaseHandleInfoDTO.getCaseCategory()));
				}
				List<DmmRepositoryDTO> dmmRepositoryDTOList = null;
				for(DmmRepositoryDTO dmmRepositoryDTO : DmmRepositoryDTOList) {
					boolean flag = false;
					boolean is3G = false;
					//狀態等於使用中
					if(IAtomsConstants.PARAM_ASSET_STATUS_IN_USE.equals(dmmRepositoryDTO.getStatus())) {
						//包含逗號
						if(!StringUtils.hasText(dmmRepositoryDTO.getCommModeId())) {
							dmmRepositoryDTO.setCommModeId(IAtomsConstants.MARK_EMPTY_STRING);
						}
						dmmRepositoryDTO.setCommModeId(dmmRepositoryDTO.getCommModeId() + ",");
						//用逗號解開
						String[] commModes = dmmRepositoryDTO.getCommModeId().split(",");
						//循環通訊模式
						for (String commModeId : commModes) {
							//只要有通訊模式支援 3G、GPRS、Bluetooth(3G/WIFI)、音源孔(3G/WIFI)通訊模式只需支援上述一種即可
							if(commModeIdList.contains(commModeId)) {
								is3G = true;
								//若設備通訊模式含 3G、GPRS、
								//Bluetooth(3G/WIFI)、音源孔(3G/WIFI)，且狀態為使用中，則從作業別單價配置表
								//取得（類別為NUMBER_FEE），反之留空
								WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory(IAtomsConstants.EDC_FEE_REPORT_NUMBER_FEE);
								//門號月租費(含稅)
								dmmRepositoryDTO.setNumberFeeTax(workFeeDTO.getFirstPrice());
								//門號月租費(未稅)
								//dmmRepositoryDTO.setNumberFeeNoTax((new Double(workFeeDTO.getFirstPrice()/1.05)).intValue());
								double numberFeeNoTax = MathUtils.divide((double)(workFeeDTO.getFirstPrice()), 1.05);
								BigDecimal numberFeeNoTaxBig = new BigDecimal(String.valueOf(numberFeeNoTax));
								double numberFeeNoTaxDou = numberFeeNoTaxBig.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
								dmmRepositoryDTO.setNumberFeeNoTax((int)numberFeeNoTaxDou);
								commModeIdCount ++;
								flag = true;
								break;
							}
						}
					}
					dmmRepositoryDTO.setIs3G(is3G);
					if(!flag) {
						dmmRepositoryDTO.setNumberFeeTax(0);
						dmmRepositoryDTO.setNumberFeeNoTax(0);
					}
					// 維護費(含稅) 若設備類別不是已銷毀，已報廢，則從作業別單價配置表取得（類別為MAINTENANCE_FEE），反之留空
					if(!(IAtomsConstants.PARAM_ASSET_STATUS_DISABLED.equals(dmmRepositoryDTO.getStatus())
							|| IAtomsConstants.PARAM_ASSET_STATUS_DESTROY.equals(dmmRepositoryDTO.getStatus()))) {
						WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory(IAtomsConstants.EDC_FEE_REPORT_MAINTENANCE_FEE);
						dmmRepositoryDTO.setMaintenanceTax(workFeeDTO.getFirstPrice());
						//dmmRepositoryDTO.setMaintenanceNoTax((new Double(workFeeDTO.getFirstPrice()/1.05)).intValue());
						double maintenanceNoTax = MathUtils.divide((double)(workFeeDTO.getFirstPrice()), 1.05);
						BigDecimal maintenanceNoTaxBig = new BigDecimal(String.valueOf(maintenanceNoTax));
				        double maintenanceNoTaxDou = maintenanceNoTaxBig.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
						dmmRepositoryDTO.setMaintenanceNoTax((int)maintenanceNoTaxDou);
					} else {
						dmmRepositoryDTO.setMaintenanceTax(0);
						dmmRepositoryDTO.setMaintenanceNoTax(0);
					}
					//設備sheet是 不定的，所以統計有多少中，每一種多少個dto
					if(dmmRepositoryListMap.get(dmmRepositoryDTO.getName()) != null) {
						dmmRepositoryDTOList = dmmRepositoryListMap.get(dmmRepositoryDTO.getName());
						dmmRepositoryDTOList.add(dmmRepositoryDTO);
					} else {
						dmmRepositoryDTOList = new ArrayList<DmmRepositoryDTO>();
						dmmRepositoryDTOList.add(dmmRepositoryDTO);
					}
					dmmRepositoryListMap.put(dmmRepositoryDTO.getName(), dmmRepositoryDTOList);
				}
				//(維護費)設備
				for (DmmRepositoryDTO dmmRepositoryDTO : DmmRepositoryDTOLists) {
					//(維護費)設備中，【已啟用】
					if(dmmRepositoryDTO.getCheckedDate() != null) {
						if(IAtomsConstants.YES.equals(dmmRepositoryDTO.getIsEnabled())
								//且【(客戶驗收日 + 1年) <= 帳務年月月底】
								// Bug #3232 維護費報表啟用日期調整   (客戶驗收日 + 1年) > 帳務年月月底/ (帳務年月月底 - 1) < 客戶驗收日
							//	&& date.compareTo(dmmRepositoryDTO.getCheckedDate()) > 0
								&& date.compareTo(dmmRepositoryDTO.getCheckedDate()) > 0
								//且【狀態<>報廢、銷毀
								&& (!IAtomsConstants.PARAM_ASSET_STATUS_DISABLED.equals(dmmRepositoryDTO.getStatus()) 
										&& !IAtomsConstants.PARAM_ASSET_STATUS_DESTROY.equals(dmmRepositoryDTO.getStatus()))) {
							actualEnableCount++;
						}
						//訂單數量：設備中，(客戶驗收日 + 1年) <= 帳務年月月底
						// Bug #3232 維護費報表啟用日期調整   (客戶驗收日 + 1年) > 帳務年月月底/ (帳務年月月底 - 1) < 客戶驗收日
					//	if(date.compareTo(dmmRepositoryDTO.getCheckedDate()) > 0) {
						if(date.compareTo(dmmRepositoryDTO.getCheckedDate()) > 0) {
							OrderCount ++;
						}
					}
					//報廢量：設備中，狀態=報廢、銷毀的數量
					if(IAtomsConstants.PARAM_ASSET_STATUS_DISABLED.equals(dmmRepositoryDTO.getStatus()) 
							|| IAtomsConstants.PARAM_ASSET_STATUS_DESTROY.equals(dmmRepositoryDTO.getStatus())) {
						ScrapCount ++;
					}
				}
				//備用量  訂單數量 - 報廢量)*5%
				if(OrderCount < ScrapCount) {
					readyCount = new Double(0);
				} else {
					//readyCount = (OrderCount - ScrapCount)*0.05;
					readyCount = MathUtils.multiply((double)(OrderCount - ScrapCount), 0.05);
				}
				//匯出的結果result
				List<WorkFeeSetting> workFeeSettingList = new ArrayList<WorkFeeSetting>(); 
				if(workFeeSetting != null) {
					//裝機 
					WorkFeeSetting workFeeDTOInstalled = new WorkFeeSetting();
					WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory(IAtomsConstants.TICKET_TYPE_INSTALLED);
					workFeeDTOInstalled.setFirstPrice(workFeeDTO.getFirstPrice());
					workFeeDTOInstalled.setOtherPrice(workFeeDTO.getOtherPrice());
					workFeeDTOInstalled.setName(workFeeDTO.getName());
					if(!CollectionUtils.isEmpty(installCaseInfo)) {
						workFeeDTOInstalled.setFirstCount(String.valueOf(installCaseInfo.size()));
						workFeeDTOInstalled.setOtherCount(String.valueOf(isNotFirstInstall-installCaseInfo.size()));
					} else {
						workFeeDTOInstalled.setFirstCount(String.valueOf(0));
						workFeeDTOInstalled.setOtherCount(String.valueOf(0));
					}
					workFeeDTOInstalled.setSum(Integer.parseInt(workFeeDTOInstalled.getFirstCount())*workFeeDTO.getFirstPrice()
							+ Integer.parseInt(workFeeDTOInstalled.getOtherCount()) * workFeeDTO.getOtherPrice());
					//workFeeDTOInstalled.setDescription("本月裝機共"+ installCount +"台，內含"+ isNotFirstInstall +"台非首裝設備");
					workFeeDTOInstalled.setDescription(i18NUtil.getName(IAtomsMessageCode.EDC_FEE_REPORT_INSTALLED_COUNT, 
							new String[] {String.valueOf(installCount), String.valueOf(isNotFirstInstall)}, null));
					workFeeSettingList.add(workFeeDTOInstalled);
					//拆機
					WorkFeeSetting workFeeDTOUninstalled = new WorkFeeSetting();
					workFeeDTO = getworkFeeNameByWorkCategory(IAtomsConstants.EDC_FEE_REPORT_UNINSTALLED);
					workFeeDTOUninstalled.setFirstPrice(workFeeDTO.getFirstPrice());
					workFeeDTOUninstalled.setOtherPrice(workFeeDTO.getOtherPrice());
					workFeeDTOUninstalled.setName(workFeeDTO.getName());
					//總拆機台數減去不收費的
					workFeeDTOUninstalled.setFirstCount(String.valueOf(unInstallCaseInfo.size()));
					workFeeDTOUninstalled.setOtherCount(String.valueOf(arriveUnInstallCount - unInstallCaseInfo.size()));
					workFeeDTOUninstalled.setSum((Integer.parseInt(workFeeDTOUninstalled.getFirstCount()) * workFeeDTO.getFirstPrice() 
							+ Integer.parseInt(workFeeDTOUninstalled.getOtherCount()) * workFeeDTO.getOtherPrice()));
					//workFeeDTOUninstalled.setDescription("本月拆機共"+unInstallCount+"台，內含"+unInstallFeeCount+"台POS機為自拆寄回故無需收費");
					workFeeDTOUninstalled.setDescription(i18NUtil.getName(IAtomsMessageCode.EDC_FEE_REPORT_UNINSTALLED_COUNT, 
							new String[] {String.valueOf(unInstallCount), String.valueOf(unInstallFeeCount)}, null));
					workFeeSettingList.add(workFeeDTOUninstalled);
					//到場
					WorkFeeSetting workFeeDTOArrived = new WorkFeeSetting();
					workFeeDTO = getworkFeeNameByWorkCategory(IAtomsConstants.EDC_FEE_REPORT_ARRIVED);
					workFeeDTOArrived.setFirstPrice(workFeeDTO.getFirstPrice());
					workFeeDTOArrived.setOtherPrice(workFeeDTO.getOtherPrice());
					workFeeDTOArrived.setName(workFeeDTO.getName());
					if(!CollectionUtils.isEmpty(updateCaseInfo)) {
						workFeeDTOArrived.setFirstCount(String.valueOf(updateCaseInfo.size()));
						workFeeDTOArrived.setOtherCount(String.valueOf(updateCount-updateCaseInfo.size()));
					} else {
						workFeeDTOArrived.setFirstCount(String.valueOf(0));
						workFeeDTOArrived.setOtherCount(String.valueOf(updateCount));
					}
					workFeeDTOArrived.setSum((Integer.parseInt(workFeeDTOArrived.getFirstCount()) * workFeeDTO.getFirstPrice() 
							+ Integer.parseInt(workFeeDTOArrived.getOtherCount()) * workFeeDTO.getOtherPrice()));
					workFeeSettingList.add(workFeeDTOArrived);
					//軟排
					WorkFeeSetting workFeeDTORemote = new WorkFeeSetting();
					workFeeDTO = getworkFeeNameByWorkCategory(IAtomsConstants.EDC_FEE_REPORT_REMOTE);
					workFeeDTORemote.setFirstPrice(workFeeDTO.getFirstPrice());
					workFeeDTORemote.setOtherCount(IAtomsConstants.MARK_EMPTY_STRING);
					workFeeDTORemote.setName(workFeeDTO.getName());
					workFeeDTORemote.setFirstCount(String.valueOf(updateSoftDispatch));
					workFeeDTORemote.setSum((Integer.parseInt(workFeeDTORemote.getFirstCount()) * workFeeDTO.getFirstPrice()));
					workFeeSettingList.add(workFeeDTORemote);
					//刷卡機(含週邊設備)二次作業	(裝/拆/設定/配合特約商店作業)
					WorkFeeSetting workFeeDTOSecond = new WorkFeeSetting();
					workFeeDTO = getworkFeeNameByWorkCategory(IAtomsConstants.EDC_FEE_REPORT_SECOND);
					workFeeDTOSecond.setFirstPrice(workFeeDTO.getFirstPrice());
					workFeeDTOSecond.setFirstCount(String.valueOf(0));
					workFeeDTOSecond.setOtherPrice(workFeeDTO.getOtherPrice());
					workFeeDTOSecond.setOtherCount(String.valueOf(0));
					workFeeDTOSecond.setName(workFeeDTO.getName());
					workFeeSettingList.add(workFeeDTOSecond);
					//門號月租費
					WorkFeeSetting workFeeDTONumberFee = new WorkFeeSetting();
					workFeeDTO = getworkFeeNameByWorkCategory(IAtomsConstants.EDC_FEE_REPORT_NUMBER_FEE);
					workFeeDTONumberFee.setFirstPrice(workFeeDTO.getFirstPrice());
					workFeeDTONumberFee.setOtherCount(IAtomsConstants.MARK_EMPTY_STRING);
					workFeeDTONumberFee.setName(workFeeDTO.getName());
					workFeeDTONumberFee.setFirstCount(String.valueOf(commModeIdCount));
					workFeeDTONumberFee.setSum((Integer.parseInt(workFeeDTONumberFee.getFirstCount()) * workFeeDTO.getFirstPrice()));
					workFeeSettingList.add(workFeeDTONumberFee);
					//急件(非都會區於甲方提供資料日次日起算2個工作日內完成)
					WorkFeeSetting workFeeDTOFast = new WorkFeeSetting();
					workFeeDTO = getworkFeeNameByWorkCategory(IAtomsConstants.EDC_FEE_REPORT_FAST);
					workFeeDTOFast.setFirstPrice(workFeeDTO.getFirstPrice());
					workFeeDTOFast.setOtherCount(IAtomsConstants.MARK_EMPTY_STRING);
					workFeeDTOFast.setName(workFeeDTO.getName());
					workFeeDTOFast.setFirstCount(String.valueOf(fast));
					workFeeDTOFast.setSum((Integer.parseInt(workFeeDTOFast.getFirstCount()) * workFeeDTO.getFirstPrice()));
					workFeeSettingList.add(workFeeDTOFast);
					//特急件(都會區、非都會區於甲方提供資料日次日起算少於24小時內完成)
					WorkFeeSetting workFeeDTOExtra = new WorkFeeSetting();
					workFeeDTO = getworkFeeNameByWorkCategory(IAtomsConstants.EDC_FEE_REPORT_EXTRA);
					workFeeDTOExtra.setFirstPrice(workFeeDTO.getFirstPrice());
					workFeeDTOExtra.setOtherCount(IAtomsConstants.MARK_EMPTY_STRING);
					workFeeDTOExtra.setFirstCount(String.valueOf(extra));
					workFeeDTOExtra.setName(workFeeDTO.getName());
					workFeeDTOExtra.setSum((Integer.parseInt(workFeeDTOExtra.getFirstCount()) * workFeeDTO.getFirstPrice()));
					workFeeSettingList.add(workFeeDTOExtra);
					//刷卡機(含週邊設備)抽樣檢測
					WorkFeeSetting workFeeDTOCheck = new WorkFeeSetting();
					workFeeDTO = getworkFeeNameByWorkCategory(IAtomsConstants.EDC_FEE_REPORT_CHECK);
					workFeeDTOCheck.setFirstPrice(workFeeDTO.getFirstPrice());
					workFeeDTOCheck.setOtherPrice(workFeeDTO.getOtherPrice());
					workFeeDTOCheck.setName(workFeeDTO.getName());
					if(!CollectionUtils.isEmpty(checkCaseInfo)) {
						workFeeDTOCheck.setFirstCount(String.valueOf(checkCaseInfo.size()));
						workFeeDTOCheck.setOtherCount(String.valueOf(checkCount - checkCaseInfo.size()));
					} else {
						workFeeDTOCheck.setFirstCount(String.valueOf(0));
						workFeeDTOCheck.setOtherCount(String.valueOf(checkCount));
					}
					workFeeDTOCheck.setSum((Integer.parseInt(workFeeDTOCheck.getFirstCount()) * workFeeDTO.getFirstPrice()
							+ Integer.parseInt(workFeeDTOCheck.getOtherCount()) * workFeeDTO.getOtherPrice()));
					workFeeSettingList.add(workFeeDTOCheck);
					//週邊設備單獨裝/拆/設定
					WorkFeeSetting workFeeDTOPeripheral = new WorkFeeSetting();
					workFeeDTO = getworkFeeNameByWorkCategory(IAtomsConstants.EDC_FEE_REPORT_PERIPHERAL);
					workFeeDTOPeripheral.setFirstPrice(workFeeDTO.getFirstPrice());
					workFeeDTOPeripheral.setOtherPrice(workFeeDTO.getOtherPrice());
					workFeeDTOPeripheral.setWorkCategory(i18NUtil.getName(IAtomsConstants.EDC_FEE_REPORT_SHEET_NAME_PERIPHERAL_SETTING));
					workFeeDTOPeripheral.setName(i18NUtil.getName(IAtomsConstants.EDC_FEE_REPORT_SHEET_NAME_PERIPHERAL_SETTING));
					workFeeSettingList.add(workFeeDTOPeripheral);
					//刷卡機、週邊設備月維護費用
					WorkFeeSetting workFeeDTOMaintenanceFee = new WorkFeeSetting();
					workFeeDTO = getworkFeeNameByWorkCategory(IAtomsConstants.EDC_FEE_REPORT_MAINTENANCE_FEE);
					workFeeDTOMaintenanceFee.setFirstPrice(workFeeDTO.getFirstPrice());
					workFeeDTOMaintenanceFee.setName(workFeeDTO.getName());
					//實際啟用量+備品量 >訂單數量-報廢量 
					if((actualEnableCount+readyCount) > (OrderCount-ScrapCount)) {
						workFeeDTOMaintenanceFee.setFirstCount(String.valueOf(OrderCount-ScrapCount));
						workFeeDTOMaintenanceFee.setSum((Integer.parseInt(workFeeDTOMaintenanceFee.getFirstCount()) * workFeeDTO.getFirstPrice()));
						workFeeDTOMaintenanceFee.setDescription(i18NUtil.getName(IAtomsMessageCode.EDC_FEE_REPORT_MAINTENANCE_FEE_MSG1, 
								new String[] {String.valueOf(OrderCount), String.valueOf(ScrapCount), String.valueOf(workFeeDTO.getFirstPrice()), String.valueOf(workFeeDTOMaintenanceFee.getSum())}, null));
					} else {
						//double installCountInt = installCount * 0.02;
						BigDecimal readyCountBig = new BigDecimal(String.valueOf(readyCount));
				        double readyCountDou = readyCountBig.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
						workFeeDTOMaintenanceFee.setFirstCount(String.valueOf((int)(actualEnableCount + readyCountDou)));
						Double count = Double.parseDouble(workFeeDTOMaintenanceFee.getFirstCount());
						workFeeDTOMaintenanceFee.setSum((count.intValue() * workFeeDTO.getFirstPrice()));
						workFeeDTOMaintenanceFee.setDescription(i18NUtil.getName(IAtomsMessageCode.EDC_FEE_REPORT_MAINTENANCE_FEE_MSG2, 
								new String[] {String.valueOf(actualEnableCount), String.valueOf((int)readyCountDou), String.valueOf(workFeeDTO.getFirstPrice()), String.valueOf(workFeeDTOMaintenanceFee.getSum())}, null));
					}
					workFeeSettingList.add(workFeeDTOMaintenanceFee);
					for (SrmCaseAssetLinkDTO srmCaseAssetLinkDTO : srmCaseLinkSuppliesDTOList) {
						WorkFeeSetting workFeeDTOSupplies = new WorkFeeSetting();
						if(srmCaseAssetLinkDTO.getNumber() == null) {
							srmCaseAssetLinkDTO.setNumber(0);
						}
						if(IAtomsConstants.PARAM_ECR_LINE.equals(srmCaseAssetLinkDTO.getItemCategory())) {
							//線材使用-ECR線材
							workFeeDTOSupplies.setWorkCategory(i18NUtil.getName(IAtomsConstants.EDC_FEE_REPORT_ECR_LINE_ZH_TW));
							workFeeDTOSupplies.setFirstCount(String.valueOf(srmCaseAssetLinkDTO.getNumber()));
							workFeeDTOSupplies.setName(i18NUtil.getName(IAtomsConstants.EDC_FEE_REPORT_ECR_LINE_ZH_TW));
							if(srmCaseAssetLinkDTO.getPrice() == null) {
								srmCaseAssetLinkDTO.setPrice(new BigDecimal(0));
							}
							workFeeDTOSupplies.setFirstPrice(srmCaseAssetLinkDTO.getPrice().intValue());
							workFeeDTOSupplies.setOtherCount(IAtomsConstants.MARK_EMPTY_STRING);
							workFeeDTOSupplies.setSum((Integer.parseInt(workFeeDTOSupplies.getFirstCount()) * srmCaseAssetLinkDTO.getPrice().intValue()));
							workFeeSettingList.add(workFeeDTOSupplies);
						} else if(IAtomsConstants.PARAM_NET_WORK_LINE.equals(srmCaseAssetLinkDTO.getItemCategory())) {
							//線材使用-網路線
							workFeeDTOSupplies.setWorkCategory(i18NUtil.getName(IAtomsConstants.EDC_FEE_REPORT_NET_WORK_LINE_ZH_TW));
							workFeeDTOSupplies.setFirstCount(String.valueOf(srmCaseAssetLinkDTO.getNumber()));
							workFeeDTOSupplies.setName(i18NUtil.getName(IAtomsConstants.EDC_FEE_REPORT_NET_WORK_LINE_ZH_TW));
							if(srmCaseAssetLinkDTO.getPrice() == null) {
								srmCaseAssetLinkDTO.setPrice(new BigDecimal(0));
							}
							workFeeDTOSupplies.setFirstPrice(srmCaseAssetLinkDTO.getPrice().intValue());
							workFeeDTOSupplies.setOtherCount(IAtomsConstants.MARK_EMPTY_STRING);
							workFeeDTOSupplies.setSum((Integer.parseInt(workFeeDTOSupplies.getFirstCount()) * srmCaseAssetLinkDTO.getPrice().intValue()));
							workFeeSettingList.add(workFeeDTOSupplies);
						}
					}
					//罚款
					WorkFeeSetting workFeeDTOFine = new WorkFeeSetting();
					workFeeDTOFine.setName("罰款件");
					workFeeSettingList.add(workFeeDTOFine);
					//零件維修
					WorkFeeSetting workFeeDTOPartsMaintenance  = new WorkFeeSetting();
					workFeeDTOPartsMaintenance.setName("零件維修");
					workFeeSettingList.add(workFeeDTOPartsMaintenance);
					//教育訓練費用
					WorkFeeSetting workFeeDTOEducation = new WorkFeeSetting();
					workFeeDTOEducation.setName("教育訓練費用");
					workFeeSettingList.add(workFeeDTOEducation);
					//APN月租費
					WorkFeeSetting workFeeDTOApn = new WorkFeeSetting();
					workFeeDTOApn.setName("APN月租費");
					workFeeDTOApn.setSum(1200);
					workFeeDTOApn.setDescription("每月固定收費：申請中華電信MDVPN，SIM卡必須加入APN連結後端的VPN。");
					workFeeSettingList.add(workFeeDTOApn);
					//VPN存取控制月租費
					WorkFeeSetting workFeeDTOVpn = new WorkFeeSetting();
					workFeeDTOVpn.setName("VPN存取控制月租費");
					workFeeDTOVpn.setSum(2300);
					workFeeDTOVpn.setDescription("每月固定收費：前述APN必須同時連結經貿聯網VPN(No.2765)執行TMS程式參數下載及連結大眾VPN(No. 3993)執行刷卡交易，故須設定此存取控制。");
					workFeeSettingList.add(workFeeDTOVpn);
				}
				criteria = new JasperReportCriteriaDTO();
				criteria.setAutoBuildJasper(false);
				criteria.setResult(workFeeSettingList);
				//設置所需報表的Name
				criteria.setJrxmlName(IAtomsConstants.PRICE_SUM_REPORT_CH_NAME);
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				Integer total = 0;
				for (WorkFeeSetting workFeeSetting : workFeeSettingList) {
					if(workFeeSetting.getSum() == null) {
						workFeeSetting.setSum(0);
					}
					total = total + workFeeSetting.getSum();
				}
				Map countMap = new HashMap();
				countMap.put("total", total);
				criteria.setParameters(countMap);
				//設置報表Name
				StringBuffer fileNameBuffer = new StringBuffer();
				fileNameBuffer.append(DateTimeUtils.toString(startDate, DateTimeUtils.DT_FMT_YYYYMM));
				fileNameBuffer.append(IAtomsConstants.MARK_UNDER_LINE);
				fileNameBuffer.append(reportSettingDTOs.get(0).getCustomerName());
				fileNameBuffer.append(i18NUtil.getName(IAtomsConstants.FEE_REPORT_FOR_GREEN_WORLD_SUBJECT_CH_NAME));
				String fileName = fileNameBuffer.toString();
				//費用總表
				criteria.setSheetName(i18NUtil.getName(IAtomsConstants.EDC_FEE_REPORT_SHEET_NAME_PRICE_SUM));
				criterias.add(criteria);
				for(Entry<String, List<DmmRepositoryDTO>> assetEntry : dmmRepositoryListMap.entrySet()){
					Integer rowCount = 1;
					List<DmmRepositoryDTO> DmmRepositoryResult = assetEntry.getValue();
					criteria = new JasperReportCriteriaDTO();
					criteria.setAutoBuildJasper(false);
					boolean is3G = false;
					for (DmmRepositoryDTO dmmRepositoryDTO : DmmRepositoryResult) {
						if(dmmRepositoryDTO.getIs3G()) {
							is3G = true;
						}
						break;
					}
					criteria.setResult(DmmRepositoryResult);
					//設置所需報表的Name
					if(is3G) {
						criteria.setJrxmlName(IAtomsConstants.ASSET_INFO_CH_NAME);
					} else {
						criteria.setJrxmlName(IAtomsConstants.ASSET_INFO_NO3G_CH_NAME);
					}
					//設置報表路徑
					criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
					//設置匯出格式
					criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
					//criteria.setSheetName("啟用設備明細- " + assetEntry.getKey() + " (計算方式)");
					criteria.setSheetName(i18NUtil.getName(IAtomsMessageCode.EDC_FEE_REPORT_ASSET_INFO, new String[] {assetEntry.getKey()}, null));
					//門號月租費 (含稅)總計
					Integer numberFeeTaxSum = 0; 
					//門號月租費 (未稅)總計
					Integer numberFeeNoTaxSum = 0; 
					//維護費(含稅) 總計
					Integer maintenanceTaxSum = 0; 
					//維護費(未稅) 總計
					Integer maintenanceNoTaxSum = 0; 
					for (DmmRepositoryDTO dmmRepositoryDTO : DmmRepositoryResult) {
						dmmRepositoryDTO.setRowId(String.valueOf(rowCount));
						rowCount ++;
						numberFeeTaxSum = numberFeeTaxSum + dmmRepositoryDTO.getNumberFeeTax();
						numberFeeNoTaxSum = numberFeeNoTaxSum + dmmRepositoryDTO.getNumberFeeNoTax();
						maintenanceTaxSum = maintenanceTaxSum + dmmRepositoryDTO.getMaintenanceTax();
						maintenanceNoTaxSum = maintenanceNoTaxSum + dmmRepositoryDTO.getMaintenanceNoTax();
						//門號月租費 (含稅)
						if(dmmRepositoryDTO.getNumberFeeTax() == 0) {
							dmmRepositoryDTO.setNumberFeeTax(null);
						}
						//門號月租費 (未稅)
						if(dmmRepositoryDTO.getNumberFeeNoTax() == 0) {
							dmmRepositoryDTO.setNumberFeeNoTax(null);
						}
						//維護費(含稅)
						if(dmmRepositoryDTO.getMaintenanceTax() == 0) {
							dmmRepositoryDTO.setMaintenanceTax(null);
						}
						//維護費(未稅)
						if(dmmRepositoryDTO.getMaintenanceNoTax() == 0) {
							dmmRepositoryDTO.setMaintenanceNoTax(null);
						}
					}
					Map assetTitleNameMap = new HashMap();
					assetTitleNameMap.put(IAtomsConstants.EDC_FEE_REPORT_NUMBER_FEE_TAXSUM, numberFeeTaxSum);
					assetTitleNameMap.put(IAtomsConstants.EDC_FEE_REPORT_NUMBER_FEE_NO_TAXSUM,  numberFeeNoTaxSum);
					if(is3G){
						assetTitleNameMap.put(IAtomsConstants.EDC_FEE_REPORT_MAIN_TENANCE_TAXSUM, maintenanceTaxSum);
						assetTitleNameMap.put(IAtomsConstants.EDC_FEE_REPORT_MAIN_TENANCE_NO_TAXSUM, maintenanceNoTaxSum);
					}
					criteria.setParameters(assetTitleNameMap);
					criterias.add(criteria);
				}
				criteria = new JasperReportCriteriaDTO();
				criteria.setAutoBuildJasper(false);
				if(SrmCaseHandleInfoDTOList.size() == 0) {
					SrmCaseHandleInfoDTO srmCaseHandleInfoDTO = new SrmCaseHandleInfoDTO();
					SrmCaseHandleInfoDTOList.add(srmCaseHandleInfoDTO);
				}
				criteria.setResult(SrmCaseHandleInfoDTOList);
				//設置所需報表的Name
				criteria.setJrxmlName(IAtomsConstants.FEE_CASE_INFO_REPORT_CH_NAME);
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				//設置報表Name
				criteria.setSheetName(i18NUtil.getName(IAtomsConstants.EDC_FEE_REPORT_SHEET_NAME_CASE_INFO));
				
				Map caseTitleNameMap = new HashMap();
				//非首裝費用總計
				caseTitleNameMap.put(IAtomsConstants.EDC_FEE_REPORT_NOT_FIRST_INSTALLED_PRICESUM, notFirstInstalledPriceSum);
				caseTitleNameMap.put(IAtomsConstants.EDC_FEE_REPORT_NOT_FIRST_INSTALLED_NOTAX_PRICESUM, notFirstInstalledNoTaxPriceSum);
				//拆機作業費 總計
				caseTitleNameMap.put(IAtomsConstants.EDC_FEE_REPORT_UNINSTALL_PRICESUM, unInstallPriceSum);
				caseTitleNameMap.put(IAtomsConstants.EDC_FEE_REPORT_UNINSTALL_PRICE_NOTAX_SUM, unInstallPriceNoTaxSum);
				//設定費 總計
				caseTitleNameMap.put(IAtomsConstants.EDC_FEE_REPORT_SETTING_PRICESUM, settingPriceSum);
				caseTitleNameMap.put(IAtomsConstants.EDC_FEE_REPORT_SETTING_PRICE_NOTAX_SUM, settingPriceNoTaxSum);
				//急件 特急件 總計
				caseTitleNameMap.put(IAtomsConstants.EDC_FEE_REPORT_FAST_SUM, fastSum);
				caseTitleNameMap.put(IAtomsConstants.EDC_FEE_REPORT_FAST_NOTAX_SUM, fastNoTaxSum);
				caseTitleNameMap.put(IAtomsConstants.EDC_FEE_REPORT_POS_SUM, posSum);
				//ECR線材總費用
				caseTitleNameMap.put(IAtomsConstants.EDC_FEE_REPORT_ECRLINE_SUM_INCASE, ecrlineSumInCase);
				caseTitleNameMap.put(IAtomsConstants.EDC_FEE_REPORT_ECRLINE_NOTAX_SUM_INCASE, ecrlineNoTaxSumInCase);
				//網路線費用總和
				caseTitleNameMap.put(IAtomsConstants.EDC_FEE_REPORT_NETWORK_ROUTE_SUMINCASE, networkRouteSumInCase);
				caseTitleNameMap.put(IAtomsConstants.EDC_FEE_REPORT_NETWORK_ROUTE_NOTAX_SUMINCASE, networkRouteNoTaxSumInCase);
				criteria.setParameters(caseTitleNameMap);
				criterias.add(criteria);
				
				//維修耗材費用-AO已回覆扣款方式
				if(srmPaymentInfoDTOList.size() == 0) {
					SrmPaymentInfoDTO paymentInfoDTO = new SrmPaymentInfoDTO();
					srmPaymentInfoDTOList.add(paymentInfoDTO);
				}
				criteria = new JasperReportCriteriaDTO();
				criteria.setAutoBuildJasper(false);
				criteria.setResult(srmPaymentInfoDTOList);
				//設置所需報表的Name
				criteria.setJrxmlName(IAtomsConstants.FEE_PAYMENT_INFO_REPORT_CH_NAME);
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				//設置報表Name
				criteria.setSheetName(i18NUtil.getName(IAtomsConstants.EDC_FEE_REPORT_SHEET_NAME_PAYMENT_INFO_REPLY));
				//criteria.setReportFileName(fileName);
				criterias.add(criteria);
				
				//維修耗材費用-AO未回覆扣款方式
				if(paymentInfoDTOList.size() == 0) {
					SrmPaymentInfoDTO paymentInfoDTO = new SrmPaymentInfoDTO();
					paymentInfoDTOList.add(paymentInfoDTO);
				}
				criteria = new JasperReportCriteriaDTO();
				criteria.setAutoBuildJasper(false);
				criteria.setResult(paymentInfoDTOList);
				//設置所需報表的Name
				criteria.setJrxmlName(IAtomsConstants.FEE_PAYMENT_INFO_REPORT_CH_NAME);
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				//設置報表Name
				criteria.setSheetName(i18NUtil.getName(IAtomsConstants.EDC_FEE_REPORT_SHEET_NAME_PAYMENT_INFO_NOT_REPLY));
				criteria.setReportFileName(fileName);
				criterias.add(criteria);
				ReportExporter.exportReportForSheetsToFile(criterias, pathBuffer.toString());
				pathBuffer.append(fileName).append(JasperReportCriteriaDTO.REPORT_EXT_NAME_MSEXCEL);
				attachments = new String[]{pathBuffer.toString()};
				for (ReportSettingDTO reportSettingDTO : reportSettingDTOs) {
					//郵件內容
					Map<String, Object> variables = new HashMap<String, Object>();
					variables.put("reportDate", DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMMDD));
					variables.put("shortName", reportSettingDTO.getCustomerName());
					variables.put("toMail", reportSettingDTO.getRecipient());
					variables.put("ccMail", reportSettingDTO.getCopy());
					try {
						this.mailComponent.sendMailTo(null, reportSettingDTO.getRecipient(), subjectTemplate, attachments, textTemplate, variables);
					} catch (Exception e) {
						LOGGER.error("sendReportMail()---> sendMailTo():", "DataAccess Exception:", e);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("toReport() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		
	}
	/**
	 * Purpose:根據類別獲取name
	 * @author HermanWang
	 * @param workCategory：類別
	 * @return String
	 */
	private WorkFeeSetting getworkFeeNameByWorkCategory(String workCategory){
		WorkFeeSetting workFeeSettingDTO = new WorkFeeSetting();
		try {
			for (WorkFeeSetting WorkFeeSettingDTO : workFeeSetting) {
				if(workCategory.equals(WorkFeeSettingDTO.getWorkCategory())) {
					workFeeSettingDTO = WorkFeeSettingDTO;
					break;
				}
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".getFeeInfoByCaseInfo(SessionContext sessionContext):" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return workFeeSettingDTO;
	}
	/**
	 * Purpose:根據分組字段分組，返回分組后的數據
	 * @author HermanWang
	 * @param groupList
	 * @param srmCaseHandleInfoDTO：案件信息dto
	 * @return List<String>：分組之後的數據
	 */
	private String getFeeInfoByCaseInfo(List<String> groupList, SrmCaseHandleInfoDTO srmCaseHandleInfoDTO){
		String caseInfoString = IAtomsConstants.MARK_EMPTY_STRING;
		try {
			for(int i = 0; i < groupList.size(); i++) {
				String result = IAtomsConstants.MARK_EMPTY_STRING;
				//拿到要分組的屬性的get方法
				String methodName = "get"+groupList.get(i).substring(0, 1).toUpperCase()+groupList.get(i).substring(1);
    			//利用反射從實體類裡面 取出來 對應的值
				Object invokeMethod = BeanUtils.invokeMethod(srmCaseHandleInfoDTO, methodName, null, null);
				Class fieldType = BeanUtils.getFieldType(srmCaseHandleInfoDTO, groupList.get(i));
				if(invokeMethod != null) {
					//date 或者 Timestamp
        			if(fieldType == BeanUtils.UTIL_DATE_CLASS || fieldType == BeanUtils.SQL_DATE_CLASS
        					|| fieldType == BeanUtils.TIMESTAMP_CLASS || fieldType == BeanUtils.TIME_CLASS) {
        				if(invokeMethod != null) {
        					result = DateTimeUtils.toString((Date)invokeMethod, DateTimeUtils.DT_FMT_YYYYMMDD);
        				} else {
        					result = IAtomsConstants.MARK_EMPTY_STRING;
        				}
        			//String
        			} else if(fieldType == BeanUtils.STRING_CLASS) {
        				result = invokeMethod.toString();
        			}
				}
				caseInfoString += result;
			}
			
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".getFeeInfoByCaseInfo(SessionContext sessionContext):" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return caseInfoString;
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
	 * @return the commonTransactionTypeList
	 */
	public List<String> getCommonTransactionTypeList() {
		return commonTransactionTypeList;
	}
	/**
	 * @param commonTransactionTypeList the commonTransactionTypeList to set
	 */
	public void setCommonTransactionTypeList(List<String> commonTransactionTypeList) {
		this.commonTransactionTypeList = commonTransactionTypeList;
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
	 * @return the commModeIdList
	 */
	public List<String> getCommModeIdList() {
		return commModeIdList;
	}
	/**
	 * @param commModeIdList the commModeIdList to set
	 */
	public void setCommModeIdList(List<String> commModeIdList) {
		this.commModeIdList = commModeIdList;
	}
	/**
	 * @return the srmCaseAssetLinkDAO
	 */
	public ISrmCaseAssetLinkDAO getSrmCaseAssetLinkDAO() {
		return srmCaseAssetLinkDAO;
	}
	/**
	 * @param srmCaseAssetLinkDAO the srmCaseAssetLinkDAO to set
	 */
	public void setSrmCaseAssetLinkDAO(ISrmCaseAssetLinkDAO srmCaseAssetLinkDAO) {
		this.srmCaseAssetLinkDAO = srmCaseAssetLinkDAO;
	}
	/**
	 * @return the paymentStatusList
	 */
	public List<String> getPaymentStatusList() {
		return paymentStatusList;
	}
	/**
	 * @param paymentStatusList the paymentStatusList to set
	 */
	public void setPaymentStatusList(List<String> paymentStatusList) {
		this.paymentStatusList = paymentStatusList;
	}
	/**
	 * @return the paymentTypeList
	 */
	public List<String> getPaymentTypeList() {
		return paymentTypeList;
	}
	/**
	 * @param paymentTypeList the paymentTypeList to set
	 */
	public void setPaymentTypeList(List<String> paymentTypeList) {
		this.paymentTypeList = paymentTypeList;
	}
	/**
	 * @return the paymentInfoDAO
	 */
	public ISrmPaymentInfoDAO getPaymentInfoDAO() {
		return paymentInfoDAO;
	}
	/**
	 * @param paymentInfoDAO the paymentInfoDAO to set
	 */
	public void setPaymentInfoDAO(ISrmPaymentInfoDAO paymentInfoDAO) {
		this.paymentInfoDAO = paymentInfoDAO;
	}
	
}