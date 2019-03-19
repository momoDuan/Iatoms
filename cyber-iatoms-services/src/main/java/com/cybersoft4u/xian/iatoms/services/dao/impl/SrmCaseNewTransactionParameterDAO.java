package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

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
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseNewTransactionParameterDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmTransactionParameterItemDTO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseNewTransactionParameterDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseNewTransactionParameter;
/**
 * Purpose: SRM_案件最新交易參數資料檔DAO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2016/12/15
 * @MaintenancePersonnel CarrieDuan
 */
public class SrmCaseNewTransactionParameterDAO extends GenericBaseDAO<SrmCaseNewTransactionParameter> implements ISrmCaseNewTransactionParameterDAO{
	
	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog logger = CafeLogFactory.getLog(SrmCaseNewTransactionParameterDAO.class);

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseNewTransactionParameterDAO#listTransactionParameterDTOsByDtid(java.lang.String)
	 */
	public List<SrmCaseNewTransactionParameterDTO> listTransactionParameterDTOsByDtid(String dtid, boolean isNewHave) {
		List<SrmCaseNewTransactionParameterDTO> caseNewTransactionParameterDTOs = null;
		try{
			logger.debug(this.getClass().getName() + ".listTransactionParameterDTOsByDtid()", "parameters:dtid=" + dtid);
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("DISTINCT trans.PARAMTER_VALUE_ID", SrmCaseNewTransactionParameterDTO.ATTRIBUTE.PARAMTER_VALUE_ID.getValue());
			sqlStatement.addSelectClause("trans.TRANSACTION_TYPE", SrmCaseNewTransactionParameterDTO.ATTRIBUTE.TRANSACTION_TYPE.getValue());
			sqlStatement.addSelectClause("item.ITEM_NAME", SrmCaseNewTransactionParameterDTO.ATTRIBUTE.TRANSACTION_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("trans.MERCHANT_CODE", SrmCaseNewTransactionParameterDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("trans.MERCHANT_CODE_OTHER", SrmCaseNewTransactionParameterDTO.ATTRIBUTE.MERCHANT_CODE_OTHER.getValue());
			sqlStatement.addSelectClause("trans.TID", SrmCaseNewTransactionParameterDTO.ATTRIBUTE.TID.getValue());
			sqlStatement.addSelectClause("trans.CASE_ID", SrmCaseNewTransactionParameterDTO.ATTRIBUTE.CASE_ID.getValue());
			if(isNewHave){
				sqlStatement.addSelectClause("trans.DTID", SrmCaseNewTransactionParameterDTO.ATTRIBUTE.DTID.getValue());
			} else{
				sqlStatement.addSelectClause("info.DTID", SrmCaseNewTransactionParameterDTO.ATTRIBUTE.DTID.getValue());
			}
			sqlStatement.addSelectClause("trans.ITEM_VALUE", SrmCaseNewTransactionParameterDTO.ATTRIBUTE.ITEM_VALUE.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			if(isNewHave){
				fromBuffer.append(schema).append(".SRM_CASE_NEW_TRANSACTION_PARAMETER trans ");
			} else {
				fromBuffer.append(schema).append(".SRM_CASE_TRANSACTION_PARAMETER trans left join ").append(schema).append(".SRM_CASE_HANDLE_INFO info on info.CASE_ID = trans.CASE_ID ");
			}
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF item on item.BPTD_CODE=:transactionCategory and item.ITEM_VALUE = trans.TRANSACTION_TYPE");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(dtid)) {
				if(isNewHave){
					sqlStatement.addWhereClause("trans.DTID = :dtid", dtid);
				} else {
					sqlStatement.addWhereClause("info.DTID = :dtid", dtid);
				}
			}
			StringBuffer whereBuffer = new StringBuffer();
			whereBuffer.append("item.EFFECTIVE_DATE =(");
			whereBuffer.append("select max(item1.EFFECTIVE_DATE) from ").append(schema);
			whereBuffer.append(".SRM_TRANSACTION_PARAMETER_ITEM item1 where ");
			whereBuffer.append("isnull(item1.APPROVED_FLAG,'N') = :approvedFlag ");
			whereBuffer.append("and item1.EFFECTIVE_DATE <= :effectiveDate)");
			sqlStatement.addWhereClause(whereBuffer.toString());
			
			if(!isNewHave){
				whereBuffer.delete(0, whereBuffer.length());
				whereBuffer.append(" info.CASE_ID in (select max(caseInfo.CASE_ID) from ");
				whereBuffer.append(schema).append(".SRM_CASE_HANDLE_INFO caseInfo ");
				whereBuffer.append(" where NOT EXISTS(select 1 from ").append(schema).append(".SRM_CASE_NEW_HANDLE_INFO newInfo where newInfo.DTID = caseInfo.DTID) ");
			//	whereBuffer.append(" and NOT EXISTS(select 1 from ").append(schema).append(".SRM_CASE_HANDLE_INFO caseInfo2 where caseInfo2.DTID =caseInfo.DTID and caseInfo2.CREATED_DATE > caseInfo.CREATED_DATE) ");
				
				whereBuffer.append(" and NOT EXISTS(select 1 from ").append(schema).append(".SRM_CASE_HANDLE_INFO caseInfo2 where caseInfo2.DTID =caseInfo.DTID and caseInfo2.CREATED_DATE > caseInfo.CREATED_DATE ");
				whereBuffer.append(" AND caseInfo2.CASE_CATEGORY = 'INSTALL' AND caseInfo2.CASE_STATUS not in ('ImmediateClose', 'Closed', 'Voided', 'Completed', 'WaitClose') ) ");
				
				whereBuffer.append(" AND caseInfo.CASE_CATEGORY = 'INSTALL' AND caseInfo.CASE_STATUS not in ('ImmediateClose', 'Closed', 'Voided', 'Completed', 'WaitClose')");
				whereBuffer.append(" group by caseInfo.DTID)");
				sqlStatement.addWhereClause(whereBuffer.toString());
			}
			
			sqlStatement.setOrderByExpression("trans.PARAMTER_VALUE_ID");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter(SrmTransactionParameterItemDTO.ATTRIBUTE.APPROVED_FLAG.getValue(), IAtomsConstants.YES);
			sqlQueryBean.setParameter(SrmTransactionParameterItemDTO.ATTRIBUTE.EFFECTIVE_DATE.getValue(), DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMMDD_DASH));
			sqlQueryBean.setParameter("transactionCategory", IATOMS_PARAM_TYPE.TRANSACTION_CATEGORY.getCode());
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseNewTransactionParameterDTO.class);
			logger.debug(this.getClass().getName() + ".listTransactionParameterDTOsByDtid()", "sql:" + sqlQueryBean.toString());
			caseNewTransactionParameterDTOs = super.getDaoSupport().findByNativeSql(sqlQueryBean,aliasBean);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+":listTransactionParameterDTOsByDtid() is error." + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return caseNewTransactionParameterDTOs;
	}

	@Override
	public List<SrmCaseNewTransactionParameterDTO> getTransactionParameterDTOsByDtid(String dtid, boolean isHave) {
		List<SrmCaseNewTransactionParameterDTO> caseNewTransactionParameterDTOs = null;
		try{ // 改為抓處理中  如果處理中全部結案 抓最新資料檔 2018/01/04 Bug #3055
			logger.debug(this.getClass().getName() + ".getTransactionParameterDTOsByDtid()", "parameters:dtid=" + dtid);
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("DISTINCT trans.PARAMTER_VALUE_ID", SrmCaseNewTransactionParameterDTO.ATTRIBUTE.PARAMTER_VALUE_ID.getValue());
			sqlStatement.addSelectClause("trans.TRANSACTION_TYPE", SrmCaseNewTransactionParameterDTO.ATTRIBUTE.TRANSACTION_TYPE.getValue());
			sqlStatement.addSelectClause("item.ITEM_NAME", SrmCaseNewTransactionParameterDTO.ATTRIBUTE.TRANSACTION_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("trans.MERCHANT_CODE", SrmCaseNewTransactionParameterDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("trans.MERCHANT_CODE_OTHER", SrmCaseNewTransactionParameterDTO.ATTRIBUTE.MERCHANT_CODE_OTHER.getValue());
			sqlStatement.addSelectClause("trans.TID", SrmCaseNewTransactionParameterDTO.ATTRIBUTE.TID.getValue());
			if(!isHave){
				sqlStatement.addSelectClause("trans.DTID", SrmCaseNewTransactionParameterDTO.ATTRIBUTE.DTID.getValue());
			} else{
				sqlStatement.addSelectClause("info.DTID", SrmCaseNewTransactionParameterDTO.ATTRIBUTE.DTID.getValue());
			}
			sqlStatement.addSelectClause("trans.ITEM_VALUE", SrmCaseNewTransactionParameterDTO.ATTRIBUTE.ITEM_VALUE.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			if(!isHave){
				fromBuffer.append(schema).append(".SRM_CASE_NEW_TRANSACTION_PARAMETER trans ");
			} else {
				fromBuffer.append(schema).append(".SRM_CASE_TRANSACTION_PARAMETER trans left join ").append(schema).append(".SRM_CASE_HANDLE_INFO info on info.CASE_ID = trans.CASE_ID ");
			}
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF item on item.BPTD_CODE=:transactionCategory and item.ITEM_VALUE = trans.TRANSACTION_TYPE");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(dtid)) {
				if(!isHave){
					sqlStatement.addWhereClause("trans.DTID = :dtid", dtid);
				} else {
					sqlStatement.addWhereClause("info.DTID = :dtid", dtid);
				}
			}
			StringBuffer whereBuffer = new StringBuffer();
			whereBuffer.append("item.EFFECTIVE_DATE =(");
			whereBuffer.append("select max(item1.EFFECTIVE_DATE) from ").append(schema);
			whereBuffer.append(".SRM_TRANSACTION_PARAMETER_ITEM item1 where ");
			whereBuffer.append("isnull(item1.APPROVED_FLAG,'N') = :approvedFlag ");
			whereBuffer.append("and item1.EFFECTIVE_DATE <= :effectiveDate)");
			sqlStatement.addWhereClause(whereBuffer.toString());
			
			if(isHave){
				whereBuffer.delete(0, whereBuffer.length());
				/*whereBuffer.append(" info.CASE_ID in (select max(caseInfo.CASE_ID) from ");
				whereBuffer.append(schema).append(".SRM_CASE_HANDLE_INFO caseInfo ");
				whereBuffer.append(" where NOT EXISTS(select 1 from ").append(schema).append(".SRM_CASE_NEW_HANDLE_INFO newInfo where newInfo.DTID = caseInfo.DTID) ");
			//	whereBuffer.append(" and NOT EXISTS(select 1 from ").append(schema).append(".SRM_CASE_HANDLE_INFO caseInfo2 where caseInfo2.DTID =caseInfo.DTID and caseInfo2.CREATED_DATE > caseInfo.CREATED_DATE) ");
				
				whereBuffer.append(" and NOT EXISTS(select 1 from ").append(schema).append(".SRM_CASE_HANDLE_INFO caseInfo2 where caseInfo2.DTID =caseInfo.DTID and caseInfo2.CREATED_DATE > caseInfo.CREATED_DATE ");
				whereBuffer.append(" AND caseInfo2.CASE_CATEGORY = 'INSTALL' AND caseInfo2.CASE_STATUS not in ('ImmediateClose', 'Closed', 'Voided', 'Completed', 'WaitClose') ) ");
				
				whereBuffer.append(" AND caseInfo.CASE_CATEGORY = 'INSTALL' AND caseInfo.CASE_STATUS not in ('ImmediateClose', 'Closed', 'Voided', 'Completed', 'WaitClose')");
				whereBuffer.append(" group by caseInfo.DTID)");*/
				whereBuffer.append(" info.CASE_ID =(SELECT top 1 CASE_ID FROM ").append(schema).append(".SRM_CASE_HANDLE_INFO fo WHERE 1=1 ");
				if (StringUtils.hasText(dtid)) {
					whereBuffer.append("AND fo.DTID ='").append(dtid).append("' ");
				}
				whereBuffer.append("AND fo.CASE_STATUS not in ('ImmediateClose', 'Closed', 'Voided', 'WaitDispatch') and fo.CASE_CATEGORY <> 'OTHER' ORDER BY fo.CREATED_DATE desc )");
				sqlStatement.addWhereClause(whereBuffer.toString());
			}
			
			sqlStatement.setOrderByExpression("trans.PARAMTER_VALUE_ID");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter(SrmTransactionParameterItemDTO.ATTRIBUTE.APPROVED_FLAG.getValue(), IAtomsConstants.YES);
			sqlQueryBean.setParameter(SrmTransactionParameterItemDTO.ATTRIBUTE.EFFECTIVE_DATE.getValue(), DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMMDD_DASH));
			sqlQueryBean.setParameter("transactionCategory", IATOMS_PARAM_TYPE.TRANSACTION_CATEGORY.getCode());
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseNewTransactionParameterDTO.class);
			logger.debug(this.getClass().getName() + ".getTransactionParameterDTOsByDtid()", "sql:" + sqlQueryBean.toString());
			caseNewTransactionParameterDTOs = super.getDaoSupport().findByNativeSql(sqlQueryBean,aliasBean);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+":getTransactionParameterDTOsByDtid() is error." + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return caseNewTransactionParameterDTOs;
	}
	
	
}
