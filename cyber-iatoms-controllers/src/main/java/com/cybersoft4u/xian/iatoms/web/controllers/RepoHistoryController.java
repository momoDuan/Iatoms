package com.cybersoft4u.xian.iatoms.web.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;

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
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryHistoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.RepoHistoryFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.CrossTabReportDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;
import com.cybersoft4u.xian.iatoms.common.utils.IAtomsUtils;
import com.cybersoft4u.xian.iatoms.common.utils.ReportExporter;

public class RepoHistoryController extends
		AbstractMultiActionController<RepoHistoryFormDTO> {

	/**
	 * 系統日志記錄
	 */
	private static final CafeLog log = CafeLogFactory.getLog(RepoHistoryController.class);
	
	/**
	 * 所有要匯出的欄位
	 */
	private Map<String, String> exportField;
	
	/**
	 * Constructor: 無參構造
	 */
	public RepoHistoryController() {
		this.setCommandClass(RepoHistoryFormDTO.class);
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	public boolean validate(RepoHistoryFormDTO parmemters)
			throws CommonException {
		return true;
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	public RepoHistoryFormDTO parse(HttpServletRequest request,
			RepoHistoryFormDTO command) throws CommonException {
		try{
			String actionId = this.getString(request, IAtomsConstants.PARAM_ACTION_ID);
			if(!StringUtils.hasText(actionId)){
				actionId = IAtomsConstants.ACTION_INIT;
			}
			command.setActionId(actionId);
			log.debug(this.getClass().getName()+".parse() --> actionId: "+actionId);
			if(IAtomsConstants.ACTION_QUERY.equals(actionId)){
				//查詢參數 -- 設備序號
				String querySerialNumber = this.getString(request, RepoHistoryFormDTO.QUERY_PAGE_SERIAL_NUMBER);
				log.debug(this.getClass().getName()+".parse() --> querySerialNumber: "+querySerialNumber);
				//查詢參數 -- TID
				String queryTID = this.getString(request, RepoHistoryFormDTO.QUERY_PAGE_TID);
				log.debug(this.getClass().getName()+".parse() --> queryTID: "+queryTID);
				//查詢參數 -- DTID
				String queryDTID = this.getString(request, RepoHistoryFormDTO.QUERY_PAGE_DTID);
				log.debug(this.getClass().getName()+".parse() --> queryDTID: "+queryDTID);
				//查詢參數 -- 當前頁碼
				int currentPage = this.getInt(request, IAtomsConstants.QUERY_PAGE_INDEX);
				log.debug(this.getClass().getName()+".parse() --> currentPage: "+currentPage);
				//查詢參數 -- 總筆數
				int pageSize = this.getInt(request, IAtomsConstants.QUERY_PAGE_ROWS);
				log.debug(this.getClass().getName()+".parse() --> pageSize: "+pageSize);
				//查詢參數 -- 排序方式
				String order = this.getString(request, IAtomsConstants.QUERY_PAGE_ORDER);
				log.debug(this.getClass().getName()+".parse() --> order: "+order);
				//查詢參數 -- 排序欄位
				String sort = this.getString(request, IAtomsConstants.QUERY_PAGE_SORT);
				log.debug(this.getClass().getName()+".parse() --> sort: "+sort);
				
				command.setQueryDTID(queryDTID);
				command.setQuerySerialNumber(querySerialNumber);
				command.setQueryTID(queryTID);
				command.setOrder(order);
				command.setSort(sort);
				command.getPageNavigation().setCurrentPage(currentPage-1);
				command.getPageNavigation().setPageSize(pageSize);
			}else if(IAtomsConstants.ACTION_EXPORT.equals(actionId)){
				String exportField = this.getString(request, RepoHistoryFormDTO.EXPORT_PARAM_FIELDS);
				String histId = this.getString(request, RepoHistoryFormDTO.EXPORT_PARAM_HIST_IDS);
				String[] exportFields = null;
				String[] histIds = null;
				if(StringUtils.hasText(exportField)){
					exportFields = exportField.split(IAtomsConstants.MARK_SEPARATOR);
				}
				if(StringUtils.hasText(histId)){
					histIds = histId.split(IAtomsConstants.MARK_SEPARATOR);
				}
				command.setHistIds(histIds);
				command.setExportFields(exportFields);
			}
		}catch(Exception e){
			log.error(this.getClass().getName()+"parse() is error: "+e, e);
		}
		return command;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public ModelAndView export(HttpServletRequest request, HttpServletResponse response, RepoHistoryFormDTO command) throws CommonException{
		// 取得sessionId
		String sessionId = request.getSession().getId();
		try{
			String[] exportFileds = command.getExportFields();
			//根据条件查询报表数据
			SessionContext sessionContext = this.doService(this.getServiceId(), IAtomsConstants.ACTION_EXPORT, command) ;
			if(sessionContext != null){
				//查询结果
				command = (RepoHistoryFormDTO) sessionContext.getResponseResult();
				List<DmmRepositoryHistoryDTO> results = command.getList();
				if (!CollectionUtils.isEmpty(results)) {
					//JasperReport條件設定DTO
					JasperReportCriteriaDTO criteria = new JasperReportCriteriaDTO();
					// 是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
					criteria.setAutoBuildJasper(false);
					//报表模板名称
					String jrxmlName = RepoHistoryFormDTO.EXPORT_JRXML_NAME;
					//汇出后的xsl名称
					String reportFileName = RepoHistoryFormDTO.EXPORT_FILE_NAME;
					List<CrossTabReportDTO> CrossTabReportDTOList = new ArrayList<CrossTabReportDTO>();
					CrossTabReportDTO crossTabDTO = null;
					String columnName = "";
					String content = "";
					for(int i=0;i<results.size();i++){
						for(int j=0;j<exportFileds.length;j++){
							columnName = exportFileds[j];
							content = IAtomsUtils.getFiledValueByName(columnName, results.get(i));
							crossTabDTO = new CrossTabReportDTO();
							crossTabDTO.setRowNo(i+1);
							crossTabDTO.setColumnName(this.exportField.get(columnName));
							crossTabDTO.setContent(content);
							CrossTabReportDTOList.add(crossTabDTO);
						}
					}
					criteria.setResult(CrossTabReportDTOList);
					//設置所需報表的Name
					criteria.setJrxmlName(jrxmlName);
					//設置報表路徑
					criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
					//設置匯出格式
					criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
					//設置報表Name
					criteria.setReportFileName(reportFileName);
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
				log.error(".export() is error:", e1);
			}
			log.error(this.getClass().getName()+".export() is error:"+e,e);
			throw new CommonException(IAtomsMessageCode.EXPORT_REPORT_FAILURE);
		}
		return null;
	}

	/**
	 * @return the exportField
	 */
	public Map<String, String> getExportField() {
		return exportField;
	}

	/**
	 * @param exportField the exportField to set
	 */
	public void setExportField(Map<String, String> exportField) {
		this.exportField = exportField;
	}

}
