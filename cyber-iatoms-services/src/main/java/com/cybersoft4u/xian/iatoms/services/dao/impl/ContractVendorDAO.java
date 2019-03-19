package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.springframework.util.StringUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IContractVendorDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimContractVendor;

/**
 * Purpose: 合約厂商DAO
 * @author allenchen
 * @since  JDK 1.6
 * @date   2016年3月30日
 * @MaintenancePersonnel allenchen
 */
public class ContractVendorDAO extends GenericBaseDAO<BimContractVendor> implements IContractVendorDAO {

	/**
	 * 系统日志记录物件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(ContractVendorDAO.class);

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractVendorDAO#deleteByContractId(java.lang.String)
	 */
	public void deleteByContractId(String contractId) throws DataAccessException {
		try {
			LOGGER.debug(".deleteByContractId()", "parameters:contractId=", contractId);
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			StringBuffer buffer = new StringBuffer();
			buffer.append("delete from ").append(schema).append(".BIM_CONTRACT_VENDOR ");
			sql.append(buffer.toString());
			if (StringUtils.hasText(contractId)) {
				sql.append(" where CONTRACT_ID = :contractId");
				sql.setParameter(BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue(), contractId);
				LOGGER.debug(".deleteByContractId()", "sql:", sql.toString());	
				super.getDaoSupport().updateByNativeSql(sql);
			}
		} catch (Exception e) {
			LOGGER.error(":deleteByContractId() is error.", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractVendorDAO#listVendorsByContractId(java.lang.String)
	 */
	public List<Parameter> listVendorsByContractId(String contractId) throws DataAccessException {
		List<Parameter> vendors = null;
		try {
			LOGGER.debug(".listVendorsByContractId()", "parameters:contractId=", contractId);
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("vendor.COMPANY_ID", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("company.SHORT_NAME", Parameter.FIELD_NAME);
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".BIM_CONTRACT_VENDOR vendor");
			fromBuffer.append(" left join ").append(schema).append(".BIM_COMPANY company on company.COMPANY_ID = vendor.COMPANY_ID");
			fromBuffer.append(" left join ").append(schema).append(".BIM_COMPANY_TYPE type on type.COMPANY_ID = vendor.COMPANY_ID and type.COMPANY_TYPE = :vendor ");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(contractId)) {
				sqlStatement.addWhereClause("vendor.CONTRACT_ID = :contractId", contractId);
			}
			sqlStatement.addWhereClause("company.DELETED = :deleted", IAtomsConstants.NO);
			// 增加排序
			sqlStatement.setOrderByExpression("company.SHORT_NAME asc");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("vendor", IAtomsConstants.PARAM_COMPANY_TYPE_VENDOR);
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			LOGGER.debug(".listVendorsByContractId()", "sql:", sqlQueryBean.toString());
			vendors = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listVendorsByContractId() is error.", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return vendors;
	}
	
}
