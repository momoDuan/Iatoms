package com.cybersoft4u.xian.iatoms.web.controllers.dataloader;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cafe.core.bean.Parameter;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.SessionContext;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.dataloader.BaseInitialDataLoader;
import cafe.core.web.locator.IServiceLocatorProxy;
import cafe.workflow.web.controller.util.WfSessionHelper;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
/**
 * Purpose: 歷史轉倉 dataLoad
 * @author echomou
 * @since  JDK 1.6
 * @date   2017/2/23
 * @MaintenancePersonnel echomou
 */
public class AssetTransInfoHistoryDataLoader  extends BaseInitialDataLoader {
	/**
	 * 系统日志记录物件  
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.COMMON, AssetTransInfoDataLoader.class);
	
	/**
	 * service代理
	 */
	private IServiceLocatorProxy serviceLocator;

	/**
	 * Constructor:無參構造函數
	 */
	public AssetTransInfoHistoryDataLoader() {
		super();
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.dataloader.BaseInitialDataLoader#load(javax.servlet.http.HttpServletRequest, cafe.core.context.SessionContext)
	 */
	public void load(HttpServletRequest request, SessionContext sessionContext)
			throws Exception {
		super.load(request, sessionContext);
		try {
			//UCNO
			String ucNo =this.getUseCaseNo(sessionContext);
			//返回值
			SessionContext returnCtx = null;
			///获取登录者
			IAtomsLogonUser logonUser = ( IAtomsLogonUser ) WfSessionHelper.getLogonUser(request);
			String userId = null; 
			if (logonUser != null) {
				userId = logonUser.getId();
				userId = logonUser.getId();
				AdmUserDTO admUserDTO = logonUser.getAdmUserDTO();
				String dataAcl = admUserDTO.getDataAcl();
				if (IAtomsConstants.NO.equals(dataAcl)) {
					userId = "";
				}
			} else {
				throw new Exception(IAtomsMessageCode.LIMITED_LOGON_ID);
			}
		
			//獲取轉出倉列表
			List<Parameter> fromWarehouseList = null;
			returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_WAREHOUSE_SERVICE, IAtomsConstants.ACTION_GET_WAREHOUSE_BY_USER_ID, userId);
			if (returnCtx != null) {
				fromWarehouseList = (List<Parameter>) returnCtx.getResponseResult();
			}
			WfSessionHelper.setAttribute(request, ucNo, IAtomsConstants.ACTION_GET_WAREHOUSE_BY_USER_ID, fromWarehouseList);
			
			//獲取轉入倉列表
			List<Parameter> warehouseList = null;
			returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_WAREHOUSE_SERVICE, IAtomsConstants.ACTION_GET_WAREHOUSE_LIST, null);
			if (returnCtx != null) {
				warehouseList = (List<Parameter>) returnCtx.getResponseResult();
			}
			WfSessionHelper.setAttribute(request, ucNo, IAtomsConstants.ACTION_GET_WAREHOUSE_LIST, warehouseList);
			
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ":load(),Error——>" + e, e);
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
