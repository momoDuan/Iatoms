package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.services.dmo.BaseParameterPostCode;
/**
 * Purpose: 郵遞區號DAO
 * @author neiljing
 * @since  JDK 1.7
 * @date   2018年5月15日
 * @MaintenancePersonnel neiljing
 */
public interface IBaseParameterPostCodeDAO extends IGenericBaseDAO<BaseParameterPostCode>{

	/**
	 * Purpose: 查詢郵遞區號集合
	 * @author amandawang
	 * @param cityId 縣市id
	 * @param postName 郵遞區域
	 * @param postCode 郵遞區號
	 * @return List<Parameter> 結果集
	 * @throws DataAccessException
	 */
	public List<Parameter> getPostCodeList(String cityId, String postName, String postCode) throws DataAccessException;

}
