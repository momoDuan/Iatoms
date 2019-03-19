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
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractAssetDTO;
import com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService;
/**
 * Purpose: DTID號碼管理初始化頁面數據加載
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/2/4
 * @MaintenancePersonnel CarrieDuan
 */
public class DtidDefDataLoader extends BaseInitialDataLoader<IIAtomsAjaxService> {
	
	/**
	 * 日誌記錄器
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(DtidDefDataLoader.class);
	
	/**
	 * service代理
	 */
	private IServiceLocatorProxy serviceLocator;

	/**
	 * Constructor:無參構造函數
	 */
	public DtidDefDataLoader() {
		super();
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.dataloader.BaseInitialDataLoader#load(javax.servlet.http.HttpServletRequest, cafe.core.context.SessionContext)
	 */
	public void load(HttpServletRequest request, SessionContext sessionContext) throws Exception {
		try {
			super.load(request, sessionContext);
			if (sessionContext != null) {
				String ucNo = this.getUseCaseNo(sessionContext);
				MultiParameterInquiryContext param = new MultiParameterInquiryContext();
				//獲取客戶下拉菜單
				param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
				param.addParameter(CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue(), IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE);
				//bug2445,增加DTID驗證方式爲自動生成的
				param.addParameter(CompanyDTO.ATTRIBUTE.DTID_TYPE.getValue(), IAtomsConstants.PARAM_IATOMS_DTID_TYPE_AUTO);
				SessionContext returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_COMPANY_SERVICE,
						IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
				List<Parameter> customers = (List<Parameter>) returnCtx.getResponseResult();
				SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_CUSTOMER_LIST, customers);
				
				//獲取設備名稱下拉菜單
				param.addParameter(BimContractAssetDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), IAtomsConstants.ASSET_CATEGORY_EDC);
				returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE,
						IAtomsConstants.ACTION_GET_ASSET_TYPE_LIST, param);
				List<Parameter> assetNames = (List<Parameter>) returnCtx.getResponseResult();
				SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_METHOD_GET_ASSET_LIST, assetNames);
			
				//測試
				
			}
		}catch(Exception e) {
			LOGGER.error(":Error in DeviceStockDataLoader.load,Error——>", e);
			throw new Exception(e);
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
