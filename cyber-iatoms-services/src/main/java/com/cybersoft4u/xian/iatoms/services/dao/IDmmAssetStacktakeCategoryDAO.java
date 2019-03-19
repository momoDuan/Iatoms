package com.cybersoft4u.xian.iatoms.services.dao;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetStocktakeCategory;

/**
 * 
 * Purpose: 設備盤點設備類別DAO
 * @author allenchen
 * @since  JDK 1.7
 * @date   2016-7-19
 * @MaintenancePersonnel allenchen
 */
public interface IDmmAssetStacktakeCategoryDAO extends IGenericBaseDAO<DmmAssetStocktakeCategory> {
	
	/**
	 * Purpose: 依據盤點批號刪除信息
	 * @author CarrieDuan
	 * @param stocktackId ：盤點批號
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return void
	 */
	public void deletedAssetStacktakeCategory(String stocktackId) throws DataAccessException;
}
