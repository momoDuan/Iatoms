package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

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
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmPwdHistoryDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmPwdHistoryDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmPwdHistory;

/**
 * Purpose: 密碼歷史記錄DAO
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2016年7月6日
 * @MaintenancePersonnel HermanWang
 */
public class AdmPwdHistoryDAO extends GenericBaseDAO<AdmPwdHistory> implements IAdmPwdHistoryDAO {
	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(AdmPwdHistoryDAO.class);
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmPwdHistoryDAO#listByUserId(java.lang.String, java.lang.Integer)
	 */
	@Override
	public List<AdmPwdHistoryDTO> listByUserId(String userId, Integer times)
			throws DataAccessException {
		List<AdmPwdHistoryDTO> admPwdHistoryDTOs = null;
		try {
			LOGGER.debug(".listByUserId()", "parameters:userId=" + userId);
			LOGGER.debug(".listByUserId()", "parameters:times=" + times);
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("PWD_HIS_ID", AdmPwdHistoryDTO.ATTRIBUTE.PWD_HIS_ID.getValue());
			sqlStatement.addSelectClause("USER_ID", AdmPwdHistoryDTO.ATTRIBUTE.USER_ID.getValue());
			sqlStatement.addSelectClause("PASSWORD", AdmPwdHistoryDTO.ATTRIBUTE.PASSWORD.getValue());
			sqlStatement.addSelectClause("CREATE_DATE", AdmPwdHistoryDTO.ATTRIBUTE.CREATE_DATE.getValue());
			StringBuffer buffer = new StringBuffer();
			buffer.append(this.getMySchema()).append(".ADM_PWD_HISTORY");
			sqlStatement.addFromExpression(buffer.toString());
			sqlStatement.addWhereClause("USER_ID=:userId", userId);
			if (times != null && times.intValue() > 0) {
				sqlStatement.setStartPage(0);
				sqlStatement.setPageSize(times.intValue());
			}
			sqlStatement.setOrderByExpression("CREATE_DATE desc");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(AdmPwdHistoryDTO.class);
			LOGGER.debug(".listByUserId()", "sql:" + sqlQueryBean.toString());
			admPwdHistoryDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(".listByUserId() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_PWD_HISTORY)}, e);
		}
		return admPwdHistoryDTOs;
	}
}
