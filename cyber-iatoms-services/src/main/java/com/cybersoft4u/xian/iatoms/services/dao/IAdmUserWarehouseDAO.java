package com.cybersoft4u.xian.iatoms.services.dao;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.services.dmo.AdmUserWarehouse;

/**
 * Purpose: 用户控管资料DAO
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016/8/19
 * @MaintenancePersonnel CrissZhang
 */
public interface IAdmUserWarehouseDAO extends IGenericBaseDAO<AdmUserWarehouse> {
	
	/**
	 * Purpose:刪除用戶所有的仓库信息
	 * @author CrissZhang
	 * @param userId:用戶編號
	 * @throws DataAccessException:出錯後, 丟出DataAccessException
	 * @return void
	 */
	public void deleteAll(String userId) throws DataAccessException;
	
	/**
	 * Purpose: 核檢該倉庫是否屬於當前人員控管
	 * @author CarrieDuan
	 * @param serialNumber ：設備序號
	 * @param userId ：用戶ID
	 * @throws DataAccessException:出錯後, 丟出DataAccessException
	 * @return boolean ：是否具有該控管權限
	 */
	public boolean isUserWarehouse(String serialNumber, String userId) throws DataAccessException;

}
