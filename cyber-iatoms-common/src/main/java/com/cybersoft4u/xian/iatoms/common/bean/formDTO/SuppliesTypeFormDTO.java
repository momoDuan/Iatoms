package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.List;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmSuppliesDTO;

/**
 * Purpose:耗材品项維護FormDTO 
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2016-7-04
 * @MaintenancePersonnel HermanWang
 */
public class SuppliesTypeFormDTO extends AbstractSimpleListFormDTO<DmmSuppliesDTO>{

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 5488277857900136894L;
	/**
	 * 常量定義
	 */
	public static final String QUERY_CUSTOMSER_ID = "queryCustomerId";
	/**
	 * 查詢條件客戶名稱
	 */
	public static final String QUERY_CUSTOMSER_NAME = "queryCustomerName";
	/**
	 * 查詢條件耗材code
	 */
	public static final String QUERY_SUPPLIES_CODE = "querySuppliesCode";
	/**
	 * 耗材id
	 */
	public static final String QUERY_SUPPLIES_ID = "querySuppliesId";
	/**
	 * 耗材分類
	 */
	public static final String QUERY_SUPPLIES_TYPE = "querySuppliesType";
	/**
	 * 耗材名稱
	 */
	public static final String QUERY_SUPPLIES_NAME = "querySuppliesName";

	/**
	 * 
	 * Constructor:構造器
	 */
	public SuppliesTypeFormDTO() {
		super();
		this.suppliesDTO = suppliesDTO;
	}

	/**
	 * 
	 */
	private String serviceSet;
	/**
	 * 當前頁碼
	 */
	private Integer page;

	/**
	 * 一頁顯示多少筆
	 */
	private Integer rows;

	/**
	 * 按此字段進行排序
	 */
	private String sort;

	/**
	 * 排序方式
	 */
	private String order;

	/**
	 * 主键
	 */
	private String suppliesId;
	/**
	 * 查尋參數，客戶id
	 */
	private String queryCustomerId;
	/**
	 * 查尋參數，客戶name
	 */
	private String queryCustomerName;

	/**
	 * 耗材名称id
	 */
	private String querySuppliesId;
	/**
	 * 查詢參數name
	 */
	private String querySuppliesCode;
	/**
	 * 查詢參數耗材分類
	 */
	private String querySuppliesType;
	/**
	 * 查詢參數耗材名稱
	 */
	private String querySuppliesName;
	/**
	 * 當前登入者角色屬性
	 */
	private String roleAttribute;

	/**
	 * @return the querySuppliesType
	 */
	public String getQuerySuppliesType() {
		return querySuppliesType;
	}

	/**
	 * @param querySuppliesType the querySuppliesType to set
	 */
	public void setQuerySuppliesType(String querySuppliesType) {
		this.querySuppliesType = querySuppliesType;
	}

	/**
	 * 耗材訊息
	 */
	private DmmSuppliesDTO suppliesDTO;
	/**
	 * 耗材dto
	 */
	private List<DmmSuppliesDTO> listSuppliesDTO;
	

	/**
	 * @return the queryCustomerName
	 */
	public String getQueryCustomerName() {
		return queryCustomerName;
	}

	/**
	 * @param queryCustomerName
	 *            the queryCustomerName to set
	 */
	public void setQueryCustomerName(String queryCustomerName) {
		this.queryCustomerName = queryCustomerName;
	}

	/**
	 * @return the querySuppliesId
	 */
	public String getQuerySuppliesId() {
		return querySuppliesId;
	}

	/**
	 * @param querySuppliesId
	 *            the querySuppliesId to set
	 */
	public void setQuerySuppliesId(String querySuppliesId) {
		this.querySuppliesId = querySuppliesId;
	}

	/**
	 * @return the page
	 */
	public Integer getPage() {
		return page;
	}

	/**
	 * @param page
	 *            the page to set
	 */
	public void setPage(Integer page) {
		this.page = page;
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
	 * @return the sort
	 */
	public String getSort() {
		return sort;
	}

	/**
	 * @param sort
	 *            the sort to set
	 */
	public void setSort(String sort) {
		this.sort = sort;
	}

	/**
	 * @return the order
	 */
	public String getorder() {
		return order;
	}

	/**
	 * @param order
	 *            the order to set
	 */
	public void setorder(String order) {
		this.order = order;
	}

	/**
	 * @return the queryCustomerId
	 */
	public String getQueryCustomerId() {
		return queryCustomerId;
	}

	/**
	 * @param queryCustomerId
	 *            the queryCustomerId to set
	 */
	public void setQueryCustomerId(String queryCustomerId) {
		this.queryCustomerId = queryCustomerId;
	}

	/**
	 * @return the querySuppliesCode
	 */
	public String getQuerySuppliesCode() {
		return querySuppliesCode;
	}

	/**
	 * @param querySuppliesCode
	 *            the querySuppliesCode to set
	 */
	public void setQuerySuppliesCode(String querySuppliesCode) {
		this.querySuppliesCode = querySuppliesCode;
	}

	/**
	 * @return the suppliesDTO
	 */
	public DmmSuppliesDTO getSuppliesDTO() {
		return suppliesDTO;
	}

	/**
	 * @param suppliesDTO
	 *            the suppliesDTO to set
	 */
	public void setSuppliesDTO(DmmSuppliesDTO suppliesDTO) {
		this.suppliesDTO = suppliesDTO;
	}

	/**
	 * @return the suppliesId
	 */
	public String getSuppliesId() {
		return suppliesId;
	}

	/**
	 * @param suppliesId
	 *            the suppliesId to set
	 */
	public void setSuppliesId(String suppliesId) {
		this.suppliesId = suppliesId;
	}

	/**
	 * @return the serviceSet
	 */
	public String getServiceSet() {
		return serviceSet;
	}

	/**
	 * @param serviceSet
	 *            the serviceSet to set
	 */
	public void setServiceSet(String serviceSet) {
		this.serviceSet = serviceSet;
	}

	/**
	 * @return the listSuppliesDTO
	 */
	public List<DmmSuppliesDTO> getListSuppliesDTO() {
		return listSuppliesDTO;
	}

	/**
	 * @param listSuppliesDTO the listSuppliesDTO to set
	 */
	public void setListSuppliesDTO(List<DmmSuppliesDTO> listSuppliesDTO) {
		this.listSuppliesDTO = listSuppliesDTO;
	}

	/**
	 * @return the querySuppliesName
	 */
	public String getQuerySuppliesName() {
		return querySuppliesName;
	}

	/**
	 * @param querySuppliesName the querySuppliesName to set
	 */
	public void setQuerySuppliesName(String querySuppliesName) {
		this.querySuppliesName = querySuppliesName;
	}

	/**
	 * @return the roleAttribute
	 */
	public String getRoleAttribute() {
		return roleAttribute;
	}

	/**
	 * @param roleAttribute the roleAttribute to set
	 */
	public void setRoleAttribute(String roleAttribute) {
		this.roleAttribute = roleAttribute;
	}
	
}
