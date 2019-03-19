package com.cybersoft4u.xian.iatoms.web.interceptor;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cafe.core.bean.Message;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.SessionContext;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.WebApplicationBeanManager;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.IAtomicController;
import cafe.core.web.controller.util.SessionHelper;
import cafe.core.web.locator.IServiceLocatorProxy;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmSystemLoggingDTO;


/**
 * 
 * Purpose: 系統日志記錄攔截器
 * @author ericdu
 * @since  JDK 1.7
 * @date   2016年7月5日
 * @MaintenancePersonnel ericdu
 */
@SuppressWarnings("rawtypes")
public class ApLogInterceptor extends HandlerInterceptorAdapter {
	
	/**
	 * 系統日志記錄
	 */
	private static CafeLog log = CafeLogFactory.getLog(GenericConfigManager.CONTROLLER, ApLogInterceptor.class);
	
	/**
	 * 服務代理
	 */
	private IServiceLocatorProxy serviceLocator;
	
	/**
	 * 不需要記錄log的action
	 */
	private List<String> ignoresItems;
	
	/**
	 * (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {		
		IAtomsLogonUser logonUser = (IAtomsLogonUser)SessionHelper.getLogonUser(request);
		if (logonUser != null && handler != null && handler instanceof IAtomicController) {
			IAtomicController controller = (IAtomicController)handler;
			String actionId = controller.getActionId(request);
			String functionId = controller.getUseCaseNo();
			String functionType = controller.getFunctionType(actionId);
			//過濾不需存儲log的功能
			if ((!CollectionUtils.isEmpty(this.ignoresItems) && this.ignoresItems.contains(actionId))) return;
			//無functionType則不存儲log
			if (!StringUtils.hasText(functionType)) return;
			//封裝存儲信息
			AdmSystemLoggingDTO apAuditLog = new AdmSystemLoggingDTO();
			//程式名稱/交易代號/交易名稱
			apAuditLog.setFunctionName(functionId);
			//登入帳號
			apAuditLog.setUserId(logonUser.getId());
			//User端IP/終端機號
			apAuditLog.setIp(this.getClientIP(request));
			//日期時間
			apAuditLog.setOperationTime(DateTimeUtils.getCurrentTimestamp());
			//sessionId
			apAuditLog.setSessionId(logonUser.getSessionId());
			//紀錄類別
			apAuditLog.setLogCategre(functionType);
			// 登入操作記錄客戶端信息log
			if(IAtomsConstants.ACTION_LOGIN.equals(functionType)){
				StringBuilder clientInfo = new StringBuilder();
				if(StringUtils.hasText(request.getParameter(AdmSystemLoggingDTO.ATTRIBUTE.OPERATING_SYSTEM.getValue()))){
					clientInfo.append(request.getParameter(AdmSystemLoggingDTO.ATTRIBUTE.OPERATING_SYSTEM.getValue())).append(IAtomsConstants.MARK_SEMICOLON);
					clientInfo.append(request.getParameter(AdmSystemLoggingDTO.ATTRIBUTE.BROWSER.getValue())).append(IAtomsConstants.MARK_SEMICOLON);
					clientInfo.append(request.getParameter(AdmSystemLoggingDTO.ATTRIBUTE.VERSION.getValue())).append(IAtomsConstants.MARK_SEMICOLON);
					clientInfo.append(request.getParameter(AdmSystemLoggingDTO.ATTRIBUTE.SHOW_WIDTH.getValue())).append(IAtomsConstants.MARK_SEMICOLON);
					clientInfo.append(request.getParameter(AdmSystemLoggingDTO.ATTRIBUTE.SHOW_HEIGHT.getValue()));
					// 記錄client
					apAuditLog.setClientInfo(clientInfo.toString());
				}
			}
			
			//交易執行結果
			String txResult = null;
			try {
				if(functionType.equals(IAtomsConstants.FUNCTION_EXPORT)){
					txResult = IAtomsConstants.EXPORT_SUCCESS;
				}else if(functionType.equals(IAtomsConstants.FUNCTION_PRINT)){
					txResult = IAtomsConstants.PRINT;
				} else {
					if (modelAndView != null && modelAndView.getModel() != null) {
						Map map = modelAndView.getModel();
						SessionContext sessionContext = (SessionContext) map.get(IAtomsConstants.PARAM_SESSION_CONTEXT);
						if(sessionContext != null){
							Message message = sessionContext.getReturnMessage();
							if(message != null){
								if(message.isSuccess()) {
									/*if(IAtomsConstants.UC_NO_ADM_01010.equals(functionId)){
										return;
									}
									if(IAtomsConstants.UC_NO_DMM_03060.equals(functionId)){
										return;
									}*/
									txResult = i18NUtil.getName(IAtomsMessageCode.IATOMS_SUCCESS);//WebApplicationBeanManager.getInstance(request).getMessage(message.getCode(), message.getArguments());
								}else {
									try {
										txResult = WebApplicationBeanManager.getInstance(request).getMessage(message.getCode(), message.getArguments());
									}catch(Exception ee){
										txResult = message.getCode();
									}	
								}
							}
						}else{
							if ((Boolean) map.get(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS)) {
								txResult = i18NUtil.getName(IAtomsMessageCode.IATOMS_SUCCESS);
							}
							else {
								txResult = 	(String) map.get(IAtomsConstants.PARAM_ACTION_RESULT_MSG);
							}
							
						}
					}
				}
				apAuditLog.setResult(txResult);
				log.debug("postHandle", apAuditLog.getUserId() + " perform " + request.getServletPath() + ", function name ="+apAuditLog.getFunctionName() + "()...");
				this.serviceLocator.doService(logonUser,IAtomsConstants.SERVICE_APLOG_SERVICE,IAtomsConstants.ACTION_LOG,apAuditLog);
			} catch (Throwable e) {
				log.debug("postHandle", "ap log is failed:" + e , e);
			}	
		}
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
			log.error("getClientIP is failed:"+e, e);
		}
	    return clientIP;
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

	/**
	 * @return the ignoresItems
	 */
	public List<String> getIgnoresItems() {
		return ignoresItems;
	}

	/**
	 * @param ignoresItems the ignoresItems to set
	 */
	public void setIgnoresItems(List<String> ignoresItems) {
		this.ignoresItems = ignoresItems;
	}
}
