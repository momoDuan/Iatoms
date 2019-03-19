package com.cybersoft4u.xian.iatoms.services;

import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;
/**
 * Purpose: 報修問題分析報表Service interface
 * @author echomou
 * @since  JDK 1.6
 * @date   2016/11/14
 * @MaintenancePersonnel echomou
 */
@SuppressWarnings("rawtypes")
public interface IRepairReportService extends IAtomicService{
	/**
	 * Purpose: 初始化頁面
	 * @author echomou
	 * @param sessionContext ：上下文Context
	 * @throws ServiceException ：service異常類
	 * @return SessionContext ：上下文Context
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:查詢報修問題
	 * @author echomou
	 * @param sessionContext ：上下文Context
	 * @throws ServiceException ：service異常類
	 * @return SessionContext ：上下文Context
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:查詢處理方式
	 * @author echomou
	 * @param sessionContext ：上下文Context
	 * @throws ServiceException ：service異常類
	 * @return SessionContext ：上下文Context
	 */
	public SessionContext list(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:匯出報表所需的數據
	 * @author echomou
	 * @param sessionContext ：上下文Context
	 * @throws ServiceException ：service異常類
	 * @return SessionContext ：上下文Context
	 */
	public SessionContext export(SessionContext sessionContext) throws ServiceException;
}
