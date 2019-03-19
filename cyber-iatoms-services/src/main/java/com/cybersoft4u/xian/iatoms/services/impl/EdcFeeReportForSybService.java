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
import cafe.core.util.BeanUtils;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.MathUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.convert.SimpleDtoDmoTransformer;
import cafe.core.util.convert.Transformer;
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
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAssetLinkDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;
import com.cybersoft4u.xian.iatoms.common.reportsetting.WorkFeeSetting;
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
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BaseParameterItemDef;
import com.cybersoft4u.xian.iatoms.services.dmo.BaseParameterItemDefId;
/**
 * 
 * Purpose: 維護費報表(陽信格式)
 * @author Hermanwang
 * @since  JDK 1.6
 * @date   2017/9/22
 * @MaintenancePersonnel Hermanwang
 */
public class EdcFeeReportForSybService extends AtomicService implements IReportService,ApplicationContextAware {
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
	 * 系統參數維護DAO接口
	 */
	private IBaseParameterManagerDAO baseParameterManagerDAO;
	/**
	 * baseParameterTypeDAO借口
	 */
	private IIAtomsBaseParameterTypeDefDAO iAtomsBaseParameterTypeDefDAO;
	/**
	 * 合约DAO
	 */
	private IContractDAO contractDAO;
	/**
	 * 
	 */
	private List<String> caseCategoryList;
	private ApplicationContext applicationContext;
	/**
	 * 
	 * Constructor:空構造
	 */
	public EdcFeeReportForSybService() {
		super();
	}
	/**
	 * 
	 * Purpose:維護費報表(格式)的配製方法
	 * @author HermanWang
	 * @throws ServiceException
	 * @return void
	 */
	public void toReport() throws ServiceException {
		CompanyDTO companyDTO = this.companyDAO.getCompanyByCompanyCode(IAtomsConstants.PARAMTER_COMPANY_CODE_SYB);
		if (companyDTO != null) {
			sendReportMail(DateTimeUtils.getCurrentDate(), companyDTO.getCompanyId(), IAtomsConstants.REPORT_TCB_SYB_NINETEEN);
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
			//帳務月的上一月
			Calendar calLast=Calendar.getInstance();
			calLast.setTime(sendDate);
			calLast.add(Calendar.MONTH, -2); 
			calLast.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为帳務月上一月第一天 
			Timestamp lastDate = DateTimeUtils.toTimestamp(DateTimeUtils.toString(calLast.getTime(), DateTimeUtils.DT_FMT_YYYYMMDD_DASH));
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
				pathBuffer.append(path).append(File.separator).append(yearMonthDay).append(File.separator).append(IAtomsConstants.UC_NO_AMM_06070);
				pathBuffer.append(File.separator);
				//陽信	設備(第一個sql)
				List<DmmRepositoryDTO> DmmRepositoryDTOList =  this.dmmRepositoryHistoryMonthlyDAO.assetInfoListSyb(DateTimeUtils.toString(startDate, DateTimeUtils.DT_FMT_YYYYMM), customerId, startDate, endDate);
				//案件信息 作業明細(第2個sql)
				List<SrmCaseHandleInfoDTO> SrmCaseHandleInfoDTOList =  this.srmCaseHandleInfoDAO.getCaseListInSyb(caseStatusList, customerId, startDate, endDate);
				//案件通過設備鏈接檔單價分組的信息(第3個sql)
				List<SrmCaseAssetLinkDTO> srmCaseAssetLinkDTOList = this.srmCaseAssetLinkDAO.getCaseAssetLinkInSyb(caseStatusList, customerId, startDate, endDate);
				//(4).	計算耗材單價數量
				List<SrmCaseAssetLinkDTO> srmCaseLinkSuppliesDTOList = this.srmCaseAssetLinkDAO.getCaseLinkSuppliesInSyb(caseStatusList, customerId, startDate, endDate);
 				//(維護費)設備(第5個sql)
				List<DmmRepositoryDTO> DmmRepositoryDTOLists =  this.dmmRepositoryHistoryMonthlyDAO.feeAssetListSyb(DateTimeUtils.toString(startDate, DateTimeUtils.DT_FMT_YYYYMM), customerId);
				//求償維修耗材費用-AO已回覆扣款方式
				List<SrmPaymentInfoDTO> srmPaymentInfoDTOList = this.paymentInfoDAO.paymentInfoListToSyb(paymentTypeList, customerId);
				//維修耗材費用-AO未回覆扣款方式
				List<SrmPaymentInfoDTO> paymentInfoDTOListNoTax = this.paymentInfoDAO.paymentInfoListToSybNoTax(paymentStatusList, customerId);
				//匯出的結果result
				List<WorkFeeSetting> workFeeSettingList = new ArrayList<WorkFeeSetting>();
				//各設備剩餘台數
				List<BaseParameterItemDefDTO> sybRemainTimeDTOList = this.baseParameterManagerDAO.list(IATOMS_PARAM_TYPE.SYB_REMAIN_TIME.getCode(), null);
				Map<String, List<BaseParameterItemDefDTO>> sybRemainTimeMap = new HashMap<String, List<BaseParameterItemDefDTO>>();
				//List<Parameter> sybRemainTimeList = new ArrayList<Parameter>();
				//int maxOrder = 0;
				List<BaseParameterItemDefDTO> defDTOs = null;
				List<String> itemNames = new ArrayList<String>();
				//需要修改／新增的設備剩餘臺數
				List<BaseParameterItemDefDTO> addDefDTOs = new ArrayList<BaseParameterItemDefDTO>();
				for (BaseParameterItemDefDTO baseParameterItemDefDTO : sybRemainTimeDTOList) {
					if (!itemNames.contains(baseParameterItemDefDTO.getItemName())) {
						defDTOs = new ArrayList<BaseParameterItemDefDTO>();
						for (BaseParameterItemDefDTO dto : sybRemainTimeDTOList) {
							if (StringUtils.hasText(baseParameterItemDefDTO.getItemName())
									&& baseParameterItemDefDTO.getItemName().equals(dto.getItemName())) {
								defDTOs.add(dto);
								itemNames.add(dto.getItemName());
							}
						}
						sybRemainTimeMap.put(defDTOs.get(0).getItemName(), defDTOs);
					}
//					/*if(baseParameterItemDefDTO.getItemOrder() > maxOrder) {
//						maxOrder = baseParameterItemDefDTO.getItemOrder();
//					}*/
//					if(!baseParameterItemDefDTO.getItemValue().equals(IAtomsConstants.EDC_FEE_REPORT_PRICE_ZERO)) {
//						/*Parameter parameter = new Parameter();
//						parameter.setName(baseParameterItemDefDTO.getItemName());
//						parameter.setValue(baseParameterItemDefDTO.getItemValue());*/
//						//sybRemainTimeList.add(parameter);
//					}
				}
				//該客戶所對應的合約說明，顯示該客戶合約的說明欄位(有值)，若有多筆換行顯示
				String contractRemark = IAtomsConstants.MARK_EMPTY_STRING;
				//該客戶所有的合約list
				List<BimContractDTO> bimContractDTOList = this.contractDAO.listBy(customerId, null, null, null, null, -1, -1);
				for (BimContractDTO bimContractDTO : bimContractDTOList) {
					if(StringUtils.hasText(bimContractDTO.getComment())) {
						contractRemark = contractRemark + "合約" +bimContractDTO.getContractCode() + ":" +  bimContractDTO.getComment() + IAtomsConstants.RETURN_LINE_FEED;
					}
				}
				//裝機的總數量
				int installCount = 0;
				//拆機無須收費
				int unInstallCount = 0;
				//業務自拆的dtid
				String unstallDtid = IAtomsConstants.MARK_EMPTY_STRING;
				//到場的dtid和sn
				String arriveDtidAndSn = IAtomsConstants.MARK_EMPTY_STRING;
				//並機或者異動  處理方式=到場處理 的數量
				int updateOrMergeCount = 0;
				//並機或者異動 處理方式=軟排 的數量
				int updateSoftDispatch = 0;
				//急件
				int fast = 0;
				//特急件
				int extra = 0;
				//查核
				int checkCount = 0;
				//實際啟用量
				int actualEnableCount = 0;
				//訂單數量
				int OrderCount = 0;
				//報廢數量
				int ScrapCount = 0;
				//備品量
				Double readyCount = new Double(0);
				//查核
				//通訊模式支援 3G、GPRS、Bluetooth(3G/WIFI)、音源孔(3G/WIFI)】數量
				int commModeIdCount = 0;
				// 維護費報表map key是設備名稱  Value是 該設備名稱分組之後的 設備的dtolist
				Map<String, List<DmmRepositoryDTO>> dmmRepositoryListMap = new HashMap<String, List<DmmRepositoryDTO>>();
				//sn到場
				//裝/移機 (免費首次裝機)Map key 是裝機關聯的設備名稱，value是 關聯設備的案件數量
				Map<String, Integer> feeAssetMap = new HashMap<String, Integer>();
				//裝/移機 (免費首次裝機)Map key 是裝機關聯的設備名稱，value是 關聯設備的剩餘數量
				Map<String, Integer> remainFeeAssetMap = new HashMap<String, Integer>();
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
					//查核
					if(IAtomsConstants.CASE_CATEGORY.CHECK.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())) {
						checkCount++;
					//裝機數量
					} else if(IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())) {
						if(feeAssetMap.get(srmCaseHandleInfoDTO.getAssetName()) != null) {
							//數量 + 1
							feeAssetMap.put(srmCaseHandleInfoDTO.getAssetName(), feeAssetMap.get(srmCaseHandleInfoDTO.getAssetName()) + 1);
						} else {
							feeAssetMap.put(srmCaseHandleInfoDTO.getAssetName(), 1);
						}
						//配置裡面剩餘台數
						//Integer assetCount = getValueByName(sybRemainTimeList, srmCaseHandleInfoDTO.getAssetName());
						Integer assetCount = calculateFreeTimes(sybRemainTimeMap.get(srmCaseHandleInfoDTO.getAssetName()), startDate, addDefDTOs);
						if(feeAssetMap.get(srmCaseHandleInfoDTO.getAssetName()) > assetCount) {
							WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("INSTALLED");
							srmCaseHandleInfoDTO.setInstallPrice(workFeeDTO.getFirstPrice());
						}
						//拆機數量 
					} else if(IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())) {
						//【拆機類型=到場拆機、遺失報損】
						if("ARRIVE_UNINSTALL".equals(srmCaseHandleInfoDTO.getUninstallType())
								|| "LOSS_REPORT".equals(srmCaseHandleInfoDTO.getUninstallType())) {
							WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("UNINSTALLED");
							srmCaseHandleInfoDTO.setUnInstallPrice(workFeeDTO.getFirstPrice());
							unInstallCount ++;
						//【案件類別=拆機】且【拆機類型=業務自拆】的設備DTID,
						} else if("SERVICE_SELF_UNINSTALL".equals(srmCaseHandleInfoDTO.getUninstallType())) {
							if(StringUtils.hasText(unstallDtid)) {
								unstallDtid = unstallDtid + IAtomsConstants.MARK_SEPARATOR + "DTID:" + srmCaseHandleInfoDTO.getDtid() + "（SN:" + srmCaseHandleInfoDTO.getSerialNumber() + "）";
							} else {
								unstallDtid = "DTID:" + srmCaseHandleInfoDTO.getDtid() + "（SN:" + srmCaseHandleInfoDTO.getSerialNumber() + "）";
							}
						}
					//並機或者異動
					} else if(IAtomsConstants.CASE_CATEGORY.MERGE.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory()) 
							|| IAtomsConstants.CASE_CATEGORY.UPDATE.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())) {
						//且【處理方式=到場處理 且 是否同裝機作業=否
						if(("ARRIVE_PROCESS").equals(srmCaseHandleInfoDTO.getProcessType())) {
							if(IAtomsConstants.NO.equals(srmCaseHandleInfoDTO.getSameInstalled())) {
								updateOrMergeCount ++;
								WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("ARRIVED");
								srmCaseHandleInfoDTO.setSettingPrice(workFeeDTO.getFirstPrice());
							} else {
								if(StringUtils.hasText(arriveDtidAndSn)) {
									arriveDtidAndSn = arriveDtidAndSn + IAtomsConstants.MARK_SEPARATOR + IAtomsConstants.RETURN_LINE_FEED + "DTID:" + srmCaseHandleInfoDTO.getDtid() + "（SN:" + srmCaseHandleInfoDTO.getSerialNumber() + "）";
								} else {
									arriveDtidAndSn = "DTID:" + srmCaseHandleInfoDTO.getDtid() + "（SN:" + srmCaseHandleInfoDTO.getSerialNumber() + "）";
								}
							}
						//【處理方式 = 軟派】
						} else if(("SOFT_DISPATCH").equals(srmCaseHandleInfoDTO.getProcessType())) {
							updateSoftDispatch ++;
							WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("REMOTE");
							srmCaseHandleInfoDTO.setSettingPrice(workFeeDTO.getFirstPrice());
						}
					} 
					//案件類型=急件
					if("FAST".equals(srmCaseHandleInfoDTO.getCaseType())) {
						fast ++;
					//案件類型=特急件
					} else if("EXTRA".equals(srmCaseHandleInfoDTO.getCaseType())) {
						extra ++; 
					} 
					//若【案件類別=裝機、異動】使用之ECR線數量和單價，計算總價
					if(IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory()) 
							|| IAtomsConstants.CASE_CATEGORY.UPDATE.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())) {
						Integer ecrlineSum = 0;
						Integer networkRouteSum = 0;
						for (SrmCaseAssetLinkDTO srmCaseAssetLinkDTO : srmCaseAssetLinkDTOList) {
							if(srmCaseAssetLinkDTO.getCaseId().equals(srmCaseHandleInfoDTO.getCaseId())) {
								if(srmCaseAssetLinkDTO.getPrice() == null) {
									srmCaseAssetLinkDTO.setPrice(new BigDecimal(0));
								}
								if("ECRLINE".equals(srmCaseAssetLinkDTO.getItemCategory())) {
									ecrlineSum = ecrlineSum + srmCaseAssetLinkDTO.getPrice().intValue();
								} else {
									networkRouteSum = networkRouteSum + srmCaseAssetLinkDTO.getPrice().intValue();
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
					srmCaseHandleInfoDTO.setSameInstalled(i18NUtil.getName(srmCaseHandleInfoDTO.getSameInstalled()));
					srmCaseHandleInfoDTO.setCaseCategory(i18NUtil.getName(srmCaseHandleInfoDTO.getCaseCategory()));
				}
				//(維護費)設備
				for (DmmRepositoryDTO dmmRepositoryDTO : DmmRepositoryDTOLists) {
					if(dmmRepositoryDTO.getCheckedDate() != null) {
						//(維護費)設備中，【已啟用】
						if(IAtomsConstants.YES.equals(dmmRepositoryDTO.getIsEnabled())
								//且【(客戶驗收日 + 1年) <= 帳務年月月底】
								// Bug #3232 維護費報表啟用日期調整   (客戶驗收日 + 1年) > 帳務年月月底/ (帳務年月月底 - 1) < 客戶驗收日
							//	&& date.compareTo(dmmRepositoryDTO.getCheckedDate()) > 0
								&& date.compareTo(dmmRepositoryDTO.getCheckedDate()) > 0
								//且【狀態<>報廢、銷毀
								&& (!IAtomsConstants.PARAM_ASSET_STATUS_DISABLED.equals(dmmRepositoryDTO.getStatus()) 
										&& !IAtomsConstants.PARAM_ASSET_STATUS_DESTROY.equals(dmmRepositoryDTO.getStatus()))) {
							actualEnableCount++;
							WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("MAINTENANCE_FEE");
							// 第三個sheet中的 維護費(含稅)
							dmmRepositoryDTO.setMaintenanceTax(workFeeDTO.getFirstPrice());
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
				//本月各設備超過免收費台數加總數量
				Integer sybRemainTimeCount = 0;
				//裝機台數以及剩餘台數說明
				String installDescription = IAtomsConstants.MARK_EMPTY_STRING;
				for(Entry<String, Integer> assetEntry : feeAssetMap.entrySet()){
					//配置裡面剩餘台數
					Integer assetCount = calculateFreeTimes(sybRemainTimeMap.get(assetEntry.getKey()), startDate, addDefDTOs);
					//本月剩餘台數
					Integer remainCount = 0;
					//如果剩餘台數大於裝機的台數，則此設備本月不收費
					if(assetCount > assetEntry.getValue()) {
						//剩餘台數為 原剩餘台數減去 使用的台數
						remainCount = assetCount - assetEntry.getValue();
						remainFeeAssetMap.put(assetEntry.getKey(), remainCount);
					} else {
						sybRemainTimeCount = sybRemainTimeCount + (assetEntry.getValue() - assetCount);
						//剩餘台數小於使用的台式，則為0
						remainFeeAssetMap.put(assetEntry.getKey(), remainCount);
					}
					String lastMonth = IAtomsConstants.MARK_EMPTY_STRING;
					if(lastDate.toString().substring(5, 6).equals(IAtomsConstants.EDC_FEE_REPORT_PRICE_ZERO)) {
						lastMonth = lastDate.toString().substring(6, 7);
					} else {
						lastMonth = lastDate.toString().substring(5, 7);
					}
					String currMonth = IAtomsConstants.MARK_EMPTY_STRING;
					if(startDate.toString().substring(5, 6).equals(IAtomsConstants.EDC_FEE_REPORT_PRICE_ZERO)) {
						currMonth = startDate.toString().substring(6, 7);
					} else {
						currMonth = startDate.toString().substring(5, 7);
					}
					installDescription = installDescription + "本月" + assetEntry.getKey() + "裝機共" + assetEntry.getValue() + "台，" +
							lastMonth + "月尚餘" + assetCount + "台，" + 
							"總計" + currMonth + "月尚餘" + remainCount + "台" + IAtomsConstants.RETURN_LINE_FEED;
				}
				//getValueByName
				List<DmmRepositoryDTO> dmmRepositoryDTOList = null;
				//循環設備明細
				//(設備明細)中【狀態=使用中】且【通訊模式支援 3G、GPRS、Bluetooth(3G/WIFI)、音源孔(3G/WIFI)】數量
				for(DmmRepositoryDTO dmmRepositoryDTO : DmmRepositoryDTOList) {
					//狀態等於使用中
					boolean is3G = false;
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
								//若設備通訊模式含 3G、GPRS、
								//Bluetooth(3G/WIFI)、音源孔(3G/WIFI)，且狀態為使用中，則從作業別單價配置表
								is3G = true;
								WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("3G_FEE");
								dmmRepositoryDTO.setThreeGPrice(workFeeDTO.getFirstPrice());
								commModeIdCount ++;
								break;
							}
						}
					}
					dmmRepositoryDTO.setIs3G(is3G);
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
				//第一行 裝/移機  (免費首次裝機) 
				WorkFeeSetting workFeeDTOInstalled = new WorkFeeSetting();
				WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("INSTALLED");
				workFeeDTOInstalled.setName(workFeeDTO.getName());
				workFeeDTOInstalled.setFirstCount(String.valueOf(sybRemainTimeCount));
				workFeeDTOInstalled.setFirstPrice(workFeeDTO.getFirstPrice());
				workFeeDTOInstalled.setSum(Integer.parseInt(workFeeDTOInstalled.getFirstCount()) * workFeeDTO.getFirstPrice());
				workFeeDTOInstalled.setDescription(contractRemark + installDescription);
				workFeeSettingList.add(workFeeDTOInstalled);
				//第二行 二次作業(第一次到場無法作業 需二次到場)
				WorkFeeSetting workFeeDTOSecond = new WorkFeeSetting();
				workFeeDTO = getworkFeeNameByWorkCategory("SECOND");
				workFeeDTOSecond.setName(workFeeDTO.getName());
				workFeeDTOSecond.setFirstCount(String.valueOf(0));
				workFeeDTOSecond.setFirstPrice(workFeeDTO.getFirstPrice());
				workFeeSettingList.add(workFeeDTOSecond);
				//第三行 拆機
				WorkFeeSetting workFeeDTOUninstal = new WorkFeeSetting();
				workFeeDTO = getworkFeeNameByWorkCategory("UNINSTALLED");
				workFeeDTOUninstal.setName(workFeeDTO.getName());
				workFeeDTOUninstal.setFirstCount(String.valueOf(unInstallCount));
				workFeeDTOUninstal.setFirstPrice(workFeeDTO.getFirstPrice());
				workFeeDTOUninstal.setSum(Integer.parseInt(workFeeDTOUninstal.getFirstCount()) * workFeeDTO.getFirstPrice());
				if(StringUtils.hasText(unstallDtid)) {
					workFeeDTOUninstal.setDescription(unstallDtid + "業務自拆，故免收取拆機費用");
				}
				workFeeSettingList.add(workFeeDTOUninstal);
				//第四行 參數設定(到場)
				WorkFeeSetting workFeeDTOArrive = new WorkFeeSetting();
				workFeeDTO = getworkFeeNameByWorkCategory("ARRIVED");
				workFeeDTOArrive.setName(workFeeDTO.getName());
				workFeeDTOArrive.setFirstCount(String.valueOf(updateOrMergeCount));
				workFeeDTOArrive.setFirstPrice(workFeeDTO.getFirstPrice());
				workFeeDTOArrive.setSum(Integer.parseInt(workFeeDTOArrive.getFirstCount()) * workFeeDTO.getFirstPrice());
				if(StringUtils.hasText(arriveDtidAndSn)) {
					workFeeDTOArrive.setDescription(arriveDtidAndSn + "同裝機作業，故免收取異動到場費用");
				}
				workFeeSettingList.add(workFeeDTOArrive);
				//第五行 參數設定(遠端軟派)
				WorkFeeSetting workFeeDTOSoftDispatch = new WorkFeeSetting();
				workFeeDTO = getworkFeeNameByWorkCategory("REMOTE");
				workFeeDTOSoftDispatch.setName(workFeeDTO.getName());
				workFeeDTOSoftDispatch.setFirstCount(String.valueOf(updateSoftDispatch));
				workFeeDTOSoftDispatch.setFirstPrice(workFeeDTO.getFirstPrice());
				workFeeDTOSoftDispatch.setSum(Integer.parseInt(workFeeDTOSoftDispatch.getFirstCount()) * workFeeDTO.getFirstPrice());
				workFeeSettingList.add(workFeeDTOSoftDispatch);
				//第六行 3G刷卡機通訊費
				WorkFeeSetting workFeeDTO3GFee = new WorkFeeSetting();
				workFeeDTO = getworkFeeNameByWorkCategory("3G_FEE");
				workFeeDTO3GFee.setName(workFeeDTO.getName());
				workFeeDTO3GFee.setFirstCount(String.valueOf(commModeIdCount));
				workFeeDTO3GFee.setFirstPrice(workFeeDTO.getFirstPrice());
				workFeeDTO3GFee.setSum(Integer.parseInt(workFeeDTO3GFee.getFirstCount()) * workFeeDTO.getFirstPrice());
				workFeeSettingList.add(workFeeDTO3GFee);
				//第7行--線材使用-ECR線材
				//使用之案件單價不同，拆多筆，列出不同台數、單價
				//第8行--線材使用-網路線
				//使用之案件單價不同，拆多筆，列出不同台數、單價
				boolean haveEcrLine = false;
				boolean haveNetLine = false;
				for (SrmCaseAssetLinkDTO srmCaseAssetLinkDTO : srmCaseLinkSuppliesDTOList) {
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
					} else if("NetworkRoute".equals(srmCaseAssetLinkDTO.getItemCategory())) {
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
				//第九行 特急件
				WorkFeeSetting workFeeDTOExtra = new WorkFeeSetting();
				workFeeDTO = getworkFeeNameByWorkCategory("EXTRA");
				workFeeDTOExtra.setName(workFeeDTO.getName());
				workFeeDTOExtra.setFirstCount(String.valueOf(extra));
				workFeeDTOExtra.setFirstPrice(workFeeDTO.getFirstPrice());
				workFeeDTOExtra.setSum(Integer.parseInt(workFeeDTOExtra.getFirstCount()) * workFeeDTO.getFirstPrice());
				workFeeSettingList.add(workFeeDTOExtra);
				//第十行 急件加收
				WorkFeeSetting workFeeDTOFast = new WorkFeeSetting();
				workFeeDTO = getworkFeeNameByWorkCategory("FAST");
				workFeeDTOFast.setName(workFeeDTO.getName());
				workFeeDTOFast.setFirstCount(String.valueOf(extra));
				workFeeDTOFast.setFirstPrice(workFeeDTO.getFirstPrice());
				workFeeDTOFast.setSum(Integer.parseInt(workFeeDTOFast.getFirstCount()) * workFeeDTO.getFirstPrice());
				workFeeSettingList.add(workFeeDTOFast);
				//第11行 維修耗材費用
				WorkFeeSetting workFeeDTOSuppliesPrice = new WorkFeeSetting();
				workFeeDTOSuppliesPrice.setName("維修耗材費用");
				workFeeSettingList.add(workFeeDTOSuppliesPrice);
				//第12行 APN月租費
				WorkFeeSetting workFeeDTOApn = new WorkFeeSetting();
				workFeeDTOApn.setName("APN月租費");
				workFeeDTOApn.setSum(1200);
				workFeeDTOApn.setDescription("每月固定收費：申請中華電信MDVPN，SIM卡必須加入APN連結後端的VPN。");
				workFeeSettingList.add(workFeeDTOApn);
				//第13行 VPN存取控制月租費
				WorkFeeSetting workFeeDTOVpn = new WorkFeeSetting();
				workFeeDTOVpn.setName("VPN存取控制月租費");
				workFeeDTOVpn.setSum(2300);
				workFeeDTOVpn.setDescription("每月固定收費：前述APN必須同時連結經貿聯網VPN(No.2765) 執行TMS程式參數下載及連結陽信VPN執行刷卡交易，故須設定此存取控制。");
				workFeeSettingList.add(workFeeDTOVpn);
				//第十四行 EDC維護費用(第2年始收費)
				WorkFeeSetting workFeeDTOMaintenanceFee = new WorkFeeSetting();
				workFeeDTO = getworkFeeNameByWorkCategory("MAINTENANCE_FEE");
				workFeeDTOMaintenanceFee.setFirstPrice(workFeeDTO.getFirstPrice());
				workFeeDTOMaintenanceFee.setName(workFeeDTO.getName());
				//實際啟用量+備品量 >訂單數量-報廢量 
				if((actualEnableCount+readyCount) > (OrderCount-ScrapCount)) {
					workFeeDTOMaintenanceFee.setFirstCount(String.valueOf(OrderCount-ScrapCount));
					workFeeDTOMaintenanceFee.setSum((Integer.parseInt(workFeeDTOMaintenanceFee.getFirstCount()) * workFeeDTO.getFirstPrice()));
					workFeeDTOMaintenanceFee.setDescription("實際啟用量+備品量(即”(訂單數量-報廢量)*5%”)超過訂單數量-報廢量，故月維護費計算方式以訂單數量-報廢量：(" + OrderCount 
							+ "-" + ScrapCount + ")*" + workFeeDTO.getFirstPrice() +"元=" + workFeeDTOMaintenanceFee.getSum());
				} else {
					BigDecimal readyCountBig = new BigDecimal(String.valueOf(readyCount));
			        double readyCountDou = readyCountBig.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
					workFeeDTOMaintenanceFee.setFirstCount(String.valueOf((int)(actualEnableCount + readyCountDou)));
					Double firstCount = Double.parseDouble(workFeeDTOMaintenanceFee.getFirstCount());
					workFeeDTOMaintenanceFee.setSum((firstCount.intValue() * workFeeDTO.getFirstPrice()));
					workFeeDTOMaintenanceFee.setDescription("訂單數量-報廢量超過實際啟用量+備品量(即”(訂單數量-報廢量)*5%”)，故月維護費計算方式以實際啟用量+備品量：(" + actualEnableCount 
							+ "+" + (int)readyCountDou + ")*" + workFeeDTO.getFirstPrice() +"元=" + workFeeDTOMaintenanceFee.getSum());
				}
				workFeeSettingList.add(workFeeDTOMaintenanceFee);
				//第一個sheet
				criteria = new JasperReportCriteriaDTO();
				criteria.setAutoBuildJasper(false);
				criteria.setResult(workFeeSettingList);
				//設置所需報表的Name
				criteria.setJrxmlName(IAtomsConstants.SYB_PRICE_SUM_REPORT);
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
				countMap.put("yyyyMM", DateTimeUtils.toString(startDate, DateTimeUtils.DT_FMT_YYYYMM));
				if(startDate.toString().substring(5, 6).equals(IAtomsConstants.EDC_FEE_REPORT_PRICE_ZERO)) {
					String chMonth = this.getMonth(startDate.toString().substring(6, 7));
					countMap.put("month", chMonth);
				} else {
					String chMonth = this.getMonth(startDate.toString().substring(5, 7));
					countMap.put("month", chMonth);
				}
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
				//第二 三個sheet 設備明細
				for(Entry<String, List<DmmRepositoryDTO>> assetEntry : dmmRepositoryListMap.entrySet()){
					List<DmmRepositoryDTO> DmmRepositoryResult = assetEntry.getValue();
					Integer rowCount = 1;
					if(DmmRepositoryResult.size() > 0) {
						boolean is3G = false;
						for (DmmRepositoryDTO dmmRepositoryDTO : DmmRepositoryResult) {
							if(dmmRepositoryDTO.getIs3G()) {
								is3G = true;
								break;
							}
						}
						criteria = new JasperReportCriteriaDTO();
						criteria.setAutoBuildJasper(false);
						for (DmmRepositoryDTO dmmRepositoryDTO : DmmRepositoryResult) {
							dmmRepositoryDTO.setRowId(String.valueOf(rowCount));
							rowCount ++;
						}
						criteria.setResult(DmmRepositoryResult);
						if(is3G) {
							//設置所需報表的Name
							criteria.setJrxmlName(IAtomsConstants.SYB_ASSET_INFO);
						} else {
							//設置所需報表的Name
							criteria.setJrxmlName(IAtomsConstants.SYB_NO_3G_ASSET_INFO);
						}
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
				//第四個sheet
				if(SrmCaseHandleInfoDTOList.size()== 0) {
					SrmCaseHandleInfoDTO SrmCaseHandleInfoDTO = new SrmCaseHandleInfoDTO();
					SrmCaseHandleInfoDTOList.add(SrmCaseHandleInfoDTO);
				}
				criteria = new JasperReportCriteriaDTO();
				criteria.setAutoBuildJasper(false);
				criteria.setResult(SrmCaseHandleInfoDTOList);
				//設置所需報表的Name
				criteria.setJrxmlName(IAtomsConstants.SYB_CASE_INFO_REPORT);
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				//設置報表Name
				//設置報表Name
				if(startDate.toString().substring(5, 6).equals(IAtomsConstants.EDC_FEE_REPORT_PRICE_ZERO)) {
					criteria.setSheetName(startDate.toString().substring(6, 7) + "月作業明細");
				} else {
					criteria.setSheetName(startDate.toString().substring(5, 7) + "月作業明細");
				}
				criteria.setReportFileName(fileName);
				criterias.add(criteria);
				//第五個 sheet
				if(DmmRepositoryDTOLists.size() == 0) {
					DmmRepositoryDTO DmmRepositoryDTO = new DmmRepositoryDTO();
					DmmRepositoryDTOLists.add(DmmRepositoryDTO);
					
				}
				criteria = new JasperReportCriteriaDTO();
				criteria.setAutoBuildJasper(false);
				criteria.setResult(DmmRepositoryDTOLists);
				//設置所需報表的Name
				criteria.setJrxmlName(IAtomsConstants.SYB_ASSET_MAINTAIN);
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
					SrmPaymentInfoDTO SrmPaymentInfoDTO = new SrmPaymentInfoDTO();
					srmPaymentInfoDTOList.add(SrmPaymentInfoDTO);
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
					SrmPaymentInfoDTO SrmPaymentInfoDTO = new SrmPaymentInfoDTO();
					paymentInfoDTOListNoTax.add(SrmPaymentInfoDTO);
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
				//上月有設備剩餘
					//更新基礎參數表的配置台數
					BaseParameterItemDef baseParameterItemDef = null;
					//查詢type表是否有記錄,重送和批次只能執行一次
						HibernateTransactionManager transactionManager = (HibernateTransactionManager) this.applicationContext.getBean("transactionManager");
						DefaultTransactionDefinition def = new DefaultTransactionDefinition();
						def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务，这样会比较安全些。
						TransactionStatus status = transactionManager.getTransaction(def); // 获得事务状态
						try {
							itemNames = new ArrayList<String>();
							Transformer transformer = new SimpleDtoDmoTransformer();
							for (BaseParameterItemDefDTO baseParameterItemDefDTO : addDefDTOs) {
								if (!itemNames.contains(baseParameterItemDefDTO.getItemName())) {
									baseParameterItemDef = (BaseParameterItemDef) transformer.transform(baseParameterItemDefDTO, new BaseParameterItemDef());
									if (!StringUtils.hasText(baseParameterItemDefDTO.getBpidId())) {
										baseParameterItemDef.setItemValue(remainFeeAssetMap.get(baseParameterItemDefDTO.getItemName()).toString());
										baseParameterItemDef.setItemOrder(2);
								//		baseParameterItemDef.setApprovedFlag(IAtomsConstants.YES);
										baseParameterItemDef.setItemDesc("");
										baseParameterItemDef.setParentBpidId("");
										baseParameterItemDef.setDeleted(IAtomsConstants.NO);
										baseParameterItemDefDTO.setBpidId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BASE_PARAMETER_ITEM_DEF));
									}
									baseParameterItemDef.setId(
											new BaseParameterItemDefId(baseParameterItemDefDTO.getBpidId(), 
													IATOMS_PARAM_TYPE.SYB_REMAIN_TIME.getCode(), baseParameterItemDefDTO.getEffectiveDate()));
									baseParameterItemDef.setTextField1(DateTimeUtils.toString(startDate, DateTimeUtils.DT_FMT_YYYYMMDD_SLASH).substring(0, 7));
									if(remainFeeAssetMap.get(baseParameterItemDefDTO.getItemName()) != null) {
										baseParameterItemDef.setItemValue(remainFeeAssetMap.get(baseParameterItemDefDTO.getItemName()).toString());
									} else {
										baseParameterItemDef.setItemValue(IAtomsConstants.EDC_FEE_REPORT_PRICE_ZERO);
									}
									itemNames.add(baseParameterItemDefDTO.getItemName());
									this.baseParameterManagerDAO.getDaoSupport().saveOrUpdate(baseParameterItemDef);
								}
							}
							if (!CollectionUtils.isEmpty(sybRemainTimeMap)) {
								for (String itemName : itemNames) {
									if (sybRemainTimeMap.containsKey(itemName)) {
										sybRemainTimeMap.remove(itemName);
									}
								}
								BaseParameterItemDefDTO baseParameterItemDefDTO = null;
								for (String itemName : sybRemainTimeMap.keySet()) {
									if (sybRemainTimeMap.get(itemName).size() == 1) {
										baseParameterItemDefDTO = sybRemainTimeMap.get(itemName).get(0);
										baseParameterItemDef = (BaseParameterItemDef) transformer.transform(baseParameterItemDefDTO, new BaseParameterItemDef());
										baseParameterItemDef.setItemOrder(2);
										baseParameterItemDef.setBpidId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BASE_PARAMETER_ITEM_DEF));
										baseParameterItemDef.setEffectiveDate(baseParameterItemDefDTO.getEffectiveDate());
										baseParameterItemDef.setItemValue(baseParameterItemDefDTO.getItemValue());
									} else {
										String firstDate = sybRemainTimeMap.get(itemName).get(0).getTextField1();
										String otherDate = sybRemainTimeMap.get(itemName).get(1).getTextField1();
										firstDate += "/01";
										otherDate += "/01";
										if (!StringUtils.hasText(firstDate) || !StringUtils.hasText(otherDate)) {
											continue;
										}
										if (!ValidateUtils.checkDate(firstDate) || !ValidateUtils.checkDate(otherDate)) {
											continue;
										}
										if (DateTimeUtils.addCalendar(startDate, 0, -1, 0).getTime() != DateTimeUtils.toDate(firstDate).getTime()
												&& DateTimeUtils.addCalendar(startDate, 0, -1, 0).getTime() != DateTimeUtils.toDate(otherDate).getTime()) {
											continue;
										}
										if (DateTimeUtils.addCalendar(DateTimeUtils.toDate(otherDate), 0, 1, 0).getTime() != DateTimeUtils.toDate(firstDate).getTime()
												&& DateTimeUtils.addCalendar(DateTimeUtils.toDate(firstDate), 0, 1, 0).getTime() != DateTimeUtils.toDate(otherDate).getTime()) {
											continue;
										}
										if (DateTimeUtils.toDate(firstDate).getTime() == startDate.getTime()) {
											baseParameterItemDef = (BaseParameterItemDef) transformer.transform(sybRemainTimeMap.get(itemName).get(0), new BaseParameterItemDef());
										} else if (DateTimeUtils.toDate(otherDate).getTime() == startDate.getTime()) {
											baseParameterItemDef = (BaseParameterItemDef) transformer.transform(sybRemainTimeMap.get(itemName).get(1), new BaseParameterItemDef());
										} else if (DateTimeUtils.addCalendar(DateTimeUtils.toDate(firstDate), 0, 1, 0).getTime() == startDate.getTime()) {
											baseParameterItemDef = (BaseParameterItemDef) transformer.transform(sybRemainTimeMap.get(itemName).get(1), new BaseParameterItemDef());
										} else if (DateTimeUtils.addCalendar(DateTimeUtils.toDate(otherDate), 0, 1, 0).getTime() == startDate.getTime()){
											baseParameterItemDef = (BaseParameterItemDef) transformer.transform(sybRemainTimeMap.get(itemName).get(0), new BaseParameterItemDef());
										}
									}
									baseParameterItemDef.setId(
											new BaseParameterItemDefId(baseParameterItemDef.getBpidId(), 
													IATOMS_PARAM_TYPE.SYB_REMAIN_TIME.getCode(), baseParameterItemDef.getEffectiveDate()));
									baseParameterItemDef.setTextField1(DateTimeUtils.toString(startDate, DateTimeUtils.DT_FMT_YYYYMMDD_SLASH).substring(0, 7));
									this.baseParameterManagerDAO.getDaoSupport().saveOrUpdate(baseParameterItemDef);
								}
							}
							transactionManager.commit(status);
						} catch (Exception e) {
							LOGGER.error(".sendReportMail(SessionContext sessionContext):", e);
							transactionManager.rollback(status);
							throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
						}
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
	private String getMonth(String enMonth){
		String chMonth = IAtomsConstants.MARK_EMPTY_STRING;
		if(IAtomsConstants.PARAM_MONTH_JANUARY.equals(enMonth)) {
			chMonth = "一";
		} else if(IAtomsConstants.PARAM_MONTH_FEBRUARY.equals(enMonth)) {
			chMonth = "二";
		} else if(IAtomsConstants.PARAM_MONTH_MARCH.equals(enMonth)) {
			chMonth = "三";
		} else if(IAtomsConstants.PARAM_MONTH_APRIL.equals(enMonth)) {
			chMonth = "四";
		} else if(IAtomsConstants.PARAM_MONTH_MAY.equals(enMonth)) {
			chMonth = "五";
		} else if(IAtomsConstants.PARAM_MONTH_JUNE.equals(enMonth)) {
			chMonth = "六";
		} else if(IAtomsConstants.PARAM_MONTH_JULY.equals(enMonth)) {
			chMonth = "七";
		} else if(IAtomsConstants.PARAM_MONTH_AUGUST.equals(enMonth)) {
			chMonth = "八";
		} else if(IAtomsConstants.PARAM_MONTH_SEPTEMBER.equals(enMonth)) {
			chMonth = "九";
		} else if(IAtomsConstants.PARAM_MONTH_OCTOBER.equals(enMonth)) {
			chMonth = "十";
		} else if(IAtomsConstants.PARAM_MONTH_NOVEMBER.equals(enMonth)) {
			chMonth = "十一";
		} else if(IAtomsConstants.PARAM_MONTH_DECEMBER.equals(enMonth)) {
			chMonth = "十二";
		}
		return chMonth;
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
        					result = DateTimeUtils.toString((Date)invokeMethod, DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_SLASH);
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
	 * Purpose:根據name從基礎參數表拿到value
	 * @author HermanWang
	 * @param parameters：下拉框列表
	 * @param name 傳入的name
	 * @return 得到的value
	 */
	/*private Integer getValueByName(List<Parameter> parameterList, String name){
		String value = IAtomsConstants.MARK_EMPTY_STRING;
		// 错误信息
		for (Parameter param : parameterList){
			if((param.getName()).equals(name)){
				value = (String) param.getValue();
				break;
			}
		}
		if(StringUtils.hasText(value)) {
			return Integer.parseInt(value);
		} else {
			return 0;
		}
	}*/
	public Integer calculateFreeTimes(List<BaseParameterItemDefDTO> baseParameterItemDefDTOs, Timestamp startDate, List<BaseParameterItemDefDTO> addDefDTOs) {
		Integer oldFreeTimes = 0;
		try {
			BaseParameterItemDefDTO baseParameterItemDefDTO = null;
			BaseParameterItemDefDTO newItemDefDTO = null;
			if (!CollectionUtils.isEmpty(baseParameterItemDefDTOs)) {
				if (baseParameterItemDefDTOs.size() == 1) {
					baseParameterItemDefDTO = baseParameterItemDefDTOs.get(0);
					Timestamp tempDate = DateTimeUtils.toTimestamp(baseParameterItemDefDTO.getTextField1() + "/01");
					if (DateTimeUtils.addCalendar(tempDate, 0, 1, 0).getTime() != startDate.getTime()) {
						return 0;
					}
					newItemDefDTO = new BaseParameterItemDefDTO();
					newItemDefDTO.setItemName(baseParameterItemDefDTO.getItemName());
					newItemDefDTO.setEffectiveDate(baseParameterItemDefDTO.getEffectiveDate());
					newItemDefDTO.setItemOrder(2);
				//	newItemDefDTO.setApprovedFlag(IAtomsConstants.YES);
					newItemDefDTO.setItemDesc("");
					newItemDefDTO.setParentBpidId("");
					newItemDefDTO.setDeleted(IAtomsConstants.NO);
					
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
				addDefDTOs.add(newItemDefDTO);
				oldFreeTimes = Integer.valueOf(baseParameterItemDefDTO.getItemValue());
			}
		} catch (Exception e) {
			LOGGER.error(".calculateFreeTimes(SessionContext sessionContext):", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return oldFreeTimes;
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
	 * @return the baseParameterManagerDAO
	 */
	public IBaseParameterManagerDAO getBaseParameterManagerDAO() {
		return baseParameterManagerDAO;
	}
	/**
	 * @param baseParameterManagerDAO the baseParameterManagerDAO to set
	 */
	public void setBaseParameterManagerDAO(IBaseParameterManagerDAO baseParameterManagerDAO) {
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
	public void setContractDAO(IContractDAO contractDAO) {
		this.contractDAO = contractDAO;
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
	/**
	 * @return the iAtomsBaseParameterTypeDefDAO
	 */
	public IIAtomsBaseParameterTypeDefDAO getiAtomsBaseParameterTypeDefDAO() {
		return iAtomsBaseParameterTypeDefDAO;
	}
	/**
	 * @param iAtomsBaseParameterTypeDefDAO the iAtomsBaseParameterTypeDefDAO to set
	 */
	public void setiAtomsBaseParameterTypeDefDAO(IIAtomsBaseParameterTypeDefDAO iAtomsBaseParameterTypeDefDAO) {
		this.iAtomsBaseParameterTypeDefDAO = iAtomsBaseParameterTypeDefDAO;
	}

}