package com.cybersoft4u.xian.iatoms.services.dao.impl;


import cafe.core.bean.CoreMessageCode;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetStacktakeInfoDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeStatusDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetStocktakeStatus;

/**
 * 
 * Purpose: 設備盤點狀態DAO
 * @author allenchen
 * @since  JDK 1.7
 * @date   2016-7-19
 * @MaintenancePersonnel allenchen
 */
public class DmmAssetStacktakeStatusDAO extends GenericBaseDAO<DmmAssetStocktakeStatus> implements IDmmAssetStacktakeStatusDAO {

	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(DmmAssetStacktakeStatusDAO.class);

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeStatusDAO#deletedAssetStacktakeStatus(java.lang.String)
	 */
	public void deletedAssetStacktakeStatus(String stocktackId) throws DataAccessException {
		try {
			LOGGER.debug("deletedAssetStacktakeStatus()", "stocktackId:", stocktackId);
			String schema = this.getMySchema();
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("delete ").append(schema).append(".DMM_ASSET_STOCKTAKE_STATUS where STOCKTACK_ID = :stocktackId");
			sqlQueryBean.setParameter(DmmAssetStacktakeInfoDTO.ATTRIBUTE.STOCKTACK_ID.getValue(), stocktackId);
			LOGGER.debug("deletedAssetStacktakeStatus()", "SQL---------->", sqlQueryBean.toString());
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
		} catch (Exception e) {
			LOGGER.error("DmmAssetStacktakeStatusDAO", ".deletedAssetStacktakeStatus()　is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE);
		}
	}

	
}
