package com.cybersoft4u.xian.iatoms.common.bean.dto;
import cafe.core.bean.dto.DataTransferObject;
/**
 * 
 * Purpose: 郵遞區號信息 
 * @author neiljing
 * @since  JDK 1.7
 * @date   2018/5/15
 * @MaintenancePersonnel neiljing
 */
public class BaseParameterPostCodeDTO extends DataTransferObject<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -113443989412482690L;
	/**
	 * 
	 * Purpose: 郵遞區號ATTRIBUTE
	 * @author neiljing
	 * @since  JDK 1.7
	 * @date   2018/5/15
	 * @MaintenancePersonnel neiljing
	 */
	public static enum ATTRIBUTE {
		POST_CODE_ID("postCodeId"),
		CITY_ID("cityId"),
		POST_NAME("postName"),
		POST_CODE("postCode");

		/**
		 * value值
		 */
		private String value;
		
		/**
		 * Constructor:構造函數
		 */
		ATTRIBUTE(String value) {
			this.value = value;
		};
		
		/**
		 * @return the value
		 */
		public String getValue() {
			return this.value;
		}
	};

	/**
	 * 郵遞區號編號
	 */
	private String postCodeId;
	/**
	 * 縣市編號
	 */
	private String cityId;
	/**
	 * 郵遞區域
	 */
	private String postName;
	/**
	 * 郵遞區號
	 */
	private String postCode;
	/**
	 * Constructor:建構子
	 */
	public BaseParameterPostCodeDTO() {
	}
	/**
	 * 
	 * Constructor:建構子
	 */
	public BaseParameterPostCodeDTO(String postCodeId, String cityId, String postName, String postCode) {
		this.postCodeId = postCodeId;
		this.cityId = cityId;
		this.postName = postName;
		this.postCode = postCode;
	}
	public String getPostCodeId() {
		return postCodeId;
	}
	public void setPostCodeId(String postCodeId) {
		this.postCodeId = postCodeId;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getPostName() {
		return postName;
	}
	public void setPostName(String postName) {
		this.postName = postName;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
}
