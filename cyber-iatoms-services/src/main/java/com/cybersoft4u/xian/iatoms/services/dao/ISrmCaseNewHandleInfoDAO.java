package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseNewHandleInfo;
/**
 * Purpose: SRM_案件處理最新資料檔DAO實現類
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2016/12/19
 * @MaintenancePersonnel CarrieDuan
 */
public interface ISrmCaseNewHandleInfoDAO extends IGenericBaseDAO<SrmCaseNewHandleInfo>{
	/**
	 * Purpose:選擇DTID
	 * @author echomou
	 * @param customerId ：客戶ID
	 * @param merchantCode ：特店代號
	 * @param tId ：TID
	 * @param merchantName ：特店名稱
	 * @param edcNumber ：刷卡機設備序號
	 * @param headerName ：表頭（特店對外名稱）
	 * @param dtid ：DTID
	 * @param sort ：排序列
	 * @param order ：排序方式 
	 * @param currentPage ：當前頁
	 * @param pageSize ：頁碼
	 * @throws DataAccessException ：出錯時拋出DataAccessException
	 * @return List<DmmRepositoryDTO> ：DTID集合
	 */
	public List<SrmCaseHandleInfoDTO> listDtidBy(String customerId, String merchantCode, String tId, String merchantName, String edcNumber, String headerName, String dtid, String sort, String order, Integer currentPage, Integer pageSize) throws DataAccessException;
	
	/**
	 * Purpose:根據查詢條件查詢DTID信息的總筆數
	 * @author echomou
	 * @param customerId ：客戶ID
	 * @param merchantCode ：特店代號
	 * @param tId ：TID
	 * @param merchantName ：特店名稱
	 * @param edcNumber ：刷卡機設備序號
	 * @param headerName ：表頭（特店對外名稱）
	 * @param dtid ：DTID
	 * @throws DataAccessException ：出錯時拋出DataAccessException
	 * @return Integer ：條數
	 */
	public Integer getCount(String customerId, String merchantCode, String tId, String merchantName, String edcNumber, String headerName, String dtid, boolean isNew)throws DataAccessException;
	
	/**
	 * Purpose:根據案件dtid獲取案件信息
	 * @author CarrieDuan
	 * @param dtid：案件dtid
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return CaseHandleInfoDTO：案件信息
	 */
	public SrmCaseHandleInfoDTO getCaseHandleInfoDTOById(String dtid) throws DataAccessException;
	
	/**
	 * Purpose:根據案件dtid獲取案件信息
	 * @author CarrieDuan
	 * @param dtid：案件dtid
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return CaseHandleInfoDTO：案件信息
	 */
	public SrmCaseHandleInfoDTO getCaseHandleInfoDTO(String dtid, boolean isNewHave) throws DataAccessException;
	
	/**
	 * Purpose:根據案件dtid獲取案件信息
	 * @author CarrieDuan
	 * @param dtid：案件dtid
	 * @param isHave：處理中存在
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return CaseHandleInfoDTO：案件信息
	 */
	public SrmCaseHandleInfoDTO getCaseHandleInfoDTO(String dtid, boolean isHave, String customerId) throws DataAccessException;
	
	/**
	 * Purpose:刪除所有轉入數據
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return void : 無返回
	 */
	public void deleteTransferData() throws DataAccessException;
	
	/**
	 * Purpose:查看最新資料檔是否存在案件
	 * @author CrissZhang
	 * @param dtid : dtid
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return boolean : 返回一個布爾型
	 */
	public boolean isInNewCase(String dtid) throws DataAccessException;
	
	/**
	 * Purpose:查看最新資料檔是否存在案件
	 * @author CrissZhang
	 * @param dtid : dtid
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return boolean : 返回一個布爾型
	 */
	public boolean isInNewCase(String dtid, String customerId) throws DataAccessException;
	/**
	 * Purpose:查看處理中資料檔是否存在案件 Bug #3055
	 * @author amandawang
	 * @param dtid : dtid
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return boolean : 返回一個布爾型
	 */
	public boolean isInCase(String dtid, String customerId) throws DataAccessException;
	
	/**
	 * Purpose:查詢轉入最新案件筆數
	 * @author CrissZhang
	 * @throws DataAccessException
	 * @return List<Integer>
	 */
	public List<Integer> queryTransferNewCaseNum()throws DataAccessException;
}
