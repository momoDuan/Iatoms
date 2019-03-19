package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;
import java.util.Date;

import cafe.core.bean.dto.DataTransferObject;
/**
 * Purpose: 求償處理記錄檔DTO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/2/10
 * @MaintenancePersonnel CarrieDuan
 */
public class SrmPaymentTranscationDTO extends DataTransferObject<String> {

	public static enum ATTRIBUTE {
		TRANSCATION_ID("transcationId"),
		PAYMENT_ID("paymentId"),
		ACTION("action"),
		ACTION_NAME("actionName"),
		STATUS("status"),
		PAYMENT_CONTENT("paymentContent"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		UPDATED_DATE("updatedDate");

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	
	/**
	 * 歷程記錄編號
	 */
	private String transcationId;
	/**
	 * 求償批號
	 */
	private String paymentId;
	/**
	 * 動作
	 */
	private String action;
	/**
	 * 動作后狀態
	 */
	private String status;
	/**
	 * 求償內容
	 */
	private String paymentContent;
	/**
	 * 異動人員ID
	 */
	private String updatedById;
	/**
	 * 異動人員姓名
	 */
	private String updatedByName;
	/**
	 * 異動日期
	 */
	private Timestamp updatedDate;
	/**
	 * 
	 */
	private String actionName;
	/**
	 * @return the transcationId
	 */
	public String getTranscationId() {
		return transcationId;
	}
	
	/**
	 * Constructor: 無參構造函數
	 */
	public SrmPaymentTranscationDTO() {
		super();
	}

	/**
	 * Constructor: 有參構造函數
	 */
	public SrmPaymentTranscationDTO(String transcationId, String paymentId,
			String action, String status, String paymentContent,
			String updatedById, String updatedByName, Timestamp updatedDate) {
		super();
		this.transcationId = transcationId;
		this.paymentId = paymentId;
		this.action = action;
		this.status = status;
		this.paymentContent = paymentContent;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
	}



	/**
	 * @param transcationId the transcationId to set
	 */
	public void setTranscationId(String transcationId) {
		this.transcationId = transcationId;
	}
	/**
	 * @return the paymentId
	 */
	public String getPaymentId() {
		return paymentId;
	}
	/**
	 * @param paymentId the paymentId to set
	 */
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
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
	 * @return the paymentContent
	 */
	public String getPaymentContent() {
		return paymentContent;
	}
	/**
	 * @param paymentContent the paymentContent to set
	 */
	public void setPaymentContent(String paymentContent) {
		this.paymentContent = paymentContent;
	}
	/**
	 * @return the updatedById
	 */
	public String getUpdatedById() {
		return updatedById;
	}
	/**
	 * @param updatedById the updatedById to set
	 */
	public void setUpdatedById(String updatedById) {
		this.updatedById = updatedById;
	}
	/**
	 * @return the updatedByName
	 */
	public String getUpdatedByName() {
		return updatedByName;
	}
	/**
	 * @param updatedByName the updatedByName to set
	 */
	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}
	/**
	 * @return the updatedDate
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}
	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	/**
	 * @return the actionName
	 */
	public String getActionName() {
		return actionName;
	}

	/**
	 * @param actionName the actionName to set
	 */
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	
	
}
