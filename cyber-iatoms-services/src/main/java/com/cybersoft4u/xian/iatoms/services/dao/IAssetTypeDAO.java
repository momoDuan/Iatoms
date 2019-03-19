package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetSupportedCommDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetSupportedFunctionDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetTypeCompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetTypeDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetType;

/**
 * Purpose: 設備品項維護DAO
 * @author HermanWang
 * @since  JDK 1.7
 * @date   2016年7月8日
 * @MaintenancePersonnel HermanWang
 */
@SuppressWarnings("rawtypes")
public interface IAssetTypeDAO extends IGenericBaseDAO<DmmAssetType> {
	
	/**
	 * 
	 * Purpose: 根据条件查询资料
	 * @author HermanWang
	 * @param assetCategoryCode 设备类别
	 * @param assetTyptId 設備編號
	 * @param sort 排序字段
	 * @param order 排序方式
	 * @throws DataAccessException 錯誤時拋出dao異常類
	 * @return List<AssetTypeDTO> 結果集
	 */
	public List<AssetTypeDTO> listBy(String assetCategoryCode, String assetTypeId, String sort, String order) throws DataAccessException;
	
	/**
	 * 
	 * Purpose: 統計復核查詢條件的資料數
	 * @author HermanWang
	 * @param assetCategoryCode 設備類別代碼
	 * @throws DataAccessException 錯誤時拋出dao異常類
	 * @return Integer 結果數
	 */
	public Integer count(String assetCategoryCode) throws DataAccessException;
	
	/**
	 * 
	 * Purpose: 廠商或者客戶信息初始化下拉框資料
	 * @author HermanWang
	 * @param companyId 廠商編號
	 * @param companyType 廠商類型
	 * @param companyCode 廠商代碼
	 * @param shortName 公司簡稱
	 * @throws DataAccessException 錯誤時拋出DAO異常
	 * @return List<Parameter> 結果集
	 */
	public List<Parameter> getCompanyParameterList(String companyId, String companyType, String companyCode, String shortName) throws DataAccessException;
	
	/**
	 * 
	 * Purpose: 獲取廠商使用設備檔資料
	 * @author HermanWang
	 * @param assetTypeId 設備類別編號
	 * @param companyId 廠商編號
	 * @throws DataAccessException 錯誤時拋出DAO異常
	 * @return List<AssetTypeCompanyDTO> 結果集
	 */
	public List<AssetTypeCompanyDTO> getAssetTypeCompanyDTOList(String assetTypeId, String companyId) throws DataAccessException;
	
	/**
	 * 
	 * Purpose: 獲取設備支援通訊模式檔資料
	 * @author HermanWang
	 * @param assetTypeId 設備類別編號
	 * @param commModeId 支援模式編號
	 * @throws DataAccessException 錯誤時拋出DAO異常
	 * @return List<AssetTypeCompanyDTO> 結果集
	 */
	public List<AssetSupportedCommDTO> getAssetSupportedCommDTOList(String assetTypeId, String commModeId) throws DataAccessException;
	
	/**
	 * 
	 * Purpose: 獲取設備支援功能檔資料
	 * @author HermanWang
	 * @param assetTypeId 設備類別編號
	 * @param functionId 功能編號
	 * @throws DataAccessException 錯誤時拋出DAO異常
	 * @return List<AssetTypeCompanyDTO> 結果集
	 */
	public List<AssetSupportedFunctionDTO> getAssetSupportedFunctionDTOList(String assetTypeId, String functionId) throws DataAccessException;
	
	/**
	 * 
	 * Purpose: 獲取設備名稱列表
	 * @author HermanWang
	 * @param assetTypeId 設備品類編號
	 * @param name 設備名稱
	 * @param assetCategory 设备类别
	 * @param contractId 合約ID
	 * @param deleted 是否已刪除
	 * @throws DataAccessException 錯誤時拋出DAO異常類
	 * @return List<Parameter> 結果集
	 */
	public List<Parameter> getAssetNameList(String assetTypeId, String name, String assetCategory, String contractId, String deleted) throws DataAccessException;
	/**
	 * Purpose:檢查設備代碼和設別名稱是否重複
	 * @author HermanWang
	 * @param assetTypeId：設別代碼
	 * @param name：設備名稱
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return Boolean：返回boolean類型
	 */
	public boolean isCheck(String assetTypeId, String name)throws DataAccessException;
	/**
	 * Purpose:獲取設備列表
	 * @author HermanWang
	 * @param editAssetTypeId
	 * @return
	 * @return List<AssetTypeDTO>
	 */
	public boolean isAssetList(String editAssetTypeId) throws DataAccessException;
	
	/**
	 * Purpose: 核檢設備名稱是否存在
	 * @author CarrieDuan
	 * @param name ：設備名稱
	 * @throws DataAccessException ：出錯時拋出DataAccessException
	 * @return boolean :是否存在
	 */
	public boolean isCheckAssetName(String name) throws DataAccessException;
	
	/**
	 * Purpose: 獲取設備型號
	 * @author CarrieDuan
	 * @param assetTypeId :id
	 * @param fieldName:需要查詢的列
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return String :設備型號
	 */
	public String getAssetInfo(String assetTypeId , String fieldName) throws DataAccessException;
	/**
	 * Purpose: 根據設備id獲取設備型號
	 * @author CarrieDuan
	 * @param assetTypeId :id
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<AssetTypeDTO> :設備型號
	 */
	public List<AssetTypeDTO> getAssetModelList(String assetTypeId) throws DataAccessException;
	
	/**
	 * Purpose:依據設備獲取對應的支援功能
	 * @author CarrieDuan
	 * @param assetTypeId：設備Id
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<Parameter>:支援功能列表
	 */
	public List<Parameter> listBuiltInFeatureByAssetTypeId(String assetTypeId) throws DataAccessException;
	
	/**
	 * Purpose:依據設備ID獲取對應的通訊模式
	 * @author CarrieDuan
	 * @param assetTypeId:設備ID
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<Parameter>：通訊模式列表
	 */
	public List<Parameter> listConnectionTypeByAssetTypeId(String assetTypeId) throws DataAccessException;
	
	/**
	 * Purpose:案件處理建案中，依據所選客戶獲取該客戶下的EDC設備/周邊設備（並且該設備的領用人為當前客戶）
	 * @author CarrieDuan
	 * @param customerId:客戶id
	 * @param ignoreDeleted ：忽略刪除
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<Parameter>:EDC列表
	 */
	public List<Parameter> listAssetByCustomerId (String customerId, String assetCategory, Boolean ignoreDeleted) throws DataAccessException;
	
	/**
	 * Purpose:通過設備名稱得到設備品項信息 若傳入assetName則根據傳入值模糊查詢 若assetName爲空則查所有
	 * @author CrissZhang
	 * @param assetName : 設備名稱
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<Parameter> ： 設備品項集合
	 */
	public List<Parameter> listAssetByName (String assetName, boolean isIgnore, boolean isEdc) throws DataAccessException;
	/**
	 * Purpose:通過設備名稱得到設備品項信息 若傳入assetName則根據傳入值模糊查詢 若assetName爲空則查所有
	 * @author amandawang
	 * @param assetName : 設備名稱
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<Parameter> ： 設備品項集合
	 */
	public List<Parameter> listAssetByNameAndType (String assetName, boolean isEdc, boolean isRelatedProducts) throws DataAccessException;
	
	/**
	 * Purpose:判斷當前DB是否存在數據 不存在返回true存在false
	 * @author CrissZhang
	 * @throws DataAccessException:出錯拋出DataAccessException
	 * @return boolean:是否存在數據的boolean值
	 */
	public boolean isNoData() throws DataAccessException;
	/**
	 * Purpose:得到周邊設備集合
	 * @author CrissZhang
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<Parameter> ： 周邊設備集合
	 */
	public List<Parameter> listPeripherals() throws DataAccessException;
	
	/**
	 * Purpose:刪除所有轉入數據
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return void : 無返回
	 */
	public void deleteTransferData() throws DataAccessException;
	/**
	 * Purpose:檢查設備代碼和設別名稱是否重複
	 * @author amandawang
	 * @param assetTypeId：設別代碼
	 * @param name：設備名稱
	 * @param assetCategory: 設備類別
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return Boolean：返回boolean類型
	 */
	public boolean isCheck(String assetTypeId, String name, String assetCategory) throws DataAccessException;
}
