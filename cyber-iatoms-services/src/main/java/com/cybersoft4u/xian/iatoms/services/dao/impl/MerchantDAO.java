package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Parameter;
import cafe.core.config.GenericConfigManager;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.MerchantDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IMerchantDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimMerchant;

/**
 * Purpose: 客戶特店維護DAO層 
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2016/6/16
 * @MaintenancePersonnel DavidZheng
 */
public class MerchantDAO extends GenericBaseDAO<BimMerchant> implements IMerchantDAO{
	
	/**
	 * 日志記錄掛件
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.DAO, MerchantDAO.class);	
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IMerchantDAO#listby(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MerchantDTO> listBy(String queryCompanyId, String queryName, String queryMerchantCode, Integer pageSize,
			Integer pageIndex, String sort, String orderby) throws DataAccessException {
			List<MerchantDTO> relults = null;
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			try {
				LOGGER.debug(".listBy()", "parameters:queryCompanyId=" + queryCompanyId);
				LOGGER.debug(".listBy()", "parameters:queryName=" + queryName);
				LOGGER.debug(".listBy()", "parameters:queryMerchantCode=" + queryMerchantCode);
				/*LOGGER.debug(this.getClass().getName() + ".listBy()", "parameters:queryStagesMerchantCode=" + queryStagesMerchantCode);*/
				LOGGER.debug(".listBy()", "parameters:pageSize=" + pageSize);
				LOGGER.debug(".listBy()", "parameters:pageIndex=" + pageIndex);
				LOGGER.debug(".listBy()", "parameters:sort=" + sort);
				LOGGER.debug(".listBy()", "parameters:orderby=" + orderby);
				sqlStatement.addSelectClause("bm.MERCHANT_ID", MerchantDTO.ATTRIBUTE.MERCHANT_ID.getValue());
				sqlStatement.addSelectClause("bm.COMPANY_ID", MerchantDTO.ATTRIBUTE.COMPANY_ID.getValue());
				sqlStatement.addSelectClause("bc.SHORT_NAME", MerchantDTO.ATTRIBUTE.SHORT_NAME.getValue());
				sqlStatement.addSelectClause("bm.MERCHANT_CODE", MerchantDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
				//Task #3249
				sqlStatement.addSelectClause("bm.UNITY_NUMBER", MerchantDTO.ATTRIBUTE.UNITY_NUMBER.getValue());
				sqlStatement.addSelectClause("bm.NAME", MerchantDTO.ATTRIBUTE.NAME.getValue());
				sqlStatement.addSelectClause("bm.REMARK", MerchantDTO.ATTRIBUTE.REMARK.getValue());
				StringBuffer buffer = new StringBuffer();
				buffer.append(schema).append(".BIM_MERCHANT bm inner join ").append(schema).append(".BIM_COMPANY bc on bm.COMPANY_ID = bc.COMPANY_ID ");
				//sqlStatement.addFromExpression(schema + ".BIM_MERCHANT bm inner join " + schema + ".BIM_COMPANY bc on bm.COMPANY_ID = bc.COMPANY_ID");
				sqlStatement.addFromExpression(buffer.toString());
				//查詢條件
				if (StringUtils.hasText(queryCompanyId)) {
					sqlStatement.addWhereClause("bm.COMPANY_ID = :companyId", queryCompanyId);
				}
				if (StringUtils.hasText(queryMerchantCode)) {
					sqlStatement.addWhereClause("bm.MERCHANT_CODE like :merchantCode", queryMerchantCode + IAtomsConstants.MARK_PERCENT);
				}
				if (StringUtils.hasText(queryName)) {
					sqlStatement.addWhereClause("bm.NAME like :name", queryName + IAtomsConstants.MARK_PERCENT);
				}
				
				/*if (StringUtils.hasText(queryStagesMerchantCode)) {
					sqlStatement.addWhereClause("bm.STAGES_MERCHANT_CODE like :stagesMerchantCode", queryStagesMerchantCode + IAtomsConstants.MARK_PERCENT);
				}*/
				//設置排序表達式
				if(StringUtils.hasText(sort) && StringUtils.hasText(orderby)){
					StringBuffer stringBuffer = new StringBuffer();
					stringBuffer.append(sort).append(IAtomsConstants.MARK_SPACE).append(orderby);
					sqlStatement.setOrderByExpression(stringBuffer.toString());
				}else {
					sqlStatement.setOrderByExpression("bc.SHORT_NAME asc, bm.MERCHANT_CODE ASC");
				}
				sqlStatement.addWhereClause("bm.DELETED =:deleted", IAtomsConstants.NO);
				//分頁
				sqlStatement.setPageSize(pageSize.intValue());
				sqlStatement.setStartPage(pageIndex.intValue() - 1);
				SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
				AliasBean aliasBean = new AliasBean(MerchantDTO.class);
				relults = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
				return relults;
			} catch (Exception e) {
				LOGGER.error(".listBy() is error" + e);
				throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
			}
		}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IMerchantDAO#count(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Integer count(String queryCompanyId, String queryName, String queryMerchantCode) throws DataAccessException {
		try {
			LOGGER.debug(".getCount()", "parameters:queryCustomerId=" + queryCompanyId);
			LOGGER.debug(".getCount()", "parameters:queryName=" + queryName);
			LOGGER.debug(".getCount()", "parameters:queryMerchantCode=" + queryMerchantCode);
			String schema = this.getMySchema();	
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(".BIM_MERCHANT bm ");
			//sqlStatement.addFromExpression(schema + ".BIM_MERCHANT bm inner join " + schema + ".BIM_COMPANY bc on bm.COMPANY_ID = bc.COMPANY_ID");
			sqlStatement.addFromExpression(buffer.toString());
			if (StringUtils.hasText(queryCompanyId)) {
				sqlStatement.addWhereClause("bm.COMPANY_ID = :customId", queryCompanyId);
			}
			if (StringUtils.hasText(queryMerchantCode)) {
				sqlStatement.addWhereClause("bm.MERCHANT_CODE like :merchantCode", queryMerchantCode + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(queryName)) {
				sqlStatement.addWhereClause("NAME like :queryName", queryName + IAtomsConstants.MARK_PERCENT);
			}
			
			sqlStatement.addWhereClause("bm.DELETED =:deleted", IAtomsConstants.NO);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug(".getCount()", "sql:" + sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			//如果沒有查到返回0
			if (!CollectionUtils.isEmpty(result)) {
				return result.get(0);
			}
			return Integer.valueOf(0);
		} catch (Exception e) {
			LOGGER.error(".getCount() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IMerchantDAO#getMerchantNewInfo(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public MerchantDTO getMerchantInfo(String merchantId, String merchantCode, String companyId, String companyCode) throws DataAccessException {
		List<MerchantDTO> relults = null;
		MerchantDTO merchantDTO = null;
		SqlStatement sqlStatement = new SqlStatement();
		String schema = this.getMySchema();
		try {
			LOGGER.debug(".getMerchantNewInfo()", "parameters:merchantId=" + merchantId);
			LOGGER.debug(".getMerchantNewInfo()", "parameters:merchantCode=" + merchantCode);
			LOGGER.debug(".getMerchantNewInfo()", "parameters:companyId=" + companyId);
			LOGGER.debug(".getMerchantNewInfo()", "parameters:companyCode=" + companyCode);
			sqlStatement.addSelectClause("bm.MERCHANT_ID", MerchantDTO.ATTRIBUTE.MERCHANT_ID.getValue());
			sqlStatement.addSelectClause("bm.COMPANY_ID", MerchantDTO.ATTRIBUTE.COMPANY_ID.getValue());
			sqlStatement.addSelectClause("bc.SHORT_NAME", MerchantDTO.ATTRIBUTE.SHORT_NAME.getValue());
			sqlStatement.addSelectClause("bm.MERCHANT_CODE", MerchantDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			//Task #3249
			sqlStatement.addSelectClause("bm.UNITY_NUMBER", MerchantDTO.ATTRIBUTE.UNITY_NUMBER.getValue());
			sqlStatement.addSelectClause("bm.NAME", MerchantDTO.ATTRIBUTE.NAME.getValue());
			sqlStatement.addSelectClause("bm.REMARK", MerchantDTO.ATTRIBUTE.REMARK.getValue());
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(".BIM_MERCHANT bm inner join ").append(schema).append(".BIM_COMPANY bc on bm.COMPANY_ID=bc.COMPANY_ID");
			sqlStatement.addFromExpression(buffer.toString());
			//sqlStatement.addFromExpression(schema + ".BIM_MERCHANT bm inner join " + schema + ".BIM_COMPANY bc on bm.COMPANY_ID=bc.COMPANY_ID");
			//查詢條件
			sqlStatement.addWhereClause("ISNULL(bm.DELETED, 'N') !=:deleted", IAtomsConstants.YES);
			if (StringUtils.hasText(merchantId)) {
				sqlStatement.addWhereClause("MERCHANT_ID = :merchantId", merchantId);
			}
			if (StringUtils.hasText(companyId)) {
				sqlStatement.addWhereClause("bm.COMPANY_ID = :companyId", companyId);
			}
			if (StringUtils.hasText(companyCode)) {
				sqlStatement.addWhereClause("bc.COMPANY_CODE = :companyCode", companyCode);
			}
			if (StringUtils.hasText(merchantCode)) {
				sqlStatement.addWhereClause("MERCHANT_CODE = :merchantCode", merchantCode);
			}
			
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = new AliasBean(MerchantDTO.class);
			relults = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			LOGGER.debug(".getMerchantNewInfo()", "sql:" + sqlQueryBean.toString());
			//獲得值
			if (!CollectionUtils.isEmpty(relults)) {
				merchantDTO = relults.get(0);
			}
			return merchantDTO;
		} catch (Exception e) {
			LOGGER.error(".count() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IMerchantDAO#checkMidAndRegistedName(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isCheck(String merchantCode, String merchantId, String companyId) throws DataAccessException {
		try {
			LOGGER.debug(".checkMidAndRegistedName()", "parameters:merchantId=" + merchantId);
			LOGGER.debug(".checkMidAndRegistedName()", "parameters:merchantCode=" + merchantCode);
			LOGGER.debug(".checkMidAndRegistedName()", "parameters:companyId=" + companyId);
			String schema = this.getMySchema();	
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(".BIM_MERCHANT");
			sqlStatement.addFromExpression(buffer.toString());
			//編輯時傳入merchantId
			if(StringUtils.hasText(merchantId)){
				 sqlStatement.addWhereClause("MERCHANT_ID != :merchantId", merchantId);
			}
			if (StringUtils.hasText(companyId)) {
				sqlStatement.addWhereClause("COMPANY_ID = :companyId", companyId);
			}
			//查詢條件
			if (StringUtils.hasText(merchantCode)) {
				sqlStatement.addWhereClause("MERCHANT_CODE = :merchantCode", merchantCode);
			}
			
			sqlStatement.addWhereClause("DELETED =:deleted", IAtomsConstants.NO);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			//AliasBean aliasBean = new AliasBean(MerchantDTO.class);
			LOGGER.debug(".check()", "sql:" + sqlQueryBean.toString());
			List<Integer> results = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			//如果查到返回false
			if (!CollectionUtils.isEmpty(results)) {
				if (results.get(0).intValue() > 0) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			LOGGER.error(".check() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IMerchantDAO#isNoData()
	 */
	@Override
	public boolean isNoData() throws DataAccessException {
		boolean isNoData = false;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("COUNT(1)");
			sqlStatement.addFromExpression(schema + ".BIM_MERCHANT");
			//打印SQL語句
			LOGGER.debug("isNoData()", "SQL : ", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				Integer count = result.get(0);
				if(count == 0){
					isNoData = true;
				}
			}
		} catch (Exception e) {
			LOGGER.error("isNoData()", "error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE);
		}
		return isNoData;
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IMerchantDAO#getMerchantsByCodeAndCompamyId(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Parameter> getMerchantsByCodeAndCompamyId(String merchantCode, String companyId) throws DataAccessException {
		List<Parameter> relults = null;
		SqlStatement sqlStatement = new SqlStatement();
		String schema = this.getMySchema();
		try {
			LOGGER.debug(".getMerchantsByCodeAndCompamyId()", "parameters:merchantCode=" + merchantCode);
			LOGGER.debug(".getMerchantsByCodeAndCompamyId()", "parameters:companyId=" + companyId);

			sqlStatement.addSelectClause("MERCHANT_ID", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("MERCHANT_CODE", Parameter.FIELD_NAME);
			sqlStatement.addFromExpression(schema + ".BIM_MERCHANT ");
			//查詢條件
			if (StringUtils.hasText(companyId)) {
				sqlStatement.addWhereClause("COMPANY_ID = :companyId", companyId);
			}
			if (StringUtils.hasText(merchantCode)) {
				sqlStatement.addWhereClause("MERCHANT_CODE = :merchantCode", merchantCode);
			}
			
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = new AliasBean(Parameter.class);
			relults = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			LOGGER.debug(".getMerchantsByCodeAndCompamyId()", "sql:" + sqlQueryBean.toString());
			return relults;
		} catch (Exception e) {
			LOGGER.error(".getMerchantsByCodeAndCompamyId() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IMerchantDAO#deleteTransferData()
	 */
	@Override
	public void deleteTransferData() throws DataAccessException {
		try {
			String schema = this.getMySchema();
			SqlQueryBean sqlQueryBean = new SqlQueryBean("delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_ASSET_FUNCTION; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_ASSET_LINK; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_TRANSACTION_PARAMETER; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_TRANSACTION; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_ATT_FILE; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_COMM_MODE; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_HANDLE_INFO; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_NEW_ASSET_LINK; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_NEW_ASSET_FUNCTION; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_NEW_TRANSACTION_PARAMETER; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_NEW_COMM_MODE; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_NEW_HANDLE_INFO; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_FAULT_DESC; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_FAULT_COM; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY_DESC; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY_COMM; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".BIM_MERCHANT_HEADER; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".BIM_MERCHANT; ");
			
			LOGGER.debug("deleteTransferData()", "sql:" + sqlQueryBean.toString());
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("deleteTransferData()", e.getMessage(), e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
	}
}
