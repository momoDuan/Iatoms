package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.services.dmo.BimContractVendor;

/**
 * 
 * Purpose: 合約厂商DAO interface 
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2016-7-1
 * @MaintenancePersonnel CarrieDuan
 */
public interface IContractVendorDAO extends IGenericBaseDAO<BimContractVendor> {

	/**
	 * Purpose:: 刪除合約中廠商
	 * @author CarrieDuan
	 * @param contractId : 合約ID
	 * @throws DataAccessException ：出錯時拋出DataAccessException
	 * @return void
	 */
	public void deleteByContractId(String contractId) throws DataAccessException;
	
	/**
	 * Purpose:獲取合約下的維護廠商
	 * @author CarrieDuan
	 * @param contractId:合約ID
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<Parameter>：維護廠商集合
	 */
	public List<Parameter> listVendorsByContractId(String contractId) throws DataAccessException;
}
