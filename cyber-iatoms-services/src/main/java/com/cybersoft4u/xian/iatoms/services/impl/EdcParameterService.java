package com.cybersoft4u.xian.iatoms.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cafe.core.bean.Message;
import cafe.core.bean.Parameter;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.SessionContext;
import cafe.core.dao.DataAccessException;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseNewTransactionParameterDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmTransactionParameterItemDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.EdcParameterFormDTO;
import com.cybersoft4u.xian.iatoms.services.IEdcParameterService;
import com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IEdcParameterDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseNewTransactionParameterDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmTransactionParameterItemDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCompany;

/**
 * Purpose: EDC交易參數業務邏輯層實現類
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016/9/22
 * @MaintenancePersonnel CrissZhang
 */
public class EdcParameterService extends AtomicService implements IEdcParameterService{

	/**
	 * 系统日志文件控件
	 */
	private static final CafeLog logger = CafeLogFactory.getLog(GenericConfigManager.SERVICE, EdcParameterService.class);
	
	/**
	 * EDC交易參數DAO
	 */
	private IEdcParameterDAO edcParameterDAO;
	
	/**
	 * 公司DAO
	 */
	private ICompanyDAO companyDAO;
	
	/**
	 * 最新交易參數DAO
	 */
	private ISrmCaseNewTransactionParameterDAO srmCaseNewTransactionParameterDAO;
	
	/**
	 * 交易參數設定DAO
	 */
	private ISrmTransactionParameterItemDAO srmTransactionParameterItemDAO;
	/**
	 * Constructor:無參構造器
	 */
	public EdcParameterService(){
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IEdcParameterService#init(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext init(SessionContext sessionContext) throws ServiceException {
		// 页面显示信息
		Message msg = null;
		try {
			EdcParameterFormDTO formDTO = (EdcParameterFormDTO) sessionContext.getRequestParameter();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
			//當前登入者對應之公司
			String logonUserCompanyId = logonUser.getAdmUserDTO().getCompanyId();
			//當前登入者角色屬性
			String userAttribute = null;
			//得到用戶角色列表
			List<AdmRoleDTO> userRoleList = logonUser.getUserFunctions();
			Boolean isCustomerAttribute = true;
			Boolean isNoRoles = false;
			if(StringUtils.hasText(logonUserCompanyId)){
				BimCompany bimCompany = (BimCompany) this.companyDAO.findByPrimaryKey(BimCompany.class, logonUserCompanyId);
				if(bimCompany != null){
					if(IAtomsConstants.YES.equals(bimCompany.getDeleted())){
						isNoRoles = true;
					}
				}
			}
			if(!CollectionUtils.isEmpty(userRoleList)){
				for (AdmRoleDTO admRoleDTO : userRoleList) {
					userAttribute = admRoleDTO.getAttribute();
					//是廠商角色
					if (IAtomsConstants.VECTOR_ROLE_ATTRIBUTE.equals(userAttribute)) {
						//如果即是客戶角色又是廠商角色則設置為廠商角色
						formDTO.setRoleAttribute(IAtomsConstants.VECTOR_ROLE_ATTRIBUTE);
						isCustomerAttribute = false;
						break;
					} else {
						formDTO.setRoleAttribute(IAtomsConstants.CUSTOMER_ROLE_ATTRIBUTE);
					}
				}
			} else {
				isNoRoles = true;
			}
			//登入者如果是客戶屬性，則顯示對應之公司信息
			if (isCustomerAttribute) {
				formDTO.setLogonUserCompanyId(logonUserCompanyId);
			}
			formDTO.setIsNoRoles(isNoRoles);
			sessionContext.setResponseResult(formDTO);
			msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+".init(SessionContext sessionContext):" + e, e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IEdcParameterService#getItemList(cafe.core.context.SessionContext)
	 */
	public List<Parameter> getItemList(SessionContext sessionContext) throws ServiceException {
		// 页面显示信息
		Message msg = null;
		List<Parameter> transactionParameterItemList = null;
		try {
			transactionParameterItemList = edcParameterDAO.getItemList();
			msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+".init(SessionContext sessionContext):" + e, e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return transactionParameterItemList;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IEdcParameterService#query(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext query(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		try {
			EdcParameterFormDTO edcParameterFormDTO = (EdcParameterFormDTO) sessionContext.getRequestParameter();
			if(!edcParameterFormDTO.getIsNoRoles()){
				//查詢當頁數據
				List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOs = edcParameterDAO.listBy(edcParameterFormDTO);
				if (!CollectionUtils.isEmpty(srmCaseHandleInfoDTOs)) {
					edcParameterFormDTO.setList(srmCaseHandleInfoDTOs);
					//查詢總筆數
					int count = edcParameterDAO.count(edcParameterFormDTO);
					edcParameterFormDTO.getPageNavigation().setRowCount(count);
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
				} else {
					msg = new Message(Message.STATUS.FAILURE,IAtomsMessageCode.DATA_NOT_FOUND);
				}
			} else {
				msg = new Message(Message.STATUS.FAILURE,IAtomsMessageCode.DATA_NOT_FOUND);
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(edcParameterFormDTO);
			return sessionContext;
		} catch (DataAccessException e) {
			logger.error(this.getClass().getName() + ".query() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ".query() Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IEdcParameterService#export(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext export(SessionContext sessionContext) throws ServiceException {
		try {
			EdcParameterFormDTO edcParameterFormDTO = (EdcParameterFormDTO) sessionContext.getRequestParameter();
			//查詢當頁數據
			List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOs = edcParameterDAO.listBy(edcParameterFormDTO);
			String versionDate = DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDD_DASH);
			// 交易參數交易項配置檔
			List<SrmTransactionParameterItemDTO> srmTransactionParameterItemDTOs = srmTransactionParameterItemDAO.listby(versionDate);
			List<SrmCaseNewTransactionParameterDTO> transactionParameterDTOs = null;
			if (!CollectionUtils.isEmpty(srmCaseHandleInfoDTOs)) {
				for(SrmCaseHandleInfoDTO srmCaseHandleInfoDTO : srmCaseHandleInfoDTOs){
					if(StringUtils.hasText(srmCaseHandleInfoDTO.getDtid())){
						transactionParameterDTOs = this.srmCaseNewTransactionParameterDAO.listTransactionParameterDTOsByDtid(srmCaseHandleInfoDTO.getDtid(), true);
					}
					srmCaseHandleInfoDTO.setCaseNewTransactionParameterDTOs(transactionParameterDTOs);
					srmCaseHandleInfoDTO.setSrmTransactionParameterItemDTOs(srmTransactionParameterItemDTOs);
				}
			}
			edcParameterFormDTO.setList(srmCaseHandleInfoDTOs);
			Message msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(edcParameterFormDTO);
		} catch (DataAccessException e) {
			logger.error(this.getClass().getName() + ".export() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+".export(SessionContext sessionContext):" + e, e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IEdcParameterService#getTransactionParams(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext getTransactionParams(SessionContext sessionContext) throws ServiceException {
		try {
			EdcParameterFormDTO edcParameterFormDTO = (EdcParameterFormDTO) sessionContext.getRequestParameter();
			String dtid = edcParameterFormDTO.getDtid();
			List<SrmCaseNewTransactionParameterDTO> caseNewTransactionParameterDTOs = this.srmCaseNewTransactionParameterDAO.listTransactionParameterDTOsByDtid(dtid, true);
			Map map = new HashMap();
			if (!CollectionUtils.isEmpty(caseNewTransactionParameterDTOs)) {
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
				map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, caseNewTransactionParameterDTOs.size());
				map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, caseNewTransactionParameterDTOs);
			} else {
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
				map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, 0);
				map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, "");
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.DATA_NOT_FOUND));
			}
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			return sessionContext;
		} catch (DataAccessException e) {
			logger.error(this.getClass().getName() + ".getTransactionParams() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ".getTransactionParams() Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}

	/**
	 * @return the edcParameterDAO
	 */
	public IEdcParameterDAO getEdcParameterDAO() {
		return edcParameterDAO;
	}

	/**
	 * @param edcParameterDAO the edcParameterDAO to set
	 */
	public void setEdcParameterDAO(IEdcParameterDAO edcParameterDAO) {
		this.edcParameterDAO = edcParameterDAO;
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
	 * @return the srmCaseNewTransactionParameterDAO
	 */
	public ISrmCaseNewTransactionParameterDAO getSrmCaseNewTransactionParameterDAO() {
		return srmCaseNewTransactionParameterDAO;
	}

	/**
	 * @param srmCaseNewTransactionParameterDAO the srmCaseNewTransactionParameterDAO to set
	 */
	public void setSrmCaseNewTransactionParameterDAO(
			ISrmCaseNewTransactionParameterDAO srmCaseNewTransactionParameterDAO) {
		this.srmCaseNewTransactionParameterDAO = srmCaseNewTransactionParameterDAO;
	}

	/**
	 * @return the srmTransactionParameterItemDAO
	 */
	public ISrmTransactionParameterItemDAO getSrmTransactionParameterItemDAO() {
		return srmTransactionParameterItemDAO;
	}

	/**
	 * @param srmTransactionParameterItemDAO the srmTransactionParameterItemDAO to set
	 */
	public void setSrmTransactionParameterItemDAO(
			ISrmTransactionParameterItemDAO srmTransactionParameterItemDAO) {
		this.srmTransactionParameterItemDAO = srmTransactionParameterItemDAO;
	}

}
