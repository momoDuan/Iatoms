package com.cybersoft4u.xian.iatoms.services.dmo;

import java.sql.Timestamp;

import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmSystemLoggingDTO;


public class AdmSystemLoggingHistory extends DomainModelObject<Integer, AdmSystemLoggingDTO> {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -2449650722784376232L;	
	private String userId;
	private Timestamp operationTime;
	private String ip;
	private String logCategre;
	private String functionName;
	private String content;
	private String result;
	private String sessionId;
	private Timestamp createdDate;
	private String clientInfo;
	/**
	 * Constructor: 無參構造子
	 */
	public AdmSystemLoggingHistory() {
		super();
	}
	/**
	 * Constructor: 構造函數
	 */
	public AdmSystemLoggingHistory(String userId, Timestamp operationTime, String ip, String logCategre,
			String functionName, String content, String result, String sessionId, Timestamp createdDate,
			String clientInfo) {
		this.userId = userId;
		this.operationTime = operationTime;
		this.ip = ip;
		this.logCategre = logCategre;
		this.functionName = functionName;
		this.content = content;
		this.result = result;
		this.sessionId = sessionId;
		this.createdDate = createdDate;
		this.clientInfo = clientInfo;
	}

	/**
	 * 
	 * Constructor: 構造函數
	 */
	public AdmSystemLoggingHistory(Integer id) {
		super(id);
		this.id = id;
	}
	
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the operationTime
	 */
	public Timestamp getOperationTime() {
		return operationTime;
	}

	/**
	 * @param operationTime the operationTime to set
	 */
	public void setOperationTime(Timestamp operationTime) {
		this.operationTime = operationTime;
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
	 * @return the logCategre
	 */
	public String getLogCategre() {
		return logCategre;
	}

	/**
	 * @param logCategre the logCategre to set
	 */
	public void setLogCategre(String logCategre) {
		this.logCategre = logCategre;
	}

	/**
	 * @return the functionName
	 */
	public String getFunctionName() {
		return functionName;
	}

	/**
	 * @param functionName the functionName to set
	 */
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the createdDate
	 */
	public Timestamp getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return the clientInfo
	 */
	public String getClientInfo() {
		return clientInfo;
	}
	/**
	 * @param clientInfo the clientInfo to set
	 */
	public void setClientInfo(String clientInfo) {
		this.clientInfo = clientInfo;
	}
	
}