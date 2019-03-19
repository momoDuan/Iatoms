package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.CoreMessageCode;
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

import com.cybersoft4u.xian.iatoms.common.bean.dto.ComplaintDTO;
import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmComplaintDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmComplaint;

/**
 * Purpose: 客訴管理DAO
 * @author	nicklin
 * @since	JDK 1.7
 * @date	2018/03/02
 * @MaintenancePersonnel cybersoft
 */
public class SrmComplaintDAO extends GenericBaseDAO<SrmComplaint> implements ISrmComplaintDAO{
	
	/**
	 * 系统log物件  
	 */
	private static CafeLog logger = CafeLogFactory.getLog(GenericConfigManager.DAO, SrmComplaintDAO.class);

	/**
	 * Constructor:無參數建構子
	 */
	public SrmComplaintDAO() {
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmComplaintDAO#listBy(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ComplaintDTO> listBy(String customerId, String merchantCode, String tid, String merchantName,
			String questionType, String vendor, String isCustomer, String isVendor, String startDate, 
			String endDate, Integer pageSize, Integer pageIndex, String sort, String orderby)
			throws DataAccessException {
		// TODO Auto-generated method stub
		logger.debug("SrmComplaintDAO.listBy.customer:'" + customerId + "'");
		logger.debug("SrmComplaintDAO.listBy.queryMerchantCode:'" + merchantCode + "'");
		logger.debug("SrmComplaintDAO.listBy.tid:'" + tid + "'");
		logger.debug("SrmComplaintDAO.listBy.merchantName:'" + merchantName + "'");
		logger.debug("SrmComplaintDAO.listBy.questionType:'" + questionType + "'");
		logger.debug("SrmComplaintDAO.listBy.vendor:'" + vendor + "'");
		logger.debug("SrmComplaintDAO.listBy.isCustomer:'" + isCustomer + "'");
		logger.debug("SrmComplaintDAO.listBy.isVendor:'" + isVendor + "'");
		logger.debug("SrmComplaintDAO.listBy.startDate:'" + startDate + "'");
		logger.debug("SrmComplaintDAO.listBy.endDate:'" + endDate + "'");
		logger.debug("SrmComplaintDAO.listBy.pageSize:'" + pageSize + "'");
		logger.debug("SrmComplaintDAO.listBy.pageIndex:'" + pageIndex + "'");
		logger.debug("SrmComplaintDAO.listBy.sort:'" + sort + "'");
		logger.debug("SrmComplaintDAO.listBy.orderby:'" + orderby + "'");
		SqlStatement sql = new SqlStatement();
		List<ComplaintDTO> complaintManageList = null;
		//得到schema
		String schema = this.getMySchema();
		try {
			sql.addSelectClause("compl.CASE_ID", ComplaintDTO.ATTRIBUTE.CASE_ID.getValue());
			sql.addSelectClause("compl.CUSTOMER_ID", ComplaintDTO.ATTRIBUTE.CUSTOMER_ID.getValue());
			sql.addSelectClause("company.SHORT_NAME", ComplaintDTO.ATTRIBUTE.CUSTOMER_NAME.getValue());
			sql.addSelectClause("compl.MERCHANT_ID", ComplaintDTO.ATTRIBUTE.MERCHANT_ID.getValue());
			sql.addSelectClause("mch.MERCHANT_CODE", ComplaintDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sql.addSelectClause("mch.NAME", ComplaintDTO.ATTRIBUTE.MERCHANT_NAME.getValue());
			sql.addSelectClause("compl.COMPANY_ID", ComplaintDTO.ATTRIBUTE.COMPANY_ID.getValue());
			sql.addSelectClause("vendor.SHORT_NAME", ComplaintDTO.ATTRIBUTE.COMPANY_NAME.getValue());
			sql.addSelectClause("compl.USER_NAME", ComplaintDTO.ATTRIBUTE.USER_NAME.getValue());
			sql.addSelectClause("compl.COMPLAINT_STAFF", ComplaintDTO.ATTRIBUTE.COMPLAINT_STAFF.getValue());
			sql.addSelectClause("compl.TID", ComplaintDTO.ATTRIBUTE.TID.getValue());
			sql.addSelectClause("compl.COMPLAINT_CONTENT", ComplaintDTO.ATTRIBUTE.COMPLAINT_CONTENT.getValue());
			sql.addSelectClause("compl.HANDLE_CONTENT", ComplaintDTO.ATTRIBUTE.HANDLE_CONTENT.getValue());
			sql.addSelectClause("compl.COMPLAINT_DATE", ComplaintDTO.ATTRIBUTE.COMPLAINT_DATE.getValue());
			sql.addSelectClause("compl.CONTACT_WAY", ComplaintDTO.ATTRIBUTE.CONTACT_WAY.getValue());
			sql.addSelectClause("compl.IS_VIP", ComplaintDTO.ATTRIBUTE.IS_VIP.getValue());
			sql.addSelectClause("compl.QUESTION_TYPE", ComplaintDTO.ATTRIBUTE.QUESTION_TYPE.getValue());
			sql.addSelectClause("base.ITEM_NAME", ComplaintDTO.ATTRIBUTE.QUESTION_TYPE_NAME.getValue());
			sql.addSelectClause("compl.IS_CUSTOMER", ComplaintDTO.ATTRIBUTE.IS_CUSTOMER.getValue());
			sql.addSelectClause("compl.CUSTOMER_AMOUNT", ComplaintDTO.ATTRIBUTE.CUSTOMER_AMOUNT.getValue());
			sql.addSelectClause("compl.IS_VENDOR", ComplaintDTO.ATTRIBUTE.IS_VENDOR.getValue());
			sql.addSelectClause("compl.VENDOR_AMOUNT", ComplaintDTO.ATTRIBUTE.VENDOR_AMOUNT.getValue());
			sql.addSelectClause("compl.DELETED", ComplaintDTO.ATTRIBUTE.DELETED.getValue());
			sql.addSelectClause("compl.CREATED_DATE", ComplaintDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sql.setFromExpression(" " + schema + ".SRM_COMPLAINT_CASE compl " +
				"left join " + schema + ".BASE_PARAMETER_ITEM_DEF base on base.BPTD_CODE=:questionTypeName and base.ITEM_VALUE=compl.QUESTION_TYPE " +
				"left join " + schema + ".BIM_COMPANY company on compl.CUSTOMER_ID=company.COMPANY_ID " +
				"left join " + schema + ".BIM_MERCHANT mch on compl.MERCHANT_ID=mch.MERCHANT_ID " +
				"left join " + schema + ".BIM_COMPANY vendor on compl.COMPANY_ID=vendor.COMPANY_ID ");
			//客戶
			if (StringUtils.hasText(customerId)) {
				sql.addWhereClause(" compl.CUSTOMER_ID = :customerId", customerId);
			}
			//特店代號
			if (StringUtils.hasText(merchantCode)) {
				sql.addWhereClause(" mch.MERCHANT_CODE like :merchantCode", IAtomsConstants.MARK_PERCENT + merchantCode + IAtomsConstants.MARK_PERCENT);
			}
			//TID/DTID
			if (StringUtils.hasText(tid)) {
				sql.addWhereClause(" compl.TID like :tid", tid + IAtomsConstants.MARK_PERCENT);
			}
			//特店名稱
			if (StringUtils.hasText(merchantName)) {
				sql.addWhereClause(" mch.NAME like :merchantName", IAtomsConstants.MARK_PERCENT + merchantName + IAtomsConstants.MARK_PERCENT);
			}
			//問題分類
			if (StringUtils.hasText(questionType)) {
				sql.addWhereClause(" compl.QUESTION_TYPE = :questionType", questionType);
			}
			//歸責廠商
			if (StringUtils.hasText(vendor)) {
				sql.addWhereClause(" compl.COMPANY_ID = :vendor", vendor);
			}
			//賠償客戶
			if (StringUtils.hasText(isCustomer)) {
				sql.addWhereClause(" compl.IS_CUSTOMER = :isCustomer", isCustomer);
			}
			//廠商罰款
			if (StringUtils.hasText(isVendor)) {
				sql.addWhereClause(" compl.IS_VENDOR = :isVendor", isVendor);
			}
			//發生日期起
			if (StringUtils.hasText(startDate)) {
				sql.addWhereClause(" convert(varchar(10), compl.COMPLAINT_DATE, 111) >= :startDate", startDate);
			}
			//發生日期迄
			if (StringUtils.hasText(endDate)) {
				sql.addWhereClause(" convert(varchar(10), compl.COMPLAINT_DATE, 111) <= :endDate", endDate);
			}
			//設置排序表達式
			if (StringUtils.hasText(sort) && StringUtils.hasText(orderby)){
				sql.setOrderByExpression(sort + " " + orderby);
			} else {
				sql.setOrderByExpression("compl.COMPLAINT_DATE desc");
			}
			//判斷刪除標誌
			sql.addWhereClause("ISNULL(compl.DELETED, 'N') =:deleted", IAtomsConstants.NO);
			sql.setPageSize(pageSize);
			sql.setStartPage(pageIndex - 1);
			//創建SqlQueryBean
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			sqlQueryBean.setParameter("questionTypeName", IATOMS_PARAM_TYPE.QUESTION_TYPE.getCode());
			//記錄sql語句
			logger.debug(this.getClass().getName() + ".listBy()", "sql:" + sqlQueryBean.toString());
			AliasBean aliasBean = sql.createAliasBean(ComplaintDTO.class);
			complaintManageList = (List<ComplaintDTO>) super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch(Exception e) {
			logger.error(this.getClass().getName() + ":Failed-- listBy()---" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_SRM_COMPLAINT_CASE)}, e);
		}
		return complaintManageList;
	}

	/** 
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmComplaintDAO#count(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Integer count(String queryCustomerId, String queryMerchantCode, String queryTid, String queryMerchantName,
			String queryQuestionType, String queryVendor, String queryIsCustomer, String queryIsVendor,
			String queryStartDate, String queryEndDate)
			throws DataAccessException {
		// TODO Auto-generated method stub
		logger.debug("SrmComplaintDAO.count.queryCustomerId:'" + queryCustomerId + "'");
		logger.debug("SrmComplaintDAO.count.queryMerchantCode:'" + queryMerchantCode + "'");
		logger.debug("SrmComplaintDAO.count.queryTid:'" + queryTid + "'");
		logger.debug("SrmComplaintDAO.count.queryMerchantName:'" + queryMerchantName + "'");
		logger.debug("SrmComplaintDAO.count.queryQuestionType:'" + queryQuestionType + "'");
		logger.debug("SrmComplaintDAO.count.queryVendor:'" + queryVendor + "'");
		logger.debug("SrmComplaintDAO.count.queryIsCustomer:'" + queryIsCustomer + "'");
		logger.debug("SrmComplaintDAO.count.queryIsVendor:'" + queryIsVendor + "'");
		logger.debug("SrmComplaintDAO.count.queryStartDate:'" + queryStartDate + "'");
		logger.debug("SrmComplaintDAO.count.queryEndDate:'" + queryEndDate + "'");
		try {
			String schema = this.getMySchema();
			SqlStatement sql = new SqlStatement();		
			sql.addSelectClause("count(1)");
			sql.setFromExpression(schema + ".SRM_COMPLAINT_CASE compl " +
			"left join " + schema + ".BIM_MERCHANT mch on compl.MERCHANT_ID=mch.MERCHANT_ID ");
			//客訴案號
			if (StringUtils.hasText(queryCustomerId)) {
				sql.addWhereClause("compl.CUSTOMER_ID = :queryCustomerId", queryCustomerId);
			}
			//特店代號
			if (StringUtils.hasText(queryMerchantCode)) {
				sql.addWhereClause("mch.MERCHANT_CODE like :queryMerchantCode", IAtomsConstants.MARK_PERCENT + queryMerchantCode + IAtomsConstants.MARK_PERCENT);
			}
			//TID
			if (StringUtils.hasText(queryTid)) {
				sql.addWhereClause("compl.TID like :queryTid", queryTid + IAtomsConstants.MARK_PERCENT);
			}
			//特店名稱
			if (StringUtils.hasText(queryMerchantName)) {
				sql.addWhereClause("mch.NAME like :queryMerchantName", IAtomsConstants.MARK_PERCENT + queryMerchantName + IAtomsConstants.MARK_PERCENT);
			}
			//問題分類
			if (StringUtils.hasText(queryQuestionType)) {
				sql.addWhereClause("compl.QUESTION_TYPE = :queryQuestionType", queryQuestionType);
			}
			//歸責廠商
			if (StringUtils.hasText(queryVendor)) {
				sql.addWhereClause("compl.COMPANY_ID = :queryVendor", queryVendor);
			}
			//賠償客戶
			if (StringUtils.hasText(queryIsCustomer)) {
				sql.addWhereClause("compl.IS_CUSTOMER = :queryIsCustomer", queryIsCustomer);
			}
			//賠償客戶
			if (StringUtils.hasText(queryIsVendor)) {
				sql.addWhereClause("compl.IS_VENDOR = :queryIsVendor", queryIsVendor);
			}
			//發生日期起
			if (StringUtils.hasText(queryStartDate)) {
				sql.addWhereClause(" convert(varchar(10), compl.COMPLAINT_DATE, 111) >= :queryStartDate", queryStartDate);
			}
			//發生日期迄
			if (StringUtils.hasText(queryEndDate)) {
				sql.addWhereClause(" convert(varchar(10), compl.COMPLAINT_DATE, 111) <= :queryEndDate", queryEndDate);
			}
			//判斷刪除標誌
			sql.addWhereClause("ISNULL(compl.DELETED, 'N') !=:deleted", IAtomsConstants.YES);
			//記錄語句
			logger.debug(this.getClass().getName() + ".count()", "sql:" + sql.toString());
			//創建SqlQueryBean
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				return result.get(0).intValue();
			}
			return 0;
		} catch(Exception e) {
			logger.error(this.getClass().getName() + ".count() is error " + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_SRM_COMPLAINT_CASE)}, e);
		}
	}



	/** 
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmComplaintDAO#getCaseInfoById(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ComplaintDTO> getCaseInfoById(String caseId)
			throws DataAccessException {
		// TODO Auto-generated method stub
		logger.debug("SrmComplaintDAO.getCaseInfoById.caseId:'" + caseId + "'");
		String schema = this.getMySchema();
		SqlStatement sql = new SqlStatement();
		try {
			sql.addSelectClause("compl.CASE_ID", ComplaintDTO.ATTRIBUTE.CASE_ID.getValue());
			sql.addSelectClause("compl.CUSTOMER_ID", ComplaintDTO.ATTRIBUTE.CUSTOMER_ID.getValue());
			sql.addSelectClause("company.SHORT_NAME", ComplaintDTO.ATTRIBUTE.CUSTOMER_NAME.getValue());
			sql.addSelectClause("compl.MERCHANT_ID", ComplaintDTO.ATTRIBUTE.MERCHANT_ID.getValue());
			sql.addSelectClause("mch.MERCHANT_CODE", ComplaintDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sql.addSelectClause("mch.NAME", ComplaintDTO.ATTRIBUTE.MERCHANT_NAME.getValue());
			sql.addSelectClause("compl.COMPANY_ID", ComplaintDTO.ATTRIBUTE.COMPANY_ID.getValue());
			sql.addSelectClause("vendor.SHORT_NAME", ComplaintDTO.ATTRIBUTE.COMPANY_NAME.getValue());
			sql.addSelectClause("compl.USER_NAME", ComplaintDTO.ATTRIBUTE.USER_NAME.getValue());
			sql.addSelectClause("compl.COMPLAINT_STAFF", ComplaintDTO.ATTRIBUTE.COMPLAINT_STAFF.getValue());
			sql.addSelectClause("compl.TID", ComplaintDTO.ATTRIBUTE.TID.getValue());
			sql.addSelectClause("compl.COMPLAINT_CONTENT", ComplaintDTO.ATTRIBUTE.COMPLAINT_CONTENT.getValue());
			sql.addSelectClause("compl.HANDLE_CONTENT", ComplaintDTO.ATTRIBUTE.HANDLE_CONTENT.getValue());
			sql.addSelectClause("compl.CREATED_DATE", ComplaintDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sql.addSelectClause("compl.CREATED_BY_ID", ComplaintDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			sql.addSelectClause("compl.CREATED_BY_NAME", ComplaintDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			sql.addSelectClause("compl.UPDATED_DATE", ComplaintDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			sql.addSelectClause("compl.UPDATED_BY_ID", ComplaintDTO.ATTRIBUTE.UPDATED_BY_ID.getValue());
			sql.addSelectClause("compl.UPDATED_BY_NAME", ComplaintDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			sql.addSelectClause("compl.COMPLAINT_DATE", ComplaintDTO.ATTRIBUTE.COMPLAINT_DATE.getValue());
			sql.addSelectClause("compl.CONTACT_WAY", ComplaintDTO.ATTRIBUTE.CONTACT_WAY.getValue());
			sql.addSelectClause("compl.IS_VIP", ComplaintDTO.ATTRIBUTE.IS_VIP.getValue());
			sql.addSelectClause("compl.QUESTION_TYPE", ComplaintDTO.ATTRIBUTE.QUESTION_TYPE.getValue());
			sql.addSelectClause("base.ITEM_NAME", ComplaintDTO.ATTRIBUTE.QUESTION_TYPE_NAME.getValue());
			sql.addSelectClause("compl.IS_CUSTOMER", ComplaintDTO.ATTRIBUTE.IS_CUSTOMER.getValue());
			sql.addSelectClause("compl.CUSTOMER_AMOUNT", ComplaintDTO.ATTRIBUTE.CUSTOMER_AMOUNT.getValue());
			sql.addSelectClause("compl.IS_VENDOR", ComplaintDTO.ATTRIBUTE.IS_VENDOR.getValue());
			sql.addSelectClause("compl.VENDOR_AMOUNT", ComplaintDTO.ATTRIBUTE.VENDOR_AMOUNT.getValue());
			sql.addSelectClause("compl.DELETED", ComplaintDTO.ATTRIBUTE.DELETED.getValue());
			sql.setFromExpression(" " + schema + ".SRM_COMPLAINT_CASE compl " +
				"left join " + schema + ".BASE_PARAMETER_ITEM_DEF base on base.BPTD_CODE=:questionType and base.ITEM_VALUE=compl.QUESTION_TYPE " +
				"left join " + schema + ".BIM_COMPANY company on compl.CUSTOMER_ID=company.COMPANY_ID " +
				"left join " + schema + ".BIM_MERCHANT mch on compl.MERCHANT_ID=mch.MERCHANT_ID " +
				"left join " + schema + ".BIM_COMPANY vendor on compl.COMPANY_ID=vendor.COMPANY_ID ");
			if (StringUtils.hasText(caseId)) {
				sql.addWhereClause("compl.CASE_ID in ( :caseId)");
			}
			//判斷刪除標誌
			sql.addWhereClause("ISNULL(compl.DELETED, 'N') =:deleted", IAtomsConstants.NO);
			
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			sqlQueryBean.setParameter("questionType", IATOMS_PARAM_TYPE.QUESTION_TYPE.getCode());
			sqlQueryBean.setParameter("caseId", StringUtils.toList(caseId, IAtomsConstants.MARK_SEPARATOR));
			
			AliasBean aliasBean = sql.createAliasBean(ComplaintDTO.class);
			logger.debug("getCaseInfoById()", "sql:" + sqlQueryBean.toString());
			List<ComplaintDTO> complaintManageList = (List<ComplaintDTO>) super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if(!CollectionUtils.isEmpty(complaintManageList)) {
				return complaintManageList;
			}
		} catch(Exception e) {
			logger.error(this.getClass().getName() + ".getCaseInfoById() is error " + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_SRM_COMPLAINT_CASE)}, e);
		}
		return null;
	}
	
}
