package com.cybersoft4u.xian.iatoms.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.service.ServiceException;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTransactionParameterDTO;
import com.cybersoft4u.xian.iatoms.services.ICaseCheckInfoService;
import com.cybersoft4u.xian.iatoms.services.workflow.impl.IAtomsHumanTaskActivityService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Purpose: 案件商業性邏輯核檢service
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2018年6月27日
 * @MaintenancePersonnel CarrieDuan
 */
public class CaseCheckInfoService extends IAtomsHumanTaskActivityService implements ICaseCheckInfoService {

	/**
	 *  系統日誌記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(CaseCheckInfoService.class);
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICaseCheckInfoService#checkS80EthernetAsset(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public String checkS80EthernetAsset(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		StringBuffer errorMsg = new StringBuffer();
		try {
			if (inquiryContext != null) {
				Boolean isHave = Boolean.FALSE;
				String connectionType = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CONNECTION_TYPE_NAME.getValue());
				String builtInFeature = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.BUILT_IN_FEATURE.getValue());
				String peripheralsName = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_NAME.getValue());
				String peripheralsFunction = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION.getValue());
				String peripheralsName2 = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_NAME.getValue());
				String peripheralsFunction2 = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION2.getValue());
				String peripheralsName3 = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_NAME.getValue());
				String peripheralsFunction3 = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION3.getValue());
				String receiptType = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.RECEIPT_TYPE.getValue());
				//String aoName = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.AO_NAME.getValue());
				List<SrmCaseTransactionParameterDTO> caseTransactionParameterDTOs = (List<SrmCaseTransactionParameterDTO>) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASETRANSACTION_PARAMETER_DTOS.getValue());
				List<Parameter> softwareVersions = (List<Parameter>) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.SOFTWARE_VERSIONS.getValue());
				
				//判斷是否爲匯入核檢
				boolean isUpload = (Boolean) inquiryContext.getParameter("isUpload");
				
				//核檢DTID是否爲【5080】開頭
				Map<String, Object> resultMap = this.checkCupAndSmartpay(caseTransactionParameterDTOs);
				Boolean isDtid = this.checkDtid((String)resultMap.get("tid"), "5080");
				if (!isDtid) {
					if (isUpload) {
						errorMsg.append(IAtomsMessageCode.GP_S80_ETHERNET_ERROR_DTID).append(IAtomsConstants.MARK_SEMICOLON);
					} else {
						return IAtomsMessageCode.GP_S80_ETHERNET_ERROR_DTID;
					}
				}
				
				//核檢連接方式是否輸入
				if (!StringUtils.hasText(connectionType) && !isUpload) {
					return IAtomsMessageCode.INPUT_CONNECTION_TYPE;
				}
				
				//核檢連接方式不可是G
				isHave = this.checkContains(connectionType, IAtomsConstants.COLUMN_G);
				if (isHave) {
					if (isUpload) {
						errorMsg.append(IAtomsMessageCode.GP_S80_ETHERNET_ERROR_CONNECTION_TYPE).append(IAtomsConstants.MARK_SEMICOLON);
					} else {
						return IAtomsMessageCode.GP_S80_ETHERNET_ERROR_CONNECTION_TYPE;
					}
				}
				
				//核檢內建功能不可有Dongle
				isHave = this.checkContains(builtInFeature, IAtomsConstants.SUPPORTED_FUNCTION_DONGLE);
				if (isHave) {
					if (isUpload) {
						errorMsg.append(IAtomsMessageCode.GP_S80_ETHERNET_ERROR_DONGLE).append(IAtomsConstants.MARK_SEMICOLON);
					} else {
						return IAtomsMessageCode.GP_S80_ETHERNET_ERROR_DONGLE;
					}
				}
				
				//獲取是否有【Cup】或者【Smartpay】
				Boolean isCup = (Boolean) resultMap.get("isCup");
				Boolean isSmartpay = (Boolean) resultMap.get("isSmartpay");
				String peripheralsFunctionS200s = null;
				String peripheralsFunctionS300Qs = null;
				String peripheralsFunctions = null;
				//判斷周邊設備是否有S200或S300Q
				if ("S200".equals(peripheralsName) || "S200".equals(peripheralsName2) || "S200".equals(peripheralsName3)
						|| "S300Q".equals(peripheralsName) || "S300Q".equals(peripheralsName2) || "S300Q".equals(peripheralsName3)) {
					//獲取S200對應的周邊設備功能
					peripheralsFunctionS200s = this.getPeripheralsFunction("S200", peripheralsName, peripheralsName2, peripheralsFunction, peripheralsFunction2, peripheralsFunction3);
					peripheralsFunctionS300Qs = this.getPeripheralsFunction("S300Q", peripheralsName, peripheralsName2, peripheralsFunction, peripheralsFunction2, peripheralsFunction3);
					peripheralsFunctions = peripheralsFunctionS200s + IAtomsConstants.MARK_SEPARATOR + peripheralsFunctionS300Qs;
					if (StringUtils.hasText(peripheralsFunctions)) {
						//[環匯刷卡機型S80 Ethernet，週邊S200或S300Q功能需有Dongle]
						isHave = this.checkContains(peripheralsFunctions, IAtomsConstants.SUPPORTED_FUNCTION_DONGLE);
						if (!isHave) {
							if (isUpload) {
								errorMsg.append(IAtomsMessageCode.GP_S80_ETHERNET_ERROR_S200_DONGLE).append(IAtomsConstants.MARK_SEMICOLON);
							} else {
								return IAtomsMessageCode.GP_S80_ETHERNET_ERROR_S200_DONGLE;
							}
						}
						//判斷交易類別是否有CUP或Smartpay
						if (isCup || isSmartpay) {
							isHave = this.checkContains(peripheralsFunctions, IAtomsConstants.SUPPORTED_FUNCTION_PINPAD);
							if (!isHave) {
								if (isUpload) {
									errorMsg.append(IAtomsMessageCode.GP_S80_ETHERNET_ERROR_S200_PINPAD).append(IAtomsConstants.MARK_SEMICOLON);
								} else {
									return IAtomsMessageCode.GP_S80_ETHERNET_ERROR_S200_PINPAD;
								}
							}
						}
					} else {
						if (isUpload) {
							errorMsg.append(IAtomsMessageCode.GP_S80_ETHERNET_ERROR_S200_DONGLE).append(IAtomsConstants.MARK_SEMICOLON);
						} else {
							return IAtomsMessageCode.GP_S80_ETHERNET_ERROR_S200_DONGLE;
						}
					}
					//週邊無S200
				} else {
					//IATOMS不可以有Dongle
					List<String> peripheralsFuns = new ArrayList<String>();
					peripheralsFuns.add(peripheralsFunction);
					peripheralsFuns.add(peripheralsFunction2);
					peripheralsFuns.add(peripheralsFunction3);
					for (String perFunc : peripheralsFuns) {
						isHave = this.checkContains(perFunc, IAtomsConstants.SUPPORTED_FUNCTION_DONGLE);
						if (isHave) {
							if (isUpload) {
								errorMsg.append(IAtomsMessageCode.GP_S80_ETHERNET_NO_S200_DONGLE).append(IAtomsConstants.MARK_SEMICOLON);
							} else {
								return IAtomsMessageCode.GP_S80_ETHERNET_NO_S200_DONGLE;
							}
							break;
						}
					}
					/*if (isCup && StringUtils.hasText(aoName) && aoName.equals("I5")) {
						if ("SP20".equals(peripheralsName) || "SP20".equals(peripheralsName2) || "SP20".equals(peripheralsName3)) {
							peripheralsFunctions = this.getPeripheralsFunction("SP20", peripheralsName, peripheralsName2, peripheralsFunction, peripheralsFunction2, peripheralsFunction3);
							if (StringUtils.hasText(peripheralsFunctions)) {
								//判斷是否有Pinpad
								isHave = this.checkContains(peripheralsFunctions, IAtomsConstants.SUPPORTED_FUNCTION_PINPAD);
								if (!isHave) {
									if (isUpload) {
										errorMsg.append(IAtomsMessageCode.GP_S80_ETHERNET_ERROR_NO_S200_PINPAD).append(IAtomsConstants.MARK_SEMICOLON);
									} else {
										return IAtomsMessageCode.GP_S80_ETHERNET_ERROR_NO_S200_PINPAD;
									}
								}
							} else {
								if (isUpload) {
									errorMsg.append(IAtomsMessageCode.GP_S80_ETHERNET_ERROR_NO_S200_PINPAD).append(IAtomsConstants.MARK_SEMICOLON);
								} else {
									return IAtomsMessageCode.GP_S80_ETHERNET_ERROR_NO_S200_PINPAD;
								}
							}
						} else {
							if (isUpload) {
								errorMsg.append(IAtomsMessageCode.GP_S80_ETHERNET_ERROR_NO_S200_PINPAD).append(IAtomsConstants.MARK_SEMICOLON);
							} else {
								return IAtomsMessageCode.GP_S80_ETHERNET_ERROR_NO_S200_PINPAD;
							}
						}	
					}*/ 
					
					//環匯刷卡機型S80 Ethernet，交易類別有CUP或Smartpay且AO人員不為I5，內建功能要有Pinpad
					/*if ((isCup || isSmartpay) && StringUtils.hasText(aoName) && !aoName.equals("I5")) {
						//核檢內建功能不可有Pinpad
						isHave = this.checkContains(builtInFeature, IAtomsConstants.SUPPORTED_FUNCTION_PINPAD);
						if (!isHave) {
							if (isUpload) {
								errorMsg.append(IAtomsMessageCode.GP_S80_ETHERNET_ERROR_NO_I5_PINPAD).append(IAtomsConstants.MARK_SEMICOLON);
							} else {
								return IAtomsMessageCode.GP_S80_ETHERNET_ERROR_NO_I5_PINPAD;
							}
						}
					}*/
				}
				String temp = checkTransactionCupAndReceiptType(caseTransactionParameterDTOs, receiptType);
				if (isUpload) {
					if (StringUtils.hasText(temp)) {
						errorMsg.append(temp).append(IAtomsConstants.MARK_SEMICOLON);
					}
					return errorMsg.toString();
				} else {
					return temp;
				}
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".checkS80EthernetAsset() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICaseCheckInfoService#checkS80RFAsset(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public String checkS80RFAsset(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		StringBuffer errorMsg = new StringBuffer();
		try {
			if (inquiryContext != null) {
				Boolean isHave = Boolean.FALSE;
				String connectionType = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CONNECTION_TYPE_NAME.getValue());
				String builtInFeature = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.BUILT_IN_FEATURE.getValue());
				/*String peripheralsName = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_NAME.getValue());
				String peripheralsFunction = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION.getValue());
				String peripheralsName2 = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_NAME.getValue());
				String peripheralsFunction2 = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION2.getValue());
				String peripheralsName3 = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_NAME.getValue());
				String peripheralsFunction3 = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION3.getValue());*/
				String receiptType = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.RECEIPT_TYPE.getValue());
				//String aoName = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.AO_NAME.getValue());
				List<SrmCaseTransactionParameterDTO> caseTransactionParameterDTOs = (List<SrmCaseTransactionParameterDTO>) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASETRANSACTION_PARAMETER_DTOS.getValue());
				//判斷是否爲匯入核檢
				boolean isUpload = (Boolean) inquiryContext.getParameter("isUpload");
				//核檢DTID是否爲【5085】開頭
				Map<String, Object> resultMap = this.checkCupAndSmartpay(caseTransactionParameterDTOs);
				Boolean isDtid = this.checkDtid((String) resultMap.get("tid"), "5085");
				if (!isDtid) {
					if (isUpload) {
						errorMsg.append(IAtomsMessageCode.GP_S80_RF_ERROR_DTID).append(IAtomsConstants.MARK_SEMICOLON);
					} else {
						return IAtomsMessageCode.GP_S80_RF_ERROR_DTID;
					}
				}
				//核檢連接方式是否輸入
				if (!StringUtils.hasText(connectionType) && !isUpload) {
					return IAtomsMessageCode.INPUT_CONNECTION_TYPE;
				}
				//核檢連接方式不可是G
				isHave = this.checkContains(connectionType, IAtomsConstants.COLUMN_G);
				if (isHave) {
					if (isUpload) {
						errorMsg.append(IAtomsMessageCode.GP_S80_RF_ERROR_CONNECTION_TYPE).append(IAtomsConstants.MARK_SEMICOLON);
					} else {
						return IAtomsMessageCode.GP_S80_RF_ERROR_CONNECTION_TYPE;
					}
				}
				
				//核檢內建功能有Dongle
				isHave = this.checkContains(builtInFeature, IAtomsConstants.SUPPORTED_FUNCTION_DONGLE);
				if (!isHave) {
					if (isUpload) {
						errorMsg.append(IAtomsMessageCode.GP_S80_RF_ERROR_DONGLE).append(IAtomsConstants.MARK_SEMICOLON);
					} else {
						return IAtomsMessageCode.GP_S80_RF_ERROR_DONGLE;
					}
				}
				
				//獲取是否有【Cup】或者【Smartpay】
				/*Boolean isCup = (Boolean) resultMap.get("isCup");
				Boolean isSmartpay = (Boolean) resultMap.get("isSmartpay");
				String peripheralsFunctions = null;*/
				
				//環匯刷卡機型S80 RF，交易類別有CUP且AO人員為I5，則需有週邊SP20且功能有PINPAD
				/*if (isCup && StringUtils.hasText(aoName) && aoName.equals("I5")) {
					if ("SP20".equals(peripheralsName) || "SP20".equals(peripheralsName2) || "SP20".equals(peripheralsName3)) {
						peripheralsFunctions = this.getPeripheralsFunction("SP20", peripheralsName, peripheralsName2, peripheralsFunction, peripheralsFunction2, peripheralsFunction3);
						if (StringUtils.hasText(peripheralsFunctions)) {
							//判斷是否有Pinpad
							isHave = this.checkContains(peripheralsFunctions, IAtomsConstants.SUPPORTED_FUNCTION_PINPAD);
							if (!isHave) {
								if (isUpload) {
									errorMsg.append(IAtomsMessageCode.GP_S80_RF_ERROR_S200_PINPAD).append(IAtomsConstants.MARK_SEMICOLON);
								} else {
									return IAtomsMessageCode.GP_S80_RF_ERROR_S200_PINPAD;
								}
							}
						} else {
							if (isUpload) {
								errorMsg.append(IAtomsMessageCode.GP_S80_RF_ERROR_S200_PINPAD).append(IAtomsConstants.MARK_SEMICOLON);
							} else {
								return IAtomsMessageCode.GP_S80_RF_ERROR_S200_PINPAD;
							}
						}
					} else {
						if (isUpload) {
							errorMsg.append(IAtomsMessageCode.GP_S80_RF_ERROR_S200_PINPAD).append(IAtomsConstants.MARK_SEMICOLON);
						} else {
							return IAtomsMessageCode.GP_S80_RF_ERROR_S200_PINPAD;
						}
					}
				}*/
				
				//環匯刷卡機型S80 RF，交易類別有CUP或Smartpay且AO人員不為I5，內建功能要有Pinpad
				/*if ((isCup || isSmartpay) && StringUtils.hasText(aoName) && !aoName.equals("I5")) {
					//核檢內建功能不可有Pinpad
					isHave = this.checkContains(builtInFeature, IAtomsConstants.SUPPORTED_FUNCTION_PINPAD);
					if (!isHave) {
						if (isUpload) {
							errorMsg.append(IAtomsMessageCode.GP_S80_RF_ERROR_I5_PINPAD).append(IAtomsConstants.MARK_SEMICOLON);
						} else {
							return IAtomsMessageCode.GP_S80_RF_ERROR_I5_PINPAD;
						}
					}
				}*/
				String temp = checkTransactionCupAndReceiptType(caseTransactionParameterDTOs, receiptType);
				if (isUpload) {
					if (StringUtils.hasText(temp)) {
						errorMsg.append(temp).append(IAtomsConstants.MARK_SEMICOLON);
					}
					return errorMsg.toString();
				} else {
					return temp;
				}
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".checkS80RFAsset() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
		return null;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICaseCheckInfoService#checkS903GAsset(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public String checkS903GAsset(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		StringBuffer errorMsg = new StringBuffer();
		try {
			if (inquiryContext != null) {
				Boolean isHave = Boolean.FALSE;
				String connectionType = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CONNECTION_TYPE_NAME.getValue());
				String builtInFeature = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.BUILT_IN_FEATURE.getValue());
				String peripheralsName = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_NAME.getValue());
				String peripheralsName2 = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_NAME.getValue());
				String peripheralsName3 = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_NAME.getValue());
				String receiptType = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.RECEIPT_TYPE.getValue());
				List<SrmCaseTransactionParameterDTO> caseTransactionParameterDTOs = (List<SrmCaseTransactionParameterDTO>) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASETRANSACTION_PARAMETER_DTOS.getValue());
				
				//判斷是否爲匯入核檢
				boolean isUpload = (Boolean) inquiryContext.getParameter("isUpload");
				
				//核檢DTID是否爲【5020】開頭
				Map<String, Object> resultMap = this.checkCupAndSmartpay(caseTransactionParameterDTOs);
				Boolean isDtid = this.checkDtid((String) resultMap.get("tid"), "5020");
				if (!isDtid) {
					if (isUpload) {
						errorMsg.append(IAtomsMessageCode.GP_S80_3G_ERROR_DTID).append(IAtomsConstants.MARK_SEMICOLON);
					} else {
						return IAtomsMessageCode.GP_S80_3G_ERROR_DTID;
					}
				}
				//核檢連接方式是否輸入
				if (!StringUtils.hasText(connectionType) && !isUpload) {
					return IAtomsMessageCode.INPUT_CONNECTION_TYPE;
				}
				//核檢連接方式不可是G
				isHave = this.checkContains(connectionType, IAtomsConstants.COLUMN_G);
				if (!isHave) {
					if (isUpload) {
						errorMsg.append(IAtomsMessageCode.GP_S80_3G_ERROR_CONNECTION_TYPE).append(IAtomsConstants.MARK_SEMICOLON);
					} else {
						return IAtomsMessageCode.GP_S80_3G_ERROR_CONNECTION_TYPE;
					}
				}
				
				//核檢內建功能不可有Dongle
				isHave = this.checkContains(builtInFeature, IAtomsConstants.SUPPORTED_FUNCTION_DONGLE);
				if (isHave) {
					if (isUpload) {
						errorMsg.append(IAtomsMessageCode.GP_S80_3G_ERROR_DONGLE).append(IAtomsConstants.MARK_SEMICOLON);
					} else {
						return IAtomsMessageCode.GP_S80_3G_ERROR_DONGLE;
					}
				}
				
				//環匯刷卡機型S90 3G，不可有週邊R50、S200、SP20
				List<String> perAssets = new ArrayList<String>();
				perAssets.add("R50");
				perAssets.add("S200");
				perAssets.add("SP20");
				if (perAssets.contains(peripheralsName) || perAssets.contains(peripheralsName2) || perAssets.contains(peripheralsName3)) {
					if (isUpload) {
						errorMsg.append(IAtomsMessageCode.GP_S80_3G_ERROR_PERIPHERALS).append(IAtomsConstants.MARK_SEMICOLON);
					} else {
						return IAtomsMessageCode.GP_S80_3G_ERROR_PERIPHERALS;
					}
				}
				String temp = checkTransactionCupAndReceiptType(caseTransactionParameterDTOs, receiptType);
				if (isUpload) {
					if (StringUtils.hasText(temp)) {
						errorMsg.append(temp).append(IAtomsConstants.MARK_SEMICOLON);
					}
					return errorMsg.toString();
				} else {
					return temp;
				}
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".checkS903GAsset() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
		return null;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICaseCheckInfoService#checkS90RFAsset(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public String checkS90RFAsset(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		StringBuffer errorMsg = new StringBuffer();
		try {
			if (inquiryContext != null) {
				Boolean isHave = Boolean.FALSE;
				String connectionType = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CONNECTION_TYPE_NAME.getValue());
				String builtInFeature = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.BUILT_IN_FEATURE.getValue());
				String peripheralsName = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_NAME.getValue());
				String peripheralsName2 = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_NAME.getValue());
				String peripheralsName3 = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_NAME.getValue());
				String receiptType = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.RECEIPT_TYPE.getValue());
				List<SrmCaseTransactionParameterDTO> caseTransactionParameterDTOs = (List<SrmCaseTransactionParameterDTO>) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASETRANSACTION_PARAMETER_DTOS.getValue());
				
				//判斷是否爲匯入核檢
				boolean isUpload = (Boolean) inquiryContext.getParameter("isUpload");
				
				//核檢DTID是否爲【5025】開頭
				Map<String, Object> resultMap = this.checkCupAndSmartpay(caseTransactionParameterDTOs);
				Boolean isDtid = this.checkDtid((String) resultMap.get("tid"), "5025");
				if (!isDtid) {
					if (isUpload) {
						errorMsg.append(IAtomsMessageCode.GP_S90_RF_ERROR_DTID).append(IAtomsConstants.MARK_SEMICOLON);
					} else {
						return IAtomsMessageCode.GP_S90_RF_ERROR_DTID;
					}
				}
				//核檢連接方式是否輸入
				if (!StringUtils.hasText(connectionType) && !isUpload) {
					return IAtomsMessageCode.INPUT_CONNECTION_TYPE;
				}
				//核檢連接方式不可是G
				isHave = this.checkContains(connectionType, IAtomsConstants.COLUMN_G);
				if (!isHave) {
					if (isUpload) {
						errorMsg.append(IAtomsMessageCode.GP_S90_RF_ERROR_CONNECTION_TYPE).append(IAtomsConstants.MARK_SEMICOLON);
					} else {
						return IAtomsMessageCode.GP_S90_RF_ERROR_CONNECTION_TYPE;
					}
				}
				
				//核檢內建功能不可有Dongle
				isHave = this.checkContains(builtInFeature, IAtomsConstants.SUPPORTED_FUNCTION_DONGLE);
				if (!isHave) {
					if (isUpload) {
						errorMsg.append(IAtomsMessageCode.GP_S90_RF_ERROR_DONGLE).append(IAtomsConstants.MARK_SEMICOLON);
					} else {
						return IAtomsMessageCode.GP_S90_RF_ERROR_DONGLE;
					}
				}
				
				//環匯刷卡機型S90 RF，不可有週邊R50、S200、SP20
				List<String> perAssets = new ArrayList<String>();
				perAssets.add("R50");
				perAssets.add("S200");
				perAssets.add("SP20");
				if (perAssets.contains(peripheralsName) || perAssets.contains(peripheralsName2) || perAssets.contains(peripheralsName3)) {
					if (isUpload) {
						errorMsg.append(IAtomsMessageCode.GP_S90_RF_ERROR_PERIPHERALS).append(IAtomsConstants.MARK_SEMICOLON);
					} else {
						return IAtomsMessageCode.GP_S90_RF_ERROR_PERIPHERALS;
					}
				}
				String temp = checkTransactionCupAndReceiptType(caseTransactionParameterDTOs, receiptType);
				if (isUpload) {
					if (StringUtils.hasText(temp)) {
						errorMsg.append(temp).append(IAtomsConstants.MARK_SEMICOLON);
					}
					return errorMsg.toString();
				} else {
					return temp;
				}
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".checkS90RFAsset() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
		return null;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICaseCheckInfoService#checkVersion(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public String checkVersion(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		Map<String, String> map = new HashMap<String, String>();
		String message = null;
		if (inquiryContext != null) {
			List<Parameter> softwareVersions = (List<Parameter>) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.SOFTWARE_VERSIONS.getValue());
			String connectionType = (String) inquiryContext.getParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CONNECTION_TYPE_NAME.getValue());
			if (!CollectionUtils.isEmpty(softwareVersions)) {
				if (StringUtils.hasText(connectionType)) {
					if (connectionType.contains("TLS")) {
						String tempVersion = null;
						int tempNumber = 0;
						for (Parameter version : softwareVersions) {
							tempVersion = version.getName().toString().substring(0, version.getName().toString().length() - 1);
							if (tempVersion.length() >= 4) {
								tempNumber = tempVersion.lastIndexOf("_ssl");
								if (tempNumber + 4 == tempVersion.length()) {
									message = (String) (String) version.getName();
									break;
								}
							}
						}
					}
					if (!StringUtils.hasText(message)) {
						message = (String) softwareVersions.get(0).getName();
					}
				} else {
					message = (String) softwareVersions.get(0).getName();
				}
			}
		}
		return message;
	}
	
	/**
	 * Purpose:CUP有開預先授權功能→CUP手輸也要開啟 and Receipt_type是否輸入
	 * @author CarrieDuan
	 * @param caseTransactionParameterDTOs： 交易參數
	 * @throws ServiceException: 出錯時抛出出ServiccseException
	 * @return String
	 */
	public String checkTransactionCupAndReceiptType(List<SrmCaseTransactionParameterDTO> caseTransactionParameterDTOs, String receiptType) throws ServiceException {
		try {
			if (!CollectionUtils.isEmpty(caseTransactionParameterDTOs)) {
				for (SrmCaseTransactionParameterDTO dto : caseTransactionParameterDTOs) {
					if (IAtomsConstants.TRANSACTION_CATEGORY.CUP.getCode().equals(dto.getTransactionType())) {
						String itemValue = dto.getItemValue();
						if (StringUtils.hasText(itemValue)) {
							Gson gsonss = new GsonBuilder().create();
							Map<String, String> itemValues = (Map<String, String>) gsonss.fromJson(
									itemValue, new TypeToken<Map<String, String>>(){}.getType());
							Boolean isTrue = Boolean.FALSE;
							for (Map.Entry<String, String> entry : itemValues.entrySet()) {
								if ("preAuthorization".equals(entry.getKey()) && "V".equals(entry.getValue())){
									for (Map.Entry<String, String> item : itemValues.entrySet()) {
										if ("manualInput".equals(item.getKey()) && "V".equals(item.getValue())){
											isTrue = Boolean.TRUE;
											break;
										}
									}
									if (!isTrue) {
										return IAtomsMessageCode.GP_CUP_ERROR;
									}
								}
							}
						}
					}
				}
			}
			if (!StringUtils.hasText(receiptType)) {
				return IAtomsMessageCode.INPUT_RECEIPT_TYPE;
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".checkTransactionCup() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
		return null;
	}
	
	/**
	 * Purpose: 核檢交易參數是否包含【CUP】或者【Smartpay】
	 * @author CarrieDuan
	 * @param caseTransactionParameterDTOs: 交易參數列表
	 * @throws ServiceException: 出錯時抛出出ServiccseException
	 * @return Map<String,Boolean>
	 */
	private Map<String, Object> checkCupAndSmartpay (List<SrmCaseTransactionParameterDTO> caseTransactionParameterDTOs) throws ServiceException{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (caseTransactionParameterDTOs != null) {
			Boolean isCup = Boolean.FALSE;
			Boolean isSmartpay = Boolean.FALSE;
			String tid = null;
			for (SrmCaseTransactionParameterDTO dto : caseTransactionParameterDTOs) {
				if (IAtomsConstants.TRANSACTION_CATEGORY.CUP.getCode().equals(dto.getTransactionType())) {
					isCup = Boolean.TRUE;
				}
				if (IAtomsConstants.TRANSACTION_CATEGORY.SMART_PAY.getCode().equals(dto.getTransactionType())) {
					isSmartpay = Boolean.TRUE;
				}
				if (IAtomsConstants.TRANSACTION_CATEGORY.COMMON_VM.getCode().equals(dto.getTransactionType())
						|| IAtomsConstants.TRANSACTION_CATEGORY.COMMON_VMJ.getCode().equals(dto.getTransactionType())
						|| IAtomsConstants.TRANSACTION_CATEGORY.COMMON_VMJU.getCode().equals(dto.getTransactionType())) {
					tid = dto.getTid();
				}
			}
			resultMap.put("isCup", isCup);
			resultMap.put("isSmartpay", isSmartpay);
			resultMap.put("tid", tid);
		}
		return resultMap;
	}
	
	/**
	 * Purpose: 獲取所需周邊設備對應的周邊設備功能
	 * @author CarrieDuan
	 * @param peripheralsName：周邊設備1名稱
	 * @param peripheralsName2：周邊設備2名稱
	 * @param peripheralsFunction：周邊設備功能1
	 * @param peripheralsFunction2：周邊設備功能2
	 * @param peripheralsFunction3：周邊設備功能2
	 * @throws ServiceException: 出錯時抛出出ServiccseException
	 * @return List<String>
	 */
	private String getPeripheralsFunction(String name, String peripheralsName, String peripheralsName2,
			String peripheralsFunction, String peripheralsFunction2, String peripheralsFunction3) throws ServiceException{
		List<String> peripheralsFunctions = null;
		String tempValue = null;
		if (StringUtils.hasText(name)) {
			if (name.equals(peripheralsName)) {
				tempValue = peripheralsFunction;
			} else if (name.equals(peripheralsName2)) {
				tempValue = peripheralsFunction2;
			} else {
				tempValue = peripheralsFunction3;
			}
		}
		return tempValue;
	}
	
	/**
	 * Purpose: 核檢DTID是否以規定的數字開頭
	 * @author CarrieDuan
	 * @param dtid：ditd
	 * @param targetValue：需以開頭數據
	 * @throws ServiceException: 出錯時抛出出ServiccseException
	 * @return Boolean
	 */
	private Boolean checkDtid(String dtid, String targetValue) throws ServiceException{
		String tempValue = null;
		if (StringUtils.hasText(dtid)) {
			if (dtid.length() >= 4) {
				tempValue =  dtid.substring(0, 4);
				if (StringUtils.hasText(targetValue) && !targetValue.equals(tempValue)) {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
		return Boolean.TRUE;
	}
	
	/**
	 * Purpose:核檢內建功能是否包含Dongle
	 * @author CarrieDuan
	 * @param builtInFeature: 內建功能
	 * @param defaultValue: 需核檢是否包含的值
	 * @throws ServiceException: 出錯時抛出出ServiccseException
	 * @return boolean:【true】表示包含，【false】表示不包含
	 */
	private boolean checkContains(String builtInFeature, String defaultValue) throws ServiceException{
		Boolean isHave = Boolean.FALSE;
		if (StringUtils.hasText(builtInFeature) && StringUtils.hasText(defaultValue)) {
			String[] builtInFeatures = builtInFeature.split(IAtomsConstants.MARK_SEPARATOR);
			for (String built : builtInFeatures) {
				if (StringUtils.hasText(built) && built.contains(defaultValue)) {
					isHave = Boolean.TRUE;
					break;
				}
			}
		}
		return isHave;
	}
}
