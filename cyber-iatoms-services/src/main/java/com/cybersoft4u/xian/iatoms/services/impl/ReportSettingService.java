package com.cybersoft4u.xian.iatoms.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Message;
import cafe.core.bean.Parameter;
import cafe.core.bean.identity.LogonUser;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.dao.DataAccessException;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.convert.SimpleDtoDmoTransformer;
import cafe.core.util.convert.Transformer;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.MailListDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ReportSettingDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ReportSettingDetailDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ReportSettingFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.mail.MailComponent;
import com.cybersoft4u.xian.iatoms.common.utils.mail.TemplateMailMessageDTO;
import com.cybersoft4u.xian.iatoms.services.IReportService;
import com.cybersoft4u.xian.iatoms.services.IReportSettingService;
import com.cybersoft4u.xian.iatoms.services.dao.IMailListDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IReportSettingDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IReportSettingDetailDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimReportSetting;
import com.cybersoft4u.xian.iatoms.services.dmo.BimReportSettingDetail;
import com.cybersoft4u.xian.iatoms.services.dmo.BimReportSettingDetailId;

/**
 * Purpose: 報表發送功能設定Service
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2016年7月7日
 * @MaintenancePersonnel ElvaHe
 */
public class ReportSettingService extends AtomicService implements IReportSettingService {
	
	/**
	 * 日誌記錄掛件
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, ReportSettingService.class);
	
	/**
	 * 報表發送功能設定DAO
	 */
	private IReportSettingDAO reportSettingDAO;
	/**
	 * 報表明細DAO
	 */
	private IReportSettingDetailDAO reportSettingDetailDAO;
	/**
	 * 報表名稱和對應的郵件類
	 * key:報表名稱
	 * value:對應的發郵件使用的類
	 */
	private Map<String,IReportService> reportSettingMap;
	
	/**
	 * 電子郵件群組DAO
	 */
	private IMailListDAO mailListDAO;
	
	/**
	 * 注入發送郵件
	 */
	private MailComponent mailComponent;
	
	/**
	 * Constructor:無參構造
	 */
	public ReportSettingService() {
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IReportSettingService#init(cafe.core.context.SessionContext)
	 */
	public SessionContext init(SessionContext sessionContext)throws ServiceException {
		try {
			ReportSettingFormDTO reportSettingFormDTO = (ReportSettingFormDTO) sessionContext.getRequestParameter();
			sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS));
			sessionContext.setResponseResult(reportSettingFormDTO);
		} catch (Exception e) {
			sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.INIT_PAGE_FAILURE));
			LOGGER.error("init()", "error - ", e);
			throw new ServiceException(e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IReportSettingService#query(cafe.core.context.SessionContext)
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException {
		try {
			List<ReportSettingDTO> resultList = new ArrayList<ReportSettingDTO>();
			List<ReportSettingDTO> tempList = null;
			ReportSettingFormDTO formDTO = (ReportSettingFormDTO) sessionContext.getRequestParameter();
			//獲取查詢條件
			String queryCustomerId = formDTO.getQueryCustomerId();
			String queryReportCode = formDTO.getQueryReportCode();
			Integer pageSize = formDTO.getRows();
			Integer pageIndex = formDTO.getPage();
			String sort = formDTO.getSort();
			String order = formDTO.getOrder();
			Message msg = null;
			//查詢結果的總記錄數
			Integer totalSize = this.reportSettingDAO.codeCount(queryCustomerId, queryReportCode);
			//若存在查詢結果
			if (totalSize.intValue() > 0 ) {
				List<ReportSettingDTO> reportCodeList = this.reportSettingDAO.listByReportCode(queryCustomerId, queryReportCode, pageSize, pageIndex, sort, order);
				int size = reportCodeList.size();
				if(size > 0){
					for(int i=0; i < size; i++){
						tempList = this.reportSettingDAO.getDetailList(reportCodeList.get(i).getCompanyId(), reportCodeList.get(i).getReportCode());
						// 設置序號
						if(tempList.size() == 1){
							
							tempList.get(0).setRowNumber(Integer.valueOf(pageSize.intValue() * (pageIndex.intValue() - 1) + (i + 1)));
						} else if (tempList.size() > 1){
							for(ReportSettingDTO reportSettingDTO : tempList){
								reportSettingDTO.setRowNumber(Integer.valueOf(pageSize.intValue() * (pageIndex.intValue() - 1) + (i + 1)));
							}
						}
						resultList.addAll(tempList);
					}
					formDTO.setList(resultList);
					formDTO.getPageNavigation().setRowCount(totalSize.intValue());
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
				}
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(formDTO);
		} catch (DataAccessException e) {
			LOGGER.error("query()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("query()", "Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IReportSettingService#initEdit(cafe.core.context.SessionContext)
	 */
	public SessionContext initEdit(SessionContext sessionContext) throws ServiceException {
		Transformer transformer = new SimpleDtoDmoTransformer();
		ReportSettingFormDTO formDTO = null;
		ReportSettingDTO reportSettingDTO = null;
		try {
			formDTO = (ReportSettingFormDTO) sessionContext.getRequestParameter();
			//獲取需要的報表編號
			String settingId = formDTO.getSettingId();
			//根據報表編號獲取該編號的所有信息
			BimReportSetting reportSetting = this.reportSettingDAO.findByPrimaryKey(BimReportSetting.class, settingId);
			if(reportSetting != null){
				//把其轉換為DTO
				reportSettingDTO = (ReportSettingDTO) transformer.transform(reportSetting, new ReportSettingDTO());
				formDTO.setReportSettingDTO(reportSettingDTO);
			}
			//重送頁面報表日期的預設值
			String reportSendDate = null;
			if (IAtomsConstants.REPORT_NAME_EDC_FEE_FOR_JDW.equals(reportSettingDTO.getReportCode())
					|| IAtomsConstants.REPORT_TCB_SYB_NINETEEN.equals(reportSettingDTO.getReportCode())) {
				reportSendDate = DateTimeUtils.toString(DateTimeUtils.addCalendar(DateTimeUtils.getCurrentDate(), 0, -1, 1), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
			} else {
				reportSendDate = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
			}
			formDTO.setCreatedDateString(reportSendDate);
			sessionContext.setResponseResult(formDTO);
		} catch (DataAccessException e) {
			LOGGER.error("initEdit()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("initEdit()", "error:", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)customerId
	 * @see com.cybersoft4u.xian.iatoms.services.IReportSettingService#save(cafe.core.context.SessionContext)
	 */
	public SessionContext save(SessionContext sessionContext) throws ServiceException {
		Transformer transformer = new SimpleDtoDmoTransformer();
		BimReportSettingDetailId reportId = null;
		BimReportSetting reportSetting = null;
		BimReportSettingDetail reportSettingDetail = null;
		//數據庫中使用的主鍵
		String settingId = null;
		Message msg = null;
		try {
			//獲取session中的FormDTO
			ReportSettingFormDTO formDTO = (ReportSettingFormDTO) sessionContext.getRequestParameter();
			//獲取角色
			LogonUser logonUser = formDTO.getLogonUser();
			//獲取報表的DTO
			ReportSettingDTO reportSettingDTO = formDTO.getReportSettingDTO();
			//獲取報表明細的DTO
			ReportSettingDetailDTO reportSettingDetailDTO = formDTO.getReportSettingDetailDTO();
			if (reportSettingDTO != null) {
				reportSetting = new BimReportSetting();
				settingId = reportSettingDTO.getSettingId();
				//若存在settingId則為修改操作，否則為新增操作
				if (StringUtils.hasText(settingId)) {
					//修改操作
					reportSetting = this.reportSettingDAO.findByPrimaryKey(BimReportSetting.class, settingId);
					//設置屬性值
					reportSetting.setRecipient(formDTO.getReportSettingDTO().getRecipient());
					reportSetting.setCopy(formDTO.getReportSettingDTO().getCopy());
					reportSetting.setRemark(formDTO.getReportSettingDTO().getRemark());
					//設置人員信息
					reportSetting.setUpdatedById(logonUser.getId());
					reportSetting.setUpdatedByName(logonUser.getName());
					reportSetting.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
					//修改
					this.reportSettingDAO.update(reportSetting);
					//刪除所有的報表設定的明細資料
					this.reportSettingDetailDAO.deleteBySettingId(settingId);
					this.reportSettingDetailDAO.getDaoSupport().flush();
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.UPDATE_SUCCESS, new String[]{this.getMyName()});
				} else {
					//新增操作
					//將DTO轉換為DMO
					reportSetting = (BimReportSetting) transformer.transform(reportSettingDTO, reportSetting);
					settingId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_REPORT_SETTING);
					reportSetting.setSettingId(settingId);
					//設置新增修改人員信息
					reportSetting.setCompanyId(reportSettingDTO.getCustomerName());
					reportSetting.setReportCode(reportSettingDTO.getReportCode());
					reportSetting.setRecipient(reportSettingDTO.getRecipient());
					reportSetting.setCopy(reportSettingDTO.getCopy());
					reportSetting.setRemark(reportSettingDTO.getRemark());
					reportSetting.setCreatedById(logonUser.getId());
					reportSetting.setCreatedByName(logonUser.getName());
					reportSetting.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
					this.reportSettingDAO.insert(reportSetting);
					this.reportSettingDAO.getDaoSupport().flush();
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INSERT_SUCCESS, new String[]{this.getMyName()});
				}
			}
			//若明細的DTO不為空			
			if(reportSettingDetailDTO != null){
				reportSettingDetail = (BimReportSettingDetail) transformer.transform(reportSettingDetailDTO, reportSettingDetail);
				//獲取報表明細
				String reportDetatil = reportSettingDetailDTO.getReportDetail();
				//若存在報表明細
				if (StringUtils.hasText(reportDetatil)) {
					//按“，”分割報表明細
					String[] reportDetatils = reportDetatil.split(IAtomsConstants.MARK_SEPARATOR);
					//依次將選擇的明細存放到數據庫中
					for (String detail : reportDetatils) {
						reportSettingDetail = new BimReportSettingDetail();
						//若存在ID就將報表明細的Id設為SettingId,否則設為TempId
						if(StringUtils.hasText(settingId)){
							reportId = new BimReportSettingDetailId(settingId, detail);
						}
						reportSettingDetail.setId(reportId);
						reportSettingDetail.setBimReportSetting(reportSetting);
						//將明細保存到數據庫
						this.reportSettingDAO.getDaoSupport().saveOrUpdate(reportSettingDetail);
					}
				}
			}
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(formDTO);
		} catch (DataAccessException e) {
			LOGGER.error("save()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.SAVE_FAILURE));
			LOGGER.error("save()", "error:", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IReportSettingService#delete(cafe.core.context.SessionContext)
	 */
	public SessionContext delete(SessionContext sessionContext) throws ServiceException {
		try {
			//獲取FormDTO
			ReportSettingFormDTO formDTO = (ReportSettingFormDTO) sessionContext.getRequestParameter();
			//獲取選中的信息的SETTINGID 
			String settingId = formDTO.getSettingId();
			//根據SettingId刪除該ID的所有明細信息
			this.reportSettingDetailDAO.deleteBySettingId(settingId);
			//獲取主表的報表信息
			BimReportSetting reportSetting = this.reportSettingDAO.findByPrimaryKey(BimReportSetting.class, settingId);
			//刪除主表的信息
			this.reportSettingDAO.delete(reportSetting);
			this.reportSettingDAO.getDaoSupport().flush();
			sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, CoreMessageCode.DELETE_SUCCESS, new String[]{this.getMyName()}));
			sessionContext.setResponseResult(formDTO);
		} catch (DataAccessException e) {
			LOGGER.error("delete()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("delete()", "error", e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IReportSettingService#getPreReportCodeList(cafe.core.context.SessionContext)
	 */
	public List<Parameter> getPreReportCodeList(MultiParameterInquiryContext param)throws ServiceException{
		try {
			String companyId = (String) param.getParameter(ReportSettingDTO.ATTRIBUTE.COMPANY_ID.getValue());
			List<Parameter> reportLists = this.reportSettingDAO.getPreReportCodeList(companyId);
			return reportLists;
		} catch (DataAccessException e) {
			LOGGER.error("getPreReportCodeList()", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("getPreReportCodeList()", "error:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IReportSettingService#send(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext send(SessionContext sessionContext) throws ServiceException {
		try {
			ReportSettingFormDTO formDTO = (ReportSettingFormDTO) sessionContext.getRequestParameter();
			this.sendReportThread(formDTO);
			Message msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.REPORT_SEND_SUCCESS);
			sessionContext.setReturnMessage(msg);
		} catch (Exception e) {
			LOGGER.error("send()", "error:", e);
			throw new ServiceException(IAtomsMessageCode.ACCEPTANCE_MSG, e);
		}
		return sessionContext;
	}
	
	/**
	 * Purpose:報表重送使用的線程
	 * @param formDTO
	 * @throws ServiceException
	 */
	public void sendReportThread(final ReportSettingFormDTO formDTO) throws ServiceException {
		new Thread(
				new Runnable() {
				    public void run() {
				    	ReportSettingDTO reportSettingDTO = null;
						Date sendDate = null;
						String customerId = null;
						//是否有異常發生
						Boolean exceptionFlag = false;
						//異常的原因
						String exceptionCause = null;
						//異常的具體位置
						StackTraceElement[] stackTrace = null;
						String errorPath = null;
						String reportCode = "";
						//是否為月報
						Boolean monthFlag = false;
						try {
							if(formDTO != null){
								reportCode = formDTO.getReportCode();
								if(!CollectionUtils.isEmpty(reportSettingMap)){
									if(reportSettingMap.containsKey(reportCode)) {
										//獲取重送的DTO參數
										reportSettingDTO = formDTO.getReportSettingDTO();
										if (reportSettingDTO != null) {
											//處理客戶選擇的時間
											//獲取輸入的客戶編號和報表名稱
											customerId = reportSettingDTO.getCompanyId();
											//獲取輸入的報表日期
											String createdDateString = reportSettingDTO.getCreatedDateString();
											//當日期只有年月時添加日 (月報時只有年月)
											if(StringUtils.hasText(createdDateString) && createdDateString.length() < 10){
												createdDateString = createdDateString + "/01";
												monthFlag = true;
											}

											try {
												//將選擇的重送日期轉化為date數據
												sendDate = DateTimeUtils.toDate(createdDateString);
												//當報表不是月報時 update by 2017/06/23
												if (!monthFlag) {
													//當報表不是半月報--案件設備明細(環匯格式)與 日報--完修逾時率警示通知時，將日期 + 1 
													if ((!IAtomsConstants.REPORT_NAME_CASE_ASSET_DETAIL.equals(reportCode)) && (!IAtomsConstants.REPORT_NAME_COMPLETE_OVERDUE_RATE_REPORT.equals(reportCode))) {
														sendDate = DateTimeUtils.addCalendar(sendDate, 0, 0, +1);
													}
												//當報表是月報時,將月份 + 1
												} else {
													//Bug #2412 EDC合約到期提示報表  不加1 update by 2017/09/18
													if (IAtomsConstants.REPORT_NAME_EDC_CONTRACT_EXPIRE_INFO_REPORT.equals(reportCode)) {
														sendDate = DateTimeUtils.addCalendar(sendDate, 0, 0, 0);
													} else {
														sendDate = DateTimeUtils.addCalendar(sendDate, 0, +1, 0);
													}
												}				
											} catch (Exception e) {
												LOGGER.error("sendReportThread()", "error:", e);
												throw new ServiceException(IAtomsMessageCode.IATOMS_FAILURE, e);
											}
											//根據報表名稱調用相應的發送郵件的方法
											reportSettingMap.get(reportCode).sendReportMail(sendDate, customerId, reportCode);
										}
									}
								}
							}
						} catch (Exception e) {
							LOGGER.error("sendReportThread()", "is error:", e);
							//異常的原因
							exceptionCause = e.getCause().toString();
							//異常的具體位置
							stackTrace = e.getCause().getStackTrace();
							//若異常的具體位置存在
							if(stackTrace != null&&stackTrace.length>0){
								for(StackTraceElement element : stackTrace){
									//若存在異常且錯誤源等於重送的郵件類名時
									if( element != null && element.toString().contains(reportSettingMap.get(reportCode).getClass().getSimpleName())){
										errorPath = element.toString();
										break;
									}
								}
							}
							//有異常發生
							exceptionFlag = true;
						}
						//exceptionFlag = true;
						//當有異常發生時通知OAS（電子郵件群組）的人員進行處理
						if (exceptionFlag) {
							reportSettingDTO = formDTO.getReportSettingDTO();
							List<ReportSettingDTO> reportSettingDTOs = reportSettingDAO.getDetailList(reportSettingDTO.getCompanyId(), reportSettingDTO.getReportCode());
							String customerName = reportSettingDTOs.get(0).getCustomerName();
							//郵件主題模板
							String subjectTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.REPORT_SEND_EXCEPTION_SUBJECT_TEMPLATE;
							//郵件內容模板
							String textTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.REPORT_SEND_EXCEPTION_TEXT_TEMPLATE;
							//查找電子郵件群組OAS的信息
							List<MailListDTO> mailListDTOs = mailListDAO.listBy(IAtomsConstants.MAIL_GROUP_OAS, null, -1, -1, null, null);
							for (MailListDTO mailListDTO : mailListDTOs) {
								String[] mailContext1 = {customerName,reportSettingDTOs.get(0).getReportName()}; 
								String[] mailContext2 = {customerName}; 
								//郵件內容
								Map<String, Object> variables = new HashMap<String, Object>();
								variables.put("toMail", mailListDTO.getEmail());
								variables.put("toName", mailListDTO.getName());
								variables.put("mailContext", i18NUtil.getName(IAtomsMessageCode.REPORT_SEND_EXCEPTION_CONTEXT, mailContext1, Locale.TAIWAN));
								variables.put("mailSubject", i18NUtil.getName(IAtomsMessageCode.REPORT_SEND_EXCEPTION_SUBJECT, mailContext2, Locale.TAIWAN));
								variables.put("exceptionTimeText", i18NUtil.getName(IAtomsMessageCode.REPORT_SEND_EXCEPTION_TIME));
								variables.put("exceptionTime", DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(),  DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_SLASH));
								variables.put("exceptionText", i18NUtil.getName(IAtomsMessageCode.REPORT_SEND_EXCEPTION_CAUSE));
								variables.put("exceptionCause", exceptionCause);
								variables.put("errorText", i18NUtil.getName(IAtomsMessageCode.REPORT_SEND_EXCEPTION_PATH));
								variables.put("errorPath", errorPath);
								mailComponent.mailTo(null, mailListDTO.getEmail(), subjectTemplate, textTemplate, variables);
							}

						}
				    }
				}
			).start();
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IReportSettingService#initSend(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext initSend(SessionContext sessionContext)
			throws ServiceException {
		try {
			this.initEdit(sessionContext);
			return sessionContext;
		} catch (Exception e) {
			LOGGER.error("initSend()", "error:", e);
			throw new ServiceException(IAtomsMessageCode.ACCEPTANCE_MSG, e);
		}
		
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IReportSettingService#getReportDetailListById(java.lang.String)
	 */
	public List<String> getReportDetailListById(MultiParameterInquiryContext param) throws ServiceException {
		try {
			String settingId = (String) param.getParameter(ReportSettingDTO.ATTRIBUTE.SETTING_ID.getValue());
			//獲取新增時選擇過的報表明細
			List<String> reportDetailList = this.reportSettingDetailDAO.listBy(settingId);
			return reportDetailList;
		} catch (Exception e) {
			LOGGER.error("getReportDetailListById()", "error:", e);
			throw new ServiceException(IAtomsMessageCode.ACCEPTANCE_MSG, e);
		}
	}

	/**
	 * @return the reportSettingDetailDAO
	 */
	public IReportSettingDetailDAO getReportSettingDetailDAO() {
		return reportSettingDetailDAO;
	}

	/**
	 * @return the reportSettingDAO
	 */
	public IReportSettingDAO getReportSettingDAO() {
		return reportSettingDAO;
	}

	/**
	 * @param reportSettingDAO the reportSettingDAO to set
	 */
	public void setReportSettingDAO(IReportSettingDAO reportSettingDAO) {
		this.reportSettingDAO = reportSettingDAO;
	}

	/**
	 * @param reportSettingDetailDAO the reportSettingDetailDAO to set
	 */
	public void setReportSettingDetailDAO(IReportSettingDetailDAO reportSettingDetailDAO) {
		this.reportSettingDetailDAO = reportSettingDetailDAO;
	}

	/**
	 * @return the reportSettingMap
	 */
	public Map<String, IReportService> getReportSettingMap() {
		return reportSettingMap;
	}

	/**
	 * @param reportSettingMap the reportSettingMap to set
	 */
	public void setReportSettingMap(Map<String, IReportService> reportSettingMap) {
		this.reportSettingMap = reportSettingMap;
	}

	/**
	 * @return the mailListDAO
	 */
	public IMailListDAO getMailListDAO() {
		return mailListDAO;
	}

	/**
	 * @param mailListDAO the mailListDAO to set
	 */
	public void setMailListDAO(IMailListDAO mailListDAO) {
		this.mailListDAO = mailListDAO;
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

}
