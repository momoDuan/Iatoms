package com.cybersoft4u.xian.iatoms.test.dao;

import junit.framework.Assert;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetTransListDAO;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;
/**
 * Purpose:設備轉倉明細DAO
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2017/5/16
 * @MaintenancePersonnel ElvaHe
 */
public class _TestDmmAssetTransListDAO extends AbstractTestCase{

	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestDmmAssetTransListDAO.class);
	
	/**
	 * 設備轉倉明細DAO
	 */
	private IDmmAssetTransListDAO assetTransListDAO;
	
	/**
	 * 無參構造
	 */
	public _TestDmmAssetTransListDAO(){
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	
	/**
	 * Purpose:測試根據設備序號刪除設備入庫明細檔
	 * @author ElvaHe
	 * @return void
	 */
	public void testDeleteAssetTransListById(){
		try {
			//設備轉倉明細檔id
			String assetTransListId = "";
			//設備轉倉批號
			String assetTransId = "TN201610080002";
			this.assetTransListDAO.deleteAssetTransListById(assetTransListId, assetTransId);
			Assert.assertTrue(true);
		} catch (Exception e) {
			LOGGER.error("_TestDmmAssetTransListDAO.testDeleteAssetTransListById()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試檢查轉倉清單中是否有該設備序號
	 * @author ElvaHe
	 * @return void
	 */
	public void testIsCheckHasSerialNumber(){
		try {
			//設備序號
			String serialNumber = "sadfsadfasdf";
			Boolean result = this.assetTransListDAO.isCheckHasSerialNumber(serialNumber);
			if(result){
				Assert.assertTrue(result);
			} else {
				Assert.assertFalse(result);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmAssetTransListDAO.testIsCheckHasSerialNumber()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the assetTransListDAO
	 */
	public IDmmAssetTransListDAO getAssetTransListDAO() {
		return assetTransListDAO;
	}

	/**
	 * @param assetTransListDAO the assetTransListDAO to set
	 */
	public void setAssetTransListDAO(IDmmAssetTransListDAO assetTransListDAO) {
		this.assetTransListDAO = assetTransListDAO;
	}
	
}
