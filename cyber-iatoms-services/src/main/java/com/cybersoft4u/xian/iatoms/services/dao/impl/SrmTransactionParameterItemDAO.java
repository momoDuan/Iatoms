package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import cafe.core.bean.CoreMessageCode;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmTransactionParameterItemDTO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmTransactionParameterItemDAO;

/**
 * Purpose:交易參數項目 
 * @author evanliu
 * @since  JDK 1.6
 * @date   2016年12月16日
 * @MaintenancePersonnel evanliu
 */
public class SrmTransactionParameterItemDAO extends GenericBaseDAO implements ISrmTransactionParameterItemDAO {
	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog logger = CafeLogFactory.getLog(SrmTransactionParameterItemDAO.class);
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmTransactionParameterItemDAO#listby(java.lang.String)
	 */
	@Override
	public List<SrmTransactionParameterItemDTO> listby(String versionDate) throws DataAccessException {
		List<SrmTransactionParameterItemDTO> result = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("item.PARAMTER_ITEM_ID",SrmTransactionParameterItemDTO.ATTRIBUTE.PARAMTER_ITEM_ID.getValue());
			sqlStatement.addSelectClause("item.PARAMTER_ITEM_CODE",SrmTransactionParameterItemDTO.ATTRIBUTE.PARAMTER_ITEM_CODE.getValue());
			sqlStatement.addSelectClause("item.PARAMTER_ITEM_NAME",SrmTransactionParameterItemDTO.ATTRIBUTE.PARAMTER_ITEM_NAME.getValue());
			sqlStatement.addSelectClause("item.PARAMTER_ITEM_TYPE",SrmTransactionParameterItemDTO.ATTRIBUTE.PARAMTER_ITEM_TYPE.getValue());
			sqlStatement.addSelectClause("item.PARAMTER_DATA_TYPE",SrmTransactionParameterItemDTO.ATTRIBUTE.PARAMTER_DATA_TYPE.getValue());
			sqlStatement.addSelectClause("item.PARAMTER_ITEM_LENGTH",SrmTransactionParameterItemDTO.ATTRIBUTE.PARAMTER_ITEM_LENGTH.getValue());
			sqlStatement.addSelectClause("item.ITEM_ORDER",SrmTransactionParameterItemDTO.ATTRIBUTE.ITEM_ORDER.getValue());
			sqlStatement.addFromExpression(schema + ".SRM_TRANSACTION_PARAMETER_ITEM item");
			if(StringUtils.hasText(versionDate)) {
				StringBuilder whereBuilder = new StringBuilder();
				whereBuilder.append("item.EFFECTIVE_DATE = (");
				whereBuilder.append("select max(item1.EFFECTIVE_DATE) from ").append(schema);
				whereBuilder.append(".SRM_TRANSACTION_PARAMETER_ITEM item1 where isnull(item1.APPROVED_FLAG,'N') = :approvedFlag ");
				whereBuilder.append("and item1.EFFECTIVE_DATE <= :effectiveDate )");
				sqlStatement.addWhereClause(whereBuilder.toString());
			}
			sqlStatement.addWhereClause("isnull(item.APPROVED_FLAG,'N') = :approvedFlag", IAtomsConstants.YES);
			sqlStatement.setOrderByExpression("item.ITEM_ORDER");
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmTransactionParameterItemDTO.class);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if (StringUtils.hasText(versionDate)) {
				sqlQueryBean.setParameter(SrmTransactionParameterItemDTO.ATTRIBUTE.APPROVED_FLAG.getValue(), IAtomsConstants.YES);
				sqlQueryBean.setParameter(SrmTransactionParameterItemDTO.ATTRIBUTE.EFFECTIVE_DATE.getValue(), versionDate);
			}
			logger.debug(this.getClass().getName() + ".listBy()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ".listby() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_SRM_TRANSACTION_PARAMETER_ITEM)} ,e);
		}
		return result;
	}

}
