package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import com.cybersoft4u.xian.iatoms.common.bean.dto.RepairReportDTO;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;
/**
 * Purpose: 報修問題分析報表DAO interface
 * @author echomou
 * @since  JDK 1.6
 * @date   2016/11/14
 * @MaintenancePersonnel echomou
 */
@SuppressWarnings("rawtypes")
public interface IRepairReportDAO extends IGenericBaseDAO {
	/**
	 * Purpose:根據查詢條件查詢報修問題
	 * @author echomou
	 * @param queryCustomer：客戶
	 * @param closedTimeStart：結案時間起
	 * @param closedTimeEnd：結案時間迄
	 * @param queryMerchantCode：特店代號
	 * @param queryEdcType：刷卡機型
	 * @param currentPage：當前頁
	 * @param pageSize：頁碼
	 * @param sort ：排序列
	 * @param order：排序方式
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<RepairReportDTO> ：符合條件的報修問題列
	 */
	public List<RepairReportDTO> listby(String queryCustomer, String closedTimeStart, String closedTimeEnd, String queryMerchantCode, String queryEdcType, Integer currentPage, Integer pageSize, boolean isPage, String sort, String order) throws DataAccessException;
	/**
	 * Purpose:根據查詢條件查詢報修問題信息的總筆數
	 * @author echomou
	 * @param queryCustomer：客戶
	 * @param closedTimeStart：結案時間起
	 * @param closedTimeEnd：結案時間迄
	 * @param queryMerchantCode：特店代號
	 * @param queryEdcType：刷卡機型
	 * @throws DataAccessException 出錯時拋出DataAccessException
	 * @return int 總筆數
	 */
	public int getCount(String queryCustomer, String closedTimeStart, String closedTimeEnd, String queryMerchantCode, String queryEdcType) throws DataAccessException;
	/**
	 * Purpose:根據查詢條件查詢處理方式
	 * @author echomou
	 * @param queryCustomer：客戶
	 * @param closedTimeStart：結案時間起
	 * @param closedTimeEnd：結案時間迄
	 * @param queryMerchantCode：特店代號
	 * @param queryEdcType：刷卡機型
	 * @param currentPage：當前頁
	 * @param pageSize：頁碼
	 * @param sort ：排序列
	 * @param order：排序方式
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<RepairReportDTO> ：符合條件的處理方式列
	 */
	public List<RepairReportDTO> listSolve(String queryCustomer, String closedTimeStart, String closedTimeEnd, String queryMerchantCode, String queryEdcType, Integer currentPage, Integer pageSize, boolean isPage, String sort, String order) throws DataAccessException;
	/**
	 * Purpose:根據查詢條件查詢處理方式信息的總筆數
	 * @author echomou
	 * @param queryCustomer：客戶
	 * @param closedTimeStart：結案時間起
	 * @param closedTimeEnd：結案時間迄
	 * @param queryMerchantCode：特店代號
	 * @param queryEdcType：刷卡機型
	 * @throws DataAccessException 出錯時拋出DataAccessException
	 * @return int 總筆數
	 */
	public int geSolvetCount(String queryCustomer, String closedTimeStart, String closedTimeEnd, String queryMerchantCode, String queryEdcType) throws DataAccessException;

}
