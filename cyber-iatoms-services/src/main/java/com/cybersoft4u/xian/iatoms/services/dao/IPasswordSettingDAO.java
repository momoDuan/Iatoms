package com.cybersoft4u.xian.iatoms.services.dao;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import java.util.Date;

import com.cybersoft4u.xian.iatoms.common.bean.dto.PasswordSettingDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmSecurityDef;

/**
 * Purpose: 密碼原則設定DAO interface
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2016/5/20
 * @MaintenancePersonnel jasonzhou
 */
public interface IPasswordSettingDAO extends IGenericBaseDAO<AdmSecurityDef>{
	
	/**
	 * Purpose:查詢密碼原則信息
	 * @author HermanWang
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return PasswordSettingDTO：密碼原則信息
	 */
	public PasswordSettingDTO getPasswordSettingInfo()throws DataAccessException;
	
	/**
	 * Purpose:密碼歷史清除
	 * @author CrissZhang
	 * @param deleteDate 刪除日期
	 * @throws DataAccessException 错误时抛出的dao異常類
	 * @return void
	 */
	public void deletePwdHistory(Date deleteDate) throws DataAccessException;

}
