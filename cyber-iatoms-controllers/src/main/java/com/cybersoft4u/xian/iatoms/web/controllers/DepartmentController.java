package com.cybersoft4u.xian.iatoms.web.controllers;


import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimDepartmentDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.DepartmentFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;

import cafe.core.bean.Message;
import cafe.core.config.GenericConfigManager;
import cafe.core.exception.CommonException;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.BindPageDataUtils;
/**
 * Purpose: 部門維護controller
 * @author Amanda Wang
 * @since  JDK 1.6
 * @date   2016/6/15
 * @MaintenancePersonnel Amanda Wang
 */
public class DepartmentController extends AbstractMultiActionController<DepartmentFormDTO>{
	/**
	 * 系统日志记录工具
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.CONTROLLER, DepartmentController.class);
	
	/**
	 * Constructor:無參構造函數
	 */
	public DepartmentController() {
		this.setCommandClass(DepartmentFormDTO.class);
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(DepartmentFormDTO command) throws CommonException {
		if (command == null) {
			return false;
		}
		String actionId = command.getActionId();
		Message msg = null;
		if (IAtomsConstants.ACTION_SAVE.equals(actionId)) {
			//檢驗頁面輸入
			BimDepartmentDTO departmentDTO = command.getBimDepartmentDTO();
			if (departmentDTO == null) {
				return false;
			} else {
				//部門名稱 --- 必輸、長度小於50
				String deptName = departmentDTO.getDeptName();
				if (!StringUtils.hasText(deptName)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_DEPARTMENT_DEPT_NAME)});
					throw new CommonException(msg);
				} else {
					if (!ValidateUtils.length(deptName, 0, 50)) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_DEPARTMENT_DEPT_NAME),
								IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
						throw new CommonException(msg);
					}
				}
				String companyId = departmentDTO.getCompanyId();
				//公司 --- 必輸下拉框
				if (!StringUtils.hasText(companyId)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_DEPARTMENT_COMPANY_ID)});
					throw new CommonException(msg);
				}
				//聯絡人 --- 長度小於50
				String contact = departmentDTO.getContact();
				if ((StringUtils.hasText(contact)) && (!ValidateUtils.length(contact, 0, 50))) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_DEPARTMENT_CONTACT),
							IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
					throw new CommonException(msg);
				}
				//聯絡人電話 --- 長度小於20
				String contactTel = departmentDTO.getContactTel();
				if ((StringUtils.hasText(contactTel)) && (!ValidateUtils.length(contactTel, 0, 20))) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_DEPARTMENT_CONTACT_TEL),
							IAtomsConstants.MAXLENGTH_NUMBER_TWENTY});
					throw new CommonException(msg);
				}
				//聯絡人傳真 --- 長度小於20
				String contactFax = departmentDTO.getContactFax();
				if ((StringUtils.hasText(contactFax)) && (!ValidateUtils.length(contactFax, 0, 20))) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_DEPARTMENT_CONTACT_FAX),
							IAtomsConstants.MAXLENGTH_NUMBER_TWENTY});
					throw new CommonException(msg);
				}
				//聯絡人Email --- 長度小於50
				String contactEmail = departmentDTO.getContactEmail();
				if ((StringUtils.hasText(contactEmail)) && (!ValidateUtils.length(contactEmail, 0, 50))) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_DEPARTMENT_CONTACT_EMAIL),
							IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
					throw new CommonException(msg);
				}
				//部門地址 --- 長度小於100
				String address = departmentDTO.getAddress();
				if ((StringUtils.hasText(address)) && (!ValidateUtils.length(address, 0, 100))) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_DEPARTMENT_ADDRESS),
							IAtomsConstants.MAXLENGTH_NUMBER_ONE_HUNDRED});
					throw new CommonException(msg);
				}
				//說明 --- 長度小於200
				String remark = departmentDTO.getRemark();
				if ((StringUtils.hasText(remark)) && (!ValidateUtils.length(remark, 0, 200))) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_DEPARTMENT_REMARK),
							IAtomsConstants.MAXLENGTH_NUMBER_TWO_HUNDRED});
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
	public DepartmentFormDTO parse(HttpServletRequest request, DepartmentFormDTO command) throws CommonException {
		try {
			if (command == null) {
				command = new DepartmentFormDTO();
			}
			// 获取actionId
			String actionId = this.getString(request, IAtomsConstants.FIELD_ACTION_ID);
			if (!StringUtils.hasText(actionId)) {
				actionId = IAtomsConstants.ACTION_INIT;
			} else if (IAtomsConstants.ACTION_SAVE.equals(actionId)){
				BimDepartmentDTO bimDepartmentDTO = command.getBimDepartmentDTO();
				//綁定頁面值對象
				bimDepartmentDTO = BindPageDataUtils.bindValueObject(request, BimDepartmentDTO.class);
				command.setBimDepartmentDTO(bimDepartmentDTO);
			}
			command.setActionId(actionId);
			return command;
		} catch (Exception e) {
			if (LOGGER != null) {
				LOGGER.error("parse()", "is error: ", e);
			}
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
	}
}