package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.ApplicationDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.PvmApplication;

/**
 * Purpose: 程式版本維護DAO interface
 * @author echomou
 * @since  JDK 1.6
 * @date   2016-7-14
 * @MaintenancePersonnel echomou
 */
@SuppressWarnings({"rawtypes"})
public interface IPvmApplicationDAO extends IGenericBaseDAO<PvmApplication> {
	
	/**
	 * Purpose:獲取程式版本信息
	 * @author echomou
	 * @param companyId：客戶編號
	 * @param name：程式名稱
	 * @param version：版本編號
	 * @param assetTypeId：適用設備編號
	 * @param sort：按照此列排序
	 * @param order：排序方式
	 * @param rows：一頁筆數
	 * @param page：當前頁碼
	 * @param applicationId：程式版本主鍵編號
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<ApplicationDTO>：程式版本信息
	 */
	public List<ApplicationDTO> listByApplication(String companyId, String name, String version, 
			String assetTypeId, String sort, String order, Integer rows, Integer page, String applicationId)throws DataAccessException;
	
	/**
	 * Purpose:刪除程式與設備關聯表
	 * @author echomou
	 * @param applicationId：主鍵編號
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return boolean：是否刪除成功
	 */
	public boolean deleteApplicationAssetLink(String applicationId)throws DataAccessException;
	
	/**
	 * Purpose:獲取設備列表
	 * @author echomou
	 * @param assetCategory：設備類型編號
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<Parameter>：設備列表
	 */
	public List<Parameter> listByAssetType(String assetCategory)throws DataAccessException;
	
	/**
	 * Purpose:獲取總筆數
	 * @author echomou
	 * @param companyId：客戶編號
	 * @param name：程式名稱
	 * @param version：版本編號
	 * @param assetTypeId：適用設備編號
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return Integer：總筆數
	 */
	public Integer count(String companyId, String name, String version, String assetTypeId)throws DataAccessException;
	/**
	 * Purpose: 檢驗同一客戶下程式名稱，版本編號是否重複
	 * @author echomou
	 * @param applicationId 主鍵
	 * @param customerId 客戶編號
	 * @param version 版本編號
	 * @param applicationName 程式名稱
	 * @return Boolean 是否重複
	 */
	public boolean check(String applicationId, String customerId, String version, String applicationName);
	
	/**
	 * Purpose:根據客戶和刷卡機行獲取軟體版本
	 * @author CarrieDuan
	 * @param customerId:客戶ID
	 * @param edcType：刷卡機行
	 * @param searchDeletedFlag：是否查詢已刪除標誌位
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<Parameter>：軟體版本列表
	 */
	public List<Parameter> listSoftwareVersionsBy(String customerId, String edcType, String searchDeletedFlag) throws DataAccessException;
	
	/**
	 * Purpose:判斷當前DB是否存在數據 不存在返回true存在false
	 * @author CrissZhang
	 * @throws DataAccessException:出錯拋出DataAccessException
	 * @return boolean:是否存在數據的boolean值
	 */
	public boolean isNoData() throws DataAccessException;
	
	/**
	 * Purpose:得到所有程式版本集合
	 * @author CrissZhang
	 * @throws DataAccessException
	 * @return List<ApplicationDTO>：程式版本列表
	 */
	public List<ApplicationDTO> getApplicationList() throws DataAccessException;
	
	/**
	 * Purpose:刪除所有轉入數據
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return void : 無返回
	 */
	public void deleteTransferData() throws DataAccessException;
}
