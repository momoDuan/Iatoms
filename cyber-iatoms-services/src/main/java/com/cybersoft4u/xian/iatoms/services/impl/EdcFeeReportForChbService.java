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
import java.util.TreeMap;

import javassist.expr.NewArray;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.Parameter;
import cafe.core.config.SystemConfigManager;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
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
import com.cybersoft4u.xian.iatoms.services.dao.IContractDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryMonthlyDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IReportSettingDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentInfoDAO;
/**
 * Purpose: 張銀格式的service
 * @author Hermanwang
 * @since  JDK 1.6
 * @date   2017年10月20日
 * @MaintenancePersonnel Hermanwang
 */
public class EdcFeeReportForChbService extends AtomicService implements IReportService {
	/**
	 * 記錄日誌
	 */
	private static final CafeLog LOGGER	= CafeLogFactory.getLog(EdcFeeReportForChbService.class);
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
	 * 通訊模式
	 */
	private List<String> commModeIdList;
	/**
	 * 案件狀態列表
	 */
	private List<String> caseStatusList;
	/**
	 * 設備狀態 報廢、銷毀 列表
	 */
	private List<String> repoStatusList;
	/**
	 * 案件類型 裝機 異動
	 */
	private List<String> caseCategoryList;
	/**
	 * 求償狀態
	 */
	private List<String> paymentStatusList;
	/**
	 * 求償類型
	 */
	private List<String> paymentTypeList;
	/**
	 * key為合約編號value為客戶自己的編號
	 */
	private Map<String, String> contractCode;
	/**
	 * 外層key為合約編號  內層key為設備名稱 value為價錢
	 */
	private Map<String, Map<String, Double>> contractWorkFeeSetting;
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
	 * 合約dao
	 */
	private IContractDAO contractDAO;
	/**
	 * Constructor: 無參構造
	 */
	public EdcFeeReportForChbService() {
		super();
	}
	/**
	 * 
	 * Purpose:維護費報表(張銀格式)的配製方法
	 * @author HermanWang
	 * @throws ServiceException
	 * @return void
	 */
	public void toReport() throws ServiceException {
		CompanyDTO companyDTO = this.companyDAO.getCompanyByCompanyCode(IAtomsConstants.PARAMTER_COMPANY_CODE_CHB);
		if (companyDTO != null) {
			//code交換 改為20 2017/11/17
			sendReportMail(DateTimeUtils.getCurrentDate(), companyDTO.getCompanyId(), IAtomsConstants.REPORT_TCB_CHB_TWENTY);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IReportService#sendReportMail(java.util.Date, java.lang.String, java.lang.String)
	 */
	public void sendReportMail(Date sendDate, String customerId, String reportCode) throws ServiceException {
		StringBuffer pathBuffer = null;
		try {
			//Bug #2867 用該公司去查詢合約 與配置文件中合約相匹配 2017/11/21
			Map<String, String> contractCodeByCompany = new HashMap<String, String>();
			Map<String, Map<String,Double>> contractWorkFeeSettingByCompany = new HashMap<String, Map<String,Double>>();
			List<Parameter> contractList = this.contractDAO.getContractByCustomer(customerId, IAtomsConstants.PARAM_CONTRACT_STATUS_IN_EFFECT);
			for (Parameter parameter : contractList) {
				if (this.contractCode.containsKey(parameter.getName())) {
					contractCodeByCompany.put(parameter.getName(), this.contractCode.get(parameter.getName()));
				}
				if (this.contractWorkFeeSetting.containsKey(parameter.getName())) {
					contractWorkFeeSettingByCompany.put(parameter.getName(), this.contractWorkFeeSetting.get(parameter.getName()));
				}
				
			}

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
			//帳務月的上一月
			Calendar calLast=Calendar.getInstance();
			calLast.setTime(sendDate);
			calLast.add(Calendar.MONTH, -2); 
			calLast.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为帳務月上一月第一天 
			Timestamp lastDate = DateTimeUtils.toTimestamp(DateTimeUtils.toString(calLast.getTime(), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH));
			
			List<ReportSettingDTO> reportSettingDTOs = this.reportSettingDAO.getDetailList(customerId, reportCode);
			if (!CollectionUtils.isEmpty(reportSettingDTOs)) {
				JasperReportCriteriaDTO criteria = null;
				//郵件主題模板
				String subjectTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.EDC_FEE_REPORT_FOR_SCSB_SUBJECT_TEMPLATE;
				//郵件內容模板
				String textTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.EDC_FEE_REPORT_FOR_SCSB_TEXT_TEMPLATE;
				//獲取臨時保存路徑-時間區
				String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD);
				//獲取臨時路徑
				String path = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH);
				List<JasperReportCriteriaDTO> criterias = new ArrayList<JasperReportCriteriaDTO>();
				pathBuffer = new StringBuffer();
				String[] attachments = null;
				pathBuffer.append(path).append(File.separator).append(yearMonthDay).append(File.separator).append(IAtomsConstants.UC_NO_AMM_06080);
				pathBuffer.append(File.separator);
				//陽信	設備(第一個sql)
				List<DmmRepositoryDTO> DmmRepositoryDTOList =  this.dmmRepositoryHistoryMonthlyDAO.assetInfoListChb(DateTimeUtils.toString(startDate, DateTimeUtils.DT_FMT_YYYYMM), customerId, startDate, endDate);
				//案件信息 作業明細(第2個sql)
				List<SrmCaseHandleInfoDTO> SrmCaseHandleInfoDTOList =  this.srmCaseHandleInfoDAO.getCaseListInChb(commonTransactionTypeList, caseStatusList, customerId, startDate, endDate);
				//案件通過設備鏈接檔單價分組的信息(第3個sql)
				List<SrmCaseAssetLinkDTO> srmCaseAssetLinkDTOList = this.srmCaseAssetLinkDAO.getCaseAssetLinkInChb(caseStatusList, customerId, startDate, endDate);
				//(4)ecrLIne LIST
				List<SrmCaseAssetLinkDTO> srmCaseLinkList = this.srmCaseAssetLinkDAO.getCaseLinkSuppliesInChb(caseStatusList, customerId, startDate, endDate);
 				//(維護費)設備(第5個sql)
				List<DmmRepositoryDTO> DmmRepositoryDTOLists =  this.dmmRepositoryHistoryMonthlyDAO.feeAssetListChb(repoStatusList, DateTimeUtils.toString(startDate, DateTimeUtils.DT_FMT_YYYYMM), customerId, endDate);
				//求償維修耗材費用-AO已回覆扣款方式
				List<SrmPaymentInfoDTO> srmPaymentInfoDTOList = this.paymentInfoDAO.paymentInfoListToSyb(paymentTypeList, customerId);
				//維修耗材費用-AO未回覆扣款方式
				List<SrmPaymentInfoDTO> paymentInfoDTOListNoTax = this.paymentInfoDAO.paymentInfoListToSybNoTax(paymentStatusList, customerId);
				//設備合約的map 外層map key是合約編號 中層 key是設備名稱，內層 key是設備狀態，一共兩種 使用中和非使用者 value是 該設備的台數.
				Map<String, Map<String, Map<String, Double>>> assetContractMap = new HashMap<String, Map<String, Map<String, Double>>>();
				Map<String, Map<String, Map<String, Double>>> assetContractInUseMap = new HashMap<String, Map<String, Map<String, Double>>>();
				Map<String, Map<String, Map<String, Double>>> assetContractNoFeeMap = new HashMap<String, Map<String, Map<String, Double>>>();
				// 維護費報表map key是設備名稱  Value是 該設備名稱分組之後的 設備的dtolist
				Map<String, Map<String, List<DmmRepositoryDTO>>> dmmRepositoryListMap = new TreeMap<String, Map<String, List<DmmRepositoryDTO>>>();
				Map<String, Double> assetMap = null;
				Map<String, Double> assetInUseMap = null;
				Map<String, Double> assetNoFeeMap = null;
				for(Entry<String, String> contractCodeEntry : contractCodeByCompany.entrySet()) {
					//contractCode 和 contractWorkFeeSetting 都配置了
					if(contractWorkFeeSettingByCompany.get(contractCodeEntry.getKey()) != null) {
						Map<String, Double> assetCountMap = contractWorkFeeSettingByCompany.get(contractCodeEntry.getKey());
						Map<String, Map<String, Double>> map = new HashMap<String, Map<String, Double>>();
						Map<String, Map<String, Double>> inUsemap = new HashMap<String, Map<String, Double>>();
						Map<String, Map<String, Double>> noFeemap = new HashMap<String, Map<String, Double>>();
						Map<String, List<DmmRepositoryDTO>> assetSheetMap = new HashMap<String, List<DmmRepositoryDTO>>();
						for(Entry<String, Double> assetEntry : assetCountMap.entrySet()) {
							if(!IAtomsConstants.PARAM_FEE_PRICE_OFFEN.equals(assetEntry.getKey()) 
									&& !IAtomsConstants.PARAM_FEE_PRICE_FAST.equals(assetEntry.getKey()) 
									&& !IAtomsConstants.PARAM_FEE_PRICE_EXTRA.equals(assetEntry.getKey())) {
								assetMap = new HashMap<String, Double>();
								assetMap.put("inUse", new Double(0));
								assetMap.put("noFee", new Double(0));
								assetMap.put("fee", new Double(0));
								assetMap.put("borrowing", new Double(0));
								map.put(assetEntry.getKey(), assetMap);
								assetContractMap.put(contractCodeEntry.getKey(), map);
								assetInUseMap = new HashMap<String, Double>();
								assetInUseMap.put("0", new Double(0));
								assetInUseMap.put("0", new Double(0));
								inUsemap.put(assetEntry.getKey(), assetInUseMap);
								assetContractInUseMap.put(contractCodeEntry.getKey(), inUsemap);
								assetNoFeeMap = new HashMap<String, Double>();
								assetNoFeeMap.put("0", new Double(0));
								assetNoFeeMap.put("0", new Double(0));
								noFeemap.put(assetEntry.getKey(), assetNoFeeMap);
								assetContractNoFeeMap.put(contractCodeEntry.getKey(), noFeemap);
								DmmRepositoryDTO DmmRepositoryDTO = new DmmRepositoryDTO();
								DmmRepositoryDTO.setName(assetEntry.getKey());
								List<DmmRepositoryDTO> dmmRepositoryDTOList = new ArrayList<DmmRepositoryDTO>();
								dmmRepositoryDTOList.add(DmmRepositoryDTO);
								assetSheetMap.put(assetEntry.getKey(), dmmRepositoryDTOList);
								//dmmRepositoryListMap.put(contractCodeEntry.getKey() , assetSheetMap);
							}
						}
					}
				}
				//循環設備 構造map
				for (DmmRepositoryDTO dmmRepositoryDTO : DmmRepositoryDTOList) {
					//查看改合約編號是否在contractCode map裡面配置
					if(contractCodeByCompany.get(dmmRepositoryDTO.getContractCode()) != null) {
						//查看改合約編號是否在contractWorkFeeSetting map裡面配置
						if(contractWorkFeeSettingByCompany.get(dmmRepositoryDTO.getContractCode()) != null) {
							Map<String, Double> contractMap = contractWorkFeeSettingByCompany.get(dmmRepositoryDTO.getContractCode());
							if(contractMap.get(dmmRepositoryDTO.getName()) != null) {
								if(dmmRepositoryDTO.getEnableDate() != null) {
									Date enableDateAddThreeYear = DateTimeUtils.addCalendar(dmmRepositoryDTO.getEnableDate(), 3, 0, -1);
									Date enableDate = DateTimeUtils.addCalendar(dmmRepositoryDTO.getEnableDate(), 0, 0, 0);
									String enableDateAddThreeYearDay = DateTimeUtils.toString(enableDateAddThreeYear, DateTimeUtils.DT_FMT_YYYYMMDD);
									String enableDateAddThreeYearDayOver = enableDateAddThreeYearDay.substring(6, 8);
									int dayInMonth = Integer.parseInt(DateTimeUtils.toString(DateTimeUtils.addCalendar(endDate, 0, 0, -1), DateTimeUtils.DT_FMT_YYYYMMDD).substring(6, 8))
											- Integer.parseInt(DateTimeUtils.toString(enableDate, DateTimeUtils.DT_FMT_YYYYMMDD).substring(6, 8)) + 1;
									//使用中台數：(設備明細)中【狀態=使用中
									if(IAtomsConstants.PARAM_ASSET_STATUS_IN_USE.equals(dmmRepositoryDTO.getStatus())) {
										//【租賃未期滿 (設備啟用日期 + 3 年 - 1天)】<= 帳務月低
										if(enableDateAddThreeYear.compareTo(endDate) > -1) {
											if(enableDate.compareTo(startDate) > -1) {
												assetContractInUseMap = getAssetContractInUseMap(assetContractInUseMap, dmmRepositoryDTO, String.valueOf(dayInMonth));
											} else {
												assetContractMap = getAssetContractMap(assetContractMap, dmmRepositoryDTO, "inUse");
											}
										//小于 月底 大于 月初
										} else {
											if(enableDateAddThreeYear.compareTo(startDate) > -1) {
												assetContractInUseMap = getAssetContractInUseMap(assetContractInUseMap, dmmRepositoryDTO, enableDateAddThreeYearDayOver);
											}
										}
									//需收費之已啟用待裝機：(設備明細)中【狀態<>使用中、已報廢】
									} else if(!IAtomsConstants.PARAM_ASSET_STATUS_DISABLED.equals(dmmRepositoryDTO.getStatus())) {
										//【租賃未期滿 (設備啟用日期 + 3 年 - 1天)】<= 帳務月低
										if(enableDateAddThreeYear.compareTo(endDate) > -1) {
											if(StringUtils.hasText(dmmRepositoryDTO.getSerialNumber())) {
												//最近一次案件
												List<SrmCaseAssetLinkDTO> srmCaseAssetLinkDTOs = this.srmCaseAssetLinkDAO.getCaseLinkAndCaseInfoBySerialNumber(dmmRepositoryDTO.getSerialNumber());
												if(!CollectionUtils.isEmpty(srmCaseAssetLinkDTOs)) {
													if(srmCaseAssetLinkDTOs.size() > 0) {
														//案件類別=異動、拆機
														if(IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode().equals(srmCaseAssetLinkDTOs.get(0).getCaseCategory())
																|| IAtomsConstants.CASE_CATEGORY.UPDATE.getCode().equals(srmCaseAssetLinkDTOs.get(0).getCaseCategory())) {
															//解除綁定動作=已拆回
															if(IAtomsConstants.ACTION_REMOVE.equals(srmCaseAssetLinkDTOs.get(0).getAction())) {
																if(enableDate.compareTo(startDate) > -1) {
																	assetContractInUseMap = getAssetContractNoFeeMap(assetContractInUseMap, dmmRepositoryDTO, String.valueOf(dayInMonth));
																} else {
																	assetContractMap = getAssetContractMap(assetContractMap, dmmRepositoryDTO, "noFee");
																}
															}
														//不需收費之已啟用待裝機：(設備明細)中【狀態<>使用中、已報廢】
														//且【租賃未期滿 (設備啟用日期 + 3 年 - 1天)】
														//且【最近一次案件，案件類別=報修 且解除綁定動作=已拆回】數量
														} else if(IAtomsConstants.CASE_CATEGORY.REPAIR.getCode().equals(srmCaseAssetLinkDTOs.get(0).getCaseCategory())) {
															if(IAtomsConstants.ACTION_REMOVE.equals(srmCaseAssetLinkDTOs.get(0).getAction())) {
																assetContractMap = getAssetContractMap(assetContractMap, dmmRepositoryDTO, "fee");
															}
														}
													}
												}
											}
											//借用中設備：(設備明細)中【狀態=借用中】且【租賃未期滿 (設備啟用日期 + 3 年 - 1天)】數量
											if(IAtomsConstants.PARAM_ASSET_STATUS_BORROWING.equals(dmmRepositoryDTO.getStatus())) {
												assetContractMap = getAssetContractMap(assetContractMap, dmmRepositoryDTO, "borrowing");
											}
										//小于 月底 大于 月初
										} else {
											if(enableDateAddThreeYear.compareTo(startDate) > -1) {
												if(StringUtils.hasText(dmmRepositoryDTO.getSerialNumber())) {
													//最近一次案件
													List<SrmCaseAssetLinkDTO> srmCaseAssetLinkDTOs = this.srmCaseAssetLinkDAO.getCaseLinkAndCaseInfoBySerialNumber(dmmRepositoryDTO.getSerialNumber());
													if(!CollectionUtils.isEmpty(srmCaseAssetLinkDTOs)) {
														if(srmCaseAssetLinkDTOs.size() > 0) {
															//案件類別=異動、拆機
															if(IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode().equals(srmCaseAssetLinkDTOs.get(0).getCaseCategory())
																	|| IAtomsConstants.CASE_CATEGORY.UPDATE.getCode().equals(srmCaseAssetLinkDTOs.get(0).getCaseCategory())) {
																//解除綁定動作=已拆回
																if(IAtomsConstants.ACTION_REMOVE.equals(srmCaseAssetLinkDTOs.get(0).getAction())) {
																	assetContractInUseMap = getAssetContractNoFeeMap(assetContractInUseMap, dmmRepositoryDTO, enableDateAddThreeYearDayOver);
																}
															}
														}
													}
												}
											}
										}
									}
								}
								
								//報表result
								if(dmmRepositoryListMap.get(dmmRepositoryDTO.getContractCode()) != null) {
									Map<String, List<DmmRepositoryDTO>> assetNameMap = dmmRepositoryListMap.get(dmmRepositoryDTO.getContractCode());
									if(assetNameMap.get(dmmRepositoryDTO.getName()) != null) {
										List<DmmRepositoryDTO> dmmRepositoryDTOList = assetNameMap.get(dmmRepositoryDTO.getName());
										dmmRepositoryDTOList.add(dmmRepositoryDTO);
										assetNameMap.put(dmmRepositoryDTO.getName(), dmmRepositoryDTOList);
										dmmRepositoryListMap.put(dmmRepositoryDTO.getContractCode(), assetNameMap);
									} else {
										List<DmmRepositoryDTO> dmmRepositoryDTOList = new ArrayList<DmmRepositoryDTO>();
										dmmRepositoryDTOList.add(dmmRepositoryDTO);
										assetNameMap.put(dmmRepositoryDTO.getName(), dmmRepositoryDTOList);
										dmmRepositoryListMap.put(dmmRepositoryDTO.getContractCode(), assetNameMap);
									}
								} else {
									List<DmmRepositoryDTO> dmmRepositoryDTOList = new ArrayList<DmmRepositoryDTO>(); 
									dmmRepositoryDTOList.add(dmmRepositoryDTO);
									Map<String, List<DmmRepositoryDTO>> assetNameMap = new HashMap<String, List<DmmRepositoryDTO>>();
									assetNameMap.put(dmmRepositoryDTO.getName(), dmmRepositoryDTOList);
									dmmRepositoryListMap.put(dmmRepositoryDTO.getContractCode(), assetNameMap);
								}
							} else {
								LOGGER.debug(this.getClass().getSimpleName() + "維護費報表(彰銀格式) 合約編號" + dmmRepositoryDTO.getContractCode() + "下，未配置設備名稱" + dmmRepositoryDTO.getName());
								continue;
							}
						} else {
							LOGGER.debug(this.getClass().getSimpleName() + "維護費報表(彰銀格式) 合約編號" + dmmRepositoryDTO.getContractCode() + "未配置");
							continue;
						}
						
					} else {
						LOGGER.debug(this.getClass().getSimpleName() + "維護費報表(彰銀格式) 合約編號" + dmmRepositoryDTO.getContractCode() + "未配置");
						continue;
					}
				}
				//裝機的總數量
				int installCount = 0;
				//查核
				int checkCount = 0;
				//(作業明細)案件中【案件類別=裝機】且【設備有收維護費】數量
				int installMaintainCount = 0;
				//拆機費(租賃期滿後)
				int unInstallFeeCount = 0;
				//sn拆機
				String unstallSn = IAtomsConstants.MARK_EMPTY_STRING;
				//並機或者異動  處理方式=到場處理 的數量
				int updateOrMergeCount = 0;
				//並機或者異動 處理方式=軟排 的數量
				int updateSoftDispatch = 0;
				//sn到場
				String arriveSn = IAtomsConstants.MARK_EMPTY_STRING;
				//案件 合約 頻繁拆機費 急件加收 特急件加收
				Map<String, Map<String, Integer>> caseContractMap = new HashMap<String, Map<String,Integer>>();
				//循環案件信息
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
					if(!IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())){
						srmCaseHandleInfoDTO.setUserdDays120(IAtomsConstants.MARK_EMPTY_STRING);
					}
					//查看改合約編號是否在contractCode map裡面配置
					if(contractCodeByCompany.get(srmCaseHandleInfoDTO.getContractCode()) != null) {
						//查看改合約編號是否在contractWorkFeeSetting map裡面配置
						if(contractWorkFeeSettingByCompany.get(srmCaseHandleInfoDTO.getContractCode()) != null) {
							Map<String, Double> contractMap = contractWorkFeeSettingByCompany.get(srmCaseHandleInfoDTO.getContractCode());
							//(作業明細)案件中【案件類別=拆機】且【(拆機日- 裝機日)未滿四個月(120天)拆機】數量
							if(IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())){
								if(StringUtils.hasText(srmCaseHandleInfoDTO.getUserdDays120())) {
									if(Integer.parseInt(srmCaseHandleInfoDTO.getUserdDays120()) < 120) {
										srmCaseHandleInfoDTO.setUserdDays120(i18NUtil.getName(IAtomsConstants.YES));
										srmCaseHandleInfoDTO.setUserdDays120Price(contractMap.get(IAtomsConstants.EDC_FEE_REPORT_OFFEN).intValue());
										caseContractMap = getCaseContractMap(caseContractMap, srmCaseHandleInfoDTO, IAtomsConstants.EDC_FEE_REPORT_OFFEN);
									} else {
										srmCaseHandleInfoDTO.setUserdDays120(i18NUtil.getName(IAtomsConstants.NO));
									}
								} else {
									srmCaseHandleInfoDTO.setUserdDays120(i18NUtil.getName(IAtomsConstants.NO));
								}
								//【拆機類型=到場拆機、遺失報損】
								if(IAtomsConstants.EDC_FEE_REPORT_ARRIVE_UNINSTALL.equals(srmCaseHandleInfoDTO.getUninstallType())
										|| IAtomsConstants.EDC_FEE_REPORT_LOSS_REPORT.equals(srmCaseHandleInfoDTO.getUninstallType())) {
									for(DmmRepositoryDTO dmmRepositoryDTO : DmmRepositoryDTOLists) {
										if(dmmRepositoryDTO.getSerialNumber().equals(srmCaseHandleInfoDTO.getSerialNumber())) {
											unInstallFeeCount ++;
											WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory(IAtomsConstants.EDC_FEE_REPORT_UNINSTALLED);
											srmCaseHandleInfoDTO.setUnInstallPrice(workFeeDTO.getFirstPrice());
											break;
										}
									}
								} else if (IAtomsConstants.EDC_FEE_REPORT_SERVICE_SELF_UNINSTALL.equals(srmCaseHandleInfoDTO.getUninstallType())) {
									for(DmmRepositoryDTO dmmRepositoryDTO : DmmRepositoryDTOLists) {
										if(dmmRepositoryDTO.getSerialNumber().equals(srmCaseHandleInfoDTO.getSerialNumber())) {
											if(StringUtils.hasText(dmmRepositoryDTO.getSerialNumber())) {
												unstallSn = unstallSn + dmmRepositoryDTO.getSerialNumber() + IAtomsConstants.MARK_SEPARATOR;
											}
											break;
										}
									}
								}
							// 台數：(作業明細)案件中【 案件類別=裝機(裝機類型=一般特店、教育訓練機)、異動】使用之ECR線數量
								// 使用之案件單價不同，拆多筆，列出不同台數、單價
							} else if((IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())
									&& (IAtomsConstants.PARAM_INSTALL_TYPE_1.equals(srmCaseHandleInfoDTO.getInstallType())
											|| IAtomsConstants.PARAM_INSTALL_TYPE_2.equals(srmCaseHandleInfoDTO.getInstallType())))
									|| IAtomsConstants.CASE_CATEGORY.UPDATE.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())) {
//								for (SrmCaseAssetLinkDTO srmCaseAssetLinkDTO : srmCaseLinkECRLineList) {
//									if("ECRLINE".equals(srmCaseAssetLinkDTO.getItemCategory())) {
//										srmCaseLinkEcrLineDTOList.add(srmCaseAssetLinkDTO);
//									} else if("NetworkRoute".equals(srmCaseAssetLinkDTO.getItemCategory())) {
//										srmCaseLinkNetworkRouteDTOList.add(srmCaseAssetLinkDTO);
//									}
//								}
								//子報表中的ecr 和網路線價格
								Integer ecrlineSum = 0;
								Integer networkRouteSum = 0;
								for (SrmCaseAssetLinkDTO srmCaseAssetLinkDTO : srmCaseAssetLinkDTOList) {
									if(srmCaseAssetLinkDTO.getCaseId().equals(srmCaseHandleInfoDTO.getCaseId())) {
										if(srmCaseAssetLinkDTO.getPrice() == null) {
											srmCaseAssetLinkDTO.setPrice(new BigDecimal(0));
										}
										if(IAtomsConstants.PARAM_ECR_LINE.equals(srmCaseAssetLinkDTO.getItemCategory())) {
											ecrlineSum = srmCaseAssetLinkDTO.getPrice().intValue();
										} else if(IAtomsConstants.PARAM_NET_WORK_LINE.equals(srmCaseAssetLinkDTO.getItemCategory())) {
											networkRouteSum = srmCaseAssetLinkDTO.getPrice().intValue();
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
							}
							if(IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())){
								installCount ++;
								//(作業明細)案件中【案件類別=裝機】且【設備有收維護費】數量
								for(DmmRepositoryDTO dmmRepositoryDTO : DmmRepositoryDTOLists) {
									if(dmmRepositoryDTO.getSerialNumber().equals(srmCaseHandleInfoDTO.getSerialNumber())) {
										installMaintainCount ++;
										break;
									}
								}
							} else if(IAtomsConstants.CASE_CATEGORY.CHECK.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())) {
								checkCount ++;
							//(作業明細)案件中【案件類別=併機、異動】
							} else if(IAtomsConstants.CASE_CATEGORY.MERGE.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory()) 
									|| IAtomsConstants.CASE_CATEGORY.UPDATE.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())) {
								//且【處理方式=到場處理 且 是否同裝機作業=否
								if(("ARRIVE_PROCESS").equals(srmCaseHandleInfoDTO.getProcessType())) {
									if(IAtomsConstants.NO.equals(srmCaseHandleInfoDTO.getSameInstalled())) {
										//且【設備有收維護費】
										for(DmmRepositoryDTO dmmRepositoryDTO : DmmRepositoryDTOLists) {
											if(dmmRepositoryDTO.getSerialNumber().equals(srmCaseHandleInfoDTO.getSerialNumber())) {
												updateOrMergeCount ++;
												WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory(IAtomsConstants.EDC_FEE_REPORT_ARRIVED);
												srmCaseHandleInfoDTO.setSettingPrice(workFeeDTO.getFirstPrice());
												break;
											}
										}
										//且【是否同裝機作業=是
									} else {
										//且【設備有收維護費】
										for(DmmRepositoryDTO dmmRepositoryDTO : DmmRepositoryDTOLists) {
											if(dmmRepositoryDTO.getSerialNumber().equals(srmCaseHandleInfoDTO.getSerialNumber())) {
												if(StringUtils.hasText(dmmRepositoryDTO.getSerialNumber())) {
													arriveSn = arriveSn + dmmRepositoryDTO.getSerialNumber() + IAtomsConstants.MARK_SEPARATOR;
												}
												break;
											}
										}
									}
								//【處理方式 = 軟派】
								} else if(("SOFT_DISPATCH").equals(srmCaseHandleInfoDTO.getProcessType())) {
									//且【設備有收維護費】
									for(DmmRepositoryDTO dmmRepositoryDTO : DmmRepositoryDTOLists) {
										if(dmmRepositoryDTO.getSerialNumber().equals(srmCaseHandleInfoDTO.getSerialNumber())) {
											updateSoftDispatch ++;
										}
									}
								}
							} 
							//案件類型=急件
							if("FAST".equals(srmCaseHandleInfoDTO.getCaseType())) {
								caseContractMap = getCaseContractMap(caseContractMap, srmCaseHandleInfoDTO, "FAST");
								//WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("FAST");
								srmCaseHandleInfoDTO.setDispatchPrice(contractMap.get("FAST").intValue());
								//案件類型=特急件
							} else if("EXTRA".equals(srmCaseHandleInfoDTO.getCaseType())) {
								caseContractMap = getCaseContractMap(caseContractMap, srmCaseHandleInfoDTO, "EXTRA");
								//WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("EXTRA");
								srmCaseHandleInfoDTO.setDispatchPrice(contractMap.get("EXTRA").intValue());
							} else {
								srmCaseHandleInfoDTO.setDispatchPrice(null);
							}
						} else {
							srmCaseHandleInfoDTO.setUserdDays120(IAtomsConstants.MARK_EMPTY_STRING);
							LOGGER.debug(this.getClass().getSimpleName() + "維護費報表(彰銀格式) 合約編號" + srmCaseHandleInfoDTO.getContractCode() + "未配置");
							continue;
						}
					} else {
						srmCaseHandleInfoDTO.setUserdDays120(IAtomsConstants.MARK_EMPTY_STRING);
						LOGGER.debug(this.getClass().getSimpleName() + "維護費報表(彰銀格式) 合約編號" + srmCaseHandleInfoDTO.getContractCode() + "未配置");
						continue;
					}
				}
				//循環維護費設備
				//通訊模式支援 3G、GPRS、Bluetooth(3G/WIFI)、音源孔(3G/WIFI)】數量
				int commModeIdCount = 0;
				for (DmmRepositoryDTO dmmRepositoryDTO : DmmRepositoryDTOLists) {
					WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("MAINTENANCE_FEE");
					dmmRepositoryDTO.setMaintenanceTax(workFeeDTO.getFirstPrice());
					//查看改合約編號是否在contractCode map裡面配置
					if(contractCodeByCompany.get(dmmRepositoryDTO.getContractCode()) != null) {
						//查看改合約編號是否在contractWorkFeeSetting map裡面配置
						if(contractWorkFeeSettingByCompany.get(dmmRepositoryDTO.getContractCode()) != null) {
							Map<String, Double> contractMap = contractWorkFeeSettingByCompany.get(dmmRepositoryDTO.getContractCode());
							//說明維護費設備的合約在配置檔配置了
							if(contractMap.get(dmmRepositoryDTO.getName()) != null) {
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
										//若設備通訊模式含 3G、GPRS、
										//Bluetooth(3G/WIFI)、音源孔(3G/WIFI)，且狀態為使用中，則從作業別單價配置表
										commModeIdCount ++;
										break;
									}
								}
							} else {
								LOGGER.debug(this.getClass().getSimpleName() + "維護費報表(彰銀格式) 合約編號" + dmmRepositoryDTO.getContractCode() + "下，未配置設備名稱" + dmmRepositoryDTO.getName());
								continue;
							}
						} else {
							LOGGER.debug(this.getClass().getSimpleName() + "維護費報表(彰銀格式) 合約編號" + dmmRepositoryDTO.getContractCode() + "未配置");
							continue;
						}
					} else {
						LOGGER.debug(this.getClass().getSimpleName() + "維護費報表(彰銀格式) 合約編號" + dmmRepositoryDTO.getContractCode() + "未配置");
						continue;
					}
				}
				//第一個總表的匯出結果result
				List<DmmRepositoryDTO> dmmRepositoryList = new ArrayList<DmmRepositoryDTO>();
				Integer assetMainCountSum = 0;
				Integer assetMainPriceSum = 0;
				//循環合約map，對應到每一個合約
				for(Entry<String, Map<String, Map<String, Double>>> assetContractEntry : assetContractMap.entrySet()) {
					//每一個合約 就是一個子報表，所以 new一個子報表對象
					DmmRepositoryDTO dmmRepositoryDTO = new DmmRepositoryDTO();
					List<WorkFeeSetting> workFeeSettingList = new ArrayList<WorkFeeSetting>();
					Map<String, Map<String, Double>> assetCountMap = assetContractEntry.getValue();
					String contactValue = contractCodeByCompany.get(assetContractEntry.getKey());
					String title = "彰總財字第" + contactValue + "號" + IAtomsConstants.RETURN_LINE_FEED
							+ "(合約" + assetContractEntry.getKey() + ")";
					Integer countSum = 0;
					Integer priceSum = 0;
					//循環設備map，對應到每一個設備
					String monthDay = DateTimeUtils.toString(DateTimeUtils.addCalendar(endDate, 0, 0, -1), DateTimeUtils.DT_FMT_YYYYMMDD).substring(6, 8);
					for(Entry<String, Map<String, Double>> assetCountEntry : assetCountMap.entrySet()) {
						Map<String, Double> assetStatusMap = assetCountEntry.getValue();
						//使用中和不使用中，最少存在一種。必然.
						if(assetStatusMap.get("inUse") == null) {
							assetStatusMap.put("inUse", new Double(0));
						} 
						if(assetStatusMap.get("noFee") == null) {
							assetStatusMap.put("noFee", new Double(0));
						}
						if(assetStatusMap.get("fee") == null) {
							assetStatusMap.put("fee", new Double(0));
						}
						if(assetStatusMap.get("borrowing") == null) {
							assetStatusMap.put("borrowing", new Double(0));
						}
						WorkFeeSetting workFeeSetting = new WorkFeeSetting();
						workFeeSetting.setName("月租费("+assetCountEntry.getKey()+")");
						//從配置檔裡面 通過合約編號拿到合約map，再根據設備名稱，拿到對應的配置單價
						Double price = contractWorkFeeSettingByCompany.get(assetContractEntry.getKey()).get(assetCountEntry.getKey());
						//workFeeSetting.setFirstPrice(price.intValue());
						workFeeSetting.setFirstPriceDou(price);
						int inUseCount = 0;
						int sum = 0;
						Map<String, Double> inuseMap = assetContractInUseMap.get(assetContractEntry.getKey()).get(assetCountEntry.getKey());
						for(Entry<String, Double> inuseEntry : inuseMap.entrySet()) {
							inUseCount = inUseCount + new Double(inuseEntry.getValue()).intValue();
							//比例,多少天 除以 本月总天数
							double day = MathUtils.divide((double)(Integer.parseInt(inuseEntry.getKey())), (double)Integer.parseInt(monthDay));
							//比例天 乘以 台数 
							double inUse = MathUtils.multiply((double)(inuseEntry.getValue()), day);
							//此设备 使用中 特殊日期的 总价
							double inUseSum = MathUtils.multiply(price, inUse);
							BigDecimal inUseSumBig = new BigDecimal(String.valueOf(inUseSum));
					        double inUseSumDou = inUseSumBig.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
							sum = sum + (int)inUseSumDou;
						}
						int noFeeCount = 0;
						
						Map<String, Double> noFeeMap = assetContractNoFeeMap.get(assetContractEntry.getKey()).get(assetCountEntry.getKey());
						for(Entry<String, Double> noFeeEntry : noFeeMap.entrySet()) {
							noFeeCount = noFeeCount + new Double(noFeeEntry.getValue()).intValue();
							//比例,多少天 除以 本月总天数
							double day = MathUtils.divide((double)(Integer.parseInt(noFeeEntry.getKey())), (double)Integer.parseInt(monthDay));
							//比例天 乘以 台数 
							double noFee = MathUtils.multiply((double)(noFeeEntry.getValue()), day);
							//此设备 使用中 特殊日期的 总价
							double noFeeSum = MathUtils.multiply(price, noFee);
							BigDecimal noFeeSumBig = new BigDecimal(String.valueOf(noFeeSum));
					        double noFeeSumDou = noFeeSumBig.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
							sum = sum + (int)noFeeSumDou;
						}
						BigDecimal sumBig = new BigDecimal(String.valueOf(workFeeSetting.getFirstPriceDou() * (assetStatusMap.get("inUse") + assetStatusMap.get("noFee"))));
				        double sumDou = sumBig.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
				        
						workFeeSetting.setFirstCount(String.valueOf(new Double(assetStatusMap.get("inUse")).intValue() + new Double(assetStatusMap.get("noFee")).intValue()  + inUseCount + noFeeCount));
						workFeeSetting.setSum((int)sumDou + sum);
						workFeeSetting.setDescription("使用中台數" + (new Double(assetStatusMap.get("inUse")).intValue() + inUseCount) + "台" +
								"+需收費之已啟用待裝機" + (new Double(assetStatusMap.get("noFee")).intValue() + noFeeCount) + "台" + IAtomsConstants.RETURN_LINE_FEED + 
								"另有" + new Double(assetStatusMap.get("fee")).intValue() + "台不需收費之已啟用待裝機與" + new Double(assetStatusMap.get("borrowing")).intValue() + "台借用中設備");
						workFeeSetting.setSubTitle(title);
						countSum += Integer.parseInt(workFeeSetting.getFirstCount());
						priceSum += workFeeSetting.getSum();
						assetMainCountSum += Integer.parseInt(workFeeSetting.getFirstCount());
						assetMainPriceSum += workFeeSetting.getSum();
						workFeeSettingList.add(workFeeSetting);
					}
					for (WorkFeeSetting workFeeSetting : workFeeSettingList) {
						workFeeSetting.setCountSum(countSum);
						workFeeSetting.setPriceSum(priceSum);
					}
					dmmRepositoryDTO.setWorkFeeSettingList(workFeeSettingList);
					dmmRepositoryList.add(dmmRepositoryDTO);
				}
				
				//第2個總表的匯出結果result
				List<SrmCaseHandleInfoDTO> srmCaseHandleInfoList = new ArrayList<SrmCaseHandleInfoDTO>();
				//第二個總表裡面 上面相同部分的消小記總和
				int countSub = 0;
				//第二個總表裡面 所有的title總和。
				String mainTitle = IAtomsConstants.MARK_EMPTY_STRING;
				// 定義一個計數器
				int count = 0;
				//循環案件的map
				for(Entry<String, Map<String, Integer>> caseContractEntry : caseContractMap.entrySet()) {
					int flag = 0;
					SrmCaseHandleInfoDTO srmCaseHandleInfoDTO = new SrmCaseHandleInfoDTO();
					List<WorkFeeSetting> workFeeSettingList = new ArrayList<WorkFeeSetting>();
					Map<String, Integer> assetCountMap = caseContractEntry.getValue();
					Integer countSum = 0;
					Integer priceSum = 0;
					String contactValue = contractCodeByCompany.get(caseContractEntry.getKey());
					String title = "彰總財字第" + contactValue + "號" + IAtomsConstants.RETURN_LINE_FEED
							+ "(合約" + caseContractEntry.getKey() + ")";
					if(count % 2 == 0) {
						if(count == 0) {
							mainTitle = "彰總財字第" + contactValue + "號" + "(" + caseContractEntry.getKey() + ")";
						} else {
							//換行
							mainTitle += IAtomsConstants.RETURN_LINE_FEED + "彰總財字第" + contactValue + "號" + "(" + caseContractEntry.getKey() + ")";
						}
					} else {
						mainTitle += "和" + "彰總財字第" + contactValue + "號" + "(" + caseContractEntry.getKey() + ")";
					}
					//頻繁拆機費
					WorkFeeSetting workFeeSetting = new WorkFeeSetting();
					workFeeSetting.setName("頻繁拆機費");
					Double priceDouble = contractWorkFeeSettingByCompany.get(caseContractEntry.getKey()).get("OFFEN");
					//workFeeSetting.setFirstPrice(priceDouble.intValue());
					workFeeSetting.setFirstPriceDou(priceDouble);
					if(assetCountMap.get("OFFEN") != null) {
						workFeeSetting.setFirstCount(String.valueOf(assetCountMap.get("OFFEN")));
						workFeeSetting.setSum(Integer.parseInt(workFeeSetting.getFirstCount()) * new Double(workFeeSetting.getFirstPriceDou()).intValue());
					} else {
						workFeeSetting.setFirstCount(String.valueOf(0));
						workFeeSetting.setSum(0);
					}
					workFeeSetting.setSubTitle(title);
					countSum += Integer.parseInt(workFeeSetting.getFirstCount());
					priceSum += workFeeSetting.getSum();
					workFeeSettingList.add(workFeeSetting);
					//急件加收
					workFeeSetting = new WorkFeeSetting();
					workFeeSetting.setName("急件加收");
					priceDouble = contractWorkFeeSettingByCompany.get(caseContractEntry.getKey()).get("FAST");
					workFeeSetting.setFirstPriceDou(priceDouble.intValue());
					if(assetCountMap.get("FAST") != null) {
						workFeeSetting.setFirstCount(String.valueOf(assetCountMap.get("FAST")));
						workFeeSetting.setSum(Integer.parseInt(workFeeSetting.getFirstCount()) * new Double(workFeeSetting.getFirstPriceDou()).intValue());
					} else {
						workFeeSetting.setFirstCount(String.valueOf(0));
						workFeeSetting.setSum(0);
					}
					workFeeSetting.setSubTitle(title);
					countSum += Integer.parseInt(workFeeSetting.getFirstCount());
					priceSum += workFeeSetting.getSum();
					workFeeSettingList.add(workFeeSetting);
					//特急件加收
					workFeeSetting = new WorkFeeSetting();
					workFeeSetting.setName("特急件加收");
					priceDouble = contractWorkFeeSettingByCompany.get(caseContractEntry.getKey()).get("EXTRA");
					workFeeSetting.setFirstPriceDou(priceDouble.intValue());
					if(assetCountMap.get("EXTRA") != null) {
						workFeeSetting.setFirstCount(String.valueOf(assetCountMap.get("EXTRA")));
						workFeeSetting.setSum(Integer.parseInt(workFeeSetting.getFirstCount()) * new Double(workFeeSetting.getFirstPriceDou()).intValue());
					} else {
						workFeeSetting.setFirstCount(String.valueOf(0));
						workFeeSetting.setSum(0);
					}
					workFeeSetting.setSubTitle(title);
					countSum += Integer.parseInt(workFeeSetting.getFirstCount());
					priceSum += workFeeSetting.getSum();
					workFeeSettingList.add(workFeeSetting);
					for (WorkFeeSetting workFeeSettingS : workFeeSettingList) {
						workFeeSettingS.setCountSum(countSum);
						workFeeSettingS.setPriceSum(priceSum);
						if(flag == 0) {
							flag ++;
							countSub += priceSum;
						}
					}
					srmCaseHandleInfoDTO.setWorkFeeSettingList(workFeeSettingList);
					srmCaseHandleInfoList.add(srmCaseHandleInfoDTO);
					count ++;
				}
				for(Entry<String, String> contractCodeEntry : contractCodeByCompany.entrySet()) {
					//contractCode 和 contractWorkFeeSetting 都配置了
					if(contractWorkFeeSettingByCompany.get(contractCodeEntry.getKey()) != null) {
						//次合約編號不在本月使用案件中
						if(caseContractMap.get(contractCodeEntry.getKey()) == null) {
							SrmCaseHandleInfoDTO srmCaseHandleInfoDTO = new SrmCaseHandleInfoDTO();
							List<WorkFeeSetting> workFeeSettingList = new ArrayList<WorkFeeSetting>();
							String contactValue = contractCodeByCompany.get(contractCodeEntry.getKey());
							String title = "彰總財字第" + contactValue + "號" + IAtomsConstants.RETURN_LINE_FEED
									+ "(合約" + contractCodeEntry.getKey() + ")";
							if(count % 2 == 0) {
								if(count == 0) {
									mainTitle = "彰總財字第" + contactValue + "號" + "(" + contractCodeEntry.getKey() + ")";
								} else {
									//換行
									mainTitle += IAtomsConstants.RETURN_LINE_FEED + "彰總財字第" + contactValue + "號" + "(" + contractCodeEntry.getKey() + ")";
								}
							} else {
								mainTitle += "和" + "彰總財字第" + contactValue + "號" + "(" + contractCodeEntry.getKey() + ")";
							}
							//頻繁拆機費
							WorkFeeSetting workFeeSetting = new WorkFeeSetting();
							workFeeSetting.setName("頻繁拆機費");
							Double priceDouble = contractWorkFeeSettingByCompany.get(contractCodeEntry.getKey()).get("OFFEN");
							workFeeSetting.setFirstPriceDou(priceDouble);
							workFeeSetting.setFirstCount(String.valueOf(0));
							workFeeSetting.setSum(0);
							workFeeSetting.setSubTitle(title);
							workFeeSetting.setCountSum(0);
							workFeeSetting.setPriceSum(0);
							workFeeSettingList.add(workFeeSetting);
							//急件加收
							workFeeSetting = new WorkFeeSetting();
							workFeeSetting.setName("急件加收");
							priceDouble = contractWorkFeeSettingByCompany.get(contractCodeEntry.getKey()).get("FAST");
							workFeeSetting.setFirstPriceDou(priceDouble);
							workFeeSetting.setFirstCount(String.valueOf(0));
							workFeeSetting.setSum(0);
							workFeeSetting.setCountSum(0);
							workFeeSetting.setPriceSum(0);
							workFeeSetting.setSubTitle(title);
							workFeeSettingList.add(workFeeSetting);
							//特急件加收
							workFeeSetting = new WorkFeeSetting();
							workFeeSetting.setName("特急件加收");
							priceDouble = contractWorkFeeSettingByCompany.get(contractCodeEntry.getKey()).get("EXTRA");
							workFeeSetting.setFirstPriceDou(priceDouble);
							workFeeSetting.setFirstCount(String.valueOf(0));
							workFeeSetting.setSum(0);
							workFeeSetting.setCountSum(0);
							workFeeSetting.setPriceSum(0);
							workFeeSetting.setSubTitle(title);
							workFeeSettingList.add(workFeeSetting);
							srmCaseHandleInfoDTO.setWorkFeeSettingList(workFeeSettingList);
							srmCaseHandleInfoList.add(srmCaseHandleInfoDTO);
							count ++;
						}
					}
				}
				//第二個總表剩下的行
				SrmCaseHandleInfoDTO subReportCaseHandleInfoDTO = new SrmCaseHandleInfoDTO();
				List<WorkFeeSetting> subReportworkFeeSettingList = new ArrayList<WorkFeeSetting>();
				Integer casePriceSum = 0;
				//第2行--線材使用-ECR線材
				//使用之案件單價不同，拆多筆，列出不同台數、單價
				boolean haveEcrLine = false;
				boolean haveNetLine = false;
				for (SrmCaseAssetLinkDTO srmCaseAssetLinkDTO : srmCaseLinkList) {
					WorkFeeSetting workFeeDTOSupplies = new WorkFeeSetting();
					if(srmCaseAssetLinkDTO.getNumber() == null) srmCaseAssetLinkDTO.setNumber(0);
					if(srmCaseAssetLinkDTO.getPrice() == null) srmCaseAssetLinkDTO.setPrice(new BigDecimal(0));
					if("ECRLINE".equals(srmCaseAssetLinkDTO.getItemCategory())) {
						haveEcrLine = true;
						workFeeDTOSupplies.setWorkCategory("線材使用-ECR Cable");
						workFeeDTOSupplies.setName("線材使用-ECR Cable");
						workFeeDTOSupplies.setSubTitle(mainTitle);
						workFeeDTOSupplies.setFirstCount(String.valueOf(srmCaseAssetLinkDTO.getNumber()));
						workFeeDTOSupplies.setFirstPriceDou(srmCaseAssetLinkDTO.getPrice().doubleValue());
						workFeeDTOSupplies.setSum((Integer.parseInt(workFeeDTOSupplies.getFirstCount()) * srmCaseAssetLinkDTO.getPrice().intValue()));
						casePriceSum += workFeeDTOSupplies.getSum();
						subReportworkFeeSettingList.add(workFeeDTOSupplies);
					} else if("NetworkRoute".equals(srmCaseAssetLinkDTO.getItemCategory())) {
						haveNetLine = true;
						workFeeDTOSupplies.setWorkCategory("線材使用-網路線");
						workFeeDTOSupplies.setName("線材使用-網路線");
						workFeeDTOSupplies.setSubTitle(mainTitle);
						workFeeDTOSupplies.setFirstCount(String.valueOf(srmCaseAssetLinkDTO.getNumber()));
						workFeeDTOSupplies.setFirstPriceDou(srmCaseAssetLinkDTO.getPrice().doubleValue());
						workFeeDTOSupplies.setSum((Integer.parseInt(workFeeDTOSupplies.getFirstCount()) * srmCaseAssetLinkDTO.getPrice().intValue()));
						subReportworkFeeSettingList.add(workFeeDTOSupplies);
						casePriceSum += workFeeDTOSupplies.getSum();
					}
				}
				//如果沒有ECR那麼也要展示空行
				if(!haveEcrLine) {
					WorkFeeSetting workFeeDTOSupplies = new WorkFeeSetting();
					workFeeDTOSupplies.setWorkCategory("線材使用-ECR Cable");
					workFeeDTOSupplies.setName("線材使用-ECR Cable");
					workFeeDTOSupplies.setFirstCount(String.valueOf(0));
					workFeeDTOSupplies.setSubTitle(mainTitle);
					workFeeDTOSupplies.setFirstPriceDou(0);
					workFeeDTOSupplies.setSum(0);
					subReportworkFeeSettingList.add(workFeeDTOSupplies);
				}
				//如果沒有網路線那麼也要展示空行
				if(!haveNetLine) {
					WorkFeeSetting workFeeDTOSupplies = new WorkFeeSetting();
					workFeeDTOSupplies.setWorkCategory("線材使用-網路線");
					workFeeDTOSupplies.setName("線材使用-網路線");
					workFeeDTOSupplies.setFirstCount(String.valueOf(0));
					workFeeDTOSupplies.setSubTitle(mainTitle);
					workFeeDTOSupplies.setFirstPriceDou(0);
					workFeeDTOSupplies.setSum(0);
					subReportworkFeeSettingList.add(workFeeDTOSupplies);
				}
				//第四行 查核
				WorkFeeSetting workFeeDTOCheck = new WorkFeeSetting();
				WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("CHECK");
				workFeeDTOCheck.setName("查核(超過裝機量0.02)");
				//double installCountInt = installCount * 0.02;
				double installCountInt = MathUtils.multiply((double)installCount, 0.02);
				BigDecimal installCountBig = new BigDecimal(String.valueOf(installCountInt));
		        double installCountDou = installCountBig.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
				workFeeDTOCheck.setSubTitle(mainTitle);
				workFeeDTOCheck.setFirstCount(String.valueOf((int)(checkCount - installCountDou)));
				workFeeDTOCheck.setFirstPriceDou(new Double(workFeeDTO.getFirstPrice()));
				Double countCheck = Double.parseDouble(workFeeDTOCheck.getFirstCount());
				workFeeDTOCheck.setSum(countCheck.intValue() * new Double(workFeeDTOCheck.getFirstPriceDou()).intValue());
				casePriceSum += workFeeDTOCheck.getSum();
				subReportworkFeeSettingList.add(workFeeDTOCheck);
				//第五行 維修耗材費用
				WorkFeeSetting workFeeDTORepair = new WorkFeeSetting();
				workFeeDTO = getworkFeeNameByWorkCategory("CHECK");
				workFeeDTORepair.setName("維修耗材費用");
				workFeeDTORepair.setSubTitle(mainTitle);
				subReportworkFeeSettingList.add(workFeeDTORepair);
				//第六行 月維護費(租賃期滿後)
				WorkFeeSetting workFeeDTOMaintain = new WorkFeeSetting();
				workFeeDTO = getworkFeeNameByWorkCategory("MAINTENANCE_FEE");
				workFeeDTOMaintain.setName("月維護費(租賃期滿後)");
				workFeeDTOMaintain.setSubTitle(mainTitle);
				workFeeDTOMaintain.setFirstCount(String.valueOf(DmmRepositoryDTOLists.size()));
				workFeeDTOMaintain.setFirstPriceDou(new Double(workFeeDTO.getFirstPrice()));
				workFeeDTOMaintain.setSum(new Double(workFeeDTOMaintain.getFirstPriceDou()).intValue() * Integer.parseInt(workFeeDTOMaintain.getFirstCount()));
				casePriceSum += workFeeDTOMaintain.getSum();
				subReportworkFeeSettingList.add(workFeeDTOMaintain);
				//第七行 裝機/移機費(租賃期滿後)
				WorkFeeSetting workFeeDTOInstall = new WorkFeeSetting();
				workFeeDTO = getworkFeeNameByWorkCategory("INSTALLED");
				workFeeDTOInstall.setName("裝機/移機費(租賃期滿後)");
				workFeeDTOInstall.setSubTitle(mainTitle);
				workFeeDTOInstall.setFirstCount(String.valueOf(installMaintainCount));
				workFeeDTOInstall.setFirstPriceDou(new Double(workFeeDTO.getFirstPrice()));
				workFeeDTOInstall.setSum(new Double(workFeeDTOInstall.getFirstPriceDou()).intValue() * installMaintainCount);
				casePriceSum += workFeeDTOInstall.getSum();
				subReportworkFeeSettingList.add(workFeeDTOInstall);
				// 第八行 拆機費(租賃期滿後)
				WorkFeeSetting workFeeDTOUnInstall = new WorkFeeSetting();
				workFeeDTO = getworkFeeNameByWorkCategory("UNINSTALLED");
				workFeeDTOUnInstall.setName("拆機費(租賃期滿後)");
				workFeeDTOUnInstall.setSubTitle(mainTitle);
				workFeeDTOUnInstall.setFirstPriceDou(new Double(workFeeDTO.getFirstPrice()));
				workFeeDTOUnInstall.setFirstCount(String.valueOf(unInstallFeeCount));
				workFeeDTOUnInstall.setSum(new Double(workFeeDTOUnInstall.getFirstPriceDou()).intValue() * unInstallFeeCount);
				casePriceSum += workFeeDTOUnInstall.getSum();
				if(unstallSn.indexOf(IAtomsConstants.MARK_SEPARATOR) > -1) {
					workFeeDTOUnInstall.setDescription(unstallSn.substring(0, unstallSn.length() - 1));
				}
				subReportworkFeeSettingList.add(workFeeDTOUnInstall);
				//第九行 參數設定(到場)(租賃期滿後) 
				WorkFeeSetting workFeeDTOArrive = new WorkFeeSetting();
				workFeeDTO = getworkFeeNameByWorkCategory("ARRIVED");
				workFeeDTOArrive.setName("參數設定(到場)(租賃期滿後)");
				workFeeDTOArrive.setSubTitle(mainTitle);
				workFeeDTOArrive.setFirstCount(String.valueOf(updateOrMergeCount));
				workFeeDTOArrive.setFirstPriceDou(new Double(workFeeDTO.getFirstPrice()));
				workFeeDTOArrive.setSum(updateOrMergeCount * new Double(workFeeDTOArrive.getFirstPriceDou()).intValue());
				casePriceSum += workFeeDTOArrive.getSum();
				if(arriveSn.indexOf(IAtomsConstants.MARK_SEPARATOR) > -1) {
					workFeeDTOArrive.setDescription(arriveSn.substring(0, arriveSn.length() - 1));
				}
				subReportworkFeeSettingList.add(workFeeDTOArrive);
				//第十行 參數設定(遠端軟派)(租賃期滿後)
				WorkFeeSetting workFeeDTORemote = new WorkFeeSetting();
				workFeeDTO = getworkFeeNameByWorkCategory("REMOTE");
				workFeeDTORemote.setName("參數設定(遠端軟派)(租賃期滿後)");
				workFeeDTORemote.setSubTitle(mainTitle);
				workFeeDTORemote.setFirstCount(String.valueOf(updateSoftDispatch));
				workFeeDTORemote.setFirstPriceDou(workFeeDTO.getFirstPrice());
				workFeeDTORemote.setSum(updateSoftDispatch * new Double(workFeeDTORemote.getFirstPriceDou()).intValue());
				casePriceSum += workFeeDTORemote.getSum();
				subReportworkFeeSettingList.add(workFeeDTORemote);
				//第十一行 門號月租費(租賃期滿後)
				WorkFeeSetting workFeeDTONumber = new WorkFeeSetting();
				workFeeDTO = getworkFeeNameByWorkCategory("NUMBER");
				workFeeDTONumber.setName("門號月租費(租賃期滿後)");
				workFeeDTONumber.setSubTitle(mainTitle);
				workFeeDTONumber.setFirstCount(String.valueOf(commModeIdCount));
				workFeeDTONumber.setFirstPriceDou(workFeeDTO.getFirstPrice());
				workFeeDTONumber.setSum(commModeIdCount * new Double(workFeeDTONumber.getFirstPriceDou()).intValue());
				casePriceSum += workFeeDTONumber.getSum();
				subReportworkFeeSettingList.add(workFeeDTONumber);
				for (WorkFeeSetting workFeeSetting : subReportworkFeeSettingList) {
					workFeeSetting.setPriceSum(casePriceSum);
				}
				//將第二個子報表內容加到list裡面
				subReportCaseHandleInfoDTO.setWorkFeeSettingList(subReportworkFeeSettingList);
				//將第二個子報表加到子報表list裡面
				srmCaseHandleInfoList.add(subReportCaseHandleInfoDTO);
				
				//第三個子報表 APN月租費，VPN存取控制月租費 固定項目列出
				SrmCaseHandleInfoDTO subReportApnAndVpnDTO = new SrmCaseHandleInfoDTO();
				List<WorkFeeSetting> subReportApnAndVpnDTOList = new ArrayList<WorkFeeSetting>();
				Integer APNAndVPNSum = 0;
				//apn
				WorkFeeSetting workFeeDTOApn = new WorkFeeSetting();
				workFeeDTOApn.setName("APN月租費");
				workFeeDTOApn.setSum(1200);
				workFeeDTOApn.setDescription("每月固定收費：申請中華電信MDVPN，SIM卡必須加入APN連結後端的VPN。");
				APNAndVPNSum += workFeeDTOApn.getSum();
				subReportApnAndVpnDTOList.add(workFeeDTOApn);
				//vpn
				WorkFeeSetting workFeeDTOVpn = new WorkFeeSetting();
				workFeeDTOVpn.setName("VPN存取控制月租費");
				workFeeDTOVpn.setSum(2300);
				workFeeDTOVpn.setDescription("每月固定收費：前述APN必須同時連結經貿聯網VPN(No.2765)執行TMS程式參數下載及連結彰銀VPN(No. 3897)執行刷卡交易，故須設定此存取控制。");
				APNAndVPNSum += workFeeDTOVpn.getSum();
				subReportApnAndVpnDTOList.add(workFeeDTOVpn);
				for (WorkFeeSetting workFeeSetting : subReportApnAndVpnDTOList) {
					workFeeSetting.setPriceSum(APNAndVPNSum);
				}
				subReportApnAndVpnDTO.setWorkFeeSettingList(subReportApnAndVpnDTOList);
				srmCaseHandleInfoList.add(subReportApnAndVpnDTO);
				
				//TODO
				Map<String, String> subjrXmlNames = new HashMap<String, String>();
				subjrXmlNames.put("CHB_PRICE_SUM_REPORT_subreport1", "SUBREPORT_DIR");
				//第一個sheet
				criteria = new JasperReportCriteriaDTO();
				Map countMap = new HashMap();
				countMap.put("mainCountSum", assetMainCountSum);
				countMap.put("mainPriceSum", assetMainPriceSum);
				countMap.put("total", assetMainPriceSum);
				countMap.put("yyyy", DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYY));
				countMap.put("yyyyMMTitle", startDate.toString().substring(0, 4) + "年"
						+ startDate.toString().substring(5, 7) + "月費用統計表");
				criteria.setParameters(countMap);
				criteria.setAutoBuildJasper(false);
				criteria.setSubjrXmlNames(subjrXmlNames);
				if(dmmRepositoryList.size() == 0) {
					DmmRepositoryDTO DmmRepositoryDTO = new DmmRepositoryDTO();
					dmmRepositoryList.add(DmmRepositoryDTO);
				}
				criteria.setResult(dmmRepositoryList);
				//設置所需報表的Name
				criteria.setJrxmlName("CHB_PRICE_SUM_REPORT");
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				//設置報表Name
				StringBuffer fileNameBuffer = new StringBuffer();
				//維護費明細
				fileNameBuffer.append(DateTimeUtils.toString(startDate, DateTimeUtils.DT_FMT_YYYYMM));
				fileNameBuffer.append(IAtomsConstants.MARK_UNDER_LINE);
				fileNameBuffer.append(reportSettingDTOs.get(0).getCustomerName());
				fileNameBuffer.append(i18NUtil.getName(IAtomsConstants.FEE_REPORT_FOR_GREEN_WORLD_SUBJECT_CH_NAME));
				String fileName = fileNameBuffer.toString();
				criteria.setReportFileName(fileName);
				criteria.setSheetName("月租費用總表");
				criterias.add(criteria);
				
				//第2個sheet
				criteria = new JasperReportCriteriaDTO();
				Map countOtherMap = new HashMap();
				countOtherMap.put("mainCountSum", IAtomsConstants.MARK_EMPTY_STRING);
				countOtherMap.put("mainPriceSum", (casePriceSum + countSub + APNAndVPNSum));
				countOtherMap.put("total", casePriceSum + countSub + APNAndVPNSum);
				countOtherMap.put("yyyy", DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYY));
				countOtherMap.put("yyyyMMTitle", startDate.toString().substring(0, 4) + "年"
						+ startDate.toString().substring(5, 7) + "月費用統計表");
				criteria.setParameters(countOtherMap);
				criteria.setAutoBuildJasper(false);
				criteria.setSubjrXmlNames(subjrXmlNames);
				criteria.setResult(srmCaseHandleInfoList);
				//設置所需報表的Name
				criteria.setJrxmlName("CHB_PRICE_SUM_REPORT");
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				//設置報表Name
				criteria.setReportFileName(fileName);
				criteria.setSheetName("其他費用總表");
				criterias.add(criteria);
				//第456789個sheet 設備明細
				for(Entry<String, Map<String, List<DmmRepositoryDTO>>> assetContractEntry : dmmRepositoryListMap.entrySet()) {
					Map<String, List<DmmRepositoryDTO>> assetNameMap = assetContractEntry.getValue();
					for(Entry<String, List<DmmRepositoryDTO>> assetNameMapEntry : assetNameMap.entrySet()) {
						List<DmmRepositoryDTO> DmmRepositoryResult = assetNameMapEntry.getValue();
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
							criteria.setJrxmlName(IAtomsConstants.CHB_ASSET_INFO);
							//設置報表路徑
							criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
							//設置匯出格式
							criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
							//設置報表Name
							criteria.setSheetName(assetContractEntry.getKey() + " " + assetNameMapEntry.getKey() + "設備明細");
							criteria.setReportFileName(fileName);
							criterias.add(criteria);
							//循環剩下的合約,找出來相同的設備名稱.
							for(Entry<String, Map<String, List<DmmRepositoryDTO>>> assetContractOtherEntry : dmmRepositoryListMap.entrySet()) {
								if(!assetContractOtherEntry.getKey().equals(assetContractEntry.getKey())) {
									Map<String, List<DmmRepositoryDTO>> assetNameOtherMap = assetContractOtherEntry.getValue();
									int rowCountOther = 1;
									//剩餘合約裏面用設備名稱可以取到設備
									if(assetNameOtherMap.get(assetNameMapEntry.getKey()) != null) {
										List<DmmRepositoryDTO> DmmRepositoryOtherResult = assetNameOtherMap.get(assetNameMapEntry.getKey());
										assetNameOtherMap.remove(assetNameMapEntry.getKey());
										criteria = new JasperReportCriteriaDTO();
										criteria.setAutoBuildJasper(false);
										for (DmmRepositoryDTO dmmRepositoryDTO : DmmRepositoryOtherResult) {
											dmmRepositoryDTO.setRowId(String.valueOf(rowCountOther));
											rowCountOther ++;
										}
										criteria.setResult(DmmRepositoryOtherResult);
										//設置所需報表的Name
										criteria.setJrxmlName(IAtomsConstants.CHB_ASSET_INFO);
										//設置報表路徑
										criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
										//設置匯出格式
										criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
										//設置報表Name
										criteria.setSheetName(assetContractOtherEntry.getKey() + " " +assetNameMapEntry.getKey() + "設備明細");
										criteria.setReportFileName(fileName);
										criterias.add(criteria);
									}
								}
							}
						}
					}
				}
				//第10個sheet
				if(SrmCaseHandleInfoDTOList.size() == 0) {
					SrmCaseHandleInfoDTO srmCaseHandleInfoDTO = new SrmCaseHandleInfoDTO();
					SrmCaseHandleInfoDTOList.add(srmCaseHandleInfoDTO);
				}
				criteria = new JasperReportCriteriaDTO();
				criteria.setAutoBuildJasper(false);
				criteria.setResult(SrmCaseHandleInfoDTOList);
				//設置所需報表的Name
				criteria.setJrxmlName(IAtomsConstants.CHB_CASE_INFO_REPORT);
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
				//維護費設備
				if(DmmRepositoryDTOLists.size() == 0) {
					DmmRepositoryDTO DmmRepositoryDTO = new DmmRepositoryDTO();
					DmmRepositoryDTOLists.add(DmmRepositoryDTO);
					
				}
				criteria = new JasperReportCriteriaDTO();
				criteria.setAutoBuildJasper(false);
				criteria.setResult(DmmRepositoryDTOLists);
				//設置所需報表的Name
				criteria.setJrxmlName(IAtomsConstants.CHB_ASSET_MAINTAIN);
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				//設置報表Name
				criteria.setSheetName("維護費設備");
				criteria.setReportFileName(fileName);
				criterias.add(criteria);
				
				
				//第六個sheet 維修耗材費用-AO已回覆扣款方式
				if(srmPaymentInfoDTOList.size() == 0) {
					SrmPaymentInfoDTO srmPaymentInfoDTO = new SrmPaymentInfoDTO();
					srmPaymentInfoDTOList.add(srmPaymentInfoDTO);
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
				
				//第7個sheet 維修耗材費用-AO未回覆扣款方式
				if(paymentInfoDTOListNoTax.size() == 0) {
					SrmPaymentInfoDTO srmPaymentInfoDTO = new SrmPaymentInfoDTO();
					paymentInfoDTOListNoTax.add(srmPaymentInfoDTO);
				}
				criteria = new JasperReportCriteriaDTO();
				criteria.setAutoBuildJasper(false);
				criteria.setResult(paymentInfoDTOListNoTax);
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
	 * Purpose:獲取設備的map
	 * @author Hermanwang
	 * @param assetContractMap
	 * @param dmmRepositoryDTO
	 * @param assetStatus
	 * @return
	 * @return Map<String,Map<String,Map<String,Integer>>>
	 */
	private Map<String, Map<String, Map<String, Double>>> getAssetContractMap (Map<String, Map<String, Map<String, Double>>> assetContractMap, 
			DmmRepositoryDTO dmmRepositoryDTO, String assetStatus) {
		try {
			if(assetContractMap.get(dmmRepositoryDTO.getContractCode()) != null) {
				//從合約設備map裡面 取出來設備map
				Map<String, Map<String, Double>> assetCountMap = assetContractMap.get(dmmRepositoryDTO.getContractCode());
				//設備map裡面有設備名稱，那麼給該設備的數量 加1 否則 重新創建設備map，存放該設備名稱和台數 此時台數 為1
				if(assetCountMap.get(dmmRepositoryDTO.getName()) != null) {
					Map<String, Double> assetStatusMap = assetCountMap.get(dmmRepositoryDTO.getName());
					assetStatusMap.put(assetStatus, assetStatusMap.get(assetStatus) + 1);
					assetCountMap.put(dmmRepositoryDTO.getName(), assetStatusMap);
				} else {
					Map<String, Double> assetStatusMap = new HashMap<String, Double>();
					assetStatusMap.put(assetStatus, new Double(1));
					assetCountMap.put(dmmRepositoryDTO.getName(), assetStatusMap);
				}
				assetContractMap.put(dmmRepositoryDTO.getContractCode(), assetCountMap);
			} else {
				//合約是第一個合約，創建設備map 存放新的合約所對應的設備 設備對應的狀態和數量
				Map<String, Double> assetStatusMap = new HashMap<String, Double>();
				assetStatusMap.put(assetStatus, new Double(1));
				Map<String, Map<String, Double>> assetCountMap = new HashMap<String, Map<String, Double>>();
				assetCountMap.put(dmmRepositoryDTO.getName(), assetStatusMap);
				assetContractMap.put(dmmRepositoryDTO.getContractCode(), assetCountMap);
			}
		} catch (Exception e) {
			LOGGER.error("toReport() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return assetContractMap;
	}
	/**
	 * Purpose:獲取設備的无需收费天数特殊计算的map
	 * @author Hermanwang
	 * @param assetContractMap
	 * @param dmmRepositoryDTO
	 * @param assetStatus
	 * @return
	 * @return Map<String,Map<String,Map<String,Integer>>>
	 */
	private Map<String, Map<String, Map<String, Double>>> getAssetContractNoFeeMap (Map<String, Map<String, Map<String, Double>>> assetContractMap, 
			DmmRepositoryDTO dmmRepositoryDTO, String noFeeDay) {
		try {
			if(assetContractMap.get(dmmRepositoryDTO.getContractCode()) != null) {
				//從合約設備map裡面 取出來設備map
				Map<String, Map<String, Double>> assetCountMap = assetContractMap.get(dmmRepositoryDTO.getContractCode());
				//設備map裡面有設備名稱，那麼給該設備的數量 加1 否則 重新創建設備map，存放該設備名稱和台數 此時台數 為1
				if(assetCountMap.get(dmmRepositoryDTO.getName()) != null) {
					Map<String, Double> assetStatusMap = assetCountMap.get(dmmRepositoryDTO.getName());
					if(assetStatusMap.get(noFeeDay) != null) {
						assetStatusMap.put(noFeeDay, assetStatusMap.get(noFeeDay) + 1);
						assetCountMap.put(dmmRepositoryDTO.getName(), assetStatusMap);
					} else {
						assetStatusMap.put(noFeeDay, new Double(1));
						assetCountMap.put(dmmRepositoryDTO.getName(), assetStatusMap);
					}
				} else {
					Map<String, Double> assetStatusMap = new HashMap<String, Double>();
					assetStatusMap.put(noFeeDay, new Double(1));
					assetCountMap.put(dmmRepositoryDTO.getName(), assetStatusMap);
				}
				assetContractMap.put(dmmRepositoryDTO.getContractCode(), assetCountMap);
			} else {
				//合約是第一個合約，創建設備map 存放新的合約所對應的設備 設備對應的狀態和數量
				Map<String, Double> assetStatusMap = new HashMap<String, Double>();
				assetStatusMap.put(noFeeDay, new Double(1));
				Map<String, Map<String, Double>> assetCountMap = new HashMap<String, Map<String, Double>>();
				assetCountMap.put(dmmRepositoryDTO.getName(), assetStatusMap);
				assetContractMap.put(dmmRepositoryDTO.getContractCode(), assetCountMap);
			}
		} catch (Exception e) {
			LOGGER.error("toReport() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return assetContractMap;
	}
	/**
	 * Purpose:獲取設備的使用中天数特殊计算的map
	 * @author Hermanwang
	 * @param assetContractMap
	 * @param dmmRepositoryDTO
	 * @param assetStatus
	 * @return
	 * @return Map<String,Map<String,Map<String,Integer>>>
	 */
	private Map<String, Map<String, Map<String, Double>>> getAssetContractInUseMap (Map<String, Map<String, Map<String, Double>>> assetContractMap, 
			DmmRepositoryDTO dmmRepositoryDTO, String inUseDay) {
		try {
			if(assetContractMap.get(dmmRepositoryDTO.getContractCode()) != null) {
				//從合約設備map裡面 取出來設備map
				Map<String, Map<String, Double>> assetCountMap = assetContractMap.get(dmmRepositoryDTO.getContractCode());
				//設備map裡面有設備名稱，那麼給該設備的數量 加1 否則 重新創建設備map，存放該設備名稱和台數 此時台數 為1
				if(assetCountMap.get(dmmRepositoryDTO.getName()) != null) {
					Map<String, Double> assetStatusMap = assetCountMap.get(dmmRepositoryDTO.getName());
					if(assetStatusMap.get(inUseDay) != null) {
						assetStatusMap.put(inUseDay, assetStatusMap.get(inUseDay) + 1);
						assetCountMap.put(dmmRepositoryDTO.getName(), assetStatusMap);
					} else {
						assetStatusMap.put(inUseDay, new Double(1));
						assetCountMap.put(dmmRepositoryDTO.getName(), assetStatusMap);
					}
				} else {
					Map<String, Double> assetStatusMap = new HashMap<String, Double>();
					assetStatusMap.put(inUseDay, new Double(1));
					assetCountMap.put(dmmRepositoryDTO.getName(), assetStatusMap);
				}
				assetContractMap.put(dmmRepositoryDTO.getContractCode(), assetCountMap);
			} else {
				//合約是第一個合約，創建設備map 存放新的合約所對應的設備 設備對應的狀態和數量
				Map<String, Double> assetStatusMap = new HashMap<String, Double>();
				assetStatusMap.put(inUseDay, new Double(1));
				Map<String, Map<String, Double>> assetCountMap = new HashMap<String, Map<String, Double>>();
				assetCountMap.put(dmmRepositoryDTO.getName(), assetStatusMap);
				assetContractMap.put(dmmRepositoryDTO.getContractCode(), assetCountMap);
			}
		} catch (Exception e) {
			LOGGER.error("toReport() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return assetContractMap;
	}
	/**
	 * Purpose:獲取配置的設備數量的map
	 * @param caseContractMap
	 * @param srmCaseHandleInfoDTO
	 * @param caseType
	 * @return
	 */
	private Map<String, Map<String, Integer>> getCaseContractMap (Map<String, Map<String, Integer>> caseContractMap, 
			SrmCaseHandleInfoDTO srmCaseHandleInfoDTO, String caseType) {
		try {
			if(caseContractMap.get(srmCaseHandleInfoDTO.getContractCode()) != null) {
				Map<String, Integer> otherAssetMap = caseContractMap.get(srmCaseHandleInfoDTO.getContractCode());
				if(otherAssetMap.get(caseType) != null) {
					otherAssetMap.put(caseType, otherAssetMap.get(caseType) + 1);
					caseContractMap.put(srmCaseHandleInfoDTO.getContractCode(), otherAssetMap);
				} else {
					//otherAssetMap = new HashMap<String, Integer>();
					otherAssetMap.put(caseType, 1);
					caseContractMap.put(srmCaseHandleInfoDTO.getContractCode(), otherAssetMap);
				}
				
			} else {
				Map<String, Integer> otherAssetMap = new HashMap<String, Integer>();
				otherAssetMap.put(caseType, 1);
				caseContractMap.put(srmCaseHandleInfoDTO.getContractCode(), otherAssetMap);
			}
			
		} catch (Exception e) {
			LOGGER.error("toReport() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return caseContractMap;
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
	 * @return the contractCode
	 */
	public Map<String, String> getContractCode() {
		return contractCode;
	}
	/**
	 * @param contractCode the contractCode to set
	 */
	public void setContractCode(Map<String, String> contractCode) {
		this.contractCode = contractCode;
	}
	/**
	 * @return the contractWorkFeeSetting
	 */
	public Map<String, Map<String, Double>> getContractWorkFeeSetting() {
		return contractWorkFeeSetting;
	}
	/**
	 * @param contractWorkFeeSetting the contractWorkFeeSetting to set
	 */
	public void setContractWorkFeeSetting(
			Map<String, Map<String, Double>> contractWorkFeeSetting) {
		this.contractWorkFeeSetting = contractWorkFeeSetting;
	}
	/**
	 * @return the caseCategoryList
	 */
	public List<String> getCaseCategoryList() {
		return caseCategoryList;
	}
	/**
	 * @param caseCategoryList the caseCategoryList to set
	 */
	public void setCaseCategoryList(List<String> caseCategoryList) {
		this.caseCategoryList = caseCategoryList;
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
	public void setContractDAO(IContractDAO contractDAO) {
		this.contractDAO = contractDAO;
	}
}
