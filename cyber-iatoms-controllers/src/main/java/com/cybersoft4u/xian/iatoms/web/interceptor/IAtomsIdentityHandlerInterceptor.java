package com.cybersoft4u.xian.iatoms.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.utils.IAtomsWebUtils;

import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.web.controller.util.SessionHelper;
import cafe.core.web.interceptor.IdentityHandlerInterceptor;

/**
 * Purpose: 限制非法登入
 * @author: sevenfu
 * @since: JDK 1.6
 * @date: 2015/7/21
 */
public class IAtomsIdentityHandlerInterceptor extends IdentityHandlerInterceptor {
	/**
	 * log
	 */
	private static Log log = LogFactory.getLog(IAtomsIdentityHandlerInterceptor.class);

	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.interceptor.IdentityHandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		String url = request.getServletPath();
		log.debug("IAtomsIdentityHandlerInterceptor.preHandle:" + url);
		if(this.getIgnores().contains(url)){
			return true;
		}
		IAtomsLogonUser logonUser = (IAtomsLogonUser)SessionHelper.getLogonUser(request);
		if (logonUser == null || !StringUtils.hasText(logonUser.getId())) {
			ModelAndView mav;
			//判斷是否是ajax請求
			if (IAtomsWebUtils.isAjaxRequest(request)){ 
				//如果是ajax請求, 響應頭會有，x-requested-with 
	            /*Map map = new HashMap();
	            map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, Integer.valueOf(-1));
				map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, "");
	            //處理結果
	            map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
	            //錯誤代碼--timeout
	            map.put(IAtomsConstants.PARAM_ACTION_RESULT_CODE, IAtomsConstants.PARAM_ACTION_RESULT_FAIL_CODE_TIMEOUT);
	            //錯誤信息-- 網路連線逾時,請重新登入
	            map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.LOGIN_ERROR_INVALID_USER_SESSION));*/
	            response.setHeader(IAtomsConstants.PARAM_ACTION_RESULT_SESSION_STATUS, IAtomsConstants.PARAM_ACTION_RESULT_FAIL_CODE_TIMEOUT);
	            //2018/01/23
	            //response.sendError(601, i18NUtil.getName(IAtomsMessageCode.LOGIN_ERROR_INVALID_USER_SESSION));
	            mav = new ModelAndView(this.getRedirectUrl(),
						IAtomsConstants.PAGE_PARAM_RETURN_MSG_CODE,
						IAtomsMessageCode.LOGIN_ERROR_INVALID_USER_SESSION);
	        } else { 
	        	mav = new ModelAndView(this.getRedirectUrl(),
					IAtomsConstants.PAGE_PARAM_RETURN_MSG_CODE,
					IAtomsMessageCode.LOGIN_ERROR_INVALID_USER_SESSION);       	
	        }
			this.getSessionHelper().logout(request);
        	log.error("IAtomsIdentityHandlerInterceptor.preHandle:User session is NULL.");
			throw new ModelAndViewDefiningException(mav);
		} else {
			HttpSession session = request.getSession(true);
			String sessionId = session.getId();
		//	System.out.println(logonUser.getSessionId().equals(sessionId));
			return true;
		}
	}
}
