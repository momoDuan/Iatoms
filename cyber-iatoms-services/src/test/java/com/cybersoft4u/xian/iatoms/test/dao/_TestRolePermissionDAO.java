package com.cybersoft4u.xian.iatoms.test.dao;

import java.util.List;

import junit.framework.Assert;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmFunctionTypeDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.RolePermissionDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IRolePermissionDAO;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;

public class _TestRolePermissionDAO extends AbstractTestCase {
	
	/**
	 * 角色權限DAO
	 */
	private IRolePermissionDAO rolePermissionDAO;

	/**
	 * Constructor:無參構造函數
	 */
	public _TestRolePermissionDAO() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	
	/**
	 * Purpose: 測試 - 根据角色Id获得角色功能权限List
	 * @author CarrieDuan
	 * @return void
	 */
	public void testGetRolePermissionByRoleId() {
		try {
			String roleId = "123";
			List<RolePermissionDTO> rolePermissionDTOs = this.rolePermissionDAO.listRolePermissionByRoleId(roleId);
			Assert.assertEquals(0, rolePermissionDTOs.size());
		} catch (Exception e) {
			System.out.println("_TestRolePermissionDAO.rolePermissionDTOs() is error" + e);
			e.printStackTrace();
		}
	}

	/**
	 * Purpose:刪除角色下所有的權限
	 * @author CarrieDuan
	 * @return void
	 */
	public void testDeleteAll() {
		try {
			String roleId = "1471319933792-0065";
			this.rolePermissionDAO.deleteAll(roleId);
		} catch (Exception e) {
			System.out.println("_TestRolePermissionDAO.rolePermissionDTOs() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the rolePermissionDAO
	 */
	public IRolePermissionDAO getRolePermissionDAO() {
		return rolePermissionDAO;
	}

	/**
	 * @param rolePermissionDAO the rolePermissionDAO to set
	 */
	public void setRolePermissionDAO(IRolePermissionDAO rolePermissionDAO) {
		this.rolePermissionDAO = rolePermissionDAO;
	}
	
	
}
