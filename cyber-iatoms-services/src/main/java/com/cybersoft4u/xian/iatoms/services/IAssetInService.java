
package com.cybersoft4u.xian.iatoms.services;

import java.util.List;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetInInfoDTO;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;
/**
 * 
 * Purpose: 設備入庫Service接口
 * @author CarrieDuan
 * @since  JDK 1.6
 * @date   2016/5/12
 * @MaintenancePersonnel CarrieDuan
 */
public interface IAssetInService extends IAtomicService {
	
	/**
	 * 
	 * Purpose: 設備入庫初始化
	 * @author hungli
	 * @param sessionContext:上下文sessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * 
	 * Purpose:歷史入庫查詢
	 * @author hungli
	 * @param sessionContext:上下文SesionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SesionContext
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException;
	/**
	 * 
	 * Purpose:查詢待入庫清單
	 * @author hungli
	 * @param sessionContext:上下文SesionContext
	 * @return
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SesionContext
	 */
	public SessionContext listAssetInList(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * 
	 * Purpose: 獲取入庫批號集合
	 * @author hungli
	 * @param inquiryContext:ajax上下文inquiryContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>:入庫批號集合
	 */
	public List<Parameter> getAssetInIdList(MultiParameterInquiryContext inquiryContext) throws ServiceException;

	
	/**
	 * 
	 * Purpose:保存設備入庫主檔
	 * @author hungli
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException 
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext save(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * 
	 * Purpose:刪除設備入庫主檔
	 * @author hungli
	 * @param sessionContext：上下文SessionContext
	 * @throws ServiceException：出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext delete(SessionContext sessionContext) throws ServiceException;
	/**
	 * 
	 * Purpose:新增入庫明細檔資料
	 * @author hungli
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext addAssetInList(SessionContext sessionContext) throws ServiceException;
	/**
	 * 
	 * Purpose:刪除入庫明細檔資料資料
	 * @author hungli
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionConytext
	 */
	public SessionContext deleteAssetInList(SessionContext sessionContext) throws ServiceException;
	/**
	 * 
	 * Purpose:實際驗收
	 * @author hungli
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext actualAcceptance(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * 
	 * Purpose:客戶驗收
	 * @author hungli
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext customesAcceptance(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * 
	 * Purpose:驗收完成
	 * @author hungli
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext finishAcceptance(SessionContext sessionContext) throws ServiceException;
	/**
	 * 
	 * Purpose:入庫
	 * @author hungli
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext storage(SessionContext sessionContext) throws ServiceException;
	/**
	 * 
	 * Purpose:根據入庫批號獲取入庫信息
	 * @author hungli
	 * @param inquiryContext:ajax上下文inquiryContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return AssetInInfoDTO：設備入庫主檔DTO
	 */
	public AssetInInfoDTO getAssetInfoDTOByAssetInId(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	/**
	 * 
	 * Purpose:匯入入庫清單
	 * @author hungli
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceExcetion
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext upload(SessionContext sessionContext)throws ServiceException;
}
