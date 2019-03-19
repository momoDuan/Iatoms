package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryHistoryDTO;

/**
 * Purpose: 設備歷史記錄查詢DAO
 * @author ericdu
 * @since  JDK 1.7
 * @date   2016年7月20日
 * @MaintenancePersonnel ericdu
 */
@SuppressWarnings("rawtypes")
public interface IDmmRepositoryHistoryDAO extends IGenericBaseDAO {
	
	/**
	 * 
	 * Purpose: 根據條件查詢資料
	 * @author ericdu
	 * @param histId 歷史記錄編號
	 * @param serialNumber 設備序號
	 * @param tid TID
	 * @param dtid DTID
	 * @param sort 排序欄位
	 * @param order 排序方式
	 * @param currentPage 當前頁碼
	 * @param pageSize 每頁總筆書
	 * @throws DataAccessException DAO異常類
	 * @return List<DmmRepositoryHistoryDTO> 結果集
	 */
	public List<DmmRepositoryHistoryDTO> list(String histId, String serialNumber, String tid, String dtid, String sort, String order, Integer currentPage, Integer pageSize) throws DataAccessException;
	
	/**
	 * 
	 * Purpose: 統計符合條件的結果數
	 * @author ericdu
	 * @param histId 歷史編號
	 * @param serialNumber 設備序號
	 * @param tid TID
	 * @param dtid DTID
	 * @throws DataAccessException DAO異常類
	 * @return Integer
	 */
	public Integer count(String histId, String serialNumber, String tid, String dtid) throws DataAccessException;
	/**
	 * Purpose:設備資料複製功能
	 * @author HermanWang
	 * @param isHis:是否第一次進入
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return void
	 */
	public void copyToMonthTable(String isHis) throws DataAccessException;
	
	/**
	 * Purpose:取消設備鏈接時候根據設備歷史檔id獲取設備關聯案件之前狀態
	 * @author HermanWang
	 * @param historyAssetId:案件設備鏈接當存儲的設備歷史id
	 * @throws DataAccessException
	 * @return DmmRepositoryHistoryDTO
	 */
	public DmmRepositoryHistoryDTO getStatusByHistoryId(String historyAssetId) throws DataAccessException;

	/**
	 * Purpose:設備資料批次異動時，保存數據至歷史資料檔
	 * @author CarrieDuan
	 * @param repositoryIds ：需要賦值的庫存表數據Id集合
	 * @param historyId：歷史庫存id
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return void
	 */
	public void insertRepositoryHistory(List<String> repositoryIds, String historyId)throws DataAccessException;
	/**
	 * Purpose:判斷庫存主檔是否為空
	 * @author amandawang
	 * @throws DataAccessException
	 * @return boolean
	 */
	public Integer countData() throws DataAccessException;
	
	/**
	 * Purpose:刪除所有轉入數據
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return void : 無返回
	 */
	public void deleteTransferData() throws DataAccessException;
}
