package com.cybersoft4u.xian.iatoms.services;

import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

/**
 * Purpose: 密碼原則設定Service interface
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2016/5/20
 * @MaintenancePersonnel HermanWang
 */
@SuppressWarnings("rawtypes")
public interface IPasswordSettingService extends IAtomicService{

	/**
	 * Purpose:初始化頁面信息
	 * @author HermanWang
	 * @param sessionContext:SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext：上下文SessionContext
	 */
	public SessionContext init(SessionContext sessionContext)throws ServiceException;

	/**
	 * Purpose:保存密碼原則設定信息
	 * @author HermanWang
	 * @param sessionContext：SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext：上下文SessionContext
	 */
	public SessionContext saveSetting(SessionContext sessionContext) throws ServiceException;
}
