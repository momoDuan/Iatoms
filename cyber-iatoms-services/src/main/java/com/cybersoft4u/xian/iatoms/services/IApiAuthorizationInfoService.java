package com.cybersoft4u.xian.iatoms.services;

import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

public interface IApiAuthorizationInfoService extends IAtomicService {

	/**
	 * 
	 * Purpose: 初始化方法
	 * @author amandawang
	 * @param sessionContext 上下文Context
	 * @throws ServiceException Service異常類
	 * @return SessionContext 上下文Context
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	/**
	 * 
	 * Purpose: 檢核id是否已存在
	 * @author amandawang
	 * @param sessionContext 上下文Context
	 * @throws ServiceException Service異常類
	 * @return SessionContext 上下文Context
	 */
	public SessionContext checkId(SessionContext sessionContext) throws ServiceException;
	/**
	 * 
	 * Purpose: 更新授權信息
	 * @author amandawang
	 * @param sessionContext 上下文Context
	 * @throws ServiceException Service異常類
	 * @return SessionContext 上下文Context
	 */
	public SessionContext updateApiAuthorizationInfo(SessionContext sessionContext) throws ServiceException;
	/**
	 * 
	 * Purpose: 保存log
	 * @author amandawang
	 * @param sessionContext 上下文Context
	 * @throws ServiceException Service異常類
	 * @return SessionContext 上下文Context
	 */
	public SessionContext saveLog(SessionContext sessionContext)throws ServiceException;
	
	/**
	 * Purpose:核檢ip是否存在以及可用
	 * @author CarrieDuan
	 * @param sessionContext：上下文Context
	 * @throws ServiceException：Service異常類
	 * @return SessionContext：上下文Context
	 */
	public SessionContext checkIp(SessionContext sessionContext)throws ServiceException;
}
