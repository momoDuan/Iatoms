package com.cybersoft4u.xian.iatoms.web.controllers.dataloader;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

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
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AdmUserFormDTO;

/**
 * Purpose: 使用者維護DataLoader 
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016年3月30日
 * @MaintenancePersonnel CrissZhang
 */
public class AdmUserDataLoader extends BaseInitialDataLoader {
	/**
	 * log
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(AdmUserDataLoader.class);
	
	/**
	 * service代理
	 */
	private IServiceLocatorProxy serviceLocator;
	
	/**
	 * Constructor--.
	 */
	public AdmUserDataLoader(){
		
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.dataloader.BaseInitialDataLoader#load(javax.servlet.http.HttpServletRequest, cafe.core.context.SessionContext)
	 */
	public void load(HttpServletRequest request, SessionContext sessionContext) throws Exception {
		super.load(request, sessionContext);
		try {
			if (sessionContext != null && sessionContext.getReturnMessage() != null) {
				if (sessionContext.getReturnMessage().isSuccess()) {
					String ucNo = this.getUseCaseNo(sessionContext);
					AdmUserFormDTO admUserFormDTO = (AdmUserFormDTO) sessionContext.getRequestParameter();
					MultiParameterInquiryContext param = new MultiParameterInquiryContext();
					// 获取登录者
					IAtomsLogonUser logonUser = (IAtomsLogonUser) SessionHelper.getLogonUser(request);
					//角色列表
					SessionContext returnCtx = this.serviceLocator.doService(logonUser, 
							IAtomsConstants.SERVICE_ADM_ROLE_SERVICE,
							IAtomsConstants.ACTION_GET_ROLE_LIST, null );
					List<Parameter> listParameter = (List<Parameter>) returnCtx.getResponseResult();
					SessionHelper.setAttribute(request, ucNo, AdmUserFormDTO.PARAM_ROLE_LIST, listParameter);
					//公司列表	
					returnCtx = this.serviceLocator.doService(logonUser, 
							IAtomsConstants.SERVICE_COMPANY_SERVICE,
							IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
					listParameter = (List<Parameter>) returnCtx.getResponseResult();
					SessionHelper.setAttribute(request, ucNo, AdmUserFormDTO.PARAM_COMPANY_LIST, listParameter);
					if (!IAtomsConstants.ACTION_INIT.equals(sessionContext.getActionId())) {
						// 仓库据点列表
						returnCtx = this.serviceLocator.doService(logonUser, 
								IAtomsConstants.SERVICE_WAREHOUSE_SERVICE,
								IAtomsConstants.ACTION_GET_WAREHOUSE_BY_USER_ID, IAtomsConstants.MARK_EMPTY_STRING);
						listParameter = (List<Parameter>) returnCtx.getResponseResult();
						SessionHelper.setAttribute(request, ucNo, AdmUserFormDTO.PARAM_WAREHOUSE_LIST, listParameter);
					}					
					//初始化用戶角色信息
					if (IAtomsConstants.ACTION_INIT_EDIT.equals(sessionContext.getActionId())) {
						String userId = admUserFormDTO.getUserId();
						param.addParameter(AdmUserDTO.ATTRIBUTE.COMPANY_ID.getValue(), admUserFormDTO.getCompanyId());
						// 修改初始化
						if (StringUtils.hasText(userId)) {
							//查詢用戶角色信息
							returnCtx = this.serviceLocator.doService(logonUser, 
									IAtomsConstants.SERVICE_ADM_ROLE_SERVICE,
									IAtomsConstants.ACTION_GET_ROLE_LIST_BY_USER_ID, userId );
							listParameter = (List<Parameter>) returnCtx.getResponseResult();
							SessionHelper.setAttribute(request, ucNo, AdmUserFormDTO.PARAM_USER_ROLE_LIST, listParameter);
							//查詢用戶控管信息
							returnCtx = this.serviceLocator.doService(logonUser, 
									IAtomsConstants.SERVICE_WAREHOUSE_SERVICE,
									IAtomsConstants.ACTION_GET_WAREHOUSE_BY_USER_ID, userId );
							listParameter = (List<Parameter>) returnCtx.getResponseResult();
							SessionHelper.setAttribute(request, ucNo, AdmUserFormDTO.PARAM_USER_WAREHOUSE_LIST, listParameter);
						}
						//Bug #2359 update by 2017/09/12
						if (admUserFormDTO!= null && admUserFormDTO.getAdmUserDTO() != null && admUserFormDTO.getAdmUserDTO().getCompanyId() != null) {
							param.addParameter(AdmUserDTO.ATTRIBUTE.COMPANY_ID.getValue(), admUserFormDTO.getAdmUserDTO().getCompanyId());
							returnCtx = this.serviceLocator.doService(logonUser, 
									IAtomsConstants.SERVICE_BIM_DEPARTMENT_SERVICE,
									IAtomsConstants.ACTION_GET_DEPT_LIST, param );
							listParameter = (List<Parameter>) returnCtx.getResponseResult();
							SessionHelper.setAttribute(request, ucNo, AdmUserFormDTO.PARAM_DEPT_LIST, listParameter);
						}						
					} else {
						SessionHelper.setAttribute(request, ucNo, AdmUserFormDTO.PARAM_USER_ROLE_LIST, null);
						SessionHelper.setAttribute(request, ucNo, AdmUserFormDTO.PARAM_USER_WAREHOUSE_LIST, null);						
					}
				}
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
