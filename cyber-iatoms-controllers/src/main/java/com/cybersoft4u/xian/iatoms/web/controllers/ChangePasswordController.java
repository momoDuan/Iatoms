package com.cybersoft4u.xian.iatoms.web.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import cafe.core.bean.Message;
import cafe.core.context.SessionContext;
import cafe.core.exception.CommonException;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.PasswordSettingDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.PasswordFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;

/**
 * Purpose:密碼修改Controller 
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2016年7月6日
 * @MaintenancePersonnel HermanWang
 */
public class ChangePasswordController extends AbstractMultiActionController<PasswordFormDTO> {

	/**
	 * 日志记录物件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(ChangePasswordController.class);
	/**
	 * Constructor:无参构造函数
	 */
	public ChangePasswordController() {
		this.setCommandClass(PasswordFormDTO.class);
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(PasswordFormDTO command) throws CommonException {
		if (command == null) {
			return false;
		}
		String actionId = command.getActionId();
		Message msg = null;
		//舊密碼
		String oldPassword = command.getOldPassword();
		//新密碼
		String newPassword = command.getNewPassword();
		//確認密碼
		String rePassword = command.getRePassword();
		//登入者的帳號
		IAtomsLogonUser logonUser = (IAtomsLogonUser) command.getLogonUser();
		String userId = logonUser.getId();
		if (IAtomsConstants.ACTION_SAVE.equals(actionId)) {
			try {
				//若未輸入舊密碼
				if (!StringUtils.hasText(oldPassword)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_USER_PWD)});
					throw new CommonException(msg);
				} else {
					if (!ValidateUtils.length(oldPassword, 0, 20)) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_USER_PWD), 
								IAtomsConstants.MAXLENGTH_NUMBER_TWENTY});
						throw new CommonException(msg);
					}
				}
				if (!StringUtils.hasText(newPassword)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_USER_PWD)});
					throw new CommonException(msg);
				} else {
					// 密碼長度限｛0｝~｛1｝之間，請重新輸入
					SessionContext pwdSettingCtx = this.getServiceLocator().doService(logonUser, this.getServiceId(), IAtomsConstants.ACTION_INIT, command);
					PasswordFormDTO passwordFormDTO = (PasswordFormDTO) pwdSettingCtx.getResponseResult();
					PasswordSettingDTO passwordSettingDTO = passwordFormDTO.getAdmSecurityDefDTO();
					if(passwordSettingDTO != null){
						if (!ValidateUtils.length(newPassword, passwordSettingDTO.getPwdLenBg().intValue(), passwordSettingDTO.getPwdLenNd().intValue())) {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.NEW_PWD_LENGTH_OVER_LIMIT, 
									new String[]{passwordSettingDTO.getPwdLenNd().toString(), passwordSettingDTO.getPwdLenBg().toString()});
							throw new CommonException(msg);
						}
					}
					// 新密碼限英數字或英文符號或英數符號，請重新輸入
					if (!ValidateUtils.inputCharacter(newPassword, Boolean.TRUE)) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.NEW_PWD_CHARACTER_FORMAT_ERROR);
						throw new CommonException(msg);
					}
					// 新密碼不可為鍵盤上的字母順序(asdf)，請重新輸入
					if (!ValidateUtils.inputCharacterOrder(newPassword)) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.NEW_PWD_CHARACTER_ORDER_ERROR);
						throw new CommonException(msg);
					}
					// 新密碼不可為連續的數字，請重新輸入
					if (!ValidateUtils.inputNumberOrder(newPassword)) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.NEW_PWD_NUMBER_FORMAT_ERROR);
						throw new CommonException(msg);
					}
					// 新密碼不可使用重複的字元(不可重複三次)，請重新輸入
					if (!ValidateUtils.inputCharRepeat(newPassword)) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.NEW_PWD_CHARACTER_REPEAT_ERROR);
						throw new CommonException(msg);
					}
					// 新密碼不能與帳號相同或部分相同，請重新輸入
					if (!ValidateUtils.inputSameAsStr(userId, newPassword)) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.NEW_PWD_SAME_ACCOUNT_ERROR);
						throw new CommonException(msg);
					}
					if (!ValidateUtils.length(newPassword, 0, 20)) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_USER_PWD), 
								IAtomsConstants.MAXLENGTH_NUMBER_TWENTY});
						throw new CommonException(msg);
					}
				}
				if (!StringUtils.hasText(rePassword)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_USER_REPWD)});
					throw new CommonException(msg);
				} else {
					if(!rePassword.equals(newPassword)){
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.REPWD_EQUAL_TO_NEW_PWD);
						throw new CommonException(msg);
					}
					if (!ValidateUtils.length(rePassword, 0, 20)) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_USER_REPWD), 
								IAtomsConstants.MAXLENGTH_NUMBER_TWENTY});
						throw new CommonException(msg);
					}
				}
			} catch (Exception e) {
				if(e instanceof CommonException){
					throw new CommonException(((CommonException) e).getErrorMessage());
				} else {
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public PasswordFormDTO parse(HttpServletRequest request,
			PasswordFormDTO command) throws CommonException {
		try {
			if(command == null) {
				command = new PasswordFormDTO();
			}
			// 获取actionId
			String actionId = this.getString(request, IAtomsConstants.FIELD_ACTION_ID);
			if (!StringUtils.hasText(actionId)) {
				actionId = IAtomsConstants.ACTION_INIT;
			}
			command.setActionId(actionId);
			return command;
		} catch (Exception e) {
			LOGGER.error(".parse() Exception.", e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
	}

}
