package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.cybersoft4u.xian.iatoms.common.bean.dto.ApiAuthorizationInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.IAtomsCaseFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAssetLinkDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAttFileDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTransactionDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTransactionParameterDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmTransactionParameterItemDTO;

/**
 * Purpose: 案件處理FormDTO
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016年8月5日
 * @MaintenancePersonnel CrissZhang
 */
public class CaseManagerFormDTO extends IAtomsCaseFormDTO {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -6034589844225196290L;
	/**
	 * 默認排序字段
	 */
	public static final String PARAM_PAGE_SORT 	= "CASE_ID";
	/**
	 * 查詢條件：客戶編號
	 */
	public static final String QUERY_CUSTOMER_ID						= "queryCustomerId";
	
	/**
	 * 查詢條件：特店代號
	 */
	public static final String QUERY_MERCHANT_CODE						= "queryMerCode";
	/**
	 * 查詢條件：DTID
	 */
	public static final String QUERY_DTID								= "queryDtid";
	
	/**
	 * 查詢條件：TID
	 */
	public static final String QUERY_TID				        		= "queryTID";
	
	/**
	 * 查詢條件：特點名稱
	 */
	public static final String QUERY_MERCHANT_NAME						= "queryMerchantName";
	
	/**
	 * 查詢條件：edc
	 */
	public static final String QUERY_EDC_NUMBER 						= "queryEDCNumber";
	
	/**
	 * 查詢條件：表頭名稱
	 */
	public static final String QUERY_HEADER_NAME						= "queryHeaderName";
	
	/**
	 * 查詢條件：地址
	 */
	public static final String QUERY_ADDRESS    						= "queryAddress";
	
	/**
	 * 通知類型
	 */
	public static final String NOTICE_TYPE    							= "noticeType";
	
	/**
	 * 轉移月份
	 */
	public static final String PARAM_TRANSFER_MONTH											= "transferMonth";
	
	/**
	 * 應回應日期
	 */
	public static final String PARAM_ACCEPTABLE_RESPONSE_DATE							= "acceptableResponseDate";
	/**
	 * 應到場日期
	 */
	public static final String PARAM_ACCEPTABLE_ARRIVE_DATE								= "acceptableArriveDate";
	/**
	 * 應完修日期
	 */
	public static final String PARAM_ACCEPTABLE_FINISH_DATE								= "acceptableFinishDate";
	/**
	 * 應XX日期
	 */
	public static final String PARAM_ACCEPTABLE_DATE									= "acceptableDate";
	/**
	 * XX警示開始日期
	 */
	public static final String PARAM_WARNNING_DATE										= "warnningDate";
	/**
	 * 案件處理畫面的控件前綴
	 */
	public static final String PARAM_CASE_SUFFIX                        = "case";
	/**
	 * Mail主题模板
	 */
	public static final String SUBJECT_TEMPLATE 										= "caseManagerMailSubject.txt";
	/**
	 * 會議通知主题模板
	 */
	public static final String MEETING_NOTICE_SUBJECT_TEMPLATE 							= "meetingNoticeContext.text";
	/**
	 * Mail内容模板
	 */
	public static final String TEXT_TEMPLATE 											= "caseManagerMailContext.html";
	/**
	 * Mail内容模板
	 */
	public static final String MEETING_NOTICE_TEXT_TEMPLATE 							= "caseManagerMeetingNoticeContext.text";
	/**
	 * 會議通知主题模板(完修、到場、回應即將逾期)
	 */
	public static final String MEETING_NOTICE_SUBJECT_OVERDUE_TEMPLATE 					= "meetingNoticeOverdueContext.text";
	/**
	 * Mail内容模板
	 */
	public static final String MEETING_NOTICE_TEXT_OVERDUE_TEMPLATE 					= "caseManagerMeetingNoticeOverdueContext.text";
	/**
	 * 案件管理通知邮件主题
	 */
	public static final String CASE_MANAGER_MAIL_SUBKECT								= "IATOMS-EMAIL-SUBKECT-SRM05020-E0001";
	/**
	 * 案件管理通知邮件內容
	 */
	public static final String CASE_MANAGER_MAIL_CONTEXT								= "IATOMS-EMAIL-CONTEXT-SRM05020-E0001";
	/**
	 * 案件管理通知邮件內容
	 */
	public static final String PARAM_SUPPORTED_FUNCTION_LIST_STR						= "supportedFunctionListStr";
	/**
	 * 部門集合
	 */
	public static final String PARAM_DEPT_LIST											= "deptList";
	
	/**
	 * 案件類型
	 */
	public static final String PARAM_TICKET_MODE											= "ticketModes";
	/**
	 * processId
	 */
	public static final String IATOMS_BP_CASE_PROCESS									= "IATOMS_BP_CASE_PROCESS";
	/**
	 * 案件類型類別
	 */
	public static final String SICK_CASE_TYPE											= "T";
	
	/**
	 * 裝機模板名稱-中文
	 */
	public static final String PARAM_CASE_IMPORT_TEMPLATE_INSTALL_CH					= "IATOMS-EMAIL-CONTEXT-SRM05020-I0001";
	/**
	 * 拼機模板名稱-中文
	 */
	public static final String PARAM_CASE_IMPORT_TEMPLATE_MERGE_CH						= "IATOMS-EMAIL-CONTEXT-SRM05020-I0002";
	/**
	 * 異動模板名稱-中文
	 */
	public static final String PARAM_CASE_IMPORT_TEMPLATE_UPDATE_CH						= "IATOMS-EMAIL-CONTEXT-SRM05020-I0003";
	/**
	 * 查核模板名稱-中文
	 */
	public static final String PARAM_CASE_IMPORT_TEMPLATE_CHECK_CH						= "IATOMS-EMAIL-CONTEXT-SRM05020-I0004";
	/**
	 * 拆機模板名稱-中文
	 */
	public static final String PARAM_CASE_IMPORT_TEMPLATE_UNINSTALL_CH					= "IATOMS-EMAIL-CONTEXT-SRM05020-I0005";
	/**
	 * 報修模板名稱-中文
	 */
	public static final String PARAM_CASE_IMPORT_TEMPLATE_REPAIR_CH						= "IATOMS-EMAIL-CONTEXT-SRM05020-I0006";
	/**
	 * 專案模板名稱-中文
	 */
	public static final String PARAM_CASE_IMPORT_TEMPLATE_PROJECT_CH					= "IATOMS-EMAIL-CONTEXT-SRM05020-I0007";
	/**
	 * 錯誤文件名稱
	 */
	public static final String ERROR_FILE_NAME											= "errorFileName";
	/**
	 * 錯誤文件路徑
	 */
	public static final String ERROR_FILE_PATH											= "errorFilePath";
	/**
	 * 主報表名稱
	 */
	public static final String PROJECT_REPORT_JRXML_NAME								= "CARD_READER_PARAMETER_REPORT";
	
	/**
	 * 子報表名稱
	 */
	public static final String PROJECT_SUB_REPORT_JRXML_NAME							= "CARD_READER_PARAMETER_REPORT_subreport1";
	
	/**
	 * 子報表定義
	 */
	public static final String SUBREPORT_DIR											= "SUBREPORT_DIR";
	
	/**
	 * 匯出報表名稱
	 */
	public static final String PROJECT_REPORT_FILE_NAME									= "jrxml.name.REPORT_NAME_CARD_READER";
	/**
	 * 交易項目
	 */
	public static final String TRANSACTION_ITEM											= "TRANSACTION_ITEM";
	/**
	 * 匯出字段集合
	 */
	public static final String EXPORT_PARAM_FIELDS										= "exportField";
	/**
	 * 匯出記錄查詢條件公司
	 */
	public static final String EXPORT_QUERY_COMPANYID										= "exportQueryCompanyId";
	/**
	 * 案件處理範本
	 */
	public static final String CASE_TEMPLATE 											= "CASE_TEMPLATE";
	/**
	 * 上傳文件參數uuid
	 */
	public static final String PARAM_CASE_FILE_UUID 									= "newUuid";
	/**
	 * 案件匯出下載模板英文名称
	 */
	public static final String CASE_EXPORT_TEMPLATE_NAME_FOR_EN							= "caseExportTemplate.xls";
	/**
	 * 案件匯出下載模板英文名称
	 */
	public static final String CASE_EXPORT_TEMPLATE_NAME_FOR_CN							= "案件資訊.xls";
	/**
	 * 案件時效常量
	 */
	public static final String CASE_PARAM_CALCULATE_DATE								= "calculateDate";
	/**
	 * 案件天數常量
	 */
	public static final String CASE_PARAM_TOTAL_ADD_DAYS								= "totalAddDays";
	/**
	 * 普通保修原因下拉列表
	 */
	public static final String REPAIR_REASON_LIST										= "repairReasonList";
	/**
	 * 台新報修原因下拉列表
	 */
	public static final String REPAIR_REASON_TAIXIN_LIST								= "repairReasonTaiXins";
	public static final String CUSTOMER_IS_TSB_EDC										= "isTsbEdc";
	
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
	 * 查詢條件：客戶
	 */
	private String queryCompanyId;
	/**
	 * 客戶id
	 */
	private String customerId;
	/**
	 * 查詢條件：需求單號
	 */
	private String queryRequirementNo;
	/**
	 * 查詢條件：案件編號
	 */
	private String queryCaseId;
	/**
	 * 查詢條件：案件編號
	 */
	private String queryCaseCategory;
	/**
	 * 查詢條件：刷卡機機型
	 */
	private String queryEdcType;
	/**
	 * 查詢條件：維護廠商
	 */
	private String queryVendorId;
	/**
	 * 查詢條件：維護部門
	 */
	private String queryVendorDept;
	/**
	 * 查詢條件：進件日期 起
	 */
	private Date queryCreateDateStart;
	/**
	 * 查詢條件：進件日期 迄
	 */
	private Date queryCreateDateEnd;
	/**
	 * 查詢條件：應完成日期 起
	 */
	private Date queryAcceptableCompleteDateStart;
	/**
	 * 查詢條件：應完成日期 迄
	 */
	private Date queryAcceptableCompleteDateEnd;
	/**
	 * 查詢條件：完修日期 起
	 */
	private Date queryCompleteDateStart;
	/**
	 * 查詢條件：完修日期 迄
	 */
	private Date queryCompleteDateEnd;
	/**
	 * 查詢條件：案件狀態
	 */
	private String queryCaseStatus;
	/**
	 * 查詢條件：特店代號
	 */
	private String queryMerchatCode;
	/**
	 * 查詢條件：特店名稱
	 */
	private String queryMerchatName;
	/**
	 * 查詢條件：表頭（同對外名稱）
	 */
	private String queryMerHeader;
	/**
	 * 查詢條件：是否專案
	 */
	private String queryIsProject;
	/**
	 * 查詢條件：專案代碼
	 */
	private String queryProjectCode;
	/**
	 * 查詢條件：專案名稱
	 */
	private String queryProjectName;
	/**
	 * 查詢條件：設備開啟模式
	 */
	private String queryOpenMode;
	/**
	 * 查詢條件：DTID
	 */
	private String queryDtid;
	/**
	 * 查詢條件：TID
	 */
	private String queryTid;
	/**
	 * 查詢條件：刷卡機暨週邊設備支援功能
	 */
	private String querySupportedFun;
	/**
	 * 查詢條件：限制條件
	 */
	private String queryConditionOperator;
	/**
	 * 查詢條件：報修次數
	 */
	private Integer queryRepairTimes;
	/**
	 * 查詢條件：SLA警示件查詢
	 */
	private String queryWarningSla;
	/**
	 * 查詢條件：AO人員
	 */
	private String queryAoName;
	/**
	 * 查詢區域
	 */
	private String queryLocation;
	/**
	 * 案件編號
	 */
	private String caseId;
	/**
	 * 案件編號
	 */
	private String dtid;
	/**
	 * 多筆案件的案件編號集合
	 */
	private List<String> caseIds;
	/**
	 * 案件處理信息
	 */
	private SrmCaseHandleInfoDTO srmCaseHandleInfoDTO;
	/**
	 * 案件處理信息list
	 */
	private List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOs;
	/**
	 * 案件交易參數集合
	 */
	private List<SrmCaseTransactionParameterDTO> srmCaseTransactionParameterDTOs;
	/**
	 * SrmCaseAssetLinkDTO
	 */
	private List<SrmCaseAssetLinkDTO> srmCaseAssetLinkDTO;
	/**
	 * 案件設備耗材鏈接集合String，用於前端和後端交互
	 */
	private String caseAssetLinkParameters;
	/**
	 * 設備類別
	 */
	public static final String PARAM_ITEM_CATEGORY										= "itemCategory";
	/**
	 * 設備類別口窗貼
	 */
	public static final String PARAM_WINDOW_STICKER										= "DMM_SUPPILIES_SUPPLIES_TYPE_WINDOW_STICKER";
	/**
	 * 案件交易參數
	 */
	private SrmCaseTransactionParameterDTO srmCaseTransactionParameterDTO;
	/**
	 * 案件交易參數集合String，用於前端和後端交互
	 */
	private String caseTransactionParameters;
	/**
	 * 案件類別
	 */
	private String caseCategory;
	/**
	 * 默認的處理類型
	 */
	private String defaultProcessType;
	/**
	 * 是客服，含有 	廠商角色、CyberAgent：客服可以查看并處理所有案件資料
	 */
	private boolean isCustomerService;
	
	/**
	 * CR #2951 廠商客服，含有 	廠商角色、CyberAgent：廠商客服可以查看并處理自己廠商案件資料
	 */
	private boolean isVendorService;
	
	/**
	 * 是客戶， 含有	客戶角色、銀行Agent：可以查看并建立本銀行之案件資料
	 */
	private boolean isCustomer;
	/**
	 * 是AQ，廠商角色、CyberAgent且角色为TMS：可以查看所有案件資料并處理派工給TMS的案件
	 */
	private boolean isQA;
	/**
	 * 是TMS，廠商角色、工程師：可以查看并處理派工给自己的案件資料
	 */
	private boolean isTMS;
	/**
	 * 是廠商Agent，廠商角色、廠商Agent：可以查看并處理派工给本廠商的案件資料
	 */
	private boolean isVendorAgent;
	/**
	 * 是Agent，廠商角色、部門Agent：可以查看并處理派工给本部門的案件資料
	 */
	private boolean isAgent;
	/**
	 * 是工程師，廠商角色、工程師：可以查看并處理派工给自己的案件資料
	 */
	private boolean isEngineer;
	/**
	 * 是CyberAgent，廠商角色、CyberAgent：可以查看并協助處理派工给所有廠商的案件資料
	 */
	private boolean isCyberAgent;
	/**
	 * 客戶廠商客服，客戶廠商角色、客戶廠商Agent且角色为CUS_VENDOR_SERVICE：可以查看并處理自己廠商的案件資料
	 */
	private boolean isCusVendorService;
	/**
	 * 客戶
	 */
	private String queryCustomerId;
	/**
	 * 特店代號
	 */
	private String queryMerCode;
	/**
	 * TID
	 */
	private String queryTID;
	/**
	 * 特店名稱
	 */
	private String queryMerchantName;
	/**
	 * 刷卡機設備序號
	 */
	private String queryEDCNumber;
	/**
	 * 表頭
	 */
	private String queryHeaderName;
	/**
	 * 營業地址
	 */
	private String queryAddress;
	
	/**
	 * 客戶默認值
	 */
	private String customerDefaultValue;
	
	/**
	 * 文件名稱
	 */
	private String fileName;
	
	/**
	 * 上傳文件的map集合
	 */
	private Map<String, MultipartFile> fileMap;
	
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 文件ID
	 */
	private String attFileId;
	
	/**
	 * 文件路徑
	 */
	private String filePath;
	
	/**
	 * 上傳文件標識字段
	 */
	private String qquuid;
	
	/**
	 * 原交易參數項計數
	 */
	private int transParamsNum;
	
	/**
	 * 原案件編號
	 */
	private String oldCaseId;
	
	/**
	 * 刪除文件編號
	 */
	private String deleteFileId;
	/**
	 * 通知種類
	 */
	private String noticeType;
	/**
	 * 發件人（隱藏域）
	 */
	private String fromMail;
	/**
	 * 列印工單的flag
	 */
	private String exportFlag;
	/**
	 * 列印工單的範本名稱
	 */
	private String templatesName;
	/**
	 * 列印工單的範本id
	 */
	private String templatesId;
	/**
	 * 列印工單的範本範本類別
	 */
	private String categoryId;
	/**
	 * 列印工單的範本範本路徑
	 */
	private String path;
	/**
	 * 案件匯出的選擇行
	 */
	private String caseExportRowJson;
	/**
	 * 案件動作
	 */
	private String caseActionId;
	/**
	 * caseTransactionRowformDTO
	 */
	private String caseTransactionRow;
	/**
	 * SrmCaseTransactionDTO
	 */
	private SrmCaseTransactionDTO srmCaseTransactionDTO;
	/**
	 * SrmCaseTransactionDTO
	 */
	private List<SrmCaseTransactionDTO> srmCaseTransactionDTOList;
	/**
	 * 案件歷程（json）格式
	 */
	private String srmCaseTransactionDTOsStr;
	/**
	 * 案件附加檔案
	 */
	private List<SrmCaseAttFileDTO> srmCaseAttFileDTOs;
	/**
	 * SrmTransactionParameterItemDTO
	 */
	private List<SrmTransactionParameterItemDTO> srmTransactionParameterItemDTOs;
	/**
	 * 當前日期字符串
	 */
	private String currentDateStr;
	/**
	 * 前一個月日期字符串
	 */
	private String preMonthDateStr;
	
	/**
	 * 及時
	 */
	private boolean isInstant;
	
	/**
	 * 設備類別
	 */
	private String itemCategory;
	/**
	 * 設備名稱
	 */
	private String itemId;
	/**
	 * 查詢派工維護廠商
	 */
	private String queryDispatchVendorId;
	/**
	 * 查詢派工單位
	 */
	private String queryDispatchDeptId;
	/**
	 * 查詢派工人員
	 */
	private String queryDispatchProcessUser;
	/**
	 * activiti所需下一關卡狀態
	 */
	private String activitiCaseStatusStage;
	
	/**
	 * 查詢EDC:設備類別
	 */
	private String chooseEdcAssetCategory;
	/**
	 * 查詢EDC:設備名稱
	 */
	private String chooseEdcAssetId;
	/**
	 * 查詢EDC:設備序號
	 */
	private String chooseEdcSerialNumber;
	/**
	 * 查詢EDC:領用/借用人
	 */
	private String chooseEdcCarrierOrBorrower;
	/**
	 * 查詢EDC:領用/借用說明
	 */
	private String chooseEdcCarrierOrBorrowerComment;
	/**
	 * 查詢EDC:倉庫名稱
	 */
	private String chooseEdcWarehouseId;
	/**
	 * 查詢EDC:派工人員
	 */
	private String chooseEdcDispatchProcessUser;
	/**
	 * 接收人地址
	 */
	private String toMail;
	
	/**
	 * 接收人名称
	 */
	private String toName;
	
	/**
	 * 发件人名称
	 */
	private String fromName;
	
	/**
	 * 邮件主题
	 */
	private String mailSubject;
	
	/**
	 * 邮件内容
	 */
	private String mailContext;
	
	/**
	 * 邮件内容
	 */
	private String mailContext1;
	
	/**
	 * 邮件内容
	 */
	private String mailContext2;
	/**
	 * 郵件通知提醒時間起
	 */
	private String remindStart;
	/**
	 * 郵件通知提醒時間迄
	 */
	private String remindEnd;
	
	/**
	 * 是否查詢歷史表
	 */
	private String isHistory;
	
	/**
	 * 是列印
	 */
	private boolean isExport;
	
	/**
	 * 是否爲廠商角色
	 */
	private boolean isVendorAttribute;
	/**
	 * 是否爲客戶角色
	 */
	private boolean isCustomerAttribute;
	/**
	 * 是否爲客戶廠商角色
	 */
	private boolean isCustomerVendorAttribute;
	/**
	 * 匯出可變列集合
	 */
	private String[] exportFields;
	/**
	 * 簽收 案件耗材鏈接要刪除的ids
	 */
	private String deleteCaseSuppliesLinkIds;
	/**
	 * 簽收 案件設備鏈接要刪除的ids
	 */
	private String deleteCaseAssetLinkIds;
	/**
	 * 簽收 案件設備鏈接已經鏈接過的設備序號
	 */
	private String caseAssetLinkSerialNumbers;
	/**
	 * 簽收 線上排除 耗材和設備鏈接dto的list的map
	 */
	private Map<String, List<SrmCaseAssetLinkDTO>> srmCaseAssetLinkDTOListMap;
	/**
	 * 案件建案畫面欄位前綴
	 */
	private String stuff;
	
	/**
	 * 轉移月份
	 */
	private int transferMonth;
	/**
	 * 匯出客戶Id
	 */
	private String exportQueryCompanyId;
	
	/**
	 * 部門集合
	 */
	private List<String> deptCodeList;
	/**
	 * 處理頁面待派工案件派工標誌位
	 */
	private String hasCaseIdFlag;
	/**
	 * 維護廠商集合
	 */
	private List<String> vendorIdList;
	/**
	 * 上传文件最大值
	 */
	private String uploadFileSize;
	/**
	 * 多筆簽收已經關聯過的sn
	 */
	private String selectSn;
	/**
	 * 查詢經過線上排除
	 */
	private boolean queryOnlineExclusion;
	/**
	 * 環匯客戶編號
	 */
	private String gpCustomerId;
	/**
	 * 郵件的歷程id
	 */
	private String caseTransactionId;
	/**
	 * 案件狀態
	 */
	private String caseStatus;
	/**
	 * 
	 */
	private String caseActionIdEmail;
	
	/**
	 * 忽略待派工
	 */
	private boolean ignoreWaitDispatch; 
	/**
	 * 临时变量－下一版会删除
	 */
	private boolean isTemp;
	
	/**
	 * 查詢條件：進件日期 起
	 */
	private String queryCreateDateStartStr;
	/**
	 * 查詢條件：進件日期 迄
	 */
	private String queryCreateDateEndStr;
	/**
	 * 查詢條件：應完成日期 起
	 */
	private String queryAcceptableCompleteDateStartStr;
	/**
	 * 查詢條件：應完成日期 迄
	 */
	private String queryAcceptableCompleteDateEndStr;
	/**
	 * 查詢條件：完修日期 起
	 */
	private String queryCompleteDateStartStr;
	/**
	 * 查詢條件：完修日期 迄
	 */
	private String queryCompleteDateEndStr;
	/**
	 * 查詢列集合
	 */
	private Map<String, String> queryColumnMap;
	/**
	 * 是否禁用所有按鈕
	 */
	private boolean isHiddenAllBtn;
	/**
	 * 点击对号带值是否要更新contact或者install相关字段标志位 2018/01/15
	 */
	private String hideForUpdateContactFlag;
	/**
	 * 查詢經過退回--Task #3110
	 */
	private boolean queryForBack;
	/**
	 * 是否點擊過dtid帶值
	 */
	private String isCheckDtidFlag;
	/**
	 * 是否有退回記錄
	 */
	private boolean queryDelayRecord;
	/**
	 * 台新銀行ID
	 */
	private String tsbCustomerId;
	/**
	 * 是否爲cms案件失敗
	 */
	private String cmsFalse;
	/**
	 * 是否cms call iatoms api建案
	 */
	private String isCmsCreate;
	/**
	 * 是否批次查詢台新案件
	 */
	private String queryTsbFlag;
	/**
	 * Task #3325(是否爲CMS查詢)
	 */
	private String isCmsQuery;
	/**
	 * 是否微型商戶
	 */
	private boolean isMicro;
	/**
	 * cms案件對應成功信息
	 */
	private Map<String, String> callCmsMap;
	/**
	 * (SYB-EDC)陽信銀行ID
	 */
	private String sybCustomerId;
	/**
	 * (CHB_EDC)彰銀銀行ID
	 */
	private String chbCustomerId;
	/**
	 * (SCSB)上銀銀行ID
	 */
	private String scsbCustomerId;
	/**
	 * api dto
	 */
	private ApiAuthorizationInfoDTO apiAuthorizationInfoDTO;
	/**
	 * 到場註記--Task #3548
	 */
	private String hasArrive;
	/**
	 * 移動工單查詢flag--Task #3560
	 */
	private String mobileQueryFlag;
	/**
	 * 結案匯入flag
	 */
	private String uploadCoordinatedCompletionFlag;
	/**
	 * 是否CyberEDC到場
	 */
	private boolean isMicroArrive;
	/**
	 * (BCC)宣揚ID
	 */
	private String bccCustomerId;
	/**
	 * Constructor: 無參構造
	 */
	public CaseManagerFormDTO() {
		super();
	}

	/**
	 * @return the caseCategory
	 */
	public String getCaseCategory() {
		return caseCategory;
	}

	/**
	 * @param caseCategory the caseCategory to set
	 */
	public void setCaseCategory(String caseCategory) {
		this.caseCategory = caseCategory;
	}

	/**
	 * @return the defaultProcessType
	 */
	public String getDefaultProcessType() {
		return defaultProcessType;
	}

	/**
	 * @param defaultProcessType the defaultProcessType to set
	 */
	public void setDefaultProcessType(String defaultProcessType) {
		this.defaultProcessType = defaultProcessType;
	}

	/**
	 * @return the isCustomerService
	 */
	public boolean getIsCustomerService() {
		return isCustomerService;
	}


	/**
	 * @param isCustomerService the isCustomerService to set
	 */
	public void setIsCustomerService(boolean isCustomerService) {
		this.isCustomerService = isCustomerService;
	}


	/**
	 * @return the isCustomer
	 */
	public boolean getIsCustomer() {
		return isCustomer;
	}

	/**
	 * @param isCustomer the isCustomer to set
	 */
	public void setIsCustomer(boolean isCustomer) {
		this.isCustomer = isCustomer;
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
	 * @return the queryCompanyId
	 */
	public String getQueryCompanyId() {
		return queryCompanyId;
	}

	/**
	 * @param queryCompanyId the queryCompanyId to set
	 */
	public void setQueryCompanyId(String queryCompanyId) {
		this.queryCompanyId = queryCompanyId;
	}

	/**
	 * @return the queryEdcType
	 */
	public String getQueryEdcType() {
		return queryEdcType;
	}

	/**
	 * @param queryEdcType the queryEdcType to set
	 */
	public void setQueryEdcType(String queryEdcType) {
		this.queryEdcType = queryEdcType;
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
	 * @return the queryAoName
	 */
	public String getQueryAoName() {
		return queryAoName;
	}

	/**
	 * @param queryAoName the queryAoName to set
	 */
	public void setQueryAoName(String queryAoName) {
		this.queryAoName = queryAoName;
	}

	/**
	 * @return the caseId
	 */
	public String getCaseId() {
		return caseId;
	}

	/**
	 * @param caseId the caseId to set
	 */
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	/**
	 * @return the srmCaseHandleInfoDTO
	 */
	public SrmCaseHandleInfoDTO getSrmCaseHandleInfoDTO() {
		return srmCaseHandleInfoDTO;
	}

	/**
	 * @param srmCaseHandleInfoDTO the srmCaseHandleInfoDTO to set
	 */
	public void setSrmCaseHandleInfoDTO(SrmCaseHandleInfoDTO srmCaseHandleInfoDTO) {
		this.srmCaseHandleInfoDTO = srmCaseHandleInfoDTO;
	}

	/**
	 * @return the srmCaseTransactionParameterDTOs
	 */
	public List<SrmCaseTransactionParameterDTO> getSrmCaseTransactionParameterDTOs() {
		return srmCaseTransactionParameterDTOs;
	}


	/**
	 * @param srmCaseTransactionParameterDTOs the srmCaseTransactionParameterDTOs to set
	 */
	public void setSrmCaseTransactionParameterDTOs(
			List<SrmCaseTransactionParameterDTO> srmCaseTransactionParameterDTOs) {
		this.srmCaseTransactionParameterDTOs = srmCaseTransactionParameterDTOs;
	}

	/**
	 * @return the caseTransactionParameters
	 */
	public String getCaseTransactionParameters() {
		return caseTransactionParameters;
	}

	/**
	 * @param caseTransactionParameters the caseTransactionParameters to set
	 */
	public void setCaseTransactionParameters(String caseTransactionParameters) {
		this.caseTransactionParameters = caseTransactionParameters;
	}

	/**
	 * @return the queryCustomerId
	 */
	public String getQueryCustomerId() {
		return queryCustomerId;
	}

	/**
	 * @param queryCustomerId the queryCustomerId to set
	 */
	public void setQueryCustomerId(String queryCustomerId) {
		this.queryCustomerId = queryCustomerId;
	}

	/**
	 * @return the queryMerCode
	 */
	public String getQueryMerCode() {
		return queryMerCode;
	}

	/**
	 * @param queryMerCode the queryMerCode to set
	 */
	public void setQueryMerCode(String queryMerCode) {
		this.queryMerCode = queryMerCode;
	}

	/**
	 * @return the queryTID
	 */
	public String getQueryTID() {
		return queryTID;
	}

	/**
	 * @param queryTID the queryTID to set
	 */
	public void setQueryTID(String queryTID) {
		this.queryTID = queryTID;
	}

	/**
	 * @return the queryMerchantName
	 */
	public String getQueryMerchantName() {
		return queryMerchantName;
	}

	/**
	 * @param queryMerchantName the queryMerchantName to set
	 */
	public void setQueryMerchantName(String queryMerchantName) {
		this.queryMerchantName = queryMerchantName;
	}

	/**
	 * @return the queryEDCNumber
	 */
	public String getQueryEDCNumber() {
		return queryEDCNumber;
	}

	/**
	 * @param queryEDCNumber the queryEDCNumber to set
	 */
	public void setQueryEDCNumber(String queryEDCNumber) {
		this.queryEDCNumber = queryEDCNumber;
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
	 * @return the queryAddress
	 */
	public String getQueryAddress() {
		return queryAddress;
	}

	/**
	 * @param queryAddress the queryAddress to set
	 */
	public void setQueryAddress(String queryAddress) {
		this.queryAddress = queryAddress;
	}

	/**
	 * @return the customerDefaultValue
	 */
	public String getCustomerDefaultValue() {
		return customerDefaultValue;
	}

	/**
	 * @param customerDefaultValue the customerDefaultValue to set
	 */
	public void setCustomerDefaultValue(String customerDefaultValue) {
		this.customerDefaultValue = customerDefaultValue;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fileMap
	 */
	public Map<String, MultipartFile> getFileMap() {
		return fileMap;
	}


	/**
	 * @param fileMap the fileMap to set
	 */
	public void setFileMap(Map<String, MultipartFile> fileMap) {
		this.fileMap = fileMap;
	}


	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the attFileId
	 */
	public String getAttFileId() {
		return attFileId;
	}


	/**
	 * @param attFileId the attFileId to set
	 */
	public void setAttFileId(String attFileId) {
		this.attFileId = attFileId;
	}


	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}


	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}


	/**
	 * @return the qquuid
	 */
	public String getQquuid() {
		return qquuid;
	}


	/**
	 * @param qquuid the qquuid to set
	 */
	public void setQquuid(String qquuid) {
		this.qquuid = qquuid;
	}


	/**
	 * @return the queryRequirementNo
	 */
	public String getQueryRequirementNo() {
		return queryRequirementNo;
	}


	/**
	 * @param queryRequirementNo the queryRequirementNo to set
	 */
	public void setQueryRequirementNo(String queryRequirementNo) {
		this.queryRequirementNo = queryRequirementNo;
	}


	/**
	 * @return the queryCaseId
	 */
	public String getQueryCaseId() {
		return queryCaseId;
	}


	/**
	 * @param queryCaseId the queryCaseId to set
	 */
	public void setQueryCaseId(String queryCaseId) {
		this.queryCaseId = queryCaseId;
	}


	/**
	 * @return the queryCaseCategory
	 */
	public String getQueryCaseCategory() {
		return queryCaseCategory;
	}


	/**
	 * @param queryCaseCategory the queryCaseCategory to set
	 */
	public void setQueryCaseCategory(String queryCaseCategory) {
		this.queryCaseCategory = queryCaseCategory;
	}


	/**
	 * @return the queryVendorId
	 */
	public String getQueryVendorId() {
		return queryVendorId;
	}


	/**
	 * @param queryVendorId the queryVendorId to set
	 */
	public void setQueryVendorId(String queryVendorId) {
		this.queryVendorId = queryVendorId;
	}


	/**
	 * @return the queryVendorDept
	 */
	public String getQueryVendorDept() {
		return queryVendorDept;
	}


	/**
	 * @param queryVendorDept the queryVendorDept to set
	 */
	public void setQueryVendorDept(String queryVendorDept) {
		this.queryVendorDept = queryVendorDept;
	}


	/**
	 * @return the queryCreateDateStart
	 */
	public Date getQueryCreateDateStart() {
		return queryCreateDateStart;
	}


	/**
	 * @param queryCreateDateStart the queryCreateDateStart to set
	 */
	public void setQueryCreateDateStart(Date queryCreateDateStart) {
		this.queryCreateDateStart = queryCreateDateStart;
	}


	/**
	 * @return the queryCreateDateEnd
	 */
	public Date getQueryCreateDateEnd() {
		return queryCreateDateEnd;
	}


	/**
	 * @param queryCreateDateEnd the queryCreateDateEnd to set
	 */
	public void setQueryCreateDateEnd(Date queryCreateDateEnd) {
		this.queryCreateDateEnd = queryCreateDateEnd;
	}


	/**
	 * @return the queryAcceptableCompleteDateStart
	 */
	public Date getQueryAcceptableCompleteDateStart() {
		return queryAcceptableCompleteDateStart;
	}


	/**
	 * @param queryAcceptableCompleteDateStart the queryAcceptableCompleteDateStart to set
	 */
	public void setQueryAcceptableCompleteDateStart(Date queryAcceptableCompleteDateStart) {
		this.queryAcceptableCompleteDateStart = queryAcceptableCompleteDateStart;
	}


	/**
	 * @return the queryAcceptableCompleteDateEnd
	 */
	public Date getQueryAcceptableCompleteDateEnd() {
		return queryAcceptableCompleteDateEnd;
	}


	/**
	 * @param queryAcceptableCompleteDateEnd the queryAcceptableCompleteDateEnd to set
	 */
	public void setQueryAcceptableCompleteDateEnd(Date queryAcceptableCompleteDateEnd) {
		this.queryAcceptableCompleteDateEnd = queryAcceptableCompleteDateEnd;
	}


	/**
	 * @return the queryCompleteDateStart
	 */
	public Date getQueryCompleteDateStart() {
		return queryCompleteDateStart;
	}


	/**
	 * @param queryCompleteDateStart the queryCompleteDateStart to set
	 */
	public void setQueryCompleteDateStart(Date queryCompleteDateStart) {
		this.queryCompleteDateStart = queryCompleteDateStart;
	}


	/**
	 * @return the queryCompleteDateEnd
	 */
	public Date getQueryCompleteDateEnd() {
		return queryCompleteDateEnd;
	}


	/**
	 * @param queryCompleteDateEnd the queryCompleteDateEnd to set
	 */
	public void setQueryCompleteDateEnd(Date queryCompleteDateEnd) {
		this.queryCompleteDateEnd = queryCompleteDateEnd;
	}


	/**
	 * @return the queryCaseStatus
	 */
	public String getQueryCaseStatus() {
		return queryCaseStatus;
	}


	/**
	 * @param queryCaseStatus the queryCaseStatus to set
	 */
	public void setQueryCaseStatus(String queryCaseStatus) {
		this.queryCaseStatus = queryCaseStatus;
	}


	/**
	 * @return the queryMerchatCode
	 */
	public String getQueryMerchatCode() {
		return queryMerchatCode;
	}


	/**
	 * @param queryMerchatCode the queryMerchatCode to set
	 */
	public void setQueryMerchatCode(String queryMerchatCode) {
		this.queryMerchatCode = queryMerchatCode;
	}


	/**
	 * @return the queryMerchatName
	 */
	public String getQueryMerchatName() {
		return queryMerchatName;
	}


	/**
	 * @param queryMerchatName the queryMerchatName to set
	 */
	public void setQueryMerchatName(String queryMerchatName) {
		this.queryMerchatName = queryMerchatName;
	}


	/**
	 * @return the queryMerHeader
	 */
	public String getQueryMerHeader() {
		return queryMerHeader;
	}


	/**
	 * @param queryMerHeader the queryMerHeader to set
	 */
	public void setQueryMerHeader(String queryMerHeader) {
		this.queryMerHeader = queryMerHeader;
	}


	/**
	 * @return the queryIsProject
	 */
	public String getQueryIsProject() {
		return queryIsProject;
	}

	/**
	 * @param queryIsProject the queryIsProject to set
	 */
	public void setQueryIsProject(String queryIsProject) {
		this.queryIsProject = queryIsProject;
	}

	/**
	 * @return the queryProjectCode
	 */
	public String getQueryProjectCode() {
		return queryProjectCode;
	}

	/**
	 * @param queryProjectCode the queryProjectCode to set
	 */
	public void setQueryProjectCode(String queryProjectCode) {
		this.queryProjectCode = queryProjectCode;
	}

	/**
	 * @return the queryProjectName
	 */
	public String getQueryProjectName() {
		return queryProjectName;
	}

	/**
	 * @param queryProjectName the queryProjectName to set
	 */
	public void setQueryProjectName(String queryProjectName) {
		this.queryProjectName = queryProjectName;
	}

	/**
	 * @return the queryOpenMode
	 */
	public String getQueryOpenMode() {
		return queryOpenMode;
	}

	/**
	 * @param queryOpenMode the queryOpenMode to set
	 */
	public void setQueryOpenMode(String queryOpenMode) {
		this.queryOpenMode = queryOpenMode;
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
	 * @return the querySupportedFun
	 */
	public String getQuerySupportedFun() {
		return querySupportedFun;
	}

	/**
	 * @param querySupportedFun the querySupportedFun to set
	 */
	public void setQuerySupportedFun(String querySupportedFun) {
		this.querySupportedFun = querySupportedFun;
	}

	/**
	 * @return the queryConditionOperator
	 */
	public String getQueryConditionOperator() {
		return queryConditionOperator;
	}

	/**
	 * @param queryConditionOperator the queryConditionOperator to set
	 */
	public void setQueryConditionOperator(String queryConditionOperator) {
		this.queryConditionOperator = queryConditionOperator;
	}

	/**
	 * @return the queryRepairTimes
	 */
	public Integer getQueryRepairTimes() {
		return queryRepairTimes;
	}


	/**
	 * @param queryRepairTimes the queryRepairTimes to set
	 */
	public void setQueryRepairTimes(Integer queryRepairTimes) {
		this.queryRepairTimes = queryRepairTimes;
	}

	/**
	 * @return the queryWarningSla
	 */
	public String getQueryWarningSla() {
		return queryWarningSla;
	}

	/**
	 * @param queryWarningSla the queryWarningSla to set
	 */
	public void setQueryWarningSla(String queryWarningSla) {
		this.queryWarningSla = queryWarningSla;
	}

	/**
	 * @return the transParamsNum
	 */
	public int getTransParamsNum() {
		return transParamsNum;
	}

	/**
	 * @param transParamsNum the transParamsNum to set
	 */
	public void setTransParamsNum(int transParamsNum) {
		this.transParamsNum = transParamsNum;
	}


	/**
	 * @return the oldCaseId
	 */
	public String getOldCaseId() {
		return oldCaseId;
	}


	/**
	 * @param oldCaseId the oldCaseId to set
	 */
	public void setOldCaseId(String oldCaseId) {
		this.oldCaseId = oldCaseId;
	}


	/**
	 * @return the deleteFileId
	 */
	public String getDeleteFileId() {
		return deleteFileId;
	}


	/**
	 * @param deleteFileId the deleteFileId to set
	 */
	public void setDeleteFileId(String deleteFileId) {
		this.deleteFileId = deleteFileId;
	}


	/**
	 * @return the srmCaseAttFileDTOs
	 */
	public List<SrmCaseAttFileDTO> getSrmCaseAttFileDTOs() {
		return srmCaseAttFileDTOs;
	}


	/**
	 * @param srmCaseAttFileDTOs the srmCaseAttFileDTOs to set
	 */
	public void setSrmCaseAttFileDTOs(List<SrmCaseAttFileDTO> srmCaseAttFileDTOs) {
		this.srmCaseAttFileDTOs = srmCaseAttFileDTOs;
	}

	/**
	 * @return the noticeType
	 */
	public String getNoticeType() {
		return noticeType;
	}


	/**
	 * @param noticeType the noticeType to set
	 */
	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}


	/**
	 * @return the fromMail
	 */
	public String getFromMail() {
		return fromMail;
	}


	/**
	 * @param fromMail the fromMail to set
	 */
	public void setFromMail(String fromMail) {
		this.fromMail = fromMail;
	}


	/**
	 * @return the srmCaseHandleInfoDTOs
	 */
	public List<SrmCaseHandleInfoDTO> getSrmCaseHandleInfoDTOs() {
		return srmCaseHandleInfoDTOs;
	}


	/**
	 * @param srmCaseHandleInfoDTOs the srmCaseHandleInfoDTOs to set
	 */
	public void setSrmCaseHandleInfoDTOs(
			List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOs) {
		this.srmCaseHandleInfoDTOs = srmCaseHandleInfoDTOs;
	}

	/**
	 * @return the caseActionId
	 */
	public String getCaseActionId() {
		return caseActionId;
	}

	/**
	 * @param caseActionId the caseActionId to set
	 */
	public void setCaseActionId(String caseActionId) {
		this.caseActionId = caseActionId;
	}

	/**
	 * @return the caseTransactionRow
	 */
	public String getCaseTransactionRow() {
		return caseTransactionRow;
	}

	/**
	 * @param caseTransactionRow the caseTransactionRow to set
	 */
	public void setCaseTransactionRow(String caseTransactionRow) {
		this.caseTransactionRow = caseTransactionRow;
	}

	/**
	 * @return the srmCaseTransactionDTO
	 */
	public SrmCaseTransactionDTO getSrmCaseTransactionDTO() {
		return srmCaseTransactionDTO;
	}

	/**
	 * @param srmCaseTransactionDTO the srmCaseTransactionDTO to set
	 */
	public void setSrmCaseTransactionDTO(SrmCaseTransactionDTO srmCaseTransactionDTO) {
		this.srmCaseTransactionDTO = srmCaseTransactionDTO;
	}
	

	/**
	 * @return the caseExportRowJson
	 */
	public String getCaseExportRowJson() {
		return caseExportRowJson;
	}


	/**
	 * @param caseExportRowJson the caseExportRowJson to set
	 */
	public void setCaseExportRowJson(String caseExportRowJson) {
		this.caseExportRowJson = caseExportRowJson;
	}


	/**
	 * @return the exportFlag
	 */
	public String getExportFlag() {
		return exportFlag;
	}


	/**
	 * @param exportFlag the exportFlag to set
	 */
	public void setExportFlag(String exportFlag) {
		this.exportFlag = exportFlag;
	}


	/**
	 * @return the srmCaseTransactionParameterDTO
	 */
	public SrmCaseTransactionParameterDTO getSrmCaseTransactionParameterDTO() {
		return srmCaseTransactionParameterDTO;
	}


	/**
	 * @param srmCaseTransactionParameterDTO the srmCaseTransactionParameterDTO to set
	 */
	public void setSrmCaseTransactionParameterDTO(
			SrmCaseTransactionParameterDTO srmCaseTransactionParameterDTO) {
		this.srmCaseTransactionParameterDTO = srmCaseTransactionParameterDTO;
	}


	/**
	 * @return the srmCaseTransactionDTOList
	 */
	public List<SrmCaseTransactionDTO> getSrmCaseTransactionDTOList() {
		return srmCaseTransactionDTOList;
	}


	/**
	 * @param srmCaseTransactionDTOList the srmCaseTransactionDTOList to set
	 */
	public void setSrmCaseTransactionDTOList(
			List<SrmCaseTransactionDTO> srmCaseTransactionDTOList) {
		this.srmCaseTransactionDTOList = srmCaseTransactionDTOList;
	}
	

	/**
	 * @return the isQA
	 */
	public boolean getIsQA() {
		return isQA;
	}


	/**
	 * @param isQA the isQA to set
	 */
	public void setIsQA(boolean isQA) {
		this.isQA = isQA;
	}


	/**
	 * @return the isTMS
	 */
	public boolean getIsTMS() {
		return isTMS;
	}


	/**
	 * @param isTMS the isTMS to set
	 */
	public void setIsTMS(boolean isTMS) {
		this.isTMS = isTMS;
	}


	/**
	 * @return the isAgent
	 */
	public boolean getIsAgent() {
		return isAgent;
	}


	/**
	 * @param isAgent the isAgent to set
	 */
	public void setIsAgent(boolean isAgent) {
		this.isAgent = isAgent;
	}


	/**
	 * @return the isEngineer
	 */
	public boolean getIsEngineer() {
		return isEngineer;
	}


	/**
	 * @param isEngineer the isEngineer to set
	 */
	public void setIsEngineer(boolean isEngineer) {
		this.isEngineer = isEngineer;
	}


	/**
	 * @return the currentDateStr
	 */
	public String getCurrentDateStr() {
		return currentDateStr;
	}


	/**
	 * @param currentDateStr the currentDateStr to set
	 */
	public void setCurrentDateStr(String currentDateStr) {
		this.currentDateStr = currentDateStr;
	}


	/**
	 * @return the preMonthDateStr
	 */
	public String getPreMonthDateStr() {
		return preMonthDateStr;
	}


	/**
	 * @param preMonthDateStr the preMonthDateStr to set
	 */
	public void setPreMonthDateStr(String preMonthDateStr) {
		this.preMonthDateStr = preMonthDateStr;
	}


	/**
	 * @return the isInstant
	 */
	public boolean getIsInstant() {
		return isInstant;
	}


	/**
	 * @param isInstant the isInstant to set
	 */
	public void setIsInstant(boolean isInstant) {
		this.isInstant = isInstant;
	}

	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}


	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}


	/**
	 * @return the itemCategory
	 */
	public String getItemCategory() {
		return itemCategory;
	}


	/**
	 * @param itemCategory the itemCategory to set
	 */
	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}


	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}


	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}


	/**
	 * @return the srmCaseAssetLinkDTO
	 */
	public List<SrmCaseAssetLinkDTO> getSrmCaseAssetLinkDTO() {
		return srmCaseAssetLinkDTO;
	}


	/**
	 * @param srmCaseAssetLinkDTO the srmCaseAssetLinkDTO to set
	 */
	public void setSrmCaseAssetLinkDTO(List<SrmCaseAssetLinkDTO> srmCaseAssetLinkDTO) {
		this.srmCaseAssetLinkDTO = srmCaseAssetLinkDTO;
	}


	/**
	 * @return the srmTransactionParameterItemDTOs
	 */
	public List<SrmTransactionParameterItemDTO> getSrmTransactionParameterItemDTOs() {
		return srmTransactionParameterItemDTOs;
	}


	/**
	 * @param srmTransactionParameterItemDTOs the srmTransactionParameterItemDTOs to set
	 */
	public void setSrmTransactionParameterItemDTOs(
			List<SrmTransactionParameterItemDTO> srmTransactionParameterItemDTOs) {
		this.srmTransactionParameterItemDTOs = srmTransactionParameterItemDTOs;
	}
	/**
	 * @return the queryDispatchDeptId
	 */
	public String getQueryDispatchDeptId() {
		return queryDispatchDeptId;
	}


	/**
	 * @param queryDispatchDeptId the queryDispatchDeptId to set
	 */
	public void setQueryDispatchDeptId(String queryDispatchDeptId) {
		this.queryDispatchDeptId = queryDispatchDeptId;
	}


	/**
	 * @return the queryDispatchProcessUser
	 */
	public String getQueryDispatchProcessUser() {
		return queryDispatchProcessUser;
	}


	/**
	 * @param queryDispatchProcessUser the queryDispatchProcessUser to set
	 */
	public void setQueryDispatchProcessUser(String queryDispatchProcessUser) {
		this.queryDispatchProcessUser = queryDispatchProcessUser;
	}


	/**
	 * @return the activitiCaseStatusStage
	 */
	public String getActivitiCaseStatusStage() {
		return activitiCaseStatusStage;
	}


	/**
	 * @param activitiCaseStatusStage the activitiCaseStatusStage to set
	 */
	public void setActivitiCaseStatusStage(String activitiCaseStatusStage) {
		this.activitiCaseStatusStage = activitiCaseStatusStage;
	}
	/**
	 * @return the caseAssetLinkParameters
	 */
	public String getCaseAssetLinkParameters() {
		return caseAssetLinkParameters;
	}


	/**
	 * @param caseAssetLinkParameters the caseAssetLinkParameters to set
	 */
	public void setCaseAssetLinkParameters(String caseAssetLinkParameters) {
		this.caseAssetLinkParameters = caseAssetLinkParameters;
	}


	/**
	 * @return the caseIds
	 */
	public List<String> getCaseIds() {
		return caseIds;
	}


	/**
	 * @param caseIds the caseIds to set
	 */
	public void setCaseIds(List<String> caseIds) {
		this.caseIds = caseIds;
	}

	/**
	 * @return the chooseEdcAssetCategory
	 */
	public String getChooseEdcAssetCategory() {
		return chooseEdcAssetCategory;
	}

	/**
	 * @param chooseEdcAssetCategory the chooseEdcAssetCategory to set
	 */
	public void setChooseEdcAssetCategory(String chooseEdcAssetCategory) {
		this.chooseEdcAssetCategory = chooseEdcAssetCategory;
	}

	/**
	 * @return the chooseEdcAssetId
	 */
	public String getChooseEdcAssetId() {
		return chooseEdcAssetId;
	}

	/**
	 * @param chooseEdcAssetId the chooseEdcAssetId to set
	 */
	public void setChooseEdcAssetId(String chooseEdcAssetId) {
		this.chooseEdcAssetId = chooseEdcAssetId;
	}

	/**
	 * @return the chooseEdcSerialNumber
	 */
	public String getChooseEdcSerialNumber() {
		return chooseEdcSerialNumber;
	}

	/**
	 * @param chooseEdcSerialNumber the chooseEdcSerialNumber to set
	 */
	public void setChooseEdcSerialNumber(String chooseEdcSerialNumber) {
		this.chooseEdcSerialNumber = chooseEdcSerialNumber;
	}

	/**
	 * @return the chooseEdcCarrierOrBorrower
	 */
	public String getChooseEdcCarrierOrBorrower() {
		return chooseEdcCarrierOrBorrower;
	}

	/**
	 * @param chooseEdcCarrierOrBorrower the chooseEdcCarrierOrBorrower to set
	 */
	public void setChooseEdcCarrierOrBorrower(String chooseEdcCarrierOrBorrower) {
		this.chooseEdcCarrierOrBorrower = chooseEdcCarrierOrBorrower;
	}

	/**
	 * @return the chooseEdcCarrierOrBorrowerComment
	 */
	public String getChooseEdcCarrierOrBorrowerComment() {
		return chooseEdcCarrierOrBorrowerComment;
	}

	/**
	 * @param chooseEdcCarrierOrBorrowerComment the chooseEdcCarrierOrBorrowerComment to set
	 */
	public void setChooseEdcCarrierOrBorrowerComment(
			String chooseEdcCarrierOrBorrowerComment) {
		this.chooseEdcCarrierOrBorrowerComment = chooseEdcCarrierOrBorrowerComment;
	}

	/**
	 * @return the chooseEdcWarehouseId
	 */
	public String getChooseEdcWarehouseId() {
		return chooseEdcWarehouseId;
	}

	/**
	 * @param chooseEdcWarehouseId the chooseEdcWarehouseId to set
	 */
	public void setChooseEdcWarehouseId(String chooseEdcWarehouseId) {
		this.chooseEdcWarehouseId = chooseEdcWarehouseId;
	}

	/**
	 * @return the templatesName
	 */
	public String getTemplatesName() {
		return templatesName;
	}

	/**
	 * @param templatesName the templatesName to set
	 */
	public void setTemplatesName(String templatesName) {
		this.templatesName = templatesName;
	}

	/**
	 * @return the templatesId
	 */
	public String getTemplatesId() {
		return templatesId;
	}

	/**
	 * @param templatesId the templatesId to set
	 */
	public void setTemplatesId(String templatesId) {
		this.templatesId = templatesId;
	}

	/**
	 * @return the categoryId
	 */
	public String getCategoryId() {
		return categoryId;
	}

	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
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
	 * @return the toName
	 */
	public String getToName() {
		return toName;
	}

	/**
	 * @param toName the toName to set
	 */
	public void setToName(String toName) {
		this.toName = toName;
	}

	/**
	 * @return the mailSubject
	 */
	public String getMailSubject() {
		return mailSubject;
	}

	/**
	 * @param mailSubject the mailSubject to set
	 */
	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	/**
	 * @return the mailContext
	 */
	public String getMailContext() {
		return mailContext;
	}

	/**
	 * @param mailContext the mailContext to set
	 */
	public void setMailContext(String mailContext) {
		this.mailContext = mailContext;
	}

	/**
	 * @return the mailContext1
	 */
	public String getMailContext1() {
		return mailContext1;
	}

	/**
	 * @param mailContext1 the mailContext1 to set
	 */
	public void setMailContext1(String mailContext1) {
		this.mailContext1 = mailContext1;
	}

	/**
	 * @return the mailContext2
	 */
	public String getMailContext2() {
		return mailContext2;
	}

	/**
	 * @param mailContext2 the mailContext2 to set
	 */
	public void setMailContext2(String mailContext2) {
		this.mailContext2 = mailContext2;
	}

	/**
	 * @return the remindStart
	 */
	public String getRemindStart() {
		return remindStart;
	}

	/**
	 * @param remindStart the remindStart to set
	 */
	public void setRemindStart(String remindStart) {
		this.remindStart = remindStart;
	}

	/**
	 * @return the remindEnd
	 */
	public String getRemindEnd() {
		return remindEnd;
	}

	/**
	 * @param remindEnd the remindEnd to set
	 */
	public void setRemindEnd(String remindEnd) {
		this.remindEnd = remindEnd;
	}

	/**
	 * @return the isHistory
	 */
	public String getIsHistory() {
		return isHistory;
	}

	/**
	 * @param isHistory the isHistory to set
	 */
	public void setIsHistory(String isHistory) {
		this.isHistory = isHistory;
	}

	/**
	 * @return the isExport
	 */
	public boolean getIsExport() {
		return isExport;
	}

	/**
	 * @param isExport the isExport to set
	 */
	public void setIsExport(boolean isExport) {
		this.isExport = isExport;
	}

	/**
	 * @return the isNoRoles
	 */
	public boolean getIsVendorAttribute() {
		return isVendorAttribute;
	}

	/**
	 * @param isNoRoles the isNoRoles to set
	 */
	public void setIsVendorAttribute(boolean isVendorAttribute) {
		this.isVendorAttribute = isVendorAttribute;
	}

	/**
	 * @return the isCustomerAttribute
	 */
	public boolean getIsCustomerAttribute() {
		return isCustomerAttribute;
	}

	/**
	 * @param isCustomerAttribute the isCustomerAttribute to set
	 */
	public void setIsCustomerAttribute(boolean isCustomerAttribute) {
		this.isCustomerAttribute = isCustomerAttribute;
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
	 * @return the chooseEdcDispatchProcessUser
	 */
	public String getChooseEdcDispatchProcessUser() {
		return chooseEdcDispatchProcessUser;
	}

	/**
	 * @param chooseEdcDispatchProcessUser the chooseEdcDispatchProcessUser to set
	 */
	public void setChooseEdcDispatchProcessUser(String chooseEdcDispatchProcessUser) {
		this.chooseEdcDispatchProcessUser = chooseEdcDispatchProcessUser;
	}

	/**
	 * @return the deleteCaseSuppliesLinkIds
	 */
	public String getDeleteCaseSuppliesLinkIds() {
		return deleteCaseSuppliesLinkIds;
	}

	/**
	 * @param deleteCaseSuppliesLinkIds the deleteCaseSuppliesLinkIds to set
	 */
	public void setDeleteCaseSuppliesLinkIds(String deleteCaseSuppliesLinkIds) {
		this.deleteCaseSuppliesLinkIds = deleteCaseSuppliesLinkIds;
	}

	/**
	 * @return the fromName
	 */
	public String getFromName() {
		return fromName;
	}

	/**
	 * @param fromName the fromName to set
	 */
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	/**
	 * @return the caseAssetLinkSerialNumbers
	 */
	public String getCaseAssetLinkSerialNumbers() {
		return caseAssetLinkSerialNumbers;
	}

	/**
	 * @param caseAssetLinkSerialNumbers the caseAssetLinkSerialNumbers to set
	 */
	public void setCaseAssetLinkSerialNumbers(String caseAssetLinkSerialNumbers) {
		this.caseAssetLinkSerialNumbers = caseAssetLinkSerialNumbers;
	}

	/**
	 * @return the srmCaseAssetLinkDTOListMap
	 */
	public Map<String, List<SrmCaseAssetLinkDTO>> getSrmCaseAssetLinkDTOListMap() {
		return srmCaseAssetLinkDTOListMap;
	}

	/**
	 * @param srmCaseAssetLinkDTOListMap the srmCaseAssetLinkDTOListMap to set
	 */
	public void setSrmCaseAssetLinkDTOListMap(
			Map<String, List<SrmCaseAssetLinkDTO>> srmCaseAssetLinkDTOListMap) {
		this.srmCaseAssetLinkDTOListMap = srmCaseAssetLinkDTOListMap;
	}

	/**
	 * @return the stuff
	 */
	public String getStuff() {
		return stuff;
	}

	/**
	 * @param stuff the stuff to set
	 */
	public void setStuff(String stuff) {
		this.stuff = stuff;
	}

	/**
	 * @return the dtid
	 */
	public String getDtid() {
		return dtid;
	}

	/**
	 * @param dtid the dtid to set
	 */
	public void setDtid(String dtid) {
		this.dtid = dtid;
	}

	/**
	 * @return the transferMonth
	 */
	public int getTransferMonth() {
		return transferMonth;
	}

	/**
	 * @param transferMonth the transferMonth to set
	 */
	public void setTransferMonth(int transferMonth) {
		this.transferMonth = transferMonth;
	}

	/**
	 * @return the isVendorAgent
	 */
	public boolean getIsVendorAgent() {
		return isVendorAgent;
	}

	/**
	 * @param isVendorAgent the isVendorAgent to set
	 */
	public void setIsVendorAgent(boolean isVendorAgent) {
		this.isVendorAgent = isVendorAgent;
	}

	/**
	 * @return the queryDispatchVendorId
	 */
	public String getQueryDispatchVendorId() {
		return queryDispatchVendorId;
	}

	/**
	 * @param queryDispatchVendorId the queryDispatchVendorId to set
	 */
	public void setQueryDispatchVendorId(String queryDispatchVendorId) {
		this.queryDispatchVendorId = queryDispatchVendorId;
	}

	/**
	 * @return the exportQueryCompanyId
	 */
	public String getExportQueryCompanyId() {
		return exportQueryCompanyId;
	}

	/**
	 * @param exportQueryCompanyId the exportQueryCompanyId to set
	 */
	public void setExportQueryCompanyId(String exportQueryCompanyId) {
		this.exportQueryCompanyId = exportQueryCompanyId;
	}
	
	/**
	 * @return the deptCodeList
	 */
	public List<String> getDeptCodeList() {
		return deptCodeList;
	}

	/**
	 * @param deptCodeList the deptCodeList to set
	 */
	public void setDeptCodeList(List<String> deptCodeList) {
		this.deptCodeList = deptCodeList;
	}

	/**
	 * @return the deleteCaseAssetLinkIds
	 */
	public String getDeleteCaseAssetLinkIds() {
		return deleteCaseAssetLinkIds;
	}

	/**
	 * @param deleteCaseAssetLinkIds the deleteCaseAssetLinkIds to set
	 */
	public void setDeleteCaseAssetLinkIds(String deleteCaseAssetLinkIds) {
		this.deleteCaseAssetLinkIds = deleteCaseAssetLinkIds;
	}

	/**
	 * @return the hasCaseIdFlag
	 */
	public String getHasCaseIdFlag() {
		return hasCaseIdFlag;
	}

	/**
	 * @param hasCaseIdFlag the hasCaseIdFlag to set
	 */
	public void setHasCaseIdFlag(String hasCaseIdFlag) {
		this.hasCaseIdFlag = hasCaseIdFlag;
	}

	/**
	 * @return the isCyberAgent
	 */
	public boolean getIsCyberAgent() {
		return isCyberAgent;
	}

	/**
	 * @param isCyberAgent the isCyberAgent to set
	 */
	public void setIsCyberAgent(boolean isCyberAgent) {
		this.isCyberAgent = isCyberAgent;
	}
	
	/**
	 * @return the isCusVendorService
	 */
	public boolean getIsCusVendorService() {
		return isCusVendorService;
	}

	/**
	 * @param isCusVendorService the isCusVendorService to set
	 */
	public void setIsCusVendorService(boolean isCusVendorService) {
		this.isCusVendorService = isCusVendorService;
	}
	/**
	 * @return the vendorIdList
	 */
	public List<String> getVendorIdList() {
		return vendorIdList;
	}

	/**
	 * @param vendorIdList the vendorIdList to set
	 */
	public void setVendorIdList(List<String> vendorIdList) {
		this.vendorIdList = vendorIdList;
	}

	/**
	 * @return the selectSn
	 */
	public String getSelectSn() {
		return selectSn;
	}

	/**
	 * @param selectSn the selectSn to set
	 */
	public void setSelectSn(String selectSn) {
		this.selectSn = selectSn;
	}

	public String getUploadFileSize() {
		return uploadFileSize;
	}

	public void setUploadFileSize(String uploadFileSize) {
		this.uploadFileSize = uploadFileSize;
	}

	/**
	 * @return the queryOnlineExclusion
	 */
	public boolean getQueryOnlineExclusion() {
		return queryOnlineExclusion;
	}

	/**
	 * @param queryOnlineExclusion the queryOnlineExclusion to set
	 */
	public void setQueryOnlineExclusion(boolean queryOnlineExclusion) {
		this.queryOnlineExclusion = queryOnlineExclusion;
	}

	/**
	 * @return the caseTransactionId
	 */
	public String getCaseTransactionId() {
		return caseTransactionId;
	}

	/**
	 * @param caseTransactionId the caseTransactionId to set
	 */
	public void setCaseTransactionId(String caseTransactionId) {
		this.caseTransactionId = caseTransactionId;
	}

	/**
	 * @return the caseStatus
	 */
	public String getCaseStatus() {
		return caseStatus;
	}

	/**
	 * @param caseStatus the caseStatus to set
	 */
	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	/**
	 * @return the caseActionIdEmail
	 */
	public String getCaseActionIdEmail() {
		return caseActionIdEmail;
	}

	/**
	 * @param caseActionIdEmail the caseActionIdEmail to set
	 */
	public void setCaseActionIdEmail(String caseActionIdEmail) {
		this.caseActionIdEmail = caseActionIdEmail;
	}

	/**
	 * @return the gpCustomerId
	 */
	public String getGpCustomerId() {
		return gpCustomerId;
	}

	/**
	 * @param gpCustomerId the gpCustomerId to set
	 */
	public void setGpCustomerId(String gpCustomerId) {
		this.gpCustomerId = gpCustomerId;
	}

	/**
	 * @return the ignoreWaitDispatch
	 */
	public boolean getIgnoreWaitDispatch() {
		return ignoreWaitDispatch;
	}

	/**
	 * @param ignoreWaitDispatch the ignoreWaitDispatch to set
	 */
	public void setIgnoreWaitDispatch(boolean ignoreWaitDispatch) {
		this.ignoreWaitDispatch = ignoreWaitDispatch;
	}

	public boolean getIsTemp() {
		return isTemp;
	}

	public void setIsTemp(boolean isTemp) {
		this.isTemp = isTemp;
	}

	/**
	 * @return the queryCreateDateStartStr
	 */
	public String getQueryCreateDateStartStr() {
		return queryCreateDateStartStr;
	}

	/**
	 * @param queryCreateDateStartStr the queryCreateDateStartStr to set
	 */
	public void setQueryCreateDateStartStr(String queryCreateDateStartStr) {
		this.queryCreateDateStartStr = queryCreateDateStartStr;
	}

	/**
	 * @return the queryCreateDateEndStr
	 */
	public String getQueryCreateDateEndStr() {
		return queryCreateDateEndStr;
	}

	/**
	 * @param queryCreateDateEndStr the queryCreateDateEndStr to set
	 */
	public void setQueryCreateDateEndStr(String queryCreateDateEndStr) {
		this.queryCreateDateEndStr = queryCreateDateEndStr;
	}

	/**
	 * @return the queryAcceptableCompleteDateStartStr
	 */
	public String getQueryAcceptableCompleteDateStartStr() {
		return queryAcceptableCompleteDateStartStr;
	}

	/**
	 * @param queryAcceptableCompleteDateStartStr the queryAcceptableCompleteDateStartStr to set
	 */
	public void setQueryAcceptableCompleteDateStartStr(String queryAcceptableCompleteDateStartStr) {
		this.queryAcceptableCompleteDateStartStr = queryAcceptableCompleteDateStartStr;
	}

	/**
	 * @return the queryAcceptableCompleteDateEndStr
	 */
	public String getQueryAcceptableCompleteDateEndStr() {
		return queryAcceptableCompleteDateEndStr;
	}

	/**
	 * @param queryAcceptableCompleteDateEndStr the queryAcceptableCompleteDateEndStr to set
	 */
	public void setQueryAcceptableCompleteDateEndStr(String queryAcceptableCompleteDateEndStr) {
		this.queryAcceptableCompleteDateEndStr = queryAcceptableCompleteDateEndStr;
	}

	/**
	 * @return the queryCompleteDateStartStr
	 */
	public String getQueryCompleteDateStartStr() {
		return queryCompleteDateStartStr;
	}

	/**
	 * @param queryCompleteDateStartStr the queryCompleteDateStartStr to set
	 */
	public void setQueryCompleteDateStartStr(String queryCompleteDateStartStr) {
		this.queryCompleteDateStartStr = queryCompleteDateStartStr;
	}

	/**
	 * @return the queryCompleteDateEndStr
	 */
	public String getQueryCompleteDateEndStr() {
		return queryCompleteDateEndStr;
	}

	/**
	 * @param queryCompleteDateEndStr the queryCompleteDateEndStr to set
	 */
	public void setQueryCompleteDateEndStr(String queryCompleteDateEndStr) {
		this.queryCompleteDateEndStr = queryCompleteDateEndStr;
	}

	/**
	 * @return the queryColumnMap
	 */
	public Map<String, String> getQueryColumnMap() {
		return queryColumnMap;
	}

	/**
	 * @param queryColumnMap the queryColumnMap to set
	 */
	public void setQueryColumnMap(Map<String, String> queryColumnMap) {
		this.queryColumnMap = queryColumnMap;
	}

	/**
	 * @return the isHiddenAllBtn
	 */
	public boolean getIsHiddenAllBtn() {
		return isHiddenAllBtn;
	}

	/**
	 * @param isHiddenAllBtn the isHiddenAllBtn to set
	 */
	public void setIsHiddenAllBtn(boolean isHiddenAllBtn) {
		this.isHiddenAllBtn = isHiddenAllBtn;
	}

	/**
	 * @return the isVendorService
	 */
	public boolean getIsVendorService() {
		return isVendorService;
	}

	/**
	 * @param isVendorService the isVendorService to set
	 */
	public void setIsVendorService(boolean isVendorService) {
		this.isVendorService = isVendorService;
	}

	/**
	 * @return the queryLocation
	 */
	public String getQueryLocation() {
		return queryLocation;
	}

	/**
	 * @param queryLocation the queryLocation to set
	 */
	public void setQueryLocation(String queryLocation) {
		this.queryLocation = queryLocation;
	}

	/**
	 * @return the hideForUpdateContactFlag
	 */
	public String getHideForUpdateContactFlag() {
		return hideForUpdateContactFlag;
	}

	/**
	 * @param hideForUpdateContactFlag the hideForUpdateContactFlag to set
	 */
	public void setHideForUpdateContactFlag(String hideForUpdateContactFlag) {
		this.hideForUpdateContactFlag = hideForUpdateContactFlag;
	}

	/**
	 * @return the queryForBack
	 */
	public boolean getQueryForBack() {
		return queryForBack;
	}

	/**
	 * @param queryForBack the queryForBack to set
	 */
	public void setQueryForBack(boolean queryForBack) {
		this.queryForBack = queryForBack;
	}

	/**
	 * @return the isCheckDtidFlag
	 */
	public String getIsCheckDtidFlag() {
		return isCheckDtidFlag;
	}

	/**
	 * @param isCheckDtidFlag the isCheckDtidFlag to set
	 */
	public void setIsCheckDtidFlag(String isCheckDtidFlag) {
		this.isCheckDtidFlag = isCheckDtidFlag;
	}

	/**
	 * @return the queryDelayRecord
	 */
	public boolean getQueryDelayRecord() {
		return queryDelayRecord;
	}

	/**
	 * @param queryDelayRecord the queryDelayRecord to set
	 */
	public void setQueryDelayRecord(boolean queryDelayRecord) {
		this.queryDelayRecord = queryDelayRecord;
	}

	/**
	 * @return the tsbCustomerId
	 */
	public String getTsbCustomerId() {
		return tsbCustomerId;
	}

	/**
	 * @param tsbCustomerId the tsbCustomerId to set
	 */
	public void setTsbCustomerId(String tsbCustomerId) {
		this.tsbCustomerId = tsbCustomerId;
	}

	/**
	 * @return the cmsFalse
	 */
	public String getCmsFalse() {
		return cmsFalse;
	}

	/**
	 * @param cmsFalse the cmsFalse to set
	 */
	public void setCmsFalse(String cmsFalse) {
		this.cmsFalse = cmsFalse;
	}

	/**
	 * @return the isCmsCreate
	 */
	public String getIsCmsCreate() {
		return isCmsCreate;
	}

	/**
	 * @param isCmsCreate the isCmsCreate to set
	 */
	public void setIsCmsCreate(String isCmsCreate) {
		this.isCmsCreate = isCmsCreate;
	}

	/**
	 * @return the queryTsbFlag
	 */
	public String getQueryTsbFlag() {
		return queryTsbFlag;
	}

	/**
	 * @param queryTsbFlag the queryTsbFlag to set
	 */
	public void setQueryTsbFlag(String queryTsbFlag) {
		this.queryTsbFlag = queryTsbFlag;
	}

	/**
	 * @return the isCmsQuery
	 */
	public String getIsCmsQuery() {
		return isCmsQuery;
	}

	/**
	 * @param isCmsQuery the isCmsQuery to set
	 */
	public void setIsCmsQuery(String isCmsQuery) {
		this.isCmsQuery = isCmsQuery;
	}

	/**
	 * @return the srmCaseTransactionDTOsStr
	 */
	public String getSrmCaseTransactionDTOsStr() {
		return srmCaseTransactionDTOsStr;
	}
	
	/**
	 * @return the isMicro
	 */
	public boolean getIsMicro() {
		return isMicro;
	}

	/**
	 * @param isMicro the isMicro to set
	 */
	public void setIsMicro(boolean isMicro) {
		this.isMicro = isMicro;
	}
	/**
	 * @param srmCaseTransactionDTOsStr the srmCaseTransactionDTOsStr to set
	 */
	public void setSrmCaseTransactionDTOsStr(String srmCaseTransactionDTOsStr) {
		this.srmCaseTransactionDTOsStr = srmCaseTransactionDTOsStr;
	}

	/**
	 * @return the callCmsMap
	 */
	public Map<String, String> getCallCmsMap() {
		return callCmsMap;
	}

	/**
	 * @param callCmsMap the callCmsMap to set
	 */
	public void setCallCmsMap(Map<String, String> callCmsMap) {
		this.callCmsMap = callCmsMap;
	}

	/**
	 * @return the sybCustomerId
	 */
	public String getSybCustomerId() {
		return sybCustomerId;
	}

	/**
	 * @param sybCustomerId the sybCustomerId to set
	 */
	public void setSybCustomerId(String sybCustomerId) {
		this.sybCustomerId = sybCustomerId;
	}

	/**
	 * @return the chbCustomerId
	 */
	public String getChbCustomerId() {
		return chbCustomerId;
	}

	/**
	 * @param chbCustomerId the chbCustomerId to set
	 */
	public void setChbCustomerId(String chbCustomerId) {
		this.chbCustomerId = chbCustomerId;
	}

	/**
	 * @return the scsbCustomerId
	 */
	public String getScsbCustomerId() {
		return scsbCustomerId;
	}

	/**
	 * @param scsbCustomerId the scsbCustomerId to set
	 */
	public void setScsbCustomerId(String scsbCustomerId) {
		this.scsbCustomerId = scsbCustomerId;
	}

	/**
	 * @return the apiAuthorizationInfoDTO
	 */
	public ApiAuthorizationInfoDTO getApiAuthorizationInfoDTO() {
		return apiAuthorizationInfoDTO;
	}

	/**
	 * @param apiAuthorizationInfoDTO the apiAuthorizationInfoDTO to set
	 */
	public void setApiAuthorizationInfoDTO(
			ApiAuthorizationInfoDTO apiAuthorizationInfoDTO) {
		this.apiAuthorizationInfoDTO = apiAuthorizationInfoDTO;
	}

	/**
	 * @return the hasArrive
	 */
	public String getHasArrive() {
		return hasArrive;
	}

	/**
	 * @param hasArrive the hasArrive to set
	 */
	public void setHasArrive(String hasArrive) {
		this.hasArrive = hasArrive;
	}

	/**
	 * @return the mobileQueryFlag
	 */
	public String getMobileQueryFlag() {
		return mobileQueryFlag;
	}

	/**
	 * @param mobileQueryFlag the mobileQueryFlag to set
	 */
	public void setMobileQueryFlag(String mobileQueryFlag) {
		this.mobileQueryFlag = mobileQueryFlag;
	}

	/**
	 * @return the uploadCoordinatedCompletionFlag
	 */
	public String getUploadCoordinatedCompletionFlag() {
		return uploadCoordinatedCompletionFlag;
	}

	/**
	 * @param uploadCoordinatedCompletionFlag the uploadCoordinatedCompletionFlag to set
	 */
	public void setUploadCoordinatedCompletionFlag(
			String uploadCoordinatedCompletionFlag) {
		this.uploadCoordinatedCompletionFlag = uploadCoordinatedCompletionFlag;
	}

	/**
	 * @return the isMicroArrive
	 */
	public boolean getIsMicroArrive() {
		return isMicroArrive;
	}

	/**
	 * @param isMicroArrive the isMicroArrive to set
	 */
	public void setIsMicroArrive(boolean isMicroArrive) {
		this.isMicroArrive = isMicroArrive;
	}

	/**
	 * @return the bccCustomerId
	 */
	public String getBccCustomerId() {
		return bccCustomerId;
	}

	/**
	 * @param bccCustomerId the bccCustomerId to set
	 */
	public void setBccCustomerId(String bccCustomerId) {
		this.bccCustomerId = bccCustomerId;
	}
	/**
	 * @return the isCustomerVendorAttribute
	 */
	public boolean getIsCustomerVendorAttribute() {
		return isCustomerVendorAttribute;
	}

	/**
	 * @param isCustomerVendorAttribute the isCustomerVendorAttribute to set
	 */
	public void setIsCustomerVendorAttribute(boolean isCustomerVendorAttribute) {
		this.isCustomerVendorAttribute = isCustomerVendorAttribute;
	}
	
}
