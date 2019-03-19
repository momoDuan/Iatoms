package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.dto.DataTransferObject;
/**
 * Purpose: 報修問題分析報表DTO
 * @author echomou
 * @since  JDK 1.6
 * @date   2016/11/14
 * @MaintenancePersonnel echomou
 */
public class RepairReportDTO extends DataTransferObject<String> {
	
	/**
	 *  序列號
	 */
	private static final long serialVersionUID = -1883298888813159898L;
	
	public static enum ATTRIBUTE {
		CUSTOMER_ID("customerId"),
		CUSTOMER_Name("customerName"),
		REPAIR_TYPE("repairType"),
		REPAIR_ITEM("repairItem"),
		REPAIR_COUNT("repairCount"),
		EDC_TYPE("edcType");

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	
	//客戶ID
	private String customerId;
	//客戶名字
	private String customerName;
	//類別
	private String repairType;
	//項目
	private String repairItem;
	//報修次數
	private Integer repairCount;
	//刷卡機型
	private String edcType;
	
	private Integer number;
	

	/**
	 * @return the number
	 */
	public Integer getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}
	
	
	public RepairReportDTO(String customerId, String customerName,
			String repairType, String repairItem, Integer repairCount) {
		super();
		this.customerId = customerId;
		this.customerName = customerName;
		this.repairType = repairType;
		this.repairItem = repairItem;
		this.repairCount = repairCount;
	}
	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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
	 * @return the repairType
	 */
	public String getRepairType() {
		return repairType;
	}
	/**
	 * @param repairType the repairType to set
	 */
	public void setRepairType(String repairType) {
		this.repairType = repairType;
	}
	/**
	 * @return the repairItem
	 */
	public String getRepairItem() {
		return repairItem;
	}
	/**
	 * @param repairItem the repairItem to set
	 */
	public void setRepairItem(String repairItem) {
		this.repairItem = repairItem;
	}
	/**
	 * @return the repairCount
	 */
	public Integer getRepairCount() {
		return repairCount;
	}
	/**
	 * @param repairCount the repairCount to set
	 */
	public void setRepairCount(Integer repairCount) {
		this.repairCount = repairCount;
	}
	/**
	 * @return the edcType
	 */
	public String getEdcType() {
		return edcType;
	}
	/**
	 * @param edcType the edcType to set
	 */
	public void setEdcType(String edcType) {
		this.edcType = edcType;
	}
	public RepairReportDTO(String customerId, String customerName) {
		super();
		this.customerId = customerId;
		this.customerName = customerName;
	}
	public RepairReportDTO(String repairType, String repairItem,
			Integer repairCount) {
		super();
		this.repairType = repairType;
		this.repairItem = repairItem;
		this.repairCount = repairCount;
	}
	
}
