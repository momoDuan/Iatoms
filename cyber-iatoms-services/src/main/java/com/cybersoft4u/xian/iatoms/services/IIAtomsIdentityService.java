package com.cybersoft4u.xian.iatoms.services;

import cafe.core.context.SessionContext;
import cafe.core.service.ServiceException;
import cafe.workflow.service.identity.IWfIdentityService;

import com.cybersoft4u.xian.iatoms.services.dao.IIAtomsIdentityDAO;


/**
 * Purpose:识别管理服务类别 interface
 * @author candicechen
 * @since  JDK 1.6
 * @date   2015年4月20日
 * @MaintenancePersonnel candicechen
 */
public interface IIAtomsIdentityService extends IWfIdentityService<IIAtomsIdentityDAO>{
	/** 
	 * Purpose: 初始化密碼設定
	 * @author evanliu
	 * @throws ServiceException：
	 * @return void
	 */
	public void initPasswordSetting() throws ServiceException; 
	
	/**
	 * Purpose:更新用戶狀態
	 * @author CrissZhang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext updateUserStatus(SessionContext sessionContext) throws ServiceException; 
}
