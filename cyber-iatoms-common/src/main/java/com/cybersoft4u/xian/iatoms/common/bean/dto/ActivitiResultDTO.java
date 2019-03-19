package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.util.List;

/**
 * Purpose: Activiti回传值DTO
 * @author riverjin
 * @since  JDK 1.6
 * @date   2015/10/16
 * @MaintenancePersonnel riverjin
 */
public class ActivitiResultDTO {
	/**
	 * Activiti当次查询回传的所有CaseID
	 * 格式: SQL:  where case_id in("此处所需要的格式")
	 */
	private String caseIds;
	/**
	 * 案件信息DTO数据集
	 */
	private List<ActivitiDataDTO> activitiDataDTOList;
	/**
	 * @return the caseIds
	 */
	public String getCaseIds() {
		return caseIds;
	}
	/**
	 * @param caseIds the caseIds to set
	 */
	public void setCaseIds(String caseIds) {
		this.caseIds = caseIds;
	}
	/**
	 * @return the activitiDataDTOList
	 */
	public List<ActivitiDataDTO> getActivitiDataDTOList() {
		return activitiDataDTOList;
	}
	/**
	 * @param activitiDataDTOList the activitiDataDTOList to set
	 */
	public void setActivitiDataDTOList(List<ActivitiDataDTO> activitiDataDTOList) {
		this.activitiDataDTOList = activitiDataDTOList;
	}
}
