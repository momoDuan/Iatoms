package com.cybersoft4u.xian.iatoms.web.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;

import cafe.core.config.GenericConfigManager;
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
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.EdcPaymentReportFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;
import com.cybersoft4u.xian.iatoms.common.utils.ReportExporter;
/**
 * 
 * Purpose: EDC維護費用付款報表Controller
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2017/6/16
 * @MaintenancePersonnel HermanWang
 */
public class EdcPaymentReportController extends AbstractMultiActionController<EdcPaymentReportFormDTO>{
	/**
	 * 日志记录物件
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.CONTROLLER, EdcPaymentReportController.class);	
	
	/**
	 * Constructor:无参构造函数
	 */
	public EdcPaymentReportController() {
		this.setCommandClass(EdcPaymentReportFormDTO.class);
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(EdcPaymentReportFormDTO parmemters) throws CommonException {
		return true;
	}

	@Override
	public EdcPaymentReportFormDTO parse(HttpServletRequest request,
			EdcPaymentReportFormDTO command) throws CommonException {
		try {
			if(command != null) {
				//獲取actionId
				String actionId = command.getActionId();
				LOGGER.debug(" .parse() ---> actionId : " + actionId);
			}
		} catch (Exception e) {
			LOGGER.error(".parse() Exception.", e);
		}
		return command;
	}
	/**
	 * 
	 * Purpose: 匯出
	 * @author HermanWang
	 * @param request 請求對象
	 * @param response 相應對象
	 * @param command formDTO
	 * @throws CommonException 錯誤時拋出的Controller異常類
	 * @return void
	 */
	public ModelAndView export(HttpServletRequest request, HttpServletResponse response, EdcPaymentReportFormDTO command) throws CommonException{
		// 取得sessionId
		String sessionId = request.getSession().getId();
		try {
			command.setQueryCustomer(command.getExportQueryCustomer());
			SessionContext sessionContext = this.doService(this.getServiceId(), IAtomsConstants.ACTION_EXPORT, command) ;
			EdcPaymentReportFormDTO formDTO = (EdcPaymentReportFormDTO) sessionContext.getResponseResult();
			List<JasperReportCriteriaDTO> criterias = new ArrayList<JasperReportCriteriaDTO>();
			JasperReportCriteriaDTO criteria = null;
			//裝機
			if(!CollectionUtils.isEmpty(formDTO.getInstallSrmCaseHandleInfoDTOs())) {
				for (SrmCaseHandleInfoDTO srmCaseHandleInfoDTO : formDTO.getInstallSrmCaseHandleInfoDTOs()) {
					srmCaseHandleInfoDTO.setEcrLine(fomatterPositiveInt(srmCaseHandleInfoDTO.getEcrLine()));
					srmCaseHandleInfoDTO.setNetLine(fomatterPositiveInt(srmCaseHandleInfoDTO.getNetLine()));
					srmCaseHandleInfoDTO.setOtherLine(fomatterPositiveInt(srmCaseHandleInfoDTO.getOtherLine()));
					srmCaseHandleInfoDTO.setPosPrice(fomatterPositiveInt(srmCaseHandleInfoDTO.getPosPrice()));
					if(IAtomsConstants.YES.equals(srmCaseHandleInfoDTO.getIsFirstInstalled())) {
						srmCaseHandleInfoDTO.setIsFirstInstalled(i18NUtil.getName(IAtomsConstants.YES));
					} else {
						srmCaseHandleInfoDTO.setIsFirstInstalled(i18NUtil.getName(IAtomsConstants.NO));
					}
				}
				 //获取service返回的值
	    		criteria = new JasperReportCriteriaDTO();
				// 是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
				criteria.setAutoBuildJasper(true);
				//设置结果集
				criteria.setResult(formDTO.getInstallSrmCaseHandleInfoDTOs());
				//設置所需報表的Name
				criteria.setJrxmlName(formDTO.EXPORT_JRXML_INSTALL_NAME);
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				criteria.setSheetName(formDTO.EXPORT_JRXML_INSTALL_SHEET_NAME);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				//設置報表Name
				criteria.setReportFileName(formDTO.EXPORT_JRXML_NAME);
				criterias.add(criteria);
			}
			//拆機
			if(!CollectionUtils.isEmpty(formDTO.getUnInstallSrmCaseHandleInfoDTOs())) {
				for (SrmCaseHandleInfoDTO srmCaseHandleInfoDTO : formDTO.getUnInstallSrmCaseHandleInfoDTOs()) {
					if(StringUtils.hasText(srmCaseHandleInfoDTO.getUserdDays90())) {
						if(Integer.parseInt(srmCaseHandleInfoDTO.getUserdDays90()) < 90) {
							srmCaseHandleInfoDTO.setUserdDays90(i18NUtil.getName(IAtomsConstants.YES));
						} else {
							srmCaseHandleInfoDTO.setUserdDays90(IAtomsConstants.MARK_EMPTY_STRING);
						}
					} 
					if(StringUtils.hasText(srmCaseHandleInfoDTO.getUserdDays120())) {
						if(Integer.parseInt(srmCaseHandleInfoDTO.getUserdDays120()) < 120) {
							srmCaseHandleInfoDTO.setUserdDays120(i18NUtil.getName(IAtomsConstants.YES));
						} else {
							srmCaseHandleInfoDTO.setUserdDays120(i18NUtil.getName(IAtomsConstants.NO));
						}
					} 
				}
				 //获取service返回的值
	    		criteria = new JasperReportCriteriaDTO();
				// 是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
				criteria.setAutoBuildJasper(true);
				//设置结果集
				criteria.setResult(formDTO.getUnInstallSrmCaseHandleInfoDTOs());
				//設置所需報表的Name
				criteria.setJrxmlName(formDTO.EXPORT_JRXML_UNINSTALL_NAME);
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				criteria.setSheetName(formDTO.EXPORT_JRXML_UNINSTALL_SHEET_NAME);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				//設置報表Name
				criteria.setReportFileName(formDTO.EXPORT_JRXML_NAME);
				criterias.add(criteria);
			}
			//其他
			if(!CollectionUtils.isEmpty(formDTO.getOtherSrmCaseHandleInfoDTOs())) {
				for (SrmCaseHandleInfoDTO srmCaseHandleInfoDTO : formDTO.getOtherSrmCaseHandleInfoDTOs()) {
					srmCaseHandleInfoDTO.setEcrLine(fomatterPositiveInt(srmCaseHandleInfoDTO.getEcrLine()));
					srmCaseHandleInfoDTO.setNetLine(fomatterPositiveInt(srmCaseHandleInfoDTO.getNetLine()));
					srmCaseHandleInfoDTO.setOtherLine(fomatterPositiveInt(srmCaseHandleInfoDTO.getOtherLine()));
				}
				 //获取service返回的值
	    		criteria = new JasperReportCriteriaDTO();
				// 是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
				criteria.setAutoBuildJasper(true);
				//设置结果集
				criteria.setResult(formDTO.getOtherSrmCaseHandleInfoDTOs());
				//設置所需報表的Name
				criteria.setJrxmlName(formDTO.EXPORT_JRXML_OTHER_CASE_NAME);
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				criteria.setSheetName(formDTO.EXPORT_JRXML_OTHER_CASE_SHEET_NAME);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				//設置報表Name
				criteria.setReportFileName(formDTO.EXPORT_JRXML_NAME);
				criterias.add(criteria);
			}
			//查核
			if(!CollectionUtils.isEmpty(formDTO.getCheckSrmCaseHandleInfoDTOs())) {
				 //获取service返回的值
	    		criteria = new JasperReportCriteriaDTO();
				// 是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
				criteria.setAutoBuildJasper(true);
				//设置结果集
				criteria.setResult(formDTO.getCheckSrmCaseHandleInfoDTOs());
				//設置所需報表的Name
				criteria.setJrxmlName(formDTO.EXPORT_JRXML_CHECK_NAME);
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				criteria.setSheetName(formDTO.EXPORT_JRXML_CHECK_SHEET_NAME);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				//設置報表Name
				criteria.setReportFileName(formDTO.EXPORT_JRXML_NAME);
				criterias.add(criteria);
			}
			ReportExporter.exportReportForSheets(criterias, response);
			// 成功標志
			SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, IAtomsConstants.YES);
		} catch(Exception e){
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
			LOGGER.error(".export() is error:", e);
		}
		return null;
	}
	/**
	 * Purpose:格式化幾個數據拼接的欄位為 整數-數量-單價，整數-數量-單價
	 * @author HermanWang
	 * @param val：需要格式化的源數據串
	 * @return String：返回一個string的字符串
	 */
	private String fomatterPositiveInt(String val){
		if(!StringUtils.hasText(val)) {
			return IAtomsConstants.MARK_EMPTY_STRING;
		} else {
			String[] stringVal = val.split(",");
			String tempVal = IAtomsConstants.MARK_EMPTY_STRING;
			for(int i =0; i < stringVal.length; i++) {
				if(IAtomsConstants.MARK_EMPTY_STRING.equals(tempVal)) {
					tempVal = stringVal[i].substring(0, stringVal[i].indexOf(".")) + ",";
				} else {
					tempVal += stringVal[i].substring(0, stringVal[i].indexOf(".")) + ",";
				}
			}
			val = tempVal.substring(0, tempVal.length() - 1);
			return val;
		}
	}
}
