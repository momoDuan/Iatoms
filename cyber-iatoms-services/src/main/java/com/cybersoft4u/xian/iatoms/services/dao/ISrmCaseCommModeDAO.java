package com.cybersoft4u.xian.iatoms.services.dao;

import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseCommMode;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

/**
 * Purpose: SRM_案件處理中通訊模式維護檔 DAO
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2017/2/24
 * @MaintenancePersonnel CrissZhang
 */
public interface ISrmCaseCommModeDAO extends IGenericBaseDAO<SrmCaseCommMode>{

	/**
	 * Purpose:刪除保護之前設定的設備支援功能
	 * @author CrissZhang
	 * @param caseId ： 案件編號
	 * @return void
	 */
	public void deleteAll(String caseId) throws DataAccessException;
	
}
