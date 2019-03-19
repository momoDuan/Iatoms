package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.springframework.util.StringUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Parameter;
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
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractAssetDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IContractAssetDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimContractAsset;

/**
 * Purpose: 合約設備DAO
 * @author CarrieDuan
 * @since  JDK 1.6
 * @date   2016年3月30日
 * @MaintenancePersonnel CarrieDuan
 */
public class ContractAssetDAO extends GenericBaseDAO<BimContractAsset> implements IContractAssetDAO {

	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(ContractAssetDAO.class);

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractAssetDAO#deleteByContractId(java.lang.String)
	 */
	public void deleteByContractId(String contractId) throws DataAccessException {
		try {
			LOGGER.debug(".deleteByContractId()", "parameters:contractId=", contractId);
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			StringBuffer buffer = new StringBuffer();
			buffer.append("delete from ").append(schema).append(".BIM_CONTRACT_ASSET ");
			sql.append(buffer.toString());
			if (StringUtils.hasText(contractId)) {
				sql.append(" where CONTRACT_ID = :contractId");
				sql.setParameter(BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue(), contractId);
				LOGGER.debug(".deleteByContractId()", "sql:", sql.toString());	
				super.getDaoSupport().updateByNativeSql(sql);
			}
		} catch (Exception e) {
			LOGGER.error(":deleteByContractId() is error.", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractAssetDAO#listContractAssetByContractId(java.lang.String)
	 */
	public List<BimContractAssetDTO> listContractAssetByContractId(String contractId) throws DataAccessException {
		List<BimContractAssetDTO> contractAssetDTOs = null;
		try {
			LOGGER.debug(".listContractAssetByContractId()", "parameters:contractId=", contractId);
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("bc.ASSET_TYPE_ID", BimContractAssetDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			sqlStatement.addSelectClause("ds.NAME", BimContractAssetDTO.ATTRIBUTE.ASSET_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("ds.ASSET_CATEGORY", BimContractAssetDTO.ATTRIBUTE.ASSET_CATEGORY.getValue());
			sqlStatement.addSelectClause("base.ITEM_NAME", BimContractAssetDTO.ATTRIBUTE.ASSET_CATEGORY_NAME.getValue());
			sqlStatement.addSelectClause("bc.AMOUNT", BimContractAssetDTO.ATTRIBUTE.AMOUNT.getValue());
			sqlStatement.addSelectClause("bc.SAFETY_STOCK", BimContractAssetDTO.ATTRIBUTE.SAFETY_STOCK.getValue());
			sqlStatement.addSelectClause("bc.PRICE", BimContractAssetDTO.ATTRIBUTE.PRICE.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".BIM_CONTRACT_ASSET bc");
			fromBuffer.append(" left join ").append(schema).append(".DMM_ASSET_TYPE  ds on bc.ASSET_TYPE_ID = ds.ASSET_TYPE_ID ");
			fromBuffer.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF base on base.BPTD_CODE = :assetCategory and ");
			fromBuffer.append("base.ITEM_VALUE = ds.ASSET_CATEGORY");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(contractId)) {
				sqlStatement.addWhereClause("CONTRACT_ID = :contractId", contractId);
			}
			sqlStatement.setOrderByExpression(BimContractAssetDTO.ATTRIBUTE.ASSET_CATEGORY.getValue().concat(IAtomsConstants.MARK_SEPARATOR)
					.concat(BimContractAssetDTO.ATTRIBUTE.ASSET_TYPE_NAME.getValue()));
			AliasBean aliasBean = sqlStatement.createAliasBean(BimContractAssetDTO.class);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter(BimContractAssetDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), IAtomsConstants.PARAM_BPTD_CODE_ASSET_CATEGORY);
			LOGGER.debug(".listContractAssetByContractId()", "sql:", sqlQueryBean.toString());	
			contractAssetDTOs = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(":listContractAssetByContractId() is error.", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return contractAssetDTOs;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractAssetDAO#listAsset()
	 */
	public List<Parameter> listAsset() throws DataAccessException {
		List<Parameter> result = null;
		try {
			String schema = this.getMySchema();
			//拿到未删除的设备列表
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("a.ASSET_TYPE_ID", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("a.NAME", Parameter.FIELD_NAME);
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".DMM_ASSET_TYPE");
			sqlStatement.addFromExpression(fromBuffer.toString());
			sqlStatement.addWhereClause("a.DELETED = :deleted", IAtomsConstants.NO);
			SqlQueryBean sql = sqlStatement.createSqlQueryBean();
			LOGGER.debug("SQL---------->", sql.toString());
			AliasBean aliasBean = new AliasBean(Parameter.class);
			result = this.getDaoSupport().findByNativeSql(sql, aliasBean);
		} catch (Exception e) {
			LOGGER.error(".listAsset is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ASSET_TYPE)}, e);
		}
		return result;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractAssetDAO#listAssetCategorysByContractId(java.lang.String, java.lang.String)
	 */
	public List<Parameter> listAssetCategorysByContractId(String contractId, String assetCategory) throws DataAccessException {
		List<Parameter> result = null;
		try {
			LOGGER.debug(".listAssetCategorysByContractId()", "parameters:contractId=", contractId);
			LOGGER.debug(".listAssetCategorysByContractId()", "parameters:contractId=", assetCategory);
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("contractAsset.ASSET_TYPE_ID", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("assetType.NAME", Parameter.FIELD_NAME);
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".BIM_CONTRACT_ASSET contractAsset");
			fromBuffer.append(" left join ").append(schema).append(".DMM_ASSET_TYPE assetType on assetType.ASSET_TYPE_ID = contractAsset.ASSET_TYPE_ID");
			fromBuffer.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF item on item.BPTD_CODE = :type and ");
			fromBuffer.append("item.ITEM_VALUE = assetType.ASSET_CATEGORY");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(contractId)) {
				sqlStatement.addWhereClause("contractAsset.CONTRACT_ID = :contractId", contractId);
			}
			if (StringUtils.hasText(assetCategory)) {
				sqlStatement.addWhereClause("assetType.ASSET_CATEGORY = :assetCategory", assetCategory);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("type", IATOMS_PARAM_TYPE.ASSET_CATEGORY.getCode());
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			LOGGER.debug(".listAssetCategorysByContractId()", "sql:", sqlQueryBean.toString());	
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(".listAssetCategorysByContractId is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return result;
	}
}
