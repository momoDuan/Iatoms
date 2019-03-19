package com.cybersoft4u.xian.iatoms.web.controllers.authenticator;

import com.cybersoft4u.xian.iatoms.common.domain.DomainUserInfoDTO;

import cafe.core.exception.CommonException;


/**
 * Purpose:域账号资料处理 interface 
 * @author candicechen
 * @since  JDK 1.6
 * @date   2015年4月22日
 * @MaintenancePersonnel candicechen
 */
public interface IAuthenticatorHelper {
	/**
	 * Purpose:
	 * @author candicechen
	 * @param userName
	 * @param password
	 * @return
	 * @throws CommonException
	 * @return DomainUserInfoDTO
	 */
	public DomainUserInfoDTO getDomainEmployee(String userName, String password) throws CommonException;
}
