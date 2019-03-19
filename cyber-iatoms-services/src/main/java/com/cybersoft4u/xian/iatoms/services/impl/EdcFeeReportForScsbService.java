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
 * 
 * Purpose: 維護費報表(上銀格式)
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2017/9/19
 * @MaintenancePersonnel HermanWang
 */
public class EdcFeeReportForScsbService extends AtomicService implements IReportService {
	/**
	 * 記錄日誌
	 */
	private static final CafeLog LOGGER	= CafeLogFactory.getLog(EdcFeeReportForScsbService.class);
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
	 * Constructor: 無參構造
	 */
	public EdcFeeReportForScsbService() {
		super();
	}
	/**
	 * 
	 * Purpose:維護費報表(上銀格式)的配製方法
	 * @author HermanWang
	 * @throws ServiceException
	 * @return void
	 */
	public void toReport() throws ServiceException {
		CompanyDTO companyDTO = this.companyDAO.getCompanyByCompanyCode(IAtomsConstants.PARAMTER_COMPANY_CODE_SCSB);
		if (companyDTO != null) {
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
				pathBuffer.append(path).append(File.separator).append(yearMonthDay).append(File.separator).append(IAtomsConstants.UC_NO_AMM_06060);
				pathBuffer.append(File.separator);
				//上銀 設備(第一個sql)
				List<DmmRepositoryDTO> DmmRepositoryDTOList =  this.dmmRepositoryHistoryMonthlyDAO.assetInfoListScsb(DateTimeUtils.toString(startDate, DateTimeUtils.DT_FMT_YYYYMM), customerId, startDate, endDate);
				//案件信息 作業明細(第2個sql)
				List<SrmCaseHandleInfoDTO> SrmCaseHandleInfoDTOList =  this.srmCaseHandleInfoDAO.getCaseListInScsb(caseStatusList, customerId, startDate, endDate, commonTransactionTypeList);
				//案件通過設備鏈接檔單價分組的信息(第3個sql) ecr 
				List<SrmCaseAssetLinkDTO> srmCaseAssetLinkDTOList = this.srmCaseAssetLinkDAO.getCaseAssetLinkInScsb(caseStatusList, customerId, startDate, endDate);
				//(4).	計算耗材單價數量
				List<SrmCaseAssetLinkDTO> srmCaseLinkSuppliesDTOList = this.srmCaseAssetLinkDAO.getCaseLinkSuppliesInScsb(caseStatusList, customerId, startDate, endDate);
 				//(維護費)設備(第5個sql)
				List<DmmRepositoryDTO> DmmRepositoryDTOLists =  this.dmmRepositoryHistoryMonthlyDAO.feeAssetListScsb(repoStatusList, DateTimeUtils.toString(startDate, DateTimeUtils.DT_FMT_YYYYMM), customerId);
				//求償維修耗材費用-AO已回覆扣款方式
				List<SrmPaymentInfoDTO> srmPaymentInfoDTOList = this.paymentInfoDAO.paymentInfoListToScsb(paymentTypeList, customerId);
				//維修耗材費用-AO未回覆扣款方式
				List<SrmPaymentInfoDTO> paymentInfoDTOListNoTax = this.paymentInfoDAO.paymentInfoListToScsbNoTax(paymentStatusList, customerId);
				//匯出的結果result子報表1
				List<WorkFeeSetting> workFeeSettingList = new ArrayList<WorkFeeSetting>();
				//匯出的結果result子報表2
				List<WorkFeeSetting> workFeeSettingFeeList = new ArrayList<WorkFeeSetting>();
				//裝機的總數量
				int installCount = 0;
				//拆機無須收費
				int unInstallFeeCount = 0;
				int unInstallCount = 0;
				//並機或者異動  處理方式=到場處理 的數量
				int updateOrMergeCount = 0;
				int updateNotSameInstallCount = 0;
				//並機或者異動 處理方式=軟排 的數量
				int updateSoftDispatch = 0;
				//急件
				int fast = 0;
				//特急件
				int extra = 0;
				//查核
				int checkCount = 0;
				//查核
				//【(拆機日- 裝機日)未滿三個月(90天)拆機】數量
				int userdDays90Count = 0;
				//通訊模式支援 3G、GPRS、Bluetooth(3G/WIFI)、音源孔(3G/WIFI)】數量
				int commModeIdCount = 0;
				// 維護費報表map key是設備名稱  Value是 該設備名稱分組之後的 設備的dtolist
				Map<String, List<DmmRepositoryDTO>> dmmRepositoryListMap = new HashMap<String, List<DmmRepositoryDTO>>();
				//sn拆機
				String unstallSn = IAtomsConstants.MARK_EMPTY_STRING;
				//sn到場
				String arriveSn = IAtomsConstants.MARK_EMPTY_STRING;
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
						for(DmmRepositoryDTO dmmRepositoryDTO : DmmRepositoryDTOLists) {
							if(dmmRepositoryDTO.getSerialNumber().equals(srmCaseHandleInfoDTO.getSerialNumber())) {
								installCount ++;
								break;
							}
						}
					//拆機數量 
					} else if(IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())) {
						//【(拆機日- 裝機日)未滿三個月(90天)拆機】數量
						if(IAtomsConstants.YES.equals(srmCaseHandleInfoDTO.getUserdDays90())) {
							srmCaseHandleInfoDTO.setUserdDays90(i18NUtil.getName(srmCaseHandleInfoDTO.getUserdDays90()));
							//頻繁拆機
							WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("OFFEN_UNINSTALL");
							srmCaseHandleInfoDTO.setUserdDays90Price(workFeeDTO.getFirstPrice());
							userdDays90Count ++;
						}
						//【拆機類型=到場拆機、遺失報損】
						if("ARRIVE_UNINSTALL".equals(srmCaseHandleInfoDTO.getUninstallType())
								|| "LOSS_REPORT".equals(srmCaseHandleInfoDTO.getUninstallType())) {
							for(DmmRepositoryDTO dmmRepositoryDTO : DmmRepositoryDTOLists) {
								if(dmmRepositoryDTO.getSerialNumber().equals(srmCaseHandleInfoDTO.getSerialNumber())) {
									unInstallFeeCount ++;
									WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("UNINSTALLED");
									srmCaseHandleInfoDTO.setUnInstallPrice(workFeeDTO.getFirstPrice());
									break;
								}
							}
						} else if ("SERVICE_SELF_UNINSTALL".equals(srmCaseHandleInfoDTO.getUninstallType())) {
							for(DmmRepositoryDTO dmmRepositoryDTO : DmmRepositoryDTOLists) {
								if(dmmRepositoryDTO.getSerialNumber().equals(srmCaseHandleInfoDTO.getSerialNumber())) {
									unInstallCount ++;
									if(StringUtils.hasText(dmmRepositoryDTO.getSerialNumber())) {
										if(!StringUtils.hasText(unstallSn)) {
											unstallSn = "案件編號:" + srmCaseHandleInfoDTO.getCaseId() + ",sn:" +
													dmmRepositoryDTO.getSerialNumber() + IAtomsConstants.MARK_SEPARATOR;
										} else {
											unstallSn = unstallSn + IAtomsConstants.RETURN_LINE_FEED + "案件編號:" + srmCaseHandleInfoDTO.getCaseId() + ",sn:" +
													dmmRepositoryDTO.getSerialNumber() + IAtomsConstants.MARK_SEPARATOR;
											
										}
									}
									break;
								}
							}
						}
					//並機或者異動
					} else if(IAtomsConstants.CASE_CATEGORY.MERGE.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory()) 
							|| IAtomsConstants.CASE_CATEGORY.UPDATE.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())) {
						//且【處理方式=到場處理 且 是否同裝機作業=否
						if(("ARRIVE_PROCESS").equals(srmCaseHandleInfoDTO.getProcessType())) {
							if(IAtomsConstants.NO.equals(srmCaseHandleInfoDTO.getSameInstalled())) {
								//且【設備有收維護費】
								for(DmmRepositoryDTO dmmRepositoryDTO : DmmRepositoryDTOLists) {
									if(dmmRepositoryDTO.getSerialNumber().equals(srmCaseHandleInfoDTO.getSerialNumber())) {
										updateOrMergeCount ++;
										WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("ARRIVED");
										srmCaseHandleInfoDTO.setSettingPrice(workFeeDTO.getFirstPrice());
										break;
									}
								}
							//且【是否同裝機作業=是
							} else {
								//且【設備有收維護費】
								for(DmmRepositoryDTO dmmRepositoryDTO : DmmRepositoryDTOLists) {
									if(dmmRepositoryDTO.getSerialNumber().equals(srmCaseHandleInfoDTO.getSerialNumber())) {
										updateNotSameInstallCount ++;
										if(StringUtils.hasText(dmmRepositoryDTO.getSerialNumber())) {
											if(!StringUtils.hasText(arriveSn)) {
												arriveSn = "案件編號:" + srmCaseHandleInfoDTO.getCaseId() + ",sn:" +
														dmmRepositoryDTO.getSerialNumber() + IAtomsConstants.MARK_SEPARATOR;
											} else {
												arriveSn = arriveSn + IAtomsConstants.RETURN_LINE_FEED + "案件編號:" + srmCaseHandleInfoDTO.getCaseId() + ",sn:" +
														dmmRepositoryDTO.getSerialNumber() + IAtomsConstants.MARK_SEPARATOR;
											}
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
						fast ++;
						WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("FAST");
						srmCaseHandleInfoDTO.setDispatchPrice(workFeeDTO.getFirstPrice());
					//案件類型=特急件
					} else if("EXTRA".equals(srmCaseHandleInfoDTO.getCaseType())) {
						extra ++;
						WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("EXTRA");
						srmCaseHandleInfoDTO.setDispatchPrice(workFeeDTO.getFirstPrice());
					} else {
						srmCaseHandleInfoDTO.setDispatchPrice(null);
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
										ecrlineSum = srmCaseAssetLinkDTO.getPrice().intValue();
									} else {
										networkRouteSum = srmCaseAssetLinkDTO.getPrice().intValue();
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
					}
				}
				List<DmmRepositoryDTO> dmmRepositoryDTOList = null;
				//循環維護費設備
				for(DmmRepositoryDTO dmmRepositoryDTO : DmmRepositoryDTOLists) {
					WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("MAINTENANCE_FEE");
					dmmRepositoryDTO.setFee(workFeeDTO.getFirstPrice());
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
								//若【狀態=使用中費用總表(SIM卡(門號)月租費)中配置的單價
								workFeeDTO = getworkFeeNameByWorkCategory("NUMBER_FEE");
								dmmRepositoryDTO.setSimCardPrice(workFeeDTO.getFirstPrice());
								//若設備通訊模式含 3G、GPRS、
								//Bluetooth(3G/WIFI)、音源孔(3G/WIFI)，且狀態為使用中，則從作業別單價配置表
								commModeIdCount ++;
								break;
							}
						}
					}
				}
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
				//循環設備通過設備名稱分組之後的map
				for(Entry<String, List<DmmRepositoryDTO>> assetEntry : dmmRepositoryListMap.entrySet()){
					//取出來某個設備名稱下的所有設備list
					List<DmmRepositoryDTO> DmmRepositoryResult = assetEntry.getValue();
					//使用量
					int inUseCount = 0;
					//serialNumber
					String serialNumber = IAtomsConstants.MARK_EMPTY_STRING;
					// 已啟用待裝機
					int enableCount = 0;
					boolean hasBorrowing = false;
					//根據設備名稱分組之後的設備
					for (DmmRepositoryDTO dmmRepositoryDTO : DmmRepositoryResult) {
						if(dmmRepositoryDTO.getEnableDate() != null) {
							Date enableDateAddThreeYear = DateTimeUtils.addCalendar(dmmRepositoryDTO.getEnableDate(), 3, 0, 0);
							if(IAtomsConstants.PARAM_ASSET_STATUS_IN_USE.equals(dmmRepositoryDTO.getStatus())) {
								//【租賃未期滿 ((設備啟用日期 + 3 年) > 帳務月底，滿的最後一個月不收，改收服務費)】數量
								if(enableDateAddThreeYear.compareTo(endDate) == 1) {
									inUseCount ++;
								}
							} else {
								if(enableDateAddThreeYear.compareTo(endDate) == 1) {
									if(IAtomsConstants.PARAM_ASSET_STATUS_BORROWING.equals(dmmRepositoryDTO.getStatus())) {
										if(StringUtils.hasText(dmmRepositoryDTO.getSerialNumber())) {
											hasBorrowing = true;
											serialNumber = serialNumber + dmmRepositoryDTO.getSerialNumber() + IAtomsConstants.MARK_SEPARATOR;
										}
									}
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
														enableCount ++;
													}
												}
											}
										}
									}
								}
							}
						}
					}
					//第一行 月租費(S80 Ethernet) 
					WorkFeeSetting workFeeDTOInstalled = new WorkFeeSetting();
					WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("MONTH_FEE_" + assetEntry.getKey());
					workFeeDTOInstalled.setName(workFeeDTO.getName());
					if(!StringUtils.hasText(workFeeDTO.getName())) {
						workFeeDTOInstalled.setName("月租費( " + assetEntry.getKey() + ")");
					}
					workFeeDTOInstalled.setFirstCount(String.valueOf(inUseCount + enableCount));
					if(workFeeDTO.getFirstPrice() == null) {
						workFeeDTO.setFirstPrice(0);
					}
					workFeeDTOInstalled.setFirstPrice(workFeeDTO.getFirstPrice());
					workFeeDTOInstalled.setSum(Integer.parseInt(workFeeDTOInstalled.getFirstCount()) * workFeeDTO.getFirstPrice());
					workFeeDTOInstalled.setDescription("使用中台數"+ inUseCount +"台+已啟用待裝機" + enableCount + "台 "+ IAtomsConstants.RETURN_LINE_FEED);
					if(hasBorrowing) {
						workFeeDTOInstalled.setDescription(workFeeDTOInstalled.getDescription() + "sn:" + serialNumber + "上銀內部使用，不收費");
					}
					workFeeSettingList.add(workFeeDTOInstalled);
				}
				//第二行 頻繁拆機費
				WorkFeeSetting workFeeDTOUninstalled = new WorkFeeSetting();
				WorkFeeSetting workFeeDTO = getworkFeeNameByWorkCategory("OFFEN_UNINSTALL");
				workFeeDTOUninstalled.setName(workFeeDTO.getName());
				workFeeDTOUninstalled.setFirstCount(String.valueOf(userdDays90Count));
				workFeeDTOUninstalled.setFirstPrice(workFeeDTO.getFirstPrice());
				workFeeDTOUninstalled.setSum(Integer.parseInt(workFeeDTOUninstalled.getFirstCount()) * workFeeDTO.getFirstPrice());
				workFeeSettingList.add(workFeeDTOUninstalled);
				//第三行 急件加收
				WorkFeeSetting workFeeDTOFast = new WorkFeeSetting();
				workFeeDTO = getworkFeeNameByWorkCategory("FAST");
				workFeeDTOFast.setName(workFeeDTO.getName());
				workFeeDTOFast.setFirstCount(String.valueOf(fast));
				workFeeDTOFast.setFirstPrice(workFeeDTO.getFirstPrice());
				workFeeDTOFast.setSum(Integer.parseInt(workFeeDTOFast.getFirstCount()) * workFeeDTO.getFirstPrice());
				workFeeSettingList.add(workFeeDTOFast);
				//第三行 特急件加收
				WorkFeeSetting workFeeDTOExtra = new WorkFeeSetting();
				workFeeDTO = getworkFeeNameByWorkCategory("EXTRA");
				workFeeDTOExtra.setName(workFeeDTO.getName());
				workFeeDTOExtra.setFirstCount(String.valueOf(extra));
				workFeeDTOExtra.setFirstPrice(workFeeDTO.getFirstPrice());
				workFeeDTOExtra.setSum(Integer.parseInt(workFeeDTOExtra.getFirstCount()) * workFeeDTO.getFirstPrice());
				workFeeSettingList.add(workFeeDTOExtra);
				//第4行--線材使用-ECR線材
				//使用之案件單價不同，拆多筆，列出不同台數、單價
				//第5行--線材使用-網路線
				//使用之案件單價不同，拆多筆，列出不同台數、單價
				for (SrmCaseAssetLinkDTO srmCaseAssetLinkDTO : srmCaseLinkSuppliesDTOList) {
					WorkFeeSetting workFeeDTOSupplies = new WorkFeeSetting();
					if("ECRLINE".equals(srmCaseAssetLinkDTO.getItemCategory())) {
						workFeeDTOSupplies.setWorkCategory("線材使用-ECR線材");
						workFeeDTOSupplies.setName("線材使用-ECR線材");
						if(srmCaseAssetLinkDTO.getNumber() == null) srmCaseAssetLinkDTO.setNumber(0);
						workFeeDTOSupplies.setFirstCount(String.valueOf(srmCaseAssetLinkDTO.getNumber()));
						if(srmCaseAssetLinkDTO.getPrice() == null) srmCaseAssetLinkDTO.setPrice(new BigDecimal(0));
						workFeeDTOSupplies.setFirstPrice(srmCaseAssetLinkDTO.getPrice().intValue());
						workFeeDTOSupplies.setSum((Integer.parseInt(workFeeDTOSupplies.getFirstCount()) * srmCaseAssetLinkDTO.getPrice().intValue()));
						workFeeSettingList.add(workFeeDTOSupplies);
					} else if("NetworkRoute".equals(srmCaseAssetLinkDTO.getItemCategory())) {
						workFeeDTOSupplies.setWorkCategory("線材使用-網路線");
						workFeeDTOSupplies.setName("線材使用-網路線");
						if(srmCaseAssetLinkDTO.getNumber() == null) srmCaseAssetLinkDTO.setNumber(0);
						workFeeDTOSupplies.setFirstCount(String.valueOf(srmCaseAssetLinkDTO.getNumber()));
						if(srmCaseAssetLinkDTO.getPrice() == null) srmCaseAssetLinkDTO.setPrice(new BigDecimal(0));
						workFeeDTOSupplies.setFirstPrice(srmCaseAssetLinkDTO.getPrice().intValue());
						workFeeDTOSupplies.setSum((Integer.parseInt(workFeeDTOSupplies.getFirstCount()) * srmCaseAssetLinkDTO.getPrice().intValue()));
						workFeeSettingList.add(workFeeDTOSupplies);
					}
				}
				//第六行 維修耗材費用
				WorkFeeSetting workFeeDTOSuppliesPrice = new WorkFeeSetting();
				workFeeDTOSuppliesPrice.setName("維修耗材費用");
				workFeeSettingList.add(workFeeDTOSuppliesPrice);
				//第七行 APN月租費
				WorkFeeSetting workFeeDTOApn = new WorkFeeSetting();
				workFeeDTOApn.setName("APN月租費");
				workFeeDTOApn.setSum(1200);
				workFeeDTOApn.setDescription("每月固定收費：申請中華電信MDVPN，SIM卡必須加入APN連結後端的VPN。");
				workFeeSettingList.add(workFeeDTOApn);
				//第八行 VPN存取控制月租費
				WorkFeeSetting workFeeDTOVpn = new WorkFeeSetting();
				workFeeDTOVpn.setName("VPN存取控制月租費");
				workFeeDTOVpn.setSum(2300);
				workFeeDTOVpn.setDescription("每月固定收費：前述APN必須同時連結經貿聯網VPN(No.2765)執行TMS程式參數下載及連結上銀VPN(No. 3956)執行刷卡交易，故須設定此存取控制。");
				workFeeSettingList.add(workFeeDTOVpn);
				//第九行 查核(超過五台)
				WorkFeeSetting workFeeDTOCheck = new WorkFeeSetting();
				workFeeDTO = getworkFeeNameByWorkCategory("CHECK");
				workFeeDTOCheck.setName(workFeeDTO.getName());
				if(checkCount < 5) {
					workFeeDTOCheck.setFirstCount(String.valueOf(0));
				} else {
					workFeeDTOCheck.setFirstCount(String.valueOf(checkCount - 5));
				}
				workFeeDTOCheck.setFirstPrice(workFeeDTO.getFirstPrice());
				workFeeDTOCheck.setSum(Integer.parseInt(workFeeDTOCheck.getFirstCount()) * workFeeDTO.getFirstPrice());
				workFeeSettingList.add(workFeeDTOCheck);
				
				//對（維護費）設備依據設備進行分組統計
				Map<String, String> assetInfo = new HashMap<String, String>();
				Map<String, Integer> assetNo = new HashMap<String, Integer>();
				Integer temp = null;
				//第十行 月維護費
				for (DmmRepositoryDTO dto : DmmRepositoryDTOLists) {
					if (assetNo.containsKey(dto.getAssetTypeId())) {
						temp = assetNo.get(dto.getAssetTypeId());
						assetNo.put(dto.getAssetTypeId(), temp + 1);
					} else {
						assetNo.put(dto.getAssetTypeId(), 1);
						assetInfo.put(dto.getAssetTypeId(), dto.getName());
					}
				}
				if (assetNo.size() != 0) {
					for (String s: assetNo.keySet()) {
						WorkFeeSetting workFeeDTOMaintenanceFee = new WorkFeeSetting();
						workFeeDTO = getworkFeeNameByWorkCategory("MAINTENANCE_FEE");
						workFeeDTOMaintenanceFee.setName(assetInfo.get(s) + IAtomsConstants.MARK_SPACE + workFeeDTO.getName());
						if(!CollectionUtils.isEmpty(DmmRepositoryDTOLists)) {
							workFeeDTOMaintenanceFee.setFirstCount(String.valueOf(assetNo.get(s)));
						} else {
							workFeeDTOMaintenanceFee.setFirstCount(String.valueOf(0));
						}
						workFeeDTOMaintenanceFee.setFirstPrice(workFeeDTO.getFirstPrice());
						workFeeDTOMaintenanceFee.setSum(Integer.parseInt(workFeeDTOMaintenanceFee.getFirstCount()) * workFeeDTO.getFirstPrice());
						workFeeSettingFeeList.add(workFeeDTOMaintenanceFee);
					}
				}
				/*WorkFeeSetting workFeeDTOMaintenanceFee = new WorkFeeSetting();
				workFeeDTO = getworkFeeNameByWorkCategory("MAINTENANCE_FEE");
				workFeeDTOMaintenanceFee.setName(workFeeDTO.getName());
				if(!CollectionUtils.isEmpty(DmmRepositoryDTOLists)) {
					workFeeDTOMaintenanceFee.setFirstCount(String.valueOf(DmmRepositoryDTOLists.size()));
				} else {
					workFeeDTOMaintenanceFee.setFirstCount(String.valueOf(0));
				}
				workFeeDTOMaintenanceFee.setFirstPrice(workFeeDTO.getFirstPrice());
				workFeeDTOMaintenanceFee.setSum(Integer.parseInt(workFeeDTOMaintenanceFee.getFirstCount()) * workFeeDTO.getFirstPrice());
				workFeeSettingFeeList.add(workFeeDTOMaintenanceFee);*/
				//第十一行 裝/移機費
				WorkFeeSetting workFeeDTOInstal = new WorkFeeSetting();
				workFeeDTO = getworkFeeNameByWorkCategory("INSTALLED");
				workFeeDTOInstal.setName(workFeeDTO.getName());
				workFeeDTOInstal.setFirstCount(String.valueOf(installCount));
				workFeeDTOInstal.setFirstPrice(workFeeDTO.getFirstPrice());
				workFeeDTOInstal.setSum(Integer.parseInt(workFeeDTOInstal.getFirstCount()) * workFeeDTO.getFirstPrice());
				workFeeSettingFeeList.add(workFeeDTOInstal);
				//第十二行 拆機費
				WorkFeeSetting workFeeDTOUninstal = new WorkFeeSetting();
				workFeeDTO = getworkFeeNameByWorkCategory("UNINSTALLED");
				workFeeDTOUninstal.setName(workFeeDTO.getName());
				workFeeDTOUninstal.setFirstCount(String.valueOf(unInstallFeeCount));
				workFeeDTOUninstal.setFirstPrice(workFeeDTO.getFirstPrice());
				workFeeDTOUninstal.setSum(Integer.parseInt(workFeeDTOUninstal.getFirstCount()) * workFeeDTO.getFirstPrice());
				workFeeDTOUninstal.setDescription(unstallSn + "共"+ unInstallCount +"台");
				workFeeSettingFeeList.add(workFeeDTOUninstal);
				//第十三行 參數設定(到場)
				WorkFeeSetting workFeeDTOArrive = new WorkFeeSetting();
				workFeeDTO = getworkFeeNameByWorkCategory("ARRIVED");
				workFeeDTOArrive.setName(workFeeDTO.getName());
				workFeeDTOArrive.setFirstCount(String.valueOf(updateOrMergeCount));
				workFeeDTOArrive.setFirstPrice(workFeeDTO.getFirstPrice());
				workFeeDTOArrive.setSum(Integer.parseInt(workFeeDTOArrive.getFirstCount()) * workFeeDTO.getFirstPrice());
				workFeeDTOArrive.setDescription(arriveSn + "共"+ updateNotSameInstallCount +"台");
				workFeeSettingFeeList.add(workFeeDTOArrive);
				//第十四行 參數設定(遠端軟派)
				WorkFeeSetting workFeeDTOSoftDispatch = new WorkFeeSetting();
				workFeeDTO = getworkFeeNameByWorkCategory("REMOTE");
				workFeeDTOSoftDispatch.setName(workFeeDTO.getName());
				workFeeDTOSoftDispatch.setFirstCount(String.valueOf(updateSoftDispatch));
				workFeeDTOSoftDispatch.setFirstPrice(workFeeDTO.getFirstPrice());
				workFeeDTOSoftDispatch.setSum(Integer.parseInt(workFeeDTOSoftDispatch.getFirstCount()) * workFeeDTO.getFirstPrice());
				workFeeSettingFeeList.add(workFeeDTOSoftDispatch);
				//第十五行 二次作業
				WorkFeeSetting workFeeDTOSecond = new WorkFeeSetting();
				workFeeDTO = getworkFeeNameByWorkCategory("SECOND");
				workFeeDTOSecond.setName(workFeeDTO.getName());
				workFeeDTOSecond.setFirstPrice(workFeeDTO.getFirstPrice());
				workFeeSettingFeeList.add(workFeeDTOSecond);
				//第十六行 SIM卡(門號)月租費
				WorkFeeSetting workFeeDTONumberFee = new WorkFeeSetting();
				workFeeDTO = getworkFeeNameByWorkCategory("NUMBER_FEE");
				workFeeDTONumberFee.setName(workFeeDTO.getName());
				workFeeDTONumberFee.setFirstCount(String.valueOf(commModeIdCount));
				workFeeDTONumberFee.setFirstPrice(workFeeDTO.getFirstPrice());
				workFeeDTONumberFee.setSum(commModeIdCount * workFeeDTO.getFirstPrice());
				workFeeSettingFeeList.add(workFeeDTONumberFee);
				
				List<DmmRepositoryDTO> dmmRepositoryList = new ArrayList<DmmRepositoryDTO>();
				DmmRepositoryDTO DmmRepositoryDTO = new DmmRepositoryDTO();
				DmmRepositoryDTO.setWorkFeeSettingList(workFeeSettingList);
				DmmRepositoryDTO.setWorkFeeSettingFeeList(workFeeSettingFeeList);
				dmmRepositoryList.add(DmmRepositoryDTO);
				Map<String, String> subjrXmlNames = new HashMap<String, String>();
				subjrXmlNames.put(IAtomsConstants.SCSB_TX_PRICE_SUM_SUB_REPORT4_CH_NAME, "SUBREPORT_DIR4");
				subjrXmlNames.put(IAtomsConstants.SCSB_TX_PRICE_SUM_SUB_REPORT3_CH_NAME, "SUBREPORT_DIR3");
				//第一個sheet
				criteria = new JasperReportCriteriaDTO();
				criteria.setAutoBuildJasper(false);
				criteria.setSubjrXmlNames(subjrXmlNames);
				criteria.setResult(dmmRepositoryList);
				//設置所需報表的Name
				criteria.setJrxmlName(IAtomsConstants.SCSB_PRICE_SUM_REPORT);
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
				countMap.put("yyyyMM", DateTimeUtils.toString(startDate, DateTimeUtils.DT_FMT_YYYYMMDD_SLASH).substring(0,7));
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
						criteria.setJrxmlName(IAtomsConstants.SCSB_ASSET_INFO);
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
				if(SrmCaseHandleInfoDTOList.size() == 0) {
					SrmCaseHandleInfoDTO srmCaseHandleInfoDTO = new SrmCaseHandleInfoDTO();
					SrmCaseHandleInfoDTOList.add(srmCaseHandleInfoDTO);
				}
				int i = 1;
				for (SrmCaseHandleInfoDTO dto : SrmCaseHandleInfoDTOList) {
					dto.setRowId(Integer.toString(i++));
				}
				criteria = new JasperReportCriteriaDTO();
				criteria.setAutoBuildJasper(false);
				criteria.setResult(SrmCaseHandleInfoDTOList);
				//設置所需報表的Name
				criteria.setJrxmlName(IAtomsConstants.SCSB_CASE_INFO_REPORT);
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
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
					DmmRepositoryDTO dmmRepositoryDTO = new DmmRepositoryDTO();
					DmmRepositoryDTOLists.add(dmmRepositoryDTO);
				}
				criteria = new JasperReportCriteriaDTO();
				criteria.setAutoBuildJasper(false);
				criteria.setResult(DmmRepositoryDTOLists);
				//設置所需報表的Name
				criteria.setJrxmlName(IAtomsConstants.SCSB_MAINTAIN_ASSET_INFO);
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
				
				//第7個sheet 維修耗材費用-AO未回覆扣款方式
				if(paymentInfoDTOListNoTax.size() == 0) {
					SrmPaymentInfoDTO paymentInfoDTO = new SrmPaymentInfoDTO();
					paymentInfoDTOListNoTax.add(paymentInfoDTO);
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

	
}