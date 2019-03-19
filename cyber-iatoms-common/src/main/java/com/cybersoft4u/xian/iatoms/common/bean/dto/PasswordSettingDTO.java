package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;

import cafe.core.bean.dto.DataTransferObject;
/**
 * Purpose: 密碼原則設定DTO
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2016/5/18
 * @MaintenancePersonnel HermanWang
 */
public class PasswordSettingDTO extends DataTransferObject<String>{

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 3565863151296327670L;
	
	public static enum ATTRIBUTE {
		ID("id"),
		PWD_LEN_BG("pwdLenBg"),
		PWD_LEN_ND("pwdLenNd"),
		PWD_VALID_DAY("pwdValidDay"),
		ID_VALID_DAY("idValidDay"),
		PWD_ERR_CNT("pwdErrCnt"),
		PWD_RP_CNT("pwdRpCnt"),
		PWD_CHG_FLAG("pwdChgFlag"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		UPDATED_DATE("updatedDate"),
		PWD_ALERT_DAY("pwdAlertDay");
		/**
		 * value
		 */
		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		}
		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}
	};
	
	/**
	 * 密碼原則主鍵編號
	 */
	private String id;
	/**
	 * 密碼最小長度
	 */
	private Integer pwdLenBg;
	/**
	 * 密碼最大長度
	 */
	private Integer pwdLenNd;
	/**
	 * 密碼有效周期
	 */
	private Integer pwdValidDay;
	/**
	 * 帳號停權周期
	 */
	private Integer idValidDay;
	/**
	 * 密碼容許錯誤次數
	 */
	private Integer pwdErrCnt;
	/**
	 * 新密碼不可與前幾次重複
	 */
	private Integer pwdRpCnt;
	/**
	 * 首次登入是否需要修改密碼
	 */
	private String pwdChgFlag = "Y";
	/**
	 * 異動人員編號
	 */
	private String updatedById;
	/**
	 * 異動人員姓名 
	 */
	private String updatedByName;
	/**
	 * 異動日期
	 */
	private Timestamp updatedDate;
	/**
	 * 密碼到期天數
	 */
	private Integer pwdAlertDay;
	
	/**
	 * Constructor:
	 */
	public PasswordSettingDTO() {
		
	}

	/**
	 * Constructor:
	 */
	public PasswordSettingDTO(String id, Integer pwdLenBg, Integer pwdLenNd,
			Integer pwdValidDay, Integer idValidDay, Integer pwdErrCnt,
			Integer pwdRpCnt, String pwdChgFlag, String updatedById,
			String updatedByName, Timestamp updatedDate, Integer pwdAlertDay) {
		this.id = id;
		this.pwdLenBg = pwdLenBg;
		this.pwdLenNd = pwdLenNd;
		this.pwdValidDay = pwdValidDay;
		this.idValidDay = idValidDay;
		this.pwdErrCnt = pwdErrCnt;
		this.pwdRpCnt = pwdRpCnt;
		this.pwdChgFlag = pwdChgFlag;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
		this.pwdAlertDay = pwdAlertDay;
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
