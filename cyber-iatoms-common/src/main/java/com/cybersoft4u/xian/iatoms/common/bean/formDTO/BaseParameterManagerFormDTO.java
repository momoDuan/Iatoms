package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BaseParameterItemDefDTO;

/**
 * 
 * Purpose: 系統參數維護FormDTO
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016年6月27日
 * @MaintenancePersonnel CrissZhang
 */
@SuppressWarnings("rawtypes")
public class BaseParameterManagerFormDTO extends AbstractSimpleListFormDTO {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -3513609627131864877L;
	
	/**
	 * 查詢條件--參數類型
	 */
	public static final String QUERY_PAGE_PARAM_TYPE						= "queryParamType";
	
	/**
	 * 查詢條件--參數代碼
	 */
	public static final String QUERY_PAGE_PARAM_CODE						= "queryParamCode";
	
	/**
	 * 查詢條件--參數名稱
	 */
	public static final String QUERY_PAGE_PARAM_NAME						= "queryParamName";
	
	/**
	 * 排序方式
	 */
	public static final String QUERY_PAGE_PARAM_ORDER						= "order";
	
	/**
	 * 排序字段
	 */
	public static final String QUERY_PAGE_PARAM_SORT						= "sort";
	
	/**
	 * 編輯參數代碼
	 */
	public static final String EDIT_BPTD_CODE								= "editBptdCode";
	
	/**
	 * 編輯參數編號
	 */
	public static final String EDIT_BPID_ID									= "editBpidId";
	
	/**
	 * 編輯啟用日期
	 */
	public static final String EDIT_EFFECITVE_DATE							= "editEffectiveDate";
	
	/**
	 * 編輯參數值
	 */
	public static final String EDIT_ITEM_VALUE								= "editItemValue";
	
	/**
	 * 參數代碼
	 */
	public static final String PARAMETER_TYPE								= "PARAMETER_TYPE";
	
	/**
	 * 父类别bptd_code
	 */
	public static final String PARAMETER_PARENT_BPTD_CODE                   = "parentBptdCode";
	
	/**
	 * 父類別item_value
	 */
	public static final String PARAMETER_PARENT_ITEM_VALUE                  = "parentBptdValue";
	
	/**
	 * 子類別bptd_code
	 */
	public static final String PARAMETER_CHILDREN_BPTD_CODE                 = "childrenBptdCode";
	/**
	 * 子類別TEXT_FIELD1
	 */
	public static final String PARAMETER_TEXT_FIELD1                		 = "textField1";
	
	/**
	 * 默認排序方式
	 */
	public static final String PARAM_PAGE_SORT = "ptName,itemValue";
	
	/**
	 * 查詢條件--參數類型
	 */
	private String queryParamType;

	/**
	 * 查詢條件--參數代碼
	 */
	private String queryParamCode;
	
	/**
	 * 查詢條件--參數名稱
	 */
	private String queryParamName;
	
	/**
	 * 編輯條件--參數編號
	 */
	private String editBpidId;

	/**
	 * 編輯條件--參數類型
	 */
	private String editBptdCode;

	/**
	 * 編輯條件--生效日期
	 */
	private String editEffectiveDate;

	/**
	 * 編輯條件--參數代碼
	 */
	private String editItemValue;
	
	/**
	 * 檢核條件--參數類型
	 */
	private String checkBptdCode;
	
	/**
	 * 檢核條件--參數代碼
	 */
	private String checkItemValue;
	
	/**
	 * 排序方式
	 */
	private String sortDirection;
	
	/**
	 * 排序字段
	 */
	private String sortFieldName;
	
	/**
	 * 封装对象
	 */
	private BaseParameterItemDefDTO baseParameterItemDefDTO;
	
	/**
	 * Constructor: 無參構造子
	 */
	public BaseParameterManagerFormDTO (){
	}
	
	/**
	 * @return the queryParamType
	 */
	public String getQueryParamType() {
		return queryParamType;
	}
	/**
	 * @param queryParamType the queryParamType to set
	 */
	public void setQueryParamType(String queryParamType) {
		this.queryParamType = queryParamType;
	}
	/**
	 * @return the queryParamCode
	 */
	public String getQueryParamCode() {
		return queryParamCode;
	}
	/**
	 * @param queryParamCode the queryParamCode to set
	 */
	public void setQueryParamCode(String queryParamCode) {
		this.queryParamCode = queryParamCode;
	}
	/**
	 * @return the queryParamName
	 */
	public String getQueryParamName() {
		return queryParamName;
	}
	/**
	 * @param queryParamName the queryParamName to set
	 */
	public void setQueryParamName(String queryParamName) {
		this.queryParamName = queryParamName;
	}


	/**
	 * @return the sortDirection
	 */
	public String getSortDirection() {
		return sortDirection;
	}

	/**
	 * @param sortDirection the sortDirection to set
	 */
	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	/**
	 * @return the sortFieldName
	 */
	public String getSortFieldName() {
		return sortFieldName;
	}

	/**
	 * @param sortFieldName the sortFieldName to set
	 */
	public void setSortFieldName(String sortFieldName) {
		this.sortFieldName = sortFieldName;
	}

	/**
	 * @return the baseParameterItemDefDTO
	 */
	public BaseParameterItemDefDTO getBaseParameterItemDefDTO() {
		return baseParameterItemDefDTO;
	}

	/**
	 * @param baseParameterItemDefDTO the baseParameterItemDefDTO to set
	 */
	public void setBaseParameterItemDefDTO(
			BaseParameterItemDefDTO baseParameterItemDefDTO) {
		this.baseParameterItemDefDTO = baseParameterItemDefDTO;
	}

	/**
	 * @return the editBpidId
	 */
	public String getEditBpidId() {
		return editBpidId;
	}

	/**
	 * @param editBpidId the editBpidId to set
	 */
	public void setEditBpidId(String editBpidId) {
		this.editBpidId = editBpidId;
	}

	/**
	 * @return the editBptdCode
	 */
	public String getEditBptdCode() {
		return editBptdCode;
	}

	/**
	 * @param editBptdCode the editBptdCode to set
	 */
	public void setEditBptdCode(String editBptdCode) {
		this.editBptdCode = editBptdCode;
	}

	/**
	 * @return the editEffectiveDate
	 */
	public String getEditEffectiveDate() {
		return editEffectiveDate;
	}

	/**
	 * @param editEffectiveDate the editEffectiveDate to set
	 */
	public void setEditEffectiveDate(String editEffectiveDate) {
		this.editEffectiveDate = editEffectiveDate;
	}

	/**
	 * @return the editItemValue
	 */
	public String getEditItemValue() {
		return editItemValue;
	}

	/**
	 * @param editItemValue the editItemValue to set
	 */
	public void setEditItemValue(String editItemValue) {
		this.editItemValue = editItemValue;
	}

	/**
	 * @return the checkBptdCode
	 */
	public String getCheckBptdCode() {
		return checkBptdCode;
	}

	/**
	 * @param checkBptdCode the checkBptdCode to set
	 */
	public void setCheckBptdCode(String checkBptdCode) {
		this.checkBptdCode = checkBptdCode;
	}

	/**
	 * @return the checkItemValue
	 */
	public String getCheckItemValue() {
		return checkItemValue;
	}

	/**
	 * @param checkItemValue the checkItemValue to set
	 */
	public void setCheckItemValue(String checkItemValue) {
		this.checkItemValue = checkItemValue;
	}

}
