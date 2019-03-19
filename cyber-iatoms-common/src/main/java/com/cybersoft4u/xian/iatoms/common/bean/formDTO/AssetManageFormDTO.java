package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.List;
import java.util.Map;

import cafe.core.bean.Parameter;
import cafe.core.bean.dto.AbstractSimpleListFormDTO;
import cafe.core.config.SystemConfigManager;

import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryHistoryDTO;
/**
 * Purpose: 設備管理作業FormDTO
 * @author amandawang 
 * @since  JDK 1.6
 * @date   2016/7/26
 * @MaintenancePersonnel amandawang
 */
public class AssetManageFormDTO extends AbstractSimpleListFormDTO<DmmRepositoryDTO> {
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -2290078347830087368L;
	/**
	 * 廠商集合
	 */
	public static final String VENDOR_LIST								= "vendorList";
	/**
	 * 客戶集合
	 */
	public static final String CUSTOMER_LIST								= "customerList";
	/**
	 * 公司廠商集合
	 */
	public static final String CUSTOMER_AND_VENDOR_LIST				= "customerAndVendorList";
	/**
	 * 設備類型集合
	 */
	public static final String GET_ASSET_TYPE_LIST 						= "getAssetTypeList";
	/**
	 * 硬體廠商集合
	 */
	public static final String REPAIR_VENDOR_LIST						= "repairVendorList";
	/**
	 * 維護廠商集合
	 */
	public static final String REPAIRD_VENDOR_LIST						= "repairdVendorList";
	/**
	 * 公司集合
	 */
	public static final String COMPANY_LIST								= "companyList";
	/**
	 * 字段集合
	 */
	public static final String EXPORT_PARAM_FIELDS						= "exportField";
	/**
	 * 歷史匯出
	 */
	public static final String HISTORY_PARAM_EXPORT						="historyExport";
	/**
	 * 設備序號
	 */
	public static final String PARAM_PAGE_SORT 							= "serialNumber";
	/**
	 * 更新日期
	 */
	public static final String PARAM_PAGE_HIST_SORT 					= "updateDate";
	/**
	 * 設備庫存匯出
	 */
	public static final String EXPORT_JRXML_NAME_LIST					= "REPOSITORY_LIST_REPORT";
	/**
	 * 設備借用單
	 */
	public static final String DOWNLOAD_FILE_NAME						= "REPOSITORY_BORROW_LIST";
	/**
	 * 下載地址
	 */
	public static final String DOWNLOAD_FILE							= "/com/cybersoft4u/xian/iatoms/services/download";
	/**
	 * 設備管理作業
	 */
	public static final String REPORT_FILE_NAME							="設備管理作業";
	/**
	 * 設備借用申請單
	 */
	public static final String ZIP_FILE_NAME								="設備借用申請單";
	/**
	 * 壓縮格式後綴
	 */
	public static final String ZIP_EXT_NAME								=".zip";
	/**
	 * 邮件主题模板
	 */
	public static final String MAIL_EXAMPLE_SUBJECT_TEXT								="mailExampleSubjectText.txt";
	/**
	 * 邮件內容模板--未歸還
	 */
	public static final String ASSET_BORROW_UNBACK_MAIL								="assetborrowUnbackMail.html";
	/**
	 * 邮件內容模板--借用
	 */
	public static final String ASSET_BORROW_CONTEXT								="assetBorrowContext.html";

	/**
	 * 緩存地址
	 */
	public static final String WORD_FILE_PATH							=SystemConfigManager.getProperty("FILE_PATH", "FILE_TEMP_PATH");
	/**
	 * 緩存地址
	 */
	public static final String ZIP_FILE_PATH								=SystemConfigManager.getProperty("FILE_PATH", "FILE_TEMP_PATH");
	/**
	 * 領用
	 */
	public static final String ACTION_CARRY 								= "carry";
	/**
	 * 借用
	 */
	public static final String ACTION_BORROW 							= "borrow";
	/**
	 * 入庫
	 */
	public static final String ACTION_ASSET_IN 							= "assetIn";
	/**
	 * 退回
	 */
	public static final String ACTION_RETURN 							= "return";
	/**
	 * 維修
	 */
	public static final String ACTION_REPAIR 							= "repair";
	/**
	 * 送修
	 */
	public static final String ACTION_REPAIRED 							= "repaired";
	/**
	 * 待報廢
	 */
	public static final String ACTION_RETIRE 							= "retire";
	/**
	 * 報廢
	 */
	public static final String ACTION_RETIRED 							= "retired";
	/**
	 * 歸還
	 */
	public static final String ACTION_BACK								= "back";
	/**
	 * 銷毀
	 */
	public static final String ACTION_DELETE 							= "delete";
	/**
	 * 台新租賃
	 */
	public static final String ACTION_TAIXIN_RENT							= "taixinRent";
	/**
	 * 解除綁定
	 */
	public static final String ACTION_REMOVE							= "remove";
	/**
	 * 捷達威維護
	 */
	public static final String ACTION_JDW							= "JDWMaintenance";
	/**
	 * 選擇DTID的查詢條件
	 */
	public static final String QUERY_ASSET_TYPE 						= "queryAssetType";
	/**
	 * 
	 */
	public static final String QUERY_ASSET_NAME 						= "queryAssetName";
	/**
	 * 
	 */
	public static final String QUERY_PEOPLE				        		= "queryPeople";
	/**
	 * 
	 */
	public static final String QUERY_COMMET     						= "queryCommet";
	/**
	 * 
	 */
	public static final String QUERY_HOUSE      						= "queryHouse";
	/**
	 * 
	 */
	public static final String QUERY_NUMBER     						= "queryNumber";
	/**
	 * 
	 */
	public static final String QUERY_NHISTORY     						= "queryHistory";
	/**
	 * 
	 */
	public static final String QUERY_PARAM								= "queryParam";
	/**
	 * 查詢倉庫
	 */
	private String queryStorage;
	/**
	 * 查詢狀態
	 */
	private String queryStatus;
	/**
	 * 公司DTO
	 */
	private CompanyDTO companyDTO;
	/**
	 * 公司Id
	 */
	private String companyId;
	/**
	 * 設備類型
	 */
	private String assetTypeName;
	/**
	 * 倉庫名稱
	 */
	private String storage;
	/**
	 * 狀態
	 */
	private String status;
	/**
	 * 支援功能
	 */
	private String supportedFunction;
	/**
	 * 合約ID
	 */
	private String queryContractId;
	/**
	 * 特店登記名稱
	 */
	private String queryMerName; 
	/**
	 * 特店對外名稱
	 */
	private String queryHeaderName;
	/**
	 * 特店代號
	 */
	private String queryMerchantCode;
	/**
	 * 裝機地址
	 */
	private String queryMerInstallAddress;
	/**
	 * 區域
	 */
	private String queryArea;
	/**
	 * 維護模式
	 */
	private String queryMaType;
	/**
	 * 是否啟用
	 */
	private String queryIsEnabled;
	/**
	 * 持有者
	 */
	private String queryKeeperName;
	/**
	 * TID
	 */
	private String queryTid;
	/**
	 * 設備所有者
	 */
	private String queryAssetOwner;
	/**
	 * 設備使用者
	 */
	private String queryAssetUser;
	/**
	 * DTID
	 */
	private String queryDtid;
	/**
	 * 財產編號
	 */
	private String queryPropertyIds;
	/**
	 * 設備序號
	 */
	private String querySerialNumbers;
	/**
	 * 苦尊主檔ID
	 */
	private String queryAssetId;
	/**
	 * 庫存歷史ID
	 */
	private String queryHistId;
	/**
	 * 借用者
	 */
	private String queryBorrower;
	/**
	 * 編輯標誌
	 */
	private String editFlag; 
	/**
	 * 設備類別
	 */
	private String queryAssetCategory;
	/**
	 * 客戶
	 */
	private String queryUser;
	/**
	 * 是否詳情頁面歷史查詢
	 */
	private String queryHistory;
	/**
	 * 庫存歷史DTO集合
	 */
	private List<DmmRepositoryHistoryDTO> dmmRepositoryHistoryDTOs;
	/**
	 * 庫存歷史DTO
	 */
	private DmmRepositoryHistoryDTO dmmRepositoryHistoryDTO;
	/**
	 * 每頁顯示條數
	 */
	private Integer rows;
	/**
	 * 當前頁碼
	 */
	private Integer page;
	/**
	 * 排序方式
	 */
	private String sort;
	/**
	 * 排序字段
	 */
	private String order;
	/**
	 * 查詢結果總筆數
	 */
	private Integer totalSize;
	/**
	 * 庫存主檔DTO
	 */
	private DmmRepositoryDTO dmmRepositoryDTO;
	/**
	 * 公司集合
	 */
	private String beforeTicketCompletionDate;
	/**
	 * 公司集合
	 */
	private String afterTicketCompletionDate;
	/**
	 * 公司集合
	 */
	private String beforeUpdateDate; 
	/**
	 * 公司集合
	 */
	private String afterUpdateDate;
	/**
	 * 公司集合
	 */
	private String queryIsCup;
	/**
	 * 公司集合
	 */
	private String[] exportFields;
	/**
	 * 歷史匯出
	 */
	private String historyExport;
	/**
	 * 截图的名称
	 */
	private String imageName;
	/**
	 * ip
	 */
	private String ip;
	/**
	 * 查詢作業
	 */
	private String queryAction;
	/**
	 * 用戶id
	 */
	private String userId;

	/**
	 * 轉倉確認通知人員
	 */
	private String toMailId;
	/**
	 * 
	 */
	private String queryAssetIds;
	/**
	 * 
	 */
	private String queryAssetIdList;
	
	
	//選擇EDC的查詢條件
	/**
	 * 設備類型
	 */
	private String queryAssetType;
	/**
	 * 設備名稱
	 */
	private String queryAssetName;
	/**
	 * 借用人/領用人
	 */
	private String queryPeople;
	/**
	 * 借用/領用說明
	 */
	private String queryCommet;
	/**
	 * 倉庫名稱
	 */
	private String queryHouse;
	/**
	 * 設備序號
	 */
	private String queryNumber;
	/**
	 * 特店formDTO
	 */
	private MerchantFormDTO merchantFormDTO;
	/**
	 * 櫃位
	 */
	private String queryCounter;
	/**
	 * 箱號
	 */
	private String queryCartonNo;
	/**
	 * 是否爲廠商角色
	 */
	private boolean isVendorAttribute;
	/**
	 * 是否爲掃碼槍查詢
	 */
	private String codeGunFlag;
	/**
	 * 是否爲維護工程師查詢
	 */
	private String maintenanceUserFlag;
	/**
	 * 掃碼槍匯出設備序號
	 */
	private String exportCodeGunSerialNumbers;
	/**
	 * 掃碼槍匯出財產編號
	 */
	private String exportCodeGunPropertyIds;
	
	/**
	 * 是否批量操作
	 */
	private String queryAllSelected;
	/**
	 * 所有要匯出的欄位
	 */
	private Map<String, String> fieldMap;
	/**
	 * 接受mail人員
	 */
	private String toMail;
	/**
	 * mail發送匯出的欄位
	 */
	private String exportField;
	/**
	 * 查詢參數json
	 */
	private String queryParam;
	/**
	 * 部門下拉框集合
	 */
	private List<Parameter> departmentList;
	
	/**
	 * 是否查詢案件編號存在flag
	 */
	private String queryCaseFlag;
	
	/**
	 * 設備型號 Task #3127
	 */
	private String queryModel;
	
	/**
	 * 隱藏域使用人 Task #3046
	 */
	private String hideQueryAssetUser;
	/**
	 * 是否批次ftps功能
	 */
	private String queryFtpsFlag;
	/**
	 * Constructor:無參構造函數
	 */
	public AssetManageFormDTO() {
		super();
	}

	/**
	 * Constructor:有參構造函數
	 */
	public AssetManageFormDTO(String companyId, String assetTypeName,
			String storage, String status, String supportedFunction,
			String queryContractId, String queryMerName,
			String queryHeaderName, String queryMerchantCode, String queryMaType,
			String queryIsEnabled, String queryKeeperName, String queryTid,
			String queryAssetOwner, String queryAssetUser, String queryDtid,
			String queryPropertyIds, String querySerialNumbers,
			String queryAssetId, String queryHistId, String queryBorrower,
			String editFlag, List<DmmRepositoryHistoryDTO> dmmRepositoryHistoryDTOs,
			DmmRepositoryHistoryDTO dmmRepositoryHistoryDTO, Integer rows, Integer page,
			String sort, String order, Integer totalSize,
			DmmRepositoryDTO dmmRepositoryDTO,
			String beforeTicketCompletionDate,
			String afterTicketCompletionDate, String beforeUpdateDate,
			String afterUpdateDate, String queryIsCup, String imageName,
			String ip, String queryAction, String toMailId) {
		super();
		this.companyId = companyId;
		this.assetTypeName = assetTypeName;
		this.storage = storage;
		this.status = status;
		this.supportedFunction = supportedFunction;
		this.queryContractId = queryContractId;
		this.queryMerName = queryMerName;
		this.queryHeaderName = queryHeaderName;
		this.queryMerchantCode = queryMerchantCode;
		this.queryMaType = queryMaType;
		this.queryIsEnabled = queryIsEnabled;
		this.queryKeeperName = queryKeeperName;
		this.queryTid = queryTid;
		this.queryAssetOwner = queryAssetOwner;
		this.queryAssetUser = queryAssetUser;
		this.queryDtid = queryDtid;
		this.queryPropertyIds = queryPropertyIds;
		this.querySerialNumbers = querySerialNumbers;
		this.queryAssetId = queryAssetId;
		this.queryHistId = queryHistId;
		this.queryBorrower = queryBorrower;
		this.editFlag = editFlag;
		this.dmmRepositoryHistoryDTOs = dmmRepositoryHistoryDTOs;
		this.dmmRepositoryHistoryDTO = dmmRepositoryHistoryDTO;
		this.rows = rows;
		this.page = page;
		this.sort = sort;
		this.order = order;
		this.totalSize = totalSize;
		this.dmmRepositoryDTO = dmmRepositoryDTO;
		this.beforeTicketCompletionDate = beforeTicketCompletionDate;
		this.afterTicketCompletionDate = afterTicketCompletionDate;
		this.beforeUpdateDate = beforeUpdateDate;
		this.afterUpdateDate = afterUpdateDate;
		this.queryIsCup = queryIsCup;
		this.imageName = imageName;
		this.ip = ip;
		this.queryAction = queryAction;
		this.toMailId = toMailId;
	}
	
	/**
	 * Constructor:有參構造函數
	 */
	public AssetManageFormDTO(String queryStorage, String queryStatus,
			CompanyDTO companyDTO, String companyId, String assetTypeName,
			String storage, String status, String supportedFunction,
			String queryContractId, String queryMerName,
			String queryHeaderName, String queryMerchantCode,
			String queryMerInstallAddress, String queryArea,
			String queryMaType, String queryIsEnabled, String queryKeeperName,
			String queryTid, String queryAssetOwner, String queryAssetUser,
			String queryDtid, String queryPropertyIds,
			String querySerialNumbers, String queryAssetId, String queryHistId,
			String queryBorrower, String editFlag, String queryAssetCategory,
			String queryUser, String queryHistory,
			List<DmmRepositoryHistoryDTO> dmmRepositoryHistoryDTOs,
			DmmRepositoryHistoryDTO dmmRepositoryHistoryDTO, Integer rows,
			Integer page, String sort, String order, Integer totalSize,
			DmmRepositoryDTO dmmRepositoryDTO,
			String beforeTicketCompletionDate,
			String afterTicketCompletionDate, String beforeUpdateDate,
			String afterUpdateDate, String queryIsCup, String[] exportFields,
			String historyExport, String imageName, String ip,
			String queryAction, String userId, String toMailId,
			String queryAssetIds, String queryAssetIdList,
			String queryAssetType, String queryAssetName, String queryPeople,
			String queryCommet, String queryHouse, String queryNumber,
			MerchantFormDTO merchantFormDTO, String queryCounter,
			String queryCartonNo, boolean isVendorAttribute,
			String codeGunFlag, String maintenanceUserFlag,
			String exportCodeGunSerialNumbers, String exportCodeGunPropertyIds,
			String queryAllSelected, Map<String, String> fieldMap,
			String toMail, String exportField, String queryParam,
			List<Parameter> departmentList, String queryCaseFlag,
			String hideQueryAssetUser) {
		super();
		this.queryStorage = queryStorage;
		this.queryStatus = queryStatus;
		this.companyDTO = companyDTO;
		this.companyId = companyId;
		this.assetTypeName = assetTypeName;
		this.storage = storage;
		this.status = status;
		this.supportedFunction = supportedFunction;
		this.queryContractId = queryContractId;
		this.queryMerName = queryMerName;
		this.queryHeaderName = queryHeaderName;
		this.queryMerchantCode = queryMerchantCode;
		this.queryMerInstallAddress = queryMerInstallAddress;
		this.queryArea = queryArea;
		this.queryMaType = queryMaType;
		this.queryIsEnabled = queryIsEnabled;
		this.queryKeeperName = queryKeeperName;
		this.queryTid = queryTid;
		this.queryAssetOwner = queryAssetOwner;
		this.queryAssetUser = queryAssetUser;
		this.queryDtid = queryDtid;
		this.queryPropertyIds = queryPropertyIds;
		this.querySerialNumbers = querySerialNumbers;
		this.queryAssetId = queryAssetId;
		this.queryHistId = queryHistId;
		this.queryBorrower = queryBorrower;
		this.editFlag = editFlag;
		this.queryAssetCategory = queryAssetCategory;
		this.queryUser = queryUser;
		this.queryHistory = queryHistory;
		this.dmmRepositoryHistoryDTOs = dmmRepositoryHistoryDTOs;
		this.dmmRepositoryHistoryDTO = dmmRepositoryHistoryDTO;
		this.rows = rows;
		this.page = page;
		this.sort = sort;
		this.order = order;
		this.totalSize = totalSize;
		this.dmmRepositoryDTO = dmmRepositoryDTO;
		this.beforeTicketCompletionDate = beforeTicketCompletionDate;
		this.afterTicketCompletionDate = afterTicketCompletionDate;
		this.beforeUpdateDate = beforeUpdateDate;
		this.afterUpdateDate = afterUpdateDate;
		this.queryIsCup = queryIsCup;
		this.exportFields = exportFields;
		this.historyExport = historyExport;
		this.imageName = imageName;
		this.ip = ip;
		this.queryAction = queryAction;
		this.userId = userId;
		this.toMailId = toMailId;
		this.queryAssetIds = queryAssetIds;
		this.queryAssetIdList = queryAssetIdList;
		this.queryAssetType = queryAssetType;
		this.queryAssetName = queryAssetName;
		this.queryPeople = queryPeople;
		this.queryCommet = queryCommet;
		this.queryHouse = queryHouse;
		this.queryNumber = queryNumber;
		this.merchantFormDTO = merchantFormDTO;
		this.queryCounter = queryCounter;
		this.queryCartonNo = queryCartonNo;
		this.isVendorAttribute = isVendorAttribute;
		this.codeGunFlag = codeGunFlag;
		this.maintenanceUserFlag = maintenanceUserFlag;
		this.exportCodeGunSerialNumbers = exportCodeGunSerialNumbers;
		this.exportCodeGunPropertyIds = exportCodeGunPropertyIds;
		this.queryAllSelected = queryAllSelected;
		this.fieldMap = fieldMap;
		this.toMail = toMail;
		this.exportField = exportField;
		this.queryParam = queryParam;
		this.departmentList = departmentList;
		this.queryCaseFlag = queryCaseFlag;
		this.hideQueryAssetUser = hideQueryAssetUser;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	/**
	 * @return the companyId
	 */
	public String getCompanyId() {
		return companyId;
	}


	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	/**
	 * @return the assetTypeName
	 */
	public String getAssetTypeName() {
		return assetTypeName;
	}

	/**
	 * @param assetTypeName the assetTypeName to set
	 */
	public void setAssetTypeName(String assetTypeName) {
		this.assetTypeName = assetTypeName;
	}

	/**
	 * @return the storage
	 */
	public String getStorage() {
		return storage;
	}

	/**
	 * @param storage the storage to set
	 */
	public void setStorage(String storage) {
		this.storage = storage;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the supportedFunction
	 */
	public String getSupportedFunction() {
		return supportedFunction;
	}

	/**
	 * @param supportedFunction the supportedFunction to set
	 */
	public void setSupportedFunction(String supportedFunction) {
		this.supportedFunction = supportedFunction;
	}

	/**
	 * @return the queryContractId
	 */
	public String getQueryContractId() {
		return queryContractId;
	}

	/**
	 * @param queryContractId the queryContractId to set
	 */
	public void setQueryContractId(String queryContractId) {
		this.queryContractId = queryContractId;
	}

	/**
	 * @return the queryMid
	 */
	public String getQueryMerchantCode() {
		return queryMerchantCode;
	}

	/**
	 * @param queryMid the queryMid to set
	 */
	public void setQueryMerchantCode(String queryMerchantCode) {
		this.queryMerchantCode = queryMerchantCode;
	}

	/**
	 * @return the queryMaType
	 */
	public String getQueryMaType() {
		return queryMaType;
	}

	/**
	 * @param queryMaType the queryMaType to set
	 */
	public void setQueryMaType(String queryMaType) {
		this.queryMaType = queryMaType;
	}

	/**
	 * @return the queryIsEnabled
	 */
	public String getQueryIsEnabled() {
		return queryIsEnabled;
	}

	/**
	 * @param queryIsEnabled the queryIsEnabled to set
	 */
	public void setQueryIsEnabled(String queryIsEnabled) {
		this.queryIsEnabled = queryIsEnabled;
	}

	/**
	 * @return the queryKeeperName
	 */
	public String getQueryKeeperName() {
		return queryKeeperName;
	}

	/**
	 * @param queryKeeperName the queryKeeperName to set
	 */
	public void setQueryKeeperName(String queryKeeperName) {
		this.queryKeeperName = queryKeeperName;
	}

	/**
	 * @return the queryTid
	 */
	public String getQueryTid() {
		return queryTid;
	}

	/**
	 * @param queryTid the queryTid to set
	 */
	public void setQueryTid(String queryTid) {
		this.queryTid = queryTid;
	}

	/**
	 * @return the queryAssetOwner
	 */
	public String getQueryAssetOwner() {
		return queryAssetOwner;
	}

	/**
	 * @param queryAssetOwner the queryAssetOwner to set
	 */
	public void setQueryAssetOwner(String queryAssetOwner) {
		this.queryAssetOwner = queryAssetOwner;
	}

	/**
	 * @return the queryAssetUser
	 */
	public String getQueryAssetUser() {
		return queryAssetUser;
	}

	/**
	 * @param queryAssetUser the queryAssetUser to set
	 */
	public void setQueryAssetUser(String queryAssetUser) {
		this.queryAssetUser = queryAssetUser;
	}

	/**
	 * @return the queryDtid
	 */
	public String getQueryDtid() {
		return queryDtid;
	}

	/**
	 * @param queryDtid the queryDtid to set
	 */
	public void setQueryDtid(String queryDtid) {
		this.queryDtid = queryDtid;
	}

	/**
	 * @return the queryPropertyIds
	 */
	public String getQueryPropertyIds() {
		return queryPropertyIds;
	}

	/**
	 * @param queryPropertyIds the queryPropertyIds to set
	 */
	public void setQueryPropertyIds(String queryPropertyIds) {
		this.queryPropertyIds = queryPropertyIds;
	}

	/**
	 * @return the querySerialNumbers
	 */
	public String getQuerySerialNumbers() {
		return querySerialNumbers;
	}

	/**
	 * @param querySerialNumbers the querySerialNumbers to set
	 */
	public void setQuerySerialNumbers(String querySerialNumbers) {
		this.querySerialNumbers = querySerialNumbers;
	}

	/**
	 * @return the queryAssetId
	 */
	public String getQueryAssetId() {
		return queryAssetId;
	}

	/**
	 * @param queryAssetId the queryAssetId to set
	 */
	public void setQueryAssetId(String queryAssetId) {
		this.queryAssetId = queryAssetId;
	}

	/**
	 * @return the queryHistId
	 */
	public String getQueryHistId() {
		return queryHistId;
	}

	/**
	 * @param queryHistId the queryHistId to set
	 */
	public void setQueryHistId(String queryHistId) {
		this.queryHistId = queryHistId;
	}

	/**
	 * @return the queryBorrower
	 */
	public String getQueryBorrower() {
		return queryBorrower;
	}

	/**
	 * @param queryBorrower the queryBorrower to set
	 */
	public void setQueryBorrower(String queryBorrower) {
		this.queryBorrower = queryBorrower;
	}

	/**
	 * @return the editFlag
	 */
	public String getEditFlag() {
		return editFlag;
	}

	/**
	 * @param editFlag the editFlag to set
	 */
	public void setEditFlag(String editFlag) {
		this.editFlag = editFlag;
	}

	/**
	 * @return the dmmRepositoryHistoryDTOs
	 */
	public List<DmmRepositoryHistoryDTO> getRepositoryHistDTOs() {
		return dmmRepositoryHistoryDTOs;
	}

	/**
	 * @param dmmRepositoryHistoryDTOs the dmmRepositoryHistoryDTOs to set
	 */
	public void setRepositoryHistDTOs(List<DmmRepositoryHistoryDTO> dmmRepositoryHistoryDTOs) {
		this.dmmRepositoryHistoryDTOs = dmmRepositoryHistoryDTOs;
	}

	/**
	 * @return the dmmRepositoryHistoryDTO
	 */
	public DmmRepositoryHistoryDTO getRepositoryHistDTO() {
		return dmmRepositoryHistoryDTO;
	}

	/**
	 * @param dmmRepositoryHistoryDTO the dmmRepositoryHistoryDTO to set
	 */
	public void setRepositoryHistDTO(DmmRepositoryHistoryDTO dmmRepositoryHistoryDTO) {
		this.dmmRepositoryHistoryDTO = dmmRepositoryHistoryDTO;
	}

	/**
	 * @return the rows
	 */
	public Integer getRows() {
		return rows;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(Integer rows) {
		this.rows = rows;
	}

	/**
	 * @return the page
	 */
	public Integer getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(Integer page) {
		this.page = page;
	}

	/**
	 * @return the sort
	 */
	public String getSort() {
		return sort;
	}

	/**
	 * @param sort the sort to set
	 */
	public void setSort(String sort) {
		this.sort = sort;
	}

	/**
	 * @return the order
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(String order) {
		this.order = order;
	}

	/**
	 * @return the totalSize
	 */
	public Integer getTotalSize() {
		return totalSize;
	}

	/**
	 * @param totalSize the totalSize to set
	 */
	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
	}

	/**
	 * @return the beforeTicketCompletionDate
	 */
	public String getBeforeTicketCompletionDate() {
		return beforeTicketCompletionDate;
	}

	/**
	 * @param beforeTicketCompletionDate the beforeTicketCompletionDate to set
	 */
	public void setBeforeTicketCompletionDate(String beforeTicketCompletionDate) {
		this.beforeTicketCompletionDate = beforeTicketCompletionDate;
	}

	/**
	 * @return the afterTicketCompletionDate
	 */
	public String getAfterTicketCompletionDate() {
		return afterTicketCompletionDate;
	}

	/**
	 * @param afterTicketCompletionDate the afterTicketCompletionDate to set
	 */
	public void setAfterTicketCompletionDate(String afterTicketCompletionDate) {
		this.afterTicketCompletionDate = afterTicketCompletionDate;
	}

	/**
	 * @return the beforeUpdateDate
	 */
	public String getBeforeUpdateDate() {
		return beforeUpdateDate;
	}

	/**
	 * @param beforeUpdateDate the beforeUpdateDate to set
	 */
	public void setBeforeUpdateDate(String beforeUpdateDate) {
		this.beforeUpdateDate = beforeUpdateDate;
	}

	/**
	 * @return the afterUpdateDate
	 */
	public String getAfterUpdateDate() {
		return afterUpdateDate;
	}

	/**
	 * @param afterUpdateDate the afterUpdateDate to set
	 */
	public void setAfterUpdateDate(String afterUpdateDate) {
		this.afterUpdateDate = afterUpdateDate;
	}

	/**
	 * @return the queryIsCup
	 */
	public String getQueryIsCup() {
		return queryIsCup;
	}

	/**
	 * @param queryIsCup the queryIsCup to set
	 */
	public void setQueryIsCup(String queryIsCup) {
		this.queryIsCup = queryIsCup;
	}

	/**
	 * @return the toMailId
	 */
	public String getToMailId() {
		return toMailId;
	}

	/**
	 * @param toMailId the toMailId to set
	 */
	public void setToMailId(String toMailId) {
		this.toMailId = toMailId;
	}

	/**
	 * @return the imageName
	 */
	public String getImageName() {
		return imageName;
	}

	/**
	 * @param imageName the imageName to set
	 */
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	/**
	 * @return the queryAction
	 */
	public String getQueryAction() {
		return queryAction;
	}

	/**
	 * @param queryAction the queryAction to set
	 */
	public void setQueryAction(String queryAction) {
		this.queryAction = queryAction;
	}

	/**
	 * @return the queryMerName
	 */
	public String getQueryMerName() {
		return queryMerName;
	}

	/**
	 * @param queryMerName the queryMerName to set
	 */
	public void setQueryMerName(String queryMerName) {
		this.queryMerName = queryMerName;
	}

	/**
	 * @return the queryHeaderName
	 */
	public String getQueryHeaderName() {
		return queryHeaderName;
	}

	/**
	 * @param queryHeaderName the queryHeaderName to set
	 */
	public void setQueryHeaderName(String queryHeaderName) {
		this.queryHeaderName = queryHeaderName;
	}

	/**
	 * @return the dmmRepositoryDTO
	 */
	public DmmRepositoryDTO getDmmRepositoryDTO() {
		return dmmRepositoryDTO;
	}

	/**
	 * @param dmmRepositoryDTO the dmmRepositoryDTO to set
	 */
	public void setDmmRepositoryDTO(DmmRepositoryDTO dmmRepositoryDTO) {
		this.dmmRepositoryDTO = dmmRepositoryDTO;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the queryAssetIds
	 */
	public String getQueryAssetIds() {
		return queryAssetIds;
	}

	/**
	 * @param queryAssetIds the queryAssetIds to set
	 */
	public void setQueryAssetIds(String queryAssetIds) {
		this.queryAssetIds = queryAssetIds;
	}

	/**
	 * @return the queryAssetIdList
	 */
	public String getQueryAssetIdList() {
		return queryAssetIdList;
	}

	/**
	 * @param queryAssetIdList the queryAssetIdList to set
	 */
	public void setQueryAssetIdList(String queryAssetIdList) {
		this.queryAssetIdList = queryAssetIdList;
	}

	/**
	 * @return the exportFields
	 */
	public String[] getExportFields() {
		return exportFields;
	}

	/**
	 * @param exportFields the exportFields to set
	 */
	public void setExportFields(String[] exportFields) {
		this.exportFields = exportFields;
	}

	/**
	 * @return the historyExport
	 */
	public String getHistoryExport() {
		return historyExport;
	}

	/**
	 * @param historyExport the historyExport to set
	 */
	public void setHistoryExport(String historyExport) {
		this.historyExport = historyExport;
	}

	/**
	 * @return the queryMerInstallAddress
	 */
	public String getQueryMerInstallAddress() {
		return queryMerInstallAddress;
	}

	/**
	 * @param queryMerInstallAddress the queryMerInstallAddress to set
	 */
	public void setQueryMerInstallAddress(String queryMerInstallAddress) {
		this.queryMerInstallAddress = queryMerInstallAddress;
	}

	/**
	 * @return the queryArea
	 */
	public String getQueryArea() {
		return queryArea;
	}

	/**
	 * @param queryArea the queryArea to set
	 */
	public void setQueryArea(String queryArea) {
		this.queryArea = queryArea;
	}

	/**
	 * @return the queryAssetCategory
	 */
	public String getQueryAssetCategory() {
		return queryAssetCategory;
	}

	/**
	 * @param queryAssetCategory the queryAssetCategory to set
	 */
	public void setQueryAssetCategory(String queryAssetCategory) {
		this.queryAssetCategory = queryAssetCategory;
	}

	/**
	 * @return the queryUser
	 */
	public String getQueryUser() {
		return queryUser;
	}

	/**
	 * @param queryUser the queryUser to set
	 */
	public void setQueryUser(String queryUser) {
		this.queryUser = queryUser;
	}
	
	/**
	 * @return the queryAssetType
	 */
	public String getQueryAssetType() {
		return queryAssetType;
	}

	/**
	 * @param queryAssetType the queryAssetType to set
	 */
	public void setQueryAssetType(String queryAssetType) {
		this.queryAssetType = queryAssetType;
	}

	/**
	 * @return the queryAssetName
	 */
	public String getQueryAssetName() {
		return queryAssetName;
	}

	/**
	 * @param queryAssetName the queryAssetName to set
	 */
	public void setQueryAssetName(String queryAssetName) {
		this.queryAssetName = queryAssetName;
	}

	/**
	 * @return the queryPeople
	 */
	public String getQueryPeople() {
		return queryPeople;
	}

	/**
	 * @param queryPeople the queryPeople to set
	 */
	public void setQueryPeople(String queryPeople) {
		this.queryPeople = queryPeople;
	}

	/**
	 * @return the queryCommet
	 */
	public String getQueryCommet() {
		return queryCommet;
	}

	/**
	 * @param queryCommet the queryCommet to set
	 */
	public void setQueryCommet(String queryCommet) {
		this.queryCommet = queryCommet;
	}

	/**
	 * @return the queryHouse
	 */
	public String getQueryHouse() {
		return queryHouse;
	}

	/**
	 * @param queryHouse the queryHouse to set
	 */
	public void setQueryHouse(String queryHouse) {
		this.queryHouse = queryHouse;
	}

	/**
	 * @return the queryNumber
	 */
	public String getQueryNumber() {
		return queryNumber;
	}

	/**
	 * @param queryNumber the queryNumber to set
	 */
	public void setQueryNumber(String queryNumber) {
		this.queryNumber = queryNumber;
	}

	/**
	 * @return the companyDTO
	 */
	public CompanyDTO getCompanyDTO() {
		return companyDTO;
	}

	/**
	 * @param companyDTO the companyDTO to set
	 */
	public void setCompanyDTO(CompanyDTO companyDTO) {
		this.companyDTO = companyDTO;
	}

	/**
	 * @return the merchantFormDTO
	 */
	public MerchantFormDTO getMerchantFormDTO() {
		return merchantFormDTO;
	}

	/**
	 * @param merchantFormDTO the merchantFormDTO to set
	 */
	public void setMerchantFormDTO(MerchantFormDTO merchantFormDTO) {
		this.merchantFormDTO = merchantFormDTO;
	}

	/**
	 * @return the queryCounter
	 */
	public String getQueryCounter() {
		return queryCounter;
	}

	/**
	 * @param queryCounter the queryCounter to set
	 */
	public void setQueryCounter(String queryCounter) {
		this.queryCounter = queryCounter;
	}

	/**
	 * @return the queryCartonNo
	 */
	public String getQueryCartonNo() {
		return queryCartonNo;
	}

	/**
	 * @param queryCartonNo the queryCartonNo to set
	 */
	public void setQueryCartonNo(String queryCartonNo) {
		this.queryCartonNo = queryCartonNo;
	}

	/**
	 * @return the isVendorAttribute
	 */
	public boolean getIsVendorAttribute() {
		return isVendorAttribute;
	}

	/**
	 * @param isVendorAttribute the isVendorAttribute to set
	 */
	public void setIsVendorAttribute(boolean isVendorAttribute) {
		this.isVendorAttribute = isVendorAttribute;
	}

	/**
	 * @return the queryHistory
	 */
	public String getQueryHistory() {
		return queryHistory;
	}

	/**
	 * @param queryHistory the queryHistory to set
	 */
	public void setQueryHistory(String queryHistory) {
		this.queryHistory = queryHistory;
	}

	/**
	 * @return the codeGunFlag
	 */
	public String getCodeGunFlag() {
		return codeGunFlag;
	}

	/**
	 * @param codeGunFlag the codeGunFlag to set
	 */
	public void setCodeGunFlag(String codeGunFlag) {
		this.codeGunFlag = codeGunFlag;
	}

	/**
	 * @return the maintenanceUserFlag
	 */
	public String getMaintenanceUserFlag() {
		return maintenanceUserFlag;
	}

	/**
	 * @param maintenanceUserFlag the maintenanceUserFlag to set
	 */
	public void setMaintenanceUserFlag(String maintenanceUserFlag) {
		this.maintenanceUserFlag = maintenanceUserFlag;
	}

	/**
	 * @return the exportCodeGunSerialNumbers
	 */
	public String getExportCodeGunSerialNumbers() {
		return exportCodeGunSerialNumbers;
	}

	/**
	 * @param exportCodeGunSerialNumbers the exportCodeGunSerialNumbers to set
	 */
	public void setExportCodeGunSerialNumbers(String exportCodeGunSerialNumbers) {
		this.exportCodeGunSerialNumbers = exportCodeGunSerialNumbers;
	}

	/**
	 * @return the exportCodeGunPropertyIds
	 */
	public String getExportCodeGunPropertyIds() {
		return exportCodeGunPropertyIds;
	}

	/**
	 * @param exportCodeGunPropertyIds the exportCodeGunPropertyIds to set
	 */
	public void setExportCodeGunPropertyIds(String exportCodeGunPropertyIds) {
		this.exportCodeGunPropertyIds = exportCodeGunPropertyIds;
	}

	/**
	 * @return the queryStorage
	 */
	public String getQueryStorage() {
		return queryStorage;
	}

	/**
	 * @param queryStorage the queryStorage to set
	 */
	public void setQueryStorage(String queryStorage) {
		this.queryStorage = queryStorage;
	}

	/**
	 * @return the queryStatus
	 */
	public String getQueryStatus() {
		return queryStatus;
	}

	/**
	 * @param queryStatus the queryStatus to set
	 */
	public void setQueryStatus(String queryStatus) {
		this.queryStatus = queryStatus;
	}

	/**
	 * @return the queryAllSelected
	 */
	public String getQueryAllSelected() {
		return queryAllSelected;
	}

	/**
	 * @param queryAllSelected the queryAllSelected to set
	 */
	public void setQueryAllSelected(String queryAllSelected) {
		this.queryAllSelected = queryAllSelected;
	}

	/**
	 * @return the fieldMap
	 */
	public Map<String, String> getFieldMap() {
		return fieldMap;
	}

	/**
	 * @param fieldMap the fieldMap to set
	 */
	public void setFieldMap(Map<String, String> fieldMap) {
		this.fieldMap = fieldMap;
	}

	/**
	 * @return the exportField
	 */
	public String getExportField() {
		return exportField;
	}

	/**
	 * @param exportField the exportField to set
	 */
	public void setExportField(String exportField) {
		this.exportField = exportField;
	}

	/**
	 * @return the toMail
	 */
	public String getToMail() {
		return toMail;
	}

	/**
	 * @param toMail the toMail to set
	 */
	public void setToMail(String toMail) {
		this.toMail = toMail;
	}

	/**
	 * @return the queryCaseFlag
	 */
	public String getQueryCaseFlag() {
		return queryCaseFlag;
	}

	/**
	 * @param queryCaseFlag the queryCaseFlag to set
	 */
	public void setQueryCaseFlag(String queryCaseFlag) {
		this.queryCaseFlag = queryCaseFlag;
	}

	/**
	 * @return the queryParam
	 */
	public String getQueryParam() {
		return queryParam;
	}

	/**
	 * @param queryParam the queryParam to set
	 */
	public void setQueryParam(String queryParam) {
		this.queryParam = queryParam;
	}

	/**
	 * @return the departmentList
	 */
	public List<Parameter> getDepartmentList() {
		return departmentList;
	}

	/**
	 * @param departmentList the departmentList to set
	 */
	public void setDepartmentList(List<Parameter> departmentList) {
		this.departmentList = departmentList;
	}

	/**
	 * @return the hideQueryAssetUser
	 */
	public String getHideQueryAssetUser() {
		return hideQueryAssetUser;
	}

	/**
	 * @param hideQueryAssetUser the hideQueryAssetUser to set
	 */
	public void setHideQueryAssetUser(String hideQueryAssetUser) {
		this.hideQueryAssetUser = hideQueryAssetUser;
	}

	/**
	 * @return the queryModel
	 */
	public String getQueryModel() {
		return queryModel;
	}

	/**
	 * @param queryModel the queryModel to set
	 */
	public void setQueryModel(String queryModel) {
		this.queryModel = queryModel;
	}

	/**
	 * @return the queryFtpsFlag
	 */
	public String getQueryFtpsFlag() {
		return queryFtpsFlag;
	}

	/**
	 * @param queryFtpsFlag the queryFtpsFlag to set
	 */
	public void setQueryFtpsFlag(String queryFtpsFlag) {
		this.queryFtpsFlag = queryFtpsFlag;
	}
	
}
