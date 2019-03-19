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
import com.cybersoft4u.xian.iatoms.common.bean.dto.WarehouseDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.WarehouseFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;

/**
 * Purpose:倉庫控制器
 * @author ElvaHe
 * @since JDK 1.6
 * @date 2016年6月1日
 * @MaintenancePersonnel ElvaHe
 */
public class WarehouseController extends AbstractMultiActionController<WarehouseFormDTO> {
	/**
	 * 日誌記錄器
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.CONTROLLER, WarehouseController.class);

	/**
	 * Constructor:無參構造
	 */
	public WarehouseController() {
		this.setCommandClass(WarehouseFormDTO.class);
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(WarehouseFormDTO command) throws CommonException {
		if (command == null) {
			return false;
		}
		String actionId = command.getActionId();
		Message msg = null;
		if (IAtomsConstants.ACTION_SAVE.equals(actionId)) {
			//倉庫編號
			//String warehouseId = command.getWarehouseId();
			//廠商 --- 必填
			String companyId = command.getWarehouseDTO().getCompanyId();
			if (!StringUtils.hasText(companyId)) {
				msg = new Message(
						Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_WAREHOUSE_COMPANY_ID)});
				throw new CommonException(msg);
			}
			//倉庫名稱 --- 必填，長度小於50
			String name = command.getWarehouseDTO().getName();
			if (!StringUtils.hasText(name)) {
				msg = new Message(
						Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_WAREHOUSE_NAME)});
				throw new CommonException(msg);
			}
			if (!ValidateUtils.length(name, 0, 50)) {
				msg = new Message(
						Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_WAREHOUSE_NAME),
								IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
				throw new CommonException(msg);
			}
			//聯絡人 --- 必填，長度小於50
			String contact = command.getWarehouseDTO().getContact();
			if (!StringUtils.hasText(contact)) {
				msg = new Message(
						Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_WAREHOUSE_CONTACT)});
				throw new CommonException(msg);
			}
			if (!ValidateUtils.length(contact, 0, 50)) {
				msg = new Message(
						Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_WAREHOUSE_CONTACT),
								IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
				throw new CommonException(msg);
			}
			//電話 --- 必填，長度小於20
			String tel = command.getWarehouseDTO().getTel();
			if (!StringUtils.hasText(tel)) {
				msg = new Message(
						Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_WAREHOUSE_TEL)});
				throw new CommonException(msg);
			}
			if (!ValidateUtils.length(tel, 0, 20)) {
				msg = new Message(
						Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_WAREHOUSE_TEL),
								IAtomsConstants.MAXLENGTH_NUMBER_TWENTY});
				throw new CommonException(msg);
			}
			//傳真 --- 長度小於20
			String fax = command.getWarehouseDTO().getFax();
			if (StringUtils.hasText(fax)) {
				if (!ValidateUtils.length(fax, 0, 20)) {
					msg = new Message(
							Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_WAREHOUSE_FAX),
									IAtomsConstants.MAXLENGTH_NUMBER_TWENTY});
					throw new CommonException(msg);
				}
			}
			
			//倉庫地址-縣市 --- 必填
			String location = command.getWarehouseDTO().getLocation();
			if (!StringUtils.hasText(location)) {
				msg = new Message(
						Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_WAREHOUSE_LOCATION)});
				throw new CommonException(msg);
			}
			//倉庫地址 --- 必填，長度小於100
			String address = command.getWarehouseDTO().getAddress();
			if (!StringUtils.hasText(address)) {
				msg = new Message(
						Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_WAREHOUSE_ADDRESS)});
				throw new CommonException(msg);
			}
			if (!ValidateUtils.length(address, 0, 100)) {
				msg = new Message(
						Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_WAREHOUSE_ADDRESS),
								IAtomsConstants.MAXLENGTH_NUMBER_ONE_HUNDRED});
				throw new CommonException(msg);
			}
			//說明 --- 長度小於200
			String comment = command.getWarehouseDTO().getComment();
			if (StringUtils.hasText(comment)) {
				if (!ValidateUtils.length(comment, 0, 200)) {
					msg = new Message(
							Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_WAREHOUSE_COMMENT),
									IAtomsConstants.MAXLENGTH_NUMBER_TWO_HUNDRED});
					throw new CommonException(msg);
				}
			}
		}
		return true;
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest,
	 *      cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public WarehouseFormDTO parse (HttpServletRequest request, WarehouseFormDTO command) throws CommonException {
		try {
			if (command == null) {
				command = new WarehouseFormDTO();
			}
			// 獲取actionId
			String actionId = this.getString(request, IAtomsConstants.FIELD_ACTION_ID);
			if (!StringUtils.hasText(actionId)) {
				actionId = IAtomsConstants.ACTION_INIT;
			}
			command.setActionId(actionId);
			if (IAtomsConstants.ACTION_QUERY.equals(actionId)) {
				String sort = this.getString(request, IAtomsConstants.QUERY_PAGE_SORT);
				if (StringUtils.hasText(sort)) {
					command.setSort(sort);
				} else {
					command.setSort(WarehouseFormDTO.PARAM_PAGE_SORT);
				}
				String order = this.getString(request, IAtomsConstants.QUERY_PAGE_ORDER);
				if (StringUtils.hasText(order)) {
					command.setOrder(order);
				} else {
					command.setOrder(IAtomsConstants.PARAM_PAGE_ORDER);
				}
			} else if (IAtomsConstants.ACTION_SAVE.equals(actionId)) {
				// 綁定畫面上參數到DTO
				WarehouseDTO warehouseDTO = BindPageDataUtils.bindValueObject(request, WarehouseDTO.class);
				command.setWarehouseDTO(warehouseDTO);
			}
		} catch (Exception e) {
			LOGGER.error("parse()", "Exception.", e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
		return command;
	}
}
