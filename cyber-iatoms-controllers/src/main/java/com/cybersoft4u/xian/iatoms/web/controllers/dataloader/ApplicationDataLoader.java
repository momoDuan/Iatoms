package com.cybersoft4u.xian.iatoms.web.controllers.dataloader;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cafe.core.bean.Parameter;
import cafe.core.config.GenericConfigManager;
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
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractAssetDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ContractSlaDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ApplicationFormDTO;

/**
 * Purpose: 程式版本維護DataLoader
 * @author echomou
 * @since  JDK 1.6
 * @date   2016-7-14
 * @MaintenancePersonnel echomou
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ApplicationDataLoader extends BaseInitialDataLoader {
	
	/**
	 * 日誌掛件
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.COMMON, ApplicationDataLoader.class);
	
	/**
	 * service代理
	 */
	private IServiceLocatorProxy serviceLocator;
	
	/**
	 * Constructor--.
	 */
	public ApplicationDataLoader(){
		super();
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.dataloader.BaseInitialDataLoader#load(javax.servlet.http.HttpServletRequest, cafe.core.context.SessionContext)
	 */
	public void load(HttpServletRequest request, SessionContext sessionContext) throws Exception {
		super.load(request, sessionContext);
		try {
			if (sessionContext != null) {
				String ucNo = this.getUseCaseNo(sessionContext);
				IAtomsLogonUser logonUser = (IAtomsLogonUser) SessionHelper.getLogonUser(request);
				MultiParameterInquiryContext param = new MultiParameterInquiryContext();
				param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
				param.addParameter(CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue(), IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE);
				param.addParameter(ContractSlaDTO.ATTRIBUTE.IS_HAVE_SLA.getValue(), false);
				//獲取客戶信息
				SessionContext returnCtx = (SessionContext) this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_COMPANY_SERVICE, IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
				List<Parameter> companyList = null;
				if(returnCtx != null){
					companyList = (List<Parameter>) returnCtx.getResponseResult();
				}
				SessionHelper.setAttribute(request, ucNo, ApplicationFormDTO.CUSTOMER_INFO_LIST, companyList);

				//獲取設備列表				
				MultiParameterInquiryContext param1 = new MultiParameterInquiryContext();
				param.addParameter(BimContractAssetDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), null);
				returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE, IAtomsConstants.ACTION_GET_ASSET_TYPE_LIST, param1 );
				List<Parameter> assetNameList = ( List<Parameter> ) returnCtx.getResponseResult();
				
				SessionHelper.setAttribute(request, ucNo, 
						ApplicationFormDTO.GET_ASSET_TYPE_LIST, assetNameList);
			}
			
		}catch(Exception e) {
			LOGGER.error(this.getClass().getName() + ":load(),Error——>" + e, e);
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
