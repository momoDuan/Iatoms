package com.cybersoft4u.xian.iatoms.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;

import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;

/**
 * Purpose: 日期時間通用方法
 * @author evanliu
 * @since  JDK 1.6
 * @date   2016年7月4日
 * @MaintenancePersonnel evanliu
 */
public class IAtomsDateTimeUtils extends DateTimeUtils {
	/**
	 * Purpose:計算兩個時間相隔天數
	 * @author evanliu
	 * @param bigDate:大的日期
	 * @param smallDate:小的日期
	 * @return int:日期相關天數
	 */
	public static int dateSubtractionDay(Date smallDate, Date bigDate) {
		int result = 0;
		try {
		    long betweenMillis = dateSubtractionMillis(smallDate, bigDate);      
	        long between_days= betweenMillis / (1000 * 3600 * 24);
		    result = (int)between_days;
		} catch (Throwable e) {
			return 0;
		}
		return result;
	}
	/**
	 * Purpose:計算兩個時間相隔毫秒數
	 * @author CrissZhang
	 * @param bigDate:大的日期
	 * @param smallDate:小的日期
	 * @return int:日期相關毫秒數
	 */
	public static long dateSubtractionMillis(Date smallDate, Date bigDate) {
		long result = 0;
		try {
			Calendar aCalendar = Calendar.getInstance();
		    aCalendar.setTime(smallDate);
		    long time1 = aCalendar.getTimeInMillis(); 
		    aCalendar.setTime(bigDate);
		    long time2 = aCalendar.getTimeInMillis();      
	        result = time2 - time1;
		} catch (Throwable e) {
			return 0;
		}
		return result;
	}
	/**
	 * Purpose:計算某個日期幾個月前的一天
	 * @author CrissZhang
	 * @param date ： 日期
	 * @param sdf ： 格式化
	 * @param allMonth ： 幾個月前
	 * @return Date ： 返回計算后的日期
	 */
	public static Date lastMonth(Date date, int allMonth, SimpleDateFormat sdf) {
		if(sdf == null){
			sdf = new SimpleDateFormat(DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
		}
		Date resultDate = null;
		int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(date));
		int month = Integer.parseInt(new SimpleDateFormat("MM").format(date)) - allMonth;
		int day = Integer.parseInt(new SimpleDateFormat("dd").format(date));
		String resultDateStr = null;
		if(month <= 0){
			int yearFlag = (month*(-1))/12 + 1;
			int monthFlag = (month *(-1))%12;
			year -= yearFlag;
			month=monthFlag*(-1) +12;
		} else if(day > 28){
			if(month == 2){
				if((year % 400 == 0)||(year % 4 == 0 && year % 100 != 0)){
					day = 29;
				} else {
					day = 28;
				}
			} else if((month == 4 || month == 6 || month == 9 || month == 11) && day == 31){
				day = 30;
			}
		}
		String y = year + IAtomsConstants.MARK_EMPTY_STRING;
		String m = IAtomsConstants.MARK_EMPTY_STRING;
		String d = IAtomsConstants.MARK_EMPTY_STRING;
		if(month < 10) {
			m = "0" + month;
		} else {
			m = month + IAtomsConstants.MARK_EMPTY_STRING;
		}
		if(day < 10) {
			d = "0" + day;
		} else {
			d = day + IAtomsConstants.MARK_EMPTY_STRING;
		}
		if(sdf.toPattern().contains(IAtomsConstants.MARK_BACKSLASH)){
			resultDateStr = y + IAtomsConstants.MARK_BACKSLASH + m + IAtomsConstants.MARK_BACKSLASH + d;
		} else if(sdf.toPattern().contains(IAtomsConstants.MARK_MIDDLE_LINE)){
			resultDateStr = y + IAtomsConstants.MARK_MIDDLE_LINE + m + IAtomsConstants.MARK_MIDDLE_LINE + d;
		} else if(sdf.toPattern().contains(IAtomsConstants.MARK_NO)){
			resultDateStr = y + IAtomsConstants.MARK_NO + m + IAtomsConstants.MARK_NO + d;
		} else {
			resultDateStr = null;
		}
		if(StringUtils.hasText(resultDateStr)){
			try {
				resultDate = sdf.parse(resultDateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return resultDate;
	}
}
