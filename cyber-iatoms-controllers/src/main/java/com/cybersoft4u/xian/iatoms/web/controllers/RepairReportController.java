package com.cybersoft4u.xian.iatoms.web.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.cybersoft4u.xian.iatoms.common.bean.dto.RepairReportDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.RepoStatusReportDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ApplicationFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetTransInfoHistoryFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.CustomerRepoFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.RepairReportFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.RepoStatusReportFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.CrossTabReportDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;
import com.cybersoft4u.xian.iatoms.common.utils.ReportExporter;
/**
 * Purpose: 報修問題分析報表Controller
 * @author echomou
 * @since  JDK 1.6
 * @date   2016/11/14
 * @MaintenancePersonnel echomou
 */
@SuppressWarnings({ "unused", "rawtypes" })
public class RepairReportController extends AbstractMultiActionController<RepairReportFormDTO> {
	

	/**
	 * 日志记录物件
	 */
	private static final CafeLog logger = CafeLogFactory.getLog(MerchantHeaderController.class);
	
	/**
	 * Constructor:无参构造函数
	 */
	public RepairReportController() {
		this.setCommandClass(RepairReportFormDTO.class);
	}

	/**
	 * 報修問題分析之原因類別
	 */
	private Map<String, String> solveType;
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public RepairReportFormDTO parse(HttpServletRequest request, RepairReportFormDTO command) throws CommonException {
		try {
			String actionId = command.getActionId();
			command.setActionId(actionId);
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ".controller Exception:", e);
		}
		return command;
	}
	
	/**
	 * Purpose : 匯出報修問題分析報表
	 * @author echomou
	 * @param request ：HttpServletRequest
	 * @param response ：HttpServletResponse
	 * @param command ：RepairReportFormDTO
	 * @throws CommonException
	 * @return void ：出錯時拋出CommonException
	 */
	public ModelAndView exportList(HttpServletRequest request, HttpServletResponse response, RepairReportFormDTO command) throws CommonException{
		// 取得sessionId
		String sessionId = request.getSession().getId();
		try {
			//根据条件查询报表数据
			SessionContext sessionContext = this.doService(this.getServiceId(), IAtomsConstants.ACTION_EXPORT, command);
			if (sessionContext != null) {
				command = (RepairReportFormDTO) sessionContext.getResponseResult();
				List<RepairReportDTO> results = command.getList();
				//List<JasperReportCriteriaDTO> criterias = new ArrayList<JasperReportCriteriaDTO>();
				JasperReportCriteriaDTO criteria = new JasperReportCriteriaDTO();
				/*Map<String, List<RepairReportDTO>> map = new HashMap<String, List<RepairReportDTO>>();
				List<RepairReportDTO> list = null;
				String customerName = null;
				*/
				//int tempIndex = 0;
				//String customerName = "";
				if (!CollectionUtils.isEmpty(results)) {
					/*//JasperReport條件設定DTO
					JasperReportCriteriaDTO criteria = null;
					criteria = new JasperReportCriteriaDTO();
					// 是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
					criteria.setAutoBuildJasper(false);
					//子報表的map
					Map<String, String> subjrXmlNames = new HashMap<String, String>();
					subjrXmlNames.put(RepairReportFormDTO.REPAIR_REPORT_SUBREPORT, RepairReportFormDTO.SUBREPORT_DIR);
					List<RepairReportFormDTO> resultFormDTO = new ArrayList<RepairReportFormDTO>();
					resultFormDTO.add(command);
					criteria.setResult(resultFormDTO);
					//封裝報表對象
					criteria.setResult(results);
					//設置報表路徑
					criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
					//設置匯出格式
					criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
					//公司簡稱
					if (results.get(tempIndex) != null) {
						customerName = results.get(tempIndex).getCustomerName();
					}
					criterias.add(criteria);
				}
				ReportExporter.exportReportForSheets(criterias, response);*/
					// 是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
					criteria.setAutoBuildJasper(false);
					criteria.setResult(results);
					//設置所需報表的Name
					criteria.setJrxmlName(RepairReportFormDTO.PROJECT_REPORT_JRXML_NAME);
					//設置報表路徑
					criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
					//設置匯出格式
					criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
					//設置報表Name
					criteria.setReportFileName(RepairReportFormDTO.REPORT_FILE_NAME);
					criteria.setSheetName(RepairReportFormDTO.REPORT_FILE_NAME);
					ReportExporter.exportReport(criteria, response);
					// 成功標志
					SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, IAtomsConstants.YES);
				}
			}
				
		} catch(Exception e) {
			// 出錯消息
			try {
				SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, false);
				Map map = new HashMap();
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.EXPORT_ERROR_MESSAGE));
				return new ModelAndView(this.getFailureView(IAtomsConstants.ACTION_EXPORT_LIST), map);
			} catch (Exception e1) {
				logger.error(".export() is error:", e1);
			}
			logger.error(this.getClass().getName() + ".export() is error: " + e, e);
		}
		return null;
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(RepairReportFormDTO command) throws CommonException {
		return true;
	}

	/**
	 * @return the solveType
	 */
	public Map<String, String> getSolveType() {
		return solveType;
	}

	/**
	 * @param solveType the solveType to set
	 */
	public void setSolveType(Map<String, String> solveType) {
		this.solveType = solveType;
	}
}
