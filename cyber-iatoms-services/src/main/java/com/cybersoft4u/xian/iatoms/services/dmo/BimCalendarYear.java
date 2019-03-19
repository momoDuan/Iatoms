package com.cybersoft4u.xian.iatoms.services.dmo;

import java.util.Date;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BimCalendarYearDTO;

import cafe.core.dmo.DomainModelObject;
/**
 * 
 * Purpose: 行事曆年檔
 * @author echomou
 * @since  JDK 1.6
 * @date   2016/6/30
 * @MaintenancePersonnel cybersoft
 */
public class BimCalendarYear extends DomainModelObject<String, BimCalendarYearDTO> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4186674544701281077L;
	/**
	 * 年份
	 */
	private Integer year;
	/**
	 * 星期一
	 */
	private String monday;
	/**
	 * 星期二
	 */
	private String tuesday;
	/**
	 * 星期三
	 */
	private String wednesday;
	/**
	 * 星期四
	 */
	private String thursday;
	/**
	 * 星期五
	 */
	private String friday;
	/**
	 * 星期六
	 */
	private String saturday;
	/**
	 * 星期日
	 */
	private String sunday;
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
	public BimCalendarYear() {
		super();
	}
	/**
	 * Constructor:
	 */
	public BimCalendarYear(Integer year, String monday, String tuesday, String wednesday, String thursday, String friday,
			String saturday, String sunday) {
		this.year = year;
		this.monday = monday;
		this.tuesday = tuesday;
		this.wednesday = wednesday;
		this.thursday = thursday;
		this.friday = friday;
		this.saturday = saturday;
		this.sunday = sunday;
	}
	/**
	 * Constructor:
	 */
	public BimCalendarYear(Integer year, String monday, String tuesday, String wednesday, String thursday, String friday,
			String saturday, String sunday, String createdById, String createdByName, Date createdDate, String updatedById,
			String updatedByName, Date updatedDate) {
		this.year = year;
		this.monday = monday;
		this.tuesday = tuesday;
		this.wednesday = wednesday;
		this.thursday = thursday;
		this.friday = friday;
		this.saturday = saturday;
		this.sunday = sunday;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
	}

	/**
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/**
	 * @return the monday
	 */
	public String getMonday() {
		return monday;
	}

	/**
	 * @param monday the monday to set
	 */
	public void setMonday(String monday) {
		this.monday = monday;
	}

	/**
	 * @return the tuesday
	 */
	public String getTuesday() {
		return tuesday;
	}

	/**
	 * @param tuesday the tuesday to set
	 */
	public void setTuesday(String tuesday) {
		this.tuesday = tuesday;
	}

	/**
	 * @return the wednesday
	 */
	public String getWednesday() {
		return wednesday;
	}

	/**
	 * @param wednesday the wednesday to set
	 */
	public void setWednesday(String wednesday) {
		this.wednesday = wednesday;
	}

	/**
	 * @return the thursday
	 */
	public String getThursday() {
		return thursday;
	}

	/**
	 * @param thursday the thursday to set
	 */
	public void setThursday(String thursday) {
		this.thursday = thursday;
	}

	/**
	 * @return the friday
	 */
	public String getFriday() {
		return friday;
	}

	/**
	 * @param friday the friday to set
	 */
	public void setFriday(String friday) {
		this.friday = friday;
	}

	/**
	 * @return the saturday
	 */
	public String getSaturday() {
		return saturday;
	}

	/**
	 * @param saturday the saturday to set
	 */
	public void setSaturday(String saturday) {
		this.saturday = saturday;
	}

	/**
	 * @return the sunday
	 */
	public String getSunday() {
		return sunday;
	}

	/**
	 * @param sunday the sunday to set
	 */
	public void setSunday(String sunday) {
		this.sunday = sunday;
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
