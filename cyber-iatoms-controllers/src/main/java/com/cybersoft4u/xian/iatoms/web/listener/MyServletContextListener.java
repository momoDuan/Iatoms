package com.cybersoft4u.xian.iatoms.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;

import cafe.core.service.locator.ServiceManager;

/**
 * 
 * Purpose: 實現ServletContextListener 接口，在啟動服務時加載該程序
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2017-9-19
 * @MaintenancePersonnel CrissZhang
 */
public class MyServletContextListener implements ServletContextListener {

	/**
	 * (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	/**
	 * (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// 啟動服務時加載bean
		ServiceManager.getInstance().getBean(IAtomsConstants.SERVICE_ADM_USER_SERVICE);
	}
	
}

