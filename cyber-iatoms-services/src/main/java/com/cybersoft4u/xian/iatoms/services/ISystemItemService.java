package com.cybersoft4u.xian.iatoms.services;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;
/**
 * Purpose: 系統參數Service interface
 * @author evanliu
 * @since  JDK 1.6
 * @date   2016年3月30日
 * @MaintenancePersonnel evanliu
 */
public interface ISystemItemService extends IAtomicService {
	/**
	 * Purpose: 初始化帳戶狀態清單
	 * @author evanliu
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return List<Parameter>:帳戶狀態List
	 */
	public List<Parameter> getAccountStatusList() throws ServiceException;
}
