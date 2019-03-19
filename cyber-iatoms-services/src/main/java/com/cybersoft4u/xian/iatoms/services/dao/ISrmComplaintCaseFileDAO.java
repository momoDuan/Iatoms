package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmComplaintCaseFileDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmComplaintCaseFile;

/**
 * Purpose:客訴管理附加檔案 DAO interface 
 * @author nicklin
 * @since  JDK 1.7
 * @date   2018/03/26
 * @MaintenancePersonnel cybersoft
 */
public interface ISrmComplaintCaseFileDAO extends IGenericBaseDAO<SrmComplaintCaseFile> {

	/**
	 * Purpose:根據客訴編號得到客訴附件檔資料的列表
	 * @author nicklin
	 * @param  caseId ： 客訴編號
	 * @throws DataAccessException ： 出錯時拋出DataAccessException
	 * @return List<SrmComplaintCaseFileDTO> ：客訴附件檔資料列表
	 */
	public List<SrmComplaintCaseFileDTO> listByCaseId(String fileId, String caseId) throws DataAccessException;
}
