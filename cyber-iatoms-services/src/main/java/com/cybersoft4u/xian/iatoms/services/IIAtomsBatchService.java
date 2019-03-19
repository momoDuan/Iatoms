package com.cybersoft4u.xian.iatoms.services;

import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;
/**
 * Purpose: 批次處理service接口
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2017/5/8
 * @MaintenancePersonnel HermanWang
 */
public interface IIAtomsBatchService  extends IAtomicService {
	/**
	 * Purpose:系统需于每月1日0:00，開始執行設備資料複製至月表中.
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServerException：出錯時, 丟出ServerException
	 * @return SessionContext:上下文SessionContext
	 */
	public void copyAssetHistoryToMonthTable () throws ServiceException;
	/**
	 * Purpose:系統于每日05：00，開始執行清除臨時文件目錄作業
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServerException：出錯時, 丟出ServerException
	 * @return SessionContext:上下文SessionContext
	 */
	public void cleanTempFiles () throws ServiceException;
	/**
	 * Purpose:根據配置的月份數，將月份數之前的案件資料轉移到案件歷史資料檔中
	 * @author amandawang
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return void
	 */
	public void transferCaseToHistory() throws ServiceException;
	
	/**
	 * Purpose:配置系統LOG限制月份，刪除系統LOG
	 * @author CrissZhang
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return void
	 */
	public void deleteHistoryLog() throws ServiceException;
	
	/**
	 * Purpose:處理系統備份或刪除期限
	 * @author CrissZhang
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return void
	 */
	public void dealSystemLimit() throws ServiceException;
}
