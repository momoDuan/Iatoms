package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.workflow.dao.identity.IWfIdentityDAO;

import com.cybersoft4u.xian.iatoms.common.IAtomsAccessControl;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;

/**
 * Purpose:识别管理DAO介面 
 * @author candicechen
 * @since  JDK 1.6
 * @date   2015年4月20日
 * @MaintenancePersonnel candicechen
 */
public interface IIAtomsIdentityDAO extends IWfIdentityDAO {	
	
	/**
	 * Purpose:依據使用者帳號抓取擁有的角色
	 * @author evanliu
	 * @param userId:使用者帳號
	 * @throws DataAccessException:出錯後, 丟出DataAccessException
	 * @return List<AdmRoleDTO>:使用者擁有的角色列表
	 */
	public List<AdmRoleDTO> listRoleByUserId(String userId) throws DataAccessException;
	/**
	 * Purpose:根據使用者帳號查找使用者信息
	 * @author evanliu
	 * @param account：帳號
	 * @throws DataAccessException:出錯後, 丟出DataAccessException
	 * @return AdmUserDTO：使用者信息
	 */
	public AdmUserDTO getAdmUserDTOByAccount(String account) throws DataAccessException;
	/**
	 * Purpose:依据登入者取得使用者存取控制權限 and URL
	 * @param logonUser:登入者
	 * @throws DataAccessException:出错时返回DataAccessException
	 * @return: List<AccessControl> 返回员工使用者存取控制權限讯息
	 */
	public List<IAtomsAccessControl> getAvailableAccessControlList(IAtomsLogonUser logonUser) throws DataAccessException;	
}
