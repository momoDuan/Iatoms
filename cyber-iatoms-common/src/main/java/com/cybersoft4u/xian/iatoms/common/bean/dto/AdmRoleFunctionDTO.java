package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.util.Date;

import com.cybersoft4u.xian.iatoms.common.bean.dto.WarehouseDTO.ATTRIBUTE;

import cafe.core.bean.dto.DataTransferObject;
import cafe.core.web.controller.util.BindPageDataUtils;

/**
 * Purpose: 系統角色功能DTO
 * @author HaimingWang
 * @since  JDK 1.6
 * @date   2016/6/28
 * @MaintenancePersonnel HaimingWang
 */
public class AdmRoleFunctionDTO extends DataTransferObject<String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7360039403226670612L;

	public static enum ATTRIBUTE {
		ROLE_ID("roleId"),
		FUNCTION_ID("functionId"),
		FUNCTION_NAME("functionName"),
		IS_QUERY("isQuery"),
		IS_ADD("isAdd"),
		IS_EDIT("isEdit"),
		IS_DELETE("isDelete"),
		IS_REPORT("isReport"),
		IS_EXPORT("isExport"),
		IS_PRINT("isPrint"),
		IS_EXT_RIGHT1("isExtRight1"),
		IS_EXT_RIGHT2("isExtRight2"),
		IS_EXT_RIGHT3("isExtRight3"),
		IS_EXT_RIGHT4("isExtRight4"),
		IS_EXT_RIGHT5("isExtRight5"),
		CREATE_USER("createUser"),
		CREATE_USER_NAME("createUserName"),
		CREATE_DATE("createDate"),
		UPDATE_USER("updateUser"),
		UPDATE_USER_NAME("updateUserName"),
		UPDATE_DATE("updateDate");

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	
	/**
	 * 角色編號
	 */
	private int roleId;
	
	/**
	 * 功能編號
	 */
	private String functionId;
	
	/**
	 * 功能名稱
	 */
	private String functionName;
	
	/**
	 * 查詢權限
	 */
	private int isQuery;
	
	/**
	 * 新增權限
	 */
	private int isAdd;
	
	/**
	 * 修改權限
	 */
	private int isEdit;
	
	/**
	 * 刪除權限
	 */
	private int isDelete;
	
	/**
	 * 報告權限
	 */
	private int isReport;
	
	/**
	 * 匯出權限
	 */
	private int isExport;
	
	/**
	 * 打印權限
	 */
	private int isPrint;
	
	/**
	 * 額外權限1
	 */
	private int isExtRight1;
	
	/**
	 * 額外權限2
	 */
	private int isExtRight2;
	
	/**
	 * 額外權限3
	 */
	private int isExtRight3;
	
	/**
	 * 額外權限4
	 */
	private int isExtRight4;
	
	/**
	 * 額外權限5
	 */
	private int isExtRight5;
	
	/**
	 * 建立人員編號
	 */
	private String createUser;
	
	/**
	 * 建立人員姓名
	 */
	private String createUserName;
	
	/**
	 * 建立日期
	 */
	private Date createDate;
	
	/**
	 * 異動人員編號
	 */
	private String updateUser;
	
	/**
	 * 異動人員姓名
	 */
	private String updateUserName;
	
	/**
	 * 異動日期
	 */
	private Date updateDate;

	/**
	 * Constructor:無參構造
	 */
	public AdmRoleFunctionDTO() {
	}

	/**
	 * Constructor:有參構造
	 */
	public AdmRoleFunctionDTO(int roleId, String functionId,
			String functionName, int isQuery, int isAdd, int isEdit,
			int isDelete, int isReport, int isExport, int isPrint,
			int isExtRight1, int isExtRight2, int isExtRight3, int isExtRight4,
			int isExtRight5, String createUser, String createUserName,
			Date createDate, String updateUser, String updateUserName,
			Date updateDate) {
		this.roleId = roleId;
		this.functionId = functionId;
		this.functionName = functionName;
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

	/**
	 * @return the roleId
	 */
	public int getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(int roleId) {
		this.roleId = roleId;
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
	 * @return the isQuery
	 */
	public int getIsQuery() {
		return isQuery;
	}

	/**
	 * @param isQuery the isQuery to set
	 */
	public void setIsQuery(int isQuery) {
		this.isQuery = isQuery;
	}

	/**
	 * @return the isAdd
	 */
	public int getIsAdd() {
		return isAdd;
	}

	/**
	 * @param isAdd the isAdd to set
	 */
	public void setIsAdd(int isAdd) {
		this.isAdd = isAdd;
	}

	/**
	 * @return the isEdit
	 */
	public int getIsEdit() {
		return isEdit;
	}

	/**
	 * @param isEdit the isEdit to set
	 */
	public void setIsEdit(int isEdit) {
		this.isEdit = isEdit;
	}

	/**
	 * @return the isDelete
	 */
	public int getIsDelete() {
		return isDelete;
	}

	/**
	 * @param isDelete the isDelete to set
	 */
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	/**
	 * @return the isReport
	 */
	public int getIsReport() {
		return isReport;
	}

	/**
	 * @param isReport the isReport to set
	 */
	public void setIsReport(int isReport) {
		this.isReport = isReport;
	}

	/**
	 * @return the isExport
	 */
	public int getIsExport() {
		return isExport;
	}

	/**
	 * @param isExport the isExport to set
	 */
	public void setIsExport(int isExport) {
		this.isExport = isExport;
	}

	/**
	 * @return the isPrint
	 */
	public int getIsPrint() {
		return isPrint;
	}

	/**
	 * @param isPrint the isPrint to set
	 */
	public void setIsPrint(int isPrint) {
		this.isPrint = isPrint;
	}

	/**
	 * @return the isExtRight1
	 */
	public int getIsExtRight1() {
		return isExtRight1;
	}

	/**
	 * @param isExtRight1 the isExtRight1 to set
	 */
	public void setIsExtRight1(int isExtRight1) {
		this.isExtRight1 = isExtRight1;
	}

	/**
	 * @return the isExtRight2
	 */
	public int getIsExtRight2() {
		return isExtRight2;
	}

	/**
	 * @param isExtRight2 the isExtRight2 to set
	 */
	public void setIsExtRight2(int isExtRight2) {
		this.isExtRight2 = isExtRight2;
	}

	/**
	 * @return the isExtRight3
	 */
	public int getIsExtRight3() {
		return isExtRight3;
	}

	/**
	 * @param isExtRight3 the isExtRight3 to set
	 */
	public void setIsExtRight3(int isExtRight3) {
		this.isExtRight3 = isExtRight3;
	}

	/**
	 * @return the isExtRight4
	 */
	public int getIsExtRight4() {
		return isExtRight4;
	}

	/**
	 * @param isExtRight4 the isExtRight4 to set
	 */
	public void setIsExtRight4(int isExtRight4) {
		this.isExtRight4 = isExtRight4;
	}

	/**
	 * @return the isExtRight5
	 */
	public int getIsExtRight5() {
		return isExtRight5;
	}

	/**
	 * @param isExtRight5 the isExtRight5 to set
	 */
	public void setIsExtRight5(int isExtRight5) {
		this.isExtRight5 = isExtRight5;
	}

	/**
	 * @return the createUser
	 */
	public String getCreateUser() {
		return createUser;
	}

	/**
	 * @param createUser the createUser to set
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	/**
	 * @return the createUserName
	 */
	public String getCreateUserName() {
		return createUserName;
	}

	/**
	 * @param createUserName the createUserName to set
	 */
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the updateUser
	 */
	public String getUpdateUser() {
		return updateUser;
	}

	/**
	 * @param updateUser the updateUser to set
	 */
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	/**
	 * @return the updateUserName
	 */
	public String getUpdateUserName() {
		return updateUserName;
	}

	/**
	 * @param updateUserName the updateUserName to set
	 */
	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	/**
	 * @return the updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
