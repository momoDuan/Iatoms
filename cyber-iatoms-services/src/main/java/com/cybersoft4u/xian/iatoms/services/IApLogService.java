package com.cybersoft4u.xian.iatoms.services;

import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

/**
 * 
 * Purpose: 系统日志记录 
 * @author ericdu
 * @since  JDK 1.7
 * @date   2016年7月5日
 * @MaintenancePersonnel ericdu
 */
@SuppressWarnings("rawtypes")
public interface IApLogService extends IAtomicService {
	
	/**
	 * 
	 * Purpose: 系統日志記錄
	 * @author ericdu
	 * @param sessionContext 上下文Context
	 * @throws ServiceException service異常類
	 * @return void 
	 */
	public void log(SessionContext sessionContext) throws ServiceException;
}
