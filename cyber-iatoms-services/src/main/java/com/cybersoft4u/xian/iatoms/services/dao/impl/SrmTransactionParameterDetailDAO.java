package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.springframework.util.StringUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmTransactionParameterDetailDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmTransactionParameterItemDTO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmTransactionParameterDetailDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmTransactionParameterDetail;
/**
 * Purpose: 交易參數項目明細
 * @author evanliu
 * @since  JDK 1.6
 * @date   2016年12月15日
 * @MaintenancePersonnel evanliu
 */
public class SrmTransactionParameterDetailDAO extends GenericBaseDAO<SrmTransactionParameterDetail> implements ISrmTransactionParameterDetailDAO{

	/**
	 * 日誌記錄物件
	 */
	private static final CafeLog logger = (CafeLog) CafeLogFactory.getLog(SrmTransactionParameterDetailDAO.class);

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmTransactionParameterDetailDAO#listby()
	 */
	@Override
	public List<SrmTransactionParameterDetailDTO> listby(String transactionType, String paramterItemId, String isEdit, String versionDate)
			throws DataAccessException {
		List<SrmTransactionParameterDetailDTO> result = null;
		try {
 			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("detail.TRANSACTION_TYPE",SrmTransactionParameterDetailDTO.ATTRIBUTE.TRANSACTION_TYPE.getValue());
			sqlStatement.addSelectClause("detail.PARAMTER_ITEM_ID",SrmTransactionParameterDetailDTO.ATTRIBUTE.PARAMTER_ITEM_ID.getValue());
			sqlStatement.addSelectClause("item.PARAMTER_ITEM_CODE",SrmTransactionParameterDetailDTO.ATTRIBUTE.PARAMTER_ITEM_CODE.getValue());
			sqlStatement.addSelectClause("detail.IS_EDIT",SrmTransactionParameterDetailDTO.ATTRIBUTE.IS_EDIT.getValue());
			sqlStatement.addSelectClause("detail.DEFAULT_VALUE",SrmTransactionParameterDetailDTO.ATTRIBUTE.DEFAULT_VALUE.getValue());			
			String schema = this.getMySchema();
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".SRM_TRANSACTION_PARAMETER_DETAIL detail,");
			fromBuffer.append(schema).append(".SRM_TRANSACTION_PARAMETER_ITEM item");
			sqlStatement.addFromExpression(fromBuffer.toString());
			sqlStatement.addWhereClause("detail.PARAMTER_ITEM_ID = item.PARAMTER_ITEM_ID");
			if (StringUtils.hasText(transactionType)) {
				sqlStatement.addWhereClause("detail.TRANSACTION_TYPE=:transactionType", transactionType);
			}
			if (StringUtils.hasText(paramterItemId)) {
				sqlStatement.addWhereClause("detail.PARAMTER_ITEM_ID=:paramterItemId", paramterItemId);
			}
			if (StringUtils.hasText(isEdit)) {
				sqlStatement.addWhereClause("detail.IS_EDIT=:isEdit", isEdit);
			}
			if (StringUtils.hasText(versionDate)) {
				StringBuffer whereBuffer = new StringBuffer();
				whereBuffer.append("item.EFFECTIVE_DATE = (");
				whereBuffer.append("select max(item1.EFFECTIVE_DATE) from ").append(schema);
				whereBuffer.append(".SRM_TRANSACTION_PARAMETER_ITEM item1 where ");
				whereBuffer.append("isnull(item1.APPROVED_FLAG,'N') = :approvedFlag ");
				whereBuffer.append("and item1.EFFECTIVE_DATE <= :effectiveDate)");
				sqlStatement.addWhereClause(whereBuffer.toString());
			}
			sqlStatement.addWhereClause(" isnull(item.APPROVED_FLAG,'N') = :approvedFlag", IAtomsConstants.YES);
			sqlStatement.setOrderByExpression("detail.TRANSACTION_TYPE,detail.PARAMTER_ITEM_ID");
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmTransactionParameterDetailDTO.class);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if (StringUtils.hasText(versionDate)) {
				sqlQueryBean.setParameter(SrmTransactionParameterItemDTO.ATTRIBUTE.APPROVED_FLAG.getValue(), IAtomsConstants.YES);
				sqlQueryBean.setParameter(SrmTransactionParameterItemDTO.ATTRIBUTE.EFFECTIVE_DATE.getValue(), versionDate);
			}
			logger.debug(this.getClass().getName() + ".listBy(String transactionType, String paramterItemId,String isEdit)", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ".listby(String transactionType, String paramterItemId,String isEdit) is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_SRM_TRANSACTION_PARAMETER_DETAIL)} ,e);
		}
		return result;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmTransactionParameterDetailDAO#getSrmTransactionParameterDetailDTOList()
	 */
	@Override
	public List<SrmTransactionParameterDetailDTO> getSrmTransactionParameterDetailDTOList()
			throws DataAccessException {
		List<SrmTransactionParameterDetailDTO> result = null;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause(" bpid.ITEM_NAME ",SrmTransactionParameterDetailDTO.ATTRIBUTE.TRANSACTION_TYPE.getValue());
			sqlStatement.addSelectClause(" stpi.PARAMTER_ITEM_CODE ",SrmTransactionParameterDetailDTO.ATTRIBUTE.PARAMTER_ITEM_CODE.getValue());
			sqlStatement.addSelectClause(" CASE stpd.IS_EDIT WHEN 'Y' THEN 1 ELSE 0 END ",SrmTransactionParameterDetailDTO.ATTRIBUTE.IS_EDIT.getValue());
			String schema = this.getMySchema();
			
			StringBuffer buffer = new StringBuffer();
			buffer.append(" ( SELECT DISTINCT ITEM_NAME , ITEM_VALUE from ");
			buffer.append(schema).append(".BASE_PARAMETER_ITEM_DEF where ");
			buffer.append(" BPTD_CODE = 'TRANSACTION_CATEGORY' ) ");
			buffer.append(" bpid LEFT JOIN ").append(schema).append(" .SRM_TRANSACTION_PARAMETER_DETAIL stpd ON stpd.TRANSACTION_TYPE = bpid.ITEM_VALUE  ");
			buffer.append(" LEFT JOIN ").append(schema).append(" .SRM_TRANSACTION_PARAMETER_ITEM stpi ON stpi.PARAMTER_ITEM_ID = stpd.PARAMTER_ITEM_ID ");
			sqlStatement.addFromExpression(buffer.toString());
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmTransactionParameterDetailDTO.class);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			logger.debug(this.getClass().getName() + ".getSrmTransactionParameterDetailDTOList()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ".listby(String transactionType, String paramterItemId,String isEdit) is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_SRM_TRANSACTION_PARAMETER_DETAIL)} ,e);
		}
		return result;
	}
}
