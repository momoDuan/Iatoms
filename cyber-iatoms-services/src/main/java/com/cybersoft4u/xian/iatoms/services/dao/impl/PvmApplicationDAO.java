package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.util.CollectionUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Parameter;
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

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ApplicationDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IPvmApplicationDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.PvmApplication;

/**
 * Purpose:程式版本維護DAO
 * @author echomou
 * @since  JDK 1.6
 * @date   2016-7-14
 * @MaintenancePersonnel echomou
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class PvmApplicationDAO extends GenericBaseDAO<PvmApplication> implements IPvmApplicationDAO {

	/**
	 * 日志記錄物件
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.DAO, PvmApplicationDAO.class);
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IPvmApplicationDAO#listByApplication(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	@Override
	public List<ApplicationDTO> listByApplication(String companyId, String name, String version, String assetTypeId, String sort, String order, Integer rows, Integer page, String applicationId)throws DataAccessException {
		LOGGER.debug(this.getClass().getName() + "companyId" + companyId);
		LOGGER.debug(this.getClass().getName() + "name" + name);
		LOGGER.debug(this.getClass().getName() + "version" + version);
		LOGGER.debug(this.getClass().getName() + "assetTypeId" + assetTypeId);
		LOGGER.debug(this.getClass().getName() + "applicationId" + applicationId);
		LOGGER.debug(this.getClass().getName() + "sort" + sort);
		LOGGER.debug(this.getClass().getName() + "order" + order);
		LOGGER.debug(this.getClass().getName() + "rows" + rows);
		LOGGER.debug(this.getClass().getName() + "page" + page);
		List<ApplicationDTO> applicationDTOList = null;
		SqlQueryBean sqlQueryBean = new SqlQueryBean();
		String schema = this.getMySchema();
		try {
			sqlQueryBean.append("SELECT");
			sqlQueryBean.append("APP.APPLICATION_ID AS applicationId ,");
			sqlQueryBean.append("APP.NAME AS name, ");
			sqlQueryBean.append("APP.VERSION AS version, ");
			sqlQueryBean.append("APP.CUSTOMER_ID AS customerId ,");
			sqlQueryBean.append("SUBSTRING(APP.assetTypeName,0,LEN(APP.assetTypeName)) AS assetTypeName,");
			sqlQueryBean.append("SUBSTRING(APP.assetTypeId,0,LEN(APP.assetTypeId)) AS assetTypeId,");
			sqlQueryBean.append("APP.ASSET_CATEGORY AS assetCategory ,");
			sqlQueryBean.append("APP.SHORT_NAME AS customerName ,");
			sqlQueryBean.append("APP.COMMENT AS comment ,");
			sqlQueryBean.append("APP.UPDATED_BY_NAME AS updatedByName ,");
			sqlQueryBean.append("CONVERT(varchar(100), APP.UPDATED_DATE, 120) AS updatedDate");
			sqlQueryBean.append("FROM");
			sqlQueryBean.append("(SELECT DISTINCT ap.APPLICATION_ID, ap.NAME,ap.VERSION, ap.CUSTOMER_ID,ap.ASSET_CATEGORY,");
			sqlQueryBean.append("(SELECT AT.NAME + ',' FROM dbo.PVM_APPLICATION_ASSET_LINK aal LEFT JOIN dbo.DMM_ASSET_TYPE AT");
			sqlQueryBean.append("ON at.ASSET_TYPE_ID = aal.ASSET_TYPE_ID");
			sqlQueryBean.append("WHERE aal.APPLICATION_ID = ap.APPLICATION_ID ");
			sqlQueryBean.append("FOR XML PATH('')");
			sqlQueryBean.append(") AS assetTypeName ,");
			sqlQueryBean.append("( SELECT aal.ASSET_TYPE_ID + ',' FROM dbo.PVM_APPLICATION_ASSET_LINK aal LEFT JOIN dbo.DMM_ASSET_TYPE AT");
			sqlQueryBean.append("ON at.ASSET_TYPE_ID = aal.ASSET_TYPE_ID");
			sqlQueryBean.append("WHERE aal.APPLICATION_ID = ap.APPLICATION_ID");
			sqlQueryBean.append("FOR XML PATH('')");
			sqlQueryBean.append(" ) AS assetTypeId ,");
			sqlQueryBean.append(" c.SHORT_NAME,ap.COMMENT,ap.UPDATED_BY_NAME,ap.UPDATED_DATE");
			sqlQueryBean.append(" FROM " + schema + ".PVM_APPLICATION ap LEFT JOIN dbo.BIM_COMPANY c ");
			sqlQueryBean.append(" ON ap.CUSTOMER_ID = c.COMPANY_ID ");
			sqlQueryBean.append("LEFT JOIN dbo.PVM_APPLICATION_ASSET_LINK paal ON paal.APPLICATION_ID = ap.APPLICATION_ID");
			if (StringUtils.hasText(applicationId)) {
				sqlQueryBean.append(" where ap.APPLICATION_ID =:applicationId ");
				sqlQueryBean.setParameter("applicationId", applicationId);
			} else {
				sqlQueryBean.append("where 1=1");
				if (StringUtils.hasText(companyId)) {
					sqlQueryBean.append("and ap.CUSTOMER_ID =:companyId");
					sqlQueryBean.setParameter("companyId", companyId);
				}
				if(StringUtils.hasText(name)){
					sqlQueryBean.append("and ap.NAME like :name");
					sqlQueryBean.setParameter("name", name + IAtomsConstants.MARK_PERCENT);
				}
				if(StringUtils.hasText(version)){
					sqlQueryBean.append("and ap.VERSION like :version");
					sqlQueryBean.setParameter("version", version + IAtomsConstants.MARK_PERCENT);
				}
				if(StringUtils.hasText(assetTypeId)){
					sqlQueryBean.append("and paal.ASSET_TYPE_ID in " + IAtomsConstants.MARK_BRACKETS_LEFT + assetTypeId + IAtomsConstants.MARK_BRACKETS_RIGHT);
				}
			}
			//增加邏輯刪除--update by 2017-07-18
			sqlQueryBean.append(" and ISNULL(ap.DELETED, 'N') =:deleted");
			sqlQueryBean.setParameter("deleted", IAtomsConstants.NO);
			sqlQueryBean.append(") APP");
			if(StringUtils.hasText(assetTypeId)){
				sqlQueryBean.append(" where assetTypeId is not null ");
			}
			
			//設置排序表達式
			if (!StringUtils.hasText(sort) && !StringUtils.hasText(order)) {
				sqlQueryBean.append(" order by APP.SHORT_NAME,APP.VERSION ASC");
			} else if (StringUtils.hasText(sort) && StringUtils.hasText(order)) {
				sqlQueryBean.append(" order by " + sort + IAtomsConstants.MARK_SPACE + order);
			}
			LOGGER.debug("SQL---------->" + sqlQueryBean.toString());
			if(rows != null && page != null){
				sqlQueryBean.setPageSize(rows.intValue());
				sqlQueryBean.setStartPage(page.intValue() - 1);
			}
			AliasBean aliasBean = new AliasBean(ApplicationDTO.class);
			aliasBean.addScalar(ApplicationDTO.ATTRIBUTE.APPLICATION_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(ApplicationDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(ApplicationDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(ApplicationDTO.ATTRIBUTE.ASSET_TYPE_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(ApplicationDTO.ATTRIBUTE.NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(ApplicationDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(ApplicationDTO.ATTRIBUTE.CUSTOMER_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(ApplicationDTO.ATTRIBUTE.VERSION.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(ApplicationDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(ApplicationDTO.ATTRIBUTE.UPDATED_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(ApplicationDTO.ATTRIBUTE.COMMENT.getValue(), StringType.INSTANCE);
			applicationDTOList = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".listByApplication() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_APPLICATION)}, e);
		}
		return applicationDTOList;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IPvmApplicationDAO#count(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public Integer count(String companyId, String name, String version, String assetTypeId)throws DataAccessException{
		LOGGER.debug(this.getClass().getName() + "companyId" + companyId);
		LOGGER.debug(this.getClass().getName() + "name" + name);
		LOGGER.debug(this.getClass().getName() + "version" + version);
		LOGGER.debug(this.getClass().getName() + "assetTypeId" + assetTypeId);
		SqlQueryBean sqlQueryBean = new SqlQueryBean();
		String schema = this.getMySchema();
		try {
			sqlQueryBean.append("SELECT");
			sqlQueryBean.append("count(*)");
			sqlQueryBean.append(" FROM");
			sqlQueryBean.append(" (SELECT  ap.APPLICATION_ID, ap.NAME,ap.VERSION, ap.CUSTOMER_ID,ap.ASSET_CATEGORY,");
			sqlQueryBean.append(" ( SELECT AT.NAME + ',' FROM dbo.PVM_APPLICATION_ASSET_LINK aal LEFT JOIN dbo.DMM_ASSET_TYPE AT");
			sqlQueryBean.append(" ON at.ASSET_TYPE_ID = aal.ASSET_TYPE_ID");
			sqlQueryBean.append("  WHERE  aal.APPLICATION_ID = ap.APPLICATION_ID ");
			if(StringUtils.hasText(assetTypeId)){
				sqlQueryBean.append("and aal.ASSET_TYPE_ID in " + "(" + assetTypeId + ")");
			}
			sqlQueryBean.append("FOR XML PATH('')");
			sqlQueryBean.append(" ) AS assetTypeName ,");
			sqlQueryBean.append("  ( SELECT aal.ASSET_TYPE_ID + ',' FROM dbo.PVM_APPLICATION_ASSET_LINK aal LEFT JOIN dbo.DMM_ASSET_TYPE AT");
			sqlQueryBean.append(" ON at.ASSET_TYPE_ID = aal.ASSET_TYPE_ID");
			sqlQueryBean.append(" WHERE aal.APPLICATION_ID = ap.APPLICATION_ID");
			if(StringUtils.hasText(assetTypeId)){
				sqlQueryBean.append("and aal.ASSET_TYPE_ID in " + "(" + assetTypeId + ")");
			}
			sqlQueryBean.append("FOR XML PATH('')");
			sqlQueryBean.append(" ) AS assetTypeId ,");
			sqlQueryBean.append(" c.SHORT_NAME,ap.COMMENT,ap.UPDATED_BY_NAME,ap.UPDATED_DATE");
			sqlQueryBean.append(" FROM " + schema + ".PVM_APPLICATION ap LEFT JOIN dbo.BIM_COMPANY c ");
			sqlQueryBean.append(" ON ap.CUSTOMER_ID = c.COMPANY_ID ");
			sqlQueryBean.append("where 1=1");
			if(StringUtils.hasText(companyId)){
				sqlQueryBean.append("and ap.CUSTOMER_ID =:companyId");
				sqlQueryBean.setParameter("companyId", companyId);
			}
			if(StringUtils.hasText(name)){
				sqlQueryBean.append("and ap.NAME like :name");
				sqlQueryBean.setParameter("name", name + IAtomsConstants.MARK_PERCENT);
			}
			if(StringUtils.hasText(version)){
				sqlQueryBean.append("and ap.VERSION like :version");
				sqlQueryBean.setParameter("version", version + IAtomsConstants.MARK_PERCENT);
			}
			//增加邏輯刪除--update by 2017-07-18
			sqlQueryBean.append(" and ISNULL(ap.DELETED, 'N') =:deleted");
			sqlQueryBean.setParameter("deleted", IAtomsConstants.NO);
			sqlQueryBean.append(") APP");
			if(StringUtils.hasText(assetTypeId)){
				sqlQueryBean.append(" where assetTypeId is not null ");
			}
			LOGGER.debug("SQL---------->" + sqlQueryBean.toString());
			Integer count = (Integer) super.getDaoSupport().findByNativeSql(sqlQueryBean).get(0);
			return count;
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".count() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_APPLICATION)}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IPvmApplicationDAO#deleteApplicationAssetLink(java.lang.String)
	 */
	@Override
	public boolean deleteApplicationAssetLink(String applicationId)throws DataAccessException {
		LOGGER.debug("ApplicationDAO.deleteApplicationAssetLink.applicationId:'" + applicationId + "'");
		if(!StringUtils.hasText(applicationId)) {
			return false;
		}
		SqlQueryBean sqlQueryBean = new SqlQueryBean();
		String schema = this.getMySchema();
		try {
			sqlQueryBean.append("delete from " + schema + ".PVM_APPLICATION_ASSET_LINK");
			if(StringUtils.hasText(applicationId)){
				sqlQueryBean.append("where APPLICATION_ID=:applicationId ");
				sqlQueryBean.setParameter("applicationId", applicationId);
			}
			LOGGER.debug("SQL---------->" + sqlQueryBean.toString());
			int count = this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			if(count > 0){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".deleteApplicationAssetLink() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_APPLICATION)}, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IPvmApplicationDAO#listByAssetType(java.lang.String)
	 */
	public List<Parameter> listByAssetType(String assetCategory)throws DataAccessException{
		LOGGER.debug("ApplicationDAO.listByAssetType.assetCategory:'" + assetCategory + "'");
		List<Parameter> assetTypeList = null;
		SqlQueryBean sqlQueryBean = new SqlQueryBean();
		String schema = this.getMySchema();
		try {
			sqlQueryBean.append("select at.NAME as name,at.ASSET_TYPE_ID as value ");
			sqlQueryBean.append("from " + schema + ".DMM_ASSET_TYPE at ");
			if(StringUtils.hasText(assetCategory)){
				sqlQueryBean.append("where at.ASSET_CATEGORY =:assetCategory ");
				sqlQueryBean.setParameter("assetCategory", assetCategory);
			}
			LOGGER.debug("SQL---------->" + sqlQueryBean.toString());
			AliasBean aliasBean = new AliasBean(Parameter.class);
			assetTypeList = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return assetTypeList;
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".listByAssetType() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_APPLICATION)}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IPvmApplicationDAO#check(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean check(String applicationId, String customerId, String version, String applicationName) {
		LOGGER.debug(this.getClass().getName() + "applicationId" + applicationId);
		LOGGER.debug(this.getClass().getName() + "customerId" + customerId);
		LOGGER.debug(this.getClass().getName() + "version" + version);
		LOGGER.debug(this.getClass().getName() + "applicationName" + applicationName);
		String schema = this.getMySchema();	
		SqlStatement sqlStatement = new SqlStatement();
		try {
			//查詢語句
			sqlStatement.addSelectClause("count(*)");
			sqlStatement.addFromExpression(schema + ".PVM_APPLICATION application");
			if (StringUtils.hasText(customerId)) {
				sqlStatement.addWhereClause("application.CUSTOMER_ID = :customerId", customerId);
			}
			if (StringUtils.hasText(version)) {
				sqlStatement.addWhereClause("application.VERSION = :version", version);
			}
			if (StringUtils.hasText(applicationName)) {
				sqlStatement.addWhereClause("application.NAME = :applicationName", applicationName);
			}
			if (StringUtils.hasText(applicationId)) {
				sqlStatement.addWhereClause("application.APPLICATION_ID <> :applicationId", applicationId);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			//打印sql語句
			LOGGER.debug(this.getClass().getName() + "SQL---------->" + sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (result.get(0).intValue() > 0) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".check() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return false;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IPvmApplicationDAO#listSoftwareVersionsBy(java.lang.String, java.lang.String)
	 */
	public List<Parameter> listSoftwareVersionsBy(String customerId, String edcType, String searchDeletedFlag) throws DataAccessException {
		LOGGER.debug(this.getClass().getName() + "customerId" + customerId);
		LOGGER.debug(this.getClass().getName() + "edcType" + edcType);
		List<Parameter> softwareVersions = null;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
		//	sqlStatement.addSelectClause("DISTINCT application.APPLICATION_ID", Parameter.FIELD_VALUE);
			// Task #2516 軟體版本欄位，預設帶出 最新一筆資料(CREATED_DATE desc)
			sqlStatement.addSelectClause("application.APPLICATION_ID", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("CONCAT( CONVERT( VARCHAR( 100 ) ,application.NAME ,111 ) ,'【' ,CONVERT( VARCHAR( 100 ) ,application.VERSION ,111 ) ,'】')", Parameter.FIELD_NAME);
			sqlStatement.addFromExpression(schema + ".PVM_APPLICATION application"
					+ " left join " + schema + ".PVM_APPLICATION_ASSET_LINK asset on application.APPLICATION_ID = asset.APPLICATION_ID"
					+ " left join " + schema + ".DMM_ASSET_TYPE type on type.ASSET_TYPE_ID = asset.ASSET_TYPE_ID");
			if (StringUtils.hasText(customerId)) {
				sqlStatement.addWhereClause("application.CUSTOMER_ID = :customerId", customerId);
			}
			if (StringUtils.hasText(edcType)) {
				sqlStatement.addWhereClause("asset.ASSET_TYPE_ID = :edcType", edcType);
			}
			//searchDeletedFlag不為Y的時候，查詢未刪除的資料
			if (!IAtomsConstants.YES.equals(searchDeletedFlag)) {
				//增加邏輯刪除--update by 2017-07-18
				sqlStatement.addWhereClause("ISNULL(application.DELETED, 'N') =:deleted", IAtomsConstants.NO);
			}
			// Task #2516 軟體版本欄位，預設帶出 最新一筆資料(CREATED_DATE desc)
			sqlStatement.setGroupByExpression("application.APPLICATION_ID,application.NAME,application.VERSION,application.CREATED_DATE");
			sqlStatement.setOrderByExpression("application.CREATED_DATE desc");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			//打印sql語句
			LOGGER.debug(this.getClass().getName() + "SQL---------->" + sqlQueryBean.toString());
			softwareVersions = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".listSoftwareVersionsBy() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return softwareVersions;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IPvmApplicationDAO#isNoData()
	 */
	@Override
	public boolean isNoData() throws DataAccessException {
		boolean isNoData = false;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("COUNT(1)");
			sqlStatement.addFromExpression(schema + ".PVM_APPLICATION");
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

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IPvmApplicationDAO#getApplicationList(java.lang.String)
	 */
	@Override
	public List<ApplicationDTO> getApplicationList() throws DataAccessException {
		List<ApplicationDTO> applicationDTOs = null;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("application.APPLICATION_ID", ApplicationDTO.ATTRIBUTE.APPLICATION_ID.getValue());
			sqlStatement.addSelectClause("application.NAME", ApplicationDTO.ATTRIBUTE.NAME.getValue());
			sqlStatement.addSelectClause("application.VERSION", ApplicationDTO.ATTRIBUTE.VERSION.getValue());
			sqlStatement.addSelectClause("application.CUSTOMER_ID", ApplicationDTO.ATTRIBUTE.CUSTOMER_ID.getValue());
			sqlStatement.addSelectClause("application.ASSET_CATEGORY", ApplicationDTO.ATTRIBUTE.ASSET_CATEGORY.getValue());
			sqlStatement.addSelectClause("application.COMMENT", ApplicationDTO.ATTRIBUTE.COMMENT.getValue());
			sqlStatement.addSelectClause("asset.ASSET_TYPE_ID", ApplicationDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			
			sqlStatement.addFromExpression(schema + ".PVM_APPLICATION application"
					+ " left join " + schema + ".PVM_APPLICATION_ASSET_LINK asset on application.APPLICATION_ID = asset.APPLICATION_ID"
					+ " left join " + schema + ".DMM_ASSET_TYPE type on type.ASSET_TYPE_ID = asset.ASSET_TYPE_ID");
			/*if (StringUtils.hasText(edcType)) {
				sqlStatement.addWhereClause("asset.ASSET_TYPE_ID = :edcType", edcType);
			}*/
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(ApplicationDTO.class);
			//打印sql語句
			LOGGER.debug("getApplicationList() SQL---------->" + sqlQueryBean.toString());
			applicationDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(".getApplicationList() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return applicationDTOs;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IPvmApplicationDAO#deleteTransferData()
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
			sqlQueryBean.append(schema).append(".PVM_APPLICATION_ASSET_LINK; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".PVM_APPLICATION; ");
			
			LOGGER.debug("deleteTransferData()", "sql:" + sqlQueryBean.toString());
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("deleteTransferData()", e.getMessage(), e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
	}

	
}
