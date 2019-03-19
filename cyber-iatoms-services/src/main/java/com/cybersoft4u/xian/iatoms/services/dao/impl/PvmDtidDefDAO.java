package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.springframework.util.CollectionUtils;

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
import com.cybersoft4u.xian.iatoms.common.bean.dto.PvmDtidDefDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IPvmDtidDefDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.PvmDtidDef;

/**
 * Purpose: DTIO帳號管理DAO層實現類
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2016/9/21
 * @MaintenancePersonnel CarrieDuan
 */
public class PvmDtidDefDAO extends GenericBaseDAO<PvmDtidDef> implements IPvmDtidDefDAO{
	
	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(PvmDtidDefDAO.class);

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IPvmDtidDefDAO#listBy(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	public List<PvmDtidDefDTO> listBy(String customerId, String assetTypeId,
			String dtidStart, String dtidEnd, String sort, String order, int page, int size, boolean isPage)throws DataAccessException {
		List<PvmDtidDefDTO> dtidDefDTOs = null;
		try {
			LOGGER.debug(this.getClass().getName() + ".listBy()", "parameters:customerId=" + customerId);
			LOGGER.debug(this.getClass().getName() + ".listBy()", "parameters:assetTypeId=" + assetTypeId);
			LOGGER.debug(this.getClass().getName() + ".listBy()", "parameters:dtidStart=" + dtidStart);
			LOGGER.debug(this.getClass().getName() + ".listBy()", "parameters:dtidEnd=" + dtidEnd);
			LOGGER.debug(this.getClass().getName() + ".listBy()", "parameters:sort=" + sort);
			LOGGER.debug(this.getClass().getName() + ".listBy()", "parameters:size=" + size);
			LOGGER.debug(this.getClass().getName() + ".listBy()", "parameters:page=" + page);
			LOGGER.debug(this.getClass().getName() + ".listBy()", "parameters:order=" + order);
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("dtid.ID", PvmDtidDefDTO.ATTRIBUTE.ID.getValue());
			sqlStatement.addSelectClause("dtid.COMPANY_ID", PvmDtidDefDTO.ATTRIBUTE.COMPANY_ID.getValue());
			sqlStatement.addSelectClause("company.SHORT_NAME", PvmDtidDefDTO.ATTRIBUTE.COMPANY_NAME.getValue());
			sqlStatement.addSelectClause("type.NAME", PvmDtidDefDTO.ATTRIBUTE.ASSET_NAME.getValue());
			sqlStatement.addSelectClause("dtid.ASSET_TYPE_ID", PvmDtidDefDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			sqlStatement.addSelectClause("dtid.CURRENT_NUMBER", PvmDtidDefDTO.ATTRIBUTE.CURRENT_NUMBER.getValue());
			sqlStatement.addSelectClause("dtid.COMMENT", PvmDtidDefDTO.ATTRIBUTE.COMMENT.getValue());
			sqlStatement.addSelectClause("dtid.DTID_START", PvmDtidDefDTO.ATTRIBUTE.DTID_START.getValue());
			sqlStatement.addSelectClause("dtid.DTID_END", PvmDtidDefDTO.ATTRIBUTE.DTID_END.getValue());
			sqlStatement.addSelectClause("CONCAT( CONVERT( VARCHAR( 100 ) ,dtid.DTID_START ,111 ) ,'~' ,CONVERT( VARCHAR( 100 ) ,dtid.DTID_END ,111 ))", PvmDtidDefDTO.ATTRIBUTE.DTID_START_END.getValue());
			sqlStatement.addSelectClause("dtid.UPDATED_BY_NAME", PvmDtidDefDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("CONVERT(varchar(100), dtid.UPDATED_DATE, 120)", PvmDtidDefDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			sqlStatement.addFromExpression(schema + ".PVM_DTID_DEF dtid " 
							+ "left join " + schema + ".DMM_ASSET_TYPE type on type.ASSET_TYPE_ID = dtid.ASSET_TYPE_ID "
							+ "left join " + schema + ".BIM_COMPANY company on company.COMPANY_ID = dtid.COMPANY_ID");
//			sqlStatement.addWhereClause("isnull(dtid.IS_DELETED,'N') = :deleted", IAtomsConstants.PARAM_NO);
			if (StringUtils.hasText(customerId)) {
				sqlStatement.addWhereClause("dtid.COMPANY_ID = :customerId", customerId);
			}
			if (StringUtils.hasText(assetTypeId)) {
				sqlStatement.addWhereClause("dtid.ASSET_TYPE_ID = :assetTypeId", assetTypeId);
			}
			if (StringUtils.hasText(dtidEnd)) {
				sqlStatement.addWhereClause("dtid.dtid_start <= :dtidEnd", Integer.valueOf(dtidEnd));
			}
			if (StringUtils.hasText(dtidStart)) {
				sqlStatement.addWhereClause("dtid.dtid_end >= :dtidStart", Integer.valueOf(dtidStart));
			}
			if(isPage){
				sqlStatement.setPageSize(size);
				sqlStatement.setStartPage(page - 1);
			}
			if(isPage){
				if (StringUtils.hasText(sort) && StringUtils.hasText(order)) {
					sqlStatement.setOrderByExpression(sort+ " " + order + IAtomsConstants.MARK_SEPARATOR + PvmDtidDefDTO.ATTRIBUTE.ID.getValue());
				}
			} else {
				sqlStatement.setOrderByExpression("cast(dtid.DTID_START as int) asc");
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(PvmDtidDefDTO.class);
			LOGGER.debug(this.getClass().getName() + ".listBy()", "sql:" + sqlQueryBean.toString());
			dtidDefDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ":listBy() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return dtidDefDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IPvmDtidDefDAO#count(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public Integer count(String customerId, String assetTypeId, String dtidStart, String dtidEnd) throws DataAccessException {
		LOGGER.debug(".count()", "parameters:customerId=", customerId);
		LOGGER.debug(".count()", "parameters:dtidStart=", dtidStart);
		LOGGER.debug(".count()", "parameters:dtidEnd=", dtidEnd);
		LOGGER.debug(".count()", "parameters:assetTypeId=", assetTypeId);
		Integer result = Integer.valueOf(0);
		try {
			String schema = this.getMySchema();	
			//查询总条数
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("COUNT(1)");
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".PVM_DTID_DEF dtid ");
			fromBuffer.append("left join ").append(schema).append(".DMM_ASSET_TYPE type on type.ASSET_TYPE_ID = dtid.ASSET_TYPE_ID ");
			fromBuffer.append("left join ").append(schema).append(".BIM_COMPANY company on company.COMPANY_ID = dtid.COMPANY_ID");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(customerId)) {
				sqlStatement.addWhereClause("dtid.COMPANY_ID = :customerId", customerId);
			}
			if (StringUtils.hasText(assetTypeId)) {
				sqlStatement.addWhereClause("dtid.ASSET_TYPE_ID = :assetTypeId", assetTypeId);
			}
			if (StringUtils.hasText(dtidEnd)) {
				sqlStatement.addWhereClause("dtid.dtid_start <= :dtidEnd", Integer.valueOf(dtidEnd));
			}
			if (StringUtils.hasText(dtidStart)) {
				sqlStatement.addWhereClause("dtid.dtid_end >= :dtidStart", Integer.valueOf(dtidStart));
			}
//			sqlStatement.addWhereClause("isnull(dtid.IS_DELETED,'N') = :deleted", IAtomsConstants.PARAM_NO);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug(".listBy()", "sql:", sqlQueryBean.toString());
			List<Integer> list = super.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(list)){
				return list.get(0);
			}	
		} catch (Exception e) {
			LOGGER.error(".count() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return result;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IPvmDtidDefDAO#checkRepeat(java.lang.Integer, java.lang.Integer)
	 */
	public boolean isCheckRepeat(Integer dtidStart, Integer dtidEnd, String id) throws DataAccessException {
		LOGGER.debug(".checkRepeat()", "parameters:dtidStart=", dtidStart.longValue());
		LOGGER.debug(".checkRepeat()", "parameters:dtidEnd=", dtidEnd.longValue());
		LOGGER.debug(".checkRepeat()", "parameters:id=", id);
		List<Integer> result = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("COUNT(1)");
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".PVM_DTID_DEF dtid");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(id)) {
				sqlStatement.addWhereClause("dtid.DTID_START <= :end and dtid.DTID_end >= :start");
				sqlStatement.addWhereClause("dtid.id <> :id", id);
			} else {
				sqlStatement.addWhereClause("dtid.DTID_START <= :end and dtid.DTID_END >= :start");
			}
//			sqlStatement.addWhereClause("isnull(dtid.IS_DELETED,'N') = :deleted", IAtomsConstants.PARAM_NO);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("start", dtidStart);
			sqlQueryBean.setParameter("end", dtidEnd);
			LOGGER.debug(".checkRepeat()", "sql:", sqlQueryBean.toString());
			result = super.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)){
				if (result.get(0).intValue() > 0) {
					return false;
				}
			}
		} catch (Exception e) {
			LOGGER.error(".checkRepeat() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return true;
	}
}
