package com.cybersoft4u.xian.iatoms.test.dao;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import cafe.core.bean.Parameter;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmFunctionTypeDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IFunctionTypeDAO;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;
/**
 * Purpose: 測試類-測試功能設定DAO
 * @author Carrie Duan
 * @since  JDK 1.7
 * @date   2016/8/16
 * @MaintenancePersonnel cybersoft
 */
public class _TestFunctionTypeDAO extends AbstractTestCase {
	
	/**
	 * 功能類型DAO
	 */
	private IFunctionTypeDAO functionTypeDAO;
	
	/**
	 * Constructor:無參構造函數
	 */
	public _TestFunctionTypeDAO() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	
	/**
	 * Purpose:測試 - 查詢用於頁面展示的FunctionTypeDTO列表
	 * @author CarrieDuan
	 * @return void
	 */
	public void getFunctionTypeDTOs() {
		try {
			String roleId = "123";
			List<AdmFunctionTypeDTO> admFunctionTypeDTOs = this.functionTypeDAO.listFunctionTypeDTOsByRoleId(roleId);
			Assert.assertEquals(0, admFunctionTypeDTOs.size());
		} catch (Exception e) {
			System.out.println("_TestFunctionTypeDAO.getFunctionTypeDTOs() is error" + e);
			e.printStackTrace();
		}
	}

	/**
	 * Purpose:測試-查詢功能列表
	 * @author CarrieDuan
	 * @return void
	 */
	public void testGetFunctionByParentId() {
		try {
			String functionId = "ADM01000";
			List<Parameter> functionTypes= this.functionTypeDAO.listFunctionByFunctionId(functionId);
			functionId = "";
			functionTypes= this.functionTypeDAO.listFunctionByFunctionId(functionId);
//			Assert.assertEquals(3, functionTypes.size());
		} catch (Exception e) {
			System.out.println("_TestFunctionTypeDAO.testGetFunctionByParentId() is error" + e);
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @return the functionTypeDAO
	 */
	public IFunctionTypeDAO getFunctionTypeDAO() {
		return functionTypeDAO;
	}

	/**
	 * @param functionTypeDAO the functionTypeDAO to set
	 */
	public void setFunctionTypeDAO(IFunctionTypeDAO functionTypeDAO) {
		this.functionTypeDAO = functionTypeDAO;
	}
	
	
}
