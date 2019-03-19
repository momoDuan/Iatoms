package com.cybersoft4u.xian.iatoms.common.util.workflow.activiti.convert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import cafe.core.bean.mappingtable.MappingFieldSet;
import cafe.core.util.BeanUtils;
import cafe.core.util.convert.ConvertException;
import cafe.workflow.bean.HumanTaskAssignee;
import cafe.workflow.context.IPayloadContext;
import cafe.workflow.context.bpmn20.activiti.ActivitiTaskContext;
import cafe.workflow.context.bpmn20.activiti.convert.RestVariableMappingField;
import cafe.workflow.context.convert.IPayloadTranslator;
import cafe.workflow.context.convert.ITaskContextConverter;

/**
 * Purpose: Activiti BPM 任務內文轉換器類別。將ActivitiRestTask實際所取得的Service 任務資料轉換成自訂的任務內文物件。
 * @author evanliu
 * @since  JDK 1.6
 * @date   2017年4月6日
 * @MaintenancePersonnel evanliu
 */
public class ActivitiServiceTaskContextConverter <T extends ActivitiTaskContext> implements ITaskContextConverter<ActivitiServiceTask, T>, InitializingBean {

	/**
	 * 日誌組件
	 */
	private static final Log log = LogFactory.getLog(ActivitiServiceTaskContextConverter.class);
	/**
	 * payloadTranslator
	 */
	private IPayloadTranslator<ActivitiServiceTask, IPayloadContext> payloadTranslator;
	/**
	 * taskAttributeMappingFieldSet
	 */
	private MappingFieldSet<RestVariableMappingField> taskAttributeMappingFieldSet;
	/**
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		Assert.notNull(this.taskAttributeMappingFieldSet, "Property 'taskAttributeMappingFieldSet' is required");
        Assert.notNull(this.taskAttributeMappingFieldSet.getTarget(), "Property 'target' is required");
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.context.convert.ITaskContextConverter#convert(cafe.workflow.bean.ITask)
	 */
	@Override
	public T convert(ActivitiServiceTask task) throws Exception {
		// 把Task的信息轉換成對應的payloadContext
		if (task == null || task.getTask() == null) return null;
		
		T taskContext = null;
		try {
			// 從*common-context*.xml中取的對應的payloadContext
			taskContext = this.newTaskContextInstance();
			
			if (taskContext == null) throw new Exception("The TaskContext is null !");
			taskContext.setProcessId(task.getProcessId());
			taskContext.setProcessName(task.getProcessName());
			taskContext.setCaseId(task.getCaseId());
			taskContext.setAcquiredBy(task.getAcquiredBy());
			TaskResponse taskResponse = task.getTask();
			if (taskResponse != null) {
				taskContext.setActivityCode(task.getActivityCode());
				taskContext.setActivityName(task.getActivityName());
				taskContext.setOwnerUser(taskResponse.getOwner());
				taskContext.setPriority(task.getPriority());
				HumanTaskAssignee assignee = task.getAssignee();
				if (assignee != null) {
					taskContext.setAssigneeGroups(assignee.getAssigneeGroupIds());
					taskContext.setAssigneeUsers(assignee.getAssigneeUserIds());
				}
				taskContext.setCreatedDate(task.getCreatedDate());
				taskContext.setTaskId(task.getTaskId());
				taskContext.setIsGroup(task.isGroup());
				if (!CollectionUtils.isEmpty(taskResponse.getVariables()) && 
					this.taskAttributeMappingFieldSet != null && !this.taskAttributeMappingFieldSet.isEmpty()) {
					for(String key : taskResponse.getVariables().keySet()) {
						if (this.taskAttributeMappingFieldSet.containsKey(key)) {
							Object value = taskResponse.getVariables().get(key);
							try {
								BeanUtils.invokeSetter(taskContext, key, value);
							}catch(Exception e) {}
						}	
					}			
				}
			}	
			
			try {
				if (this.payloadTranslator != null && task.getPayload() != null) {
					IPayloadContext payload = this.payloadTranslator.unmarshal(task);
					taskContext.setPayload(payload);
				}
			}catch(Throwable ee) {}
			
		}catch(Throwable e) {
			log.debug("Convert task to TaskContext is failed:"+e,e);
			e.printStackTrace();
			throw new ConvertException(e);
		}
		return taskContext;
	}	
	
	/**
	 * 建立一個Activiti任務內文物件實例
	 * @return
	 */
	public T newTaskContextInstance() {
		T taskContext = null;
		try {
			if (this.taskAttributeMappingFieldSet != null && this.taskAttributeMappingFieldSet.getTarget() != null) {
				taskContext = (T)BeanUtils.newInstance(this.taskAttributeMappingFieldSet.getTarget());
			}
		}catch(Exception e){
			log.debug("can't new task context instance !");
		}
		return taskContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.context.convert.ITaskContextConverter#setPayloadTranslator(cafe.workflow.context.convert.IPayloadTranslator)
	 */
	@Override
	public void setPayloadTranslator(IPayloadTranslator payloadTranslator) {
		// TODO Auto-generated method stub
		this.payloadTranslator = payloadTranslator;
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.context.convert.ITaskContextConverter#getPayloadTranslator()
	 */
	@Override
	public IPayloadTranslator getPayloadTranslator() {
		return this.payloadTranslator;
	}

	/**
	 * @return the taskAttributeMappingFieldSet
	 */
	public MappingFieldSet<RestVariableMappingField> getTaskAttributeMappingFieldSet() {
		return taskAttributeMappingFieldSet;
	}

	/**
	 * @param taskAttributeMappingFieldSet the taskAttributeMappingFieldSet to set
	 */
	public void setTaskAttributeMappingFieldSet(MappingFieldSet<RestVariableMappingField> taskAttributeMappingFieldSet) {
		this.taskAttributeMappingFieldSet = taskAttributeMappingFieldSet;
	}

}
