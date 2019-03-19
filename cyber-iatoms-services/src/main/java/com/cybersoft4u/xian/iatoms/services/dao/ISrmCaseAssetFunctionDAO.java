package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAssetFunctionDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseAssetFunction;

/**
 * Purpose: SRM_案件處理中設備支援功能檔 DAO
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2017/2/24
 * @MaintenancePersonnel CrissZhang
 */
public interface ISrmCaseAssetFunctionDAO extends IGenericBaseDAO<SrmCaseAssetFunction>{

	/**
	 * Purpose:根據案件標號查設備支援功能檔 
	 * @author CrissZhang
	 * @throws DataAccessException
	 * @return List<SrmCaseAssetFunctionDTO>
	 */
	public List<SrmCaseAssetFunctionDTO> listByCaseId(String caseId) throws DataAccessException;

	/**
	 * Purpose:閃存保護之前設定的設備支援功能
	 * @author CrissZhang
	 * @param caseId ： 案件編號
	 * @return void
	 */
	public void deleteAll(String caseId) throws DataAccessException;
	
}
