package com.cybersoft4u.xian.iatoms.services;

import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

/**
 * 
 * Purpose: 財產批號匯入service interface
 * @author CarrieDuan	
 * @since  JDK 1.7
 * @date   2016-7-7
 * @MaintenancePersonnel CarrieDuan
 */
public interface IPropertyImportService extends IAtomicService {
	
	/**
	 * Purpose:初始化頁面
	 * @author CarrieDuan
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時抛出出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	@SuppressWarnings("rawtypes")
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:檢查格式是否正確
	 * @author CarrieDuan
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出serviceException
	 * @return SessionContext:上下文SessionContext
	 */
	@SuppressWarnings("rawtypes")
	public SessionContext checkAssetData(SessionContext sessionContext) throws ServiceException;
	/**
	 * 
	 * Purpose:上傳文件
	 * @author CarrieDuan
	 * @param sessionContext:上下文sessionContext
	 * @return
	 * @throws ServiceException:出錯時跑出ServiceContext
	 * @return SessionContext
	 */
	@SuppressWarnings("rawtypes")
	public SessionContext upload(SessionContext sessionContext) throws ServiceException;
}
