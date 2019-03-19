package com.cybersoft4u.xian.iatoms.common.util.workflow.activiti.convert;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import cafe.core.bean.mappingtable.MappingField;
import cafe.core.bean.mappingtable.MappingFieldSet;
import cafe.core.exception.TranslateException;
import cafe.core.util.BeanUtils;
import cafe.workflow.bean.ITask;
import cafe.workflow.context.bpmn20.activiti.ActivitiPayloadContext;
import cafe.workflow.context.bpmn20.activiti.convert.RestVariableMappingField;
import cafe.workflow.context.convert.AbstractPayloadTranslator;

/**
 * Purpose: The Activiti BPM payload Translator translates payload with variables.
 * @author evanliu
 * @since  JDK 1.6
 * @date   2017年4月6日
 * @MaintenancePersonnel evanliu
 */
public class ActivitiServicePayloadTranslator<P extends ActivitiPayloadContext> extends AbstractPayloadTranslator<Map<String, Object>, P> {
	private static final Log log = LogFactory.getLog(ActivitiServicePayloadTranslator.class);
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.context.convert.IPayloadTranslator#unmarshal(cafe.workflow.bean.ITask)
	 */
	@Override
	public P unmarshal(ITask task) throws TranslateException {
		if (task == null || task.getTask() == null) throw new TranslateException("Input object can't be null or not instance of Task object !");
		long currentTime = System.currentTimeMillis();
		String caseId = "";
		try {
			// 流程ID
			String processId = task.getProcessId();
			// 根據流程ID取得配置在XML中的PayloadContext
	    	P payloadContext = newPayloadContextInstance(processId);
	    	if (payloadContext == null) throw new TranslateException("Can't get a Payload Context Object instance for process "+processId+" !");
	        // 取得ActivitiRest服務查詢Task回傳的任務參數集
	    	Map<String, Object> payload = (Map<String, Object>)task.getPayload();
	    	//List<RestVariable> payload = (List<RestVariable>)task.getPayload();
	    	// 獲取在*common-context*.xml中根據流程ID配置的所傳參數Map集
	    	MappingFieldSet<RestVariableMappingField> mappingFieldSet = (MappingFieldSet<RestVariableMappingField>)this.getMappingFieldSet(processId);
	    	// 把ActivitiRest回傳的任務參數集放置到PayloadContext中(相當於把payloadSetter方法,只是一個參數Set過程)
	    	if (!CollectionUtils.isEmpty(payload) && mappingFieldSet != null && !mappingFieldSet.isEmpty()) {
	    		String sourceFieldName = null;
	    		Object value = null;
	    		MappingField mappingField = null;
	    		for(String key : payload.keySet()) {
	    			mappingField = mappingFieldSet.getMappingFieldBySource(key);
					if (mappingField != null) {
						value = payload.get(key);
						try {
							BeanUtils.invokeSetter(payloadContext, mappingField.getTargetFieldName(), value);
						}catch(Exception e) {}
					}	
	    		}
	    	}
	    	payloadContext.setCurrentActivityCode(task.getActivityCode());
	    	payloadContext.setCurrentActivityName(task.getActivityName());
	        return payloadContext;
		}catch(Exception e) {
			log.error("Convert Payload to Payload Context Object is failed:"+e, e);
			throw new TranslateException("Convert Payload to Payload Context Object is failed:"+e, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.context.convert.IPayloadTranslator#marshal(cafe.workflow.context.IPayloadContext)
	 */
	@Override
	public Map<String, Object> marshal(P payload) throws TranslateException {
		if (payload == null) return null;
		long currentTime = System.currentTimeMillis();
		String caseId = "";
		Map<String, Object> variables = null;
		//List<RestVariable> variables = null;
		try {
			MappingFieldSet<RestVariableMappingField> mappingFieldSet = (MappingFieldSet<RestVariableMappingField>)this.getMappingFieldSet(payload);
			if (mappingFieldSet != null) {
				Set<RestVariableMappingField> mappingFields = mappingFieldSet.getMappings();
				if (!CollectionUtils.isEmpty(mappingFields)) {
					
					variables = new HashMap<String, Object>();

					Iterator<RestVariableMappingField> fieldsIter = mappingFields.iterator();
					RestVariableMappingField mappingField = null;
					while (fieldsIter.hasNext()) {
						mappingField = fieldsIter.next();
						
			    		//RestVariable variable = new RestVariable();
			    		//variable.setName(mappingField.getSourceFieldName());
			    		//variable.setScope(mappingField.getScope());
			    		Object value = BeanUtils.invokeGetter(payload, mappingField.getTargetFieldName());
			    		//variable.setValue(value);
			    		//Class fieldType = BeanUtils.getFieldType(payload, mappingField.getTargetFieldName());
			    		//variable.setType(RestUtils.getRestTypeName(fieldType));
			    		//variables.add(variable);
			    		variables.put(mappingField.getSourceFieldName(), value);
					}//end of while
				}//end of if	
	    	}
	    	return variables;
		}catch(Exception e) {
			log.error("Convert Payload Context Object to Payload is failed:"+e, e);
			throw new TranslateException("Convert Payload Context Object to Payload is failed:"+e, e);
		}
	}
}
