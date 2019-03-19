package com.cybersoft4u.xian.iatoms.web.controllers;

import java.io.File;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.LogFileDownloadFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.FileUtils;

import cafe.core.bean.Message;
import cafe.core.exception.CommonException;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;

/**
 * Purpose:錯誤記錄檔下載Controller
 * @author NickLin 
 * @since  JDK 1.7
 * @date   2018/07/27
 * @MaintenancePersonnel CyberSoft
 */
public class LogFileDownloadController extends AbstractMultiActionController<LogFileDownloadFormDTO> {
	
	/**
	 * 日誌記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(LogFileDownloadController.class);
	
	/**
	 * Constructor: 無參數建構子
	 */
	public LogFileDownloadController() {
		this.setCommandClass(LogFileDownloadFormDTO.class);
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(LogFileDownloadFormDTO command) throws CommonException {
		// TODO Auto-generated method stub
		if (command == null) {
			return false;
		}
		// 獲取actionId
		String actionId = command.getActionId();
		Message msg = null;
		String logFileName = command.getQueryLogFileName();
		if (IAtomsConstants.ACTION_DOWNLOAD.equals(actionId)) {
			//檔案類型
			if (!StringUtils.hasText(logFileName)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_LOG_FILE_DOWNLOAD_LOG_FILE_NAME)});
				throw new CommonException(msg);
			}
		}
		return true;
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public LogFileDownloadFormDTO parse(HttpServletRequest request,
			LogFileDownloadFormDTO command) throws CommonException {
		// TODO Auto-generated method stub
		try {
			if (command == null) {
				command = new LogFileDownloadFormDTO();
			}
			//獲取actionId
			String actionId = this.getString(request, IAtomsConstants.FIELD_ACTION_ID);
			if(!StringUtils.hasText(actionId)){
				actionId = IAtomsConstants.ACTION_INIT;
			} else if (IAtomsConstants.ACTION_DOWNLOAD.equals(actionId)) {
				String logFileType = this.getString(request, LogFileDownloadFormDTO.QUERY_LOG_FILE_TYPE);
				LOGGER.debug("LogFileDownloadController.parse() --> logFileType: " + logFileType);
				String logFileName = this.getString(request, LogFileDownloadFormDTO.QUERY_LOG_FILE_NAME);
				LOGGER.debug("LogFileDownloadController.parse() --> logFileName: " + logFileName);
				command.setQueryLogFileType(logFileType);
				command.setQueryLogFileName(logFileName);
			}
			LOGGER.debug("LogFileDownloadController.parse() --> actionId: " + actionId);
			command.setActionId(actionId);
		} catch(Exception e) {
			LOGGER.error("LogFileDownloadController.parse() is error: " + e, e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
		return command;
	}
	
	/**
	 * Purpose:刷卡機標籤匯入格式下載
	 * @author NickLin
	 * @param request：請求
	 * @param response：響應
	 * @param command：LogFileDownloadFormDTO對象
	 * @throws CommonException:出錯時抛出CommonException例外
	 * @return void：無返回值
	 */
	@SuppressWarnings("rawtypes")
	public void download(HttpServletRequest request, HttpServletResponse response, LogFileDownloadFormDTO command) throws CommonException {
		try {
			if (command != null) {
				String filePath = null;
				String fileType = command.getQueryLogFileType();
				String fileName = command.getQueryLogFileName();
				LOGGER.debug("LogFileDownloadController.download() --> fileType: " + fileType);
				LOGGER.debug("LogFileDownloadController.download() --> fileName: " + fileName);
			    
				if (fileType.equals("ap")) {
					//取得log4j.properties 裡logfile 的檔案路徑
					Enumeration e = Logger.getRootLogger().getAllAppenders();
				    while (e.hasMoreElements()) {
				    	Appender app = (Appender)e.nextElement();
				    	if (app instanceof FileAppender) {
				    		filePath = "C:" + ((FileAppender)app).getFile();
				    	}
				    }
		    		filePath = filePath.replace('/', '\\');
		    		filePath = filePath.substring(0, filePath.lastIndexOf("\\") + 1) + fileName;
				} else if (fileType.equals("tomcat")) {
					//取得Tomcat logfile 的檔案路徑
					filePath = System.getProperty("catalina.home") + File.separator + "logs" + File.separator + fileName;
				}
	    		LOGGER.debug("LogFileDownloadController.download() --> File Path: " + filePath);
				FileUtils.download(request, response, filePath, fileName);
			} else {
				LOGGER.debug("LogFileDownloadController.download() --> LogFileDownloadFormDTO is null");
			}
		} catch (Exception e) {
			LOGGER.error("LogFileDownloadController.download() is error: " + e, e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
}
