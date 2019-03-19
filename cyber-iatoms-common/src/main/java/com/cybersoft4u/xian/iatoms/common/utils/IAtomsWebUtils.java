package com.cybersoft4u.xian.iatoms.common.utils;

import javax.servlet.http.HttpServletRequest;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;

/**
 * Purpose: IAtoms web 請求通用處理類 
 * @author evanliu
 * @since  JDK 1.6
 * @date   2016年7月26日
 * @MaintenancePersonnel evanliu
 */
public class IAtomsWebUtils {
	/**
	 * Purpose: 判斷request是否是ajax請求
	 * @author evanliu
	 * @param request:HttpServletRequest
	 * @return Boolean:true-ajax請求, false-非ajax請求
	 */
	public static Boolean isAjaxRequest(HttpServletRequest request){
		if (null == request) {
			return null;
		}
		Boolean isAjaxBoolean = Boolean.FALSE;
		if (null != request.getHeader(IAtomsConstants.REQUEST_AJAX_HEADER) && 
				request.getHeader(IAtomsConstants.REQUEST_AJAX_HEADER).equalsIgnoreCase(IAtomsConstants.REQUEST_AJAX_OBJECT_NAME)){ 
			isAjaxBoolean = Boolean.TRUE;
		}
		return isAjaxBoolean;
	}
}
