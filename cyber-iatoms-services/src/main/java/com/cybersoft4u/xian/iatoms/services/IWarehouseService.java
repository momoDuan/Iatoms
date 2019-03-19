package com.cybersoft4u.xian.iatoms.services;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

/**
 * Purpose: 倉庫維護service接口
 * @author HaimingWang
 * @since JDK 1.6
 * @date 2016/6/1
 * @MaintenancePersonnel HaimingWang
 */
public interface IWarehouseService extends IAtomicService {
	/**
	 * Purpose:初始化頁面
	 * @author ElvaHe
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:查詢
	 * @author HaimingWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException;

	/**
	 * Purpose:初始化修改頁面
	 * @author HaimingWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext initEdit(SessionContext sessionContext) throws ServiceException;

	/**
	 * Purpose:新增修改的保存
	 * @author HaimingWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext save(SessionContext sessionContext) throws ServiceException;

	/**
	 * Purpose:刪除
	 * @author HaimingWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext delete(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:得到所有仓库据点下拉框
	 * @author CrissZhang
	 * @throws ServiceException:出錯拋出ServiceException
	 * @return List<Parameter>:仓库据点下拉框
	 */
	public List<Parameter> getWarehouseList() throws ServiceException;
	
	/**
	 * Purpose:根据用户编号得到所有仓库据点信息
	 * @author CrissZhang
	 * @param userId : 用戶編號
	 * @throws ServiceException:出錯拋出ServiceException
	 * @return List<Parameter>:仓库据点下拉框
	 */
	public List<Parameter> getWarehouseByUserId(String userId) throws ServiceException;
}
