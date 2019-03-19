package com.cybersoft4u.xian.iatoms.services;

import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

/**
 * Purpose:批次匯出案件資訊範本(派工給華經資訊)Service接口
 * @author nicklin
 * @since  JDK 1.7
 * @date   2018/03/22
 * @MaintenancePersonnel cybersoft
 */
public interface IExportCaseTemplateForFisService extends IAtomicService {
	/**
	 * Purpose:每日23:30 將建案內容有異動的案件資訊匯出成excel
	 * @author nicklin
	 * @throws ServerException：出錯時, 丟出ServerException
	 */
	public void exportCaseTemplate() throws ServiceException;
}
