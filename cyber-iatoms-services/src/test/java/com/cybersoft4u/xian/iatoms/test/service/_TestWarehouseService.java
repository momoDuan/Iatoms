package com.cybersoft4u.xian.iatoms.test.service;
import java.util.List;

import junit.framework.Assert;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.Parameter;
import cafe.core.context.SessionContext;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.WarehouseDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.WarehouseFormDTO;
import com.cybersoft4u.xian.iatoms.services.IWarehouseService;
import com.cybersoft4u.xian.iatoms.services.dao.impl.AdmUserDAO;
import com.cybersoft4u.xian.iatoms.services.dao.impl.WarehouseDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmUser;
import com.cybersoft4u.xian.iatoms.services.dmo.BimWarehouse;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;

/**
 * Purpose: 倉庫Service的單元測試
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2016/6/22
 * @MaintenancePersonnel ElvaHe
 */
public class _TestWarehouseService extends AbstractTestCase {
	
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestWarehouseService.class);
	
	/**
	 * 注入warehouseService
	 */
	public IWarehouseService warehouseService;
	
	/**
	 * 注入warehouseDAO
	 */
	private WarehouseDAO warehouseDAO;
	/**
	 * admUserDAO
	 */
	private AdmUserDAO admUserDAO;
	
	/**
	 * 無參構造
	 */
	public _TestWarehouseService() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}

	/**
	 * Purpose:測試查詢方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testQuery(){
		try {
			SessionContext  ctx =  new SessionContext();
			WarehouseFormDTO formDTO = new WarehouseFormDTO();
			formDTO.setQueryCompanyId("1474440222588-0125");
			formDTO.setQueryName("庫打2");
			formDTO.setRows(10);
			formDTO.setPage(1);
			formDTO.setSort("");
			formDTO.setOrder("WAREHOUSE_ID");
			ctx.setRequestParameter(formDTO);
			warehouseService.query(ctx);
			//符合查詢條件的記錄數
			Integer totalSize = this.warehouseDAO.count(formDTO.getQueryCompanyId(), formDTO.getQueryName());
			//若記錄數不等於0則查詢符合查詢條件的記錄信息
			if (totalSize == 0) {
				Assert.assertEquals(0, totalSize.intValue());
			} else {
				//符合查詢條件的信息集合
				List<WarehouseDTO> warehouseDTOs = 
						this.warehouseDAO.listBy(formDTO.getQueryCompanyId(), formDTO.getQueryName(), formDTO.getRows(), formDTO.getPage(), formDTO.getSort(), formDTO.getOrder());
				if (!CollectionUtils.isEmpty(warehouseDTOs)) {
					Assert.assertNotNull(warehouseDTOs);
				} else {
					Assert.assertFalse(warehouseDTOs != null);
				}
			}
		} catch (ServiceException e) {
			LOGGER.error("_TestWarehouseService.testQuery() ", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試初始化修改的方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testInitEdit(){
		try {
			SessionContext  ctx =  new SessionContext();
			WarehouseFormDTO formDTO = new WarehouseFormDTO();
			formDTO.setWarehouseId("1470713143335-0002aasf");
			ctx.setRequestParameter(formDTO);
			warehouseService.initEdit(ctx);
			//根據主鍵查找該筆記錄的信息
			BimWarehouse warehouse = this.warehouseDAO.findByPrimaryKey(BimWarehouse.class, formDTO.getWarehouseId());
			if (warehouse == null) {
				Assert.assertFalse(warehouse != null);
			} else {
				Assert.assertNotNull(warehouse);
			}
		} catch (ServiceException e) {
			LOGGER.error("_TestWarehouseService.testInitEdit() ", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試保存信息的方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testSave(){
		try {
			SessionContext  ctx =  new SessionContext();
			WarehouseFormDTO formDTO = new WarehouseFormDTO();
			//formDTO.setWarehouseId("1470713143335-0002");
			WarehouseDTO warehouseDTO = new WarehouseDTO();
			BimWarehouse warehouse = new BimWarehouse();
			IAtomsLogonUser logonUser = new IAtomsLogonUser();
			logonUser.setId("1473076241770-0048");
			logonUser.setName("賀文華");
			formDTO.setLogonUser(logonUser);
			warehouseDTO.setCompanyId("1");
			warehouseDTO.setName("newWarehouse");
			warehouseDTO.setContact("小明");
			formDTO.setWarehouseDTO(warehouseDTO);
			ctx.setRequestParameter(formDTO);
			warehouseService.save(ctx);
			boolean repeat = false;
			if (StringUtils.hasText(formDTO.getWarehouseId())) {
				//修改操作
				warehouse = this.warehouseDAO.findByPrimaryKey(BimWarehouse.class, formDTO.getWarehouseId());
				if(warehouse != null){
					//保存時檢查同一廠商下倉庫名稱是否重複
					repeat = this.warehouseDAO.isCheck(formDTO.getWarehouseId(), warehouseDTO.getName(), warehouseDTO.getCompanyId());
					if (repeat) {
						Assert.assertFalse(repeat == true);
					} else {
						//不存在時進行修改操作
						warehouse.setName("單元測試 - 修改");
						warehouse.setContact("單元測試 - 修改");
						warehouse.setTel("單元測試 - 修改");
						warehouse.setFax("單元測試 - 修改");
						warehouse.setLocation("單元測試 - 修改");
						warehouse.setAddress("單元測試 - 修改");
						warehouse.setComment("單元測試 - 修改");
						warehouse.setUpdatedById(logonUser.getId());
						warehouse.setUpdatedByName(logonUser.getName());
						warehouse.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
						warehouse.setDeleted(IAtomsConstants.PARAM_NO);
						this.warehouseDAO.getDaoSupport().update(warehouse);
						this.warehouseDAO.getDaoSupport().flush();
						Assert.assertTrue(IAtomsMessageCode.UPDATE_SUCCESS.equals(ctx.getReturnMessage().getCode()));
					}
				} else {
					Assert.assertTrue(IAtomsMessageCode.UPDATE_FAILURE.equals(ctx.getReturnMessage().getCode()));
				}
			} else {
				//保存操作
				//保存時檢查同一廠商下倉庫名稱是否重複
				repeat = this.warehouseDAO.isCheck(formDTO.getWarehouseId(), warehouseDTO.getName(), warehouseDTO.getCompanyId());
				if(repeat){
					System.out.println("1212");
					Assert.assertFalse(repeat == true);
				} else {
					//不存在時進行存儲操作
					warehouse.setWarehouseId("1234567890");
					warehouse.setCreatedById(logonUser.getId());
					warehouse.setCreatedByName(logonUser.getName());
					warehouse.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
					warehouse.setUpdatedById(logonUser.getId());
					warehouse.setUpdatedByName(logonUser.getName());
					warehouse.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
					warehouse.setDeleted(IAtomsConstants.PARAM_NO);
					this.warehouseDAO.getDaoSupport().save(warehouse);
					this.warehouseDAO.getDaoSupport().flush();
					Assert.assertTrue(IAtomsMessageCode.INSERT_SUCCESS.equals(ctx.getReturnMessage().getCode()));
				}
			}
		} catch (ServiceException e) {
			LOGGER.error("_TestWarehouseService.testSave() ", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試刪除的方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testDelete(){
		try {
			SessionContext  ctx =  new SessionContext();
			WarehouseFormDTO formDTO = new WarehouseFormDTO();
			formDTO.setWarehouseId("1474530435292-0151");
			IAtomsLogonUser logonUser = new IAtomsLogonUser();
			logonUser.setId("1473076241770-0048");
			logonUser.setName("賀文華");
			formDTO.setLogonUser(logonUser);
			ctx.setRequestParameter(formDTO);
			warehouseService.delete(ctx);
			boolean isHave = false;
			//存在要刪除的數據時
			if(StringUtils.hasText(formDTO.getWarehouseId())){
				//檢查倉庫內是否有設備
				isHave = this.warehouseDAO.isCheckWarehouse(formDTO.getWarehouseId());
				if (isHave) {
					Assert.assertTrue(IAtomsMessageCode.WARE_HAVE_ASSET.equals(ctx.getReturnMessage().getCode()));
				} else {
					//沒有設備時進行刪除操作
					BimWarehouse warehouse = this.warehouseDAO.findByPrimaryKey(BimWarehouse.class, formDTO.getWarehouseId());
					if (warehouse != null) {
						warehouse.setUpdatedById(logonUser.getId());
						warehouse.setUpdatedByName(logonUser.getName());
						warehouse.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
						warehouse.setDeleted(IAtomsConstants.PARAM_YES);
						this.warehouseDAO.getDaoSupport().update(warehouse);
						Assert.assertTrue(IAtomsMessageCode.DELETE_SUCCESS.equals(ctx.getReturnMessage().getCode()));
					} else {
						Assert.assertTrue(IAtomsMessageCode.DELETE_FAILURE.equals(ctx.getReturnMessage().getCode()));
					}
				}
			}
		} catch (ServiceException e) {
			LOGGER.error("_TestWarehouseService.testDelete() ", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試得到所有仓库据点下拉框
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetWarehouseList(){
		try {
			List<Parameter> warehouses = this.warehouseDAO.getWarehouseByUserId(null);
			if(!CollectionUtils.isEmpty(warehouses)){
				Assert.assertNotNull(warehouses);
			} else {
				Assert.assertFalse(warehouses == null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestWarehouseService.testGetWarehouseList() ", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試根据用户编号得到所有仓库据点信息
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetWarehouseByUserId(){
		try {
			//用戶編號
			String userId = "1472213846821-0025";
			if (StringUtils.hasText(userId)) {
				AdmUser admUser = (AdmUser) this.admUserDAO.findByPrimaryKey(AdmUser.class, userId);
				if(admUser != null){
					if (IAtomsConstants.NO.equals(admUser.getDataAcl())) {
						userId = "";
					}
				}
			}
			List<Parameter> warehouses = this.warehouseDAO.getWarehouseByUserId(userId);
			if(!CollectionUtils.isEmpty(warehouses)){
				Assert.assertNotNull(warehouses);
			} else {
				Assert.assertFalse(warehouses == null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestWarehouseService.testGetWarehouseByUserId() ", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the warehouseService
	 */
	public IWarehouseService getWarehouseService() {
		return warehouseService;
	}

	/**
	 * @param warehouseService the warehouseService to set
	 */
	public void setWarehouseService(IWarehouseService warehouseService) {
		this.warehouseService = warehouseService;
	}

	/**
	 * @return the warehouseDAO
	 */
	public WarehouseDAO getWarehouseDAO() {
		return warehouseDAO;
	}

	/**
	 * @param warehouseDAO the warehouseDAO to set
	 */
	public void setWarehouseDAO(WarehouseDAO warehouseDAO) {
		this.warehouseDAO = warehouseDAO;
	}

	/**
	 * @return the admUserDAO
	 */
	public AdmUserDAO getAdmUserDAO() {
		return admUserDAO;
	}

	/**
	 * @param admUserDAO the admUserDAO to set
	 */
	public void setAdmUserDAO(AdmUserDAO admUserDAO) {
		this.admUserDAO = admUserDAO;
	}
	
}
	
	
