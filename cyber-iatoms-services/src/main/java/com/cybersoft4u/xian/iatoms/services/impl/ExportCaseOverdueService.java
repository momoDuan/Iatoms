package com.cybersoft4u.xian.iatoms.services.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.Parameter;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.IAtomsUtils;
import com.cybersoft4u.xian.iatoms.common.utils.mail.MailComponent;
import com.cybersoft4u.xian.iatoms.services.IExportCaseOverdueService;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmRoleDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmUser;
/**
 * Purpose: 批次通知工程師回應、到場、完修逾期情況
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2018年6月12日
 * @MaintenancePersonnel CarrieDuan
 */
public class ExportCaseOverdueService extends AtomicService implements IExportCaseOverdueService {

	/**
	 * 記錄日誌
	 */
	private static final CafeLog LOGGER	= CafeLogFactory.getLog(ExportCaseOverdueService.class);

	/**
	 * 案件正在處理主檔DAO
	 */
	private ISrmCaseHandleInfoDAO srmCaseHandleInfoDAO;
	
	/**
	 * 发送邮件
	 */
	private MailComponent mailComponent;
	/**
	 * 使用者賬號DAO
	 */
	private IAdmUserDAO admUserDAO;
	
	/**
	 * 使用者角色DAO
	 */
	private IAdmRoleDAO admRoleDAO;
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IExportCaseOverdueService#queryCaseInfo()
	 */
	@Override
	public void queryCaseInfo() throws ServiceException {
		try {
			int hours = DateTimeUtils.getCurrentCalendar(Calendar.HOUR_OF_DAY);
			if (hours >= 9 && hours <= 21) {
				//獲取當前時間（查詢逾期）
				Date startDate = DateTimeUtils.setTime(DateTimeUtils.getCurrentDate(), hours, 0, 0);
				Timestamp queryStartDate = DateTimeUtils.toTimestamp(DateTimeUtils.toString(startDate, DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_SLASH));
				//獲取截止時間（查詢逾期）
				Date endDate = DateTimeUtils.setTime(DateTimeUtils.getCurrentDate(), hours + 2, 0, 0);
				Timestamp queryEndDate = DateTimeUtils.toTimestamp(DateTimeUtils.toString(endDate, DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_SLASH));
				List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = this.srmCaseHandleInfoDAO.getOverdueCaseInfo(queryStartDate, queryEndDate);
				if (!CollectionUtils.isEmpty(caseHandleInfoDTOs)) {
					//邮件主题模板
					String subjectTemplate = MailComponent.MAIL_TEMPLATE_ADD + CaseManagerFormDTO.MEETING_NOTICE_SUBJECT_OVERDUE_TEMPLATE;
					//邮件内容模板
					String textTemplate = MailComponent.MAIL_TEMPLATE_ADD + CaseManagerFormDTO.MEETING_NOTICE_TEXT_OVERDUE_TEMPLATE;
					StringBuffer toMailAddress = new StringBuffer();
					AdmUser admUser = null;
					String deptCode = null;
					String roleCode = null;
					List<String> userIds = null;
					List<AdmUserDTO> admUserDTOs = null;
					for (SrmCaseHandleInfoDTO dto : caseHandleInfoDTOs) {
						toMailAddress = new StringBuffer();
						//判斷是否派工給工程師
						if (StringUtils.hasText(dto.getDispatchProcessUser())) {
							admUser = (AdmUser) this.admUserDAO.findByPrimaryKey(AdmUser.class, dto.getDispatchProcessUser());
							if (admUser != null) {
								toMailAddress.append(admUser.getEmail());
							} else {
								continue;
							}
							//派工給部門
						} else if (StringUtils.hasText(dto.getDispatchDeptId())) {
							if (IAtomsConstants.CASE_ROLE.CUSTOMER_SERVICE.getCode().equals(dto.getDispatchDeptId())
									|| IAtomsConstants.CASE_ROLE.QA.getCode().equals(dto.getDispatchDeptId())
									|| IAtomsConstants.CASE_ROLE.TMS.getCode().equals(dto.getDispatchDeptId())) {
								roleCode = dto.getDispatchDeptId();
							} else {
								deptCode = dto.getDispatchDeptId();
							}
							List<Parameter> users = this.admRoleDAO.getUserByDepartmentAndRole(deptCode, roleCode, Boolean.FALSE, Boolean.FALSE);
							if (!CollectionUtils.isEmpty(users)) {
								userIds = new ArrayList<String>();
								for (Parameter parameter : users) {
									if (parameter.getValue() != null && parameter.getValue() != "") {
										userIds.add((String) parameter.getValue());
									}
								}
								//獲取對應user的mail
								admUserDTOs = this.admUserDAO.getUserEmailById(userIds);
								if (!CollectionUtils.isEmpty(admUserDTOs)) {
									for (AdmUserDTO admUserDTO : admUserDTOs) {
										toMailAddress.append(admUserDTO.getEmail()).append(IAtomsConstants.MARK_SEMICOLON);
									}
									toMailAddress.substring(0, toMailAddress.length() - 1);
								}
							}
							//未派工,派工給客服
						} else {
							List<String> roles = new ArrayList<String>();
							roles.add(IAtomsConstants.CASE_ROLE.CUSTOMER_SERVICE.getCode());
							admUserDTOs = this.admUserDAO.getUserDTOsBy(roles);
							if (!CollectionUtils.isEmpty(admUserDTOs)) {
								for (AdmUserDTO admUserDTO : admUserDTOs) {
									toMailAddress.append(admUserDTO.getEmail()).append(IAtomsConstants.MARK_SEMICOLON);
								}
								toMailAddress.substring(0, toMailAddress.length() - 1);
							}
						}
						if (toMailAddress.length() > 0) {
							Map<String, Object> variables = new HashMap<String, Object>();
							//接收人Maill地址
							variables.put(SrmCaseHandleInfoDTO.ATTRIBUTE.TO_MAIL.getValue(), StringUtils.hasText(toMailAddress) ? toMailAddress : IAtomsConstants.MARK_EMPTY_STRING);
							//发件人Maill地址
							//variables.put(SrmCaseHandleInfoDTO.ATTRIBUTE.FROM_MAIL.getValue(), StringUtils.hasText(caseManagerFormDTO.getFromMail()) ? caseManagerFormDTO.getFromMail() : IAtomsConstants.MARK_EMPTY_STRING);
							//发件人名称 
							//variables.put(SrmCaseHandleInfoDTO.ATTRIBUTE.FROM_NAME.getValue(), StringUtils.hasText(caseManagerFormDTO.getFromName()) ? caseManagerFormDTO.getFromName() : IAtomsConstants.MARK_EMPTY_STRING);
							//
							
							//案件編號
							variables.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue(), StringUtils.hasText(dto.getCaseId()) ? dto.getCaseId() : IAtomsConstants.MARK_EMPTY_STRING);
							//Task #3089  主题所用 需求单号 当需求单号为空时，不要斜杠
							variables.put(SrmCaseHandleInfoDTO.ATTRIBUTE.MAIN_REQUIREMENT_NO.getValue(), StringUtils.hasText(dto.getRequirementNo()) ? IAtomsConstants.MARK_BACKSLASH + dto.getRequirementNo() : IAtomsConstants.MARK_EMPTY_STRING);
							//案件類別
							variables.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue(), i18NUtil.getName(IAtomsConstants.CASE_CATEGORY.REPAIR.getCode()));
							//需求單號
							variables.put(SrmCaseHandleInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue(), StringUtils.hasText(dto.getRequirementNo()) ? dto.getRequirementNo() : IAtomsConstants.MARK_EMPTY_STRING);
							//客戶名稱
							variables.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_NAME.getValue(), StringUtils.hasText(dto.getCustomerName()) ? dto.getCustomerName() : IAtomsConstants.MARK_EMPTY_STRING);
							//進見時間
							if(dto.getCreatedDate() != null) {
								variables.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue(), DateTimeUtils.toString( dto.getCreatedDate(),  DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_SLASH));
							} else {
								variables.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue(), IAtomsConstants.MARK_EMPTY_STRING);
							}
							//行動電話
							variables.put(SrmCaseHandleInfoDTO.ATTRIBUTE.PHONE.getValue(), StringUtils.hasText(dto.getPhone()) ? dto.getPhone() : IAtomsConstants.MARK_EMPTY_STRING);
							//dtid
							variables.put(SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue(), StringUtils.hasText(dto.getDtid()) ? dto.getDtid() : IAtomsConstants.MARK_EMPTY_STRING);
							//tid
							variables.put(SrmCaseHandleInfoDTO.ATTRIBUTE.TID.getValue(), StringUtils.hasText(dto.getTid()) ? dto.getTid() : IAtomsConstants.MARK_EMPTY_STRING);
							//特店代號
							variables.put(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringUtils.hasText(dto.getMerchantCode()) ? dto.getMerchantCode() : IAtomsConstants.MARK_EMPTY_STRING);
							//特店名稱
							variables.put(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue(), StringUtils.hasText(dto.getMerchantName()) ? dto.getMerchantName() : IAtomsConstants.MARK_EMPTY_STRING);
							//表頭
							variables.put(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_HEADER_NAME.getValue(), StringUtils.hasText(dto.getMerchantHeaderName()) ? dto.getMerchantHeaderName() : IAtomsConstants.MARK_EMPTY_STRING);
							//設備名稱
							variables.put(SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE.getValue(), StringUtils.hasText(dto.getEdcTypeName()) ? dto.getEdcTypeName() : IAtomsConstants.MARK_EMPTY_STRING);
							//聯絡人姓名
							variables.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER.getValue(), StringUtils.hasText(dto.getContactUser()) ? dto.getContactUser() : IAtomsConstants.MARK_EMPTY_STRING);
							//聯絡人電話
							variables.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_PHONE.getValue(), StringUtils.hasText(dto.getContactUserPhone()) ? dto.getContactUserPhone() : IAtomsConstants.MARK_EMPTY_STRING);
							//聯繫地址
							variables.put(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue(), StringUtils.hasText(dto.getContactAddress()) ? dto.getContactAddress() : IAtomsConstants.MARK_EMPTY_STRING);
							variables.put(IAtomsConstants.ADDRESS, i18NUtil.getName(IAtomsConstants.PARAM_CONTACT_ADDRESS));
							//預計完成日
							if(dto.getAcceptableFinishDate() != null) {
								variables.put(SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_FINISH_DATE.getValue(), DateTimeUtils.toString( dto.getAcceptableFinishDate(),  DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_SLASH));
							} else {
								variables.put(SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_FINISH_DATE.getValue(), IAtomsConstants.MARK_EMPTY_STRING);
							}
							if(dto.getCompleteDate() != null) {
								//實際完成日
								variables.put(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue(), DateTimeUtils.toString( dto.getCompleteDate(),  DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_SLASH));
							} else {
								variables.put(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue(), IAtomsConstants.MARK_EMPTY_STRING);
							}
							//需求描述
							variables.put(SrmCaseHandleInfoDTO.ATTRIBUTE.DESCRIPTION.getValue(),
										StringUtils.hasText(dto.getDescription()) ? dto.getDescription().replaceAll(IAtomsConstants.RETURN_LINE_FEED, "<caseMeetingBr>"): IAtomsConstants.MARK_EMPTY_STRING);
							//處理人員
							variables.put(SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue(), StringUtils.hasText(dto.getUpdatedByName()) ? dto.getUpdatedByName() : IAtomsConstants.MARK_EMPTY_STRING);
							//處理說明
							variables.put(SrmCaseHandleInfoDTO.ATTRIBUTE.TRANSACTION_DESCRIPTION.getValue(),
										StringUtils.hasText(dto.getDescription()) ? dto.getDescription().replaceAll(IAtomsConstants.MARK_WRAP, "<caseMeetingBr>") : IAtomsConstants.MARK_EMPTY_STRING);
							//拼接成會議通知需要的格式 20170306T125000
							//通知開始時間
							String meetingStartTimeDate = DateTimeUtils.toString(queryStartDate, DateTimeUtils.DT_FMT_YYYYMMDDHHMM_SLASH).replace(IAtomsConstants.MARK_BACKSLASH, IAtomsConstants.MARK_EMPTY_STRING).replace(IAtomsConstants.MARK_SPACE, IAtomsConstants.COLUMN_T).replace(IAtomsConstants.MARK_COLON, IAtomsConstants.MARK_EMPTY_STRING).concat(IAtomsConstants.LEAVE_CASE_STATUS_ZERO).concat(IAtomsConstants.LEAVE_CASE_STATUS_ZERO);
							LOGGER.debug(this.getClass().getName(), ".queryCaseInfo()--->", " meetingStartTimeDate ="+meetingStartTimeDate);
							//通知結束時間
							String meetingEndTimeDate = DateTimeUtils.toString(queryEndDate, DateTimeUtils.DT_FMT_YYYYMMDDHHMM_SLASH).replace(IAtomsConstants.MARK_BACKSLASH, IAtomsConstants.MARK_EMPTY_STRING).replace(IAtomsConstants.MARK_SPACE, IAtomsConstants.COLUMN_T).replace(IAtomsConstants.MARK_COLON, IAtomsConstants.MARK_EMPTY_STRING).concat(IAtomsConstants.LEAVE_CASE_STATUS_ZERO).concat(IAtomsConstants.LEAVE_CASE_STATUS_ZERO);
							LOGGER.debug(this.getClass().getName(), ".queryCaseInfo()--->", " meetingEndTimeDate ="+meetingEndTimeDate);
							//去除重複mail地址 2017/10/24
							String toMail = toMailAddress.toString();
							try {
								toMail = IAtomsUtils.removeDuplicate(toMail, IAtomsConstants.MARK_SEMICOLON);
							} catch (Exception e) {
								LOGGER.debug(this.getClass().getName(), "Error is in queryCaseInfo.removeDuplicate,Error——>toMailAddress "+toMailAddress);
							}
							this.mailComponent.meetingNoticeTo(null, toMail, subjectTemplate, textTemplate, variables, variables.get(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue()).toString(), meetingStartTimeDate, meetingEndTimeDate);
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(".queryCaseInfo(SessionContext sessionContext):", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
	}

	/**
	 * @return the srmCaseHandleInfoDAO
	 */
	public ISrmCaseHandleInfoDAO getSrmCaseHandleInfoDAO() {
		return srmCaseHandleInfoDAO;
	}

	/**
	 * @param srmCaseHandleInfoDAO the srmCaseHandleInfoDAO to set
	 */
	public void setSrmCaseHandleInfoDAO(ISrmCaseHandleInfoDAO srmCaseHandleInfoDAO) {
		this.srmCaseHandleInfoDAO = srmCaseHandleInfoDAO;
	}

	/**
	 * @return the mailComponent
	 */
	public MailComponent getMailComponent() {
		return mailComponent;
	}

	/**
	 * @param mailComponent the mailComponent to set
	 */
	public void setMailComponent(MailComponent mailComponent) {
		this.mailComponent = mailComponent;
	}

	/**
	 * @return the admUserDAO
	 */
	public IAdmUserDAO getAdmUserDAO() {
		return admUserDAO;
	}

	/**
	 * @param admUserDAO the admUserDAO to set
	 */
	public void setAdmUserDAO(IAdmUserDAO admUserDAO) {
		this.admUserDAO = admUserDAO;
	}

	/**
	 * @return the admRoleDAO
	 */
	public IAdmRoleDAO getAdmRoleDAO() {
		return admRoleDAO;
	}

	/**
	 * @param admRoleDAO the admRoleDAO to set
	 */
	public void setAdmRoleDAO(IAdmRoleDAO admRoleDAO) {
		this.admRoleDAO = admRoleDAO;
	}
}
