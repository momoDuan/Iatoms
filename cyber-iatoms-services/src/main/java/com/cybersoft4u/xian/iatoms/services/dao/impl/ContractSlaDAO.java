package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.config.GenericConfigManager;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ContractSlaDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractSlaFormDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IContractSlaDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimSla;
/**
 * Purpose: 合約SLA設定DAO
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016/6/29
 * @MaintenancePersonnel CrissZhang
 */
public class ContractSlaDAO extends GenericBaseDAO<BimSla> implements IContractSlaDAO {
	/**
	 * 系统日志记录物件  
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.DAO, ContractSlaDAO.class);

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractSlaDAO#listBy(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@Override
	public List<ContractSlaDTO> listBy(String queryCustomerId, String queryContractId, String queryLocation, String queryTicketType,
			String queryImportance, Integer pageSize, Integer pageIndex, String sort, String orderby, boolean isPage) throws DataAccessException {
		LOGGER.debug("listBy()", "parameters:queryCustomerId = " + queryCustomerId);
		LOGGER.debug("listBy()", "parameters:queryContractId = " + queryContractId);
		LOGGER.debug("listBy()", "parameters:queryLocation = " + queryLocation);
		LOGGER.debug("listBy()", "parameters:queryTicketType = " + queryTicketType);
		LOGGER.debug("listBy()", "parameters:queryImportance = " + queryImportance);
		LOGGER.debug("listBy()", "parameters:pageSize = " + pageSize);
		LOGGER.debug("listBy()", "parameters:pageIndex = " + pageIndex);
		LOGGER.debug("listBy()", "parameters:sort = " + sort);
		LOGGER.debug("listBy()", "parameters:orderby = " + orderby);
		// 查詢結果列表
		List<ContractSlaDTO> contractSlaDTOList = null;
		try {
			// 拿到schema
			String schema = this.getMySchema();
			SqlStatement sql = new SqlStatement();
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			// 查询语句
			sql.addSelectClause("s.SLA_ID", ContractSlaDTO.ATTRIBUTE.SLA_ID.getValue());
			sql.addSelectClause("com.SHORT_NAME", ContractSlaDTO.ATTRIBUTE.CUSTOMER_NAME.getValue());
			sql.addSelectClause("com.COMPANY_ID", ContractSlaDTO.ATTRIBUTE.CUSTOMER_ID.getValue());
			sql.addSelectClause("s.CONTRACT_ID", ContractSlaDTO.ATTRIBUTE.CONTRACT_ID.getValue());
			sql.addSelectClause("con.CONTRACT_CODE", ContractSlaDTO.ATTRIBUTE.CONTRACT_CODE.getValue());
			sql.addSelectClause("s.TICKET_TYPE", ContractSlaDTO.ATTRIBUTE.TICKET_TYPE.getValue());
			sql.addSelectClause("bpidTicketType.ITEM_NAME", ContractSlaDTO.ATTRIBUTE.TICKET_TYPE_NAME.getValue());
			sql.addSelectClause("s.LOCATION", ContractSlaDTO.ATTRIBUTE.LOCATION.getValue());
			sql.addSelectClause("bpidRegion.ITEM_NAME", ContractSlaDTO.ATTRIBUTE.LOCATION_NAME.getValue());
			sql.addSelectClause("s.TICKET_MODE", ContractSlaDTO.ATTRIBUTE.TICKET_MODE.getValue());
			sql.addSelectClause("bpidTicketMode.ITEM_NAME", ContractSlaDTO.ATTRIBUTE.TICKET_MODE_NAME.getValue());
			sql.addSelectClause("s.IS_WORK_DAY", ContractSlaDTO.ATTRIBUTE.IS_WORK_DAY.getValue());
			sql.addSelectClause("s.IS_THAT_DAY", ContractSlaDTO.ATTRIBUTE.IS_THAT_DAY.getValue());
			sql.addSelectClause("s.THAT_DAY_TIME", ContractSlaDTO.ATTRIBUTE.THAT_DAY_TIME.getValue());
			sql.addSelectClause(" s.RESPONSE_HOUR", ContractSlaDTO.ATTRIBUTE.RESPONSE_HOUR.getValue());
			sql.addSelectClause(" s.RESPONSE_WARNNING", ContractSlaDTO.ATTRIBUTE.RESPONSE_WARNNING.getValue());
			sql.addSelectClause(" s.ARRIVE_HOUR", ContractSlaDTO.ATTRIBUTE.ARRIVE_HOUR.getValue());
			sql.addSelectClause(" s.ARRIVE_WARNNING", ContractSlaDTO.ATTRIBUTE.ARRIVE_WARNNING.getValue());
			sql.addSelectClause(" s.COMPLETE_HOUR", ContractSlaDTO.ATTRIBUTE.COMPLETE_HOUR.getValue());
			sql.addSelectClause(" s.COMPLETE_WARNNING", ContractSlaDTO.ATTRIBUTE.COMPLETE_WARNNING.getValue());
			sql.addSelectClause(" s.COMMENT", ContractSlaDTO.ATTRIBUTE.COMMENT.getValue());
			StringBuilder builder = new StringBuilder();
			builder.append(schema).append(".BIM_SLA s LEFT JOIN ");
			builder.append(schema).append(".BIM_CONTRACT con ON s.CONTRACT_ID = con.CONTRACT_ID LEFT JOIN ");
			builder.append(schema).append(".BIM_COMPANY com ON con.COMPANY_ID = com.COMPANY_ID LEFT JOIN ");
			builder.append(schema).append(".BASE_PARAMETER_ITEM_DEF bpidTicketType ON s.TICKET_TYPE = bpidTicketType.ITEM_VALUE ");
			builder.append(" AND bpidTicketType.bptd_code = :ticketTypeParam LEFT JOIN ");
			builder.append(schema).append(".BASE_PARAMETER_ITEM_DEF bpidRegion ON s.LOCATION = bpidRegion.ITEM_VALUE");
			builder.append(" AND bpidRegion.bptd_code = :regionParam LEFT JOIN ");
			builder.append(schema).append(".BASE_PARAMETER_ITEM_DEF bpidTicketMode ON s.TICKET_MODE = bpidTicketMode.ITEM_VALUE");
			builder.append(" AND bpidTicketMode.bptd_code = :ticketModeParam");
			sql.setFromExpression(builder.toString());
			// 查詢條件：客戶編號
			if (StringUtils.hasText(queryCustomerId)) {
				sql.addWhereClause(" con.COMPANY_ID=:customerId", queryCustomerId);
			}
			// 查詢條件：合約編號
			if (StringUtils.hasText(queryContractId)) {
				sql.addWhereClause(" s.CONTRACT_ID=:contractId", queryContractId);
			}
			// 查詢條件：地區編號
			if (StringUtils.hasText(queryLocation)) {
				sql.addWhereClause(" s.LOCATION=:location", queryLocation);
			}
			// 查詢條件：案件類別
			if (StringUtils.hasText(queryTicketType)) {
				sql.addWhereClause(" s.TICKET_TYPE=:ticketType", queryTicketType);
			}
			// 查詢條件：案件類型
			if (StringUtils.hasText(queryImportance)) {
				sql.addWhereClause(" s.TICKET_MODE=:ticketMode", queryImportance);
			}
			// 排序
			if((StringUtils.hasText(sort)) && (StringUtils.hasText(orderby))){
				sql.setOrderByExpression(sort + " " + orderby);
			} else {
				sql.setOrderByExpression(ContractSlaFormDTO.PARAM_PAGE_SORT);
			}
			sqlQueryBean = sql.createSqlQueryBean();

			sqlQueryBean.setParameter("ticketTypeParam", IATOMS_PARAM_TYPE.TICKET_TYPE.getCode());
			sqlQueryBean.setParameter("regionParam", IATOMS_PARAM_TYPE.REGION.getCode());
			sqlQueryBean.setParameter("ticketModeParam", IATOMS_PARAM_TYPE.TICKET_MODE.getCode());

			if(isPage){
				// 分頁設置
				sqlQueryBean.setPageSize(pageSize.intValue());
				sqlQueryBean.setStartPage(pageIndex.intValue() - 1);
			}
			LOGGER.debug("listBy()", "sql:" + sql.toString());
			AliasBean aliasBean = sql.createAliasBean(ContractSlaDTO.class);
			// 执行查询语句
			contractSlaDTOList = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listBy()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return contractSlaDTOList;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractSlaDAO#count(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public int count(String queryCustomerId, String queryContractId, String queryLocation, String queryTicketType,
			String queryImportance) throws DataAccessException {
		LOGGER.debug("count()", "parameters:queryCustomerId = " + queryCustomerId);
		LOGGER.debug("count()", "parameters:queryContractId = " + queryContractId);
		LOGGER.debug("count()", "parameters:queryLocation = " + queryLocation);
		LOGGER.debug("count()", "parameters:queryTicketType = " + queryTicketType);
		LOGGER.debug("count()", "parameters:queryImportance = " + queryImportance);
		try {
			// 拿到schema
			String schema = this.getMySchema();
			SqlStatement sql = new SqlStatement();
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			// 查询语句
			sql.addSelectClause("count(1)");
			StringBuilder builder = new StringBuilder();
			builder.append(schema).append(".BIM_SLA s LEFT JOIN ");
			builder.append(schema).append(".BIM_CONTRACT con ON s.CONTRACT_ID = con.CONTRACT_ID");
			builder.append(" left join ").append(schema).append(".BIM_COMPANY com ON con.COMPANY_ID = com.COMPANY_ID");
			sql.setFromExpression(builder.toString());
			// 查詢條件：客戶編號
			if (StringUtils.hasText(queryCustomerId)) {
				sql.addWhereClause(" con.COMPANY_ID=:customerId", queryCustomerId);
			}
			// 查詢條件：合約編號
			if (StringUtils.hasText(queryContractId)) {
				sql.addWhereClause(" s.CONTRACT_ID=:contractId", queryContractId);
			}
			// 查詢條件：地區編號
			if (StringUtils.hasText(queryLocation)) {
				sql.addWhereClause(" s.LOCATION=:location", queryLocation);
			}
			// 查詢條件：案件類別
			if (StringUtils.hasText(queryTicketType)) {
				sql.addWhereClause(" s.TICKET_TYPE=:ticketType", queryTicketType);
			}
			// 查詢條件：案件類型
			if (StringUtils.hasText(queryImportance)) {
				sql.addWhereClause(" s.TICKET_MODE=:ticketMode", queryImportance);
			}
			sqlQueryBean = sql.createSqlQueryBean();
			LOGGER.debug("count()", "sql:" + sql.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				return result.get(0).intValue();
			}
			return 0;
		} catch (Exception e) {
			LOGGER.error("count()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractSlaDAO#getSlaByCustomerAndContract(java.lang.String, java.lang.String)
	 */
	@Override
	public List<ContractSlaDTO> getSlaByCustomerAndContract(String customerId, String contractId) throws DataAccessException {
		LOGGER.debug("getSlaByCustomerAndContract()", "parameters:customerId = " + customerId);
		LOGGER.debug("getSlaByCustomerAndContract()", "parameters:contractId = " + contractId);
		// 得到sla信息列表
		List<ContractSlaDTO> contractSlaDTOList = null;
		try {
			// 拿到schema
			String schema = this.getMySchema();
			SqlStatement sql = new SqlStatement();
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			// 查询语句
			sql.addSelectClause("s.SLA_ID", ContractSlaDTO.ATTRIBUTE.SLA_ID.getValue());
			sql.addSelectClause("con.COMPANY_ID", ContractSlaDTO.ATTRIBUTE.CUSTOMER_ID.getValue());
			sql.addSelectClause("s.CONTRACT_ID", ContractSlaDTO.ATTRIBUTE.CONTRACT_ID.getValue());
			sql.addSelectClause("s.TICKET_TYPE", ContractSlaDTO.ATTRIBUTE.TICKET_TYPE.getValue());
			sql.addSelectClause("s.LOCATION", ContractSlaDTO.ATTRIBUTE.LOCATION.getValue());
			sql.addSelectClause("s.TICKET_MODE", ContractSlaDTO.ATTRIBUTE.TICKET_MODE.getValue());
			sql.addSelectClause("s.IS_WORK_DAY", ContractSlaDTO.ATTRIBUTE.IS_WORK_DAY.getValue());
			sql.addSelectClause("s.IS_THAT_DAY", ContractSlaDTO.ATTRIBUTE.IS_THAT_DAY.getValue());
			sql.addSelectClause("s.THAT_DAY_TIME", ContractSlaDTO.ATTRIBUTE.THAT_DAY_TIME.getValue());
			sql.addSelectClause(" s.RESPONSE_HOUR", ContractSlaDTO.ATTRIBUTE.RESPONSE_HOUR.getValue());
			sql.addSelectClause(" s.RESPONSE_WARNNING", ContractSlaDTO.ATTRIBUTE.RESPONSE_WARNNING.getValue());
			sql.addSelectClause(" s.ARRIVE_HOUR", ContractSlaDTO.ATTRIBUTE.ARRIVE_HOUR.getValue());
			sql.addSelectClause(" s.ARRIVE_WARNNING", ContractSlaDTO.ATTRIBUTE.ARRIVE_WARNNING.getValue());
			sql.addSelectClause(" s.COMPLETE_HOUR", ContractSlaDTO.ATTRIBUTE.COMPLETE_HOUR.getValue());
			sql.addSelectClause(" s.COMPLETE_WARNNING", ContractSlaDTO.ATTRIBUTE.COMPLETE_WARNNING.getValue());
			sql.addSelectClause(" s.COMMENT", ContractSlaDTO.ATTRIBUTE.COMMENT.getValue());
			StringBuilder builder = new StringBuilder();
			builder.append(schema).append(".BIM_SLA s LEFT JOIN ");
			builder.append(schema).append(".BIM_CONTRACT con ON s.CONTRACT_ID = con.CONTRACT_ID");
			builder.append(" left join ").append(schema).append(".BIM_COMPANY com ON con.COMPANY_ID = com.COMPANY_ID");
			sql.setFromExpression(builder.toString());
			// 傳入的客戶編號
			if (StringUtils.hasText(customerId)) {
				sql.addWhereClause(" con.COMPANY_ID=:customerId", customerId);
			}
			// 傳入的合約編號
			if (StringUtils.hasText(contractId)) {
				sql.addWhereClause(" s.CONTRACT_ID=:contractId", contractId);
			}
			sqlQueryBean = sql.createSqlQueryBean();
			LOGGER.debug("getSlaByCustomerAndContract()", "sql:" + sql.toString());
			AliasBean aliasBean = sql.createAliasBean(ContractSlaDTO.class);
			// 执行查询语句
			contractSlaDTOList = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("getSlaByCustomerAndContract()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return contractSlaDTOList;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractSlaDAO#isCopyRepeat(java.lang.String, java.lang.String)
	 */
	public boolean isCopyRepeat(String contractId, String copyContractId) throws DataAccessException {
		LOGGER.debug("isCopyRepeat()", "parameters:contractId = " + contractId);
		LOGGER.debug("isCopyRepeat()", "parameters:copyContractId = " + copyContractId);
		try {
			// 拿到schema
			String schema = this.getMySchema();
			SqlStatement sql = new SqlStatement();
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			// 查询语句
			sql.addSelectClause("count(1)");
			StringBuilder builder = new StringBuilder();
			builder.append(schema).append(".BIM_SLA s inner join ");
			builder.append(schema).append(".BIM_SLA s1 on s.LOCATION = s1.LOCATION and s.TICKET_TYPE = s1.TICKET_TYPE and s.TICKET_MODE = s1.TICKET_MODE");
			sql.setFromExpression(builder.toString());
			if (StringUtils.hasText(contractId)) {
				sql.addWhereClause(" s.CONTRACT_ID=:contractId", contractId);
			}
			if (StringUtils.hasText(copyContractId)) {
				sql.addWhereClause(" s1.CONTRACT_ID=:copyContractId", copyContractId);
			}
			sqlQueryBean = sql.createSqlQueryBean();
			LOGGER.debug("isCopyRepeat()", "sql:" + sql.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				if (result.get(0).intValue() > 0) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			LOGGER.error("isCopyRepeat()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractSlaDAO#isRepeat(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isRepeat(String slaId, String contractId, String ticketType, String location, String ticketMode) throws DataAccessException {
		LOGGER.debug("isRepeat()", "parameters:slaId = " + slaId);
		LOGGER.debug("isRepeat()", "parameters:contractId = " + contractId);
		LOGGER.debug("isRepeat()", "parameters:ticketType = " + ticketType);
		LOGGER.debug("isRepeat()", "parameters:location = " + location);
		LOGGER.debug("isRepeat()", "parameters:ticketMode = " + ticketMode);
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			sqlStatement.addFromExpression(schema + ".BIM_SLA s");
			if (StringUtils.hasText(slaId)) {
				sqlStatement.addWhereClause("s.SLA_ID <> :slaId", slaId);
			}
			if (StringUtils.hasText(contractId)) {
				sqlStatement.addWhereClause("s.CONTRACT_ID = :contractId", contractId);
			}
			if (StringUtils.hasText(location)) {
				sqlStatement.addWhereClause("s.LOCATION = :location", location);
			}
			if (StringUtils.hasText(ticketType)) {
				sqlStatement.addWhereClause("s.TICKET_TYPE = :ticketType", ticketType);
			}			
			if (StringUtils.hasText(ticketMode)) {
				sqlStatement.addWhereClause("s.TICKET_MODE = :ticketMode", ticketMode);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug("isRepeat()", "sql:" + sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				if (result.get(0).intValue() > 0) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			LOGGER.error("isRepeat()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
}
