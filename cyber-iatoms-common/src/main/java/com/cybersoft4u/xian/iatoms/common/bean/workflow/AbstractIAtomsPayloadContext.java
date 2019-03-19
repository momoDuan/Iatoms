package com.cybersoft4u.xian.iatoms.common.bean.workflow;

import cafe.workflow.context.bpmn20.activiti.ActivitiPayloadContext;

/**
 * Purpose:案件关卡共用Payload设定
 * @author Allenchen
 * @since  JDK 1.7
 * @date   2016/3/30
 * @MaintenancePersonnel Allenchen
 */
public abstract class AbstractIAtomsPayloadContext extends ActivitiPayloadContext {
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 969072974756537954L;
	/**
	 * 收件人Email
	 */
	private String toEmail;
	/**
	 * 发送人Email
	 */
	private String fromEmail;
	/**
	 * 抄送Email（多个Email依据“,”隔开）
	 */
	private String ccEmail;
	/**
	 * 收件人名称
	 */
	private String toName;
	/**
	 * 发件人名称
	 */
	private String fromName;
	/**
	 * 退件理由
	 */
	private String rejectReason;
	/**
	 * 输出参数
	 */
	private String outcome;
	
	private String candidateUsers;
	/**
	 * Constructor:AbstractCmsPayloadContext建构子
	 */
	public AbstractIAtomsPayloadContext() {
	}

	/**
	 * Constructor:AbstractCmsPayloadContext建构子
	 */
	public AbstractIAtomsPayloadContext(String toMail, String fromEmail,
			String ccMail, String toName, String fromName, String rejectReason,
			String outcome,String candidateUsers) {
		super();
		this.toEmail = toMail;
		this.fromEmail = fromEmail;
		this.ccEmail = ccMail;
		this.toName = toName;
		this.fromName = fromName;
		this.rejectReason = rejectReason;
		this.outcome = outcome;
		this.candidateUsers = candidateUsers;
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
	 * @return the candidateUsers
	 */
	public String getCandidateUsers() {
		return candidateUsers;
	}

	/**
	 * @param candidateUsers the candidateUsers to set
	 */
	public void setCandidateUsers(String candidateUsers) {
		this.candidateUsers = candidateUsers;
	}

}
