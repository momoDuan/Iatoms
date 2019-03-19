package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetStockReportDTO;

/**
 * Purpose: 設備庫存表DAO interface
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016-7-29
 * @MaintenancePersonnel CrissZhang
 */
@SuppressWarnings("rawtypes")
public interface IAssetStockReportDAO extends IGenericBaseDAO {
	
	/**
	 * Purpose:匯出查詢
	 * @author CrissZhang
	 * @param formDTO:DeviceStockTableFormDTO
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return List<RepoStatusReportDTO>:查詢listDTO
	 */
	public List<AssetStockReportDTO> listBy(String queryTableName, String queryCustomerId, String queryMaintainMode,
			String queryMonth, String roleAttribute, String dataAcl,String userId) throws DataAccessException;
}
