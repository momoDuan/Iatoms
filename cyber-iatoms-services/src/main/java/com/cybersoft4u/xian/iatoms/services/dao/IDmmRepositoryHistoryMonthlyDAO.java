package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.Date;
import java.util.List;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO;
import com.cybersoft4u.xian.iatoms.common.reportsetting.AssetMaintainFeeSetting;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;
/**
 * Purpose: 設備庫存歷史月檔的dao接口
 * @author Hermanwang
 * @since  JDK 1.6
 * @date   2017/8/23
 * @MaintenancePersonnel Hermanwang
 */
public interface IDmmRepositoryHistoryMonthlyDAO extends IGenericBaseDAO {
	/**
	 * Purpose:根據設備序號查詢到庫存信息
	 * @author barryZhang
	 * @param serialNumber:設備序號
	 * @param fromWarehouseId:轉出倉
	 * @param state:庫存狀態
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return DmmRepositoryDTO:庫存信息DTO
	 */
	public DmmRepositoryDTO getRepositoryBySerialNumber(String serialNumber, String monthYear, String state) throws DataAccessException;
	/**
	 * Purpose:維護費報表(大眾格式)(1).設備明細（by 設備）
	 * @author HermanWang
	 * @param yearMonthDay：上個月的年月，格式：YYYYMM
	 * @param customerCode：TCB-EDC
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<DmmRepositoryHistoryDTO>：List<DmmRepositoryHistoryDTO> 結果集
	 */
	public List<DmmRepositoryDTO> assetInfoList(String yearMonthDay, String customerCode, Date startDate, Date endDate) throws DataAccessException;
	/**
	 * Purpose::維護費報表(大眾格式)(1).(維護費)設備
	 * @author HermanWang
	 * @param yearMonthDay：上個月的年月，格式：YYYYMM
	 * @param customerCode：TCB-EDC
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<DmmRepositoryDTO>
	 */
	public List<DmmRepositoryDTO> feeAssetList(String yearMonthDay, String customerCode) throws DataAccessException;

	/**
	 * Purpose:维护费报表(捷达威格式)--查询维护费设备列表
	 * @author CarrieDuan
	 * @param customerId: 客戶Id
	 * @param status：設備狀態
	 * @param monthYear：上个月的年月，格式：YYYYMM
	 * @param queryDate：这个月的1日凌晨0分0秒
	 * @throws DataAccessException：出錯時, 丟出DataAccessException
	 * @return List<DmmRepositoryDTO>：查詢結果集合
	 */
	public List<DmmRepositoryDTO> listFeeAssetList(String customerId, List<String> status, String monthYear, Date queryDate, AssetMaintainFeeSetting assetMaintainFeeSetting) throws DataAccessException;
	/**
	 * Purpose:綠界 (1).	設備明細
	 * @author HermanWang
	 * @param yearMonthDay：上個月的年月，格式：YYYYMM
	 * @param customerCode：綠界ID
	 * @param startDate：開始日期 
	 * @param endDate：結束日期
	 * @throws DataAccessException：出錯時, 丟出DataAccessException
	 * @return List<DmmRepositoryDTO>：查詢結果集合
	 */
	public List<DmmRepositoryDTO> assetInfoListGreenWorld(String yearMonthDay, String customerCode, Date startDate, Date endDate) throws DataAccessException;
	/**
	 * Purpose:綠界 查詢(維護費)聯天設備
	 * @author HermanWang
	 * @param yearMonthDay：上個月的年月，格式：YYYYMM
	 * @param customerCode：客戶 綠界
	 * @throws DataAccessException：出錯時, 丟出DataAccessException
	 * @return List<DmmRepositoryDTO>
	 */
	public List<DmmRepositoryDTO> feeChatAssetList(String yearMonthDay, String customerCode) throws DataAccessException;
	/**
	 * Purpose:(6).	(維護費)台新設備
	 * @author HermanWang
	 * @param yearMonthDay：上個月的年月，格式：YYYYMM
	 * @param customerCode:綠界的公司主鍵
	 * @param TaiXincustomerCode:台新的公司主鍵
	 * @throws DataAccessException：出錯時, 丟出DataAccessException
	 * @return List<DmmRepositoryDTO>
	 */
	public List<DmmRepositoryDTO> feeTaiXinAssetList(String yearMonthDay, String customerCode, String TaiXincustomerCode) throws DataAccessException;
	/**
	 * Purpose:維護費設備（綠界）
	 * @author HermanWang
	 * @param yearMonthDay：上個月的年月，格式：YYYYMM
	 * @param customerCode：客戶 綠界
	 * @throws DataAccessException：出錯時, 丟出DataAccessException
	 * @return List<DmmRepositoryDTO>
	 */
	public List<DmmRepositoryDTO> feeChatAssetListGreenWorld(List<String> status, String yearMonthDay, String customerCode) throws DataAccessException;
	/**
	 * Purpose:维护费报表(捷达威格式)--查询（TMS次数）设备
	 * @author CarrieDuan
	 * @param monthYear: 上个月的年月，格式：YYYYMM
	 * @param discountContract: 设定的免费合约编号
	 * @param customerId: 客戶ID
	 * @throws DataAccessException：出錯時, 丟出DataAccessException
	 * @return Integer: 符合條件的個數
	 */
	public Integer getTmsTotal(String monthYear, List<String> discountContract, String customerId) throws DataAccessException;
	
	/**
	 * Purpose:環匯費用總表
	 * @author CarrieDuan
	 * @param monthYear:查詢月份
	 * @param commModeId:需要统计的通讯模式ID
	 * @param customerId:需要查询的客户ID
	 * @throws DataAccessException：出錯時, 丟出DataAccessException
	 * @return List<DmmRepositoryDTO>：設備list
	 */
	public List<DmmRepositoryDTO> listFeeAssetListForGp(String monthYear, List<String> commModeId, String customerId) throws DataAccessException;
	/**
	 * Purpose:環匯費用總表-查詢使用中個數
	 * @author CarrieDuan
	 * @param monthYear：查詢月份
	 * @param startDate：查詢起始時間
	 * @param endDate：查詢截止時間
	 * @param commModeId:需要统计的通讯模式ID
	 * @param customerId:需要查询的客户ID
	 * @throws DataAccessException：出錯時, 丟出DataAccessException
	 * @return List<DmmRepositoryDTO>：設備list
	 */
	public List<DmmRepositoryDTO> listFeeAssetListInUseForGp(String monthYear, Date startDate, Date endDate, List<String> commModeId, String customerId) throws DataAccessException;

	/**
	 * Purpose:報廢機明細（費用報表環匯）
	 * @author CarrieDuan
	 * @param monthYear：查詢月份
	 * @param customerId:需要查询的客户ID
	 * @throws DataAccessException：出錯時, 丟出DataAccessException
	 * @return List<DmmRepositoryDTO>：報廢list集合
	 */
	public List<DmmRepositoryDTO> listScrapAssetForGp(String monthYear, String customerId) throws DataAccessException;
	
	/**
	 * Purpose:獲取啟用設備明細
	 * @author CarrieDuan
	 * @param monthYear：查詢月份
	 * @param assetStatus：設備狀態
	 * @param companyId: 公司ID
	 * @return List<DmmRepositoryDTO>：list集合
	 */
	public List<DmmRepositoryDTO> listEnableAssetInfoForGp(String monthYear,Date startDate, Date endDate ,List<String> assetStatus, String companyId);
	/**
	 * Purpose:上銀	設備明細(BY設備)
	 * @author HermanWang
	 * @param yearMonthDay：上個月的年月，格式：YYYYMM
	 * @param customerCode：上銀ID
	 * @param startDate：開始日期 
	 * @param endDate：結束日期
	 * @throws DataAccessException：出錯時, 丟出DataAccessException
	 * @return List<DmmRepositoryDTO>：查詢結果集合
	 */
	public List<DmmRepositoryDTO> assetInfoListScsb(String yearMonthDay, String customerCode, Date startDate, Date endDate) throws DataAccessException;
	/**
	 * Purpose:維護費設備（上銀）
	 * @author HermanWang
	 * @param yearMonthDay：上個月的年月，格式：YYYYMM
	 * @param customerCode：客戶 上銀
	 * @throws DataAccessException：出錯時, 丟出DataAccessException
	 * @return List<DmmRepositoryDTO>
	 */
	public List<DmmRepositoryDTO> feeAssetListScsb(List<String> status, String yearMonthDay, String customerCode) throws DataAccessException;
	
	/**
	 * Purpose:庫存歷史月檔清除
	 * @author CrissZhang
	 * @param deleteDate :刪除日期
	 * @throws DataAccessException：出錯時, 丟出DataAccessException
	 * @return void
	 */
	public void deleteRepoMonthlyHis(String deleteDateString) throws DataAccessException;
	/**
	 * Purpose:	陽信格式 設備明細(BY設備)
	 * @author HermanWang
	 * @param yearMonthDay：上個月的年月，格式：YYYYMM
	 * @param customerCode：陽信ID
	 * @param startDate：開始日期 
	 * @param endDate：結束日期
	 * @throws DataAccessException：出錯時, 丟出DataAccessException
	 * @return List<DmmRepositoryDTO>：查詢結果集合
	 */
	public List<DmmRepositoryDTO> assetInfoListSyb(String yearMonthDay, String customerCode, Date startDate, Date endDate) throws DataAccessException;
	/**
	 * Purpose:維護費設備（陽信）
	 * @author HermanWang
	 * @param yearMonthDay：上個月的年月，格式：YYYYMM
	 * @param customerCode：客戶 陽信
	 * @throws DataAccessException：出錯時, 丟出DataAccessException
	 * @return List<DmmRepositoryDTO>
	 */
	public List<DmmRepositoryDTO> feeAssetListSyb(String yearMonthDay, String customerCode) throws DataAccessException;
	/**
	 * Purpose:	彰銀格式 設備明細(BY設備)
	 * @author HermanWang
	 * @param yearMonthDay：上個月的年月，格式：YYYYMM
	 * @param customerCode：彰銀ID
	 * @param startDate：開始日期 
	 * @param endDate：結束日期
	 * @throws DataAccessException：出錯時, 丟出DataAccessException
	 * @return List<DmmRepositoryDTO>：查詢結果集合
	 */
	public List<DmmRepositoryDTO> assetInfoListChb(String yearMonthDay, String customerCode, Date startDate, Date endDate) throws DataAccessException;
	/**
	 * Purpose:維護費設備（彰銀）
	 * @author HermanWang
	 * @param yearMonthDay：上個月的年月，格式：YYYYMM
	 * @param customerCode：客戶 	彰銀
	 * @throws DataAccessException：出錯時, 丟出DataAccessException
	 * @return List<DmmRepositoryDTO>
	 */
	public List<DmmRepositoryDTO> feeAssetListChb(List<String> repoStatusList, String yearMonthDay, String customerCode, Date endDate) throws DataAccessException;
}
