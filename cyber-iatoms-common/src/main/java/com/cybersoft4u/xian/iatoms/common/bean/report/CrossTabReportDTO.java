package com.cybersoft4u.xian.iatoms.common.bean.report;

import java.util.List;

import cafe.core.bean.dto.DataTransferObject;

/**
 * 
 * Purpose: 交叉报表
 * @author ericdu
 * @since  JDK 1.7
 * @date   2016年6月22日
 * @MaintenancePersonnel ericdu
 */
public class CrossTabReportDTO extends DataTransferObject<String> {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 7360457990582923319L;
	
	public static enum ATTRIBUTE {
		COLUMN_NAME("columnName"),
		COLUMN_NAME_TYPE("columnNameType"),
		CONTENT("content"),
		ROW_NO("rowNo"),
		ROW_NAME("rowName"),
		CUSTOMER_NAME("customerName"),
		INT_CONTENT("intContent"),
		TITLE("title"),
		ASSET_TYPE_STATUS("assetTypeStatus");

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	
	//列名称
	private String columnName;
	private String columnNameType;
	//对应栏位
	private String content;
	//行号
	private int rowNo;

	private String rowName;
	private String customerName;
	
	private int intContent;
	private String title;
	private String assetTypeStatus;
	private List<CrossTabReportDTO> crossTabReportDTOsOrderByShort;
	private List<CrossTabReportDTO> crossTabReportDTOsOrderByDept;
	/**
	 * Constructor: 无参构造函数
	 */
	public CrossTabReportDTO() {
		super();
	}

	/**
	 * @return the assetTypeStatus
	 */
	public String getAssetTypeStatus() {
		return assetTypeStatus;
	}




	/**
	 * @param assetTypeStatus the assetTypeStatus to set
	 */
	public void setAssetTypeStatus(String assetTypeStatus) {
		this.assetTypeStatus = assetTypeStatus;
	}




	/**
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * @param columnName the columnName to set
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the rowNo
	 */
	public int getRowNo() {
		return rowNo;
	}

	/**
	 * @param rowNo the rowNo to set
	 */
	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}

	/**
	 * @return the rowName
	 */
	public String getRowName() {
		return rowName;
	}

	/**
	 * @param rowName the rowName to set
	 */
	public void setRowName(String rowName) {
		this.rowName = rowName;
	}

	/**
	 * @return the intContent
	 */
	public int getIntContent() {
		return intContent;
	}

	/**
	 * @param intContent the intContent to set
	 */
	public void setIntContent(int intContent) {
		this.intContent = intContent;
	}

	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the columnNameType
	 */
	public String getColumnNameType() {
		return columnNameType;
	}

	/**
	 * @param columnNameType the columnNameType to set
	 */
	public void setColumnNameType(String columnNameType) {
		this.columnNameType = columnNameType;
	}

	/**
	 * @return the crossTabReportDTOsOrderByShort
	 */
	public List<CrossTabReportDTO> getCrossTabReportDTOsOrderByShort() {
		return crossTabReportDTOsOrderByShort;
	}

	/**
	 * @param crossTabReportDTOsOrderByShort the crossTabReportDTOsOrderByShort to set
	 */
	public void setCrossTabReportDTOsOrderByShort(
			List<CrossTabReportDTO> crossTabReportDTOsOrderByShort) {
		this.crossTabReportDTOsOrderByShort = crossTabReportDTOsOrderByShort;
	}

	/**
	 * @return the crossTabReportDTOsOrderByDept
	 */
	public List<CrossTabReportDTO> getCrossTabReportDTOsOrderByDept() {
		return crossTabReportDTOsOrderByDept;
	}

	/**
	 * @param crossTabReportDTOsOrderByDept the crossTabReportDTOsOrderByDept to set
	 */
	public void setCrossTabReportDTOsOrderByDept(
			List<CrossTabReportDTO> crossTabReportDTOsOrderByDept) {
		this.crossTabReportDTOsOrderByDept = crossTabReportDTOsOrderByDept;
	}


	
}
