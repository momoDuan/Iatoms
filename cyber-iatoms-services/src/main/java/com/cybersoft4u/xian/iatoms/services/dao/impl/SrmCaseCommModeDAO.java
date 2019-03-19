package com.cybersoft4u.xian.iatoms.services.dao.impl;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTransactionParameterDTO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseCommModeDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseCommMode;

import cafe.core.bean.CoreMessageCode;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

/**
 * Purpose: SRM_案件處理中設備支援功能檔 DAO 實現類
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2017/2/24
 * @MaintenancePersonnel CrissZhang
 */
public class SrmCaseCommModeDAO extends GenericBaseDAO<SrmCaseCommMode> implements ISrmCaseCommModeDAO{

	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(SrmCaseCommModeDAO.class);
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseCommModeDAO#deleteAll(java.lang.String)
	 */
	@Override
	public void deleteAll(String caseId) throws DataAccessException {
		try {
			LOGGER.debug("deleteAll()", "parameters:caseId=" + caseId);
			if (StringUtils.hasText(caseId)) {
				SqlQueryBean sqlQueryBean = new SqlQueryBean("delete ");
				String schema = this.getMySchema();
				sqlQueryBean.append(schema).append(".SRM_CASE_COMM_MODE where case_id=:caseId");
				sqlQueryBean.setParameter(SrmCaseTransactionParameterDTO.ATTRIBUTE.CASE_ID.getValue(), caseId);
				LOGGER.debug("deleteAll()", "sql:" + sqlQueryBean.toString());
				this.getDaoSupport().updateByNativeSql(sqlQueryBean);
				this.getDaoSupport().flush();
			}
		} catch (Exception e) {
			LOGGER.error("deleteAll()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

}
