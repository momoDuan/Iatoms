package com.cybersoft4u.xian.iatoms.services;


import java.rmi.ServerException;
import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

import com.cybersoft4u.xian.iatoms.common.bean.dto.MerchantDTO;

/**
 * Purpose: 客戶特店維護Service interface
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2016/6/16
 * @MaintenancePersonnel DavidZheng
 */
public interface IMerchantService extends IAtomicService{
	/**
	 * Purpose:耗材品維護的初始化頁面方法
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServerException：出錯時, 丟出ServerException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:查詢
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	@SuppressWarnings("rawtypes")
	public SessionContext query(SessionContext sessionContext) throws ServiceException;

	/**
	 * Purpose:修改時頁面會顯的資料
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	@SuppressWarnings("rawtypes")
	public SessionContext initEdit(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:保存客戶特店
	 * @author HermanWang
	 * @param sessionContext：上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext：上下文SessionContext
	 */
	public SessionContext save(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:刪除一筆客戶特店
	 * @author HermanWang
	 * @param sessionContext：上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext：上下文SessionContext
	 */
	public SessionContext delete(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:判斷特店是否有表頭(有表頭就不可以刪除)
	 * @author HermanWang
	 * @param param：ajax請求傳遞的參數
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return Boolean:返回boolean類型
	 */
	public Boolean getMerchantList(MultiParameterInquiryContext param) throws ServiceException;
	
	/**
	 * Purpose:客戶特店的匯入
	 * @author HermanWang
	 * @param sessionContext：上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext：上下文SessionContext
	 */
	public SessionContext upload(SessionContext sessionContext)throws ServiceException;
	
	/**
	 * Purpose: 初始化MID查詢頁面--案件處理
	 * @author CarrieDuan
	 * @param sessionContext：上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext：上下文SessionContext
	 */
	public SessionContext initMid(SessionContext sessionContext)throws ServiceException;
	
	
	/**
	 * Purpose:根據ID獲取特點信息--案件處理
	 * @author CarrieDuan
	 * @param param ：參數
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return MerchantDTO：特店DTO
	 */
	public MerchantDTO getMerchantDTOBy(MultiParameterInquiryContext param) throws ServiceException;
	
	/**
	 * Purpose: 根據特店代號與客戶ID獲取特店信息
	 * @author NickLin
	 * @param merchantCode：特店代號
	 * @param customerId : 客戶ID
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>：特店信息
	 */
	public List<Parameter> getMerchantsByCodeAndCompamyId(MultiParameterInquiryContext param) throws ServiceException;
	/**
	 * Purpose: 查詢MID--案件處理
	 * @author CarrieDuan
	 * @param sessionContext：上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext：上下文SessionContext
	 */
	public SessionContext queryMid(SessionContext sessionContext)throws ServiceException;
	/**
	 * Purpose:新增時頁面會顯的資料
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext initAdd(SessionContext sessionContext) throws ServiceException;
}
