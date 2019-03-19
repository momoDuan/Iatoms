package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.Date;
import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAssetLinkDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseAssetLink;

/**
 * Purpose: SRM_案件處理中設備連接檔 DAO
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2017/2/24
 * @MaintenancePersonnel CrissZhang
 */
public interface ISrmCaseAssetLinkDAO extends IGenericBaseDAO<SrmCaseAssetLink> {
	/**
	 * Purpose:根據caseID查詢案件處理中的鏈接設備
	 * @author HermanWang
	 * @param caseId：案件編號
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<SrmCaseTransactionDTO>
	 */
	public List<SrmCaseAssetLinkDTO> listByCaseId(String caseId, String flag, Boolean isAssetLink, Boolean isSelectDelete) throws DataAccessException;

	/**
	 * Purpose: 處理設備鏈接檔的異動
	 * @author CrissZhang
	 * @param caseId ： 案件編號
	 * @param assetId ： 設備編號
	 * @param itemType ： 項目類別
	 * @throws DataAccessException: 出錯時, 丟出DataAccessException
	 * @return void ： 無返回
	 */
	public void updateCaseAsset(String caseId, String assetId, String itemType) throws DataAccessException;
	
	/**
	 * Purpose:根據案件編號刪除鏈接檔信息
	 * @author CrissZhang
	 * @param caseId ： 案件編號
	 * @throws DataAccessException ： 出錯時拋出DataAccessException
	 * @return void
	 */
	public void deleteAll(String caseId) throws DataAccessException;
	
	/**
	 * Purpose:根據按鍵編號刪除鏈接檔信息（忽略所傳項目類別）
	 * @author CrissZhang
	 * @param caseId ： 案件編號
	 * @param ignoreItem ： 忽略項目類別
	 * @throws DataAccessException ： 出錯時拋出DataAccessException
	 * @return void
	 */
	public void deleteIgnoreItem(String caseId, String ignoreItem) throws DataAccessException;
	/**
	 * Purpose:根據dtid查詢案件歷史的鏈接設備
	 * @author HermanWang
	 * @param dtid：dtid
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<SrmCaseTransactionDTO>
	 */
	public List<SrmCaseAssetLinkDTO> listByDtid(String dtid, String caseInfoTableName, String caseLinkTableName) throws DataAccessException;
	/**
	 * Purpose:根據caseid查詢案件歷史的鏈接設備
	 * @author HermanWang
	 * @param caseId：案件編號
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<SrmCaseTransactionDTO>
	 */
	public List<SrmCaseAssetLinkDTO> listByCaseId(String caseId) throws DataAccessException;
	/**
	 * Purpose:根據caseid查詢該案件是否有簽收或者線上排除的記錄
	 * @author HermanWang
	 * @param caseId：案件編號
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return Boolean：返回一個booean類型
	 */
	public Boolean isWaitClose(String caseId) throws DataAccessException;
	/**
	 * Purpose:根據caseid查詢改案件所對應的有設備序號的設備list
	 * @author HermanWang
	 * @param caseId：案件編號
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<SrmCaseAssetLinkDTO>：返回一個dtolist
	 */
	public List<SrmCaseAssetLinkDTO> getCaseAssetLinkByCaseId(String caseId, boolean flag) throws DataAccessException;
	/**
	 * Purpose:维护费报表(大眾格式)--查询作业明细
	 * @author HermanWang
	 * @param customerId:大眾客戶
	 * @param startDate：查詢開始時間
	 * @param endDate：查詢截止時間
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseAssetLinkDTO>：案件資料集合
	 */
	public List<SrmCaseAssetLinkDTO> getCaseAssetLinkInFee(List<String> caseStatus, String customerId, Date startDate, Date endDate) throws DataAccessException;

	/**
	 * Purpose:維護費報表（環匯格式）-ECR線材,網路線
	 * @author CarrieDuan
	 * @param caseStatus：案件狀態
	 * @param customerId：客戶ID
	 * @param startDate：查詢開始時間
	 * @param endDate：查詢結束時間
	 * @param suppliesTypes: 耗材分類
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseAssetLinkDTO>：案件資料集合
	 */
	public List<SrmCaseAssetLinkDTO> listSuppliesTypeForGp(List<String> caseStatus, String customerId, Date startDate, Date endDate, List<String> suppliesTypes)  throws DataAccessException;
	/**
	 * Purpose:维护费报表（綠界格式)ECR線材,網路線sql
	 * @author HermanWang
	 * @param caseStatus:案件狀態 '完修' , '待結案審查' , '結案' , '立即結案'
	 * @param customerId：–綠界的公司主鍵
	 * @param startDate：COMPLETE_DATE月初
	 * @param endDate：COMPLETE_DATE 月末
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseAssetLinkDTO>： 返回一個設備鏈接檔的 dtolist 
	 */
	public List<SrmCaseAssetLinkDTO> getCaseAssetLinkInGreenWorld(List<String> caseStatus, String customerId, Date startDate, Date endDate) throws DataAccessException;
	public List<SrmCaseAssetLinkDTO> getCaseAssetLinkInTaiXin(List<String> caseStatus, String customerId, Date startDate, Date endDate, String TaiXin) throws DataAccessException;
	/**
	 * Purpose:维护费报表（綠界格式)(4).	計算耗材單價數量
	 * @author Hermanwang
	 * @param caseStatus:案件狀態 '完修' , '待結案審查' , '結案' , '立即結案'
	 * @param customerId：–綠界的公司主鍵
	 * @param startDate:COMPLETE_DATE月初
	 * @param endDate：COMPLETE_DATE 月末
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseAssetLinkDTO>： 返回一個設備鏈接檔的 dtolist 
	 */
	public List<SrmCaseAssetLinkDTO> getCaseLinkSuppliesInGreenWorld(List<String> caseStatus, String customerId, Date startDate, Date endDate) throws DataAccessException;
	public List<SrmCaseAssetLinkDTO> getCaseLinkSuppliesInTaiXin(List<String> caseStatus, String customerId, Date startDate, Date endDate, String TaiXin) throws DataAccessException;
	/**
	 * Purpose:维护费报表（上銀格式)ECR線材,網路線sql
	 * @author HermanWang
	 * @param caseStatus:案件狀態 '完修' , '待結案審查' , '結案' , '立即結案'
	 * @param customerId：–上銀的公司主鍵
	 * @param startDate：COMPLETE_DATE月初
	 * @param endDate：COMPLETE_DATE 月末
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseAssetLinkDTO>： 返回一個設備鏈接檔的 dtolist 
	 */
	public List<SrmCaseAssetLinkDTO> getCaseAssetLinkInScsb(List<String> caseStatus, String customerId, Date startDate, Date endDate) throws DataAccessException;
	/**
	 * Purpose:维护费报表（上銀格式)(4).	計算耗材單價數量
	 * @author HermanWang
	 * @param caseStatus:案件狀態 '完修' , '待結案審查' , '結案' , '立即結案'
	 * @param customerId:上銀的公司主鍵
	 * @param startDate：COMPLETE_DATE月初
	 * @param endDate：COMPLETE_DATE 月末
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseAssetLinkDTO>： 返回一個設備鏈接檔的 dtolist 
	 */
	public List<SrmCaseAssetLinkDTO> getCaseLinkSuppliesInScsb(List<String> caseStatus, String customerId, Date startDate, Date endDate) throws DataAccessException;
	/**
	 * Purpose:根據設備序號查詢鏈接檔和案件相關信息 上銀的 已啟用待裝機：且【最近一次案件，案件類別=異動、拆機且解除綁定動作=已拆回】
	 * @author HermanWang
	 * @param serialNumber:設備序號
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseAssetLinkDTO>： 返回一個設備鏈接檔的 dtolist 
	 */
	public List<SrmCaseAssetLinkDTO> getCaseLinkAndCaseInfoBySerialNumber(String serialNumber) throws DataAccessException;
	/**
	 * Purpose:维护费报表（陽信格式)ECR線材,網路線sql
	 * @author HermanWang
	 * @param caseStatus:案件狀態 '完修' , '待結案審查' , '結案' , '立即結案'
	 * @param customerId：陽信的公司主鍵
	 * @param startDate：COMPLETE_DATE月初
	 * @param endDate：COMPLETE_DATE 月末
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseAssetLinkDTO>： 返回一個設備鏈接檔的 dtolist 
	 */
	public List<SrmCaseAssetLinkDTO> getCaseAssetLinkInSyb(List<String> caseStatus, String customerId, Date startDate, Date endDate) throws DataAccessException;
	/**
	 * Purpose:维护费报表（陽新格式)(4).	計算耗材單價數量
	 * @author HermanWang
	 * @param caseStatus:案件狀態 '完修' , '待結案審查' , '結案' , '立即結案'
	 * @param customerId:陽信的公司主鍵
	 * @param startDate：COMPLETE_DATE月初
	 * @param endDate：COMPLETE_DATE 月末
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseAssetLinkDTO>： 返回一個設備鏈接檔的 dtolist 
	 */
	public List<SrmCaseAssetLinkDTO> getCaseLinkSuppliesInSyb(List<String> caseStatus, String customerId, Date startDate, Date endDate) throws DataAccessException;
	/**
	 * Purpose:维护费报表（彰銀格式)ECR線材,網路線sql
	 * @author HermanWang
	 * @param caseStatus:案件狀態 '完修' , '待結案審查' , '結案' , '立即結案'
	 * @param customerId：彰銀的公司主鍵
	 * @param startDate：COMPLETE_DATE月初
	 * @param endDate：COMPLETE_DATE 月末
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseAssetLinkDTO>： 返回一個設備鏈接檔的 dtolist 
	 */
	public List<SrmCaseAssetLinkDTO> getCaseAssetLinkInChb(List<String> caseStatus, String customerId, Date startDate, Date endDate) throws DataAccessException;
	/**
	 * Purpose:维护费报表（彰銀格式)(4).	計算耗材單價數量
	 * @author HermanWang
	 * @param caseStatus:案件狀態 '完修' , '待結案審查' , '結案' , '立即結案'
	 * @param customerId:彰銀的公司主鍵
	 * @param startDate：COMPLETE_DATE月初
	 * @param endDate：COMPLETE_DATE 月末
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseAssetLinkDTO>： 返回一個設備鏈接檔的 dtolist 
	 */
	public List<SrmCaseAssetLinkDTO> getCaseLinkSuppliesInChb(List<String> caseStatus, String customerId, Date startDate, Date endDate) throws DataAccessException;
	
}
