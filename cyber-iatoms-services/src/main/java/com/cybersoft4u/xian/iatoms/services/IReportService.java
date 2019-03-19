package com.cybersoft4u.xian.iatoms.services;

import java.util.Date;

import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

/**
 * Purpose: 報表公用接口.
 * @author MooreChen
 * @since  JDK 1.7
 * @date   2017/3/16
 * @MaintenancePersonnel CarrieDuan
 */
public interface IReportService extends IAtomicService {

	/**
	 * Purpose:發送報表mail公用方法
	 * @author MooreChen
	 * @param sendDate:發送時間
	 * @param customerId:客戶編號
     * @param reportCode:報表編號
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return void
	 */
	public void sendReportMail(Date sendDate,String customerId, String reportCode) throws ServiceException;

}
