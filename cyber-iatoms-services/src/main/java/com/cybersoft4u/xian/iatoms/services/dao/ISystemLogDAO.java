package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.Date;
import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmSystemLoggingDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmSystemLogging;

/**
 * 
 * Purpose: 系統日志DAO 
 * @author amandawang
 * @since  JDK 1.7
 * @date   2016年7月5日
 * @MaintenancePersonnel amandawang
 */
public interface ISystemLogDAO extends IGenericBaseDAO<AdmSystemLogging> {
	
	/**
	 * 
	 * Purpose: 根據條件查詢系統日志資料
	 * @author ericdu 
	 * @param account 使用者帳號
	 * @param formDate 起始日期
	 * @param toDate 終止日期
	 * @param currentPage 當前頁碼
	 * @param pageSize 每頁總筆數
	 * @param sort 排序欄位
	 * @param order 排序方式
	 * @throws DataAccessException 错误时抛出的dao異常類
	 * @return List<AdmSystemLogDTO> 結果集
	 */
	public List<AdmSystemLoggingDTO> listBy(String account, String formDate, String toDate, String sort, String order, Integer currentPage, Integer pageSize) throws DataAccessException;
	
	/**
	 * 
	 * Purpose: 查詢系統日志表中結果總數
	 * @author ericdu 
	 * @param account 使用者帳號
	 * @param formDate 起始日期
	 * @param toDate 終止日期
	 * @throws DataAccessException 错误时抛出的dao異常類
	 * @return Integer 結果条数
	 */
	public Integer count(String account, String formDate, String toDate) throws DataAccessException;
	
	/**
	 * Purpose:刪除歷史log
	 * @author CrissZhang
	 * @param deleteDate 刪除日期
	 * @throws DataAccessException 错误时抛出的dao異常類
	 * @return void
	 */
	public void deleteHistoryLog(Date deleteDate) throws DataAccessException;
	
	/**
	 * Purpose:轉移系統log
	 * @author CrissZhang
	 * @param transferDate log轉移日期
	 * @throws DataAccessException 错误时抛出的dao異常類
	 * @return void
	 */
	public void transferSystemLog(Date transferDate) throws DataAccessException;
}
