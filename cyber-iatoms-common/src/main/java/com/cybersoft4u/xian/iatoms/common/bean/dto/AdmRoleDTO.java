package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;

import cafe.core.bean.dto.DataTransferObject;

/**
 * Purpose: 系統角色DTO
 * @author HaimingWang
 * @since  JDK 1.6
 * @date   2016年6月28日
 * @MaintenancePersonnel HaimingWang
 */
public class AdmRoleDTO extends DataTransferObject<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6140494835740300674L;
	
	public static enum ATTRIBUTE {
		ROLE_ID("roleId"),
		ROLE_CODE("roleCode"),
		ROLE_NAME("roleName"),
		ROLE_DESC("roleDesc"),
		ATTRIBUTE("attribute"),
		ATTRIBUTE_NAME("attributeName"),
		WORK_FLOW_ROLE("workFlowRole"),
		WORK_FLOW_ROLE_NAME("workFlowRoleName"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
		CREATED_DATE("createdDate"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		IS_CUSTOMER_SERVICE("isCustomerService"),
		IS_VENDOR_SERVICE("isVendorService"),
		IS_CUSTOMER("isCustomer"),
		IS_QA("isQA"),
		IS_TMS("isTMS"),
		IS_AGENT("isAgent"),
		IS_VENDOR_AGENT("isVendorAgent"),
		IS_ENGINEER("isEngineer"),
		USER_ID("userId"),
		ACCOUNT("account"),
		UPDATED_DATE("updatedDate"),
		IS_CUS_VENDOR_SERVICE("isCusVendorService"),;

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	
	/**
	 * 角色編號
	 */
	private String roleId;
	
	/**
	 * 角色代碼
	 */
	private String roleCode;
	
	/**
	 * 角色名稱
	 */
	private String roleName;
	
	/**
	 * 角色描述
	 */
	private String roleDesc;
		
	/**
	 * 角色屬性編號
	 */
	private String attribute;
	
	/**
	 * 角色屬性名稱
	 */
	private String attributeName;
	
	/**
	 * 表單角色編號
	 */
	private String workFlowRole;
	
	/**
	 * 表單角色名稱
	 */
	private String workFlowRoleName;
	
	/**
	 * 建立人員編號
	 */
	private String createdById;
	
	/**
	 * 建立人員姓名
	 */
	private String createdByName;
	
	/**
	 * 建立日期
	 */
	private Timestamp createdDate;
	
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
	 * 是客服，含有 	廠商角色、CyberAgent：客服可以查看并處理所有案件資料
	 */
	private boolean isCustomerService;
	
	/**
	 * CR #2951 廠商客服，含有 	廠商角色、CyberAgent：廠商客服可以查看并處理自己廠商案件資料
	 */
	private boolean isVendorService;
	
	/**
	 * 是客戶， 含有	客戶角色、銀行Agent：可以查看并建立本銀行之案件資料
	 */
	private boolean isCustomer;
	/**
	 * 是AQ，廠商角色、CyberAgent且角色为TMS：可以查看所有案件資料并處理派工給TMS的案件
	 */
	private boolean isQA;
	/**
	 * 是TMS，廠商角色、工程師：可以查看并處理派工给自己的案件資料
	 */
	private boolean isTMS;
	/**
	 * 是Agent，廠商角色、部門Agent：可以查看并處理派工给本部門的案件資料
	 */
	private boolean isAgent;
	/**
	 * 是工程師，廠商角色、工程師：可以查看并處理派工给自己的案件資料
	 */
	private boolean isEngineer;
	/**
	 * 是廠商Agent，廠商角色、廠商Agent：可以查看并處理派工给本廠商的案件資料
	 */
	private boolean isVendorAgent;
	/**
	 * 客戶廠商客服，客戶廠商角色、客戶廠商Agent且角色为CUS_VENDOR_SERVICE：可以查看并處理自己廠商的案件資料
	 */
	private boolean isCusVendorService;
	/**
	 * 使用者編號
	 */
	private String userId;
	/**
	 * 使用者帳號
	 */
	private String account;
	/**
	 * Constructor:無參構造
	 */
	public AdmRoleDTO() {
	}

	/**
	 * Constructor:有參構造
	 */
	public AdmRoleDTO(String roleId, String roleCode, String roleName,
			String roleDesc, String attribute,
			String attributeName, String workFlowRole,
			String workFlowRoleName, String createdById, String createdByName, 
			Timestamp createdDate, String updatedById,
			String updatedByName, Timestamp updatedDate) {
		super();
		this.roleId = roleId;
		this.roleCode = roleCode;
		this.roleName = roleName;
		this.roleDesc = roleDesc;
		this.attribute = attribute;
		this.attributeName = attributeName;
		this.workFlowRole = workFlowRole;
		this.workFlowRoleName = workFlowRoleName;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
	}

	/**
	 * @return the roleId
	 */
	public String getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the roleCode
	 */
	public String getRoleCode() {
		return roleCode;
	}

	/**
	 * @param roleCode the roleCode to set
	 */
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * @return the roleDesc
	 */
	public String getRoleDesc() {
		return roleDesc;
	}

	/**
	 * @param roleDesc the roleDesc to set
	 */
	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	/**
	 * @return the attribute
	 */
	public String getAttribute() {
		return attribute;
	}

	/**
	 * @param attribute the attribute to set
	 */
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	/**
	 * @return the attributeName
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * @param attributeName the attributeName to set
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	/**
	 * @return the workFlowRole
	 */
	public String getWorkFlowRole() {
		return workFlowRole;
	}

	/**
	 * @param workFlowRole the workFlowRole to set
	 */
	public void setWorkFlowRole(String workFlowRole) {
		this.workFlowRole = workFlowRole;
	}

	/**
	 * @return the workFlowRoleName
	 */
	public String getWorkFlowRoleName() {
		return workFlowRoleName;
	}

	/**
	 * @param workFlowRoleName the workFlowRoleName to set
	 */
	public void setWorkFlowRoleName(String workFlowRoleName) {
		this.workFlowRoleName = workFlowRoleName;
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
	 * @return the isCustomerService
	 */
	public boolean getIsCustomerService() {
		return isCustomerService;
	}

	/**
	 * @param isCustomerService the isCustomerService to set
	 */
	public void setIsCustomerService(boolean isCustomerService) {
		this.isCustomerService = isCustomerService;
	}

	/**
	 * @return the isCustomer
	 */
	public boolean getIsCustomer() {
		return isCustomer;
	}

	/**
	 * @param isCustomer the isCustomer to set
	 */
	public void setIsCustomer(boolean isCustomer) {
		this.isCustomer = isCustomer;
	}

	/**
	 * @return the isQA
	 */
	public boolean getIsQA() {
		return isQA;
	}

	/**
	 * @param isQA the isQA to set
	 */
	public void setIsQA(boolean isQA) {
		this.isQA = isQA;
	}

	/**
	 * @return the isTMS
	 */
	public boolean getIsTMS() {
		return isTMS;
	}

	/**
	 * @param isTMS the isTMS to set
	 */
	public void setIsTMS(boolean isTMS) {
		this.isTMS = isTMS;
	}

	/**
	 * @return the isAgent
	 */
	public boolean getIsAgent() {
		return isAgent;
	}

	/**
	 * @param isAgent the isAgent to set
	 */
	public void setIsAgent(boolean isAgent) {
		this.isAgent = isAgent;
	}

	/**
	 * @return the isEngineer
	 */
	public boolean getIsEngineer() {
		return isEngineer;
	}

	/**
	 * @param isEngineer the isEngineer to set
	 */
	public void setIsEngineer(boolean isEngineer) {
		this.isEngineer = isEngineer;
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
	 * @return the isVendorAgent
	 */
	public boolean getIsVendorAgent() {
		return isVendorAgent;
	}

	/**
	 * @param isVendorAgent the isVendorAgent to set
	 */
	public void setIsVendorAgent(boolean isVendorAgent) {
		this.isVendorAgent = isVendorAgent;
	}

	/**
	 * @return the isVendorService
	 */
	public boolean getIsVendorService() {
		return isVendorService;
	}

	/**
	 * @param isVendorService the isVendorService to set
	 */
	public void setIsVendorService(boolean isVendorService) {
		this.isVendorService = isVendorService;
	}
	/**
	 * @return the isCusVendorService
	 */
	public boolean getIsCusVendorService() {
		return isCusVendorService;
	}

	/**
	 * @param IsCusVendorService the IsCusVendorService to set
	 */
	public void setIsCusVendorService(boolean isCusVendorService) {
		this.isCusVendorService = isCusVendorService;
	}
}
