package com.cybersoft4u.xian.iatoms.services;

import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

/**
 * Purpose: 客戶設備總表Service接口
 * @author HermanWang
 * @since  JDK 1.7
 * @date   2016年7月29日
 * @MaintenancePersonnel HermanWang
 */
@SuppressWarnings("rawtypes")
public interface ICustomerRepoService extends IAtomicService {
	
	/**
	 * 
	 * Purpose: 頁面初始化
	 * @author HermanWang
	 * @param sessionContext 上下文Context
	 * @throws ServiceException service異常類
	 * @return SessionContext 上下文Context
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * 
	 * Purpose: 查询
	 * @author HermanWang
	 * @param sessionContext 上下文Context
	 * @throws ServiceException service異常類
	 * @return SessionContext 上下文Context
	 */
	public SessionContext queryData(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * 
	 * Purpose: 匯出
	 * @author HermanWang
	 * @param sessionContext 上下文Context
	 * @throws ServiceException service異常類
	 * @return SessionContext 上下文Context
	 */
	public SessionContext export(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * 
	 * Purpose: 獲取查詢列
	 * @author HermanWang
	 * @param sessionContext 上下文Context
	 * @throws ServiceException service異常類querys
	 * @return SessionContext 上下文Context
	 */
	public SessionContext getAssetNameList(SessionContext sessionContext) throws ServiceException;
}
