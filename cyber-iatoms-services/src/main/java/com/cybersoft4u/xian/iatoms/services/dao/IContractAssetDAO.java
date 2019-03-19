package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractAssetDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimContractAsset;

/**
 * Purpose: 合約設備DAO interface 
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2016-7-1
 * @MaintenancePersonnel CarrieDuan
 */
public interface IContractAssetDAO extends IGenericBaseDAO<BimContractAsset> {
	
	/**
	 * Purpose:刪除合約設備
	 * @author CarrieDuan
	 * @param contractId：合約ID
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return void
	 */
	public void deleteByContractId(String contractId) throws DataAccessException;
	
	/**
	 * Purpose:根據合約ID獲取已經存在合約設備
	 * @author CarrieDuan
	 * @param contractId：合約ID
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<ContractAssetDTO> : 返回設備列表
	 */
	public List<BimContractAssetDTO> listContractAssetByContractId(String contractId) throws DataAccessException;
	
	/**
	 * 
	 * Purpose:获得设备集合集合
	 * @author allenchen
	 * @throws DataAccessException:出错是返回DateAccessException
	 * @return List<Parameter>：设备集合
	 */
	public List<Parameter> listAsset() throws DataAccessException;
	
	/**
	 * Purpose:根據合約ID獲取對應的周邊設備
	 * @author CarrieDuan
	 * @param contractId :合約ID
	 * @param assetCategory：合約類型
	 * @throws DataAccessException:出错是返回DateAccessException
	 * @return List<Parameter>:設備列表
	 */
	public List<Parameter> listAssetCategorysByContractId(String contractId, String assetCategory) throws DataAccessException;
}
