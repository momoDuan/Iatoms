package com.cybersoft4u.xian.iatoms.web.controllers.dataloader;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.dataloader.BaseInitialDataLoader;
import cafe.core.web.controller.util.SessionHelper;
import cafe.core.web.locator.IServiceLocatorProxy;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;


/**
 * Purpose: 設備庫存表DataLoader
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016-7-28
 * @MaintenancePersonnel CrissZhang
 */
public class AssetStockReportDataLoader extends BaseInitialDataLoader {
	/**
	 * log
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(AssetStockReportDataLoader.class);
	
	/**
	 * service代理
	 */
	private IServiceLocatorProxy serviceLocator;
	
	/**
	 * Constructor--.
	 */
	public AssetStockReportDataLoader(){
		
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.dataloader.BaseInitialDataLoader#load(javax.servlet.http.HttpServletRequest, cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void load(HttpServletRequest request, SessionContext sessionContext) throws Exception {
		super.load(request, sessionContext);
		try {
			if (sessionContext != null) {
				String ucNo = this.getUseCaseNo(sessionContext);
				IAtomsLogonUser logonUser = (IAtomsLogonUser) sessionContext.getLogonUser();
				MultiParameterInquiryContext param = new MultiParameterInquiryContext();
				// 客戶類型
				param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
				// iatoms驗證
				param.addParameter(CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue(), IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE);
				SessionContext returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_COMPANY_SERVICE, IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
				List<Parameter> listParameter = (List<Parameter>) returnCtx.getResponseResult();
				SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_COMPANY_LIST, listParameter);
				//獲取客戶下拉選單資料
				param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), null);
				returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_COMPANY_SERVICE, IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
				listParameter = (List<Parameter>) returnCtx.getResponseResult();
				SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_CUSTOMER_LIST, listParameter);
			}
		}catch(Exception e) {
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
