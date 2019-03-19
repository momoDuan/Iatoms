package com.cybersoft4u.xian.iatoms.common.bean.dto;
/**
 * Purpose: 解析Activiti数据库与系统数据库主要关系DTO
 * @author riverjin
 * @since  JDK 1.6
 * @date   2015/10/16
 * @MaintenancePersonnel riverjin
 */
public class ActivitiDataDTO {

	/**
	 * Constructor:空构造 
	 */
	public ActivitiDataDTO(){}
	/**
	 * Constructor:
	 */
	public ActivitiDataDTO(String caseId,String taskId){
		this.caseId = caseId;
		this.taskId = taskId;
	}
	
	/**
	 * 案件ID
	 */
	private String caseId;
	/**
	 * Activiti执行ID
	 */
	private String taskId;
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
	
}
