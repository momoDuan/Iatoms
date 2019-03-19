package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.config.GenericConfigManager;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmEdcLabelDTO;
import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmEdcLabelDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmEdcLabel;

/**
 * Purpose:刷卡機標籤管理DAO
 * @author NickLin 
 * @since  JDK 1.7
 * @date   2018/06/27
 * @MaintenancePersonnel CyberSoft
 */
public class DmmEdcLabelDAO extends GenericBaseDAO<DmmEdcLabel> implements IDmmEdcLabelDAO {

	/**
	 * 系统log物件  
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.DAO, DmmEdcLabelDAO.class);
	
	/**
	 * Constructor:無參數建構子
	 */
	public DmmEdcLabelDAO() {
	}

	/** 
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmEdcLabelDAO#listBy(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DmmEdcLabelDTO> listBy(String queryMerchantCode,
			String queryDtid, String queryStartDate, String queryEndDate,
			Integer pageSize, Integer pageIndex, String sort, String orderby)
			throws DataAccessException {
		// TODO Auto-generated method stub
		LOGGER.debug("DmmEdcLabelDAO.listBy.queryMerchantCode:'" + queryMerchantCode + "'");
		LOGGER.debug("DmmEdcLabelDAO.listBy.queryDtid:'" + queryDtid + "'");
		LOGGER.debug("DmmEdcLabelDAO.listBy.queryStartDate:'" + queryStartDate + "'");
		LOGGER.debug("DmmEdcLabelDAO.listBy.queryEndDate:'" + queryEndDate + "'");
		LOGGER.debug("DmmEdcLabelDAO.listBy.pageSize:'" + pageSize + "'");
		LOGGER.debug("DmmEdcLabelDAO.listBy.pageIndex:'" + pageIndex + "'");
		LOGGER.debug("DmmEdcLabelDAO.listBy.sort:'" + sort + "'");
		LOGGER.debug("DmmEdcLabelDAO.listBy.orderby:'" + orderby + "'");
		List<DmmEdcLabelDTO> edcLabelDTOs = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("edc.SEQ_NO", DmmEdcLabelDTO.ATTRIBUTE.SEQ_NO.getValue());
			sqlStatement.addSelectClause("edc.MERCHANT_CODE", DmmEdcLabelDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("edc.DTID", DmmEdcLabelDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("edc.MERCHANT_TYPE", DmmEdcLabelDTO.ATTRIBUTE.MERCHANT_TYPE.getValue());
			sqlStatement.addSelectClause("edc.RELATION", DmmEdcLabelDTO.ATTRIBUTE.RELATION.getValue());
			sqlStatement.addSelectClause("edc.STATUS", DmmEdcLabelDTO.ATTRIBUTE.STATUS.getValue());
			sqlStatement.addSelectClause("edc.IP", DmmEdcLabelDTO.ATTRIBUTE.IP.getValue());
			sqlStatement.addSelectClause("edc.CREATED_BY_ID", DmmEdcLabelDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			sqlStatement.addSelectClause("edc.CREATED_BY_NAME", DmmEdcLabelDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("edc.CREATED_DATE", DmmEdcLabelDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sqlStatement.addSelectClause("edc.UPDATED_BY_ID", DmmEdcLabelDTO.ATTRIBUTE.UPDATED_BY_ID.getValue());
			sqlStatement.addSelectClause("edc.UPDATED_BY_NAME", DmmEdcLabelDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("edc.UPDATED_DATE", DmmEdcLabelDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			sqlStatement.setFromExpression(" " + schema + ".DMM_EDC_LABEL edc ");
			//特店代號
			if (StringUtils.hasText(queryMerchantCode)) {
				sqlStatement.addWhereClause("edc.MERCHANT_CODE like :queryMerchantCode", queryMerchantCode + IAtomsConstants.MARK_PERCENT);
			}
			//DTID
			if (StringUtils.hasText(queryDtid)) {
				sqlStatement.addWhereClause("edc.DTID like :queryDtid", queryDtid + IAtomsConstants.MARK_PERCENT);
			}
			//匯入日期起
			if(StringUtils.hasText(queryStartDate)){
				sqlStatement.addWhereClause("replace(convert(varchar(16), edc.UPDATED_DATE, 120), '-', '/') >= :queryStartDate", queryStartDate);
			}
			//匯入日期迄
			if(StringUtils.hasText(queryStartDate)){
				sqlStatement.addWhereClause("replace(convert(varchar(16), edc.UPDATED_DATE, 120), '-', '/') <= :queryEndDate", queryEndDate);
			}
			//設置排序表達式
			if (StringUtils.hasText(sort) && StringUtils.hasText(orderby)){
				sqlStatement.setOrderByExpression(sort + " " + orderby);
			} else {
				sqlStatement.setOrderByExpression("edc.SEQ_NO, edc.MERCHANT_CODE desc");
			}
			sqlStatement.setPageSize(pageSize);
			sqlStatement.setStartPage(pageIndex - 1);
			//創建SqlQueryBean
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmEdcLabelDTO.class);
			LOGGER.debug("listBy()", "sql:" + sqlQueryBean.toString());
			edcLabelDTOs = (List<DmmEdcLabelDTO>) super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listBy()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,
					new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_DMM_EDC_LABEL)}, e);
		}
		return edcLabelDTOs;
	}

	/** 
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmEdcLabelDAO#count(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Integer count(String queryMerchantCode, String queryDtid,
			String queryStartDate, String queryEndDate)
			throws DataAccessException {
		// TODO Auto-generated method stub
		LOGGER.debug("DmmEdcLabelDAO.count.queryMerchantCode:'" + queryMerchantCode + "'");
		LOGGER.debug("DmmEdcLabelDAO.count.queryDtid:'" + queryDtid + "'");
		LOGGER.debug("DmmEdcLabelDAO.count.queryStartDate:'" + queryStartDate + "'");
		LOGGER.debug("DmmEdcLabelDAO.count.queryEndDate:'" + queryEndDate + "'");
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			sqlStatement.setFromExpression(" " + schema + ".DMM_EDC_LABEL edc ");
			//特店代號
			if (StringUtils.hasText(queryMerchantCode)) {
				sqlStatement.addWhereClause("edc.MERCHANT_CODE like :queryMerchantCode", queryMerchantCode + IAtomsConstants.MARK_PERCENT);
			}
			//DTID
			if (StringUtils.hasText(queryDtid)) {
				sqlStatement.addWhereClause("edc.DTID like :queryDtid", queryDtid + IAtomsConstants.MARK_PERCENT);
			}
			//匯入日期起
			if(StringUtils.hasText(queryStartDate)){
				sqlStatement.addWhereClause("replace(convert(varchar(16), edc.UPDATED_DATE, 120), '-', '/') >= :queryStartDate", queryStartDate);
			}
			//匯入日期迄
			if(StringUtils.hasText(queryStartDate)){
				sqlStatement.addWhereClause("replace(convert(varchar(16), edc.UPDATED_DATE, 120), '-', '/') <= :queryEndDate", queryEndDate);
			}
			//記錄語句
			LOGGER.debug(this.getClass().getName() + ".count()", "sql:" + sqlStatement.toString());
			//創建SqlQueryBean
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			List<Integer> result = super.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				return result.get(0).intValue();
			}
			return 0;
		} catch (Exception e) {
			LOGGER.error("count()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,
					new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_DMM_EDC_LABEL)}, e);
		}
	}

	/** 
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmEdcLabelDAO#isUpdate(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean isUpdate(String merchantCode, String dtid)
			throws DataAccessException {
		// TODO Auto-generated method stub
		LOGGER.debug("DmmEdcLabelDAO.isUpdate.merchantCode:'" + merchantCode + "'");
		LOGGER.debug("DmmEdcLabelDAO.isUpdate.dtid:'" + dtid + "'");
		try {
			String schema = this.getMySchema();	
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			sqlStatement.setFromExpression(" " + schema + ".DMM_EDC_LABEL ");
			//特店代號
			if (StringUtils.hasText(merchantCode)) {
				sqlStatement.addWhereClause("MERCHANT_CODE = :merchantCode", merchantCode);
			}
			//DTID
			if (StringUtils.hasText(dtid)) {
				sqlStatement.addWhereClause("DTID = :dtid", dtid);
			}
			//記錄語句
			LOGGER.debug(this.getClass().getName() + ".isUpdate()", "sql:" + sqlStatement.toString());
			//創建SqlQueryBean
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			List<Integer> results = super.getDaoSupport().findByNativeSql(sqlQueryBean);
			//如果查到返回true
			if (!CollectionUtils.isEmpty(results)) {
				if (results.get(0).intValue() > 0) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			LOGGER.error("isUpdate()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,
					new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_DMM_EDC_LABEL)}, e);
		}
	}

	/** 
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmEdcLabelDAO#getInfoByCompositeKey(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DmmEdcLabelDTO> getInfoByCompositeKey(String merchantCode,
			String dtid) throws DataAccessException {
		// TODO Auto-generated method stub
		LOGGER.debug("DmmEdcLabelDAO.isUpdate.merchantCode:'" + merchantCode + "'");
		LOGGER.debug("DmmEdcLabelDAO.isUpdate.dtid:'" + dtid + "'");
		List<DmmEdcLabelDTO> edcLabelDTOs = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("edc.SEQ_NO", DmmEdcLabelDTO.ATTRIBUTE.SEQ_NO.getValue());
			sqlStatement.addSelectClause("edc.MERCHANT_CODE", DmmEdcLabelDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("edc.DTID", DmmEdcLabelDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("edc.MERCHANT_TYPE", DmmEdcLabelDTO.ATTRIBUTE.MERCHANT_TYPE.getValue());
			sqlStatement.addSelectClause("edc.RELATION", DmmEdcLabelDTO.ATTRIBUTE.RELATION.getValue());
			sqlStatement.addSelectClause("edc.STATUS", DmmEdcLabelDTO.ATTRIBUTE.STATUS.getValue());
			sqlStatement.addSelectClause("edc.IP", DmmEdcLabelDTO.ATTRIBUTE.IP.getValue());
			sqlStatement.addSelectClause("edc.CREATED_BY_ID", DmmEdcLabelDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			sqlStatement.addSelectClause("edc.CREATED_BY_NAME", DmmEdcLabelDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("edc.CREATED_DATE", DmmEdcLabelDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sqlStatement.addSelectClause("edc.UPDATED_BY_ID", DmmEdcLabelDTO.ATTRIBUTE.UPDATED_BY_ID.getValue());
			sqlStatement.addSelectClause("edc.UPDATED_BY_NAME", DmmEdcLabelDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("edc.UPDATED_DATE", DmmEdcLabelDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			sqlStatement.setFromExpression(" " + schema + ".DMM_EDC_LABEL edc ");
			//特店代號
			if (StringUtils.hasText(merchantCode)) {
				sqlStatement.addWhereClause("edc.MERCHANT_CODE = :merchantCode", merchantCode);
			}
			//DTID
			if (StringUtils.hasText(dtid)) {
				sqlStatement.addWhereClause("edc.DTID = :dtid", dtid);
			}
			sqlStatement.setOrderByExpression("edc.MERCHANT_CODE asc");
			//創建SqlQueryBean
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmEdcLabelDTO.class);
			//記錄語句
			LOGGER.debug(this.getClass().getName() + ".getInfoByCompositeKey()", "sql:" + sqlStatement.toString());
			edcLabelDTOs = (List<DmmEdcLabelDTO>) super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("getInfoByCompositeKey()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,
					new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_DMM_EDC_LABEL)}, e);
		}
		return edcLabelDTOs;
	}
}
