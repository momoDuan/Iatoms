package com.cybersoft4u.xian.iatoms.services;

import java.util.List;
import java.util.Map;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;
/**
 * Purpose:用戶欄位模板維護檔Service接口 
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016/6/29
 * @MaintenancePersonnel CrissZhang
 */
public interface ISrmQueryTemplateService extends IAtomicService{

	/**
	 * Purpose:初始化頁面
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:刪除用戶欄位模板維護檔
	 * @author CrissZhang
	 * @param  SessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext delete(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:保存用戶欄位模板維護檔
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext saveTemplate(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * 
	 * Purpose:得到用戶模板集合
	 * @author CrissZhang
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return List<Parameter>:下拉框集合
	 */
	public List<Parameter> getUserColumnTemplateList(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	
	/**
	 * Purpose:得到用戶模板
	 * @author CrissZhang
	 * @param inquiryContext : ajax傳入參數
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return String ： 返回用戶模板
	 */
	public Map getUserColumnTemplate(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	
	/**
	 * Purpose:得到用戶當前模板
	 * @author CrissZhang
	 * @param inquiryContext : ajax傳入參數
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return Parameter : 返回用戶當前模板
	 */
	public Parameter getCurrentColumnTemplate(MultiParameterInquiryContext inquiryContext) throws ServiceException;
}
