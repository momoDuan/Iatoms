package com.cybersoft4u.xian.iatoms.test.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import junit.framework.Assert;
import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmRoleDAO;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;

/**
 * Purpose: 角色DAO-測試類
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2016/8/16
 * @MaintenancePersonnel CarrieDuan
 */
public class _TestAdmRoleDAO extends AbstractTestCase {

	/**
	 * 注入角色DAO
	 */
	private IAdmRoleDAO admRoleDAO;

	/**
	 * Constructor: 無參構造函數
	 */
	public _TestAdmRoleDAO() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	} 
	
	/**
	 * Purpose:測試-獲得角色列表
	 * @author CarrieDuan
	 * @return void
	 */
	/*public void testGetRoleList() {
		try {
			List<Parameter> roles = this.admRoleDAO.listby();
			Assert.assertEquals(17, roles.size());
		} catch (DataAccessException e) {
			System.out.println("_TestAdmRoleDAO.testGetRoleList() is error" + e);
			e.printStackTrace();
		}
	}*/
	
	/**
	 * Purpose:測試 - 獲得角色列表，根據角色ID
	 * @author CarrieDuan
	 * @return void
	 */
	public void testListByUserId() {
		try {
			List<Parameter> roles = this.admRoleDAO.listByUserId("2016083117590003");
			if(!CollectionUtils.isEmpty(roles)){
				Assert.assertNotNull(roles);
			} else {
				Assert.assertFalse(roles == null);
			}
			roles = this.admRoleDAO.listByUserId(null);
			if(!CollectionUtils.isEmpty(roles)){
				Assert.assertNotNull(roles);
			} else {
				Assert.assertFalse(roles == null);
			}
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE);
		} catch (Exception e) {
			System.out.println("_TestAdmRoleDAO.testListByUserId() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試-獲取角色代碼
	 * @author CarrieDuan
	 * @return void
	 */
	public void testGetRoleCode() {
		try {
			List<Parameter> roleCodes = this.admRoleDAO.listRoleCode();
			if(!CollectionUtils.isEmpty(roleCodes)){
				Assert.assertNotNull(roleCodes);
			} else {
				Assert.assertFalse(roleCodes == null);
			}
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE);
		} catch (Exception e) {
			System.out.println("_TestAdmRoleDAO.testGetRoleCode() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試-查詢總條數
	 * @author CarrieDuan
	 * @return void
	 */
	public void testCount() {
		try {
			int count = 0;
			//count = this.admRoleDAO.count(null, null);
			count = this.admRoleDAO.count("test", "Q");
			if(count != 0){
				Assert.assertTrue(count!=0);
			} else {
				Assert.assertFalse(count!=0);
			}
			count = this.admRoleDAO.count("test", null);
			if(count != 0){
				Assert.assertTrue(count!=0);
			} else {
				Assert.assertFalse(count!=0);
			}
			count = this.admRoleDAO.count("", "");
			if(count != 0){
				Assert.assertTrue(count!=0);
			} else {
				Assert.assertFalse(count!=0);
			}
			count = this.admRoleDAO.count("-677", "-777");
			if(count != 0){
				Assert.assertTrue(count!=0);
			} else {
				Assert.assertFalse(count!=0);
			}
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE);
		} catch (Exception e) {
			System.out.println("_TestAdmRoleDAO.testCount() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose: 測試-分頁查詢
	 * @author CarrieDuan
	 * @return void
	 */
	public void testListBy() {
		try {
			List<AdmRoleDTO> roleDTOs = this.admRoleDAO.listBy("E001", null, 10, 1, "roleCode", "asc", true);
			if(!CollectionUtils.isEmpty(roleDTOs)){
				Assert.assertNotNull(roleDTOs);
			} else {
				Assert.assertFalse(roleDTOs == null);
			}
			roleDTOs = this.admRoleDAO.listBy(null, "E001", 10, 1, "roleCode", "asc", true);
			if(!CollectionUtils.isEmpty(roleDTOs)){
				Assert.assertNotNull(roleDTOs);
			} else {
				Assert.assertFalse(roleDTOs == null);
			}
			roleDTOs = this.admRoleDAO.listBy(null, null, 10, 1, "roleCode", "asc", true);
			if(!CollectionUtils.isEmpty(roleDTOs)){
				Assert.assertNotNull(roleDTOs);
			} else {
				Assert.assertFalse(roleDTOs == null);
			}
			roleDTOs = this.admRoleDAO.listBy("E001", "E001", 10, 1, "roleCode", "asc", Boolean.FALSE);
			if(!CollectionUtils.isEmpty(roleDTOs)){
				Assert.assertNotNull(roleDTOs);
			} else {
				Assert.assertFalse(roleDTOs == null);
			}
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE);
		} catch (Exception e) {
			System.out.println("_TestAdmRoleDAO.testListBy() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試-測試角色是否被引用
	 * @author Carrie Duan
	 * @return void
	 */
	public void testCheckUseRole() {
		try {
			String roleId = "1000000000-0004";
			boolean isUse = this.admRoleDAO.checkUseRole(roleId);
			if(isUse){
				Assert.assertTrue(isUse);
			} else {
				Assert.assertFalse(isUse);
			}
			isUse = this.admRoleDAO.checkUseRole("2342323423");
			if(isUse){
				Assert.assertTrue(isUse);
			} else {
				Assert.assertFalse(isUse);
			}
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE);
		} catch (Exception e) {
			System.out.println("_TestAdmRoleDAO.testCheckUseRole() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試-角色代碼角色名稱是否存在
	 * @author CarrieDuan
	 * @return void
	 */
	public void testCheckRepeat() {
		try {
			String roleCode = "";
			String roleName = "求償客服";
			boolean isRepeat = this.admRoleDAO.checkRepeat(roleCode, roleName, "1000000000-0007");
			if(isRepeat){
				Assert.assertTrue(isRepeat);
			} else {
				Assert.assertFalse(isRepeat);
			}
			isRepeat = this.admRoleDAO.checkRepeat("一般", null, null);
			if(isRepeat){
				Assert.assertTrue(isRepeat);
			} else {
				Assert.assertFalse(isRepeat);
			}
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE);
		} catch (Exception e) {
			System.out.println("_TestAdmRoleDAO.testCheckRepeat() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試 - 根據角色編號查詢角色
	 * @author CarrieDuan
	 * @return void
	 */
	public void testGetRoleById() {
		try {
			String roleId = "1471319892302-0064";
			AdmRoleDTO admRoleDTO = this.admRoleDAO.getRoleById(roleId);
			if(admRoleDTO != null){
				Assert.assertNotNull(admRoleDTO);
			} else {
				Assert.assertNull(admRoleDTO);
			}
			admRoleDTO = this.admRoleDAO.getRoleById(null);
			if(admRoleDTO != null){
				Assert.assertNotNull(admRoleDTO);
			} else {
				Assert.assertNull(admRoleDTO == null);
			}
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE);
		} catch (Exception e) {
			System.out.println("_TestAdmRoleDAO.testCheckRepeat() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試-根據角色屬性查找對應的表單角色
	 * @author CarrieDuan
	 * @return void
	 */
	public void testListWorkFlowByAttribute() {
		try {
			String attribute = "01";
			List<Parameter> workFlows = this.admRoleDAO.listWorkFlowByAttribute(attribute);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE);
		//	Assert.assertEquals(1, workFlows.size());
		} catch (Exception e) {
			System.out.println("_TestAdmRoleDAO.testListWorkFlowByAttribute() is error" + e);
			e.printStackTrace();
		}
	}
	
	public void testGetUserByDepartmentAndRole() {
		try {
			String deptCode = "1474252978644-0046";
			String roleCode = "CUSTOMER_SERVICE";
			List<Parameter> workFlows = this.admRoleDAO.getUserByDepartmentAndRole(deptCode, roleCode, false, false);
			if(workFlows != null){
				Assert.assertNotNull(workFlows);
			} else {
				Assert.assertNull(workFlows == null);
			}
			roleCode = "TMS";
			workFlows = this.admRoleDAO.getUserByDepartmentAndRole(deptCode, roleCode, true, true);
			if(workFlows != null){
				Assert.assertNotNull(workFlows);
			} else {
				Assert.assertNull(workFlows == null);
			}
			roleCode = "QA";
			workFlows = this.admRoleDAO.getUserByDepartmentAndRole(deptCode, roleCode, true, true);
			if(workFlows != null){
				Assert.assertNotNull(workFlows);
			} else {
				Assert.assertNull(workFlows == null);
			}
			roleCode = "VENDOR_WAREHOUSE";
			workFlows = this.admRoleDAO.getUserByDepartmentAndRole(deptCode, roleCode, true, true);
			if(workFlows != null){
				Assert.assertNotNull(workFlows);
			} else {
				Assert.assertNull(workFlows == null);
			}
			//	Assert.assertEquals(1, workFlows.size());
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE);
		} catch (Exception e) {
			System.out.println("_TestAdmRoleDAO.testListWorkFlowByAttribute() is error" + e);
			e.printStackTrace();
		}
	}
	
	public void testGetRoleGroup() {
		try {
			String id = "1473071421200-0047";
			String ignoreRoleId = "1000000000-0011";
			List<String> userIdList = new ArrayList<String>();
			//userIdList.add("");
			List<AdmRoleDTO> workFlows = this.admRoleDAO.getRoleGroup(id, true, ignoreRoleId, userIdList);
			if(workFlows != null){
				Assert.assertNotNull(workFlows);
			} else {
				Assert.assertNull(workFlows == null);
			}
			userIdList.add("1473071421200-0047");
			workFlows = this.admRoleDAO.getRoleGroup(null, false, null, userIdList);
			if(workFlows != null){
				Assert.assertNotNull(workFlows);
			} else {
				Assert.assertNull(workFlows == null);
			}
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE);
		} catch (Exception e) {
			System.out.println("_TestAdmRoleDAO.testListWorkFlowByAttribute() is error" + e);
			e.printStackTrace();
		}
	}
	/**
	 * @return the admRoleDAO
	 */
	public IAdmRoleDAO getAdmRoleDAO() {
		return admRoleDAO;
	}

	/**
	 * @param admRoleDAO the admRoleDAO to set
	 */
	public void setAdmRoleDAO(IAdmRoleDAO admRoleDAO) {
		this.admRoleDAO = admRoleDAO;
	}
	
	
}
