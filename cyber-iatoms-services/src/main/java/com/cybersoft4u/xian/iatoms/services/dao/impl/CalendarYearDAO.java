package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.config.GenericConfigManager;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.services.dao.ICalendarYearDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCalendarYear;
/**
 * 
 * Purpose:行事曆日檔DAO
 * @author echomou
 * @since  JDK 1.6
 * @date   2016/7/4
 * @MaintenancePersonnel cybersoft
 */
public class CalendarYearDAO extends GenericBaseDAO<BimCalendarYear> implements ICalendarYearDAO {
	/**
	 * 日誌記錄器
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.DAO, CalendarYearDAO.class);
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ICalendarYearDAO#count()
	 */
	@Override
	public boolean isNoData() throws DataAccessException {
		boolean isNoData = false;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("COUNT(1)");
			sqlStatement.addFromExpression(schema + ".BIM_CALENDAR_YEAR");
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
	
}
