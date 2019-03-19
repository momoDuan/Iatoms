package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
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
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO;
import com.cybersoft4u.xian.iatoms.common.reportsetting.AssetMaintainFeeSetting;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryMonthlyDAO;
/**
 * Purpose: 設備庫存歷史月檔的dao
 * @author Hermanwang
 * @since  JDK 1.6
 * @date   2017/8/23
 * @MaintenancePersonnel Hermanwang
 */
public class DmmRepositoryHistoryMonthlyDAO extends GenericBaseDAO implements IDmmRepositoryHistoryMonthlyDAO {
	/**
	 * 系統日志記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(DmmRepositoryHistoryMonthlyDAO.class);

	/**
	 * Constructor: 無參構造
	 */
	public DmmRepositoryHistoryMonthlyDAO() {
		super();
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryMonthlyDAO#assetInfoList(java.lang.String, java.lang.String)
	 */
	@Override
	public List<DmmRepositoryDTO> assetInfoList(String yearMonthDay,
			String customerCode, Date startDate, Date endDate) throws DataAccessException {
		LOGGER.error(this.getClass().getName()+".assetInfoList() --> yearMonthDay: "+yearMonthDay);
		LOGGER.error(this.getClass().getName()+".assetInfoList() --> customerCode: "+customerCode);
		List<DmmRepositoryDTO> listDmmRepositoryHistoryDTO= null;
		try {
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append("SELECT");
			sql.append(" ROW_NUMBER() over (order by repos.ASSET_ID) as rowId, ");
			sql.append(" repos.ASSET_ID AS assetId, ");	
			sql.append(" repos.STATUS AS status, ");	
			sql.append(" asset.NAME as name ,");
			sql.append(" mer.MERCHANT_CODE AS merchantCode, ");	
			sql.append(" mer.NAME AS merchantName , ");
			sql.append(" repos.DTID as dtid , ");
			sql.append(" repos.SERIAL_NUMBER AS serialNumber , ");
			sql.append(" (CASE WHEN repos.MA_TYPE = 'BUYOUT' THEN repos.PROPERTY_ID ");
			sql.append(" ELSE repos.SIM_ENABLE_NO END ) AS propertyId , ");
			sql.append(" con.CONTRACT_CODE AS contractCode , ");
			sql.append(" repos.CHECKED_DATE AS checkedDate , ");
			sql.append(" repos.ENABLE_DATE AS enableDate , ");
			sql.append(" repos.UPDATE_DATE  AS updateDate, ");
			sql.append(" statusDef.ITEM_NAME AS statusName , ");
			sql.append(" STUFF( ( SELECT ',' + LTRIM( m.SERIAL_NUMBER ) FROM ").append(schema).append(".DMM_REPOSITORY_HISTORY_MONTHLY m LEFT JOIN dbo.DMM_ASSET_TYPE t ON m.ASSET_TYPE_ID = t.ASSET_TYPE_ID ");
			sql.append(" WHERE m.MONTH_YEAR = :monthYear and m.DTID = repos.DTID and m.DTID is not null and m.DTID <> '' AND m.ASSET_ID <> repos.ASSET_ID ");
			sql.append(" AND t.NAME LIKE :TSAM ");
			sql.append(" FOR XML PATH('') ) , 1 , 1 , '') AS tSam , ");
			sql.append(" header.AO_NAME as aoAame, ");
			sql.append(" STUFF((SELECT ','+ ltrim(bcomm.ITEM_VALUE) FROM dbo.BASE_PARAMETER_ITEM_DEF bcomm,dbo.DMM_ASSET_SUPPORTED_COMM comm where comm.ASSET_TYPE_ID = asset.ASSET_TYPE_ID and comm.COMM_MODE_ID= bcomm.ITEM_VALUE for XML PATH('')), 1, 1, '') as commModeId , ");
			sql.append(" repos.ASSET_TYPE_ID  as assetTypeId");
			sql.append(" INTO  #temp ");
			sql.append(" FROM ").append(schema).append(" .DMM_REPOSITORY_HISTORY_MONTHLY repos ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT con ON repos.CONTRACT_ID = con.CONTRACT_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE asset ON repos.ASSET_TYPE_ID = asset.ASSET_TYPE_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer ON repos.MERCHANT_ID = mer.MERCHANT_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF statusDef ON statusDef.BPTD_CODE =:asset_status AND statusDef.ITEM_VALUE = repos.STATUS ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT_HEADER header ON repos.MERCHANT_HEADER_ID = header.MERCHANT_HEADER_ID ");
			sql.append(" WHERE ");
			//設備類別
			sql.append(" repos.MONTH_YEAR = :monthYear");
			sql.setParameter(DmmRepositoryDTO.ATTRIBUTE.YEAR_MONTH_DAY.getValue(), yearMonthDay);
			//設備代碼
			if(StringUtils.hasText(customerCode)){
				sql.append(" and repos.ASSET_USER = :customerCode");
				sql.setParameter(DmmRepositoryDTO.ATTRIBUTE.CUSTOMER_CODE.getValue(), customerCode);
				LOGGER.debug(this.getClass().getName(), ".assetInfoList() --> customerCode: ", customerCode);
			}
			sql.append(" AND asset.ASSET_CATEGORY =:assetCategoryEDC");
			sql.append(" AND repos.IS_ENABLED =:yes ");
			sql.append(" ; SELECT ROW_NUMBER() over (order by repos.ASSET_ID) as rowId, ");
			sql.append(" repos.ASSET_ID AS assetId, ");	
			sql.append(" repos.STATUS AS status, ");	
			sql.append(" asset.NAME as name, ");
			sql.append(" mer.MERCHANT_CODE AS merchantCode, ");	
			sql.append(" mer.NAME AS merchantName , ");
			sql.append(" repos.DTID as dtid , ");
			sql.append(" repos.SERIAL_NUMBER AS serialNumber , ");
			sql.append(" (CASE WHEN repos.MA_TYPE = 'BUYOUT' THEN repos.PROPERTY_ID ");
			sql.append(" ELSE repos.SIM_ENABLE_NO END ) AS propertyId , ");
			sql.append(" con.CONTRACT_CODE AS contractCode , ");
			sql.append(" repos.CHECKED_DATE AS checkedDate , ");
			sql.append(" repos.ENABLE_DATE AS enableDate , ");
			sql.append(" repos.UPDATE_DATE  AS updateDate, ");
			sql.append(" statusDef.ITEM_NAME AS statusName , ");
			sql.append(" STUFF( ( SELECT ',' + LTRIM( m.SERIAL_NUMBER ) FROM ").append(schema).append(".DMM_REPOSITORY_HISTORY_MONTHLY m LEFT JOIN dbo.DMM_ASSET_TYPE t ON m.ASSET_TYPE_ID = t.ASSET_TYPE_ID ");
			sql.append(" WHERE m.MONTH_YEAR = :monthYear and m.DTID = repos.DTID  and m.DTID is not null and m.DTID <> '' AND m.ASSET_ID <> repos.ASSET_ID ");
			sql.append(" AND t.NAME LIKE :TSAM ");
			sql.append(" FOR XML PATH('') ) , 1 , 1 , '') AS tSam , ");
			sql.append(" header.AO_NAME as aoAame, ");
			sql.append(" STUFF((SELECT ','+ ltrim(bcomm.ITEM_VALUE) FROM dbo.BASE_PARAMETER_ITEM_DEF bcomm,dbo.DMM_ASSET_SUPPORTED_COMM comm where comm.ASSET_TYPE_ID = asset.ASSET_TYPE_ID and comm.COMM_MODE_ID= bcomm.ITEM_VALUE for XML PATH('')), 1, 1, '') as commModeId , ");
			sql.append(" repos.ASSET_TYPE_ID  as assetTypeId");
			sql.append(" into #temp1 ");
			sql.append(" FROM ").append(schema).append(" .DMM_REPOSITORY repos ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT con ON repos.CONTRACT_ID = con.CONTRACT_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE asset ON repos.ASSET_TYPE_ID = asset.ASSET_TYPE_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer ON repos.MERCHANT_ID = mer.MERCHANT_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF statusDef ON statusDef.BPTD_CODE =:asset_status AND statusDef.ITEM_VALUE = repos.STATUS ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT_HEADER header ON repos.MERCHANT_HEADER_ID = header.MERCHANT_HEADER_ID ");
			sql.append(" WHERE ");
			//設備類別
			sql.append(" repos.CASE_COMPLETION_DATE >= :startDate");
			sql.setParameter("startDate", startDate);
			sql.append(" AND repos.CASE_COMPLETION_DATE < :endDate");
			sql.setParameter("endDate", endDate);
			//設備代碼
			if(StringUtils.hasText(customerCode)){
				sql.append(" and repos.ASSET_USER = :customerCode");
				sql.setParameter(DmmRepositoryDTO.ATTRIBUTE.CUSTOMER_CODE.getValue(), customerCode);
				LOGGER.debug(this.getClass().getName(), ".assetInfoList() --> customerCode: ", customerCode);
			}
			sql.append(" AND asset.ASSET_CATEGORY =:assetCategoryEDC");
			sql.append(" AND repos.IS_ENABLED =:yes ");
			sql.append(" AND NOT EXISTS( SELECT * FROM #temp t  WHERE repos.ASSET_ID = t.assetId ); ");
			sql.append(" SELECT * FROM #temp ");
			sql.append(" UNION ALL ");
			sql.append(" select *from #temp1; ");
			sql.append(" drop TABLE #temp; ");
			sql.append(" drop TABLE #temp1; ");
			//排序
			sql.setParameter("asset_status", IATOMS_PARAM_TYPE.ASSET_STATUS.getCode());
			sql.setParameter("yes", IAtomsConstants.YES);
			sql.setParameter("assetCategoryEDC", IAtomsConstants.ASSET_CATEGORY_EDC);
			sql.setParameter("TSAM", "T-SAM" + IAtomsConstants.MARK_PERCENT);
			AliasBean alias = new AliasBean(DmmRepositoryDTO.class);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.ROWID.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.ASSET_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.STATUS.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.MERCHANT_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.PROPERTY_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.CONTRACT_CODE.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.CHECKED_DATE.getValue(), DateType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.ENABLE_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.UPDATE_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.STATUS_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.TSAM.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.AO_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.COMMMODE_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), StringType.INSTANCE);
			listDmmRepositoryHistoryDTO = this.getDaoSupport().findByNativeSql(sql, alias);
			LOGGER.debug(this.getClass().getName(), "assetInfoList() --> sql: ", sql.toString()); 
		} catch (Exception e) {
			LOGGER.error(".assetInfoList() is error", "DAO Exception", e);
			throw new DataAccessException(CoreMessageCode.QUERY_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.REB_IATOMS_TB_NAME_SUPPLIES)}, e);
		}
		return listDmmRepositoryHistoryDTO;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryMonthlyDAO#listFeeAssetList(java.lang.String, java.util.List, java.lang.String, java.sql.Date)
	 */
	@Override
	public List<DmmRepositoryDTO> listFeeAssetList(String customerId, List<String> status, String monthYear, Date queryDate, AssetMaintainFeeSetting assetMaintainFeeSetting) throws DataAccessException {
		LOGGER.debug("listFeeAssetList()", "parameters:customerId=", customerId);
		LOGGER.debug("listFeeAssetList()", "parameters:status=", status.toString());
		LOGGER.debug(".listFeeAssetList()", "parameters:monthYear=", monthYear);
		LOGGER.debug("listFeeAssetList()", "parameters:queryDate=", queryDate.toString());
		List<DmmRepositoryDTO> dmmRepositoryDTOs = null;
		try{
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			StringBuffer buffer = new StringBuffer();
			sqlStatement.addSelectClause("a.merchantCode", DmmRepositoryDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("a.merchantName", DmmRepositoryDTO.ATTRIBUTE.MER_NAME.getValue());
			sqlStatement.addSelectClause("a.warehouseName", DmmRepositoryDTO.ATTRIBUTE.WAREHOUSE_NAME.getValue());
			sqlStatement.addSelectClause("a.dtid", DmmRepositoryDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("a.serialNumber", DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("a.contractCode", DmmRepositoryDTO.ATTRIBUTE.CONTRACT_CODE.getValue());
			sqlStatement.addSelectClause("a.updateDate", DmmRepositoryDTO.ATTRIBUTE.UPDATE_DATE.getValue());
			sqlStatement.addSelectClause("a.propertyId", DmmRepositoryDTO.ATTRIBUTE.PROPERTY_ID.getValue());
			sqlStatement.addSelectClause("a.checkedDate", DmmRepositoryDTO.ATTRIBUTE.CHECKED_DATE.getValue());
			sqlStatement.addSelectClause("a.assetStatus", DmmRepositoryDTO.ATTRIBUTE.STATUS.getValue());
			sqlStatement.addSelectClause("(case when a.rowid <= :maintainNumber then :maintenanceFee1 else :maintenanceFee2 end)", DmmRepositoryDTO.ATTRIBUTE.FEE.getValue());
			buffer.append("(select ");
			buffer.append("mer.MERCHANT_CODE as merchantCode,");
			buffer.append("mer.NAME as merchantName,");
			buffer.append("ware.NAME as warehouseName, ");
			buffer.append("repos.DTID as dtid,");
			buffer.append("repos.SERIAL_NUMBER as serialNumber,");
			buffer.append("con.CONTRACT_CODE as contractCode,");
			buffer.append("repos.UPDATE_DATE as updateDate,");
			buffer.append("(case when repos.MA_TYPE = :maType then repos.PROPERTY_ID else repos.SIM_ENABLE_NO end) as propertyId,");
			buffer.append("repos.CHECKED_DATE as checkedDate,");
			buffer.append("statusDef.ITEM_NAME as assetStatus,");
			buffer.append("ROW_NUMBER() over (order by repos.ASSET_ID) as rowid ");
			buffer.append("from ").append(schema).append(".DMM_REPOSITORY_HISTORY_MONTHLY repos ");
			buffer.append("left join ").append(schema).append(".BIM_CONTRACT con on repos.CONTRACT_ID = con.CONTRACT_ID ");
			//buffer.append("left join ").append(schema).append(".BIM_COMPANY c on c.COMPANY_ID = repos.ASSET_USER ");
			buffer.append("left join ").append(schema).append(".DMM_ASSET_TYPE asset on repos.ASSET_TYPE_ID = asset.ASSET_TYPE_ID ");
			buffer.append("left join ").append(schema).append(".BIM_MERCHANT mer on repos.MERCHANT_ID = mer.MERCHANT_ID ");
			buffer.append("left join ").append(schema).append(".BIM_WAREHOUSE ware on repos.WAREHOUSE_ID = ware.WAREHOUSE_ID ");
			buffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF statusDef on statusDef.BPTD_CODE = :assetStatus and statusDef.ITEM_VALUE = repos.STATUS ");
			buffer.append("where ");
			buffer.append("repos.MONTH_YEAR = :monthYear ");
			// Bug #3232 維護費報表啟用日期調整    (客戶驗收日 + 3年 + 1個月) > 帳務年月月底 / 客戶驗收日 > (帳務年月月底 - 3年 - 1個月)
		//	buffer.append("and repos.CHECKED_DATE < dateadd(month,-37, :date) ");
			buffer.append("and repos.CHECKED_DATE > dateadd(month,-37, :date) ");
			buffer.append("and repos.STATUS not in ( :status )");
			buffer.append("and repos.ASSET_USER = :customerId ");
			buffer.append("and asset.ASSET_CATEGORY = :assetCategory) a");
			sqlStatement.addFromExpression(buffer.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("maType", IAtomsConstants.MA_TYPE_BUYOUT);
			sqlQueryBean.setParameter("monthYear", monthYear);
			sqlQueryBean.setParameter("date", queryDate);
			sqlQueryBean.setParameter("status", status);
			sqlQueryBean.setParameter("customerId", customerId);
			sqlQueryBean.setParameter("assetCategory", IAtomsConstants.ASSET_CATEGORY_EDC);
			sqlQueryBean.setParameter("maintainNumber", assetMaintainFeeSetting.getAssetTotal());
			sqlQueryBean.setParameter("maintenanceFee1", assetMaintainFeeSetting.getFirstMaintainFee());
			sqlQueryBean.setParameter("maintenanceFee2", assetMaintainFeeSetting.getOtherMaintainFee());
			sqlQueryBean.setParameter("assetStatus", IATOMS_PARAM_TYPE.ASSET_STATUS.getCode());
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmRepositoryDTO.class);
			LOGGER.debug(".listFeeAssetList()", "listFeeAssetList:", sqlQueryBean.toString());	
			dmmRepositoryDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(":listFeeAssetList() is error.", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return dmmRepositoryDTOs;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryMonthlyDAO#getTmsTotal(java.lang.String, java.util.List, java.lang.String)
	 */
	@Override
	public Integer getTmsTotal(String monthYear, List<String> discountContract, String customerId) throws DataAccessException {
		Integer number = null;
		LOGGER.debug("getTmsTotal()", "parameters:discountContract=", discountContract.toString());
		LOGGER.debug(".getTmsTotal()", "parameters:monthYear=", monthYear);
		LOGGER.debug(".getTmsTotal()", "parameters:customerId=", customerId);
		try {
			String schema = this.getMySchema();
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("select count(1) ");
			sqlQueryBean.append("from ").append(schema).append(".DMM_REPOSITORY_HISTORY_MONTHLY repos ");
			sqlQueryBean.append("left join ").append(schema).append(".BIM_CONTRACT con on repos.CONTRACT_ID = con.CONTRACT_ID ");
			//sqlQueryBean.append("left join ").append(schema).append(".BIM_COMPANY c on c.COMPANY_ID = con.COMPANY_ID ");
			sqlQueryBean.append("left join ").append(schema).append(".DMM_ASSET_TYPE asset on repos.ASSET_TYPE_ID = asset.ASSET_TYPE_ID ");
			sqlQueryBean.append("where 1=1 ");
			if (StringUtils.hasText(monthYear)) {
				sqlQueryBean.append("and repos.MONTH_YEAR = :monthYear ");
				sqlQueryBean.setParameter("monthYear", monthYear);
			}
			if (!CollectionUtils.isEmpty(discountContract)) {
				sqlQueryBean.append("and con.CONTRACT_CODE in ( :discountContract ) ");
				sqlQueryBean.setParameter("discountContract", discountContract);
			}
			sqlQueryBean.append("and repos.ASSET_USER = :customerId ");
			sqlQueryBean.append("and asset.ASSET_CATEGORY = :assetCategory ");
			sqlQueryBean.setParameter("customerId", customerId);
			sqlQueryBean.setParameter("assetCategory", IAtomsConstants.ASSET_CATEGORY_EDC);
			LOGGER.debug(".getTmsTotal()", "getTmsTotal:", sqlQueryBean.toString());	
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				number = result.get(0);
			}
		} catch (Exception e) {
			LOGGER.error(":getTmsTotal() is error.", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return number;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryMonthlyDAO#feeAssetList(java.lang.String, java.lang.String)
	 */
	@Override
	public List<DmmRepositoryDTO> feeAssetList(String yearMonthDay, String customerCode) throws DataAccessException {
		LOGGER.debug("feeAssetList()", "parameters:customerCode=", customerCode);
		List<DmmRepositoryDTO> dmmRepositoryDTOs = null;
		try{
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			StringBuffer buffer = new StringBuffer();
			sqlStatement.addSelectClause("repos.ASSET_ID", DmmRepositoryDTO.ATTRIBUTE.ASSET_ID.getValue());
			sqlStatement.addSelectClause("repos.CHECKED_DATE", DmmRepositoryDTO.ATTRIBUTE.CHECKED_DATE.getValue());
			sqlStatement.addSelectClause("repos.IS_ENABLED", DmmRepositoryDTO.ATTRIBUTE.IS_ENABLED.getValue());
			sqlStatement.addSelectClause("repos.ENABLE_DATE", DmmRepositoryDTO.ATTRIBUTE.ENABLE_DATE.getValue());
			sqlStatement.addSelectClause("repos.UPDATE_DATE", DmmRepositoryDTO.ATTRIBUTE.UPDATE_DATE.getValue());
			sqlStatement.addSelectClause("repos.STATUS", DmmRepositoryDTO.ATTRIBUTE.STATUS.getValue());
			buffer.append(schema).append(".DMM_REPOSITORY_HISTORY_MONTHLY repos ");
			buffer.append("left join ").append(schema).append(".DMM_ASSET_TYPE asset on repos.ASSET_TYPE_ID = asset.ASSET_TYPE_ID ");
			buffer.append("where ");
			//設備類別
			buffer.append(" repos.MONTH_YEAR = :yearMonthDay");
			//設備代碼
			if(StringUtils.hasText(customerCode)){
				buffer.append(" AND repos.ASSET_USER = :customerCode");
				LOGGER.debug(this.getClass().getName(), ".feeAssetList() --> customerCode: ", customerCode);
			}
			buffer.append(" AND asset.ASSET_CATEGORY =:assetCategoryEDC");
			//buffer.append(" AND repos.IS_ENABLED =:yes");
			//排序
			sqlStatement.addFromExpression(buffer.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter(DmmRepositoryDTO.ATTRIBUTE.CUSTOMER_CODE.getValue(), customerCode);
			sqlQueryBean.setParameter("yearMonthDay", yearMonthDay);
			//sqlQueryBean.setParameter("yes", IAtomsConstants.YES);
			sqlQueryBean.setParameter("assetCategoryEDC", IAtomsConstants.ASSET_CATEGORY_EDC);
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmRepositoryDTO.class);
			LOGGER.debug("feeAssetList()", "listFeeAssetList:", sqlQueryBean.toString());	
			dmmRepositoryDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("feeAssetList() is error.", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return dmmRepositoryDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryMonthlyDAO#listFeeAssetListForGp(java.lang.String)
	 */
	@Override
	public List<DmmRepositoryDTO> listFeeAssetListForGp(String monthYear, List<String> commModeId, String customerId) throws DataAccessException {
		LOGGER.debug("listFeeAssetListForGp()", "parameters:monthYear=", monthYear);
		LOGGER.debug("listFeeAssetListForGp()", "parameters:commModeId=", commModeId.toString());
		LOGGER.debug("listFeeAssetListForGp()", "parameters:customerId=", customerId);
		List<DmmRepositoryDTO> dmmRepositoryDTOs = null;
		try {
			String schema = this.getMySchema();
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			getFeeTemp(monthYear, sqlQueryBean);
			sqlQueryBean.append("select ");
			sqlQueryBean.append("ASSET_TYPE_ID as assetTypeId, ").append("NAME as name, ");
			sqlQueryBean.append("COUNT(1) as total, ");
			sqlQueryBean.append("case when EXISTS( ");
			sqlQueryBean.append("select 1 ");
			sqlQueryBean.append("from ").append(schema);
			sqlQueryBean.append(".DMM_ASSET_SUPPORTED_COMM comm ");
			sqlQueryBean.append("where ");
			sqlQueryBean.append("comm.ASSET_TYPE_ID = t.ASSET_TYPE_ID and comm.COMM_MODE_ID in ( :commModeId )) ");
			sqlQueryBean.append("then 1 else 0 end  as commModeNum, ");
			sqlQueryBean.append("sum(case when STATUS in ('DISABLED','DESTROY') then 1 else 0 end) as inScrap, ");
			sqlQueryBean.append("sum(case when STATUS = 'IN_USE' then 1 else 0 end) as inUse, ");
			sqlQueryBean.append("sum(case when STATUS not in ('DISABLED','DESTROY','IN_USE') and ");
			// Bug #3232 維護費報表啟用日期調整   (設備啟用日期 + 1年) > 帳務年月月底
		//	sqlQueryBean.append("dateadd(year,1,ENABLE_DATE) < dateadd(month,1,convert(DATE, :monthYear + '01')) then 1 else 0 end) as inEnable ");
			sqlQueryBean.append("dateadd(year,1,ENABLE_DATE) > dateadd(month,1,convert(DATE, :monthYear + '01')) then 1 else 0 end) as inEnable ");
			sqlQueryBean.append("from #temp t");
			sqlQueryBean.append("group by ASSET_TYPE_ID, NAME ");
			sqlQueryBean.append("DROP TABLE #temp");
			sqlQueryBean.setParameter("monthYear", monthYear);
			sqlQueryBean.setParameter("customerId", customerId);
			sqlQueryBean.setParameter("assetCategory", IAtomsConstants.ASSET_CATEGORY_EDC);
			sqlQueryBean.setParameter("commModeId", commModeId);
			AliasBean aliasBean = new AliasBean(DmmRepositoryDTO.class);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue() , StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.NAME.getValue() , StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.IN_ENABLE.getValue() , IntegerType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.TOTAL.getValue() , IntegerType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.IN_SCRAP.getValue() , IntegerType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.IN_USE.getValue() , IntegerType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.COMM_MODE_NUM.getValue() , IntegerType.INSTANCE);
			LOGGER.debug(".listFeeAssetListForGp()", "listFeeAssetListForGp:", sqlQueryBean.toString());
			dmmRepositoryDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(":listFeeAssetListForGp() is error.", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return dmmRepositoryDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryMonthlyDAO#listFeeAssetListInUseForGp(java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<DmmRepositoryDTO> listFeeAssetListInUseForGp(String monthYear,
			Date startDate, Date endDate, List<String> commModeId, String customerId) throws DataAccessException {
		List<DmmRepositoryDTO> dmmRepositoryDTOs = null;
		LOGGER.debug("listFeeAssetListInUseForGp()", "parameters:monthYear=", monthYear);
		LOGGER.debug("listFeeAssetListInUseForGp()", "parameters:startDate=", startDate.toString());
		LOGGER.debug("listFeeAssetListInUseForGp()", "parameters:endDate=", endDate.toString());
		LOGGER.debug("listFeeAssetListInUseForGp()", "parameters:commModeId=", commModeId.toString());
		LOGGER.debug("listFeeAssetListInUseForGp()", "parameters:customerId=", customerId);
		try{
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			getFeeTemp(monthYear, sqlQueryBean);
			//查詢月檔啟用設備
			sqlQueryBean.append("select ");
			sqlQueryBean.append("t.ASSET_ID, ").append("t.STATUS,").append("t.SERIAL_NUMBER,");
			sqlQueryBean.append("t.ENABLE_DATE,").append("t.ASSET_TYPE_ID,");
			sqlQueryBean.append("t.NAME ");
			sqlQueryBean.append("into #tempEnabled ");
			sqlQueryBean.append("from #temp t ");
			sqlQueryBean.append("where ");
			sqlQueryBean.append("t.STATUS = :inUse ");
			sqlQueryBean.append("or (STATUS not in ('DISABLED','DESTROY','IN_USE') and ");
			// Bug #3232 維護費報表啟用日期調整   (設備啟用日期 + 1年) > 帳務年月月底
		//	sqlQueryBean.append("dateadd(year,1,ENABLE_DATE) < dateadd(month,1,convert(DATE, :monthYear + '01'))) ");
			sqlQueryBean.append("dateadd(year,1,ENABLE_DATE) > dateadd(month,1,convert(DATE, :monthYear + '01'))) ");
			//查詢最新設備資料中取得使用中設備
			sqlQueryBean.append("select ");
			sqlQueryBean.append("repos.ASSET_ID, ").append("repos.STATUS,").append("repos.SERIAL_NUMBER,");
			sqlQueryBean.append("repos.ENABLE_DATE,").append("asset.ASSET_TYPE_ID,");
			sqlQueryBean.append("asset.NAME ");
			sqlQueryBean.append("into #tempEabledNew ");
			sqlQueryBean.append("from ").append(schema).append(".DMM_REPOSITORY repos ");
			sqlQueryBean.append("left join ").append(schema).append(".DMM_ASSET_TYPE asset on repos.ASSET_TYPE_ID = asset.ASSET_TYPE_ID ");
			sqlQueryBean.append("where ");
			sqlQueryBean.append("repos.STATUS = :inUse ");
			sqlQueryBean.append("and repos.CASE_COMPLETION_DATE >= :startDate and repos.CASE_COMPLETION_DATE < :endDate ");
			sqlQueryBean.append("and repos.ASSET_USER = :customerId ");
			sqlQueryBean.append("and asset.ASSET_CATEGORY = :assetCategory ");
			sqlQueryBean.append("and not exists (select * from #tempEnabled t where repos.ASSET_ID = t.ASSET_ID) ");
			//查詢啟用設備
			sqlQueryBean.append("select ");
			sqlQueryBean.append("a.assetTypeId as assetTypeId, ").append("a.name as name, ");
			sqlQueryBean.append("a.commModeNum as commModeNum, ").append("count(1) as inUse ");
			sqlQueryBean.append("from( ");
			sqlQueryBean.append("select ");
			sqlQueryBean.append("ASSET_TYPE_ID as assetTypeId,").append("NAME as name, ");
			sqlQueryBean.append("case when EXISTS( ");
			sqlQueryBean.append("select 1 ");
			sqlQueryBean.append("from ").append(schema);
			sqlQueryBean.append(".DMM_ASSET_SUPPORTED_COMM comm ");
			sqlQueryBean.append("where ");
			sqlQueryBean.append("comm.ASSET_TYPE_ID = t.ASSET_TYPE_ID and comm.COMM_MODE_ID in ( :commModeId )) ");
			sqlQueryBean.append("then 1 else 0 end  as commModeNum ");
			sqlQueryBean.append("from #tempEnabled t");
			sqlQueryBean.append("where t.STATUS = :inUse");
			sqlQueryBean.append("union all ");
			sqlQueryBean.append("select ");
			sqlQueryBean.append("ASSET_TYPE_ID as assetTypeId,").append("NAME as name, ");
			sqlQueryBean.append("case when EXISTS( ");
			sqlQueryBean.append("select 1 ");
			sqlQueryBean.append("from ").append(schema);
			sqlQueryBean.append(".DMM_ASSET_SUPPORTED_COMM comm ");
			sqlQueryBean.append("where ");
			sqlQueryBean.append("comm.ASSET_TYPE_ID = tt.ASSET_TYPE_ID and comm.COMM_MODE_ID in ( :commModeId )) ");
			sqlQueryBean.append("then 1 else 0 end  as commModeNum ");
			sqlQueryBean.append("from #tempEabledNew tt ");
			sqlQueryBean.append(") a ");
			sqlQueryBean.append("group by a.assetTypeId,a.name,a.commModeNum ");
			sqlQueryBean.append("DROP TABLE #temp");
			sqlQueryBean.append("DROP TABLE #tempEabledNew");
			sqlQueryBean.append("DROP TABLE #tempEnabled");
			sqlQueryBean.setParameter("monthYear", monthYear);
			sqlQueryBean.setParameter("customerId", customerId);
			sqlQueryBean.setParameter("assetCategory", IAtomsConstants.ASSET_CATEGORY_EDC);
			sqlQueryBean.setParameter("inUse", IAtomsConstants.PARAM_ASSET_STATUS_IN_USE);
			sqlQueryBean.setParameter("startDate", startDate);
			sqlQueryBean.setParameter("endDate", endDate);
			sqlQueryBean.setParameter("commModeId", commModeId);
			AliasBean aliasBean = new AliasBean(DmmRepositoryDTO.class);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue() , StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.NAME.getValue() , StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.COMM_MODE_NUM.getValue() , IntegerType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.IN_USE.getValue() , IntegerType.INSTANCE);
			LOGGER.debug(".listFeeAssetListInUseForGp()", "listFeeAssetListInUseForGp:", sqlQueryBean.toString());
			dmmRepositoryDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(":listFeeAssetListInUseForGp() is error.", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return dmmRepositoryDTOs;
	}
	/**
	 * Purpose:
	 * @author CarrieDuan
	 * @param monthYear
	 * @param sqlQueryBean
	 * @throws DataAccessException
	 * @return void
	 */
	private void getFeeTemp (String monthYear, SqlQueryBean sqlQueryBean) throws DataAccessException {
		String schema = this.getMySchema();
		sqlQueryBean.append("select ");
		sqlQueryBean.append("repos.ASSET_ID,").append("repos.STATUS,").append("repos.SERIAL_NUMBER,");
		sqlQueryBean.append("repos.ENABLE_DATE,").append("asset.ASSET_TYPE_ID,");
		sqlQueryBean.append("asset.NAME ").append("into #temp ");
		sqlQueryBean.append("from ");
		sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY_MONTHLY repos ");
		sqlQueryBean.append("left join ").append(schema).append(".DMM_ASSET_TYPE asset on repos.ASSET_TYPE_ID = asset.ASSET_TYPE_ID ");
		sqlQueryBean.append("where 1=1");
		sqlQueryBean.append("and repos.MONTH_YEAR = :monthYear ");
		sqlQueryBean.append("and asset.ASSET_CATEGORY = :assetCategory ");
		sqlQueryBean.append("and repos.ASSET_USER = :customerId");
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryMonthlyDAO#listScrapAssetForGp(java.lang.String)
	 */
	@Override
	public List<DmmRepositoryDTO> listScrapAssetForGp(String monthYear, String customerId) throws DataAccessException {
		List<DmmRepositoryDTO> dmmRepositoryDTOs = null;
		LOGGER.debug("listScrapAssetForGp()", "parameters:monthYear=", monthYear);
		LOGGER.debug("listScrapAssetForGp()", "parameters:customerId=", customerId);
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("asset.ASSET_TYPE_ID", DmmRepositoryDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			sqlStatement.addSelectClause("asset.NAME", DmmRepositoryDTO.ATTRIBUTE.NAME.getValue());
			sqlStatement.addSelectClause("mer.MERCHANT_CODE", DmmRepositoryDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("mer.NAME", DmmRepositoryDTO.ATTRIBUTE.MERCHANT_NAME.getValue());
			sqlStatement.addSelectClause("repos.DTID", DmmRepositoryDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("repos.SERIAL_NUMBER", DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("repos.ENABLE_DATE", DmmRepositoryDTO.ATTRIBUTE.ENABLE_DATE.getValue());
			sqlStatement.addSelectClause("repos.UPDATE_DATE", DmmRepositoryDTO.ATTRIBUTE.UPDATE_DATE.getValue());
			sqlStatement.addSelectClause("w.NAME", DmmRepositoryDTO.ATTRIBUTE.WAREHOUSE_NAME.getValue());
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(".DMM_REPOSITORY_HISTORY_MONTHLY repos ");
			buffer.append("left join ").append(schema).append(".DMM_ASSET_TYPE asset on repos.ASSET_TYPE_ID = asset.ASSET_TYPE_ID ");
			buffer.append("left join ").append(schema).append(".BIM_WAREHOUSE w on repos.WAREHOUSE_ID = w.WAREHOUSE_ID ");
			buffer.append("left join ").append(schema).append(".BIM_MERCHANT mer ON mer.MERCHANT_ID = repos.MERCHANT_ID ");
			sqlStatement.addFromExpression(buffer.toString());
			if (StringUtils.hasText(monthYear)) {
				sqlStatement.addWhereClause("repos.MONTH_YEAR = :monthYear", monthYear);
			}
			if (StringUtils.hasText(customerId)) {
				sqlStatement.addWhereClause("repos.ASSET_USER = :customerId", customerId);
			}
			sqlStatement.addWhereClause("asset.ASSET_CATEGORY = :assetCategory", IAtomsConstants.ASSET_CATEGORY_EDC);
			sqlStatement.addWhereClause("repos.STATUS in ('DISABLED','DESTROY')");
			sqlStatement.setOrderByExpression("asset.NAME");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmRepositoryDTO.class);
			LOGGER.debug(".listScrapAssetForGp()", "listScrapAssetForGp:", sqlQueryBean.toString());
			dmmRepositoryDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(":listScrapAssetForGp() is error.", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return dmmRepositoryDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryMonthlyDAO#assetInfoListGreenWorld(java.lang.String, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<DmmRepositoryDTO> assetInfoListGreenWorld(String yearMonthDay, String customerCode, Date startDate, Date endDate)
			throws DataAccessException {
		LOGGER.error(this.getClass().getName()+".assetInfoListGreenWorld() --> yearMonthDay: "+yearMonthDay);
		LOGGER.error(this.getClass().getName()+".assetInfoListGreenWorld() --> customerCode: "+customerCode);
		List<DmmRepositoryDTO> listDmmRepositoryHistoryDTO= null;
		try {
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append("SELECT");
			sql.append(" ROW_NUMBER() over (order by repos.ASSET_ID) as rowId, ");
			sql.append(" repos.ASSET_ID AS assetId, ");	
			sql.append(" asset.NAME as name ,");
			sql.append(" mer.MERCHANT_CODE AS merchantCode, ");	
			sql.append(" mer.NAME AS merchantName , ");
			sql.append(" w.NAME as warehouseName, ");
			sql.append(" repos.DTID as dtid , ");
			sql.append(" repos.SERIAL_NUMBER AS serialNumber , ");
			sql.append(" repos.UPDATE_DATE  AS updateDate, ");
			sql.append(" (CASE WHEN repos.MA_TYPE = 'BUYOUT' THEN repos.PROPERTY_ID ");
			sql.append(" ELSE repos.SIM_ENABLE_NO END ) AS propertyId , ");
			sql.append(" repos.CHECKED_DATE AS checkedDate , ");
			sql.append(" statusDef.ITEM_NAME AS caseStatus ");
			sql.append(" INTO  #temp ");
			sql.append(" FROM ").append(schema).append(" .DMM_REPOSITORY_HISTORY_MONTHLY repos ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_WAREHOUSE w on w.WAREHOUSE_ID = repos.WAREHOUSE_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE asset on repos.ASSET_TYPE_ID = asset.ASSET_TYPE_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer on repos.MERCHANT_ID = mer.MERCHANT_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF statusDef on statusDef.BPTD_CODE = 'ASSET_STATUS' and statusDef.ITEM_VALUE = repos.STATUS ");
			sql.append(" WHERE ");
			//設備類別
			sql.append(" repos.MONTH_YEAR = :monthYear");
			sql.setParameter(DmmRepositoryDTO.ATTRIBUTE.YEAR_MONTH_DAY.getValue(), yearMonthDay);
			//設備代碼
			if(StringUtils.hasText(customerCode)){
				sql.append(" and repos.ASSET_USER = :customerCode");
				sql.setParameter(DmmRepositoryDTO.ATTRIBUTE.CUSTOMER_CODE.getValue(), customerCode);
				LOGGER.debug(this.getClass().getName(), ".assetInfoListGreenWorld() --> customerCode: ", customerCode);
			}
			sql.append(" AND asset.ASSET_CATEGORY =:assetCategoryEDC");
			sql.append(" AND repos.IS_ENABLED =:yes ");
			sql.append(" and repos.ASSET_MODEL not like :chat ; ");
			sql.append(" select * from #temp union all ");
			sql.append(" SELECT ROW_NUMBER() over (order by repos.ASSET_ID) as rowId, ");
			sql.append(" repos.ASSET_ID AS assetId, ");	
			sql.append(" asset.NAME as name, ");
			sql.append(" mer.MERCHANT_CODE AS merchantCode, ");	
			sql.append(" mer.NAME AS merchantName , ");
			sql.append(" w.NAME as wareHouseName, ");
			sql.append(" repos.DTID as dtid , ");
			sql.append(" repos.SERIAL_NUMBER AS serialNumber , ");
			sql.append(" repos.UPDATE_DATE  AS updateDate, ");
			sql.append(" (CASE WHEN repos.MA_TYPE = 'BUYOUT' THEN repos.PROPERTY_ID ");
			sql.append(" ELSE repos.SIM_ENABLE_NO END ) AS propertyId , ");
			sql.append(" repos.CHECKED_DATE AS checkedDate , ");
			sql.append(" statusDef.ITEM_NAME AS caseStatus ");
			sql.append(" FROM ").append(schema).append(" .DMM_REPOSITORY repos ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_WAREHOUSE w on w.WAREHOUSE_ID = repos.WAREHOUSE_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer on repos.MERCHANT_ID = mer.MERCHANT_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE asset on repos.ASSET_TYPE_ID = asset.ASSET_TYPE_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF statusDef on statusDef.BPTD_CODE =:asset_status and statusDef.ITEM_VALUE = repos.STATUS ");
			sql.append(" WHERE ");
			//設備類別
			sql.append(" repos.CASE_COMPLETION_DATE >= :startDate");
			sql.setParameter("startDate", startDate);
			sql.append(" AND repos.CASE_COMPLETION_DATE < :endDate");
			sql.setParameter("endDate", endDate);
			//設備代碼
			if(StringUtils.hasText(customerCode)){
				sql.append(" and repos.ASSET_USER = :customerCode");
				sql.setParameter(DmmRepositoryDTO.ATTRIBUTE.CUSTOMER_CODE.getValue(), customerCode);
				LOGGER.debug(this.getClass().getName(), ".assetInfoListGreenWorld() --> customerCode: ", customerCode);
			}
			sql.append(" AND asset.ASSET_CATEGORY =:assetCategoryEDC");
			sql.append(" AND repos.IS_ENABLED =:yes ");
			sql.append(" and repos.ASSET_MODEL not like :chat ");
			sql.append(" AND NOT EXISTS( SELECT * FROM #temp t  WHERE repos.ASSET_ID = t.assetId ); ");
			sql.append(" drop TABLE #temp; ");
			//排序
			sql.setParameter("asset_status", IATOMS_PARAM_TYPE.ASSET_STATUS.getCode());
			sql.setParameter("yes", IAtomsConstants.YES);
			sql.setParameter("assetCategoryEDC", IAtomsConstants.ASSET_CATEGORY_EDC);
			sql.setParameter("chat", IAtomsConstants.MARK_PERCENT + "聯天" + IAtomsConstants.MARK_PERCENT);
			AliasBean alias = new AliasBean(DmmRepositoryDTO.class);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.ROWID.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.ASSET_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.MERCHANT_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.WAREHOUSE_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.PROPERTY_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.CHECKED_DATE.getValue(), DateType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.UPDATE_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.CASE_STATUS.getValue(), StringType.INSTANCE);
			listDmmRepositoryHistoryDTO = this.getDaoSupport().findByNativeSql(sql, alias);
			LOGGER.debug(this.getClass().getName(), "assetInfoListGreenWorld() --> sql: ", sql.toString()); 
		} catch (Exception e) {
			LOGGER.error(".assetInfoListGreenWorld() is error", "DAO Exception", e);
			throw new DataAccessException(CoreMessageCode.QUERY_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.REB_IATOMS_TB_NAME_SUPPLIES)}, e);
		}
		return listDmmRepositoryHistoryDTO;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryMonthlyDAO#feeChatAssetList(java.lang.String, java.lang.String)
	 */
	@Override
	public List<DmmRepositoryDTO> feeChatAssetList(String yearMonthDay, String customerCode) throws DataAccessException {
		LOGGER.debug("feeChatAssetList()", "parameters:customerCode=", customerCode);
		List<DmmRepositoryDTO> dmmRepositoryDTOs = null;
		try{
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			StringBuffer buffer = new StringBuffer();
			sqlStatement.addSelectClause("ROW_NUMBER() over (order by repos.ASSET_ID)", DmmRepositoryDTO.ATTRIBUTE.ROWID.getValue());
			sqlStatement.addSelectClause("mer.MERCHANT_CODE", DmmRepositoryDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("mer.NAME", DmmRepositoryDTO.ATTRIBUTE.MERCHANT_NAME.getValue());
			sqlStatement.addSelectClause("w.NAME", DmmRepositoryDTO.ATTRIBUTE.WAREHOUSE_NAME.getValue());
			sqlStatement.addSelectClause("repos.DTID", DmmRepositoryDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("asset.NAME", DmmRepositoryDTO.ATTRIBUTE.NAME.getValue());
			sqlStatement.addSelectClause("repos.SERIAL_NUMBER", DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("repos.IS_ENABLED", DmmRepositoryDTO.ATTRIBUTE.IS_ENABLED.getValue());
			sqlStatement.addSelectClause("repos.UPDATE_DATE", DmmRepositoryDTO.ATTRIBUTE.UPDATE_DATE.getValue());
			sqlStatement.addSelectClause("(case when repos.MA_TYPE = 'BUYOUT' then repos.PROPERTY_ID else repos.SIM_ENABLE_NO end)", DmmRepositoryDTO.ATTRIBUTE.PROPERTY_ID.getValue());
			sqlStatement.addSelectClause("repos.CHECKED_DATE", DmmRepositoryDTO.ATTRIBUTE.CHECKED_DATE.getValue());
			sqlStatement.addSelectClause("repos.STATUS", DmmRepositoryDTO.ATTRIBUTE.STATUS.getValue());
			sqlStatement.addSelectClause("statusDef.ITEM_NAME", DmmRepositoryDTO.ATTRIBUTE.STATUS_NAME.getValue());
			buffer.append(schema).append(".DMM_REPOSITORY_HISTORY_MONTHLY repos ");
			buffer.append("left join ").append(schema).append(".BIM_WAREHOUSE w on w.WAREHOUSE_ID = repos.WAREHOUSE_ID ");
			buffer.append("left join ").append(schema).append(".DMM_ASSET_TYPE asset on repos.ASSET_TYPE_ID = asset.ASSET_TYPE_ID ");
			buffer.append("left join ").append(schema).append(".BIM_MERCHANT mer on repos.MERCHANT_ID = mer.MERCHANT_ID ");
			buffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF statusDef on statusDef.BPTD_CODE = 'ASSET_STATUS' and statusDef.ITEM_VALUE = repos.STATUS ");
			buffer.append("where ");
			//設備類別
			buffer.append(" repos.MONTH_YEAR = :yearMonthDay");
			//設備代碼
			if(StringUtils.hasText(customerCode)){
				buffer.append(" AND repos.ASSET_USER = :customerCode");
				LOGGER.debug(this.getClass().getName(), ".feeChatAssetList() --> customerCode: ", customerCode);
			}
			buffer.append(" AND asset.ASSET_CATEGORY =:assetCategoryEDC");
			buffer.append(" and repos.ASSET_MODEL like :chat");
			//排序
			sqlStatement.addFromExpression(buffer.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter(DmmRepositoryDTO.ATTRIBUTE.CUSTOMER_CODE.getValue(), customerCode);
			sqlQueryBean.setParameter("yearMonthDay", yearMonthDay);
			sqlQueryBean.setParameter("assetCategoryEDC", IAtomsConstants.ASSET_CATEGORY_EDC);
			sqlQueryBean.setParameter("chat", IAtomsConstants.MARK_PERCENT + "聯天" + IAtomsConstants.MARK_PERCENT);
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmRepositoryDTO.class);
			LOGGER.debug("feeChatAssetList()", "listFeeAssetList:", sqlQueryBean.toString());	
			dmmRepositoryDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("feeChatAssetList() is error.", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return dmmRepositoryDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryMonthlyDAO#feeTaiXinAssetList(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<DmmRepositoryDTO> feeTaiXinAssetList(String yearMonthDay, String customerCode, 
			String TaiXincustomerCode) throws DataAccessException {
		LOGGER.debug("feeTaiXinAssetList()", "parameters:customerCode=", customerCode);
		List<DmmRepositoryDTO> dmmRepositoryDTOs = null;
		try{
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			StringBuffer buffer = new StringBuffer();
			sqlStatement.addSelectClause("ROW_NUMBER() over (order by repos.ASSET_ID)", DmmRepositoryDTO.ATTRIBUTE.ROWID.getValue());
			sqlStatement.addSelectClause("mer.MERCHANT_CODE", DmmRepositoryDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("mer.NAME", DmmRepositoryDTO.ATTRIBUTE.MERCHANT_NAME.getValue());
			sqlStatement.addSelectClause("merAdd.ITEM_NAME + header.BUSINESS_ADDRESS", DmmRepositoryDTO.ATTRIBUTE.BUSSINESS_ADDRESS.getValue());
			sqlStatement.addSelectClause("w.NAME", DmmRepositoryDTO.ATTRIBUTE.WAREHOUSE_NAME.getValue());
			sqlStatement.addSelectClause("repos.DTID", DmmRepositoryDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("repos.SERIAL_NUMBER", DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("repos.UPDATE_DATE", DmmRepositoryDTO.ATTRIBUTE.UPDATE_DATE.getValue());
			sqlStatement.addSelectClause("(case when repos.MA_TYPE = 'BUYOUT' then repos.PROPERTY_ID else repos.SIM_ENABLE_NO end)", DmmRepositoryDTO.ATTRIBUTE.PROPERTY_ID.getValue());
			sqlStatement.addSelectClause("repos.CHECKED_DATE", DmmRepositoryDTO.ATTRIBUTE.CHECKED_DATE.getValue());
			sqlStatement.addSelectClause("statusDef.ITEM_NAME", DmmRepositoryDTO.ATTRIBUTE.STATUS_NAME.getValue());
			sqlStatement.addSelectClause("repos.STATUS", DmmRepositoryDTO.ATTRIBUTE.STATUS.getValue());
			sqlStatement.addSelectClause("repos.ASSET_MODEL", DmmRepositoryDTO.ATTRIBUTE.ASSET_MODEL.getValue());
			sqlStatement.addSelectClause("repos.IS_ENABLED", DmmRepositoryDTO.ATTRIBUTE.IS_ENABLED.getValue());
			buffer.append(schema).append(".DMM_REPOSITORY_HISTORY_MONTHLY repos ");
			buffer.append("left join ").append(schema).append(".BIM_WAREHOUSE w on w.WAREHOUSE_ID = repos.WAREHOUSE_ID ");
			buffer.append("left join ").append(schema).append(".BIM_MERCHANT mer on repos.MERCHANT_ID = mer.MERCHANT_ID ");
			buffer.append("left join ").append(schema).append(".DMM_ASSET_TYPE asset on repos.ASSET_TYPE_ID = asset.ASSET_TYPE_ID ");
			buffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF statusDef on statusDef.BPTD_CODE = 'ASSET_STATUS' and statusDef.ITEM_VALUE = repos.STATUS ");
			buffer.append("left join ").append(schema).append(".BIM_MERCHANT_HEADER header on header.MERCHANT_HEADER_ID = repos.MERCHANT_HEADER_ID ");
			buffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF merAdd on merAdd.BPTD_CODE = :location and merAdd.ITEM_VALUE = header.LOCATION ");
			buffer.append("where ");
			//設備類別
			buffer.append(" repos.MONTH_YEAR = :yearMonthDay");
			//設備代碼
			if(StringUtils.hasText(customerCode)){
				buffer.append(" AND repos.ASSET_USER = :customerCode");
				LOGGER.debug(this.getClass().getName(), ".feeTaiXinAssetList() --> customerCode: ", customerCode);
			}
			if(StringUtils.hasText(TaiXincustomerCode)){
				buffer.append(" AND repos.ASSET_OWNER = :TaiXincustomerCode");
				LOGGER.debug(this.getClass().getName(), ".feeTaiXinAssetList() --> TaiXincustomerCode: ", TaiXincustomerCode);
			}
			buffer.append(" AND asset.ASSET_CATEGORY =:assetCategoryEDC");
			buffer.append(" AND repos.STATUS = 'IN_USE'");
			//排序
			sqlStatement.addFromExpression(buffer.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if(StringUtils.hasText(customerCode)){
				sqlQueryBean.setParameter(DmmRepositoryDTO.ATTRIBUTE.CUSTOMER_CODE.getValue(), customerCode);
			}
			if(StringUtils.hasText(TaiXincustomerCode)){
				sqlQueryBean.setParameter(DmmRepositoryDTO.ATTRIBUTE.TAIXIN_CUSTOMER_CODE.getValue(), TaiXincustomerCode);
			}
			sqlQueryBean.setParameter("yearMonthDay", yearMonthDay);
			sqlQueryBean.setParameter("location", IATOMS_PARAM_TYPE.LOCATION.getCode());
			sqlQueryBean.setParameter("assetCategoryEDC", IAtomsConstants.ASSET_CATEGORY_EDC);
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmRepositoryDTO.class);
			LOGGER.debug("feeTaiXinAssetList:", sqlQueryBean.toString());	
			dmmRepositoryDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("feeTaiXinAssetList() is error.", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return dmmRepositoryDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryMonthlyDAO#feeChatAssetListGreenWorld(java.lang.String, java.lang.String)
	 */
	@Override
	public List<DmmRepositoryDTO> feeChatAssetListGreenWorld(List<String> status, String yearMonthDay, 
			String customerCode) throws DataAccessException {
		LOGGER.debug("feeChatAssetList()", "parameters:customerCode=", customerCode);
		List<DmmRepositoryDTO> dmmRepositoryDTOs = null;
		try{
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			StringBuffer buffer = new StringBuffer();
			sqlStatement.addSelectClause("ROW_NUMBER() over (order by repos.ASSET_ID)", DmmRepositoryDTO.ATTRIBUTE.ROWID.getValue());
			sqlStatement.addSelectClause("mer.MERCHANT_CODE", DmmRepositoryDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("mer.NAME", DmmRepositoryDTO.ATTRIBUTE.MERCHANT_NAME.getValue());
			sqlStatement.addSelectClause("w.NAME", DmmRepositoryDTO.ATTRIBUTE.WAREHOUSE_NAME.getValue());
			sqlStatement.addSelectClause("repos.DTID", DmmRepositoryDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("asset.NAME", DmmRepositoryDTO.ATTRIBUTE.NAME.getValue());
			sqlStatement.addSelectClause("repos.SERIAL_NUMBER", DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("repos.UPDATE_DATE", DmmRepositoryDTO.ATTRIBUTE.UPDATE_DATE.getValue());
			sqlStatement.addSelectClause("(case when repos.MA_TYPE = 'BUYOUT' then repos.PROPERTY_ID else repos.SIM_ENABLE_NO end)", DmmRepositoryDTO.ATTRIBUTE.PROPERTY_ID.getValue());
			sqlStatement.addSelectClause("repos.CHECKED_DATE", DmmRepositoryDTO.ATTRIBUTE.CHECKED_DATE.getValue());
			sqlStatement.addSelectClause("statusDef.ITEM_NAME", DmmRepositoryDTO.ATTRIBUTE.STATUS_NAME.getValue());
			sqlStatement.addSelectClause("repos.STATUS", DmmRepositoryDTO.ATTRIBUTE.STATUS.getValue());
			buffer.append(schema).append(".DMM_REPOSITORY_HISTORY_MONTHLY repos ");
			buffer.append("left join ").append(schema).append(".BIM_WAREHOUSE w on w.WAREHOUSE_ID = repos.WAREHOUSE_ID ");
			buffer.append("left join ").append(schema).append(".DMM_ASSET_TYPE asset on repos.ASSET_TYPE_ID = asset.ASSET_TYPE_ID ");
			buffer.append("left join ").append(schema).append(".BIM_MERCHANT mer on repos.MERCHANT_ID = mer.MERCHANT_ID ");
			buffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF statusDef on statusDef.BPTD_CODE = 'ASSET_STATUS' and statusDef.ITEM_VALUE = repos.STATUS ");
			buffer.append("where ");
			//設備類別
			buffer.append(" repos.MONTH_YEAR = :yearMonthDay");
			//設備代碼
			if(StringUtils.hasText(customerCode)){
				buffer.append(" AND repos.ASSET_USER = :customerCode");
				LOGGER.debug(this.getClass().getName(), ".feeChatAssetList() --> customerCode: ", customerCode);
			}
			buffer.append(" AND asset.ASSET_CATEGORY =:assetCategoryEDC");
			buffer.append(" and (repos.ASSET_MODEL not like :chat  or repos.ASSET_MODEL is null or repos.ASSET_MODEL = '')");
			buffer.append(" AND repos.STATUS NOT in ( :status )  ");
			buffer.append(" AND repos.IS_ENABLED =:yes ");
			// Bug #3232 維護費報表啟用日期調整   (客戶驗收日期 + 1年) > 帳務年月月底
		//	buffer.append(" AND dateadd( YEAR , 1 , repos.CHECKED_DATE) < dateadd( MONTH ,  1 ,CONVERT(DATE , :yearMonthDay + '01' )) ");
			buffer.append(" AND dateadd( YEAR , 1 , repos.CHECKED_DATE) < dateadd( MONTH ,  1 ,CONVERT(DATE , :yearMonthDay + '01' )) ");
			//排序
			sqlStatement.addFromExpression(buffer.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if(StringUtils.hasText(customerCode)){
				sqlQueryBean.setParameter(DmmRepositoryDTO.ATTRIBUTE.CUSTOMER_CODE.getValue(), customerCode);
			}
			sqlQueryBean.setParameter("yearMonthDay", yearMonthDay);
			sqlQueryBean.setParameter("yes", IAtomsConstants.YES);
			sqlQueryBean.setParameter("assetCategoryEDC", IAtomsConstants.ASSET_CATEGORY_EDC);
			sqlQueryBean.setParameter("chat", IAtomsConstants.MARK_PERCENT + "聯天" + IAtomsConstants.MARK_PERCENT);
			sqlQueryBean.setParameter("status", status);
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmRepositoryDTO.class);
			LOGGER.debug("feeChatAssetList()", "listFeeAssetList:", sqlQueryBean.toString());	
			dmmRepositoryDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("feeChatAssetList() is error.", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return dmmRepositoryDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryMonthlyDAO#listEnableAssetInfoForGp(java.lang.String, java.util.List)
	 */
	@Override
	public List<DmmRepositoryDTO> listEnableAssetInfoForGp(String monthYear,Date startDate, Date endDate, List<String> assetStatus, String companyId) {
		LOGGER.debug("listEnableAssetInfoForGp()", "parameters:monthYear=", monthYear);
		LOGGER.debug("listEnableAssetInfoForGp()", "parameters:companyId=", companyId);
		LOGGER.debug("listEnableAssetInfoForGp()", "parameters:assetStatus=", assetStatus.toString());
		List<DmmRepositoryDTO> dmmRepositoryDTOs = null;
		try{
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("select ");
			sqlQueryBean.append("repos.ASSET_ID,").append("repos.STATUS,").append("repos.SERIAL_NUMBER,");
			sqlQueryBean.append("repos.ENABLE_DATE,").append("asset.ASSET_TYPE_ID,").append("mer.MERCHANT_CODE, ");
			sqlQueryBean.append("asset.NAME as name, ").append("repos.TID, ").append("repos.UPDATE_DATE,");
			sqlQueryBean.append("mer.NAME as merchantName ");
			sqlQueryBean.append("into #temp ");
			sqlQueryBean.append("from ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY_MONTHLY repos ");
			sqlQueryBean.append("left join ").append(schema).append(".DMM_ASSET_TYPE asset on repos.ASSET_TYPE_ID = asset.ASSET_TYPE_ID ");
			sqlQueryBean.append("left join ").append(schema).append(".BIM_MERCHANT mer on repos.MERCHANT_ID = mer.MERCHANT_ID ");
			sqlQueryBean.append("where 1=1");
			sqlQueryBean.append("and repos.MONTH_YEAR = :monthYear ");
			sqlQueryBean.append("and asset.ASSET_CATEGORY = :assetCategory ");
			sqlQueryBean.append("and repos.ASSET_USER = :customerId");
			sqlQueryBean.append("select * into #tempTable");
			sqlQueryBean.append("from #temp t");
			// Bug #3232 維護費報表啟用日期調整   (設備啟用日期 + 1年) > 帳務年月月底
		//	sqlQueryBean.append("where ((dateadd(YEAR, 1, t.ENABLE_DATE) < dateadd(MONTH, 1, CONVERT(DATE, :monthYear + '01'))")
			sqlQueryBean.append("where ((dateadd(YEAR, 1, t.ENABLE_DATE) > dateadd(MONTH, 1, CONVERT(DATE, :monthYear + '01'))")
				.append("AND t.STATUS not in( :assetStatus )) or t.STATUS = :inUse); ");
			sqlQueryBean.append("select * ");
			sqlQueryBean.append("from( ");
			sqlQueryBean.append("select ");
			sqlQueryBean.append("rep.ASSET_ID as assetId, ");
			sqlQueryBean.append("mer1.MERCHANT_CODE as merchantCode, ");
			sqlQueryBean.append("rep.TID as tid, ");
			sqlQueryBean.append("rep.SERIAL_NUMBER as serialNumber, ");
			sqlQueryBean.append("rep.UPDATE_DATE as updateDate, ");
			sqlQueryBean.append("rep.ENABLE_DATE as enableDate, ");
			sqlQueryBean.append("mer1.NAME as merchantName, ");
			sqlQueryBean.append("rep.ASSET_TYPE_ID as assetTypeId, ");
			sqlQueryBean.append("assetType1.NAME as name ");
			sqlQueryBean.append("FROM ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY rep ");
			sqlQueryBean.append("left join ").append(schema).append(".BIM_MERCHANT mer1 on rep.MERCHANT_ID = mer1.MERCHANT_ID ");
			sqlQueryBean.append("left join ").append(schema).append(".DMM_ASSET_TYPE assetType1 on assetType1.ASSET_TYPE_ID = rep.ASSET_TYPE_ID ");
			sqlQueryBean.append("where ");
			sqlQueryBean.append("rep.STATUS = :inUse ");
			sqlQueryBean.append("and rep.CASE_COMPLETION_DATE >= :startDate ");
			sqlQueryBean.append("and rep.CASE_COMPLETION_DATE < :endDate ");
			sqlQueryBean.append("AND rep.ASSET_USER = :customerId ");
			sqlQueryBean.append("AND assetType1.ASSET_CATEGORY = :assetCategory ");
			sqlQueryBean.append("AND not exists(select * from #tempTable tempTable where rep.ASSET_ID = tempTable.ASSET_ID ) ");
			sqlQueryBean.append("union all ");
			sqlQueryBean.append("select ");
			sqlQueryBean.append("tt.ASSET_ID as assetId, ");
			sqlQueryBean.append("tt.MERCHANT_CODE as merchantCode, ");
			sqlQueryBean.append("tt.TID as tid, ");
			sqlQueryBean.append("tt.SERIAL_NUMBER as serialNumber, ");
			sqlQueryBean.append("tt.UPDATE_DATE as updateDate, ");
			sqlQueryBean.append("tt.ENABLE_DATE as enableDate, ");
			sqlQueryBean.append("tt.merchantName, ");
			sqlQueryBean.append("tt.ASSET_TYPE_ID as assetTypeId, ");
			sqlQueryBean.append("tt.name ");
			sqlQueryBean.append("from #tempTable tt");
			sqlQueryBean.append(") t ");
			sqlQueryBean.append("order by t.assetTypeId ");
			sqlQueryBean.append("drop table #temp ");
			sqlQueryBean.append("drop table #tempTable ");
			sqlQueryBean.setParameter("monthYear", monthYear);
			sqlQueryBean.setParameter("assetStatus", assetStatus);
			sqlQueryBean.setParameter("assetCategory", IAtomsConstants.ASSET_CATEGORY_EDC);
			sqlQueryBean.setParameter("startDate", startDate);
			sqlQueryBean.setParameter("endDate", endDate);
			sqlQueryBean.setParameter("inUse", IAtomsConstants.PARAM_ASSET_STATUS_IN_USE);
			sqlQueryBean.setParameter("customerId", companyId);
			AliasBean aliasBean = new AliasBean(DmmRepositoryDTO.class);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.ASSET_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.MERCHANT_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.TID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.UPDATE_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.ENABLE_DATE.getValue(), TimestampType.INSTANCE);
			LOGGER.debug("listEnableAssetInfoForGp()", "listEnableAssetInfoForGp:", sqlQueryBean.toString());	
			dmmRepositoryDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listEnableAssetInfoForGp() is error.", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return dmmRepositoryDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryMonthlyDAO#assetInfoListScsb(java.lang.String, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<DmmRepositoryDTO> assetInfoListScsb(String yearMonthDay, String customerCode, Date startDate, 
			Date endDate) throws DataAccessException {
		LOGGER.error(this.getClass().getName()+".assetInfoListScsb() --> yearMonthDay: "+yearMonthDay);
		LOGGER.error(this.getClass().getName()+".assetInfoListScsb() --> customerCode: "+customerCode);
		List<DmmRepositoryDTO> listDmmRepositoryHistoryDTO= null;
		try {
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append("SELECT");
			sql.append(" ROW_NUMBER() over (order by repos.ASSET_ID) as rowId, ");
			sql.append(" repos.ASSET_ID AS assetId, ");	
			sql.append(" asset.NAME as name ,");
			sql.append(" mer.MERCHANT_CODE AS merchantCode, ");	
			sql.append(" mer.NAME AS merchantName , ");
			sql.append(" w.NAME as warehouseName, ");
			sql.append(" repos.DTID as dtid , ");
			sql.append(" repos.STATUS AS status, ");
			sql.append(" repos.SERIAL_NUMBER AS serialNumber , ");
			sql.append(" repos.ENABLE_DATE AS enableDate ,");
			sql.append(" repos.UPDATE_DATE  AS updateDate, ");
			sql.append(" repos.CHECKED_DATE AS checkedDate , ");
			sql.append(" statusDef.ITEM_NAME AS caseStatus ");
			sql.append(" INTO  #temp ");
			sql.append(" FROM ").append(schema).append(" .DMM_REPOSITORY_HISTORY_MONTHLY repos ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_WAREHOUSE w on w.WAREHOUSE_ID = repos.WAREHOUSE_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE asset on repos.ASSET_TYPE_ID = asset.ASSET_TYPE_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer on repos.MERCHANT_ID = mer.MERCHANT_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF statusDef on statusDef.BPTD_CODE =:asset_status and statusDef.ITEM_VALUE = repos.STATUS ");
			sql.append(" WHERE ");
			//設備類別
			sql.append(" repos.MONTH_YEAR = :monthYear");
			sql.setParameter(DmmRepositoryDTO.ATTRIBUTE.YEAR_MONTH_DAY.getValue(), yearMonthDay);
			//設備代碼
			if(StringUtils.hasText(customerCode)){
				sql.append(" and repos.ASSET_USER = :customerCode");
				sql.setParameter(DmmRepositoryDTO.ATTRIBUTE.CUSTOMER_CODE.getValue(), customerCode);
				LOGGER.debug(this.getClass().getName(), ".assetInfoListScsb() --> customerCode: ", customerCode);
			}
			sql.append(" AND asset.ASSET_CATEGORY =:assetCategoryEDC");
			sql.append(" AND repos.IS_ENABLED =:yes ;");
			sql.append(" select * from #temp union all ");
			sql.append(" SELECT ROW_NUMBER() over (order by repos.ASSET_ID) as rowId, ");
			sql.append(" repos.ASSET_ID AS assetId, ");	
			sql.append(" asset.NAME as name, ");
			sql.append(" mer.MERCHANT_CODE AS merchantCode, ");	
			sql.append(" mer.NAME AS merchantName , ");
			sql.append(" w.NAME as warehouseName, ");
			sql.append(" repos.DTID as dtid , ");
			sql.append(" repos.STATUS AS status, ");
			sql.append(" repos.SERIAL_NUMBER AS serialNumber , ");
			sql.append(" repos.ENABLE_DATE AS enableDate ,");
			sql.append(" repos.UPDATE_DATE  AS updateDate, ");
			sql.append(" repos.CHECKED_DATE AS checkedDate , ");
			sql.append(" statusDef.ITEM_NAME AS caseStatus ");
			sql.append(" FROM ").append(schema).append(" .DMM_REPOSITORY repos ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_WAREHOUSE w on w.WAREHOUSE_ID = repos.WAREHOUSE_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer on repos.MERCHANT_ID = mer.MERCHANT_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE asset on repos.ASSET_TYPE_ID = asset.ASSET_TYPE_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF statusDef on statusDef.BPTD_CODE =:asset_status and statusDef.ITEM_VALUE = repos.STATUS ");
			sql.append(" WHERE ");
			//設備類別
			sql.append(" repos.CASE_COMPLETION_DATE >= :startDate");
			sql.setParameter("startDate", startDate);
			sql.append(" AND repos.CASE_COMPLETION_DATE < :endDate");
			sql.setParameter("endDate", endDate);
			//設備代碼
			if(StringUtils.hasText(customerCode)){
				sql.append(" and repos.ASSET_USER = :customerCode");
				sql.setParameter(DmmRepositoryDTO.ATTRIBUTE.CUSTOMER_CODE.getValue(), customerCode);
				LOGGER.debug(this.getClass().getName(), ".assetInfoListScsb() --> customerCode: ", customerCode);
			}
			sql.append(" AND asset.ASSET_CATEGORY =:assetCategoryEDC");
			sql.append(" AND repos.IS_ENABLED =:yes ");
			sql.append(" AND NOT EXISTS( SELECT * FROM #temp t  WHERE repos.ASSET_ID = t.assetId ); ");
			sql.append(" drop TABLE #temp; ");
			//排序
			sql.setParameter("asset_status", IATOMS_PARAM_TYPE.ASSET_STATUS.getCode());
			sql.setParameter("yes", IAtomsConstants.YES);
			sql.setParameter("assetCategoryEDC", IAtomsConstants.ASSET_CATEGORY_EDC);
			AliasBean alias = new AliasBean(DmmRepositoryDTO.class);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.ROWID.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.ASSET_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.STATUS.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.MERCHANT_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.WAREHOUSE_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.ENABLE_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.CHECKED_DATE.getValue(), DateType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.UPDATE_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.CASE_STATUS.getValue(), StringType.INSTANCE);
			listDmmRepositoryHistoryDTO = this.getDaoSupport().findByNativeSql(sql, alias);
			LOGGER.debug(this.getClass().getName(), "assetInfoListScsb() --> sql: ", sql.toString()); 
		} catch (Exception e) {
			LOGGER.error(".assetInfoListScsb() is error", "DAO Exception", e);
			throw new DataAccessException(CoreMessageCode.QUERY_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.REB_IATOMS_TB_NAME_SUPPLIES)}, e);
		}
		return listDmmRepositoryHistoryDTO;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryMonthlyDAO#feeAssetListScsb(java.util.List, java.lang.String, java.lang.String)
	 */
	@Override
	public List<DmmRepositoryDTO> feeAssetListScsb(List<String> status, String yearMonthDay, String customerCode)
			throws DataAccessException {
		LOGGER.debug("feeAssetListScsb()", "parameters:customerCode=", customerCode);
		LOGGER.debug("feeAssetListScsb()", "parameters:yearMonthDay=", yearMonthDay);
		List<DmmRepositoryDTO> dmmRepositoryDTOs = null;
		try{
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			StringBuffer buffer = new StringBuffer();
			sqlStatement.addSelectClause("ROW_NUMBER() over (order by repos.ASSET_ID)", DmmRepositoryDTO.ATTRIBUTE.ROWID.getValue());
			sqlStatement.addSelectClause("mer.MERCHANT_CODE", DmmRepositoryDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("mer.NAME", DmmRepositoryDTO.ATTRIBUTE.MERCHANT_NAME.getValue());
			sqlStatement.addSelectClause("w.NAME", DmmRepositoryDTO.ATTRIBUTE.WAREHOUSE_NAME.getValue());
			sqlStatement.addSelectClause("repos.DTID", DmmRepositoryDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("repos.SERIAL_NUMBER", DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("repos.ENABLE_DATE", DmmRepositoryDTO.ATTRIBUTE.ENABLE_DATE.getValue());
			sqlStatement.addSelectClause("repos.UPDATE_DATE", DmmRepositoryDTO.ATTRIBUTE.UPDATE_DATE.getValue());
			sqlStatement.addSelectClause("repos.ASSET_TYPE_ID", DmmRepositoryDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			sqlStatement.addSelectClause("asset.NAME", DmmRepositoryDTO.ATTRIBUTE.NAME.getValue());
			StringBuffer buffer2 = new StringBuffer();
			buffer2.append(" STUFF((SELECT ','+ ltrim(bcomm.ITEM_VALUE) FROM dbo.BASE_PARAMETER_ITEM_DEF bcomm,dbo.DMM_ASSET_SUPPORTED_COMM comm where comm.ASSET_TYPE_ID = asset.ASSET_TYPE_ID and comm.COMM_MODE_ID= bcomm.ITEM_VALUE for XML PATH('')), 1, 1, '') ");
			sqlStatement.addSelectClause(buffer2.toString(), DmmRepositoryDTO.ATTRIBUTE.COMMMODE_ID.getValue());
			sqlStatement.addSelectClause("statusDef.ITEM_NAME", DmmRepositoryDTO.ATTRIBUTE.STATUS_NAME.getValue());
			sqlStatement.addSelectClause("repos.STATUS", DmmRepositoryDTO.ATTRIBUTE.STATUS.getValue());
			buffer.append(schema).append(".DMM_REPOSITORY_HISTORY_MONTHLY repos ");
			buffer.append("left join ").append(schema).append(".BIM_WAREHOUSE w on w.WAREHOUSE_ID = repos.WAREHOUSE_ID ");
			buffer.append("left join ").append(schema).append(".DMM_ASSET_TYPE asset on repos.ASSET_TYPE_ID = asset.ASSET_TYPE_ID ");
			buffer.append("left join ").append(schema).append(".BIM_MERCHANT mer on repos.MERCHANT_ID = mer.MERCHANT_ID ");
			buffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF statusDef on statusDef.BPTD_CODE = 'ASSET_STATUS' and statusDef.ITEM_VALUE = repos.STATUS ");
			buffer.append("where ");
			//設備類別
			buffer.append(" repos.MONTH_YEAR = :yearMonthDay");
			//設備代碼
			if(StringUtils.hasText(customerCode)){
				buffer.append(" AND repos.ASSET_USER = :customerCode");
				LOGGER.debug(this.getClass().getName(), ".feeAssetListScsb() --> customerCode: ", customerCode);
			}
			buffer.append(" AND asset.ASSET_CATEGORY =:assetCategoryEDC");
			buffer.append(" AND repos.STATUS NOT in (:status ) ");
			// Bug #3232 維護費報表啟用日期調整   (設備啟用日期 + 3年) > 帳務年月月底
		//	buffer.append(" AND dateadd( YEAR , 3 , repos.ENABLE_DATE) < dateadd( MONTH ,  1 ,CONVERT(DATE , :yearMonthDay + '01' )) ");
			//此處需統計的爲已滿3年的設備數量，所以應爲(設備啟用日期 + 3年) < 帳務年月月底 Task #3286
			//buffer.append(" AND dateadd( YEAR , 3 , repos.ENABLE_DATE) > dateadd( MONTH ,  1 ,CONVERT(DATE , :yearMonthDay + '01' )) ");
			buffer.append(" AND dateadd( YEAR , 3 , repos.ENABLE_DATE) < dateadd( MONTH ,  1 ,CONVERT(DATE , :yearMonthDay + '01' )) ");
			//排序
			sqlStatement.addFromExpression(buffer.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if(StringUtils.hasText(customerCode)){
				sqlQueryBean.setParameter(DmmRepositoryDTO.ATTRIBUTE.CUSTOMER_CODE.getValue(), customerCode);
			}
			sqlQueryBean.setParameter("yearMonthDay", yearMonthDay);
			sqlQueryBean.setParameter("assetCategoryEDC", IAtomsConstants.ASSET_CATEGORY_EDC);
			sqlQueryBean.setParameter("status", status);
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmRepositoryDTO.class);
			LOGGER.debug("feeAssetListScsb()", "listFeeAssetList:", sqlQueryBean.toString());	
			dmmRepositoryDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("feeAssetListScsb() is error.", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return dmmRepositoryDTOs;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryMonthlyDAO#deleteRepoMonthlyHis(java.util.Date)
	 */
	@Override
	public void deleteRepoMonthlyHis(String deleteDateString) throws DataAccessException {
		try {
			// 沒傳入日期不處理
			if(StringUtils.hasText(deleteDateString)){
				String schema = this.getMySchema();
				SqlQueryBean sqlQueryBean = new SqlQueryBean("delete ");
				sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY_MONTHLY_DESC ");
				// 刪除設定日期之前的所有設備歷史月檔
				sqlQueryBean.append(" where MONTH_YEAR<= :deleteDate");
				
				sqlQueryBean.append("delete ");
				sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY_MONTHLY_COM ");
				// 刪除設定日期之前的所有設備歷史月檔
				sqlQueryBean.append(" where MONTH_YEAR<= :deleteDate");
				
				sqlQueryBean.append("delete ");
				sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY_MONTHLY ");
				// 刪除設定日期之前的所有設備歷史月檔
				sqlQueryBean.append(" where MONTH_YEAR<= :deleteDate");
				sqlQueryBean.setParameter("deleteDate", deleteDateString);
				LOGGER.debug("deleteRepoMonthlyHis()", "sql:" + sqlQueryBean.toString());
				this.getDaoSupport().updateByNativeSql(sqlQueryBean);
				this.getDaoSupport().flush();
			}
		} catch (Exception e) {
			LOGGER.error("deleteRepoMonthlyHis()", e.getMessage(), e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryMonthlyDAO#assetInfoListSyb(java.lang.String, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<DmmRepositoryDTO> assetInfoListSyb(String yearMonthDay,String customerCode, Date startDate, 
			Date endDate) throws DataAccessException {
		LOGGER.error(this.getClass().getName()+".assetInfoListSyb() --> yearMonthDay: "+yearMonthDay);
		LOGGER.error(this.getClass().getName()+".assetInfoListSyb() --> customerCode: "+customerCode);
		List<DmmRepositoryDTO> listDmmRepositoryHistoryDTO= null;
		try {
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append("SELECT");
			sql.append(" ROW_NUMBER() over (order by repos.ASSET_ID) as rowId, ");
			sql.append(" repos.ASSET_ID AS assetId, ");	
			sql.append(" asset.NAME as name ,");
			sql.append(" mer.MERCHANT_CODE AS merchantCode, ");	
			sql.append(" mer.NAME AS merchantName , ");
			sql.append(" w.NAME as warehouseName, ");
			sql.append(" repos.STATUS AS status, ");
			sql.append(" repos.DTID as dtid , ");
			sql.append(" repos.SERIAL_NUMBER AS serialNumber , ");
			sql.append(" c.CONTRACT_CODE AS contractCode , ");
			sql.append(" repos.ENABLE_DATE AS enableDate ,");
			sql.append(" repos.UPDATE_DATE  AS updateDate, ");
			sql.append(" repos.CHECKED_DATE AS checkedDate , ");
			sql.append(" STUFF((SELECT ','+ ltrim(bcomm.ITEM_VALUE) FROM dbo.BASE_PARAMETER_ITEM_DEF bcomm,dbo.DMM_ASSET_SUPPORTED_COMM comm where comm.ASSET_TYPE_ID = asset.ASSET_TYPE_ID and comm.COMM_MODE_ID= bcomm.ITEM_VALUE for XML PATH('')), 1, 1, '') as commModeId , ");
			sql.append(" (case when repos.MA_TYPE= 'BUYOUT' then repos.PROPERTY_ID else repos.SIM_ENABLE_NO end) as propertyId, ");
			sql.append(" statusDef.ITEM_NAME AS caseStatus ");
			sql.append(" INTO  #temp ");
			sql.append(" FROM ").append(schema).append(" .DMM_REPOSITORY_HISTORY_MONTHLY repos ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT c on repos.CONTRACT_ID = c.CONTRACT_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_WAREHOUSE w on w.WAREHOUSE_ID = repos.WAREHOUSE_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE asset on repos.ASSET_TYPE_ID = asset.ASSET_TYPE_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer on repos.MERCHANT_ID = mer.MERCHANT_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF statusDef on statusDef.BPTD_CODE =:asset_status and statusDef.ITEM_VALUE = repos.STATUS ");
			sql.append(" WHERE ");
			//設備類別
			sql.append(" repos.MONTH_YEAR = :monthYear");
			sql.setParameter(DmmRepositoryDTO.ATTRIBUTE.YEAR_MONTH_DAY.getValue(), yearMonthDay);
			//設備代碼
			if(StringUtils.hasText(customerCode)){
				sql.append(" and repos.ASSET_USER = :customerCode");
				sql.setParameter(DmmRepositoryDTO.ATTRIBUTE.CUSTOMER_CODE.getValue(), customerCode);
				LOGGER.debug(this.getClass().getName(), ".assetInfoListSyb() --> customerCode: ", customerCode);
			}
			sql.append(" AND asset.ASSET_CATEGORY =:assetCategoryEDC");
			sql.append(" AND repos.IS_ENABLED =:yes ;");
			sql.append(" select * from #temp union all ");
			sql.append(" SELECT ROW_NUMBER() over (order by repos.ASSET_ID) as rowId, ");
			sql.append(" repos.ASSET_ID AS assetId, ");	
			sql.append(" asset.NAME as name, ");
			sql.append(" mer.MERCHANT_CODE AS merchantCode, ");	
			sql.append(" mer.NAME AS merchantName , ");
			sql.append(" w.NAME as warehouseName, ");
			sql.append(" repos.STATUS AS status, ");
			sql.append(" repos.DTID as dtid , ");
			sql.append(" repos.SERIAL_NUMBER AS serialNumber , ");
			sql.append(" c.CONTRACT_CODE AS contractCode , ");
			sql.append(" repos.ENABLE_DATE AS enableDate ,");
			sql.append(" repos.UPDATE_DATE  AS updateDate, ");
			sql.append(" repos.CHECKED_DATE AS checkedDate , ");
			sql.append(" STUFF((SELECT ','+ ltrim(bcomm.ITEM_VALUE) FROM dbo.BASE_PARAMETER_ITEM_DEF bcomm,dbo.DMM_ASSET_SUPPORTED_COMM comm where comm.ASSET_TYPE_ID = asset.ASSET_TYPE_ID and comm.COMM_MODE_ID= bcomm.ITEM_VALUE for XML PATH('')), 1, 1, '') as commModeId , ");
			sql.append(" (case when repos.MA_TYPE= 'BUYOUT' then repos.PROPERTY_ID else repos.SIM_ENABLE_NO end) as propertyId, ");
			sql.append(" statusDef.ITEM_NAME AS caseStatus ");
			sql.append(" FROM ").append(schema).append(" .DMM_REPOSITORY repos ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT c on repos.CONTRACT_ID = c.CONTRACT_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_WAREHOUSE w on w.WAREHOUSE_ID = repos.WAREHOUSE_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer on repos.MERCHANT_ID = mer.MERCHANT_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE asset on repos.ASSET_TYPE_ID = asset.ASSET_TYPE_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF statusDef on statusDef.BPTD_CODE =:asset_status and statusDef.ITEM_VALUE = repos.STATUS ");
			sql.append(" WHERE ");
			//設備類別
			sql.append(" repos.CASE_COMPLETION_DATE >= :startDate");
			sql.setParameter("startDate", startDate);
			sql.append(" AND repos.CASE_COMPLETION_DATE < :endDate");
			sql.setParameter("endDate", endDate);
			//設備代碼
			if(StringUtils.hasText(customerCode)){
				sql.append(" and repos.ASSET_USER = :customerCode");
				sql.setParameter(DmmRepositoryDTO.ATTRIBUTE.CUSTOMER_CODE.getValue(), customerCode);
				LOGGER.debug(this.getClass().getName(), ".assetInfoListSyb() --> customerCode: ", customerCode);
			}
			sql.append(" AND asset.ASSET_CATEGORY =:assetCategoryEDC");
			sql.append(" AND repos.IS_ENABLED =:yes ");
			sql.append(" AND NOT EXISTS( SELECT * FROM #temp t  WHERE repos.ASSET_ID = t.assetId ); ");
			sql.append(" drop TABLE #temp; ");
			//排序
			sql.setParameter("asset_status", IATOMS_PARAM_TYPE.ASSET_STATUS.getCode());
			sql.setParameter("yes", IAtomsConstants.YES);
			sql.setParameter("assetCategoryEDC", IAtomsConstants.ASSET_CATEGORY_EDC);
			AliasBean alias = new AliasBean(DmmRepositoryDTO.class);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.ROWID.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.ASSET_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.STATUS.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.MERCHANT_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.WAREHOUSE_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.CONTRACT_CODE.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.ENABLE_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.CHECKED_DATE.getValue(), DateType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.COMMMODE_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.UPDATE_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.PROPERTY_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.CASE_STATUS.getValue(), StringType.INSTANCE);
			listDmmRepositoryHistoryDTO = this.getDaoSupport().findByNativeSql(sql, alias);
			LOGGER.debug(this.getClass().getName(), "assetInfoListSyb() --> sql: ", sql.toString()); 
		} catch (Exception e) {
			LOGGER.error(".assetInfoListSyb() is error", "DAO Exception", e);
			throw new DataAccessException(CoreMessageCode.QUERY_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.REB_IATOMS_TB_NAME_SUPPLIES)}, e);
		}
		return listDmmRepositoryHistoryDTO;
	}
	@Override
	public List<DmmRepositoryDTO> feeAssetListSyb(String yearMonthDay, String customerCode)
			throws DataAccessException {
		LOGGER.debug("feeAssetListScsb()", "parameters:customerCode=", customerCode);
		LOGGER.debug("feeAssetListScsb()", "parameters:yearMonthDay=", yearMonthDay);
		List<DmmRepositoryDTO> dmmRepositoryDTOs = null;
		try{
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			StringBuffer buffer = new StringBuffer();
			sqlStatement.addSelectClause("ROW_NUMBER() over (order by repos.ASSET_ID)", DmmRepositoryDTO.ATTRIBUTE.ROWID.getValue());
			sqlStatement.addSelectClause("mer.MERCHANT_CODE", DmmRepositoryDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("mer.NAME", DmmRepositoryDTO.ATTRIBUTE.MERCHANT_NAME.getValue());
			sqlStatement.addSelectClause("w.NAME", DmmRepositoryDTO.ATTRIBUTE.WAREHOUSE_ID.getValue());
			sqlStatement.addSelectClause("repos.DTID", DmmRepositoryDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("repos.SERIAL_NUMBER", DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("c.CONTRACT_CODE", DmmRepositoryDTO.ATTRIBUTE.CONTRACT_CODE.getValue());
			sqlStatement.addSelectClause("repos.ENABLE_DATE", DmmRepositoryDTO.ATTRIBUTE.ENABLE_DATE.getValue());
			sqlStatement.addSelectClause("repos.IS_ENABLED", DmmRepositoryDTO.ATTRIBUTE.IS_ENABLED.getValue());
			sqlStatement.addSelectClause("repos.CHECKED_DATE", DmmRepositoryDTO.ATTRIBUTE.CHECKED_DATE.getValue());
			sqlStatement.addSelectClause("repos.UPDATE_DATE", DmmRepositoryDTO.ATTRIBUTE.UPDATE_DATE.getValue());
			StringBuffer buffer1 = new StringBuffer();
			buffer1.append(" ( CASE WHEN repos.MA_TYPE = 'BUYOUT' ").append(" THEN repos.PROPERTY_ID ")
			.append(" else repos.SIM_ENABLE_NO ").append("  END )  ");
			sqlStatement.addSelectClause(buffer1.toString(), DmmRepositoryDTO.ATTRIBUTE.PROPERTY_ID.getValue());
			sqlStatement.addSelectClause("statusDef.ITEM_NAME", DmmRepositoryDTO.ATTRIBUTE.STATUS_NAME.getValue());
			sqlStatement.addSelectClause("repos.STATUS", DmmRepositoryDTO.ATTRIBUTE.STATUS.getValue());
			buffer.append(schema).append(".DMM_REPOSITORY_HISTORY_MONTHLY repos ");
			buffer.append("left join ").append(schema).append(".BIM_WAREHOUSE w on w.WAREHOUSE_ID = repos.WAREHOUSE_ID ");
			buffer.append("left join ").append(schema).append(".DMM_ASSET_TYPE asset on repos.ASSET_TYPE_ID = asset.ASSET_TYPE_ID ");
			buffer.append("left join ").append(schema).append(".BIM_MERCHANT mer on repos.MERCHANT_ID = mer.MERCHANT_ID ");
			buffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF statusDef on statusDef.BPTD_CODE = 'ASSET_STATUS' and statusDef.ITEM_VALUE = repos.STATUS ");
			buffer.append("left join ").append(schema).append(".BIM_CONTRACT c ON repos.CONTRACT_ID = c.CONTRACT_ID ");
			buffer.append("where ");
			//設備類別
			buffer.append(" repos.MONTH_YEAR = :yearMonthDay");
			//設備代碼
			if(StringUtils.hasText(customerCode)){
				buffer.append(" AND repos.ASSET_USER = :customerCode");
				LOGGER.debug(this.getClass().getName(), ".feeAssetListScsb() --> customerCode: ", customerCode);
			}
			buffer.append(" AND asset.ASSET_CATEGORY =:assetCategoryEDC");
			//排序
			sqlStatement.addFromExpression(buffer.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if(StringUtils.hasText(customerCode)){
				sqlQueryBean.setParameter(DmmRepositoryDTO.ATTRIBUTE.CUSTOMER_CODE.getValue(), customerCode);
			}
			sqlQueryBean.setParameter("yearMonthDay", yearMonthDay);
			sqlQueryBean.setParameter("assetCategoryEDC", IAtomsConstants.ASSET_CATEGORY_EDC);
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmRepositoryDTO.class);
			LOGGER.debug("feeAssetListScsb()", "listFeeAssetList:", sqlQueryBean.toString());	
			dmmRepositoryDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("feeAssetListScsb() is error.", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return dmmRepositoryDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryMonthlyDAO#assetInfoListChb(java.lang.String, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<DmmRepositoryDTO> assetInfoListChb(String yearMonthDay, String customerCode, Date startDate,
			Date endDate) throws DataAccessException {
		LOGGER.error(this.getClass().getName()+".assetInfoListChb() --> yearMonthDay: "+yearMonthDay);
		LOGGER.error(this.getClass().getName()+".assetInfoListChb() --> customerCode: "+customerCode);
		List<DmmRepositoryDTO> listDmmRepositoryHistoryDTO= null;
		try {
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append("SELECT");
			sql.append(" ROW_NUMBER() over (order by repos.ASSET_ID) as rowId, ");
			sql.append(" repos.ASSET_ID AS assetId, ");	
			sql.append(" asset.NAME as name ,");
			sql.append(" mer.MERCHANT_CODE AS merchantCode, ");	
			sql.append(" mer.NAME AS merchantName , ");
			sql.append(" w.NAME as warehouseId, ");
			sql.append(" repos.DTID as dtid , ");
			sql.append(" repos.SERIAL_NUMBER AS serialNumber , ");
			sql.append(" c.CONTRACT_CODE AS contractCode , ");
			sql.append(" repos.ENABLE_DATE AS enableDate ,");
			sql.append(" repos.UPDATE_DATE  AS updateDate, ");
			sql.append(" repos.CHECKED_DATE AS checkedDate , ");
			sql.append(" repos.STATUS AS status, ");
			sql.append(" statusDef.ITEM_NAME AS statusName ");
			sql.append(" INTO  #temp ");
			sql.append(" FROM ").append(schema).append(" .DMM_REPOSITORY_HISTORY_MONTHLY repos ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT c on repos.CONTRACT_ID = c.CONTRACT_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_WAREHOUSE w on w.WAREHOUSE_ID = repos.WAREHOUSE_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE asset on repos.ASSET_TYPE_ID = asset.ASSET_TYPE_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer on repos.MERCHANT_ID = mer.MERCHANT_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF statusDef on statusDef.BPTD_CODE =:asset_status and statusDef.ITEM_VALUE = repos.STATUS ");
			sql.append(" WHERE ");
			//設備類別
			sql.append(" repos.MONTH_YEAR = :monthYear");
			sql.setParameter(DmmRepositoryDTO.ATTRIBUTE.YEAR_MONTH_DAY.getValue(), yearMonthDay);
			//設備代碼
			if(StringUtils.hasText(customerCode)){
				sql.append(" and repos.ASSET_USER = :customerCode");
				sql.setParameter(DmmRepositoryDTO.ATTRIBUTE.CUSTOMER_CODE.getValue(), customerCode);
				LOGGER.debug(this.getClass().getName(), ".assetInfoListChb() --> customerCode: ", customerCode);
			}
			sql.append(" AND (asset.ASSET_CATEGORY =:assetCategoryEDC or (asset.ASSET_CATEGORY =:Related_Products and asset.NAME = 'S200') )");
			sql.append(" AND repos.IS_ENABLED =:yes ;");
			sql.append(" select * from #temp union all ");
			sql.append(" SELECT ROW_NUMBER() over (order by repos.ASSET_ID) as rowId, ");
			sql.append(" repos.ASSET_ID AS assetId, ");	
			sql.append(" asset.NAME as name, ");
			sql.append(" mer.MERCHANT_CODE AS merchantCode, ");	
			sql.append(" mer.NAME AS merchantName , ");
			sql.append(" w.NAME as warehouseId, ");
			sql.append(" repos.DTID as dtid , ");
			sql.append(" repos.SERIAL_NUMBER AS serialNumber , ");
			sql.append(" c.CONTRACT_CODE AS contractCode , ");
			sql.append(" repos.ENABLE_DATE AS enableDate ,");
			sql.append(" repos.UPDATE_DATE  AS updateDate, ");
			sql.append(" repos.CHECKED_DATE AS checkedDate , ");
			sql.append(" repos.STATUS AS status, ");
			sql.append(" statusDef.ITEM_NAME AS statusName ");
			sql.append(" FROM ").append(schema).append(" .DMM_REPOSITORY repos ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT c on repos.CONTRACT_ID = c.CONTRACT_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_WAREHOUSE w on w.WAREHOUSE_ID = repos.WAREHOUSE_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer on repos.MERCHANT_ID = mer.MERCHANT_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE asset on repos.ASSET_TYPE_ID = asset.ASSET_TYPE_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF statusDef on statusDef.BPTD_CODE =:asset_status and statusDef.ITEM_VALUE = repos.STATUS ");
			sql.append(" WHERE ");
			//設備類別
			sql.append(" repos.CASE_COMPLETION_DATE >= :startDate");
			sql.setParameter("startDate", startDate);
			sql.append(" AND repos.CASE_COMPLETION_DATE < :endDate");
			sql.setParameter("endDate", endDate);
			//設備代碼
			if(StringUtils.hasText(customerCode)){
				sql.append(" and repos.ASSET_USER = :customerCode");
				sql.setParameter(DmmRepositoryDTO.ATTRIBUTE.CUSTOMER_CODE.getValue(), customerCode);
				LOGGER.debug(this.getClass().getName(), ".assetInfoListChb() --> customerCode: ", customerCode);
			}
			sql.append(" AND (asset.ASSET_CATEGORY =:assetCategoryEDC or (asset.ASSET_CATEGORY =:Related_Products and asset.NAME = 'S200') )");
			//sql.append(" AND asset.ASSET_CATEGORY =:assetCategoryEDC");
			sql.append(" AND repos.IS_ENABLED =:yes ");
			sql.append(" AND NOT EXISTS( SELECT * FROM #temp t  WHERE repos.ASSET_ID = t.assetId ); ");
			sql.append(" drop TABLE #temp; ");
			//排序
			sql.setParameter("asset_status", IATOMS_PARAM_TYPE.ASSET_STATUS.getCode());
			sql.setParameter("yes", IAtomsConstants.YES);
			sql.setParameter("assetCategoryEDC", IAtomsConstants.ASSET_CATEGORY_EDC);
			//周邊設備
			sql.setParameter("Related_Products", IAtomsConstants.ASSET_CATEGORY_RODUND_ASSET);
			AliasBean alias = new AliasBean(DmmRepositoryDTO.class);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.ROWID.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.ASSET_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.MERCHANT_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.WAREHOUSE_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.CONTRACT_CODE.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.ENABLE_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.CHECKED_DATE.getValue(), DateType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.UPDATE_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.STATUS.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryDTO.ATTRIBUTE.STATUS_NAME.getValue(), StringType.INSTANCE);
			listDmmRepositoryHistoryDTO = this.getDaoSupport().findByNativeSql(sql, alias);
			LOGGER.debug(this.getClass().getName(), "assetInfoListChb() --> sql: ", sql.toString()); 
		} catch (Exception e) {
			LOGGER.error(".assetInfoListChb() is error", "DAO Exception", e);
			throw new DataAccessException(CoreMessageCode.QUERY_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.REB_IATOMS_TB_NAME_SUPPLIES)}, e);
		}
		return listDmmRepositoryHistoryDTO;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryMonthlyDAO#feeAssetListChb(java.lang.String, java.lang.String)
	 */
	@Override
	public List<DmmRepositoryDTO> feeAssetListChb(List<String> repoStatusList, String yearMonthDay,
			String customerCode, Date endDate) throws DataAccessException {
		LOGGER.debug("feeAssetListScsb()", "parameters:customerCode=", customerCode);
		LOGGER.debug("feeAssetListScsb()", "parameters:yearMonthDay=", yearMonthDay);
		List<DmmRepositoryDTO> dmmRepositoryDTOs = null;
		try{
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			StringBuffer buffer = new StringBuffer();
			sqlStatement.addSelectClause("ROW_NUMBER() over (order by repos.ASSET_ID)", DmmRepositoryDTO.ATTRIBUTE.ROWID.getValue());
			sqlStatement.addSelectClause("mer.MERCHANT_CODE", DmmRepositoryDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("mer.NAME", DmmRepositoryDTO.ATTRIBUTE.MERCHANT_NAME.getValue());
			sqlStatement.addSelectClause("w.NAME", DmmRepositoryDTO.ATTRIBUTE.WAREHOUSE_ID.getValue());
			sqlStatement.addSelectClause("repos.DTID", DmmRepositoryDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("repos.SERIAL_NUMBER", DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("c.CONTRACT_CODE", DmmRepositoryDTO.ATTRIBUTE.CONTRACT_CODE.getValue());
			sqlStatement.addSelectClause("asset.NAME", DmmRepositoryDTO.ATTRIBUTE.NAME.getValue());
			StringBuffer buffer2 = new StringBuffer();
			buffer2.append(" STUFF((SELECT ','+ ltrim(bcomm.ITEM_VALUE) FROM dbo.BASE_PARAMETER_ITEM_DEF bcomm,dbo.DMM_ASSET_SUPPORTED_COMM comm where comm.ASSET_TYPE_ID = asset.ASSET_TYPE_ID and comm.COMM_MODE_ID= bcomm.ITEM_VALUE for XML PATH('')), 1, 1, '') ");
			sqlStatement.addSelectClause(buffer2.toString(), DmmRepositoryDTO.ATTRIBUTE.COMMMODE_ID.getValue());
			sqlStatement.addSelectClause("repos.ENABLE_DATE", DmmRepositoryDTO.ATTRIBUTE.ENABLE_DATE.getValue());
			sqlStatement.addSelectClause("repos.UPDATE_DATE", DmmRepositoryDTO.ATTRIBUTE.UPDATE_DATE.getValue());
			sqlStatement.addSelectClause("statusDef.ITEM_NAME", DmmRepositoryDTO.ATTRIBUTE.STATUS.getValue());
			buffer.append(schema).append(".DMM_REPOSITORY_HISTORY_MONTHLY repos ");
			buffer.append("left join ").append(schema).append(".BIM_WAREHOUSE w on w.WAREHOUSE_ID = repos.WAREHOUSE_ID ");
			buffer.append("left join ").append(schema).append(".DMM_ASSET_TYPE asset on repos.ASSET_TYPE_ID = asset.ASSET_TYPE_ID ");
			buffer.append("left join ").append(schema).append(".BIM_MERCHANT mer on repos.MERCHANT_ID = mer.MERCHANT_ID ");
			buffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF statusDef on statusDef.BPTD_CODE = 'ASSET_STATUS' and statusDef.ITEM_VALUE = repos.STATUS ");
			buffer.append("left join ").append(schema).append(".BIM_CONTRACT c ON repos.CONTRACT_ID = c.CONTRACT_ID ");
			buffer.append("where ");
			//設備類別
			buffer.append(" repos.MONTH_YEAR = :yearMonthDay");
			//設備代碼
			if(StringUtils.hasText(customerCode)){
				buffer.append(" AND repos.ASSET_USER = :customerCode");
				LOGGER.debug(this.getClass().getName(), ".feeAssetListScsb() --> customerCode: ", customerCode);
			}
			buffer.append(" AND asset.ASSET_CATEGORY =:assetCategoryEDC");
			buffer.append(" AND repos.STATUS NOT IN( :repoStatusList)");
			// Bug #3232 維護費報表啟用日期調整   (設備啟用日期 + 3年) > 帳務年月月底/ 設備啟用日期 > (帳務年月月底 - 3年)
		//	buffer.append("  AND repos.ENABLE_DATE < dateadd( MONTH ,-37 ,:endDate)  ");
			buffer.append("  AND repos.ENABLE_DATE < dateadd( MONTH ,-36 ,:endDate)  ");
			//排序
			sqlStatement.addFromExpression(buffer.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if(StringUtils.hasText(customerCode)){
				sqlQueryBean.setParameter(DmmRepositoryDTO.ATTRIBUTE.CUSTOMER_CODE.getValue(), customerCode);
			}
			sqlQueryBean.setParameter("yearMonthDay", yearMonthDay);
			sqlQueryBean.setParameter("endDate", endDate);
			sqlQueryBean.setParameter("assetCategoryEDC", IAtomsConstants.ASSET_CATEGORY_EDC);
			sqlQueryBean.setParameter("repoStatusList", repoStatusList);
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmRepositoryDTO.class);
			LOGGER.debug("feeAssetListScsb()", "listFeeAssetList:", sqlQueryBean.toString());	
			dmmRepositoryDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("feeAssetListScsb() is error.", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return dmmRepositoryDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryMonthlyDAO#getRepositoryBySerialNumber(java.lang.String)
	 */
	@Override
	public DmmRepositoryDTO getRepositoryBySerialNumber(String serialNumber, String monthYear, String state) throws DataAccessException {
		LOGGER.debug(this.getClass().getSimpleName() + "getRepositoryBySerialNumber", "serialNumber:" + serialNumber);
		LOGGER.debug(this.getClass().getSimpleName() + "getRepositoryBySerialNumber", "monthYear:" + monthYear);
		//庫存信息
		DmmRepositoryDTO repositoryDTO = null;
		List<DmmRepositoryDTO> result = null;
		//得到schema
		String schema = this.getMySchema();
		//創建SqlStatement
		SqlStatement sqlStatement = new SqlStatement();
		try{
			sqlStatement.addSelectClause("repository.ASSET_ID", DmmRepositoryDTO.ATTRIBUTE.ASSET_ID.getValue());
			sqlStatement.addSelectClause("repository.SERIAL_NUMBER", DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("repository.ASSET_TYPE_ID", DmmRepositoryDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			sqlStatement.addSelectClause("type.NAME", DmmRepositoryDTO.ATTRIBUTE.NAME.getValue());
			sqlStatement.addSelectClause("PROPERTY_ID", DmmRepositoryDTO.ATTRIBUTE.PROPERTY_ID.getValue());
			sqlStatement.addSelectClause("repository.WAREHOUSE_ID", DmmRepositoryDTO.ATTRIBUTE.WAREHOUSE_ID.getValue());
			sqlStatement.addSelectClause("repository.CONTRACT_ID", DmmRepositoryDTO.ATTRIBUTE.CONTRACT_ID.getValue());
			sqlStatement.addSelectClause("contract.CONTRACT_CODE", DmmRepositoryDTO.ATTRIBUTE.CONTRACT_CODE.getValue());
			sqlStatement.addSelectClause("repository.MA_TYPE", DmmRepositoryDTO.ATTRIBUTE.MA_TYPE.getValue());
			sqlStatement.addSelectClause("repository.STATUS", DmmRepositoryDTO.ATTRIBUTE.STATUS.getValue());
			sqlStatement.addSelectClause("repository.IS_ENABLED", DmmRepositoryDTO.ATTRIBUTE.IS_ENABLED.getValue());
			sqlStatement.addSelectClause("repository.ENABLE_DATE", DmmRepositoryDTO.ATTRIBUTE.ENABLE_DATE.getValue());
			//sqlStatement.addSelectClause("repository.IS_SIM_ENABLE", DmmRepositoryDTO.ATTRIBUTE.IS_SIM_ENABLE.getValue());
			sqlStatement.addSelectClause("repository.SIM_ENABLE_DATE", DmmRepositoryDTO.ATTRIBUTE.SIM_ENABLE_DATE.getValue());
			sqlStatement.addSelectClause("repository.SIM_ENABLE_NO", DmmRepositoryDTO.ATTRIBUTE.SIM_ENABLE_NO.getValue());
			sqlStatement.addSelectClause("repository.CARRIER", DmmRepositoryDTO.ATTRIBUTE.CARRIER.getValue());
			sqlStatement.addSelectClause("repository.CARRY_DATE", DmmRepositoryDTO.ATTRIBUTE.CARRY_DATE.getValue());
			sqlStatement.addSelectClause("repository.BORROWER", DmmRepositoryDTO.ATTRIBUTE.BORROWER.getValue());
			sqlStatement.addSelectClause("repository.BORROWER_START", DmmRepositoryDTO.ATTRIBUTE.BORROWER_START.getValue());
			sqlStatement.addSelectClause("repository.BORROWER_END", DmmRepositoryDTO.ATTRIBUTE.BORROWER_END.getValue());
			sqlStatement.addSelectClause("repository.BORROWER_EMAIL", DmmRepositoryDTO.ATTRIBUTE.BORROWER_EMAIL.getValue());
			sqlStatement.addSelectClause("repository.BORROWER_MGR_EMAIL", DmmRepositoryDTO.ATTRIBUTE.BORROWER_MGR_EMAIL.getValue());
			sqlStatement.addSelectClause("repository.BACK_DATE", DmmRepositoryDTO.ATTRIBUTE.BACK_DATE.getValue());
			sqlStatement.addSelectClause("repository.ASSET_OWNER", DmmRepositoryDTO.ATTRIBUTE.ASSET_OWNER.getValue());
			sqlStatement.addSelectClause("company.SHORT_NAME", DmmRepositoryDTO.ATTRIBUTE.ASSET_OWNER_NAME.getValue());
			sqlStatement.addSelectClause("repository.ASSET_USER", DmmRepositoryDTO.ATTRIBUTE.ASSET_USER.getValue());
			sqlStatement.addSelectClause("companyUser.SHORT_NAME", DmmRepositoryDTO.ATTRIBUTE.ASSET_USER_NAME.getValue());
			sqlStatement.addSelectClause("companyUser.COMPANY_CODE", DmmRepositoryDTO.ATTRIBUTE.COMPANY_CODE.getValue());
			sqlStatement.addSelectClause("repository.IS_CUP", DmmRepositoryDTO.ATTRIBUTE.IS_CUP.getValue());
			/*sqlStatement.addSelectClause("repository.RETIRE_REASON_CODE", DmmRepositoryDTO.ATTRIBUTE.RETIRE_REASON_CODE.getValue());
			sqlStatement.addSelectClause("repository.CASE_ID", DmmRepositoryDTO.ATTRIBUTE.CASE_ID.getValue());*/
			/*sqlStatement.addSelectClause("repository.RETIRE_COMMENT", DmmRepositoryDTO.ATTRIBUTE.REPAIR_COMMENT.getValue());*/
			sqlStatement.addSelectClause("repository.CASE_ID", DmmRepositoryDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("repository.CASE_COMPLETION_DATE", DmmRepositoryDTO.ATTRIBUTE.CASE_COMPLETION_DATE.getValue());
//			sqlStatement.addSelectClause("repository.CASE_CLOSE_DATE", DmmRepositoryDTO.ATTRIBUTE.CASE_CLOSE_DATE.getValue());
			sqlStatement.addSelectClause("repository.TID", DmmRepositoryDTO.ATTRIBUTE.TID.getValue());
			sqlStatement.addSelectClause("repository.DTID", DmmRepositoryDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("repository.APPLICATION_ID", DmmRepositoryDTO.ATTRIBUTE.APPLICATION_ID.getValue());
			sqlStatement.addSelectClause("repository.MERCHANT_ID", DmmRepositoryDTO.ATTRIBUTE.MERCHANT_ID.getValue());
			sqlStatement.addSelectClause("merchant.MERCHANT_CODE", DmmRepositoryDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("merchantHeader.HEADER_NAME", DmmRepositoryDTO.ATTRIBUTE.HEADER_NAME.getValue());
			sqlStatement.addSelectClause("repository.ASSET_IN_ID", DmmRepositoryDTO.ATTRIBUTE.ASSET_IN_ID.getValue());
			sqlStatement.addSelectClause("repository.ASSET_IN_TIME", DmmRepositoryDTO.ATTRIBUTE.ASSET_IN_TIME.getValue());
			sqlStatement.addSelectClause("repository.ASSET_TRANS_ID", DmmRepositoryDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue());
			sqlStatement.addSelectClause("repository.MAINTAIN_COMPANY", DmmRepositoryDTO.ATTRIBUTE.MAINTAIN_COMPANY.getValue());
			sqlStatement.addSelectClause("companyMaintain.SHORT_NAME", DmmRepositoryDTO.ATTRIBUTE.MAINTAIN_COMPANY_NAME.getValue());
			sqlStatement.addSelectClause("repository.REPAIR_VENDOR", DmmRepositoryDTO.ATTRIBUTE.REPAIR_VENDOR.getValue());
			sqlStatement.addSelectClause("repository.DESCRIPTION", DmmRepositoryDTO.ATTRIBUTE.DESCRIPTION.getValue());
			sqlStatement.addSelectClause("repository.ACTION", DmmRepositoryDTO.ATTRIBUTE.ACTION.getValue());
			sqlStatement.addSelectClause("repository.FAULT_COMPONENT", DmmRepositoryDTO.ATTRIBUTE.FAULT_COMPONENT.getValue());
			sqlStatement.addSelectClause("repository.FAULT_DESCRIPTION", DmmRepositoryDTO.ATTRIBUTE.FAULT_DESCRIPTION.getValue());
			sqlStatement.addSelectClause("repository.CUSTOMER_WARRANTY_DATE", DmmRepositoryDTO.ATTRIBUTE.CUSTOMER_WARRANTY_DATE.getValue());
			sqlStatement.addSelectClause("repository.FACTORY_WARRANTY_DATE", DmmRepositoryDTO.ATTRIBUTE.FACTORY_WARRANTY_DATE.getValue());
			sqlStatement.addSelectClause("repository.CREATE_USER", DmmRepositoryDTO.ATTRIBUTE.CREATE_USER.getValue());
			sqlStatement.addSelectClause("repository.CREATE_USER_NAME", DmmRepositoryDTO.ATTRIBUTE.CREATE_USER_NAME.getValue());
			sqlStatement.addSelectClause("repository.CREATE_DATE", DmmRepositoryDTO.ATTRIBUTE.CREATE_DATE.getValue());
			sqlStatement.addSelectClause("repository.UPDATE_USER", DmmRepositoryDTO.ATTRIBUTE.UPDATE_USER.getValue());
			sqlStatement.addSelectClause("repository.UPDATE_USER_NAME", DmmRepositoryDTO.ATTRIBUTE.UPDATE_USER_NAME.getValue());
			sqlStatement.addSelectClause("repository.UPDATE_DATE", DmmRepositoryDTO.ATTRIBUTE.UPDATE_DATE.getValue());
//			sqlStatement.addSelectClause("repository.DELETED", DmmRepositoryDTO.ATTRIBUTE.DELETED.getValue());
			sqlStatement.addSelectClause("repository.CHECKED_DATE", DmmRepositoryDTO.ATTRIBUTE.CHECKED_DATE.getValue());
			sqlStatement.addSelectClause("repository.CYBER_APPROVED_DATE", DmmRepositoryDTO.ATTRIBUTE.CYBER_APPROVED_DATE.getValue());
			sqlStatement.addSelectClause("repository.ASSET_MODEL", DmmRepositoryDTO.ATTRIBUTE.ASSET_MODEL.getValue());
			sqlStatement.addSelectClause("repository.IS_CHECKED", DmmRepositoryDTO.ATTRIBUTE.IS_CHECKED.getValue());
			sqlStatement.addSelectClause("repository.IS_CUSTOMER_CHECKED", DmmRepositoryDTO.ATTRIBUTE.IS_CUSTOMER_CHECKED.getValue());
			sqlStatement.addSelectClause("repository.DEPARTMENT_ID", DmmRepositoryDTO.ATTRIBUTE.DEPARTMENT_ID.getValue());
			sqlStatement.addSelectClause("repository.REPAIR_COMPANY", DmmRepositoryDTO.ATTRIBUTE.REPAIR_COMPANY.getValue());
			sqlStatement.addSelectClause("repository.BRAND", DmmRepositoryDTO.ATTRIBUTE.BRAND.getValue());
			sqlStatement.addSelectClause("repository.MAINTAIN_USER", DmmRepositoryDTO.ATTRIBUTE.MAINTAIN_USER.getValue());
			sqlStatement.addSelectClause("admUser.CNAME", DmmRepositoryDTO.ATTRIBUTE.MAINTAIN_USER_NAME.getValue());
			sqlStatement.addSelectClause("admUser.ENAME", DmmRepositoryDTO.ATTRIBUTE.ENAME.getValue());
			sqlStatement.addSelectClause("repository.ANALYZE_DATE", DmmRepositoryDTO.ATTRIBUTE.ANALYZE_DATE.getValue());
			sqlStatement.addSelectClause("repository.UNINSTALL_OR_REPAIR_REASON", DmmRepositoryDTO.ATTRIBUTE.UNINSTALL_OR_REPAIRREASON.getValue());
			
			sqlStatement.addSelectClause("repository.COUNTER", DmmRepositoryDTO.ATTRIBUTE.COUNTER.getValue());
			sqlStatement.addSelectClause("repository.CARTON_NO", DmmRepositoryDTO.ATTRIBUTE.CARTON_NO.getValue());
			sqlStatement.addSelectClause("repository.INSTALLED_DEPT_ID", DmmRepositoryDTO.ATTRIBUTE.INSTALLED_DEPT_ID.getValue());
			sqlStatement.addSelectClause("itemType.ITEM_NAME", DmmRepositoryDTO.ATTRIBUTE.INSTALLED_ADRESS_LOCATION_NAME.getValue());
			sqlStatement.addSelectClause("repository.INSTALLED_ADRESS", DmmRepositoryDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue());
			sqlStatement.addFromExpression(schema + ".DMM_REPOSITORY_HISTORY_MONTHLY repository"
							+ " left join " + schema + ".DMM_ASSET_TYPE AS type ON repository.ASSET_TYPE_ID = type.ASSET_TYPE_ID"
							+ " left join " + schema + ".BIM_COMPANY company on company.COMPANY_ID = repository.ASSET_OWNER"
							+ " left join " + schema + ".BIM_COMPANY companyUser on companyUser.COMPANY_ID = repository.ASSET_USER"
							+ " left join " + schema + ".BIM_COMPANY companyMaintain on companyMaintain.COMPANY_ID = repository.MAINTAIN_COMPANY"
							+ " left join " + schema + ".BIM_MERCHANT merchant on merchant.MERCHANT_ID = repository.MERCHANT_ID"
							+ " left join " + schema + ".BIM_MERCHANT_HEADER merchantHeader on merchantHeader.MERCHANT_HEADER_ID = repository.MERCHANT_HEADER_ID"
							+ " left join " + schema + ".ADM_USER admUser on admUser.USER_ID = repository.MAINTAIN_USER"
							+ " left join " + schema + ".BIM_CONTRACT contract on contract.CONTRACT_ID = repository.CONTRACT_ID"
							+ " left join " + schema + ".BASE_PARAMETER_ITEM_DEF itemType on itemType.ITEM_VALUE = repository.INSTALLED_ADRESS_LOCATION and itemType.BPTD_CODE = :location");
			if (StringUtils.hasText(serialNumber)) {
				sqlStatement.addWhereClause("repository.SERIAL_NUMBER =:serialNumber", serialNumber);
			}
			if (StringUtils.hasText(monthYear)) {
				sqlStatement.addWhereClause("repository.MONTH_YEAR = :monthYear", monthYear);
			}
			//設備狀態為庫存
			if (StringUtils.hasText(state)) {
				sqlStatement.addWhereClause("repository.STATUS =:status", state);
			}
			//判斷刪除標誌
//			sqlStatement.addWhereClause("ISNULL(repository.DELETED, 'N') !=:deleted",IAtomsConstants.YES);
			//記錄sql語句
			LOGGER.debug(this.getClass().getName() + "list ---->sql:" + sqlStatement.toString() );
			//創建SqlQueryBean
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("location", IATOMS_PARAM_TYPE.LOCATION.getCode());
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmRepositoryDTO.class);
			//執行sql語句
			result = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(result)) {
				repositoryDTO = result.get(0);
			}
		} catch(Exception e) {
			LOGGER.error("DmmRepositoryDAO.getRepositoryBySerialNumber() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		return repositoryDTO;
	}
}
