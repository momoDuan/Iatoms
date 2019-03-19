package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmHistoryCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmHistoryCaseHandleInfo;

/**
 * Purpose: 案件歷史處理資料檔DAO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/2/9
 * @MaintenancePersonnel CarrieDuan
 */
public interface ISrmHistoryCaseHandleInfoDAO extends IGenericBaseDAO<SrmHistoryCaseHandleInfo>{

	/**
	 * Purpose: 查詢案件歷史處理資料檔
	 * @author CarrieDuan
	 * @param historyId :id
	 * @param companyId :客戶ID
	 * @param merchantCode ：特點代號
	 * @param dtid ：DTID
	 * @param tid ：TID
	 * @param serialNumber ：設備序號
	 * @param caseId ：案件編號
	 * @param sort ：排序列
	 * @param order ：排序方式
	 * @param pageSize ：每頁顯示條數
	 * @param pageIndex ：第幾頁
	 * @throws DataAccessException ：出錯時拋出DataAccessException
	 * @return List<SrmHistoryCaseHandleInfoDTO> :案件資料集合
	 */
	public List<SrmHistoryCaseHandleInfoDTO> listBy(String historyId, String companyId, String merchantCode, String dtid, String tid, String serialNumber, String caseId, String sort, String order, Integer pageSize, Integer pageIndex)throws DataAccessException;
	
	/**
	 * Purpose: 獲取查詢結果總條數
	 * @author CarrieDuan
	 * @param historyId :id
	 * @param companyId :客戶ID
	 * @param merchantCode ：特點代號
	 * @param dtid ：DTID
	 * @param tid ：TID
	 * @param serialNumber ：設備序號
	 * @param caseId ：案件編號
	 * @throws DataAccessException ：出錯時拋出DataAccessException
	 * @return Integer
	 */
	public Integer getCount(String historyId, String companyId, String merchantCode, String dtid, String tid, String serialNumber, String caseId)throws DataAccessException;
}
