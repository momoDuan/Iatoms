package com.cybersoft4u.xian.iatoms.web.controllers.dataloader;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cafe.core.bean.Parameter;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.dataloader.BaseInitialDataLoader;
import cafe.core.web.controller.util.SessionHelper;
import cafe.core.web.locator.IServiceLocatorProxy;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractAssetDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmTransactionParameterItemDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.BaseParameterManagerFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.EdcParameterFormDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/**
 * Purpose: EDC交易參數DataLoader
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016/9/22
 * @MaintenancePersonnel CrissZhang
 */
public class EdcParameterDataLoader extends BaseInitialDataLoader {

	/**
	 * 系统日志记录物件  
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.COMMON, EdcParameterDataLoader.class);
	
	/**
	 * service代理
	 */
	private IServiceLocatorProxy serviceLocator;

	/**
	 * Constructor:無參構造函數
	 */
	public EdcParameterDataLoader() {
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
				// 客戶類型
				param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
				// iatoms驗證客戶
				param.addParameter(CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue(), IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE);
				SessionContext returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_COMPANY_SERVICE, IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
				List<Parameter> listParameter = (List<Parameter>) returnCtx.getResponseResult();
				SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_COMPANY_LIST, listParameter);
				//獲取客戶下拉選單資料
				param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), null);
				returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_COMPANY_SERVICE, IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
				listParameter = (List<Parameter>) returnCtx.getResponseResult();
				SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_CUSTOMER_LIST, listParameter);
				// EDC設備列表
				param.addParameter(BimContractAssetDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), IAtomsConstants.ASSET_CATEGORY_EDC);
				returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE, IAtomsConstants.ACTION_GET_ASSET_TYPE_LIST, param);
				listParameter = (List<Parameter>) returnCtx.getResponseResult();
				SessionHelper.setAttribute(request, ucNo, EdcParameterFormDTO.PARAM_EDC_ASSET_LIST, listParameter);
				// 周邊設備列表
				param.addParameter(BimContractAssetDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), IAtomsConstants.ASSET_CATEGORY_RODUND_ASSET);
				returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE, IAtomsConstants.ACTION_GET_ASSET_TYPE_LIST, param);
				listParameter = (List<Parameter>) returnCtx.getResponseResult();
				SessionHelper.setAttribute(request, ucNo, EdcParameterFormDTO.PARAM_CATEGORY_RODUND_ASSET_LIST, listParameter);
				
				
				String versionDate = DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDD_DASH);
				// 交易类别
				param = new MultiParameterInquiryContext();
				param.addParameter(BaseParameterManagerFormDTO.PARAMETER_PARENT_BPTD_CODE, IATOMS_PARAM_TYPE.TICKET_TYPE.getCode());
				param.addParameter(BaseParameterManagerFormDTO.PARAMETER_PARENT_ITEM_VALUE, IAtomsConstants.CASE_CATEGORY.INSTALL.getCode());
				param.addParameter(BaseParameterManagerFormDTO.PARAMETER_CHILDREN_BPTD_CODE, IATOMS_PARAM_TYPE.TRANSACTION_CATEGORY.getCode());
				param.addParameter(BaseParameterManagerFormDTO.EDIT_EFFECITVE_DATE, DateTimeUtils.parseDate(versionDate, DateTimeUtils.DT_FMT_YYYYMMDD_DASH));
				returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_BASE_PARAMETER_MANAGER_SERVICE, IAtomsConstants.ACTION_GET_PARAMETER_ITEMS_BY_PARENT, param);
				listParameter = (List<Parameter>) returnCtx.getResponseResult();
				SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_TRANS_CATEGORY_LIST, listParameter);
				
				//加載交易參數數據交易參數項目list
				param = new MultiParameterInquiryContext();
				param.addParameter(SrmTransactionParameterItemDTO.ATTRIBUTE.EFFECTIVE_DATE.getValue(), versionDate);
				returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE,
						IAtomsConstants.ACTION_GET_TRANSACTION_PARAMETER_ITEM_LIST, param);
				List<Parameter> transationParameterList = (List<Parameter>) returnCtx.getResponseResult();
				SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_TRANSACTION_PARAMETER_ITEM_LIST, transationParameterList);
				
				//加載不同交易類別，可修改列名。
				Gson gsonss = new GsonBuilder().create();
				param = new MultiParameterInquiryContext();
				param.addParameter(SrmTransactionParameterItemDTO.ATTRIBUTE.EFFECTIVE_DATE.getValue(), versionDate);
				returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE,
						IAtomsConstants.ACTION_GET_EDIT_FIELDS_GROUP_BY_TRANSACTION_TYPE, param);
				Map<String,List<String>> editFildsMap = (Map<String,List<String>>) returnCtx.getResponseResult();
				String editFildsMapString = gsonss.toJson(editFildsMap);
				SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_TRANSACTION_PARAMETER_EDIT_FIELDS_MAP, editFildsMapString);
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
