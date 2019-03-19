package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cafe.core.bean.Parameter;
import cafe.core.bean.dto.DataTransferObject;
/**
 * Purpose: 求償項目資料表DTO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/2/10
 * @MaintenancePersonnel CarrieDuan
 */
public class SrmPaymentItemDTO extends DataTransferObject<String> {
	
	public static enum ATTRIBUTE {
		ITEM_ID("itemId"),
		PAYMENT_ID("paymentId"),
		PAYMENT_ITEM("paymentItem"),
		ITEM_NAME("itemName"),
		SERIAL_NUMBER("serialNumber"),
		ASSET_ID("assetId"),
		CONTRACT_ID("contractId"),
		CONTRACT_CODE("contractCode"),
		SUPPLIES_TYPE("suppliesType"),
		SUPPLIES_AMOUNT("suppliesAmount"),
		IS_PAY("isPay"),
		PAYMENT_REASON("paymentReason"),
		REASON_DETAIL("reasonDetail"),
		CHECK_RESULT("checkResult"),
		CHECK_USER("checkUser"),
		FEE("fee"),
		FEE_DESC("feeDesc"),
		IS_ASK_PAY("isAskPay"),
		PAYMENT_TYPE("paymentType"),
		PAYMENT_TYPE_NAME("paymentTypeName"),
		PAYMENT_TYPE_DESC("paymentTypeDesc"),
		ASK_PAY_DATE("askPayDate"),
		ASK_PAY_DESC("askPayDesc"),
		CANCEL_DATE("cancelDate"),
		REPAYMENT_DATE("repaymentDate"),
		CANCEL_DESC("cancelDesc"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		UPDATED_DATE("updatedDate"),
		STATUS("status"),
		BACK_DESC("backDesc"),
		ASSET_NAME("assetName"),
		SUPPLIES_NAME("suppliesName"),
		PAY_INFO("payInfo"),
		PAYMENT_ITEM_NAME("paymentItemName"),
		CHECK_UPDATE_DATE("checkUpdateDate"),
		PAYMENT_REASON_NAME("paymentReasonName"),
		CUSTOMER_NAME("customerName");

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	
	/**
	 * 求償項目編號
	 */
	private String itemId;
	/**
	 * 求償批號
	 */
	private String paymentId;
	/**
	 * 求償項目
	 */
	private String paymentItem;
	/**
	 * 設備名稱/耗材項目
	 */
	private String itemName;
	/**
	 * 設備ID
	 */
	private String assetId;
	/**
	 * 設備序號
	 */
	private String serialNumber;
	/**
	 * 合約Id
	 */
	private String contractId;
	/**
	 * 合約編號
	 */
	private String contractCode;
	/**
	 * 耗材分類
	 */
	private String suppliesType;
	/**
	 * 耗材名稱
	 */
	private String suppliesName;
	/**
	 * 耗材數量
	 */
	private Integer suppliesAmount;
	/**
	 * 是否求償
	 */
	private String isPay = "N";
	/**
	 * 求償資訊
	 */
	private String paymentReason;
	/**
	 * 求償資訊自行輸入
	 */
	private String reasonDetail;
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
	private String fee;
	/**
	 * 求償費用說明
	 */
	private String feeDesc;
	/**
	 * 是否需要請款
	 */
	private String isAskPay = "N";
	/**
	 * 求償方式
	 */
	private String paymentType;
	/**
	 * 求償方式-名稱
	 */
	private String paymentTypeName;
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
	 * 
	 */
	private Long updateDateLong;
	/**
	 * 狀態
	 */
	private String status;
	/**
	 * 退回說明
	 */
	private String backDesc;
	/**
	 * 設備名稱
	 */
	private String assetName;
	/**
	 * paymentItemName
	 */
	private String paymentItemName;
	/**
	 * 求償資訊-頁面顯示
	 */
	private String payInfo;
	/**
	 * 
	 */
	private List<SrmPaymentItemDTO> checkUpdateDate;
	/**
	 * 
	 */
	private String customerName;
	/**
	 * 
	 */
	private String paymentReasonName;
	/**
	 * Constructor: 無參構造函數
	 */
	public SrmPaymentItemDTO() {
		super();
	}
	
	/**
	 * Constructor: 有參構造函數
	 */
	public SrmPaymentItemDTO(String itemId, String paymentId,
			String paymentItem, String itemName, String serialNumber,
			String contractId, String suppliesType, Integer suppliesAmount,
			String isPay, String paymentReason, String reasonDetail,
			String checkResult, String checkUser, String fee,
			String feeDesc, String isAskPay, String paymentType,
			String paymentTypeDesc, Date askPayDate, String askPayDesc,
			Date cancelDate, Date repaymentDate, String cancelDesc,
			String updatedById, String updatedByName, Timestamp updatedDate) {
		super();
		this.itemId = itemId;
		this.paymentId = paymentId;
		this.paymentItem = paymentItem;
		this.itemName = itemName;
		this.serialNumber = serialNumber;
		this.contractId = contractId;
		this.suppliesType = suppliesType;
		this.suppliesAmount = suppliesAmount;
		this.isPay = isPay;
		this.paymentReason = paymentReason;
		this.reasonDetail = reasonDetail;
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
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
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
	 * @return the contractId
	 */
	public String getContractId() {
		return contractId;
	}

	/**
	 * @param contractId the contractId to set
	 */
	public void setContractId(String contractId) {
		this.contractId = contractId;
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
	public String getFee() {
		return fee;
	}

	/**
	 * @param fee the fee to set
	 */
	public void setFee(String fee) {
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
	 * @return the assetId
	 */
	public String getAssetId() {
		return assetId;
	}

	/**
	 * @param assetId the assetId to set
	 */
	public void setAssetId(String assetId) {
		this.assetId = assetId;
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
	 * @return the paymentTypeName
	 */
	public String getPaymentTypeName() {
		return paymentTypeName;
	}

	/**
	 * @param paymentTypeName the paymentTypeName to set
	 */
	public void setPaymentTypeName(String paymentTypeName) {
		this.paymentTypeName = paymentTypeName;
	}

	/**
	 * @return the backDesc
	 */
	public String getBackDesc() {
		return backDesc;
	}

	/**
	 * @param backDesc the backDesc to set
	 */
	public void setBackDesc(String backDesc) {
		this.backDesc = backDesc;
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
	 * @return the assetName
	 */
	public String getAssetName() {
		return assetName;
	}

	/**
	 * @param assetName the assetName to set
	 */
	public void setAssetName(String assetName) {
		this.assetName = assetName;
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
	 * @return the updateDateLong
	 */
	public Long getUpdateDateLong() {
		return updateDateLong;
	}

	/**
	 * @param updateDateLong the updateDateLong to set
	 */
	public void setUpdateDateLong(Long updateDateLong) {
		this.updateDateLong = updateDateLong;
	}

	public List<SrmPaymentItemDTO> getUpdateDateMap() {
		return checkUpdateDate;
	}

	public void setUpdateDateMap(List<SrmPaymentItemDTO> updateDateMap) {
		this.checkUpdateDate = updateDateMap;
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
	
}
