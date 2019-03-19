package com.cybersoft4u.xian.iatoms.web.controllers.dataloader;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.web.controller.dataloader.BaseInitialDataLoader;
import cafe.core.web.controller.util.SessionHelper;
import cafe.core.web.locator.IServiceLocatorProxy;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractManageFormDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 * Purpose: 合约维护DataLoader
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2016/5/18
 * @MaintenancePersonnel CarrieDuan
 */
@SuppressWarnings("rawtypes")
public class ContractDataLoader extends BaseInitialDataLoader {
	/**
	 * log
	 */
	private static final Log LOGGER = LogFactory.getLog(ContractDataLoader.class);
	
	/**
	 * service代理
	 */
	private IServiceLocatorProxy serviceLocator;
	
	/**
	 * Constructor--無參構造函數
	 */
	public ContractDataLoader(){
		
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.dataloader.BaseInitialDataLoader#load(javax.servlet.http.HttpServletRequest, cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "unchecked"})
	public void load(HttpServletRequest request, SessionContext sessionContext) throws Exception {
		super.load(request, sessionContext);
		try {
			if (sessionContext != null) {
				String ucNo = this.getUseCaseNo(sessionContext);
				// 获取登录者
				IAtomsLogonUser logonUser = (IAtomsLogonUser) SessionHelper.getLogonUser(request);
				MultiParameterInquiryContext param1 = new MultiParameterInquiryContext();
				//客户列表
				param1.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
				SessionContext returnCtx = super.getServiceLocator().doService(logonUser, IAtomsConstants.SERVICE_COMPANY_SERVICE, IAtomsConstants.ACTION_GET_COMPANY_LIST, param1);
				List<Parameter> listParameter = (List<Parameter>) returnCtx.getResponseResult();
				SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_CUSTOMER_LIST, listParameter);
				//厂商列表
				MultiParameterInquiryContext param = new MultiParameterInquiryContext();
				param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_VENDOR);
				returnCtx = super.getServiceLocator().doService(logonUser, IAtomsConstants.SERVICE_COMPANY_SERVICE, IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
				listParameter = (List<Parameter>) returnCtx.getResponseResult();
				SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_MANU_FACTURER_LIST, listParameter);
				//廠商列表的value集合
				List<String> companyValues = new ArrayList<String>();
				if (!CollectionUtils.isEmpty(listParameter)) {
					for (Parameter paramrter : listParameter) {
						companyValues.add((String) paramrter.getValue());
					}
				}
				SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_MANU_FACTURER_VALUE_LIST, companyValues);
				List<Parameter> assetCategory = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, 
														IAtomsConstants.PARAM_BPTD_CODE_ASSET_CATEGORY);
				Gson gsonss = new GsonBuilder().create();
				//将集合转化为JSON字符串
			    String assetCategoryList = gsonss.toJson(assetCategory);
			    SessionHelper.setAttribute(request, ucNo,
						ContractManageFormDTO.PARAM_ASSET_CATEGORY_LIST, assetCategoryList);	
			}
		}catch(Exception e) {
			LOGGER.error("load(),Error——>", e);
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
