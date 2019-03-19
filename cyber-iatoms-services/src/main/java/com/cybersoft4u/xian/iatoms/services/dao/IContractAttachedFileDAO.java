package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractAttachedFileDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimContractAttachedFile;

/**
 * Purpose: 合約文件DAO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/2/4
 * @MaintenancePersonnel CarrieDuan
 */
public interface IContractAttachedFileDAO extends IGenericBaseDAO<BimContractAttachedFile>{

	/**
	 * Purpose: 根據合約ID獲取該合約下的文件
	 * @author CarrieDuan
	 * @param contractId ：合約ID
	 * @throws DataAccessException ：出錯時拋出DataAccessException
	 * @return void
	 */
	public List<BimContractAttachedFileDTO> getContractAttachedFileByContractId(String contractId) throws DataAccessException; 
}
