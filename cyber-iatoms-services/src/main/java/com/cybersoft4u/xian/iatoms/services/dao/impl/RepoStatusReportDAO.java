package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.util.CollectionUtils;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.RepoStatusReportDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IRepoStatusReportDAO;

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

/**
 * Purpose: 設筆狀態報表DTO
 * @author barryzhang
 * @since  JDK 1.6
 * @date   2016/9/6
 * @MaintenancePersonnel barryzhang
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class RepoStatusReportDAO extends GenericBaseDAO implements IRepoStatusReportDAO{
	
	/**
	 * 日志記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(RepoStatusReportDAO.class);

	/**
	 * Constructor: 無參構造
	 */
	public RepoStatusReportDAO() {

	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IRepoStatusReportDAO#listBy(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<RepoStatusReportDTO> listBy(String tableName, String companyId, String maTypeCode, String assetTypeId, 
			String commModeId, String date, String order, String sort, List<String> warehouseList, String dataAcl) throws DataAccessException {
		LOGGER.debug("listBy()", "parameters:tableName=", tableName);
		LOGGER.debug("listBy()", "parameters:companyId=", companyId);
		LOGGER.debug("listBy()", "parameters:maTypeCode=", maTypeCode);
		LOGGER.debug("listBy()", "parameters:assetTypeId=", assetTypeId);
		LOGGER.debug("listBy()", "parameters:commModeId=", commModeId);
		LOGGER.debug("listBy()", "parameters:order=", order);
		LOGGER.debug("listBy()", "parameters:sort=", sort);
		LOGGER.debug("listBy()", "parameters:warehouseList=", warehouseList.toString());

		//查詢結果集
		List<RepoStatusReportDTO> results = null;
		try{
			//創建sqlQueryBean
			SqlQueryBean sql = new SqlQueryBean();
			String schema = this.getMySchema();
			StringBuffer stringBuffer = new StringBuffer();
			//拼接SQL
			stringBuffer.append("SELECT bcom.SHORT_NAME + wh.NAME + ass.NAME AS tempValue , stuff((SELECT ',' + [commModeName] FROM (SELECT  Distinct commName.ITEM_NAME AS commModeName, comm.ASSET_TYPE_ID as ASSET_TYPE_ID  from ")
				.append(schema).append(".DMM_ASSET_SUPPORTED_COMM comm LEFT JOIN ").append(schema);
			
		//	stringBuffer.append(".BASE_PARAMETER_ITEM_DEF commName ON commName.ITEM_VALUE = comm.COMM_MODE_ID AND commName.APPROVED_FLAG = '").append(IAtomsConstants.YES).append("' AND commName.BPTD_CODE = '")
			// 處理approvedFlag
			stringBuffer.append(".BASE_PARAMETER_ITEM_DEF commName ON commName.ITEM_VALUE = comm.COMM_MODE_ID AND isnull(commName.DELETED,'N') = '").append(IAtomsConstants.NO).append("' AND commName.BPTD_CODE = '")
				.append(IAtomsConstants.COMM_MODE).append("') B where repo.ASSET_TYPE_ID = ASSET_TYPE_ID FOR XML path('')), 1, 1,'') as commModeName, bcom.SHORT_NAME as companyName,  bcom.company_ID AS companyId ,");
			stringBuffer.append(" wh.WAREHOUSE_ID AS warehouseId, wh.NAME as warehouseName, ass.NAME as assetTypeName, assetCategory.ITEM_NAME as assetCategory, sum(case repo.STATUS when '").append(IAtomsConstants.PARAM_ASSET_STATUS_IN_APPLY).append("' then 1 else 0 end) as inApply,")
				.append("sum(case repo.STATUS when '").append(IAtomsConstants.PARAM_ASSET_STATUS_ON_WAY).append("' then 1 else 0 end) as inTrans, sum(case repo.STATUS when '").append(IAtomsConstants.PARAM_ASSET_STATUS_REPERTORY)
				.append("' then 1 else 0 end) as inStorage, sum(case repo.STATUS when '").append(IAtomsConstants.PARAM_ASSET_STATUS_IN_USE).append("' then 1 else 0 end) as inUse, sum(case repo.STATUS when '");
			stringBuffer.append(IAtomsConstants.PARAM_ASSET_STATUS_BORROWING).append("' then 1 else 0 end) as inBorrow, sum(case repo.STATUS when '").append(IAtomsConstants.PARAM_ASSET_STATUS_PENDING_DISABLED)
				.append("' then 1 else 0 end) as toScrap, sum(case repo.STATUS when '").append(IAtomsConstants.PARAM_ACTION_REPAIRED).append("' then 1 else 0 end) as inRepair, sum(case repo.STATUS when '").append(IAtomsConstants.PARAM_ACTION_DISABLED)
				//Bug #2382 update by 2017/09/12
				.append("' then 1 else 0 end) as inScrap, sum(case repo.STATUS when '").append(IAtomsConstants.PARAM_ASSET_STATUS_DESTROY).append("' then 1 else 0 end) as inDestroy, sum(case repo.STATUS when '")
				.append(IAtomsConstants.PARAM_ASSET_STATUS_REPAIR).append("' then 1 else 0 end) as inRepaired,")
				//Task #3242 2018/03/06
				.append(" sum(case repo.STATUS when '").append(IAtomsConstants.PARAM_ASSET_STATUS_RETURNED).append("' then 1 else 0 end) as inReturned,")
				.append(" sum(case repo.STATUS when '").append(IAtomsConstants.PARAM_ASSET_STATUS_LOST).append("' then 1 else 0 end) as inLost,")
				.append(" count(bcom.SHORT_NAME) as total");
			sql.append(stringBuffer.toString());
			stringBuffer.delete(0, stringBuffer.length());
			stringBuffer.append("from ").append(schema).append(".").append(tableName).append(" repo LEFT JOIN ").append(schema).append(".DMM_ASSET_TYPE ass ON ass.ASSET_TYPE_ID = repo.ASSET_TYPE_ID LEFT JOIN ").append(schema);
			stringBuffer.append(".BIM_WAREHOUSE wh ON wh.WAREHOUSE_ID = repo.WAREHOUSE_ID LEFT JOIN ").append(schema).append(".BIM_COMPANY whCompany ON wh.COMPANY_ID = whCompany.COMPANY_ID LEFT JOIN ").append(schema)
				.append(".BIM_CONTRACT bc ON bc.CONTRACT_ID = repo.CONTRACT_ID LEFT JOIN ").append(schema).append(".BIM_COMPANY bcom ON bc.COMPANY_ID = bcom.COMPANY_ID LEFT JOIN ").append(schema)
			//	.append(".BASE_PARAMETER_ITEM_DEF assetCategory ON assetCategory.ITEM_VALUE = ass.ASSET_CATEGORY  AND assetCategory.APPROVED_FLAG = '").append(IAtomsConstants.YES).append("' AND assetCategory.BPTD_CODE = '").append(IAtomsConstants.ASSET_CATEGORY)
				// 處理approvedFlag
				.append(".BASE_PARAMETER_ITEM_DEF assetCategory ON assetCategory.ITEM_VALUE = ass.ASSET_CATEGORY  AND isnull(assetCategory.DELETED, 'N') = '").append(IAtomsConstants.NO).append("' AND assetCategory.BPTD_CODE = '").append(IAtomsConstants.ASSET_CATEGORY)
				.append("' WHERE 1=1 AND repo.STATUS IN ('").append(IAtomsConstants.PARAM_ASSET_STATUS_IN_APPLY).append("', '").append(IAtomsConstants.PARAM_ASSET_STATUS_ON_WAY).append("', '").append(IAtomsConstants.PARAM_ASSET_STATUS_REPERTORY)
				.append("', '").append(IAtomsConstants.PARAM_ASSET_STATUS_IN_USE).append("', '").append(IAtomsConstants.PARAM_ASSET_STATUS_BORROWING).append("', '").append(IAtomsConstants.PARAM_ASSET_STATUS_PENDING_DISABLED).append("', '").append(IAtomsConstants.PARAM_ACTION_REPAIRED)
				.append("', '").append(IAtomsConstants.PARAM_ACTION_DISABLED).append("', '").append(IAtomsConstants.PARAM_ASSET_STATUS_DESTROY).append("', '").append(IAtomsConstants.PARAM_ASSET_STATUS_REPAIR)
				.append("', '").append(IAtomsConstants.PARAM_ASSET_STATUS_RETURNED).append("', '").append(IAtomsConstants.PARAM_ASSET_STATUS_LOST).append("')");
			sql.append(stringBuffer.toString());
			//客戶
			if (StringUtils.hasText(companyId)) {
				sql.append(" AND bcom.COMPANY_ID = :companyId");
				sql.setParameter(RepoStatusReportDTO.ATTRIBUTE.COMPANY_ID.getValue(), companyId);
			}
			//維護模式
			if (StringUtils.hasText(maTypeCode)) {
				sql.append(" AND repo.MA_TYPE =:maTypeCode ");
				sql.setParameter(RepoStatusReportDTO.ATTRIBUTE.MA_TYPE_CODE.getValue(), maTypeCode);
			}
			//通訊模式多選
			if (StringUtils.hasText(commModeId)){
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append(" AND exists (select comm.COMM_MODE_ID from ").append(schema).append(".DMM_ASSET_SUPPORTED_COMM comm where comm.ASSET_TYPE_ID = repo.ASSET_TYPE_ID AND comm.COMM_MODE_ID in (")
					.append(commModeId).append("))");
				sql.append(stringBuffer.toString());
			}
			//設備名稱
			if (StringUtils.hasText(assetTypeId)) {
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append(" AND ass.ASSET_TYPE_ID in (").append(assetTypeId).append(")");
				sql.append(stringBuffer.toString());
			}
			//倉庫控管
			if (!CollectionUtils.isEmpty(warehouseList) && IAtomsConstants.YES.equals(dataAcl)) {
				sql.append(" AND repo.WAREHOUSE_ID in (:warehouseList)");
				sql.setParameter("warehouseList", warehouseList);
			}
			//查詢月份
			if (IAtomsConstants.IATOMS_TB_NAME_REPOSITORY_HIST_MONTHLY.equals(tableName)) {
				sql.append(" AND repo.MONTH_YEAR = :yyyyMM");
				sql.setParameter(RepoStatusReportDTO.ATTRIBUTE.YYYY_MM.getValue(), date);
			}
			//設備類別為“EDC”和“週邊設備”
			stringBuffer.delete(0, stringBuffer.length());
			stringBuffer.append("AND ass.ASSET_CATEGORY in ('").append(IAtomsConstants.ASSET_CATEGORY_RODUND_ASSET).append("', '").append(IAtomsConstants.ASSET_CATEGORY_EDC).append("')");
			sql.append(stringBuffer.toString());
			//分組查詢
			sql.append("GROUP BY bcom.Company_ID ,bcom.SHORT_NAME, wh.WAREHOUSE_ID, wh.NAME, ass.NAME, assetCategory.ITEM_NAME, repo.ASSET_TYPE_ID");
			//排序 -- 若有用戶選中的排序方式則使用選擇的排序方式拼接SQL，否則使用默認的排序方式
			if (StringUtils.hasText(order) && StringUtils.hasText(sort)) {
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append("ORDER BY ").append(sort).append(IAtomsConstants.MARK_SPACE).append(order);
				sql.append(stringBuffer.toString());
			} else {
				sql.append(" ORDER BY companyName, warehouseName ,warehouseId, assetCategory, assetTypeName , commModeName ");
			}
			//設定對應的DTO屬性值
			AliasBean alias = new AliasBean(RepoStatusReportDTO.class);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.COMPANY_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.COMPANY_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.WAREHOUSE_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.WAREHOUSE_ID.getValue(), StringType.INSTANCE);

			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.ASSET_TYPE_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.COMM_MODE_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.IN_STORAGE.getValue(), IntegerType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.IN_APPLY.getValue(), IntegerType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.IN_TRANS.getValue(), IntegerType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.IN_USE.getValue(), IntegerType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.IN_BORROW.getValue(), IntegerType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.IN_SCRAP.getValue(), IntegerType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.IN_REPAIR.getValue(), IntegerType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.TO_SCRAP.getValue(), IntegerType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.TOTAL.getValue(), IntegerType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.IN_DESTROY.getValue(), IntegerType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.IN_REPAIRED.getValue(), IntegerType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.IN_LOST.getValue(), IntegerType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.IN_RETURNED.getValue(), IntegerType.INSTANCE);
			//打印sql語句
			LOGGER.debug("listBy()", "sql:", sql.toString());
			//查詢結果
			results = this.getDaoSupport().findByNativeSql(sql, alias);
		}catch(Exception e){
			LOGGER.error("listBy()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		return results;
	}

	/**
	 * (non-Javadoc)0
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IRepoStatusReportDAO#count(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public Integer count(String tableName, String companyId, String maTypeCode, String assetTypeId,
			String commModeId, String date, List<String> warehouseList, String dataAcl) throws DataAccessException {
		LOGGER.debug("listRepositoryStatus", "parameters:tableName=", tableName);
		LOGGER.debug("listRepositoryStatus()", "parameters:companyId=", companyId);
		LOGGER.debug("listRepositoryStatus()", "parameters:maTypeCode=", maTypeCode);
		LOGGER.debug("listRepositoryStatus()", "parameters:assetTypeId=", assetTypeId);
		LOGGER.debug("listRepositoryStatus()", "parameters:commModeIdt=", commModeId);
		LOGGER.debug("listRepositoryStatus()", "parameters:date=", date);
		int result = 0;
		try{
			//創建sqlQueryBean
			SqlQueryBean sql = new SqlQueryBean();
			String schema = this.getMySchema();
			//拼接SQL
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("select count(*) from (SELECT distinct repo.SERIAL_NUMBER from ").append(schema).append(".").append(tableName).append(" repo LEFT JOIN ").append(schema)
				.append(".DMM_ASSET_TYPE ass ON ass.ASSET_TYPE_ID = repo.ASSET_TYPE_ID LEFT JOIN ").append(schema).append(".BIM_WAREHOUSE wh ON wh.WAREHOUSE_ID = repo.WAREHOUSE_ID LEFT JOIN ");
			stringBuffer.append(schema).append(".BIM_COMPANY whCompany ON wh.COMPANY_ID = whCompany.COMPANY_ID LEFT JOIN ").append(schema).append(".BIM_CONTRACT bc ON bc.CONTRACT_ID = repo.CONTRACT_ID LEFT JOIN ").append(schema)
				.append(".BIM_COMPANY bcom ON bc.COMPANY_ID = bcom.COMPANY_ID LEFT JOIN ").append(schema).append(".DMM_ASSET_SUPPORTED_COMM comm on comm.ASSET_TYPE_ID = repo.ASSET_TYPE_ID LEFT JOIN ")
			//	.append(schema).append(".BASE_PARAMETER_ITEM_DEF commName ON commName.ITEM_VALUE = comm.COMM_MODE_ID  AND commName.APPROVED_FLAG = '").append(IAtomsConstants.YES).append("' AND commName.BPTD_CODE = '")
				// 處理approvedFlag
				.append(schema).append(".BASE_PARAMETER_ITEM_DEF commName ON commName.ITEM_VALUE = comm.COMM_MODE_ID  AND isnull(commName.DELETED, 'N') = '").append(IAtomsConstants.NO).append("' AND commName.BPTD_CODE = '")
				.append(IAtomsConstants.COMM_MODE).append("' WHERE 1=1 ");
			sql.append(stringBuffer.toString());
			//客戶
			if (StringUtils.hasText(companyId)) {
				sql.append(" AND bcom.COMPANY_ID = :companyId");
				sql.setParameter(RepoStatusReportDTO.ATTRIBUTE.COMPANY_ID.getValue(), companyId);
			}
			//倉庫控管
			if (!CollectionUtils.isEmpty(warehouseList) && IAtomsConstants.YES.equals(dataAcl)) {
				sql.append(" AND repo.WAREHOUSE_ID in (:warehouseList)");
				sql.setParameter("warehouseList", warehouseList);
			}
			//維護模式
			if (StringUtils.hasText(maTypeCode)) {
				sql.append(" AND repo.MA_TYPE =:maTypeCode ");
				sql.setParameter(RepoStatusReportDTO.ATTRIBUTE.MA_TYPE_CODE.getValue(), maTypeCode);
			}
			//通訊模式多選
			if (StringUtils.hasText(commModeId)){
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append(" AND comm.COMM_MODE_ID in ( ").append(commModeId).append(")");
				sql.append(stringBuffer.toString());
			}
			//設備名稱
			if (StringUtils.hasText(assetTypeId)) {
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append(" AND ass.ASSET_TYPE_ID in ( ").append(assetTypeId).append(")");
				sql.append(stringBuffer.toString());
			}
			//查詢月份
			if (IAtomsConstants.IATOMS_TB_NAME_REPOSITORY_HIST_MONTHLY.equals(tableName)) {
				sql.append(" AND repo.MONTH_YEAR = :yyyyMM");
				sql.setParameter(RepoStatusReportDTO.ATTRIBUTE.YYYY_MM.getValue(), date);
			}
			//判斷刪除標誌
			/*sql.append(" AND isNull(wh.DELETED,'') =:deleted");
			sql.append(" AND isNull(repo.DELETED,'') =:deleted");
			sql.setParameter(RepoStatusReportDTO.ATTRIBUTE.DELETED.getValue(), IAtomsConstants.NO);*/
			//設備類別為“EDC”和“週邊設備”
			stringBuffer.delete(0, stringBuffer.length());
			stringBuffer.append(" AND ass.ASSET_CATEGORY in ('").append(IAtomsConstants.ASSET_CATEGORY_RODUND_ASSET).append("', '").append(IAtomsConstants.ASSET_CATEGORY_EDC).append("')");
			sql.append(stringBuffer.toString());
			sql.append(") repos ");
			//查詢結果集
			List<Integer> list = this.getDaoSupport().findByNativeSql(sql);
			//打印SQL
			LOGGER.debug("count()", "sql:", sql.toString());
			//若查詢結果集不為空則獲取該結果集第一個元素的值作為最終結果
			if(!CollectionUtils.isEmpty(list)){
				result = list.get(0).intValue();
			}
		}catch(Exception e){
			LOGGER.error("count()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		return result;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IRepoStatusReportDAO#listRepositoryStatus(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<RepoStatusReportDTO> listRepositoryStatus(String tableName, String companyId, String maTypeCode, String assetTypeId,
			String commModeId, String date, List<String> warehouseList, String dataAcl) throws DataAccessException {
		LOGGER.debug("listRepositoryStatus", "parameters:tableName=", tableName);
		LOGGER.debug("listRepositoryStatus()", "parameters:companyId=", companyId);
		LOGGER.debug("listRepositoryStatus()", "parameters:maTypeCode=", maTypeCode);
		LOGGER.debug("listRepositoryStatus()", "parameters:assetTypeId=", assetTypeId);
		LOGGER.debug("listRepositoryStatus()", "parameters:commModeIdt=", commModeId);

		List<RepoStatusReportDTO> results = null;
		try{
			String schema = this.getMySchema();
			//創建sqlStatement
			SqlStatement sqlStatement = new SqlStatement();
			//拼接sql的查詢結果語句
			sqlStatement.addSelectClause("DISTINCT status.ITEM_NAME", RepoStatusReportDTO.ATTRIBUTE.STATUS_NAME.getValue()); 
			sqlStatement.addSelectClause("company.SHORT_NAME", RepoStatusReportDTO.ATTRIBUTE.COMPANY_NAME.getValue()); 
			sqlStatement.addSelectClause("company.COMPANY_ID", RepoStatusReportDTO.ATTRIBUTE.COMPANY_ID.getValue()); 
			sqlStatement.addSelectClause("repo.SERIAL_NUMBER", RepoStatusReportDTO.ATTRIBUTE.SERIAL_NUMBER.getValue()); 
			sqlStatement.addSelectClause("repo.PROPERTY_ID", RepoStatusReportDTO.ATTRIBUTE.PROPERTY_ID.getValue()); 
			sqlStatement.addSelectClause("ass.NAME", RepoStatusReportDTO.ATTRIBUTE.ASSET_NAME.getValue()); 
			//所在位置
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append("case when repo.STATUS=:repertory or repo.STATUS=:repair or repo.STATUS=:pendingDisabled or repo.STATUS=:disabled or repo.STATUS=:destroy then compa.SHORT_NAME + '-' + wh.NAME when ");
			fromBuffer.append("repo.STATUS=:borrowing then us.CNAME ");
			fromBuffer.append("when repo.STATUS=:inApply then adm.CNAME when repo.STATUS=:inUse then merchant.NAME ");
			fromBuffer.append("when repo.STATUS=:maintenance then repairVendor.SHORT_NAME when repo.STATUS=:RETURNED then maintainUser.CNAME else compa.SHORT_NAME + '-' + wh.NAME end ");
			
			sqlStatement.addSelectClause(fromBuffer.toString(), RepoStatusReportDTO.ATTRIBUTE.ADDRESS.getValue()); 
			sqlStatement.addSelectClause(" wh.NAME", RepoStatusReportDTO.ATTRIBUTE.WAREHOUSE_NAME.getValue()); 
			sqlStatement.addSelectClause("contract.CONTRACT_CODE", RepoStatusReportDTO.ATTRIBUTE.CONTRACT_ID.getValue()); 
			//維護模式
			sqlStatement.addSelectClause("maType.ITEM_NAME", RepoStatusReportDTO.ATTRIBUTE.MA_TYPE_NAME.getValue()); 
			//型號
			sqlStatement.addSelectClause("repo.ASSET_MODEL", RepoStatusReportDTO.ATTRIBUTE.MODEL.getValue()); 
			sqlStatement.addSelectClause("repo.IS_ENABLED", RepoStatusReportDTO.ATTRIBUTE.IS_ENABLED.getValue()); 
			sqlStatement.addSelectClause("repo.ENABLE_DATE", RepoStatusReportDTO.ATTRIBUTE.ENABLE_DATE.getValue()); 
			/*//租賃已啟用
			sqlStatement.addSelectClause("repo.IS_SIM_ENABLE", RepoStatusReportDTO.ATTRIBUTE.IS_SIM_ENABLED.getValue()); */
			//租賃啟用日
			sqlStatement.addSelectClause("repo.SIM_ENABLE_DATE", RepoStatusReportDTO.ATTRIBUTE.SIM_ENABLE_DATE.getValue());
			//租賃編號
			sqlStatement.addSelectClause("repo.SIM_ENABLE_NO", RepoStatusReportDTO.ATTRIBUTE.SIM_ENABLE_NO.getValue());
			//領用人/借用人
			sqlStatement.addSelectClause("repo.CARRIER + '" + IAtomsConstants.MARK_BACKSLASH + "' + repo.BORROWER", RepoStatusReportDTO.ATTRIBUTE.CARRIER.getValue()); 
			sqlStatement.addSelectClause("repo.BORROWER", RepoStatusReportDTO.ATTRIBUTE.BORROWER.getValue()); 
			sqlStatement.addSelectClause("repo.BORROWER_START", RepoStatusReportDTO.ATTRIBUTE.BORROW_START.getValue()); 
			sqlStatement.addSelectClause("repo.BORROWER_END", RepoStatusReportDTO.ATTRIBUTE.BORROW_END.getValue()); 
			//歸還日期
			sqlStatement.addSelectClause("repo.BACK_DATE", RepoStatusReportDTO.ATTRIBUTE.BACK_DATE.getValue());
			//案件編號
			sqlStatement.addSelectClause("repo.CASE_ID", RepoStatusReportDTO.ATTRIBUTE.CASE_ID.getValue()); 
			//完修日期
			sqlStatement.addSelectClause("repo.CASE_COMPLETION_DATE", RepoStatusReportDTO.ATTRIBUTE.CASE_COMPLETION_DATE.getValue());
			//TID
			sqlStatement.addSelectClause("repo.TID", RepoStatusReportDTO.ATTRIBUTE.TID.getValue()); 
			//DTID
			sqlStatement.addSelectClause("repo.DTID ", RepoStatusReportDTO.ATTRIBUTE.DTID.getValue()); 
			//特店代號
			sqlStatement.addSelectClause("merchant.MERCHANT_CODE", RepoStatusReportDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			//特店名稱
			sqlStatement.addSelectClause("merchant.NAME", RepoStatusReportDTO.ATTRIBUTE.MERCHANT_NAME.getValue()); 
			//特店表頭
			sqlStatement.addSelectClause("header.headerName", RepoStatusReportDTO.ATTRIBUTE.MERCHANT_HEADER_NAME.getValue()); 
			//程式名稱
			sqlStatement.addSelectClause("app.NAME", RepoStatusReportDTO.ATTRIBUTE.APP_NAME.getValue()); 
			//程式版本
			sqlStatement.addSelectClause("app.VERSION", RepoStatusReportDTO.ATTRIBUTE.APP_VERSION.getValue()); 
			//銀聯
			sqlStatement.addSelectClause("repo.IS_CUP", RepoStatusReportDTO.ATTRIBUTE.IS_CUP.getValue());
			//入庫批號
			sqlStatement.addSelectClause("repo.ASSET_IN_ID", RepoStatusReportDTO.ATTRIBUTE.ASSET_IN_ID.getValue()); 
			//建檔人員
			sqlStatement.addSelectClause("repo.CREATE_USER_NAME", RepoStatusReportDTO.ATTRIBUTE.CREATE_USER.getValue());
			//入庫日期
			sqlStatement.addSelectClause("repo.ASSET_IN_TIME", RepoStatusReportDTO.ATTRIBUTE.GUARANTEE_DATE.getValue());
			//原廠保固日期
			sqlStatement.addSelectClause("repo.FACTORY_WARRANTY_DATE", RepoStatusReportDTO.ATTRIBUTE.FACTORY_WARRANTY.getValue());
			//客戶保固日期
			sqlStatement.addSelectClause("repo.CUSTOMER_WARRANTY_DATE", RepoStatusReportDTO.ATTRIBUTE.CUSTOMER_WARRANTY_DATE.getValue());
			//使用人
			sqlStatement.addSelectClause("userCompany.SHORT_NAME", RepoStatusReportDTO.ATTRIBUTE.ASSET_USER.getValue());
			//資產owner
			sqlStatement.addSelectClause("ownerCompany.SHORT_NAME", RepoStatusReportDTO.ATTRIBUTE.ASSET_OWNER.getValue());
			//維護廠商
			sqlStatement.addSelectClause("mtCompany.SHORT_NAME", RepoStatusReportDTO.ATTRIBUTE.MAINTAIN_COMPANY.getValue());
			//維修廠商
			sqlStatement.addSelectClause("hwCompany.SHORT_NAME", RepoStatusReportDTO.ATTRIBUTE.HARDWARE_COMPANY.getValue());
			//故障組件
			sqlStatement.addSelectClause("faultCom.ITEM_NAME", RepoStatusReportDTO.ATTRIBUTE.FAULT_COMPONENT.getValue());
			//故障現象
			sqlStatement.addSelectClause("faultDesc.ITEM_NAME", RepoStatusReportDTO.ATTRIBUTE.FAULT_DESCRIPTION.getValue());
			//說明排除方式
			sqlStatement.addSelectClause("repo.DESCRIPTION", RepoStatusReportDTO.ATTRIBUTE.DESCRIPTION.getValue());
			//執行作業
			sqlStatement.addSelectClause("action.ITEM_NAME", RepoStatusReportDTO.ATTRIBUTE.ACTION.getValue());
			//異動人員
			sqlStatement.addSelectClause("repo.UPDATE_USER_NAME", RepoStatusReportDTO.ATTRIBUTE.UPDATE_USER_NAME.getValue());
			//裝機區域
			sqlStatement.addSelectClause("baseparam.ITEM_NAME", RepoStatusReportDTO.ATTRIBUTE.MER_LOCATION.getValue()); 
			//裝機地址
			sqlStatement.addSelectClause("baseparam.ITEM_NAME + '-' + repo.INSTALLED_ADRESS", RepoStatusReportDTO.ATTRIBUTE.ORDER_NO.getValue()); 
			// 裝機類別
			sqlStatement.addSelectClause("installType.ITEM_NAME", RepoStatusReportDTO.ATTRIBUTE.TICKET_ID.getValue()); 
			//異動日期
			sqlStatement.addSelectClause("repo.UPDATE_DATE", RepoStatusReportDTO.ATTRIBUTE.UPDATE_DATE.getValue()); 
			//實際驗收
			sqlStatement.addSelectClause("repo.IS_CHECKED", RepoStatusReportDTO.ATTRIBUTE.CYBER_CHECKED.getValue());
			//驗收日期
			sqlStatement.addSelectClause("repo.CHECKED_DATE", RepoStatusReportDTO.ATTRIBUTE.CUSTOMER_CHECKED_DATE.getValue());
			//廠牌
			sqlStatement.addSelectClause("repo.BRAND", RepoStatusReportDTO.ATTRIBUTE.BRAND.getValue());
			//客戶實際驗收
			sqlStatement.addSelectClause("repo.IS_CUSTOMER_CHECKED", RepoStatusReportDTO.ATTRIBUTE.CUSTOMER_CHECKED.getValue());
			//客戶實際驗收日期
			sqlStatement.addSelectClause("repo.CYBER_APPROVED_DATE", RepoStatusReportDTO.ATTRIBUTE.CYBER_CHECKED_DATE.getValue());
			StringBuffer stringBuffer = new StringBuffer();
			//拼接查尋數據表的SQL部分
			stringBuffer.append(schema).append(".").append(tableName).append(" repo LEFT JOIN ").append(schema).append(".BASE_PARAMETER_ITEM_DEF status ON status.ITEM_VALUE = repo.STATUS AND status.BPTD_CODE = '")
				// 處理approvedFlag
			//	.append(IAtomsConstants.ASSET_STATUS).append("' AND status.APPROVED_FLAG = '").append(IAtomsConstants.YES).append("' LEFT JOIN ").append(schema).append(".BASE_PARAMETER_ITEM_DEF dtid ON dtid.ITEM_VALUE = repo.DTID AND dtid.BPTD_CODE = '")
				// 處理approvedFlag
				.append(IAtomsConstants.ASSET_STATUS).append("' AND isnull(status.DELETED, 'N') = '").append(IAtomsConstants.NO).append("' LEFT JOIN ").append(schema).append(".BASE_PARAMETER_ITEM_DEF dtid ON dtid.ITEM_VALUE = repo.DTID AND dtid.BPTD_CODE = '")
			//	.append(IAtomsConstants.DTID_TYPE).append("' AND dtid.APPROVED_FLAG = '").append(IAtomsConstants.YES).append("'  LEFT JOIN ").append(schema).append(".DMM_ASSET_TYPE ass ON ass.ASSET_TYPE_ID = repo.ASSET_TYPE_ID LEFT JOIN ")
				.append(IAtomsConstants.DTID_TYPE).append("' AND isnull(dtid.DELETED, 'N') = '").append(IAtomsConstants.NO).append("'  LEFT JOIN ").append(schema).append(".DMM_ASSET_TYPE ass ON ass.ASSET_TYPE_ID = repo.ASSET_TYPE_ID LEFT JOIN ")
				.append(schema).append(".BASE_PARAMETER_ITEM_DEF b on repo.STATUS = b.ITEM_VALUE LEFT JOIN ").append(schema).append(".BIM_WAREHOUSE wh ON wh.WAREHOUSE_ID = repo.WAREHOUSE_ID LEFT JOIN ").append(schema)
				.append(".BIM_COMPANY compa on wh.COMPANY_ID=compa.COMPANY_ID LEFT JOIN ").append(schema).append(" .ADM_USER us on us.USER_ID=repo.BORROWER LEFT JOIN ").append(schema)
				.append(" .ADM_USER adm on adm.USER_ID=repo.CARRIER LEFT JOIN ").append(schema)
				.append(" .BASE_PARAMETER_ITEM_DEF baseparam on repo.INSTALLED_ADRESS_LOCATION=baseparam.ITEM_VALUE and baseparam.BPTD_CODE =:headerArea LEFT JOIN ").append(schema)
				.append(" .BASE_PARAMETER_ITEM_DEF installType on repo.INSTALL_TYPE=installType.ITEM_VALUE and installType.BPTD_CODE =:INSTALL_TYPE LEFT JOIN ").append(schema)
			//	.append(".BASE_PARAMETER_ITEM_DEF location ON location.ITEM_VALUE = wh.LOCATION AND location.BPTD_CODE = '").append(IAtomsConstants.LOCATION).append("' AND location.APPROVED_FLAG = '").append(IAtomsConstants.YES).append("' LEFT JOIN ")
				// 處理approvedFlag
				.append(".BASE_PARAMETER_ITEM_DEF location ON location.ITEM_VALUE = wh.LOCATION AND location.BPTD_CODE = '").append(IAtomsConstants.LOCATION).append("' AND isnull(location.DELETED, 'N') = '").append(IAtomsConstants.NO).append("' LEFT JOIN ")
			//	.append(schema).append(".BASE_PARAMETER_ITEM_DEF maType ON maType.ITEM_VALUE = repo.MA_TYPE AND maType.BPTD_CODE = '").append(IAtomsConstants.PARAM_MA_TYPE).append("' AND maType.APPROVED_FLAG = '").append(IAtomsConstants.YES);
				// 處理approvedFlag
				.append(schema).append(".BASE_PARAMETER_ITEM_DEF maType ON maType.ITEM_VALUE = repo.MA_TYPE AND maType.BPTD_CODE = '").append(IAtomsConstants.PARAM_MA_TYPE).append("' AND isnull(maType.DELETED, 'N') = '").append(IAtomsConstants.NO);
			stringBuffer.append("' LEFT JOIN ").append(".BIM_COMPANY hwCompany ON repo.REPAIR_VENDOR = hwCompany.COMPANY_ID LEFT JOIN ").append(schema).append(".BIM_COMPANY mtCompany ON repo.MAINTAIN_COMPANY = mtCompany.COMPANY_ID LEFT JOIN ")
				.append(".BIM_COMPANY ownerCompany ON repo.ASSET_OWNER = ownerCompany.COMPANY_ID LEFT JOIN ").append(schema).append(".BIM_COMPANY userCompany ON repo.ASSET_USER = userCompany.COMPANY_ID LEFT JOIN ").append(schema)
				.append(".BIM_COMPANY repairVendor on repo.REPAIR_COMPANY=repairVendor.COMPANY_ID LEFT JOIN ").append(schema).append(" .ADM_USER  maintainUser on maintainUser.USER_ID=repo.MAINTAIN_USER LEFT JOIN ").append(schema)
				.append(".BIM_CONTRACT contract ON contract.CONTRACT_ID = repo.CONTRACT_ID LEFT JOIN ").append(schema).append(".BIM_COMPANY company ON company.COMPANY_ID = contract.COMPANY_ID LEFT JOIN ").append(schema);
			stringBuffer.append(".DMM_ASSET_SUPPORTED_COMM comm ON comm.ASSET_TYPE_ID = ass.ASSET_TYPE_ID LEFT JOIN ").append(schema).append(".PVM_APPLICATION app ON repo.APPLICATION_ID = app.APPLICATION_ID LEFT JOIN ").append(schema)
				.append(".BIM_MERCHANT merchant ON repo.MERCHANT_ID = merchant.MERCHANT_ID LEFT JOIN (select MERCHANT_ID, stuff((SELECT ',' + HEADER_NAME FROM ").append(schema).append(".BIM_MERCHANT_HEADER WHERE header.MERCHANT_ID = MERCHANT_ID FOR XML path('')) ,1 ,1 , '') AS headerName from ")
				.append(schema).append(".BIM_MERCHANT_HEADER header group by header.MERCHANT_ID) header on merchant.MERCHANT_ID = header.MERCHANT_ID LEFT JOIN ").append(schema).append(".BIM_MERCHANT_HEADER merchantHeader ON merchant.MERCHANT_ID = merchantHeader.MERCHANT_ID LEFT JOIN ").append(schema)
			//	.append(".BASE_PARAMETER_ITEM_DEF action ON repo.ACTION = action.ITEM_VALUE AND action.BPTD_CODE = '").append(IAtomsConstants.ACTION).append("' AND action.APPROVED_FLAG = '").append(IAtomsConstants.YES).append("' LEFT JOIN ").append(schema)
				// 處理approvedFlag
				.append(".BASE_PARAMETER_ITEM_DEF action ON repo.ACTION = action.ITEM_VALUE AND action.BPTD_CODE = '").append(IAtomsConstants.ACTION).append("' AND isnull(action.DELETED, 'N') = '").append(IAtomsConstants.NO).append("' LEFT JOIN ").append(schema)
			//	.append(".BASE_PARAMETER_ITEM_DEF faultDesc ON repo.FAULT_DESCRIPTION = faultDesc.ITEM_VALUE AND faultDesc.BPTD_CODE = '").append(IAtomsConstants.FAULT_DESCRIPTION).append("' AND faultDesc.APPROVED_FLAG = '").append(IAtomsConstants.YES).append("' LEFT JOIN ").append(schema)
				// 處理approvedFlag
				.append(".BASE_PARAMETER_ITEM_DEF faultDesc ON repo.FAULT_DESCRIPTION = faultDesc.ITEM_VALUE AND faultDesc.BPTD_CODE = '").append(IAtomsConstants.FAULT_DESCRIPTION).append("' AND isnull(faultDesc.DELETED, 'N') = '").append(IAtomsConstants.NO).append("' LEFT JOIN ").append(schema)
			//	.append(".BASE_PARAMETER_ITEM_DEF faultCom ON repo.FAULT_COMPONENT = faultCom.ITEM_VALUE AND faultCom.BPTD_CODE = '").append(IAtomsConstants.FAULT_COMPONENT).append("' AND faultCom.APPROVED_FLAG = '").append(IAtomsConstants.YES).append("'");
				// 處理approvedFlag
				.append(".BASE_PARAMETER_ITEM_DEF faultCom ON repo.FAULT_COMPONENT = faultCom.ITEM_VALUE AND faultCom.BPTD_CODE = '").append(IAtomsConstants.FAULT_COMPONENT).append("' AND isnull(faultCom.DELETED, 'N') = '").append(IAtomsConstants.NO).append("'");
			sqlStatement.addFromExpression(stringBuffer.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.append("WHERE 1=1  AND (ass.ASSET_CATEGORY =:Related_Products or ass.ASSET_CATEGORY =:edc )");
			sqlQueryBean.setParameter("Related_Products", IAtomsConstants.ASSET_CATEGORY_RODUND_ASSET);
			sqlQueryBean.setParameter("edc", IAtomsConstants.ASSET_CATEGORY_EDC);
			
			sqlQueryBean.setParameter("RETURNED", IAtomsConstants.PARAM_ASSET_STATUS_RETURNED);
			sqlQueryBean.setParameter("repertory", IAtomsConstants.PARAM_ASSET_STATUS_REPERTORY);
			sqlQueryBean.setParameter("repair", IAtomsConstants.PARAM_ASSET_STATUS_REPAIR);
			sqlQueryBean.setParameter("disabled", IAtomsConstants.PARAM_ASSET_STATUS_DISABLED);
			sqlQueryBean.setParameter("destroy", IAtomsConstants.PARAM_ASSET_STATUS_DESTROY);
			sqlQueryBean.setParameter("borrowing", IAtomsConstants.PARAM_ASSET_STATUS_BORROWING);
			sqlQueryBean.setParameter("pendingDisabled", IAtomsConstants.PARAM_ASSET_STATUS_PENDING_DISABLED);
			sqlQueryBean.setParameter("inApply", IAtomsConstants.PARAM_ASSET_STATUS_IN_APPLY);
			sqlQueryBean.setParameter("inUse", IAtomsConstants.PARAM_ASSET_STATUS_IN_USE);
			sqlQueryBean.setParameter("maintenance", IAtomsConstants.PARAM_ASSET_STATUS_MAINTENANCE);
			sqlQueryBean.setParameter("headerArea", IATOMS_PARAM_TYPE.LOCATION.getCode());
			sqlQueryBean.setParameter("INSTALL_TYPE", IATOMS_PARAM_TYPE.INSTALL_TYPE.getCode());
			
			//Bug #2382 update by 2017/09/12
			sqlQueryBean.append("and repo.STATUS in ('IN_APPLY', 'ON_WAY', 'REPERTORY', 'IN_USE', 'BORROWING', 'PENDING_DISABLED', 'MAINTENANCE', 'DISABLED', 'DESTROY','REPAIR','RETURNED','LOST') ");
			//廠商查詢條件
			if(StringUtils.hasText(companyId)){
				sqlQueryBean.append(" AND company.COMPANY_ID = :companyId");
				sqlQueryBean.setParameter(RepoStatusReportDTO.ATTRIBUTE.COMPANY_ID.getValue(), companyId);
			}
			//倉庫控管
			if (!CollectionUtils.isEmpty(warehouseList) && IAtomsConstants.YES.equals(dataAcl)) {
				sqlQueryBean.append(" AND repo.WAREHOUSE_ID in (:warehouseList)");
				sqlQueryBean.setParameter("warehouseList", warehouseList);
			}
			//維護模式
			if(StringUtils.hasText(maTypeCode)){
				sqlQueryBean.append(" AND repo.MA_TYPE =:maTypeCode ");
				sqlQueryBean.setParameter(RepoStatusReportDTO.ATTRIBUTE.MA_TYPE_CODE.getValue(), maTypeCode);
			}
			//通訊模式
			if(StringUtils.hasText(commModeId)){
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append(" AND comm.COMM_MODE_ID in (").append(commModeId).append(")");
				sqlQueryBean.append(stringBuffer.toString());
			}
			//設備名稱
			if(StringUtils.hasText(assetTypeId)){
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append(" AND ass.ASSET_TYPE_ID in (").append(assetTypeId).append(")");
				sqlQueryBean.append(stringBuffer.toString());
			}
			//查詢月份
			if(IAtomsConstants.IATOMS_TB_NAME_REPOSITORY_HIST_MONTHLY.equals(tableName)){
				sqlQueryBean.append(" AND repo.MONTH_YEAR = :yyyyMM");
				sqlQueryBean.setParameter(RepoStatusReportDTO.ATTRIBUTE.YYYY_MM.getValue(), date);
			}
			sqlQueryBean.append(" ORDER BY companyName, warehouseName, assetName, statusName ");
			//設定對應的DTO屬性值
			AliasBean alias = new AliasBean(RepoStatusReportDTO.class);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.COMPANY_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.COMPANY_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.MODEL.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.STATUS_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.TICKET_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.ORDER_NO.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.MER_LOCATION.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.PROPERTY_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.ASSET_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.ADDRESS.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.WAREHOUSE_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.CONTRACT_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.MA_TYPE_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.IS_ENABLED.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.ENABLE_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.CARRIER.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.BORROWER.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.BORROW_START.getValue(), TimestampType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.BORROW_END.getValue(), TimestampType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.BACK_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.TID.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			//alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.IS_SIM_ENABLED.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.SIM_ENABLE_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.SIM_ENABLE_NO.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.CASE_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.CASE_COMPLETION_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.MERCHANT_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.MERCHANT_HEADER_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.APP_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.APP_VERSION.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.IS_CUP.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.ASSET_IN_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.CREATE_USER.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.GUARANTEE_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.CYBER_CHECKED.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.CYBER_CHECKED_DATE.getValue(), DateType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.CUSTOMER_CHECKED.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.CUSTOMER_CHECKED_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.FACTORY_WARRANTY.getValue(), DateType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.CUSTOMER_WARRANTY_DATE.getValue(), DateType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.ASSET_USER.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.ASSET_OWNER.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.UPDATE_USER_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.UPDATE_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.FAULT_DESCRIPTION.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.FAULT_COMPONENT.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.DESCRIPTION.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.MAINTAIN_COMPANY.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.HARDWARE_COMPANY.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.BRAND.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.ACTION.getValue(), StringType.INSTANCE);
			//打印SQL
			LOGGER.debug("listRepositoryStatus()", "sql:", sqlQueryBean.toString());
			//查詢結果集
			results = this.getDaoSupport().findByNativeSql(sqlQueryBean, alias);
		}catch(Exception e){
			LOGGER.error("listRepositoryStatus()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		return results;
	}
	
	private SqlStatement append(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IRepoStatusReportDAO#companylistBy(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<RepoStatusReportDTO> companylistBy(String tableName, String maTypeCode, String assetTypeId, String commModeId,
			String date, String order, String sort, List<String> warehouseList, String dataAcl, Integer rows, Integer page) throws DataAccessException {
		List<RepoStatusReportDTO> repoStatusReportDTOList = null;
		try {
			String schema = this.getMySchema();
			//創建sqlStatement
			SqlStatement sqlStatement = new SqlStatement();
			//拼接SQL
			sqlStatement.addSelectClause("DISTINCT bcom.COMPANY_ID", RepoStatusReportDTO.ATTRIBUTE.COMPANY_ID.getValue());
			sqlStatement.addSelectClause(" bcom.SHORT_NAME", RepoStatusReportDTO.ATTRIBUTE.COMPANY_NAME.getValue());
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(schema).append(".").append(tableName).append(" repo LEFT JOIN ").append(schema).append(".DMM_ASSET_TYPE ass ON ass.ASSET_TYPE_ID = repo.ASSET_TYPE_ID LEFT JOIN ")
				.append(schema).append(".BIM_WAREHOUSE wh ON wh.WAREHOUSE_ID = repo.WAREHOUSE_ID LEFT JOIN ").append(schema).append(".BIM_COMPANY whCompany ON wh.COMPANY_ID = whCompany.COMPANY_ID LEFT JOIN ").append(schema)
				.append(".BIM_CONTRACT bc ON bc.CONTRACT_ID = repo.CONTRACT_ID LEFT JOIN ").append(schema).append(".BIM_COMPANY bcom ON bc.COMPANY_ID = bcom.COMPANY_ID LEFT JOIN ")
			//	.append(schema).append(".DMM_ASSET_SUPPORTED_COMM comm on comm.ASSET_TYPE_ID = repo.ASSET_TYPE_ID LEFT JOIN ").append(schema).append(".BASE_PARAMETER_ITEM_DEF commName ON commName.ITEM_VALUE = comm.COMM_MODE_ID  AND commName.APPROVED_FLAG = '")
				// 處理approvedFlag
				.append(schema).append(".DMM_ASSET_SUPPORTED_COMM comm on comm.ASSET_TYPE_ID = repo.ASSET_TYPE_ID LEFT JOIN ").append(schema).append(".BASE_PARAMETER_ITEM_DEF commName ON commName.ITEM_VALUE = comm.COMM_MODE_ID  AND isnull(commName.DELETED, 'N') = '")
			//	.append(IAtomsConstants.YES).append("' AND commName.BPTD_CODE = '").append(IAtomsConstants.COMM_MODE).append("'");
				.append(IAtomsConstants.NO).append("' AND commName.BPTD_CODE = '").append(IAtomsConstants.COMM_MODE).append("'");
			sqlStatement.setFromExpression(stringBuffer.toString());
			sqlStatement.setPageSize(rows);
			sqlStatement.setStartPage(page -1 );
			SqlQueryBean sql = sqlStatement.createSqlQueryBean();
			sql.append("WHERE 1=1");
			//倉庫控管
			if (!CollectionUtils.isEmpty(warehouseList) && IAtomsConstants.YES.equals(dataAcl)) {
				sql.append(" AND repo.WAREHOUSE_ID in (:warehouseList)");
				sql.setParameter("warehouseList", warehouseList);
			}
			//維護模式
			if (StringUtils.hasText(maTypeCode)) {
				sql.append(" AND repo.MA_TYPE =:maTypeCode ");
				sql.setParameter(RepoStatusReportDTO.ATTRIBUTE.MA_TYPE_CODE.getValue(), maTypeCode);
			}
			//通訊模式多選
			if (StringUtils.hasText(commModeId)){
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append(" AND comm.COMM_MODE_ID in (").append(commModeId).append(")");
				sql.append(stringBuffer.toString());
			}
			//設備名稱
			if (StringUtils.hasText(assetTypeId)) {
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append(" AND ass.ASSET_TYPE_ID in (").append(assetTypeId).append(")");
				sql.append(stringBuffer.toString());
			}
			//查詢月份
			if (IAtomsConstants.IATOMS_TB_NAME_REPOSITORY_HIST_MONTHLY.equals(tableName)) {
				sql.append(" AND repo.MONTH_YEAR = :yyyyMM");
				sql.setParameter(RepoStatusReportDTO.ATTRIBUTE.YYYY_MM.getValue(), date);
			}
			//設備類別為“EDC”和“週邊設備”
			stringBuffer.delete(0, stringBuffer.length());
			stringBuffer.append(" AND ass.ASSET_CATEGORY in ('").append(IAtomsConstants.ASSET_CATEGORY_RODUND_ASSET).append("','").append(IAtomsConstants.ASSET_CATEGORY_EDC).append("')");
			sql.append(stringBuffer.toString());
			//Bug #2382 update by 2017/09/12
			sql.append(" AND repo.STATUS IN( 'IN_APPLY' , 'ON_WAY' ,'REPERTORY' ,'IN_USE' , 'BORROWING' , 'PENDING_DISABLED' , 'MAINTENANCE' ,'DISABLED' ,'DESTROY','REPAIR','RETURNED','LOST')");
			//排序
			if (StringUtils.hasText(order) && StringUtils.hasText(sort)) {
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append("ORDER BY ").append(sort).append(IAtomsConstants.MARK_SPACE).append(order);
				sql.append(stringBuffer.toString());
			} else {
				sql.append(" ORDER BY companyName");
			}
			//打印SQL
			LOGGER.debug("companylistBy()", "sql:", sql.toString());
			AliasBean alias = sqlStatement.createAliasBean(RepoStatusReportDTO.class);
			//查詢結果
			repoStatusReportDTOList = this.getDaoSupport().findByNativeSql(sql, alias);
		} catch (Exception e) {
			LOGGER.error("companylistBy()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String [] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_DMM_REPOSITORY)},e);
		}
		return repoStatusReportDTOList;
	}
}
