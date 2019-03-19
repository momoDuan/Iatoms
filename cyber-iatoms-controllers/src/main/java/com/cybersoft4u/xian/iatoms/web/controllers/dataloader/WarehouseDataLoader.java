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
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;

/**
 * Purpose:倉庫據點維護 Dataloader
 * @author ElvaHe
 * @since JDK 1.6
 * @date 2016年6月2日
 * @MaintenancePersonnel ElvaHe
 */
public class WarehouseDataLoader extends BaseInitialDataLoader {
	/**
	 * 日誌記錄器
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.COMMON, WarehouseDataLoader.class);

	/**
	 * service代理
	 */
	private IServiceLocatorProxy serviceLocator;

	/**
	 * Constructor:無參構造
	 */
	public WarehouseDataLoader() {
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.dataloader.BaseInitialDataLoader#load(javax.servlet.http.HttpServletRequest,
	 *      cafe.core.context.SessionContext)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void load(HttpServletRequest request, SessionContext sessionContext) throws Exception {
		super.load(request, sessionContext);
		try {
			if (sessionContext != null) {
				if (sessionContext.getReturnMessage().isSuccess()) {
					//獲取廠商下拉選單資料
					MultiParameterInquiryContext param = new MultiParameterInquiryContext();
					param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_VENDOR);
					SessionContext returnCtx = this.serviceLocator.doService(
							null, IAtomsConstants.SERVICE_COMPANY_SERVICE,
							IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
					// 廠商列表
					List<Parameter> listParameter = (List<Parameter>) returnCtx.getResponseResult();
					SessionHelper.setAttribute(request,
							IAtomsConstants.UC_NO_BIM_02050,
							IAtomsConstants.PARAM_COMPANY_LIST, listParameter);
				}
			}
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
