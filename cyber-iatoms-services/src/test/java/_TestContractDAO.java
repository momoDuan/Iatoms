import java.util.List;

import junit.framework.Assert;

import cafe.core.dao.DataAccessException;

import com.cybersoft4u.xian.iatoms.common.bean.dto.WarehouseDTO;
import com.cybersoft4u.xian.iatoms.services.dao.impl.WarehouseDAO;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;

/**
 * Purpose: 倉庫DAO的單元測試
 * @author HaimingWang
 * @since  JDK 1.6
 * @date   2016/6/22
 * @MaintenancePersonnel HaimingWang
 */
public class _TestContractDAO extends AbstractTestCase {
	/**
	 * 注入warehouseDAO
	 */
	public WarehouseDAO warehouseDAO;

	/**
	 * 無參構造
	 */
	public _TestContractDAO() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}

	/**
	 * Purpose: 測試查詢方法
	 * @author HaimingWang
	 * @return void
	 */
	public void testListBy(){
		try {
		String queryVendorId = "1";
		String queryWarehouseName = "";
		Integer pageSize = 10;
		Integer pageIndex = 1;
		String sort = "";
		String orderby = "WAREHOUSE_ID";
		Boolean isPage = true;
		WarehouseDTO warehouseDTO = null;
		List<WarehouseDTO> list = warehouseDAO.listBy(queryVendorId, queryWarehouseName, pageSize, pageIndex, sort, orderby);
//		Assert.assertEquals(10, list.size());
		} catch (DataAccessException e) {
			System.out.println("_TestWarehouseDAO.testListBy() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose: 測試獲得記錄數的方法
	 * @author HaimingWang
	 * @return void
	 */
	public void testCount(){
		try {
		String queryVendorId = "1";
		String queryWarehouseName = "北";
		int count = warehouseDAO.count(queryVendorId, queryWarehouseName);
//		Assert.assertEquals(4, count);
		} catch (DataAccessException e) {
			System.out.println("_TestWarehouseDAO.testCount() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose: 測試檢核同一廠商下倉庫名稱是否重複的方法
	 * @author HaimingWang
	 * @return void
	 */
	public void testCheckRepeat(){
		try {
		String vendorId = "1";
		String name = "北一倉";
		String warehouseId = "163";
		Boolean repeatResult = warehouseDAO.isCheck(vendorId, name, warehouseId);
//		Assert.assertTrue(repeatResult);
		} catch (DataAccessException e) {
			System.out.println("_TestWarehouseDAO.testCheckRepeat() is error" + e);
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