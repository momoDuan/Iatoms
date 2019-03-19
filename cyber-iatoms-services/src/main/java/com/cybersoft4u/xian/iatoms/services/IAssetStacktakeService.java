package com.cybersoft4u.xian.iatoms.services;

import java.util.List;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetStacktakeListDTO;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

/**
 * 
 * Purpose: 設備盤點service interface
 * @author allenchen
 * @since  JDK 1.7
 * @date   2016-7-15
 * @MaintenancePersonnel allenchen
 */
@SuppressWarnings("rawtypes")
public interface IAssetStacktakeService extends IAtomicService {
	
	/**
	 * Purpose:初始化頁面
	 * @author allenchen
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時抛出出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:新增盤點批號，自動生成
	 * @author allenchen
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時抛出出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext initAdd(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:儲存盤點批號
	 * @author allenchen
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時抛出出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext save(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:刪除設備盤點批號批號
	 * @author allenchen
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時抛出出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext delete(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:根據設備盤點批號查詢待盤點設備的明細
	 * @author allenchen
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時抛出出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext list(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:根據設備盤點批號查詢待盤點設備清冊數據
	 * @author allenchen
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時抛出出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:盤點完成
	 * @author allenchen
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時抛出出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext complete(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:單個設備盤點完成
	 * @author allenchen
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時抛出出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext send(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:儲存說明
	 * @author allenchen
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時抛出出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext saveRemark(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:抓取盤點批號集合
	 * @author allenchen
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>:設備盤點批號集合
	 */
	public List<Parameter> getInventoryNumberList() throws ServiceException;
	/**
	 * Purpose:獲取盤點批號list
	 * @author amandawang
	 * @param inquiryContext：上下文inquiryContext
	 * @throws ServiceException：出错是抛出：ServiceException
	 * @return List<Parameter>:盤點批號List集合
	 */
	public List<Parameter> getStocktackIdList(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	/**
	 * 
	 * Purpose:根據盤點批號ID查詢設備盤點明細
	 * @author allenchen
	 * @param inquiryContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return AssetStocktackInfoDTO
	 */
	public DmmAssetStacktakeListDTO getAssetStocktackInfoByStocktackId(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	/**
	 * 
	 * Purpose:匯出盤點清冊報表
	 * @author allecnhen
	 * @param sessionContext:上下文sessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 */
	public SessionContext exportInventory(SessionContext sessionContext) throws ServiceException;
	/**
	 * 
	 * Purpose:匯出盤點結果報表
	 * @author allecnhen
	 * @param sessionContext:上下文sessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 */
	public SessionContext exportSummary(SessionContext sessionContext) throws ServiceException;
}
