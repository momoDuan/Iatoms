package com.cybersoft4u.xian.iatoms.test.dao;
import java.util.List;

import org.springframework.util.CollectionUtils;

import junit.framework.Assert;
import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BimDepartmentDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.DepartmentFormDTO;
import com.cybersoft4u.xian.iatoms.services.dao.impl.DepartmentDAO;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;

/**
 * Purpose:部門DAO的單元測試 
 * @author Amanda Wang
 * @since  JDK 1.6
 * @date   2016/6/27
 * @MaintenancePersonnel Amanda Wang
 */
public class _TestDepartmentDAO extends AbstractTestCase {
	
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestDepartmentDAO.class);
	
	/**
	 * 注入AdmDeptDAO
	 */
	private DepartmentDAO departmentDAO;

	/**
	 * 無參構造
	 */
	@SuppressWarnings("deprecation")
	public _TestDepartmentDAO() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	/**
	 * Purpose:測試查詢方法
	 * @author Amanda Wang
	 * @return void
	 */
	public void testListBy(){
		try {
			String deptCode = "";
			String companyId = "1470649218282-0002";
			String deptName = "部";
			Integer pageSize = 10;
			Integer pageIndex = 0;
			String sort = "ASC";
			String orderby = "DEPT_NAME";
			List<BimDepartmentDTO> list = departmentDAO.listBy(deptCode, companyId, deptName, pageSize, pageIndex, sort, orderby);
			if(!CollectionUtils.isEmpty(list)){
				Assert.assertNotNull(list);
			} else {
				Assert.assertFalse(list == null);
			}
		} catch (DataAccessException e) {
			LOGGER.error("_TestDepartmentDAO.testListBy()", "is error", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試獲得查詢條數的方法
	 * @author Amanda Wang
	 * @return void
	 */
	public void testCount(){
		try {
			DepartmentFormDTO bimDepartmentFormDTO = new DepartmentFormDTO();
			bimDepartmentFormDTO.setQueryCompany("1470649218282-0002");
			bimDepartmentFormDTO.setQueryDeptName("保");
			int count =  departmentDAO.count(bimDepartmentFormDTO.getQueryDeptName(), bimDepartmentFormDTO.getQueryCompany());
//			Assert.assertEquals(1, count);	
		} catch (DataAccessException e) {
			System.out.println("_TestDepartmentDAO.testCount() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:檢核同一公司下是否存在相同名稱的部門名
	 * @author Amanda Wang
	 * @return void
	 */
	public void testCheck(){
		try {
			String companyId = "1470653353175-0005"; 
			String deptCode = "1472004327953-0005";
			String deptName = "信息開發部" ;
			boolean isRepate =  departmentDAO.check(companyId, deptCode, deptName);
			Assert.assertEquals(true, isRepate);	
		} catch (DataAccessException e) {
			System.out.println("_TestDepartmentDAO.testCheck() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:根據公司ID得到部門信息列表
	 * @author barryzhang
	 * @return void
	 */
	@SuppressWarnings("rawtypes")
	public void testGetDeptByCompanyId(){
		try {
			String  companyId = "1470653353175-0005"; 
			List<Parameter> result = departmentDAO.getDeptList(companyId, true);
//			Assert.assertEquals(2, result.size());
		} catch (DataAccessException e) {
			System.out.println("_TestDepartmentDAO.testGetDeptByCompanyId() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the departmentDAO
	 */
	public DepartmentDAO getDepartmentDAO() {
		return departmentDAO;
	}
	/**
	 * @param departmentDAO the departmentDAO to set
	 */
	public void setDepartmentDAO(DepartmentDAO departmentDAO) {
		this.departmentDAO = departmentDAO;
	}
}
