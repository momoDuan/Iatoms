package com.cybersoft4u.xian.iatoms.services;


import java.util.List;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BimMerchantHeaderDTO;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

/**
 * Purpose: 特店表頭維護Service interface
 * @author echomou
 * @since  JDK 1.6
 * @date   2016/6/8
 * @MaintenancePersonnel jasonzhou
 */
@SuppressWarnings({ "rawtypes" })
public interface IMerchantHeaderService extends IAtomicService{
	/**
	 * Purpose:修改初始化頁面信息
	 * @author echomou
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext init(SessionContext sessionContext)throws ServiceException;
	/**
	 * Purpose:修改初始化頁面信息
	 * @author jasonzhou
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext initEdit(SessionContext sessionContext)throws ServiceException;
	
	/**
	 * Purpose:新增初始化頁面信息
	 * @author jasonzhou
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext initAdd(SessionContext sessionContext)throws ServiceException;
	
	/**
	 * Purpose:保存特店表頭信息
	 * @author jasonzhou
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext save(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:查詢特店表頭信息
	 * @author jasonzhou
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:刪除特店表頭信息
	 * @author jasonzhou
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext delete(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:檢核特點編號是否存在並獲取客戶特店信息
	 * @author jasonzhou
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext getMerchantInfo(SessionContext sessionContext)throws ServiceException;
	
	/**
	 * Purpose:根據表頭ID獲取信息--案件處理
	 * @author CarrieDuan
	 * @param param:參數
	 * @throws ServiceException：出錯時拋出ServiceException
	 * @return BimMerchantHeaderDTO：特店表頭DTO
	 */
	public BimMerchantHeaderDTO getMerchantHeaderById(MultiParameterInquiryContext param) throws ServiceException;
	
	/**
	 * Purpose:獲取表頭下拉框--案件處理
	 * @author CarrieDuan
	 * @param param:參數
	 * @throws ServiceException：出錯時拋出ServiceException
	 * @return List<Parameter>：表頭集合
	 */
	public List<Parameter> getMerchantHeaderList(MultiParameterInquiryContext param) throws ServiceException;
	/**
	 * Purpose: 查詢MID--案件處理
	 * @author Carrie Duan
	 * @param sessionContext：上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext：上下文SessionContext
	 */
	public SessionContext queryMid(SessionContext sessionContext)throws ServiceException;
	
	/**
	 * Purpose:獲取郵遞區號下拉框--案件
	 * @author CarrieDuan
	 * @param sessionContext：上下文SessionContext
	 * @throws ServiceException：出錯時拋出ServiceException
	 * @return List<Parameter>：表頭集合
	 */
	public List<Parameter> getPostCodeList(MultiParameterInquiryContext param)throws ServiceException;
	
	/**
	 * Purpose:根據特店代號以及公司id獲取表頭信息
	 * @author CarrieDuan
	 * @param param:參數
	 * @throws ServiceException：出錯時拋出ServiceException
	 * @return BimMerchantHeaderDTO：特店表頭DTO
	 */
	public BimMerchantHeaderDTO getHeaderDTOByMerchantCode(MultiParameterInquiryContext param)throws ServiceException;
	
}
