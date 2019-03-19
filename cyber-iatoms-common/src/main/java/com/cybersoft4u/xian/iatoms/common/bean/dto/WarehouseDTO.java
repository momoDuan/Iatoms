package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.util.Date;

import cafe.core.bean.dto.DataTransferObject;

/**
 * Purpose:倉庫DTO
 * @author ElvaHe
 * @since JDK 1.6
 * @date 2016年6月1日
 * @MaintenancePersonnel ElvaHe
 */
public class WarehouseDTO extends DataTransferObject<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2930192841701052358L;

	public static enum ATTRIBUTE {
		WAREHOUSE_ID("warehouseId"),
		COMPANY_ID("companyId"),
		COMPANY_NAME("companyName"),
		NAME("name"),
		CONTACT("contact"),
		TEL("tel"),
		FAX("fax"),
		LOCATION("location"),
		ADDRESS("address"),
		COMMENT("comment"),
		DELETED("deleted"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
		CREATED_DATE("createdDate"),
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
	 * 倉庫編號
	 */
	private String warehouseId;
	/**
	 * 廠商ID
	 */
	private String companyId;
	/**
	 * 厂商名稱
	 */
	private String companyName;
	/**
	 * 倉庫名稱
	 */
	private String name;
	
	/**
	 * 聯絡人
	 */
	private String contact;
	
	/**
	 * 電話
	 */
	private String tel;
	
	/**
	 * 傳真
	 */
	private String fax;
	
	/**
	 * 市縣區域
	 */
	private String location;
	
	/**
	 * 倉庫地址
	 */
	private String address;
	
	/**
	 * 說明
	 */
	private String comment;
	
	/**
	 * 是否已刪除
	 */
	private String deleted = "N";
	
	/**
	 * 建立者代號
	 */
	private String createdById;
	
	/**
	 * 建立者姓名
	 */
	private String createdByName;
	
	/**
	 * 建立日期
	 */
	private Date createdDate;
	
	/**
	 * 更新者代號
	 */
	private String updatedById;
	
	/**
	 * 更新者姓名
	 */
	private String updatedByName;
	
	/**
	 * 更新日期
	 */
	private Date updatedDate;

	/**
	 * Constructor:無參構造
	 */
	public WarehouseDTO() {
	}

	/**
	 * Constructor:有參構造
	 */
	
	public WarehouseDTO(String warehouseId, String companyId,
			String companyName, String name, String contact, String tel,
			String fax, String location, String address, String comment, String deleted,
			String createdById, String createdByName, Date createdDate,
			String updatedById, String updatedByName, Date updatedDate) {
		super();
		this.warehouseId = warehouseId;
		this.companyId = companyId;
		this.companyName = companyName;
		this.name = name;
		this.contact = contact;
		this.tel = tel;
		this.fax = fax;
		this.location = location;
		this.address = address;
		this.comment = comment;
		this.deleted = deleted;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
	}

	/**
	 * @return the warehouseId
	 */
	public String getWarehouseId() {
		return warehouseId;
	}

	/**
	 * @param warehouseId the warehouseId to set
	 */
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
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
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the contact
	 */
	public String getContact() {
		return contact;
	}

	/**
	 * @param contact the contact to set
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}

	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}

	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	 * @return the fax
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
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
