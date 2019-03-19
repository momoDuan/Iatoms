package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmLogFileDownloadDTO;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

/**
 * Purpose:錯誤記錄檔下載formDTO
 * @author NickLin 
 * @since  JDK 1.7
 * @date   2018/07/27
 * @MaintenancePersonnel CyberSoft
 */
public class LogFileDownloadFormDTO extends AbstractSimpleListFormDTO<AdmLogFileDownloadDTO> {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -105936232784984483L;
	
	/**
	 * 查詢條件:log檔案類型
	 */
	public static final String QUERY_LOG_FILE_TYPE = "queryLogFileType";
	/**
	 * 查詢條件:log檔案名稱
	 */
	public static final String QUERY_LOG_FILE_NAME = "queryLogFileName";
	/**
	 * 查詢條件:log檔案類型
	 */
	private String queryLogFileType;
	/**
	 * 查詢條件:log檔案名稱
	 */
	private String queryLogFileName;
	/**
	 * 錯誤記錄檔下載DTO
	 */
	private AdmLogFileDownloadDTO logFileDownloadDTO;
	
	/**
	 * Constructor: 無參數建構子
	 */
	public LogFileDownloadFormDTO() {
		super();
	}

	/**
	 * @return the queryLogFileType
	 */
	public String getQueryLogFileType() {
		return queryLogFileType;
	}

	/**
	 * @param queryLogFileType the queryLogFileType to set
	 */
	public void setQueryLogFileType(String queryLogFileType) {
		this.queryLogFileType = queryLogFileType;
	}

	/**
	 * @return the queryLogFileName
	 */
	public String getQueryLogFileName() {
		return queryLogFileName;
	}

	/**
	 * @param queryLogFileName the queryLogFileName to set
	 */
	public void setQueryLogFileName(String queryLogFileName) {
		this.queryLogFileName = queryLogFileName;
	}

	/**
	 * @return the logFileDownloadDTO
	 */
	public AdmLogFileDownloadDTO getLogFileDownloadDTO() {
		return logFileDownloadDTO;
	}

	/**
	 * @param logFileDownloadDTO the logFileDownloadDTO to set
	 */
	public void setLogFileDownloadDTO(AdmLogFileDownloadDTO logFileDownloadDTO) {
		this.logFileDownloadDTO = logFileDownloadDTO;
	}
}
