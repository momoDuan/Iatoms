package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.PvmDtidDefDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.PvmDtidDef;

/**
* Purpose: DTIO帳號管理DAO
* @author CarrieDuan
* @since  JDK 1.7
* @date   2016/9/21
* @MaintenancePersonnel CarrieDuan
*/
public interface IPvmDtidDefDAO extends IGenericBaseDAO<PvmDtidDef>{

	/**
	 * Purpose: 根據條件查詢DTID列表
	 * @author CarrieDuan
	 * @param customerId ：客戶ID
	 * @param assetTypeId ：設備ID
	 * @param dtidStart：DTID號碼起
	 * @param dtidEnd: DTID號碼止
	 * @param sort ：排序方式
	 * @param order ：排序列
	 * @param page ：頁碼
	 * @param size ：每頁顯示數量
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<PvmDtidDefDTO> ：DTID集合
	 */
	public List<PvmDtidDefDTO> listBy(String customerId, String assetTypeId, String dtidStart, String dtidEnd, String sort, String order, int page, int size, boolean isPage) throws DataAccessException;

	/**
	 * Purpose:獲取總條數
	 * @author CarrieDuan
	 * @param customerId：客戶ID
	 * @param assetTypeId：設備ID
	 * @param dtidStart：DTID號碼起
	 * @param dtidEnd: DTID號碼止
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return Integer :返回總條數
	 */
	public Integer count (String customerId, String assetTypeId, String dtidStart, String dtidEnd) throws DataAccessException;
	
	/**
	 * Purpose: 檢驗DTID起止是否重複
	 * @author CarrieDuan
	 * @param dtidStart ：DTID起
	 * @param dtidEnd ：DTID止
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return boolean：返回是否重複
	 */
	public boolean isCheckRepeat(Integer dtidStart, Integer dtidEnd, String id) throws DataAccessException;
}
