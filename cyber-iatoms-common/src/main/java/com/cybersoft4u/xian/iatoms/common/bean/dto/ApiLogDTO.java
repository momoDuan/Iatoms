package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;

import cafe.core.bean.dto.DataTransferObject;

public class ApiLogDTO  extends DataTransferObject<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1003238758069495007L;
	
	public static enum ATTRIBUTE {
		ID("id"),
		IP("ip"),
		CLIENT_CODE("clientCode"),
		FUNCTION_CODE("functionCode"),
		FUNCTION_NAME("functionName"),
		MSG_TYPE("msgType"),
		MESSAGE("message"),
		FAIL_REASON_DESC("failReasonDesc"),
		RESULT("result"),
		MASTER_ID("masterId"),
		DETAIL_ID("detailId"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
		CREATED_DATE("createdDate"),
		;

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	/**
	 * 資料鍵值
	 */
	private String id;
	/**
	 * 請求ip
	 */
	private String ip;
	/**
	 * 客戶碼
	 */
	private String clientCode;
	/**
	 * 功能代碼
	 */
	private String functionCode;
	/**
	 * 功能代碼名稱
	 */
	private String functionName;
	/**
	 * 電文類型,01：上行(RQ),02：下行(RS)
	 */
	private String msgType; 
	/**
	 * 電文內容
	 */
	private String message;
	/**
	 * 交易回覆錯誤訊息
	 */
	private String failReasonDesc;
	/**
	 * 交易結果
	 */
	private String result;
	/**
	 * 交易主檔
	 */
	private String masterId; 
	/**
	 * 交易明細檔
	 */
	private String detailId;
	private String createdById;
	private String createdByName;
	private Timestamp createdDate;
	/**
	 * Constructor:無參構造
	 */
	public ApiLogDTO() {
	}
	/**
	 * Constructor:有參構造
	 */
	public ApiLogDTO(String id, String ip, String clientCode,
			String functionCode, String functionName, String msgType, String message,
			String failReasonDesc, String result, String masterId,
			String detailId, String createdById, String createdByName,
			Timestamp createdDate) {
		super();
		this.id = id;
		this.ip = ip;
		this.clientCode = clientCode;
		this.functionCode = functionCode;
		this.functionName = functionName;
		this.msgType = msgType;
		this.message = message;
		this.failReasonDesc = failReasonDesc;
		this.result = result;
		this.masterId = masterId;
		this.detailId = detailId;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return the functionCode
	 */
	public String getFunctionCode() {
		return functionCode;
	}
	/**
	 * @param functionCode the functionCode to set
	 */
	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
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
	 * @return the msgType
	 */
	public String getMsgType() {
		return msgType;
	}
	/**
	 * @param msgType the msgType to set
	 */
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the failReasonDesc
	 */
	public String getFailReasonDesc() {
		return failReasonDesc;
	}
	/**
	 * @param failReasonDesc the failReasonDesc to set
	 */
	public void setFailReasonDesc(String failReasonDesc) {
		this.failReasonDesc = failReasonDesc;
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
	 * @return the masterId
	 */
	public String getMasterId() {
		return masterId;
	}
	/**
	 * @param masterId the masterId to set
	 */
	public void setMasterId(String masterId) {
		this.masterId = masterId;
	}
	/**
	 * @return the detailId
	 */
	public String getDetailId() {
		return detailId;
	}
	/**
	 * @param detailId the detailId to set
	 */
	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}
	/**
	 * @return the createdById
	 */
	public String getCreatedById() {
		return createdById;
	}
	/**
	 * @param createdById the createdById to set
	 */
	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}
	/**
	 * @return the createdByName
	 */
	public String getCreatedByName() {
		return createdByName;
	}
	/**
	 * @param createdByName the createdByName to set
	 */
	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
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
	
	
}
