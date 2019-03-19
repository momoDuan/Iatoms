package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.RepoStatusReportDTO;

/**
 * Purpose: 客戶設備總表DAO接口
 * @author HermanWang
 * @since  JDK 1.7
 * @date   2016年7月29日
 * @MaintenancePersonnel HermanWang
 */
@SuppressWarnings("rawtypes")
public interface ICustomerRepoDAO extends IGenericBaseDAO {
	
	/**
	 * 
	 * Purpose:查詢符合條件的設備
	 * @author HermanWang
	 * @param tableName：表名
	 * @param yyyyMM：查詢日期
	 * @param customer：客戶
	 * @param maType：設備類別
	 * @throws DataAccessException：出錯時丟出DataAccessException異常
	 * @return List<Object>
	 */
	public List<RepoStatusReportDTO> list(String tableName, Boolean isCustomerAttribute, String userId, String dataAcl, String yyyyMM, String customer, String maType, Integer pageSize, Integer page, String order, String sort) throws DataAccessException;
	/**
	 * 
	 * Purpose:查詢符合條件的設別列表
	 * @author HermanWang
	 * @param tableName：表名
	 * @throws DataAccessExceptio：出錯時丟出DataAccessException異常n
	 * @return List<Parameter>
	 */
	public List<Parameter> getAssetNameList(String tableName, String customerId, String maType) throws DataAccessException;
	/**
	 * Purpose:分頁查詢條數
	 * @author HermanWang
	 * @param getQueryCustomer：查詢條件 客戶
	 * @param getYyyyMM：查詢條件 日期
	 * @param getQueryMaType：查詢條件 維護模式
	 * @throws DataAccessException：出錯時丟出DataAccessException異常
	 */
	public List<RepoStatusReportDTO> getAssetList(String tableName, Boolean isCustomerAttribute, String userId, String dataAcl, String yyyyMM, String customer, String maType)throws DataAccessException;
}
