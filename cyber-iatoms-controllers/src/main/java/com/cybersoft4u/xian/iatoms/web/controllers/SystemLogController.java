package com.cybersoft4u.xian.iatoms.web.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmSystemLoggingDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AdmRoleFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AdmUserFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.SystemLogFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;
import com.cybersoft4u.xian.iatoms.common.utils.ReportExporter;

import cafe.core.bean.Message;
import cafe.core.context.SessionContext;
import cafe.core.exception.CommonException;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.SessionHelper;

/**
 * Purpose: 系統日志查詢Controller
 * @author amandawang
 * @since  JDK 1.7
 * @date   2016年7月5日
 * @MaintenancePersonnel amandawang
 */
public class SystemLogController extends AbstractMultiActionController<SystemLogFormDTO> {
	
	/**
	 * 日志記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(SystemLogController.class);
	
	/**
	 * 
	 * Constructor: 無參構造子
	 */
	public SystemLogController () {
		this.setCommandClass(SystemLogFormDTO.class);
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	public boolean validate(SystemLogFormDTO command)
			throws CommonException {
		if (command == null) {
			return false;
		}
		// 获取actionId
		String actionId = command.getActionId();
		Message msg = null;
		String formDate = command.getQueryFromDate();
		String toDate = command.getQueryToDate();
		if(IAtomsConstants.ACTION_QUERY.equals(actionId) || IAtomsConstants.ACTION_EXPORT.equals(actionId)){
			//時間起
			if (!StringUtils.hasText(formDate)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_SYSTEM_LOGGING_FROM_DATE)});
				throw new CommonException(msg);
			}
			//時間迄
			if (!StringUtils.hasText(toDate)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_SYSTEM_LOGGING_TO_DATE)});
				throw new CommonException(msg);
			}
		}
		return true;
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	public SystemLogFormDTO parse(HttpServletRequest request, SystemLogFormDTO command) throws CommonException {
		try{
			String actionId = this.getString(request, IAtomsConstants.FIELD_ACTION_ID);
			if(!StringUtils.hasText(actionId)){
				actionId = IAtomsConstants.ACTION_INIT;
			}
			command.setActionId(actionId);
			LOGGER.debug("parse() --> actionId: " + actionId);
			if(IAtomsConstants.ACTION_QUERY.equals(actionId) || IAtomsConstants.ACTION_EXPORT.equals(actionId)){
				//查詢參數 -- 用戶帳號
				String queryAccount = this.getString(request, SystemLogFormDTO.QUERY_PAGE_PARAM_ACCOUNT);
				LOGGER.debug("parse() --> queryAccount: " + queryAccount);
				//拼接'%',後置模糊查詢
				if(StringUtils.hasText(queryAccount)) {
					queryAccount = queryAccount + IAtomsConstants.MARK_PERCENT;
				}
				//查詢參數 -- 起始日期
				String queryFromDate = this.getString(request, SystemLogFormDTO.QUERY_PAGE_PARAM_FROM_DATE);
				LOGGER.debug("parse() --> queryFromDate: " + queryFromDate);
				//查詢參數 -- 終止日期
				String queryToDate = this.getString(request, SystemLogFormDTO.QUERY_PAGE_PARAM_TO_DATE);
				LOGGER.debug("parse() --> queryToDate: " + queryToDate);
				//查詢參數 -- 當前頁碼
				int currentPage = this.getInt(request, IAtomsConstants.QUERY_PAGE_INDEX);
				LOGGER.debug("parse() --> currentPage: " + currentPage);
				//查詢參數 -- 總筆數
				int pageSize = this.getInt(request, IAtomsConstants.QUERY_PAGE_ROWS);
				LOGGER.debug("parse() --> pageSize: " + pageSize);
				//查詢參數 -- 排序方式
				String order = this.getString(request, IAtomsConstants.QUERY_PAGE_ORDER);
				if (!StringUtils.hasText(order)) {
					order = SystemLogFormDTO.PARAM_PAGE_ORDER;
				}
				LOGGER.debug("parse() --> order: " + order);
				//查詢參數 -- 排序欄位
				String sort = this.getString(request, IAtomsConstants.QUERY_PAGE_SORT);
				if (!StringUtils.hasText(sort)) {
					sort = SystemLogFormDTO.PARAM_PAGE_SORT;
				}
				LOGGER.debug("parse() --> sort: " + sort);
				command.setQueryFromDate(queryFromDate);
				command.setQueryToDate(queryToDate);
				command.setQueryAccount(queryAccount);
				command.setOrder(order);
				command.setSort(sort);
				command.getPageNavigation().setCurrentPage(currentPage - 1);
				command.getPageNavigation().setPageSize(pageSize);
			}
		}catch(Exception e){
			LOGGER.error("SystemLogController.parse() is error: " + e, e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
		return command;
	}
	
	/**
	 * 
	 * Purpose: 系統日志查詢結果匯出
	 * @author ericdu
	 * @param request 請求對象
	 * @param response 響應對象
	 * @param command formDTO
	 * @throws CommonException 異常類
	 * @return void
	 */
	@SuppressWarnings({ "rawtypes"})
	public ModelAndView export(HttpServletRequest request, HttpServletResponse response, SystemLogFormDTO command) throws CommonException {
		// 取得sessionId
		String sessionId = request.getSession().getId();
		try{
			if(command != null){
				IAtomsLogonUser logonUser = (IAtomsLogonUser) command.getLogonUser();
				JasperReportCriteriaDTO criteria = new JasperReportCriteriaDTO();
				//請求service進行數據查詢
				SessionContext returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_SYSTEM_LOG_SERVICE, IAtomsConstants.ACTION_EXPORT, command);
				List<AdmSystemLoggingDTO> results = null;
				if(returnCtx != null){
					command = (SystemLogFormDTO) returnCtx.getResponseResult();
					results = command.getList();
				}
				//封裝報表對象
				if(!CollectionUtils.isEmpty(results)){
					// 是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
					criteria.setAutoBuildJasper(false);
					criteria.setResult(results);
					//設置所需報表的Name
					criteria.setJrxmlName(SystemLogFormDTO.REPORT_JRXML_NAME);
					//設置報表路徑
					criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
					//設置匯出格式
					criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
					//設置報表Name
					criteria.setReportFileName(SystemLogFormDTO.REPORT_FILE_NAME);
					criteria.setSheetName(SystemLogFormDTO.REPORT_FILE_NAME);
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
				LOGGER.error("SystemLogController.export() is error:", e1);
			}
			LOGGER.error("SystemLogController.export() is error: " + e, e);
			throw new CommonException(IAtomsMessageCode.EXPORT_REPORT_FAILURE);
		}
		return null;
	}
	
	/**
	 * Purpose:打開系統log對話框
	 * @author CrissZhang
	 * @param request : 請求
	 * @param response ： 響應
	 * @param command ：AdmUserFormDTO
	 * @throws Exception :發生錯誤時, 丟出Exception
	 * @return ModelAndView ：返回一个ModelAndView对象
	 */
	public ModelAndView openLogDialog(HttpServletRequest request, HttpServletResponse response, SystemLogFormDTO command) throws Exception{
		ModelAndView result = null;
		try {
			SessionContext ctx = this.getServiceLocator().doService(command.getLogonUser(), this.getServiceId(), AdmUserFormDTO.ACTION_OPEN_LOG_DIALOG, command);
			command = (SystemLogFormDTO) ctx.getResponseResult();
			if((command.getLogCategre().equals(IAtomsConstants.ACTION_QUERY) || command.getLogCategre().equals(IAtomsConstants.ACTION_DELETE)) && (IAtomsConstants.UC_NO_ADM_01010.equals(command.getFunctionName()))){
				result = new ModelAndView(AdmUserFormDTO.PARAM_LIST_ADM_USER, IAtomsConstants.PARAM_SESSION_CONTEXT, ctx);
			} else if((command.getLogCategre().equals(IAtomsConstants.ACTION_UPDATE) || command.getLogCategre().equals(IAtomsConstants.ACTION_SAVE) || command.getLogCategre().equals(IAtomsConstants.PARAM_CREATE + IAtomsConstants.MARK_NO + IAtomsConstants.ACTION_SAVE)|| command.getLogCategre().equals(IAtomsConstants.PARAM_UPDATE + IAtomsConstants.MARK_NO + IAtomsConstants.ACTION_SAVE)) && (IAtomsConstants.UC_NO_ADM_01010.equals(command.getFunctionName()))){
				result = new ModelAndView(AdmUserFormDTO.PARAM_EDIT_ADM_USER, IAtomsConstants.PARAM_SESSION_CONTEXT, ctx);
			} else if((command.getLogCategre().equals(IAtomsConstants.ACTION_QUERY) || command.getLogCategre().equals(IAtomsConstants.ACTION_DELETE) || command.getLogCategre().equals(IAtomsConstants.ACTION_UPDATE) || command.getLogCategre().equals(IAtomsConstants.ACTION_SAVE) || command.getLogCategre().equals(IAtomsConstants.PARAM_CREATE + IAtomsConstants.MARK_NO + IAtomsConstants.ACTION_SAVE)|| command.getLogCategre().equals(IAtomsConstants.PARAM_UPDATE + IAtomsConstants.MARK_NO + IAtomsConstants.ACTION_SAVE)) 
					&& (IAtomsConstants.UC_NO_ADM_01030.equals(command.getFunctionName()))){
				result = new ModelAndView(AdmRoleFormDTO.PARAM_LIST_ADM_ROLE, IAtomsConstants.PARAM_SESSION_CONTEXT, ctx);
			} else if((command.getLogCategre().equals(IAtomsConstants.ACTION_INIT_DETAIL) || command.getLogCategre().equals(IAtomsConstants.ACTION_SAVE_DETAIL)) && (IAtomsConstants.UC_NO_ADM_01030.equals(command.getFunctionName()))){
				result = new ModelAndView(AdmRoleFormDTO.PARAM_DETAIL_ADM_ROLE, IAtomsConstants.PARAM_SESSION_CONTEXT, ctx);
			}
		} catch (Exception e) {
			logger.error("SystemLogController.openLogDialog() is error: ", e);
		}
		return result;
	}
}
