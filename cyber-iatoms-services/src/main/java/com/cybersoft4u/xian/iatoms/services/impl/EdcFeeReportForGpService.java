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

import org.springframework.util.CollectionUtils;

import cafe.core.config.SystemConfigManager;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
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
 * Purpose: 維護費報表(環匯格式)報表作業
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/9/12
 * @MaintenancePersonnel CarrieDuan
 */
public class EdcFeeReportForGpService extends AtomicService implements IReportService {
		
	/**
	 * 記錄日誌
	 */
	private static final CafeLog LOGGER	= CafeLogFactory.getLog(EdcFeeReportForGpService.class);
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
	 * 求償作業DAO
	 */
	private ISrmPaymentInfoDAO paymentInfoDAO;
	/**
	 * 設備月檔
	 */
	private IDmmRepositoryHistoryMonthlyDAO dmmRepositoryHistoryMonthlyDAO;
	/**
	 * 
	 */
	private Map<String, String> cnMonth;
	/**
	 * 計算合計所需數據
	 */
	private List<Double> calculatioNumber;
	/**
	 * 作業別
	 */
	private List<WorkFeeSetting> workFeeSetting;
	/**
	 * SRM_案件處理資料檔DAO
	 */
	private ISrmCaseHandleInfoDAO caseHandleInfoDAO;
	/**
	 * 案件狀態
	 */
	private List<String> caseStatus;
	/**
	 * 一般交易參數
	 */
	private List<String> commonTransactionType;
	/**
	 * SRM_案件處理中設備連接檔
	 */
	private ISrmCaseAssetLinkDAO caseAssetLinkDAO;
	/**
	 * 
	 */
	private List<String> suppliesTypes;
	/**
	 * 查詢啟用設備明細所用案件狀態
	 */
	private List<String> enableAssetStatus;
	/**
	 * 求償狀態
	 */
	private List<String> paymentStatusList;
	/**
	 * 賠償方式
	 */
	private List<String> paymentTypeList;
	/**
	 * 需要统计的通讯模式ID
	 */
	private List<String> commModeId;
	
	/**
	 * Purpose:系統時間每月5號，4:30，發送mail
	 * @author CarrieDuan
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return void
	 */
	public void toReport() throws ServiceException {
		CompanyDTO companyDTO = this.companyDAO.getCompanyByCompanyCode(IAtomsConstants.PARAMTER_COMPANY_CODE_GP_EDC);
		if (companyDTO != null) {
			sendReportMail(DateTimeUtils.getCurrentDate(), companyDTO.getCompanyId(), IAtomsConstants.REPORT_NAME_EDC_FEE_FOR_JDW);
			//sendReportMail(DateTimeUtils.getCurrentDate(), "1495682122030-0366", IAtomsConstants.FEE_REPORT_FOR_GP);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IReportService#sendReportMail(java.util.Date, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendReportMail(Date sendDate, String customerId, String reportCode) throws ServiceException {
		try {
			List<ReportSettingDTO> reportSettingList = this.reportSettingDAO.getDetailList(customerId, reportCode);
			if (!CollectionUtils.isEmpty(reportSettingList)){
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
				String monthYear = DateTimeUtils.toString(startDate, DateTimeUtils.DT_FMT_YYYYMM);
				//統計特急件、幾件的單價
				Map<String, Integer> priceMap = new HashMap<String, Integer>();
				for (WorkFeeSetting workFee : workFeeSetting) {
					if (IAtomsConstants.TICKET_MODE_FAST.equals(workFee.getWorkCategory())) {
						priceMap.put(IAtomsConstants.TICKET_MODE_FAST, workFee.getPrice());
						continue;
					} else if (IAtomsConstants.TICKET_MODE_EXTRA.equals(workFee.getWorkCategory())) {
						priceMap.put(IAtomsConstants.TICKET_MODE_EXTRA, workFee.getPrice());
						continue;
					}
				}
				//獲得費用總表數據
				List<DmmRepositoryDTO> repositoryDTOs = this.dmmRepositoryHistoryMonthlyDAO.listFeeAssetListForGp(monthYear, commModeId, customerId);
				//得到正在使用的設備數據
				List<DmmRepositoryDTO> inUseRepositoryDTOs = this.dmmRepositoryHistoryMonthlyDAO.listFeeAssetListInUseForGp(monthYear, startDate, endDate, commModeId, customerId);
				//報廢機明細
				List<DmmRepositoryDTO> scrapAssets = this.dmmRepositoryHistoryMonthlyDAO.listScrapAssetForGp(monthYear, customerId);
				//作業明細
				List<SrmCaseHandleInfoDTO> caseWorkDetailHandleInfoDTOs = this.caseHandleInfoDAO.listCaseWorkDetailForGp(caseStatus, priceMap, customerId, startDate, endDate, commonTransactionType);
				//ecr連線、網路線
				List<SrmCaseAssetLinkDTO> assetLinkDTOs = this.caseAssetLinkDAO.listSuppliesTypeForGp(caseStatus, customerId, startDate, endDate, suppliesTypes);
				//啟用設備明細
				List<DmmRepositoryDTO> enableRepositoryDTOs = this.dmmRepositoryHistoryMonthlyDAO.listEnableAssetInfoForGp(monthYear, startDate, endDate, enableAssetStatus, customerId);
				//記錄ecr連線
				List<SrmCaseAssetLinkDTO> ecrLines = new ArrayList<SrmCaseAssetLinkDTO>();
				//記錄網路線
				List<SrmCaseAssetLinkDTO> networkroutes = new ArrayList<SrmCaseAssetLinkDTO>();
				//維修耗材費用-AO已回覆扣款方式
				List<SrmPaymentInfoDTO> paymentInfoDTOs = this.paymentInfoDAO.paymentInfoListToGreenWorld(paymentTypeList, customerId);
				//維修耗材費用-AO未回覆扣款方式
				List<SrmPaymentInfoDTO> paymentInfoDTOsNoTax = this.paymentInfoDAO.paymentInfoListToGreenWorldNoTax(paymentStatusList, customerId);
				//分別統計網路線、ecr連線
				if (!CollectionUtils.isEmpty(assetLinkDTOs)) {
					for (SrmCaseAssetLinkDTO caseAssetLinkDTO : assetLinkDTOs) {
						if (IAtomsConstants.PARAM_NET_WORK_LINE.equals(caseAssetLinkDTO.getItemCategory())) {
							networkroutes.add(caseAssetLinkDTO);
						} else if(IAtomsConstants.PARAM_ECR_LINE.equals(caseAssetLinkDTO.getItemCategory())) {
							ecrLines.add(caseAssetLinkDTO);
						}
					}
				}
				//記錄每個客戶下的報廢設備序號
				Map<String, String> scrapAsset = new HashMap<String, String>();
				List<JasperReportCriteriaDTO> criterias = new ArrayList<JasperReportCriteriaDTO>();
				StringBuffer assetBuffer = null;
				String assetTypeId = "";
				//將設備對應的設備序號進行整合
				if (!CollectionUtils.isEmpty(scrapAssets)) {
					for (DmmRepositoryDTO dmmRepositoryDTO : scrapAssets) {
						if (StringUtils.hasText(assetTypeId) && assetTypeId.equals(dmmRepositoryDTO.getAssetTypeId())) {
							assetBuffer.append(dmmRepositoryDTO.getSerialNumber()).append(IAtomsConstants.MARK_SEPARATOR);
						} else {
							if (StringUtils.hasText(assetTypeId)) {
								scrapAsset.put(assetTypeId, assetBuffer.substring(0, assetBuffer.length() - 1).toString());
							}
							assetTypeId = dmmRepositoryDTO.getAssetTypeId();
							assetBuffer = new StringBuffer();
							assetBuffer.append(dmmRepositoryDTO.getSerialNumber()).append(IAtomsConstants.MARK_SEPARATOR);
						}
					}
					scrapAsset.put(assetTypeId, assetBuffer.substring(0, assetBuffer.length() - 1).toString());
				}
				//台數
				Integer number = 0;
				//合計
				Double total = 0.0;
				//使用中
				Integer inUse = 0;
				//啟用中的數據
				Integer inEnable = 0;
				//單價
				Integer price = 0;
				//
				List<WorkFeeSetting> workFeeSettings = new ArrayList<WorkFeeSetting>();
				//记录设备ID
				List<String> assetTypeIds = new ArrayList<String>();
				//(維護費)設備中【通訊模式支援 3G、GPRS、Bluetooth(3G/WIFI)、音源孔(3G/WIFI)】數量
				Integer commModeNum = 0;
				//記錄臨時數據
				Integer temp = 0;
				Integer freeTotal = 0;
				WorkFeeSetting feeSetting = null;
				if (!CollectionUtils.isEmpty(repositoryDTOs) || !CollectionUtils.isEmpty(inUseRepositoryDTOs)) {
					if (CollectionUtils.isEmpty(repositoryDTOs)) {
						repositoryDTOs = new ArrayList<DmmRepositoryDTO>();
						for (DmmRepositoryDTO inUseDTO : inUseRepositoryDTOs) {
							repositoryDTOs.add(inUseDTO);
						}
					} else {
						if (!CollectionUtils.isEmpty(inUseRepositoryDTOs)) {
							for (DmmRepositoryDTO repositoryDTO : repositoryDTOs) {
								for (DmmRepositoryDTO inUseDTO : inUseRepositoryDTOs) {
									if (inUseDTO.getAssetTypeId().equals(repositoryDTO.getAssetTypeId())) {
										repositoryDTO.setInUse(inUseDTO.getInUse());
										inUseRepositoryDTOs.remove(inUseDTO);
										break;
									}
								}
							}
							if (!CollectionUtils.isEmpty(inUseRepositoryDTOs)) {
								for (DmmRepositoryDTO inUseDTO : inUseRepositoryDTOs) {
									repositoryDTOs.add(inUseDTO);
								}
							}
						}
					}
				}
				if (!CollectionUtils.isEmpty(repositoryDTOs)) {
					Boolean isHave3G = Boolean.FALSE;
					for (DmmRepositoryDTO dmmRepositoryDTO : repositoryDTOs) {
						assetTypeIds.add(dmmRepositoryDTO.getAssetTypeId());
						if (dmmRepositoryDTO.getCommModeNum() == 1) {
							isHave3G = Boolean.TRUE;
						}
						total = 0.0;
						number = 0;
						inUse = 0;
						temp = 0;
						feeSetting = new WorkFeeSetting();
						//計算啟用中中數據
						inEnable = dmmRepositoryDTO.getInEnable();
						//计算使用中数据
						inUse = dmmRepositoryDTO.getInUse();
						////計算使用中數據,判斷inUseRepositoryDTOs如果為空，則取repositoryDTOs中的使用中數據
						/*if (CollectionUtils.isEmpty(inUseRepositoryDTOs)) {
							inUse = dmmRepositoryDTO.getInUse();
						} else {
							for (DmmRepositoryDTO inUseRepositoryDTO : inUseRepositoryDTOs) {
								if (StringUtils.hasText(inUseRepositoryDTO.getAssetTypeId()) 
										&& inUseRepositoryDTO.getAssetTypeId().equals(dmmRepositoryDTO.getAssetTypeId())) {
									inUse += 1;
								}
							}
						}*/
						number = inEnable + inUse;
						if (isHave3G) {
							commModeNum += number;
							isHave3G = Boolean.FALSE;
						}
						
						//計算合計
						total = (number + (dmmRepositoryDTO.getTotal() - dmmRepositoryDTO.getInScrap()) 
								* calculatioNumber.get(0)) * calculatioNumber.get(1);
						//備註
						StringBuffer buffer = new StringBuffer();
						buffer.append(IAtomsConstants.MARK_BRACKETS_LEFT).append(number);
						buffer.append(IAtomsConstants.MARK_ADD).append(IAtomsConstants.MARK_BRACKETS_LEFT);
						buffer.append(dmmRepositoryDTO.getTotal()).append(IAtomsConstants.MARK_CENTER_LINE);
						buffer.append(dmmRepositoryDTO.getInScrap()).append(IAtomsConstants.MARK_BRACKETS_RIGHT);
						buffer.append(IAtomsConstants.MARK_MULTIPLICATION).append(calculatioNumber.get(0));
						buffer.append(IAtomsConstants.MARK_BRACKETS_RIGHT).append(IAtomsConstants.MARK_MULTIPLICATION);
						buffer.append(calculatioNumber.get(1)).append(IAtomsConstants.MARK_EQUALS).append(Math.round(total));
						if (scrapAsset.containsKey(dmmRepositoryDTO.getAssetTypeId())) {
							temp = scrapAsset.get(dmmRepositoryDTO.getAssetTypeId()).split(IAtomsConstants.MARK_SEPARATOR).length;
						}
						String remark = i18NUtil.getName(IAtomsMessageCode.FEE_TOTAL_REMARK, new String[]{
								buffer.toString(), calculatioNumber.get(0).toString(), calculatioNumber.get(1).toString(),
								dmmRepositoryDTO.getName(), temp.toString(), scrapAsset.containsKey(dmmRepositoryDTO.getAssetTypeId())?scrapAsset.get(dmmRepositoryDTO.getAssetTypeId()):""
						}, null);
						if (temp == 0) {
							remark = remark.substring(0, remark.lastIndexOf("\n"));
							remark = remark.substring(0, remark.lastIndexOf("\n"));
						}
						
						feeSetting.setName(i18NUtil.getName(IAtomsMessageCode.ASSET_MAINTAIN_FEE, new String[]{dmmRepositoryDTO.getName()}, null));
						feeSetting.setNumber(number);
						feeSetting.setPriceString(IAtomsConstants.MARK_MIDDLE_LINE);
						feeSetting.setTotal((int)Math.round(total));
						feeSetting.setRemarks(remark);
						feeSetting.setOtherRemarks(i18NUtil.getName(IAtomsMessageCode.IN_USE_AND_IN_ENABLE_NUMBER, new String[]{
								inUse.toString(), inEnable.toString()
						}, null));
						workFeeSettings.add(feeSetting);
						freeTotal += feeSetting.getTotal();
					}
				}
				//急件加收
				feeSetting = workFeeSetting.get(0);
				number = 0;
				//計算台數（(作業明細)案件中【 案件類型=急件】數量）
				if (!CollectionUtils.isEmpty(caseWorkDetailHandleInfoDTOs)) {
					for (SrmCaseHandleInfoDTO caseHandleInfoDTO : caseWorkDetailHandleInfoDTOs) {
						if (IAtomsConstants.TICKET_MODE_FAST.equals(caseHandleInfoDTO.getCaseType())) {
							number += 1;
						}
					}
				}
				//單價
				price = feeSetting.getPrice();
				//合計（台數*單價）
				total = (double)(price * number);
				feeSetting.setNumber(number);
				feeSetting.setTotal((int)Math.round(total));
				feeSetting.setPriceString(IAtomsConstants.MARK_DOLLAR + DecimalFormat.getNumberInstance().format(price));
				freeTotal += (int)Math.round(total);
				workFeeSettings.add(feeSetting);

				//特急件加收
				feeSetting = workFeeSetting.get(1);
				number = 0;
				if (!CollectionUtils.isEmpty(caseWorkDetailHandleInfoDTOs)) {
					for (SrmCaseHandleInfoDTO caseHandleInfoDTO : caseWorkDetailHandleInfoDTOs) {
						if (IAtomsConstants.TICKET_MODE_EXTRA.equals(caseHandleInfoDTO.getCaseType())) {
							number += 1;
						}
					}
				}
				//單價
				price = feeSetting.getPrice();
				//合計（台數*單價）
				total = (double)(price * number);
				feeSetting.setNumber(number);
				feeSetting.setTotal((int)Math.round(total));
				feeSetting.setPriceString(IAtomsConstants.MARK_DOLLAR + DecimalFormat.getNumberInstance().format(price));
				freeTotal += (int)Math.round(total);
				workFeeSettings.add(feeSetting);

				//線材使用-網路線
				//獲取作業明細案件類別為裝機或異動的案件編號
				List<String> caseIds = new ArrayList<String>();
				for (SrmCaseHandleInfoDTO caseHandleInfoDTO : caseWorkDetailHandleInfoDTOs) {
					if (IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(caseHandleInfoDTO.getCaseCategory())
							|| IAtomsConstants.CASE_CATEGORY.UPDATE.getCode().equals(caseHandleInfoDTO.getCaseCategory())) {
						caseIds.add(caseHandleInfoDTO.getCaseId());
					}
				}
				Boolean isFlag = Boolean.FALSE;
				if (!CollectionUtils.isEmpty(caseIds) && !CollectionUtils.isEmpty(networkroutes)) {
					for (SrmCaseAssetLinkDTO assetLinkDTO : networkroutes) {
						if (caseIds.contains(assetLinkDTO.getCaseId())) {
							isFlag = Boolean.TRUE;
							feeSetting = (WorkFeeSetting) workFeeSetting.get(2).clone();
							feeSetting.setNumber(assetLinkDTO.getNumber());
							feeSetting.setPriceString(IAtomsConstants.MARK_DOLLAR + DecimalFormat.getNumberInstance().format(assetLinkDTO.getPrice().intValue()));
							feeSetting.setTotal(assetLinkDTO.getNumber() * assetLinkDTO.getPrice().intValue());
							freeTotal += feeSetting.getTotal();
							workFeeSettings.add(feeSetting);
						}
					}
					if (!isFlag) {
						workFeeSettings.add(workFeeSetting.get(2));
					}
				} else {
					workFeeSettings.add(workFeeSetting.get(2));
				}

				//線材使用-ECR線材
				if (!CollectionUtils.isEmpty(caseIds) && !CollectionUtils.isEmpty(ecrLines)) {
					isFlag = Boolean.FALSE;
					for (SrmCaseAssetLinkDTO assetLinkDTO : ecrLines) {
						if (caseIds.contains(assetLinkDTO.getCaseId())) {
							isFlag = Boolean.TRUE;
							feeSetting = (WorkFeeSetting) workFeeSetting.get(3).clone();
							feeSetting.setNumber(assetLinkDTO.getNumber());
							feeSetting.setPriceString(IAtomsConstants.MARK_DOLLAR + DecimalFormat.getNumberInstance().format(assetLinkDTO.getPrice().intValue()));
							feeSetting.setTotal(assetLinkDTO.getNumber() * assetLinkDTO.getPrice().intValue());
							freeTotal += feeSetting.getTotal();
							workFeeSettings.add(feeSetting);
						}
					}
					if (!isFlag) {
						workFeeSettings.add(workFeeSetting.get(3));
					}
				} else {
					workFeeSettings.add(workFeeSetting.get(3));
				}
				
				//維修耗材費用
				workFeeSettings.add(workFeeSetting.get(4));
				
				//台灣大哥大專案-收機
				workFeeSettings.add(workFeeSetting.get(5));
				
				//單獨裝/拆/異動 R50 \& SP20
				feeSetting = (WorkFeeSetting) workFeeSetting.get(6).clone();
				feeSetting.setPriceString(IAtomsConstants.MARK_DOLLAR + DecimalFormat.getNumberInstance().format(feeSetting.getPrice()));
				workFeeSettings.add(feeSetting);
				feeSetting = (WorkFeeSetting) workFeeSetting.get(6).clone();
				feeSetting.setPriceString(IAtomsConstants.MARK_DOLLAR + DecimalFormat.getNumberInstance().format(feeSetting.getOtherPrice()));
				workFeeSettings.add(feeSetting);
				
				//3G／GPRS／行動通信月租費
				feeSetting = workFeeSetting.get(7);
				feeSetting.setNumber(commModeNum);
				feeSetting.setTotal(commModeNum * feeSetting.getPrice());
				feeSetting.setPriceString(IAtomsConstants.MARK_DOLLAR + DecimalFormat.getNumberInstance().format(feeSetting.getPrice()));
				freeTotal += commModeNum * feeSetting.getPrice();
				workFeeSettings.add(feeSetting);
				
				//APN月租費
				feeSetting = workFeeSetting.get(8);
				feeSetting.setPriceString(IAtomsConstants.MARK_DOLLAR + DecimalFormat.getNumberInstance().format(feeSetting.getPrice()));
				total = (double) (feeSetting.getPrice() * feeSetting.getNumber());
				feeSetting.setTotal((int)Math.round(total));
				freeTotal += (int)Math.round(total);
				workFeeSettings.add(feeSetting);
				
				//VPN存取控制月租費
				feeSetting = workFeeSetting.get(9);
				feeSetting.setPriceString(IAtomsConstants.MARK_DOLLAR + DecimalFormat.getNumberInstance().format(feeSetting.getPrice()));
				total = (double) (feeSetting.getPrice() * feeSetting.getNumber());
				feeSetting.setTotal((int)Math.round(total));
				freeTotal += (int)Math.round(total);
				workFeeSettings.add(feeSetting);
				
				//郵件內容
				JasperReportCriteriaDTO criteria = null;
				//創建第一個sheet（费用总表）
				criteria = new JasperReportCriteriaDTO();
				criteria.setAutoBuildJasper(false);
				criteria.setResult(workFeeSettings);
				//設置所需報表的Name
				criteria.setJrxmlName(IAtomsConstants.EDC_FEE_REPORT_FOR_GP_WORK_FEE);
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				criteria.setSheetName(i18NUtil.getName(IAtomsConstants.EDC_FEE_REPORT_FOR_JDW_WORK_FEE));
				Map titleNameMap = new HashMap();
				titleNameMap.put("month", i18NUtil.getName(IAtomsMessageCode.EDC_FEE_REPORT_FOR_JDW_TITLE, 
						new String[]{monthYear}, null).substring(0, 6).concat(i18NUtil.getName(IAtomsMessageCode.EDC_FEE_REPORT_FOR_JDW_TITLE, 
						new String[]{monthYear}, null).substring(7, i18NUtil.getName(IAtomsMessageCode.EDC_FEE_REPORT_FOR_JDW_TITLE, 
						new String[]{monthYear}, null).length())));
				titleNameMap.put("year", Integer.toString(calEnd.get(Calendar.YEAR)));
				titleNameMap.put("totalNumber", freeTotal.intValue());
				criteria.setParameters(titleNameMap);
				criterias.add(criteria);
				
				//創建啟用設備明細sheet
				List<DmmRepositoryDTO> dtos = new ArrayList<DmmRepositoryDTO>();
				assetTypeId = enableRepositoryDTOs.get(0).getAssetTypeId();
				if (!CollectionUtils.isEmpty(repositoryDTOs)) {
					for (DmmRepositoryDTO dmmRepositoryDTO : repositoryDTOs) {
						for (DmmRepositoryDTO repositoryDTO : enableRepositoryDTOs) {
							if (repositoryDTO.getAssetTypeId().equals(dmmRepositoryDTO.getAssetTypeId())) {
								dtos.add(repositoryDTO);
							}
						}
						criteria = new JasperReportCriteriaDTO();
						criteria.setAutoBuildJasper(false);
						criteria.setResult(dtos);
						//設置所需報表的Name
						criteria.setJrxmlName(IAtomsConstants.EDC_FEE_REPORT_FOR_GP_ENABLE_ASSET_INFO);
						//設置報表路徑
						criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
						//設置匯出格式
						criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
						criteria.setSheetName(i18NUtil.getName(IAtomsConstants.EDC_FEE_REPORT_FOR_GP_ENABLE_ASSET_INFO)
								.concat(IAtomsConstants.MARK_MIDDLE_LINE).concat(dmmRepositoryDTO.getName()));
						criterias.add(criteria);
						dtos = new ArrayList<DmmRepositoryDTO>();
					}
				} else {
					criteria = new JasperReportCriteriaDTO();
					criteria.setAutoBuildJasper(false);
					criteria.setResult(null);
					//設置所需報表的Name
					criteria.setJrxmlName(IAtomsConstants.EDC_FEE_REPORT_FOR_GP_ENABLE_ASSET_INFO);
					//設置報表路徑
					criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
					//設置匯出格式
					criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
					criteria.setSheetName(i18NUtil.getName(IAtomsConstants.EDC_FEE_REPORT_FOR_GP_ENABLE_ASSET_INFO));
					criterias.add(criteria);
				} 
					
				//作業明細sheet
				for (SrmCaseHandleInfoDTO dto : caseWorkDetailHandleInfoDTOs) {
					if (dto.getFeePrice() == 0) {
						dto.setFeePrice(null);
					}
					if (dto.getEcrLineInFee() == 0) {
						dto.setEcrLineInFee(null);
					}
					if (dto.getNetLineInFee() == 0) {
						dto.setNetLineInFee(null);
					}
					if (IAtomsConstants.YES.equals(dto.getPeripheralsUpdate())) {
						dto.setPeripheralsUpdate("V");
					} else if (IAtomsConstants.NO.equals(dto.getPeripheralsUpdate())){
						dto.setPeripheralsUpdate(null);
					}
				}
				criteria = new JasperReportCriteriaDTO();
				criteria.setAutoBuildJasper(false);
				criteria.setResult(caseWorkDetailHandleInfoDTOs);
				//設置所需報表的Name
				criteria.setJrxmlName(IAtomsConstants.EDC_FEE_REPORT_FOR_GP_WORK_DETAIL);
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				criteria.setSheetName(i18NUtil.getName(IAtomsConstants.EDC_FEE_REPORT_FOR_GP_WORK_DETAIL));
				criterias.add(criteria);
				
				//報廢機明細sheet
				criteria = new JasperReportCriteriaDTO();
				criteria.setAutoBuildJasper(false);
				criteria.setResult(scrapAssets);
				//設置所需報表的Name
				criteria.setJrxmlName(IAtomsConstants.EDC_FEE_REPORT_FOR_GP_SCRAP_ASSET);
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				criteria.setSheetName(i18NUtil.getName(IAtomsConstants.EDC_FEE_REPORT_FOR_GP_SCRAP_ASSET));
				criterias.add(criteria);
				
				//維修耗材費用-AO已回覆扣款方式sheet
				criteria = new JasperReportCriteriaDTO();
				criteria.setAutoBuildJasper(false);
				criteria.setResult(paymentInfoDTOs);
				//設置所需報表的Name
				criteria.setJrxmlName(IAtomsConstants.EDC_FEE_REPORT_FOR_GP_PAYMENT_INFO);
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				criteria.setSheetName(i18NUtil.getName(IAtomsConstants.EDC_FEE_REPORT_FOR_GP_PAYMENT_INFO_IS_REPLY));
				criterias.add(criteria);
				
				//維修耗材費用-AO未回覆扣款方式sheet
				criteria = new JasperReportCriteriaDTO();
				criteria.setAutoBuildJasper(false);
				criteria.setResult(paymentInfoDTOsNoTax);
				//設置所需報表的Name
				criteria.setJrxmlName(IAtomsConstants.EDC_FEE_REPORT_FOR_GP_PAYMENT_INFO);
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				//設置報表Name
				StringBuffer fileNameBuffer = new StringBuffer();
				fileNameBuffer.append(monthYear);
				fileNameBuffer.append(IAtomsConstants.MARK_UNDER_LINE);
				fileNameBuffer.append(reportSettingList.get(0).getCustomerName());
				fileNameBuffer.append(i18NUtil.getName(IAtomsConstants.EDC_FEE_REPORT_FOR_GP_NAME));
				String fileName = fileNameBuffer.toString();
				criteria.setReportFileName(fileName);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				criteria.setSheetName(i18NUtil.getName(IAtomsConstants.EDC_FEE_REPORT_FOR_GP_PAYMENT_INFO_IS_NOT_REPLY));
				criterias.add(criteria);
				
				//匯出報表至臨時目錄
				StringBuffer pathBuffer = new StringBuffer();
				pathBuffer.append(SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH)).append(File.separator).append(IAtomsConstants.UC_NO_AMM_06030);
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
				pathBuffer.append(fileName).append(JasperReportCriteriaDTO.REPORT_EXT_NAME_MSEXCEL);
				String[] attachments = new String[]{pathBuffer.toString()};
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
		} catch (Exception e) {
			LOGGER.error(".sendReportMail(SessionContext sessionContext):", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
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
	 * @return the calculatioNumber
	 */
	public List<Double> getCalculatioNumber() {
		return calculatioNumber;
	}

	/**
	 * @param calculatioNumber the calculatioNumber to set
	 */
	public void setCalculatioNumber(List<Double> calculatioNumber) {
		this.calculatioNumber = calculatioNumber;
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
	 * @return the srmCaseHandleInfoDAO
	 */
	public ISrmCaseHandleInfoDAO getCaseHandleInfoDAO() {
		return caseHandleInfoDAO;
	}

	/**
	 * @param srmCaseHandleInfoDAO the srmCaseHandleInfoDAO to set
	 */
	public void setCaseHandleInfoDAO(ISrmCaseHandleInfoDAO caseHandleInfoDAO) {
		this.caseHandleInfoDAO = caseHandleInfoDAO;
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
	 * @return the caseAssetLinkDAO
	 */
	public ISrmCaseAssetLinkDAO getCaseAssetLinkDAO() {
		return caseAssetLinkDAO;
	}

	/**
	 * @param caseAssetLinkDAO the caseAssetLinkDAO to set
	 */
	public void setCaseAssetLinkDAO(ISrmCaseAssetLinkDAO caseAssetLinkDAO) {
		this.caseAssetLinkDAO = caseAssetLinkDAO;
	}

	/**
	 * @return the suppliesTypes
	 */
	public List<String> getSuppliesTypes() {
		return suppliesTypes;
	}

	/**
	 * @param suppliesTypes the suppliesTypes to set
	 */
	public void setSuppliesTypes(List<String> suppliesTypes) {
		this.suppliesTypes = suppliesTypes;
	}

	/**
	 * @return the enableAssetStatus
	 */
	public List<String> getEnableAssetStatus() {
		return enableAssetStatus;
	}

	/**
	 * @param enableAssetStatus the enableAssetStatus to set
	 */
	public void setEnableAssetStatus(List<String> enableAssetStatus) {
		this.enableAssetStatus = enableAssetStatus;
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

	public List<String> getCommModeId() {
		return commModeId;
	}

	public void setCommModeId(List<String> commModeId) {
		this.commModeId = commModeId;
	}
	
	
}
