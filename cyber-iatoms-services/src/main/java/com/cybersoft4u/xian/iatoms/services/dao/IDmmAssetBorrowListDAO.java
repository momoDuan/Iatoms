package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetBorrowListDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetBorrowList;
/**
 * Purpose: 設備借用列表DAO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2018年8月1日
 * @MaintenancePersonnel CarrieDuan
 */
public interface IDmmAssetBorrowListDAO extends IGenericBaseDAO<DmmAssetBorrowList> {
	
	/**
	 * 
	 * Purpose:
	 * @author CarrieDuan
	 * @param assetTypeId
	 * @return
	 * @throws DataAccessException
	 * @return List<DmmAssetBorrowListDTO>
	 */
	public Integer listBy(String serialNumber) throws DataAccessException;

}
