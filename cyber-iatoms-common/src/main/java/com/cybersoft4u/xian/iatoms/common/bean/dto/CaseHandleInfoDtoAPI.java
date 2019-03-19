package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.util.List;

import cafe.core.bean.dto.DataTransferObject;

public class CaseHandleInfoDtoAPI extends DataTransferObject<String>{

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 8119520869284568672L;
	/**
	 * 通訊模式
	 */
	private String connectionTypeName;
	/**
	 * 內建功能
	 */
	private String builtInFeature;
	/**
	 * 週邊設備
	 */
	private String peripheralsName;
	/**
	 * 週邊設備2
	 */
	private String peripherals2Name;
	/**
	 * 週邊設備3
	 */
	private String peripherals3Name;
	/**
	 * Receipt_type中文
	 */
	private String receiptTypeName;
	/**
	 * 案件交易參數
	 */
	private List<SrmCaseTransactionParameterDTO> itemValue;
	/**
	 * 週邊設備功能
	 */
	private String peripheralsFunction;
	/**
	 * 週邊設備功能2
	 */
	private String peripheralsFunction2;
	/**
	 * 週邊設備功能3
	 */
	private String peripheralsFunction3;
	/**
	 * AOName
	 */
	private String aoName;
	/**
	 * 案件類別
	 */
	private String caseCategory;
	/**
	 * 刷卡機類型
	 */
	private String edcTypeName;
	/**
	 * 公司名稱
	 */
	private String customerName;
	
	/**
	 * 特店代號
	 */
	private String merchantCode;
	
	/**
	 * 特店表頭名稱
	 */
	private String headerName;
	/**
	 * @return the connectionTypeName
	 */
	public String getConnectionTypeName() {
		return connectionTypeName;
	}
	/**
	 * @param connectionTypeName the connectionTypeName to set
	 */
	public void setConnectionTypeName(String connectionTypeName) {
		this.connectionTypeName = connectionTypeName;
	}
	/**
	 * @return the builtInFeature
	 */
	public String getBuiltInFeature() {
		return builtInFeature;
	}
	/**
	 * @param builtInFeature the builtInFeature to set
	 */
	public void setBuiltInFeature(String builtInFeature) {
		this.builtInFeature = builtInFeature;
	}
	/**
	 * @return the peripheralsName
	 */
	public String getPeripheralsName() {
		return peripheralsName;
	}
	/**
	 * @param peripheralsName the peripheralsName to set
	 */
	public void setPeripheralsName(String peripheralsName) {
		this.peripheralsName = peripheralsName;
	}
	/**
	 * @return the peripherals2Name
	 */
	public String getPeripherals2Name() {
		return peripherals2Name;
	}
	/**
	 * @param peripherals2Name the peripherals2Name to set
	 */
	public void setPeripherals2Name(String peripherals2Name) {
		this.peripherals2Name = peripherals2Name;
	}
	/**
	 * @return the peripherals3Name
	 */
	public String getPeripherals3Name() {
		return peripherals3Name;
	}
	/**
	 * @param peripherals3Name the peripherals3Name to set
	 */
	public void setPeripherals3Name(String peripherals3Name) {
		this.peripherals3Name = peripherals3Name;
	}
	/**
	 * @return the receiptTypeName
	 */
	public String getReceiptTypeName() {
		return receiptTypeName;
	}
	/**
	 * @param receiptTypeName the receiptTypeName to set
	 */
	public void setReceiptTypeName(String receiptTypeName) {
		this.receiptTypeName = receiptTypeName;
	}
	
	/**
	 * @return the itemValue
	 */
	public List<SrmCaseTransactionParameterDTO> getItemValue() {
		return itemValue;
	}
	/**
	 * @param itemValue the itemValue to set
	 */
	public void setItemValue(List<SrmCaseTransactionParameterDTO> itemValue) {
		this.itemValue = itemValue;
	}
	/**
	 * @return the peripheralsFunction
	 */
	public String getPeripheralsFunction() {
		return peripheralsFunction;
	}
	/**
	 * @param peripheralsFunction the peripheralsFunction to set
	 */
	public void setPeripheralsFunction(String peripheralsFunction) {
		this.peripheralsFunction = peripheralsFunction;
	}
	/**
	 * @return the peripheralsFunction2
	 */
	public String getPeripheralsFunction2() {
		return peripheralsFunction2;
	}
	/**
	 * @param peripheralsFunction2 the peripheralsFunction2 to set
	 */
	public void setPeripheralsFunction2(String peripheralsFunction2) {
		this.peripheralsFunction2 = peripheralsFunction2;
	}
	/**
	 * @return the peripheralsFunction3
	 */
	public String getPeripheralsFunction3() {
		return peripheralsFunction3;
	}
	/**
	 * @param peripheralsFunction3 the peripheralsFunction3 to set
	 */
	public void setPeripheralsFunction3(String peripheralsFunction3) {
		this.peripheralsFunction3 = peripheralsFunction3;
	}
	/**
	 * @return the aoName
	 */
	public String getAoName() {
		return aoName;
	}
	/**
	 * @param aoName the aoName to set
	 */
	public void setAoName(String aoName) {
		this.aoName = aoName;
	}
	/**
	 * @return the caseCategory
	 */
	public String getCaseCategory() {
		return caseCategory;
	}
	/**
	 * @param caseCategory the caseCategory to set
	 */
	public void setCaseCategory(String caseCategory) {
		this.caseCategory = caseCategory;
	}
	/**
	 * @return the edcTypeName
	 */
	public String getEdcTypeName() {
		return edcTypeName;
	}
	/**
	 * @param edcTypeName the edcTypeName to set
	 */
	public void setEdcTypeName(String edcTypeName) {
		this.edcTypeName = edcTypeName;
	}
	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	/**
	 * @return the merchantCode
	 */
	public String getMerchantCode() {
		return merchantCode;
	}
	/**
	 * @param merchantCode the merchantCode to set
	 */
	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}
	/**
	 * @return the headerName
	 */
	public String getHeaderName() {
		return headerName;
	}
	/**
	 * @param headerName the headerName to set
	 */
	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
	
}
