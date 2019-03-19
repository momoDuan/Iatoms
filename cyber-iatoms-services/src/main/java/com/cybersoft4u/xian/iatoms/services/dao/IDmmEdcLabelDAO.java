package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmEdcLabelDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmEdcLabel;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

/**
 * Purpose:刷卡機標籤管理DAO interface
 * @author NickLin 
 * @since  JDK 1.7
 * @date   2018/06/27
 * @MaintenancePersonnel CyberSoft
 */
public interface IDmmEdcLabelDAO extends IGenericBaseDAO<DmmEdcLabel> {
	
	/**
	 * Purpose : 根據條件查詢客訴信息
	 * @author	 NickLin
	 * @param	 queryMerchantCode : 特店代號
	 * @param	 queryDtid ： DTID
	 * @param	 queryStartDate : 匯入日期起
	 * @param	 queryEndDate : 匯入日期迄
	 * @param	 pageSize ： 每頁筆數
	 * @param	 pageIndex ： 頁碼
	 * @param	 sort ： 升序降序
	 * @param	 orderby : 排序字段
	 * @throws	 DataAccessException : 出錯時, 丟出DataAccessException
	 * @return	 List<DmmEdcLabelDTO> : 刷卡機標籤信息List
	 */
	public List<DmmEdcLabelDTO>listBy(String queryMerchantCode, String queryDtid, String queryStartDate, String queryEndDate,
			Integer pageSize, Integer pageIndex, String sort, String orderby) throws DataAccessException;
	
	/**
	 * Purpose : 獲取符合條件資料總筆數
	 * @author	 NickLin
	 * @param	 queryMerchantCode : 特店代號
	 * @param	 queryDtid ： DTID
	 * @param	 queryStartDate : 匯入日期起
	 * @param	 queryEndDate : 匯入日期迄
	 * @throws	 DataAccessException : 出錯時, 丟出DataAccessException
	 * @return	 Integer : 總筆數
	 */
	public Integer count(String queryMerchantCode, String queryDtid, String queryStartDate, String queryEndDate) throws DataAccessException;
	
	/**
	 * Purpose : 與DB對比資料，若merchantCode 與dtid 有重複之資料，判定該筆資料為更新
	 * @author	 NickLin
	 * @param	 merchantCode : 特店代號
	 * @param	 dtid ： DTID
	 * @throws	 DataAccessException : 出錯時, 丟出DataAccessException
	 * @return	 boolean : 是否為更新
	 */
	public boolean isUpdate(String merchantCode, String dtid) throws DataAccessException;
	
	/**
	 * Purpose : 根據Composite Key 查詢刷卡機標籤資訊
	 * @author	 NickLin
	 * @param	 merchantCode : 特店代號
	 * @param	 dtid ： DTID
	 * @throws	 DataAccessException : 出錯時, 丟出DataAccessException
	 * @return	 List<DmmEdcLabelDTO> : 刷卡機標籤信息
	 */
	public List<DmmEdcLabelDTO> getInfoByCompositeKey(String merchantCode, String dtid) throws DataAccessException;
}
