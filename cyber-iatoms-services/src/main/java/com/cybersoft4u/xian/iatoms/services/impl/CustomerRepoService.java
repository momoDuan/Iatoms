package com.cybersoft4u.xian.iatoms.services.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.RepoStatusReportDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.CustomerRepoFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.IAtomsUtils;
import com.cybersoft4u.xian.iatoms.services.ICustomerRepoService;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ICustomerRepoDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IWarehouseDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmUser;

/**
 * 
 * Purpose: 客戶設備總表Service 實現類
 * @author HermanWang
 * @since  JDK 1.7
 * @date   2016年7月29日
 * @MaintenancePersonnel HermanWang
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CustomerRepoService extends AtomicService implements
		ICustomerRepoService {

	/**
	 * 系統日志記錄
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, CustomerRepoService.class);

	/**
	 * 客戶設備總表DAO
	 */
	private ICustomerRepoDAO customerRepoDAO;
	/**
	 * 使用者DAO
	 */
	private IAdmUserDAO admUserDAO;
	/**
	 * 倉庫DAO
	 */
	private IWarehouseDAO warehouseDAO;
	/**
	 * Constructor: 無參構造
	 */
	public CustomerRepoService() {
		super();
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICustomerRepoService#init(cafe.core.context.SessionContext)
	 */
	public SessionContext init(SessionContext sessionContext)
			throws ServiceException {
		try{
			if(sessionContext != null){
				CustomerRepoFormDTO formDTO = (CustomerRepoFormDTO) sessionContext.getRequestParameter();
				Timestamp currentDate = DateTimeUtils.getCurrentTimestamp();
				formDTO.setCurrentDate(currentDate);
				Message msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
				IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
				//當前登入者對應之公司
				String  logonUserCompanyId = logonUser.getAdmUserDTO().getCompanyId();
				//當前登入者角色屬性
				String userAttribute = null;
				//得到用戶角色列表
				List<AdmRoleDTO> userRoleList = logonUser.getUserFunctions();
				// 是客戶角色
				Boolean isCustomerAttribute = false;
				// 是廠商角色
				Boolean isVendorAttribute = false;
				// Task #3583  是客戶廠商角色
				Boolean isCustomerVendorAttribute = false;
				//遍歷所有的角色
				for (AdmRoleDTO admRoleDTO : userRoleList) {
					userAttribute = admRoleDTO.getAttribute();
					//是廠商角色
					if (IAtomsConstants.VECTOR_ROLE_ATTRIBUTE.equals(userAttribute)) {
						//如果即是客戶角色又是廠商角色則設置為廠商角色
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
				} else if (isCustomerVendorAttribute) {
					//Task #3583  客戶廠商
					formDTO.setLogonUserCompanyId(logonUserCompanyId);
				} else if (isCustomerAttribute) {
					//登入者如果是客戶屬性，則顯示對應之公司信息
					formDTO.setLogonUserCompanyId(logonUserCompanyId);
				}
				sessionContext.setReturnMessage(msg);
				sessionContext.setResponseResult(formDTO);
			}
		}catch(Exception e){
			LOGGER.error(".init() is error: ", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICustomerRepoService#query(cafe.core.context.SessionContext)
	 */
	public SessionContext queryData(SessionContext sessionContext)
			throws ServiceException {
		try{
			Map map = new HashMap();
			//Message msg = null;
			//key是客戶.
			Map<String, Map<String, Map<String, Integer>>> shortNameMap = null;
			//key是設備狀態
			Map<String, Map<String, Integer>> assetStatusMap = null;
			//key是設備名稱 value 是此設備的數量
			Map<String, Integer> assetMap = null;
			List<String>  assetCategoryKeyList = new ArrayList<String>();
			//存放edc裡面的設備名稱的list
			List<String> assetEDCList = new ArrayList<String>();
			//存放周旁設備裡面的設備名稱的list
			List<String> assetList = new ArrayList<String>();
			//匯出方法中查詢出結果集
			sessionContext = this.export(sessionContext);			
			if(sessionContext != null){
				CustomerRepoFormDTO formDTO = (CustomerRepoFormDTO) sessionContext.getRequestParameter();
				IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
				//當前登入者對應之公司
				String  logonUserCompanyId = logonUser.getAdmUserDTO().getCompanyId();
				//當前登入者角色屬性
				String userAttribute = null;
				//得到用戶角色列表
				List<AdmRoleDTO> userRoleList = logonUser.getUserFunctions();
				// 是客戶角色
				Boolean isCustomerAttribute = false;
				// 是廠商角色
				Boolean isVendorAttribute = false;
				// Task #3583  是客戶廠商角色
				Boolean isCustomerVendorAttribute = false;
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
				} else if (isCustomerVendorAttribute) {
					//Task #3583  客戶廠商
					formDTO.setLogonUserCompanyId(logonUserCompanyId);
				} else if (isCustomerAttribute) {
					//登入者如果是客戶屬性，則顯示對應之公司信息
					formDTO.setLogonUserCompanyId(logonUserCompanyId);
				}
				//傳入頁面的json字符串
				String jsonData = "";
				int page = (formDTO.getPage()).intValue();
				int rows = (formDTO.getRows()).intValue();
				int startIndex = 0;
				int endIndex = 0;
				startIndex = (page - 1) * rows;
				endIndex = page * rows;
				//獲取查詢結果集
				List<RepoStatusReportDTO> results = formDTO.getList();
				if(!CollectionUtils.isEmpty(results)){
					String shortName = null;
					shortNameMap = new LinkedHashMap<String, Map<String,Map<String,Integer >>>();
					String assetName = null;
					Map<String ,String> assetStatus = formDTO.getAssetStatus();
					//拿到設備狀態的列表
					Set<String> keySet = assetStatus.keySet();
					//循環處理，將數據封裝成以下類型  <客戶,<設備狀態,<設備名稱,數量>>> map =new HashMap（）；
					for (RepoStatusReportDTO repoStatusReportDTO : results) {
						//客戶名稱
						shortName = repoStatusReportDTO.getShortName();
						//設備所屬類別
						String assetCategory = repoStatusReportDTO.getAssetCategory();
						if(assetCategory.equals(IAtomsConstants.ASSET_CATEGORY_RODUND_ASSET)){
							assetCategory = IAtomsConstants.ASSET_CATEGORY_RELATED_PRODUCTS;
						}
						assetName = repoStatusReportDTO.getAssetTypeName();
						//設備類別list
						if (!assetCategoryKeyList.contains(assetCategory)) {
							assetCategoryKeyList.add(assetCategory);
						}
						//如果設備類別為edc.往edc的list裡面加.
						if (assetCategory.equals(IAtomsConstants.ASSET_CATEGORY_EDC)) {
							if (!assetEDCList.contains(assetName)) {
								assetEDCList.add(assetName);
							}
						} else {
							if (!assetList.contains(assetName)) {
								assetList.add(assetName);
							}
						}
						if (shortName != null) {
							if (!shortNameMap.containsKey(shortName)) {
								assetStatusMap = new LinkedHashMap<String,Map<String,Integer >>();
								shortNameMap.put(shortName, assetStatusMap);
							} else {
								assetStatusMap = shortNameMap.get(shortName);
							}
							//循環設備狀態
							for(String status : keySet){
								if(!status.equals(IAtomsConstants.STRING_GRAND)){
									if (!assetStatusMap.containsKey(status)) {
										assetMap = new LinkedHashMap<String, Integer>();
										assetStatusMap.put(status, assetMap);
									} else {
										assetMap = assetStatusMap.get(status);
									}
									int assetCount = 0;
									//利用反射得到此設備狀態的設備數量
									assetCount = Integer.parseInt(IAtomsUtils.getFiledValueByName(status, repoStatusReportDTO));
									//只把有數據的往map裡面加.
									if(assetCount != 0){
										assetMap.put(assetName, Integer.valueOf(assetCount));
									}
								}
							}
						}
					}
					String status = null;
					StringBuilder builder = new StringBuilder();
					int i = -1;
					int assetCount = 0;
					//循環三個map，去處理面的數據
					//map的類型 為 <客戶,<設備狀態,<設備名稱,數量>>> map =new HashMap（）；
					for(Entry<String, Map<String, Map<String, Integer>>> shortNameEntry : shortNameMap.entrySet()) {
						//客戶
						shortName = shortNameEntry.getKey();
						//取出來中間層的assetStatusMap 類型為 <設備狀態,<設備名稱,數量>> assetStatusMap = new HashMap();
						assetStatusMap = shortNameEntry.getValue();
						for(Entry<String, Map<String, Integer>> assetStatusEntry : assetStatusMap.entrySet()) {
							//取出來設備狀態
							status = assetStatusEntry.getKey();
							//取出來最裡層的小map assetMap 類型為<設備名稱,設備數量> map = new HashMap(); 
							assetMap = assetStatusEntry.getValue();
							//循環的時候加分頁條件判斷循環
							i ++;
							if (i < startIndex) {
								continue;
							} else if (i >= endIndex) {
								break;
							}
							//builder.append("{'shortName':'" + shortName + "','status':'" + assetStatus.get(status).substring(1, assetStatus.get(status).length()) + "'");
							builder.append("{'shortName':'").append(shortName).append("','status':'").append(assetStatus.get(status).substring(1, assetStatus.get(status).length())).append("'");
							for(Entry<String, Integer> assetEntry : assetMap.entrySet()){
								assetName = assetEntry.getKey();
								assetCount = (assetEntry.getValue()).intValue();
								//拼接json字符串
								//builder.append(",'"+assetName+"':'"+assetCount+"'");
								builder.append(",'").append(assetName).append("':'").append(assetCount).append("'");
							}
							builder.append("},");
						}
					}
					if (builder.length() > 0) {
						jsonData = IAtomsConstants.MARK_BRACKET_LEFT + builder.substring(0, builder.length() - 1) + IAtomsConstants.MARK_BRACKET_RIGHT;
					}
				} else {
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, IAtomsMessageCode.DATA_NOT_FOUND);
				}
				formDTO.setAssetEDCList(assetEDCList);
				formDTO.setAssetList(assetList);
				formDTO.setAssetCategoryKeyList(assetCategoryKeyList);
				formDTO.setJsonData(jsonData);
				sessionContext.setResponseResult(formDTO);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.valueOf(true));
				map.put(IAtomsConstants.ASSET_EDC_LIST, assetEDCList);
				map.put(IAtomsConstants.ASSET_LIST, assetList);
				map.put(IAtomsConstants.ASSET_CATEGORY_KEY_LIST, assetCategoryKeyList);
				map.put(IAtomsConstants.JSON_DATA, jsonData);
				map.put(IAtomsConstants.COUNT, Integer.valueOf(formDTO.getPageNavigation().getRowCount()));
				sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			}
		}catch(DataAccessException e){
			LOGGER.error(".query() is error in DataAccess: ", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE);
		}catch(Exception e){
			LOGGER.error(".query() is error in Service: ", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED);
		}
		return sessionContext;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICustomerRepoService#export(cafe.core.context.SessionContext)
	 */
	public SessionContext export(SessionContext sessionContext)
			throws ServiceException {
		try{
			//Map map1 = new HashMap();
			if(sessionContext != null){
				CustomerRepoFormDTO formDTO = (CustomerRepoFormDTO) sessionContext.getRequestParameter();
				Message message = null;
				if(formDTO != null){
					String userId = formDTO.getLogonUser().getId();
					Integer pageSize =  formDTO.getRows();
					Integer page =  formDTO.getPage();
					String order = formDTO.getOrder();
					String sort = formDTO.getSort();
					String queryCustomer = formDTO.getQueryCustomer();
					String yyyyMM = formDTO.getYyyyMM();
					String queryMaType = formDTO.getQueryMaType();
					String strCurrentDate = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMM);
					String tableName = null;
					//根據查詢條件的月份，判讀是從那個表查數據
					if(strCurrentDate.equals(yyyyMM)){
						tableName = IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY;
					} else {
						tableName = IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY_MONTHLY;
					}
					IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
					//當前登入者角色屬性
					String userAttribute = null;
					//得到用戶角色列表
					List<AdmRoleDTO> userRoleList = logonUser.getUserFunctions();
					// 是客戶角色
					Boolean isCustomerAttribute = false;
					// 是廠商角色
					Boolean isVendorAttribute = false;
					// Task #3583  是客戶廠商角色
					Boolean isCustomerVendorAttribute = false;
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
						//Task #3583  客戶廠商
					} else if (isCustomerAttribute) {
						formDTO.setRoleAttribute(IAtomsConstants.CUSTOMER_ROLE_ATTRIBUTE);
						//登入者如果是客戶屬性，則顯示對應之公司信息
					}
					if (isCustomerVendorAttribute) {
						isCustomerAttribute = true;
					}
					//加倉庫控管角色
					AdmUser admUser = (AdmUser) this.admUserDAO.findByPrimaryKey(AdmUser.class, userId);
					String dataAcl = IAtomsConstants.MARK_EMPTY_STRING;
					if(admUser != null) {
						dataAcl = admUser.getDataAcl();
					}
					//查詢出來的dtolist，需要給total賦值
					List<RepoStatusReportDTO> results = this.customerRepoDAO.list(tableName, isCustomerAttribute, userId, dataAcl, yyyyMM, queryCustomer, queryMaType, pageSize, page, order, sort);
					if(!CollectionUtils.isEmpty(results)){
						formDTO.setList(results);
						int total = 0;
						//將總計的設備狀態求和，並且加入到設備狀態的列表中
						for(RepoStatusReportDTO dto : results){
							total = dto.getInStorage() + dto.getInApply()
									+ dto.getInBorrow() + dto.getInTrans()
									+ dto.getInUse() + dto.getInRepair()
									+ dto.getToScrap() + dto.getInScrap()
									+ dto.getInDestroy() + dto.getInPrep()
									+ dto.getInMaintenance()+ dto.getInLost()
									+ dto.getInReturned();
							dto.setTotal(total);
						}
						formDTO.setQueryFlag(IAtomsConstants.YES);
					}else{
						message = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DATA_NOT_FOUND);
						formDTO.setQueryFlag(CustomerRepoFormDTO.QUERY_PAGE_FLAG_EMPTY);
					}
					//求出來查詢到的客戶result
					List<RepoStatusReportDTO> result = this.customerRepoDAO.getAssetList(tableName, isCustomerAttribute, userId, dataAcl, yyyyMM, queryCustomer, queryMaType);
					String shortName = null;
					Map<String, List<RepoStatusReportDTO>> map = new HashMap<String, List<RepoStatusReportDTO>>();
					List<RepoStatusReportDTO> list = null;
					//遍歷出來不重複的客戶的數量
					for (RepoStatusReportDTO repoStatusReportDTO : result) {
						shortName = repoStatusReportDTO.getShortName();
						if (shortName != null) {
							list = map.get(shortName);
							if (list == null) {
								list = new ArrayList<RepoStatusReportDTO>();
							}
							list.add(repoStatusReportDTO);
							map.put(shortName, list);
						}
					}
					Map<String ,String> assetStatus = formDTO.getAssetStatus();
					//設備狀態
					Set<String> keySet = null;
					if(!CollectionUtils.isEmpty(assetStatus)) {
						keySet = assetStatus.keySet();
					} else {
						keySet = new HashSet();
					}
					//根據客戶和狀態算出總條數.
					Integer count = Integer.valueOf((map.keySet().size()) * (keySet.size() - 1));
					formDTO.getPageNavigation().setRowCount(count.intValue());
				}
				sessionContext.setResponseResult(formDTO);
				sessionContext.setReturnMessage(message);
			}
		}catch(DataAccessException e){
			LOGGER.error(".export() is error in DataAccess: ", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE);
		}catch(Exception e){
			LOGGER.error(".export() is error in Service: ", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ICustomerRepoService#getAssetNameList(cafe.core.context.SessionContext)
	 */
	public SessionContext getAssetNameList(SessionContext sessionContext)
			throws ServiceException {
		try{
			List<Parameter> results = null;
			MultiParameterInquiryContext param = null;
			if(sessionContext != null){
				//查詢條件
				param = (MultiParameterInquiryContext) sessionContext.getRequestParameter();
				String tableName = IAtomsConstants.MARK_EMPTY_STRING;
				String customer = IAtomsConstants.MARK_EMPTY_STRING;
				String maType = IAtomsConstants.MARK_EMPTY_STRING;
				if(param != null){
					//設備品類編號
					tableName = (String) param.getParameter(CustomerRepoFormDTO.QUERY_PAGE_TABLE_NAME);
					customer = (String) param.getParameter(CustomerRepoFormDTO.QUERY_PAGE_PARAM_CUSTOMER);
					maType = (String) param.getParameter(CustomerRepoFormDTO.QUERY_PAGE_PARAM_MA_TYPES);
					LOGGER.debug(".getAssetNameList() --> tableName: ", tableName);
				}
				//根據條件查詢
				results = this.customerRepoDAO.getAssetNameList(tableName, customer, maType);
				sessionContext.setResponseResult(results);
			}
		}catch(Exception e){
			LOGGER.error(".getAssetNameList() is error: ", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE);
		}
		return sessionContext;
	}

	/**
	 * @return the customerRepoDAO
	 */
	public ICustomerRepoDAO getCustomerRepoDAO() {
		return customerRepoDAO;
	}

	/**
	 * @param customerRepoDAO the customerRepoDAO to set
	 */
	public void setCustomerRepoDAO(ICustomerRepoDAO customerRepoDAO) {
		this.customerRepoDAO = customerRepoDAO;
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

}