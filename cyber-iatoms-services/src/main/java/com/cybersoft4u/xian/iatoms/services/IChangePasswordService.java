package com.cybersoft4u.xian.iatoms.services;

import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

/**
 * Purpose:密碼修改service interface 
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2016年7月6日
 * @MaintenancePersonnel HermanWang
 */
public interface IChangePasswordService extends IAtomicService {
	/**
	 * Purpose:初始化頁面
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose: save
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext save(SessionContext sessionContext) throws ServiceException;
}
