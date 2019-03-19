package com.cybersoft4u.xian.iatoms.web.controllers.dataloader;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cafe.core.bean.Parameter;
import cafe.core.context.SessionContext;
import cafe.core.web.controller.dataloader.BaseInitialDataLoader;
import cafe.core.web.controller.util.SessionHelper;
import cafe.core.web.locator.IServiceLocatorProxy;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.MailListManageFormDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * 
 * Purpose: 電子郵件羣組維護DataLoader
 * @author CarrieDuan	
 * @since  JDK 1.7
 * @date   2016-7-4
 * @MaintenancePersonnel CarrieDuan
 */
public class MailListManageDataLoader extends BaseInitialDataLoader {
	/**
	 * log
	 */
	private static final Log LOGGER = LogFactory.getLog(MailListManageDataLoader.class);
	
	/**
	 * service代理
	 */
	private IServiceLocatorProxy serviceLocator;
	
	/**
	 * Constructor--.
	 */
	public MailListManageDataLoader(){
		
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.dataloader.BaseInitialDataLoader#load(javax.servlet.http.HttpServletRequest, cafe.core.context.SessionContext)
	 */
	public void load(HttpServletRequest request, SessionContext sessionContext) throws Exception {
		super.load(request, sessionContext);
		try {
			if (sessionContext != null) {
				//獲取ucNO
				String ucNo = this.getUseCaseNo(sessionContext);
				//獲取郵件通知種類
				List<Parameter> mailGroups = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IAtomsConstants.MAIL_GROUP);
				Gson gsonss = new GsonBuilder().create();
				//将集合转化为JSON字符串
			    String mailGroupString = gsonss.toJson(mailGroups);
			    SessionHelper.setAttribute(request, ucNo,
						MailListManageFormDTO.PARAM_MAIL_GROUP_STRING, mailGroupString);
					
			}
		}catch(Exception e) {
			LOGGER.error(":load(),Error——>", e);
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
