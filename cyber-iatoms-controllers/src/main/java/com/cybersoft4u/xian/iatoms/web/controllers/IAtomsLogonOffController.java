package com.cybersoft4u.xian.iatoms.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import cafe.core.bean.Message;
import cafe.core.config.GenericConfigManager;
import cafe.core.exception.CommonException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.util.HttpRequestHelper;
import cafe.core.web.controller.util.SessionHelper;
import cafe.workflow.web.controller.identity.WfLogoffController;

import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;

/**
 * 
 * Purpose:  用户登出Controller
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016/4/30
 * @MaintenancePersonnel CrissZhang
 */
public class IAtomsLogonOffController extends WfLogoffController{

	/**
	 * log
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.CONTROLLER, IAtomsLogonOffController.class);
	
	/**
	 * Constructor--.
	 */
	public IAtomsLogonOffController() {
		
	}


	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.identity.LogoffController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequestInternal(HttpServletRequest request,HttpServletResponse response) throws Exception {
		IAtomsLogonUser logonUser = null;
		ModelAndView mav = null;
		try {
			logonUser = (IAtomsLogonUser) SessionHelper.getLogonUser(request);
    		this.doService(this.getServiceId(), this.getActionId(), logonUser);
    		this.getSessionHelper().logout(request);
    		String currentDate = DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH);
    		LOGGER.info("人員"+((logonUser != null) ? logonUser.getId() : "")+"於"+currentDate+"自"+request.getRemoteAddr()+"登出!!");
		} catch (Throwable e) {			
			LOGGER.error(this.getClass().getSimpleName()+".logout is failed:"+e, e);
			e.printStackTrace();
			String messageCode = IAtomsMessageCode.GENERAL_EXCEPTION;
			if(e instanceof CommonException) {
				Message msg = (((CommonException)e).getErrorMessage());
				if (msg != null && StringUtils.hasText(msg.getCode())) messageCode = msg.getCode();
			}
			HttpRequestHelper.toOutputStream(response, messageCode.getBytes());
			return null;
		}
		if (mav == null) mav = new ModelAndView(this.getFormView());
		return mav;
	}
}
