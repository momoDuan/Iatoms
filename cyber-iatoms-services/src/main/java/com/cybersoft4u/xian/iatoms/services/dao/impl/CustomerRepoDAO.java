package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

import cafe.core.bean.Parameter;
import cafe.core.config.GenericConfigManager;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.RepoStatusReportDTO;
import com.cybersoft4u.xian.iatoms.services.dao.ICustomerRepoDAO;

/**
 * 
 * Purpose: 設備客戶總表DAO 實現類
 * @author HermanWang
 * @since  JDK 1.7
 * @date   2016年7月29日
 * @MaintenancePersonnel HermanWang
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class CustomerRepoDAO extends GenericBaseDAO implements
		ICustomerRepoDAO {

	/**
	 * 系統日志記錄
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.DAO, CustomerRepoDAO.class);

	/**
	 * Constructor: 無參構造
	 */
	public CustomerRepoDAO() {
		super();
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ICustomerRepoDAO#list(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<RepoStatusReportDTO> list(String tableName, Boolean isCustomerAttribute, String userId, String dataAcl, String yyyyMM, String customer, String maTypeCode, Integer pageSize, Integer page, String order, String sort)
			throws DataAccessException {
		LOGGER.debug(".list()", "tableName:", tableName);
		LOGGER.debug(".list()", "yyyyMM:", yyyyMM);
		LOGGER.debug(".list()", "customer:", customer);
		LOGGER.debug(".list()", "maType:", maTypeCode);
		LOGGER.debug(".list()", "row:", pageSize.toString());
		LOGGER.debug(".list()", "page:", page.toString());
		LOGGER.debug(".list()", "userId:", userId);
		List<RepoStatusReportDTO> results = null;
		try{
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			//查詢設備類別
			sql.append("SELECT ass.NAME as assetTypeName, ");
			sql.append(" repo.ASSET_TYPE_ID as assetTypeId, ");
			sql.append(" conCompany.SHORT_NAME as shortName, ");
			sql.append(" ass.ASSET_CATEGORY as assetCategory, ");
			sql.append(" sum(case repo.STATUS when 'IN_APPLY' then 1 else 0 end) as inApply, ");
			sql.append(" sum(case repo.STATUS when 'ON_WAY' then 1 else 0 end) as inTrans, ");
			sql.append(" sum(case repo.STATUS when 'REPERTORY' then 1 else 0 end) as inStorage, ");
			sql.append(" sum(case repo.STATUS when 'IN_USE' then 1 else 0 end) as inUse, ");
			sql.append(" sum(case repo.STATUS when 'BORROWING' then 1 else 0 end) as inBorrow, ");
			sql.append(" sum(case repo.STATUS when 'REPAIR' then 1 else 0 end) as inRepair, ");
			sql.append(" sum(case repo.STATUS when 'PENDING_DISABLED' then 1 else 0 end) as toScrap, ");
			sql.append(" sum(case repo.STATUS when 'DISABLED' then 1 else 0 end) as inScrap, ");
			sql.append(" sum(case repo.STATUS when 'DESTROY'  then 1 else 0 end) as inDestroy, ");
			sql.append(" sum(case repo.STATUS when 'BACKUP'  then 1 else 0 end) as inPrep, ");
			sql.append(" sum(case repo.STATUS when 'MAINTENANCE' then 1 else 0 end) as inMaintenance, ");
			//Task #3242
			sql.append(" sum(case repo.STATUS when 'RETURNED' then 1 else 0 end) as inReturned, ");
			sql.append(" sum(case repo.STATUS when 'LOST' then 1 else 0 end) as inLost ");
			sql.append("from ").append(schema).append(IAtomsConstants.MARK_NO).append(tableName).append(" repo");
			sql.append(" Left JOIN ").append(schema).append(IAtomsConstants.MARK_NO).append("DMM_ASSET_TYPE ass ");
			sql.append(" ON ass.ASSET_TYPE_ID = repo.ASSET_TYPE_ID ");
			sql.append( " Left JOIN ").append(schema).append(IAtomsConstants.MARK_NO).append("BASE_PARAMETER_ITEM_DEF status ON ");
			sql.append( " status.ITEM_VALUE = repo.STATUS AND status.BPTD_CODE =:asset_status ");
		//	sql.append( " AND status.APPROVED_FLAG = 'Y'");
			// 處理approvedFlag
			sql.append( " AND isnull(status.DELETED, 'N') = 'N'");
			sql.append("Left JOIN ").append(schema).append(IAtomsConstants.MARK_NO).append("BIM_CONTRACT contract ON contract.CONTRACT_ID = repo.CONTRACT_ID");	
			sql.append("Left JOIN ").append(schema).append(IAtomsConstants.MARK_NO).append("BIM_COMPANY conCompany ON contract.COMPANY_ID = conCompany.COMPANY_ID");
			//廠商角色
			if(!isCustomerAttribute) {
				//dataAcl ！ = Y 說明可以查看所有倉庫
				if(dataAcl.equals(IAtomsConstants.YES)){
					sql.append("Left JOIN ").append(schema).append(IAtomsConstants.MARK_NO).append("ADM_USER_WAREHOUSE uw on repo.WAREHOUSE_ID = uw.WAREHOUSE_ID");
				}
			}
			sql.append("WHERE 1 = 1");
			sql.append(" AND conCompany.DELETED =:deleted");
			sql.setParameter("deleted", IAtomsConstants.NO);
			//登入驗證方式（iatoms驗證）
			sql.append(" AND conCompany.AUTHENTICATION_TYPE =:authenticationType");
			sql.setParameter("authenticationType", IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE);
			//廠商角色
			if(!isCustomerAttribute) {
				//dataAcl ！ = Y 說明可以查看所有倉庫
				if(dataAcl.equals(IAtomsConstants.YES)){
					sql.append(" and uw.USER_ID = :userId");
					sql.setParameter("userId", userId);
				}
			}
			//查詢條件客戶
			if(StringUtils.hasText(customer)){
				sql.append(" AND conCompany.COMPANY_ID = :companyId");
				sql.setParameter(RepoStatusReportDTO.ATTRIBUTE.COMPANY_ID.getValue(), customer);
				LOGGER.debug(".list() --> customer: ", customer);
			}
			//查詢條件，設備類別
			if(StringUtils.hasText(maTypeCode)){
				sql.append(" AND repo.MA_TYPE = :maTypeCode");
				sql.setParameter(RepoStatusReportDTO.ATTRIBUTE.MA_TYPE_CODE.getValue(), maTypeCode);
				LOGGER.debug(".list() --> maType: ", maTypeCode);
			}
			if(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY_MONTHLY.equals(tableName)){
				sql.append(" AND repo.MONTH_YEAR = :yyyyMM");
				sql.setParameter(RepoStatusReportDTO.ATTRIBUTE.YYYY_MM.getValue(), yyyyMM);
				LOGGER.debug(".list() --> yyyyMM: ", yyyyMM);
			}
			//分組
			sql.append(" AND (ass.ASSET_CATEGORY =:edc OR ass.ASSET_CATEGORY =:Related_Products )");
			sql.append("GROUP BY repo.ASSET_TYPE_ID,ass.NAME,conCompany.SHORT_NAME,ass.ASSET_CATEGORY ");
			//排序
			if (StringUtils.hasText(sort) && StringUtils.hasText(order)) {
				sql.append(" order by ").append(sort).append(IAtomsConstants.MARK_SPACE).append(order);
			} else {
				sql.append(" order by  conCompany.SHORT_NAME ASC,ass.ASSET_CATEGORY asc, ass.NAME asc" );
			}
			//設備狀態
			sql.setParameter("asset_status", IATOMS_PARAM_TYPE.ASSET_STATUS.getCode());
			//EDC
			sql.setParameter("edc", IAtomsConstants.ASSET_CATEGORY_EDC);
			//周邊設備
			sql.setParameter("Related_Products", IAtomsConstants.ASSET_CATEGORY_RODUND_ASSET);
			AliasBean alias = new AliasBean(RepoStatusReportDTO.class);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.ASSET_TYPE_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.SHORT_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.IN_TRANS.getValue(), IntegerType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.IN_APPLY.getValue(), IntegerType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.IN_BORROW.getValue(), IntegerType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.IN_USE.getValue(), IntegerType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.IN_STORAGE.getValue(), IntegerType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.IN_REPAIR.getValue(), IntegerType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.TO_SCRAP.getValue(), IntegerType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.IN_SCRAP.getValue(), IntegerType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.IN_DESTROY.getValue(), IntegerType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.IN_PREP.getValue(), IntegerType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.IN_MAINTENANCE.getValue(), IntegerType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.IN_LOST.getValue(), IntegerType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.IN_RETURNED.getValue(), IntegerType.INSTANCE);
			results = this.getDaoSupport().findByNativeSql(sql, alias);
			LOGGER.debug( ".list() --> sql: ", sql.toString());
		}catch(Exception e){
			LOGGER.error( ".list() is error: ", e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
		return results;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ICustomerRepoDAO#getAssetNameList(java.lang.String)
	 */
	public List<Parameter> getAssetNameList(String tableName, String customer, String maTypeCode) throws DataAccessException {
		LOGGER.debug(".getAssetNameList()", "tableName:", tableName);
		LOGGER.debug(".getAssetNameList()", "yyyyMM:", customer);
		LOGGER.debug(".getAssetNameList()", "customer:", maTypeCode);
		List<Parameter> results = null;
		try{
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			String assetCategory = IAtomsConstants.ASSET_CATEGORY_RODUND_ASSET + IAtomsConstants.MARK_SEPARATOR + IAtomsConstants.ASSET_CATEGORY_EDC;
			sql.append(" SELECT DISTINCT repo.ASSET_TYPE_ID as value,ass.NAME as name");
			sql.append(" FROM ").append(schema).append(".").append(tableName).append("repo");
			sql.append(" INNER JOIN dbo.DMM_ASSET_TYPE ass ON ass.ASSET_TYPE_ID = repo.ASSET_TYPE_ID INNER JOIN dbo.BASE_PARAMETER_ITEM_DEF status ON");
		//	sql.append(" status.ITEM_VALUE = repo.STATUS AND status.BPTD_CODE = 'ASSET_STATUS' AND status.APPROVED_FLAG = 'Y'");
			// 處理approvedFlag
			sql.append(" status.ITEM_VALUE = repo.STATUS AND status.BPTD_CODE = 'ASSET_STATUS' AND isnull(status.DELETED, 'N') = 'N'");
			sql.append("LEFT JOIN dbo.BIM_CONTRACT contract ON contract.CONTRACT_ID = repo.CONTRACT_ID");	
			sql.append("LEFT JOIN dbo.BIM_COMPANY conCompany ON contract.COMPANY_ID = conCompany.COMPANY_ID");
			sql.append("WHERE 1 = 1");
			sql.append(" AND conCompany.DELETED = 'N'");
			if(StringUtils.hasText(customer)){
				sql.append(" AND conCompany.COMPANY_ID = :companyId");
				sql.setParameter(RepoStatusReportDTO.ATTRIBUTE.COMPANY_ID.getValue(), customer);
				LOGGER.debug( ".getAssetNameList() --> customer: ", customer);
			}
			if(StringUtils.hasText(maTypeCode)){
				sql.append(" AND repo.MA_TYPE = :maTypeCode");
				sql.setParameter(RepoStatusReportDTO.ATTRIBUTE.MA_TYPE_CODE.getValue(), maTypeCode);
				LOGGER.debug(".getAssetNameList() --> maType: ", maTypeCode);
			}
			//sql.append(" AND ass.ASSET_CATEGORY in ('").append(IAtomsConstants.ASSET_CATEGORY_RODUND_ASSET).append(IAtomsConstants.MARK_QUOTES).append(IAtomsConstants.MARK_SEPARATOR).append(IAtomsConstants.MARK_QUOTES).append(IAtomsConstants.ASSET_CATEGORY_EDC).append("')");
			sql.append(" AND ass.ASSET_CATEGORY in ( :assetCategory )");
			sql.setParameter(RepoStatusReportDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), StringUtils.toList(assetCategory, IAtomsConstants.MARK_SEPARATOR));
			AliasBean alias = new AliasBean(Parameter.class);
			alias.addScalar(Parameter.FIELD_VALUE, StringType.INSTANCE);
			alias.addScalar(Parameter.FIELD_NAME, StringType.INSTANCE);
			results = this.getDaoSupport().findByNativeSql(sql, alias);
		}catch(Exception e){
			LOGGER.error(".getAssetNameList() is error: ", e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
		return results;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ICustomerRepoDAO#getCount(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<RepoStatusReportDTO> getAssetList(String tableName, Boolean isCustomerAttribute, String userId, String dataAcl, String yyyyMM, String customer, String maTypeCode) throws DataAccessException {
		LOGGER.debug(".getAssetList()", "tableName:", tableName);
		LOGGER.debug(".getAssetList()", "userId:", userId);
		LOGGER.debug(".getAssetList()", "dataAcl:", dataAcl);
		LOGGER.debug(".getAssetList()", "userId:", yyyyMM);
		LOGGER.debug(".getAssetList()", "userId:", customer);
		LOGGER.debug(".getAssetList()", "userId:", maTypeCode);
		List<RepoStatusReportDTO> result = null;
		try{
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append(" SELECT ass.NAME as assetTypeName, ");
			sql.append(" repo.ASSET_TYPE_ID as assetTypeId, ");
			sql.append(" conCompany.SHORT_NAME as shortName ");
			sql.append("from ").append(schema).append(IAtomsConstants.MARK_NO).append(tableName).append(" repo");
			sql.append(" Left JOIN ").append(schema).append(IAtomsConstants.MARK_NO).append("DMM_ASSET_TYPE ass ");
			sql.append(" ON ass.ASSET_TYPE_ID = repo.ASSET_TYPE_ID ");
			sql.append( " Left JOIN ").append(schema).append(IAtomsConstants.MARK_NO).append("BASE_PARAMETER_ITEM_DEF status ON ");
			sql.append( " status.ITEM_VALUE = repo.STATUS AND status.BPTD_CODE =:asset_status ");
		//	sql.append( " AND status.APPROVED_FLAG = 'Y'");
			// 處理approvedFlag
			sql.append(" AND isnull(status.DELETED, 'N') = 'N'");
			sql.append("Left JOIN ").append(schema).append(IAtomsConstants.MARK_NO).append("BIM_CONTRACT contract ON contract.CONTRACT_ID = repo.CONTRACT_ID");	
			sql.append("Left JOIN ").append(schema).append(IAtomsConstants.MARK_NO).append("BIM_COMPANY conCompany ON contract.COMPANY_ID = conCompany.COMPANY_ID");
			//廠商角色
			if(!isCustomerAttribute) {
				//dataAcl ！ = Y 說明可以查看所有倉庫
				if(dataAcl.equals(IAtomsConstants.YES)){
					sql.append("Left JOIN ").append(schema).append(IAtomsConstants.MARK_NO).append("ADM_USER_WAREHOUSE uw on repo.WAREHOUSE_ID = uw.WAREHOUSE_ID");
				}
			}
			sql.append("WHERE 1 = 1");
			sql.append(" AND conCompany.DELETED =:deleted");
			sql.setParameter("deleted", IAtomsConstants.NO);
			//登入驗證方式（iatoms驗證）
			sql.append(" AND conCompany.AUTHENTICATION_TYPE =:authenticationType");
			sql.setParameter("authenticationType", IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE);
			//廠商角色
			if(!isCustomerAttribute) {
				//dataAcl ！ = Y 說明可以查看所有倉庫
				if(dataAcl.equals(IAtomsConstants.YES)){
					sql.append(" and uw.USER_ID = :userId");
					sql.setParameter("userId", userId);
				}
			}
			//查詢條件客戶
			if(StringUtils.hasText(customer)){
				sql.append(" AND conCompany.COMPANY_ID = :companyId");
				sql.setParameter(RepoStatusReportDTO.ATTRIBUTE.COMPANY_ID.getValue(), customer);
				LOGGER.debug(".getAssetList() --> customer: ", customer);
			}
			//查詢條件，設備類別
			if(StringUtils.hasText(maTypeCode)){
				sql.append(" AND repo.MA_TYPE = :maTypeCode");
				sql.setParameter(RepoStatusReportDTO.ATTRIBUTE.MA_TYPE_CODE.getValue(), maTypeCode);
				LOGGER.debug(".getAssetList() --> maType: ", maTypeCode);
			}
			if(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY_MONTHLY.equals(tableName)){
				sql.append(" AND repo.MONTH_YEAR = :yyyyMM");
				sql.setParameter(RepoStatusReportDTO.ATTRIBUTE.YYYY_MM.getValue(), yyyyMM);
				LOGGER.debug(".getAssetList() --> yyyyMM: ", yyyyMM);
			}
			//分組
			sql.append(" AND (ass.ASSET_CATEGORY =:edc OR ass.ASSET_CATEGORY =:Related_Products )");
			sql.append("GROUP BY repo.ASSET_TYPE_ID,ass.NAME,conCompany.SHORT_NAME,ass.ASSET_CATEGORY ");
			sql.append(" order by  conCompany.SHORT_NAME ASC,ass.ASSET_CATEGORY asc, ass.NAME asc" );
			//設備狀態
			sql.setParameter("asset_status", IATOMS_PARAM_TYPE.ASSET_STATUS.getCode());
			//EDC
			sql.setParameter("edc", IAtomsConstants.ASSET_CATEGORY_EDC);
			//周邊設備
			sql.setParameter("Related_Products", IAtomsConstants.ASSET_CATEGORY_RODUND_ASSET);
			AliasBean alias = new AliasBean(RepoStatusReportDTO.class);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.ASSET_TYPE_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(RepoStatusReportDTO.ATTRIBUTE.SHORT_NAME.getValue(), StringType.INSTANCE);
			result = this.getDaoSupport().findByNativeSql(sql, alias);
			LOGGER.debug(".getAssetList() --> sql: ", sql.toString());
		}catch(Exception e){
			LOGGER.error(".getAssetList() is error: ", e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
		return result;
	}
}
