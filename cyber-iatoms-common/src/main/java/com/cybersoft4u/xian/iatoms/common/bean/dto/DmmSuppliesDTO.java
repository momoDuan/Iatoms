package com.cybersoft4u.xian.iatoms.common.bean.dto;


import java.math.BigDecimal;
import java.sql.Timestamp;

import cafe.core.bean.dto.DataTransferObject;

/**
 * 
 * Purpose: 耗材品項維護DTO
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2016/7/04
 * @MaintenancePersonnel HermanWang
 */
public class DmmSuppliesDTO extends DataTransferObject<String> {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -6573917877835785323L;
	/**
	 * 空構造
	 */
	public DmmSuppliesDTO() {
	}
	
	public static enum ATTRIBUTE {
		SUPPLIES_ID("suppliesId"),
		COMPANY_ID("companyId"),
		CUSTOMER_NAME("customerName"),
		SUPPLIES_CODE("suppliesCode"),
		SUPPLIES_NAME("suppliesName"),
		SUPPLIES_TYPE("suppliesType"),
		PRICE("price"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
		CREATED_DATE("createdDate"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		UPDATED_DATE("updatedDate");
		/**
		 * value
		 */
		private String value;
		/**
		 * @param value
		 */
		ATTRIBUTE(String value) {
			this.value = value;
		};
		/**
		 * @return this.value
		 */
		public String getValue() {
			return this.value;
		}
	};

	/**
	 * identifier
	 */
	private String suppliesId;
	/**
	 * 客户id
	 */
	private String companyId;
	/**
	 * 客户名称
	 */
	private String customerName;
	/**
	 * 耗材代号
	 */
	private String suppliesCode;
	/**
	 * 耗材名称
	 */
	private String suppliesName;
	/**
	 * 耗材分類
	 */
	private String suppliesType;
	/**
	 * 单价
	 */
	private BigDecimal price;
	/**
	 * 创建人
	 */
	private String createdById;
	/**
	 * 创建人姓名
	 */
	private String createdByName;
	/***
	 * 创建日期
	 */
	private Timestamp createdDate;
	/**
	 * 修改人
	 */
	private String updatedById;
	/**
	 * 修改人姓名
	 */
	private String updatedByName;
	/**
	 * 修改日期
	 */
	private Timestamp updatedDate;
	
	/**
	 * @param companyId
	 * @param suppliesCode
	 */
	public DmmSuppliesDTO(String companyId, String suppliesCode) {
		this.companyId = companyId;
		this.suppliesCode = suppliesCode;
	}
	/**
	 * 
	 * Constructor:建構子
	 */
	public DmmSuppliesDTO(String suppliesId, String companyId, String suppliesCode, 
			BigDecimal price, String createdById, String createdByName, Timestamp createdDate, 
			String updatedById, String updatedByName, Timestamp updatedDate) {
		this.suppliesId = suppliesId;
		this.companyId = companyId;
		this.suppliesCode = suppliesCode;
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
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	/**
	 * @return the suppliesCode
	 */
	public String getSuppliesCode() {
		return suppliesCode;
	}
	/**
	 * @param suppliesCode the suppliesCode to set
	 */
	public void setSuppliesCode(String suppliesCode) {
		this.suppliesCode = suppliesCode;
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
	public Timestamp getUpdatedDate() {
		return updatedDate;
	}
	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(Timestamp updatedDate) {
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
