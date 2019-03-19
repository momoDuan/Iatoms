package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.List;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetStocktakeCategroyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetStocktakeStatusDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetStacktakeInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetStacktakeListDTO;

/**
 * 
 * Purpose:設備盤點功能FormDTO
 * @author dell
 * @since  JDK 1.7
 * @date   2016-7-15
 * @MaintenancePersonnel dell
 */
public class AssetStacktakeFormDTO extends AbstractSimpleListFormDTO<DmmAssetStacktakeListDTO> {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 753319726029949498L;

	/**
	 * 无参构造子
	 */
	public AssetStacktakeFormDTO() {
	}

	public static final String ASSET_INVENTORY_ID = "assetInventoryId";//设备批号
	public static final String PARAM_ASSET_NAME_LIST = "getAssetNameList";//设备名稱列
	public static final String PARAM_METHOD_GET_INVENTORY_NUMBER_LIST = "getInventoryNumberList";//獲取盤點批號list
	public static final String SEND_SERIAL_NUMBER = "sendSerialNumber";//单笔盘点送出的设备批号
	public static final String SAVE_REMARK = "saveRemark";//单笔盘点送出的设备批号
	public static final String ASSET_STOCKTACK_LIST = "assetStocktackList";//单笔盘点送出的设备批号
	public static final String EXPORT_INVENTORY = "exportInventory";//列印清冊匯出報表
	public static final String EXPORT_SUMMARY = "exportSummary";//盤點結果匯出報表
	//導出
	public static final String PROJECT_REPORT_JRXML_INVENTORY_NAME = "DMM_ASSET_INVENTORY_REPORT";
	public static final String PROJECT_REPORT_FILE_INVENTORY_NAME = "設備盤點清冊";
	public static final String PROJECT_REPORT_JRXML_SUMMARY_NAME = "DMM_ASSET_SUMMARY_REPORT";
	public static final String PROJECT_REPORT_JRXML_INVENTORY_OTHER_NAME = "DMM_ASSET_INVENTORY_OTHER_REPORT";
	public static final String PROJECT_REPORT_FILE_INVENTORY_OTHER_NAME = "設備盤點結果";
	public static final String PROJECT_REPORT_JRXML_INVENTORY_CROSS_NAME = "DMM_ASSET_INVENTORY_CROSS_REPORT";
	public static final String PROJECT_REPORT_FILE_INVENTORY_CROSS_NAME = "盤點清冊";
	public static final String PROJECT_REPORT_SHEET_NAME = "設備盤點";
	
	private String assetInventoryId;
	private DmmAssetStacktakeInfoDTO assetStacktakeInfoDTO;
	private DmmAssetStacktakeListDTO assetStacktakeListDTO;
	private DmmAssetStacktakeListDTO assetStocktackListDTO;
	private AssetStocktakeCategroyDTO assetStocktackCategroyDTO;
	private AssetStocktakeStatusDTO assetStocktackStatusDTO;
	private String queryStocktackId;//查詢設備批號
	private String sendSerialNumber;//待盤點的設備編號
	private String assetStocktackList;//待儲存的設備說明
	private List<DmmAssetStacktakeListDTO> assetStocktackListDTOs;
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
	private String isComplete;
	/**
	 * 設備狀態名
	 */
	/**
	 * @return the isComplete
	 */
	public String getIsComplete() {
		return isComplete;
	}
	/**
	 * @param isComplete the isComplete to set
	 */
	public void setIsComplete(String isComplete) {
		this.isComplete = isComplete;
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
	 * @return the assetStocktackListDTOs
	 */
	public List<DmmAssetStacktakeListDTO> getAssetStocktackListDTOs() {
		return assetStocktackListDTOs;
	}
	/**
	 * @param assetStocktackListDTOs the assetStocktackListDTOs to set
	 */
	public void setAssetStocktackListDTOs(
			List<DmmAssetStacktakeListDTO> assetStocktackListDTOs) {
		this.assetStocktackListDTOs = assetStocktackListDTOs;
	}
	/**
	 * @return the assetStocktackList
	 */
	public String getAssetStocktackList() {
		return assetStocktackList;
	}
	/**
	 * @param assetStocktackList the assetStocktackList to set
	 */
	public void setAssetStocktackList(String assetStocktackList) {
		this.assetStocktackList = assetStocktackList;
	}
	/**
	 * @return the sendSerialNumber
	 */
	public String getSendSerialNumber() {
		return sendSerialNumber;
	}
	/**
	 * @param sendSerialNumber the sendSerialNumber to set
	 */
	public void setSendSerialNumber(String sendSerialNumber) {
		this.sendSerialNumber = sendSerialNumber;
	}
	/**
	 * @return the queryStocktackId
	 */
	public String getQueryStocktackId() {
		return queryStocktackId;
	}
	/**
	 * @param queryStocktackId the queryStocktackId to set
	 */
	public void setQueryStocktackId(String queryStocktackId) {
		this.queryStocktackId = queryStocktackId;
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
	 * @return the assetInventoryId
	 */
	public String getAssetInventoryId() {
		return assetInventoryId;
	}
	/**
	 * @param assetInventoryId the assetInventoryId to set
	 */
	public void setAssetInventoryId(String assetInventoryId) {
		this.assetInventoryId = assetInventoryId;
	}
	
	/**
	 * @return the assetStocktackListDTO
	 */
	public DmmAssetStacktakeListDTO getAssetStocktackListDTO() {
		return assetStocktackListDTO;
	}
	/**
	 * @return the assetStocktackCategroyDTO
	 */
	public AssetStocktakeCategroyDTO getAssetStocktackCategroyDTO() {
		return assetStocktackCategroyDTO;
	}
	/**
	 * @return the assetStocktackStatusDTO
	 */
	public AssetStocktakeStatusDTO getAssetStocktackStatusDTO() {
		return assetStocktackStatusDTO;
	}
	
	/**
	 * @param assetStocktackListDTO the assetStocktackListDTO to set
	 */
	public void setAssetStocktackListDTO(DmmAssetStacktakeListDTO assetStocktackListDTO) {
		this.assetStocktackListDTO = assetStocktackListDTO;
	}
	/**
	 * @param assetStocktackCategroyDTO the assetStocktackCategroyDTO to set
	 */
	public void setAssetStocktackCategroyDTO(
			AssetStocktakeCategroyDTO assetStocktackCategroyDTO) {
		this.assetStocktackCategroyDTO = assetStocktackCategroyDTO;
	}
	/**
	 * @param assetStocktackStatusDTO the assetStocktackStatusDTO to set
	 */
	public void setAssetStocktackStatusDTO(
			AssetStocktakeStatusDTO assetStocktackStatusDTO) {
		this.assetStocktackStatusDTO = assetStocktackStatusDTO;
	}
	/**
	 * @return the assetStacktakeInfoDTO
	 */
	public DmmAssetStacktakeInfoDTO getAssetStacktakeInfoDTO() {
		return assetStacktakeInfoDTO;
	}
	/**
	 * @param assetStacktakeInfoDTO the assetStacktakeInfoDTO to set
	 */
	public void setAssetStacktakeInfoDTO(
			DmmAssetStacktakeInfoDTO assetStacktakeInfoDTO) {
		this.assetStacktakeInfoDTO = assetStacktakeInfoDTO;
	}
	/**
	 * @return the assetStacktakeListDTO
	 */
	public DmmAssetStacktakeListDTO getAssetStacktakeListDTO() {
		return assetStacktakeListDTO;
	}
	/**
	 * @param assetStacktakeListDTO the assetStacktakeListDTO to set
	 */
	public void setAssetStacktakeListDTO(
			DmmAssetStacktakeListDTO assetStacktakeListDTO) {
		this.assetStacktakeListDTO = assetStacktakeListDTO;
	}

	
}
