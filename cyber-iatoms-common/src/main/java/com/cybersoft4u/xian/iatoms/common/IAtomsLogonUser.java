package com.cybersoft4u.xian.iatoms.common;

import java.util.List;
import java.util.Map;

import cafe.core.bean.identity.LogonUser;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmFunctionTypeDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * Purpose: 当前登入者资料
 * @author allenchen	
 * @since  JDK 1.7
 * @date   2016/3/30
 * @MaintenancePersonnel
 */
public class IAtomsLogonUser extends LogonUser {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5699536608885313067L;	
	/**
	 * 当前登录者讯息
	 */
	private AdmUserDTO admUserDTO;
	/**
	 * 当前登入者所属角色
	 */
	@JsonIgnore
	private List<AdmRoleDTO> userFunctions;
	
	/**
	 * 当前登入这所属角色Code
	 */
	@JsonIgnore
	private List<String> userFunctionCodes;
	
	/**
	 * 當前登入者parent URL
	 */
	private List<AdmFunctionTypeDTO> parentNodes;
	/**
	 * 當前登入者child URL
	 */
	private List<AdmFunctionTypeDTO> childNodes;
	/**
	 * 當前登入者與某功能之權限
	 */
	@JsonIgnore
	private Map<String,String> accRghts;
	
	/**
	 * 當前登入者沒有的功能權限
	 */
	@JsonIgnore
	private Map<String,String> unAccRghts;
	/**
	 * 是否cyber用戶
	 */
	private Boolean isCyberUser;	
	/**
	 * 登錄ip
	 */
	private String fromIp;
	/**
	 * 
	 */
	private String passwordIgnoreFlag;
	/**
	 * 登陸者密碼
	 */
	private String logUserPwd;
	/**
	 * Constructor:无参构造函数
	 */
	public IAtomsLogonUser() {
		
	}

	/**
	 * Constructor:带参构造函数
	 */
	public IAtomsLogonUser(LogonUser logonUser) {
		super(logonUser);
	}

	/**
	 * Constructor:
	 */
	public IAtomsLogonUser(String id) {
		super(id);
	}	
	/**
	 * @return the userFunctions
	 */
	public List<AdmRoleDTO> getUserFunctions() {
		return userFunctions;
	}

	/**
	 * @param userFunctions the userFunctions to set
	 */
	public void setUserFunctions(List<AdmRoleDTO> userFunctions) {
		this.userFunctions = userFunctions;
	}

	/**
	 * @return the parentNodes
	 */
	public List<AdmFunctionTypeDTO> getParentNodes() {
		return parentNodes;
	}

	/**
	 * @param parentNodes the parentNodes to set
	 */
	public void setParentNodes(List<AdmFunctionTypeDTO> parentNodes) {
		this.parentNodes = parentNodes;
	}

	/**
	 * @return the childNodes
	 */
	public List<AdmFunctionTypeDTO> getChildNodes() {
		return childNodes;
	}

	/**
	 * @param childNodes the childNodes to set
	 */
	public void setChildNodes(List<AdmFunctionTypeDTO> childNodes) {
		this.childNodes = childNodes;
	}
	

	/**
	 * @return the accRghts
	 */
	public Map<String, String> getAccRghts() {
		return accRghts;
	}

	/**
	 * @param accRghts the accRghts to set
	 */
	public void setAccRghts(Map<String, String> accRghts) {
		this.accRghts = accRghts;
	}

	/**
	 * @return the userFunctionCodes
	 */
	public List<String> getUserFunctionCodes() {
		return userFunctionCodes;
	}

	/**
	 * @param userFunctionCodes the userFunctionCodes to set
	 */
	public void setUserFunctionCodes(List<String> userFunctionCodes) {
		this.userFunctionCodes = userFunctionCodes;
	}

	/**
	 * @return the admUserDTO
	 */
	public AdmUserDTO getAdmUserDTO() {
		return admUserDTO;
	}

	/**
	 * @param admUserDTO the admUserDTO to set
	 */
	public void setAdmUserDTO(AdmUserDTO admUserDTO) {
		this.admUserDTO = admUserDTO;
	}

	/**
	 * @return the isCyberUser
	 */
	public Boolean getIsCyberUser() {
		return isCyberUser;
	}

	/**
	 * @param isCyberUser the isCyberUser to set
	 */
	public void setIsCyberUser(Boolean isCyberUser) {
		this.isCyberUser = isCyberUser;
	}

	/**
	 * @return the fromIp
	 */
	public String getFromIp() {
		return fromIp;
	}

	/**
	 * @param fromIp the fromIp to set
	 */
	public void setFromIp(String fromIp) {
		this.fromIp = fromIp;
	}
	/**
	 * @return the passwordIgnoreFlag
	 */
	public String getPasswordIgnoreFlag() {
		return passwordIgnoreFlag;
	}
	/**
	 * @param passwordIgnoreFlag the passwordIgnoreFlag to set
	 */
	public void setPasswordIgnoreFlag(String passwordIgnoreFlag) {
		this.passwordIgnoreFlag = passwordIgnoreFlag;
	}
	

	/**
	 * @return the unAccRghts
	 */
	public Map<String, String> getUnAccRghts() {
		return unAccRghts;
	}

	/**
	 * @param unAccRghts the unAccRghts to set
	 */
	public void setUnAccRghts(Map<String, String> unAccRghts) {
		this.unAccRghts = unAccRghts;
	}

	/**
	 * @return the logUserPwd
	 */
	public String getLogUserPwd() {
		return logUserPwd;
	}

	/**
	 * @param logUserPwd the logUserPwd to set
	 */
	public void setLogUserPwd(String logUserPwd) {
		this.logUserPwd = logUserPwd;
	}
	
}
