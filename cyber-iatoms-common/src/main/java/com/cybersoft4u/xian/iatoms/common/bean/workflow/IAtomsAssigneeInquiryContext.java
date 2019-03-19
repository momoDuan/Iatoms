package com.cybersoft4u.xian.iatoms.common.bean.workflow;

import java.util.Date;
import java.util.List;

import cafe.core.bean.identity.LogonUser;
import cafe.core.bean.identity.RoleParticipant;
import cafe.workflow.context.AssigneeInquiryContext;

/**
 * 
 * Purpose: 
 * @author Allenchen
 * @since  JDK 1.7
 * @date   2016/3/30
 * @MaintenancePersonnel Allenchen
 */
public class IAtomsAssigneeInquiryContext extends AssigneeInquiryContext {
	private static final long serialVersionUID = 4548993314998627955L;
	/**
	 * 案件業務區
	 */
	private String businessArea;
	/**
	 * 業務人員Id
	 */
	private String salesId;

	/**
	 * 自動指派流程編號
	 */
	private String autoAssignFlowId;
	/**
	 * 關卡類型
	 */
	private String activityType;
	/**
	 * 项目ID
	 */
	private String projectId;
	/**
	 * CcapsAssigneeInquiryContext建構子
	 */
	public IAtomsAssigneeInquiryContext() {
	}

	/**
	 * CcapsAssigneeInquiryContext建構子
	 * @param logonUser
	 * @param nextActivityCode
	 */
	public IAtomsAssigneeInquiryContext(LogonUser logonUser, String nextActivityCode) {
		super(logonUser, nextActivityCode);
	}

	/**
	 * CcapsAssigneeInquiryContext建構子
	 * @param logonUser
	 * @param nextActivityCode
	 * @param caseInitiateDate
	 */
	public IAtomsAssigneeInquiryContext(LogonUser logonUser, String nextActivityCode, Date caseInitiateDate) {
		super(logonUser, nextActivityCode, caseInitiateDate);
	}

	/**
	 * CcapsAssigneeInquiryContext建構子
	 * @param logonUser
	 * @param nextActivityCode
	 * @param candidateRoles
	 */
	public IAtomsAssigneeInquiryContext(LogonUser logonUser, String nextActivityCode, List<RoleParticipant> candidateRoles) {
		super(logonUser, nextActivityCode, candidateRoles);
	}

	/**
	 * CcapsAssigneeInquiryContext建構子
	 * @param logonUser
	 * @param nextActivityCode
	 * @param caseInitiateDate
	 * @param candidateRoles
	 */
	public IAtomsAssigneeInquiryContext(LogonUser logonUser, String nextActivityCode, Date caseInitiateDate, List<RoleParticipant> candidateRoles) {
		super(logonUser, nextActivityCode, caseInitiateDate, candidateRoles);
	}

	public String getBusinessArea() {
		return businessArea;
	}

	public void setBusinessArea(String businessArea) {
		this.businessArea = businessArea;
	}

	public String getSalesId() {
		return salesId;
	}

	public void setSalesId(String salesId) {
		this.salesId = salesId;
	}

	public String getAutoAssignFlowId() {
		return autoAssignFlowId;
	}

	public void setAutoAssignFlowId(String autoAssignFlowId) {
		this.autoAssignFlowId = autoAssignFlowId;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	/**
	 * @return the projectId
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

}
