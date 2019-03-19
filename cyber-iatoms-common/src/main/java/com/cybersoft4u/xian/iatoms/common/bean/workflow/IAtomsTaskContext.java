package com.cybersoft4u.xian.iatoms.common.bean.workflow;

import cafe.workflow.context.bpmn20.activiti.ActivitiTaskContext;
/**
 * 
 * Purpose: 关卡定义Context参数
 * @author Allenchen	
 * @since  JDK 1.7
 * @date   2016/3/30
 * @MaintenancePersonnel Allenchen
 */
public class IAtomsTaskContext extends ActivitiTaskContext<AbstractIAtomsPayloadContext> {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -8355415183506742278L;
	public static final String FIELD_EMPLOYEE_NAME = "employeeName";
	private String employeeName;
	/**
	 * 项目ID
	 */
	private String projectId;
	public IAtomsTaskContext() {
		super();
	}
	/**
	 * @return the employeeName
	 */
	public String getEmployeeName() {
		return employeeName;
	}
	/**
	 * @param employeeName the employeeName to set
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
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
