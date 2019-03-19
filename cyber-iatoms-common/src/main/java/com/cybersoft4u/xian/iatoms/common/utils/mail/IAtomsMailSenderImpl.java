package com.cybersoft4u.xian.iatoms.common.utils.mail;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import cafe.core.config.SystemConfigManager;
import cafe.core.util.StringUtils;
import cafe.workflow.config.WfSystemConfigManager;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
/**
 * Purpose: 寄送mail实现类 
 * @author candicechen
 * @since  JDK 1.6
 * @date   2015年8月13日
 * @MaintenancePersonnel candicechen
 */
public class IAtomsMailSenderImpl extends JavaMailSenderImpl implements InitializingBean {
	
	/**
	 * Constructor:寄送mail实现类构造函数
	 */
	public IAtomsMailSenderImpl() {
	}
	
	/**
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		try {
			String host = WfSystemConfigManager.getProperty(IAtomsConstants.MAIL, IAtomsConstants.MAIL_PROPS_HOST_NAME);
			if (StringUtils.hasText(host)) this.setHost(host);
			
			String userName = WfSystemConfigManager.getProperty(IAtomsConstants.MAIL, SystemConfigManager.PROPS_ATTR_ADMIN_ID);
			if (StringUtils.hasText(userName))this.setUsername(userName);
			
			String password = WfSystemConfigManager.getProperty(IAtomsConstants.MAIL, SystemConfigManager.PROPS_ATTR_ADMIN_PASSWORD);
			if (StringUtils.hasText(password)) this.setPassword(password);
		}catch(Exception e){}	
	}
}
