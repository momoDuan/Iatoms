package com.cybersoft4u.xian.iatoms.services;

import java.util.List;

import com.cybersoft4u.xian.iatoms.common.bean.dto.IAtomsCaseFormDTO;

import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;
import cafe.workflow.bean.ITask;

/**
 * Purpose: IATOMS - Activiti公共Service
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2017/3/22
 * @MaintenancePersonnel ElvaHe
 */
public interface IIAtomsActivitiUtilsService extends IAtomicService {

	/**
	 * Purpose:Service从Activiti上面取当前workList案件
	 * @author ElvaHe
	 * @param formDTO:IAtomsCaseFormDTO
	 * @return
	 * @throws ServiceException:出现异常时判处
	 * @return List<ITask>:返回任务列表
	 */
	public List<ITask> getTaskList(IAtomsCaseFormDTO formDTO) throws ServiceException;
}
