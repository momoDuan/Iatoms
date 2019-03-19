package com.cybersoft4u.xian.iatoms.services.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cafe.core.service.ServiceException;
import cafe.workflow.bean.ITask;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ActivitiDataDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ActivitiResultDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO;

/**
 * Purpose: 解析Activiti REST 服务器回传TaskDTO数据
 * @author riverjin
 * @since  JDK 1.6
 * @date   2015/10/16
 * @MaintenancePersonnel riverjin
 */
public class AnalyticalActivitiTaskDataUtils{
	private static final Log log = LogFactory.getLog(AnalyticalActivitiTaskDataUtils.class);
	
	/**
	 * Purpose:解析出CaseID + EcexuteID
	 * @author riverjin
	 * @return
	 * @return List<ActivitiDataDTO>
	 */
	public static ActivitiResultDTO analytical(List<ITask> tasks)  throws ServiceException{
		ActivitiResultDTO activitiResultDTO = null;
		List<ActivitiDataDTO> ActivitiDataDTOs = null;
		try {
			if(CollectionUtils.isEmpty(tasks)) throw new ServiceException();
			else{
				//回传DTO
				activitiResultDTO = new ActivitiResultDTO();
				ActivitiDataDTOs = new ArrayList<ActivitiDataDTO>();
				StringBuffer CaseIdBuffer = new StringBuffer();
				String caseIds = "";
				for (ITask task : tasks) {
					if(StringUtils.hasText(task.getCaseId())){
						CaseIdBuffer.append(IAtomsConstants.MARK_QUOTES + task.getCaseId() + IAtomsConstants.MARK_QUOTES + IAtomsConstants.MARK_SEPARATOR);
					}
					ActivitiDataDTO activitiDataDTO = new ActivitiDataDTO();
					activitiDataDTO.setCaseId(task.getCaseId());
					activitiDataDTO.setTaskId(task.getTaskId());
					ActivitiDataDTOs.add(activitiDataDTO);
				}
				caseIds = CaseIdBuffer.toString();
				if(StringUtils.hasText(caseIds)){
					caseIds = caseIds.substring(0, caseIds.length()-1);
				}
				activitiResultDTO.setCaseIds(caseIds);
				activitiResultDTO.setActivitiDataDTOList(ActivitiDataDTOs);
			}
		} catch (Exception e) {
			//暂时打印,解除Bug用
			e.printStackTrace();
			log.error("AnalyticalActivitiTaskDataUtils.analytical() is Error,ErrorInfo:" + e);
			throw new ServiceException(e);
		}
		return activitiResultDTO;
	}
	public static Map<String, ActivitiResultDTO> analytical2Map(List<ITask> tasks)  throws ServiceException{
		Map<String, ActivitiResultDTO> returnMap = new HashMap<String, ActivitiResultDTO>();
		try {
			if(CollectionUtils.isEmpty(tasks)) throw new ServiceException();
			else{
				//任务案件
				String taskCaseIds = "";
				ActivitiResultDTO taskActivitiResultDTO = new ActivitiResultDTO();
				List<ActivitiDataDTO> taskActivitiDataDTOs = new ArrayList<ActivitiDataDTO>();
				for (ITask task : tasks) {
					if (StringUtils.hasText(task.getCaseId())) {
						/*if (task.getCaseId().indexOf(CaseManagerFormDTO.SICK_CASE_TYPE) != -1) {//为假单案件
							//拼接假单caseId
							if (!StringUtils.hasText(taskCaseIds)) {
								taskCaseIds = IAtomsConstants.MARK_QUOTES + task.getCaseId() + IAtomsConstants.MARK_QUOTES;
							}else {
								taskCaseIds += IAtomsConstants.MARK_SEPARATOR + IAtomsConstants.MARK_QUOTES + task.getCaseId() + IAtomsConstants.MARK_QUOTES;
							}
							
							
						}*/
						ActivitiDataDTO activitiDataDTO = new ActivitiDataDTO();
						activitiDataDTO.setCaseId(task.getCaseId());
						activitiDataDTO.setTaskId(task.getTaskId());
						taskActivitiDataDTOs.add(activitiDataDTO);
					}
				}
				//将任务案件加入map
				taskActivitiResultDTO.setCaseIds(taskCaseIds);
				taskActivitiResultDTO.setActivitiDataDTOList(taskActivitiDataDTOs);
				returnMap.put(CaseManagerFormDTO.SICK_CASE_TYPE, taskActivitiResultDTO);
			}
		} catch (Exception e) {
			log.error("AnalyticalActivitiTaskDataUtils.analytical2Map() is Error,ErrorInfo:" + e);
			throw new ServiceException(e);
		}
		return returnMap;
	}
	
}
