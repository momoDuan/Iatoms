package com.cybersoft4u.xian.iatoms.services.dao.impl;

import org.springframework.util.StringUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserRoleDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmUserRole;


/**
 * Purpose: 用戶角色DAO 
 * @author evanliu
 * @since  JDK 1.6
 * @date   2016年4月1日
 * @MaintenancePersonnel evanliu
 */
public class AdmUserRoleDAO extends GenericBaseDAO<AdmUserRole> implements
		IAdmUserRoleDAO {
	
	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog logger = CafeLogFactory.getLog(AdmUserRoleDAO.class);

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmUserRoleDAO#deleteAll(java.lang.String)
	 */
	@Override
	public void deleteAll(String userId) throws DataAccessException {
		try {
			if (StringUtils.hasText(userId)) {
				SqlQueryBean sqlQueryBean = new SqlQueryBean("delete ");
				String schma = this.getMySchema();
				sqlQueryBean.append(schma).append(".Adm_User_Role where user_Id=:userId");
				sqlQueryBean.setParameter(AdmUserDTO.ATTRIBUTE.USER_ID.getValue(), userId);
				logger.debug(this.getClass().getName() + ".deleteAll()", "parameters:userId=" + userId);
				logger.debug(this.getClass().getName() + ".deleteAll()", "sql:" + sqlQueryBean.toString());
				this.getDaoSupport().updateByNativeSql(sqlQueryBean);
				this.getDaoSupport().flush();
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ".deleteAll() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,e);
		}
	}
}
