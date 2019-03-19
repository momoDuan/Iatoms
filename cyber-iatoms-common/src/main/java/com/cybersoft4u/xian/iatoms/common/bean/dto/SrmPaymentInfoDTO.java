package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import cafe.core.bean.dto.DataTransferObject;
/**
 * Purpose: 求償資料檔DTO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/2/10
 * @MaintenancePersonnel CarrieDuan
 */
public class SrmPaymentInfoDTO extends DataTransferObject<String> {
	
	public static enum ATTRIBUTE {
		PAYMENT_ID("paymentId"),
		ITEM_ID("itemId"),
		CUSTOMER_ID("customerId"),
		CUSTOMER_NAME("customerName"),
		DTID("dtid"),
		REQUIREMENT_NO("requirementNo"),
		CASE_ID("caseId"),
		TID("tid"),
		MERCHANT_ID("merchantId"),
		MERCHANT_CODE("merchantCode"),
		NAME("name"),
		REAL_FINISH_DATE("realFinishDate"),
		CASE_CREATED_DATE("caseCreatedDate"),
		STATUS("status"),
		STATUS_NAME("statusName"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
		CREATED_DATE("createdDate"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		UPDATED_DATE("updatedDate"),
		PAYMENT_ITEM_NAME("paymentItemName"),
		PAYMENT_ITEM("paymentItem"),
		ITEM_NAME("itemName"),
		CONTRACT_CODE("contractCode"),
		SUPPLIES_AMOUNT("suppliesAmount"),
		PAYMENT_REASON("paymentReason"),
		PAYMENT_REASON_NAME("paymentReasonName"),
		CHECK_RESULT("checkResult"),
		CHECK_USER("checkUser"),
		FEE("fee"),
		FEE_DESC("feeDesc"),
		IS_ASK_PAY("isAskPay"),
		PAYMENT_TYPE("paymentType"),
		PAYMENT_TYPE_DESC("paymentTypeDesc"),
		ASK_PAY_DATE("askPayDate"),
		ASK_PAY_DESC("askPayDesc"),
		CANCEL_DATE("cancelDate"),
		REPAYMENT_DATE("repaymentDate"),
		CANCEL_DESC("cancelDesc"),
		SERIAL_NUMBER("serialNumber"),
		PAY_INFO("payInfo"),
		REASON_DETAIL("reasonDetail"),
		SUPPLIES_TYPE("suppliesType"),
		SUPPLIES_TYPE_NAME("suppliesTypeName"),
		SUPPLIES_NAME("suppliesName"),
		IS_PAY("isPay"),
		AO_NAME("aoName"),
		LOCKED_DATE("lockedDate"),
		COMPANY_CODE("companyCode"),
		ROW_ID("rowId"),
		CASE_CATEGORY("caseCategory"),
		CMS_CASE("cmsCase"),
		REPAIR_REASON("repairReason"),
		MERCHANT_HEADER_ID("merchantHeaderId"),
		DISPATCH_DATE("dispatchDate"),
		IS_IATOMS_CREATE_CMS("isIatomsCreateCms"),
		EDC_TYPE("edcType");

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	
	/**
	 * 求償批號
	 */
	private String paymentId;
	/**
	 * 求償項目編號
	 */
	private String itemId;
	/**
	 * 客戶
	 */
	private String customerId;
	/**
	 * 客戶名稱
	 */
	private String customerName;
	/**
	 * DTID
	 */
	private String dtid;
	/**
	 * 需求單號
	 */
	private String requirementNo;
	/**
	 * 案件編號
	 */
	private String caseId;
	/**
	 * TID
	 */
	private String tid;
	/**
	 * 特店Id
	 */
	private String merchantId;
	/**
	 * 特點代號
	 */
	private String merchantCode;
	/**
	 * 特點名稱
	 */
	private String name;
	/**
	 * 通報遺失日
	 */
	private Date realFinishDate;
	/**
	 * 進件時間
	 */
	private Timestamp caseCreatedDate;
	/**
	 * 狀態
	 */
	private String status;
	/**
	 * 狀態-名稱
	 */
	private String statusName;
	/**
	 * 新增人員ID
	 */
	private String createdById;
	/**
	 * 新增人員姓名
	 */
	private String createdByName;
	/**
	 * 新增日期
	 */
	private Date createdDate;
	/**
	 * 異動人員ID
	 */
	private String updatedById;
	/**
	 * 異動人員姓名
	 */
	private String updatedByName;
	/**
	 * 異動日期
	 */
	private Timestamp updatedDate;
	/**
	 * 求償項目-名稱
	 */
	private String paymentItemName;
	/**
	 * 求償項目
	 */
	private String paymentItem;
	/**
	 * 設備名稱/耗材項目
	 */
	private String itemName;
	/**
	 * 合約編號
	 */
	private String contractCode;
	/**
	 * 耗材數量
	 */
	private Integer suppliesAmount;
	/**
	 * 求償資訊
	 */
	private String paymentReason;
	/**
	 * 求償資訊自行輸入
	 */
	private String reasonDetail;
	/**
	 * 求償資訊-名稱
	 */
	private String paymentReasonName;
	/**
	 * 檢測結果
	 */
	private String checkResult;
	/**
	 * 檢測人
	 */
	private String checkUser;
	/**
	 * 求償費用
	 */
	private BigDecimal fee;
	/**
	 * 求償費用說明
	 */
	private String feeDesc;
	/**
	 * 是否需要請款
	 */
	private String isAskPay;
	/**
	 * 求償方式
	 */
	private String paymentType;
	/**
	 * 求償方式說明
	 */
	private String paymentTypeDesc;
	/**
	 * 請款時間
	 */
	private Date askPayDate;
	/**
	 * 請款說明
	 */
	private String askPayDesc;
	/**
	 * 取消時間
	 */
	private Date cancelDate;
	/**
	 * 還款時間
	 */
	private Date repaymentDate;
	/**
	 * 取消說明
	 */
	private String cancelDesc;
	/**
	 * 設備序號
	 */
	private String serialNumber;
	/**
	 * 求償資訊-頁面顯示
	 */
	private String payInfo;
	/**
	 * 耗材分類名稱
	 */
	private String suppliesTypeName;
	/**
	 * 耗材分類
	 */
	private String suppliesType;
	/**
	 * 耗材品名稱
	 */
	private String suppliesName;
	/**
	 * 是否求償
	 */
	private String isPay;
	/**
	 * 
	 */
	private String aoName;
	/**
	 * 鎖定時間
	 */
	private Timestamp lockedDate;
	/**
	 * 公司 code
	 */
	private	String companyCode;
	/**
	 * 匯出序號
	 */
	private String index;
	/**
	 * 特點表頭id
	 */
	private String merchantHeaderId;
	/**
	 * 匯出報表的rowId
	 */
	private String rowId;
	/**
	 * 是否为CMS案件
	 */
	private String cmsCase;
	/**
	 * 案件类别
	 */
	private String caseCategory;
	/**
	 * 報修原因
	 */
	private String repairReason;
	/**
	 * 派工日期
	 */
	private Timestamp dispatchDate;
	/**
	 * 是否爲iatoms建案
	 */
	private String isIatomsCreateCms;
	/**
	 * 案件刷卡機型
	 */
	private String edcType;
	/**
	 * Constructor: 無參構造函數
	 */
	public SrmPaymentInfoDTO() {
		super();
	}
	
	
	
	/**
	 * Constructor:有參構造函數
	 */
	public SrmPaymentInfoDTO(String paymentId, String customerId, String dtid,
			String requirementNo, String caseId, String tid, String merchantId,
			String merchantCode, String name, Date realFinishDate,
			Timestamp caseCreatedDate, String status, String createdById,
			String createdByName, Date createdDate, String updatedById,
			String updatedByName, Timestamp updatedDate, String paymentItemName,
			String itemName, String contractCode, Integer suppliesAmount,
			String paymentReason, String checkResult, String checkUser,
			BigDecimal fee, String feeDesc, String isAskPay,
			String paymentType, String paymentTypeDesc, Date askPayDate,
			String askPayDesc, Date cancelDate, Date repaymentDate,
			String cancelDesc) {
		super();
		this.paymentId = paymentId;
		this.customerId = customerId;
		this.dtid = dtid;
		this.requirementNo = requirementNo;
		this.caseId = caseId;
		this.tid = tid;
		this.merchantId = merchantId;
		this.merchantCode = merchantCode;
		this.name = name;
		this.realFinishDate = realFinishDate;
		this.caseCreatedDate = caseCreatedDate;
		this.status = status;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
		this.paymentItemName = paymentItemName;
		this.itemName = itemName;
		this.contractCode = contractCode;
		this.suppliesAmount = suppliesAmount;
		this.paymentReason = paymentReason;
		this.checkResult = checkResult;
		this.checkUser = checkUser;
		this.fee = fee;
		this.feeDesc = feeDesc;
		this.isAskPay = isAskPay;
		this.paymentType = paymentType;
		this.paymentTypeDesc = paymentTypeDesc;
		this.askPayDate = askPayDate;
		this.askPayDesc = askPayDesc;
		this.cancelDate = cancelDate;
		this.repaymentDate = repaymentDate;
		this.cancelDesc = cancelDesc;
	}



	/**
	 * @return the paymentId
	 */
	public String getPaymentId() {
		return paymentId;
	}
	/**
	 * @param paymentId the paymentId to set
	 */
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
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
	 * @return the tid
	 */
	public String getTid() {
		return tid;
	}
	/**
	 * @param tid the tid to set
	 */
	public void setTid(String tid) {
		this.tid = tid;
	}
	/**
	 * @return the merchantId
	 */
	public String getMerchantId() {
		return merchantId;
	}
	/**
	 * @param merchantId the merchantId to set
	 */
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	/**
	 * @return the realFinishDate
	 */
	public Date getRealFinishDate() {
		return realFinishDate;
	}
	/**
	 * @param realFinishDate the realFinishDate to set
	 */
	public void setRealFinishDate(Date realFinishDate) {
		this.realFinishDate = realFinishDate;
	}
	/**
	 * @return the caseCreatedDate
	 */
	public Timestamp getCaseCreatedDate() {
		return caseCreatedDate;
	}
	/**
	 * @param caseCreatedDate the caseCreatedDate to set
	 */
	public void setCaseCreatedDate(Timestamp caseCreatedDate) {
		this.caseCreatedDate = caseCreatedDate;
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
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return the updatedById
	 */
	public String getUpdatedById() {
		return updatedById;
	}
	/**
	 * @param updatedById the updatedById to set
	 */
	public void setUpdatedById(String updatedById) {
		this.updatedById = updatedById;
	}
	/**
	 * @return the updatedByName
	 */
	public String getUpdatedByName() {
		return updatedByName;
	}
	/**
	 * @param updatedByName the updatedByName to set
	 */
	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}
	/**
	 * @return the updatedDate
	 */
	public Timestamp getUpdatedDate() {
		return updatedDate;
	}
	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the paymentItemName
	 */
	public String getPaymentItemName() {
		return paymentItemName;
	}
	/**
	 * @param paymentItemName the paymentItemName to set
	 */
	public void setPaymentItemName(String paymentItemName) {
		this.paymentItemName = paymentItemName;
	}
	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}
	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	/**
	 * @return the contractCode
	 */
	public String getContractCode() {
		return contractCode;
	}
	/**
	 * @param contractCode the contractCode to set
	 */
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	/**
	 * @return the suppliesAmount
	 */
	public Integer getSuppliesAmount() {
		return suppliesAmount;
	}
	/**
	 * @param suppliesAmount the suppliesAmount to set
	 */
	public void setSuppliesAmount(Integer suppliesAmount) {
		this.suppliesAmount = suppliesAmount;
	}
	/**
	 * @return the paymentReason
	 */
	public String getPaymentReason() {
		return paymentReason;
	}
	/**
	 * @param paymentReason the paymentReason to set
	 */
	public void setPaymentReason(String paymentReason) {
		this.paymentReason = paymentReason;
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
	 * @return the checkUser
	 */
	public String getCheckUser() {
		return checkUser;
	}
	/**
	 * @param checkUser the checkUser to set
	 */
	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}
	/**
	 * @return the fee
	 */
	public BigDecimal getFee() {
		return fee;
	}
	/**
	 * @param fee the fee to set
	 */
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	/**
	 * @return the feeDesc
	 */
	public String getFeeDesc() {
		return feeDesc;
	}
	/**
	 * @param feeDesc the feeDesc to set
	 */
	public void setFeeDesc(String feeDesc) {
		this.feeDesc = feeDesc;
	}
	/**
	 * @return the isAskPay
	 */
	public String getIsAskPay() {
		return isAskPay;
	}
	/**
	 * @param isAskPay the isAskPay to set
	 */
	public void setIsAskPay(String isAskPay) {
		this.isAskPay = isAskPay;
	}
	/**
	 * @return the paymentType
	 */
	public String getPaymentType() {
		return paymentType;
	}
	/**
	 * @param paymentType the paymentType to set
	 */
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	/**
	 * @return the paymentTypeDesc
	 */
	public String getPaymentTypeDesc() {
		return paymentTypeDesc;
	}
	/**
	 * @param paymentTypeDesc the paymentTypeDesc to set
	 */
	public void setPaymentTypeDesc(String paymentTypeDesc) {
		this.paymentTypeDesc = paymentTypeDesc;
	}
	/**
	 * @return the askPayDate
	 */
	public Date getAskPayDate() {
		return askPayDate;
	}
	/**
	 * @param askPayDate the askPayDate to set
	 */
	public void setAskPayDate(Date askPayDate) {
		this.askPayDate = askPayDate;
	}
	/**
	 * @return the askPayDesc
	 */
	public String getAskPayDesc() {
		return askPayDesc;
	}
	/**
	 * @param askPayDesc the askPayDesc to set
	 */
	public void setAskPayDesc(String askPayDesc) {
		this.askPayDesc = askPayDesc;
	}
	/**
	 * @return the cancelDate
	 */
	public Date getCancelDate() {
		return cancelDate;
	}
	/**
	 * @param cancelDate the cancelDate to set
	 */
	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}
	/**
	 * @return the repaymentDate
	 */
	public Date getRepaymentDate() {
		return repaymentDate;
	}
	/**
	 * @param repaymentDate the repaymentDate to set
	 */
	public void setRepaymentDate(Date repaymentDate) {
		this.repaymentDate = repaymentDate;
	}
	/**
	 * @return the cancelDesc
	 */
	public String getCancelDesc() {
		return cancelDesc;
	}
	/**
	 * @param cancelDesc the cancelDesc to set
	 */
	public void setCancelDesc(String cancelDesc) {
		this.cancelDesc = cancelDesc;
	}
	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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
	 * @return the paymentReasonName
	 */
	public String getPaymentReasonName() {
		return paymentReasonName;
	}
	/**
	 * @param paymentReasonName the paymentReasonName to set
	 */
	public void setPaymentReasonName(String paymentReasonName) {
		this.paymentReasonName = paymentReasonName;
	}
	/**
	 * @return the statusName
	 */
	public String getStatusName() {
		return statusName;
	}
	/**
	 * @param statusName the statusName to set
	 */
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	/**
	 * @return the paymentItem
	 */
	public String getPaymentItem() {
		return paymentItem;
	}
	/**
	 * @param paymentItem the paymentItem to set
	 */
	public void setPaymentItem(String paymentItem) {
		this.paymentItem = paymentItem;
	}



	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}

	

	/**
	 * @return the payInfo
	 */
	public String getPayInfo() {
		return payInfo;
	}



	/**
	 * @param payInfo the payInfo to set
	 */
	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;
	}



	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}



	/**
	 * @return the reasonDetail
	 */
	public String getReasonDetail() {
		return reasonDetail;
	}



	/**
	 * @param reasonDetail the reasonDetail to set
	 */
	public void setReasonDetail(String reasonDetail) {
		this.reasonDetail = reasonDetail;
	}



	/**
	 * @return the suppliesTypeName
	 */
	public String getSuppliesTypeName() {
		return suppliesTypeName;
	}



	/**
	 * @param suppliesTypeName the suppliesTypeName to set
	 */
	public void setSuppliesTypeName(String suppliesTypeName) {
		this.suppliesTypeName = suppliesTypeName;
	}



	/**
	 * @return the suppliesType
	 */
	public String getSuppliesType() {
		return suppliesType;
	}



	/**
	 * @param suppliesType the suppliesType to set
	 */
	public void setSuppliesType(String suppliesType) {
		this.suppliesType = suppliesType;
	}



	/**
	 * @return the suppliesName
	 */
	public String getSuppliesName() {
		return suppliesName;
	}



	/**
	 * @param suppliesName the suppliesName to set
	 */
	public void setSuppliesName(String suppliesName) {
		this.suppliesName = suppliesName;
	}



	/**
	 * @return the isPay
	 */
	public String getIsPay() {
		return isPay;
	}



	/**
	 * @param isPay the isPay to set
	 */
	public void setIsPay(String isPay) {
		this.isPay = isPay;
	}
	/**
	 * @return the aoName
	 */
	public String getAoName() {
		return aoName;
	}
	/**
	 * @param aoName the aoName to set
	 */
	public void setAoName(String aoName) {
		this.aoName = aoName;
	}
	/**
	 * @return the lockDate
	 */
	public Timestamp getLockedDate() {
		return lockedDate;
	}
	/**
	 * @param lockDate the lockDate to set
	 */
	public void setLockDate(Timestamp lockedDate) {
		this.lockedDate = lockedDate;
	}
	/**
	 * @return the index
	 */
	public String getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(String index) {
		this.index = index;
	}
	/**
	 * @return the merchantHeaderId
	 */
	public String getMerchantHeaderId() {
		return merchantHeaderId;
	}
	/**
	 * @param merchantHeaderId the merchantHeaderId to set
	 */
	public void setMerchantHeaderId(String merchantHeaderId) {
		this.merchantHeaderId = merchantHeaderId;
	}

	/**
	 * @return the companyCode
	 */
	public String getCompanyCode() {
		return companyCode;
	}

	/**
	 * @param companyCode the companyCode to set
	 */
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	/**
	 * @return the rowId
	 */
	public String getRowId() {
		return rowId;
	}

	/**
	 * @param rowId the rowId to set
	 */
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}



	/**
	 * @return the cmsCase
	 */
	public String getCmsCase() {
		return cmsCase;
	}

	/**
	 * @param cmsCase the cmsCase to set
	 */
	public void setCmsCase(String cmsCase) {
		this.cmsCase = cmsCase;
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
	 * @return the repairReason
	 */
	public String getRepairReason() {
		return repairReason;
	}



	/**
	 * @param repairReason the repairReason to set
	 */
	public void setRepairReason(String repairReason) {
		this.repairReason = repairReason;
	}



	/**
	 * @return the dispatchDate
	 */
	public Timestamp getDispatchDate() {
		return dispatchDate;
	}



	/**
	 * @param dispatchDate the dispatchDate to set
	 */
	public void setDispatchDate(Timestamp dispatchDate) {
		this.dispatchDate = dispatchDate;
	}



	/**
	 * @return the isIatomsCreateCms
	 */
	public String getIsIatomsCreateCms() {
		return isIatomsCreateCms;
	}



	/**
	 * @param isIatomsCreateCms the isIatomsCreateCms to set
	 */
	public void setIsIatomsCreateCms(String isIatomsCreateCms) {
		this.isIatomsCreateCms = isIatomsCreateCms;
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
	
}
