package com.cybersoft4u.xian.iatoms.services.dao.impl;

import org.springframework.util.StringUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IContractTypeDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimContractType;

/**
 * Purpose:合約类型DAO 
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2016-8-12
 * @MaintenancePersonnel CarrieDuan
 */
public class ContractTypeDAO extends GenericBaseDAO<BimContractType> implements IContractTypeDAO {

	/**
	 * 系统日志记录物件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(ContractTypeDAO.class);

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractTypeDAO#deleteByContractId(java.lang.String)
	 */
	public void deleteByContractId(String contractId) throws DataAccessException {
		try {
			LOGGER.debug(".deleteByContractId()", "parameters:contractId=", contractId);
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			StringBuffer buffer = new StringBuffer();
			buffer.append("delete from ").append(schema).append(".BIM_CONTRACT_TYPE ");
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
}
