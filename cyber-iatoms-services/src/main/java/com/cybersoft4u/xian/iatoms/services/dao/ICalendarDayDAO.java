package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.Date;
import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BimCalendarDayDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCalendarDay;
/**
 * 
 * Purpose: 行事曆日檔Dao interface
 * @author echomou
 * @since  JDK 1.6
 * @date   2016/7/4
 * @MaintenancePersonnel cybersoft
 */
public interface ICalendarDayDAO extends IGenericBaseDAO<BimCalendarDay> {
	/**
	 * Purpose: 得到行事曆中所有的假日
	 * @author echomou
	 * @throws DataAccessException 出錯時拋出DataAccessException
	 * @return List<BimCalendarDayDTO> 行事曆資料列表
	 */
	public List<BimCalendarDayDTO> listBy(Integer year) throws DataAccessException;
	/**
	 * Purpose: 刪除對應年份放入所有假日信息
	 * @author echomou
	 * @param year 年份
	 * @throws DataAccessException 出錯時拋出DataAccessException
	 * @return void
	 */
	public void deleteHolidayByYear(Integer year) throws DataAccessException;
	
	/**
	 * Purpose:根據日期區間得到這段時間的假日信息
	 * @author CrissZhang
	 * @param startDate ： 起始日期
	 * @param endDate ： 截止日期
	 * @throws DataAccessException ： 出錯時拋出DataAccessException
	 * @return List<BimCalendarDayDTO> : 得到日期信息的DTO集合
	 */
	public List<BimCalendarDayDTO> getHoildayByDate(Date startDate, Date endDate) throws DataAccessException;
	
	/**
	 * Purpose:計算推遲多少個工作日后的一天
	 * @author CrissZhang
	 * @param currentDate ： 當前日期
	 * @param responseAddDays ： 回應增加天數
	 * @param arriveAddDays ： 到場增加天數
	 * @param completeAddDays ：完修增加天數
	 * @return List<Date> : 得到Date的集合
	 */
	public Object[] culculateDate(Date currentDate, Integer responseAddDays, Integer arriveAddDays,
			Integer completeAddDays);
	
	/**
	 * Purpose:傳入日期是否為節假日
	 * @author CarrieDuan
	 * @param currentDate:查詢日期
	 * @throws DataAccessException： 出錯時拋出DataAccessException
	 * @return Boolean：是否為節假日
	 */
	public Boolean checkIsHoliday(Date currentDate) throws DataAccessException;
	
	/**
	 * Purpose:根據年份查詢該年存在說明的日期
	 * @param year:年份
	 * @return List<BimCalendarDay>：存在說明日期的集合
	 * @throws DataAccessException： 出錯時拋出DataAccessException
	 */
	public List<BimCalendarDayDTO> queryDateByYear(Integer year) throws DataAccessException;
	
	/**
	 * Purpose:判斷當前DB是否存在數據 不存在返回true存在false
	 * @author CrissZhang
	 * @throws DataAccessException:出錯拋出DataAccessException
	 * @return boolean:是否存在數據的boolean值
	 */
	public boolean isNoData() throws DataAccessException;
	
	/**
	 * Purpose:刪除所有轉入數據
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return void : 無返回
	 */
	public void deleteTransferData() throws DataAccessException;
}
