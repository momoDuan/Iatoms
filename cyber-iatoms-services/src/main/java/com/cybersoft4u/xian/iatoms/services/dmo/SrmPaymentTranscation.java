package com.cybersoft4u.xian.iatoms.services.dmo;

// Generated 2017/2/9 �W�� 10:18:50 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentTranscationDTO;

import cafe.core.dmo.DomainModelObject;

/**
 * SrmPaymentTranscation generated by hbm2java
 */
public class SrmPaymentTranscation extends DomainModelObject<String, SrmPaymentTranscationDTO>{

	private String transcationId;
	private String paymentId;
	private String action;
	private String status;
	private String paymentContent;
	private String updatedById;
	private String updatedByName;
	private Date updatedDate;

	public SrmPaymentTranscation() {
	}

	public SrmPaymentTranscation(String transcationId) {
		this.transcationId = transcationId;
	}

	public SrmPaymentTranscation(String transcationId,String paymentId,
			String action, String status,
			String paymentContent, String updatedById, String updatedByName,
			Date updatedDate) {
		this.transcationId = transcationId;
		this.paymentId = paymentId;
		this.action = action;
		this.status = status;
		this.paymentContent = paymentContent;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
	}

	public String getTranscationId() {
		return this.transcationId;
	}

	public void setTranscationId(String transcationId) {
		this.transcationId = transcationId;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPaymentContent() {
		return this.paymentContent;
	}

	public void setPaymentContent(String paymentContent) {
		this.paymentContent = paymentContent;
	}

	public String getUpdatedById() {
		return this.updatedById;
	}

	public void setUpdatedById(String updatedById) {
		this.updatedById = updatedById;
	}

	public String getUpdatedByName() {
		return this.updatedByName;
	}

	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}

	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

}
