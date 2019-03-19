package com.cybersoft4u.xian.iatoms.web.controllers.restapi;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cafe.core.exception.CommonException;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

/**
 * Purpose: global handle rest api exception 
 * @author evanliu
 * @since  JDK 1.6
 * @date   2018年5月8日
 * @MaintenancePersonnel evanliu
 */
@ControllerAdvice
public class GlobalExceptionHandler  {
	/**
	 * system log
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(GlobalExceptionHandler.class);
	
	/**
	 * Purpose: when controllers trigger an exception of typed commonException, this method can catch it and process.
	 * After completion, it returns client the error message.
	 * @author evanliu
	 * @param request HttpServletRequest
	 * @param e Exception
	 * @return AjaxResponse
	 */
	@ExceptionHandler(value = CommonException.class)
	@ResponseBody
    public String errorCommonExceptionHandler(HttpServletRequest request, Exception e) {
		LOGGER.error("request trigger commonException:", e);
		return "{\"result\":\"false\",\"resultDesc\":\"系統發生未知錯誤\"}";
    }
	
	/**
	 * Purpose: when controllers trigger an Exception, this method can catch it and process.
	 * After completion, it returns client the error message.
	 * @author evanliu
	 * @param request HttpServletRequest
	 * @param e Exception
	 * @return AjaxResponse
	 */
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
    public String errorExceptionHandler(HttpServletRequest request, Exception e) {
		LOGGER.error("request trigger Exception:", e);
		return "{\"result\":\"false\",\"resultDesc\":\"系統發生未知錯誤\"}";
    }
}
