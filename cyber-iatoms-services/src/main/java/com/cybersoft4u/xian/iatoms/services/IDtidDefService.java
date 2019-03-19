package com.cybersoft4u.xian.iatoms.services;

import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

/**
 * Purpose: DTIO帳號管理service接口
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2016/9/21
 * @MaintenancePersonnel CarrieDuan
 */
public interface IDtidDefService extends IAtomicService{
	
	/**
	 * Purpose:初始化頁面
	 * @author CarrieDuan
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時抛出出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException;

	/**
	 * Purpose: 查詢DTID列表
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException:出錯時抛出出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose: 初始化修改頁面
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException:出錯時抛出出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext initEdit(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose: 初始化新增頁面
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException:出錯時抛出出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext initAdd(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose: 保存DTID信息
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException:出錯時抛出出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext save(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose: 刪除DTID信息
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException:出錯時抛出出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext delete(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:核檢要刪除的DTID是否已被使用
	 * @author CarrieDuan
	 * @param param ： 參數
	 * @throws ServiceException：:出錯時抛出出ServiceException
	 * @return Boolean：是否被使用
	 */
	public Boolean isUseDtid (MultiParameterInquiryContext param) throws ServiceException;
}
