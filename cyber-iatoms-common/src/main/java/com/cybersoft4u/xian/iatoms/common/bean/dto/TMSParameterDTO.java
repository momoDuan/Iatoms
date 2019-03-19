package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.util.List;

/**
 * Purpose: TMS參數封裝成json格式容器
 * @author TonyChen
 * @since  JDK 1.6
 * @date   2018/05/04
 * @MaintenancePersonnel TonyChen
 */
public class TMSParameterDTO {

	/**
	 * Constructor:空构造 
	 */
	public TMSParameterDTO(){}
	/**
	 * Constructor:
	 */
	//public TMSParameterDTO(String caseId,String taskId){
	//	this.caseId = caseId;
	//	this.taskId = taskId;
	//}
	
	public String batch_num;
	public String CASE_ID;				
	public String DTID;
	public String CUSTOMER_ID;
	public String CASE_CATEGORY;
	public String INSTALL_TYPE;
	public String MERCHANT_HEADER_ID;
	public String INSTALLED_ADRESS_LOCATION;
	public String INSTALLED_ADRESS;		
	public String IS_OPEN_ENCRYPT;
	public String ELECTRONIC_PAY_PLATFORM;
	public String EDC_TYPE;
	public String SOFTWARE_VERSION;
	public String CONNECTION_TYPE;
	public String ECR_CONNECTION;
	public String ELECTRONIC_INVOICE;
	public String NET_VENDOR_ID;
	public String BUILT_IN_FEATURE;
	public String PERIPHERALS;
	public String PERIPHERALS_FUNCTION;
	public String PERIPHERALS2;
	public String PERIPHERALS_FUNCTION2;
	public String PERIPHERALS3;
	public String PERIPHERALS_FUNCTION3;
	public String CUP_QUICK_PASS;
	public String DESCRIPTION;
	public String ACCOUNT;
	public List<TMSTransactionParameterDTO> TXParam;
	
	public String getBatch_num() {
		return batch_num;
	}
	public void setBatch_num(String batch_num) {
		this.batch_num = batch_num;
	}
	public String getCASE_ID() {
		return CASE_ID;
	}
	public void setCASE_ID(String cASE_ID) {
		CASE_ID = cASE_ID;
	}
	public String getDTID() {
		return DTID;
	}
	public void setDTID(String dTID) {
		DTID = dTID;
	}
	public String getCUSTOMER_ID() {
		return CUSTOMER_ID;
	}
	public void setCUSTOMER_ID(String cUSTOMER_ID) {
		CUSTOMER_ID = cUSTOMER_ID;
	}
	public String getCASE_CATEGORY() {
		return CASE_CATEGORY;
	}
	public void setCASE_CATEGORY(String cASE_CATEGORY) {
		CASE_CATEGORY = cASE_CATEGORY;
	}
	public String getINSTALL_TYPE() {
		return INSTALL_TYPE;
	}
	public void setINSTALL_TYPE(String iNSTALL_TYPE) {
		INSTALL_TYPE = iNSTALL_TYPE;
	}
	public String getMERCHANT_HEADER_ID() {
		return MERCHANT_HEADER_ID;
	}
	public void setMERCHANT_HEADER_ID(String mERCHANT_HEADER_ID) {
		MERCHANT_HEADER_ID = mERCHANT_HEADER_ID;
	}
	public String getINSTALLED_ADRESS_LOCATION() {
		return INSTALLED_ADRESS_LOCATION;
	}
	public void setINSTALLED_ADRESS_LOCATION(String iNSTALLED_ADRESS_LOCATION) {
		INSTALLED_ADRESS_LOCATION = iNSTALLED_ADRESS_LOCATION;
	}
	public String getINSTALLED_ADRESS() {
		return INSTALLED_ADRESS;
	}
	public void setINSTALLED_ADRESS(String iNSTALLED_ADRESS) {
		INSTALLED_ADRESS = iNSTALLED_ADRESS;
	}
	public String getIS_OPEN_ENCRYPT() {
		return IS_OPEN_ENCRYPT;
	}
	public void setIS_OPEN_ENCRYPT(String iS_OPEN_ENCRYPT) {
		IS_OPEN_ENCRYPT = iS_OPEN_ENCRYPT;
	}
	public String getELECTRONIC_PAY_PLATFORM() {
		return ELECTRONIC_PAY_PLATFORM;
	}
	public void setELECTRONIC_PAY_PLATFORM(String eLECTRONIC_PAY_PLATFORM) {
		ELECTRONIC_PAY_PLATFORM = eLECTRONIC_PAY_PLATFORM;
	}
	public String getEDC_TYPE() {
		return EDC_TYPE;
	}
	public void setEDC_TYPE(String eDC_TYPE) {
		EDC_TYPE = eDC_TYPE;
	}
	public String getSOFTWARE_VERSION() {
		return SOFTWARE_VERSION;
	}
	public void setSOFTWARE_VERSION(String sOFTWARE_VERSION) {
		SOFTWARE_VERSION = sOFTWARE_VERSION;
	}
	public String getCONNECTION_TYPE() {
		return CONNECTION_TYPE;
	}
	public void setCONNECTION_TYPE(String cONNECTION_TYPE) {
		CONNECTION_TYPE = cONNECTION_TYPE;
	}
	public String getECR_CONNECTION() {
		return ECR_CONNECTION;
	}
	public void setECR_CONNECTION(String eCR_CONNECTION) {
		ECR_CONNECTION = eCR_CONNECTION;
	}
	public String getELECTRONIC_INVOICE() {
		return ELECTRONIC_INVOICE;
	}
	public void setELECTRONIC_INVOICE(String eLECTRONIC_INVOICE) {
		ELECTRONIC_INVOICE = eLECTRONIC_INVOICE;
	}
	public String getNET_VENDOR_ID() {
		return NET_VENDOR_ID;
	}
	public void setNET_VENDOR_ID(String nET_VENDOR_ID) {
		NET_VENDOR_ID = nET_VENDOR_ID;
	}
	public String getBUILT_IN_FEATURE() {
		return BUILT_IN_FEATURE;
	}
	public void setBUILT_IN_FEATURE(String bUILT_IN_FEATURE) {
		BUILT_IN_FEATURE = bUILT_IN_FEATURE;
	}
	public String getPERIPHERALS() {
		return PERIPHERALS;
	}
	public void setPERIPHERALS(String pERIPHERALS) {
		PERIPHERALS = pERIPHERALS;
	}
	public String getPERIPHERALS_FUNCTION() {
		return PERIPHERALS_FUNCTION;
	}
	public void setPERIPHERALS_FUNCTION(String pERIPHERALS_FUNCTION) {
		PERIPHERALS_FUNCTION = pERIPHERALS_FUNCTION;
	}
	public String getPERIPHERALS2() {
		return PERIPHERALS2;
	}
	public void setPERIPHERALS2(String pERIPHERALS2) {
		PERIPHERALS2 = pERIPHERALS2;
	}
	public String getPERIPHERALS_FUNCTION2() {
		return PERIPHERALS_FUNCTION2;
	}
	public void setPERIPHERALS_FUNCTION2(String pERIPHERALS_FUNCTION2) {
		PERIPHERALS_FUNCTION2 = pERIPHERALS_FUNCTION2;
	}
	public String getPERIPHERALS3() {
		return PERIPHERALS3;
	}
	public void setPERIPHERALS3(String pERIPHERALS3) {
		PERIPHERALS3 = pERIPHERALS3;
	}
	public String getPERIPHERALS_FUNCTION3() {
		return PERIPHERALS_FUNCTION3;
	}
	public void setPERIPHERALS_FUNCTION3(String pERIPHERALS_FUNCTION3) {
		PERIPHERALS_FUNCTION3 = pERIPHERALS_FUNCTION3;
	}
	public String getCUP_QUICK_PASS() {
		return CUP_QUICK_PASS;
	}
	public void setCUP_QUICK_PASS(String cUP_QUICK_PASS) {
		CUP_QUICK_PASS = cUP_QUICK_PASS;
	}
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}
	public String getACCOUNT() {
		return ACCOUNT;
	}
	public void setACCOUNT(String aCCOUNT) {
		ACCOUNT = aCCOUNT;
	}
	public List<TMSTransactionParameterDTO> getTXParam() {
		return TXParam;
	}
	public void setTXParam(List<TMSTransactionParameterDTO> tXParam) {
		TXParam = tXParam;
	}
	
	

}
