package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetTypeDTO;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

/**
 * Purpose: 設備品項維護FormDTO
 * @author HermanWang
 * @since  JDK 1.7
 * @date   2016年7月8日
 * @MaintenancePersonnel HermanWang
 */
public class AssetTypeFormDTO extends AbstractSimpleListFormDTO<AssetTypeDTO> {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -3982082469755070538L;
	/**
	 * 查詢條件設備類別
	 */
	public static final String QUERY_PAGE_PARAM_ASSET_CATEGORY_CODE = "queryAssetCategoryCode";
	/**
	 * 多選設備廠商
	 */
	public static final String EDIT_PAGE_PARAM_ARRAY_COMPANY_ID		= "companyId[]";
	/**
	 * 多選通訊模式
	 */
	public static final String EDIT_PAGE_PARAM_ARRAY_COMM_MODE_ID	= "commModeId[]";
	/**
	 * 多選支援功能
	 */
	public static final String EDIT_PAGE_PARAM_ARRAY_FUNCTION_ID	= "functionId[]";
	/**
	 * 預編輯資料編號
	 */
	public static final String EDIT_PAGE_PARAM_ASSET_TYPE_ID 		= "editAssetTypeId";
	
	/**
	 * 查詢條件--設備類別
	 */
	private String queryAssetCategoryCode;
	/**
	 * 排序欄位
	 */
	private String sort;
	/**
	 * 排序方式
	 */
	private String order;
	/**
	 * 預編輯資料編號
	 */
	private String editAssetTypeId;
	/**
	 * 資料存儲對象
	 */
	private AssetTypeDTO assetTypeDTO;
	
	/**
	 * Constructor: 無參構造
	 */
	public AssetTypeFormDTO(){
		super();
	}

	/**
	 * @return the queryAssetCategoryCode
	 */
	public String getQueryAssetCategoryCode() {
		return queryAssetCategoryCode;
	}

	/**
	 * @param queryAssetCategoryCode the queryAssetCategoryCode to set
	 */
	public void setQueryAssetCategoryCode(String queryAssetCategoryCode) {
		this.queryAssetCategoryCode = queryAssetCategoryCode;
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
	 * @return the editAssetTypeId
	 */
	public String getEditAssetTypeId() {
		return editAssetTypeId;
	}

	/**
	 * @param editAssetTypeId the editAssetTypeId to set
	 */
	public void setEditAssetTypeId(String editAssetTypeId) {
		this.editAssetTypeId = editAssetTypeId;
	}

	/**
	 * @return the assetTypeDTO
	 */
	public AssetTypeDTO getAssetTypeDTO() {
		return assetTypeDTO;
	}

	/**
	 * @param assetTypeDTO the assetTypeDTO to set
	 */
	public void setAssetTypeDTO(AssetTypeDTO assetTypeDTO) {
		this.assetTypeDTO = assetTypeDTO;
	}
}
