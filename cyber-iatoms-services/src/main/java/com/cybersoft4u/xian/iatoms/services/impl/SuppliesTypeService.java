package com.cybersoft4u.xian.iatoms.services.impl;

import java.util.List;

import org.springframework.util.CollectionUtils;

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

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmSuppliesDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.PaymentFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.SuppliesTypeFormDTO;
import com.cybersoft4u.xian.iatoms.services.ISuppliesTypeService;
import com.cybersoft4u.xian.iatoms.services.dao.ISuppliesTypeDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmSupplies;

/**
 * Purpose: 耗材品項維護service
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2016-7-06
 * @MaintenancePersonnel starwang
 */
public class SuppliesTypeService extends AtomicService implements ISuppliesTypeService{
	
	
	/**
	 * 系统日志文件控件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, SuppliesTypeService.class);
	
	/**
	 * 耗材品DAO
	 */
	private ISuppliesTypeDAO suppliesDAO;
	/**
	 * 構造器
	 */
	public SuppliesTypeService() {
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ISuppliesTypeService#init(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext init(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		try {
			SuppliesTypeFormDTO formDTO = (SuppliesTypeFormDTO) sessionContext.getRequestParameter();
			//Task #3583 當 【客戶廠商角色】 時 不設倉庫控管，可見本公司下所有設備
			setUserRoleAttribute(formDTO);
			sessionContext.setResponseResult(formDTO);
			msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
		} catch (Exception e) {
			LOGGER.error(".init() is error:", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		}
		return sessionContext;
	}
	/**
	 * 判斷用戶角色屬性
	 */
	private void setUserRoleAttribute(SuppliesTypeFormDTO formDTO) {
		IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
		if (logonUser != null) {
			//當前登入者角色屬性
			String userAttribute = null;
			// 是客戶角色
			Boolean isCustomerAttribute = false;
			// 是廠商角色
			Boolean isVendorAttribute = false;
			// Task #3583  是客戶廠商角色
			Boolean isCustomerVendorAttribute = false;
			//得到用戶角色列表
			List<AdmRoleDTO> userRoleList = logonUser.getUserFunctions();
			//遍歷所有的角色
			for (AdmRoleDTO admRoleDTO : userRoleList) {
				userAttribute = admRoleDTO.getAttribute();
				// 是廠商角色
				if (IAtomsConstants.VECTOR_ROLE_ATTRIBUTE.equals(userAttribute)) {
					// 如果即是客戶角色又是廠商角色則設置為廠商角色
					isVendorAttribute = true;
					//Task #3583  客戶廠商      如果即是客戶廠商角色又是廠商角色則設置為廠商角色
				} else if (IAtomsConstants.CUSTOMER_VECTOR_ROLE_ATTRIBUTE.equals(userAttribute)) {
					isCustomerVendorAttribute = true;
					// 是客戶角色
				} else if(IAtomsConstants.CUSTOMER_ROLE_ATTRIBUTE.equals(userAttribute)){
					isCustomerAttribute = true;
				}
			}
			if (isVendorAttribute) {
				//如果即是客戶角色又是廠商角色則設置為廠商角色
				formDTO.setRoleAttribute(IAtomsConstants.VECTOR_ROLE_ATTRIBUTE);
			} else if (isCustomerVendorAttribute) {
				formDTO.setRoleAttribute(IAtomsConstants.CUSTOMER_VECTOR_ROLE_ATTRIBUTE);
			} else if (isCustomerAttribute) {
				formDTO.setRoleAttribute(IAtomsConstants.CUSTOMER_ROLE_ATTRIBUTE);
			}
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ISuppliesTypeService#query(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public SessionContext query(SessionContext sessionContext) throws ServiceException {
		Message msg=null;
		try {
			SuppliesTypeFormDTO suppliesFormDTO = (SuppliesTypeFormDTO) sessionContext.getRequestParameter();
			Integer pageSize =  suppliesFormDTO.getRows();
			Integer page =  suppliesFormDTO.getPage();
			String queryCustomerId = suppliesFormDTO.getQueryCustomerId();
			String querySuppliesCode = suppliesFormDTO.getQuerySuppliesCode();
			String querySuppliesName = suppliesFormDTO.getQuerySuppliesName();
			String order = suppliesFormDTO.getorder();
			String sort = suppliesFormDTO.getSort();
			//Task #3583 當 【客戶廠商角色】 時 不設倉庫控管，可見本公司下所有設備
			setUserRoleAttribute(suppliesFormDTO);
			//查詢當頁數據
			List<DmmSuppliesDTO> suppliesDTOs = this.suppliesDAO.listBy(queryCustomerId, querySuppliesCode, querySuppliesName, pageSize,  page, order, sort);
			if (!CollectionUtils.isEmpty(suppliesDTOs)) {
				suppliesFormDTO.setList(suppliesDTOs);
				//查詢總筆數
				Integer count = this.suppliesDAO.getCount(suppliesFormDTO.getQueryCustomerId(), suppliesFormDTO.getQuerySuppliesCode(), suppliesFormDTO.getQuerySuppliesName());
				suppliesFormDTO.getPageNavigation().setRowCount(count.intValue());
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
			}else{
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
			}
			sessionContext.setResponseResult(suppliesFormDTO);
			sessionContext.setReturnMessage(msg);
			return sessionContext;	
		} catch (DataAccessException e) {
			LOGGER.error(".query() DataAccess Exception:", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".query() Exception:", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ISuppliesTypeService#save(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext save(SessionContext sessionContext) throws ServiceException {
		Message msg = null;	
		LogonUser logonUser = null;
		String suppliesId = null;
		try {
			SuppliesTypeFormDTO formDTO = (SuppliesTypeFormDTO) sessionContext.getRequestParameter();
			//從formdto裡面拿到在controller綁定的dto
			DmmSuppliesDTO  suppliesDTO = formDTO.getSuppliesDTO();			
			//從dto裡面得到此行的ID，如果有ID 就是修改，反之為增加.
			suppliesId = suppliesDTO.getSuppliesId();
			logonUser = formDTO.getLogonUser();
			if(logonUser != null){
				//有ID就是修改
				if (StringUtils.hasText(suppliesDTO.getCompanyId())) {
					if(StringUtils.hasText(suppliesDTO.getSuppliesName())){
						boolean isRepeat = this.suppliesDAO.isCheck(suppliesDTO.getCompanyId(), suppliesDTO.getSuppliesName(), suppliesId);
						if (isRepeat) {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.SUPPLIES_NAME_REPEAT);
							sessionContext.setReturnMessage(msg);
							return sessionContext;
						}
					}
				} 
				if(StringUtils.hasText(suppliesId)){
					DmmSupplies supplies = this.suppliesDAO.findByPrimaryKey(DmmSupplies.class, suppliesId);
					if(supplies != null){
						supplies.setUpdatedById(logonUser.getId());
						supplies.setUpdatedByName(logonUser.getName());
						supplies.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
						supplies.setCompanyId(suppliesDTO.getCompanyId());
						supplies.setSuppliesName(suppliesDTO.getSuppliesName());
						supplies.setSuppliesType(suppliesDTO.getSuppliesType());
						supplies.setPrice(suppliesDTO.getPrice());
						msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.UPDATE_SUCCESS, new String[]{this.getMyName()});
						this.suppliesDAO.getDaoSupport().saveOrUpdate(supplies);
					}else{
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.UPDATE_FAILURE, new String[]{this.getMyName()});
					}
				}else{
					//格式轉換方法
					Transformer transformer = new SimpleDtoDmoTransformer();
					DmmSupplies supplies = (DmmSupplies) transformer.transform(suppliesDTO, new DmmSupplies());
					suppliesId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_SUPPLIES);
					supplies.setSuppliesId(suppliesId);
					supplies.setCreatedById(logonUser.getId());
					supplies.setCreatedByName(logonUser.getName());
					supplies.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
					supplies.setUpdatedById(logonUser.getId());
					supplies.setUpdatedByName(logonUser.getName());
					supplies.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INSERT_SUCCESS, new String[]{this.getMyName()});
					this.suppliesDAO.getDaoSupport().saveOrUpdate(supplies);
					}
			}else{
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.SESSION_INVALIDATED_TIME_OUT);				
			}
			//提交事務
			this.suppliesDAO.getDaoSupport().flush();
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(formDTO);
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error(".save(SessionContext sessionContext):", "service Exception", e);
			if (StringUtils.hasText(suppliesId)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.UPDATE_FAILURE, new String[]{this.getMyName()});
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INSERT_FAILURE, new String[]{this.getMyName()});
			}
			throw new ServiceException(msg);
		} catch (Exception e) {
			LOGGER.error(".save(SessionContext sessionContext):", "service Exception", e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
	}
    /**
     * (non-Javadoc)
     * @see com.cybersoft4u.xian.iatoms.services.ISuppliesTypeService#delete(cafe.core.context.SessionContext)
     */
	@SuppressWarnings({ "unchecked", "rawtypes"})
	@Override
	public SessionContext delete(SessionContext sessionContext) throws ServiceException {
		Message msg=null;
		try {
			//從sessionContext中拿到formdto
			SuppliesTypeFormDTO suppliesFormDTO = (SuppliesTypeFormDTO) sessionContext.getRequestParameter();
			//從formdto中拿到要刪除行的id
			String suppliesId = suppliesFormDTO.getSuppliesId();
			if (StringUtils.hasText(suppliesId)) {
				//通過此條數據的主鍵拿到數據
				DmmSupplies supplies = this.suppliesDAO.findByPrimaryKey(DmmSupplies.class, suppliesId);
				//如果通過ID從數據庫查詢到的dmo不為空
				if (supplies != null) {
					this.suppliesDAO.getDaoSupport().delete(supplies);
					this.suppliesDAO.getDaoSupport().flush();
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_SUCCESS, new String[]{this.getMyName()});
				}else{
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()});
				}
			}else{
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()});
			}
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOGGER.error(".delete(SessionContext sessionContext):", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(".delete(SessionContext sessionContext):", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ISuppliesTypeService#getSuppliesTypeNameList(cafe.core.context.MultiParameterInquiryContext)
	 */
	public List<Parameter> getSuppliesTypeNameList(MultiParameterInquiryContext param) throws ServiceException {
		List<Parameter> list = null;
		try {
			String customerId = (String) param.getParameter(PaymentFormDTO.PARAM_QUERY_COMPANY_ID);
			String suppliesType = (String) param.getParameter(DmmSuppliesDTO.ATTRIBUTE.SUPPLIES_TYPE.getValue());
			list = this.suppliesDAO.getSuppliesTypes(customerId, suppliesType);
		} catch (DataAccessException e) {
			LOGGER.error(".getSuppliesTypeNameList(MultiParameterInquiryContext param):", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(".getSuppliesTypeNameList(MultiParameterInquiryContext param):", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return list;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ISuppliesTypeService#getSuppliesListByCustomseId(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public List<Parameter> getSuppliesListByCustomseId(
			MultiParameterInquiryContext param) throws ServiceException {
		List<Parameter> list = null;
		try {
			String customerId = (String) param.getParameter(DmmSuppliesDTO.ATTRIBUTE.COMPANY_ID.getValue());
			list = this.suppliesDAO.getSuppliesTypeList(customerId);
		} catch (DataAccessException e) {
			LOGGER.error(".getSuppliesListByCustomseId(MultiParameterInquiryContext param):", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(".getSuppliesListByCustomseId(MultiParameterInquiryContext param):", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return list;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ISuppliesTypeService#getSuppliesNameList(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public List<Parameter> getSuppliesNameList(
			MultiParameterInquiryContext param) throws ServiceException {
		List<Parameter> list = null;
		try {
			String customerId = (String) param.getParameter(DmmSuppliesDTO.ATTRIBUTE.COMPANY_ID.getValue());
			String suppliesType = (String) param.getParameter(DmmSuppliesDTO.ATTRIBUTE.SUPPLIES_TYPE.getValue());
			if(StringUtils.hasText(suppliesType)) {
				list = this.suppliesDAO.getSuppliesNameList(customerId, suppliesType);
			} 
		} catch (DataAccessException e) {
			LOGGER.error(".getSuppliesNameList(MultiParameterInquiryContext param):", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(".getSuppliesNameList(MultiParameterInquiryContext param):", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return list;
	}
	/**
	 * @return the suppliesDAO
	 */
	public ISuppliesTypeDAO getSuppliesDAO() {
		return suppliesDAO;
	}
	/**
	 * @param suppliesDAO the suppliesDAO to set
	 */
	public void setSuppliesDAO(ISuppliesTypeDAO suppliesDAO) {
		this.suppliesDAO = suppliesDAO;
	}
	
}
