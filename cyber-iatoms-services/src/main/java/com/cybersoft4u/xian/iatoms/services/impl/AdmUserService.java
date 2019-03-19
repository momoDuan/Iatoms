package com.cybersoft4u.xian.iatoms.services.impl;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.rest.service.api.legacy.identity.LegacyUserInfoWithPassword;
import org.springframework.util.CollectionUtils;

import cafe.core.bean.Message;
import cafe.core.bean.Parameter;
import cafe.core.bean.identity.LogonUser;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.parameter.IBaseParameterItemDefDAO;
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

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.PasswordSettingDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AdmUserFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.IAtomsHumanPersonnelFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.PasswordEncoderUtilities;
import com.cybersoft4u.xian.iatoms.services.IAdmUserService;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmRoleDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserRoleDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserWarehouseDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IPasswordSettingDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmSystemLogging;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmUser;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmUserRole;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmUserRoleId;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmUserWarehouse;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmUserWarehouseId;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCompany;
import com.cybersoft4u.xian.iatoms.services.workflow.impl.IAtomsHumanTaskActivityService;
/** 
 * Purpose: 使用者維護service
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016年6月19日
 * @MaintenancePersonnel CrissZhang
 */
public class AdmUserService extends IAtomsHumanTaskActivityService implements IAdmUserService {
	/**
	 * 系统日志文件控件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, AdmUserService.class);
	/**
	 * 使用者DAO
	 */
	private IAdmUserDAO admUserDAO;
	/**
	 * 角色DAO
	 */
	private IAdmRoleDAO admRoleDAO;
	/**
	 * 使用者角色DAO
	 */
	private IAdmUserRoleDAO admUserRoleDAO;
	
	/**
	 * 用户仓库据点
	 */
	private IAdmUserWarehouseDAO admUserWarehouseDAO;
	
	/**
	 * 密碼設定
	 */
	private IPasswordSettingDAO passwordSettingDAO;
	
	/**
	 * 基本參數DAO
	 */
	private IBaseParameterItemDefDAO baseParameterItemDefDAO;
	/**
	 * 公司基本訊息維護DAO
	 */
	private ICompanyDAO companyDAO;
	
	/**
	 * Constructor:构造函数
	 */
	public AdmUserService() {
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.atoms.services.IAdmUserService#init(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext init(SessionContext sessionContext) throws ServiceException {
		try {
			AdmUserFormDTO admUserFormDTO = (AdmUserFormDTO) sessionContext.getRequestParameter();
			Message msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(admUserFormDTO);
			return sessionContext;
		} catch (Exception e) {
			LOGGER.error(".init(SessionContext sessionContext)", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAdmUserService#list(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext list(SessionContext sessionContext) throws ServiceException {
		try {
			AdmUserFormDTO admUserFormDTO = (AdmUserFormDTO) sessionContext.getRequestParameter();
			//查詢當頁數據
			List<AdmUserDTO> admUserDTOs = this.admUserDAO.getUserList(admUserFormDTO.getQueryCompany(),
					admUserFormDTO.getQueryDeptCode(), admUserFormDTO.getQueryAccount(), admUserFormDTO.getQueryCname(),  
					admUserFormDTO.getRows(), admUserFormDTO.getPage(), admUserFormDTO.getSort(), admUserFormDTO.getOrder());
			Message msg = null;
			if (!CollectionUtils.isEmpty(admUserDTOs)) {
				admUserFormDTO.setList(admUserDTOs);				
				//查詢總筆數
				int count = this.admUserDAO.countUser(admUserFormDTO.getQueryCompany(),
						admUserFormDTO.getQueryDeptCode(), admUserFormDTO.getQueryAccount(), admUserFormDTO.getQueryCname());
				admUserFormDTO.getPageNavigation().setRowCount(count);
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
			} else {
				LOGGER.error("AdmUserService --> list() error--> ", "dto is null");
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(admUserFormDTO);
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error("admUserService.list() DataAccess Exception:", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("admUserService.list() Exception:", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.atoms.services.IAdmUserService#query(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext query(SessionContext sessionContext)
			throws ServiceException {
		try {
			AdmUserFormDTO admUserFormDTO = (AdmUserFormDTO) sessionContext.getRequestParameter();
			//查詢當頁數據
			String role = admUserFormDTO.getQueryRole();
			List<String> roles = null;
			if (StringUtils.hasText(role)) {
				roles = StringUtils.toList(role, IAtomsConstants.MARK_SEPARATOR);
			}
			List<AdmUserDTO> admUserDTOs = this.admUserDAO.listby(admUserFormDTO.getQueryAccount(), 
					admUserFormDTO.getQueryCname(), admUserFormDTO.getQueryCompany(), 
					roles, admUserFormDTO.getQueryStatus(),
					admUserFormDTO.getRows(), admUserFormDTO.getPage(), admUserFormDTO.getSort(),
					admUserFormDTO.getOrder(), Boolean.valueOf(true));
			Message msg = null;
			if (!CollectionUtils.isEmpty(admUserDTOs)) {
				for(AdmUserDTO admUserDTO : admUserDTOs){
					String createdByName = admUserDTO.getCreatedByName();
					if(StringUtils.hasText(createdByName) && createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) > 0 && createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT) > 0){
						admUserDTO.setCreatedByName(createdByName.substring(createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) + 1, createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT)));
					}
					String updatedByName = admUserDTO.getUpdatedByName();
					if(StringUtils.hasText(updatedByName) && updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) > 0 && updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT) > 0){
						admUserDTO.setUpdatedByName(updatedByName.substring(updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) + 1, updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT)));
					}
				}
				admUserFormDTO.setList(admUserDTOs);				
				//查詢總筆數
				int count = this.admUserDAO.count(admUserFormDTO.getQueryAccount(), 
					admUserFormDTO.getQueryCname(), admUserFormDTO.getQueryCompany(), 
					roles, admUserFormDTO.getQueryStatus());
				admUserFormDTO.getPageNavigation().setRowCount(count);
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
				LOGGER.error("AdmUserService --> query() error--> ", "dto is null");
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(admUserFormDTO);
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error(".query() DataAccess Exception:", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".query() Exception:", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.atoms.services.IAdmUserService#initEdit(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext initEdit(SessionContext sessionContext) throws ServiceException {
		try {
			AdmUserFormDTO admUserFormDTO = (AdmUserFormDTO) sessionContext.getRequestParameter();
			PasswordSettingDTO passwordSettingDTO = this.passwordSettingDAO.getPasswordSettingInfo();
			admUserFormDTO.setPasswordSettingDTO(passwordSettingDTO);
			String userId = admUserFormDTO.getUserId();
			Transformer transformer = new SimpleDtoDmoTransformer();
			AdmUserDTO admUserDTO = new AdmUserDTO();
			// 修改初始化
			if (StringUtils.hasText(userId)) {
				//查詢用戶信息
				AdmUser admUser = (AdmUser) this.admUserDAO.findByPrimaryKey(AdmUser.class, userId);
				admUserDTO = (AdmUserDTO) transformer.transform(admUser, admUserDTO);
				admUserDTO.setUserId(userId);
				admUserFormDTO.setAdmUserDTO(admUserDTO);
			} else {
				LOGGER.error("AdmUserService --> initEdit() error--> ", "id is null");
			}
			sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS));
			sessionContext.setResponseResult(admUserFormDTO);
		} catch (DataAccessException e) {
			LOGGER.error(".initEdit() DataAccess Exception:", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".initEdit(SessionContext sessionContext)", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAdmUserService#saveSystemLog(cafe.core.context.MultiParameterInquiryContext)
	 */
	public void saveSystemLog(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		AdmSystemLogging apAuditLog = new AdmSystemLogging();
		try {
			String actionId = (String) inquiryContext.getParameter(IAtomsConstants.FIELD_ACTION_ID);
			String logContent = (String) inquiryContext.getParameter(AdmUserFormDTO.PARAM_LOG_CONTENT);
			IAtomsLogonUser logonUser = (IAtomsLogonUser) inquiryContext.getParameter(AdmUserFormDTO.PARAM_USER_LOGON_USER);
			String ip = null;
			if(logonUser != null){
				ip = logonUser.getFromIp();
				String ucNo = (String) inquiryContext.getParameter(AdmUserFormDTO.PARAM_USER_UC_NO);
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
				if(IAtomsConstants.ACTION_INIT_EDIT.equals(actionId)){
					//紀錄類別
					apAuditLog.setLogCategre(IAtomsConstants.ACTION_UPDATE);
				} else if(IAtomsConstants.ACTION_QUERY.equals(actionId)){
					//紀錄類別
					apAuditLog.setLogCategre(IAtomsConstants.ACTION_QUERY);
				}
				apAuditLog.setResult(i18NUtil.getName(IAtomsMessageCode.IATOMS_SUCCESS));
				apAuditLog.setContent(logContent);
				this.admUserDAO.getDaoSupport().save(apAuditLog);
				this.admUserDAO.getDaoSupport().flush();
			} else {
				LOGGER.error("AdmUserService --> saveSystemLog() error--> ", "logonUser is null");
			}
		} catch (DataAccessException e) {
			LOGGER.error(".saveSystemLog() DataAccess Exception:", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(".saveSystemLog():", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAdmUserService#initAdd(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext initAdd(SessionContext sessionContext) throws ServiceException {
		try {
			AdmUserFormDTO admUserFormDTO = (AdmUserFormDTO) sessionContext.getRequestParameter();
			PasswordSettingDTO passwordSettingDTO = this.passwordSettingDAO.getPasswordSettingInfo();
			admUserFormDTO.setPasswordSettingDTO(passwordSettingDTO);
			sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS));
			sessionContext.setResponseResult(admUserFormDTO);
		} catch (DataAccessException e) {
			LOGGER.error(".initAdd() DataAccess Exception:", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".initAdd(SessionContext sessionContext):", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.atoms.services.IAdmUserService#save(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext save(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		boolean isEdit = false;
		AdmUserFormDTO admUserFormDTO = (AdmUserFormDTO) sessionContext.getRequestParameter();
	//	LogonUser logonUser = admUserFormDTO.getLogonUser();
		IAtomsLogonUser logonUser = (IAtomsLogonUser) admUserFormDTO.getLogonUser();
		AdmUserDTO admUserDTO = admUserFormDTO.getAdmUserDTO();
		String userId = admUserDTO.getUserId();
		AdmSystemLogging apAuditLog = new AdmSystemLogging();
		try {
			Transformer transformer = new SimpleDtoDmoTransformer();
			String password = admUserDTO.getPassword();
			AdmUser admUser = null;
			Integer defaultRetry = Integer.valueOf(0);
			if (!StringUtils.hasText(userId)) {
				String account = admUserDTO.getAccount();
				//新增
				//先判斷帳號是否重複
				boolean isRepeat = this.admUserDAO.isRepeat(account);
				if (isRepeat) {
					//帳號重複
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.ACCOUNT_REPEAT);
					sessionContext.setReturnMessage(msg);
					return sessionContext;
				}
				admUser = new AdmUser();
				admUser = (AdmUser) transformer.transform(admUserDTO, admUser);
				userId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_ADM_USER);
				if(!StringUtils.hasText(admUser.getDeptCode())){
					admUser.setDeptCode(null);
				} else {
					admUser.setDeptCode(admUser.getDeptCode());
				}
				admUser.setUserId(userId);
				admUser.setRetry(defaultRetry);
				admUser.setCreatedById(logonUser.getId());
				admUser.setCreatedByName(logonUser.getName());
				admUser.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
				admUser.setDeleted(IAtomsConstants.NO);
				admUser.setStatus(IAtomsConstants.ACCOUNT_STATUS_NEW);
				admUser.setResetPwd(IAtomsConstants.NO);
				// 首次預設最後一次登錄時間
				admUser.setLastLoginDate(DateTimeUtils.getCurrentTimestamp());
				if (StringUtils.hasText(password)) {
					password = PasswordEncoderUtilities.encodePassword(admUserDTO.getPassword());
					admUser.setPassword(password);
					// 首次預設變更密碼日期
					admUser.setChangePwdDate(DateTimeUtils.getCurrentDate());
				}
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INSERT_SUCCESS, new String[]{this.getMyName()});
				//bug2359 update by 2017/09/06
				apAuditLog.setLogCategre(IAtomsConstants.PARAM_CREATE + IAtomsConstants.MARK_NO + IAtomsConstants.ACTION_SAVE);
			} else {
				isEdit = true;
				//修改
				admUser = (AdmUser) this.admUserDAO.findByPrimaryKey(AdmUser.class, userId);
				if(IAtomsConstants.ACCOUNT_STATUS_STOP_RIGHT.equals(admUser.getStatus())
						&& !IAtomsConstants.ACCOUNT_STATUS_STOP_RIGHT.equals(admUserDTO.getStatus())){
					admUser.setLastLoginDate(DateTimeUtils.getCurrentTimestamp());
				}
				admUser.setUserId(userId);
				admUser.setDataAcl(admUserDTO.getDataAcl());
				admUser.setCompanyId(admUserDTO.getCompanyId());
				admUser.setCname(admUserDTO.getCname());
				if(!StringUtils.hasText(admUserDTO.getDeptCode())){
					admUser.setDeptCode(null);
				} else {
					admUser.setDeptCode(admUserDTO.getDeptCode());
				}
				admUser.setEname(admUserDTO.getEname());
				admUser.setTel(admUserDTO.getTel());
				admUser.setMobile(admUserDTO.getMobile());
				admUser.setEmail(admUserDTO.getEmail());
				admUser.setManagerEmail(admUserDTO.getManagerEmail());
				admUser.setUserDesc(admUserDTO.getUserDesc());
				
				if (StringUtils.hasText(password)) {
					password = PasswordEncoderUtilities.encodePassword(admUserDTO.getPassword());
					admUser.setPassword(password);
					admUser.setResetPwd(IAtomsConstants.YES);
					admUser.setRetry(defaultRetry);
					admUser.setChangePwdDate(DateTimeUtils.getCurrentDate());
				} else {
					if(StringUtils.hasText(admUser.getResetPwd())){
						admUser.setResetPwd(admUser.getResetPwd());
					} else {
						admUser.setResetPwd(IAtomsConstants.NO);
					}
				}
				admUser.setStatus(admUserDTO.getStatus());
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.UPDATE_SUCCESS, new String[]{this.getMyName()});
				//bug2359 update by 2017/09/06
				apAuditLog.setLogCategre(IAtomsConstants.PARAM_UPDATE + IAtomsConstants.MARK_NO + IAtomsConstants.ACTION_SAVE);
			}				
			admUser.setId(userId);
			admUser.setUpdatedById(logonUser.getId());
			admUser.setUpdatedByName(logonUser.getName());
			admUser.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
			// 編輯 處理activiti信息
			if(isEdit){
				IAtomsHumanPersonnelFormDTO personFormDTO = new IAtomsHumanPersonnelFormDTO();
				ActivitiWorkflowContext activitiWorkflowContext = new ActivitiWorkflowContext(logonUser);
				personFormDTO.setWorkflowContext(activitiWorkflowContext);
				personFormDTO.setActivitiUserId(admUser.getAccount());
				// 判斷activiti有沒有該用戶
				ActivitiUserInfoDTO activitiUserInfo = super.getUserInfoById(personFormDTO);
				MemberShipBaseDTO memberShipBaseDTO = null;
				List<String> oldGroupList = null;
				List<String> newGroupList = null;
				// 添加group標記
				boolean isAddGroup = false;
				// 刪除group標記
				boolean isRemoveGroup = false;
				// 根據使用者編號查到的角色group信息
				Map<String, Object> oldResultMap = null;
				// 根據角色代碼查到的角色group信息
				Map<String, Object> newResultMap = null;
				// activiti是否有該用戶
				if(activitiUserInfo == null){
					if (StringUtils.hasText(admUserDTO.getFunctionGroup())) {
						newResultMap = this.getRoleGroup(admUserDTO.getFunctionGroup(), false);
						if((Boolean) newResultMap.get(IAtomsConstants.PARAM_HAVE_ROLE_GROUP)){
							// 保存用戶信息對象
							LegacyUserInfoWithPassword userInfo = new LegacyUserInfoWithPassword();
							userInfo.setId(admUser.getAccount());
							userInfo.setFirstName(admUser.getCname());
							userInfo.setLastName(IAtomsConstants.MARK_EMPTY_STRING);
							userInfo.setEmail(admUser.getEmail());
							personFormDTO.setActivitiUserId(admUser.getAccount());
							userInfo.setPassword(PasswordEncoderUtilities.encodePassword(admUser.getAccount()));
							personFormDTO.setInsertUser(userInfo);
							super.createActivitiUser(personFormDTO);
							// 增加group
							isAddGroup = true;
						}
					}
				} else {
					oldResultMap = this.getRoleGroup(userId, true);
					// 有選擇角色代碼
					if (StringUtils.hasText(admUserDTO.getFunctionGroup())) {
						newResultMap = this.getRoleGroup(admUserDTO.getFunctionGroup(), false);
						// 之前的有
						if((Boolean) oldResultMap.get(IAtomsConstants.PARAM_HAVE_ROLE_GROUP)){
							// 現在的有
							if((Boolean) newResultMap.get(IAtomsConstants.PARAM_HAVE_ROLE_GROUP)){
								// 先刪
								isRemoveGroup = true;
								// 後增
								isAddGroup = true;
							// 現在的無
							} else {
								// 刪除Membership
								isRemoveGroup = true;
							}
						// 之前的沒有
						} else {
							// 現在的有
							if((Boolean) newResultMap.get(IAtomsConstants.PARAM_HAVE_ROLE_GROUP)){
								// 增加group信息
								isAddGroup = true;
							// 現在的無
							}
						}
					// 無角色代碼
					} else {
						// 之前的有group
						if((Boolean) oldResultMap.get(IAtomsConstants.PARAM_HAVE_ROLE_GROUP)){
							isRemoveGroup = true;
						// 無group 不處理
						}
					}
				}
				// 刪除Membership
				if(isRemoveGroup){
					oldGroupList = (List<String>) oldResultMap.get(IAtomsConstants.PARAM_ROLE_GROUP_LIST);
					// 刪除Membership
					if(!CollectionUtils.isEmpty(oldGroupList)){
						for(String roleGroup : oldGroupList ){
							personFormDTO = new IAtomsHumanPersonnelFormDTO();
							personFormDTO.setWorkflowContext(activitiWorkflowContext);
							personFormDTO.setActivitiUserId(admUser.getAccount());
							personFormDTO.setActivitiGroupId(roleGroup);
							// 刪除指定group
							super.removeUserForGroup(personFormDTO);
						}
					}
				}
				// 增加Membership
				if(isAddGroup){
					newGroupList = (List<String>) newResultMap.get(IAtomsConstants.PARAM_ROLE_GROUP_LIST);
					if(!CollectionUtils.isEmpty(newGroupList)){
						// 加Membership
						for(String roleGroup : newGroupList ){
							personFormDTO = new IAtomsHumanPersonnelFormDTO();
							personFormDTO.setWorkflowContext(activitiWorkflowContext);
							memberShipBaseDTO = new MemberShipBaseDTO();
							memberShipBaseDTO.setUserId(admUser.getAccount());
							personFormDTO.setMemberShipBaseDTO(memberShipBaseDTO);
							personFormDTO.setActivitiUserId(admUser.getAccount());
							personFormDTO.setActivitiGroupId(roleGroup);
							super.addUserToGroup(personFormDTO);
						}
					}
				}
			// 新增
			} else {
				//role group
				if (StringUtils.hasText(admUserDTO.getFunctionGroup())) {
					List<String> groupList = null;
					// 根據roleg代碼查角色group信息
					Map<String, Object> resultMap = this.getRoleGroup(admUserDTO.getFunctionGroup(), false);
					if((Boolean) resultMap.get(IAtomsConstants.PARAM_HAVE_ROLE_GROUP)){
						groupList = (List<String>) resultMap.get(IAtomsConstants.PARAM_ROLE_GROUP_LIST);
						// 添加用戶
						IAtomsHumanPersonnelFormDTO personFormDTO = new IAtomsHumanPersonnelFormDTO();
						ActivitiWorkflowContext activitiWorkflowContext = new ActivitiWorkflowContext(logonUser);
						personFormDTO.setWorkflowContext(activitiWorkflowContext);
						// 保存用戶信息對象
						LegacyUserInfoWithPassword userInfo = new LegacyUserInfoWithPassword();
						userInfo.setId(admUser.getAccount());
						userInfo.setFirstName(admUser.getCname());
						userInfo.setLastName(IAtomsConstants.MARK_EMPTY_STRING);
						userInfo.setEmail(admUser.getEmail());
						personFormDTO.setActivitiUserId(admUser.getAccount());
						ActivitiUserInfoDTO activitiUserInfo = super.getUserInfoById(personFormDTO);
						// 沒有該用戶 增加用戶
						if(activitiUserInfo == null){
							userInfo.setPassword(PasswordEncoderUtilities.encodePassword(admUser.getAccount()));
							personFormDTO.setInsertUser(userInfo);
							super.createActivitiUser(personFormDTO);
						} else {
							personFormDTO.setUpdateUser(userInfo);
							super.updateActivitiUser(personFormDTO);
						}
						// 用於向群組中添加用戶成員對象
						MemberShipBaseDTO memberShipBaseDTO = null;
						if(!CollectionUtils.isEmpty(groupList)){
							// 加Membership
							for(String roleGroup : groupList ){
								personFormDTO = new IAtomsHumanPersonnelFormDTO();
								personFormDTO.setWorkflowContext(activitiWorkflowContext);
								memberShipBaseDTO = new MemberShipBaseDTO();
								memberShipBaseDTO.setUserId(admUser.getAccount());
								personFormDTO.setMemberShipBaseDTO(memberShipBaseDTO);
								personFormDTO.setActivitiUserId(admUser.getAccount());
								personFormDTO.setActivitiGroupId(roleGroup);
								// 增加group
								super.addUserToGroup(personFormDTO);
							}
						}
					}
				}
			}
			
			//先刪除所有的角色
			this.admUserRoleDAO.deleteAll(userId);
			this.admUserRoleDAO.getDaoSupport().flush();
			//保存使用者信息
			this.admUserDAO.getDaoSupport().saveOrUpdate(admUser);
			this.admUserDAO.getDaoSupport().flush();
			// 删除所有仓库据点信息
			this.admUserWarehouseDAO.deleteAll(userId);
			this.admUserWarehouseDAO.getDaoSupport().flush();
			Date currentDate = DateTimeUtils.getCurrentTimestamp();
			String roleGroup = admUserDTO.getFunctionGroup();
			if (StringUtils.hasText(roleGroup)) {
				//保存角色信息
				String[] roleGroups = roleGroup.split(IAtomsConstants.MARK_SEPARATOR);
				AdmUserRole admUserRole = null;
				AdmUserRoleId admUserRoleId = null;
				for (String roleId : roleGroups) {
					admUserRole = new AdmUserRole();
					admUserRoleId = new AdmUserRoleId();
					admUserRoleId.setRoleId(roleId);
					admUserRoleId.setUserId(userId);
					admUserRole.setId(admUserRoleId);
					this.admUserRoleDAO.getDaoSupport().saveOrUpdate(admUserRole);
				}
				this.admUserRoleDAO.getDaoSupport().flush();
			}	
			String warehouseGroup = admUserDTO.getWarehouseGroup();
			if (StringUtils.hasText(warehouseGroup)) {
				//保存用户仓库信息
				String[] warehouseGroups = warehouseGroup.split(IAtomsConstants.MARK_SEPARATOR);
				AdmUserWarehouse admUserWarehouse = null;
				AdmUserWarehouseId admUserWarehouseId = null;
				for (String warehouseId : warehouseGroups) {
					admUserWarehouse = new AdmUserWarehouse();
					admUserWarehouseId = new AdmUserWarehouseId();
					admUserWarehouseId.setWarehouseId(warehouseId);
					admUserWarehouseId.setUserId(userId);
					admUserWarehouse.setId(admUserWarehouseId);
					this.admUserWarehouseDAO.getDaoSupport().saveOrUpdate(admUserWarehouse);
				}
				this.admUserRoleDAO.getDaoSupport().flush();
			}	
			//程式名稱/交易代號/交易名稱
			apAuditLog.setFunctionName(admUserFormDTO.getUseCaseNo());
			//登入帳號
			apAuditLog.setUserId(logonUser.getId());
			//User端IP/終端機號
			apAuditLog.setIp(((IAtomsLogonUser) admUserFormDTO.getLogonUser()).getFromIp());
			//日期時間
			apAuditLog.setOperationTime(DateTimeUtils.getCurrentTimestamp());
			//sessionId
			apAuditLog.setSessionId(logonUser.getSessionId());
			//紀錄類別
			apAuditLog.setContent(admUserFormDTO.getLogContent());
			apAuditLog.setResult(i18NUtil.getName(IAtomsMessageCode.IATOMS_SUCCESS));
			this.admUserDAO.getDaoSupport().save(apAuditLog);
			this.admUserDAO.getDaoSupport().flush();
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOGGER.error(".save() DataAccess Exception:", e.getMessage(), e);
			if (!StringUtils.hasText(userId)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INSERT_FAILURE, new String[]{this.getMyName()});
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()});
			}
			throw new ServiceException(msg);
		}  catch (Exception e) {
			LOGGER.error(".save(SessionContext sessionContext):", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.atoms.services.IAdmUserService#isRepeat(java.lang.String)
	 */
	@Override
	public SessionContext check(SessionContext sessionContext) throws ServiceException {
		try {
			AdmUserFormDTO admUserFormDTO = (AdmUserFormDTO) sessionContext.getRequestParameter();
			String account = admUserFormDTO.getAccount();
			boolean isRepeat = this.admUserDAO.isRepeat(account);
			Message msg;
			if (isRepeat) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.ACCOUNT_REPEAT);
			} else {
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.ACCOUNT_CHECKED_SUCCESS);
			}
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOGGER.error(".check() DataAccess Exception:", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(".check(SessionContext sessionContext):", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}

	/**
	 * Purpose:根據編號得到角色group信息
	 * @author CrissZhang
	 * @param id ： 編號，根據標志位不同傳值不同
	 * @param isQueryUser ： 查詢使用者的標志位
	 * @return Map<String,Object> : 返回保存角色group的map集合
	 */
	private Map<String, Object> getRoleGroup(String id, boolean isQueryUser){
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
		// 放置角色group集合
		List<String> groupList = new ArrayList<String>();
		// 查詢角色group信息
		List<AdmRoleDTO> admRoleDTOs = this.admRoleDAO.getRoleGroup(id, isQueryUser, null, null);
		if(!CollectionUtils.isEmpty(admRoleDTOs)){
			for(AdmRoleDTO admRoleDTO : admRoleDTOs){
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
		// 有任意一種group信息 // CR #2951 廠商客服      // Task #3578  客戶廠商客服
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
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.atoms.services.IAdmUserService#delete(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext delete(SessionContext sessionContext) throws ServiceException {
		AdmSystemLogging apAuditLog = new AdmSystemLogging();
		AdmUserFormDTO admUserFormDTO = (AdmUserFormDTO) sessionContext.getRequestParameter();
		LogonUser logonUser = admUserFormDTO.getLogonUser();
		Message msg = null;
		try {
			String userId = admUserFormDTO.getUserId();
			//通过主键得到这笔数据
			AdmUser admUser = (AdmUser) this.admUserDAO.findByPrimaryKey(AdmUser.class, userId);
			if(admUser == null){
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()});
			} else {
				// role group信息
				Map<String, Object> resultMap = this.getRoleGroup(userId, true);
				// 有角色group信息處理 沒有不處理
				if((Boolean) resultMap.get(IAtomsConstants.PARAM_HAVE_ROLE_GROUP)){
					// 取得當前用戶擁有的角色group信息
					List<String> groupList = (List<String>) resultMap.get(IAtomsConstants.PARAM_ROLE_GROUP_LIST);
					// 用於向群組中添加用戶成員對象
					if(!CollectionUtils.isEmpty(groupList)){
						// 處理activiti人員信息FormDTO
						IAtomsHumanPersonnelFormDTO personFormDTO = new IAtomsHumanPersonnelFormDTO();
						// Activiti BPM平台認證內容類別
						ActivitiWorkflowContext activitiWorkflowContext = new ActivitiWorkflowContext(logonUser);
						personFormDTO.setWorkflowContext(activitiWorkflowContext);
						// 設置activiti用戶主鍵
						personFormDTO.setActivitiUserId(admUser.getAccount());
						// Activiti用戶信息
						ActivitiUserInfoDTO activitiUserInfo = super.getUserInfoById(personFormDTO);
						if(activitiUserInfo != null){
							// 刪除Membership
							for(String roleGroup : groupList ){
								personFormDTO = new IAtomsHumanPersonnelFormDTO();
								personFormDTO.setWorkflowContext(activitiWorkflowContext);
								personFormDTO.setActivitiUserId(admUser.getAccount());
								personFormDTO.setActivitiGroupId(roleGroup);
								// 移除用戶的當前羣組
								super.removeUserForGroup(personFormDTO);
							}
							personFormDTO = new IAtomsHumanPersonnelFormDTO();
							personFormDTO.setWorkflowContext(activitiWorkflowContext);
							personFormDTO.setActivitiUserId(admUser.getAccount());
							// 刪除activiti用戶
							super.deleteActivitiUser(personFormDTO);
						}
					}
				}
				//程式名稱/交易代號/交易名稱
				apAuditLog.setFunctionName(admUserFormDTO.getUseCaseNo());
				//登入帳號
				apAuditLog.setUserId(logonUser.getId());
				//User端IP/終端機號
				apAuditLog.setIp(((IAtomsLogonUser) admUserFormDTO.getLogonUser()).getFromIp());
				//日期時間
				apAuditLog.setOperationTime(DateTimeUtils.getCurrentTimestamp());
				//sessionId
				apAuditLog.setSessionId(logonUser.getSessionId());
				//紀錄類別
				apAuditLog.setLogCategre(IAtomsConstants.ACTION_DELETE);
				//role group
				//先刪除所有的角色
				this.admUserRoleDAO.deleteAll(userId);
				this.admUserRoleDAO.getDaoSupport().flush();
				// 删除所有仓库据点信息
				this.admUserWarehouseDAO.deleteAll(userId);
				this.admUserWarehouseDAO.getDaoSupport().flush();
				admUser.setId(userId);
				//修改刪除標記,邏輯刪除
				admUser.setUpdatedById(logonUser.getId());
				admUser.setUpdatedByName(logonUser.getName());
				admUser.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
				admUser.setDeleted(IAtomsConstants.YES);
				this.admUserDAO.save(admUser);
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_SUCCESS, new String[]{this.getMyName()});
				apAuditLog.setResult(i18NUtil.getName(IAtomsMessageCode.IATOMS_SUCCESS));
				apAuditLog.setContent(admUserFormDTO.getLogContent());
				this.admUserDAO.getDaoSupport().save(apAuditLog);
				this.admUserDAO.getDaoSupport().flush();
			} 
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOGGER.error(".delete() DataAccess Exception:", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(".delete(SessionContext sessionContext):", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.atoms.services.IAdmUserService#export(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext export(SessionContext sessionContext) throws ServiceException {
		try {
			AdmUserFormDTO admUserFormDTO = (AdmUserFormDTO) sessionContext.getRequestParameter();
			String queryCname = IAtomsConstants.MARK_EMPTY_STRING;
			if(StringUtils.hasText(admUserFormDTO.getQueryCname())){
				queryCname = URLDecoder.decode(admUserFormDTO.getQueryCname(), "UTF-8"); 
			}
			String role = admUserFormDTO.getQueryRole();
			List<String> roles = null;
			if (StringUtils.hasText(role)) {
				roles = StringUtils.toList(role, IAtomsConstants.MARK_SEPARATOR);
			}
			List<AdmUserDTO> admUserDTOs = this.admUserDAO.listby(admUserFormDTO.getQueryAccount(), 
					queryCname, admUserFormDTO.getQueryCompany(), 
					roles, admUserFormDTO.getQueryStatus(),
					null, null, admUserFormDTO.getSort(),
					admUserFormDTO.getOrder(), Boolean.valueOf(false));
			for(AdmUserDTO admUserDTO : admUserDTOs){
				String createdByName = admUserDTO.getCreatedByName();
				if(StringUtils.hasText(createdByName) && createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) > 0 && createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT) > 0){
					admUserDTO.setCreatedByName(createdByName.substring(createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) + 1, createdByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT)));
				}
				String updatedByName = admUserDTO.getUpdatedByName();
				if(StringUtils.hasText(updatedByName) && updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) > 0 && updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT) > 0){
					admUserDTO.setUpdatedByName(updatedByName.substring(updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_LEFT) + 1, updatedByName.indexOf(IAtomsConstants.MARK_BRACKETS_RIGHT)));
				}
			}
			admUserFormDTO.setList(admUserDTOs);
			Message msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(admUserFormDTO);
		} catch (DataAccessException e) {
			LOGGER.error(".export() DataAccess Exception:", e.getMessage(), e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(".export(SessionContext sessionContext):", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAdmUserService#getNormalUserEmailList(cafe.core.context.MultiParameterInquiryContext)
	 */
	public List<Parameter> getNormalUserEmailList(MultiParameterInquiryContext inquiryContext)
			throws ServiceException {
		long startQueryEmpEmailTime = System.currentTimeMillis();
		List<Parameter> empMailList = null;
		try {
			// 帳號狀態
			String status = (String) inquiryContext.getParameter(AdmUserDTO.ATTRIBUTE.STATUS.getValue());
			// 获取合約下拉框值
			empMailList = this.admUserDAO.getNormalUserEmailList(status);
		} catch (DataAccessException e) {
			LOGGER.error(".getContractCodeList() DataAccess Exception : ", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("getContractCodeList() Exception : ", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		long endQueryEmpEmailTime = System.currentTimeMillis();
		LOGGER.debug("calculate time --> load", "Service getNormalUserEmailList:" + (endQueryEmpEmailTime - startQueryEmpEmailTime));
		return empMailList;
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAdmUserService#getMailGroupList()
	 */
	@Override
	public List<Parameter> getMailGroupList() throws ServiceException {
		List<Parameter> mailgroupList = null;
		String mailgroupListString = null;
		try {
			List<Parameter> empMailList = null;
			empMailList = this.admUserDAO.getNormalUserEmailList(IAtomsConstants.ACCOUNT_STATUS_NORMAL);
			mailgroupList = (List<Parameter>) this.baseParameterItemDefDAO.getAvailableParameterItems(IATOMS_PARAM_TYPE.MAIL_GROUP.getCode(), null);
			//給通知收件人添加統一格式 (MerchantAO (特店 AO))
			Parameter aoEmail = new Parameter();
			aoEmail.setValue(IAtomsConstants.AOEMAIL);
			aoEmail.setName(IAtomsConstants.MERCHANT_AO);
			mailgroupList.add(aoEmail);
			mailgroupList.addAll(empMailList);
			//Gson gsonss = new GsonBuilder().create();
			//mailgroupListString = gsonss.toJson(mailgroupList);
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".getMailGroupList() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".getMailGroupList() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
		return mailgroupList;
	}
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAdmUserService#getNameList()
	 */
	@Override
	public String getNameList() throws ServiceException {
		List<Parameter> mailgroupList = null;
		String nameListString = "";
		try {
			List<Parameter> empMailList = null;
			empMailList = this.admUserDAO.getNormalUserEmailList(IAtomsConstants.ACCOUNT_STATUS_NORMAL);
			mailgroupList = (List<Parameter>) this.baseParameterItemDefDAO.getAvailableParameterItems(IATOMS_PARAM_TYPE.MAIL_GROUP.getCode(), null);
			//給通知收件人添加統一格式 (MerchantAO (特店 AO))
			Parameter aoEmail = new Parameter();
			aoEmail.setValue(IAtomsConstants.AOEMAIL);
			aoEmail.setName(IAtomsConstants.MERCHANT_AO);
			mailgroupList.add(aoEmail);
			mailgroupList.addAll(empMailList);
			for (Parameter parameter : mailgroupList) {
				if (!cafe.core.util.StringUtils.hasText(nameListString)) {
					nameListString = (StringUtils.hasText(parameter.getName())?parameter.getName():"") + ",";
				} else {
					nameListString += (StringUtils.hasText(parameter.getName())?parameter.getName():"") + ",";
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".getMailGroupList() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".getMailGroupList() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
		return nameListString;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAdmUserService#getUserListByCompany(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public List<Parameter> getUserListByCompany(MultiParameterInquiryContext parameterInquiryContext)
			throws ServiceException {
		List<Parameter> list = null;
		try {
			//獲取公司id
			String companyId = (String) parameterInquiryContext.getParameter(CompanyDTO.ATTRIBUTE.COMPANY_ID.getValue());
			//若存在公司id
			if (StringUtils.hasText(companyId)) {
				//根據公司id獲取該公司的所有的工程師信息
				list = this.admUserDAO.getUserListByCompany(companyId);
			}
		} catch (DataAccessException e) {
			LOGGER.error("getUserListByCompany()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE);
		} catch (Exception e) {
			LOGGER.error("getUserListByCompany()", "SessionContext sessionContext:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED); 
		}
		return list;

	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAdmUserService#isAuthenticationTypeEqualsIAtoms(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext isAuthenticationTypeEqualsIAtoms(SessionContext sessionContext) throws ServiceException {
		boolean result = false;
		try {
			// 公司编号
			AdmUserFormDTO admUserFormDTO = (AdmUserFormDTO) sessionContext.getRequestParameter();
			String companyId = admUserFormDTO.getCompanyId();
			if(companyId != null){
				//根据公司编号得到登入验证方式
				BimCompany company = this.companyDAO.findByPrimaryKey(BimCompany.class, companyId);
				//若登入驗證方式為iAtoms驗證返回true
				if(IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE.equals(company.getAuthenticationType())){
					result = true;
				}
			} else {
				LOGGER.error("AdmUserService --> isAuthenticationTypeEqualsIAtoms() error--> ", "id is null");
			}
			admUserFormDTO.setTemp(result);
			sessionContext.setResponseResult(admUserFormDTO);
		} catch (DataAccessException e) {
			LOGGER.error("isAuthenticationTypeEqualsIAtoms()", "DataAccess Exception : ", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("isAuthenticationTypeEqualsIAtoms()", "Exception : ", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		}
		return sessionContext;
	}
	
	/**
	 * @return the companyDAO
	 */
	public ICompanyDAO getCompanyDAO() {
		return companyDAO;
	}
	/**
	 * @param companyDAO the companyDAO to set
	 */
	public void setCompanyDAO(ICompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
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
	 * @return the admUserRoleDAO
	 */
	public IAdmUserRoleDAO getAdmUserRoleDAO() {
		return admUserRoleDAO;
	}
	/**
	 * @param admUserRoleDAO the admUserRoleDAO to set
	 */
	public void setAdmUserRoleDAO(IAdmUserRoleDAO admUserRoleDAO) {
		this.admUserRoleDAO = admUserRoleDAO;
	}
	/**
	 * @return the admUserWarehouseDAO
	 */
	public IAdmUserWarehouseDAO getAdmUserWarehouseDAO() {
		return admUserWarehouseDAO;
	}
	/**
	 * @param admUserWarehouseDAO the admUserWarehouseDAO to set
	 */
	public void setAdmUserWarehouseDAO(IAdmUserWarehouseDAO admUserWarehouseDAO) {
		this.admUserWarehouseDAO = admUserWarehouseDAO;
	}
	/**
	 * @return the passwordSettingDAO
	 */
	public IPasswordSettingDAO getPasswordSettingDAO() {
		return passwordSettingDAO;
	}
	/**
	 * @param passwordSettingDAO the passwordSettingDAO to set
	 */
	public void setPasswordSettingDAO(IPasswordSettingDAO passwordSettingDAO) {
		this.passwordSettingDAO = passwordSettingDAO;
	}
	/**
	 * @return the baseParameterItemDefDAO
	 */
	public IBaseParameterItemDefDAO getBaseParameterItemDefDAO() {
		return baseParameterItemDefDAO;
	}
	/**
	 * @param baseParameterItemDefDAO the baseParameterItemDefDAO to set
	 */
	public void setBaseParameterItemDefDAO(
			IBaseParameterItemDefDAO baseParameterItemDefDAO) {
		this.baseParameterItemDefDAO = baseParameterItemDefDAO;
	}
}