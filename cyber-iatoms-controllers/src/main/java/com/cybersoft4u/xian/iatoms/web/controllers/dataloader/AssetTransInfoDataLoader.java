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
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetTransInfoFormDTO;
/**
 * Purpose: 設備轉倉作業轉出dataloader
 * @author Aamnda Wang
 * @since  JDK 1.6
 * @date   2016/7/19
 * @MaintenancePersonnel Aamnda Wang
 */
public class AssetTransInfoDataLoader extends BaseInitialDataLoader {
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
	public AssetTransInfoDataLoader() {
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
			IAtomsLogonUser logonUser = (IAtomsLogonUser) SessionHelper.getLogonUser(request);
			String userId = logonUser.getId(); 
			//當前登錄者信息
			AdmUserDTO admUserDTO = null;
			//倉庫控管標誌
			String dataAcl = null;
			if (logonUser != null) {
				userId = logonUser.getId();
				admUserDTO = logonUser.getAdmUserDTO();
				dataAcl = admUserDTO.getDataAcl();
			} else {
				throw new Exception(IAtomsMessageCode.LIMITED_LOGON_ID);
			}
			//獲取轉出倉列表
			List<Parameter> fromWarehouseList = null;
			//如果使用者中不進行倉庫控管，則得到所有倉庫列表
			/*if (IAtomsConstants.NO.equals(dataAcl)) {
				userId = "";
			}*/
			returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_WAREHOUSE_SERVICE, IAtomsConstants.ACTION_GET_WAREHOUSE_BY_USER_ID, userId);
			if (returnCtx != null) {
				fromWarehouseList = (List<Parameter>) returnCtx.getResponseResult();
			}
			SessionHelper.setAttribute(request, ucNo, IAtomsConstants.ACTION_GET_WAREHOUSE_BY_USER_ID, fromWarehouseList);
			
			//獲取轉入倉列表
			List<Parameter> warehouseList = null;
			returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_WAREHOUSE_SERVICE, IAtomsConstants.ACTION_GET_WAREHOUSE_LIST, null);
			if (returnCtx != null) {
				warehouseList = (List<Parameter>) returnCtx.getResponseResult();
			}
			SessionHelper.setAttribute(request, ucNo, IAtomsConstants.ACTION_GET_WAREHOUSE_LIST, warehouseList);
			
			//獲取轉出轉倉批號列表
			List<Parameter> assetTransIdList = null;
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(IAtomsLogonUser.FIELD_ID, userId);
			param.addParameter(AssetTransInfoFormDTO.TAB_TYPE, AssetTransInfoFormDTO.TAB_ASSET_TRANFER_OUT);
			returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_DMM_ASSET_TRANS_INFO_SERVICE, IAtomsConstants.ACTION_GET_ASSET_TRANS_ID_LIST, param);
			if (returnCtx != null) {
				assetTransIdList = (List<Parameter>) returnCtx.getResponseResult();
			}
			SessionHelper.setAttribute(request, ucNo, IAtomsConstants.ACTION_GET_ASSET_TRANS_ID_LIST, assetTransIdList);
			//獲取轉入驗收轉倉批號列表
			param = new MultiParameterInquiryContext();
			param.addParameter(IAtomsLogonUser.FIELD_ID, userId);
			param.addParameter(AssetTransInfoFormDTO.TAB_TYPE, AssetTransInfoFormDTO.TAB_ASSET_TRANFER_IN_CHECK);
			returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_DMM_ASSET_TRANS_INFO_SERVICE, IAtomsConstants.ACTION_GET_ASSET_TRANS_ID_LIST, param);
			if (returnCtx != null) {
				assetTransIdList = (List<Parameter>) returnCtx.getResponseResult();
			}
			SessionHelper.setAttribute(request, ucNo, AssetTransInfoFormDTO.PARAM_LIST_CHECK_TRANST_INFO, assetTransIdList);
			//獲得通知人員列表
			//returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_DMM_ASSET_TRANS_INFO_SERVICE, IAtomsConstants.ACTION_GET_WARE_HOUSE_USER_NAME_LIST, null);
			//List<Parameter> userList = (List<Parameter>) returnCtx.getResponseResult();
			//WfSessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_WARE_HOUSE_USER_NAME_LIST, userList);
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
