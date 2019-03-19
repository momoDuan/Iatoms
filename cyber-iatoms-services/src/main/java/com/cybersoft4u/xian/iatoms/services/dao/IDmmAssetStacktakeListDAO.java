package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetStocktakeCategroyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetStocktakeStatusDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetStacktakeListDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetStacktakeFormDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetStocktakeList;

/**
 * 
 * Purpose: 設備盤點明細DAO interface
 * @author echomou
 * @since  JDK 1.7
 * @date   2016-7-19
 * @MaintenancePersonnel echomou
 */
public interface IDmmAssetStacktakeListDAO extends IGenericBaseDAO<DmmAssetStocktakeList> {
	
	/**
	 * Purpose:根據新增的設備批號獲取要盤點的設備集合
	 * @author echomou
	 * @param deviceInventoryDTO:新增的設備批號的詳細信息DTO
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return List<AssetStocktackListDTO>:盤點的設備集合
	 */
	public List<DmmAssetStacktakeListDTO> getListAssetStockDTOs(String stocktackId)throws DataAccessException;
	
	/**
	 * Purpose:根據設備盤點批獲取當前的設備列表總數
	 * @author echomou
	 * @param stocktackId:設備盤點批號
	 * @param isMore:是否統計盤盈
	 * @return Integer:設備總數
	 */
	public Integer getCount(String stocktackId, Boolean isMore)throws DataAccessException;
	
	/**
	 * Purpose:根據設備盤點批號查詢當前設備
	 * @author echomou
	 * @param stocktackId:設備盤點批號
	 * @param serialNumber : 設備序號
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return List<AssetStocktackListDTO>：設備集合
	 */
	public List<DmmAssetStacktakeListDTO> list(String stocktackId, String serialNumber, Integer pageSize, Integer pageIndex, String sort, String orderby) throws DataAccessException;
	/**
	 * Purpose:根據設備盤點批號查詢當前設備的盤點數量
	 * @author echomou
	 * @param stocktackId:設備盤點批號
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return List<AssetStocktackListDTO>：設備集合
	 */
	public List<DmmAssetStacktakeListDTO> listAssetInventory(String stocktackId, boolean isComplete, String sort, String order, Integer currentPage, Integer pageSize) throws DataAccessException;
	/**
	 * Purpose:根據設備盤點批獲取當前的設備列表總數
	 * @author echomou
	 * @param stocktackId:設備盤點批號
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return Integer:設備總數
	 */
	public Integer getListCount(String stocktackId, boolean isComplete)throws DataAccessException;
	/**
	 * Purpose:根據盤點批號查找設備種類
	 * @author echomou
	 * @param stocktackId:盤點批號ID
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return List<AssetStocktackListDTO>:設備種類集合
	 */
	public List<AssetStocktakeCategroyDTO> getCategoryByStocktackId(String stocktackId)throws DataAccessException;
	/**
	 * Purpose:根據盤點批號查找設備狀態
	 * @author echomou
	 * @param stocktackId:盤點批號ID
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return List<AssetStocktackListDTO>:設備狀態集合
	 */
	public List<AssetStocktakeStatusDTO> getStatusByStocktackId(String stocktackId)throws DataAccessException;
	/**
	 * Purpose:根據設備編號查找設備是否存在並且盤點此設備
	 * @author echomou
	 * @param stocktackId:盤點批號
	 * @param serialNumber:設備編號
	 * @param propertyId:財產批號
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return List<AssetStocktackListDTO>:待盤點設備
	 */
	public List<DmmAssetStacktakeListDTO> getAssetByStocktackId(String stocktackId, String serialNumber, String propertyId)throws DataAccessException;
	/**
	 * Purpose:根據設備盤點批號查詢當前要匯出的設備
	 * @author echomou
	 * @param stocktackId:設備盤點批號
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return List<AssetStocktackListDTO>：設備集合
	 */
	public List<DmmAssetStacktakeListDTO> export(AssetStacktakeFormDTO formDTO)throws DataAccessException;
	/**
	 * Purpose:根據設備盤點批號查詢當前要匯出的盤點結果
	 * @author echomou
	 * @param stocktackId:設備盤點批號
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return List<AssetStocktackListDTO>：設備集合
	 */
	public List<DmmAssetStacktakeListDTO> exportSummary(AssetStacktakeFormDTO formDTO)throws DataAccessException;
	/**
	 * Purpose: 判斷設備序號是否存在
	 * @author echomou
	 * @param sendSerialNumber：設備序號/財產編號
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return Boolean 是否存在
	 */
	public Boolean Check(String sendSerialNumber) throws DataAccessException;
	/**
	 * Purpose: 判斷當客戶輸入是設備序號時是否存在設備
	 * @author echomou
	 * @param stocktackId 盤點批號
	 * @param sendSerialNumber 設備序號
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return Boolean是否存在
	 */
	public Boolean checkList(String stocktackId, String sendSerialNumber)throws DataAccessException;
	/**
	 * Purpose: 保存assetStocktackList的資料
	 * @author CarrieDuan
	 * @param listId : 資料檔ID
	 * @param stocktackId ：盤點批號
	 * @param warWarehouseId ：盤點倉庫
	 * @param assetTypeId ：設備類別
	 * @param assetStatus ：設備狀態
	 * @param stocktackStatus ：盤點狀態
	 * @param userId ：當前人員ID
	 * @param userName ：當前人員名稱
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return void
	 */
	public void saveStocktackList(String listId, String stocktackId, String warWarehouseId, String assetTypeId, String assetStatus, Integer stocktackStatus, String userId, String userName)throws DataAccessException;
	/**
	 * Purpose:刪除指定盤點批號下的盤點設備
	 * @author echomou
	 * @param stocktackId ：盤點批號
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return void
	 */
	public void deleteStocktackList(String stocktackId)throws DataAccessException;
	/**
	 * Purpose:盤點完成后對未盤點的設備做操作
	 * @author echomou
	 * @param stocktackId ：盤點批號 
	 * @param id ：登錄者ID
	 * @param name ：登錄者姓名
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return void
	 */
	public void completeStocktack(String stocktackId, String id, String name)throws DataAccessException;
}
