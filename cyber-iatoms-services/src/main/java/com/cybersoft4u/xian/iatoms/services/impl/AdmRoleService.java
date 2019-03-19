package com.cybersoft4u.xian.iatoms.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.Message;
import cafe.core.bean.Parameter;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.dao.DataAccessException;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.convert.SimpleDtoDmoTransformer;
import cafe.core.util.convert.Transformer;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.workflow.bean.dto.ActivitiUserInfoDTO;
import cafe.workflow.bean.dto.MemberShipBaseDTO;
import cafe.workflow.context.bpmn20.activiti.ActivitiWorkflowContext;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmFunctionTypeDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimDepartmentDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.PermissionDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.RolePermissionDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AdmRoleFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.IAtomsHumanPersonnelFormDTO;
import com.cybersoft4u.xian.iatoms.services.IAdmRoleService;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmRoleDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IFunctionTypeDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IPermissionDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IRolePermissionDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmRole;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmRolePermission;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmRolePermissionId;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmSystemLogging;
import com.cybersoft4u.xian.iatoms.services.workflow.impl.IAtomsHumanTaskActivityService;

/** 
 * Purpose: 使用者角色維護service
 * @author CarrieDuan
 * @since  JDK 1.6
 * @date   2016年6月19日
 * @MaintenancePersonnel CarrieDuan
 */
public class AdmRoleService extends IAtomsHumanTaskActivityService implements IAdmRoleService{
	/**
	 * 日誌記錄器  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, AdmRoleService.class);	
	
	/**
	 * 角色权限设定DAO
	 */
	private IAdmRoleDAO admRoleDAO;
	/**
	 * 功能权限DAO
	 */
	private IPermissionDAO permissionDAO;
	/**
	 * 功能類型DAO
	 */
	private IFunctionTypeDAO functionTypeDAO;
	/**
	 * 角色權限
	 */
	private IRolePermissionDAO rolePermissionDAO;
	/**
	 * 使用者
	 */
	private IAdmUserDAO admUserDAO;
	/**
	 * Constructor:無參構造
	 */
	public AdmRoleService() {
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.workflow.impl.IAtomsHumanTaskActivityService#init(cafe.core.context.SessionContext)
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException {
		try {
			AdmRoleFormDTO admRoleFormDTO = (AdmRoleFormDTO) sessionContext.getRequestParameter();
			Message msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(admRoleFormDTO);
			return sessionContext;
		} catch (Exception e) {
			LOGGER.error(".init(SessionContext sessionContext)", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.atoms.services.IAdmRoleService#getRoleList()
	 */
	@Override
	public List<Parameter> getRoleList() throws ServiceException {
		try {
			return this.admRoleDAO.listby();
		} catch (DataAccessException e) {
			LOGGER.error("getRoleList()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("getRoleList()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAdmRoleService#getRoleListByUserId(java.lang.String)
	 */
	@Override
	public List<Parameter> getRoleListByUserId(String userId) throws ServiceException {
		try {
			return this.admRoleDAO.listByUserId(userId);
		} catch (DataAccessException e) {
			LOGGER.error("getRoleListByUserId()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("getRoleListByUserId()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAdmRoleService#getRoleCode()
	 */
	@Override
	public List<Parameter> getRoleCode() throws ServiceException {
		try {
			return this.admRoleDAO.listRoleCode();
		} catch (DataAccessException e) {
			LOGGER.error("getRoleCode()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("getRoleCode()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAdmRoleService#query(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext query(SessionContext sessionContext) throws ServiceException {
		try {
			AdmRoleFormDTO admRoleFormDTO = (AdmRoleFormDTO) sessionContext.getRequestParameter();
			// 查詢符合條件的數據
			List<AdmRoleDTO> admRoleDTOs = this.admRoleDAO.listBy(
					admRoleFormDTO.getQueryRoleCode(),
					admRoleFormDTO.getQueryRoleName(),
					admRoleFormDTO.getRows(), admRoleFormDTO.getPage(),
					admRoleFormDTO.getSort(), admRoleFormDTO.getOrder(),
					Boolean.TRUE);
			Message msg = null;
			if (!CollectionUtils.isEmpty(admRoleDTOs)) {
				for(AdmRoleDTO admRoleDTO : admRoleDTOs){
					//修改創建人與異動人員的名稱。-只保留中文名稱。
					String createdByName = admRoleDTO.getCreatedByName();
					if(StringUtils.hasText(createdByName) && createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) > 0 && createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT) > 0){
						admRoleDTO.setCreatedByName(createdByName.substring(createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) + 1, createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT)));
					}
					String updatedByName = admRoleDTO.getUpdatedByName();
					if(StringUtils.hasText(updatedByName) && updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) > 0 && updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT) > 0){
						admRoleDTO.setUpdatedByName(updatedByName.substring(updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) + 1, updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT)));
					}
				}
				admRoleFormDTO.setList(admRoleDTOs);
				// 查詢記錄數
				Integer count = this.admRoleDAO.count(
						admRoleFormDTO.getQueryRoleCode(),
						admRoleFormDTO.getQueryRoleName());
				admRoleFormDTO.getPageNavigation().setRowCount(count.intValue());
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
			} else {
				// 查無數據，設置訊息
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(admRoleFormDTO);
		} catch (DataAccessException e) {
			LOGGER.error("query() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("query()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAdmRoleService#delete(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext delete(SessionContext sessionContext) throws ServiceException {
		AdmSystemLogging apAuditLog = new AdmSystemLogging();
		try {
			// 通過角色編號刪除角色
			AdmRoleFormDTO admRoleFormDTO = (AdmRoleFormDTO) sessionContext.getRequestParameter();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) admRoleFormDTO.getLogonUser();
			String roleId = admRoleFormDTO.getRoleId();	
			//程式名稱/交易代號/交易名稱
			apAuditLog.setFunctionName(admRoleFormDTO.getUseCaseNo());
			//登入帳號
			apAuditLog.setUserId(logonUser.getId());
			//User端IP/終端機號
			apAuditLog.setIp(logonUser.getFromIp());
			//日期時間
			apAuditLog.setOperationTime(DateTimeUtils.getCurrentTimestamp());
			//sessionId
			apAuditLog.setSessionId(logonUser.getSessionId());
			//紀錄類別
			apAuditLog.setLogCategre(IAtomsConstants.ACTION_DELETE);
			//調用權限刪除方法，刪除該角色所具有的權限
			if (StringUtils.hasText(roleId)) {
				this.rolePermissionDAO.deleteAll(roleId);
			}
			Message msg = null;
			//根據ID查找角色信息，進行刪除操作
			AdmRole role = this.admRoleDAO.findByPrimaryKey(AdmRole.class, roleId);
			if (role != null) {
				this.admRoleDAO.getDaoSupport().delete(role);
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_SUCCESS, new String[]{this.getMyName()});
				apAuditLog.setResult(i18NUtil.getName(IAtomsMessageCode.IATOMS_SUCCESS));
				apAuditLog.setContent(admRoleFormDTO.getLogContent());
				this.admRoleDAO.getDaoSupport().save(apAuditLog);
			} else {
				msg = new Message(Message.STATUS.FAILURE,IAtomsMessageCode.DATA_ALERDAY_UPDATE);
			}
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOGGER.error("delete() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("delete() Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}

	/**
	 * Purpose: 根據角色編號或者角色管理DTO得到儲存角色信息的map集合
	 * @author CrissZhang
	 * @param roleId : 角色編號
	 * @param admRoleDTOs ： 角色管理DTO集合
	 * @return Map<String,Object> : 返回儲存角色信息的map集合
	 */
	private Map<String, Object> getRoleGroup(String roleId, List<AdmRoleDTO> admRoleDTOs){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 有羣組信息
		boolean hasGroup = false;
		// 客服
		boolean isCustomerService = false;
		// CR #2951 廠商客服
		boolean isVendorService = false;
		//Task #3578 客戶廠商客服
		boolean isCusVendorService = false;
		// 客戶
		boolean isCustomer = false;
		// QA
		boolean isQA = false;
		// TMS
		boolean isTMS = false;
		// 部門AGENT
		boolean isAgent = false;
		// 廠商AGENT
		boolean isVendorAgent = false;
		// 工程師
		boolean isEngineer = false;
		List<String> groupList = new ArrayList<String>();
		// 處理已知角色管理DTO的group信息
		if(!CollectionUtils.isEmpty(admRoleDTOs)){
			for(AdmRoleDTO admRoleDTO : admRoleDTOs){
				// 客服
				if(IAtomsConstants.VECTOR_ROLE_ATTRIBUTE.equals(admRoleDTO.getAttribute()) &&
						IAtomsConstants.WORK_FLOW_ROLE_CYBER_AGENT.equals(admRoleDTO.getWorkFlowRole()) &&
						IAtomsConstants.CASE_ROLE.CUSTOMER_SERVICE.getCode().equals(admRoleDTO.getRoleCode())){
					isCustomerService = true;
				// CR #2951 廠商客服
				} else if(IAtomsConstants.VECTOR_ROLE_ATTRIBUTE.equals(admRoleDTO.getAttribute()) &&
						IAtomsConstants.WORK_FLOW_ROLE_CYBER_AGENT.equals(admRoleDTO.getWorkFlowRole()) &&
						IAtomsConstants.CASE_ROLE.VENDOR_SERVICE.getCode().equals(admRoleDTO.getRoleCode())){
					// CR #2951 廠商客服
					isVendorService = true;
				// 客戶
				} else if(IAtomsConstants.CUSTOMER_ROLE_ATTRIBUTE.equals(admRoleDTO.getAttribute()) &&
						IAtomsConstants.WORK_FLOW_ROLE_BANK_AGENT.equals(admRoleDTO.getWorkFlowRole())){
					isCustomer = true;
				// QA
				} else if(IAtomsConstants.VECTOR_ROLE_ATTRIBUTE.equals(admRoleDTO.getAttribute()) &&
						IAtomsConstants.WORK_FLOW_ROLE_CYBER_AGENT.equals(admRoleDTO.getWorkFlowRole()) &&
						IAtomsConstants.ROLE_NAME_QA.equals(admRoleDTO.getRoleCode())){
					isQA = true;
				// TMS
				} else if(IAtomsConstants.VECTOR_ROLE_ATTRIBUTE.equals(admRoleDTO.getAttribute()) &&
						IAtomsConstants.WORK_FLOW_ROLE_CYBER_AGENT.equals(admRoleDTO.getWorkFlowRole()) &&
						IAtomsConstants.ROLE_NAME_TMS.equals(admRoleDTO.getRoleCode())){
					isTMS = true;
				// 部門AGENT				//Task #3583 角色屬性-新增 客戶廠商
				} else if((IAtomsConstants.VECTOR_ROLE_ATTRIBUTE.equals(admRoleDTO.getAttribute()) || IAtomsConstants.CUSTOMER_VECTOR_ROLE_ATTRIBUTE.equals(admRoleDTO.getAttribute())) &&
						(IAtomsConstants.WORK_FLOW_ROLE_DEPT_AGENT.equals(admRoleDTO.getWorkFlowRole()) || IAtomsConstants.WORK_FLOW_ROLE_CUS_DEPT_AGENT.equals(admRoleDTO.getWorkFlowRole()))){
					isAgent = true;
				// 廠商AGENT				//Task #3583 角色屬性-新增 客戶廠商
				} else if((IAtomsConstants.VECTOR_ROLE_ATTRIBUTE.equals(admRoleDTO.getAttribute()) || IAtomsConstants.CUSTOMER_VECTOR_ROLE_ATTRIBUTE.equals(admRoleDTO.getAttribute())) &&
						(IAtomsConstants.WORK_FLOW_ROLE_VENDOR_AGENT.equals(admRoleDTO.getWorkFlowRole()) || IAtomsConstants.WORK_FLOW_ROLE_VENDOR_CUS_AGENT.equals(admRoleDTO.getWorkFlowRole()))){
					isVendorAgent = true;
				// 工程師					//Task #3583 角色屬性-新增 客戶廠商
				} else if((IAtomsConstants.VECTOR_ROLE_ATTRIBUTE.equals(admRoleDTO.getAttribute()) || IAtomsConstants.CUSTOMER_VECTOR_ROLE_ATTRIBUTE.equals(admRoleDTO.getAttribute())) &&
						(IAtomsConstants.WORK_FLOW_ROLE_ENGINEER.equals(admRoleDTO.getWorkFlowRole()) || IAtomsConstants.WORK_FLOW_ROLE_CUS_ENGINEER.equals(admRoleDTO.getWorkFlowRole()))){
					isEngineer = true;
				// CyberAgent
				} else if(IAtomsConstants.VECTOR_ROLE_ATTRIBUTE.equals(admRoleDTO.getAttribute()) &&
						IAtomsConstants.WORK_FLOW_ROLE_CYBER_AGENT.equals(admRoleDTO.getWorkFlowRole()) &&
						IAtomsConstants.WORK_FLOW_ROLE_CYBER_AGENT.equals(admRoleDTO.getRoleCode())){
					isVendorAgent = true;
				} else if (IAtomsConstants.CUSTOMER_VECTOR_ROLE_ATTRIBUTE.equals(admRoleDTO.getAttribute()) &&
						IAtomsConstants.WORK_FLOW_ROLE_CUS_VENDOR_AGENT.equals(admRoleDTO.getWorkFlowRole()) &&
						IAtomsConstants.CASE_ROLE.CUS_VENDOR_SERVICE.getCode().equals(admRoleDTO.getRoleCode())) {
					//Task #3578 客戶廠商客服
					//客戶廠商角色 + 客戶廠商Agent + 角色代碼為CUS_VENDOR_SERVICE == CUS_VENDOR_SERVICE
					isCusVendorService = true;
				} 
			}
		} else {
			// 根據角色編號去查角色羣組信息
			List<AdmRoleDTO> tempAdmRoleDTOs = this.admRoleDAO.getRoleGroup(roleId, false, null, null);
			if(!CollectionUtils.isEmpty(tempAdmRoleDTOs)){
				for(AdmRoleDTO admRoleDTO : tempAdmRoleDTOs){
					// 客服
					if(admRoleDTO.getIsCustomerService()){
						isCustomerService = true;
					// CR #2951 廠商客服
					} else if(admRoleDTO.getIsVendorService()){
						// CR #2951 廠商客服
						isVendorService = true;
					// 客戶
					} else if(admRoleDTO.getIsCustomer()){
						isCustomer = true;
					// QA
					} else if(admRoleDTO.getIsQA()){
						isQA = true;
					// TMS
					} else if(admRoleDTO.getIsTMS()){
						isTMS = true;
						// 部門AGENT
					} else if(admRoleDTO.getIsAgent()){
						isAgent = true;
					// 廠商AGENT
					} else if(admRoleDTO.getIsVendorAgent()){
						isVendorAgent = true;
					// 工程師
					} else if(admRoleDTO.getIsEngineer()){
						isEngineer = true;
					// Task #3578  客戶廠商客服
					} else if(admRoleDTO.getIsCusVendorService()){
						isCusVendorService = true;
					}
				}
			}
		}
		// 有任意一種group信息 // CR #2951 廠商客服  // Task #3578  客戶廠商客服
		if(isCustomerService || isVendorService || isCusVendorService || isCustomer || isQA || isTMS || isAgent || isVendorAgent || isEngineer){
			// 標志位置位
			hasGroup = true;
			// 客服
			if(isCustomerService){
				groupList.add(IAtomsConstants.CASE_ROLE.CUSTOMER_SERVICE.getCode());
			}
			// CR #2951 廠商客服
			if(isVendorService){
				groupList.add(IAtomsConstants.CASE_ROLE.VENDOR_SERVICE.getCode());
			}
			// 客戶
			if(isCustomer){
				groupList.add(IAtomsConstants.CASE_ROLE.CUSTOMER.getCode());
			}
			// QA
			if(isQA){
				groupList.add(IAtomsConstants.CASE_ROLE.QA.getCode());
			}
			// TMS
			if(isTMS){
				groupList.add(IAtomsConstants.CASE_ROLE.TMS.getCode());
			}
			// 部門AGENT
			if(isAgent){
				groupList.add(IAtomsConstants.CASE_ROLE.AGENT.getCode());
			}
			// 廠商AGENT
			if(isVendorAgent){
				groupList.add(IAtomsConstants.CASE_ROLE.VENDOR_AGENT.getCode());
			}
			// 工程師
			if(isEngineer){
				groupList.add(IAtomsConstants.CASE_ROLE.ENGINEER.getCode());
			}
			// Task #3578  客戶廠商客服
			if(isCusVendorService){
				groupList.add(IAtomsConstants.CASE_ROLE.CUS_VENDOR_SERVICE.getCode());
			}
		}
		// 添加標志位信息
		resultMap.put(IAtomsConstants.PARAM_HAVE_ROLE_GROUP, hasGroup);
		// 添加group信息
		resultMap.put(IAtomsConstants.PARAM_ROLE_GROUP_LIST, groupList);
		return resultMap;
	}
	
	/**
	 * 
	 * Purpose:根據角色編號以及使用者信息集合得到所有相關使用者角色信息的map集合
	 * @author CrissZhang
	 * @param roleId
	 * @param userIdMap ：存放使用者帳號的map集合
	 * @return Map<String,Object> : 返回儲存角色信息的map集合
	 */
	private Map<String, Object> getUsersRoleGroup(String roleId, Map<String, String> userIdMap){
		// 存放使用者編號的集合
		List<String> userIdList = new ArrayList<String>();
		if(!CollectionUtils.isEmpty(userIdMap)){
			for(String key : userIdMap.keySet()){
				userIdList.add(key);
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 臨時使用的map集合
		Map<String, Object> tempMap = null;
		// 有羣組信息
		boolean hasGroup = false;
		// 客服
		boolean isCustomerService = false;
		
		// CR #2951 廠商客服
		boolean isVendorService = false;
		//Task #3578 客戶廠商客服
		boolean isCusVendorService = false;
		// 客戶
		boolean isCustomer = false;
		// QA
		boolean isQA = false;
		// TMS
		boolean isTMS = false;
		// 部門AGENT
		boolean isAgent = false;
		// 廠商AGENT
		boolean isVendorAgent = false;
		// 工程師
		boolean isEngineer = false;
		List<String> groupList = null;
		List<AdmRoleDTO> admRoleDTOs = this.admRoleDAO.getRoleGroup(null, true, roleId, userIdList);
		boolean isAddGroup = false;
		if(!CollectionUtils.isEmpty(admRoleDTOs)){
			for(int i = 0; i < admRoleDTOs.size(); i++){
				// 客服
				if(admRoleDTOs.get(i).getIsCustomerService()){
					isCustomerService = true;
				// CR #2951 廠商客服
				} else if(admRoleDTOs.get(i).getIsVendorService()){
					// CR #2951 廠商客服
					isVendorService = true;
				// 客戶
				} else if(admRoleDTOs.get(i).getIsCustomer()){
					isCustomer = true;
				// QA
				} else if(admRoleDTOs.get(i).getIsQA()){
					isQA = true;
				// 部門AGENT
				} else if(admRoleDTOs.get(i).getIsAgent()){
					isAgent = true;
				// 廠商AGENT
				} else if(admRoleDTOs.get(i).getIsVendorAgent()){
					isVendorAgent = true;
				// 工程師
				} else if(admRoleDTOs.get(i).getIsEngineer()){
					isEngineer = true;
				// Task #3578 客戶廠商客服
				} else if(admRoleDTOs.get(i).getIsCusVendorService()){
					isCusVendorService = true;
				}
				// 集合大小只有一個時直接添加
				if(admRoleDTOs.size() == 1){
					isAddGroup = true;
				} else {
					// 不是集合的最後一個元素，且與下一個元素帳號不同/最後一個元素直接添加
					if(((i != (admRoleDTOs.size() -1)) && (!admRoleDTOs.get(i).getAccount().equals(admRoleDTOs.get(i + 1).getAccount()))) 
							|| (i == (admRoleDTOs.size() -1))){
						isAddGroup = true;
					}
				}
				// 添加group
				if(isAddGroup){
					isAddGroup = false;
					tempMap = new HashMap<String, Object>();
					groupList = new ArrayList<String>();
					// CR #2951 廠商客服
					if(isCustomerService || isVendorService || isCusVendorService || isCustomer || isQA || isTMS || isAgent || isVendorAgent || isEngineer){
						hasGroup = true;
						// 客服
						if(isCustomerService){
							groupList.add(IAtomsConstants.CASE_ROLE.CUSTOMER_SERVICE.getCode());
						}
						// CR #2951 廠商客服
						if(isVendorService){
							groupList.add(IAtomsConstants.CASE_ROLE.VENDOR_SERVICE.getCode());
						}
						// 客戶
						if(isCustomer){
							groupList.add(IAtomsConstants.CASE_ROLE.CUSTOMER.getCode());
						}
						// QA
						if(isQA){
							groupList.add(IAtomsConstants.CASE_ROLE.QA.getCode());
						}
						// TMS
						if(isTMS){
							groupList.add(IAtomsConstants.CASE_ROLE.TMS.getCode());
						}
						// 部門AGENT
						if(isAgent){
							groupList.add(IAtomsConstants.CASE_ROLE.AGENT.getCode());
						}
						// 廠商AGENT
						if(isVendorAgent){
							groupList.add(IAtomsConstants.CASE_ROLE.VENDOR_AGENT.getCode());
						}
						// 工程師
						if(isEngineer){
							groupList.add(IAtomsConstants.CASE_ROLE.ENGINEER.getCode());
						}
						// Task #3578  客戶廠商客服
						if(isCusVendorService){
							groupList.add(IAtomsConstants.CASE_ROLE.CUS_VENDOR_SERVICE.getCode());
						}
					}
					tempMap.put(IAtomsConstants.PARAM_HAVE_ROLE_GROUP, hasGroup);
					tempMap.put(IAtomsConstants.PARAM_ROLE_GROUP_LIST, groupList);
					// 集合添加元素
					resultMap.put(admRoleDTOs.get(i).getAccount(), tempMap);
					// 重置標志位
					hasGroup = false;
					isCustomerService = false;
					
					// CR #2951 廠商客服
					isVendorService = false;
					// Task #3578  客戶廠商客服
					isCusVendorService = false;
					
					isCustomer= false;
					isQA = false;
					isTMS = false;
					isAgent = false;
					isVendorAgent = false;
					isEngineer = false;
				}
			}
		}
		// 處理無其他group信息的使用者
		if(!CollectionUtils.isEmpty(userIdMap) && !CollectionUtils.isEmpty(resultMap)){
			for(String key : userIdMap.keySet()){
				if(!resultMap.containsKey(userIdMap.get(key))){
					tempMap = new HashMap<String, Object>();
					tempMap.put(IAtomsConstants.PARAM_HAVE_ROLE_GROUP, false);
					tempMap.put(IAtomsConstants.PARAM_ROLE_GROUP_LIST, new ArrayList<String>());
					resultMap.put(userIdMap.get(key), tempMap);
				}
			}
		}
		return resultMap;
	}		
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAdmRoleService#save(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext save(SessionContext sessionContext) throws ServiceException {
		String roleId = null;
		Message msg = null;
		try {
			AdmRoleFormDTO admRoleFormDTO = (AdmRoleFormDTO) sessionContext.getRequestParameter();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) admRoleFormDTO.getLogonUser();
			AdmSystemLogging apAuditLog = new AdmSystemLogging();
			AdmRoleDTO admRoleDTO = admRoleFormDTO.getAdmRoleDTO();			
			roleId = admRoleDTO.getRoleId();
			//驗證角色代碼角色代碼是否重複
			Boolean isRepeat = admRoleDAO.checkRepeat(admRoleDTO.getRoleCode(), null, roleId);
			if (isRepeat.booleanValue()) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.ROLE_CODE_REPEATED);
				sessionContext.setReturnMessage(msg);
				return sessionContext;
			}
			//驗證角色代碼角色名稱是否重複
			isRepeat = admRoleDAO.checkRepeat(null, admRoleDTO.getRoleName(), roleId);
			if (isRepeat.booleanValue()) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.ROLE_NAME_REPEATED);
				sessionContext.setReturnMessage(msg);
				return sessionContext;
			}
			AdmRole admRole = null;			
			// 如果角色編號不為空，屬於修改
			if (StringUtils.hasText(roleId)) {
				List<AdmRoleDTO> newAdmRoleDTOs = new ArrayList<AdmRoleDTO>();
				admRole = admRoleDAO.findByPrimaryKey(AdmRole.class, roleId);
				List<String> oldGroupList = null;
				String oldRoleGroup = null;
				List<String> newGroupList = null;
				String newRoleGroup = null;

				IAtomsHumanPersonnelFormDTO personFormDTO = null;
				ActivitiWorkflowContext activitiWorkflowContext = new ActivitiWorkflowContext(logonUser);
				newAdmRoleDTOs.add(admRoleDTO);
				// 當前角色group
				Map<String, Object> newRoleMap = this.getRoleGroup(null, newAdmRoleDTOs);
				// 舊的角色group
				Map<String, Object> oldRoleMap = this.getRoleGroup(roleId, null);
				// 編輯前屬於6種羣組或者編輯後屬於6種羣組
				if((Boolean) oldRoleMap.get(IAtomsConstants.PARAM_HAVE_ROLE_GROUP) || (Boolean) newRoleMap.get(IAtomsConstants.PARAM_HAVE_ROLE_GROUP)){
					// 編輯前屬於
					if((Boolean) oldRoleMap.get(IAtomsConstants.PARAM_HAVE_ROLE_GROUP)){
						oldGroupList = ((List<String>) oldRoleMap.get(IAtomsConstants.PARAM_ROLE_GROUP_LIST));
						oldRoleGroup = oldGroupList.get(0);
					}
					// 編輯後屬於
					if((Boolean) newRoleMap.get(IAtomsConstants.PARAM_HAVE_ROLE_GROUP)){
						newGroupList = ((List<String>) newRoleMap.get(IAtomsConstants.PARAM_ROLE_GROUP_LIST));
						newRoleGroup = newGroupList.get(0);
					}
					// 直接跳出
					boolean isBreak = false;
					if((Boolean) oldRoleMap.get(IAtomsConstants.PARAM_HAVE_ROLE_GROUP) && (Boolean) newRoleMap.get(IAtomsConstants.PARAM_HAVE_ROLE_GROUP)
							&& oldRoleGroup.equals(newRoleGroup)){
						isBreak = true;
					}
					// 繼續執行
					if(!isBreak){
						List<AdmUserDTO> admUerDTOs = this.admUserDAO.getUserByRoleId(roleId);
						Map<String, String> userIdMap = new HashMap<String, String>();
						if(!CollectionUtils.isEmpty(admUerDTOs)){
							for(AdmUserDTO admUserDTO : admUerDTOs){
								userIdMap.put(admUserDTO.getUserId(), admUserDTO.getAccount());
							}
							Map<String, Object> resultMap = this.getUsersRoleGroup(roleId, userIdMap);
							Map<String, Object> tempMap = null;
							MemberShipBaseDTO memberShipBaseDTO = null;
							List<String> userGroupList = null;
							// 是否有移除角色
							boolean removeRole = false;
							// 是否增加角色
							boolean addRole = false;
							if(!CollectionUtils.isEmpty(resultMap)){
								for(String key : resultMap.keySet()){
									removeRole = false;
									addRole = false;
									tempMap = (Map<String, Object>) resultMap.get(key);
									personFormDTO = new IAtomsHumanPersonnelFormDTO();
									personFormDTO.setWorkflowContext(activitiWorkflowContext);
									personFormDTO.setActivitiUserId(key);
									ActivitiUserInfoDTO activitiUserInfo = super.getUserInfoById(personFormDTO);
									if(activitiUserInfo != null){
										// 編輯前不屬於 編輯後屬於
										if(!(Boolean) oldRoleMap.get(IAtomsConstants.PARAM_HAVE_ROLE_GROUP) && (Boolean) newRoleMap.get(IAtomsConstants.PARAM_HAVE_ROLE_GROUP)){
											// 如果有其他group，判斷其他group是否包含當前group
											if((Boolean) tempMap.get(IAtomsConstants.PARAM_HAVE_ROLE_GROUP)){
												userGroupList = ((List<String>) tempMap.get(IAtomsConstants.PARAM_ROLE_GROUP_LIST));
												// 該用戶其他group不包含增加項
												if(!CollectionUtils.isEmpty(userGroupList) && !userGroupList.toString().contains(newRoleGroup)){
													addRole = true;
												}
											// 無其他group直接增加
											} else {
												addRole = true;
											}
										// 編輯前屬於 編輯後不屬於
										} else if((Boolean) oldRoleMap.get(IAtomsConstants.PARAM_HAVE_ROLE_GROUP) && !(Boolean) newRoleMap.get(IAtomsConstants.PARAM_HAVE_ROLE_GROUP)){
											// 如果有其他group，判斷其他group是否包含當前group
											if((Boolean) tempMap.get(IAtomsConstants.PARAM_HAVE_ROLE_GROUP)){
												userGroupList = ((List<String>) tempMap.get(IAtomsConstants.PARAM_ROLE_GROUP_LIST));
												// 該用戶其他group不包含刪除項
												if(!CollectionUtils.isEmpty(userGroupList) && !userGroupList.toString().contains(oldRoleGroup)){
													removeRole = true;
												}
											// 無其他group直接刪除
											} else {
												removeRole = true;
											}
										// 編輯前屬於 編輯後屬於
										} else {
											// 如果有其他group，判斷其他group是否包含當前group
											if((Boolean) tempMap.get(IAtomsConstants.PARAM_HAVE_ROLE_GROUP)){
												userGroupList = ((List<String>) tempMap.get(IAtomsConstants.PARAM_ROLE_GROUP_LIST));
												if(!CollectionUtils.isEmpty(userGroupList)){
													// 有編輯之前的角色
													if(userGroupList.toString().contains(oldRoleGroup)){
														// 有編輯之後的角色
														if(userGroupList.toString().contains(newRoleGroup)){
															// 不處理
														// 沒有編輯之後的角色
														} else {
															addRole = true;
														}
													// 沒有編輯之前的角色
													} else {
														// 有編輯之後的角色
														if(userGroupList.toString().contains(newRoleGroup)){
															removeRole = true;
														// 沒有編輯之後的角色
														} else {
															removeRole = true;
															addRole = true;
														}
													}
												}
											// 無其他group 刪編輯之前 增加編輯之後
											} else {
												removeRole = true;
												addRole = true;
											}
										}
										// 是否刪除編輯之前羣組
										if(removeRole){
											personFormDTO = new IAtomsHumanPersonnelFormDTO();
											personFormDTO.setWorkflowContext(activitiWorkflowContext);
											personFormDTO.setActivitiUserId(key);
											personFormDTO.setActivitiGroupId(oldRoleGroup);
											super.removeUserForGroup(personFormDTO);
										}
										// 是否增加編輯之後羣組
										if(addRole){
											personFormDTO = new IAtomsHumanPersonnelFormDTO();
											personFormDTO.setWorkflowContext(activitiWorkflowContext);
											memberShipBaseDTO = new MemberShipBaseDTO();
											memberShipBaseDTO.setUserId(key);
											personFormDTO.setMemberShipBaseDTO(memberShipBaseDTO);
											personFormDTO.setActivitiUserId(key);
											personFormDTO.setActivitiGroupId(newRoleGroup);
											super.addUserToGroup(personFormDTO);
										}
									}
								}
							} else {
								for(String key : userIdMap.keySet()){
									removeRole = false;
									addRole = false;
									personFormDTO = new IAtomsHumanPersonnelFormDTO();
									personFormDTO.setWorkflowContext(activitiWorkflowContext);
									personFormDTO.setActivitiUserId(userIdMap.get(key));
									ActivitiUserInfoDTO activitiUserInfo = super.getUserInfoById(personFormDTO);
									if(activitiUserInfo != null){
										// 編輯前不屬於 編輯後屬於
										if(!(Boolean) oldRoleMap.get(IAtomsConstants.PARAM_HAVE_ROLE_GROUP) && (Boolean) newRoleMap.get(IAtomsConstants.PARAM_HAVE_ROLE_GROUP)){
											addRole = true;
										// 編輯前屬於 編輯後不屬於
										} else if((Boolean) oldRoleMap.get(IAtomsConstants.PARAM_HAVE_ROLE_GROUP) && !(Boolean) newRoleMap.get(IAtomsConstants.PARAM_HAVE_ROLE_GROUP)){
											removeRole = true;
										// 編輯前屬於 編輯後屬於
										} else {
											removeRole = true;
											addRole = true;
										}
										// 是否刪除編輯之前羣組
										if(removeRole){
											personFormDTO = new IAtomsHumanPersonnelFormDTO();
											personFormDTO.setWorkflowContext(activitiWorkflowContext);
											personFormDTO.setActivitiUserId(userIdMap.get(key));
											personFormDTO.setActivitiGroupId(oldRoleGroup);
											super.removeUserForGroup(personFormDTO);
										}
										// 是否增加編輯之後羣組
										if(addRole){
											personFormDTO = new IAtomsHumanPersonnelFormDTO();
											personFormDTO.setWorkflowContext(activitiWorkflowContext);
											memberShipBaseDTO = new MemberShipBaseDTO();
											memberShipBaseDTO.setUserId(userIdMap.get(key));
											personFormDTO.setMemberShipBaseDTO(memberShipBaseDTO);
											personFormDTO.setActivitiUserId(userIdMap.get(key));
											personFormDTO.setActivitiGroupId(newRoleGroup);
											super.addUserToGroup(personFormDTO);
										}
									}
								}
							}
						}
					}
				}
				admRole.setRoleName(admRoleDTO.getRoleName());
				admRole.setRoleDesc(admRoleDTO.getRoleDesc());
				admRole.setAttribute(admRoleDTO.getAttribute());
				admRole.setWorkFlowRole(admRoleDTO.getWorkFlowRole());
				// 修改設置異動人員相關信息
				admRole.setUpdatedById(logonUser.getId());
				admRole.setUpdatedByName(logonUser.getName());
				admRole.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
				this.admRoleDAO.update(admRole);
				// 修改成功的訊息
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.UPDATE_SUCCESS, new String[]{this.getMyName()});
				//bug2359 update by 2017/09/06
				apAuditLog.setLogCategre(IAtomsConstants.PARAM_UPDATE + IAtomsConstants.MARK_NO + IAtomsConstants.ACTION_SAVE);
			} else {
				Transformer transformer = new SimpleDtoDmoTransformer();
				// 將DTO轉成DMO
				admRole = (AdmRole) transformer.transform(admRoleDTO, new AdmRole());
				String id =	this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_ADM_ROLE);
				admRole.setRoleId(id);
				// 新增設置創建人員相關信息
				admRole.setCreatedById(logonUser.getId());
				admRole.setCreatedByName(logonUser.getName());
				admRole.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
				// 修改設置異動人員相關信息
				admRole.setUpdatedById(logonUser.getId());
				admRole.setUpdatedByName(logonUser.getName());
				admRole.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
				this.admRoleDAO.save(admRole);
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INSERT_SUCCESS, new String[]{this.getMyName()});
				//bug2359 update by 2017/09/06
				apAuditLog.setLogCategre(IAtomsConstants.PARAM_CREATE + IAtomsConstants.MARK_NO + IAtomsConstants.ACTION_SAVE);
			}
			//程式名稱/交易代號/交易名稱
			apAuditLog.setFunctionName(admRoleFormDTO.getUseCaseNo());
			//登入帳號
			apAuditLog.setUserId(logonUser.getId());
			//User端IP/終端機號
			apAuditLog.setIp(logonUser.getFromIp());
			//日期時間
			apAuditLog.setOperationTime(DateTimeUtils.getCurrentTimestamp());
			//sessionId
			apAuditLog.setSessionId(logonUser.getSessionId());
			//紀錄類別
			apAuditLog.setContent(admRoleFormDTO.getLogContent());
			apAuditLog.setResult(i18NUtil.getName(IAtomsMessageCode.IATOMS_SUCCESS));
			//apAuditLog.setLogCategre(IAtomsConstants.ACTION_SAVE);
			this.admRoleDAO.getDaoSupport().save(apAuditLog);
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOGGER.error("save()", "DataAccess Exception", e);
			// 如果角色編號不為空並且不為0
			if (StringUtils.hasText(roleId)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.UPDATE_FAILURE, new String[]{this.getMyName()});
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INSERT_FAILURE, new String[]{this.getMyName()});
			}
			throw new ServiceException(msg);
		} catch (Exception e) {
			LOGGER.error("save() Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAdmRoleService#initDetail(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext initDetail(SessionContext sessionContext) throws ServiceException {
		Map<String, List<String>> result;
		try {
			AdmRoleFormDTO admRoleFormDTO = (AdmRoleFormDTO) sessionContext.getRequestParameter();
			String roleId = admRoleFormDTO.getRoleId();
			//根據角色id獲取角色信息
			AdmRoleDTO admRoleDTO = this.admRoleDAO.getRoleById(roleId);
			//獲取角色權限
			List<PermissionDTO> permissionDTOs = this.permissionDAO.listByFunctionIds(null);
			result = new HashMap<String, List<String>>();
			String prevType = null;
			String functionId = null;
			int length = permissionDTOs.size();
			PermissionDTO permissionDTO = null;
			List<String> editFields = null;
			for (int i = 0; i < length; i++) {
				permissionDTO = permissionDTOs.get(i);
				functionId = permissionDTO.getFunctionId();
				if (i == 0) {
					prevType = functionId;
					editFields = new ArrayList<String>();
				}
				else if (!functionId.equals(prevType)) {
					result.put(prevType, editFields);
					prevType = permissionDTO.getFunctionId();
					editFields = new ArrayList<String>();					
				}
				editFields.add(permissionDTO.getAccessRight().toLowerCase());
				if (i == length - 1) {
					result.put(prevType, editFields);
				}
			}
			admRoleFormDTO.setFunctionPermissions(result);
			admRoleFormDTO.setRoleCode(admRoleDTO.getRoleCode());
			admRoleFormDTO.setRoleName(admRoleDTO.getRoleName());
			Message msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(admRoleFormDTO);
		} catch (DataAccessException e) {
			LOGGER.error("initDetail() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("initDetail() Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return  sessionContext;
	}
	

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAdmRoleService#loadDlgData(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext loadDlgData(SessionContext sessionContext) throws ServiceException {
		try {
			Map map = new HashMap();
			AdmRoleFormDTO admRoleFormDTO = (AdmRoleFormDTO) sessionContext.getRequestParameter();
			List<AdmFunctionTypeDTO> functionTypeDTOs = null;
			String roleId = admRoleFormDTO.getRoleId();
			if (StringUtils.hasText(roleId)) {
				//獲取當前角色的權限
				functionTypeDTOs = this.functionTypeDAO.listFunctionTypeDTOsByRoleId(roleId);
				for(AdmFunctionTypeDTO admFunctionTypeDTO : functionTypeDTOs){
					String createdByName = admFunctionTypeDTO.getCreatedByName();
					if(StringUtils.hasText(createdByName) && createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) > 0 && createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT) > 0){
						admFunctionTypeDTO.setCreatedByName(createdByName.substring(createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) + 1, createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT)));
					}
					String updatedByName = admFunctionTypeDTO.getUpdatedByName();
					if(StringUtils.hasText(updatedByName) && updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) > 0 && updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT) > 0){
						admFunctionTypeDTO.setUpdatedByName(updatedByName.substring(updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) + 1, updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT)));
					}
				}
			}
			if (functionTypeDTOs == null) {
				functionTypeDTOs = new ArrayList<AdmFunctionTypeDTO>();
			}
			map.put(IAtomsConstants.QUERY_PAGE_ROWS, functionTypeDTOs);
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, Integer.valueOf(functionTypeDTOs.size()));
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
		} catch (DataAccessException e) {
			LOGGER.error("loadDetailGridData() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("loadDetailGridData() Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return  sessionContext;
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAdmRoleService#getParentFunction(cafe.core.context.SessionContext)
	 */
	public List<Parameter> getParentFunction() throws ServiceException {
		try {
			return this.functionTypeDAO.listFunctionByFunctionId(null);
		} catch (DataAccessException e) {
			LOGGER.error("getParentFunction() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("getParentFunction() Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAdmRoleService#getFunctionByParentId(cafe.core.context.SessionContext)
	 */
	public List<Parameter> getFunctionByParentId(MultiParameterInquiryContext parameterInquiryContext) throws ServiceException {
		try {
			String parentId = parameterInquiryContext.getParameter(AdmFunctionTypeDTO.ATTRIBUTE.PARENT_FUNCTION_ID.getValue()).toString();
			return this.functionTypeDAO.listFunctionByFunctionId(parentId);
		} catch (DataAccessException e) {
			LOGGER.error("getFunctionByParentId() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("getFunctionByParentId() Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAdmRoleService#saveRolePermission(cafe.core.context.SessionContext)
	 */
	public SessionContext saveRolePermission(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		String functionId = null;
		//用於記錄已存在角色權限和新角色權限相同的權限
		List<RolePermissionDTO> sameRolePermissionDTOs = new ArrayList<RolePermissionDTO>();
		try {
			AdmSystemLogging apAuditLog = new AdmSystemLogging();
			//獲取formDTO
			AdmRoleFormDTO admRoleFormDTO = (AdmRoleFormDTO) sessionContext.getRequestParameter();
			//獲取當前登錄信息
			IAtomsLogonUser logonUser = (IAtomsLogonUser) admRoleFormDTO.getLogonUser();
			//獲取新的角色權限信息
			List<PermissionDTO> currentPermissionDTOs = admRoleFormDTO.getPermissionDTOs();
			//儲存需要進行增加、刪除的角色權限的功能ID
			Set<String> updateFunctionIds = new HashSet<String>();
			List<PermissionDTO> permissionDTOs = null;
			if (currentPermissionDTOs != null) {
				String roleId = admRoleFormDTO.getRoleId();
				//获取选取的functionId
				String functionIds = "";
				//獲取的功能權限ID
				String permissionId = "";
				//新增Table--->ROLE_PERMISSION数据处理
				if(!CollectionUtils.isEmpty(currentPermissionDTOs)){
					for (PermissionDTO permissionDTO : currentPermissionDTOs) {
						functionIds =functionIds.concat(IAtomsConstants.MARK_QUOTES).concat(permissionDTO.getFunctionId())
								.concat(IAtomsConstants.MARK_QUOTES).concat(IAtomsConstants.MARK_SEPARATOR);
					}
					//根據已有的functionId
					permissionDTOs = this.permissionDAO.listByFunctionIds(functionIds.substring(0, functionIds.length()-1)); 
				}
				//獲取每個角色權限的ID
				if (!CollectionUtils.isEmpty(currentPermissionDTOs)) {
					for (PermissionDTO permissionDTO : currentPermissionDTOs) {
						permissionId = getRolePermissionId(permissionDTOs, permissionDTO.getFunctionId(), permissionDTO.getAccessRight().toUpperCase());
						permissionDTO.setId(permissionId);
					}
				}
				//先查找之前所有的權限
				List<RolePermissionDTO> oldRolePermissionDTOs = rolePermissionDAO.listRolePermissionByRoleId(roleId);		
				//比較新獲取的角色權限以及舊的角色權限，獲取相同的角色權限，以及需要刪除的和增加的
				for (int i = 0; i < oldRolePermissionDTOs.size(); i++) {
					for (int j = 0; j < currentPermissionDTOs.size(); j++) {
						if (oldRolePermissionDTOs.get(i).getPermissionId().equals(currentPermissionDTOs.get(j).getId())) {
							sameRolePermissionDTOs.add(oldRolePermissionDTOs.get(i));
							oldRolePermissionDTOs.remove(i);
							currentPermissionDTOs.remove(j);
							i--;
							break;
						}
					}
				}
				//用於臨時記錄角色權限ID
				AdmRolePermissionId rolePermissionId;
				//原先的有, 現在沒有, 刪除
				for (RolePermissionDTO removeRolePermissionDTO : oldRolePermissionDTOs) {
					updateFunctionIds.add(removeRolePermissionDTO.getFunctionId());
				    rolePermissionId = new AdmRolePermissionId(removeRolePermissionDTO.getRoleId(), removeRolePermissionDTO.getPermissionId());
				    if (rolePermissionId != null) {
				    	this.rolePermissionDAO.getDaoSupport().delete(rolePermissionId,AdmRolePermission.class);
				    }
				}
				AdmRolePermission rolePermission = null;
				//之前沒有, 現在有, 新增
				for (PermissionDTO permissionDTO: currentPermissionDTOs) {
					updateFunctionIds.add(permissionDTO.getFunctionId());
					if (permissionDTO.getId() != null) {
						rolePermission = getAdmRolePermission(permissionDTO.getId(), roleId, logonUser);
						this.rolePermissionDAO.getDaoSupport().save(rolePermission);						
					}
				}
				this.rolePermissionDAO.getDaoSupport().flush();
				Object[] updateIds = updateFunctionIds.toArray();
				//需要進行修改的據
				for (RolePermissionDTO rolePermissionDTO : sameRolePermissionDTOs) {
					for (Object updateId : updateIds) {
						if (StringUtils.hasText(updateId.toString()) && updateId.equals(rolePermissionDTO.getFunctionId())) {
							rolePermission = getAdmRolePermission(rolePermissionDTO.getPermissionId(), roleId, logonUser);
							this.permissionDAO.getDaoSupport().saveOrUpdate(rolePermission);							
						}
					}
				}
				this.permissionDAO.getDaoSupport().flush();
				if (rolePermission != null) {
					this.getFunctionTypeDAO().getDaoSupport().evict(rolePermission);
				}
				//程式名稱/交易代號/交易名稱
				apAuditLog.setFunctionName(admRoleFormDTO.getUseCaseNo());
				//登入帳號
				apAuditLog.setUserId(logonUser.getId());
				//User端IP/終端機號
				apAuditLog.setIp(logonUser.getFromIp());
				//日期時間
				apAuditLog.setOperationTime(DateTimeUtils.getCurrentTimestamp());
				//sessionId
				apAuditLog.setSessionId(logonUser.getSessionId());
				//紀錄類別
				apAuditLog.setLogCategre(IAtomsConstants.ACTION_SAVE_DETAIL);
				apAuditLog.setResult(i18NUtil.getName(IAtomsMessageCode.IATOMS_SUCCESS));
				apAuditLog.setContent(admRoleFormDTO.getLogContent());
				this.admRoleDAO.getDaoSupport().save(apAuditLog);
				this.admRoleDAO.getDaoSupport().flush();
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.SAVE_ROLE_PERMISSION_SUCCESS);
			}
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOGGER.error("save() DataAccess Exception:", e);
			msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()});
			throw new ServiceException(msg);
		} catch (Exception e) {
			LOGGER.error("save() Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}
	
	/**
	 * Purpose:獲取角色權限DMO
	 * @author CarrieDuan
	 * @param permissionId ：角色權限ID
	 * @param roleId ：角色ID
	 * @param logonUser ：當前登錄者信息
	 * @return AdmRolePermission ：返回角色權限DMO
	 */
	private AdmRolePermission getAdmRolePermission(String permissionId, String roleId, IAtomsLogonUser logonUser) {
		AdmRolePermission tempRolePermission = null;
		AdmRolePermissionId rolePermissionId;
		rolePermissionId = new AdmRolePermissionId(roleId, permissionId);
/*		tempRolePermission = new AdmRolePermission(rolePermissionId, IAtomsConstants.ROLE_PERMISSION_TYPE_ROLE, 
								IAtomsConstants.ROLE_PERMISSION_ACTIVIT, logonUser.getId(), logonUser.getName(), 
								DateTimeUtils.getCurrentTimestamp());*/
		tempRolePermission = new AdmRolePermission(rolePermissionId, logonUser.getId(), logonUser.getName(), 
				DateTimeUtils.getCurrentTimestamp());
		return tempRolePermission;
	}
	
	/**
	 * Purpose: 根據functionId,accessRight 獲得permissionId
	 * @author evanliu
	 * @param permissionDTOs:權限DTO List
	 * @param functionId: 功能編號
	 * @param accessRight: 功能權限
	 * @return String: 權限編號
	 */
	private String getRolePermissionId(List<PermissionDTO> permissionDTOs, String functionId,String accessRight) {
		for (PermissionDTO permissionDTO : permissionDTOs) {
			if (StringUtils.hasText(permissionDTO.getFunctionId()) && StringUtils.hasText(permissionDTO.getAccessRight())) {
				if (permissionDTO.getFunctionId().equals(functionId) && permissionDTO.getAccessRight().equals(accessRight)) {
					return permissionDTO.getId();
				}
			}
		}
		return null;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAdmRoleService#getWorkFlowRole(cafe.core.context.MultiParameterInquiryContext)
	 */
	public List<Parameter> getWorkFlowRoleList(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		List<Parameter> workFlows = null;
		try {
			String attribute = (String) inquiryContext.getParameter(AdmRoleDTO.ATTRIBUTE.ATTRIBUTE.getValue());
			workFlows = this.admRoleDAO.listWorkFlowByAttribute(attribute);
		} catch (DataAccessException e) {
			LOGGER.error("getWorkFlowRoleList() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("getWorkFlowRoleList() Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return workFlows;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAdmRoleService#checkUseRole(cafe.core.context.MultiParameterInquiryContext)
	 */
	public Boolean checkUseRole(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		Boolean isUse = true;
		try {
			String roleId = (String) inquiryContext.getParameter(AdmRoleDTO.ATTRIBUTE.ROLE_ID.getValue());
			//如果存在roleId,则进行判断该role是否已经被员工使用
			if (StringUtils.hasText(roleId)) {
				isUse = this.admRoleDAO.checkUseRole(roleId);
			}
		} catch (DataAccessException e) {
			LOGGER.error("checkUseRole() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("checkUseRole() Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return isUse;
	}
	
	public void saveLogMessage(AdmRoleFormDTO formDTO) {
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAdmRoleService#saveLog(cafe.core.context.MultiParameterInquiryContext)
	 */
	public void saveLog(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		AdmSystemLogging apAuditLog = new AdmSystemLogging();
		try {
			String actionId = (String) inquiryContext.getParameter(IAtomsConstants.FIELD_ACTION_ID);
			IAtomsLogonUser logonUser = (IAtomsLogonUser) inquiryContext.getParameter(AdmRoleFormDTO.PARAM_USER_LOGON_USER);
			String ip = "";
			if(logonUser != null){
				ip = logonUser.getFromIp();
			}
			String ucNo = (String) inquiryContext.getParameter(AdmRoleFormDTO.PARAM_USER_UC_NO);
			//程式名稱/交易代號/交易名稱
			apAuditLog.setFunctionName(ucNo);
			//登入帳號
			apAuditLog.setUserId(logonUser.getId());
			//User端IP/終端機號
			apAuditLog.setIp(ip);
			//日期時間
			apAuditLog.setOperationTime(DateTimeUtils.getCurrentTimestamp());
			//sessionId
			apAuditLog.setSessionId(logonUser.getSessionId());
			if (IAtomsConstants.ACTION_UPDATE.equals(actionId)) {
				apAuditLog.setLogCategre(IAtomsConstants.ACTION_UPDATE);
			} else if (IAtomsConstants.ACTION_QUERY.equals(actionId)) {
				apAuditLog.setLogCategre(IAtomsConstants.ACTION_QUERY);
			} else if (IAtomsConstants.ACTION_INIT_DETAIL.equals(actionId)) {
				apAuditLog.setLogCategre(IAtomsConstants.ACTION_INIT_DETAIL);
			}
			
			apAuditLog.setResult(i18NUtil.getName(IAtomsMessageCode.IATOMS_SUCCESS));
			this.admRoleDAO.getDaoSupport().save(apAuditLog);
			this.admRoleDAO.getDaoSupport().flush();
		} catch (DataAccessException e) {
			LOGGER.error("saveLog() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("saveLog():" + e, e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAdmRoleService#saveSystemLog(cafe.core.context.MultiParameterInquiryContext)
	 */
	public void saveSystemLog(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		AdmSystemLogging apAuditLog = new AdmSystemLogging();
		try {
			String actionId = (String) inquiryContext.getParameter(IAtomsConstants.FIELD_ACTION_ID);
			String logContent = (String) inquiryContext.getParameter(AdmRoleFormDTO.PARAM_LOG_CONTENT);
			IAtomsLogonUser logonUser = (IAtomsLogonUser) inquiryContext.getParameter(AdmRoleFormDTO.PARAM_USER_LOGON_USER);
			String ip = null;
			if(logonUser != null){
				ip = logonUser.getFromIp();
			}
			String ucNo = (String) inquiryContext.getParameter(AdmRoleFormDTO.PARAM_USER_UC_NO);
			//程式名稱/交易代號/交易名稱
			apAuditLog.setFunctionName(ucNo);
			//登入帳號
			apAuditLog.setUserId(logonUser.getId());
			//User端IP/終端機號
			apAuditLog.setIp(ip);
			//日期時間
			apAuditLog.setOperationTime(DateTimeUtils.getCurrentTimestamp());
			//sessionId
			apAuditLog.setSessionId(logonUser.getSessionId());
			if (IAtomsConstants.ACTION_UPDATE.equals(actionId)) {
				apAuditLog.setLogCategre(IAtomsConstants.ACTION_UPDATE);
			} else if (IAtomsConstants.ACTION_QUERY.equals(actionId)) {
				apAuditLog.setLogCategre(IAtomsConstants.ACTION_QUERY);
			} else if (IAtomsConstants.ACTION_INIT_DETAIL.equals(actionId)) {
				apAuditLog.setLogCategre(IAtomsConstants.ACTION_INIT_DETAIL);
			}
			apAuditLog.setResult(i18NUtil.getName(IAtomsMessageCode.IATOMS_SUCCESS));
			apAuditLog.setContent(logContent);
			this.admRoleDAO.getDaoSupport().save(apAuditLog);
			this.admRoleDAO.getDaoSupport().flush();
		} catch (DataAccessException e) {
			LOGGER.error("saveSystemLog() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("saveSystemLog():", e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
	}
	
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAdmRoleService#getUserByDepartmentAndRole(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public List<Parameter> getUserByDepartmentAndRole(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		List<Parameter> users = null;
		try {
			String deptCode = (String) inquiryContext.getParameter(BimDepartmentDTO.ATTRIBUTE.DEPT_CODE.getValue());
			String roleCode = (String) inquiryContext.getParameter(AdmRoleDTO.ATTRIBUTE.ROLE_CODE.getValue());
			Boolean flag = (Boolean) inquiryContext.getParameter(IAtomsConstants.PARAM_FLAG);
			Boolean isDeptAgent = (Boolean) inquiryContext.getParameter("isDeptAgent");
			users = this.admRoleDAO.getUserByDepartmentAndRole(deptCode, roleCode, flag, isDeptAgent);
		} catch (DataAccessException e) {
			LOGGER.error("getUserByDepartmentAndRole() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("getUserByDepartmentAndRole() Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return users;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAdmRoleService#checkRoleRepeat(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public String checkRoleRepeat(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		try {
			String roleCode = (String) inquiryContext.getParameter(AdmRoleDTO.ATTRIBUTE.ROLE_CODE.getValue());
			String roleName = (String) inquiryContext.getParameter(AdmRoleDTO.ATTRIBUTE.ROLE_NAME.getValue());
			String roleId = (String) inquiryContext.getParameter(AdmRoleDTO.ATTRIBUTE.ROLE_ID.getValue());
			//驗證角色代碼角色代碼是否重複
			Boolean isRepeat = admRoleDAO.checkRepeat(roleCode, null, roleId);
			if (isRepeat.booleanValue()) {
				return i18NUtil.getName(IAtomsMessageCode.ROLE_CODE_REPEATED);
			}
			//驗證角色代碼角色名稱是否重複
			isRepeat = admRoleDAO.checkRepeat(null, roleName, roleId);
			if (isRepeat.booleanValue()) {
				return i18NUtil.getName(IAtomsMessageCode.ROLE_NAME_REPEATED);
			}
		} catch (DataAccessException e) {
			LOGGER.error("checkRoleRepeat() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("checkRoleRepeat() Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return null;
	}
	/**
	 * @return the functionTypeDAO
	 */
	public IFunctionTypeDAO getFunctionTypeDAO() {
		return functionTypeDAO;
	}

	/**
	 * @param functionTypeDAO the functionTypeDAO to set
	 */
	public void setFunctionTypeDAO(IFunctionTypeDAO functionTypeDAO) {
		this.functionTypeDAO = functionTypeDAO;
	}

	/**
	 * @return the admRoleDAO
	 */
	public IAdmRoleDAO getAdmRoleDAO() {
		return admRoleDAO;
	}
	/**
	 * @param admRoleDAO the admRoleDAO to set
	 */
	public void setAdmRoleDAO(IAdmRoleDAO admRoleDAO) {
		this.admRoleDAO = admRoleDAO;
	}
	/**
	 * @return the rolePermissionDAO
	 */
	public IRolePermissionDAO getRolePermissionDAO() {
		return rolePermissionDAO;
	}
	/**
	 * @param rolePermissionDAO the rolePermissionDAO to set
	 */
	public void setRolePermissionDAO(IRolePermissionDAO rolePermissionDAO) {
		this.rolePermissionDAO = rolePermissionDAO;
	}
	/**
	 * @return the permissionDAO
	 */
	public IPermissionDAO getPermissionDAO() {
		return permissionDAO;
	}
	/**
	 * @param permissionDAO the permissionDAO to set
	 */
	public void setPermissionDAO(IPermissionDAO permissionDAO) {
		this.permissionDAO = permissionDAO;
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
