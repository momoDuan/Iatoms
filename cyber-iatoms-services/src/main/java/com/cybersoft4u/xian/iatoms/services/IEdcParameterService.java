package com.cybersoft4u.xian.iatoms.services;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;
/**
 * 
 * Purpose: EDC交易參數業務邏輯層接口
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016/9/22
 * @MaintenancePersonnel CrissZhang
 */
public interface IEdcParameterService extends IAtomicService{

	/**
	 * Purpose:初始化頁面
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:EDC交易參數查詢
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:export
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext export(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:通過id獲得交易參數信息
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext getTransactionParams(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:得到交易參數的所有交易項
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return List<Parameter>:上下文SessionContext
	 */
	public List<Parameter> getItemList(SessionContext sessionContext) throws ServiceException;
}
