package com.cybersoft4u.xian.iatoms.test.dao;

import java.util.List;

import junit.framework.Assert;

import org.springframework.util.CollectionUtils;

import cafe.core.dao.DataAccessException;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetStockReportDTO;
import com.cybersoft4u.xian.iatoms.services.dao.impl.AssetStockReportDAO;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;

public class _TestAssetStockReportDAO extends AbstractTestCase {

	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestAssetStockReportDAO.class);
	/**
	 * 注入設備庫存表DAO
	 */
	private AssetStockReportDAO assetStockReportDAO;
	
	/**
	 * Constructor: 無參構造函數
	 */
	@SuppressWarnings("deprecation")
	public _TestAssetStockReportDAO() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}

	/**
	 * Purpose: 測試查詢方法
	 * @author amandawang
	 * @return void
	 */
	public void testListBy() {
		try {
			String queryTableName = "DMM_REPOSITORY"; 
			String queryCustomerId = "1492743446432-0353"; 
			String queryMaintainMode = "BUYOUT";
			String queryMonth = "2017/05";
			String roleAttribute = "VENDOR";
			String dataAcl = "N";
			String userId = "1473125444068-0049";
			//獲取匯出的集合
			List<AssetStockReportDTO> list = this.assetStockReportDAO.listBy(queryTableName, queryCustomerId,
					queryMaintainMode, queryMonth, roleAttribute, dataAcl, userId);
			if(!CollectionUtils.isEmpty(list)) {
				Assert.assertNotNull(list);
			} else {
				Assert.assertTrue(list.size() == 0);
			} 
		}catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testListBy()", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the assetStockReportDAO
	 */
	public AssetStockReportDAO getAssetStockReportDAO() {
		return assetStockReportDAO;
	}

	/**
	 * @param assetStockReportDAO the assetStockReportDAO to set
	 */
	public void setAssetStockReportDAO(AssetStockReportDAO assetStockReportDAO) {
		this.assetStockReportDAO = assetStockReportDAO;
	}
	
}
