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
import com.cybersoft4u.xian.iatoms.common.bean.dto.ContractSlaDTO;
import com.cybersoft4u.xian.iatoms.web.controllers.ajax.IAtomsAjaxService;

/**
 * Purpose: 設備品項維護初始化載入器
 * @author HermanWang
 * @since  JDK 1.7
 * @date   2016年7月8日
 * @MaintenancePersonnel HermanWang
 */
public class AssetTypeDataLoader extends BaseInitialDataLoader<IAtomsAjaxService> {
	
	/**
	 * 系統日志記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(AssetTypeDataLoader.class);
	
	/**
	 * 代理service類
	 */
	private IServiceLocatorProxy serviceLocator;

	/**
	 * Constructor: 無參構造
	 */
	public AssetTypeDataLoader() {
		super();
	}
	
	/** (non-Javadoc)
	 * @see cafe.core.web.controller.dataloader.BaseInitialDataLoader#load(javax.servlet.http.HttpServletRequest, cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void load(HttpServletRequest request, SessionContext sessionContext)
			throws Exception {
		super.load(request, sessionContext);
		try{
			String ucNo = this.getUseCaseNo(sessionContext);
			IAtomsLogonUser logonUser = (IAtomsLogonUser) sessionContext.getLogonUser();
			//獲取廠商下拉選單資料
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_HARDWARE_VENDOR);
			param.addParameter(CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue(), null);
			param.addParameter(ContractSlaDTO.ATTRIBUTE.IS_HAVE_SLA.getValue(), Boolean.valueOf(false));
			SessionContext returnCtx = super.getServiceLocator().doService(logonUser, IAtomsConstants.SERVICE_COMPANY_SERVICE, IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
			List<Parameter> companyList = null;
			if(returnCtx != null){
				companyList = (List<Parameter>) returnCtx.getResponseResult();
			}
			SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_BPTD_CODE_COMPANY, companyList);
		}catch(Exception e){
			LOGGER.error(".load() is error:", "DataLoader Exception", e);
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
