package com.cybersoft4u.xian.iatoms.jdbc.datasource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.cybersoft4u.xian.iatoms.common.utils.PasswordEncoderUtilities;
/**
 * Purpose:解密DataSource 
 * @author evanliu
 * @since  JDK 1.6
 * @date   2017年8月1日
 * @MaintenancePersonnel evanliu
 */
public class EncryptedDriverManagerDataSource extends DriverManagerDataSource {
	/**
	 * Constructor:無參建構子
	 */
	public EncryptedDriverManagerDataSource() {
		
	}
	/**
	 * Constructor:建構子
	 * @param url the JDBC URL to use for accessing the DriverManager
	 * @param username the JDBC username to use for accessing the DriverManager
	 * @param password the JDBC password to use for accessing the DriverManager
	 * @see java.sql.DriverManager#getConnection(String, String, String)
	 */
	public EncryptedDriverManagerDataSource(String url, String username, String password) {
		setUrl(url);
		setUsername(username);		
		setPassword(password);
	}
	/**
	 * Constructor:建構子
	 * @param driverClassName the JDBC driver class name
	 * @param url the JDBC URL to use for accessing the DriverManager
	 * @param username the JDBC username to use for accessing the DriverManager
	 * @param password the JDBC password to use for accessing the DriverManager
	 * @see java.sql.DriverManager#getConnection(String, String, String)
	 */
	public EncryptedDriverManagerDataSource(String driverClassName, String url, String username, String password) {
		setDriverClassName(driverClassName);
		setUrl(url);
		setUsername(username);
		setPassword(password);
	}
	/**
	 * (non-Javadoc)
	 * @see org.springframework.jdbc.datasource.AbstractDriverBasedDataSource#setUsername(java.lang.String)
	 */
	@Override
	public void setUsername(String username) {
		username = PasswordEncoderUtilities.decodePassword(username);
		super.setUsername(username);
	}
	/**
	 * (non-Javadoc)
	 * @see org.springframework.jdbc.datasource.AbstractDriverBasedDataSource#setPassword(java.lang.String)
	 */
	public void setPassword(String password) {
		password = PasswordEncoderUtilities.decodePassword(password);
		super.setPassword(password);
	}
}
