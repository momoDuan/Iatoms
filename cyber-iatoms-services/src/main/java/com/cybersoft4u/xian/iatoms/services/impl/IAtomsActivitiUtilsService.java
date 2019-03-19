package com.cybersoft4u.xian.iatoms.services.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.activiti.rest.service.api.runtime.task.TaskQueryRequest;
import org.springframework.util.CollectionUtils;

import cafe.core.bean.INameValue;
import cafe.core.bean.NameValue;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.BeanUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.workflow.bean.ITask;
import cafe.workflow.bean.TaskFilterField;
import cafe.workflow.context.ITaskInquiryContext;
import cafe.workflow.context.IWorkflowContext;
import cafe.workflow.util.helper.IHumanWorkflowServiceHelper;
import cafe.workflow.util.helper.ITaskPredicateFilterHelper;

import com.cybersoft4u.xian.iatoms.common.bean.dto.IAtomsCaseFormDTO;
import com.cybersoft4u.xian.iatoms.services.IIAtomsActivitiUtilsService;

/**
 * Purpose: IATOMS - Activiti公共Service
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2017/3/23
 * @MaintenancePersonnel ElvaHe
 */
public class IAtomsActivitiUtilsService extends AtomicService implements IIAtomsActivitiUtilsService {
	/**
	 *  系統日誌記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(IAtomsActivitiUtilsService.class);
	
	protected IHumanWorkflowServiceHelper humanWorkflowServiceHelper;
	protected ITaskPredicateFilterHelper taskPredicateFilterHelper;
	private List<TaskFilterField> predicateBuildings;
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.cms.services.ICmsActivitiUtilsService#getTaskList(com.cybersoft4u.xian.cms.common.bean.dto.WorkListFormDTO)
	 */
	public synchronized List<ITask> getTaskList(IAtomsCaseFormDTO formDTO) throws ServiceException {
		List<ITask> tasks = null;
		try {
			IWorkflowContext wfCtx = formDTO.getWfCtx();
			ITaskInquiryContext taskQueryCtx = formDTO.getTaskQueryCtx();
			List<INameValue> filters = new ArrayList<INameValue>();
    		if (formDTO != null && !CollectionUtils.isEmpty(this.predicateBuildings)) {
    			Iterator<TaskFilterField> fields = this.predicateBuildings.iterator();
    			TaskFilterField field = null;
    			while (fields.hasNext()) {
    				field = fields.next();
    				Object defaultValue = field.getDefaultValueObject();
    				Object value = null;
    				try {
    					value = BeanUtils.invokeGetter(formDTO, field.getValueObjectFieldName());
    				}catch(Exception e){}
    				if (value == null) value = defaultValue;
    				filters.add(new NameValue(field.getFilterName(), value));
    			}
    		}
    		TaskQueryRequest predicate = (TaskQueryRequest)this.taskPredicateFilterHelper.build(wfCtx, filters, formDTO.getIgnoreFilters());
    		//predicate.setCreatedAfter(DateTimeUtils.addCalendar(DateTimeUtils.getCurrentDate(), 0, 0, -15));
    		predicate.setCreatedAfter(null);
    		//创建时间结束
    		//predicate.setCreatedBefore(DateTimeUtils.getCurrentDate());
    		predicate.setCreatedBefore(null);
    		//下一关ActivitiCode
    		predicate.setTaskDefinitionKey(formDTO.getCurrentActivitiCode());
    		//predicate.setProcessInstanceBusinessKey(processInstanceBusinessKey)
    		//当前任务指派人
    		//predicate.setAssignee(formDTO.getAssignee());
    		//當前任務指派群組
    		predicate.setCandidateGroup(formDTO.getCandidateGroup());
    		predicate.setInvolvedUser(null);
    		predicate.setProcessDefinitionKey(formDTO.getProcessId());
    		taskQueryCtx.setPredicate(predicate);
//    		taskQueryCtx.setPageSize(1000);
//    		taskQueryCtx.setPredicate(this.taskPredicateFilterHelper.build(formDTO.getWorkflowContext(), filters, formDTO.getIgnoreFilters()));
    		
    		tasks= humanWorkflowServiceHelper.getTaskList(wfCtx, taskQueryCtx);
		} catch (Exception e) {
			LOGGER.error("getTaskList()", "is Error,ErrorInfo :", e);
			throw new ServiceException(e);
		}
		return tasks;
	}
	/**
	 * @return the humanWorkflowServiceHelper
	 */
	public IHumanWorkflowServiceHelper getHumanWorkflowServiceHelper() {
		return humanWorkflowServiceHelper;
	}
	/**
	 * @param humanWorkflowServiceHelper the humanWorkflowServiceHelper to set
	 */
	public void setHumanWorkflowServiceHelper(
			IHumanWorkflowServiceHelper humanWorkflowServiceHelper) {
		this.humanWorkflowServiceHelper = humanWorkflowServiceHelper;
	}
	/**
	 * @return the taskPredicateFilterHelper
	 */
	public ITaskPredicateFilterHelper getTaskPredicateFilterHelper() {
		return taskPredicateFilterHelper;
	}
	/**
	 * @param taskPredicateFilterHelper the taskPredicateFilterHelper to set
	 */
	public void setTaskPredicateFilterHelper(
			ITaskPredicateFilterHelper taskPredicateFilterHelper) {
		this.taskPredicateFilterHelper = taskPredicateFilterHelper;
	}
	/**
	 * @return the predicateBuildings
	 */
	public List<TaskFilterField> getPredicateBuildings() {
		return predicateBuildings;
	}
	/**
	 * @param predicateBuildings the predicateBuildings to set
	 */
	public void setPredicateBuildings(List<TaskFilterField> predicateBuildings) {
		this.predicateBuildings = predicateBuildings;
	}


}
