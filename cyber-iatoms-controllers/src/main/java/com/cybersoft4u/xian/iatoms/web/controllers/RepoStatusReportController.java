package com.cybersoft4u.xian.iatoms.web.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;

import cafe.core.bean.Message;
import cafe.core.context.SessionContext;
import cafe.core.exception.CommonException;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.SessionHelper;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.RepoStatusReportDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.RepoStatusReportFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;
import com.cybersoft4u.xian.iatoms.common.utils.ReportExporter;

/**
 * Purpose: 設備狀態報表Controller
 * @author barryzhang
 * @since  JDK 1.7
 * @date   2016年8月23日
 * @MaintenancePersonnel barryzhang
 */
public class RepoStatusReportController extends AbstractMultiActionController<RepoStatusReportFormDTO> {

	/**
	 * 日志記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(RepoStatusReportController.class);
	
	/**
	 * Constructor: 無參構造
	 */
	public RepoStatusReportController() {
		this.setCommandClass(RepoStatusReportFormDTO.class);
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	public boolean validate(RepoStatusReportFormDTO parmemters) throws CommonException {
		if (parmemters == null) {
			return false;
		}
		//獲取查詢條件 -- 查詢月份 -- 必輸
		String queryDate = parmemters.getQueryDate();
		//若查詢月份的值不存在時設定提示消息並拋出異常
		if (!StringUtils.hasText(queryDate)) {
			Message msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
					new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSTATUSREPORT_QUERY_DATE)});
			throw new CommonException(msg);
		}
		return true;
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	public RepoStatusReportFormDTO parse(HttpServletRequest request, RepoStatusReportFormDTO command) throws CommonException {
		try {
			//獲取actionId
			String actionId = this.getString(request, IAtomsConstants.FIELD_ACTION_ID);
			//當actionId不存在時設置值為Init
			if(!StringUtils.hasText(actionId)){
				actionId = IAtomsConstants.ACTION_INIT;
			}
			command.setActionId(actionId);
			//查詢和匯出時
			if(IAtomsConstants.ACTION_QUERY.equals(actionId) || IAtomsConstants.ACTION_EXPORT.equals(actionId)){
				// 查詢條件--排序方式
				String order = this.getString(request, IAtomsConstants.QUERY_PAGE_ORDER);
				// 查詢條件--查询日期
				String queryDate = this.getString(request, RepoStatusReportFormDTO.QUERY_PAGE_PARAM_DATE);
				String yyyyMM = IAtomsConstants.MARK_EMPTY_STRING;
				//當存在查詢月份並且月份中存在‘/’時
				if(StringUtils.hasText(queryDate) && queryDate.contains(IAtomsConstants.MARK_BACKSLASH)){
					//設定月份的格式為yyyymm
					yyyyMM = queryDate.replace(IAtomsConstants.MARK_BACKSLASH, IAtomsConstants.MARK_EMPTY_STRING);
				}
				// 查詢條件--客户
				String queryCustomer = this.getString(request, RepoStatusReportFormDTO.QUERY_PAGE_PARAM_CUSTOMER);
				// 查詢條件--维护模式
				String queryMaType = this.getString(request, RepoStatusReportFormDTO.QUERY_PAGE_PARAM_MA_TYPE);
				// 查詢條件--設備名稱
				String queryAssetNames = this.getString(request, RepoStatusReportFormDTO.QUERY_PAGE_PARAM_ASSET_NAMES);
				// 查詢條件--通訊模式
				String queryCommModes = this.getString(request, RepoStatusReportFormDTO.QUERY_PAGE_PARAM_COMM_MODES);
				//匯出模式
				String reportType = this.getString(request, RepoStatusReportFormDTO.EXPORT_REPORT_TYPE);
				command.setQueryAssetNames(queryAssetNames);
				command.setQueryCommModes(queryCommModes);
				command.setQueryCustomer(queryCustomer);
				command.setQueryMaType(queryMaType);
				command.setReportType(reportType);
				command.setQueryDate(queryDate);
				command.setYyyyMM(yyyyMM);
				command.setOrder(order);
			}
		} catch(Exception e) {
			if (LOGGER != null) {
				LOGGER.error("parse()", "Exception:", e);
			}
		}
		return command;
	}
	
	/**
	 * 
	 * Purpose: 汇出
	 * @author barryzhang
	 * @param request 請求對象
	 * @param response 相應對象
	 * @param command formDTO
	 * @throws CommonException 錯誤時拋出的Controller異常類
	 * @return void
	 */
	@SuppressWarnings("rawtypes")
	public ModelAndView export(HttpServletRequest request, HttpServletResponse response, RepoStatusReportFormDTO command) throws CommonException{
		// 取得sessionId
		String sessionId = request.getSession().getId();
		try{
			//匯出的報表類型
			String queryType = command.getReportType();
			//根据条件查询报表数据
			SessionContext sessionContext = this.doService(this.getServiceId(), 
					IAtomsConstants.ACTION_EXPORT, command) ;
			//需要匯出的信息集合
			List<RepoStatusReportDTO> results = new ArrayList<RepoStatusReportDTO>();
			//客戶信息集合
			List<String> companyIdList = new ArrayList<String>();
			if(sessionContext != null){
				command = (RepoStatusReportFormDTO) sessionContext.getResponseResult();
				results = command.getList();
				companyIdList = command.getCompanyIdList();
			}
			//若存在查詢結果
			if(!CollectionUtils.isEmpty(results)){
				//匯出信息的記錄數
				int resultSize = results.size();
				//設置變量，用於ireport中設備類別列的分組顯示的判斷條件
				for (int t = 0; t < resultSize;t++) {
					//拼接報表中的查詢月份欄位 - 查詢月份：yyyy/mm
					results.get(t).setYyyyMM(i18NUtil.getName(IAtomsMessageCode.REPOSTATUS_REPORT_QUERY_MOUNTH) + IAtomsConstants.MARK_SPACE + results.get(t).getYyyyMM());
					if (results.get(t) != null) {
						if (t == 0){
							results.get(t).setAssetAndWarehouse(null);
							results.get(t).setWarehouseNameAndId(null);
						} else {
							if (results.get(t - 1) != null) {
								results.get(t).setAssetAndWarehouse(results.get(t - 1).getWarehouseId() + results.get(t - 1).getAssetCategory());
								results.get(t).setWarehouseNameAndId(results.get(t - 1).getCompanyName() + results.get(t - 1).getWarehouseId() + results.get(t - 1).getWarehouseName());
							}
						}
					}
				}
				//按sheet匯出報表對象
				List<JasperReportCriteriaDTO> criterias = new ArrayList<JasperReportCriteriaDTO>();
				//報表對象
				JasperReportCriteriaDTO criteria = null;
				//每個sheet中應匯出的結果集
				List<RepoStatusReportDTO> tempRepoStatusReportList = null;
				int tempIndex = 0;
				String companyName = "";
				//按客戶分sheet匯出
				int companyIdListSize = companyIdList.size();
				//遍歷客戶信息
				for (int i = 0; i < companyIdListSize; i++) {
					tempRepoStatusReportList = new ArrayList<RepoStatusReportDTO>();
					//依次設置變量
					for (int j = 0;j < resultSize;j++) {
						if (results.get(j) != null) {
							if (companyIdList.get(i).equals(results.get(j).getCompanyId())) {
								tempIndex = j;
								tempRepoStatusReportList.add(results.get(j));
							}
						}
					}
					criteria = new JasperReportCriteriaDTO();
					// 是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
					criteria.setAutoBuildJasper(true);
					//封裝報表對象
					criteria.setResult(tempRepoStatusReportList);
					//設置報表路徑
					criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
					//設置匯出格式
					criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
					//公司簡稱
					if (results.get(tempIndex) != null) {
						companyName = results.get(tempIndex).getCompanyName();
						companyName = companyName.replaceAll("\\\\", "|");
						companyName = companyName.replaceAll("/", "|");
					}
					//設備狀態清單
					if(StringUtils.hasText(queryType) && queryType.equals(RepoStatusReportFormDTO.EXPORT_REPORT_TYPE_LIST)){
						//設置所需報表的Name
						criteria.setJrxmlName(RepoStatusReportFormDTO.EXPORT_JRXML_NAME_LIST);
						criteria.setReportFileName(i18NUtil.getName(IAtomsMessageCode.REPOSTATUS_REPORT_FILE_NAME_LIST));
						//設置報表sheetName
						criteria.setSheetName(companyName.concat(IAtomsConstants.MARK_MIDDLE_LINE).concat(i18NUtil.getName(IAtomsMessageCode.REPOSTATUS_REPORT_FILE_NAME_LIST)));
					}else{
						//設置所需報表的Name
						criteria.setJrxmlName(RepoStatusReportFormDTO.EXPORT_JRXML_NAME_DETAIL);
						criteria.setReportFileName(i18NUtil.getName(IAtomsMessageCode.REPOSTATUS_REPORT_FILE_NAME_DETAIL));
						//設置報表sheetName
						criteria.setSheetName(companyName.concat(IAtomsConstants.MARK_MIDDLE_LINE).concat(i18NUtil.getName(IAtomsMessageCode.REPOSTATUS_REPORT_FILE_NAME_DETAIL)));
					}
					//將子報表添加到主報表中
					criterias.add(criteria);
				}
				ReportExporter.exportReportForSheets(criterias, response);
				// 成功標志
				SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, IAtomsConstants.YES);
			}
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
			if (LOGGER != null) {
				LOGGER.error("export()", "Exception:", e);
			}
			throw new CommonException(IAtomsMessageCode.EXPORT_REPORT_FAILURE, e);
		}
		return null;
	}
}
