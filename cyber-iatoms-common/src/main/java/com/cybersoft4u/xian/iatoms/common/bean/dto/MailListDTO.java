package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;

import cafe.core.bean.dto.DataTransferObject;

/**
 * 
 * Purpose: 電子郵件群組維護DTO
 * @author allenchen
 * @since  JDK 1.7
 * @date   2016-6-30
 * @MaintenancePersonnel allenchen
 */
public class MailListDTO extends DataTransferObject<String>{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7296393099075830013L;
	/*public static void main(String[] args) {
		BindPageDataUtils.generateAttributeEnum(MailListDTO.class);
	}*/
		
	public static enum ATTRIBUTE {
		MAIL_ID("mailId"),
		MAIL_GROUP("mailGroup"),
		MAIL_GROUP_NAME("mailGroupName"),
		NAME("name"),
		EMAIL("email"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
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


	private String mailId;//郵件代碼
	private String mailGroup;//郵件類別
	private String mailGroupName;//郵件類別名稱
	private String name;//名稱
	private String email;//Email
	private String createdById;//建立者代號
	private String createdByName;//建立者姓名
	private String updatedById;//更新者代號
	private String updatedByName;//更新者姓名
	private Timestamp updatedDate;//更新日期
	/**
	 * @return the mailId
	 */
	public String getMailId() {
		return mailId;
	}
	/**
	 * @return the mailGroup
	 */
	public String getMailGroup() {
		return mailGroup;
	}
	/**
	 * @return the mailGroupName
	 */
	public String getMailGroupName() {
		return mailGroupName;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @return the createdById
	 */
	public String getCreatedById() {
		return createdById;
	}
	/**
	 * @return the createdByName
	 */
	public String getCreatedByName() {
		return createdByName;
	}

	/**
	 * @return the updatedById
	 */
	public String getUpdatedById() {
		return updatedById;
	}
	/**
	 * @return the updatedByName
	 */
	public String getUpdatedByName() {
		return updatedByName;
	}
	/**
	 * @return the updatedDate
	 */
	public Timestamp getUpdatedDate() {
		return updatedDate;
	}
	/**
	 * @param mailId the mailId to set
	 */
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}
	/**
	 * @param mailGroup the mailGroup to set
	 */
	public void setMailGroup(String mailGroup) {
		this.mailGroup = mailGroup;
	}
	/**
	 * @param mailGroupName the mailGroupName to set
	 */
	public void setMailGroupName(String mailGroupName) {
		this.mailGroupName = mailGroupName;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @param createdById the createdById to set
	 */
	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}
	/**
	 * @param createdByName the createdByName to set
	 */
	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	/**
	 * @param updatedById the updatedById to set
	 */
	public void setUpdatedById(String updatedById) {
		this.updatedById = updatedById;
	}
	/**
	 * @param updatedByName the updatedByName to set
	 */
	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}
	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}
	
}
