package com.cybersoft4u.xian.iatoms.web.controllers.authenticator;

import cafe.core.bean.identity.AuthenticationInquiryContext;
import cafe.core.bean.identity.LogonUser;
import cafe.core.exception.CommonException;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.identity.AbstractAuthenticator;

import com.cybersoft4u.xian.iatoms.common.domain.DomainUserInfoDTO;
import com.cybersoft4u.xian.iatoms.config.IAtomsSystemConfigManager;


/**
 * Purpose:验证cybersoft网域验证 
 * @author candicechen
 * @since  JDK 1.6
 * @date   2015年4月20日
 * @MaintenancePersonnel candicechen
 */
public class LdapAuthenticator extends AbstractAuthenticator<LogonUser> {
	private static CafeLog log = CafeLogFactory.getLog(IAtomsSystemConfigManager.LADP, LdapAuthenticator.class);
	/**
	 * 识别管理服务类别Service
	 */
	private AuthenticatorHelper authenticatorHelper;
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.identity.AbstractAuthenticator#authenticate(cafe.core.bean.identity.AuthenticationInquiryContext)
	 */
	@Override
	public LogonUser authenticate(
			AuthenticationInquiryContext<LogonUser> context)
			throws CommonException {
		log.debug("Enter "+this.getClass().getName()+".authenticate start!!");
		LogonUser logonUser = context.getLogonUser();
		String logUserPwd = (String) context.getAttribute("logPwd");
		try {
			if(logonUser == null){
				log.debug("Logon User is null");
			}
			DomainUserInfoDTO domainUserInfoDTO = this.authenticatorHelper.getDomainEmployee(logonUser.getId(), logUserPwd);
			if(domainUserInfoDTO!=null){
				logonUser.setEmail(domainUserInfoDTO.getMail());
				logonUser.setName(domainUserInfoDTO.getDisplayName());
				logonUser.setPassword(logonUser.getId());
			}
		}catch(Throwable e) {
    		log.error(this.getClass().getName()+".authenticate is error !!-->"+e.getMessage());
    		throw new CommonException(new CommonException(e).getErrorMessage());
		}
		return logonUser;
	}
	/**
	 * @return the authenticatorHelper
	 */
	public AuthenticatorHelper getAuthenticatorHelper() {
		return authenticatorHelper;
	}
	/**
	 * @param authenticatorHelper the authenticatorHelper to set
	 */
	public void setAuthenticatorHelper(AuthenticatorHelper authenticatorHelper) {
		this.authenticatorHelper = authenticatorHelper;
	}
}
