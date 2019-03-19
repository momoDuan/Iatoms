package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Parameter;
import cafe.core.config.GenericConfigManager;
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
import com.cybersoft4u.xian.iatoms.common.bean.dto.WarehouseDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.WarehouseFormDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IWarehouseDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimWarehouse;

/**
 * Purpose: 倉庫DAO
 * @author ElvaHe
 * @since JDK 1.6
 * @date 2016年6月1日
 * @MaintenancePersonnel ElvaHe
 */
public class WarehouseDAO extends GenericBaseDAO<BimWarehouse> implements IWarehouseDAO {
	/**
	 * 日誌記錄器
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.DAO, WarehouseDAO.class);

	
	/**
	 * Constructor:無參構造
	 */
	public WarehouseDAO() {
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IWarehouseDAO#listBy(
	 *      java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@Override
	public List<WarehouseDTO> listBy(String queryCompanyId, String queryName, Integer pageSize, Integer pageIndex, String sort, String orderby) throws DataAccessException {
		List<WarehouseDTO> warehouseDTOs = null;
		try {
			LOGGER.debug("listBy()", "parameters:queryCompanyId=", queryCompanyId);
			LOGGER.debug("listBy()", "parameters:queryName=", queryName);
			LOGGER.debug("listBy()", "parameters:pageSize=", String.valueOf(pageSize));
			LOGGER.debug("listBy()", "parameters:pageIndex=", String.valueOf(pageIndex));
			LOGGER.debug("listBy()", "parameters:sort=", sort);
			LOGGER.debug("listBy()", "parameters:orderby=", orderby);
			SqlStatement sqlStatement = new SqlStatement();
			//SqlStatement sql = new SqlStatement();
			String schema = this.getMySchema();
			StringBuffer stringBuffer = new StringBuffer();
			//拼接SQL語句
			sqlStatement.addSelectClause("w.WAREHOUSE_ID", WarehouseDTO.ATTRIBUTE.WAREHOUSE_ID.getValue());
			sqlStatement.addSelectClause("w.COMPANY_ID", WarehouseDTO.ATTRIBUTE.COMPANY_ID.getValue());
			sqlStatement.addSelectClause("w.NAME", WarehouseDTO.ATTRIBUTE.NAME.getValue());
			sqlStatement.addSelectClause("w.CONTACT", WarehouseDTO.ATTRIBUTE.CONTACT.getValue());
			sqlStatement.addSelectClause("w.TEL", WarehouseDTO.ATTRIBUTE.TEL.getValue());
			sqlStatement.addSelectClause("w.FAX", WarehouseDTO.ATTRIBUTE.FAX.getValue());
			sqlStatement.addSelectClause("ISNULL(base.ITEM_NAME,'')+w.ADDRESS", WarehouseDTO.ATTRIBUTE.ADDRESS.getValue());
			sqlStatement.addSelectClause("w.COMMENT", WarehouseDTO.ATTRIBUTE.COMMENT.getValue());
			sqlStatement.addSelectClause("c.SHORT_NAME", WarehouseDTO.ATTRIBUTE.COMPANY_NAME.getValue());
			sqlStatement.addSelectClause("w.DELETED", WarehouseDTO.ATTRIBUTE.DELETED.getValue());
			stringBuffer.append(schema).append(".BIM_WAREHOUSE AS w LEFT JOIN ").append(schema).append(".BIM_COMPANY AS c ON w.COMPANY_ID = c.COMPANY_ID  LEFT JOIN ");
			stringBuffer.append(schema).append(".BIM_COMPANY_TYPE AS companyType ON c.COMPANY_ID = companyType.COMPANY_ID LEFT JOIN ");
			stringBuffer.append(schema).append(".BASE_PARAMETER_ITEM_DEF base ON base.ITEM_VALUE = w.LOCATION AND base.BPTD_CODE = :bptdCode");
			/*sqlStatement.addFromExpression(
					schema + ".BIM_WAREHOUSE AS w LEFT JOIN " 
				+ schema + ".BIM_COMPANY AS c ON w.COMPANY_ID = c.COMPANY_ID  LEFT JOIN " 
				+ schema + ".BIM_COMPANY_TYPE AS companyType ON c.COMPANY_ID = companyType.COMPANY_ID LEFT JOIN "
				+ schema + ".BASE_PARAMETER_ITEM_DEF base ON base.ITEM_VALUE = w.LOCATION AND base.BPTD_CODE = :bptdCode");*/
			sqlStatement.addFromExpression(stringBuffer.toString());
			
			//若用戶選擇了廠商就拼接關於客戶的查詢條件
			if(StringUtils.hasText(queryCompanyId)){
				sqlStatement.addWhereClause(" w.COMPANY_ID =:queryCompanyId");
			}
			//若用戶輸入了倉庫名稱就拼接關於相應的查詢條件
			if(StringUtils.hasText(queryName)){
				sqlStatement.addWhereClause("w.NAME LIKE :queryName", queryName + IAtomsConstants.MARK_PERCENT);
			}
			sqlStatement.addWhereClause("isnull(w.DELETED, 'N') = :deleted AND companyType.COMPANY_TYPE = :comanytype ");
			//打印子表SQL語句
			//LOGGER.debug("listBy()", "SQL ：", sqlStatement.toString());
			//stringBuffer.delete(0, stringBuffer.length());
			/*sql.addSelectClause("a.warehouseId,a.companyId,a.name,a.contact,a.tel,a.fax,a.address,a.comment,a.companyName,a.deleted");
			stringBuffer.append("(").append(sqlStatement.toString()).append(") a");
			sql.addFromExpression(stringBuffer.toString());*/
			//排序方式
			if ((StringUtils.hasText(sort)) && (StringUtils.hasText(orderby))) {
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append(sort).append(IAtomsConstants.MARK_SPACE).append(orderby);
				sqlStatement.setOrderByExpression(stringBuffer.toString());
			} else {
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append(WarehouseFormDTO.PARAM_PAGE_SORT).append(IAtomsConstants.MARK_SPACE).append(IAtomsConstants.PARAM_PAGE_ORDER);
				sqlStatement.setOrderByExpression(stringBuffer.toString());
			}
			//分頁
			sqlStatement.setPageSize(pageSize.intValue());
			sqlStatement.setStartPage(pageIndex.intValue() - 1);
			//打印SQL語句
			LOGGER.debug("listBy()", "SQL : ", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("deleted", IAtomsConstants.NO);
			sqlQueryBean.setParameter("comanytype", IAtomsConstants.PARAM_COMPANY_TYPE_VENDOR);
			sqlQueryBean.setParameter("bptdCode", IATOMS_PARAM_TYPE.LOCATION.getCode());
			if(StringUtils.hasText(queryCompanyId)){
				sqlQueryBean.setParameter("queryCompanyId", queryCompanyId);
			}
			AliasBean aliasBean = sqlStatement.createAliasBean(WarehouseDTO.class);
			warehouseDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listBy()", "error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_BIM_WAREHOUSE)}, e);
		}
		return warehouseDTOs;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IWarehouseDAO#count(java.lang.String, java.lang.String)
	 */
	@Override
	public Integer count(String queryCompanyId, String queryName) throws DataAccessException {
		Integer count = null;
		try {
			LOGGER.debug("count()", "parameters:queryCompanyId=", queryCompanyId);
			LOGGER.debug("count()", "parameters:queryName=", queryName);
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			StringBuffer stringBuffer = new StringBuffer();
			sqlStatement.addSelectClause("COUNT(1)");
			stringBuffer.append(schema).append(".BIM_WAREHOUSE warehouse LEFT JOIN ").append(schema).append(".BIM_COMPANY_TYPE companytype ON warehouse.COMPANY_ID = companytype.COMPANY_ID ");
			/*sqlStatement.addFromExpression(
					schema + ".BIM_WAREHOUSE warehouse LEFT JOIN " 
				+ schema + ".BIM_COMPANY_TYPE companytype ON warehouse.COMPANY_ID = companytype.COMPANY_ID ");*/
			sqlStatement.addFromExpression(stringBuffer.toString());
			sqlStatement.addWhereClause("warehouse.COMPANY_ID = companytype.COMPANY_ID ");
			sqlStatement.addWhereClause("companytype.COMPANY_TYPE = :companyType", IAtomsConstants.PARAM_COMPANY_TYPE_VENDOR);
			
			//若用戶選擇了廠商就拼接關於客戶的查詢條件
			if(StringUtils.hasText(queryCompanyId)){
				sqlStatement.addWhereClause(" warehouse.COMPANY_ID =:queryCompanyId", queryCompanyId);
			}
			//若用戶輸入了倉庫名稱就拼接關於相應的查詢條件
			if(StringUtils.hasText(queryName)){
				/*stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append(" warehouse.NAME LIKE '").append(queryName).append("%'");
				//sqlStatement.addWhereClause(" warehouse.NAME LIKE '" + queryName + "%'");
				sqlStatement.addWhereClause(stringBuffer.toString());*/
				sqlStatement.addWhereClause("warehouse.NAME LIKE :queryName", queryName + IAtomsConstants.MARK_PERCENT);
			}
			sqlStatement.addWhereClause("isnull(warehouse.DELETED, 'N') = :deleted", IAtomsConstants.NO);
			//打印SQL語句
			LOGGER.debug("count()", "SQL : ", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				count = result.get(0);
			}
		} catch (Exception e) {
			LOGGER.error("count()", "error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_BIM_WAREHOUSE)}, e);
		}
		return count;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IWarehouseDAO#isCheck(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isCheck(String warehouseId, String name, String companyId) throws DataAccessException {
		boolean hasName = false;
		try {
			LOGGER.debug("checkRepeat()", "parameters:warehouseId=", warehouseId);
			LOGGER.debug("checkRepeat()", "parameters:name=", name);
			LOGGER.debug("checkRepeat()", "parameters:companyId=", companyId);
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			StringBuffer stringBuffer = new StringBuffer();
			sqlStatement.addSelectClause("count(*)");
			stringBuffer.append(schema).append(".BIM_WAREHOUSE warehouse");
			//sqlStatement.addFromExpression(schema + ".BIM_WAREHOUSE warehouse");
			sqlStatement.addFromExpression(stringBuffer.toString());
			sqlStatement.addWhereClause("warehouse.COMPANY_ID = :companyId", companyId);
			sqlStatement.addWhereClause("warehouse.NAME = :name", name);
			sqlStatement.addWhereClause("isnull(warehouse.DELETED, 'N') = :deleted", IAtomsConstants.NO);
			//修改時
			if(StringUtils.hasText(warehouseId)){
				sqlStatement.addWhereClause("warehouse.WAREHOUSE_ID < > :warehouseId", warehouseId);
			}
			//打印SQL語句
			LOGGER.debug("check()", "sql:", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			//若存在結果
			if(!CollectionUtils.isEmpty(result)){
				int count = result.get(0).intValue();
				//若值大於0表示存在
				if(count > 0 ){
					hasName = true;
				}
			}
		} catch (Exception e) {
			LOGGER.error("check()", "error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_BIM_WAREHOUSE)}, e);
		}
		return hasName;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IWarehouseDAO#getWarehouseList()
	 */
	/*public List<Parameter> getWarehouseList(String userId) throws DataAccessException {
		LOGGER.debug("getWarehouseList()", "parameters:userId=", userId);
		List<Parameter> userWarehouseList = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("w.WAREHOUSE_ID", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("c.short_name + '-' + w.NAME", Parameter.FIELD_NAME);
			if(StringUtils.hasText(userId)){
				sqlStatement.setFromExpression(schema + ".BIM_WAREHOUSE w left join " + schema + ".BIM_COMPANY c on w.company_id=c.company_id LEFT JOIN " + schema + ".BIM_COMPANY_TYPE type ON type.COMPANY_ID=c.COMPANY_ID ," + schema + ".ADM_USER_WAREHOUSE uw");
				sqlStatement.addWhereClause("w.WAREHOUSE_ID = uw.WAREHOUSE_ID");
				sqlStatement.addWhereClause("isnull(uw.USER_ID, 'N') = :userId", userId);
			} else {
				sqlStatement.setFromExpression(schema + ".BIM_WAREHOUSE w left join " + schema + ".BIM_COMPANY c on w.company_id=c.company_id LEFT JOIN " + schema + ".BIM_COMPANY_TYPE type ON type.COMPANY_ID=c.COMPANY_ID ");
			}
			sqlStatement.addWhereClause("isNUll(w.deleted ,'N') =:deleted", IAtomsConstants.NO);
			sqlStatement.addWhereClause("type.COMPANY_TYPE =:type", IAtomsConstants.PARAM_COMPANY_TYPE_VENDOR);
			sqlStatement.setOrderByExpression("c.short_name");
			AliasBean aliasBean = new AliasBean(Parameter.class);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			userWarehouseList = super.getDaoSupport().findByNativeSql(sqlQueryBean,aliasBean);
			LOGGER.debug(".getWarehouseList()", "sql:", sqlQueryBean.toString());
		} catch (Exception e) {
			LOGGER.error("getWarehouseList()", "error.", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_BIM_WAREHOUSE)}, e);
		}
		return userWarehouseList;
	}*/

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IWarehouseDAO#listByUserId(java.lang.String)
	 */
	/*public List<Parameter> getListByUserId(String userId) throws DataAccessException {
		LOGGER.debug("getListByUserId()", "parameters:userId=", userId);
		List<Parameter> userWarehouseList = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			StringBuffer stringBuffer = new StringBuffer();
			sqlStatement.addSelectClause("w.WAREHOUSE_ID", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("c.short_name + '-' + w.NAME", Parameter.FIELD_NAME);
			if(StringUtils.hasText(userId)){
				stringBuffer.append(schema).append(".BIM_WAREHOUSE w left join ").append(schema).append(".BIM_COMPANY c on w.company_id=c.company_id LEFT JOIN ").append(schema);
				stringBuffer.append(".BIM_COMPANY_TYPE type ON type.COMPANY_ID=c.COMPANY_ID ,").append(schema).append(".ADM_USER_WAREHOUSE uw");
				sqlStatement.setFromExpression(stringBuffer.toString());
				//sqlStatement.setFromExpression(schema + ".BIM_WAREHOUSE w left join " + schema + ".BIM_COMPANY c on w.company_id=c.company_id LEFT JOIN " + schema + ".BIM_COMPANY_TYPE type ON type.COMPANY_ID=c.COMPANY_ID ," + schema + ".ADM_USER_WAREHOUSE uw");
				sqlStatement.addWhereClause("w.WAREHOUSE_ID = uw.WAREHOUSE_ID");
				sqlStatement.addWhereClause("isnull(uw.USER_ID, 'N') = :userId", userId);
			} else {
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append(schema).append(".BIM_WAREHOUSE w left join ").append(schema).append(".BIM_COMPANY c on w.company_id=c.company_id LEFT JOIN ").append(schema).append(".BIM_COMPANY_TYPE type ON type.COMPANY_ID=c.COMPANY_ID ");
				sqlStatement.setFromExpression(stringBuffer.toString());
				//sqlStatement.setFromExpression(schema + ".BIM_WAREHOUSE w left join " + schema + ".BIM_COMPANY c on w.company_id=c.company_id LEFT JOIN " + schema + ".BIM_COMPANY_TYPE type ON type.COMPANY_ID=c.COMPANY_ID ");
			}
			sqlStatement.addWhereClause("isNUll(w.deleted ,'N') =:deleted", IAtomsConstants.NO);
			sqlStatement.addWhereClause("type.COMPANY_TYPE =:type", IAtomsConstants.PARAM_COMPANY_TYPE_VENDOR);
			sqlStatement.setOrderByExpression("c.short_name");
			AliasBean aliasBean = new AliasBean(Parameter.class);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			userWarehouseList = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			LOGGER.debug("getListByUserId()", "sql:", sqlQueryBean.toString());
		} catch (Exception e) {
			LOGGER.error("getListByUserId()", "error.", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_BIM_WAREHOUSE)}, e);
		}
		return userWarehouseList;
	}*/
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IWarehouseDAO#checWarehouse(java.lang.String)
	 */
	@Override
	public boolean isCheckWarehouse(String warehouseId) throws DataAccessException { 
		LOGGER.debug("isCheckWarehouse()", "parameters:warehouseId = ", warehouseId);
		boolean isHave = false;
		try {
			//document.getElementsByName("dtidType").checked = '<%=IAtomsConstants.PARAM_IATOMS_DTID_TYPE%>';
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			StringBuffer stringBuffer = new StringBuffer();
			sqlStatement.addSelectClause("count(1)");
			stringBuffer.append(schema).append(".DMM_REPOSITORY re");
			//sqlStatement.addFromExpression(schema + ".DMM_REPOSITORY re");
			sqlStatement.addFromExpression(stringBuffer.toString());
//			sqlStatement.addWhereClause("isnull(re.DELETED,'N') = :deleted");
			if (StringUtils.hasText(warehouseId)) {
				sqlStatement.addWhereClause("re.WAREHOUSE_ID = :warehouseId", warehouseId);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
//			sqlQueryBean.setParameter("deleted", IAtomsConstants.NO);
			//打印SQL語句
			LOGGER.debug("checWarehouse()", "sql:", sqlStatement.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			//若存在結果
			if(!CollectionUtils.isEmpty(result)){
				int count = result.get(0).intValue();
				//若值大於0表示存在
				if(count > 0 ){
					isHave = true;
				}
			}
		} catch (Exception e) {
			LOGGER.error("checWarehouse()", "error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_BIM_WAREHOUSE)}, e);
		}
		return isHave;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IWarehouseDAO#getWarehouseByUserId(java.lang.String)
	 */
	@Override
	public List<Parameter> getWarehouseByUserId(String userId) throws DataAccessException {
		LOGGER.debug("getWarehouseByUserId()", "parameters:userId=", userId);
		long startQueryWarehouseTime = System.currentTimeMillis();
		List<Parameter> userWarehouseList = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			StringBuffer stringBuffer = new StringBuffer();
			sqlStatement.addSelectClause("w.WAREHOUSE_ID", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("c.short_name + '-' + w.NAME", Parameter.FIELD_NAME);
			if(StringUtils.hasText(userId)){
				stringBuffer.append(schema).append(".BIM_WAREHOUSE w left join ").append(schema).append(".BIM_COMPANY c on w.company_id=c.company_id LEFT JOIN ").append(schema);
				stringBuffer.append(".BIM_COMPANY_TYPE type ON type.COMPANY_ID=c.COMPANY_ID ,").append(schema).append(".ADM_USER_WAREHOUSE uw");
				sqlStatement.setFromExpression(stringBuffer.toString());
				sqlStatement.addWhereClause("w.WAREHOUSE_ID = uw.WAREHOUSE_ID");
				sqlStatement.addWhereClause("uw.USER_ID = :userId", userId);
			} else {
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append(schema).append(".BIM_WAREHOUSE w left join ").append(schema).append(".BIM_COMPANY c on w.company_id=c.company_id LEFT JOIN ").append(schema).append(".BIM_COMPANY_TYPE type ON type.COMPANY_ID=c.COMPANY_ID ");
				sqlStatement.setFromExpression(stringBuffer.toString());
			}
			sqlStatement.addWhereClause("isNUll(w.deleted, 'N') =:deleted", IAtomsConstants.NO);
			sqlStatement.addWhereClause("type.COMPANY_TYPE =:type", IAtomsConstants.PARAM_COMPANY_TYPE_VENDOR);
			sqlStatement.setOrderByExpression("c.short_name, w.NAME");
			AliasBean aliasBean = new AliasBean(Parameter.class);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			userWarehouseList = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			LOGGER.debug("getWarehouseByUserId()", "sql:", sqlQueryBean.toString());
		} catch (Exception e) {
			LOGGER.error("getWarehouseByUserId()", "error.", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_BIM_WAREHOUSE)}, e);
		}
		long endQueryWarehouseTime = System.currentTimeMillis();
		LOGGER.debug("calculate time --> load", "DAO getWarehouseByUserId:" + (endQueryWarehouseTime - startQueryWarehouseTime));
		return userWarehouseList;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IWarehouseDAO#isNoData()
	 */
	@Override
	public boolean isNoData() throws DataAccessException {
		boolean isNoData = false;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("COUNT(1)");
			sqlStatement.addFromExpression(schema + ".BIM_WAREHOUSE");
			//打印SQL語句
			LOGGER.debug("isNoData()", "SQL : ", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				Integer count = result.get(0);
				if(count == 0){
					isNoData = true;
				}
			}
		} catch (Exception e) {
			LOGGER.error("isNoData()", "error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE);
		}
		return isNoData;
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IWarehouseDAO#listWarehouseByName(java.lang.String)
	 */
	@Override
	public List<Parameter> listWarehouseByName(String warehouseName) throws DataAccessException {
		LOGGER.debug(".listWarehouseByName()", "parameters:warehouseName=" + warehouseName);
		List<Parameter> result = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("warehouse.WAREHOUSE_ID", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("warehouse.NAME", Parameter.FIELD_NAME);
			sqlStatement.addFromExpression(schema + ".BIM_WAREHOUSE warehouse");
			if (StringUtils.hasText(warehouseName)) {
				sqlStatement.addWhereClause("warehouse.NAME =:warehouseName", warehouseName);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			LOGGER.debug(".listWarehouseByName()", "SQL---------->"+sqlStatement.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".listWarehouseByName() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,e);
		}
		return result;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IWarehouseDAO#deleteTransferData()
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
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_FAULT_DESC; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_FAULT_COM; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY_DESC; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY_COMM; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".ADM_USER_WAREHOUSE; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_ASSET_IN_LIST; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_ASSET_IN_INFO; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".BIM_WAREHOUSE; ");
			
			LOGGER.debug("deleteTransferData()", "sql:" + sqlQueryBean.toString());
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("deleteTransferData()", e.getMessage(), e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
	}
}
