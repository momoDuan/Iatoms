package com.cybersoft4u.xian.iatoms.services.dmo;

// Generated 2016/6/2 �U�� 05:11:32 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractDTO;

/**
 * Contract generated by hbm2java
 */
public class BimContract  extends  DomainModelObject<String,BimContractDTO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5100178894974035611L;
	private String contractId;
	private String companyId;
	private String contractCode;
	private Date startDate;
	private Date endDate;
	private Date cancelDate;
	private String contractStatus;
	private Long contractPrice;
	private String payMode;
	private String payRequire;
	private Integer factoryWarranty;
	private Integer customerWarranty;
	private String workHourStart1;
	private String workHourEnd1;
	private String workHourStart2;
	private String workHourEnd2;
	private String window1;
	private String window1Connection;
	private String window2;
	private String window2Connection;
	private String comment;
	private String createdById;
	private String createdByName;
	private Date createdDate;
	private String updatedById;
	private String updatedByName;
	private Date updatedDate;
	private String deleted = "N";

	public BimContract() {
	}

	public BimContract(String contractId) {
		this.contractId = contractId;
	}

	public BimContract(String contractId, String companyId,
			String contractCode, Date startDate,
			Date endDate, Date cancelDate, String contractStatus,
			Long contractPrice, String payMode, String payRequire,
			Integer factoryWarranty, Integer customerWarranty,
			String workHourStart1, String workHourEnd1, String workHourStart2,
			String workHourEnd2, String window1, String window1Connection,
			String window2, String window2Connection, String comment,
			String createdById, String createdByName,
			Date createdDate, String updatedById, String updatedByName,
			Date updatedDate, String deleted) {
		this.contractId = contractId;
		this.companyId = companyId;
		this.contractCode = contractCode;
		this.startDate = startDate;
		this.endDate = endDate;
		this.cancelDate = cancelDate;
		this.contractStatus = contractStatus;
		this.contractPrice = contractPrice;
		this.payMode = payMode;
		this.payRequire = payRequire;
		this.factoryWarranty = factoryWarranty;
		this.customerWarranty = customerWarranty;
		this.workHourStart1 = workHourStart1;
		this.workHourEnd1 = workHourEnd1;
		this.workHourStart2 = workHourStart2;
		this.workHourEnd2 = workHourEnd2;
		this.window1 = window1;
		this.window1Connection = window1Connection;
		this.window2 = window2;
		this.window2Connection = window2Connection;
		this.comment = comment;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
		this.deleted = deleted;
	}

	/**
	 * @return the contractId
	 */
	public String getContractId() {
		return contractId;
	}

	/**
	 * @param contractId the contractId to set
	 */
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	/**
	 * @return the companyId
	 */
	public String getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	/**
	 * @return the contractCode
	 */
	public String getContractCode() {
		return contractCode;
	}

	/**
	 * @param contractCode the contractCode to set
	 */
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}	

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the cancelDate
	 */
	public Date getCancelDate() {
		return cancelDate;
	}

	/**
	 * @param cancelDate the cancelDate to set
	 */
	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}

	/**
	 * @return the contractStatus
	 */
	public String getContractStatus() {
		return contractStatus;
	}

	/**
	 * @param contractStatus the contractStatus to set
	 */
	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}

	/**
	 * @return the contractPrice
	 */
	public Long getContractPrice() {
		return contractPrice;
	}

	/**
	 * @param contractPrice the contractPrice to set
	 */
	public void setContractPrice(Long contractPrice) {
		this.contractPrice = contractPrice;
	}

	/**
	 * @return the payMode
	 */
	public String getPayMode() {
		return payMode;
	}

	/**
	 * @param payMode the payMode to set
	 */
	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	/**
	 * @return the payRequire
	 */
	public String getPayRequire() {
		return payRequire;
	}

	/**
	 * @param payRequire the payRequire to set
	 */
	public void setPayRequire(String payRequire) {
		this.payRequire = payRequire;
	}

	/**
	 * @return the factoryWarranty
	 */
	public Integer getFactoryWarranty() {
		return factoryWarranty;
	}

	/**
	 * @param factoryWarranty the factoryWarranty to set
	 */
	public void setFactoryWarranty(Integer factoryWarranty) {
		this.factoryWarranty = factoryWarranty;
	}

	/**
	 * @return the customerWarranty
	 */
	public Integer getCustomerWarranty() {
		return customerWarranty;
	}

	/**
	 * @param customerWarranty the customerWarranty to set
	 */
	public void setCustomerWarranty(Integer customerWarranty) {
		this.customerWarranty = customerWarranty;
	}

	/**
	 * @return the workHourStart1
	 */
	public String getWorkHourStart1() {
		return workHourStart1;
	}

	/**
	 * @param workHourStart1 the workHourStart1 to set
	 */
	public void setWorkHourStart1(String workHourStart1) {
		this.workHourStart1 = workHourStart1;
	}

	/**
	 * @return the workHourEnd1
	 */
	public String getWorkHourEnd1() {
		return workHourEnd1;
	}

	/**
	 * @param workHourEnd1 the workHourEnd1 to set
	 */
	public void setWorkHourEnd1(String workHourEnd1) {
		this.workHourEnd1 = workHourEnd1;
	}

	/**
	 * @return the workHourStart2
	 */
	public String getWorkHourStart2() {
		return workHourStart2;
	}

	/**
	 * @param workHourStart2 the workHourStart2 to set
	 */
	public void setWorkHourStart2(String workHourStart2) {
		this.workHourStart2 = workHourStart2;
	}

	/**
	 * @return the workHourEnd2
	 */
	public String getWorkHourEnd2() {
		return workHourEnd2;
	}

	/**
	 * @param workHourEnd2 the workHourEnd2 to set
	 */
	public void setWorkHourEnd2(String workHourEnd2) {
		this.workHourEnd2 = workHourEnd2;
	}

	/**
	 * @return the window1
	 */
	public String getWindow1() {
		return window1;
	}

	/**
	 * @param window1 the window1 to set
	 */
	public void setWindow1(String window1) {
		this.window1 = window1;
	}

	/**
	 * @return the window1Connection
	 */
	public String getWindow1Connection() {
		return window1Connection;
	}

	/**
	 * @param window1Connection the window1Connection to set
	 */
	public void setWindow1Connection(String window1Connection) {
		this.window1Connection = window1Connection;
	}

	/**
	 * @return the window2
	 */
	public String getWindow2() {
		return window2;
	}

	/**
	 * @param window2 the window2 to set
	 */
	public void setWindow2(String window2) {
		this.window2 = window2;
	}

	/**
	 * @return the window2Connection
	 */
	public String getWindow2Connection() {
		return window2Connection;
	}

	/**
	 * @param window2Connection the window2Connection to set
	 */
	public void setWindow2Connection(String window2Connection) {
		this.window2Connection = window2Connection;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
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
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	/**
	 * @return the deleted
	 */
	public String getDeleted() {
		return deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
}