package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;

import cafe.core.bean.dto.DataTransferObject;

/**
 * Purpose: 密碼歷史記錄
 * @author evanliu
 * @since  JDK 1.6
 * @date   2016年7月6日
 * @MaintenancePersonnel evanliu
 */
public class AdmPwdHistoryDTO extends DataTransferObject<String> {
	
	/**
	 * 
	 * Purpose: 使用者密碼歷史記錄ATTRIBUTE
	 * @author evanliu
	 * @since  JDK 1.6
	 * @date   2016年6月19日
	 * @MaintenancePersonnel evanliu
	 */
	public static enum ATTRIBUTE {
		PWD_HIS_ID("pwdHisId"),
		USER_ID("userId"),
		PASSWORD("password"),
		CREATE_DATE("createDate"),
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
	 * 密碼曆史記錄編號
	 */
	private Integer pwdHisId;
	/**
	 * 使用者帳號
	 */
	private String userId;
	/**
	 * 密碼
	 */
	private String password;
	/**
	 * 創建時間
	 */
	private Timestamp createDate;
	/**
	 * 
	 * Constructor:無參構造
	 */
	public AdmPwdHistoryDTO() {
	}
	/**
	 * 
	 * Constructor:有參構造
	 */
	public AdmPwdHistoryDTO(Integer pwdHisId, String userId, String password,Timestamp createDate) {
		super();
		this.pwdHisId = pwdHisId;
		this.userId = userId;
		this.password = password;
		this.createDate = createDate;
	}
	/**
	 * @return the pwdHisId
	 */
	public Integer getPwdHisId() {
		return pwdHisId;
	}
	/**
	 * @param pwdHisId the pwdHisId to set
	 */
	public void setPwdHisId(Integer pwdHisId) {
		this.pwdHisId = pwdHisId;
	}
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the createDate
	 */
	public Timestamp getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
}
