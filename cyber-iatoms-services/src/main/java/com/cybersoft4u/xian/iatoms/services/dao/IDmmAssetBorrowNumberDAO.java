package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetBorrowNumberDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetBorrowNumber;
/**
 * Purpose: 設備借用數量DAO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2018年8月1日
 * @MaintenancePersonnel CarrieDuan
 */
public interface IDmmAssetBorrowNumberDAO extends IGenericBaseDAO<DmmAssetBorrowNumber> {

	/**
	 * Purpose:
	 * @author CarrieDuan
	 * @param borrowCaseId:設備借用編號
	 * @throws DataAccessException
	 * @return List<DmmAssetBorrowNumberDTO>
	 */
	public List<DmmAssetBorrowNumberDTO> listBy(String borrowCaseId) throws DataAccessException; 
}
