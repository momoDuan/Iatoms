package com.cybersoft4u.xian.iatoms.web.controllers.dataloader;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ComplaintDTO;

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
 * Purpose: 客訴管理 DataLoader
 * @author	nicklin
 * @since	JDK 1.7
 * @date	2018/03/06
 * @MaintenancePersonnel cybersoft
 */
@SuppressWarnings("rawtypes")
public class ComplaintDataLoader extends BaseInitialDataLoader {
	
	/**
	 * 系统log物件
	 */
	private static CafeLog logger = CafeLogFactory.getLog(GenericConfigManager.COMMON, ComplaintDataLoader.class);
	
	/**
	 * service代理
	 */
	private IServiceLocatorProxy serviceLocator;
	
	/**
	 * Constructor:無參數建構子
	 */
	public ComplaintDataLoader() {
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.dataloader.BaseInitialDataLoader#load(javax.servlet.http.HttpServletRequest, cafe.core.context.SessionContext)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void load(HttpServletRequest request, SessionContext sessionContext)
			throws Exception {
		// TODO Auto-generated method stub
		super.load(request, sessionContext);
		try {
			//UCNO
			String ucNo =this.getUseCaseNo(sessionContext);
			//返回值
			SessionContext returnCtx = null;
			List<Parameter> customers = null;
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			
			if(IAtomsConstants.ACTION_INIT.equals(sessionContext.getActionId())
					|| IAtomsConstants.ACTION_INIT_EDIT.equals(sessionContext.getActionId())) {
				//客戶下拉菜單
				param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
				param.addParameter(CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue(), IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE);
				returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_COMPANY_SERVICE,
						IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
				customers = (List<Parameter>) returnCtx.getResponseResult();
				
				//維護廠商列表(客訴對象廠商)
				List<Parameter> vendorList = null;
				param = new MultiParameterInquiryContext();
				param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_VENDOR);
				returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_COMPANY_SERVICE,
						IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
				vendorList = (List<Parameter>) returnCtx.getResponseResult();
				SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_VENDOR_LIST, vendorList);
			}
			SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_CUSTOMER_LIST, customers);
		} catch(Exception e) {
			logger.error(this.getClass().getName() + ":load(), Error——>" + e, e);
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
