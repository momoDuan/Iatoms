package com.cybersoft4u.xian.iatoms.services.dmo;


import java.math.BigDecimal;
import java.util.Date;

import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmSuppliesDTO;

/**
 * Purpose: 耗材品项维护dmo
 * @author starwang
 * @since  JDK 1.6
 * @date   2016/7/04
 * @MaintenancePersonnel starwang
 */
public class DmmSupplies extends DomainModelObject<String, DmmSuppliesDTO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4457304606071630816L;
	
	private String suppliesId;
	private String companyId;
	private String suppliesType;
	private String suppliesName;
	private BigDecimal price;
	private String createdById;
	private String createdByName;
	private Date createdDate;
	private String updatedById;
	private String updatedByName;
	private Date updatedDate;

	public DmmSupplies() {
	}

	public DmmSupplies(String suppliesId) {
		this.suppliesId = suppliesId;
	}

	public DmmSupplies(String suppliesId, String companyId,
			String suppliesType,
			String suppliesName, BigDecimal price, String createdById,
			String createdByName, Date createdDate, String updatedById,
			String updatedByName, Date updatedDate) {
		this.suppliesId = suppliesId;
		this.companyId = companyId;
		this.suppliesType = suppliesType;
		this.suppliesName = suppliesName;
		this.price = price;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
	}

	/**
	 * @return the suppliesId
	 */
	public String getSuppliesId() {
		return suppliesId;
	}

	/**
	 * @param suppliesId the suppliesId to set
	 */
	public void setSuppliesId(String suppliesId) {
		this.suppliesId = suppliesId;
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
	 * @return the suppliesName
	 */
	public String getSuppliesName() {
		return suppliesName;
	}

	/**
	 * @param suppliesName the suppliesName to set
	 */
	public void setSuppliesName(String suppliesName) {
		this.suppliesName = suppliesName;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
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
	 * @return the suppliesType
	 */
	public String getSuppliesType() {
		return suppliesType;
	}

	/**
	 * @param suppliesType the suppliesType to set
	 */
	public void setSuppliesType(String suppliesType) {
		this.suppliesType = suppliesType;
	}

	
}