package com.cybersoft4u.xian.iatoms.services;

import java.util.List;
import com.cybersoft4u.xian.iatoms.services.workflow.IIAtomsHumanTaskActivityService;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.service.ServiceException;

/**
 * Purpose: atoms使用者維護
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016年3月30日
 * @MaintenancePersonnel CrissZhang
 */
public interface IAdmUserService extends IIAtomsHumanTaskActivityService {
	/**
	 * Purpose:初始化頁面
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:查詢
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose: init edit page
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext initEdit(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:進入新增頁面
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext initAdd(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose: save
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext save(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose: 檢查用戶名是否重複
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext check(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose: 刪除用戶
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext delete(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose: export
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext export(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose: 保存系統日誌
	 * @author CrissZhang
	 * @param inquiryContext ： ajax傳入參數
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return void ： 不返回
	 */
	public void saveSystemLog(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	/**
	 * Purpose: 獲取正常帳號的EMAIL
	 * @author HermanWang
	 * @param inquiryContext ：ajax傳入的參數
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return @return List<Parameter> ：下拉框列表
	 */
	public List<Parameter> getNormalUserEmailList(MultiParameterInquiryContext inquiryContext) throws ServiceException;

	/**
	 * Purpose:查詢
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext list(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:獲取通知mail人員
	 * @author amandawang
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return String:json stringmail 人員字符串
	 */
	public List<Parameter> getMailGroupList() throws ServiceException;
	/**
	 * Purpose:獲取通知mail人員name
	 * @author amandawang
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return String:json stringmail 人員字符串
	 */
	public String getNameList() throws ServiceException;
	/**
	 * Purpose:根據公司id獲取公司下的所有的工程師
	 * @author Hermanwang
	 * @param parameterInquiryContext: ajax傳入的參數
	 * @throws ServiceException：出錯時, 丟出ServiceException
	 * @return List<Parameter>：下拉框List
	 */
	public List<Parameter> getUserListByCompany(MultiParameterInquiryContext parameterInquiryContext)throws ServiceException;
	
	/**
	 * Purpose:根据公司编号判断登入验证方式是否为iAtoms驗證
	 * @author CrissZhang
	 * @param inquiryContext ：ajax传入参数
	 * @throws ServiceException：出錯時拋出ServiceException
	 * @return boolean :返回布尔类型
	 */
	public SessionContext isAuthenticationTypeEqualsIAtoms(SessionContext sessionContext) throws ServiceException;
}