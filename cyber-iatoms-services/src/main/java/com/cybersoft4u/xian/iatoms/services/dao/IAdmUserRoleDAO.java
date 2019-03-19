package com.cybersoft4u.xian.iatoms.services.dao;

import com.cybersoft4u.xian.iatoms.services.dmo.AdmUserRole;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

/**
 * Purpose:用戶角色DAO interface 
 * @author evanliu
 * @since  JDK 1.6
 * @date   2016年4月1日
 * @MaintenancePersonnel evanliu
 */
public interface IAdmUserRoleDAO extends IGenericBaseDAO<AdmUserRole> {
	/**
	 * Purpose: 刪除用戶所有的角色
	 * @author evanliu
	 * @param userId:用戶編號
	 * @throws DataAccessException:出錯後, 丟出DataAccessException
	 * @return void
	 */
	public void deleteAll(String userId) throws DataAccessException;
}
