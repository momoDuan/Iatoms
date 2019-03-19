package com.cybersoft4u.xian.iatoms.web.controllers.dataloader;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.util.StringUtils;
import cafe.core.web.controller.dataloader.BaseInitialDataLoader;
import cafe.core.web.controller.util.SessionHelper;
import cafe.core.web.locator.IServiceLocatorProxy;

import com.ctc.wstx.util.StringUtil;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.DmmAssetBorrowFormDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AssetBorrowDataLoader extends BaseInitialDataLoader {
	/**
	 * log
	 */
	private static final Log LOGGER = LogFactory.getLog(AssetBorrowDataLoader.class);
	
	/**
	 * service代理
	 */
	private IServiceLocatorProxy serviceLocator;
	
	/**
	 * Constructor--無參構造函數
	 */
	public AssetBorrowDataLoader(){
		
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
				if (IAtomsConstants.UC_NO_DMM_03130.equals(ucNo)) {
					List<Parameter> assetCategory = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, 
							IAtomsConstants.PARAM_BPTD_CODE_ASSET_CATEGORY);
					Gson gsonss = new GsonBuilder().create();
					//将集合转化为JSON字符串
					String assetCategoryList = gsonss.toJson(assetCategory);
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_ASSET_CATEGORY_LIST, assetCategoryList);
				} else {
					DmmAssetBorrowFormDTO assetBorrowFormDTO = (DmmAssetBorrowFormDTO) sessionContext.getResponseResult();
					String loginRole = assetBorrowFormDTO.getLoginRoles();
					if (StringUtils.hasText(loginRole)) {
						MultiParameterInquiryContext param = new MultiParameterInquiryContext();
						param.addParameter(DmmAssetBorrowFormDTO.PARAM_LOGIN_ROLES, loginRole);
						sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_ASSET_BORROW_SERVICE, 
								IAtomsConstants.ACTION_GET_BORROW_CASE_ID, param);
						List<Parameter> parameters = (List<Parameter>) sessionContext.getResponseResult();
						SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_ASSET_BORROW_CASE_ID, parameters);
					}
				}
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
