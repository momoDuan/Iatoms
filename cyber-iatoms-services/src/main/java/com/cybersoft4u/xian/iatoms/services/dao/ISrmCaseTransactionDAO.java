package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.Date;
import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTransactionDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseTransaction;
/**
 * Purpose: SRM_案件處理記錄DAO接口
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016年11月10日
 * @MaintenancePersonnel CrissZhang
 */
public interface ISrmCaseTransactionDAO extends IGenericBaseDAO<SrmCaseTransaction>{

	
	/**
	 * Purpose:根據案件編號得到案件附件檔資料的列表
	 * @author CrissZhang
	 * @param caseId ： 案件編號
	 * @param isHistory ： 是否查歷史表
	 * @throws DataAccessException ： 出錯時拋出DataAccessException
	 * @return List<SrmCaseTransactionDTO> ：案件處理記錄料列表
	 */
	public List<SrmCaseTransactionDTO> listByCaseId(String caseId, String isHistory, String sort, String order) throws DataAccessException;
	
	/**
	 * Purpose:查詢案件報修明細(環匯格式)資料
	 * @author ElvaHe
	 * @param customerId: 客戶編號
	 * @param startDate: 進件日期起
	 * @param endDate: 進件日期訖
	 * @param caseCategory: 案件類別為報修
	 * @return List<SrmCaseTransactionDTO>:案件報修明細(環匯格式)結果集
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 */
	public List<SrmCaseTransactionDTO> listCaseRepairDetailReportToGP(String customerId, Date startDate, Date endDate, String caseCategory) throws DataAccessException;
	
	/**
	 * Purpose:查詢案件設備明細(環匯格式)資料
	 * @author ElvaHe
	 * @param customerId: 客戶編號
	 * @param startDate: 上個月16日或當前月1日
	 * @param endDate: 當前日
	 * @return List<SrmCaseTransactionDTO>:案件設備明細(環匯格式)結果集
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 */
	public List<SrmCaseTransactionDTO> listCaseAssetDetailReportToGP(String customerId, Date startDate, Date endDate) throws DataAccessException;
	/**
	 * Purpose:判斷是否經過QA部分
	 * @author CarrieDuan
	 * @param caseId
	 * @throws DataAccessException
	 * @return List<SrmCaseTransactionDTO>
	 */
	public List<SrmCaseTransactionDTO> listAfterQAs(String caseId) throws DataAccessException;
	/**
	 * Purpose:獲取指定案件操作的最新一筆記錄
	 * @author CarrieDuan
	 * @param caseId
	 * @throws DataAccessException： 出錯時拋出DataAccessException
	 * @return List<SrmCaseTransactionDTO>：出錯時拋出DataAccessException
	 */
	public SrmCaseTransactionDTO listNewCompleteDate (String field, String caseId, String orderBy) throws DataAccessException;
	
	/**
	 * Purpose:根據歷程id獲取對應的【配送中】地址
	 * @author CarrieDuan
	 * @param caseId
	 * @throws DataAccessException： 出錯時拋出DataAccessException
	 * @return List<SrmCaseTransactionDTO>：歷程資料
	 */
	public SrmCaseTransactionDTO getLogisticsVendor (String transactionId) throws DataAccessException;
	
}
