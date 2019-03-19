package com.cybersoft4u.xian.iatoms.services;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

/**
 * Purpose: 設備品項維護
 * @author HermanWang
 * @since  JDK 1.7
 * @date   2016年7月8日
 * @MaintenancePersonnel HermanWang
 */
@SuppressWarnings("rawtypes")
public interface IAssetTypeService extends IAtomicService {
	/**
	 * 
	 * Purpose: 初始化頁面
	 * @author HermanWang
	 * @param sessionContext 上下文Context
	 * @throws ServiceException 錯誤時拋出service異常類
	 * @return SessionContext 上下文Context
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	/**
	 * 
	 * Purpose: 查詢
	 * @author HermanWang
	 * @param sessionContext 上下文Context
	 * @throws ServiceException 錯誤時拋出service異常類
	 * @return SessionContext 上下文Context
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException;
	/**
	 * 
	 * Purpose: 初始化編輯頁面
	 * @author HermanWang
	 * @param sessionContext 上下文Context
	 * @throws ServiceException 錯誤時拋出service異常類
	 * @return SessionContext 上下文Context
	 */
	public SessionContext initEdit(SessionContext sessionContext) throws ServiceException;
	/**
	 * 
	 * Purpose: 獲取廠商信息下拉框初始化資料
	 * @author HermanWang
	 * @param sessionContext 上下文Context
	 * @throws ServiceException 錯誤時拋出service異常類
	 * @return SessionContext 上下文Context
	 */
	public SessionContext getCompanyParameterList(SessionContext sessionContext) throws ServiceException;
	/**
	 * 
	 * Purpose: 儲存
	 * @author HermanWang
	 * @param sessionContext 上下文Context
	 * @throws ServiceException 錯誤時拋出service異常類
	 * @return SessionContext 上下文Context
	 */
	public SessionContext save(SessionContext sessionContext) throws ServiceException;
	/**
	 * 
	 * Purpose: 删除
	 * @author HermanWang
	 * @param sessionContext 上下文Context
	 * @throws ServiceException 錯誤時拋出service異常類
	 * @return SessionContext 上下文Context
	 */
	public SessionContext delete(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose: 根據設備類別獲取對應的設備列表
	 * @author CarrieDuan
	 * @param param ：包含設備類別的參數
	 * @throws SecurityException ：錯誤時拋出service異常類
	 * @return List<Parameter> ：返回設備列表
	 */
	public List<Parameter> getAssetTypeList(MultiParameterInquiryContext param) throws SecurityException;
	/**
	 * Purpose:判斷改設備是否有庫存和設備代碼
	 * @author HermanWang
	 * @param param
	 * @throws ServiceException：錯誤時拋出ServiceException異常public List<Parameter> getAssetModelsByAssetTypeId()
	 * @return Boolean：boolean的結果
	 */
	public boolean isAssetList(MultiParameterInquiryContext param) throws ServiceException;
	
	/**
	 * Purpose: 獲取設備型號
	 * @author CarrieDuan
	 * @param param
	 * @throws ServiceException：錯誤時拋出ServiceException異常
	 * @return List<Parameter> ：設備型號列表
	 */
	public List<Parameter> getAssetModelList(MultiParameterInquiryContext param) throws ServiceException;
	
	/**
	 * 
	 * Purpose:跟據設備id獲取設備廠牌
	 * @author CarrieDuan
	 * @param param
	 * @throws ServiceException：錯誤時拋出ServiceException異常
	 * @return List<Parameter> ：設備廠牌列表
	 */
	public List<Parameter> getAssetBrandList(MultiParameterInquiryContext param) throws ServiceException;
	
	/**
	 * Purpose: 獲取內建功能
	 * @author CarrieDuan
	 * @param param
	 * @throws ServiceException：錯誤時拋出ServiceException異常
	 * @return List<Parameter> ：內建功能列表
	 */
	public List<Parameter> getBuiltInFeature(MultiParameterInquiryContext param) throws ServiceException;
	
	/**
	 * Purpose:依據設備ID獲取通訊模式
	 * @author CarrieDuan
	 * @param param
	 * @throws SecurityException：錯誤時拋出ServiceException異常
	 * @return List<Parameter>：通訊模式列表
	 */
	public List<Parameter> getConnectionTypeList(MultiParameterInquiryContext param) throws SecurityException;
	
	/**
	 * Purpose:案件處理建案中，依據所選客戶獲取該客戶下的EDC設備或周邊設備（並且該設備的領用人為當前客戶）
	 * @author CarrieDuan
	 * @param param
	 * @throws ServiceException：錯誤時拋出ServiceException異常
	 * @return List<Parameter>：刷卡機型設備列表
	 */
	public List<Parameter> getAssetListForCase(MultiParameterInquiryContext param) throws ServiceException;
}
