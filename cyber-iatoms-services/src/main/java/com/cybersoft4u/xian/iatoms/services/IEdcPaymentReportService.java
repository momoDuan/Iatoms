package com.cybersoft4u.xian.iatoms.services;

import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;
/**
 * Purpose: EDC維護費用付款報表Service interface
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2017/6/16
 * @MaintenancePersonnel HermanWang
 */
public interface IEdcPaymentReportService  extends IAtomicService{
	/**
	 * Purpose:EDC維護費用付款報表的初始化頁面方法
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException：出錯時, 丟出ServerException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:查詢裝機
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext queryInstall(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:查詢拆機
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext queryUnInstall(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:查詢其他案件
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext queryOther(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:查詢查核案件
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext queryCheck(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:匯出
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext export(SessionContext sessionContext) throws ServiceException;
	
}
