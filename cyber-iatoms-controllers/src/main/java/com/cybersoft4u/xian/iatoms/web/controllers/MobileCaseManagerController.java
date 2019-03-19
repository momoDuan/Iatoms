package com.cybersoft4u.xian.iatoms.web.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import cafe.core.exception.CommonException;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.util.BindPageDataUtils;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAssetLinkDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTransactionDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Purpose: 案件處理Controller
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016年8月5日
 * @MaintenancePersonnel CrissZhang
 */
public class MobileCaseManagerController extends CaseManagerController {

	/**
	 * 系統日誌記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(MobileCaseManagerController.class);
		
	/**
	 * Constructor: 無參構造
	 */
	public MobileCaseManagerController() {
		this.setCommandClass(CaseManagerFormDTO.class);
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	public boolean validate(CaseManagerFormDTO command) throws CommonException {
		long startTime = System.currentTimeMillis();
		if (command == null) {
			return false;
		}
		String actionId = command.getActionId();
		LOGGER.debug("actionId : "+actionId);
		
		
		long endTime = System.currentTimeMillis();
		LOGGER.debug("calculate time --> validate", "Controller actionId:" + command.getActionId() + " , costTime:" + (endTime - startTime));
		return true;
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	public CaseManagerFormDTO parse(HttpServletRequest request, CaseManagerFormDTO command) throws CommonException {
		long startTime = System.currentTimeMillis();
		try{
			// 获取actionId
			String actionId = command.getActionId();
			
			LOGGER.debug("IN parse actionId ===> "+actionId);
			if (IAtomsConstants.ACTION_QUERY_CASE.equals(actionId)) {
				LOGGER.debug("IN IAtomsConstants.ACTION_QUERY_CASE...........................");
				command.setIsInstant(true);
				String exportField = this.getString(request, CaseManagerFormDTO.EXPORT_PARAM_FIELDS);
				LOGGER.debug("exportField ====> "+exportField);
				// 查詢條件map集合
				Map<String, String> queryColumnMap = new HashMap<String, String>();
				if(StringUtils.hasText(exportField)){
					for(String tempColumn : StringUtils.toList(exportField, IAtomsConstants.MARK_SEPARATOR)){
						queryColumnMap.put(tempColumn, tempColumn);
					}
				}
				// 設置查詢列
				command.setQueryColumnMap(queryColumnMap);
			} else if (IAtomsConstants.CASE_ACTION.ARRIVE.getCode().equals(actionId) || IAtomsConstants.CASE_ACTION.COMPLETE.getCode().equals(actionId)) {
				SrmCaseTransactionDTO caseTransactionDTO = BindPageDataUtils.bindValueObject(request, SrmCaseTransactionDTO.class);
				command.setSrmCaseTransactionDTO(caseTransactionDTO);
				Gson gsonss = new GsonBuilder().create();
				List<SrmCaseAssetLinkDTO> SrmCaseAssetLinkDTO = 
						(List<SrmCaseAssetLinkDTO>) gsonss.fromJson(
						command.getCaseAssetLinkParameters(), new TypeToken<List<SrmCaseAssetLinkDTO>>(){}.getType());
				command.setSrmCaseAssetLinkDTO(SrmCaseAssetLinkDTO);
			} else {
				// 獲取案件歷程內容
				SrmCaseTransactionDTO caseTransactionDTO = BindPageDataUtils.bindValueObject(request, SrmCaseTransactionDTO.class);
				command.setSrmCaseTransactionDTO(caseTransactionDTO);
				Gson gsonss = new GsonBuilder().create();
				List<SrmCaseAssetLinkDTO> SrmCaseAssetLinkDTO = 
						(List<SrmCaseAssetLinkDTO>) gsonss.fromJson(
						command.getCaseAssetLinkParameters(), new TypeToken<List<SrmCaseAssetLinkDTO>>(){}.getType());
				if (SrmCaseAssetLinkDTO==null) {
					SrmCaseAssetLinkDTO = new ArrayList<SrmCaseAssetLinkDTO>();
				}
				command.setSrmCaseAssetLinkDTO(SrmCaseAssetLinkDTO);
			}
		}catch (Exception e) {
			LOGGER.error("parse()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
		long endTime = System.currentTimeMillis();
		LOGGER.debug("calculate time --> parse", "Controller actionId:" + command.getActionId() + " , costTime:" + (endTime - startTime));
		return command;
	}
	/**
	 * Purpose:簽收
	 * @author Hermanwang
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 
	public ModelAndView sign(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SIGN, command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("sign");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("sign()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	*/
	
	
	/**
	 * Purpose:完修
	 * @author Hermanwang
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 
	public ModelAndView complete(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_COMPLETE, command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("complete");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("complete()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	*/
	
	
	
	
	/**
	 * Purpose:回應
	 * @author Hermanwang
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 
	public ModelAndView response(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, "response", command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("response");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("response()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	*/
	/**
	 * Purpose:延期
	 * @author Hermanwang
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 
	public ModelAndView delay(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, "delay", command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("delay");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("delay()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	*/
	/**
	 * Purpose:到場
	 * @author Hermanwang
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 
	public ModelAndView arrive(HttpServletRequest request, HttpServletResponse response,CaseManagerFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, "arrive", command);
			Message msg = sessionContext.getReturnMessage();
			if (Message.STATUS.SUCCESS == msg.getStatus()) {
				//驗證通過，異步批量建案
				command = (CaseManagerFormDTO)sessionContext.getResponseResult();
				command.setCaseActionId("arrive");
				IAtomsAsynchronousHandler<CaseManagerFormDTO> thread = new IAtomsAsynchronousHandler<CaseManagerFormDTO>(command, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_SEND);
				thread.setServiceLocator(this.getServiceLocator());
				thread.start();
			}
			return new ModelAndView("", CoreConstants.PARAM_SESSION_CONTEXT, sessionContext); 
		} catch (Exception e) {
			LOGGER.error("arrive()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	*/
}
