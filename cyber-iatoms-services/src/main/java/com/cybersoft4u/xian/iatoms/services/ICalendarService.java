package com.cybersoft4u.xian.iatoms.services;

import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;
/**
 * 
 * Purpose: 行事曆interface
 * @author echomou	
 * @since  JDK 1.6
 * @date   2016/6/30
 * @MaintenancePersonnel cybersoft
 */
public interface ICalendarService extends IAtomicService {
	/**
	 * Purpose: 頁面初始化
	 * @author echomou
	 * @param sessionContext ：上下文SessionContext
	 * @throws ServiceException：出錯時拋出ServiceException
	 * @return SessionContext: 上下文SessionContext
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose: "上一年"的初始化
	 * @author echomou
	 * @param sessionContext ：上下文SessionContext
	 * @throws ServiceException：出錯時拋出ServiceException
	 * @return SessionContext: 上下文SessionContext
	 */
	public SessionContext initPreYear(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose: “下一年”的初始化
	 * @author echomou
	 * @param sessionContext ：上下文SessionContext
	 * @throws ServiceException：出錯時拋出ServiceException
	 * @return SessionContext: 上下文SessionContext
	 */
	public SessionContext initNextYear(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose: 修改年度行事曆的初始化
	 * @author echomou
	 * @param sessionContext：上下文SessionContext
	 * @throws ServiceException：出錯時拋出ServiceException
	 * @return SessionContext: 上下文SessionContext
	 */
	public SessionContext initYearDetail(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose: 修改行事歷頁面的初始化
	 * @author echomou
	 * @param sessionContext：上下文SessionContext
	 * @throws ServiceException：出錯時拋出ServiceException
	 * @return SessionContext：上下文SessionContext
	 */
	public SessionContext initDateDetail(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose: 保存“年度行事曆”
	 * @author echomou
	 * @param sessionContext：上下文SessionContext
	 * @throws ServiceException：出錯時拋出ServiceException
	 * @return SessionContext：上下文SessionContext
	 */
	public SessionContext saveCalendarYear(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose: 保存“行事曆”
	 * @author echomou
	 * @param sessionContext：上下文SessionContext
	 * @throws ServiceException：出錯時拋出ServiceException
	 * @return SessionContext：上下文SessionContext
	 */
	public SessionContext saveCalendarDate(SessionContext sessionContext) throws ServiceException;
}
