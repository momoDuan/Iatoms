
package com.cybersoft4u.xian.iatoms.web.controllers.dataloader;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.dataloader.BaseInitialDataLoader;
import cafe.core.web.controller.util.SessionHelper;
import cafe.core.web.locator.IServiceLocatorProxy;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.web.controllers.ajax.IAtomsAjaxService;

/**
 * Purpose: 設備轉倉作業--確認入庫初始化載入器
 * @author ericdu
 * @since  JDK 1.7
 * @date   2016年7月18日
 * @MaintenancePersonnel ericdu
 */
public class CheckTransInfoDataLoader extends BaseInitialDataLoader<IAtomsAjaxService> {
	
	/**
	 * 系統日志記錄
	 */
	private static final Log log = CafeLogFactory.getLog(CheckTransInfoDataLoader.class);
	
	/**
	 * service代理
	 */
	private IServiceLocatorProxy serviceLocator;
	
	/**
	 * 
	 * Constructor: 無參構造
	 */
	public CheckTransInfoDataLoader() {
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
			IAtomsLogonUser logonUser = (IAtomsLogonUser)SessionHelper.getLogonUser(request);
			//獲取轉倉批號
			List<Parameter> assetTransIdList = null;
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(IAtomsLogonUser.FIELD_ID, logonUser.getId());
			SessionContext returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_CHECK_TRANS_INFO_SERVICE, IAtomsConstants.ACTION_GET_ASSET_TRANS_ID_LIST, param);
			if(returnCtx != null){
				assetTransIdList = (List<Parameter>) returnCtx.getResponseResult();
			}
			SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_BPTD_CODE_ASSET_TRANS_ID, assetTransIdList);
		}catch(Exception e){
			log.error(this.getClass().getName()+".load() is error: "+e, e);
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
