package com.cybersoft4u.xian.iatoms.common.utils.mail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import cafe.core.service.ServiceException;
import cafe.core.util.StringUtils;
import cafe.workflow.config.WfSystemConfigManager;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;

/**
 * Purpose: 发送mail组件
 * @author candicechen
 * @since  JDK 1.6
 * @date   2015年8月13日
 * @MaintenancePersonnel candicechen
 */
public class MailComponent {
	
	public static final String MAIL_TEMPLATE_ADD                             = "/com/cybersoft4u/xian/iatoms/templete/mail/";

	/**
	 *  系统日志记录元件
	 */
	private Log LOGGER = LogFactory.getLog(MailComponent.class);
	

	/**
	 * javaMailSender
	 */
	private JavaMailSender javaMailSender;

	/**
	 * @param javaMailSender
	 *            the javaMailSender to set
	 */
	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	/**
	 * Purpose:發送email
	 * @param message：mail訊息DTO
	 * @throws Exception：錯誤發生時拋出Exception
	 */
	public void mail(final TemplateMailMessageDTO message) throws Exception {
		if (javaMailSender == null) {
			LOGGER.warn("mailSender is not set. Switch to mock operation only.");
			return;
		}
		//組裝mail地址
		MailTargetSeparator.split(message);
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage)
					throws MessagingException {
				// from
				String from = message.getFrom();
				mimeMessage.setFrom(new InternetAddress(from));
				LOGGER.debug("from = " + from);
				// to
				Address[] to = toAddresses(message.getTo());
				mimeMessage.setRecipients(Message.RecipientType.TO, to);
				LOGGER.debug("to = " + genStringText(message.getTo()));
				// CC
				Address[] cc = toAddresses(message.getCc());
				if (cc != null) {
					mimeMessage.setRecipients(Message.RecipientType.CC, cc);
					LOGGER.debug("cc = " + genStringText(message.getCc()));
				}
				// BCC
				Address[] bcc = toAddresses(message.getBcc());
				if (bcc != null) {
					mimeMessage.setRecipients(Message.RecipientType.BCC, bcc);
					LOGGER.debug("bcc = " + genStringText(message.getBcc()));
				}
				// subject
				String subject = VelocityUtil.merge(
						findTemplate(message.getSubjectTemplate(),
								message.getCharset()),
						message.getSubjectVariables(), message.getCharset());
				mimeMessage.setSubject(subject, message.getCharset());
				LOGGER.debug("subject = " + subject);
				// context (text)
				String text = VelocityUtil.merge(
						findTemplate(message.getTextTemplate(),
								message.getCharset()),
						message.getTextVariables(), message.getCharset());
				Multipart multipart = new MimeMultipart();
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setContent(text,
						"text/html;charset=" + message.getCharset());
				multipart.addBodyPart(messageBodyPart);
				mimeMessage.setContent(multipart);
				LOGGER.debug("text = " + text);
				// attachments
				String[] attachments = message.getAttachments();
				if (attachments != null) {
					for (String attachment : attachments) {
						LOGGER.debug("attachment - " + attachment);
						File file = new File(attachment);
						messageBodyPart = new MimeBodyPart();
						DataSource source = new FileDataSource(attachment);
						messageBodyPart.setDataHandler(new DataHandler(source));
						try {
							messageBodyPart.setFileName(MimeUtility.encodeText(
									file.getName(), message.getCharset(), "B"));
						} catch (UnsupportedEncodingException ex) {
							LOGGER.warn(ex.getMessage());
							messageBodyPart.setFileName(file.getName());
						}
						LOGGER.debug("filename - " + file.getName());
						multipart.addBodyPart(messageBodyPart);
					}
				}
			}
		};
		javaMailSender.send(preparator);
	}
	/**
	 * Purpose:發送會議通知
	 * @param message：mail訊息DTO
	 * @throws Exception：錯誤發生時拋出Exception
	 */
	public void meetingNotice(final TemplateMailMessageDTO message, final String meetingPlace, 
			final String meetingStartTimeDate, final String meetingEndTimeDate) throws Exception {
		if (javaMailSender == null) {
			LOGGER.warn("mailSender is not set. Switch to mock operation only.");
			return;
		}
		//組裝mail地址
		MailTargetSeparator.split(message);
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage)
					throws MessagingException {
				// from
				String from = message.getFrom();
				mimeMessage.setFrom(new InternetAddress(from));
				LOGGER.debug("from = " + from);
				// to
				Address[] to = toAddresses(message.getTo());
				mimeMessage.setRecipients(Message.RecipientType.TO, to);
				LOGGER.debug("to = " + genStringText(message.getTo()));
				String[] toMail = message.getTo();
				String toMailAddress = "";
				for (int i=0; i<toMail.length;i++){
					if(i == 0){
						toMailAddress += toMail[i];
					} else {
						toMailAddress += IAtomsConstants.MARK_SEMICOLON+toMail[i];
					}
				}
				// subject
				String subject = VelocityUtil.merge(
						findTemplate(message.getSubjectTemplate(),
								message.getCharset()),
						message.getSubjectVariables(), message.getCharset());
				mimeMessage.setSubject(subject, message.getCharset());
				LOGGER.debug("subject = " + subject);
				// context (text)
				String text = VelocityUtil.merge(
						findTemplate(message.getTextTemplate(),
								message.getCharset()),
						message.getTextVariables(), message.getCharset());
				Multipart multipart = new MimeMultipart();
				BodyPart messageBodyPart = new MimeBodyPart();
				/*messageBodyPart.setContent(text,
						"text/html;charset=" + message.getCharset());*/
				StringBuffer buffer = new StringBuffer();  
				text = text.replaceAll("<caseMeetingBr>", "\\\\n"+IAtomsConstants.MARK_TAB
						+IAtomsConstants.MARK_TAB + IAtomsConstants.MARK_TAB +IAtomsConstants.MARK_SPACE+IAtomsConstants.MARK_SPACE);
	            buffer.append("BEGIN:VCALENDAR\n"  
	            		+ "PRODID:-//Microsoft Corporation//Outlook 9.0 MIMEDIR//EN\n"  
	                    + "VERSION:2.0\n"  
	                    + "METHOD:REQUEST\n"  
	                    + "BEGIN:VEVENT\n"  
	                    + "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:"+ toMailAddress +"\n"  
	                    + "ORGANIZER:MAILTO:"+from+"\n"  
	                    + "DTSTART:"+meetingStartTimeDate+"\n"  
	                    + "DTEND:"+meetingEndTimeDate+"\n"  
	                    + "LOCATION:"+meetingPlace+"\n"  
	                    + "UID:"+UUID.randomUUID().toString()+"\n"//如果id相同的话，outlook会认为是同一个会议请求，所以使用uuid。  
	                    + "DESCRIPTION:"+text +"\n" 
	                    + "SUMMARY: meeting request\n" 
	                    + "PRIORITY:5\n"  
	                    + "CLASS:PUBLIC\n" 
	                    + "BEGIN:VALARM\n"  
	                    + "TRIGGER:-PT15M\n" 
	                    + "ACTION:DISPLAY\n"  
	                    + "DESCRIPTION:Reminder\n" 
	                    + "END:VALARM\n"  
	                    + "END:VEVENT\n" 
	                    + "END:ICALENDAR");  
	            //BodyPart messageBodyPart = new MimeBodyPart();  
	            // 测试下来如果不这么转换的话，会以纯文本的形式发送过去，  
	            //如果没有method=REQUEST;charset=\"UTF-8\"，outlook会议附件的形式存在，而不是直接打开就是一个会议请求  
	            try {
					messageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(buffer.toString(),   
					        "text/calendar;method=REQUEST;charset=\"UTF-8\"")));
				} catch (IOException e) {
					e.printStackTrace();
				} 
				
				multipart.addBodyPart(messageBodyPart);
				mimeMessage.setContent(multipart);
				LOGGER.debug("text = " + text);
				// attachments
				String[] attachments = message.getAttachments();
				if (attachments != null) {
					for (String attachment : attachments) {
						LOGGER.debug("attachment - " + attachment);
						File file = new File(attachment);
						messageBodyPart = new MimeBodyPart();
						DataSource source = new FileDataSource(attachment);
						messageBodyPart.setDataHandler(new DataHandler(source));
						try {
							messageBodyPart.setFileName(MimeUtility.encodeText(
									file.getName(), message.getCharset(), "B"));
						} catch (UnsupportedEncodingException ex) {
							LOGGER.warn(ex.getMessage());
							messageBodyPart.setFileName(file.getName());
						}
						LOGGER.debug("filename - " + file.getName());
						multipart.addBodyPart(messageBodyPart);
					}
				}
			}
		};
		javaMailSender.send(preparator);
	}

	/**
	 * Purpose:發送email
	 * @param content
	 * @throws Exception
	 * @return void
	 */
	public void mail(SimpleMailMessageDTO content) throws Exception {
		if (javaMailSender == null) {
			mailMock(content);
			return;
		}
		// split emails
		MailTargetSeparator.split(content);
		// Create a thread safe "sandbox" of the message
		SimpleMailMessage msg = new SimpleMailMessage(content);
		try {
			LOGGER.debug("mail javaMailSender host:"+((IAtomsMailSenderImpl)javaMailSender).getHost());
			javaMailSender.send(msg);
			LOGGER.debug("mail  ");
			log(content);
		} catch (MailException ex) {
			LOGGER.error(ex, ex);
		}
	}
	
	/**
	 * Purpose:組裝mail地址
	 * @param array
	 * @throws AddressException
	 * @return Address[]
	 */
	private Address[] toAddresses(String[] array) throws AddressException {
		if (array == null || array.length == 0) {
			return null;
		}
		Address[] addresses = new Address[array.length];
		for (int i = 0; i < array.length; i++) {
			addresses[i] = new InternetAddress(array[i]);
		}
		return addresses;
	}
	
	/**
	 * Purpose:读取mail内容
	 * @param key:模板地址
	 * @param charset:编码方式
	 * @return String
	 */
	private String findTemplate(String key, String charset) {
		try {
			// look up classpath
			InputStream inStream = this.getClass().getResourceAsStream(key);
			if (inStream == null) {
				throw new IllegalStateException("resouce can not be found - "
						+ key);
			}
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, length);
			}
			return new String(outStream.toByteArray(), charset);
		} catch (Exception ex) {
			LOGGER.error(ex, ex);
			throw new IllegalStateException(ex);
		}
	}
	/**
	 * Purpose:MAIL发送器异常，则记录异常讯息
	 * @author candicechen
	 * @param content:mail message讯息
	 * @throws Exception:出错时返回Exception
	 * @return void
	 */
	private void mailMock(SimpleMailMessageDTO content) throws Exception {
		LOGGER.warn("mailSender is not set. Switch to mock operation only.");
		log(content);
	}
	/**
	 * Purpose:组装mail讯息
	 * @author candicechen
	 * @param message:错误message讯息
	 * @throws Exception:出错时返回Exception
	 * @return void
	 */
	private void log(SimpleMailMessageDTO message) throws Exception {
		LOGGER.debug("from = " + message.getFrom());
		StringBuffer buffer = new StringBuffer();
		for (String to : message.getTo()) {
			buffer.append(to + ",");
		}
		LOGGER.debug("to[] = " + buffer.toString());
		LOGGER.debug("subject = " + message.getSubject());
		LOGGER.debug("text = " + message.getText());
	}
	/**
	 * Purpose:取得字串
	 * @author candicechen
	 * @param messages:message讯息集合
	 * @return String:返回字符串
	 */
	private String genStringText(String[] messages) {
		StringBuffer buffer = new StringBuffer();
		if (messages == null) {
			buffer.append("(empty)");
		} else {
			for (String message : messages) {
				buffer.append(message);
				buffer.append(",");
			}
		}
		return buffer.toString();
	}
	/**
	 * Purpose:发送mail
	 * @author candicechen
	 * @param sender:发件人地址
	 * @param receiver:收件地址
	 * @param subjectTemplate:mial主旨
	 * @param textTemplate:mail内容
	 * @param variables:mail变量集合
	 * @throws ServiceException:出错时返回ServiceException
	 * @return void
	 */
	public void mailTo(String sender, String receiver, String subjectTemplate, String textTemplate, Map<String, Object> variables) throws ServiceException {
		try {
			if(!StringUtils.hasText(sender)){
				LOGGER.debug("MailComponent.mailTo from:Because LogonUser set FromMailAddress is Null,So,OS get System.config default fromMailAddress");
				sender = WfSystemConfigManager.getProperty(IAtomsConstants.MAIL, IAtomsConstants.MAIL_FROM_MAIL);
				LOGGER.debug("MailComponent.mailTo OS default fromMailAddress:"+sender);
			}
			LOGGER.debug("MailComponent.mailTo from:"+sender);
			LOGGER.debug("MailComponent.mailTo to:"+receiver);			
			LOGGER.debug("MailComponent.mailTo subjectTemplate:"+subjectTemplate);
			LOGGER.debug("MailComponent.mailTo textTemplate:"+textTemplate);
			TemplateMailMessageDTO message = new TemplateMailMessageDTO();
			message.setFrom(sender);
			message.setTo(receiver);
			message.setSubjectTemplate(subjectTemplate);
			message.setTextTemplate(textTemplate);
			message.setSubjectVariables(variables);
			message.setTextVariables(variables);
			if (variables.get("ccMail") != null) {
				message.setCc((String)variables.get("ccMail"));
			}
			this.mail(message);		 
		} 
		catch(Throwable e){
			LOGGER.debug("MailComponent prepareMailContent failed:"+e, e);
			throw new ServiceException(e);
		}
	}
	/**
	 * Purpose:发送會議通知
	 * @author HermanWang
	 * @param sender:发件人地址
	 * @param receiver:收件地址
	 * @param subjectTemplate:mial主旨
	 * @param textTemplate:mail内容
	 * @param variables:mail变量集合
	 * @param meetingPlace:會議地點
	 * @param meetingStartTime：會議開始時間
	 * @param meetingEndTime：會議結束時間
	 * @throws ServiceException:出错时返回ServiceException
	 * @return void
	 */
	public void meetingNoticeTo(String sender, String receiver, String subjectTemplate, 
			String textTemplate, Map<String, Object> variables, String meetingPlace, 
			String meetingStartTimeDate, String meetingEndTimeDate) throws ServiceException {
		try {
			if(!StringUtils.hasText(sender)){
				LOGGER.debug("MailComponent.meetingNoticeTo from:Because LogonUser set FromMailAddress is Null,So,OS get System.config default fromMailAddress");
				sender = WfSystemConfigManager.getProperty(IAtomsConstants.MAIL, IAtomsConstants.MAIL_FROM_MAIL);
				LOGGER.debug("MailComponent.meetingNoticeTo OS default fromMailAddress:"+sender);
			}
			LOGGER.debug("MailComponent.mailTo from:"+sender);
			LOGGER.debug("MailComponent.mailTo to:"+receiver);			
			LOGGER.debug("MailComponent.mailTo subjectTemplate:"+subjectTemplate);
			LOGGER.debug("MailComponent.mailTo textTemplate:"+textTemplate);
			LOGGER.debug("MailComponent.mailTo meetingPlace:"+meetingPlace);			
			LOGGER.debug("MailComponent.mailTo meetingStartTime:"+meetingStartTimeDate);
			LOGGER.debug("MailComponent.mailTo meetingEndTime:"+meetingEndTimeDate);
			TemplateMailMessageDTO message = new TemplateMailMessageDTO();
			message.setFrom(sender);
			message.setTo(receiver);
			message.setSubjectTemplate(subjectTemplate);
			message.setTextTemplate(textTemplate);
			message.setSubjectVariables(variables);
			message.setTextVariables(variables);
			this.meetingNotice(message, meetingPlace, meetingStartTimeDate, meetingEndTimeDate);		 
		} 
		catch(Throwable e){
			LOGGER.debug("MailComponent prepareMailContent failed:"+e, e);
			throw new ServiceException(e);
		}
	}

	/**
	 * Purpose:发送mail--增加抄送地址
	 * @param sender:发件人地址
	 * @param receiver:收件地址
	 * @param subjectTemplate:mial主旨
	 * @param textTemplate:mail内容
	 * @param variables:mail变量集合
	 * @throws ServiceException:出错时返回ServiceException
	 * @return void
	 */
	public void sendMailTo(String sender, String receiver, String subjectTemplate, String textTemplate, Map<String, Object> variables) throws ServiceException {
		try {
			if(!StringUtils.hasText(sender)){
				LOGGER.debug("MailComponent.mailTo from:Because LogonUser set FromMailAddress is Null,So,OS get System.config default fromMailAddress");
				sender = WfSystemConfigManager.getProperty(IAtomsConstants.MAIL, IAtomsConstants.MAIL_FROM_MAIL);
				LOGGER.debug("MailComponent.mailTo OS default fromMailAddress:"+sender);
			}
			LOGGER.debug("MailComponent.mailTo from:"+sender);
			LOGGER.debug("MailComponent.mailTo to:"+receiver);			
			LOGGER.debug("MailComponent.mailTo subjectTemplate:"+subjectTemplate);
			LOGGER.debug("MailComponent.mailTo textTemplate:"+textTemplate);
			TemplateMailMessageDTO message = new TemplateMailMessageDTO();
			message.setFrom(sender);
			message.setTo(receiver);
			if (!StringUtils.pathEquals((String) variables.get("ccMail"), "XXX")) {
				message.setCc(variables.get("ccMail").toString());
			}
			message.setSubjectTemplate(subjectTemplate);
			message.setTextTemplate(textTemplate);
			message.setSubjectVariables(variables);
			message.setTextVariables(variables);
			this.mail(message);		 
		} 
		catch(Throwable e){
			LOGGER.debug("MailComponent prepareMailContent failed:"+e, e);
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Purpose:发送mail--增加抄送地址
	 * @param sender:发件人地址
	 * @param receiver:收件地址
	 * @param subjectTemplate:mial主旨
	 * @param attachments:mail附件
	 * @param textTemplate:mail内容
	 * @param variables:mail变量集合
	 * @throws ServiceException:出错时返回ServiceException
	 * @return void
	 */
	public void sendMailTo(String sender, String receiver, String subjectTemplate, String[] attachments, String textTemplate, Map<String, Object> variables) throws ServiceException {
		try {
			if(!StringUtils.hasText(sender)){
				LOGGER.debug("MailComponent.mailTo from:Because LogonUser set FromMailAddress is Null,So,OS get System.config default fromMailAddress");
				sender = WfSystemConfigManager.getProperty(IAtomsConstants.MAIL, IAtomsConstants.MAIL_FROM_MAIL);
				LOGGER.debug("MailComponent.mailTo OS default fromMailAddress:"+sender);
			}
			LOGGER.debug("MailComponent.mailTo from:"+sender);
			LOGGER.debug("MailComponent.mailTo to:"+receiver);			
			LOGGER.debug("MailComponent.mailTo subjectTemplate:"+subjectTemplate);
			LOGGER.debug("MailComponent.mailTo textTemplate:"+textTemplate);
			TemplateMailMessageDTO message = new TemplateMailMessageDTO();
			message.setFrom(sender);
			message.setTo(receiver);
			if (!StringUtils.pathEquals((String) variables.get("ccMail"), "XXX")) {
				message.setCc(variables.get("ccMail").toString());
			}
			message.setSubjectTemplate(subjectTemplate);
			message.setTextTemplate(textTemplate);
			message.setSubjectVariables(variables);
			message.setAttachments(attachments);
			message.setTextVariables(variables);
			this.mail(message);		 
		} 
		catch(Throwable e){
			LOGGER.debug("MailComponent prepareMailContent failed:"+e, e);
			throw new ServiceException(e);
		}
	}
}
