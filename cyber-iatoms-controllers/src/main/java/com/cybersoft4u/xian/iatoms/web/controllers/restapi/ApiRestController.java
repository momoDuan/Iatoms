package com.cybersoft4u.xian.iatoms.web.controllers.restapi;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cafe.core.bean.Message;
import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.exception.CommonException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.locator.IServiceLocatorProxy;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ApiAuthorizationInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ApiLogDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ApplicationDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CaseHandleInfoDtoAPI;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTransactionParameterDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmTransactionParameterItemDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetManageFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.PaymentFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@RequestMapping("/api")  
@Controller
public class ApiRestController {
	
	/**
	 * 系統日誌記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(ApiRestController.class);
	public IServiceLocatorProxy serviceLocator;
	/**
	 * Purpose:檢核字段
	 * @param apiAuthorizationInfoDTO:授權資料dto
	 * @throws CommonException
	 * @return Message:驗證失敗消息
	 */
	public Message validate(ApiAuthorizationInfoDTO apiAuthorizationInfoDTO) throws CommonException {
		Message msg = null;
		//token
		String token = apiAuthorizationInfoDTO.getToken();
		if(!StringUtils.hasText(token)){
			msg = new Message(Message.STATUS.FAILURE, "10");
			return msg;
		}
		//id
		String uuid = apiAuthorizationInfoDTO.getUuid();
		//唯一編號為空
		if(!StringUtils.hasText(uuid)){
			msg = new Message(Message.STATUS.FAILURE, "03");
			return msg;
		} else if(!ValidateUtils.varcharLength(uuid, 0, 32)){
			msg = new Message(Message.STATUS.FAILURE, "11");
			return msg;
		}
		String caseCategory = apiAuthorizationInfoDTO.getCaseCategory();
		// 案件類別
		if(!StringUtils.hasText(caseCategory)){
			//案件類別為空
			msg = new Message(Message.STATUS.FAILURE, "12");
			return msg;
		}
		// DTID
		String dtid = apiAuthorizationInfoDTO.getDtid();
		if(!StringUtils.hasText(dtid)){
			msg = new Message(Message.STATUS.FAILURE, "14");
			return msg;
		} else {
			// dtid限制8位
			if(!ValidateUtils.length(dtid, 0, 8)){
				msg = new Message(Message.STATUS.FAILURE, "15");
				return msg;
			}
		}
		//特店代號
		String merchantCode = apiAuthorizationInfoDTO.getMerchantCode();
		if(!StringUtils.hasText(merchantCode)){
			msg = new Message(Message.STATUS.FAILURE, "16");
			return msg;
		} else {
			if(!ValidateUtils.varcharLength(merchantCode, 0, 20)){
				msg = new Message(Message.STATUS.FAILURE, "17");
				return msg;
			}
		}
		if ("02".equals(caseCategory) || "04".equals(caseCategory)) {
			// 聯系聯絡人
			String contactUser = apiAuthorizationInfoDTO.getContact();
			if(!StringUtils.hasText(contactUser)){
				msg = new Message(Message.STATUS.FAILURE, "18");
				return msg;
			} else {
				if(!ValidateUtils.length(contactUser, 0, 50)){
					msg = new Message(Message.STATUS.FAILURE, "19");
					return msg;
				}
			}
			// 聯系聯絡人手機
			String contactUserPhone = apiAuthorizationInfoDTO.getContactPhone();
			if(!StringUtils.hasText(contactUserPhone)){
				msg = new Message(Message.STATUS.FAILURE, "20");
				return msg;
			} else {
				if(!ValidateUtils.length(contactUserPhone, 0, 10)){
					msg = new Message(Message.STATUS.FAILURE, "21");
					return msg;
				} else if (!ValidateUtils.twPhone(contactUserPhone)) {
					msg = new Message(Message.STATUS.FAILURE, "22");
					return msg;
				}
			}
			// 聯系聯絡人電話
			String contactTel = apiAuthorizationInfoDTO.getContactTel();
			if(!StringUtils.hasText(contactTel)){
				msg = new Message(Message.STATUS.FAILURE, "23");
				return msg;
			} else {
				if(!ValidateUtils.length(contactTel, 0, 20)){
					msg = new Message(Message.STATUS.FAILURE, "24");
					return msg;
				}
			}
			// 聯系聯絡人email
			String contactEmail = apiAuthorizationInfoDTO.getContactEmail();
			if(!StringUtils.hasText(contactEmail)){
				msg = new Message(Message.STATUS.FAILURE, "25");
				return msg;
			} else {
				if(!ValidateUtils.length(contactEmail, 0, 100)){
					msg = new Message(Message.STATUS.FAILURE, "26");
					return msg;
				} else if (!ValidateUtils.email(contactEmail)) {
					msg = new Message(Message.STATUS.FAILURE, "27");
					return msg;
				}
			}
			// 聯系地址-縣市
			String contactAddressLocation = apiAuthorizationInfoDTO.getContactCounty();
			if(!StringUtils.hasText(contactAddressLocation)){
				msg = new Message(Message.STATUS.FAILURE, "28");
				return msg;
			}
			// 聯系地址
			String contactAddress = apiAuthorizationInfoDTO.getContactAddress();
			if(!StringUtils.hasText(contactAddress)){
				msg = new Message(Message.STATUS.FAILURE, "29");
				return msg;
			} else {
				if(!ValidateUtils.length(contactAddress, 0, 100)){
					msg = new Message(Message.STATUS.FAILURE, "30");
					return msg;
				}
			}
			//Task #3480 新增欄位 聯繫郵遞區號  聯繫郵遞區域
			String contactCode = apiAuthorizationInfoDTO.getContactCode();
			if(!StringUtils.hasText(contactCode)){
				msg = new Message(Message.STATUS.FAILURE, "36");
				return msg;
			}
			String contactArea = apiAuthorizationInfoDTO.getContactArea();
			if(!StringUtils.hasText(contactArea)){
				msg = new Message(Message.STATUS.FAILURE, "37");
				return msg;
			}
			if ("04".equals(caseCategory)) {
				// 拆機類型
				String uninstallType = apiAuthorizationInfoDTO.getUninstallType();
				if (!StringUtils.hasText(uninstallType)) {
					msg = new Message(Message.STATUS.FAILURE, "31");
					return msg;
				} else {
					//Task #3548 新增 03：到場拆機
					if ((!"01".equals(uninstallType)) && (!"02".equals(uninstallType)) && (!"03".equals(uninstallType))) {
						msg = new Message(Message.STATUS.FAILURE, "32");
						return msg;
					}
				}
			}
			//Task #3548 新增 到場註記
			String hasArrive = apiAuthorizationInfoDTO.getHasArrive();
			if (!StringUtils.hasText(hasArrive)) {
				msg = new Message(Message.STATUS.FAILURE, "40");
				return msg;
			} else {
				if (!("0".equals(hasArrive) || "1".equals(hasArrive))) {
					msg = new Message(Message.STATUS.FAILURE, "41");
					return msg;
				}
			}
		} else if ("05".equals(caseCategory) || "06".equals(caseCategory)) {
			//設備序號
			String serialNumber = apiAuthorizationInfoDTO.getSerialNumber();
			if(!StringUtils.hasText(serialNumber)){
				msg = new Message(Message.STATUS.FAILURE, "33");
				return msg;
			} else {
				if (!ValidateUtils.length(serialNumber, 0, 20)) {
					msg = new Message(Message.STATUS.FAILURE, "34");
					return msg;
				}
			}
		} else if ("00".equals(caseCategory)) {
			//Task #3547
			
		} else if ("01".equals(caseCategory) || "03".equals(caseCategory)) {
			//Task #3548 案件編號 
			String caseId = apiAuthorizationInfoDTO.getCaseId();
			if(!StringUtils.hasText(caseId)){
				msg = new Message(Message.STATUS.FAILURE, "42");
				return msg;
			}
			//Task #3548 新增 到場註記
			String hasArrive = apiAuthorizationInfoDTO.getHasArrive();
			if (!StringUtils.hasText(hasArrive)) {
				msg = new Message(Message.STATUS.FAILURE, "40");
				return msg;
			} else {
				if (!("0".equals(hasArrive) || "1".equals(hasArrive))) {
					msg = new Message(Message.STATUS.FAILURE, "41");
					return msg;
				}
			}
		} else if ("07".equals(caseCategory)) {
			//Task #3597 案件編號 
			String paymentId = apiAuthorizationInfoDTO.getPaymentId();
			if(!StringUtils.hasText(paymentId)){
				msg = new Message(Message.STATUS.FAILURE, "45");
				return msg;
			}
		} else if (IAtomsConstants.NUMBER_EIGHT.equals(caseCategory)) {
			//Task #3600
			//案件編號 
			String caseId = apiAuthorizationInfoDTO.getCaseId();
			if(!StringUtils.hasText(caseId)){
				msg = new Message(Message.STATUS.FAILURE, "42");
				return msg;
			}
		} else {
			msg = new Message(Message.STATUS.FAILURE, "35");
			return msg;
		}
		// 其他說明
		String descripton = apiAuthorizationInfoDTO.getDescription();
		if(StringUtils.hasText(descripton)){
			// 設備 其他說明限制200位
			if ("05".equals(caseCategory) || "06".equals(caseCategory)) {
				if(!ValidateUtils.length(descripton, 0, 200)){
					msg = new Message(Message.STATUS.FAILURE, "39");
					return msg;
				}
			} else if ("02".equals(caseCategory) || "04".equals(caseCategory)) {
				// 案件 其他說明限制1000位
				if(!ValidateUtils.length(descripton, 0, 1000)){
					msg = new Message(Message.STATUS.FAILURE, "13");
					return msg;
				}
			}
		}
		return msg;
	}
	
	/**
	 * 服務確認
	 */
	@RequestMapping(value = "/api ", produces = "text/plain;charset=UTF-8") 
	public @ResponseBody String api(final HttpServletRequest httpRequest,@RequestBody String jsonStr) throws CommonException{
		return "{\"result\":\"true\",\"Your IP\":\""+httpRequest.getRemoteAddr()+"\"}";
    }
	
	/**
	 * 登陸授權
	 */
	@RequestMapping(value = "/login ", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8") 
	public @ResponseBody String login(final HttpServletRequest httpRequest,@RequestBody String jsonStr) throws CommonException{
		String remoteAddr = httpRequest.getRemoteAddr();
		try {
			Gson gsonss = new GsonBuilder().create();
			ApiAuthorizationInfoDTO apiAuthorizationInfoDTO = (ApiAuthorizationInfoDTO) gsonss.fromJson(jsonStr, new TypeToken<ApiAuthorizationInfoDTO>(){}.getType());
			synchronized (apiAuthorizationInfoDTO) {
				if (StringUtils.hasText(remoteAddr)) {
					HttpSession session = httpRequest.getSession();
					//session過期時間
					int maxInactiveInterval = session.getMaxInactiveInterval();
					
					apiAuthorizationInfoDTO.setMaxInactiveInterval(maxInactiveInterval);
					apiAuthorizationInfoDTO.setRemoteAddr(remoteAddr);
					//授權 設為Y
					apiAuthorizationInfoDTO.setFlag(IAtomsConstants.PARAM_YES);
					SessionContext sessionContext = this.serviceLocator.doService(null, "apiAuthorizationInfoService", "init", apiAuthorizationInfoDTO);
					apiAuthorizationInfoDTO = (ApiAuthorizationInfoDTO) sessionContext.getRequestParameter();
					//ApiLogDTO apiLogDTO = new ApiLogDTO();
					if (apiAuthorizationInfoDTO != null) {
						//返回授權成功，授權失敗
						if(StringUtils.hasText(apiAuthorizationInfoDTO.getJsonString())){
							return apiAuthorizationInfoDTO.getJsonString();
						} else {
							return "{\"result\":\"false\",\"token\":\"\"}";
						}
					} else {
						return "{\"result\":\"false\",\"token\":\"\"}";
					}
				} else {
					return "{\"result\":\"false\",\"token\":\"\"}";
				}
			}
		} catch (Exception e) {
			LOGGER.error("login is wrong ");
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
    }  
	
	/**
	 * 處理請求
	 */
	@RequestMapping(value = "/handle", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8") 
	public @ResponseBody String handle (final HttpServletRequest httpRequest,@RequestBody String jsonStr) throws CommonException{
		ApiAuthorizationInfoDTO apiAuthorizationInfoDTO = new ApiAuthorizationInfoDTO();
		try {
			Gson gsonss = new GsonBuilder().create();
			apiAuthorizationInfoDTO = (ApiAuthorizationInfoDTO) gsonss.fromJson(jsonStr, new TypeToken<ApiAuthorizationInfoDTO>(){}.getType());
			synchronized (apiAuthorizationInfoDTO) {
				//返回數據
				String responseStr = IAtomsConstants.MARK_EMPTY_STRING;
				SessionContext sessionContext = null;
				//請求ip
				String remoteAddr = httpRequest.getRemoteAddr();
				LOGGER.debug(".handle() -->remoteAddr=" + remoteAddr);
				if (StringUtils.hasText(remoteAddr)) {
					ApiLogDTO apiLogDTO = new ApiLogDTO();
					HttpSession session = httpRequest.getSession();
					//session過期時間
					int maxInactiveInterval = session.getMaxInactiveInterval();
					LOGGER.debug(".handle() -->maxInactiveInterval=" + maxInactiveInterval);
					CaseManagerFormDTO command = new CaseManagerFormDTO();
					if (apiAuthorizationInfoDTO!= null) {
						LOGGER.debug(".handle() -->apiAuthorizationInfoDTO is not null...");
						apiAuthorizationInfoDTO.setMaxInactiveInterval(maxInactiveInterval);
						apiAuthorizationInfoDTO.setRemoteAddr(remoteAddr);
						//通知 設為N
						apiAuthorizationInfoDTO.setFlag(IAtomsConstants.PARAM_NO);
						Message msg = null;
						LOGGER.debug(".handle() -->uuid=" + apiAuthorizationInfoDTO.getUuid());
						//初始化方法 驗證ip及token
						sessionContext = this.serviceLocator.doService(null, "apiAuthorizationInfoService", "init", apiAuthorizationInfoDTO);
						msg = sessionContext.getReturnMessage();
						apiAuthorizationInfoDTO = (ApiAuthorizationInfoDTO) sessionContext.getRequestParameter();
						//Task #3519
						apiLogDTO = dealFailReasonDesc(apiAuthorizationInfoDTO, "", jsonStr, remoteAddr, IAtomsConstants.API_RQ, null, "true");
						this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);

						//沒有uuid直接返回
						if (!StringUtils.hasText(apiAuthorizationInfoDTO.getUuid())) {
							responseStr = "{\"result\":\"false\",\"failReason\":\"03\"}";
							apiLogDTO = dealFailReasonDesc(apiAuthorizationInfoDTO, responseStr, "", remoteAddr, IAtomsConstants.API_RS, null, "false");
							responseStr = responseStr.substring(0, responseStr.length()-1)+",\"failReasonDesc\":\""+apiLogDTO.getFailReasonDesc()+"\"}";
							apiLogDTO.setMessage(responseStr);
							apiLogDTO.setResult("false");
							//保存log記錄
							this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
							LOGGER.debug(".saveApiLog() --> do saveLog finished...");
							return responseStr;
						}
						//如果ip及token驗證通過
						if (msg!= null && IAtomsMessageCode.IATOMS_SUCCESS.equals(msg.getCode())) {
							LOGGER.debug(".handle() -->init() msg is not null msg.getCode()=" + msg.getCode());
							//是否存在唯一編號
							if(StringUtils.hasText(apiAuthorizationInfoDTO.getUuid())) {
								//驗證唯一編號是否重複
								sessionContext = this.serviceLocator.doService(null, "apiAuthorizationInfoService", "checkId", apiAuthorizationInfoDTO.getUuid());
								msg = sessionContext.getReturnMessage();
								LOGGER.debug(".handle() -->checkId() msg is not null and msg.getCode()=" + msg.getCode());
								if (msg!= null && Message.STATUS.SUCCESS.equals(msg.getStatus())) {
									//驗證字段必填與長度
									msg = validate(apiAuthorizationInfoDTO);
									if (msg != null && Message.STATUS.FAILURE.equals(msg.getStatus())) {
										LOGGER.debug(".handle() -->checkId() msg is not null and msg.getCode()=" + msg.getCode());
										responseStr = "{\"result\":\"false\",\"failReason\":\""+msg.getCode()+"\"}";
										apiLogDTO = dealFailReasonDesc(apiAuthorizationInfoDTO, responseStr, "", remoteAddr, IAtomsConstants.API_RS, null, "false");
										responseStr = responseStr.substring(0, responseStr.length()-1)+",\"failReasonDesc\":\""+apiLogDTO.getFailReasonDesc()+"\"}";
										apiLogDTO.setMessage(responseStr);
										apiLogDTO.setResult("false");
										//保存log記錄
										this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
										LOGGER.debug(".saveApiLog() --> do saveLog finished...");
										return responseStr;
									}
									SrmCaseHandleInfoDTO caseHandleInfoDTO = new SrmCaseHandleInfoDTO();
									caseHandleInfoDTO.setDtid(apiAuthorizationInfoDTO.getDtid());
									caseHandleInfoDTO.setMerchantCode(apiAuthorizationInfoDTO.getMerchantCode());
									caseHandleInfoDTO.setCaseCategory(apiAuthorizationInfoDTO.getCaseCategory());
									//contactUser 王越 contactMobilePhone 13312121212 contactUserPhone 8822222333 contactUserEmail 1@qq.com contactAddressLocation 02 contactAddress 一個地方  repairReason  uninstallType
									caseHandleInfoDTO.setContactMobilePhone(apiAuthorizationInfoDTO.getContactPhone());
									caseHandleInfoDTO.setContactUser(apiAuthorizationInfoDTO.getContact());
									caseHandleInfoDTO.setContactUserPhone(apiAuthorizationInfoDTO.getContactTel());
									caseHandleInfoDTO.setContactUserEmail(apiAuthorizationInfoDTO.getContactEmail());
									caseHandleInfoDTO.setContactAddressLocation(apiAuthorizationInfoDTO.getContactCounty());
									caseHandleInfoDTO.setContactAddress(apiAuthorizationInfoDTO.getContactAddress());
									//Task #3480 新增欄位 聯繫郵遞區號  聯繫郵遞區域   
									caseHandleInfoDTO.setContactPostCode(apiAuthorizationInfoDTO.getContactCode());
									caseHandleInfoDTO.setContactPostArea(apiAuthorizationInfoDTO.getContactArea());
									caseHandleInfoDTO.setDescription(apiAuthorizationInfoDTO.getDescription());
									caseHandleInfoDTO.setHasArrive("0".equals(apiAuthorizationInfoDTO.getHasArrive()) ? "N":"Y");
									LOGGER.debug(".handle() -->hasArrive=" + caseHandleInfoDTO.getHasArrive());
									LOGGER.debug(".handle() -->CaseCategory=" + caseHandleInfoDTO.getCaseCategory());
									//02：報修,04：拆機
									if (IAtomsConstants.TASK_STAUS_TOW.equals(caseHandleInfoDTO.getCaseCategory()) 
												|| IAtomsConstants.TASK_STATUS_FOUR.equals(caseHandleInfoDTO.getCaseCategory())) {
										if (IAtomsConstants.TASK_STAUS_TOW.equals(caseHandleInfoDTO.getCaseCategory())) {
											caseHandleInfoDTO.setCaseCategory(IAtomsConstants.CASE_CATEGORY.REPAIR.getCode());
										} else if( IAtomsConstants.TASK_STATUS_FOUR.equals(caseHandleInfoDTO.getCaseCategory())){
											caseHandleInfoDTO.setCaseCategory(IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode());
											//01：強制解約, 02：商店寄回
											if (StringUtils.hasText(apiAuthorizationInfoDTO.getUninstallType())) {
												if (IAtomsConstants.PROJECT_STATUS_OPEN.equals(apiAuthorizationInfoDTO.getUninstallType())) {
													caseHandleInfoDTO.setUninstallType("COMPULSORY_DISSOLUTION");
												} else if (IAtomsConstants.TASK_STAUS_TOW.equals(apiAuthorizationInfoDTO.getUninstallType())) {
													caseHandleInfoDTO.setUninstallType("STORE_SEND_BACK");
												} else {
													//Task #3548 03：到場拆機
													caseHandleInfoDTO.setUninstallType("ARRIVE_UNINSTALL");
												}
												LOGGER.debug(".handle() -->UninstallType=" + apiAuthorizationInfoDTO.getUninstallType());
											}
										}
										LOGGER.debug(".handle() -->Description=" + apiAuthorizationInfoDTO.getDescription());
										caseHandleInfoDTO.setDescription(apiAuthorizationInfoDTO.getDescription());
										command.setCaseCategory(caseHandleInfoDTO.getCaseCategory());
										command.setSrmCaseHandleInfoDTO(caseHandleInfoDTO);
										//建案
										sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, "buildCaseByApi", command);
										msg = sessionContext.getReturnMessage();
										//如果建案失敗
										if (msg!= null && Message.STATUS.FAILURE.equals(msg.getStatus())) {
											LOGGER.debug(".handle() -->buildCaseByApi() ststus=FAILURE msg.getCode()=" + msg.getCode());
											//縣市 查無資料
											if (IAtomsMessageCode.IMPORT_VALUE_NOT_FOUND.equals(msg.getCode())) {
												responseStr = "{\"result\":\"false\",\"failReason\":\"04\"}";
											} else if (IAtomsMessageCode.MERCHANT_CODE_NOT_EXIST.equals(msg.getCode())) {
												//特店 查無資料
												responseStr = "{\"result\":\"false\",\"failReason\":\"05\"}";
											} else if (IAtomsMessageCode.INPUT_ERROR.equals(msg.getCode())) {
												//聯繫郵遞區域 查無資料
												responseStr = "{\"result\":\"false\",\"failReason\":\"38\"}";
											} else if (IAtomsMessageCode.NO_DITD_FOR_CUSTOMER_AND_TYPE.equals(msg.getCode())) {
												//dtid 查無資料
												responseStr = "{\"result\":\"false\",\"failReason\":\"06\"}";
												
											} 
											apiLogDTO = dealFailReasonDesc(apiAuthorizationInfoDTO, responseStr, "", remoteAddr, IAtomsConstants.API_RS, null, "false");
											if (StringUtils.hasText(responseStr)) {
												responseStr = responseStr.substring(0, responseStr.length()-1)+",\"failReasonDesc\":\""+apiLogDTO.getFailReasonDesc()+"\"}";
											}
											if (StringUtils.hasText(responseStr)) {
												apiLogDTO.setMessage(responseStr);
											} else {
												apiLogDTO.setMessage(apiAuthorizationInfoDTO.getJsonString());
											}
											apiLogDTO.setResult("false");
											//保存log記錄
											this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
											LOGGER.debug(".saveApiLog() --> do saveLog finished...");
											return responseStr;
										}
										Map map = (Map) sessionContext.getAttribute(IAtomsConstants.PARAM_ACTION_RESULT);
										//如果建案成功
										if ((Boolean) map.get(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS)) {
											LOGGER.debug(".handle() -->buildCaseByApi() map=success msg.getCode()=" + msg.getCode());
											Message message =sessionContext.getReturnMessage();
											String[] caseId = message.getArguments();
											if (caseId.length > 0) {
												LOGGER.debug(".handle() -->buildCaseByApi() result=true");
												String transactionId = (String) sessionContext.getAttribute("apiTransactionId");
												//返回 成功標誌 ，返回 案件編號
												responseStr = "{\"result\":\"true\",\"caseId\":\""+caseId[0]+"\"}";
												LOGGER.debug(".handle() -->buildCaseByApi()  caseId=" + caseId[0]);
												apiLogDTO.setId(apiAuthorizationInfoDTO.getUuid());
												apiLogDTO.setIp(remoteAddr);
												apiLogDTO.setClientCode(apiAuthorizationInfoDTO.getClientCode());
												apiLogDTO.setFunctionCode("handle");
												apiLogDTO.setMsgType(IAtomsConstants.API_RS);//下行
												apiLogDTO.setMasterId(caseId[0]);
												apiLogDTO.setDetailId(transactionId);//歷程id
												apiLogDTO.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
												apiLogDTO.setCreatedById("CMS000000-0001");
												apiLogDTO.setCreatedByName("cms_to_iatoms");
												apiLogDTO.setMessage(responseStr);
												apiLogDTO.setResult("true");
												LOGGER.debug(".handle() -->Message=" + jsonStr + responseStr);
												//更新授權時間
												sessionContext = this.serviceLocator.doService(null, "apiAuthorizationInfoService", "updateApiAuthorizationInfo", apiLogDTO);
												LOGGER.debug(".handle() --> do updateApiAuthorizationInfo finished...");
												//保存log記錄
												sessionContext = this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
												LOGGER.debug(".handle() --> do saveLog finished...");
												return responseStr;
											}
										}
										//05：設備停用 , 06：設備啟用
									} else if (IAtomsConstants.TASK_STATUS_FIVE.equals(caseHandleInfoDTO.getCaseCategory()) 
														|| IAtomsConstants.NUMBER_SIX.equals(caseHandleInfoDTO.getCaseCategory())){
										AssetManageFormDTO assetManageFormDTO = new AssetManageFormDTO();
										assetManageFormDTO.setQuerySerialNumbers(apiAuthorizationInfoDTO.getSerialNumber());
										if (IAtomsConstants.NUMBER_SIX.equals(caseHandleInfoDTO.getCaseCategory())) {
											assetManageFormDTO.setQueryAction("DEVICE_ENABLE");
										} else {
											assetManageFormDTO.setQueryAction("DEVICE_DISABLE");
										}
										LOGGER.debug(".handle() -->  QueryAction=" + assetManageFormDTO.getQueryAction());
										assetManageFormDTO.setQueryCommet(apiAuthorizationInfoDTO.getDescription());
										assetManageFormDTO.setQueryMerchantCode(apiAuthorizationInfoDTO.getMerchantCode());
										assetManageFormDTO.setQueryDtid(apiAuthorizationInfoDTO.getDtid());
										//修改設備狀態
										sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_REPOSITORY_SERVICE, "changeAssetStatus", assetManageFormDTO);
										msg = sessionContext.getReturnMessage();
										//如果修改失敗
										if (msg!= null && Message.STATUS.FAILURE.equals(msg.getStatus())) {
											LOGGER.debug(".handle() -->  changeAssetStatus() SerialNumber not found SerialNumber=" + apiAuthorizationInfoDTO.getSerialNumber());
											//設備序號 查無資料
											if (IAtomsMessageCode.IMPORT_VALUE_NOT_FOUND.equals(msg.getCode())) {
												responseStr = "{\"result\":\"false\",\"failReason\":\"07\"}";
												LOGGER.debug(".handle() --> SerialNumber not found...");
												apiLogDTO = dealFailReasonDesc(apiAuthorizationInfoDTO, responseStr, "", remoteAddr, IAtomsConstants.API_RS, null, "false");
												responseStr = responseStr.substring(0, responseStr.length()-1)+",\"failReasonDesc\":\""+apiLogDTO.getFailReasonDesc()+"\"}";
											} 
											if (StringUtils.hasText(responseStr)) {
												apiLogDTO.setMessage(responseStr);
											} else {
												apiLogDTO.setMessage(apiAuthorizationInfoDTO.getJsonString());
											}
											apiLogDTO.setResult("false");
											//保存log記錄
											this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
											LOGGER.debug(".saveApiLog() --> do saveLog finished...");
											return responseStr;
										}
										//如果修改成功
										if (msg!= null && Message.STATUS.SUCCESS.equals(msg.getStatus())) {
											LOGGER.debug(".handle() -->  changeAssetStatus() success serialNumber=" + apiAuthorizationInfoDTO.getSerialNumber());
											String historyId = (String) sessionContext.getAttribute("historyId");
											//返回 成功標誌 ，返回 設備序號
											responseStr = "{\"result\":\"true\",\"serialNumber\":\""+apiAuthorizationInfoDTO.getSerialNumber()+"\"}";
											apiLogDTO.setId(apiAuthorizationInfoDTO.getUuid());
											apiLogDTO.setIp(remoteAddr);
											apiLogDTO.setClientCode(apiAuthorizationInfoDTO.getClientCode());
											apiLogDTO.setFunctionCode("handle");
											apiLogDTO.setMsgType(IAtomsConstants.API_RS);//下行
											apiLogDTO.setMasterId(apiAuthorizationInfoDTO.getSerialNumber());
											apiLogDTO.setDetailId(historyId);//歷程id
											apiLogDTO.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
											apiLogDTO.setCreatedById("CMS000000-0001");
											apiLogDTO.setCreatedByName("cms_to_iatoms");
											apiLogDTO.setMessage(responseStr);
											apiLogDTO.setResult("true");
											
											//更新授權時間
											sessionContext = this.serviceLocator.doService(null, "apiAuthorizationInfoService", "updateApiAuthorizationInfo", apiLogDTO);
											LOGGER.debug(".handle() --> do updateApiAuthorizationInfo finished...");
											//保存log記錄
											sessionContext = this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
											LOGGER.debug(".handle() --> do saveLog finished...");
											return responseStr;
										}
									} else if (IAtomsConstants.PROJECT_STATUS_ALL.equals(caseHandleInfoDTO.getCaseCategory())) {
										//Task #3547  新增一個 00 查詢
										command = new CaseManagerFormDTO();
										command.setQueryDtid(apiAuthorizationInfoDTO.getDtid());
										//根據dtid查詢案件資料
										sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, "queryCaseInfoByApi", command);
										msg = sessionContext.getReturnMessage();
										//如果修改失敗
										if (msg!= null && Message.STATUS.FAILURE.equals(msg.getStatus())) {
											LOGGER.debug(".handle() -->  queryCaseInfoByApi() dtid not found match caseid ==> dtid=" + apiAuthorizationInfoDTO.getDtid());
											//dtid 查無資料
											if (IAtomsMessageCode.NO_DITD_FOR_CUSTOMER_AND_TYPE.equals(msg.getCode())) {
												responseStr = "{\"result\":\"false\",\"failReason\":\"06\"}";
												LOGGER.debug(".handle() -->queryCaseInfoByApi() dtid not found...");
												apiLogDTO = dealFailReasonDesc(apiAuthorizationInfoDTO, responseStr, "", remoteAddr, IAtomsConstants.API_RS, null, "false");
												responseStr = responseStr.substring(0, responseStr.length()-1)+",\"failReasonDesc\":\""+apiLogDTO.getFailReasonDesc()+"\"}";
											} 
											if (StringUtils.hasText(responseStr)) {
												apiLogDTO.setMessage(responseStr);
											} else {
												apiLogDTO.setMessage(apiAuthorizationInfoDTO.getJsonString());
											}
											apiLogDTO.setResult("false");
											//保存log記錄
											this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
											LOGGER.debug(".saveApiLog() --> do saveLog finished...");
											return responseStr;
										}
										//如果查詢成功
										if (msg!= null && Message.STATUS.SUCCESS.equals(msg.getStatus())) {
											LOGGER.debug(".handle() -->  queryCaseInfoByApi() success dtid=" + apiAuthorizationInfoDTO.getDtid());
											command =  (CaseManagerFormDTO) sessionContext.getResponseResult();
											ApiAuthorizationInfoDTO queryApiAuthorizationInfoDTO = command.getApiAuthorizationInfoDTO();
											//返回 成功標誌 ，返回 聯繫聯絡人 聯繫電話 聯繫手機 聯繫EMAIL 聯繫郵遞區號 聯繫郵遞區域 聯繫地址-縣市 聯繫地址
											responseStr = "{\"result\":\"true\",\"contact\":\"" + queryApiAuthorizationInfoDTO.getContact()
																	+ "\",\"contactPhone\":\""+queryApiAuthorizationInfoDTO.getContactPhone()
																	+"\",\"contactTel\":\""+queryApiAuthorizationInfoDTO.getContactTel()
																	+"\",\"contactEmail\":\""+queryApiAuthorizationInfoDTO.getContactEmail()
																	+"\",\"contactCounty\":\""+queryApiAuthorizationInfoDTO.getContactCounty()
																	+"\",\"contactAddress\":\""+queryApiAuthorizationInfoDTO.getContactAddress()
																	+"\",\"contactCode\":\""+queryApiAuthorizationInfoDTO.getContactCode()
																	+"\",\"contactArea\":\""+queryApiAuthorizationInfoDTO.getContactArea()+"\"}";
											apiLogDTO.setId(null);
											apiLogDTO.setIp(remoteAddr);
											apiLogDTO.setClientCode(apiAuthorizationInfoDTO.getClientCode());
											apiLogDTO.setFunctionCode("handle");
											apiLogDTO.setMsgType(IAtomsConstants.API_RS);//下行
											apiLogDTO.setMasterId(queryApiAuthorizationInfoDTO.getCaseId());
											apiLogDTO.setDetailId(null);
											apiLogDTO.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
											apiLogDTO.setCreatedById("CMS000000-0001");
											apiLogDTO.setCreatedByName("cms_to_iatoms");
											apiLogDTO.setMessage(responseStr);
											apiLogDTO.setResult("true");
											
											//更新授權時間
											sessionContext = this.serviceLocator.doService(null, "apiAuthorizationInfoService", "updateApiAuthorizationInfo", apiLogDTO);
											LOGGER.debug(".handle() --> do updateApiAuthorizationInfo finished...");
											//保存log記錄
											sessionContext = this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
											LOGGER.debug(".handle() --> do saveLog finished...");
											return responseStr;
										}
									} else if (IAtomsConstants.PROJECT_STATUS_OPEN.equals(caseHandleInfoDTO.getCaseCategory()) 
													|| IAtomsConstants.TASK_STAUS_THREE.equals(caseHandleInfoDTO.getCaseCategory())) {
										//Task #3548 01：裝機, 03：快速報修
										command.setQueryCaseId(apiAuthorizationInfoDTO.getCaseId());
										command.setHasArrive(caseHandleInfoDTO.getHasArrive());
										command.setDescription(apiAuthorizationInfoDTO.getDescription());
										//建案
										sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, "changeCaseInfoByApi", command);
										msg = sessionContext.getReturnMessage();
										//如果修改失敗
										if (msg!= null && Message.STATUS.FAILURE.equals(msg.getStatus())) {
											LOGGER.debug(".handle() -->changeCaseInfoByApi() ststus=FAILURE msg.getCode()=" + msg.getCode());
											//案件編號 查無資料
											if (IAtomsMessageCode.PARAM_CMS_CASE_ERROR.equals(msg.getCode())) {
												responseStr = "{\"result\":\"false\",\"failReason\":\"43\"}";
											} 
											apiLogDTO = dealFailReasonDesc(apiAuthorizationInfoDTO, responseStr, "", remoteAddr, IAtomsConstants.API_RS, null, "false");
											if (StringUtils.hasText(responseStr)) {
												responseStr = responseStr.substring(0, responseStr.length()-1)+",\"failReasonDesc\":\""+apiLogDTO.getFailReasonDesc()+"\"}";
											}
											if (StringUtils.hasText(responseStr)) {
												apiLogDTO.setMessage(responseStr);
											} else {
												apiLogDTO.setMessage(apiAuthorizationInfoDTO.getJsonString());
											}
											apiLogDTO.setResult("false");
											//保存log記錄
											this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
											LOGGER.debug(".saveApiLog() --> do saveLog finished...");
											return responseStr;
										}
										//如果修改成功
										if (msg!= null && Message.STATUS.SUCCESS.equals(msg.getStatus())) {
											LOGGER.debug(".handle() -->changeCaseInfoByApi() map=success msg.getCode()=" + msg.getCode());
											LOGGER.debug(".handle() -->changeCaseInfoByApi() result=true");
											String transactionId = (String) sessionContext.getAttribute("apiTransactionId");
											//返回 成功標誌 ，返回 案件編號
											responseStr = "{\"result\":\"true\"}";
											LOGGER.debug(".handle() -->buildCaseByApi()  caseId=" + apiAuthorizationInfoDTO.getCaseId());
											apiLogDTO.setId(apiAuthorizationInfoDTO.getUuid());
											apiLogDTO.setIp(remoteAddr);
											apiLogDTO.setClientCode(apiAuthorizationInfoDTO.getClientCode());
											apiLogDTO.setFunctionCode("handle");
											apiLogDTO.setMsgType(IAtomsConstants.API_RS);//下行
											apiLogDTO.setMasterId(apiAuthorizationInfoDTO.getCaseId());
											apiLogDTO.setDetailId(transactionId);//歷程id
											apiLogDTO.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
											apiLogDTO.setCreatedById("CMS000000-0001");
											apiLogDTO.setCreatedByName("cms_to_iatoms");
											apiLogDTO.setMessage(responseStr);
											apiLogDTO.setResult("true");
											LOGGER.debug(".handle() -->Message=" + jsonStr + responseStr);
											//更新授權時間
											sessionContext = this.serviceLocator.doService(null, "apiAuthorizationInfoService", "updateApiAuthorizationInfo", apiLogDTO);
											LOGGER.debug(".handle() --> do updateApiAuthorizationInfo finished...");
											//保存log記錄
											sessionContext = this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
											LOGGER.debug(".handle() --> do saveLog finished...");
											return responseStr;
										}
									} else if (IAtomsConstants.NUMBER_SEVEN.equals(caseHandleInfoDTO.getCaseCategory())) {
										PaymentFormDTO formDTO = new PaymentFormDTO();
										SrmPaymentInfoDTO paymentInfoDTO = new SrmPaymentInfoDTO();
										paymentInfoDTO.setPaymentId(apiAuthorizationInfoDTO.getPaymentId());
										paymentInfoDTO.setMerchantCode(apiAuthorizationInfoDTO.getMerchantCode());
										paymentInfoDTO.setDtid(apiAuthorizationInfoDTO.getDtid());
										paymentInfoDTO.setAskPayDesc(apiAuthorizationInfoDTO.getDescription());
										formDTO.setPaymentInfoDTO(paymentInfoDTO);
										//求償完成
										sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_PAYMENT_SERVICE, "changePaymentCompleteByApi", formDTO);
										msg = sessionContext.getReturnMessage();
										//如果修改失敗
										if (msg != null && Message.STATUS.FAILURE.equals(msg.getStatus())) {
											if (IAtomsMessageCode.SYSTEM_FAILED.equals(msg.getCode())) {
												responseStr = "{\"result\":\"false\",\"failReason\":\"00\"}";
											} else if (IAtomsMessageCode.NOT_QUERY_PAYMENT_INFO.equals(msg.getCode())) {
												responseStr = "{\"result\":\"false\",\"failReason\":\"46\"}";
											} else {
												LOGGER.debug(".handle() -->  changePaymentCompleteByApi() 求償狀態不爲求償確認");
												responseStr = "{\"result\":\"false\",\"failReason\":\"44\"}";
											}
											apiLogDTO = dealFailReasonDesc(apiAuthorizationInfoDTO, responseStr, "", remoteAddr, IAtomsConstants.API_RS, null, "false");
											if (StringUtils.hasText(responseStr)) {
												responseStr = responseStr.substring(0, responseStr.length()-1)+",\"failReasonDesc\":\""+apiLogDTO.getFailReasonDesc()+"\"}";
											}
											if (StringUtils.hasText(responseStr)) {
												apiLogDTO.setMessage(responseStr);
											} else {
												apiLogDTO.setMessage(apiAuthorizationInfoDTO.getJsonString());
											}
											apiLogDTO.setResult("false");
											//保存log記錄
											this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
											LOGGER.debug(".saveApiLog() --> do saveLog finished...");
											return responseStr;
										} else {
											LOGGER.debug(".handle() -->changePaymentCompleteByApi() map=success msg.getCode()=" + msg.getCode());
											LOGGER.debug(".handle() -->changePaymentCompleteByApi() result=true");
											String transactionId = (String) sessionContext.getAttribute("apiTransactionId");
											//返回 成功標誌 ，返回 案件編號
											responseStr = "{\"result\":\"true\"}";
											LOGGER.debug(".handle() -->buildCaseByApi()  caseId=" + apiAuthorizationInfoDTO.getCaseId());
											apiLogDTO.setId(apiAuthorizationInfoDTO.getUuid());
											apiLogDTO.setIp(remoteAddr);
											apiLogDTO.setClientCode(apiAuthorizationInfoDTO.getClientCode());
											apiLogDTO.setFunctionCode("handle");
											apiLogDTO.setMsgType(IAtomsConstants.API_RS);//下行
											apiLogDTO.setMasterId(apiAuthorizationInfoDTO.getCaseId());
											apiLogDTO.setDetailId(transactionId);//歷程id
											apiLogDTO.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
											apiLogDTO.setCreatedById("CMS000000-0001");
											apiLogDTO.setCreatedByName("cms_to_iatoms");
											apiLogDTO.setMessage(responseStr);
											apiLogDTO.setResult("true");
											LOGGER.debug(".handle() -->Message=" + jsonStr + responseStr);
											//更新授權時間
											sessionContext = this.serviceLocator.doService(null, "apiAuthorizationInfoService", "updateApiAuthorizationInfo", apiLogDTO);
											LOGGER.debug(".handle() --> do updateApiAuthorizationInfo finished...");
											//保存log記錄
											sessionContext = this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
											LOGGER.debug(".handle() --> do saveLog finished...");
											return responseStr;
										}
									} else if (IAtomsConstants.NUMBER_EIGHT.equals(caseHandleInfoDTO.getCaseCategory())) {
										//Task #3600 08：設備逾期未還(拆機，強制解約)
										command.setQueryCaseId(apiAuthorizationInfoDTO.getCaseId());
										//command.setHasArrive(caseHandleInfoDTO.getHasArrive());
										command.setDescription(apiAuthorizationInfoDTO.getDescription());
										//修改
										sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, "changeCaseOverdueByApi", command);
										msg = sessionContext.getReturnMessage();
										//如果修改失敗
										if (msg!= null && Message.STATUS.FAILURE.equals(msg.getStatus())) {
											LOGGER.debug(".handle() -->changeCaseOverdueByApi() ststus=FAILURE msg.getCode()=" + msg.getCode());
											//案件編號 查無資料
											if (IAtomsMessageCode.PARAM_CMS_CASE_ERROR.equals(msg.getCode())) {
												responseStr = "{\"result\":\"false\",\"failReason\":\"43\"}";
											} else if (IAtomsMessageCode.VIEW_FAILURE.equals(msg.getCode())) {
												//非拆機件 不可強制解約
												responseStr = "{\"result\":\"false\",\"failReason\":\"35\"}";
											}
											apiLogDTO = dealFailReasonDesc(apiAuthorizationInfoDTO, responseStr, "", remoteAddr, IAtomsConstants.API_RS, null, "false");
											if (StringUtils.hasText(responseStr)) {
												responseStr = responseStr.substring(0, responseStr.length()-1)+",\"failReasonDesc\":\""+apiLogDTO.getFailReasonDesc()+"\"}";
											}
											if (StringUtils.hasText(responseStr)) {
												apiLogDTO.setMessage(responseStr);
											} else {
												apiLogDTO.setMessage(apiAuthorizationInfoDTO.getJsonString());
											}
											apiLogDTO.setResult("false");
											//保存log記錄
											this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
											LOGGER.debug("changeCaseOverdueByApi().saveApiLog() --> do saveLog finished...");
											return responseStr;
										}
										//如果修改成功
										if (msg!= null && Message.STATUS.SUCCESS.equals(msg.getStatus())) {
											LOGGER.debug(".handle() -->changeCaseOverdueByApi() map=success msg.getCode()=" + msg.getCode());
											LOGGER.debug(".handle() -->changeCaseOverdueByApi() result=true");
											String transactionId = (String) sessionContext.getAttribute("apiTransactionId");
											//返回 成功標誌 
											responseStr = "{\"result\":\"true\"}";
											LOGGER.debug(".handle() -->changeCaseOverdueByApi()  caseId=" + apiAuthorizationInfoDTO.getCaseId());
											apiLogDTO.setId(apiAuthorizationInfoDTO.getUuid());
											apiLogDTO.setIp(remoteAddr);
											apiLogDTO.setClientCode(apiAuthorizationInfoDTO.getClientCode());
											apiLogDTO.setFunctionCode("handle");
											apiLogDTO.setMsgType(IAtomsConstants.API_RS);//下行
											apiLogDTO.setMasterId(apiAuthorizationInfoDTO.getCaseId());
											apiLogDTO.setDetailId(transactionId);//歷程id
											apiLogDTO.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
											apiLogDTO.setCreatedById("CMS000000-0001");
											apiLogDTO.setCreatedByName("cms_to_iatoms");
											apiLogDTO.setMessage(responseStr);
											apiLogDTO.setResult("true");
											LOGGER.debug(".handle() -->Message=" + jsonStr + responseStr);
											//更新授權時間
											sessionContext = this.serviceLocator.doService(null, "apiAuthorizationInfoService", "updateApiAuthorizationInfo", apiLogDTO);
											LOGGER.debug(".handle() --> do updateApiAuthorizationInfo finished...");
											//保存log記錄
											sessionContext = this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
											LOGGER.debug(".handle() --> do saveLog finished...");
											return responseStr;
										}
									
									}
								} else {
									//唯一編號 重複 說明該筆資料已處理過
									responseStr = "{\"result\":\"false\",\"failReason\":\"02\"}";
									LOGGER.debug(".handle() --> uuid repeat...uuid=" + apiAuthorizationInfoDTO.getUuid());
									apiLogDTO = dealFailReasonDesc(apiAuthorizationInfoDTO, responseStr, "", remoteAddr, IAtomsConstants.API_RS, null, "false");
									responseStr = responseStr.substring(0, responseStr.length()-1)+",\"failReasonDesc\":\""+apiLogDTO.getFailReasonDesc()+"\"}";
									apiLogDTO.setMessage(responseStr);
									apiLogDTO.setResult("false");
									//保存log記錄
									this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
									LOGGER.debug(".saveApiLog() --> do saveLog finished...");
									return responseStr;
								}
							} else {
								//唯一編號為空
								responseStr = "{\"result\":\"false\",\"failReason\":\"03\"}";
								LOGGER.debug(".handle() --> uuid is null");
								apiLogDTO = dealFailReasonDesc(apiAuthorizationInfoDTO, responseStr, "", remoteAddr, IAtomsConstants.API_RS, null, "false");
								responseStr = responseStr.substring(0, responseStr.length()-1)+",\"failReasonDesc\":\""+apiLogDTO.getFailReasonDesc()+"\"}";
								apiLogDTO.setMessage(responseStr);
								apiLogDTO.setResult("false");
								//保存log記錄
								this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
								LOGGER.debug(".saveApiLog() --> do saveLog finished...");
								return responseStr;
							}
						} else if (apiAuthorizationInfoDTO != null) {
							//ip or token 驗證未通過
							responseStr = apiAuthorizationInfoDTO.getJsonString();
							LOGGER.debug(".handle()  --> responseStr ="+responseStr);
							apiLogDTO = dealFailReasonDesc(apiAuthorizationInfoDTO, responseStr, "", remoteAddr, IAtomsConstants.API_RS, null, "false");
							if (StringUtils.hasText(responseStr)) {
								responseStr = responseStr.substring(0, responseStr.length()-1)+",\"failReasonDesc\":\""+apiLogDTO.getFailReasonDesc()+"\"}";
							}
							apiLogDTO.setMessage(responseStr);
							apiLogDTO.setResult("false");
							//保存log記錄
							this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
							LOGGER.debug(".saveApiLog() --> do saveLog finished...");
							return responseStr;
						} 
					} else {
						LOGGER.debug(".handle()  --> apiAuthorizationInfoDTO is null....");
						return "{\"result\":\"false\",\"failReason\":\"00\",\"failReasonDesc\":\"系統出現未知異常\"}";
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("ApiRestController.handle is error...");
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
			
		}
		LOGGER.debug(".handle() --> result=false failReason is null... ");
		return "{\"result\":\"false\",\"failReason\":\"00\",\"failReasonDesc\":\"系統出現未知異常\"}";
    } 
	
	@RequestMapping(value = "/checkGpInfo", method = RequestMethod.POST, produces = "text/plain; charset=UTF-8")
	public @ResponseBody String checkGpInfo (final HttpServletRequest httpRequest, @RequestBody String jsonStr) throws CommonException{
		try {
			CaseHandleInfoDtoAPI caseHandleInfoDTO = new CaseHandleInfoDtoAPI();
			Gson gsonss = new GsonBuilder().create();
			caseHandleInfoDTO = (CaseHandleInfoDtoAPI) gsonss.fromJson(jsonStr, new TypeToken<CaseHandleInfoDtoAPI>(){}.getType());
			synchronized (caseHandleInfoDTO) {
				//返回數據
				String responseStr = IAtomsConstants.MARK_EMPTY_STRING;
				SessionContext sessionContext = null;
				//請求ip
				String remoteAddr = httpRequest.getRemoteAddr();
				LOGGER.debug(".checkGpInfo() -->remoteAddr=" + remoteAddr);
				if (StringUtils.hasText(remoteAddr)) {
					//Task #3519
					ApiLogDTO apiLogDTO = dealFailReasonDesc(jsonStr, remoteAddr, IAtomsConstants.API_RQ, "true", null);
					this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
					String caseCategory = caseHandleInfoDTO.getCaseCategory();
					String customerName = caseHandleInfoDTO.getCustomerName();
					// 刷卡機類型
					String edcTypeName = caseHandleInfoDTO.getEdcTypeName();
					if (!StringUtils.hasText(caseCategory)) {
						responseStr = "{\"result\":\"false\",\"resultDesc\":\"請輸入案件類別\"}";
						apiLogDTO = dealFailReasonDesc(responseStr, remoteAddr, IAtomsConstants.API_RS, "false", "請輸入案件類別");
						this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
						return responseStr;
					} else if (!StringUtils.hasText(customerName)) {
						responseStr = "{\"result\":\"false\",\"resultDesc\":\"請輸入案件客戶\"}";
						apiLogDTO = dealFailReasonDesc(responseStr, remoteAddr, IAtomsConstants.API_RS, "false", "請輸入案件客戶");
						this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
						return responseStr;
					} else if (!StringUtils.hasText(edcTypeName)) {
						responseStr = "{\"result\":\"false\",\"resultDesc\":\"請輸入刷卡機類型\"}";
						apiLogDTO = dealFailReasonDesc(responseStr, remoteAddr, IAtomsConstants.API_RS, "false", "請輸入刷卡機類型");
						this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
						return responseStr;
					}
					
					//"01"代表裝機
					if ("01".equals(caseCategory)) {
						//判斷當前公司是否爲環匯
						MultiParameterInquiryContext parameterInquiryContext = new MultiParameterInquiryContext();
						
						//獲取環匯客戶
						parameterInquiryContext.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue(), IAtomsConstants.PARAM_GP);
						sessionContext = this.getServiceLocator().doService(null, IAtomsConstants.SERVICE_COMPANY_SERVICE, IAtomsConstants.ACTION_GET_COMPANY_BY_COMPANY_CODE, parameterInquiryContext);
						CompanyDTO companyDTO = (CompanyDTO) sessionContext.getResponseResult();
						if (companyDTO != null) {
							if (!companyDTO.getShortName().equals(customerName)) {
								responseStr = "{\"result\":\"true\",\"resultDesc\":\"\"}";
								apiLogDTO = dealFailReasonDesc(null, remoteAddr, IAtomsConstants.API_RS, "true", "客戶不爲環匯");
								this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
								return responseStr;
							}
							//初始化交易參數數據
							parameterInquiryContext = new MultiParameterInquiryContext();
							parameterInquiryContext.addParameter(SrmTransactionParameterItemDTO.ATTRIBUTE.EFFECTIVE_DATE.getValue(), 
									DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDD_DASH));
							sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE,
									IAtomsConstants.ACTION_GET_TRANSACTION_PARAMETER_ITEM_LIST, parameterInquiryContext);
							List<SrmTransactionParameterItemDTO> transationParameterList = (List<SrmTransactionParameterItemDTO>) sessionContext.getResponseResult();
							List<SrmCaseTransactionParameterDTO> caseTransactionParameterDTOs = caseHandleInfoDTO.getItemValue();
							for (SrmCaseTransactionParameterDTO caseTransactionParameterDTO : caseTransactionParameterDTOs) {
								if ("01".equals(caseTransactionParameterDTO.getTransactionTypeName())) {
									caseTransactionParameterDTO.setTransactionType(IAtomsConstants.TRANSACTION_CATEGORY.COMMON_VM.getCode());
								} else if ("02".equals(caseTransactionParameterDTO.getTransactionTypeName())) {
									caseTransactionParameterDTO.setTransactionType(IAtomsConstants.TRANSACTION_CATEGORY.COMMON_VMJ.getCode());
								} else if ("03".equals(caseTransactionParameterDTO.getTransactionTypeName())) {
									caseTransactionParameterDTO.setTransactionType(IAtomsConstants.TRANSACTION_CATEGORY.COMMON_VMJU.getCode());
								} else if ("04".equals(caseTransactionParameterDTO.getTransactionTypeName())) {
									caseTransactionParameterDTO.setTransactionType(IAtomsConstants.TRANSACTION_CATEGORY.CUP.getCode());
								} else if ("05".equals(caseTransactionParameterDTO.getTransactionTypeName())) {
									caseTransactionParameterDTO.setTransactionType(IAtomsConstants.TRANSACTION_CATEGORY.SMART_PAY.getCode());
								} 
							}
							//驗證ip
							ApiAuthorizationInfoDTO apiAuthorizationInfoDTO = new ApiAuthorizationInfoDTO();
							apiAuthorizationInfoDTO.setRemoteAddr(remoteAddr);
							//通知 設為N
							apiAuthorizationInfoDTO.setFlag(IAtomsConstants.PARAM_NO);
							Message msg = null;
							//初始化方法 驗證ip
							sessionContext = this.serviceLocator.doService(null, "apiAuthorizationInfoService", "checkIp", apiAuthorizationInfoDTO);
							msg = sessionContext.getReturnMessage();
							apiAuthorizationInfoDTO = (ApiAuthorizationInfoDTO) sessionContext.getRequestParameter();
							//如果ip及token驗證通過
							//if (true){
							if (msg!= null && IAtomsMessageCode.IATOMS_SUCCESS.equals(msg.getCode())) {
								LOGGER.debug(".checkGpInfo() -->checkIp() msg is not null msg.getCode()=" + msg.getCode());
								//判斷刷卡機型
								if ("S80 Ethernet".equals(edcTypeName) || "S80 RF".equals(edcTypeName) || "S90 3G".equals(edcTypeName)
										|| "S90 RF".equals(edcTypeName)) {
									String softwareVersion = "";
									//獲取客戶與刷卡機型對應的最新軟體版本
									parameterInquiryContext = new MultiParameterInquiryContext();
									parameterInquiryContext.addParameter(ApplicationDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), companyDTO.getCompanyId());
									parameterInquiryContext.addParameter(ApplicationDTO.ATTRIBUTE.ASSET_TYPE_NAME.getValue(), edcTypeName);
									parameterInquiryContext.addParameter(ApplicationDTO.ATTRIBUTE.SEARCH_DELETED_FLAG.getValue(), IAtomsConstants.NO);
									sessionContext = this.getServiceLocator().doService(null, IAtomsConstants.SERVICE_APPLICATION_SERVICE, IAtomsConstants.ACTION_GET_SOFTWAREVERSIONS, parameterInquiryContext);
									List<Parameter> softwareVersions = null;
									if (sessionContext != null) {
										softwareVersions = (List<Parameter>) sessionContext.getResponseResult();
										parameterInquiryContext = new MultiParameterInquiryContext();
										parameterInquiryContext.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.SOFTWARE_VERSIONS.getValue(), softwareVersions);
										parameterInquiryContext.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CONNECTION_TYPE_NAME.getValue(), caseHandleInfoDTO.getConnectionTypeName());
										sessionContext = this.getServiceLocator().doService(null, IAtomsConstants.SERVICE_CASE_CHECK_INFO_SERVICE, "checkVersion", parameterInquiryContext);
										if (sessionContext != null) {
											softwareVersion = (String) sessionContext.getResponseResult();
										}
									}
									parameterInquiryContext = new MultiParameterInquiryContext();
									parameterInquiryContext.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CONNECTION_TYPE_NAME.getValue(), caseHandleInfoDTO.getConnectionTypeName());
									parameterInquiryContext.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.BUILT_IN_FEATURE.getValue(), caseHandleInfoDTO.getBuiltInFeature());
									parameterInquiryContext.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.RECEIPT_TYPE.getValue(), caseHandleInfoDTO.getReceiptTypeName());
									parameterInquiryContext.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASETRANSACTION_PARAMETER_DTOS.getValue(), caseTransactionParameterDTOs);
									parameterInquiryContext.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.SOFTWARE_VERSIONS.getValue(), softwareVersions);
									parameterInquiryContext.addParameter("isUpload", Boolean.TRUE);
									if (!"S80 RF".equals(edcTypeName)) {
										parameterInquiryContext.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_NAME.getValue(), caseHandleInfoDTO.getPeripheralsName());
										parameterInquiryContext.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_NAME.getValue(), caseHandleInfoDTO.getPeripherals2Name());
										parameterInquiryContext.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_NAME.getValue(), caseHandleInfoDTO.getPeripherals3Name());
									}
									if ("S80 Ethernet".equals(edcTypeName)) {
										parameterInquiryContext.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION.getValue(), caseHandleInfoDTO.getPeripheralsFunction());
										parameterInquiryContext.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION2.getValue(), caseHandleInfoDTO.getPeripheralsFunction2());
										parameterInquiryContext.addParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION3.getValue(), caseHandleInfoDTO.getPeripheralsFunction3());
										sessionContext = this.getServiceLocator().doService(null, IAtomsConstants.SERVICE_CASE_CHECK_INFO_SERVICE, IAtomsConstants.ACTION_CHECKS80_ETHERNET_ASSET, parameterInquiryContext);
									} else if ("S80 RF".equals(edcTypeName)) {
										sessionContext = this.getServiceLocator().doService(null, IAtomsConstants.SERVICE_CASE_CHECK_INFO_SERVICE, IAtomsConstants.ACTION_CHECKS80_RF_ASSET, parameterInquiryContext);
									} else if ("S90 RF".equals(edcTypeName)) {
										sessionContext = this.getServiceLocator().doService(null, IAtomsConstants.SERVICE_CASE_CHECK_INFO_SERVICE, IAtomsConstants.ACTION_CHECKS90_RF_ASSET, parameterInquiryContext);
									} else if ("S90 3G".equals(edcTypeName)) {
										sessionContext = this.getServiceLocator().doService(null, IAtomsConstants.SERVICE_CASE_CHECK_INFO_SERVICE, IAtomsConstants.ACTION_CHECKS90_3G_ASSET, parameterInquiryContext);
									}
									if (sessionContext != null) {
										String message = (String) sessionContext.getResponseResult();
										if (StringUtils.hasText(message)) {
											List<String> errorMsgs = StringUtils.toList(message, IAtomsConstants.MARK_SEMICOLON);
											StringBuffer errorMsg = new StringBuffer();
											errorMsg.append("[");
											for (String error : errorMsgs) {
												errorMsg.append("{\"err\":\"").append(i18NUtil.getName(error)).append("\"},");
											}
											//message = i18NUtil.getName(message);
											message = errorMsg.substring(0, errorMsg.length() -1) + "]";
											responseStr = "{\"result\":\"false\",\"resultDesc\":" + message + ", \"version\":\"" + softwareVersion + "\"}";
											apiLogDTO = dealFailReasonDesc(responseStr, remoteAddr, IAtomsConstants.API_RS, "false", message);
											this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
										} else {
											responseStr = "{\"result\":\"true\",\"resultDesc\":\"\", \"version\":\"" + softwareVersion + "\"}";
											apiLogDTO = dealFailReasonDesc(responseStr, remoteAddr, IAtomsConstants.API_RS, "true", null);
											this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
										}
									} else {
										responseStr = "{\"result\":\"true\",\"resultDesc\":\"\", \"version\":\"" + softwareVersion + "\"}";
										apiLogDTO = dealFailReasonDesc(responseStr, remoteAddr, IAtomsConstants.API_RS, "true", null);
										this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
									}
								} else {
									responseStr = "{\"result\":\"true\",\"resultDesc\":\"\"}";
									apiLogDTO = dealFailReasonDesc(responseStr, remoteAddr, IAtomsConstants.API_RS, "true", "刷卡機型不爲S80 Ethernet、S80 RF、S90 RF、S90 3G");
									this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
								}
								return responseStr;
							} else if (apiAuthorizationInfoDTO != null) {
								//ip or token 驗證未通過
								responseStr = apiAuthorizationInfoDTO.getJsonString();
								LOGGER.debug(".handle()  --> responseStr ="+responseStr);
								apiLogDTO = dealFailReasonDesc(responseStr, remoteAddr, IAtomsConstants.API_RS, "false", null);
								this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
								return responseStr;
							} 
						} else {
							responseStr = "{\"result\":\"false\",\"resultDesc\":\"系統發生未知錯誤\"}";
							apiLogDTO = dealFailReasonDesc(responseStr, remoteAddr, IAtomsConstants.API_RS, "false", "系統發生未知錯誤");
							this.serviceLocator.doService(null, "apiAuthorizationInfoService", "saveLog", apiLogDTO);
							return responseStr;
						}
					} else {
						responseStr = "{\"result\":\"true\",\"resultDesc\":\"\"}";
						return responseStr;
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("ApiRestController.checkGpInfo is error...");
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		LOGGER.debug(".checkGpInfo() --> result=false failReason is null... ");
		return "{\"result\":\"false\", \"failReasonDesc\":\"系統出現未知異常\"}";
	}
	
	/**
	 * 保存下行失敗log，保存上行電文記錄log
	 * @param apiAuthorizationInfoDTO
	 * @param responseStr
	 * @param jsonStr
	 * @param remoteAddr
	 * @throws CommonException
	 */
	private ApiLogDTO dealFailReasonDesc(ApiAuthorizationInfoDTO apiAuthorizationInfoDTO, String responseStr, String jsonStr, String remoteAddr, String msgType, String detailId, String result) throws CommonException{
		try {
			ApiLogDTO apiLogDTO = new ApiLogDTO();
			apiLogDTO.setId(null);
			apiLogDTO.setIp(remoteAddr);
			apiLogDTO.setClientCode(apiAuthorizationInfoDTO.getClientCode());
			apiLogDTO.setFunctionCode("handle");
			apiLogDTO.setMsgType(msgType);//下行 RS , 上行RQ
			apiLogDTO.setMasterId(("05".equals(apiAuthorizationInfoDTO.getCaseCategory()) || "06".equals(apiAuthorizationInfoDTO.getCaseCategory())) 
												? apiAuthorizationInfoDTO.getSerialNumber() 
												: ("01".equals(apiAuthorizationInfoDTO.getCaseCategory()) 
															|| "03".equals(apiAuthorizationInfoDTO.getCaseCategory()) 
															|| "07".equals(apiAuthorizationInfoDTO.getCaseCategory())
															|| "08".equals(apiAuthorizationInfoDTO.getCaseCategory()))?apiAuthorizationInfoDTO.getCaseId() : null);
			apiLogDTO.setDetailId(detailId);//歷程id
			apiLogDTO.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
			apiLogDTO.setCreatedById("CMS000000-0001");
			apiLogDTO.setCreatedByName("cms_to_iatoms");
			apiLogDTO.setResult(result);
			LOGGER.debug(".dealfailReasonDesc() --> remoteAddr=" + remoteAddr);
			LOGGER.debug(".dealfailReasonDesc() --> ClientCode=" + apiAuthorizationInfoDTO.getClientCode());
			LOGGER.debug(".dealfailReasonDesc() --> CaseCategory=" + apiAuthorizationInfoDTO.getCaseCategory());
			
			if (StringUtils.hasText(responseStr)) {
				apiLogDTO.setMessage(responseStr);
			} else {
				apiLogDTO.setMessage(jsonStr);
			}
			LOGGER.debug(".dealfailReasonDesc() --> Message=" + apiLogDTO.getMessage());

			if (StringUtils.hasText(apiLogDTO.getMessage())) {
				if (apiLogDTO.getMessage().indexOf("failReason\":\"0") > 0
						||apiLogDTO.getMessage().indexOf("failReason\":\"1") > 0
						||apiLogDTO.getMessage().indexOf("failReason\":\"2") > 0
						||apiLogDTO.getMessage().indexOf("failReason\":\"3") > 0
						||apiLogDTO.getMessage().indexOf("failReason\":\"4") > 0) {
					
					if (apiLogDTO.getMessage().indexOf("failReason\":\"01\"") > 0) {
						apiLogDTO.setFailReasonDesc("令牌過期");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"02\"") > 0) {
						apiLogDTO.setFailReasonDesc("資料重複（說明該筆資料已處理過）");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"03\"") > 0) {
						apiLogDTO.setFailReasonDesc("唯一編號為空");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"04\"") > 0) {
						apiLogDTO.setFailReasonDesc("聯繫地址-縣市查無資料");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"05\"") > 0) {
						apiLogDTO.setFailReasonDesc("特店代號查無資料");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"06\"") > 0) {
						apiLogDTO.setFailReasonDesc("dtid查無資料");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"07\"") > 0) {
						apiLogDTO.setFailReasonDesc("設備序號查無資料");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"08\"") > 0) {
						apiLogDTO.setFailReasonDesc("帳號已被鎖定");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"09\"") > 0) {
						apiLogDTO.setFailReasonDesc("ip錯誤");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"10\"") > 0) {
						apiLogDTO.setFailReasonDesc("token錯誤");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"11\"") > 0) {
						apiLogDTO.setFailReasonDesc("uuid長度不可大於32位");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"12\"") > 0) {
						apiLogDTO.setFailReasonDesc("案件類別為空");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"13\"") > 0) {
						apiLogDTO.setFailReasonDesc("（案件類別=02、04）說明長度不可大於1000位");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"14\"") > 0) {
						apiLogDTO.setFailReasonDesc("dtid為空");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"15\"") > 0) {
						apiLogDTO.setFailReasonDesc("dtid長度不可大於8位");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"16\"") > 0) {
						apiLogDTO.setFailReasonDesc("特店代號為空");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"17\"") > 0) {
						apiLogDTO.setFailReasonDesc("特店代號長度不可大於20位");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"18\"") > 0) {
						apiLogDTO.setFailReasonDesc("聯系聯絡人為空");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"19\"") > 0) {
						apiLogDTO.setFailReasonDesc("聯系聯絡人長度不可大於50位");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"20\"") > 0) {
						apiLogDTO.setFailReasonDesc("聯系手機為空");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"21\"") > 0) {
						apiLogDTO.setFailReasonDesc("聯系手機長度不可大於10位");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"22\"") > 0) {
						apiLogDTO.setFailReasonDesc("聯系手機格式錯誤");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"23\"") > 0) {
						apiLogDTO.setFailReasonDesc("聯繫電話為空");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"24\"") > 0) {
						apiLogDTO.setFailReasonDesc("聯繫電話長度不可大於20位");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"25\"") > 0) {
						apiLogDTO.setFailReasonDesc("聯繫EMAIL為空");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"26\"") > 0) {
						apiLogDTO.setFailReasonDesc("聯繫EMAIL長度不可大於100位");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"27\"") > 0) {
						apiLogDTO.setFailReasonDesc("聯繫EMAIL格式錯誤");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"28\"") > 0) {
						apiLogDTO.setFailReasonDesc("聯繫地址-縣市為空");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"29\"") > 0) {
						apiLogDTO.setFailReasonDesc("聯繫地址為空");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"30\"") > 0) {
						apiLogDTO.setFailReasonDesc("聯繫地址長度不可大於100位");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"31\"") > 0) {
						apiLogDTO.setFailReasonDesc("拆機類型為空");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"32\"") > 0) {
						apiLogDTO.setFailReasonDesc("無效拆機類型");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"33\"") > 0) {
						apiLogDTO.setFailReasonDesc("設備序號為空");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"34\"") > 0) {
						apiLogDTO.setFailReasonDesc("設備序號長度不可大於20位");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"35\"") > 0) {
						apiLogDTO.setFailReasonDesc("無效案件類別");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"36\"") > 0) {
						apiLogDTO.setFailReasonDesc("聯繫郵遞區號為空");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"37\"") > 0) {
						apiLogDTO.setFailReasonDesc("聯繫郵遞區域為空");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"38\"") > 0) {
						apiLogDTO.setFailReasonDesc("聯繫郵遞區號(郵遞區域)查無資料");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"39\"") > 0) {
						apiLogDTO.setFailReasonDesc("（案件類別=05、06）說明長度不可大於200位");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"40\"") > 0) {
						apiLogDTO.setFailReasonDesc("到場註記為空");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"41\"") > 0) {
						apiLogDTO.setFailReasonDesc("無效到場註記");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"42\"") > 0) {
						apiLogDTO.setFailReasonDesc("案件編號為空");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"43\"") > 0) {
						apiLogDTO.setFailReasonDesc("案件編號查無資料");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"44\"") > 0) {
						apiLogDTO.setFailReasonDesc("求償案件狀態不爲求償確認，不可進行完成操作");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"00\"") > 0) {
						apiLogDTO.setFailReasonDesc("系統出現未知異常");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"45\"") > 0) {
						apiLogDTO.setFailReasonDesc("求償編號爲空");
					} else if (apiLogDTO.getMessage().indexOf("failReason\":\"46\"") > 0) {
						apiLogDTO.setFailReasonDesc("求償作業查無資料");
					}
				}
			}
			LOGGER.debug(".dealfailReasonDesc() --> failReasonDesc=" + apiLogDTO.getFailReasonDesc());
			return apiLogDTO;
		} catch (Exception e) {
			LOGGER.error("ApiRestController.saveApiLog is error...");
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	
	/**
	 * Purpose:
	 * @author CarrieDuan
	 * @param jsonStr
	 * @param remoteAddr
	 * @param msgType
	 * @throws CommonException
	 * @return ApiLogDTO
	 */
	private ApiLogDTO dealFailReasonDesc(String jsonStr, String remoteAddr, String msgType, String result, String failReasonDesc) throws CommonException{
		ApiLogDTO apiLogDTO = new ApiLogDTO();
		apiLogDTO.setIp(remoteAddr);
		apiLogDTO.setClientCode(IAtomsConstants.PARAM_CHECK_GP_CLIENT_CODE);
		apiLogDTO.setFunctionCode("checkGpInfo");
		apiLogDTO.setMsgType(msgType);//上行
		apiLogDTO.setResult(result);
		apiLogDTO.setMessage(jsonStr);
		apiLogDTO.setFailReasonDesc(failReasonDesc);
		apiLogDTO.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
		apiLogDTO.setCreatedById("CMS000000-0001");
		apiLogDTO.setCreatedByName("cms_to_iatoms");
		return apiLogDTO;
	}
	
	/**
	 * @return the serviceLocator
	 */
	public IServiceLocatorProxy getServiceLocator() {
		return serviceLocator;
	}
	/**
	 * @param serviceLocator the serviceLocator to set
	 */
	public void setServiceLocator(IServiceLocatorProxy serviceLocator) {
		this.serviceLocator = serviceLocator;
	}
	
}
