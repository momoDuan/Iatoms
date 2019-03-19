package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.PasswordSettingDTO;
/**
 * Purpose: 密碼原則設定FormDTO
 * @author jasonzhou
 * @since  JDK 1.6
 * @date   2016/5/19
 * @MaintenancePersonnel jasonzhou
 */
@SuppressWarnings("rawtypes")
public class PasswordSettingFormDTO extends AbstractSimpleListFormDTO<PasswordSettingDTO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7580175586444840493L;
	
	/**
	 * 密碼原則設定DTO，密碼規則設定頁面使用
	 */
	private PasswordSettingDTO passwordSettingDTO;
	
	/**
	 * 獲取密碼長度集合
	 */
	private List<Parameter>  passwordLengthList;
	
	/**
	 * Constructor:無參構造方法
	 */
	public PasswordSettingFormDTO(){
			
	}

	/**
	 * @return the passwordSettingDTO
	 */
	public PasswordSettingDTO getPasswordSettingDTO() {
		return passwordSettingDTO;
	}

	/**
	 * @param passwordSettingDTO the passwordSettingDTO to set
	 */
	public void setPasswordSettingDTO(PasswordSettingDTO passwordSettingDTO) {
		this.passwordSettingDTO = passwordSettingDTO;
	}

	/**
	 * @return the passwordLengthList
	 */
	public List<Parameter> getPasswordLengthList() {
		return passwordLengthList;
	}

	/**
	 * @param passwordLengthList the passwordLengthList to set
	 */
	public void setPasswordLengthList(List<Parameter> passwordLengthList) {
		this.passwordLengthList = passwordLengthList;
	}
}
