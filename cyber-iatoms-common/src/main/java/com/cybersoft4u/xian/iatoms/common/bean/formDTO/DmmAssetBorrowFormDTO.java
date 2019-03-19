package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.security.Timestamp;
import java.util.List;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetBorrowInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetBorrowNumberDTO;
/**
 * Purpose: 設備借用formDTO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2018年7月31日
 * @MaintenancePersonnel CarrieDuan
 */
public class DmmAssetBorrowFormDTO extends AbstractSimpleListFormDTO<DmmAssetBorrowInfoDTO> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7574515941110500953L;
	/**
	 * 借用日期起
	 */
	public static final String BORROW_START_DATE 											= "borrowStartDate";
	/**
	 * 借用日期迄
	 */
	public static final String BORROW_END_DATE 												= "borrowEndDate";
	/**
	 * 說明
	 */
	public static final String COMMENT 														= "comment";
	/**
	 * 案件編號-AB
	 */
	public static final String AB															= "AB";
	/**
	 * 當前登陸者角色
	 */
	public static final String PARAM_LOGIN_ROLES											= "loginRoles";
	/**
	 * 郵件主題模板
	 */
	public static final String PARAM_BORROW_MAIL_THEME										= "borrowMailTheme.txt";
	/**
	 * 郵件內容模板
	 */
	public static final String PARAM_BORROW_MAIL_CONTEXT									= "borrowMailContext.html";
	/**
	 * 
	 */
	public static final String PARAM_BORROW_BACK_MAIL_CONTEXT 								="borrowBackMailContext.html";
	/**
	 * 借用日期起
	 */
	private String borrowStartDate;
	/**
	 * 借用日期迄
	 */
	private String borrowEndDate;
	/**
	 * 說明
	 */
	private String comment;
	
	/**
	 * 排序方式
	 */
	private String sort;
	/**
	 * 排序字段
	 */
	private String order;
	/**
	 * 每頁顯示條數
	 */
	private Integer rows;
	
	/**
	 * 當前頁碼
	 */
	private Integer page;
	/**
	 * 保存借用設備json字符串
	 */
	private String assetBorrowNumber;
	/**
	 * 設備借用DTO
	 */
	private DmmAssetBorrowInfoDTO assetBorrowInfoDTO;
	
	/**
	 * 當前登陸者角色
	 */
	private String loginRoles;
	/**
	 * 案件查詢狀態
	 */
	private String caseStatus;
	/**
	 * 案件類別
	 */
	private String caseCategory;
	/**
	 * 設備案件編號
	 */
	private String borrowCaseId;
	/**
	 * 是否爲退回
	 */
	private String isBack;
	/**
	 * 是否倉管主管處理
	 */
	private String isDirectorCheck;
	/**
	 * 
	 */
	private List<DmmAssetBorrowInfoDTO> assetBorrowInfoDTOs; 
	/**
	 * 
	 */
	private String saveAssetBorrowInfoDTO;
	/**
	 * 
	 */
	private String delayDate;
	/**
	 * 
	 */
	private String borrowListId;
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
	 * @return the borrowStartDate
	 */
	public String getBorrowStartDate() {
		return borrowStartDate;
	}
	/**
	 * @param borrowStartDate the borrowStartDate to set
	 */
	public void setBorrowStartDate(String borrowStartDate) {
		this.borrowStartDate = borrowStartDate;
	}
	/**
	 * @return the borrowEndDate
	 */
	public String getBorrowEndDate() {
		return borrowEndDate;
	}
	/**
	 * @param borrowEndDate the borrowEndDate to set
	 */
	public void setBorrowEndDate(String borrowEndDate) {
		this.borrowEndDate = borrowEndDate;
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
	 * @return the assetBorrowNumber
	 */
	public String getAssetBorrowNumber() {
		return assetBorrowNumber;
	}
	/**
	 * @param assetBorrowNumber the assetBorrowNumber to set
	 */
	public void setAssetBorrowNumber(String assetBorrowNumber) {
		this.assetBorrowNumber = assetBorrowNumber;
	}
	/**
	 * @return the assetBorrowInfoDTO
	 */
	public DmmAssetBorrowInfoDTO getAssetBorrowInfoDTO() {
		return assetBorrowInfoDTO;
	}
	/**
	 * @param assetBorrowInfoDTO the assetBorrowInfoDTO to set
	 */
	public void setAssetBorrowInfoDTO(DmmAssetBorrowInfoDTO assetBorrowInfoDTO) {
		this.assetBorrowInfoDTO = assetBorrowInfoDTO;
	}
	/**
	 * @return the caseStatus
	 */
	public String getCaseStatus() {
		return caseStatus;
	}
	/**
	 * @param caseStatus the caseStatus to set
	 */
	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
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
	 * @return the loginRoles
	 */
	public String getLoginRoles() {
		return loginRoles;
	}
	/**
	 * @param loginRoles the loginRoles to set
	 */
	public void setLoginRoles(String loginRoles) {
		this.loginRoles = loginRoles;
	}
	/**
	 * @return the borrowCaseId
	 */
	public String getBorrowCaseId() {
		return borrowCaseId;
	}
	/**
	 * @param borrowCaseId the borrowCaseId to set
	 */
	public void setBorrowCaseId(String borrowCaseId) {
		this.borrowCaseId = borrowCaseId;
	}
	/**
	 * @return the caseCategory
	 */
	public String getCaseCategory() {
		return caseCategory;
	}
	/**
	 * @param caseCategory the caseCategory to set
	 */
	public void setCaseCategory(String caseCategory) {
		this.caseCategory = caseCategory;
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
	 * @return the isDirectorCheck
	 */
	public String getIsDirectorCheck() {
		return isDirectorCheck;
	}
	/**
	 * @param isDirectorCheck the isDirectorCheck to set
	 */
	public void setIsDirectorCheck(String isDirectorCheck) {
		this.isDirectorCheck = isDirectorCheck;
	}
	/**
	 * @return the assetBorrowInfoDTOs
	 */
	public List<DmmAssetBorrowInfoDTO> getAssetBorrowInfoDTOs() {
		return assetBorrowInfoDTOs;
	}
	/**
	 * @param assetBorrowInfoDTOs the assetBorrowInfoDTOs to set
	 */
	public void setAssetBorrowInfoDTOs(
			List<DmmAssetBorrowInfoDTO> assetBorrowInfoDTOs) {
		this.assetBorrowInfoDTOs = assetBorrowInfoDTOs;
	}
	/**
	 * @return the saveAssetBorrowInfoDTO
	 */
	public String getSaveAssetBorrowInfoDTO() {
		return saveAssetBorrowInfoDTO;
	}
	/**
	 * @param saveAssetBorrowInfoDTO the saveAssetBorrowInfoDTO to set
	 */
	public void setSaveAssetBorrowInfoDTO(String saveAssetBorrowInfoDTO) {
		this.saveAssetBorrowInfoDTO = saveAssetBorrowInfoDTO;
	}
	/**
	 * @return the delayDate
	 */
	public String getDelayDate() {
		return delayDate;
	}
	/**
	 * @param delayDate the delayDate to set
	 */
	public void setDelayDate(String delayDate) {
		this.delayDate = delayDate;
	}
	/**
	 * @return the borrowListId
	 */
	public String getBorrowListId() {
		return borrowListId;
	}
	/**
	 * @param borrowListId the borrowListId to set
	 */
	public void setBorrowListId(String borrowListId) {
		this.borrowListId = borrowListId;
	}
	
}
