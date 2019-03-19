package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;

import cafe.core.bean.dto.DataTransferObject;

/**
 * 
 * Purpose: 系統日志記錄
 * @author amandawang
 * @since  JDK 1.7
 * @date   2016年7月5日
 * @MaintenancePersonnel amandawang
 */

public class AdmSystemLoggingDTO extends DataTransferObject<Integer> {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -2148238386567592950L;
	
	/**
	 * 
	 * Purpose: 枚舉類
	 * @author amandawang
	 * @since  JDK 1.7
	 * @date   2016年7月5日
	 * @MaintenancePersonnel amandawang
	 */
	public static enum ATTRIBUTE {
		FUNCTION_ID("functionId"),
		FUNCTION_NAME("functionName"),
		CONTENT("content"),
		USER_ID("userId"),
		USER_NAME("userName"),
		ACCOUNT("account"),
		IP("ip"),
		OPERATION_TIME("operationTime"),
		SESSION_ID("sessionId"),
		LOG_CATEGRE("logCategre"),
		LOG_CATEGRE_NAME("logCategreName"),
		LOG_ID("logId"),
		CLIENT_INFO("clientInfo"),
		OPERATING_SYSTEM("operatingSystem"),
		BROWSER("browser"),
		VERSION("version"),
		SHOW_WIDTH("showWidth"),
		SHOW_HEIGHT("showHeight"),
		RESULT("result");

		/**
		 * value值
		 */
		private String value;
		/**
		 * Constructor:構造函數
		 */
		ATTRIBUTE(String value) {
			this.value = value;
		};
		/**
		 * @return the value
		 */
		public String getValue() {
			return this.value;
		}
	};

	/**
	 * 功能ID
	 */
	private String functionId;
	/**
	 * 功能name
	 */
	private String functionName;
	/**
	 * 內容
	 */
	private String content;
	/**
	 * 執行結果
	 */
	private String result;
	/**
	 * 用戶id
	 */
	private String userId;
	/**
	 * 用戶名稱
	 */
	private String userName;
	/**
	 * 用戶帳號
	 */
	private String account;
	/**
	 * ip
	 */
	private String ip;
	/**
	 * 操作時間
	 */
	private Timestamp operationTime;
	/**
	 * sessionId
	 */
	private String sessionId;
	/**
	 * 記錄類別
	 */
	private String logCategre;
	/**
	 * 記錄類別名
	 */
	private String logCategreName;
	/**
	 * 記錄id
	 */
	private String logId;
	
	/**
	 * 客戶端信息
	 */
	private String clientInfo;
	
	/**
	 * Constructor: 構造函數
	 */
	public AdmSystemLoggingDTO() {
		super();
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
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
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
	 * @return the logCategreName
	 */
	public String getLogCategreName() {
		return logCategreName;
	}

	/**
	 * @param logCategreName the logCategreName to set
	 */
	public void setLogCategreName(String logCategreName) {
		this.logCategreName = logCategreName;
	}

	/**
	 * @return the functionId
	 */
	public String getFunctionId() {
		return functionId;
	}

	/**
	 * @param functionId the functionId to set
	 */
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
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
	 * @return the logId
	 */
	public String getLogId() {
		return logId;
	}

	/**
	 * @param logId the logId to set
	 */
	public void setLogId(String logId) {
		this.logId = logId;
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
