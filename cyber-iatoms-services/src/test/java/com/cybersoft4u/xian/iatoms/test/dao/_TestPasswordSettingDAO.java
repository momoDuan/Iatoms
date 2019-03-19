package com.cybersoft4u.xian.iatoms.test.dao;

import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.bean.dto.PasswordSettingDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IPasswordSettingDAO;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;

/**
 * Purpose: PasswordSettingDAO類單元測試
 * @author jasonzhou
 * @since  JDK 1.6
 * @date   2016-6-27
 * @MaintenancePersonnel jasonzhou
 */
public class _TestPasswordSettingDAO extends AbstractTestCase{
	
	/**
	 * 密碼原則設定DAO
	 */
	private IPasswordSettingDAO passwordSettingDAO;
	
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestPasswordSettingDAO.class);
	
	@SuppressWarnings("deprecation")
	public _TestPasswordSettingDAO(){
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	
	/**
	 * Purpose:查詢方法測試
	 * @author jasonzhou
	 * @return void
	 */
	public void testGetPasswordSettingInfo(){
		try {
			PasswordSettingDTO admSecurityDefDTO = this.passwordSettingDAO.getPasswordSettingInfo();
			assertNotNull(admSecurityDefDTO);
			assertNotNull(admSecurityDefDTO.getId());
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+".testGetPasswordSettingInfo()", e);
		}
	}
	
	/**
	 * @return the passwordSettingDAO
	 */
	public IPasswordSettingDAO getPasswordSettingDAO() {
		return passwordSettingDAO;
	}

	/**
	 * @param passwordSettingDAO the passwordSettingDAO to set
	 */
	public void setPasswordSettingDAO(IPasswordSettingDAO passwordSettingDAO) {
		this.passwordSettingDAO = passwordSettingDAO;
	}
}
