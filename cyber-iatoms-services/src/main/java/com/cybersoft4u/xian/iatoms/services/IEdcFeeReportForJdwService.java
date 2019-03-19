package com.cybersoft4u.xian.iatoms.services;

import java.util.Date;

import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;
/**
 * Purpose: 维护费报表(捷达威格式)
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/8/22
 * @MaintenancePersonnel CarrieDuan
 */
public interface IEdcFeeReportForJdwService extends IAtomicService{

	/**
	 * Purpose:维护费报表(捷达威格式)
	 * @author CarrieDuan
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return void
	 */
	public void sendReportMail() throws ServiceException;
}
