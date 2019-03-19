package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Parameter;
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
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetStacktakeListDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetStocktakeInfo;

/**
 * 
 * Purpose: 設備盤點主檔
 * @author allenchen
 * @since  JDK 1.7
 * @date   2016-7-19
 * @MaintenancePersonnel allenchen
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DmmAssetStacktakeInfoDAO extends GenericBaseDAO<DmmAssetStocktakeInfo> implements IDmmAssetStacktakeInfoDAO {

	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(DmmAssetStacktakeInfoDAO.class);

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeInfoDAO#getAssetStocktakeIdList(boolean)
	 */
	@Override
	public List<Parameter> getAssetStocktakeIdList(boolean isComplate) throws DataAccessException {
		List<Parameter> result = null;
		LOGGER.debug("getAssetStocktakeIdList()", "isComplate : ", String.valueOf(isComplate));
		try {
			String schema = this.getMySchema();
			SqlStatement sql = new SqlStatement();
			sql.addSelectClause("info.STOCKTACK_ID", Parameter.FIELD_VALUE);
			sql.addSelectClause("info.STOCKTACK_ID", Parameter.FIELD_NAME);
			sql.addFromExpression(schema + ".DMM_ASSET_STOCKTAKE_INFO info");
			if (isComplate) {
				sql.addWhereClause("info.COMPLETE_STATUS = :status", IAtomsConstants.YES);
			} else {
				sql.addWhereClause("info.COMPLETE_STATUS = :status", IAtomsConstants.NO);
			}
			sql.setOrderByExpression("info.STOCKTACK_ID DESC");
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			AliasBean aliasBean = sql.createAliasBean(Parameter.class);
			LOGGER.debug("getAssetStocktakeIdList()", "SQL---------->", sql.toString());
			result = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("DmmAssetStacktakeInfoDAO()", "getAssetStocktakeIdList()　is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ASSET_STOCKTACK_INFO)} ,e);
		}
		return result;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeInfoDAO#getAssetStocktackInfoDTOList(java.lang.String)
	 */
	@Override
	public List<DmmAssetStacktakeListDTO> getAssetStocktackInfoDTOList( String stocktackId, boolean isComplete) throws DataAccessException {
		LOGGER.debug("getAssetStocktackInfoDTOList()", "stocktackId : ", stocktackId);
		LOGGER.debug("getAssetStocktackInfoDTOList()", "isComplete : ", String.valueOf(isComplete));
		try {
			//根據盤點批號查詢到的數據分類顯示
			String schema = this.getMySchema();
			List<DmmAssetStacktakeListDTO> relults;
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("SELECT bpid1.ITEM_NAME AS ").append(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_STATUS_NAME.getValue()).append(",");
			sqlQueryBean.append("type.NAME AS ").append(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_TYPE_NAME.getValue()).append(",");
			sqlQueryBean.append("total.padding as ").append(DmmAssetStacktakeListDTO.ATTRIBUTE.NO_STOCKTACK.getValue()).append(",");
			sqlQueryBean.append("total.ok as ").append(DmmAssetStacktakeListDTO.ATTRIBUTE.ALREADY_STOCKTACK.getValue()).append(",");
			sqlQueryBean.append("total.more as ").append(DmmAssetStacktakeListDTO.ATTRIBUTE.OVERAGE.getValue()).append(",");
			sqlQueryBean.append("total.total as ").append(DmmAssetStacktakeListDTO.ATTRIBUTE.TOTAL_STOCKTACK.getValue());
			//盤點完成就去統計盤差數
			if(isComplete){
				sqlQueryBean.append(", total.less as ").append(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSETL_LESS.getValue());
			}
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(" from (select asl.asset_type_id assetCategory, asl.asset_status assetStatus, sum(case asl.STOCKTAKE_STATUS when :noStocktack then 1 else 0 end) as padding, ")
				.append("sum(case asl.STOCKTACK_STATUS when :alreadyStocktack then 1 else 0 end) as ok, sum(case asl.STOCKTAKE_STATUS when :overage then 1 else 0 end) as more, ");
			sqlQueryBean.append(stringBuffer.toString());
			/*sqlQueryBean.append(" from (select asl.asset_type_id assetCategory, asl.asset_status assetStatus, " +
					"sum(case asl.STOCKTAKE_STATUS when :noStocktack then 1 else 0 end) as padding, " +
					"sum(case asl.STOCKTACK_STATUS when :alreadyStocktack then 1 else 0 end) as ok,  " +
					"sum(case asl.STOCKTAKE_STATUS when :overage then 1 else 0 end) as more, ");*/
			//盤點完成就去統計盤差數
			if(isComplete){
				sqlQueryBean.append(" sum(case asl.STOCKTAKE_STATUS when :assetlLess then 1 else 0 end) as less, ");
			}
			sqlQueryBean.append(" count(*) as total");
			sqlQueryBean.append(" from " + schema + ".DMM_ASSET_STOCKTAKE_LIST asl where 1=1 ");
			sqlQueryBean.append(" and asl.STOCKTACK_ID = :stocktackId");
			sqlQueryBean.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.STOCKTACK_ID.getValue(), stocktackId);
			stringBuffer.delete(0, stringBuffer.length());
			stringBuffer.append(" group by asl.asset_type_id, asl.asset_status) total left join ").append(schema).append(".DMM_ASSET_TYPE type ON type.asset_type_id = total.assetCategory left join ")
				.append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid1  ON bpid1.ITEM_VALUE = total.assetStatus  AND bpid1.BPTD_CODE = :assetStatus");
			sqlQueryBean.append(stringBuffer.toString());
			/*sqlQueryBean.append(" group by asl.asset_type_id, asl.asset_status) total left join " + schema + ".DMM_ASSET_TYPE type ON type.asset_type_id = total.assetCategory ");
			sqlQueryBean.append(" LEFT JOIN " + schema + ".BASE_PARAMETER_ITEM_DEF bpid1  ON bpid1.ITEM_VALUE = total.assetStatus  AND bpid1.BPTD_CODE = :assetStatus");*/
			sqlQueryBean.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.NO_STOCKTACK.getValue(), IAtomsConstants.PARAM_NO_STOCKTAKE);
			sqlQueryBean.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.ALREADY_STOCKTACK.getValue(), IAtomsConstants.PARAM_ALREADY_STOCKTAKE);
			if (isComplete) {
				sqlQueryBean.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSETL_LESS.getValue(), IAtomsConstants.PARAM_LESS_STOCKTAKE);
			}
			sqlQueryBean.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.OVERAGE.getValue(), IAtomsConstants.PARAM_MORE_STOCKTAKE);
			sqlQueryBean.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_STATUS.getValue(), IATOMS_PARAM_TYPE.ASSET_STATUS.getCode());
			AliasBean aliasBean = new AliasBean(DmmAssetStacktakeListDTO.class);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_STATUS_NAME.getValue(),StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_TYPE_NAME.getValue(),StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.NO_STOCKTACK.getValue(),IntegerType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.ALREADY_STOCKTACK.getValue(),IntegerType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.OVERAGE.getValue(),IntegerType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.TOTAL_STOCKTACK.getValue(),IntegerType.INSTANCE);
			if(isComplete){
				aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSETL_LESS.getValue(),IntegerType.INSTANCE);
			}
			LOGGER.debug("getAssetStocktackInfoDTOList()", "SQL---------->", sqlQueryBean.toString());
			relults = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return relults;
		} catch (Exception e) {
			LOGGER.error("DmmAssetStacktakeInfoDAO.getAssetStocktackInfoDTOList()", " is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeInfoDAO#getAssetStocktackInfoDTO(java.lang.String)
	 */
	@Override
	public DmmAssetStacktakeListDTO getAssetStocktackInfoDTO(String stocktackId)throws DataAccessException {
		LOGGER.debug("getAssetStocktackInfoDTO()", "stocktackId : ", stocktackId );
		DmmAssetStacktakeListDTO result = null;
		String schema = this.getMySchema();
		SqlStatement sqlStatement = new SqlStatement();
		try {
			sqlStatement.addSelectClause("distinct asi.STOCKTACK_ID", DmmAssetStacktakeListDTO.ATTRIBUTE.STOCKTACK_ID.getValue());
			sqlStatement.addSelectClause("asi.WAR_WAREHOUSE_ID", DmmAssetStacktakeListDTO.ATTRIBUTE.WAR_WAREHOUSE_ID.getValue());
			sqlStatement.addSelectClause("asi.REMARK", DmmAssetStacktakeListDTO.ATTRIBUTE.REMARK.getValue());
			sqlStatement.addSelectClause("c.short_name + '-' + w.NAME", DmmAssetStacktakeListDTO.ATTRIBUTE.WAR_HOUSE_NAME.getValue());
			sqlStatement.addSelectClause("w.CONTACT", DmmAssetStacktakeListDTO.ATTRIBUTE.CONTACT.getValue());
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("STUFF((SELECT ','+ ltrim(type.NAME) FROM ").append(schema).append(".DMM_ASSET_STOCKTAKE_INFO a1 LEFT JOIN ").append(schema)
				.append(".DMM_ASSET_STOCKTAKE_CATEGORY b1 ON a1.STOCKTACK_ID = b1.STOCKTACK_ID LEFT JOIN ").append(schema).append(".DMM_ASSET_TYPE type ON b1.ASSET_TYPE_ID = type.ASSET_TYPE_ID ")
				.append(" WHERE a1.STOCKTACK_ID = asi.STOCKTACK_ID order by type.NAME FOR XML PATH('')) , 1, 1, '')");
			sqlStatement.addSelectClause(stringBuffer.toString(), DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_TYPE_NAME.getValue());
			/*sqlStatement.addSelectClause("STUFF((SELECT ','+ ltrim(type.NAME) FROM " + Schema + ".DMM_ASSET_STOCKTAKE_INFO a1 LEFT JOIN " + Schema + ".DMM_ASSET_STOCKTAKE_CATEGORY b1" +
					" ON a1.STOCKTACK_ID = b1.STOCKTACK_ID LEFT JOIN " + Schema + ".DMM_ASSET_TYPE type " +
					"ON b1.ASSET_TYPE_ID = type.ASSET_TYPE_ID " + 
					" WHERE a1.STOCKTACK_ID = asi.STOCKTACK_ID order by type.NAME FOR XML PATH('')) , 1, 1, '')", DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_TYPE_NAME.getValue());*/
			stringBuffer.delete(0, stringBuffer.length());
			stringBuffer.append("STUFF((SELECT ','+ ltrim(bpid2.ITEM_NAME) FROM ").append(schema).append(".DMM_ASSET_STOCKTAKE_INFO a2 LEFT JOIN ").append(schema)
				.append(".DMM_ASSET_STOCKTAKE_STATUS b2 ON a2.STOCKTACK_ID = b2.STOCKTACK_ID LEFT JOIN ").append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid2 ON bpid2.ITEM_VALUE = b2.ASSET_STATUS AND bpid2.BPTD_CODE = :assetStatus")
				.append(" WHERE a2.STOCKTACK_ID = asi.STOCKTACK_ID order by bpid2.ITEM_ORDER FOR XML PATH('')) , 1, 1, '')");
			sqlStatement.addSelectClause(stringBuffer.toString(), DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_STATUS_NAME.getValue());
			/*sqlStatement.addSelectClause("STUFF((SELECT ','+ ltrim(bpid2.ITEM_NAME) FROM  " + Schema + ".DMM_ASSET_STOCKTAKE_INFO a2 LEFT JOIN " + Schema + ".DMM_ASSET_STOCKTAKE_STATUS b2" +
					" ON a2.STOCKTACK_ID = b2.STOCKTACK_ID LEFT JOIN " + Schema + ".BASE_PARAMETER_ITEM_DEF bpid2 " +
					" ON bpid2.ITEM_VALUE = b2.ASSET_STATUS AND bpid2.BPTD_CODE = 'ASSET_STATUS'" + 
					" WHERE a2.STOCKTACK_ID = asi.STOCKTACK_ID order by bpid2.ITEM_ORDER FOR XML PATH('')) , 1, 1, '')", DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_STATUS_NAME.getValue());	*/	
			stringBuffer.delete(0, stringBuffer.length());
			stringBuffer.append(schema).append(".DMM_ASSET_STOCKTAKE_INFO asi left join ").append(schema).append(".DMM_ASSET_STOCKTAKE_CATEGORY asca on asi.STOCKTACK_ID = asca.STOCKTACK_ID left join ").append(schema)
				.append(".DMM_ASSET_STOCKTAKE_STATUS ass on ass.STOCKTACK_ID = asi.STOCKTACK_ID left join ").append(schema).append(".BIM_WAREHOUSE w on asi.WAR_WAREHOUSE_ID = w.WAREHOUSE_ID left join ").append(schema)
				.append(".BIM_COMPANY c on w.company_id = c.company_id left join ").append(schema).append(".BIM_COMPANY_TYPE type ON type.COMPANY_ID = c.COMPANY_ID ");
			sqlStatement.addFromExpression(stringBuffer.toString());
		/*	sqlStatement.addFromExpression(Schema + ".DMM_ASSET_STOCKTAKE_INFO asi " +
			
			" left join " + Schema + ".DMM_ASSET_STOCKTAKE_CATEGORY asca on asi.STOCKTACK_ID = asca.STOCKTACK_ID " +
			" left join " + Schema + ".DMM_ASSET_STOCKTAKE_STATUS ass on ass.STOCKTACK_ID = asi.STOCKTACK_ID " +
			" left join " + Schema + ".BIM_WAREHOUSE w on asi.WAR_WAREHOUSE_ID = w.WAREHOUSE_ID " +
			" left join " + Schema + ".BIM_COMPANY c on w.company_id = c.company_id " +
			" left join " + Schema + ".BIM_COMPANY_TYPE type ON type.COMPANY_ID = c.COMPANY_ID ");*/
			if (StringUtils.hasText(stocktackId)) {
				sqlStatement.addWhereClause("asi.STOCKTACK_ID =:stocktackId", stocktackId);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("assetStatus", IAtomsConstants.ASSET_STATUS);
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmAssetStacktakeListDTO.class);
			LOGGER.debug("getAssetStocktackInfoDTO()", "SQL---------->", sqlQueryBean.toString());
			List<DmmAssetStacktakeListDTO> results = (List<DmmAssetStacktakeListDTO>) this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (results != null) {
				result = results.get(0);
			}
		} catch (Exception e) {
			LOGGER.error("DmmAssetStacktakeInfoDAO", ".getAssetStocktackInfoDTO() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ASSET_STOCKTACK_INFO)} ,e);
		}
		return result;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeInfoDAO#).isExistCheck(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isExistCheck(String warHouseId, String assetType, String assetStatu) throws DataAccessException {
		LOGGER.debug("isExistCheck()", "warHouseId : ", warHouseId);
		LOGGER.debug("isExistCheck()", "assetType : ", assetType);
		LOGGER.debug("isExistCheck()", "assetStatu : ", assetStatu);
		String schema = this.getMySchema();	
		SqlStatement sqlStatement = new SqlStatement();
		try {
			//查詢語句
			sqlStatement.addSelectClause("count(*)");
			sqlStatement.addFromExpression(schema + ".DMM_REPOSITORY repo");
			if (StringUtils.hasText(warHouseId)) {
				sqlStatement.addWhereClause("repo.WAREHOUSE_ID = :warHouseId", warHouseId);
			}
			if (StringUtils.hasText(assetType)) {
				sqlStatement.addWhereClause("repo.ASSET_TYPE_ID in".concat( IAtomsConstants.MARK_BRACKETS_LEFT).concat(assetType).concat(IAtomsConstants.MARK_BRACKETS_RIGHT));
			}
			if (StringUtils.hasText(assetStatu)) {
				sqlStatement.addWhereClause("repo.STATUS in".concat( IAtomsConstants.MARK_BRACKETS_LEFT).concat(assetStatu).concat(IAtomsConstants.MARK_BRACKETS_RIGHT));
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			//打印sql語句
			LOGGER.debug("isExistCheck()", "SQL---------->", sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (result.get(0).intValue() > 0) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.error("DmmAssetStacktakeInfoDAO()", ".isExistCheck() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return false;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeInfoDAO#getAssetStocktackIdHistList(java.lang.String, java.lang.String)
	 */
	public List<Parameter> getAssetStocktackIdHistList(String warHouseId, String assetTypeId) throws DataAccessException {
		List<Parameter> result = null;
		LOGGER.debug("getAssetStocktackIdHistList()", "warHouseId : ", warHouseId);
		LOGGER.debug("getAssetStocktackIdHistList()", "assetTypeId : ", assetTypeId);
		try {
			String schema = this.getMySchema();
			SqlStatement sql = new SqlStatement();
			sql.addSelectClause("info.STOCKTACK_ID", Parameter.FIELD_VALUE);
			sql.addSelectClause("info.STOCKTACK_ID", Parameter.FIELD_NAME);
			sql.addFromExpression(schema.concat(".DMM_ASSET_STOCKTAKE_INFO info"));
			if (StringUtils.hasText(warHouseId)) {
				sql.addWhereClause("info.WAR_WAREHOUSE_ID = :warehouseId", warHouseId);
			}
			if (StringUtils.hasText(assetTypeId)) {
				sql.addWhereClause(" exists(select 1 from ".concat(schema).concat(".DMM_ASSET_STOCKTAKE_CATEGORY asset ").concat("where asset.STOCKTACK_ID = info.STOCKTACK_ID ")
							.concat("and asset.ASSET_TYPE_ID = :assetTypeId)"));
			}
			sql.addWhereClause("info.COMPLETE_STATUS = :complateStatus", IAtomsConstants.YES);
			sql.setOrderByExpression("info.STOCKTACK_ID DESC");
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			if (StringUtils.hasText(assetTypeId)) {
				sqlQueryBean.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), assetTypeId);
			}
			AliasBean aliasBean = sql.createAliasBean(Parameter.class);
			LOGGER.debug("getAssetStocktackIdHistList()", "SQL---------->", sql.toString());
			result = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("DmmAssetStacktakeInfoDAO()", ".getAssetStocktackIdHistList()　is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ASSET_STOCKTACK_INFO)} ,e);
		}
		return result;
	}
	
}
