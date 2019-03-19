package com.cybersoft4u.xian.iatoms.services;

import java.rmi.ServerException;
import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractManageFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.SrmCaseTemplatesFormDTO;

/**
 * Purpose: 工單範本維護Service interface
 * @author echomou
 * @since  JDK 1.6
 * @date   2016/9/23
 * @MaintenancePersonnel echomou
 */
@SuppressWarnings("rawtypes")
public interface ISrmCaseTemplatesService  extends IAtomicService{
	/**
	 * Purpose:工單範本維護的初始化頁面方法
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServerException：出錯時, 丟出ServerException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:工單範本維護的上傳範本方法
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServerException：出錯時, 丟出ServerException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext upload(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:工單範本維護的刪除範本方法
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServerException：出錯時, 丟出ServerException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext delete(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:工單範本維護的下載範本方法
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServerException：出錯時, 丟出ServerException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext getFilePath(SrmCaseTemplatesFormDTO command) throws ServiceException;
	/**
	 * Purpose:獲取下載文件路徑
	 * @author HermanWang
	 * @param command ：FORMDTO
	 * @throws ServiceException ：出錯時, 丟出ServiceException
	 * @return String ：文件路徑
	 */
	public String getFilePath(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	/**
	 * Purpose:根據範本類別和範本名稱獲取上傳範本的id
	 * @author HermanWang
	 * @param inquiryContext：ajax參數
	 * @throws ServiceException ：出錯時, 丟出ServiceException
	 * @return String ：文件id
	 */
	public String getUploadTemplatesId(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	/**
	 * Purpose:獲取上傳的所有範本
	 * @author HermanWang
	 * @param inquiryContext：ajax參數
	 * @throws ServiceException：出錯時, 丟出ServiceException
	 * @return List<Parameter>：範本List
	 */
	public List<Parameter> getTemplatesList(MultiParameterInquiryContext inquiryContext) throws ServiceException;
}
