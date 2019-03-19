package com.cybersoft4u.xian.iatoms.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.Message;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.SessionContext;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.convert.SimpleDtoDmoTransformer;
import cafe.core.util.convert.Transformer;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ApiAuthorizationInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ApiLogDTO;
import com.cybersoft4u.xian.iatoms.common.utils.PasswordEncoderUtilities;
import com.cybersoft4u.xian.iatoms.services.IApiAuthorizationInfoService;
import com.cybersoft4u.xian.iatoms.services.dao.IApiAuthorizationInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IApiLogDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.ApiAuthorizationInfo;

public class ApiAuthorizationInfoService extends AtomicService implements IApiAuthorizationInfoService {

	/**
	 * 系统日志文件控件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, ApiAuthorizationInfoService.class);
	
	/**
	 * 授權資料DAO
	 */
	private IApiAuthorizationInfoDAO apiAuthorizationInfoDAO;
	/**
	 * api log DAO
	 */
	private IApiLogDAO apiLogDAO;
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.atoms.services.IApiAuthorizationInfoService#init(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext init(SessionContext sessionContext)
			throws ServiceException {
		Message msg = null;
		try {
			ApiAuthorizationInfoDTO apiAuthorizationInfoDTO = (ApiAuthorizationInfoDTO) sessionContext.getRequestParameter();
			String remoteAddr = apiAuthorizationInfoDTO.getRemoteAddr();
			if(apiAuthorizationInfoDTO != null && StringUtils.hasText(remoteAddr)){
				List<ApiAuthorizationInfoDTO> apiAuthorizationInfoDTOList = this.apiAuthorizationInfoDAO.getAuthorizationInfoList(remoteAddr);
				if (!CollectionUtils.isEmpty(apiAuthorizationInfoDTOList) && apiAuthorizationInfoDTOList.get(0)!= null) {
					ApiAuthorizationInfoDTO dbApiAuthorizationInfoDTO = apiAuthorizationInfoDTOList.get(0);
					if ((IAtomsConstants.ACCOUNT_STATUS_NORMAL.equals(dbApiAuthorizationInfoDTO.getStatus()))
								|| (IAtomsConstants.ACCOUNT_STATUS_NEW.equals(dbApiAuthorizationInfoDTO.getStatus()))) {
						//有授權碼，授權獲取令牌
						if ((StringUtils.hasText(apiAuthorizationInfoDTO.getCode()))
									&& (IAtomsConstants.PARAM_YES.equals(apiAuthorizationInfoDTO.getFlag()))) {
							LOGGER.debug(".init(SessionContext sessionContext) --> code=" + apiAuthorizationInfoDTO.getCode());
							//授權
							token(apiAuthorizationInfoDTO,dbApiAuthorizationInfoDTO);
							if(StringUtils.hasText(apiAuthorizationInfoDTO.getToken())){
								msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.LOGIN_SUCCESS);
							} else {
								LOGGER.error("can not get token..." );
								msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.LOGIN_FAILED);
							}
						} else {
							apiAuthorizationInfoDTO.setIp(dbApiAuthorizationInfoDTO.getIp());
							apiAuthorizationInfoDTO.setClientCode(dbApiAuthorizationInfoDTO.getClientCode());
							//檢核令牌
							if (StringUtils.hasText(apiAuthorizationInfoDTO.getToken()) 
											&& apiAuthorizationInfoDTO.getToken().equals(dbApiAuthorizationInfoDTO.getToken())) {
								//如果當前user 無須令牌 hastoken為N，則不再驗證時效
								if("N".equals(dbApiAuthorizationInfoDTO.getHasToken())){
									msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.IATOMS_SUCCESS);
								} else {
									long nowTime = DateTimeUtils.getCurrentTimestamp().getTime();
									long authorizationTime = dbApiAuthorizationInfoDTO.getAuthorizationTime().getTime();
									if (nowTime - authorizationTime > apiAuthorizationInfoDTO.getMaxInactiveInterval()*1000) {
										//令牌過期
										apiAuthorizationInfoDTO.setJsonString("{\"result\":\"false\",\"failReason\":\"01\"}");
										msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.CHECK_PWD_ERROR);
									} else if (nowTime >= authorizationTime) {
										msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.IATOMS_SUCCESS);
									}
								}
								
							} else {
								//token錯誤
								apiAuthorizationInfoDTO.setJsonString("{\"result\":\"false\",\"failReason\":\"10\"}");
								msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.CHECK_PWD_ERROR);
							}
						}
					} else if (IAtomsConstants.ACCOUNT_STATUS_LOCK.equals(dbApiAuthorizationInfoDTO.getStatus())) {
						if (IAtomsConstants.PARAM_NO.equals(apiAuthorizationInfoDTO.getFlag())) {
							//帳號已被鎖定
							apiAuthorizationInfoDTO.setJsonString("{\"result\":\"false\",\"failReason\":\"08\"}");
						} else {
							apiAuthorizationInfoDTO.setJsonString("{\"result\":\"false\",\"token\":\"\"}");
						}
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.CHECK_PWD_ERROR);
					} else {
						if (IAtomsConstants.PARAM_NO.equals(apiAuthorizationInfoDTO.getFlag())) {
							//ip錯誤
							apiAuthorizationInfoDTO.setJsonString("{\"result\":\"false\",\"failReason\":\"09\"}");
						} else {
							apiAuthorizationInfoDTO.setJsonString("{\"result\":\"false\",\"token\":\"\"}");
						}
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INIT_PAGE_FAILURE);
					}
				} else {
					if (IAtomsConstants.PARAM_NO.equals(apiAuthorizationInfoDTO.getFlag())) {
						//ip錯誤
						apiAuthorizationInfoDTO.setJsonString("{\"result\":\"false\",\"failReason\":\"09\"}");
					} else {
						apiAuthorizationInfoDTO.setJsonString("{\"result\":\"false\",\"token\":\"\"}");
					}
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INIT_PAGE_FAILURE);
				}
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(apiAuthorizationInfoDTO);
		} catch (Exception e) {
			LOGGER.error(".init(SessionContext sessionContext)", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * 
	 * Purpose: 授權 獲取令牌
	 * @author amandawang
	 * @param apiAuthorizationInfoDTO：外部系統
	 * @throws ServiceException Service異常類
	 * @return SessionContext 上下文Context
	 */
	public ApiAuthorizationInfoDTO token(ApiAuthorizationInfoDTO apiAuthorizationInfoDTO,ApiAuthorizationInfoDTO dbApiAuthorizationInfoDTO)
			throws ServiceException {
		try {
			//令牌
			String token = null;
			String deToken = null;
			String verificationCode = apiAuthorizationInfoDTO.getCode();
			boolean verificationFlag = true;
			Transformer transformer = new SimpleDtoDmoTransformer();
			ApiAuthorizationInfo apiAuthorizationInfo = null;
			apiAuthorizationInfo = (ApiAuthorizationInfo)transformer.transform(dbApiAuthorizationInfoDTO, new ApiAuthorizationInfo());
			if(apiAuthorizationInfo != null){
				//獲取授權
				if(StringUtils.hasText(verificationCode)){
					LOGGER.debug(".token(SessionContext sessionContext) --> verificationCode=" + verificationCode);
					//密鈅：apiAuthorizationInfoDTOList.get(0).getIp() + apiAuthorizationInfoDTOList.get(0).getClientCode()
					//用127.0.0.1CMS解密verificationCode得到 clientCode：CMS
					String clientCode = PasswordEncoderUtilities.decrypt(verificationCode,dbApiAuthorizationInfoDTO.getIp() + dbApiAuthorizationInfoDTO.getClientCode());
					LOGGER.debug(".token(SessionContext sessionContext) --> clientCode=" + clientCode);
					//客戶碼
					if(dbApiAuthorizationInfoDTO.getClientCode().equals(clientCode)){
						//如果是 無須token驗證的客戶 ，將token存為verificationCode update 2018/04/16
						if ("Y".equals(dbApiAuthorizationInfoDTO.getHasToken())) {
							LOGGER.debug(".token(SessionContext sessionContext) --> HasToken=" + dbApiAuthorizationInfoDTO.getHasToken());
							//隨機數
							token =UUID.randomUUID().toString();
							deToken = PasswordEncoderUtilities.encrypt(token, dbApiAuthorizationInfoDTO.getIp() + dbApiAuthorizationInfoDTO.getClientCode());
							apiAuthorizationInfoDTO.setJsonString("{\"result\":\"true\",\"token\":\""+deToken+"\"}");
							apiAuthorizationInfoDTO.setToken(deToken);
							apiAuthorizationInfo.setToken(deToken);
						} else {
							LOGGER.debug(".token(SessionContext sessionContext) --> HasToken=" + dbApiAuthorizationInfoDTO.getHasToken());
							apiAuthorizationInfoDTO.setJsonString("{\"result\":\"true\",\"token\":\""+verificationCode+"\"}");
							apiAuthorizationInfoDTO.setToken(verificationCode);
							apiAuthorizationInfo.setToken(verificationCode);
						}
						apiAuthorizationInfoDTO.setIp(dbApiAuthorizationInfoDTO.getIp());
						apiAuthorizationInfoDTO.setClientCode(dbApiAuthorizationInfoDTO.getClientCode());
						apiAuthorizationInfo.setStatus(IAtomsConstants.ACCOUNT_STATUS_NORMAL);
						
						apiAuthorizationInfo.setRetry(0);
						apiAuthorizationInfo.setAuthorizationTime(DateTimeUtils.getCurrentTimestamp());
						this.apiAuthorizationInfoDAO.update(apiAuthorizationInfo);
						return apiAuthorizationInfoDTO;
					} else {
						verificationFlag = false;
					}
				} else {
					verificationFlag = false;
				}
				//授權失敗
				if (!verificationFlag) {
					if (apiAuthorizationInfo != null) {
						apiAuthorizationInfo.setRetry(apiAuthorizationInfo.getRetry() + 1);
						//驗證連續錯3次以上，鎖定
						if (apiAuthorizationInfo.getRetry() >= 3) {
							apiAuthorizationInfo.setStatus(IAtomsConstants.ACCOUNT_STATUS_LOCK);
						}
						this.apiAuthorizationInfoDAO.update(apiAuthorizationInfo);
					}
					apiAuthorizationInfoDTO.setJsonString("{\"result\":\"false\",\"token\":\"\"}");
					LOGGER.error("wrong verification code from:" + apiAuthorizationInfoDTO.getIp());
					return apiAuthorizationInfoDTO;
				}
			}
			
		} catch (Exception e) {
			LOGGER.error(".token(SessionContext sessionContext)", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return apiAuthorizationInfoDTO;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.atoms.services.IApiAuthorizationInfoService#checkId(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext checkId(SessionContext sessionContext)
			throws ServiceException{
		try{
			Message msg = null;
			String id = (String) sessionContext.getRequestParameter();
			LOGGER.debug(".checkId(SessionContext sessionContext) --> id="+id);
			if(StringUtils.hasText(id)){
				ApiLogDTO apiLogDTO = this.apiLogDAO.getApiLogDto(id);
				try {
					if(apiLogDTO!= null){
						LOGGER.debug(".checkId(SessionContext sessionContext) -->id is already exist, id =" + id);
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.IATOMS_FAILURE);
						sessionContext.setReturnMessage(msg);
						return sessionContext;
					}
				} catch (Throwable e) {
					LOGGER.error(".checkId(SessionContext sessionContext) apiLogDTO transform apiLog failed -->id=" + id, e);
					e.printStackTrace();
				}
				
			}
			msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.IATOMS_SUCCESS);
			sessionContext.setReturnMessage(msg);
			return sessionContext;
		} catch (Exception e) {
			LOGGER.error(".checkId(SessionContext sessionContext)", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.atoms.services.IApiAuthorizationInfoService#updateApiAuthorizationInfo(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext updateApiAuthorizationInfo(SessionContext sessionContext)
			throws ServiceException{
		try{
			ApiLogDTO apiLogDTO = (ApiLogDTO) sessionContext.getRequestParameter();
			LOGGER.debug(".updateApiAuthorizationInfo(SessionContext sessionContext) --> logTime=" + apiLogDTO.getCreatedDate());
			LOGGER.debug(".updateApiAuthorizationInfo(SessionContext sessionContext) --> ip=" + apiLogDTO.getIp());
			this.apiAuthorizationInfoDAO.updateApiAuthorizationInfo(apiLogDTO.getIp(), apiLogDTO.getCreatedDate());
			LOGGER.debug(".updateApiAuthorizationInfo(SessionContext sessionContext) --> updateApiAuthorizationInfo is Success...");
			return sessionContext;
		} catch (Exception e) {
			LOGGER.error(".updateApiAuthorizationInfo(SessionContext sessionContext)", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.atoms.services.IApiAuthorizationInfoService#saveLog(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext saveLog(SessionContext sessionContext)
			throws ServiceException{
		try{
			ApiLogDTO apiLogDTO = (ApiLogDTO) sessionContext.getRequestParameter();
			if (!StringUtils.hasText(apiLogDTO.getIp())) {
				apiLogDTO.setIp(IAtomsConstants.MARK_SPACE);
			}
			if (!StringUtils.hasText(apiLogDTO.getClientCode())) {
				apiLogDTO.setClientCode(IAtomsConstants.MARK_SPACE);
			}
			if (!StringUtils.hasText(apiLogDTO.getFunctionCode())) {
				apiLogDTO.setFunctionCode(IAtomsConstants.MARK_SPACE);
			}
			if (!StringUtils.hasText(apiLogDTO.getMessage())) {
				apiLogDTO.setMessage(IAtomsConstants.MARK_SPACE);
			}
			if (!StringUtils.hasText(apiLogDTO.getResult())) {
				apiLogDTO.setResult(IAtomsConstants.MARK_SPACE);
			}
			if(apiLogDTO!= null){
				LOGGER.debug(".saveLog(SessionContext sessionContext) -->apiLogDTO is not null Id=" + apiLogDTO.getId());
				if(!StringUtils.hasText(apiLogDTO.getId())){
					apiLogDTO.setId(this.generateGeneralUUID("API_LOG"));
					LOGGER.debug(".saveLog(SessionContext sessionContext) -->apiLogDTO Id is null new id =" + apiLogDTO.getId());
				}
			}
			this.apiLogDAO.insertApiLog(apiLogDTO);
			LOGGER.debug(".saveLog(SessionContext sessionContext) --> insertApiLog is Success...");
		} catch (Exception e) {
			LOGGER.error(".saveLog(SessionContext sessionContext)", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IApiAuthorizationInfoService#checkIp(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext checkIp(SessionContext sessionContext) throws ServiceException {
		try {
			Message msg = null;
			ApiAuthorizationInfoDTO apiAuthorizationInfoDTO = (ApiAuthorizationInfoDTO) sessionContext.getRequestParameter();
			String remoteAddr = apiAuthorizationInfoDTO.getRemoteAddr();
			if(apiAuthorizationInfoDTO != null && StringUtils.hasText(remoteAddr)){
				List<ApiAuthorizationInfoDTO> apiAuthorizationInfoDTOList = this.apiAuthorizationInfoDAO.getAuthorizationInfoList(remoteAddr);
				if (!CollectionUtils.isEmpty(apiAuthorizationInfoDTOList) && apiAuthorizationInfoDTOList.get(0)!= null) {
					ApiAuthorizationInfoDTO dbApiAuthorizationInfoDTO = apiAuthorizationInfoDTOList.get(0);
					if ((IAtomsConstants.ACCOUNT_STATUS_NORMAL.equals(dbApiAuthorizationInfoDTO.getStatus()))
								|| (IAtomsConstants.ACCOUNT_STATUS_NEW.equals(dbApiAuthorizationInfoDTO.getStatus()))) {
						msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.IATOMS_SUCCESS);
					} else if (IAtomsConstants.ACCOUNT_STATUS_LOCK.equals(dbApiAuthorizationInfoDTO.getStatus())) {
						//帳號已被鎖定
						apiAuthorizationInfoDTO.setJsonString("{\"result\":\"false\",\"resultDesc\":\"帳號已被鎖定\"}");
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.CHECK_PWD_ERROR);
					} else {
						//ip錯誤
						apiAuthorizationInfoDTO.setJsonString("{\"result\":\"false\",\"resultDesc\":\"ip錯誤\"}");
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INIT_PAGE_FAILURE);
					}
				} else {
					//ip錯誤
					apiAuthorizationInfoDTO.setJsonString("{\"result\":\"false\",\"resultDesc\":\"ip錯誤\"}");
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INIT_PAGE_FAILURE);
				}
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(apiAuthorizationInfoDTO);
		} catch (Exception e) {
			LOGGER.error(".token(SessionContext sessionContext)", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	
	/**
	 * @return the apiAuthorizationInfoDAO
	 */
	public IApiAuthorizationInfoDAO getApiAuthorizationInfoDAO() {
		return apiAuthorizationInfoDAO;
	}
	/**
	 * @param apiAuthorizationInfoDAO the apiAuthorizationInfoDAO to set
	 */
	public void setApiAuthorizationInfoDAO(
			IApiAuthorizationInfoDAO apiAuthorizationInfoDAO) {
		this.apiAuthorizationInfoDAO = apiAuthorizationInfoDAO;
	}
	/**
	 * @return the apiLogDAO
	 */
	public IApiLogDAO getApiLogDAO() {
		return apiLogDAO;
	}
	/**
	 * @param apiLogDAO the apiLogDAO to set
	 */
	public void setApiLogDAO(IApiLogDAO apiLogDAO) {
		this.apiLogDAO = apiLogDAO;
	}
}
