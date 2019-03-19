package com.cybersoft4u.xian.iatoms.web.controllers.dataloader;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cafe.core.bean.Parameter;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.SessionContext;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.dataloader.BaseInitialDataLoader;
import cafe.core.web.controller.util.SessionHelper;
import cafe.core.web.locator.IServiceLocatorProxy;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AdmRoleFormDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Purpose: 系統角色管理DataLoader
 * @author CarrieDuan
 * @since  JDK 1.6
 * @date   2016/6/29
 * @MaintenancePersonnel CarrieDuan
 */
public class AdmRoleDataLoader extends BaseInitialDataLoader {
	/**
	 * 日誌記錄器
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.COMMON, AdmRoleDataLoader.class);

	/**
	 * service代理
	 */
	private IServiceLocatorProxy serviceLocator;

	/**
	 * Constructor:無參構造
	 */
	public AdmRoleDataLoader() {
	}

	/** (non-Javadoc)
	 * @see cafe.core.web.controller.dataloader.BaseInitialDataLoader#load(javax.servlet.http.HttpServletRequest, cafe.core.context.SessionContext)
	 */
	@Override
	public void load(HttpServletRequest request, SessionContext sessionContext)
			throws Exception {
		super.load(request, sessionContext);
		try {
			if (sessionContext != null) {
				// 获取登录者
				IAtomsLogonUser logonUser = (IAtomsLogonUser) SessionHelper.getLogonUser(request);
				//獲取ucNO
				String ucNo = this.getUseCaseNo(sessionContext);
				if (sessionContext.getReturnMessage().isSuccess()) {
					Gson gsonss = new GsonBuilder().create();
					AdmRoleFormDTO admRoleFormDTO = (AdmRoleFormDTO) sessionContext.getRequestParameter();
					Map<String,List<String>> editFildsMap = admRoleFormDTO.getFunctionPermissions();
					String editFildsMapString = gsonss.toJson(editFildsMap);
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_FUNCTION_PERMISSION_MAP, editFildsMapString);
					SessionContext returnCtx = this.serviceLocator.doService(
							logonUser, IAtomsConstants.SERVICE_ADM_ROLE_SERVICE,
							IAtomsConstants.ACTION_GET_ROLE_CODE, null);
					// 角色代號列表
					List<Parameter> admRoleCodeList = (List<Parameter>) returnCtx.getResponseResult();
					SessionHelper.setAttribute(request, ucNo, AdmRoleFormDTO.PARAM_ADM_ROLE_CODE_LIST, admRoleCodeList);
					
					//角色屬性列表
					List<Parameter> admRoleAttributeList = (List<Parameter>) SessionHelper.getAttribute(request,
							ucNo, IATOMS_PARAM_TYPE.ROLE_ATTRIBUTE.getCode());
					//功能權限列表
					List<Parameter> accessRightList = (List<Parameter>) SessionHelper.getAttribute(request, 
							ucNo, IATOMS_PARAM_TYPE.FUNCTION_TYPE.getCode());
					//將權限轉換為小寫
					for (Parameter parameter : accessRightList) {
						parameter.setValue(((String) parameter.getValue()).toLowerCase());
					}
					
					//将集合转化为JSON字符串
					String admRoleAttributeListString = gsonss.toJson(admRoleAttributeList);
					SessionHelper.setAttribute(request, ucNo,
							AdmRoleFormDTO.PARAM_ADM_ROLE_ATTRIBUTE_LIST, admRoleAttributeListString);
					String accessRightListString = gsonss.toJson(accessRightList);
					SessionHelper.setAttribute(request, ucNo, 
												AdmRoleFormDTO.PARAM_ACCESS_RIGHT_LIST, accessRightListString);
					returnCtx = this.serviceLocator.doService(
							logonUser, IAtomsConstants.SERVICE_ADM_ROLE_SERVICE,
							IAtomsConstants.ACTION_GET_PARENT_FUNCTION, null);
					List<Parameter> parentFunctionList = (List<Parameter>) returnCtx.getResponseResult();
					String parentFunctionListString = gsonss.toJson(parentFunctionList);
					SessionHelper.setAttribute(request, ucNo,
							AdmRoleFormDTO.PARAM_PARENT_FUNCTION_LIST, parentFunctionListString);
				}
			}
		} catch (Exception e) {
			LOGGER.error("load()", "Error——>", e);
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
