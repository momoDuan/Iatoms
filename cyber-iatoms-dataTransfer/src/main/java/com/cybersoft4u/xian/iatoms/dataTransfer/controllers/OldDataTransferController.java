package com.cybersoft4u.xian.iatoms.dataTransfer.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;

import cafe.core.bean.Message;
import cafe.core.config.SystemConfigManager;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.exception.CommonException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.SessionHelper;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.async.IAtomsAsynchronousHandler;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryHistoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAssetLinkDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAttFileDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTransactionDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmTransactionParameterItemDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;
import com.cybersoft4u.xian.iatoms.common.utils.FileUtils;
import com.cybersoft4u.xian.iatoms.common.utils.IAtomsDateTimeUtils;
import com.cybersoft4u.xian.iatoms.common.utils.POIUtils;
import com.cybersoft4u.xian.iatoms.common.utils.ReportExporter;
import com.cybersoft4u.xian.iatoms.dataTransfer.formDTO.OldDataTransferFormDTO;

/**
 * Purpose: 舊資料轉檔Controller
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2017-7-13
 * @MaintenancePersonnel CrissZhang
 */
public class OldDataTransferController extends AbstractMultiActionController<OldDataTransferFormDTO>{

	/**
	 * 系統日誌記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(OldDataTransferController.class);
	/**
	 * 案件類型對應的模板名稱
	 */
	private Map<String,String> importFilePath;
	/**
	 * Constructor:无参构造函数
	 */
	public OldDataTransferController() {
		this.setCommandClass(OldDataTransferFormDTO.class);
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(OldDataTransferFormDTO arg0) throws CommonException {
		return true;
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public OldDataTransferFormDTO parse(HttpServletRequest arg0,
			OldDataTransferFormDTO command) throws CommonException {
		try {
			String actionId = command.getActionId();
			if ("transferRepository".equals(actionId)) {
				// 得到交易參數可編輯的字段
				MultiParameterInquiryContext param = new MultiParameterInquiryContext();
				String versionDate = DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDD_DASH);
				param.addParameter(SrmTransactionParameterItemDTO.ATTRIBUTE.EFFECTIVE_DATE.getValue(), versionDate);
				SessionContext returnCtx = this.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE,
						IAtomsConstants.ACTION_GET_EDIT_FIELDS_GROUP_BY_TRANSACTION_TYPE, param);
				// 可編輯字段的map集合
				Map<String,List<String>> editFildsMap = (Map<String,List<String>>) returnCtx.getResponseResult();
				command.setEditFildsMap(editFildsMap);
			} else if("transferCaseHandleInfo".equals(actionId)) {
				String transferCaseId = command.getTransferCaseId();
				if(StringUtils.hasText(transferCaseId)){
					transferCaseId = transferCaseId.trim().replace("\n", IAtomsConstants.MARK_SEPARATOR);
					command.setTransferCaseId(transferCaseId);
				}
			}
			
		} catch (Exception e) {
			LOGGER.error("Exception----parse()", e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
		return command;
	}
	/**
	 * 
	 * Purpose:
	 * @author CrissZhang
	 * @param command
	 * @return
	 * @throws CommonException
	 * @return SessionContext
	 */
	public SessionContext transferCaseHandleInfoPart(OldDataTransferFormDTO command)throws CommonException{
		try {
			// 處理中案件轉移筆數
			int transferCaseInfoNum = 0;
			// 處理中案件歷程轉移筆數
			int transferCaseTransactionsNum = 0;
			// 處理中案件附加資料轉移筆數
			int transferCaseFilesNum = 0;
			LOGGER.debug(".transferCaseHandleInfo()", "Controller startDate()" + DateTimeUtils.getCurrentTimestamp());
			Timestamp startDate = DateTimeUtils.getCurrentTimestamp();
			SessionContext sessionContext = this.serviceLocator.doService(null, "oldDataTransferService", "transferCaseHandleInfo", command);
			// 查詢結果的map集合
			Map queryMap = (Map) sessionContext.getAttribute(IAtomsConstants.PARAM_ACTION_RESULT);
			// 返回的map
			Map returnMap = new HashMap();
			if(!CollectionUtils.isEmpty(queryMap) && queryMap.containsKey(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS)){
				// 查詢無誤，進行保存
				if((Boolean) queryMap.get(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS)){
					// 得到fotmDTO
					command = (OldDataTransferFormDTO)sessionContext.getResponseResult();
					List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOs = command.getSrmCaseHandleInfoDTOs();
					// 案件與歷程集合
					Map<String, List<SrmCaseTransactionDTO>> caseTransactionMap = command.getCaseTransactionMap();
					// 案件與附加資料集合
					Map<String, List<SrmCaseAttFileDTO>> caseAttFileMap = command.getCaseAttFileMap();
					
					// 處理中案件轉移筆數
					transferCaseInfoNum = command.getTransferCaseInfoNum();
					// 處理中案件歷程轉移筆數
					transferCaseTransactionsNum = command.getTransferCaseTransactionsNum();
					// 處理中案件附加資料轉移筆數
					transferCaseFilesNum = command.getTransferCaseFilesNum();
					
					
					LOGGER.debug(".transferCaseHandleInfo()", "Controller saveStart()" + DateTimeUtils.getCurrentTimestamp());
					Timestamp saveStart = DateTimeUtils.getCurrentTimestamp();
					// 500筆分線程
				//	int pageSize = 500;
					int pageSize = 300;
					int threadSize = 0;
					int listSize = srmCaseHandleInfoDTOs.size();
					if(listSize % pageSize == 0){
						threadSize = listSize / pageSize;
					} else {
						threadSize = listSize / pageSize + 1;
					}
					IAtomsAsynchronousHandler<OldDataTransferFormDTO>[] threads = new IAtomsAsynchronousHandler[threadSize];
					// 案件集合
					List<SrmCaseHandleInfoDTO> tempDtos = null;
					OldDataTransferFormDTO formDTO = null;
					for(int i = 0; i < threadSize; i ++) {
						if (i == threadSize -1) {
							tempDtos = srmCaseHandleInfoDTOs.subList(i * pageSize, listSize);
						} else {
							tempDtos = srmCaseHandleInfoDTOs.subList(i * pageSize, (i + 1) * pageSize);
						}
						formDTO = new OldDataTransferFormDTO();
						
						formDTO.setSrmCaseHandleInfoDTOs(tempDtos);
						formDTO.setCaseTransactionMap(caseTransactionMap);
						formDTO.setCaseAttFileMap(caseAttFileMap);
						// 分線程處理保存
						threads[i] = new IAtomsAsynchronousHandler<OldDataTransferFormDTO>(formDTO, "oldDataTransferService", "saveCaseDataThread");
						threads[i].setServiceLocator(this.getServiceLocator());
						threads[i].start();
					}
//					OldDataTransferFormDTO saveFormDTO = null;
					boolean saveFlag = true;
					// 判斷線程轉入結果
					for(IAtomsAsynchronousHandler tempThread : threads){
						try {
							tempThread.join();
							// 判斷線程是否出錯
							if(tempThread.getIsExcuFailure()){
								saveFlag = false;
							}
						} catch (Exception e) {
							saveFlag = false;
						}
					}
					// 若保存成功
					if(saveFlag){
						// 保存成功消息
						SessionContext countCtx = this.serviceLocator.doService(null, "oldDataTransferService", "queryTransferCaseNum", command.getTransferCaseId());
						Map<String, Integer> resultMap = (Map<String, Integer>) countCtx.getResponseResult();
						StringBuilder msStringBuilder = new StringBuilder();
						msStringBuilder.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")").append(queryMap.get("resultMsg"));
						msStringBuilder.append("處理中案件資料共：" + transferCaseInfoNum + "筆，轉入：" + resultMap.get("transferCaseInfoNum")+"筆</br>");
						msStringBuilder.append("處理中案件歷程資料共：" + transferCaseTransactionsNum + "筆，轉入：" + resultMap.get("transferCaseTransactionsNum")+"筆</br>");
						msStringBuilder.append("處理中案件附加資料共：" + transferCaseFilesNum + "筆，轉入：" + resultMap.get("transferCaseFilesNum")+"筆</br>");
						
						returnMap.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
						returnMap.put("resultMsg", msStringBuilder.toString());
						
					//	returnMap.put("resultMsg", "(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + queryMap.get("resultMsg"));
					} else {
						returnMap.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
						returnMap.put("resultMsg", "(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "處理中案件資料轉入失敗</br>");
					}
					LOGGER.debug(".transferCaseHandleInfo()", "Controller endDate" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH));
					Timestamp endDate = DateTimeUtils.getCurrentTimestamp();
					
					long s = IAtomsDateTimeUtils.dateSubtractionMillis(startDate, endDate);
					try {
						LOGGER.debug(".transferCaseHandleInfo()", "Controller totalTime" + (s/60000.0)*100/100);
					} catch (Throwable e) {
					}
				} else {
					returnMap.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
				//	returnMap.put("resultMsg", queryMap.get("resultMsg"));
					returnMap.put("resultMsg", "(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "處理中案件資料轉入失敗</br>" + queryMap.get("resultMsg"));
				}
			} else {
				returnMap.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
				returnMap.put("resultMsg", "(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "處理中案件資料轉入失敗</br>");
			}
			if(!(Boolean) returnMap.get(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS)){
				// Task #3066 只轉入特定案件編號案件
				// 刪除案件信息
			//	this.serviceLocator.doService(null, "oldDataTransferService", "deleteCaseInfoData", returnMap.get("resultMsg"));
			} else {
				// 保存成功消息
				this.serviceLocator.doService(null, "oldDataTransferService", "saveSuccessLog", returnMap.get("resultMsg").toString());
			}
		//	return new ModelAndView(new MappingJacksonJsonView(), returnMap); 
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, returnMap);
			return sessionContext;
		} catch (Exception e) {
			LOGGER.error("transferCaseHandleInfo()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:
	 * @author CrissZhang
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 */
	public ModelAndView transferCaseHandleInfo(HttpServletRequest request, HttpServletResponse response, OldDataTransferFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = transferCaseHandleInfoPart(command);
			return new ModelAndView(this.getSuccessView("transferCaseHandleInfo"), IAtomsConstants.PARAM_SESSION_CONTEXT, sessionContext);
		} catch (Exception e) {
			LOGGER.error("transferCaseHandleInfo()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * 
	 * Purpose:
	 * @author CrissZhang
	 * @param command
	 * @return
	 * @throws CommonException
	 * @return SessionContext
	 */
	public SessionContext transferCaseNewHandleInfoPart(OldDataTransferFormDTO command)throws CommonException{
		try {
			//  案件最新轉移筆數
			int transferCaseNewInfoNum = 0;
			// 案件最新歷程轉移筆數
			int transferCaseNewLinkNum = 0;
			
			LOGGER.debug(".transferCaseNewHandleInfo()", "Controller startDate" + DateTimeUtils.getCurrentTimestamp());
			Timestamp startDate = DateTimeUtils.getCurrentTimestamp();
			// 得到交易參數可編輯的字段
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			String versionDate = DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDD_DASH);
			param.addParameter(SrmTransactionParameterItemDTO.ATTRIBUTE.EFFECTIVE_DATE.getValue(), versionDate);
			SessionContext returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE,
					IAtomsConstants.ACTION_GET_EDIT_FIELDS_GROUP_BY_TRANSACTION_TYPE, param);
			// 可編輯字段的map集合
			Map<String,List<String>> editFildsMap = (Map<String,List<String>>) returnCtx.getResponseResult();
			command.setEditFildsMap(editFildsMap);
			// 查詢案件信息
			SessionContext sessionContext = this.serviceLocator.doService(null, "oldDataTransferService", "transferCaseNewHandleInfo", command);
			// 查詢結果的map集合
			Map queryMap = (Map) sessionContext.getAttribute(IAtomsConstants.PARAM_ACTION_RESULT);
			// 返回的map
			Map returnMap = new HashMap();
			if(!CollectionUtils.isEmpty(queryMap) && queryMap.containsKey(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS)){
				// 查詢無誤，進行保存
				if((Boolean) queryMap.get(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS)){
					// 得到fotmDTO
					command = (OldDataTransferFormDTO)sessionContext.getResponseResult();
					
					// 得到最新案件信息集合
					List<SrmCaseHandleInfoDTO> srmCaseNewHandleInfoDTOs = command.getSrmCaseHandleInfoDTOs();
					// 得到TMS資料
					Map<String, SrmCaseHandleInfoDTO> tmsCaseInfoMap = command.getTmsCaseInfoMap();
					// 案件周邊設備信息集合
					Map<String, SrmCaseHandleInfoDTO> assetCaseInfoListMap = command.getAssetCaseInfoListMap();
					// 案件最新設備鏈接檔集合
					Map<String, List<SrmCaseAssetLinkDTO>> caseAssetLinkMap = command.getCaseAssetLinkMap();
					
					//  案件最新轉移筆數
					transferCaseNewInfoNum = command.getTransferCaseNewInfoNum();
					// 案件最新歷程轉移筆數
					transferCaseNewLinkNum = command.getTransferCaseNewLinkNum();
					
					// 500筆分線程
					int pageSize = 500;
					int threadSize = 0;
					int listSize = srmCaseNewHandleInfoDTOs.size();
					if(listSize % pageSize == 0){
						threadSize = listSize / pageSize;
					} else {
						threadSize = listSize / pageSize + 1;
					}
					IAtomsAsynchronousHandler<OldDataTransferFormDTO>[] threads = new IAtomsAsynchronousHandler[threadSize];
					List<SrmCaseHandleInfoDTO> tempDtos = null;
					OldDataTransferFormDTO formDTO = null;
					for(int i = 0; i < threadSize; i ++) {
						if (i == threadSize -1) {
							tempDtos = srmCaseNewHandleInfoDTOs.subList(i * pageSize, listSize);
						} else {
							tempDtos = srmCaseNewHandleInfoDTOs.subList(i * pageSize, (i + 1) * pageSize);
						}
						formDTO = new OldDataTransferFormDTO();
						
						formDTO.setSrmCaseHandleInfoDTOs(tempDtos);
						formDTO.setTmsCaseInfoMap(tmsCaseInfoMap);
						formDTO.setAssetCaseInfoListMap(assetCaseInfoListMap);
						formDTO.setCaseAssetLinkMap(caseAssetLinkMap);
						// 分線程處理保存
						threads[i] = new IAtomsAsynchronousHandler<OldDataTransferFormDTO>(formDTO, "oldDataTransferService", "saveNewCaseDataThread");
						threads[i].setServiceLocator(this.getServiceLocator());
						threads[i].start();
					}
					
//					OldDataTransferFormDTO saveFormDTO = null;
					boolean saveFlag = true;
					// 判斷線程轉入結果
					for(IAtomsAsynchronousHandler tempThread : threads){
						try {
							tempThread.join();
							// 判斷線程是否出錯
							if(tempThread.getIsExcuFailure()){
								saveFlag = false;
							}
						} catch (Exception e) {
							saveFlag = false;
						}
					}
					// 若保存成功
					if(saveFlag){
						// 保存成功消息
						SessionContext countCtx = this.serviceLocator.doService(null, "oldDataTransferService", "queryTransferNewCaseNum", null);
						Map<String, Integer> resultMap = (Map<String, Integer>) countCtx.getResponseResult();
						StringBuilder msStringBuilder = new StringBuilder();
						msStringBuilder.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")").append(queryMap.get("resultMsg"));
						msStringBuilder.append("最新案件資料共：" + transferCaseNewInfoNum + "筆，轉入：" + resultMap.get("transferCaseNewInfoNum")+"筆</br>");
						msStringBuilder.append("最新案件鏈接當資料共：" + transferCaseNewLinkNum + "筆，轉入：" + resultMap.get("transferCaseNewLinkNum")+"筆</br>");
						
						returnMap.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
						returnMap.put("resultMsg", msStringBuilder.toString());
						
					//	returnMap.put("resultMsg", "(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + queryMap.get("resultMsg"));
					} else {
						returnMap.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
						returnMap.put("resultMsg", "(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "最新案件資料轉入失敗</br>");
					}
					LOGGER.debug(".transferCaseNewHandleInfo()", "Controller endDate" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH));
					Timestamp endDate = DateTimeUtils.getCurrentTimestamp();
					
					long s = IAtomsDateTimeUtils.dateSubtractionMillis(startDate, endDate);
					try {
						LOGGER.debug(".transferCaseNewHandleInfo()", "Controller totalTime" + (s/60000.0)*100/100);
					} catch (Throwable e) {
					}
				} else {
					returnMap.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					returnMap.put("resultMsg", "(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "最新案件資料轉入失敗</br>" + queryMap.get("resultMsg"));
				}
			} else {
				returnMap.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
				returnMap.put("resultMsg", "(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "最新案件資料轉入失敗</br>");
			}
		//	return new ModelAndView(new MappingJacksonJsonView(), returnMap); 
			
			if(!(Boolean) returnMap.get(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS)){
				// 刪除案件信息
				this.serviceLocator.doService(null, "oldDataTransferService", "deleteNewCaseData", returnMap.get("resultMsg"));
			} else {
				// 保存成功消息
				this.serviceLocator.doService(null, "oldDataTransferService", "saveSuccessLog", returnMap.get("resultMsg").toString());
			}
			
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, returnMap);
			return sessionContext;
		//	return new ModelAndView(this.getSuccessView("transferCaseNewHandleInfo"), IAtomsConstants.PARAM_SESSION_CONTEXT, sessionContext);
		} catch (Exception e) {
			LOGGER.error("transferCaseNewHandleInfo()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:
	 * @author CrissZhang
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 */
	public ModelAndView transferCaseNewHandleInfo(HttpServletRequest request, HttpServletResponse response, OldDataTransferFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = transferCaseNewHandleInfoPart(command);
			return new ModelAndView(this.getSuccessView("transferCaseNewHandleInfo"), IAtomsConstants.PARAM_SESSION_CONTEXT, sessionContext);
		} catch (Exception e) {
			LOGGER.error("transferCaseNewHandleInfo()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * 
	 * Purpose:
	 * @author CrissZhang
	 * @param command
	 * @return
	 * @throws CommonException
	 * @return SessionContext
	 */
	public SessionContext transferHistoryRepositoryPart(OldDataTransferFormDTO command)throws CommonException{
		try {
			LOGGER.debug(".transferHistoryRepository()", "Controller===================================================startDate" + DateTimeUtils.getCurrentTimestamp());
			Timestamp startDate = DateTimeUtils.getCurrentTimestamp();
			SessionContext sessionContext = this.serviceLocator.doService(null, "oldDataTransferService", "transferHistoryRepository", command);
			Integer count = (Integer) sessionContext.getAttribute(IAtomsConstants.COUNT);
			Message msg = sessionContext.getReturnMessage();
			// 返回的map
			Map returnMap = new HashMap();
			// 查詢結果的map集合
			Map queryMap = (Map) sessionContext.getAttribute(IAtomsConstants.PARAM_ACTION_RESULT);
			if(!CollectionUtils.isEmpty(queryMap) && queryMap.containsKey(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS)){
				// 查詢無誤，進行保存
				if((Boolean) queryMap.get(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS)){
					// 得到fotmDTO
					command = (OldDataTransferFormDTO)sessionContext.getResponseResult();
					// 設備歷史集合
					List<DmmRepositoryHistoryDTO> dmmRepositoryHistoryDTOs = command.getDmmRepositoryHistoryDTOs();
					
					LOGGER.debug(".transferHistoryRepository()", "Controller===================================================saveStart" + DateTimeUtils.getCurrentTimestamp());
					Timestamp saveStart = DateTimeUtils.getCurrentTimestamp();
					int pageSize = 3000;
					int threadSize = 0;
					int listSize = dmmRepositoryHistoryDTOs.size();
					if(listSize % pageSize == 0){
						threadSize = listSize / pageSize;
					} else {
						threadSize = listSize / pageSize + 1;
					}
					IAtomsAsynchronousHandler<OldDataTransferFormDTO>[] threads = new IAtomsAsynchronousHandler[threadSize];
					List<DmmRepositoryHistoryDTO> tempDtos = null;
					OldDataTransferFormDTO formDTO = null;
					for(int i = 0; i < threadSize; i ++) {
						if (i == threadSize -1) {
							tempDtos = dmmRepositoryHistoryDTOs.subList(i * pageSize, listSize);
						} else {
							tempDtos = dmmRepositoryHistoryDTOs.subList(i * pageSize, (i + 1) * pageSize);
						}	
						formDTO = new OldDataTransferFormDTO();
						
						formDTO.setDmmRepositoryHistoryDTOs(tempDtos);
						
						threads[i] = new IAtomsAsynchronousHandler<OldDataTransferFormDTO>(formDTO, "oldDataTransferService", "repositoryHistoryDataThread");
						threads[i].setServiceLocator(this.getServiceLocator());
						LOGGER.error(".transferHistoryRepository().transferHistoryRepositoryPart start ");

						threads[i].start();
					}
//					OldDataTransferFormDTO saveFormDTO = null;
					boolean saveFlag = true;
					// 判斷線程轉入結果
					for(IAtomsAsynchronousHandler tempThread : threads){
						try {
							tempThread.join();
							// 判斷線程是否出錯
							if(tempThread.getIsExcuFailure()){
								saveFlag = false;
								LOGGER.debug(".transferHistoryRepository().transferHistoryRepositoryPart false ", DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH));
							}
						} catch (Exception e) {
							LOGGER.error(".transferHistoryRepository().transferHistoryRepositoryPart error ",  DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH));
							saveFlag = false;
						}
					}
					LOGGER.debug(".transferHistoryRepository()", "Controller===================================================endDate" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH));
					// 若保存成功
					if(saveFlag){
						returnMap.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
						SessionContext querySessionContext = this.serviceLocator.doService(null, "oldDataTransferService", "countRepositoryHistData", command);
						Integer queryCount = (Integer) querySessionContext.getAttribute(IAtomsConstants.COUNT);
						if (queryCount.equals(count)) {
							returnMap.put("resultMsg", "(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + queryMap.get("resultMsg")+":共轉入"+count+"筆</br>");
						} else {
							returnMap.put("resultMsg", "(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + queryMap.get("resultMsg")+":共"+count+"筆，轉入"+queryCount+"筆</br>");
						}
					} else {
						//sessionContext = this.serviceLocator.doService(null, "oldDataTransferService", "deleteHistoryRepository", returnMap.get("resultMsg"));
						returnMap.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
						LOGGER.debug("activeCountMain1 : " + Thread.activeCount());  
						returnMap.put("resultMsg", "(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "設備庫存歷史資料轉入失敗</br>");
						LOGGER.error(".transferHistoryRepository().transferHistoryRepositoryPart error 設備庫存歷史資料轉入失敗</br>",  DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH));
					}
					LOGGER.debug(".transferHistoryRepository()", "Controller endDate" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH));
					Timestamp endDate = DateTimeUtils.getCurrentTimestamp();
					LOGGER.debug(returnMap);
					long s = IAtomsDateTimeUtils.dateSubtractionMillis(startDate, endDate);
					try {
						LOGGER.debug(".transferHistoryRepository()", "Controller totalTime" + (s/60000.0)*100/100);
					} catch (Throwable e) {
					}
				} else {
					returnMap.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					LOGGER.error(".transferHistoryRepository().transferHistoryRepositoryPart error </br>",  DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH));
					returnMap.put("resultMsg", queryMap.get("resultMsg"));
				}
			} else {
				returnMap.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
				LOGGER.error(".transferHistoryRepository().transferHistoryRepositoryPart false</br>",  DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH));
				returnMap.put("resultMsg", "設備庫存歷史資料轉入失敗</br>");
			}
			if(!(Boolean) returnMap.get(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS)){
				LOGGER.error("transferHistoryRepository().deleteHistoryRepository() is error then deleteHistoryRepository ");
				// 刪除庫存信息
				this.serviceLocator.doService(null, "oldDataTransferService", "deleteHistoryRepository", returnMap.get("resultMsg"));
			} else {
				// 保存成功消息
				this.serviceLocator.doService(null, "oldDataTransferService", "saveSuccessLog", returnMap.get("resultMsg").toString());
			}
			//return new ModelAndView(new MappingJacksonJsonView(), returnMap); 
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, returnMap);
			return sessionContext;
		//	return new ModelAndView(this.getSuccessView("transferHistoryRepository"), IAtomsConstants.PARAM_SESSION_CONTEXT, sessionContext);
		} catch (Exception e) {
			try {
				LOGGER.error("transferHistoryRepository().deleteHistoryRepository() is error then deleteHistoryRepository ");
				this.serviceLocator.doService(null, "oldDataTransferService", "deleteHistoryRepository", null);
			} catch (Exception e1) {
				LOGGER.error("transferHistoryRepository().deleteHistoryRepository() error ", e.getMessage(), e);
				//e1.printStackTrace();
			}
			LOGGER.error("transferHistoryRepository()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:轉移庫存歷史資料
	 * @author amandawang
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 */
	public ModelAndView transferHistoryRepository(HttpServletRequest request, HttpServletResponse response, OldDataTransferFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = transferHistoryRepositoryPart(command);
			return new ModelAndView(this.getSuccessView("transferHistoryRepository"), IAtomsConstants.PARAM_SESSION_CONTEXT, sessionContext);
		} catch (Exception e) {
			try {
				this.serviceLocator.doService(null, "oldDataTransferService", "deleteHistoryRepository", null);
				LOGGER.error("transferHistoryRepository().deleteHistoryRepository() is error then deleteHistoryRepository transferHistoryRepository()");
			} catch (Exception e1) {
				LOGGER.error("transferHistoryRepository().deleteHistoryRepository() error ", e.getMessage(), e);
				//e1.printStackTrace();
			}
			LOGGER.error("transferHistoryRepository()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * 
	 * Purpose:
	 * @author CarrieDuan
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return void
	 */
	public ModelAndView export(HttpServletRequest request, HttpServletResponse response, OldDataTransferFormDTO command) throws CommonException{
		// 取得sessionId
		String sessionId = request.getSession().getId();
		try{
			if(command != null){
				String caseCategory = command.getCaseCategory();
				IAtomsLogonUser logonUser = (IAtomsLogonUser) command.getLogonUser();
				JasperReportCriteriaDTO criteria = new JasperReportCriteriaDTO();
				//請求service進行數據查詢
				SessionContext returnCtx = this.serviceLocator.doService(logonUser, "oldDataTransferService", "uploadNotClosedCaseInfo", command);
				List<SrmCaseHandleInfoDTO> results = new ArrayList<SrmCaseHandleInfoDTO>();
				List<SrmTransactionParameterItemDTO> result = new ArrayList<SrmTransactionParameterItemDTO>();
				List<String> paramterItemName = new ArrayList<String>();
				if(returnCtx != null){
					command = (OldDataTransferFormDTO) returnCtx.getResponseResult();
					results = command.getSrmCaseHandleInfoDTOs();
					if (IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(caseCategory)
							|| IAtomsConstants.CASE_CATEGORY.UPDATE.getCode().equals(caseCategory)
							|| IAtomsConstants.CASE_CATEGORY.MERGE.getCode().equals(caseCategory)) {
						MultiParameterInquiryContext inquiryContext = new MultiParameterInquiryContext();
						String versionDate = DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDD_DASH);
						inquiryContext.addParameter(SrmTransactionParameterItemDTO.ATTRIBUTE.EFFECTIVE_DATE.getValue(), versionDate);
						returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_GET_TRANSACTION_PARAMETER_ITEM_LIST, inquiryContext);
						if (returnCtx != null) {
							result = (List<SrmTransactionParameterItemDTO>) returnCtx.getResponseResult();
						}
					}
				}
				//封裝報表對象
				// 是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
				criteria.setAutoBuildJasper(false);
				criteria.setResult(results);
				//設置所需報表的Name
				criteria.setJrxmlName(importFilePath.get(caseCategory));
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				//設置報表Name
				criteria.setSheetName(i18NUtil.getName(IAtomsMessageCode.CASE_INFO_MESSAGE));
				
				String fileName = i18NUtil.getName(IAtomsMessageCode.OLD_DATATRANS, new String[]{i18NUtil.getName(caseCategory)}, null);
				criteria.setReportFileName(fileName);
				if (IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(caseCategory)
						|| IAtomsConstants.CASE_CATEGORY.UPDATE.getCode().equals(caseCategory)
						|| IAtomsConstants.CASE_CATEGORY.MERGE.getCode().equals(caseCategory)){
					String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD);
					String path = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH);
					StringBuffer pathBuffer = new StringBuffer();
					pathBuffer.append(path).append(File.separator).append(yearMonthDay).append(File.separator);
					path = pathBuffer.toString();
					ReportExporter.exportReportToFile(criteria, path, null);
					Integer i = 0;
					List<Integer> index = new ArrayList<Integer>();
					for (SrmTransactionParameterItemDTO srmTransactionParameterItemDTO : result) {
						paramterItemName.add(srmTransactionParameterItemDTO.getParamterItemName());
						if ("combobox".equals(srmTransactionParameterItemDTO.getParamterItemType())) {
							index.add(i);
						}
						i++;
					}
					StringBuffer filePathBuffer = new StringBuffer();
					filePathBuffer.append(path).append(fileName).append(".xls");
					POIUtils.createExcel(filePathBuffer.toString(), paramterItemName, index, caseCategory, 2);
					InputStream inputStream2 = new FileInputStream(new File(filePathBuffer.toString()));
					FileUtils.download(request, response, inputStream2, fileName+".xls");
				} else {
					ReportExporter.exportReport(criteria, response);
				}
				// 成功標志
				SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, IAtomsConstants.YES);
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
			LOGGER.error(".export() is error: ", e);
		}
		return null;
	}
	/**
	 * Purpose:批次轉入
	 * @author CrissZhang
	 * @param request
	 * @param response
	 * @param command
	 * @throws CommonException
	 * @return ModelAndView
	 */
	public ModelAndView selectTransfer(HttpServletRequest request, HttpServletResponse response, OldDataTransferFormDTO command)throws CommonException{
		try {
			boolean isStartTransfer = false;
			boolean isTransferOk = true;
			StringBuilder resultBuilder = new StringBuilder();
			SessionContext returnContext = null;
			// 返回的map
			Map returnMap = null;
			this.serviceLocator.doService(null, "oldDataTransferService", "saveSuccessLog", "(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "開始批量轉入。。。</br>");
			if(command.getIsTransferCalendar()){
				long startTime = System.currentTimeMillis();
				isStartTransfer = true;
				if(isStartTransfer && isTransferOk){
					command.setLogMessage("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "行事曆資料開始轉入。。。");
					returnContext = this.serviceLocator.doService(null, "oldDataTransferService", "transferCalendar", command);
					returnMap = (Map) returnContext.getAttribute(IAtomsConstants.PARAM_ACTION_RESULT);
					isTransferOk = (Boolean) returnMap.get(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS);
					resultBuilder.append(command.getLogMessage() + "</br>" + returnMap.get("resultMsg"));
					/*if(returnMap.get("resultMsg") == null){
						returnMap.put("resultMsg", "");
					}
					this.serviceLocator.doService(null, "oldDataTransferService", "saveSuccessLog", returnMap.get("resultMsg").toString());*/
				} else {
					resultBuilder.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "行事曆資料轉入失敗").append("</br>");
				}
				
				
				long endTime = System.currentTimeMillis();
				LOGGER.debug("selectTransfer()", "testTransferTime transferCalendar:行事曆 totalTime:" + (endTime - startTime));
				
				
			}
			if(command.getIsTransferFaultParamData()){
				long startTime = System.currentTimeMillis();
				isStartTransfer = true;
				if(isStartTransfer && isTransferOk){
					command.setLogMessage("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "故障參數資料開始轉入。。。");
					returnContext = this.serviceLocator.doService(null, "oldDataTransferService", "transferFaultParamData", command);
					returnMap = (Map) returnContext.getAttribute(IAtomsConstants.PARAM_ACTION_RESULT);
					isTransferOk = (Boolean) returnMap.get(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS);
					resultBuilder.append(command.getLogMessage() + "</br>" + returnMap.get("resultMsg"));
					/*if(returnMap.get("resultMsg") == null){
						returnMap.put("resultMsg", "");
					}
					this.serviceLocator.doService(null, "oldDataTransferService", "saveSuccessLog", returnMap.get("resultMsg").toString());*/
				} else {
					resultBuilder.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "故障參數資料轉入失敗").append("</br>");
				}
				
				long endTime = System.currentTimeMillis();
				LOGGER.debug("selectTransfer()", "testTransferTime transferFaultParamData:故障參數 totalTime:" + (endTime - startTime));
				
			}
			if(command.getIsTransferCompanyData()){
				long startTime = System.currentTimeMillis();
				isStartTransfer = true;
				if(isStartTransfer && isTransferOk){
					command.setLogMessage("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "公司資料開始轉入。。。");
					returnContext = this.serviceLocator.doService(null, "oldDataTransferService", "transferCompanyData", command);
					returnMap = (Map) returnContext.getAttribute(IAtomsConstants.PARAM_ACTION_RESULT);
					isTransferOk = (Boolean) returnMap.get(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS);
					resultBuilder.append(command.getLogMessage() + "</br>" + returnMap.get("resultMsg"));
					/*if(returnMap.get("resultMsg") == null){
						returnMap.put("resultMsg", "");
					}
					this.serviceLocator.doService(null, "oldDataTransferService", "saveSuccessLog", returnMap.get("resultMsg").toString());*/
				} else {
					resultBuilder.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "公司資料轉入失敗").append("</br>");
				}
				
				long endTime = System.currentTimeMillis();
				LOGGER.debug("selectTransfer()", "testTransferTime transferCompanyData:公司 totalTime:" + (endTime - startTime));
				
			}
			if(command.getIsTransferWarehouse()){
				long startTime = System.currentTimeMillis();
				isStartTransfer = true;
				if(isStartTransfer && isTransferOk){
					command.setLogMessage("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "倉庫據點資料開始轉入。。。");
					returnContext = this.serviceLocator.doService(null, "oldDataTransferService", "transferWarehouse", command);
					returnMap = (Map) returnContext.getAttribute(IAtomsConstants.PARAM_ACTION_RESULT);
					isTransferOk = (Boolean) returnMap.get(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS);
					resultBuilder.append(command.getLogMessage() + "</br>" + returnMap.get("resultMsg"));
					/*if(returnMap.get("resultMsg") == null){
						returnMap.put("resultMsg", "");
					}
					this.serviceLocator.doService(null, "oldDataTransferService", "saveSuccessLog", returnMap.get("resultMsg").toString());*/
				} else {
					resultBuilder.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "倉庫據點資料轉入失敗").append("</br>");
				}

				long endTime = System.currentTimeMillis();
				LOGGER.debug("selectTransfer()", "testTransferTime transferWarehouse:倉庫據點 totalTime:" + (endTime - startTime));
				
			}
			if(command.getIsTransferAssetType()){
				long startTime = System.currentTimeMillis();
				isStartTransfer = true;
				if(isStartTransfer && isTransferOk){
					command.setLogMessage("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "設備品項資料開始轉入。。。");
					returnContext = this.serviceLocator.doService(null, "oldDataTransferService", "transferAssetType", command);
					returnMap = (Map) returnContext.getAttribute(IAtomsConstants.PARAM_ACTION_RESULT);
					isTransferOk = (Boolean) returnMap.get(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS);
					resultBuilder.append(command.getLogMessage() + "</br>" + returnMap.get("resultMsg"));
					/*if(returnMap.get("resultMsg") == null){
						returnMap.put("resultMsg", "");
					}
					this.serviceLocator.doService(null, "oldDataTransferService", "saveSuccessLog", returnMap.get("resultMsg").toString());*/
				} else {
					resultBuilder.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "設備品項資料轉入失敗").append("</br>");
				}

				long endTime = System.currentTimeMillis();
				LOGGER.debug("selectTransfer()", "testTransferTime transferAssetType:設備品項 totalTime:" + (endTime - startTime));
				
			}
			if(command.getIsTransferApplicaton()){
				long startTime = System.currentTimeMillis();
				isStartTransfer = true;
				if(isStartTransfer && isTransferOk){
					command.setLogMessage("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "程式版本資料開始轉入。。。");
					returnContext = this.serviceLocator.doService(null, "oldDataTransferService", "transferApplicaton", command);
					returnMap = (Map) returnContext.getAttribute(IAtomsConstants.PARAM_ACTION_RESULT);
					isTransferOk = (Boolean) returnMap.get(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS);
					resultBuilder.append(command.getLogMessage() + "</br>" + returnMap.get("resultMsg"));
					/*if(returnMap.get("resultMsg") == null){
						returnMap.put("resultMsg", "");
					}
					this.serviceLocator.doService(null, "oldDataTransferService", "saveSuccessLog", returnMap.get("resultMsg").toString());*/
				} else {
					resultBuilder.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "程式版本資料轉入失敗").append("</br>");
				}

				long endTime = System.currentTimeMillis();
				LOGGER.debug("selectTransfer()", "testTransferTime transferApplicaton:程式版本 totalTime:" + (endTime - startTime));
				
			}
			if(command.getIsTransferMerchant()){
				long startTime = System.currentTimeMillis();
				isStartTransfer = true;
				if(isStartTransfer && isTransferOk){
					command.setLogMessage("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "客戶特店、表頭資料開始轉入。。。");
					returnContext = this.serviceLocator.doService(null, "oldDataTransferService", "transferMerchant", command);
					returnMap = (Map) returnContext.getAttribute(IAtomsConstants.PARAM_ACTION_RESULT);
					isTransferOk = (Boolean) returnMap.get(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS);
					resultBuilder.append(command.getLogMessage() + "</br>" + returnMap.get("resultMsg"));
					/*if(returnMap.get("resultMsg") == null){
						returnMap.put("resultMsg", "");
					}
					this.serviceLocator.doService(null, "oldDataTransferService", "saveSuccessLog", returnMap.get("resultMsg").toString());*/
				} else {
					resultBuilder.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "客戶特店、表頭資料轉入失敗").append("</br>");
				}

				long endTime = System.currentTimeMillis();
				LOGGER.debug("selectTransfer()", "testTransferTime transferMerchant:客戶特店、表頭 totalTime:" + (endTime - startTime));
				
			}
			if(command.getIsTransferContract()){
				long startTime = System.currentTimeMillis();
				isStartTransfer = true;
				if(isStartTransfer && isTransferOk){
					command.setLogMessage("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "合約資料開始轉入。。。");
					returnContext = this.serviceLocator.doService(null, "oldDataTransferService", "transferContract", command);
					returnMap = (Map) returnContext.getAttribute(IAtomsConstants.PARAM_ACTION_RESULT);
					isTransferOk = (Boolean) returnMap.get(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS);
					resultBuilder.append(command.getLogMessage() + "</br>" + returnMap.get("resultMsg"));
					/*if(returnMap.get("resultMsg") == null){
						returnMap.put("resultMsg", "");
					}
					this.serviceLocator.doService(null, "oldDataTransferService", "saveSuccessLog", returnMap.get("resultMsg").toString());*/
				} else {
					resultBuilder.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "合約資料轉入失敗").append("</br>");
				}

				long endTime = System.currentTimeMillis();
				LOGGER.debug("selectTransfer()", "testTransferTime transferContract:合約 totalTime:" + (endTime - startTime));
				
			}
			if(command.getIsTransferAdmUser()){
				long startTime = System.currentTimeMillis();
				isStartTransfer = true;
				if(isStartTransfer && isTransferOk){
					command.setLogMessage("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "使用者帳號資料開始轉入。。。");
					returnContext = this.serviceLocator.doService(null, "oldDataTransferService", "transferAdmUser", command);
					returnMap = (Map) returnContext.getAttribute(IAtomsConstants.PARAM_ACTION_RESULT);
					isTransferOk = (Boolean) returnMap.get(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS);
					resultBuilder.append(command.getLogMessage() + "</br>" + returnMap.get("resultMsg"));
					/*if(returnMap.get("resultMsg") == null){
						returnMap.put("resultMsg", "");
					}
					this.serviceLocator.doService(null, "oldDataTransferService", "saveSuccessLog", returnMap.get("resultMsg").toString());*/
				} else {
					resultBuilder.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "使用者帳號資料轉入失敗").append("</br>");
				}

				long endTime = System.currentTimeMillis();
				LOGGER.debug("selectTransfer()", "testTransferTime transferAdmUser:使用者 totalTime:" + (endTime - startTime));
				
			}
			if(command.getIsTransferRepository()){
				long startTime = System.currentTimeMillis();
				isStartTransfer = true;
				if(isStartTransfer && isTransferOk){
					command.setLogMessage("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "設備庫存資料開始轉入。。。");
					returnContext = this.serviceLocator.doService(null, "oldDataTransferService", "transferRepository", command);
					returnMap = (Map) returnContext.getAttribute(IAtomsConstants.PARAM_ACTION_RESULT);
					isTransferOk = (Boolean) returnMap.get(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS);
					resultBuilder.append(command.getLogMessage() + "</br>" + returnMap.get("resultMsg"));
/*					if(returnMap.get("resultMsg") == null){
						returnMap.put("resultMsg", "");
					}
					this.serviceLocator.doService(null, "oldDataTransferService", "saveSuccessLog", returnMap.get("resultMsg").toString());*/
				} else {
					resultBuilder.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "設備庫存資料轉入失敗").append("</br>");
				}

				long endTime = System.currentTimeMillis();
				LOGGER.debug("selectTransfer()", "testTransferTime transferRepository:設備庫存 totalTime:" + (endTime - startTime));
				
			}
			if(command.getIsTransferHistoryRepository()){
				long startTime = System.currentTimeMillis();
				isStartTransfer = true;
				if(isStartTransfer && isTransferOk){
					command.setLogMessage("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "設備庫存歷史開始轉入。。。");
				//	returnContext = this.serviceLocator.doService(null, "oldDataTransferService", "transferHistoryRepository", command);
					returnContext = this.transferHistoryRepositoryPart(command);
					returnMap = (Map) returnContext.getAttribute(IAtomsConstants.PARAM_ACTION_RESULT);
					isTransferOk = (Boolean) returnMap.get(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS);
					resultBuilder.append(command.getLogMessage() + "</br>" + returnMap.get("resultMsg"));
					/*if(returnMap.get("resultMsg") == null){
						returnMap.put("resultMsg", "");
					}
					this.serviceLocator.doService(null, "oldDataTransferService", "saveSuccessLog", returnMap.get("resultMsg").toString());*/
				} else {
					resultBuilder.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "設備庫存歷史資料轉入失敗").append("</br>");
				}

				long endTime = System.currentTimeMillis();
				LOGGER.debug("selectTransfer()", "testTransferTime transferHistoryRepository:設備庫存歷史 totalTime:" + (endTime - startTime));
				
			}
			if(command.getIsTransferCaseHandleInfo()){
				long startTime = System.currentTimeMillis();
				isStartTransfer = true;
				if(isStartTransfer && isTransferOk){
					command.setLogMessage("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "案件處理資料開始轉入。。。");
				//	returnContext = this.serviceLocator.doService(null, "oldDataTransferService", "transferCaseHandleInfo", command);
					returnContext = this.transferCaseHandleInfoPart(command);
					returnMap = (Map) returnContext.getAttribute(IAtomsConstants.PARAM_ACTION_RESULT);
					isTransferOk = (Boolean) returnMap.get(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS);
					resultBuilder.append(command.getLogMessage() + "</br>" + returnMap.get("resultMsg"));
					/*if(returnMap.get("resultMsg") == null){
						returnMap.put("resultMsg", "");
					}
					this.serviceLocator.doService(null, "oldDataTransferService", "saveSuccessLog", returnMap.get("resultMsg").toString());*/
				} else {
					resultBuilder.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "處理中案件資料轉入失敗").append("</br>");
				}

				long endTime = System.currentTimeMillis();
				LOGGER.debug("selectTransfer()", "testTransferTime transferCaseHandleInfo:案件處理資料 totalTime:" + (endTime - startTime));
				
			}
			if(command.getIsTransferCaseNewHandleInfo()){
				long startTime = System.currentTimeMillis();
				isStartTransfer = true;
				if(isStartTransfer && isTransferOk){
					command.setLogMessage("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "案件最新處理資料開始轉入。。。");
				//	returnContext = this.serviceLocator.doService(null, "oldDataTransferService", "transferCaseNewHandleInfo", command);
					returnContext = this.transferCaseNewHandleInfoPart(command);
					returnMap = (Map) returnContext.getAttribute(IAtomsConstants.PARAM_ACTION_RESULT);
					isTransferOk = (Boolean) returnMap.get(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS);
					resultBuilder.append(command.getLogMessage() + "</br>" + returnMap.get("resultMsg"));
					/*if(returnMap.get("resultMsg") == null){
						returnMap.put("resultMsg", "");
					}
					this.serviceLocator.doService(null, "oldDataTransferService", "saveSuccessLog", returnMap.get("resultMsg").toString());*/
				} else {
					resultBuilder.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "案件最新處理資料轉入失敗").append("</br>");
				}

				long endTime = System.currentTimeMillis();
				LOGGER.debug("selectTransfer()", "testTransferTime transferCaseNewHandleInfo:案件最新處理 totalTime:" + (endTime - startTime));
				
			}
			if(command.getIsTransferProblemData()){
				long startTime = System.currentTimeMillis();
				isStartTransfer = true;
				if(isStartTransfer && isTransferOk){
					command.setLogMessage("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "報修參數資料開始轉入。。。");
					returnContext = this.serviceLocator.doService(null, "oldDataTransferService", "transferProblemData", command);
					returnMap = (Map) returnContext.getAttribute(IAtomsConstants.PARAM_ACTION_RESULT);
					isTransferOk = (Boolean) returnMap.get(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS);
					resultBuilder.append(command.getLogMessage() + "</br>" + returnMap.get("resultMsg"));
					/*if(returnMap.get("resultMsg") == null){
						returnMap.put("resultMsg", "");
					}
					this.serviceLocator.doService(null, "oldDataTransferService", "saveSuccessLog", returnMap.get("resultMsg").toString());*/
				} else {
					resultBuilder.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "報修參數資料轉入失敗").append("</br>");
				}

				long endTime = System.currentTimeMillis();
				LOGGER.debug("selectTransfer()", "testTransferTime transferProblemData:報修參數 totalTime:" + (endTime - startTime));
				
			}
			resultBuilder.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "批量轉入完成。。。").append("</br>");
			this.serviceLocator.doService(null, "oldDataTransferService", "saveSuccessLog", "(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "批量轉入完成。。。</br>");
			returnMap.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, isTransferOk);
			returnMap.put("resultMsg", resultBuilder.toString());
			returnContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, returnMap);
			return new ModelAndView(this.getSuccessView("selectTransfer"), IAtomsConstants.PARAM_SESSION_CONTEXT, returnContext);
		} catch (Exception e) {
			LOGGER.error("transferCaseHandleInfo()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * @return the importFilePath
	 */
	public Map<String, String> getImportFilePath() {
		return importFilePath;
	}

	/**
	 * @param importFilePath the importFilePath to set
	 */
	public void setImportFilePath(Map<String, String> importFilePath) {
		this.importFilePath = importFilePath;
	}
	
}
