package com.cybersoft4u.xian.iatoms.services.dao;

import java.sql.Timestamp;
import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentInfoDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmPaymentInfo;

/**
 * Purpose: 求償資料檔DAO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/2/14
 * @MaintenancePersonnel CarrieDuan
 */
public interface ISrmPaymentInfoDAO extends IGenericBaseDAO<SrmPaymentInfo>{
	
	/**
	 * Purpose: 獲取求償資料信息
	 * @author CarrieDuan
	 * @param itemId :求償批號
	 * @param customerId ：客戶ID
	 * @param createCaseStart ：建案開始時間
	 * @param createCaseEnd ：建案結束時間
	 * @param merchantCode ：特點代號
	 * @param dtid ：DTID
	 * @param tid ：TID
	 * @param status ：資料狀態
	 * @param payType ：賠償方式
	 * @param realFinishDate ：通報遺失日
	 * @param serialNumber ：設備序號
	 * @param order ：排列字段
	 * @param sort ：排列方式
	 * @param page ：頁碼
	 * @param rows ：每頁顯示條數
	 * @throws DataAccessException：出错时抛出：DataAccessExecption
	 * @return List<SrmPaymentInfoDTO> :求償資料集合
	 */
	public List<SrmPaymentInfoDTO> listBy(String customerId, List<String> paymentIds, Timestamp createCaseStart, Timestamp createCaseEnd, String merchantCode, String dtid, String tid, String status, String payType, Timestamp lostDayStart, Timestamp lostDayEnd, String serialNumber, String order, String sort, int page, int rows, Boolean isMicro) throws DataAccessException;

	/**
	 * Purpose:  獲取查詢總條數
	 * @author CarrieDuan
	 * @param customerId ：客戶ID
	 * @param createCaseStart ：建案開始時間
	 * @param createCaseEnd ：建案結束時間
	 * @param merchantCode ：特點代號
	 * @param dtid ：DTID
	 * @param tid ：TID
	 * @param status ：資料狀態
	 * @param payType ：賠償方式
	 * @param realFinishDate ：通報遺失日
	 * @param serialNumber ：設備序號
	 * @throws DataAccessException：出错时抛出：DataAccessExecption
	 * @return Integer：符合條件的數量
	 */
	/*public Integer getCount(String customerId, Timestamp createCaseStart, Timestamp createCaseEnd, String merchantCode, String dtid, String tid, String status, String payType, Timestamp realFinishDate, String serialNumber)throws DataAccessException;*/
	
	/**
	 * Purpose: 根據條件獲取對應的求償批號
	 * @author CarrieDuan
	 * @param itemId :求償批號
	 * @param customerId ：客戶ID
	 * @param createCaseStart ：建案開始時間
	 * @param createCaseEnd ：建案結束時間
	 * @param merchantCode ：特點代號
	 * @param dtid ：DTID
	 * @param tid ：TID
	 * @param status ：資料狀態
	 * @param payType ：賠償方式
	 * @param lostDayStart ：通報遺失日起
	 * @param lostDayEnd ：通報遺失日止
	 * @param serialNumber ：設備序號
	 * @param order ：排列字段
	 * @param sort ：排列方式
	 * @param page ：頁碼
	 * @param rows ：每頁顯示條數
	 * @param isMicro ：是否微型商戶
	 * @throws DataAccessException：出错时抛出：DataAccessExecption
	 * @return List<String>
	 */
	public List<SrmPaymentInfoDTO> getIds(String customerId, String paymentId, Timestamp createCaseStart, Timestamp createCaseEnd, String merchantCode, String dtid, String tid, String status, String payType, Timestamp lostDayStart, Timestamp lostDayEnd, String serialNumber, String order, String sort, int page, int rows, boolean isMicro) throws DataAccessException;
	/**
	 * Purpose:維護費報表(大眾格式)維修耗材費用-AO已回覆扣款方式
	 * @author HermanWang
	 * @param customerId：TCB-EDC公司
	 * @throws DataAccessException：出错时抛出：DataAccessExecption
	 * @return List<SrmPaymentInfoDTO>
	 */
	public List<SrmPaymentInfoDTO> listFeePaymentInfoList(List<String> paymentTypeList, String customerId) throws DataAccessException;
	/**
	 * Purpose:維護費報表(大眾格式).維修耗材費用-AO未回覆扣款方式
	 * @author HermanWang
	 * @param customerId：TCB-EDC公司
	 * @throws DataAccessException：出错时抛出：DataAccessExecption
	 * @return List<SrmPaymentInfoDTO>
	 */
	public List<SrmPaymentInfoDTO> listFeePaymentInfoListToTCBEDC(List<String> paymentStatusList, String customerId) throws DataAccessException;
	/**
	 * Purpose:維護費報表(綠界格式)維修耗材費用-AO已回覆扣款方式
	 * @author HermanWang
	 * @param paymentTypeList 賠償方式 特店賠償、客戶賠償 
	 * @param customerId：綠界公司
	 * @throws DataAccessException
	 * @return List<SrmPaymentInfoDTO>
	 */
	public List<SrmPaymentInfoDTO> paymentInfoListToGreenWorld(List<String> paymentTypeList, String customerId) throws DataAccessException;
	/**
	 * Purpose:維護費報表(綠界格式).維修耗材費用-AO未回覆扣款方式
	 * @author HermanWang
	 * @param customerId：綠界公司
	 * @throws DataAccessException：出错时抛出：DataAccessExecption
	 * @return List<SrmPaymentInfoDTO>
	 */
	public List<SrmPaymentInfoDTO> paymentInfoListToGreenWorldNoTax(List<String> paymentStatusList, String customerId) throws DataAccessException;
	/**
	 * Purpose:維護費報表(上銀格式)維修耗材費用-AO已回覆扣款方式
	 * @author HermanWang
	 * @param paymentTypeList 賠償方式 特店賠償、客戶賠償 
	 * @param customerId：上銀公司
	 * @throws DataAccessException
	 * @return List<SrmPaymentInfoDTO>
	 */
	public List<SrmPaymentInfoDTO> paymentInfoListToScsb(List<String> paymentTypeList, String customerId) throws DataAccessException;
	/**
	 * Purpose:維護費報表(上銀格式).維修耗材費用-AO未回覆扣款方式
	 * @author HermanWang
	 * @param customerId：上銀公司
	 * @throws DataAccessException：出错时抛出：DataAccessExecption
	 * @return List<SrmPaymentInfoDTO>
	 */
	public List<SrmPaymentInfoDTO> paymentInfoListToScsbNoTax(List<String> paymentStatusList, String customerId) throws DataAccessException;
	/**
	 * Purpose:維護費報表(陽信格式)維修耗材費用-AO已回覆扣款方式
	 * @author HermanWang
	 * @param paymentTypeList 賠償方式： 特店賠償、客戶賠償 
	 * @param customerId：上銀公司
	 * @throws DataAccessException
	 * @return List<SrmPaymentInfoDTO>
	 */
	public List<SrmPaymentInfoDTO> paymentInfoListToSyb(List<String> paymentTypeList, String customerId) throws DataAccessException;
	/**
	 * Purpose:維護費報表(綠界格式).維修耗材費用-AO未回覆扣款方式
	 * @author HermanWang
	 * @param customerId：綠界公司
	 * @throws DataAccessException：出错时抛出：DataAccessExecption
	 * @return List<SrmPaymentInfoDTO>
	 */
	public List<SrmPaymentInfoDTO> paymentInfoListToSybNoTax(List<String> paymentStatusList, String customerId) throws DataAccessException;
	
	/**
	 * Purpose:維護費報表(綠界格式).維修耗材費用-AO未回覆扣款方式
	 * @author CarrieDuan
	 * @param paymentId：求償編號
	 * @throws DataAccessException：出错时抛出：DataAccessExecption
	 * @return List<SrmPaymentInfoDTO>
	 */
	public SrmPaymentInfoDTO getPaymentCaseInfo (String paymentId) throws DataAccessException;
	
	/**
	 * Purpose:Task #3346，驗證該筆案件是否由案件建立過求償資料
	 * @author CarrieDuan
	 * @param caseId：案件編號
	 * @throws DataAccessException：出错时抛出：DataAccessExecption
	 * @return List<SrmPaymentInfoDTO>
	 */
	public List<SrmPaymentInfoDTO> getByCaseCreate(String caseId) throws DataAccessException;
	
	/**
	 * Purpose:Task #3346，刪除求償資料
	 * @author CarrieDuan
	 * @param paymentId：案件編號
	 * @throws DataAccessException：出错时抛出：DataAccessExecption
	 * @return void
	 */
	public void deletePaymenInfo(List<String> paymentIds) throws DataAccessException;
}
