package com.cybersoft4u.xian.iatoms.services.dmo;

import java.sql.Timestamp;
import java.util.Date;

import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.PasswordSettingDTO;

/**
 * Purpose: 密碼原則設定
 * @author jasonzhou
 * @since  JDK 1.6
 * @date   2016/5/20
 * @MaintenancePersonnel jasonzhou
 */
public class AdmSecurityDef extends DomainModelObject<String, PasswordSettingDTO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1848544072168915999L;
	
	private String id;
	private Integer pwdLenBg;
	private Integer pwdLenNd;
	private Integer pwdValidDay;
	private Integer idValidDay;
	private Integer pwdErrCnt;
	private Integer pwdRpCnt;
	private String pwdChgFlag = "Y";
	private Integer pwdAlertDay;
	private String updatedById;
	private String updatedByName;
	private Date updatedDate;
	
	public AdmSecurityDef(){
		
	}
	
	public AdmSecurityDef(Integer pwdLenBg, Integer pwdLenNd,
			Integer pwdValidDay, Integer idValidDay, Integer pwdErrCnt,
			Integer pwdRpCnt, String pwdChgFlag, String updatedById,
			String updatedByName, Timestamp updatedDate, Integer pwdAlertDay) {
		this.pwdLenBg = pwdLenBg;
		this.pwdLenNd = pwdLenNd;
		this.pwdValidDay = pwdValidDay;
		this.idValidDay = idValidDay;
		this.pwdErrCnt = pwdErrCnt;
		this.pwdRpCnt = pwdRpCnt;
		this.pwdChgFlag = pwdChgFlag;
		this.pwdAlertDay = pwdAlertDay;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
	}

	/**
	 * @return the pwdLenBg
	 */
	public Integer getPwdLenBg() {
		return pwdLenBg;
	}

	/**
	 * @param pwdLenBg the pwdLenBg to set
	 */
	public void setPwdLenBg(Integer pwdLenBg) {
		this.pwdLenBg = pwdLenBg;
	}

	/**
	 * @return the pwdLenNd
	 */
	public Integer getPwdLenNd() {
		return pwdLenNd;
	}

	/**
	 * @param pwdLenNd the pwdLenNd to set
	 */
	public void setPwdLenNd(Integer pwdLenNd) {
		this.pwdLenNd = pwdLenNd;
	}

	/**
	 * @return the pwdValidDay
	 */
	public Integer getPwdValidDay() {
		return pwdValidDay;
	}

	/**
	 * @param pwdValidDay the pwdValidDay to set
	 */
	public void setPwdValidDay(Integer pwdValidDay) {
		this.pwdValidDay = pwdValidDay;
	}

	/**
	 * @return the idValidDay
	 */
	public Integer getIdValidDay() {
		return idValidDay;
	}

	/**
	 * @param idValidDay the idValidDay to set
	 */
	public void setIdValidDay(Integer idValidDay) {
		this.idValidDay = idValidDay;
	}

	/**
	 * @return the pwdErrCnt
	 */
	public Integer getPwdErrCnt() {
		return pwdErrCnt;
	}

	/**
	 * @param pwdErrCnt the pwdErrCnt to set
	 */
	public void setPwdErrCnt(Integer pwdErrCnt) {
		this.pwdErrCnt = pwdErrCnt;
	}

	/**
	 * @return the pwdRpCnt
	 */
	public Integer getPwdRpCnt() {
		return pwdRpCnt;
	}

	/**
	 * @param pwdRpCnt the pwdRpCnt to set
	 */
	public void setPwdRpCnt(Integer pwdRpCnt) {
		this.pwdRpCnt = pwdRpCnt;
	}

	/**
	 * @return the pwdChgFlag
	 */
	public String getPwdChgFlag() {
		return pwdChgFlag;
	}

	/**
	 * @param pwdChgFlag the pwdChgFlag to set
	 */
	public void setPwdChgFlag(String pwdChgFlag) {
		this.pwdChgFlag = pwdChgFlag;
	}

	/**
	 * @return the pwdAlertDay
	 */
	public Integer getPwdAlertDay() {
		return pwdAlertDay;
	}

	/**
	 * @param pwdAlertDay the pwdAlertDay to set
	 */
	public void setPwdAlertDay(Integer pwdAlertDay) {
		this.pwdAlertDay = pwdAlertDay;
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
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
}
