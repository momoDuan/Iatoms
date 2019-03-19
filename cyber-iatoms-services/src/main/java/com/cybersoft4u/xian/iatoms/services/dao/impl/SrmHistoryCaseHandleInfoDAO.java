package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

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

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmHistoryCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmHistoryCaseHandleInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmHistoryCaseHandleInfo;
/**
 * Purpose: 案件歷史處理資料檔DAO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/2/9
 * @MaintenancePersonnel CarrieDuan
 */
public class SrmHistoryCaseHandleInfoDAO extends GenericBaseDAO<SrmHistoryCaseHandleInfo> implements
		ISrmHistoryCaseHandleInfoDAO {

	/**
	 * 日誌記錄物件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(SrmHistoryCaseHandleInfoDAO.class);
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmHistoryCaseHandleInfoDAO#listBy(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	public List<SrmHistoryCaseHandleInfoDTO> listBy(String historyId, String companyId, String merchantCode, String dtid, String tid, String serialNumber,
			String caseId, String sort, String order, Integer pageSize, Integer pageIndex) throws DataAccessException {
		LOGGER.debug("listBy()", "historyId:", historyId);
		LOGGER.debug("listBy()", "companyId:", companyId);
		LOGGER.debug("listBy()", "merchantCode:", merchantCode);
		LOGGER.debug("listBy()", "dtid:", dtid);
		LOGGER.debug("listBy()", "tid:", tid);
		LOGGER.debug("listBy()", "serialNumber:", serialNumber);
		LOGGER.debug("listBy()", "caseId:", caseId);
		LOGGER.debug("listBy()", "sort:", sort);
		LOGGER.debug("listBy()", "pageSize:", pageSize.intValue());
		LOGGER.debug("listBy()", "order:", order);
		LOGGER.debug("listBy()", "pageIndex:", pageIndex.intValue());
		List<SrmHistoryCaseHandleInfoDTO> srmHistoryCaseHandleInfoDTOs = null;
		String schema = this.getMySchema();
		SqlStatement sqlStatement = new SqlStatement();
		try {
			sqlStatement.addSelectClause("caseInfo.CUSTOMER_ID", SrmHistoryCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue());
			sqlStatement.addSelectClause("caseInfo.DTID", SrmHistoryCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("STUFF((SELECT ','+ ltrim(trans.TID) FROM " + schema + ".SRM_CASE_HANDLE_INFO info,"+ schema +".SRM_CASE_TRANSACTION_PARAMETER trans"
						.concat(" WHERE trans.CASE_ID=info.CASE_ID FOR XML PATH('')), 1, 1, '')"), SrmHistoryCaseHandleInfoDTO.ATTRIBUTE.TID.getValue());
			
			//sqlStatement.addSelectClause("trans.TID", SrmHistoryCaseHandleInfoDTO.ATTRIBUTE.TID.getValue());
			sqlStatement.addSelectClause("caseInfo.REQUIREMENT_NO", SrmHistoryCaseHandleInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue());
			sqlStatement.addSelectClause("caseInfo.MERCHANT_CODE", SrmHistoryCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_ID.getValue());
			sqlStatement.addSelectClause("merchant.MERCHANT_CODE", SrmHistoryCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("merchant.NAME", SrmHistoryCaseHandleInfoDTO.ATTRIBUTE.NAME.getValue());
			sqlStatement.addSelectClause("merchant.MERCHANT_ID", SrmHistoryCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_ID.getValue());
			sqlStatement.addSelectClause("caseInfo.CASE_ID", SrmHistoryCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("caseInfo.ACCEPTABLE_FINISH_DATE", SrmHistoryCaseHandleInfoDTO.ATTRIBUTE.REAL_FINISH_DATE.getValue());
			sqlStatement.addSelectClause("caseInfo.CREATED_DATE", SrmHistoryCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sqlStatement.addSelectClause("type.NAME", SrmHistoryCaseHandleInfoDTO.ATTRIBUTE.ASSET_NAME.getValue());
			sqlStatement.addSelectClause("assetLink.ITEM_ID", SrmHistoryCaseHandleInfoDTO.ATTRIBUTE.ASSET_ID.getValue());
			sqlStatement.addSelectClause("assetLink.SERIAL_NUMBER", SrmHistoryCaseHandleInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("repository.CONTRACT_ID", SrmHistoryCaseHandleInfoDTO.ATTRIBUTE.CONTRACT_ID.getValue());
			sqlStatement.addSelectClause("contract.CONTRACT_CODE", SrmHistoryCaseHandleInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue());
			sqlStatement.addSelectClause("merHeader.AO_NAME", SrmHistoryCaseHandleInfoDTO.ATTRIBUTE.AO_NAME.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".SRM_CASE_HANDLE_INFO caseInfo");
			fromBuffer.append(" left join ").append(schema).append(".SRM_CASE_ASSET_LINK assetLink on caseInfo.CASE_ID = assetLink.CASE_ID");
			//fromBuffer.append(" left join ").append(schema).append(".SRM_CASE_TRANSACTION_PARAMETER trans on trans.CASE_ID = caseInfo.CASE_ID");
			fromBuffer.append(" left join ").append(schema).append(".DMM_ASSET_TYPE type on type.ASSET_TYPE_ID = assetLink.ITEM_ID");
			fromBuffer.append(" left join ").append(schema).append(".DMM_REPOSITORY repository on repository.SERIAL_NUMBER = assetLink.SERIAL_NUMBER");
			fromBuffer.append(" left join ").append(schema).append(".BIM_CONTRACT contract on repository.CONTRACT_ID = contract.CONTRACT_ID");
			fromBuffer.append(" left join ").append(schema).append(".BIM_MERCHANT merchant on merchant.MERCHANT_ID = caseInfo.MERCHANT_CODE");
			fromBuffer.append(" left join ").append(schema).append(".BIM_MERCHANT_HEADER merHeader on merHeader.MERCHANT_HEADER_ID = caseInfo.MERCHANT_HEADER_ID");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(historyId)) {
				sqlStatement.addWhereClause("caseInfo.HISTORY_ID = :historyId", historyId);
			}
			if (StringUtils.hasText(companyId)) {
				sqlStatement.addWhereClause("caseInfo.CUSTOMER_ID = :companyId", companyId);
			}
			if (StringUtils.hasText(merchantCode)) {
				sqlStatement.addWhereClause("merchant.MERCHANT_CODE = :merchantCode", merchantCode);
			}
			if (StringUtils.hasText(dtid)) {
				sqlStatement.addWhereClause("caseInfo.DTID = :dtid", dtid);
			}
			if (StringUtils.hasText(tid)) {
				sqlStatement.addWhereClause("trans.TID = :tid", tid);
			}
			if (StringUtils.hasText(caseId)) {
				sqlStatement.addWhereClause("caseInfo.CASE_ID = :caseId", caseId);
			}
			if (StringUtils.hasText(serialNumber)) {
				sqlStatement.addWhereClause("repository.SERIAL_NUMBER = :serialNumber", serialNumber);
			}
			sqlStatement.addWhereClause("(assetLink.ACTION = :remove or assetLink.ACTION = :loss)");
			sqlStatement.addWhereClause("(caseInfo.CASE_STATUS = :closed or caseInfo.CASE_STATUS = :immediateclose)");
			sqlStatement.addWhereClause("(trans.TRANSACTION_TYPE in ( :transactionType ))");
			//分頁
			sqlStatement.setPageSize(pageSize.intValue());
			sqlStatement.setStartPage(pageIndex.intValue() - 1);
			if (StringUtils.hasText(sort) && StringUtils.hasText(order)) {
				sqlStatement.setOrderByExpression(sort + ',' + "caseInfo.CASE_ID" + IAtomsConstants.MARK_SPACE + order);
			}
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmHistoryCaseHandleInfoDTO.class);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("remove", IAtomsConstants.ACTION_REMOVE);
			sqlQueryBean.setParameter("loss", IAtomsConstants.ACTION_LOSS);
			sqlQueryBean.setParameter("closed", IAtomsConstants.CASE_STATUS.CLOSED.getCode());
			sqlQueryBean.setParameter("immediateclose", IAtomsConstants.CASE_STATUS.IMMEDIATE_CLOSE.getCode());
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(IAtomsConstants.MARK_QUOTES).append(IAtomsConstants.TRANSACTION_CATEGORY.COMMON_VM.getCode()).append(IAtomsConstants.MARK_QUOTES).append(IAtomsConstants.MARK_SEPARATOR);
			stringBuffer.append(IAtomsConstants.MARK_QUOTES).append(IAtomsConstants.TRANSACTION_CATEGORY.COMMON_VMJ.getCode()).append(IAtomsConstants.MARK_QUOTES).append(IAtomsConstants.MARK_SEPARATOR);
			stringBuffer.append(IAtomsConstants.MARK_QUOTES).append(IAtomsConstants.TRANSACTION_CATEGORY.COMMON_VMJU.getCode()).append(IAtomsConstants.MARK_QUOTES);
			//sqlQueryBean.setParameter(SrmHistoryCaseHandleInfoDTO.ATTRIBUTE.TRANSACTION_TYPE.getValue(), stringBuffer.toString());
			//打印SQL語句
			LOGGER.debug("listBy()", "SQL:", sqlQueryBean.toString());
			srmHistoryCaseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.debug("listBy()", "error:", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return srmHistoryCaseHandleInfoDTOs;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmHistoryCaseHandleInfoDAO#getCount(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public Integer getCount(String historyId, String companyId, String merchantCode, String dtid,
			String tid, String serialNumber, String caseId) throws DataAccessException {
		LOGGER.debug("listBy()", "historyId:", historyId);
		LOGGER.debug("listBy()", "companyId:", companyId);
		LOGGER.debug("listBy()", "merchantCode:", merchantCode);
		LOGGER.debug("listBy()", "dtid:", dtid);
		LOGGER.debug("listBy()", "tid:", tid);
		LOGGER.debug("listBy()", "serialNumber:", serialNumber);
		LOGGER.debug("listBy()", "caseId:", caseId);
		Integer count = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".SRM_CASE_HANDLE_INFO caseInfo");
			fromBuffer.append(" left join ").append(schema).append(".SRM_CASE_ASSET_LINK assetLink on caseInfo.CASE_ID = assetLink.CASE_ID");
			fromBuffer.append(" left join ").append(schema).append(".SRM_CASE_TRANSACTION_PARAMETER trans on trans.CASE_ID = caseInfo.CASE_ID and trans.TRANSACTION_TYPE = :transactionType");
			fromBuffer.append(" left join ").append(schema).append(".DMM_ASSET_TYPE type on type.ASSET_TYPE_ID = assetLink.ITEM_ID");
			fromBuffer.append(" left join ").append(schema).append(".DMM_REPOSITORY repository on repository.SERIAL_NUMBER = assetLink.SERIAL_NUMBER");
			fromBuffer.append(" left join ").append(schema).append(".BIM_CONTRACT contract on repository.CONTRACT_ID = contract.CONTRACT_ID");
			fromBuffer.append(" left join ").append(schema).append(".BIM_MERCHANT merchant on merchant.MERCHANT_ID = caseInfo.MERCHANT_CODE");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(historyId)) {
				sqlStatement.addWhereClause("caseInfo.HISTORY_ID = :historyId", historyId);
			}
			if (StringUtils.hasText(companyId)) {
				sqlStatement.addWhereClause("caseInfo.CUSTOMER_ID = :companyId", companyId);
			}
			if (StringUtils.hasText(merchantCode)) {
				sqlStatement.addWhereClause("merchant.MERCHANT_CODE = :merchantCode", merchantCode);
			}
			if (StringUtils.hasText(dtid)) {
				sqlStatement.addWhereClause("caseInfo.DTID = :dtid", dtid);
			}
			if (StringUtils.hasText(tid)) {
				sqlStatement.addWhereClause("trans.TID = :tid", tid);
			}
			if (StringUtils.hasText(caseId)) {
				sqlStatement.addWhereClause("caseInfo.CASE_ID = :caseId", caseId);
			}
			if (StringUtils.hasText(serialNumber)) {
				sqlStatement.addWhereClause("repository.SERIAL_NUMBER = :serialNumber", serialNumber);
			}
			sqlStatement.addWhereClause("(assetLink.ACTION = :remove or assetLink.ACTION = :loss)");
			sqlStatement.addWhereClause("(caseInfo.CASE_STATUS = :closed or caseInfo.CASE_STATUS = :immediateclose)");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("remove", IAtomsConstants.ACTION_REMOVE);
			sqlQueryBean.setParameter("loss", IAtomsConstants.ACTION_LOSS);
			sqlQueryBean.setParameter("closed", IAtomsConstants.CASE_STATUS.CLOSED.getCode());
			sqlQueryBean.setParameter("immediateclose", IAtomsConstants.CASE_STATUS.IMMEDIATE_CLOSE.getCode());
			sqlQueryBean.setParameter(SrmHistoryCaseHandleInfoDTO.ATTRIBUTE.TRANSACTION_TYPE.getValue(), IAtomsConstants.TRANSACTION_CATEGORY.COMMON.getCode());
			//打印SQL語句
			LOGGER.debug("listBy()", "SQL:", sqlQueryBean.toString());
			List<Integer> list = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(list)) {
				return list.get(0);
			}
		} catch (Exception e) {
			LOGGER.debug("getCount()", "error:", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return count;
	}

}
