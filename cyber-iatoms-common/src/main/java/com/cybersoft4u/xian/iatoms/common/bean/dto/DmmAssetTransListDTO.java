package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;
import cafe.core.bean.dto.DataTransferObject;
/**
 * Purpose: 設備轉倉單明細DTO
 * @author Amanda Wang
 * @since  JDK 1.6
 * @date   2016/7/11
 * @MaintenancePersonnel Amanda Wang
 */
public class DmmAssetTransListDTO extends DataTransferObject<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7123351101117705120L;
	
	/**
	 * Purpose: 枚举类型参数
	 * @author amandawang
	 * @since  JDK 1.6
	 * @date   2016/7/11
	 * @MaintenancePersonnel amandawang
	 */
	public static enum ATTRIBUTE {
		ASSET_TRANS_LIST_ID("assetTransListId"),
		ASSET_TRANS_ID("assetTransId"),
		ASSET_ID("assetId"),
		IS_SPARE_PARTS("isSpareParts"),
		IS_CUP("isCup"),
		IS_CUP_STRING("isCupString"),
		IS_ENABLED("isEnabled"),
		IS_ENABLED_STRING("isEnabledString"),
		IS_CUP_CHAR("isCupChar"),
		IS_ENABLE_CHAR("isEnableChar"),
		CONTRACT_ID("contractId"),
		CONTRACT_CODE("contractCode"),
		COMMENT("comment"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
		CREATED_DATE("createdDate"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		UPDATED_DATE("updatedDate"),
		SERIAL_NUMBER("serialNumber"),
		NAME("name"),
		ID("id"),
		FROM_WARE_HOUSE_NAME("fromWareHouseName"),
		TO_WARE_HOUSE_NAME("toWareHouseName"),
		GENERAL_COMMNET("generalComment"),
		SUMMARY("summary"),
		SUM("sum"),
		UPDATED_DATE_ID("updatedDataStr"),
		IS_CANCEL("isCancel"),
		NUMBER("number"),
		ASSET_USER("assetUser"),
		ASSET_USER_NAME("assetUserName"),
		TRANS_STATUS("transStatus");

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	/**
	 * 統計總數量
	 */
	private Integer summary;
	/**
	 * 統計各類型數量
	 */
	private Integer sum;
	/**
	 * 設備轉倉清單ID
	 */
	private String assetTransListId;
	/**
	 * 設備轉倉單代碼
	 */
	private String assetTransId;
	/**
	 * 是否為銀聯
	 */
	private String isCup = "N";
	/**
	 * char是否為銀聯
	 */
	private Character isCupChar;
	/**
	 * 是否為銀聯：漢字
	 */
	private String isCupString;
	/**
	 * 資產代碼
	 */
	private int assetId;
	/**
	 * 是否啟用
	 */
	private String isEnabled = "N";
	/**
	 * 是否啟用 ：漢字
	 */
	private String isEnabledString;
	/**
	 * char是否啟用
	 */
	private Character isEnableChar;
	/**
	 * 維護合約代碼
	 */
	private String contractId;
	/**
	 * 合約編號Code
	 */
	private String contractCode;
	/**
	 * 說明
	 */
	private String comment;
	/**
	 * 建立者代號
	 */
	private String createdById;
	/**
	 * 建立者姓名
	 */
	private String createdByName;
	/**
	 * 建立日期
	 */
	private Timestamp createdDate;
	/**
	 * 更新者代號
	 */
	private String updatedById;
	/**
	 * 更新者姓名
	 */
	private String updatedByName;
	/**
	 * 更新日期
	 */
	private Timestamp updatedDate;
	/**
	 * 設備序號
	 */
	private String serialNumber;
	/**
	 * 設備名稱
	 */
	private String name;
	/**
	 * 轉入倉名稱
	 */
	private String fromWareHouseName;
	/**
	 * 轉出倉名稱
	 */
	private String toWareHouseName;
	/**
	 * 取消轉倉
	 */
	private String isCancel;
	/**
	 * 序號
	 */
	private Integer number;
	/**
	 * 使用人
	 */
	private String assetUser;
	/**
	 * 使用人名稱
	 */
	private String assetUserName;
	/**
	 * 轉倉狀態
	 */
	private String transStatus;
	/**
	 * Constructor:無參構造函數
	 */
	public DmmAssetTransListDTO() {
		super();
	}
	
	/**
	 * @return the number
	 */
	public Integer getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}

	/**
	 * @return the assetTransId
	 */
	public String getAssetTransId() {
		return assetTransId;
	}

	/**
	 * @param assetTransId the assetTransId to set
	 */
	public void setAssetTransId(String assetTransId) {
		this.assetTransId = assetTransId;
	}

	/**
	 * @return the assetId
	 */
	public int getAssetId() {
		return assetId;
	}

	/**
	 * @param assetId the assetId to set
	 */
	public void setAssetId(int assetId) {
		this.assetId = assetId;
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
	 * @return the fromWareHouseName
	 */
	public String getFromWareHouseName() {
		return fromWareHouseName;
	}

	/**
	 * @param fromWareHouseName the fromWareHouseName to set
	 */
	public void setFromWareHouseName(String fromWareHouseName) {
		this.fromWareHouseName = fromWareHouseName;
	}

	/**
	 * @return the toWareHouseName
	 */
	public String getToWareHouseName() {
		return toWareHouseName;
	}

	/**
	 * @param toWareHouseName the toWareHouseName to set
	 */
	public void setToWareHouseName(String toWareHouseName) {
		this.toWareHouseName = toWareHouseName;
	}

	
	
	/**
	 * @return the summary
	 */
	public Integer getSummary() {
		return summary;
	}

	/**
	 * @param summary the summary to set
	 */
	public void setSummary(Integer summary) {
		this.summary = summary;
	}

	/**
	 * @return the isEnabled
	 */
	public String getIsEnabled() {
		return isEnabled;
	}

	/**
	 * @param isEnabled the isEnabled to set
	 */
	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
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
	 * @return the assetTransListId
	 */
	public String getAssetTransListId() {
		return assetTransListId;
	}

	/**
	 * @param assetTransListId the assetTransListId to set
	 */
	public void setAssetTransListId(String assetTransListId) {
		this.assetTransListId = assetTransListId;
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
	 * @return the isCupChar
	 */
	public Character getIsCupChar() {
		return isCupChar;
	}

	/**
	 * @param isCupChar the isCupChar to set
	 */
	public void setIsCupChar(Character isCupChar) {
		this.isCup = String.valueOf(isCupChar);
		this.isCupChar = isCupChar;
	}

	/**
	 * @return the isEnableChar
	 */
	public Character getIsEnableChar() {
		return isEnableChar;
	}

	/**
	 * @param isEnableChar the isEnableChar to set
	 */
	public void setIsEnableChar(Character isEnableChar) {
		this.isEnabled = String.valueOf(isEnableChar);
		this.isEnableChar = isEnableChar;
	}

	/**
	 * @return the isCupString
	 */
	public String getIsCupString() {
		return isCupString;
	}

	/**
	 * @param isCupString the isCupString to set
	 */
	public void setIsCupString(String isCupString) {
		this.isCupString = isCupString;
	}

	/**
	 * @return the isEnabledString
	 */
	public String getIsEnabledString() {
		return isEnabledString;
	}

	/**
	 * @param isEnabledString the isEnabledString to set
	 */
	public void setIsEnabledString(String isEnabledString) {
		this.isEnabledString = isEnabledString;
	}

	/**
	 * @return the sum
	 */
	public Integer getSum() {
		return sum;
	}

	/**
	 * @param sum the sum to set
	 */
	public void setSum(Integer sum) {
		this.sum = sum;
	}

	/**
	 * @return the isCancel
	 */
	public String getIsCancel() {
		return isCancel;
	}

	/**
	 * @param isCancel the isCancel to set
	 */
	public void setIsCancel(String isCancel) {
		this.isCancel = isCancel;
	}

	/**
	 * @return the assetUser
	 */
	public String getAssetUser() {
		return assetUser;
	}

	/**
	 * @param assetUser the assetUser to set
	 */
	public void setAssetUser(String assetUser) {
		this.assetUser = assetUser;
	}

	/**
	 * @return the assetUserName
	 */
	public String getAssetUserName() {
		return assetUserName;
	}

	/**
	 * @param assetUserName the assetUserName to set
	 */
	public void setAssetUserName(String assetUserName) {
		this.assetUserName = assetUserName;
	}

	/**
	 * @return the transStatus
	 */
	public String getTransStatus() {
		return transStatus;
	}

	/**
	 * @param transStatus the transStatus to set
	 */
	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}
	
	
}