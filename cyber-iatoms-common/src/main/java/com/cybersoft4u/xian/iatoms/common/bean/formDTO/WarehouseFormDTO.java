package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.List;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.WarehouseDTO;

/**
 * Purpose:倉庫FormDTO
 * @author ElvaHe
 * @since JDK 1.6
 * @date 2016年6月1日
 * @MaintenancePersonnel ElvaHe
 */
public class WarehouseFormDTO extends AbstractSimpleListFormDTO<WarehouseDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2601682614165142684L;
	
	/**
	 * 靜態常量-查詢參數，廠商編號
	 */
	public static final String QUERY_COMPANY_ID = "queryCompanyId";
	/**
	 * 靜態常量-查詢參數，倉庫名稱
	 */
	public static final String QUERY_NAME = "queryName";
	/**
	 * 靜態常量-廠商列表參數
	 */
	public static final String PARAM_VENDOR_LIST = "vendorList";
	/**
	 * 靜態常量-頁面排序字段參數
	 */
	public static final String PARAM_PAGE_SORT = "companyName,name ";
	/**
	 * 查詢參數，廠商編號
	 */
	private String queryCompanyId;
	
	/**
	 * 查詢參數，倉庫名稱
	 */
	private String queryName;

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
	 * 總記錄數
	 */
	private Integer totalSize;
	/**
	 * 倉庫DTO列表，查詢使用
	 */
	private List<WarehouseDTO> warehouseDTOs;
	
	/**
	 * 倉庫DTO,新增修改畫面使用
	 */
	private WarehouseDTO warehouseDTO;
	
	/**
	 * 倉庫編號
	 */
	private String warehouseId;

	/**
	 * Constructor:無參構造
	 */
	public WarehouseFormDTO() {
		
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
	 * @return the queryName
	 */
	public String getQueryName() {
		return queryName;
	}

	/**
	 * @param queryName the queryName to set
	 */
	public void setQueryName(String queryName) {
		this.queryName = queryName;
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
	 * @return the warehouseDTOs
	 */
	public List<WarehouseDTO> getWarehouseDTOs() {
		return warehouseDTOs;
	}

	/**
	 * @param warehouseDTOs the warehouseDTOs to set
	 */
	public void setWarehouseDTOs(List<WarehouseDTO> warehouseDTOs) {
		this.warehouseDTOs = warehouseDTOs;
	}

	/**
	 * @return the warehouseDTO
	 */
	public WarehouseDTO getWarehouseDTO() {
		return warehouseDTO;
	}

	/**
	 * @param warehouseDTO the warehouseDTO to set
	 */
	public void setWarehouseDTO(WarehouseDTO warehouseDTO) {
		this.warehouseDTO = warehouseDTO;
	}

	/**
	 * @return the warehouseId
	 */
	public String getWarehouseId() {
		return warehouseId;
	}

	/**
	 * @param warehouseId the warehouseId to set
	 */
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

}
