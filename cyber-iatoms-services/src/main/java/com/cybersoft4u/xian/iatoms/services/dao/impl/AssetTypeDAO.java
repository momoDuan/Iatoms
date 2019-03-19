package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.hibernate.type.BigDecimalType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.util.CollectionUtils;

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
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetSupportedCommDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetSupportedFunctionDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetTypeCompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetTypeDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetType;

/**
 * Purpose: 設備品項維護DAO
 * @author HermanWang
 * @since  JDK 1.7
 * @date   2016年7月8日
 * @MaintenancePersonnel HermanWang
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class AssetTypeDAO extends GenericBaseDAO<DmmAssetType> implements IAssetTypeDAO {
	
	/**
	 * 系統日志記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(AssetTypeDAO.class);
	
	/**
	 * 
	 * Constructor: 構造函數
	 */
	public AssetTypeDAO(){
		super();
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO#listBy(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<AssetTypeDTO> listBy(String assetCategoryCode, String assetTypeId, String sort, String order) throws DataAccessException {
		LOGGER.debug(this.getClass().getName(), ".list() --> assetCategoryCode: ", assetCategoryCode);
		LOGGER.debug(this.getClass().getName(), ".list() --> assetTypeId: ", assetTypeId);
		LOGGER.debug(this.getClass().getName(), ".list() --> sort: ", sort);
		LOGGER.debug(this.getClass().getName(), ".list() --> order: ", order);
		List<AssetTypeDTO> results = null;
		try{
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append("SELECT");
			sql.append(" asset.ASSET_TYPE_ID as assetTypeId, ");
			sql.append(" asset.NAME as name,");	
			sql.append(" asset.BRAND as brand, ");
			sql.append(" asset.MODEL as model, ");
			sql.append(" asset.ASSET_CATEGORY as assetCategory, ");
			sql.append(" base.ITEM_NAME as assetCategoryName, ");
			sql.append(" asset.UNIT as unit, ");
			sql.append(" asset.DELETED as deleted, ");
			sql.append(" STUFF((SELECT ','+ ltrim(company.SHORT_NAME) FROM dbo.DMM_ASSET_TYPE_COMPANY companytype,dbo.BIM_COMPANY company where companytype.ASSET_TYPE_ID = asset.ASSET_TYPE_ID and companytype.COMPANY_ID=company.COMPANY_ID FOR XML PATH('')), 1, 1, '') as companyName, ");
			sql.append(" STUFF((SELECT ','+ ltrim(company.COMPANY_ID) FROM dbo.DMM_ASSET_TYPE_COMPANY companytype,dbo.BIM_COMPANY company where companytype.ASSET_TYPE_ID = asset.ASSET_TYPE_ID and companytype.COMPANY_ID=company.COMPANY_ID FOR XML PATH('')), 1, 1, '') as companyId, ");
			sql.append(" STUFF((SELECT ','+ ltrim(bcomm.ITEM_VALUE) FROM dbo.BASE_PARAMETER_ITEM_DEF bcomm,dbo.DMM_ASSET_SUPPORTED_COMM comm where comm.ASSET_TYPE_ID = asset.ASSET_TYPE_ID and comm.COMM_MODE_ID= bcomm.ITEM_VALUE for XML PATH('')), 1, 1, '') as commModeId, ");
			sql.append(" STUFF((SELECT ','+ ltrim(bcomm.ITEM_NAME) FROM dbo.BASE_PARAMETER_ITEM_DEF bcomm,dbo.DMM_ASSET_SUPPORTED_COMM comm where comm.ASSET_TYPE_ID = asset.ASSET_TYPE_ID and comm.COMM_MODE_ID= bcomm.ITEM_VALUE for XML PATH('')), 1, 1, '') as commModeName, ");
			sql.append(" STUFF((SELECT ',' + RTRIM( t2.ITEM_NAME ) FROM ( SELECT supportFunction.ITEM_NAME FROM dbo.DMM_ASSET_SUPPORTED_FUNCTION assetSupportFunction LEFT JOIN dbo.BASE_PARAMETER_ITEM_DEF supportFunction ON supportFunction.ITEM_VALUE = assetSupportFunction.FUNCTION_ID ");
			sql.append(" AND supportFunction.BPTD_CODE = 'SUPPORTED_FUNCTION'  WHERE  assetSupportFunction.ASSET_TYPE_ID = asset.ASSET_TYPE_ID ) t2 FOR XML path('') ) , 1 , 1 ,'') AS functionName, ");
			sql.append(" STUFF((SELECT ',' + RTRIM( t2.ITEM_VALUE ) FROM ( SELECT supportFunction.ITEM_VALUE FROM dbo.DMM_ASSET_SUPPORTED_FUNCTION assetSupportFunction LEFT JOIN dbo.BASE_PARAMETER_ITEM_DEF supportFunction ON supportFunction.ITEM_VALUE = assetSupportFunction.FUNCTION_ID ");
			sql.append(" AND supportFunction.BPTD_CODE = 'SUPPORTED_FUNCTION'  WHERE  assetSupportFunction.ASSET_TYPE_ID = asset.ASSET_TYPE_ID ) t2 FOR XML path('') ) , 1 , 1 ,'') AS functionId, ");
			sql.append(" asset.SAFETY_STOCK as safetyStock, ");
			sql.append(" asset.REMARK as remark, ");
			sql.append(" asset.DELETED_DATE as deletedDate ");
			sql.append(" FROM ").append(schema).append(" .DMM_ASSET_TYPE asset  left join dbo.BASE_PARAMETER_ITEM_DEF base on asset.ASSET_CATEGORY = base.item_value and base.bptd_code = :assetCategory_type ");
			sql.append(" WHERE 1 = 1");
			//設備類別
			if(StringUtils.hasText(assetCategoryCode)){
				sql.append(" AND asset.ASSET_CATEGORY = :assetCategory");
				sql.setParameter(AssetTypeDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), assetCategoryCode);
				LOGGER.debug(this.getClass().getName(), ".list() --> assetCategoryCode: ", assetCategoryCode);
			}
			//設備代碼
			if(StringUtils.hasText(assetTypeId)){
				sql.append(" AND asset.ASSET_TYPE_ID = :assetTypeId");
				sql.setParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), assetTypeId);
				LOGGER.debug(this.getClass().getName(), ".list() --> assetTypeId: ", assetTypeId);
			}
			//排序
			if(StringUtils.hasText(sort) && StringUtils.hasText(order)){
				sql.append(" ORDER BY ").append(sort).append(IAtomsConstants.MARK_SPACE).append(order);
				LOGGER.debug(this.getClass().getName(), ".list() --> sort: ", sort);
				LOGGER.debug(this.getClass().getName(), ".list() --> order: ", order);
			} else {
				sql.append(" ORDER BY base.ITEM_ORDER ASC , asset.NAME  ASC ");
			}
			sql.setParameter("assetCategory_type", IATOMS_PARAM_TYPE.ASSET_CATEGORY.getCode());
			AliasBean alias = new AliasBean(AssetTypeDTO.class);
			alias.addScalar(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(AssetTypeDTO.ATTRIBUTE.NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(AssetTypeDTO.ATTRIBUTE.BRAND.getValue(), StringType.INSTANCE);
			alias.addScalar(AssetTypeDTO.ATTRIBUTE.MODEL.getValue(), StringType.INSTANCE);
			alias.addScalar(AssetTypeDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), StringType.INSTANCE);
			alias.addScalar(AssetTypeDTO.ATTRIBUTE.ASSET_CATEGORY_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(AssetTypeDTO.ATTRIBUTE.UNIT.getValue(), StringType.INSTANCE);
			alias.addScalar(AssetTypeDTO.ATTRIBUTE.DELETED.getValue(), StringType.INSTANCE);
			alias.addScalar(AssetTypeDTO.ATTRIBUTE.COMPANY_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(AssetTypeDTO.ATTRIBUTE.COMPANY_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(AssetTypeDTO.ATTRIBUTE.COMM_MODE_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(AssetTypeDTO.ATTRIBUTE.COMM_MODE_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(AssetTypeDTO.ATTRIBUTE.FUNCTION_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(AssetTypeDTO.ATTRIBUTE.FUNCTION_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(AssetTypeDTO.ATTRIBUTE.SAFETY_STOCK.getValue(), BigDecimalType.INSTANCE);
			alias.addScalar(AssetTypeDTO.ATTRIBUTE.REMARK.getValue(), StringType.INSTANCE);
			alias.addScalar(AssetTypeDTO.ATTRIBUTE.DELETED_DATE.getValue(), TimestampType.INSTANCE);
			results = this.getDaoSupport().findByNativeSql(sql, alias);
			LOGGER.debug(this.getClass().getName(), "listBy() --> sql: ", sql.toString()); 
		}catch(Exception e){
			LOGGER.error(this.getClass().getName(), ".listBy() is error: ", e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
		return results;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO#count(java.lang.String)
	 */
	public Integer count(String assetCategoryCode) throws DataAccessException {
		LOGGER.debug(".count() --> assetCategoryCode: ", assetCategoryCode);
		Integer result = null;
		try{
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append(" SELECT COUNT(ass.ASSET_TYPE_ID)");
			sql.append(" FROM ").append(schema).append(".DMM_ASSET_TYPE ass");
			sql.append(" WHERE 1=1");
			if(StringUtils.hasText(assetCategoryCode)){
				sql.append(" AND ass.ASSET_CATEGORY = :assetCategory");
				sql.setParameter(AssetTypeDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), assetCategoryCode);
				LOGGER.debug(".count() --> assetCategoryCode: ", assetCategoryCode);
			}
			List<Integer> list = this.getDaoSupport().findByNativeSql(sql);
			LOGGER.debug(".count() --> sql: ", sql.toString());
			if(!CollectionUtils.isEmpty(list)){
				result = list.get(0);
			}
		}catch(Exception e){
			LOGGER.error(".count() is error: ", e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
		return result;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO#getCompanyParameterList(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<Parameter> getCompanyParameterList(String companyId,
			String companyType, String companyCode, String shortName)
			throws DataAccessException {
		LOGGER.debug(".getCompanyParameterList() --> companyId: ", companyId);
		LOGGER.debug(".getCompanyParameterList() --> companyType: ", companyType);
		LOGGER.debug(".getCompanyParameterList() --> companyCode: ", companyCode);
		LOGGER.debug(".getCompanyParameterList() --> shortName: ", shortName);
		List<Parameter> results = null;
		try{
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append(" SELECT DISTINCT");	
			sql.append(" c.COMPANY_ID as value ,");
			sql.append(" c.SHORT_NAME as name ");
			sql.append(" FROM ").append(schema).append( ".BIM_COMPANY c");
			sql.append(" left join ").append(schema).append(" .BIM_COMPANY_TYPE ct on c.COMPANY_ID = ct.COMPANY_ID");
			sql.append(" where 1=1");
			//公司id
			if(StringUtils.hasText(companyId)){
				sql.append(" AND c.COMPANY_ID = :companyId");
				sql.setParameter(CompanyDTO.ATTRIBUTE.COMPANY_ID.getValue(), companyId);
				LOGGER.debug(".getCompanyParameterList() --> companyId: ", companyId);
			}
			//公司code
			if(StringUtils.hasText(companyCode)){
				sql.append(" AND c.COMPANY_CODE = :companyCode");
				sql.setParameter(CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue(), companyCode);
				LOGGER.debug(".getCompanyParameterList() --> companyCode: ", companyCode);
			}
			//公司名稱
			if(StringUtils.hasText(shortName)){
				sql.append(" AND c.SHORT_NAME like :shortName");
				sql.setParameter(CompanyDTO.ATTRIBUTE.SHORT_NAME.getValue(), shortName.concat(IAtomsConstants.MARK_PERCENT));
				LOGGER.debug(".getCompanyParameterList() --> shortName: ", shortName);
			}
			//公司類型
			if(StringUtils.hasText(companyType)){
				sql.append(" AND ct.COMPANY_TYPE = :companyType");
				sql.setParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), companyType);
				sql.append(" and c.deleted = :deleted");
				sql.setParameter(AssetTypeDTO.ATTRIBUTE.DELETED.getValue(), IAtomsConstants.NO);
				sql.append("order by c.SHORT_NAME asc");
				LOGGER.debug(".getCompanyParameterList() --> companyType: ", companyType);
			}
			AliasBean alias = new AliasBean(Parameter.class);
			alias.addScalar(Parameter.FIELD_NAME, StringType.INSTANCE);
			alias.addScalar(Parameter.FIELD_VALUE, StringType.INSTANCE);
			results = this.getDaoSupport().findByNativeSql(sql, alias);
			LOGGER.debug(".getCompanyParameterList() --> sql: ", sql.toString());
		}catch(Exception e){
			LOGGER.error(".getCompanyParameterList() is error: ", e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
		return results;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO#getAssetTypeCompanyDTOList(java.lang.String, java.lang.String)
	 */
	public List<AssetTypeCompanyDTO> getAssetTypeCompanyDTOList(
			String assetTypeId, String companyId) throws DataAccessException {
		LOGGER.debug(".getAssetTypeCompanyDTOList() --> assetTypeId: ", assetTypeId);
		LOGGER.debug(".getAssetTypeCompanyDTOList() --> companyId: ", companyId);
		List<AssetTypeCompanyDTO> results = null;
		try{
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append(" SELECT atc.ASSET_TYPE_ID as assetTypeId ,");
			sql.append(" atc.COMPANY_ID as companyId ");			
			sql.append(" FROM ").append(schema).append(".DMM_ASSET_TYPE_COMPANY atc" );
			sql.append(" INNER JOIN ").append(schema).append(".DMM_ASSET_TYPE ast  ON atc.ASSET_TYPE_ID = ast.ASSET_TYPE_ID");
			sql.append(" WHERE 1=1");
			//設備代碼
			if(StringUtils.hasText(assetTypeId)){
				sql.append(" AND atc.ASSET_TYPE_ID = :assetTypeId");
				sql.setParameter(AssetTypeCompanyDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), assetTypeId);
				LOGGER.debug(".getAssetTypeCompanyDTOList() --> assetTypeId: ", assetTypeId);
			}
			//公司id
			if(StringUtils.hasText(companyId)){
				sql.append(" AND atc.COMPANY_ID = :companyId");
				sql.setParameter(AssetTypeCompanyDTO.ATTRIBUTE.COMPANY_ID.getValue(), companyId);
				LOGGER.debug(".getAssetTypeCompanyDTOList() --> companyId: ", companyId);
			}
			AliasBean alias = new AliasBean(AssetTypeCompanyDTO.class);
			alias.addScalar(AssetTypeCompanyDTO.ATTRIBUTE.COMPANY_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(AssetTypeCompanyDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), StringType.INSTANCE);
			results = this.getDaoSupport().findByNativeSql(sql, alias);
			LOGGER.debug( ".getAssetTypeCompanyDTOList() --> sql: ", sql.toString());
		}catch(Exception e){
			LOGGER.error(".getAssetTypeCompanyDTOList() is error: ", "DAO Exception", e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
		return results;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO#getAssetSupportedCommDTOList(java.lang.String, java.lang.String)
	 */
	public List<AssetSupportedCommDTO> getAssetSupportedCommDTOList(
			String assetTypeId, String commModeId) throws DataAccessException {
		LOGGER.debug(".getAssetSupportedCommDTOList() --> assetTypeId: ", assetTypeId);
		LOGGER.debug(".getAssetSupportedCommDTOList() --> commModeId: ", commModeId);
		List<AssetSupportedCommDTO> results = null;
		try{
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append(" SELECT comm.ASSET_TYPE_ID as assetTypeId , ");
			sql.append(" comm.COMM_MODE_ID as commModeId");
			sql.append(" FROM ").append(schema).append(".DMM_ASSET_SUPPORTED_COMM comm" );
			sql.append(" INNER JOIN ").append(schema).append(".DMM_ASSET_TYPE ast ON comm.ASSET_TYPE_ID = ast.ASSET_TYPE_ID");
			sql.append(" WHERE 1=1");
			//設備代碼
			if(StringUtils.hasText(assetTypeId)){
				sql.append(" AND comm.ASSET_TYPE_ID = :assetTypeId");
				sql.setParameter(AssetSupportedCommDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), assetTypeId);
				LOGGER.debug(".getAssetSupportedCommDTOList() --> assetTypeId: ", assetTypeId);
			}
			//通訊模式
			if(StringUtils.hasText(commModeId)){
				sql.append(" AND comm.COMM_MODE_ID = :commModeId");
				sql.setParameter(AssetSupportedCommDTO.ATTRIBUTE.COMM_MODE_ID.getValue(), commModeId);
				LOGGER.debug(".getAssetSupportedCommDTOList() --> commModeId: ", commModeId);
			}
			AliasBean alias = new AliasBean(AssetSupportedCommDTO.class);
			alias.addScalar(AssetSupportedCommDTO.ATTRIBUTE.COMM_MODE_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(AssetSupportedCommDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), StringType.INSTANCE);
			results = this.getDaoSupport().findByNativeSql(sql, alias);
			LOGGER.debug(".getAssetSupportedCommDTOList() --> sql: ", sql.toString());
		}catch(Exception e){
			LOGGER.error(".getAssetSupportedCommDTOList() is error: ", "DAO Exception", e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
		return results;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO#getAssetSupportedFunctionDTOList(java.lang.String, java.lang.String)
	 */
	public List<AssetSupportedFunctionDTO> getAssetSupportedFunctionDTOList(
			String assetTypeId, String functionId) throws DataAccessException {
		LOGGER.debug(".getAssetSupportedFunctionDTOList() --> assetTypeId: ", assetTypeId);
		LOGGER.debug(".getAssetSupportedFunctionDTOList() --> functionId: ", functionId);
		List<AssetSupportedFunctionDTO> results = null;
		try{
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append(" SELECT asf.ASSET_TYPE_ID as assetTypeId, asf.FUNCTION_ID as functionId");
			sql.append(" FROM ").append(schema).append(".DMM_ASSET_SUPPORTED_FUNCTION asf");
			sql.append(" INNER JOIN ").append(schema).append(".DMM_ASSET_TYPE ast ON asf.ASSET_TYPE_ID = ast.ASSET_TYPE_ID");
			sql.append(" WHERE 1=1");
			//設備代碼
			if(StringUtils.hasText(assetTypeId)){
				sql.append(" AND asf.ASSET_TYPE_ID = :assetTypeId");
				sql.setParameter(AssetSupportedFunctionDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), assetTypeId);
				LOGGER.debug(".getAssetSupportedFunctionDTOList() --> assetTypeId: ", assetTypeId);
			}
			//支援功能
			if(StringUtils.hasText(functionId)){
				sql.append(" AND asf.FUNCTION_ID = :functionId");
				sql.setParameter(AssetSupportedFunctionDTO.ATTRIBUTE.FUNCTION_ID.getValue(), functionId);
				LOGGER.debug(".getAssetSupportedFunctionDTOList() --> functionId: ", functionId);
			}
			AliasBean alias = new AliasBean(AssetSupportedFunctionDTO.class);
			alias.addScalar(AssetSupportedFunctionDTO.ATTRIBUTE.FUNCTION_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(AssetSupportedFunctionDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), StringType.INSTANCE);
			results = this.getDaoSupport().findByNativeSql(sql, alias);
			LOGGER.debug(".getAssetSupportedFunctionDTOList() --> sql: ", sql.toString());
		}catch(Exception e){
			LOGGER.error(".getAssetSupportedFunctionDTOList() is error: ", "DAO Exception", e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
		return results;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO#getAssetNameList(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<Parameter> getAssetNameList(String assetTypeId,
			String name, String assetCategory, String contractId, String deleted) throws DataAccessException {
		LOGGER.debug(".getAssetNameList() --> assetTypeId: ", assetTypeId);
		LOGGER.debug(".getAssetNameList() --> name: ", name);
		LOGGER.debug(".getAssetNameList() --> assetCategory: ", assetCategory);
		LOGGER.debug(".getAssetNameList() --> contractId: ", contractId);
		LOGGER.debug(".getAssetNameList() --> deleted: ", deleted);
		long startQueryAssetTime = System.currentTimeMillis();
		List<Parameter> results = null;
		try{
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append("SELECT ass.ASSET_TYPE_ID as value,ass.NAME as name");
			sql.append("FROM ").append(schema).append(".DMM_ASSET_TYPE ass");
			sql.append(" inner join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF def ON ass.ASSET_CATEGORY = def.ITEM_VALUE ");
			if (StringUtils.hasText(contractId)) {
				sql.append(" left join ").append(schema).append(".BIM_CONTRACT_ASSET ca on ca.ASSET_TYPE_ID = ass.ASSET_TYPE_ID ");
				sql.append(" left join ").append(schema).append(".BIM_CONTRACT c on c.CONTRACT_ID = ca.CONTRACT_ID ");
			}
			sql.append(" WHERE 1=1");
			//設備代碼
			if(StringUtils.hasText(assetTypeId)){
				sql.append(" AND ass.ASSET_TYPE_ID = :assetTypeId");
				sql.setParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), assetTypeId);
				LOGGER.debug(".getAssetNameList() --> assetTypeId: ", assetTypeId);
			}
			//設備名稱
			if(StringUtils.hasText(name)){
				sql.append(" AND ass.NAME = :name");
				sql.setParameter(AssetTypeDTO.ATTRIBUTE.NAME.getValue(), name);
				LOGGER.debug(".getAssetNameList() --> name: ", name);
			}
			//設備類別
			if(StringUtils.hasText(assetCategory)){
				sql.append(" AND ass.ASSET_CATEGORY  in ( :assetCategory)");
				sql.setParameter(AssetTypeDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), StringUtils.toList(assetCategory, ","));
//				sql.setParameter(AssetTypeDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(),assetCategory);
				LOGGER.debug(".getAssetNameList() --> assetCategory: ", assetCategory);
			}
			//合約id
			if(StringUtils.hasText(contractId)) {
				sql.append(" and c.CONTRACT_ID = :contractId");
				sql.setParameter(BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue(), contractId);
				LOGGER.debug(".getAssetNameList() --> assetCategory: ", assetCategory);
			}
			//刪除標誌
			if(StringUtils.hasText(deleted)){
				sql.append(" AND ass.DELETED = :deleted");
				sql.setParameter(AssetTypeDTO.ATTRIBUTE.DELETED.getValue(), deleted);
				LOGGER.debug(".getAssetNameList() --> deleted: ", deleted);
			}
			sql.append(" ORDER By def.ITEM_ORDER,ass.NAME ");
			AliasBean alias = new AliasBean(Parameter.class);
			alias.addScalar(Parameter.FIELD_NAME, StringType.INSTANCE);
			alias.addScalar(Parameter.FIELD_VALUE, StringType.INSTANCE);
			results = this.getDaoSupport().findByNativeSql(sql, alias);
			LOGGER.debug(".getAssetNameList() --> sql: ", sql.toString());
		}catch(Exception e){
			LOGGER.error(".getAssetNameList() is error: ", "DAO Exception", e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
		long endQueryAssetTime = System.currentTimeMillis();
		LOGGER.debug("calculate time --> load", "DAO getAssetTypeList:" + (endQueryAssetTime - startQueryAssetTime));
		return results;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO#check(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isCheck(String assetTypeId, String name) throws DataAccessException {
		LOGGER.debug(".check()", "parameters:assetTypeId=", assetTypeId);
		LOGGER.debug(".check()", "parameters:name=", name);
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectColumn("type.ASSET_TYPE_ID", AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			sqlStatement.addSelectColumn("type.NAME", AssetTypeDTO.ATTRIBUTE.NAME.getValue());
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(" .DMM_ASSET_TYPE type  ");
			sqlStatement.addFromExpression(buffer.toString());
			//sqlStatement.addFromExpression(schema + ".DMM_ASSET_TYPE type  ");
			//判斷重複的設備代碼
			if (StringUtils.hasText(assetTypeId)) {
				sqlStatement.addWhereClause(" type.ASSET_TYPE_ID !=:assetTypeId", assetTypeId);
			}
			//判斷重複的設備名稱
			if (StringUtils.hasText(name)) {
				sqlStatement.addWhereClause(" type.NAME =:name", name);
			}
			//記錄sql語句
			LOGGER.debug(".check()", " Native SQL---------->", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(AssetTypeDTO.class);
			List<AssetTypeDTO> result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(result)) {
				if (result.get(0) != null) {
					return true;
				}
			}
		} catch (Exception e) {
			LOGGER.error(".check() is error", "DAO Exception", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_USER)}, e);
		}
		return false;
	}
	
	public boolean isCheck(String assetTypeId, String name, String assetCategory) throws DataAccessException {
		LOGGER.debug(".check()", "parameters:assetTypeId=", assetTypeId);
		LOGGER.debug(".check()", "parameters:name=", name);
		LOGGER.debug(".check()", "parameters:assetCategory=", assetCategory);

		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectColumn("type.ASSET_TYPE_ID", AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			sqlStatement.addSelectColumn("type.NAME", AssetTypeDTO.ATTRIBUTE.NAME.getValue());
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(" .DMM_ASSET_TYPE type  ");
			sqlStatement.addFromExpression(buffer.toString());
			//判斷重複的設備代碼
			if (StringUtils.hasText(assetTypeId)) {
				sqlStatement.addWhereClause(" type.ASSET_TYPE_ID !=:assetTypeId", assetTypeId);
			}
			//判斷重複的設備名稱
			if (StringUtils.hasText(name)) {
				sqlStatement.addWhereClause(" type.NAME =:name", name);
			}
			//判斷重複的設備名稱
			if (StringUtils.hasText(assetCategory)) {
				sqlStatement.addWhereClause(" type.ASSET_CATEGORY =:assetCategory", assetCategory);
			}
			//記錄sql語句
			LOGGER.debug(".check()", " Native SQL---------->", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(AssetTypeDTO.class);
			List<AssetTypeDTO> result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(result)) {
				if (result.get(0) != null) {
					return true;
				}
			}
		} catch (Exception e) {
			LOGGER.error(".check() is error", "DAO Exception", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_USER)}, e);
		}
		return false;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO#getAssetListForCase(java.lang.String)
	 */
	@Override
	public boolean isAssetList(String editAssetTypeId) {
		LOGGER.debug(".getAssetList() --> editAssetTypeId: ", editAssetTypeId);
		List<Integer> results = null;
		boolean hasAssetType = false;
		try{
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append("SELECT count(1) ");
			sql.append("FROM ").append(schema).append(".DMM_REPOSITORY r ");
			sql.append(" WHERE 1=1");
			sql.append(" and r.STATUS =:repertory");
			sql.setParameter("repertory", IAtomsConstants.PARAM_ASSET_STATUS_REPERTORY);
			if (StringUtils.hasText(editAssetTypeId)) {
				sql.append(" and r.ASSET_TYPE_ID =:editAssetTypeId");
				sql.setParameter("editAssetTypeId", editAssetTypeId);
			}
//			sql.append(" and isnull( r.DELETED , 'N' ) =:deleted ");
//			sql.setParameter("deleted", IAtomsConstants.NO);
			results =  this.getDaoSupport().findByNativeSql(sql);
			if (!CollectionUtils.isEmpty(results)) {
				if (results.get(0).intValue() > 0){
					hasAssetType = true;
				}
			}
			LOGGER.debug(".getAssetList() --> sql: " + (sql.toString()));
		}catch(Exception e){
			LOGGER.error(".getAssetList() is error: ", "DAO Exception", e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}

		return hasAssetType;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO#checkAssetName(java.lang.String)
	 */
	public boolean isCheckAssetName(String name) throws DataAccessException {
		LOGGER.debug(".checkAssetName()", "parameters:name=", name);
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(".DMM_ASSET_TYPE type ");
			sqlStatement.addFromExpression(buffer.toString());
			//判斷重複的設備名稱
			if (StringUtils.hasText(name)) {
				sqlStatement.addWhereClause(" type.NAME =:name",name);
			}
			sqlStatement.addWhereClause("type.DELETED = :deleted", IAtomsConstants.NO);
			//記錄sql語句
			LOGGER.debug(".checkAssetName()"," Native SQL---------->"+sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			List<AssetTypeDTO> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				if (result.get(0) != null) {
					return true;
				}
			}
		} catch (Exception e) {
			LOGGER.error(".checkAssetName() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,e);
		}
		return false;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO#getAssetInfo(java.lang.String, java.lang.String)
	 */
	public String getAssetInfo(String assetTypeId, String fieldName) throws DataAccessException {
		LOGGER.debug(".getAssetModelsByAssetTypeId()", "parameters:assetTypeId=" + assetTypeId);
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			if (AssetTypeDTO.ATTRIBUTE.MODEL.getValue().equals(fieldName)) {
				sqlStatement.addSelectClause("type.MODEL", AssetTypeDTO.ATTRIBUTE.MODEL.getValue());
			} else if (AssetTypeDTO.ATTRIBUTE.BRAND.getValue().equals(fieldName)) {
				sqlStatement.addSelectClause("type.BRAND", AssetTypeDTO.ATTRIBUTE.BRAND.getValue());
			} else {
				sqlStatement.addSelectClause("count(1)");
			}
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(".DMM_ASSET_TYPE type ");
			sqlStatement.addFromExpression(buffer.toString());
			if (StringUtils.hasText(assetTypeId)) {
				sqlStatement.addWhereClause("type.ASSET_TYPE_ID = :assetTypeId", assetTypeId);
			}
			sqlStatement.addWhereClause("type.DELETED = :deleted", IAtomsConstants.NO);
			//記錄sql語句
			LOGGER.debug(this.getClass().getName()+".getAssetModelsByAssetTypeId()"," Native SQL---------->"+sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if(AssetTypeDTO.ATTRIBUTE.MODEL.getValue().equals(fieldName) || AssetTypeDTO.ATTRIBUTE.BRAND.getValue().equals(fieldName)) {
				AliasBean aliasBean = sqlStatement.createAliasBean(AssetTypeDTO.class);
				List<AssetTypeDTO> result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
				//List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
				if (!CollectionUtils.isEmpty(result)) {
					if (AssetTypeDTO.ATTRIBUTE.MODEL.getValue().equals(fieldName)) {
						return result.get(0).getModel();
					} else if (AssetTypeDTO.ATTRIBUTE.BRAND.getValue().equals(fieldName)) {
						return result.get(0).getBrand();
					}
				}
			} else {
				List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
				return result.toString();
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".getAssetModelsByAssetTypeId() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,e);
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO#getAssetModelList(java.lang.String)
	 */
	public List<AssetTypeDTO> getAssetModelList(String assetTypeId) throws DataAccessException {
		LOGGER.debug(".getAssetModelList()", "parameters:assetTypeId=" + assetTypeId);
		List<AssetTypeDTO> result = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("type.MODEL", AssetTypeDTO.ATTRIBUTE.MODEL.getValue());
			
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(".DMM_ASSET_TYPE type ");
			sqlStatement.addFromExpression(buffer.toString());
			if (StringUtils.hasText(assetTypeId)) {
				String [] assetTypeIds = assetTypeId.split(",");
				StringBuffer serialBuffer = new StringBuffer();
				for (int i = 0; i < assetTypeIds.length - 1; i++) {
					if (StringUtils.hasText(assetTypeIds[i])) {
						serialBuffer.append("'" + assetTypeIds[i] + "',");
					}
				}
				serialBuffer.append("'" + assetTypeIds[assetTypeIds.length - 1] + "'");
				serialBuffer.append(" ) ");
				sqlStatement.addWhereClause("type.ASSET_TYPE_ID in(" + serialBuffer);
				//sqlStatement.addWhereClause("type.ASSET_TYPE_ID in (:assetTypeId)", StringUtils.toList(assetTypeId, IAtomsConstants.MARK_SEPARATOR));
			}
			sqlStatement.addWhereClause("type.DELETED = :deleted", IAtomsConstants.NO);
			//記錄sql語句
			LOGGER.debug(this.getClass().getName()+".getAssetModelList()", " Native SQL---------->" + sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(AssetTypeDTO.class);
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(result)) {
					return result;
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".getAssetModelList() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,e);
		}
		return null;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO#listBuiltInFeatureByAssetTypeId(java.lang.String)
	 */
	public List<Parameter> listBuiltInFeatureByAssetTypeId(String assetTypeId) throws DataAccessException {
		LOGGER.debug(this.getClass().getName() + ".listBuiltInFeatureByAssetTypeId()", "parameters:assetTypeId=" + assetTypeId);
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("supported.FUNCTION_ID", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("item.ITEM_NAME", Parameter.FIELD_NAME);
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(".DMM_ASSET_SUPPORTED_FUNCTION supported ");
			buffer.append("left join ").append(schema).append(".DMM_ASSET_TYPE type on supported.ASSET_TYPE_ID = type.ASSET_TYPE_ID");
			buffer.append(" inner join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF item on item.ITEM_VALUE = supported.FUNCTION_ID");
			sqlStatement.addFromExpression(buffer.toString());
			if (StringUtils.hasText(assetTypeId)) {
				sqlStatement.addWhereClause("type.ASSET_TYPE_ID = :assetTypeId", assetTypeId);
				SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
				AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
				LOGGER.debug(this.getClass().getName()+".listBuiltInFeatureByAssetTypeId()","SQL---------->"+sqlStatement.toString());
				return this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".listBuiltInFeatureByAssetTypeId() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,e);
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO#listConnectionTypeByAssetTypeId(java.lang.String)
	 */
	public List<Parameter> listConnectionTypeByAssetTypeId(String assetTypeId) throws DataAccessException {
		LOGGER.debug(this.getClass().getName() + ".listConnectionTypeByAssetTypeId()", "parameters:assetTypeId=" + assetTypeId);
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("comm.COMM_MODE_ID", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("item.ITEM_NAME", Parameter.FIELD_NAME);
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(".DMM_ASSET_SUPPORTED_COMM comm");
			buffer.append(" left join ").append(schema).append(".DMM_ASSET_TYPE type on comm.ASSET_TYPE_ID = type.ASSET_TYPE_ID");
			buffer.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF item on item.BPTD_CODE=:commModeId and item.ITEM_VALUE = comm.COMM_MODE_ID");
			sqlStatement.addFromExpression(buffer.toString());
			if (StringUtils.hasText(assetTypeId)) {
				sqlStatement.addWhereClause("type.ASSET_TYPE_ID = :assetTypeId", assetTypeId);
				SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
				sqlQueryBean.setParameter(AssetSupportedCommDTO.ATTRIBUTE.COMM_MODE_ID.getValue(), IATOMS_PARAM_TYPE.COMM_MODE.getCode());
				AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
				LOGGER.debug(this.getClass().getName()+".listConnectionTypeByAssetTypeId()","SQL---------->"+sqlStatement.toString());
				return this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".listConnectionTypeByAssetTypeId() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,e);
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO#listEdcAssetByCustomerId(java.lang.String)
	 */
	public List<Parameter> listAssetByCustomerId(String customerId, String assetCategory, Boolean ignoreDeleted) throws DataAccessException {
		LOGGER.debug(this.getClass().getName() + ".listEdcAssetByCustomerId()", "parameters:customerId=" + customerId);
		LOGGER.debug(this.getClass().getName() + ".listEdcAssetByCustomerId()", "parameters:assetCategory=" + assetCategory);
		LOGGER.debug(this.getClass().getName() + ".listEdcAssetByCustomerId()", "parameters:ignoreDeleted=" + ignoreDeleted);
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("DISTINCT asset.ASSET_TYPE_ID", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("asset.NAME", Parameter.FIELD_NAME);
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(".DMM_ASSET_TYPE asset ");
			buffer.append(" left join ").append(schema).append(".DMM_REPOSITORY repo on repo.ASSET_TYPE_ID = asset.ASSET_TYPE_ID");
			sqlStatement.addFromExpression(buffer.toString());
			if (StringUtils.hasText(customerId)) {
				sqlStatement.addWhereClause("repo.ASSET_USER = :customerId", customerId);
			}
			// Task #2496 EDC或周邊設備
			if(StringUtils.hasText(assetCategory)){
				sqlStatement.addWhereClause("asset.ASSET_CATEGORY = :assetCategory", assetCategory);
			}
		//	sqlStatement.addWhereClause("asset.ASSET_CATEGORY = :edc", IAtomsConstants.ASSET_CATEGORY_EDC);
			if(ignoreDeleted){
				sqlStatement.addWhereClause("asset.DELETED = :deleted", IAtomsConstants.NO);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			LOGGER.debug(this.getClass().getName()+".listEdcAssetByCustomerId()","SQL---------->"+sqlStatement.toString());
			List<Parameter> edcList = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(edcList)) {
				return edcList;
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".listEdcAssetByCustomerId() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,e);
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO#listAssetByName(java.lang.String)
	 */
	@Override
	public List<Parameter> listAssetByName(String assetName, boolean isIgnore, boolean isEdc) throws DataAccessException {
		LOGGER.debug(".listAssetByName()", "parameters:assetName=" + assetName);
		List<Parameter> result = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("asset.ASSET_TYPE_ID", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("asset.NAME", Parameter.FIELD_NAME);
			sqlStatement.addFromExpression(schema + ".DMM_ASSET_TYPE asset");
			if (StringUtils.hasText(assetName)) {
				sqlStatement.addWhereClause("asset.NAME like :assetName", assetName + IAtomsConstants.MARK_PERCENT);
				if(isIgnore){
					sqlStatement.addWhereClause("asset.NAME <> 'S800' AND asset.NAME <> 'S900'");
				}
			}
			// edc
			if(isEdc){
				sqlStatement.addWhereClause("asset.ASSET_CATEGORY = :edc", IAtomsConstants.ASSET_CATEGORY_EDC);
			} else {
				sqlStatement.addWhereClause("ISNULL(asset.DELETED, 'N') = 'N'");
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			LOGGER.debug(".listAssetByName()","SQL---------->"+sqlStatement.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".listAssetByName() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,e);
		}
		return result;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO#listAssetByNameAndType(java.lang.String)
	 */
	@Override
	public List<Parameter> listAssetByNameAndType(String assetName, boolean isEdc, boolean isRelatedProducts) throws DataAccessException {
		LOGGER.debug(".listAssetByNameAndType()", "parameters:assetName=" + assetName);
		List<Parameter> result = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("asset.ASSET_TYPE_ID", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("asset.NAME", Parameter.FIELD_NAME);
			sqlStatement.addFromExpression(schema + ".DMM_ASSET_TYPE asset");
			if (StringUtils.hasText(assetName)) {
				sqlStatement.addWhereClause("asset.NAME like :assetName", assetName + IAtomsConstants.MARK_PERCENT);
			}
			// edc
			if(isEdc){
				sqlStatement.addWhereClause("asset.ASSET_CATEGORY = :edc", IAtomsConstants.ASSET_CATEGORY_EDC);
			} 
			// Related_Products
			if (isRelatedProducts) {
				sqlStatement.addWhereClause("asset.ASSET_CATEGORY = :relatedProducts", IAtomsConstants.ASSET_CATEGORY_RODUND_ASSET);
			} 
			if ((!isEdc) || (!isRelatedProducts)) {
				sqlStatement.addWhereClause("ISNULL(asset.DELETED, 'N') = 'N'");
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			LOGGER.debug(".listAssetByNameAndType()","SQL---------->"+sqlStatement.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".listAssetByNameAndType() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,e);
		}
		return result;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO#isNoData()
	 */
	@Override
	public boolean isNoData() throws DataAccessException {
		boolean isNoData = false;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("COUNT(1)");
			sqlStatement.addFromExpression(schema + ".DMM_ASSET_TYPE");
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
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO#listPeripherals()
	 */
	@Override
	public List<Parameter> listPeripherals() throws DataAccessException {
		List<Parameter> result = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("asset.ASSET_TYPE_ID", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("asset.NAME", Parameter.FIELD_NAME);
			sqlStatement.addFromExpression(schema + ".DMM_ASSET_TYPE asset");
			sqlStatement.addWhereClause("asset.ASSET_CATEGORY = :assetCategory", IAtomsConstants.ASSET_CATEGORY_RODUND_ASSET);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			LOGGER.debug(".listPeripherals()","SQL---------->"+sqlStatement.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".listPeripherals() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,e);
		}
		return result;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO#deleteTransferData()
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
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY_DESC; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY_COMM; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".PVM_APPLICATION_ASSET_LINK; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".PVM_APPLICATION; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_ASSET_IN_LIST; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_ASSET_IN_INFO; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".BIM_CONTRACT_ASSET; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_ASSET_TYPE_COMPANY; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_ASSET_SUPPORTED_COMM; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_ASSET_SUPPORTED_FUNCTION; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_ASSET_TYPE; ");
			
			LOGGER.debug("deleteTransferData()", "sql:" + sqlQueryBean.toString());
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("deleteTransferData()", e.getMessage(), e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
	}
}

