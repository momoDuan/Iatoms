package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCompany;

/**
 * Purpose: 公司基本訊息維護DAO interface
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2016年6月29日
 * @MaintenancePersonnel ElvaHe
 */
public interface ICompanyDAO extends IGenericBaseDAO<BimCompany>{
	
	/**
	 * Purpose:查詢公司基本資料訊息
	 * @author ElvaHe
	 * @param companyType：公司類型
	 * @param shortName：廠商名稱
	 * @param sort：按此列排序
	 * @param order：排序方式
	 * @param pageSize：一頁筆數
	 * @param pageIndex：當前頁碼
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<CompanyDTO>：公司基本資料訊息
	 */
	public List<CompanyDTO> listBy(String companyType, String shortName, String sort, String order, Integer pageSize, Integer pageIndex)throws DataAccessException;

	/**
	 * Purpose:獲取符合條件資料總筆數
	 * @author ElvaHe
	 * @param queryCompanyType：公司類型
	 * @param queryShortName：公司名稱
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return Integer：符合條件資料總筆數
	 */
	public Integer count(String queryCompanyType, String queryShortName) throws DataAccessException;
	
	/**
	 * Purpose:檢核是否存在重複的公司代號或客戶碼
	 * @author ElvaHe
	 * @param companyId：公司編號
	 * @param shortName: 公司簡稱
	 * @param companyCode：公司代號
	 * @param customerCode：客戶碼
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return boolean
	 */
	public boolean isCheck(String companyId, String companyCode, String shortName, String customerCode) throws DataAccessException;
	
	/**
	 * Purpose:根據條件的到公司下拉框列表
	 * @author CrissZhang
	 * @param companyTypeList : 公司類型
	 * @param authenticationType ： 登入驗證方式
	 * @param isHaveSla ： 是否有sla信息
	 * @param dtidType : dtid生成方式
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<Parameter> ：下拉框列表
	 */
	public List<Parameter> getCompanyList(List<String> companyTypeList, String authenticationType, Boolean isHaveSla, String dtidType) throws DataAccessException;
	
	/**
	 * Purpose：根據公司編號以及刪除標誌查詢符合條件的數量
	 * @param companyId：公司編號
	 * @param deleted：刪除標誌
	 * @return Integer 符合條件的數量
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 */
	public Integer countUserByCompanyId(String companyId, String deleted) throws DataAccessException;
	
	/**
	 * Purpose: 根據公司名稱獲取公司ID
	 * @author CarrieDuan
	 * @param name ：公司名稱
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return String：公司ID
	 */
	public String getCompanyIdByName(String name) throws DataAccessException;
	
	/**
	 * Purpose: 根據公司ID獲取公司信息
	 * @author CarrieDuan
	 * @param companyId ：公司ID
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return CompanyDTO ：公司DTO
	 */
	public CompanyDTO getCompanyDTOByCompanyId(String companyId) throws DataAccessException;
	
	/**
	 * Purpose: 根據客戶ID獲取客戶DTID生成方式
	 * @author CarrieDuan
	 * @param companyId ：客戶id
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return String ：DTID生成方式
	 */
	//public String getDtidTypeByCompanyId(String companyId) throws DataAccessException;
	/**
	 * Purpose: 根據公司CODE獲取公司信息
	 * @author amandawang
	 * @param companyCode ：公司ID
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return CompanyDTO ：公司DTO
	 */
	public CompanyDTO getCompanyByCompanyCode(String companyCode) throws DataAccessException;
	
	/**
	 * Purpose:獲取公司信息集合 若isCodeList爲true則獲取Id與code的集合，若爲false則獲取Id與shortName的集合
	 * @author CrissZhang
	 * @param isCodeList ： 是否獲取code與Id的集合
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<Parameter> : 公司信息集合
	 */
	public List<Parameter> getCompanyList(boolean isCodeList) throws DataAccessException;
	
	/**
	 * Purpose:刪除所有轉入數據
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return void : 無返回
	 */
	public void deleteTransferData() throws DataAccessException;
}
