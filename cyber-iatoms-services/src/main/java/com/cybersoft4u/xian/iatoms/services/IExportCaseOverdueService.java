package com.cybersoft4u.xian.iatoms.services;

import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;
/**
 * Purpose: 批次通知工程師回應、到場、完修逾期情況接口
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2018年6月12日
 * @MaintenancePersonnel CarrieDuan
 */
public interface IExportCaseOverdueService extends IAtomicService{
	/**
	 * Purpose: 查詢即將逾期案件
	 * @author CarrieDuan
	 * @throws ServiceException :出錯時, 丟出ServiceException
	 * @return void
	 */
	public void queryCaseInfo() throws ServiceException;
}
