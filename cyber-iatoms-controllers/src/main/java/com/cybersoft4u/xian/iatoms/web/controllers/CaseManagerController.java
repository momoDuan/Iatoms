package com.cybersoft4u.xian.iatoms.web.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import cafe.core.bean.Message;
import cafe.core.config.SystemConfigManager;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.exception.CommonException;
import cafe.core.service.ServiceException;
import cafe.core.util.CoreConstants;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.BindPageDataUtils;
import cafe.core.web.controller.util.SessionHelper;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.async.IAtomsAsynchronousHandler;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAssetLinkDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTransactionDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTransactionParameterDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmTransactionParameterItemDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.CrossTabReportDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;
import com.cybersoft4u.xian.iatoms.common.utils.FileUtils;
import com.cybersoft4u.xian.iatoms.common.utils.POIUtils;
import com.cybersoft4u.xian.iatoms.common.utils.ReportExporter;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;
import com.cybersoft4u.xian.iatoms.services.impl.CaseManagerService;
import com.cybersoft4u.xian.iatoms.services.impl.ContractSlaService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
/**
 * Purpose: 案件處理Controller
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016年8月5日
 * @MaintenancePersonnel CrissZhang
 */
public class CaseManagerController extends AbstractMultiActionController<CaseManagerFormDTO> {

	/**
	 * 系統日誌記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(CaseManagerController.class);
	/**
	 * 案件類型對應的模板名稱
	 */
	private Map<String,String> importFilePath;
	
	/**
	 * Constructor: 無參構造
	 */
	public CaseManagerController() {
		this.setCommandClass(CaseManagerFormDTO.class);
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	public boolean validate(CaseManagerFormDTO command) throws CommonException {
		long startTime = System.currentTimeMillis();
		if (command == null) {
			return false;
		}
		String actionId = command.getActionId();
		//獲取案件類別
		String caseCategory = command.getCaseCategory();
		Map<String, Object> map = null;
		SrmCaseTransactionDTO srmCaseTransactionDTO = command.getSrmCaseTransactionDTO();
		Message msg = null;
		if (IAtomsConstants.ACTION_CREATE_CASE.equals(actionId) || IAtomsConstants.ACTION_NEW_DISPATCH.equals(actionId)) {
			SrmCaseHandleInfoDTO srmCaseHandleInfoDTO = command.getSrmCaseHandleInfoDTO();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) command.getLogonUser();
			// 如果srmCaseHandleInfoDTO 對象為null直接返回false
			if (srmCaseHandleInfoDTO == null) {
				return false;
			}
			// 案件編號
			String caseId = srmCaseHandleInfoDTO.getCaseId();
			// 繼續判斷
			boolean isContinue = false;
			// 忽略設備 // Task #2682 進入待結案審查，就只能改 需求單號
//			boolean isIgnoreAsset = false;
			// 結案、立即結案 編輯時
			if(StringUtils.hasText(caseId)){
				if(StringUtils.hasText(srmCaseHandleInfoDTO.getCaseStatus())){
					// 結案立即結案，驗證需求單號 // Task #2682 進入待結案審查，就只能改 需求單號
					if(IAtomsConstants.CASE_STATUS.IMMEDIATE_CLOSE.getCode().equals(srmCaseHandleInfoDTO.getCaseStatus())
							|| IAtomsConstants.CASE_STATUS.CLOSED.getCode().equals(srmCaseHandleInfoDTO.getCaseStatus())
							|| IAtomsConstants.CASE_STATUS.WAIT_CLOSE.getCode().equals(srmCaseHandleInfoDTO.getCaseStatus())){
						// 需求單號 限制30
						String requirementNo = srmCaseHandleInfoDTO.getRequirementNo();
						if(StringUtils.hasText(requirementNo)){
							if(!ValidateUtils.length(requirementNo, 0, 17)){
								msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
										new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_REQUIREMENT_NO), IAtomsConstants.REQUIREMENT_NO_LENGTH});
								throw new CommonException(msg);
							}
						}
					// 待結案審查設備信息不改
					}/* else if(IAtomsConstants.CASE_STATUS.WAIT_CLOSE.getCode().equals(srmCaseHandleInfoDTO.getCaseStatus())){
						isContinue = true;
						// Task #2682 進入待結案審查，就只能改 需求單號
//						isIgnoreAsset = true;
					// 其他狀態暫不處理
					}*/ else {
						isContinue = true;
					}
				}
			} else {
				isContinue = true;
			}
			// 繼續驗證
			if(isContinue){
				// 其他說明
				String customerId = null;
				String descripton = srmCaseHandleInfoDTO.getDescription();
				if(StringUtils.hasText(descripton)){
					// 其他說明限制1000位
					if(!ValidateUtils.length(descripton, 0, 1000)){
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_DESCRIPTION), IAtomsConstants.MAXLENGTH_NUMBER_ONE_THOUSAND});
						throw new CommonException(msg);
					}
				}
				// 裝機保存
				if(IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(command.getCaseCategory())
						|| IAtomsConstants.CASE_CATEGORY.UPDATE.getCode().equals(command.getCaseCategory())){
					// 客戶
					customerId = srmCaseHandleInfoDTO.getCustomerId();
					
					// 刷卡機類型
					String edcType = srmCaseHandleInfoDTO.getEdcType();
					
					// 裝機
					if(IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(command.getCaseCategory())){
						// Task #2682 進入待結案審查，就只能改 需求單號
//						if(!isIgnoreAsset){
							// 客戶
							if (!StringUtils.hasText(customerId)) {
								msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
										new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_CUSTOMER_ID)});
								throw new CommonException(msg);
							}
							// 合約
							String contractId = srmCaseHandleInfoDTO.getContractId();
							if (!StringUtils.hasText(contractId)) {
								msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
										new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_CONTRACT_ID)});
								throw new CommonException(msg);
							}
							// 待派工 + 已派工 維護廠商
							if(!StringUtils.hasText(caseId) || IAtomsConstants.CASE_STATUS.WAIT_DISPATCH.getCode().equals(srmCaseHandleInfoDTO.getCaseStatus())
									 || IAtomsConstants.CASE_STATUS.DISPATCHED.getCode().equals(srmCaseHandleInfoDTO.getCaseStatus())){
								// 維護廠商
								String companyId = srmCaseHandleInfoDTO.getCompanyId();
								if (!StringUtils.hasText(companyId)) {
									msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
											new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_COMPANY_ID)});
									throw new CommonException(msg);
								}
							}
							// 刷卡機類型
							if(!StringUtils.hasText(edcType)){
								msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
										new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_EDC_TYPE)});
								throw new CommonException(msg);
							}
//						}
						
						// 裝機類型
						String installType = srmCaseHandleInfoDTO.getInstallType();
						if (!StringUtils.hasText(installType)) {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_INSTALL_TYPE)});
							throw new CommonException(msg);
						}
						// ECR連線
						String ecrConnection = srmCaseHandleInfoDTO.getEcrConnection();
						if(!StringUtils.hasText(ecrConnection)){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_ECR_CONNECTION)});
							throw new CommonException(msg);
						}
						
						// 裝機聯絡人
						String installedContact = srmCaseHandleInfoDTO.getInstalledContact();
						if(!StringUtils.hasText(installedContact)){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_INSTALLED_CONTACT)});
							throw new CommonException(msg);
						} else {
							if(!ValidateUtils.length(installedContact, 0, 50)){
								msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
										new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_INSTALLED_CONTACT), IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
								throw new CommonException(msg);
							}
						}
						// 裝機聯絡人電話
						String installedContactPhone = srmCaseHandleInfoDTO.getInstalledContactPhone();
						if(!StringUtils.hasText(installedContactPhone)){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_INSTALLED_CONTACT_PHONE)});
							throw new CommonException(msg);
						} else {
							if(!ValidateUtils.length(installedContactPhone, 0, 20)){
								msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
										new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_INSTALLED_CONTACT_PHONE), IAtomsConstants.MAXLENGTH_NUMBER_TWENTY});
								throw new CommonException(msg);
							}
						}
					} else {
						// Task #2682 進入待結案審查，就只能改 需求單號
//						if(!isIgnoreAsset){
							// DTID
							String dtid = srmCaseHandleInfoDTO.getDtid();
							if(!StringUtils.hasText(dtid)){
								msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
										new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_CUSTOMER_ID)});
								throw new CommonException(msg);
							} else {
								// dtid限制8位
								if(!ValidateUtils.length(dtid, 0, 8)){
									msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
											new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_DTID), IAtomsConstants.MAXLENGTH_NUMBER_EIGHT});
									throw new CommonException(msg);
								}
							}
//						}
					}
					// 需求單號 限制30
					String requirementNo = srmCaseHandleInfoDTO.getRequirementNo();
					if(StringUtils.hasText(requirementNo)){
						if(!ValidateUtils.length(requirementNo, 0, 17)){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_REQUIREMENT_NO), IAtomsConstants.REQUIREMENT_NO_LENGTH});
							throw new CommonException(msg);
						}
					}
					// Task #2682 進入待結案審查，就只能改 需求單號
//					if(!isIgnoreAsset){
						// 案件類型
						String caseType = srmCaseHandleInfoDTO.getCaseType();
						if(!StringUtils.hasText(caseType)){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_CASE_TYPE)});
							throw new CommonException(msg);
						} else {
							if(IAtomsConstants.TICKET_MODE_APPOINTMENT.equals(caseType)){
								Date expectedCompletionDate = srmCaseHandleInfoDTO.getExpectedCompletionDate();
								// 預計完成日
								if(expectedCompletionDate == null){
									msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
											new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_EXPECTED_COMPLETION_DATE)});
									throw new CommonException(msg);
								}
							}
						}
//					}
					
					// 特點代號
					/*String merchantCode = srmCaseHandleInfoDTO.getMerchantCode();
					if(!StringUtils.hasText(merchantCode)){
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_MERCHANT_CODE)});
						throw new CommonException(msg);
					} else {
						if(!ValidateUtils.length(merchantCode, 0, 20)){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_MERCHANT_CODE)});
							throw new CommonException(msg);
						}
					}*/
					String merchantCode = srmCaseHandleInfoDTO.getMerMid();
					if(!StringUtils.hasText(merchantCode)){
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_MERCHANT_CODE)});
						throw new CommonException(msg);
					} else {
						if(!ValidateUtils.varcharLength(merchantCode, 0, 20)){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_MERCHANT_CODE), IAtomsConstants.MAXLENGTH_NUMBER_TWENTY});
							throw new CommonException(msg);
						}
					}
					// 表頭（同對外名稱）
					String merchantHeaderId = srmCaseHandleInfoDTO.getMerchantHeaderId();
					if(!StringUtils.hasText(merchantHeaderId)){
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_MERCHANT_HEADER_ID)});
						throw new CommonException(msg);
					}
					// 裝機地址-縣市
					String installLocation = srmCaseHandleInfoDTO.getInstalledAdressLocation();
					if(!StringUtils.hasText(installLocation)){
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_INSTALLED_LOCATION)});
						throw new CommonException(msg);
					}
					// 裝機地址
					String installedAdress = srmCaseHandleInfoDTO.getInstalledAdress();
					if(!StringUtils.hasText(installedAdress)){
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_INSTALLED_ADRESS)});
						throw new CommonException(msg);
					} else {
						if(!ValidateUtils.length(installedAdress, 0, 100)){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_INSTALLED_ADRESS), IAtomsConstants.MAXLENGTH_NUMBER_ONE_HUNDRED});
							throw new CommonException(msg);
						}
					}
					
					// 軟體版本
					String softwareVersion = srmCaseHandleInfoDTO.getSoftwareVersion();
					if(!StringUtils.hasText(softwareVersion)){
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_SOFTWARE_VERSION)});
						throw new CommonException(msg);
					}
					
					// 本機IP
					String localhostIp = srmCaseHandleInfoDTO.getLocalhostIp();
					if(StringUtils.hasText(localhostIp)){
						if(!ValidateUtils.length(localhostIp, 0, 50)){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_LOCALHOST_IP), IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
							throw new CommonException(msg);
						}
					}
					// Gateway
					String gateway = srmCaseHandleInfoDTO.getGateway();
					if(StringUtils.hasText(gateway)){
						if(!ValidateUtils.length(gateway, 0, 50)){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_GATEWAY), IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
							throw new CommonException(msg);
						}
					}
					// netmask
					String netmask = srmCaseHandleInfoDTO.getNetmask();
					if(StringUtils.hasText(netmask)){
						if(!ValidateUtils.length(netmask, 0, 50)){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_NETMASK), IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
							throw new CommonException(msg);
						}
					}
					try {
						MultiParameterInquiryContext param = null;
						// 處理不判斷
						if(!StringUtils.hasText(caseId)){
							// 裝機
							if(IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(command.getCaseCategory())){
								/*
								 * 若案件筆數選擇“單筆”，則僅檢核一筆DTID；若複製多筆，則依【複製筆數】檢核可用之DTID數量，
								 * 若客戶案件之DTID為自動生成，檢核是否有可使用之DTID，若無可用DTID需提示「客戶：{客戶} +，機型：{機型}無可使用之DTID，請于【DTID號碼管理】設定」
								 */
								// 單筆還是多筆
								Boolean isSingle = srmCaseHandleInfoDTO.getIsSingle();
								// 複製筆數
								String caseNumber = srmCaseHandleInfoDTO.getCaseNumber();
								
								if(isSingle != null && !isSingle.booleanValue()){
									// 多筆判斷輸入是否為正整數
									if(StringUtils.hasText(caseNumber)){
										if(!ValidateUtils.number(caseNumber)){
											msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.ONLY_ALLOW_INTEGER, 
													new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_CASE_NUMBER)});
											throw new CommonException(msg);
										}
									} else {
										// 判斷是否輸入
										msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
												new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_CASE_NUMBER)});
										throw new CommonException(msg);
									}
								}
								// 驗證dtid
								param = new MultiParameterInquiryContext();
								param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_NUMBER.getValue(), caseNumber);
								param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
								param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE.getValue(), edcType);
								param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.IS_SINGLE.getValue(), isSingle);
								param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASETRANSACTION_PARAMETER_DTOS.getValue(), command.getSrmCaseTransactionParameterDTOs());
								SessionContext dtidNumberCtx = this.getServiceLocator().doService(logonUser, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_CHECK_DTID_NUMBER, param);
								map = (Map<String, Object>) dtidNumberCtx.getResponseResult();
								if(!((Boolean) map.get(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS)).booleanValue()){
									msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.NO_DITD_FOR_CUSTOMER_AND_TYPE, 
											new String[]{(String) map.get(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_NAME.getValue()), (String) map.get(SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE_NAME.getValue())});
									throw new CommonException(msg);
								}
							}
						}
						// 週邊設備1
						String peripherals = srmCaseHandleInfoDTO.getPeripherals();
						// 週邊設備2
						String peripherals2 = srmCaseHandleInfoDTO.getPeripherals2();
						// 週邊設備3
						String peripherals3 = srmCaseHandleInfoDTO.getPeripherals3();
						// 週邊設備1名稱
						String peripheralsName = srmCaseHandleInfoDTO.getPeripheralsName();
						// 週邊設備2名稱
						String peripherals2Name = srmCaseHandleInfoDTO.getPeripherals2Name();
						// 週邊設備3名稱
						String peripherals3Name = srmCaseHandleInfoDTO.getPeripherals3Name();
						String tempPeripheralsName = null;
						/*
						 * 若週邊設備(1~3)選項重覆，錯誤訊息「週邊設備選項XXX已重覆」
						 */
						// 如果周邊設備1存在 且與周邊設備2或者周邊設備3相同
						if(StringUtils.hasText(peripherals) && (peripherals.equals(peripherals2) || peripherals.equals(peripherals3))){
							tempPeripheralsName = peripheralsName;
							// 如果周邊設備2存在 且與周邊設備1或者周邊設備3相同
						} else if(StringUtils.hasText(peripherals2) && (peripherals2.equals(peripherals) || peripherals2.equals(peripherals3))){
							tempPeripheralsName = peripherals2Name;
							// 如果周邊設備3存在 且與周邊設備1或者周邊設備2相同
						} else if(StringUtils.hasText(peripherals3) && (peripherals3.equals(peripherals) || peripherals3.equals(peripherals2))){
							tempPeripheralsName = peripherals3Name;
						}
						// 符合上面三種情況的任意一種
						if(StringUtils.hasText(tempPeripheralsName)){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PERIPHERALS_IS_REPEAT, 
									new String[]{tempPeripheralsName});
							throw new CommonException(msg);
						}
						
						// 週邊設備功能1:
						String peripheralsFunction = srmCaseHandleInfoDTO.getPeripheralsFunction();
						// 週邊設備功能2:
						String peripheralsFunction2 = srmCaseHandleInfoDTO.getPeripheralsFunction2();
						// 週邊設備功能3:
						String peripheralsFunction3 = srmCaseHandleInfoDTO.getPeripheralsFunction3();
						// 內建功能
						String builtInFeature = srmCaseHandleInfoDTO.getBuiltInFeature();
						/*
						 * 若設備內建功能與週邊設備功能重覆，錯誤訊息「設備功能XXX已重覆」
						 */
						if(StringUtils.hasText(builtInFeature) || StringUtils.hasText(peripheralsFunction) || StringUtils.hasText(peripheralsFunction2) || StringUtils.hasText(peripheralsFunction3)){
							param = new MultiParameterInquiryContext();
							param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION.getValue(), peripheralsFunction);
							param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION2.getValue(), peripheralsFunction2);
							param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION3.getValue(), peripheralsFunction3);
							param.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.BUILT_IN_FEATURE.getValue(), builtInFeature);
							SessionContext supportFunCtx = this.getServiceLocator().doService(logonUser, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_GET_REPEAT_SUPPORT_FUN, param);
							String repeatSupportFun = (String) supportFunCtx.getResponseResult();
							if(StringUtils.hasText(repeatSupportFun)){
								msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PERIPHERALS_IS_REPEAT, 
										new String[]{repeatSupportFun});
								throw new CommonException(msg);
							}
						}
						/*
						 * 若交易參數未設定，錯誤訊息「請至少設定一筆交易參數」
						 * 	未設定交易類別為“一般交易”的參數，錯誤訊息「請設定交易類別為“一般交易”的交易參數」
						 */
						List<SrmCaseTransactionParameterDTO> srmCaseTransactionParameterDTOs = command.getSrmCaseTransactionParameterDTOs();
						if(CollectionUtils.isEmpty(srmCaseTransactionParameterDTOs)){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.AT_LEAST_ONE_TRANS_PARAMETER);
							throw new CommonException(msg);
						} else {
							boolean commonFlag = false;
							boolean cupAndSmartpayFlag = false;
							for(SrmCaseTransactionParameterDTO srmCaseTransactionParameterDTO : srmCaseTransactionParameterDTOs){
								// 交易類別為“一般交易”
								if(IAtomsConstants.TRANSACTION_CATEGORY.COMMON_VM.getCode().equals(srmCaseTransactionParameterDTO.getTransactionType())
								|| IAtomsConstants.TRANSACTION_CATEGORY.COMMON_VMJ.getCode().equals(srmCaseTransactionParameterDTO.getTransactionType())
								|| IAtomsConstants.TRANSACTION_CATEGORY.COMMON_VMJU.getCode().equals(srmCaseTransactionParameterDTO.getTransactionType())
								|| IAtomsConstants.TRANSACTION_CATEGORY.CONSTRUCTION_COMPANY.getCode().equals(srmCaseTransactionParameterDTO.getTransactionType())){
									commonFlag = true;
								}
								// 交易類別為CUP或Smartpay
								if(IAtomsConstants.TRANSACTION_CATEGORY.CUP.getCode().equals(srmCaseTransactionParameterDTO.getTransactionType())
										|| IAtomsConstants.TRANSACTION_CATEGORY.SMART_PAY.getCode().equals(srmCaseTransactionParameterDTO.getTransactionType())){
									cupAndSmartpayFlag = true;
								}
							}
							if(!commonFlag){
								msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.TRANSACTION_TYPE_IS_COMMON);
								throw new CommonException(msg);
							}
							/*
							 * 未選取CUP或Smartpay交易類別，不可選取Pinpad設備及功能
							 */
							if((StringUtils.hasText(builtInFeature) && builtInFeature.contains(IAtomsConstants.SUPPORTED_FUNCTION_PINPAD)) 
									|| (StringUtils.hasText(peripheralsFunction) && peripheralsFunction.contains(IAtomsConstants.SUPPORTED_FUNCTION_PINPAD))
									|| (StringUtils.hasText(peripheralsFunction2) && peripheralsFunction2.contains(IAtomsConstants.SUPPORTED_FUNCTION_PINPAD))
									|| (StringUtils.hasText(peripheralsFunction3) && peripheralsFunction3.contains(IAtomsConstants.SUPPORTED_FUNCTION_PINPAD)) ){
								if(!cupAndSmartpayFlag){
									msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.NO_CUP_OR_SMARTPAY_TRANS_TYPE);
									throw new CommonException(msg);
								}
							} else {
								/*
								 * 選取CUP或Smartpay交易類別，要選取Pinpad設備及功能
								 */
								if(cupAndSmartpayFlag){
									msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.NEED_PINPAD_ASSET_FUNCTION);
									throw new CommonException(msg);
								}
							}
						}
					} catch (Exception e) {
						if(e instanceof CommonException){
							throw new CommonException(((CommonException) e).getErrorMessage());
						} else {
							return false;
						}
					}
					// 驗證tms參數
					String tmsParamDesc = srmCaseHandleInfoDTO.getTmsParamDesc();
					if(StringUtils.hasText(tmsParamDesc)){
						// tms參數限制2000位
						if(!ValidateUtils.length(tmsParamDesc, 0, 2000)){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_TMS_PARAM_DESC), IAtomsConstants.MAXLENGTH_TMS_PARAM_DESC});
							throw new CommonException(msg);
						}
					}
				} else if(IAtomsConstants.CASE_CATEGORY.MERGE.getCode().equals(command.getCaseCategory())
						|| IAtomsConstants.CASE_CATEGORY.CHECK.getCode().equals(command.getCaseCategory())
						|| IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode().equals(command.getCaseCategory())
						|| IAtomsConstants.CASE_CATEGORY.PROJECT.getCode().equals(command.getCaseCategory())
						|| IAtomsConstants.CASE_CATEGORY.REPAIR.getCode().equals(command.getCaseCategory())){
					// Task #2682 進入待結案審查，就只能改 需求單號
//					if(!isIgnoreAsset){
						// DTID
						String dtid = srmCaseHandleInfoDTO.getDtid();
						if(!StringUtils.hasText(dtid)){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_CUSTOMER_ID)});
							throw new CommonException(msg);
						} else {
							// dtid限制8位
							if(!ValidateUtils.length(dtid, 0, 8)){
								msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
										new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_DTID), IAtomsConstants.MAXLENGTH_NUMBER_EIGHT});
								throw new CommonException(msg);
							}
						}
//					}
					// 需求單號 限制30
					String requirementNo = srmCaseHandleInfoDTO.getRequirementNo();
					if(StringUtils.hasText(requirementNo)){
						if(!ValidateUtils.length(requirementNo, 0, 17)){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_REQUIREMENT_NO), IAtomsConstants.REQUIREMENT_NO_LENGTH});
							throw new CommonException(msg);
						}
					}
					// 如果為拆機/倂機
					if(IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode().equals(command.getCaseCategory()) 
							|| IAtomsConstants.CASE_CATEGORY.MERGE.getCode().equals(command.getCaseCategory())){
						// 拆機驗證拆機類型
						if(IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode().equals(command.getCaseCategory())){
							// 拆機類型
							String uninstallType = srmCaseHandleInfoDTO.getUninstallType();
							if (!StringUtils.hasText(uninstallType)) {
								msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
										new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_UNINSTALL_TYPE)});
								throw new CommonException(msg);
							}
						// 併機驗證TMS參數
						} else {
							// 驗證tms參數
							String tmsParamDesc = srmCaseHandleInfoDTO.getTmsParamDesc();
							if(StringUtils.hasText(tmsParamDesc)){
								// tms參數限制2000位
								if(!ValidateUtils.length(tmsParamDesc, 0, 2000)){
									msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
											new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_TMS_PARAM_DESC), IAtomsConstants.MAXLENGTH_TMS_PARAM_DESC});
									throw new CommonException(msg);
								}
							}
						}
						// 聯系聯絡人
						String contactUser = srmCaseHandleInfoDTO.getContactUser();
						if(!StringUtils.hasText(contactUser)){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_CONTACT_USER)});
							throw new CommonException(msg);
						} else {
							if(!ValidateUtils.length(contactUser, 0, 50)){
								msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
										new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_CONTACT_USER), IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
								throw new CommonException(msg);
							}
						}
						// 聯系聯絡人電話
						String contactUserPhone = srmCaseHandleInfoDTO.getContactUserPhone();
						if(!StringUtils.hasText(contactUserPhone)){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_CONTACT_USER_PHONE)});
							throw new CommonException(msg);
						} else {
							if(!ValidateUtils.length(contactUserPhone, 0, 20)){
								msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
										new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_CONTACT_USER_PHONE), IAtomsConstants.MAXLENGTH_NUMBER_TWENTY});
								throw new CommonException(msg);
							}
						}
					}
					// 聯系地址-縣市
					String contactAddressLocation = srmCaseHandleInfoDTO.getContactAddressLocation();
					if(!StringUtils.hasText(contactAddressLocation)){
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_CONTACT_ADDRESS_LOCATION)});
						throw new CommonException(msg);
					}
					// 聯系地址
					String contactAddress = srmCaseHandleInfoDTO.getContactAddress();
					if(!StringUtils.hasText(contactAddress)){
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_CONTACT_ADDRESS)});
						throw new CommonException(msg);
					} else {
						if(!ValidateUtils.length(contactAddress, 0, 100)){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_CONTACT_ADDRESS), IAtomsConstants.MAXLENGTH_NUMBER_ONE_HUNDRED});
							throw new CommonException(msg);
						}
					}
					// Task #3044 報修件「報修原因」為必填 改為 非必填
					// 如果為報修
					/*if(IAtomsConstants.CASE_CATEGORY.REPAIR.getCode().equals(command.getCaseCategory())){
						// 報修原因
						String repairReason = srmCaseHandleInfoDTO.getRepairReason();
						if(!StringUtils.hasText(repairReason)){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_REPAIR_REASON)});
							throw new CommonException(msg);
						}
					}*/
					// Task #2682 進入待結案審查，就只能改 需求單號
//					if(!isIgnoreAsset){
						// 案件類型
						String caseType = srmCaseHandleInfoDTO.getCaseType();
						if(!StringUtils.hasText(caseType)){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_CASE_TYPE)});
							throw new CommonException(msg);
						} else {
							if(IAtomsConstants.TICKET_MODE_APPOINTMENT.equals(caseType)){
								Date expectedCompletionDate = srmCaseHandleInfoDTO.getExpectedCompletionDate();
								// 預計完成日
								if(expectedCompletionDate == null){
									msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
											new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_EXPECTED_COMPLETION_DATE)});
									throw new CommonException(msg);
								}
							}
						}
//					}
				} 
				//環匯公司所做核檢
				if (IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(caseCategory)) {
					//判斷當前公司是否爲環匯
					MultiParameterInquiryContext parameterInquiryContext = new MultiParameterInquiryContext();
					parameterInquiryContext.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_ID.getValue(), customerId);
					SessionContext sessionCtx = null;
					try {
						sessionCtx = this.getServiceLocator().doService(logonUser, IAtomsConstants.SERVICE_COMPANY_SERVICE, IAtomsConstants.ACTION_GET_COMPANY_BY_COMPANY_ID, parameterInquiryContext);
					} catch (Exception e) {
						e.printStackTrace();
					}
					CompanyDTO companyDTO = (CompanyDTO) sessionCtx.getResponseResult();
					if (companyDTO != null) {
						if (IAtomsConstants.PARAM_GP.equals(companyDTO.getCompanyCode())) {
							// 刷卡機類型
							String edcTypeName = srmCaseHandleInfoDTO.getEdcTypeName();
							parameterInquiryContext = new MultiParameterInquiryContext();
							parameterInquiryContext.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CONNECTION_TYPE_NAME.getValue(), srmCaseHandleInfoDTO.getConnectionTypeName());
							parameterInquiryContext.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.BUILT_IN_FEATURE.getValue(), srmCaseHandleInfoDTO.getBuiltInFeatureName());
							parameterInquiryContext.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_NAME.getValue(), srmCaseHandleInfoDTO.getPeripheralsName());
							parameterInquiryContext.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_NAME.getValue(), srmCaseHandleInfoDTO.getPeripherals2Name());
							parameterInquiryContext.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_NAME.getValue(), srmCaseHandleInfoDTO.getPeripherals3Name());
							parameterInquiryContext.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.RECEIPT_TYPE.getValue(), srmCaseHandleInfoDTO.getReceiptType());
							parameterInquiryContext.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASETRANSACTION_PARAMETER_DTOS.getValue(), command.getSrmCaseTransactionParameterDTOs());
							parameterInquiryContext.addParameter("isUpload", Boolean.FALSE);
							//判斷刷卡機型
							if ("S80 Ethernet".equals(edcTypeName) || "S80 RF".equals(edcTypeName) || "S90 RF".equals(edcTypeName)
									|| "S90 3G".equals(edcTypeName)) {
								try {
									if ("S80 Ethernet".equals(edcTypeName) || "S80 RF".equals(edcTypeName)) {
										parameterInquiryContext.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION.getValue(), srmCaseHandleInfoDTO.getPeripheralsFunctionName());
										parameterInquiryContext.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION2.getValue(), srmCaseHandleInfoDTO.getPeripheralsFunction2Name());
										parameterInquiryContext.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION3.getValue(), srmCaseHandleInfoDTO.getPeripheralsFunction3Name());
										parameterInquiryContext.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.AO_NAME.getValue(), srmCaseHandleInfoDTO.getAoName());
										if ("S80 Ethernet".equals(edcTypeName)) {
											sessionCtx = this.getServiceLocator().doService(logonUser, IAtomsConstants.SERVICE_CASE_CHECK_INFO_SERVICE, IAtomsConstants.ACTION_CHECKS80_ETHERNET_ASSET, parameterInquiryContext);
										} else {
											sessionCtx = this.getServiceLocator().doService(logonUser, IAtomsConstants.SERVICE_CASE_CHECK_INFO_SERVICE, IAtomsConstants.ACTION_CHECKS80_RF_ASSET, parameterInquiryContext);
										}
									} else if ("S90 RF".equals(edcTypeName)) {
										sessionCtx = this.getServiceLocator().doService(logonUser, IAtomsConstants.SERVICE_CASE_CHECK_INFO_SERVICE, IAtomsConstants.ACTION_CHECKS90_RF_ASSET, parameterInquiryContext);
									} else if ("S90 3G".equals(edcTypeName)) {
										sessionCtx = this.getServiceLocator().doService(logonUser, IAtomsConstants.SERVICE_CASE_CHECK_INFO_SERVICE, IAtomsConstants.ACTION_CHECKS90_3G_ASSET, parameterInquiryContext);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								if (sessionCtx != null) {
									String message = (String) sessionCtx.getResponseResult();
									if (StringUtils.hasText(message)) {
										msg = new Message(Message.STATUS.FAILURE, message);
										throw new CommonException(msg);
									}
								}
							}
						}
					}
				}
			}
		} else if ((StringUtils.hasText(caseCategory)) && (IAtomsConstants.CASE_CATEGORY.REPAIR.equals(caseCategory))){
			//驗證問題原因、解決方式、責任歸屬的欄位檢驗
			//問題原因 -- 必填
			String problemReason = srmCaseTransactionDTO.getProblemReason();
			if(!StringUtils.hasText(problemReason)){
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_PROBLEM_REASON)});
				throw new CommonException(msg);
			}
			//解決方式 -- 必填
			String problemSolution = srmCaseTransactionDTO.getProblemSolution();
			if(!StringUtils.hasText(problemSolution)){
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK,
						 new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_PROBLEM_SOLUTION)});
				throw new CommonException(msg);
			}
			//責任歸屬 -- 必填
			String responsobity = srmCaseTransactionDTO.getResponsibity();
			if(!StringUtils.hasText(responsobity)){
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK,
						 new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_HANDLE_RESPONSIBITY)});
				throw new CommonException(msg);
			}
		} else if (IAtomsConstants.ACTION_CLOSED.equals(actionId)){
			//處理說明 -- 若有限制長度為0～200
			String description = srmCaseTransactionDTO.getDescription();
			if(StringUtils.hasText(description)){
				if(!ValidateUtils.length(description, 0, 200)){
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_CASE_TRANSACTION_DESCRIPTION), IAtomsConstants.MAXLENGTH_NUMBER_TWO_HUNDRED});
					throw new CommonException(msg);
				}
			}
		}
		long endTime = System.currentTimeMillis();
		LOGGER.debug("calculate time --> validate", "Controller actionId:" + command.getActionId() + " , costTime:" + (endTime - startTime));
		return true;
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	public CaseManagerFormDTO parse(HttpServletRequest request, CaseManagerFormDTO command) throws CommonException {
		long startTime = System.currentTimeMillis();
		try{
			// 获取actionId
			String actionId = command.getActionId();
			if (IAtomsConstants.ACTION_CREATE_CASE.equals(actionId) || IAtomsConstants.ACTION_NEW_DISPATCH.equals(actionId)
					|| "checkGpInfo".equals(actionId)) {
				Boolean flag = Boolean.valueOf(true);
				//裝機，建案，併機，等儲存操作
				SrmCaseHandleInfoDTO srmCaseHandleInfoDTO = BindPageDataUtils.bindValueObject(request, 
						SrmCaseHandleInfoDTO.class, CaseManagerFormDTO.PARAM_CASE_SUFFIX, flag);
				command.setSrmCaseHandleInfoDTO(srmCaseHandleInfoDTO);
				
				Gson gsonss = new GsonBuilder().create();
				List<SrmCaseTransactionParameterDTO> srmCaseTransactionParameterDTOs = 
						(List<SrmCaseTransactionParameterDTO>) gsonss.fromJson(
						command.getCaseTransactionParameters(), new TypeToken<List<SrmCaseTransactionParameterDTO>>(){}.getType());
				command.setSrmCaseTransactionParameterDTOs(srmCaseTransactionParameterDTOs);
			} else if (IAtomsConstants.ACTION_SAVE_FILE.equals(actionId)) {
				// 有文件上傳時，儲存文件
				if(request instanceof MultipartHttpServletRequest){	
					MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
					Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
					command.setFileMap(fileMap);
				}
			} else if (IAtomsConstants.ACTION_SAVE_CASE_TRANSACTION.equals(actionId) || IAtomsConstants.ACTION_SIGN.equals(actionId)
					|| IAtomsConstants.ACTION_CLOSED.equals(actionId) || IAtomsConstants.ACTION_DISPATCH.equals(actionId) 
					|| IAtomsConstants.ACTION_IMMEDIATELY_CLOSING.equals(actionId) || IAtomsConstants.CASE_ACTION.ONLINE_EXCLUSION.getCode().equals(actionId) 
					|| IAtomsConstants.CASE_ACTION.SIGN.getCode().equals(actionId) || IAtomsConstants.CASE_ACTION.VOID_CASE.getCode().equals(actionId)
					|| IAtomsConstants.CASE_ACTION.DELAY.getCode().equals(actionId) || IAtomsConstants.CASE_ACTION.RETREAT.getCode().equals(actionId)
					|| IAtomsConstants.CASE_ACTION.COMPLETE.getCode().equals(actionId) || IAtomsConstants.CASE_ACTION.RUSH_REPAIR.getCode().equals(actionId)
					|| IAtomsConstants.CASE_ACTION.ARRIVE.getCode().equals(actionId) || IAtomsConstants.CASE_ACTION.RESPONSE.getCode().equals(actionId) 
					|| IAtomsConstants.CASE_ACTION.CHANGE_CASE_TYPE.getCode().equals(actionId) || IAtomsConstants.CASE_ACTION.ADD_RECORD.getCode().equals(actionId)
					|| IAtomsConstants.CASE_ACTION.AUTO_DISPATCHING.getCode().equals(actionId)
					|| IAtomsConstants.CASE_ACTION.CHANGE_COMPLETE_DATE.getCode().equals(actionId)
					|| IAtomsConstants.CASE_ACTION.CHANGE_CREATE_DATE.getCode().equals(actionId)
					|| IAtomsConstants.CASE_ACTION.DISTRIBUTION.getCode().equals(actionId)
					|| "confirmAuthorizes".equals(actionId)
					|| "payment".equals(actionId)
					|| "arrivalInspection".equals(actionId)
					|| IAtomsConstants.CASE_ACTION.LEASE_PRELOAD.getCode().equals(actionId)
					|| IAtomsConstants.CASE_ACTION.LEASE_SIGN.getCode().equals(actionId)
					|| IAtomsConstants.CASE_ACTION.CANCEL_CONFIRM_AUTHORIZES.getCode().equals(actionId)) {
				// 獲取案件歷程內容
				SrmCaseTransactionDTO caseTransactionDTO = BindPageDataUtils.bindValueObject(request, SrmCaseTransactionDTO.class);
				command.setSrmCaseTransactionDTO(caseTransactionDTO);
				Gson gsonss = new GsonBuilder().create();
				List<SrmCaseAssetLinkDTO> SrmCaseAssetLinkDTO = 
						(List<SrmCaseAssetLinkDTO>) gsonss.fromJson(
						command.getCaseAssetLinkParameters(), new TypeToken<List<SrmCaseAssetLinkDTO>>(){}.getType());
				command.setSrmCaseAssetLinkDTO(SrmCaseAssetLinkDTO);
			} else if (IAtomsConstants.ACTION_UPLOAD.equals(actionId)) {
				if(request instanceof MultipartHttpServletRequest){	
					MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
					Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
					command.setFileMap(fileMap);
				}
			} else if (IAtomsConstants.ACTION_VARIABLE_EXPORT.equals(actionId)) {
				// 進件日期起
				if(StringUtils.hasText(command.getQueryCreateDateStartStr())){
					command.setQueryCreateDateStart(DateTimeUtils.toDate(command.getQueryCreateDateStartStr()));
				}
				// 進件日期迄
				if(StringUtils.hasText(command.getQueryCreateDateEndStr())){
					command.setQueryCreateDateEnd(DateTimeUtils.toDate(command.getQueryCreateDateEndStr()));
				}
				// 應完成日期起
				if(StringUtils.hasText(command.getQueryAcceptableCompleteDateStartStr())){
					command.setQueryAcceptableCompleteDateStart(DateTimeUtils.toDate(command.getQueryAcceptableCompleteDateStartStr()));
				}
				// 應完成日期迄
				if(StringUtils.hasText(command.getQueryAcceptableCompleteDateEndStr())){
					command.setQueryAcceptableCompleteDateEnd(DateTimeUtils.toDate(command.getQueryAcceptableCompleteDateEndStr()));
				}
				// 完修日期起
				if(StringUtils.hasText(command.getQueryCompleteDateStartStr())){
					command.setQueryCompleteDateStart(DateTimeUtils.toDate(command.getQueryCompleteDateStartStr()));
				}
				// 完修日期迄
				if(StringUtils.hasText(command.getQueryCompleteDateEndStr())){
					command.setQueryCompleteDateEnd(DateTimeUtils.toDate(command.getQueryCompleteDateEndStr()));
				}
				String isInstant = request.getParameter("queryDateMode");
				if(isInstant.equals(IAtomsConstants.REPORT_NAME_MONTH_REPORT)) {
					command.setIsInstant(true);
				}
				String isMicro = request.getParameter("queryMicro");
				if(IAtomsConstants.REPORT_NAME_MONTH_REPORT.equals(isMicro)) {
					command.setIsMicro(true);
				} else {
					command.setIsMicro(false);
				}
				//Task #3549
				String queryMicroArrive = request.getParameter("queryMicroArrive");
				if(IAtomsConstants.REPORT_NAME_MONTH_REPORT.equals(queryMicroArrive)) {
					command.setIsMicroArrive(true);
				} else {
					command.setIsMicroArrive(false);
				}
				String exportField = this.getString(request, CaseManagerFormDTO.EXPORT_PARAM_FIELDS);
				String[] exportFields = null;
				if(StringUtils.hasText(exportField)){
					exportFields = exportField.split(IAtomsConstants.MARK_SEPARATOR);
					command.setExportFields(exportFields);
					
					// 查詢條件map集合
					Map<String, String> queryColumnMap = new HashMap<String, String>();
					for(String tempColumn : StringUtils.toList(exportField, IAtomsConstants.MARK_SEPARATOR)){
						queryColumnMap.put(tempColumn, tempColumn);
					}
					// 設置查詢列
					command.setQueryColumnMap(queryColumnMap);
				}
			// 查詢案件資料
			} else if (IAtomsConstants.ACTION_QUERY_CASE.equals(actionId)) {
				// 進件日期起
				if(StringUtils.hasText(command.getQueryCreateDateStartStr())){
					command.setQueryCreateDateStart(DateTimeUtils.toDate(command.getQueryCreateDateStartStr()));
				}
				// 進件日期迄
				if(StringUtils.hasText(command.getQueryCreateDateEndStr())){
					command.setQueryCreateDateEnd(DateTimeUtils.toDate(command.getQueryCreateDateEndStr()));
				}
				// 應完成日期起
				if(StringUtils.hasText(command.getQueryAcceptableCompleteDateStartStr())){
					command.setQueryAcceptableCompleteDateStart(DateTimeUtils.toDate(command.getQueryAcceptableCompleteDateStartStr()));
				}
				// 應完成日期迄
				if(StringUtils.hasText(command.getQueryAcceptableCompleteDateEndStr())){
					command.setQueryAcceptableCompleteDateEnd(DateTimeUtils.toDate(command.getQueryAcceptableCompleteDateEndStr()));
				}
				// 完修日期起
				if(StringUtils.hasText(command.getQueryCompleteDateStartStr())){
					command.setQueryCompleteDateStart(DateTimeUtils.toDate(command.getQueryCompleteDateStartStr()));
				}
				// 完修日期迄
				if(StringUtils.hasText(command.getQueryCompleteDateEndStr())){
					command.setQueryCompleteDateEnd(DateTimeUtils.toDate(command.getQueryCompleteDateEndStr()));
				}
				String exportField = this.getString(request, CaseManagerFormDTO.EXPORT_PARAM_FIELDS);
				// 查詢條件map集合
				Map<String, String> queryColumnMap = new HashMap<String, String>();
				if(StringUtils.hasText(exportField)){
					for(String tempColumn : StringUtils.toList(exportField, IAtomsConstants.MARK_SEPARATOR)){
						queryColumnMap.put(tempColumn, tempColumn);
					}
				}
				// 設置查詢列
				command.setQueryColumnMap(queryColumnMap);
			}
		}catch (Exception e) {
			LOGGER.error("parse()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
		long endTime = System.currentTimeMillis();
		LOGGER.debug("calculate time --> parse", "Controller actionId:" + command.getActionId() + " , costTime:" + (endTime - startTime));
		return command;
	}
	/**
	 * Purpose:簽收
	 * @author Hermanwang
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 */
	public ModelAndView sign(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SIGN, command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("sign");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("sign()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:租賃簽收
	 * @author amandawang
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 */
	public ModelAndView leaseSign(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, "leaseSign", command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("sign");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("leaseSign()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:線上排除
	 * @author Hermanwang
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 */
	public ModelAndView onlineExclusion(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, "onlineExclusion", command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("onlineExclusion");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("onlineExclusion()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:修改案件類型
	 * @author Hermanwang
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 */
	public ModelAndView changeCaseType(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, "changeCaseType", command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("changeCaseType");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("changeCaseType()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:新增記錄
	 * @author Hermanwang
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 */
	public ModelAndView addRecord(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, "addRecord", command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("addRecord");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("addRecord()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:完修
	 * @author Hermanwang
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 */
	public ModelAndView complete(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_COMPLETE, command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("complete");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("complete()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:結案
	 * @author Hermanwang
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 */
	public ModelAndView closed(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_CLOSED, command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("closed");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("closed()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:修改實際完修時間
	 * @author CarrieDuan
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 */
	public ModelAndView changeCompleteDate(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_CHANGE_COMPLETE_DATE, command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("changeCompleteDate");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("changeCompleteDate()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:修改進件時間
	 * @author CarrieDuan
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 */
	public ModelAndView changeCreateDate(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_CHANGE_CREATE_DATE, command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("changeCreateDate");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("changeCompleteDate()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:租賃預載
	 * @author amandawang
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 */
	public ModelAndView leasePreload(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_LEASE_PRELOAD, command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("leasePreload");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("changeCompleteDate()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:立即結案
	 * @author Hermanwang
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 */
	public ModelAndView immediatelyClosing(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_IMMEDIATELY_CLOSING, command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("immediatelyClosing");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("immediatelyClosing()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:自動派工
	 * @author Hermanwang
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 */
	public ModelAndView autoDispatching(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, "autoDispatching", command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("autoDispatching");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("autoDispatching()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:派工
	 * @author Hermanwang
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 */
	public ModelAndView dispatching(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, "dispatching", command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("dispatching");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("dispatching()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:派工
	 * @author Hermanwang
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 */
	public ModelAndView dispatch(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, "dispatch", command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("dispatch");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("dispatch()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:作廢
	 * @author Hermanwang
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 */
	public ModelAndView voidCase(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, "voidCase", command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("voidCase");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("voidCase()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:回應
	 * @author Hermanwang
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 */
	public ModelAndView response(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, "response", command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("response");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("response()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:配送中
	 * @author neiljing
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 */
	public ModelAndView distribution(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, "distribution", command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("distribution");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("distribution()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:延期
	 * @author Hermanwang
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 */
	public ModelAndView delay(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, "delay", command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("delay");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("delay()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:退回
	 * @author Hermanwang
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 */
	public ModelAndView retreat(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, "retreat", command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("retreat");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("retreat()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:存修
	 * @author Hermanwang
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 */
	public ModelAndView rushRepair(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, "rushRepair", command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("rushRepair");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("rushRepair()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:到場
	 * @author Hermanwang
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 */
	public ModelAndView arrive(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, "arrive", command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("arrive");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("arrive()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:求償
	 * @author CarrieDuan
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 */
	public ModelAndView payment(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, "payment", command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("payment");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("payment()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:求償
	 * @author CarrieDuan
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 */
	public ModelAndView arrivalInspection(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, "arrivalInspection", command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				LOGGER.debug("arrivalInspection() doservice success...");
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("arrivalInspection");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("arrivalInspection()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:上傳文件，匯入案件資料，首先判斷匯入的資料是否符合要求，如不符合則提示用戶下載錯誤文件。符合則批量建案（異步的方式）
	 * @author evanliu
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return void
	 */
	public ModelAndView upload(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			//上傳文件，驗證上傳文件的資料
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_UPLOAD, command);
			Message msg = sessionContext.getReturnMessage();
			/*if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.CASE_IMPORT_ASYNCHRONOUS_HANDLE);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}*/
			Map map = null;
			Object object = sessionContext.getAttribute(IAtomsConstants.PARAM_ACTION_RESULT);
			if (object != null) {
				if (object instanceof Map) {
					map = (Map) object;						
				} else {
					map = new HashMap();
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
				}
			} else {
				map = new HashMap();
			}
			if (msg != null && StringUtils.hasText(msg.getCode())) {
				if (msg.getStatus() == Message.STATUS.SUCCESS) {
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.TRUE);
				} else {
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
				}
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(msg.getCode(), msg.getArguments(), null));
			}
			return new ModelAndView(new MappingJacksonJsonView(), map); 
		} catch (Exception e) {
			LOGGER.error("upload()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	
	/**
	 * Purpose: 下載上傳案件附加資料
	 * @author CrissZhang
	 * @param request: 請求對象
	 * @param response: 響應對象 
	 * @param command: formDTO
	 * @throws CommonException: 出錯時拋出CommonException
	 * @return void
	 */
	public void download(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_CHECK_CASE_ATT_FILE, command);
			if (sessionContext != null) {
				command =  (CaseManagerFormDTO) sessionContext.getResponseResult();
				String fileRealName = command.getFileName();
				// 下載
			//	FileUtils.download(request,response, command.getFilePath(), null);
				FileUtils.download(request,response, command.getFilePath(), fileRealName);
			}
		} catch (Exception e) {
			LOGGER.error("download()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	
	
	/**
	 * 
	 * Purpose: 案件管理列印清單匯出
	 * @author Hermanwang
	 * @param request 請求對象
	 * @param response 響應對象
	 * @param command formDTO
	 * @throws CommonException 異常類
	 * @return void
	 */
	@SuppressWarnings({ "rawtypes"})
	public ModelAndView export(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command) throws CommonException{
		// 取得sessionId
		String sessionId = request.getSession().getId();
		try{
			command.setCaseId(URLDecoder.decode(command.getCaseId(), IAtomsConstants.ENCODE_UTF_8));
			command.setIsExport(true);
			command.setQueryColumnMap(getQueryColumnMap());
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE,IAtomsConstants.ACTION_GET_CASE_MANAGER_LIST_BY_CASE_ID, command);
			if(sessionContext != null){
				command = (CaseManagerFormDTO) sessionContext.getResponseResult();
				List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOList = command.getSrmCaseHandleInfoDTOs();
				String templatesName = command.getTemplatesName();
				sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_GET_CATEGORY_BY_TEMPLATES_ID, command);
				command =  (CaseManagerFormDTO) sessionContext.getResponseResult();
				String category = command.getCategoryId();
				String filePath = command.getPath();
				
				// 得到交易參數可編輯的字段
				MultiParameterInquiryContext multiParameterInquiryContext = new MultiParameterInquiryContext();
				String versionDate = DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDD_DASH);
				multiParameterInquiryContext.addParameter(SrmTransactionParameterItemDTO.ATTRIBUTE.EFFECTIVE_DATE.getValue(), versionDate);
				sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE,
						IAtomsConstants.ACTION_GET_EDIT_FIELDS_GROUP_BY_TRANSACTION_TYPE, multiParameterInquiryContext);
				// 可編輯字段的map集合
				Map<String,List<String>> editFildsMap = (Map<String,List<String>>) sessionContext.getResponseResult();
				//案件資訊
				for(SrmCaseHandleInfoDTO srmCaseHandleInfoDTO :srmCaseHandleInfoDTOList) {
					//map = new HashMap<String, String>();
					command.setCaseId(srmCaseHandleInfoDTO.getCaseId());
					//原案件交易參數的信息
					sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_GET_CASE_TRANSACTION_PARAMETER_BY_CASEID, command);
					command =  (CaseManagerFormDTO) sessionContext.getResponseResult();
					List<SrmCaseTransactionParameterDTO> srmCaseTransactionParameterDTOList = command.getSrmCaseTransactionParameterDTOs();
					//案件歷史記錄
					//sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_GET_CASE_TRANSACTIONDTO_BY_CASE_ID, command);
					//command =  (CaseManagerFormDTO) sessionContext.getResponseResult();
					//List<SrmCaseTransactionDTO> srmCaseTransactionDTOList = command.getSrmCaseTransactionDTOList();
					//交易參數，可能有多筆
					for(SrmCaseTransactionParameterDTO srmCaseTransactionParameterDTO : srmCaseTransactionParameterDTOList) {
						String itemValue = srmCaseTransactionParameterDTO.getItemValue();
						srmCaseTransactionParameterDTO.setDTID(srmCaseHandleInfoDTO.getDtid());
						Gson gsonss = new GsonBuilder().create();
						//將item_value轉化為map，存在交易參數dto裡面
						Map<String, String> srmCaseTransactionParametermap = (Map<String, String>) gsonss.fromJson(
										itemValue, new TypeToken<Map<String, String>>(){}.getType());
						srmCaseTransactionParameterDTO.setSrmCaseTransactionParametermap(srmCaseTransactionParametermap);
					}
					// Task #2721 所有類別都要能列印 無交易參數也可列印
					if(CollectionUtils.isEmpty(srmCaseTransactionParameterDTOList)){
						srmCaseTransactionParameterDTOList.add(new SrmCaseTransactionParameterDTO());
					}
					srmCaseHandleInfoDTO.setCaseTransactionParameterDTOs(srmCaseTransactionParameterDTOList);
				}
				String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD);
				String tempPath = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH) + File.separator 
						 + yearMonthDay + File.separator + IAtomsConstants.UC_NO_SRM_05010 + File.separator + category + File.separator + File.separator;
				FileInputStream fileInputStream = null;
				OutputStream outputStream = null;
				String fileName = templatesName;
				int x=(int)(Math.random()*10000);
				try {
		            FileUtils.copyFile(filePath + templatesName, tempPath, templatesName);
					//列印
		            POIUtils pOIUtils = new POIUtils();
		            //產生隨機數給臨時文件拼接文件名
		            //Task #3591 當交易參數多于9條時，請將 列印工單-案件資訊 分兩頁匯出
		            if ("1".equals(category)) {
			            pOIUtils.createExcelDynamic(templatesName, tempPath, srmCaseHandleInfoDTOList, editFildsMap, x);
					} else {
			            pOIUtils.createExcel(templatesName, tempPath, srmCaseHandleInfoDTOList, editFildsMap, x);
					}
					File file = new File(tempPath + File.separator + x+CaseManagerFormDTO.CASE_EXPORT_TEMPLATE_NAME_FOR_EN);
					// 模板路径
					//輸入流
					fileInputStream = new FileInputStream(file);
					FileUtils.download(request, response, fileInputStream, templatesName);
					// 成功標志
					SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, IAtomsConstants.YES);
				} catch (Exception e) {
					LOGGER.error(".downloadErrorFile() Exception:", e);
				} finally {
					fileName=new String((x+CaseManagerFormDTO.CASE_EXPORT_TEMPLATE_NAME_FOR_EN).getBytes("ISO-8859-1"),"utf-8");
					String inputfile = tempPath + File.separator + fileName;
					FileUtils.removeFile(inputfile);
					try {
						if(fileInputStream != null) {
							fileInputStream.close();
						}
						if(outputStream != null) {
							outputStream.flush();
							outputStream.close();
						}
					} catch (Exception e) {
						LOGGER.error(".downloadErrorFile() Exception:", e);
					}
				}
				
			}
		}catch(Exception e){
			// 出錯消息
			try {
				SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, false);
				Map map = new HashMap();
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.EXPORT_ERROR_MESSAGE));
				return new ModelAndView(this.getFailureView(IAtomsConstants.ACTION_EXPORT), map);
			} catch (Exception e1) {
				LOGGER.error(".export() is error:", e1);
			}
			if(LOGGER != null){
				LOGGER.error("export()", "error", e);
			}
			throw new CommonException(IAtomsMessageCode.EXPORT_REPORT_FAILURE);
		}
		return null;
	}
	/**
	 * Purpose:案件匯入-下載匯入格式範本
	 * @author CarrieDuan
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return void
	 */
	public void exportTemplate(HttpServletRequest request, HttpServletResponse response, CaseManagerFormDTO command)throws CommonException{
		try {
			String caseCategory = command.getCaseCategory();
			if (StringUtils.hasText(caseCategory)) {
				//模板名稱
				String fileName = importFilePath.get(caseCategory);
				//中文名稱
				String fileNameCN = null;
				//交易參數動態列起始位置
				Integer startIndex = 0;
				if (IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(caseCategory)) {
					startIndex = 2;
					fileNameCN = i18NUtil.getName(CaseManagerFormDTO.PARAM_CASE_IMPORT_TEMPLATE_INSTALL_CH).concat(IAtomsConstants.MARK_NO).concat(IAtomsConstants.FILE_TXT_MSEXCEL);
				} else if (IAtomsConstants.CASE_CATEGORY.MERGE.getCode().equals(caseCategory)) {
					startIndex = 3;
					fileNameCN = i18NUtil.getName(CaseManagerFormDTO.PARAM_CASE_IMPORT_TEMPLATE_MERGE_CH).concat(IAtomsConstants.MARK_NO).concat(IAtomsConstants.FILE_TXT_MSEXCEL);
				} else if (IAtomsConstants.CASE_CATEGORY.UPDATE.getCode().equals(caseCategory)) {
					startIndex = 3;
					fileNameCN = i18NUtil.getName(CaseManagerFormDTO.PARAM_CASE_IMPORT_TEMPLATE_UPDATE_CH).concat(IAtomsConstants.MARK_NO).concat(IAtomsConstants.FILE_TXT_MSEXCEL);
				} else if (IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode().equals(caseCategory)) {
					fileNameCN = i18NUtil.getName(CaseManagerFormDTO.PARAM_CASE_IMPORT_TEMPLATE_UNINSTALL_CH).concat(IAtomsConstants.MARK_NO).concat(IAtomsConstants.FILE_TXT_MSEXCEL);
				} else if (IAtomsConstants.CASE_CATEGORY.CHECK.getCode().equals(caseCategory)) {
					fileNameCN = i18NUtil.getName(CaseManagerFormDTO.PARAM_CASE_IMPORT_TEMPLATE_CHECK_CH).concat(IAtomsConstants.MARK_NO).concat(IAtomsConstants.FILE_TXT_MSEXCEL);
				} else if (IAtomsConstants.CASE_CATEGORY.PROJECT.getCode().equals(caseCategory)) {
					startIndex = 3;
					fileNameCN = i18NUtil.getName(CaseManagerFormDTO.PARAM_CASE_IMPORT_TEMPLATE_PROJECT_CH).concat(IAtomsConstants.MARK_NO).concat(IAtomsConstants.FILE_TXT_MSEXCEL);
				} else if (IAtomsConstants.CASE_CATEGORY.REPAIR.getCode().equals(caseCategory)) {
					fileNameCN = i18NUtil.getName(CaseManagerFormDTO.PARAM_CASE_IMPORT_TEMPLATE_REPAIR_CH).concat(IAtomsConstants.MARK_NO).concat(IAtomsConstants.FILE_TXT_MSEXCEL);
				} 
				MultiParameterInquiryContext inquiryContext = new MultiParameterInquiryContext();
				String versionDate = DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDD_DASH);
				inquiryContext.addParameter(SrmTransactionParameterItemDTO.ATTRIBUTE.EFFECTIVE_DATE.getValue(), versionDate);
				SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_GET_TRANSACTION_PARAMETER_ITEM_LIST, inquiryContext);
				// 模板路径
				String tempPath = IAtomsConstants.TEMPLATE_DOWNLOAD_PATH;
				String tempFailString = tempPath.concat(fileName);
				if (sessionContext != null) {
					InputStream inputStream = CaseManagerService.class.getResourceAsStream(tempFailString); 
					//創建臨時路徑
					String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS);
					StringBuffer tempPathBuffer = new StringBuffer();
					tempPathBuffer.append(SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH));
					tempPathBuffer.append(File.separator).append(yearMonthDay).append(File.separator).append(IAtomsConstants.PARAM_STRING_IMPORT);
					File fileFord = new File(tempPathBuffer.toString());
					//判斷儲存路徑是否存在，若不存在，則重新新建
					if (!fileFord.exists() || !fileFord.isDirectory()) {
						fileFord.mkdirs();
					}
					StringBuffer filePathBuffer = new StringBuffer();
					filePathBuffer.append(tempPathBuffer.toString()).append(File.separator).append(fileName);
					OutputStream outputStream = new FileOutputStream(new File(filePathBuffer.toString()));
					byte[] buffer = new byte[1024];
					//开始向网络传输文件流    
					int len = -1;
					while ((len = inputStream.read(buffer)) != -1) {    
						outputStream.write(buffer, 0, len);
					}
					outputStream.flush();
					outputStream.close();
					List<SrmTransactionParameterItemDTO> result = (List<SrmTransactionParameterItemDTO>) sessionContext.getResponseResult();
					List<String> paramterItemName = new ArrayList<String>();
					Integer i = 0;
					List<Integer> index = new ArrayList<Integer>();
					for (SrmTransactionParameterItemDTO srmTransactionParameterItemDTO : result) {
						//map.put(i++, srmTransactionParameterItemDTO.getParamterItemName());
						paramterItemName.add(srmTransactionParameterItemDTO.getParamterItemName());
						if ("combobox".equals(srmTransactionParameterItemDTO.getParamterItemType())) {
							index.add(i);
						}
						i++;
					}
					//map = sortMapByKey(map);
					if (IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(caseCategory)
							|| IAtomsConstants.CASE_CATEGORY.UPDATE.getCode().equals(caseCategory)
							|| IAtomsConstants.CASE_CATEGORY.MERGE.getCode().equals(caseCategory)
							|| IAtomsConstants.CASE_CATEGORY.PROJECT.getCode().equals(caseCategory)) {
						POIUtils.createExcel(filePathBuffer.toString(), paramterItemName, index, null, startIndex);
					}
					InputStream inputStream2 = new FileInputStream(new File(filePathBuffer.toString()));
					FileUtils.download(request, response, inputStream2, fileNameCN);
					FileUtils.removeFile(filePathBuffer.toString());
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception----download()", e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	
	/**
	 * Purpose:案件匯入-下載錯誤文檔
	 * @author CarrieDuan
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return void
	 */
	public void downloadErrorFile(HttpServletRequest request, HttpServletResponse response, CaseManagerFormDTO command) throws CommonException{
		try {
			//下載错误信息文件
			String fileName = this.getString(request, CaseManagerFormDTO.ERROR_FILE_NAME);
			String tempPath = this.getString(request, CaseManagerFormDTO.ERROR_FILE_PATH);
			String inputfile = tempPath.concat(IAtomsConstants.MARK_BACKSLASH).concat(fileName);
			FileUtils.download(request, response, inputfile, IAtomsConstants.UPLOAD_ERROR_MESSAGE_FILE_NAME);
			FileUtils.removeFile(inputfile);
		} catch (Exception e) {
			LOGGER.error(".downloadErrorFile() Exception:", e);
		}
		
	}
	/**
	 * Purpose:交叉報表匯出刷卡機參數（工單列印）
	 * @author HermanWang
	 * @param request 請求對象
	 * @param response 響應
	 * @param command
	 * @throws CommonException 異常類
	 * @return void
	 */
	public ModelAndView exportCardReaderParam(HttpServletRequest request, HttpServletResponse response, CaseManagerFormDTO command) throws CommonException {
		// 取得sessionId
		String sessionId = request.getSession().getId();
		try {
			String caseId = URLDecoder.decode(command.getCaseId(), IAtomsConstants.ENCODE_UTF_8); 
			command.setCaseId(caseId);
			command.setIsExport(false);
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE,IAtomsConstants.ACTION_GET_CASE_MANAGER_LIST_BY_CASE_ID, command);
			if(sessionContext != null){
				command = (CaseManagerFormDTO) sessionContext.getResponseResult();
				List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOList = command.getSrmCaseHandleInfoDTOs();
				// 得到交易參數可編輯的字段
				MultiParameterInquiryContext multiParameterInquiryContext = new MultiParameterInquiryContext();
				String versionDate = DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDD_DASH);
				multiParameterInquiryContext.addParameter(SrmTransactionParameterItemDTO.ATTRIBUTE.EFFECTIVE_DATE.getValue(), versionDate);
				sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE,
						IAtomsConstants.ACTION_GET_EDIT_FIELDS_GROUP_BY_TRANSACTION_TYPE, multiParameterInquiryContext);
				// 可編輯字段的map集合
				Map<String,List<String>> editFildsMap = (Map<String,List<String>>) sessionContext.getResponseResult();
				//command = (CaseManagerFormDTO) sessionContext.getResponseResult();
				// 案件交易參數配置檔
				List<SrmTransactionParameterItemDTO> srmTransactionParameterItemDTOs = null;
				// 報表所需參數map集合
				Map map = new HashMap();
				JasperReportCriteriaDTO criteria = new JasperReportCriteriaDTO();
				criteria.setParameters(map);
				// 交叉報表對象集合
				List<CrossTabReportDTO> crossTabReportDTOs = null;
				// 交叉報表對象
				CrossTabReportDTO tempCrossTabReportDTO = null;
				// 交叉報表放置參數的全部結果集合
				Map<String, Map<String, String>> resultMap = null;
				// 交叉報表放置參數的臨時單個結果集合
				Map<String, String> tempMap = null;
				Gson gson = new Gson();
				// 存放交易參數轉換後的map對象
				Map<String, Object> jsonMap = null;
				StringBuilder builder = new StringBuilder();
				//Map titleNameMap = new HashMap();
				// 給每筆案件增加交易參數
				if(srmCaseHandleInfoDTOList != null) {
					//案件資訊(選取的行)
					for(SrmCaseHandleInfoDTO srmCaseHandleInfoDTO :srmCaseHandleInfoDTOList) {
						command.setCaseId(srmCaseHandleInfoDTO.getCaseId());
						//原案件交易參數的信息
						sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_GET_CASE_TRANSACTION_PARAMETER_BY_CASEID, command);
						command =  (CaseManagerFormDTO) sessionContext.getResponseResult();
						List<SrmCaseTransactionParameterDTO> srmCaseTransactionParameterDTOList = command.getSrmCaseTransactionParameterDTOs();	
							// 得到配置檔
							srmTransactionParameterItemDTOs = srmCaseHandleInfoDTO.getSrmTransactionParameterItemDTOs();
							// 得到這筆案件之交易參數
							//caseNewTransactionParameterDTOs = srmCaseHandleInfoDTO.getCaseNewTransactionParameterDTOs();
							// Task #2721 所有類別都要能列印 無交易參數也可列印
							boolean isEmpty = false;
							if(CollectionUtils.isEmpty(srmCaseTransactionParameterDTOList)){
								isEmpty = true;
								srmCaseTransactionParameterDTOList.add(new SrmCaseTransactionParameterDTO());
							}
							if(!CollectionUtils.isEmpty(srmCaseTransactionParameterDTOList)){
								resultMap = new LinkedHashMap<String, Map<String,String>>();
								// 每筆案件交易參數集合
								crossTabReportDTOs = new ArrayList<CrossTabReportDTO>();
								//區別交易參數是不是改案件下的交易參數.
								String flag = IAtomsConstants.MARK_EMPTY_STRING;
								//交易類別 一般交易三種，只能算一種
								for(int j = 0; j < editFildsMap.size()-2;j++){
									SrmCaseTransactionParameterDTO srmCaseTransactionParameterDTO = null;
									// 交易參數可編輯字段
									String tempEditFilds = null;
									//本案件下的交易參數筆數小於總的交易參數筆數
									if(j < srmCaseTransactionParameterDTOList.size()) {
										// Task #2721 所有類別都要能列印 無交易參數也可列印
										if(isEmpty){
											flag = IAtomsConstants.COLUMN_W + IAtomsConstants.COLUMN_W + IAtomsConstants.COLUMN_W;
										} else {
											flag = IAtomsConstants.COLUMN_V + IAtomsConstants.COLUMN_V + IAtomsConstants.COLUMN_V;
										}
										srmCaseTransactionParameterDTO = srmCaseTransactionParameterDTOList.get(j);
										srmCaseTransactionParameterDTO.setDTID(srmCaseHandleInfoDTO.getDtid());
										// 得到每項交易參數可編輯字段
										// Task #2721 所有類別都要能列印 無交易參數也可列印
										if(StringUtils.hasText(srmCaseTransactionParameterDTO.getTransactionType())){
											tempEditFilds = editFildsMap.get(srmCaseTransactionParameterDTO.getTransactionType()).toString();
										}
										if(StringUtils.hasText(srmCaseTransactionParameterDTO.getItemValue())){
											jsonMap = new HashMap<String, Object>();
											// 將item_value中交易參數轉爲map集合對象
											jsonMap = gson.fromJson(srmCaseTransactionParameterDTO.getItemValue(), jsonMap.getClass());
										}
									//本案件下的交易參數已經循環完，給空白列添加數據
									} else {
										flag = IAtomsConstants.COLUMN_W + IAtomsConstants.COLUMN_W + IAtomsConstants.COLUMN_W;
										srmCaseTransactionParameterDTO = new SrmCaseTransactionParameterDTO();
									} 
									tempMap = new LinkedHashMap<String, String>();
									int i = 0;
									for(SrmTransactionParameterItemDTO srmTransactionParameterItemDTO : srmTransactionParameterItemDTOs){
										if(StringUtils.hasText(tempEditFilds)){
											if(i == 0) {
												tempMap.put(i18NUtil.getName(CaseManagerFormDTO.TRANSACTION_ITEM), srmCaseTransactionParameterDTO.getTransactionTypeName());										
											}
											// 放置特店代號
											if(srmTransactionParameterItemDTO.getParamterItemCode().equals(IAtomsConstants.PARAMTER_ITEM_CODE_MERCHANT_CODE)){
												if(tempEditFilds.contains(srmTransactionParameterItemDTO.getParamterItemCode())){
													tempMap.put(srmTransactionParameterItemDTO.getParamterItemName(), srmCaseTransactionParameterDTO.getMerchantCode());
												} else {
													tempMap.put(srmTransactionParameterItemDTO.getParamterItemName(), IAtomsConstants.MARK_SPACE);
												}
												// 放置分期特店代號
											} else if(srmTransactionParameterItemDTO.getParamterItemCode().equals(IAtomsConstants.PARAMTER_ITEM_CODE_MERCHANT_CODE_OTHER)){
												if(tempEditFilds.contains(srmTransactionParameterItemDTO.getParamterItemCode())){
													tempMap.put(srmTransactionParameterItemDTO.getParamterItemName(), srmCaseTransactionParameterDTO.getMerchantCodeOther());
												} else {
													tempMap.put(srmTransactionParameterItemDTO.getParamterItemName(), IAtomsConstants.MARK_SPACE);
												}
												// 放置dtid
											} else if(srmTransactionParameterItemDTO.getParamterItemCode().equals(IAtomsConstants.PARAMTER_ITEM_CODE_DTID)){
												if(tempEditFilds.contains(srmTransactionParameterItemDTO.getParamterItemCode())){
													tempMap.put(srmTransactionParameterItemDTO.getParamterItemName(), srmCaseTransactionParameterDTO.getDTID());
												} else {
													tempMap.put(srmTransactionParameterItemDTO.getParamterItemName(), IAtomsConstants.MARK_SPACE);
												}
												// 放置tid
											}else if(srmTransactionParameterItemDTO.getParamterItemCode().equals(IAtomsConstants.PARAMTER_ITEM_CODE_TID)){
												tempMap.put(IAtomsConstants.PARAMTER_ITEM_CODE_DTID, srmCaseTransactionParameterDTO.getDTID());
												if(tempEditFilds.contains(srmTransactionParameterItemDTO.getParamterItemCode())){
													tempMap.put(srmTransactionParameterItemDTO.getParamterItemName(), srmCaseTransactionParameterDTO.getTid());
												} else {
													tempMap.put(srmTransactionParameterItemDTO.getParamterItemName(), IAtomsConstants.MARK_SPACE);
												}
											} else {
												// 處理可編輯的字段
												if(tempEditFilds.contains(srmTransactionParameterItemDTO.getParamterItemCode())){
													// 編輯字段存與item_value
													if(!CollectionUtils.isEmpty(jsonMap)){
														if(jsonMap.containsKey(srmTransactionParameterItemDTO.getParamterItemCode())) {
															tempMap.put(srmTransactionParameterItemDTO.getParamterItemName(), (String) jsonMap.get(srmTransactionParameterItemDTO.getParamterItemCode()));
														} else {
															tempMap.put(srmTransactionParameterItemDTO.getParamterItemName(), (String) jsonMap.get(IAtomsConstants.MARK_EMPTY_STRING));
														}
													// 編輯字段未存
													} else {
														tempMap.put(srmTransactionParameterItemDTO.getParamterItemName(), IAtomsConstants.MARK_SPACE);
													}
												// 處理禁用字段
												} else {
													tempMap.put(srmTransactionParameterItemDTO.getParamterItemName(), IAtomsConstants.MARK_SPACE);
												}
											}
											i++;
										} else {
											// Task #2721 所有類別都要能列印 無交易參數也可列印
											if(isEmpty){
												tempMap.put(srmTransactionParameterItemDTO.getParamterItemName(), IAtomsConstants.MARK_EMPTY_STRING);
											} else {
												continue;
											}
//											continue;
										}
									}
									// 將交易項對應結果存入map
									resultMap.put(IAtomsConstants.COLUMN_V + (char) (j+64) + flag + srmCaseTransactionParameterDTO.getTransactionTypeName(), tempMap);
								}
							}
							//給列名稱前面拼ijk，來使用此排序。（交叉報表不會自動排序.）
							// 處理結果map
							if(!CollectionUtils.isEmpty(resultMap)){
								tempMap = new LinkedHashMap<String, String>();
								int i;
								int j;
								int k;
								// Task #2721 所有類別都要能列印 無交易參數也可列印
								int count =0;
								// 遍歷結果集合
								for(String resultMapKey : resultMap.keySet()){
									i = 0;
									j = 0;
									k = 0;
									if(!resultMapKey.substring(resultMapKey.length()-4).equals("null")) {
										tempMap = resultMap.get(resultMapKey);
									} else {
										// Task #2721 所有類別都要能列印 無交易參數也可列印
										if(isEmpty && count == 0){
											tempMap = resultMap.get(resultMapKey);
											count ++;
										}
									}
									for(String tempMapKey : tempMap.keySet()){
										builder.delete(0, builder.length());
										i ++;
										tempCrossTabReportDTO = new CrossTabReportDTO();
										// 放置行名稱
										tempCrossTabReportDTO.setRowName(resultMapKey);
										// 放置列名稱
										builder.append(k).append(j).append(i).append(tempMapKey);
										tempCrossTabReportDTO.setColumnName(builder.toString());
										if(i == 9){
											i = -1;
											j ++;
											if(j == 9){
												j = 0;
												k ++;
											}
										}
										// 放置列對應值
										if(resultMapKey.substring(resultMapKey.length()-4).equals("null")){
											tempCrossTabReportDTO.setContent(IAtomsConstants.MARK_EMPTY_STRING);
										} else {
											tempCrossTabReportDTO.setContent(tempMap.get(tempMapKey));
										}
										crossTabReportDTOs.add(tempCrossTabReportDTO);
									}
								}
								// 放置交叉報表集合
								srmCaseHandleInfoDTO.setCrossTabReportDTOs(crossTabReportDTOs);
							}
						}
					// 是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
					criteria.setAutoBuildJasper(false);
					//子報表的map
					Map<String, String> subjrXmlNames = new HashMap<String, String>();
					subjrXmlNames.put(CaseManagerFormDTO.PROJECT_SUB_REPORT_JRXML_NAME, CaseManagerFormDTO.SUBREPORT_DIR);
					criteria.setResult(srmCaseHandleInfoDTOList);
					//設置所需報表的Name
					criteria.setJrxmlName(CaseManagerFormDTO.PROJECT_REPORT_JRXML_NAME);
					//設置報表路徑
					criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
					//設置匯出格式
					criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
					//設置報表Name
					criteria.setReportFileName(i18NUtil.getName(CaseManagerFormDTO.PROJECT_REPORT_FILE_NAME));
					criteria.setSheetName(i18NUtil.getName(CaseManagerFormDTO.PROJECT_REPORT_FILE_NAME));
					//criteria.setParameters(titleNameMap);
					ReportExporter.exportMainAndSubReport(criteria, subjrXmlNames, response);
					// 成功標志
					SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, IAtomsConstants.YES);
				}
			}
		} catch (ServiceException e) {
			// 出錯消息
			try {
				SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, false);
				Map map = new HashMap();
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.EXPORT_ERROR_MESSAGE));
				return new ModelAndView(this.getFailureView("exportCardReaderParam"), map);
			} catch (Exception e1) {
				LOGGER.error(".export() is error:", e1);
			}
			LOGGER.error("exportCardReaderParam()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.EXPORT_REPORT_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("exportCardReaderParam()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return null;
	}
	/**
	 * Purpose:案件匯出（可變列）
	 * @author HermanWang
	 * @param request：請求對象
	 * @param response：響應
	 * @param command
	 * @throws CommonException：異常類
	 * @return void
	 */
	public ModelAndView variableExport(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command) throws CommonException{
		// 取得sessionId
		String sessionId = request.getSession().getId();
		try {
			command.setQueryCompanyId(command.getExportQueryCompanyId());
			if(StringUtils.hasText(command.getQueryWarningSla())) {
				command.setQueryWarningSla(IAtomsConstants.YES);
			} else {
				command.setQueryWarningSla(IAtomsConstants.NO);
			}
			String pathString = this.getServletContext().getRealPath("/");
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE,IAtomsConstants.ACTION_QUERY_CASE, command);
			if(sessionContext != null) {
				Object object = sessionContext.getAttribute(IAtomsConstants.PARAM_ACTION_RESULT);
				Map map = null;
				if (object != null) {
					if (object instanceof Map) {
						map = (Map) object;	
						String[] exportField = command.getExportFields();
						int i = 0;
						for (String field : exportField) {
							if ("confirmAuthorizes".equals(field) || "cmsCase".equals(field)) {
								i++;
							}
						}
						String[] tempExportField = new String[exportField.length - i];
						i = 0;
						for (String field : exportField) {
							if (!"confirmAuthorizes".equals(field) && !"cmsCase".equals(field)) {
								tempExportField[i++] = field;
							}
						}
						List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOs = (List<SrmCaseHandleInfoDTO>) map.get(IAtomsConstants.PARAM_QUERY_RESULT_ROWS);
						String templatesName = "";
						String tempPath = "";
						FileInputStream fileInputStream = null;
						OutputStream outputStream = null;
						try {
							//poi創建excel，並且填充值
							POIUtils.createExcel(tempExportField, srmCaseHandleInfoDTOs, pathString);
							String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD);
							tempPath = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH) + File.separator 
									+ yearMonthDay;
							templatesName = CaseManagerFormDTO.CASE_EXPORT_TEMPLATE_NAME_FOR_EN;
							File file = new File(tempPath + File.separator + templatesName);
							//POIUtils.main111(tempPath, templatesName, pathString);
							//下載
							// 模板名称
							String fileNameCn = CaseManagerFormDTO.CASE_EXPORT_TEMPLATE_NAME_FOR_CN;
							// 模板路径
							//輸入流
							fileInputStream = new FileInputStream(file);
							FileUtils.download(request, response, fileInputStream, fileNameCn);
							// 成功標志
							SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, IAtomsConstants.YES);
						} catch (Exception e) {
							LOGGER.error(".downloadErrorFile() Exception:", e);
						} finally {
							templatesName=new String(CaseManagerFormDTO.CASE_EXPORT_TEMPLATE_NAME_FOR_EN.getBytes("ISO-8859-1"),"utf-8");
							String inputfile = tempPath + File.separator + templatesName;
							FileUtils.removeFile(inputfile);
							try {
								if(fileInputStream != null) {
									fileInputStream.close();
								}
								if(outputStream != null) {
									outputStream.flush();
									outputStream.close();
								}
							} catch (Exception e) {
								LOGGER.error(".downloadErrorFile() Exception:", e);
							}
						}
					}
				}
			}
		} catch (ServiceException e) {
			// 出錯消息
			try {
				SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, false);
				Map map = new HashMap();
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.EXPORT_ERROR_MESSAGE));
				return new ModelAndView(this.getFailureView("variableExport"), map);
			} catch (Exception e1) {
				LOGGER.error(".exportAsset() is error:", e1);
			}
			LOGGER.error("variableExport()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.EXPORT_REPORT_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("variableExport()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return null;
	}
	
	/**
	 * Purpose:得到查詢列集合
	 * @author CrissZhang
	 * @return Map<String,String>
	 */
	private Map<String, String> getQueryColumnMap(){
		// 查詢條件map集合
		Map<String, String> queryColumnMap = new HashMap<String, String>();
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_TYPE_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_TYPE_NAME.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALL_TYPE_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALL_TYPE_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.IS_PROJECT.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.IS_PROJECT.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.PROJECT_CODE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.PROJECT_CODE.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.PROJECT_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.PROJECT_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.REPAIR_REASON_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.REPAIR_REASON_NAME.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.SAME_INSTALLED.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.SAME_INSTALLED.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.OLD_MERCHANT_CODE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.OLD_MERCHANT_CODE.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE_NAME.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.HEADER_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.HEADER_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.EXPECTED_COMPLETION_DATE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.EXPECTED_COMPLETION_DATE.getValue());
		// Task #3081 列印顯示應完修時間
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_FINISH_DATE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_FINISH_DATE.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.AREA_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.AREA_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.IS_VIP.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.IS_VIP.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_TEL.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_TEL.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_TEL2.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_TEL2.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_EMAIL.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_EMAIL.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.PHONE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.PHONE.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_PHONE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_PHONE.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_MOBILE_PHONE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_MOBILE_PHONE.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_EMAIL.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_EMAIL.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.BUSINESS_HOURS.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.BUSINESS_HOURS.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.AO_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.AO_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.AO_EMAIL.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.AO_EMAIL.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.BUSINESS_ADDRESS.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.BUSINESS_ADDRESS.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_PHONE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_PHONE.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_MOBILE_PHONE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_MOBILE_PHONE.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_EMAIL.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_EMAIL.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.APPLICATION_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.APPLICATION_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.OPEN_FUNCTION_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.OPEN_FUNCTION_NAME.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.MULTI_MODULE_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.MULTI_MODULE_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION_NAME.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.ECR_CONNECTION_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.ECR_CONNECTION_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION2_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION2_NAME.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CONNECTION_TYPE_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONNECTION_TYPE_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION3_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION3_NAME.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.LOGO_STYLE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.LOGO_STYLE.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.IS_OPEN_ENCRYPT.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.IS_OPEN_ENCRYPT.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_PAY_PLATFORM.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_PAY_PLATFORM.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_INVOICE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_INVOICE.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_QUICK_PASS.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_QUICK_PASS.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.LOCALHOST_IP.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.LOCALHOST_IP.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.NET_VENDOR_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.NET_VENDOR_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.GATEWAY.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.GATEWAY.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.NETMASK.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.NETMASK.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.DESCRIPTION.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.DESCRIPTION.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.TMS_PARAM_DESC.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.TMS_PARAM_DESC.getValue());
		
		
		
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_DATE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_DATE.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.TID.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.TID.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_SERIAL_NUMBER.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_SERIAL_NUMBER.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.PROPERTY_ID.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.PROPERTY_ID.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_SERIAL_NUMBER.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_SERIAL_NUMBER.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_SERIAL_NUMBER.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_SERIAL_NUMBER.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_SERIAL_NUMBER.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_SERIAL_NUMBER.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.LOGISTICS_VENDOR.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.LOGISTICS_VENDOR.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.LOGISTICS_NUMBER.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.LOGISTICS_NUMBER.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.RECEIPT_TYPE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.RECEIPT_TYPE.getValue());
		return queryColumnMap;
	}	
	/**
	 * Purpose:結案汇入
	 * @author amandawang
	 * @param request ：请求
	 * @param response ：响应
	 * @param command ：FormDTO对象
	 * @throws CommonException ：出错时抛出CommonException异常
	 * @return ModelAndView ：返回ModelAndView
	 */
	public ModelAndView uploadCoordinatedCompletion(HttpServletRequest request, HttpServletResponse response, CaseManagerFormDTO command) throws CommonException{
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			Map<String, MultipartFile> fileMap = multipartRequest.getFileMap(); 
			if (!CollectionUtils.isEmpty(fileMap)) {
				command.setFileMap(fileMap);
			}
			// 调service方法
			SessionContext ctx = this.getServiceLocator().doService(command.getLogonUser(), this.getServiceId(), "uploadCoordinatedCompletion", command);
			Map map = null;
			Message msg = ctx.getReturnMessage();
			Object object = ctx.getAttribute(IAtomsConstants.PARAM_ACTION_RESULT);
			if (object != null) {
				if (object instanceof Map) {
					map = (Map) object;						
				} else {
					map = new HashMap();
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
				}
			} else {
				map = new HashMap();
			}
			if (msg != null && StringUtils.hasText(msg.getCode())) {
				if (msg.getStatus() == Message.STATUS.SUCCESS) {
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.TRUE);
				} else {
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
				}
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(msg.getCode(), msg.getArguments(), null));
			}
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量發送mail
				command = (CaseManagerFormDTO)ctx.getResponseResult();
				command.setCaseActionId("immediatelyClosing");
				command.setUploadCoordinatedCompletionFlag("Y");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView(new MappingJacksonJsonView(), map); 
		} catch (Exception e) {
			LOGGER.error("uploadCoordinatedCompletion()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	
	/**
	 * Purpose:結案匯入--汇入格式下载
	 * @author amandawang
	 * @param request ：请求
	 * @param response ：响应
	 * @param command ：FormDTO对象
	 * @throws CommonException ：出错时抛出CommonException异常
	 * @return void ：无返回值
	 */
	public void downloadCoordinatedCompletion(HttpServletRequest request, HttpServletResponse response, CaseManagerFormDTO command) throws CommonException{
		try {
			// 模板名称
			String fileNameCn = i18NUtil.getName(IAtomsConstants.FIELD_CASE_END_TEMPLATE_NAME_FOR_CN);
			String fileNameEn = IAtomsConstants.FIELD_CASE_END_TEMPLATE_NAME_FOR_EN;
			// 模板路径
			String tempPath = IAtomsConstants.TEMPLATE_DOWNLOAD_PATH;
			StringBuffer buffer = new StringBuffer();
			buffer.append(tempPath).append(fileNameEn);
			String tempFailString = buffer.toString();
		//	String tempFailString = tempPath.concat(fileNameEn);
			InputStream inputStream = ContractSlaService.class.getResourceAsStream(tempFailString);
			FileUtils.download(request, response, inputStream, fileNameCn);
		} catch (Exception e) {
			LOGGER.error("downloadCoordinatedCompletion()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * @return the importFilePath
	 */
	public Map<String, String> getImportFilePath() {
		return importFilePath;
	}

	/**
	 * @param importFilePath the importFilePath to set
	 */
	public void setImportFilePath(Map<String, String> importFilePath) {
		this.importFilePath = importFilePath;
	}
	
}
