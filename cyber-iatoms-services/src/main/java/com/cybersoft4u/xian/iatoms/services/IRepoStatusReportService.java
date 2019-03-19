package com.cybersoft4u.xian.iatoms.services;

import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

/**
 * Purpose: 設備狀態報表Service接口
 * @author barryzhang
 * @since  JDK 1.7
 * @date   2016年7月22日
 * @MaintenancePersonnel barryzhang
 */
@SuppressWarnings("rawtypes")
public interface IRepoStatusReportService extends IAtomicService {

	/**
	 * 
	 * Purpose: 初始化頁面
	 * @author barryzhang
	 * @param sessionContext 上下文Context
	 * @throws ServiceException service異常類
	 * @return SessionContext 上下文Context
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * 
	 * Purpose: 查詢
	 * @author barryzhang
	 * @param sessionContext 上下文Context
	 * @throws ServiceException service異常類
	 * @return SessionContext 上下文Context
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * 
	 * Purpose: 匯出資料查詢
	 * @author barryzhang
	 * @param sessionContext 上下文Context
	 * @throws ServiceException service異常類
	 * @return SessionContext 上下文Context
	 */
	public SessionContext export(SessionContext sessionContext) throws ServiceException;
	
}
