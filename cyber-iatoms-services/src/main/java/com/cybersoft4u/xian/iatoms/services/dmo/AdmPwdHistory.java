package com.cybersoft4u.xian.iatoms.services.dmo;

//Generated 2016/3/30 �U�� 01:51:49 by Hibernate Tools 3.4.0.CR1

import java.sql.Timestamp;

import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmPwdHistoryDTO;

/**
 * AdmRole generated by hbm2java
 */
public class AdmPwdHistory extends DomainModelObject<String,AdmPwdHistoryDTO> {
	
	private Integer pwdHisId;
	private String userId;
	private String password;
	private Timestamp createDate;

	public AdmPwdHistory() {
	}
	public AdmPwdHistory(Integer pwdHisId, String userId, String password,Timestamp createDate) {
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
