package com.cybersoft4u.xian.iatoms.services.impl;

import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.LogFileDownloadFormDTO;
import com.cybersoft4u.xian.iatoms.services.ILogFileDownloadService;

import cafe.core.bean.Message;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.SessionContext;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

/**
 * Purpose:錯誤記錄檔下載Serivce
 * @author NickLin 
 * @since  JDK 1.7
 * @date   2018/07/27
 * @MaintenancePersonnel CyberSoft
 */
public class LogFileDownloadService extends AtomicService implements ILogFileDownloadService {

	/**
	 * 系统log物件
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.DAO, LogFileDownloadService.class);
	
	/**
	 * Constructor:無參數建構子
	 */
	public LogFileDownloadService() {
		super();
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ILogFileDownloadService#init(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SessionContext init(SessionContext sessionContext)
			throws ServiceException {
		// TODO Auto-generated method stub
		LogFileDownloadFormDTO logFileDownloadFormDTO = null;
		try {
			logFileDownloadFormDTO = (LogFileDownloadFormDTO) sessionContext.getRequestParameter();
			sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS));
			sessionContext.setResponseResult(logFileDownloadFormDTO);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+".init():" + e, e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
}
