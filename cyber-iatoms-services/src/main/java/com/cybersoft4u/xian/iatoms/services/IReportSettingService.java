package com.cybersoft4u.xian.iatoms.services;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;
/**
 * Purpose: 報表發送功能設定Service interface
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2016年7月4日
 * @MaintenancePersonnel ElvaHe
 */
@SuppressWarnings({ "rawtypes" })
public interface IReportSettingService extends IAtomicService{
	
	/**
	 * Purpose:初始化頁面信息
	 * @author ElvaHe
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext init(SessionContext sessionContext)throws ServiceException;
	
	/**
	 * Purpose:修改初始化頁面信息
	 * @author ElvaHe
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext initEdit(SessionContext sessionContext)throws ServiceException;
	
	/**
	 * Purpose:保存報表發送功能設定信息
	 * @author ElvaHe
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext save(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:查詢報表發送功能設定信息
	 * @author ElvaHe
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:刪除報表發送功能設定信息
	 * @author ElvaHe
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext delete(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:獲取在相同客戶編號下報表編號集合
	 * @author ElvaHe
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public List<Parameter> getPreReportCodeList(MultiParameterInquiryContext param)throws ServiceException; 
	
	/**
	 * Purpose:重送選中的信息
	 * @author ElvaHe
	 * @param sessionContext 上下文SessionContext
	 * @throws ServiceException 出錯時拋出ServiceException
	 * @return SessionContext 上下文SessionContext
	 */
	public SessionContext send(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:初始化重送畫面
	 * @author ElvaHe
	 * @param sessionContext：上下文SessionContext
	 * @throws ServiceException：上下文SessionContext
	 * @return SessionContext：上下文SessionContext
	 */
	public SessionContext initSend(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:根據報表編號獲取選擇的明細信息
	 * @author ElvaHe
	 * @param param：傳遞的參數
	 * @throws ServiceException 出錯時拋出ServiceException
	 * @return List<String> 明細列表
	 */
	public List<String> getReportDetailListById(MultiParameterInquiryContext param) throws ServiceException;
	
}
