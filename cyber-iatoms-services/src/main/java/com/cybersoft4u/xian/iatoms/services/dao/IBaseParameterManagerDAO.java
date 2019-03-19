package com.cybersoft4u.xian.iatoms.services.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BaseParameterItemDefDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.BaseParameterItemDef;

/**
 * Purpose: 系統參數維護DAO接口
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016年6月28日
 * @MaintenancePersonnel CrissZhang
 */
@SuppressWarnings("rawtypes")
public interface IBaseParameterManagerDAO extends IGenericBaseDAO<BaseParameterItemDef> {
	
	/**
	 * 
	 * Purpose: 從BASE_PARAMETER_TYPE_DEF表獲取參數類別下拉選單資料
	 * @author CrissZhang
	 * @throws DataAccessException 錯誤時拋出DAO異常類
	 * @return List<Parameter> 結果集
	 */
	public List<Parameter> listParameterTypes() throws DataAccessException;
	
	/**
	 * 
	 * Purpose: 根據條件查詢基礎參數表數據
	 * @author CrissZhang
	 * @param bptdCode 參數類型
	 * @param itemValue 參數代碼
	 * @param itemName 參數名稱
	 * @param sortDirection 排序方向
	 * @param sortFieldName 排序欄位名稱
	 * @param currentPage 當前頁
	 * @param pageSize 每頁總筆數
	 * @throws DataAccessException 錯誤時拋出DAO異常類
	 * @return List<BaseParameterItemDefDTO> 結果集
	 */
	public List<BaseParameterItemDefDTO> listBy(String bptdCode, String itemValue, String itemName, String sortDirection, String sortFieldName, Integer currentPage, Integer pageSize) throws DataAccessException;
	
	/**
	 * 
	 * Purpose: 根據條件查詢基礎表總結果數
	 * @author CrissZhang
	 * @param bptdCode 參數類型
	 * @param itemValue 參數代碼
	 * @param itemName 參數名稱
	 * @param approvedFlag 覆核標記
	 * @throws DataAccessException DAO異常類
	 * @return int 結果集
	 */
	public int count(String bptdCode, String itemValue, String itemName) throws DataAccessException;
	
	/**
	 * 
	 * Purpose: 根據所給條件查詢對應資料
	 * @author CrissZhang
	 * @param bpidId 參數Id
	 * @param bptdCode 參數類型
	 * @param itemValue 參數代碼
	 * @param itemName 參數名稱
	 * @throws DataAccessException 錯誤時拋出dao異常類
	 * @return BaseParameterItemDefDTO 查詢結果
	 */
	public BaseParameterItemDefDTO getBaseParameterItemDefDTO(String bpidId, Timestamp effectiveDate, String bptdCode, String itemValue, String itemName) throws DataAccessException;
	
	/**
	 * Purpose:驗證當前傳入的條件是否存在重復值
	 * @author CrissZhang
	 * @param bpidId ： 當前參數編號
	 * @param bptdCode ： 參數類別
	 * @param itemValue ：參數代碼
	 * @param itemName ： 參數名稱
	 * @throws DataAccessException ： 錯誤時拋出dao異常類
	 * @return boolean ：返回是否成功的結果
	 */
	public boolean isRepeat(String bpidId, String bptdCode, String itemValue, String itemName) throws DataAccessException;
	
	/**
	 * Purpose:根據bptdCode刪除轉入的基本參數
	 * @author CrissZhang
	 * @param bptdCode ： 參數類別
	 * @throws DataAccessException ： 錯誤時拋出dao異常類
	 * @return void ： 無返回
	 */
	public void deleteTransferData(String bptdCodes) throws DataAccessException;
	/**
	 * 依參數類型代碼和版本日期(生效日期)取得基本參數項目清單.
	 * @param parameterType - 參數類型代碼
	 * @param versionDate - 版本日期(生效日期) 
	 * @return List<BaseParameterItemDefDTO> 基本參數項目清單
	 * @throws DataAccessException
	 */
	public List<BaseParameterItemDefDTO> list(String parameterType,Date versionDate) throws DataAccessException;
}
