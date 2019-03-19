package com.cybersoft4u.xian.iatoms.common.util.workflow.activiti.convert;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.rest.service.api.engine.RestIdentityLink;
import org.apache.commons.collections.KeyValue;
import org.springframework.util.CollectionUtils;

import cafe.core.util.StringUtils;
import cafe.workflow.bean.CafeTask;
import cafe.workflow.bean.HumanTaskAssignee;
import cafe.workflow.context.ITaskContext;
import cafe.workflow.context.IWorkflowContext;
import cafe.workflow.util.bpmn20.activiti.RestUtils;

/**
 * Purpose: Activiti API原始任務物件的包裝具象類別。 
 * @author evanliu
 * @since  JDK 1.6
 * @date   2017年4月6日
 * @MaintenancePersonnel evanliu
 */
public class ActivitiServiceTask extends CafeTask<TaskResponse, Map<String, Object>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1235190204341745915L;
	private ProcessInstance processInstance;
	private List<RestIdentityLink> identityLinks;
	/**
	 * ActivitiServiceTask建構子
	 * @param workflowContext
	 * @param task
	 */
	public ActivitiServiceTask(IWorkflowContext workflowContext, TaskResponse task) {
		super(workflowContext, task);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * ActivitiServiceTask建構子
	 * @param workflowContext
	 * @param task
	 */
	public ActivitiServiceTask(IWorkflowContext workflowContext, TaskResponse task, ProcessInstance processInstance) {
		super(workflowContext, task);
		this.processInstance = processInstance;
		// TODO Auto-generated constructor stub
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.bean.ITask#getPayload()
	 */
	@Override
	public Map<String, Object> getPayload() {
		if (this.getTask() == null) return null;
		return this.getTask().getVariables();
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.bean.ITask#setPayload(java.lang.Object)
	 */
	@Override
	public void setPayload(Map<String, Object> payload) {
		if (this.getTask() == null) return;
		this.getTask().setVariables(payload);
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.bean.ITask#getTaskId()
	 */
	@Override
	public String getTaskId() {
		if (this.getTask() == null) return null;
		return this.getTask().getId();
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.bean.ITask#getProcessId()
	 */
	@Override
	public String getProcessId() {
		if (this.getTask() == null) return null;
		String processDefinitionId = this.getTask().getProcessDefinitionId();
		String processId = RestUtils.getProcessId(processDefinitionId);
		return processId;
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.bean.ITask#getProcessName()
	 */
	@Override
	public String getProcessName() {
		return this.getProcessId();
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.bean.ITask#getActivityCode()
	 */
	@Override
	public String getActivityCode() {
		if (this.getTask() == null) return null;
		return this.getTask().getTaskDefinitionKey();
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.bean.ITask#getActivityName()
	 */
	@Override
	public String getActivityName() {
		if (this.getTask() == null) return null;
		return this.getTask().getName();
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.bean.ITask#getPriority()
	 */
	@Override
	public int getPriority() {
		if (this.getTask() == null) return 0;
		return this.getTask().getPriority();
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.bean.ITask#getCaseId()
	 */
	@Override
	public String getCaseId() {
		String caseId = (this.processInstance == null) ? null : this.processInstance.getBusinessKey();
		if (!StringUtils.hasText(caseId)) {
			if (this.getPayload() == null) return null;
			caseId = (String)this.getPayloadElementValue(ITaskContext.FIELD_CASE_ID);
		}
		return caseId;
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.bean.ITask#getCreatedDate()
	 */
	@Override
	public Date getCreatedDate() {
		if (this.getTask() == null) return null;
		return this.getTask().getCreateTime();
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.bean.ITask#isGroup()
	 */
	@Override
	public boolean isGroup() {
		if (CollectionUtils.isEmpty(identityLinks)) return false;
		for(RestIdentityLink link : identityLinks) {
			if (link.getType().equals(IdentityLinkType.CANDIDATE)) return true;
		}//end of for
		return false;
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.bean.ITask#getAcquiredBy()
	 */
	@Override
	public String getAcquiredBy() {
		if (this.getTask() == null) return null;
		if (this.isGroup() && StringUtils.hasText(this.getTask().getAssignee())) return this.getTask().getAssignee();
		return null;
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.bean.ITask#getRealm()
	 */
	@Override
	public String getRealm() {
		if (this.getPayload() == null) return null;
		String realm = (String)this.getPayloadElementValue(ITaskContext.FIELD_REALM);
		return realm;
	}
	
	public HumanTaskAssignee getAssignee() {
		if (CollectionUtils.isEmpty(identityLinks)) return null;
		HumanTaskAssignee assignee = new HumanTaskAssignee();
		for(RestIdentityLink link : identityLinks) {
			if (StringUtils.hasText(link.getGroup())) {
				if (link.getType().equals(IdentityLinkType.CANDIDATE)) {
					assignee.addAssigneeGroup(link.getGroup());
				}
			} else if (StringUtils.hasText(link.getUser())) {
				if (link.getType().equals(IdentityLinkType.CANDIDATE) || link.getType().equals(IdentityLinkType.ASSIGNEE)) {
					assignee.addAssigneeUser(link.getUser());
				}
			}
		}//end of for
		return assignee;
	}
	public boolean containPayloadElement(String name) {
		return this.getPayload().containsKey(name);
		//return RestUtils.contain(this.getPayload(), name);
	}
	/**
	 * 以變數名稱取得某個Payload 元素。
	 * @param name 變數名稱 
	 * @return
	 */
	public Object getPayloadElement(String name) {
		return this.getPayload().get(name);
		//return RestUtils.getVariable(this.getPayload(), name);
	}
	public Object getPayloadElementValue(String name) {
		Object object = this.getPayloadElement(name);
		return (object == null) ? null : object.toString();
	}
	
	public void setPayloadElement(KeyValue variable) {
		if (variable == null) return;
		if (this.getPayload() == null) this.setPayload(new HashMap<String,Object>());
		
		//List<RestVariable> variables = this.getPayload();
		Map<String, Object> variables = this.getPayload();

		boolean isFound = containPayloadElement(variable.getKey().toString());
		if (!isFound) variables.put(variable.getKey().toString(),variable.getValue());
	}

	/**
	 * @return the processInstance
	 */
	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	/**
	 * @param processInstance the processInstance to set
	 */
	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

	/**
	 * @return the identityLinks
	 */
	public List<RestIdentityLink> getIdentityLinks() {
		return identityLinks;
	}

	/**
	 * @param identityLinks the identityLinks to set
	 */
	public void setIdentityLinks(List<RestIdentityLink> identityLinks) {
		this.identityLinks = identityLinks;
	}

}
