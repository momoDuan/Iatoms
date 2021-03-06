package com.cybersoft4u.xian.iatoms.services.dmo;

// Generated 2016/6/3 �U�� 01:30:43 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleFunctionDTO;

/**
 * AdmRoleFunction generated by hbm2java
 */
public class AdmRoleFunction extends DomainModelObject<AdmRoleFunctionId,AdmRoleFunctionDTO> implements java.io.Serializable {

	private AdmRoleFunctionId id;
	private int isQuery;
	private int isAdd;
	private int isEdit;
	private int isDelete;
	private int isReport;
	private int isExport;
	private int isPrint;
	private int isExtRight1;
	private int isExtRight2;
	private int isExtRight3;
	private int isExtRight4;
	private int isExtRight5;
	private String createUser;
	private String createUserName;
	private Date createDate;
	private String updateUser;
	private String updateUserName;
	private Date updateDate;

	public AdmRoleFunction() {
	}

	public AdmRoleFunction(AdmRoleFunctionId id, int isQuery, int isAdd,
			int isEdit, int isDelete, int isReport, int isExport, int isPrint,
			int isExtRight1, int isExtRight2, int isExtRight3, int isExtRight4,
			int isExtRight5) {
		this.id = id;
		this.isQuery = isQuery;
		this.isAdd = isAdd;
		this.isEdit = isEdit;
		this.isDelete = isDelete;
		this.isReport = isReport;
		this.isExport = isExport;
		this.isPrint = isPrint;
		this.isExtRight1 = isExtRight1;
		this.isExtRight2 = isExtRight2;
		this.isExtRight3 = isExtRight3;
		this.isExtRight4 = isExtRight4;
		this.isExtRight5 = isExtRight5;
	}

	public AdmRoleFunction(AdmRoleFunctionId id, int isQuery, int isAdd,
			int isEdit, int isDelete, int isReport, int isExport, int isPrint,
			int isExtRight1, int isExtRight2, int isExtRight3, int isExtRight4,
			int isExtRight5, String createUser, String createUserName,
			Date createDate, String updateUser, String updateUserName,
			Date updateDate) {
		this.id = id;
		this.isQuery = isQuery;
		this.isAdd = isAdd;
		this.isEdit = isEdit;
		this.isDelete = isDelete;
		this.isReport = isReport;
		this.isExport = isExport;
		this.isPrint = isPrint;
		this.isExtRight1 = isExtRight1;
		this.isExtRight2 = isExtRight2;
		this.isExtRight3 = isExtRight3;
		this.isExtRight4 = isExtRight4;
		this.isExtRight5 = isExtRight5;
		this.createUser = createUser;
		this.createUserName = createUserName;
		this.createDate = createDate;
		this.updateUser = updateUser;
		this.updateUserName = updateUserName;
		this.updateDate = updateDate;
	}

	public AdmRoleFunctionId getId() {
		return this.id;
	}

	public void setId(AdmRoleFunctionId id) {
		this.id = id;
	}

	public int getIsQuery() {
		return this.isQuery;
	}

	public void setIsQuery(int isQuery) {
		this.isQuery = isQuery;
	}

	public int getIsAdd() {
		return this.isAdd;
	}

	public void setIsAdd(int isAdd) {
		this.isAdd = isAdd;
	}

	public int getIsEdit() {
		return this.isEdit;
	}

	public void setIsEdit(int isEdit) {
		this.isEdit = isEdit;
	}

	public int getIsDelete() {
		return this.isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public int getIsReport() {
		return this.isReport;
	}

	public void setIsReport(int isReport) {
		this.isReport = isReport;
	}

	public int getIsExport() {
		return this.isExport;
	}

	public void setIsExport(int isExport) {
		this.isExport = isExport;
	}

	public int getIsPrint() {
		return this.isPrint;
	}

	public void setIsPrint(int isPrint) {
		this.isPrint = isPrint;
	}

	public int getIsExtRight1() {
		return this.isExtRight1;
	}

	public void setIsExtRight1(int isExtRight1) {
		this.isExtRight1 = isExtRight1;
	}

	public int getIsExtRight2() {
		return this.isExtRight2;
	}

	public void setIsExtRight2(int isExtRight2) {
		this.isExtRight2 = isExtRight2;
	}

	public int getIsExtRight3() {
		return this.isExtRight3;
	}

	public void setIsExtRight3(int isExtRight3) {
		this.isExtRight3 = isExtRight3;
	}

	public int getIsExtRight4() {
		return this.isExtRight4;
	}

	public void setIsExtRight4(int isExtRight4) {
		this.isExtRight4 = isExtRight4;
	}

	public int getIsExtRight5() {
		return this.isExtRight5;
	}

	public void setIsExtRight5(int isExtRight5) {
		this.isExtRight5 = isExtRight5;
	}

	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateUserName() {
		return this.createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getUpdateUserName() {
		return this.updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}
