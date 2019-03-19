package com.cybersoft4u.xian.iatoms.common.utils.mail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.StringUtils;

/**
 * Purpose: 将以';'的收件地址,抄送地址拼装为数组
 * @author candicechen
 * @since  JDK 1.6
 * @date   2015年8月13日
 * @MaintenancePersonnel candicechen
 */
public class MailTargetSeparator {
	/**
	 * 符號‘;’
	 */
	private static final String Delimiter = ";";
	
	/**
	 * 系统日志记录元件
	 */
	private static Log LOGGER = LogFactory.getLog(MailTargetSeparator.class);
	
	/**
	 * Purpose:組裝地址
	 * @param message
	 */
	public static void split(SimpleMailMessage message) {
		String[] result = null;
		// To
		result = split(message.getTo());
		message.setTo(result);
		LOGGER.debug("Splitted 'To' length = " + result.length);
		
		// Cc
		result = split(message.getCc());
		message.setCc(result);
		LOGGER.debug("Splitted 'Cc' length = " + result.length);
		
		// Bcc
		result = split(message.getBcc());
		message.setBcc(result);
		LOGGER.debug("Splitted 'Bcc' length = " + result.length);
	}
	
	/**
	 * Purpose:组装mail数组
	 * @author candicechen
	 * @param emailArray:email数组
	 * @return String[]:组装后的mail数组
	 */
	public static String[] split(String[] emailArray) {
		if (emailArray == null) {
			return new String[0];
		}
		List<String> result = new ArrayList<String>();
		for (String element : emailArray) {
			String[] emails = element.split(Delimiter);
			for (String email : emails) {
				email = email.trim();
				if (StringUtils.hasText(email)) {
					result.add(email);
				}
			}
		}
		return result.toArray(new String[0]);
	}

}
