package com.cybersoft4u.xian.iatoms.services;

import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

/**
 * Purpose:錯誤記錄檔下載Service interface
 * @author NickLin 
 * @since  JDK 1.7
 * @date   2018/07/27
 * @MaintenancePersonnel CyberSoft
 */
public interface ILogFileDownloadService extends IAtomicService {
	
	/**
	 * Purpose:初始化頁面
	 * @author NickLin
	 * @param  sessionContext:上下文SessionContext
	 * @throws ServiceException:錯誤時抛出的service例外
	 * @return SessionContext:上下文SessionContext
	 */
	@SuppressWarnings("rawtypes")
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
}
