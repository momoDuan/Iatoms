package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.util.CollectionUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTransactionDTO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseTransactionDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseTransaction;
/**
 * Purpose: SRM_案件處理記錄DAO實現類
 * @author RiverJin
 * @since  JDK 1.7
 * @date   2016年11月10日
 * @MaintenancePersonnel RiverJin
 */
public class SrmCaseTransactionDAO extends GenericBaseDAO<SrmCaseTransaction> implements ISrmCaseTransactionDAO {

	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(SrmCaseTransactionDAO.class);
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseTransactionDAO#listByCaseId(java.lang.String)
	 */
	@Override
	public List<SrmCaseTransactionDTO> listByCaseId(String caseId, String isHistory, String sort, String order) throws DataAccessException {
		List<SrmCaseTransactionDTO> caseTransactionDTOS = null;
		try{
			LOGGER.debug(this.getClass().getName() + ".listByCaseId()", "parameters:caseId=" + caseId);
			LOGGER.debug(this.getClass().getName() + ".listByCaseId()", "parameters:isHistory=" + isHistory);
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("caseTransaction.TRANSACTION_ID", SrmCaseTransactionDTO.ATTRIBUTE.TRANSACTION_ID.getValue());
			sqlStatement.addSelectClause("caseTransaction.CASE_ID", SrmCaseTransactionDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("caseTransaction.ACTION_ID", SrmCaseTransactionDTO.ATTRIBUTE.ACTION_ID.getValue());
			sqlStatement.addSelectClause("caseAction.ITEM_NAME", SrmCaseTransactionDTO.ATTRIBUTE.ACTION_NAME.getValue());
			sqlStatement.addSelectClause("caseTransaction.CASE_STATUS", SrmCaseTransactionDTO.ATTRIBUTE.CASE_STATUS.getValue());
			sqlStatement.addSelectClause("caseStatus.ITEM_NAME", SrmCaseTransactionDTO.ATTRIBUTE.CASE_STATUS_NAME.getValue());
			sqlStatement.addSelectClause("caseStatus.ITEM_NAME", SrmCaseTransactionDTO.ATTRIBUTE.AFTER_ACTION_STATUS.getValue());
			sqlStatement.addSelectClause("caseTransaction.DEAL_BY_ID", SrmCaseTransactionDTO.ATTRIBUTE.DEAL_BY_ID.getValue());
			sqlStatement.addSelectClause("caseTransaction.DEAL_BY_NAME", SrmCaseTransactionDTO.ATTRIBUTE.DEAL_BY_NAME.getValue());
			sqlStatement.addSelectClause("caseTransaction.DEAL_DATE", SrmCaseTransactionDTO.ATTRIBUTE.DEAL_DATE.getValue());
			/*sqlStatement.addSelectClause("caseTransaction.DESCRIPTION", SrmCaseTransactionDTO.ATTRIBUTE.DESCRIPTION.getValue());
			sqlStatement.addSelectClause("caseTransaction.DESCRIPTION", SrmCaseTransactionDTO.ATTRIBUTE.DEAL_DESCRIPTION.getValue());*/
			sqlStatement.addSelectClause("(case when caseTransaction.ACTION_ID = :actionId then caseTransaction.UPDATE_ITEM else " +
					"caseTransaction.DESCRIPTION end)", SrmCaseTransactionDTO.ATTRIBUTE.DESCRIPTION.getValue());
			sqlStatement.addSelectClause("(case when caseTransaction.ACTION_ID = :actionId then caseTransaction.UPDATE_ITEM else " +
					"caseTransaction.DESCRIPTION end)", SrmCaseTransactionDTO.ATTRIBUTE.DEAL_DESCRIPTION.getValue());
			sqlStatement.addSelectClause("caseTransaction.CASE_STAGE", SrmCaseTransactionDTO.ATTRIBUTE.CASE_STAGE.getValue());
			sqlStatement.addSelectClause("caseTransaction.CASE_STAGE_NAME", SrmCaseTransactionDTO.ATTRIBUTE.CASE_STAGE_NAME.getValue());
			sqlStatement.addSelectClause("caseTransaction.NEXT_CASE_STAGE", SrmCaseTransactionDTO.ATTRIBUTE.NEXT_CASE_STAGE.getValue());
			sqlStatement.addSelectClause("caseTransaction.NEXT_CASE_STAGE_NAME", SrmCaseTransactionDTO.ATTRIBUTE.NEXT_CASE_STAGE_NAME.getValue());
			sqlStatement.addSelectClause("caseTransaction.DELAY_TIME", SrmCaseTransactionDTO.ATTRIBUTE.DELAY_TIME.getValue());
			sqlStatement.addSelectClause("caseTransaction.PROBLEM_REASON", SrmCaseTransactionDTO.ATTRIBUTE.PROBLEM_REASON.getValue());
			sqlStatement.addSelectClause("caseTransaction.PROBLEM_SOLUTION", SrmCaseTransactionDTO.ATTRIBUTE.PROBLEM_SOLUTION.getValue());
			sqlStatement.addSelectClause("caseTransaction.RESPONSIBITY", SrmCaseTransactionDTO.ATTRIBUTE.RESPONSIBITY.getValue());
			sqlStatement.addSelectClause("caseTransaction.CREATED_BY_ID", SrmCaseTransactionDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			sqlStatement.addSelectClause("caseTransaction.CREATED_BY_NAME", SrmCaseTransactionDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("caseTransaction.CREATED_DATE", SrmCaseTransactionDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sqlStatement.addSelectClause("caseTransaction.DEPT_CODE", SrmCaseTransactionDTO.ATTRIBUTE.DEPT_CODE.getValue());
			sqlStatement.addSelectClause("caseTransaction.MAIL_INFO", SrmCaseTransactionDTO.ATTRIBUTE.MAIL_INFO.getValue());
			sqlStatement.addSelectClause("logisticsDef.TEXT_FIELD1", SrmCaseTransactionDTO.ATTRIBUTE.LOGISTICS_VENDOR_EMAIL.getValue());
			sqlStatement.addSelectClause("logisticsDef.ITEM_NAME", SrmCaseTransactionDTO.ATTRIBUTE.LOGISTICS_VENDOR_NAME.getValue());
			sqlStatement.addSelectClause("caseTransaction.LOGISTICS_VENDOR", SrmCaseTransactionDTO.ATTRIBUTE.LOGISTICS_VENDOR.getValue());
			StringBuffer buffer = new StringBuffer();
			// 判斷標志位
			if(StringUtils.hasText(isHistory) && IAtomsConstants.YES.equals(isHistory)){
				buffer.append(schema).append(".SRM_HISTORY_CASE_TRANSACTION caseTransaction left join ");
			} else {
				buffer.append(schema).append(".SRM_CASE_TRANSACTION caseTransaction left join ");
			}
			buffer.append(schema).append(".BASE_PARAMETER_ITEM_DEF caseStatus on caseTransaction.CASE_STATUS = caseStatus.ITEM_VALUE ");
			buffer.append(" and caseStatus.BPTD_CODE='CASE_STATUS'  left join ");
			buffer.append(schema).append(".BASE_PARAMETER_ITEM_DEF caseAction on caseTransaction.ACTION_ID = caseAction.ITEM_VALUE ");
			buffer.append(" and caseAction.BPTD_CODE='CASE_ACTION' "); 
			buffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF logisticsDef on logisticsDef.BPTD_CODE = :logisticsVendor ");
			buffer.append(" and logisticsDef.ITEM_VALUE = caseTransaction.LOGISTICS_VENDOR "); 
			sqlStatement.addFromExpression(buffer.toString());
			if (StringUtils.hasText(caseId)) {
				sqlStatement.addWhereClause("caseTransaction.CASE_ID in(:caseId)");
			}
			// 查詢排序
			if(StringUtils.hasText(sort) && StringUtils.hasText(order)){
				sqlStatement.setOrderByExpression(sort + " " + order);
			} else {
				//Task #3146
				sqlStatement.setOrderByExpression("caseTransaction.CREATED_DATE desc");
			}
			//當標誌位為  A 的時候 是匯出ftps 全查
			if (!IAtomsConstants.STATUS_ACTIVE.equals(isHistory)) {
				// 系統管理員調整案件默認不查
				sqlStatement.addWhereClause("isnull(caseTransaction.CREATED_BY_ID, '') <>:userId", "1000000000-0001");
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("caseId", StringUtils.toList(caseId, IAtomsConstants.MARK_SEPARATOR));
			sqlQueryBean.setParameter("actionId", IAtomsConstants.ACTION_SAVE);
			sqlQueryBean.setParameter("logisticsVendor", IATOMS_PARAM_TYPE.LOGISTICS_VENDOR.getCode());
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseTransactionDTO.class);
			LOGGER.debug(this.getClass().getName() + ".listByCaseId()", "sql:" + sqlQueryBean.toString());
			caseTransactionDTOS = super.getDaoSupport().findByNativeSql(sqlQueryBean,aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+":listByCaseId() is error." + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return caseTransactionDTOS;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseTransactionDAO#listCaseRepairDetailReportToGP(java.lang.String, java.util.Date, java.util.Date, java.lang.String)
	 */
	@Override
	public List<SrmCaseTransactionDTO> listCaseRepairDetailReportToGP(
			String customerId, Date startDate, Date endDate, String caseCategory)
			throws DataAccessException {
		LOGGER.debug("listCaseRepairDetailReportToGP()", "parameter : customerId=", customerId);
		LOGGER.debug("listCaseRepairDetailReportToGP()", "parameter : startDate=", startDate.toString());
		LOGGER.debug("listCaseRepairDetailReportToGP()", "parameter : endDate=", endDate.toString());
		LOGGER.debug("listCaseRepairDetailReportToGP()", "parameter : caseCategory=", caseCategory);
		List<SrmCaseTransactionDTO> srmCaseTransactionList = null;
		try {
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append(
					"SELECT * FROM ( SELECT schi.REQUIREMENT_NO as requirementNo, schi.CASE_CATEGORY as caseCategory, bpid1.ITEM_NAME AS caseCategoryName, bpid2.ITEM_NAME AS caseStatus, bm.MERCHANT_CODE as merchantCode, bm.NAME AS merchantName, schi.DTID as dtid, schi.CREATED_DATE as createdDate, schi.DESCRIPTION AS description, CONVERT(VARCHAR(100), sct.DEAL_DATE, 120 ) as dealDate, sct.DESCRIPTION AS comment, sct.CASE_ID AS caseId FROM ");
			sqlQueryBean.append(schema).append(".SRM_CASE_TRANSACTION sct LEFT JOIN ").append(schema);
			sqlQueryBean.append(".SRM_CASE_HANDLE_INFO schi ON schi.CASE_ID = sct.CASE_ID LEFT JOIN ");
			sqlQueryBean.append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid1 ON bpid1.ITEM_VALUE = schi.CASE_CATEGORY AND bpid1.BPTD_CODE =:ticketType ");
			sqlQueryBean.append(" LEFT JOIN ");
			sqlQueryBean.append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid2 ON bpid2.ITEM_VALUE = schi.CASE_STATUS AND bpid2.BPTD_CODE = :caseStatus").append(" LEFT JOIN ");
			sqlQueryBean.append(".BIM_MERCHANT bm ON bm.COMPANY_ID = schi.CUSTOMER_ID AND bm.MERCHANT_ID = schi.MERCHANT_CODE LEFT JOIN ").append(schema);
			sqlQueryBean.append(".BIM_MERCHANT_HEADER mhi ON mhi.MERCHANT_HEADER_ID = schi.MERCHANT_HEADER_ID WHERE sct.DESCRIPTION IS NOT NULL AND sct.DESCRIPTION <>'' ");
			
			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND schi.CUSTOMER_ID = :customerId");
			}
			if((startDate != null) && (endDate != null)){
				sqlQueryBean.append(" AND CONVERT(varchar(12), schi.CREATED_DATE, 111) >= :startDate AND CONVERT(varchar(12), schi.CREATED_DATE, 111) < :endDate ");
			}
			if(StringUtils.hasText(caseCategory)){
				sqlQueryBean.append( "AND schi.CASE_CATEGORY = :caseCategory");
			}
			//Bug #2462 案件報修明細(環匯格式)報表 但不抓已作廢的 update by 2017/09/21
			sqlQueryBean.append( "AND schi.CASE_STATUS <> :status");	
			
			/*sqlQueryBean.append(
					" UNION ALL SELECT shchi.REQUIREMENT_NO as requirementNo, shchi.CASE_CATEGORY as caseCategory, bpid1.ITEM_NAME AS caseCategoryName, bpid2.ITEM_NAME  AS caseStatus, shchi.MERCHANT_CODE as merchantCode, bm.NAME as merchantName, shchi.DTID as dtid, shchi.CREATED_DATE as createdDate, shchi.DESCRIPTION AS description, CONVERT(VARCHAR(100), shct.DEAL_DATE, 120 ) as dealDate, shct.DESCRIPTION AS comment FROM ");
			sqlQueryBean.append(schema).append(".SRM_HISTORY_CASE_TRANSACTION shct LEFT JOIN ").append(schema).append(".SRM_HISTORY_CASE_HANDLE_INFO shchi ON shchi.CASE_ID = shct.CASE_ID LEFT JOIN ");
			sqlQueryBean.append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid1 ON bpid1.ITEM_VALUE = shchi.CASE_CATEGORY AND bpid1.BPTD_CODE = '").append(IATOMS_PARAM_TYPE.TICKET_TYPE.getCode()).append("' LEFT JOIN ");
			sqlQueryBean.append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid2 ON bpid2.ITEM_VALUE = shchi.CASE_STATUS AND bpid2.BPTD_CODE = '").append(IATOMS_PARAM_TYPE.CASE_STATUS.getCode()).append("' LEFT JOIN ");
			sqlQueryBean.append(schema).append(".BIM_MERCHANT bm ON bm.COMPANY_ID = shchi.CUSTOMER_ID AND bm.MERCHANT_CODE = shchi.MERCHANT_CODE LEFT JOIN ").append(schema);
			sqlQueryBean.append(".BIM_MERCHANT_HEADER mhi ON mhi.MERCHANT_HEADER_ID = shchi.MERCHANT_HEADER_ID WHERE shct.DESCRIPTION IS NOT NULL");

			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND shchi.CUSTOMER_ID = :customerId");
			}
			if((startDate != null) && (endDate != null)){
				sqlQueryBean.append(" AND CONVERT(varchar(12), shchi.CREATED_DATE, 111) >= :startDate AND CONVERT(varchar(12), shchi.CREATED_DATE, 111) < :endDate ");
			}
			if(StringUtils.hasText(caseCategory)){
				sqlQueryBean.append( "AND shchi.CASE_CATEGORY = :caseCategory");
			}*/
			
			sqlQueryBean.append(") t1 ");
			sqlQueryBean.setParameter("customerId", customerId);
			sqlQueryBean.setParameter("startDate", startDate);
			sqlQueryBean.setParameter("endDate", endDate);
			sqlQueryBean.setParameter("caseCategory", caseCategory);
			sqlQueryBean.setParameter("ticketType", IATOMS_PARAM_TYPE.TICKET_TYPE.getCode());
			sqlQueryBean.setParameter("caseStatus", IATOMS_PARAM_TYPE.CASE_STATUS.getCode());
			sqlQueryBean.setParameter("status", IAtomsConstants.CASE_STATUS.VOIDED.getCode());

			LOGGER.debug("listCaseRepairDetailReportToGP() ---> SQL = ", sqlQueryBean.toString());
			
			AliasBean aliasBean = new AliasBean(SrmCaseTransactionDTO.class);
			aliasBean.addScalar(SrmCaseTransactionDTO.ATTRIBUTE.REQUIREMENT_NO.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseTransactionDTO.ATTRIBUTE.CASE_CATEGORY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseTransactionDTO.ATTRIBUTE.CASE_CATEGORY_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseTransactionDTO.ATTRIBUTE.CASE_STATUS.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseTransactionDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseTransactionDTO.ATTRIBUTE.MERCHANT_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseTransactionDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseTransactionDTO.ATTRIBUTE.CREATED_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(SrmCaseTransactionDTO.ATTRIBUTE.DESCRIPTION.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseTransactionDTO.ATTRIBUTE.DEAL_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(SrmCaseTransactionDTO.ATTRIBUTE.COMMENT.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseTransactionDTO.ATTRIBUTE.CASE_ID.getValue(), StringType.INSTANCE);

			srmCaseTransactionList = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("SrmCaseTransactionDAO", "listCaseRepairDetailReportToGP() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return srmCaseTransactionList;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseTransactionDAO#listCaseAssetDetailReportToGP(java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<SrmCaseTransactionDTO> listCaseAssetDetailReportToGP(
			String customerId, Date startDate, Date endDate)
			throws DataAccessException {
		LOGGER.debug("listCaseAssetDetailReportToGP()", "parameter : customerId=", customerId);
		LOGGER.debug("listCaseAssetDetailReportToGP()", "parameter : startDate=", startDate.toString());
		LOGGER.debug("listCaseAssetDetailReportToGP()", "parameter : endDate=", endDate.toString());
		List<SrmCaseTransactionDTO> srmCaseTransactionDTOs = null;
		try {
			String schema = this.getMySchema();
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("SELECT * FROM ( SELECT schi.CASE_ID as caseId, bpid1.ITEM_NAME as caseCategory, schi.DTID as dtid, scal.SERIAL_NUMBER as serialNumber, dr.PROPERTY_ID as propertyId, dat.NAME as edcType, dr.IS_CUP as isCup,")
				.append(" schi.COMPLETE_DATE as completeDate, bm.NAME as merchantCode, (case when schi.CASE_CATEGORY ='INSTALL' or schi.CASE_CATEGORY ='UPDATE' THEN bpid2.ITEM_NAME + schi.INSTALLED_ADRESS ELSE bpid3.ITEM_NAME + schi.CONTACT_ADDRESS END) address, dat1.NAME AS itemId1, scal1.SERIAL_NUMBER AS")
				.append(" serialNumber1, dat2.NAME AS itemId2, scal2.SERIAL_NUMBER AS serialNumber2 FROM ");
			sqlQueryBean.append(schema).append(".SRM_CASE_HANDLE_INFO schi LEFT JOIN ").append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid1 ON bpid1.ITEM_VALUE = schi.CASE_CATEGORY AND bpid1.BPTD_CODE = :ticketType");
			sqlQueryBean.append(" LEFT JOIN ").append(schema);
			sqlQueryBean.append(".SRM_CASE_ASSET_LINK scal ON scal.CASE_ID = schi.CASE_ID AND scal.ITEM_TYPE = :edc");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(".DMM_REPOSITORY dr ON dr.SERIAL_NUMBER = scal.SERIAL_NUMBER LEFT JOIN ").append(schema);
			sqlQueryBean.append(".BIM_MERCHANT bm ON bm.COMPANY_ID = schi.CUSTOMER_ID AND bm.MERCHANT_ID = schi.MERCHANT_CODE LEFT JOIN ").append(schema);
			sqlQueryBean.append(".BASE_PARAMETER_ITEM_DEF bpid2 ON bpid2.ITEM_VALUE = schi.INSTALLED_ADRESS_LOCATION AND bpid2.BPTD_CODE = :location");
			//Bug #2463  案件設備明細(環匯格式)  裝機地址改成 裝機\聯繫地址   裝機、異動=裝機地址   其他=聯繫地址
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid3 ON bpid3.ITEM_VALUE = schi.CONTACT_ADDRESS_LOCATION AND bpid3.BPTD_CODE = :location");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(".SRM_CASE_ASSET_LINK scal1 ON scal1.CASE_ID = schi.CASE_ID AND scal1.ITEM_TYPE = :assetLink1  and scal1.IS_LINK='Y'");
			sqlQueryBean.append("left join ").append(schema).append(".DMM_ASSET_TYPE dat1 on dat1.ASSET_TYPE_ID = scal1.ITEM_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(".SRM_CASE_ASSET_LINK scal2 ON scal2.CASE_ID = schi.CASE_ID AND scal2.ITEM_TYPE = :assetLink2  and scal2.IS_LINK='Y'").append(" left join ").append(schema).append(".DMM_ASSET_TYPE dat2 on dat2.ASSET_TYPE_ID = scal2.ITEM_ID ")
			//Bug #2463  案件設備明細(環匯格式) 2. 設備類別是指 刷卡機機型(名稱)	 將設備類別 改為設備名稱 update by 2017/09/21
			.append("LEFT JOIN ").append(schema).append(".DMM_ASSET_TYPE dat on dat.ASSET_TYPE_ID = schi.EDC_TYPE").append(" WHERE 1=1");
			
			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND schi.CUSTOMER_ID = :customerId");
				sqlQueryBean.setParameter("customerId", customerId);
			}
			StringBuffer fromBuffer = null;
			//Bug #2463 案件設備明細(環匯格式)立即結案：實際執行時間(結案)線上排除：實際執行時間(線上排除)完修：實際執行時間(完修) update by 2017/09/21
			if((startDate != null) && (endDate != null)){
				fromBuffer = new StringBuffer();
				fromBuffer.append("AND schi.CASE_ID in ( ");
				fromBuffer.append("select trans.CASE_ID from ");
				fromBuffer.append(schema).append(".SRM_CASE_TRANSACTION trans where ");
				fromBuffer.append("trans.ACTION_ID in ('complete', 'onlineExclusion', 'immediatelyClosing') ");
				sqlQueryBean.append(fromBuffer.toString());
				sqlQueryBean.append("AND CONVERT(varchar(12), trans.CREATED_DATE, 111) >= :startDate");
				sqlQueryBean.append("AND CONVERT(varchar(12), trans.CREATED_DATE, 111) < :endDate) ");
				/*sqlQueryBean.setParameter("startDate", startDate);
				sqlQueryBean.setParameter("endDate", endDate);*/
				sqlQueryBean.setParameter("startDate", DateTimeUtils.toString(startDate, DateTimeUtils.DT_FMT_YYYYMMDD_SLASH));
				sqlQueryBean.setParameter("endDate", DateTimeUtils.toString(endDate, DateTimeUtils.DT_FMT_YYYYMMDD_SLASH));
			}
			sqlQueryBean.append("AND schi.CASE_STATUS IN ('" + IAtomsConstants.CASE_STATUS.WAIT_CLOSE.getCode() + "','" + IAtomsConstants.CASE_STATUS.COMPLETED.getCode() + "','" + IAtomsConstants.CASE_STATUS.CLOSED.getCode() + "','" + IAtomsConstants.CASE_STATUS.IMMEDIATE_CLOSE.getCode() + "')");
			//Bug #2280 update by 2017/09/01
			sqlQueryBean.append("and scal.IS_LINK not IN (CASE when schi.CASE_CATEGORY <>'UNINSTALL' then 'D' else '' end) ");
			/*sqlQueryBean.append(" UNION ALL SELECT shchi.CASE_ID as caseId, bpid1.ITEM_NAME as caseCategory, shchi.DTID as dtid, shcal.SERIAL_NUMBER as serialNumber, dr.PROPERTY_ID as propertyId, shcal.ITEM_CATEGORY as edcType, dr.IS_CUP as isCup, shchi.COMPLETE_DATE as completeDate, bm.NAME as merchantCode,")
				.append("bpid2.ITEM_NAME + shchi.INSTALLED_ADRESS as address, dath1.NAME AS itemId1, shcal1.SERIAL_NUMBER AS serialNumber1, dath2.NAME AS itemId2, shcal2.SERIAL_NUMBER AS serialNumber2 FROM ");
			sqlQueryBean.append(schema).append(".SRM_HISTORY_CASE_HANDLE_INFO shchi LEFT JOIN ").append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid1 ON bpid1.ITEM_VALUE = shchi.CASE_CATEGORY AND bpid1.BPTD_CODE = :ticketType");
			sqlQueryBean.append(" LEFT JOIN ").append(schema);
			sqlQueryBean.append(".SRM_HISTORY_CASE_ASSET_LINK shcal ON shcal.CASE_ID = shchi.CASE_ID AND shcal.ITEM_TYPE = :edc");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(".DMM_REPOSITORY dr ON dr.SERIAL_NUMBER = shcal.SERIAL_NUMBER LEFT JOIN ").append(schema);
			sqlQueryBean.append(".BIM_MERCHANT bm ON bm.COMPANY_ID = shchi.CUSTOMER_ID AND bm.MERCHANT_ID = shchi.MERCHANT_CODE LEFT JOIN ").append(schema);
			sqlQueryBean.append(".BASE_PARAMETER_ITEM_DEF bpid2 ON bpid2.ITEM_VALUE = shchi.INSTALLED_ADRESS_LOCATION AND bpid2.BPTD_CODE = :location");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(".SRM_HISTORY_CASE_ASSET_LINK shcal1 ON shcal1.CASE_ID = shchi.CASE_ID AND shcal1.ITEM_TYPE = :assetLink1").append("left join ").append(schema).append(".DMM_ASSET_TYPE dath1 on dath1.ASSET_TYPE_ID = shcal1.ITEM_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(".SRM_HISTORY_CASE_ASSET_LINK shcal2 ON shcal2.CASE_ID = shchi.CASE_ID AND shcal2.ITEM_TYPE = :assetLink2").append("left join ").append(schema).append(".DMM_ASSET_TYPE dath2 on dath2.ASSET_TYPE_ID = shcal2.ITEM_ID ").append(" WHERE 1=1");
			sqlQueryBean.setParameter("ticketType", IATOMS_PARAM_TYPE.TICKET_TYPE.getCode());
			sqlQueryBean.setParameter("edc", IAtomsConstants.CASE_ASSET_LINK_ITEM_TYPE_EDC);
			sqlQueryBean.setParameter("location", IATOMS_PARAM_TYPE.LOCATION.getCode());
			sqlQueryBean.setParameter("assetLink1", IAtomsConstants.CASE_ASSET_LINK_ITEM_TYPE_ONE);
			sqlQueryBean.setParameter("assetLink2", IAtomsConstants.CASE_ASSET_LINK_ITEM_TYPE_TWO);
			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND shchi.CUSTOMER_ID = :customerId");
				sqlQueryBean.setParameter("customerId", customerId);
			}
			if((startDate != null) && (endDate != null)){
				sqlQueryBean.append("AND CONVERT(varchar(12), shchi.COMPLETE_DATE, 111) >= :startDate");
				sqlQueryBean.append("AND CONVERT(varchar(12), shchi.COMPLETE_DATE, 111) < :endDate");
				sqlQueryBean.setParameter("startDate", startDate);
				sqlQueryBean.setParameter("endDate", endDate);
				sqlQueryBean.setParameter("startDate", DateTimeUtils.toString(startDate, DateTimeUtils.DT_FMT_YYYYMMDD_SLASH));
				sqlQueryBean.setParameter("endDate", DateTimeUtils.toString(endDate, DateTimeUtils.DT_FMT_YYYYMMDD_SLASH));
			}
			sqlQueryBean.append("AND shchi.CASE_STATUS IN ('" + IAtomsConstants.CASE_STATUS.CLOSED.getCode() + "','" + IAtomsConstants.CASE_STATUS.COMPLETED.getCode() + "')");
*/			sqlQueryBean.append(") t1 order by t1.completeDate;");
			sqlQueryBean.setParameter("ticketType", IATOMS_PARAM_TYPE.TICKET_TYPE.getCode());
			sqlQueryBean.setParameter("edc", IAtomsConstants.CASE_ASSET_LINK_ITEM_TYPE_EDC);
			sqlQueryBean.setParameter("location", IATOMS_PARAM_TYPE.LOCATION.getCode());
			sqlQueryBean.setParameter("assetLink1", IAtomsConstants.CASE_ASSET_LINK_ITEM_TYPE_ONE);
			sqlQueryBean.setParameter("assetLink2", IAtomsConstants.CASE_ASSET_LINK_ITEM_TYPE_TWO);
			//sqlQueryBean.setParameter("remove", IAtomsConstants.CASE_DELETE_ASSET);

			LOGGER.debug("listCaseAssetDetailReportToGP():", "SQL ---> " + sqlQueryBean.toString());
			
			AliasBean aliasBean = new AliasBean(SrmCaseTransactionDTO.class);
			aliasBean.addScalar(SrmCaseTransactionDTO.ATTRIBUTE.CASE_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseTransactionDTO.ATTRIBUTE.CASE_CATEGORY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseTransactionDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseTransactionDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseTransactionDTO.ATTRIBUTE.PROPERTY_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseTransactionDTO.ATTRIBUTE.EDC_TYPE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseTransactionDTO.ATTRIBUTE.IS_CUP.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseTransactionDTO.ATTRIBUTE.COMPLETE_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(SrmCaseTransactionDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseTransactionDTO.ATTRIBUTE.ADDRESS.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseTransactionDTO.ATTRIBUTE.ITEM_ID_ONE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseTransactionDTO.ATTRIBUTE.SERIAL_NUMBER_ONE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseTransactionDTO.ATTRIBUTE.ITEM_ID_TWO.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseTransactionDTO.ATTRIBUTE.SERIAL_NUMBER_TWO.getValue(), StringType.INSTANCE);
			
			srmCaseTransactionDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("SrmCaseTransactionDAO", "listCaseAssetDetailReportToGP() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return srmCaseTransactionDTOs;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseTransactionDAO#listAfterQAs(java.lang.String)
	 */
	@Override
	public List<SrmCaseTransactionDTO> listAfterQAs(String caseId) throws DataAccessException {
		LOGGER.debug("listAfterQAs()", "parameter : caseId=", caseId);
		List<SrmCaseTransactionDTO> srmCaseTransactionDTOs = null;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("CASE_ID");
			StringBuffer formBuffer = new StringBuffer();
			formBuffer.append(schema);
			formBuffer.append(".SRM_CASE_TRANSACTION");
			sqlStatement.addFromExpression(formBuffer.toString());
			if (StringUtils.hasText(caseId)) {
				sqlStatement.addWhereClause("CASE_ID = :caseId", caseId);
			}
			sqlStatement.addWhereClause("DEPT_CODE = :deptId", IAtomsConstants.CASE_ROLE.QA.getCode());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			srmCaseTransactionDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean);
		} catch (Exception e) {
			LOGGER.error("listAfterQAs", "listAfterQAs() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return srmCaseTransactionDTOs;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseTransactionDAO#listNewCompleteDate(java.lang.String)
	 */
	@Override
	public SrmCaseTransactionDTO listNewCompleteDate(String field, String caseId, String orderBy) throws DataAccessException {
		LOGGER.debug("listNewCompleteDate()", "parameter : field=", field);
		SrmCaseTransactionDTO srmCaseTransactionDTO = null;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("TRANSACTION_ID", SrmCaseTransactionDTO.ATTRIBUTE.TRANSACTION_ID.getValue());
			sqlStatement.addFromExpression(schema + ".SRM_CASE_TRANSACTION");
			if (StringUtils.hasText(field) && IAtomsConstants.CASE_STATUS.COMPLETED.getCode().equals(field)) {
				StringBuffer buffer = new StringBuffer();
				buffer.append("((ACTION_ID = :actionId AND CASE_STATUS = :caseStatus)");
				buffer.append("or (ACTION_ID = 'foms_complete' AND CASE_STATUS = 'foms_EdcStatusCompletion'))");
				sqlStatement.addWhereClause(buffer.toString());
				/*sqlStatement.addWhereClause("ACTION_ID = :actionId", IAtomsConstants.CASE_ACTION.COMPLETE.getCode());
				sqlStatement.addWhereClause("CASE_STATUS = :caseStatus", IAtomsConstants.CASE_STATUS.COMPLETED.getCode());*/
			} else if (StringUtils.hasText(field) && IAtomsConstants.ACTION_SAVE.equals(field)) {
				sqlStatement.addWhereClause("ACTION_ID = :actionId", IAtomsConstants.ACTION_SAVE);
				/*sqlStatement.addWhereClause("CASE_STATUS = :caseStatus", IAtomsConstants.CASE_STATUS.COMPLETED.getCode());*/
			}
			if (StringUtils.hasText(caseId)) {
				sqlStatement.addWhereClause("CASE_ID = :caseId", caseId);
			}
			if (StringUtils.hasText(orderBy)) {
				sqlStatement.setOrderByExpression("CREATED_DATE " + orderBy);
			} else {
				sqlStatement.setOrderByExpression("CREATED_DATE");
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if (StringUtils.hasText(field) && IAtomsConstants.CASE_STATUS.COMPLETED.getCode().equals(field)) {
				sqlQueryBean.setParameter("actionId", IAtomsConstants.CASE_ACTION.COMPLETE.getCode());
				sqlQueryBean.setParameter("caseStatus", IAtomsConstants.CASE_STATUS.COMPLETED.getCode());
			}
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseTransactionDTO.class);
			List<SrmCaseTransactionDTO> srmCaseTransactionDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(srmCaseTransactionDTOs)) {
				srmCaseTransactionDTO = srmCaseTransactionDTOs.get(0);
			}
		} catch (Exception e) {
			LOGGER.error("listNewCompleteDate", "listNewCompleteDate() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return srmCaseTransactionDTO;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseTransactionDAO#listNewCompleteDate(java.lang.String)
	 */
	@Override
	public SrmCaseTransactionDTO getLogisticsVendor(String transactionId) throws DataAccessException {
		try{
			/*LOGGER.debug(this.getClass().getName() + ".listByCaseId()", "parameters:caseId=" + caseId);
			LOGGER.debug(this.getClass().getName() + ".listByCaseId()", "parameters:isHistory=" + isHistory);*/
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("caseTransaction.TRANSACTION_ID", SrmCaseTransactionDTO.ATTRIBUTE.TRANSACTION_ID.getValue());
			sqlStatement.addSelectClause("logisticsDef.TEXT_FIELD1", SrmCaseTransactionDTO.ATTRIBUTE.LOGISTICS_VENDOR_EMAIL.getValue());
			StringBuffer buffer = new StringBuffer();
			// 判斷標志位
			/*if(StringUtils.hasText(isHistory) && IAtomsConstants.YES.equals(isHistory)){
				buffer.append(schema).append(".SRM_HISTORY_CASE_TRANSACTION caseTransaction left join ");
			} else {
				buffer.append(schema).append(".SRM_CASE_TRANSACTION caseTransaction left join ");
			}*/
			buffer.append(schema).append(".SRM_CASE_TRANSACTION caseTransaction left join ");
			buffer.append(schema).append(".BASE_PARAMETER_ITEM_DEF logisticsDef on logisticsDef.BPTD_CODE = :logisticsVendor ");
			buffer.append(" and logisticsDef.ITEM_VALUE = caseTransaction.LOGISTICS_VENDOR "); 
			sqlStatement.addFromExpression(buffer.toString());
			//當標誌位為  A 的時候 是匯出ftps 全查
			/*if (!IAtomsConstants.STATUS_ACTIVE.equals(isHistory)) {
				// 系統管理員調整案件默認不查
				sqlStatement.addWhereClause("isnull(caseTransaction.CREATED_BY_ID, '') <>:userId", "1000000000-0001");
			}*/
			if (StringUtils.hasText(transactionId)) {
				sqlStatement.addWhereClause("caseTransaction.TRANSACTION_ID = :transactionId", transactionId);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			/*sqlQueryBean.setParameter("caseId", StringUtils.toList(caseId, IAtomsConstants.MARK_SEPARATOR));*/
			/*sqlQueryBean.setParameter("actionId", IAtomsConstants.ACTION_SAVE);*/
			sqlQueryBean.setParameter("logisticsVendor", IATOMS_PARAM_TYPE.LOGISTICS_VENDOR.getCode());
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseTransactionDTO.class);
			LOGGER.debug(this.getClass().getName() + ".listByCaseId()", "sql:" + sqlQueryBean.toString());
			List<SrmCaseTransactionDTO> caseTransactionDTOS = super.getDaoSupport().findByNativeSql(sqlQueryBean,aliasBean);
			if (!CollectionUtils.isEmpty(caseTransactionDTOS)) {
				return caseTransactionDTOS.get(0);
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+":getLogisticsVendor() is error." + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return null;
	}
}
