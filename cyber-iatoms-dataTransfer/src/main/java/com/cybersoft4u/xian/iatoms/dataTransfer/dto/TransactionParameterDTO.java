package com.cybersoft4u.xian.iatoms.dataTransfer.dto;

import cafe.core.bean.dto.DataTransferObject;

/**
 * Purpose:案件交易參數 
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016年12月16日
 * @MaintenancePersonnel CrissZhang
 */
public class TransactionParameterDTO extends DataTransferObject<String> {
	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -1840468113387391210L;
	
	/**
	 * dtid
	 */
	private String dtid;

	/**
	 * 案件編號
	 */
	private String caseId;
	/**
	 * 終端機編號
	 */
	private String tid;
	/**
	 * master
	 */
	private String master;
	/**
	 * jcb
	 */
	private String jcb;	
	/**
	 * 退貨交易
	 */
	private String returnTransaction;
	/**
	 * 人工手輸
	 */
	private String manualInput;
	
	/**
	 * 小費交易
	 */
	private String tipTransaction;
	
	/**
	 * @return the dtid
	 */
	public String getDtid() {
		return dtid;
	}
	/**
	 * @param dtid the dtid to set
	 */
	public void setDtid(String dtid) {
		this.dtid = dtid;
	}
	/**
	 * @return the caseId
	 */
	public String getCaseId() {
		return caseId;
	}
	/**
	 * @param caseId the caseId to set
	 */
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	/**
	 * @return the tid
	 */
	public String getTid() {
		return tid;
	}
	/**
	 * @param tid the tid to set
	 */
	public void setTid(String tid) {
		this.tid = tid;
	}
	/**
	 * @return the master
	 */
	public String getMaster() {
		return master;
	}
	/**
	 * @param master the master to set
	 */
	public void setMaster(String master) {
		this.master = master;
	}
	/**
	 * @return the jcb
	 */
	public String getJcb() {
		return jcb;
	}
	/**
	 * @param jcb the jcb to set
	 */
	public void setJcb(String jcb) {
		this.jcb = jcb;
	}
	/**
	 * @return the returnTransaction
	 */
	public String getReturnTransaction() {
		return returnTransaction;
	}
	/**
	 * @param returnTransaction the returnTransaction to set
	 */
	public void setReturnTransaction(String returnTransaction) {
		this.returnTransaction = returnTransaction;
	}
	/**
	 * @return the manualInput
	 */
	public String getManualInput() {
		return manualInput;
	}
	/**
	 * @param manualInput the manualInput to set
	 */
	public void setManualInput(String manualInput) {
		this.manualInput = manualInput;
	}
	/**
	 * @return the tipTransaction
	 */
	public String getTipTransaction() {
		return tipTransaction;
	}
	/**
	 * @param tipTransaction the tipTransaction to set
	 */
	public void setTipTransaction(String tipTransaction) {
		this.tipTransaction = tipTransaction;
	}
	
}
