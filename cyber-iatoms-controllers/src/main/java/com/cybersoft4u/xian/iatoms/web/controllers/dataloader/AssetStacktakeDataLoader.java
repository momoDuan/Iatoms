package com.cybersoft4u.xian.iatoms.web.controllers.dataloader;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.web.controller.dataloader.BaseInitialDataLoader;
import cafe.core.web.controller.util.SessionHelper;
import cafe.core.web.locator.IServiceLocatorProxy;
import cafe.workflow.web.controller.util.WfSessionHelper;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractAssetDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetStacktakeFormDTO;


/**
 * Purpose:設備盤點DataLoader 
 * @author allenchen
 * @since  JDK 1.7
 * @date   2016-7-15
 * @MaintenancePersonnel allenchen
 */
@SuppressWarnings("rawtypes")
public class AssetStacktakeDataLoader extends BaseInitialDataLoader {
	/**
	 * log
	 */
	private static final Log logger = LogFactory.getLog(AssetStacktakeDataLoader.class);
	
	/**
	 * service代理
	 */
	private IServiceLocatorProxy serviceLocator;
	
	/**
	 * Constructor--.
	 */
	public AssetStacktakeDataLoader(){
		
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.dataloader.BaseInitialDataLoader#load(javax.servlet.http.HttpServletRequest, cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "unchecked" })
	public void load(HttpServletRequest request, SessionContext sessionContext) throws Exception {
		super.load(request, sessionContext);
		try {
			if (sessionContext != null) {
				//UCNO
				String ucNo =this.getUseCaseNo(sessionContext);
				///获取登录者
				IAtomsLogonUser logonUser = (IAtomsLogonUser)WfSessionHelper.getLogonUser(request);
				String userId = null; 
				if (logonUser != null) {
					userId = logonUser.getId();
				} else {
					throw new Exception(IAtomsMessageCode.LIMITED_LOGON_ID);
				}
				SessionContext returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_DMM_ASSET_TRANS_INFO_SERVICE, IAtomsConstants.ACTION_GET_WAREHOUSE_BY_USER_ID, null);
				//獲取倉庫列表
				List<Parameter> warehouseList = null;
				returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_WAREHOUSE_SERVICE, IAtomsConstants.ACTION_GET_WAREHOUSE_BY_USER_ID, "");
				if (returnCtx != null) {
					warehouseList = (List<Parameter>) returnCtx.getResponseResult();
				}
				WfSessionHelper.setAttribute(request, ucNo, IAtomsConstants.ACTION_GET_WAREHOUSE_BY_USER_ID, warehouseList);
				//獲取設備名稱
				MultiParameterInquiryContext param = new MultiParameterInquiryContext();
				param.addParameter(BimContractAssetDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), null);
				returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE, IAtomsConstants.ACTION_GET_ASSET_TYPE_LIST, param );
				List<Parameter> assetNameList = (List<Parameter>)returnCtx.getResponseResult();
				SessionHelper.setAttribute(request, ucNo, AssetStacktakeFormDTO.PARAM_ASSET_NAME_LIST, assetNameList);
				
				//盤點批號list
				returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_DMM_ASSET_STACKTAKE_SERVICE, AssetStacktakeFormDTO.PARAM_METHOD_GET_INVENTORY_NUMBER_LIST, null);
				List<Parameter> listParameter = (List<Parameter>)returnCtx.getResponseResult();
				WfSessionHelper.setAttribute(request, ucNo, AssetStacktakeFormDTO.PARAM_METHOD_GET_INVENTORY_NUMBER_LIST, listParameter);
			}
		}catch(Exception e) {
			logger.error(this.getClass().getName()+":load(),Error——>" + e, e);
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
