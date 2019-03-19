package com.cybersoft4u.xian.iatoms.services;

import java.util.List;

import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentItemDTO;
/**
 * Purpose: 求償作業Service
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/2/7
 * @MaintenancePersonnel CarrieDuan
 */
public interface IPaymentService extends IAtomicService{
	
	/**
	 * Purpose:初始化頁面
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException :出錯時抛出出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext init (SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:初始化修改頁面
	 * @author CarrieDuan
	 * @param sessionContext
	 * @throws ServiceException
	 * @return SessionContext
	 */
	public SessionContext initEdit (SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:查詢
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException :出錯時抛出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext query (SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose: 保存
	 * @author CarrieDuan
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException :出錯時抛出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext save (SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose: 獲取求償歷程數據
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException :出錯時抛出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext queryPaymentTranscation (SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose: 查詢DTID --點擊放大鏡查詢
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException :出錯時抛出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext queryDTID (SessionContext sessionContext) throws SecurityException;
	
	/**
	 * Purpose: 選擇DTID之後。獲取對應的信息用於新增頁面顯示
	 * @author CarrieDuan
	 * @param param:參數
	 * @throws ServiceException :出錯時抛出ServiceException
	 * @return SrmCaseHandleInfoDTO :求償資料DTO
	 */
	public SrmCaseHandleInfoDTO getPayInfo (MultiParameterInquiryContext param) throws SecurityException;
	
	/**
	 * Purpose: 初始化查詢DTID頁面
	 * @author CarrieDuan
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException :出錯時抛出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext initView (SessionContext sessionContext) throws SecurityException;
	/**
	 * Purpose: 初始化耗材清單數據
	 * @author CarrieDuan
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException :出錯時抛出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext initSuppliesValues (SessionContext sessionContext) throws SecurityException;
	/**
	 * Purpose: 送出操作
	 * @author CarrieDuan
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException :出錯時抛出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext send (SessionContext sessionContext) throws SecurityException;
	/**
	 * Purpose:鎖定操作
	 * @author CarrieDuan
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException :出錯時抛出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext lock (SessionContext sessionContext) throws SecurityException;
	/**
	 * Purpose:退回操作
	 * @author CarrieDuan
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException :出錯時抛出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext back (SessionContext sessionContext) throws SecurityException;
	/**
	 * Purpose:完成操作
	 * @author CarrieDuan
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException :出錯時抛出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext complete (SessionContext sessionContext) throws SecurityException;
	/**
	 * Purpose:還款操作
	 * @author CarrieDuan
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException :出錯時抛出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext repayment (SessionContext sessionContext) throws SecurityException;
	
	/**
	 * Purpose:送出等操作時根據求償編號獲取求償列表信息
	 * @author CarrieDuan
	 * @param param :參數
	 * @throws SecurityException:出錯時抛出ServiceException
	 * @return SrmPaymentItemDTO：求償資料列表
	 */
	public List<SrmPaymentItemDTO> getPaymentItemByItemIds (MultiParameterInquiryContext param) throws SecurityException;
	/**
	 * Purpose:刪除求償信息
	 * @author CarrieDuan
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException :出錯時抛出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext delete (SessionContext sessionContext) throws SecurityException;
	
	/**
	 * Purpose: export
	 * @author CarrieDuan
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext export(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:求償作業-修改資料驗證是否已被修改。
	 * @author CarrieDuan
	 * @param paymentId：求償資料ID
	 * @param updatedDate：頁面資料異動時間
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return Boolean：是否進行了其他操作
	 */
	public Boolean initEditCheckUpdate(MultiParameterInquiryContext param)throws ServiceException;
	
	/**
	 * Purpose:CMS通知IATOMS進行完成操作
	 * @author CarrieDuan
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext changePaymentCompleteByApi(SessionContext sessionContext) throws ServiceException;
}
