package com.cybersoft4u.xian.iatoms.test.dao;

import java.util.List;

import junit.framework.Assert;

import com.cybersoft4u.xian.iatoms.common.bean.dto.PermissionDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IPermissionDAO;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;
/**
 * Purpose: 測試類-測試權限DAO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2016/8/16
 * @MaintenancePersonnel CarrieDuan
 */
public class _TestPermissionDAO extends AbstractTestCase {
 
	/**
	 * 注入權限DAO
	 */
	private IPermissionDAO permissionDAO;
	
	/**
	 * Constructor: 無參構造函數
	 */
	public _TestPermissionDAO() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	
	/**
	 * Purpose:測試-抓取功能清单对应的功能权限
	 * @author CarrieDuan
	 * @return void
	 */
	public void testListByFunctionIds() {
		try {
			String functionId = "'ADM01010','ADM01020'";
			List<PermissionDTO> permissionDTOs = this.permissionDAO.listByFunctionIds(functionId);
//			Assert.assertEquals(10, permissionDTOs.size());
		} catch (Exception e) {
			System.out.println("_TestPermissionDAO.testListByFunctionIds() is error" + e);
			e.printStackTrace();
		}
	}

	/**
	 * @return the permissionDAO
	 */
	public IPermissionDAO getPermissionDAO() {
		return permissionDAO;
	}

	/**
	 * @param permissionDAO the permissionDAO to set
	 */
	public void setPermissionDAO(IPermissionDAO permissionDAO) {
		this.permissionDAO = permissionDAO;
	}
	
	
}
