package com.cybersoft4u.xian.iatoms.services.dao.impl;


import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserWarehouseDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmUserWarehouse;

/**
 * Purpose: 用户控管资料DAO实现类
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016/8/19
 * @MaintenancePersonnel CrissZhang
 */
public class AdmUserWarehouseDAO extends GenericBaseDAO<AdmUserWarehouse> implements IAdmUserWarehouseDAO{

	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(AdmUserWarehouseDAO.class);

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmUserWarehouseDAO#deleteAll(java.lang.String)
	 */
	@Override
	public void deleteAll(String userId) throws DataAccessException {
		try {
			LOGGER.debug("deleteAll()", "parameters:userId=" + userId);
			if (StringUtils.hasText(userId)) {
				SqlQueryBean sqlQueryBean = new SqlQueryBean("delete ");
				String schema = this.getMySchema();
				sqlQueryBean.append(schema).append(".ADM_USER_WAREHOUSE where user_Id=:userId");
				sqlQueryBean.setParameter(AdmUserDTO.ATTRIBUTE.USER_ID.getValue(), userId);
				LOGGER.debug("deleteAll()", "sql:" + sqlQueryBean.toString());
				this.getDaoSupport().updateByNativeSql(sqlQueryBean);
				this.getDaoSupport().flush();
			}
		} catch (Exception e) {
			LOGGER.error("deleteAll()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmUserWarehouseDAO#isUserWarehouse(java.lang.String, java.lang.String)
	 */
	public boolean isUserWarehouse(String serialNumber, String userId) throws DataAccessException {
		try {
			LOGGER.debug("checkUserWarehouse()", "parameters:serialNumber=" + serialNumber);
			LOGGER.debug("checkUserWarehouse()", "parameters:userId=" + userId);
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("count(1)");
			StringBuilder builder = new StringBuilder();
			builder.append(schema).append(".ADM_USER_WAREHOUSE userWare left join ");
			builder.append(schema).append(".DMM_REPOSITORY repository on userWare.WAREHOUSE_ID = repository.WAREHOUSE_ID");
			/*sqlStatement.addFromExpression(schema + ".ADM_USER_WAREHOUSE userWare"
					+ " left join " + schema + ".DMM_REPOSITORY repository on userWare.WAREHOUSE_ID = repository.WAREHOUSE_ID");*/
			sqlStatement.addFromExpression(builder.toString());
			if (StringUtils.hasText(serialNumber)) {
				sqlStatement.addWhereClause("repository.SERIAL_NUMBER = :serialNumber", serialNumber);
			}
			if (StringUtils.hasText(userId)) {
				sqlStatement.addWhereClause("userWare.USER_ID = :userId", userId);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug("checkUserWarehouse()", "sql:" + sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				if (result.get(0).intValue() > 0) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			LOGGER.error("checkUserWarehouse()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
}
