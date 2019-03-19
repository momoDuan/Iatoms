package com.cybersoft4u.xian.iatoms.web.controllers.dataloader;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AdmRoleFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.SystemLogFormDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cafe.core.bean.Parameter;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.SessionContext;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.dataloader.BaseInitialDataLoader;
import cafe.core.web.controller.util.SessionHelper;
import cafe.core.web.locator.IServiceLocatorProxy;


/**
 * Purpose: 系統角色管理DataLoader
 * @author CarrieDuan
 * @since  JDK 1.6
 * @date   2016/6/29
 * @MaintenancePersonnel CarrieDuan
 */
public class SystemLogDataLoader extends BaseInitialDataLoader {
	/**
	 * 日誌記錄器
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.COMMON, SystemLogDataLoader.class);

	/**
	 * service代理
	 */
	private IServiceLocatorProxy serviceLocator;

	/**
	 * Constructor:無參構造
	 */
	public SystemLogDataLoader() {
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
				
				Gson gsonss = new GsonBuilder().create();
				SystemLogFormDTO formDTO = (SystemLogFormDTO) sessionContext.getRequestParameter();
				Map<String,List<String>> editFildsMap = formDTO.getFunctionPermissions();
				String editFildsMapString = gsonss.toJson(editFildsMap);
				SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_FUNCTION_PERMISSION_MAP, editFildsMapString);
				
				//功能權限列表
				List<Parameter> accessRightList = (List<Parameter>) SessionHelper.getAttribute(request, 
						ucNo, IATOMS_PARAM_TYPE.FUNCTION_TYPE.getCode());
				//將權限轉換為小寫
				for (Parameter parameter : accessRightList) {
					parameter.setValue(((String) parameter.getValue()).toLowerCase());
				}
				String accessRightListString = gsonss.toJson(accessRightList);
				SessionHelper.setAttribute(request, ucNo, 
											AdmRoleFormDTO.PARAM_ACCESS_RIGHT_LIST, accessRightListString);
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
