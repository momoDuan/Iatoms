package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.util.CollectionUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmSystemLoggingDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.SystemLogFormDTO;
import com.cybersoft4u.xian.iatoms.services.dao.ISystemLogDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmSystemLogging;

/**
 * 
 * Purpose: 系统日志DAO 
 * @author amandawang
 * @since  JDK 1.7
 * @date   2016年7月5日
 * @MaintenancePersonnel amandawang
 */
@SuppressWarnings("unchecked")
public class SystemLogDAO extends GenericBaseDAO<AdmSystemLogging> implements ISystemLogDAO {
	
	/**
	 * 系統日志記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(SystemLogDAO.class);

	/**
	 * 
	 * Constructor: 無參構造子
	 */
	public SystemLogDAO(){
		super();
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISystemLogDAO#listBy(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	public List<AdmSystemLoggingDTO> listBy(String account, String formDate,
			String toDate, String sort, String order, Integer currentPage, Integer pageSize) throws DataAccessException {
		LOGGER.error("SystemLogDAO.list() --> account : " + account);
		LOGGER.error("SystemLogDAO.list() --> formDate : " + formDate);
		LOGGER.error("SystemLogDAO.list() --> toDate : " + toDate);
		LOGGER.error("SystemLogDAO.list() --> sort : " + sort);
		LOGGER.error("SystemLogDAO.list() --> order : " + order);
		LOGGER.error("SystemLogDAO.list() --> currentPage : " + currentPage);
		LOGGER.error("SystemLogDAO.list() --> pageSize : " + pageSize);
		List<AdmSystemLoggingDTO> admSystemLoggingDTOs = null;
		try{
			SqlQueryBean sql = new SqlQueryBean();
			String schema = this.getMySchema();
			sql.append(" SELECT * FROM(");
			//Bug #2437 賬號(中文姓名) update by 2017/09/15
			sql.append(" SELECT au.USER_ID as userId,(case when au.CNAME is not null or au.CNAME <>'' then au.ACCOUNT + '(' + au.CNAME + ')' else au.ACCOUNT end )  as userName,au.ACCOUNT as account,");
			sql.append(" lg.FUNCTION_NAME as functionId,fun.FUNCTION_NAME as functionName,lg.OPERATION_TIME as operationTime,lg.IP as ip,");
							//+ "lg.CONTENT as content,"
			sql.append(" lg.LOG_ID as logId,lg.RESULT as result,lg.LOG_CATEGRE as logCategre,bpid.ITEM_NAME as logCategreName");
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(" FROM ");
			fromBuffer.append(schema);
			fromBuffer.append(".ADM_SYSTEM_LOGGING lg LEFT JOIN ");
			fromBuffer.append(schema);
			fromBuffer.append(".ADM_FUNCTION_TYPE fun ON fun.FUNCTION_ID = lg.FUNCTION_NAME LEFT JOIN ");
			fromBuffer.append(schema);
			fromBuffer.append(".ADM_USER au ON lg.USER_ID = au.USER_ID LEFT JOIN ");
			fromBuffer.append(schema);
			//bug2359 update by 2017/09/06
		//	fromBuffer.append(".BASE_PARAMETER_ITEM_DEF bpid ON (case when CHARINDEX( '.',lg.LOG_CATEGRE )>0 then substring( lg.LOG_CATEGRE, CHARINDEX( '.',lg.LOG_CATEGRE  )+1,len(lg.LOG_CATEGRE)) else lg.LOG_CATEGRE end ) = bpid.ITEM_VALUE AND bpid.APPROVED_FLAG = 'Y' AND bpid.BPTD_CODE =:accessRight ");
			// 處理approvedFlag
			fromBuffer.append(".BASE_PARAMETER_ITEM_DEF bpid ON (case when CHARINDEX( '.',lg.LOG_CATEGRE )>0 then substring( lg.LOG_CATEGRE, CHARINDEX( '.',lg.LOG_CATEGRE  )+1,len(lg.LOG_CATEGRE)) else lg.LOG_CATEGRE end ) = bpid.ITEM_VALUE AND isnull(bpid.DELETED, 'N') = 'N' AND bpid.BPTD_CODE =:accessRight ");
			sql.append(fromBuffer.toString());
			sql.setParameter("accessRight", IAtomsConstants.ACCESS_RIGHT);
			sql.append(" WHERE 1=1 ");
			//帳號
			if(StringUtils.hasText(account)){
				sql.append(" AND au.ACCOUNT like :account");
				sql.setParameter(AdmSystemLoggingDTO.ATTRIBUTE.ACCOUNT.getValue(), account);
				LOGGER.debug("SystemLogDAO.list() --> account: " + account);
			}
			//時間起
			if(StringUtils.hasText(formDate)){
				sql.append(" AND CONVERT(varchar,lg.OPERATION_TIME,111) >= :queryFromDate");
				sql.setParameter(SystemLogFormDTO.QUERY_PAGE_PARAM_FROM_DATE, formDate);
				LOGGER.debug("SystemLogDAO.list() --> formDate: " + formDate);
			}
			//時間迄
			if(StringUtils.hasText(toDate)){
				sql.append(" AND CONVERT(varchar,lg.OPERATION_TIME,111) <= :queryToDate");
				sql.setParameter(SystemLogFormDTO.QUERY_PAGE_PARAM_TO_DATE, toDate);
				LOGGER.debug("SystemLogDAO.list() --> toDate: " + toDate);
			}
			sql.setStartPage(currentPage.intValue());
			sql.setPageSize(pageSize.intValue());
			sql.append(")temp");
			if(StringUtils.hasText(sort) && StringUtils.hasText(order)){
				fromBuffer = new StringBuffer();
				fromBuffer.append(" ORDER BY temp.");
				fromBuffer.append(sort);
				fromBuffer.append(" ");
				fromBuffer.append(order);
				sql.append(fromBuffer.toString());
				LOGGER.debug("SystemLogDAO.list() --> sort:" + sort);
				LOGGER.debug("SystemLogDAO.list() --> order:" + order);
			}
			AliasBean alias = new AliasBean(AdmSystemLoggingDTO.class);
			alias.addScalar(AdmSystemLoggingDTO.ATTRIBUTE.USER_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(AdmSystemLoggingDTO.ATTRIBUTE.USER_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(AdmSystemLoggingDTO.ATTRIBUTE.ACCOUNT.getValue(), StringType.INSTANCE);
			alias.addScalar(AdmSystemLoggingDTO.ATTRIBUTE.FUNCTION_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(AdmSystemLoggingDTO.ATTRIBUTE.FUNCTION_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(AdmSystemLoggingDTO.ATTRIBUTE.OPERATION_TIME.getValue(), TimestampType.INSTANCE);
			alias.addScalar(AdmSystemLoggingDTO.ATTRIBUTE.IP.getValue(), StringType.INSTANCE);
			alias.addScalar(AdmSystemLoggingDTO.ATTRIBUTE.RESULT.getValue(), StringType.INSTANCE);
		//	alias.addScalar(AdmSystemLoggingDTO.ATTRIBUTE.CONTENT.getValue(), StringType.INSTANCE);
			alias.addScalar(AdmSystemLoggingDTO.ATTRIBUTE.LOG_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(AdmSystemLoggingDTO.ATTRIBUTE.LOG_CATEGRE.getValue(), StringType.INSTANCE);
			alias.addScalar(AdmSystemLoggingDTO.ATTRIBUTE.LOG_CATEGRE_NAME.getValue(), StringType.INSTANCE);
			admSystemLoggingDTOs = this.getDaoSupport().findByNativeSql(sql, alias);
			LOGGER.error("SystemLogDAO.list() --> sql : " + sql.toString());
		}catch(Exception e){
			LOGGER.error("SystemLogDAO.list() is error: " + e, e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return admSystemLoggingDTOs;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISystemLogDAO#count(java.lang.String, java.lang.String, java.lang.String)
	 */
	public Integer count(String account, String formDate, String toDate)
			throws DataAccessException {
		LOGGER.error("SystemLogDAO.count() --> account : " + account);
		LOGGER.error("SystemLogDAO.count() --> formDate : " + formDate);
		LOGGER.error("SystemLogDAO.count() --> toDate : " + toDate);
		Integer result = Integer.valueOf(0);
		try{
			SqlQueryBean sql = new SqlQueryBean();
			String schema = this.getMySchema();
			sql.append(" SELECT count(lg.USER_ID)");
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(" FROM ");
			fromBuffer.append(schema);
			fromBuffer.append(".ADM_SYSTEM_LOGGING lg LEFT JOIN ");
			fromBuffer.append(schema);
			fromBuffer.append(".ADM_USER us ON us.USER_ID = lg.USER_ID ");
			sql.append(fromBuffer.toString());
			sql.append(" WHERE 1=1");
			//帳號
			if(StringUtils.hasText(account)){
				sql.append(" AND us.ACCOUNT like :account");
				sql.setParameter(AdmSystemLoggingDTO.ATTRIBUTE.ACCOUNT.getValue(), account);
				LOGGER.debug("SystemLogDAO.count() --> account: " + account);
			}
			//時間起
			if(StringUtils.hasText(formDate)){
				sql.append(" AND CONVERT(varchar,lg.OPERATION_TIME,111) >= :queryFromDate");
				sql.setParameter(SystemLogFormDTO.QUERY_PAGE_PARAM_FROM_DATE, formDate);
				LOGGER.debug("SystemLogDAO.count() --> formDate: " + formDate);
			}
			//時間迄
			if(StringUtils.hasText(toDate)){
				sql.append(" AND CONVERT(varchar,lg.OPERATION_TIME,111) <= :queryToDate");
				sql.setParameter(SystemLogFormDTO.QUERY_PAGE_PARAM_TO_DATE, toDate);
				LOGGER.debug("SystemLogDAO.count() --> toDate: " + toDate);
			}
			List<Integer> list = this.getDaoSupport().findByNativeSql(sql);
			LOGGER.debug("SystemLogDAO.count() --> sql: " + sql.toString());
			if(!CollectionUtils.isEmpty(list)){
				result = list.get(0);
			}
		}catch(Exception e){
			LOGGER.error("SystemLogDAO.count() is error: " + e, e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return result;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISystemLogDAO#deleteHistoryLog()
	 */
	@Override
	public void deleteHistoryLog(Date deleteDate) throws DataAccessException {
		try {
			// 沒傳入日期不處理
			if(deleteDate != null){
				String schema = this.getMySchema();
				SqlQueryBean sqlQueryBean = new SqlQueryBean("delete ");
				sqlQueryBean.append(schema).append(".ADM_SYSTEM_LOGGING_HISTORY ");
				// 刪除設定日期之前的所有歷史log
			//	sqlQueryBean.append(" where OPERATION_TIME<= :deleteDate");
				sqlQueryBean.append(" where CREATED_DATE<= :deleteDate");
				sqlQueryBean.setParameter("deleteDate", deleteDate);
				LOGGER.debug("deleteHistoryLog()", "sql:" + sqlQueryBean.toString());
				this.getDaoSupport().updateByNativeSql(sqlQueryBean);
				this.getDaoSupport().flush();
			}
		} catch (Exception e) {
			LOGGER.error("deleteHistoryLog()", e.getMessage(), e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISystemLogDAO#transferSystemLog(java.util.Date)
	 */
	@Override
	public void transferSystemLog(Date transferDate) throws DataAccessException {
		LOGGER.debug("transferSystemLog", "parameter:transferDate=" + transferDate);
		try {
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("exec usp_Move_To_History_System_Log :transferDate");
			sqlQueryBean.setParameter("transferDate", transferDate);
			LOGGER.debug("transferSystemLog", "sql---->", sqlQueryBean.toString() );
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("transferSystemLog() do PROCEDURE usp_Move_To_History_System_Log is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	
}

