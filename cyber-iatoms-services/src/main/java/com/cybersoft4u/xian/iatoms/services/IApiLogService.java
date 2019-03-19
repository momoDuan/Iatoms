package com.cybersoft4u.xian.iatoms.services;

import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

/**
 * Purpose:電文記錄查詢
 * @author NickLin
 * @since  JDK 1.7
 * @date   2018/06/12
 * @MaintenancePersonnel CyberSoft
 */
public interface IApiLogService extends IAtomicService {

	/**
	 * Purpose:初始化頁面
	 * @author NickLin
	 * @param  sessionContext 上下文Context
	 * @throws ServiceException 錯誤時抛出的service例外
	 * @return SessionContext 上下文Context
	 */
	@SuppressWarnings("rawtypes")
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:查詢電文紀錄
	 * @author NickLin
	 * @param  sessionContext 上下文Context
	 * @throws ServiceException 錯誤時抛出的service例外
	 * @return SessionContext 上下文Context
	 */
	@SuppressWarnings("rawtypes")
	public SessionContext query(SessionContext sessionContext) throws ServiceException;
}
