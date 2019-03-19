package com.cybersoft4u.xian.iatoms.web.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import cafe.core.bean.Message;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.SessionContext;
import cafe.core.exception.CommonException;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.BindPageDataUtils;
import cafe.core.web.controller.util.SessionHelper;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.PvmDtidDefDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.DtidDefFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;
import com.cybersoft4u.xian.iatoms.common.utils.ReportExporter;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;
/**
 * Purpose:  DTID帳號管理FormDTO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2016/9/21
 * @MaintenancePersonnel CarrieDuan
 */
public class DtidDefController extends AbstractMultiActionController<DtidDefFormDTO>{

	/**
	 * 日誌記錄器
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.CONTROLLER, DtidDefController.class);
	
	/**
	 * Constructor:無參構造
	 */
	public DtidDefController() {
		this.setCommandClass(DtidDefFormDTO.class);
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(DtidDefFormDTO command) throws CommonException {
		if (command == null) {
			return false;
		}
		String actionId = command.getActionId();
		Message msg = null;
		if (IAtomsConstants.ACTION_SAVE.equals(actionId)) {
			PvmDtidDefDTO pvmDtidDefDTO = command.getDtidDefDTO();
			String dtidId = pvmDtidDefDTO.getId();
			//客戶
			String customerId = pvmDtidDefDTO.getCompanyId();
			//設備名稱
			String assetTypeId = pvmDtidDefDTO.getAssetTypeId();
			//dtid起
			String dtidStart = pvmDtidDefDTO.getDtidStart();
			//dtid止
			String dtidEnd = pvmDtidDefDTO.getDtidEnd();
			//獲取說明
			String comment = pvmDtidDefDTO.getComment();
			//核檢客戶是否輸入
			if (!StringUtils.hasText(customerId)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_PVM_DTID_DEF_COMPANY_ID)});
				throw new CommonException(msg);
			}
			//核檢設備是否輸入
			if (!StringUtils.hasText(assetTypeId)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_PVM_DTID_DEF_ASSET_TYPE_ID)});
				throw new CommonException(msg);
			}
			if (!StringUtils.hasText(dtidId) || (StringUtils.hasText(dtidId) && !StringUtils.hasText(pvmDtidDefDTO.getCurrentNumber()))) {
				//核檢DTID起是否輸入
				if (!StringUtils.hasText(dtidStart)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_PVM_DTID_DEF_DTID_START)});
					throw new CommonException(msg);
				} else {
					//核檢DTID長度與輸入是否正確
					if (!ValidateUtils.equalsLength(dtidStart, 8) || !ValidateUtils.numberOrEnglish(dtidStart)) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_DTID_START_ERROR);
						throw new CommonException(msg);
					}
				}
			}
			//核檢DTID止是否輸入
			if (!StringUtils.hasText(dtidEnd)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_PVM_DTID_DEF_DTID_END)});
				throw new CommonException(msg);
			} else {
				//核檢DTID長度與輸入是否正確
				if (!ValidateUtils.equalsLength(dtidEnd, 8) || !ValidateUtils.numberOrEnglish(dtidEnd)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_DTID_END_ERROR);
					throw new CommonException(msg);
				}
			}
			//核檢說明長度是否正確
			if (StringUtils.hasText(comment)) {
				if (!ValidateUtils.length(comment, 0, 200)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_PVM_DTID_DEF_COMMENT), IAtomsConstants.DESCRIPTION_LENGTH});
					throw new CommonException(msg);
				}
			}
		}
		return true;
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractMultiActionController#parse(javax.servlet.http.HttpServletRequest, cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public DtidDefFormDTO parse(HttpServletRequest request, DtidDefFormDTO command) throws CommonException {
		try {
			PvmDtidDefDTO dtidDefDTO = null;
			String actionId = command.getActionId(); 
			if (IAtomsConstants.ACTION_SAVE.equals(actionId) || IAtomsConstants.ACTION_CHECK.equals(actionId)) {
				dtidDefDTO = BindPageDataUtils.bindValueObject(request, PvmDtidDefDTO.class);
				command.setDtidDefDTO(dtidDefDTO);
			} 
			if (IAtomsConstants.ACTION_QUERY.equals(actionId)) {
				String sort = this.getString(request, IAtomsConstants.QUERY_PAGE_SORT);
				if (StringUtils.hasText(sort)) {
					command.setSort(sort);
				} else {
					command.setSort(PvmDtidDefDTO.ATTRIBUTE.COMPANY_NAME.getValue().concat(IAtomsConstants.MARK_SEPARATOR) 
								.concat(PvmDtidDefDTO.ATTRIBUTE.ASSET_NAME.getValue()));
				}
				String order = this.getString(request, IAtomsConstants.QUERY_PAGE_ORDER);
				if (StringUtils.hasText(order)) {
					command.setOrder(order);
				} else {
					command.setOrder(IAtomsConstants.PARAM_PAGE_ORDER);
				}
			}
		} catch (Exception e) {
			LOGGER.error(".parse() Exception.", e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
		return command;
	}
	/**
	 * Purpose: 匯出
	 * @author Amanda Wang
	 * @param request: 請求
	 * @param response: 響應
	 * @param command: AssetTransInfoFormDTO
	 * @throws CommonException：出錯時拋出CommonException
	 * @return void
	 */
	@SuppressWarnings("rawtypes")
	public ModelAndView export(HttpServletRequest request, HttpServletResponse response, DtidDefFormDTO command) throws CommonException{
		// 取得sessionId
		String sessionId = request.getSession().getId();
		try{
			if(command != null){
				command.setRows(Integer.valueOf(-1));
				command.setPage(Integer.valueOf(-1));
				command.setSort(PvmDtidDefDTO.ATTRIBUTE.COMPANY_NAME.getValue().concat(IAtomsConstants.MARK_SEPARATOR) 
						.concat(PvmDtidDefDTO.ATTRIBUTE.ASSET_NAME.getValue()));
				command.setOrder(IAtomsConstants.PARAM_PAGE_ORDER);
				IAtomsLogonUser logonUser = (IAtomsLogonUser) command.getLogonUser();
				JasperReportCriteriaDTO criteria = new JasperReportCriteriaDTO();
				//請求service進行數據查詢
				SessionContext returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_DTID_DEF_SERVICE, IAtomsConstants.ACTION_QUERY, command);
				List<PvmDtidDefDTO> results = null;
				if(returnCtx != null){
					command = (DtidDefFormDTO) returnCtx.getResponseResult();
					results = command.getList();
				}
				//封裝報表對象
				if(!CollectionUtils.isEmpty(results)){
					// 是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
					criteria.setAutoBuildJasper(false);
					criteria.setResult(results);
					//設置所需報表的Name
					criteria.setJrxmlName(DtidDefFormDTO.REPORT_JRXML_NAME);
					//設置報表路徑
					criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
					//設置匯出格式
					criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
					criteria.setSheetName(DtidDefFormDTO.REPORT_FILE_NAME);
					//設置報表Name
					String fileName = DtidDefFormDTO.REPORT_FILE_NAME;
					criteria.setReportFileName(fileName);
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
				LOGGER.error(".export() is error:", e1);
			}
			LOGGER.error(".export() is error: ", e);
		}
		return null;
	}
}
