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
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractAssetDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetStacktakeFormDTO;
/**
 * Purpose: 設備盤點歷史查詢dataloader
 * @author Aamnda Wang
 * @since  JDK 1.6
 * @date   2016/7/19
 * @MaintenancePersonnel Aamnda Wang
 */
public class AssetStocktakeHistoryDataLoader extends BaseInitialDataLoader {
	/**
	 * 系统日志记录物件  
	 */
	private static CafeLog logger = CafeLogFactory.getLog(GenericConfigManager.COMMON, AssetStocktakeHistoryDataLoader.class);
	
	/**
	 * service代理
	 */
	private IServiceLocatorProxy serviceLocator;

	/**
	 * Constructor::無參構造函數
	 */
	public AssetStocktakeHistoryDataLoader() {
		super();
	}

	public void load(HttpServletRequest request, SessionContext sessionContext)
			throws Exception {
		super.load(request, sessionContext);
		try {
			//UCNO
			String ucNo =this.getUseCaseNo(sessionContext);
			//返回值
			SessionContext returnCtx = null;
			//获取登录者
			//IAtomsLogonUser logonUser = (IAtomsLogonUser)WfSessionHelper.getLogonUser(request);
			//獲取倉庫集合
			List<Parameter> stocktackIdList = null;
			returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_DMM_ASSET_STOCKTACK_HISTORY_SERVICE, IAtomsConstants.ACTION_GET_STOCKTACK_ID_LIST, null);
			if (returnCtx != null) {
				stocktackIdList = (List<Parameter>) returnCtx.getResponseResult();
			}
			SessionHelper.setAttribute(request, ucNo, IAtomsConstants.ACTION_GET_STOCKTACK_ID_LIST, stocktackIdList);
			//獲取設備名稱
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(BimContractAssetDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), IAtomsConstants.ASSET_CATEGORY_EDC);
			returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE, IAtomsConstants.ACTION_GET_ASSET_TYPE_LIST, param );
			List<Parameter> assetNameList = (List<Parameter>)returnCtx.getResponseResult();
			SessionHelper.setAttribute(request, ucNo, AssetStacktakeFormDTO.PARAM_ASSET_NAME_LIST, assetNameList);
			
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
