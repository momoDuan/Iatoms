package com.cybersoft4u.xian.iatoms.web.controllers.dataloader;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.dataloader.BaseInitialDataLoader;
import cafe.core.web.controller.util.SessionHelper;
import cafe.core.web.locator.IServiceLocatorProxy;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetTypeDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.RepoStatusReportFormDTO;
import com.cybersoft4u.xian.iatoms.web.controllers.ajax.IAtomsAjaxService;
/**
 * Purpose: 設備狀態報表DataLoader
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/5/3
 * @MaintenancePersonnel CarrieDuan
 */
public class RepoStatusReportInitialDataLoader extends
		BaseInitialDataLoader<IAtomsAjaxService> {
	
	/**
	 * 系統日志記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(RepoStatusReportInitialDataLoader.class);
	
	/**
	 * 代理service類
	 */
	private IServiceLocatorProxy serviceLocator;

	/**
	 * Constructor: 無參構造
	 */
	public RepoStatusReportInitialDataLoader() {
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
			//獲取formDTO
			RepoStatusReportFormDTO formDTO = (RepoStatusReportFormDTO) sessionContext.getRequestParameter();
			//獲取ucNo
			String ucNo = this.getUseCaseNo(sessionContext);
			//獲取當前登錄者
			IAtomsLogonUser logonUser = (IAtomsLogonUser) sessionContext.getLogonUser();
			MultiParameterInquiryContext param = null;
			SessionContext returnCtx = null;
			List<Parameter> paramList = null;
			//若存在formDTO
			if (formDTO != null) {
				//獲取當前登錄者的客戶編號
				String logonUserCompanyId = formDTO.getLogonUserCompanyId();
				//若存在當前登錄者的客戶編號
				if (!StringUtils.hasText(logonUserCompanyId)) {
					//獲取客戶下拉框的資料
					param = new MultiParameterInquiryContext();
					param.addParameter(CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue(), IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE);
					param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
					//調用公司service獲取公司下拉框的值
					returnCtx = (SessionContext) this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_COMPANY_SERVICE, IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
					if(returnCtx != null){
						paramList = (List<Parameter>) returnCtx.getResponseResult();
					}
				} else {
					paramList = new ArrayList<Parameter>();
					paramList.add(new Parameter(logonUser.getCompanyName(), logonUser.getCompanyId()));
				}
				SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_CUSTOMER_LIST, paramList);
				
				//設備品類名稱列表
				param = new MultiParameterInquiryContext();
				param.addParameter(AssetTypeDTO.ATTRIBUTE.DELETED.getValue(), IAtomsConstants.NO);
				param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), IAtomsConstants.ASSET_CATEGORY_RODUND_ASSET + IAtomsConstants.MARK_SEPARATOR + IAtomsConstants.ASSET_CATEGORY_EDC);
				//調用設備品項維護的獲取設備類型對應的設備列表
				returnCtx = (SessionContext) this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE, IAtomsConstants.ACTION_GET_ASSET_TYPE_LIST, param);
				if(returnCtx != null){
					paramList = (List<Parameter>) returnCtx.getResponseResult();
				}
				SessionHelper.setAttribute(request, ucNo, IATOMS_PARAM_TYPE.ASSET_NAME.getCode(), paramList);
			}
		}catch(Exception e){
			LOGGER.error("RepoStatusReportInitialDataLoader.load()", "is error:", e);
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
