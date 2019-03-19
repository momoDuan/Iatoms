package com.cybersoft4u.xian.iatoms.services.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetSupportedCommDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetSupportedFunctionDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetTypeCompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetTypeDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractAssetDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetTypeFormDTO;
import com.cybersoft4u.xian.iatoms.services.IAssetTypeService;
import com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetSupportedComm;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetSupportedCommId;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetSupportedFunction;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetSupportedFunctionId;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetType;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetTypeCompany;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetTypeCompanyId;

/**
 * Purpose: 設備品項維護Service
 * @author HermanWang
 * @since  JDK 1.7
 * @date   2016年7月8日
 * @MaintenancePersonnel HermanWang
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class AssetTypeService extends AtomicService implements
		IAssetTypeService {
	
	/**
	 * 系統日志記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, AssetTypeService.class);
	
	/**
	 * 設備品項維護DAO注入
	 */
	private IAssetTypeDAO assetTypeDAO;
	
	/**
	 * 
	 * Constructor: 構造函數
	 */
	public AssetTypeService() {
		super();
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTypeService#init(cafe.core.context.SessionContext)
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException {
		try{
			if(sessionContext != null){
				AssetTypeFormDTO formDTO = (AssetTypeFormDTO) sessionContext.getRequestParameter();
				Message message = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
				sessionContext.setResponseResult(formDTO);
				sessionContext.setReturnMessage(message);
			}
		}catch(Exception e){
			LOGGER.error(".init() is error: ", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTypeService#query(cafe.core.context.SessionContext)
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException {
		try{
			AssetTypeFormDTO formDTO = null;
			List<AssetTypeDTO> results = null;
			Message message = null;
			if(sessionContext != null){
				formDTO = (AssetTypeFormDTO) sessionContext.getRequestParameter();
				if(formDTO != null){
					//查詢條件 -- 設備類別
					String queryAssetCategoryCode = formDTO.getQueryAssetCategoryCode();
					LOGGER.error(".query() --> queryAssetCategoryCode: ", "service Exception", queryAssetCategoryCode);
					//查詢條件 -- 排序欄位
					String sort = formDTO.getSort();
					LOGGER.error(".query() --> sort: ", "service Exception", sort);
					//查詢條件 -- 排序方式
					String order = formDTO.getOrder();
					LOGGER.error(".query() --> order: ", "service Exception", order);
					//復核條件的結果數
					Integer rowCount = this.assetTypeDAO.count(queryAssetCategoryCode);
					LOGGER.error(".query() --> rowCount: ", "service Exception", rowCount.longValue());
					//若結果數大於0,則查詢具體資料
					if(rowCount.intValue() > 0){
						formDTO.getPageNavigation().setRowCount(rowCount.intValue());
						results = this.assetTypeDAO.listBy(queryAssetCategoryCode, null, sort, order);
						//results = this.combineData(results);
						//rowCount = new Integer(results.size());
						formDTO.getPageNavigation().setRowCount(results.size());
						//當前頁碼
						int currentPage = formDTO.getPageNavigation().getCurrentPage();
						LOGGER.error(".query() --> currentPage: ", "service Exception", currentPage);
						//每頁顯示的數目
						int pageSize = formDTO.getPageNavigation().getPageSize();
						LOGGER.error(".query() --> pageSize: ", "service Exception", pageSize);
						//用sublist分頁,構建起始/終止位置
						int fromIndex = currentPage * pageSize;
						int toIndex = (currentPage + 1) * pageSize;
						//分頁
						results = results.subList(fromIndex, (toIndex > rowCount.intValue())?(rowCount.intValue()):(toIndex));
					}else{
						message = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
					}
					formDTO.setList(results);
				}
				sessionContext.setResponseResult(formDTO);
				sessionContext.setReturnMessage(message);
			}
		}catch(Exception e){
			LOGGER.error(".query() is error: ", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTypeService#initEdit(cafe.core.context.SessionContext)
	 */
	public SessionContext initEdit(SessionContext sessionContext) throws ServiceException {
		try{
			AssetTypeFormDTO formDTO = null;
			AssetTypeDTO assetTypeDTO = null;
			if(sessionContext != null){
				formDTO = (AssetTypeFormDTO) sessionContext.getRequestParameter();
				if(formDTO != null){
					//編輯條件--設備編號
					//String editAssetTypeId = formDTO.getEditAssetTypeId();
					String assetTypeId = formDTO.getEditAssetTypeId();
					LOGGER.debug(".initEdit() --> assetTypeId : ", assetTypeId);
					if(StringUtils.hasText(assetTypeId)){
						//查詢滿足條件的結果集
						List<AssetTypeDTO> list = this.assetTypeDAO.listBy(null, assetTypeId, null, null);
						//整合結果集,將companyId,commModeId,functionId合並
						//list = this.combineData(list);
						//取出對象
						if(!CollectionUtils.isEmpty(list)){
							assetTypeDTO = list.get(0);
							String commModeId = assetTypeDTO.getCommModeId();
							//把查詢出來的多選通訊模式（用，隔開的形式）進行遍歷到list
							if(StringUtils.hasText(commModeId)){
								String [] commModeIdArray = commModeId.split(",");
								Set<String> commModeIds = new TreeSet<String>();
								for (int i=0; i < commModeIdArray.length; i++) {
									commModeIds.add(commModeIdArray[i]);
								}
								assetTypeDTO.setCommModeIds(commModeIds);
							}
							//把查詢出來的多選設備廠商（用，隔開的形式）進行遍歷到list
							String companyId = assetTypeDTO.getCompanyId();
							if(StringUtils.hasText(companyId)){
								String [] companyIdArray = companyId.split(",");
								Set<String> companyIds = new TreeSet<String>();
								for (int i=0; i < companyIdArray.length; i++) {
									companyIds.add(companyIdArray[i]);
								}
								assetTypeDTO.setCompanyIds(companyIds);
							}
							//把查詢出來的多選支援功能（用，隔開的形式）進行遍歷到list
							String functionId = assetTypeDTO.getFunctionId();
							if(StringUtils.hasText(functionId)){
								String [] functionIdArray = functionId.split(",");
								Set<String> functionIds = new TreeSet<String>();
								for (int i=0; i < functionIdArray.length; i++) {
									functionIds.add(functionIdArray[i]);
								}
								assetTypeDTO.setFunctionIds(functionIds);
							}
						}
					}
					formDTO.setAssetTypeDTO(assetTypeDTO);
				}
				sessionContext.setResponseResult(formDTO);
			}
		}catch(Exception e){
			LOGGER.error(".initEdit() is error: ", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTypeService#getCompanyParameterList(cafe.core.context.SessionContext)
	 */
	public SessionContext getCompanyParameterList(SessionContext sessionContext) throws ServiceException {
		try{
			List<Parameter> results = null;
			MultiParameterInquiryContext param = null;
			if(sessionContext != null){
				//查詢條件
				param = (MultiParameterInquiryContext) sessionContext.getRequestParameter();
				String companyId = IAtomsConstants.MARK_EMPTY_STRING;
				String companyCode = IAtomsConstants.MARK_EMPTY_STRING;
				String companyType = IAtomsConstants.MARK_EMPTY_STRING;
				String shortName = IAtomsConstants.MARK_EMPTY_STRING;
				if(param != null){
					//廠商編號
					companyId = (String) param.getParameter(CompanyDTO.ATTRIBUTE.COMPANY_ID.getValue());
					LOGGER.debug(".getCompanyParameterList() --> companyId: ", companyId);
					//廠商代碼
					companyCode = (String) param.getParameter(CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue());
					LOGGER.debug(".getCompanyParameterList() --> companyType: ", companyType);
					//廠商類型
					companyType = (String) param.getParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue());
					LOGGER.debug(".getCompanyParameterList() --> companyCode: ", companyCode);	
					//廠商簡稱
					shortName = (String) param.getParameter(CompanyDTO.ATTRIBUTE.SHORT_NAME.getValue());
					LOGGER.debug(".getCompanyParameterList() --> shortName: ", shortName);
				}
				//根據條件查詢
				results = this.assetTypeDAO.getCompanyParameterList(companyId, companyType, companyCode, shortName);
				sessionContext.setResponseResult(results);
			}
		}catch(Exception e){
			LOGGER.error(".getCompanyParameterList() is error: ", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTypeService#save(cafe.core.context.SessionContext)
	 */
	public SessionContext save(SessionContext sessionContext) throws ServiceException {
		String messageCode = IAtomsMessageCode.UPDATE_SUCCESS;
		try{
			Message message = null;
			AssetTypeFormDTO formDTO = null;
			IAtomsLogonUser logonUser = null;
			AssetTypeDTO sourceDTO = null;
			boolean isRepeat = false;
			if(sessionContext != null){
				formDTO = (AssetTypeFormDTO) sessionContext.getRequestParameter();
				if(formDTO != null){
					sourceDTO = formDTO.getAssetTypeDTO();
					logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
					if(sourceDTO != null){
						//獲取當前時間
						Timestamp currentTime = DateTimeUtils.getCurrentTimestamp();
						//獲取存儲編號
						String assetTypeId = sourceDTO.getAssetTypeId();
						String name = sourceDTO.getName();
						LOGGER.debug(".save() -- > assetTypeId: ", assetTypeId);
						//欲存儲的支援功能編號集合
						Set<String> functionIds = sourceDTO.getFunctionIds();
						//欲存儲的廠商編號集合
						Set<String> companyIds = sourceDTO.getCompanyIds();
						//欲存儲的通訊模式編號集合
						Set<String> commModeIds = sourceDTO.getCommModeIds();
						//根據設備編號在數據庫查詢,若數據存在,則爲修改,若不存在則爲新增
						DmmAssetType assetType = this.assetTypeDAO.findByPrimaryKey(DmmAssetType.class, assetTypeId);
						//新增時,設置新增人員信息,預設刪除標記爲false
						if(assetType == null){
							//對設備名稱進行判重（包括已刪除）
							isRepeat = this.assetTypeDAO.isCheck(null, name, sourceDTO.getAssetCategory());
							if (isRepeat) {
								message = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.ASSET_NAME_REPEAT);
								sessionContext.setReturnMessage(message);
								return sessionContext;
							}
							assetTypeId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_ASSET_TYPE);
							assetType = new DmmAssetType();
							assetType.setAssetTypeId(assetTypeId);
							assetType.setCreatedDate(currentTime);
							assetType.setCreatedById(logonUser.getId());
							assetType.setCreatedByName(logonUser.getName());
							assetType.setUpdatedDate(currentTime);
							assetType.setUpdatedById(logonUser.getId());
							assetType.setUpdatedByName(logonUser.getName());
							assetType.setDeleted(IAtomsConstants.NO);
							messageCode = IAtomsMessageCode.INSERT_SUCCESS;
						}else{
							isRepeat  = this.assetTypeDAO.isCheck(assetTypeId, name, sourceDTO.getAssetCategory());
							//對設備名稱進行判重（包括已刪除）
							if (isRepeat) {
								message = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.ASSET_NAME_REPEAT);
								sessionContext.setReturnMessage(message);
								return sessionContext;
							}
						}
						//將頁面欄位傳入存儲對象
						assetType.setName(sourceDTO.getName());
						assetType.setAssetCategory(sourceDTO.getAssetCategory());
						assetType.setBrand(sourceDTO.getBrand());
						assetType.setRemark(sourceDTO.getRemark());
						assetType.setModel(sourceDTO.getModel());
						assetType.setSafetyStock(sourceDTO.getSafetyStock());
						assetType.setUnit(sourceDTO.getUnit());
						assetType.setUpdatedDate(currentTime);
						assetType.setUpdatedById(logonUser.getId());
						assetType.setUpdatedByName(logonUser.getName());
						//存儲主表
						this.assetTypeDAO.getDaoSupport().saveOrUpdate(assetType);
						//存儲ASSET_TYPE_COMPANY表數據
						DmmAssetTypeCompany assetTypeCompany = null;
						DmmAssetTypeCompanyId assetTypeCompanyId = null;
						List<AssetTypeCompanyDTO> assetTypeCompanyDTOList = this.assetTypeDAO.getAssetTypeCompanyDTOList(assetTypeId, null);
						if(!CollectionUtils.isEmpty(assetTypeCompanyDTOList)){
							for(AssetTypeCompanyDTO assetTypeCompanyDTO : assetTypeCompanyDTOList){
								assetTypeCompanyId = new DmmAssetTypeCompanyId(assetTypeId, assetTypeCompanyDTO.getCompanyId());
								this.assetTypeDAO.getDaoSupport().delete(assetTypeCompanyId, DmmAssetTypeCompany.class);
							}
							this.assetTypeDAO.getDaoSupport().flush();
						}
						if(companyIds != null && companyIds.size() > 0){
							for(String companyId : companyIds){
								assetTypeCompany = new DmmAssetTypeCompany();
								assetTypeCompanyId = new DmmAssetTypeCompanyId(assetTypeId, companyId);
								assetTypeCompany.setId(assetTypeCompanyId);
								this.assetTypeDAO.getDaoSupport().saveOrUpdate(assetTypeCompany);
							}
						}
						//存儲ASSET_SUPPORTED_FUNCITON表數據
						DmmAssetSupportedFunction assetSupportedFunction = null;
						DmmAssetSupportedFunctionId assetSupportedFunctionId = null;
						List<AssetSupportedFunctionDTO> assetSupportedFunctionDTOList = this.assetTypeDAO.getAssetSupportedFunctionDTOList(assetTypeId, null);
						if(!CollectionUtils.isEmpty(assetSupportedFunctionDTOList)){
							for(AssetSupportedFunctionDTO assetSupportedFunctionDTO : assetSupportedFunctionDTOList){
								assetSupportedFunctionId = new DmmAssetSupportedFunctionId(assetSupportedFunctionDTO.getFunctionId(), assetTypeId);
								this.assetTypeDAO.getDaoSupport().delete(assetSupportedFunctionId, DmmAssetSupportedFunction.class);
							}
							this.assetTypeDAO.getDaoSupport().flush();
						}
						if(functionIds != null && functionIds.size() > 0){
							for(String functionId : functionIds){
								assetSupportedFunction = new DmmAssetSupportedFunction();
								assetSupportedFunctionId = new DmmAssetSupportedFunctionId(functionId, assetTypeId);
								assetSupportedFunction.setId(assetSupportedFunctionId);
								this.assetTypeDAO.getDaoSupport().saveOrUpdate(assetSupportedFunction);
							}
						}
						//存儲ASSET_SUPPORTED_COMM表數據
						DmmAssetSupportedComm assetSupportedComm = null;
						DmmAssetSupportedCommId assetSupportedCommId = null;
						List<AssetSupportedCommDTO> assetSupportedCommDTOList = this.assetTypeDAO.getAssetSupportedCommDTOList(assetTypeId, null);
						if(!CollectionUtils.isEmpty(assetSupportedCommDTOList)){
							for(AssetSupportedCommDTO assetSupportedCommDTO : assetSupportedCommDTOList){
								assetSupportedCommId = new DmmAssetSupportedCommId(assetTypeId, assetSupportedCommDTO.getCommModeId());
								this.assetTypeDAO.getDaoSupport().delete(assetSupportedCommId, DmmAssetSupportedComm.class);
							}
							this.assetTypeDAO.getDaoSupport().flush();
						}
						if(commModeIds != null && commModeIds.size() > 0){
							for(String commModeId : commModeIds){
								assetSupportedComm = new DmmAssetSupportedComm();
								assetSupportedCommId = new DmmAssetSupportedCommId(assetTypeId, commModeId);
								assetSupportedComm.setId(assetSupportedCommId);
								this.assetTypeDAO.getDaoSupport().saveOrUpdate(assetSupportedComm);
							}
						}
						//交互資料庫
						this.assetTypeDAO.getDaoSupport().flush();
						message = new Message(Message.STATUS.SUCCESS, messageCode, new String[]{this.getMyName()});
					}
				}
				sessionContext.setRequestParameter(formDTO);
				sessionContext.setReturnMessage(message);
			}
		}catch(Exception e){
			LOGGER.error(".save() is error: ", "service Exception", e);
			if(IAtomsMessageCode.INSERT_SUCCESS.equals(messageCode)){
				messageCode = IAtomsMessageCode.INSERT_FAILURE;
			}else{
				messageCode = IAtomsMessageCode.UPDATE_FAILURE;
			}
			throw new ServiceException(messageCode, new String[]{this.getMyName()});
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTypeService#delete(cafe.core.context.SessionContext)
	 */
	public SessionContext delete(SessionContext sessionContext) throws ServiceException {
		try{
			AssetTypeFormDTO formDTO = null;
			Message message = null;
			if(sessionContext != null){
				formDTO = (AssetTypeFormDTO) sessionContext.getRequestParameter();
				if(formDTO != null){
					//獲取欲刪除資料的主鍵值
					String editAssetTypeId = formDTO.getEditAssetTypeId();
					IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
					//根據主鍵獲取資料庫數據
					if(StringUtils.hasText(editAssetTypeId)){
						DmmAssetType assetType = this.assetTypeDAO.findByPrimaryKey(DmmAssetType.class, editAssetTypeId);
						if(assetType != null){
							//刪除數據--刪除標記設爲true,加入刪除人員信息
							assetType.setDeleted(IAtomsConstants.YES);
							assetType.setDeletedById(logonUser.getId());
							assetType.setDeletedByName(logonUser.getName());
							assetType.setDeletedDate(DateTimeUtils.getCurrentTimestamp());
							this.assetTypeDAO.getDaoSupport().update(assetType);
							this.assetTypeDAO.getDaoSupport().flush();
							message = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_SUCCESS, new String[]{this.getMyName()});
						} else {
							message = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()});
						}
					} else {
						message = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()});
					}
				}
				sessionContext.setReturnMessage(message);
				sessionContext.setResponseResult(formDTO);
			}
		}catch(Exception e){
			LOGGER.error(".delete() is error: ", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()});
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTypeService#getAssetTypeList(cafe.core.context.MultiParameterInquiryContext)
	 */
	public List<Parameter> getAssetTypeList(MultiParameterInquiryContext param) throws ServiceException {
		long startQueryAssetTime = System.currentTimeMillis();
		List<Parameter> assAssetTypes = null;
		try {
			String assetCategory = (String) param.getParameter(BimContractAssetDTO.ATTRIBUTE.ASSET_CATEGORY.getValue());
			String contractId = (String) param.getParameter(BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue());
			assAssetTypes = this.assetTypeDAO.getAssetNameList(null, null, assetCategory, contractId, IAtomsConstants.NO);
		} catch (DataAccessException e) {
			LOGGER.error(".getAssetTypeList() DataAccess Exception:", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".getAssetTypeList() Exception:", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		long endQueryAssetTime = System.currentTimeMillis();
		LOGGER.debug("calculate time --> load", "Service getAssetTypeList:" + (endQueryAssetTime - startQueryAssetTime));
		return assAssetTypes;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTypeService#getMerchantList(cafe.core.context.MultiParameterInquiryContext)
	 */
	public boolean isAssetList(MultiParameterInquiryContext param)
			throws ServiceException {
		boolean flag = false;
		try {
			String editAssetTypeId = (String) param.getParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			flag = this.assetTypeDAO.isAssetList(editAssetTypeId);
			return flag;
		} catch (DataAccessException e) {
			LOGGER.error(".getAssetList() is error : ", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		} catch (Exception e){
			LOGGER.error(".getAssetList() is error : ", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTypeService#getAssetModelList(cafe.core.context.MultiParameterInquiryContext)
	 */
	public List<Parameter> getAssetModelList(MultiParameterInquiryContext param) throws ServiceException {
		List<Parameter> assetModels = new ArrayList<Parameter>();
		Parameter parameter = null;
		String assetModel = null;
		List<AssetTypeDTO> assetModelList = null;
		try {
			String assetTypeId = (String) param.getParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			if (StringUtils.hasText(assetTypeId)) {
				//Task #3127
				if (assetTypeId.indexOf(IAtomsConstants.MARK_SEPARATOR) < 0) {
					assetModel = this.assetTypeDAO.getAssetInfo(assetTypeId, AssetTypeDTO.ATTRIBUTE.MODEL.getValue());
				} else {
					assetModelList = this.assetTypeDAO.getAssetModelList(assetTypeId);
				}
			}
			if (StringUtils.hasText(assetModel)) {
				String[] models = assetModel.split(IAtomsConstants.MARK_SEPARATOR);
				for (String model : models) {
					if (model.equals("")) {
						continue;
					}
					parameter = new Parameter(model, model);
					assetModels.add(parameter);
				}
			//Task #3127
			} else if (!CollectionUtils.isEmpty(assetModelList)) {
				String[] models = {};
				for (AssetTypeDTO assetTypeDTO : assetModelList) {
					if (StringUtils.hasText(assetTypeDTO.getModel())) {
						models = assetTypeDTO.getModel().split(IAtomsConstants.MARK_SEPARATOR);
						for (String model : models) {
							if (model.equals("")) {
								continue;
							}
							parameter = new Parameter(model, model);
							assetModels.add(parameter);
						}
					}
				}
			}
		}  catch (DataAccessException e) {
			LOGGER.error(".getAssetModelList() is error : ", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e){
			LOGGER.error(".getAssetModelList() is error : ", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return assetModels;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTypeService#getAssetBrandList(cafe.core.context.MultiParameterInquiryContext)
	 */
	public List<Parameter> getAssetBrandList(MultiParameterInquiryContext param) throws ServiceException {
		List<Parameter> assetBrands = new ArrayList<Parameter>();
		Parameter parameter = null;
		String brand = null;
		try {
			String assetTypeId = (String) param.getParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			if (StringUtils.hasText(assetTypeId)) {
				brand = this.assetTypeDAO.getAssetInfo(assetTypeId, AssetTypeDTO.ATTRIBUTE.BRAND.getValue());
			}
			if (StringUtils.hasText(brand)) {
				String[] brands = brand.split(IAtomsConstants.MARK_SEPARATOR);
				for (String model : brands) {
					if (model.equals("")) {
						continue;
					}
					parameter = new Parameter(model, model);
					assetBrands.add(parameter);
				}
			}
		}  catch (DataAccessException e) {
			LOGGER.error(".getAssetBrandList() is error : ", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e){
			LOGGER.error(".getAssetBrandList() is error : ", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return assetBrands;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTypeService#getBuiltInFeature(cafe.core.context.MultiParameterInquiryContext)
	 */
	public List<Parameter> getBuiltInFeature(MultiParameterInquiryContext param) throws ServiceException {
		List<Parameter> builtInFeatures = null;
		try {
			String edcType = (String) param.getParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			if (StringUtils.hasText(edcType)) {
				builtInFeatures = this.assetTypeDAO.listBuiltInFeatureByAssetTypeId(edcType);
			}
		}  catch (DataAccessException e) {
			LOGGER.error(".getBuiltInFeature() is error : ", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e){
			LOGGER.error(".getBuiltInFeature() is error : ", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return builtInFeatures;
	}	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTypeService#getConnectionTypeList(cafe.core.context.MultiParameterInquiryContext)
	 */
	public List<Parameter> getConnectionTypeList(MultiParameterInquiryContext param) throws ServiceException {
		List<Parameter> connectionTypes = null;
		try {
			String edcType = (String) param.getParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			if (StringUtils.hasText(edcType)) {
				connectionTypes = this.assetTypeDAO.listConnectionTypeByAssetTypeId(edcType);
			}
		}  catch (DataAccessException e) {
			LOGGER.error(".getBuiltInFeature() is error : ", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e){
			LOGGER.error(".getBuiltInFeature() is error : ", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return connectionTypes;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetTypeService#getAssetListForCase(cafe.core.context.MultiParameterInquiryContext)
	 */
	public List<Parameter> getAssetListForCase(MultiParameterInquiryContext param) throws ServiceException {
		List<Parameter> edcAssets = null;
		try {
			String customerId = (String) param.getParameter(AssetTypeDTO.ATTRIBUTE.COMPANY_ID.getValue());
			// Task #2496 設備類別
			String assetCategory = (String) param.getParameter(AssetTypeDTO.ATTRIBUTE.ASSET_CATEGORY.getValue());
			// 是否忽略刪除
			Boolean ignoreDeleted = null;
			Object ignoreDeletedObject = param.getParameter(IAtomsConstants.PARAM_IGNORE_DELETED);
			if (ignoreDeletedObject instanceof Boolean) {
				ignoreDeleted = (Boolean)ignoreDeletedObject;
			}
			if(ignoreDeleted == null){
				ignoreDeleted = Boolean.TRUE;
			}
			// Task #2945 結案后不查詢使用人
			/*if (StringUtils.hasText(customerId)) {
				edcAssets = this.assetTypeDAO.listAssetByCustomerId(customerId, assetCategory, ignoreDeleted);
			}*/
			edcAssets = this.assetTypeDAO.listAssetByCustomerId(customerId, assetCategory, ignoreDeleted);
		}  catch (DataAccessException e) {
			LOGGER.error(".getAssetListForCase() is error : ", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e){
			LOGGER.error(".getAssetListForCase() is error : ", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return edcAssets;
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
