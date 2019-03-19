package com.cybersoft4u.xian.iatoms.web.controllers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Message;
import cafe.core.bean.identity.AuthenticationHelperAttribute;
import cafe.core.bean.identity.AuthenticationInquiryContext;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.SessionContext;
import cafe.core.exception.CommonException;
import cafe.core.util.CoreConstants;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.identity.Authenticator;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractBaseFormController;
import cafe.core.web.controller.util.SessionHelper;
import cafe.workflow.bean.WfMessageCode;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;

/**
 * 
 * Purpose:  帐号登录Controller
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016/3/30
 * @MaintenancePersonnel CrissZhang
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused", "deprecation" })
public class IAtomsLogonController<T extends IAtomsLogonUser> extends AbstractBaseFormController<T> {
	
	/**
	 * 系统日志记录工具
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.CONTROLLER, IAtomsLogonController.class);
	
	/**
	 * 常量设定
	 */
	private static final String ATTR_MODEL_RESULT						= "result";
	
	/**
	 * Authenticator 域验证器注入
	 */
	private List<Authenticator> authenticators;
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.identity.LogonController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
	    	LOGGER.debug("Start to enter "+this.getClass().getSimpleName()+" onSubmit..");
	    	ModelAndView mav = null;
	    	HttpSession session = null;
			SessionContext sessionContext = null;
			IAtomsLogonUser logonUser = null;
			String password = null;
			try {
				//获取当前登陆者Object
				logonUser = (IAtomsLogonUser) command;
				/*
				 * 先從session取得logonuUser信息,若存在,則代表曾經已登入,後面所有帳號皆不可登入.
				 * 防止多開IE造成案件措置問題
				 * */
				IAtomsLogonUser sessionLogonUser = (IAtomsLogonUser) SessionHelper.getLogonUser(request);
				if(sessionLogonUser != null) {
					String status = sessionLogonUser.getStatus();
					if (StringUtils.hasText(status) 
							&& (StringUtils.hasText(sessionLogonUser.getUserCode()) 
								&& sessionLogonUser.getUserCode().equals(logonUser.getUserCode()))) {
						sessionLogonUser = null;
						/*if (!IAtomsConstants.ACCOUNT_STATUS_NORMAL.equals(status)) {
							//查找资料库， 判断用户状态是否正常
							sessionContext = doService(this.getServiceId(), "getLogonUser", logonUser);
							if (sessionContext != null && sessionContext.getReturnMessage() != null && sessionContext.getReturnMessage().isSuccess()) {
								logonUser = (IAtomsLogonUser)sessionContext.getResponseResult();
								if (IAtomsConstants.ACCOUNT_STATUS_NORMAL.equals(logonUser.getStatus())) {
									sessionLogonUser.setStatus(IAtomsConstants.ACCOUNT_STATUS_NORMAL);
									SessionHelper.setLogonUser(request, logonUser);
									sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, CoreMessageCode.LOGIN_SUCCESS));
									this.postSubmited(request, response, sessionContext);
									mav = new ModelAndView(this.getSuccessView(),CoreConstants.PARAM_SESSION_CONTEXT, sessionContext);
									return mav;
								}
							}
							sessionLogonUser = null;
						} else {
							mav = new ModelAndView(this.getSuccessView(),CoreConstants.PARAM_SESSION_CONTEXT, sessionContext);
							return mav;
						}*/
					} else {
						throw new CommonException(IAtomsMessageCode.LIMITED_LOGON_ID);
					}
					//throw new CommonException(IAtomsMessageCode.LIMITED_LOGON_ID);
					//sessionLogonUser = null;
				}			
		    	
				password = logonUser.getLogUserPwd();
				if (!StringUtils.hasText(logonUser.getRelogonFlag())){
					logonUser.setRelogonFlag(CoreConstants.NO);
				}
				
				//取得ServletContext
				ServletContext sc = this.getServletContext();
				StringBuilder builder = new StringBuilder();
				LOGGER.info("[Server Name:" + request.getServerName() + ",Port:" + request.getServerPort() + ",Server Info:" + ((sc != null)?sc.getServerInfo():"") + "]");
				//取得当前系统日期
				String currentDate = DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), CoreConstants.DT_FMT_YYYYMMDDHHMMSS_DASH);
				//获取当前Session
				session = request.getSession(true);
				String sessionId = session.getId();
				logonUser.setFromIp(getClientIP(request));
				LOGGER.info("人員"+logonUser.getUserCode()+"於"+currentDate+"自"+request.getRemoteAddr()+"登入!!");	
				LOGGER.debug("session id==>"+sessionId);
				LOGGER.debug("user relogin==>"+logonUser.getRelogonFlag());
	    	    logonUser.setSessionId(sessionId);
	    	    //调用处理逻辑Service
			    sessionContext = doService(this.getServiceId(), CoreConstants.ACTION_LOGON, logonUser);
			    if (sessionContext != null && sessionContext.getReturnMessage() != null && sessionContext.getReturnMessage().isSuccess()) {
			    	if (!IAtomsMessageCode.NEW_ACCOUNT_PWD_CHANGE.equals(sessionContext.getReturnMessage().getCode())
			    			&& !IAtomsMessageCode.PWD_DUE_WARING.equals(sessionContext.getReturnMessage().getCode())
			    			&& !IAtomsMessageCode.RESET_PWD_NEED_EDIT.equals(sessionContext.getReturnMessage().getCode())) {
				    	logonUser = (IAtomsLogonUser) sessionContext.getResponseResult();
				    	if (!CollectionUtils.isEmpty(this.authenticators)) {
				    		String id = logonUser.getId();
				    		logonUser.setId(logonUser.getUserCode());
					    	AuthenticationInquiryContext authCtx = new AuthenticationInquiryContext(logonUser);					    	
					    	Iterator<Authenticator> auths = this.authenticators.iterator();
					    	Authenticator authenticator = null;
					    	AuthenticationHelperAttribute authAttr = null;
					    	CommonException ce = null;
					    	boolean isSessionable = false;
					    	String sessionAttributeName = IAtomsConstants.MARK_EMPTY_STRING;
					    	while (auths.hasNext()) {
					    		authenticator = auths.next();
					    		if (IAtomsConstants.AUTHENTICATOR_LDAP.equals(authenticator.getId())) {
					    			if (!logonUser.getIsCyberUser().booleanValue()) {
					    				//非cyber用戶不驗證
					    				continue;
					    			}
								} else {
									logonUser.setId(logonUser.getUserCode());
							        logonUser.setPassword(logonUser.getUserCode());
								}
					    		try{
//					    			boolean isAuthenticated = authenticator.execute(authCtx);
					    			authCtx.setAttribute("logPwd", password);
					    			if(!authenticator.execute(authCtx)){
					    				throw new CommonException(IAtomsMessageCode.PWD_ERROR);
					    			}
					    		} catch(Exception e){
					    			LOGGER.error(".execute()", e.getMessage(), e);
//					    			CommonException ce = new CommonException(IAtomsMessageCode.PASSWORD_ERROR);
					    			if(StringUtils.hasText(new CommonException(e).getErrorMessage().getCode())){
					    				ce = new CommonException(IAtomsMessageCode.PWD_ERROR);
					    			} else {
					    				ce = new CommonException(IAtomsMessageCode.LDAP_IS_ERROR);
					    			}
					    		//	ce = new CommonException(IAtomsMessageCode.PWD_ERROR);
					    			throw ce;
					    		}
					    		
					    		//是否要將結果cache到session中
								if (authenticator.getAuthenticationHelperAttribute() != null) {
//									boolean isSessionable = authenticator.getAuthenticationHelperAttribute().isSessionable();
//									String sessionAttributeName = authenticator.getAuthenticationHelperAttribute().getSessionAttributeName();
									isSessionable = authenticator.getAuthenticationHelperAttribute().isSessionable();
									sessionAttributeName = authenticator.getAuthenticationHelperAttribute().getSessionAttributeName();
									if (isSessionable && StringUtils.hasText(sessionAttributeName)) {
										LOGGER.debug("Caching "+sessionAttributeName+" for "+logonUser.getId());
										Object result = authCtx.getResult(authenticator.getId());
										SessionHelper.setAttribute(request, sessionAttributeName, result);
									}
								}
					    	}
					    	logonUser.setId(id);
					    	// 调service方法
					    	if (logonUser.getIsCyberUser()) {
					    		logonUser.setName(logonUser.getAdmUserDTO().getCname());
					    		SessionContext ctx = this.getServiceLocator().doService(logonUser, this.getServiceId(), IAtomsConstants.ACTION_UPDATE_USER_STATUS, command);
			    			}
				    	}
			    	}
			    	if((logonUser.getAdmUserDTO() != null) && (StringUtils.hasText(logonUser.getAdmUserDTO().getEmail()))){
			    		logonUser.setEmail(logonUser.getAdmUserDTO().getEmail());
			    	}
			    	logonUser.setLastLoginTime(DateTimeUtils.getCurrentTimestamp());
			    	//logonUser.setUserPassword(password);
			    	SessionHelper.setLogonUser(request, logonUser);
					this.postSubmited(request, response, sessionContext);
			    }		    
			} catch (Throwable e) {
			    LOGGER.error("user login error:"+e, e);
			    e.printStackTrace();
			    if (sessionContext == null) {
			    	sessionContext = new SessionContext();
			    }
				if(e instanceof CommonException) {
					if(((CommonException) e).getErrorCode().equals(WfMessageCode.BPM_AUTHENTICATION_FAILED)){
						sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE,IAtomsMessageCode.BPM_AUTHENTICATION_FAILED, new String[]{((CommonException) e).getArguments()[0].toString()}));
					}else{
						sessionContext.setReturnMessage(((CommonException)e).getErrorMessage());
					}
				} else {
					sessionContext.setReturnMessage(new CommonException(e).getErrorMessage());
				}
			}
			if (sessionContext == null) {
				sessionContext = new SessionContext();
			}
			Message msg = sessionContext.getReturnMessage();
			if(msg == null) {
				sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, CoreMessageCode.LOGIN_ERROR_USER_LOGIN_ERROR));
			}
			sessionContext.setLogonUser(logonUser);
			if (sessionContext != null && msg.isSuccess() //){
					&& !IAtomsMessageCode.NEW_ACCOUNT_PWD_CHANGE.equals(msg.getCode())
					&& !IAtomsMessageCode.PWD_DUE_WARING.equals(msg.getCode())
					&& !IAtomsMessageCode.RESET_PWD_NEED_EDIT.equals(msg.getCode())) {
				mav = new ModelAndView(this.getSuccessView(), CoreConstants.PARAM_SESSION_CONTEXT, sessionContext);
			} else {
				mav = new ModelAndView(this.getFailureView(),CoreConstants.PARAM_SESSION_CONTEXT, sessionContext);
				try {
					if (session != null 
							&& !IAtomsMessageCode.NEW_ACCOUNT_PWD_CHANGE.equals(msg.getCode())
							&& !IAtomsMessageCode.PWD_DUE_WARING.equals(msg.getCode())
							&& !IAtomsMessageCode.RESET_PWD_NEED_EDIT.equals(msg.getCode())) {
						session.invalidate();
					}
				}catch(Throwable e) {}
			}
			return mav;
    }
	
	/**
	 * Purpose: 獲取client端IP
	 * @param request
	 * @return: String
	 */
	public String getClientIP(ServletRequest request) {
    	//通過代理軟件無法獲取真實IP,所以先取得Header，判斷是否有用代理
    	String[] httpHeaders = new String[]{"x-forwarded-for", "Proxy-Client-clientIP", "WL-Proxy-Client-clientIP"};
    	String clientIP = null;
	    try {
	    	for (int i = 0; i < httpHeaders.length; i++) {
	    		clientIP = ((HttpServletRequest) request).getHeader(httpHeaders[i]);  
			    if(StringUtils.hasText(clientIP) && !clientIP.equalsIgnoreCase("unknown")) break;
	    	}
		    if(!StringUtils.hasText(clientIP) || clientIP.equalsIgnoreCase("unknown"))  clientIP = request.getRemoteAddr();  

		}catch (Exception e) {
			LOGGER.error("getClientIP is failed:"+e, e);
		}
	    return clientIP;
	}
	
	/**
	 * @return the authenticators
	 */
	public List<Authenticator> getAuthenticators() {
		return authenticators;
	}
	/**
	 * @param authenticators the authenticators to set
	 */
	public void setAuthenticators(List<Authenticator> authenticators) {
		this.authenticators = authenticators;
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(T parmemters) throws CommonException {
		// TODO Auto-generated method stub
		return false;
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractBaseFormController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public T parse(HttpServletRequest request, T command)
			throws CommonException {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractBaseFormController#postSubmited(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, cafe.core.context.SessionContext)
	 */
	@Override
	public ModelAndView postSubmited(HttpServletRequest request,
			HttpServletResponse response, SessionContext ctx)
			throws CommonException {
		// TODO Auto-generated method stub
		return null;
	}
}