package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmQueryTemplateDTO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmQueryTemplateDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmQueryTemplate;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

/**
 * Purpose: 用戶欄位模板維護檔  DAO 實現類
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2017/2/24
 * @MaintenancePersonnel CrissZhang
 */
public class SrmQueryTemplateDAO extends GenericBaseDAO<SrmQueryTemplate> implements ISrmQueryTemplateDAO{
	
	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(SrmQueryTemplateDAO.class);
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmQueryTemplateDAO#getUserColumnTemplateList(java.lang.String)
	 */
	@Override
	public List<Parameter> getUserColumnTemplateList(String userId, boolean isCurrentTemplate, boolean isSettingOther) throws DataAccessException {
		LOGGER.debug("getUserColumnTemplateList()", "parameters:userId=" + userId);
		LOGGER.debug("getUserColumnTemplateList()", "parameters:isCurrentTemplate=" + isCurrentTemplate);
		LOGGER.debug("getUserColumnTemplateList()", "parameters:isSettingOther=" + isSettingOther);
		List<Parameter> result = null;
		//得到schema
		String schema = this.getMySchema();
		SqlStatement sqlStatement = new SqlStatement();
		try {
			sqlStatement.addSelectClause("TEMPLATE_ID as value");
			if(isCurrentTemplate){
				sqlStatement.addSelectClause("FIELD_CONTENT as name");
			} else {
				sqlStatement.addSelectClause("TEMPLATE_NAME as name");
			}
			sqlStatement.addFromExpression(schema + ".SRM_QUERY_TEMPLATE");
			
			if(StringUtils.hasText(userId)){
				if(isCurrentTemplate){
					if(isSettingOther){
						sqlStatement.addWhereClause("isnull(USER_ID, '') =:userId and IS_DEFAULT = 'Y'");
					} else {
						sqlStatement.addWhereClause("TEMPLATE_ID = '100000000-0001'");
					}
				} else {
					sqlStatement.addWhereClause("isnull(USER_ID, '') =:userId or TEMPLATE_ID = '100000000-0001'");
				}
			} else {
				sqlStatement.addWhereClause("TEMPLATE_ID = '100000000-0001'");
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if(StringUtils.hasText(userId)){
				if(isCurrentTemplate && !isSettingOther){
					
				} else {
					sqlQueryBean.setParameter("userId", userId);
				}
			}
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			LOGGER.debug("getUserColumnTemplateList()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return result;
		} catch (Exception e) {
			LOGGER.error("getUserColumnTemplateList()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmQueryTemplateDAO#updateOtherTemplate(java.lang.String, java.lang.String)
	 */
	@Override
	public void updateOtherTemplate(String userId, String templateId) throws DataAccessException {
		LOGGER.debug("updateOtherTemplate()", "parameters:userId=" + userId);
		LOGGER.debug("updateOtherTemplate()", "parameters:templateId=" + templateId);
		//得到schema
		String schema = this.getMySchema();
		try {
			SqlQueryBean sqlQueryBean = new SqlQueryBean("update ");
			sqlQueryBean.append(schema).append(".SRM_QUERY_TEMPLATE set IS_DEFAULT= 'N'");
			sqlQueryBean.append(" WHERE ISNULL(USER_ID, '') = :userId AND TEMPLATE_ID <> :templateId");
			sqlQueryBean.setParameter(SrmQueryTemplateDTO.ATTRIBUTE.USER_ID.getValue(), userId);
			sqlQueryBean.setParameter(SrmQueryTemplateDTO.ATTRIBUTE.TEMPLATE_ID.getValue(), templateId);
			LOGGER.debug("updateOtherTemplate()", "sql:" + sqlQueryBean.toString());
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("updateOtherTemplate()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmQueryTemplateDAO#isSettingOther(java.lang.String)
	 */
	@Override
	public boolean isSettingOther(String userId) throws DataAccessException {
		try {
			LOGGER.debug("isSettingOther()", "parameters:userId=" + userId);
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			sqlStatement.addFromExpression(schema + ".SRM_QUERY_TEMPLATE");
			sqlStatement.addWhereClause("isnull(USER_ID, '') =:userId and IS_DEFAULT = 'Y'");
			
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if (StringUtils.hasText(userId)) {
				sqlQueryBean.setParameter("userId", userId);
			} 
			LOGGER.debug("isSettingOther()", "sql:" + sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				if(result.get(0).intValue() != 0){
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			LOGGER.error("isSettingOther()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmQueryTemplateDAO#isRepeat(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isRepeat(String templateId, String userId, String templateName) throws DataAccessException {
		LOGGER.debug("isRepeat()", "parameters:templateId = " + templateId);
		LOGGER.debug("isRepeat()", "parameters:userId = " + userId);
		LOGGER.debug("isRepeat()", "parameters:templateName = " + templateName);
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			sqlStatement.addFromExpression(schema + ".SRM_QUERY_TEMPLATE");
			if (StringUtils.hasText(templateId)) {
				sqlStatement.addWhereClause("TEMPLATE_ID <> :templateId", templateId);
			}
			if (StringUtils.hasText(userId)) {
				sqlStatement.addWhereClause("USER_ID = :userId", userId);
			}
			if (StringUtils.hasText(templateName)) {
				sqlStatement.addWhereClause("TEMPLATE_NAME = :templateName", templateName);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug("isRepeat()", "sql:" + sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				if (result.get(0).intValue() > 0) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			LOGGER.error("isRepeat()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmQueryTemplateDAO#isTemplateOverLimit(java.lang.String)
	 */
	@Override
	public boolean isTemplateOverLimit(String userId) throws DataAccessException {
		try {
			LOGGER.debug("isTemplateOverLimit()", "parameters:userId=" + userId);
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			sqlStatement.addFromExpression(schema + ".SRM_QUERY_TEMPLATE");
			sqlStatement.addWhereClause("isnull(USER_ID, '') =:userId");
			
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if (StringUtils.hasText(userId)) {
				sqlQueryBean.setParameter("userId", userId);
			} 
			LOGGER.debug("isTemplateOverLimit()", "sql:" + sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				//將模板數量從3改為20 2018/02/02
				if(result.get(0).intValue() >= 20){
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			LOGGER.error("isTemplateOverLimit()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

}
