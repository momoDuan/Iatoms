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
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ContractSlaDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.MerchantFormDTO;

/**
 * Purpose: 客戶特店數據加載
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2016/6/24
 * @MaintenancePersonnel HermanWang
 */
@SuppressWarnings("rawtypes")
public class MerchantDataLoader extends BaseInitialDataLoader {
	
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.COMMON, MerchantDataLoader.class);		
	/**
	 *在servlet中已經注入 
	 */
	private IServiceLocatorProxy serviceLocator;
	
	/**
	 * Constructor:
	 */
	public MerchantDataLoader(){
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.dataloader.BaseInitialDataLoader#load(javax.servlet.http.HttpServletRequest, cafe.core.context.SessionContext)
	 */
	@SuppressWarnings("unchecked")
	public void load(HttpServletRequest request, SessionContext sessionContext) throws Exception {
		super.load(request, sessionContext);
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			MerchantFormDTO formDTO = null;
			IAtomsLogonUser logonUser = (IAtomsLogonUser) SessionHelper.getLogonUser(request);
			formDTO = (MerchantFormDTO) sessionContext.getRequestParameter();
			String ucNo = formDTO.getUseCaseNo();
			//bimCompanyDTO.setCompanyType(IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
			//bimCompanyDTO.setAuthenticationType(IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE);
			param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
			param.addParameter(CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue(), IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE);
			param.addParameter(ContractSlaDTO.ATTRIBUTE.IS_HAVE_SLA.getValue(), Boolean.valueOf(false));
			SessionContext returnCtx = super.getServiceLocator().doService(logonUser, IAtomsConstants.SERVICE_COMPANY_SERVICE, IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
			List<Parameter> customerList = (List<Parameter>) returnCtx.getResponseResult();
			SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_CUSTOMER_LIST, customerList);
		} catch (Exception e) {
			LOGGER.error(":load(),Error——>" + e, e);
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
