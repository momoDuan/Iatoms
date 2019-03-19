package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.util.StringUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ApiAuthorizationInfoDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IApiAuthorizationInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.ApiAuthorizationInfo;

public class ApiAuthorizationInfoDAO extends GenericBaseDAO<ApiAuthorizationInfo> implements IApiAuthorizationInfoDAO {

	/**
	 * 日誌記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog) CafeLogFactory.getLog(ApiAuthorizationInfoDAO.class);
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IApiAuthorizationInfoDAO#getAuthorizationInfoList(java.lang.String)
	 */
	@Override
	public List<ApiAuthorizationInfoDTO> getAuthorizationInfoList(String ip) throws DataAccessException {
		List<ApiAuthorizationInfoDTO> result = null;
		//得到schema
		String schema = this.getMySchema();
		SqlStatement sqlStatement = new SqlStatement();
		try {
			LOGGER.debug("getAuthorizationInfoList()", "ip=" + ip);
			sqlStatement.addSelectClause("CLIENT_CODE",ApiAuthorizationInfoDTO.ATTRIBUTE.CLIENT_CODE.getValue());
			sqlStatement.addSelectClause("IP",ApiAuthorizationInfoDTO.ATTRIBUTE.IP.getValue());
			sqlStatement.addSelectClause("STATUS",ApiAuthorizationInfoDTO.ATTRIBUTE.STATUS.getValue());
			sqlStatement.addSelectClause("RETRY",ApiAuthorizationInfoDTO.ATTRIBUTE.RETRY.getValue());
			sqlStatement.addSelectClause("AUTHORIZATION_TIME",ApiAuthorizationInfoDTO.ATTRIBUTE.AUTHORIZATION_TIME.getValue());
			sqlStatement.addSelectClause("TOKEN",ApiAuthorizationInfoDTO.ATTRIBUTE.TOKEN.getValue());
			sqlStatement.addSelectClause("HAS_TOKEN",ApiAuthorizationInfoDTO.ATTRIBUTE.HAS_TOKEN.getValue());
			sqlStatement.addFromExpression(schema + ".API_AUTHORIZATION_INFO");
			//sqlStatement.addWhereClause("STATUS = 'NORMAL' or STATUS = 'NEW'");
			if (StringUtils.hasText(ip)) {
				sqlStatement.addWhereClause("IP = :ip", ip);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(ApiAuthorizationInfoDTO.class);
			LOGGER.debug("getIpAndClientCodeList()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return result;
		} catch (Exception e) {
			LOGGER.error("getIpAndClientCodeList()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_USER)}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IApiAuthorizationInfoDAO#getauthorizationTimeByToken(java.lang.String)
	 */
	@Override
	public List<Parameter> getAuthorizationTimeByToken(String token)
			throws DataAccessException {
		List<Parameter> result = null;
		//得到schema
		String schema = this.getMySchema();
		SqlStatement sqlStatement = new SqlStatement();
		try {
			sqlStatement.addSelectClause("AUTHORIZATION_TIME as value");
			sqlStatement.addSelectClause("IP as name");
			sqlStatement.addFromExpression(schema + ".API_AUTHORIZATION_INFO");
			sqlStatement.addWhereClause("STATUS = 'NORMAL' or STATUS = 'NEW'");
			if (StringUtils.hasText(token)) {
				sqlStatement.addWhereClause("TOKEN like :token", token);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			LOGGER.debug("getAuthorizationTimeByToken()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return result;
		} catch (Exception e) {
			LOGGER.error("getAuthorizationTimeByToken()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_USER)}, e);
		}
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IApiAuthorizationInfoDAO#updateApiAuthorizationInfo(java.lang.String, java.security.Timestamp)
	 */
	@Override
	public void updateApiAuthorizationInfo(String ip, Timestamp logTime) throws DataAccessException {
		try {
			LOGGER.debug("updateApiAuthorizationInfo()", "ip=" + ip);
			LOGGER.debug("updateApiAuthorizationInfo()", "logTime=" + logTime);
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("UPDATE ").append(schema).append(".API_AUTHORIZATION_INFO SET AUTHORIZATION_TIME=:time WHERE IP=:ip");
			sqlQueryBean.setParameter("ip", ip);
			sqlQueryBean.setParameter("time", logTime);
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
			LOGGER.debug("updateApiAuthorizationInfo()", "updateByNativeSql Success ");
		} catch (Exception e) {
			LOGGER.error("updateApiAuthorizationInfo() error :", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DB_UNKNOWN_ERROR,e);
		}
	}
	
}
