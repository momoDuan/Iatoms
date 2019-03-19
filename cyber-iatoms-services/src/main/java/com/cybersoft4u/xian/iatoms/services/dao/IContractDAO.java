package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.Date;
import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimContract;

/**
 * Purpose:使用者DAO interface 
 * @author CarrieDuan
 * @since  JDK 1.6
 * @date   2016年3月30日
 * @MaintenancePersonnel CarrieDuan
 */
public interface IContractDAO extends IGenericBaseDAO<BimContract> {
	
	
	/**
	 * Purpose:查询合约维护集合
	 * @author CarrieDuan
	 * @param customerId : 客戶ID
	 * @param companyId ：維護上ID
	 * @param order ：排列字段
	 * @param sort ：排列方式
	 * @param page ：頁碼
	 * @param rows ：每頁顯示行數
	 * @throws DataAccessException：出错时抛出：DataAccessExecption
	 * @return List<ContractManageDTO> : 返回合約信息列表
	 */
	public List<BimContractDTO> listBy(String customerId, String companyId, String contractId, String order, String sort, int page, int rows) throws DataAccessException;
	
	/**
	 * Purpose:獲取總條數
	 * @author CarrieDuan
	 * @param customerId: 客戶ID
	 * @param companyId ：維護上ID
	 * @throws DataAccessException：出错时抛出：DataAccessExecption
	 * @return Integer 返回總條數
	 */
	public Integer count(String customerId, String companyId) throws DataAccessException;

	/**
	 * Purpose:檢查合約編號是否重復
	 * @author allenchen
	 * @param contractCode:合約編號
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return boolean:是否重復
	 */
	public Boolean isCheck(String contractCode, String contractId) throws DataAccessException;

	/**
	 * Purpose:獲取公司Id
	 * @author amandawang
	 * @param contractId：合約Id
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<Parameter>
	 */
	public String getCustomerIdBycontractId(String contractId) throws DataAccessException;
	
	/**
	 * 
	 * Purpose:获得客户集合
	 * @author allenchen
	 * @return
	 * @throws DataAccessException:出错是返回DateAccessException
	 * @return List<Parameter>：客户集合
	 */
	/*public List<Parameter> getCustomerList() throws DataAccessException;*/
	/**
	 * Purpose:根據客戶和狀態得到合約列表（支持不限狀態與不限客戶）
	 * @author CrissZhang
	 * @param customerId : 客戶編號
	 * @param contractStatus ： 合約狀態
	 * @param companyType : 客戶編號
	 * @throws DataAccessException :出錯時, 丟出DataAccessException
	 * @return List<Parameter> ：下拉框列表
	 */
	public List<Parameter> getContractByCustomerAndStatus(String customerId, String contractStatus, String companyType) throws DataAccessException;
	
	/**
	 * Purpose:根據客戶、狀態公司類型得到合約列表
	 * @author CrissZhang
	 * @param customerId : 客戶編號
	 * @param contractStatus ： 合約狀態
	 * @param isHaveSla : 是否有sla信息
	 * @param order :排序列
	 * @param sort : 排序方式
	 * @param ignoreDeleted ：忽略刪除
	 * @throws DataAccessException :出錯時, 丟出DataAccessException
	 * @return List<Parameter> ：下拉框列表
	 */
	public List<Parameter> getContractCodeList(String customerId, String contractStatus, Boolean isHaveSla, String order, String sort, Boolean ignoreDeleted) throws DataAccessException;
	
	/**
	 * Purpose:查詢EDC合約到期提示報表信息
	 * @author ElvaHe
	 * @param contractStatus:合約狀態
	 * @param startDate:當前系統時間
	 * @param endDate:當前系統日後X個月時間
	 * @return:EDC合約到期提示報表信息集合
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 */
	public List<BimContractDTO> listEdcContractExpireInformReport(String contractStatus, Date startDate, Date endDate) throws DataAccessException;

	/**
	 * Purpose:根據合約編號獲取對應的合約信息
	 * @author CarrieDuan
	 * @param contractCode：合約編號
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return BimContractDTO：合約信息
	 */
	public BimContractDTO getContractDTOByContractCode(String contractCode) throws DataAccessException;
	
	/**
	 * Purpose:根據維護廠商名稱以及合約ID獲取維護廠商ID
	 * @author CarrieDuan
	 * @param customerName：維護廠商名稱
	 * @param contractId：合約ID
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return BimContractDTO：合約信息
	 */
	public BimContractDTO getVendorBy(String customerName, String contractId) throws DataAccessException;
	
	/**
	 * Purpose: 在批次異動時，異動合約編號或cyber日期時，獲取最新合約信息
	 * @author CarrieDuan
	 * @param contractIds：合約ID集合
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<BimContractDTO>:合約DTO
	 */
	public List<BimContractDTO> listContractDTOByIds(List<String> contractIds) throws DataAccessException;
	
	/**
	 * Purpose:判斷當前DB是否存在數據 不存在返回true存在false
	 * @author CrissZhang
	 * @throws DataAccessException:出錯拋出DataAccessException
	 * @return boolean:是否存在數據的boolean值
	 */
	public boolean isNoData() throws DataAccessException;
	/**
	 * Purpose:通過合約code得到合約信息 若傳入contractCode則根據傳入值查詢 若contractCode爲空則查所有
	 * @author amandawang
	 * @param contractCode：合約code
	 * @throws DataAccessException:出錯拋出DataAccessException
	 * @return List<Parameter> 合約集合
	 */
	public List<Parameter> getContractByCode(String contractCode) throws DataAccessException;
	
	/**
	 * Purpose: 得到所有合約信息集合
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<BimContractDTO>:合約DTO
	 */
	public List<BimContractDTO> getContractList() throws DataAccessException;
	
	/**
	 * Purpose:刪除所有轉入數據
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return void : 無返回
	 */
	public void deleteTransferData() throws DataAccessException;
	
	/**
	 * Purpose:案件根據客戶獲取第一筆設備 有edc先edc，有周邊就是周邊，無按照合約編號取第一值
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return void : 無返回
	 */
	public List<BimContractDTO> getEdcAssetContract(String customerId) throws DataAccessException;
	/**
	 * Purpose:根據客戶和狀態得到合約列表
	 * @author amandawang
	 * @param customerId : 客戶編號
	 * @param contractStatus ： 合約狀態
	 * @throws DataAccessException :出錯時, 丟出DataAccessException
	 * @return List<Parameter> ：下拉框列表
	 */
	public List<Parameter> getContractByCustomer(String customerId, String contractStatus) throws DataAccessException;
}
