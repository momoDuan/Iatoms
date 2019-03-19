package com.cybersoft4u.xian.iatoms.services.workflow.impl;

import java.util.Iterator;

import org.activiti.rest.service.api.runtime.task.TaskQueryRequest;
import org.springframework.util.CollectionUtils;

import cafe.core.bean.Message;
import cafe.core.bean.identity.LogonUser;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.SessionContext;
import cafe.core.service.ServiceException;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.workflow.bean.HumanTaskAssignee;
import cafe.workflow.bean.WfMessageCode;
import cafe.workflow.context.ITaskContext;
import cafe.workflow.service.ICompensatedTaskActionHandler;
import cafe.workflow.service.impl.AbstractCaseReceivedService;
import cafe.workflow.service.impl.HumanTaskActivityService;
import cafe.workflow.util.WfConstants;

import com.cybersoft4u.xian.iatoms.common.bean.dto.IAtomsCaseFormDTO;
import com.cybersoft4u.xian.iatoms.services.IIAtomsCaseDetailService;
import com.cybersoft4u.xian.iatoms.services.dmo.CaseDetail;

/**
 * Purpose: 流程处理父类
 * @author RiverJin
 * @since  JDK 1.6
 * @date   2016/09/09
 * @MaintenancePersonnel RiverJin
 */
public abstract class AbstractIAtomsHumanTaskActivityService extends HumanTaskActivityService<IIAtomsCaseDetailService, TaskQueryRequest> {
	private static CafeLog log = CafeLogFactory.getLog(GenericConfigManager.SERVICE, CafeLog.class, AbstractCaseReceivedService.class);
	private String acquireTaskActionAliasCode;
	/**
	 * Constructor:空构造函数
	 */
	public AbstractIAtomsHumanTaskActivityService() {
		
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.service.impl.HumanTaskActivityService#releaseTaskFilters()
	 */
	protected TaskQueryRequest releaseTaskFilters() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Purpose:起案
	 * @author RiverJin
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出错时返回ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext start(SessionContext sessionContext) throws ServiceException {
		try {
			IAtomsCaseFormDTO formDTO = (IAtomsCaseFormDTO)sessionContext.getRequestParameter();
			//取得下一關被指派者
			if (formDTO != null) {
				HumanTaskAssignee nextAssignee = formDTO.getNextAssignee();
				if (nextAssignee == null || !nextAssignee.hasAssignee()) {
					nextAssignee = this.getNextAssignee(formDTO);
					formDTO.setNextAssignee(nextAssignee);
				}
				if (nextAssignee != null && nextAssignee.hasAssignee()) {
					formDTO.getCaseDetail().setAssigneeUser(nextAssignee.toAssigneeUserString());
					formDTO.getCaseDetail().setAssigneeGroup(nextAssignee.toAssigneeGroupString());
				}
			}
			// 新增案件資料至資料庫
			CaseDetail caseDetail = this.getCaseDetailService().createNewCase(formDTO);
			//caseDetail.setId(caseDetail.getId());
		//	formDTO.setCaseId(caseDetail.getId());
			LogonUser initiator = formDTO.getLogonUser();
			//若有外掛額外的任務執行處理器,則執行此額外處理
			if (!CollectionUtils.isEmpty(this.getCompensatedTaskActionHandlers())) {
				log.debug("start to invoke compensated human task action handler...");
				Iterator<ICompensatedTaskActionHandler> handlers = this.getCompensatedTaskActionHandlers().iterator();
				ICompensatedTaskActionHandler handler;
				while (handlers.hasNext()) {
					handler = handlers.next();
					handler.handle(formDTO, caseDetail);
				}	

			}
			//activiti 起案
			ITaskContext taskCtx = this.getHumanTaskActionService().createProcessInstance(formDTO);
			
			if (taskCtx != null && this.getHumanTaskActionService().isAutoAcquireTaskAfterCaseReceived()) {
				try {
					//update case status
					if (!StringUtils.hasText(this.acquireTaskActionAliasCode)) this.acquireTaskActionAliasCode = WfConstants.ACTION_ALIAS_CODE_ACQUIRE_TASK;
					caseDetail.set(	taskCtx, 
									this.acquireTaskActionAliasCode, 
									WfConstants.CASE_STATUS_ACQUIRED, 
									WfConstants.CASE_SUBSTATUS_NONE);

					this.getCaseDetailService().updateCase(caseDetail);
				}catch(Exception e) {
						log.error("acquired task is failed:"+e, e);	
				}
				String redirectStageUrl = null;
				try {
					redirectStageUrl = this.getIdentityService().getFunctionUrl(initiator.getRealmId(), taskCtx.getActivityCode());
				}catch(Exception ee) {}
				if (!StringUtils.hasText(redirectStageUrl)) {
					log.error("The case ("+taskCtx.getCaseId()+") has been acquired, but its relevant function url can't been gotten !");
					throw new ServiceException(WfMessageCode.BPM_TASK_FORM_PAGE_REDIRECT_FAILED);
				}else taskCtx.setRedirectStageUrl(redirectStageUrl);
				
				sessionContext.setResponseResult(taskCtx);
			} 
			sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, WfMessageCode.BPM_INIT_PROCESS_INSTANCE_SUCCESS, new String[]{""}));;

			return sessionContext;
	} catch (Throwable e) {
			log.error("Initiating a new case is failed:" + e, e);
			if (e instanceof ServiceException) throw (ServiceException)e;
			else throw new ServiceException(WfMessageCode.BPM_INIT_PROCESS_INSTANCE_FAILED, new String[]{""}, e);
	}
}
}
