package com.cybersoft4u.xian.iatoms.services.dmo;

// Generated 2015/5/19 �W�� 11:15:03 by Hibernate Tools 3.4.0.CR1

import java.sql.Timestamp;
import java.util.Date;

import cafe.core.bean.dto.DataTransferObject;
import cafe.core.dmo.DomainModelObject;


/**
 * SessionLogging generated by hbm2java
 */
public class SessionLogging extends cafe.core.dmo.UserSessionLogging {

	/**
	 * Constructor:
	 */
	public SessionLogging() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor:
	 */
	public SessionLogging(DataTransferObject dto) throws Throwable {
		super(dto);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor:
	 */
	public SessionLogging(String userId, String sessionId) {
		super(userId, sessionId);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor:
	 */
	public SessionLogging(String userId, Timestamp loginTime,
			Timestamp logoutTime, String userIp, String status, String sessionId) {
		super(userId, loginTime, logoutTime, userIp, status, sessionId);
		// TODO Auto-generated constructor stub
	}
	
}
