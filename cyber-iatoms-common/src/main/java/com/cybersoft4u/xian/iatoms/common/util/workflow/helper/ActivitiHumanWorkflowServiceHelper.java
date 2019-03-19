package com.cybersoft4u.xian.iatoms.common.util.workflow.helper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.rest.service.api.engine.variable.RestVariable;
import org.activiti.rest.service.api.legacy.identity.LegacyGroupInfo;
import org.activiti.rest.service.api.legacy.identity.LegacyUserInfoWithPassword;
import org.activiti.rest.service.api.runtime.task.TaskQueryRequest;
import org.apache.http.auth.Credentials;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.util.workflow.activiti.convert.ActivitiServiceTask;
import com.cybersoft4u.xian.iatoms.common.util.workflow.activiti.convert.TaskResponse;

import cafe.core.bean.INameValue;
import cafe.core.bean.NameValue;
import cafe.core.bean.identity.LogonUser;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.workflow.bean.HumanTaskAssignee;
import cafe.workflow.bean.ITaskAssignee;
import cafe.workflow.bean.TaskConstants;
import cafe.workflow.bean.WfMessageCode;
import cafe.workflow.bean.bpmn20.activiti.rest.RestOrdering;
import cafe.workflow.bean.dto.ActivitiGroupInfoDTO;
import cafe.workflow.bean.dto.ActivitiUserInfoDTO;
import cafe.workflow.bean.dto.MemberShipBaseDTO;
import cafe.workflow.bean.dto.MemberShipDTO;
import cafe.workflow.config.WfSystemConfigManager;
import cafe.workflow.context.IPayloadContext;
import cafe.workflow.context.ITaskContext;
import cafe.workflow.context.ITaskInquiryContext;
import cafe.workflow.context.IWorkflowContext;
import cafe.workflow.context.bpmn20.activiti.ActivitiRestTaskInquiryContext;
import cafe.workflow.context.bpmn20.activiti.ActivitiWorkflowContext;
import cafe.workflow.context.convert.IPayloadTranslator;
import cafe.workflow.context.convert.ITaskContextConverter;
import cafe.workflow.util.WfConstants;
import cafe.workflow.util.i18NUtil;
import cafe.workflow.util.helper.DuplicateAcquiredTaskException;
import cafe.workflow.util.helper.HumanWorkflowException;
import cafe.workflow.util.helper.IHumanWorkflowException;
import cafe.workflow.util.helper.IHumanWorkflowServiceHelper;
import cafe.workflow.util.helper.ITaskPredicateFilterHelper;
import cafe.workflow.util.helper.bpmn20.activiti.ActivitiRestHumanWorkflowException;

/**
 * Purpose: Activiti service Helper使用actviti 原生服務對象操作activiti流程 
 * @author evanliu
 * @since  JDK 1.6
 * @date   2017年3月10日
 * @MaintenancePersonnel evanliu
 */
public class ActivitiHumanWorkflowServiceHelper implements IHumanWorkflowServiceHelper<ActivitiServiceTask, IWorkflowContext<LogonUser>>, WfMessageCode, InitializingBean {

	/**
	 * 日誌組件
	 */
	private static CafeLog log = CafeLogFactory.getLog(WfSystemConfigManager.BPM, CafeLog.class, ActivitiHumanWorkflowServiceHelper.class);
	/**
	 * 自動取件間隔時間
	 */
	private static final int DEFAULT_DURATION_TIME 				= 30;//單位:秒
	/**
	 * ITaskContextConverter
	 */
	private ITaskContextConverter taskContextConverter;
	/**
	 * ITaskPredicateFilterHelper
	 */
	private ITaskPredicateFilterHelper taskPredicateFilterHelper;
	/**
	 * 自動取件每頁大小
	 */
	private int pageSizeOfAutoAcquireTask;
	/**
	 * 標記
	 */
	private boolean enableAccessFlag;
	/**
	 * 同步鎖定
	 */
	private Object monitor = new Object();	
	/**
	 * activiti驗證服務
	 */
	private IdentityService identityService;
	/**
	 * activiti RuntimeService
	 */
	private RuntimeService runtimeService;
	/**
	 * activiti TaskService
	 */
	private TaskService taskService;
	/**
	 * activiti RepositoryService
	 */ 
	protected RepositoryService repositoryService;
	
	/**
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		//初始化相關，可以不用
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#authenticate(java.lang.String, java.lang.String)
	 */
	@Override
	public IWorkflowContext<LogonUser> authenticate(String userId, String password) throws HumanWorkflowException {
		// 登錄	
		LogonUser logonUser = new LogonUser(null, userId, null, password);
		return this.authenticate(logonUser);
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#authenticate(cafe.core.bean.identity.LogonUser)
	 */
	@Override
	public IWorkflowContext<LogonUser> authenticate(LogonUser logonUser) throws HumanWorkflowException {
		long startTime = System.currentTimeMillis();
		String userId = logonUser.getUserCode();
		String password = logonUser.getPassword();
		try {
			//驗證密碼
			boolean checkPassword = identityService.checkPassword(userId, password);
			if (checkPassword) {
				new ActivitiRestHumanWorkflowException(BPM_AUTHENTICATION_FAILED, new String[]{userId});
			}
			log.debug("authenticate", "Authenticate "+userId+" is successful !");
			return new ActivitiWorkflowContext(logonUser);
		}catch(Throwable e) {
			log.error("authenticate", "Authenticate "+userId+" is failed:"+e, e);
        	if (e instanceof HumanWorkflowException) {
        		throw (HumanWorkflowException)e;
        	}
        	else {
        		throw new ActivitiRestHumanWorkflowException(BPM_AUTHENTICATION_FAILED, new String[]{userId, e.getMessage()}, e);
        	}
		}
		finally {
			long endTime = System.currentTimeMillis();
			log.debug("authenticate", "Authenticate "+userId+" is finished!", (endTime - startTime));
		}
	}	
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#createProcessInstance(cafe.workflow.context.IWorkflowContext, java.lang.String, java.lang.String, cafe.workflow.context.IPayloadContext)
	 */
	@Override
	public boolean createProcessInstance(IWorkflowContext<LogonUser> workflowContext, String realm, String processId,
		IPayloadContext payloadContext) throws HumanWorkflowException {
		// 驗證操作Activiti的憑證
		if (workflowContext == null || workflowContext.getContext() == null) {
			throw new ActivitiRestHumanWorkflowException(BPM_WORKFLOW_CONTEXT_ISNULL);
		}
		// 驗證操作Activiti的流程ID是否有值 
		if (!StringUtils.hasText(processId)) {
			throw new ActivitiRestHumanWorkflowException(ARGUMENT_IS_NULL, new String[]{ARGUMENT_CODE.BPEL_PROCESS_NAME.toString()});
		}
		// 驗證傳參數中介是否為空
		if (payloadContext == null) {
			throw new ActivitiRestHumanWorkflowException(ARGUMENT_IS_NULL, new String[]{IAtomsConstants.PARAM_PAY_LOAD});
		}

		long enterTime = System.currentTimeMillis();

		try {
	    	log.debug("newProcessInstance", "enter " + realm + " " + processId + " bpel's task service to new bpel instance at "+new Timestamp(enterTime));
	    	// Activiti登錄憑證<相當於AP中的Session中是否有LogonUser>
	    	Map<String, Object> variables = null;
	    	//Task
			if (payloadContext != null) {
				IPayloadTranslator<Map<String, Object>, IPayloadContext> payloadTranslator = this.getPayloadTranslator();
				if (this.getPayloadTranslator() != null) {
					// 把PayloadContext中的值放置到当前PayloadContext在XML中配置的N个变量中,多余的不要了
					variables = payloadTranslator.marshal(payloadContext);
				}
			}
			identityService.setAuthenticatedUserId(workflowContext.getLogonUser().getUserCode());
			ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processId, payloadContext.getCaseId(), variables);
			return processInstance != null;
    	} catch(Throwable e) {
    		log.error("newProcessInstance", "New bpel instance is failed !! " + e, e);
    		if (e instanceof HumanWorkflowException) {
    			throw (HumanWorkflowException)e;
    		}
			throw new ActivitiRestHumanWorkflowException(BPM_INIT_PROCESS_INSTANCE_FAILED, new String[]{processId, e.getMessage()}, e);
    	}
		finally {
			long exitTime = System.currentTimeMillis();
    		log.debug("newProcessInstance", "Create "+realm+"'s" + processId+" bpel instance is finished.", (exitTime-enterTime));
    	}
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#getTasksAcquiredBy(cafe.workflow.context.IWorkflowContext, cafe.workflow.context.ITaskInquiryContext)
	 */
	@Override
	public List<ActivitiServiceTask> getTasksAcquiredBy(IWorkflowContext<LogonUser> workflowContext,
		ITaskInquiryContext taskQueryContext) throws HumanWorkflowException {
		// 根據查詢條件進行任務查詢
		if (workflowContext == null || workflowContext.getContext() == null) throw new ActivitiRestHumanWorkflowException(BPM_WORKFLOW_CONTEXT_ISNULL);
		try {
			ITaskInquiryContext<TaskQueryRequest, RestOrdering> queryBean = (taskQueryContext == null) ? new ActivitiRestTaskInquiryContext() : taskQueryContext;
			// 查詢條件封裝
			TaskQueryRequest predicate = (queryBean.getPredicate() == null) ? new TaskQueryRequest() : queryBean.getPredicate();
			predicate.setAssignee(workflowContext.getLogonUser().getUserCode());
			return this.getTaskList(workflowContext, queryBean);
		}catch (Throwable e) {
        	log.error("getTaskList", "Error occured while getting tasklist", e);
        	if (e instanceof HumanWorkflowException) {
        		throw (HumanWorkflowException)e;
        	}
        	else {
        		throw new ActivitiRestHumanWorkflowException(BPM_QUERY_TASK_FAILED, new String[]{"", e.getMessage()}, e);
        	}
        }
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#count(cafe.workflow.context.IWorkflowContext, cafe.workflow.context.ITaskInquiryContext)
	 */
	@Override
	public int count(IWorkflowContext<LogonUser> workflowContext, ITaskInquiryContext taskQueryContext) {
		// TODO Auto-generated method stub
		return 0;
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#getTaskList(cafe.workflow.context.IWorkflowContext, cafe.workflow.context.ITaskInquiryContext)
	 */
	@Override
	public List<ActivitiServiceTask> getTaskList(IWorkflowContext<LogonUser> workflowContext,
			ITaskInquiryContext taskQueryContext) throws HumanWorkflowException {
		if (workflowContext == null || workflowContext.getContext() == null) {
			throw new ActivitiRestHumanWorkflowException(BPM_WORKFLOW_CONTEXT_ISNULL);
		}
		long startTime = System.currentTimeMillis();
		try {
			log.debug("getTaskList", "start to query tasks from bpel...");
			// queryBean 中包含查詢條件DTO：TaskQueryRequest，查詢開始頁，每頁1000筆（默認）
			ITaskInquiryContext<TaskQueryRequest, RestOrdering> queryBean = (taskQueryContext == null) ? new ActivitiRestTaskInquiryContext() : taskQueryContext;
						
			TaskQuery taskQuery = taskService.createTaskQuery();
			
			TaskQueryRequest taskQueryRequest = queryBean.getPredicate();
			taskQueryRequest.getProcessDefinitionKey();
			taskQuery.taskCandidateOrAssigned(workflowContext.getLogonUser().getUserCode());
			if (StringUtils.hasText(taskQueryRequest.getAssignee())) {
				taskQuery.taskAssignee(taskQueryRequest.getAssignee());
			}
			if (StringUtils.hasText(taskQueryRequest.getCandidateGroup())) {
				taskQuery.taskCandidateGroup(taskQueryRequest.getCandidateGroup());
			}
			if (StringUtils.hasText(taskQueryRequest.getCandidateOrAssigned())) {
				taskQuery.taskCandidateOrAssigned(taskQueryRequest.getCandidateOrAssigned());
			}
			if (StringUtils.hasText(taskQueryRequest.getCandidateUser())) {
				taskQuery.taskCandidateUser(taskQueryRequest.getCandidateUser());
			}
			if (!CollectionUtils.isEmpty(taskQueryRequest.getCandidateGroupIn())) {
				taskQuery.taskCandidateGroupIn(taskQueryRequest.getCandidateGroupIn());
			}
			if (StringUtils.hasText(taskQueryRequest.getTaskDefinitionKey())) {
				taskQuery.taskDefinitionKey(taskQueryRequest.getTaskDefinitionKey());
			}
			if (StringUtils.hasText(taskQueryRequest.getProcessDefinitionKey())) {
				taskQuery.processDefinitionKey(taskQueryRequest.getProcessDefinitionKey());
			}
			//查找任務列表
			List<Task> taskList = taskQuery.active().list();
			//查找任務相關處理信息
			List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processDefinitionKey(taskQueryRequest.getProcessDefinitionKey()).list();
			
			List<ActivitiServiceTask> tasks = new ArrayList<ActivitiServiceTask>();
			if (!CollectionUtils.isEmpty(taskList) && !CollectionUtils.isEmpty(processInstances)) {
				for (Task task : taskList) {
					for (ProcessInstance processInstance : processInstances) {
						if (task.getProcessInstanceId().equals(processInstance.getProcessInstanceId())) {
							tasks.add(new ActivitiServiceTask(workflowContext, new TaskResponse(task), processInstance));
							break;
						}
					}
				}
			}						
			return tasks;
        }catch (Throwable e) {
        	log.error("getTaskList", "Error occured while getting tasklist", e);
        	if (e instanceof HumanWorkflowException) {
        		throw (HumanWorkflowException)e;
        	}
        	else {
        		throw new ActivitiRestHumanWorkflowException(BPM_QUERY_TASK_FAILED, new String[]{"", e.getMessage()}, e);
        	}
        }
        finally {
        	long endTime = System.currentTimeMillis();
        	log.debug("getTaskList","get task list is finished!", (endTime-startTime));
        }
	}
	
	/**
	 * Purpose: 簽收任務
	 * @author evanliu
	 * @param workflowContext:workflowContext
	 * @param taskId:任務編號
	 * @throws HumanWorkflowException:出錯後，拋出HumanWorkflowException
	 * @return void
	 */
	protected void claim(IWorkflowContext<LogonUser> workflowContext, String taskId) throws HumanWorkflowException {
		// 任務簽收
		if (workflowContext == null) {
			throw new ActivitiRestHumanWorkflowException(BPM_WORKFLOW_CONTEXT_ISNULL);
		}
		try {
			taskService.claim(taskId, workflowContext.getLogonUser().getUserCode());
		}catch(Throwable e) {
        	if (e instanceof HumanWorkflowException) {
        		throw (HumanWorkflowException)e;
        	}
        	else {
        		throw new ActivitiRestHumanWorkflowException(BPM_TASK_ACQUIRED_FAILED, new String[]{taskId, e.getMessage()}, e);
        	}
		}		
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#acquireTask(cafe.workflow.context.IWorkflowContext, java.lang.String)
	 */
	@Override
	public ActivitiServiceTask acquireTask(IWorkflowContext<LogonUser> workflowContext, String taskId)
			throws HumanWorkflowException {
		// 根據TaskId查詢
		if (workflowContext == null) {
			throw new ActivitiRestHumanWorkflowException(BPM_WORKFLOW_CONTEXT_ISNULL);
		}
		try {
			ActivitiServiceTask task = this.getTaskDetailsById(workflowContext, taskId);
			return this.acquireTask(workflowContext, task);
		}catch(Throwable e) {
        	if (e instanceof HumanWorkflowException) {
        		throw (HumanWorkflowException)e;
        	}
        	else {
        		throw new ActivitiRestHumanWorkflowException(BPM_TASK_ACQUIRED_FAILED, new String[]{taskId, e.getMessage()}, e);
        	}
		}
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#acquireTask(cafe.workflow.context.IWorkflowContext, cafe.workflow.bean.ITask)
	 */
	@Override
	public ActivitiServiceTask acquireTask(IWorkflowContext<LogonUser> workflowContext, ActivitiServiceTask task)
			throws HumanWorkflowException {
		if (workflowContext == null) {
			throw new ActivitiRestHumanWorkflowException(BPM_WORKFLOW_CONTEXT_ISNULL);
		}
		if (task == null) {
			throw new ActivitiRestHumanWorkflowException(ARGUMENT_IS_NULL, new String[]{ARGUMENT_CODE.TASK.toString()});
		}
		
		log.debug("acquireTask", "start to acquire task by "+workflowContext.getUserId()+"...");
		long startTime = System.currentTimeMillis();
		String caseId = "";
		String acquiredBy = "";
		try {
			LogonUser logonUser = workflowContext.getContext();
			caseId = task.getCaseId();
			String taskId = task.getTaskId();
			boolean isGroup = task.isGroup();
	        acquiredBy = task.getAcquiredBy();
	        boolean needToAcquire = true;
	        
	        if (isGroup) {
	        	if(acquiredBy != null) {
	        		if (acquiredBy.equals(workflowContext.getUserId())) needToAcquire = false;
	        		else {
	        			log.debug("acquireTask", caseId, "the case has been acquired by "+acquiredBy+"!");
	        			throw new DuplicateAcquiredTaskException(IHumanWorkflowException.ACTION_ACQUIRE, acquiredBy);
	        		}
	        	}	
	        } else {
	        	needToAcquire = false;
	        }
	        boolean needToUpdate = false;
	        if (this.enableAccessFlag) {
	        	needToUpdate = true;
	        }	        
	    	long enterTime = System.currentTimeMillis();
	    	log.debug("acquireTask", caseId, "enter bpel's task service to acquire task at "+new Timestamp(enterTime));

	        synchronized (this.monitor) {
	        	if (needToAcquire)  {
	        		this.claim(workflowContext, taskId);
	        	}

				if (needToUpdate) {
					task = this.updateTask(workflowContext, task);
				} else {
					task = this.getTaskDetailsById(workflowContext, taskId);
				}
			}
		}catch(Throwable e) {
			log.error("acquireTask", caseId, "acquire task is failed:"+e, e);
        	if (e instanceof HumanWorkflowException) throw (HumanWorkflowException)e;
        	else throw new ActivitiRestHumanWorkflowException(BPM_TASK_ACQUIRED_FAILED, new String[]{e.getMessage()}, e);
		}
		finally {
			long endTime = System.currentTimeMillis();
			log.debug("acquireTask", caseId, workflowContext.getUserId() + " acquire task is finished!", (endTime-startTime));
		}
		return task;
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#acquireTask(cafe.workflow.context.IWorkflowContext, java.lang.String, java.lang.String, java.lang.String, cafe.core.bean.identity.LogonUser, int, int, long)
	 */
	@Override
	public ActivitiServiceTask acquireTask(IWorkflowContext<LogonUser> workflowContext, String processId,
			String activityCode, String caseId, LogonUser initiator, int interval, int frequency, long duration)
			throws HumanWorkflowException {
		if (workflowContext == null) {
			throw new ActivitiRestHumanWorkflowException(BPM_WORKFLOW_CONTEXT_ISNULL);
		}

		ActivitiServiceTask task = null;
		long startTime = System.currentTimeMillis();
        try {
    		log.debug("acquireTask", caseId, "start to acquire "+((StringUtils.hasText(processId))? processId+"'" : "")+activityCode+"'task("+caseId+") by "+initiator.getId()+"...");
    		boolean isFinished = false;
			
			if(duration <= 0) duration = DEFAULT_DURATION_TIME;
			
			//為了正確取得該task,需自行建立Predicate
			ActivitiRestTaskInquiryContext taskQueryCtx = new ActivitiRestTaskInquiryContext();
			
			List<INameValue> filters = new ArrayList<INameValue>();
			filters.add(new NameValue(TaskConstants.FILTER_TYPE_PROCESS_ID, processId));
			filters.add(new NameValue(TaskConstants.FILTER_TYPE_CASE_TYPE, TaskConstants.ASSIGNMENT_FILTER_ALL));
			filters.add(new NameValue(TaskConstants.FILTER_TYPE_ACTIVITY_CODE, activityCode));
			filters.add(new NameValue(TaskConstants.FILTER_TYPE_CASE_ID, caseId));
			filters.add(new NameValue(TaskConstants.FILTER_TYPE_INITIATOR, initiator.getId()));
			IWorkflowContext<LogonUser> initiatorWfCtx = null;
			if (workflowContext.getUserId().equals(initiator.getId())) initiatorWfCtx = workflowContext;
			else initiatorWfCtx = this.authenticate(initiator);

			TaskQueryRequest filter = (TaskQueryRequest)this.taskPredicateFilterHelper.build(initiatorWfCtx, filters, null);

			taskQueryCtx.setPredicate(filter);
			long sleepTime = (interval > 0) ? interval*1000 : 0;
			log.debug("acquireTask", caseId, "Send task and wait for acquiring task with iterval "+interval +" seconds..");
			int count = 0;
			boolean isOverTime = false;

			while (!isFinished) {
				count++;
			    List<ActivitiServiceTask> tasks = this.getTaskList(workflowContext, taskQueryCtx);
			    if (!CollectionUtils.isEmpty(tasks)) {
			    	task = tasks.get(0);
			    	isFinished = true;  
			    } else {
			         int exendedTime = (int)(System.currentTimeMillis() - startTime) / 1000;//單位:秒
			         //若沒取得，不可以無限迴圈的查詢
			         if((exendedTime >= duration) || ((frequency > 0) && (count >= frequency))) {
			        	 isOverTime = true;
			        	 isFinished = true;
			         }
			         else {
			            if (interval > 0) Thread.sleep(sleepTime);
			         }
			    }
			}
			if (isOverTime) {
				throw new ActivitiRestHumanWorkflowException(BPM_ACQUIRE_TASK_TIMEOUT, new String[]{i18NUtil.getActivityName(activityCode), caseId});
			}
			if (task != null) {
				task = this.acquireTask(workflowContext, task.getTaskId());
			}
        }catch (Exception e) {
        	log.debug("acquireTask", caseId, "Acquire "+activityCode+"'task("+caseId+") by "+initiator.getId()+" is failed:"+e, e);
        	if (e instanceof HumanWorkflowException) throw (HumanWorkflowException)e;
        	else throw new ActivitiRestHumanWorkflowException(BPM_ACTIVITY_TASK_AUTO_ACQUIRED_FAILED, new String[]{i18NUtil.getActivityName(activityCode), caseId}, e);
        }
		finally {
			long endTime = System.currentTimeMillis();
			log.debug("acquireTask", caseId, initiator.getId() + " acquire task is finished!", (endTime-startTime));
		}
		return task;
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#autoAcquireTask(cafe.workflow.context.IWorkflowContext, cafe.workflow.context.ITaskInquiryContext)
	 */
	@Override
	public ActivitiServiceTask autoAcquireTask(IWorkflowContext<LogonUser> workflowContext,
		ITaskInquiryContext taskQueryContext) throws HumanWorkflowException {
		// 自動取件
		if (workflowContext == null) {
			throw new ActivitiRestHumanWorkflowException(BPM_WORKFLOW_CONTEXT_ISNULL);
		}
		ActivitiServiceTask task = null;
		long startTime = System.currentTimeMillis();
		try {
			log.debug("autoAcquireTask", "start to acquire a task by system automatically..");
		
			int startIndex = 0;
			int endIndex = 0;
			boolean isFinished = false;
			int index = 0;
			List<ActivitiServiceTask> tasks = null;
			int pageSize = (this.pageSizeOfAutoAcquireTask <= 0 || this.pageSizeOfAutoAcquireTask > WfConstants.DEFAULT_PAGE_ROW_SIZE) ? WfConstants.DEFAULT_PAGE_ROW_SIZE : this.pageSizeOfAutoAcquireTask;
			
			ITaskInquiryContext<TaskQueryRequest, RestOrdering> taskQueryCtx = (taskQueryContext == null) ? new ActivitiRestTaskInquiryContext() : taskQueryContext;
			// 判斷該查詢條件下是否已經沒有案件了
			while (!isFinished) {
				// 自動取件原理:在原查詢開始頁 + 1,根據原查詢條件查詢出pageSize條數據
				taskQueryCtx.setStartPage(startIndex+1);
				taskQueryCtx.setPageSize(pageSize);
						
				tasks = this.getTaskList(workflowContext, taskQueryCtx);
				
				if (CollectionUtils.isEmpty(tasks))  {
					isFinished = true;
					throw new ActivitiRestHumanWorkflowException(BPM_NO_TASK_ACQUIRED);
				}
				
				Iterator<ActivitiServiceTask> taskIter = tasks.iterator();
				while (taskIter.hasNext()) {
						task = taskIter.next();
						
						String taskId = task.getTaskId();
						try {
							task = this.acquireTask(workflowContext, taskId);
							log.debug("autoAcquireTask", "A task has been acquired by system successfully !");
							isFinished = true;
							break;
						}catch(HumanWorkflowException ee) {
							//若案件已被取走,再取下一件
							if (ee instanceof DuplicateAcquiredTaskException) continue;
							else if (ee instanceof ActivitiRestHumanWorkflowException) {
								if(((ActivitiRestHumanWorkflowException)ee).getErrorCode().equals(BPM_TASK_HAS_BEEN_DONE)) continue;
								else {
									isFinished = true;
									throw ee;
								}
							}
						}
				}//end of while
				index++;
			}	
		} catch (Exception e) {
			log.error("autoAcquireTask", "AutoAcquireTask is failed:"+e, e);
			if (e instanceof ActivitiRestHumanWorkflowException) {
				throw (ActivitiRestHumanWorkflowException)e;
			} else {
        		throw new ActivitiRestHumanWorkflowException(BPM_TASK_ACQUIRED_FAILED, new String[]{e.getMessage()}, e);
        	}
		}
		finally {
			long endTime = System.currentTimeMillis();
			log.debug("autoAcquireTask", "Aacquire task is finished!", (endTime-startTime));
		}
		return task;
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#releaseTask(cafe.workflow.context.IWorkflowContext, java.lang.String)
	 */
	@Override
	public void releaseTask(IWorkflowContext<LogonUser> workflowContext, String taskId) throws HumanWorkflowException {
		// 释放任務
		if (workflowContext == null) {
			throw new ActivitiRestHumanWorkflowException(BPM_WORKFLOW_CONTEXT_ISNULL);
		}
		try {
			taskService.unclaim(taskId);
		}catch(Throwable e) {
        	if (e instanceof HumanWorkflowException) {
        		throw (HumanWorkflowException)e;
        	} else {
        		throw new ActivitiRestHumanWorkflowException(BPM_TASK_RELEASED_FAILED, new String[]{taskId, e.getMessage()}, e);
        	}
		}
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#releaseTask(cafe.workflow.context.IWorkflowContext, cafe.workflow.bean.ITask)
	 */
	@Override
	public void releaseTask(IWorkflowContext<LogonUser> workflowContext, ActivitiServiceTask task)
			throws HumanWorkflowException {
		// 釋放任務並更新參數
		if (workflowContext == null) throw new ActivitiRestHumanWorkflowException(BPM_WORKFLOW_CONTEXT_ISNULL);
		if (task == null) throw new ActivitiRestHumanWorkflowException(ARGUMENT_IS_NULL, new String[]{ARGUMENT_CODE.TASK.toString()});

		long startTime = System.currentTimeMillis();
		try {
			log.debug("releaseTask", task.getCaseId(), "start to release task by "+workflowContext.getUserId()+"...");
			// 根據TaskId釋放任務方法
			this.releaseTask(workflowContext, task.getTaskId());
			// 釋放任務後更新參數
			this.updateTask(workflowContext, task);
			log.debug("releaseTask", task.getCaseId(), "case has been released by user "+workflowContext.getUserId()+" successfully!");
		}catch(Exception e) {
        	log.debug("releaseTask", task.getCaseId(), "Release task by "+workflowContext.getUserId()+" is failed:"+e, e);
			if (e instanceof HumanWorkflowException) throw (HumanWorkflowException)e;
        	else throw new ActivitiRestHumanWorkflowException(BPM_TASK_RELEASED_FAILED, new String[]{workflowContext.getUserId(), task.getCaseId(), e.getMessage()}, e);
		}
		finally {
			long endTime = System.currentTimeMillis();
			log.debug("releaseTask", task.getCaseId() , "release task is finished!", (endTime-startTime));
		}
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#getTaskDetailsById(cafe.workflow.context.IWorkflowContext, java.lang.String)
	 */
	@Override
	public ActivitiServiceTask getTaskDetailsById(IWorkflowContext<LogonUser> workflowContext, String taskId)
			throws HumanWorkflowException {
		if (workflowContext == null) {
			throw new ActivitiRestHumanWorkflowException(BPM_WORKFLOW_CONTEXT_ISNULL);
		}
		if (!StringUtils.hasText(taskId)) {
			throw new ActivitiRestHumanWorkflowException(ARGUMENT_IS_NULL, new String[]{ARGUMENT_CODE.TASK_ID.toString()});
		}
		long startTime = System.currentTimeMillis();
		try {
			Task task = taskService.createTaskQuery().taskId(taskId).active().singleResult();
			if (null != task) {
				ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
				return new ActivitiServiceTask(workflowContext, new TaskResponse(task), processInstance);
			} else {
				throw new ActivitiRestHumanWorkflowException(IHumanWorkflowException.ACTION_QUERY, null, new String[] {"案件查詢", taskId});
			}
		}catch(Exception e){
        	if (e instanceof HumanWorkflowException) throw (HumanWorkflowException)e;
        	else throw new ActivitiRestHumanWorkflowException(BPM_QUERY_TASK_FAILED, new String[]{taskId, e.getMessage()}, e);
		}
		finally {
			long endTime = System.currentTimeMillis();
			log.debug("getTaskDetailsById","get task by taskId("+taskId+") finished!", (endTime-startTime));
		}
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#getTaskContextById(cafe.workflow.context.IWorkflowContext, java.lang.String)
	 */
	@Override
	public ITaskContext getTaskContextById(IWorkflowContext<LogonUser> workflowContext, String taskId)
			throws HumanWorkflowException {
		try {
			// 通過TaskId查詢任務明細
			ActivitiServiceTask task = this.getTaskDetailsById(workflowContext, taskId);
			// 參數解析
			return this.translate(task);
		}catch(Exception e) {
			if (e instanceof HumanWorkflowException) throw (HumanWorkflowException)e;
			else throw new ActivitiRestHumanWorkflowException(BPM_QUERY_TASK_FAILED, new String[]{taskId, e.getMessage()}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#updateTask(cafe.workflow.context.IWorkflowContext, cafe.workflow.bean.ITask)
	 */
	@Override
	public ActivitiServiceTask updateTask(IWorkflowContext<LogonUser> workflowContext, ActivitiServiceTask task)
			throws HumanWorkflowException {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#updateTask(cafe.workflow.context.IWorkflowContext, java.lang.String, cafe.workflow.context.IPayloadContext)
	 */
	@Override
	public ActivitiServiceTask updateTask(IWorkflowContext<LogonUser> workflowContext, String taskId,
			IPayloadContext payloadContext) throws HumanWorkflowException {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#performTask(cafe.workflow.context.IWorkflowContext, cafe.workflow.bean.ITask, java.lang.String)
	 */
	@Override
	public void performTask(IWorkflowContext<LogonUser> workflowContext, ActivitiServiceTask task, String outcome)
			throws HumanWorkflowException {
		if (workflowContext == null) {
			throw new ActivitiRestHumanWorkflowException(BPM_WORKFLOW_CONTEXT_ISNULL);
		}
		if (task == null) {
			throw new ActivitiRestHumanWorkflowException(ARGUMENT_IS_NULL, new String[]{ARGUMENT_CODE.TASK.toString()});
		}
		long startTime = System.currentTimeMillis();
		String caseId = task.getCaseId();
		String taskId = task.getTaskId();
		try {
    		log.debug("performTask", caseId, "payload:\n"+task.toString());
    		identityService.setAuthenticatedUserId(workflowContext.getLogonUser().getUserCode());
    		taskService.complete(taskId, task.getPayload());
	    	log.debug("performTask", caseId,"enter task service to update task...");
		} catch(Throwable e) {
        	if (e instanceof HumanWorkflowException) {
        		throw (HumanWorkflowException)e;
        	} else {throw new ActivitiRestHumanWorkflowException(BPM_PERFORM_TASK_FAILED, new String[]{taskId, e.getMessage()}, e);
        	
        	}
		} finally {
			long endTime = System.currentTimeMillis();
			log.debug("performTask", caseId,  "perform Task is finished!", (endTime-startTime));
		}
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#performTask(cafe.workflow.context.IWorkflowContext, java.lang.String, java.lang.String)
	 */
	@Override
	public void performTask(IWorkflowContext<LogonUser> workflowContext, String taskId, String outcome)
			throws HumanWorkflowException {
		if (workflowContext == null) throw new ActivitiRestHumanWorkflowException(BPM_WORKFLOW_CONTEXT_ISNULL);
		if (!StringUtils.hasText(taskId)) throw new ActivitiRestHumanWorkflowException(ARGUMENT_IS_NULL, new String[]{ARGUMENT_CODE.TASK_ID.toString()});
		long startTime = System.currentTimeMillis();
		try {
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put(ITaskContext.FIELD_OUTCOME, outcome);
			taskService.complete(taskId, variables);
		}catch(Throwable e) {
        	if (e instanceof HumanWorkflowException) throw (HumanWorkflowException)e;
        	else throw new ActivitiRestHumanWorkflowException(BPM_PERFORM_TASK_FAILED, new String[]{taskId, e.getMessage()}, e);
		}
		finally {
			long endTime = System.currentTimeMillis();
			log.debug("performTask", "perform Task is finished!", (endTime-startTime));
		}
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#performTasks(cafe.workflow.context.IWorkflowContext, java.util.List, java.lang.String)
	 */
	@Override
	public void performTasks(IWorkflowContext<LogonUser> workflowContext, List<String> taskIds, String outcome)
			throws HumanWorkflowException {
		/**
		 * 多任務送出
		 */
		if (workflowContext == null) {
			throw new ActivitiRestHumanWorkflowException(BPM_WORKFLOW_CONTEXT_ISNULL);
		}
		if (CollectionUtils.isEmpty(taskIds)) {
			throw new ActivitiRestHumanWorkflowException(ARGUMENT_IS_NULL, new String[]{ARGUMENT_CODE.TASK.toString()});
		}
		for (String taskId : taskIds) {
			this.performTask(workflowContext, taskId, outcome);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#logout(cafe.workflow.context.IWorkflowContext)
	 */
	@Override
	public void logout(IWorkflowContext<LogonUser> workflowContext) throws HumanWorkflowException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#withdrawTask(cafe.workflow.context.IWorkflowContext, java.lang.String)
	 */
	@Override
	public void withdrawTask(IWorkflowContext<LogonUser> workflowContext, String taskId) throws HumanWorkflowException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#withdrawTask(cafe.workflow.context.IWorkflowContext, cafe.workflow.bean.ITask)
	 */
	@Override
	public void withdrawTask(IWorkflowContext<LogonUser> workflowContext, ActivitiServiceTask task)
			throws HumanWorkflowException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#reassignTask(cafe.workflow.context.IWorkflowContext, java.lang.String, cafe.workflow.bean.HumanTaskAssignee)
	 */
	@Override
	public void reassignTask(IWorkflowContext<LogonUser> workflowContext, String taskId, HumanTaskAssignee assignees)
			throws HumanWorkflowException {
		// 任務重新指派
		if (assignees == null || !assignees.hasAssignee()) {
			throw new ActivitiRestHumanWorkflowException(BPM_REASSIGN_TASK_FAILED, new String[]{ARGUMENT_CODE.REASSIGNEE_MUST_BE_AT_LEAST_ONE.toString()});
		}
		reassignTask(workflowContext, taskId, assignees.getAssignees());
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#reassignTask(cafe.workflow.context.IWorkflowContext, java.lang.String, java.util.List)
	 */
	@Override
	public void reassignTask(IWorkflowContext<LogonUser> workflowContext, String taskId, List<ITaskAssignee> assignees)
			throws HumanWorkflowException {
		/**
		 * 任務重新指派
		 * 			taskId:任務Id
		 * 			assignees:重新指派人
		 */
		if (workflowContext == null) {
			throw new ActivitiRestHumanWorkflowException(BPM_WORKFLOW_CONTEXT_ISNULL);
		}
		if(CollectionUtils.isEmpty(assignees)) {
			throw new ActivitiRestHumanWorkflowException(BPM_REASSIGN_TASK_FAILED, new String[]{ARGUMENT_CODE.REASSIGNEE_MUST_BE_AT_LEAST_ONE.toString()});
		}
		try {
			return;
		}catch(Throwable e) {
        	if (e instanceof HumanWorkflowException) {
        		throw (HumanWorkflowException)e;
        	}
        	else {
        		throw new ActivitiRestHumanWorkflowException(BPM_REASSIGN_TASK_FAILED, new String[]{taskId, e.getMessage()}, e);
        	}
		}
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#delegateTask(cafe.workflow.context.IWorkflowContext, java.lang.String, java.lang.String)
	 */
	@Override
	public void delegateTask(IWorkflowContext<LogonUser> workflowContext, String taskId, String assingeeUser)
			throws HumanWorkflowException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#resolveTask(cafe.workflow.context.IWorkflowContext, java.lang.String)
	 */
	@Override
	public void resolveTask(IWorkflowContext<LogonUser> workflowContext, String taskId) throws HumanWorkflowException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#translate(cafe.workflow.bean.ITask)
	 */
	@Override
	public ITaskContext translate(ActivitiServiceTask task) throws Exception {
		return this.taskContextConverter.convert(task);
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#getPayloadTranslator()
	 */
	@Override
	public IPayloadTranslator getPayloadTranslator() {
		if (this.taskContextConverter == null) return null;
		return this.taskContextConverter.getPayloadTranslator();
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#createUser(cafe.workflow.context.IWorkflowContext, org.activiti.rest.service.api.legacy.identity.LegacyUserInfoWithPassword)
	 */
	@Override
	public int createUser(IWorkflowContext<LogonUser> workflowContext, LegacyUserInfoWithPassword request)
			throws HumanWorkflowException {
		// 驗證操作Activiti的憑證
		if (workflowContext == null || workflowContext.getContext() == null) throw new ActivitiRestHumanWorkflowException(BPM_WORKFLOW_CONTEXT_ISNULL);
		long enterTime = System.currentTimeMillis();
		try {
			log.debug(this.getClass().getName() + ".createUser() startTime = "  + enterTime);
			log.debug(this.getClass().getName() + ".createUser() USERID = "  + request.getId());
	    	// Activiti登錄憑證<相當於AP中的Session中是否有LogonUser>
	    	
	    	identityService.setAuthenticatedUserId(workflowContext.getLogonUser().getUserCode());
	    	
	    	User user = identityService.createUserQuery().userId(request.getId()).singleResult();
	    	if (user != null) {
	    		throw new ActivitiRestHumanWorkflowException(BPM_CREATE_FAILED, new String[]{"用戶"});
	    	}
	        user = identityService.newUser(request.getId());
	         
	        user.setFirstName(request.getFirstName());
	        user.setLastName(request.getLastName());
	        user.setEmail(request.getEmail());
	        if (StringUtils.hasText(request.getPassword())) {
	            user.setPassword(request.getPassword());
	        }
	        identityService.saveUser(user);
		} catch (Exception e) { 
			if (e instanceof HumanWorkflowException) throw (HumanWorkflowException)e;
        	else throw new ActivitiRestHumanWorkflowException(BPM_CREATE_FAILED,new String[]{"用戶"}, e);
		}
		return 0;
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#deleteUser(cafe.workflow.context.IWorkflowContext, java.lang.String)
	 */
	@Override
	public int deleteUser(IWorkflowContext<LogonUser> workflowContext, String userId) throws HumanWorkflowException {
		// 驗證操作Activiti的憑證
		if (workflowContext == null || workflowContext.getContext() == null) throw new ActivitiRestHumanWorkflowException(BPM_WORKFLOW_CONTEXT_ISNULL);
		if (!StringUtils.hasText(userId)) throw new ActivitiRestHumanWorkflowException(BPM_REQUEST_ID_IS_NULL,new String[]{"用戶"});
		long enterTime = System.currentTimeMillis();
		try {
			log.debug(this.getClass().getName() + ".deleteUser() startTime = "  + enterTime);
			log.debug(this.getClass().getName() + ".deleteUser() USERID = "  + userId);
			identityService.setAuthenticatedUserId(workflowContext.getLogonUser().getUserCode());
			identityService.deleteUser(userId);
		} catch (Exception e) { 
			if (e instanceof HumanWorkflowException) throw (HumanWorkflowException)e;
        	else throw new ActivitiRestHumanWorkflowException(BPM_DELETE_FAILED,new String[]{"用戶"}, e);
		}
		return 0;
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#updateUser(cafe.workflow.context.IWorkflowContext, org.activiti.rest.service.api.legacy.identity.LegacyUserInfoWithPassword)
	 */
	@Override
	public int updateUser(IWorkflowContext<LogonUser> workflowContext, LegacyUserInfoWithPassword request)
			throws HumanWorkflowException {
		// 驗證操作Activiti的憑證
		if (workflowContext == null || workflowContext.getContext() == null) throw new ActivitiRestHumanWorkflowException(BPM_WORKFLOW_CONTEXT_ISNULL);
		if (request == null)  throw new ActivitiRestHumanWorkflowException(BPM_REQUEST_DATA_IS_NULL);
		long enterTime = System.currentTimeMillis();
		try {
			log.debug(this.getClass().getName() + ".updateUser() startTime = "  + enterTime);
			log.debug(this.getClass().getName() + ".updateUser() USERID = "  + request.getId());
			log.debug(this.getClass().getName() + ".updateUser() FIRST_NAME = "  + request.getFirstName());
			log.debug(this.getClass().getName() + ".updateUser() LAST_NAME = "  + request.getLastName());
			log.debug(this.getClass().getName() + ".updateUser() E-MAIL = "  + request.getEmail());
			
			identityService.setAuthenticatedUserId(workflowContext.getLogonUser().getUserCode());
	    	User user = identityService.createUserQuery().userId(request.getId()).singleResult();
	    	if (user == null) {
	    		throw new ActivitiRestHumanWorkflowException(BPM_UPDATE_FAILED, new String[]{"用戶"});
	    	}
	         
	        user.setFirstName(request.getFirstName());
	        user.setLastName(request.getLastName());
	        user.setEmail(request.getEmail());
	        identityService.saveUser(user);
		} catch (Exception e) {
			if (e instanceof HumanWorkflowException) throw (HumanWorkflowException)e;
        	else throw new ActivitiRestHumanWorkflowException(BPM_UPDATE_FAILED,new String[]{"用戶"}, e);
		}
		return 0;
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#getUserInfoById(cafe.workflow.context.IWorkflowContext, java.lang.String)
	 */
	@Override
	public ActivitiUserInfoDTO getUserInfoById(IWorkflowContext<LogonUser> workflowContext, String userId)
			throws HumanWorkflowException {		
		if (workflowContext == null || workflowContext.getContext() == null) throw new ActivitiRestHumanWorkflowException(BPM_WORKFLOW_CONTEXT_ISNULL);
		if (!StringUtils.hasText(userId)) throw new ActivitiRestHumanWorkflowException(BPM_REQUEST_ID_IS_NULL,new String[]{"用戶"});
		ActivitiUserInfoDTO activitiUserInfoDTO = null;
		try {
			long enterTime = System.currentTimeMillis();
			log.debug(this.getClass().getName() + ".getUserInfoById() startTime = "  + enterTime);
			log.debug(this.getClass().getName() + ".getUserInfoById() USERID = "  + userId);
			
			identityService.setAuthenticatedUserId(workflowContext.getLogonUser().getUserCode());
			User user = identityService.createUserQuery().userId(userId).singleResult();
			if (null != user) {
				activitiUserInfoDTO = new ActivitiUserInfoDTO();
				activitiUserInfoDTO.setEmail(user.getEmail());
				activitiUserInfoDTO.setFirstName(user.getFirstName());
				activitiUserInfoDTO.setId(user.getId());
				activitiUserInfoDTO.setLastName(user.getLastName());
				activitiUserInfoDTO.setPassword(user.getPassword());
			}
		} catch (Exception e) {
			if(e instanceof HttpClientErrorException){
				HttpStatus status = ((HttpClientErrorException) e).getStatusCode();
				if(status.value() == HttpStatus.NOT_FOUND.value()){
					return null;
				}
			}
			if (e instanceof HumanWorkflowException) throw (HumanWorkflowException)e;
        	else throw new ActivitiRestHumanWorkflowException(BPM_QUERY_FAILE,new String[]{"用戶"}, e);
		}
		return activitiUserInfoDTO;
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#createGroup(cafe.workflow.context.IWorkflowContext, org.activiti.rest.service.api.legacy.identity.LegacyGroupInfo)
	 */
	@Override
	public int createGroup(IWorkflowContext<LogonUser> workflowContext, LegacyGroupInfo request)
			throws HumanWorkflowException {
		// 驗證操作Activiti的憑證
		if (workflowContext == null || workflowContext.getContext() == null) throw new ActivitiRestHumanWorkflowException(BPM_WORKFLOW_CONTEXT_ISNULL);
		
		long enterTime = System.currentTimeMillis();
		try {
			log.debug(this.getClass().getName() + ".createGroup() startTime = "  + enterTime);
			log.debug(this.getClass().getName() + ".createGroup() GROUPID = "  + request.getId());
	    	// Activiti登錄憑證<相當於AP中的Session中是否有LogonUser>
			identityService.setAuthenticatedUserId(workflowContext.getLogonUser().getUserCode());
			Group group = identityService.createGroupQuery().groupId(request.getId()).singleResult();
	        if (group != null) {
	        	throw new ActivitiRestHumanWorkflowException(BPM_UPDATE_FAILED, new String[]{"群組"});
	        }
	        group = identityService.newGroup(request.getId());
	        group.setName(request.getName());
	        group.setType(request.getType());
	        identityService.saveGroup(group);
		} catch (Exception e) {
			if (e instanceof HumanWorkflowException) throw (HumanWorkflowException)e;
        	else throw new ActivitiRestHumanWorkflowException(BPM_CREATE_FAILED,new String[]{"群組"}, e);
		}
		return 0;
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#updateGroup(cafe.workflow.context.IWorkflowContext, org.activiti.rest.service.api.legacy.identity.LegacyGroupInfo)
	 */
	@Override
	public int updateGroup(IWorkflowContext<LogonUser> workflowContext, LegacyGroupInfo request)
			throws HumanWorkflowException {
		if (workflowContext == null || workflowContext.getContext() == null) throw new ActivitiRestHumanWorkflowException(BPM_WORKFLOW_CONTEXT_ISNULL);
		if (request == null)  throw new ActivitiRestHumanWorkflowException(BPM_REQUEST_DATA_IS_NULL);
		long enterTime = System.currentTimeMillis();
		try {
			log.debug(this.getClass().getName() + ".updateGroup() startTime = "  + enterTime);
			log.debug(this.getClass().getName() + ".updateGroup() GROUP_ID = "  + request.getId());
			log.debug(this.getClass().getName() + ".updateGroup() GROUP_NAME = "  + request.getName());
			log.debug(this.getClass().getName() + ".updateGroup() GROUP_TYPE = "  + request.getType());
			identityService.setAuthenticatedUserId(workflowContext.getLogonUser().getUserCode());
			Group group = identityService.createGroupQuery().groupId(request.getId()).singleResult();
	        if (group == null) {
	        	throw new ActivitiRestHumanWorkflowException(BPM_CREATE_FAILED, new String[]{"群組"});
	        }
	        group.setName(request.getName());
	        group.setType(request.getType());
	        identityService.saveGroup(group);
		} catch (Exception e) {
			if (e instanceof HumanWorkflowException) throw (HumanWorkflowException)e;
        	else throw new ActivitiRestHumanWorkflowException(BPM_UPDATE_FAILED,new String[]{"群組"}, e);
		}
		return 0;
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#deleteGroup(cafe.workflow.context.IWorkflowContext, java.lang.String)
	 */
	@Override
	public int deleteGroup(IWorkflowContext<LogonUser> workflowContext, String groupId) throws HumanWorkflowException {
		// 驗證操作Activiti的憑證
		if (workflowContext == null || workflowContext.getContext() == null) throw new ActivitiRestHumanWorkflowException(BPM_WORKFLOW_CONTEXT_ISNULL);
		if (!StringUtils.hasText(groupId)) throw new ActivitiRestHumanWorkflowException(BPM_REQUEST_ID_IS_NULL,new String[]{"群組"});
		long enterTime = System.currentTimeMillis();
		try {
			log.debug(this.getClass().getName() + ".deleteGroup() startTime = "  + enterTime);
			log.debug(this.getClass().getName() + ".deleteGroup() GROUP_ID = "  + groupId);
			identityService.setAuthenticatedUserId(workflowContext.getLogonUser().getUserCode());
			identityService.deleteGroup(groupId);
		} catch (Exception e) { 
			if (e instanceof HumanWorkflowException) throw (HumanWorkflowException)e;
        	else throw new ActivitiRestHumanWorkflowException(BPM_DELETE_FAILED,new String[]{"群組"}, e);
		}
		return 0;
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#getGroupInfoById(cafe.workflow.context.IWorkflowContext, java.lang.String)
	 */
	@Override
	public ActivitiGroupInfoDTO getGroupInfoById(IWorkflowContext<LogonUser> workflowContext, String groupId)
			throws HumanWorkflowException {
		if (workflowContext == null || workflowContext.getContext() == null) throw new ActivitiRestHumanWorkflowException(BPM_WORKFLOW_CONTEXT_ISNULL);
		if (!StringUtils.hasText(groupId)) throw new ActivitiRestHumanWorkflowException(BPM_REQUEST_ID_IS_NULL,new String[]{"群組"});
		ActivitiGroupInfoDTO activitiGroupInfoDTO = null;
		try {
			long enterTime = System.currentTimeMillis();
			log.debug(this.getClass().getName() + ".getGroupInfoById() startTime = "  + enterTime);
			log.debug(this.getClass().getName() + ".getGroupInfoById() GROUP_ID = "  + groupId);
			identityService.setAuthenticatedUserId(workflowContext.getLogonUser().getUserCode());
			Group group = identityService.createGroupQuery().groupId(groupId).singleResult();
			if (null != group) {
				activitiGroupInfoDTO = new ActivitiGroupInfoDTO();
				activitiGroupInfoDTO.setId(group.getId());
				activitiGroupInfoDTO.setName(group.getName());
				activitiGroupInfoDTO.setType(group.getType());
			}
		} catch (Exception e) {
			if(e instanceof HttpClientErrorException){
				HttpStatus status = ((HttpClientErrorException) e).getStatusCode();
				if(status.value() == HttpStatus.NOT_FOUND.value()){
					return null;
				}
			}
			if (e instanceof HumanWorkflowException) throw (HumanWorkflowException)e;
        	else throw new ActivitiRestHumanWorkflowException(BPM_QUERY_FAILE,new String[]{"群組"}, e);
		}
		return activitiGroupInfoDTO;
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#getAllUserByGroupId(cafe.workflow.context.IWorkflowContext, java.lang.String)
	 */
	@Override
	public List<ActivitiUserInfoDTO> getAllUserByGroupId(IWorkflowContext<LogonUser> workflowContext, String groupId)
			throws HumanWorkflowException {
		if (workflowContext == null || workflowContext.getContext() == null) throw new ActivitiRestHumanWorkflowException(BPM_WORKFLOW_CONTEXT_ISNULL);
		if (!StringUtils.hasText(groupId)) throw new ActivitiRestHumanWorkflowException(BPM_REQUEST_ID_IS_NULL,new String[]{"群組"});
		List<ActivitiUserInfoDTO> activitiUserInfoDTOList = null;
		try {
			log.debug("getAllUserByGroupId", "start to query users by groupId from bpel...");
			// 登錄憑證,相當於AP中--》Session中的LogonUser
			Credentials credentials = ((ActivitiWorkflowContext)workflowContext).getCredentials();
			identityService.setAuthenticatedUserId(workflowContext.getLogonUser().getUserCode());
			List<User> users = identityService.createUserQuery().memberOfGroup(groupId).list();
			if (!CollectionUtils.isEmpty(users)) {
				activitiUserInfoDTOList = new ArrayList<ActivitiUserInfoDTO>();
				ActivitiUserInfoDTO activitiUserInfoDTO = null;
				for (User user : users) {
					activitiUserInfoDTO = new ActivitiUserInfoDTO();
					activitiUserInfoDTO.setEmail(user.getEmail());
					activitiUserInfoDTO.setFirstName(user.getFirstName());
					activitiUserInfoDTO.setId(user.getId());
					activitiUserInfoDTO.setLastName(user.getLastName());
					activitiUserInfoDTO.setPassword(user.getPassword());
					activitiUserInfoDTOList.add(activitiUserInfoDTO);
				}
			};
			log.debug("getAllUserByGroupId", "query users by groupId from bpel successfully!");
		}catch(Exception e){
			if (e instanceof HumanWorkflowException) throw (HumanWorkflowException)e;
        	else throw new ActivitiRestHumanWorkflowException(BPM_QUERY_FAILE,new String[]{"群組"}, e);
		}
		return activitiUserInfoDTOList;
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#addUserToGroup(cafe.workflow.context.IWorkflowContext, cafe.workflow.bean.dto.MemberShipBaseDTO, java.lang.String)
	 */
	@Override
	public MemberShipDTO addUserToGroup(IWorkflowContext<LogonUser> workflowContext, MemberShipBaseDTO request,
			String groupId) throws HumanWorkflowException {
		if (workflowContext == null || workflowContext.getContext() == null) throw new ActivitiRestHumanWorkflowException(BPM_WORKFLOW_CONTEXT_ISNULL);
		if (request == null || !StringUtils.hasText(groupId) || !StringUtils.hasText(request.getUserId())){
			throw new ActivitiRestHumanWorkflowException(BPM_REQUEST_ID_IS_NULL,new String[]{"用戶或群組"});
		}
		try {
			long enterTime = System.currentTimeMillis();
			log.debug(this.getClass().getName()+".addUserToGroup() startTime = "  + enterTime);
			log.debug(this.getClass().getName()+".addUserToGroup() GROUP_ID = "  + groupId);
			log.debug(this.getClass().getName()+".addUserToGroup() USER_ID  = "  + request.getUserId());
			// 向Activiti中利用問號傳參的方式進行參數傳遞
			identityService.setAuthenticatedUserId(workflowContext.getLogonUser().getUserCode());
			identityService.createMembership(request.getUserId(), groupId);
		} catch (Exception e) {
			if (e instanceof HumanWorkflowException) throw (HumanWorkflowException)e;
        	else throw new ActivitiRestHumanWorkflowException(BPM_ADD_USER_TO_GROUP_FAILE, e);
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.util.helper.IHumanWorkflowServiceHelper#removeUserForGroup(cafe.workflow.context.IWorkflowContext, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean removeUserForGroup(IWorkflowContext<LogonUser> workflowContext, String userId, String groupId)
			throws HumanWorkflowException {
		boolean isSuccess = false;
		if (workflowContext == null || workflowContext.getContext() == null) throw new ActivitiRestHumanWorkflowException(BPM_WORKFLOW_CONTEXT_ISNULL);
		if (!StringUtils.hasText(groupId) || !StringUtils.hasText(userId)){
			throw new ActivitiRestHumanWorkflowException(BPM_REQUEST_ID_IS_NULL,new String[]{"用戶或群組"});
		}
		try {
			long enterTime = System.currentTimeMillis();
			log.debug(this.getClass().getName()+".removeUserForGroup() startTime = "  + enterTime);
			log.debug(this.getClass().getName()+".removeUserForGroup() GROUP_ID = "  + groupId);
			log.debug(this.getClass().getName()+".removeUserForGroup() USER_ID  = "  + userId);
			identityService.setAuthenticatedUserId(workflowContext.getLogonUser().getUserCode());
			identityService.deleteMembership(userId, groupId);
			
		} catch (Exception e) {
			if (e instanceof HumanWorkflowException) throw (HumanWorkflowException)e;
        	else throw new ActivitiRestHumanWorkflowException(BPM_REMOVE_USER_FOR_GROUP_FAILE, e);
		}
		return isSuccess;
	}
	
	/**
	 * 
	 * @param workflowContext
	 * @param taskId
	 * @param action
	 * @param assigneeUser
	 * @param variables
	 * @return
	 * @throws Exception
	 */
	private HttpStatus executeTaskAction(IWorkflowContext<LogonUser> workflowContext, String taskId, String restAction, String outcome, String assigneeUser, List<RestVariable> variables) throws Exception {
		if (workflowContext == null) throw new ActivitiRestHumanWorkflowException(BPM_WORKFLOW_CONTEXT_ISNULL);
		try {
			return null;
		}catch(Exception e) {
			throw e;
		}
	}
	/**
	 * @return the taskService
	 */
	public TaskService getTaskService() {
		return taskService;
	}
	/**
	 * @param taskService the taskService to set
	 */
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
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
	public void setTaskPredicateFilterHelper(ITaskPredicateFilterHelper taskPredicateFilterHelper) {
		this.taskPredicateFilterHelper = taskPredicateFilterHelper;
	}
	/**
	 * @return the repositoryService
	 */
	public RepositoryService getRepositoryService() {
		return repositoryService;
	}
	/**
	 * @param repositoryService the repositoryService to set
	 */
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	/**
	 * @return the taskContextConverter
	 */
	public ITaskContextConverter getTaskContextConverter() {
		return taskContextConverter;
	}
	/**
	 * @param taskContextConverter the taskContextConverter to set
	 */
	public void setTaskContextConverter(ITaskContextConverter taskContextConverter) {
		this.taskContextConverter = taskContextConverter;
	}
	/**
	 * @return the pageSizeOfAutoAcquireTask
	 */
	public int getPageSizeOfAutoAcquireTask() {
		return pageSizeOfAutoAcquireTask;
	}
	/**
	 * @param pageSizeOfAutoAcquireTask the pageSizeOfAutoAcquireTask to set
	 */
	public void setPageSizeOfAutoAcquireTask(int pageSizeOfAutoAcquireTask) {
		this.pageSizeOfAutoAcquireTask = pageSizeOfAutoAcquireTask;
	}
	/**
	 * @return the enableAccessFlag
	 */
	public boolean isEnableAccessFlag() {
		return enableAccessFlag;
	}
	/**
	 * @param enableAccessFlag the enableAccessFlag to set
	 */
	public void setEnableAccessFlag(boolean enableAccessFlag) {
		this.enableAccessFlag = enableAccessFlag;
	}
	/**
	 * @return the monitor
	 */
	public Object getMonitor() {
		return monitor;
	}
	/**
	 * @param monitor the monitor to set
	 */
	public void setMonitor(Object monitor) {
		this.monitor = monitor;
	}
	/**
	 * @return the identityService
	 */
	public IdentityService getIdentityService() {
		return identityService;
	}
	/**
	 * @param identityService the identityService to set
	 */
	public void setIdentityService(IdentityService identityService) {
		this.identityService = identityService;
	}
	/**
	 * @return the runtimeService
	 */
	public RuntimeService getRuntimeService() {
		return runtimeService;
	}
	/**
	 * @param runtimeService the runtimeService to set
	 */
	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}

}
