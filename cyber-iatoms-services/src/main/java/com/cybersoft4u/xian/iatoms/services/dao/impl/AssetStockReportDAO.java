package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import cafe.core.config.GenericConfigManager;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetStockReportDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IAssetStockReportDAO;

/**
 * Purpose: 設備庫存表DAO interface
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016-7-29
 * @MaintenancePersonnel CrissZhang
 */
public class AssetStockReportDAO extends GenericBaseDAO implements IAssetStockReportDAO{
	
	/**
	 * 日志記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.DAO, AssetStockReportDAO.class);

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetStockReportDAO#list(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<AssetStockReportDTO> listBy(String queryTableName, String queryCustomerId, String queryMaintainMode,
			String queryMonth, String roleAttribute, String dataAcl, String userId) throws DataAccessException {
		LOGGER.debug("listBy()", "parameters:queryTableName =" + queryTableName);
		LOGGER.debug("listBy()", "parameters:queryCustomerId =" + queryCustomerId);
		LOGGER.debug("listBy()", "parameters:queryMaintainMode =" + queryMaintainMode);
		LOGGER.debug("listBy()", "parameters:queryMonth =" + queryMonth);
		LOGGER.debug("listBy()", "parameters:roleAttribute =" + roleAttribute);
		LOGGER.debug("list()", "parameters:dataAcl =" + dataAcl);
		LOGGER.debug("list()", "parameters:userId =" + userId);

		List<AssetStockReportDTO> results = null;
		int size = 0;
		String warehouse = null;
		// 查詢的設備類別
		String queryAssetCategory = IAtomsConstants.ASSET_CATEGORY_RODUND_ASSET + IAtomsConstants.MARK_SEPARATOR + IAtomsConstants.ASSET_CATEGORY_EDC;
		try{
			SqlStatement sqlStatement = new SqlStatement();
			//SqlStatement sql = new SqlStatement();
			String schema = this.getMySchema();
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			StringBuilder builder = new StringBuilder();
			builder.append("select  c.SHORT_NAME AS companyName ,c.COMPANY_ID AS companyId ,");
			builder.append(" stuff( ( SELECT ',' + RTRIM( commMode.ITEM_NAME ) FROM ");
			builder.append(schema).append(".DMM_ASSET_SUPPORTED_COMM assetSupportComm LEFT JOIN ");
			builder.append(schema).append(".BASE_PARAMETER_ITEM_DEF commMode ON commMode.ITEM_VALUE = assetSupportComm.COMM_MODE_ID ");
			builder.append(" AND commMode.BPTD_CODE =:COMM_MODE WHERE assetSupportComm.ASSET_TYPE_ID = conAsset.ASSET_TYPE_ID FOR XML path('')), 1,1,'') as maTypeName, ");
			sqlQueryBean.append(builder.toString());
			//sqlStatement.addSelectClause(builder.toString(), AssetStockReportDTO.ATTRIBUTE.MA_TYPE_NAME.getValue());
			builder = new StringBuilder();
			builder.append("stuff(( SELECT ',' + RTRIM( supportFunction.ITEM_NAME ) FROM ");
			builder.append(schema).append(".DMM_ASSET_SUPPORTED_FUNCTION assetSupportFunction LEFT JOIN ");
			builder.append(schema).append(".BASE_PARAMETER_ITEM_DEF supportFunction ON supportFunction.ITEM_VALUE = assetSupportFunction.FUNCTION_ID");
			builder.append(" AND supportFunction.BPTD_CODE =:SUPPORTED_FUNCTION WHERE assetSupportFunction.ASSET_TYPE_ID = conAsset.ASSET_TYPE_ID FOR XML path('')), 1, 1,'') as supporedFunctionName,");
			sqlQueryBean.append(builder.toString());
			builder = new StringBuilder();
			//--採購台數
			//由於Task #2459合約設備數量改為非必填，增加判空 2017/10/25
			sqlQueryBean.append(" asset.ASSET_CATEGORY AS assetCategory, assetCategory.ITEM_NAME AS assetCategoryName,conAsset.ASSET_TYPE_ID AS assetTypeId ,asset.NAME AS assetTypeName ,con.START_DATE AS purchaseYearMonth ,con.CONTRACT_ID AS contract , con.CONTRACT_CODE AS contractId ,(case when max( conAsset.AMOUNT ) is null then 0 else max( conAsset.AMOUNT ) end ) AS purchaseNumber,");
			//--入庫台數
			sqlQueryBean.append(" sum(CASE WHEN repos.ASSET_ID IS NULL THEN 0 ELSE 1 END ) AS  storageNumber ,");
            //--入庫台數
            sqlQueryBean.append(" SUM( CASE repos.STATUS WHEN 'DESTROY' THEN 1  ELSE 0  END ) AS destoryedNumber,");
            
            //--已銷毀
            sqlQueryBean.append(" SUM( CASE  repos.STATUS WHEN 'DISABLED' THEN 1 ELSE 0 END ) AS scrapedNumber,");
            
            //--已報廢
            sqlQueryBean.append(" SUM( CASE repos.STATUS WHEN 'PENDING_DISABLED' THEN 1 ELSE 0 END ) AS loseNumber,");
            // --待報廢
            sqlQueryBean.append(" SUM( CASE repos.STATUS WHEN 'MAINTENANCE' THEN 1 ELSE 0 END ) AS maintenanceNumber,");
            //--送修中
            sqlQueryBean.append(" SUM( CASE repos.STATUS WHEN 'IN_USE' THEN 1 WHEN 'BORROWING' THEN 1 ELSE 0 END ) AS stepNumber,");
            //Bug #2388  update by 2017/09/13  --維修中
            sqlQueryBean.append(" SUM( CASE repos.STATUS WHEN 'REPAIR' THEN 1 ELSE 0 END ) AS repairedNumber,");
            
            //--已佈台數
            sqlQueryBean.append(" SUM( CASE repos.STATUS WHEN 'REPERTORY' THEN 1 WHEN 'IN_APPLY' THEN 1 WHEN 'ON_WAY' THEN 1 ELSE 0 END) AS availableNumber,");
            //--可用台數
            //sqlQueryBean.append(" SUM(conAsset.AMOUNT) AS purchaseNumber,");
            //--採購台數
            sqlQueryBean.append(" 0 AS preparationNumber,");
            //--備機
            sqlQueryBean.append(" asset.ASSET_CATEGORY AS assetCategory, assetCategory.ITEM_NAME AS assetCategoryName ");
            sqlQueryBean.append(" FROM ");
            builder = new StringBuilder();
            builder.append(schema).append(".BIM_CONTRACT_ASSET conAsset LEFT JOIN ");
            builder.append(schema).append(".BIM_CONTRACT con  ON conAsset.CONTRACT_ID = con.CONTRACT_ID INNER JOIN ");
            builder.append(schema).append(".BIM_COMPANY c  ON con.COMPANY_ID = c.COMPANY_ID INNER JOIN ");
            
            builder.append(schema).append(".DMM_ASSET_TYPE asset ON conAsset.ASSET_TYPE_ID = asset.ASSET_TYPE_ID INNER JOIN ");
            builder.append(schema).append(".BASE_PARAMETER_ITEM_DEF assetCategory ON assetCategory.ITEM_VALUE = asset.ASSET_CATEGORY AND assetCategory.BPTD_CODE =:ASSET_CATEGORY LEFT JOIN ");
            builder.append(schema).append(".").append(queryTableName).append(" repos ON conAsset.CONTRACT_ID = repos.CONTRACT_ID AND repos.ASSET_TYPE_ID = conAsset.ASSET_TYPE_ID ");
            
            //--登入者為廠商角色,且登陸者DATA_ACL = 'Y'，查詢空管之設備資料
            if (IAtomsConstants.VECTOR_ROLE_ATTRIBUTE.equals(roleAttribute) && IAtomsConstants.PARAM_YES.equals(dataAcl)) {
                builder.append(" left join ").append(schema).append(".ADM_USER_WAREHOUSE uw on repos.WAREHOUSE_ID = uw.WAREHOUSE_ID ");
			}
            sqlQueryBean.append(builder.toString());
            sqlQueryBean.append(" WHERE 1=1 and c.AUTHENTICATION_TYPE =:IATOMS_AUTHEN ");
			//COMM_MODE SUPPORTED_FUNCTION ASSET_CATEGORY IATOMS_AUTHEN EDC Related_Products
			sqlQueryBean.setParameter("COMM_MODE", IATOMS_PARAM_TYPE.COMM_MODE.getCode());
			sqlQueryBean.setParameter("SUPPORTED_FUNCTION", IATOMS_PARAM_TYPE.SUPPORTED_FUNCTION.getCode());
			sqlQueryBean.setParameter("ASSET_CATEGORY", IATOMS_PARAM_TYPE.ASSET_CATEGORY.getCode());
			sqlQueryBean.setParameter("IATOMS_AUTHEN", IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE);
			sqlQueryBean.setParameter("EDC", IAtomsConstants.ASSET_CATEGORY_EDC);
			sqlQueryBean.setParameter("Related_Products", IAtomsConstants.ASSET_CATEGORY_RODUND_ASSET);

            //如果當前月，沒有yyyy/mm的查詢條件
			if (StringUtils.hasText(queryMonth) && !IAtomsConstants.IATOMS_TB_NAME_REPOSITORY.equals(queryTableName)) {
				sqlQueryBean.append(" AND repos.MONTH_YEAR =:queryMonth");
				sqlQueryBean.setParameter("queryMonth", queryMonth);
			}
			if (StringUtils.hasText(queryMaintainMode)) {
				sqlQueryBean.append(" AND repos.MA_TYPE =:queryMaintainMode");
				sqlQueryBean.setParameter("queryMaintainMode", queryMaintainMode);
			}
			if (StringUtils.hasText(queryCustomerId)) {
				sqlQueryBean.append(" AND con.COMPANY_ID =:queryCustomerId");
				sqlQueryBean.setParameter("queryCustomerId", queryCustomerId);
			}
			//--登入者為廠商角色,且登陸者DATA_ACL = 'Y'，查詢空管之設備資料
			if (IAtomsConstants.VECTOR_ROLE_ATTRIBUTE.equals(roleAttribute) && IAtomsConstants.PARAM_YES.equals(dataAcl)) {
				sqlQueryBean.append(" and uw.USER_ID = :userId");
				sqlQueryBean.setParameter("userId", userId);
			}
            
			sqlQueryBean.append(" AND(asset.ASSET_CATEGORY =:EDC OR asset.ASSET_CATEGORY =:Related_Products ) ");
			
            sqlQueryBean.append(" GROUP BY c.COMPANY_ID ,c.SHORT_NAME ,asset.ASSET_CATEGORY ,assetCategory.ITEM_NAME , conAsset.ASSET_TYPE_ID ,asset.NAME ,con.START_DATE, con.CONTRACT_ID ,con.CONTRACT_CODE ");
            sqlQueryBean.append(" ORDER BY C.SHORT_NAME,assetCategory.ITEM_NAME ,asset.NAME ,con.CONTRACT_CODE ");
            AliasBean alias = new AliasBean(AssetStockReportDTO.class);
            alias.addScalar(AssetStockReportDTO.ATTRIBUTE.MA_TYPE_NAME.getValue().toString(), StringType.INSTANCE);
			alias.addScalar(AssetStockReportDTO.ATTRIBUTE.SUPPORED_FUNCTION_NAME.getValue().toString(), StringType.INSTANCE);
			alias.addScalar(AssetStockReportDTO.ATTRIBUTE.COMPANY_NAME.getValue().toString(), StringType.INSTANCE);
			alias.addScalar(AssetStockReportDTO.ATTRIBUTE.COMPANY_ID.getValue().toString(), StringType.INSTANCE);
			alias.addScalar(AssetStockReportDTO.ATTRIBUTE.CONTRACT_ID.getValue().toString(), StringType.INSTANCE);
			
			alias.addScalar(AssetStockReportDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue().toString(), StringType.INSTANCE);
			alias.addScalar(AssetStockReportDTO.ATTRIBUTE.PURCHASE_YEAR_MONTH.getValue().toString(), DateType.INSTANCE);
			alias.addScalar(AssetStockReportDTO.ATTRIBUTE.ASSET_TYPE_NAME.getValue().toString(), StringType.INSTANCE);
			alias.addScalar(AssetStockReportDTO.ATTRIBUTE.STORAGE_NUMBER.getValue().toString(), IntegerType.INSTANCE);
			alias.addScalar(AssetStockReportDTO.ATTRIBUTE.DESTORYED_NUMBER.getValue().toString(), IntegerType.INSTANCE);
			alias.addScalar(AssetStockReportDTO.ATTRIBUTE.SCRAPED_NUMBER.getValue().toString(), IntegerType.INSTANCE);
			alias.addScalar(AssetStockReportDTO.ATTRIBUTE.LOSE_NUMBER.getValue().toString(), IntegerType.INSTANCE);
			alias.addScalar(AssetStockReportDTO.ATTRIBUTE.MAINTENANCE_NUMBER.getValue().toString(), IntegerType.INSTANCE);
			alias.addScalar(AssetStockReportDTO.ATTRIBUTE.STEP_NUMBER.getValue().toString(), IntegerType.INSTANCE);
			alias.addScalar(AssetStockReportDTO.ATTRIBUTE.CONTRACT.getValue().toString(), StringType.INSTANCE);

			alias.addScalar(AssetStockReportDTO.ATTRIBUTE.AVAILABLE_NUMBER.getValue().toString(), IntegerType.INSTANCE);
			alias.addScalar(AssetStockReportDTO.ATTRIBUTE.PURCHASE_NUMBER.getValue().toString(), LongType.INSTANCE);
			alias.addScalar(AssetStockReportDTO.ATTRIBUTE.PREPARATION_NUMBER.getValue().toString(), IntegerType.INSTANCE);
			alias.addScalar(AssetStockReportDTO.ATTRIBUTE.ASSET_CATEGORY.getValue().toString(), StringType.INSTANCE);
			alias.addScalar(AssetStockReportDTO.ATTRIBUTE.ASSET_CATEGORY_NAME.getValue().toString(), StringType.INSTANCE);
			alias.addScalar(AssetStockReportDTO.ATTRIBUTE.REPAIRED_NUMBER.getValue().toString(), IntegerType.INSTANCE);
			
			results = this.getDaoSupport().findByNativeSql(sqlQueryBean, alias);
			LOGGER.debug("listBy()", "sql:" + sqlStatement.toString());
		}catch(Exception e){
			LOGGER.error("listBy()", e.getMessage(), e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
		return results;
	}
}
