package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import cafe.core.bean.dto.AbstractFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.PasswordSettingDTO;

/**
 * Purpose: 密碼修改formDTO 
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2016年7月6日
 * @MaintenancePersonnel HermanWang
 */
public class PasswordFormDTO extends AbstractFormDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1980403454327578815L;
	/**
	 * 使用者編號
	 */
	private String userId;
	/**
	 * 舊密碼
	 */
	private String oldPassword;
	/**
	 * 新密碼
	 */
	private String newPassword;
	/**
	 * 新密碼不可與前幾次重複
	 */
	private Integer pwdRpCnt;
	/**
	 * 確認密碼
	 */
	private String rePassword;
	/**
	 * 密碼設定
	 */
	private PasswordSettingDTO admSecurityDefDTO;
	/**
	 * 是否為IAtoms驗證
	 */
	private Boolean isIatomsAuthen;
	/**
	 * 
	 * Constructor:無參構造函數
	 */
	public PasswordFormDTO() {
		
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
	 * @return the oldPassword
	 */
	public String getOldPassword() {
		return oldPassword;
	}
	/**
	 * @param oldPassword the oldPassword to set
	 */
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	
	/**
	 * @return the newPassword
	 */
	public String getNewPassword() {
		return newPassword;
	}

	/**
	 * @param newPassword the newPassword to set
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
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
	 * @return the admSecurityDefDTO
	 */
	public PasswordSettingDTO getAdmSecurityDefDTO() {
		return admSecurityDefDTO;
	}

	/**
	 * @param admSecurityDefDTO the admSecurityDefDTO to set
	 */
	public void setAdmSecurityDefDTO(PasswordSettingDTO admSecurityDefDTO) {
		this.admSecurityDefDTO = admSecurityDefDTO;
	}

	/**
	 * @return the rePassword
	 */
	public String getRePassword() {
		return rePassword;
	}

	/**
	 * @param rePassword the rePassword to set
	 */
	public void setRePassword(String rePassword) {
		this.rePassword = rePassword;
	}

	/**
	 * @return the isIatomsAuthen
	 */
	public Boolean getIsIatomsAuthen() {
		return isIatomsAuthen;
	}

	/**
	 * @param isIatomsAuthen the isIatomsAuthen to set
	 */
	public void setIsIatomsAuthen(Boolean isIatomsAuthen) {
		this.isIatomsAuthen = isIatomsAuthen;
	}

	
}
