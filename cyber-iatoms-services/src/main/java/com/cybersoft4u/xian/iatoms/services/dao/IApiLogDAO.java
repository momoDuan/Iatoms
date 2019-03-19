package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.ApiLogDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.ApiLog;

public interface IApiLogDAO extends IGenericBaseDAO<ApiLog> {

	/**
	 * 查詢api-log數據
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public ApiLogDTO getApiLogDto(String id) throws DataAccessException;
	/**
	 * 新增api_log表數據
	 * @param apiLogDTO
	 * @throws DataAccessException
	 */
	public void insertApiLog(ApiLogDTO apiLogDTO) throws DataAccessException;
	/**
	 * Purpose : 根據條件查詢電文紀錄
	 * @author	 NickLin
	 * @param	 queryClientCode : ClientCode
	 * @param	 queryStartDate : 建檔日期起
	 * @param	 queryEndDate : 建檔日期迄
	 * @param	 pageSize ： 每頁筆數
	 * @param	 pageIndex ： 頁碼
	 * @param	 sort ： 升序降序
	 * @param	 orderby : 排序字段
	 * @throws	 DataAccessException : 出錯時, 丟出DataAccessException
	 * @return	 List<ApiLogDTO> : 電文紀錄List
	 */
	public List<ApiLogDTO> listBy(String queryClientCode, String queryStartDate, String queryEndDate,
			Integer pageSize, Integer pageIndex, String sort, String orderby) throws DataAccessException;
	/**
	 * Purpose : 獲取符合條件資料總筆數
	 * @author	 NickLin
	 * @param	 queryClientCode : ClientCode
	 * @param	 queryStartDate : 建檔日期起
	 * @param	 queryEndDate : 建檔日期迄
	 * @throws	 DataAccessException : 出錯時, 丟出DataAccessException
	 * @return	 Integer : 總筆數
	 */
	public Integer count(String queryClientCode, String queryStartDate, String queryEndDate) throws DataAccessException;
}
