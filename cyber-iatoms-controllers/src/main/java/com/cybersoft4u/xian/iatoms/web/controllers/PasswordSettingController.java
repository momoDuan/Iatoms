package com.cybersoft4u.xian.iatoms.web.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import cafe.core.bean.Message;
import cafe.core.config.GenericConfigManager;
import cafe.core.exception.CommonException;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.BindPageDataUtils;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.PasswordSettingDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.PasswordSettingFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;

/**
 * Purpose: 密碼原則設定 Controller
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2016/5/20
 * @MaintenancePersonnel HermanWang
 */
public class PasswordSettingController extends AbstractMultiActionController<PasswordSettingFormDTO>{

	/**
	 * 日誌記錄物件
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.CONTROLLER, PasswordSettingController.class);
	
	/**
	 * Constructor:无參構造函數
	 */
	public PasswordSettingController() {
		this.setCommandClass(PasswordSettingFormDTO.class);
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(PasswordSettingFormDTO command) throws CommonException {
		if (command == null) {
			return false;
		}
		String actionId = command.getActionId();
		Message msg = null;
		if (IAtomsConstants.ACTION_SAVE_SETTING.equals(actionId)) {
			//檢驗頁面輸入
			PasswordSettingDTO passwordSettingDTO = command.getPasswordSettingDTO();
			if (passwordSettingDTO == null) {
				return false;
			}
			//密碼長度起
			Integer pwdLenBg = passwordSettingDTO.getPwdLenBg();
			//密碼長度迄
			Integer pwdLenNd = passwordSettingDTO.getPwdLenNd();
			//容錯次數
			Integer pwdErrCnt = passwordSettingDTO.getPwdErrCnt();
			//密碼不可與前N此相同
			Integer pwdRpCnt = passwordSettingDTO.getPwdRpCnt();
			//密碼有效周期
			String pwdValidDay = passwordSettingDTO.getPwdValidDay().toString();
			//密碼停權期
			String idValidDay = passwordSettingDTO.getIdValidDay().toString();
			//密碼到期提示
			String pwdAlertDay = passwordSettingDTO.getPwdAlertDay().toString();
			//未輸入密碼長度起
			if (pwdLenBg == null) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_SECURITY_DEF_PWD_LEN_BG)});
				throw new CommonException(msg);
			}
			//未輸入密碼長度迄
			if (pwdLenNd == null) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_SECURITY_DEF_PWD_LEN_ND)});
				throw new CommonException(msg);
			}
			//容錯次數
			if (pwdErrCnt == null) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_SECURITY_DEF_PWD_ERR_CNT)});
				throw new CommonException(msg);
			}
			//密碼不可與前N此相同
			if (pwdRpCnt == null) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_SECURITY_DEF_PWD_RP_CNT)});
				throw new CommonException(msg);
			}
			//密碼有效周期
			if (!StringUtils.hasText(pwdValidDay)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_SECURITY_DEF_PWD_VALID_DAY)});
				throw new CommonException(msg);
			} else {
				//驗證長度
				if (!ValidateUtils.varcharLength(pwdValidDay, 0, 3)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_SECURITY_DEF_PWD_VALID_DAY), IAtomsConstants.MAXLENGTH_NUMBER_THREE });
					throw new CommonException(msg);
				}
				//驗證正整數
				if(!ValidateUtils.number(pwdValidDay)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_POSITIVE_NUMBER, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_SECURITY_DEF_PWD_VALID_DAY)});
					throw new CommonException(msg);
				}
			}
			//未輸入密碼停權期
			if (!StringUtils.hasText(idValidDay)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_SECURITY_DEF_ID_VALID_DAY)});
				throw new CommonException(msg);
			} else {
				if (!ValidateUtils.varcharLength(idValidDay, 0, 3)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_SECURITY_DEF_ID_VALID_DAY), IAtomsConstants.MAXLENGTH_NUMBER_THREE});
					throw new CommonException(msg);
				}
				//驗證正整數
				if(!ValidateUtils.number(idValidDay)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_POSITIVE_NUMBER, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_SECURITY_DEF_ID_VALID_DAY)});
					throw new CommonException(msg);
				}
			}
			//未輸入密碼到期提示
			if (!StringUtils.hasText(pwdAlertDay)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_SECURITY_DEF_PWD_ALERT_DAY)});
				throw new CommonException(msg);
			} else {
				if (!ValidateUtils.varcharLength(pwdAlertDay, 0, 3)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_SECURITY_DEF_PWD_ALERT_DAY), IAtomsConstants.MAXLENGTH_NUMBER_THREE});
					throw new CommonException(msg);
				}
				//驗證正整數
				if(!ValidateUtils.number(pwdAlertDay)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_POSITIVE_NUMBER, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_SECURITY_DEF_PWD_ALERT_DAY)});
					throw new CommonException(msg);
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
	public PasswordSettingFormDTO parse(HttpServletRequest request,
			PasswordSettingFormDTO command) throws CommonException {
		try {
			if(command == null) {
				command = new PasswordSettingFormDTO();
			}
			// 获取actionId
			String actionId = this.getString(request, IAtomsConstants.FIELD_ACTION_ID);
			if (!StringUtils.hasText(actionId)) {
				actionId = IAtomsConstants.ACTION_INIT;
			}
			command.setActionId(actionId);
			if (IAtomsConstants.ACTION_SAVE_SETTING.equals(actionId)){
				PasswordSettingDTO pwdSettingDTO = command.getPasswordSettingDTO();
				pwdSettingDTO = BindPageDataUtils.bindValueObject(request, PasswordSettingDTO.class);
				command.setPasswordSettingDTO(pwdSettingDTO);
			}
			return command;
		} catch (Exception e) {
			LOGGER.error("Exception----parse()", "Controller Exception", e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
	}
}
