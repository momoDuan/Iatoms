package com.cybersoft4u.xian.iatoms.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Message;
import cafe.core.bean.identity.LogonUser;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.SessionContext;
import cafe.core.dao.DataAccessException;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.workflow.service.identity.impl.WfIdentityService;

import com.cybersoft4u.xian.iatoms.common.IAtomsAccessControl;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.PasswordSettingDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmFunctionTypeDTO;
import com.cybersoft4u.xian.iatoms.common.utils.IAtomsDateTimeUtils;
import com.cybersoft4u.xian.iatoms.common.utils.PasswordEncoderUtilities;
import com.cybersoft4u.xian.iatoms.services.IIAtomsIdentityService;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IIAtomsIdentityDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IPasswordSettingDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmSystemLogging;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmUser;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCompany;
import com.cybersoft4u.xian.iatoms.services.dmo.SessionLogging;
/**
 * Purpose: 识别管理服务类别 IMPL
 * @author candicechen
 * @since  JDK 1.6
 * @date   2015年4月20日
 * @MaintenancePersonnel candicechen
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class IAtomsIdentityService extends WfIdentityService<IIAtomsIdentityDAO> implements IIAtomsIdentityService {
	/**
	 * log
	 */
	private static CafeLog log = CafeLogFactory.getLog(GenericConfigManager.SERVICE, IAtomsIdentityService.class);
	/**
	 * 识别管理服务类别DAO介面
	 */
	private IIAtomsIdentityDAO iAtomsIdentityDAO;
	/**
	 * 使用者DAO
	 */
	private IAdmUserDAO admUserDAO;
	/**
	 * 密碼設定DAO
	 */
	private IPasswordSettingDAO passwordSettingDAO;
	/**
	 * 密碼設定DTO
	 */
	private PasswordSettingDTO admSecurityDefDTO;	
	
	/**
	 * 公司DAO
	 */
	private ICompanyDAO companyDAO;
	
	/**
	 * Constructor:无参构造函数
	 */
	public IAtomsIdentityService() {
		
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IIAtomsIdentityService#initPasswordSetting()
	 */
	@Override
	public void initPasswordSetting() throws ServiceException {
		try {
			admSecurityDefDTO = passwordSettingDAO.getPasswordSettingInfo();
		} catch (DataAccessException e) {
			log.error(this.getClass().getName() + ".initPasswordSetting() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			log.error(this.getClass().getName() + ".initPasswordSetting() Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.service.identity.impl.IdentityService#logon(cafe.core.context.SessionContext)
	 */
	public SessionContext<LogonUser, LogonUser, Message> logon(SessionContext<LogonUser, LogonUser, Message> sessionContext) throws ServiceException {
		IAtomsLogonUser logonUser = (IAtomsLogonUser)sessionContext.getRequestParameter();
		try {
			log.debug("Enter "+this.getClass().getName()+"logon start!!!");
			String userAccount = logonUser.getUserCode();
			log.debug("Enter "+this.getClass().getName()+"logon param userId --->"+userAccount);
			String sessionId = logonUser.getSessionId();
			log.debug("Enter "+this.getClass().getName()+"logon param sessionId --->"+sessionId);
			
			//查詢密碼設定信息
			initPasswordSetting();
			
			//查詢用戶信息
			AdmUserDTO admUserDTO = this.iAtomsIdentityDAO.getAdmUserDTOByAccount(userAccount);
			
			//1. 若無此帳號，錯誤訊息「此帳號不存在，請洽系統管理員」
			if(admUserDTO == null){
				log.error("The user "+userAccount+" is not exist!");
				throw new ServiceException(IAtomsMessageCode.ACCOUNT_ID_NOT_FOUND);
			}
			logonUser.setId(admUserDTO.getUserId());
			String accouontStatus = admUserDTO.getStatus();
			//2. 若該帳號已被停用，帳號狀態為“終止”，錯誤訊息「此帳號已被停用，請洽系統管理員」
			if (IAtomsConstants.ACCOUNT_STATUS_DISABLED.equals(accouontStatus)){
				log.error("The account "+userAccount+" is disabled!");
				throw new ServiceException(IAtomsMessageCode.ACCOUNT_ID_DISABLED);
			}
			//3. (d)若帳號長期未登入且超過【帳號停權週期】，錯誤訊息「帳號已被停權無法登入，請洽系統管理員重新設定後才可登入」并更新帳號狀態為“停權”
			if (IAtomsConstants.ACCOUNT_STATUS_STOP_RIGHT.equals(accouontStatus)) {
				log.error("The account "+userAccount+" 長期未登入且超過【帳號停權週期】!");
				throw new ServiceException(IAtomsMessageCode.ACCOUNT_ID_STOP_RIGHT);
			} else {
				if(!StringUtils.hasText(admUserDTO.getResetPwd()) || !admUserDTO.getResetPwd().equals(IAtomsConstants.YES)){
					Date lastLoginDate = admUserDTO.getLastLoginDate();
					if (lastLoginDate != null && admSecurityDefDTO != null) {
						int dateSubtractionDay = IAtomsDateTimeUtils.dateSubtractionDay(lastLoginDate, DateTimeUtils.getCurrentTimestamp());
						if (dateSubtractionDay > admSecurityDefDTO.getIdValidDay()) {
							admUserDTO.setStatus(IAtomsConstants.ACCOUNT_STATUS_STOP_RIGHT);
							admUserDAO.updateUser(admUserDTO);
							log.error("The account "+userAccount+" 長期未登入且超過【帳號停權週期】!");
							sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, 								
									IAtomsMessageCode.ACCOUNT_ID_STOP_RIGHT));
							return sessionContext;	
							//throw new ServiceException(IAtomsMessageCode.ACCOUNT_ID_STOP_RIGHT);
						}
					}
				}
			}
			//4. 此帳號已被鎖定，錯誤訊息「此帳號已被鎖定，請洽系統管理員」
			if (IAtomsConstants.ACCOUNT_STATUS_LOCK.equals(accouontStatus)) {
				log.error("The account "+userAccount+" is lock");
				throw new ServiceException(IAtomsMessageCode.ACCOUNT_ID_LOCK);
			}
			boolean flag = true;
			BimCompany company = (BimCompany) this.companyDAO.findByPrimaryKey(BimCompany.class, admUserDTO.getCompanyId());
			if(IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE.equals(company.getAuthenticationType())){
				flag = false;
			}
			//判斷是否cyber用戶
			if (flag) {
				//cyber用戶
				logonUser.setIsCyberUser(true);
			} else {
				logonUser.setIsCyberUser(false);
				//非Cyber員工
				Date changePwdDate = admUserDTO.getChangePwdDate();
				//1. 判斷密碼是否已過有效期
				if (IAtomsConstants.ACCOUNT_STATUS_PWD_OVERDUE.equals(accouontStatus)) {
					log.error("The account "+userAccount+" 密碼已過有效週期(【密碼原則設定-密碼有效週期(天)】)");
					throw new ServiceException(IAtomsMessageCode.PWD_OVERDUE);
				} else {					
					if (changePwdDate != null  && admSecurityDefDTO != null) {
						//密碼已過有效週期(【密碼原則設定-密碼有效週期(天)】)，錯誤訊息「密碼過期，請洽系統管理員」并更新帳號狀態為“密碼過期”
						int dateSubtractionDay = IAtomsDateTimeUtils.dateSubtractionDay(changePwdDate, DateTimeUtils.getCurrentDate());
						if (dateSubtractionDay > admSecurityDefDTO.getPwdValidDay()) {
							admUserDTO.setStatus(IAtomsConstants.ACCOUNT_STATUS_PWD_OVERDUE);
							admUserDAO.updateUser(admUserDTO);
							log.error("The account "+userAccount+" 密碼已過有效週期(【密碼原則設定-密碼有效週期(天)】)");
							//throw new ServiceException(IAtomsMessageCode.PASSWORD_OVERDUE);
							sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, 								
									IAtomsMessageCode.PWD_OVERDUE));
							return sessionContext;
						}
					}
				}
				//驗證密碼
				String password = logonUser.getLogUserPwd();
				String currentPwd = PasswordEncoderUtilities.decodePassword(admUserDTO.getPassword());
				//password = PasswordEncoderUtilities.encodePassword(password);
				//密碼加密
				if (!password.equals(currentPwd)) {
					//密碼錯誤
					//(h)	若非Cyber員工，密碼錯誤次數已超過【密碼容許錯誤次數】，錯誤訊息「密碼錯誤已達OO次，帳號已被鎖定，請洽系統管理員重新設定後才可登入」并更新帳號狀態為“鎖定”
					if (admSecurityDefDTO != null) {
						int retry = admUserDTO.getRetry();
						retry ++;
						admUserDTO.setRetry(retry);
						if (retry >= admSecurityDefDTO.getPwdErrCnt() + 1) {
							admUserDTO.setStatus(IAtomsConstants.ACCOUNT_STATUS_LOCK);
							admUserDAO.updateUser(admUserDTO);
							log.error("The account "+userAccount+" 非Cyber員工，密碼錯誤次數已超過密碼容許錯誤次數, 帳號已被鎖定");
							sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, 								
									IAtomsMessageCode.PWD_ERROR_TIMES_TO_LOCK, new String[] {String.valueOf(retry)}));
							return sessionContext;
						} else {
							//(g)	若非Cyber員工，密碼錯誤，錯誤訊息「密碼錯誤XX次，達OO次，則帳號將被鎖定」
							admUserDAO.updateUser(admUserDTO);
							log.error("The account "+userAccount+" 非Cyber員工，密碼錯誤，錯誤訊息「密碼錯誤" + retry + "次，達" + admSecurityDefDTO.getPwdErrCnt() + "次，則帳號將被鎖定」");
							sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, 
									IAtomsMessageCode.PWD_ERROR_TIMES, new String[] {String.valueOf(retry),String.valueOf(admSecurityDefDTO.getPwdErrCnt() + 1)}));
							return sessionContext;
						}				
					} else {
						log.error("The account "+userAccount+" 非Cyber員工，密碼原則未設定,密碼錯誤，錯誤訊息「密碼錯誤，請重新輸入」");
						sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PWD_ERROR));
						return sessionContext;
					}
				} else {
					//首先保存帳號信息
					admUserDTO.setRetry(0);
					admUserDTO.setLastLoginDate(DateTimeUtils.getCurrentTimestamp());
					//非Cyber員工且為首次登入,無須進行密碼變更作業(【密碼原則設定-首次登入需修改密碼】為「否」)，則更新【帳號狀態】為“正常” 
					if(admSecurityDefDTO != null && IAtomsConstants.ACCOUNT_STATUS_NEW.equals(admUserDTO.getStatus()) 
							&& IAtomsConstants.NO.equals(admSecurityDefDTO.getPwdChgFlag()) 
							&& StringUtils.hasText(admUserDTO.getResetPwd()) && admUserDTO.getResetPwd().equals(IAtomsConstants.NO)){
						admUserDTO.setStatus(IAtomsConstants.ACCOUNT_STATUS_NORMAL);
					}
					admUserDAO.updateUser(admUserDTO);
					
					//非Cyber員工且為首次登入，則須進行密碼變更作業(【密碼原則設定-首次登入需修改密碼】為「是」)，pop密碼變更頁 ，變更成功後，進入登入後首頁
					if (admSecurityDefDTO != null && IAtomsConstants.ACCOUNT_STATUS_NEW.equals(admUserDTO.getStatus()) 
							&& IAtomsConstants.TRUE_VALUE.YES.getValue().equals(admSecurityDefDTO.getPwdChgFlag())) {
						log.error("The account "+userAccount+" 非Cyber員工，員工且為首次登入");
						logonUser.setStatus(admUserDTO.getStatus());
						sessionContext.setResponseResult(logonUser);
						sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, 
								IAtomsMessageCode.NEW_ACCOUNT_PWD_CHANGE));
						return sessionContext;
					} 
					//密碼正確,是否為管理員重置密碼
					if(StringUtils.hasText(admUserDTO.getResetPwd()) && admUserDTO.getResetPwd().equals(IAtomsConstants.YES)){
						log.error("The account "+userAccount+" 密碼已被重置，請修改密碼");
						logonUser.setStatus(admUserDTO.getStatus());
						sessionContext.setResponseResult(logonUser);
						sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, 
								IAtomsMessageCode.RESET_PWD_NEED_EDIT));
						return sessionContext;
					}
					if (changePwdDate != null && admSecurityDefDTO != null) {
						if (!IAtomsConstants.YES.equals(logonUser.getPasswordIgnoreFlag())) {
							//(b)	非Cyber員工若密碼即將過期(【密碼原則設定-密碼到期提示天數】) ，提示使用者「您的密碼將在XX天後到期，是否進行變更？(密碼過期後，須請管理員重新設定)」
							int dateSubtractionDay = IAtomsDateTimeUtils.dateSubtractionDay(changePwdDate, DateTimeUtils.getCurrentDate());
						//	int subDay = admSecurityDefDTO.getIdValidDay() - dateSubtractionDay;
							int subDay = admSecurityDefDTO.getPwdValidDay() - dateSubtractionDay;
							if (subDay < admSecurityDefDTO.getPwdAlertDay()) {
								log.error("The account "+userAccount+" 非Cyber員工若密碼即將過期");
								logonUser.setStatus(admUserDTO.getStatus());
								sessionContext.setResponseResult(logonUser);
								sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, 
										IAtomsMessageCode.PWD_DUE_WARING, new String[] {String.valueOf(subDay + 1)}));
								return sessionContext;
							}
						}
					}
					logonUser.setId(admUserDTO.getUserId());
					//記錄session log
					SessionLogging session = new SessionLogging();
					session.setId(logonUser.getId());
					session = (SessionLogging)this.iAtomsIdentityDAO.findByPrimaryKey(session);
					//使用者尚未登入
					if(session == null){
						log.debug("user session is not exist..");
						session = new SessionLogging();
						session.setId(logonUser.getId()); 
						session.setLoginTime(DateTimeUtils.getCurrentTimestamp());
						session.setUserIp(logonUser.getFromIp());
						session.setStatus(IAtomsConstants.STATUS_ACTIVE);
						session.setSessionId(logonUser.getSessionId());
						this.iAtomsIdentityDAO.getDaoSupport().save(session);
					} else {//使用者已登入
						log.debug("user session is exist..");
						if(!logonUser.getSessionId().equals(session.getSessionId())){
							log.debug("session status is INACTIVE..");
							session.setLoginTime(DateTimeUtils.getCurrentTimestamp());
							session.setLogoutTime(null);
							session.setUserIp(logonUser.getFromIp());
							session.setStatus(IAtomsConstants.STATUS_ACTIVE);
							session.setSessionId(logonUser.getSessionId());
							this.iAtomsIdentityDAO.update(session);
						} else {
								log.debug("the user session status is ACTIVE, user("+logonUser.getId()+") is different login !");
							//	throw new ServiceException(IAtomsMessageCode.LOGIN_ERROR_USER_DIFFERENT_LOGIN);
						}
					}
				}
			}
			logonUser.setId(admUserDTO.getUserId());
			logonUser.setUserCode(admUserDTO.getAccount());
			logonUser.setStatus(admUserDTO.getStatus());
			logonUser.setName(admUserDTO.getCname());
			logonUser.setAdmUserDTO(admUserDTO);
			logonUser.setCompanyId(company.getCompanyId());
			logonUser.setCompanyName(company.getShortName());
			//取得使用者角色
			List<AdmRoleDTO> roles = this.getIdentityDAO().listRoleByUserId(logonUser.getId());
			//转换成使用者角色代码
			List<String> userRoleCodes = new ArrayList<String>();
			if (!CollectionUtils.isEmpty(roles)){
				for (AdmRoleDTO role : roles) {
					userRoleCodes.add(role.getRoleCode());
				}
			}
				
			logonUser.setUserFunctions(roles);
			logonUser.setUserFunctionCodes(userRoleCodes);
			//取得使用者存取控制權限 and URL
			List<IAtomsAccessControl> accessControls = this.getIdentityDAO().getAvailableAccessControlList(logonUser);
			//當前登入者與某功能之權限
			Map<String,String> accessRights = new HashMap<String, String>();
			//當前登入者沒有的功能權限
			Map<String, String> unAccessRights = new HashMap<String, String>();
			if(!CollectionUtils.isEmpty(accessControls)){
				//當前登入者parent URL
				List<AdmFunctionTypeDTO> parentNodes = new ArrayList<AdmFunctionTypeDTO>();
				AdmFunctionTypeDTO parentNode = null;
				//當前登入者child URL
				List<AdmFunctionTypeDTO> childNodes = new ArrayList<AdmFunctionTypeDTO>();
				AdmFunctionTypeDTO childNode = null;
				for(IAtomsAccessControl ac:accessControls){
					//判斷是否當前登錄者之權限 ‘Y’
					if (IAtomsConstants.PARAM_YES.equals(ac.getFunctionRight())) {
						//組裝父節點
						parentNode = new AdmFunctionTypeDTO();
						parentNode.setFunctionName(ac.getParentName());
						parentNode.setId(ac.getParentId());
						if(!parentNodes.contains(parentNode) && StringUtils.hasText(parentNode.getId())){
							parentNodes.add(parentNode);
						}
						//組裝子節點
						childNode = new AdmFunctionTypeDTO();
						childNode.setFunctionName(ac.getName());
						childNode.setId(ac.getId());
						childNode.setFunctionCategory(ac.getResourceType());
						childNode.setFunctionUrl(ac.getResourceUrl());
						childNode.setParentFunctionId(ac.getParentId());
						if(!childNodes.contains(childNode)){
							childNodes.add(childNode);
						}
						//當前登入者與某功能之權限
						if(accessRights.containsKey(ac.getId())){
							accessRights.put(ac.getId(),accessRights.get(ac.getId())
															+ IAtomsConstants.DELIMITER_COMMA + ac.getAccessRight());
						}else{
							accessRights.put(ac.getId(),ac.getAccessRight());
						}
					} else {
						//當前登入者無功能之權限map,以ucNo為key，functionType與'.'拼接為value
						if(unAccessRights.containsKey(ac.getId())){
							unAccessRights.put(ac.getId(), unAccessRights.get(ac.getId())
															+ ac.getAccessRight() + IAtomsConstants.DELIMITER_DOT );
						}else{
							unAccessRights.put(ac.getId(), IAtomsConstants.MARK_QUOTES + ac.getAccessRight() + IAtomsConstants.DELIMITER_DOT);
						}
					}
				}
				logonUser.setParentNodes(parentNodes);
				logonUser.setChildNodes(childNodes);
				logonUser.setAccRghts(accessRights);
				Set<String> set = unAccessRights.keySet();
				for (String string : set) {
					unAccessRights.put(string, unAccessRights.get(string) + IAtomsConstants.MARK_QUOTES);
				}
				logonUser.setUnAccRghts(unAccessRights);

			}
		} catch (Throwable e) {
			ServiceException se = null;
			String msgCode = null;
			if (e instanceof ServiceException) {
				se = (ServiceException) e;
				if (se != null) {
					Message msg = se.getErrorMessage();
					msgCode = msg.getCode();
				}
			}
			String messageCode = (msgCode == null || !StringUtils.hasText(msgCode)?CoreMessageCode.LOGIN_FAILED:msgCode);
			throw new ServiceException(messageCode,e);
		}
		sessionContext.setResponseResult(logonUser);
		if (sessionContext.getReturnMessage() == null) {
			sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, CoreMessageCode.LOGIN_SUCCESS));
		}		
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.service.identity.impl.IdentityService#logoff(cafe.core.context.SessionContext)
	 */
	public SessionContext logoff(SessionContext sessionContext) throws ServiceException {
		IAtomsLogonUser logonUser = (IAtomsLogonUser)sessionContext.getRequestParameter();
		try {
			if(logonUser != null){
				AdmSystemLogging apAuditLog = new AdmSystemLogging();
				apAuditLog.setLogCategre(IAtomsConstants.ACTION_LOGOFF);
				//程式名稱/交易代號/交易名稱
				apAuditLog.setFunctionName(IAtomsConstants.UC_NO_ADM_01000);
				//登入帳號
				apAuditLog.setUserId(logonUser.getId());
				//User端IP/終端機號
				apAuditLog.setIp(logonUser.getFromIp());
				//日期時間
				apAuditLog.setOperationTime(DateTimeUtils.getCurrentTimestamp());
				//sessionId
				apAuditLog.setSessionId(logonUser.getSessionId());
				log.debug((new StringBuilder()).append("userId==>").append((String)logonUser.getId()).toString());
				log.debug((new StringBuilder()).append("sessionId==>").append(logonUser.getSessionId()).toString());
				SessionLogging session = new SessionLogging();
				session.setId(logonUser.getId());
				session = (SessionLogging)this.iAtomsIdentityDAO.findByPrimaryKey(session);
				if(session != null){
					log.debug((new StringBuilder()).append("DB sessionId==>").append(session.getSessionId()).toString());
					if(session.getSessionId().equals(logonUser.getSessionId())) {
						log.debug("session equal");
						session.setLogoutTime(DateTimeUtils.getCurrentTimestamp());
						session.setStatus(IAtomsConstants.STATUS_INACTIVE);
						this.iAtomsIdentityDAO.update(session);
						this.iAtomsIdentityDAO.getDaoSupport().flush();
					} else {
						log.error("The user "+logonUser.getId()+" logonOff error--->no data found");
						// 不拋出異常只做記錄
					//	throw new ServiceException(new Message(Message.STATUS.SUCCESS, CoreMessageCode.LOGIN_ID_NOT_FOUND));
					}
				} else {
					log.error("The user "+logonUser.getId()+" DB sessionId is null");
				}
				apAuditLog.setResult(i18NUtil.getName(IAtomsMessageCode.IATOMS_SUCCESS));
				this.iAtomsIdentityDAO.getDaoSupport().save(apAuditLog);
				this.iAtomsIdentityDAO.getDaoSupport().flush();
			}
		} catch (Throwable e) {
			log.error("The user "+logonUser.getId()+" logonOff error--->"+e,e);
			throw new ServiceException(e);
		}
		sessionContext.setResponseResult(logonUser);
		sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, CoreMessageCode.LOGOFF_SUCCESS));
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.service.identity.impl.IdentityService#getLogonUser(cafe.core.context.SessionContext)
	 */
	public SessionContext getLogonUser(SessionContext sessionContext) throws ServiceException {
		IAtomsLogonUser logonUser = (IAtomsLogonUser)sessionContext.getRequestParameter();
		try {
			if(logonUser != null){
				String userId = logonUser.getId();
				//查詢用戶信息
				AdmUser admUser = (AdmUser) this.admUserDAO.findByPrimaryKey(AdmUser.class, userId);
				
				//1. 若無此帳號，錯誤訊息「此帳號不存在，請洽系統管理員」
				if(admUser == null){
					log.error("The user "+userId+" is not exist!");
					throw new ServiceException(IAtomsMessageCode.ACCOUNT_ID_NOT_FOUND);
				}
				logonUser.setStatus(admUser.getStatus());
			}
		} catch (Throwable e) {
			log.error("The user "+logonUser.getId()+" logonOff error--->"+e,e);
			throw new ServiceException(e);
		}
		sessionContext.setResponseResult(logonUser);
		sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS));
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IIAtomsIdentityService#updateUserStatus(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext updateUserStatus(SessionContext sessionContext) throws ServiceException {
		IAtomsLogonUser logonUser = (IAtomsLogonUser)sessionContext.getRequestParameter();
		try {
			if(logonUser != null){
				String userId = logonUser.getId();
				//查詢用戶信息
				AdmUser admUser = (AdmUser) this.admUserDAO.findByPrimaryKey(AdmUser.class, userId);
				if(admUser == null){
					log.error("The user "+userId+" is not exist!");
					throw new ServiceException(IAtomsMessageCode.ACCOUNT_ID_NOT_FOUND);
				}
				if(IAtomsConstants.ACCOUNT_STATUS_NEW.equals(logonUser.getStatus())){
					// 更改用戶狀態
					admUser.setStatus(IAtomsConstants.ACCOUNT_STATUS_NORMAL);
				}
				// 最後一次登錄時間
				admUser.setLastLoginDate(DateTimeUtils.getCurrentTimestamp());
				this.admUserDAO.getDaoSupport().saveOrUpdate(admUser);
				sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS));
				//記錄session log
				SessionLogging session = new SessionLogging();
				session.setId(logonUser.getId());
				session = (SessionLogging)this.iAtomsIdentityDAO.findByPrimaryKey(session);
				//使用者尚未登入
				if(session == null){
					log.debug("user session is not exist..");
					session = new SessionLogging();
					session.setId(logonUser.getId()); 
					session.setLoginTime(DateTimeUtils.getCurrentTimestamp());
					session.setUserIp(logonUser.getFromIp());
					session.setStatus(IAtomsConstants.STATUS_ACTIVE);
					session.setSessionId(logonUser.getSessionId());
					this.iAtomsIdentityDAO.getDaoSupport().save(session);
				} else {//使用者已登入
					log.debug("user session is exist..");
					if(!logonUser.getSessionId().equals(session.getSessionId())){
						log.debug("session status is INACTIVE..");
						session.setLoginTime(DateTimeUtils.getCurrentTimestamp());
						session.setLogoutTime(null);
						session.setUserIp(logonUser.getFromIp());
						session.setStatus(IAtomsConstants.STATUS_ACTIVE);
						session.setSessionId(logonUser.getSessionId());
						this.iAtomsIdentityDAO.update(session);
					} else {
							log.debug("the user session status is ACTIVE, user("+logonUser.getId()+") is different login !");
							throw new ServiceException(IAtomsMessageCode.LOGIN_ERROR_USER_DIFFERENT_LOGIN);
					}
				}
			}
		} catch (Throwable e) {
			log.error("The user "+logonUser.getId()+" updateUserStatus error--->"+e,e);
			throw new ServiceException(e);
		}
		return sessionContext;
	}
	/**
	 * @return the iAtomsIdentityDAO
	 */
	public IIAtomsIdentityDAO getiAtomsIdentityDAO() {
		return iAtomsIdentityDAO;
	}
	/**
	 * @param iAtomsIdentityDAO the iAtomsIdentityDAO to set
	 */
	public void setiAtomsIdentityDAO(IIAtomsIdentityDAO iAtomsIdentityDAO) {
		this.iAtomsIdentityDAO = iAtomsIdentityDAO;
	}
	/**
	 * @return the admUserDAO
	 */
	public IAdmUserDAO getAdmUserDAO() {
		return admUserDAO;
	}
	/**
	 * @param admUserDAO the admUserDAO to set
	 */
	public void setAdmUserDAO(IAdmUserDAO admUserDAO) {
		this.admUserDAO = admUserDAO;
	}
	/**
	 * @return the passwordSettingDAO
	 */
	public IPasswordSettingDAO getPasswordSettingDAO() {
		return passwordSettingDAO;
	}
	/**
	 * @param passwordSettingDAO the passwordSettingDAO to set
	 */
	public void setPasswordSettingDAO(IPasswordSettingDAO passwordSettingDAO) {
		this.passwordSettingDAO = passwordSettingDAO;
	}
	/**
	 * @return the passwordSettingDTO
	 */
	public PasswordSettingDTO getAdmSecurityDefDTO() {
		return admSecurityDefDTO;
	}
	/**
	 * @param passwordSettingDTO the passwordSettingDTO to set
	 */
	public void setAdmSecurityDefDTO(PasswordSettingDTO admSecurityDefDTO) {
		this.admSecurityDefDTO = admSecurityDefDTO;
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

}
