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
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ContractSlaDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.EdcPaymentReportFormDTO;
/**
 * Purpose: EDC維護費用付款報表DataLoader
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2017/6/16
 * @MaintenancePersonnel HermanWang
 */
public class EdcQueryDataLoader extends BaseInitialDataLoader {
	
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.COMMON, EdcQueryDataLoader.class);
	/**
	 *在servlet中已經注入 
	 */
	private IServiceLocatorProxy serviceLocator;
	
	/**
	 * Constructor:
	 */
	public EdcQueryDataLoader(){
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.dataloader.BaseInitialDataLoader#load(javax.servlet.http.HttpServletRequest, cafe.core.context.SessionContext)
	 */
	public void load(HttpServletRequest request, SessionContext sessionContext)
			throws Exception {
		super.load(request, sessionContext);
		try{
			String ucNo = this.getUseCaseNo(sessionContext);
			EdcPaymentReportFormDTO formDTO = (EdcPaymentReportFormDTO) sessionContext.getRequestParameter();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
			
			//當前登入者角色屬性
			String userAttribute = null;
			//得到用戶角色列表
			List<AdmRoleDTO> userRoleList = logonUser.getUserFunctions();
			Boolean isVendor = false;
			for (AdmRoleDTO admRoleDTO : userRoleList) {
				userAttribute = admRoleDTO.getAttribute();
				//是廠商角色
				if (IAtomsConstants.VECTOR_ROLE_ATTRIBUTE.equals(userAttribute) || IAtomsConstants.CUSTOMER_ROLE_ATTRIBUTE.equals(userAttribute)) {
					isVendor = true;
					//獲取廠商下拉選單資料
					List<Parameter> companyList = null;
					List<Parameter> customerList = null;
					MultiParameterInquiryContext param = new MultiParameterInquiryContext();
					param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
					param.addParameter(CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue(), IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE);
					param.addParameter(ContractSlaDTO.ATTRIBUTE.IS_HAVE_SLA.getValue(), Boolean.valueOf(false));
					SessionContext returnCtx = super.getServiceLocator().doService(logonUser, IAtomsConstants.SERVICE_COMPANY_SERVICE, IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
					if(returnCtx != null){
						companyList = (List<Parameter>) returnCtx.getResponseResult();
					}
					MultiParameterInquiryContext param1 = new MultiParameterInquiryContext();
					param1.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), null);
					param1.addParameter(CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue(), null);
					param1.addParameter(ContractSlaDTO.ATTRIBUTE.IS_HAVE_SLA.getValue(), Boolean.valueOf(false));
					SessionContext returnCtx1 = super.getServiceLocator().doService(logonUser, IAtomsConstants.SERVICE_COMPANY_SERVICE, IAtomsConstants.ACTION_GET_COMPANY_LIST, param1);
					if(returnCtx1 != null){
						customerList = (List<Parameter>) returnCtx1.getResponseResult();
					}
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_CUSTOMER_LIST, customerList);
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_COMPANY_LIST, companyList);
				} 
				if(isVendor) {
					break;
				}
			}
		}catch(Exception e){
			LOGGER.error(".load() is error:", e);
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
