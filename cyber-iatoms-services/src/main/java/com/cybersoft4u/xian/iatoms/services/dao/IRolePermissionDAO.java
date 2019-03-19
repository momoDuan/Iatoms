package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.RolePermissionDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmRolePermission;
/**
 * 
 * Purpose: 角色功能权限DAO
 * @author:CarrieDuan
 * @since  JDK 1.6
 * @date   2015/7/9
 * @MaintenancePersonnel CarrieDuan
 */
public interface IRolePermissionDAO extends IGenericBaseDAO<AdmRolePermission> {
	/**
	 * 
	 * Purpose:根据角色Id获得角色功能权限List
	 * @author:CarrieDuan
	 * @param roleId:角色Id
	 * @throws DataAccessException:出错时返回DataAccessException
	 * @return List<RolePermissionDTO>:返回角色Id所对应List<RolePermissionDTO>
	 */
	public List<RolePermissionDTO> listRolePermissionByRoleId(String roleId) throws DataAccessException;
	/**
	 * Purpose: 刪除角色下所有的權限
	 * @author CarrieDuan
	 * @param roleId:角色編號
	 * @throws DataAccessException:出錯後, 丟出DataAccessException
	 * @return void
	 */
	public void deleteAll(String roleId) throws DataAccessException;
}
