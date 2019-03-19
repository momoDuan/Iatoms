package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BimDepartmentDTO;

/**
 * Purpose: 部門維護FormDTO
 * @author barryzhang
 * @since  JDK 1.6
 * @date   2016/8/30
 * @MaintenancePersonnel cybersoft
 */
public class DepartmentFormDTO extends AbstractSimpleListFormDTO<BimDepartmentDTO>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6721279022599890163L;

	public static final String QUERY_COMPANY = "queryCompany";
	public static final String QUERY_DEPT_NAME = "queryDeptName";
	
	/**
	 * 查詢參數, 公司編號
	 */
	private String queryCompany;
	
	/**
	 * 查詢參數, 部門名稱
	 */
	private String queryDeptName;
	
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
	 * 部門維護DTO
	 */
	private BimDepartmentDTO bimDepartmentDTO;
	
	/**
	 * 部門ID
	 */
	private String deptCode;
	
	
	/**
	 * Constructor: 無參構造函數
	 */
	public DepartmentFormDTO() {
		super();
	}


	/**
	 * @return the queryCompany
	 */
	public String getQueryCompany() {
		return queryCompany;
	}


	/**
	 * @param queryCompany the queryCompany to set
	 */
	public void setQueryCompany(String queryCompany) {
		this.queryCompany = queryCompany;
	}


	/**
	 * @return the queryDeptName
	 */
	public String getQueryDeptName() {
		return queryDeptName;
	}


	/**
	 * @param queryDeptName the queryDeptName to set
	 */
	public void setQueryDeptName(String queryDeptName) {
		this.queryDeptName = queryDeptName;
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
	 * @return the bimDepartmentDTO
	 */
	public BimDepartmentDTO getBimDepartmentDTO() {
		return bimDepartmentDTO;
	}


	/**
	 * @param bimDepartmentDTO the bimDepartmentDTO to set
	 */
	public void setBimDepartmentDTO(BimDepartmentDTO bimDepartmentDTO) {
		this.bimDepartmentDTO = bimDepartmentDTO;
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

	
}
