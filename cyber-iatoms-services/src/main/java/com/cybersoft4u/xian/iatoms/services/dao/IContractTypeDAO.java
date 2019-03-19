package com.cybersoft4u.xian.iatoms.services.dao;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.services.dmo.BimContractType;

/**
 * 
 * Purpose: 合約类型DAO interface 
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2016-7-1
 * @MaintenancePersonnel CarrieDuan
 */
public interface IContractTypeDAO extends IGenericBaseDAO<BimContractType> {

	/**
	 * Purpose: 刪除合約的合約類型
	 * @author CarrieDuan
	 * @param contractId ：合約ID
	 * @throws DataAccessException ：出錯時拋出DataAccessException
	 * @return void
	 */
	public void deleteByContractId(String contractId) throws DataAccessException;
}
