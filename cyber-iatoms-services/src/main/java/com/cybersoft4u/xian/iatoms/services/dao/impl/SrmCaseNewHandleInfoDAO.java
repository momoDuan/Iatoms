package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cafe.core.bean.CoreMessageCode;
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
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseNewHandleInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseNewHandleInfo;
/**
 * Purpose: SRM_案件處理最新資料檔DAO實現類
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2016/12/19
 * @MaintenancePersonnel CarrieDuan
 */
public class SrmCaseNewHandleInfoDAO extends GenericBaseDAO<SrmCaseNewHandleInfo> implements ISrmCaseNewHandleInfoDAO{
	/**
	 * 日誌記錄物件
	 */
	private static final CafeLog logger = (CafeLog) CafeLogFactory.getLog(SrmCaseNewHandleInfoDAO.class);
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#listDtidBy(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	public List<SrmCaseHandleInfoDTO> listDtidBy(String customerId, String merchantCode, String tId, String merchantName,String edcNumber, 
			String headerName, String dtid, String sort, String order, Integer currentPage, Integer pageSize) throws DataAccessException {
		List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = null;
		try {
			logger.debug(this.getClass().getName() + ".listDtidBy()", "parameters:customerId=" + customerId);
			logger.debug(this.getClass().getName() + ".listDtidBy()", "parameters:merchantCode=" + merchantCode);
			logger.debug(this.getClass().getName() + ".listDtidBy()", "parameters:tId=" + tId);
			logger.debug(this.getClass().getName() + ".listDtidBy()", "parameters:merchantName=" + merchantName);
			logger.debug(this.getClass().getName() + ".listDtidBy()", "parameters:edcNumber=" + edcNumber);
			logger.debug(this.getClass().getName() + ".listDtidBy()", "parameters:headerName=" + headerName);
			logger.debug(this.getClass().getName() + ".listDtidBy()", "parameters:dtid=" + dtid);
			logger.debug(this.getClass().getName() + ".listDtidBy()", "parameters:sort=" + sort);
			logger.debug(this.getClass().getName() + ".listDtidBy()", "parameters:order=" + order);
			logger.debug(this.getClass().getName() + ".listDtidBy()", "parameters:currentPage=" + currentPage);
			logger.debug(this.getClass().getName() + ".listDtidBy()", "parameters:pageSize=" + pageSize);
			StringBuilder builder = new StringBuilder();
			SqlStatement sql = new SqlStatement();
			sql.addSelectClause("caseInfoData.caseId", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue());
			sql.addSelectClause("caseInfoData.dtid", SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue());
			sql.addSelectClause("caseInfoData.companyName", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_NAME.getValue());
			sql.addSelectClause("caseInfoData.serialNumber", SrmCaseHandleInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sql.addSelectClause("caseInfoData.merchantHeaderName", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_HEADER_NAME.getValue());
			sql.addSelectClause("caseInfoData.bussinessAddress", SrmCaseHandleInfoDTO.ATTRIBUTE.BUSSINESS_ADDRESS.getValue());
			sql.addSelectClause("caseInfoData.merchantName", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue());
			sql.addSelectClause("caseInfoData.assetName", SrmCaseHandleInfoDTO.ATTRIBUTE.ASSET_NAME.getValue());
			
			sql.addSelectClause("caseInfoData.tid", SrmCaseHandleInfoDTO.ATTRIBUTE.TID.getValue());
			sql.addSelectClause("caseInfoData.merchantCode", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			
			builder.append(" ("); // 改為抓處理中  如果處理中全部結案 抓最新資料檔 2018/01/04 Bug #3055
			builder.append(queryPart(customerId, merchantCode, tId, merchantName, edcNumber, headerName, dtid, false, pageSize, currentPage));
			builder.append(" union all "); // 改為抓處理中  如果處理中全部結案 抓最新資料檔 2018/01/04 Bug #3055
			builder.append(queryPart(customerId, merchantCode, tId, merchantName, edcNumber, headerName, dtid, true, pageSize, currentPage));
			builder.append(" )caseInfoData");
			sql.addFromExpression(builder.toString());
			
			sql.setPageSize(pageSize);
			sql.setStartPage(currentPage - 1);
			//設置排序表達式
			if (!StringUtils.hasText(sort) && !StringUtils.hasText(order)) {
				sql.setOrderByExpression("caseInfoData.DTID asc");
			} else if (StringUtils.hasText(sort) && StringUtils.hasText(order)) {
				sql.setOrderByExpression(sort + IAtomsConstants.MARK_SPACE + order);
			}			
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			////CR #2551 待派工不可以查出來  建案 不可引用待派工案件dtid  2017/12/11  // 改為抓處理中  如果處理中全部結案 抓最新資料檔 2018/01/04
			//sqlQueryBean.setParameter("caseStatus", IAtomsConstants.CASE_STATUS.WAIT_DISPATCH.getCode());
			if (StringUtils.hasText(tId)) {
				sqlQueryBean.setParameter("tid", tId + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(merchantCode)) {
				sqlQueryBean.setParameter("merchantCode", merchantCode + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(merchantName)) {
				sqlQueryBean.setParameter("merchantName", merchantName + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(edcNumber)) {
				sqlQueryBean.setParameter("edcNumber", edcNumber + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(headerName)) {
				sqlQueryBean.setParameter("headerName", headerName + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(dtid)) {
				sqlQueryBean.setParameter("dtid", dtid + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(customerId)) {
				sqlQueryBean.setParameter("customerId", customerId);
			}
			AliasBean aliasBean = sql.createAliasBean(SrmCaseHandleInfoDTO.class);
			logger.debug(this.getClass().getName() + "SQL---------->" + sqlQueryBean.toString());
			caseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ".getDTID() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return caseHandleInfoDTOs;
	}
	
	/**
	 * Purpose:查詢sql部分
	 * @author CrissZhang
	 * @param customerId
	 * @param merchantCode
	 * @param tId
	 * @param merchantName
	 * @param edcNumber
	 * @param headerName
	 * @param dtid
	 * @param isNew
	 * @return String
	 */
	private String queryPart(String customerId, String merchantCode, String tId, String merchantName,String edcNumber, 
			String headerName, String dtid, boolean isNew,Integer pageSize, Integer currentPage){
		SqlStatement sql = new SqlStatement();
		StringBuilder builder = new StringBuilder();
		String schema = this.getMySchema();
		sql.addSelectClause("caseInfo.CASE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue());
		sql.addSelectClause("caseInfo.DTID", SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue());
		sql.addSelectClause("company.SHORT_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_NAME.getValue());
		sql.addSelectClause("assetLink.SERIAL_NUMBER", SrmCaseHandleInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
		sql.addSelectClause("header.HEADER_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_HEADER_NAME.getValue());
		sql.addSelectClause("header.BUSINESS_ADDRESS", SrmCaseHandleInfoDTO.ATTRIBUTE.BUSSINESS_ADDRESS.getValue());
		sql.addSelectClause("merchant.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue());
		sql.addSelectClause("asset.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.ASSET_NAME.getValue());

		builder.delete(0, builder.length());
		builder.append("STUFF((SELECT DISTINCT ',' + LTRIM(param.TID) FROM ").append(schema);
		if(isNew){
			builder.append(".SRM_CASE_NEW_HANDLE_INFO info ");
			builder.append(" left join ").append(schema).append(".SRM_CASE_NEW_TRANSACTION_PARAMETER param on info.CASE_ID = param.CASE_ID ");
		} else {
			builder.append(".SRM_CASE_HANDLE_INFO info ");
			builder.append(" left join ").append(schema).append(".SRM_CASE_TRANSACTION_PARAMETER param on info.CASE_ID = param.CASE_ID ");
		}
		builder.append(" WHERE info.CASE_ID=caseInfo.CASE_ID  and param.TID <> ''AND param.TID IS NOT NULL");
		builder.append(" FOR XML PATH('')), 1, 1, '')");
		sql.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.TID.getValue());
		
		builder.delete(0, builder.length());
		builder.append("STUFF(( SELECT DISTINCT ',' + LTRIM(param.MERCHANT_CODE  ) FROM ").append(schema);
		if(isNew){
			builder.append(".SRM_CASE_NEW_HANDLE_INFO info ");
			builder.append(" left join ").append(schema).append(".SRM_CASE_NEW_TRANSACTION_PARAMETER param on info.CASE_ID = param.CASE_ID ");
		} else {
			builder.append(".SRM_CASE_HANDLE_INFO info ");
			builder.append(" left join ").append(schema).append(".SRM_CASE_TRANSACTION_PARAMETER param on info.CASE_ID = param.CASE_ID ");
		}
		builder.append(" WHERE info.CASE_ID=caseInfo.CASE_ID  and param.MERCHANT_CODE <> ''AND param.MERCHANT_CODE IS NOT NULL");
		builder.append(" FOR XML PATH('')), 1, 1, '')");
		sql.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
		
		builder.delete(0, builder.length());
		if(isNew){
			builder.append(schema).append(".SRM_CASE_NEW_HANDLE_INFO caseInfo ");
//			builder.append(" left join ").append(schema).append(".SRM_CASE_NEW_TRANSACTION_PARAMETER trans ");
//			builder.append(" on trans.CASE_ID = caseInfo.CASE_ID");
			builder.append(" left join ").append(schema).append(".SRM_CASE_NEW_ASSET_LINK assetLink ");
			builder.append(" on assetLink.CASE_ID = caseInfo.CASE_ID and assetLink.ITEM_TYPE = '10' ");
		} else {
			builder.append(schema).append(".SRM_CASE_HANDLE_INFO caseInfo ");
//			builder.append(" left join ").append(schema).append(".SRM_CASE_TRANSACTION_PARAMETER trans ");
//			builder.append(" on trans.CASE_ID = caseInfo.CASE_ID");
			 // 改為抓處理中  如果處理中全部結案 抓最新資料檔 2018/01/04 Bug #3055
			builder.append(" left join ").append(schema).append(".SRM_CASE_ASSET_LINK assetLink ");
			builder.append(" on assetLink.CASE_ID = caseInfo.CASE_ID and assetLink.ITEM_TYPE = '10' and assetLink.IS_LINK<>'D' ");
		}
		builder.append(" left join ").append(schema).append(".DMM_ASSET_TYPE asset on asset.ASSET_TYPE_ID = assetLink.ITEM_ID");
		builder.append(" left join ").append(schema).append(".BIM_MERCHANT_HEADER header on header.MERCHANT_HEADER_ID = caseInfo.MERCHANT_HEADER_ID");
		builder.append(" left join ").append(schema).append(".BIM_COMPANY company on company.COMPANY_ID = caseInfo.COMPANY_ID");
		builder.append(" left join ").append(schema).append(".BIM_MERCHANT merchant on merchant.MERCHANT_ID = header.MERCHANT_ID");
		sql.addFromExpression(builder.toString());
		
		if (StringUtils.hasText(dtid)) {
			sql.addWhereClause("caseInfo.DTID like :dtid");
		}
		if (StringUtils.hasText(customerId)) {
			sql.addWhereClause("caseInfo.CUSTOMER_ID = :customerId");
		}
		if (StringUtils.hasText(merchantCode)) {
			if(isNew){
				sql.addWhereClause("EXISTS (select 1 from dbo.SRM_CASE_NEW_TRANSACTION_PARAMETER trans where trans.CASE_ID = caseInfo.CASE_ID and trans.MERCHANT_CODE like :merchantCode)");
			} else {
				sql.addWhereClause("EXISTS (select 1 from dbo.SRM_CASE_TRANSACTION_PARAMETER trans where trans.CASE_ID = caseInfo.CASE_ID and trans.MERCHANT_CODE like :merchantCode)");
			}
		}
		if (StringUtils.hasText(tId)) {
			//sql.addWhereClause("trans.TID in " + IAtomsConstants.MARK_BRACKETS_LEFT + IAtomsConstants.MARK_QUOTES + tId + IAtomsConstants.MARK_QUOTES + IAtomsConstants.MARK_BRACKETS_RIGHT);
			if(isNew){
				sql.addWhereClause("EXISTS (select 1 from dbo.SRM_CASE_NEW_TRANSACTION_PARAMETER trans where trans.CASE_ID = caseInfo.CASE_ID and trans.TID like :tid)");
			} else {
				sql.addWhereClause("EXISTS (select 1 from dbo.SRM_CASE_TRANSACTION_PARAMETER trans where trans.CASE_ID = caseInfo.CASE_ID and trans.TID like :tid)");
			}
		}
		if (StringUtils.hasText(merchantName)) {
			sql.addWhereClause("merchant.NAME like :merchantName");
		}
		if (StringUtils.hasText(edcNumber)) {
			sql.addWhereClause("assetLink.SERIAL_NUMBER like :edcNumber");
		}
		if (StringUtils.hasText(headerName)) {
			sql.addWhereClause("header.HEADER_NAME like :headerName");
		}
		
//		sql.addWhereClause("caseInfo.CASE_ID not in(select info.CASE_ID from dbo.SRM_CASE_NEW_HANDLE_INFO info where info.CASE_STATUS != :close and info.CASE_STATUS != :immediateclose and info.CASE_CATEGORY = :caseCategory)");
		StringBuilder temp4 = new StringBuilder();
		//優先抓處理中Bug #3055 
		temp4.append(" select t1.* from (select info.DTID,info.CASE_ID from dbo.SRM_CASE_HANDLE_INFO info,");
		temp4.append("((select info.DTID,max( CREATED_DATE ) as maxCreatedDate from dbo.SRM_CASE_HANDLE_INFO info where info.CASE_STATUS not in ('ImmediateClose','Closed','Voided','WaitDispatch') group by info.DTID)) t ");
		temp4.append("	where info.DTID = t.DTID and info.CREATED_DATE = t.maxCreatedDate AND info.CASE_STATUS not in('ImmediateClose','Closed','Voided','WaitDispatch' ");
		temp4.append("	)) t1,(select w.dtid,max( w.case_id ) as maxCaseId from ");
		temp4.append("	(select info.DTID,info.CASE_ID from dbo.SRM_CASE_HANDLE_INFO info,((select info.DTID,max( CREATED_DATE ) as maxCreatedDate from dbo.SRM_CASE_HANDLE_INFO info where info.CASE_STATUS not in( 'ImmediateClose', 'Closed', 'Voided', 'WaitDispatch') ");
		temp4.append("	group by info.DTID)) t where info.DTID = t.DTID and info.CREATED_DATE = t.maxCreatedDate AND info.CASE_STATUS not in( ");
		temp4.append("	'ImmediateClose','Closed','Voided','WaitDispatch')) w group by w.dtid) t2 where t1.case_id = t2.maxCaseId");
		if(!isNew){
			//建案 不可引用待派工案件dtid CR #2551 2017/12/11 // 改為抓處理中  如果處理中全部結案 抓最新資料檔 2018/01/04
			//sql.addWhereClause("caseInfo.CASE_STATUS <>:caseStatus");
			
			builder.delete(0, builder.length());
			
			//查詢處理中
			builder.append(" caseInfo.CASE_ID in (select aa.case_id from (").append(temp4).append(") aa)");
			sql.addWhereClause(builder.toString());
		} else {
			builder.delete(0, builder.length());
			
			builder.append(" caseInfo.CASE_ID in (select newInfo.CASE_ID from ").append(schema).append(".SRM_CASE_NEW_HANDLE_INFO newInfo where newInfo.DTID not in (select dtid from (").append(temp4).append(") t))");
			sql.addWhereClause(builder.toString());
		}
		return sql.toString();
	}
	
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#getCount(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public Integer getCount(String customerId, String merchantCode, String tId, String merchantName, String edcNumber, String headerName,
			String dtid, boolean isNew) throws DataAccessException {
		List<Integer> result = null;
		try {
			logger.debug(this.getClass().getName() + ".getCount()", "parameters:customerId=" + customerId);
			logger.debug(this.getClass().getName() + ".getCount()", "parameters:merchantCode=" + merchantCode);
			logger.debug(this.getClass().getName() + ".getCount()", "parameters:tId=" + tId);
			logger.debug(this.getClass().getName() + ".getCount()", "parameters:merchantName=" + merchantName);
			logger.debug(this.getClass().getName() + ".getCount()", "parameters:edcNumber=" + edcNumber);
			logger.debug(this.getClass().getName() + ".getCount()", "parameters:headerName=" + headerName);
			logger.debug(this.getClass().getName() + ".getCount()", "parameters:dtid=" + dtid);
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			StringBuilder builder = new StringBuilder();
			sqlStatement.addSelectClause("count(1)");
			builder.delete(0, builder.length());
			if(isNew){
				builder.append(schema).append(".SRM_CASE_NEW_HANDLE_INFO caseInfo ");
				builder.append(" left join ").append(schema).append(".SRM_CASE_NEW_ASSET_LINK assetLink on assetLink.CASE_ID = caseInfo.CASE_ID and assetLink.ITEM_TYPE = '10'");
			} else {
				builder.append(schema).append(".SRM_CASE_HANDLE_INFO caseInfo ");
				builder.append(" left join ").append(schema).append(".SRM_CASE_ASSET_LINK assetLink on assetLink.CASE_ID = caseInfo.CASE_ID and assetLink.ITEM_TYPE = '10' and assetLink.IS_LINK<>'D' ");
			}
			builder.append(" left join ").append(schema).append(".DMM_ASSET_TYPE asset on asset.ASSET_TYPE_ID = assetLink.ITEM_ID");
			builder.append(" left join ").append(schema).append(".BIM_MERCHANT_HEADER header on header.MERCHANT_HEADER_ID = caseInfo.MERCHANT_HEADER_ID");
			builder.append(" left join ").append(schema).append(".BIM_MERCHANT merchant on merchant.MERCHANT_ID = header.MERCHANT_ID");
			sqlStatement.addFromExpression(builder.toString());
			
			
			if (StringUtils.hasText(dtid)) {
				sqlStatement.addWhereClause("caseInfo.DTID like :dtid", dtid + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(customerId)) {
				sqlStatement.addWhereClause("caseInfo.CUSTOMER_ID = :customerId", customerId);
			}
			if (StringUtils.hasText(merchantCode)) {
				if(isNew){
					sqlStatement.addWhereClause("EXISTS (select 1 from dbo.SRM_CASE_NEW_TRANSACTION_PARAMETER trans where trans.CASE_ID = caseInfo.CASE_ID and trans.MERCHANT_CODE like :merchantCode)");
				} else {
					sqlStatement.addWhereClause("EXISTS (select 1 from dbo.SRM_CASE_TRANSACTION_PARAMETER trans where trans.CASE_ID = caseInfo.CASE_ID and trans.MERCHANT_CODE like :merchantCode)");
				}
			}
			if (StringUtils.hasText(tId)) {
				if(isNew){
					sqlStatement.addWhereClause("EXISTS (select 1 from dbo.SRM_CASE_NEW_TRANSACTION_PARAMETER trans where trans.CASE_ID = caseInfo.CASE_ID and trans.TID like :tid)");
				} else {
					sqlStatement.addWhereClause("EXISTS (select 1 from dbo.SRM_CASE_TRANSACTION_PARAMETER trans where trans.CASE_ID = caseInfo.CASE_ID and trans.TID like :tid)");
				}
			}
			if (StringUtils.hasText(merchantName)) {
				sqlStatement.addWhereClause("merchant.NAME like :merchantName", merchantName + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(edcNumber)) {
				sqlStatement.addWhereClause("assetLink.SERIAL_NUMBER like :edcNumber", edcNumber + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(headerName)) {
				sqlStatement.addWhereClause("header.HEADER_NAME like :headerName", headerName + IAtomsConstants.MARK_PERCENT);
			}
			 // 改為抓處理中  如果處理中全部結案 抓最新資料檔 2018/01/04 Bug #3055
			StringBuilder temp4 = new StringBuilder();
			temp4.append(" select t1.* from (select info.DTID,info.CASE_ID from dbo.SRM_CASE_HANDLE_INFO info,");
			temp4.append("((select info.DTID,max( CREATED_DATE ) as maxCreatedDate from dbo.SRM_CASE_HANDLE_INFO info where info.CASE_STATUS not in ('ImmediateClose','Closed','Voided','WaitDispatch') group by info.DTID)) t ");
			temp4.append("	where info.DTID = t.DTID and info.CREATED_DATE = t.maxCreatedDate AND info.CASE_STATUS not in('ImmediateClose','Closed','Voided','WaitDispatch' ");
			temp4.append("	)) t1,(select w.dtid,max( w.case_id ) as maxCaseId from ");
			temp4.append("	(select info.DTID,info.CASE_ID from dbo.SRM_CASE_HANDLE_INFO info,((select info.DTID,max( CREATED_DATE ) as maxCreatedDate from dbo.SRM_CASE_HANDLE_INFO info where info.CASE_STATUS not in( 'ImmediateClose', 'Closed', 'Voided', 'WaitDispatch') ");
			temp4.append("	group by info.DTID)) t where info.DTID = t.DTID and info.CREATED_DATE = t.maxCreatedDate AND info.CASE_STATUS not in( ");
			temp4.append("	'ImmediateClose','Closed','Voided','WaitDispatch') AND info.CASE_CATEGORY <> 'OTHER') w group by w.dtid) t2 where t1.case_id = t2.maxCaseId");
			if(!isNew){
				//建案 不可引用待派工案件dtid CR #2551 2017/12/11  // 改為抓處理中  如果處理中全部結案 抓最新資料檔 2018/01/04
				//sqlStatement.addWhereClause("caseInfo.CASE_STATUS <>:caseStatus", IAtomsConstants.CASE_STATUS.WAIT_DISPATCH.getCode());
				
				builder.delete(0, builder.length());
				
				builder.append(" caseInfo.CASE_ID in (select aa.case_id from (").append(temp4).append(") aa)");
				sqlStatement.addWhereClause(builder.toString());
			} else {
				builder.delete(0, builder.length());
				
				builder.append(" caseInfo.CASE_ID in (select newInfo.CASE_ID from ").append(schema).append(".SRM_CASE_NEW_HANDLE_INFO newInfo where newInfo.DTID not in (select dtid from (").append(temp4).append(") t))");
				sqlStatement.addWhereClause(builder.toString());
			}
			
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			
			if (StringUtils.hasText(tId)) {
				sqlQueryBean.setParameter("tid", tId + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(merchantCode)) {
				sqlQueryBean.setParameter("merchantCode", merchantCode + IAtomsConstants.MARK_PERCENT);
			}
			logger.debug(this.getClass().getName() + "SQL---------->" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)){
				return result.get(0).intValue();
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ".getCount() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return null;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#getCaseHandleInfoDTOById(java.lang.String)
	 */
	public SrmCaseHandleInfoDTO getCaseHandleInfoDTOById(String dtid) throws DataAccessException {
		try {
			// 依照dtid查詢最新案件資料
			return this.getCaseHandleInfoDTO(dtid, true);
		} catch (Exception e) {
			logger.error("getCaseHandleInfoDTOById()", e.getMessage(), e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
	}
	
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseNewHandleInfoDAO#deleteTransferData()
	 */
	@Override
	public void deleteTransferData() throws DataAccessException {
		try {
			String schema = this.getMySchema();
			SqlQueryBean sqlQueryBean = new SqlQueryBean("delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_NEW_ASSET_LINK; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_NEW_ASSET_FUNCTION; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_NEW_TRANSACTION_PARAMETER; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_NEW_COMM_MODE; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_NEW_HANDLE_INFO; ");
			logger.debug("deleteTransferData()", "sql:" + sqlQueryBean.toString());
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			logger.error("deleteTransferData()", e.getMessage(), e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
	}
	

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseNewHandleInfoDAO#isInNewCase()
	 */
	@Override
	public boolean isInNewCase(String dtid) throws DataAccessException {
		try {
			return this.isInNewCase(dtid, null);
		} catch (Exception e) {
			logger.error("isInNewCase()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseNewHandleInfoDAO#isInNewCase()
	 */
	@Override
	public boolean isInNewCase(String dtid, String customerId) throws DataAccessException {
		try {
			logger.debug("isInNewCase()", "parameters:dtid=" + dtid);
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			sqlStatement.addFromExpression(schema + ".SRM_CASE_NEW_HANDLE_INFO ");
			if (StringUtils.hasText(dtid)) {
				sqlStatement.addWhereClause("DTID =:dtid", dtid);
			}
			if (StringUtils.hasText(customerId)) {
				sqlStatement.addWhereClause("CUSTOMER_ID =:customerId", customerId);
			}
			
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			logger.debug("isInNewCase()", "sql:" + sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				if(result.get(0).intValue() > 0){
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			logger.error("isInNewCase()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseNewHandleInfoDAO#isInCase()
	 */
	@Override
	public boolean isInCase(String dtid, String customerId) throws DataAccessException {
		try { // 改為抓處理中  如果處理中全部結案 抓最新資料檔 2018/01/04 Bug #3055
			logger.debug("isInNewCase()", "parameters:dtid=" + dtid);
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			sqlStatement.addFromExpression(schema + ".SRM_CASE_HANDLE_INFO ");
			if (StringUtils.hasText(dtid)) {
				sqlStatement.addWhereClause("DTID =:dtid", dtid);
			}
			if (StringUtils.hasText(customerId)) {
				sqlStatement.addWhereClause("CUSTOMER_ID =:customerId", customerId);
			}
			sqlStatement.addWhereClause("CASE_STATUS not in ('ImmediateClose', 'Closed', 'Voided', 'WaitDispatch') AND CASE_CATEGORY <> 'OTHER'");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			logger.debug("isInCase()", "sql:" + sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				if(result.get(0).intValue() > 0){
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			logger.error("isInCase()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseNewHandleInfoDAO#getCaseHandleInfoDTO(java.lang.String, boolean)
	 */
	@Override
	public SrmCaseHandleInfoDTO getCaseHandleInfoDTO(String dtid, boolean isNewHave) throws DataAccessException {
		try {
			return this.getCaseHandleInfoDTO(dtid, isNewHave, null);
		} catch (Exception e) {
			logger.error("getCaseHandleInfoDTO()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseNewHandleInfoDAO#getCaseHandleInfoDTO(java.lang.String, boolean,java.lang.String)
	 */
	@Override
	public SrmCaseHandleInfoDTO getCaseHandleInfoDTO(String dtid, boolean isHave, String customerId) throws DataAccessException {
		logger.debug(this.getClass().getName() + "caseId" + dtid);
		SqlStatement  sqlStatement = new SqlStatement();
		String schema = this.getMySchema();
		try{
			StringBuilder builder = new StringBuilder();
			sqlStatement.addSelectClause("info.CASE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("info.REQUIREMENT_NO", SrmCaseHandleInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue());
			sqlStatement.addSelectClause("info.CONTRACT_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTRACT_ID.getValue());
			sqlStatement.addSelectClause("info.CASE_CATEGORY", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue());
			sqlStatement.addSelectClause("contract.CONTRACT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue());
			sqlStatement.addSelectClause("info.CUSTOMER_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue());
			sqlStatement.addSelectClause("company.SHORT_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_NAME.getValue());
			sqlStatement.addSelectClause("company.COMPANY_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_CODE.getValue());
			sqlStatement.addSelectClause("vendor.SHORT_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_NAME.getValue());
			sqlStatement.addSelectClause("info.DEPARTMENT_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_ID.getValue());
//			sqlStatement.addSelectClause("dept.DEPT_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_NAME.getValue());
			sqlStatement.addSelectClause("(case when dept.DEPT_NAME is not null then dept.DEPT_NAME else deptCaseGroup.ITEM_NAME end)", SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_NAME.getValue());			
			
			sqlStatement.addSelectClause("info.DTID", SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("info.INSTALL_TYPE", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_TYPE.getValue());
		//	sqlStatement.addSelectClause("info.HANDLED_TYPE", SrmCaseHandleInfoDTO.ATTRIBUTE.HANDLED_TYPE.getValue());
			sqlStatement.addSelectClause("info.COMPANY_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_ID.getValue());
			sqlStatement.addSelectClause("info.REPAIR_REASON", SrmCaseHandleInfoDTO.ATTRIBUTE.REPAIR_REASON.getValue());
			sqlStatement.addSelectClause("info.UNINSTALL_TYPE", SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALL_TYPE.getValue());
			sqlStatement.addSelectClause("info.CASE_TYPE", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE.getValue());
			sqlStatement.addSelectClause("ticketMode.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("info.EXPECTED_COMPLETION_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.EXPECTED_COMPLETION_DATE.getValue());
			sqlStatement.addSelectClause("info.MERCHANT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_ID.getValue());
		//	sqlStatement.addSelectClause("merchant.STAGES_MERCHANT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.STAGES_MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("merchant.MERCHANT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("merchant.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue());
			sqlStatement.addSelectClause("info.MERCHANT_HEADER_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_HEADER_ID.getValue());
			sqlStatement.addSelectClause("header.HEADER_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.HEADER_NAME.getValue());
			sqlStatement.addSelectClause("header.IS_VIP", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_VIP.getValue());
			sqlStatement.addSelectClause("header.AREA", SrmCaseHandleInfoDTO.ATTRIBUTE.AREA.getValue());
			sqlStatement.addSelectClause("region.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.AREA_NAME.getValue());
			sqlStatement.addSelectClause("header.LOCATION", SrmCaseHandleInfoDTO.ATTRIBUTE.LOCATION.getValue());
			sqlStatement.addSelectClause("header.CONTACT", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT.getValue());
			sqlStatement.addSelectClause("header.CONTACT_TEL", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_TEL.getValue());
			sqlStatement.addSelectClause("header.CONTACT_TEL2", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_TEL2.getValue());
			sqlStatement.addSelectClause("header.PHONE", SrmCaseHandleInfoDTO.ATTRIBUTE.PHONE.getValue());
			sqlStatement.addSelectClause("header.BUSINESS_ADDRESS", SrmCaseHandleInfoDTO.ATTRIBUTE.BUSINESS_ADDRESS.getValue());
			sqlStatement.addSelectClause("header.OPEN_HOUR", SrmCaseHandleInfoDTO.ATTRIBUTE.OPEN_HOUR.getValue());
			sqlStatement.addSelectClause("header.CLOSE_HOUR", SrmCaseHandleInfoDTO.ATTRIBUTE.CLOSE_HOUR.getValue());
			sqlStatement.addSelectClause("header.AOEmail", SrmCaseHandleInfoDTO.ATTRIBUTE.AO_EMAIL.getValue());
			sqlStatement.addSelectClause("header.AO_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.AO_NAME.getValue());
			sqlStatement.addSelectClause("header.POST_CODE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.LOCATION_POST_CODE.getValue());
			//Task #3343
			sqlStatement.addSelectClause("header.CONTACT_EMAIL", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_EMAIL.getValue());
			
			sqlStatement.addSelectClause("info.CONTACT_USER_PHONE", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_PHONE.getValue());
			sqlStatement.addSelectClause("info.CONTACT_USER", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER.getValue());
			sqlStatement.addSelectClause("info.CONTACT_ADDRESS", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS.getValue());
			sqlStatement.addSelectClause("info.CONTACT_ADDRESS_LOCATION", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS_LOCATION.getValue());
			sqlStatement.addSelectClause("contactLocation.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS_LOCATION_NAME.getValue());
			sqlStatement.addSelectClause("info.CONTACT_IS_BUSSINESS_CONTACT_PHONE", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_IS_BUSSINESS_CONTACT_PHONE.getValue());
			sqlStatement.addSelectClause("info.CONTACT_IS_BUSSINESS_CONTACT", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_IS_BUSSINESS_CONTACT.getValue());
			sqlStatement.addSelectClause("info.CONTACT_IS_BUSSINESS_ADDRESS", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_IS_BUSSINESS_ADDRESS.getValue());
			sqlStatement.addSelectClause("info.CONTACT_POST_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_POST_CODE.getValue());
			//Task #3343
			sqlStatement.addSelectClause("info.CONTACT_IS_BUSSINESS_CONTACT_MOBILE_PHONE", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_IS_BUSSINESS_CONTACT_MOBILE_PHONE.getValue());
			sqlStatement.addSelectClause("info.CONTACT_IS_BUSSINESS_CONTACT_EMAIL", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_IS_BUSSINESS_CONTACT_EMAIL.getValue());
			sqlStatement.addSelectClause("info.CONTACT_MOBILE_PHONE", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_MOBILE_PHONE.getValue());
			sqlStatement.addSelectClause("info.CONTACT_USER_EMAIL", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_EMAIL.getValue());

			sqlStatement.addSelectClause("info.INSTALLED_POST_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_POST_CODE.getValue());
			sqlStatement.addSelectClause("info.INSTALLED_ADRESS", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue());
			sqlStatement.addSelectClause("info.INSTALLED_ADRESS_LOCATION", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS_LOCATION.getValue());
			sqlStatement.addSelectClause("installedLocation.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS_LOCATION_NAME.getValue());
			sqlStatement.addSelectClause("info.IS_BUSSINESS_ADDRESS", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_BUSSINESS_ADDRESS.getValue());
			sqlStatement.addSelectClause("info.INSTALLED_CONTACT", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT.getValue());
			sqlStatement.addSelectClause("info.IS_BUSSINESS_CONTACT", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_BUSSINESS_CONTACT.getValue());
			sqlStatement.addSelectClause("info.INSTALLED_CONTACT_PHONE", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_PHONE.getValue());
			sqlStatement.addSelectClause("info.IS_BUSSINESS_CONTACT_PHONE", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_BUSSINESS_CONTACT_PHONE.getValue());
			//Task #3343
			sqlStatement.addSelectClause("info.IS_BUSSINESS_CONTACT_MOBILE_PHONE", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_BUSSINESS_CONTACT_MOBILE_PHONE.getValue());
			sqlStatement.addSelectClause("info.IS_BUSSINESS_CONTACT_EMAIL", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_BUSSINESS_CONTACT_EMAIL.getValue());
			sqlStatement.addSelectClause("info.INSTALLED_CONTACT_MOBILE_PHONE", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_MOBILE_PHONE.getValue());
			sqlStatement.addSelectClause("info.INSTALLED_CONTACT_EMAIL", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_EMAIL.getValue());
			
			sqlStatement.addSelectClause("info.EDC_TYPE", SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE.getValue());
			sqlStatement.addSelectClause("assetEdc.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("info.SOFTWARE_VERSION", SrmCaseHandleInfoDTO.ATTRIBUTE.SOFTWARE_VERSION.getValue());
			sqlStatement.addSelectClause("info.IS_TMS", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_TMS.getValue());
			//Task #3322
			sqlStatement.addSelectClause("info.CMS_CASE", SrmCaseHandleInfoDTO.ATTRIBUTE.CMS_CASE.getValue());
			//Task #3349 
			sqlStatement.addSelectClause("info.INSTALL_CASE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_CASE_ID.getValue());
			sqlStatement.addSelectClause("info.INSTALL_COMPLETE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_COMPLETE_DATE.getValue());

			sqlStatement.addSelectClause("case when isnull(info.SOFTWARE_VERSION, '') = '' then null "
					+" else (CONCAT( CONVERT( VARCHAR( 100 ) ,application.NAME ,111 ) ,'【' ,CONVERT( VARCHAR( 100 ) ,application.VERSION ,111 ) ,'】')) "
					+ " end", SrmCaseHandleInfoDTO.ATTRIBUTE.SOFTWARE_VERSION_NAME.getValue());
			
		//	sqlStatement.addSelectClause("CONCAT( CONVERT( VARCHAR( 100 ) ,application.NAME ,111 ) ,'【' ,CONVERT( VARCHAR( 100 ) ,application.VERSION ,111 ) ,'】')", SrmCaseHandleInfoDTO.ATTRIBUTE.SOFTWARE_VERSION_NAME.getValue());
			sqlStatement.addSelectClause("info.BUILT_IN_FEATURE", SrmCaseHandleInfoDTO.ATTRIBUTE.BUILT_IN_FEATURE.getValue());
			
			// 內建功能名稱
			builder.delete(0, builder.length());
			if(!isHave){
				builder.append("stuff(( SELECT ',' + RTRIM( supportedFun.ITEM_NAME ) FROM ").append(schema).append(".SRM_CASE_NEW_ASSET_FUNCTION caseAssetFun ");
			} else {
				builder.append("stuff(( SELECT ',' + RTRIM( supportedFun.ITEM_NAME ) FROM ").append(schema).append(".SRM_CASE_ASSET_FUNCTION caseAssetFun ");
			}
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF supportedFun on supportedFun.BPTD_CODE = :supportedFunctionParam");
			builder.append(" and supportedFun.ITEM_VALUE = caseAssetFun.FUNCTION_ID where caseAssetFun.CASE_ID = info.CASE_ID ");
			builder.append(" and caseAssetFun.FUNCTION_CATEGORY = '10' FOR XML path('') ) ,1 , 1 ,'')");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.BUILT_IN_FEATURE_NAME.getValue());
			
			sqlStatement.addSelectClause("info.MULTI_MODULE", SrmCaseHandleInfoDTO.ATTRIBUTE.MULTI_MODULE.getValue());
			sqlStatement.addSelectClause("item.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.MULTI_MODULE_NAME.getValue());
			sqlStatement.addSelectClause("info.PERIPHERALS", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS.getValue());
			sqlStatement.addSelectClause("assetPer.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_NAME.getValue());
			sqlStatement.addSelectClause("info.PERIPHERALS_FUNCTION", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION.getValue());
			sqlStatement.addSelectClause("info.RECEIPT_TYPE", SrmCaseHandleInfoDTO.ATTRIBUTE.RECEIPT_TYPE.getValue());
			sqlStatement.addSelectClause("receiptItem.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.RECEIPT_TYPE_NAME.getValue());
			// 周邊設備功能名稱
			builder.delete(0, builder.length());
			if(!isHave){
				builder.append("stuff(( SELECT ',' + RTRIM( supportedFun.ITEM_NAME ) FROM ").append(schema).append(".SRM_CASE_NEW_ASSET_FUNCTION caseAssetFun ");
			} else {
				builder.append("stuff(( SELECT ',' + RTRIM( supportedFun.ITEM_NAME ) FROM ").append(schema).append(".SRM_CASE_ASSET_FUNCTION caseAssetFun ");
			}
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF supportedFun on supportedFun.BPTD_CODE = :supportedFunctionParam");
			builder.append(" and supportedFun.ITEM_VALUE = caseAssetFun.FUNCTION_ID where caseAssetFun.CASE_ID = info.CASE_ID ");
			builder.append(" and caseAssetFun.FUNCTION_CATEGORY = '11' FOR XML path('') ) ,1 , 1 ,'')");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION_NAME.getValue());
			
			sqlStatement.addSelectClause("info.PERIPHERALS2", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2.getValue());
			sqlStatement.addSelectClause("assetPer2.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_NAME.getValue());
			sqlStatement.addSelectClause("info.PERIPHERALS_FUNCTION2", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION2.getValue());
			// 周邊設備2功能名稱
			builder.delete(0, builder.length());
			if(!isHave){
				builder.append("stuff(( SELECT ',' + RTRIM( supportedFun.ITEM_NAME ) FROM ").append(schema).append(".SRM_CASE_NEW_ASSET_FUNCTION caseAssetFun ");
			}else {
				builder.append("stuff(( SELECT ',' + RTRIM( supportedFun.ITEM_NAME ) FROM ").append(schema).append(".SRM_CASE_ASSET_FUNCTION caseAssetFun ");
			}
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF supportedFun on supportedFun.BPTD_CODE = :supportedFunctionParam");
			builder.append(" and supportedFun.ITEM_VALUE = caseAssetFun.FUNCTION_ID where caseAssetFun.CASE_ID = info.CASE_ID ");
			builder.append(" and caseAssetFun.FUNCTION_CATEGORY = '12' FOR XML path('') ) ,1 , 1 ,'')");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION2_NAME.getValue());
			
			sqlStatement.addSelectClause("info.PERIPHERALS3", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3.getValue());
			sqlStatement.addSelectClause("assetPer3.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_NAME.getValue());
			sqlStatement.addSelectClause("info.PERIPHERALS_FUNCTION3", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION3.getValue());
			// 周邊設備3功能名稱
			builder.delete(0, builder.length());
			if(!isHave){
				builder.append("stuff(( SELECT ',' + RTRIM( supportedFun.ITEM_NAME ) FROM ").append(schema).append(".SRM_CASE_NEW_ASSET_FUNCTION caseAssetFun ");
			} else {
				builder.append("stuff(( SELECT ',' + RTRIM( supportedFun.ITEM_NAME ) FROM ").append(schema).append(".SRM_CASE_ASSET_FUNCTION caseAssetFun ");
			}
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF supportedFun on supportedFun.BPTD_CODE = :supportedFunctionParam");
			builder.append(" and supportedFun.ITEM_VALUE = caseAssetFun.FUNCTION_ID where caseAssetFun.CASE_ID  = info.CASE_ID ");
			builder.append(" and caseAssetFun.FUNCTION_CATEGORY = '13' FOR XML path('') ) ,1 , 1 ,'')");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION3_NAME.getValue());
			
			sqlStatement.addSelectClause("info.ECR_CONNECTION", SrmCaseHandleInfoDTO.ATTRIBUTE.ECR_CONNECTION.getValue());
			sqlStatement.addSelectClause("itemEcr.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.ECR_CONNECTION_NAME.getValue());
			sqlStatement.addSelectClause("info.CONNECTION_TYPE", SrmCaseHandleInfoDTO.ATTRIBUTE.CONNECTION_TYPE.getValue());
//			sqlStatement.addSelectClause("itemConnec.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CONNECTION_TYPE_NAME.getValue());
			
			builder.delete(0, builder.length());
			// 連線方式名稱
			builder.append("stuff(( SELECT ',' + RTRIM( itemConnec.ITEM_NAME ) FROM ").append(schema);
			if(!isHave){
				builder.append(".SRM_CASE_NEW_COMM_MODE caseCommMode ");
			} else {
				builder.append(".SRM_CASE_COMM_MODE caseCommMode ");
			}
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF itemConnec on itemConnec.BPTD_CODE = :connection");
			builder.append(" and itemConnec.ITEM_VALUE = caseCommMode.COMM_MODE_ID where caseCommMode.CASE_ID = info.CASE_ID ");
			builder.append("  FOR XML path('') ) ,1 , 1 ,'')");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONNECTION_TYPE_NAME.getValue());
			
			sqlStatement.addSelectClause("info.LOCALHOST_IP", SrmCaseHandleInfoDTO.ATTRIBUTE.LOCALHOST_IP.getValue());
			sqlStatement.addSelectClause("info.NET_VENDOR_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.NET_VENDOR_ID.getValue());
			sqlStatement.addSelectClause("netVendor.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.NET_VENDOR_NAME.getValue());
			sqlStatement.addSelectClause("info.GATEWAY", SrmCaseHandleInfoDTO.ATTRIBUTE.GATEWAY.getValue());
			sqlStatement.addSelectClause("info.NETMASK", SrmCaseHandleInfoDTO.ATTRIBUTE.NETMASK.getValue());
			sqlStatement.addSelectClause("info.DESCRIPTION", SrmCaseHandleInfoDTO.ATTRIBUTE.DESCRIPTION.getValue());
			sqlStatement.addSelectClause("info.ATTENDANCE_TIMES", SrmCaseHandleInfoDTO.ATTRIBUTE.ATTENDANCE_TIMES.getValue());
			sqlStatement.addSelectClause("info.CASE_STATUS", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS.getValue());
			sqlStatement.addSelectClause("info.IS_PROJECT", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_PROJECT.getValue());
			sqlStatement.addSelectClause("caseStatus.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS_NAME.getValue());
			sqlStatement.addSelectClause("info.ELECTRONIC_INVOICE", SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_INVOICE.getValue());
			sqlStatement.addSelectClause("info.CUP_QUICK_PASS", SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_QUICK_PASS.getValue());
			sqlStatement.addSelectClause("info.TMS_PARAM_DESC", SrmCaseHandleInfoDTO.ATTRIBUTE.TMS_PARAM_DESC.getValue());
			// LOGO
			sqlStatement.addSelectClause("info.LOGO_STYLE", SrmCaseHandleInfoDTO.ATTRIBUTE.LOGO_STYLE.getValue());
			// 是否開啟加密
			sqlStatement.addSelectClause("info.IS_OPEN_ENCRYPT", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_OPEN_ENCRYPT.getValue());
			// 電子化繳費平台
			sqlStatement.addSelectClause("info.ELECTRONIC_PAY_PLATFORM", SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_PAY_PLATFORM.getValue());
			// 裝機部門
			sqlStatement.addSelectClause("info.INSTALLED_DEPT_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_DEPT_ID.getValue());
			// 裝機人員
			sqlStatement.addSelectClause("info.INSTALLED_USER", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_USER.getValue());
			// 首裝
			sqlStatement.addSelectClause("info.IS_FIRST_INSTALLED", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_FIRST_INSTALLED.getValue());
			// 裝機日期
			sqlStatement.addSelectClause("info.INSTALLED_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_DATE.getValue());
			// cup啓用日期
			sqlStatement.addSelectClause("info.CUP_ENABLE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_ENABLE_DATE.getValue());
			// cup移除日期
			sqlStatement.addSelectClause("info.CUP_DISABLE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_DISABLE_DATE.getValue());
			// 是否查詢最新案件資料檔
			if(!isHave){
				sqlStatement.addSelectClause("'Y'", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_NEW_CASE.getValue());
			} else {
				sqlStatement.addSelectClause("'N'", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_NEW_CASE.getValue());
			}
			
			sqlStatement.addSelectClause("(CONCAT(installedPostCode.POST_CODE  ,'(' ,installedPostCode.POST_NAME ,')')) ", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_POST_CODE_NAME.getValue());
			sqlStatement.addSelectClause("(CONCAT(contactPostCode.POST_CODE  ,'(' ,contactPostCode.POST_NAME ,')')) ", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_POST_CODE_NAME.getValue());
			builder.delete(0, builder.length());
			if(!isHave){
				builder.append(schema).append(".SRM_CASE_NEW_HANDLE_INFO info");
				builder.append(" left join ").append(schema).append(".SRM_CASE_NEW_ASSET_LINK assetLink on assetLink.CASE_ID = info.CASE_ID and assetLink.ITEM_TYPE = '10'");
			} else {
				builder.append(schema).append(".SRM_CASE_HANDLE_INFO info");
				builder.append(" left join ").append(schema).append(".SRM_CASE_ASSET_LINK assetLink on assetLink.CASE_ID = info.CASE_ID and assetLink.ITEM_TYPE = '10' and assetLink.IS_LINK<>'D'");
			}
			builder.append(" left join ").append(schema).append(".BIM_CONTRACT contract on contract.CONTRACT_ID=info.CONTRACT_ID");
			builder.append(" left join ").append(schema).append(".PVM_APPLICATION application on application.APPLICATION_ID = info.SOFTWARE_VERSION");
			builder.append(" left join ").append(schema).append(".BIM_COMPANY company on company.COMPANY_ID = info.CUSTOMER_ID");
			builder.append(" left join ").append(schema).append(".BIM_COMPANY vendor on vendor.COMPANY_ID = info.COMPANY_ID");
			builder.append(" left join ").append(schema).append(".DMM_ASSET_TYPE assetEdc on assetEdc.ASSET_TYPE_ID = info.EDC_TYPE");
			builder.append(" left join ").append(schema).append(".DMM_ASSET_TYPE assetPer on assetPer.ASSET_TYPE_ID = info.PERIPHERALS");
			builder.append(" left join ").append(schema).append(".DMM_ASSET_TYPE assetPer2 on assetPer2.ASSET_TYPE_ID = info.PERIPHERALS2");
			builder.append(" left join ").append(schema).append(".DMM_ASSET_TYPE assetPer3 on assetPer3.ASSET_TYPE_ID = info.PERIPHERALS3");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF item on item.BPTD_CODE = :double and item.ITEM_VALUE = info.MULTI_MODULE");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF itemEcr on itemEcr.BPTD_CODE = :ecr and itemEcr.ITEM_VALUE = info.ECR_CONNECTION");
			builder.append(" left join ").append(schema).append(".BIM_MERCHANT_HEADER header on header.MERCHANT_HEADER_ID = info.MERCHANT_HEADER_ID");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF region on region.BPTD_CODE = :region and region.ITEM_VALUE = header.AREA");
			builder.append(" left join ").append(schema).append(".BIM_MERCHANT merchant on merchant.MERCHANT_ID = info.MERCHANT_CODE");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF netVendor on netVendor.BPTD_CODE = :netVendor and netVendor.ITEM_VALUE = info.NET_VENDOR_ID");
			builder.append(" left join ").append(schema).append(".BIM_DEPARTMENT dept on dept.DEPT_CODE = info.DEPARTMENT_ID");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF caseStatus on caseStatus.BPTD_CODE = :caseStatus and caseStatus.ITEM_VALUE = info.CASE_STATUS");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF ticketMode on ticketMode.BPTD_CODE = :ticketModeParam and ticketMode.ITEM_VALUE = info.CASE_TYPE");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF deptCaseGroup on deptCaseGroup.BPTD_CODE = :caseGroupParam ");
			builder.append(" and deptCaseGroup.ITEM_VALUE = info.DEPARTMENT_ID ");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_POST_CODE installedPostCode on installedPostCode.POST_CODE_ID = info.INSTALLED_POST_CODE ");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_POST_CODE contactPostCode on contactPostCode.POST_CODE_ID = info.CONTACT_POST_CODE ");
			
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF installedLocation on installedLocation.BPTD_CODE = :location and installedLocation.ITEM_VALUE = info.INSTALLED_ADRESS_LOCATION ");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF contactLocation on contactLocation.BPTD_CODE = :location and contactLocation.ITEM_VALUE = info.CONTACT_ADDRESS_LOCATION ");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF receiptItem on receiptItem.ITEM_VALUE = info.RECEIPT_TYPE and receiptItem.BPTD_CODE = :receiptType");
			sqlStatement.addFromExpression(builder.toString());
/*			sqlStatement.addFromExpression( schema + ".SRM_CASE_NEW_HANDLE_INFO info"
					+ " left join " + schema + ".BIM_CONTRACT contract on contract.CONTRACT_ID=info.CONTRACT_ID"
					+ " left join " + schema + ".PVM_APPLICATION application on application.APPLICATION_ID = info.SOFTWARE_VERSION"
					+ " left join " + schema + ".BIM_COMPANY company on company.COMPANY_ID = info.CUSTOMER_ID"
					+ " left join " + schema + ".BIM_COMPANY vendor on vendor.COMPANY_ID = info.COMPANY_ID"
					+ " left join " + schema + ".DMM_ASSET_TYPE assetEdc on assetEdc.ASSET_TYPE_ID = info.EDC_TYPE"
					+ " left join " + schema + ".DMM_ASSET_TYPE assetPer on assetPer.ASSET_TYPE_ID = info.PERIPHERALS"
					+ " left join " + schema + ".DMM_ASSET_TYPE assetPer2 on assetPer2.ASSET_TYPE_ID = info.PERIPHERALS2"
					+ " left join " + schema + ".DMM_ASSET_TYPE assetPer3 on assetPer3.ASSET_TYPE_ID = info.PERIPHERALS3"
					+ " left join " + schema + ".BASE_PARAMETER_ITEM_DEF item on item.BPTD_CODE = :double and item.ITEM_VALUE = info.MULTI_MODULE"
					+ " left join " + schema + ".BASE_PARAMETER_ITEM_DEF itemEcr on itemEcr.BPTD_CODE = :ecr and itemEcr.ITEM_VALUE = info.ECR_CONNECTION"
//					+ " left join " + schema + ".BASE_PARAMETER_ITEM_DEF itemConnec on itemConnec.BPTD_CODE = :connection and itemConnec.ITEM_VALUE = info.CONNECTION_TYPE"
					+ " left join " + schema + ".BIM_MERCHANT_HEADER header on header.MERCHANT_HEADER_ID = info.MERCHANT_HEADER_ID"
					+ " left join " + schema + ".BASE_PARAMETER_ITEM_DEF region on region.BPTD_CODE = :region and region.ITEM_VALUE = header.AREA"
					+ " left join " + schema + ".BIM_MERCHANT merchant on merchant.MERCHANT_ID = info.MERCHANT_CODE"
					+ " left join " + schema + ".BASE_PARAMETER_ITEM_DEF netVendor on netVendor.BPTD_CODE = :netVendor and netVendor.ITEM_VALUE = info.NET_VENDOR_ID"
					+ " left join " + schema + ".BIM_DEPARTMENT dept on dept.DEPT_CODE = info.DEPARTMENT_ID"
					+ " left join " + schema + ".BASE_PARAMETER_ITEM_DEF caseStatus on caseStatus.BPTD_CODE = :caseStatus and caseStatus.ITEM_VALUE = info.CASE_STATUS"
					+ " left join " + schema + ".SRM_CASE_NEW_ASSET_LINK assetLink on assetLink.CASE_ID = info.CASE_ID and assetLink.ITEM_TYPE = '10'"
					+ " left join " + schema + ".BASE_PARAMETER_ITEM_DEF ticketMode on ticketMode.BPTD_CODE = :ticketModeParam and ticketMode.ITEM_VALUE = info.CASE_TYPE"
					+ " left join " + schema + ".BASE_PARAMETER_ITEM_DEF deptCaseGroup on deptCaseGroup.BPTD_CODE = :caseGroupParam "
					+ " and deptCaseGroup.ITEM_VALUE = info.DEPARTMENT_ID ");*/
			if (StringUtils.hasText(dtid)) {
				sqlStatement.addWhereClause("info.DTID = :dtid", dtid);
			}
			if (StringUtils.hasText(customerId)) {
				sqlStatement.addWhereClause("info.CUSTOMER_ID = :customerId", customerId);
			}
			 // 改為抓處理中  如果處理中全部結案 抓最新資料檔 2018/01/04 Bug #3055
			if(isHave){
			//	sql.addWhereClause("caseInfo.CASE_CATEGORY = 'INSTALL' AND caseInfo.CASE_STATUS <> 'ImmediateClose' AND caseInfo.CASE_STATUS <> 'Closed'");
				//建案 不可引用待派工案件dtid CR #2551 2017/12/07// 改為抓處理中  如果處理中全部結案 抓最新資料檔 2018/01/04
				sqlStatement.addWhereClause("info.CASE_STATUS <>:caseStatus", IAtomsConstants.CASE_STATUS.WAIT_DISPATCH.getCode());
			//	sql.addWhereClause("NOT EXISTS (select 1 from dbo.SRM_CASE_NEW_HANDLE_INFO newIfo where newIfo.DTID = caseInfo.DTID)");
				builder.delete(0, builder.length());
				/*builder.append(" info.CASE_ID in (select max(caseInfo.CASE_ID) from ");
				builder.append(schema).append(".SRM_CASE_HANDLE_INFO caseInfo ");
				builder.append(" where NOT EXISTS(select 1 from ").append(schema).append(".SRM_CASE_NEW_HANDLE_INFO newInfo where newInfo.DTID = caseInfo.DTID) ");*/
//				builder.append(" and NOT EXISTS(select 1 from ").append(schema).append(".SRM_CASE_HANDLE_INFO caseInfo2 where caseInfo2.DTID =caseInfo.DTID and caseInfo2.CREATED_DATE > caseInfo.CREATED_DATE) ");
				
				/*builder.append(" and NOT EXISTS(select 1 from ").append(schema).append(".SRM_CASE_HANDLE_INFO caseInfo2 where caseInfo2.DTID =caseInfo.DTID and caseInfo2.CREATED_DATE > caseInfo.CREATED_DATE ");
				builder.append(" AND caseInfo2.CASE_CATEGORY = 'INSTALL' AND caseInfo2.CASE_STATUS not in ('ImmediateClose', 'Closed', 'Voided', 'Completed', 'WaitClose') )");
				
				builder.append(" AND caseInfo.CASE_CATEGORY = 'INSTALL' AND caseInfo.CASE_STATUS not in ('ImmediateClose', 'Closed', 'Voided', 'Completed', 'WaitClose')");
				builder.append(" group by caseInfo.DTID)");*/
				builder.append(" info.CASE_ID =(SELECT top 1 fo.CASE_ID FROM ").append(schema).append(".SRM_CASE_HANDLE_INFO fo WHERE 1=1 ");
				if (StringUtils.hasText(dtid)) {
					builder.append("AND fo.DTID ='").append(dtid).append("' ");
				}
				builder.append("AND fo.CASE_STATUS not in ('ImmediateClose', 'Closed', 'Voided', 'WaitDispatch') AND fo.CASE_CATEGORY <> 'OTHER' order BY fo.CREATED_DATE desc,fo.CASE_ID desc)");
				
				sqlStatement.addWhereClause(builder.toString());
			}
			
		//	sqlStatement.addWhereClause("info.CASE_ID not in(select caseInfo.CASE_ID from dbo.SRM_CASE_NEW_HANDLE_INFO caseInfo where caseInfo.CASE_STATUS != :close and caseInfo.CASE_STATUS != :immediateclose and caseInfo.CASE_CATEGORY = :caseCategory)");
			
			/*sqlStatement.addWhereClause("info.CASE_ID not in(select caseInfo.CASE_ID from dbo.SRM_CASE_NEW_HANDLE_INFO caseInfo where caseInfo.CASE_STATUS != :caseStatus and caseInfo.CASE_CATEGORY = :caseCategory)");
			sqlStatement.addWhereClause("assetLink.IS_LINK = 'Y'");*/
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
//			sqlQueryBean.setParameter("close", IAtomsConstants.CASE_STATUS.CLOSED.getCode());
//			sqlQueryBean.setParameter("immediateclose", IAtomsConstants.CASE_STATUS.IMMEDIATE_CLOSE.getCode());
//			sqlQueryBean.setParameter("caseCategory", IAtomsConstants.CASE_CATEGORY.INSTALL.getCode());
			
			/*sqlQueryBean.setParameter("caseStatus", IAtomsConstants.CASE_STATUS.CLOSED.getCode());
			sqlQueryBean.setParameter("caseCategory", IAtomsConstants.CASE_CATEGORY.INSTALL.getCode());*/
			sqlQueryBean.setParameter("double", IATOMS_PARAM_TYPE.DOUBLE_MODULE.getCode());
			sqlQueryBean.setParameter("ecr", IATOMS_PARAM_TYPE.ECR_LINE.getCode());
			sqlQueryBean.setParameter("connection", IATOMS_PARAM_TYPE.COMM_MODE.getCode());
			sqlQueryBean.setParameter("region", IATOMS_PARAM_TYPE.REGION.getCode());
			sqlQueryBean.setParameter("netVendor", IATOMS_PARAM_TYPE.NET_VENDOR.getCode());
			sqlQueryBean.setParameter("caseStatus", IATOMS_PARAM_TYPE.CASE_STATUS.getCode());
			sqlQueryBean.setParameter("supportedFunctionParam", IATOMS_PARAM_TYPE.SUPPORTED_FUNCTION.getCode());
			sqlQueryBean.setParameter("ticketModeParam", IATOMS_PARAM_TYPE.TICKET_MODE.getCode());
			sqlQueryBean.setParameter("caseGroupParam", IATOMS_PARAM_TYPE.CASE_GROUP.getCode());
			sqlQueryBean.setParameter("location", IATOMS_PARAM_TYPE.LOCATION.getCode());
			sqlQueryBean.setParameter("receiptType", IATOMS_PARAM_TYPE.RECEIPT_TYPE.getCode());
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseHandleInfoDTO.class);
			logger.debug(this.getClass().getName() + "SQL------->" + sqlQueryBean.toString());
			List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(caseHandleInfoDTOs)){
				return caseHandleInfoDTOs.get(0);
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ".getCaseHandleInfoDTOById() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseNewHandleInfoDAO#queryTransferNewCaseNum()
	 */
	@Override
	public List<Integer> queryTransferNewCaseNum() throws DataAccessException {
		List<Integer> result = null;
		try {
			String schema = this.getMySchema();
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append(" select count(1)");
			sqlQueryBean.append(" from "+schema + ".SRM_CASE_NEW_HANDLE_INFO");
			
			sqlQueryBean.append(" union all");
			
			sqlQueryBean.append(" select count(1)");
			sqlQueryBean.append(" from "+schema + ".SRM_CASE_NEW_ASSET_LINK");
			
			logger.debug("queryTransferNewCaseNum()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			return result;
		} catch (Exception e) {
			logger.error("queryTransferNewCaseNum()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
}
