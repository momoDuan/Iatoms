package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BimMerchantHeaderDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimMerchantHeader;

/**
 * Purpose: 特店表頭維護DAO interface
 * @author echomou
 * @since  JDK 1.6
 * @date   2016/6/8
 * @MaintenancePersonnel echomou
 */
public interface IMerchantHeaderDAO extends IGenericBaseDAO<BimMerchantHeader>{

	/**
    * Purpose: 根據查詢條件查詢特店表頭信息
    * @author echomou
    * @param queryCustomerId:查詢特店表頭時的id條件
    * @param queryMerCode :查詢特店表頭時的特店代號條件
    * @param queryRegisteredName： 查詢特店表頭時的名稱條件
    * @param queryAnnouncedName： 查詢特店表頭時的對外名稱條件
    * @param queryIsVip：查詢特店表頭時的vip條件
    * @param currentPage：當前頁
    * @param pageSize ：頁碼
    * @param sort  排序列
    * @param order 排序方式
    * @param merchantHeaderId 特店表頭主鍵
    * @param merchantId  客戶特店主鍵
    * @throws DataAccessException 出錯時拋出DataAccessException
    * @return List<BimMerchantHeaderDTO> 符合條件的特店表頭信息列
    */
	public List<BimMerchantHeaderDTO> listby(String queryCustomerId, String queryMerCode, String queryStagesMerCode, String queryRegisteredName, String queryAnnouncedName, String businessAddress, String queryIsVip, int currentPage, int pageSize, boolean isPage, String sort, String order, String merchantHeaderId,String merchantId) throws DataAccessException;
	/**
	 * Purpose: 根據查詢條件查詢特店表頭信息的總筆數
	 * @author echomou
	 * @param queryCustomerId 查詢特店表頭時的id條件
	 * @param queryMerCode 查詢特店表頭時的特店代號條件
	 * @param queryRegisteredName  查詢特店表頭時的名稱條件
	 * @param queryAnnouncedName 查詢特店表頭時的對外名稱條件
	 * @param queryIsVip 查詢特店表頭時的vip條件
	 * @throws DataAccessException 出錯時拋出DataAccessException
	 * @return Integer 總筆數
	 */
	public Integer getCount(String queryCustomerId, String queryMerCode, String queryRegisteredName,  String queryAnnouncedName, String businessAddress, String queryIsVip) throws DataAccessException;
	/**
	 * Purpose:判斷該特店下表頭是否重複
	 * @author echomou
	 * @param merchantCode 特店編號
	 * @param headerName：特店表頭名稱
	 * @throws DataAccessException: 出錯時拋出DataAccessException
	 * @return Boolean：是否重復
	 */
	public boolean check(String merchantId, String headerName, String merchantHeaderId) throws DataAccessException;
	
	/**
	 * Purpose:根據ID獲取表頭下拉框--案件處理
	 * @author CarrieDuan
	 * @param merchantId：特點ID
	 * @param customerId :客戶ID
	 * @param merchantCode：特點代號
	 * @throws DataAccessException: 出錯時拋出DataAccessException
	 * @return List<Parameter>：下拉列表
	 */
	public List<Parameter> getMerchantHeaders(String customerId, String merchantCode, String merchantId) throws DataAccessException;
	
	/**
	 * Purpose:案件匯入---根據表頭名稱以及特店獲取特點表頭ID
	 * @author CarrieDuan
	 * @param merchantId：特店ID
	 * @param headerName：表頭名稱
	 * @throws DataAccessException: 出錯時拋出DataAccessException
	 * @return BimMerchantHeaderDTO：特店表頭信息
	 */
	public BimMerchantHeaderDTO getMerchantHeaderDTOBy (String merchantId, String headerName, String merchantCode) throws DataAccessException;
	/**
	 * Purpose:獲取特店表頭集合
	 * @author amandawang
	 * @param merchantId：特店ID
	 * @param headerName：表頭名稱
	 * @throws DataAccessException: 出錯時拋出DataAccessException
	 * @return List<Parameter>：特店表頭集合
	 */
	public List<Parameter> getMerchantHeadersBy(String merchantId, String headerName) throws DataAccessException;
	/**
	 * Purpose:獲取特店表頭集合
	 * @author CrissZhang
	 * @throws DataAccessException
	 * @return List<BimMerchantHeaderDTO>：特店表頭集合
	 */
	public List<BimMerchantHeaderDTO> getMerchantHeaderList() throws DataAccessException;
	
	/**
	 * Purpose:獲取特店表頭集合
	 * @author CarrieDuan
	 * @param id：特店表頭ID
	 * @throws DataAccessException
	 * @return BimMerchantHeaderDTO：特店表頭
	 */
	public BimMerchantHeaderDTO getMerchantHeaderById(String id) throws DataAccessException;
}
