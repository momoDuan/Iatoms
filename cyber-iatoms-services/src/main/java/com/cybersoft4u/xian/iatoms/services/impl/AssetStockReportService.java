package com.cybersoft4u.xian.iatoms.services.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.Message;
import cafe.core.bean.Parameter;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.SessionContext;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.parameter.IBaseParameterItemDefDAO;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetStockReportDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetStockReportFormDTO;
import com.cybersoft4u.xian.iatoms.services.IAssetStockReportService;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IAssetStockReportDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IWarehouseDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmUser;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCompany;

/**
 * Purpose: 設備庫存表Service
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016-7-28
 * @MaintenancePersonnel CrissZhang
 */
public class AssetStockReportService extends AtomicService implements IAssetStockReportService {

	/**
	 * 系统日志文件控件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, AssetStockReportService.class);
	/**
	 * 設備庫存表DAO
	 */
	private IAssetStockReportDAO assetStockReportDAO;
	/**
	 * 基本參數DAO
	 */
	private IBaseParameterItemDefDAO baseParameterItemDefDAO;
	
	/**
	 * 公司DAO
	 */
	private ICompanyDAO companyDAO;

	/**
	 * 倉管DAO
	 */
	private IWarehouseDAO warehouseDAO;
	
	/**
	 * 使用者DAO
	 */
	private IAdmUserDAO admUserDAO;
	/**
	 * 無慘構造函數
	 */
	public AssetStockReportService() {
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMailListService#init(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext init(SessionContext sessionContext) throws ServiceException {
		// 页面显示信息
		Message msg = null;
		try {
			AssetStockReportFormDTO formDTO = (AssetStockReportFormDTO) sessionContext.getRequestParameter();
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
			// 設置當前年月
			//Timestamp currentDate = DateTimeUtils.getCurrentTimestamp();
			formDTO.setCurrentDate(DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH));
			formDTO.setIsNoRoles(isNoRoles);
			sessionContext.setResponseResult(formDTO);
			msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+".init(SessionContext sessionContext):" + e, e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetStockReportService#query(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext query(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		try {
			AssetStockReportFormDTO formDTO = (AssetStockReportFormDTO)sessionContext.getRequestParameter();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
			//Boolean emptyWarehouse = false;
			if(!formDTO.getIsNoRoles()){
				String userId = logonUser.getId();
				AdmUser admUser = (AdmUser) this.admUserDAO.findByPrimaryKey(AdmUser.class, userId);
				String dataAcl = null;
				if(admUser != null){
					//倉庫控管
					dataAcl = admUser.getDataAcl();
				}
				//角色屬性
				String vender = formDTO.getRoleAttribute();
				//List<Parameter> tempWarehouseList = null;
				/*List<String> warehouseList = new ArrayList<String>();
				if(IAtomsConstants.YES.equals(dataAcl)){
					tempWarehouseList = this.warehouseDAO.getWarehouseByUserId(userId);
				} else {
					tempWarehouseList = this.warehouseDAO.getWarehouseByUserId(null);
				}
				if(!CollectionUtils.isEmpty(tempWarehouseList)){
					for(Parameter warehouse : tempWarehouseList){
						warehouseList.add((String)warehouse.getValue());
					}
				} else {
					emptyWarehouse = true;
				}*/
				//維護模式
				String queryMaintainMode = formDTO.getQueryMaintainMode();
				//查找月份
				String queryMonth = formDTO.getQueryMonth();
				String monthString = formDTO.getQueryMonth();
				//查詢廠商編號
				String queryCustomerId = formDTO.getQueryCustomerId();
				//查找設備狀態的列表
				List<Parameter> listAssetCategoryList = (List<Parameter>) this.baseParameterItemDefDAO.getAvailableParameterItems(IATOMS_PARAM_TYPE.ASSET_CATEGORY.getCode(), null);
				if (!CollectionUtils.isEmpty(listAssetCategoryList)) {
					formDTO.setListAssetCategory(listAssetCategoryList);
				}
				if (StringUtils.hasText(queryMonth)) {
					queryMonth = queryMonth.replace(IAtomsConstants.MARK_BACKSLASH, IAtomsConstants.MARK_EMPTY_STRING);
					formDTO.setQueryMonth(queryMonth);
				}
			/*	String[] maintainMode = queryMaintainMode.split(IAtomsConstants.MARK_SEPARATOR);
				String queryMaintainModeTemp = "";
				if (StringUtils.hasText(queryMaintainMode)) {
					for (String temp : maintainMode) {
						queryMaintainModeTemp += IAtomsConstants.MARK_QUOTES + temp + IAtomsConstants.MARK_QUOTES +IAtomsConstants.MARK_SEPARATOR;
					}
					queryMaintainModeTemp = queryMaintainModeTemp.substring(0, queryMaintainModeTemp.length()-1).trim();
				}
				formDTO.setQueryMaintainMode(queryMaintainModeTemp);*/
				//得到系統當前月份
				String strCurrentDate = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMM);
				//查詢月份小於系統當前月份，則從設備資料月檔抓取資料；反之，則從設備最新資料檔抓取資料。
				String queryTableName = IAtomsConstants.IATOMS_TB_NAME_REPOSITORY_HIST_MONTHLY;
				if (strCurrentDate.equals(queryMonth)) {
					queryTableName = IAtomsConstants.IATOMS_TB_NAME_REPOSITORY;
				}
				//獲取匯出的集合
				List<AssetStockReportDTO> assetStockReportDTOs = this.assetStockReportDAO.listBy(queryTableName, queryCustomerId,
						queryMaintainMode, queryMonth, vender, dataAcl, admUser.getId());
				if (!CollectionUtils.isEmpty(assetStockReportDTOs)) {
					List<String> companyIdList= new ArrayList<String>();
					for (AssetStockReportDTO deviceStockTableDTO:assetStockReportDTOs) {
						companyIdList.add(deviceStockTableDTO.getCompanyId());
					}
					//去除List中重複的客戶--LinkedHashSet有序
					HashSet set = new LinkedHashSet(companyIdList); 
					companyIdList.clear(); 
					companyIdList.addAll(set);
					//移除所有的null
					List<Integer> nullList = new ArrayList<Integer>();  
					nullList.add(null); 
					companyIdList.removeAll(nullList); 
					//Collections.sort(companyIdList,Collator.getInstance(java.util.Locale.ENGLISH));
					formDTO.setCompanyIdList(companyIdList);
					formDTO.setAssetStockReportDTOs(assetStockReportDTOs);
					formDTO.setQueryMonth(monthString);
					msg = new Message(Message.STATUS.SUCCESS);
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
				}
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(formDTO);
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".query() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+".query(SessionContext sessionContext):" + e, e);
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
		SessionContext ctx = query(sessionContext);
		/*try {
			AssetStockReportFormDTO formDTO = (AssetStockReportFormDTO)sessionContext.getRequestParameter();
			List<AssetStockReportDTO> assetStockReportDTOs = formDTO.getAssetStockReportDTOs();
			formDTO.setAssetStockReportDTOs(assetStockReportDTOs);
			ctx.setResponseResult(formDTO);
		}  catch (Exception e) {
			logger.error(this.getClass().getName()+".export(SessionContext sessionContext):" + e, e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}*/
		return ctx;
	}

	
	/**
	 * @return the assetStockReportDAO
	 */
	public IAssetStockReportDAO getAssetStockReportDAO() {
		return assetStockReportDAO;
	}

	/**
	 * @param assetStockReportDAO the assetStockReportDAO to set
	 */
	public void setAssetStockReportDAO(IAssetStockReportDAO assetStockReportDAO) {
		this.assetStockReportDAO = assetStockReportDAO;
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
	 * @return the warehouseDAO
	 */
	public IWarehouseDAO getWarehouseDAO() {
		return warehouseDAO;
	}

	/**
	 * @param warehouseDAO the warehouseDAO to set
	 */
	public void setWarehouseDAO(IWarehouseDAO warehouseDAO) {
		this.warehouseDAO = warehouseDAO;
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
