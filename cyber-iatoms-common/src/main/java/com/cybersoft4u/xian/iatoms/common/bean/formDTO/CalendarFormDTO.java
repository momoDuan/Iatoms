package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.Date;
import java.util.List;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BimCalendarDayDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimCalendarYearDTO;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;
/**
 * Purpose: 行事曆formDTO
 * @author hungli
 * @since  JDK 1.6
 * @date   2016/6/30
 * @MaintenancePersonnel cybersoft
 */
public class CalendarFormDTO  extends AbstractSimpleListFormDTO {
	
	
	
	//行事曆日檔
	private BimCalendarDayDTO  calendarDayDTO;
	//行事曆年檔
	private BimCalendarYearDTO calendarYearDTO;
	//行事曆日檔集合
	private List<BimCalendarDayDTO> calendarDayDTOs;
	//行事曆年檔集合
	private List<BimCalendarYearDTO> calendarYearDTOs;
	public static final String QUERY_YEAR = "queryYear";
	public static final String DAY = "day";
	public static final String FIRST_DAY = "-01-01";
	public static final String LAST_DAY = "-12-31";
	//
	private Integer queryYear;
	private Date day;
	private String weekRests;
	private List<Integer> weekRestLists;
	
 	/**
	 * @return the weekRestLists
	 */
	public List<Integer> getWeekRestLists() {
		return weekRestLists;
	}
	/**
	 * @param weekRestLists the weekRestLists to set
	 */
	public void setWeekRestLists(List<Integer> weekRestLists) {
		this.weekRestLists = weekRestLists;
	}
	/**
	 * @return the weekRests
	 */
	public String getWeekRests() {
		return weekRests;
	}
	/**
	 * @param weekRests the weekRests to set
	 */
	public void setWeekRests(String weekRests) {
		this.weekRests = weekRests;
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
	 * @return the calendarDayDTO
	 */
	public BimCalendarDayDTO getCalendarDayDTO() {
		return calendarDayDTO;
	}
	/**
	 * @param calendarDayDTO the calendarDayDTO to set
	 */
	public void setCalendarDayDTO(BimCalendarDayDTO calendarDayDTO) {
		this.calendarDayDTO = calendarDayDTO;
	}
	/**
	 * @return the calendarYearDTO
	 */
	public BimCalendarYearDTO getCalendarYearDTO() {
		return calendarYearDTO;
	}
	/**
	 * @param calendarYearDTO the calendarYearDTO to set
	 */
	public void setCalendarYearDTO(BimCalendarYearDTO calendarYearDTO) {
		this.calendarYearDTO = calendarYearDTO;
	}
	/**
	 * @return the calendarDayDTOs
	 */
	public List<BimCalendarDayDTO> getCalendarDayDTOs() {
		return calendarDayDTOs;
	}
	/**
	 * @param calendarDayDTOs the calendarDayDTOs to set
	 */
	public void setCalendarDayDTOs(List<BimCalendarDayDTO> calendarDayDTOs) {
		this.calendarDayDTOs = calendarDayDTOs;
	}
	
	/**
	 * @return the calendarYearDTOs
	 */
	public List<BimCalendarYearDTO> getCalendarYearDTOs() {
		return calendarYearDTOs;
	}
	/**
	 * @param calendarYearDTOs the calendarYearDTOs to set
	 */
	public void setCalendarYearDTOs(List<BimCalendarYearDTO> calendarYearDTOs) {
		this.calendarYearDTOs = calendarYearDTOs;
	}
	/**
	 * @return the queryYear
	 */
	public Integer getQueryYear() {
		return queryYear;
	}
	/**
	 * @param queryYear the queryYear to set
	 */
	public void setQueryYear(Integer queryYear) {
		this.queryYear = queryYear;
	}
	
}
