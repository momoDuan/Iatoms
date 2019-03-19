package com.cybersoft4u.xian.iatoms.services;

import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ComplaintFormDTO;

import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

/**
 * Purpose: 客訴管理Service接口 
 * @author	nicklin
 * @since	JDK 1.7
 * @date	2018/03/06
 * @MaintenancePersonnel cybersoft
 */
public interface IComplaintService extends IAtomicService {

	/**
	 * Purpose:初始化頁面信息
	 * @author nicklin
	 * @param  sessionContext：上下文SessionContext
	 * @throws ServiceException：出錯時，丟出ServiceException	
	 * @return SessionContext：上下文SessionContext
	 */
	@SuppressWarnings("rawtypes")
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:查詢客訴信息
	 * @author nicklin
	 * @param  sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	@SuppressWarnings("rawtypes")
	public SessionContext query(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:初始化編輯頁面
	 * @author nicklin
	 * @param  sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	@SuppressWarnings("rawtypes")
	public SessionContext initEdit(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:保存客訴信息
	 * @author nicklin
	 * @param  sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	@SuppressWarnings("rawtypes")
	public SessionContext save(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:刪除客訴
	 * @author nicklin
	 * @param  SessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	@SuppressWarnings("rawtypes")
	public SessionContext delete(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:保存上傳文件
	 * @author nicklin
	 * @param  sessionContext：上下文SessionContext
	 * @throws ServiceException：出錯時抛出ServiceException
	 * @return SessionContext：上下文SessionContext
	 */
	@SuppressWarnings("rawtypes")
	public SessionContext saveFile(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose：獲取文件路徑
	 * @author nicklin
	 * @param  param
	 * @throws ServiceException：出錯時抛出出ServiceException
	 * @return String：文件路徑
	 */
	public String getComplaintFilePath(MultiParameterInquiryContext param) throws ServiceException;
	
	/**
	 * Purpose：獲取文件下載路徑
	 * @author nicklin
	 * @param  command
	 * @throws ServiceException：出錯時抛出出ServiceException
	 * @return SessionContext：上下文SessionContext
	 */
	@SuppressWarnings("rawtypes")
	public SessionContext getComplaintFilePath(ComplaintFormDTO command) throws ServiceException;
	
	/**
	 * Purpose:匯出
	 * @author nicklin
	 * @param  sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	@SuppressWarnings("rawtypes")
	public SessionContext export(SessionContext sessionContext) throws ServiceException;
}
