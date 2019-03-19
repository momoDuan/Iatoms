package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.dto.DataTransferObject;

/**
 * Purpose: 歷史轉倉查詢轉倉批號查詢結果DTO
 * @author echomou
 * @since  JDK 1.6
 * @date   2016/7/19
 * @MaintenancePersonnel echomou
 */
public class AssetTransInfoHitoryDTO extends DataTransferObject<String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6746706198185498153L;
	
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
		IS_TRANS_DONE("isTransDone"),
		IS_BACK("isBack"),
		ASSET_TRANS_RESULT("assetTransResult"),
		COMMENT("comment"),
		IS_CANCEL("isCancel"),
		TO_WAREHOUSE_ADD("toWarehouseAdd"),
		TO_WAREHOUSE_TEL("toWarehouseTel"),
		FROM_WAREHOUSE_ADD("fromWarehouseAdd"),
		FROM_WAREHOUSE_TEL("fromWarehouseTel"),
		FROM_DATE("fromDate")
		;
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
	 * 設備轉倉單號
	 */
	private String assetTransId;
	/**
	 * 來源倉代碼
	 */
	private String fromWarehouseId;
	/**
	 * 來源倉名稱
	 */ 
	private String fromWarehouseName;

	/**
	 * 目的倉代碼
	 */
	private String toWarehouseId;
	
	/**
	 * 目的倉名稱
	 */
	private String toWarehouseName;
	
	/**
	 * 轉倉結果
	 */
	private String assetTransResult;
	
	/**
	 * 是否已確認入庫
	 */
	private String isTransDone;
	
	/**
	 * 是否轉倉退回
	 */
	private String isBack;
	/**
	 * 是否取消轉倉
	 */
	private String isCancel;
	/**
	 * 說明
	 */
	private String comment;
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
	private java.sql.Timestamp fromDate;
	/**
	 * @return the fromDate
	 */
	public java.sql.Timestamp getFromDate() {
		return fromDate;
	}
	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(java.sql.Timestamp fromDate) {
		this.fromDate = fromDate;
	}
	
	/**
	 * Constructor:無參構造函數
	 */
	public AssetTransInfoHitoryDTO() {
		super();
	}
	/**
	 * Constructor:有參構造函數
	 * @param isTransDone 
	 */
	public AssetTransInfoHitoryDTO(String assetTransId,
			String fromWarehouseId, String fromWarehouseName,
			String toWarehouseId, String toWarehouseName,
			String assetTransResult, String isBisTransDone, String isBack,
			String comment, String isTransDone) {
		super();
		this.assetTransId = assetTransId;
		this.fromWarehouseId = fromWarehouseId;
		this.fromWarehouseName = fromWarehouseName;
		this.toWarehouseId = toWarehouseId;
		this.toWarehouseName = toWarehouseName;
		this.assetTransResult = assetTransResult;
		this.isTransDone = isTransDone;
		this.isBack = isBack;
		this.comment = comment;
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
	
}

