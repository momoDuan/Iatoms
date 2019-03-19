package com.cybersoft4u.xian.iatoms.web.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import cafe.core.bean.Message;
import cafe.core.config.SystemConfigManager;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.exception.CommonException;
import cafe.core.service.ServiceException;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.AbstractMultiActionController;
import cafe.core.web.controller.util.BindPageDataUtils;
import cafe.core.web.controller.util.SessionHelper;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.PasswordSettingDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AdmUserFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;
import com.cybersoft4u.xian.iatoms.common.utils.PasswordEncoderUtilities;
import com.cybersoft4u.xian.iatoms.common.utils.ReportExporter;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;
import com.cybersoft4u.xian.iatoms.web.controllers.authenticator.AuthenticatorHelper;

/**
 * 
 * Purpose:  使用者維護Controller
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016/3/30
 * @MaintenancePersonnel CrissZhang
 */
public class AdmUserController extends AbstractMultiActionController<AdmUserFormDTO> {
	/**
	 * 日志记录物件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(AdmUserController.class);
	/**
	 * 识别管理服务类别Service
	 */
	private AuthenticatorHelper authenticatorHelper;
	/**
	 * Constructor:无参构造函数
	 */
	public AdmUserController() {
		this.setCommandClass(AdmUserFormDTO.class);
	}
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.IAtomicController#validate(cafe.core.bean.dto.DataTransferObject)
	 */
	@Override
	public boolean validate(AdmUserFormDTO command) throws CommonException {
		if (command == null) {
			return false;
		}
		String actionId = command.getActionId();
		Message msg = null;
		if (IAtomsConstants.ACTION_SAVE.equals(actionId)) {
			IAtomsLogonUser logonUser = (IAtomsLogonUser) command.getLogonUser();
			//檢驗頁面輸入
			AdmUserDTO admUserDTO = command.getAdmUserDTO();
			if (admUserDTO == null) {
				return false;
			}
			// 檢驗
			String userId = admUserDTO.getUserId();
			String account = admUserDTO.getAccount();
			String companyId = admUserDTO.getCompanyId();
			//若未輸入帳號
			if (!StringUtils.hasText(account)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_USER_ACCOUNT)});
				throw new CommonException(msg);
			} else {
				//帳號長度驗證+帳號字符驗證
				/*if (!ValidateUtils.length(account, 4, 20) || !ValidateUtils.numberOrEnglish(account)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.ACCOUNT_FORMAT_ERROR);
					throw new CommonException(msg);
				}*/
				if (!ValidateUtils.length(account, 4, 20) || !ValidateUtils.inputCharacter(account, false)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.ACCOUNT_FORMAT_ERROR);
					throw new CommonException(msg);
				}
			}
			//若未輸入公司編號
			if (!StringUtils.hasText(companyId)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_USER_COMPANY)});
				throw new CommonException(msg);
			}
			String cname = admUserDTO.getCname();
			// 若未輸入中文姓名
			if (!StringUtils.hasText(cname)) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
						new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_USER_CNAME)});
				throw new CommonException(msg);
			} else {
				// 檢查中文姓名格式和長度
				if (!ValidateUtils.length(cname, 1, 50)) {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_USER_CNAME), IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
					throw new CommonException(msg);
				}
			}
			//檢核帳號
			try {
				MultiParameterInquiryContext param = new MultiParameterInquiryContext();
				param.addParameter(AdmUserDTO.ATTRIBUTE.COMPANY_ID.getValue(), companyId);
				SessionContext authenTypeCtx = this.getServiceLocator().doService(logonUser, IAtomsConstants.SERVICE_COMPANY_SERVICE, IAtomsConstants.ACTION_IS_AUTHENTICATION_TYPE_EQUALS_IATOMS, param);
				// 登入驗證方式
				if(((Boolean) authenTypeCtx.getResponseResult()).booleanValue()){
					// 未輸入密碼
					String password = admUserDTO.getPassword();
					if (!StringUtils.hasText(password)) {
						if(!StringUtils.hasText(userId)){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_USER_PWD)});
							throw new CommonException(msg);
						}
					} else {
						// 密碼長度限｛0｝~｛1｝之間，請重新輸入
						SessionContext pwdSettingCtx = this.getServiceLocator().doService(logonUser, this.getServiceId(), IAtomsConstants.ACTION_INIT_ADD, command);
						AdmUserFormDTO admUserFormDTO = (AdmUserFormDTO) pwdSettingCtx.getResponseResult();
						PasswordSettingDTO passwordSettingDTO = admUserFormDTO.getPasswordSettingDTO();
						if(passwordSettingDTO != null){
							if (!ValidateUtils.length(password, passwordSettingDTO.getPwdLenBg().intValue(), passwordSettingDTO.getPwdLenNd().intValue())) {
								msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PWD_LENGTH_OVER_LIMIT, 
										new String[]{passwordSettingDTO.getPwdLenNd().toString(), passwordSettingDTO.getPwdLenBg().toString()});
								throw new CommonException(msg);
							}
						}
						// 密碼限英數字或英文符號或英數符號，請重新輸入
						if (!ValidateUtils.inputCharacter(password, Boolean.TRUE)) {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PWD_CHARACTER_FORMAT_ERROR);
							throw new CommonException(msg);
						}
					}
					String repassword = admUserDTO.getRepassword();
					// 未輸入確認密碼
					if (!StringUtils.hasText(repassword)) {
						if(!StringUtils.hasText(userId) || StringUtils.hasText(password)){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_BLANK, 
									new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_USER_REPWD)});
							throw new CommonException(msg);
						}
					} else {
						if(!repassword.equals(password)){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.REPWD_EQUAL_TO_PWD);
							throw new CommonException(msg);
						}
					}
				} else {
					if(!StringUtils.hasText(userId)){
						//檢核cyber帳號是否存在
						String cyberAccount = SystemConfigManager.getProperty(IAtomsConstants.CYBER_AUTHENTICATION, IAtomsConstants.ACCOUNT);
						String cyberPassword = SystemConfigManager.getProperty(IAtomsConstants.CYBER_AUTHENTICATION, IAtomsConstants.PWD);
						AdmUserDTO tempAdmUserDTO = null;
						/*if(authenticatorHelper != null){
							tempAdmUserDTO = authenticatorHelper.getAdmUserDTO(PasswordEncoderUtilities.decodePassword(cyberAccount),
									PasswordEncoderUtilities.decodePassword(cyberPassword), account);
						}*/
						if(authenticatorHelper != null){
							try {
								tempAdmUserDTO = authenticatorHelper.getAdmUserDTO(PasswordEncoderUtilities.decodePassword(cyberAccount),
										PasswordEncoderUtilities.decodePassword(cyberPassword), account);
							} catch (Exception e) {
								// 域驗證失敗
								msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.LDAP_IS_FAILURE);
								throw new CommonException(msg);
							}
						}
						if(tempAdmUserDTO == null){
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.ACCOUNT_IS_NOT_CYBER);
							throw new CommonException(msg);
						}
					}
				}			
			} catch (Exception e) {
				if(e instanceof CommonException){
					throw new CommonException(((CommonException) e).getErrorMessage());
				} else {
					return false;
				}
			}
			// 檢查英文姓名
			String ename = admUserDTO.getEname();
			if(StringUtils.hasText(ename)){
				if(!ValidateUtils.length(ename, 0, 50)){
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_USER_ENAME), IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
					throw new CommonException(msg);
				}
			}
			// 檢查電話長度
			String phone = admUserDTO.getTel();
			if(StringUtils.hasText(phone)){
				if(!ValidateUtils.length(phone, 0, 20)){
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_USER_PHONE), IAtomsConstants.MAXLENGTH_NUMBER_TWENTY});
					throw new CommonException(msg);
				}
			}
			// 檢查行動電話
			String mobile = admUserDTO.getMobile();
			if(StringUtils.hasText(mobile)){
				if(StringUtils.hasText(mobile)){
					if(!ValidateUtils.twMobile(mobile)){
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_PHONE_FORMAT_ERROR);
						throw new CommonException(msg);
					}
				}
			}
			// 檢查Email
			String email = admUserDTO.getEmail();
			if(StringUtils.hasText(email)){
				if(!ValidateUtils.email(email)){
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_FORMAT_ERROR, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_USER_EMAIL)});
					throw new CommonException(msg);
				}
				if(!ValidateUtils.length(email, 0, 50)){
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_USER_EMAIL), IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
					throw new CommonException(msg);
				}
			}
			// 檢查主管Email
			String managerEmail = admUserDTO.getManagerEmail();
			if(StringUtils.hasText(managerEmail)){
				if(!ValidateUtils.email(managerEmail)){
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INPUT_FORMAT_ERROR, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_USER_MANAGER_EMAIL)});
					throw new CommonException(msg);
				}
				if(!ValidateUtils.length(managerEmail, 0, 50)){
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_USER_MANAGER_EMAIL), IAtomsConstants.MAXLENGTH_NUMBER_FIFTY});
					throw new CommonException(msg);
				}
			}
			// 檢查備註信息長度
			String userDesc = admUserDTO.getUserDesc();
			if(StringUtils.hasText(userDesc)){
				if(!ValidateUtils.length(userDesc, 0, 200)){
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, 
							new String[]{i18NUtil.getName(IAtomsConstants.FIELD_ADM_USER_DESC), IAtomsConstants.MAXLENGTH_NUMBER_TWO_HUNDRED});
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
	public AdmUserFormDTO parse(HttpServletRequest request, AdmUserFormDTO command)
			throws CommonException {
		try {
			// 获取actionId
			String actionId = command.getActionId();
			if (IAtomsConstants.ACTION_QUERY.equals(actionId) || IAtomsConstants.ACTION_EXPORT.equals(actionId)) {
				//查詢條件	
				String sort = this.getString(request, IAtomsConstants.QUERY_PAGE_SORT);
				if (StringUtils.hasText(sort)) {
				//	sort = AdmUserDTO.getATTRIBUTENameByValue(sort);
					command.setSort(sort);
				} else {
					command.setSort(AdmUserFormDTO.PARAM_PAGE_SORT);
				}
				String order = this.getString(request, IAtomsConstants.QUERY_PAGE_ORDER);			
				if (StringUtils.hasText(order)) {
					command.setOrder(order);
				} else {
					command.setOrder(IAtomsConstants.PARAM_PAGE_ORDER);
				}
			} else if (IAtomsConstants.ACTION_SAVE.equals(actionId)){
				//保存
				AdmUserDTO admUserDTO = BindPageDataUtils.bindValueObject(request, AdmUserDTO.class);
				command.setAdmUserDTO(admUserDTO);
			} else if (IAtomsConstants.ACTION_LIST.equals(actionId)) {
				//查詢條件	
				String sort = this.getString(request, IAtomsConstants.QUERY_PAGE_SORT);
				if (StringUtils.hasText(sort)) {
				//	sort = AdmUserDTO.getATTRIBUTENameByValue(sort);
					command.setSort(sort);
				} else {
					command.setSort(AdmUserFormDTO.PARAM_PAGE_SORT_REPOSTORY);
				}
				String order = this.getString(request, IAtomsConstants.QUERY_PAGE_ORDER);			
				if (StringUtils.hasText(order)) {
					command.setOrder(order);
				} else {
					command.setOrder(IAtomsConstants.PARAM_PAGE_ORDER);
				}
			}
			return command;
		} catch (Exception e) {
			LOGGER.error("parse()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.HTTP_REQUEST_PARAMS_PARSING_FAILED, e);
		}
	}
	
	/**
	 * Purpose: export
	 * @author CrissZhang
	 * @param request:HttpServletRequest
	 * @param response:HttpServletResponse
	 * @param command:AdmUserFormDTO
	 * @throws CommonException:發生錯誤時, 丟出Exception
	 * @return ModelAndView
	 */
	public ModelAndView export(HttpServletRequest request, HttpServletResponse response, AdmUserFormDTO command) throws CommonException {
		// 取得sessionId
		String sessionId = request.getSession().getId();
		try {
			SessionContext ctx = this.getServiceLocator().doService(command.getLogonUser(), this.getServiceId(), IAtomsConstants.ACTION_EXPORT, command);
			command = (AdmUserFormDTO) ctx.getResponseResult();
			JasperReportCriteriaDTO criteria = new JasperReportCriteriaDTO();
			List<AdmUserDTO> admUserDTOs = command.getList();//.getAdmUserDtoList();
			if (!CollectionUtils.isEmpty(admUserDTOs)) {
				// 是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
				criteria.setAutoBuildJasper(false);
				criteria.setResult(admUserDTOs);
				//設置所需報表的Name
				criteria.setJrxmlName(AdmUserFormDTO.PROJECT_REPORT_JRXML_NAME);
				//設置報表路徑
				criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
				//設置匯出格式
				criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);
				//設置報表Name
				criteria.setReportFileName(AdmUserFormDTO.PROJECT_REPORT_FILE_NAME);
				criteria.setSheetName(AdmUserFormDTO.PROJECT_REPORT_FILE_NAME);
				ReportExporter.exportReport(criteria, response);
				// 成功標志
				SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, IAtomsConstants.YES);
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
				LOGGER.error(".export() is error:", e1);
			}
			LOGGER.error("export()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.EXPORT_REPORT_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("export()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return null;
	}
	
	/**
	 * Purpose:根據公司編號和帳號獲取用戶信息
	 * @author CrissZhang
	 * @param request:HttpServletRequest
	 * @param response:HttpServletResponse
	 * @param command:AdmUserFormDTO
	 * @throws ServiceException ： 出錯時拋出ServiceException
	 * @return ModelAndView
	 */
	public ModelAndView getUserByCompanyAndAccount(HttpServletRequest request, HttpServletResponse response, AdmUserFormDTO command) throws CommonException {
		Map map = new HashMap();
		// 取得sessionId
		String sessionId = request.getSession().getId();
		AdmUserDTO admUserDTO = null;
		try {
			SessionContext sessionContext = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_ADM_USER_SERVICE, IAtomsConstants.ACTION_IS_AUTHENTICATION_TYPE_EQUALS_IATOMS, command);
			String account = command.getAccount();
			command = (AdmUserFormDTO) sessionContext.getResponseResult();
			if (command != null) {
				boolean isAuthenTypeEqualsIAtoms = command.isTemp();
				String cyberAccount = SystemConfigManager.getProperty(IAtomsConstants.CYBER_AUTHENTICATION, IAtomsConstants.ACCOUNT);
				String cyberPassword = SystemConfigManager.getProperty(IAtomsConstants.CYBER_AUTHENTICATION, IAtomsConstants.PWD);
				if(!isAuthenTypeEqualsIAtoms){
					if(StringUtils.hasText(account)){
						try {
							admUserDTO = authenticatorHelper.getAdmUserDTO(PasswordEncoderUtilities.decodePassword(cyberAccount),
									PasswordEncoderUtilities.decodePassword(cyberPassword), account);
						} catch (Exception e) {
							admUserDTO = new AdmUserDTO();
							// 驗證失敗
							admUserDTO.setIsLdapFailure(true);
						}
					}
				}
				if(admUserDTO != null){
					if(StringUtils.hasText(admUserDTO.getCname())){
						String nameStr = admUserDTO.getCname();
						String cname = null;
						String ename = null;
						if(nameStr.indexOf("(") > 0 && nameStr.indexOf(")") > 0){
							cname = nameStr.substring(nameStr.indexOf("(") + 1, nameStr.indexOf(")"));
						} else {
							cname = nameStr;
						}
						if(nameStr.indexOf("(") > 0){
							ename = nameStr.substring(0, nameStr.indexOf("(")-1);
						}
						admUserDTO.setCname(cname);
						admUserDTO.setEname(ename);
					} else {
						admUserDTO.setCname(null);
						admUserDTO.setEname(null);
					//	admUserDTO.setEmail(null);
					}
				} else {
					admUserDTO = new AdmUserDTO();
				}
				admUserDTO.setIsAuthenTypeEqualsIAtoms(isAuthenTypeEqualsIAtoms);
				map.put("userDTO", admUserDTO);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.TRUE);
			} else {
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
			}
			return new ModelAndView(new MappingJacksonJsonView(), map); 
		} catch (Exception e) {
			try {
				SessionHelper.setAttribute(request, command.getUseCaseNo(), sessionId, false);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.QUERY_FAILURE));
				return new ModelAndView(new MappingJacksonJsonView(), map);
			} catch (Exception e1) {
				LOGGER.error(".getUserByCompanyAndAccount() is error:", e1);
			}
			LOGGER.error("getUserByCompanyAndAccount()", e.getMessage(), e);
			throw new CommonException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
	}
	
	/**
	 * @return the authenticatorHelper
	 */
	public AuthenticatorHelper getAuthenticatorHelper() {
		return authenticatorHelper;
	}
	/**
	 * @param authenticatorHelper the authenticatorHelper to set
	 */
	public void setAuthenticatorHelper(AuthenticatorHelper authenticatorHelper) {
		this.authenticatorHelper = authenticatorHelper;
	}
	
}