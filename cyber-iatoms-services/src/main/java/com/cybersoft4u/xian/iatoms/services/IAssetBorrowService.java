package com.cybersoft4u.xian.iatoms.services;

import java.util.List;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetBorrowInfoDTO;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;
/**
 * Purpose: 設備借用service
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2018年7月31日
 * @MaintenancePersonnel CarrieDuan
 */
public interface IAssetBorrowService extends IAtomicService {

	/**
	 * Purpose: 初始化方法
	 * @author CarrieDuan
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:查詢
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException :出錯拋出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:保存
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException:出錯拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext save(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:獲取案件編號下拉列表
	 * @author CarrieDuan
	 * @param param：參數
	 * @throws ServiceException:出錯拋出ServiceException
	 * @return List<Parameter>：案件下拉列表
	 */
	public List<Parameter> getBorrowCaseId(MultiParameterInquiryContext param) throws ServiceException;
	
	/**
	 * Purpose:設備借用處理保存
	 * @author CarrieDuan
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext saveProcess(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:
	 * @author CarrieDuan
	 * @param param: 參數
	 * @throws ServiceException:出錯拋出ServiceException
	 * @return List<DmmAssetBorrowInfoDTO>
	 */
	public List<DmmAssetBorrowInfoDTO> getBorrowAssetItemByIds(MultiParameterInquiryContext param) throws ServiceException;
	/**
	 * Purpose:查詢已經處理完成作業
	 * @author CarrieDuan
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext queryProcess(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:續借保存代碼
	 * @author CarrieDuan
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext saveBorrow(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:核檢設備序號是否輸入正確
	 * @author CarrieDuan
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext checkSerialNumber(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * 
	 * Purpose:
	 * @author CarrieDuan
	 * @param param
	 * @return
	 * @throws ServiceException
	 * @return String
	 */
	public String checkAssetIsBorrow(MultiParameterInquiryContext param)  throws ServiceException;
}
