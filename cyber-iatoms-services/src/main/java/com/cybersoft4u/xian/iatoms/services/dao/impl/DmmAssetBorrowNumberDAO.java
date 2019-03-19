package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import cafe.core.bean.CoreMessageCode;
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
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetBorrowNumberDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetBorrowNumberDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetBorrowNumber;
/**
 * Purpose: 設備借用數量DAO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2018年8月1日
 * @MaintenancePersonnel CarrieDuan
 */
public class DmmAssetBorrowNumberDAO extends GenericBaseDAO<DmmAssetBorrowNumber> implements IDmmAssetBorrowNumberDAO {

	/**
	 * 系统日志记录物件  
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.DAO, DmmAssetBorrowNumberDAO.class);
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetBorrowNumberDAO#listBy(java.lang.String)
	 */
	@Override
	public List<DmmAssetBorrowNumberDTO> listBy(String borrowCaseId) throws DataAccessException {
		LOGGER.debug(this.getClass().getName() + "listBy.borrowCaseId=" + borrowCaseId);
		List<DmmAssetBorrowNumberDTO> assetBorrowNumberDTOs = null;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("borrowNum.BORROW_NUMBER", DmmAssetBorrowNumberDTO.ATTRIBUTE.BORROW_NUMBER.getValue());
			sqlStatement.addSelectClause("assetType.NAME", DmmAssetBorrowNumberDTO.ATTRIBUTE.ASSET_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("itemType.ITEM_NAME", DmmAssetBorrowNumberDTO.ATTRIBUTE.ASSET_CATEGORY_NAME.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".DMM_ASSET_BORROW_NUMBER borrowNum ");
			fromBuffer.append("left join ").append(schema).append(".DMM_ASSET_TYPE assetType on assetType.ASSET_TYPE_ID = borrowNum.ASSET_TYPE_ID ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF itemType on itemType.ITEM_VALUE = assetType.ASSET_CATEGORY and itemType.BPTD_CODE = :assetCategory ");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(borrowCaseId)) {
				sqlStatement.addWhereClause("borrowNum.BORROW_CASE_ID = :borrowCaseId", borrowCaseId);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmAssetBorrowNumberDTO.class);
			sqlQueryBean.setParameter("assetCategory", IATOMS_PARAM_TYPE.ASSET_CATEGORY.getCode());
			assetBorrowNumberDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ":Failed-- listBy()---" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return assetBorrowNumberDTOs;
	}

}
