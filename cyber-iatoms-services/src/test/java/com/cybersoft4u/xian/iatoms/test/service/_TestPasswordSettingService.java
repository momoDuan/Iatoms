package com.cybersoft4u.xian.iatoms.test.service;

import cafe.core.context.SessionContext;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.bean.dto.PasswordSettingDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.PasswordSettingFormDTO;
import com.cybersoft4u.xian.iatoms.services.IPasswordSettingService;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;

/**
 * Purpose: PasswordSettingService類單元測試
 * @author jasonzhou
 * @since  JDK 1.6
 * @date   2016-6-24
 * @MaintenancePersonnel jasonzhou
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class _TestPasswordSettingService extends AbstractTestCase {
	
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog logger = (CafeLog)CafeLogFactory.getLog(_TestPasswordSettingService.class);
	
	/**
	 * 密碼原則設定service
	 */
	public IPasswordSettingService  passwordSettingService;
	
	@SuppressWarnings("deprecation")
	public _TestPasswordSettingService(){
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase#testInit()
	 */
	public void testInit() {
		try {
			SessionContext ctx = new SessionContext();
			PasswordSettingFormDTO passwordSettingFormDTO = new PasswordSettingFormDTO();
			ctx.setRequestParameter(passwordSettingFormDTO);
			passwordSettingFormDTO = (PasswordSettingFormDTO) passwordSettingService.init(ctx).getResponseResult();
			assertNotNull(passwordSettingFormDTO);
			assertNotNull(passwordSettingFormDTO.getPasswordSettingDTO());
			assertNotNull(passwordSettingFormDTO.getPasswordSettingDTO().getId());
		} catch (Exception e) {
			logger.error(this.getClass().getName()+".testInit()",e);
		}
	}
	
	/**
	 * Purpose:保存方法測試
	 * @author jasonzhou
	 * @return void
	 */
	public void testSaveSetting() {
		try {
			SessionContext ctx = new SessionContext();
			PasswordSettingFormDTO passwordSettingFormDTO = new PasswordSettingFormDTO();
			PasswordSettingDTO admSecurityDefDTO = new PasswordSettingDTO();
			admSecurityDefDTO.setId("1");
			admSecurityDefDTO.setPwdLenBg(2);
			admSecurityDefDTO.setPwdLenNd(5);
			admSecurityDefDTO.setPwdRpCnt(2);
			admSecurityDefDTO.setPwdErrCnt(3);
			admSecurityDefDTO.setPwdChgFlag("Y");
			admSecurityDefDTO.setIdValidDay(40);
			admSecurityDefDTO.setPwdAlertDay(30);
			admSecurityDefDTO.setPwdValidDay(40);
			admSecurityDefDTO.setUpdatedById("11");
			admSecurityDefDTO.setUpdatedByName("張三");
			admSecurityDefDTO.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
			passwordSettingFormDTO.setPasswordSettingDTO(admSecurityDefDTO);
			ctx.setRequestParameter(passwordSettingFormDTO);
			passwordSettingFormDTO = (PasswordSettingFormDTO) passwordSettingService.saveSetting(ctx).getResponseResult();
			assertNotNull(passwordSettingFormDTO);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+".testSaveSetting()",e);
		}
	}
	
	/**
	 * @return the passwordSettingService
	 */
	public IPasswordSettingService getPasswordSettingService() {
		return passwordSettingService;
	}

	/**
	 * @param passwordSettingService the passwordSettingService to set
	 */
	public void setPasswordSettingService(
			IPasswordSettingService passwordSettingService) {
		this.passwordSettingService = passwordSettingService;
	}
}
