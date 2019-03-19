package com.cybersoft4u.xian.iatoms.services;

import java.util.List;

import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;


import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

/**
 * Purpose: 公司基本訊息維護Service interface
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2016年7月1日
 * @MaintenancePersonnel ElvaHe
 */
@SuppressWarnings({ "rawtypes" })
public interface ICompanyService extends IAtomicService{
	
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
	 * Purpose:保存公司基本訊息信息
	 * @author ElvaHe
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext save(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:查詢公司基本訊息信息
	 * @author ElvaHe
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:刪除公司基本訊息信息
	 * @author ElvaHe
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext delete(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:查詢要匯出的公司信息
	 * @author ElvaHe
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext export(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:根據客戶ID獲取相應的唯一編號
	 * @author CarrieDuan
	 * @param parameterInquiryContext : 合約編號ID
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return String ：返回唯一編號
	 */
	public String getUnityNameByCompanyId (MultiParameterInquiryContext parameterInquiryContext) throws ServiceException; 
	
	/**
	 * Purpose:得到公司下拉框列表
	 * @author CrissZhang
	 * @param inquiryContext ：ajax傳入的參數
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return List<Parameter>：下拉框List
	 */
	public List<Parameter> getCompanyList(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	
	/**
	 * Purpose:根據條件得到公司下拉框列表
	 * @author CrissZhang
	 * @param companyTypeList ： 公司下拉框列表
	 * @param authenticationType ： 公司登入驗證方式 
	 * @param isHaveSla ： 是否有sla信息
	 * @param dtidType : dtid生成方式
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return List<Parameter>：下拉框List
	 */
	public List<Parameter> getCompanyParameters (List<String> companyTypeList, String authenticationType, Boolean isHaveSla, String dtidType) throws ServiceException;
	
	/**
	 * Purpose: 將需要新增的數據設置到數據庫
	 * @author ElvaHe
	 * @param companyDTO：公司DTO
	 * @param userId：使用者編號
	 * @param userName：使用者姓名
	 * @throws ServiceException :出錯時, 丟出ServiceException
	 * @return void
	 */
	public void saveData(CompanyDTO companyDTO, String userId, String userName) throws ServiceException;
	
	/**
	 * Purpose:是否為同TID
	 * @author CarrieDuan
	 * @param inquiryContext : ajax傳入的參數
	 * @throws ServiceException ：出錯時, 丟出ServiceException
	 * @return boolean：是否為同TID
	 */
	public boolean isDtidType(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	
	/**
	 * Purpose:根據code獲取公司資料
	 * @author amandawang
	 * @param inquiryContext : ajax傳入的參數
	 * @throws ServiceException ：出錯時, 丟出ServiceException
	 * @return CompanyDTO：公司dto
	 */
	public CompanyDTO getCompanyByCompanyCode(MultiParameterInquiryContext parameterInquiryContext)throws ServiceException;
	/**
	 * Purpose:根據code獲取公司資料
	 * @author CrissZhang
	 * @param inquiryContext : ajax傳入的參數
	 * @throws ServiceException ：出錯時, 丟出ServiceException
	 * @return CompanyDTO：公司dto
	 */
	public boolean isAuthenticationTypeEqualsIAtoms (MultiParameterInquiryContext parameterInquiryContext)throws ServiceException;
	
	/**
	 * Purpose:根據id獲取公司資料
	 * @author CarrieDuan
	 * @param parameterInquiryContext : ajax傳入的參數
	 * @throws ServiceException ：出錯時, 丟出ServiceException
	 * @return CompanyDTO：公司dto
	 */
	public CompanyDTO getCompanyByCompanyId(MultiParameterInquiryContext parameterInquiryContext) throws ServiceException;
}
