package com.cybersoft4u.xian.iatoms.services.dao.impl;

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
import com.cybersoft4u.xian.iatoms.services.dao.IBaseParameterPostCodeDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BaseParameterPostCode;

/**
 * Purpose: 郵遞區號DAO
 * @author neiljing
 * @since  JDK 1.7
 * @date   2018/5/15
 * @MaintenancePersonnel neiljing
 */
public class BaseParameterPostCodeDAO extends GenericBaseDAO<BaseParameterPostCode> implements IBaseParameterPostCodeDAO {

	/**
	 * 日誌記錄物件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(BaseParameterPostCodeDAO.class);
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IBaseParameterPostCodeDAO#getPostCodeList(java.lang.String, java.lang.String, java.lang.String,java.lang.String)
	 */
	@Override
	public List<Parameter> getPostCodeList(String cityId, String postName, String postCode) throws DataAccessException{
		try {
			LOGGER.debug("getPostCodeList()", "parameters:cityId=" + cityId);
			LOGGER.debug("getPostCodeList()", "parameters:postName=" + postName);
			LOGGER.debug("getPostCodeList()", "parameters:postCode=" + postCode);
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("POST_CODE_ID", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("POST_CODE+'('+POST_NAME+')'", Parameter.FIELD_NAME);
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".BASE_PARAMETER_POST_CODE");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(cityId)) {
				sqlStatement.addWhereClause("CITY_ID = :cityId", cityId);
			}
			if (StringUtils.hasText(postName)) {
				sqlStatement.addWhereClause("POST_NAME = :postName", postName);
			}
			if (StringUtils.hasText(postCode)) {
				sqlStatement.addWhereClause("POST_CODE = :postCode", postCode);
			}
			sqlStatement.setOrderByExpression("POST_CODE");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			List<Parameter> postCodeList = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			LOGGER.debug("getPostCodeList()", "sql:", sqlQueryBean.toString());
			return postCodeList;
		} catch (Exception e) {
			LOGGER.error(":getPostCodeList() is error ", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}	
	}
}
