package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.util.CollectionUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.Condition;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.CoreConstants;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BaseParameterItemDefDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.BaseParameterManagerFormDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IBaseParameterManagerDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BaseParameterItemDef;

/**
 * Purpose: 系統參數維護DAO實現類
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016年6月28日
 * @MaintenancePersonnel CrissZhang
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BaseParameterManagerDAO extends GenericBaseDAO<BaseParameterItemDef> implements IBaseParameterManagerDAO {
	
	/**
	 * 系統日志記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(BaseParameterManagerDAO.class);

	/**
	 * 
	 * Constructor: 無參構造函數
	 */
	public BaseParameterManagerDAO(){
		super();
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IBaseParameterManagerDAO#listParameterTypes()
	 */
	public List<Parameter> listParameterTypes() throws DataAccessException {
		List<Parameter> results = null;
		try{
			// 得到schema
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("bptd.BPTD_CODE", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("(case when bptd.VALUE_SCOPE_OPERATOR2 is null then bptd.PT_NAME else bptd.VALUE_SCOPE_OPERATOR1 + '-' + bptd.PT_NAME end)", Parameter.FIELD_NAME);
			sqlStatement.addFromExpression(schema + ".BASE_PARAMETER_TYPE_DEF bptd");
			sqlStatement.addWhereClause("isnull(bptd.READONLY,'N') = 'N'");
			StringBuffer buffer = new StringBuffer();
			buffer.append("bptd.EFFECTIVE_DATE = ( SELECT MAX(EFFECTIVE_DATE) FROM ");
			buffer.append(schema).append(".BASE_PARAMETER_TYPE_DEF type");
			buffer.append(" WHERE type.BPTD_CODE = bptd.BPTD_CODE)");
			sqlStatement.addWhereClause(buffer.toString());
			// 允許標誌
		//	sqlStatement.addWhereClause(" bptd.APPROVED_FLAG = :approvedFlag", IAtomsConstants.YES);
			// 轉為SqlQueryBean對象
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			// 實例化AliasBean對象
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			LOGGER.debug("listParameterTypes()", "sql:" + sqlQueryBean.toString());
			results = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		}catch(Exception e){
			LOGGER.error("listParameterTypes()", e.getMessage(), e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
		return results;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IBaseParameterManagerDAO#listBy(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	public List<BaseParameterItemDefDTO> listBy(String bptdCode, String itemValue, String itemName, String sortDirection,
			String sortFieldName, Integer currentPage, Integer pageSize) throws DataAccessException {
		LOGGER.debug("listBy()", "parameters:bptdCode=" + bptdCode);
		LOGGER.debug("listBy()", "parameters:itemValue=" + itemValue);
		LOGGER.debug("listBy()", "parameters:itemName=" + itemName);
		LOGGER.debug("listBy()", "parameters:sortDirection=" + sortDirection);
		LOGGER.debug("listBy()", "parameters:sortFieldName=" + sortFieldName);
		LOGGER.debug("listBy()", "parameters:currentPage=" + currentPage);
		LOGGER.debug("listBy()", "parameters:pageSize=" + pageSize);
		List<BaseParameterItemDefDTO> results = null;
		try{
			SqlQueryBean sql = new SqlQueryBean();
			String schema = this.getMySchema();
			//sql.append("SELECT * FROM(");
			sql.append(" SELECT bpid.BPID_ID as bpidId,");
			sql.append(" bpid.EFFECTIVE_DATE as effectiveDate,");
			sql.append(" bpid.BPTD_CODE as bptdCode,");
			sql.append(" (case when bptd.VALUE_SCOPE_OPERATOR2 is null then bptd.PT_NAME else bptd.VALUE_SCOPE_OPERATOR1 + '-' + bptd.PT_NAME end) as ptName,");
			sql.append(" bpid.ITEM_VALUE as itemValue,");
			sql.append(" bpid.ITEM_NAME as itemName,");
			sql.append(" bpid.ITEM_DESC as itemDesc,");
			sql.append(" bpid.ITEM_ORDER as itemOrder,");
			sql.append(" bpid.TEXT_FIELD1 as textField1,");
			sql.append(" bpid.UPDATED_BY_ID as updatedById,");
			sql.append(" bpid.UPDATED_BY_NAME as updatedByName,");
			sql.append(" bpid.UPDATED_DATE as updatedDate,");
			// 去除approvedFlag
//			sql.append(" bpid.APPROVED_FLAG as approvedFlag,");
			sql.append(" bpid.DELETED as deleted,");
			sql.append(" bpid.DELETED_DATE as deletedDate");
			sql.append(" FROM ").append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid");
			sql.append(" INNER JOIN ").append(schema).append(".BASE_PARAMETER_TYPE_DEF bptd");
			sql.append(" ON bpid.BPTD_CODE = bptd.BPTD_CODE");
			// 去除approvedFlag
//			sql.append(" AND bptd.APPROVED_FLAG = :approvedFlag");
//			sql.setParameter(BaseParameterItemDefDTO.ATTRIBUTE.APPROVED_FLAG.getValue(), IAtomsConstants.YES);
			sql.append(" WHERE 1=1");			
			if(StringUtils.hasText(bptdCode)){
				sql.append(" AND bpid.BPTD_CODE = :bptdCode");
				sql.setParameter(BaseParameterItemDefDTO.ATTRIBUTE.BPTD_CODE.getValue(), bptdCode);
			}
			if(StringUtils.hasText(itemValue)){
				sql.append(" AND bpid.ITEM_VALUE like :itemValue");
				sql.setParameter(BaseParameterItemDefDTO.ATTRIBUTE.ITEM_VALUE.getValue(), itemValue + IAtomsConstants.MARK_PERCENT);
			}
			if(StringUtils.hasText(itemName)){
				sql.append(" AND bpid.ITEM_NAME like :itemName");
				sql.setParameter(BaseParameterItemDefDTO.ATTRIBUTE.ITEM_NAME.getValue(), itemName + IAtomsConstants.MARK_PERCENT);
			}
			sql.append(" AND isnull(bptd.READONLY,'N') = 'N' ");
			if(currentPage != null){
				sql.setStartPage(currentPage.intValue());
			}
			if(pageSize != null){
				sql.setPageSize(pageSize.intValue());
			}
			//sql.append(")temp");
			if(StringUtils.hasText(sortDirection) && StringUtils.hasText(sortFieldName)){
			//	sql.append(" ORDER BY " + sortFieldName + " " + sortDirection);
				sql.append(" ORDER BY ").append(sortFieldName).append(" ").append(sortDirection);
			} else {
			//	sql.append(" ORDER BY "+ BaseParameterManagerFormDTO.PARAM_PAGE_SORT);
				sql.append(" ORDER BY ").append(BaseParameterManagerFormDTO.PARAM_PAGE_SORT);
			}
			AliasBean alias = new AliasBean(BaseParameterItemDefDTO.class);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.BPID_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.EFFECTIVE_DATE.getValue(), DateType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.BPTD_CODE.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.PT_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.ITEM_VALUE.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.ITEM_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.ITEM_DESC.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.ITEM_ORDER.getValue(), IntegerType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD1.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.UPDATED_BY_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.UPDATED_DATE.getValue(), TimestampType.INSTANCE);
			// 去除approvedFlag
		//	alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.APPROVED_FLAG.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.DELETED.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.DELETED_DATE.getValue(), TimestampType.INSTANCE);
			results = this.getDaoSupport().findByNativeSql(sql, alias);
			LOGGER.debug("listBy()", "sql:" + sql.toString());
		} catch (Exception e){
			LOGGER.error("listBy()", e.getMessage(), e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
		return results;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IBaseParameterManagerDAO#count(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public int count(String bptdCode, String itemValue, String itemName) throws DataAccessException {
		LOGGER.debug("count()", "parameters:bptdCode=" + bptdCode);
		LOGGER.debug("count()", "parameters:itemValue=" + itemValue);
		LOGGER.debug("count()", "parameters:itemName=" + itemName);
		int result = 0;
		try{
			SqlQueryBean sql = new SqlQueryBean();
			String schema = this.getMySchema();
			sql.append(" SELECT COUNT(bpid.BPTD_CODE)");
			sql.append(" FROM ").append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid");
			sql.append(" INNER JOIN ").append(schema).append(".BASE_PARAMETER_TYPE_DEF bptd");
			// 去除approvedFlag
		//	sql.append(" ON bptd.APPROVED_FLAG = :approvedFlag");
		//	sql.append(" AND bpid.BPTD_CODE = bptd.BPTD_CODE");
			sql.append(" ON bpid.BPTD_CODE = bptd.BPTD_CODE");
		//	sql.setParameter(BaseParameterItemDefDTO.ATTRIBUTE.APPROVED_FLAG.getValue(), IAtomsConstants.YES);
			sql.append(" WHERE 1=1");			
			if(StringUtils.hasText(bptdCode)){
				sql.append(" AND bpid.BPTD_CODE = :bptdCode");
				sql.setParameter(BaseParameterItemDefDTO.ATTRIBUTE.BPTD_CODE.getValue(), bptdCode);
			}
			if(StringUtils.hasText(itemValue)){
				sql.append(" AND bpid.ITEM_VALUE like :itemValue");
				sql.setParameter(BaseParameterItemDefDTO.ATTRIBUTE.ITEM_VALUE.getValue(), itemValue + IAtomsConstants.MARK_PERCENT);
			}
			if(StringUtils.hasText(itemName)){
				sql.append(" AND bpid.ITEM_NAME like :itemName");
				sql.setParameter(BaseParameterItemDefDTO.ATTRIBUTE.ITEM_NAME.getValue(), itemName + IAtomsConstants.MARK_PERCENT);
			}
			sql.append(" AND isnull(bptd.READONLY,'N') = 'N' ");
			List<Integer> list = this.getDaoSupport().findByNativeSql(sql);
			LOGGER.debug("count()", "sql:" + sql.toString());
			if(!CollectionUtils.isEmpty(list)){
				result = list.get(0).intValue();
			}
		}catch(Exception e){
			LOGGER.error("count()", e.getMessage(), e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
		return result;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IBaseParameterManagerDAO#getBaseParameterItemDefDTO(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public BaseParameterItemDefDTO getBaseParameterItemDefDTO(String bpidId,
			Timestamp effectiveDate, String bptdCode, String itemValue, String itemName)
			throws DataAccessException {
		LOGGER.debug("getBaseParameterItemDefDTO()", "parameters:bpidId=" + bpidId);
		LOGGER.debug("getBaseParameterItemDefDTO()", "parameters:bptdCode=" + bptdCode);
		LOGGER.debug("getBaseParameterItemDefDTO()", "parameters:itemValue=" + itemValue);
		LOGGER.debug("getBaseParameterItemDefDTO()", "parameters:itemName=" + itemName);
		BaseParameterItemDefDTO result = null;
		try{
			SqlQueryBean sql = new SqlQueryBean();
			String schema = this.getMySchema();
			sql.append(" SELECT bpid.BPID_ID as bpidId,");
			sql.append(" bpid.BPTD_CODE as bptdCode,");
			sql.append(" bpid.EFFECTIVE_DATE as effectiveDate,");
			sql.append(" bpid.ITEM_VALUE as itemValue,");
			sql.append(" bpid.ITEM_NAME as itemName,");
//			sql.append(" bpid.REFERENCE_CODE as referenceCode,");
			sql.append(" bpid.ITEM_DESC as itemDesc,");
			sql.append(" bpid.ITEM_ORDER as itemOrder,");
//			sql.append(" bpid.ITEM_DEPTH as itemDepth,");
			// 去除approvedFlag
//			sql.append(" bpid.APPROVED_FLAG as approvedFlag,");
			sql.append(" bpid.TEXT_FIELD1 as textField1,");
//			sql.append(" bpid.TEXT_FIELD1 as textField2,");
//			sql.append(" bpid.TEXT_FIELD1 as textField3,");
//			sql.append(" bpid.TEXT_FIELD1 as textField4,");
//			sql.append(" bpid.TEXT_FIELD1 as textField5,");
//			sql.append(" bpid.NUMBER_FIELD1 as numberField1,");
//			sql.append(" bpid.NUMBER_FIELD2 as numberField2,");
			sql.append(" bpid.PARENT_BPID_ID as parentBpidId,");
//			sql.append(" bpid.CREATED_BY_ID as createdById,");
//			sql.append(" bpid.CREATED_BY_NAME as createdByName,");
//			sql.append(" bpid.CREATED_DATE as createdDate,");
			sql.append(" bpid.UPDATED_BY_ID as updatedById,");
			sql.append(" bpid.UPDATED_BY_NAME as updatedByName,");
			sql.append(" bpid.UPDATED_DATE as updatedDate");
			sql.append(" FROM ").append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid");
			sql.append(" LEFT JOIN ").append(schema).append(".BASE_PARAMETER_TYPE_DEF bptd");
			sql.append(" ON bpid.BPTD_CODE = bptd.BPTD_CODE");
			// 去除approvedFlag
		//	sql.append(" AND bptd.APPROVED_FLAG = :approvedFlag");
		//	sql.setParameter(BaseParameterItemDefDTO.ATTRIBUTE.APPROVED_FLAG.getValue(), IAtomsConstants.YES);
			sql.append(" WHERE 1=1");			
			if(StringUtils.hasText(bpidId)){
				sql.append(" AND bpid.BPID_ID = :bpidId");
				sql.setParameter(BaseParameterItemDefDTO.ATTRIBUTE.BPID_ID.getValue(), bpidId);
			}
			if(effectiveDate != null){
				sql.append(" AND bpid.EFFECTIVE_DATE = :effectiveDate");
				sql.setParameter(BaseParameterItemDefDTO.ATTRIBUTE.EFFECTIVE_DATE.getValue(), effectiveDate);
			}
			if(StringUtils.hasText(bptdCode)){
				sql.append(" AND bpid.BPTD_CODE = :bptdCode");
				sql.setParameter(BaseParameterItemDefDTO.ATTRIBUTE.BPTD_CODE.getValue(), bptdCode);
			}
			if(StringUtils.hasText(itemValue)){
				sql.append(" AND bpid.ITEM_VALUE = :itemValue");
				sql.setParameter(BaseParameterItemDefDTO.ATTRIBUTE.ITEM_VALUE.getValue(), itemValue);
			}
			if(StringUtils.hasText(itemName)){
				sql.append(" AND bpid.ITEM_NAME = :itemName");
				sql.setParameter(BaseParameterItemDefDTO.ATTRIBUTE.ITEM_NAME.getValue(), itemName);
			}
			sql.append(" AND isnull(bptd.READONLY,'N') = 'N' ");
			AliasBean alias = new AliasBean(BaseParameterItemDefDTO.class);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.BPID_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.BPTD_CODE.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.EFFECTIVE_DATE.getValue(), DateType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.ITEM_VALUE.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.ITEM_NAME.getValue(), StringType.INSTANCE);
//			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.REFERENCE_CODE.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.ITEM_DESC.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.ITEM_ORDER.getValue(), IntegerType.INSTANCE);
//			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.ITEM_DEPTH.getValue(), IntegerType.INSTANCE);
			// 去除approvedFlag
//			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.APPROVED_FLAG.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD1.getValue(), StringType.INSTANCE);
//			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD2.getValue(), StringType.INSTANCE);
//			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD3.getValue(), StringType.INSTANCE);
//			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD4.getValue(), StringType.INSTANCE);
//			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD5.getValue(), StringType.INSTANCE);
//			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.NUMBER_FIELD1.getValue(), DoubleType.INSTANCE);
//			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.NUMBER_FIELD2.getValue(), DoubleType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.PARENT_BPID_ID.getValue(), StringType.INSTANCE);
//			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.CREATED_BY_ID.getValue(), StringType.INSTANCE);
//			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.CREATED_BY_NAME.getValue(), StringType.INSTANCE);
//			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.CREATED_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.UPDATED_BY_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.UPDATED_DATE.getValue(), TimestampType.INSTANCE);
			List<BaseParameterItemDefDTO> list = this.getDaoSupport().findByNativeSql(sql, alias);
			LOGGER.debug("getBaseParameterItemDefDTO()", "sql:" + sql.toString());
			if(!CollectionUtils.isEmpty(list)){
				result = list.get(0);
			}
		} catch(Exception e){
			LOGGER.error("getBaseParameterItemDefDTO()", e.getMessage(), e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
		return result;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IBaseParameterManagerDAO#isRepeat(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isRepeat(String bpidId, String bptdCode, String itemValue, String itemName) throws DataAccessException {
		try {
			LOGGER.debug("isRepeat()", "parameters : bpidId=" + bpidId);
			LOGGER.debug("isRepeat()", "parameters : bptdCode=" + bptdCode);
			LOGGER.debug("isRepeat()", "parameters : itemValue=" + itemValue);
			LOGGER.debug("isRepeat()", "parameters : itemName=" + itemName);
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			sqlStatement.addFromExpression(schema + ".BASE_PARAMETER_ITEM_DEF bpid");
			// 當前id
			if (StringUtils.hasText(bpidId)) {
				sqlStatement.addWhereClause("bpid.BPID_ID <> :bpidId", bpidId);
			}
			sqlStatement.addWhereClause("bpid.BPTD_CODE=:bptdCode", bptdCode);
			// 當前itemValue
			if (StringUtils.hasText(itemValue)) {
				sqlStatement.addWhereClause("bpid.ITEM_VALUE = :itemValue", itemValue);
			}
			// 當前itemName
			if (StringUtils.hasText(itemName)) {
				sqlStatement.addWhereClause("bpid.ITEM_NAME = :itemName", itemName);
			}			
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug("isRepeat()", "sql:" + sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				if (result.get(0).intValue() > 0) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			LOGGER.error("isRepeat()", e.getMessage(), e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IBaseParameterManagerDAO#deleteByBptdCode(java.lang.String)
	 */
	@Override
	public void deleteTransferData(String bptdCodes) throws DataAccessException {
		try {
			LOGGER.debug("deleteTransferData()", "parameters : bptdCodes=" + bptdCodes);
			String schema = this.getMySchema();
			if (StringUtils.hasText(bptdCodes)) {
				SqlQueryBean sqlQueryBean = new SqlQueryBean("delete ");
				sqlQueryBean.append(schema).append(".BASE_PARAMETER_ITEM_DEF where BPTD_CODE in (:bptdCodes)");
				sqlQueryBean.setParameter("bptdCodes", StringUtils.toList(bptdCodes, IAtomsConstants.MARK_SEPARATOR));
				LOGGER.debug("deleteTransferData()", "sql:" + sqlQueryBean.toString());
				this.getDaoSupport().updateByNativeSql(sqlQueryBean);
				this.getDaoSupport().flush();
			}
		} catch (Exception e) {
			LOGGER.error("deleteTransferData()", e.getMessage(), e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
	}
	/** (non-Javadoc)
	 * @see cafe.core.dao.parameter.IBaseParameterItemDefDAO#list(java.lang.String, java.util.Date)
	 */
	public List<BaseParameterItemDefDTO> list(String parameterType,Date version) throws DataAccessException {
		if (!StringUtils.hasText(parameterType)) {
			throw new DataAccessException(CoreMessageCode.ARGUMENT_IS_NULL);
		}
		List<BaseParameterItemDefDTO> results = null;
		Date versionDate = (version == null) ? null : DateTimeUtils.truncateTime(version);
		try{
			SqlQueryBean sql = new SqlQueryBean();
			String schema = this.getMySchema();
			//sql.append("SELECT * FROM(");
			sql.append(" SELECT bpid.BPID_ID as bpidId,");
			sql.append(" bpid.EFFECTIVE_DATE as effectiveDate,");
			sql.append(" bpid.BPTD_CODE as bptdCode,");
			sql.append(" bpid.ITEM_VALUE as itemValue,");
			sql.append(" bpid.ITEM_NAME as itemName,");
			sql.append(" bpid.ITEM_DESC as itemDesc,");
			sql.append(" bpid.ITEM_ORDER as itemOrder,");
			sql.append(" bpid.TEXT_FIELD1 as textField1,");
			sql.append(" bpid.UPDATED_BY_ID as updatedById,");
			sql.append(" bpid.UPDATED_BY_NAME as updatedByName,");
			sql.append(" bpid.UPDATED_DATE as updatedDate,");
			// 去除approvedFlag
	//		sql.append(" bpid.APPROVED_FLAG as approvedFlag,");
			sql.append(" bpid.DELETED as deleted,");
			sql.append(" bpid.DELETED_DATE as deletedDate");
			sql.append(" FROM ").append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid");
			sql.append(" WHERE 1=1");			
			if(StringUtils.hasText(parameterType)){
				sql.append(" AND bpid.BPTD_CODE = :bptdCode");
				sql.setParameter(BaseParameterItemDefDTO.ATTRIBUTE.BPTD_CODE.getValue(), parameterType);
			}
			if(versionDate != null){
				sql.append(" AND bpid.EFFECTIVE_DATE  = :effectiveDate");
				sql.setParameter(BaseParameterItemDefDTO.ATTRIBUTE.EFFECTIVE_DATE.getValue(), versionDate);
			}
			AliasBean alias = new AliasBean(BaseParameterItemDefDTO.class);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.BPID_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.EFFECTIVE_DATE.getValue(), DateType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.BPTD_CODE.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.ITEM_VALUE.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.ITEM_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.ITEM_DESC.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.ITEM_ORDER.getValue(), IntegerType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD1.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.UPDATED_BY_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.UPDATED_DATE.getValue(), TimestampType.INSTANCE);
			// 去除approvedFlag
	//		alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.APPROVED_FLAG.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.DELETED.getValue(), StringType.INSTANCE);
			alias.addScalar(BaseParameterItemDefDTO.ATTRIBUTE.DELETED_DATE.getValue(), TimestampType.INSTANCE);
			results = this.getDaoSupport().findByNativeSql(sql, alias);
			LOGGER.debug("listBy()", "sql:" + sql.toString());
		} catch (Exception e){
			LOGGER.error("listBy()", e.getMessage(), e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
		return results;
	}
}
