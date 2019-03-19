package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.springframework.util.StringUtils;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.PermissionDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IPermissionDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmPermission;
	/**
	 * 
	 * Purpose: 功能权限DAO实现
	 * @author:CarrieDuan
	 * @since JDK 1.6
	 * @date 2015/7/9
	 * @MaintenancePersonnel CarrieDuan
	 */
public class PermissionDAO extends GenericBaseDAO<AdmPermission> implements IPermissionDAO {
	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(PermissionDAO.class);
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.fotile.services.dao.IPermissionDAO#listByFunctionIds(java.lang.String)
	 */
	public List<PermissionDTO> listByFunctionIds(String functionId) throws DataAccessException {
		List<PermissionDTO> results  = null;
		try {
			LOGGER.debug("listByFunctionIds()", "parameter functionId:", functionId);
			SqlStatement sql = new SqlStatement();
			sql.addSelectClause("PERMISSION_ID", PermissionDTO.ATTRIBUTE.PERMISSIONID.getValue());
			sql.addSelectClause("FUNCTION_ID", PermissionDTO.ATTRIBUTE.FUNCTION_ID.getValue());
			sql.addSelectClause("ACCESS_RIGHT", PermissionDTO.ATTRIBUTE.ACCESS_RIGHT.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(this.getMySchema()).append(".ADM_PERMISSION");
			sql.setFromExpression(fromBuffer.toString());
			//functionId
			if(StringUtils.hasText(functionId)){
				StringBuffer whereBuffer = new StringBuffer();
				whereBuffer.append("FUNCTION_ID in").append(IAtomsConstants.MARK_BRACKETS_LEFT).append(functionId).append(IAtomsConstants.MARK_BRACKETS_RIGHT);
				sql.addWhereClause(whereBuffer.toString());
			}			
			sql.setOrderByExpression("FUNCTION_ID");
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			AliasBean aliasBean =sql.createAliasBean(PermissionDTO.class);
			LOGGER.debug("listByFunctionIds()", "sql:", sqlQueryBean.toString());
			//取得currentSize
			results = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listByFunctionIds()", e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE, 
					new String[] {i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return results;
	}
}
