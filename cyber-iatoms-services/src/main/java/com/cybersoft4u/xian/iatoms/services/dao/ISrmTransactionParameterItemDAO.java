package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmTransactionParameterItemDTO;

/**
 * Purpose: 交易參數項目 DAO Interface
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016年12月14日
 * @MaintenancePersonnel evanliu
 */
public interface ISrmTransactionParameterItemDAO extends IGenericBaseDAO {
	/**
	 * Purpose:查找所有的交易參數項目
	 * @author CrissZhang
	 * @param versionDate: 新建案傳入當前時間，否則傳入案件起案日
	 * @throws DataAccessException：出錯後，丟出DataAccessException
	 * @return List<SrmTransactionParameterItemDTO>：交易參數項目列表list
	 */
	public List<SrmTransactionParameterItemDTO> listby(String versionDate) throws DataAccessException;
}
