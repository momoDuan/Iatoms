package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.PermissionDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmPermission;
	/**
	 * 
	 * Purpose: 功能权限DAO
	 * @author:CarrieDuan
	 * @since  JDK 1.6
	 * @date   2015/7/9
	 * @MaintenancePersonnel CarrieDuan
	 */
public interface IPermissionDAO extends  IGenericBaseDAO<AdmPermission>{
	/**
	 * 
	 * Purpose:抓取功能清单对应的功能权限
	 * @author felixli
     * @param functionId:功能Id
	 * @throws ServiceException:出错时抛出ServiceException
	 * @return List:返回PermissionId集合
	 */
	public List<PermissionDTO> listByFunctionIds(String functionId) throws DataAccessException;
}
