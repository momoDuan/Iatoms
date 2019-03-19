package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.codehaus.groovy.tools.shell.commands.AliasCommand;
import org.springframework.util.CollectionUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Parameter;
import cafe.core.config.GenericConfigManager;
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
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetBorrowInfoDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetBorrowInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetBorrowInfo;
/**
 * Purpose: 設備借用主檔DAO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2018年8月1日
 * @MaintenancePersonnel CarrieDuan
 */
public class DmmAssetBorrowInfoDAO extends GenericBaseDAO<DmmAssetBorrowInfo> implements IDmmAssetBorrowInfoDAO {

	/**
	 * 系统日志记录物件  
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.DAO, DmmAssetBorrowInfoDAO.class);
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetBorrowInfoDAO#getBorrowIds(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	@Override
	public List<DmmAssetBorrowInfoDTO> getBorrowIds(String borrowCaseId, String borrowStartDate, String borrowEndDate, String borrowCaseCategory,
			String borrowUserId, String caseStatus, int page, int rows) throws DataAccessException {
		LOGGER.debug(this.getClass().getName() + "getBorrowIds.borrowCaseId=" + borrowCaseId);
		LOGGER.debug(this.getClass().getName() + "getBorrowIds.borrowStartDate=" + borrowStartDate);
		LOGGER.debug(this.getClass().getName() + "getBorrowIds.borrowEndDate=" + borrowEndDate);
		LOGGER.debug(this.getClass().getName() + "getBorrowIds.borrowCaseCategory=" + borrowCaseCategory);
		LOGGER.debug(this.getClass().getName() + "getBorrowIds.borrowUserId=" + borrowUserId);
		LOGGER.debug(this.getClass().getName() + "getBorrowIds.caseStatus=" + caseStatus);
		LOGGER.debug(this.getClass().getName() + "getBorrowIds.page=" + page);
		LOGGER.debug(this.getClass().getName() + "getBorrowIds.rows=" + rows);
		List<DmmAssetBorrowInfoDTO> borrowInfoDTOs = null;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("borrInfo.BORROW_CASE_ID", DmmAssetBorrowInfoDTO.ATTRIBUTE.BORROW_CASE_ID.getValue());
			sqlStatement.addFromExpression(schema + ".DMM_ASSET_BORROW_INFO borrInfo");
			if (StringUtils.hasText(borrowCaseId)) {
				sqlStatement.addWhereClause("borrInfo.BORROW_CASE_ID = :borrowCaseId", borrowCaseId);
			}
			if (StringUtils.hasText(borrowStartDate)) {
				sqlStatement.addWhereClause("borrInfo.BORROW_START_DATE >= :borrowStartDate", borrowStartDate);
			}
			if (StringUtils.hasText(borrowEndDate)) {
				sqlStatement.addWhereClause("borrInfo.BORROW_END_DATE <= :borrowEndDate", borrowEndDate);
			}
			if (StringUtils.hasText(borrowCaseCategory)) {
				sqlStatement.addWhereClause("borrInfo.BORROW_CATEGORY = :borrowCaseCategory", borrowCaseCategory);
			}
			if (StringUtils.hasText(borrowUserId)) {
				sqlStatement.addWhereClause("borrInfo.BORROW_USER = :borrowUserId", borrowUserId);
			}
			if (StringUtils.hasText(caseStatus)) {
				sqlStatement.addWhereClause("borrInfo.BORROW_STATUS = :caseStatus", caseStatus);
			}
			if (rows != -1 && page != -1) {
				sqlStatement.setPageSize(rows);
				sqlStatement.setStartPage(page - 1);
			}
			sqlStatement.setOrderByExpression("borrInfo.CREATED_DATE ");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmAssetBorrowInfoDTO.class);
			borrowInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ":Failed-- getBorrowIds()---" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return borrowInfoDTOs;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetBorrowInfoDAO#listBy(java.lang.String, java.lang.String, java.util.List, int, int)
	 */
	@Override
	public List<DmmAssetBorrowInfoDTO> listBy(String borrowUserId,
			String caseStatus, List<String> borrowCodeIds, int page, int rows) throws DataAccessException {
		LOGGER.debug(this.getClass().getName() + "listBy.borrowUserId=" + borrowUserId);
		LOGGER.debug(this.getClass().getName() + "listBy.caseStatus=" + caseStatus);
		LOGGER.debug(this.getClass().getName() + "listBy.borrowCodeIds=" + borrowCodeIds.toString());
		LOGGER.debug(this.getClass().getName() + "listBy.page=" + page);
		LOGGER.debug(this.getClass().getName() + "listBy.rows=" + rows);
		List<DmmAssetBorrowInfoDTO> borrowInfoDTOs = null;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("borrowInfo.BORROW_CASE_ID", DmmAssetBorrowInfoDTO.ATTRIBUTE.BORROW_CASE_ID.getValue());
			sqlStatement.addSelectClause("borrowInfo.BORROW_STATUS", DmmAssetBorrowInfoDTO.ATTRIBUTE.BORROW_STATUS.getValue());
			sqlStatement.addSelectClause("borrowInfo.BORROW_CATEGORY", DmmAssetBorrowInfoDTO.ATTRIBUTE.BORROW_CATEGORY.getValue());
			sqlStatement.addSelectClause("admUser.CNAME", DmmAssetBorrowInfoDTO.ATTRIBUTE.BORROW_USER.getValue());
			sqlStatement.addSelectClause("borrowInfo.BORROW_START_DATE", DmmAssetBorrowInfoDTO.ATTRIBUTE.BORROW_START_DATE.getValue());
			sqlStatement.addSelectClause("borrowInfo.BORROW_END_DATE", DmmAssetBorrowInfoDTO.ATTRIBUTE.BORROW_END_DATE.getValue());
			sqlStatement.addSelectClause("borrowInfo.DIRECTOR_CHECK_BY_DATE", DmmAssetBorrowInfoDTO.ATTRIBUTE.DIRECTOR_CHECK_BY_DATE.getValue());
			sqlStatement.addSelectClause("borrowInfo.AGENT_BY_DATE", DmmAssetBorrowInfoDTO.ATTRIBUTE.AGENT_BY_DATE.getValue());
			sqlStatement.addSelectClause("assetType.NAME", DmmAssetBorrowInfoDTO.ATTRIBUTE.ASSET_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("borrNumber.ASSET_TYPE_ID", DmmAssetBorrowInfoDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			sqlStatement.addSelectClause("itemType.ITEM_NAME", DmmAssetBorrowInfoDTO.ATTRIBUTE.ASSET_CATEGORY_NAME.getValue());
			sqlStatement.addSelectClause("borrNumber.BORROW_NUMBER", DmmAssetBorrowInfoDTO.ATTRIBUTE.BORROW_NUMBER.getValue());
			sqlStatement.addSelectClause("borrowInfo.DIRECTOR_BACK", DmmAssetBorrowInfoDTO.ATTRIBUTE.DIRECTOR_BACK.getValue());
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(".DMM_ASSET_BORROW_INFO borrowInfo ");
			buffer.append("left join ").append(schema).append(".ADM_USER admUser on admUser.USER_ID = borrowInfo.BORROW_USER ");
			buffer.append("left join ").append(schema).append(".DMM_ASSET_BORROW_NUMBER borrNumber on borrNumber.BORROW_CASE_ID = borrowInfo.BORROW_CASE_ID ");
			buffer.append("left join ").append(schema).append(".DMM_ASSET_TYPE assetType on assetType.ASSET_TYPE_ID = borrNumber.ASSET_TYPE_ID ");
			buffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF itemType on itemType.ITEM_VALUE = assetType.ASSET_CATEGORY and itemType.BPTD_CODE = :assetCategory ");
			sqlStatement.addFromExpression(buffer.toString());
			if (StringUtils.hasText(borrowUserId)) {
				sqlStatement.addWhereClause("borrowInfo.BORROW_USER = :borrowUserId", borrowUserId);
			}
			if (StringUtils.hasText(caseStatus)) {
				sqlStatement.addWhereClause("borrowInfo.BORROW_STATUS = :caseStatus", caseStatus);
			}
			if (!CollectionUtils.isEmpty(borrowCodeIds)) {
				sqlStatement.addWhereClause("borrowInfo.BORROW_CASE_ID in ( :borrowCodeIds )");
			}
			if (rows != -1 && page != -1) {
				sqlStatement.setPageSize(rows);
				sqlStatement.setStartPage(page - 1);
			}
			sqlStatement.setOrderByExpression("borrowInfo.CREATED_DATE, itemType.ITEM_NAME, assetType.NAME  ");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmAssetBorrowInfoDTO.class);
			sqlQueryBean.setParameter("borrowCodeIds", borrowCodeIds);
			sqlQueryBean.setParameter("assetCategory", IATOMS_PARAM_TYPE.ASSET_CATEGORY.getCode());
			borrowInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ":Failed-- listBy()---" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return borrowInfoDTOs;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetBorrowInfoDAO#getBorrowCaseIds(java.lang.String)
	 */
	@Override
	public List<Parameter> getBorrowCaseIds(String roles) throws DataAccessException {
		List<Parameter> parameters = null;
		LOGGER.debug(this.getClass().getName() + "getBorrowCaseIds.roles=" + roles);
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("borrowInfo.BORROW_CASE_ID", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("borrowInfo.BORROW_CASE_ID", Parameter.FIELD_NAME);
			sqlStatement.addFromExpression(schema + ".DMM_ASSET_BORROW_INFO borrowInfo");
			/*if (StringUtils.hasText(roles) && roles.equals(IAtomsConstants.PARAM_ONLY_STOREHOUSE_SUPERVISO)) {
				sqlStatement.addWhereClause("borrowInfo.DIRECTOR_CHECK_BY_DATE is null");
			}
			if (StringUtils.hasText(roles) && roles.equals(IAtomsConstants.PARAM_ONLY_STOREHOUSE_HANDLE)) {
				sqlStatement.addWhereClause("borrowInfo.DIRECTOR_CHECK_BY_DATE is not null");
				sqlStatement.addWhereClause("borrowInfo.AGENT_BY_DATE is null");
			}
			sqlStatement.addWhereClause("borrowInfo.BORROW_STATUS = :borrowStatus", IAtomsConstants.FIELD_ASSET_BORROW_STATUS_WAIT_PROCESS);*/
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			parameters = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ":Failed-- getBorrowCaseIds()---" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return parameters;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetBorrowInfoDAO#getBorrowInfoProcess(java.lang.String, int, int)
	 */
	@Override
	public List<DmmAssetBorrowInfoDTO> getBorrowInfoProcess(String borrowUserId, int page, int rows) throws DataAccessException {
		List<DmmAssetBorrowInfoDTO> borrowInfoDTOs = null;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("borList.ID", DmmAssetBorrowInfoDTO.ATTRIBUTE.BORROW_LIST_ID.getValue());
			sqlStatement.addSelectClause("admUser.CNAME", DmmAssetBorrowInfoDTO.ATTRIBUTE.BORROW_USER.getValue());
			sqlStatement.addSelectClause("itemDef.ITEM_NAME", DmmAssetBorrowInfoDTO.ATTRIBUTE.ASSET_CATEGORY_NAME.getValue());
			sqlStatement.addSelectClause("assetType.NAME", DmmAssetBorrowInfoDTO.ATTRIBUTE.ASSET_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("borList.SERIAL_NUMBER", DmmAssetBorrowInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("borInfo.BORROW_START_DATE", DmmAssetBorrowInfoDTO.ATTRIBUTE.BORROW_START_DATE.getValue());
			sqlStatement.addSelectClause("borList.BORROW_END_DATE", DmmAssetBorrowInfoDTO.ATTRIBUTE.BORROW_END_DATE.getValue());
			sqlStatement.addSelectClause("borInfo.BORROW_CATEGORY", DmmAssetBorrowInfoDTO.ATTRIBUTE.BORROW_CATEGORY.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".DMM_ASSET_BORROW_INFO borInfo ");
			fromBuffer.append("left join ").append(schema).append(".DMM_ASSET_BORROW_LIST borList on borList.BORROW_CASE_ID = borInfo.BORROW_CASE_ID ");
			fromBuffer.append("left join ").append(schema).append(".ADM_USER admUser on admUser.USER_ID = borInfo.BORROW_USER ");
			fromBuffer.append("left join ").append(schema).append(".DMM_ASSET_TYPE assetType on assetType.ASSET_TYPE_ID = borList.ASSET_TYPE_ID ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF itemDef on itemDef.BPTD_CODE = 'ASSET_CATEGORY' and itemDef.ITEM_VALUE = assetType.ASSET_CATEGORY ");
			fromBuffer.append("left join ").append(schema).append(".DMM_REPOSITORY repo on repo.SERIAL_NUMBER = borList.SERIAL_NUMBER");
			sqlStatement.addFromExpression(fromBuffer.toString());
			sqlStatement.addWhereClause("borInfo.BORROW_STATUS = :borrowStatus", IAtomsConstants.FIELD_ASSET_BORROW_STATUS_PROCESS);
			sqlStatement.addWhereClause("borInfo.BORROW_USER = :borrowUserId", borrowUserId);
			sqlStatement.addWhereClause("repo.STATUS = :assetStatus", IAtomsConstants.PARAM_ASSET_STATUS_BORROWING);
			sqlStatement.addWhereClause("isnull(borInfo.DIRECTOR_BACK, 'N') = :isBack", IAtomsConstants.NO);
			if (rows != -1 && page != -1) {
				sqlStatement.setPageSize(rows);
				sqlStatement.setStartPage(page - 1);
			}
			sqlStatement.setOrderByExpression("borInfo.CREATED_DATE ");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmAssetBorrowInfoDTO.class);
			borrowInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ":Failed-- getBorrowInfoProcess()---" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return borrowInfoDTOs;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetBorrowInfoDAO#getBorrowListId(java.lang.String)
	 */
	@Override
	public List<DmmAssetBorrowInfoDTO> getBorrowListId(List<String> borrowCodeIds) throws DataAccessException {
		List<DmmAssetBorrowInfoDTO> borrowInfoDTOs = null;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("borrowInfo.BORROW_CASE_ID", DmmAssetBorrowInfoDTO.ATTRIBUTE.BORROW_CASE_ID.getValue());
			sqlStatement.addSelectClause("borrowInfo.BORROW_STATUS", DmmAssetBorrowInfoDTO.ATTRIBUTE.BORROW_STATUS.getValue());
			sqlStatement.addSelectClause("borrowInfo.BORROW_CATEGORY", DmmAssetBorrowInfoDTO.ATTRIBUTE.BORROW_CATEGORY.getValue());
			sqlStatement.addSelectClause("admUser.CNAME", DmmAssetBorrowInfoDTO.ATTRIBUTE.BORROW_USER.getValue());
			sqlStatement.addSelectClause("borrowInfo.BORROW_START_DATE", DmmAssetBorrowInfoDTO.ATTRIBUTE.BORROW_START_DATE.getValue());
			sqlStatement.addSelectClause("borrowInfo.BORROW_END_DATE", DmmAssetBorrowInfoDTO.ATTRIBUTE.BORROW_END_DATE.getValue());
			sqlStatement.addSelectClause("assetType.NAME", DmmAssetBorrowInfoDTO.ATTRIBUTE.ASSET_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("borrNumber.ASSET_TYPE_ID", DmmAssetBorrowInfoDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			sqlStatement.addSelectClause("itemType.ITEM_NAME", DmmAssetBorrowInfoDTO.ATTRIBUTE.ASSET_CATEGORY_NAME.getValue());
			sqlStatement.addSelectClause("borrowList.SERIAL_NUMBER", DmmAssetBorrowInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("borrNumber.BORROW_NUMBER", DmmAssetBorrowInfoDTO.ATTRIBUTE.BORROW_NUMBER.getValue());
			sqlStatement.addSelectClause("borrowList.ID", DmmAssetBorrowInfoDTO.ATTRIBUTE.BORROW_LIST_ID.getValue());
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(".DMM_ASSET_BORROW_INFO borrowInfo ");
			buffer.append("left join ").append(schema).append(".ADM_USER admUser on admUser.USER_ID = borrowInfo.BORROW_USER ");
			buffer.append("left join ").append(schema).append(".DMM_ASSET_BORROW_NUMBER borrNumber on borrNumber.BORROW_CASE_ID = borrowInfo.BORROW_CASE_ID ");
			buffer.append("left join ").append(schema).append(".DMM_ASSET_TYPE assetType on assetType.ASSET_TYPE_ID = borrNumber.ASSET_TYPE_ID ");
			buffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF itemType on itemType.ITEM_VALUE = assetType.ASSET_CATEGORY and itemType.BPTD_CODE = :assetCategory ");
			buffer.append("left join ").append(schema).append(".DMM_ASSET_BORROW_LIST borrowList on borrowList.BORROW_CASE_ID = borrowInfo.BORROW_CASE_ID");
			sqlStatement.addFromExpression(buffer.toString());
			if (!CollectionUtils.isEmpty(borrowCodeIds)) {
				sqlStatement.addWhereClause("borrowInfo.BORROW_CASE_ID in ( :borrowCodeIds )");
			}
			sqlStatement.setOrderByExpression("borrowInfo.CREATED_DATE, itemType.ITEM_NAME, assetType.NAME  ");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmAssetBorrowInfoDTO.class);
			sqlQueryBean.setParameter("borrowCodeIds", borrowCodeIds);
			sqlQueryBean.setParameter("assetCategory", IATOMS_PARAM_TYPE.ASSET_CATEGORY.getCode());
			borrowInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ":Failed-- getBorrowListId()---" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return borrowInfoDTOs;
	}

}
