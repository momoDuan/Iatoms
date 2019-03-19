package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.util.Date;

import cafe.core.bean.dto.DataTransferObject;

/**
 * Purpose: 活动信息
 * @author Allenchen
 * @since  JDK 1.7
 * @date   2016/4/11
 * @MaintenancePersonnel Allenchen
 */
public class ActivityDefDTO extends DataTransferObject<String> {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 533330452203295974L;
	
	private String activityId;
	private String activityName;
	private String activityCode;
	private String parentActivityId;
	private String status;
	private String acitvityShape;
	private String createdById;
	private String createdByName;
	private Date createdDate;
	private String updatedById;
	private String udpatedByName;
	private Date updatedDate;
	
	
	/**
	 * 
	 * Constructor:无参构建子
	 */
	public ActivityDefDTO (){
		super();
	}
	/**
	 * @return the activityId
	 */
	public String getActivityId() {
		return activityId;
	}
	/**
	 * @param activityId the activityId to set
	 */
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	/**
	 * @return the activityName
	 */
	public String getActivityName() {
		return activityName;
	}
	/**
	 * @param activityName the activityName to set
	 */
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	/**
	 * @return the activityCode
	 */
	public String getActivityCode() {
		return activityCode;
	}
	/**
	 * @param activityCode the activityCode to set
	 */
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	/**
	 * @return the parentActivityId
	 */
	public String getParentActivityId() {
		return parentActivityId;
	}
	/**
	 * @param parentActivityId the parentActivityId to set
	 */
	public void setParentActivityId(String parentActivityId) {
		this.parentActivityId = parentActivityId;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the acitvityShape
	 */
	public String getAcitvityShape() {
		return acitvityShape;
	}
	/**
	 * @param acitvityShape the acitvityShape to set
	 */
	public void setAcitvityShape(String acitvityShape) {
		this.acitvityShape = acitvityShape;
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
	 * @return the udpatedByName
	 */
	public String getUdpatedByName() {
		return udpatedByName;
	}
	/**
	 * @param udpatedByName the udpatedByName to set
	 */
	public void setUdpatedByName(String udpatedByName) {
		this.udpatedByName = udpatedByName;
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

}
