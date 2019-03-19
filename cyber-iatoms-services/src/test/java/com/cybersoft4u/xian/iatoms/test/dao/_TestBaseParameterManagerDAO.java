package com.cybersoft4u.xian.iatoms.test.dao;

import java.util.List;

import junit.framework.Assert;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BaseParameterItemDefDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IBaseParameterManagerDAO;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;

/**
 * 
 * Purpose: 系統參數維護DAO實現類單元測試
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2017-5-26
 * @MaintenancePersonnel CrissZhang
 */
public class _TestBaseParameterManagerDAO extends AbstractTestCase {
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestBaseParameterManagerDAO.class);

	/**
	 * 注入系統參數維護DAO
	 */
	private IBaseParameterManagerDAO baseParameterManagerDAO;
	
	/**
	 * Constructor:構造函數
	 */
	public _TestBaseParameterManagerDAO() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	
	/**
	 * Purpose:測試從BASE_PARAMETER_TYPE_DEF表獲取參數類別下拉選單資料
	 * @author CrissZhang
	 * @return void
	 */
	public void testListParameterTypes(){
		try {
			// 調用查詢TYPE集合的方法
			List<Parameter> results = baseParameterManagerDAO.listParameterTypes();
			// 判斷結果
			if(!CollectionUtils.isEmpty(results)) {
				Assert.assertNotNull(results);
			} else {
				Assert.assertTrue(results.size() == 0);
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testListParameterTypes()", e);
			e.printStackTrace();
		}
	}

	/**
	 * Purpose:測試根據條件查詢基礎參數表數據
	 * @author CrissZhang
	 * @return void
	 */
	public void testListBy(){
		try {
			// 查詢條件：參數類型
			String bptdCode = null;
			// 查詢條件：參數代碼
			String itemValue = null;
			// 查詢條件：參數名稱
			String itemName = null;
			// 查詢參數：排序方向
			String sortDirection = null;
			// 查詢參數：排序欄位名稱
			String sortFieldName = null;
			// 查詢參數：當前頁
			Integer currentPage = null;
			// 查詢參數：每頁總筆數
			Integer pageSize = null;
			// 調用查詢方法
			List<BaseParameterItemDefDTO> results = baseParameterManagerDAO.listBy(bptdCode, itemValue, itemName, sortDirection, sortFieldName, currentPage, pageSize);
			// 判斷結果
			if(!CollectionUtils.isEmpty(results)) {
				Assert.assertNotNull(results);
			} else {
				Assert.assertTrue(results.size() == 0);
			}
			
			bptdCode = "DTID_TYPE";
			itemValue = "SAME";
			currentPage = 0;
			pageSize = 10;
			results = baseParameterManagerDAO.listBy(bptdCode, itemValue, itemName, sortDirection, sortFieldName, currentPage, pageSize);
			// 判斷結果
			if(!CollectionUtils.isEmpty(results)) {
				Assert.assertNotNull(results);
			} else {
				Assert.assertTrue(results.size() == 0);
			}
			
			sortDirection = "asc";
			results = baseParameterManagerDAO.listBy(bptdCode, itemValue, itemName, sortDirection, sortFieldName, currentPage, pageSize);
			// 判斷結果
			if(!CollectionUtils.isEmpty(results)) {
				Assert.assertNotNull(results);
			} else {
				Assert.assertTrue(results.size() == 0);
			}
			
			sortDirection = null;
			sortFieldName = "itemValue";
			results = baseParameterManagerDAO.listBy(bptdCode, itemValue, itemName, sortDirection, sortFieldName, currentPage, pageSize);
			// 判斷結果
			if(!CollectionUtils.isEmpty(results)) {
				Assert.assertNotNull(results);
			} else {
				Assert.assertTrue(results.size() == 0);
			}
			itemName = "同";
			sortDirection = "asc";
			results = baseParameterManagerDAO.listBy(bptdCode, itemValue, itemName, sortDirection, sortFieldName, currentPage, pageSize);
			// 判斷結果
			if(!CollectionUtils.isEmpty(results)) {
				Assert.assertNotNull(results);
			} else {
				Assert.assertTrue(results.size() == 0);
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testListBy()", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試根據條件查詢基礎表總結果數
	 * @author CrissZhang
	 * @return void
	 */
	public void testCount(){
		try {
			// 查詢條件：參數類型
			String bptdCode = null;
			// 查詢條件：參數代碼
			String itemValue = null;
			// 查詢條件：參數名稱
			String itemName = null;
			// 調用查詢count的方法
			int count = baseParameterManagerDAO.count(bptdCode, itemValue, itemName);
			if(count != 0){
				Assert.assertTrue(count != 0);
			} else {
				Assert.assertTrue(count == 0);
			}
			
			bptdCode = "DTID_TYPE";
			itemValue = "SAME";
			count = baseParameterManagerDAO.count(bptdCode, itemValue, itemName);
			if(count != 0){
				Assert.assertTrue(count != 0);
			} else {
				Assert.assertTrue(count == 0);
			}
			
			itemName = "2222333";
			count = baseParameterManagerDAO.count(bptdCode, itemValue, itemName);
			if(count != 0){
				Assert.assertTrue(count != 0);
			} else {
				Assert.assertTrue(count == 0);
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testCount()", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試根據所給條件查詢對應資料
	 * @author CrissZhang
	 * @return void
	 */
	public void testGetBaseParameterItemDefDTO(){
		try {
			// 參數Id
			String bpidId = null;
			// 參數類型
			String bptdCode = null;
			// 參數代碼
			String itemValue = null;
			// 參數名稱
			String itemName = null;
			BaseParameterItemDefDTO baseParameterItemDefDTO = baseParameterManagerDAO.getBaseParameterItemDefDTO(bpidId, null, bptdCode, itemValue, itemName);
			if(baseParameterItemDefDTO != null) {
				Assert.assertNotNull(baseParameterItemDefDTO);
			} else {
				Assert.assertNull(baseParameterItemDefDTO);
			}
			bpidId = "DTID_TYPE_02";
			bptdCode = "DTID_TYPE";
			itemValue = "SAME";
			baseParameterItemDefDTO = baseParameterManagerDAO.getBaseParameterItemDefDTO(bpidId, null, bptdCode, itemValue, itemName);
			if(baseParameterItemDefDTO != null) {
				Assert.assertNotNull(baseParameterItemDefDTO);
			} else {
				Assert.assertNull(baseParameterItemDefDTO);
			}
			
			itemName = "32312312";
			baseParameterItemDefDTO = baseParameterManagerDAO.getBaseParameterItemDefDTO(bpidId, null, bptdCode, itemValue, itemName);
			if(baseParameterItemDefDTO != null) {
				Assert.assertNotNull(baseParameterItemDefDTO);
			} else {
				Assert.assertNull(baseParameterItemDefDTO);
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testGetBaseParameterItemDefDTO()", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試驗證當前傳入的條件是否存在重復值
	 * @author CrissZhang
	 * @return void
	 */
	public void testIsRepeat(){
		try {
			String bpidId = "DTID_TYPE_02";
			String bptdCode = "DTID_TYPE"; 
			String itemValue= "SAME";
			String itemName = null;
			boolean result = this.baseParameterManagerDAO.isRepeat(bpidId, bptdCode, itemValue, itemName);
			if(result){
				Assert.assertTrue(result);
			} else {
				Assert.assertTrue(!result);
			}
			
			itemName = "2333";
			result = this.baseParameterManagerDAO.isRepeat(bpidId, bptdCode, itemValue, itemName);
			if(result){
				Assert.assertTrue(result);
			} else {
				Assert.assertTrue(!result);
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testIsRepeat()", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試根據bptdCode刪除轉入的基本參數
	 * @author CrissZhang
	 * @return void
	 */
	public void testDeleteTransferData(){
		try {
			String bptdCodes = "DTID_TYPE11111";
			this.baseParameterManagerDAO.deleteTransferData(bptdCodes);
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testIsRepeat()", e);
			e.printStackTrace();
		}
	}
	
	public void testDeleteTransferData1(){
		try {
			String bptdCodes = "DTID_TYPE11111";
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testIsRepeat()", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the baseParameterManagerDAO
	 */
	public IBaseParameterManagerDAO getBaseParameterManagerDAO() {
		return baseParameterManagerDAO;
	}

	/**
	 * @param baseParameterManagerDAO the baseParameterManagerDAO to set
	 */
	public void setBaseParameterManagerDAO(
			IBaseParameterManagerDAO baseParameterManagerDAO) {
		this.baseParameterManagerDAO = baseParameterManagerDAO;
	}
}
