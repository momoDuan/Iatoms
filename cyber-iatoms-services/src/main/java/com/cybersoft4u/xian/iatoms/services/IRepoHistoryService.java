package com.cybersoft4u.xian.iatoms.services;

import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

/**
 * Purpose: 設備歷史記錄查詢Service
 * @author ericdu
 * @since  JDK 1.7
 * @date   2016年7月20日
 * @MaintenancePersonnel ericdu
 */
@SuppressWarnings("rawtypes")
public interface IRepoHistoryService extends IAtomicService {

	/**
	 * 
	 * Purpose: 初始化
	 * @author ericdu
	 * @param sessionContext 上下文Context
	 * @throws ServiceException service異常類
	 * @return SessionContext 上下文Context
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * 
	 * Purpose: 查詢
	 * @author ericdu
	 * @param sessionContext 上下文Context
	 * @throws ServiceException service異常類
	 * @return SessionContext 上下文Context
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * 
	 * Purpose: 匯出資料查詢
	 * @author ericdu
	 * @param sessionContext 上下文Context
	 * @throws ServiceException service異常類
	 * @return SessionContext 上下文Context
	 */
	public SessionContext export(SessionContext sessionContext) throws ServiceException;
}
