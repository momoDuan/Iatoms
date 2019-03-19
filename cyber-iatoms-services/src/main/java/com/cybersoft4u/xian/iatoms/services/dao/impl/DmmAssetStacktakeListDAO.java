package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetStocktakeCategroyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetStocktakeStatusDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimMerchantHeaderDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetStacktakeInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetStacktakeListDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetStacktakeFormDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeListDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetStocktakeList;

/**
 * 
 * Purpose: 設備盤點明細
 * @author echomou
 * @since  JDK 1.7
 * @date   2016-7-19
 * @MaintenancePersonnel echomou
 */
@SuppressWarnings("unchecked")
public class DmmAssetStacktakeListDAO extends GenericBaseDAO<DmmAssetStocktakeList> implements IDmmAssetStacktakeListDAO {

	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(DmmAssetStacktakeListDAO.class);
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeListDAO#getListAssetStockDTOs(com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetStacktakeInfoDTO)
	 */
	@Override
	public List<DmmAssetStacktakeListDTO> getListAssetStockDTOs(String stocktackId) throws DataAccessException {
		LOGGER.debug(this.getClass().getName() + ".getListAssetStockDTOs()" + stocktackId);
		try {
			List<DmmAssetStacktakeListDTO> results = null;
			String schema = this.getMySchema();	
			SqlStatement sqlStatement = new SqlStatement();
			//根據條件從資料庫表里查詢數據，查詢出來的是List對象的集合，以後會把這個集合存到LIST表裡面
			sqlStatement.addSelectClause("DISTINCT repo.SERIAL_NUMBER", DmmAssetStacktakeListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("repo.STATUS", DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_STATUS.getValue());
			sqlStatement.addSelectClause("type.ASSET_TYPE_ID", DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			sqlStatement.addSelectClause("info.STOCKTACK_ID", DmmAssetStacktakeListDTO.ATTRIBUTE.STOCKTACK_ID.getValue());
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(schema).append(".DMM_REPOSITORY repo, ").append(schema).append(".DMM_ASSET_STOCKTAKE_INFO info, ").append(schema)
				.append(".DMM_ASSET_STOCKTAKE_CATEGORY category, ");
			stringBuffer.append(schema).append(".DMM_ASSET_TYPE type, ").append(schema).append(".DMM_ASSET_STOCKTAKE_STATUS status");
			sqlStatement.addFromExpression(stringBuffer.toString());
			/*sqlStatement.addFromExpression(schema + ".DMM_REPOSITORY repo, " + schema + ".DMM_ASSET_STOCKTAKE_INFO info, " + schema + ".DMM_ASSET_STOCKTAKE_CATEGORY category, "
					+ schema + ".DMM_ASSET_TYPE type, " + schema + ".DMM_ASSET_STOCKTAKE_STATUS status");*/
			sqlStatement.addWhereClause("repo.STATUS = status.ASSET_STATUS and status.STOCKTACK_ID = info.STOCKTACK_ID");
			sqlStatement.addWhereClause("repo.ASSET_TYPE_ID = type.ASSET_TYPE_ID");
			sqlStatement.addWhereClause("type.ASSET_TYPE_ID = category.ASSET_TYPE_ID");
			sqlStatement.addWhereClause("category.STOCKTACK_ID = info.STOCKTACK_ID");
			sqlStatement.addWhereClause("info.STOCKTACK_ID = :stocktackId", stocktackId);
			//記錄SQL語句
			LOGGER.debug("getListAssetStockDTOs()", "sql:", sqlStatement.toString() );
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = new AliasBean(DmmAssetStacktakeListDTO.class);
			results = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return results;
		} catch (Exception e) {
			LOGGER.error("DmmAssetStacktakeListDAO.getListAssetStockDTOs()", " is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeListDAO#getCount(java.lang.String)
	 */
	@Override
	public Integer getCount(String stocktackId, Boolean isMore) throws DataAccessException {
		LOGGER.debug("getCount()", "stocktackId :", stocktackId);
		String schema = this.getMySchema();	
		SqlStatement sqlStatement = new SqlStatement();
		try {
			sqlStatement.addSelectClause("count(*)");
			sqlStatement.addFromExpression(schema + ".DMM_ASSET_STOCKTAKE_LIST list");
			if (StringUtils.hasText(stocktackId)) {
				sqlStatement.addWhereClause("list.STOCKTACK_ID =:stocktackId", stocktackId);
			}
			if (!isMore) {
				sqlStatement.addWhereClause("list.STOCKTAKE_STATUS != :stocktakeStatus", IAtomsConstants.PARAM_MORE_STOCKTAKE);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug("getCount()", "SQL---------->", sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)){
				return result.get(0);
			}	
		} catch (Exception e) {
			LOGGER.error("DmmAssetStacktakeListDAO.getCount()", "is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ASSET_STOCKTACK_LIST)}, e);
		}
		return Integer.valueOf(0);
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeListDAO#list(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@Override
	public List<DmmAssetStacktakeListDTO> list(String stocktackId, String serialNumber, Integer pageSize, Integer pageIndex, String sort, String orderby)throws DataAccessException {
		LOGGER.debug("list()", "stocktackId:", stocktackId);
		LOGGER.debug("list()", "serialNumber:", serialNumber);
		LOGGER.debug("list()", "pageSize:", pageSize);
		LOGGER.debug("list()", "pageIndex:", pageIndex);
		LOGGER.debug("list()", "sort:", sort);
		LOGGER.debug("list()", "orderby:", orderby);
		String schema = this.getMySchema();	
		List<DmmAssetStacktakeListDTO> results = null;
		SqlStatement sqlStatement = new SqlStatement();
		try {
			//查詢盤點明細內容
		    sqlStatement.addSelectClause("list.STOCKTAKE_STATUS", DmmAssetStacktakeListDTO.ATTRIBUTE.STOCKTAKE_STATUS.getValue());
			sqlStatement.addSelectClause("list.SERIAL_NUMBER ", DmmAssetStacktakeListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("list.ID", DmmAssetStacktakeListDTO.ATTRIBUTE.TACK_ID.getValue());
			sqlStatement.addSelectClause("list.STOCKTACK_ID", DmmAssetStacktakeListDTO.ATTRIBUTE.STOCKTACK_ID.getValue());
			sqlStatement.addSelectClause("list.UPDATED_BY_ID", DmmAssetStacktakeListDTO.ATTRIBUTE.UPDATED_BY_ID.getValue());
			sqlStatement.addSelectClause("list.ASSET_STATUS", DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_STATUS.getValue());
			sqlStatement.addSelectClause("bpid2.ITEM_NAME", DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_STATUS_NAME.getValue());
			sqlStatement.addSelectClause("list.ASSET_TYPE_ID", DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			sqlStatement.addSelectClause("type.NAME", DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("list.REMARK", DmmAssetStacktakeListDTO.ATTRIBUTE.REMARK.getValue());
			sqlStatement.addSelectClause("list.UPDATED_BY_NAME", DmmAssetStacktakeListDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("list.UPDATED_DATE", DmmAssetStacktakeListDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			sqlStatement.addSelectClause("house.CONTACT", DmmAssetStacktakeListDTO.ATTRIBUTE.CONTACT.getValue());
			sqlStatement.addSelectClause("house.NAME", DmmAssetStacktakeListDTO.ATTRIBUTE.WAR_HOUSE_NAME.getValue());
			sqlStatement.addSelectClause("house.ADDRESS", DmmAssetStacktakeListDTO.ATTRIBUTE.ADDRESS.getValue());
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(schema).append(".DMM_ASSET_STOCKTAKE_LIST list left join ").append(schema).append(".DMM_ASSET_TYPE type ON type.ASSET_TYPE_ID = list.ASSET_TYPE_ID left join ")
				.append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid2 ON bpid2.ITEM_VALUE = list.ASSET_STATUS AND bpid2.BPTD_CODE = :assetStatus left join ").append(schema)
				.append(".DMM_ASSET_STOCKTAKE_INFO asi ON asi.STOCKTACK_ID = list.STOCKTACK_ID left join ").append(schema).append(".BIM_WAREHOUSE house ON asi.WAR_WAREHOUSE_ID = house.WAREHOUSE_ID");
			sqlStatement.addFromExpression(stringBuffer.toString());
			/*sqlStatement.addFromExpression(schema + ".DMM_ASSET_STOCKTAKE_LIST list" + 
			" left join " + schema + ".DMM_ASSET_TYPE type ON type.ASSET_TYPE_ID = list.ASSET_TYPE_ID" +
			" left join " + schema + ".BASE_PARAMETER_ITEM_DEF bpid2 ON bpid2.ITEM_VALUE = list.ASSET_STATUS AND bpid2.BPTD_CODE = :assetStatus" +
			" left join " + schema + ".DMM_ASSET_STOCKTAKE_INFO asi ON asi.STOCKTACK_ID = list.STOCKTACK_ID" +
			" left join " + schema + ".BIM_WAREHOUSE house ON asi.WAR_WAREHOUSE_ID = house.WAREHOUSE_ID" );*/
			if (StringUtils.hasText(stocktackId)) {
				sqlStatement.addWhereClause("list.STOCKTACK_ID = :stocktackId", stocktackId);
			}
			if (StringUtils.hasText(serialNumber)) {
				sqlStatement.addWhereClause("list.SERIAL_NUMBER = :serialNumber", serialNumber);
			}
			//設置排序表達式
			if (!StringUtils.hasText(sort) && !StringUtils.hasText(orderby)) {
				sqlStatement.setOrderByExpression("type.NAME, list.SERIAL_NUMBER");
			} else if (StringUtils.hasText(sort) && StringUtils.hasText(orderby)) {
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append(sort).append(IAtomsConstants.MARK_SPACE).append(orderby);
				sqlStatement.setOrderByExpression(stringBuffer.toString());
				//sqlStatement.setOrderByExpression(sort + IAtomsConstants.MARK_SPACE + orderby);
			}
			//分頁
			sqlStatement.setPageSize(pageSize.intValue());
			sqlStatement.setStartPage(pageIndex.intValue() - 1);
			//記錄SQL語句
			LOGGER.debug("list()", "sql:", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_STATUS.getValue(), IATOMS_PARAM_TYPE.ASSET_STATUS.getCode());
			AliasBean aliasBean = new AliasBean(DmmAssetStacktakeListDTO.class);
			results = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return results;
		} catch (Exception e) {
			LOGGER.error("DmmAssetStacktakeListDAO.list()", "is error:", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeListDAO#listAssetInventory(com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetStacktakeFormDTO)
	 */
	@Override
	public List<DmmAssetStacktakeListDTO> listAssetInventory(String stocktackId, boolean isComplete, String sort, String order, Integer currentPage, Integer pageSize)  {
		LOGGER.debug("listAssetInventory()", "stocktackId:", stocktackId);
		LOGGER.debug("listAssetInventory()", "isComplete:", String.valueOf(isComplete));
		LOGGER.debug("listAssetInventory()", "pageSize:", pageSize);
		LOGGER.debug("listAssetInventory()", "currentPage:", currentPage);
		LOGGER.debug("listAssetInventory()", "sort:", sort);
		LOGGER.debug("listAssetInventory()", "order:", order);
		String schema = this.getMySchema();	
		List<DmmAssetStacktakeListDTO> results = null;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("type.NAME", DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("status.ITEM_NAME", DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_STATUS_NAME.getValue());
			sqlStatement.addSelectClause("temp.counter", DmmAssetStacktakeListDTO.ATTRIBUTE.TOTAL_STOCKTACK.getValue());
			sqlStatement.addSelectClause("temp.padding", DmmAssetStacktakeListDTO.ATTRIBUTE.NO_STOCKTACK.getValue());
			sqlStatement.addSelectClause("temp.ok", DmmAssetStacktakeListDTO.ATTRIBUTE.ALREADY_STOCKTACK.getValue());
			sqlStatement.addSelectClause("temp.more", DmmAssetStacktakeListDTO.ATTRIBUTE.OVERAGE.getValue());
			sqlStatement.addSelectClause("temp.less", DmmAssetStacktakeListDTO.ATTRIBUTE.ASSETL_LESS.getValue());
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("(select list.ASSET_TYPE_ID, list.ASSET_STATUS, count(1) as counter, sum(case list.STOCKTAKE_STATUS when :noStocktack then 1 else 0 end) as padding, ")
				.append("sum(case list.STOCKTAKE_STATUS when :alreadyStocktack then 1 else 0 end) as ok, sum(case list.STOCKTAKE_STATUS when :overage then 1 else 0 end) as more,")
				.append("sum(case list.STOCKTAKE_STATUS when :assetlLess then 1 else 0 end) as less from ").append(schema).append(".DMM_ASSET_STOCKTAKE_LIST list where list.STOCKTACK_ID = :stocktackId ");
			stringBuffer.append("group by list.ASSET_TYPE_ID, list.ASSET_STATUS) temp left join ").append(schema).append(".DMM_ASSET_TYPE type on type.ASSET_TYPE_ID = temp.ASSET_TYPE_ID left join ")
				.append(schema).append(".BASE_PARAMETER_ITEM_DEF status on status.BPTD_CODE = :assetStatus and status.ITEM_VALUE = temp.ASSET_STATUS");
			sqlStatement.addFromExpression(stringBuffer.toString());
			/*sqlStatement.addFromExpression("(select list.ASSET_TYPE_ID, list.ASSET_STATUS, count(1) as counter," + 
						"sum(case list.STOCKTAKE_STATUS when :noStocktack then 1 else 0 end) as padding," + 
						"sum(case list.STOCKTAKE_STATUS when :alreadyStocktack then 1 else 0 end) as ok," + 
						"sum(case list.STOCKTAKE_STATUS when :overage then 1 else 0 end) as more, "+
						"sum(case list.STOCKTAKE_STATUS when :assetlLess then 1 else 0 end) as less " + 
						"from " + schema + ".DMM_ASSET_STOCKTAKE_LIST list " +
						"where list.STOCKTACK_ID = :stocktackId "+
						"group by list.ASSET_TYPE_ID, list.ASSET_STATUS) temp " +
						" left join " + schema + ".DMM_ASSET_TYPE type on type.ASSET_TYPE_ID = temp.ASSET_TYPE_ID" +
						" left join " + schema + ".BASE_PARAMETER_ITEM_DEF status on status.BPTD_CODE = :assetStatus and status.ITEM_VALUE = temp.ASSET_STATUS");*/	
			//設置排序表達式
			if (!StringUtils.hasText(sort) && !StringUtils.hasText(order)) {
				sqlStatement.setOrderByExpression("type.name,status.ITEM_VALUE");
			} else if (StringUtils.hasText(sort) && StringUtils.hasText(order)) {
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append(sort).append(IAtomsConstants.MARK_SPACE).append(order);
				sqlStatement.setOrderByExpression(stringBuffer.toString());
				//sqlStatement.setOrderByExpression(sort + IAtomsConstants.MARK_SPACE + order);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmAssetStacktakeListDTO.class);
			//分頁
			sqlQueryBean.setPageSize(pageSize);
			sqlQueryBean.setStartPage(currentPage - 1);
			sqlQueryBean.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.NO_STOCKTACK.getValue(), IAtomsConstants.PARAM_NO_STOCKTAKE);
			sqlQueryBean.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.ALREADY_STOCKTACK.getValue(), IAtomsConstants.PARAM_ALREADY_STOCKTAKE);
			sqlQueryBean.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.OVERAGE.getValue(), IAtomsConstants.PARAM_MORE_STOCKTAKE);
			sqlQueryBean.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSETL_LESS.getValue(), IAtomsConstants.PARAM_LESS_STOCKTAKE);
			sqlQueryBean.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.STOCKTACK_ID.getValue(), stocktackId);
			sqlQueryBean.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_STATUS.getValue(), IATOMS_PARAM_TYPE.ASSET_STATUS.getCode());
			LOGGER.debug("listAssetInventory()", "sql:", sqlQueryBean.toString());
			results = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return results;
		} catch (Exception e) {
			LOGGER.error("DmmAssetStacktakeListDAO.listAssetInventory()", "is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeListDAO#getListCount(java.lang.String)
	 */
	@Override
	public Integer getListCount(String stocktackId, boolean isComplete) throws DataAccessException {
		LOGGER.debug("getListCount()", "stocktackId:", stocktackId);
		LOGGER.debug("getListCount()", "isComplete:", String.valueOf(isComplete));
		String schema = this.getMySchema();	
		try {
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("SELECT bpid1.ITEM_NAME AS ").append(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_STATUS_NAME.getValue()).append(",");
			sqlQueryBean.append("type.NAME AS ").append(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_TYPE_NAME.getValue()).append(",");
			sqlQueryBean.append("(case when info.COMPLETE_STATUS = :completeStatus then 0 else total.padding end) as ").append(DmmAssetStacktakeListDTO.ATTRIBUTE.NO_STOCKTACK.getValue()).append(",");
			sqlQueryBean.append("total.ok as ").append(DmmAssetStacktakeListDTO.ATTRIBUTE.ALREADY_STOCKTACK.getValue()).append(",");
			sqlQueryBean.append("total.more as ").append(DmmAssetStacktakeListDTO.ATTRIBUTE.OVERAGE.getValue()).append(",");
			sqlQueryBean.append("total.total as ").append(DmmAssetStacktakeListDTO.ATTRIBUTE.TOTAL_STOCKTACK.getValue());
			//盤點完成就去統計盤差數
			if(isComplete){
				sqlQueryBean.append(", total.less as ").append(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSETL_LESS.getValue());
			}
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(" from (select list.asset_type_id assetCategory, list.asset_status assetStatus, ").append("sum(case list.STOCKTAKE_STATUS when :noStocktack then 1 else 0 end) as padding, ")
				.append("sum(case list.STOCKTAKE_STATUS when :alreadyStocktack then 1 else 0 end) as ok, sum(case list.STOCKTAKE_STATUS when :overage then 1 else 0 end) as more, ");
			sqlQueryBean.append(stringBuffer.toString());
			/*sqlQueryBean.append(" from (select list.asset_type_id assetCategory, list.asset_status assetStatus, " +
					"sum(case list.STOCKTAKE_STATUS when :noStocktack then 1 else 0 end) as padding, " +
					"sum(case list.STOCKTAKE_STATUS when :alreadyStocktack then 1 else 0 end) as ok, " +
					"sum(case list.STOCKTAKE_STATUS when :overage then 1 else 0 end) as more, ");*/
			//盤點完成就去統計盤差數
			if (isComplete) {
				sqlQueryBean.append(" sum(case list.STOCKTAKE_STATUS when :assetlLess then 1 else 0 end) as less, ");
			}
			sqlQueryBean.append(" count(*) as total");
			stringBuffer.delete(0, stringBuffer.length());
			stringBuffer.append(" from ").append(schema).append(".DMM_ASSET_STOCKTAKE_LIST list where 1=1 ");
			sqlQueryBean.append(stringBuffer.toString());
			//sqlQueryBean.append(" from " + schema + ".DMM_ASSET_STOCKTAKE_LIST list where 1=1 ");
			sqlQueryBean.append(" and list.STOCKTACK_ID = :stocktackId");
			sqlQueryBean.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.STOCKTACK_ID.getValue(), stocktackId);
			stringBuffer.delete(0, stringBuffer.length());
			stringBuffer.append(" group by list.asset_type_id, list.asset_status) total left join ").append(schema).append(".DMM_ASSET_TYPE type ON type.asset_type_id = total.assetCategory left join ")
				.append(schema).append(".DMM_ASSET_STOCKTAKE_INFO info  on info.STOCKTACK_ID = :stocktackId left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid1 ON bpid1.ITEM_VALUE = total.assetStatus  AND bpid1.BPTD_CODE =:assetStatus");
			sqlQueryBean.append(stringBuffer.toString());
			//sqlQueryBean.append(" group by list.asset_type_id, list.asset_status) total left join " + schema + ".DMM_ASSET_TYPE type ON type.asset_type_id = total.assetCategory ");
			/*sqlQueryBean.append(" LEFT JOIN " + schema + ".DMM_ASSET_STOCKTAKE_INFO info ");
			sqlQueryBean.append(" on info.STOCKTACK_ID = :stocktackId");*/
			sqlQueryBean.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.STOCKTACK_ID.getValue(), stocktackId);
			//sqlQueryBean.append(" LEFT JOIN " + schema + ".BASE_PARAMETER_ITEM_DEF bpid1 ON bpid1.ITEM_VALUE = total.assetStatus  AND bpid1.BPTD_CODE =:assetStatus");
			sqlQueryBean.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.NO_STOCKTACK.getValue(), IAtomsConstants.PARAM_NO_STOCKTAKE);
			sqlQueryBean.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.ALREADY_STOCKTACK.getValue(), IAtomsConstants.PARAM_ALREADY_STOCKTAKE);
			if (isComplete) {
				sqlQueryBean.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSETL_LESS.getValue(), IAtomsConstants.PARAM_LESS_STOCKTAKE);
			}
			sqlQueryBean.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.OVERAGE.getValue(), IAtomsConstants.PARAM_MORE_STOCKTAKE);
			sqlQueryBean.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_STATUS.getValue(), IATOMS_PARAM_TYPE.ASSET_STATUS.getCode());
			sqlQueryBean.setParameter(DmmAssetStacktakeInfoDTO.ATTRIBUTE.COMPLETE_STATUS.getValue(), IAtomsConstants.YES);
			AliasBean aliasBean = new AliasBean(DmmAssetStacktakeListDTO.class);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_STATUS_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_TYPE_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.NO_STOCKTACK.getValue(), IntegerType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.ALREADY_STOCKTACK.getValue(), IntegerType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.OVERAGE.getValue(), IntegerType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.TOTAL_STOCKTACK.getValue(), IntegerType.INSTANCE);
			if (isComplete) {
				aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSETL_LESS.getValue(), IntegerType.INSTANCE);
			}
			//分頁
			LOGGER.debug("getListCount()", "SQL---------->", sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)){
				return result.size();
			}
		} catch (Exception e) {
			LOGGER.error("DmmAssetStacktakeListDAO.getListCount()", " is error:", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ASSET_STOCKTACK_LIST)} ,e);
		}
		return 0;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeListDAO#getCategoryByStocktackId(java.lang.String)
	 */
	@Override
	public List<AssetStocktakeCategroyDTO> getCategoryByStocktackId(String stocktackId) throws DataAccessException {
		LOGGER.debug("getCategoryByStocktackId()", "parameter : stocktackId = ", stocktackId);
		List<AssetStocktakeCategroyDTO> result =null;
		try {
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append(" select a.STOCKTACK_ID as stocktackId,a.ASSET_TYPE_ID as assetTypeId ");
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(" from ").append(schema).append(".DMM_ASSET_STOCKTAKE_CATEGORY a where 1=1");
			sql.append(stringBuffer.toString());
			//sql.append(" from " + schema + ".DMM_ASSET_STOCKTAKE_CATEGORY a ");
			//sql.append(" where 1=1");
			if (StringUtils.hasText(stocktackId)) {
				sql.append("　and a.STOCKTACK_ID =:stocktackId");
				sql.setParameter(AssetStocktakeCategroyDTO.ATTRIBUTE.STOCKTACK_ID.getValue(), stocktackId);
			}
			LOGGER.debug("getCategoryByStocktackId()", "SQL---------->", sql.toString());
			AliasBean aliasBean = new AliasBean(AssetStocktakeCategroyDTO.class);
			aliasBean.addScalar(AssetStocktakeCategroyDTO.ATTRIBUTE.STOCKTACK_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AssetStocktakeCategroyDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), StringType.INSTANCE);
			result = this.getDaoSupport().findByNativeSql(sql, aliasBean);
		} catch (Exception e) {
			LOGGER.error("DmmAssetStacktakeListDAO", ".getListAssetStockDTOs()　is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ASSET_STOCKTACK_CATEGROY)} ,e);
		}
		return result;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeListDAO#getStatusByStocktackId(java.lang.String)
	 */
	@Override
	public List<AssetStocktakeStatusDTO> getStatusByStocktackId(String stocktackId) throws DataAccessException {
		LOGGER.debug("getStatusByStocktackId()", "parameter : stocktackId = ", stocktackId);
		List<AssetStocktakeStatusDTO> result = null;
		try {
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append(" select status.STOCKTACK_ID as stocktackId,status.ASSET_STATUS as assetStatus ");
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(" from ").append(schema).append(".DMM_ASSET_STOCKTAKE_STATUS status where 1=1");
			/*sql.append(" from " + schema + ".DMM_ASSET_STOCKTAKE_STATUS status ");
			sql.append(" where 1=1");*/
			sql.append(stringBuffer.toString());
			if (StringUtils.hasText(stocktackId)) {
				sql.append(" and status.STOCKTACK_ID =:stocktackId");
				sql.setParameter(AssetStocktakeStatusDTO.ATTRIBUTE.STOCKTACK_ID.getValue(), stocktackId);
			}
			LOGGER.debug("getStatusByStocktackId()", "SQL---------->", sql.toString());
			AliasBean aliasBean = new AliasBean(AssetStocktakeStatusDTO.class);
			aliasBean.addScalar(AssetStocktakeStatusDTO.ATTRIBUTE.STOCKTACK_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AssetStocktakeStatusDTO.ATTRIBUTE.ASSET_STATUS.getValue(), StringType.INSTANCE);
			result = this.getDaoSupport().findByNativeSql(sql, aliasBean);
		} catch (Exception e) {
			LOGGER.error("DmmAssetStacktakeListDAO", ".getListAssetStockDTOs()　is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ASSET_STOCKTACK_STATUS)} ,e);
		}
		return result;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeListDAO#getAssetByStocktackId(java.lang.String)
	 */
	@Override
	public List<DmmAssetStacktakeListDTO> getAssetByStocktackId(String stocktackId, String serialNumber, String propertyId)throws DataAccessException {
		LOGGER.debug("getAssetByStocktackId()", "parameter : stocktackId = ", stocktackId);
		LOGGER.debug("getAssetByStocktackId()", "parameter : serialNumber = " + serialNumber);
		LOGGER.debug("getAssetByStocktackId()", "parameter : propertyId = ", propertyId);
		List<DmmAssetStacktakeListDTO> result = null;
		try {
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append(" select list.ID as tackId ,list.STOCKTACK_ID as stocktackId,");
			sql.append(" list.SERIAL_NUMBER as serialNumber,list.REMARK as remark,");
			sql.append(" list.UPDATED_BY_ID as updatedById,list.UPDATED_BY_NAME as updatedByName,");
			sql.append(" list.UPDATED_DATE as updatedDate,");
			sql.append(" list.ASSET_TYPE_ID as assetTypeId,list.ASSET_STATUS as assetStatus,");
			sql.append(" list.STOCKTAKE_STATUS as stocktakeStatus");
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(" from ").append(schema).append(".DMM_ASSET_STOCKTAKE_LIST list ");
			//sql.append(" from " + schema + ".DMM_ASSET_STOCKTAKE_LIST list ");
			if (StringUtils.hasText(propertyId)) {
				stringBuffer.append(" left join ").append(schema).append(".DMM_REPOSITORY repo on list.SERIAL_NUMBER = repo.SERIAL_NUMBER ");
				sql.append(" left join " + schema + ".DMM_REPOSITORY repo on list.SERIAL_NUMBER = repo.SERIAL_NUMBER");
			}
			sql.append(stringBuffer.toString());
			sql.append(" where 1=1");
			if (StringUtils.hasText(stocktackId)) {
				sql.append("　and list.STOCKTACK_ID =:stocktackId");
				sql.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.STOCKTACK_ID.getValue(), stocktackId);
			}
			if (StringUtils.hasText(serialNumber)) {
				sql.append("　and list.SERIAL_NUMBER =:serialNumber");
				sql.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), serialNumber);
			}
			if (StringUtils.hasText(propertyId)) {
				sql.append(" AND repo.PROPERTY_ID =:propertyId");
				sql.setParameter(DmmRepositoryDTO.ATTRIBUTE.PROPERTY_ID.getValue(), propertyId);
			}
			LOGGER.debug("getAssetByStocktackId()", "SQL---------->", sql.toString());
			AliasBean aliasBean = new AliasBean(DmmAssetStacktakeListDTO.class);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.TACK_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.STOCKTACK_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.REMARK.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.UPDATED_BY_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.UPDATED_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_STATUS.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.STOCKTAKE_STATUS.getValue(), IntegerType.INSTANCE);
			result = this.getDaoSupport().findByNativeSql(sql, aliasBean);
		} catch (Exception e) {
			LOGGER.error("DmmAssetStacktakeListDAO", ".getListAssetStockDTOs()　is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ASSET_STOCKTACK_LIST)} ,e);
		}
		return result;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeListDAO#export(com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetStacktakeFormDTO)
	 */
	@Override
	public List<DmmAssetStacktakeListDTO> export(AssetStacktakeFormDTO formDTO)throws DataAccessException {
		LOGGER.debug("export()", "parameter : formDTO.assetInventoryId = ", formDTO.getAssetInventoryId());
		List<DmmAssetStacktakeListDTO> result = null;
		try {
			String stocktackId = formDTO.getAssetInventoryId();
			//SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			StringBuffer stringBuffer = new StringBuffer();
			sql.append(" select b.STOCKTACK_ID as stocktackId,b.ASSET_TYPE_ID as assetTypeId,");
			sql.append(" b.ASSET_STATUS as assetStatus,b.SERIAL_NUMBER as serialNumberList, type.NAME as assetTypeName,bpid2.ITEM_NAME as assetStatusName,");
			sql.append(" house.NAME as warHouseName,house.CONTACT as contact, isnull(base.ITEM_NAME,'') + house.ADDRESS as address,b.UPDATED_BY_NAME as updatedByName,b.UPDATED_DATE as updatedDate");
			sql.append(" from(");
			sql.append(" select info.STOCKTACK_ID,info.WAR_WAREHOUSE_ID,info.UPDATED_BY_NAME,info.UPDATED_DATE,");
			sql.append(" a.ASSET_TYPE_ID,a.ASSET_STATUS, a.SERIAL_NUMBER");
			sql.append(" from (");
			sql.append(" select t.STOCKTACK_ID, t.ASSET_TYPE_ID, t.ASSET_STATUS,");
			sql.append(" stuff((select ','+rtrim(t1.SERIAL_NUMBER) from ");
			sql.append(" (select list.ASSET_TYPE_ID, list.ASSET_STATUS, list.SERIAL_NUMBER, list.STOCKTACK_ID ");
			stringBuffer.append(" from ").append(schema).append(".DMM_ASSET_STOCKTAKE_LIST list where 1=1 ");
			/*sql.append(" from " + schema + ".DMM_ASSET_STOCKTAKE_LIST list");
			sql.append(" where 1=1");*/
			sql.append(stringBuffer.toString());
			if (StringUtils.hasText(stocktackId)) {
				sql.append(" and list.STOCKTACK_ID =:stocktackId1)");
				sql.setParameter("stocktackId1", stocktackId);	
			}
			sql.append(" t1 where t1.ASSET_TYPE_ID=t.ASSET_TYPE_ID");
			sql.append(" and t1.ASSET_STATUS=t.ASSET_STATUS");
			sql.append(" for xml path('')), 1, 1, '') as SERIAL_NUMBER");
			sql.append(" from (");
			sql.append(" select list.ASSET_TYPE_ID, list.ASSET_STATUS, list.SERIAL_NUMBER, list.STOCKTACK_ID ");
			stringBuffer.delete(0, stringBuffer.length());
			stringBuffer.append(" from ").append(schema).append(".DMM_ASSET_STOCKTAKE_LIST list where 1=1 ");
			/*sql.append(" from " + schema + ".DMM_ASSET_STOCKTAKE_LIST list");
			sql.append(" where 1=1");*/
			sql.append(stringBuffer.toString());
			if (StringUtils.hasText(stocktackId)) {
				sql.append(" and list.STOCKTACK_ID =:stocktackId1) t ");
			}
			sql.append(" group by t.ASSET_TYPE_ID, t.ASSET_STATUS, t.STOCKTACK_ID) a");
			stringBuffer.delete(0, stringBuffer.length());
			stringBuffer.append(" left join ").append(schema).append(".DMM_ASSET_STOCKTAKE_INFO info on a.STOCKTACK_ID = info.STOCKTACK_ID) b left join ").append(schema)
				.append(".DMM_ASSET_TYPE type on b.ASSET_TYPE_ID = type.ASSET_TYPE_ID left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid2 ON bpid2.ITEM_VALUE = b.ASSET_STATUS AND bpid2.BPTD_CODE = :assetStatus left join ");
			stringBuffer.append(schema).append(".BIM_WAREHOUSE house ON b.WAR_WAREHOUSE_ID = house.WAREHOUSE_ID left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF base ON base.ITEM_VALUE = house.location AND base.BPTD_CODE= :location")
				.append(" order by type.name,bpid2.ITEM_ORDER");
			sql.append(stringBuffer.toString());
			/*sql.append(" left join " + schema + ".DMM_ASSET_STOCKTAKE_INFO info on a.STOCKTACK_ID = info.STOCKTACK_ID) b");
			sql.append(" LEFT JOIN " + schema + ".DMM_ASSET_TYPE type on b.ASSET_TYPE_ID = type.ASSET_TYPE_ID ");
			sql.append(" LEFT JOIN " + schema + ".BASE_PARAMETER_ITEM_DEF bpid2");
			sql.append(" ON bpid2.ITEM_VALUE = b.ASSET_STATUS AND bpid2.BPTD_CODE = :assetStatus ");
			sql.append(" LEFT JOIN " + schema + ".BIM_WAREHOUSE house ON b.WAR_WAREHOUSE_ID = house.WAREHOUSE_ID");
			sql.append(" LEFT JOIN " + schema + ".BASE_PARAMETER_ITEM_DEF base");
			sql.append(" ON base.ITEM_VALUE = house.location AND base.BPTD_CODE= :location");
			sql.append(" order by type.name,bpid2.ITEM_ORDER");*/
			LOGGER.debug("export()", "SQL---------->", sql.toString());
			sql.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_STATUS.getValue(), IATOMS_PARAM_TYPE.ASSET_STATUS.getCode());
			sql.setParameter(BimMerchantHeaderDTO.ATTRIBUTE.LOCATION.getValue(), IATOMS_PARAM_TYPE.LOCATION.getCode());
			AliasBean aliasBean = new AliasBean(DmmAssetStacktakeListDTO.class);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.SERIAL_NUMBER_LIST.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_TYPE_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.UPDATED_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_STATUS.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_STATUS_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.STOCKTACK_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.ADDRESS.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.CONTACT.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.WAR_HOUSE_NAME.getValue(), StringType.INSTANCE);
			result = this.getDaoSupport().findByNativeSql(sql,aliasBean);
		} catch (Exception e) {
			LOGGER.error("DmmAssetStacktakeListDAO", ".export()　is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ASSET_STOCKTACK_LIST)} ,e);
		}
		return result;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeListDAO#export(com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetStacktakeFormDTO)
	 */
	@Override
	public List<DmmAssetStacktakeListDTO> exportSummary(AssetStacktakeFormDTO formDTO)throws DataAccessException {
		LOGGER.debug("exportSummary()", "parameter : formDTO.assetInventoryId = ", formDTO.getAssetInventoryId());
		List<DmmAssetStacktakeListDTO> result = null;
		try {
			String stocktackId = formDTO.getAssetInventoryId();
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			StringBuffer stringBuffer = new StringBuffer();
			sql.append(" select b.STOCKTACK_ID as stocktackId, b.ASSET_TYPE_ID as assetTypeId,");
			sql.append(" (case when info.COMPLETE_STATUS = :completeStatus then NULL else b.noStocktack end) as noStocktackList,b.alealystocktack as alreadyStocktackList, b.morestocktack as overageList, (case when info.COMPLETE_STATUS = 'N' then NULL else b.lesstocktack end) as assetlLessList, type.NAME as assetTypeName,");
			sql.append(" w.NAME as warHouseName, w.CONTACT as contact, isnull(base.ITEM_NAME,'') + w.ADDRESS as address, b.UPDATED_BY_NAME as updatedByName, b.UPDATED_DATE as updatedDate");
			sql.append(" from(");
			sql.append(" select i.STOCKTACK_ID,i.WAR_WAREHOUSE_ID,i.UPDATED_BY_NAME,i.UPDATED_DATE,");
			sql.append(" a.ASSET_TYPE_ID, a.nostocktack, a.alealystocktack,a.morestocktack,a.lesstocktack");
			sql.append(" from (");
			sql.append(" select t.STOCKTACK_ID, t.ASSET_TYPE_ID,");
			sql.append(" stuff((select ',' + RTRIM(t1.SERIAL_NUMBER) from");
			sql.append(" (select asl.ASSET_TYPE_ID, asl.SERIAL_NUMBER, asl.STOCKTACK_ID ");
			stringBuffer.append(" from ").append(schema).append(".DMM_ASSET_STOCKTAKE_LIST asl where asl.STOCKTAKE_STATUS = :alreadyStocktack");
			/*sql.append(" from " + schema + ".DMM_ASSET_STOCKTAKE_LIST asl");
			sql.append(" where asl.STOCKTAKE_STATUS = :alreadyStocktack");*/
			sql.append(stringBuffer.toString());
			if (StringUtils.hasText(stocktackId)) {
				sql.append(" and asl.STOCKTACK_ID =:stocktackId)");
			}
			sql.append(" t1 where t1.ASSET_TYPE_ID = t.ASSET_TYPE_ID");
			sql.append(" FOR XML path('')), 1, 1, '') as alealystocktack ,");
			sql.append(" stuff((select ',' + RTRIM(t1.SERIAL_NUMBER) from");
			sql.append(" (select asl.ASSET_TYPE_ID, asl.SERIAL_NUMBER, asl.STOCKTACK_ID ");
			stringBuffer.delete(0, stringBuffer.length());
			stringBuffer.append(" from ").append(schema).append(".DMM_ASSET_STOCKTAKE_LIST asl where asl.STOCKTAKE_STATUS = :noStocktack");
			/*sql.append(" from " + schema + ".DMM_ASSET_STOCKTAKE_LIST asl");
			sql.append(" where asl.STOCKTAKE_STATUS = :noStocktack");*/
			sql.append(stringBuffer.toString());
			if (StringUtils.hasText(stocktackId)) {
				sql.append(" and asl.STOCKTACK_ID =:stocktackId)");
			}
			sql.append(" t1 where t1.ASSET_TYPE_ID = t.ASSET_TYPE_ID");
			sql.append(" FOR XML path('')), 1, 1, '') as nostocktack,");
			sql.append(" stuff((select ',' + RTRIM(t1.SERIAL_NUMBER) from");
			sql.append(" (select asl.ASSET_TYPE_ID, asl.SERIAL_NUMBER, asl.STOCKTACK_ID ");
			stringBuffer.delete(0, stringBuffer.length());
			stringBuffer.append(" from ").append(schema).append(".DMM_ASSET_STOCKTAKE_LIST asl where asl.STOCKTAKE_STATUS = :assetlLess");
			/*sql.append(" from " + schema + ".DMM_ASSET_STOCKTAKE_LIST asl");
			sql.append(" where asl.STOCKTAKE_STATUS = :assetlLess");*/
			sql.append(stringBuffer.toString());
			if (StringUtils.hasText(stocktackId)) {
				sql.append(" and asl.STOCKTACK_ID =:stocktackId)");
			}
			sql.append(" t1 where t1.ASSET_TYPE_ID = t.ASSET_TYPE_ID");
			sql.append(" FOR XML path('')), 1, 1, '') as lesstocktack ,");
			sql.append(" stuff((select ',' + RTRIM(t1.SERIAL_NUMBER) from");
			sql.append(" (select asl.ASSET_TYPE_ID, asl.SERIAL_NUMBER, asl.STOCKTACK_ID ");
			stringBuffer.delete(0, stringBuffer.length());
			stringBuffer.append(" from ").append(schema).append(".DMM_ASSET_STOCKTAKE_LIST asl where asl.STOCKTAKE_STATUS = :overage");
			sql.append(stringBuffer.toString());
			/*sql.append(" from " + schema + ".DMM_ASSET_STOCKTAKE_LIST asl");
			sql.append(" where asl.STOCKTAKE_STATUS = :overage");*/
			if (StringUtils.hasText(stocktackId)) {
				sql.append(" and asl.STOCKTACK_ID =:stocktackId)");
			}
			sql.append(" t1 where t1.ASSET_TYPE_ID = t.ASSET_TYPE_ID");
			sql.append(" for xml path('')), 1, 1, '') as morestocktack ");
			sql.append(" from (");
			sql.append(" select asl.ASSET_TYPE_ID, asl.SERIAL_NUMBER, asl.STOCKTACK_ID ");
			stringBuffer.delete(0, stringBuffer.length());
			stringBuffer.append(" from ").append(schema).append(".DMM_ASSET_STOCKTAKE_LIST asl where 1=1 ");
			sql.append(stringBuffer.toString());
			/*sql.append(" from " + schema + ".DMM_ASSET_STOCKTAKE_LIST asl");
			sql.append(" where 1=1");*/
			if (StringUtils.hasText(stocktackId)) {
				sql.append(" and asl.STOCKTACK_ID =:stocktackId)");
			}
			sql.append(" t group by t.ASSET_TYPE_ID, t.STOCKTACK_ID) a");
			stringBuffer.delete(0, stringBuffer.length());
			stringBuffer.append(" left join ").append(schema).append(".DMM_ASSET_STOCKTAKE_INFO i on a.STOCKTACK_ID = i.STOCKTACK_ID) b left join ")
				.append(schema).append(".DMM_ASSET_TYPE type on b.ASSET_TYPE_ID = type.ASSET_TYPE_ID left join ").append(schema).append(".BIM_WAREHOUSE w ON b.WAR_WAREHOUSE_ID = w.WAREHOUSE_ID left join ")
				.append(schema).append(".DMM_ASSET_STOCKTAKE_INFO info ON info.STOCKTACK_ID = :stocktackId left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF base")
				.append(" ON base.ITEM_VALUE = w.location AND base.BPTD_CODE= :location order by type.NAME ");
			sql.append(stringBuffer.toString());
			sql.setParameter("location", IAtomsConstants.LOCATION);
			/*sql.append(" left join " + schema + ".DMM_ASSET_STOCKTAKE_INFO i on a.STOCKTACK_ID = i.STOCKTACK_ID) b");
			sql.append(" LEFT JOIN " + schema + ".DMM_ASSET_TYPE type on b.ASSET_TYPE_ID = type.ASSET_TYPE_ID ");
			sql.append(" LEFT JOIN " + schema + ".BIM_WAREHOUSE w ON b.WAR_WAREHOUSE_ID = w.WAREHOUSE_ID");
			sql.append(" LEFT JOIN " + schema + ".DMM_ASSET_STOCKTAKE_INFO info ON info.STOCKTACK_ID = :stocktackId");
			sql.append(" LEFT JOIN " + schema + ".BASE_PARAMETER_ITEM_DEF base");
			sql.append(" ON base.ITEM_VALUE = w.location AND base.BPTD_CODE='LOCATION' order by type.NAME ");*/
			if (StringUtils.hasText(stocktackId)) {
				sql.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.STOCKTACK_ID.getValue(), stocktackId);
			}
			LOGGER.debug("exportSummary()", "SQL---------->", sql.toString());
			sql.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.NO_STOCKTACK.getValue(), IAtomsConstants.PARAM_NO_STOCKTAKE);
			sql.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.ALREADY_STOCKTACK.getValue(), IAtomsConstants.PARAM_ALREADY_STOCKTAKE);
			sql.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSETL_LESS.getValue(), IAtomsConstants.PARAM_LESS_STOCKTAKE);
			sql.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.OVERAGE.getValue(), IAtomsConstants.PARAM_MORE_STOCKTAKE);
			sql.setParameter(DmmAssetStacktakeInfoDTO.ATTRIBUTE.COMPLETE_STATUS.getValue(), IAtomsConstants.YES);
			AliasBean aliasBean = new AliasBean(DmmAssetStacktakeListDTO.class);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.NO_STOCKTACK_LIST.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.ALREADY_STOCKTACK_LIST.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.OVERAGE_LIST.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSETL_LESS_LIST.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.ASSET_TYPE_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.UPDATED_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.STOCKTACK_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.ADDRESS.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.CONTACT.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmAssetStacktakeListDTO.ATTRIBUTE.WAR_HOUSE_NAME.getValue(), StringType.INSTANCE);
			result = this.getDaoSupport().findByNativeSql(sql,aliasBean);
		} catch (Exception e) {
			LOGGER.error("DmmAssetStacktakeListDAO", ".exportSummary()　is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ASSET_STOCKTACK_LIST)} ,e);
		}
		return result;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeListDAO#Check(java.lang.String)
	 */
	@Override
	public Boolean Check(String sendSerialNumber) throws DataAccessException {
		LOGGER.debug("Check()", "sendSerialNumber", sendSerialNumber);
		String schema = this.getMySchema();	
		SqlStatement sqlStatement = new SqlStatement();
		try {
			//查詢語句
			sqlStatement.addSelectClause(" count(*) ");
			sqlStatement.addFromExpression(schema + ".DMM_REPOSITORY list");
			if (StringUtils.hasText(sendSerialNumber)) {
				sqlStatement.addWhereClause("list.SERIAL_NUMBER = :serialNumber or list.PROPERTY_ID = :serialNumber");
			}
//			sqlStatement.addWhereClause("isnull(list.DELETED,'N') = :deleted", IAtomsConstants.NO);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter(DmmAssetStacktakeListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), sendSerialNumber);
			//打印sql語句
			LOGGER.debug("Check()", "SQL---------->", sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (result.get(0).intValue() > 0) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.error("DmmAssetStacktakeListDAO", ".Check() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return false;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeListDAO#checkList(java.lang.String, java.lang.String)
	 */
	@Override
	public Boolean checkList(String stocktackId, String sendSerialNumber) throws DataAccessException {
		LOGGER.debug("checkList()", "stocktackId = ", stocktackId);
		LOGGER.debug("checkList()", "sendSerialNumber = ", sendSerialNumber);
		String schema = this.getMySchema();	
		SqlStatement sqlStatement = new SqlStatement();
		try {
			//查詢語句
			sqlStatement.addSelectClause(" count(*) ");
			sqlStatement.addFromExpression(schema + ".DMM_ASSET_STOCKTAKE_LIST list");
			if (StringUtils.hasText(sendSerialNumber)) {
				sqlStatement.addWhereClause("list.SERIAL_NUMBER = :serialNumber", sendSerialNumber);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			//打印sql語句
			LOGGER.debug("checkList()", "SQL---------->", sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (result.get(0).intValue() > 0) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.error("DmmAssetStacktakeListDAO", ".checkList() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return false;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeListDAO#saveStocktackList(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@Override
	public void saveStocktackList(String listId, String stocktackId, String warWarehouseId, String assetTypeId, String assetStatus, Integer stocktackStatus, String userId, String userName) throws DataAccessException {
		LOGGER.debug("saveStocktackList()", "listId = ", listId);
		LOGGER.debug("saveStocktackList()", "stocktackId = ", stocktackId);
		LOGGER.debug("saveStocktackList()", "stocktackStatus = ", stocktackStatus);
		try {
			//得到schema
			String schema = this.getMySchema();
			//創建SqlStatement
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("insert into ").append(schema).append(".DMM_ASSET_STOCKTAKE_LIST(");
			sqlQueryBean.append("ID, STOCKTACK_ID, SERIAL_NUMBER, ASSET_TYPE_ID, ASSET_STATUS, STOCKTAKE_STATUS, UPDATED_BY_ID, UPDATED_BY_NAME, UPDATED_DATE)");
			sqlQueryBean.append(" select ".concat(IAtomsConstants.SINGLE_QUOTATION_MARKS).concat(listId).concat(IAtomsConstants.MARK_QUOTES).concat("+").concat(IAtomsConstants.SINGLE_QUOTATION_MARKS).concat(IAtomsConstants.MARK_MIDDLE_LINE).concat(IAtomsConstants.MARK_QUOTES).concat("+").concat("cast(ROW_NUMBER()over (ORDER BY repo.ASSET_ID) AS VARCHAR)")).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(":stocktackId").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("repo.SERIAL_NUMBER").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("repo.ASSET_TYPE_ID").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("repo.STATUS").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(":completeStatus").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(":updatedById").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(":updatedByName").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("getDate()");
			sqlQueryBean.append(" from ").append(schema).append(".DMM_REPOSITORY repo");
			sqlQueryBean.append(" where repo.WAREHOUSE_ID = :warWarehouseId");
//			sqlQueryBean.append(" and isnull(repo.DELETED,'N') = :deleted");
			sqlQueryBean.append(" and repo.ASSET_TYPE_ID in (").append(assetTypeId).append(IAtomsConstants.MARK_BRACKETS_RIGHT);
			sqlQueryBean.append(" and repo.STATUS in (").append(assetStatus).append(IAtomsConstants.MARK_BRACKETS_RIGHT);
			sqlQueryBean.setParameter(DmmAssetStacktakeInfoDTO.ATTRIBUTE.STOCKTACK_ID.getValue(), stocktackId);
			sqlQueryBean.setParameter(DmmAssetStacktakeInfoDTO.ATTRIBUTE.COMPLETE_STATUS.getValue(), stocktackStatus);
			sqlQueryBean.setParameter(DmmAssetStacktakeInfoDTO.ATTRIBUTE.UPDATED_BY_ID.getValue(), userId);
			sqlQueryBean.setParameter(DmmAssetStacktakeInfoDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue(), userName);
			sqlQueryBean.setParameter(DmmAssetStacktakeInfoDTO.ATTRIBUTE.WAR_WAREHOUSE_ID.getValue(), warWarehouseId);
//			sqlQueryBean.setParameter(DmmRepositoryDTO.ATTRIBUTE.DELETED.getValue(), IAtomsConstants.NO);
			LOGGER.debug("saveStocktackList()", "sql : ", sqlQueryBean.toString());
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("DmmAssetStacktakeListDAO", ".saveStocktackList() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeListDAO#deleteStocktackList(java.lang.String)
	 */
	@Override
	public void deleteStocktackList(String stocktackId) throws DataAccessException {
		LOGGER.debug("deleteStocktackList()", "stocktackId : ", stocktackId);
		try {
			//得到schema
			String schema = this.getMySchema();
			//創建SqlStatement
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("delete from " + schema + ".DMM_ASSET_STOCKTAKE_LIST WHERE STOCKTACK_ID =:stocktackId");
			sqlQueryBean.setParameter("stocktackId", stocktackId);
			LOGGER.debug("deleteStocktackList()", "sql : ", sqlQueryBean.toString());
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("DmmAssetStacktakeListDAO", ".deleteStocktackList() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetStacktakeListDAO#completeStocktack(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void completeStocktack(String stocktackId, String id, String name) throws DataAccessException {
		LOGGER.debug("completeStocktack()", "stocktackId : ", stocktackId);
		LOGGER.debug("completeStocktack()", "id : ", id);
		LOGGER.debug("completeStocktack()", "name ：", name);
		try {
			//得到schema
			String schema = this.getMySchema();
			//創建SqlStatement
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("update " + schema + ".DMM_ASSET_STOCKTAKE_LIST set STOCKTAKE_STATUS =:lessStocktake ");
			sqlQueryBean.append("where STOCKTACK_ID =:stocktackId and STOCKTAKE_STATUS =:noStocktake");
			sqlQueryBean.setParameter("stocktackId", stocktackId);
			sqlQueryBean.setParameter("lessStocktake", IAtomsConstants.PARAM_LESS_STOCKTAKE);
			sqlQueryBean.setParameter("noStocktake", IAtomsConstants.PARAM_NO_STOCKTAKE);
			LOGGER.debug("completeStocktack()", "sql : ", sqlQueryBean.toString());
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("DmmAssetStacktakeListDAO", ".completeStocktack() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	
}
