package com.cybersoft4u.xian.iatoms.common.utils.mail;

import org.springframework.mail.SimpleMailMessage;

/**
 * Purpose: Mail message 封装DTO
 * @author candicechen
 * @since  JDK 1.6
 * @date   2015年8月13日
 * @MaintenancePersonnel candicechen
 */
public class SimpleMailMessageDTO extends SimpleMailMessage {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 8602435938715249054L;

	/**
	 * Constructor:无参构造函数
	 */
	public SimpleMailMessageDTO() {
		super();
	}

	/**
	 * @param original
	 */
	public SimpleMailMessageDTO(SimpleMailMessage original) {
		super(original);
	}

	
}
