package com.cybersoft4u.xian.iatoms.services.dbcp;

import org.apache.commons.dbcp.BasicDataSource;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.utils.PasswordEncoderUtilities;

import cafe.core.config.SystemConfigManager;

/**
 * Purpose: BasicDataSource 加密字串
 * @author evanliu
 * @since  JDK 1.6
 * @date   2017年8月2日
 * @MaintenancePersonnel evanliu
 */
public class EncryptedBasicDataSource extends BasicDataSource {
	/**
	 * Constructor:無參建構子
	 */
	public EncryptedBasicDataSource() {
        super();
    }
	
	/**
	 * DB類別(IATOMS/FOMS)
	 */
	private String databaseType;
	
	/**
	 * (non-Javadoc)
	 * @see org.apache.commons.dbcp.BasicDataSource#setPassword(java.lang.String)
	 *//*
	@Override
    public void setPassword(String password){
       this.password = PasswordEncoderUtilities.decodePassword(password);
	}
	*//**
	 * (non-Javadoc)
	 * @see org.apache.commons.dbcp.BasicDataSource#setUsername(java.lang.String)
	 *//*
	@Override
	public void setUsername(String username) {
		this.username = PasswordEncoderUtilities.decodePassword(username);
	}*/
	
	/**
	 * Purpose:初始化DB連接
	 * @author CrissZhang
	 * @return void
	 */
	public void initDataSource(){
		// database URL
		this.setUrl(SystemConfigManager.getProperty(this.databaseType, IAtomsConstants.PARAM_DATABASE_URL));
		// database 用戶名
		this.setUsername(PasswordEncoderUtilities.decodePassword(SystemConfigManager.getProperty(this.databaseType, IAtomsConstants.PARAM_DATABASE_USERNAME)));
		// database 密碼
		this.setPassword(PasswordEncoderUtilities.decodePassword(SystemConfigManager.getProperty(this.databaseType, IAtomsConstants.PARAM_DATABASE_PWD)));
	}
	/**
	 * @return the databaseType
	 */
	public String getDatabaseType() {
		return databaseType;
	}
	/**
	 * @param databaseType the databaseType to set
	 */
	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}
	
}
