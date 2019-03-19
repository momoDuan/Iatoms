package com.cybersoft4u.xian.iatoms.services.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetInInfoDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetInInfo;
/**
 * 
 * Purpose: 設備入庫主檔DAO interface
 * @author CarrieDuan
 * @since  JDK 1.6
 * @date   2016/6/29
 * @MaintenancePersonnel CarrieDuan
 */
public interface IAssetInInfoDAO extends IGenericBaseDAO<DmmAssetInInfo> {
	/**
	 * 
	 * Purpose:獲取入庫編號
	 * @author CarrieDuan
	 * @param  isDone 是否入庫
	 * @throws DataAccessException:出錯時拋出DataAccessException異常
	 * @return List<Parameter>:入庫編號集合
	 */
	public List<Parameter> listAssetInId(String isDone) throws DataAccessException;
	/**
	 * 
	 * Purpose: 獲取歷史入庫資料總條數
	 * @author CarrieDuan
	 * @param queryContractId :查詢條件-合約編號
	 * @param queryAssetInId ：查詢條件-設備入庫編號
	 * @throws DataAccessException:出錯時拋出DataAccessException異常
	 * @return Integer:入庫資料總條數
	 */
	public Integer getCount(String queryContractId, String queryAssetInId, String queryCompanyId) throws DataAccessException;
	
	/**
	 * Purpose: 查詢歷史入庫資料
	 * @author CarrieDuan
	 * @param queryContractId :查詢條件-合約編號
	 * @param queryAssetInId ：查詢條件-設備入庫編號
	 * @param order ：排序列
	 * @param sort ：排序方式
	 * @param page ：頁碼
	 * @param rows ：每頁顯示條數
	 * @throws DataAccessException ：出錯時拋出DataAccessException異常
	 * @return List<AssetInInfoDTO> ：返回列表
	 */
	public List<AssetInInfoDTO> listAssetInInfoDTO(String queryContractId, String queryAssetInId, String queryCompanyId, String order, String sort, Integer page, Integer rows) throws DataAccessException;
	
	/**
	 * Purpose: 根據庫存表ID獲取需要更新的入庫明細當Id
	 * @author CarrieDuan
	 * @param assetId :庫存表ID
	 * @param cyberApprovedDate :cyber驗證日期
	 * @throws DataAccessException：出錯時拋出DataAccessException異常
	 * @return void
	 */
	public void updateCyberApprovedDate(String assetId , Timestamp cyberApprovedDate, IAtomsLogonUser logonUser) throws DataAccessException;
	/**
	 * Purpose: 在批次異動時，異動合約編號或cyber日期時，獲取入庫信息
	 * @author CarrieDuan
	 * @param contractIds：合約ID集合
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<AssetInInfoDTO>:入庫DTO
	 */
	public List<AssetInInfoDTO> listAssetInInfoDTOByIds(List<String> assetInIds) throws DataAccessException;
	
	/**
	 * Purpose:入庫資料清除
	 * @author CrissZhang
	 * @param deleteDate : 刪除日期
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return void
	 */
	public void deleteAssetIn(Date deleteDate) throws DataAccessException;
}
