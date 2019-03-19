package com.cybersoft4u.xian.iatoms.web.controllers.dataloader;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ContractSlaDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmTransactionParameterItemDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetManageFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractManageFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractSlaFormDTO;

/**
 * Purpose: 設備管理作業
 * @author jasonzhou
 * @since  JDK 1.6
 * @date   2016-7-18
 * @MaintenancePersonnel jasonzhou
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class AssetManageDataLoader extends BaseInitialDataLoader {
	
	/**
	 * 日誌掛件
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.COMMON, AssetManageDataLoader.class);
	
	/**
	 * service代理
	 */
	private IServiceLocatorProxy serviceLocator;
	
	/**
	 * Constructor--無參構造
	 */
	public AssetManageDataLoader(){
		super();
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.dataloader.BaseInitialDataLoader#load(javax.servlet.http.HttpServletRequest, cafe.core.context.SessionContext)
	 */
	public void load(HttpServletRequest request, SessionContext sessionContext) throws Exception {
		super.load(request, sessionContext);
		try {
			long startTime = System.currentTimeMillis();
			if (sessionContext != null) {
				AssetManageFormDTO formDTO = (AssetManageFormDTO) sessionContext.getRequestParameter();

				String ucNo = this.getUseCaseNo(sessionContext);
				List<Parameter> customerList = null;
				IAtomsLogonUser logonUser = (IAtomsLogonUser) sessionContext.getLogonUser();
				String userId = null;
				//獲取用戶ID
				if (logonUser != null) {
					userId = logonUser.getId();
				} else {
					throw new Exception(IAtomsMessageCode.LIMITED_LOGON_ID);
				}
				//獲取廠商下拉選單資料
				MultiParameterInquiryContext param = new MultiParameterInquiryContext();
				MultiParameterInquiryContext param1 = new MultiParameterInquiryContext();
				param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_VENDOR);
				SessionContext returnCtx = this.serviceLocator.doService(logonUser, 
																				IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE,
																				IAtomsConstants.ACTION_GET_COMPANY_PARAMETER_LIST,
																				param);
				List<Parameter> vendorList = null;
				if(returnCtx != null){
					vendorList = (List<Parameter>) returnCtx.getResponseResult();
				}
				SessionHelper.setAttribute(request, ucNo, AssetManageFormDTO.VENDOR_LIST, vendorList);
				//獲取硬體廠商下拉選單資料
				param = new MultiParameterInquiryContext();
				param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_HARDWARE_VENDOR);
				returnCtx = this.serviceLocator.doService(logonUser, 
																				IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE,
																				IAtomsConstants.ACTION_GET_COMPANY_PARAMETER_LIST,
																				param);
				List<Parameter> repairVendorList = null;
				if(returnCtx != null){
					repairVendorList = (List<Parameter>) returnCtx.getResponseResult();
				}
				SessionHelper.setAttribute(request, ucNo, AssetManageFormDTO.REPAIR_VENDOR_LIST, repairVendorList);
				
				//獲取維護廠商下拉選單資料
				param = new MultiParameterInquiryContext();
				param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_VENDOR);
				returnCtx = this.serviceLocator.doService(logonUser, 
																				IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE,
																				IAtomsConstants.ACTION_GET_COMPANY_PARAMETER_LIST,
																				param);
				List<Parameter> repairdVendorList = null;
				if(returnCtx != null){
					repairdVendorList = (List<Parameter>) returnCtx.getResponseResult();
				}
				SessionHelper.setAttribute(request, ucNo, AssetManageFormDTO.REPAIRD_VENDOR_LIST, repairdVendorList);
				
				List<Parameter> paramList = null;
				//設備品類名稱列表
				param = new MultiParameterInquiryContext();
				//param.addParameter(AssetTypeDTO.ATTRIBUTE.DELETED.getValue(), IAtomsConstants.NO);
				returnCtx = this.serviceLocator.doService(logonUser, 
																				IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE,
																				IAtomsConstants.PARAM_ASSET_LIST,
																				param);
				if(returnCtx != null){
					paramList = (List<Parameter>) returnCtx.getResponseResult();
				}
				SessionHelper.setAttribute(request, ucNo, IATOMS_PARAM_TYPE.ASSET_NAME.getCode(), paramList);
			
				List<Parameter> listParameter = (List<Parameter>) returnCtx.getResponseResult();
				SessionHelper.setAttribute(request, ucNo, 
						AssetManageFormDTO.GET_ASSET_TYPE_LIST, listParameter);
				//獲取登入者角色權限如果包含廠商角色就依廠商角色顯示
				List<AdmRoleDTO> roleCodes =logonUser.getUserFunctions();
				String roleFlag = null;
				if (CollectionUtils.isEmpty(roleCodes)) {
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.ACTION_GET_WAREHOUSE_BY_USER_ID, null);
				} else {
					roleFlag = IAtomsConstants.PARAM_YES;
					List<Parameter> warehouseList = null;
					Parameter attribute = new Parameter();
					List<Parameter> attributeList = new ArrayList<Parameter>();
					for (AdmRoleDTO admRoleDTO : roleCodes) {
						if (StringUtils.pathEquals(admRoleDTO.getAttribute(), IAtomsConstants.VECTOR_ROLE_ATTRIBUTE)) {
							roleFlag = IAtomsConstants.PARAM_NO;
							break;
						} 
					}
					//廠商角色
					if (StringUtils.pathEquals(roleFlag, IAtomsConstants.PARAM_NO)) {
						attribute.setName(IAtomsConstants.PARAM_YES);
						attribute.setValue(IAtomsConstants.MARK_EMPTY_STRING);
						attributeList.add(attribute);
						SessionHelper.setAttribute(request, ucNo, AdmRoleDTO.ATTRIBUTE.ATTRIBUTE.getValue(),  attributeList);
						//獲取倉庫列表
						returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_WAREHOUSE_SERVICE, IAtomsConstants.ACTION_GET_WAREHOUSE_BY_USER_ID, userId);
						if (returnCtx != null) {
							warehouseList = (List<Parameter>) returnCtx.getResponseResult();
						}
						SessionHelper.setAttribute(request, ucNo, IAtomsConstants.ACTION_GET_WAREHOUSE_BY_USER_ID, warehouseList);
						//獲取公司
						param1.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
						SessionContext returnCtx2 = this.serviceLocator.doService(logonUser, 
																						IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE,
																						IAtomsConstants.ACTION_GET_COMPANY_PARAMETER_LIST,
																						param1);
						if(returnCtx2 != null){
							customerList = (List<Parameter>) returnCtx2.getResponseResult();
						}
						SessionHelper.setAttribute(request, ucNo, AssetManageFormDTO.CUSTOMER_LIST, customerList);
						
					} else {
						attribute.setName(IAtomsConstants.PARAM_NO);
						attribute.setValue(logonUser.getAdmUserDTO().getCompanyId());
						attributeList.add(attribute);
						SessionHelper.setAttribute(request, ucNo, AdmRoleDTO.ATTRIBUTE.ATTRIBUTE.getValue(),  attributeList);
						//客戶角色--獲取倉庫列表
						returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_WAREHOUSE_SERVICE, IAtomsConstants.ACTION_GET_WAREHOUSE_LIST, null);
						if (returnCtx != null) {
							warehouseList = (List<Parameter>) returnCtx.getResponseResult();
						}
						SessionHelper.setAttribute(request, ucNo, IAtomsConstants.ACTION_GET_WAREHOUSE_BY_USER_ID, warehouseList);
						//獲取公司
						param1.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_ID.getValue(), logonUser.getAdmUserDTO().getCompanyId());
						SessionContext returnCtx2 = this.serviceLocator.doService(logonUser, 
																						IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE,
																						IAtomsConstants.ACTION_GET_COMPANY_PARAMETER_LIST,
																						param1);
						if(returnCtx2 != null){
							customerList = (List<Parameter>) returnCtx2.getResponseResult();
						}
						SessionHelper.setAttribute(request, ucNo, AssetManageFormDTO.CUSTOMER_LIST, customerList);
					}
				}
				//如果是客戶角色 //Task #3046
				/*if (StringUtils.pathEquals(roleFlag, IAtomsConstants.PARAM_YES)) {
					param.addParameter(ContractSlaFormDTO.ORIGINAL_CUSTOMER_ID, logonUser.getCompanyId());
				}*/
				//獲取合約編號
				List<Parameter> contractList = null;
				param.addParameter(IAtomsConstants.PARAM_PAGE_ORDER, IAtomsConstants.PARAM_PAGE_ORDER);
				param.addParameter(IAtomsConstants.QUERY_PAGE_ORDER, ContractManageFormDTO.PARAM_ORDER_BY_CONTRACT_CODE);
				returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.PARAM_CONTRACT_MANAGE_SERVICE, IAtomsConstants.ACTION_GET_CONTRACT_CODE_LIST, param);
				if (returnCtx != null) {
					contractList = (List<Parameter>) returnCtx.getResponseResult();
				}
				SessionHelper.setAttribute(request, ucNo, IAtomsConstants.ACTION_GET_CONTRACT_LIST, contractList);
				//獲取合約編號
				/*returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_ASSET_IN_SERVICE, IAtomsConstants.ACTION_GET_CONTRACT_LIST, param);
				if (returnCtx != null) {
					contractList = (List<Parameter>) returnCtx.getResponseResult();
				}
				SessionHelper.setAttribute(request, ucNo, IAtomsConstants.ACTION_GET_CONTRACT_LIST, contractList);
				*/	
				//獲取資產OWNER和使用人下拉選單資料
				/*MultiParameterInquiryContext param2 = new MultiParameterInquiryContext();
				
				param2.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
				SessionContext returnCtx2 = this.serviceLocator.doService(logonUser, 
																				IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE,
																				IAtomsConstants.ACTION_GET_COMPANY_PARAMETER_LIST,
																				param2);*/
				List<String> params = new ArrayList<String>();
				params.add(IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
				params.add(IAtomsConstants.PARAM_COMPANY_TYPE_HARDWARE_VENDOR);
				param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), params);
				SessionContext returnCtx2 = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_COMPANY_SERVICE,
						IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
				if(returnCtx2 != null){
					customerList = (List<Parameter>) returnCtx2.getResponseResult();
				}
				SessionHelper.setAttribute(request, ucNo, AssetManageFormDTO.CUSTOMER_AND_VENDOR_LIST, customerList);
				// 公司列表
				MultiParameterInquiryContext param3 = new MultiParameterInquiryContext();
				param3.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), null);
				//param3.addParameter(CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue(), IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE);
				param3.addParameter(ContractSlaDTO.ATTRIBUTE.IS_HAVE_SLA.getValue(), Boolean.valueOf(false));
				
				SessionContext returnCtx3 = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_COMPANY_SERVICE, IAtomsConstants.ACTION_GET_COMPANY_LIST, param3);
				List<Parameter> parameters = null;
				if(returnCtx3 != null){
					parameters = (List<Parameter>) returnCtx3.getResponseResult();
				}
				SessionHelper.setAttribute(request, ucNo, IAtomsConstants.ACTION_GET_COMPANY_LIST, parameters);
				//詳情頁面
				if (IAtomsConstants.ACTION_DETAIL.equals(formDTO.getActionId())) {
					String versionDate = null;
					versionDate = DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDD_DASH);
					//加載交易參數數據交易參數項目list
					param = new MultiParameterInquiryContext();
					param.addParameter(SrmTransactionParameterItemDTO.ATTRIBUTE.EFFECTIVE_DATE.getValue(), versionDate);
					returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE,
							IAtomsConstants.ACTION_GET_TRANSACTION_PARAMETER_ITEM_LIST, param);
					List<Parameter> transationParameterList = (List<Parameter>) returnCtx.getResponseResult();
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_TRANSACTION_PARAMETER_ITEM_LIST, transationParameterList);
				}
			}
			long endTime = System.currentTimeMillis();
			LOGGER.debug(this.getClass().getName() + "load", " end ", (endTime - startTime));
		}catch(Exception e) {
			LOGGER.error(this.getClass().getName() + ".load is error " + e);
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
