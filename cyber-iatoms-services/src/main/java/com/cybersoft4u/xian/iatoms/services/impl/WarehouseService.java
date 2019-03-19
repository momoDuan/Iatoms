package com.cybersoft4u.xian.iatoms.services.impl;
import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cafe.core.bean.Message;
import cafe.core.bean.Parameter;
import cafe.core.bean.identity.LogonUser;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.SessionContext;
import cafe.core.dao.DataAccessException;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.convert.SimpleDtoDmoTransformer;
import cafe.core.util.convert.Transformer;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.WarehouseDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.WarehouseFormDTO;
import com.cybersoft4u.xian.iatoms.services.IWarehouseService;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IWarehouseDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmUser;
import com.cybersoft4u.xian.iatoms.services.dmo.BimWarehouse;

/**
 * Purpose: 倉庫service
 * @author ElvaHe
 * @since JDK 1.6
 * @date 2016年6月1日
 * @MaintenancePersonnel ElvaHe
 */
public class WarehouseService extends AtomicService implements IWarehouseService {
	/**
	 * 日誌記錄器
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, WarehouseService.class);

	/**
	 * 倉庫DAO
	 */
	private IWarehouseDAO warehouseDAO;
	/**
	 * admUserDAO
	 */
	private IAdmUserDAO admUserDAO;
	
	/**
	 * Constructor:無參構造
	 */
	public WarehouseService() {
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IWarehouseService#init(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext init(SessionContext sessionContext) throws ServiceException {
		try {
			WarehouseFormDTO warehouseManegeFormDTO = (WarehouseFormDTO) sessionContext.getRequestParameter();
			sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS));
			sessionContext.setResponseResult(warehouseManegeFormDTO);
		} catch (Exception e) {
			sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INIT_PAGE_FAILURE));
			LOGGER.error("init", "Error in init,error - ", e);
			throw new ServiceException(e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IWarehouseService#query(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext query(SessionContext sessionContext) throws ServiceException {
		try {
			Message msg = null;
			WarehouseFormDTO formDTO = (WarehouseFormDTO) sessionContext.getRequestParameter();
			String queryCompanyId = null;
			queryCompanyId = formDTO.getQueryCompanyId();
			String queryName = null;
			queryName = formDTO.getQueryName();
			Integer pageSize = formDTO.getRows();
			Integer pageIndex = formDTO.getPage();
			String sort = formDTO.getSort();
			String orderby = formDTO.getOrder();
			// 查詢記錄數
			Integer totalSize = null;
			//符合查詢條件的記錄數
			totalSize = this.warehouseDAO.count(queryCompanyId, queryName);
			if (totalSize.intValue() == 0) {
				// 查無數據，設置訊息
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
			} else {
				// 查詢符合條件的數據
				List<WarehouseDTO> warehouseDTOs = this.warehouseDAO.listBy(queryCompanyId, queryName, pageSize, pageIndex, sort, orderby);
				if (!CollectionUtils.isEmpty(warehouseDTOs)) {
					formDTO.setList(warehouseDTOs);
					formDTO.getPageNavigation().setRowCount(totalSize.intValue());
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
				} 
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(formDTO);
		} catch (DataAccessException e) {
			LOGGER.error("query()",  "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_BIM_WAREHOUSE)}, e);
		} catch (Exception e) {
			LOGGER.error("query()", "Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_BIM_WAREHOUSE)}, e);
		}
		return sessionContext;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IWarehouseService#initEdit(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext initEdit(SessionContext sessionContext) throws ServiceException {
		Transformer transformer = new SimpleDtoDmoTransformer();
		WarehouseDTO warehouseDTO = null;
		Message msg = null;
		try {
			WarehouseFormDTO warehouseFormDTO = (WarehouseFormDTO) sessionContext.getRequestParameter();
			String id = warehouseFormDTO.getWarehouseId();
			// 通過主鍵獲得DMO
			BimWarehouse warehouse = this.warehouseDAO.findByPrimaryKey(BimWarehouse.class, id);
			//若存在DMO
			if (warehouse == null ) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INIT_PAGE_FAILURE);
			} else {
				warehouseDTO = new WarehouseDTO();
				// DMO轉DTO
				warehouseDTO = (WarehouseDTO) transformer.transform(warehouse, warehouseDTO);
				warehouseFormDTO.setWarehouseDTO(warehouseDTO);
				msg = new Message(Message.STATUS.SUCCESS);
			}
			sessionContext.setResponseResult(warehouseFormDTO);
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOGGER.error("initEdit()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("initEdit(SessionContext ctx):", "error:", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		}
		return sessionContext;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IWarehouseService#save(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext save(SessionContext sessionContext) throws ServiceException {
		String warehouseId = null;
		Message msg = null;
		try {
			// 新增修改的保存
			WarehouseFormDTO formDTO = (WarehouseFormDTO) sessionContext.getRequestParameter();
			warehouseId = formDTO.getWarehouseId();
			WarehouseDTO warehouseDTO = formDTO.getWarehouseDTO();
			BimWarehouse warehouse = null;
			String companyId = null;
			String name = null;
			boolean repeat = false;
			//若存在DTO
			if (warehouseDTO != null){
				companyId = warehouseDTO.getCompanyId();
				name = warehouseDTO.getName();
			}
			LogonUser logonUser = formDTO.getLogonUser();
			warehouse = new BimWarehouse();
			if (StringUtils.hasText(warehouseId)) {
				//修改操作
				//根據倉庫編號獲取該倉庫的所有信息
				warehouse = this.warehouseDAO.findByPrimaryKey(BimWarehouse.class, warehouseId);
				//獲取要修改的廠商編號
				if(warehouse != null) {
					companyId = warehouse.getCompanyId();
					//倉庫名稱
					if(warehouseDTO != null){
						name = warehouseDTO.getName();
						//判斷倉庫名稱是否已存在
						repeat = this.warehouseDAO.isCheck(warehouseId, name, companyId);
						if (repeat) {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.WAREHOUSE_NAME_REPEAT);
						} else{
							warehouse.setName(name);
							warehouse.setContact(formDTO.getWarehouseDTO().getContact());
							warehouse.setTel(formDTO.getWarehouseDTO().getTel());
							warehouse.setFax(formDTO.getWarehouseDTO().getFax());
							warehouse.setLocation(formDTO.getWarehouseDTO().getLocation());
							warehouse.setAddress(formDTO.getWarehouseDTO().getAddress());
							warehouse.setComment(formDTO.getWarehouseDTO().getComment());
							warehouse.setUpdatedById(logonUser.getId());
							warehouse.setUpdatedByName(logonUser.getName());
							warehouse.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
							warehouse.setDeleted(IAtomsConstants.PARAM_NO);
							this.warehouseDAO.getDaoSupport().update(warehouse);
							this.warehouseDAO.getDaoSupport().flush();
							// 修改成功的訊息
							msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.UPDATE_SUCCESS, new String[]{this.getMyName()});
						}
					}
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.UPDATE_FAILURE, new String[]{this.getMyName()});
				}
			} else {
				//新增操作
				repeat = this.warehouseDAO.isCheck(warehouseId, name, companyId);
				Transformer transformer = new SimpleDtoDmoTransformer();
				// 將DTO轉成DMO
				warehouse = (BimWarehouse) transformer.transform(warehouseDTO, warehouse);
				warehouseId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BIM_WAREHOUSE);
				if (repeat) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.WAREHOUSE_NAME_REPEAT);
				} else{
					warehouse.setWarehouseId(warehouseId);
					warehouse.setCreatedById(logonUser.getId());
					warehouse.setCreatedByName(logonUser.getName());
					warehouse.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
					warehouse.setUpdatedById(logonUser.getId());
					warehouse.setUpdatedByName(logonUser.getName());
					warehouse.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
					warehouse.setDeleted(IAtomsConstants.PARAM_NO);
					this.warehouseDAO.getDaoSupport().save(warehouse);
					this.warehouseDAO.getDaoSupport().flush();
					// 新增成功的訊息
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INSERT_SUCCESS, new String[]{this.getMyName()});
				}
			}
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOGGER.error("save()", "DataAccess Exception:", e);
			if (!StringUtils.hasText(warehouseId)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INSERT_FAILURE, new String[]{this.getMyName()});
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()});
			}
			throw new ServiceException(msg);
		} catch (Exception e) {
			LOGGER.error("save()", "SessionContext ctx error :", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IWarehouseService#delete(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext delete(SessionContext sessionContext) throws ServiceException {
		boolean isHave = false;
		Message msg = null;
		BimWarehouse warehouse = null;
		try {
			// 通過倉庫編號刪除倉庫
			WarehouseFormDTO warehouseFormDTO = (WarehouseFormDTO) sessionContext.getRequestParameter();
			String warehouseId = warehouseFormDTO.getWarehouseId();
			String userId = warehouseFormDTO.getLogonUser().getId();
			String userName = warehouseFormDTO.getLogonUser().getName();
			if (StringUtils.hasText(warehouseId)) {
				isHave = this.warehouseDAO.isCheckWarehouse(warehouseId);
				if (isHave) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.WARE_HAVE_ASSET);
				} else {
					warehouse = this.warehouseDAO.findByPrimaryKey(BimWarehouse.class, warehouseId);
					//將DMO的刪除標誌設置為true
					if (warehouse != null) {
						warehouse.setUpdatedById(userId);
						warehouse.setUpdatedByName(userName);
						warehouse.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
						warehouse.setDeleted(IAtomsConstants.PARAM_YES);
						this.warehouseDAO.getDaoSupport().update(warehouse);
						msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_SUCCESS, new String[]{this.getMyName()});
					} else {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()});
					}
				}
			}
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOGGER.error("delete()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("delete()", "error:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IWarehouseService#getWarehouseList()
	 */
	@Override
	public List<Parameter> getWarehouseList() throws ServiceException {
		try {
			return this.getWarehouseByUserId(null);
		} catch (DataAccessException e) {
			LOGGER.error("getWarehouseList()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("getWarehouseList()", "Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IWarehouseService#getWarehouseByUserId(java.lang.String)
	 */
	@Override
	public List<Parameter> getWarehouseByUserId(String userId) throws ServiceException {
		try {
			long startQueryWarehouseTime = System.currentTimeMillis();
			if (StringUtils.hasText(userId)) {
				LOGGER.debug("getWarehouseByUserId()", "parameter:userId=", userId);
				AdmUser admUser = (AdmUser) this.admUserDAO.findByPrimaryKey(AdmUser.class, userId);
				LOGGER.debug("getWarehouseByUserId()", "parameter:admUser=" + admUser);
				if(admUser != null){
					if (IAtomsConstants.NO.equals(admUser.getDataAcl())) {
						userId = "";
					}
				}
			}
			List<Parameter> result = this.warehouseDAO.getWarehouseByUserId(userId);
			long endQueryWarehouseTime = System.currentTimeMillis();
			LOGGER.debug("calculate time --> load", "Service getWarehouseByUserId:" + (endQueryWarehouseTime - startQueryWarehouseTime));
			return result;
		} catch (DataAccessException e) {
			LOGGER.error("getWarehouseByUserId()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("getWarehouseByUserId()",  "Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
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
