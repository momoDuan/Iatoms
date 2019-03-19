package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.util.Date;

import cafe.core.bean.dto.DataTransferObject;
/**
 * Purpose: 案件附加資料檔
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016/12/8
 * @MaintenancePersonnel CrissZhang
 */
public class SrmCaseAttFileDTO  extends DataTransferObject<String> {
	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -3712551646247939990L;
	
	/**
	 * Purpose: 
	 * @author CrissZhang
	 * @since  JDK 1.6
	 * @date   2017/2/23
	 * @MaintenancePersonnel CrissZhang
	 */
	public static enum ATTRIBUTE {
		ATT_FILE_ID("attFileId"),
		DTID("dtid"),
		CASE_ID("caseId"),
		FILE_NAME("fileName"),
		FILE_PATH("filePath"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
		CREATED_DATE("createdDate");

		/**
		 * value值
		 */
		private String value;
		
		/**
		 * Constructor:構造函數
		 */
		ATTRIBUTE(String value) {
			this.value = value;
		};
		
		/**
		 * @return the value
		 */
		public String getValue() {
			return this.value;
		}
	};
	
	/**
	 * 附加檔案ID
	 */
	private String attFileId;
	/**
	 * DTID
	 */
	private String dtid;
	/**
	 * 文件名稱
	 */
	private String fileName;
	/**
	 * 文件路徑
	 */
	private String filePath;
	/**
	 * 新增人員編號
	 */
	private String createdById;
	/**
	 * 新增人員名稱
	 */
	private String createdByName;
	/**
	 * 新增日期
	 */
	private Date createdDate;
	/**
	 * 案件編號
	 */
	private String caseId;
	/**
	 * Constructor:無參構造函數
	 */
	public SrmCaseAttFileDTO() {
		super();
	}
	
	/**
	 * Constructor:有參構造函數
	 */
	public SrmCaseAttFileDTO(String attFileId, String dtid, String fileName,
			String filePath, String createdById,
			String createdByName, Date createdDate) {
		super();
		this.attFileId = attFileId;
		this.dtid = dtid;
		this.fileName = fileName;
		this.filePath = filePath;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
	}
	
	/**
	 * @return the attFileId
	 */
	public String getAttFileId() {
		return attFileId;
	}
	/**
	 * @param attFileId the attFileId to set
	 */
	public void setAttFileId(String attFileId) {
		this.attFileId = attFileId;
	}
	/**
	 * @return the dtid
	 */
	public String getDtid() {
		return dtid;
	}
	/**
	 * @param dtid the dtid to set
	 */
	public void setDtid(String dtid) {
		this.dtid = dtid;
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}
	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	/**
	 * @return the createdById
	 */
	public String getCreatedById() {
		return createdById;
	}
	/**
	 * @param createdById the createdById to set
	 */
	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}
	/**
	 * @return the createdByName
	 */
	public String getCreatedByName() {
		return createdByName;
	}
	/**
	 * @param createdByName the createdByName to set
	 */
	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}
	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	/**
	 * @return the caseId
	 */
	public String getCaseId() {
		return caseId;
	}

	/**
	 * @param caseId the caseId to set
	 */
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	
}
