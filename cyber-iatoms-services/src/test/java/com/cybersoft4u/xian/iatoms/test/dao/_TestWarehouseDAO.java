package com.cybersoft4u.xian.iatoms.test.dao;


import java.util.List;

import junit.framework.Assert;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.bean.dto.WarehouseDTO;
import com.cybersoft4u.xian.iatoms.services.dao.impl.WarehouseDAO;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;

/**
 * Purpose: 倉庫DAO的單元測試
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2016/6/22
 * @MaintenancePersonnel ElvaHe
 */
public class _TestWarehouseDAO extends AbstractTestCase {
	
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestWarehouseDAO.class);
	
	/**
	 * 注入warehouseDAO
	 */
	public WarehouseDAO warehouseDAO;

	/**
	 * 無參構造
	 */
	public _TestWarehouseDAO() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}

	/**
	 * Purpose: 測試查詢方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testListBy(){
		try {
			//廠商編號
			String queryComanyId = "1470654243644-00111";
			//倉庫名稱
			String queryName = "";
			Integer pageSize = 10;
			Integer pageIndex = 1;
			//排序參考列
			String sort = "WAREHOUSE_ID";
			//排序方式
			String orderby = "";
			List<WarehouseDTO> list = warehouseDAO.listBy(queryComanyId, queryName, pageSize, pageIndex, sort, orderby);
			if(!CollectionUtils.isEmpty(list)){
				Assert.assertNotNull(list);
			} else {
				Assert.assertFalse(list == null);
			}
		} catch (DataAccessException e) {
			LOGGER.error("_TestWarehouseDAO.testListBy()", "is error ：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose: 測試獲得記錄數的方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testCount(){
		try {
			//倉庫編號
			String queryCompanyId = "1470654243644-0011";
			//倉庫名稱
			String queryName = "中3";
			int count = warehouseDAO.count(queryCompanyId, queryName);
			if(count != 0){
				Assert.assertEquals(count, count);
			} else {
				Assert.assertFalse(count != 0);
			}
		} catch (DataAccessException e) {
			LOGGER.error("_TestWarehouseDAO.testCount()", "is error ：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose: 測試保存時檢查同一廠商下倉庫名稱是否重複的方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testIsCheck(){
		try {
			//倉庫編號
			String warehouseId = "";
			//倉庫名稱
			String name = "北一倉";
			//廠商編號
			String companyId = "163";
			boolean result = this.warehouseDAO.isCheck(warehouseId, name, companyId);
			if(result){
				Assert.assertTrue(result);
			} else {
				Assert.assertFalse(result);
			}
		} catch (DataAccessException e) {
			LOGGER.error("_TestWarehouseDAO.testIsCheck()", "is error ：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose: 測試檢查倉庫內是否有設備的方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testIsCheckWarehouse(){
		try {
			//倉庫編號
			String warehouseId = "1470713143335-0002";
			boolean isCheckWarehouse = this.warehouseDAO.isCheckWarehouse(warehouseId);
			if(isCheckWarehouse){
				Assert.assertTrue(isCheckWarehouse);
			} else {
				Assert.assertFalse(isCheckWarehouse);
			}
		} catch (Exception e) {
			LOGGER.error("_TestWarehouseDAO.testIsCheckWarehouse()", "is error ：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose: 測試根据用户编号得到所有仓库据点信息的方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetWarehouseByUserId(){
		try {
			//用戶編號
			String userId = "1472213846821-0025";
			List<Parameter> warehourses = this.warehouseDAO.getWarehouseByUserId(userId);
			if(!CollectionUtils.isEmpty(warehourses)){
				Assert.assertNotNull(warehourses);
			} else {
				Assert.assertFalse(warehourses == null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestWarehouseDAO.testGetWarehouseByUserId()", "is error ：", e);
			e.printStackTrace();
		}
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
}
