package com.cybersoft4u.xian.iatoms.services.dao;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.services.dmo.BimCalendarYear;
/**
 * 
 * Purpose: 行事曆年檔Dao interface
 * @author echomou
 * @since  JDK 1.6
 * @date   2016/7/4
 * @MaintenancePersonnel cybersoft
 */
public interface ICalendarYearDAO extends IGenericBaseDAO<BimCalendarYear> {
	
	/**
	 * Purpose:判斷當前DB是否存在數據 不存在返回true存在false
	 * @author CrissZhang
	 * @throws DataAccessException:出錯拋出DataAccessException
	 * @return boolean:是否存在數據的boolean值
	 */
	public boolean isNoData() throws DataAccessException;
}
