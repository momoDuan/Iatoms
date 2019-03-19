/**
 * 
 */
package com.cybersoft4u.xian.iatoms.services.utils;

import java.util.List;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.identity.RoleParticipant;
import cafe.core.config.GenericConfigManager;
import cafe.core.service.ServiceException;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.workflow.bean.HumanTaskAssignee;
import cafe.workflow.bean.WfMessageCode;
import cafe.workflow.bean.dto.AbstractHumanTaskFormDTO;
import cafe.workflow.service.util.CommonNextAssigneeRetriever;
import cafe.workflow.service.util.INextAssigneeRetriever;

import com.cybersoft4u.xian.iatoms.common.bean.workflow.IAtomsAssigneeInquiryContext;
import com.cybersoft4u.xian.iatoms.services.IIAtomsIdentityService;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO;

/**
 * Purpose: 取得下一關指派人
 * @author RiverJin
 * @since  JDK 1.7
 * @date   2016年9月12日
 * @MaintenancePersonnel RiverJin
 */
public class IAtomsCommonNextAssigneeRetriever<I extends IAtomsAssigneeInquiryContext, F extends AbstractHumanTaskFormDTO> extends CommonNextAssigneeRetriever<I,F> implements INextAssigneeRetriever<I,F> {
	/**
	 * 系統日誌記錄物件
	 */
	private static CafeLog log = CafeLogFactory.getLog(GenericConfigManager.SERVICE, IAtomsCommonNextAssigneeRetriever.class);
	/**
	 * 構造
	 */
	public IAtomsCommonNextAssigneeRetriever() {
			this.setAssigneeInquiryContextClass(IAtomsAssigneeInquiryContext.class);
	}

	/**
	 * 员工DAO
	 */
	private IAdmUserDAO admUserDAO;
	
	/** (non-Javadoc)
	 * @see cafe.workflow.service.util.CommonNextAssigneeRetriever#retrieve(cafe.workflow.context.AssigneeInquiryContext)
	 */
	public HumanTaskAssignee retrieve(I inquiryContext) throws ServiceException {
			if (inquiryContext == null) throw new ServiceException(WfMessageCode.BPM_GET_NEXT_ASSIGNEE_IS_FAILED, new String[]{"輸入參數之指派資訊為空"});
			if (inquiryContext.getUser() == null) throw new ServiceException(WfMessageCode.BPM_GET_NEXT_ASSIGNEE_IS_FAILED, new String[]{"輸入參數之使用者資訊為空"});
			HumanTaskAssignee assignee = new HumanTaskAssignee();
			/**
			 * 邏輯部分根據專案需要開發
			 */
			if (inquiryContext != null) {/*
				//1. 根據所要指派的下一關卡，從功能與角色的表格中取得哪些角色可以處理該關卡
				try {
					if (inquiryContext.getCandidateRoles() == null) {
						List<RoleParticipant> candidateRoles = this.getIdentityService().getActivityRoles(	inquiryContext.getCaseInitiateDate(), 
																											inquiryContext.getUser().getRealmId(),
																											inquiryContext.getNextActivityCode(), 
																											inquiryContext.getBusinessArea());
						inquiryContext.setCandidateRoles(candidateRoles);
						List<RoleParticipant> candidateRoles = null;
						if(StringUtils.hasText(inquiryContext.getProjectId())){
							candidateRoles = this.getProjectDAO().getAvailableGroups(inquiryContext.getProjectId());
						}else if(StringUtils.hasText(inquiryContext.getSalesId())){
							String ids = "";
							String salesId = inquiryContext.getSalesId();
							String [] salesIdArray = salesId.split(CmsConstants.MARK_SEPARATOR);
							for (int i = 0; i < salesIdArray.length; i++) {
								if(StringUtils.hasText(ids)){
									ids += CmsConstants.MARK_SEPARATOR + CmsConstants.SINGLE_QUOTATION_MARKS + salesIdArray[i] + CmsConstants.SINGLE_QUOTATION_MARKS;
								}else{
									ids = CmsConstants.SINGLE_QUOTATION_MARKS + salesIdArray[i] + CmsConstants.SINGLE_QUOTATION_MARKS;
								}
							}
							candidateRoles = this.getEmployeeDAO().getAvailableRoles(ids);
						}
						
						inquiryContext.setCandidateRoles(candidateRoles);
					}	
					assignee = this.retrieve(inquiryContext.getCandidateRoles(), inquiryContext); 
				}catch(Exception e){
					log.error("retrieve","Inquiry Context:"+inquiryContext.toString()+" error:"+e, e);
					if(e instanceof ServiceException) throw (ServiceException)e;
					else throw new ServiceException(WfMessageCode.BPM_GET_NEXT_ASSIGNEE_IS_FAILED, new String[]{"角色群組查詢錯誤"});
				}
			*/}
			return assignee;
	}
	/**
	 * 取得下一關卡被指派者
	 * @param roles 角色清單
	 * @param inquiryContext 下一關被指派查詢條件
	 * @return HumanTaskAssignee
	 * @throws ServiceException
	 */
	protected HumanTaskAssignee retrieve(List<RoleParticipant> roles, I inquiryContext) throws ServiceException {
			if (CollectionUtils.isEmpty(roles))  throw new ServiceException(WfMessageCode.BPM_GET_NEXT_ASSIGNEE_IS_FAILED, new String[]{"角色群組查無資料"});
			if (inquiryContext == null) throw new ServiceException(WfMessageCode.BPM_GET_NEXT_ASSIGNEE_IS_FAILED, new String[]{"輸入參數之指派資訊為空"});
			HumanTaskAssignee assignee = new HumanTaskAssignee(inquiryContext.getNextActivityCode());
			/**
			 * 邏輯部分根據專案需要開發
			 */
			//1. 根據所要指派的下一關卡，從功能與角色的表格中取得哪些角色可以處理該關卡
			/*try {
				
				if(!CollectionUtils.isEmpty(roles)){
					int size = roles.size();
					EmployeeDTO employeeDTO = null;
					for (int i = 0; i < size; i++) {
						employeeDTO = (EmployeeDTO)roles.get(i);
//						assignee.addAssigneeGroup(employeeDTO.getId(), employeeDTO.getEmployeeName());
						assignee.addAssigneeUser(employeeDTO.getAccount(), employeeDTO.getAccount());
					}	
				}
				
			}catch(Exception e){
					log.error("retrieve","Inquiry Context:"+inquiryContext.toString()+" error:"+e, e);
					throw new ServiceException(WfMessageCode.BPM_GET_NEXT_ASSIGNEE_IS_FAILED, new String[]{"業務跨區分區過濾錯誤"});
			}
			if(!assignee.hasAssignee()){
				//未找到角色可以處理該關卡 (功能與角色的表格)
				log.error("retrieve","Inquiry Context:"+inquiryContext.toString()+", assignee is empty!");
				throw new ServiceException(WfMessageCode.BPM_GET_NEXT_ASSIGNEE_IS_FAILED, new String[]{"可處理關卡之角色群組查無資料"});
			}*/
			return assignee;
	}

	public IIAtomsIdentityService getIdentityService() {
		return (IIAtomsIdentityService)super.getIdentityService();
	}
	/**
	 * @return the admUserDAO
	 */
	public IAdmUserDAO getAdmUserDAO() {
		return admUserDAO;
	}
	/**
	 * @param admUserDAO the admUserDAO to set
	 */
	public void setAdmUserDAO(IAdmUserDAO admUserDAO) {
		this.admUserDAO = admUserDAO;
	}

}
