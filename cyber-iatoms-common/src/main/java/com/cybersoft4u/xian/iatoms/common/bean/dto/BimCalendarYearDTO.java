package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;
import java.util.Date;

import cafe.core.bean.dto.DataTransferObject;
/**
 * 
 * Purpose: 行事曆年檔
 * @author echomou
 * @since  JDK 1.6
 * @date   2016/7/1
 * @MaintenancePersonnel cybersoft
 */
public class BimCalendarYearDTO extends DataTransferObject<String> {
	
	public static enum ATTRIBUTE {
		YEAR("year"),
		WORK_START_HOUR("workStartHour"),
		WORK_START_MIN("workStartMin"),
		WORK_END_HOUR("workEndHour"),
		WORK_END_MIN("workEndMin"),
		MONDAY("monday"),
		TUESDAY("tuesday"),
		WEDNESDAY("wednesday"),
		THURSDAY("thursday"),
		FRIDAY("friday"),
		SATURDAY("saturday"),
		SUNDAY("sunday"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
		CREATED_DATE("createdDate"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		UPDATED_DATE("updatedDate"),
		WEEK_RESTS("weekRests");
		/**
		 * 
		 */
		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	/**
	 * 年
	 */
	private Integer year;
	/**
	 * 上班開始時
	 */
	private String workStartHour;
	/**
	 * 上班開始分
	 */
	private String workStartMin;
	/**
	 * 上班結束時
	 */
	private String workEndHour;
	/**
	 * 上班結束分
	 */
	private String workEndMin;
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
	private Timestamp createdDate;
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
	private Timestamp updatedDate;

	/**
	 * 周修日數組：
	 */
	private String[] weekRests;
	
	
	/**
	 * @return the weekRests
	 */
	public String[] getWeekRests() {
		return weekRests;
	}
	/**
	 * @param weekRests the weekRests to set
	 */
	public void setWeekRests(String[] weekRests) {
		this.weekRests = weekRests;
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
	 * @return the workStartHour
	 */
	public String getWorkStartHour() {
		return workStartHour;
	}
	/**
	 * @param workStartHour the workStartHour to set
	 */
	public void setWorkStartHour(String workStartHour) {
		this.workStartHour = workStartHour;
	}
	/**
	 * @return the workStartMin
	 */
	public String getWorkStartMin() {
		return workStartMin;
	}
	/**
	 * @param workStartMin the workStartMin to set
	 */
	public void setWorkStartMin(String workStartMin) {
		this.workStartMin = workStartMin;
	}
	/**
	 * @return the workEndHour
	 */
	public String getWorkEndHour() {
		return workEndHour;
	}
	/**
	 * @param workEndHour the workEndHour to set
	 */
	public void setWorkEndHour(String workEndHour) {
		this.workEndHour = workEndHour;
	}
	/**
	 * @return the workEndMin
	 */
	public String getWorkEndMin() {
		return workEndMin;
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
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Timestamp createdDate) {
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
	public Timestamp getUpdatedDate() {
		return updatedDate;
	}
	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}
	/**
	 * @param workEndMin the workEndMin to set
	 */
	public void setWorkEndMin(String workEndMin) {
		this.workEndMin = workEndMin;
	}
}
