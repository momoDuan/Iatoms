package com.cybersoft4u.xian.iatoms.web.controllers.dataloader;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ContractSlaDTO;

import cafe.core.bean.Parameter;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.dataloader.BaseInitialDataLoader;
import cafe.core.web.controller.util.SessionHelper;
import cafe.core.web.locator.IServiceLocatorProxy;
/**
 * Purpose: 部門維護 DataLoader
 * @author Amanda Wang
 * @since  JDK 1.6
 * @date   2016/6/14
 * @MaintenancePersonnel Amanda Wang
 */
@SuppressWarnings("rawtypes")
public class DepartmentDataLoader extends BaseInitialDataLoader {
	/**
	 * 系统日志记录物件  
	 */
	private static CafeLog logger = CafeLogFactory.getLog(GenericConfigManager.COMMON, DepartmentDataLoader.class);

	/**
	 * service代理
	 */
	private IServiceLocatorProxy serviceLocator;

	/**
	 * Constructor:無參構造函數
	 */
	public DepartmentDataLoader() {
		super();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cafe.core.web.controller.dataloader.BaseInitialDataLoader#load(javax.servlet.http.HttpServletRequest,
	 *      cafe.core.context.SessionContext)
	 */
	@SuppressWarnings("unchecked")
	public void load(HttpServletRequest request, SessionContext sessionContext)
			throws Exception {
		super.load(request, sessionContext);
		try {
			// 获取登录者
			IAtomsLogonUser logonUser = (IAtomsLogonUser) SessionHelper.getLogonUser(request);
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), null);
			param.addParameter(CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue(), null);
			param.addParameter(ContractSlaDTO.ATTRIBUTE.IS_HAVE_SLA.getValue(), false);
			// 公司列表
			SessionContext returnCtx = (SessionContext) this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_COMPANY_SERVICE,IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
			List<Parameter> listParameter = (List<Parameter>)returnCtx.getResponseResult();
			SessionHelper.setAttribute(request,IAtomsConstants.UC_NO_BIM_02020,IAtomsConstants.ACTION_GET_COMPANY_LIST, listParameter);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+":load(),Error——>"+e,e);
			throw new Exception(IAtomsMessageCode.BASE_PARAMETER_VALUE_IS_NULL,e);
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
