package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTransactionParameterDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmTransactionParameterItemDTO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseTransactionParameterDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseTransactionParameter;
/**
 * Purpose:案件交易參數 
 * @author evanliu
 * @since  JDK 1.6
 * @date   2016年12月16日
 * @MaintenancePersonnel evanliu
 */
public class SrmCaseTransactionParameterDAO extends GenericBaseDAO<SrmCaseTransactionParameter> implements ISrmCaseTransactionParameterDAO {

	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(SrmCaseTransactionParameterDAO.class);
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseTransactionParameterDAO#listByCaseId(java.lang.String)
	 */
	@Override
	public List<SrmCaseTransactionParameterDTO> listByCaseId(String caseId, String isHistory)throws DataAccessException {
		List<SrmCaseTransactionParameterDTO> caseTransactionParameterDTOs = null;
		try{
			LOGGER.debug(this.getClass().getName() + ".listByCaseId()", "parameters:caseId=" + caseId);
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("DISTINCT transParameter.PARAMTER_VALUE_ID", SrmCaseTransactionParameterDTO.ATTRIBUTE.PARAMTER_VALUE_ID.getValue());
			sqlStatement.addSelectClause("transParameter.TRANSACTION_TYPE", SrmCaseTransactionParameterDTO.ATTRIBUTE.TRANSACTION_TYPE.getValue());
			sqlStatement.addSelectClause("transCategory.ITEM_NAME", SrmCaseTransactionParameterDTO.ATTRIBUTE.TRANSACTION_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("transParameter.MERCHANT_CODE", SrmCaseTransactionParameterDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("transParameter.MERCHANT_CODE_OTHER", SrmCaseTransactionParameterDTO.ATTRIBUTE.MERCHANT_CODE_OTHER.getValue());
			sqlStatement.addSelectClause("transParameter.TID", SrmCaseTransactionParameterDTO.ATTRIBUTE.TID.getValue());
			sqlStatement.addSelectClause("transParameter.CASE_ID", SrmCaseTransactionParameterDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("transParameter.ITEM_VALUE", SrmCaseTransactionParameterDTO.ATTRIBUTE.ITEM_VALUE.getValue());
			StringBuffer buffer = new StringBuffer();
			buffer.append("convert(int, ");
			buffer.append("SUBSTRING(transParameter.PARAMTER_VALUE_ID, CHARINDEX('_' ,transParameter.PARAMTER_VALUE_ID) +1 , LEN(transParameter.PARAMTER_VALUE_ID) + 1)) ");
			sqlStatement.addSelectClause(buffer.toString(), "rowIndex");
			buffer.delete(0, buffer.length());
			// 判斷標志位
			if(StringUtils.hasText(isHistory) && IAtomsConstants.YES.equals(isHistory)){
				buffer.append(schema).append(".SRM_HISTORY_CASE_TRANSACTION_PARAMETER transParameter ");
			} else {
				buffer.append(schema).append(".SRM_CASE_TRANSACTION_PARAMETER transParameter ");
			}
			buffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF transCategory on transCategory.BPTD_CODE = :transactionCategory and transCategory.ITEM_VALUE = transParameter.TRANSACTION_TYPE");
			sqlStatement.addFromExpression(buffer.toString());
			if (StringUtils.hasText(caseId)) {
				sqlStatement.addWhereClause("transParameter.CASE_ID = :caseId", caseId);
			}
			StringBuffer whereBuffer = new StringBuffer();
			whereBuffer.append("transCategory.EFFECTIVE_DATE =(");
			whereBuffer.append("select max(transCategory1.EFFECTIVE_DATE) from ").append(schema);
			whereBuffer.append(".SRM_TRANSACTION_PARAMETER_ITEM transCategory1 where ");
			whereBuffer.append("isnull(transCategory1.APPROVED_FLAG,'N') = :approvedFlag ");
			whereBuffer.append("and transCategory1.EFFECTIVE_DATE <= :effectiveDate)");
			sqlStatement.addWhereClause(whereBuffer.toString());
			sqlStatement.setOrderByExpression("rowIndex");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter(SrmTransactionParameterItemDTO.ATTRIBUTE.APPROVED_FLAG.getValue(), IAtomsConstants.YES);
			sqlQueryBean.setParameter(SrmTransactionParameterItemDTO.ATTRIBUTE.EFFECTIVE_DATE.getValue(), DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMMDD_DASH));
			sqlQueryBean.setParameter("transactionCategory",  IATOMS_PARAM_TYPE.TRANSACTION_CATEGORY.getCode());
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseTransactionParameterDTO.class);
			LOGGER.debug(this.getClass().getName() + ".listByCaseId()", "sql:" + sqlQueryBean.toString());
			caseTransactionParameterDTOs = super.getDaoSupport().findByNativeSql(sqlQueryBean,aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+":listByCaseId() is error." + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return caseTransactionParameterDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseTransactionParameterDAO#listByCaseIds(java.lang.String)
	 */
	@Override
	public List<SrmCaseTransactionParameterDTO> listByCaseIds(String caseIds)throws DataAccessException {
		List<SrmCaseTransactionParameterDTO> caseTransactionParameterDTOs = null;
		try{
			LOGGER.debug(this.getClass().getName() + ".listByCaseIds()", "caseIds=" + caseIds);
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("DISTINCT transParameter.PARAMTER_VALUE_ID", SrmCaseTransactionParameterDTO.ATTRIBUTE.PARAMTER_VALUE_ID.getValue());
			sqlStatement.addSelectClause("transParameter.TRANSACTION_TYPE", SrmCaseTransactionParameterDTO.ATTRIBUTE.TRANSACTION_TYPE.getValue());
			sqlStatement.addSelectClause("transCategory.ITEM_NAME", SrmCaseTransactionParameterDTO.ATTRIBUTE.TRANSACTION_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("transParameter.MERCHANT_CODE", SrmCaseTransactionParameterDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("transParameter.MERCHANT_CODE_OTHER", SrmCaseTransactionParameterDTO.ATTRIBUTE.MERCHANT_CODE_OTHER.getValue());
			sqlStatement.addSelectClause("transParameter.TID", SrmCaseTransactionParameterDTO.ATTRIBUTE.TID.getValue());
			sqlStatement.addSelectClause("transParameter.CASE_ID", SrmCaseTransactionParameterDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("transParameter.ITEM_VALUE", SrmCaseTransactionParameterDTO.ATTRIBUTE.ITEM_VALUE.getValue());
			StringBuffer buffer = new StringBuffer();
			
			buffer.append(schema).append(".SRM_CASE_TRANSACTION_PARAMETER transParameter ");
			buffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF transCategory on transCategory.BPTD_CODE = :transactionCategory and transCategory.ITEM_VALUE = transParameter.TRANSACTION_TYPE");
			sqlStatement.addFromExpression(buffer.toString());
			if (StringUtils.hasText(caseIds)) {
				sqlStatement.addWhereClause("transParameter.CASE_ID in(:caseIds)");
			}
			
			StringBuffer whereBuffer = new StringBuffer();
			whereBuffer.append("transCategory.EFFECTIVE_DATE =(");
			whereBuffer.append("select max(transCategory1.EFFECTIVE_DATE) from ").append(schema);
			whereBuffer.append(".SRM_TRANSACTION_PARAMETER_ITEM transCategory1 where ");
			whereBuffer.append("isnull(transCategory1.APPROVED_FLAG,'N') = :approvedFlag ");
			whereBuffer.append("and transCategory1.EFFECTIVE_DATE <= :effectiveDate)");
			sqlStatement.addWhereClause(whereBuffer.toString());
			sqlStatement.setOrderByExpression("transParameter.PARAMTER_VALUE_ID");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("caseIds", StringUtils.toList(caseIds, IAtomsConstants.MARK_SEPARATOR));
			sqlQueryBean.setParameter(SrmTransactionParameterItemDTO.ATTRIBUTE.APPROVED_FLAG.getValue(), IAtomsConstants.YES);
			sqlQueryBean.setParameter(SrmTransactionParameterItemDTO.ATTRIBUTE.EFFECTIVE_DATE.getValue(), DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMMDD_DASH));
			sqlQueryBean.setParameter("transactionCategory",  IATOMS_PARAM_TYPE.TRANSACTION_CATEGORY.getCode());
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseTransactionParameterDTO.class);
			LOGGER.debug(this.getClass().getName() + ".listByCaseIds()", "sql:" + sqlQueryBean.toString());
			caseTransactionParameterDTOs = super.getDaoSupport().findByNativeSql(sqlQueryBean,aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+":listByCaseIds() is error." + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return caseTransactionParameterDTOs;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseTransactionParameterDAO#deleteAll(java.lang.String)
	 */
	@Override
	public void deleteAll(String caseId) throws DataAccessException {
		try {
			LOGGER.debug("deleteAll()", "parameters:caseId=" + caseId);
			if (StringUtils.hasText(caseId)) {
				SqlQueryBean sqlQueryBean = new SqlQueryBean("delete ");
				String schema = this.getMySchema();
				sqlQueryBean.append(schema).append(".SRM_CASE_TRANSACTION_PARAMETER where case_id=:caseId");
				sqlQueryBean.setParameter(SrmCaseTransactionParameterDTO.ATTRIBUTE.CASE_ID.getValue(), caseId);
				LOGGER.debug("deleteAll()", "sql:" + sqlQueryBean.toString());
				this.getDaoSupport().updateByNativeSql(sqlQueryBean);
				this.getDaoSupport().flush();
			}
		} catch (Exception e) {
			LOGGER.error("deleteAll()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseTransactionParameterDAO#listByCaseIdHaveTransParam(java.util.List)
	 */
	@Override
	public List<SrmCaseTransactionParameterDTO> listByCaseIdHaveTransParam(List<String> caseIds) throws DataAccessException {
		List<SrmCaseTransactionParameterDTO> caseTransactionParameterDTOs = null;
		try{
			LOGGER.debug(this.getClass().getName() + ".listByCaseIdHaveTransParam()", "parameters:caseIds=" + caseIds);
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("DISTINCT trans.CASE_ID", SrmCaseTransactionParameterDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addFromExpression(schema + ".SRM_CASE_TRANSACTION_PARAMETER trans");
			if (!CollectionUtils.isEmpty(caseIds)) {
				sqlStatement.addWhereClause("trans.CASE_ID in ( :caseIds )");
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if (!CollectionUtils.isEmpty(caseIds)) {
				sqlQueryBean.setParameter("caseIds", caseIds);
			}
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseTransactionParameterDTO.class);
			LOGGER.debug(this.getClass().getName() + ".listByCaseIdHaveTransParam()", "sql:" + sqlQueryBean.toString());
			caseTransactionParameterDTOs = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+":listByCaseIdHaveTransParam() is error." + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return caseTransactionParameterDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseTransactionParameterDAO#getTransactionParameterByCaseIds(java.lang.String)
	 */
	@Override
	public List<SrmCaseTransactionParameterDTO> getTransactionParameterByCaseIds(String caseIds)throws DataAccessException {
		List<SrmCaseTransactionParameterDTO> caseTransactionParameterDTOs = null;
		try{
			LOGGER.debug(this.getClass().getName() + ".getTransactionParameterByCaseIds()", "caseIds=" + caseIds);
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("DISTINCT transParameter.PARAMTER_VALUE_ID", SrmCaseTransactionParameterDTO.ATTRIBUTE.PARAMTER_VALUE_ID.getValue());
			sqlStatement.addSelectClause("transParameter.TRANSACTION_TYPE", SrmCaseTransactionParameterDTO.ATTRIBUTE.TRANSACTION_TYPE.getValue());
			sqlStatement.addSelectClause("transCategory.ITEM_NAME", SrmCaseTransactionParameterDTO.ATTRIBUTE.TRANSACTION_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("transParameter.MERCHANT_CODE", SrmCaseTransactionParameterDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("transParameter.MERCHANT_CODE_OTHER", SrmCaseTransactionParameterDTO.ATTRIBUTE.MERCHANT_CODE_OTHER.getValue());
			sqlStatement.addSelectClause("transParameter.TID", SrmCaseTransactionParameterDTO.ATTRIBUTE.TID.getValue());
			sqlStatement.addSelectClause("transParameter.CASE_ID", SrmCaseTransactionParameterDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("transParameter.ITEM_VALUE", SrmCaseTransactionParameterDTO.ATTRIBUTE.ITEM_VALUE.getValue());
			StringBuffer buffer = new StringBuffer();
			
			buffer.append(schema).append(".SRM_CASE_TRANSACTION_PARAMETER transParameter ");
			buffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF transCategory on transCategory.BPTD_CODE = :transactionCategory and transCategory.ITEM_VALUE = transParameter.TRANSACTION_TYPE");
			sqlStatement.addFromExpression(buffer.toString());
			if (StringUtils.hasText(caseIds)) {
				sqlStatement.addWhereClause("transParameter.CASE_ID in(:caseIds)");
			}
			
			sqlStatement.setOrderByExpression("transParameter.PARAMTER_VALUE_ID");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("caseIds", StringUtils.toList(caseIds, IAtomsConstants.MARK_SEPARATOR));
			sqlQueryBean.setParameter("transactionCategory",  "TRANSACTION_CATEGORY_ATOMS");
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseTransactionParameterDTO.class);
			LOGGER.debug(this.getClass().getName() + ".getTransactionParameterByCaseIds()", "sql:" + sqlQueryBean.toString());
			caseTransactionParameterDTOs = super.getDaoSupport().findByNativeSql(sqlQueryBean,aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+":getTransactionParameterByCaseIds() is error." + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return caseTransactionParameterDTOs;
	}

}
