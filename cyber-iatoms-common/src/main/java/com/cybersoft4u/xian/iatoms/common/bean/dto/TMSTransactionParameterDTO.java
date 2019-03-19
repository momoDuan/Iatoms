package com.cybersoft4u.xian.iatoms.common.bean.dto;
/**
 * Purpose: TMS參數封裝成json格式容器
 * @author TonyChen
 * @since  JDK 1.6
 * @date   2018/05/04
 * @MaintenancePersonnel TonyChen
 */
public class TMSTransactionParameterDTO {

	/**
	 * Constructor:空构造 
	 */
	public TMSTransactionParameterDTO(){}
	/**
	 * Constructor:
	 */
	//public TMSParameterDTO(String caseId,String taskId){
	//	this.caseId = caseId;
	//	this.taskId = taskId;
	//}
	
	public String TSTID;
	public String TSMID;
	public String saleTransaction;
	public String cancelTransaction;
	public String checkoutTransaction;
	public String returnTransaction;
	public String manualInput;
	public String transactionFill;
	public String transactionFillOnline;
	public String openNumber;
	public String adjustmentAmount;
	public String preAuthorization;
	public String preAuthorized;
	public String cupCancelPreAuth;
	public String cupCancelPreAuthed;
	public String autoCall;
	public String tipTransaction;
	//因變數名稱規則，因此變數名稱加上底線，最後程式處理去掉底線
	public String _4DBC;
	public String branch;
	public String MCC;
	public String veps;
	public String master;
	public String jcb;
	public String cup;
	public String transactionType;
	public String transactionTypeName;
	public String MID;
	public String MID2;
	public String TID;
	
	public String getTSTID() {
		return TSTID;
	}
	public void setTSTID(String tSTID) {
		TSTID = tSTID;
	}
	public String getTSMID() {
		return TSMID;
	}
	public void setTSMID(String tSMID) {
		TSMID = tSMID;
	}
	public String getSaleTransaction() {
		return saleTransaction;
	}
	public void setSaleTransaction(String saleTransaction) {
		this.saleTransaction = saleTransaction;
	}
	public String getCancelTransaction() {
		return cancelTransaction;
	}
	public void setCancelTransaction(String cancelTransaction) {
		this.cancelTransaction = cancelTransaction;
	}
	public String getCheckoutTransaction() {
		return checkoutTransaction;
	}
	public void setCheckoutTransaction(String checkoutTransaction) {
		this.checkoutTransaction = checkoutTransaction;
	}
	public String getReturnTransaction() {
		return returnTransaction;
	}
	public void setReturnTransaction(String returnTransaction) {
		this.returnTransaction = returnTransaction;
	}
	public String getManualInput() {
		return manualInput;
	}
	public void setManualInput(String manualInput) {
		this.manualInput = manualInput;
	}
	public String getTransactionFill() {
		return transactionFill;
	}
	public void setTransactionFill(String transactionFill) {
		this.transactionFill = transactionFill;
	}
	public String getTransactionFillOnline() {
		return transactionFillOnline;
	}
	public void setTransactionFillOnline(String transactionFillOnline) {
		this.transactionFillOnline = transactionFillOnline;
	}
	public String getOpenNumber() {
		return openNumber;
	}
	public void setOpenNumber(String openNumber) {
		this.openNumber = openNumber;
	}
	public String getAdjustmentAmount() {
		return adjustmentAmount;
	}
	public void setAdjustmentAmount(String adjustmentAmount) {
		this.adjustmentAmount = adjustmentAmount;
	}
	public String getPreAuthorization() {
		return preAuthorization;
	}
	public void setPreAuthorization(String preAuthorization) {
		this.preAuthorization = preAuthorization;
	}
	public String getPreAuthorized() {
		return preAuthorized;
	}
	public void setPreAuthorized(String preAuthorized) {
		this.preAuthorized = preAuthorized;
	}
	public String getCupCancelPreAuth() {
		return cupCancelPreAuth;
	}
	public void setCupCancelPreAuth(String cupCancelPreAuth) {
		this.cupCancelPreAuth = cupCancelPreAuth;
	}
	public String getCupCancelPreAuthed() {
		return cupCancelPreAuthed;
	}
	public void setCupCancelPreAuthed(String cupCancelPreAuthed) {
		this.cupCancelPreAuthed = cupCancelPreAuthed;
	}
	public String getAutoCall() {
		return autoCall;
	}
	public void setAutoCall(String autoCall) {
		this.autoCall = autoCall;
	}
	public String getTipTransaction() {
		return tipTransaction;
	}
	public void setTipTransaction(String tipTransaction) {
		this.tipTransaction = tipTransaction;
	}
	public String get_4DBC() {
		return _4DBC;
	}
	public void set_4DBC(String _4dbc) {
		_4DBC = _4dbc;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getMCC() {
		return MCC;
	}
	public void setMCC(String mCC) {
		MCC = mCC;
	}
	public String getVeps() {
		return veps;
	}
	public void setVeps(String veps) {
		this.veps = veps;
	}
	public String getMaster() {
		return master;
	}
	public void setMaster(String master) {
		this.master = master;
	}
	public String getJcb() {
		return jcb;
	}
	public void setJcb(String jcb) {
		this.jcb = jcb;
	}
	public String getCup() {
		return cup;
	}
	public void setCup(String cup) {
		this.cup = cup;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getTransactionTypeName() {
		return transactionTypeName;
	}
	public void setTransactionTypeName(String transactionTypeName) {
		this.transactionTypeName = transactionTypeName;
	}
	public String getMID() {
		return MID;
	}
	public void setMID(String mID) {
		MID = mID;
	}
	public String getMID2() {
		return MID2;
	}
	public void setMID2(String mID2) {
		MID2 = mID2;
	}
	public String getTID() {
		return TID;
	}
	public void setTID(String tID) {
		TID = tID;
	}

}
