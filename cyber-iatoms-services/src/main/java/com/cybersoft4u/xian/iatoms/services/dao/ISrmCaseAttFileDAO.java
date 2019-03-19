package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAttFileDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseAttFile;

/**
 * Purpose:案件附件檔 dao interface 
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016年12月16日
 * @MaintenancePersonnel CrissZhang
 */
public interface ISrmCaseAttFileDAO  extends IGenericBaseDAO<SrmCaseAttFile>{

	/**
	 * Purpose:根據案件編號得到案件附件檔資料的列表
	 * @author CrissZhang
	 * @param caseId ： 案件編號
	 * @param isHistory ： 是否查詢歷史表
	 * @throws DataAccessException ： 出錯時拋出DataAccessException
	 * @return List<SrmCaseAttFileDTO> ：案件附件檔資料列表
	 */
	public List<SrmCaseAttFileDTO> listByCaseId(String caseId, String attFileId, String isHistory) throws DataAccessException;
}
