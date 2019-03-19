package com.cybersoft4u.xian.iatoms.services;


import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

/**
 * 
 * Purpose: 系統日志Service
 * @author amandawang
 * @since  JDK 1.7
 * @date   2016年7月5日
 * @MaintenancePersonnel amandawang
 */
@SuppressWarnings("rawtypes")
public interface ISystemLogService extends IAtomicService {
	
	/**
	 * 
	 * Purpose: 初始化頁面
	 * @author ericdu
	 * @param sessionContext 上下文Context
	 * @throws ServiceException 错误时抛出的service異常類
	 * @return SessionContext 上下文Context
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * 
	 * Purpose: 查詢系統日志
	 * @author ericdu
	 * @param sessionContext 上下文Context
	 * @throws ServiceException 错误时抛出的service異常類
	 * @return SessionContext 上下文Context
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException;

	/**
	 * 
	 * Purpose: 查詢匯出資料
	 * @author ericdu
	 * @param sessionContext 上下文Context
	 * @throws ServiceException 错误时抛出的service異常類
	 * @return SessionContext 上下文Context
	 */
	public SessionContext export(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:打開log執行內容的對話框
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext openLogDialog(SessionContext sessionContext) throws ServiceException;
}
