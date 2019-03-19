package com.cybersoft4u.xian.iatoms.common;

import cafe.core.bean.identity.AccessControl;

/**
 * 
 * Purpose: 權限控制訊息
 * @author allenchen	
 * @since  JDK 1.7
 * @date   2016/4/29
 * @MaintenancePersonnel allenchen
 */
public class IAtomsAccessControl extends AccessControl{

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 297254658951280874L;
	
	/**
	 * 常量定義
	 */
	public static final String FIELD_PARENT_NAME = "parentName";
	
	public static final String FIELD_FUNCTION_RIGHT = "functionRight";
	
	/**
	 * 父節點名稱
	 */
	private String parentName;
	/**
	 * 權限控管標記位
	 */
	private String functionRight;

	/**
	 * Constructor--.構建函數
	 */
	public IAtomsAccessControl() {
		
	}

	/**
	 * @return the parentName
	 */
	public String getParentName() {
		return parentName;
	}

	/**
	 * @param parentName the parentName to set
	 */
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	/**
	 * @return the functionRight
	 */
	public String getFunctionRight() {
		return functionRight;
	}

	/**
	 * @param functionRight the functionRight to set
	 */
	public void setFunctionRight(String functionRight) {
		this.functionRight = functionRight;
	}
}
