package com.cybersoft4u.xian.iatoms.web.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.utils.FileUtils;
import com.cybersoft4u.xian.iatoms.common.utils.ReportExporter;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ComplaintDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ComplaintFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;

import cafe.core.bean.Message;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.SessionContext;
import cafe.core.exception.CommonException;
import cafe.core.service.ServiceException;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.BindPageDataUtils;
import cafe.core.web.controller.util.SessionHelper;

/**
 * Purpose: 客訴管理Controller
 * @author  nicklin
 * @since  JDK 1.7
 * @date   2018/03/01
 * @MaintenancePersonnel cybersoft
 */
public class ComplaintController extends AbstractMultiActionController<ComplaintFormDTO> {
	
	/**
	 * 日誌記錄物件
	 */
	private static CafeLog logger = CafeLogFactory.getLog(GenericConfigManager.CONTROLLER, ComplaintController.class);
	
	/**
	 * Constructor:無參數建構子
	 */
	public ComplaintController() {
		this.setCommandClass(ComplaintFormDTO.class);
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(ComplaintFormDTO command) throws CommonException {
		// TODO Auto-generated method stub
		if (command == null) {
			return false;
		}
		String actionId = command.getActionId();
		Message msg = null;
		if (IAtomsConstants.ACTION_SAVE.equals(actionId)) {
			//檢驗頁面輸入
			ComplaintDTO complaintDTO = command.getComplaintDTO();
			if (complaintDTO == null) {
				return false;
			} else {
				//申訴人員
				String complaintStaff = complaintDTO.getComplaintStaff();
				if (!StringUtils.hasText(complaintStaff)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_COMPLAINT_STAFF)});
					throw new CommonException(msg);
				} else {
					if (!ValidateUtils.length(complaintStaff, 0, 50)) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_COMPLAINT_STAFF),
								IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
						throw new CommonException(msg);
					}
				}
				//發生日期
				Date complaintDate = complaintDTO.getComplaintDate();
				if (complaintDate == null) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_COMPLAINT_DATE)});
					throw new CommonException(msg);
				}
				//聯繫方式
				String contactWay = complaintDTO.getContactWay();
				if (StringUtils.hasText(contactWay) && !ValidateUtils.length(contactWay, 0, 100)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_COMPLAINT_CONTACT_WAY),
							IAtomsConstants.MAXLENGTH_NUMBER_ONE_HUNDRED});
					throw new CommonException(msg);
				}
				//客戶
				String customerId = complaintDTO.getCustomerId();
				if (!StringUtils.hasText(customerId)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_COMPLAINT_CUSTOMER)});
					throw new CommonException(msg);
				}
				//特店代號
				String merchantId = complaintDTO.getMerchantId();
				if (!StringUtils.hasText(merchantId)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_COMPLAINT_MERCHANT_ID)});
					throw new CommonException(msg);
				} else {
					if (!ValidateUtils.length(merchantId, 0, 20)) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_COMPLAINT_MERCHANT_ID),
								IAtomsConstants.MAXLENGTH_NUMBER_TWENTY});
						throw new CommonException(msg);
					}
				}
				//TID/DTID
				String tid = complaintDTO.getTid();
				if (!StringUtils.hasText(tid)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_COMPLAINT_TID)});
					throw new CommonException(msg);
				} else {
					if (!ValidateUtils.length(tid, 0, 8)) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_COMPLAINT_TID),
								IAtomsConstants.MAXLENGTH_NUMBER_EIGHT});
						throw new CommonException(msg);
					}
				}
				//客訴內容
				String complaintContent = complaintDTO.getComplaintContent();
				if (StringUtils.hasText(complaintContent) && !ValidateUtils.length(complaintContent, 0, 2000)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_COMPLAINT_CONTENT),
							IAtomsConstants.MAXLENGTH_NUMBER_TWO_THOUSAND});
					throw new CommonException(msg);
				}
				//處理說明
				String handleContent = complaintDTO.getHandleContent();
				if (StringUtils.hasText(handleContent) && (!ValidateUtils.length(handleContent, 0, 2000))) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_COMPLAINT_REMARK),
							IAtomsConstants.MAXLENGTH_NUMBER_TWO_THOUSAND});
					throw new CommonException(msg);
				}
				//歸責廠商
				String companyId = complaintDTO.getCompanyId();
				if (!StringUtils.hasText(companyId)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_COMPLAINT_COMPANY)});
					throw new CommonException(msg);
				}
				//歸責人員
				String userName = complaintDTO.getUserName();
				if (StringUtils.hasText(userName) && (!ValidateUtils.length(userName, 0, 100))) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_COMPLAINT_USER_NAME),
							IAtomsConstants.MAXLENGTH_NUMBER_ONE_HUNDRED});
					throw new CommonException(msg);
				}
				//問題分類
				String questionType = complaintDTO.getQuestionType();
				if (!StringUtils.hasText(questionType)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_COMPLAINT_QUESTION_TYPE)});
					throw new CommonException(msg);
				}
				//賠償金額
				Integer customerAmount = complaintDTO.getCustomerAmount();
				if (StringUtils.hasText(String.valueOf(customerAmount)) && !ValidateUtils.length(String.valueOf(customerAmount), 0, 7)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_COMPLAINT_CUSTOMER_AMOUNT),
							IAtomsConstants.MAXLENGTH_NUMBER_SEVEN});
					throw new CommonException(msg);
				}
				//罰款金額
				Integer vendorAmount = complaintDTO.getVendorAmount();
				if (StringUtils.hasText(String.valueOf(vendorAmount)) && !ValidateUtils.length(String.valueOf(vendorAmount), 0, 7)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_COMPLAINT_VENDOR_AMOUNT),
							IAtomsConstants.MAXLENGTH_NUMBER_SEVEN});
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
	public ComplaintFormDTO parse(HttpServletRequest request, ComplaintFormDTO command)
			throws CommonException {
		// TODO Auto-generated method stub
		try {
			if (command == null) {
				command = new ComplaintFormDTO();
			}
			//獲取actionId
			String actionId = this.getString(request, IAtomsConstants.FIELD_ACTION_ID);
			if (!StringUtils.hasText(actionId)) {
				actionId = IAtomsConstants.ACTION_INIT;
			} else if (IAtomsConstants.ACTION_QUERY.equals(actionId)) {
				//查詢條件	
				String sort = this.getString(request, IAtomsConstants.QUERY_PAGE_SORT);
				if (StringUtils.hasText(sort)) {
					command.setSort(sort);
				}
				String order = this.getString(request, IAtomsConstants.QUERY_PAGE_ORDER);			
				if (StringUtils.hasText(order)) {
					command.setOrder(order);
				} else {
					command.setOrder(IAtomsConstants.PARAM_PAGE_ORDER);
				}
			} else if (IAtomsConstants.ACTION_SAVE.equals(actionId)) {
				ComplaintDTO complaintDTO = command.getComplaintDTO();
				//綁定頁面值對象
				complaintDTO = BindPageDataUtils.bindValueObject(request, ComplaintDTO.class);
				command.setComplaintDTO(complaintDTO);
			} else if (IAtomsConstants.ACTION_SAVE_FILE.equals(actionId)) {
				// 有文件上傳時，儲存文件
				if(request instanceof MultipartHttpServletRequest){	
					MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
					Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
					command.setFileMap(fileMap);
				}
			}
			command.setActionId(actionId);
			return command;
		} catch (Exception e) {
			if (logger != null) {
				logger.error("parse()", "is error: ", e);
			}
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
	}
	
	/**
	 * Purpose:查詢結果匯出
	 * @author nicklin
	 * @param  request:請求對象
	 * @param  response:響應對象 
	 * @param  command:ComplaintFormDTO
	 * @throws CommonException:出錯時拋出CommonException
	 * @return ModelAndView
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ModelAndView export(HttpServletRequest request, HttpServletResponse response, ComplaintFormDTO command) throws CommonException {
		// 取得sessionId
		String sessionId = request.getSession().getId();
		try {
			if (command != null) {
				IAtomsLogonUser logonUser = (IAtomsLogonUser) command.getLogonUser();
				JasperReportCriteriaDTO criteria = new JasperReportCriteriaDTO();
				SessionContext ctx = this.getServiceLocator().doService(logonUser, IAtomsConstants.SERVICE_COMPLAINT_SERVICE, IAtomsConstants.ACTION_EXPORT, command);
				List<ComplaintDTO> complaintDTOs = null;
				if (ctx != null) {
					command = (ComplaintFormDTO) ctx.getResponseResult();
					complaintDTOs = command.getList();
				}
				if (!CollectionUtils.isEmpty(complaintDTOs)) {
					// 是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
					criteria.setAutoBuildJasper(false);
					criteria.setResult(complaintDTOs);
					//設置所需報表的Name
					criteria.setJrxmlName(ComplaintFormDTO.PROJECT_REPORT_JRXML_NAME);
					//設置報表路徑
					criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
					//設置匯出格式
					criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
					//設置報表Name
					criteria.setReportFileName(ComplaintFormDTO.PROJECT_REPORT_FILE_NAME);
					criteria.setSheetName(ComplaintFormDTO.PROJECT_REPORT_FILE_NAME);
					ReportExporter.exportReport(criteria, response);
					//成功標誌
					SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, IAtomsConstants.YES);
				}
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
				logger.error(".export() is error:", e1);
			}
			logger.error("export()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.EXPORT_REPORT_FAILURE, e);
		} catch (Exception e) {
			logger.error("export()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return null;
	}
	
	/**
	 * Purpose:附加檔案下載
	 * @author nicklin
	 * @param  request:請求對象
	 * @param  response:響應對象 
	 * @param  command:ComplaintFormDTO
	 * @throws CommonException:出錯時拋出CommonException
	 * @return void
	 */
	@SuppressWarnings("rawtypes")
	public void download(HttpServletRequest request, HttpServletResponse response, ComplaintFormDTO command)throws CommonException{
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_COMPLAINT_SERVICE, IAtomsConstants.ACTION_GET_COMPLAINT_FILE_PATH, command);
			if (sessionContext != null) {
				command = (ComplaintFormDTO) sessionContext.getResponseResult();
				//下載
				FileUtils.download(request, response, command.getFilePath(), command.getFileName());
			}
		} catch (Exception e) {
			logger.error("download()", "is error: ", e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
}
