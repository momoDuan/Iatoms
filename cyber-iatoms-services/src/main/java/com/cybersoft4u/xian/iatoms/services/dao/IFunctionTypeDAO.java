package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmFunctionTypeDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmFunctionType;

/**
 * 
 * Purpose:  功能設定DAO
 * @author CarrieDuan
 * @since  JDK 1.6
 * @date   2015/6/25
 * @MaintenancePersonnel CarrieDuan
 */
public interface IFunctionTypeDAO extends  IGenericBaseDAO<AdmFunctionType>{
	
	
	/**
	 * Purpose:查詢用於頁面展示的FunctionTypeDTO列表
	 * @author CarrieDuan
	 * @param roleId:角色編號
	 * @throws DataAccessException:出錯拋出DataAccessException
	 * @return List<FunctionTypeDTO>:FunctionTypeDTO列表
	 */
	public List<AdmFunctionTypeDTO> listFunctionTypeDTOsByRoleId (String roleId) throws DataAccessException;
	
	/**
	 * Purpose:根據父功能編號查詢功能列表
	 * @author CarrieDuan
	 * @param parentFunctionId:父功能編號
	 * @return List<Parameter>:功能列表
	 */
	public List<Parameter> listFunctionByFunctionId(String functionId) throws DataAccessException;

}
