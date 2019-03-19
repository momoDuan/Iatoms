package com.cybersoft4u.xian.iatoms.services;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

/**
 * Purpose: 部門維護Service接口 
 * @author Amanda Wang
 * @since JDK 1.6
 * @date 2016年6月1日
 * @MaintenancePersonnel Amanda Wang
 */
public interface IDepartmentService extends IAtomicService {
	
	/**
	 * Purpose:初始化頁面信息
	 * @author barryzhang
	 * @param sessionContext：上下文SessionContext
	 * @throws ServiceException：出錯時，丟出ServiceException	
	 * @return SessionContext：上下文SessionContext
	 */
	public SessionContext init(SessionContext sessionContext)throws ServiceException;

	/**
	 * Purpose:刪除部門
	 * @author amandawang
	 * @param  SessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	@SuppressWarnings("rawtypes")
	public SessionContext delete(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:查詢部門
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	@SuppressWarnings("rawtypes")
	public SessionContext query(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:初始化編輯頁面
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	@SuppressWarnings("rawtypes")
	public SessionContext initEdit(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:保存部門
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	@SuppressWarnings("rawtypes")
	public SessionContext save(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose: 依據條件獲得部門列表
	 * @author CrissZhang
	 * @param inquiryContext ：ajax傳入參數
	 * @throws ServiceException : 出錯時，丟出ServiceException
	 * @return List<Parameter> ：返回部門集合
	 */
	public List<Parameter> getDeptList(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	
	/**
	 * Purpose: 依據公司等信息獲得部門列表
	 * @author CrissZhang
	 * @param inquiryContext ：ajax傳入參數
	 * @throws ServiceException : 出錯時，丟出ServiceException
	 * @return List<Parameter> ：返回部門集合
	 */
	public List<Parameter> getDeptByCompany(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	/**
	 * Purpose:獲取部門
	 * @author amandawang
	 * @param sessionContext : 上下文SessionContext
	 * @throws ServiceException : 出錯時，丟出ServiceException
	 * @return SessionContext ：上下文SessionContext
	 */
	public List<Parameter> getDepartmentList(MultiParameterInquiryContext inquiryContext) throws ServiceException;

}
