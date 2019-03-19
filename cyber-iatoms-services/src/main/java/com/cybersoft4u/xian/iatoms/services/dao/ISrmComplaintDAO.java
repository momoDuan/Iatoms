package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import com.cybersoft4u.xian.iatoms.common.bean.dto.ComplaintDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmComplaint;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

/**
 * Purpose: 客訴管理DAO接口
 * @author	nicklin
 * @since	JDK 1.7
 * @date	2018/03/02
 * @MaintenancePersonnel cybersoft
 */

public interface ISrmComplaintDAO extends IGenericBaseDAO<SrmComplaint> {

	/**
	 * Purpose : 根據條件查詢客訴信息
	 * @author	 nicklin
	 * @param	 customer : 客戶
	 * @param	 merchantCode : 特店代號
	 * @param	 tid ：TID
	 * @param	 merchantName : 特店名稱
	 * @param	 questionType : 問題分類
	 * @param	 vendor : 歸責廠商
	 * @param	 isCustomer : 賠償客戶
	 * @param	 isVendor : 廠商罰款
	 * @param	 startDate : 發生日期起
	 * @param	 endDate : 發生日期迄
	 * @param	 pageSize ： 每頁筆數
	 * @param	 pageIndex ： 頁碼
	 * @param	 sort ： 升序降序
	 * @param	 orderby : 排序字段
	 * @throws	 DataAccessException : 出錯時, 丟出DataAccessException
	 * @return	 List<ComplaintDTO> : 客訴信息List
	 */
	public List<ComplaintDTO> listBy(String customerId, String merchantCode, String tid, String merchantName, 
			String questionType, String vendor, String isCustomer, String isVendor, String startDate, 
			String endDate, Integer pageSize, Integer pageIndex, String sort, String orderby) throws DataAccessException;
	
	/**
	 * Purpose : 獲取符合條件資料總筆數
	 * @author	 nicklin
	 * @param	 queryCustomer : 客戶
	 * @param	 queryMerchantCode : 特店代號
	 * @param	 queryTid ：TID
	 * @param	 queryMerchantName : 特店名稱
	 * @param	 queryQuestionType : 問題分類
	 * @param	 queryVendor : 歸責廠商
	 * @param	 queryIsCustomer : 賠償客戶
	 * @param	 queryIsVendor : 廠商罰款
	 * @param	 queryStartDate : 發生日期起
	 * @param	 queryEndDate : 發生日期迄
	 * @throws	 DataAccessException : 出錯時, 丟出DataAccessException
	 * @return	 Integer : 總筆數
	 */
	public Integer count(String queryCustomerId, String queryMerchantCode, String queryTid, String queryMerchantName,
			String queryQuestionType, String queryVendor, String queryIsCustomer, String queryIsVendor,
			String queryStartDate, String queryEndDate) throws DataAccessException;
	
	/**
	 * Purpose:透過案件編號得到案件信息
	 * @author nicklin
	 * @param  caseId ： 案件編號
	 * @throws DataAccessException ： 出錯時拋出DataAccessException
	 * @return List<ComplaintDTO> ： 得到案件處理信息的DTO
	 */
	public List<ComplaintDTO> getCaseInfoById(String caseId) throws DataAccessException;
}
