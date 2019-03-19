package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmSuppliesDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmSupplies;

/**
 * Purpose: 耗材品项维护DAO接口
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2016年7月05日
 * @MaintenancePersonnel HermanWang
 */
public interface ISuppliesTypeDAO extends IGenericBaseDAO<DmmSupplies> {
	/**
	 * Purpose:查詢符合條件的耗材品列表
	 * @author HermanWang
	 * @param queryCustomerId:查詢參數顧客ID
	 * @param querySuppliesCode：查詢參數耗材品名
	 * @param pageSize：分頁條件每頁條數
	 * @param page：分頁條件頁數
	 * @param order：排序字段
	 * @param Sort：排序方式
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<DmmSuppliesDTO>:返回一個耗材品列表
	 */
	public List<DmmSuppliesDTO> listBy(String queryCustomerId, String querySuppliesCode, String querySuppliesName, Integer pageSize, Integer page, String order, String sort) throws DataAccessException;
	/**
	 * Purpose:獲取查詢條件下的耗材品數量
	 * @author HermanWang
	 * @param queryCustomerId：查詢參數顧客ID
	 * @param querySuppliesId:查詢參數耗材品名
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return Integer：符合查詢條件的耗材品總筆數
	 */
	public Integer getCount(String queryCustomerId, String querySuppliesCode, String querySuppliesName)throws DataAccessException;
	/**
	 * Purpose:判斷同一客戶下的耗材品不能重複
	 * @author HermanWang
	 * @param companyId：客戶id
	 * @param suppliesName：耗材品名
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 */
	public boolean isCheck(String companyId, String suppliesName, String suppliesId) throws DataAccessException;
	
	/**
	 * Purpose:取得【耗材品項維護】該客戶的耗材資料
	 * @author CarrieDuan
	 * @param customerId:客戶ID
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<Parameter>：耗材名稱列表
	 */
	public List<Parameter> getSuppliesTypes (String customerId, String suppliesType) throws DataAccessException;
	/**
	 * Purpose::取得【耗材品項維護】該客戶的耗材分類
	 * @author HermanWang
	 * @param customerId
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<Parameter>：耗材分類列表
	 */
	public List<Parameter> getSuppliesTypeList (String customerId) throws DataAccessException;
	/**
	 * Purpose:取得【耗材品項維護】該客戶和耗材分類下的耗材名稱
	 * @author HermanWang
	 * @param customerId：客戶
	 * @param suppliesType：耗材分類
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<Parameter>：耗材名稱列表
	 */
	public List<Parameter> getSuppliesNameList(String customerId, String suppliesType) throws DataAccessException;
}
