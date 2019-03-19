package com.cybersoft4u.xian.iatoms.common.bean.dto;
import java.sql.Timestamp;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cafe.core.bean.dto.DataTransferObject;
/**
 * 
 * Purpose: 员工信息 
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016/4/13
 * @MaintenancePersonnel CrissZhang
 */
public class AdmUserDTO extends DataTransferObject<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4964248010394205401L;
	/**
	 * 
	 * Purpose: 使用者ATTRIBUTE
	 * @author CrissZhang
	 * @since  JDK 1.6
	 * @date   2016年6月19日
	 * @MaintenancePersonnel CrissZhang
	 */
	public static enum ATTRIBUTE {
		USER_ID("userId"),
		ACCOUNT("account"),
		COMPANY_ID("companyId"),
		DEPT_CODE("deptCode"),
		PASSWORD("password"),
		CNAME("cname"),
		ENAME("ename"),
		EMAIL("email"),
		MANAGER_EMAIL("managerEmail"),
		TEL("tel"),
		MOBILE("mobile"),
		USER_DESC("userDesc"),
		DATA_ACL("dataAcl"),
		RETRY("retry"),
		STATUS("status"),
		LAST_LOGIN_DATE("lastLoginDate"),
		CHANGE_PWD_DATE("changePwdDate"),
		DELETED("deleted"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
		CREATED_DATE("createdDate"),
		DISABLED_PWD("disabledPwd"),
		REQUIRED_PWD("requiredPwd"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		UPDATED_DATE("updatedDate"),
		ROLE_GROUP("functionGroup"),
		COMPANY("company"),
		DEPT_NAME("deptName"),
		ACCOUNT_STATUS("accountStatus"),
		RESET_PWD("resetPwd"),
		REPASSWORD("repassword"),
		ROLE_ID("roleId"),
		WAREHOUSE_GROUP("warehouseGroup");

		/**
		 * value值
		 */
		private String value;
		
		/**
		 * Constructor:構造函數
		 */
		ATTRIBUTE(String value) {
			this.value = value;
		};
		
		/**
		 * @return the value
		 */
		public String getValue() {
			return this.value;
		}
	};

	/**
	 * 使用者編號
	 */
	private String userId;
	/**
	 * 使用者登錄帳號
	 */
	private String account;
	/**
	 * 公司編號
	 */
	private String companyId;
	/**
	 * 部門編號
	 */
	private String deptCode;
	/**
	 * 密碼
	 */
	private String password;
	/**
	 * 中文名稱
	 */
	private String cname;
	/**
	 * 英文名稱
	 */
	private String ename;
	/**
	 * email
	 */
	private String email;
	/**
	 * 主管Email
	 */
	private String managerEmail;
	/**
	 * 電話
	 */
	private String tel;
	/**
	 * 手機
	 */
	private String mobile;
	/**
	 * 描述
	 */
	private String userDesc;
	/**
	 * ACL
	 */
	private String dataAcl = "N";
	/**
	 * 密碼重複次數
	 */
	private Integer retry;
	/**
	 * 狀態
	 */
	private String status;
	/**
	 * 上一次登錄時間
	 */
	private Timestamp lastLoginDate;
	/**
	 * 修改密碼時間
	 */
	private Date changePwdDate;	
	/**
	 * 是否被刪除
	 */
	private String deleted = "N";
	/**
	 * 創建者
	 */
	private String createdById;
	/**
	 * 創建者姓名
	 */
	private String createdByName;
	/**
	 * 創建時間
	 */
	private Timestamp createdDate;
	/**
	 * 修改者
	 */
	private String updatedById;
	/**
	 * 修改者姓名
	 */
	private String updatedByName;
	/**
	 * 修改時間
	 */
	private Timestamp updatedDate;
	
	/**
	 * 角色集合
	 */
	@JsonIgnore
	private String functionGroup;
	/**
	 * 公司名稱
	 */
	private String company;
	/**
	 * 部門名稱
	 */
	private String deptName;
	/**
	 * 帳號狀態--中文描述
	 */
	private String accountStatus;
	/**
	 * 仓库据点集合
	 */
	private String warehouseGroup;
	/**
	 * 確認密碼
	 */
	private String repassword;
	/**
	 * 是否iatoms驗證
	 */
	private Boolean isAuthenTypeEqualsIAtoms;
	/**
	 * 重置密碼
	 */
	private String resetPwd = "N";
	/**
	 * 公司名稱
	 */
	private String companyName;
	/**
	 * 帳號狀態名稱
	 */
	private String statusName;
	/**
	 * 密碼框禁用
	 */
	private String disabledPwd;
	/**
	 * 密碼框必填
	 */
	private String requiredPwd;
	/**
	 * 已選角色權限
	 */
	@JsonIgnore
	private String selectFunctionStr;
	/**
	 * 全部角色權限
	 */
	@JsonIgnore
	private String functionStrAll;
	/**
	 * 已選倉庫控管
	 */
	private String selectWarehouseStr;
	/**
	 *	所有倉庫控管
	 */
	private String warehouseStrAll;
	/**
	 * 密碼顯示文本
	 */
	private String pwdShowText;
	/**
	 * 域驗證失敗
	 */
	private boolean isLdapFailure;
	/**
	 * Constructor:建構子
	 */
	public AdmUserDTO() {
	}
	/**
	 * 
	 * Constructor:建構子
	 */
	public AdmUserDTO(String userId, String account, String deleted) {
		this.userId = userId;
		this.account = account;
		this.deleted = deleted;
	}
	/**
	 * 
	 * Constructor:建構子
	 */
	public AdmUserDTO(String userId, String dataAcl, Integer retry, String deleted) {
		this.userId = userId;
		this.dataAcl = dataAcl;
		this.retry = retry;
		this.deleted = deleted;
	}
	/**
	 * 
	 * Constructor:建構子
	 */
	public AdmUserDTO(String userId, String account, String companyId, String deptCode, String password,
			String cname, String ename, String tel, String mobile, String email, String managerEmail, String userDesc,
			Integer retry, String status, String dataAcl, Timestamp lastLoginDate, Date changePwdDate, String createdById,
			String createdByName, Timestamp createdDate, String updatedById, String updatedByName, Timestamp updatedDate,
			String deleted, String roleGroup) {
		this(userId, account, companyId, deptCode, password, cname, ename, tel, mobile, 
				email, managerEmail, userDesc, retry, status, dataAcl, 
				lastLoginDate, changePwdDate, createdById, createdByName, createdDate, 
				updatedById, updatedByName, updatedDate, deleted);
		this.functionGroup = roleGroup;
	}
	/**
	 * 
	 * Constructor:建構子
	 */
	public AdmUserDTO(String userId, String account, String companyId, String deptCode, String password,
			String cname, String ename, String tel, String mobile, String email, String managerEmail, String userDesc,
			Integer retry, String status, String dataAcl, Timestamp lastLoginDate, Date changePwdDate, String createdById,
			String createdByName, Timestamp createdDate, String updatedById, String updatedByName, Timestamp updatedDate,
			String deleted) {
		this.userId = userId;
		this.account = account;
		this.companyId = companyId;
		this.deptCode = deptCode;
		this.password = password;
		this.cname = cname;
		this.ename = ename;
		this.tel = tel;
		this.mobile = mobile;
		this.email = email;
		this.managerEmail = managerEmail;
		this.userDesc = userDesc;
		this.retry = retry;
		this.status = status;
		this.dataAcl = dataAcl;
		this.lastLoginDate = lastLoginDate;
		this.changePwdDate = changePwdDate;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
		this.deleted = deleted;
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
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}
	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
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
	 * @return the deptCode
	 */
	public String getDeptCode() {
		return deptCode;
	}
	/**
	 * @param deptCode the deptCode to set
	 */
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
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
	 * @return the cname
	 */
	public String getCname() {
		return cname;
	}
	/**
	 * @param cname the cname to set
	 */
	public void setCname(String cname) {
		this.cname = cname;
	}
	/**
	 * @return the ename
	 */
	public String getEname() {
		return ename;
	}
	/**
	 * @param ename the ename to set
	 */
	public void setEname(String ename) {
		this.ename = ename;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the managerEmail
	 */
	public String getManagerEmail() {
		return managerEmail;
	}
	/**
	 * @param managerEmail the managerEmail to set
	 */
	public void setManagerEmail(String managerEmail) {
		this.managerEmail = managerEmail;
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
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * @return the userDesc
	 */
	public String getUserDesc() {
		return userDesc;
	}
	/**
	 * @param userDesc the userDesc to set
	 */
	public void setUserDesc(String userDesc) {
		this.userDesc = userDesc;
	}
	/**
	 * @return the dataAcl
	 */
	public String getDataAcl() {
		return dataAcl;
	}
	/**
	 * @param dataAcl the dataAcl to set
	 */
	public void setDataAcl(String dataAcl) {
		this.dataAcl = dataAcl;
	}
	/**
	 * @return the retry
	 */
	public Integer getRetry() {
		return retry;
	}
	/**
	 * @param retry the retry to set
	 */
	public void setRetry(Integer retry) {
		this.retry = retry;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the lastLoginDate
	 */
	public Timestamp getLastLoginDate() {
		return lastLoginDate;
	}
	/**
	 * @param lastLoginDate the lastLoginDate to set
	 */
	public void setLastLoginDate(Timestamp lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	/**
	 * @return the changePwdDate
	 */
	public Date getChangePwdDate() {
		return changePwdDate;
	}
	/**
	 * @param changePwdDate the changePwdDate to set
	 */
	public void setChangePwdDate(Date changePwdDate) {
		this.changePwdDate = changePwdDate;
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
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Timestamp createdDate) {
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
	 * @return the functionGroup
	 */
	public String getFunctionGroup() {
		return functionGroup;
	}
	/**
	 * @param functionGroup the functionGroup to set
	 */
	public void setFunctionGroup(String functionGroup) {
		this.functionGroup = functionGroup;
	}
	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}
	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	/**
	 * @return the deptName
	 */
	public String getDeptName() {
		return deptName;
	}
	/**
	 * @param deptName the deptName to set
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	/**
	 * @return the accountStatus
	 */
	public String getAccountStatus() {
		return accountStatus;
	}
	/**
	 * @param accountStatus the accountStatus to set
	 */
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	
	/**
	 * @return the repassword
	 */
	public String getRepassword() {
		return repassword;
	}
	/**
	 * @param repassword the repassword to set
	 */
	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}
	
	/**
	 * @return the warehouseGroup
	 */
	public String getWarehouseGroup() {
		return warehouseGroup;
	}
	/**
	 * @param warehouseGroup the warehouseGroup to set
	 */
	public void setWarehouseGroup(String warehouseGroup) {
		this.warehouseGroup = warehouseGroup;
	}
	
	/**
	 * @return the isAuthenTypeEqualsIAtoms
	 */
	public Boolean getIsAuthenTypeEqualsIAtoms() {
		return isAuthenTypeEqualsIAtoms;
	}
	/**
	 * @param isAuthenTypeEqualsIAtoms the isAuthenTypeEqualsIAtoms to set
	 */
	public void setIsAuthenTypeEqualsIAtoms(Boolean isAuthenTypeEqualsIAtoms) {
		this.isAuthenTypeEqualsIAtoms = isAuthenTypeEqualsIAtoms;
	}
	
	/**
	 * @return the resetPwd
	 */
	public String getResetPwd() {
		return resetPwd;
	}
	/**
	 * @param resetPwd the resetPwd to set
	 */
	public void setResetPwd(String resetPwd) {
		this.resetPwd = resetPwd;
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
	 * @return the statusName
	 */
	public String getStatusName() {
		return statusName;
	}
	/**
	 * @param statusName the statusName to set
	 */
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	/**
	 * @return the disabledPwd
	 */
	public String getDisabledPwd() {
		return disabledPwd;
	}
	/**
	 * @param disabledPwd the disabledPwd to set
	 */
	public void setDisabledPwd(String disabledPwd) {
		this.disabledPwd = disabledPwd;
	}
	
	/**
	 * @return the selectFunctionStr
	 */
	public String getSelectFunctionStr() {
		return selectFunctionStr;
	}
	/**
	 * @param selectFunctionStr the selectFunctionStr to set
	 */
	public void setSelectFunctionStr(String selectFunctionStr) {
		this.selectFunctionStr = selectFunctionStr;
	}
	/**
	 * @return the selectWarehouseStr
	 */
	public String getSelectWarehouseStr() {
		return selectWarehouseStr;
	}
	/**
	 * @param selectWarehouseStr the selectWarehouseStr to set
	 */
	public void setSelectWarehouseStr(String selectWarehouseStr) {
		this.selectWarehouseStr = selectWarehouseStr;
	}
	/**
	 * @return the requiredPwd
	 */
	public String getRequiredPwd() {
		return requiredPwd;
	}
	/**
	 * @param requiredPwd the requiredPwd to set
	 */
	public void setRequiredPwd(String requiredPwd) {
		this.requiredPwd = requiredPwd;
	}
	
	/**
	 * @return the functionStrAll
	 */
	public String getFunctionStrAll() {
		return functionStrAll;
	}
	/**
	 * @param functionStrAll the functionStrAll to set
	 */
	public void setFunctionStrAll(String functionStrAll) {
		this.functionStrAll = functionStrAll;
	}
	/**
	 * @return the warehouseStrAll
	 */
	public String getWarehouseStrAll() {
		return warehouseStrAll;
	}
	/**
	 * @param warehouseStrAll the warehouseStrAll to set
	 */
	public void setWarehouseStrAll(String warehouseStrAll) {
		this.warehouseStrAll = warehouseStrAll;
	}
	/**
	 * @return the pwdShowText
	 */
	public String getPwdShowText() {
		return pwdShowText;
	}
	/**
	 * @param pwdShowText the pwdShowText to set
	 */
	public void setPwdShowText(String pwdShowText) {
		this.pwdShowText = pwdShowText;
	}
	/**
	 * @return the isLdapFailure
	 */
	public boolean getIsLdapFailure() {
		return isLdapFailure;
	}
	/**
	 * @param isLdapFailure the isLdapFailure to set
	 */
	public void setIsLdapFailure(boolean isLdapFailure) {
		this.isLdapFailure = isLdapFailure;
	}
}
