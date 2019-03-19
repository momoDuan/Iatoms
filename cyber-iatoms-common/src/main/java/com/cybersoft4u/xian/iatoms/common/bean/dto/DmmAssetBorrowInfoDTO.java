package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;
import java.util.List;

import cafe.core.bean.dto.DataTransferObject;
/**
 * Purpose: 设备借用DTO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2018年7月30日
 * @MaintenancePersonnel CarrieDuan
 */
public class DmmAssetBorrowInfoDTO extends DataTransferObject<String> {
	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 2380044162698206492L;
	
	public static enum ATTRIBUTE {
		BORROW_CASE_ID("borrowCaseId"),
		BORROW_USER("borrowUser"),
		COMMENT("comment"),
		BORROW_CATEGORY("borrowCategory"),
		BORROW_STATUS("borrowStatus"),
		BORROW_START_DATE("borrowStartDate"),
		BORROW_END_DATE("borrowEndDate"),
		DIRECTOR_CHECK_BY_ID("directorCheckById"),
		DIRECTOR_CHECK_BY_NAME("directorCheckByName"),
		DIRECTOR_CHECK_BY_DATE("directorCheckByDate"),
		AGENT_BY_ID("agentById"),
		AGENT_BY_NAME("agentByName"),
		AGENT_BY_DATE("agentByDate"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
		CREATED_DATE("createdDate"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		UPDATED_DATE("updatedDate"),
		ASSET_TYPE_NAME("assetTypeName"),
		ASSET_CATEGORY_NAME("assetCategoryName"),
		BORROW_NUMBER("borrowNumber"),
		DIRECTOR_BACK("directorBack"),
		ASSET_TYPE_ID("assetTypeId"),
		SERIAL_NUMBER("serialNumber"),
		BORROW_LIST_ID("borrowListId");

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	/**
	 * 借用設備id
	 */
	private String borrowCaseId;
	/**
	 * 設備借用人
	 */
	private String borrowUser;
	/**
	 * 說明
	 */
	private String comment;
	/**
	 * 案件類別
	 */
	private String borrowCategory;
	/**
	 * 案件狀態
	 */
	private String borrowStatus;
	/**
	 * 案件狀態名稱
	 */
	private String borrowStatusName;
	/**
	 * 借用日期起
	 */
	private Timestamp borrowStartDate;
	/**
	 * 借用日期迄
	 */
	private Timestamp borrowEndDate;
	/**
	 * 倉管主管審核人員id
	 */
	private String directorCheckById;
	/**
	 * 倉管主管審核人員審核名稱
	 */
	private String directorCheckByName;
	/**
	 * 倉管主管審核日期
	 */
	private Timestamp directorCheckByDate;
	/**
	 * 倉管經辦審核人員id
	 */
	private String agentById;
	/**
	 * 倉管經辦審核名稱
	 */
	private String agentByName;
	/**
	 * 倉管經辦審核日期
	 */
	private Timestamp agentByDate;
	/**
	 * 新增人員id
	 */
	private String createdById;
	/**
	 * 新增人員名稱
	 */
	private String createdByName;
	/**
	 * 新增人員日期
	 */
	private Timestamp createdDate;
	/**
	 * 異動人員id
	 */
	private String updatedById;
	/**
	 * 異動人員名稱
	 */
	private String updatedByName;
	/**
	 * 異動人員日期
	 */
	private Timestamp updatedDate;
	/**
	 * 設備名稱
	 */
	private String assetTypeName;
	/**
	 * 設備id
	 */
	private String assetTypeId;
	/**
	 * 設備類別名稱
	 */
	private String assetCategoryName;
	/**
	 * 借用數量
	 */
	private String borrowNumber;
	/**
	 * 倉管主管是否返回
	 */
	private String directorBack;
	
	/**
	 * 設備借用數量DTO
	 */
	private List<DmmAssetBorrowNumberDTO> assetBorrowNumberDTOs;
	/**
	 * 記錄行號
	 */
	private int rowNumber;
	/**
	 * 是否倉管經辦處理
	 */
	private String isAgentProcess = "N";
	/**
	 * 設備序號
	 */
	private String serialNumber;
	/**
	 * 設備管理id
	 */
	private String assetId;
	private String borrowListId;
	private String serialNumberHidden;
	private String sendToMail;
	private String backComment;
	/**
	 * Constructor:
	 */
	public DmmAssetBorrowInfoDTO() {
	}
	
	/**
	 * Constructor:
	 */
	public DmmAssetBorrowInfoDTO(String borrowCaseId, String borrowUser,
			String comment, String borrowCategory, String borrowStatus,
			Timestamp borrowStartDate, Timestamp borrowEndDate,
			String directorCheckById, String directorCheckByName,
			Timestamp directorCheckByDate, String agentById,
			String agentByName, Timestamp agentByDate, String createdById,
			String createdByName, Timestamp createdDate, String updatedById,
			String updatedByName, Timestamp updatedDate) {
		this.borrowCaseId = borrowCaseId;
		this.borrowUser = borrowUser;
		this.comment = comment;
		this.borrowCategory = borrowCategory;
		this.borrowStatus = borrowStatus;
		this.borrowStartDate = borrowStartDate;
		this.borrowEndDate = borrowEndDate;
		this.directorCheckById = directorCheckById;
		this.directorCheckByName = directorCheckByName;
		this.directorCheckByDate = directorCheckByDate;
		this.agentById = agentById;
		this.agentByName = agentByName;
		this.agentByDate = agentByDate;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
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
	 * @return the borrowUser
	 */
	public String getBorrowUser() {
		return borrowUser;
	}
	/**
	 * @param borrowUser the borrowUser to set
	 */
	public void setBorrowUser(String borrowUser) {
		this.borrowUser = borrowUser;
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
	 * @return the borrowCategory
	 */
	public String getBorrowCategory() {
		return borrowCategory;
	}
	/**
	 * @param borrowCategory the borrowCategory to set
	 */
	public void setBorrowCategory(String borrowCategory) {
		this.borrowCategory = borrowCategory;
	}
	/**
	 * @return the borrowStatus
	 */
	public String getBorrowStatus() {
		return borrowStatus;
	}
	/**
	 * @param borrowStatus the borrowStatus to set
	 */
	public void setBorrowStatus(String borrowStatus) {
		this.borrowStatus = borrowStatus;
	}
	/**
	 * @return the borrowStartDate
	 */
	public Timestamp getBorrowStartDate() {
		return borrowStartDate;
	}
	/**
	 * @param borrowStartDate the borrowStartDate to set
	 */
	public void setBorrowStartDate(Timestamp borrowStartDate) {
		this.borrowStartDate = borrowStartDate;
	}
	/**
	 * @return the borrowEndDate
	 */
	public Timestamp getBorrowEndDate() {
		return borrowEndDate;
	}
	/**
	 * @param borrowEndDate the borrowEndDate to set
	 */
	public void setBorrowEndDate(Timestamp borrowEndDate) {
		this.borrowEndDate = borrowEndDate;
	}
	/**
	 * @return the directorCheckById
	 */
	public String getDirectorCheckById() {
		return directorCheckById;
	}
	/**
	 * @param directorCheckById the directorCheckById to set
	 */
	public void setDirectorCheckById(String directorCheckById) {
		this.directorCheckById = directorCheckById;
	}
	/**
	 * @return the directorCheckByName
	 */
	public String getDirectorCheckByName() {
		return directorCheckByName;
	}
	/**
	 * @param directorCheckByName the directorCheckByName to set
	 */
	public void setDirectorCheckByName(String directorCheckByName) {
		this.directorCheckByName = directorCheckByName;
	}
	/**
	 * @return the directorCheckByDate
	 */
	public Timestamp getDirectorCheckByDate() {
		return directorCheckByDate;
	}
	/**
	 * @param directorCheckByDate the directorCheckByDate to set
	 */
	public void setDirectorCheckByDate(Timestamp directorCheckByDate) {
		this.directorCheckByDate = directorCheckByDate;
	}
	/**
	 * @return the agentById
	 */
	public String getAgentById() {
		return agentById;
	}
	/**
	 * @param agentById the agentById to set
	 */
	public void setAgentById(String agentById) {
		this.agentById = agentById;
	}
	/**
	 * @return the agentByName
	 */
	public String getAgentByName() {
		return agentByName;
	}
	/**
	 * @param agentByName the agentByName to set
	 */
	public void setAgentByName(String agentByName) {
		this.agentByName = agentByName;
	}
	/**
	 * @return the agentByDate
	 */
	public Timestamp getAgentByDate() {
		return agentByDate;
	}
	/**
	 * @param agentByDate the agentByDate to set
	 */
	public void setAgentByDate(Timestamp agentByDate) {
		this.agentByDate = agentByDate;
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
	 * @return the assetBorrowNumberDTOs
	 */
	public List<DmmAssetBorrowNumberDTO> getAssetBorrowNumberDTOs() {
		return assetBorrowNumberDTOs;
	}

	/**
	 * @param assetBorrowNumberDTOs the assetBorrowNumberDTOs to set
	 */
	public void setAssetBorrowNumberDTOs(
			List<DmmAssetBorrowNumberDTO> assetBorrowNumberDTOs) {
		this.assetBorrowNumberDTOs = assetBorrowNumberDTOs;
	}

	/**
	 * @return the assetTypeName
	 */
	public String getAssetTypeName() {
		return assetTypeName;
	}

	/**
	 * @param assetTypeName the assetTypeName to set
	 */
	public void setAssetTypeName(String assetTypeName) {
		this.assetTypeName = assetTypeName;
	}

	/**
	 * @return the assetCategoryName
	 */
	public String getAssetCategoryName() {
		return assetCategoryName;
	}

	/**
	 * @param assetCategoryName the assetCategoryName to set
	 */
	public void setAssetCategoryName(String assetCategoryName) {
		this.assetCategoryName = assetCategoryName;
	}

	/**
	 * @return the borrowNumber
	 */
	public String getBorrowNumber() {
		return borrowNumber;
	}

	/**
	 * @param borrowNumber the borrowNumber to set
	 */
	public void setBorrowNumber(String borrowNumber) {
		this.borrowNumber = borrowNumber;
	}

	/**
	 * @return the rowNumber
	 */
	public int getRowNumber() {
		return rowNumber;
	}

	/**
	 * @param rowNumber the rowNumber to set
	 */
	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	/**
	 * @return the borrowStatusName
	 */
	public String getBorrowStatusName() {
		return borrowStatusName;
	}

	/**
	 * @param borrowStatusName the borrowStatusName to set
	 */
	public void setBorrowStatusName(String borrowStatusName) {
		this.borrowStatusName = borrowStatusName;
	}

	/**
	 * @return the directorBack
	 */
	public String getDirectorBack() {
		return directorBack;
	}

	/**
	 * @param directorBack the directorBack to set
	 */
	public void setDirectorBack(String directorBack) {
		this.directorBack = directorBack;
	}

	/**
	 * @return the isAgentProcess
	 */
	public String getIsAgentProcess() {
		return isAgentProcess;
	}

	/**
	 * @param isAgentProcess the isAgentProcess to set
	 */
	public void setIsAgentProcess(String isAgentProcess) {
		this.isAgentProcess = isAgentProcess;
	}

	/**
	 * @return the assetTypeId
	 */
	public String getAssetTypeId() {
		return assetTypeId;
	}

	/**
	 * @param assetTypeId the assetTypeId to set
	 */
	public void setAssetTypeId(String assetTypeId) {
		this.assetTypeId = assetTypeId;
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
	 * @return the assetId
	 */
	public String getAssetId() {
		return assetId;
	}

	/**
	 * @param assetId the assetId to set
	 */
	public void setAssetId(String assetId) {
		this.assetId = assetId;
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

	/**
	 * @return the serialNumberHidden
	 */
	public String getSerialNumberHidden() {
		return serialNumberHidden;
	}

	/**
	 * @param serialNumberHidden the serialNumberHidden to set
	 */
	public void setSerialNumberHidden(String serialNumberHidden) {
		this.serialNumberHidden = serialNumberHidden;
	}

	/**
	 * @return the sendToMail
	 */
	public String getSendToMail() {
		return sendToMail;
	}

	/**
	 * @param sendToMail the sendToMail to set
	 */
	public void setSendToMail(String sendToMail) {
		this.sendToMail = sendToMail;
	}

	/**
	 * @return the backComment
	 */
	public String getBackComment() {
		return backComment;
	}

	/**
	 * @param backComment the backComment to set
	 */
	public void setBackComment(String backComment) {
		this.backComment = backComment;
	}
	
	
	
}
