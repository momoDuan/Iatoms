package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.RepoStatusReportDTO;

/**
 * Purpose: 設備狀態報表DAO接口
 * @author barryzhang
 * @since  JDK 1.7
 * @date   2016年7月22日
 * @MaintenancePersonnel barryzhang
 */
@SuppressWarnings("rawtypes")
public interface IRepoStatusReportDAO extends IGenericBaseDAO {
	
	/**
	 * Purpose:根據條件查詢資料
	 * @author barryzhang
	 * @param tableName:查詢表明
	 * @param maTypeCode：維護模式
	 * @param assetTypeId：設備名稱
	 * @param commModeId：通訊模式
	 * @param date：查詢日期
	 * @param order：排序列
	 * @param sort：排序方式
	 * @param warehouseList:控管倉庫列表
	 * @param dataAcl:是否倉庫控管
	 * @param rows：一頁顯示多少行
	 * @param page:頁碼
	 * @throws DataAccessException DAO異常類
	 * @return List<RepoStatusReportDTO> 結果集
	 */
	public List<RepoStatusReportDTO> companylistBy(String tableName, String maTypeCode, String assetTypeId, String commModeId, String date, String order, String sort,List<String> warehouseList, String dataAcl, Integer rows, Integer page) throws DataAccessException;
	
	/**
	 * 
	 * Purpose: 根據條件查詢資料
	 * @author barryzhang
	 * @param tableName 要查詢的主表名稱
	 * @param companyId 客戶編號
	 * @param maTypeCode 維護模式代碼
	 * @param assetTypeId 設備名稱編號
	 * @param commModeId 通訊模式代碼
	 * @param date 查询日期
	 * @param order 排序列
	 * @param sort 排序方式
	 * @param warehouseList 控管的倉庫列表
	 * @param dataAcl 是否倉庫控管
	 * @throws DataAccessException DAO異常類
	 * @return List<RepositoryHistDTO> 結果集
	 */
	public List<RepoStatusReportDTO> listBy(String tableName, String companyId, String maTypeCode, String assetTypeId, String commModeId, String date, String order, String sort,List<String> warehouseList, String dataAcl) throws DataAccessException;
	
	/**
	 * 
	 * Purpose: 統計符合條件的資料數
	 * @author barryzhang
	 * @param tableName 要查詢的主表名稱
	 * @param companyId 客戶
	 * @param maTypeCode 維護模式
	 * @param assetTypeId 設備名稱
	 * @param commModeId 通訊模式
	 * @param date 查询日期
	 * @param warehouseList 控管的倉庫列表
	 * @param dataAcl 是否倉庫控管
	 * @throws DataAccessException DAO異常類
	 * @return Integer 資料數目
	 */
	public Integer count(String tableName, String companyId, String maTypeCode, String assetTypeId, String commModeId, String date, List<String> warehouseList, String dataAcl) throws DataAccessException;
	
	/**
	 * 
	 * Purpose: 根據條件查詢資料
	 * @author ericdu
	 * @param tableName 要查詢的主表名稱
	 * @param companyId 客戶編號
	 * @param maTypeCode 維護模式代碼
	 * @param assetTypeId 設備名稱編號
	 * @param commModeId 通訊模式代碼
	 * @param date 查询日期
	 * @param order 排序方式
	 * @param warehouseList 控管的倉庫列表
	 * @param dataAcl 是否倉庫控管
	 * @throws DataAccessException DAO異常類
	 * @return List<RepositoryHistDTO> 結果集
	 */
	public List<RepoStatusReportDTO> listRepositoryStatus(String tableName, String companyId, String maTypeCode, String assetTypeId, String commModeId, String date, List<String> warehouseList, String dataAcl) throws DataAccessException;
}
