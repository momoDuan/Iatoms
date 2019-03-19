package com.cybersoft4u.xian.iatoms.web.controllers;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import cafe.core.bean.Message;
import cafe.core.exception.CommonException;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.BindPageDataUtils;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmSuppliesDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.SuppliesTypeFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;

/**
 * Purpose: 耗材品项維護Controller
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2016-7-4
 * @MaintenancePersonnel HermanWang
 */
public class SuppliesTypeController extends AbstractMultiActionController<SuppliesTypeFormDTO>{
	/**
	 * 日志记录物件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(SuppliesTypeController.class);
	/**
	 * 
	 * Constructor:構造器
	 */
	public SuppliesTypeController() {
		this.setCommandClass(SuppliesTypeFormDTO.class);
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(SuppliesTypeFormDTO command) throws CommonException {
		if (command == null) {
			return false;
		}
		String actionId = command.getActionId();
		Message msg = null;
		if(IAtomsConstants.ACTION_SAVE.equals(actionId)) {
			DmmSuppliesDTO suppliesDTO = command.getSuppliesDTO();
			if(suppliesDTO == null){
				return false;
			}
			String companyId = suppliesDTO.getCompanyId();
			String suppliesType = suppliesDTO.getSuppliesType();
			String supliesName = suppliesDTO.getSuppliesName();
			BigDecimal price = suppliesDTO.getPrice();
			//未輸入客戶
			if (!StringUtils.hasText(companyId)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_SUPPILIES_COMPANY_ID)});
				throw new CommonException(msg);
			}
			//耗材分類
			if (!StringUtils.hasText(suppliesType)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_SUPPILIES_SUPPLIES_TYPE)});
				throw new CommonException(msg);
			}
			//耗材名稱
			if (!StringUtils.hasText(supliesName)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_SUPPILIES_SUPPLIES_NAME)});
				throw new CommonException(msg);
			} else {
				if (!ValidateUtils.length(supliesName, 0, 50)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_SUPPILIES_PRICE), 
							IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
					throw new CommonException(msg);
				}
			}
			String priceString = price.toString();
			if(StringUtils.hasText(priceString)) {
				if (!ValidateUtils.varcharLength(priceString, 0, 10)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_SUPPILIES_PRICE), 
							IAtomsConstants.MAXLENGTH_NUMBER_TEN});
					throw new CommonException(msg);
				}
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_SUPPILIES_PRICE)});
				throw new CommonException(msg);
			}
		}
		return true;
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	public SuppliesTypeFormDTO parse(HttpServletRequest request, SuppliesTypeFormDTO command) throws CommonException {
		try {
			if (command == null) {
				command = new SuppliesTypeFormDTO();
			}			
			//存儲方法要綁值成為dto
			if(IAtomsConstants.ACTION_SAVE.equals(command.getActionId())){
				//保存獲得頁面上的資料放到dto中,綁值
				DmmSuppliesDTO suppliesDTO = BindPageDataUtils.bindValueObject(request, DmmSuppliesDTO.class);
				command.setSuppliesDTO(suppliesDTO);
			}
			return command;
		} catch (Exception e) {
			LOGGER.error("Exception----parse()", "Controller Exception", e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
	}
}
