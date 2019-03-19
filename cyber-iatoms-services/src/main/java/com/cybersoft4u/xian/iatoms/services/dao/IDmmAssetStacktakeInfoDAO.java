package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetStacktakeListDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetStocktakeInfo;

/**
 * 
 * Purpose: 設備盤點DAO interface
 * @author echomou
 * @since  JDK 1.7
 * @date   2016-7-19
 * @MaintenancePersonnel echomou
 */
@SuppressWarnings("rawtypes")
public interface IDmmAssetStacktakeInfoDAO extends IGenericBaseDAO<DmmAssetStocktakeInfo> {
	
	/**
	 * Purpose:獲取設備偏殿批號下拉列表
	 * @author echomou
	 * @param isComplate 是否完成驗收
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return List<Parameter>:設備盤點集合
	 */
	public List<Parameter> getAssetStocktakeIdList(boolean isComplate) throws DataAccessException;
	
	/**
	 * 
	 * Purpose: 根據倉庫ID、設備ID獲取歷史設備盤點序號下拉選單
	 * @author CarrieDuan
	 * @param warHouseId ：倉庫ID
	 * @param assetTypeId ：設備ID
	 * @throws DataAccessException :出錯時拋出DataAccessException
	 * @return List<Parameter>：設備盤點集合
	 */
	public List<Parameter> getAssetStocktackIdHistList (String warHouseId, String assetTypeId) throws DataAccessException;
	
	/**
	 * Purpose:根據盤點批號抓取此批號下所有設備的明細
	 * @author echomou
	 * @param stockId:盤點批號ID
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return List<AssetStocktackInfoDTO>:盤點批號抓取此批號下所有設備的明細集合
	 */
	public List<DmmAssetStacktakeListDTO> getAssetStocktackInfoDTOList(String stockId, boolean isComplete)throws DataAccessException;
	/**
	 * Purpose:根據盤點批號抓取此批號下所有設備的明細
	 * @author echomou
	 * @param stockId:盤點批號ID
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return List<AssetStocktackInfoDTO>:盤點批號抓取此批號下所有設備的明細集合
	 */
	public DmmAssetStacktakeListDTO getAssetStocktackInfoDTO(String stocktackId)throws DataAccessException;
	/**
	 * Purpose: 檢核設備是否存在於該倉庫
	 * @author echomou
	 * @param warHouseId 倉庫Id
	 * @param assetType 設備名稱
	 * @param assetStatu 設備狀態
	 * @throws DataAccessException :出錯時拋出DataAccessException
	 * @return Boolean 是否存在
	 */
	public boolean isExistCheck(String warHouseId, String assetType, String assetStatu)throws DataAccessException;
	
}
