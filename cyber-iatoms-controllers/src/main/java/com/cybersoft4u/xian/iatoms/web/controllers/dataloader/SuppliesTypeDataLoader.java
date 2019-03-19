package com.cybersoft4u.xian.iatoms.web.controllers.dataloader;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.dataloader.BaseInitialDataLoader;
import cafe.core.web.controller.util.SessionHelper;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ContractSlaDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.SuppliesTypeFormDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/**
 * Purpose: 獲取耗材品維護下拉框的dataloader
 * @author Hermanwang
 * @since  JDK 1.6
 * @date   2016/8/25
 * @MaintenancePersonnel Hermanwang
 */
public class SuppliesTypeDataLoader extends BaseInitialDataLoader {
	/**
	 * 日誌掛件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(SuppliesTypeDataLoader.class);
	/**
	 * Constructor--.構造器
	 */
	public SuppliesTypeDataLoader(){
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.dataloader.BaseInitialDataLoader#load(javax.servlet.http.HttpServletRequest, cafe.core.context.SessionContext)
	 */
	public void load(HttpServletRequest request, SessionContext sessionContext) throws Exception {
		super.load(request, sessionContext);
		try {
			SuppliesTypeFormDTO formDTO = null;
			IAtomsLogonUser logonUser = (IAtomsLogonUser) SessionHelper.getLogonUser(request);
			formDTO = (SuppliesTypeFormDTO) sessionContext.getRequestParameter();
			String ucNo = IAtomsConstants.MARK_EMPTY_STRING;
			if(formDTO != null) {
				ucNo = formDTO.getUseCaseNo();
			}
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
			param.addParameter(CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue(), IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE);
			param.addParameter(ContractSlaDTO.ATTRIBUTE.IS_HAVE_SLA.getValue(), Boolean.valueOf(false));
			//bimCompanyDTO.setCompanyType(IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
			SessionContext returnCtx = super.getServiceLocator().doService(logonUser, IAtomsConstants.SERVICE_COMPANY_SERVICE, IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
			//獲取查詢條件耗材品下拉框.
			List<Parameter> suppliesList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.SUPPLIES_TYPE.getCode());
			//獲取查詢條件客戶下拉框.
			List<Parameter> customerList = (List<Parameter>) returnCtx.getResponseResult();
			Gson gson = new GsonBuilder().create();
			//查詢條件和頁面對應起來
			SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_CUSTOMER_LIST, customerList);
			//轉換成json格式，頁面結果集下拉框需要json格式
			String customerToJson = gson.toJson(customerList);
			//查詢結果和頁面對應起來
			SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_CUSTOMER_JSON, customerToJson);
			//查詢條件和頁面對應起來
			SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_SUPPLIES_LIST, suppliesList);	
			//轉換成json格式，頁面結果集下拉框需要json格式
			String suppliesToJson = gson.toJson(suppliesList);
			//查詢結果和頁面對應起來
			SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_SUPPLIES_JSON, suppliesToJson);
		} catch (Exception e) {
			LOGGER.error(":load(),Error——>", "DataLoader Exception", e);
			throw new Exception(IAtomsMessageCode.BASE_PARAMETER_VALUE_IS_NULL, e);
		}

	}
	
}
