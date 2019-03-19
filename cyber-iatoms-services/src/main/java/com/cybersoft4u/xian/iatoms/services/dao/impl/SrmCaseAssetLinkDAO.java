package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.type.BigDecimalType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.util.CollectionUtils;

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

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAssetLinkDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTransactionParameterDTO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseAssetLink;

/**
 * Purpose: SRM_案件處理中設備連接檔 DAO 實現類
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2017/2/24
 * @MaintenancePersonnel CrissZhang
 */
public class SrmCaseAssetLinkDAO extends GenericBaseDAO<SrmCaseAssetLink> implements ISrmCaseAssetLinkDAO{
	
	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(SrmCaseAssetLinkDAO.class);
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO#listByCaseId(java.lang.String)
	 */
	@Override
	public List<SrmCaseAssetLinkDTO> listByCaseId(String caseId, String flag, Boolean isAssetLink, Boolean isSelectDelete)
			throws DataAccessException {
		List<SrmCaseAssetLinkDTO> SrmCaseAssetLinkDTOS = null;
		try{
			LOGGER.debug(this.getClass().getName() + ".listByCaseId()", "parameters:caseId=" + caseId);
			LOGGER.debug(this.getClass().getName() + ".listByCaseId()", "parameters:flag=" + flag);
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("caseAssetLink.ASSET_LINK_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.ASSET_LINK_ID.getValue());
			sqlStatement.addSelectClause("caseAssetLink.SERIAL_NUMBER", SrmCaseAssetLinkDTO.ATTRIBUTE.UNINSTALL_SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("caseAssetLink.SERIAL_NUMBER", SrmCaseAssetLinkDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("caseAssetLink.ITEM_TYPE", SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_TYPE.getValue());
			sqlStatement.addSelectClause("caseAssetLink.ITEM_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_ID.getValue());
			sqlStatement.addSelectClause("caseAssetLink.ITEM_CATEGORY", SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_CATEGORY.getValue());
			sqlStatement.addSelectClause("caseAssetLink.IS_LINK", SrmCaseAssetLinkDTO.ATTRIBUTE.IS_LINK.getValue());
			if(isAssetLink) {
				sqlStatement.addSelectClause("assetType.NAME", SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_NAME.getValue());
			} else {
				sqlStatement.addSelectClause("supplies.SUPPLIES_NAME", SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_NAME.getValue());
			}
			sqlStatement.addSelectClause("caseAssetLink.NUMBER", SrmCaseAssetLinkDTO.ATTRIBUTE.NUMBER.getValue());
			sqlStatement.addSelectClause("caseAssetLink.CONTENT", SrmCaseAssetLinkDTO.ATTRIBUTE.CONTENT.getValue());
			sqlStatement.addSelectClause("baseParam.ITEM_NAME", SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_CATEGORY_NAME.getValue());
			sqlStatement.addSelectClause("caseAssetLink.CASE_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("caseAssetLink.ACTION", SrmCaseAssetLinkDTO.ATTRIBUTE.ACTION.getValue());
			sqlStatement.addSelectClause("caseAssetLink.CREATED_BY_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			sqlStatement.addSelectClause("caseAssetLink.CREATED_BY_NAME", SrmCaseAssetLinkDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("caseAssetLink.CREATED_DATE", SrmCaseAssetLinkDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sqlStatement.addSelectClause("caseAssetLink.ENABLE_DATE", SrmCaseAssetLinkDTO.ATTRIBUTE.ENABLE_DATE.getValue());
			sqlStatement.addSelectClause("caseAssetLink.WAREHOUSE_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.WAREHOUSE_ID.getValue());
			sqlStatement.addSelectClause("caseAssetLink.CONTRACT_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.CONTRACT_ID.getValue());
			sqlStatement.addSelectClause("caseAssetLink.PRICE", SrmCaseAssetLinkDTO.ATTRIBUTE.PRICE.getValue());
			sqlStatement.addSelectClause("caseAssetLink.HISTORY_ASSET_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.HISTORY_ASSET_ID.getValue());
			sqlStatement.addSelectClause("caseAssetLink.PROPERTY_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.PROPERTY_ID.getValue());
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(".SRM_CASE_ASSET_LINK caseAssetLink ");
			buffer.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF baseParam on caseAssetLink.ITEM_CATEGORY = baseParam.ITEM_VALUE and baseParam.BPTD_CODE = :paramType ");
			if(isAssetLink) {
				buffer.append(" left join ").append(schema).append(".DMM_ASSET_TYPE assetType ON assetType.ASSET_TYPE_ID = caseAssetLink.ITEM_ID ");
			} else {
				buffer.append(" left join ").append(schema).append(".DMM_SUPPLIES supplies on caseAssetLink.ITEM_ID = supplies.SUPPLIES_ID ");
			}
			sqlStatement.addFromExpression(buffer.toString());
			if (StringUtils.hasText(caseId)) {
				sqlStatement.addWhereClause("caseAssetLink.CASE_ID = :caseId", caseId);
			}
			// 查詢耗材類型與設備類型
			if(isAssetLink) {
				sqlStatement.addWhereClause("caseAssetLink.ITEM_TYPE != :flag", flag);
				if(isSelectDelete) {
					sqlStatement.addWhereClause("caseAssetLink.IS_LINK != :deleteFlag");
				}
			} else {
				sqlStatement.addWhereClause("caseAssetLink.ITEM_TYPE = :flag", flag);
			}
			//sqlStatement.setOrderByExpression("caseAssetLink.SERIAL_NUMBER desc");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if(isAssetLink) {
				// 查設備類別
				sqlQueryBean.setParameter("paramType", IATOMS_PARAM_TYPE.ASSET_CATEGORY.getCode());
				if(isSelectDelete) {
					sqlQueryBean.setParameter("deleteFlag", IAtomsConstants.CASE_DELETE_ASSET);
				}
			} else {
				// 查耗材類別
				sqlQueryBean.setParameter("paramType", IATOMS_PARAM_TYPE.SUPPLIES_TYPE.getCode());
			}
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseAssetLinkDTO.class);
			LOGGER.debug(this.getClass().getName() + ".listByCaseId()", "sql:" + sqlQueryBean.toString());
			SrmCaseAssetLinkDTOS = super.getDaoSupport().findByNativeSql(sqlQueryBean,aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+":listByCaseId() is error." + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return SrmCaseAssetLinkDTOS;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO#updateCaseAsset(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateCaseAsset(String caseId, String assetId, String itemType) throws DataAccessException {
		try {
			LOGGER.debug("updateCaseAsset()", "parameters:caseId=" + caseId);
			LOGGER.debug("updateCaseAsset()", "parameters:assetId=" + assetId);
			LOGGER.debug("updateCaseAsset()", "parameters:itemType=" + itemType);
			if (StringUtils.hasText(caseId)) {
				SqlQueryBean sqlQueryBean = new SqlQueryBean("UPDATE ");
				String schema = this.getMySchema();
				sqlQueryBean.append(schema).append(".SRM_CASE_ASSET_LINK  SET IS_LINK = :removeParam");
				sqlQueryBean.append("where 1=1");
				if(StringUtils.hasText(caseId)){
					sqlQueryBean.append(" and CASE_ID = :caseId ");
					sqlQueryBean.setParameter("caseId", caseId);
				}
				if(StringUtils.hasText(assetId)){
					sqlQueryBean.append(" and ITEM_ID = :assetId ");
					sqlQueryBean.setParameter("assetId", assetId);
				}
				if(StringUtils.hasText(itemType)){
					sqlQueryBean.append(" and ITEM_TYPE = :itemType ");
					sqlQueryBean.setParameter("itemType", itemType);
				}
				sqlQueryBean.setParameter("removeParam", IAtomsConstants.CASE_REMOVE_ASSET);
				LOGGER.debug("updateCaseAsset()", "sql:" + sqlQueryBean.toString());
				this.getDaoSupport().updateByNativeSql(sqlQueryBean);
				this.getDaoSupport().flush();
			}
		} catch (Exception e) {
			LOGGER.error("updateCaseAsset()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO#deleteAll(java.lang.String)
	 */
	public void deleteAll(String caseId) throws DataAccessException {
		try {
			LOGGER.debug("deleteAll()", "parameters:caseId=" + caseId);
			if (StringUtils.hasText(caseId)) {
				SqlQueryBean sqlQueryBean = new SqlQueryBean("delete ");
				String schema = this.getMySchema();
				sqlQueryBean.append(schema).append(".SRM_CASE_ASSET_LINK where case_id=:caseId");
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
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO#deleteIgnoreItem(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteIgnoreItem(String caseId, String ignoreItem)throws DataAccessException {
		try {
			LOGGER.debug("deleteIgnoreItem()", "parameters:caseId=" + caseId);
			LOGGER.debug("deleteIgnoreItem()", "parameters:ignoreItem=" + ignoreItem);
			if (StringUtils.hasText(caseId)) {
				SqlQueryBean sqlQueryBean = new SqlQueryBean("delete ");
				String schema = this.getMySchema();
				sqlQueryBean.append(schema).append(".SRM_CASE_ASSET_LINK where CASE_ID=:caseId");
				// 添加忽略項目的處理
				if(StringUtils.hasText(ignoreItem)){
					sqlQueryBean.append(" and ITEM_TYPE not in (:ignoreItem)");
					sqlQueryBean.setParameter("ignoreItem", StringUtils.toList(ignoreItem, IAtomsConstants.MARK_SEPARATOR));
				}
				sqlQueryBean.setParameter(SrmCaseTransactionParameterDTO.ATTRIBUTE.CASE_ID.getValue(), caseId);
				LOGGER.debug("deleteIgnoreItem()", "sql:" + sqlQueryBean.toString());
				this.getDaoSupport().updateByNativeSql(sqlQueryBean);
				this.getDaoSupport().flush();
			}
		} catch (Exception e) {
			LOGGER.error("deleteIgnoreItem()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO#listByDtid(java.lang.String)
	 */
	@Override
	public List<SrmCaseAssetLinkDTO> listByDtid(String dtid, String caseInfoTableName, String caseLinkTableName)
			throws DataAccessException {
		List<SrmCaseAssetLinkDTO> SrmCaseAssetLinkDTOS = null;
		try{
			LOGGER.debug(this.getClass().getName() + ".listByCaseId()", "parameters:dtid=" + dtid);
			LOGGER.debug(this.getClass().getName() + ".listByCaseId()", "parameters:caseInfoTableName=" + caseInfoTableName);
			LOGGER.debug(this.getClass().getName() + ".listByCaseId()", "parameters:caseLinkTableName=" + caseLinkTableName);
			SqlQueryBean sql = new SqlQueryBean();
			String schema = this.getMySchema();
			sql.append(" select ");
			sql.append(" caseLink.ITEM_TYPE as itemType , ");
			sql.append(" caseLink.ITEM_ID as itemId, ");
			sql.append(" caseLink.SERIAL_NUMBER as serialNumber");
			sql.append(" from ").append(schema).append(IAtomsConstants.MARK_NO).append(caseLinkTableName).append(" caseLink ");
			sql.append(" INNER JOIN( ");
			sql.append(" SELECT TOP 1 info.CASE_ID ");
			sql.append(" from ").append(schema).append(IAtomsConstants.MARK_NO).append(caseInfoTableName).append("info");
			sql.append(" WHERE 1 = 1 ");
			if(StringUtils.hasText(dtid)){
				sql.append(" AND info.DTID = :dtid ");
				sql.setParameter(SrmCaseAssetLinkDTO.ATTRIBUTE.DTID.getValue(), dtid);
				LOGGER.debug(this.getClass().getName(), ".listByDtid() --> dtid: ", dtid);
			}
			//sql.append(" and (info.CASE_STATUS = :closed or info.CASE_STATUS = 'immediatelyClosing' ) ");
			sql.append(" ORDER BY info.CLOSE_DATE DESC ");
			sql.append(" ) temp ON caseLink.CASE_ID = temp.CASE_ID ");
			sql.append("  and caseLink.IS_LINK = 'Y' ");
			AliasBean alias = new AliasBean(SrmCaseAssetLinkDTO.class);
			//sql.setParameter("closed", IAtomsConstants.CASE_STATUS.CLOSED.getCode());
			alias.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_TYPE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			SrmCaseAssetLinkDTOS = this.getDaoSupport().findByNativeSql(sql, alias);
			LOGGER.debug(this.getClass().getName(), "listBy() --> sql: ", sql.toString()); 
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+":listByCaseId() is error." + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return SrmCaseAssetLinkDTOS;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO#listByCaseId(java.lang.String)
	 */
	@Override
	public List<SrmCaseAssetLinkDTO> listByCaseId(String caseId) throws DataAccessException {
		  List<SrmCaseAssetLinkDTO> SrmCaseAssetLinkDTOS = null;
		  try{
		   LOGGER.debug(this.getClass().getName() + ".listByCaseId()", "parameters:caseId=" + caseId);
		   SqlQueryBean sql = new SqlQueryBean();
		   String schema = this.getMySchema();
		  
		   sql.append(" select ");
		   sql.append(" caseLink.ITEM_TYPE as itemType , ");
		   sql.append(" caseLink.ITEM_ID as itemId, ");
		   sql.append(" caseLink.SERIAL_NUMBER as serialNumber");
		   sql.append(" from ").append(schema).append(IAtomsConstants.MARK_NO).append("SRM_CASE_ASSET_LINK ").append(" caseLink ");
		   sql.append(" WHERE 1 = 1 ");
		   if(StringUtils.hasText(caseId)){
		    sql.append(" AND (caseLink.SERIAL_NUMBER in (SELECT link.SERIAL_NUMBER FROM ").append(schema).append(IAtomsConstants.MARK_NO).append("SRM_CASE_ASSET_LINK link WHERE link.IS_LINK='D' AND link.CASE_ID =:caseId AND link.action is not null) ");
		    sql.append(" OR  caseLink.ITEM_TYPE not in (SELECT link.ITEM_TYPE FROM ").append(schema).append(".SRM_CASE_ASSET_LINK link WHERE link.IS_LINK='D' AND link.CASE_ID =:caseId AND link.action is not null)) ");
		    sql.append(" AND caseLink.CASE_ID = :caseId ");
		    sql.setParameter(SrmCaseAssetLinkDTO.ATTRIBUTE.CASE_ID.getValue(), caseId);
		    LOGGER.debug(this.getClass().getName(), ".listByCaseId() --> caseId: ", caseId);
		   }
		   AliasBean alias = new AliasBean(SrmCaseAssetLinkDTO.class);
		   alias.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_TYPE.getValue(), StringType.INSTANCE);
		   alias.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_ID.getValue(), StringType.INSTANCE);
		   alias.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
		   SrmCaseAssetLinkDTOS = this.getDaoSupport().findByNativeSql(sql, alias);
		   LOGGER.debug(this.getClass().getName(), "listByCaseId() --> sql: ", sql.toString()); 
		  } catch (Exception e) {
		   LOGGER.error(this.getClass().getName()+":listByCaseId() is error." + e);
		   throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		  }
		  return SrmCaseAssetLinkDTOS;
		 }
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO#isWaitClose(java.lang.String)
	 */
	@Override
	public Boolean isWaitClose(String caseId) throws DataAccessException {
		try {
			LOGGER.debug(".isWaitClose()", "parameters:caseId=", caseId);
			String schema = this.getMySchema();	
			SqlStatement sqlStatement = new SqlStatement();
			StringBuffer buffer = new StringBuffer();
			sqlStatement.addSelectClause("count(1)");
			buffer.append(schema).append(" .SRM_CASE_TRANSACTION caseTransaction");
			sqlStatement.addFromExpression(buffer.toString());
			//查詢條件
			if (StringUtils.hasText(caseId)) {
				sqlStatement.addWhereClause("CASE_ID = :caseId", caseId);
			}
			sqlStatement.addWhereClause("caseTransaction.CASE_STATUS =:waitClose", IAtomsConstants.CASE_STATUS.WAIT_CLOSE.getCode());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug(".isWaitClose()", "sql:", sqlQueryBean.toString());
			List<Integer> results = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			//如果查到返回false
			if (!CollectionUtils.isEmpty(results)) {
				if (results.get(0).intValue() > 0) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			LOGGER.error(".isWaitClose() is error", "DAO Exception", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO#getCaseAssetLinkByCaseId(java.lang.String)
	 */
	@Override
	public List<SrmCaseAssetLinkDTO> getCaseAssetLinkByCaseId(String caseId, boolean flag)
			throws DataAccessException {
		List<SrmCaseAssetLinkDTO> SrmCaseAssetLinkDTOS = null;
		try{
			LOGGER.debug(this.getClass().getName() + ".getCaseAssetLinkByCaseId()", "parameters:caseId=" + caseId);
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("caseAssetLink.SERIAL_NUMBER", SrmCaseAssetLinkDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("caseAssetLink.ITEM_TYPE", SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_TYPE.getValue());
			sqlStatement.addSelectClause("caseAssetLink.ITEM_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_ID.getValue());
			//CR #2551 2017/12/12
			sqlStatement.addSelectClause("caseAssetLink.CASE_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.CASE_ID.getValue());
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(".SRM_CASE_ASSET_LINK caseAssetLink ");
			sqlStatement.addFromExpression(buffer.toString());
			//查詢條件
			if (StringUtils.hasText(caseId)) {
				sqlStatement.addWhereClause("caseAssetLink.CASE_ID = :caseId", caseId);
			}
			//
			if (flag) {
				sqlStatement.addWhereClause(" caseAssetLink.IS_LINK != 'N' ");
			}
			//sqlStatement.addWhereClause(" caseAssetLink.IS_LINK != 'N' ");
			sqlStatement.setOrderByExpression(" caseAssetLink.ITEM_TYPE ");
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseAssetLinkDTO.class);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug(".getCaseAssetLinkByCaseId()", "sql:", sqlQueryBean.toString());
			SrmCaseAssetLinkDTOS = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+":getCaseAssetLinkByCaseId() is error." + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return SrmCaseAssetLinkDTOS;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO#getCaseAssetLinkInFee(java.util.List, java.util.List, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<SrmCaseAssetLinkDTO> getCaseAssetLinkInFee(List<String> caseStatusArray, 
			String customerId, Date startDate, Date endDate) throws DataAccessException {
		List<SrmCaseAssetLinkDTO> SrmCaseAssetLinkDTOS = null;
		LOGGER.debug("getCaseAssetLinkInFee()", "parameter:customerId=", customerId);
		LOGGER.debug("getCaseAssetLinkInFee()", "parameter:startDate=", startDate.toString());
		LOGGER.debug("getCaseAssetLinkInFee()", "parameter:endDate=", endDate.toString());
		try{
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("select ");
			sqlQueryBean.append(" link.CASE_ID as caseId, ");
			sqlQueryBean.append(" link.ITEM_CATEGORY as itemCategory, ");
			sqlQueryBean.append(" sum(link.PRICE * link.NUMBER) as price , ");
			sqlQueryBean.append(" sum(link.NUMBER) as number ");
			sqlQueryBean.append(" FROM ").append(schema).append(" .SRM_CASE_ASSET_LINK link  ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_HANDLE_INFO info on link.CASE_ID = info.CASE_ID ");
			sqlQueryBean.append(" WHERE ");
			sqlQueryBean.append(" info.COMPLETE_DATE >= :startDate");
			sqlQueryBean.append(" AND info.COMPLETE_DATE < :endDate ");
			sqlQueryBean.append(" AND info.CASE_STATUS IN (:caseStatus) ");
			sqlQueryBean.append(" AND info.CASE_CATEGORY IN('INSTALL','UPDATE') ");
			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND info.CUSTOMER_ID = :customerId ");
				sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
				LOGGER.debug(this.getClass().getName(), ".getCaseAssetLinkInFee() --> customerId: ", customerId);
			}
			sqlQueryBean.append(" AND link.ITEM_TYPE = '20' ");
			sqlQueryBean.append(" AND link.ITEM_CATEGORY  in('ECRLINE','NetworkRoute') ");
			sqlQueryBean.append(" group by link.CASE_ID, link.ITEM_CATEGORY  ");
			sqlQueryBean.append(" order by link.CASE_ID ");
			sqlQueryBean.setParameter("startDate", startDate);
			sqlQueryBean.setParameter("endDate", endDate);
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS.getValue(), caseStatusArray);
			AliasBean aliasBean = new AliasBean(SrmCaseAssetLinkDTO.class);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.CASE_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_CATEGORY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.PRICE.getValue(), BigDecimalType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.NUMBER.getValue(), IntegerType.INSTANCE);
			LOGGER.debug("getCaseAssetLinkInFee()", "sql:", sqlQueryBean.toString());
			SrmCaseAssetLinkDTOS = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("getCaseAssetLinkInFee() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return SrmCaseAssetLinkDTOS;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO#getCaseAssetLinkInGreenWorld(java.util.List, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<SrmCaseAssetLinkDTO> getCaseAssetLinkInGreenWorld(List<String> caseStatusArray, String customerId, Date startDate,
			Date endDate) throws DataAccessException {
		List<SrmCaseAssetLinkDTO> SrmCaseAssetLinkDTOS = null;
		LOGGER.debug("getCaseAssetLinkInFee()", "parameter:customerId=", customerId);
		LOGGER.debug("getCaseAssetLinkInFee()", "parameter:startDate=", startDate.toString());
		LOGGER.debug("getCaseAssetLinkInFee()", "parameter:endDate=", endDate.toString());
		try{
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("select ");
			sqlQueryBean.append(" link.CASE_ID as caseId, ");
			sqlQueryBean.append(" link.ITEM_CATEGORY as itemCategory, ");
			sqlQueryBean.append(" sum(link.PRICE * link.NUMBER) as price , ");
			sqlQueryBean.append(" sum(link.NUMBER) as number ");
			sqlQueryBean.append(" FROM ").append(schema).append(" .SRM_CASE_ASSET_LINK link  ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_HANDLE_INFO info on link.CASE_ID = info.CASE_ID ");
			sqlQueryBean.append(" WHERE ");
			sqlQueryBean.append(" info.COMPLETE_DATE >= :startDate");
			sqlQueryBean.append(" AND info.COMPLETE_DATE < :endDate ");
			sqlQueryBean.append(" AND info.CASE_STATUS IN (:caseStatus) ");
			sqlQueryBean.append(" AND info.CASE_CATEGORY IN('INSTALL','UPDATE') ");
			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND info.CUSTOMER_ID = :customerId ");
				sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
				LOGGER.debug(this.getClass().getName(), ".getCaseAssetLinkInFee() --> customerId: ", customerId);
			}
			sqlQueryBean.append(" AND link.ITEM_TYPE = '20' ");
			sqlQueryBean.append(" AND link.ITEM_CATEGORY  in('ECRLINE','NetworkRoute') ");
			sqlQueryBean.append(" group by link.CASE_ID, link.ITEM_CATEGORY  ");
			sqlQueryBean.append(" order by link.CASE_ID ");
			sqlQueryBean.setParameter("startDate", startDate);
			sqlQueryBean.setParameter("endDate", endDate);
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS.getValue(), caseStatusArray);
			AliasBean aliasBean = new AliasBean(SrmCaseAssetLinkDTO.class);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.CASE_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_CATEGORY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.PRICE.getValue(), BigDecimalType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.NUMBER.getValue(), IntegerType.INSTANCE);
			LOGGER.debug("getCaseAssetLinkInFee()", "sql:", sqlQueryBean.toString());
			SrmCaseAssetLinkDTOS = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("getCaseAssetLinkInFee() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return SrmCaseAssetLinkDTOS;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO#getCaseAssetLinkInTaiXin(java.util.List, java.lang.String, java.util.Date, java.util.Date, java.lang.String)
	 */
	public List<SrmCaseAssetLinkDTO> getCaseAssetLinkInTaiXin(List<String> caseStatusArray, String customerId, Date startDate,
			Date endDate, String TaiXin) throws DataAccessException {
		List<SrmCaseAssetLinkDTO> SrmCaseAssetLinkDTOS = null;
		LOGGER.debug("getCaseAssetLinkInFee()", "parameter:customerId=", customerId);
		LOGGER.debug("getCaseAssetLinkInFee()", "parameter:startDate=", startDate.toString());
		LOGGER.debug("getCaseAssetLinkInFee()", "parameter:endDate=", endDate.toString());
		try{
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("select ");
			sqlQueryBean.append(" link.CASE_ID as caseId, ");
			sqlQueryBean.append(" link.ITEM_CATEGORY as itemCategory, ");
			sqlQueryBean.append(" sum(link.PRICE * link.NUMBER) as price , ");
			sqlQueryBean.append(" sum(link.NUMBER) as number ");
			sqlQueryBean.append(" FROM ").append(schema).append(" .SRM_CASE_ASSET_LINK link  ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_HANDLE_INFO info on link.CASE_ID = info.CASE_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_ASSET_LINK link1 on info.CASE_ID = link1.CASE_ID  and link1.ITEM_TYPE = '10' AND  link1.IS_LINK = 'Y' ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .DMM_REPOSITORY repo ON	link1.SERIAL_NUMBER = repo.SERIAL_NUMBER ");
			sqlQueryBean.append(" WHERE ");
			sqlQueryBean.append(" info.COMPLETE_DATE >= :startDate");
			sqlQueryBean.append(" AND info.COMPLETE_DATE < :endDate ");
			sqlQueryBean.append(" AND info.CASE_STATUS IN (:caseStatus) ");
			sqlQueryBean.append(" AND info.CASE_CATEGORY IN('INSTALL','UPDATE') ");
			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND info.CUSTOMER_ID = :customerId ");
				sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
				LOGGER.debug(this.getClass().getName(), ".getCaseAssetLinkInFee() --> customerId: ", customerId);
			}
			if(StringUtils.hasText(TaiXin)){
				sqlQueryBean.append(" AND repo.ASSET_OWNER = :taixin ");
				sqlQueryBean.setParameter("taixin", TaiXin);
				LOGGER.debug(this.getClass().getName(), ".getCaseAssetLinkInFee() --> customerId: ", TaiXin);
			}
			sqlQueryBean.append(" AND link.ITEM_TYPE = '20' ");
			sqlQueryBean.append(" AND link.ITEM_CATEGORY  in('ECRLINE','NetworkRoute') ");
			sqlQueryBean.append(" group by link.CASE_ID, link.ITEM_CATEGORY  ");
			sqlQueryBean.append(" order by link.CASE_ID ");
			sqlQueryBean.setParameter("startDate", startDate);
			sqlQueryBean.setParameter("endDate", endDate);
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS.getValue(), caseStatusArray);
			AliasBean aliasBean = new AliasBean(SrmCaseAssetLinkDTO.class);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.CASE_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_CATEGORY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.PRICE.getValue(), BigDecimalType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.NUMBER.getValue(), IntegerType.INSTANCE);
			LOGGER.debug("getCaseAssetLinkInFee()", "sql:", sqlQueryBean.toString());
			SrmCaseAssetLinkDTOS = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("getCaseAssetLinkInFee() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return SrmCaseAssetLinkDTOS;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO#getCaseLinkSuppliesInGreenWorld(java.util.List, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<SrmCaseAssetLinkDTO> getCaseLinkSuppliesInGreenWorld(List<String> caseStatusArray, String customerId, Date startDate,
			Date endDate) throws DataAccessException {
		List<SrmCaseAssetLinkDTO> SrmCaseAssetLinkDTOS = null;
		LOGGER.debug("getCaseLinkSuppliesInGreenWorld()", "parameter:customerId=", customerId);
		LOGGER.debug("getCaseLinkSuppliesInGreenWorld()", "parameter:startDate=", startDate.toString());
		LOGGER.debug("getCaseLinkSuppliesInGreenWorld()", "parameter:endDate=", endDate.toString());
		try{
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("select ");
			sqlQueryBean.append(" link.ITEM_CATEGORY as itemCategory, ");
			sqlQueryBean.append(" link.PRICE as price, ");
			sqlQueryBean.append(" sum(link.NUMBER) as number ");
			sqlQueryBean.append(" FROM ").append(schema).append(" .SRM_CASE_ASSET_LINK link  ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_HANDLE_INFO info on link.CASE_ID = info.CASE_ID ");
			sqlQueryBean.append(" WHERE ");
			sqlQueryBean.append(" info.COMPLETE_DATE >= :startDate");
			sqlQueryBean.append(" AND info.COMPLETE_DATE < :endDate ");
			sqlQueryBean.append(" AND info.CASE_STATUS IN (:caseStatus) ");
			sqlQueryBean.append(" AND link.ITEM_CATEGORY  in('ECRLINE','NetworkRoute') ");
			sqlQueryBean.append(" AND info.CASE_CATEGORY IN('INSTALL','UPDATE') ");
			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND info.CUSTOMER_ID = :customerId ");
				sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
				LOGGER.debug(this.getClass().getName(), ".getCaseLinkSuppliesInGreenWorld() --> customerId: ", customerId);
			}
			sqlQueryBean.append(" AND link.ITEM_TYPE = '20' ");
			sqlQueryBean.append(" group by link.ITEM_CATEGORY, link.PRICE ");
			sqlQueryBean.append(" order by link.ITEM_CATEGORY ");
			sqlQueryBean.setParameter("startDate", startDate);
			sqlQueryBean.setParameter("endDate", endDate);
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS.getValue(), caseStatusArray);
			AliasBean aliasBean = new AliasBean(SrmCaseAssetLinkDTO.class);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_CATEGORY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.PRICE.getValue(), BigDecimalType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.NUMBER.getValue(), IntegerType.INSTANCE);
			LOGGER.debug("getCaseLinkSuppliesInGreenWorld()", "sql:", sqlQueryBean.toString());
			SrmCaseAssetLinkDTOS = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("getCaseLinkSuppliesInGreenWorld() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return SrmCaseAssetLinkDTOS;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO#getCaseLinkSuppliesInTaiXin(java.util.List, java.lang.String, java.util.Date, java.util.Date, java.lang.String)
	 */
	public List<SrmCaseAssetLinkDTO> getCaseLinkSuppliesInTaiXin(List<String> caseStatusArray, String customerId, Date startDate,
			Date endDate, String TaiXin) throws DataAccessException {
		List<SrmCaseAssetLinkDTO> SrmCaseAssetLinkDTOS = null;
		LOGGER.debug("getCaseLinkSuppliesInGreenWorld()", "parameter:customerId=", customerId);
		LOGGER.debug("getCaseLinkSuppliesInGreenWorld()", "parameter:startDate=", startDate.toString());
		LOGGER.debug("getCaseLinkSuppliesInGreenWorld()", "parameter:endDate=", endDate.toString());
		try{
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("select ");
			sqlQueryBean.append(" link.ITEM_CATEGORY as itemCategory, ");
			sqlQueryBean.append(" link.PRICE as price, ");
			sqlQueryBean.append(" sum(link.NUMBER) as number ");
			sqlQueryBean.append(" FROM ").append(schema).append(" .SRM_CASE_ASSET_LINK link  ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_HANDLE_INFO info on link.CASE_ID = info.CASE_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_ASSET_LINK link1 on info.CASE_ID = link1.CASE_ID  and link1.ITEM_TYPE = '10' AND  link1.IS_LINK = 'Y'");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .DMM_REPOSITORY repo ON	link1.SERIAL_NUMBER = repo.SERIAL_NUMBER ");
			sqlQueryBean.append(" WHERE ");
			sqlQueryBean.append(" info.COMPLETE_DATE >= :startDate");
			sqlQueryBean.append(" AND info.COMPLETE_DATE < :endDate ");
			sqlQueryBean.append(" AND info.CASE_STATUS IN (:caseStatus) ");
			sqlQueryBean.append(" AND link.ITEM_CATEGORY  in('ECRLINE','NetworkRoute') ");
			sqlQueryBean.append(" AND info.CASE_CATEGORY IN('INSTALL','UPDATE') ");
			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND info.CUSTOMER_ID = :customerId ");
				sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
				LOGGER.debug(this.getClass().getName(), ".getCaseLinkSuppliesInGreenWorld() --> customerId: ", customerId);
			}
			if(StringUtils.hasText(TaiXin)){
				sqlQueryBean.append(" AND repo.ASSET_OWNER = :taixin ");
				sqlQueryBean.setParameter("taixin", TaiXin);
				LOGGER.debug(this.getClass().getName(), ".getCaseAssetLinkInFee() --> customerId: ", TaiXin);
			}
			sqlQueryBean.append(" AND link.ITEM_TYPE = '20' ");
			sqlQueryBean.append(" group by link.ITEM_CATEGORY, link.PRICE ");
			sqlQueryBean.append(" order by link.ITEM_CATEGORY ");
			sqlQueryBean.setParameter("startDate", startDate);
			sqlQueryBean.setParameter("endDate", endDate);
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS.getValue(), caseStatusArray);
			AliasBean aliasBean = new AliasBean(SrmCaseAssetLinkDTO.class);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_CATEGORY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.PRICE.getValue(), BigDecimalType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.NUMBER.getValue(), IntegerType.INSTANCE);
			LOGGER.debug("getCaseLinkSuppliesInGreenWorld()", "sql:", sqlQueryBean.toString());
			SrmCaseAssetLinkDTOS = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("getCaseLinkSuppliesInGreenWorld() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return SrmCaseAssetLinkDTOS;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO#listSuppliesTypeForGp(java.util.List, java.lang.String, java.util.Date, java.util.Date, java.util.List)
	 */
	@Override
	public List<SrmCaseAssetLinkDTO> listSuppliesTypeForGp(List<String> caseStatus, String customerId, Date startDate,
			Date endDate, List<String> suppliesTypes)
			throws DataAccessException {
		List<SrmCaseAssetLinkDTO> SrmCaseAssetLinkDTOS = null;
		LOGGER.debug("listSuppliesTypeForGp()", "parameter:customerId=", caseStatus.toString());
		LOGGER.debug("listSuppliesTypeForGp()", "parameter:startDate=", startDate.toString());
		LOGGER.debug("listSuppliesTypeForGp()", "parameter:endDate=", endDate.toString());
		LOGGER.debug("listSuppliesTypeForGp()", "parameter:customerId=", customerId);
		LOGGER.debug("listSuppliesTypeForGp()", "parameter:suppliesTypes=", suppliesTypes.toString());
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("link.CASE_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("link.ITEM_CATEGORY", SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_CATEGORY.getValue());
			sqlStatement.addSelectClause("link.PRICE", SrmCaseAssetLinkDTO.ATTRIBUTE.PRICE.getValue());
			sqlStatement.addSelectClause("sum(link.\"NUMBER\")", "\""+SrmCaseAssetLinkDTO.ATTRIBUTE.NUMBER.getValue()+"\"");
			//sqlStatement.addSelectClause("sum(link.\"NUMBER\" * link.PRICE)", SrmCaseAssetLinkDTO.ATTRIBUTE.TOTAL_PRICE.getValue());
			//sqlStatement.addSelectClause("");
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".SRM_CASE_ASSET_LINK link ");
			fromBuffer.append("left join ").append(schema).append(".SRM_CASE_HANDLE_INFO info on link.CASE_ID = info.CASE_ID ");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (startDate != null) {
				sqlStatement.addWhereClause("info.COMPLETE_DATE >= :startDate", startDate);
			}
			if (endDate != null) {
				sqlStatement.addWhereClause("info.COMPLETE_DATE < :endDate", endDate);
			}
			if (!CollectionUtils.isEmpty(caseStatus)) {
				sqlStatement.addWhereClause("info.CASE_STATUS in ( :caseStatus )");
			}
			if (StringUtils.hasText(customerId)) {
				sqlStatement.addWhereClause("info.CUSTOMER_ID = :customerId", customerId);
			}
			sqlStatement.addWhereClause("link.ITEM_TYPE = :itemType", IAtomsConstants.PROPERTY_ID_LENGTH);
			if (!CollectionUtils.isEmpty(suppliesTypes)) {
				sqlStatement.addWhereClause("link.ITEM_CATEGORY in ( :suppliesTypes )");
			}
			sqlStatement.addWhereClause("link.\"NUMBER\" is not null");
			sqlStatement.addWhereClause("link.PRICE is not null");
			sqlStatement.setGroupByExpression("link.CASE_ID, link.ITEM_CATEGORY,link.PRICE");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if (!CollectionUtils.isEmpty(caseStatus)) {
				sqlQueryBean.setParameter("caseStatus", caseStatus);
			}
			if (!CollectionUtils.isEmpty(suppliesTypes)) {
				sqlQueryBean.setParameter("suppliesTypes", suppliesTypes);
			}
			AliasBean aliasBean = new AliasBean(SrmCaseAssetLinkDTO.class);
			//AliasBean aliasBean = new AliasBean();
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.CASE_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_CATEGORY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.PRICE.getValue(), BigDecimalType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.NUMBER.getValue(), IntegerType.INSTANCE);
			//aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.TOTAL_PRICE.getValue(), BigDecimalType.INSTANCE);
			LOGGER.debug("listSuppliesTypeForGp()", "sql:", sqlQueryBean.toString());
			SrmCaseAssetLinkDTOS = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listSuppliesTypeForGp() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return SrmCaseAssetLinkDTOS;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO#getCaseAssetLinkInScsb(java.util.List, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<SrmCaseAssetLinkDTO> getCaseAssetLinkInScsb(List<String> caseStatusArray, String customerId, 
			Date startDate, Date endDate) throws DataAccessException {
		List<SrmCaseAssetLinkDTO> SrmCaseAssetLinkDTOS = null;
		LOGGER.debug("getCaseAssetLinkInScsb()", "parameter:customerId=", customerId);
		LOGGER.debug("getCaseAssetLinkInScsb()", "parameter:startDate=", startDate.toString());
		LOGGER.debug("getCaseAssetLinkInScsb()", "parameter:endDate=", endDate.toString());
		try{
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("select ");
			sqlQueryBean.append(" link.CASE_ID as caseId, ");
			sqlQueryBean.append(" link.ITEM_CATEGORY as itemCategory, ");
			sqlQueryBean.append(" sum(link.NUMBER * link.PRICE) as price , ");
			sqlQueryBean.append(" sum(link.NUMBER) as number ");
			sqlQueryBean.append(" FROM ").append(schema).append(" .SRM_CASE_ASSET_LINK link  ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_HANDLE_INFO info on link.CASE_ID = info.CASE_ID ");
			sqlQueryBean.append(" WHERE ");
			sqlQueryBean.append(" info.COMPLETE_DATE >= :startDate");
			sqlQueryBean.append(" AND info.COMPLETE_DATE < :endDate ");
			sqlQueryBean.append(" AND info.CASE_STATUS IN (:caseStatus) ");
			sqlQueryBean.append(" AND info.CASE_CATEGORY IN('INSTALL','UPDATE') ");
			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND info.CUSTOMER_ID = :customerId ");
				sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
				LOGGER.debug(this.getClass().getName(), ".getCaseAssetLinkInScsb() --> customerId: ", customerId);
			}
			sqlQueryBean.append(" AND link.ITEM_TYPE = '20' ");
			sqlQueryBean.append(" AND link.ITEM_CATEGORY  in('ECRLINE','NetworkRoute') ");
			sqlQueryBean.append(" group by link.CASE_ID, link.ITEM_CATEGORY  ");
			sqlQueryBean.append(" order by link.CASE_ID ");
			sqlQueryBean.setParameter("startDate", startDate);
			sqlQueryBean.setParameter("endDate", endDate);
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS.getValue(), caseStatusArray);
			AliasBean aliasBean = new AliasBean(SrmCaseAssetLinkDTO.class);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.CASE_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_CATEGORY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.PRICE.getValue(), BigDecimalType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.NUMBER.getValue(), IntegerType.INSTANCE);
			LOGGER.debug("getCaseAssetLinkInScsb()", "sql:", sqlQueryBean.toString());
			SrmCaseAssetLinkDTOS = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("getCaseAssetLinkInFee() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return SrmCaseAssetLinkDTOS;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO#getCaseLinkSuppliesInScsb(java.util.List, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<SrmCaseAssetLinkDTO> getCaseLinkSuppliesInScsb(List<String> caseStatusArray, String customerId, 
			Date startDate, Date endDate) throws DataAccessException {
		List<SrmCaseAssetLinkDTO> SrmCaseAssetLinkDTOS = null;
		LOGGER.debug("getCaseLinkSuppliesInScsb()", "parameter:customerId=", customerId);
		LOGGER.debug("getCaseLinkSuppliesInScsb()", "parameter:startDate=", startDate.toString());
		LOGGER.debug("getCaseLinkSuppliesInScsb()", "parameter:endDate=", endDate.toString());
		try{
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("select ");
			sqlQueryBean.append(" link.ITEM_CATEGORY as itemCategory, ");
			sqlQueryBean.append(" link.PRICE as price, ");
			sqlQueryBean.append(" sum(link.NUMBER) as number ");
			sqlQueryBean.append(" FROM ").append(schema).append(" .SRM_CASE_ASSET_LINK link  ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_HANDLE_INFO info on link.CASE_ID = info.CASE_ID ");
			sqlQueryBean.append(" WHERE ");
			sqlQueryBean.append(" info.COMPLETE_DATE >= :startDate");
			sqlQueryBean.append(" AND info.COMPLETE_DATE < :endDate ");
			sqlQueryBean.append(" AND info.CASE_STATUS IN (:caseStatus) ");
			sqlQueryBean.append(" AND info.CASE_CATEGORY IN('INSTALL','UPDATE') ");
			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND info.CUSTOMER_ID = :customerId ");
				sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
				LOGGER.debug(this.getClass().getName(), ".getCaseLinkSuppliesInScsb() --> customerId: ", customerId);
			}
			sqlQueryBean.append(" AND link.ITEM_TYPE = '20' ");
			sqlQueryBean.append(" group by link.ITEM_CATEGORY, link.PRICE ");
			sqlQueryBean.append(" order by link.ITEM_CATEGORY ");
			sqlQueryBean.setParameter("startDate", startDate);
			sqlQueryBean.setParameter("endDate", endDate);
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS.getValue(), caseStatusArray);
			AliasBean aliasBean = new AliasBean(SrmCaseAssetLinkDTO.class);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_CATEGORY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.PRICE.getValue(), BigDecimalType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.NUMBER.getValue(), IntegerType.INSTANCE);
			LOGGER.debug("getCaseLinkSuppliesInScsb()", "sql:", sqlQueryBean.toString());
			SrmCaseAssetLinkDTOS = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("getCaseLinkSuppliesInScsb() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return SrmCaseAssetLinkDTOS;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO#getCaseLinkAndCaseInfoBySerialNumber(java.lang.String)
	 */
	@Override
	public List<SrmCaseAssetLinkDTO> getCaseLinkAndCaseInfoBySerialNumber(String serialNumber)
			throws DataAccessException {
		LOGGER.debug("getCaseLinkAndCaseInfoBySerialNumber()", "parameter:serialNumber=", serialNumber);
		List<SrmCaseAssetLinkDTO> SrmCaseAssetLinkDTOS = null;
		try{
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("select ");
			sqlQueryBean.append(" top 1 info.case_category as caseCategory, ");
			sqlQueryBean.append(" link.ACTION as action ");
			sqlQueryBean.append(" FROM ").append(schema).append(" .SRM_CASE_HANDLE_INFO info  ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_ASSET_LINK link on info.CASE_ID = link.CASE_ID and link.ITEM_TYPE = '10' ");
			sqlQueryBean.append(" WHERE");
			if(StringUtils.hasText(serialNumber)){
				sqlQueryBean.append(" link.SERIAL_NUMBER = :serialNumber");
				sqlQueryBean.setParameter(SrmCaseAssetLinkDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), serialNumber);
				LOGGER.debug(this.getClass().getName(), ".getCaseLinkAndCaseInfoBySerialNumber() --> serialNumber: ", serialNumber);
			}
			sqlQueryBean.append(" ORDER BY info.COMPLETE_DATE desc");
			AliasBean aliasBean = new AliasBean(SrmCaseAssetLinkDTO.class);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.CASE_CATEGORY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.ACTION.getValue(), StringType.INSTANCE);
			LOGGER.debug("getCaseLinkAndCaseInfoBySerialNumber()", "sql:", sqlQueryBean.toString());
			SrmCaseAssetLinkDTOS = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("getCaseLinkAndCaseInfoBySerialNumber() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return SrmCaseAssetLinkDTOS;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO#getCaseAssetLinkInSyb(java.util.List, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<SrmCaseAssetLinkDTO> getCaseAssetLinkInSyb( List<String> caseStatusArray, String customerId, 
			Date startDate, Date endDate) throws DataAccessException {
		List<SrmCaseAssetLinkDTO> SrmCaseAssetLinkDTOS = null;
		LOGGER.debug("getCaseAssetLinkInSyb()", "parameter:customerId=", customerId);
		LOGGER.debug("getCaseAssetLinkInSyb()", "parameter:startDate=", startDate.toString());
		LOGGER.debug("getCaseAssetLinkInSyb()", "parameter:endDate=", endDate.toString());
		try{
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("select ");
			sqlQueryBean.append(" link.CASE_ID as caseId, ");
			sqlQueryBean.append(" link.ITEM_CATEGORY as itemCategory, ");
			sqlQueryBean.append(" sum(link.PRICE * link.NUMBER) as price , ");
			sqlQueryBean.append(" sum(link.NUMBER) as number ");
			sqlQueryBean.append(" FROM ").append(schema).append(" .SRM_CASE_ASSET_LINK link  ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_HANDLE_INFO info on link.CASE_ID = info.CASE_ID ");
			sqlQueryBean.append(" WHERE ");
			sqlQueryBean.append(" info.COMPLETE_DATE >= :startDate");
			sqlQueryBean.append(" AND info.COMPLETE_DATE < :endDate ");
			sqlQueryBean.append(" AND info.CASE_STATUS IN (:caseStatus) ");
			sqlQueryBean.append(" AND info.CASE_CATEGORY IN('INSTALL','UPDATE') ");
			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND info.CUSTOMER_ID = :customerId ");
				sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
				LOGGER.debug(this.getClass().getName(), ".getCaseAssetLinkInSyb() --> customerId: ", customerId);
			}
			sqlQueryBean.append(" AND link.ITEM_TYPE = '20' ");
			sqlQueryBean.append(" AND link.ITEM_CATEGORY  in('ECRLINE','NetworkRoute') ");
			sqlQueryBean.append(" group by link.CASE_ID, link.ITEM_CATEGORY  ");
			sqlQueryBean.append(" order by link.CASE_ID ");
			sqlQueryBean.setParameter("startDate", startDate);
			sqlQueryBean.setParameter("endDate", endDate);
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS.getValue(), caseStatusArray);
			AliasBean aliasBean = new AliasBean(SrmCaseAssetLinkDTO.class);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.CASE_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_CATEGORY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.PRICE.getValue(), BigDecimalType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.NUMBER.getValue(), IntegerType.INSTANCE);
			LOGGER.debug("getCaseAssetLinkInSyb()", "sql:", sqlQueryBean.toString());
			SrmCaseAssetLinkDTOS = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("getCaseAssetLinkInSyb() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return SrmCaseAssetLinkDTOS;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO#getCaseLinkSuppliesInSyb(java.util.List, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<SrmCaseAssetLinkDTO> getCaseLinkSuppliesInSyb(List<String> caseStatusArray, String customerId, 
			Date startDate, Date endDate) throws DataAccessException {
		List<SrmCaseAssetLinkDTO> SrmCaseAssetLinkDTOS = null;
		LOGGER.debug("getCaseLinkSuppliesInSyb()", "parameter:customerId=", customerId);
		LOGGER.debug("getCaseLinkSuppliesInSyb()", "parameter:startDate=", startDate.toString());
		LOGGER.debug("getCaseLinkSuppliesInSyb()", "parameter:endDate=", endDate.toString());
		try{
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("select ");
			sqlQueryBean.append(" link.ITEM_CATEGORY as itemCategory, ");
			sqlQueryBean.append(" link.PRICE as price, ");
			sqlQueryBean.append(" sum(link.NUMBER) as number ");
			sqlQueryBean.append(" FROM ").append(schema).append(" .SRM_CASE_ASSET_LINK link  ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_HANDLE_INFO info on link.CASE_ID = info.CASE_ID ");
			sqlQueryBean.append(" WHERE ");
			sqlQueryBean.append(" info.COMPLETE_DATE >= :startDate");
			sqlQueryBean.append("  and info.COMPLETE_DATE < :endDate ");
			sqlQueryBean.append(" AND info.CASE_STATUS IN (:caseStatus) ");
			sqlQueryBean.append(" AND info.CASE_CATEGORY IN('INSTALL','UPDATE') ");
			sqlQueryBean.append(" AND link.ITEM_CATEGORY  in('ECRLINE','NetworkRoute') ");
			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND info.CUSTOMER_ID = :customerId ");
				sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
				LOGGER.debug(this.getClass().getName(), ".getCaseLinkSuppliesInSyb() --> customerId: ", customerId);
			}
			sqlQueryBean.append(" AND link.ITEM_TYPE = '20' ");
			sqlQueryBean.append(" group by link.ITEM_CATEGORY, link.PRICE ");
			sqlQueryBean.append(" order by link.ITEM_CATEGORY ");
			sqlQueryBean.setParameter("startDate", startDate);
			sqlQueryBean.setParameter("endDate", endDate);
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS.getValue(), caseStatusArray);
			AliasBean aliasBean = new AliasBean(SrmCaseAssetLinkDTO.class);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_CATEGORY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.PRICE.getValue(), BigDecimalType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.NUMBER.getValue(), IntegerType.INSTANCE);
			LOGGER.debug("getCaseLinkSuppliesInSyb()", "sql:", sqlQueryBean.toString());
			SrmCaseAssetLinkDTOS = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("getCaseLinkSuppliesInSyb() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return SrmCaseAssetLinkDTOS;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO#getCaseAssetLinkInChb(java.util.List, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<SrmCaseAssetLinkDTO> getCaseAssetLinkInChb(List<String> caseStatusArray, String customerId, Date startDate,
			Date endDate) throws DataAccessException {
		List<SrmCaseAssetLinkDTO> SrmCaseAssetLinkDTOS = null;
		LOGGER.debug("getCaseAssetLinkInChb()", "parameter:customerId=", customerId);
		LOGGER.debug("getCaseAssetLinkInChb()", "parameter:startDate=", startDate.toString());
		LOGGER.debug("getCaseAssetLinkInChb()", "parameter:endDate=", endDate.toString());
		try{
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("select ");
			sqlQueryBean.append(" link.CASE_ID as caseId, ");
			sqlQueryBean.append(" link.ITEM_CATEGORY as itemCategory, ");
			sqlQueryBean.append(" sum(link.PRICE * link.NUMBER) as price , ");
			sqlQueryBean.append(" sum(link.NUMBER) as number ");
			sqlQueryBean.append(" FROM ").append(schema).append(" .SRM_CASE_ASSET_LINK link  ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_HANDLE_INFO info on link.CASE_ID = info.CASE_ID ");
			sqlQueryBean.append(" WHERE ");
			sqlQueryBean.append(" info.COMPLETE_DATE >= :startDate");
			sqlQueryBean.append("  and info.COMPLETE_DATE < :endDate ");
			sqlQueryBean.append(" AND info.CASE_STATUS IN (:caseStatus) ");
			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND info.CUSTOMER_ID = :customerId ");
				sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
				LOGGER.debug(this.getClass().getName(), ".getCaseAssetLinkInChb() --> customerId: ", customerId);
			}
			sqlQueryBean.append(" AND link.ITEM_TYPE = '20' ");
			sqlQueryBean.append(" AND link.ITEM_CATEGORY  in('ECRLINE','NetworkRoute') ");
			sqlQueryBean.append(" group by link.CASE_ID, link.ITEM_CATEGORY  ");
			sqlQueryBean.append(" order by link.CASE_ID ");
			sqlQueryBean.setParameter("startDate", startDate);
			sqlQueryBean.setParameter("endDate", endDate);
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS.getValue(), caseStatusArray);
			AliasBean aliasBean = new AliasBean(SrmCaseAssetLinkDTO.class);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.CASE_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_CATEGORY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.PRICE.getValue(), BigDecimalType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.NUMBER.getValue(), IntegerType.INSTANCE);
			LOGGER.debug("getCaseAssetLinkInChb()", "sql:", sqlQueryBean.toString());
			SrmCaseAssetLinkDTOS = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("getCaseAssetLinkInChb() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return SrmCaseAssetLinkDTOS;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO#getCaseLinkSuppliesInChb(java.util.List, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<SrmCaseAssetLinkDTO> getCaseLinkSuppliesInChb(List<String> caseStatusArray, String customerId, 
			Date startDate, Date endDate) throws DataAccessException {
		List<SrmCaseAssetLinkDTO> SrmCaseAssetLinkDTOS = null;
		LOGGER.debug("getCaseLinkSuppliesInChb()", "parameter:customerId=", customerId);
		LOGGER.debug("getCaseLinkSuppliesInChb()", "parameter:startDate=", startDate.toString());
		LOGGER.debug("getCaseLinkSuppliesInChb()", "parameter:endDate=", endDate.toString());
		try{
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("select ");
			sqlQueryBean.append(" link.ITEM_CATEGORY as itemCategory, ");
			sqlQueryBean.append(" link.PRICE as price, ");
			sqlQueryBean.append(" sum(link.NUMBER) as number ");
			sqlQueryBean.append(" FROM ").append(schema).append(" .SRM_CASE_ASSET_LINK link  ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_HANDLE_INFO info on link.CASE_ID = info.CASE_ID ");
			sqlQueryBean.append(" WHERE ");
			sqlQueryBean.append(" info.COMPLETE_DATE >= :startDate");
			sqlQueryBean.append(" and info.COMPLETE_DATE < :endDate ");
			sqlQueryBean.append(" AND info.CASE_STATUS IN (:caseStatus) ");
			//sqlQueryBean.append(" AND info.CASE_CATEGORY IN('INSTALL','UPDATE') ");
			sqlQueryBean.append(" AND ((info.CASE_CATEGORY = 'INSTALL' and info.INSTALL_TYPE in('1', '2') ) or info.CASE_CATEGORY = 'UPDATE') ");
			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND info.CUSTOMER_ID = :customerId ");
				sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
				LOGGER.debug(this.getClass().getName(), ".getCaseLinkSuppliesInChb() --> customerId: ", customerId);
			}
			sqlQueryBean.append(" AND link.ITEM_TYPE = '20' ");
			sqlQueryBean.append(" AND link.ITEM_CATEGORY  in('ECRLINE','NetworkRoute') ");
			sqlQueryBean.append(" group by link.ITEM_CATEGORY, link.PRICE ");
			sqlQueryBean.append(" order by link.ITEM_CATEGORY ");
			sqlQueryBean.setParameter("startDate", startDate);
			sqlQueryBean.setParameter("endDate", endDate);
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS.getValue(), caseStatusArray);
			AliasBean aliasBean = new AliasBean(SrmCaseAssetLinkDTO.class);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_CATEGORY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.PRICE.getValue(), BigDecimalType.INSTANCE);
			aliasBean.addScalar(SrmCaseAssetLinkDTO.ATTRIBUTE.NUMBER.getValue(), IntegerType.INSTANCE);
			LOGGER.debug("getCaseLinkSuppliesInChb()", "sql:", sqlQueryBean.toString());
			SrmCaseAssetLinkDTOS = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("getCaseLinkSuppliesInChb() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return SrmCaseAssetLinkDTOS;
	}

}
