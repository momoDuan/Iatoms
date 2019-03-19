package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.type.BigDecimalType;
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
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.PaymentFormDTO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmPaymentInfo;
/**
 * Purpose: 求償資料檔DAO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/2/14
 * @MaintenancePersonnel CarrieDuan
 */
public class SrmPaymentInfoDAO extends GenericBaseDAO<SrmPaymentInfo> implements ISrmPaymentInfoDAO {

	/**
	 * 日誌記錄物件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(SrmPaymentInfoDAO.class);

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentInfoDAO#listBy(java.lang.String, java.lang.String, java.lang.String, java.sql.Timestamp, java.sql.Timestamp, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.sql.Timestamp, java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	public List<SrmPaymentInfoDTO> listBy(String customerId, List<String> paymentIds, Timestamp createCaseStart, Timestamp createCaseEnd, String merchantCode,
			String dtid, String tid, String status, String payType, Timestamp lostDayStart, Timestamp lostDayEnd, String serialNumber, String order,
			String sort, int page, int rows, Boolean isMicro) throws DataAccessException {
		List<SrmPaymentInfoDTO> paymentInfoDTOs = null;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("item.ITEM_ID", SrmPaymentInfoDTO.ATTRIBUTE.ITEM_ID.getValue());
			sqlStatement.addSelectClause("info.CUSTOMER_ID", SrmPaymentInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue());
			sqlStatement.addSelectClause("c.SHORT_NAME", SrmPaymentInfoDTO.ATTRIBUTE.CUSTOMER_NAME.getValue());
			sqlStatement.addSelectClause("info.PAYMENT_ID", SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_ID.getValue());
			sqlStatement.addSelectClause("item.PAYMENT_ITEM", SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_ITEM.getValue());
			sqlStatement.addSelectClause("typeDef.ITEM_NAME", SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_ITEM_NAME.getValue());
			sqlStatement.addSelectClause("(case when item.PAYMENT_ITEM='PAYMENT_ASSET' then type.NAME else s.SUPPLIES_NAME end)", SrmPaymentInfoDTO.ATTRIBUTE.ITEM_NAME.getValue());
			sqlStatement.addSelectClause("item.PAYMENT_REASON", SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_REASON.getValue());
			sqlStatement.addSelectClause("item.SERIAL_NUMBER", SrmPaymentInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("con.CONTRACT_CODE", SrmPaymentInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue());
			sqlStatement.addSelectClause("item.SUPPLIES_AMOUNT", SrmPaymentInfoDTO.ATTRIBUTE.SUPPLIES_AMOUNT.getValue());
			sqlStatement.addSelectClause("item.REASON_DETAIL", SrmPaymentInfoDTO.ATTRIBUTE.REASON_DETAIL.getValue());
			sqlStatement.addSelectClause("(case when reason.ITEM_VALUE='SELF_INPUT' THEN reason.ITEM_NAME + '-' + item.REASON_DETAIL ELSE reason.ITEM_NAME END)", 
					SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_REASON_NAME.getValue());
			//sqlStatement.addSelectClause("reason.ITEM_NAME", SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_REASON_NAME.getValue());
			sqlStatement.addSelectClause("item.STATUS", SrmPaymentInfoDTO.ATTRIBUTE.STATUS.getValue());
			sqlStatement.addSelectClause("info.DTID", SrmPaymentInfoDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("info.TID", SrmPaymentInfoDTO.ATTRIBUTE.TID.getValue());
			sqlStatement.addSelectClause("info.REQUIREMENT_NO", SrmPaymentInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue());
			sqlStatement.addSelectClause("info.CASE_ID", SrmPaymentInfoDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("mer.MERCHANT_CODE", SrmPaymentInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("mer.NAME", SrmPaymentInfoDTO.ATTRIBUTE.NAME.getValue());
			sqlStatement.addSelectClause("info.REAL_FINISH_DATE", SrmPaymentInfoDTO.ATTRIBUTE.REAL_FINISH_DATE.getValue());
			sqlStatement.addSelectClause("item.CHECK_RESULT", SrmPaymentInfoDTO.ATTRIBUTE.CHECK_RESULT.getValue());
			sqlStatement.addSelectClause("item.CHECK_USER", SrmPaymentInfoDTO.ATTRIBUTE.CHECK_USER.getValue());
			sqlStatement.addSelectClause("item.FEE", SrmPaymentInfoDTO.ATTRIBUTE.FEE.getValue());
			sqlStatement.addSelectClause("item.FEE_DESC", SrmPaymentInfoDTO.ATTRIBUTE.FEE_DESC.getValue());
			sqlStatement.addSelectClause("item.IS_ASK_PAY", SrmPaymentInfoDTO.ATTRIBUTE.IS_ASK_PAY.getValue());
			sqlStatement.addSelectClause("payType.ITEM_NAME", SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_TYPE.getValue());
			sqlStatement.addSelectClause("item.PAYMENT_TYPE_DESC", SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_TYPE_DESC.getValue());
			sqlStatement.addSelectClause("item.ASK_PAY_DATE", SrmPaymentInfoDTO.ATTRIBUTE.ASK_PAY_DATE.getValue());
			sqlStatement.addSelectClause("item.ASK_PAY_DESC", SrmPaymentInfoDTO.ATTRIBUTE.ASK_PAY_DESC.getValue());
			sqlStatement.addSelectClause("item.CANCEL_DATE", SrmPaymentInfoDTO.ATTRIBUTE.CANCEL_DATE.getValue());
			sqlStatement.addSelectClause("item.REPAYMENT_DATE", SrmPaymentInfoDTO.ATTRIBUTE.REPAYMENT_DATE.getValue());
			sqlStatement.addSelectClause("item.CANCEL_DESC", SrmPaymentInfoDTO.ATTRIBUTE.CANCEL_DESC.getValue());
			sqlStatement.addSelectClause("item.UPDATED_BY_NAME", SrmPaymentInfoDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("item.IS_PAY", SrmPaymentInfoDTO.ATTRIBUTE.IS_PAY.getValue());
			sqlStatement.addSelectClause("CONVERT(varchar(100), item.UPDATED_DATE, 120)", SrmPaymentInfoDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			sqlStatement.addSelectClause("info.REQUIREMENT_NO", SrmPaymentInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue());
			sqlStatement.addSelectClause("info.CASE_CREATED_DATE", SrmPaymentInfoDTO.ATTRIBUTE.CASE_CREATED_DATE.getValue());
			sqlStatement.addSelectClause("caseCategory.ITEM_NAME", SrmPaymentInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue());
			sqlStatement.addSelectClause("edcType.NAME", SrmPaymentInfoDTO.ATTRIBUTE.EDC_TYPE.getValue());
			sqlStatement.addSelectClause("merHeader.AO_NAME", SrmPaymentInfoDTO.ATTRIBUTE.AO_NAME.getValue());
			sqlStatement.addSelectClause("CONVERT(varchar(100), item.LOCKED_DATE, 120)", SrmPaymentInfoDTO.ATTRIBUTE.LOCKED_DATE.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".SRM_PAYMENT_INFO info ");
			fromBuffer.append("left join ").append(schema).append(".SRM_PAYMENT_ITEM item on info.PAYMENT_ID = item.PAYMENT_ID ");
			fromBuffer.append("left join ").append(schema).append(".BIM_MERCHANT mer on info.MERCHANT_ID = mer.MERCHANT_ID ");
			fromBuffer.append("left join ").append(schema).append(".BIM_COMPANY c on info.CUSTOMER_ID = c.COMPANY_ID ");
			fromBuffer.append("left join ").append(schema).append(".DMM_ASSET_TYPE type on item.ITEM_NAME = type.ASSET_TYPE_ID ");
			fromBuffer.append("left join ").append(schema).append(".DMM_SUPPLIES s on item.ITEM_NAME = s.SUPPLIES_ID ");
			fromBuffer.append("left join ").append(schema).append(".BIM_CONTRACT con on item.CONTRACT_ID = con.CONTRACT_ID ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF reason on reason.BPTD_CODE = :paymentReason and reason.ITEM_VALUE = item.PAYMENT_REASON ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF typeDef on typeDef.BPTD_CODE = :paymentItem and typeDef.ITEM_VALUE = item.PAYMENT_ITEM ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF payType on payType.BPTD_CODE = :paymentType and payType.ITEM_VALUE = item.PAYMENT_TYPE ");
			fromBuffer.append("left join ").append(schema).append(".SRM_CASE_HANDLE_INFO historyCase on historyCase.CASE_ID = info.CASE_ID ");
			fromBuffer.append("left join ").append(schema).append(".DMM_ASSET_TYPE edcType on edcType.ASSET_TYPE_ID = historyCase.EDC_TYPE ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF caseCategory on caseCategory.BPTD_CODE = :caseCategory and caseCategory.ITEM_VALUE = historyCase.CASE_CATEGORY ");
			fromBuffer.append("left join ").append(schema).append(".BIM_MERCHANT_HEADER merHeader on merHeader.MERCHANT_HEADER_ID = historyCase.MERCHANT_HEADER_ID");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(customerId)) {
				sqlStatement.addWhereClause("info.CUSTOMER_ID = :customerId", customerId);
			}
			if (StringUtils.hasText(dtid)) {
				sqlStatement.addWhereClause("info.DTID like :dtid", dtid + IAtomsConstants.MARK_PERCENT);
			}
			if (createCaseStart != null) {
				sqlStatement.addWhereClause("info.CREATED_DATE >= :caseCreatedDateStart", createCaseStart);
			}
			if (createCaseEnd != null) {
				sqlStatement.addWhereClause("info.CREATED_DATE < :caseCreatedDateEnd", createCaseEnd);
			}
			if (StringUtils.hasText(merchantCode)) {
				sqlStatement.addWhereClause("mer.MERCHANT_CODE like :merchantCode", merchantCode + IAtomsConstants.MARK_PERCENT);
			}			
			if (StringUtils.hasText(tid)) {
				//sqlStatement.addWhereClause("info.TID like :tid", tid + IAtomsConstants.MARK_PERCENT);
				sqlStatement.addWhereClause("EXISTS (select 1 from " + schema + ".SRM_CASE_TRANSACTION_PARAMETER trans where trans.CASE_ID = info.CASE_ID and trans.TID like :tid)");
			}
			
			if (lostDayStart != null) {
				sqlStatement.addWhereClause("info.REAL_FINISH_DATE >= :lostDayStart", lostDayStart);
			}
			if (lostDayEnd != null) {
				sqlStatement.addWhereClause("info.REAL_FINISH_DATE < :lostDayEnd", lostDayEnd);
			}
			//Task #3452 微型商戶
			if (isMicro != null) {
				if(isMicro){
					sqlStatement.addWhereClause(" historyCase.CMS_CASE = 'Y'");
				} else {
					sqlStatement.addWhereClause(" historyCase.CMS_CASE = 'N'");
				}
			}
			
			/*if (lostDayStart != null) {
				sqlStatement.addWhereClause("info.REAL_FINISH_DATE = :realFinishDate", realFinishDate);
			}*/
			if (StringUtils.hasText(status) || StringUtils.hasText(serialNumber) || StringUtils.hasText(payType)) {
				StringBuffer whereBuffer = new StringBuffer();
				whereBuffer.append("exists(select 1 from dbo.SRM_PAYMENT_ITEM item1 where item1.PAYMENT_ID = item.PAYMENT_ID ");
				if (StringUtils.hasText(status)) {
					whereBuffer.append("and item1.STATUS = :queryDataStatus ");
				}
				if (StringUtils.hasText(payType)) {
					whereBuffer.append("and item1.PAYMENT_TYPE = :paymentType ");
				}
				if (StringUtils.hasText(serialNumber)) {
					whereBuffer.append("and item1.SERIAL_NUMBER like :serialNumber");
				}
				whereBuffer.append(IAtomsConstants.MARK_BRACKETS_RIGHT);
				sqlStatement.addWhereClause(whereBuffer.toString());
			}
			
			if (!CollectionUtils.isEmpty(paymentIds)) {
				sqlStatement.addWhereClause("item.PAYMENT_ID in ( :paymentId )");
			}
			if (StringUtils.hasText(sort) && StringUtils.hasText(order)) {
				if (SrmPaymentInfoDTO.ATTRIBUTE.CUSTOMER_NAME.getValue().equals(sort)) {
					sqlStatement.setOrderByExpression(sort + ", paymentId, paymentItemName ,itemName " + order);
				} else {
					sqlStatement.setOrderByExpression(sort + " " + order);
				}
			}
			sqlStatement.setPageSize(rows);
			sqlStatement.setStartPage(page - 1);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if (StringUtils.hasText(status)) {
				sqlQueryBean.setParameter(PaymentFormDTO.PARAM_QUERY_DATA_STATUS, status);
			}
			if (StringUtils.hasText(serialNumber)) {
				sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), serialNumber + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(payType)) {
				sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_TYPE.getValue(), payType);
			}
			if (StringUtils.hasText(tid)) {
				sqlQueryBean.setParameter("tid", tid + IAtomsConstants.MARK_PERCENT);
			}
			sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_ID.getValue(), paymentIds);
			sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_REASON.getValue(), IATOMS_PARAM_TYPE.PAYMENT_REASON.getCode());
			sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_ITEM.getValue(), IATOMS_PARAM_TYPE.PAYMENT_ITEM.getCode());
			sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_TYPE.getValue(), IATOMS_PARAM_TYPE.PAYMENT_TYPE.getCode());
			sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue(), IATOMS_PARAM_TYPE.TICKET_TYPE.getCode());
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmPaymentInfoDTO.class);
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
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentInfoDAO#getCount(java.lang.String, java.sql.Timestamp, java.sql.Timestamp, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.sql.Timestamp, java.lang.String)
	 */
	/*public Integer getCount(String customerId, Timestamp createCaseStart, Timestamp createCaseEnd, String merchantCode, 
			String dtid, String tid, String status, String payType, Timestamp realFinishDate, String serialNumber) throws DataAccessException {
		Integer count = null;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("count(DISTINCT info.PAYMENT_ID)");
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".SRM_PAYMENT_INFO info ");
			fromBuffer.append("left join ").append(schema).append(".SRM_PAYMENT_ITEM item on info.PAYMENT_ID = item.PAYMENT_ID ");
			fromBuffer.append("left join ").append(schema).append(".BIM_MERCHANT mer on info.MERCHANT_ID = mer.MERCHANT_ID ");
			fromBuffer.append("left join ").append(schema).append(".BIM_COMPANY c on info.CUSTOMER_ID = c.COMPANY_ID ");
			fromBuffer.append("left join ").append(schema).append(".DMM_ASSET_TYPE type on item.ITEM_NAME = type.ASSET_TYPE_ID ");
			fromBuffer.append("left join ").append(schema).append(".DMM_SUPPLIES s on item.ITEM_NAME = s.SUPPLIES_ID ");
			fromBuffer.append("left join ").append(schema).append(".BIM_CONTRACT con on item.CONTRACT_ID = con.CONTRACT_ID ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF reason on reason.BPTD_CODE = :paymentReason and reason.ITEM_VALUE = item.PAYMENT_REASON ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF def on def.BPTD_CODE = :status and def.ITEM_VALUE = item.STATUS ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF typeDef on typeDef.BPTD_CODE = :paymentItem and typeDef.ITEM_VALUE = item.PAYMENT_ITEM ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF payType on payType.BPTD_CODE = :paymentType and payType.ITEM_VALUE = item.PAYMENT_TYPE ");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(customerId)) {
				sqlStatement.addWhereClause("info.CUSTOMER_ID = :customerId", customerId);
			}
			if (StringUtils.hasText(dtid)) {
				sqlStatement.addWhereClause("info.DTID like :dtid", dtid + IAtomsConstants.MARK_PERCENT);
				
			}
			if (createCaseStart != null) {
				sqlStatement.addWhereClause("info.CREATED_DATE >= :caseCreatedDateStart", createCaseStart);
			}
			if (createCaseEnd != null) {
				sqlStatement.addWhereClause("info.CREATED_DATE < :caseCreatedDateEnd", createCaseEnd);
			}
			if (StringUtils.hasText(merchantCode)) {
				sqlStatement.addWhereClause("mer.MERCHANT_CODE like :merchantCode", merchantCode + IAtomsConstants.MARK_PERCENT);
			}
			
			if (StringUtils.hasText(tid)) {
				//sqlStatement.addWhereClause("info.TID = :tid", tid);
				sqlStatement.addWhereClause("EXISTS (select 1 from " + schema + ".SRM_CASE_TRANSACTION_PARAMETER trans where trans.CASE_ID = info.CASE_ID and trans.TID like :tid)");
			}
			if (realFinishDate != null) {
				sqlStatement.addWhereClause("info.REAL_FINISH_DATE = :realFinishDate", realFinishDate);
			}
			if (StringUtils.hasText(status) || StringUtils.hasText(serialNumber) || StringUtils.hasText(payType)) {
				StringBuffer whereBuffer = new StringBuffer();
				whereBuffer.append("exists(select 1 from dbo.SRM_PAYMENT_ITEM item1 where item1.PAYMENT_ID = item.PAYMENT_ID ");
				if (StringUtils.hasText(status)) {
					whereBuffer.append("and item1.STATUS = :status ");
				}
				if (StringUtils.hasText(payType)) {
					whereBuffer.append("and item1.PAYMENT_TYPE = :paymentType ");
				}
				if (StringUtils.hasText(serialNumber)) {
					whereBuffer.append("and item1.SERIAL_NUMBER like :serialNumber");
				}
				whereBuffer.append(IAtomsConstants.MARK_BRACKETS_RIGHT);
				sqlStatement.addWhereClause(whereBuffer.toString());
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if (StringUtils.hasText(status)) {
				sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.STATUS.getValue(), status);
			}
			if (StringUtils.hasText(serialNumber)) {
				sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), serialNumber + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(payType)) {
				sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_TYPE.getValue(), payType);
			}
			if (StringUtils.hasText(tid)) {
				sqlQueryBean.setParameter("tid", tid + IAtomsConstants.MARK_PERCENT);
			}
			sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_REASON.getValue(), IATOMS_PARAM_TYPE.PAYMENT_REASON.getCode());
			sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.STATUS.getValue(), IATOMS_PARAM_TYPE.PAYMENT_STATUS.getCode());
			sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_ITEM.getValue(), IATOMS_PARAM_TYPE.PAYMENT_ITEM.getCode());
			sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_TYPE.getValue(), IATOMS_PARAM_TYPE.PAYMENT_TYPE.getCode());
			LOGGER.debug(".getCount() --> sql: ", sqlQueryBean.toString());
			List<Integer> list = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if(!CollectionUtils.isEmpty(list)){
				count = list.get(0);
			}
		} catch (Exception e) {
			LOGGER.error(".getCount() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return count;
	}*/

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentInfoDAO#getIds(java.lang.String, java.lang.String, java.lang.String, java.sql.Timestamp, java.sql.Timestamp, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.sql.Timestamp, java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	public List<SrmPaymentInfoDTO> getIds(String customerId, String paymentId, Timestamp createCaseStart,
			Timestamp createCaseEnd, String merchantCode, String dtid, String tid, String status, String payType,
			Timestamp lostDayStart, Timestamp lostDayEnd, String serialNumber, String order, String sort, int page, int rows, boolean isMicro) throws DataAccessException {
		List<SrmPaymentInfoDTO> paymentIds = null;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("DISTINCT info.PAYMENT_ID", SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_ID.getValue());
			sqlStatement.addSelectClause("c.SHORT_NAME", SrmPaymentInfoDTO.ATTRIBUTE.CUSTOMER_NAME.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".SRM_PAYMENT_INFO info ");
			fromBuffer.append("left join ").append(schema).append(".SRM_PAYMENT_ITEM item on info.PAYMENT_ID = item.PAYMENT_ID ");
			fromBuffer.append("left join ").append(schema).append(".BIM_MERCHANT mer on info.MERCHANT_ID = mer.MERCHANT_ID ");
			fromBuffer.append("left join ").append(schema).append(".BIM_COMPANY c on info.CUSTOMER_ID = c.COMPANY_ID ");
			//Task #3452 微型商戶
			fromBuffer.append("left join ").append(schema).append(".SRM_CASE_HANDLE_INFO caseHandle on info.CASE_ID = caseHandle.CASE_ID ");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(customerId)) {
				sqlStatement.addWhereClause("info.CUSTOMER_ID = :customerId", customerId);
			}
			if (StringUtils.hasText(dtid)) {
				sqlStatement.addWhereClause("info.DTID like :dtid", dtid + IAtomsConstants.MARK_PERCENT);
			}
			if (createCaseStart != null) {
				sqlStatement.addWhereClause("info.CREATED_DATE >= :caseCreatedDateStart", createCaseStart);
			}
			if (createCaseEnd != null) {
				sqlStatement.addWhereClause("info.CREATED_DATE < :caseCreatedDateEnd", createCaseEnd);
			}
			if (StringUtils.hasText(merchantCode)) {
				sqlStatement.addWhereClause("mer.MERCHANT_CODE like :merchantCode", merchantCode + IAtomsConstants.MARK_PERCENT);
			}
			
			
			if (StringUtils.hasText(tid)) {
				//sqlStatement.addWhereClause("info.TID = :tid", tid);
				sqlStatement.addWhereClause("EXISTS (select 1 from " + schema + ".SRM_CASE_TRANSACTION_PARAMETER trans where trans.CASE_ID = info.CASE_ID and trans.TID like :tid)");
			}
			
			if (lostDayStart != null) {
				sqlStatement.addWhereClause("info.REAL_FINISH_DATE >= :lostDayStart", lostDayStart);
			}
			if (lostDayEnd != null) {
				sqlStatement.addWhereClause("info.REAL_FINISH_DATE < :lostDayEnd", lostDayEnd);
			}
			//Task #3452 微型商戶
			if(isMicro){
				sqlStatement.addWhereClause(" caseHandle.CMS_CASE = 'Y'");
			} else {
				sqlStatement.addWhereClause(" caseHandle.CMS_CASE = 'N'");
			}
			/*if (realFinishDate != null) {
				sqlStatement.addWhereClause("info.REAL_FINISH_DATE = :realFinishDate", realFinishDate);
			}*/
			if (StringUtils.hasText(status) || StringUtils.hasText(serialNumber) || StringUtils.hasText(payType)) {
				StringBuffer whereBuffer = new StringBuffer();
				whereBuffer.append("exists(select 1 from dbo.SRM_PAYMENT_ITEM item1 where item1.PAYMENT_ID = item.PAYMENT_ID ");
				if (StringUtils.hasText(status)) {
					whereBuffer.append("and item1.STATUS like :status ");
				}
				if (StringUtils.hasText(payType)) {
					whereBuffer.append("and item1.PAYMENT_TYPE = :paymentType ");
				}
				if (StringUtils.hasText(serialNumber)) {
					whereBuffer.append("and item1.SERIAL_NUMBER like :serialNumber");
				}
				whereBuffer.append(IAtomsConstants.MARK_BRACKETS_RIGHT);
				sqlStatement.addWhereClause(whereBuffer.toString());
			}
			if (rows != -1 && page != -1) {
				sqlStatement.setPageSize(rows);
				sqlStatement.setStartPage(page - 1);
			}
			
			if (StringUtils.hasText(sort) && (SrmPaymentInfoDTO.ATTRIBUTE.CUSTOMER_NAME.getValue().equals(sort)
					|| SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_ID.getValue().equals(sort))) {
				sqlStatement.setOrderByExpression(sort + " " + order);
			} else {
				sqlStatement.setOrderByExpression("info.PAYMENT_ID " + order);
			}
			
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if (StringUtils.hasText(status)) {
				sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.STATUS.getValue(), status + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(serialNumber)) {
				sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), serialNumber + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(payType)) {
				sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_TYPE.getValue(), payType);
			}
			if (StringUtils.hasText(tid)) {
				sqlQueryBean.setParameter("tid", tid + IAtomsConstants.MARK_PERCENT);
			}
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmPaymentInfoDTO.class);
			/*sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_REASON.getValue(), IATOMS_PARAM_TYPE.PAYMENT_REASON.getCode());
			sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.STATUS.getValue(), IATOMS_PARAM_TYPE.PAYMENT_STATUS.getCode());
			sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_ITEM.getValue(), IATOMS_PARAM_TYPE.PAYMENT_ITEM.getCode());
			sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_TYPE.getValue(), IATOMS_PARAM_TYPE.PAYMENT_TYPE.getCode());*/
			LOGGER.debug(".getIds() --> sql: ", sqlQueryBean.toString());
			paymentIds = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(".getIds() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return paymentIds;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentInfoDAO#listFeePaymentInfoList(java.lang.String)
	 */
	public List<SrmPaymentInfoDTO> listFeePaymentInfoList(List<String> paymentTypeList, String customerId) throws DataAccessException {
		List<SrmPaymentInfoDTO> paymentIds = null;
		try {
			LOGGER.debug("listFeePaymentInfoList()", "parameter:customerId=", customerId);
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("select ");
			sqlQueryBean.append(" ROW_NUMBER() OVER( ORDER BY info1.PAYMENT_ID, item.PAYMENT_ITEM, type.ASSET_CATEGORY, item.ITEM_NAME ) AS rowId , ");
			sqlQueryBean.append(" ( CASE WHEN item.PAYMENT_ITEM = :payment_asset ");
			sqlQueryBean.append(" THEN type.NAME ELSE s.SUPPLIES_NAME END ) AS suppliesName , ");
			sqlQueryBean.append(" item.SERIAL_NUMBER AS serialNumber, ");
			sqlQueryBean.append(" con.CONTRACT_CODE AS contractCode, ");
			sqlQueryBean.append(" item.SUPPLIES_AMOUNT AS suppliesAmount, ");
			sqlQueryBean.append(" (case when base.ITEM_VALUE='SELF_INPUT' THEN base.ITEM_NAME + '-' + item.REASON_DETAIL ELSE base.ITEM_NAME END) AS paymentReason, ");
			sqlQueryBean.append(" info1.DTID AS dtid, ");
			sqlQueryBean.append(" info1.TID AS tid, ");
			sqlQueryBean.append(" mer.MERCHANT_CODE AS merchantCode, ");
			sqlQueryBean.append(" mer.NAME AS name, ");
			sqlQueryBean.append(" info1.REAL_FINISH_DATE AS realFinishDate, ");
			sqlQueryBean.append(" info1.PAYMENT_ID AS paymentId, ");
			sqlQueryBean.append(" item.FEE AS fee, ");
			sqlQueryBean.append(" herder.AO_NAME AS aoName, ");
			sqlQueryBean.append(" item.LOCKED_DATE AS lockedDate ");
			sqlQueryBean.append(" FROM ").append(schema).append(" .SRM_PAYMENT_INFO info1 ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .SRM_PAYMENT_ITEM item  ON info1.PAYMENT_ID = item.PAYMENT_ID  ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE type ON item.ITEM_NAME = type.ASSET_TYPE_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .DMM_SUPPLIES s ON item.ITEM_NAME = s.SUPPLIES_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT con ON item.CONTRACT_ID = con.CONTRACT_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer ON info1.MERCHANT_ID = mer.MERCHANT_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT_HEADER herder ON info1.MERCHANT_HEADER_ID = herder.MERCHANT_HEADER_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .base_parameter_item_def base on item.PAYMENT_REASON = base.item_value and base.bptd_code = 'PAYMENT_REASON' ");

			sqlQueryBean.append(" WHERE ");
			sqlQueryBean.append(" item.STATUS = :pay_confirm");
			sqlQueryBean.append(" AND item.IS_ASK_PAY = 'Y' ");
			sqlQueryBean.append(" AND PAYMENT_TYPE IN (:paymentTypeList) ");
			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND info1.CUSTOMER_ID = :customerId ");
				sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
				LOGGER.debug(this.getClass().getName(), ".listFeePaymentInfoList() --> customerId: ", customerId);
			}
			sqlQueryBean.append(" order by info1.PAYMENT_ID, item.PAYMENT_ITEM, type.ASSET_CATEGORY, item.ITEM_NAME ");
			//設備
			sqlQueryBean.setParameter("payment_asset", IAtomsConstants.PAYMENT_ITEM_ASSET);
			//求償確認
			sqlQueryBean.setParameter("pay_confirm", IAtomsConstants.DATA_STATUS_CLAIM_CONFIRM);
			sqlQueryBean.setParameter("paymentTypeList", paymentTypeList);
			AliasBean aliasBean = new AliasBean(SrmPaymentInfoDTO.class);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.ROW_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.SUPPLIES_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.SUPPLIES_AMOUNT.getValue(), IntegerType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_REASON.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.TID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.REAL_FINISH_DATE.getValue(), DateType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.FEE.getValue(), BigDecimalType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.AO_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.LOCKED_DATE.getValue(), TimestampType.INSTANCE);
			LOGGER.debug("listFeePaymentInfoList()", "sql:", sqlQueryBean.toString());
			paymentIds = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(".listFeePaymentInfoList() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return paymentIds;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentInfoDAO#listFeePaymentInfoListToTCBEDC(java.util.List, java.lang.String)
	 */
	public List<SrmPaymentInfoDTO> listFeePaymentInfoListToTCBEDC(List<String> paymentStatusList, String customerId) throws DataAccessException {
		List<SrmPaymentInfoDTO> paymentIds = null;
		try {
			LOGGER.debug("listFeePaymentInfoListToTCBEDC()", "parameter:customerId=", customerId);
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("select ");
			sqlQueryBean.append(" ROW_NUMBER() OVER( ORDER BY info1.PAYMENT_ID, item.PAYMENT_ITEM, s.SUPPLIES_NAME, item.ITEM_NAME ) AS rowId , ");
			sqlQueryBean.append(" ( CASE WHEN item.PAYMENT_ITEM = :payment_asset ");
			sqlQueryBean.append(" THEN type.NAME ELSE s.SUPPLIES_NAME END ) AS suppliesName , ");
			sqlQueryBean.append(" item.SERIAL_NUMBER AS serialNumber, ");
			sqlQueryBean.append(" con.CONTRACT_CODE AS contractCode, ");
			sqlQueryBean.append(" item.SUPPLIES_AMOUNT AS suppliesAmount, ");
			//sqlQueryBean.append(" base.ITEM_NAME AS paymentReason, ");
			sqlQueryBean.append(" (case when base.ITEM_VALUE='SELF_INPUT' THEN base.ITEM_NAME + '-' + item.REASON_DETAIL ELSE base.ITEM_NAME END) AS paymentReason, ");
			sqlQueryBean.append(" info1.DTID AS dtid, ");
			sqlQueryBean.append(" info1.TID AS tid, ");
			sqlQueryBean.append(" mer.MERCHANT_CODE AS merchantCode, ");
			sqlQueryBean.append(" mer.NAME AS name, ");
			sqlQueryBean.append(" info1.REAL_FINISH_DATE AS realFinishDate, ");
			sqlQueryBean.append(" item.FEE AS fee, ");
			sqlQueryBean.append(" herder.AO_NAME AS aoName, ");
			sqlQueryBean.append(" item.LOCKED_DATE AS lockedDate ");
			sqlQueryBean.append(" FROM ").append(schema).append(" .SRM_PAYMENT_INFO info1 ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .SRM_PAYMENT_ITEM item  ON info1.PAYMENT_ID = item.PAYMENT_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE type ON item.ITEM_NAME = type.ASSET_TYPE_ID ")
			.append(" and item. PAYMENT_ITEM='PAYMENT_ASSET' ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .DMM_SUPPLIES s ON item.ITEM_NAME = s.SUPPLIES_ID ")
			.append(" and item. PAYMENT_ITEM='PAYMENT_SUPPLIES' ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT con ON item.CONTRACT_ID = con.CONTRACT_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer ON info1.MERCHANT_ID = mer.MERCHANT_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT_HEADER herder ON info1.MERCHANT_HEADER_ID = herder.MERCHANT_HEADER_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .base_parameter_item_def base on item.PAYMENT_REASON = base.item_value and base.bptd_code = 'PAYMENT_REASON' ");
			sqlQueryBean.append(" WHERE 1 = 1 ");
			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND info1.CUSTOMER_ID = :customerId ");
				sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
				LOGGER.debug(this.getClass().getName(), ".listFeePaymentInfoListToTCBEDC() --> customerId: ", customerId);
			}
			sqlQueryBean.append(" AND item.STATUS IN ( :paymentStatusList ) ");
			sqlQueryBean.append(" order by info1.PAYMENT_ID, item.PAYMENT_ITEM, s.SUPPLIES_NAME, item.ITEM_NAME ");
			//設備
			sqlQueryBean.setParameter("payment_asset", IAtomsConstants.PAYMENT_ITEM_ASSET);
			//求償確認
			sqlQueryBean.setParameter("paymentStatusList", paymentStatusList);
			AliasBean aliasBean = new AliasBean(SrmPaymentInfoDTO.class);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.ROW_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.SUPPLIES_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.SUPPLIES_AMOUNT.getValue(), IntegerType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_REASON.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.TID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.REAL_FINISH_DATE.getValue(), DateType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.FEE.getValue(), BigDecimalType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.AO_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.LOCKED_DATE.getValue(), TimestampType.INSTANCE);
			LOGGER.debug("listFeePaymentInfoListToTCBEDC()", "sql:", sqlQueryBean.toString());
			paymentIds = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(".listFeePaymentInfoListToTCBEDC() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return paymentIds;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentInfoDAO#listPaymentInfoListToGreenWorld(java.util.List, java.lang.String)
	 */
	@Override
	public List<SrmPaymentInfoDTO> paymentInfoListToGreenWorld(List<String> paymentTypeList, String customerId)
			throws DataAccessException {
		List<SrmPaymentInfoDTO> paymentIds = null;
		try {
			LOGGER.debug("paymentInfoListToGreenWorld()", "parameter:customerId=", customerId);
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("select ");
			sqlQueryBean.append(" ROW_NUMBER() OVER( ORDER BY info1.PAYMENT_ID, item.PAYMENT_ITEM, s.SUPPLIES_NAME, item.ITEM_NAME  ) AS rowId , ");
			sqlQueryBean.append(" ( CASE WHEN item.PAYMENT_ITEM = :payment_asset ");
			sqlQueryBean.append(" THEN type.NAME ELSE s.SUPPLIES_NAME END ) AS suppliesName , ");
			sqlQueryBean.append(" item.SERIAL_NUMBER AS serialNumber, ");
			sqlQueryBean.append(" con.CONTRACT_CODE AS contractCode, ");
			sqlQueryBean.append(" item.SUPPLIES_AMOUNT AS suppliesAmount, ");
			//sqlQueryBean.append(" base.ITEM_NAME AS paymentReason, ");
			sqlQueryBean.append(" (case when base.ITEM_VALUE='SELF_INPUT' THEN base.ITEM_NAME + '-' + item.REASON_DETAIL ELSE base.ITEM_NAME END) AS paymentReason, ");
			sqlQueryBean.append(" info1.DTID AS dtid, ");
			sqlQueryBean.append(" info1.TID AS tid, ");
			sqlQueryBean.append(" mer.MERCHANT_CODE AS merchantCode, ");
			sqlQueryBean.append(" mer.NAME AS name, ");
			sqlQueryBean.append(" info1.REAL_FINISH_DATE AS realFinishDate, ");
			sqlQueryBean.append(" item.FEE AS fee, ");
			sqlQueryBean.append(" herder.AO_NAME AS aoName, ");
			sqlQueryBean.append(" item.LOCKED_DATE AS lockedDate ");
			sqlQueryBean.append(" FROM ").append(schema).append(" .SRM_PAYMENT_INFO info1 ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .SRM_PAYMENT_ITEM item  ON info1.PAYMENT_ID = item.PAYMENT_ID  ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE type ON item.ITEM_NAME = type.ASSET_TYPE_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .DMM_SUPPLIES s ON item.ITEM_NAME = s.SUPPLIES_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT con ON item.CONTRACT_ID = con.CONTRACT_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer ON info1.MERCHANT_ID = mer.MERCHANT_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT_HEADER herder ON info1.MERCHANT_HEADER_ID = herder.MERCHANT_HEADER_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .base_parameter_item_def base on item.PAYMENT_REASON = base.item_value and base.bptd_code = 'PAYMENT_REASON' ");
			sqlQueryBean.append(" WHERE ");
			sqlQueryBean.append(" item.STATUS = :pay_confirm");
			sqlQueryBean.append(" AND item.IS_ASK_PAY = 'Y' ");
			sqlQueryBean.append(" AND PAYMENT_TYPE IN (:paymentTypeList) ");
			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND info1.CUSTOMER_ID  = :customerId ");
				sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
				LOGGER.debug(this.getClass().getName(), ".paymentInfoListToGreenWorld() --> customerId: ", customerId);
			}
			sqlQueryBean.append(" ORDER BY info1.PAYMENT_ID, item.PAYMENT_ITEM, s.SUPPLIES_NAME, item.ITEM_NAME ");
			//設備
			sqlQueryBean.setParameter("payment_asset", IAtomsConstants.PAYMENT_ITEM_ASSET);
			//求償確認
			sqlQueryBean.setParameter("pay_confirm", IAtomsConstants.DATA_STATUS_CLAIM_CONFIRM);
			sqlQueryBean.setParameter("paymentTypeList", paymentTypeList);
			AliasBean aliasBean = new AliasBean(SrmPaymentInfoDTO.class);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.ROW_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.SUPPLIES_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.SUPPLIES_AMOUNT.getValue(), IntegerType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_REASON.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.TID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.REAL_FINISH_DATE.getValue(), DateType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.FEE.getValue(), BigDecimalType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.AO_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.LOCKED_DATE.getValue(), TimestampType.INSTANCE);
			LOGGER.debug("paymentInfoListToGreenWorld()", "sql:", sqlQueryBean.toString());
			paymentIds = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(".paymentInfoListToGreenWorld() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return paymentIds;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentInfoDAO#paymentInfoListToGreenWorldNoTax(java.util.List, java.lang.String)
	 */
	@Override
	public List<SrmPaymentInfoDTO> paymentInfoListToGreenWorldNoTax(List<String> paymentStatusList, String customerId)
			throws DataAccessException {
		List<SrmPaymentInfoDTO> paymentIds = null;
		try {
			LOGGER.debug("paymentInfoListToGreenWorldNoTax()", "parameter:customerId=", customerId);
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("select ");
			sqlQueryBean.append(" ROW_NUMBER() OVER( ORDER BY info1.PAYMENT_ID, item.PAYMENT_ITEM, s.SUPPLIES_NAME, item.ITEM_NAME ) AS rowId , ");
			sqlQueryBean.append(" ( CASE WHEN item.PAYMENT_ITEM = :payment_asset ");
			sqlQueryBean.append(" THEN type.NAME ELSE s.SUPPLIES_NAME END ) AS suppliesName , ");
			sqlQueryBean.append(" item.SERIAL_NUMBER AS serialNumber, ");
			sqlQueryBean.append(" con.CONTRACT_CODE AS contractCode, ");
			sqlQueryBean.append(" item.SUPPLIES_AMOUNT AS suppliesAmount, ");
			//sqlQueryBean.append(" base.ITEM_NAME AS paymentReason, ");
			sqlQueryBean.append(" (case when base.ITEM_VALUE='SELF_INPUT' THEN base.ITEM_NAME + '-' + item.REASON_DETAIL ELSE base.ITEM_NAME END) AS paymentReason, ");
			sqlQueryBean.append(" info1.DTID AS dtid, ");
			sqlQueryBean.append(" info1.TID AS tid, ");
			sqlQueryBean.append(" mer.MERCHANT_CODE AS merchantCode, ");
			sqlQueryBean.append(" mer.NAME AS name, ");
			sqlQueryBean.append(" info1.REAL_FINISH_DATE AS realFinishDate, ");
			sqlQueryBean.append(" item.FEE AS fee, ");
			sqlQueryBean.append(" herder.AO_NAME AS aoName, ");
			sqlQueryBean.append(" item.LOCKED_DATE AS lockedDate ");
			sqlQueryBean.append(" FROM ").append(schema).append(" .SRM_PAYMENT_INFO info1 ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .SRM_PAYMENT_ITEM item  ON info1.PAYMENT_ID = item.PAYMENT_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE type ON item.ITEM_NAME = type.ASSET_TYPE_ID ")
			.append(" and item. PAYMENT_ITEM='PAYMENT_ASSET' ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .DMM_SUPPLIES s ON item.ITEM_NAME = s.SUPPLIES_ID ")
			.append(" and item. PAYMENT_ITEM= 'PAYMENT_SUPPLIES' ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT con ON item.CONTRACT_ID = con.CONTRACT_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer ON info1.MERCHANT_ID = mer.MERCHANT_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT_HEADER herder ON info1.MERCHANT_HEADER_ID = herder.MERCHANT_HEADER_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .base_parameter_item_def base on item.PAYMENT_REASON = base.item_value and base.bptd_code = 'PAYMENT_REASON' ");
			sqlQueryBean.append(" WHERE 1 = 1 ");
			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND info1.CUSTOMER_ID = :customerId ");
				sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
				LOGGER.debug(this.getClass().getName(), ".paymentInfoListToGreenWorldNoTax() --> customerId: ", customerId);
			}
			sqlQueryBean.append(" AND item.STATUS IN ( :paymentStatusList ) ");
			sqlQueryBean.append(" ORDER BY info1.PAYMENT_ID, item.PAYMENT_ITEM, s.SUPPLIES_NAME, item.ITEM_NAME ");
			//設備
			sqlQueryBean.setParameter("payment_asset", IAtomsConstants.PAYMENT_ITEM_ASSET);
			//求償確認
			sqlQueryBean.setParameter("paymentStatusList", paymentStatusList);
			AliasBean aliasBean = new AliasBean(SrmPaymentInfoDTO.class);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.ROW_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.SUPPLIES_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.SUPPLIES_AMOUNT.getValue(), IntegerType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_REASON.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.TID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.REAL_FINISH_DATE.getValue(), DateType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.FEE.getValue(), BigDecimalType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.AO_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.LOCKED_DATE.getValue(), TimestampType.INSTANCE);
			LOGGER.debug("paymentInfoListToGreenWorldNoTax()", "sql:", sqlQueryBean.toString());
			paymentIds = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("paymentInfoListToGreenWorldNoTax() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return paymentIds;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentInfoDAO#paymentInfoListToScsb(java.util.List, java.lang.String)
	 */
	@Override
	public List<SrmPaymentInfoDTO> paymentInfoListToScsb(List<String> paymentTypeList, String customerId)
			throws DataAccessException {
		List<SrmPaymentInfoDTO> paymentIds = null;
		try {
			LOGGER.debug("paymentInfoListToScsb()", "parameter:customerId=", customerId);
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("select ");
			sqlQueryBean.append(" ROW_NUMBER() OVER( ORDER BY info1.PAYMENT_ID, item.PAYMENT_ITEM, s.SUPPLIES_NAME, item.ITEM_NAME ) AS rowId , ");
			sqlQueryBean.append(" ( CASE WHEN item.PAYMENT_ITEM = :payment_asset ");
			sqlQueryBean.append(" THEN type.NAME ELSE s.SUPPLIES_NAME END ) AS suppliesName , ");
			sqlQueryBean.append(" item.SERIAL_NUMBER AS serialNumber, ");
			sqlQueryBean.append(" con.CONTRACT_CODE AS contractCode, ");
			sqlQueryBean.append(" item.SUPPLIES_AMOUNT AS suppliesAmount, ");
			//sqlQueryBean.append(" base.ITEM_NAME AS paymentReason, ");
			sqlQueryBean.append(" (case when base.ITEM_VALUE='SELF_INPUT' THEN base.ITEM_NAME + '-' + item.REASON_DETAIL ELSE base.ITEM_NAME END) AS paymentReason, ");
			sqlQueryBean.append(" info1.DTID AS dtid, ");
			sqlQueryBean.append(" info1.TID AS tid, ");
			sqlQueryBean.append(" mer.MERCHANT_CODE AS merchantCode, ");
			sqlQueryBean.append(" mer.NAME AS name, ");
			sqlQueryBean.append(" info1.REAL_FINISH_DATE AS realFinishDate, ");
			sqlQueryBean.append(" item.FEE AS fee, ");
			sqlQueryBean.append(" herder.AO_NAME AS aoName, ");
			sqlQueryBean.append(" item.LOCKED_DATE AS lockedDate ");
			sqlQueryBean.append(" FROM ").append(schema).append(" .SRM_PAYMENT_INFO info1 ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .SRM_PAYMENT_ITEM item  ON info1.PAYMENT_ID = item.PAYMENT_ID  ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE type ON item.ITEM_NAME = type.ASSET_TYPE_ID ")
			.append(" and item. PAYMENT_ITEM='PAYMENT_ASSET' ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .DMM_SUPPLIES s ON item.ITEM_NAME = s.SUPPLIES_ID ")
			.append(" and item. PAYMENT_ITEM= 'PAYMENT_SUPPLIES' ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT con ON item.CONTRACT_ID = con.CONTRACT_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer ON info1.MERCHANT_ID = mer.MERCHANT_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT_HEADER herder ON info1.MERCHANT_HEADER_ID = herder.MERCHANT_HEADER_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .base_parameter_item_def base on item.PAYMENT_REASON = base.item_value and base.bptd_code = 'PAYMENT_REASON' ");
			sqlQueryBean.append(" WHERE ");
			//求償確認
			sqlQueryBean.append(" item.STATUS = :pay_confirm");
			sqlQueryBean.append(" AND item.IS_ASK_PAY = 'Y' ");
			//特店賠償、客戶賠償
			sqlQueryBean.append(" AND PAYMENT_TYPE IN (:paymentTypeList) ");
			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND info1.CUSTOMER_ID = :customerId ");
				sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
				LOGGER.debug(this.getClass().getName(), ".paymentInfoListToScsb() --> customerId: ", customerId);
			}
			sqlQueryBean.append(" ORDER BY info1.PAYMENT_ID, item.PAYMENT_ITEM, s.SUPPLIES_NAME, item.ITEM_NAME ");
			//設備
			sqlQueryBean.setParameter("payment_asset", IAtomsConstants.PAYMENT_ITEM_ASSET);
			//求償確認
			sqlQueryBean.setParameter("pay_confirm", IAtomsConstants.DATA_STATUS_CLAIM_CONFIRM);
			sqlQueryBean.setParameter("paymentTypeList", paymentTypeList);
			AliasBean aliasBean = new AliasBean(SrmPaymentInfoDTO.class);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.ROW_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.SUPPLIES_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.SUPPLIES_AMOUNT.getValue(), IntegerType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_REASON.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.TID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.REAL_FINISH_DATE.getValue(), DateType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.FEE.getValue(), BigDecimalType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.AO_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.LOCKED_DATE.getValue(), TimestampType.INSTANCE);
			LOGGER.debug("paymentInfoListToScsb()", "sql:", sqlQueryBean.toString());
			paymentIds = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("paymentInfoListToScsb() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return paymentIds;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentInfoDAO#paymentInfoListToScsbNoTax(java.util.List, java.lang.String)
	 */
	@Override
	public List<SrmPaymentInfoDTO> paymentInfoListToScsbNoTax(List<String> paymentStatusList, String customerId)
			throws DataAccessException {
		List<SrmPaymentInfoDTO> paymentIds = null;
		try {
			LOGGER.debug("paymentInfoListToScsbNoTax()", "parameter:customerId=", customerId);
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("select ");
			sqlQueryBean.append(" ROW_NUMBER() OVER( ORDER BY info1.PAYMENT_ID, item.PAYMENT_ITEM, s.SUPPLIES_NAME, item.ITEM_NAME ) AS rowId , ");
			sqlQueryBean.append(" ( CASE WHEN item.PAYMENT_ITEM = :payment_asset ");
			sqlQueryBean.append(" THEN type.NAME ELSE s.SUPPLIES_NAME END ) AS suppliesName , ");
			sqlQueryBean.append(" item.SERIAL_NUMBER AS serialNumber, ");
			sqlQueryBean.append(" con.CONTRACT_CODE AS contractCode, ");
			sqlQueryBean.append(" item.SUPPLIES_AMOUNT AS suppliesAmount, ");
			//sqlQueryBean.append(" base.ITEM_NAME AS paymentReason, ");
			sqlQueryBean.append(" (case when base.ITEM_VALUE='SELF_INPUT' THEN base.ITEM_NAME + '-' + item.REASON_DETAIL ELSE base.ITEM_NAME END) AS paymentReason, ");
			sqlQueryBean.append(" info1.DTID AS dtid, ");
			sqlQueryBean.append(" info1.TID AS tid, ");
			sqlQueryBean.append(" mer.MERCHANT_CODE AS merchantCode, ");
			sqlQueryBean.append(" mer.NAME AS name, ");
			sqlQueryBean.append(" info1.REAL_FINISH_DATE AS realFinishDate, ");
			sqlQueryBean.append(" item.FEE AS fee, ");
			sqlQueryBean.append(" herder.AO_NAME AS aoName, ");
			sqlQueryBean.append(" item.LOCKED_DATE AS lockedDate ");
			sqlQueryBean.append(" FROM ").append(schema).append(" .SRM_PAYMENT_INFO info1 ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .SRM_PAYMENT_ITEM item  ON info1.PAYMENT_ID = item.PAYMENT_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE type ON item.ITEM_NAME = type.ASSET_TYPE_ID ")
			.append(" and item. PAYMENT_ITEM='PAYMENT_ASSET' ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .DMM_SUPPLIES s ON item.ITEM_NAME = s.SUPPLIES_ID ")
			.append(" and item. PAYMENT_ITEM= 'PAYMENT_SUPPLIES' ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT con ON item.CONTRACT_ID = con.CONTRACT_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer ON info1.MERCHANT_ID = mer.MERCHANT_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT_HEADER herder ON info1.MERCHANT_HEADER_ID = herder.MERCHANT_HEADER_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .base_parameter_item_def base on item.PAYMENT_REASON = base.item_value and base.bptd_code = 'PAYMENT_REASON' ");
			sqlQueryBean.append(" WHERE 1 = 1 ");
			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND info1.CUSTOMER_ID = :customerId ");
				sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
				LOGGER.debug(this.getClass().getName(), ".paymentInfoListToScsbNoTax() --> customerId: ", customerId);
			}
			sqlQueryBean.append(" AND item.STATUS IN ( :paymentStatusList ) ");
			sqlQueryBean.append(" ORDER BY info1.PAYMENT_ID, item.PAYMENT_ITEM, s.SUPPLIES_NAME, item.ITEM_NAME ");
			//設備
			sqlQueryBean.setParameter("payment_asset", IAtomsConstants.PAYMENT_ITEM_ASSET);
			//求償確認
			sqlQueryBean.setParameter("paymentStatusList", paymentStatusList);
			AliasBean aliasBean = new AliasBean(SrmPaymentInfoDTO.class);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.ROW_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.SUPPLIES_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.SUPPLIES_AMOUNT.getValue(), IntegerType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_REASON.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.TID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.REAL_FINISH_DATE.getValue(), DateType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.FEE.getValue(), BigDecimalType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.AO_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.LOCKED_DATE.getValue(), TimestampType.INSTANCE);
			LOGGER.debug("paymentInfoListToScsbNoTax()", "sql:", sqlQueryBean.toString());
			paymentIds = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("paymentInfoListToScsbNoTax() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return paymentIds;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentInfoDAO#paymentInfoListToSyb(java.util.List, java.lang.String)
	 */
	@Override
	public List<SrmPaymentInfoDTO> paymentInfoListToSyb(List<String> paymentTypeList, String customerId)
			throws DataAccessException {
		List<SrmPaymentInfoDTO> paymentIds = null;
		try {
			LOGGER.debug("paymentInfoListToSyb()", "parameter:customerId=", customerId);
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("select ");
			sqlQueryBean.append(" ROW_NUMBER() OVER( ORDER BY info1.PAYMENT_ID, item.PAYMENT_ITEM, s.SUPPLIES_NAME, item.ITEM_NAME ) AS rowId , ");
			sqlQueryBean.append(" ( CASE WHEN item.PAYMENT_ITEM = :payment_asset ");
			sqlQueryBean.append(" THEN type.NAME ELSE s.SUPPLIES_NAME END ) AS suppliesName , ");
			sqlQueryBean.append(" item.SERIAL_NUMBER AS serialNumber, ");
			sqlQueryBean.append(" con.CONTRACT_CODE AS contractCode, ");
			sqlQueryBean.append(" item.SUPPLIES_AMOUNT AS suppliesAmount, ");
			sqlQueryBean.append(" (case when base.ITEM_VALUE='SELF_INPUT' THEN base.ITEM_NAME + '-' + item.REASON_DETAIL ELSE base.ITEM_NAME END) AS paymentReason, ");
			//sqlQueryBean.append(" base.ITEM_NAME AS paymentReason, ");
			sqlQueryBean.append(" info1.DTID AS dtid, ");
			sqlQueryBean.append(" info1.TID AS tid, ");
			sqlQueryBean.append(" mer.MERCHANT_CODE AS merchantCode, ");
			sqlQueryBean.append(" mer.NAME AS name, ");
			sqlQueryBean.append(" info1.REAL_FINISH_DATE AS realFinishDate, ");
			sqlQueryBean.append(" item.FEE AS fee, ");
			sqlQueryBean.append(" herder.AO_NAME AS aoName, ");
			sqlQueryBean.append(" item.LOCKED_DATE AS lockedDate ");
			sqlQueryBean.append(" FROM ").append(schema).append(" .SRM_PAYMENT_INFO info1 ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .SRM_PAYMENT_ITEM item  ON info1.PAYMENT_ID = item.PAYMENT_ID  ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE type ON item.ITEM_NAME = type.ASSET_TYPE_ID ")
			.append(" and item. PAYMENT_ITEM='PAYMENT_ASSET' ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .DMM_SUPPLIES s ON item.ITEM_NAME = s.SUPPLIES_ID ")
			.append(" and item. PAYMENT_ITEM= 'PAYMENT_SUPPLIES' ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT con ON item.CONTRACT_ID = con.CONTRACT_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer ON info1.MERCHANT_ID = mer.MERCHANT_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT_HEADER herder ON info1.MERCHANT_HEADER_ID = herder.MERCHANT_HEADER_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .base_parameter_item_def base on item.PAYMENT_REASON = base.item_value and base.bptd_code = 'PAYMENT_REASON' ");
			sqlQueryBean.append(" WHERE ");
			//求償確認
			sqlQueryBean.append(" item.STATUS = :pay_confirm");
			sqlQueryBean.append(" AND item.IS_ASK_PAY = 'Y' ");
			//特店賠償、客戶賠償
			sqlQueryBean.append(" AND PAYMENT_TYPE IN (:paymentTypeList) ");
			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND info1.CUSTOMER_ID = :customerId ");
				sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
				LOGGER.debug(this.getClass().getName(), ".paymentInfoListToSyb() --> customerId: ", customerId);
			}
			sqlQueryBean.append(" ORDER BY info1.PAYMENT_ID, item.PAYMENT_ITEM, s.SUPPLIES_NAME, item.ITEM_NAME ");
			//設備
			sqlQueryBean.setParameter("payment_asset", IAtomsConstants.PAYMENT_ITEM_ASSET);
			//求償確認
			sqlQueryBean.setParameter("pay_confirm", IAtomsConstants.DATA_STATUS_CLAIM_CONFIRM);
			sqlQueryBean.setParameter("paymentTypeList", paymentTypeList);
			AliasBean aliasBean = new AliasBean(SrmPaymentInfoDTO.class);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.ROW_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.SUPPLIES_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.SUPPLIES_AMOUNT.getValue(), IntegerType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_REASON.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.TID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.REAL_FINISH_DATE.getValue(), DateType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.FEE.getValue(), BigDecimalType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.AO_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.LOCKED_DATE.getValue(), TimestampType.INSTANCE);
			LOGGER.debug("paymentInfoListToSyb()", "sql:", sqlQueryBean.toString());
			paymentIds = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("paymentInfoListToSyb() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return paymentIds;
	}

	@Override
	public List<SrmPaymentInfoDTO> paymentInfoListToSybNoTax(List<String> paymentStatusList, String customerId)
			throws DataAccessException {
		List<SrmPaymentInfoDTO> paymentIds = null;
		try {
			LOGGER.debug("paymentInfoListToSybNoTax()", "parameter:customerId=", customerId);
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("select ");
			sqlQueryBean.append(" ROW_NUMBER() OVER( ORDER BY info1.PAYMENT_ID, item.PAYMENT_ITEM, s.SUPPLIES_NAME, item.ITEM_NAME ) AS rowId , ");
			sqlQueryBean.append(" ( CASE WHEN item.PAYMENT_ITEM = :payment_asset ");
			sqlQueryBean.append(" THEN type.NAME ELSE s.SUPPLIES_NAME END ) AS suppliesName , ");
			sqlQueryBean.append(" item.SERIAL_NUMBER AS serialNumber, ");
			sqlQueryBean.append(" con.CONTRACT_CODE AS contractCode, ");
			sqlQueryBean.append(" item.SUPPLIES_AMOUNT AS suppliesAmount, ");
			sqlQueryBean.append(" (case when base.ITEM_VALUE='SELF_INPUT' THEN base.ITEM_NAME + '-' + item.REASON_DETAIL ELSE base.ITEM_NAME END) AS paymentReason, ");
			//sqlQueryBean.append(" base.ITEM_NAME AS paymentReason, ");
			sqlQueryBean.append(" info1.DTID AS dtid, ");
			sqlQueryBean.append(" info1.TID AS tid, ");
			sqlQueryBean.append(" mer.MERCHANT_CODE AS merchantCode, ");
			sqlQueryBean.append(" mer.NAME AS name, ");
			sqlQueryBean.append(" info1.REAL_FINISH_DATE AS realFinishDate, ");
			sqlQueryBean.append(" item.FEE AS fee, ");
			sqlQueryBean.append(" herder.AO_NAME AS aoName, ");
			sqlQueryBean.append(" item.LOCKED_DATE AS lockedDate ");
			sqlQueryBean.append(" FROM ").append(schema).append(" .SRM_PAYMENT_INFO info1 ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .SRM_PAYMENT_ITEM item  ON info1.PAYMENT_ID = item.PAYMENT_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE type ON item.ITEM_NAME = type.ASSET_TYPE_ID ")
			.append(" and item. PAYMENT_ITEM='PAYMENT_ASSET' ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .DMM_SUPPLIES s ON item.ITEM_NAME = s.SUPPLIES_ID ")
			.append(" and item. PAYMENT_ITEM= 'PAYMENT_SUPPLIES' ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT con ON item.CONTRACT_ID = con.CONTRACT_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer ON info1.MERCHANT_ID = mer.MERCHANT_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT_HEADER herder ON info1.MERCHANT_HEADER_ID = herder.MERCHANT_HEADER_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .base_parameter_item_def base on item.PAYMENT_REASON = base.item_value and base.bptd_code = 'PAYMENT_REASON' ");
			sqlQueryBean.append(" WHERE 1 = 1 ");
			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND info1.CUSTOMER_ID = :customerId ");
				sqlQueryBean.setParameter(SrmPaymentInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
				LOGGER.debug(this.getClass().getName(), ".paymentInfoListToSybNoTax() --> customerId: ", customerId);
			}
			sqlQueryBean.append(" AND item.STATUS IN ( :paymentStatusList ) ");
			sqlQueryBean.append(" ORDER BY info1.PAYMENT_ID, item.PAYMENT_ITEM, s.SUPPLIES_NAME, item.ITEM_NAME ");
			//設備
			sqlQueryBean.setParameter("payment_asset", IAtomsConstants.PAYMENT_ITEM_ASSET);
			//求償確認
			sqlQueryBean.setParameter("paymentStatusList", paymentStatusList);
			AliasBean aliasBean = new AliasBean(SrmPaymentInfoDTO.class);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.ROW_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.SUPPLIES_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.SUPPLIES_AMOUNT.getValue(), IntegerType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_REASON.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.TID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.REAL_FINISH_DATE.getValue(), DateType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.FEE.getValue(), BigDecimalType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.AO_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmPaymentInfoDTO.ATTRIBUTE.LOCKED_DATE.getValue(), TimestampType.INSTANCE);
			LOGGER.debug("paymentInfoListToSybNoTax()", "sql:", sqlQueryBean.toString());
			paymentIds = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("paymentInfoListToSybNoTax() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return paymentIds;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentInfoDAO#getPaymentCaseInfo(java.lang.String)
	 */
	@Override
	public SrmPaymentInfoDTO getPaymentCaseInfo(String paymentId) throws DataAccessException {
		List<SrmPaymentInfoDTO> dtos = null;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("caseInfo.CMS_CASE", SrmPaymentInfoDTO.ATTRIBUTE.CMS_CASE.getValue());
			sqlStatement.addSelectClause("caseInfo.CASE_CATEGORY", SrmPaymentInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue());
			sqlStatement.addSelectClause("caseInfo.CASE_ID", SrmPaymentInfoDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("caseInfo.DTID", SrmPaymentInfoDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("caseInfo.REPAIR_REASON", SrmPaymentInfoDTO.ATTRIBUTE.REPAIR_REASON.getValue());
			sqlStatement.addSelectClause("merchant.MERCHANT_CODE", SrmPaymentInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("caseInfo.MERCHANT_HEADER_ID", SrmPaymentInfoDTO.ATTRIBUTE.MERCHANT_HEADER_ID.getValue());
			sqlStatement.addSelectClause("caseInfo.DISPATCH_DATE", SrmPaymentInfoDTO.ATTRIBUTE.DISPATCH_DATE.getValue());
			sqlStatement.addSelectClause("caseInfo.IS_IATOMS_CREATE_CMS", SrmPaymentInfoDTO.ATTRIBUTE.IS_IATOMS_CREATE_CMS.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".SRM_PAYMENT_INFO payInfo ");
			fromBuffer.append("left join ").append(schema).append(".SRM_CASE_HANDLE_INFO caseInfo on caseInfo.CASE_ID = payInfo.CASE_ID ");
			fromBuffer.append("left join ").append(schema).append(".BIM_MERCHANT merchant on merchant.MERCHANT_ID = payInfo.MERCHANT_ID ");
			sqlStatement.addFromExpression(fromBuffer.toString());
			sqlStatement.addWhereClause("payInfo.PAYMENT_ID = :paymentId", paymentId);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmPaymentInfoDTO.class);
			dtos = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(dtos)) {
				return dtos.get(0);
			}
		} catch (Exception e) {
			LOGGER.error("getPaymentCaseInfo() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return null;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentInfoDAO#getByCaseCreate(java.util.List)
	 */
	@Override
	public List<SrmPaymentInfoDTO> getByCaseCreate(String caseId) throws DataAccessException {
		List<SrmPaymentInfoDTO> paymentInfoDTOs = null;
		try {
			LOGGER.debug("getByCaseCreate()", "parameter:caseId=", caseId);
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("paymentInfo.PAYMENT_ID", SrmPaymentInfoDTO.ATTRIBUTE.PAYMENT_ID.getValue());
			sqlStatement.addFromExpression(schema + ".SRM_PAYMENT_INFO paymentInfo");
			sqlStatement.addWhereClause("paymentInfo.CASE_ID = :caseId", caseId);
			sqlStatement.addWhereClause("paymentInfo.IS_CASE_CREATE = :isCaseCreate", IAtomsConstants.YES);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmPaymentInfoDTO.class);
			paymentInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("getByCaseCreate() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return paymentInfoDTOs;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmPaymentInfoDAO#deletePaymenInfo(java.util.List)
	 */
	@Override
	public void deletePaymenInfo(List<String> paymentIds) throws DataAccessException {
		try {
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("delete from ").append(schema).append(".SRM_PAYMENT_TRANSCATION where PAYMENT_ID in ( :paymentIds )");
			sqlQueryBean.append("delete from ").append(schema).append(".SRM_PAYMENT_ITEM where PAYMENT_ID in ( :paymentIds )");
			sqlQueryBean.append("delete from ").append(schema).append(".SRM_PAYMENT_INFO where PAYMENT_ID in ( :paymentIds )");
			sqlQueryBean.setParameter("paymentIds", paymentIds);
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
		} catch (Exception e) {
			LOGGER.error("deletePaymenInfo() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
}
