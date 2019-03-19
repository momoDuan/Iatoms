package com.cybersoft4u.xian.iatoms.services.dmo;

import java.sql.Timestamp;

import cafe.core.bean.identity.LogonUser;
import cafe.core.dao.DataAccessException;
import cafe.workflow.bean.dto.CaseTransactionDTO;
import cafe.workflow.context.ITaskContext;
import cafe.workflow.dmo.AbstractCaseDetail;

/**
 * Purpose: 案件歷程DMO
 * @author RiverJin
 * @since  JDK 1.6
 * @date   2016年11月2日
 * @MaintenancePersonnel RiverJin
 */
public class CaseTransaction extends cafe.workflow.dmo.CaseTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5022490251343972253L;

	/**
	 * Constructor:构造函数
	 */
	public CaseTransaction() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor:构造函数
	 */
	public CaseTransaction(AbstractCaseDetail caseMaster) {
		super(caseMaster);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor:构造函数
	 */
	public CaseTransaction(CaseTransactionDTO dto) throws Throwable {
		super(dto);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor:构造函数
	 */
	public CaseTransaction(ITaskContext taskCtx, String action, String status,
			String substatus, LogonUser logonUser) throws DataAccessException {
		super(taskCtx, action, status, substatus, logonUser);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor:构造函数
	 */
	public CaseTransaction(String txId, AbstractCaseDetail<?> caseMaster,
			String description) {
		super(txId, caseMaster, description);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor:构造函数
	 */
	public CaseTransaction(String caseId, String activityCode,
			String activityName, String action, String status,
			String substatus, LogonUser assigner, String txDescription)
			throws DataAccessException {
		super(caseId, activityCode, activityName, action, status, substatus, assigner,
				txDescription);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor:构造函数
	 */
	public CaseTransaction(String id, String caseId, String description,
			Timestamp startTime, Timestamp endTime, String assignee,
			String assignerId, String assignerName, String activityCode,
			String action, String status, String remark) {
		super(id, caseId, description, startTime, endTime, assignee, assignerId,
				assignerName, activityCode, action, status, remark);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor:构造函数
	 */
	public CaseTransaction(String caseId, Timestamp startTime,
			String activityCode, String activityName, String action,
			String status, String substatus, LogonUser logonUser) {
		super(caseId, startTime, activityCode, activityName, action, status, substatus,
				logonUser);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor:构造函数
	 */
	public CaseTransaction(String caseId, Timestamp startTime,
			String activityCode, String activityName, String assigneeUser,
			String assigneeGroup, String assignerId, String assignerName,
			String action, String status, String substatus,
			LogonUser logonUser, String txDescription) {
		super(caseId, startTime, activityCode, activityName, assigneeUser,
				assigneeGroup, assignerId, assignerName, action, status, substatus,
				logonUser, txDescription);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor:构造函数
	 */
	public CaseTransaction(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	

}
