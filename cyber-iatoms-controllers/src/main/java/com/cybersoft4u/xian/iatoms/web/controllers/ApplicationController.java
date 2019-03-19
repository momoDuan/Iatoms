package com.cybersoft4u.xian.iatoms.web.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import cafe.core.config.GenericConfigManager;
import cafe.core.context.SessionContext;
import cafe.core.exception.CommonException;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.BindPageDataUtils;
import cafe.core.web.controller.util.SessionHelper;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ApplicationDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ApplicationFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;
import com.cybersoft4u.xian.iatoms.common.utils.ReportExporter;

/**
 * Purpose: 程式版本維護Controller
 * @author echomou
 * @since  JDK 1.6
 * @date   2016-7-14
 * @MaintenancePersonnel echomou
 */
public class ApplicationController extends AbstractMultiActionController<ApplicationFormDTO> {

	/**
	 * 日誌記錄物件
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.CONTROLLER, ApplicationController.class);
	
	/**
	 * Constructor:无參構造函數
	 */
	public ApplicationController() {
		this.setCommandClass(ApplicationFormDTO.class);
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(ApplicationFormDTO command) throws CommonException {
		/*if (command == null) {
			return false;
		}
		String actionId = command.getActionId();
		Message msg = null;
			if (IAtomsConstants.ACTION_SAVE.equals(actionId)) {
			//檢驗頁面輸入
			ApplicationDTO applicationDTO = command.getApplicationDTO();
			if (applicationDTO == null) {
				return false;
			}
			String customerId = applicationDTO.getCustomerId();
			String applicationName = applicationDTO.getName();
			String assetGategory = applicationDTO.getAssetCategory();
			if (!StringUtils.hasText(customerId)) {
				//若未輸入客戶編號
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK,new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_COMPANY_ID)});
				throw new CommonException(msg);
			}
			if (!StringUtils.hasText(applicationName)) {
				//若未輸入程式名稱
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK,new String[]{i18NUtil.getName(IAtomsConstants.FIELD_PVM_APPLICATION_NAME)});
				throw new CommonException(msg);
			}
			if (!StringUtils.hasText(assetGategory)) {
				//若未輸入設備類別
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK,new String[]{i18NUtil.getName(IAtomsConstants.FIELD_PVM_APPLICATION_ASSET_GATEGORY)});
				throw new CommonException(msg);
			}
		}*/
		return true;
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public ApplicationFormDTO parse(HttpServletRequest request, ApplicationFormDTO command) throws CommonException {
		try {
			if(command == null) {
				command = new ApplicationFormDTO();
			}
			// 获取actionId
			String actionId = this.getString(request, IAtomsConstants.FIELD_ACTION_ID);
			if (!StringUtils.hasText(actionId)) {
				actionId = IAtomsConstants.ACTION_INIT;
			}
			command.setActionId(actionId);
			if(actionId.equals(IAtomsConstants.ACTION_SAVE)){
				String[] assetTypeIds = request.getParameterValues(ApplicationDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue() + "[]");
				ApplicationDTO applicationDTO = BindPageDataUtils.bindValueObject(request, ApplicationDTO.class);
				String assetTypeId = assetTypeIds[0];
				for(int i = 1; i < assetTypeIds.length; i++){
					assetTypeId += "," + assetTypeIds[i];
				}
				applicationDTO.setAssetTypeId(assetTypeId);
				command.setApplicationDTO(applicationDTO);
			}
			return command;
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + "Exception----parse()", e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
	}

	/**
	 * Purpose:匯出程式版本報表
	 * @author jasonzhou
	 * @param request：HttpServletRequest
	 * @param response：HttpServletResponse
	 * @param command：ApplicationFormDTO
	 * @throws CommonException：出錯時拋出CommonException
	 * @return void
	 */
	@SuppressWarnings("rawtypes")
	public ModelAndView export(HttpServletRequest request, HttpServletResponse response, ApplicationFormDTO command) throws CommonException{
		// 取得sessionId
		String sessionId = request.getSession().getId();
		try{
			if(command != null){
				IAtomsLogonUser logonUser = (IAtomsLogonUser) command.getLogonUser();
				JasperReportCriteriaDTO criteria = new JasperReportCriteriaDTO();
				command.setPage(Integer.valueOf(-1));
				command.setRows(Integer.valueOf(-1));
				SessionContext returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_APPLICATION_SERVICE, IAtomsConstants.ACTION_QUERY, command);
				List<ApplicationDTO> results = null;
				if(returnCtx != null){
					command = (ApplicationFormDTO) returnCtx.getResponseResult();
					results = command.getList();
				}
				//封裝報表對象
				if(!CollectionUtils.isEmpty(results)){
					// 是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
					criteria.setAutoBuildJasper(false);
					criteria.setResult(results);
					//設置所需報表的Name
					criteria.setJrxmlName(ApplicationFormDTO.REPORT_JRXML_NAME);
					//設置報表路徑
					criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
					//設置匯出格式
					criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
					//設置報表Name
					criteria.setReportFileName(ApplicationFormDTO.REPORT_FILE_NAME);
					criteria.setSheetName(ApplicationFormDTO.REPORT_FILE_NAME);
					ReportExporter.exportReport(criteria, response);
					// 成功標志
					SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, IAtomsConstants.YES);
				}
			}
		}catch(Exception e){
			// 出錯消息
			try {
				SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, false);
				Map map = new HashMap();
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.EXPORT_ERROR_MESSAGE));
				return new ModelAndView(this.getFailureView(IAtomsConstants.ACTION_EXPORT), map);
			} catch (Exception e1) {
				LOGGER.error(".export() is error:", e1);
			}
			LOGGER.error(this.getClass().getName() + ".export() is error: " + e, e);
		}
		return null;
	}
}
