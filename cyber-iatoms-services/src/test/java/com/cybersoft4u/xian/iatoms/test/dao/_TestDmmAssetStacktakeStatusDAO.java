package com.cybersoft4u.xian.iatoms.test.dao;

import junit.framework.Assert;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeStatusDAO;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;
/**
 * Purpose:設備盤點作業DAO的單元測試 
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2017/6/12
 * @MaintenancePersonnel ElvaHe
 */
public class _TestDmmAssetStacktakeStatusDAO extends AbstractTestCase{
	
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestDmmAssetStacktakeStatusDAO.class);
	
	/**
	 * 注入設備盤點作業DAO
	 */
	private IDmmAssetStacktakeStatusDAO assetStocktackStatusDAO;
	
	/**
	 * 無參構造
	 */
	public _TestDmmAssetStacktakeStatusDAO(){
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	
	/**
	 * Purpose:測試依據盤點批號刪除信息
	 * @author ElvaHe
	 * @return void
	 */
	public void testDeletedAssetStacktakeStatus(){
		try {
			//盤點批號
			String stocktackId = "IN201611030010";
			this.assetStocktackStatusDAO.deletedAssetStacktakeStatus(stocktackId);
			Assert.assertTrue(true);
		} catch (Exception e) {
			LOGGER.error("_TestDmmAssetStacktakeStatusDAO.testDeletedAssetStacktakeStatus()", "is error ", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the assetStocktackStatusDAO
	 */
	public IDmmAssetStacktakeStatusDAO getAssetStocktackStatusDAO() {
		return assetStocktackStatusDAO;
	}

	/**
	 * @param assetStocktackStatusDAO the assetStocktackStatusDAO to set
	 */
	public void setAssetStocktackStatusDAO(
			IDmmAssetStacktakeStatusDAO assetStocktackStatusDAO) {
		this.assetStocktackStatusDAO = assetStocktackStatusDAO;
	}
	
}
