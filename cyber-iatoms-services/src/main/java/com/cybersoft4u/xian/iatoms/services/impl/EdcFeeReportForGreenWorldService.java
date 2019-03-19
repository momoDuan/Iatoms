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
/**
 * Purpose: 維護費報表(綠界格式)
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2017/9/12
 * @MaintenancePersonnel HermanWang
 */
public class EdcFeeReportForGreenWorldService extends AtomicService implements IReportService {
	/**
	 * 記錄日誌
	 */
	private static final CafeLog LOGGER	= CafeLogFactory.getLog(EdcFeeReportForJdwService.class);
	/**
	 * 注入報表發送功能設定DAO
	 */
	private IReportSettingDAO reportSettingDAO;
	/**
	 * 注入設備庫存歷史月檔的dao
	 */
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
	 * 設備狀態 報廢、銷毀 列表
	 */
	private List<String> repoStatusList;
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
	public EdcFeeReportForGreenWorldService() {
		super();
	}
	/**
	 * 台新作业别費用單價
	 */
	private List<WorkFeeSetting> TaiXinworkFeeSetting;
	/**
	 * 
	 * Purpose:維護費報表(綠界格式)的配製方法
	 * @author HermanWang
	 * @throws ServiceException
	 * @return void
	 */
	public void toReport() throws ServiceException {
		CompanyDTO companyDTO = this.companyDAO.getCompanyByCompanyCode(IAtomsConstants.PARAMTER_COMPANY_CODE_GREEN_WORLD);
		if (companyDTO != null) {
			sendReportMail(DateTimeUtils.getCurrentDate(), companyDTO.getCompanyId(), IAtomsConstants.REPORT_TCB_GREEN_WORLD_SIXTEEN);
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
			//帳務年月 月底 -1年
			Timestamp date = DateTimeUtils.toTimestamp(DateTimeUtils.toString(calLastYearEnd.getTime(), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH));
			
			List<ReportSettingDTO> reportSettingDTOs = this.reportSettingDAO.getDetailList(customerId, reportCode);
			if (!CollectionUtils.isEmpty(reportSettingDTOs)) {
				JasperReportCriteriaDTO criteria = null;
				//郵件主題模板
				String subjectTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.EDC_FEE_REPORT_FOR_GREEN_WORLD_SUBJECT_TEMPLATE;
				//郵件內容模板
				String textTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.EDC_FEE_REPORT_FOR_GREEN_WORLD_TEXT_TEMPLATE;
				//獲取臨時保存路徑-時間區
				String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD);
				//獲取臨時路徑
				String path = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH);
				List<JasperReportCriteriaDTO> criterias = new ArrayList<JasperReportCriteriaDTO>();
				pathBuffer = new StringBuffer();
				String[] attachments = null;
				CompanyDTO companyDTO = this.companyDAO.getCompanyByCompanyCode(IAtomsConstants.PARAM_TSB_EDC);
				String Taixin = IAtomsConstants.MARK_EMPTY_STRING;
				if (companyDTO != null) {
					Taixin = companyDTO.getCompanyId();
				}
				pathBuffer.append(path).append(File.separator).append(yearMonthDay).append(File.separator).append(IAtomsConstants.UC_NO_AMM_06050);
				pathBuffer.append(File.separator);
				//設備信息(第一個sql)
				List<DmmRepositoryDTO> DmmRepositoryDTOList =  this.dmmRepositoryHistoryMonthlyDAO.assetInfoListGreenWorld(DateTimeUtils.toString(startDate, DateTimeUtils.DT_FMT_YYYYMM), customerId, startDate, endDate);
				//(維護費)聯天設備(第2個sql)
				List<DmmRepositoryDTO> dmmRepositoryDTOChatList = this.dmmRepositoryHistoryMonthlyDAO.feeChatAssetList(DateTimeUtils.toString(startDate,  DateTimeUtils.DT_FMT_YYYYMM), customerId);
				//案件信息(第3個sql)
				List<SrmCaseHandleInfoDTO> SrmCaseHandleInfoDTOList =  this.srmCaseHandleInfoDAO.getCaseListInGreenWorld(caseStatusList, commonTransactionTypeList, customerId, startDate, endDate, false , null);
				List<SrmCaseHandleInfoDTO> taiXinSrmCaseHandleInfoDTOList = this.srmCaseHandleInfoDAO.getCaseListInGreenWorld(caseStatusList, commonTransactionTypeList, customerId, startDate, endDate, true, Taixin);
				//案件通過設備鏈接檔單價分組的信息(第4個sql)
				List<SrmCaseAssetLinkDTO> srmCaseAssetLinkDTOList = this.srmCaseAssetLinkDAO.getCaseAssetLinkInGreenWorld(caseStatusList, customerId, startDate, endDate);
				//(維護費)設備(第5個sql)
				List<DmmRepositoryDTO> DmmRepositoryDTOLists =  this.dmmRepositoryHistoryMonthlyDAO.feeChatAssetListGreenWorld(repoStatusList, DateTimeUtils.toString(startDate, DateTimeUtils.DT_FMT_YYYYMM), customerId);
				//(維護費)台新設備(第6個sql)
				List<DmmRepositoryDTO> feeTaiXinAssetList = this.dmmRepositoryHistoryMonthlyDAO.feeTaiXinAssetList(DateTimeUtils.toString(startDate, DateTimeUtils.DT_FMT_YYYYMM), customerId, Taixin);
				//案件通過台新設備鏈接檔單價分組的信息
				List<SrmCaseAssetLinkDTO> taiXinSrmCaseAssetLinkDTOList = this.srmCaseAssetLinkDAO.getCaseAssetLinkInTaiXin(caseStatusList, customerId, startDate, endDate, Taixin);
				//求償維修耗材費用-AO已回覆扣款方式
				List<SrmPaymentInfoDTO> srmPaymentInfoDTOList = this.paymentInfoDAO.paymentInfoListToGreenWorld(paymentTypeList, customerId);
				//維修耗材費用-AO未回覆扣款方式
				List<SrmPaymentInfoDTO> paymentInfoDTOList = this.paymentInfoDAO.paymentInfoListToGreenWorldNoTax(paymentStatusList, customerId);
				//設備鏈接 單價
				List<SrmCaseAssetLinkDTO> srmCaseLinkSuppliesDTOList = this.srmCaseAssetLinkDAO.getCaseLinkSuppliesInGreenWorld(caseStatusList, customerId, startDate, endDate);
				//台新設備鏈接 單價
				List<SrmCaseAssetLinkDTO> taixinSrmCaseLinkSuppliesDTOList = this.srmCaseAssetLinkDAO.getCaseLinkSuppliesInTaiXin(caseStatusList, customerId, startDate, endDate, Taixin);
				//裝機的總數量
				int installCount = 0;
				//首裝買斷機 數量
				int isBuyoutFirstInstall = 0;
				//拆級數量
				int unInstallCount = 0;
				//拆機無須收費
				int unInstallFeeCount = 0;
				//首拆買斷機數量
				int firstUnInstallCount = 0;
				//並機或者異動  處理方式=到場處理 的數量
				int updateOrMergeCount = 0;
				//並機或者異動 處理方式=軟排 的數量
				int updateSoftDispatch = 0;
				//使用量
				int inUseCount = 0;
				//訂單數兩
				int OrderCount = 0;
				//報廢兩
				int ScrapCount = 0;
				//備品量
				Double readyCount = new Double(0);
				//急件
				int fast = 0;
				//特急件
				int extra = 0;
				//查核
				int checkCount = 0;
				//首裝買斷機的信息
				List<String> installCaseInfo = new ArrayList<String>();
				//查核
				List<String> checkCaseInfo = new ArrayList<String>();
				// 維護費報表map key是設備名稱  Value是 該設備名稱分組之後的 設備的dtolist
				Map<String, List<DmmRepositoryDTO>> dmmRepositoryListMap = new HashMap<String, List<DmmRepositoryDTO>>();
				Map<String, List<DmmRepositoryDTO>> dmmRepositoryMainListMap = new HashMap<String, List<DmmRepositoryDTO>>();
				Map<String, Integer> caseCategoryMap = new HashMap<String, Integer>();
				Map<String, Integer> TaixinaseCategoryMap = new HashMap<String, Integer>();
				int fastSum = 0;
				int installedPriceSum = 0;
				int settingPriceSum = 0;
				int unInstallPriceSum = 0;
				int ecrlineSumInCase = 0;
				int networkRouteSumInCase = 0;
				for (SrmCaseHandleInfoDTO srmCaseHandleInfoDTO : SrmCaseHandleInfoDTOList) {
					//作業明細中,更換案件類型,序號從1開始.
					if(caseCategoryMap.get(srmCaseHandleInfoDTO.getCaseCategory()) == null) {
						caseCategoryMap.put(srmCaseHandleInfoDTO.getCaseCategory(), 1);
						srmCaseHandleInfoDTO.setRowId(String.valueOf(1));
					} else {
						caseCategoryMap.put(srmCaseHandleInfoDTO.getCaseCategory(), caseCategoryMap.get(srmCaseHandleInfoDTO.getCaseCategory()) + 1);
						srmCaseHandleInfoDTO.setRowId(String.valueOf(caseCategoryMap.get(srmCaseHandleInfoDTO.getCaseCategory())));
					}
					//(作業明細)案件中【案件類別=裝機】
					if(IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())) {
						installCount ++;
						//首裝買斷機
						if(IAtomsConstants.YES.equals(srmCaseHandleInfoDTO.getFirstInstall())) {
							isBuyoutFirstInstall ++;
						} else if (IAtomsConstants.NO.equals(srmCaseHandleInfoDTO.getFirstInstall())){
							List<String> groupList = new ArrayList<String>();
							groupList.add("assetName");
							groupList.add("merchantCode");
							groupList.add("completeDate");
							groupList.add("installedAdress");
							String caseInfoString = getFeeInfoByCaseInfo(groupList, srmCaseHandleInfoDTO);
							WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("INSTALLED");
							if(!installCaseInfo.contains(caseInfoString)) {
								installCaseInfo.add(caseInfoString);
								srmCaseHandleInfoDTO.setIsSamePlaceInstall(null);
								srmCaseHandleInfoDTO.setInstallPrice(workFeeDTO.getFirstPrice());
								installedPriceSum = installedPriceSum + workFeeDTO.getFirstPrice();
							} else {
								srmCaseHandleInfoDTO.setIsSamePlaceInstall("是");
								srmCaseHandleInfoDTO.setInstallPrice(workFeeDTO.getOtherPrice());
								installedPriceSum = installedPriceSum + workFeeDTO.getOtherPrice();
							}
							if(srmCaseHandleInfoDTO.getNotFirstInstalledPrice() == null) {
								srmCaseHandleInfoDTO.setNotFirstInstalledPrice(0);
							}
						}
					} else if(IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())) {
						unInstallCount ++;
						//首拆買斷機
						if(IAtomsConstants.YES.equals(srmCaseHandleInfoDTO.getFirstUnInstall())) {
							firstUnInstallCount ++;
						//且【拆機類型=業務自拆】數量
						} else if("SERVICE_SELF_UNINSTALL".equals(srmCaseHandleInfoDTO.getUninstallType())) {
							unInstallFeeCount ++;
						} else {
							WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("UNINSTALLED");
							srmCaseHandleInfoDTO.setUnInstallPrice(workFeeDTO.getFirstPrice());
							unInstallPriceSum = unInstallPriceSum + workFeeDTO.getFirstPrice();
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
						if(("ARRIVE_PROCESS").equals(srmCaseHandleInfoDTO.getProcessType())) {
							if(IAtomsConstants.NO.equals(srmCaseHandleInfoDTO.getSameInstalled())) {
								WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("ARRIVED");
								srmCaseHandleInfoDTO.setSettingPrice(workFeeDTO.getFirstPrice());
								settingPriceSum = settingPriceSum + workFeeDTO.getFirstPrice();
								updateOrMergeCount ++;
							}
						//【處理方式 = 軟派】
						} else if(("SOFT_DISPATCH").equals(srmCaseHandleInfoDTO.getProcessType())) {
							updateSoftDispatch ++;
							WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("REMOTE");
							srmCaseHandleInfoDTO.setSettingPrice(workFeeDTO.getFirstPrice());
							settingPriceSum = settingPriceSum + workFeeDTO.getFirstPrice();
						}
					//查核
					} else if(IAtomsConstants.CASE_CATEGORY.CHECK.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())) {
						checkCount++;
						if(checkCount > 5) {
							List<String> groupList = new ArrayList<String>();
							groupList.add("assetName");
							groupList.add("merchantCode");
							groupList.add("completeDate");
							if(IAtomsConstants.YES.equals(srmCaseHandleInfoDTO.getContactIsBussinessAddress())) {
								groupList.add("bussinessAddress");
							} else {
								groupList.add("contactAddress");
							}
							String caseInfoString = getFeeInfoByCaseInfo(groupList, srmCaseHandleInfoDTO);
							if(!checkCaseInfo.contains(caseInfoString)) {
								checkCaseInfo.add(caseInfoString);
							}
						}
					}
					//案件類型=急件
					if("FAST".equals(srmCaseHandleInfoDTO.getCaseType())) {
						fast ++;
						if(fast > 5) {
							WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("FAST");
							srmCaseHandleInfoDTO.setDispatchPrice(workFeeDTO.getFirstPrice());
							fastSum = fastSum + workFeeDTO.getFirstPrice();
						} else {
							srmCaseHandleInfoDTO.setDispatchPrice(null);
						}
					//案件類型=特急件
					} else if("EXTRA".equals(srmCaseHandleInfoDTO.getCaseType())) {
						extra ++;
						if(extra > 5) {
							WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("EXTRA");
							srmCaseHandleInfoDTO.setDispatchPrice(workFeeDTO.getFirstPrice());
							
						} else {
							srmCaseHandleInfoDTO.setDispatchPrice(null);
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
									if("ECRLINE".equals(srmCaseAssetLinkDTO.getItemCategory())) {
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
						}
						//網路線
						if(!networkRouteSum.equals(0)) {
							srmCaseHandleInfoDTO.setNetLineInFee(networkRouteSum);
						}
						ecrlineSumInCase = ecrlineSumInCase + ecrlineSum;
						networkRouteSumInCase = networkRouteSumInCase + networkRouteSum;
					}
				}
				//台新作業裝機數量
				int taiXinInstallCount = 0;
				//首裝買斷機的信息
				List<String> taiXinInstallCaseInfo = new ArrayList<String>();
				//台新拆機鍵的數量
				int taiXinUnInstallCount = 0;
				//台新拆機 業務自拆的數量
				int taiXinUnInstallFeeCount = 0;
				//台新 參數設定(到場)
				int taiXinUpdateOrMergeCount = 0;
				//台新 參數設定(遠端軟派 不到場)
				int taiXinUpdateSoftDispatch = 0;
				//台新 急件加收(每月5台免費、第6台(含)以上)
				int taiXinFast = 0;
				//台新  查核
				int taiXinCheckCount = 0;
				//台新  查核
				List<String> taiXinCheckCaseInfo = new ArrayList<String>();
				//循環台新案件信息
				for (SrmCaseHandleInfoDTO taiXinsrmCaseHandleInfoDTO : taiXinSrmCaseHandleInfoDTOList) {
					if(TaixinaseCategoryMap.get(taiXinsrmCaseHandleInfoDTO.getCaseCategory()) == null) {
						TaixinaseCategoryMap.put(taiXinsrmCaseHandleInfoDTO.getCaseCategory(), 1);
						taiXinsrmCaseHandleInfoDTO.setRowId(String.valueOf(1));
					} else {
						TaixinaseCategoryMap.put(taiXinsrmCaseHandleInfoDTO.getCaseCategory(), TaixinaseCategoryMap.get(taiXinsrmCaseHandleInfoDTO.getCaseCategory()) + 1);
						taiXinsrmCaseHandleInfoDTO.setRowId(String.valueOf(TaixinaseCategoryMap.get(taiXinsrmCaseHandleInfoDTO.getCaseCategory())));
					}
					//裝機件： 台新作業費案件中【案件類別=裝機】且【設備OWNER=台新】數量
					if(IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(taiXinsrmCaseHandleInfoDTO.getCaseCategory())) {
						//【設備OWNER=台新】數量
						if(Taixin.equals(taiXinsrmCaseHandleInfoDTO.getAssetOwner())) {
							taiXinInstallCount ++;
							List<String> groupList = new ArrayList<String>();
							groupList.add("assetName");
							groupList.add("merchantCode");
							groupList.add("completeDate");
							groupList.add("installedAdress");
							String caseInfoString = getFeeInfoByCaseInfo(groupList, taiXinsrmCaseHandleInfoDTO);
							WorkFeeSetting workFeeDTO = getworkFeeNameByTaiXinWorkCategory("INSTALLED");
							//依【設備、MID、完修日、裝機地址】統計數量超過1台，就寫 是，只有1台  就 空白
							if(!taiXinInstallCaseInfo.contains(caseInfoString)) {
								taiXinInstallCaseInfo.add(caseInfoString);
								taiXinsrmCaseHandleInfoDTO.setIsSamePlaceInstall(null);
								//裝(併、移機)費用  第一台原價，第二台之後（含）特價。
								taiXinsrmCaseHandleInfoDTO.setInstallPrice(workFeeDTO.getFirstPrice());
							} else {
								taiXinsrmCaseHandleInfoDTO.setIsSamePlaceInstall("是");
								//裝(併、移機)費用 第一台原價，第二台之後（含）特價。
								taiXinsrmCaseHandleInfoDTO.setInstallPrice(workFeeDTO.getOtherPrice());
							}
						}
					} else if(IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode().equals(taiXinsrmCaseHandleInfoDTO.getCaseCategory())) {
						//【設備OWNER=台新】數量
						if(Taixin.equals(taiXinsrmCaseHandleInfoDTO.getAssetOwner())) {
							taiXinUnInstallCount ++;
							//且【拆機類型=業務自拆】
							if("SERVICE_SELF_UNINSTALL".equals(taiXinsrmCaseHandleInfoDTO.getUninstallType())) {
								taiXinUnInstallFeeCount ++;
							} else {
								WorkFeeSetting workFeeDTO = getworkFeeNameByTaiXinWorkCategory("UNINSTALLED");
								taiXinsrmCaseHandleInfoDTO.setUnInstallPrice(workFeeDTO.getFirstPrice());
							}
						}
					//並機或者異動
					} else if(IAtomsConstants.CASE_CATEGORY.MERGE.getCode().equals(taiXinsrmCaseHandleInfoDTO.getCaseCategory()) 
							|| IAtomsConstants.CASE_CATEGORY.UPDATE.getCode().equals(taiXinsrmCaseHandleInfoDTO.getCaseCategory())) {
						//且【處理方式=到場處理 且 是否同裝機作業=否
						if(("ARRIVE_PROCESS").equals(taiXinsrmCaseHandleInfoDTO.getProcessType())) {
							if(IAtomsConstants.NO.equals(taiXinsrmCaseHandleInfoDTO.getSameInstalled())) {
								//且【設備OWNER=台新】數量
								if(Taixin.equals(taiXinsrmCaseHandleInfoDTO.getAssetOwner())) {
									WorkFeeSetting workFeeDTO = getworkFeeNameByTaiXinWorkCategory("ARRIVED");
									taiXinsrmCaseHandleInfoDTO.setSettingPrice(workFeeDTO.getFirstPrice());
									taiXinUpdateOrMergeCount ++;
								}
							}
						//【處理方式 = 軟派】
						} else if(("SOFT_DISPATCH").equals(taiXinsrmCaseHandleInfoDTO.getProcessType())) {
							if(Taixin.equals(taiXinsrmCaseHandleInfoDTO.getAssetOwner())) {
								WorkFeeSetting workFeeDTO = getworkFeeNameByTaiXinWorkCategory("REMOTE");
								taiXinsrmCaseHandleInfoDTO.setSettingPrice(workFeeDTO.getFirstPrice());
								taiXinUpdateSoftDispatch ++;
							}
						}
						if(StringUtils.hasText(taiXinsrmCaseHandleInfoDTO.getPeripheralsUpdate())) {
							if(taiXinsrmCaseHandleInfoDTO.getPeripheralsUpdate().equals(IAtomsConstants.YES)) {
								taiXinsrmCaseHandleInfoDTO.setPeripheralsUpdate(IAtomsConstants.COLUMN_V);
							} else {
								taiXinsrmCaseHandleInfoDTO.setPeripheralsUpdate(IAtomsConstants.MARK_EMPTY_STRING);
							}
						}
					//查核
					} else if(IAtomsConstants.CASE_CATEGORY.CHECK.getCode().equals(taiXinsrmCaseHandleInfoDTO.getCaseCategory())) {
						if(Taixin.equals(taiXinsrmCaseHandleInfoDTO.getAssetOwner())) {
							taiXinCheckCount++;
							if(taiXinCheckCount > 5) {
								List<String> groupList = new ArrayList<String>();
								groupList.add("assetName");
								groupList.add("merchantCode");
								groupList.add("completeDate");
								if(IAtomsConstants.YES.equals(taiXinsrmCaseHandleInfoDTO.getContactIsBussinessAddress())) {
									groupList.add("bussinessAddress");
								} else {
									groupList.add("contactAddress");
								}
								String caseInfoString = getFeeInfoByCaseInfo(groupList, taiXinsrmCaseHandleInfoDTO);
								if(!taiXinCheckCaseInfo.contains(caseInfoString)) {
									taiXinCheckCaseInfo.add(caseInfoString);
								}
							}
						}
					}
					//案件類型=急件
					if("FAST".equals(taiXinsrmCaseHandleInfoDTO.getCaseType())) {
						if(Taixin.equals(taiXinsrmCaseHandleInfoDTO.getAssetOwner())) {
							taiXinFast ++;
							if(taiXinFast > 5) {
								WorkFeeSetting workFeeDTO = getworkFeeNameByTaiXinWorkCategory("FAST");
								taiXinsrmCaseHandleInfoDTO.setDispatchPrice(workFeeDTO.getFirstPrice());
							} else {
								taiXinsrmCaseHandleInfoDTO.setDispatchPrice(null);
							}
						}
					}
					//若【案件類別=裝機、異動】使用之ECR線數量和單價，計算總價
					if(IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(taiXinsrmCaseHandleInfoDTO.getCaseCategory()) 
							|| IAtomsConstants.CASE_CATEGORY.UPDATE.getCode().equals(taiXinsrmCaseHandleInfoDTO.getCaseCategory())) {
						if(Taixin.equals(taiXinsrmCaseHandleInfoDTO.getAssetOwner())) {
							Integer ecrlineSum = 0;
							Integer networkRouteSum = 0;
							for (SrmCaseAssetLinkDTO srmCaseAssetLinkDTO : taiXinSrmCaseAssetLinkDTOList) {
								if(srmCaseAssetLinkDTO.getCaseId().equals(taiXinsrmCaseHandleInfoDTO.getCaseId())) {
									if(srmCaseAssetLinkDTO.getPrice() != null) {
										if("ECRLINE".equals(srmCaseAssetLinkDTO.getItemCategory())) {
											ecrlineSum = ecrlineSum + srmCaseAssetLinkDTO.getPrice().intValue();
										} else {
											networkRouteSum = networkRouteSum + srmCaseAssetLinkDTO.getPrice().intValue();
										}
									}
								}
							}
							//ECR線材費用
							if(!ecrlineSum.equals(0)) {
								taiXinsrmCaseHandleInfoDTO.setEcrLineInFee(ecrlineSum);
							}
							//網路線
							if(!networkRouteSum.equals(0)) {
								taiXinsrmCaseHandleInfoDTO.setNetLineInFee(networkRouteSum);
							}
						}
					}
				}
				//循環(維護費)聯天設備
				for(DmmRepositoryDTO DmmRepositoryChatDTO : dmmRepositoryDTOChatList) {
					//訂單數量
					OrderCount ++;
					//狀態等於使用中
					if(IAtomsConstants.PARAM_ASSET_STATUS_IN_USE.equals(DmmRepositoryChatDTO.getStatus())) {
						//使用量
						inUseCount ++;
					}
					// 報廢量：設備中，狀態=報廢、銷毀的數量
					if((IAtomsConstants.PARAM_ASSET_STATUS_DISABLED.equals(DmmRepositoryChatDTO.getStatus())
							|| IAtomsConstants.PARAM_ASSET_STATUS_DESTROY.equals(DmmRepositoryChatDTO.getStatus()))) {
						ScrapCount ++;
//					} else {
//						//若設備=已啟用；設備型號中不含聯天字眼；
//						//設備狀態<>報廢、銷毀；(客戶驗收日期 + 1年) <= 帳務年月月底，則取費用總表中EDC維護費用(第2年始收費)中配置的單價
//						if(IAtomsConstants.YES.equals(DmmRepositoryChatDTO.getIsEnabled())) {
//							if(DmmRepositoryChatDTO.getCheckedDate() != null) {
//								if(date.compareTo(DmmRepositoryChatDTO.getCheckedDate()) > 0) {
//									WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("MAINTENANCE_FEE");
//									DmmRepositoryChatDTO.setFee(workFeeDTO.getFirstPrice());
//								}
//							}
//						}
//					}
					}
				}
				//備用量  訂單數量 - 報廢量)*5%
				if(OrderCount < ScrapCount) {
					readyCount = new Double(0);
				} else {
					//readyCount = (OrderCount - ScrapCount)*0.05;
					readyCount = MathUtils.multiply((double)(OrderCount - ScrapCount), 0.05);
				}
				int maintenanceFeeCount = 0;
				if((inUseCount+readyCount) > (OrderCount-ScrapCount)) {
					maintenanceFeeCount = OrderCount-ScrapCount;
				} else {
					BigDecimal readyCountBig = new BigDecimal(String.valueOf(readyCount));
			        double readyCountDou = readyCountBig.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
					maintenanceFeeCount = (int)(inUseCount + readyCountDou);
				}
				int Count = 0;
				for(DmmRepositoryDTO DmmRepositoryChatDTO : dmmRepositoryDTOChatList) {
					//狀態狀態!=報廢、銷毀的數量
					if(!IAtomsConstants.PARAM_ASSET_STATUS_DISABLED.equals(DmmRepositoryChatDTO.getStatus())
							&& !IAtomsConstants.PARAM_ASSET_STATUS_DESTROY.equals(DmmRepositoryChatDTO.getStatus())) {
						if(Count < maintenanceFeeCount) {
							WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("MAINTENANCE_FEE");
							DmmRepositoryChatDTO.setFee(workFeeDTO.getFirstPrice());
							Count++;
						}
					}
				}
				List<DmmRepositoryDTO> dmmRepositoryDTOList = null;
				for(DmmRepositoryDTO dmmRepositoryDTO : DmmRepositoryDTOList) {
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
				List<DmmRepositoryDTO> dmmRepositoryDTOMainList = null;
				for(DmmRepositoryDTO dmmRepositoryDTO : DmmRepositoryDTOLists) {
					//設備sheet是 不定的，所以統計有多少中，每一種多少個dto
					if(dmmRepositoryMainListMap.get(dmmRepositoryDTO.getName()) != null) {
						dmmRepositoryDTOMainList = dmmRepositoryMainListMap.get(dmmRepositoryDTO.getName());
						dmmRepositoryDTOMainList.add(dmmRepositoryDTO);
					} else {
						dmmRepositoryDTOMainList = new ArrayList<DmmRepositoryDTO>();
						dmmRepositoryDTOMainList.add(dmmRepositoryDTO);
					}
					dmmRepositoryMainListMap.put(dmmRepositoryDTO.getName(), dmmRepositoryDTOMainList);
					WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("MAINTENANCE_FEE");
					dmmRepositoryDTO.setFee(workFeeDTO.getFirstPrice());
				}
				//若設備=已啟用；設備型號中不含聯天字眼；設備狀態<>報廢、銷毀；
				//(客戶驗收日期 + 1年) <= 帳務年月月底，
				//則取費用總表中EDC維護費用(第2年始收費)中配置的單價
				for(DmmRepositoryDTO feeTaiXinRepositoryDTO : feeTaiXinAssetList) {
					//若設備=已啟用
					if(IAtomsConstants.YES.equals(feeTaiXinRepositoryDTO.getIsEnabled())) {
						if(!StringUtils.hasText(feeTaiXinRepositoryDTO.getAssetModel())) {
							feeTaiXinRepositoryDTO.setAssetModel(IAtomsConstants.MARK_EMPTY_STRING);
						}
						//設備型號中不含聯天字眼
						if(feeTaiXinRepositoryDTO.getAssetModel().indexOf("聯天") < 0) {
							//設備狀態<>報廢、銷毀
							if(!IAtomsConstants.PARAM_ASSET_STATUS_DISABLED.equals(feeTaiXinRepositoryDTO.getStatus()) 
									&& !IAtomsConstants.PARAM_ASSET_STATUS_DESTROY.equals(feeTaiXinRepositoryDTO.getStatus())) {
								//(客戶驗收日期 + 1年) <= 帳務年月月底，
								if(feeTaiXinRepositoryDTO.getCheckedDate() != null) {
									// Bug #3232 維護費報表啟用日期調整   (客戶驗收日 + 1年) > 帳務年月月底/ (帳務年月月底 - 1) < 客戶驗收日
								//	if(date.compareTo(feeTaiXinRepositoryDTO.getCheckedDate()) > 0) {
									if(date.compareTo(feeTaiXinRepositoryDTO.getCheckedDate()) > 0) {
										WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("MAINTENANCE_FEE");
										// 第三個sheet中的 維護費(含稅)
										feeTaiXinRepositoryDTO.setMaintenanceTax(workFeeDTO.getFirstPrice());
									}
								}
							}
						}
					}
				}
				//匯出的結果result
				List<WorkFeeSetting> workFeeSettingList = new ArrayList<WorkFeeSetting>(); 
				List<WorkFeeSetting> taiXinWorkFeeSettingList = new ArrayList<WorkFeeSetting>(); 
				if(workFeeSetting != null) {
					//第一個總表要返回的result
					workFeeSettingList = getworkFeeDTOList(workFeeSettingList, installCount, installCaseInfo,
							isBuyoutFirstInstall, startDate, unInstallCount, firstUnInstallCount, unInstallFeeCount, 
							updateOrMergeCount, updateSoftDispatch, dmmRepositoryMainListMap, inUseCount, readyCount,
							OrderCount, ScrapCount, fast, extra, srmCaseLinkSuppliesDTOList, checkCaseInfo, checkCount);
				}
				if(TaiXinworkFeeSetting != null) {
					//第2個總表要返回的result
					taiXinWorkFeeSettingList = getTaiXinWorkFeeDTOList(taiXinWorkFeeSettingList, taiXinInstallCount, taiXinInstallCaseInfo, 
							taiXinUnInstallCount, taiXinUnInstallFeeCount, taiXinUpdateOrMergeCount, taiXinUpdateSoftDispatch, 
							taiXinFast, taixinSrmCaseLinkSuppliesDTOList, taiXinCheckCount, taiXinCheckCaseInfo);
				}
				//第一個sheet 費用總表
				criteria = new JasperReportCriteriaDTO();
				criteria.setAutoBuildJasper(false);
				criteria.setResult(workFeeSettingList);
				//設置所需報表的Name
				criteria.setJrxmlName(IAtomsConstants.GREEN_WORLD_PRICE_SUM_REPORT_CH_NAME);
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
				countMap.put("yyyy", DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYY));
				criteria.setParameters(countMap);
				//設置報表Name
				StringBuffer fileNameBuffer = new StringBuffer();
				fileNameBuffer.append(DateTimeUtils.toString(startDate, DateTimeUtils.DT_FMT_YYYYMM));
				fileNameBuffer.append(IAtomsConstants.MARK_UNDER_LINE);
				fileNameBuffer.append(reportSettingDTOs.get(0).getCustomerName());
				fileNameBuffer.append(i18NUtil.getName(IAtomsConstants.FEE_REPORT_FOR_GREEN_WORLD_SUBJECT_CH_NAME));
				String fileName = fileNameBuffer.toString();
				criteria.setReportFileName(fileName);
				criteria.setSheetName("費用總表");
				criterias.add(criteria);
				//第二個sheet 台新費用總表
				criteria = new JasperReportCriteriaDTO();
				criteria.setAutoBuildJasper(false);
				criteria.setResult(taiXinWorkFeeSettingList);
				//設置所需報表的Name
				criteria.setJrxmlName(IAtomsConstants.GREEN_WORLD_TX_PRICE_SUM_REPORT_CH_NAME);
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				Integer totalTaiXin = 0;
				for (WorkFeeSetting workFeeSetting : taiXinWorkFeeSettingList) {
					if(workFeeSetting.getSum() == null) {
						workFeeSetting.setSum(0);
					}
					totalTaiXin = totalTaiXin + workFeeSetting.getSum();
				}
				Map taiXinCountMap = new HashMap();
				taiXinCountMap.put("total", totalTaiXin);
				taiXinCountMap.put("yyyy", DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYY));
				criteria.setParameters(taiXinCountMap);
				criteria.setReportFileName(fileName);
				criteria.setSheetName("台新費用總表");
				criterias.add(criteria);
				
				//第三個sheet 台新設備維護費
				if(feeTaiXinAssetList.size() == 0) {
					DmmRepositoryDTO dmmRepositoryDTO = new DmmRepositoryDTO();
					feeTaiXinAssetList.add(dmmRepositoryDTO);
				}
				criteria = new JasperReportCriteriaDTO();
				criteria.setAutoBuildJasper(false);
				criteria.setResult(feeTaiXinAssetList);
				//設置所需報表的Name
				criteria.setJrxmlName(IAtomsConstants.GREEN_WORLD_TX_ASSET_MAINTAIN);
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				//設置報表Name
				criteria.setSheetName("台新設備維護費");
				criteria.setReportFileName(fileName);
				criterias.add(criteria);
				
				//第四個sheet 台新作業費
				if(taiXinSrmCaseHandleInfoDTOList.size() == 0) {
					SrmCaseHandleInfoDTO srmCaseHandleInfoDTO = new SrmCaseHandleInfoDTO();
					taiXinSrmCaseHandleInfoDTOList.add(srmCaseHandleInfoDTO);
				}
				criteria = new JasperReportCriteriaDTO();
				criteria.setAutoBuildJasper(false);
				criteria.setResult(taiXinSrmCaseHandleInfoDTOList);
				//設置所需報表的Name
				criteria.setJrxmlName(IAtomsConstants.GREEN_WORLD_TX_CASE_INFO_REPORT);
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				//設置報表Name
				criteria.setSheetName("台新作業費");
				criteria.setReportFileName(fileName);
				criterias.add(criteria);
				
				//第五個第六個sheet 設備明細 循環顯示
				for(Entry<String, List<DmmRepositoryDTO>> assetEntry : dmmRepositoryListMap.entrySet()){
					List<DmmRepositoryDTO> DmmRepositoryResult = assetEntry.getValue();
					int rowCount = 1;
					if(DmmRepositoryResult.size() > 0) {
						criteria = new JasperReportCriteriaDTO();
						criteria.setAutoBuildJasper(false);
						for (DmmRepositoryDTO dmmRepositoryDTO : DmmRepositoryResult) {
							dmmRepositoryDTO.setRowId(String.valueOf(rowCount));
							rowCount ++;
						}
						criteria.setResult(DmmRepositoryResult);
						//設置所需報表的Name
						criteria.setJrxmlName(IAtomsConstants.GREEN_WORLD_ASSET_INFO);
						//設置報表路徑
						criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
						//設置匯出格式
						criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
						//設置報表Name
						criteria.setSheetName(assetEntry.getKey() + " 設備明細");
						criteria.setReportFileName(fileName);
						criterias.add(criteria);
					}
				}
				//第七個sheet 聊天設備
				if(dmmRepositoryDTOChatList.size() == 0) {
					DmmRepositoryDTO dmmRepositoryDTO = new DmmRepositoryDTO();
					dmmRepositoryDTOChatList.add(dmmRepositoryDTO);
				}
				criteria = new JasperReportCriteriaDTO();
				criteria.setAutoBuildJasper(false);
				criteria.setResult(dmmRepositoryDTOChatList);
				//設置所需報表的Name
				criteria.setJrxmlName(IAtomsConstants.GREEN_WORLD_CHAT_ASSET);
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				//設置報表Name
				criteria.setSheetName("聯天設備");
				criteria.setReportFileName(fileName);
				criterias.add(criteria);
				
				//第八個sheet *月作業明細
				if(SrmCaseHandleInfoDTOList.size() == 0) {
					SrmCaseHandleInfoDTO srmCaseHandleInfoDTO = new SrmCaseHandleInfoDTO();
					SrmCaseHandleInfoDTOList.add(srmCaseHandleInfoDTO);
				}
				criteria = new JasperReportCriteriaDTO();
				criteria.setAutoBuildJasper(false);
				criteria.setResult(SrmCaseHandleInfoDTOList);
				Map caseSumMap = new HashMap();
				caseSumMap.put("fastSum", fastSum);
				caseSumMap.put("installedPriceSum", installedPriceSum);
				caseSumMap.put("settingPriceSum", settingPriceSum);
				caseSumMap.put("unInstallPriceSum", unInstallPriceSum);
				caseSumMap.put("ecrlineSumInCase", ecrlineSumInCase);
				caseSumMap.put("networkRouteSumInCase", networkRouteSumInCase);
				criteria.setParameters(caseSumMap);
				//設置所需報表的Name
				criteria.setJrxmlName(IAtomsConstants.GREEN_WORLD_TX_MONTH_CASE_INFO_REPORT);
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				//設置報表Name
				//criteria.setSheetName(startDate.toString().substring(6, 7) + "月作業明細");
				if(startDate.toString().substring(5, 6).equals(IAtomsConstants.EDC_FEE_REPORT_PRICE_ZERO)) {
					criteria.setSheetName(startDate.toString().substring(6, 7) + "月作業明細");
				} else {
					criteria.setSheetName(startDate.toString().substring(5, 7) + "月作業明細");
				}
				criteria.setReportFileName(fileName);
				criterias.add(criteria);
				
				//第九個sheet 維護費設備
				if(DmmRepositoryDTOLists.size() == 0) {
					DmmRepositoryDTO dmmRepositoryDTO = new DmmRepositoryDTO();
					DmmRepositoryDTOLists.add(dmmRepositoryDTO);
				}
				criteria = new JasperReportCriteriaDTO();
				criteria.setAutoBuildJasper(false);
				criteria.setResult(DmmRepositoryDTOLists);
				//設置所需報表的Name
				criteria.setJrxmlName(IAtomsConstants.GREEN_WORLD_CHAT_ASSET);
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				//設置報表Name
				criteria.setSheetName("維護費設備");
				criteria.setReportFileName(fileName);
				criterias.add(criteria);
				//第十個sheet 維修耗材費用-AO已回覆扣款方式
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
				criteria.setSheetName("維修耗材費用-AO已回覆扣款方式");
				criteria.setReportFileName(fileName);
				criterias.add(criteria);
				
				//第十個sheet 維修耗材費用-AO未回覆扣款方式
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
				criteria.setSheetName("維修耗材費用-AO未回覆扣款方式");
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
	 * Purpose:獲取第二個總表的數據
	 * @author HermanWang
	 * @param taiXinWorkFeeSettingList：台新價錢配置list
	 * @param taiXinInstallCount：裝機數量
	 * @param taiXinInstallCaseInfo：首裝買斷機的信息
	 * @param taiXinUnInstallCount： 拆機買斷機 數量
	 * @param taiXinUnInstallFeeCount： 拆機無須收費
	 * @param taiXinUpdateOrMergeCount：並機或者異動  處理方式=到場處理 的數量
	 * @param taiXinUpdateSoftDispatch：並機或者異動 處理方式=軟排 的數量
	 * @param taiXinFast:急件數量
	 * @param taiXinSrmCaseAssetLinkDTOList:案件通過設備鏈接檔單價分組的信息
	 * @param taiXinCheckCount:查核數量
	 * @param taiXinCheckCaseInfo:查核
	 * @return List<WorkFeeSetting>返回的第二個總表的result
	 */
	private List<WorkFeeSetting> getTaiXinWorkFeeDTOList(List<WorkFeeSetting> taiXinWorkFeeSettingList, int taiXinInstallCount, 
			List<String> taiXinInstallCaseInfo, int taiXinUnInstallCount, int taiXinUnInstallFeeCount, 
			int taiXinUpdateOrMergeCount, int taiXinUpdateSoftDispatch, int taiXinFast, 
			List<SrmCaseAssetLinkDTO> taiXinSrmCaseAssetLinkDTOList, int taiXinCheckCount, List<String> taiXinCheckCaseInfo) {
		try {
			//第一行--裝(併、移)機
			WorkFeeSetting workFeeDTOInstalled = new WorkFeeSetting();
			WorkFeeSetting workFeeDTO = getworkFeeNameByTaiXinWorkCategory(IAtomsConstants.TICKET_TYPE_INSTALLED);
			workFeeDTOInstalled.setName(workFeeDTO.getName());
			if(!CollectionUtils.isEmpty(taiXinInstallCaseInfo)) {
				workFeeDTOInstalled.setFirstCount(String.valueOf(taiXinInstallCaseInfo.size()));
				workFeeDTOInstalled.setOtherCount(String.valueOf(taiXinInstallCount-taiXinInstallCaseInfo.size()));
			} else {
				workFeeDTOInstalled.setFirstCount(String.valueOf(0));
				workFeeDTOInstalled.setOtherCount(String.valueOf(0));
			}
			workFeeDTOInstalled.setFirstPrice(workFeeDTO.getFirstPrice());
			workFeeDTOInstalled.setOtherPrice(workFeeDTO.getOtherPrice());
			//裝機合計
			workFeeDTOInstalled.setSum(Integer.parseInt(workFeeDTOInstalled.getFirstCount())*workFeeDTO.getFirstPrice()
					+ Integer.parseInt(workFeeDTOInstalled.getOtherCount()) * workFeeDTO.getOtherPrice());
			taiXinWorkFeeSettingList.add(workFeeDTOInstalled);
			//第二行 二次作業(第一次到場無法作業 需二次到場)
			WorkFeeSetting workFeeDTOSecond = new WorkFeeSetting();
			workFeeDTO = getworkFeeNameByTaiXinWorkCategory("SECOND");
			workFeeDTOSecond.setName(workFeeDTO.getName());
			workFeeDTOSecond.setFirstPrice(workFeeDTO.getFirstPrice());
			workFeeDTOSecond.setOtherPrice(workFeeDTO.getOtherPrice());
			taiXinWorkFeeSettingList.add(workFeeDTOSecond);
			//第三行--拆、撤機
			WorkFeeSetting workFeeDTOUninstalled = new WorkFeeSetting();
			workFeeDTO = getworkFeeNameByTaiXinWorkCategory("UNINSTALLED");
			workFeeDTOUninstalled.setName(workFeeDTO.getName());
			workFeeDTOUninstalled.setFirstPrice(workFeeDTO.getFirstPrice());
			//台數：拆機件 - 首拆買斷機 - 無需收費
			workFeeDTOUninstalled.setFirstCount(String.valueOf(taiXinUnInstallCount - taiXinUnInstallFeeCount));
			workFeeDTOUninstalled.setSum((Integer.parseInt(workFeeDTOUninstalled.getFirstCount()) * workFeeDTO.getFirstPrice()));
			taiXinWorkFeeSettingList.add(workFeeDTOUninstalled);
			//第四行--參數設定(到場)
			WorkFeeSetting workFeeDTOArrived = new WorkFeeSetting();
			workFeeDTO = getworkFeeNameByTaiXinWorkCategory("ARRIVED");
			workFeeDTOArrived.setName(workFeeDTO.getName());
			workFeeDTOArrived.setFirstPrice(workFeeDTO.getFirstPrice());
			workFeeDTOArrived.setFirstCount(String.valueOf(taiXinUpdateOrMergeCount));
			workFeeDTOArrived.setSum((Integer.parseInt(workFeeDTOArrived.getFirstCount()) * workFeeDTO.getFirstPrice()));
			taiXinWorkFeeSettingList.add(workFeeDTOArrived);
			//參數設定(遠端軟派 不到場)—只統計總數，以總數計算。
			WorkFeeSetting workFeeDTORemote = new WorkFeeSetting();
			workFeeDTO = getworkFeeNameByTaiXinWorkCategory("REMOTE");
			workFeeDTORemote.setName(workFeeDTO.getName());
			workFeeDTORemote.setFirstPrice(workFeeDTO.getFirstPrice());
			workFeeDTORemote.setFirstCount(String.valueOf(taiXinUpdateSoftDispatch));
			workFeeDTORemote.setSum((Integer.parseInt(workFeeDTORemote.getFirstCount()) * workFeeDTO.getFirstPrice()));
			taiXinWorkFeeSettingList.add(workFeeDTORemote);
			//第八行---急件加收(每月5台免費、第6台(含)以上)
			WorkFeeSetting workFeeDTOFast = new WorkFeeSetting();
			workFeeDTO = getworkFeeNameByTaiXinWorkCategory("FAST");
			workFeeDTOFast.setName(workFeeDTO.getName());
			//大於5台 減去5
			if(taiXinFast > 5) {
				workFeeDTOFast.setFirstCount(String.valueOf(taiXinFast - 5));
			} else {
				workFeeDTOFast.setFirstCount(String.valueOf(0));
			}
			workFeeDTOFast.setFirstPrice(workFeeDTO.getFirstPrice());
			workFeeDTOFast.setSum(Integer.parseInt(workFeeDTOFast.getFirstCount()) * workFeeDTO.getFirstPrice());
			workFeeDTOFast.setDescription(" 本月共" + taiXinFast + "台急件 ");
			taiXinWorkFeeSettingList.add(workFeeDTOFast);
			//第九行--線材使用-ECR線材
			//第十行--線材使用-網路線
			//使用之案件單價不同，拆多筆，列出不同台數、單價
			boolean haveEcrLine = false;
			boolean haveNetLine = false;
			for (SrmCaseAssetLinkDTO srmCaseAssetLinkDTO : taiXinSrmCaseAssetLinkDTOList) {
				if(srmCaseAssetLinkDTO.getNumber() == null) srmCaseAssetLinkDTO.setNumber(0);
				if(srmCaseAssetLinkDTO.getPrice() == null) srmCaseAssetLinkDTO.setPrice(new BigDecimal(0));
				WorkFeeSetting workFeeDTOSupplies = new WorkFeeSetting();
				if("ECRLINE".equals(srmCaseAssetLinkDTO.getItemCategory())) {
					haveEcrLine = true;
					workFeeDTOSupplies.setWorkCategory("線材使用-ECR線材");
					workFeeDTOSupplies.setName("線材使用-ECR線材");
					workFeeDTOSupplies.setFirstCount(String.valueOf(srmCaseAssetLinkDTO.getNumber()));
					workFeeDTOSupplies.setFirstPrice(srmCaseAssetLinkDTO.getPrice().intValue());
					workFeeDTOSupplies.setSum((Integer.parseInt(workFeeDTOSupplies.getFirstCount()) * srmCaseAssetLinkDTO.getPrice().intValue()));
					taiXinWorkFeeSettingList.add(workFeeDTOSupplies);
				} else {
					haveNetLine = true;
					workFeeDTOSupplies.setWorkCategory("線材使用-網路線");
					workFeeDTOSupplies.setName("線材使用-網路線");
					workFeeDTOSupplies.setFirstCount(String.valueOf(srmCaseAssetLinkDTO.getNumber()));
					workFeeDTOSupplies.setFirstPrice(srmCaseAssetLinkDTO.getPrice().intValue());
					workFeeDTOSupplies.setSum((Integer.parseInt(workFeeDTOSupplies.getFirstCount()) * srmCaseAssetLinkDTO.getPrice().intValue()));
					taiXinWorkFeeSettingList.add(workFeeDTOSupplies);
				}
			}
			//如果沒有ECR那麼也要展示空行
			if(!haveEcrLine) {
				WorkFeeSetting workFeeDTOSupplies = new WorkFeeSetting();
				workFeeDTOSupplies.setWorkCategory("線材使用-ECR線材");
				workFeeDTOSupplies.setName("線材使用-ECR線材");
				workFeeDTOSupplies.setFirstCount(String.valueOf(0));
				workFeeDTOSupplies.setFirstPrice(0);
				workFeeDTOSupplies.setSum(0);
				taiXinWorkFeeSettingList.add(workFeeDTOSupplies);
			}
			//如果沒有網路線那麼也要展示空行
			if(!haveNetLine) {
				WorkFeeSetting workFeeDTOSupplies = new WorkFeeSetting();
				workFeeDTOSupplies.setWorkCategory("線材使用-網路線");
				workFeeDTOSupplies.setName("線材使用-網路線");
				workFeeDTOSupplies.setFirstCount(String.valueOf(0));
				workFeeDTOSupplies.setFirstPrice(0);
				workFeeDTOSupplies.setSum(0);
				taiXinWorkFeeSettingList.add(workFeeDTOSupplies);
			}
			//第十一行--維修耗材費用
			WorkFeeSetting workFeeDTOSuppliesPrice = new WorkFeeSetting();
			workFeeDTOSuppliesPrice.setName("維修耗材費用");
			taiXinWorkFeeSettingList.add(workFeeDTOSuppliesPrice);
			//第十二行---查核
			WorkFeeSetting workFeeDTOCheck = new WorkFeeSetting();
			workFeeDTO = getworkFeeNameByTaiXinWorkCategory("CHECK");
			workFeeDTOCheck.setName(workFeeDTO.getName());
			workFeeDTOCheck.setFirstPrice(workFeeDTO.getFirstPrice());
			workFeeDTOCheck.setOtherPrice(workFeeDTO.getOtherPrice());
			if(!CollectionUtils.isEmpty(taiXinCheckCaseInfo)) {
				workFeeDTOCheck.setFirstCount(String.valueOf(taiXinCheckCaseInfo.size()));
				//(作業明細)案件中【 案件類別=查核】並依完修日排序 - 前 5 台後，
				//再依【設備、MID、完修日、聯繫地址】統計數量，第一台原價，剩餘特價(第二台)
				//checkCount - 5 是 總共收費的台數    
				//checkCount - 5 - checkCaseInfo.size()是 台數減去第一台 即為第二胎
				if(taiXinCheckCount - 5 - taiXinCheckCaseInfo.size() > 0) {
					workFeeDTOCheck.setOtherCount(String.valueOf(taiXinCheckCount - 5 - taiXinCheckCaseInfo.size()));
				} else {
					workFeeDTOCheck.setOtherCount(String.valueOf(0));
				}
			} else {
				workFeeDTOCheck.setFirstCount(String.valueOf(0));
				workFeeDTOCheck.setOtherCount(String.valueOf(0));
			}
			//合計
			workFeeDTOCheck.setSum(Integer.parseInt(workFeeDTOCheck.getFirstCount()) * workFeeDTO.getFirstPrice() + 
					Integer.parseInt(workFeeDTOCheck.getOtherCount()) * workFeeDTO.getOtherPrice());
			taiXinWorkFeeSettingList.add(workFeeDTOCheck);
		} catch (Exception e) {
			LOGGER.error("toReport() ", "DataAccess Exception:", e);
		}
		return taiXinWorkFeeSettingList;
	}
	/**
	 * Purpose:
	 * @author HermanWang
	 * @param workFeeSettingList:返回的第一個總表的result
	 * @param installCount：裝機數量
	 * @param installCaseInfo：首裝買斷機的信息
	 * @param isBuyoutFirstInstall： 首裝買斷機 數量
	 * @param startDate： 帳務月
	 * @param unInstallCount：拆機數量
	 * @param firstUnInstallCount：首拆買斷機數量
	 * @param unInstallFeeCount：拆機無須收費
	 * @param updateOrMergeCount：並機或者異動  處理方式=到場處理 的數量
	 * @param updateSoftDispatch：並機或者異動 處理方式=軟排 的數量
	 * @param dmmRepositoryListMap：維護費報表map key是設備名稱  Value是 該設備名稱分組之後的 設備的dtolist
	 * @param inUseCount：使用量
	 * @param readyCount:備品量
	 * @param OrderCount:訂單數兩
	 * @param ScrapCount:報廢兩
	 * @param fast:急件數量
	 * @param extra:特急件數量
	 * @param srmCaseAssetLinkDTOList:案件通過設備鏈接檔單價分組的信息
	 * @param checkCaseInfo:查核
	 * @param checkCount:查核數量
	 * @return List<WorkFeeSetting>返回的第一個總表的result
	 */
	private List<WorkFeeSetting> getworkFeeDTOList(List<WorkFeeSetting> workFeeSettingList, int installCount,
			List<String> installCaseInfo, int isBuyoutFirstInstall, Timestamp startDate, int unInstallCount, 
			int firstUnInstallCount, int unInstallFeeCount, int updateOrMergeCount, int updateSoftDispatch,
			Map<String, List<DmmRepositoryDTO>> dmmRepositoryListMap, int inUseCount, Double readyCount,
			int OrderCount, int ScrapCount, int fast, int extra, List<SrmCaseAssetLinkDTO> srmCaseAssetLinkDTOList, 
			List<String> checkCaseInfo, int checkCount){
		try {
			//第一行裝機 
			WorkFeeSetting workFeeDTOInstalled = new WorkFeeSetting();
			WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory(IAtomsConstants.TICKET_TYPE_INSTALLED);
			workFeeDTOInstalled.setName(workFeeDTO.getName());
			if(!CollectionUtils.isEmpty(installCaseInfo)) {
				workFeeDTOInstalled.setFirstCount(String.valueOf(installCaseInfo.size()));
				workFeeDTOInstalled.setOtherCount(String.valueOf(installCount - isBuyoutFirstInstall-installCaseInfo.size()));
			} else {
				workFeeDTOInstalled.setFirstCount(String.valueOf(0));
				workFeeDTOInstalled.setOtherCount(String.valueOf(0));
			}
			workFeeDTOInstalled.setFirstPrice(workFeeDTO.getFirstPrice());
			workFeeDTOInstalled.setOtherPrice(workFeeDTO.getOtherPrice());
			//裝機合計
			workFeeDTOInstalled.setSum(Integer.parseInt(workFeeDTOInstalled.getFirstCount())*workFeeDTO.getFirstPrice()
					+ Integer.parseInt(workFeeDTOInstalled.getOtherCount()) * workFeeDTO.getOtherPrice());
			workFeeDTOInstalled.setDescription(DateTimeUtils.getCalendar(startDate, Calendar.MONTH) + "月份共" + installCount +
					"台裝機，(" + workFeeDTOInstalled.getOtherCount() + "台為同一地安裝),其中" + isBuyoutFirstInstall + "台為首裝買斷設備，請參閱作業明細頁籤，故免收裝機費用");
			workFeeSettingList.add(workFeeDTOInstalled);
			//第二行 二次作業(第一次到場無法作業 需二次到場)
			WorkFeeSetting workFeeDTOSecond = new WorkFeeSetting();
			workFeeDTO = getworkFeeNameByWorkCategory("SECOND");
			workFeeDTOSecond.setName(workFeeDTO.getName());
			workFeeDTOSecond.setFirstPrice(workFeeDTO.getFirstPrice());
			workFeeDTOSecond.setOtherPrice(workFeeDTO.getOtherPrice());
			workFeeSettingList.add(workFeeDTOSecond);
			//第三行 拆、撤機 (內含買斷機免費首次拆機)
			WorkFeeSetting workFeeDTOUninstalled = new WorkFeeSetting();
			workFeeDTO = getworkFeeNameByWorkCategory("UNINSTALLED");
			workFeeDTOUninstalled.setFirstPrice(workFeeDTO.getFirstPrice());
			workFeeDTOUninstalled.setName(workFeeDTO.getName());
			//台數：拆機件 - 首拆買斷機 - 無需收費
			workFeeDTOUninstalled.setFirstCount(String.valueOf(unInstallCount - firstUnInstallCount - unInstallFeeCount));
			workFeeDTOUninstalled.setSum((Integer.parseInt(workFeeDTOUninstalled.getFirstCount()) * workFeeDTO.getFirstPrice()));
			workFeeDTOUninstalled.setDescription(DateTimeUtils.getCalendar(startDate, Calendar.MONTH) + "月份共" + unInstallCount + "台拆機，" + 
			"其中" + firstUnInstallCount + "台為首拆買斷設備和" + unInstallFeeCount + "台為自拆寄回，請參閱作業明細頁籤，故免收拆機費用");
			workFeeSettingList.add(workFeeDTOUninstalled);
			//第四行到場
			WorkFeeSetting workFeeDTOArrived = new WorkFeeSetting();
			workFeeDTO = getworkFeeNameByWorkCategory("ARRIVED");
			workFeeDTOArrived.setName(workFeeDTO.getName());
			workFeeDTOArrived.setFirstPrice(workFeeDTO.getFirstPrice());
			workFeeDTOArrived.setFirstCount(String.valueOf(updateOrMergeCount));
			workFeeDTOArrived.setSum((Integer.parseInt(workFeeDTOArrived.getFirstCount()) * workFeeDTO.getFirstPrice()));
			workFeeSettingList.add(workFeeDTOArrived);
			//第5行軟排
			WorkFeeSetting workFeeDTORemote = new WorkFeeSetting();
			workFeeDTO = getworkFeeNameByWorkCategory("REMOTE");
			workFeeDTORemote.setName(workFeeDTO.getName());
			workFeeDTORemote.setFirstPrice(workFeeDTO.getFirstPrice());
			workFeeDTORemote.setFirstCount(String.valueOf(updateSoftDispatch));
			workFeeDTORemote.setSum((Integer.parseInt(workFeeDTORemote.getFirstCount()) * workFeeDTO.getFirstPrice()));
			workFeeSettingList.add(workFeeDTORemote);
			//第六行--EDC維護費用(第2年始收費)
			//以設備名稱分組計算維護費，並按格式顯示設備的維護費用。每種設備顯示一行
			workFeeDTO = getworkFeeNameByWorkCategory("MAINTENANCE_FEE");
			WorkFeeSetting workFeeDTOMaintenanceFee = new WorkFeeSetting();
			workFeeDTOMaintenanceFee.setName(workFeeDTO.getName());
			workFeeDTOMaintenanceFee.setFirstPrice(workFeeDTO.getFirstPrice());
			int count = 0;
			String description = IAtomsConstants.MARK_EMPTY_STRING;
			for(Entry<String, List<DmmRepositoryDTO>> assetEntry : dmmRepositoryListMap.entrySet()){
				List<DmmRepositoryDTO> DmmRepositoryResult = assetEntry.getValue();
				if(!CollectionUtils.isEmpty(DmmRepositoryResult)) {
					count = count + DmmRepositoryResult.size();
					description = description + assetEntry.getKey() + "維護費用共計$"+ (workFeeDTO.getFirstPrice() * DmmRepositoryResult.size()) +"，請參閱" + assetEntry.getKey() + "設備明細頁籤" + IAtomsConstants.RETURN_LINE_FEED;
				}
				
			}
			workFeeDTOMaintenanceFee.setDescription(description);
			//第一台數量 等於(維護費)設備數量
			workFeeDTOMaintenanceFee.setFirstCount(String.valueOf(count));
			workFeeDTOMaintenanceFee.setSum((count * workFeeDTO.getFirstPrice()));
			workFeeSettingList.add(workFeeDTOMaintenanceFee);
			//第七行--聯天EDC維護費用(106/03/15始收費)
			WorkFeeSetting workFeeDTOChatDeviceFee = new WorkFeeSetting();
			workFeeDTO = getworkFeeNameByWorkCategory("CHAT_DEVICE_FEE");
			workFeeDTOChatDeviceFee.setName(workFeeDTO.getName());
			workFeeDTOChatDeviceFee.setFirstPrice(workFeeDTO.getFirstPrice());
			//（使用量+備品量）和（訂單數量-報廢量），計算比較後，取小的那個
			//使用量+備品量 >訂單數量-報廢量 
			if((inUseCount+readyCount) > (OrderCount-ScrapCount)) {
				workFeeDTOChatDeviceFee.setFirstCount(String.valueOf(OrderCount-ScrapCount));
				workFeeDTOChatDeviceFee.setSum((Integer.parseInt(workFeeDTOChatDeviceFee.getFirstCount()) * workFeeDTO.getFirstPrice()));
				workFeeDTOChatDeviceFee.setDescription("維護費用共計$" + workFeeDTOChatDeviceFee.getSum() + "，請參閱聯天設備頁籤" + IAtomsConstants.RETURN_LINE_FEED + 
					"使用量+備品量(即”(訂單數量-報廢量)*5%”)超過訂單數量-報廢量，故月維護費計算方式以訂單數量-報廢量: (" + OrderCount + "-" + ScrapCount + ") * " + workFeeDTOChatDeviceFee.getFirstPrice()+ "=" +  workFeeDTOChatDeviceFee.getSum());
			} else {
				BigDecimal readyCountBig = new BigDecimal(String.valueOf(readyCount));
		        double readyCountDou = readyCountBig.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
				workFeeDTOChatDeviceFee.setFirstCount(String.valueOf((int)(inUseCount + readyCountDou)));
				Double firstCount = Double.parseDouble(workFeeDTOChatDeviceFee.getFirstCount());
				workFeeDTOChatDeviceFee.setSum((firstCount.intValue() * workFeeDTO.getFirstPrice()));
				workFeeDTOChatDeviceFee.setDescription("維護費用共計$" + workFeeDTOChatDeviceFee.getSum() + "，請參閱聯天設備頁籤" + IAtomsConstants.RETURN_LINE_FEED + 
					"訂單數量-報廢量 超過 使用量+備品量(即”(訂單數量-報廢量)*5%”)，故月維護費計算方式以使用量+備品量(即”(訂單數量-報廢量)*5%”): (" + inUseCount + "+" + (int)readyCountDou + ") * " + workFeeDTOChatDeviceFee.getFirstPrice()+ "=" +  workFeeDTOChatDeviceFee.getSum());
			}
			workFeeSettingList.add(workFeeDTOChatDeviceFee);
			//第八行---急件加收(每月5台免費、第6台(含)以上)
			WorkFeeSetting workFeeDTOFast = new WorkFeeSetting();
			workFeeDTO = getworkFeeNameByWorkCategory("FAST");
			workFeeDTOFast.setName(workFeeDTO.getName());
			//大於5台 減去5
			if(fast > 5) {
				workFeeDTOFast.setFirstCount(String.valueOf(fast - 5));
			} else {
				workFeeDTOFast.setFirstCount(String.valueOf(0));
			}
			workFeeDTOFast.setFirstPrice(workFeeDTO.getFirstPrice());
			workFeeDTOFast.setSum(Integer.parseInt(workFeeDTOFast.getFirstCount()) * workFeeDTO.getFirstPrice());
			workFeeDTOFast.setDescription(" 本月共" + fast + "台急件 ");
			workFeeSettingList.add(workFeeDTOFast);
			//第九行--特急件加收(每月5台免費、第6台(含)以上)
			WorkFeeSetting workFeeDTOExtra = new WorkFeeSetting();
			workFeeDTO = getworkFeeNameByWorkCategory("EXTRA");
			workFeeDTOExtra.setName(workFeeDTO.getName());
			//大於5台 減去5
			if(extra > 5) {
				workFeeDTOExtra.setFirstCount(String.valueOf(extra - 5));
			} else {
				workFeeDTOExtra.setFirstCount(String.valueOf(0));
			}
			workFeeDTOExtra.setFirstPrice(workFeeDTO.getFirstPrice());
			workFeeDTOExtra.setSum(Integer.parseInt(workFeeDTOExtra.getFirstCount()) * workFeeDTO.getFirstPrice());
			workFeeDTOExtra.setDescription(" 本月共" + extra + "台特急件 ");
			workFeeSettingList.add(workFeeDTOExtra);
			//第十行--線材使用-ECR線材
			//使用之案件單價不同，拆多筆，列出不同台數、單價
			//第十一行--線材使用-網路線
			//使用之案件單價不同，拆多筆，列出不同台數、單價
			boolean haveEcrLine = false;
			boolean haveNetLine = false;
			for (SrmCaseAssetLinkDTO srmCaseAssetLinkDTO : srmCaseAssetLinkDTOList) {
				WorkFeeSetting workFeeDTOSupplies = new WorkFeeSetting();
				if(srmCaseAssetLinkDTO.getNumber() == null) srmCaseAssetLinkDTO.setNumber(0);
				if(srmCaseAssetLinkDTO.getPrice() == null) srmCaseAssetLinkDTO.setPrice(new BigDecimal(0));
				if("ECRLINE".equals(srmCaseAssetLinkDTO.getItemCategory())) {
					haveEcrLine = true;
					workFeeDTOSupplies.setWorkCategory("線材使用-ECR線材");
					workFeeDTOSupplies.setName("線材使用-ECR線材");
					workFeeDTOSupplies.setFirstCount(String.valueOf(srmCaseAssetLinkDTO.getNumber()));
					workFeeDTOSupplies.setFirstPrice(srmCaseAssetLinkDTO.getPrice().intValue());
					workFeeDTOSupplies.setSum((Integer.parseInt(workFeeDTOSupplies.getFirstCount()) * srmCaseAssetLinkDTO.getPrice().intValue()));
					workFeeSettingList.add(workFeeDTOSupplies);
				} else {
					haveNetLine = true;
					workFeeDTOSupplies.setWorkCategory("線材使用-網路線");
					workFeeDTOSupplies.setName("線材使用-網路線");
					workFeeDTOSupplies.setFirstCount(String.valueOf(srmCaseAssetLinkDTO.getNumber()));
					workFeeDTOSupplies.setFirstPrice(srmCaseAssetLinkDTO.getPrice().intValue());
					workFeeDTOSupplies.setSum((Integer.parseInt(workFeeDTOSupplies.getFirstCount()) * srmCaseAssetLinkDTO.getPrice().intValue()));
					workFeeSettingList.add(workFeeDTOSupplies);
				}
			}
			//如果沒有ECR那麼也要展示空行
			if(!haveEcrLine) {
				WorkFeeSetting workFeeDTOSupplies = new WorkFeeSetting();
				workFeeDTOSupplies.setWorkCategory("線材使用-ECR線材");
				workFeeDTOSupplies.setName("線材使用-ECR線材");
				workFeeDTOSupplies.setFirstCount(String.valueOf(0));
				workFeeDTOSupplies.setFirstPrice(0);
				workFeeDTOSupplies.setSum(0);
				workFeeSettingList.add(workFeeDTOSupplies);
			}
			//如果沒有網路線那麼也要展示空行
			if(!haveNetLine) {
				WorkFeeSetting workFeeDTOSupplies = new WorkFeeSetting();
				workFeeDTOSupplies.setWorkCategory("線材使用-網路線");
				workFeeDTOSupplies.setName("線材使用-網路線");
				workFeeDTOSupplies.setFirstCount(String.valueOf(0));
				workFeeDTOSupplies.setFirstPrice(0);
				workFeeDTOSupplies.setSum(0);
				workFeeSettingList.add(workFeeDTOSupplies);
			}
			WorkFeeSetting workFeeDTORepairSupplies = new WorkFeeSetting();
			workFeeDTORepairSupplies.setName("維修耗材費用");
			workFeeSettingList.add(workFeeDTORepairSupplies);
			//第十二行--Internet專線月租費
			WorkFeeSetting workFeeDTOInternetFee = new WorkFeeSetting();
			workFeeDTO = getworkFeeNameByWorkCategory("INTERNET_FEE");
			workFeeDTOInternetFee.setName(workFeeDTO.getName());
			workFeeDTOInternetFee.setFirstPrice(workFeeDTO.getFirstPrice());
			workFeeDTOInternetFee.setSum(workFeeDTO.getFirstPrice());
			workFeeDTOInternetFee.setDescription("每月固定收費：執行TMS程式參數下載及連結VPN(No.2765)執行刷卡交易，故須設定此存取控制。");
			workFeeSettingList.add(workFeeDTOInternetFee);
			//第十三行--查核
			WorkFeeSetting workFeeDTOCheck = new WorkFeeSetting();
			workFeeDTO = getworkFeeNameByWorkCategory("CHECK");
			workFeeDTOCheck.setName(workFeeDTO.getName());
			workFeeDTOCheck.setFirstPrice(workFeeDTO.getFirstPrice());
			workFeeDTOCheck.setOtherPrice(workFeeDTO.getOtherPrice());
			if(!CollectionUtils.isEmpty(checkCaseInfo)) {
				workFeeDTOCheck.setFirstCount(String.valueOf(checkCaseInfo.size()));
				//(作業明細)案件中【 案件類別=查核】並依完修日排序 - 前 5 台後，
				//再依【設備、MID、完修日、聯繫地址】統計數量，第一台原價，剩餘特價(第二台)
				//checkCount - 5 是 總共收費的台數    
				//checkCount - 5 - checkCaseInfo.size()是 台數減去第一台 即為第二胎
				if(checkCount - 5 - checkCaseInfo.size() > 0) {
					workFeeDTOCheck.setOtherCount(String.valueOf(checkCount - 5 - checkCaseInfo.size()));
				} else {
					workFeeDTOCheck.setOtherCount(String.valueOf(0));
				}
			} else {
				workFeeDTOCheck.setFirstCount(String.valueOf(0));
				workFeeDTOCheck.setOtherCount(String.valueOf(0));
			}
			//合計
			workFeeDTOCheck.setSum(Integer.parseInt(workFeeDTOCheck.getFirstCount()) * workFeeDTO.getFirstPrice() + 
					Integer.parseInt(workFeeDTOCheck.getOtherCount()) * workFeeDTO.getOtherPrice());
			workFeeSettingList.add(workFeeDTOCheck);
			//第十四行-- dongle、pinpad現場維護費
			WorkFeeSetting workFeeDTODonglePinpad = new WorkFeeSetting();
			workFeeDTO = getworkFeeNameByWorkCategory("DONGLEPINPAD");
			workFeeDTODonglePinpad.setName(workFeeDTO.getName());
			workFeeDTODonglePinpad.setFirstPrice(workFeeDTO.getFirstPrice());
			workFeeDTODonglePinpad.setOtherPrice(workFeeDTO.getOtherPrice());
			workFeeSettingList.add(workFeeDTODonglePinpad);
		} catch (Exception e) {
			LOGGER.error("toReport() ", "DataAccess Exception:", e);
		}
		return workFeeSettingList;
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
	 * Purpose:根據類別獲取name
	 * @author HermanWang
	 * @param workCategory：類別
	 * @return String
	 */
	private WorkFeeSetting getworkFeeNameByTaiXinWorkCategory(String workCategory){
		WorkFeeSetting workFeeSettingDTO = new WorkFeeSetting();
		try {
			for (WorkFeeSetting WorkFeeSettingDTO : TaiXinworkFeeSetting) {
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
	 * @return the taiXinworkFeeSetting
	 */
	public List<WorkFeeSetting> getTaiXinworkFeeSetting() {
		return TaiXinworkFeeSetting;
	}
	/**
	 * @param taiXinworkFeeSetting the taiXinworkFeeSetting to set
	 */
	public void setTaiXinworkFeeSetting(List<WorkFeeSetting> taiXinworkFeeSetting) {
		TaiXinworkFeeSetting = taiXinworkFeeSetting;
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
	/**
	 * @return the repoStatusList
	 */
	public List<String> getRepoStatusList() {
		return repoStatusList;
	}
	/**
	 * @param repoStatusList the repoStatusList to set
	 */
	public void setRepoStatusList(List<String> repoStatusList) {
		this.repoStatusList = repoStatusList;
	}
	
}
