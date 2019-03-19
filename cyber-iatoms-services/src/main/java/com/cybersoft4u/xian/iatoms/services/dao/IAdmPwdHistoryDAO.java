package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmPwdHistoryDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmPwdHistory;
/**
 * Purpose:密碼歷史記錄DAO interface 
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2016年7月6日
 * @MaintenancePersonnel evanliu
 */
public interface IAdmPwdHistoryDAO extends IGenericBaseDAO<AdmPwdHistory> {
	/**
	 * Purpose: 查找最近的幾次使用者密碼歷史記錄
	 * @author HermanWang
	 * @param userId:使用者帳號
	 * @param times:查找最近的幾次
	 * @throws DataAccessException:出錯後, 丟出DataAccessException
	 * @return List<AdmPwdHistoryDTO>:使用者密碼歷史記錄
	 */
	public List<AdmPwdHistoryDTO> listByUserId(String userId, Integer times) throws DataAccessException;
}
