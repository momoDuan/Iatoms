package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetInListDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetInList;
/**
 * 
 * Purpose: 設備入庫明細檔DAO
 * @author CarrieDuan
 * @since  JDK 1.6
 * @date   2016/6/29
 * @MaintenancePersonnel CarrieDuan
 */
public interface IAssetInListDAO extends IGenericBaseDAO<DmmAssetInList> {
	
	/**
	 * 
	 * Purpose:根據id刪除設備入庫明細檔
	 * @author CarrieDuan
	 * @param deleteAssetListIds :id 備入庫明細檔id
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return void
	 */
	public void deleteAssetListByIds(List<String> deleteAssetListIds) throws DataAccessException;
	/**
	 * 
	 * Purpose: 根據入庫批號獲取實際驗收的台數
	 * @author CarrieDuan
	 * @param assetInId 入庫批號
	 * @param isChecked: true :實際驗收，false：客戶驗收
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return Integer:驗收台數
	 */
	public Integer getAcceptCount(String assetInId, Boolean isChecked)throws DataAccessException;
	/**
	 * 
	 * Purpose:根據設備編號獲得設備名稱和財產編號
	 * @author CarrieDuan
	 * @param serialNumber:設備編號
	 * @throws DataAccessException：出錯時跑出DataAccessException
	 * @return AssetInListDTO:獲得ＤＴＯ集合
	 */
	public AssetInListDTO getAssetInListDTO(String serialNumber)throws DataAccessException;
	/**
	 * 
	 * Purpose:根據入庫批號刪除入庫明細檔
	 * @author CarrieDuan
	 * @param assetInId :入庫批號
	 * @throws DataAccessException：出錯時跑出DataAccessException
	 * @return void
	 */
	public void deleteAssetInListByAssetInId(String assetInId) throws DataAccessException;
	
	/**
	 * Purpose:檢測財產編號是否重複
	 * @author CarrieDuan
	 * @param propertyId ： 財產編號
	 * @throws DataAccessException：出錯時跑出DataAccessException
	 * @return Boolean ：是否重複
	 */
	public Boolean isCheckRropertyIdRepeat (String propertyId) throws DataAccessException;
	
	/**
	 * Purpose:查詢入庫清單
	 * @author CarrieDuan
	 * @param assetInId ：設備入庫單號
	 * @param order ：排序列
	 * @param sort ： 排序方式
	 * @param page ：頁碼
	 * @param rows ：每頁顯示條數
	 * @throws DataAccessException ：出錯時跑出DataAccessException
	 * @return List<AssetInListDTO> ：返回設備列表
	 */
	public List<AssetInListDTO> listBy(String assetInId, String order, String sort, Integer page, Integer rows) throws DataAccessException;
	
	/**
	 * Purpose:獲取總條數
	 * @author CarrieDuan
	 * @param assetInId ：設備入庫編號
	 * @throws DataAccessException ：出錯時跑出DataAccessException
	 * @return Integer ：返回條數
	 */
	public Integer count(String assetInId) throws DataAccessException;
	
	/**
	 * Purpose:設備入庫時匯入驗證重複，獲取所有的設備序號以及財產編號
	 * @author CarrieDuan
	 * @throws DataAccessException ：出錯時跑出DataAccessException
	 * @return List<AssetInListDTO> : 返回設備明細資料當集合
	 */
	public List<AssetInListDTO> getAssetInListDTOs() throws DataAccessException;
	/**
	 * 
	 * Purpose: 根據入庫批號獲取客戶實際驗收的台數
	 * @author CarrieDuan
	 * @param assetInId 入庫批號
	 * @param isChecked: true :實際驗收，false：客戶驗收
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return Integer:驗收台數
	 */
	public Integer getCustomerAcceptCount(String assetInId, Boolean isChecked) throws DataAccessException;
	/**
	 * Purpose:將入庫明細表中的數據刪除，存入歷史明細表中
	 * @author CarrieDuan
	 * @param assetInId 入庫批號
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return void
	 */
	public void saveAssetInListInfo(String assetInId)  throws DataAccessException;
}
