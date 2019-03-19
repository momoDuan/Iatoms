package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BimCalendarDayDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimCalendarYearDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IOldDataBatchDAO;

import cafe.core.bean.CoreMessageCode;
import cafe.core.config.GenericConfigManager;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
/**
 * Purpose: 舊資料批次訪問層實現類
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2017-7-13
 * @MaintenancePersonnel CrissZhang
 */
public class OldDataBatchDAO extends GenericBaseDAO implements IOldDataBatchDAO{
	
	/**
	 * 系统日志文件控件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, OldDataBatchDAO.class);

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.IOldDataBatchDAO.dao.IOldDataTransferDAO#listCalendarYear()
	 */
	@Override
	public List<BimCalendarYearDTO> listCalendarYear()throws DataAccessException {
		long startTime = System.currentTimeMillis();
		
		List<BimCalendarYearDTO> result = null;
		//得到schema
		String schema = this.getSchema("iatoms-foms");
		SqlStatement sqlStatement = new SqlStatement();
		StringBuilder builder = new StringBuilder();
		try {
			sqlStatement.addSelectClause("a.year", BimCalendarYearDTO.ATTRIBUTE.YEAR.getValue());
			sqlStatement.addSelectClause("case when a.monday=0 then 'N' else 'Y' end", BimCalendarYearDTO.ATTRIBUTE.MONDAY.getValue());
			sqlStatement.addSelectClause("case when a.tuesday=0 then 'N' else 'Y' end", BimCalendarYearDTO.ATTRIBUTE.TUESDAY.getValue());
			sqlStatement.addSelectClause("case when a.wednesday=0 then 'N' else 'Y' end", BimCalendarYearDTO.ATTRIBUTE.WEDNESDAY.getValue());
			sqlStatement.addSelectClause("case when a.thursday=0 then 'N' else 'Y' end", BimCalendarYearDTO.ATTRIBUTE.THURSDAY.getValue());
			sqlStatement.addSelectClause("case when a.friday=0 then 'N' else 'Y' end", BimCalendarYearDTO.ATTRIBUTE.FRIDAY.getValue());
			sqlStatement.addSelectClause("case when a.saturday=0 then 'N' else 'Y' end", BimCalendarYearDTO.ATTRIBUTE.SATURDAY.getValue());
			sqlStatement.addSelectClause("case when a.sunday=0 then 'N' else 'Y' end", BimCalendarYearDTO.ATTRIBUTE.SUNDAY.getValue());
			// 新增人員編號
			sqlStatement.addSelectClause("a.MODIFYINFO_ADD_USER_ID", BimCalendarYearDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			// 新增人員姓名
			sqlStatement.addSelectClause("b.name", BimCalendarYearDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			// 新增日期
			sqlStatement.addSelectClause("a.MODIFYINFO_ADD_DATE", BimCalendarYearDTO.ATTRIBUTE.CREATED_DATE.getValue());
			// 異動人員編號
			sqlStatement.addSelectClause("a.MODIFYINFO_MODIFY_USER_ID", BimCalendarYearDTO.ATTRIBUTE.UPDATED_BY_ID.getValue());
			// 異動人員姓名
			sqlStatement.addSelectClause("c.name", BimCalendarYearDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			// 異動日期
			sqlStatement.addSelectClause("a.MODIFYINFO_MODIFY_DATE", BimCalendarYearDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			
			builder.append(schema).append(".CCM_Year a left join ");
			builder.append(schema).append(".ORG_PARTY b on a.MODIFYINFO_ADD_USER_ID=b.identifier left join ");
			builder.append(schema).append(".ORG_PARTY c on a.MODIFYINFO_MODIFY_USER_ID=c.identifier");
			sqlStatement.addFromExpression(builder.toString());
			
			builder.delete(0, builder.length());
			builder.append("a.year in (convert(int,substring(convert(varchar, getdate(),112),1,4)),");
			builder.append("(convert(int,substring(convert(varchar, getdate(),112),1,4)) + 1)) ");
			builder.append(" and a.deleted=0 ");
			sqlStatement.addWhereClause(builder.toString());
		//	sqlStatement.addWhereClause("a.year in (2017,2018) and a.deleted=0");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(BimCalendarYearDTO.class);
			LOGGER.debug("listCalendarYear()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			
			long endTime = System.currentTimeMillis();
			LOGGER.debug("listCalendarYear()", "testSpeedTime DAO totalTime:" + (endTime - startTime));
			
			return result;
		} catch (Exception e) {
			LOGGER.error("listCalendarYear()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.IOldDataBatchDAO.dao.IOldDataTransferDAO#listCalendarDate()
	 */
	@Override
	public List<BimCalendarDayDTO> listCalendarDate()throws DataAccessException {
		long startTime = System.currentTimeMillis();
		
		List<BimCalendarDayDTO> result = null;
		//得到schema
		String schema = this.getSchema("iatoms-foms");
		SqlStatement sqlStatement = new SqlStatement();
		StringBuilder builder = new StringBuilder();
		try {
			// 日期
			sqlStatement.addSelectClause("a.date", BimCalendarDayDTO.ATTRIBUTE.DAY.getValue());
			// 是否為假日
		//	sqlStatement.addSelectClause("case when a.Special=0 then 'N' else 'Y' end", BimCalendarDayDTO.ATTRIBUTE.IS_HOLIDAY.getValue());
			sqlStatement.addSelectClause("case when a.Special is null then 'N' else 'Y' end", BimCalendarDayDTO.ATTRIBUTE.IS_HOLIDAY.getValue());
			// 說明
			sqlStatement.addSelectClause("a.remark", BimCalendarDayDTO.ATTRIBUTE.COMMENT.getValue());
			// 新增人員編號
			sqlStatement.addSelectClause("a.MODIFYINFO_ADD_USER_ID", BimCalendarDayDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			// 新增人員姓名
			sqlStatement.addSelectClause("b.name", BimCalendarDayDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			// 新增日期
			sqlStatement.addSelectClause("a.MODIFYINFO_ADD_DATE", BimCalendarDayDTO.ATTRIBUTE.CREATED_DATE.getValue());
			// 異動人員編號
			sqlStatement.addSelectClause("a.MODIFYINFO_MODIFY_USER_ID", BimCalendarDayDTO.ATTRIBUTE.UPDATED_BY_ID.getValue());
			// 異動人員姓名
			sqlStatement.addSelectClause("c.name", BimCalendarDayDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			// 異動日期
			sqlStatement.addSelectClause("a.MODIFYINFO_MODIFY_DATE", BimCalendarDayDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			
			builder.append(schema).append(".CCM_Holiday a left join ");
			builder.append(schema).append(".ORG_PARTY b on a.MODIFYINFO_ADD_USER_ID=cast(b.[identifier] as nvarchar) left join ");
			builder.append(schema).append(".ORG_PARTY c on a.MODIFYINFO_MODIFY_USER_ID=cast(c.[identifier] as nvarchar)");
			sqlStatement.addFromExpression(builder.toString());
			
/*			builder.delete(0, builder.length());
			builder.append("a.YEAR_ID in (select identifier from ");
			builder.append(schema).append(".CCM_Year where year in (2017,2018) and deleted=0) and a.deleted=0");
			sqlStatement.addWhereClause(builder.toString());*/
			
			builder.delete(0, builder.length());
			builder.append("a.YEAR_ID in (select identifier from ");
			builder.append(schema).append(".CCM_Year where year in (convert(int,");
			builder.append("substring(convert(varchar, getdate(),112),1,4)),");
			builder.append("(convert(int,substring(convert(varchar, getdate(),112),1,4)) + 1))");
			builder.append(" and deleted=0) and a.deleted=0");
			sqlStatement.addWhereClause(builder.toString());
			
			sqlStatement.setOrderByExpression("a.date");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(BimCalendarDayDTO.class);
			LOGGER.debug("listCalendarDate()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			
			long endTime = System.currentTimeMillis();
			LOGGER.debug("listCalendarDate()", "testSpeedTime DAO totalTime:" + (endTime - startTime));
			
			return result;
		} catch (Exception e) {
			LOGGER.error("listCalendarDate()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

}