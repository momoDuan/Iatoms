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

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ApiLogDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IApiLogDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.ApiLog;

public class ApiLogDAO extends GenericBaseDAO<ApiLog> implements IApiLogDAO {
	/**
	 * 日誌記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog) CafeLogFactory.getLog(ApiLogDAO.class);
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IApiLogDAO#getAuthorizationInfoList(java.lang.String)
	 */
	@Override
	public ApiLogDTO getApiLogDto(String id) throws DataAccessException {
		ApiLogDTO result = null;
		LOGGER.debug("getApiLogDto()", "id=" + id);
		//得到schema
		String schema = this.getMySchema();
		SqlStatement sqlStatement = new SqlStatement();
		try {
			sqlStatement.addSelectClause("ID",ApiLogDTO.ATTRIBUTE.ID.getValue());
			sqlStatement.addSelectClause("IP",ApiLogDTO.ATTRIBUTE.IP.getValue());
			sqlStatement.addSelectClause("CLIENT_CODE",ApiLogDTO.ATTRIBUTE.CLIENT_CODE.getValue());
			sqlStatement.addSelectClause("FUNCTION_CODE",ApiLogDTO.ATTRIBUTE.FUNCTION_CODE.getValue());
			sqlStatement.addSelectClause("MSG_TYPE",ApiLogDTO.ATTRIBUTE.MSG_TYPE.getValue());
			sqlStatement.addSelectClause("MESSAGE",ApiLogDTO.ATTRIBUTE.MESSAGE.getValue());
			sqlStatement.addSelectClause("FAIL_REASON_DESC",ApiLogDTO.ATTRIBUTE.FAIL_REASON_DESC.getValue());
			sqlStatement.addSelectClause("RESULT",ApiLogDTO.ATTRIBUTE.RESULT.getValue());
			sqlStatement.addSelectClause("MASTER_ID",ApiLogDTO.ATTRIBUTE.MASTER_ID.getValue());
			sqlStatement.addSelectClause("DETAIL_ID",ApiLogDTO.ATTRIBUTE.DETAIL_ID.getValue());
			sqlStatement.addSelectClause("CREATED_BY_ID",ApiLogDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			sqlStatement.addSelectClause("CREATED_BY_NAME",ApiLogDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("CREATED_DATE",ApiLogDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sqlStatement.addFromExpression(schema + ".API_LOG");
			if (StringUtils.hasText(id)) {
				sqlStatement.addWhereClause("ID = :id", id);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(ApiLogDTO.class);
			LOGGER.debug("getApiLogDto()", "sql:" + sqlQueryBean.toString());
			List<ApiLogDTO> list = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(list)) {
				LOGGER.debug("getApiLogDto()", "list is not null...");
				result = list.get(0);
			}
			return result;
		} catch (Exception e) {
			LOGGER.error("getApiLogDto()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_USER)}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IApiLogDAO#insertApiLog(ApiLogDTO)
	 */
	@Override
	public void insertApiLog(ApiLogDTO apiLogDTO)  throws DataAccessException {
		try {
			LOGGER.debug("insertApiLog()", "id=" + apiLogDTO.getId());
			LOGGER.debug("insertApiLog()", "ip=" + apiLogDTO.getIp());
			LOGGER.debug("insertApiLog()", "clientCode=" + apiLogDTO.getClientCode());
			LOGGER.debug("insertApiLog()", "functionCode=" + apiLogDTO.getFunctionCode());
			LOGGER.debug("insertApiLog()", "ceratedDate" + apiLogDTO.getCreatedDate());
			LOGGER.debug("insertApiLog()", "message=" + apiLogDTO.getMessage());
			LOGGER.debug("insertApiLog()", "failReasonDesc=" + apiLogDTO.getFailReasonDesc());
			LOGGER.debug("insertApiLog()", "createdById=" + apiLogDTO.getCreatedById());
			LOGGER.debug("insertApiLog()", "createdByName=" + apiLogDTO.getCreatedByName());
			LOGGER.debug("insertApiLog()", "detailId=" + apiLogDTO.getDetailId());
			LOGGER.debug("insertApiLog()", "masterId=" + apiLogDTO.getMasterId());
			LOGGER.debug("insertApiLog()", "msgType=" + apiLogDTO.getMsgType());
			LOGGER.debug("insertApiLog()", "result=" + apiLogDTO.getResult());

			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("INSERT INTO ").append(schema).append(".API_LOG(ID, IP, CLIENT_CODE, FUNCTION_COdE, MSG_TYPE, MESSAGE, FAIL_REASON_DESC, RESULT, MASTER_ID, DETAIL_ID, CREATED_BY_ID, CREATED_BY_NAME, CREATED_DATE)")
						.append("VALUES(:id, :ip, :clientCode, :functionCode, :msgType, :message, :failReasonDesc, :result, :masterId, :detailId, :createdById, :createdByName, :createdDate)");
			sqlQueryBean.setParameter("id", apiLogDTO.getId());
			sqlQueryBean.setParameter("ip", apiLogDTO.getIp());
			sqlQueryBean.setParameter("clientCode", apiLogDTO.getClientCode());
			sqlQueryBean.setParameter("functionCode", apiLogDTO.getFunctionCode());
			sqlQueryBean.setParameter("msgType", apiLogDTO.getMsgType());
			sqlQueryBean.setParameter("message", apiLogDTO.getMessage());
			sqlQueryBean.setParameter("failReasonDesc", StringUtils.hasText(apiLogDTO.getFailReasonDesc()) ? apiLogDTO.getFailReasonDesc() : "");
			sqlQueryBean.setParameter("result", apiLogDTO.getResult());
			sqlQueryBean.setParameter("masterId", StringUtils.hasText(apiLogDTO.getMasterId()) ? apiLogDTO.getMasterId() : "");
			sqlQueryBean.setParameter("detailId", StringUtils.hasText(apiLogDTO.getDetailId()) ? apiLogDTO.getDetailId() : "");
			sqlQueryBean.setParameter("createdById", apiLogDTO.getCreatedById());
			sqlQueryBean.setParameter("createdByName", apiLogDTO.getCreatedByName());
			sqlQueryBean.setParameter("createdDate", apiLogDTO.getCreatedDate());

			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
			LOGGER.debug("insertApiLog()", "updateByNativeSql Success ");
		} catch (Exception e) {
			LOGGER.error("insertApiLog() error:", e);
			throw new DataAccessException(CoreMessageCode.DB_UNKNOWN_ERROR,e);
		}
	}
	/** 
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IApiLogDAO#listBy(java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ApiLogDTO> listBy(String queryClientCode,
			String queryStartDate, String queryEndDate, Integer pageSize,
			Integer pageIndex, String sort, String orderby)
			throws DataAccessException {
		// TODO Auto-generated method stub
		LOGGER.debug("listBy()", "queryClientCode=" + queryClientCode);
		LOGGER.debug("listBy()", "queryStartDate=" + queryStartDate);
		LOGGER.debug("listBy()", "queryEndDate=" + queryEndDate);
		LOGGER.debug("listBy()", "pageSize=" + pageSize);
		LOGGER.debug("listBy()", "pageIndex=" + pageIndex);
		LOGGER.debug("listBy()", "sort=" + sort);
		LOGGER.debug("listBy()", "orderby=" + orderby);
		
		List<ApiLogDTO> apiLogDTOs = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("api.ID", ApiLogDTO.ATTRIBUTE.ID.getValue());
			sqlStatement.addSelectClause("api.IP", ApiLogDTO.ATTRIBUTE.IP.getValue());
			sqlStatement.addSelectClause("api.CLIENT_CODE", ApiLogDTO.ATTRIBUTE.CLIENT_CODE.getValue());
			sqlStatement.addSelectClause("api.FUNCTION_CODE", ApiLogDTO.ATTRIBUTE.FUNCTION_CODE.getValue());
			sqlStatement.addSelectClause("case when fun.FUNCTION_NAME is null then api.FUNCTION_CODE else fun.FUNCTION_NAME end", ApiLogDTO.ATTRIBUTE.FUNCTION_NAME.getValue());
			//sqlStatement.addSelectClause("fun.FUNCTION_NAME", ApiLogDTO.ATTRIBUTE.FUNCTION_NAME.getValue());
			sqlStatement.addSelectClause("api.MSG_TYPE", ApiLogDTO.ATTRIBUTE.MSG_TYPE.getValue());
			sqlStatement.addSelectClause("api.MESSAGE", ApiLogDTO.ATTRIBUTE.MESSAGE.getValue());
			sqlStatement.addSelectClause("api.FAIL_REASON_DESC", ApiLogDTO.ATTRIBUTE.FAIL_REASON_DESC.getValue());
			sqlStatement.addSelectClause("api.RESULT", ApiLogDTO.ATTRIBUTE.RESULT.getValue());
			sqlStatement.addSelectClause("api.MASTER_ID", ApiLogDTO.ATTRIBUTE.MASTER_ID.getValue());
			sqlStatement.addSelectClause("api.DETAIL_ID", ApiLogDTO.ATTRIBUTE.DETAIL_ID.getValue());
			sqlStatement.addSelectClause("api.CREATED_BY_ID", ApiLogDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			sqlStatement.addSelectClause("api.CREATED_BY_NAME", ApiLogDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("api.CREATED_DATE", ApiLogDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sqlStatement.setFromExpression(" " + schema + ".API_LOG api " +
					"left join " + schema + ".ADM_FUNCTION_TYPE fun on api.FUNCTION_CODE=fun.FUNCTION_ID ");
			//ClientCode
			if (StringUtils.hasText(queryClientCode)) {
				sqlStatement.addWhereClause("api.CLIENT_CODE like :queryClientCode", queryClientCode + IAtomsConstants.MARK_PERCENT);
			}
			//建檔時間起
			if(StringUtils.hasText(queryStartDate)){
				sqlStatement.addWhereClause("replace(convert(varchar(16), api.CREATED_DATE, 120), '-', '/') >= :queryStartDate", queryStartDate);
			}
			//建檔時間迄
			if(StringUtils.hasText(queryStartDate)){
				sqlStatement.addWhereClause("replace(convert(varchar(16), api.CREATED_DATE, 120), '-', '/') <= :queryEndDate", queryEndDate);
			}
			//設置排序表達式
			if (StringUtils.hasText(sort) && StringUtils.hasText(orderby)){
				sqlStatement.setOrderByExpression(sort + " " + orderby);
			} else {
				sqlStatement.setOrderByExpression("api.CREATED_DATE desc");
			}
			sqlStatement.setPageSize(pageSize);
			sqlStatement.setStartPage(pageIndex - 1);
			//創建SqlQueryBean
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(ApiLogDTO.class);
			LOGGER.debug("listBy()", "sql:" + sqlQueryBean.toString());
			apiLogDTOs = (List<ApiLogDTO>) super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listBy()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,
					new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_API_LOG)}, e);
		}
		return apiLogDTOs;
	}
	/** 
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IApiLogDAO#count(java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Integer count(String queryClientCode, String queryStartDate,
			String queryEndDate) throws DataAccessException {
		// TODO Auto-generated method stub
		LOGGER.debug("count()", "queryClientCode=" + queryClientCode);
		LOGGER.debug("count()", "queryStartDate=" + queryStartDate);
		LOGGER.debug("count()", "queryEndDate=" + queryEndDate);
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			sqlStatement.setFromExpression(" " + schema + ".API_LOG api " +
					"left join " + schema + ".ADM_FUNCTION_TYPE fun on api.FUNCTION_CODE=fun.FUNCTION_CODE ");
			//ClientCode
			if (StringUtils.hasText(queryClientCode)) {
				sqlStatement.addWhereClause("api.CLIENT_CODE like :queryClientCode", queryClientCode + IAtomsConstants.MARK_PERCENT);
			}
			//建檔時間起
			if(StringUtils.hasText(queryStartDate)){
				sqlStatement.addWhereClause("replace(convert(varchar(16), api.CREATED_DATE, 120), '-', '/') >= :queryStartDate", queryStartDate);
			}
			//建檔時間迄
			if(StringUtils.hasText(queryStartDate)){
				sqlStatement.addWhereClause("replace(convert(varchar(16), api.CREATED_DATE, 120), '-', '/') <= :queryEndDate", queryEndDate);
			}
			//記錄語句
			LOGGER.debug(this.getClass().getName() + ".count()", "sql:" + sqlStatement.toString());
			//創建SqlQueryBean
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			List<Integer> result = super.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				return result.get(0).intValue();
			}
			return 0;
		} catch (Exception e) {
			LOGGER.error("count()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,
					new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_API_LOG)}, e);
		}
	}
}
