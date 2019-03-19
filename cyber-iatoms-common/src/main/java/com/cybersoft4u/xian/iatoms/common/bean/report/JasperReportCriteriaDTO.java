package com.cybersoft4u.xian.iatoms.common.bean.report;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cafe.core.bean.dto.DataTransferObject;

/**
 * Purpose: JasperReport條件設定DTO
 * @author: ivenguo
 * @version: 1.0
 * @Created date: 2009/11/9
 * @Modified date: 2009/11/9
 */
public class JasperReportCriteriaDTO extends DataTransferObject<String> {
		/** serialVersionUID */
		private static final long serialVersionUID = -5318641602441586165L;
		/** jxls路徑前綴*/
		private String jxlsPath;
		/** jrXml後綴名 */
		public static final String JRXML_EXT_NAME = ".jrxml";
		public static final String JASPER_EXT_NAME = ".jasper";
		
		public static final String REPORT_TYPE_TXT = "text";
		public static final String REPORT_TYPE_RTF = "rtf";
		public static final String REPORT_TYPE_HTML = "html";
		public static final String REPORT_TYPE_XML = "xml";
		public static final String REPORT_TYPE_PDF = "pdf";
		public static final String REPORT_TYPE_MSWORD = "msword";
		public static final String REPORT_TYPE_MSEXCEL = "msexcel";
		public static final String REPORT_TYPE_MSEXCELX = "msexcelx";
		public static final String REPORT_TYPE_MSPPT = "msppt";
		public static final String REPORT_TYPE_CSV = "csv";
		
		public static final String REPORT_EXT_NAME_CSV = ".csv";
		public static final String REPORT_EXT_NAME_TXT = ".txt";
		public static final String REPORT_EXT_NAME_RTF = ".rtf";
		public static final String REPORT_EXT_NAME_HTML = ".html";
		public static final String REPORT_EXT_NAME_XML = ".xml";
		public static final String REPORT_EXT_NAME_PDF = ".pdf";
		public static final String REPORT_EXT_NAME_MSWORD = ".doc";
		public static final String REPORT_EXT_NAME_MSEXCEL = ".xls";
		public static final String REPORT_EXT_NAME_MSEXCELX = ".xlsx";
		public static final String REPORT_EXT_NAME_MSPPT = ".ppt";
		public static final String REPORT_EXT_NAME_ZIP = ".zip";
		
		public static final String RESPONSE_CONTENT_TYPE_TXT = "text/plain";
		public static final String RESPONSE_CONTENT_TYPE_RTF = "application/rtf";
		public static final String RESPONSE_CONTENT_TYPE_HTML = "text/html";
		public static final String RESPONSE_CONTENT_TYPE_XML = "text/xml";
		public static final String RESPONSE_CONTENT_TYPE_PDF = "application/pdf";
		public static final String RESPONSE_CONTENT_TYPE_MSWORD = "application/msword";
		public static final String RESPONSE_CONTENT_TYPE_MSEXCEL = "application/vnd.ms-excel";
		public static final String RESPONSE_CONTENT_TYPE_2007_MSEXCEL = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		public static final String RESPONSE_CONTENT_TYPE_MSPPT = "application/vnd.ms-powerpoint";
		
		public static final String CHARACTER_ENCODING = "UTF-8";
		public static final String DEFAULT_REPORT_FILE_NAME = "noTitle";
		
		/** contentTypeMap */
		public static Map<String, String> contentTypeMap;
		/** reportExtNameMap */
		public static Map<String, String> reportExtNameMap;
		
		static {
			contentTypeMap = new HashMap<String, String>();
			contentTypeMap.put(REPORT_TYPE_TXT, RESPONSE_CONTENT_TYPE_TXT);
			contentTypeMap.put(REPORT_TYPE_RTF, RESPONSE_CONTENT_TYPE_RTF);
			contentTypeMap.put(REPORT_TYPE_HTML, RESPONSE_CONTENT_TYPE_HTML);
			contentTypeMap.put(REPORT_TYPE_XML, RESPONSE_CONTENT_TYPE_XML);
			contentTypeMap.put(REPORT_TYPE_PDF, RESPONSE_CONTENT_TYPE_PDF);
			contentTypeMap.put(REPORT_TYPE_MSWORD, RESPONSE_CONTENT_TYPE_MSWORD);
			contentTypeMap.put(REPORT_TYPE_MSEXCEL, RESPONSE_CONTENT_TYPE_MSEXCEL);
			contentTypeMap.put(REPORT_TYPE_MSPPT, RESPONSE_CONTENT_TYPE_MSPPT);
			
			reportExtNameMap = new HashMap<String, String>();
			reportExtNameMap.put(REPORT_TYPE_TXT, REPORT_EXT_NAME_TXT);
			reportExtNameMap.put(REPORT_TYPE_RTF, REPORT_EXT_NAME_RTF);
			reportExtNameMap.put(REPORT_TYPE_HTML, REPORT_EXT_NAME_HTML);
			reportExtNameMap.put(REPORT_TYPE_XML, REPORT_EXT_NAME_XML);
			reportExtNameMap.put(REPORT_TYPE_PDF, REPORT_EXT_NAME_PDF);
			reportExtNameMap.put(REPORT_TYPE_MSWORD, REPORT_EXT_NAME_MSWORD);
			reportExtNameMap.put(REPORT_TYPE_MSEXCEL, REPORT_EXT_NAME_MSEXCEL);
			reportExtNameMap.put(REPORT_TYPE_MSPPT, REPORT_EXT_NAME_MSPPT);
		}
		
		/** 結果集（根據不同的報表放置與報表對應的類） */
		private Collection<?> result;
		/** 子報表集合  */
		private Map<String,String> subjrXmlNames;
		/** sheet名稱 for MS-Excel*/
		private String sheetName;
		/** jrxml文件的名字（不包含包名和後綴名） */
		private String jrxmlName;
		/** 產生的報表文件名 */
		private String reportFileName = JasperReportCriteriaDTO.DEFAULT_REPORT_FILE_NAME;
		/** 其他顯示的屬性（ 如列印時間、 條件 等，可以為null） */
		private Map<String, Object> parameters;
		/** 導出的格式（默認是pdf） */
		private String type = JasperReportCriteriaDTO.REPORT_TYPE_PDF;
		/** 是否使用iReport設計jrxml的方式（true:不使用iReport， false:使用iReport） */
		private boolean autoBuildJasper;
		//modified by edward yen 2011/11/28
		/** jrXml路徑前綴*/
		private String jrxmlPath;
		private String jxlsName;
		/**打開文檔所需密碼 */
		private String openPwd;
		public JasperReportCriteriaDTO() {
		}
		
		public JasperReportCriteriaDTO(String jrxmlPath) {
			this.jrxmlPath = jrxmlPath;
		}		
		/**
		 * @return the result
		 */
		public Collection<?> getResult() {
			return result;
		}
		/**
		 * @param result the result to set
		 */
		public void setResult(Collection<?> result) {
			this.result = result;
		}
		/**
		 * @return the jrxmlName
		 */
		public String getJrxmlName() {
			return jrxmlName;
		}
		/**
		 * @param jrxmlName the jrxmlName to set
		 */
		public void setJrxmlName(String jrxmlName) {
			this.jrxmlName = jrxmlName;
		}
		/**
		 * @return the reportFileName
		 */
		public String getReportFileName() {
			return reportFileName;
		}
		/**
		 * @param reportFileName the reportFileName to set
		 */
		public void setReportFileName(String reportFileName) {
			this.reportFileName = reportFileName;
		}
		/**
		 * @return the parameters
		 */
		public Map<String, Object> getParameters() {
			return parameters;
		}
		/**
		 * @param parameters the parameters to set
		 */
		public void setParameters(Map<String, Object> parameters) {
			this.parameters = parameters;
		}
		/**
		 * @return the type
		 */
		public String getType() {
			return type;
		}
		/**
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}
		/**
		 * @return the autoBuildJasper
		 */
		public boolean isAutoBuildJasper() {
			return autoBuildJasper;
		}
		/**
		 * @param autoBuildJasper the autoBuildJasper to set
		 */
		public void setAutoBuildJasper(boolean autoBuildJasper) {
			this.autoBuildJasper = autoBuildJasper;
		}
		/**
		 * @return the contentTypeMap
		 */
		public static Map<String, String> getContentTypeMap() {
			return contentTypeMap;
		}
		/**
		 * @param contentTypeMap the contentTypeMap to set
		 */
		public static void setContentTypeMap(Map<String, String> contentTypeMap) {
			JasperReportCriteriaDTO.contentTypeMap = contentTypeMap;
		}
		/**
		 * @return the reportExtNameMap
		 */
		public static Map<String, String> getReportExtNameMap() {
			return reportExtNameMap;
		}
		/**
		 * @param reportExtNameMap the reportExtNameMap to set
		 */
		public static void setReportExtNameMap(Map<String, String> reportExtNameMap) {
			JasperReportCriteriaDTO.reportExtNameMap = reportExtNameMap;
		}
		/**
		 * @return the sheetName
		 */
		public String getSheetName() {
			return sheetName;
		}
		/**
		 * @param sheetName the sheetName to set
		 */
		public void setSheetName(String sheetName) {
			this.sheetName = sheetName;
		}
		public String getJrxmlPath() {
			return jrxmlPath;
		}
		public void setJrxmlPath(String jrxmlPath) {
			this.jrxmlPath = jrxmlPath;
		}

		/**
		 * @return the subjrXmlNames
		 */
		public Map<String, String> getSubjrXmlNames() {
			return subjrXmlNames;
		}

		/**
		 * @param subjrXmlNames the subjrXmlNames to set
		 */
		public void setSubjrXmlNames(Map<String, String> subjrXmlNames) {
			this.subjrXmlNames = subjrXmlNames;
		}

		/**
		 * @return the openPwd
		 */
		public String getOpenPwd() {
			return openPwd;
		}

		/**
		 * @param openPwd the openPwd to set
		 */
		public void setOpenPwd(String openPwd) {
			this.openPwd = openPwd;
		}

		/**
		 * @return the jxlsPath
		 */
		public String getJxlsPath() {
			return jxlsPath;
		}

		/**
		 * @param jxlsPath the jxlsPath to set
		 */
		public void setJxlsPath(String jxlsPath) {
			this.jxlsPath = jxlsPath;
		}

		/**
		 * @return the jxlsName
		 */
		public String getJxlsName() {
			return jxlsName;
		}

		/**
		 * @param jxlsName the jxlsName to set
		 */
		public void setJxlsName(String jxlsName) {
			this.jxlsName = jxlsName;
		}
		
}
