package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;
import java.util.Date;

import cafe.core.bean.dto.DataTransferObject;
/**
 * 
 * Purpose: SRM_案件處理記錄DTO
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016年11月10日
 * @MaintenancePersonnel CrissZhang
 */
public class SrmCaseTransactionDTO extends DataTransferObject<String> {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 3824753788939756159L;
	
	/**
	 * Purpose: 枚舉類
	 * @author CrissZhang
	 * @since  JDK 1.6
	 * @date   2017/2/14
	 * @MaintenancePersonnel CrissZhang
	 */
	public static enum ATTRIBUTE {
		TRANSACTION_ID("transactionId"),
		CASE_ID("caseId"),
		ACTION_ID("actionId"),
		ACTION_NAME("actionName"),
		CASE_STATUS("caseStatus"),
		CASE_STATUS_NAME("caseStatusName"),
		DEAL_BY_ID("dealById"),
		DEAL_BY_NAME("dealByName"),
		DEAL_DATE("dealDate"),
		DESCRIPTION("description"),
		CASE_STAGE("caseStage"),
		CASE_STAGE_NAME("caseStageName"),
		NEXT_CASE_STAGE("nextCaseStage"),
		NEXT_CASE_STAGE_NAME("nextCaseStageName"),
		DELAY_TIME("delayTime"),
		PROBLEM_REASON("problemReason"),
		PROBLEM_SOLUTION("problemSolution"),
		RESPONSIBITY("responsibity"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
		CREATED_DATE("createdDate"),
		DEPT_CODE("deptCode"),
		REQUIREMENT_NO("requirementNo"),//需求單號
		CASE_CATEGORY("caseCategory"), //案件類別
		CASE_CATEGORY_NAME("caseCategoryName"), //案件類別
		MERCHANT_CODE("merchantCode"),//特店代號
		MERCHANT_NAME("merchantName"),//特店名稱
		DTID("dtid"),//DTID
		COMMENT("comment"),//description -- 問題描述  comment -- 處理說明
		SERIAL_NUMBER("serialNumber"),//設備序號
		PROPERTY_ID("propertyId"),//財產編號
		EDC_TYPE("edcType"),//設備類別
		IS_CUP("isCup"),//銀聯卡備註
		COMPLETE_DATE("completeDate"),//實際完成日
		ADDRESS("address"),//裝機地址
		ITEM_ID_ONE("itemId1"),//周邊設備1
		SERIAL_NUMBER_ONE("serialNumber1"),//週邊設備1序號
		ITEM_ID_TWO("itemId2"),//周邊設備2
		CHECK_RESULT("checkResult"),
		CASE_TYPE("caseType"),
		EXPECTED_COMPLETION_DATE("expectedCompletionDate"),
		PROBLEM_REASON_CODE("problemReasonCode"),
		PROBLEM_SOLUTION_CODE("problemSolutionCode"),
		DEAL_DESCRIPTION("dealDescription"),
		AFTER_ACTION_STATUS("afterActionStatus"),
		UPDATE_ITEM("updateItem"),
		MAIL_INFO("mailInfo"),
		MOBILE_FLAG("mobileFlag"),
		SERIAL_NUMBER_TWO("serialNumber2"),//週邊設備2序號
		LOGISTICS_NUMBER("logisticsNumber"),//物流編號
		SIM_SERIAL_NUMBER("simSerialNumber"),
		LOGISTICS_VENDOR("logisticsVendor"),
		LOGISTICS_VENDOR_NAME("logisticsVendorName"),
		LOGISTICS_VENDOR_EMAIL("logisticsVendorEmail");
		/**
		 * value值
		 */
		private String value;
		
		/**
		 * Constructor:構造函數
		 */
		ATTRIBUTE(String value) {
			this.value = value;
		};
		
		/**
		 * @return the value
		 */
		public String getValue() {
			return this.value;
		}
	};

	/**
	 * Constructor:無參構造函數
	 */
	public SrmCaseTransactionDTO() {
	}
	/**
	 * 案件處理記錄ID
	 */
	private String transactionId;
	/**
	 * 案件編號
	 */
	private String caseId;
	/**
	 * 執行動作
	 */
	private String actionId;
	/**
	 * 執行動作名稱
	 */
	private String actionName;
	/**
	 * 案件狀態
	 */
	private String caseStatus;
	/**
	 * 案件狀態名稱
	 */
	private String caseStatusName;
	/**
	 * 動作後狀態 別名
	 */
	private String afterActionStatus;
	/**
	 * 處理人員ID
	 */
	private String dealById;
	/**
	 * 處理人員ID
	 */
	private String dealByName;
	/**
	 * 實際處理日期
	 */
	private Timestamp dealDate;
	/**
	 * 處理說明
	 */
	private String description;
	/**
	 * 處理說明 別名
	 */
	private String dealDescription;
	/**
	 * 當前關卡
	 */
	private String caseStage;
	/**
	 * 當前關卡名稱
	 */
	private String caseStageName;
	/**
	 * 下一關關卡代碼
	 */
	private String nextCaseStage;
	/**
	 * 下一關關卡代碼
	 */
	private String nextCaseStageName;
	/**
	 * 延期時間
	 */
	private Timestamp delayTime;
	/**
	 * 問題原因
	 */
	private String problemReason;
	/**
	 * 問題原因名稱
	 */
	private String problemReasonName;
	/**
	 * 問題解決方式
	 */
	private String problemSolution;
	/**
	 * 問題解決方式名稱
	 */
	private String problemSolutionName;
	/**
	 * 責任歸屬
	 */
	private String responsibity;
	/**
	 * 責任歸屬名稱
	 */
	private String responsibityName;
	/**
	 * 創建人員編號
	 */
	private String createdById;
	/**
	 * 創建人員名稱
	 */
	private String createdByName;
	/**
	 * 新增日期
	 */
	private Timestamp createdDate;
	/**
	 * 案件動作
	 */
	private String caseActionId;
	/**
	 * 動作後狀態
	 */
	private String afterCaseStatus;
	/**
	 * 派工單位
	 */
	private String dispatchUnit;
	/**
	 * 查核結果
	 */
	private String checkResult;
	/**
	 * 查核結果名稱
	 */
	private String checkResultName;
	/**
	 * 線上排除所選的處理代理工程師
	 */
	private String agentsId;
	/**
	 * 線上排除所選的處理代理工程師
	 */
	private String agentsName;
	/**
	 * 案件類型
	 */
	private String caseType;
	/**
	 * 預計完成日
	 */
	private Timestamp expectedCompleteDate;
	/**
	 * 處理單位
	 */
	private String deptCode;

	/**
	 * 案件類別
	 */
	private String caseCategory;
	/**
	 * 郵件通知種類
	 */
	private String noticeType;
	/**
	 * 郵件通知提醒時間起
	 */
	private String remindStart;
	/**
	 * 郵件通知提醒時間迄
	 */
	private String remindEnd;
	/**
	 * 郵件通知提醒收件人
	 */
	private String toMail;
	/**
	 * 需求單號
	 */
	private String requirementNo;
	/**
	 * 案件類別名稱
	 */
	private String caseCategoryName;
	/**
	 * 特店代號	
	 */
	private String merchantCode;
	/**
	 * 特點名稱
	 */
	private String merchantName;
	/**
	 * DTID
	 */
	private String dtid;
	/**
	 * 處理說明
	 */
	private String comment;
	/**
	 * 設備序號
	 */
	private String serialNumber;
	/**
	 * 財產編號
	 */
	private String propertyId;
	/**
	 * 設備類別
	 */
	private String edcType;
	/**
	 * 銀聯卡備註
	 */
	private String isCup;
	/**
	 * 實際完成日
	 */
	private Timestamp completeDate;
	/**
	 * 裝機地址
	 */
	private String address;
	/**
	 * 周邊設備1
	 */
	private String itemId1;
	/**
	 * 周邊設備序號1
	 */
	private String serialNumber1;
	/**
	 * 周邊設備2
	 */
	private String itemId2;
	/**
	 * 周邊設備序號2
	 */
	private String serialNumber2;
	/**
	 * 問題原因code
	 */
	private String problemReasonCode;
	/**
	 * 問題解決方式code
	 */
	private String problemSolutionCode;
	/**
	 * 預計完成日
	 */
	private Date expectedCompletionDate;
	/**
	 * 異動項目
	 */
	private String updateItem;
	/**
	 * 頁面記錄案件狀態，用於後台驗證狀態是否與數據庫一致
	 */
	private String nowCaseStatus;
	/**
	 * 頁面記錄更新時間，用於後台驗證更新時間是否與數據庫一致
	 */
	private String updatedDateString;
	/**
	 * 派工單位名稱
	 */
	private String deptName;
	/**
	 * mail的一些信息
	 */
	private String mailInfo;
	/**
	 * 是否為移動版
	 */
	private String mobileFlag;
	/**
	 * 物流編號
	 */
	private String logisticsNumber;
	/**
	 * 設備序號(SIM卡)
	 */
	private String simSerialNumber;
	/**
	 * 物流廠商
	 */
	private String logisticsVendor;
	/**
	 * 物流廠商(中文名稱)
	 */
	private String logisticsVendorName;
	/**
	 * 物流Email
	 */
	private String logisticsVendorEmail;

	/**
	 * Constructor:有參構造函數
	 */
	public SrmCaseTransactionDTO(String transactionId, String caseId,
			String actionId, String caseStatus, String dealById,
			String dealByName, Timestamp dealDate, String description,
			String caseStage, String caseStageName, String nextCaseStage,
			String nextCaseStageName, Timestamp delayTime, String problemReason,
			String problemSolution, String responsibity, String createdById,
			String createdByName, Timestamp createdDate, String serialNumber,
			String propertyId, String edcType, String isCup, Timestamp completeDate,
			String address, String itemId1, String serialNumber1, String itemId2, String serialNumber2,
			String logisticsNumber) {
		super();
		this.transactionId = transactionId;
		this.caseId = caseId;
		this.actionId = actionId;
		this.caseStatus = caseStatus;
		this.dealById = dealById;
		this.dealByName = dealByName;
		this.dealDate = dealDate;
		this.description = description;
		this.caseStage = caseStage;
		this.caseStageName = caseStageName;
		this.nextCaseStage = nextCaseStage;
		this.nextCaseStageName = nextCaseStageName;
		this.delayTime = delayTime;
		this.problemReason = problemReason;
		this.problemSolution = problemSolution;
		this.responsibity = responsibity;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
		this.serialNumber = serialNumber;
		this.propertyId = propertyId;
		this.edcType = edcType;
		this.isCup = isCup;
		this.completeDate = completeDate;
		this.address = address;
		this.itemId1 = itemId1;
		this.serialNumber1 = serialNumber1;
		this.itemId2 = itemId2;
		this.serialNumber2 = serialNumber2;
		this.logisticsNumber = logisticsNumber;
	}

	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
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
	 * @return the actionId
	 */
	public String getActionId() {
		return actionId;
	}

	/**
	 * @param actionId the actionId to set
	 */
	public void setActionId(String actionId) {
		this.actionId = actionId;
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
	 * @return the dealById
	 */
	public String getDealById() {
		return dealById;
	}

	/**
	 * @param dealById the dealById to set
	 */
	public void setDealById(String dealById) {
		this.dealById = dealById;
	}

	/**
	 * @return the dealByName
	 */
	public String getDealByName() {
		return dealByName;
	}

	/**
	 * @param dealByName the dealByName to set
	 */
	public void setDealByName(String dealByName) {
		this.dealByName = dealByName;
	}

	/**
	 * @return the dealDate
	 */
	public Timestamp getDealDate() {
		return dealDate;
	}

	/**
	 * @param dealDate the dealDate to set
	 */
	public void setDealDate(Timestamp dealDate) {
		this.dealDate = dealDate;
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
	 * @return the caseStage
	 */
	public String getCaseStage() {
		return caseStage;
	}

	/**
	 * @param caseStage the caseStage to set
	 */
	public void setCaseStage(String caseStage) {
		this.caseStage = caseStage;
	}

	/**
	 * @return the caseStageName
	 */
	public String getCaseStageName() {
		return caseStageName;
	}

	/**
	 * @param caseStageName the caseStageName to set
	 */
	public void setCaseStageName(String caseStageName) {
		this.caseStageName = caseStageName;
	}

	/**
	 * @return the nextCaseStage
	 */
	public String getNextCaseStage() {
		return nextCaseStage;
	}

	/**
	 * @param nextCaseStage the nextCaseStage to set
	 */
	public void setNextCaseStage(String nextCaseStage) {
		this.nextCaseStage = nextCaseStage;
	}

	/**
	 * @return the nextCaseStageName
	 */
	public String getNextCaseStageName() {
		return nextCaseStageName;
	}

	/**
	 * @param nextCaseStageName the nextCaseStageName to set
	 */
	public void setNextCaseStageName(String nextCaseStageName) {
		this.nextCaseStageName = nextCaseStageName;
	}

	/**
	 * @return the delayTime
	 */
	public Timestamp getDelayTime() {
		return delayTime;
	}

	/**
	 * @param delayTime the delayTime to set
	 */
	public void setDelayTime(Timestamp delayTime) {
		this.delayTime = delayTime;
	}

	/**
	 * @return the problemReason
	 */
	public String getProblemReason() {
		return problemReason;
	}

	/**
	 * @param problemReason the problemReason to set
	 */
	public void setProblemReason(String problemReason) {
		this.problemReason = problemReason;
	}

	/**
	 * @return the problemSolution
	 */
	public String getProblemSolution() {
		return problemSolution;
	}

	/**
	 * @param problemSolution the problemSolution to set
	 */
	public void setProblemSolution(String problemSolution) {
		this.problemSolution = problemSolution;
	}

	/**
	 * @return the responsibity
	 */
	public String getResponsibity() {
		return responsibity;
	}

	/**
	 * @param responsibity the responsibity to set
	 */
	public void setResponsibity(String responsibity) {
		this.responsibity = responsibity;
	}

	/**
	 * @return the createdById
	 */
	public String getCreatedById() {
		return createdById;
	}

	/**
	 * @param createdById the createdById to set
	 */
	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}

	/**
	 * @return the createdByName
	 */
	public String getCreatedByName() {
		return createdByName;
	}

	/**
	 * @param createdByName the createdByName to set
	 */
	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	/**
	 * @return the createdDate
	 */
	public Timestamp getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
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
	 * @return the afterCaseStatus
	 */
	public String getAfterCaseStatus() {
		return afterCaseStatus;
	}

	/**
	 * @param afterCaseStatus the afterCaseStatus to set
	 */
	public void setAfterCaseStatus(String afterCaseStatus) {
		this.afterCaseStatus = afterCaseStatus;
	}

	/**
	 * @return the dispatchUnit
	 */
	public String getDispatchUnit() {
		return dispatchUnit;
	}

	/**
	 * @param dispatchUnit the dispatchUnit to set
	 */
	public void setDispatchUnit(String dispatchUnit) {
		this.dispatchUnit = dispatchUnit;
	}

	/**
	 * @return the checkResult
	 */
	public String getCheckResult() {
		return checkResult;
	}

	/**
	 * @param checkResult the checkResult to set
	 */
	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	/**
	 * @return the caseType
	 */
	public String getCaseType() {
		return caseType;
	}

	/**
	 * @param caseType the caseType to set
	 */
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	/**
	 * @return the expectedCompleteDate
	 */
	public Timestamp getExpectedCompleteDate() {
		return expectedCompleteDate;
	}

	/**
	 * @param expectedCompleteDate the expectedCompleteDate to set
	 */
	public void setExpectedCompleteDate(Timestamp expectedCompleteDate) {
		this.expectedCompleteDate = expectedCompleteDate;
	}

	/**
	 * @return the actionName
	 */
	public String getActionName() {
		return actionName;
	}

	/**
	 * @param actionName the actionName to set
	 */
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	/**
	 * @return the caseStatusName
	 */
	public String getCaseStatusName() {
		return caseStatusName;
	}

	/**
	 * @param caseStatusName the caseStatusName to set
	 */
	public void setCaseStatusName(String caseStatusName) {
		this.caseStatusName = caseStatusName;
	}

	/**
	 * @return the deptCode
	 */
	public String getDeptCode() {
		return deptCode;
	}

	/**
	 * @param deptCode the deptCode to set
	 */
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
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
	 * @return the problemReasonName
	 */
	public String getProblemReasonName() {
		return problemReasonName;
	}

	/**
	 * @param problemReasonName the problemReasonName to set
	 */
	public void setProblemReasonName(String problemReasonName) {
		this.problemReasonName = problemReasonName;
	}

	/**
	 * @return the problemSolutionName
	 */
	public String getProblemSolutionName() {
		return problemSolutionName;
	}

	/**
	 * @param problemSolutionName the problemSolutionName to set
	 */
	public void setProblemSolutionName(String problemSolutionName) {
		this.problemSolutionName = problemSolutionName;
	}

	/**
	 * @return the responsibityName
	 */
	public String getResponsibityName() {
		return responsibityName;
	}

	/**
	 * @param responsibityName the responsibityName to set
	 */
	public void setResponsibityName(String responsibityName) {
		this.responsibityName = responsibityName;
	}

	/**
	 * @return the checkResultName
	 */
	public String getCheckResultName() {
		return checkResultName;
	}

	/**
	 * @param checkResultName the checkResultName to set
	 */
	public void setCheckResultName(String checkResultName) {
		this.checkResultName = checkResultName;
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
	 * @return the requirementNo
	 */
	public String getRequirementNo() {
		return requirementNo;
	}

	/**
	 * @param requirementNo the requirementNo to set
	 */
	public void setRequirementNo(String requirementNo) {
		this.requirementNo = requirementNo;
	}

	/**
	 * @return the caseCategoryName
	 */
	public String getCaseCategoryName() {
		return caseCategoryName;
	}

	/**
	 * @param caseCategoryName the caseCategoryName to set
	 */
	public void setCaseCategoryName(String caseCategoryName) {
		this.caseCategoryName = caseCategoryName;
	}

	/**
	 * @return the merchantCode
	 */
	public String getMerchantCode() {
		return merchantCode;
	}

	/**
	 * @param merchantCode the merchantCode to set
	 */
	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	/**
	 * @return the merchantName
	 */
	public String getMerchantName() {
		return merchantName;
	}

	/**
	 * @param merchantName the merchantName to set
	 */
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
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
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	/**
	 * @return the propertyId
	 */
	public String getPropertyId() {
		return propertyId;
	}

	/**
	 * @param propertyId the propertyId to set
	 */
	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	/**
	 * @return the edcType
	 */
	public String getEdcType() {
		return edcType;
	}

	/**
	 * @param edcType the edcType to set
	 */
	public void setEdcType(String edcType) {
		this.edcType = edcType;
	}

	/**
	 * @return the isCup
	 */
	public String getIsCup() {
		return isCup;
	}

	/**
	 * @param isCup the isCup to set
	 */
	public void setIsCup(String isCup) {
		this.isCup = isCup;
	}

	/**
	 * @return the completeDate
	 */
	public Timestamp getCompleteDate() {
		return completeDate;
	}

	/**
	 * @param completeDate the completeDate to set
	 */
	public void setCompleteDate(Timestamp completeDate) {
		this.completeDate = completeDate;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the itemId1
	 */
	public String getItemId1() {
		return itemId1;
	}

	/**
	 * @param itemId1 the itemId1 to set
	 */
	public void setItemId1(String itemId1) {
		this.itemId1 = itemId1;
	}

	/**
	 * @return the serialNumber1
	 */
	public String getSerialNumber1() {
		return serialNumber1;
	}

	/**
	 * @param serialNumber1 the serialNumber1 to set
	 */
	public void setSerialNumber1(String serialNumber1) {
		this.serialNumber1 = serialNumber1;
	}

	/**
	 * @return the itemId2
	 */
	public String getItemId2() {
		return itemId2;
	}

	/**
	 * @param itemId2 the itemId2 to set
	 */
	public void setItemId2(String itemId2) {
		this.itemId2 = itemId2;
	}

	/**
	 * @return the serialNumber2
	 */
	public String getSerialNumber2() {
		return serialNumber2;
	}

	/**
	 * @param serialNumber2 the serialNumber2 to set
	 */
	public void setSerialNumber2(String serialNumber2) {
		this.serialNumber2 = serialNumber2;
	}

	/**
	 * @return the problemReasonCode
	 */
	public String getProblemReasonCode() {
		return problemReasonCode;
	}

	/**
	 * @return the problemSolutionCode
	 */
	public String getProblemSolutionCode() {
		return problemSolutionCode;
	}

	/**
	 * @param problemReasonCode the problemReasonCode to set
	 */
	public void setProblemReasonCode(String problemReasonCode) {
		this.problemReasonCode = problemReasonCode;
	}

	/**
	 * @param problemSolutionCode the problemSolutionCode to set
	 */
	public void setProblemSolutionCode(String problemSolutionCode) {
		this.problemSolutionCode = problemSolutionCode;
	}

	/**
	 * @return the expectedCompletionDate
	 */
	public Date getExpectedCompletionDate() {
		return expectedCompletionDate;
	}

	/**
	 * @param expectedCompletionDate the expectedCompletionDate to set
	 */
	public void setExpectedCompletionDate(Date expectedCompletionDate) {
		this.expectedCompletionDate = expectedCompletionDate;
	}

	/**
	 * @return the updateItem
	 */
	public String getUpdateItem() {
		return updateItem;
	}

	/**
	 * @param updateItem the updateItem to set
	 */
	public void setUpdateItem(String updateItem) {
		this.updateItem = updateItem;
	}

	/**
	 * @return the afterActionStatus
	 */
	public String getAfterActionStatus() {
		return afterActionStatus;
	}

	/**
	 * @return the dealDescription
	 */
	public String getDealDescription() {
		return dealDescription;
	}

	/**
	 * @param afterActionStatus the afterActionStatus to set
	 */
	public void setAfterActionStatus(String afterActionStatus) {
		this.afterActionStatus = afterActionStatus;
	}

	/**
	 * @param dealDescription the dealDescription to set
	 */
	public void setDealDescription(String dealDescription) {
		this.dealDescription = dealDescription;
	}

	/**
	 * @return the nowCaseStatus
	 */
	public String getNowCaseStatus() {
		return nowCaseStatus;
	}

	/**
	 * @param nowCaseStatus the nowCaseStatus to set
	 */
	public void setNowCaseStatus(String nowCaseStatus) {
		this.nowCaseStatus = nowCaseStatus;
	}

	/**
	 * @return the updateDateString
	 */
	public String getUpdatedDateString() {
		return updatedDateString;
	}

	/**
	 * @param updateDateString the updateDateString to set
	 */
	public void setUpdatedDateString(String updatedDateString) {
		this.updatedDateString = updatedDateString;
	}

	/**
	 * @return the deptName
	 */
	public String getDeptName() {
		return deptName;
	}

	/**
	 * @param deptName the deptName to set
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	/**
	 * @return the agentsName
	 */
	public String getAgentsName() {
		return agentsName;
	}

	/**
	 * @param agentsName the agentsName to set
	 */
	public void setAgentsName(String agentsName) {
		this.agentsName = agentsName;
	}

	/**
	 * @return the agentsId
	 */
	public String getAgentsId() {
		return agentsId;
	}

	/**
	 * @param agentsId the agentsId to set
	 */
	public void setAgentsId(String agentsId) {
		this.agentsId = agentsId;
	}

	/**
	 * @return the mailInfo
	 */
	public String getMailInfo() {
		return mailInfo;
	}

	/**
	 * @param mailInfo the mailInfo to set
	 */
	public void setMailInfo(String mailInfo) {
		this.mailInfo = mailInfo;
	}
	/**
	 * @return the mobileFlag
	 */
	public String getMobileFlag() {
		return mobileFlag;
	}

	/**
	 * @param mobileFlag the mobileFlag to set
	 */
	public void setMobileFlag(String mobileFlag) {
		this.mobileFlag = mobileFlag;
	}

	public String getLogisticsNumber() {
		return logisticsNumber;
	}

	public void setLogisticsNumber(String logisticsNumber) {
		this.logisticsNumber = logisticsNumber;
	}

	/**
	 * @return the simSerialNumber
	 */
	public String getSimSerialNumber() {
		return simSerialNumber;
	}

	/**
	 * @param simSerialNumber the simSerialNumber to set
	 */
	public void setSimSerialNumber(String simSerialNumber) {
		this.simSerialNumber = simSerialNumber;
	}

	/**
	 * @return the logisticsVendor
	 */
	public String getLogisticsVendor() {
		return logisticsVendor;
	}

	/**
	 * @param logisticsVendor the logisticsVendor to set
	 */
	public void setLogisticsVendor(String logisticsVendor) {
		this.logisticsVendor = logisticsVendor;
	}

	/**
	 * @return the logisticsVendorName
	 */
	public String getLogisticsVendorName() {
		return logisticsVendorName;
	}

	/**
	 * @param logisticsVendorName the logisticsVendorName to set
	 */
	public void setLogisticsVendorName(String logisticsVendorName) {
		this.logisticsVendorName = logisticsVendorName;
	}

	/**
	 * @return the logisticsVendorEmail
	 */
	public String getLogisticsVendorEmail() {
		return logisticsVendorEmail;
	}

	/**
	 * @param logisticsVendorEmail the logisticsVendorEmail to set
	 */
	public void setLogisticsVendorEmail(String logisticsVendorEmail) {
		this.logisticsVendorEmail = logisticsVendorEmail;
	}
	
}
