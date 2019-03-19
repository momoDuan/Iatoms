package com.cybersoft4u.xian.iatoms.services.dao;

import java.sql.Timestamp;
import java.util.List;

import com.cybersoft4u.xian.iatoms.common.bean.dto.ApiAuthorizationInfoDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.ApiAuthorizationInfo;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

public interface IApiAuthorizationInfoDAO  extends IGenericBaseDAO<ApiAuthorizationInfo> {
	/**
	 * Purpose: 查詢符合條件資料
	 * @author amandawang
	 * @param ip ：ip
	 * @throws DataAccessException ：出錯時拋出DataAccessException
	 * @return List<ApiAuthorizationInfoDTO>
	 */
	public List<ApiAuthorizationInfoDTO> getAuthorizationInfoList(String ip) throws DataAccessException;
	
	/**
	 * Purpose: 根據令牌獲取時效
	 * @author amandawang
	 * @param token ：令牌
	 * @throws DataAccessException ：出錯時拋出DataAccessException
	 * @return List<Parameter>
	 */
	public List<Parameter> getAuthorizationTimeByToken(String token) throws DataAccessException;
	/**
	 * Purpose: 更新授權表
	 * @param ip
	 * @param logTime
	 * @throws DataAccessException
	 */
	public void updateApiAuthorizationInfo(String ip, Timestamp logTime) throws DataAccessException;
	
}
