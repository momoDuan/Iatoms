package com.cybersoft4u.xian.iatoms.services;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.service.ServiceException;

import com.cybersoft4u.xian.iatoms.services.workflow.IIAtomsHumanTaskActivityService;
/**
 * Purpose: 角色service interface 
 * @author CarrieDuan
 * @since  JDK 1.6
 * @date   2016年3月30日
 * @MaintenancePersonnel evanliu
 */
public interface IAdmRoleService extends IIAtomsHumanTaskActivityService {
	/**
	 * Purpose:初始化頁面
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose: 初始化角色清單
	 * @author CarrieDuan
	 * @throws ServiceException :出錯時, 丟出ServiceException
	 * @return List<Parameter> :角色List
	 */
	public List<Parameter> getRoleList() throws ServiceException;
	/**
	 * Purpose:根據用戶編號查找角色信息
	 * @author CarrieDuan
	 * @param userId :用戶編號
	 * @throws ServiceException :出錯時, 丟出ServiceException
	 * @return List<Parameter>:角色List
	 */
	public List<Parameter> getRoleListByUserId(String userId) throws ServiceException;
	
	/**
	 * Purpose:初始化查詢參數角色代號
	 * @author CarrieDuan
	 * @throws ServiceException :出錯時拋出ServiceException
	 * @return List<Parameter>:角色代號列表
	 */
	public List<Parameter> getRoleCode() throws ServiceException;	
	
	/**
	 * Purpose:查詢
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException :出錯拋出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:刪除
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException :出錯拋出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext delete(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:保存角色信息
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException :出錯拋出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext save (SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:初始化明細頁面
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException :出錯拋出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext initDetail (SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:明細頁面加載數據
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException:出錯拋出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext loadDlgData (SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:父功能列表
	 * @author CarrieDuan
	 * @param sessionContext : 上下文SessionContext
	 * @throws ServiceException : 出錯拋出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public List<Parameter> getParentFunction() throws ServiceException;
	
	/**
	 * Purpose:子功能列表
	 * @author CarrieDuan
	 * @param sessionContext : 上下文SessionContext
	 * @throws ServiceException : 出錯拋出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public List<Parameter> getFunctionByParentId (MultiParameterInquiryContext parentFunctionId) throws ServiceException;

	/**
	 * Purpose:保存角色權限
	 * @author CarrieDuan
	 * @param sessionContext :上下文SessionContext
	 * @throws ServiceException :出錯拋出ServiceException
	 * @return SessionContext :上下文SessionContext
	 */
	public SessionContext saveRolePermission (SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose: 根據角色屬性獲取對應的表單角色
	 * @author CarrieDuan
	 * @param attribute ： 角色屬性ID
	 * @throws ServiceException:出錯拋出ServiceException
	 * @return List<Parameter> ：返回表單角色列表
	 */
	public List<Parameter> getWorkFlowRoleList (MultiParameterInquiryContext inquiryContext) throws ServiceException;
	
	/**
	 * Purpose: 檢測角色是否已經被人員使用
	 * @author CarrieDuan
	 * @param inquiryContext ：參數
	 * @throws ServiceException:出錯拋出ServiceException
	 * @return Boolean ：結果的Boolean類型
	 */
	public Boolean checkUseRole (MultiParameterInquiryContext inquiryContext) throws ServiceException;
	
	/**
	 * Purpose: 保存系統日誌
	 * @author CrissZhang
	 * @param inquiryContext ： ajax傳入參數
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return void ： 不返回
	 */
	public void saveLog(MultiParameterInquiryContext inquiryContext) throws ServiceException ;
	
	/**
	 * Purpose: 保存系統日誌
	 * @author CrissZhang
	 * @param inquiryContext ： ajax傳入參數
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return void ： 不返回
	 */
	public void saveSystemLog(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	
	/**
	 * 
	 * Purpose:根據部門獲取工程師，根據TMS,QA,CUSTOMER_SERVICE獲取人員
	 * @author  amandawang
	 * @param inquiryContext ： ajax傳入參數
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return List<Parameter> 人員列表
	 */
	public List<Parameter> getUserByDepartmentAndRole(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	/**
	 * Purpose:保存角色明細時，核檢角色代碼，角色名稱是否重複。
	 * @author CarrieDuan
	 * @param inquiryContext： ajax傳入參數
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return String:錯誤信息
	 */
	public String checkRoleRepeat(MultiParameterInquiryContext inquiryContext) throws ServiceException;
}
