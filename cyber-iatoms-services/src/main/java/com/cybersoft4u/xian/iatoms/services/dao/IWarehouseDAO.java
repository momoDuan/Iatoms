package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.WarehouseDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimWarehouse;

/**
 * Purpose:倉庫DAO interface
 * @author ElvaHe
 * @since JDK 1.6
 * @date 2016年6月1日
 * @MaintenancePersonnel ElvaHe
 */
public interface IWarehouseDAO extends IGenericBaseDAO<BimWarehouse> {
	/**
	 * Purpose:分頁查詢數據
	 * @author ElvaHe
	 * @param queryCompanyId:廠商編號
	 * @param queryName:倉庫名稱
	 * @param pageSize:每頁大小
	 * @param pageIndex:頁序號
	 * @param sort:排序方式
	 * @param orderby:分組方式
	 * @throws DataAccessException:出錯拋出DataAccessException
	 * @return List<WarehouseDTO>:倉庫據點DTO列表
	 */
	public List<WarehouseDTO> listBy(String queryCompanyId, String queryName, Integer pageSize, Integer pageIndex, String sort, String orderby) throws DataAccessException;

	/**
	 * Purpose:查詢符合條件的筆數
	 * @author ElvaHe
	 * @param queryCompanyId:廠商編號
	 * @param queryName:倉庫名稱
	 * @throws DataAccessException:出錯拋出DataAccessException
	 * @return Integer:筆數
	 */
	public Integer count(String queryCompanyId, String queryName) throws DataAccessException;
	
	/**
	 * Purpose:保存時檢查同一廠商下倉庫名稱是否重複
	 * @author ElvaHe
	 * @param warehouseId:倉庫編號
	 * @param name:廠商名稱
	 * @param companyId:廠商編號
	 * @throws DataAccessException:出錯時拋DataAccessException
	 * @return Boolean:是否重複
	 */
	public boolean isCheck(String warehouseId, String name, String companyId) throws DataAccessException;
	
	/**
	 * Purpose:获取所有的仓库据点信息
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時拋DataAccessException
	 * @return List<Parameter>：仓库据点列表
	 */
	//public List<Parameter> getWarehouseList(String userId) throws DataAccessException;
	
	/**
	 * Purpose:根据用户获取仓库据点信息
	 * @author CrissZhang
	 * @param userId：客户编号
	 * @throws DataAccessException:出錯時拋DataAccessException
	 * @return List<Parameter>：仓库据点列表
	 */
	//public List<Parameter> getListByUserId(String userId) throws DataAccessException;
	
	/**
	 * Purpose:檢查倉庫內是否有設備
	 * @author ElvaHe
	 * @param warehouseId：倉庫編號
	 * @throws DataAccessException:出錯時拋DataAccessException
	 * @return Boolean：是否有
	 */
	public boolean isCheckWarehouse(String warehouseId) throws DataAccessException;

	/**
	 * Purpose:根据用户编号得到所有仓库据点信息
	 * @author CrissZhang
	 * @param userId : 用戶編號
	 * @throws DataAccessException:出錯時拋DataAccessException
	 * @return List<Parameter>：仓库据点列表
	 */
	public List<Parameter> getWarehouseByUserId(String userId) throws DataAccessException;
	
	/**
	 * Purpose:判斷當前DB是否存在數據 不存在返回true存在false
	 * @author CrissZhang
	 * @throws DataAccessException:出錯拋出DataAccessException
	 * @return boolean:是否存在數據的boolean值
	 */
	public boolean isNoData() throws DataAccessException;
	/**
	 * Purpose:通過倉庫名稱得到倉庫信息 若傳入warehouseName則根據傳入值查詢 若warehouseName爲空則查所有
	 * @author amandawang
	 * @param warehouseName : 倉庫名稱
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<Parameter> ： 倉庫集合
	 */
	public List<Parameter> listWarehouseByName(String warehouseName) throws DataAccessException;
	
	/**
	 * Purpose:刪除所有轉入數據
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return void : 無返回
	 */
	public void deleteTransferData() throws DataAccessException;
}
