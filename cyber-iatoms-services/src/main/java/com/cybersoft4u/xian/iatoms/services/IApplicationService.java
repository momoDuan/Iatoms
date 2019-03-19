package com.cybersoft4u.xian.iatoms.services;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

/**
 * Purpose: 程式版本維護Service interface
 * @author echomou
 * @since  JDK 1.6
 * @date   2016-7-14
 * @MaintenancePersonnel echomou
 */
@SuppressWarnings("rawtypes")
public interface IApplicationService extends IAtomicService {
	
	/**
	 * Purpose:初始化頁面信息
	 * @author echomou
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext init(SessionContext sessionContext)throws ServiceException;
	/**
	 * Purpose:修改初始化頁面信息
	 * @author echomou
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext initEdit(SessionContext sessionContext)throws ServiceException;
	
	/**
	 * Purpose:保存程式版本信息
	 * @author echomou
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext save(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:查詢程式版本信息
	 * @author echomou
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:刪除程式版本信息
	 * @author echomou
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext delete(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:獲取設備列表
	 * @author echomou
	 * @param sessionContext：SessionContext
	 * @throws ServiceException：出錯時拋出ServiceException
	 * @return SessionContext：SessionContext
	 */
	public SessionContext getAssetTypeList(SessionContext sessionContext)throws ServiceException;
	
	/**
	 * Purpose:獲取軟件版本列表
	 * @author CarrieDuan
	 * @param parameterInquiryContext：參數 
	 * @throws ServiceException：出錯時拋出ServiceException
	 * @return List<Parameter>：軟件版本列表
	 */
	public List<Parameter> getSoftwareVersions(MultiParameterInquiryContext parameterInquiryContext) throws ServiceException;
}
