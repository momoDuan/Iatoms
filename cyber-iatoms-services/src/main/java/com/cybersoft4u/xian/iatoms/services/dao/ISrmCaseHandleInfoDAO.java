package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;
import cafe.core.service.ServiceException;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseHandleInfo;
/**
 * Purpose: SRM_案件處理資料檔DAO接口
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016年11月10日
 * @MaintenancePersonnel CrissZhang
 */
public interface ISrmCaseHandleInfoDAO extends IGenericBaseDAO<SrmCaseHandleInfo>{

	/**
	 * Purpose:查詢案件信息
	 * @author CrissZhang
	 * @param formDTO : 存放查詢參數的對象
	 * @throws DataAccessException ： 出錯時拋出DataAccessException
	 * @return List<SrmCaseHandleInfoDTO> ： 得到一個案件處理對象的集合
	 */
	public List<SrmCaseHandleInfoDTO> queryListBy(CaseManagerFormDTO formDTO)throws DataAccessException;
	/**
	 * 
	 * Purpose:查詢案件信息數目
	 * @author CrissZhang
	 * @param formDTO : 存放查詢參數的對象
	 * @throws DataAccessException ： 出錯時拋出DataAccessException
	 * @return int ： 得到案件信息數目
	 */
	public int count(CaseManagerFormDTO formDTO) throws DataAccessException;

	/**
	 * Purpose:通過案件編號得到一筆案件信息
	 * @author CrissZhang
	 * @param caseId ： 案件編號
	 * @throws DataAccessException ： 出錯時拋出DataAccessException
	 * @return SrmCaseHandleInfoDTO ： 得到案件處理信息的DTO
	 */
	public List<SrmCaseHandleInfoDTO> getCaseInfoById(String caseId, String isHistory) throws DataAccessException;
	/**
	 * Purpose:通過已更新的案件編號得到一筆案件信息
	 * @author amandawang
	 * @param caseId ： 案件編號
	 * @throws DataAccessException ： 出錯時拋出DataAccessException
	 * @return SrmCaseHandleInfoDTO ： 得到案件處理信息的DTO
	 */
	public List<SrmCaseHandleInfoDTO> getChangeCaseInfoById(String caseId, String isHistory) throws DataAccessException;
	/**
	 * Purpose:顯示是否重複進建以及重複進建的caseid
	 * @author HermanWang
	 * @param caseId ： 案件編號
	 * @throws DataAccessException ： 出錯時拋出DataAccessException
	 * @return SrmCaseHandleInfoDTO ： 得到案件處理信息的DTO
	 */
	public List<SrmCaseHandleInfoDTO> getCaseRepeatList(String dtid, String caseId, List<String> caseStatusList) throws DataAccessException;
	/**
	 * Purpose:查詢該筆dtid下比本案件建安早的裝機案件數量
	 * @author HermanWang
	 * @param dtid ： dtid
	 * @throws DataAccessException ： 出錯時拋出DataAccessException
	 * @return int ： 案件條數
	 */
	public int getCountByInstall(String dtid, String caseId, boolean isNew) throws DataAccessException;
	/**
	 * Purpose:顯示同一筆dtid之案件為裝機案且未完修的案件資料
	 * @author amandawang
	 * @param dtid:dtid
	 * @param caseId ： 案件編號
	 * @throws DataAccessException ： 出錯時拋出DataAccessException
	 * @return SrmCaseHandleInfoDTO ： 得到案件處理信息的DTO
	 */
	public List<SrmCaseHandleInfoDTO> getCaseRepeatList(String dtid, String caseId, List<String> caseStatusList, String caseCategory) throws DataAccessException;
	/**
	 * Purpose:查詢EDC報表服務需求案件(維修)分析月報報表信息
	 * @author CarrieDuan
	 * @param customerId:客戶編號
	 * @param startDate:當前系統日上個月第一天時間.
	 * @param endDate:當前系統日本月第一天時間
	 * @param orderByCode:依X統計月報
	 * @throws DataAccessException： 出錯時拋出DataAccessException
	 * @return List<SrmCaseHandleInfoDTO> ： 得到案件處理信息的DTO
	 */
	public List<SrmCaseHandleInfoDTO> listEdcReqCaseAnalysisMonReport(String customerId, Date startDate, Date endDate, String orderByCode) throws DataAccessException;
	
	/**
	 * Purpose:查詢未完修報表(大眾格式)資料
	 * @author CarrieDuan
	 * @param customerId: 客戶編號
	 * @param expectedCompletionDate:當前系統日
	 * @param caseCategoryArray: 案件類別(報修、專案)
	 * @param caseStatusArray: 案件狀態(完修、待結案審查、結案、立即結案、已作廢)
	 * @param isPublic:是否為大眾格式
	 * @throws DataAccessException： 出錯時拋出DataAccessException
	 * @return List<SrmCaseHandleInfoDTO>:未完修報表(大眾格式)結果集
	 */
	public List<SrmCaseHandleInfoDTO> listUnfinishedReportToTCB(String customerId, Date expectedCompletionDate, List<String> caseCategoryArray, List<String> caseStatusArray, Boolean isPublic, List<String> installedCaseCategorys) throws DataAccessException;

	/**
	 * Purpose:得到問題原因的下拉框
	 * @author CrissZhang
	 * @throws DataAccessException： 出錯時拋出DataAccessException
	 * @return List<Parameter> : 問題原因下拉框
	 */
	public List<Parameter> getProblemReasonList(boolean isTSB) throws DataAccessException;
	
	/**
	 * Purpose:得到問題解決方式的下拉框
	 * @author CrissZhang
	 * @throws DataAccessException： 出錯時拋出DataAccessException
	 * @return List<Parameter> : 問題原因下拉框
	 */
	public List<Parameter> getProblemSolutionList(boolean isTSB) throws DataAccessException;
	
	/**
	 * Purpose:查詢完工回覆檔(環匯格式)資料
	 * @author CarrieDuan
	 * @param customerId:客戶編號
	 * @param completeDate:當前系統日-1日
	 * @param caseCategoryArray:案件類別(裝機、併機、拆機、異動)
	 * @param caseStatusArray:案件狀態(完修、待結案審查、結案、立即結案)
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseHandleInfoDTO>:完工回覆檔(環匯格式)結果集HermanWang
	 */
	public List<SrmCaseHandleInfoDTO> listFinishedReportToGP(String customerId, String completeDate, List<String> caseCategoryArray, List<String> caseStatusArray) throws DataAccessException;
	
	/**
	 * Purpose:查詢完工回覆檔(大眾格式)資料
	 * @author HermanWang
	 * @param customerId:客戶編號
	 * @param completeDate:當前系統日-1日
	 * @param caseCategoryArray:案件類別(裝機、併機、拆機、異動)
	 * @param caseStatusArray:案件狀態(完修、待結案審查、結案、立即結案)
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseHandleInfoDTO>:完工回覆檔(大眾格式)結果集
	 */
	public List<SrmCaseHandleInfoDTO> listFinishedReportToTCB(String customerId, String completeDate, List<String> caseCategoryArray, List<String> caseStatusArray) throws DataAccessException;
	/**
	 * Purpose:根據案件單號將案件資料處理中相關信息複製到歷史資料和最新資料的相關表中
	 * @author ElvaHe
	 * @param caseId：案件單號
	 * @param dtid:案件DTID
	 * @param isToNew:是否複製到最新檔
	 * @param isCloseCopy:是否結案複製
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 */
	public void copyToHis(String caseId, String ditd, String isToNew, String isCloseCopy) throws DataAccessException;
	
	/**
	 * Purpose:查詢完修逾時率警示通知-完修逾時率資料
	 * @author CarrieDuan
	 * @param customerId：客戶編號
	 * @param startDate:起始時間
	 * @param endDate:截至日期
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseHandleInfoDTO>：完修逾時率資料結果集
	 */
	public List<SrmCaseHandleInfoDTO> listCompleteOverdueRate(String customerId, Date startDate, Date endDate) throws DataAccessException;
	
	/**
	 * Purpose:完修逾時率警示通知-進件案件明細
	 * @author CarrieDuan
	 * @param customerId:客戶編號
	 * @param startDate:起始時間
	 * @param endDate:截至日期
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseHandleInfoDTO>:完修逾時率進件案件明細結果集
	 */
	public List<SrmCaseHandleInfoDTO> listCreateCaseToOverdueReport(String customerId, Date startDate, Date endDate) throws DataAccessException;
	
	/**
	 * Purpose:完修逾時率警示通知-結案案件明細、完修逾時案件明細
	 * @author CarrieDuan
	 * @param customerId:客戶編號
	 * @param startDate:起始時間
	 * @param endDate:截至日期
	 * @param isOverdue:是否添加逾時條件
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseHandleInfoDTO>:完修逾時率結案案件明細、完修逾時結果集
	 */
	public List<SrmCaseHandleInfoDTO> listClosedCaseToOverdueReport(String customerId, Date startDate, Date endDate, Boolean isOverdue) throws DataAccessException;
	
	/**
	 * Purpose:系统需于每日4:00，发送提醒邮件，提醒完工回覆檔(歐付寶格式).
	 * @author CarrieDuan
	 * @param customerId:客戶編號
	 * @param completeDate:當前系統日-1日
	 * @param caseCategoryArray:案件類別(報修、專案)
	 * @param caseStatusArray:案件狀態(完修、待結案審查、結案、立即結案)
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseHandleInfoDTO>:完工回覆檔(歐付寶格式)結果集
	 */
	public List<SrmCaseHandleInfoDTO> listFinishedReportToOFB(String customerId, String completeDate, List<String> caseCategoryArray, List<String> caseStatusArray) throws DataAccessException;
	
	/**
	 * Purpose: 查詢DTID在流程中是否有未結案的案件
	 * @author CarrieDuan
	 * @param dtid ：查詢條件DTID
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseHandleInfoDTO>：案件編號集合
	 */
	public List<SrmCaseHandleInfoDTO> listCaseIdByDtid (String dtid) throws DataAccessException;
	/**
	 * Purpose:查詢裝機案件信息
	 * @author HermanWang
	 * @param queryCustomerId：客戶id
	 * @param queryCompleteDateStart：完成日期開始
	 * @param queryCompleteDateEnd:完成日期結束
	 * @param category：案件類別
	 * @param pageIndex：當前頁
	 * @param pageSize：每頁條數
	 * @param sort：排序字段
	 * @param order：排序方式
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseHandleInfoDTO>：案件dtolist
	 */
	public List<SrmCaseHandleInfoDTO> installListBy(String queryCustomerId, String queryCompleteDateStart, String queryCompleteDateEnd,String isInstant,
			Integer pageIndex, Integer pageSize, String sort, String order) throws DataAccessException;
	/**
	 * Purpose:查詢拆機案件信息
	 * @author HermanWang
	 * @param queryCustomerId：客戶id
	 * @param queryCompleteDateStart：完成日期開始
	 * @param queryCompleteDateEnd:完成日期結束
	 * @param category：案件類別
	 * @param pageIndex：當前頁
	 * @param pageSize：每頁條數
	 * @param sort：排序字段
	 * @param order：排序方式
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseHandleInfoDTO>：案件dtolist
	 */
	public List<SrmCaseHandleInfoDTO> unInstallListBy(String queryCustomerId, String queryCompleteDateStart, String queryCompleteDateEnd,String isInstant,
			Integer pageIndex, Integer pageSize, String sort, String order) throws DataAccessException;
	/**
	 * Purpose:查詢拆機案件信息的count
	 * @author HermanWang
	 * @param queryCustomerId：客戶id
	 * @param queryCompleteDateStart：完成日期開始
	 * @param queryCompleteDateEnd:完成日期結束
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return int
	 */
	public int unInstallCount(String queryCustomerId, String queryCompleteDateStart, 
			String queryCompleteDateEnd, String isInstant) throws DataAccessException;
	/**
	 * Purpose:查詢裝機案件信息的count
	 * @author HermanWang
	 * @param queryCustomerId：客戶id
	 * @param queryCompleteDateStart：完成日期開始
	 * @param queryCompleteDateEnd:完成日期結束
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return int
	 */
	public int installCount(String queryCustomerId, String queryCompleteDateStart, 
			String queryCompleteDateEnd, String isInstant) throws DataAccessException;
	/**
	 * Purpose:查詢其他案件信息
	 * @author HermanWang
	 * @param queryCustomerId：客戶id
	 * @param queryCompleteDateStart：完成日期開始
	 * @param queryCompleteDateEnd:完成日期結束
	 * @param category：案件類別
	 * @param pageIndex：當前頁
	 * @param pageSize：每頁條數
	 * @param sort：排序字段
	 * @param order：排序方式
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseHandleInfoDTO>：案件dtolist
	 */
	public List<SrmCaseHandleInfoDTO> otherListBy(String queryCustomerId, String queryCompleteDateStart, String queryCompleteDateEnd,String isInstant,
			Integer pageIndex, Integer pageSize, String sort, String order) throws DataAccessException;
	/**
	 * Purpose:查詢其他案件信息的count
	 * @author HermanWang
	 * @param queryCustomerId：客戶id
	 * @param queryCompleteDateStart：完成日期開始
	 * @param queryCompleteDateEnd:完成日期結束
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return int
	 */
	public int otherCount(String queryCustomerId,String queryCompleteDateStart, String queryCompleteDateEnd,String isInstant) throws DataAccessException;
	/**
	 * Purpose:查詢查核案件信息
	 * @author HermanWang
	 * @param queryCustomerId：客戶id
	 * @param queryCompleteDateStart：完成日期開始
	 * @param queryCompleteDateEnd:完成日期結束
	 * @param category：案件類別
	 * @param pageIndex：當前頁
	 * @param pageSize：每頁條數
	 * @param sort：排序字段
	 * @param order：排序方式
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseHandleInfoDTO>：案件dtolist
	 */
	public List<SrmCaseHandleInfoDTO> checkListBy(String queryCustomerId, String queryCompleteDateStart, String queryCompleteDateEnd, String isInstant,
			Integer pageIndex, Integer pageSize, String sort, String order) throws DataAccessException;
	
	/**
	 * Purpose:查詢查核案件信息的count
	 * @author HermanWang
	 * @param queryCustomerId：客戶id
	 * @param queryCompleteDateStart：完成日期開始
	 * @param queryCompleteDateEnd:完成日期結束
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return int
	 */
	public int checkCount(String queryCustomerId, String queryCompleteDateStart, String queryCompleteDateEnd, String isInstant) throws DataAccessException;
	
	/**
	 * Purpose:轉移結案日期之前的案件資料到案件歷史檔中
	 * @author amandawang
	 * @param moveDate：結案時間
	 * @throws DataAccessException
	 * @return void
	 */
	public void moveCaseToHistroy(Date moveDate) throws DataAccessException;
	
	/**
	 * Purpose: 求償作業---查詢DTID以及點選一筆資料后，獲取相應的信息
	 * @author CarrieDuan
	 * @param historyId :id
	 * @param companyId :客戶ID
	 * @param merchantCode ：特點代號
	 * @param dtid ：DTID
	 * @param tid ：TID
	 * @param serialNumber ：設備序號
	 * @param caseId ：案件編號
	 * @param sort ：排序列
	 * @param order ：排序方式
	 * @param pageSize ：每頁顯示條數
	 * @param pageIndex ：第幾頁
	 * @throws DataAccessException ：出錯時拋出DataAccessException
	 * @return List<SrmHistoryCaseHandleInfoDTO> :案件資料集合
	 */
	public List<SrmCaseHandleInfoDTO> listBy(String historyId, String companyId, String merchantCode, String dtid, String tid, String serialNumber, String caseId, String sort, String order, Integer pageSize, Integer pageIndex)throws DataAccessException;
	
	/**
	 * Purpose: 求償作業---查詢DTID獲取總行數
	 * @author CarrieDuan
	 * @param historyId :id
	 * @param companyId :客戶ID
	 * @param merchantCode ：特點代號
	 * @param dtid ：DTID
	 * @param tid ：TID
	 * @param serialNumber ：設備序號
	 * @param caseId ：案件編號
	 * @throws DataAccessException ：出錯時拋出DataAccessException
	 * @return Integer
	 */
	public Integer getCount(String historyId, String companyId, String merchantCode, String dtid, String tid, String serialNumber, String caseId)throws DataAccessException;
	
	/**
	 * Purpose:维护费报表(捷达威格式)--查询作业明细
	 * @author CarrieDuan
	 * @param customerId: 客户ID
	 * @param caseStatus：案件狀態
	 * @param commonTransactionType: 一般交易
	 * @param startDate：查詢開始時間
	 * @param endDate：查詢截止時間
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseHandleInfoDTO>：案件資料集合
	 */
	public List<SrmCaseHandleInfoDTO> listComplateCaseList(String customerId, List<String> caseStatus, List<String> commonTransactionType, Date startDate, Date endDate) throws DataAccessException;
	/**
	 * Purpose:维护费报表(大眾格式)--查询作业明细
	 * @author HermanWang
	 * @param customerId:大眾客戶
	 * @param startDate：查詢開始時間
	 * @param endDate：查詢截止時間
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseHandleInfoDTO>：案件資料集合
	 */
	public List<SrmCaseHandleInfoDTO> listComplateCaseList(List<String> caseStatus, List<String> commonTransactionType, String customerId, Date startDate, Date endDate) throws DataAccessException;
	/**
	 * Purpose:维护费报表(綠界格式)--查询作业明细
	 * @author HermanWang
	 * @param caseStatus：案件狀態
	 * @param commonTransactionType：通訊模式
	 * @param customerId：綠界的公司id
	 * @param startDate：開始日期 
	 * @param endDate：結束日期 
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseHandleInfoDTO>:案件集合
	 */
	public List<SrmCaseHandleInfoDTO> getCaseListInGreenWorld(List<String> caseStatus,  List<String> commonTransactionType, String customerId, Date startDate, Date endDate, boolean isTaiXin, String TaiXin) throws DataAccessException;
	
	/**
	 * Purpose:刪除所有轉入數據
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return void : 無返回
	 */
	public void deleteTransferData() throws DataAccessException;
	
	/**
	 * Purpose:驗證是否異動設備
	 * @author CrissZhang
	 * @param caseId :	案件編號
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return boolean：返回boolean
	 */
	public boolean isChangeAsset (String caseId) throws ServiceException;
	
	/**
	 * Purpose:维护费报表(環匯格式)--查询作业明细
	 * @author CarrieDuan
	 * @param caseStatus：案件狀態
	 * @param customerId：客戶ID
	 * @param startDate：查詢開始時間
	 * @param endDate：查詢結束時間
	 * @param transactionTypes：交易類別
	 * @throws DataAccessException:出錯時拋出ServiceException
	 * @return List<SrmCaseHandleInfoDTO>:案件集合
	 */
	public List<SrmCaseHandleInfoDTO> listCaseWorkDetailForGp(List<String> caseStatus, Map<String, Integer> priceMap, String customerId, Date startDate, Date endDate, List<String> transactionTypes) throws DataAccessException;
	/**
	 * Purpose:维护费报表(上銀格式)--查询作业明细
	 * @author HermanWang
	 * @param caseStatus：案件狀態
	 * @param commonTransactionType：通訊模式
	 * @param customerId：上銀的公司id
	 * @param startDate：開始日期 
	 * @param endDate：結束日期 
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseHandleInfoDTO>:案件集合
	 */
	public List<SrmCaseHandleInfoDTO> getCaseListInScsb(List<String> caseStatus,  String customerId, Date startDate, Date endDate, List<String> transactionTypes) throws DataAccessException;
	/**
	 * Purpose:维护费报表(陽信格式)--查询作业明细
	 * @author HermanWang
	 * @param caseStatus：案件狀態
	 * @param customerId：陽信的公司id
	 * @param startDate：開始日期 
	 * @param endDate：結束日期 
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseHandleInfoDTO>:案件集合
	 */
	public List<SrmCaseHandleInfoDTO> getCaseListInSyb(List<String> caseStatus,  String customerId, Date startDate, Date endDate) throws DataAccessException;
	/**
	 * Purpose:维护费报表(彰銀格式)--查询作业明细
	 * @author HermanWang
	 * @param caseStatus：案件狀態
	 * @param commonTransactionTypeList:交易參數類別列表
	 * @param customerId：彰銀的公司id
	 * @param startDate：開始日期 
	 * @param endDate：結束日期 
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseHandleInfoDTO>:案件集合
	 */
	public List<SrmCaseHandleInfoDTO> getCaseListInChb(List<String> commonTransactionTypeList, List<String> caseStatus,  String customerId, Date startDate, Date endDate) throws DataAccessException;

	/**
	 * Purpose:查詢轉入案件筆數
	 * @author CrissZhang
	 * @throws DataAccessException
	 * @return List<Integer>
	 */
	public List<Integer> queryTransferCaseNum(String caseId)throws DataAccessException;
	/**
	 * Purpose:線上排除、簽收、協調完成--檢核設備是否異動時 當處理中案件 案件設備連接檔無設備時，將最新資料檔中的copy出來的存儲過程
	 * @author amandawang
	 * @param caseId :	案件編號
	 * @param linkId :	案件設備連接檔id
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return void : 無返回
	 */
	public void copyNewAssetLinkToCase(String caseId, String linkId, String transactionId) throws DataAccessException ;
	
	/**
	 * Purpose:call cms 所需參數查詢
	 * @author CarrieDuan
	 * @param caseId: 案件編號
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return SrmCaseHandleInfoDTO：案件資料
	 */
	public List<SrmCaseHandleInfoDTO> getCaseInfoById(String caseId) throws DataAccessException ;
	
	/**
	 * Purpose:查詢該公司下設備相關的案件資料
	 * @author amandawang
	 * @param assetUserId:使用人(公司id)
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseHandleInfoDTO>： 返回一個案件的 dtolist 
	 */
	public List<SrmCaseHandleInfoDTO> getAssetByCompanyId(String assetUserId, String companyId)throws DataAccessException;
	
	/**
	 * Purpose:獲得每日派工給特定廠商的案件編號
	 * @author nicklin
	 * @param  companyId:廠商編號
	 * @param  dispatchDeptId:派工部門編號
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<String>
	 */
	public List<String> getCaseIdListByDispatching(String companyId, String dispatchDeptId) throws DataAccessException ;
	
	/**
	 * Purpose:查詢兩小時後逾期的案件信息
	 * @author CarrieDuan
	 * @param queryStartDate：查詢開始時間
	 * @param queryEndDate：查詢結束時間
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<SrmCaseHandleInfoDTO>
	 */
	public List<SrmCaseHandleInfoDTO> getOverdueCaseInfo(Date queryStartDate, Date queryEndDate) throws DataAccessException ;
	/**
	 * Purpose:查詢符合條件的裝機案件單號
	 * @author amandawang
	 * @param dtid
	 * @throws DataAccessException
	 * @return String：裝機案件單號
	 */
	public String getInstallCaseId(String dtid) throws DataAccessException;
	/**
	 * Purpose:調用存儲過程修改裝機案件編號
	 * @author amandawang
	 * @param caseId
	 * @param dtid
	 * @param requirementNo
	 * @throws DataAccessException
	 * @return void
	 */
	public void changeInstallCaseId(String caseId, String dtid, String requirementNo, Date installCompleteDate, String flag) throws DataAccessException;
}
