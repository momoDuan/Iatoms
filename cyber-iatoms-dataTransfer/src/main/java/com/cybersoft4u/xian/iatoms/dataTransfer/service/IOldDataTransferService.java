package com.cybersoft4u.xian.iatoms.dataTransfer.service;

import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

/**
 * Purpose: 舊資料轉檔業務邏輯層接口
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2017-7-13
 * @MaintenancePersonnel CrissZhang
 */
public interface IOldDataTransferService extends IAtomicService{
	/**
	 * Purpose:部署流程
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext setup(SessionContext sessionContext) throws ServiceException;

	/**
	 * Purpose:初始化頁面
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:轉移行事曆數據
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext transferCalendar(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:轉移故障組件故障現象數據
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext transferFaultParamData(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:轉移公司數據
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext transferCompanyData(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:轉移倉庫據點數據
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext transferWarehouse(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:轉移客戶特店，特店表頭數據
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext transferMerchant(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:轉移程序版本數據
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext transferApplicaton(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:轉移合約數據
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext transferContract(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:設備品項轉入
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext transferAssetType(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:庫存資料轉入
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext transferRepository(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:庫存歷史資料轉入
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext transferHistoryRepository(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:轉移案件處理中資料
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext transferCaseHandleInfo(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:轉移使用者帳號資料
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext transferAdmUser(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:匯出未結案案件
	 * @author CarrieDuan
	 * @param sessionContext
	 * @throws ServiceException
	 * @return SessionContext
	 */
	public SessionContext uploadNotClosedCaseInfo(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:轉移最新案件資料
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext transferCaseNewHandleInfo(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:轉移報修原因、報修處理方式數據
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext transferProblemData(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:清空庫存歷史表
	 * @author amandawang
	 * @throws ServiceException
	 * @return Boolean
	 */
	public Boolean deleteHistoryRepository(String tempLogMessage) throws ServiceException;
	
	/**
	 * Purpose:轉tms程式版本
	 * @author CrissZhang
	 * @param sessionContext
	 * @throws ServiceException
	 * @return SessionContext
	 */
	public void transferApplicatonTms()throws ServiceException;
	
	/**
	 * Purpose:測試fomsDB連接速度
	 * @author CrissZhang
	 * @throws ServiceException
	 * @return void
	 */
	public void testSpeed() throws ServiceException;
	
	/**
	 * Purpose:查詢庫存歷史表總條數
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext countRepositoryHistData(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:更新交易參數
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext updateTransaction(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:增加設備鏈接資料
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext insertAssetLink(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:更新案件SIM設備
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext updateSimAssetLink(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:更新Smartpay退貨交易
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext updateSmartpayTrans(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:更新 一般交易 人工輸入 
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext updateManualInput(SessionContext sessionContext) throws ServiceException;
}
