package com.cybersoft4u.xian.iatoms.web.controllers.dataloader;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.Parameter;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.MultiParameterInquiryContext;
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
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.PaymentFormDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/**
 * Purpose: 求償作業DataLoader
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/2/6
 * @MaintenancePersonnel CarrieDuan
 */
public class PaymentDataLoader extends BaseInitialDataLoader{

	/**
	 * 日誌掛件
	 */
	private static CafeLog logger = CafeLogFactory.getLog(GenericConfigManager.COMMON, PaymentDataLoader.class);
	
	/**
	 * service代理
	 */
	private IServiceLocatorProxy serviceLocator;

	/**
	 * Constructor:無參構造函數
	 */
	public PaymentDataLoader() {
		super();
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
					//獲取ucNO
					String ucNo = this.getUseCaseNo(sessionContext);
					//獲取formDTO
					PaymentFormDTO claimWorkFormDTO = (PaymentFormDTO) sessionContext.getRequestParameter();
					MultiParameterInquiryContext multiParameterInquiryContext = new MultiParameterInquiryContext();
					//獲取客戶下拉選單
					multiParameterInquiryContext.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
					multiParameterInquiryContext.addParameter(CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue(), IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE);
					SessionContext returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_COMPANY_SERVICE, IAtomsConstants.ACTION_GET_COMPANY_LIST, multiParameterInquiryContext );
					List<Parameter> companyList = (List<Parameter>)returnCtx.getResponseResult();
					SessionHelper.setAttribute(request, ucNo, PaymentFormDTO.PARAM_COMPANY_LIST, companyList);
					//獲取耗材分類列表
					List<Parameter> suppliesTypes = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.SUPPLIES_TYPE.getCode());
					//将集合转化为JSON字符串
					Gson gsonss = new GsonBuilder().create();
					String suppliesTypeString = gsonss.toJson(suppliesTypes);
					SessionHelper.setAttribute(request, ucNo,
							PaymentFormDTO.PARAM_SUPPLIES_TYPE_STRING, suppliesTypeString);
					//獲取求償方式下拉列表
					List<Parameter> payType = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.PAYMENT_TYPE.getCode());
					//将集合转化为JSON字符串
					String payTypeString = gsonss.toJson(payType);
					SessionHelper.setAttribute(request, ucNo,
							PaymentFormDTO.PARAM_PAY_TYPE_STRING, payTypeString);
					//資料狀態下拉列表
					List<Parameter> dataStatus = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.PAYMENT_STATUS.getCode());
					List<Parameter> temps = new ArrayList<Parameter>();
					for (Parameter status : dataStatus) {
						if (IAtomsConstants.DATA_STATUS_BACK.equals(status.getValue()) || IAtomsConstants.DATA_STATUS_COMPLETE.equals(status.getValue())) {
							temps.add(status);
						}
					}
					if (!CollectionUtils.isEmpty(temps)) {
						for (Parameter parameter : temps) {
							dataStatus.remove(parameter);
						}
					}
				}
			}
		}catch(Exception e) {
			logger.error(this.getClass().getName() + ":load(),Error——>" + e, e);
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
