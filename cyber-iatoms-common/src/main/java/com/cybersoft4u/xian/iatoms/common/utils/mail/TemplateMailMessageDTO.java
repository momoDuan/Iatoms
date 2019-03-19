package com.cybersoft4u.xian.iatoms.common.utils.mail;

import java.util.Map;

import org.springframework.mail.SimpleMailMessage;

/**
 * Purpose: TemplateMailMessageDTO.
 * @author snipperxia
 * @since  JDK 1.5
 * @date   2013-4-2
 * @MaintenancePersonnel snipperxia
 */
 
public class TemplateMailMessageDTO extends SimpleMailMessage
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3598229064722226643L;
	
	/**
	 * EDC合約到期提示報表 -- 郵件內容模板
	 */
	public static final String EDC_CONTRACT_EXPIRE_TEXT_TEMPLATE			= "edcContractExpireContext.html";
	
	/**
	 * EDC合約到期提示報表 -- 郵件主題模板
	 */
	public static final String EDC_CONTRACT_EXPIRE_SUBJECT_TEMPLATE 		= "edcContractExpireSubject.txt";
	/**
	 * EDC報表服務需求案件(維修)分析月報 -- 郵件主題模板
	 */
	public static final String EDC_REQ_CASE_ANALYSIS_SUBJECT				= "edcReqCaseAnalysisSubject.txt";
	/**
	 * EDC報表服務需求案件(維修)分析月報 -- 郵件內容模板
	 */
	public static final String EDC_REQ_CASE_ANALYSIS_CONTEXT				= "edcReqCaseAnalysisContext.html";
	/**
	 * EDC流通在外台數報表 -- 郵件主題模板
	 */
	public static final String EDC_OUTSTANDING_NUM_SUBJECT					= "edcOutstandingNumSubject.txt";
	/**
	 * EDC流通在外台數報表 -- 郵件內容模板
	 */
	public static final String EDC_OUTSTANDING_NUM_CONTEXT					= "edcOutstandingNumContext.html";
	/**
	 * 未完修報表 -- 郵件主題模板
	 */
	public static final String UN_FINISHED_REPORT_TO_TCB_SUBJECT			= "unfinishedReportToTCBSubject.txt";
	/**
	 * 未完修報表 -- 郵件內容模板
	 */
	public static final String UN_FINISHED_REPORT_TO_TCB_CONTEXT			= "unfinishedReportToTCBContext.html";
	/**
	 * 未完修報表(環匯格式) -- 郵件主題模板
	 */
	public static final String UNFINISHED_TO_GP_SUBJECT_TEMPLATE			= "unfinishedReportToGPSubject.txt";
	/**
	 * 未完修報表(環匯格式) -- 郵件內容模板
	 */
	public static final String UNFINISHED_TO_GP_TEXT_TEMPLATE				= "unfinishedReportToGPContext.html";
	/**
	 * 完工回覆檔(環匯格式) -- 郵件主題模板
	 */
	public static final String FINISHED_REPLY_TO_GP_SUBJECT_TEMPLATE 		= "finishedReplyReportToGPSubject.txt";
	/**
	 * 完工回覆檔(環匯格式) -- 郵件內容模板
	 */
	public static final String FINISHED_REPLY_TO_GP_TEXT_TEMPLATE 			= "finishedReplyReportToGPContext.html";
	/**
	 * 案件匯入 -- 郵件主題模板
	 */
	public static final String CASE_UPLOAD_SUBJECT_TEMPLATE 				= "caseUploadSubject.txt";
	/**
	 * 案件匯入 -- 郵件內容模板
	 */
	public static final String CASE_UPLOAD_TEXT_TEMPLATE 					= "caseUploadContext.html";
	/**
	 * 完工回覆檔(大眾格式) -- 郵件主題模板
	 */
	public static final String FINISHED_REPLY_TO_TCB_SUBJECT_TEMPLATE		= "finishedReplyReportToTCBSubject.txt";	
	/**
	 * 完工回覆檔(大眾格式) -- 郵件內容模板
	 */
	public static final String FINISHED_REPLY_TO_TCB_TEXT_TEMPLATE			= "finishedReplyReportToTCBContext.html";
	/**
	 * 案件報修明細（環匯格式） -- 郵件主題模板
	 */
	public static final String CASE_REPAIR_DETAIL_REPORT_TO_GP_SUBJECT_TEMPLATE = "caseRepairDetailReportToGPSubject.txt";
	/**
	 * 案件報修明細（環匯格式） -- 郵件內容模板
	 */
	public static final String CASE_REPAIR_DETAIL_REPORT_TO_GP_TEXT_TEMPLATE = "caseRepairDetailReportToGPContext.html";
	/**
	 * 完修逾時率警示通知 -- 郵件主題模板
	 */
	public static final String COMPLETE_OVERDUE_RATE_REPORT_SUBJECT			= "completeOverdueRateReportSubject.txt";
	/**
	 * 完修逾時率警示通知 -- 郵件內容模板
	 */
	public static final String COMPLETE_OVERDUE_RATE_REPORT_CONTEXT			= "completeOverdueRateReportContext.html";
	/**
	 * 案件設備明細（環匯格式） -- 郵件內容模板
	 */
	public static final String CASE_ASSET_DETAIL_REPORT_TO_GP_TEXT_TEMPLATE	= "caseAssetDetailReportToGPContext.html";
	/**
	 * 案件設備明細（環匯格式） -- 郵件主題模板
	 */
	public static final String CASE_ASSET_DETAIL_REPORT_TO_GP_SUBJECT_TEMPLATE = "caseAssetDetailReportToGPSubject.txt";
	/**
	 * 完工回覆檔(歐付寶格式) -- 郵件主題模板
	 */
	public static final String FINISHED_REPLY_TO_OFB_SUBJECT_TEMPLATE		= "finishedReplyReportToOFBSubject.txt";
	/**
	 * 完工回覆檔(歐付寶格式) -- 郵件內容模板
	 */
	public static final String FINISHED_REPLY_TO_OFB_TEXT_TEMPLATE			= "finishedReplyReportToOFBContext.html";
	/**
	 * 報表重送出現異常 -- 郵件主題模板
	 */
	public static final String REPORT_SEND_EXCEPTION_SUBJECT_TEMPLATE		= "reportSettingSendExceptionSubject.txt";
	/**
	 * 報表重送出現異常 -- 郵件內容模板
	 */
	public static final String REPORT_SEND_EXCEPTION_TEXT_TEMPLATE			= "reportSettingSendExceptionContext.html";
	/**
	 * 維護費報表(大眾格式) -- Mail主题模板
	 */
	public static final String EDC_FEE_REPORT_FOR_TCB_SUBJECT_TEMPLATE		= "edcFeeReportForTcbSubject.txt";
	/**
	 * 維護費報表(大眾格式) -- Mail内容模板
	 */
	public static final String EDC_FEE_REPORT_FOR_TCB_TEXT_TEMPLATE			= "edcFeeReportForTcbContext.html";
	/**
	 * 维护费报表(捷达威格式) -- Mail主题模板
	 */
	public static final String EDC_FEE_REPORT_FOR_JDW_SUBJECT_TEMPLATE		= "edcFeeReportForJdwSubject.txt";
	/**
	 * 维护费报表(捷达威格式) -- Mail内容模板
	 */
	public static final String EDC_FEE_REPORT_FOR_JDW_TEXT_TEMPLATE			= "edcFeeReportForJdwContext.html";
	/**
	 * 維護費報表(綠界格式) -- Mail主题模板
	 */
	public static final String EDC_FEE_REPORT_FOR_GREEN_WORLD_SUBJECT_TEMPLATE = "edcFeeReportForGreenWorldSubject.txt";
	/**
	 * 維護費報表(綠界格式) -- Mail内容模板
	 */
	public static final String EDC_FEE_REPORT_FOR_GREEN_WORLD_TEXT_TEMPLATE = "edcFeeReportForGreenWorldContext.html";
	/**
	 * 維護費報表(上銀格式) -- Mail主题模板
	 */
	public static final String EDC_FEE_REPORT_FOR_SCSB_SUBJECT_TEMPLATE = "edcFeeReportForScsbSubject.txt";
	/**
	 * 維護費報表(上銀格式) -- Mail内容模板
	 */
	public static final String EDC_FEE_REPORT_FOR_SCSB_TEXT_TEMPLATE = "edcFeeReportForScsbContext.html";
	/**
	 * 维护费报表(環匯格式) -- Mail主题模板
	 */
	public static final String EDC_FEE_REPORT_FOR_GP_SUBJECT				= "edcFeeReportForGpSubject.txt";
	/**
	 * 维护费报表(環匯格式) -- Mail内容模板
	 */
	public static final String EDC_FEE_REPORT_FOR_GP_CONTEXT				= "edcFeeReportForGpContext.html";
	/**
	 * Find a template of a subject in following sequence :<br>
	 * 1. Find in DB by applying 'subjectTemplate' as a key.
	 * 2. Find in classpath by applying 'subjectTemplate' as a classpath name.
	 */
	private String subjectTemplate;
	
	/**
	 * replace ${xxx} in subjectTemplate by varialbes
	 */
	private Map<String, Object> subjectVariables;
	
	/**
	 * Find a template of text(mail content) in following sequence :<br>
	 * 1. Find in DB by applying 'subjectTemplate' as a key.
	 * 2. Find in classpath by applying 'subjectTemplate' as a classpath name.
	 */
	private String textTemplate;
	
	/**
	 * replace ${xxx} in textTemplate by varialbes
	 */
	private Map<String, Object> textVariables;
	
	/**
	 * Character Set
	 */
	private String charset = "UTF-8";
	
	/**
	 * Attachments : full path filename
	 */
	private String[] attachments;

	/**
	 * 
	 */
	public TemplateMailMessageDTO() {
		super();
	}

	/**
	 * @return the subjectTemplate
	 */
	public String getSubjectTemplate() {
		return subjectTemplate;
	}

	/**
	 * @param subjectTemplate the subjectTemplate to set
	 */
	public void setSubjectTemplate(String subjectTemplate) {
		this.subjectTemplate = subjectTemplate;
	}

	/**
	 * @return the subjectVariables
	 */
	public Map<String, Object> getSubjectVariables() {
		return subjectVariables;
	}

	/**
	 * @param subjectVariables the subjectVariables to set
	 */
	public void setSubjectVariables(Map<String, Object> subjectVariables) {
		this.subjectVariables = subjectVariables;
	}

	/**
	 * @return the textTemplate
	 */
	public String getTextTemplate() {
		return textTemplate;
	}

	/**
	 * @param textTemplate the textTemplate to set
	 */
	public void setTextTemplate(String textTemplate) {
		this.textTemplate = textTemplate;
	}

	/**
	 * @return the textVariables
	 */
	public Map<String, Object> getTextVariables() {
		return textVariables;
	}

	/**
	 * @param textVariables the textVariables to set
	 */
	public void setTextVariables(Map<String, Object> textVariables) {
		this.textVariables = textVariables;
	}

	/**
	 * @return the charset
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * @param charset the charset to set
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * @return the attachments
	 */
	public String[] getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(String[] attachments) {
		this.attachments = attachments;
	}

}
