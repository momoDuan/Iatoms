package com.cybersoft4u.xian.iatoms.web.controllers;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;

import cafe.core.config.GenericConfigManager;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.exception.CommonException;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.SessionHelper;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseNewTransactionParameterDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmTransactionParameterItemDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.EdcParameterFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.CrossTabReportDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;
import com.cybersoft4u.xian.iatoms.common.utils.ReportExporter;
import com.google.gson.Gson;

/**
 * Purpose: EDC交易參數查詢
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016/9/22
 * @MaintenancePersonnel CrissZhang
 */
public class EdcParameterController extends AbstractMultiActionController<EdcParameterFormDTO>{

	/**
	 * 系统日志记录工具
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.CONTROLLER, EdcParameterController.class);
	/**
	 * Constructor:無參構造函數
	 */
	public EdcParameterController(){
		this.setCommandClass(EdcParameterFormDTO.class);
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(EdcParameterFormDTO parmemters) throws CommonException {
		return true;
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public EdcParameterFormDTO parse(HttpServletRequest request, EdcParameterFormDTO command) throws CommonException {
		try {
			// 得到actionId
			String actionId = command.getActionId();
			if(!StringUtils.hasText(actionId)){
				actionId = IAtomsConstants.ACTION_INIT;
			}
		} catch (Exception e) {
			LOGGER.error("parse()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
		return command;
	}

	/**
	 * Purpose:export
	 * @author CrissZhang
	 * @param request:HttpServletRequest
	 * @param response:HttpServletResponse
	 * @param command :EdcParameterFormDTO
	 * @throws CommonException:發生錯誤時, 丟出Exception
	 * @return void
	 */
	public ModelAndView export(HttpServletRequest request, HttpServletResponse response, EdcParameterFormDTO command) throws CommonException {
		// 取得sessionId
		String sessionId = request.getSession().getId();
		try {
			SessionContext ctx = this.getServiceLocator().doService(command.getLogonUser(), this.getServiceId(), IAtomsConstants.ACTION_EXPORT, command);
			// 得到交易參數可編輯的字段
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			String versionDate = DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDD_DASH);
			param.addParameter(SrmTransactionParameterItemDTO.ATTRIBUTE.EFFECTIVE_DATE.getValue(), versionDate);
			SessionContext returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE,
					IAtomsConstants.ACTION_GET_EDIT_FIELDS_GROUP_BY_TRANSACTION_TYPE, param);
			// 可編輯字段的map集合
			Map<String,List<String>> editFildsMap = (Map<String,List<String>>) returnCtx.getResponseResult();
			command = (EdcParameterFormDTO) ctx.getResponseResult();
			// 案件處理集合
			List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOs = command.getList();
			// 案件交易參數配置檔
			List<SrmTransactionParameterItemDTO> srmTransactionParameterItemDTOs = null;
			// 案件最新交易參數
			List<SrmCaseNewTransactionParameterDTO> caseNewTransactionParameterDTOs = null;
			// 報表所需參數map集合
			Map map = new HashMap();
			JasperReportCriteriaDTO criteria = new JasperReportCriteriaDTO();
			criteria.setParameters(map);
			// 交叉報表對象集合
			List<CrossTabReportDTO> crossTabReportDTOs = null;
			// 交叉報表對象
			CrossTabReportDTO tempCrossTabReportDTO = null;
			// 交叉報表放置參數的全部結果集合
			Map<String, Map<String, String>> resultMap = null;
			// 交叉報表放置參數的臨時單個結果集合
			Map<String, String> tempMap = null;
			Gson gson = new Gson();
			// 存放交易參數轉換後的map對象
			Map<String, Object> jsonMap = null;
			// 交易參數可編輯字段
			String tempEditFilds = null;
			StringBuilder builder = new StringBuilder();
			if (!CollectionUtils.isEmpty(srmCaseHandleInfoDTOs)){
				// 給每筆案件增加交易參數
				for(SrmCaseHandleInfoDTO srmCaseHandleInfoDTO : srmCaseHandleInfoDTOs){
					// 得到配置檔
					srmTransactionParameterItemDTOs = srmCaseHandleInfoDTO.getSrmTransactionParameterItemDTOs();
					// 得到這筆案件之交易參數
					caseNewTransactionParameterDTOs = srmCaseHandleInfoDTO.getCaseNewTransactionParameterDTOs();
					resultMap = new LinkedHashMap<String, Map<String,String>>();
					if(!CollectionUtils.isEmpty(caseNewTransactionParameterDTOs)){
						// 每筆案件交易參數集合
						crossTabReportDTOs = new LinkedList<CrossTabReportDTO>();
						int j = 0;
						for(SrmCaseNewTransactionParameterDTO caseNewTransactionParameterDTO : caseNewTransactionParameterDTOs){
							j ++;
							// 得到每項交易參數可編輯字段
							tempEditFilds = editFildsMap.get(caseNewTransactionParameterDTO.getTransactionType()).toString();
							if(StringUtils.hasText(caseNewTransactionParameterDTO.getItemValue())){
								jsonMap = new HashMap<String, Object>();
								// 將item_value中交易參數轉爲map集合對象
								jsonMap = gson.fromJson(caseNewTransactionParameterDTO.getItemValue(), jsonMap.getClass());
							}
							tempMap = new LinkedHashMap<String, String>();
							for(SrmTransactionParameterItemDTO srmTransactionParameterItemDTO : srmTransactionParameterItemDTOs){
								if(StringUtils.hasText(tempEditFilds)){
									// 放置特店代號
									if(srmTransactionParameterItemDTO.getParamterItemCode().equals(IAtomsConstants.PARAMTER_ITEM_CODE_MERCHANT_CODE)){
										if(tempEditFilds.contains(srmTransactionParameterItemDTO.getParamterItemCode())){
											tempMap.put(srmTransactionParameterItemDTO.getParamterItemName(), caseNewTransactionParameterDTO.getMerchantCode());
										} else {
											tempMap.put(srmTransactionParameterItemDTO.getParamterItemName(), IAtomsConstants.MARK_SPACE);
										}
										// 放置分期特店代號
									} else if(srmTransactionParameterItemDTO.getParamterItemCode().equals(IAtomsConstants.PARAMTER_ITEM_CODE_MERCHANT_CODE_OTHER)){
										if(tempEditFilds.contains(srmTransactionParameterItemDTO.getParamterItemCode())){
											tempMap.put(srmTransactionParameterItemDTO.getParamterItemName(), caseNewTransactionParameterDTO.getMerchantCodeOther());
										} else {
											tempMap.put(srmTransactionParameterItemDTO.getParamterItemName(), IAtomsConstants.MARK_SPACE);
										}
										// 放置tid
									} else if(srmTransactionParameterItemDTO.getParamterItemCode().equals(IAtomsConstants.PARAMTER_ITEM_CODE_TID)){
										if(tempEditFilds.contains(srmTransactionParameterItemDTO.getParamterItemCode())){
											tempMap.put(srmTransactionParameterItemDTO.getParamterItemName(), caseNewTransactionParameterDTO.getTid());
										} else {
											tempMap.put(srmTransactionParameterItemDTO.getParamterItemName(), IAtomsConstants.MARK_SPACE);
										}
									} else {
										// 處理可編輯的字段
										if(tempEditFilds.contains(srmTransactionParameterItemDTO.getParamterItemCode())){
											// 編輯字段存與item_value
											if(!CollectionUtils.isEmpty(jsonMap)){
												if(jsonMap.containsKey(srmTransactionParameterItemDTO.getParamterItemCode())){
													tempMap.put(srmTransactionParameterItemDTO.getParamterItemName(), (String) jsonMap.get(srmTransactionParameterItemDTO.getParamterItemCode()));
												} else {
													tempMap.put(srmTransactionParameterItemDTO.getParamterItemName(), IAtomsConstants.MARK_EMPTY_STRING);
												}
											// 支持该项但未使用
											} else {
												tempMap.put(srmTransactionParameterItemDTO.getParamterItemName(), IAtomsConstants.MARK_EMPTY_STRING);
											}
										// 處理禁用字段
										} else {
											tempMap.put(srmTransactionParameterItemDTO.getParamterItemName(), IAtomsConstants.MARK_SPACE);
										}	
									}
								// 直接继续下一次
								} else {
									continue;
								}
							}
							// 將交易項對應結果存入map
							resultMap.put(StringUtils.toFixString(3, j) + caseNewTransactionParameterDTO.getTransactionTypeName(), tempMap);
						}
					}
					// 處理結果map
					if(!CollectionUtils.isEmpty(resultMap)){
						tempMap = new LinkedHashMap<String, String>();
						int i;
						// 遍歷結果集合
						for(String resultMapKey : resultMap.keySet()){
							i = 0;
							tempMap = resultMap.get(resultMapKey);
							for(String tempMapKey : tempMap.keySet()){
								builder.delete(0, builder.length());
								i ++;
								tempCrossTabReportDTO = new CrossTabReportDTO();
								// 放置行名稱
								tempCrossTabReportDTO.setRowName(resultMapKey);
								// 放置列名稱
							//	builder.append(k).append(j).append(i).append(tempMapKey);
								builder.append(StringUtils.toFixString(3, i)).append(tempMapKey);
								tempCrossTabReportDTO.setColumnName(builder.toString());
								// 放置列對應值
								tempCrossTabReportDTO.setContent(tempMap.get(tempMapKey));
								crossTabReportDTOs.add(tempCrossTabReportDTO);
							}
						}
						// 放置交叉報表集合
						srmCaseHandleInfoDTO.setCrossTabReportDTOs(crossTabReportDTOs);
					}
				}
				// 是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
				criteria.setAutoBuildJasper(true);
				//子報表的map
				Map<String, String> subjrXmlNames = new HashMap<String, String>();
				subjrXmlNames.put(EdcParameterFormDTO.PROJECT_SUB_REPORT_JRXML_NAME, EdcParameterFormDTO.SUBREPORT_DIR);
				criteria.setResult(srmCaseHandleInfoDTOs);
				//設置所需報表的Name
				criteria.setJrxmlName(EdcParameterFormDTO.PROJECT_REPORT_JRXML_NAME);
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				//設置報表Name
				criteria.setReportFileName(EdcParameterFormDTO.PROJECT_REPORT_FILE_NAME);
				//設置sheet名稱
				criteria.setSheetName(EdcParameterFormDTO.PROJECT_REPORT_FILE_NAME);
				ReportExporter.exportMainAndSubReport(criteria, subjrXmlNames, response);
				// 成功標志
				SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, IAtomsConstants.YES);
			}
		} catch (ServiceException e) {
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
			LOGGER.error("export()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.EXPORT_REPORT_FAILURE, e);
		} catch (Exception e) {
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
			LOGGER.error("export()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return null;
	}
}
