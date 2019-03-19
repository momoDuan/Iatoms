package com.cybersoft4u.xian.iatoms.services;

import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;
/**
 * Purpose:合約SLA設定Service接口 
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016/6/29
 * @MaintenancePersonnel CrissZhang
 */
public interface IContractSlaService extends IAtomicService{

	/**
	 * Purpose:初始化頁面
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:刪除合約SLA
	 * @author CrissZhang
	 * @param  SessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext delete(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:查詢合約SLA
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:初始化編輯頁面
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext initEdit(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:保存合約SLA
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext save(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:複製
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext copy(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:匯入一笔或多笔sla信息
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException：出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext upload(SessionContext sessionContext) throws ServiceException;
	
}
