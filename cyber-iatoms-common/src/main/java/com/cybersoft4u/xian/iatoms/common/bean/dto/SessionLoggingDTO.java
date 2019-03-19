package com.cybersoft4u.xian.iatoms.common.bean.dto;


import java.util.Date;

import cafe.core.bean.dto.DataTransferObject;

/**
 * 
 * Purpose: 
 * @author Allenchen
 * @since  JDK 1.7
 * @date   2016/4/13
 * @MaintenancePersonnel Allenchen
 */
public class SessionLoggingDTO extends DataTransferObject<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6573917877835078323L;
	private String account;
	private Date loginTime;
	private Date logoutTime;
	private String userIp;
	private String status;
	private String sessionId;

	public SessionLoggingDTO() {
	}

	public SessionLoggingDTO(String account, String sessionId) {
		this.account = account;
		this.sessionId = sessionId;
	}

	public SessionLoggingDTO(String account, Date loginTime, Date logoutTime,
			String userIp, String status, String sessionId) {
		this.account = account;
		this.loginTime = loginTime;
		this.logoutTime = logoutTime;
		this.userIp = userIp;
		this.status = status;
		this.sessionId = sessionId;
	}

	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Date getLoginTime() {
		return this.loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Date getLogoutTime() {
		return this.logoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}

	public String getUserIp() {
		return this.userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
