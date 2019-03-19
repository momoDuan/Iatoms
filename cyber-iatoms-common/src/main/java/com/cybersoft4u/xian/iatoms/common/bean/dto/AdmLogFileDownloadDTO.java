package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.dto.DataTransferObject;

/**
 * Purpose:錯誤記錄檔下載DTO
 * @author NickLin
 * @since  JDK 1.7
 * @date   2018/07/27
 * @MaintenancePersonnel CyberSoft
 */
public class AdmLogFileDownloadDTO extends DataTransferObject<String> {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -1688244025030009197L;

	public static enum ATTRIBUTE {
		LOG_FILE_TYPE("logFileType"),
		LOG_FILE_NAME("logFileName"),
		LOG_FILE_PATH("logFilePath");
		
		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	}
	
	/**
	 * log檔案類型
	 */
	private String logFileType;
	/**
	 * log檔案名稱
	 */
	private String logFileName;
	/**
	 * log檔案名稱
	 */
	private String logFilePath;
	/**
	 * Constructor:無參構造
	 */
	public AdmLogFileDownloadDTO() {
	}
	/**
	 * Constructor:有參構造
	 */
	public AdmLogFileDownloadDTO(String logFileType, String logFileName, String logFilePath) {
		super();
		this.logFileType = logFileType;
		this.logFileName = logFileName;
		this.logFilePath = logFilePath;
	}
	
	/**
	 * @return the logFileType
	 */
	public String getLogFileType() {
		return logFileType;
	}
	/**
	 * @param logFileType the logFileType to set
	 */
	public void setLogFileType(String logFileType) {
		this.logFileType = logFileType;
	}
	/**
	 * @return the logFileName
	 */
	public String getLogFileName() {
		return logFileName;
	}
	/**
	 * @param logFileName the logFileName to set
	 */
	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}
	/**
	 * @return the logFilePath
	 */
	public String getLogFilePath() {
		return logFilePath;
	}
	/**
	 * @param logFilePath the logFilePath to set
	 */
	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}
}
