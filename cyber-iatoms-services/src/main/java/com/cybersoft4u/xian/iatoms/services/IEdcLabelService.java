package com.cybersoft4u.xian.iatoms.services;

import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

/**
 * Purpose:刷卡機標籤管理Service
 * @author NickLin 
 * @since  JDK 1.7
 * @date   2018/06/27
 * @MaintenancePersonnel CyberSoft
 */
public interface IEdcLabelService extends IAtomicService {

	/**
	 * Purpose:初始化頁面
	 * @author NickLin
	 * @param  sessionContext:上下文SessionContext
	 * @throws ServiceException:錯誤時抛出的service例外
	 * @return SessionContext:上下文SessionContext
	 */
	@SuppressWarnings("rawtypes")
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:查詢標籤
	 * @author NickLin
	 * @param  sessionContext:上下文SessionContext
	 * @throws ServiceException:錯誤時抛出的service例外
	 * @return SessionContext:上下文SessionContext
	 */
	@SuppressWarnings("rawtypes")
	public SessionContext query(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:刷卡機標籤匯入
	 * @author NickLin
	 * @param  sessionContext:上下文SessionContext
	 * @throws ServiceException:錯誤時抛出的service例外
	 * @return SessionContext:上下文SessionContext
	 */
	@SuppressWarnings("rawtypes")
	public SessionContext upload(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:刪除刷卡機標籤
	 * @author NickLin
	 * @param  sessionContext:上下文SessionContext
	 * @throws ServiceException:錯誤時抛出的service例外
	 * @return SessionContext:上下文SessionContext
	 */
	@SuppressWarnings("rawtypes")
	public SessionContext delete(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:列印刷卡機標籤
	 * @author NickLin
	 * @param  sessionContext:上下文SessionContext
	 * @throws ServiceException:錯誤時抛出的service例外
	 * @return SessionContext:上下文SessionContext
	 */
	@SuppressWarnings("rawtypes")
	public SessionContext printEdcLabel(SessionContext sessionContext) throws ServiceException;
}
