package com.cybersoft4u.xian.iatoms.services;

import java.util.List;

import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractManageFormDTO;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

/**
 * Purpose: 合約維護service interface
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2016/6/16
 * @MaintenancePersonnel CarrieDuan
 */
public interface IContractService extends IAtomicService{
	/**
	 * Purpose:初始化頁面
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException :出錯時抛出出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext init (SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:查詢
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException :出錯時抛出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext query (SessionContext sessionContext) throws ServiceException;

	/**
	 * Purpose:初始化编辑
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException :出錯時抛出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	SessionContext initEdit (SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:初始化新增
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException :出錯時抛出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	SessionContext initAdd (SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose: 储存
	 * @author CarrieDuan
	 * @param sessionContext : 上下文SessionContext
	 * @throws ServiceException : 出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext save(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose: 刪除合约维护
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException :出錯時, 丟出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext delete (SessionContext sessionContext) throws ServiceException;
	/**
	 * 
	 * Purpose:修改時抓取合約設備集合
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException :出錯時, 丟出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext getContractAssetList (SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:根據選擇的客戶獲取其所有合同
	 * @author CrissZhang
	 * @param inquiryContext ：ajax傳入的參數
	 * @throws ServiceException ：出錯時, 丟出ServiceException
	 * @return List<Parameter> ：下拉框列表
	 */
	public List<Parameter> getContractByCustomerAndStatus(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	
	/**
	 * Purpose:根據條件查找合約編號
	 * @author CrissZhang
	 * @param inquiryContext ：ajax傳入的參數
	 * @throws ServiceException ：出錯時, 丟出ServiceException
	 * @return List<Parameter> ：下拉框列表
	 */
	public List<Parameter> getContractCodeList(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	
	/**
	 * Purpose:獲取合約的客戶保固期與廠商保固期
	 * @author CarrieDuan
	 * @param inquiryContext
	 * @throws ServiceException
	 * @return String
	 */
	public Integer getContractWarranty(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	/**
	 * Purpose:獲取下載文件路徑
	 * @author CarrieDuan
	 * @param command ：合約FORMDTO
	 * @throws ServiceException ：出錯時, 丟出ServiceException
	 * @return String ：文件路徑
	 */
	public SessionContext getFilePath(ContractManageFormDTO command) throws ServiceException;
	
	/**
	 * Purpose: 根據文件ID，獲取路徑
	 * @author CarrieDuan
	 * @param inquiryContext ：傳入的參數
	 * @throws ServiceException：出錯時, 丟出ServiceException
	 * @return String：文件路徑
	 */
	public String getFilePath(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	
	/**
	 * Purpose:根據合約ID獲取對應的周邊設備
	 * @author CarrieDuan
	 * @param inquiryContext
	 * @throws ServiceException ：出錯時, 丟出ServiceException
	 * @return List<Parameter> : 周邊設備
	 */
	public List<Parameter> getPeripherals(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	
	/**
	 * Purpose:根據合約獲取對應的維護廠商
	 * @author CarrieDuan
	 * @param inquiryContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>:維護廠商列表
	 */
	public List<Parameter> getVendors(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	
	/**
	 * Purpose:獲得設備表
	 * @author CarrieDuan
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>:設備列表
	 */
	public List<Parameter> getAssetList() throws ServiceException;
	
	/**
	 * Purpose:獲取客戶下第一個有edc的合約
	 * @author CrissZhang
	 * @param inquiryContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return String : 返回一個字符串
	 */
	public String getHaveEdcContract(MultiParameterInquiryContext inquiryContext) throws ServiceException;
}
