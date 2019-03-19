package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BimDepartmentDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimDepartment;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

/**
 * Purpose: 部門維護DAO接口
 * @author Amanda Wang
 * @since  JDK 1.6
 * @date   2016年6月16日
 * @MaintenancePersonnel Amanda Wang
 */
public interface IDepartmentDAO extends IGenericBaseDAO<BimDepartment> {
	
	/**
	 * Purpose:根據條件查詢部門信息
	 * @author barryzhang
	 * @param deptCode：部門編號
	 * @param companyId：公司編號
	 * @param deptName：部門名稱
	 * @param pageSize：每頁筆數
	 * @param pageIndex：頁碼
	 * @param sort：升序降序
	 * @param orderby: 排序字段
	 * @throws DataAccessException: 出錯時, 丟出DataAccessException
	 * @return List<BimDepartmentDTO>: 部門信息List
	 */
	public List<BimDepartmentDTO> listBy(String deptCode, String companyId, String deptName,
			Integer pageSize, Integer pageIndex, String sort, String orderby) throws DataAccessException;
	
	/**
	 * Purpose:獲得條數
	 * @author amandawang
	 * @param queryDeptName：部門名稱
	 * @param queryCompany：公司ID
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return Integer:總筆數
	 */
	public Integer count(String queryDeptName, String queryCompany) throws DataAccessException;
	
	/**
	 * Purpose:檢核同一公司下是否存在重複的部門名稱. 
	 * @author barryzhang
	 * @param companyId: 公司ID	
	 * @param deptCode： 部門Code
	 * @param deptName： 部門名稱
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return boolean：是否存在重複的部門名稱.
	 */
	public boolean check(String companyId, String deptCode, String deptName) throws DataAccessException;
	
	/**
	 * Purpose:根据所选公司联动部门
	 * @author CrissZhang
	 * @param companyId ：公司编号
	 * @param ignoreDeleted ：忽略刪除
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<Parameter>：得到部門列表
	 */
	public List<Parameter> getDeptList(String companyId, Boolean ignoreDeleted) throws DataAccessException;
	
	/**
	 * Purpose:根据所选公司以及是否拼接信息得到部門列表
	 * @author CrissZhang
	 * @param companyId ：公司编号
	 * @param isSplitInfo ：是否拼接信息
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<Parameter>：得到部門列表
	 */
	public List<Parameter> getDeptByCompany(String companyId, Boolean isSplitInfo) throws DataAccessException;

	/**
	 * Purpose:根据所选公司联动部门
	 * @author amandawang
	 * @param companyId ：公司編號
	 * @param deptCode : 部門名稱
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<Parameter>：得到部門列表
	 */
	public List<Parameter> getDepartmentList(String companyId, String deptCode) throws DataAccessException;
	
	/**
	 * Purpose: 依據部門名稱獲取部門ID
	 * @author CarrieDuan
	 * @param departmentName：部門名稱
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return String：部門ID
	 */
	public String getDepartmentIdByName(String departmentName, String companyId) throws DataAccessException;
	/**
	 * Purpose:根据所选公司以及是否拼接信息得到部門列表
	 * @author amandawang
	 * @param companyId ：公司编号
	 * @param isSplitInfo ：是否拼接信息
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<Parameter>：得到部門列表
	 */
	public List<Parameter> getDepts(String companyId) throws DataAccessException;
	
	/**
	 * Purpose:得到所有部門信息集合
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<BimDepartmentDTO>：得到部門列表
	 */
	public List<BimDepartmentDTO> getDeptList() throws DataAccessException;
}
