package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.util.Date;

import cafe.core.bean.dto.DataTransferObject;

/**
 * Purpose: 功能列表
 * @author evanliu
 * @since  JDK 1.6
 * @date   2016年7月5日
 * @MaintenancePersonnel evanliu
 */
public class AdmFunctionDTO extends DataTransferObject<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8091410197056870605L;
	/**
	 * 功能id
	 */
	private String functionId;
	/**
	 * 功能名稱
	 */
	private String functionName;
	/**
	 * 功能描述
	 */
	private String functionDescription;
	/**
	 * 地址
	 */
	private String functionUrl;
	/**
	 * 父功能id
	 */
	private String parentFunctionId;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 是否forder
	 */
	private Boolean isFolder;
	/**
	 * 顯示
	 */
	private Boolean isShow;
	/**
	 * 是否可用
	 */
	private Boolean isEnabled;
	/**
	 * 創建人員
	 */
	private String createdById;
	/**
	 * 創建人員姓名
	 */
	private String createdByName;
	/**
	 * 創建時間
	 */
	private Date createdDate;
	/**
	 * 修改人員
	 */
	private String updatedById;
	/**
	 * 修改人員姓名
	 */
	private String updatedByName;
	/**
	 * 修改時間
	 */
	private Date updatedDate;
	/**
	 * 
	 * Constructor:無參建構
	 */
	public AdmFunctionDTO() {
	}
	/**
	 * Constructor:有參建構
	 */
	public AdmFunctionDTO(String functionId, String functionName,
			String functionDescription, String functionUrl,
			String parentFunctionId, Integer sort, Boolean isFolder,
			Boolean isShow, Boolean isEnabled, String createdById,
			String createdByName, Date createdDate, String updatedById,
			String updatedByName, Date updatedDate) {
		this.functionId = functionId;
		this.functionName = functionName;
		this.functionDescription = functionDescription;
		this.functionUrl = functionUrl;
		this.parentFunctionId = parentFunctionId;
		this.sort = sort;
		this.isFolder = isFolder;
		this.isShow = isShow;
		this.isEnabled = isEnabled;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
	}
	/**
	 * @return the functionId
	 */
	public String getFunctionId() {
		return functionId;
	}
	/**
	 * @param functionId the functionId to set
	 */
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
	/**
	 * @return the functionName
	 */
	public String getFunctionName() {
		return functionName;
	}
	/**
	 * @param functionName the functionName to set
	 */
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	/**
	 * @return the functionDescription
	 */
	public String getFunctionDescription() {
		return functionDescription;
	}
	/**
	 * @param functionDescription the functionDescription to set
	 */
	public void setFunctionDescription(String functionDescription) {
		this.functionDescription = functionDescription;
	}
	/**
	 * @return the functionUrl
	 */
	public String getFunctionUrl() {
		return functionUrl;
	}
	/**
	 * @param functionUrl the functionUrl to set
	 */
	public void setFunctionUrl(String functionUrl) {
		this.functionUrl = functionUrl;
	}
	/**
	 * @return the parentFunctionId
	 */
	public String getParentFunctionId() {
		return parentFunctionId;
	}
	/**
	 * @param parentFunctionId the parentFunctionId to set
	 */
	public void setParentFunctionId(String parentFunctionId) {
		this.parentFunctionId = parentFunctionId;
	}
	/**
	 * @return the sort
	 */
	public Integer getSort() {
		return sort;
	}
	/**
	 * @param sort the sort to set
	 */
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	/**
	 * @return the isFolder
	 */
	public Boolean getIsFolder() {
		return isFolder;
	}
	/**
	 * @param isFolder the isFolder to set
	 */
	public void setIsFolder(Boolean isFolder) {
		this.isFolder = isFolder;
	}
	/**
	 * @return the isShow
	 */
	public Boolean getIsShow() {
		return isShow;
	}
	/**
	 * @param isShow the isShow to set
	 */
	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}
	/**
	 * @return the isEnabled
	 */
	public Boolean getIsEnabled() {
		return isEnabled;
	}
	/**
	 * @param isEnabled the isEnabled to set
	 */
	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
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
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
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
	public Date getUpdatedDate() {
		return updatedDate;
	}
	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
}
