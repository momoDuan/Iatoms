package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import cafe.core.bean.CoreMessageCode;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentTranscationDTO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentTranscationDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmPaymentTranscation;
/**
 * Purpose: 求償處理記錄檔DAO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/2/14
 * @MaintenancePersonnel CarrieDuan
 */
public class SrmPaymentTranscationDAO extends GenericBaseDAO<SrmPaymentTranscation> implements ISrmPaymentTranscationDAO {
	/**
	 * 系統日志記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(SrmPaymentTranscationDAO.class);

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentTranscationDAO#listBy(java.lang.String)
	 */
	public List<SrmPaymentTranscationDTO> listBy(String paymentId) throws DataAccessException {
		List<SrmPaymentTranscationDTO> srmPaymentTranscationDTOs = null;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("trans.STATUS", SrmPaymentTranscationDTO.ATTRIBUTE.STATUS.getValue());
			sqlStatement.addSelectClause("itemAction.ITEM_NAME", SrmPaymentTranscationDTO.ATTRIBUTE.ACTION_NAME.getValue());
			sqlStatement.addSelectClause("trans.ACTION", SrmPaymentTranscationDTO.ATTRIBUTE.ACTION.getValue());
			sqlStatement.addSelectClause("trans.PAYMENT_CONTENT", SrmPaymentTranscationDTO.ATTRIBUTE.PAYMENT_CONTENT.getValue());
			sqlStatement.addSelectClause("trans.UPDATED_BY_NAME", SrmPaymentTranscationDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("trans.UPDATED_DATE", SrmPaymentTranscationDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".SRM_PAYMENT_TRANSCATION trans ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF itemAction on itemAction.BPTD_CODE = :action and itemAction.ITEM_VALUE = trans.ACTION ");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(paymentId)) {
				sqlStatement.addWhereClause("trans.PAYMENT_ID = :paymentId", paymentId);
			}
			sqlStatement.setOrderByExpression("trans.UPDATED_DATE desc");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmPaymentTranscationDTO.class);
			sqlQueryBean.setParameter(SrmPaymentTranscationDTO.ATTRIBUTE.ACTION.getValue(), IATOMS_PARAM_TYPE.PAY_ACTION.getCode());
			LOGGER.debug(".getCount() --> sql: ", sqlQueryBean.toString());
			srmPaymentTranscationDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(".getCount() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return srmPaymentTranscationDTOs;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentTranscationDAO#deleteByPaymentId(java.lang.String)
	 */
	public void deleteByPaymentId(String paymentId) throws DataAccessException {
		try {
			if (StringUtils.hasText(paymentId)) {
				SqlQueryBean sqlQueryBean = new SqlQueryBean("delete ");
				String schma = this.getMySchema();
				sqlQueryBean.append(schma).append(".SRM_PAYMENT_TRANSCATION where PAYMENT_ID = :paymentId");
				sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_ID.getValue(), paymentId);
				LOGGER.debug(".deleteByPaymentId()", "sql:", sqlQueryBean.toString());
				this.getDaoSupport().updateByNativeSql(sqlQueryBean);
				this.getDaoSupport().flush();
			}
		} catch (Exception e) {
			LOGGER.error(".deleteByPaymentId() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
}
