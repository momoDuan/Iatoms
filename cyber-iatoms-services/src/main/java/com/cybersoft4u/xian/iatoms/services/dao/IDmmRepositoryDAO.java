package com.cybersoft4u.xian.iatoms.services.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.bean.identity.LogonUser;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryHistoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetManageFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.CrossTabReportDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmRepository;
/**
 * Purpose: 設備管理作業DAO接口
 * @author amandawang
 * @since  JDK 1.6
 * @date   2016/7/25
 * @MaintenancePersonnel amandawang
 */
@SuppressWarnings("rawtypes")
public interface IDmmRepositoryDAO extends IGenericBaseDAO<DmmRepository> {
	
	/**
	 * Purpose:獲得庫存清單集合
	 * @author amandawang
	 * @param AssetManageFormDTO:設備管理作業formDTO
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<DmmRepositoryDTO> 設備管理作業列表
	 */
	public List<DmmRepositoryDTO> listBy(AssetManageFormDTO assetManageFormDTO) throws DataAccessException;
	/**
	 * Purpose:獲得條數
	 * @author amandawang
	 * @param AssetManageFormDTO:設備管理作業formDTO
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<DmmRepositoryDTO> 設備管理作業列表
	 */
	public Integer count(AssetManageFormDTO assetManageFormDTO) throws DataAccessException;

	/**
	 * Purpose:獲得歷史庫存集合
	 * @author amandawang
	 * @param repositoryFormDTO:設備管理作業formDTO
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<DmmRepositoryHistoryDTO>庫存歷史表
	 */
	public List<DmmRepositoryHistoryDTO> getListByAssetId(AssetManageFormDTO assetManageFormDTO) throws DataAccessException;
	
	/**
	 * Purpose:獲得條數
	 * @author amandawang
	 * @param repositoryFormDTO：設備管理作業formDTO
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return Integer
	 */
	public Integer getCountByAssetId(AssetManageFormDTO assetManageFormDTO) throws DataAccessException;
	
	/**
	 * Purpose:獲得用戶Email與主管Email
	 * @author amandawang
	 * @param userId:使用者ID
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return Parameter
	 */
	public AdmUserDTO getEmailByUserId(String userId) throws DataAccessException;
	
	/**
	 * Purpose:驗證財產編號重複
	 * @author amandawang
	 * @param propertyId:財產編號
	 * @param assetId:設備序號
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return boolean
	 */
	public boolean isRepeatPropertyId(String propertyId, String assetId) throws DataAccessException;
	
	/**
	 * Purpose:驗證DTID重複
	 * @author amandawang
	 * @param dtid:dtid
	 * @param assetId:設備序號
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return boolean
	 */
	public boolean isRepeatDtid(String dtid, String assetId) throws DataAccessException;
	
	/**
	 * Purpose:根據設備序號查詢到庫存信息
	 * @author barryZhang
	 * @param serialNumber:設備序號
	 * @param fromWarehouseId:轉出倉
	 * @param state:庫存狀態
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return DmmRepositoryDTO:庫存信息DTO
	 */
	public DmmRepositoryDTO getRepositoryBySerialNumber(String serialNumber, String fromWarehouseId, String state) throws DataAccessException;
	/**
	 * Purpose:獲取借用到期資料
	 * @author amandawang
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<DmmRepositoryDTO>:庫存信息DTO集合
	 */
	public List<DmmRepositoryDTO> getUnBackBorrowers() throws DataAccessException;
	/**
	 * Purpose:由account獲取userId
	 * @author amandawang
	 * @param account
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return String
	 */
	public String getUserIdByAccount(String account) throws DataAccessException;
	
	/**
	 * Purpose: 檢驗財產編號是否重複
	 * @author CarrieDuan
	 * @param propertyId ：財產編號
	 * @param serialNumber ：設備序號
	 * @throws DataAccessException ：出錯時拋出DataAccessException
	 * @return boolean ：是否重複
	 */
	public boolean isPropertyIdRepeat(String propertyId, String serialNumber) throws DataAccessException;
	
	/**
	 * Purpose:驗證該DTID是否已經被使用。
	 * @author CarrieDuan
	 * @param dtidStart :dtid開始號碼
	 * @param dtidEnd ：dtid結束號碼
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return boolean :是否已經被使用
	 */
	/*public boolean isDtidInUse(String dtidStart, String dtidEnd) throws DataAccessException;*/
	/**
	 * Purpose:根據財編得到設備信息
	 * @author echomou
	 * @param propertyId：財產編號 
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return DmmRepositoryDTO：設備DTO
	 */
	public DmmRepositoryDTO getRepositoryByPropertyId(String propertyId) throws DataAccessException;
	
	/**
	 * Purpose:確認入庫
	 * @author CarrieDuan
	 * @param assetId ：在service創建的庫存ID
	 * @param historyId ：在service創建的庫存歷史ID
	 * @param userId ：創建人員ID
	 * @param userName ：創建人員名稱
	 * @param assetInId ：設備入庫單號
	 * @param cyberApprovedDate ：cyber驗收日期
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return void
	 */
	public void saveRepository(String assetId, String historyId, String userId, String userName, String assetInId, Timestamp cyberApprovedDate) throws DataAccessException;

	/**
	 * Purpose:在核檢修改庫存表的數據時，依據名稱獲取對應的ID
	 * @author CarrieDuan
	 * @param contractCode ：合約代碼
	 * @param assetName ：設備名稱
	 * @param owner ：資產owner
	 * @param user ：使用人
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return DmmRepositoryDTO：設備DTO
	 */
	public DmmRepositoryDTO getRepositoryDTOBy(String contractCode, String assetName, String owner, String user) throws DataAccessException;
	
	/**
	 * Purpose: 獲取財產編號列表
	 * @author CarrieDuan
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<Parameter> ：財產編號集合
	 */
	public List<Parameter> getPropertyIds () throws DataAccessException;
	
	/**
	 * Purpose: 根據所有的設備序號，獲取對應的cyber驗收日期
	 * @author CarrieDuan
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<Parameter> :cyber驗收日期
	 */
	public List<Parameter> getCyberApproveDate() throws DataAccessException;
	
	/**
	 * Purpose:更新數據
	 * @author CarrieDuan
	 * @param updateColumns :需要更新的列
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return void
	 */
	public void updateRepository(DmmRepositoryDTO repositoryDTO, Boolean isCounter, Boolean isCartonNo, Boolean isEnableDate, Boolean isBrand, Boolean isModel , Boolean isMaintenanceMode) throws DataAccessException;
	
	/**
	 * Purpose:設備入庫時匯入驗證重複，獲取所有的設備序號以及財產編號
	 * @author CarrieDuan
	 * @throws DataAccessException ：出錯時跑出DataAccessException
	 * @return List<AssetInListDTO> : 返回設備明細資料當集合
	 */
	public List<DmmRepositoryDTO> getDmmRepositoryDTOs() throws DataAccessException;
	/**
	 * Purpose:查詢EDC
	 * @author echomou
	 * @param queryAssetType ：設備類別
	 * @param queryAssetName ：設備名稱
	 * @param queryPeople ：借用/領用人
	 * @param queryCommet ：借用/領用說明
	 * @param queryHouse ：倉庫名稱
	 * @param queryNumber ：設備序號
	 * @param sort ：排序列
	 * @param order ：排序方式 
	 * @param currentPage ：當前頁
	 * @param pageSize ：頁碼
	 * @throws DataAccessException ：出錯時跑出DataAccessException
	 * @return List<DmmRepositoryDTO> ：DmmRepositoryDTO集合
	 */
	public List<DmmRepositoryDTO> getEDC(String queryAssetType, String queryAssetName, String queryPeople, String queryCommet, String queryHouse, String queryNumber, String sort, String order, Integer currentPage, Integer pageSize)throws DataAccessException;
	/**
	 * Purpose: 根據查詢條件查詢EDC信息的總筆數
	 * @author echomou
	 * @param queryAssetType ：設備類別
	 * @param queryAssetName ：設備名稱
	 * @param queryPeople ：借用/領用人
	 * @param queryCommet ：借用/領用說明
	 * @param queryHouse ：倉庫名稱
	 * @param queryNumber ：設備序號
	 * @throws DataAccessException ：出錯時跑出DataAccessException
	 * @return Integer ：條數
	 */
	public Integer getEDCCount(String queryAssetType, String queryAssetName, String queryPeople, String queryCommet, String queryHouse, String queryNumber)throws DataAccessException;
	
	/**
	 * Purpose: 設備確認轉倉-修改庫存表的設備狀態
	 * @author CarrieDuan
	 * @param serialNumber ：設備序號
	 * @param logonUser : 當前登錄者信息
	 * @param assetTransId : 轉倉批號Id
	 * @throws DataAccessException ：出錯時跑出DataAccessException
	 * @return void
	 */
	public void updateRepository(String serialNumber, LogonUser logonUser, String assetTransId) throws DataAccessException;
	
	/**
	 * Purpose:
	 * @author CarrieDuan
	 * @param assetTransId :轉倉批號ID
	 * @param status ：設備狀態
	 * @param isTransDone ：是否轉倉
	 * @param historyId : 庫存歷史起始ID
	 * @param logonUser : 當前登錄者信息
	 * @param transAction :動作
	 * @throws DataAccessException ：出錯時跑出DataAccessException
	 * @return void
	 */
	public void confirmStorage(String assetTransId, String status, String isTransDone, String historyId, LogonUser logonUser, String transAction) throws DataAccessException;
	/**
	 * Purpose:
	 * @author CarrieDuan
	 * @param assetTransId :轉倉批號ID
	 * @param status ：設備狀態
	 * @param isTransDone ：確認入庫
	 * @param historyId : 庫存歷史起始ID
	 * @param logonUser : 當前登錄者信息
	 * @param transAction :動作
	 * @throws DataAccessException ：出錯時跑出DataAccessException
	 * @return void
	 */
	public void confirmStorageAll(String assetTransId, String status, String isTransDone, String historyId, LogonUser logonUser, String transAction, String transferSuccess) throws DataAccessException;
	
	/**
	 * Purpose:設備盤點-根據輸入的設備序號或者財產編號獲取對應的庫存信息
	 * @author CarrieDuan
	 * @param serialNumber ：設備序號或者財產編號
	 * @throws DataAccessException ：出錯時跑出DataAccessException
	 * @return DmmRepositoryDTO ：庫存DTO
	 */
	public DmmRepositoryDTO getBySerialNumber(String serialNumber) throws DataAccessException;
	/**
	 * Purpose:保存庫存歷史檔與故障組件故障現象歷史檔
	 * @author amandawang
	 * @param assetId :庫存ID
	 * @param historyId : 庫存歷史起始ID
	 * @throws DataAccessException ：出錯時拋出DataAccessException
	 * @return void
	 */
	public void saveRepositoryHist(String assetId, String historyId) throws DataAccessException;
	
	/**
	 * Purpose:刷卡機裝機數報表
	 * @author CarrieDuan
	 * @param status: 設備狀態
	 * @param assetCategory: 設備類型
	 * @param orderBy:查詢結果依客戶簡稱/維護組別升冪排序
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return List<CrossTabReportDTO>:刷卡機裝機數交叉報表資料
	 */
	public List<CrossTabReportDTO> edcInstallNumReport(String status,String assetCategory, String orderBy) throws DataAccessException;
	/**
	 * Purpose:根據設備類別和設備名稱查詢選取EDC設備
	 * @author HermanWang
	 * @param itemId:設備名稱
	 * @param itemCategory：設備類別
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<SrmCaseAssetLinkDTO>
	 */
	public List<DmmRepositoryDTO> listByAsset(String chooseEdcAssetId, String chooseEdcAssetCategory, 
			String chooseEdcSerialNumber, String chooseEdcCarrierOrBorrower, String chooseEdcCarrierOrBorrowerComment,
			String chooseEdcWarehouseId, String chooseEdcDispatchProcessUser, String chooseEdcCustomerId, 
			Integer pageSize, Integer page, String order, String sort, List<String> selectSnsList) throws DataAccessException;
	/**
	 * Purpose:保存庫存歷史檔與故障組件故障現象歷史檔(簽收和線上排除設備鏈接)
	 * @author HermanWang
	 * @param assetId :庫存ID
	 * @param historyId : 庫存歷史起始ID
	 * @param status : 庫存狀態
	 * @throws DataAccessException ：出錯時拋出DataAccessException
	 * @return void
	 */
	public void saveRepositoryHist(String assetId, String historyId, String status) throws DataAccessException;
	
	/**
	 * Purpose:判斷當前DB庫存數量
	 * @author CrissZhang
	 * @throws DataAccessException:出錯拋出DataAccessException
	 * @return boolean:是否存在數據的boolean值
	 */
	public Integer countData() throws DataAccessException;
	/**
	 * Purpose:設備資料批次異動使用
	 * @author CarrieDuan
	 * @param assetId：設備ID
	 * @throws DataAccessException:出錯拋出DataAccessException
	 * @return List<DmmRepositoryDTO>
	 */
	public List<DmmRepositoryDTO> listByAssetId(List<String> assetId) throws DataAccessException;
	/**
	 * Purpose: 核檢使用人是否等於台新
	 * @author CarrieDuan
	 * @param serialNumbers：需要進行核檢的設備序號
	 * @throws DataAccessException:出錯拋出DataAccessException
	 * @return List<DmmRepositoryDTO>
	 */
	/*public List<DmmRepositoryDTO> checkAssetUserIsTaixinRent(List<String> serialNumbers) throws DataAccessException;*/
	
	/**
	 * Purpose:刪除所有轉入數據
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return void : 無返回
	 */
	public void deleteTransferData() throws DataAccessException;
	/**
	 * Purpose:存儲過程保存
	 * @param action
	 * @param assetIds
	 * @param transactionId
	 * @param historyId
	 * @param userId
	 * @param userName
	 * @param maintainCompany
	 * @param analyzeDate
	 * @param caseCompletionDate
	 * @param maintainUser
	 * @param description
	 * @param nowTime
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return Object[] 
	 */
	public Object[] saveRepositoryByProcedure(String action, String assetIds, String transactionId, String historyId,
			String userId, String userName, String maintainCompany, java.util.Date analyzeDate, Timestamp caseCompletionDate,
			String maintainUser, String description,String flag, Timestamp nowTime, String nowStatus, String faultComponent,
			String repairVendor, String faultDescription, String carrier, Date carryDate, String borrower,
			Date borrowerEnd, String borrowerMgrEmail, String borrowerEmail, Date borrowerStart, Date enableDate, String isEnabled,
			String dtid, String caseId, String merchantId, String merchantHeaderId, String installedAdress,
			String installedAdressLocation, String IsCup, String retireReasonCode) throws DataAccessException;
	/**
	 * Purpose:查詢設備主鍵
	 * @param assetManageFormDTO
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<DmmRepositoryDTO>
	 */
	public List<String> getAssetIdList(AssetManageFormDTO assetManageFormDTO) throws DataAccessException ;
	/**
	 * Purpose:存儲過程驗證設備
	 * @param action：執行作業
	 * @param assetIds：設備id
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return Object[] db驗證返回數組
	 */
	public Object[] assetValidateByProcedure(String action, String assetIds) throws DataAccessException;
	/**
	 * Purpose:列印借用單壓縮修改庫存資訊，新增庫存歷史檔
	 * @param assetIds
	 * @param nowTime
	 * @param userId
	 * @param userName
	 * @param historyId
	 * @throws DataAccessException
	 */
	public void saveRepositoryByBorrowZip(String assetIds, Timestamp nowTime, String userId, String userName, String historyId) throws DataAccessException;
	/**
	 * Purpose:轉倉匯入核檢設備序號是否在此倉庫下
	 * @author CarrieDuan
	 * @param serialNumber
	 * @param fromWarehouseId
	 * @throws DataAccessException
	 * @return DmmRepositoryDTO
	 */
	public DmmRepositoryDTO checkAssetIsInWarehouse(String serialNumber, String fromWarehouseId) throws DataAccessException;
	
	/**
	 * Purpose:
	 * @author CarrieDuan
	 * @param assetTransId :轉倉批號ID
	 * @param status ：設備狀態
	 * @param isTransDone ：是否轉倉
	 * @param historyId : 庫存歷史起始ID
	 * @param logonUser : 當前登錄者信息
	 * @param transAction :動作
	 * @throws DataAccessException ：出錯時跑出DataAccessException
	 * @return void
	 */
	public void confirmInStorage(String assetTransId, String historyId, LogonUser logonUser, String transferAction, String serialNumbers, String transferSuccess, String cancelSuccess, String endFlag, String cancelAction) throws DataAccessException;
	/**
	 * Purpose: 設備資料批次異動修改臺新租賃維護與捷達威維護數據
	 * @author CarrieDuan
	 * @param serialNumber：設備序號
	 * @param nowTime：系統當前時間
	 * @param userId：當前登陸者
	 * @param userName：當前登陸者名稱
	 * @param description：
	 * @param hId：歷程表ＩＤ
	 * @param actionId: 執行作業
	 * @param nowStatus: 
	 * @param enableDate: 使用日期
	 * @param isEnabled：是否已啓用
	 * @param dtid：dtid
	 * @param caseId: 案件編號
	 * @param merchantId：特店代號
	 * @param merchantHeaderId：特店表頭ＩＤ
	 * @param installedAdress：裝機地址
	 * @param installedAdressLocation：裝機地址－市縣
	 * @param isCup：是否銀聯
	 * @param maintainCompany：維護廠商
	 * @param analyzeDate：簽收日期
	 * @param caseCompletionDate：案件完修日
	 * @param maintainUser：維護工程師
	 * @param jdwFlag：捷達威維護flag
	 * @param updateTableFlag: 1：代表修改DMM_REPOSITORY，2：代表修改DMM_REPOSITORY_HISTORY_MONTHLY
	 * @param selectMonyhYear: 當查詢DMM_REPOSITORY_HISTORY_MONTHLY時，需要查詢的月份
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return Object[]
	 */
	public Object[] repositoryAssetManageTSBAndJdw (String serialNumber, Timestamp nowTime, String userId, String userName, String description,
			String hId, String actionId, String nowStatus, Timestamp enableDate, String isEnabled, String dtid, String caseId, String merchantId,
			String merchantHeaderId, String installedAdress, String installedAdressLocation, String isCup, String maintainCompany, Date analyzeDate,
			Timestamp caseCompletionDate, String maintainUser, String jdwFlag, String updateTableFlag, String selectMonyhYear
			) throws DataAccessException;
	/**
	 * Purpose:由hql語句查詢dmo
	 * @author amandawang
	 * @param serialNumber：設備序號
	 * @throws DataAccessException
	 * @return DmmRepository
	 */
	public DmmRepository getRepositoryByHql(String serialNumber) throws DataAccessException;
	
	/**
	 * Purpose:根據案件編號更新設備庫存資料設備啟用日以及案件完修日期
	 * @author CrissZhang
	 * @param caseId : 案件編號
	 * @param completeDate : 修改後完修日期
	 * @param isHistory : 是否為歷史
	 * @throws DataAccessException : 出錯時, 丟出DataAccessException
	 * @return void : 無返回
	 */
	public void updateRepositoryByCaseId(String caseId, Date completeDate, String isHistory) throws DataAccessException;
	
	/**
	 * Purpose:取得借用明細
	 * @author nicklin
	 * @throws DataAccessException : 出錯時, 丟出DataAccessException
	 * @return List<DmmRepositoryDTO>
	 */
	public List<DmmRepositoryDTO> getBorrowDetail() throws DataAccessException;
	
	/**
	 * Purpose:設備借用－核檢輸入的設備序號是否正確
	 * @author CarrieDuan
	 * @param assetTypeId:設備id
	 * @param serialNumber：設備序號
	 * @throws DataAccessException : 出錯時, 丟出DataAccessException
	 * @return List<DmmRepositoryDTO>
	 */
	public List<DmmRepositoryDTO> checkBorrowSerialNumber(String assetTypeId, String serialNumber) throws DataAccessException;
}
