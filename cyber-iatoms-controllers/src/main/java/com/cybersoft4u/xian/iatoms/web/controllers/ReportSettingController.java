package com.cybersoft4u.xian.iatoms.web.controllers;

import javax.servlet.http.HttpServletRequest;

import cafe.core.bean.Message;
import cafe.core.config.GenericConfigManager;
import cafe.core.exception.CommonException;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.BindPageDataUtils;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ReportSettingDetailDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ReportSettingDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ReportSettingFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;

/**
 * Purpose: 報表發送功能設定Controller
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2016年7月4日
 * @MaintenancePersonnel ElvaHe
 */
public class ReportSettingController extends AbstractMultiActionController<ReportSettingFormDTO> {

	/**
	 * 日誌記錄物件
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.CONTROLLER, ReportSettingController.class);
	
	/**
	 * Constructor:无參構造函數
	 */
	public ReportSettingController() {
		this.setCommandClass(ReportSettingFormDTO.class);
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(ReportSettingFormDTO command)throws CommonException {
		if (command == null) {
			return false;
		}
		String actionId = command.getActionId();
		Message msg = null;
		if ((IAtomsConstants.ACTION_SAVE.equals(actionId)) || (IAtomsConstants.ACTION_SEND.equals(actionId))) {
			String sendFlag = command.getSendFlag();
			//報表編號
			String settingId = command.getSettingId();
			//客戶 --- 必填
			String customerId = command.getReportSettingDTO().getCompanyId();
			if (!StringUtils.hasText(customerId)) {
				msg = new Message(
						Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_CUSTOMER_ID)});
				throw new CommonException(msg);
			}
			//報表名稱 --- 必填
			String reportName = command.getReportSettingDTO().getReportCode();
			if (!StringUtils.hasText(settingId)) {
				if (!StringUtils.hasText(reportName)) {
					msg = new Message(
							Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_REPORT_NAEM)});
					throw new CommonException(msg);
				}
			}
			//報表明細 --- 必填
			String reportDetail = command.getReportSettingDTO().getReportDetail();
			if (StringUtils.hasText(settingId)) {
				//當報表名稱為
				if ((IAtomsConstants.REPORT_NAME_DAY_REPORT.equals(reportName)) 
						|| (IAtomsConstants.REPORT_NAME_COMPLETE_OVERDUE_RATE_REPORT.equals(reportName))) {
					if (!StringUtils.hasText(reportDetail)) {
						msg = new Message(
								Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_REPORT_DETAIL)});
						throw new CommonException(msg);
					}
				}
			}
			//報表日期
			//收件人 --- 必填,長度小於200
			String recipient = command.getReportSettingDTO().getRecipient();
			if ((!StringUtils.hasText(settingId)) && (StringUtils.hasText(sendFlag)) && (IAtomsConstants.NO.equals(sendFlag)))  {
				if (!StringUtils.hasText(recipient)) {
					msg = new Message(
							Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_RECIPIENT)});
					throw new CommonException(msg);
				} 
				if (!ValidateUtils.length(recipient, 0, 200)) {
					msg = new Message(
							Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_RECIPIENT),
									IAtomsConstants.MAXLENGTH_NUMBER_TWO_HUNDRED});
					throw new CommonException(msg);
				}
			}
			//副本 --- 長度小於200
			String copy = command.getReportSettingDTO().getCopy();
			if ((!StringUtils.hasText(settingId)) && (StringUtils.hasText(sendFlag)) && (IAtomsConstants.NO.equals(sendFlag))) {
				if (StringUtils.hasText(copy)) {
					if (!ValidateUtils.length(copy, 0, 200)) {
						msg = new Message(
								Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_COPY),
										IAtomsConstants.MAXLENGTH_NUMBER_TWO_HUNDRED});
						throw new CommonException(msg);
					}
				}
			}
			//備註 --- 長度小於200
			String remark = command.getReportSettingDTO().getRemark();
			if ((!StringUtils.hasText(settingId)) && (StringUtils.hasText(sendFlag)) && (IAtomsConstants.NO.equals(sendFlag))) {
				if (StringUtils.hasText(remark)) {
					if (!ValidateUtils.length(remark, 0, 200)) {
						msg = new Message(
								Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID,
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_REMARKS),
										IAtomsConstants.MAXLENGTH_NUMBER_TWO_HUNDRED});
						throw new CommonException(msg);
					}
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
	public ReportSettingFormDTO parse(HttpServletRequest request, ReportSettingFormDTO command) throws CommonException {
		try {
			//獲取actionId
			String actionId = command.getActionId();
			LOGGER.debug("parse()", "actionId : ", actionId);
			//若actionId為SAVE
			if((IAtomsConstants.ACTION_SAVE.equals(actionId)) || (IAtomsConstants.ACTION_SEND.equals(actionId))){
				//獲取報表發送功能的DTO  
				ReportSettingDTO reportSettingDTO = BindPageDataUtils.bindValueObject(request, ReportSettingDTO.class);
				this.getTimestamp(request, ReportSettingDetailDTO.ATTRIBUTE.REPORT_DETAIL.getValue());
				command.setReportSettingDTO(reportSettingDTO);
				//獲取報表明細的DTO
				ReportSettingDetailDTO reportSettingDetatilDTO = BindPageDataUtils.bindValueObject(request, ReportSettingDetailDTO.class);
				//獲取選擇的明細信息
				String reportDetails = request.getParameter(IAtomsConstants.REPORT_DETATIL);
				//若存在明細信息
				if(StringUtils.hasText(reportDetails)){
					reportSettingDetatilDTO.setReportDetail(reportDetails);
					command.setReportSettingDetailDTO(reportSettingDetatilDTO);
				}
			}
		} catch (Exception e) {
			LOGGER.error("parse()", "error:", e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
		return command;
	}
}
