package com.cybersoft4u.xian.iatoms.services;

import java.rmi.ServerException;
import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;
/**
 * Purpose: 耗材品維護的service接口
 * @author Hermanwang
 * @since  JDK 1.6
 * @date   2016/8/25
 * @MaintenancePersonnel Hermanwang
 */
public interface ISuppliesTypeService extends IAtomicService{
	
	/**
	 * Purpose:耗材品維護的初始化頁面方法
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServerException：出錯時, 丟出ServerException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext init(SessionContext sessionContext) throws ServerException;
	/**
	 * Purpose:耗材品的查詢
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException：出錯時, 丟出ServerException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException;

	/**
	 * Purpose:耗材品的存儲（增加和修改）
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException：出錯時, 丟出ServerException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext save(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:耗材品的刪除
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException：出錯時, 丟出ServerException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext delete(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose: 取得【耗材品項維護】該客戶的耗材資料
	 * @author CarrieDuan
	 * @param param ：參數
	 * @throws ServiceException：出錯時, 丟出ServerException
	 * @return List<Parameter>：耗材名稱列表
	 */
	public List<Parameter> getSuppliesTypeNameList (MultiParameterInquiryContext param) throws ServiceException;
	/**
	 * Purpose:取得【耗材品項維護】該客戶的耗材分類
	 * @author HermanWang
	 * @param param：參數
	 * @throws ServiceException：出錯時, 丟出ServerException
	 * @return List<Parameter>：耗材分類列表
	 */
	public List<Parameter> getSuppliesListByCustomseId (MultiParameterInquiryContext param) throws ServiceException;
	/**
	 * Purpose::取得【耗材品項維護】該客戶和耗材分類的耗材名稱
	 * @author HermanWang
	 * @param param：參數
	 * @throws ServiceException：出錯時, 丟出ServerException
	 * @return List<Parameter>：耗材分類列表
	 */
	public List<Parameter> getSuppliesNameList (MultiParameterInquiryContext param) throws ServiceException;
}
