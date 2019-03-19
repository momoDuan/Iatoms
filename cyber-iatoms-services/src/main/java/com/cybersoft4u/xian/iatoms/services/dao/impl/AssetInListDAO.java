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
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetInListDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IAssetInListDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetInList;
/**
 * 
 * Purpose: 設備入庫明細檔DAO
 * @author CarrieDuan
 * @since  JDK 1.6
 * @date   2016/6/24
 * @MaintenancePersonnel CarrieDuan
 */
public class AssetInListDAO extends GenericBaseDAO<DmmAssetInList> implements IAssetInListDAO {
	
	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(AssetInListDAO.class);
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetInListDAO#deleteAssetListByIds(java.lang.String)
	 */
	public void deleteAssetListByIds(List<String> deleteAssetListIds) throws DataAccessException{
		try {
			//打印參數
			LOGGER.debug(".deleteAssetListByIds()", "parameters:deleteAssetListIds=", deleteAssetListIds.toString());
			//schema
			String schema = this.getMySchema();
			//sql
			SqlQueryBean sql = new SqlQueryBean();
			sql.append("DELETE From ").append(schema).append(".DMM_ASSET_IN_LIST ");
			//條件
			sql.append(" WHERE ID in ( :assetInListIds )");
			sql.setParameter("assetInListIds", deleteAssetListIds);
			//刪除
			super.getDaoSupport().updateByNativeSql(sql);
		} catch (Exception e) {
			LOGGER.error("Exception deleteAssetListByIds() --->", e);
			throw new DataAccessException(IAtomsMessageCode.DELETE_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetInListDAO#getAcceptCount(java.lang.String, java.lang.Boolean)
	 */
	public Integer getAcceptCount(String assetInId, Boolean isChecked) throws DataAccessException {
		Integer count = Integer.valueOf(0);
		try {
			//打印參數---入庫批號
			LOGGER.debug("Parameter getAcceptCount()---->assetInId:", assetInId);
			//打印參數---是否客戶|實際驗收
			LOGGER.debug("Parameter getAcceptCount()---->isChecked:", isChecked.toString());
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			sqlStatement.addFromExpression(schema.concat(".DMM_ASSET_IN_LIST list "));
			if (StringUtils.hasText(assetInId)) {
				sqlStatement.addWhereClause("list.ASSET_IN_ID = :assetInId", assetInId);
			}
			if (isChecked.booleanValue()) {
				sqlStatement.addWhereClause("list.IS_CHECKED = :isChecked", IAtomsConstants.YES);
			}
			SqlQueryBean sql = sqlStatement.createSqlQueryBean();
		    //打印sql
			LOGGER.debug("getAcceptCount()---->sql:", sql.toString());
			//查詢
			List list = this.getDaoSupport().findByNativeSql(sql);
			if (!CollectionUtils.isEmpty(list)) {
				count = Integer.valueOf(list.get(0).toString());
			}
		} catch (Exception e) {
			LOGGER.debug("Exception getAcceptCount()---->", e);
			throw new DataAccessException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return count;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetInListDAO#getAssetInListDTO(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public AssetInListDTO getAssetInListDTO(String serialNumber)throws DataAccessException {
		LOGGER.debug(".getAssetInListDTO()", serialNumber );
		List<AssetInListDTO> result = null;
		AssetInListDTO assetInListDTO = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("ail.ID", AssetInListDTO.ATTRIBUTE.ASSET_ID.getValue());
			sqlStatement.addSelectClause("ail.SERIAL_NUMBER", AssetInListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("aii.ASSET_TYPE_ID", AssetInListDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			sqlStatement.addSelectClause("ail.PROPERTY_ID", AssetInListDTO.ATTRIBUTE.PROPERTY_ID.getValue());
			sqlStatement.addSelectClause("att.NAME", AssetInListDTO.ATTRIBUTE.ASSET_NAME.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".DMM_ASSET_IN_LIST ail");
			fromBuffer.append(" left join ").append(schema).append(".DMM_ASSET_IN_INFO aii on ail.ASSET_IN_ID = aii.ASSET_IN_ID ");
			fromBuffer.append(" left join ").append(schema).append(".DMM_ASSET_TYPE att on aii.ASSET_TYPE_ID = att.ASSET_TYPE_ID");
			sqlStatement.addFromExpression(fromBuffer.toString());
			/*sqlStatement.addFromExpression(schema +".DMM_ASSET_IN_LIST ail" +
					" left join "+ schema +".DMM_ASSET_IN_INFO aii on ail.ASSET_IN_ID = aii.ASSET_IN_ID " +
					" left join "+ schema +".DMM_ASSET_TYPE att on aii.ASSET_TYPE_ID = att.ASSET_TYPE_ID");*/
			if (StringUtils.hasText(serialNumber)) {
				sqlStatement.addWhereClause("ail.SERIAL_NUMBER=:serialNumber", serialNumber);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(AssetInListDTO.class);
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(result)) {
				assetInListDTO = result.get(0);
			}
		} catch (Exception e) {
			LOGGER.error(".getAssetInListDTO() is Error !!!", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ASSET_IN_LIST)}, e);
		}
		return assetInListDTO;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetInListDAO#deleteAssetInListByAssetInId(java.lang.String)
	 */
	public void deleteAssetInListByAssetInId(String assetInId)
			throws DataAccessException {
		//入庫批號
		LOGGER.error(".deleteAssetInListByAssetInId() Paramter assetInId--->", assetInId);
		try {
			if (StringUtils.hasText(assetInId)) {
				//schema
				String schema = this.getMySchema();
				//sql
				SqlQueryBean sql = new SqlQueryBean();
				sql.append("DELETE FROM  ").append(schema).append(".DMM_ASSET_IN_LIST ");
				sql.append("where ASSET_IN_ID = :assetInId");
				//設置條件
				sql.setParameter(AssetInListDTO.ATTRIBUTE.ASSET_IN_ID.getValue(), assetInId);
				LOGGER.debug(".deleteAssetInListByAssetInId() sql----->", sql.toString());
				super.getDaoSupport().updateByNativeSql(sql);
			}
		} catch (Exception e) {
			LOGGER.error(".deleteAssetInListByAssetInId()  Exception ", e);
			throw new DataAccessException(IAtomsMessageCode.DELETE_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetInListDAO#isCheckRropertyIdRepeat(java.lang.String)
	 */
	public Boolean isCheckRropertyIdRepeat(String propertyId) throws DataAccessException {
		LOGGER.error(".checkRropertyIdRepeat() Paramter propertyId--->", propertyId);
		try {
			String schema = this.getMySchema();
			List<Integer> assetInLists = null;
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".DMM_ASSET_IN_LIST");
			sqlStatement.addFromExpression(fromBuffer.toString());
			sqlStatement.addWhereClause("PROPERTY_ID = :propertyId", propertyId);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug(".checkRropertyIdRepeat()", "sql:", sqlQueryBean.toString());
			assetInLists = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(assetInLists)) {
				if (assetInLists.get(0).intValue() == 0) {
					return Boolean.FALSE;
				}
				return Boolean.TRUE;
			}
		} catch (Exception e) {
			LOGGER.error(".checkRropertyIdRepeat() is Error !!!", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ASSET_IN_LIST)}, e);
		}
		return Boolean.FALSE;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetInListDAO#listBy(java.lang.String, java.lang.String, java.lang.String, Integer, Integer)
	 */
	public List<AssetInListDTO> listBy(String assetInId, String order, String sort, Integer page, Integer rows) throws DataAccessException {
		List<AssetInListDTO> assetInLists = null;
		try {
			LOGGER.debug(".listBy()", "parameters:assetInId=", assetInId);
			LOGGER.debug(".listBy()", "parameters:order=", order);
			LOGGER.debug(".listBy()", "parameters:sort=", sort);
			LOGGER.debug(".listBy()", "parameters:page=", page.longValue());
			LOGGER.debug(".listBy()", "parameters:rows=", rows.longValue());
			String schema = this.getMySchema();	
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("ail.ASSET_IN_ID", AssetInListDTO.ATTRIBUTE.ASSET_IN_ID.getValue());
			sqlStatement.addSelectClause("ail.ID", AssetInListDTO.ATTRIBUTE.ASSET_ID.getValue());
			sqlStatement.addSelectClause("ail.SERIAL_NUMBER", AssetInListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("ail.PROPERTY_ID", AssetInListDTO.ATTRIBUTE.PROPERTY_ID.getValue());
			sqlStatement.addSelectClause("ail.IS_CHECKED", AssetInListDTO.ATTRIBUTE.IS_CHECKED.getValue());
			sqlStatement.addSelectClause("ail.IS_CUSTOMER_CHECKED", AssetInListDTO.ATTRIBUTE.IS_CUSTOMER_CHECKED.getValue());
			sqlStatement.addSelectClause("ain.KEEPER_NAME", AssetInListDTO.ATTRIBUTE.KEEPER_NAME.getValue());
			sqlStatement.addSelectClause("ain.ASSET_TYPE_ID", AssetInListDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			sqlStatement.addSelectClause("ail.UPDATED_BY_NAME", AssetInListDTO.ATTRIBUTE.UPDATE_USER_NAME.getValue());
			sqlStatement.addSelectClause("ail.UPDATED_DATE", AssetInListDTO.ATTRIBUTE.UPDATE_DATE.getValue());
			sqlStatement.addSelectClause("ail.UPDATED_BY_ID", AssetInListDTO.ATTRIBUTE.UPDATE_USER.getValue());
			sqlStatement.addSelectClause("type.NAME", AssetInListDTO.ATTRIBUTE.ASSET_NAME.getValue());
			
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".DMM_ASSET_IN_INFO ain");
			fromBuffer.append(" left join ").append(schema).append(".DMM_ASSET_IN_LIST ail on ail.ASSET_IN_ID = ain.ASSET_IN_ID ");
			fromBuffer.append(" left join ").append(schema).append(".DMM_ASSET_TYPE type on type.ASSET_TYPE_ID = ain.ASSET_TYPE_ID");
			sqlStatement.addFromExpression(fromBuffer.toString());
			/*sqlStatement.addFromExpression(schema + ".DMM_ASSET_IN_INFO ain" 
					+ " left join " + schema + ".DMM_ASSET_IN_LIST ail on ail.ASSET_IN_ID = ain.ASSET_IN_ID" 
					+ " left join " + schema + ".DMM_ASSET_TYPE type on type.ASSET_TYPE_ID = ain.ASSET_TYPE_ID");*/
			if (StringUtils.hasText(assetInId)) {
				sqlStatement.addWhereClause("ail.ASSET_IN_ID = :assetInId", assetInId);
			}
			if (StringUtils.hasText(sort) && StringUtils.hasText(order)) {
				sqlStatement.setOrderByExpression(sort.concat(" ").concat(order));
			}
			sqlStatement.setPageSize(rows.intValue());
			sqlStatement.setStartPage(page.intValue() - 1);
			AliasBean aliasBean = sqlStatement.createAliasBean(AssetInListDTO.class);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug("SQL---------->", sqlQueryBean.toString());
			assetInLists = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(".listBy() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_CONTRACT)}, e);
		}
		return assetInLists;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetInListDAO#count(java.lang.String)
	 */
	public Integer count(String assetInId) throws DataAccessException {
		LOGGER.debug(".count()", "parameters:assetInId=", assetInId);
		Integer result = Integer.valueOf(0);
		try {
			String schema = this.getMySchema();	
			//查询总条数
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("COUNT(1)");
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".DMM_ASSET_IN_LIST ail ");
			sqlStatement.addFromExpression(fromBuffer.toString());
			//sqlStatement.addFromExpression(schema +".DMM_ASSET_IN_LIST ail ");
			if ( StringUtils.hasText(assetInId)) {
				sqlStatement.addWhereClause("ail.ASSET_IN_ID = :assetInId", assetInId);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug("SQL---------->", sqlQueryBean.toString());
			List<Integer> list = super.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(list)){
				result = list.get(0);
			}	
		} catch (Exception e) {
			LOGGER.error(".count() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_CONTRACT)}, e);
		}
		return result;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetInListDAO#getAssetInListDTOs()
	 */
	public List<AssetInListDTO> getAssetInListDTOs() throws DataAccessException {
		List<AssetInListDTO> assetInLists = null;
		try {
			String schema = this.getMySchema();	
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("ail.SERIAL_NUMBER", AssetInListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("ail.PROPERTY_ID", AssetInListDTO.ATTRIBUTE.PROPERTY_ID.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".DMM_ASSET_IN_LIST ail ");
			sqlStatement.addFromExpression(fromBuffer.toString());
			//sqlStatement.addFromExpression(schema + ".DMM_ASSET_IN_LIST ail" );
			AliasBean aliasBean = sqlStatement.createAliasBean(AssetInListDTO.class);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug("SQL---------->", sqlQueryBean.toString());
			assetInLists = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(".listBy() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_CONTRACT)}, e);
		}
		return assetInLists;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetInListDAO#getCustomerAcceptCount(java.lang.String, java.lang.Boolean)
	 */
	@Override
	public Integer getCustomerAcceptCount(String assetInId, Boolean isChecked)
			throws DataAccessException {
		Integer count = Integer.valueOf(0);
		try {
			//打印參數---入庫批號
			LOGGER.debug("Parameter getAcceptCount()---->assetInId:", assetInId);
			//打印參數---是否客戶|實際驗收
			LOGGER.debug("Parameter getAcceptCount()---->isChecked:", isChecked.toString());
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			sqlStatement.addFromExpression(schema.concat(".DMM_ASSET_IN_LIST list "));
			if (StringUtils.hasText(assetInId)) {
				sqlStatement.addWhereClause("list.ASSET_IN_ID = :assetInId", assetInId);
			}
			if (isChecked.booleanValue()) {
				sqlStatement.addWhereClause("list.IS_CUSTOMER_CHECKED = :isChecked", IAtomsConstants.YES);
			}
			SqlQueryBean sql = sqlStatement.createSqlQueryBean();
		    //打印sql
			LOGGER.debug("getAcceptCount()---->sql:", sql.toString());
			//查詢
			List list = this.getDaoSupport().findByNativeSql(sql);
			if (!CollectionUtils.isEmpty(list)) {
				count = Integer.valueOf(list.get(0).toString());
			}
		} catch (Exception e) {
			LOGGER.debug("Exception getAcceptCount()---->", e);
			throw new DataAccessException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return count;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetInListDAO#saveAssetInListInfo(java.lang.String)
	 */
	@Override
	public void saveAssetInListInfo(String assetInId) throws DataAccessException {
		try{
			//打印參數
			LOGGER.debug(this.getClass().getSimpleName() + "saveAssetInListInfo", "assetInId:" + assetInId);
			//創建SqlStatement
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("exec dbo.usp_Copy_DMM_ASSET_IN_LIST_TO_HISTORY :assetInId");
			sqlQueryBean.setParameter(AssetInListDTO.ATTRIBUTE.ASSET_IN_ID.getValue(), assetInId);
			LOGGER.debug(this.getClass().getName() + "saveAssetInListInfo ---->sql:" + sqlQueryBean.toString() );
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.debug("Exception saveAssetInListInfo()---->", e);
			throw new DataAccessException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
	} 
}
