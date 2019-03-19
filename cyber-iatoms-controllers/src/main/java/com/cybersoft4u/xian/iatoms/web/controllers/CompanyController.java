package com.cybersoft4u.xian.iatoms.web.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.CompanyFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;
import com.cybersoft4u.xian.iatoms.common.utils.ReportExporter;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;

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

/**
 * Purpose: 公司基本訊息維護Controller
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2016年7月1日
 * @MaintenancePersonnel ElvaHe
 */
public class CompanyController extends AbstractMultiActionController<CompanyFormDTO>{

	/**
	 * 日誌記錄物件
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.CONTROLLER, CompanyController.class);
	
	/**
	 * Constructor:无參構造函數
	 */
	public CompanyController() {
		this.setCommandClass(CompanyFormDTO.class);
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(CompanyFormDTO command)throws CommonException {
		if (command == null) {
			return false;
		}
		String actionId = command.getActionId();
		Message msg = null;
		//驗證新增頁面的輸入
		if (IAtomsConstants.ACTION_SAVE.equals(actionId)) {
			String companyId = command.getCompanyId();
			//獲取公司類型
			String companyType = command.getCompanyDTO().getCompanyType();
			String[] companyTypes = null;
			//驗證公司類型 -- 必輸
			if (!StringUtils.hasText(companyId)) {
				if (!StringUtils.hasText(companyType)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_TYPE_COMPANY_TYPE)});
					throw new CommonException(msg);
				} else {
					//存在公司類型時
					if (StringUtils.hasText(companyType)) {
						//按“，”拆分公司類型
						companyTypes = companyType.split(IAtomsConstants.MARK_SEPARATOR);
					}
				}
			}
			//公司代號 --- 必輸、長度小於10
			String companyCode = command.getCompanyDTO().getCompanyCode();
			if (!StringUtils.hasText(companyId)) {
				//必輸
				if (!StringUtils.hasText(companyCode)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_TYPE_COMPANY_CODE)});
					throw new CommonException(msg);
				} else {
					//當長度大於10時
					if (!ValidateUtils.length(companyCode, 0, 10)) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_TYPE_COMPANY_CODE),
										IAtomsConstants.MAXLENGTH_NUMBER_TEN});
						throw new CommonException(msg);
					}
				}
			}
			//公司簡稱 --- 必輸、長度小於20
			String shortName = command.getCompanyDTO().getShortName();
			if (!StringUtils.hasText(shortName)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_SHORT_NAME)});
				throw new CommonException(msg);
			} else {
				//當長度大於20時
				if (!ValidateUtils.length(shortName, 0, 20)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_SHORT_NAME),
							IAtomsConstants.MAXLENGTH_NUMBER_TWENTY});
					throw new CommonException(msg);
				}
			}
			//統一編號 --- 長度小於8
			String unityNumber = command.getCompanyDTO().getUnityNumber();
			if ((StringUtils.hasText(unityNumber)) && (!ValidateUtils.length(unityNumber, 0, 8))) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_UNITY_NUMBER),
						IAtomsConstants.MAXLENGTH_NUMBER_EIGHT});
				throw new CommonException(msg);
			}
			//發票抬頭 --- 長度小於50
			String invoiceHeader = command.getCompanyDTO().getInvoiceHeader();
			if ((StringUtils.hasText(invoiceHeader)) && (!ValidateUtils.length(invoiceHeader, 0, 50))) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_INVOICE_HEADER),
						IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
				throw new CommonException(msg);
			}
			//負責人 --- 長度小於50
			String leader = command.getCompanyDTO().getLeader();
			if ((StringUtils.hasText(leader)) && (!ValidateUtils.length(leader, 0, 50))) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_LEADER),
						IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
				throw new CommonException(msg);
			}
			//公司電話 --- 長度小於20
			String tel = command.getCompanyDTO().getTel();
			if ((StringUtils.hasText(tel)) && (!ValidateUtils.length(tel, 0, 20))) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_TEL),
						IAtomsConstants.MAXLENGTH_NUMBER_TWENTY});
				throw new CommonException(msg);
			}
			//公司傳真 --- 長度小於20
			String fax = command.getCompanyDTO().getFax();
			if ((StringUtils.hasText(fax)) && (!ValidateUtils.length(fax, 0, 20))) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_FAX),
						IAtomsConstants.MAXLENGTH_NUMBER_TWENTY});
				throw new CommonException(msg);
			}
			//請款日 --- 長度小於20
			String applyDate = command.getCompanyDTO().getApplyDate();
			if ((StringUtils.hasText(applyDate)) && (!ValidateUtils.length(applyDate, 0, 20))) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_APPLY_DATE),
						IAtomsConstants.MAXLENGTH_NUMBER_TWENTY});
				throw new CommonException(msg);
			}
			//付款日 --- 長度小於20
			String payDate = command.getCompanyDTO().getPayDate();
			if ((StringUtils.hasText(payDate)) && (!ValidateUtils.length(payDate, 0, 20))) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_PAY_DATE),
						IAtomsConstants.MAXLENGTH_NUMBER_TWENTY});
				throw new CommonException(msg);
			}
			//聯絡人 --- 長度小於50
			String contact = command.getCompanyDTO().getContact();
			if ((StringUtils.hasText(contact)) && (!ValidateUtils.length(contact, 0, 50))) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_CONTACT),
						IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
				throw new CommonException(msg);
			}
			//聯絡人電話 --- 長度小於20
			String contactTel = command.getCompanyDTO().getContactTel();
			if ((StringUtils.hasText(contactTel)) && (!ValidateUtils.length(contactTel, 0, 20))) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_CONTACT_TEL),
						IAtomsConstants.MAXLENGTH_NUMBER_TWENTY});
				throw new CommonException(msg);
			}
			//聯絡人Email --- 長度小於50、驗證email格式
			String contactEmail = command.getCompanyDTO().getContactEmail();
			if (StringUtils.hasText(contactEmail)) {
				//當email格式不對或者長度大於50時
				if ((!ValidateUtils.email(contactEmail)) || (!ValidateUtils.length(contactEmail, 0, 50))) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_CONTACT_EMAIL),
							IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
					throw new CommonException(msg);
				}
			}
			//公司Email --- 長度小於50、驗證email格式
			String companyEmail = command.getCompanyDTO().getCompanyEmail();
			if (StringUtils.hasText(companyEmail)) {
				//當email格式不對或者長度大於50時
				if ((!ValidateUtils.length(companyEmail, 0, 255))) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_COMPANY_EMAIL),
							IAtomsConstants.MAXLENGTH_NUMBER_TWO_HUNDRED_FIFTY_FIVE});
					throw new CommonException(msg);
				}
				//驗證格式
				String[] email = companyEmail.split(IAtomsConstants.MARK_SEMICOLON);
				for(int i = 0; i < email.length; i++) {
					if((!ValidateUtils.email(email[i]))) {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_VALUE_IS_ERROR, 
								new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_COMPANY_EMAIL)});
						throw new CommonException(msg);
					}
				}
			}
			//登入驗證方式 -- 必輸
			String authenticationType = command.getCompanyDTO().getAuthenticationType();
			if (!StringUtils.hasText(companyId)) {
				if (!StringUtils.hasText(authenticationType)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_AUTHENTICATION_TYPE)});
					throw new CommonException(msg);
				}
			}
			
			//客戶碼 --- 當公司類型有客戶，登入驗證方式為iATOMS時必輸，長度小於1，且不能為空格
			String customerCode = command.getCompanyDTO().getCustomerCode();
			if (!StringUtils.hasText(companyId)) {
				if (StringUtils.hasText(companyType)) {
					//當公司類型存在客戶時	
					if (IAtomsConstants.CUSTOMER_ROLE_ATTRIBUTE.equals(companyTypes[0])) {
						//當登入驗證方式為iatoms驗證時	
						if (IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE.equals(authenticationType)) {
							//當存在客戶碼時，判斷客戶碼的長度是否為1，輸入的是否為空格
							if(StringUtils.hasText(customerCode)){
								//判斷輸入的長度
								if(!ValidateUtils.length(customerCode, 0, 1)){
									msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
											new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_CUSTOMER_CODE),
											IAtomsConstants.MAXLENGTH_NUMBER_ONE});
									throw new CommonException(msg);
								} else if(IAtomsConstants.MARK_SPACE.equals(customerCode)){
									//輸入空格時
									msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_FORMAT_ERROR, 
											new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_CUSTOMER_CODE)});
									throw new CommonException(msg);
								}
							} else {
								//驗證客戶碼的必輸
								msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
										new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_CUSTOMER_CODE)});
								throw new CommonException(msg);
							}
						}
					}
				}
			}
			
			//客戶DTID方式 --- 當公司類型有客戶，登入驗證方式為iATOMS時必輸
			String dtidType = command.getCompanyDTO().getDtidType();
			if (!StringUtils.hasText(companyId)) {
				if (StringUtils.hasText(companyType)) {
						if (IAtomsConstants.CUSTOMER_ROLE_ATTRIBUTE.equals(companyTypes[0])) {
							if (IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE.equals(authenticationType)) {
								if (!StringUtils.hasText(dtidType)) {
									msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
											new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_DTID_TYPE)});
									throw new CommonException(msg);
								}
							}
						}
					}
				}
			//公司地址 --- 下拉框，輸入框長度小於100
			String address = command.getCompanyDTO().getAddress();
			if ((StringUtils.hasText(address)) && (!ValidateUtils.length(address, 0, 100))) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_COMPANY_ADDRESS),
						IAtomsConstants.MAXLENGTH_NUMBER_ONE_HUNDRED});
				throw new CommonException(msg);
			}
			//發票地址 --- 下拉框，輸入框長度小於100
			String invoiceAddress = command.getCompanyDTO().getInvoiceAddress();
			if ((StringUtils.hasText(invoiceAddress)) && (!ValidateUtils.length(invoiceAddress, 0, 100))) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_INVOICE_ADDRESS),
						IAtomsConstants.MAXLENGTH_NUMBER_ONE_HUNDRED});
				throw new CommonException(msg);
			}
			//說明 --- 長度小於200
			String remark = command.getCompanyDTO().getRemark();
			if ((StringUtils.hasText(remark)) && (!ValidateUtils.length(remark, 0, 200))) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_COMPANY_REMARK),
						IAtomsConstants.MAXLENGTH_NUMBER_TWO_HUNDRED});
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
	public CompanyFormDTO parse(HttpServletRequest request,CompanyFormDTO command) throws CommonException {
		try {
			if(command == null) {
				command = new CompanyFormDTO();
			}
			// 获取actionId
			String actionId = this.getString(request, IAtomsConstants.FIELD_ACTION_ID);
			//無actionId時設置actionId為init
			if (!StringUtils.hasText(actionId)) {
				actionId = IAtomsConstants.ACTION_INIT;
			}
			command.setActionId(actionId);
			//當actionId為save時，解析傳遞的數據
			if(actionId.equals(IAtomsConstants.ACTION_SAVE)){
				CompanyDTO bimCompanyDTO = BindPageDataUtils.bindValueObject(request, CompanyDTO.class);
				command.setCompanyDTO(bimCompanyDTO);
			}
			return command;
		} catch (Exception e) {
			if(LOGGER != null){
				LOGGER.error("parse()", "Exception", e);
			}
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
	}
	
	/**
	 * 
	 * Purpose: 系統日志查詢結果匯出
	 * @author ElvaHe
	 * @param request 請求對象
	 * @param response 響應對象
	 * @param command formDTO：傳遞的信息
	 * @throws CommonException：出錯時拋出異常
	 * @return void
	 */
	public ModelAndView export(HttpServletRequest request, HttpServletResponse response,CompanyFormDTO command) throws CommonException{
		// 取得sessionId
		String sessionId = request.getSession().getId();
		try{
			if(command != null){
				//獲取登錄者信息
				IAtomsLogonUser logonUser = (IAtomsLogonUser) command.getLogonUser();
				JasperReportCriteriaDTO criteria = new JasperReportCriteriaDTO();
				//請求service進行數據查詢
				SessionContext returnCtx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_COMPANY_SERVICE, IAtomsConstants.ACTION_EXPORT, command);
				List<CompanyDTO> results = null;
				//當存在查詢結果時，將查詢結果設置到要匯出的對象中去
				if(returnCtx != null){
					command = (CompanyFormDTO) returnCtx.getResponseResult();
					results = command.getList();
					//封裝報表對象
					if(!CollectionUtils.isEmpty(results)){
						// 是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
						criteria.setAutoBuildJasper(false);
						criteria.setResult(results);
						//設置所需報表的Name
						criteria.setJrxmlName(CompanyFormDTO.REPORT_JRXML_NAME);
						//設置報表路徑
						criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
						//設置sheetName
						criteria.setSheetName(i18NUtil.getName(IAtomsMessageCode.COMPANY_REPORT_SHEET_NAME));
						//設置匯出格式
						criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
						//設置報表Name
						criteria.setReportFileName(i18NUtil.getName(IAtomsMessageCode.COMPANY_REPORT_SHEET_NAME));
						ReportExporter.exportReport(criteria, response);
						// 成功標志
						SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, IAtomsConstants.YES);
					}
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
			if(LOGGER != null){
				LOGGER.error("export()", "error", e);
			}
			throw new CommonException(IAtomsMessageCode.EXPORT_REPORT_FAILURE);
		}
		return null;
	}
}
