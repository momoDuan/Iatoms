package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import cafe.core.bean.CoreMessageCode;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentItemDTO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentItemDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmPaymentItem;
/**
 * Purpose: 求償項目資料表DAO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/2/14
 * @MaintenancePersonnel CarrieDuan
 */
public class SrmPaymentItemDAO extends GenericBaseDAO<SrmPaymentItem> implements ISrmPaymentItemDAO {

	/**
	 * 系統日志記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(SrmPaymentItemDAO.class);

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentItemDAO#listBy(java.lang.String)
	 */
	public List<SrmPaymentInfoDTO> listBy(String paymentId, Boolean isSend) throws DataAccessException {
		List<SrmPaymentInfoDTO> paymentInfoDTOs = null;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("info.PAYMENT_ID", SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_ID.getValue());
			sqlStatement.addSelectClause("info.CUSTOMER_ID", SrmPaymentInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue());
			sqlStatement.addSelectClause("company.SHORT_NAME", SrmPaymentInfoDTO.ATTRIBUTE.CUSTOMER_NAME.getValue());
			sqlStatement.addSelectClause("info.REQUIREMENT_NO", SrmPaymentInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue());
			sqlStatement.addSelectClause("info.DTID", SrmPaymentInfoDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("info.TID", SrmPaymentInfoDTO.ATTRIBUTE.TID.getValue());
			sqlStatement.addSelectClause("info.CASE_ID", SrmPaymentInfoDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("merchant.MERCHANT_CODE", SrmPaymentInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("merchant.NAME", SrmPaymentInfoDTO.ATTRIBUTE.NAME.getValue());
			sqlStatement.addSelectClause("info.REAL_FINISH_DATE", SrmPaymentInfoDTO.ATTRIBUTE.REAL_FINISH_DATE.getValue());
			sqlStatement.addSelectClause("info.CASE_CREATED_DATE", SrmPaymentInfoDTO.ATTRIBUTE.CASE_CREATED_DATE.getValue());
			sqlStatement.addSelectClause("item.SERIAL_NUMBER", SrmPaymentInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("contract.CONTRACT_CODE", SrmPaymentInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue());
			sqlStatement.addSelectClause("item.ITEM_ID", SrmPaymentInfoDTO.ATTRIBUTE.ITEM_ID.getValue());
			sqlStatement.addSelectClause("item.PAYMENT_ITEM", SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_ITEM.getValue());
			sqlStatement.addSelectClause("item.IS_PAY", SrmPaymentInfoDTO.ATTRIBUTE.IS_PAY.getValue());
			sqlStatement.addSelectClause("item.PAYMENT_REASON", SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_REASON.getValue());
			if (isSend) {
				sqlStatement.addSelectClause("(case when itemDef.ITEM_VALUE='SELF_INPUT' THEN itemDef.ITEM_NAME + '-' + item.REASON_DETAIL ELSE itemDef.ITEM_NAME END)", SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_REASON_NAME.getValue());
			} else {
				sqlStatement.addSelectClause("itemDef.ITEM_NAME", SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_REASON_NAME.getValue());
			}
			sqlStatement.addSelectClause("item.REASON_DETAIL", SrmPaymentInfoDTO.ATTRIBUTE.REASON_DETAIL.getValue());
			sqlStatement.addSelectClause("item.SUPPLIES_TYPE", SrmPaymentInfoDTO.ATTRIBUTE.SUPPLIES_TYPE.getValue());
			sqlStatement.addSelectClause("itemSupp.ITEM_NAME", SrmPaymentInfoDTO.ATTRIBUTE.SUPPLIES_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("item.SUPPLIES_AMOUNT", SrmPaymentInfoDTO.ATTRIBUTE.SUPPLIES_AMOUNT.getValue());
			sqlStatement.addSelectClause("(case when item.PAYMENT_ITEM='PAYMENT_ASSET' then asset.NAME else item.ITEM_NAME end)", SrmPaymentInfoDTO.ATTRIBUTE.ITEM_NAME.getValue());
			sqlStatement.addSelectClause("supplies.SUPPLIES_NAME", SrmPaymentInfoDTO.ATTRIBUTE.SUPPLIES_NAME.getValue());
			sqlStatement.addSelectClause("typeDef.ITEM_NAME", SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_ITEM_NAME.getValue());
			sqlStatement.addSelectClause("merHeader.AO_NAME", SrmPaymentInfoDTO.ATTRIBUTE.AO_NAME.getValue());
			sqlStatement.addSelectClause("CONVERT(varchar(100), item.UPDATED_DATE, 120)", SrmPaymentInfoDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".SRM_PAYMENT_INFO info ");
/*			fromBuffer.append("left join ").append(schema).append(".SRM_CASE_HANDLE_INFO handleInfo on handleInfo.CASE_ID = info.CASE_ID ");
*/			fromBuffer.append("left join ").append(schema).append(".SRM_PAYMENT_ITEM item on item.PAYMENT_ID = info.PAYMENT_ID ");
			fromBuffer.append("left join ").append(schema).append(".BIM_MERCHANT_HEADER merHeader on merHeader.MERCHANT_HEADER_ID = info.MERCHANT_HEADER_ID ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF typeDef on typeDef.BPTD_CODE = :paymentItem and typeDef.ITEM_VALUE = item.PAYMENT_ITEM ");
			fromBuffer.append("left join ").append(schema).append(".BIM_COMPANY company on company.COMPANY_ID = info.CUSTOMER_ID ");
			fromBuffer.append("left join ").append(schema).append(".BIM_MERCHANT merchant on merchant.MERCHANT_ID = info.MERCHANT_ID ");
			fromBuffer.append("left join ").append(schema).append(".DMM_SUPPLIES supplies on supplies.SUPPLIES_ID = item.ITEM_NAME ");
			fromBuffer.append("left join ").append(schema).append(".DMM_ASSET_TYPE asset on asset.ASSET_TYPE_ID = item.ITEM_NAME ");
			fromBuffer.append("left join ").append(schema).append(".BIM_CONTRACT contract on contract.CONTRACT_ID = item.CONTRACT_ID ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF itemSupp on itemSupp.BPTD_CODE = :suppliesType and itemSupp.ITEM_VALUE = item.SUPPLIES_TYPE ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF itemDef on itemDef.BPTD_CODE = :paymentReason and itemDef.ITEM_VALUE = item.PAYMENT_REASON ");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(paymentId)) {
				sqlStatement.addWhereClause("info.PAYMENT_ID = :paymentId", paymentId);
			}
			sqlStatement.setOrderByExpression("paymentId, item.PAYMENT_ITEM, asset.ASSET_CATEGORY,suppliesName, itemName");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmPaymentInfoDTO.class);
			sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.SUPPLIES_TYPE.getValue(), IATOMS_PARAM_TYPE.SUPPLIES_TYPE.getCode());
			sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_REASON.getValue(), IATOMS_PARAM_TYPE.PAYMENT_REASON.getCode());
			sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_ITEM.getValue(), IATOMS_PARAM_TYPE.PAYMENT_ITEM.getCode());
			LOGGER.debug("SQL---------->", sqlQueryBean.toString());
			paymentInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(".listBy() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return paymentInfoDTOs;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentItemDAO#getItemIds(java.lang.String, java.lang.String)
	 */
	public List<SrmPaymentItemDTO> getItemIds(String paymentId, String paymentItem) throws DataAccessException {
		List<SrmPaymentItemDTO> paymentItemDTOs = null;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("item.ITEM_ID", SrmPaymentItemDTO.ATTRIBUTE.ITEM_ID.getValue());
			sqlStatement.addFromExpression(schema + ".SRM_PAYMENT_ITEM item");
			if (StringUtils.hasText(paymentId)) {
				sqlStatement.addWhereClause("item.PAYMENT_ID = :paymentId", paymentId);
			}
			if (StringUtils.hasText(paymentItem)) {
				sqlStatement.addWhereClause("item.PAYMENT_ITEM = :paymentItem", paymentItem);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmPaymentItemDTO.class);
			LOGGER.debug("SQL---------->", sqlQueryBean.toString());
			paymentItemDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(".getItemIds() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return paymentItemDTOs;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentItemDAO#deletedByPaymentId(java.lang.String)
	 */
	public void deletedByPaymentId(String paymentId) throws DataAccessException {
		try {
			if (StringUtils.hasText(paymentId)) {
				SqlQueryBean sqlQueryBean = new SqlQueryBean("delete ");
				String schma = this.getMySchema();
				sqlQueryBean.append(schma).append(".SRM_PAYMENT_ITEM where PAYMENT_ID = :paymentId");
				sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_ID.getValue(), paymentId);
				LOGGER.debug(".deletedByPaymentId()", "sql:", sqlQueryBean.toString());
				this.getDaoSupport().updateByNativeSql(sqlQueryBean);
				this.getDaoSupport().flush();
			}
		} catch (Exception e) {
			LOGGER.error(".deleteByPaymentId() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentItemDAO#getPaymentItemDTOByItemId(java.lang.String, java.lang.String)
	 */
	public List<SrmPaymentItemDTO> getPaymentItemDTOByItemId(String itemId, String paymentId) throws DataAccessException {
		List<SrmPaymentItemDTO> srmPaymentItemDTOs = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("item.ITEM_ID", SrmPaymentItemDTO.ATTRIBUTE.ITEM_ID.getValue());
			sqlStatement.addSelectClause("item.PAYMENT_ID", SrmPaymentItemDTO.ATTRIBUTE.PAYMENT_ID.getValue());
			sqlStatement.addSelectClause("company.SHORT_NAME", SrmPaymentItemDTO.ATTRIBUTE.CUSTOMER_NAME.getValue());
			sqlStatement.addSelectClause("item.SERIAL_NUMBER", SrmPaymentItemDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("item.SUPPLIES_AMOUNT", SrmPaymentItemDTO.ATTRIBUTE.SUPPLIES_AMOUNT.getValue());
			sqlStatement.addSelectClause("item.IS_ASK_PAY", SrmPaymentItemDTO.ATTRIBUTE.IS_ASK_PAY.getValue());
			sqlStatement.addSelectClause("item.PAYMENT_TYPE", SrmPaymentItemDTO.ATTRIBUTE.PAYMENT_TYPE.getValue());
			sqlStatement.addSelectClause("typeDef.ITEM_NAME", SrmPaymentItemDTO.ATTRIBUTE.PAYMENT_ITEM_NAME.getValue());
			sqlStatement.addSelectClause("item.STATUS", SrmPaymentItemDTO.ATTRIBUTE.STATUS.getValue());
			sqlStatement.addSelectClause("contract.CONTRACT_CODE", SrmPaymentItemDTO.ATTRIBUTE.CONTRACT_CODE.getValue());
			sqlStatement.addSelectClause("CONVERT(varchar(100), item.UPDATED_DATE, 120)", SrmPaymentItemDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			sqlStatement.addSelectClause("(CASE WHEN item.PAYMENT_ITEM = 'PAYMENT_ASSET' THEN assetType.NAME  ELSE sipplies.SUPPLIES_NAME END )", SrmPaymentItemDTO.ATTRIBUTE.ITEM_NAME.getValue());
			sqlStatement.addSelectClause("item.PAYMENT_REASON", SrmPaymentItemDTO.ATTRIBUTE.PAYMENT_REASON.getValue());
			sqlStatement.addSelectClause("(case when itemDef.ITEM_VALUE='SELF_INPUT' THEN itemDef.ITEM_NAME + '-' + item.REASON_DETAIL ELSE itemDef.ITEM_NAME END)", SrmPaymentItemDTO.ATTRIBUTE.PAYMENT_REASON_NAME.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".SRM_PAYMENT_ITEM item ");
			fromBuffer.append("left join ").append(schema).append(".SRM_PAYMENT_INFO info on info.PAYMENT_ID = item.PAYMENT_ID ");
			fromBuffer.append("left join ").append(schema).append(".BIM_COMPANY company on company.COMPANY_ID = info.CUSTOMER_ID ");
			fromBuffer.append("left join ").append(schema).append(".BIM_CONTRACT contract on contract.CONTRACT_ID = item.CONTRACT_ID ");
			fromBuffer.append("left join ").append(schema).append(".DMM_ASSET_TYPE assetType ON item.ITEM_NAME = assetType.ASSET_TYPE_ID ");
			fromBuffer.append("left join ").append(schema).append(".DMM_SUPPLIES sipplies ON item.ITEM_NAME = sipplies.SUPPLIES_ID ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF typeDef on typeDef.BPTD_CODE = :paymentItem and typeDef.ITEM_VALUE = item.PAYMENT_ITEM ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF itemDef on itemDef.BPTD_CODE = :paymentReason and itemDef.ITEM_VALUE = item.PAYMENT_REASON ");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(itemId)) {
				sqlStatement.addWhereClause("item.ITEM_ID in ( " + itemId + " )");
			}
			if (StringUtils.hasText(paymentId)) {
				sqlStatement.addWhereClause("item.PAYMENT_ID = :paymentId", paymentId);
			}
			sqlStatement.setOrderByExpression("paymentId, item.PAYMENT_ITEM, assetType.ASSET_CATEGORY, itemName");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_ITEM.getValue(), IATOMS_PARAM_TYPE.PAYMENT_ITEM.getCode());
			sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_REASON.getValue(), IATOMS_PARAM_TYPE.PAYMENT_REASON.getCode());
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmPaymentItemDTO.class);
			LOGGER.debug("SQL---------->", sqlQueryBean.toString());
			srmPaymentItemDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(".getPaymentItemDTOByItemId() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return srmPaymentItemDTOs;
	}
}
