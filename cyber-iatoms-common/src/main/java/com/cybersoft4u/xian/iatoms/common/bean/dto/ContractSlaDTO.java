package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.util.Date;

import cafe.core.bean.dto.DataTransferObject;
/**
 * Purpose: 合約SLA設定DTO
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016/6/28
 * @MaintenancePersonnel CrissZhang
 */
public class ContractSlaDTO extends DataTransferObject<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7202448105257707096L;

	/**
	 * Purpose: 枚举类型参数
	 * @author CrissZhang
	 * @since  JDK 1.6
	 * @date   2016/6/28
	 * @MaintenancePersonnel CrissZhang
	 */
	public static enum ATTRIBUTE {
		SLA_ID("slaId"),
		TICKET_TYPE("ticketType"),
		LOCATION("location"),
		TICKET_MODE("ticketMode"),
		CONTRACT_ID("contractId"),
		CONTRACT_CODE("contractCode"),
		IS_WORK_DAY("isWorkDay"),
		IS_THAT_DAY("isThatDay"),
		THAT_DAY_TIME("thatDayTime"),
		RESPONSE_HOUR("responseHour"),
		RESPONSE_WARNNING("responseWarnning"),
		ARRIVE_HOUR("arriveHour"),
		ARRIVE_WARNNING("arriveWarnning"),
		COMPLETE_HOUR("completeHour"),
		COMPLETE_WARNNING("completeWarnning"),
		COMMENT("comment"),
		CUSTOMER_ID("customerId"),
		CUSTOMER_NAME("customerName"),
		TICKET_TYPE_NAME("ticketTypeName"),
		LOCATION_NAME("locationName"),
		TICKET_MODE_NAME("ticketModeName"),
		IS_HAVE_SLA("isHaveSla"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
		CREATED_DATE("createdDate"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		UPDATED_DATE("updatedDate");
		
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
	 * Constructor:無參構造函數
	 */
	public ContractSlaDTO() {
	}

	/**
	 * 合约sla设定主键
	 */
	private String slaId;
	/**
	 * 案件類別
	 */
	private String ticketType;
	
	/**
	 * 區域主檔
	 */
	private String location;
	
	/**
	 * 案件類型
	 */
	private String ticketMode;
	
	/**
	 * 合約代碼
	 */
	private String contractId;
	
	/**
	 * 合約編號
	 */
	private String contractCode;
	/**
	 * 是否為工作日
	 */
	private String isWorkDay = "N";
	
	/**
	 * 当天件
	 */
	private String isThatDay = "N";
	
	/**
	 * 当天件时间
	 */
	private String thatDayTime;
	/**
	 * 回應時間
	 */
	private Double responseHour;
	
	/**
	 * 回應警示
	 */
	private Double responseWarnning;
	
	/**
	 * 到場時間
	 */
	private Double arriveHour;
	
	/**
	 * 到場警示
	 */
	private Double arriveWarnning;
	
	/**
	 * 完修時間
	 */
	private Double completeHour;
	
	/**
	 * 完修警示
	 */
	private Double completeWarnning;
	
	/**
	 * 說明
	 */
	private String comment;

	/**
	 * 客戶ID
	 */
	private String customerId;
	
	/**
	 * 客戶名稱
	 */
	private String customerName;
	
	/**
	 * 案件類別名称
	 */
	private String ticketTypeName;
	
	/**
	 * 區域名稱
	 */
	private String locationName;
	
	/**
	 * 案件类型名稱
	 */
	private String ticketModeName;
	
	/**
	 * 创建人员编号
	 */
	private String createdById;
	
	/**
	 * 创建人员名称
	 */
	private String createdByName;
	
	/**
	 * 创建日期
	 */
	private Date createdDate;
	
	/**
	 * 异动人员编号
	 */
	private String updatedById;
	
	/**
	 * 异动人员名称
	 */
	private String updatedByName;
	
	/**
	 * 异动日期
	 */
	private Date updatedDate;
	
	/**
	 * 复制合同编号
	 */
	private String copyContractId;

	/**
	 * 編輯公司ID
	 */
	private String insertCustomerId;
	
	/**
	 * 編輯合約ID
	 */
	private String insertContractId;
	
	/**
	 * 是否有sla信息
	 */
	private Boolean isHaveSla;
	
	/**
	 * 回應時效字符
	 */
	private String responseHourString;
	
	/**
	 * 回應警示字符
	 */
	private String responseWarnningString;
	
	/**
	 * 完成時效字符
	 */
	private String completeHourString;
	
	/**
	 * 完成警示字符
	 */
	private String completeWarnningString;
	
	/**
	 * 到場時效字符
	 */
	private String arriveHourString;
	
	/**
	 * 到場警示字符
	 */
	private String arriveWarnningString;
	
	/**
	 * Constructor:有參構造函數
	 */
	public ContractSlaDTO(String ticketType, String location,
			String ticketMode, String contractId, String isWorkDay,
			String isThatDay, String thatDayTime, Double responseHour,
			Double responseWarnning, Double arriveHour, Double arriveWarnning,
			Double completeHour, Double completeWarnning, String comment,
			String customerId, String customerName, String ticketTypeName,
			String locationName, String ticketModeName, 
			String createdById, String createdByName, Date createdDate,
			String updatedById, String updatedByName, Date updatedDate,
			String copyContractId, String insertCustomerId,
			String insertContractId) {
		super();
		this.ticketType = ticketType;
		this.location = location;
		this.ticketMode = ticketMode;
		this.contractId = contractId;
		this.isWorkDay = isWorkDay;
		this.isThatDay = isThatDay;
		this.thatDayTime = thatDayTime;
		this.responseHour = responseHour;
		this.responseWarnning = responseWarnning;
		this.arriveHour = arriveHour;
		this.arriveWarnning = arriveWarnning;
		this.completeHour = completeHour;
		this.completeWarnning = completeWarnning;
		this.comment = comment;
		this.customerId = customerId;
		this.customerName = customerName;
		this.ticketTypeName = ticketTypeName;
		this.locationName = locationName;
		this.ticketModeName = ticketModeName;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
		this.copyContractId = copyContractId;
		this.insertCustomerId = insertCustomerId;
		this.insertContractId = insertContractId;
	}

	/**
	 * @return the slaId
	 */
	public String getSlaId() {
		return slaId;
	}

	/**
	 * @param slaId the slaId to set
	 */
	public void setSlaId(String slaId) {
		this.slaId = slaId;
	}

	/**
	 * @return the ticketType
	 */
	public String getTicketType() {
		return ticketType;
	}

	/**
	 * @param ticketType the ticketType to set
	 */
	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the ticketMode
	 */
	public String getTicketMode() {
		return ticketMode;
	}

	/**
	 * @param ticketMode the ticketMode to set
	 */
	public void setTicketMode(String ticketMode) {
		this.ticketMode = ticketMode;
	}

	/**
	 * @return the contractId
	 */
	public String getContractId() {
		return contractId;
	}

	/**
	 * @param contractId the contractId to set
	 */
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	/**
	 * @return the isWorkDay
	 */
	public String getIsWorkDay() {
		return isWorkDay;
	}

	/**
	 * @param isWorkDay the isWorkDay to set
	 */
	public void setIsWorkDay(String isWorkDay) {
		this.isWorkDay = isWorkDay;
	}

	/**
	 * @return the isThatDay
	 */
	public String getIsThatDay() {
		return isThatDay;
	}

	/**
	 * @param isThatDay the isThatDay to set
	 */
	public void setIsThatDay(String isThatDay) {
		this.isThatDay = isThatDay;
	}

	/**
	 * @return the thatDayTime
	 */
	public String getThatDayTime() {
		return thatDayTime;
	}

	/**
	 * @param thatDayTime the thatDayTime to set
	 */
	public void setThatDayTime(String thatDayTime) {
		this.thatDayTime = thatDayTime;
	}

	/**
	 * @return the responseHour
	 */
	public Double getResponseHour() {
		return responseHour;
	}

	/**
	 * @param responseHour the responseHour to set
	 */
	public void setResponseHour(Double responseHour) {
		this.responseHour = responseHour;
	}

	/**
	 * @return the responseWarnning
	 */
	public Double getResponseWarnning() {
		return responseWarnning;
	}

	/**
	 * @param responseWarnning the responseWarnning to set
	 */
	public void setResponseWarnning(Double responseWarnning) {
		this.responseWarnning = responseWarnning;
	}

	/**
	 * @return the arriveHour
	 */
	public Double getArriveHour() {
		return arriveHour;
	}

	/**
	 * @param arriveHour the arriveHour to set
	 */
	public void setArriveHour(Double arriveHour) {
		this.arriveHour = arriveHour;
	}

	/**
	 * @return the arriveWarnning
	 */
	public Double getArriveWarnning() {
		return arriveWarnning;
	}

	/**
	 * @param arriveWarnning the arriveWarnning to set
	 */
	public void setArriveWarnning(Double arriveWarnning) {
		this.arriveWarnning = arriveWarnning;
	}

	/**
	 * @return the completeHour
	 */
	public Double getCompleteHour() {
		return completeHour;
	}

	/**
	 * @param completeHour the completeHour to set
	 */
	public void setCompleteHour(Double completeHour) {
		this.completeHour = completeHour;
	}

	/**
	 * @return the completeWarnning
	 */
	public Double getCompleteWarnning() {
		return completeWarnning;
	}

	/**
	 * @param completeWarnning the completeWarnning to set
	 */
	public void setCompleteWarnning(Double completeWarnning) {
		this.completeWarnning = completeWarnning;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
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
	 * @return the ticketTypeName
	 */
	public String getTicketTypeName() {
		return ticketTypeName;
	}

	/**
	 * @param ticketTypeName the ticketTypeName to set
	 */
	public void setTicketTypeName(String ticketTypeName) {
		this.ticketTypeName = ticketTypeName;
	}

	/**
	 * @return the locationName
	 */
	public String getLocationName() {
		return locationName;
	}

	/**
	 * @param locationName the locationName to set
	 */
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	/**
	 * @return the ticketModeName
	 */
	public String getTicketModeName() {
		return ticketModeName;
	}

	/**
	 * @param ticketModeName the ticketModeName to set
	 */
	public void setTicketModeName(String ticketModeName) {
		this.ticketModeName = ticketModeName;
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
	 * @return the updatedById
	 */
	public String getUpdatedById() {
		return updatedById;
	}

	/**
	 * @param updatedById the updatedById to set
	 */
	public void setUpdatedById(String updatedById) {
		this.updatedById = updatedById;
	}

	/**
	 * @return the updatedByName
	 */
	public String getUpdatedByName() {
		return updatedByName;
	}

	/**
	 * @param updatedByName the updatedByName to set
	 */
	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}

	/**
	 * @return the updatedDate
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	/**
	 * @return the copyContractId
	 */
	public String getCopyContractId() {
		return copyContractId;
	}

	/**
	 * @param copyContractId the copyContractId to set
	 */
	public void setCopyContractId(String copyContractId) {
		this.copyContractId = copyContractId;
	}

	/**
	 * @return the insertCustomerId
	 */
	public String getInsertCustomerId() {
		return insertCustomerId;
	}

	/**
	 * @param insertCustomerId the insertCustomerId to set
	 */
	public void setInsertCustomerId(String insertCustomerId) {
		this.insertCustomerId = insertCustomerId;
	}

	/**
	 * @return the insertContractId
	 */
	public String getInsertContractId() {
		return insertContractId;
	}

	/**
	 * @param insertContractId the insertContractId to set
	 */
	public void setInsertContractId(String insertContractId) {
		this.insertContractId = insertContractId;
	}

	/**
	 * @return the contractCode
	 */
	public String getContractCode() {
		return contractCode;
	}

	/**
	 * @param contractCode the contractCode to set
	 */
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	/**
	 * @return the isHaveSla
	 */
	public Boolean getIsHaveSla() {
		return isHaveSla;
	}

	/**
	 * @param isHaveSla the isHaveSla to set
	 */
	public void setIsHaveSla(Boolean isHaveSla) {
		this.isHaveSla = isHaveSla;
	}

	/**
	 * @return the responseHourString
	 */
	public String getResponseHourString() {
		return responseHourString;
	}

	/**
	 * @param responseHourString the responseHourString to set
	 */
	public void setResponseHourString(String responseHourString) {
		this.responseHourString = responseHourString;
	}

	/**
	 * @return the responseWarnningString
	 */
	public String getResponseWarnningString() {
		return responseWarnningString;
	}

	/**
	 * @param responseWarnningString the responseWarnningString to set
	 */
	public void setResponseWarnningString(String responseWarnningString) {
		this.responseWarnningString = responseWarnningString;
	}

	/**
	 * @return the completeHourString
	 */
	public String getCompleteHourString() {
		return completeHourString;
	}

	/**
	 * @param completeHourString the completeHourString to set
	 */
	public void setCompleteHourString(String completeHourString) {
		this.completeHourString = completeHourString;
	}

	/**
	 * @return the completeWarnningString
	 */
	public String getCompleteWarnningString() {
		return completeWarnningString;
	}

	/**
	 * @param completeWarnningString the completeWarnningString to set
	 */
	public void setCompleteWarnningString(String completeWarnningString) {
		this.completeWarnningString = completeWarnningString;
	}

	/**
	 * @return the arriveHourString
	 */
	public String getArriveHourString() {
		return arriveHourString;
	}

	/**
	 * @param arriveHourString the arriveHourString to set
	 */
	public void setArriveHourString(String arriveHourString) {
		this.arriveHourString = arriveHourString;
	}

	/**
	 * @return the arriveWarnningString
	 */
	public String getArriveWarnningString() {
		return arriveWarnningString;
	}

	/**
	 * @param arriveWarnningString the arriveWarnningString to set
	 */
	public void setArriveWarnningString(String arriveWarnningString) {
		this.arriveWarnningString = arriveWarnningString;
	}
	
}
