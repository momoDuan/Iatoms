package com.cybersoft4u.xian.iatoms.web.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import cafe.core.bean.Parameter;
import cafe.core.context.SessionContext;
import cafe.core.exception.CommonException;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.SessionHelper;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetStockReportDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetStockReportFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;
import com.cybersoft4u.xian.iatoms.common.utils.ReportExporter;

/**
 * 
 * Purpose：設備狀態報表Controller 
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016-7-15
 * @MaintenancePersonnel CrissZhang
 */
public class AssetStockReportController extends AbstractMultiActionController<AssetStockReportFormDTO> {
	/**
	 * 日志记录物件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(AssetStockReportController.class);
	
	/**
	 * Constructor:无参构造函数
	 */
	public AssetStockReportController() {
		this.setCommandClass(AssetStockReportFormDTO.class);
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(AssetStockReportFormDTO command) throws CommonException {
		return true;
	}

	/**	
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public AssetStockReportFormDTO parse(HttpServletRequest request, AssetStockReportFormDTO command) throws CommonException {
		try {
			// 獲取actionId
			String actionId = command.getActionId();
			if (!StringUtils.hasText(actionId)) {
				actionId = IAtomsConstants.ACTION_INIT;
			}
			command.setActionId(actionId);
		} catch (Exception e) {
			LOGGER.error("AssetStockReportController.parse()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
		return command;
	}
	/**
	 * 
	 * Purpose:匯出
	 * @author allenchen
	 * @param request:HttpServletRequest
	 * @param response:HttpServletResponse
	 * @param formDTO:DeviceStockTableFormDTO
	 * @throws CommonException:出錯時拋出CommonException
	 * @return void
	 */
	@SuppressWarnings("rawtypes")
	public ModelAndView export(HttpServletRequest request, HttpServletResponse response, AssetStockReportFormDTO formDTO) throws CommonException{
		// 取得sessionId
		String sessionId = request.getSession().getId();
		try {
			List<JasperReportCriteriaDTO> criterias = new ArrayList<JasperReportCriteriaDTO>();
			SessionContext ctx = this.getServiceLocator().doService(formDTO.getLogonUser(), this.getServiceId(), IAtomsConstants.ACTION_EXPORT, formDTO);
			List<AssetStockReportDTO> results = null;
			List<Parameter> listAssetCategoryList = null;
			List<String> companyIdList = null;
			String queryMonth = null;
			JasperReportCriteriaDTO criteria = null;
			List<AssetStockReportDTO> exportList = null; 
			List<AssetStockReportDTO> exports = null; 
			//总计数量
			long sumPurchaseNumber = 0;
			int sumStorageNumber = 0;
			int sumPreparationNumber = 0;
			int sumDestoryedNumber = 0;
			int sumScrapedNumber = 0;
			int sumLoseNumber = 0;
			int sumMaintenanceNumber = 0;
			int sumStepNumber = 0;
			int sumAvailableNumber = 0;
			int tempIndex = 0;
			int companyIdLength = 0;
			int assetStockReportLength = 0;
			//Bug #2388 update by 2017/09/12
			int sumRepairedNumber = 0;
			if(ctx != null){
				formDTO = (AssetStockReportFormDTO) ctx.getResponseResult();
				results = formDTO.getAssetStockReportDTOs();
				listAssetCategoryList = formDTO.getListAssetCategory();
				queryMonth = formDTO.getQueryMonth();
				companyIdList = formDTO.getCompanyIdList();
			}
			// 當前時間
			String currentDate = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
			// 處理字符串拼接
			StringBuilder builder = new StringBuilder();
			if (!CollectionUtils.isEmpty(results) && !CollectionUtils.isEmpty(listAssetCategoryList)) {
				int assetCategoryLength = listAssetCategoryList.size();
				
				// 按照客戶分類
				if (!CollectionUtils.isEmpty(results) && !CollectionUtils.isEmpty(companyIdList) ) {
					tempIndex = 0;
					companyIdLength = companyIdList.size();
					for (int k = 0;k < companyIdLength;k++) {
						exportList =  new ArrayList<AssetStockReportDTO>();
						assetStockReportLength = results.size();
						for (int j = 0;j < assetStockReportLength; j++) {
							if (results.get(j) != null) {
								if (companyIdList.get(k).equals(results.get(j).getCompanyId())) {
									tempIndex = j;
									exportList.add(results.get(j));
								}
							}
						}
						// 再按照設備類別分類
						for (int i=0; i < assetCategoryLength; i++) {
							exports =  new ArrayList<AssetStockReportDTO>();
							sumPurchaseNumber = 0;
							sumStorageNumber = 0;
							sumPreparationNumber = 0;
							sumDestoryedNumber = 0;
							sumScrapedNumber = 0;
							sumLoseNumber = 0;
							sumMaintenanceNumber = 0;
							sumStepNumber = 0;
							sumAvailableNumber = 0;
							sumRepairedNumber = 0;
							for (AssetStockReportDTO assetStockReportDTO : exportList) {
								if (listAssetCategoryList.get(i).getValue().equals(assetStockReportDTO.getAssetCategory())) {
									assetStockReportDTO.setTitleName(listAssetCategoryList.get(i).getName());
									assetStockReportDTO.setYyyymm(i18NUtil.getName(IAtomsMessageCode.REPOSTATUS_REPORT_QUERY_MOUNTH) + IAtomsConstants.MARK_SPACE + queryMonth);
									assetStockReportDTO.setCurrentDate(currentDate);
									
									if (assetStockReportDTO.getPurchaseNumber() != null) {
										sumPurchaseNumber += assetStockReportDTO.getPurchaseNumber().longValue();
									}
									sumStorageNumber += assetStockReportDTO.getStorageNumber().intValue();
									sumPreparationNumber += assetStockReportDTO.getPreparationNumber().intValue();
									sumDestoryedNumber += assetStockReportDTO.getDestoryedNumber().intValue();
									sumScrapedNumber += assetStockReportDTO.getScrapedNumber().intValue();
									sumLoseNumber += assetStockReportDTO.getLoseNumber().intValue();
									sumMaintenanceNumber += assetStockReportDTO.getMaintenanceNumber().intValue();
									sumStepNumber += assetStockReportDTO.getStepNumber().intValue();
									sumAvailableNumber += assetStockReportDTO.getAvailableNumber().intValue();
									//Bug #2388 update by 2017/09/12
									sumRepairedNumber += assetStockReportDTO.getRepairedNumber().intValue();
									exports.add(assetStockReportDTO);
								}
							}
							if (!CollectionUtils.isEmpty(exports)){
								exports.get(exports.size() - 1).setSumPurchaseNumber(Long.valueOf(sumPurchaseNumber));
								exports.get(exports.size() - 1).setSumStorageNumber(Integer.valueOf(sumStorageNumber));
								exports.get(exports.size() - 1).setSumPreparationNumber(Integer.valueOf(sumPreparationNumber));
								exports.get(exports.size() - 1).setSumDestoryedNumber(Integer.valueOf(sumDestoryedNumber));
								exports.get(exports.size() - 1).setSumScrapedNumber(Integer.valueOf(sumScrapedNumber));
								exports.get(exports.size() - 1).setSumLoseNumber(Integer.valueOf(sumLoseNumber));
								exports.get(exports.size() - 1).setSumMaintenanceNumber(Integer.valueOf(sumMaintenanceNumber));
								exports.get(exports.size() - 1).setSumStepNumber(Integer.valueOf(sumStepNumber));
								exports.get(exports.size() - 1).setSumAvailableNumber(Integer.valueOf(sumAvailableNumber));
								//Bug #2388 update by 2017/09/12
								exports.get(exports.size() - 1).setSumRepairedNumber(Integer.valueOf(sumRepairedNumber));

								criteria = new JasperReportCriteriaDTO();
								//是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
								criteria.setAutoBuildJasper(true);
								//设置结果集
								criteria.setResult(exports);
								//设置所需报表的名称
								criteria.setJrxmlName(AssetStockReportFormDTO.PROJECT_REPORT_JRXML_DEVICE_STOCK_TABLE_NAME);
								//设置报表路径
								criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
								/*criteria.setSheetName(tempAssetStockReportDTOs.get(tempIndex).getCompanyName()
										+IAtomsConstants.MARK_MIDDLE_LINE+AssetStockReportFormDTO.PROJECT_REPORT_FILE_STOCK_TABLE_NAME
										+IAtomsConstants.MARK_BRACKETS_LEFT + listAssetCategoryList.get(i).getName() 
										+ IAtomsConstants.MARK_BRACKETS_RIGHT);*/
								builder.append(results.get(tempIndex).getCompanyName()).append(IAtomsConstants.MARK_MIDDLE_LINE);
								builder.append(AssetStockReportFormDTO.PROJECT_REPORT_FILE_STOCK_TABLE_NAME).append(IAtomsConstants.MARK_BRACKETS_LEFT);
								builder.append(listAssetCategoryList.get(i).getName()).append(IAtomsConstants.MARK_BRACKETS_RIGHT);
								criteria.setSheetName(builder.toString());
								builder.delete(0, builder.length());
								//设置汇出格式
								criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
								//设置报表名称
								criteria.setReportFileName(AssetStockReportFormDTO.PROJECT_REPORT_FILE_DEVICE_STOCK_TABLE_NAME);
								criterias.add(criteria);
							}
						}
					}
				}
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
				return new ModelAndView(this.getFailureView(IAtomsConstants.ACTION_EXPORT), map);
			} catch (Exception e1) {
				LOGGER.error(".export() is error:", e1);
			}
			LOGGER.error("AssetStockReportController.export()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.EXPORT_REPORT_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("AssetStockReportController.export()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return null;
	}
}
