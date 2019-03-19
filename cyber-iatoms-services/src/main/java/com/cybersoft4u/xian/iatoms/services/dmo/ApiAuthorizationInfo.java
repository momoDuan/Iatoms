package com.cybersoft4u.xian.iatoms.services.dmo;

import java.sql.Timestamp;

import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.ApiAuthorizationInfoDTO;

public class ApiAuthorizationInfo extends DomainModelObject<String,ApiAuthorizationInfoDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7315816206430833486L;
	
	private String clientCode;
	private String ip;
	private String status;
	private int retry;
	private Timestamp authorizationTime;
	private String token;
	private String hasToken;
	
	public ApiAuthorizationInfo() {
	}

	public ApiAuthorizationInfo(String clientCode, String ip, String status,
			int retry, Timestamp authorizationTime, String token, String hasToken) {
		super();
		this.clientCode = clientCode;
		this.ip = ip;
		this.status = status;
		this.retry = retry;
		this.authorizationTime = authorizationTime;
		this.token = token;
		this.hasToken = hasToken;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the retry
	 */
	public int getRetry() {
		return retry;
	}

	/**
	 * @param retry the retry to set
	 */
	public void setRetry(int retry) {
		this.retry = retry;
	}

	/**
	 * @return the authorizationTime
	 */
	public Timestamp getAuthorizationTime() {
		return authorizationTime;
	}

	/**
	 * @param authorizationTime the authorizationTime to set
	 */
	public void setAuthorizationTime(Timestamp authorizationTime) {
		this.authorizationTime = authorizationTime;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the clientCode
	 */
	public String getClientCode() {
		return clientCode;
	}

	/**
	 * @param clientCode the clientCode to set
	 */
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the hasToken
	 */
	public String getHasToken() {
		return hasToken;
	}

	/**
	 * @param hasToken the hasToken to set
	 */
	public void setHasToken(String hasToken) {
		this.hasToken = hasToken;
	}
	
}
