package com.cybersoft4u.xian.iatoms.services.dmo;

import java.util.Date;

import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BimCalendarDayDTO;
/**
 * 
 * Purpose: 行事曆日檔
 * @author echomou
 * @since  JDK 1.6
 * @date   2016/6/30
 * @MaintenancePersonnel cybersoft
 */
public class BimCalendarDay extends DomainModelObject<String,BimCalendarDayDTO>{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 114181434238249837L;
	/**
	 * 日期
	 */
	private Date day;
	/**
	 * 是否假日
	 */
	private String isHoliday = "N";
	/**
	 * 說明
	 */
	private String comment;
	/**
	 * 創造者id
	 */
	private String createdById;
	/**
	 * 創造者name
	 */
	private String createdByName;
	/**
	 * 創造日期
	 */
	private Date createdDate;
	/**
	 * 更新者id
	 */
	private String updatedById;
	/**
	 * 更新者name
	 */
	private String updatedByName;
	/**
	 * 更新日期
	 */
	private Date updatedDate;
	/**
	 * Constructor:
	 */
	public BimCalendarDay() {
		super();
	}
	/**
	 * Constructor:
	 */
	public BimCalendarDay(Date day, String isHoliday) {
		this.day = day;
		this.isHoliday = isHoliday;
	}
	/**
	 * Constructor:
	 */
	public BimCalendarDay(Date day, String isHoliday, String comment, String createdById, String createdByName,
			Date createdDate, String updatedById, String updatedByName, Date updatedDate) {
		this.day = day;
		this.isHoliday = isHoliday;
		this.comment = comment;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
	}
	
	/**
	 * @return the day
	 */
	public Date getDay() {
		return day;
	}
	/**
	 * @param day the day to set
	 */
	public void setDay(Date day) {
		this.day = day;
	}
	/**
	 * @return the isHoliday
	 */
	public String getIsHoliday() {
		return isHoliday;
	}
	/**
	 * @param isHoliday the isHoliday to set
	 */
	public void setIsHoliday(String isHoliday) {
		this.isHoliday = isHoliday;
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
}
