package com.cybersoft4u.xian.iatoms.services.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ReportSettingDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ReportSettingFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.IAtomsDateTimeUtils;
import com.cybersoft4u.xian.iatoms.common.utils.mail.MailComponent;
import com.cybersoft4u.xian.iatoms.common.utils.mail.TemplateMailMessageDTO;
import com.cybersoft4u.xian.iatoms.services.IReportService;
import com.cybersoft4u.xian.iatoms.services.dao.IContractDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IReportSettingDAO;

/**
 * Purpose:EDC合約到期提示報表Service層接口的實現類
 * @author ElvaHe
 * @since jdk1.6
 * @date 2017年2月17日
 * @MaintenancePersonnel ElvaHe
 * 
 */
public class EdcContractExpireService extends AtomicService implements IReportService {
	
	/**
	 * 記錄日誌
	 */
	private static final CafeLog LOGGER	= CafeLogFactory.getLog(EdcContractExpireService.class);
	
	/**
	 *系統中設定的幾個月 
	 */
	private int expireMonths;
	
	/**
	 * 注入報表發送功能設定DAO
	 */
	private IReportSettingDAO reportSettingDAO;
	
	/**
	 * 注入合約DAO 
	 */
	private IContractDAO contractDAO;
	
	/**
	 * 注入發送郵件
	 */
	private MailComponent mailComponent;
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IReportService#sendReportMail(cafe.core.context.SessionContext)
	 */
	@Override
	public void sendReportMail(Date sendDate, String customerId, String reportCode) throws ServiceException {
		List<ReportSettingDTO> reportSettingList = null;
		List<BimContractDTO> contractDTO = null;
		//當前時間后幾個月的時間
		Date endDate = IAtomsDateTimeUtils.addCalendar(sendDate, 0, this.expireMonths, 0);
		//收件人的地址
		String toMail = null;
		//抄送人地址
		String ccMail = null;
		//郵件主題
		String mailSubject = i18NUtil.getName(ReportSettingFormDTO.EDC_CONTRACT_EXPIRE_SUBJECT_TEMPLATE);
		//郵件內容
		String mailContext = null;
		//郵件主題模板
		String subjectTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.EDC_CONTRACT_EXPIRE_SUBJECT_TEMPLATE;
		//郵件內容模板
		String textTemplate = MailComponent.MAIL_TEMPLATE_ADD + TemplateMailMessageDTO.EDC_CONTRACT_EXPIRE_TEXT_TEMPLATE;
		//郵件內容
		Map<String, Object> variables = new HashMap<String, Object>();
		StringBuffer buffer = new StringBuffer();
		//公司名稱
		String companyName = null;
		//合約編號
		String vendorName = null;
		//維護廠商
		String contractCode = null;
		//客戶合約到期日
		String endDateString = null;
		try {
			//查詢EDC合約到期提示報表的信息
			reportSettingList = this.reportSettingDAO.listByReportCode(customerId, reportCode);
			//判斷該報表名稱的報表信息
			if(CollectionUtils.isEmpty(reportSettingList)){
				//不存在時
				return;
			} else {
				//存在時
				//截止到結束時間的生效中的合約信息集合
				contractDTO = this.contractDAO.listEdcContractExpireInformReport(IAtomsConstants.PARAM_CONTRACT_STATUS_IN_EFFECT, sendDate, endDate);
				//若存在生效中的合約信息
				if(!CollectionUtils.isEmpty(contractDTO)){
					//生效中合約信息的筆數
					int size = contractDTO.size();
					//遍歷查詢出的合約信息，將其設置到郵件內容中
					for (int i = 0; i < size; i++) {
						//拼接郵件內容
						buffer.append("<tr>");
						buffer.append("<td style=\"border:1px solid black; width :5%; text-align:center; \">").append(i+1).append("</td>");
						companyName = contractDTO.get(i).getCompanyName();
						vendorName = contractDTO.get(i).getVendorName();
						contractCode = contractDTO.get(i).getContractCode();
						endDateString = contractDTO.get(i).getEndDateString();
						//若存在公司名稱時將其拼接到郵件內容中，不存在時拼接空值
						if(StringUtils.hasText(companyName)){
							buffer.append("<td style=\"border:1px solid black; width :30%; text-align:center; \">").append(companyName).append("</td>");
						} else {
							buffer.append("<td style=\"border:1px solid black; width :30%; text-align:center; \">").append("&nbsp;&nbsp;</td>");
						}
						//若存在廠商時將其拼接到郵件內容中，不存在時拼接空值
						if(StringUtils.hasText(vendorName)){
							buffer.append("<td style=\"border:1px solid black; width :30%; text-align:center; \">").append(vendorName).append("</td>");
						} else {
							buffer.append("<td style=\"border:1px solid black; width :30%; text-align:center; \">").append("&nbsp;&nbsp;</td>");
						}
						//若存在合約編號時將其拼接到郵件內容中，不存在時拼接空值
						if(StringUtils.hasText(contractCode)){
							buffer.append("<td style=\"border:1px solid black; width :15%; text-align:center; \">").append(contractCode).append("</td>");
						} else {
							buffer.append("<td style=\"border:1px solid black; width :15%; text-align:center; \">").append("&nbsp;&nbsp;</td>");
						}
						//若存在客戶合約到期日時將其拼接到郵件內容中，不存在時拼接空值
						if(StringUtils.hasText(endDateString)){
							buffer.append("<td style=\"border:1px solid black; width :20%; text-align:center; \">").append(endDateString).append("</td>");
						} else {
							buffer.append("<td style=\"border:1px solid black; width :20%; text-align:center; \">").append("&nbsp;&nbsp;</td>");
						}
						buffer.append("</tr>");
					}
					mailContext = buffer.toString();
				} else {
					//不存在符合條件的合約信息時郵件內容為空
					mailContext = " ";
				}
				
				//遍歷查詢出的該報表民稱的報表信息，給設置的收件人和副本人員發送郵件
				for (ReportSettingDTO reportSettingDTO : reportSettingList) {
					//收件人
					toMail = reportSettingDTO.getRecipient();
					//副本
					ccMail = reportSettingDTO.getCopy();
					//當存在收件人或副本時，發送郵件；否則的話跳出循環
					if ((!StringUtils.isEmpty(toMail)) || (!StringUtils.isEmpty(ccMail))) {
						//設置郵件內容的收件地址、CC地址、郵件主題以及郵件內容
						variables.put(BimContractDTO.ATTRIBUTE.TO_MAIL.getValue(), toMail);
						variables.put(BimContractDTO.ATTRIBUTE.CC_MAIL.getValue(), ccMail);
						variables.put(BimContractDTO.ATTRIBUTE.MAIL_SUBJECT.getValue(), mailSubject);
						variables.put(BimContractDTO.ATTRIBUTE.MAIL_CONTEXT.getValue(), mailContext);
						if (!CollectionUtils.isEmpty(contractDTO)) {
							variables.put("result", "如下");
						} else {
							variables.put("result", i18NUtil.getName(IAtomsConstants.REPORT_RESULT_IS_NULL));
						}
						this.mailComponent.sendMailTo(null, toMail, subjectTemplate, textTemplate, variables);
					} else {
						break;
					}
					
				}
			}
		} catch (Exception e) {
			LOGGER.error("sendReportMail()", "is error :", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, new String[]{this.getMyName()}, e);
		}
	}
	
	/**
	 * 
	 * Purpose:系统需于每月第一天 00:11，发送提醒邮件，提醒EDC合約到期提示.
	 * @author ElvaHe
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return void
	 */
	public void sendMailExpireInformReport() throws ServiceException {
		sendReportMail(DateTimeUtils.getCurrentDate(), null, IAtomsConstants.REPORT_NAME_EDC_CONTRACT_EXPIRE_INFO_REPORT);
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
	 * @return the contractDAO
	 */
	public IContractDAO getContractDAO() {
		return contractDAO;
	}
	/**
	 * @param contractDAO the contractDAO to set
	 */
	public void setContractDAO(IContractDAO contractDAO) {
		this.contractDAO = contractDAO;
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
	 * @return the expireMonths
	 */
	public int getExpireMonths() {
		return expireMonths;
	}
	/**
	 * @param expireMonths the expireMonths to set
	 */
	public void setExpireMonths(int expireMonths) {
		this.expireMonths = expireMonths;
	}
	
}
