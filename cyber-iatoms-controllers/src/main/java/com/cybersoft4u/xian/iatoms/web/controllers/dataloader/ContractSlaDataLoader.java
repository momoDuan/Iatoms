package com.cybersoft4u.xian.iatoms.web.controllers.dataloader;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ContractSlaDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractSlaFormDTO;

import cafe.core.bean.Parameter;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.BaseParameterInquiryContext;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.dataloader.BaseInitialDataLoader;
import cafe.core.web.controller.util.SessionHelper;
import cafe.core.web.locator.IServiceLocatorProxy;
/**
 * Purpose: 合約SLA設定DataLoader
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016/6/29
 * @MaintenancePersonnel CrissZhang
 */
public class ContractSlaDataLoader extends BaseInitialDataLoader {
	/**
	 * 系统日志记录物件  
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.COMMON, ContractSlaDataLoader.class);

	/**
	 * service代理
	 */
	private IServiceLocatorProxy serviceLocator;

	/**
	 * Constructor:無參構造函數
	 */
	public ContractSlaDataLoader() {
	}
	
	/** (non-Javadoc)
	 * @see cafe.core.web.controller.dataloader.BaseInitialDataLoader#load(javax.servlet.http.HttpServletRequest, cafe.core.context.SessionContext)
	 */
	public void load(HttpServletRequest request, SessionContext sessionContext)
			throws Exception {
		super.load(request, sessionContext);
		try {
			if (sessionContext != null){
				String ucNo = this.getUseCaseNo(sessionContext);
				// 获取登录者
				IAtomsLogonUser logonUser = (IAtomsLogonUser) SessionHelper.getLogonUser(request);
				MultiParameterInquiryContext param = new MultiParameterInquiryContext();
				param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
				param.addParameter(CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue(), IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE);
				Boolean flag = Boolean.valueOf(false);
				param.addParameter(ContractSlaDTO.ATTRIBUTE.IS_HAVE_SLA.getValue(), flag);
				// 客户列表
				SessionContext returnCtx = super.getServiceLocator().doService(logonUser, IAtomsConstants.SERVICE_COMPANY_SERVICE, IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
				List<Parameter> listParameter = (List<Parameter>) returnCtx.getResponseResult();
				SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_CUSTOMER_LIST, listParameter);
				// 獲取有sla信息顧客的列表
				flag = Boolean.valueOf(true);
				param.addParameter(ContractSlaDTO.ATTRIBUTE.IS_HAVE_SLA.getValue(), flag);
				returnCtx = super.getServiceLocator().doService(logonUser, IAtomsConstants.SERVICE_COMPANY_SERVICE, IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
				listParameter = (List<Parameter>) returnCtx.getResponseResult();
				SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_SLA_COMPANY_LIST, listParameter);
				// 案件類型列表下拉框
				BaseParameterInquiryContext inquiryCtx = new BaseParameterInquiryContext();
				inquiryCtx.setParameterType(IATOMS_PARAM_TYPE.TICKET_MODE.getCode());
				inquiryCtx.setTextField1(IAtomsConstants.PARAM_SLA_USE);
				returnCtx = super.getServiceLocator().doService(logonUser, IAtomsConstants.SERVICE_BASE_PARAMETER_SERVICE, IAtomsConstants.ACTION_GET_PARAMETER_ITEMS, inquiryCtx);
				listParameter = (List<Parameter>) returnCtx.getResponseResult();
				SessionHelper.setAttribute(request, ucNo, ContractSlaFormDTO.PARAM_TICKET_MODE_LIST, listParameter);
			}
		} catch (Exception e) {
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
