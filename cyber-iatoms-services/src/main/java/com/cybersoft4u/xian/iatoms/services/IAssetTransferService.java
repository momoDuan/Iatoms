package com.cybersoft4u.xian.iatoms.services;

import java.util.List;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetTransInfoDTO;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;
/**
 * Purpose: 設備轉倉作業Service接口
 * @author amandawang
 * @since  JDK 1.6
 * @date   2016/7/13
 * @MaintenancePersonnel amandawang
 */
public interface IAssetTransferService extends IAtomicService {
	/**
	 * Purpose:初始化轉倉作業頁面
	 * @author CarrieDuan
	 * @param sessionContext：上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:初始化轉入驗收頁面
	 * @author CarrieDuan
	 * @param sessionContext：上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext initCheck(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:初始化歷史轉倉查詢頁面頁面
	 * @author CarrieDuan
	 * @param sessionContext：上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext initHist(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:初始化轉倉批號list
	 * @author amandawang
	 * @param inquiryContext:參數
	 * @throws ServiceException：出错是抛出：ServiceException
	 * @return List<Parameter>:返回倉庫List集合
	 */
	public List<Parameter> getAssetTransIdList(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	
	/**
	 * Purpose: 查詢--轉入驗收
	 * @author ericdu
	 * @param sessionContext 上下文Context
	 * @throws ServiceException service異常類
	 * @return SessionContext 上下文Context
	 */
	public SessionContext queryTransInfo(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:歷史轉倉查詢
	 * @author starwang
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	//public SessionContext queryHist(SessionContext sessionContext)throws ServiceException;
	
	/**
	 * Purpose:保存設備轉倉單主檔
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	@SuppressWarnings("rawtypes")
	public SessionContext save(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:確認入庫/轉倉退回信息保存
	 * @author CarrieDuan
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext saveTranferCheck(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:查詢待轉倉設備明細
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:設備轉倉單主檔邏輯刪除
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext delete(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:增加設備轉倉明細集合
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext addAssetTransList(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:刪除設備轉倉明細
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext deleteAssetTransList(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:確認轉倉
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext transferWarehouse(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose: 取消轉倉
	 * @author barryzhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext cancleTransferWarehouse(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:文件匯入
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext upload(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:獲取轉倉主檔資料
	 * @author amandawang
	 * @param inquiryContext:ajax上下文inquiryContext:ajax
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return AssetTransInfoDTO:轉倉主檔DTO
	 */
	public DmmAssetTransInfoDTO getAssetTransInfoDTOByAssetTransId(MultiParameterInquiryContext 
			inquiryContext) throws ServiceException;
	
	/**
	 * Purpose:更新轉倉明細
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext update(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:列印出庫單
	 * @author barryzhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext exportAsset(SessionContext sessionContext) throws ServiceException;

	/**
	 * Purpose:得到通知倉管人員下拉框
	 * @author barryZhang
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return List<Parameter>:通知倉管人員下拉框
	 */
	//public List<Parameter> getWareHouseUserNameList() throws ServiceException;
	
	/**
	 * Purpose:得到通知倉管人員下拉框
	 * @author barryzhang
	 * @param inquiryContext:ajax上下文inquiryContext:ajax
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return List<Parameter>:通知人員列表
	 */
	public List<Parameter> getWareHouseUserNameList(MultiParameterInquiryContext 
			inquiryContext) throws ServiceException;
	
	/**
	 * Purpose:轉倉批號查詢
	 * @author starwang
	 * @param inquiryContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>
	 */
	public List<Parameter> getAssetInfoList(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	/**
	 * Purpose:查詢設備序號
	 * @author starwang
	 * @param inquiryContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>
	 */
	public String checkSerialNumber(MultiParameterInquiryContext inquiryContext) throws ServiceException;
}
