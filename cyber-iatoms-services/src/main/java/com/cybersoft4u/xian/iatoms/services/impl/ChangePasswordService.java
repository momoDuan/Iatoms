package com.cybersoft4u.xian.iatoms.services.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cafe.core.bean.Message;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.SessionContext;
import cafe.core.dao.DataAccessException;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmPwdHistoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.PasswordSettingDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.PasswordFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.PasswordEncoderUtilities;
import com.cybersoft4u.xian.iatoms.services.IChangePasswordService;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmPwdHistoryDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IPasswordSettingDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmPwdHistory;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmUser;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCompany;

/**
 * Purpose: 密碼修改Service 
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2016年7月6日
 * @MaintenancePersonnel HermanWang
 */
public class ChangePasswordService extends AtomicService implements IChangePasswordService {

	/**
	 * 系统日志文件控件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, ChangePasswordService.class);
	/**
	 * 使用者DAO
	 */
	private IAdmUserDAO admUserDAO;
	/**
	 * 使用者密碼歷史記錄
	 */
	private IAdmPwdHistoryDAO admPwdHistoryDAO;
	/**
	 * 密碼設定
	 */
	private IPasswordSettingDAO passwordSettingDAO;
	/**
	 * 公司
	 */
	private ICompanyDAO companyDAO;
	/**
	 * Constructor:构造函数
	 */
	public ChangePasswordService() {
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IChangePasswordService#init(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext init(SessionContext sessionContext)
			throws ServiceException {
		try {
			PasswordFormDTO passwordFormDTO = (PasswordFormDTO) sessionContext.getRequestParameter();
			if (passwordFormDTO == null) {
				passwordFormDTO = new PasswordFormDTO();
			}
			PasswordSettingDTO admSecurityDefDTO = this.passwordSettingDAO.getPasswordSettingInfo();
			passwordFormDTO.setAdmSecurityDefDTO(admSecurityDefDTO);
			Message msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(passwordFormDTO);
			return sessionContext;
		} catch (Exception e) {
			LOGGER.error(".init(SessionContext sessionContext):", e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IChangePasswordService#save(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext save(SessionContext sessionContext)
			throws ServiceException {
		Message msg = null;
		try {			
			PasswordFormDTO passwordFormDTO = (PasswordFormDTO) sessionContext.getRequestParameter();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) passwordFormDTO.getLogonUser();
			Integer times = passwordFormDTO.getPwdRpCnt();
			String oldPassword = passwordFormDTO.getOldPassword();
			String newPassword = passwordFormDTO.getNewPassword();
			String userId = logonUser.getId();
			//查找
			AdmUser admUser = (AdmUser) admUserDAO.findByPrimaryKey(AdmUser.class, userId);
			if (admUser != null) {
				//驗證舊密碼是否正確
				String currentPassword = PasswordEncoderUtilities.decodePassword(admUser.getPassword());
				if (currentPassword.equals(oldPassword)) {
					//密碼相等
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.CHECK_INPUT_IS_TRUE);
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.OLD_PWD_ERROR);
					sessionContext.setReturnMessage(msg);
					return sessionContext;
				}
				if(currentPassword.equals(newPassword)){
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.OLDPWD_NOT_EQUAL_NEWPWD);
					sessionContext.setReturnMessage(msg);
					return sessionContext;
				}
				//驗證確認密碼與前N此相同
				List<AdmPwdHistoryDTO> admPwdHistoryDTOs = this.admPwdHistoryDAO.listByUserId(userId, times);
				boolean result = false;
				if (!CollectionUtils.isEmpty(admPwdHistoryDTOs)) {
					for (AdmPwdHistoryDTO admPwdHistoryDTO : admPwdHistoryDTOs) {
						currentPassword = PasswordEncoderUtilities.decodePassword(admPwdHistoryDTO.getPassword());
						if (newPassword.equals(currentPassword)) {
							result = true;
							break;
						}
					}
				} 
				if (newPassword.equals(oldPassword)) {
					result = true;
				}
				if (!result) {
					//密碼Ok
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.CHECK_INPUT_IS_TRUE);
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.NEW_PWD_REPEAT, new String[]{String.valueOf(passwordFormDTO.getPwdRpCnt())});
					sessionContext.setReturnMessage(msg);
					return sessionContext;
				}
				//加密	
				newPassword = PasswordEncoderUtilities.encodePassword(newPassword);
				Timestamp currentDate = DateTimeUtils.getCurrentTimestamp();
				//修改密碼
				admUser.setPassword(newPassword);
				admUser.setChangePwdDate(currentDate);
				if(StringUtils.hasText(admUser.getStatus()) && (admUser.getStatus().equals(IAtomsConstants.ACCOUNT_STATUS_NEW))){
					admUser.setStatus(IAtomsConstants.ACCOUNT_STATUS_NORMAL);
				}
				admUser.setResetPwd(IAtomsConstants.NO);
				admUser.setRetry(Integer.valueOf(0));
				admUser.setUpdatedById(userId);
			//	admUser.setUpdatedByName(logonUser.getName());
				admUser.setUpdatedByName(admUser.getCname());
				admUser.setUpdatedDate(currentDate);
				this.admUserDAO.save(admUser);					
				//新增密碼修改記錄
				AdmPwdHistory admPwdHistory = new AdmPwdHistory();
				admPwdHistory.setCreateDate(DateTimeUtils.getCurrentTimestamp());
				admPwdHistory.setPassword(newPassword);
				admPwdHistory.setUserId(userId);
				this.admPwdHistoryDAO.insert(admPwdHistory);
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.PWD_SAVE_SUCCESS);
			} else {
				LOGGER.error("ChangePasswordService --> save() error--> ", "dto is null");
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.USERID_NOT_INPUT);
			}
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOGGER.error(".save() DataAccess Exception:", e);
			throw new ServiceException(msg);
		}  catch (Exception e) {
			LOGGER.error(".save(SessionContext sessionContext):", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
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

	/**
	 * @return the companyDAO
	 */
	public ICompanyDAO getCompanyDAO() {
		return companyDAO;
	}

	/**
	 * @param companyDAO the companyDAO to set
	 */
	public void setCompanyDAO(ICompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}
	
}
