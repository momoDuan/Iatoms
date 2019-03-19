package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmRole;

/**
 * Purpose: 角色DAO interface
 * @author CarrieDuan
 * @since  JDK 1.6
 * @date   2016年3月30日
 * @MaintenancePersonnel CarrieDuan
 */
public interface IAdmRoleDAO extends IGenericBaseDAO<AdmRole> {
	
	/**
	 * Purpose: 獲得角色列表
	 * @author CarrieDuan
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<Parameter>:角色列表
	 */
	public List<Parameter> listby() throws DataAccessException;
	
	/**
	 * Purpose: 獲得角色列表
	 * @author CarrieDuan
	 * @param userId :使用者編號
	 * @throws DataAccessException :出錯時, 丟出DataAccessException
	 * @return List<Parameter>:角色列表
	 */
	public List<Parameter> listByUserId(String userId) throws DataAccessException;

	/**
	 * Purpose:獲得角色代號列表
	 * @author CarrieDuan
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return List<Parameter>:角色代號列表
	 */
	public List<Parameter> listRoleCode() throws DataAccessException;

	/**
	 * Purpose:查詢符合條件的筆數
	 * @author CarrieDuan
	 * @param queryRoleCode:角色代號
	 * @param queryRoleName:角色名稱
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return Integer:筆數
	 */
	public Integer count(String queryRoleCode, String queryRoleName) throws DataAccessException;

	/**
	 * Purpose:分頁查詢數據
	 * @author CarrieDuan
	 * @param queryRoleCode:角色代碼
	 * @param queryRoleName:角色名稱
	 * @param pageSize:每頁大小
	 * @param pageIndex:頁序號
	 * @param sort:排序字段
	 * @param orderby:排序方式
	 * @param isPage:是否分頁
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return List<AdmRoleDTO>
	 */
	public List<AdmRoleDTO> listBy(String queryRoleCode, String queryRoleName,
			Integer pageSize, Integer pageIndex, String sort, String orderby, 
			Boolean isPage) throws DataAccessException;

	/**
	 * Purpose: 查找要刪除的角色是否已經被引用
	 * @author CarrieDuan
	 * @param roleId: 角色ID
	 * @throws DataAccessException: 出錯時, 丟出DataAccessException
	 * @return Boolean ：返回該角色是否被引用的Boolean值
	 */
	public Boolean checkUseRole(String roleId) throws DataAccessException;
	
	/**
	 * Purpose: 檢測角色代碼角色名稱是否重複
	 * @author CarrieDuan
	 * @param roleCode ： 角色代碼
	 * @param roleName ：角色名稱
	 * @param roleId ：角色ID
	 * @throws DataAccessException ：DataAccessException:出錯時拋出DataAccessException
	 * @return Boolean ：是否重複
	 */
	public Boolean checkRepeat(String roleCode, String roleName, String roleId) throws DataAccessException;

	/**
	 * Purpose:根據角色編號查詢角色
	 * @author CarrieDuan
	 * @param roleId:角色編號
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return AdmRoleDTO:角色DTO
	 */
	public AdmRoleDTO getRoleById(String roleId) throws DataAccessException;
	
	/**
	 * Purpose: 根據角色屬性查找對應的表單角色
	 * @author CarrieDuan
	 * @param attribute ：角色清單
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return List<Parameter> ：返回表單角色
	 */
	public List<Parameter> listWorkFlowByAttribute(String attribute) throws DataAccessException;
	/**
	 * Purpose:根據部門獲取工程師，根據TMS,QA,CUSTOMER_SERVICE獲取人員
	 * @author amandawang
	 * @param deptCode ：部門id
	 * @param roleCode : 角色id
	 * @throws DataAccessException : 出錯時拋出DataAccessException
	 * @return List<Parameter> : 人員列表
	 */
	public List<Parameter> getUserByDepartmentAndRole(String deptCode, String roleCode, boolean flag, boolean isDeptAgent) throws DataAccessException;
	
	/**
	 * Purpose:根據角色編號、使用者編號等信息獲取角色group信息
	 * @author CrissZhang
	 * @param id ： 傳入的編號，根據isQueryUser標志位的不同進行區分
	 * @param isQueryUser ： 是否查詢使用者信息
	 * @param ignoreRoleId ： 忽略的角色編號
	 * @param userIdList ： 傳入的使用者編號集合
	 * @throws DataAccessException : 出錯時拋出DataAccessException
	 * @return List<AdmRoleDTO> : 角色group信息
	 */
	public List<AdmRoleDTO> getRoleGroup(String id, boolean isQueryUser, String ignoreRoleId, List<String> userIdList) throws DataAccessException;
}
