package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetBorrowInfoDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetBorrowInfo;
/**
 * Purpose: 設備借用主檔DAO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2018年8月1日
 * @MaintenancePersonnel CarrieDuan
 */
public interface IDmmAssetBorrowInfoDAO extends IGenericBaseDAO<DmmAssetBorrowInfo> {

	/**
	 * Purpose:
	 * @author CarrieDuan
	 * @param borrowUserId
	 * @param caseStatus
	 * @param page
	 * @param rows
	 * @throws DataAccessException
	 * @return List<DmmAssetBorrowInfoDTO>
	 */
	public List<DmmAssetBorrowInfoDTO> getBorrowIds(String borrowCaseId, String borrowStartDate, String borrowEndDate, String borrowCaseCategory,
				String borrowUserId, String caseStatus, int page, int rows) throws DataAccessException; 
	/**
	 * Purpose:
	 * @author CarrieDuan
	 * @param borrowUserId
	 * @param caseStatus
	 * @param borrowCodeIds
	 * @param page
	 * @param rows
	 * @throws DataAccessException
	 * @return List<DmmAssetBorrowInfoDTO>
	 */
	public List<DmmAssetBorrowInfoDTO> listBy(String borrowUserId, String caseStatus, List<String> borrowCodeIds, int page, int rows) throws DataAccessException; 

	/**
	 * Purpose:獲取借用案件編號
	 * @author CarrieDuan
	 * @param roles：當前的登陸者角色
	 * @throws DataAccessException
	 * @return List<Parameter>
	 */
	public List<Parameter> getBorrowCaseIds(String roles) throws DataAccessException;
	
	/**
	 * Purpose:查詢已經結案的借用設備
	 * @author CarrieDuan
	 * @param borrowUserId
	 * @param page
	 * @param rows
	 * @throws DataAccessException
	 * @return List<DmmAssetBorrowInfoDTO>
	 */
	public List<DmmAssetBorrowInfoDTO> getBorrowInfoProcess(String borrowUserId, int page, int rows) throws DataAccessException;
	/**
	 * Purpose:
	 * @author CarrieDuan
	 * @param borrowUserId
	 * @throws DataAccessException
	 * @return List<DmmAssetBorrowInfoDTO>
	 */
	public List<DmmAssetBorrowInfoDTO> getBorrowListId (List<String> borrowCodeIds) throws DataAccessException;
}
