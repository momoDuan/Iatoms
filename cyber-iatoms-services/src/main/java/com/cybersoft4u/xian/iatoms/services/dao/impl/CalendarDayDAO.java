package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimCalendarDayDTO;
import com.cybersoft4u.xian.iatoms.services.dao.ICalendarDayDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCalendarDay;
/**
 * 
 * Purpose:行事曆日檔DAO
 * @author echomou
 * @since  JDK 1.6
 * @date   2016/7/4
 * @MaintenancePersonnel cybersoft
 */
public class CalendarDayDAO extends GenericBaseDAO<BimCalendarDay> implements ICalendarDayDAO {
	
	
	/**
	 * 系统日志记录物件 
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(CalendarDayDAO.class);
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ICalendarDayDAO#listBy(java.lang.Integer)
	 */
	@Override
	public List<BimCalendarDayDTO> listBy(Integer year) throws DataAccessException {
		LOGGER.debug(this.getClass().getName() + "year" + year);
		List<BimCalendarDayDTO> listHoliday = null;
		String schema = this.getMySchema();
		SqlStatement sqlStatement = new SqlStatement();
		try {
			sqlStatement.addSelectClause("day.DAY", BimCalendarDayDTO.ATTRIBUTE.DAY.getValue());
			sqlStatement.addSelectClause("day.IS_HOLIDAY", BimCalendarDayDTO.ATTRIBUTE.IS_HOLIDAY.getValue());
			sqlStatement.addFromExpression(schema + ".BIM_CALENDAR_DAY day");
			if (year.intValue() > 0) {
				sqlStatement.addWhereClause("DATEPART(year,day) = :year", year);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(BimCalendarDayDTO.class);
			LOGGER.debug(this.getClass().getName() + "SQL---------->" + sqlQueryBean.toString());
			listHoliday = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".listby() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return listHoliday;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ICalendarDayDAO#deleteHolidayByYear(java.lang.Integer)
	 */
	@Override
	public void deleteHolidayByYear(Integer year) throws DataAccessException {
		LOGGER.debug(this.getClass().getName() + "year" + year);
		String schema = this.getMySchema();
		SqlQueryBean sql = new SqlQueryBean();
		try {
			sql.append(" delete from " + schema + ".BIM_CALENDAR_DAY where DATEPART(year,day) =:year and COMMENT is null");
			sql.setParameter("year", year);
			LOGGER.debug(this.getClass().getName() + "SQL---------->" + sql.toString());
			this.getDaoSupport().updateByNativeSql(sql);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".deleteHolidayByYear() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ICalendarDayDAO#getHoildayByDate(java.util.Date, java.util.Date)
	 */
	@Override
	public List<BimCalendarDayDTO> getHoildayByDate(Date startDate, Date endDate) throws DataAccessException {
		LOGGER.debug(this.getClass().getName() + "startDate" + startDate);
		LOGGER.debug(this.getClass().getName() + "endDate" + endDate);
		List<BimCalendarDayDTO> listHoliday = null;
		String schema = this.getMySchema();
		SqlStatement sqlStatement = new SqlStatement();
		try {
			sqlStatement.addSelectClause("calendarDay.DAY", BimCalendarDayDTO.ATTRIBUTE.DAY.getValue());
			sqlStatement.addSelectClause("calendarDay.IS_HOLIDAY", BimCalendarDayDTO.ATTRIBUTE.IS_HOLIDAY.getValue());
			sqlStatement.addFromExpression(schema + ".BIM_CALENDAR_DAY calendarDay");
			sqlStatement.addWhereClause("calendarDay.day > :startDate", startDate);
			sqlStatement.addWhereClause("calendarDay.day < :endDate", endDate);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(BimCalendarDayDTO.class);
			LOGGER.debug(this.getClass().getName() + "SQL---------->" + sqlQueryBean.toString());
			listHoliday = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".listby() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return listHoliday;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ICalendarDayDAO#culculateDate(java.util.Date, Integer, Integer, Integer)
	 */
	@Override
	public Object[] culculateDate(Date currentDate, Integer responseAddDays, Integer arriveAddDays,
			Integer completeAddDays) {
		LOGGER.debug(this.getClass().getName() + "currentDate" + currentDate);
		LOGGER.debug(this.getClass().getName() + "responseAddDays" + responseAddDays);
		LOGGER.debug(this.getClass().getName() + "arriveAddDays" + arriveAddDays);
		LOGGER.debug(this.getClass().getName() + "completeAddDays" + completeAddDays);
		Object[] objects = null;
		String schema = this.getMySchema();
		SqlQueryBean sqlQueryBean = new SqlQueryBean();
		try {
			sqlQueryBean.append("declare @responseDate date,@arriveDate date,@completeDate date ");
			sqlQueryBean.append("exec").append(schema).append(".usp_culculateSlaDate :currentDate, :responseAddDays, :arriveAddDays, :completeAddDays,");
			sqlQueryBean.append("@responseDate output, @arriveDate output, @completeDate output ");
			sqlQueryBean.append("select @responseDate as responseDate,@arriveDate as arriveDate,@completeDate as completeDate");
			sqlQueryBean.setParameter("currentDate", currentDate);
			sqlQueryBean.setParameter("responseAddDays", responseAddDays);
			sqlQueryBean.setParameter("arriveAddDays", arriveAddDays);
			sqlQueryBean.setParameter("completeAddDays", completeAddDays);
			LOGGER.debug(this.getClass().getName() + "SQL---------->" + sqlQueryBean.toString());
			List list = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if(!CollectionUtils.isEmpty(list)){
				objects = (Object[]) list.get(0);
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".culculateDate() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return objects;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ICalendarDayDAO#checkIsHoliday(java.util.Date)
	 */
	public Boolean checkIsHoliday(Date currentDate) throws DataAccessException {
		Boolean isHoliday = Boolean.FALSE;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("calDay.IS_HOLIDAY", BimCalendarDayDTO.ATTRIBUTE.IS_HOLIDAY.getValue());
			sqlStatement.addFromExpression(schema + ".BIM_CALENDAR_DAY calDay");
			if (currentDate != null) {
				sqlStatement.addWhereClause("calDay.DAY = :currentDate", currentDate);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(BimCalendarDayDTO.class);
			List<BimCalendarDayDTO> bimCalendarDayDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(bimCalendarDayDTOs)) {
				if (IAtomsConstants.YES.equals(bimCalendarDayDTOs.get(0).getIsHoliday())) {
					isHoliday = Boolean.TRUE;
				}
			}
		} catch (Exception e) {
			LOGGER.error(".checkIsHoliday() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return isHoliday;
	}
	@Override
	public List<BimCalendarDayDTO> queryDateByYear(Integer year)
			throws DataAccessException {
		LOGGER.debug("queryDateByYear:parameter", "year", year);
		List<BimCalendarDayDTO> calendarDays = null;
		try {
			SqlStatement sql = new SqlStatement();
			String schema = this.getMySchema();
			sql.addSelectClause("day", BimCalendarDayDTO.ATTRIBUTE.DAY.getValue());
			sql.addSelectClause("IS_HOLIDAY", BimCalendarDayDTO.ATTRIBUTE.IS_HOLIDAY.getValue());
			sql.addSelectClause("COMMENT", BimCalendarDayDTO.ATTRIBUTE.COMMENT.getValue());
			sql.addFromExpression(schema + ".BIM_CALENDAR_DAY");
			if (year.intValue() != 0) {
				sql.addWhereClause("DATEPART(year,day) =:year");
			}
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			sqlQueryBean.setParameter("year", year);
			LOGGER.debug("queryDateByYear:", "SQL--->", sqlQueryBean.toString());
			AliasBean aliasBean = sql.createAliasBean(BimCalendarDayDTO.class);
			calendarDays = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("queryDateByYear()", "is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return calendarDays;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ICalendarDayDAO#isNoData()
	 */
	@Override
	public boolean isNoData() throws DataAccessException {
		boolean isNoData = false;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("COUNT(1)");
			sqlStatement.addFromExpression(schema + ".BIM_CALENDAR_DAY");
			//打印SQL語句
			LOGGER.debug("isNoData()", "SQL : ", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				Integer count = result.get(0);
				if(count == 0){
					isNoData = true;
				}
			}
		} catch (Exception e) {
			LOGGER.error("isNoData()", "error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE);
		}
		return isNoData;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ICalendarDayDAO#deleteTransferData()
	 */
	@Override
	public void deleteTransferData() throws DataAccessException {
		try {
			String schema = this.getMySchema();
			SqlQueryBean sqlQueryBean = new SqlQueryBean("delete ");
			sqlQueryBean.append(schema).append(".BIM_CALENDAR_YEAR ");
			sqlQueryBean.append(" where YEAR in (convert(int,substring(convert(varchar, getdate(),112),1,4)),");
			sqlQueryBean.append(" (convert(int,substring(convert(varchar, getdate(),112),1,4)) + 1));");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".BIM_CALENDAR_DAY ");
			sqlQueryBean.append(" where DAY >= (CONVERT(char(5),getdate(),120)+'01-01')");
			sqlQueryBean.append(" and DAY<= (CONVERT(char(5),DATEADD(year,1,getdate()),120)+'12-31');");
			
			LOGGER.debug("deleteTransferData()", "sql:" + sqlQueryBean.toString());
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("deleteTransferData()", e.getMessage(), e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
	}
}
