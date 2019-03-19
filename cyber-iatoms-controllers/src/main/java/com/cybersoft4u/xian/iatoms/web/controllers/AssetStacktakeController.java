package com.cybersoft4u.xian.iatoms.web.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import cafe.core.bean.Message;
import cafe.core.context.SessionContext;
import cafe.core.exception.CommonException;
import cafe.core.service.ServiceException;
import cafe.core.util.i18NUtil;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.BindPageDataUtils;
import cafe.core.web.controller.util.SessionHelper;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetStacktakeInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetStacktakeListDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetStacktakeFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.CrossTabReportDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;
import com.cybersoft4u.xian.iatoms.common.utils.ReportExporter;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * Purpose：設備盤點功能Controller 
 * @author echomou
 * @since  JDK 1.7
 * @date   2016-7-15
 * @MaintenancePersonnel echomou
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AssetStacktakeController extends AbstractMultiActionController<AssetStacktakeFormDTO> {
	/**
	 * 日志记录物件
	 */
	private static final Log LOGGER = LogFactory.getLog(AssetStacktakeController.class);
	
	/**
	 * Constructor:无参构造函数
	 */
	public AssetStacktakeController() {
		super();
		this.setCommandClass(AssetStacktakeFormDTO.class);
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(AssetStacktakeFormDTO command) throws CommonException {
		if (command == null) {
			return false;
		}
		String actionId = command.getActionId();
		Message msg = null;
		if (IAtomsConstants.ACTION_SAVE.equals(actionId)) {
			//檢驗頁面輸入
			DmmAssetStacktakeInfoDTO assetStacktakeInfoDTO = command.getAssetStacktakeInfoDTO();
			if (assetStacktakeInfoDTO == null) {
				return false;
			}
			String houseName = assetStacktakeInfoDTO.getWarWarehouseId();
			String assetName = assetStacktakeInfoDTO.getAssetTypeId();
			String assetStatus = assetStacktakeInfoDTO.getAssetStatus();
			String remark = assetStacktakeInfoDTO.getRemark();
			//檢核倉庫名稱
			if (!StringUtils.hasText(houseName)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_STOCKTAKE_HOUSE_NAME)});
				throw new CommonException(msg);
			} 
			//檢核設備名稱
			if (!StringUtils.hasText(assetName)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_STOCKTAKE_ASSET_NAME)});
				throw new CommonException(msg);
			}
			//檢核設備狀態
			if (!StringUtils.hasText(assetStatus)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_STOCKTAKE_ASSET_STATUS)});
				throw new CommonException(msg);
			}
			//檢核說明
			if (StringUtils.hasText(remark)) {
				if (!ValidateUtils.length(remark, 0, 200)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_STOCKTAKE_REMARK),
							IAtomsConstants.MAXLENGTH_NUMBER_TWO_HUNDRED});
					throw new CommonException(msg);
				}
			}
		}
		if (IAtomsConstants.ACTION_SEND.equals(actionId)) {
			String stocktakeId = command.getQueryStocktackId();
			String serialNumber = command.getSendSerialNumber();
			//檢核盤點序號
			if (!StringUtils.hasText(stocktakeId)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_STOCKTAKE_ID)});
				throw new CommonException(msg);
			}
			//檢核設備序號/財編
			if (!StringUtils.hasText(serialNumber)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_STOCKTAKE_ASSET_SERIAL)});
				throw new CommonException(msg);
			} 
		}
		return true;
	}

	/**	
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public AssetStacktakeFormDTO parse(HttpServletRequest request, AssetStacktakeFormDTO command) throws CommonException {
		try {
			String actionId = command.getActionId();
			String assetListRow = null;
			if ((IAtomsConstants.ACTION_SAVE).equals(actionId)) {
				//綁定DeviceInventoryDTO
				DmmAssetStacktakeInfoDTO assetStacktakeInfoDTO = BindPageDataUtils.bindValueObject(request, DmmAssetStacktakeInfoDTO.class);
				command.setAssetStacktakeInfoDTO(assetStacktakeInfoDTO);
			} else if ((AssetStacktakeFormDTO.SAVE_REMARK).equals(actionId)) {
				// 獲取轉倉清單中需要儲存的JSON數據
				assetListRow = command.getAssetStocktackList();
				Gson gson = new GsonBuilder().create();
				List<DmmAssetStacktakeListDTO> assetListDTOList = gson.fromJson(assetListRow, new TypeToken<List<DmmAssetStacktakeListDTO>>(){}.getType());
				command.setAssetStocktackListDTOs(assetListDTOList);
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".parse() exception ", e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
		return command;
	}
	/**
	 * 
	 * Purpose:匯出設備盤點列印清冊報表
	 * @author echomou
	 * @param request:HttpServletRequest
	 * @param response:HttpServletResponse
	 * @param formDTO:DeviceInventoryFormDTO
	 * @throws CommonException:出錯時拋出CommonException
	 * @return void
	 */
	public ModelAndView exportInventory(HttpServletRequest request, HttpServletResponse response, AssetStacktakeFormDTO formDTO) throws CommonException{
		// 取得sessionId
		String sessionId = request.getSession().getId();
		try {
			List<JasperReportCriteriaDTO> criterias = new ArrayList<JasperReportCriteriaDTO>();
			SessionContext ctx = this.getServiceLocator().doService(formDTO.getLogonUser(), this.getServiceId(), AssetStacktakeFormDTO.EXPORT_INVENTORY, formDTO);
			List<DmmAssetStacktakeListDTO> results = null;
			if(ctx != null){
				formDTO = (AssetStacktakeFormDTO) ctx.getResponseResult();
				results = formDTO.getAssetStocktackListDTOs();
			}
			if (!CollectionUtils.isEmpty(results)) {
			    JasperReportCriteriaDTO criteria = new JasperReportCriteriaDTO();
			    //是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
			    criteria.setAutoBuildJasper(false);
			    //设置结果集
			    List<DmmAssetStacktakeListDTO> resultsOne = new ArrayList<DmmAssetStacktakeListDTO>();
			    resultsOne.add(results.get(0));
			    criteria.setResult(resultsOne);
			    //设置所需报表的名称
			    criteria.setJrxmlName(AssetStacktakeFormDTO.PROJECT_REPORT_JRXML_INVENTORY_NAME);
			    //设置报表路径
			    criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
			    criteria.setSheetName(AssetStacktakeFormDTO.PROJECT_REPORT_SHEET_NAME);
			    //设置汇出格式
			    criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
			    //设置报表名称
			    criteria.setReportFileName(AssetStacktakeFormDTO.PROJECT_REPORT_FILE_INVENTORY_NAME);
			    criterias.add(criteria);
			    //存放机型集合
			    List<String> liStrings = new ArrayList<String>();
			    for (int i = 0; i < results.size(); i++) {
			    	liStrings.add(results.get(i).getStocktackId());
			    }
				CrossTabReportDTO crossTabReportDTO = null;
				List<CrossTabReportDTO> crossTabReportDTOList = null;
				if (!CollectionUtils.isEmpty(liStrings)) {
					String tempSeialNumber = "";
					String[] tempSeialNumberListStrings = null;
					for (int k = 0; k < liStrings.size(); k++) {
						crossTabReportDTOList = new ArrayList<CrossTabReportDTO>();
						tempSeialNumber = results.get(k).getSerialNumberList();
						tempSeialNumberListStrings = tempSeialNumber.split(IAtomsConstants.MARK_SEPARATOR);
						for (int i = 0; i < tempSeialNumberListStrings.length; i++) {
							crossTabReportDTO = new CrossTabReportDTO();
							crossTabReportDTO.setAssetTypeStatus(results.get(k).getAssetStatusName());
							crossTabReportDTO.setContent(tempSeialNumberListStrings[i]);
							crossTabReportDTO.setRowNo(i + 1);
							crossTabReportDTOList.add(crossTabReportDTO);
						}
						while (crossTabReportDTOList.size()%4 != 0) {
							crossTabReportDTO = new CrossTabReportDTO();
							crossTabReportDTO.setContent("");
							crossTabReportDTO.setRowNo(crossTabReportDTOList.size()+1);
							crossTabReportDTOList.add(crossTabReportDTO);
						}
						Map map = new HashMap();
						map.put("assetTypeName", results.get(k).getAssetTypeName());
						map.put("assetTypeStatus", results.get(k).getAssetStatusName());
						map.put("count", Integer.valueOf(tempSeialNumberListStrings.length));
						criteria = new JasperReportCriteriaDTO();
						criteria.setParameters(map);
						if (!CollectionUtils.isEmpty(crossTabReportDTOList)) {
						   //是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
						   criteria.setAutoBuildJasper(false);
						   //设置结果集
						   criteria.setResult(crossTabReportDTOList);
						   //设置所需报表的名称
						   criteria.setJrxmlName(AssetStacktakeFormDTO.PROJECT_REPORT_JRXML_INVENTORY_CROSS_NAME);
						   //设置报表路径
						   criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
						   criteria.setSheetName(results.get(k).getAssetTypeName() + IAtomsConstants.MARK_BRACKETS_LEFT + results.get(k).getAssetStatusName() + IAtomsConstants.MARK_BRACKETS_RIGHT);
						   //设置汇出格式
						   criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
						   //设置报表名称
						   criterias.add(criteria);
						}
					}
				}
				criteria = new JasperReportCriteriaDTO();
				//是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
			    criteria.setAutoBuildJasper(false);
			    //设置结果集
			    criteria.setResult(results);
			    //设置所需报表的名称
			    criteria.setJrxmlName(AssetStacktakeFormDTO.PROJECT_REPORT_JRXML_INVENTORY_OTHER_NAME);
			    //设置报表路径
			    criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
			    criteria.setSheetName("other");
			    //设置汇出格式
			    criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
			    //设置报表名称
			    criteria.setReportFileName(AssetStacktakeFormDTO.PROJECT_REPORT_FILE_INVENTORY_OTHER_NAME);
			    criterias.add(criteria);
			}
			ReportExporter.exportReportForSheets(criterias, response);
			// 成功標志
			SessionHelper.setAttribute(request, formDTO.getUseCaseNo(), sessionId, IAtomsConstants.YES);
		} catch (ServiceException e) {
			// 出錯消息
			try {
				SessionHelper.setAttribute(request, formDTO.getUseCaseNo(), sessionId, false);
				Map map = new HashMap();
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.EXPORT_ERROR_MESSAGE));
				return new ModelAndView(this.getFailureView("exportInventory"), map);
			} catch (Exception e1) {
				LOGGER.error(".exportInventory() is error:", e1);
			}
			LOGGER.error(this.getClass().getName() + ".exportInventory() service Exception.", e);
			throw new CommonException(IAtomsMessageCode.EXPORT_REPORT_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".exportInventory() Exception.", e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return null;
	}
	/**
	 * 
	 * Purpose:匯出設備盤點列印清冊報表--歷史查詢
	 * @author echomou
	 * @param request:HttpServletRequest
	 * @param response:HttpServletResponse
	 * @param formDTO:DeviceInventoryFormDTO
	 * @throws CommonException:出錯時拋出CommonException
	 * @return void
	 */
	public ModelAndView exportInventoryHistory(HttpServletRequest request, HttpServletResponse response, AssetStacktakeFormDTO formDTO) throws CommonException{
		// 取得sessionId
		String sessionId = request.getSession().getId();
		try {
			List<JasperReportCriteriaDTO> criterias = new ArrayList<JasperReportCriteriaDTO>();
			SessionContext ctx = this.getServiceLocator().doService(formDTO.getLogonUser(), this.getServiceId(), AssetStacktakeFormDTO.EXPORT_INVENTORY, formDTO);
			List<DmmAssetStacktakeListDTO> results = null;
			if(ctx != null){
				formDTO = (AssetStacktakeFormDTO) ctx.getResponseResult();
				results = formDTO.getAssetStocktackListDTOs();
			}
			if (!CollectionUtils.isEmpty(results)) {
			    JasperReportCriteriaDTO criteria = new JasperReportCriteriaDTO();
			    //是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
			    criteria.setAutoBuildJasper(false);
			    //设置结果集
			    List<DmmAssetStacktakeListDTO> resultsOne = new ArrayList<DmmAssetStacktakeListDTO>();
			    resultsOne.add(results.get(0));
			    criteria.setResult(resultsOne);
			    //设置所需报表的名称
			    criteria.setJrxmlName(AssetStacktakeFormDTO.PROJECT_REPORT_JRXML_INVENTORY_NAME);
			    //设置报表路径
			    criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
			    criteria.setSheetName(AssetStacktakeFormDTO.PROJECT_REPORT_SHEET_NAME);
			    //设置汇出格式
			    criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
			    //设置报表名称
			    criteria.setReportFileName(AssetStacktakeFormDTO.PROJECT_REPORT_FILE_INVENTORY_NAME);
			    criterias.add(criteria);
			    //存放机型集合
			    List<String> liStrings = new ArrayList<String>();
			    for (int i = 0; i < results.size(); i++) {
			    	liStrings.add(results.get(i).getStocktackId());
			    }
				CrossTabReportDTO crossTabReportDTO = null;
				List<CrossTabReportDTO> crossTabReportDTOList = null;
				if (!CollectionUtils.isEmpty(liStrings)) {
					String tempSeialNumber = "";
					String[] tempSeialNumberListStrings = null;
					for (int k = 0; k < liStrings.size(); k++) {
						crossTabReportDTOList = new ArrayList<CrossTabReportDTO>();
						tempSeialNumber = results.get(k).getSerialNumberList();
						tempSeialNumberListStrings = tempSeialNumber.split(IAtomsConstants.MARK_SEPARATOR);
						for (int i = 0; i < tempSeialNumberListStrings.length; i++) {
							crossTabReportDTO = new CrossTabReportDTO();
							crossTabReportDTO.setAssetTypeStatus(results.get(k).getAssetStatusName());
							crossTabReportDTO.setContent(tempSeialNumberListStrings[i]);
							crossTabReportDTO.setRowNo(i + 1);
							crossTabReportDTOList.add(crossTabReportDTO);
						}
						while (crossTabReportDTOList.size()%4 != 0) {
							crossTabReportDTO = new CrossTabReportDTO();
							crossTabReportDTO.setContent("");
							crossTabReportDTO.setRowNo(crossTabReportDTOList.size()+1);
							crossTabReportDTOList.add(crossTabReportDTO);
						}
						Map map = new HashMap();
						map.put("assetTypeName", results.get(k).getAssetTypeName());
						map.put("assetTypeStatus", results.get(k).getAssetStatusName());
						map.put("count", Integer.valueOf(tempSeialNumberListStrings.length));
						criteria = new JasperReportCriteriaDTO();
						criteria.setParameters(map);
						criteria.setReportFileName(AssetStacktakeFormDTO.PROJECT_REPORT_FILE_INVENTORY_NAME);
						if (!CollectionUtils.isEmpty(crossTabReportDTOList)) {
						   //是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
						   criteria.setAutoBuildJasper(false);
						   //设置结果集
						   criteria.setResult(crossTabReportDTOList);
						   //设置所需报表的名称
						   criteria.setJrxmlName(AssetStacktakeFormDTO.PROJECT_REPORT_JRXML_INVENTORY_CROSS_NAME);
						   //设置报表路径
						   criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
						   criteria.setSheetName(results.get(k).getAssetTypeName() + IAtomsConstants.MARK_BRACKETS_LEFT + results.get(k).getAssetStatusName() + IAtomsConstants.MARK_BRACKETS_RIGHT);
						   //设置汇出格式
						   criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
						   //设置报表名称
						   criterias.add(criteria);
						}
					}
				}
				criteria = new JasperReportCriteriaDTO();
				//是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
			    criteria.setAutoBuildJasper(false);
			    //设置结果集
			    criteria.setResult(results);
			    //设置所需报表的名称
			    criteria.setJrxmlName(AssetStacktakeFormDTO.PROJECT_REPORT_JRXML_INVENTORY_OTHER_NAME);
			    //设置报表路径
			    criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
			    criteria.setSheetName("other");
			    //设置汇出格式
			    criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
			    //设置报表名称
			    criteria.setReportFileName(AssetStacktakeFormDTO.PROJECT_REPORT_FILE_INVENTORY_OTHER_NAME);
			    criterias.add(criteria);
			}
			ReportExporter.exportReportForSheets(criterias, response);
			// 成功標志
			SessionHelper.setAttribute(request, formDTO.getUseCaseNo(), sessionId, IAtomsConstants.YES);
		} catch (ServiceException e) {
			// 出錯消息
			try {
				SessionHelper.setAttribute(request, formDTO.getUseCaseNo(), sessionId, false);
				Map map = new HashMap();
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.EXPORT_ERROR_MESSAGE));
				return new ModelAndView(this.getFailureView("exportInventoryHistory"), map);
			} catch (Exception e1) {
				LOGGER.error(".export() is error:", e1);
			}
			LOGGER.error(this.getClass().getName() + ".exportInventoryHistory() service Exception.", e);
			throw new CommonException(IAtomsMessageCode.EXPORT_REPORT_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".exportInventoryHistory() Exception.", e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return null;
	}
	
	
	/**
	 * 
	 * Purpose:匯出設備盤點结果報表
	 * @author allenchen
	 * @param request:HttpServletRequest
	 * @param response:HttpServletResponse
	 * @param formDTO:DeviceInventoryFormDTO
	 * @throws CommonException:出錯時拋出CommonException
	 * @return void
	 */
	public ModelAndView exportSummary(HttpServletRequest request, HttpServletResponse response, AssetStacktakeFormDTO formDTO) throws CommonException{
		// 取得sessionId
		String sessionId = request.getSession().getId();
		try {
			List<JasperReportCriteriaDTO> criterias = new ArrayList<JasperReportCriteriaDTO>();
			SessionContext ctx = this.getServiceLocator().doService(formDTO.getLogonUser(), this.getServiceId(), AssetStacktakeFormDTO.EXPORT_SUMMARY, formDTO);
			List<DmmAssetStacktakeListDTO> results = null;
			if(ctx != null){
				formDTO = (AssetStacktakeFormDTO) ctx.getResponseResult();
				results = formDTO.getAssetStocktackListDTOs();
			}
			if (!CollectionUtils.isEmpty(results)) {
			    JasperReportCriteriaDTO criteria = new JasperReportCriteriaDTO();
			    //是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
			    criteria.setAutoBuildJasper(false);
			    //设置结果集
			    List<DmmAssetStacktakeListDTO> resultsOne = new ArrayList<DmmAssetStacktakeListDTO>();
			    resultsOne.add(results.get(0));
			    criteria.setResult(resultsOne);
			    //设置所需报表的名称
			    criteria.setJrxmlName(AssetStacktakeFormDTO.PROJECT_REPORT_JRXML_INVENTORY_NAME);
			    //设置报表路径
			    criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
			    criteria.setSheetName(AssetStacktakeFormDTO.PROJECT_REPORT_SHEET_NAME);
			    //设置汇出格式
			    criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
			    //设置报表名称
			    criteria.setReportFileName(AssetStacktakeFormDTO.PROJECT_REPORT_FILE_INVENTORY_OTHER_NAME);
			    criterias.add(criteria);
			    //存放集合
			    List<String> liStrings = new ArrayList<String>();
			    for (int i = 0; i < results.size(); i++) {
			    	liStrings.add(results.get(i).getAssetTypeName());
			    }
			    
			    //報表DTO
				CrossTabReportDTO crossTabReportDTO = null;
				//報表DTOList
				List<CrossTabReportDTO> crossTabReportDTOList = null;
				if (!CollectionUtils.isEmpty(liStrings)) {
					String tempStockSeial = "";
					String[] tempStockSeialStrings = null;
					for (int k = 0; k < liStrings.size(); k++) {
						for (int i = 0; i < 4; i++) {
							crossTabReportDTOList = new ArrayList<CrossTabReportDTO>();
							String status = null;
							if (i == 0){
								tempStockSeial = results.get(k).getNoStocktackList();
								status = "待盤點";
							}
							if (i == 1){
								tempStockSeial = results.get(k).getAlreadyStocktackList();
								status = "已盤點";
							}
							if (i == 2){
								tempStockSeial = results.get(k).getOverageList();
								status = "盤盈";
							}
							if (i == 3){
								tempStockSeial = results.get(k).getAssetlLessList();
								status = "盤差";
							}
							if (tempStockSeial == null) {
								continue;
							}
							tempStockSeialStrings = tempStockSeial.split(IAtomsConstants.MARK_SEPARATOR);
							for (int m = 0; m < tempStockSeialStrings.length; m++) {
								crossTabReportDTO = new CrossTabReportDTO();
								crossTabReportDTO.setContent(tempStockSeialStrings[m]);
								crossTabReportDTO.setRowNo(m + 1);
								crossTabReportDTOList.add(crossTabReportDTO);
							}
							while (crossTabReportDTOList.size()%4 != 0) {
								crossTabReportDTO = new CrossTabReportDTO();
								crossTabReportDTO.setContent("");
								crossTabReportDTO.setRowNo(crossTabReportDTOList.size()+1);
								crossTabReportDTOList.add(crossTabReportDTO);
							}
							Map map = new HashMap();
							map.put("assetTypeName", results.get(k).getAssetTypeName());
							map.put("assetTypeStatus", status);
							map.put("count", Integer.valueOf(tempStockSeialStrings.length));
							criteria = new JasperReportCriteriaDTO();
							criteria.setParameters(map);
							if (!CollectionUtils.isEmpty(crossTabReportDTOList)) {
								//是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
							   criteria.setAutoBuildJasper(false);
							   //设置结果集
							   criteria.setResult(crossTabReportDTOList);
							   //设置所需报表的名称
							   criteria.setJrxmlName(AssetStacktakeFormDTO.PROJECT_REPORT_JRXML_INVENTORY_CROSS_NAME);
							   //设置报表路径
							   criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
							   criteria.setSheetName(results.get(k).getAssetTypeName() + IAtomsConstants.MARK_BRACKETS_LEFT + status + IAtomsConstants.MARK_BRACKETS_RIGHT );
							   //设置汇出格式
							   criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
							   criterias.add(criteria);
							}
						}
					}
				}
				criteria = new JasperReportCriteriaDTO();
				//是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
			    criteria.setAutoBuildJasper(false);
			    //设置结果集
			    criteria.setResult(results);
			    //设置所需报表的名称
			    criteria.setJrxmlName(AssetStacktakeFormDTO.PROJECT_REPORT_JRXML_INVENTORY_OTHER_NAME);
			    //设置报表路径
			    criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
			    criteria.setSheetName("other");
			    //设置汇出格式
			    criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
			    //设置报表名称
			    criteria.setReportFileName(AssetStacktakeFormDTO.PROJECT_REPORT_FILE_INVENTORY_OTHER_NAME);
			    criterias.add(criteria);
			}
			ReportExporter.exportReportForSheets(criterias, response);
			// 成功標志
			SessionHelper.setAttribute(request, formDTO.getUseCaseNo(), sessionId, IAtomsConstants.YES);
		} catch (ServiceException e) {
			// 出錯消息
			try {
				SessionHelper.setAttribute(request, formDTO.getUseCaseNo(), sessionId, false);
				Map map = new HashMap();
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.EXPORT_ERROR_MESSAGE));
				return new ModelAndView(this.getFailureView("exportSummary"), map);
			} catch (Exception e1) {
				LOGGER.error(".exportSummary() is error:", e1);
			}
			LOGGER.error(this.getClass().getName() + ".exportSummary() service Exception.", e);
			throw new CommonException(IAtomsMessageCode.EXPORT_REPORT_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".exportSummary() Exception.", e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return null;
	}
	
	/**
	 * 
	 * Purpose:匯出設備盤點结果報表--歷史查詢
	 * @author allenchen
	 * @param request:HttpServletRequest
	 * @param response:HttpServletResponse
	 * @param formDTO:DeviceInventoryFormDTO
	 * @throws CommonException:出錯時拋出CommonException
	 * @return void
	 */
	public ModelAndView exportSummaryHistory(HttpServletRequest request, HttpServletResponse response, AssetStacktakeFormDTO formDTO) throws CommonException{
		// 取得sessionId
		String sessionId = request.getSession().getId();
		try {
			List<JasperReportCriteriaDTO> criterias = new ArrayList<JasperReportCriteriaDTO>();
			SessionContext ctx = this.getServiceLocator().doService(formDTO.getLogonUser(), this.getServiceId(), AssetStacktakeFormDTO.EXPORT_SUMMARY, formDTO);
			List<DmmAssetStacktakeListDTO> results = null;
			if(ctx != null){
				formDTO = (AssetStacktakeFormDTO) ctx.getResponseResult();
				results = formDTO.getAssetStocktackListDTOs();
			}
			if (!CollectionUtils.isEmpty(results)) {
			    JasperReportCriteriaDTO criteria = new JasperReportCriteriaDTO();
			    //是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
			    criteria.setAutoBuildJasper(false);
			  //设置结果集
			    List<DmmAssetStacktakeListDTO> resultsOne = new ArrayList<DmmAssetStacktakeListDTO>();
			    resultsOne.add(results.get(0));
			    criteria.setResult(resultsOne);
			    //设置所需报表的名称
			    criteria.setJrxmlName(AssetStacktakeFormDTO.PROJECT_REPORT_JRXML_INVENTORY_NAME);
			    //设置报表路径
			    criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
			    criteria.setSheetName(AssetStacktakeFormDTO.PROJECT_REPORT_SHEET_NAME);
			    //设置汇出格式
			    criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
			    //设置报表名称
			    criteria.setReportFileName(AssetStacktakeFormDTO.PROJECT_REPORT_FILE_INVENTORY_OTHER_NAME);
			    criterias.add(criteria);
			    //存放集合
			    List<String> liStrings = new ArrayList<String>();
			    for (int i = 0; i < results.size(); i++) {
			    	liStrings.add(results.get(i).getAssetTypeName());
			    }
			    //報表DTO
				CrossTabReportDTO crossTabReportDTO = null;
				//報表DTOList
				List<CrossTabReportDTO> crossTabReportDTOList = null;
				if (!CollectionUtils.isEmpty(liStrings)) {
					String tempStockSeial = "";
					String[] tempStockSeialStrings = null;
					for (int k = 0; k < liStrings.size(); k++) {
						for (int i = 0; i < 4; i++) {
							crossTabReportDTOList = new ArrayList<CrossTabReportDTO>();
							String status = null;
							if (i == 0){
								tempStockSeial = results.get(k).getNoStocktackList();
								status = "待盤點";
							}
							if (i == 1){
								tempStockSeial = results.get(k).getAlreadyStocktackList();
								status = "已盤點";
							}
							if (i == 2){
								tempStockSeial = results.get(k).getOverageList();
								status = "盤盈";
							}
							if (i == 3){
								tempStockSeial = results.get(k).getAssetlLessList();
								status = "盤差";
							}
							if (tempStockSeial == null) {
								continue;
							}
							tempStockSeialStrings = tempStockSeial.split(IAtomsConstants.MARK_SEPARATOR);
							for (int m = 0; m < tempStockSeialStrings.length; m++) {
								crossTabReportDTO = new CrossTabReportDTO();
								crossTabReportDTO.setContent(tempStockSeialStrings[m]);
								crossTabReportDTO.setRowNo(m + 1);
								crossTabReportDTOList.add(crossTabReportDTO);
							}
							while (crossTabReportDTOList.size()%4 != 0) {
								crossTabReportDTO = new CrossTabReportDTO();
								crossTabReportDTO.setContent("");
								crossTabReportDTO.setRowNo(crossTabReportDTOList.size()+1);
								crossTabReportDTOList.add(crossTabReportDTO);
							}
							Map map = new HashMap();
							map.put("assetTypeName", results.get(k).getAssetTypeName());
							map.put("assetTypeStatus", status);
							map.put("count", Integer.valueOf(tempStockSeialStrings.length));
							criteria = new JasperReportCriteriaDTO();
							criteria.setParameters(map);
							criteria.setReportFileName(AssetStacktakeFormDTO.PROJECT_REPORT_FILE_INVENTORY_OTHER_NAME);
							if (!CollectionUtils.isEmpty(crossTabReportDTOList)) {
								//是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
							   criteria.setAutoBuildJasper(false);
							   //设置结果集
							   criteria.setResult(crossTabReportDTOList);
							   //设置所需报表的名称
							   criteria.setJrxmlName(AssetStacktakeFormDTO.PROJECT_REPORT_JRXML_INVENTORY_CROSS_NAME);
							   //设置报表路径
							   criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
							   criteria.setSheetName(results.get(k).getAssetTypeName() + IAtomsConstants.MARK_BRACKETS_LEFT + status + IAtomsConstants.MARK_BRACKETS_RIGHT );
							   //设置汇出格式
							   criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
							   criterias.add(criteria);
							}
						}
					}
				}
				criteria = new JasperReportCriteriaDTO();
				//是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
			    criteria.setAutoBuildJasper(false);
			    //设置结果集
			    criteria.setResult(results);
			    //设置所需报表的名称
			    criteria.setJrxmlName(AssetStacktakeFormDTO.PROJECT_REPORT_JRXML_INVENTORY_OTHER_NAME);
			    //设置报表路径
			    criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
			    criteria.setSheetName("other");
			    //设置汇出格式
			    criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
			    //设置报表名称
			    criteria.setReportFileName(AssetStacktakeFormDTO.PROJECT_REPORT_FILE_INVENTORY_OTHER_NAME);
			    criterias.add(criteria);
			}
			ReportExporter.exportReportForSheets(criterias, response);
			// 成功標志
			SessionHelper.setAttribute(request, formDTO.getUseCaseNo(), sessionId, IAtomsConstants.YES);
		} catch (ServiceException e) {
			// 出錯消息
			try {
				SessionHelper.setAttribute(request, formDTO.getUseCaseNo(), sessionId, false);
				Map map = new HashMap();
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.EXPORT_ERROR_MESSAGE));
				return new ModelAndView(this.getFailureView("exportSummaryHistory"), map);
			} catch (Exception e1) {
				LOGGER.error(".export() is error:", e1);
			}
			LOGGER.error(this.getClass().getName() + ".exportInventory() service Exception.", e);
			throw new CommonException(IAtomsMessageCode.EXPORT_REPORT_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".exportInventory() Exception.", e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return null;
	}
	
}
