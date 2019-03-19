package com.cybersoft4u.xian.iatoms.services;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.context.BaseParameterInquiryContext;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

/**
 * Purpose: 系統參數維護Service
 * @author ericdu
 * @since  JDK 1.7
 * @date   2016年6月27日
 * @MaintenancePersonnel ericdu
 */
@SuppressWarnings("rawtypes")
public interface IBaseParameterManagerService extends IAtomicService {
	
	/**
	 * 
	 * Purpose: 頁面初始化
	 * @author ericdu
	 * @param sessionContext 上下文Context
	 * @throws ServiceException 錯誤時拋出service異常類
	 * @return SessionContext 上下文Context
	 */
	public SessionContext init(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * 
	 * Purpose: 初始化获取参数类别下拉选单资料
	 * @author ericdu
	 * @param sessionContext 上下文Context
	 * @throws ServiceException 錯誤時拋出service异常类
	 * @return SessionContext 上下文Context
	 */
	public SessionContext listParameterTypes(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * 
	 * Purpose: 查詢
	 * @author ericdu
	 * @param sessionContext 上下文Context
	 * @throws ServiceException 錯誤時拋出service异常类
	 * @return SessionContext 上下文Context
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * 
	 * Purpose: 存储
	 * @author ericdu
	 * @param sessionContext 上下文Context
	 * @throws ServiceException 錯誤時拋出service异常类
	 * @return SessionContext 上下文Context
	 */
	public SessionContext save(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * 
	 * Purpose: 獲取初始化編輯數據
	 * @author ericdu
	 * @param sessionContext 上下文Context
	 * @throws ServiceException 錯誤時拋出service异常类
	 * @return SessionContext 上下文Context
	 */
	public SessionContext initEdit(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * 
	 * Purpose: 刪除參數數據(標記ApprovedFlag 爲 'N')
	 * @author ericdu
	 * @param sessionContext 上下文Context
	 * @throws ServiceException service异常类
	 * @return SessionContext 上下文Context
	 */
	public SessionContext delete(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:根據父類別參數查找對應的子類別參數列表
	 * @author evanliu
	 * @param inquiryContext：傳入的參數值（parentCode:父類別code， parentValue：父類別itemValue，childrenCode:子類別code）
	 * @throws ServiceException：出錯後拋出ServiceException
	 * @return List<Parameter>：參數列表
	 */
	public List<Parameter> getParametersByParent(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	
	/**
	 * Purpose:根據TextField參數查找對應的參數列表
	 * @author CarrieDuan
	 * @param inquiryContext：傳入的參數值（Bptd_code, TEXT_FIELD1）
	 * @throws ServiceException：出錯後拋出ServiceException
	 * @return List<Parameter>：參數列表
	 */
	public List<Parameter> getParametersByTextField(MultiParameterInquiryContext inquiryContext) throws ServiceException;
}
