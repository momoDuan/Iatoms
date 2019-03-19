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

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAssetFunctionDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTransactionParameterDTO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetFunctionDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseAssetFunction;

/**
 * Purpose: SRM_案件處理中設備支援功能檔 DAO 實現類
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2017/2/24
 * @MaintenancePersonnel CrissZhang
 */
public class SrmCaseAssetFunctionDAO extends GenericBaseDAO<SrmCaseAssetFunction> implements ISrmCaseAssetFunctionDAO{

	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(SrmCaseAssetFunctionDAO.class);
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetFunctionDAO#listByCaseId()
	 */
	@Override
	public List<SrmCaseAssetFunctionDTO> listByCaseId(String caseId) throws DataAccessException {
		List<SrmCaseAssetFunctionDTO> srmCaseAssetFunctionDTOs = null;
		try{
			LOGGER.debug(this.getClass().getName() + ".listByCaseId()", "parameters:caseId=" + caseId);
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("assetFun.ID", SrmCaseAssetFunctionDTO.ATTRIBUTE.ID.getValue());
			sqlStatement.addSelectClause("assetFun.CASE_ID", SrmCaseAssetFunctionDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("assetFun.FUNCTION_CATEGORY", SrmCaseAssetFunctionDTO.ATTRIBUTE.FUNCTION_CATEGORY.getValue());
			sqlStatement.addSelectClause("assetFun.FUNCTION_ID", SrmCaseAssetFunctionDTO.ATTRIBUTE.FUNCTION_ID.getValue());
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(".SRM_CASE_ASSET_FUNCTION assetFun ");
			sqlStatement.addFromExpression(buffer.toString());
			if (StringUtils.hasText(caseId)) {
				sqlStatement.addWhereClause("assetFun.CASE_ID = :caseId", caseId);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseTransactionParameterDTO.class);
			LOGGER.debug(this.getClass().getName() + ".listByCaseId()", "sql:" + sqlQueryBean.toString());
			srmCaseAssetFunctionDTOs = super.getDaoSupport().findByNativeSql(sqlQueryBean,aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+":listByCaseId() is error." + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return srmCaseAssetFunctionDTOs;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetFunctionDAO#deleteAll(java.lang.String)
	 */
	@Override
	public void deleteAll(String caseId) throws DataAccessException {
		try {
			LOGGER.debug("deleteAll()", "parameters:caseId=" + caseId);
			if (StringUtils.hasText(caseId)) {
				SqlQueryBean sqlQueryBean = new SqlQueryBean("delete ");
				String schema = this.getMySchema();
				sqlQueryBean.append(schema).append(".SRM_CASE_ASSET_FUNCTION where case_id=:caseId");
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

}
