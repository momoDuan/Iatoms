package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.List;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.MailListDTO;

/**
 * Purpose: 電子郵件群組維護FormDTO
 * 
 * @author CarrieDuan
 * @since JDK 1.7
 * @date 2016-6-30
 * @MaintenancePersonnel CarrieDuan
 */
public class MailListManageFormDTO extends AbstractSimpleListFormDTO<MailListDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4075453212406722300L;

	/**
	 * 无参构造子
	 */
	public MailListManageFormDTO() {
	}
	
	/**
	 * 查询通知種類
	 */
	public static final String QUERY_NOTICE_TYPE = "queryNoticeType";
	/**
	 * 查询姓名
	 */
	public static final String QUERY_NAME = "queryName";
	/**
	 * 查詢郵件通知種類
	 */
	public static final String PARAM_MAIL_GROUP_STRING = "mailGroupString";
	/**
	 * 郵件通知種類
	 */
	public static final String PARAM_METHOD_GET_MAIL_GROUP_LIST = "getMailGroupList"; 
	/**
	 * 排序方式-1
	 */
	public static final String PARAM_PAGE_SORT_ONE = "name";
	/**
	 * 排序方式-2
	 */
	public static final String PARAM_PAGE_SORT_TWO = "mailGroupName";
	/**
	 * 查詢條件:查詢通知類別
	 */
	private String queryNoticeType;
	/**
	 * 電子郵件羣組ＩＤ
	 */
	private String mailId;
	/**
	 * 查詢條件:姓名
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
	 * mail地址
	 */
	private String mailAddress;
	/**
	 * 通知種類
	 */
	private String mailGroup;
	/**
	 * 電子郵件羣組維護DTO
	 */
	private MailListDTO mailListDTO;
	/**
	 * 保存时，存储電子郵件集合的JSON字符串
	 */
	private String mailListRow;
	/**
	 * 電子郵件羣組維護ＤＴＯ集合
	 */
	private List<MailListDTO> mailListDTOs;

	/**
	 * @return the queryNoticeType
	 */
	public String getQueryNoticeType() {
		return queryNoticeType;
	}

	/**
	 * @param queryNoticeType
	 *            the queryNoticeType to set
	 */
	public void setQueryNoticeType(String queryNoticeType) {
		this.queryNoticeType = queryNoticeType;
	}

	/**
	 * @return the queryName
	 */
	public String getQueryName() {
		return queryName;
	}

	/**
	 * @param queryName
	 *            the queryName to set
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
	 * @param rows
	 *            the rows to set
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
	 * @param page
	 *            the page to set
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
	 * @param sort
	 *            the sort to set
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
	 * @param order
	 *            the order to set
	 */
	public void setOrder(String order) {
		this.order = order;
	}

	/**
	 * @return the mailListDTO
	 */
	public MailListDTO getMailListDTO() {
		return mailListDTO;
	}

	/**
	 * @param mailListDTO
	 *            the mailListDTO to set
	 */
	public void setMailListDTO(MailListDTO mailListDTO) {
		this.mailListDTO = mailListDTO;
	}

	/**
	 * @return the mailListRow
	 */
	public String getMailListRow() {
		return mailListRow;
	}

	/**
	 * @param mailListRow the mailListRow to set
	 */
	public void setMailListRow(String mailListRow) {
		this.mailListRow = mailListRow;
	}

	/**
	 * @return the mailListDTOs
	 */
	public List<MailListDTO> getMailListDTOs() {
		return mailListDTOs;
	}

	/**
	 * @param mailListDTOs the mailListDTOs to set
	 */
	public void setMailListDTOs(List<MailListDTO> mailListDTOs) {
		this.mailListDTOs = mailListDTOs;
	}

	/**
	 * @return the mailId
	 */
	public String getMailId() {
		return mailId;
	}

	/**
	 * @param mailId the mailId to set
	 */
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	/**
	 * @return the mailAddress
	 */
	public String getMailAddress() {
		return mailAddress;
	}

	/**
	 * @return the mailGroup
	 */
	public String getMailGroup() {
		return mailGroup;
	}

	/**
	 * @param mailAddress the mailAddress to set
	 */
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	/**
	 * @param mailGroup the mailGroup to set
	 */
	public void setMailGroup(String mailGroup) {
		this.mailGroup = mailGroup;
	}

}
