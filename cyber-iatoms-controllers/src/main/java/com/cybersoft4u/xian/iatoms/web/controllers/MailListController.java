package com.cybersoft4u.xian.iatoms.web.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import cafe.core.bean.Message;
import cafe.core.exception.CommonException;
import cafe.core.util.i18NUtil;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.BindPageDataUtils;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.MailListDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.MailListManageFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;

/**
 * 
 * Purpose: 電子郵件群組維護Controller
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2016/6/30
 * @MaintenancePersonnel CarrieDuan
 */
public class MailListController extends AbstractMultiActionController<MailListManageFormDTO> {
	/**
	 * 日志记录物件
	 */
	private static final Log LOGGER = LogFactory.getLog(MailListController.class);
	
	/**
	 * Constructor:无参构造函数
	 */
	public MailListController() {
		this.setCommandClass(MailListManageFormDTO.class);
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(MailListManageFormDTO command) throws CommonException {
		if (command == null) {
			return false;
		}
		String actionId = command.getActionId();
		Message msg = null;
		if (IAtomsConstants.ACTION_SAVE.equals(actionId)) {
			MailListDTO mailListDTO = command.getMailListDTO();
			//獲取郵件類別
			String mailGroup = mailListDTO.getMailGroup();
			//獲取姓名
			String name = mailListDTO.getName();
			//獲取mail
			String main = mailListDTO.getEmail();
			//核檢郵件類類別是否為空
			if (!StringUtils.hasText(mailGroup)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MAIL_LIST_MAIL_GROUP)});
				throw new CommonException(msg);
			}
			//核檢姓名是否為空 Task2490，姓名改為不必輸
			/*if (!StringUtils.hasText(name)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MAIL_LIST_NAME)});
				throw new CommonException(msg);
			}*/
			//核檢Mail是否為空
			if (!StringUtils.hasText(main)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MAIL_LIST_EMAIL)});
				throw new CommonException(msg);
			}
			//核檢姓名長度是否超過50
			if (StringUtils.hasText(name) && !ValidateUtils.length(name, 0, 50)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MAIL_LIST_NAME), IAtomsConstants.CONTACT_USER_LENGTH});
				throw new CommonException(msg);
			}
			//核檢mail格式以及長度是否符合要求 Task2490，mail改為可輸入多個，並且長度改為255
			if (!ValidateUtils.length(main, 0, 255)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MAIL_LIST_EMAIL), IAtomsConstants.MAXLENGTH_NUMBER_TWO_HUNDRED_FIFTY_FIVE});
				throw new CommonException(msg);
			}
			if (!ValidateUtils.manyEmail(main)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.FORMAT_ERROR, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MAIL_LIST_EMAIL)});
				throw new CommonException(msg);
			}
		}
		if (IAtomsConstants.ACTION_QUERY.equals(actionId)) {
			//獲取姓名
			String name = command.getQueryName();
			//核檢姓名長度是否超過50
			if (StringUtils.hasText(name)) {
				if (!ValidateUtils.length(name, 1, 50)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MAIL_LIST_NAME), IAtomsConstants.CONTACT_USER_LENGTH});
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
	public MailListManageFormDTO parse(HttpServletRequest request, MailListManageFormDTO command) throws CommonException {
		try {
			MailListDTO mailListDTO = null;
			String actionId = command.getActionId(); 
			if (IAtomsConstants.ACTION_SAVE.equals(actionId) || IAtomsConstants.ACTION_CHECK.equals(actionId)) {
				mailListDTO = BindPageDataUtils.bindValueObject(request, MailListDTO.class);
				command.setMailListDTO(mailListDTO);
			} 
			if (IAtomsConstants.ACTION_QUERY.equals(actionId)) {
				String sort = this.getString(request, IAtomsConstants.QUERY_PAGE_SORT);
				if (StringUtils.hasText(sort)) {
					command.setSort(sort);
				} else {
					command.setSort(MailListManageFormDTO.PARAM_PAGE_SORT_TWO.concat(IAtomsConstants.MARK_SEPARATOR).concat(MailListManageFormDTO.PARAM_PAGE_SORT_ONE));
				}
				String order = this.getString(request, IAtomsConstants.QUERY_PAGE_ORDER);
				if (StringUtils.hasText(order)) {
					command.setOrder(order);
				} else {
					command.setOrder(IAtomsConstants.PARAM_PAGE_ORDER);
				}
			}
			return command;
		} catch (Exception e) {
			LOGGER.error("parse() Exception.", e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
	}
	
}
