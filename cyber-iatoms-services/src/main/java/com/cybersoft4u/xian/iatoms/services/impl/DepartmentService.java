package com.cybersoft4u.xian.iatoms.services.impl;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimDepartmentDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ContractSlaDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.DepartmentFormDTO;
import com.cybersoft4u.xian.iatoms.services.IDepartmentService;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDepartmentDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimDepartment;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Message;
import cafe.core.bean.Parameter;
import cafe.core.bean.identity.LogonUser;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.dao.DataAccessException;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.convert.SimpleDtoDmoTransformer;
import cafe.core.util.convert.Transformer;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
/**
 * Purpose: 部門維護SERVICE實現類
 * @author barryzhang
 * @since  JDK 1.6
 * @date   2016/6/14
 * @MaintenancePersonnel barryzhang
 */
public class DepartmentService extends AtomicService implements IDepartmentService {
	/**
	 * 系统日志文件控件
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, DepartmentService.class);
	
	/**
	 * 部門維護DAO
	 */
	private IDepartmentDAO departmentDAO;
	
	/**
	 * 使用者DAO
	 */
	private IAdmUserDAO admUserDAO;
	
	/**
	 * Constructor:無參構造函數
	 */
	public DepartmentService() {
		super();
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IDepartmentService#init(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SessionContext init(SessionContext sessionContext) throws ServiceException {
		//部門formDTO
		DepartmentFormDTO departmentManageFormDTO = null;
		try {
			departmentManageFormDTO = (DepartmentFormDTO) sessionContext.getRequestParameter();
			sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS));
			sessionContext.setResponseResult(departmentManageFormDTO);
		} catch (Exception e) {
			if (LOGGER != null) {
				LOGGER.error("init(SessionContext sessionContext):", "error", e);
			}
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IDepartmentService#query(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SessionContext query(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		try {
			DepartmentFormDTO departmentManageFormDTO = (DepartmentFormDTO) sessionContext.getRequestParameter();
			//部門名查詢條件
			String deptName = departmentManageFormDTO.getQueryDeptName();
			//公司查詢條件
			String companyId = departmentManageFormDTO.getQueryCompany();
			//頁碼
			Integer pageIndex = departmentManageFormDTO.getPage();
			//每頁筆數
			Integer pageSize = departmentManageFormDTO.getRows();
			//排序字段
			String orderBy = departmentManageFormDTO.getOrder();
			//升序降序
			String sort = departmentManageFormDTO.getSort();
			//查詢總筆數
			Integer count = null;
			count = departmentDAO.count(deptName, companyId);
			if (count.intValue() > 0) {
				//查詢結果List
				List<BimDepartmentDTO> departmentDTOs = departmentDAO.listBy(null, companyId, 
						deptName, pageSize, pageIndex, orderBy, sort );
				departmentManageFormDTO.getPageNavigation().setRowCount(count.intValue());
				departmentManageFormDTO.setList(departmentDTOs);
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(departmentManageFormDTO);
		} catch (DataAccessException e) {
			if (LOGGER != null) {
				LOGGER.error("query()", "DataAccess Exception:", e);
			}
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			if (LOGGER != null) {
				LOGGER.error("query()", "Exception:", e);
			}
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IDepartmentService#initEdit(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SessionContext initEdit(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		try {
			DepartmentFormDTO departmentManageFormDTO = (DepartmentFormDTO) sessionContext.getRequestParameter();
			BimDepartmentDTO bimDepartmentDTO = new BimDepartmentDTO();
			String deptCode = departmentManageFormDTO.getDeptCode();
			// 如果deptId為空，則為新增初始化反之則為修改
			if (deptCode != null) {
				//根據deptCode得到部門信息
				BimDepartment departmentManage = departmentDAO.findByPrimaryKey(BimDepartment.class, deptCode);
				if (departmentManage != null){
					Transformer transformer = new SimpleDtoDmoTransformer();
					bimDepartmentDTO = (BimDepartmentDTO) transformer.transform(departmentManage, bimDepartmentDTO);
					departmentManageFormDTO.setBimDepartmentDTO(bimDepartmentDTO);
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
				}else{
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INIT_PAGE_FAILURE);
				}
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(departmentManageFormDTO);
		} catch (DataAccessException e) {
			if (LOGGER != null) {
				LOGGER.error("initEdit()", "DataAccess Exception:", e);
			}
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		} catch (Exception e) {
			if (LOGGER != null) {
				LOGGER.error("initEdit()", "Exception:", e);
			}
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IDepartmentService#save(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SessionContext save(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		String deptCode = null;
		boolean isRepate = false;
		try {		
			DepartmentFormDTO bimDepartmentFormDTO = (DepartmentFormDTO) sessionContext.getRequestParameter();
			//獲取登錄信息
			LogonUser logonUser = bimDepartmentFormDTO.getLogonUser();
			//當前登入者的Id
			String userId = null; 
			//當前登入者的Name
			String userName = null;
			if (logonUser != null) {
				userId = logonUser.getId();
				userName = logonUser.getName();
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.LIMITED_LOGON_ID);
				sessionContext.setReturnMessage(msg);
				return sessionContext;
			}
			BimDepartmentDTO bimDepartmentDTO = bimDepartmentFormDTO.getBimDepartmentDTO();
			String companyId = null;
			String deptName = null;
			// 獲得主鍵部門ID
			deptCode = bimDepartmentDTO.getDeptCode();
			//公司ID
			companyId = bimDepartmentDTO.getCompanyId();
			//部門名稱
			deptName = bimDepartmentDTO.getDeptName();
			//檢核同一公司下是否存在重複的部門名稱. //Task #3029  部門維護   邏輯改為 所有 公司下的部門，部門名稱 不能相同 2018/01/02
			isRepate = this.departmentDAO.check(null, deptCode, deptName);
			//如果同一公司下存在重複的部門名稱. 
			if (isRepate) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DEPT_NAME_REPEAT, new String[]{});
				sessionContext.setReturnMessage(msg);
				return sessionContext;
			}
			Transformer transformer = new SimpleDtoDmoTransformer();
			BimDepartment departmentManage = null;
			//新增生成部門主鍵
			String newDeptCode = null;
			// 部門為空則為新增
			if (!StringUtils.hasText(deptCode)) {
				departmentManage = (BimDepartment) transformer.transform(bimDepartmentDTO, new BimDepartment());
				newDeptCode = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BIM_DEPARTMENT);
				departmentManage.setDeptCode(newDeptCode);
				//設置創建者信息
				departmentManage.setCreatedById(userId);
				departmentManage.setCreatedByName(userName);
				departmentManage.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
				//設置更新信息
				bimDepartmentDTO.setUpdatedById(userId);
				bimDepartmentDTO.setUpdatedByName(userName);
				bimDepartmentDTO.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
				//設置刪除標誌
				departmentManage.setDeleted(IAtomsConstants.NO);
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INSERT_SUCCESS, new String[]{this.getMyName()});
			// 如果部門不為空則為修改	
			} else {
				departmentManage = this.departmentDAO.findByPrimaryKey(BimDepartment.class, deptCode);
				//取得數據庫中的創建信息
				bimDepartmentDTO.setCreatedById(departmentManage.getCreatedById());
				bimDepartmentDTO.setCreatedByName(departmentManage.getCreatedByName());
				bimDepartmentDTO.setCreatedDate(departmentManage.getCreatedDate());
				//設置更新信息
				bimDepartmentDTO.setUpdatedById(userId);
				bimDepartmentDTO.setUpdatedByName(userName);
				bimDepartmentDTO.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
				//刪除標誌
				bimDepartmentDTO.setDeleted(departmentManage.getDeleted());
				departmentManage = (BimDepartment) transformer.transform(bimDepartmentDTO, departmentManage);
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.UPDATE_SUCCESS, new String[]{this.getMyName()});
			}
			this.departmentDAO.getDaoSupport().saveOrUpdate(departmentManage);
			sessionContext.setResponseResult(bimDepartmentFormDTO);
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			if (LOGGER != null) {
				LOGGER.error("save()", "DataAccess Exception:", e);
			}
			if (deptCode == null) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INSERT_FAILURE, new String[]{this.getMyName()});
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.UPDATE_FAILURE, new String[]{this.getMyName()});
			}
			throw new ServiceException(msg);
		} catch (Exception e) {
			if (LOGGER != null) {
				LOGGER.error("save()", "Exception:", e);
			}
			if (deptCode == null) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INSERT_FAILURE, new String[]{this.getMyName()});
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.UPDATE_FAILURE, new String[]{this.getMyName()});
			}
			throw new ServiceException(msg);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IDepartmentService#delete(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SessionContext delete(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		try {
			DepartmentFormDTO bimDepartmentFormDTO = (DepartmentFormDTO) sessionContext.getRequestParameter();
			String deptCode = bimDepartmentFormDTO.getDeptCode();
			//獲取登錄信息
			LogonUser logonUser = bimDepartmentFormDTO.getLogonUser();
			//當前登入者的Id
			String userId = null; 
			//當前登入者的Name
			String userName = null;
			if (logonUser != null) {
				userId = logonUser.getId();
				userName = logonUser.getName();
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.LIMITED_LOGON_ID);
				sessionContext.setReturnMessage(msg);
				return sessionContext;
			}
			//判斷是否有主鍵
			if (StringUtils.hasText(deptCode)) {
				BimDepartment department = departmentDAO.findByPrimaryKey(BimDepartment.class, deptCode);
				if (department != null) {
					//檢核該部門下是否設定使用者帳號資料
					List<AdmUserDTO> admUserDTOs = this.admUserDAO.listBy(null, deptCode);
					if(!CollectionUtils.isEmpty(admUserDTOs)){
						sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.EXISTED_USER_WITH_DEPT_CODE));
						return sessionContext;
					}
					//設置刪除標誌
					department.setDeleted(IAtomsConstants.YES);
					//設置刪除者信息
					department.setUpdatedById(userId);
					department.setUpdatedByName(userName);
					department.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
					this.departmentDAO.getDaoSupport().update(department);
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_SUCCESS, new String[]{this.getMyName()});
				} else{
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()});
				}
			} 
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(bimDepartmentFormDTO);
		} catch (DataAccessException e) {
			if (LOGGER != null) {
				LOGGER.error("delete()", "DataAccess Exception:", e);
			}
			throw new ServiceException(CoreMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			if (LOGGER != null) {
				LOGGER.error("delete()", "Exception:", e);
			}
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IDepartmentService#getDeptList(java.lang.String)
	 */
	@Override
	public List<Parameter> getDeptList(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		try {
			String companyId = (String) inquiryContext.getParameter(AdmUserDTO.ATTRIBUTE.COMPANY_ID.getValue());
			// 是否忽略刪除
			Boolean ignoreDeleted = null;
			Object ignoreDeletedObject = inquiryContext.getParameter(IAtomsConstants.PARAM_IGNORE_DELETED);
			if (ignoreDeletedObject instanceof Boolean) {
				ignoreDeleted = (Boolean)ignoreDeletedObject;
			}
			if(ignoreDeleted == null){
				ignoreDeleted = Boolean.TRUE;
			}
			return this.departmentDAO.getDeptList(companyId, ignoreDeleted);
		} catch (DataAccessException e) {
			if (LOGGER != null) {
				LOGGER.error("getDeptList()", "DataAccess Exception : ", e);
			}
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			if (LOGGER != null) {
				LOGGER.error("getDeptList()", "Exception : ", e);
			}
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IDepartmentService#getDeptByCompany(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public List<Parameter> getDeptByCompany(MultiParameterInquiryContext inquiryContext) throws ServiceException{
		try {
			String companyId = (String) inquiryContext.getParameter(AdmUserDTO.ATTRIBUTE.COMPANY_ID.getValue());
			Boolean isSplitInfo = Boolean.valueOf(false);
			Object isSplitInfoObject = inquiryContext.getParameter(ContractSlaDTO.ATTRIBUTE.IS_HAVE_SLA.getValue());
			if(isSplitInfoObject instanceof Boolean){
				isSplitInfo = (Boolean) isSplitInfoObject;
			} 
			return this.departmentDAO.getDeptByCompany(companyId, isSplitInfo);
		} catch (DataAccessException e) {
			if (LOGGER != null) {
				LOGGER.error("getDeptByCompany()", "DataAccess Exception : ", e);
			}
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			if (LOGGER != null) {
				LOGGER.error("getDeptByCompany()", "Exception : ", e);
			}
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IDepartmentService#getDepartmentList(java.lang.String)
	 */
	@Override
	public List<Parameter> getDepartmentList(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		List<Parameter> result = null;
		try {
			long startQueryDeptTime = System.currentTimeMillis();
			String companyId = (String) inquiryContext.getParameter(CompanyDTO.ATTRIBUTE.COMPANY_ID.getValue());
			String deptCode = (String) inquiryContext.getParameter(BimDepartmentDTO.ATTRIBUTE.DEPT_CODE.getValue());
			result = this.departmentDAO.getDepartmentList(companyId, deptCode);
			long endQueryDeptTime = System.currentTimeMillis();
			LOGGER.debug("calculate time --> load", "Service getDepartmentList:" + (endQueryDeptTime - startQueryDeptTime));
			return result;
		} catch (DataAccessException e) {
			if (LOGGER != null) {
				LOGGER.error("getDepartmentList()", "DataAccess Exception : ", e);
			}
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			if (LOGGER != null) {
				LOGGER.error("getDepartmentList()", "Exception : ", e);
			}
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * @return the departmentDAO
	 */
	public IDepartmentDAO getDepartmentDAO() {
		return departmentDAO;
	}

	/**
	 * @param departmentDAO the departmentDAO to set
	 */
	public void setDepartmentDAO(IDepartmentDAO departmentDAO) {
		this.departmentDAO = departmentDAO;
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