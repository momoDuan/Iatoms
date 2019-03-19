package com.cybersoft4u.xian.iatoms.common.domain;

import java.io.Serializable;

import cafe.core.web.controller.util.BindPageDataUtils;

/**
 * Purpose: 域资料对象
 * @author candicechen
 * @since  JDK 1.6
 * @date   2015年4月21日
 * @MaintenancePersonnel candicechen
 */
public class DomainUserInfoDTO implements Serializable {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4244538520380260359L;
	/**
	 * Purpose: 枚举类型参数
	 * @author candicechen
	 * @since  JDK 1.6
	 * @date   2015年4月23日
	 * @MaintenancePersonnel candicechen
	 */
	public static enum ATTRIBUTE {
		LOGIN_ACCOUNT("loginAccount"),
		OBJECT_CLASS("objectClass"),
		S_A_M_ACCOUNT_NAME("sAMAccountName"),
		CN("cn"),
		GIVEN_NAME("givenName"),
		SN("sn"),
		DISPLAY_NAME("displayName"),
		DESCRIPTION("description"),
		USER_PRINCIPAL_NAME("userPrincipalName"),
		MAIL("mail"),
		TELEPHONE_NUMBER("telephoneNumber"),
		USER_ACCOUNT_CONTROL("userAccountControl"),
		PASSWORD("password"),
		S_A_M_PWD("sAMPwd");

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	/**當前登陸的的賬號*/
	private String loginAccount;
	/** 用戶級別 分用戶,組..等 */
	private String objectClass;
	/**用戶的登錄賬號*/
	private String sAMAccountName;
	/** 用戶的全稱 */
	private String cn;
	/** 用戶的名 */
	private String givenName;
	/** 用戶的姓*/
	private String sn;
	/** 用戶的展示名(全名) */
	private String displayName;
	/** 用戶的描述 */
	private String description;
	/** 用戶的代理名* */
	private String userPrincipalName;
	/** 用戶的郵件 */
	private String mail;
	/** 用戶的電話*/
	private String telephoneNumber;
	/** 用戶的權限 */
	private String userAccountControl;
	/**用戶的密碼(加密后的)*/
	private String password;
	/**
	 * Constructor:无参构造函数
	 */
	public DomainUserInfoDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * Constructor:带参构造函数
	 */
	public DomainUserInfoDTO(String loginAccount, String objectClass,
			String sAMAccountName, String cn, String givenName, String sn,
			String displayName, String description, String userPrincipalName,
			String mail, String telephoneNumber, String userAccountControl,
			String password) {
		super();
		this.loginAccount = loginAccount;
		this.objectClass = objectClass;
		this.sAMAccountName = sAMAccountName;
		this.cn = cn;
		this.givenName = givenName;
		this.sn = sn;
		this.displayName = displayName;
		this.description = description;
		this.userPrincipalName = userPrincipalName;
		this.mail = mail;
		this.telephoneNumber = telephoneNumber;
		this.userAccountControl = userAccountControl;
		this.password = password;
	}
	/**
	 * @return the loginAccount
	 */
	public String getLoginAccount() {
		return loginAccount;
	}
	/**
	 * @param loginAccount the loginAccount to set
	 */
	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}
	/**
	 * @return the objectClass
	 */
	public String getObjectClass() {
		return objectClass;
	}
	/**
	 * @param objectClass the objectClass to set
	 */
	public void setObjectClass(String objectClass) {
		this.objectClass = objectClass;
	}
	/**
	 * @return the sAMAccountName
	 */
	public String getsAMAccountName() {
		return sAMAccountName;
	}
	/**
	 * @param sAMAccountName the sAMAccountName to set
	 */
	public void setsAMAccountName(String sAMAccountName) {
		this.sAMAccountName = sAMAccountName;
	}
	/**
	 * @return the cn
	 */
	public String getCn() {
		return cn;
	}
	/**
	 * @param cn the cn to set
	 */
	public void setCn(String cn) {
		this.cn = cn;
	}
	/**
	 * @return the givenName
	 */
	public String getGivenName() {
		return givenName;
	}
	/**
	 * @param givenName the givenName to set
	 */
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	/**
	 * @return the sn
	 */
	public String getSn() {
		return sn;
	}
	/**
	 * @param sn the sn to set
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}
	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the userPrincipalName
	 */
	public String getUserPrincipalName() {
		return userPrincipalName;
	}
	/**
	 * @param userPrincipalName the userPrincipalName to set
	 */
	public void setUserPrincipalName(String userPrincipalName) {
		this.userPrincipalName = userPrincipalName;
	}
	/**
	 * @return the mail
	 */
	public String getMail() {
		return mail;
	}
	/**
	 * @param mail the mail to set
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}
	/**
	 * @return the telephoneNumber
	 */
	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	/**
	 * @param telephoneNumber the telephoneNumber to set
	 */
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
	/**
	 * @return the userAccountControl
	 */
	public String getUserAccountControl() {
		return userAccountControl;
	}
	/**
	 * @param userAccountControl the userAccountControl to set
	 */
	public void setUserAccountControl(String userAccountControl) {
		this.userAccountControl = userAccountControl;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public static void main(String[] argv) {
		BindPageDataUtils.generateAttributeEnum(DomainUserInfoDTO.class);
	}
}
