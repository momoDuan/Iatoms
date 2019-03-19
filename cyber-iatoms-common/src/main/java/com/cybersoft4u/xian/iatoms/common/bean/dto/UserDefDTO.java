package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.util.Date;

import cafe.core.bean.dto.AbstractFormDTO;

/**
 * Purpose: Cafe範例
 * @author candicechen
 * @since  JDK 1.5
 * @date   2014/6/26
 * @MaintenancePersonnel candicechen
 */
public class UserDefDTO extends AbstractFormDTO{
	private static final long serialVersionUID = -4751314303936123880L;
	/**
	 * Purpose: 枚舉參數
	 * @author candicechen
	 * @since  JDK 1.5
	 * @date   2014/6/26
	 * @MaintenancePersonnel candicechen
	 */
	public enum ATTRIBUTE {
		USER_ID("userId"),
		USER_NAME("userName"),
		DESCRIPTION("description"),
		EFFECTIVE_DATE("effectiveDate");

		public String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
	};

	/**
	 * ID
	 */
	private String userId;
	/**
	 * 姓名
	 */
	private String userName;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 日期
	 */
	private Date effectiveDate;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
/*	public static void main(String[] args){
		BindPageDataUtils.generateAttributeEnum(UserDefDTO.class);
	}*/
	/**
	 * @return the effectiveDate
	 */
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	/**
	 * @param effectiveDate the effectiveDate to set
	 */
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	
}
