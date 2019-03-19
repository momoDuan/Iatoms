package com.cybersoft4u.xian.iatoms.common.async;

import cafe.core.bean.Message;
import cafe.core.bean.dto.AbstractFormDTO;
import cafe.core.context.SessionContext;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.locator.IServiceLocatorProxy;

/**
 * Purpose: iatoms 多線程處理
 * @author evanliu
 * @since  JDK 1.6
 * @date   2017年5月25日
 * @MaintenancePersonnel evanliu
 */
public class IAtomsAsynchronousHandler<P extends AbstractFormDTO> extends Thread {
	/**
	 * Loger
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(IAtomsAsynchronousHandler.class);
	/**
	 * service代理
	 */
	private IServiceLocatorProxy serviceLocator;
	/**
	 * formDTO
	 */
	private P formDTO;
	/**
	 * 服務名
	 */
	private String serviceName;
	/**
	 * 處理方法名
	 */
	private String actionName;
	/**
	 * 是否執行失敗
	 */
	private boolean isExcuFailure;
	
	/**
	 * Constructor:無餐構造
	 */
	public IAtomsAsynchronousHandler(){
	}
	/**
	 * Constructor:建構子
	 * @param formDTO formDTO
	 * @param serviceName 服務名
	 * @param actionName 處理方法名
	 */
	public IAtomsAsynchronousHandler(P formDTO, String serviceName, String actionName) {
		this.formDTO = formDTO;
		this.actionName = actionName;
		this.serviceName = serviceName;
	}
	
	/**
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			if (StringUtils.hasText(serviceName) && StringUtils.hasText(actionName) && null != formDTO) {
				SessionContext ctx = this.serviceLocator.doService(null, serviceName, actionName, formDTO);
				if(ctx != null && ctx.getReturnMessage().getStatus() == Message.STATUS.FAILURE){
					this.setIsExcuFailure(true);
				}
			} else {
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("run() is failed! paramters is null, serviceName= ")
					.append(serviceName).append(",actionName=").append(actionName)
					.append(",formDTO=").append(formDTO);
				LOGGER.error(stringBuilder);
				this.setIsExcuFailure(true);
			}			
		} catch (Exception e) {
			this.setIsExcuFailure(true);
			LOGGER.error(".run() is failed！！！ " + e);
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

	/**
	 * @return the formDTO
	 */
	public P getFormDTO() {
		return formDTO;
	}

	/**
	 * @param formDTO the formDTO to set
	 */
	public void setFormDTO(P formDTO) {
		this.formDTO = formDTO;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * @return the actionName
	 */
	public String getActionName() {
		return actionName;
	}

	/**
	 * @param actionName the actionName to set
	 */
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	/**
	 * @return the isExcuSuccess
	 */
	public boolean getIsExcuFailure() {
		return isExcuFailure;
	}
	/**
	 * @param isExcuSuccess the isExcuSuccess to set
	 */
	public void setIsExcuFailure(boolean isExcuFailure) {
		this.isExcuFailure = isExcuFailure;
	}
}
