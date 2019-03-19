package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.util.Map;

import cafe.workflow.bean.HumanTaskAssignee;

import cafe.workflow.bean.dto.CaseFormDTO;
import cafe.workflow.bean.identity.WfLogonUser;
import cafe.workflow.context.ITaskInquiryContext;
import cafe.workflow.context.IWorkflowContext;
import cafe.workflow.context.bpmn20.activiti.ActivitiWorkflowContext;

import com.cybersoft4u.xian.iatoms.common.bean.workflow.IAtomsTaskContext;

/**
 * Purpose:案件流程FormDTO
 * @author RiverJin
 * @since  JDK 1.6
 * @date   2015/09/09
 * @MaintenancePersonnel RiverJin
 */
public class IAtomsCaseFormDTO extends CaseFormDTO<CaseDetailDTO, ActivitiWorkflowContext, IAtomsTaskContext> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2257826294144605725L;
	/**
	 * Task Id.
	 */
	private String taskId;
	/**
	 * 收件人Email
	 */
	private String toEmail;
	/**
	 * 收件人名称
	 */
	private String toName;
	/**
	 * 发件人Email
	 */
	private String fromEmail;
	/**
	 * 发件人名称
	 */
	private String fromName;
	/**
	 * 抄送Email
	 */
	private String ccEmail;
	/**
	 * 抄送Email
	 */
	private String timeCard;
	/**
	 * 送关卡输出参数
	 */
	private String outcome;
	/**
	 * 退件理由
	 */
	private String rejectReason;
	/**
	 * 案件Id
	 */
	private String projectId;
	/**
	 * 下一关指派人ID
	 */
	private String nextAssignerId;
	/**
	 * 下一关指派人名称
	 */
	private String nextAssignerName;
	/**
	 * 指派人
	 */
	private String assignee;
	/**
	 * workflow
	 */
	private IWorkflowContext wfCtx;
	
	/**
	 * 任务信息
	 */
	private SrmCaseHandleInfoDTO caseHandleInfoDTO;
	/**
	 * 案件当前所在关卡Code
	 */
	private String currentActivitiCode;
	/**
	 * 代理人
	 */
	private String currentAssigneeUsers;//代理人
	/**
	 * 下一關審核人
	 */
	private String currentAssigner;//下一關審核人
	/**
	 * 下一關審核人名
	 */
	private String currentAssignerName;//下一關審核人名
	/**
	 * 下一關指派群組
	 */
	private String candidateGroup;
	/**
	 * 執行人員
	 */
	private String dispatchUser;
	/**
	 * 處理說明
	 */
	private String description;
	private ITaskInquiryContext taskQueryCtx;
	private Map<String, String> ignoreFilters;
	private ActivitiResultDTO activitiResultDTO;
	
	/**
	 * Constructor:CmsFormDTO构造函数
	 */
	public IAtomsCaseFormDTO() {
		super(new CaseDetailDTO());
	}
	/**
	 * Constructor:CmsFormDTO带参构造函数
	 * @param caseDetail:案件明细DTO
	 */
	public IAtomsCaseFormDTO(CaseDetailDTO caseDetail) {
		super(caseDetail);
	}

	/**
	 * Constructor:CmsFormDTO带参构造函数
	 * @param caseId:案件编号
	 * @param actionAliasCode:Action别名
	 * @param status:案件状态
	 * @param substatus:案件子状态
	 * @param assignee:案件处理人员
	 * @param logonUser:当前登入者
	 */
	public IAtomsCaseFormDTO(String caseId, String actionAliasCode,
			String status, String substatus, HumanTaskAssignee assignee,
			WfLogonUser logonUser) {
		super(caseId, actionAliasCode, status, substatus, assignee, logonUser);
	}
	
	/**
	 * Constructor:带参构造函数
	 */
	public IAtomsCaseFormDTO(String toEmail, String toName, String fromEmail,
			String fromName, String ccEmail, String outcome, String rejectReason) {
		super();
		this.toEmail = toEmail;
		this.toName = toName;
		this.fromEmail = fromEmail;
		this.fromName = fromName;
		this.ccEmail = ccEmail;
		this.outcome = outcome;
		this.rejectReason = rejectReason;
	}
	/**
	 * @return the toEmail
	 */
	public String getToEmail() {
		return toEmail;
	}
	/**
	 * @param toEmail the toEmail to set
	 */
	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}
	/**
	 * @return the toName
	 */
	public String getToName() {
		return toName;
	}
	/**
	 * @param toName the toName to set
	 */
	public void setToName(String toName) {
		this.toName = toName;
	}
	/**
	 * @return the fromEmail
	 */
	public String getFromEmail() {
		return fromEmail;
	}
	/**
	 * @param fromEmail the fromEmail to set
	 */
	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}
	/**
	 * @return the fromName
	 */
	public String getFromName() {
		return fromName;
	}
	/**
	 * @param fromName the fromName to set
	 */
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	/**
	 * @return the ccEmail
	 */
	public String getCcEmail() {
		return ccEmail;
	}
	/**
	 * @param ccEmail the ccEmail to set
	 */
	public void setCcEmail(String ccEmail) {
		this.ccEmail = ccEmail;
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
	/**
	 * @return the rejectReason
	 */
	public String getRejectReason() {
		return rejectReason;
	}
	/**
	 * @param rejectReason the rejectReason to set
	 */
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
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
	/**
	 * @return the nextAssignerId
	 */
	public String getNextAssignerId() {
		return nextAssignerId;
	}
	/**
	 * @param nextAssignerId the nextAssignerId to set
	 */
	public void setNextAssignerId(String nextAssignerId) {
		this.nextAssignerId = nextAssignerId;
	}
	/**
	 * @return the nextAssignerName
	 */
	public String getNextAssignerName() {
		return nextAssignerName;
	}
	/**
	 * @param nextAssignerName the nextAssignerName to set
	 */
	public void setNextAssignerName(String nextAssignerName) {
		this.nextAssignerName = nextAssignerName;
	}
	/**
	 * @return the assignee
	 */
	public String getAssignee() {
		return assignee;
	}
	/**
	 * @param assignee the assignee to set
	 */
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	/**
	 * @return the currentActivitiCode
	 */
	public String getCurrentActivitiCode() {
		return currentActivitiCode;
	}
	/**
	 * @param currentActivitiCode the currentActivitiCode to set
	 */
	public void setCurrentActivitiCode(String currentActivitiCode) {
		this.currentActivitiCode = currentActivitiCode;
	}
	/**
	 * @return the currentAssigneeUsers
	 */
	public String getCurrentAssigneeUsers() {
		return currentAssigneeUsers;
	}
	/**
	 * @param currentAssigneeUsers the currentAssigneeUsers to set
	 */
	public void setCurrentAssigneeUsers(String currentAssigneeUsers) {
		this.currentAssigneeUsers = currentAssigneeUsers;
	}
	/**
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
	 * @return the currentAssignerName
	 */
	public String getCurrentAssignerName() {
		return currentAssignerName;
	}
	/**
	 * @param currentAssignerName the currentAssignerName to set
	 */
	public void setCurrentAssignerName(String currentAssignerName) {
		this.currentAssignerName = currentAssignerName;
	}
	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}
	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	/**
	 * @return the wfCtx
	 */
	public IWorkflowContext getWfCtx() {
		return wfCtx;
	}
	/**
	 * @param wfCtx the wfCtx to set
	 */
	public void setWfCtx(IWorkflowContext wfCtx) {
		this.wfCtx = wfCtx;
	}
	/**
	 * @return the timeCard
	 */
	public String getTimeCard() {
		return timeCard;
	}
	/**
	 * @param timeCard the timeCard to set
	 */
	public void setTimeCard(String timeCard) {
		this.timeCard = timeCard;
	}
	/**
	 * @return the caseHandleInfoDTO
	 */
	public SrmCaseHandleInfoDTO getCaseHandleInfoDTO() {
		return caseHandleInfoDTO;
	}
	/**
	 * @param caseHandleInfoDTO the caseHandleInfoDTO to set
	 */
	public void setCaseHandleInfoDTO(SrmCaseHandleInfoDTO caseHandleInfoDTO) {
		this.caseHandleInfoDTO = caseHandleInfoDTO;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the taskQueryCtx
	 */
	public ITaskInquiryContext getTaskQueryCtx() {
		return taskQueryCtx;
	}
	/**
	 * @param taskQueryCtx the taskQueryCtx to set
	 */
	public void setTaskQueryCtx(ITaskInquiryContext taskQueryCtx) {
		this.taskQueryCtx = taskQueryCtx;
	}
	/**
	 * @return the ignoreFilters
	 */
	public Map<String, String> getIgnoreFilters() {
		return ignoreFilters;
	}
	/**
	 * @param ignoreFilters the ignoreFilters to set
	 */
	public void setIgnoreFilters(Map<String, String> ignoreFilters) {
		this.ignoreFilters = ignoreFilters;
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
	 * @return the activitiResultDTO
	 */
	public ActivitiResultDTO getActivitiResultDTO() {
		return activitiResultDTO;
	}
	/**
	 * @param activitiResultDTO the activitiResultDTO to set
	 */
	public void setActivitiResultDTO(ActivitiResultDTO activitiResultDTO) {
		this.activitiResultDTO = activitiResultDTO;
	}
	
}
