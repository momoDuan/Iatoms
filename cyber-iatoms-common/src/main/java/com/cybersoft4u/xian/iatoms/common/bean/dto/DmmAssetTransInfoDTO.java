package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;

import cafe.core.bean.dto.DataTransferObject;
/**
 * Purpose: 設備轉倉單主檔DTO
 * @author Amanda Wang
 * @since  JDK 1.6
 * @date   2016/7/11
 * @MaintenancePersonnel Amanda Wang
 */
public class DmmAssetTransInfoDTO extends DataTransferObject<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6746706198185498133L;
	
	/**
	 * Purpose: 枚举类型参数
	 * @author amandawang
	 * @since  JDK 1.6
	 * @date   2016/7/11
	 * @MaintenancePersonnel amandawang
	 */
	public static enum ATTRIBUTE {
		ASSET_TRANS_ID("assetTransId"),
		FROM_WAREHOUSE_ID("fromWarehouseId"),
		FROM_WAREHOUSE_NAME("fromWarehouseName"),
		TO_WAREHOUSE_ID("toWarehouseId"),
		TO_WAREHOUSE_NAME("toWarehouseName"),
		COMMENT("comment"),
		CREATE_USER("createUser"),
		CREATE_USER_NAME("createUserName"),
		CREATE_DATE("createDate"),
		UPDATE_USER("updateUser"),
		UPDATE_USER_NAME("updateUserName"),
		UPDATE_DATE("updateDate"),
		IS_LIST_DONE("isListDone"),
		IS_TRANS_DONE("isTransDone"),
		IS_BACK("isBack"),
		TO_WAREHOUSE_ADD("toWarehouseAdd"),
		TO_WAREHOUSE_TEL("toWarehouseTel"),
		FROM_WAREHOUSE_ADD("fromWarehouseAdd"),
		FROM_WAREHOUSE_TEL("fromWarehouseTel"),
		IS_CANCLE("isCancel"),
		ASSET_TRANS_RESULT("assetTransResult"),
		FROM_DATE("fromDate")
		;
		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	
	/**
	 * 設備轉倉單號
	 */
	private String assetTransId;
	/**
	 * 來源倉代碼
	 */
	private String fromWarehouseId;
	/**
	 * 轉出倉名稱
	 */
	private String fromWarehouseName;
	/**
	 * 目的倉代碼
	 */
	private String toWarehouseId;
	/**
	 * 轉入倉名稱
	 */
	private String toWarehouseName;
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
	 * 是否已確認轉倉
	 */
	private String isListDone = "N";
	/**
	 * 是否已確認入庫
	 */
	private String isTransDone = "N";
	/**
	 * 是否轉倉退回
	 */
	private String isBack = "N";
	/**
	 * 轉出倉地址
	 */
	private String fromWarehouseAdd;
	/**
	 * 轉入倉地址
	 */
	private String toWarehouseAdd;
	/**
	 * 轉出倉電話
	 */
	private String fromWarehouseTel;
	/**
	 * 轉入倉電話
	 */
	private String toWarehouseTel;
	/**
	 * 出貨日期
	 */
	private Timestamp fromDate;
	/**
	 * 轉倉結果
	 */
	private String assetTransResult;
	
	/**
	 * 是否取消轉倉
	 */
	private String isCancel = "N";
	
	/**
	 * Constructor:無參構造函數
	 */
	public DmmAssetTransInfoDTO() {
		super();
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
	 * @return the fromWarehouseId
	 */
	public String getFromWarehouseId() {
		return fromWarehouseId;
	}
	/**
	 * @param fromWarehouseId the fromWarehouseId to set
	 */
	public void setFromWarehouseId(String fromWarehouseId) {
		this.fromWarehouseId = fromWarehouseId;
	}
	/**
	 * @return the fromWarehouseName
	 */
	public String getFromWarehouseName() {
		return fromWarehouseName;
	}
	/**
	 * @param fromWarehouseName the fromWarehouseName to set
	 */
	public void setFromWarehouseName(String fromWarehouseName) {
		this.fromWarehouseName = fromWarehouseName;
	}
	/**
	 * @return the toWarehouseId
	 */
	public String getToWarehouseId() {
		return toWarehouseId;
	}
	/**
	 * @param toWarehouseId the toWarehouseId to set
	 */
	public void setToWarehouseId(String toWarehouseId) {
		this.toWarehouseId = toWarehouseId;
	}
	/**
	 * @return the toWarehouseName
	 */
	public String getToWarehouseName() {
		return toWarehouseName;
	}
	/**
	 * @param toWarehouseName the toWarehouseName to set
	 */
	public void setToWarehouseName(String toWarehouseName) {
		this.toWarehouseName = toWarehouseName;
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
	 * @return the isListDone
	 */
	public String getIsListDone() {
		return isListDone;
	}
	/**
	 * @param isListDone the isListDone to set
	 */
	public void setIsListDone(String isListDone) {
		this.isListDone = isListDone;
	}
	/**
	 * @return the isTransDone
	 */
	public String getIsTransDone() {
		return isTransDone;
	}
	/**
	 * @param isTransDone the isTransDone to set
	 */
	public void setIsTransDone(String isTransDone) {
		this.isTransDone = isTransDone;
	}
	/**
	 * @return the isBack
	 */
	public String getIsBack() {
		return isBack;
	}
	/**
	 * @param isBack the isBack to set
	 */
	public void setIsBack(String isBack) {
		this.isBack = isBack;
	}
	/**
	 * @return the fromWarehouseAdd
	 */
	public String getFromWarehouseAdd() {
		return fromWarehouseAdd;
	}

	/**
	 * @param fromWarehouseAdd the fromWarehouseAdd to set
	 */
	public void setFromWarehouseAdd(String fromWarehouseAdd) {
		this.fromWarehouseAdd = fromWarehouseAdd;
	}

	/**
	 * @return the toWarehouseAdd
	 */
	public String getToWarehouseAdd() {
		return toWarehouseAdd;
	}

	/**
	 * @param toWarehouseAdd the toWarehouseAdd to set
	 */
	public void setToWarehouseAdd(String toWarehouseAdd) {
		this.toWarehouseAdd = toWarehouseAdd;
	}

	/**
	 * @return the fromWarehouseTel
	 */
	public String getFromWarehouseTel() {
		return fromWarehouseTel;
	}

	/**
	 * @param fromWarehouseTel the fromWarehouseTel to set
	 */
	public void setFromWarehouseTel(String fromWarehouseTel) {
		this.fromWarehouseTel = fromWarehouseTel;
	}

	/**
	 * @return the toWarehouseTel
	 */
	public String getToWarehouseTel() {
		return toWarehouseTel;
	}

	/**
	 * @param toWarehouseTel the toWarehouseTel to set
	 */
	public void setToWarehouseTel(String toWarehouseTel) {
		this.toWarehouseTel = toWarehouseTel;
	}

	/**
	 * @return the fromDate
	 */
	public Timestamp getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(Timestamp fromDate) {
		this.fromDate = fromDate;
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
	 * @return the assetTransResult
	 */
	public String getAssetTransResult() {
		return assetTransResult;
	}

	/**
	 * @param assetTransResult the assetTransResult to set
	 */
	public void setAssetTransResult(String assetTransResult) {
		this.assetTransResult = assetTransResult;
	}
	
}
