package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.ReportSettingDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimReportSetting;

/**
 * Purpose:報表發送功能設定DAO interface 
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2016年7月4日
 * @MaintenancePersonnel ElvaHe
 */
@SuppressWarnings({"rawtypes"})
public interface IReportSettingDAO extends IGenericBaseDAO<BimReportSetting> {
	
	/**
	 * Purpose:查詢報表發送功能設定信息
	 * @author ElvaHe
	 * @param queryCustomerId 查詢條件 --- 客戶編號
	 * @param queryReportCode 查詢條件 --- 報表名稱
	 * @param pageSize 每頁顯示的條數
	 * @param pageIndex 頁碼
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<ReportSettingDTO>：報表發送功能設定信息集合
	 */
	//public List<ReportSettingDTO> listBy(String queryCustomerId, String queryReportCode, Integer pageSize, Integer pageIndex, String sort, String order) throws DataAccessException;
	
	/**
	 * Purpose:根據客戶和報表名稱查詢報表發送功能設定信息
	 * @author CrissZhang
	 * @param queryCustomerId 查詢條件 --- 客戶編號
	 * @param queryReportCode 查詢條件 --- 報表名稱
	 * @param pageSize 每頁顯示的條數
	 * @param pageIndex 頁碼
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<ReportSettingDTO>：報表發送功能設定信息集合
	 */
	public List<ReportSettingDTO> listByReportCode(String queryCustomerId, String queryReportCode, Integer pageSize, Integer pageIndex, String sort, String order) throws DataAccessException;
	

	/**
	 * Purpose:獲取符合條件的資料筆數
	 * @author ElvaHe
	 * @param queryCustomerId 查詢條件 --- 客戶編號
	 * @param queryReportCode 查詢條件 --- 報表名稱
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return Integer：總筆數
	 */
	//public Integer count(String queryCustomerId, String queryReportCode)throws DataAccessException;
	
	/**
	 * Purpose:根據客戶和報表名稱獲取符合條件的資料筆數
	 * @author CrissZhang
	 * @param queryCustomerId 查詢條件 --- 客戶編號
	 * @param queryReportCode 查詢條件 --- 報表名稱
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return Integer：總筆數
	 */
	public Integer codeCount(String queryCustomerId, String queryReportCode)throws DataAccessException;
	/**
	 * Purpose:獲取在相同客戶編號下報表編號集合
	 * @author ElvaHe
	 * @param companyId:客戶編號
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<Parameter>：報表編號集合
	 */
	public List<Parameter> getPreReportCodeList(String companyId)throws DataAccessException;

	/**
	 * Purpose:根據客戶編號和報表編號獲取報表集合
	 * @author CrissZhang
	 * @param customerId ： 客戶編號
	 * @param reportCode ： 報表編號
	 * @return List<ReportSettingDTO>
	 */
	public List<ReportSettingDTO> getDetailList(String customerId, String reportCode)throws DataAccessException;
	
	/**
	 * Purpose:根據客戶和報表名稱查詢報表發送功能設定信息
	 * @author CrissZhang
	 * @param queryCustomerId 查詢條件 --- 客戶編號
	 * @param queryReportCode 查詢條件 --- 報表名稱
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<ReportSettingDTO>：報表發送功能設定信息集合
	 */
	public List<ReportSettingDTO> listByReportCode(String queryCustomerId, String queryReportCode)throws DataAccessException;
}
