package com.cybersoft4u.xian.iatoms.web.controllers;

import java.util.List;

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
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.PermissionDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AdmRoleFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Purpose: 系統角色控制器
 * @author CarrieDuan
 * @since  JDK 1.6
 * @date   2016/6/27
 * @MaintenancePersonnel CarrieDuan
 */
public class AdmRoleController extends AbstractMultiActionController<AdmRoleFormDTO> {
	/**
	 * 日誌記錄器
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.CONTROLLER, AdmRoleController.class);
	
	/**
	 * Constructor:無參構造
	 */
	public AdmRoleController() {
		this.setCommandClass(AdmRoleFormDTO.class);
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(AdmRoleFormDTO command) throws CommonException {
		if (command == null) {
			return false;
		}
		String actionId = command.getActionId();
		Message msg = null;
		if (IAtomsConstants.ACTION_SAVE.equals(actionId)){
			AdmRoleDTO roleDTO = command.getAdmRoleDTO();
			if (roleDTO == null) {
				return false;
			}
			String roleCode = roleDTO.getRoleCode();
			String roleName = roleDTO.getRoleName();
			String roleDesc = roleDTO.getRoleDesc();
			if (!StringUtils.hasText(roleCode)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_ROLE_ROLE_CODE)});
				throw new CommonException(msg);
			}
			if (!StringUtils.hasText(roleName)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_ROLE_ROLE_NAME)});
				throw new CommonException(msg);
			}
			if (!ValidateUtils.length(roleCode, 1, 20)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_ROLE_ROLE_CODE), IAtomsConstants.PROPERTY_ID_LENGTH});
				throw new CommonException(msg);
			}
			if (!ValidateUtils.length(roleName, 1, 50)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_ROLE_ROLE_NAME), IAtomsConstants.CONTACT_USER_LENGTH});
				throw new CommonException(msg);
			}
			if (StringUtils.hasText(roleDesc)) {
				if (!ValidateUtils.length(roleDesc, 1, 200)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_ROLE_ROLE_DESC), IAtomsConstants.MAXLENGTH_NUMBER_TWO_HUNDRED});
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
	public AdmRoleFormDTO parse(HttpServletRequest request,
			AdmRoleFormDTO command) throws CommonException {
		try {
			// 獲取actionId
			String actionId = command.getActionId();
			if (IAtomsConstants.ACTION_QUERY.equals(actionId)) {
				//列表畫面grid查詢
				String sort = this.getString(request, IAtomsConstants.QUERY_PAGE_SORT);
				if (StringUtils.hasText(sort)) {
					//sort = AdmRoleDTO.getATTRIBUTENameByValue(sort);
					command.setSort(sort);
				} else {
					command.setSort(AdmRoleFormDTO.PARAM_PAGE_SORT);
				}
				String order = this.getString(request, IAtomsConstants.QUERY_PAGE_ORDER);
				if (StringUtils.hasText(order)) {
					command.setOrder(order);
				} else {
					command.setOrder(IAtomsConstants.PARAM_PAGE_ORDER);
				}
			} else if (IAtomsConstants.ACTION_SAVE.equals(actionId)) {
				// 綁定畫面上參數到DTO
				AdmRoleDTO admRoleDTO = BindPageDataUtils.bindValueObject(request, AdmRoleDTO.class);
				command.setAdmRoleDTO(admRoleDTO);
			} else if (IAtomsConstants.ACTION_SAVE_ROLE_PERMISSION.equals(actionId)) {
				//明細畫面保存操作
				//解析畫面的functionList
				String permissions = this.getString(request, AdmRoleFormDTO.PARAM_PERMISSION_LIST);
				Gson gsonss = new GsonBuilder().create();
				List<PermissionDTO> permissionDTOs = ( List<PermissionDTO> ) gsonss.fromJson(permissions, new TypeToken<List<PermissionDTO>>(){}.getType());
				command.setPermissionDTOs(permissionDTOs);
			}
		} catch (Exception e) {
			LOGGER.error("parse()", "Exception.", e);	
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
		return command;
	}

}
