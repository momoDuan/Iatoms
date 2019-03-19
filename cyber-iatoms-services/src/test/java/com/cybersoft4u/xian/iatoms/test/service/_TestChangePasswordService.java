package com.cybersoft4u.xian.iatoms.test.service;

import cafe.core.context.SessionContext;
import cafe.core.service.ServiceException;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.bean.formDTO.PasswordFormDTO;
import com.cybersoft4u.xian.iatoms.services.IChangePasswordService;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmPwdHistoryDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;
/**
 * Purpose: 使用設密碼修改的單元測試
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2017/5/9
 * @MaintenancePersonnel HermanWang
 */
public class _TestChangePasswordService extends AbstractTestCase {
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestChangePasswordService.class);
	/**
	 * 注入changePasswordService
	 */
	public IChangePasswordService changePasswordService;
	/**
	 * 使用者DAO
	 */
	private IAdmUserDAO admUserDAO;
	/**
	 * 使用者密碼歷史記錄
	 */
	private IAdmPwdHistoryDAO admPwdHistoryDAO;

	/**
	 * 無參構造
	 */
	@SuppressWarnings("deprecation")
	public _TestChangePasswordService() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase#testInit()
	 */
	public void testInit(){
		try {
			//ctx 不為空
			SessionContext  ctx =  new SessionContext();
			changePasswordService.init(ctx);
			
			PasswordFormDTO passwordFormDTO = new PasswordFormDTO();
			ctx.setRequestParameter(passwordFormDTO);
			changePasswordService.init(ctx);
		} catch (ServiceException e) {
			LOGGER.error(this.getClass().getName()+".testInit()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:測試密碼修改的存儲
	 * @author HermanWang
	 * @return void
	 */
	/*public void testSavePassword() {
		try {
			//Message msg = null;
			SessionContext ctx =  new SessionContext();
			PasswordFormDTO formDTO = new PasswordFormDTO();
			IAtomsLogonUser logonUser = new IAtomsLogonUser();
			logonUser.setId("1473311383499-0060");
			logonUser.setName("王佳強");
			
			//新密碼不可與前幾次重複
			formDTO.setPwdRpCnt(Integer.valueOf(5));
			//舊密碼
			formDTO.setOldPassword("3ed4rf");
			//新密碼
			formDTO.setNewPassword("23232!!");
			formDTO.setLogonUser(logonUser);
			ctx.setRequestParameter(formDTO);
			ctx = changePasswordService.save(ctx);
			AdmUser admUser = (AdmUser) admUserDAO.findByPrimaryKey(AdmUser.class, logonUser.getId());
			//如果userid錯誤
			if(admUser == null) {
				Assert.assertTrue(IAtomsMessageCode.USERID_NOT_INPUT.equals(ctx.getReturnMessage().getCode()));
			} else {
				String currentPassword = PasswordEncoderUtilities.decodePassword(admUser.getPassword());
				//測試舊密碼是否正確
				if(!currentPassword.equals(formDTO.getOldPassword())) {
					Assert.assertTrue(IAtomsMessageCode.OLD_PASSWORD_ERROR.equals(ctx.getReturnMessage().getCode()));
				} else {
					//測試舊密碼與新密碼相同
					if(currentPassword.equals(formDTO.getNewPassword())) {
						Assert.assertTrue(IAtomsMessageCode.OLDPASSWORD_NOT_EQUAL_NEWPASSWOD.equals(ctx.getReturnMessage().getCode()));
					} else {
						if(IAtomsMessageCode.PASSWORD_SAVE_SUCCESS.equals(ctx.getReturnMessage().getCode())) {
							Assert.assertTrue(IAtomsMessageCode.PASSWORD_SAVE_SUCCESS.equals(ctx.getReturnMessage().getCode()));
						} else {
							//驗證確認密碼與前N此相同
							List<AdmPwdHistoryDTO> admPwdHistoryDTOs = this.admPwdHistoryDAO.listByUserId(logonUser.getId(), formDTO.getPwdRpCnt());
							boolean result = false;
							if (!CollectionUtils.isEmpty(admPwdHistoryDTOs)) {
								for (AdmPwdHistoryDTO admPwdHistoryDTO : admPwdHistoryDTOs) {
									currentPassword = PasswordEncoderUtilities.decodePassword(admPwdHistoryDTO.getPassword());
									if (formDTO.getNewPassword().equals(currentPassword)) {
										result = true;
										break;
									}
								}
							} 
							if (formDTO.getNewPassword().equals(formDTO.getOldPassword())) {
								result = true;
							}
							//如果新密碼和前N此密碼重複
							if(result) {
								Assert.assertTrue(IAtomsMessageCode.NEW_PASSWORD_REPEAT.equals(ctx.getReturnMessage().getCode()));
							}
						}
					}
				}
			}
			//新密碼不可與前幾次重複
			formDTO.setPwdRpCnt(Integer.valueOf(5));
			//舊密碼
			formDTO.setOldPassword("3ed4rf");
			//新密碼
			formDTO.setNewPassword("3ed4rf");
			formDTO.setLogonUser(logonUser);
			ctx.setRequestParameter(formDTO);
			ctx = changePasswordService.save(ctx);
			//測試舊密碼與新密碼相同
			if(IAtomsMessageCode.OLDPASSWORD_NOT_EQUAL_NEWPASSWOD.equals(ctx.getReturnMessage().getCode())) {
				Assert.assertTrue(IAtomsMessageCode.OLDPASSWORD_NOT_EQUAL_NEWPASSWOD.equals(ctx.getReturnMessage().getCode()));
			} else {
				Assert.assertTrue(true);
			}
			
			//新密碼不可與前幾次重複
			formDTO.setPwdRpCnt(Integer.valueOf(5));
			//舊密碼
			formDTO.setOldPassword("3ed4rf");
			//新密碼
			formDTO.setNewPassword("3ed4rf!");
			formDTO.setLogonUser(logonUser);
			ctx.setRequestParameter(formDTO);
			ctx = changePasswordService.save(ctx);
			//測試舊密碼與前幾次相同
			if(IAtomsMessageCode.NEW_PASSWORD_REPEAT.equals(ctx.getReturnMessage().getCode())) {
				Assert.assertTrue(true);
			} else {
				Assert.assertTrue(true);
			}
			
			//成功修改
			//新密碼不可與前幾次重複
			formDTO.setPwdRpCnt(Integer.valueOf(3));
			//舊密碼
			formDTO.setOldPassword("3ed4rf");
			//新密碼
			formDTO.setNewPassword("3ed4rf3234");
			formDTO.setLogonUser(logonUser);
			ctx.setRequestParameter(formDTO);
			ctx = changePasswordService.save(ctx);
			if(IAtomsMessageCode.PASSWORD_SAVE_SUCCESS.equals(ctx.getReturnMessage().getCode())) {
				Assert.assertTrue(true);
			} else {
				Assert.assertTrue(true);
			}
			
		} catch (ServiceException e) {
			LOGGER.error(this.getClass().getName()+".testSavePassword()", e);
			e.printStackTrace();
		}
	}*/
	/**
	 * @return the changePasswordService
	 */
	public IChangePasswordService getChangePasswordService() {
		return changePasswordService;
	}
	/**
	 * @param changePasswordService the changePasswordService to set
	 */
	public void setChangePasswordService(
			IChangePasswordService changePasswordService) {
		this.changePasswordService = changePasswordService;
	}
	/**
	 * @return the admUserDAO
	 */
	public IAdmUserDAO getAdmUserDAO() {
		return admUserDAO;
	}
	/**
	 * @param admUserDAO the admUserDAO to set
	 */
	public void setAdmUserDAO(IAdmUserDAO admUserDAO) {
		this.admUserDAO = admUserDAO;
	}
	/**
	 * @return the admPwdHistoryDAO
	 */
	public IAdmPwdHistoryDAO getAdmPwdHistoryDAO() {
		return admPwdHistoryDAO;
	}
	/**
	 * @param admPwdHistoryDAO the admPwdHistoryDAO to set
	 */
	public void setAdmPwdHistoryDAO(IAdmPwdHistoryDAO admPwdHistoryDAO) {
		this.admPwdHistoryDAO = admPwdHistoryDAO;
	}
	
}
