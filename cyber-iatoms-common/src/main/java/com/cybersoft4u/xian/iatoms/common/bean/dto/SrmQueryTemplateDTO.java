package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.dto.DataTransferObject;

/**
 * Purpose: 用戶欄位模板維護檔DTO
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2017/3/1
 * @MaintenancePersonnel CrissZhang
 */
public class SrmQueryTemplateDTO extends DataTransferObject<String> {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 2453315587755776177L;

	/**
	 * Purpose: enum
	 * @author CrissZhang
	 * @since  JDK 1.6
	 * @date   2017/3/1
	 * @MaintenancePersonnel CrissZhang
	 */
	public static enum ATTRIBUTE {
		TEMPLATE_ID("templateId"),
		USER_ID("userId"),
		TEMPLATE_NAME("templateName"),
		
		FIELD_CONTENT("fieldContent");

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	/**
	 * 編號
	 */
	private String templateId;
	
	/**
	 * 模板名稱
	 */
	private String templateName;
	
	/**
	 * 用戶編號
	 */
	private String userId;
	
	/**
	 * 詳細內容
	 */
	private String fieldContent;
	
	/**
	 * 是否為預設
	 */
	private String isDefault = "N";
	

	/**
	 * Constructor:無參構造函數
	 */
	public SrmQueryTemplateDTO() {
	}


	/**
	 * @return the templateId
	 */
	public String getTemplateId() {
		return templateId;
	}

	/**
	 * @param templateId the templateId to set
	 */
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}


	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the fieldContent
	 */
	public String getFieldContent() {
		return fieldContent;
	}


	/**
	 * @param fieldContent the fieldContent to set
	 */
	public void setFieldContent(String fieldContent) {
		this.fieldContent = fieldContent;
	}


	/**
	 * @return the templateName
	 */
	public String getTemplateName() {
		return templateName;
	}


	/**
	 * @param templateName the templateName to set
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}


	/**
	 * @return the isDefault
	 */
	public String getIsDefault() {
		return isDefault;
	}


	/**
	 * @param isDefault the isDefault to set
	 */
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

}
