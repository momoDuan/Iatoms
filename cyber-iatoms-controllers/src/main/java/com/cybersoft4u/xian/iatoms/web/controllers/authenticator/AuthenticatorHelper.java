package com.cybersoft4u.xian.iatoms.web.controllers.authenticator;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;

import cafe.core.config.SystemConfigManager;
import cafe.core.exception.CommonException;
import cafe.core.util.CoreConstants;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.workflow.bean.WfMessageCode;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.domain.DomainUserInfoDTO;
import com.cybersoft4u.xian.iatoms.config.IAtomsSystemConfigManager;


/**
 * Purpose: 域账号资料处理
 * @author candicechen
 * @since  JDK 1.6
 * @date   2015年4月22日
 * @MaintenancePersonnel candicechen
 */
public class AuthenticatorHelper {
	private static CafeLog log = CafeLogFactory.getLog(IAtomsSystemConfigManager.LADP, AuthenticatorHelper.class);
	private static String ERROR_CODE_LOGON_ID_NOT_FOUND_OR_WRONG_PWD		= "error code 49";
	private static String NO_ATTRIBUTES										= "No attributes";
	/**
	 * 設置初始化上下文factory:com.sun.jndi.ldap.LdapCtxFactory
	 */
	private String ldapCtxFactory;
	/**
	 * 設置域的URL:ldap://192.168.92.30:389
	 */
	private String ldapUrl;
	/**
	 * 設置安全證明:simple
	 */
	private String ldapSecurity;
	/**
	 * 設置代理帳號:@cybersoft4u.com.tw
	 */
	private String ldapSecurityPrincipal;
	/**
	 *用戶級別:默認是 user 
	 */
	private String ldapLevel;
	/**
	 * 網域名:cybersoft4u.com.tw
	 */
	private String domainName;
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.web.controllers.authenticator.IAuthenticatorHelper#getDomainEmployee(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DomainUserInfoDTO getDomainEmployee(String userName, String password)
			throws CommonException {
		log.debug("Enter "+this.getClass().getName()+".getDomainEmployee() start!!");
		log.debug("Enter "+this.getClass().getName()+".getDomainEmployee param userName -->"+userName);
	//	log.debug("Enter "+this.getClass().getName()+".getDomainEmployee param password -->"+password);
		DomainUserInfoDTO domainDTO=null;
		//定義屬性集合
		Hashtable hashTable = new Hashtable(); 
		// 檢核域驗證服務器配置是否完整
		if(!StringUtils.hasText(ldapCtxFactory) || !StringUtils.hasText(ldapUrl) || !StringUtils.hasText(ldapSecurityPrincipal)
				|| !StringUtils.hasText(ldapSecurity) || !StringUtils.hasText(ldapLevel) || !StringUtils.hasText(domainName)){
			throw new CommonException();
		}
		try {
			//設置初始化上下文factory
			hashTable.put(Context.INITIAL_CONTEXT_FACTORY,ldapCtxFactory); 
			log.debug("Enter "+this.getClass().getName()+".getDomainEmployee param ldapCtxFactory -->"+ldapCtxFactory);
			//設置域的URL
			hashTable.put(Context.PROVIDER_URL, ldapUrl);
			log.debug("Enter "+this.getClass().getName()+".getDomainEmployee param ldapUrl -->"+ldapUrl);
			//設置安全證明
			hashTable.put(Context.SECURITY_AUTHENTICATION,ldapSecurity);
			log.debug("Enter "+this.getClass().getName()+".getDomainEmployee param ldapSecurity -->"+ldapSecurity);
			//設置代理帳號
			hashTable.put(Context.SECURITY_PRINCIPAL, userName+ldapSecurityPrincipal);  
			log.debug("Enter "+this.getClass().getName()+".getDomainEmployee param ldapSecurityPrincipal -->"+ldapSecurityPrincipal);
			//設置密碼
			hashTable.put(Context.SECURITY_CREDENTIALS, password);
			//得倒初始化LDAP上下文對象
			InitialLdapContext ctx = new InitialLdapContext(hashTable, null);
	        //設置過濾條件  objectClass表示用戶級別 sAMAccountName表示帳號 
	        //String searchFilter = "(&(objectClass="+ldapLevel+")(sAMAccountName="+userName+"))";
			AndFilter andFilter = new AndFilter();
			andFilter.and(new EqualsFilter("objectClass", ldapLevel));
			andFilter.and(new EqualsFilter("sAMAccountName", userName));
			String searchFilter = andFilter.toString();
	        //返回的屬性
	        String returnedAtts[] = {"objectClass","sAMAccountName","cn","givenName","sn","displayName",
	        		"description","userPrincipalName","mail","telephoneNumber","userAccountControl","sAMPwd"};
	        //得到查詢控制對象
	        SearchControls searchCtls = new SearchControls();
	        //設置范圍
	        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
	        //設置返回的屬性
	        searchCtls.setReturningAttributes(returnedAtts);
	        //得倒符合條件的域用戶數據 //網域名是 cybersoft4u.com.tw
	        NamingEnumeration results = ctx.search(domainName,searchFilter, searchCtls);
	        //定义属性集合
	        Attributes attrs = null;
	        //定義一個属性对象
	        Attribute attr = null;
	        //遍歷
	        while (results != null && results.hasMoreElements()) {
	        	//得倒返回的對象
	            SearchResult entry = (SearchResult)results.next();
	            //如果屬性存在 
	            if(StringUtils.hasText(entry.getAttributes().toString()) && !NO_ATTRIBUTES.equals(entry.getAttributes().toString())){
	            	//得倒屬性集
	            	attrs= entry.getAttributes();
	            	//如果屬性集合不為空
	            	if (attrs != null) {
	            		domainDTO=new DomainUserInfoDTO();
						//得倒objectClass屬性對象
						attr =attrs.get(DomainUserInfoDTO.ATTRIBUTE.OBJECT_CLASS.getValue());
						//設置objectClass屬性
						domainDTO.setObjectClass(attr==null?"":attr.get(attr.size()-1).toString());
						attr =attrs.get(DomainUserInfoDTO.ATTRIBUTE.S_A_M_ACCOUNT_NAME.getValue());
						domainDTO.setsAMAccountName(attr==null?"":attr.get(attr.size()-1).toString());
						attr =attrs.get(DomainUserInfoDTO.ATTRIBUTE.CN.getValue());
						domainDTO.setCn(attr==null?"":attr.get(attr.size()-1).toString());
						attr =attrs.get(DomainUserInfoDTO.ATTRIBUTE.GIVEN_NAME.getValue());
						domainDTO.setGivenName(attr==null?"":attr.get(attr.size()-1).toString());
						attr =attrs.get(DomainUserInfoDTO.ATTRIBUTE.SN.getValue());
						domainDTO.setSn(attr==null?"":attr.get(attr.size()-1).toString());
						attr =attrs.get(DomainUserInfoDTO.ATTRIBUTE.DISPLAY_NAME.getValue());
						domainDTO.setDisplayName(attr==null?"":attr.get(attr.size()-1).toString());
						attr =attrs.get(DomainUserInfoDTO.ATTRIBUTE.DESCRIPTION.getValue());
						domainDTO.setDescription(attr==null?"":attr.get(attr.size()-1).toString());
						attr =attrs.get(DomainUserInfoDTO.ATTRIBUTE.USER_PRINCIPAL_NAME.getValue());
						domainDTO.setUserPrincipalName(attr==null?"":attr.get(attr.size()-1).toString());
						attr =attrs.get(DomainUserInfoDTO.ATTRIBUTE.MAIL.getValue());
						domainDTO.setMail(attr==null?"":attr.get(attr.size()-1).toString());
						attr =attrs.get(DomainUserInfoDTO.ATTRIBUTE.TELEPHONE_NUMBER.getValue());
						domainDTO.setTelephoneNumber(attr==null?"":attr.get(attr.size()-1).toString());
						attr =attrs.get(DomainUserInfoDTO.ATTRIBUTE.USER_ACCOUNT_CONTROL.getValue());
						domainDTO.setUserAccountControl(attr==null?"":attr.get(attr.size()-1).toString());
						attr =attrs.get(DomainUserInfoDTO.ATTRIBUTE.S_A_M_PWD.getValue());
						domainDTO.setPassword(attr==null?password:attr.get(attr.size()-1).toString());
						break;
	            	}
	            }
	        }
		} catch (Exception e) {
			log.error(this.getClass().getName()+".getDomainEmployee LDAP is error!!  - " + e.toString());
			String errorMessage = e.getMessage();
			log.error(this.getClass().getName()+".getDomainEmployee LDAP is error!!  - " + errorMessage);
			if(StringUtils.hasText(errorMessage)){
				String errorCode[] = errorMessage.split(CoreConstants.DELIMITER_COLON);
				if (errorCode!=null && errorCode.length>0){
					String returnCode = errorCode[1];
	                if (returnCode.indexOf(ERROR_CODE_LOGON_ID_NOT_FOUND_OR_WRONG_PWD) >= 0){
	                	throw new CommonException(IAtomsMessageCode.LOGON_ID_NOT_FOUND_OR_WRONG_PWD);
	                }else if (returnCode.startsWith("#BLANK#")){
	                	throw new CommonException(WfMessageCode.LOGIN_ERROR_LDAP_AUTHENTICATION_FAILED, new String[]{"使用者無專案角色或無權限讀取"});
	                }else{
	                	throw new CommonException(WfMessageCode.LOGIN_ERROR_LDAP_AUTHENTICATION_FAILED, new String[]{returnCode+"登入失敗，請重新登入或洽連管理員"});
	                }
				}
			}
			throw new CommonException();
		} 
		return domainDTO;
	}
	/**
	 * Purpose:獲得域用戶信息
	 * @author evanliu
	 * @param userName：域用戶帳號
	 * @param password：域用戶密碼
	 * @param account：要獲得的域用戶帳號
	 * @throws CommonException：出錯後，丟出CommonException
	 * @return AdmUserDTO：域用戶信息
	 */
	public AdmUserDTO getAdmUserDTO(String userName, String password, String account)
			throws CommonException {
		this.log.debug("Enter "+this.getClass().getName()+".getAdmUserDTO() start!!");
		this.log.debug("Enter "+this.getClass().getName()+".getAdmUserDTO() param userName -->" + userName);
	//	this.log.debug("Enter "+this.getClass().getName()+".getAdmUserDTO() param password -->" + password);
		this.log.debug("Enter "+this.getClass().getName()+".getAdmUserDTO() param account -->" + account);
		AdmUserDTO admUserDTO = null;
		//定義屬性集合
		Hashtable hashTable = new Hashtable(); 
		try {
			//設置初始化上下文factory
			hashTable.put(Context.INITIAL_CONTEXT_FACTORY, ldapCtxFactory); 
			this.log.debug("Enter " + this.getClass().getName() + ".getAdmUserDTO() param ldapCtxFactory -->" + ldapCtxFactory);
			//設置域的URL
			hashTable.put(Context.PROVIDER_URL, ldapUrl);
			this.log.debug("Enter " + this.getClass().getName() + ".getAdmUserDTO() param ldapUrl -->" + ldapUrl);
			//設置安全證明
			hashTable.put(Context.SECURITY_AUTHENTICATION, ldapSecurity);
			this.log.debug("Enter " + this.getClass().getName() + ".getAdmUserDTO() param ldapSecurity -->" + ldapSecurity);
			//設置代理帳號
			hashTable.put(Context.SECURITY_PRINCIPAL, userName+ldapSecurityPrincipal);  
			this.log.debug("Enter " + this.getClass().getName() + ".getAdmUserDTO() param ldapSecurityPrincipal -->" + ldapSecurityPrincipal);
			//設置密碼
			hashTable.put(Context.SECURITY_CREDENTIALS, password);
			//得倒初始化LDAP上下文對象
			InitialLdapContext ctx = new InitialLdapContext(hashTable, null);
	        //設置過濾條件  objectClass表示用戶級別 sAMAccountName表示帳號 
	        //String searchFilter = "(&(objectClass=" + ldapLevel + ")(sAMAccountName=" + account + "))";
			AndFilter andFilter = new AndFilter();
			andFilter.and(new EqualsFilter("objectClass", ldapLevel));
			andFilter.and(new EqualsFilter("sAMAccountName", account));
			String searchFilter = andFilter.toString();
	        //返回的屬性
	        String returnedAtts[] = {"cn", "mail"};
	        //得到查詢控制對象
	        SearchControls searchCtls = new SearchControls();
	        //設置范圍
	        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
	        //設置返回的屬性
	        searchCtls.setReturningAttributes(returnedAtts);
	        //得倒符合條件的域用戶數據 //網域名是 cybersoft4u.com.tw
	        NamingEnumeration results = ctx.search(domainName, searchFilter, searchCtls);
	        //定义属性集合
	        Attributes attrs = null;
	        //定義一個属性对象
	        Attribute attr = null;
	        //遍歷
	        while (results != null && results.hasMoreElements()) {
	        	//得倒返回的對象
	            SearchResult entry = (SearchResult) results.next();
	            //如果屬性存在 
	            if(StringUtils.hasText(entry.getAttributes().toString()) && !NO_ATTRIBUTES.equals(entry.getAttributes().toString())){
	            	//得倒屬性集
	            	attrs= entry.getAttributes();
	            	//如果屬性集合不為空
	            	if (attrs != null) {
	            		admUserDTO = new AdmUserDTO();
						attr = attrs.get(DomainUserInfoDTO.ATTRIBUTE.CN.getValue());
						admUserDTO.setCname(attr == null ? "" : attr.get(attr.size() - 1).toString());
						attr = attrs.get(DomainUserInfoDTO.ATTRIBUTE.MAIL.getValue());
						admUserDTO.setEmail(attr == null ? "" : attr.get(attr.size() - 1).toString());
						break;
	            	}
	            }
	        }
		} catch (Exception e) {
			log.error(this.getClass().getName() + ".getAdmUserDTO LDAP is error!!  ----> " + e.toString());
			throw new CommonException(e);
		} 
		return admUserDTO;
	}
	
	/**
	 * Purpose:初始化域驗證配置(從system.config讀取)
	 * @author CrissZhang
	 * @throws CommonException：出錯後，丟出CommonException
	 * @return void
	 */
	public void init()throws CommonException {
		try {
			// 域服務工廠類
		//	this.setLdapCtxFactory(SystemConfigManager.getProperty(IAtomsConstants.PARAM_LDAP_AUTH, "ldapCtxFactory"));
			// 設置域服務器URL
			this.setLdapUrl(SystemConfigManager.getProperty(IAtomsConstants.PARAM_LDAP_AUTH, IAtomsConstants.PARAM_LDAP_URL));
			// 設置安全證明
			this.setLdapSecurity(SystemConfigManager.getProperty(IAtomsConstants.PARAM_LDAP_AUTH, IAtomsConstants.PARAM_LDAP_SECURITY));
			// 設置代理帳號
			this.setLdapSecurityPrincipal(SystemConfigManager.getProperty(IAtomsConstants.PARAM_LDAP_AUTH, IAtomsConstants.PARAM_LDAP_SECURITY_PRINCIPAL));
			// 設置用戶級別
			this.setLdapLevel(SystemConfigManager.getProperty(IAtomsConstants.PARAM_LDAP_AUTH, IAtomsConstants.PARAM_LDAP_LEVEL));
			// 設置網域名
			this.setDomainName(SystemConfigManager.getProperty(IAtomsConstants.PARAM_LDAP_AUTH, IAtomsConstants.PARAM_DOMAIN_NAME));
		} catch (Exception e) {
			log.error(this.getClass().getName() + ".init LDAP is error!!  ----> " + e.toString());
			throw new CommonException(e);
		} 
	}
	/**
	 * @return the ldapCtxFactory
	 */
	public String getLdapCtxFactory() {
		return ldapCtxFactory;
	}
	/**
	 * @param ldapCtxFactory the ldapCtxFactory to set
	 */
	public void setLdapCtxFactory(String ldapCtxFactory) {
		this.ldapCtxFactory = ldapCtxFactory;
	}
	/**
	 * @return the ldapUrl
	 */
	public String getLdapUrl() {
		return ldapUrl;
	}
	/**
	 * @param ldapUrl the ldapUrl to set
	 */
	public void setLdapUrl(String ldapUrl) {
		this.ldapUrl = ldapUrl;
	}
	/**
	 * @return the ldapSecurity
	 */
	public String getLdapSecurity() {
		return ldapSecurity;
	}
	/**
	 * @param ldapSecurity the ldapSecurity to set
	 */
	public void setLdapSecurity(String ldapSecurity) {
		this.ldapSecurity = ldapSecurity;
	}
	/**
	 * @return the ldapSecurityPrincipal
	 */
	public String getLdapSecurityPrincipal() {
		return ldapSecurityPrincipal;
	}
	/**
	 * @param ldapSecurityPrincipal the ldapSecurityPrincipal to set
	 */
	public void setLdapSecurityPrincipal(String ldapSecurityPrincipal) {
		this.ldapSecurityPrincipal = ldapSecurityPrincipal;
	}
	/**
	 * @return the ldapLevel
	 */
	public String getLdapLevel() {
		return ldapLevel;
	}
	/**
	 * @param ldapLevel the ldapLevel to set
	 */
	public void setLdapLevel(String ldapLevel) {
		this.ldapLevel = ldapLevel;
	}
	/**
	 * @return the domainName
	 */
	public String getDomainName() {
		return domainName;
	}
	/**
	 * @param domainName the domainName to set
	 */
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

}
