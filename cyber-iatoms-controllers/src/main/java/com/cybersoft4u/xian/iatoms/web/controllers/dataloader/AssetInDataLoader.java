package com.cybersoft4u.xian.iatoms.web.controllers.dataloader;

import java.util.ArrayList;
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
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetInInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetInInfoFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractManageFormDTO;
import com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService;
/**
 * 
 * Purpose: 设备入库DataLoader加载器
 * @author hungli
 * @since  JDK 1.6
 * @date   2016/5/12
 * @MaintenancePersonnel cybersoft
 */
public class AssetInDataLoader extends BaseInitialDataLoader<IIAtomsAjaxService> {

	/**
	 * 日誌記錄器
	 */
	private static final CafeLog log = CafeLogFactory.getLog(AssetInDataLoader.class);

	/**
	 * service代理
	 */
	private IServiceLocatorProxy serviceLocator;
	
	/**
	 * Constructor:构造函数
	 */
	public AssetInDataLoader() {
		super();
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.dataloader.BaseInitialDataLoader#load(javax.servlet.http.HttpServletRequest, cafe.core.context.SessionContext)
	 */
	public void load(HttpServletRequest request, SessionContext sessionContext) throws Exception {
		try {
			super.load(request, sessionContext);
			//UCNO
			String ucNo =this.getUseCaseNo(sessionContext);
			//登錄者
			IAtomsLogonUser logonUser = (IAtomsLogonUser)SessionHelper.getLogonUser(request);
			//返回值
			SessionContext returnCtx = null;
			//查詢參數
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			//獲取廠商集合
			List<Parameter> vendorlist = null;
			param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_HARDWARE_VENDOR);
			returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_COMPANY_SERVICE,
					IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
			if (returnCtx != null) {
				vendorlist = (List<Parameter>)returnCtx.getResponseResult();
			}
			SessionHelper.setAttribute(request, ucNo, IAtomsConstants.ACTION_GET_VENDOR_LIST, vendorlist);
			//獲取客戶集合
			List<Parameter> customerList = null;
			
			param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
			returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_COMPANY_SERVICE,
					IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
			if (returnCtx != null) {
				customerList = (List<Parameter>)returnCtx.getResponseResult();
			}
			SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_COMPANY_LIST, customerList);
			List<String> params = new ArrayList<String>();
			params.add(IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
			params.add(IAtomsConstants.PARAM_COMPANY_TYPE_HARDWARE_VENDOR);
			param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), params);
			returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_COMPANY_SERVICE,
					IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
			if (returnCtx != null) {
				customerList = (List<Parameter>)returnCtx.getResponseResult();
			}
			SessionHelper.setAttribute(request, ucNo, AssetInInfoFormDTO.PARAM_ASSETS_OWNER_AND_USE_EMPLOYEE_NAME_LIST, customerList);
			
			
			//獲取倉庫集合
			List<Parameter> getStorageList = null;
			returnCtx = this.serviceLocator.doService(logonUser,IAtomsConstants.SERVICE_WAREHOUSE_SERVICE, IAtomsConstants.ACTION_GET_WAREHOUSE_LIST, null);
			if (returnCtx != null) {
				getStorageList = (List<Parameter>) returnCtx.getResponseResult();
			}
			SessionHelper.setAttribute(request, ucNo, IAtomsConstants.ACTION_GET_WAREHOUSE_LIST, getStorageList);
			//獲取未入庫批號集合
			List<Parameter> getPreBatchNoList = null;
			param.addParameter(AssetInInfoDTO.ATTRIBUTE.IS_DONE.getValue(), IAtomsConstants.NO);
			returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_ASSET_IN_SERVICE, IAtomsConstants.ACTION_GET_ASSET_IN_ID_LIST, param);
			if (returnCtx != null) {
				getPreBatchNoList = (List<Parameter>) returnCtx.getResponseResult();
			}
			SessionHelper.setAttribute(request, ucNo, IAtomsConstants.ACTION_GET_ASSET_IN_ID_LIST, getPreBatchNoList);
			//獲取已入庫批號集合
			List<Parameter> getBatchNoList = null;
			param.addParameter(AssetInInfoDTO.ATTRIBUTE.IS_DONE.getValue(), IAtomsConstants.YES);
			returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_ASSET_IN_SERVICE, IAtomsConstants.ACTION_GET_ASSET_IN_ID_LIST, param);
			if (returnCtx != null) {
				getBatchNoList = (List<Parameter>) returnCtx.getResponseResult();
			}
			SessionHelper.setAttribute(request, ucNo, IAtomsConstants.ACTION_LIST_ASSET_IN_LIST, getBatchNoList);
			
			//獲取合約編號
			List<Parameter> contractList = null;
			param.addParameter(IAtomsConstants.QUERY_PAGE_ORDER, ContractManageFormDTO.PARAM_ORDER_BY_CONTRACT_CODE);
			param.addParameter(IAtomsConstants.PARAM_PAGE_ORDER, IAtomsConstants.PARAM_PAGE_ORDER);
			//param.addParameter(BimContractDTO.ATTRIBUTE.CONTRACT_STATUS.getValue(), IAtomsConstants.PARAM_CONTRACT_STATUS_IN_EFFECT);
			returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.PARAM_CONTRACT_MANAGE_SERVICE, IAtomsConstants.ACTION_GET_CONTRACT_CODE_LIST, param);
			if (returnCtx != null) {
				contractList = (List<Parameter>) returnCtx.getResponseResult();
			}
			SessionHelper.setAttribute(request, ucNo, IAtomsConstants.ACTION_GET_CONTRACT_LIST, contractList);
			
			
		}catch(Exception e) {
			log.error(this.getClass().getName()+":Error in DeviceStockDataLoader.load,Error——>"+e,e);
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
