package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyTypeDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCompanyType;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;
/**
 * Purpose: 公司類型DAO
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2016年8月30日
 * @MaintenancePersonnel ElvaHe
 */
public interface ICompanyTypeDAO extends IGenericBaseDAO<BimCompanyType>{
	
	/**
	 * Purpose:根據公司編號查找公司類型
	 * @author ElvaHe
	 * @param companyId：公司編號
	 * @throws DataAccessException：出錯時拋出DataAccessException異常
	 * @return List<CompanyTypeDTO>：公司類型List
	 */
	public List<CompanyTypeDTO> listByCompanyId(String companyId) throws DataAccessException;
	
	/**
	 * Purpose:根據公司編號刪除公司類型
	 * @author ElvaHe
	 * @param companyId：公司編號
	 * @throws DataAccessException：出錯時拋出DataAccessException異常
	 */
	public void deleteByCompanyId(String companyId) throws DataAccessException;
	
	/**
	 * Purpose: 根據公司ID，核檢公司是否符合指定類型
	 * @author CarrieDuan
	 * @param companyId：公司ID
	 * @param companyType:公司類型
	 * @throws DataAccessException：出錯時拋出DataAccessException異常
	 * @return boolean
	 */
	public boolean checkCompanyType(String companyId, String companyType) throws DataAccessException;
}
