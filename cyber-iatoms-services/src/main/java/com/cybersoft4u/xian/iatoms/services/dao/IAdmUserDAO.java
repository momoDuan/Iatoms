package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

/**
 * Purpose:公司DAO interface 
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016年3月30日
 * @MaintenancePersonnel CrissZhang
 */
public interface IAdmUserDAO extends IGenericBaseDAO {
	
	/**
	 * Purpose: 分頁查詢數據
	 * @author CrissZhang
	 * @param account:使用者帳號
	 * @param cName:中文名
	 * @param companyId:公司編號
	 * @param roleId:角色編號
	 * @param accountStatus:帳戶狀態
	 * @param pageSize:每頁大小
	 * @param pageIndex:頁序號
	 * @param sort:排序
	 * @param orderby:查詢
	 * @param isPage:是否分頁
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<AdmUserDTO>:使用者列表
	 */
	public List<AdmUserDTO> listby(String account, String cName, String companyId, List<String> roleId, String accountStatus, Integer pageSize, Integer pageIndex, String sort, String orderby, Boolean isPage) throws DataAccessException;
	/**
	 * Purpose: 分頁查詢數據
	 * @author amandawang
	 * @param companyId:公司編號
	 * @param deptCode:角色編號
	 * @param account:使用者帳號
	 * @param cName:中文名
	 * @param pageSize:每頁大小
	 * @param pageIndex:頁序號
	 * @param sort:排序
	 * @param orderby:查詢
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<AdmUserDTO>:使用者列表
	 */
	public List<AdmUserDTO> getUserList(String companyId, String deptCode, String account, String cName, Integer pageSize, Integer pageIndex, String sort, String orderby) throws DataAccessException;
	/**
	 * Purpose:查詢符合條件的筆數
	 * @author CrissZhang
	 * @param account:使用者帳號
	 * @param cName:中文名
	 * @param companyId:公司編號
	 * @param roleId:角色編號
	 * @param accountStatus:帳戶狀態
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return Integer:筆數
	 */
	public int count(String account, String cName, String companyId, List<String> roleId, String accountStatus) throws DataAccessException;
	/**
	 * Purpose:check account repeat
	 * @author CrissZhang
	 * @param account:使用者編號
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return boolean:repeat?
	 */
	public boolean isRepeat(String account) throws DataAccessException;
	/**
	 * Purpose: 修改使用者帳號信息
	 * @author CrissZhang
	 * @param admUserDTO:帳號
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return void
	 */
	public void updateUser(AdmUserDTO admUserDTO) throws DataAccessException;
	
	/**
	 * Purpose:根據公司Id和部門Code查詢使用者信息
	 * @author barryzhang
	 * @param companyId:公司ID
	 * @param deptCode:部門code
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<AdmUserDTO>：使用者列表
	 */
	public List<AdmUserDTO> listBy(String companyId, String deptCode) throws DataAccessException;
	
	/**
	 * Purpose:根據部門Code查詢使用者信息
	 * @author amandawang
	 * @param deptCode:部門code
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<Parameter>：使用者列表
	 */
	public List<Parameter> getUserByDept(String deptCode) throws DataAccessException;
	/**
	 * Purpose:根據帳號狀態查詢正常的員工emailL
	 * @author HermanWang
	 * @param status:帳號狀態
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<Parameter>：使用者列表
	 */
	public List<Parameter> getNormalUserEmailList(String status) throws DataAccessException;
	
	/**
	 * Purpose:獲取具有指定角色的用戶信息
	 * @author CarrieDuan
	 * @param roles ：角色
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<Parameter>：使用者列表
	 */
	public List<AdmUserDTO> getUserDTOsBy(List<String> roles) throws DataAccessException;
	
	/**
	 * Purpose: 根據角色編號獲取使用者信息
	 * @author CrissZhang
	 * @param roleId : 角色編號
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<AdmUserDTO>：使用者列表
	 */
	public List<AdmUserDTO> getUserByRoleId(String roleId) throws DataAccessException;
	/**
	 * Purpose:獲取廠商的廠商agent
	 * @author HermanWang
	 * @param companyId：公司id
	 * @param cyberFlag：公司是否為cyber
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<Parameter>
	 */
	public List<Parameter> getDeptAgentByCompanyId(String companyId, Boolean cyberFlag) throws DataAccessException;
	/**
	 * Purpose:查詢人員數量
	 * @author amandawang
	 * @param companyId：公司id
	 * @param deptCode：部門id
	 * @param account：帳號
	 * @param cName：中文姓名
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return int：數量
	 */
	public int countUser(String companyId, String deptCode, String account, String cName) throws DataAccessException;
	/**
	 * Purpose:獲取最後一條轉入的帳號資料
	 * @author amandawang
	 * @throws DataAccessException
	 * @return Parameter id與名稱
	 */
	public Parameter getLastUser() throws DataAccessException;
	/**
	 * Purpose:獲取全部帳號資料
	 * @author amandawang
	 * @throws DataAccessException
	 * @return List<Parameter> id與中文名稱集合
	 */
	public List<Parameter> getUserAll() throws DataAccessException;
	
	/**
	 * Purpose:刪除所有轉入數據
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return void : 無返回
	 */
	public void deleteTransferData() throws DataAccessException;
	/**
	 * Purpose:根據公司id查詢公司所有的工程師
	 * @author Hermanwang
	 * @param companyId:公司id
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<Parameter>
	 */
	public List<Parameter> getUserListByCompany(String companyId) throws DataAccessException;
	
	/**
	 * Purpose:核检账号是否在特定公司下
	 * @author CarrieDuan
	 * @param companyId：公司id
	 * @param account：人员账号
	 * @param cName: 人員中文名稱
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return AdmUserDTO
	 */
	public AdmUserDTO getUserDTOByCompanyIdAndAccount(String companyId, String account, String cName) throws DataAccessException;
	
	/**
	 * Purpose:根據userId獲取對應的email
	 * @author CarrieDuan
	 * @param userIds：需要獲取email的人員Id
	 * @throws DataAccessException: 出錯時, 丟出DataAccessException
	 * @return List<AdmUserDTO>
	 */
	public List<AdmUserDTO> getUserEmailById(List<String> userIds) throws DataAccessException;
}
