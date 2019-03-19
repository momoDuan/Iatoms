package com.cybersoft4u.xian.iatoms.services.dmo;

import java.sql.Timestamp;

import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetBorrowInfoDTO;

public class DmmAssetBorrowInfo extends DomainModelObject<String, DmmAssetBorrowInfoDTO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6022891666368754651L;
	private String borrowCaseId;
	private String borrowUser;
	private String comment;
	private String borrowCategory;
	private String borrowStatus;
	private Timestamp borrowStartDate;
	private Timestamp borrowEndDate;
	private String directorCheckById;
	private String directorCheckByName;
	private Timestamp directorCheckByDate;
	private String agentById;
	private String agentByName;
	private Timestamp agentByDate;
	private String createdById;
	private String createdByName;
	private Timestamp createdDate;
	private String updatedById;
	private String updatedByName;
	private Timestamp updatedDate;
	private String directorBack = "N";
	
	/**
	 * 
	 * Constructor:
	 */
	public DmmAssetBorrowInfo() {
	}
	
	/**
	 * 
	 * Constructor:
	 */
	public DmmAssetBorrowInfo(String borrowCaseId, String borrowUser,
			String comment, String borrowCategory, String borrowStatus,
			Timestamp borrowStartDate, Timestamp borrowEndDate,
			String directorCheckById, String directorCheckByName,
			Timestamp directorCheckByDate, String agentById,
			String agentByName, Timestamp agentByDate, String createdById,
			String createdByName, Timestamp createdDate, String updatedById,
			String updatedByName, Timestamp updatedDate, String directorBack) {
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
		this.directorBack = directorBack;
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
	 * @param string the createdById to set
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
	
	
}
