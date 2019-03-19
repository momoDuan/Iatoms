package com.cybersoft4u.xian.iatoms.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.Message;
import cafe.core.bean.Parameter;
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
import com.cybersoft4u.xian.iatoms.common.bean.dto.ApplicationDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ApplicationFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.CopyPropertiesUtils;
import com.cybersoft4u.xian.iatoms.services.IApplicationService;
import com.cybersoft4u.xian.iatoms.services.dao.IApplicationAssetLinkDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IPvmApplicationDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.PvmApplication;
import com.cybersoft4u.xian.iatoms.services.dmo.PvmApplicationAssetLink;
import com.cybersoft4u.xian.iatoms.services.dmo.PvmApplicationAssetLinkId;

/**
 * Purpose: 程式版本維護Service
 * @author echomou
 * @since  JDK 1.6
 * @date   2016-7-14
 * @MaintenancePersonnel echomou
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ApplicationService extends AtomicService implements IApplicationService {

	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, ApplicationService.class);

	/**
	 * 程式版本DAO
	 */
	private IPvmApplicationDAO applicationDAO;
	
	/**
	 * 程式設備DAO
	 */
	private IApplicationAssetLinkDAO applicationAssetLinkDAO;
	/**
	 * 設備類別DAO
	 */
	private IAssetTypeDAO assetTypeDAO;

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IApplicationService#init(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext init(SessionContext sessionContext)throws ServiceException {
		ApplicationFormDTO applicationFormDTO = null;
		try {
			applicationFormDTO = (ApplicationFormDTO) sessionContext.getRequestParameter();
			sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS));
			sessionContext.setResponseResult(applicationFormDTO);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".init():" + e, e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IApplicationService#query(cafe.core.context.SessionContext)
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException {
		ApplicationFormDTO applicationFormDTO = null;
		try {
			applicationFormDTO = (ApplicationFormDTO) sessionContext.getRequestParameter();
			String companyId = applicationFormDTO.getQueryCompanyId();
			String name = applicationFormDTO.getQueryName();
			String version = applicationFormDTO.getQueryVersion();
			String assetTypeId = applicationFormDTO.getQueryAssetTypeId();
			StringBuffer stringBuffer = new StringBuffer();
			if(StringUtils.hasText(assetTypeId)){
				String[] assetTypeIdArray = assetTypeId.split(",");
				for(int i=0; i < assetTypeIdArray.length - 1; i++){
					stringBuffer.append(IAtomsConstants.MARK_QUOTES + assetTypeIdArray[i] + IAtomsConstants.MARK_QUOTES + IAtomsConstants.MARK_SEPARATOR);
				}
				stringBuffer.append(IAtomsConstants.MARK_QUOTES + assetTypeIdArray[assetTypeIdArray.length - 1] + IAtomsConstants.MARK_QUOTES);
			}
			assetTypeId = stringBuffer.toString();
			String sort = applicationFormDTO.getSort();
			String order = applicationFormDTO.getOrder();
			Integer rows = applicationFormDTO.getRows();
			Integer page = applicationFormDTO.getPage();
			List<ApplicationDTO> applicationDTOList = this.applicationDAO.listByApplication(companyId, name, version, assetTypeId, sort, order, rows, page, null);
			if(!CollectionUtils.isEmpty(applicationDTOList)){
				Integer totalSize = this.applicationDAO.count(companyId, name, version, assetTypeId);
				applicationFormDTO.getPageNavigation().setRowCount(totalSize.intValue());
				for(ApplicationDTO applicationDTO : applicationDTOList){
					String updateByName = applicationDTO.getUpdatedByName();
					if(StringUtils.hasText(updateByName) && updateByName.indexOf("(") > 0 && updateByName.indexOf(")") > 0){
						applicationDTO.setUpdatedByName(updateByName.substring(updateByName.indexOf("(") + 1, updateByName.indexOf(")")));
					}
				}
				applicationFormDTO.setList(applicationDTOList);
				sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS));
			}else{
				sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND));
			}
			sessionContext.setResponseResult(applicationFormDTO);
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".query() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".query() Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IApplicationService#initEdit(cafe.core.context.SessionContext)
	 */
	public SessionContext initEdit(SessionContext sessionContext) throws ServiceException {
		ApplicationFormDTO applicationFormDTO = null;
		try {
			applicationFormDTO = (ApplicationFormDTO) sessionContext.getRequestParameter();
			//根據主鍵獲取程式版本信息
			String applicationId = applicationFormDTO.getApplicationId();
			String assetCategory = applicationFormDTO.getAssetCategory();
			ApplicationDTO applicationDTO = this.applicationDAO.listByApplication(null, null, null, null, null, null, null, null, applicationId).get(0);
			if (applicationDTO != null){
				applicationFormDTO.setApplicationDTO(applicationDTO);
				List<Parameter> assetTypeList = this.applicationDAO.listByAssetType(assetCategory);
				//List<Parameter> assetCategoryList = this.applicationDAO.getAssetListTypeByCustomer(applicationDTO.getCustomerId());
				//applicationFormDTO.setAssetCategoryList(assetCategoryList);
				applicationFormDTO.setAssetTypeList(assetTypeList);
				sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS));
			}else{
				sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_FAILURE));
			}
			sessionContext.setResponseResult(applicationFormDTO);
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".initEdit() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".initEdit():" + e, e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IApplicationService#save(cafe.core.context.SessionContext)
	 */
	public SessionContext save(SessionContext sessionContext) throws ServiceException {
		ApplicationFormDTO applicationFormDTO = null;
		try {
			applicationFormDTO = (ApplicationFormDTO) sessionContext.getRequestParameter();
			ApplicationDTO applicationDTO = applicationFormDTO.getApplicationDTO();
			PvmApplication application = null;
			String applicationId = applicationDTO.getApplicationId();
			String customerId = applicationDTO.getCustomerId();
			String version = applicationDTO.getVersion();
			String applicationName = applicationDTO.getName();
			//得到登錄者
			IAtomsLogonUser logonUser = (IAtomsLogonUser) sessionContext.getLogonUser();
			String userId = null;
			String userName = null;
			if (logonUser != null) {
				//得到當前登入者的Id和Name
				userId = logonUser.getId();
				userName = logonUser.getName();
			} else {
				sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.LIMITED_LOGON_ID));
				return sessionContext;
			}
			//驗證程式名稱，版本編號是否重複
			boolean isReapet = this.applicationDAO.check(applicationId, customerId, version, applicationName);
			if (isReapet) {
				sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.APPLICATION_VERSION_NAME_REPEAT));
				return  sessionContext;
			}
			//得到設備類別
			String assetTypeId = applicationDTO.getAssetTypeId();
			PvmApplicationAssetLink applicationAssetLink = null;
			PvmApplicationAssetLinkId applicationAssetLinkId = null;
			List<PvmApplicationAssetLink> applicationAssetLinkList = new ArrayList<PvmApplicationAssetLink>();
			String[] assetTypeIdArray = null;
			if(StringUtils.hasText(assetTypeId) && !assetTypeId.equals(IAtomsConstants.STRING_NULL)){
				assetTypeIdArray = assetTypeId.split(IAtomsConstants.MARK_SEPARATOR);
			}
			if (StringUtils.hasText(applicationId)) {
				//修改
				application = this.applicationDAO.findByPrimaryKey(PvmApplication.class, applicationId);
				new CopyPropertiesUtils().copyProperties(applicationDTO, application, null);
				application.setUpdatedById(userId);
				application.setUpdatedByName(userName);
				application.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
				//更新程式版本信息
				this.applicationDAO.getDaoSupport().saveOrUpdate(application);
				this.applicationDAO.getDaoSupport().flush();
				//刪除對應的程式設備
				this.applicationDAO.deleteApplicationAssetLink(applicationId);
				this.applicationDAO.getDaoSupport().flush();
				//保存新的程式設備
				if(assetTypeIdArray != null && assetTypeIdArray.length > 0){
					for(int i=0; i < assetTypeIdArray.length; i++) {
						applicationAssetLink = new PvmApplicationAssetLink();
						applicationAssetLinkId = new PvmApplicationAssetLinkId();
						applicationAssetLinkId.setApplicationId(applicationId);
						applicationAssetLinkId.setAssetTypeId(assetTypeIdArray[i]);
						applicationAssetLink.setId(applicationAssetLinkId);
						applicationAssetLinkList.add(applicationAssetLink);
					}
				}
				this.applicationAssetLinkDAO.getDaoSupport().saveOrUpdateAll(applicationAssetLinkList);
				this.applicationAssetLinkDAO.getDaoSupport().flush();
				sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.UPDATE_SUCCESS, new String[]{this.getMyName()}));
			} else {
				//DTO轉換為DMO
				Transformer transformer = new SimpleDtoDmoTransformer();
				application = (PvmApplication) transformer.transform(applicationDTO, new PvmApplication());
				String id =	this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_APPLICATION);
				//設置主鍵
				application.setApplicationId(id);
				application.setCreatedById(userId);
				application.setCreatedByName(userName);
				application.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
				application.setUpdatedById(userId);
				application.setUpdatedByName(userName);
				application.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
				//保存程式版本信息
				this.applicationDAO.getDaoSupport().save(application);
				this.applicationDAO.getDaoSupport().flush();
				if (assetTypeIdArray != null && assetTypeIdArray.length > 0) {
					for(int i=0; i < assetTypeIdArray.length; i++){
						applicationAssetLink = new PvmApplicationAssetLink();
						applicationAssetLinkId = new PvmApplicationAssetLinkId();
						applicationAssetLinkId.setApplicationId(id);
						applicationAssetLinkId.setAssetTypeId(assetTypeIdArray[i]);
						applicationAssetLink.setId(applicationAssetLinkId);
						applicationAssetLinkList.add(applicationAssetLink);
					}
					this.applicationAssetLinkDAO.getDaoSupport().saveOrUpdateAll(applicationAssetLinkList);
					this.applicationAssetLinkDAO.getDaoSupport().flush();
				}
				sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INSERT_SUCCESS, new String[]{this.getMyName()}));
			}
			sessionContext.setResponseResult(applicationFormDTO);
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".save() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		}  catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".save():" + e, e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IApplicationService#delete(cafe.core.context.SessionContext)
	 */
	public SessionContext delete(SessionContext sessionContext) throws ServiceException {
		ApplicationFormDTO applicationFormDTO = null;
		try {
			applicationFormDTO = (ApplicationFormDTO) sessionContext.getRequestParameter();
			String applicationId = applicationFormDTO.getApplicationId();
			PvmApplication application = this.applicationDAO.findByPrimaryKey(PvmApplication.class, applicationId);
			if (application != null) {
				//this.applicationDAO.deleteApplicationAssetLink(applicationId);
				application.setDeleted(IAtomsConstants.PARAM_YES);
				this.applicationDAO.getDaoSupport().saveOrUpdate(application);
				this.applicationDAO.getDaoSupport().flush();
				sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_SUCCESS, new String[]{this.getMyName()}));
			} else {
				sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}));
			}
			sessionContext.setResponseResult(applicationFormDTO);
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".delete() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".delete():" + e, e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IApplicationService#getAssetTypeList(cafe.core.context.SessionContext)
	 */
	public SessionContext getAssetTypeList(SessionContext sessionContext)throws ServiceException{
		ApplicationFormDTO applicationFormDTO = null;
		try {
			applicationFormDTO = (ApplicationFormDTO) sessionContext.getRequestParameter();
			String assetCategory = applicationFormDTO.getAssetCategory();
			Map map = new HashMap();
			if(!StringUtils.hasText(assetCategory)){
				List<Parameter> assetCategoryList = this.assetTypeDAO.getAssetNameList(null, null, null, null, IAtomsConstants.NO);
				map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROW, assetCategoryList);
			}else{
				List<Parameter> assetCategoryList = this.assetTypeDAO.getAssetNameList(null, null, assetCategory, null, IAtomsConstants.NO);
				if(!CollectionUtils.isEmpty(assetCategoryList)){
					map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROW, assetCategoryList);
				}else{
					map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROW, null);
				}
			}
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			sessionContext.setResponseResult(applicationFormDTO);
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".getAssetCategoryList() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".getAssetCategoryList():" + e, e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
		
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IApplicationService#getSoftwareVersions(cafe.core.context.MultiParameterInquiryContext)
	 */
	public List<Parameter> getSoftwareVersions(MultiParameterInquiryContext parameterInquiryContext) throws ServiceException {
		List<Parameter> softwareVersions = null;
		try {
			String customerId = (String) parameterInquiryContext.getParameter(ApplicationDTO.ATTRIBUTE.CUSTOMER_ID.getValue());
			String edcType = (String) parameterInquiryContext.getParameter(ApplicationDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			String edcTypeName = (String) parameterInquiryContext.getParameter(ApplicationDTO.ATTRIBUTE.ASSET_TYPE_NAME.getValue());
			String searchDeletedFlag = (String) parameterInquiryContext.getParameter(ApplicationDTO.ATTRIBUTE.SEARCH_DELETED_FLAG.getValue());
			if (StringUtils.hasText(edcTypeName) && !StringUtils.hasText(edcType)) {
				List<Parameter> assetList = this.assetTypeDAO.getAssetNameList(null, edcTypeName, IAtomsConstants.ASSET_CATEGORY_EDC, null, null);
				if (!CollectionUtils.isEmpty(assetList)) {
					edcType = (String) assetList.get(0).getValue();
				}
			}
			softwareVersions =  this.applicationDAO.listSoftwareVersionsBy(customerId, edcType, searchDeletedFlag);
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".getSoftwareVersions() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".getSoftwareVersions(SessionContext sessionContext):" + e, e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return softwareVersions;
	}
	
	
	/**
	 * @return the applicationDAO
	 */
	public IPvmApplicationDAO getApplicationDAO() {
		return applicationDAO;
	}

	/**
	 * @param applicationDAO the applicationDAO to set
	 */
	public void setApplicationDAO(IPvmApplicationDAO applicationDAO) {
		this.applicationDAO = applicationDAO;
	}
	/**
	 * @return the applicationAssetLinkDAO
	 */
	public IApplicationAssetLinkDAO getApplicationAssetLinkDAO() {
		return applicationAssetLinkDAO;
	}
	/**
	 * @param applicationAssetLinkDAO the applicationAssetLinkDAO to set
	 */
	public void setApplicationAssetLinkDAO(IApplicationAssetLinkDAO applicationAssetLinkDAO) {
		this.applicationAssetLinkDAO = applicationAssetLinkDAO;
	}
	/**
	 * @return the assetTypeDAO
	 */
	public IAssetTypeDAO getAssetTypeDAO() {
		return assetTypeDAO;
	}
	/**
	 * @param assetTypeDAO the assetTypeDAO to set
	 */
	public void setAssetTypeDAO(IAssetTypeDAO assetTypeDAO) {
		this.assetTypeDAO = assetTypeDAO;
	}
	
}
