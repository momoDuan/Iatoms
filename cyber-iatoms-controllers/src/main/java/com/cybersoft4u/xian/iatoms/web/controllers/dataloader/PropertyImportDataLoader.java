package com.cybersoft4u.xian.iatoms.web.controllers.dataloader;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cafe.core.bean.Parameter;
import cafe.core.context.SessionContext;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.dataloader.BaseInitialDataLoader;
import cafe.core.web.controller.util.SessionHelper;
import cafe.core.web.locator.IServiceLocatorProxy;

import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.PropertyNumberImportFormDTO;
import com.cybersoft4u.xian.iatoms.web.controllers.ajax.IAtomsAjaxService;
/**
 * Purpose: 設備資料批次異動初始化載入器
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2018年5月24日
 * @MaintenancePersonnel HermanWang
 */
public class PropertyImportDataLoader extends BaseInitialDataLoader<IAtomsAjaxService> {

	/**
	 * 系統日志記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(AssetTypeDataLoader.class);
	
	/**
	 * 代理service類
	 */
	private IServiceLocatorProxy serviceLocator;

	/**
	 * Constructor: 無參構造
	 */
	public PropertyImportDataLoader() {
		super();
	}
	
	/** (non-Javadoc)
	 * @see cafe.core.web.controller.dataloader.BaseInitialDataLoader#load(javax.servlet.http.HttpServletRequest, cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void load(HttpServletRequest request, SessionContext sessionContext) throws Exception {
		super.load(request, sessionContext);
		try{
			String ucNo = this.getUseCaseNo(sessionContext);
			//獲取可修改月份下拉列表
			List<Parameter> monthYears = new ArrayList<Parameter>();
			String monthYear = null;
			for (int i = 1; i < 7; i++) {
				monthYear = DateTimeUtils.toString(DateTimeUtils.addCalendar(DateTimeUtils.getCurrentTimestamp(), 0, -i, 0), DateTimeUtils.DT_FMT_YYYYMM);
				monthYears.add(new Parameter(monthYear, monthYear));
			}
			SessionHelper.setAttribute(request, ucNo, PropertyNumberImportFormDTO.UPDATE_MONTH_YEAR_LISY, monthYears);
		}catch(Exception e){
			LOGGER.error(".load() is error:", "DataLoader Exception", e);
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
