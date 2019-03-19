package com.cybersoft4u.xian.iatoms.services.dmo;

import java.sql.Timestamp;

import com.cybersoft4u.xian.iatoms.common.bean.dto.ApiLogDTO;

import cafe.core.dmo.DomainModelObject;

public class ApiLog extends DomainModelObject<String,ApiLogDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3280197425741122138L;
	
	//private String id;
	private String ip;
	private String clientCode;
	private String functionCode;
	private String msgType; 
	private String message;
	private String failReasonDesc;
	private String result;
	private String masterId; 
	private String detailId;
	private String createdById;
	private String createdByName;
	private Timestamp createdDate;
	
	public ApiLog() {
	}

	public ApiLog(String ip, String clientCode, String functionCode,
			String msgType, String message, String failReasonDesc,
			String result, String masterId, String detailId,
			String createdById, String createdByName, Timestamp createdDate) {
		super();
		this.ip = ip;
		this.clientCode = clientCode;
		this.functionCode = functionCode;
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
