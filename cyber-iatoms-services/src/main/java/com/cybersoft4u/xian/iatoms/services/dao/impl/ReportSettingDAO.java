package com.cybersoft4u.xian.iatoms.services.dao.impl;
import java.util.List;

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
import com.cybersoft4u.xian.iatoms.common.bean.dto.ReportSettingDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ReportSettingFormDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IReportSettingDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimReportSetting;

/**
 * Purpose: 報表發送設定DAO
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2016年7月4日
 * @MaintenancePersonnel ElvaHe
 */
public class ReportSettingDAO extends GenericBaseDAO<BimReportSetting> implements IReportSettingDAO {
	
	/**
	 * 日志記錄物件  
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.DAO, ReportSettingDAO.class);
	

/*	*//**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IReportSettingDAO#listBy(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String)
	 *//*
	@Override
	public List<ReportSettingDTO> listBy(String queryCustomerId, String queryReportCode, Integer pageSize, Integer pageIndex, String sort, String order)throws DataAccessException {
		//打印參數
		LOGGER.debug("listBy()", "parameters : queryCustomerId= ", queryCustomerId);
		LOGGER.debug("listBy()", "parameters : queryReportCode= ", queryReportCode);
		LOGGER.debug("listBy()", "parameters : pageSize= ", String.valueOf(pageSize));
		LOGGER.debug("listBy()", "parameters : pageIndex= ", String.valueOf(pageIndex));
		LOGGER.debug("listBy()", "parameters : sort= ", sort);
		LOGGER.debug("listBy()", "parameters : order= ", order);
		List<ReportSettingDTO> reportSettingDTOs = null;
		//子表
		SqlStatement sqlStatement = new SqlStatement();
		//主表
		SqlStatement sql = new SqlStatement();
		String schema = this.getMySchema();
		StringBuffer stringBuffer = new StringBuffer();
		try {
			//拼接子表SQL
			sqlStatement.addSelectClause(" report.SETTING_ID", ReportSettingDTO.ATTRIBUTE.SETTING_ID.getValue());
			sqlStatement.addSelectClause(" customer.COMPANY_ID", ReportSettingDTO.ATTRIBUTE.COMPANY_ID.getValue());
			sqlStatement.addSelectClause(" customer.SHORT_NAME", ReportSettingDTO.ATTRIBUTE.CUSTOMER_NAME.getValue());
			sqlStatement.addSelectClause(" report.REPORT_CODE", ReportSettingDTO.ATTRIBUTE.REPORT_CODE.getValue());
			sqlStatement.addSelectClause(" baseCode.ITEM_NAME", ReportSettingDTO.ATTRIBUTE.REPORT_NAME.getValue());
			sqlStatement.addSelectClause(" report.RECIPIENT", ReportSettingDTO.ATTRIBUTE.RECIPIENT.getValue());
			sqlStatement.addSelectClause(" baseDetail.ITEM_NAME", ReportSettingDTO.ATTRIBUTE.REPORT_DETAIL.getValue());
			sqlStatement.addSelectClause(" detail.REPORT_DETAIL", ReportSettingDTO.ATTRIBUTE.REPORT_DETAIL_ID.getValue());
			sqlStatement.addSelectClause(" report.COPY", ReportSettingDTO.ATTRIBUTE.COPY.getValue());
			sqlStatement.addSelectClause(" report.REMARK", ReportSettingDTO.ATTRIBUTE.REMARK.getValue());
			sqlStatement.addFromExpression(
					schema + ".BIM_REPORT_SETTING report LEFT JOIN " 
			      + schema + ".BIM_REPORT_SETTING_DETAIL detail ON report.SETTING_ID = detail.SETTING_ID LEFT JOIN " 
			      + schema + ".BIM_COMPANY customer ON report.COMPANY_ID  = customer.COMPANY_ID LEFT JOIN " 
			      + schema + ".BASE_PARAMETER_ITEM_DEF baseCode ON baseCode.BPTD_CODE = :bptdReportCode AND report.REPORT_CODE = baseCode.ITEM_VALUE LEFT JOIN " 
			      + schema + ".BASE_PARAMETER_ITEM_DEF baseDetail ON baseDetail.BPTD_CODE = :bptdReportDetail AND detail.REPORT_DETAIL = baseDetail.ITEM_VALUE AND baseDetail.PARENT_BPID_ID = 'REPORT_CODE_01'");
			stringBuffer.append(schema).append(".BIM_REPORT_SETTING report LEFT JOIN ");
			stringBuffer.append(schema).append(".BIM_REPORT_SETTING_DETAIL detail ON report.SETTING_ID = detail.SETTING_ID LEFT JOIN ");
			stringBuffer.append(schema).append(".BIM_COMPANY customer ON report.COMPANY_ID  = customer.COMPANY_ID LEFT JOIN ");
			stringBuffer.append(schema).append(".BASE_PARAMETER_ITEM_DEF baseCode ON baseCode.BPTD_CODE = :bptdReportCode AND report.REPORT_CODE = baseCode.ITEM_VALUE LEFT JOIN ");
			stringBuffer.append(schema).append(".BASE_PARAMETER_ITEM_DEF baseDetail ON baseDetail.BPTD_CODE = :bptdReportDetail AND detail.REPORT_DETAIL = baseDetail.ITEM_VALUE AND baseDetail.PARENT_BPID_ID = 'REPORT_CODE_01'");
			sqlStatement.addFromExpression(stringBuffer.toString());
			//若用戶選擇了客戶就拼接關於客戶的查詢條件
			if(StringUtils.hasText(queryCustomerId)){
				sqlStatement.addWhereClause(" report.COMPANY_ID =:customer", queryCustomerId);
			}
			//若用戶選擇了報表名稱就拼接關於報表的查詢條件
			if(StringUtils.hasText(queryReportCode)){
				sqlStatement.addWhereClause(" report.REPORT_CODE =:reportCode", queryReportCode);
				//查詢條件為第一個報表時 
//				if (IAtomsConstants.LEAVE_CASE_STATUS_ONE.equals(queryReportCode)) {
//					sqlStatement.addWhereClause(" baseDetail.PARENT_BPID_ID = :parentBptdId", IATOMS_PARAM_TYPE.REPORT_CODE.getCode() + "_" + IAtomsConstants.PROJECT_STATUS_OPEN);
//				} else if(IAtomsConstants.LEAVE_CASE_STATUS_TWELVE.equals(queryReportCode)) {
//					sqlStatement.addWhereClause(" baseDetail.PARENT_BPID_ID = :parentBptdId", IATOMS_PARAM_TYPE.REPORT_CODE.getCode() + "_" + queryReportCode);
//				}
			}
//			else {
//				sqlStatement.addWhereClause(" baseDetail.PARENT_BPID_ID = :parentBptdId", IATOMS_PARAM_TYPE.REPORT_CODE.getCode() + "_" + IAtomsConstants.PROJECT_STATUS_OPEN);
//			}
			//打印子表的SQL語句
			LOGGER.debug("listBy()", "子表SQL ： ", sqlStatement.toString());
			//拼接主表的SQL
			sql.addSelectClause(
					" a.settingId, a.companyId, a.customerName, a.reportCode, a.reportName, a.recipient, a.reportDetail, a.reportDetatilId, a.copy, a.remark");
			//sql.addFromExpression("("+ sqlStatement.toString() +") a");
			stringBuffer.delete(0, stringBuffer.length());
			stringBuffer.append("(").append(sqlStatement.toString()).append(") a");
			sql.addFromExpression(stringBuffer.toString());
			//排序方式
			if ((StringUtils.hasText(sort)) && (StringUtils.hasText(order))) {
				//sql.setOrderByExpression(sort + IAtomsConstants.MARK_SPACE + order);
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append(sort).append(IAtomsConstants.MARK_SPACE).append(order);
				sql.setOrderByExpression(stringBuffer.toString());
			} else {
				//sql.setOrderByExpression( ReportSettingFormDTO.PARAM_SORT_NAME + IAtomsConstants.MARK_SPACE  +IAtomsConstants.PARAM_PAGE_ORDER);
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append(ReportSettingFormDTO.PARAM_SORT_NAME).append(IAtomsConstants.MARK_SPACE).append(IAtomsConstants.PARAM_PAGE_ORDER);
				sql.setOrderByExpression(stringBuffer.toString());
			}
			//打印SQL語句
			LOGGER.debug("listBy()", "SQL : ", sql.toString());
			//分頁
			sql.setPageSize(pageSize.intValue());
			sql.setStartPage(pageIndex.intValue() - 1);
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			sqlQueryBean.setParameter("bptdReportCode", IATOMS_PARAM_TYPE.REPORT_CODE.getCode());
			sqlQueryBean.setParameter("bptdReportDetail", IATOMS_PARAM_TYPE.REPORT_DETAIL.getCode());
			sqlQueryBean.setParameter("customer", queryCustomerId);
			sqlQueryBean.setParameter("reportCode", queryReportCode);
			AliasBean aliasBean = sql.createAliasBean(ReportSettingDTO.class);
			reportSettingDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listBy()", "error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_REPORT_SETTING)}, e);
		}
		return reportSettingDTOs;
	}*/

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IReportSettingDAO#listByReportCode(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@Override
	public List<ReportSettingDTO> listByReportCode(String queryCustomerId, String queryReportCode, Integer pageSize, Integer pageIndex, String sort, String order)throws DataAccessException {
		//打印參數
		LOGGER.debug("listBy()", "parameters : queryCustomerId= ", queryCustomerId);
		LOGGER.debug("listBy()", "parameters : queryReportCode= ", queryReportCode);
		LOGGER.debug("listBy()", "parameters : pageSize= ", String.valueOf(pageSize));
		LOGGER.debug("listBy()", "parameters : pageIndex= ", String.valueOf(pageIndex));
		LOGGER.debug("listBy()", "parameters : sort= ", sort);
		LOGGER.debug("listBy()", "parameters : order= ", order);
		List<ReportSettingDTO> reportCodeList = null;
		//子表
		SqlStatement sqlStatement = new SqlStatement();
		//主表
		String schema = this.getMySchema();
		StringBuffer stringBuffer = new StringBuffer();
		try {
			sqlStatement.addSelectClause(" report.SETTING_ID", ReportSettingDTO.ATTRIBUTE.SETTING_ID.getValue());
			sqlStatement.addSelectClause(" customer.COMPANY_ID", ReportSettingDTO.ATTRIBUTE.COMPANY_ID.getValue());
			sqlStatement.addSelectClause(" customer.SHORT_NAME", ReportSettingDTO.ATTRIBUTE.CUSTOMER_NAME.getValue());
			sqlStatement.addSelectClause(" customer.COMPANY_CODE", ReportSettingDTO.ATTRIBUTE.CUSTOMER_CODE.getValue());
			sqlStatement.addSelectClause(" report.REPORT_CODE", ReportSettingDTO.ATTRIBUTE.REPORT_CODE.getValue());
			sqlStatement.addSelectClause(" baseCode.ITEM_NAME", ReportSettingDTO.ATTRIBUTE.REPORT_NAME.getValue());
			sqlStatement.addSelectClause(" report.RECIPIENT", ReportSettingDTO.ATTRIBUTE.RECIPIENT.getValue());
			sqlStatement.addSelectClause(" report.COPY", ReportSettingDTO.ATTRIBUTE.COPY.getValue());
			sqlStatement.addSelectClause(" report.REMARK", ReportSettingDTO.ATTRIBUTE.REMARK.getValue());
			sqlStatement.addSelectClause("STUFF((SELECT ','+ ltrim(detail.REPORT_DETAIL) FROM dbo.BIM_REPORT_SETTING_DETAIL detail where detail.SETTING_ID = report.SETTING_ID FOR XML PATH('')), 1, 1, '')", ReportSettingDTO.ATTRIBUTE.REPORT_DETAIL_ID.getValue());
			/*sqlStatement.addFromExpression(schema + ".BIM_REPORT_SETTING report LEFT JOIN " 
			      + schema + ".BIM_COMPANY customer ON report.COMPANY_ID  = customer.COMPANY_ID LEFT JOIN " 
			      + schema + ".BASE_PARAMETER_ITEM_DEF baseCode ON baseCode.BPTD_CODE = :bptdReportCode AND report.REPORT_CODE = baseCode.ITEM_VALUE " );*/
			stringBuffer.append(schema).append(".BIM_REPORT_SETTING report LEFT JOIN ");
			stringBuffer.append(schema).append(".BIM_COMPANY customer ON report.COMPANY_ID  = customer.COMPANY_ID LEFT JOIN ");
			stringBuffer.append(schema).append(".BASE_PARAMETER_ITEM_DEF baseCode ON baseCode.BPTD_CODE = :bptdReportCode AND report.REPORT_CODE = baseCode.ITEM_VALUE ");
			sqlStatement.addFromExpression(stringBuffer.toString());
			//若用戶選擇了客戶就拼接關於客戶的查詢條件
			if(StringUtils.hasText(queryCustomerId)){
				sqlStatement.addWhereClause(" report.COMPANY_ID =:customer", queryCustomerId);
			}
			//若用戶選擇了報表名稱就拼接關於報表的查詢條件
			if(StringUtils.hasText(queryReportCode)){
				sqlStatement.addWhereClause(" report.REPORT_CODE =:reportCode", queryReportCode);
			}
			//排序方式
			if ((StringUtils.hasText(sort)) && (StringUtils.hasText(order))) {
				//sqlStatement.setOrderByExpression(sort + IAtomsConstants.MARK_SPACE + order);
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append(sort).append(IAtomsConstants.MARK_SPACE).append(order);
				sqlStatement.setOrderByExpression(stringBuffer.toString());
			} else {
				//sqlStatement.setOrderByExpression(ReportSettingFormDTO.PARAM_SORT_NAME + IAtomsConstants.MARK_SPACE + IAtomsConstants.PARAM_PAGE_ORDER);
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append(ReportSettingFormDTO.PARAM_SORT_NAME).append(IAtomsConstants.MARK_SPACE).append(IAtomsConstants.PARAM_PAGE_ORDER);
				sqlStatement.setOrderByExpression(stringBuffer.toString());
			}
			//打印SQL語句
			LOGGER.debug("listBy()", "SQL : ", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			// 分頁設置
			sqlQueryBean.setPageSize(pageSize.intValue());
			sqlQueryBean.setStartPage(pageIndex.intValue() - 1);
			sqlQueryBean.setParameter("bptdReportCode", IATOMS_PARAM_TYPE.REPORT_CODE.getCode());
			AliasBean aliasBean = sqlStatement.createAliasBean(ReportSettingDTO.class);
			reportCodeList = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listBy()", "is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_REPORT_SETTING)}, e);
		}
		return reportCodeList;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IReportSettingDAO#getCount(java.lang.String,java.lang.String)
	 *//*
	@Override
	public Integer count(String queryCustomerId, String queryReportCode) throws DataAccessException {
		//打印傳遞的參數
		LOGGER.debug("getCount()", "parameter : queryCustomerId= ", queryCustomerId);
		LOGGER.debug("getCount()", "parameter : queryReportCode= ", queryReportCode);
		SqlStatement sqlStatement = new SqlStatement();
		String schema = this.getMySchema();
		Integer count = null;
		StringBuffer stringBuffer = new StringBuffer();
		try {
			//拼接SQL
			sqlStatement.addSelectClause(" count(1) ");
			sqlStatement.addFromExpression(
					schema + ".BIM_REPORT_SETTING report LEFT JOIN " 
					+ schema +".BIM_REPORT_SETTING_DETAIL detail ON report.SETTING_ID = detail.SETTING_ID" );
			stringBuffer.append(schema).append(".BIM_REPORT_SETTING report LEFT JOIN ").append(schema).append(".BIM_REPORT_SETTING_DETAIL detail ON report.SETTING_ID = detail.SETTING_ID");
			sqlStatement.addFromExpression(stringBuffer.toString());
			//拼接查詢條件
			if(StringUtils.hasText(queryCustomerId)){
				sqlStatement.addWhereClause(" report.COMPANY_ID =:customer", queryCustomerId);
			}
			if(StringUtils.hasText(queryReportCode)){
				sqlStatement.addWhereClause(" report.REPORT_CODE =:reportCode", queryReportCode);
			}
			LOGGER.debug("count()", "SQL : ", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			//將結果先存放在一個集合中
			List<Integer> integers = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			//若存放結果的集合不為空
			if(!CollectionUtils.isEmpty(integers)){
				//獲取查詢結果
				count = integers.get(0);
			}
		} catch (Exception e) {
			LOGGER.error("count()", "is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_REPORT_SETTING)}, e);
		}
		return count;
	}*/
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IReportSettingDAO#codeCount(java.lang.String, java.lang.String)
	 */
	@Override
	public Integer codeCount(String queryCustomerId, String queryReportCode) throws DataAccessException {
		//打印傳遞的參數
		LOGGER.debug("codeCount()", "parameter : queryCustomerId= ", queryCustomerId);
		LOGGER.debug("codeCount()", "parameter : queryReportCode= ", queryReportCode);
		SqlStatement sqlStatement = new SqlStatement();
		String schema = this.getMySchema();
		Integer count = null;
		StringBuffer stringBuffer = new StringBuffer();
		try {
			//拼接SQL
			sqlStatement.addSelectClause(" count(1) ");
			//sqlStatement.addFromExpression(schema + ".BIM_REPORT_SETTING report" );
			stringBuffer.append(schema).append(".BIM_REPORT_SETTING report");
			sqlStatement.addFromExpression(stringBuffer.toString());
			//拼接查詢條件
			if(StringUtils.hasText(queryCustomerId)){
				sqlStatement.addWhereClause(" report.COMPANY_ID =:customer", queryCustomerId);
			}
			if(StringUtils.hasText(queryReportCode)){
				sqlStatement.addWhereClause(" report.REPORT_CODE =:reportCode", queryReportCode);
			}
			LOGGER.debug("count()", "SQL : ", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			//將結果先存放在一個集合中
			List<Integer> integers = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			//若存放結果的集合不為空
			if(!CollectionUtils.isEmpty(integers)){
				//獲取查詢結果
				count = integers.get(0);
			}
		} catch (Exception e) {
			LOGGER.error("count()", "is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_REPORT_SETTING)}, e);
		}
		return count;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IReportSettingDAO#getPreReportCodeList(java.lang.String)
	 */
	public List<Parameter> getPreReportCodeList(String companyId) throws DataAccessException{
		//存放報表名稱的列表
		List<Parameter> reportLists= null;
		try {
		//打印傳遞的客戶編號
		LOGGER.debug("getPreReportCodeList()", "parameter : companyId= ", companyId);
		SqlStatement sqlStatement = new SqlStatement();
		//子表
		SqlStatement sql = new SqlStatement();
		StringBuffer stringBuffer = new StringBuffer();
		String schema = this.getMySchema();
		//拼接子表的SQL語句
		sql.addSelectClause(" setting.REPORT_CODE");
		/*sql.addFromExpression(
				schema + ".BIM_REPORT_SETTING setting");*/
		stringBuffer.append(schema).append(".BIM_REPORT_SETTING setting");
		sql.addFromExpression(stringBuffer.toString());
		if (StringUtils.hasText(companyId)) {
			sql.addWhereClause("setting.COMPANY_ID = :companyId");
		}
		//打印子表SQL語句
		LOGGER.debug("getPreReportCodeList()", "子表SQL : ", sql.toString());
		//拼接主表的SQL語句
		sqlStatement.addSelectClause("base.ITEM_VALUE ", Parameter.FIELD_VALUE);
		sqlStatement.addSelectClause("base.ITEM_NAME", Parameter.FIELD_NAME);
		//sqlStatement.addFromExpression(schema + ".BASE_PARAMETER_ITEM_DEF base");
		stringBuffer.delete(0, stringBuffer.length());
		stringBuffer.append(schema).append(".BASE_PARAMETER_ITEM_DEF base");
		sqlStatement.addFromExpression(stringBuffer.toString());
		/*sqlStatement.addWhereClause(
				" base.BPTD_CODE = :bptdCode AND base.ITEM_VALUE NOT IN(" + sql.toString() + ") AND isnull(base.APPROVED_FLAG,'N') = :approvedFlag AND isnull(base.DELETED,'N') = :deleted");*/
		stringBuffer.delete(0, stringBuffer.length());
	//	stringBuffer.append(" base.BPTD_CODE = :bptdCode AND base.ITEM_VALUE NOT IN(").append(sql.toString()).append(") AND isnull(base.APPROVED_FLAG,'").append(IAtomsConstants.NO).append("') = :approvedFlag AND isnull(base.DELETED,'").append(IAtomsConstants.NO).append("') = :deleted");
		// 處理approvedFlag
		stringBuffer.append(" base.BPTD_CODE = :bptdCode AND base.ITEM_VALUE NOT IN(").append(sql.toString()).append(") AND isnull(base.DELETED,'").append(IAtomsConstants.NO).append("') = :deleted");
		sqlStatement.addWhereClause(stringBuffer.toString());
		sqlStatement.setOrderByExpression("item_order asc");
		//打印SQL語句
		LOGGER.debug("getReportList()", "SQL : ", sqlStatement.toString());
		SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
		sqlQueryBean.setParameter("bptdCode", IATOMS_PARAM_TYPE.REPORT_CODE.getCode());
		sqlQueryBean.setParameter("companyId", companyId);
	//	sqlQueryBean.setParameter("approvedFlag", IAtomsConstants.YES);
		sqlQueryBean.setParameter("deleted", IAtomsConstants.NO);
		AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
		reportLists = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
	} catch (Exception e) {
		LOGGER.error("getPreReportCodeList()", "is error :", e);
		throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
	}
		return reportLists;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IReportSettingDAO#getDetailList(java.lang.String, java.lang.String)
	 */
	@Override
	public List<ReportSettingDTO> getDetailList(String customerId, String reportCode) {
		//打印傳遞的參數
		LOGGER.debug("getDetailList()", "parameter : customerId= ", customerId);
		LOGGER.debug("getDetailList()", "parameter : reportCode= ", reportCode);
		List<ReportSettingDTO> reportSettingDTOs = null;
		SqlStatement sqlStatement = new SqlStatement();
		// schema
		String schema = this.getMySchema();
		StringBuffer stringBuffer = new StringBuffer();
		try {
			//拼接子表SQL
			sqlStatement.addSelectClause("DISTINCT report.SETTING_ID", ReportSettingDTO.ATTRIBUTE.SETTING_ID.getValue());
			sqlStatement.addSelectClause(" customer.COMPANY_ID", ReportSettingDTO.ATTRIBUTE.COMPANY_ID.getValue());
			sqlStatement.addSelectClause(" customer.SHORT_NAME", ReportSettingDTO.ATTRIBUTE.CUSTOMER_NAME.getValue());
			sqlStatement.addSelectClause(" report.REPORT_CODE", ReportSettingDTO.ATTRIBUTE.REPORT_CODE.getValue());
			sqlStatement.addSelectClause(" baseCode.ITEM_NAME", ReportSettingDTO.ATTRIBUTE.REPORT_NAME.getValue());
			sqlStatement.addSelectClause(" report.RECIPIENT", ReportSettingDTO.ATTRIBUTE.RECIPIENT.getValue());
			sqlStatement.addSelectClause(" baseDetail.ITEM_NAME", ReportSettingDTO.ATTRIBUTE.REPORT_DETAIL.getValue());
			sqlStatement.addSelectClause(" detail.REPORT_DETAIL", ReportSettingDTO.ATTRIBUTE.REPORT_DETAIL_ID.getValue());
			sqlStatement.addSelectClause(" report.COPY", ReportSettingDTO.ATTRIBUTE.COPY.getValue());
			sqlStatement.addSelectClause(" report.REMARK", ReportSettingDTO.ATTRIBUTE.REMARK.getValue());
		/*	sqlStatement.addFromExpression(
					schema + ".BIM_REPORT_SETTING report LEFT JOIN " 
			      + schema + ".BIM_REPORT_SETTING_DETAIL detail ON report.SETTING_ID = detail.SETTING_ID LEFT JOIN " 
			      + schema + ".BIM_COMPANY customer ON report.COMPANY_ID  = customer.COMPANY_ID LEFT JOIN " 
			      + schema + ".BASE_PARAMETER_ITEM_DEF baseCode ON baseCode.BPTD_CODE = :bptdReportCode AND report.REPORT_CODE = baseCode.ITEM_VALUE LEFT JOIN " 
			      + schema + ".BASE_PARAMETER_ITEM_DEF baseDetail ON baseDetail.BPTD_CODE = :bptdReportDetail AND detail.REPORT_DETAIL = baseDetail.ITEM_VALUE AND baseDetail.PARENT_BPID_ID = 'REPORT_CODE_01'");*/
			stringBuffer.append(schema).append(".BIM_REPORT_SETTING report LEFT JOIN ").append(schema).append(".BIM_REPORT_SETTING_DETAIL detail ON report.SETTING_ID = detail.SETTING_ID LEFT JOIN ");
			stringBuffer.append(schema).append(".BIM_COMPANY customer ON report.COMPANY_ID  = customer.COMPANY_ID LEFT JOIN ").append(schema);
			stringBuffer.append(".BASE_PARAMETER_ITEM_DEF baseCode ON baseCode.BPTD_CODE = :bptdReportCode AND report.REPORT_CODE = baseCode.ITEM_VALUE LEFT JOIN ").append(schema);
			stringBuffer.append(".BASE_PARAMETER_ITEM_DEF baseDetail ON baseDetail.BPTD_CODE = :bptdReportDetail AND detail.REPORT_DETAIL = baseDetail.ITEM_VALUE AND baseDetail.PARENT_BPID_ID = baseCode.BPID_ID");
			sqlStatement.addFromExpression(stringBuffer.toString());
			if(StringUtils.hasText(customerId)){
				sqlStatement.addWhereClause(" report.COMPANY_ID =:customer", customerId);
			}
			if(StringUtils.hasText(reportCode)){
				sqlStatement.addWhereClause(" report.REPORT_CODE =:reportCode", reportCode);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("bptdReportCode", IATOMS_PARAM_TYPE.REPORT_CODE.getCode());
			sqlQueryBean.setParameter("bptdReportDetail", IATOMS_PARAM_TYPE.REPORT_DETAIL.getCode());
			//打印SQL語句
			LOGGER.debug("getDetailList()", "SQL : ", sqlQueryBean.toString());
			AliasBean aliasBean = sqlStatement.createAliasBean(ReportSettingDTO.class);
			reportSettingDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("getDetailList()", "is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_REPORT_SETTING)}, e);
		}
		return reportSettingDTOs;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IReportSettingDAO#listByReportCode(java.lang.String, java.lang.String)
	 */
	@Override
	public List<ReportSettingDTO> listByReportCode(String queryCustomerId, String queryReportCode) throws DataAccessException {
		List<ReportSettingDTO> reportCodeList = null;
		try {
			reportCodeList = this.listByReportCode(queryCustomerId, queryReportCode, -1, -1, null, null);
		} catch (Exception e) {
			LOGGER.error("listByReportCode()", "is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_REPORT_SETTING)}, e);
		}
		return reportCodeList;
	}
}
