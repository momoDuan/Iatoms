package com.cybersoft4u.xian.iatoms.web.controllers.dataloader;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cafe.core.bean.Parameter;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.BaseParameterInquiryContext;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.dataloader.BaseInitialDataLoader;
import cafe.core.web.controller.util.SessionHelper;
import cafe.core.web.locator.IServiceLocatorProxy;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ReportSettingDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ReportSettingFormDTO;

/**
 * Purpose: 報表發送功能設定DataLoader
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2016年7月4日
 * @MaintenancePersonnel ElvaHe
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ReportSettingDataLoader extends BaseInitialDataLoader{
	
	/**
	 * 日誌掛件
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.COMMON, ReportSettingDataLoader.class);
	
	/**
	 * service代理
	 */
	private IServiceLocatorProxy serviceLocator;
	
	/**
	 * Constructor--.
	 */
	public ReportSettingDataLoader(){
		
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.dataloader.BaseInitialDataLoader#load(javax.servlet.http.HttpServletRequest, cafe.core.context.SessionContext)
	 */
	public void load(HttpServletRequest request, SessionContext sessionContext) throws Exception {
		super.load(request, sessionContext);
		try {
			if (sessionContext != null) {
				if (sessionContext.getReturnMessage().isSuccess()) {
					// 获取登录者
					IAtomsLogonUser logonUser = (IAtomsLogonUser) SessionHelper.getLogonUser(request);
					MultiParameterInquiryContext param = new MultiParameterInquiryContext();
					param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
					//param.addParameter(CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue(), IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE);
					//獲取客戶信息
					SessionContext returnCtx = this.serviceLocator.doService(logonUser, 
							IAtomsConstants.SERVICE_COMPANY_SERVICE,
							IAtomsConstants.ACTION_GET_COMPANY_LIST, 
							param);
					List<Parameter> customerNames = (List<Parameter>) returnCtx.getResponseResult();
					SessionHelper.setAttribute(request, IAtomsConstants.UC_NO_BIM_02100, 
							IAtomsConstants.PARAM_CUSTOMER_LIST, customerNames);
					//獲取選中的報表明細
					ReportSettingFormDTO formDTO = (ReportSettingFormDTO) sessionContext.getRequestParameter();
					//獲取報表名稱編號
					String reportCode = formDTO.getReportCode();
					BaseParameterInquiryContext inquiryContext = new BaseParameterInquiryContext();
					inquiryContext.setParameterType("");
					inquiryContext.setParentItemId("reportCode");
					List<Parameter> reportDetails = null;
					//若存在報表編號
					if (StringUtils.hasText(reportCode)) {
						returnCtx = this.serviceLocator.doService(logonUser,
								IAtomsConstants.SERVICE_BASE_PARAMETER_SERVICE, 
								IAtomsConstants.ACTION_GET_PARAMETER_ITEMS, 
								inquiryContext);
						reportDetails = (List<Parameter>) returnCtx.getResponseResult();
						SessionHelper.setAttribute(request, IAtomsConstants.UC_NO_BIM_02100, IAtomsConstants.PARAM_REPORT_DETAIL_LIST, reportDetails);
					}
					//initEdit的初始化頁面需要的明細
					String actionId = sessionContext.getActionId();
					List<String> reportDetailList = null;
					if ((IAtomsConstants.ACTION_INIT_EDIT.equals(actionId)) || (IAtomsConstants.ACTION_INIT_SEND.equals(actionId))) {
						String settingId = formDTO.getSettingId();
						param.addParameter(ReportSettingDTO.ATTRIBUTE.SETTING_ID.getValue(), settingId);
						if (StringUtils.hasText(settingId)) {
							returnCtx = this.serviceLocator.doService(
									logonUser, IAtomsConstants.SERVICE_REPORT_SETTING_SERVICE, IAtomsConstants.ACTION_GET_REPORT_DETAIL_LIST_BY_ID, param);
							reportDetailList = (List<String>) returnCtx.getResponseResult();
							SessionHelper.setAttribute(request, IAtomsConstants.UC_NO_BIM_02100, IAtomsConstants.PARAM_REPORT_DETAIL_LIST, reportDetailList);
						}
					}
				}
			}
		}catch(Exception e) {
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
