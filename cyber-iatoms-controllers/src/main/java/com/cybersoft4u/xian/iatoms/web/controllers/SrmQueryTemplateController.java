package com.cybersoft4u.xian.iatoms.web.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmQueryTemplateDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.SrmQueryTemplateFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractSlaFormDTO;

import cafe.core.config.GenericConfigManager;
import cafe.core.exception.CommonException;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.BindPageDataUtils;
/**
 * Purpose: 用戶欄位模板維護檔Controller
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016/6/29
 * @MaintenancePersonnel CrissZhang
 */
public class SrmQueryTemplateController extends AbstractMultiActionController<SrmQueryTemplateFormDTO>{
	/**
	 * 系统日志记录工具
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.CONTROLLER, SrmQueryTemplateController.class);
	
	/**
	 * Constructor:無參構造函數
	 */
	public SrmQueryTemplateController(){
		this.setCommandClass(SrmQueryTemplateFormDTO.class);
	}
	/** (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(SrmQueryTemplateFormDTO command) throws CommonException {
		return true;
	}

	/** (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public SrmQueryTemplateFormDTO parse(HttpServletRequest request, SrmQueryTemplateFormDTO command) throws CommonException {
		try {
			// 得到actionId
			String actionId = command.getActionId();
			if (!StringUtils.hasText(actionId)) {
				actionId = IAtomsConstants.ACTION_INIT;
			} else if (IAtomsConstants.ACTION_SAVE_TEMPLATE.equals(actionId)){
				SrmQueryTemplateDTO admUserColumnTemplateDTO = BindPageDataUtils.bindValueObject(request, SrmQueryTemplateDTO.class);
				command.setAdmUserColumnTemplateDTO(admUserColumnTemplateDTO);
			}
		} catch (Exception e) {
			LOGGER.error("parse()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
		return command;
	}	
	
}
