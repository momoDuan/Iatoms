package com.cybersoft4u.xian.iatoms.web.listener;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.context.support.WebApplicationObjectSupport;

import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;

/**
 * Purpose: 服務啟動時,把基礎數據存儲在applcation中
 * @author RiverJin
 * @since  JDK 1.7
 * @date   2016年10月21日
 * @MaintenancePersonnel RiverJin
 */
public class InstantiationTracingBeanPostProcessor
										// 繼承該類可以獲取到application對象
										extends WebApplicationObjectSupport 
										// 實現該類可以實現服務啟動後執行一些必要的自定義程式
										implements ApplicationListener<ContextRefreshedEvent> {
	/**
	 * 系統日誌記錄物件
	 */
	private static final CafeLog logger = CafeLogFactory.getLog(InstantiationTracingBeanPostProcessor.class);
	
	/**
	 * 是否已經對基礎數據進行了加載操作
	 */
	public boolean isLoadBaseData = false;
	/**
	 * java application對象
	 */
	private ServletContext application;
	
	/**
	 * (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			// 獲取application存儲對象(來源:父類)
			application = getWebApplicationContext().getServletContext();
			// 從application中取出是否已經加載過基礎數據的標記
			Object object = application.getAttribute(IAtomsConstants.IS_LOAD_BASEDATA_FALG);
			if(object != null){
				isLoadBaseData = (Boolean) object;
			}
			if(!isLoadBaseData){
				// 加載基礎數據
				// 第一步: 根據id(xml中配置的spring bean的id)獲取由spring負責管理的在xml中注入的bean對象
				//IAdmUserService admUserService = (IAdmUserService) event.getApplicationContext().getBean("admUserService");
				// 第二步: 通過service查詢 DB數據 或者通過其他路徑得到的數據
				//AdmUser admUser = admUserService.getRiverApplicationFunction();
				// 第三步: 將查詢好的的基礎數據存儲到application對象中去
				//application.setAttribute("riverJin", admUser);
				// 第四步: 加載過基礎數據以後把加載過的標記存儲在application中以防止多次加載
				this.setIsLoadBaseData();
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ".onApplicationEvent() is error " + e);
		}
	}
	
	public void setIsLoadBaseData(){
		application.setAttribute(IAtomsConstants.IS_LOAD_BASEDATA_FALG,true);
	}

}
