package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.ApiCmsRepositoryDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.ApiCmsRepository;

public interface IApiCmsRepositoryDAO extends IGenericBaseDAO<ApiCmsRepository> {
	/**
	 * 查詢所有未傳送的設備
	 * @return List<ApiCmsRepositoryDTO>
	 * @throws DataAccessException
	 */
	public List<ApiCmsRepositoryDTO> listBy() throws DataAccessException;
}
