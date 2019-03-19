package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

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
import com.cybersoft4u.xian.iatoms.common.bean.dto.RolePermissionDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IRolePermissionDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmRolePermission;

/**
 * Purpose: 角色權限
 * @author CarrieDuan
 * @since  JDK 1.6
 * @date   2016年7月13日
 * @MaintenancePersonnel CarrieDuan
 */
public class RolePermissionDAO extends GenericBaseDAO<AdmRolePermission> implements
		IRolePermissionDAO {
	
	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(RolePermissionDAO.class);
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IRolePermissionDAO#listRolePermissionByRoleId(java.lang.String)
	 */
	public List<RolePermissionDTO> listRolePermissionByRoleId(String roleId) throws DataAccessException {
		List<RolePermissionDTO> results  = null;
		try {
			LOGGER.debug("listRolePermissionByRoleId() roleId:", roleId);
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("rp.PERMISSION_ID", RolePermissionDTO.ATTRIBUTE.PERMISSION_ID.getValue());
			sqlStatement.addSelectClause("rp.ROLE_ID", RolePermissionDTO.ATTRIBUTE.ROLE_ID.getValue());
//			sqlStatement.addSelectClause("rp.TYPE", RolePermissionDTO.ATTRIBUTE.TYPE.getValue());
			sqlStatement.addSelectClause("p.FUNCTION_ID", RolePermissionDTO.ATTRIBUTE.FUNCTION_ID.getValue());
//			sqlStatement.addSelectClause("rp.STATUS", RolePermissionDTO.ATTRIBUTE.STATUS.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".ADM_ROLE_PERMISSION rp left outer join ");
			fromBuffer.append(schema).append(".ADM_PERMISSION p on rp.PERMISSION_ID = p.PERMISSION_ID ");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if(StringUtils.hasText(roleId)){
				sqlStatement.addWhereClause("ROLE_ID=:roleId", roleId);
			}
			LOGGER.debug("listRolePermissionByRoleId() sql:", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(RolePermissionDTO.class);
			results = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listRolePermissionByRoleId()---", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, 
					new String[] {i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_ROLE_PERMISSION)}, e);
		}
		return results;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IRolePermissionDAO#deleteAll(java.lang.String)
	 */
	public void deleteAll(String roleId) throws DataAccessException {
		try {
			if (StringUtils.hasText(roleId)) {
				SqlQueryBean sqlQueryBean = new SqlQueryBean("delete ");
				String schma = this.getMySchema();
				sqlQueryBean.append(schma).append(".ADM_ROLE_PERMISSION where ROLE_ID=:roleId");
				sqlQueryBean.setParameter(RolePermissionDTO.ATTRIBUTE.ROLE_ID.getValue(), roleId);
				LOGGER.debug("deleteAll()", "parameters:roleId=", roleId);
				LOGGER.debug(".deleteAll()", "sql:", sqlQueryBean.toString());
				this.getDaoSupport().updateByNativeSql(sqlQueryBean);
				this.getDaoSupport().flush();
			}
		} catch (Exception e) {
			LOGGER.error(".deleteAll() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}	
}
