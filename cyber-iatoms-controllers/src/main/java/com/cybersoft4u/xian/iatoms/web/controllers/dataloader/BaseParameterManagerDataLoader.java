package com.cybersoft4u.xian.iatoms.web.controllers.dataloader;

import java.util.List;

import javax.servlet.http.HttpServletRequest;


import cafe.core.bean.Parameter;
import cafe.core.context.SessionContext;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.dataloader.BaseInitialDataLoader;
import cafe.core.web.controller.util.SessionHelper;
import cafe.core.web.locator.IServiceLocatorProxy;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.BaseParameterManagerFormDTO;
import com.cybersoft4u.xian.iatoms.web.controllers.ajax.IAtomsAjaxService;

/**
 * Purpose: 系統參數維護初始化載入器
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016年6月28日
 * @MaintenancePersonnel CrissZhang
 */
public class BaseParameterManagerDataLoader extends BaseInitialDataLoader<IAtomsAjaxService> {
	
	/**
	 * 系統日志記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(BaseParameterManagerDataLoader.class);
	
	/**
	 * service代理
	 */
	private IServiceLocatorProxy serviceLocator;

	/**
	 * 
	 * Constructor: 無參構造函數
	 */
	public BaseParameterManagerDataLoader() {
	}
	
	/** (non-Javadoc)
	 * @see cafe.core.web.controller.dataloader.BaseInitialDataLoader#load(javax.servlet.http.HttpServletRequest, cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void load(HttpServletRequest request, SessionContext sessionContext)
			throws Exception {
		super.load(request, sessionContext);
		try{
			if (sessionContext != null){
				IAtomsLogonUser logonUser = (IAtomsLogonUser) sessionContext.getLogonUser();
				String ucNo = this.getUseCaseNo(sessionContext);
				List<Parameter> parameterTypes = null;
				SessionContext returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_BASE_PARAMETER_MANAGER_SERVICE, IAtomsConstants.ACTION_LIST_PARAMETER_TYPES, null);
				parameterTypes = (List<Parameter>) returnCtx.getResponseResult();
				SessionHelper.setAttribute(request, ucNo, BaseParameterManagerFormDTO.PARAMETER_TYPE, parameterTypes);
			}
		} catch(Exception e) {
			LOGGER.error("load()", e.getMessage(), e);
			throw new Exception(IAtomsMessageCode.BASE_PARAMETER_VALUE_IS_NULL, e);
		}
	}
	
	
	/**
	 * @return the serviceLocator
	 */
	public IServiceLocatorProxy getServiceLocator() {
		return serviceLocator;
	}

	/**
	 * @param serviceLocator the serviceLocator to set
	 */
	public void setServiceLocator(IServiceLocatorProxy serviceLocator) {
		this.serviceLocator = serviceLocator;
	}
}
