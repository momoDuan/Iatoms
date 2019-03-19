package com.cybersoft4u.xian.iatoms.web.controllers.dataloader;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.dataloader.BaseInitialDataLoader;
import cafe.core.web.controller.util.SessionHelper;
import cafe.core.web.locator.IServiceLocatorProxy;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmTransactionParameterItemDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.BaseParameterManagerFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.SrmCaseTemplatesFormDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SrmCaseTemplatesDataLoader  extends BaseInitialDataLoader {
	/**
	 * 日誌掛件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(SrmCaseTemplatesDataLoader.class);
	/**
	 * Constructor--.構造器
	 */
	public SrmCaseTemplatesDataLoader(){
	}
	/**
	 * service代理
	 */
	private IServiceLocatorProxy serviceLocator;
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.dataloader.BaseInitialDataLoader#load(javax.servlet.http.HttpServletRequest, cafe.core.context.SessionContext)
	 */
	public void load(HttpServletRequest request, SessionContext sessionContext) throws Exception {
		super.load(request, sessionContext);
		try {
			SrmCaseTemplatesFormDTO formDTO = null;
			SessionContext returnCtx = null;
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) SessionHelper.getLogonUser(request);
			formDTO = (SrmCaseTemplatesFormDTO) sessionContext.getRequestParameter();
			String ucNo = formDTO.getUseCaseNo();
			Gson gsonss = new GsonBuilder().create();
			String versionDate = null;
			SimpleDateFormat sf = new SimpleDateFormat(DateTimeUtils.DT_FMT_YYYYMMDD_DASH);
			versionDate = sf.format(DateTimeUtils.getCurrentTimestamp());
			param = new MultiParameterInquiryContext();
			param.addParameter(SrmTransactionParameterItemDTO.ATTRIBUTE.EFFECTIVE_DATE.getValue(), versionDate);
			//加載交易參數數據交易參數項目list
			returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE,
					IAtomsConstants.ACTION_GET_TRANSACTION_PARAMETER_ITEM_LIST, param);
			List<Parameter> transationParameterList = (List<Parameter>) returnCtx.getResponseResult();
			SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_TRANSACTION_PARAMETER_ITEM_LIST, transationParameterList);
			//加載不同交易類別，可修改列名。
			returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE,
					IAtomsConstants.ACTION_GET_EDIT_FIELDS_GROUP_BY_TRANSACTION_TYPE, param);
			Map<String,List<String>> editFildsMap = (Map<String,List<String>>) returnCtx.getResponseResult();
			String editFildsMapString = gsonss.toJson(editFildsMap);
			SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_TRANSACTION_PARAMETER_EDIT_FIELDS_MAP, editFildsMapString);
			param = new MultiParameterInquiryContext();
			param.addParameter(BaseParameterManagerFormDTO.PARAMETER_PARENT_BPTD_CODE, IATOMS_PARAM_TYPE.TICKET_TYPE.getCode());
			param.addParameter(BaseParameterManagerFormDTO.PARAMETER_PARENT_ITEM_VALUE, "INSTALL");
			param.addParameter(BaseParameterManagerFormDTO.PARAMETER_CHILDREN_BPTD_CODE, IATOMS_PARAM_TYPE.TRANSACTION_CATEGORY.getCode());
			returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_BASE_PARAMETER_MANAGER_SERVICE, IAtomsConstants.ACTION_GET_PARAMETER_ITEMS_BY_PARENT, param);
			List<Parameter> transationTypeParameterList = (List<Parameter>) returnCtx.getResponseResult();
			String transationTypeParameterListString = gsonss.toJson(transationTypeParameterList);
			SessionHelper.setAttribute(request, ucNo, IATOMS_PARAM_TYPE.TRANSACTION_CATEGORY.getCode(), transationTypeParameterListString);
		} catch (Exception e) {
			LOGGER.error(":load(),Error——>", "DataLoader Exception", e);
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
