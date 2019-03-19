package com.cybersoft4u.xian.iatoms.common.bean.workflow;

/**
 * Purpose: 案件處理payload設定
 * @author evanliu
 * @since  JDK 1.6
 * @date   2017年3月10日
 * @MaintenancePersonnel evanliu
 */
public class CaseProcessPayloadContext extends AbstractIAtomsPayloadContext {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1158546542605261564L;
/*	*//**
	 * 案件編號
	 */
	private String caseId;
	/**
	 * 案件類別
	 *//*
	private String caseCategory;
	*//**
	 * 建案時間
	 *//*
	private String createdDate;
	*//**
	 * 建案人員姓名
	 *//*
	private String createdUserName;
	*//**
	 * 案件當前指定人
	 */
	private String currentAssigner;
	/**
	 * 案件編號
	 */
	private String caseCategory;
	/**
	 * 客戶編號
	 */
	private String customerId;
	/**
	 * 下一關接受群組
	 */
	private String candidateGroup;
	/**
	 * 執行人員
	 */
	private String dispatchUser;
	/**
	 * 輸出參數
	 */
	private String outcome;
	/**
	 * Constructor:無參構造函數
	 */
	public CaseProcessPayloadContext() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Constructor:带参构造函数
	 * @param caseCategory:案件編號
	 * @param customerId:客戶編號
	 */
	public CaseProcessPayloadContext(String caseCategory, String customerId, String candidateGroup) {
		super();
		this.caseCategory = caseCategory;
		this.customerId = customerId;
		this.candidateGroup = candidateGroup;
	}
	/**
	 * @return the caseId
	 */
	public String getCaseId() {
		return caseId;
	}
	/**
	 * @param caseId the caseId to set
	 */
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	/**
	 * @return the caseCategory
	 */
	public String getCaseCategory() {
		return caseCategory;
	}
	/**
	 * @param caseCategory the caseCategory to set
	 */
	public void setCaseCategory(String caseCategory) {
		this.caseCategory = caseCategory;
	}
	
	
	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	/**
	 * @return the candidateGroup
	 */
	public String getCandidateGroup() {
		return candidateGroup;
	}
	/**
	 * @param candidateGroup the candidateGroup to set
	 */
	public void setCandidateGroup(String candidateGroup) {
		this.candidateGroup = candidateGroup;
	}
	/**
	 * @return the dispatchUser
	 */
	public String getDispatchUser() {
		return dispatchUser;
	}
	/**
	 * @param dispatchUser the dispatchUser to set
	 */
	public void setDispatchUser(String dispatchUser) {
		this.dispatchUser = dispatchUser;
	}
	
	/**
	 * @return the createdDate
	 *//*
	public String getCreatedDate() {
		return createdDate;
	}
	*//**
	 * @param createdDate the createdDate to set
	 *//*
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	*//**
	 * @return the createdUserName
	 *//*
	public String getCreatedUserName() {
		return createdUserName;
	}
	*//**
	 * @param createdUserName the createdUserName to set
	 *//*
	public void setCreatedUserName(String createdUserName) {
		this.createdUserName = createdUserName;
	}
	*//**
	 * @return the currentAssigner
	 */
	public String getCurrentAssigner() {
		return currentAssigner;
	}
	/**
	 * @param currentAssigner the currentAssigner to set
	 */
	public void setCurrentAssigner(String currentAssigner) {
		this.currentAssigner = currentAssigner;
	}
	/**
	 * @return the outcome
	 */
	public String getOutcome() {
		return outcome;
	}
	/**
	 * @param outcome the outcome to set
	 */
	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}
	
}
