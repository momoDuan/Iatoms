package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Parameter;
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
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmSuppliesDTO;
import com.cybersoft4u.xian.iatoms.services.dao.ISuppliesTypeDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmSupplies;

/**
 * Purpose: 耗材品项维护DAO實現類
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2016年7月04日
 * @MaintenancePeronnel HermanWang
 */
public class SuppliesTypeDAO extends GenericBaseDAO<DmmSupplies> implements ISuppliesTypeDAO {
	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(SuppliesTypeDAO.class);	
	
	/**
	 * 
	 * Constructor:構造器
	 */
	public SuppliesTypeDAO() {
		
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISuppliesTypeDAO#listBy(com.cybersoft4u.xian.iatoms.common.bean.formDTO.SuppliesFormDTO, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<DmmSuppliesDTO> listBy(String queryCustomerId, String querySuppliesCode, String querySuppliesName, Integer pageSize, Integer page, String order, String sort) throws DataAccessException {
		LOGGER.debug(".listBy()", "parameters:queryCustomerId:", queryCustomerId );
		LOGGER.debug(".listBy()", "parameters:querySuppliesCode:", querySuppliesCode );
		LOGGER.debug(".listBy()", "parameters:querySuppliesName:", querySuppliesName );
		LOGGER.debug(".listBy()", "row:", pageSize.intValue() );
		LOGGER.debug(".listBy()", "page:", page.intValue() );
		List<DmmSuppliesDTO> listsuppliesDTO= null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectColumn("SUPPLIES_ID", DmmSuppliesDTO.ATTRIBUTE.SUPPLIES_ID.getValue());
			sqlStatement.addSelectColumn("SUPPLIES_NAME", DmmSuppliesDTO.ATTRIBUTE.SUPPLIES_NAME.getValue());
			sqlStatement.addSelectColumn("base.ITEM_NAME", DmmSuppliesDTO.ATTRIBUTE.SUPPLIES_CODE.getValue());
			sqlStatement.addSelectColumn("c.COMPANY_ID", DmmSuppliesDTO.ATTRIBUTE.COMPANY_ID.getValue());
			sqlStatement.addSelectColumn("c.SHORT_NAME", DmmSuppliesDTO.ATTRIBUTE.CUSTOMER_NAME.getValue());
			sqlStatement.addSelectColumn("PRICE", DmmSuppliesDTO.ATTRIBUTE.PRICE.getValue());
			sqlStatement.addSelectColumn("SUPPLIES_TYPE", DmmSuppliesDTO.ATTRIBUTE.SUPPLIES_TYPE.getValue());
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(" .DMM_SUPPLIES s  ");
			buffer.append(" left join ").append(" bim_company c on s.COMPANY_ID= c.company_id ");
			buffer.append(" left join ").append(" base_parameter_item_def base on s.SUPPLIES_TYPE = base.item_value and base.bptd_code = :supplies_type ");
			sqlStatement.addFromExpression(buffer.toString());
			/*sqlStatement.addFromExpression( schema );
			sqlStatement.addFromExpression( ".DMM_SUPPLIES s " );
			sqlStatement.addFromExpression( " left join bim_company c on  s.COMPANY_ID= c.company_id  left join base_parameter_item_def base on s.SUPPLIES_TYPE = base.item_value and base.bptd_code = :supplies_type " );*/
			//根據顧客下拉框來查詢
			if (StringUtils.hasText(queryCustomerId)) {
				sqlStatement.addWhereClause("c.COMPANY_ID = :queryCustomerId", queryCustomerId);
			}
			//根據耗材分類下拉框來查詢
			if (StringUtils.hasText(querySuppliesCode)) {
				sqlStatement.addWhereClause("s.SUPPLIES_Type = :querySuppliesCode", querySuppliesCode);
			}
			//根據耗材名稱輸入框來查詢
			if (StringUtils.hasText(querySuppliesName)) {
				sqlStatement.addWhereClause("s.SUPPLIES_NAME like :querySuppliesName", querySuppliesName.concat(IAtomsConstants.MARK_PERCENT));
			}
			//排序的兩個參數判斷是否為空
			if (StringUtils.hasText(sort) && StringUtils.hasText(order)) {
				StringBuffer stringBufferbuffer = new StringBuffer();
				stringBufferbuffer.append(sort).append(IAtomsConstants.MARK_SPACE).append(order);
				//sqlStatement.setOrderByExpression(sort.concat(IAtomsConstants.MARK_SPACE).concat(order));
				sqlStatement.setOrderByExpression(stringBufferbuffer.toString());
			} else {
				sqlStatement.setOrderByExpression("c.SHORT_NAME ASC , SUPPLIES_TYPE ASC, SUPPLIES_NAME ASC ");
			}
			sqlStatement.addWhereClause("ISNULL(c.DELETED, 'N') =:deleted", IAtomsConstants.NO);
			//記錄sql語句
			LOGGER.debug(".listBy()", " Native SQL---------->", sqlStatement.toString());
			//根據行和頁分出來.
			sqlStatement.setPageSize(pageSize.intValue());
			sqlStatement.setStartPage(page.intValue() - 1);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("supplies_type", IATOMS_PARAM_TYPE.SUPPLIES_TYPE.getCode());
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmSuppliesDTO.class);
			listsuppliesDTO = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(".listBy() is error", "DAO Exception", e);
			throw new DataAccessException(CoreMessageCode.QUERY_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.REB_IATOMS_TB_NAME_SUPPLIES)}, e);
		}
		return listsuppliesDTO;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISuppliesTypeDAO#getCount(java.lang.String, java.lang.String)
	 */
	@Override
	public Integer getCount(String queryCustomerId, String querySuppliesCode, String querySuppliesName) throws DataAccessException {
		LOGGER.debug(".count()", "parameters:queryCustomerId:", queryCustomerId);
		LOGGER.debug(".count()", "parameters:querySuppliesCode:", querySuppliesCode);
		LOGGER.debug(".count()", "parameters:querySuppliesName:", querySuppliesName);
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			Integer count = null;
			//查詢分頁最底下顯示的總條數
			sqlStatement.addSelectClause("count(*)");
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(" .DMM_SUPPLIES s left join bim_company c on  s.COMPANY_ID= c.company_id ");
			sqlStatement.addFromExpression(buffer.toString());
			/*sqlStatement.addFromExpression(schema);
			sqlStatement.addFromExpression(".DMM_SUPPLIES s left join bim_company c on  s.COMPANY_ID= c.company_id");*/
			//用顧客ID取查詢符合條件的數量
			if (StringUtils.hasText(queryCustomerId)) {
				sqlStatement.addWhereClause("s.COMPANY_ID = :queryCustomerId", queryCustomerId);
			}
			//用耗材品code去查詢符合條件的數量
			if(StringUtils.hasText(querySuppliesCode)){
				sqlStatement.addWhereClause("s.SUPPLIES_TYPE =:querySuppliesCode", querySuppliesCode);
			}
			//根據耗材名稱輸入框來查詢
			if (StringUtils.hasText(querySuppliesName)) {
				sqlStatement.addWhereClause("s.SUPPLIES_NAME like :querySuppliesName", querySuppliesName.concat(IAtomsConstants.MARK_PERCENT));
			}
			sqlStatement.addWhereClause("ISNULL(c.DELETED, 'N') =:deleted", IAtomsConstants.NO);
			LOGGER.debug(".count()", " Native SQL---------->", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				count = result.get(0);
			}
			return count;
		} catch (Exception e) {
			LOGGER.error(".getCount() is error", "DAO Exception", e);
			throw new DataAccessException(CoreMessageCode.QUERY_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.REB_IATOMS_TB_NAME_SUPPLIES)}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISuppliesTypeDAO#check(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isCheck(String companyId, String suppliesName, String suppliesId) throws DataAccessException {
		try {
			LOGGER.debug(".checkMidAndRegistedName()", "parameters:companyId=", companyId);
			LOGGER.debug(".checkMidAndRegistedName()", "parameters:suppliesName=", suppliesName);
			LOGGER.debug(".checkMidAndRegistedName()", "parameters:suppliesId=", suppliesId);
			String schema = this.getMySchema();	
			SqlStatement sqlStatement = new SqlStatement();
			StringBuffer buffer = new StringBuffer();
			sqlStatement.addSelectClause("count(1)");
			buffer.append(schema).append(" .DMM_SUPPLIES ");
			sqlStatement.addFromExpression(buffer.toString());
			/*sqlStatement.addFromExpression(schema);
			sqlStatement.addFromExpression(".DMM_SUPPLIES");*/
			//查詢條件
			if (StringUtils.hasText(companyId)) {
				sqlStatement.addWhereClause("COMPANY_ID = :companyId", companyId);
			}
			if (StringUtils.hasText(suppliesName)) {
				sqlStatement.addWhereClause("SUPPLIES_NAME = :suppliesName", suppliesName);
			}
			if(StringUtils.hasText(suppliesId)){
				sqlStatement.addWhereClause("SUPPLIES_ID != :suppliesId", suppliesId);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			//AliasBean aliasBean = new AliasBean(DmmSuppliesDTO.class);
			LOGGER.debug(".check()", "sql:", sqlQueryBean.toString());
			List<Integer> results = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			//如果查到返回false
			if (!CollectionUtils.isEmpty(results)) {
				if (results.get(0).intValue() > 0) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			LOGGER.error(".check() is error", "DAO Exception", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISuppliesTypeDAO#getSuppliesTypes(java.lang.String)
	 */
	public List<Parameter> getSuppliesTypes(String customerId, String suppliesType) throws DataAccessException {
		List<Parameter> list = null;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("supplies.SUPPLIES_ID", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("supplies.SUPPLIES_NAME", Parameter.FIELD_NAME);
			sqlStatement.addFromExpression(schema + ".DMM_SUPPLIES supplies");
			if (StringUtils.hasText(customerId)) {
				sqlStatement.addWhereClause("supplies.COMPANY_ID = :customerId", customerId);
			}
			if (StringUtils.hasText(suppliesType)) {
				sqlStatement.addWhereClause("supplies.SUPPLIES_TYPE = :suppliesType", suppliesType);
			}
			sqlStatement.setOrderByExpression("supplies.SUPPLIES_NAME");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			LOGGER.debug(".getSuppliesTypes()", "sql:", sqlQueryBean.toString());
			list = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(".getSuppliesTypes() is error", "DAO Exception", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return list;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISuppliesTypeDAO#getSuppliesTypes(java.lang.String)
	 */
	public List<Parameter> getSuppliesTypeList(String customerId) throws DataAccessException {
		List<Parameter> list = null;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("distinct base.ITEM_VALUE", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("base.ITEM_NAME", Parameter.FIELD_NAME);
			sqlStatement.addFromExpression(schema + ".DMM_SUPPLIES supplies left join base_parameter_item_def base on supplies.SUPPLIES_TYPE = base.item_value and base.bptd_code = :supplies_type ");
			if (StringUtils.hasText(customerId)) {
				sqlStatement.addWhereClause("supplies.COMPANY_ID = :customerId", customerId);
			}
			sqlStatement.setOrderByExpression("base.ITEM_VALUE ");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("supplies_type", IATOMS_PARAM_TYPE.SUPPLIES_TYPE.getCode());
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			LOGGER.debug(".getSuppliesTypeList()", "sql:", sqlQueryBean.toString());
			list = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(".getSuppliesTypeList() is error", "DAO Exception", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return list;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISuppliesTypeDAO#getSuppliesNameList(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Parameter> getSuppliesNameList(String customerId,
			String suppliesType) throws DataAccessException {
		List<Parameter> list = null;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("distinct supplies.SUPPLIES_ID", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("supplies.SUPPLIES_NAME", Parameter.FIELD_NAME);
			sqlStatement.addFromExpression(schema + ".DMM_SUPPLIES supplies left join base_parameter_item_def base on supplies.SUPPLIES_TYPE = base.item_value and base.bptd_code = :supplies_type ");
			if (StringUtils.hasText(customerId)) {
				sqlStatement.addWhereClause("supplies.COMPANY_ID = :customerId", customerId);
			}
			if (StringUtils.hasText(suppliesType)) {
				sqlStatement.addWhereClause("base.ITEM_VALUE = :suppliesType", suppliesType);
			}
			sqlStatement.setOrderByExpression("supplies.SUPPLIES_ID ");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("supplies_type", IATOMS_PARAM_TYPE.SUPPLIES_TYPE.getCode());
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			LOGGER.debug(".getSuppliesNameList()", "sql:", sqlQueryBean.toString());
			list = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(".getSuppliesNameList() is error", "DAO Exception", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return list;
	}
}
