package com.cybersoft4u.xian.iatoms.common.util.workflow.helper;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.rest.service.api.runtime.task.TaskQueryRequest;
import org.springframework.util.CollectionUtils;

import cafe.core.bean.INameValue;
import cafe.core.bean.identity.LogonUser;
import cafe.core.util.StringUtils;
import cafe.workflow.bean.bpmn20.activiti.rest.QueryVariable;
import cafe.workflow.context.bpmn20.activiti.ActivitiWorkflowContext;
import cafe.workflow.util.helper.bpmn20.activiti.ActivitiRestTaskPredicateFilterHelper;

import com.cybersoft4u.xian.iatoms.common.bean.workflow.IAtomsTaskContext;
import com.cybersoft4u.xian.iatoms.common.bean.workflow.IAtomsTaskConstants;

/**
 * Purpose:案件查詢過濾輔助器
 * @author Allenchen
 * @since  JDK 1.7
 * @date   2016/3/30
 * @MaintenancePersonnel Allenchen
 */
public class IAtomsTaskPredicateFilterHelper extends ActivitiRestTaskPredicateFilterHelper {

	/**
	 * VacationTaskPredicateFilterHelper建構子
	 */
	public IAtomsTaskPredicateFilterHelper() {
		
	}
	@SuppressWarnings({ "rawtypes", "unused" })
	public TaskQueryRequest build(ActivitiWorkflowContext wfCtx, List<INameValue> filters, Map<String, String> ignoreFilters)  throws Exception {
		TaskQueryRequest predicate = super.build(wfCtx, filters, ignoreFilters);
		if (!CollectionUtils.isEmpty(filters)) {
			Iterator<INameValue> filterIter = filters.iterator();
			LogonUser user = null;
			String userId = null;
			if (wfCtx != null) {
				user = wfCtx.getLogonUser();
				userId = (StringUtils.hasText(wfCtx.getUserId())) ? wfCtx.getUserId() : null;
			}
			INameValue filterItem = null;
		    while (filterIter.hasNext()) {
		    	filterItem = filterIter.next();
		    	String filterName = filterItem.getName();
		    	if (ignoreFilters != null && ignoreFilters.containsKey(filterName)) continue;
		    	Object filterValue = filterItem.getValue();
		    	if (filterName.equals(IAtomsTaskConstants.FILTER_EMPLOYEE_NAME)) {
		    		String employeeName = (String)filterValue;
		    		if (StringUtils.hasText(employeeName)) {
		    			addVariables(predicate, IAtomsTaskContext.FIELD_EMPLOYEE_NAME, QueryVariable.QueryVariableOperation.EQUALS.getFriendlyName(), employeeName);
		    		}
		    	}
		    }
		}
		return predicate;
	}	
}
