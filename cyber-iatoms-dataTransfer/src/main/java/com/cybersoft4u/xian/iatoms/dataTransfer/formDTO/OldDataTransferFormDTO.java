package com.cybersoft4u.xian.iatoms.dataTransfer.formDTO;

import java.util.List;
import java.util.Map;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryHistoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAssetLinkDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAttFileDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTransactionDTO;

/**
 * 
 * Purpose: 舊資料轉檔FormDTO
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2017-7-13
 * @MaintenancePersonnel CrissZhang
 */
public class OldDataTransferFormDTO extends AbstractSimpleListFormDTO{
	
	/**
	 * 匯出模板名稱
	 */
	public static final String REPORT_JRXML_NAME						= "CASE_IMPORT_TEMPLATE_INSTALL";
	/**
	 * 匯出模板名稱
	 */
	public static final String REPORT_FILE_NAME							= "裝機舊資料";

	/**
	 * 案件處理集合
	 */
	private List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOs;
	/**
	 * 案件歷程
	 */
	private Map<String, List<SrmCaseTransactionDTO>> caseTransactionMap;
	/**
	 * 案件附加資料
	 */
	private Map<String, List<SrmCaseAttFileDTO>> caseAttFileMap;
	/**
	 * TMS資料
	 */
	private Map<String, SrmCaseHandleInfoDTO> tmsCaseInfoMap;
	/**
	 * 案件周邊設備信息集合
	 */
	private Map<String, SrmCaseHandleInfoDTO> assetCaseInfoListMap;
	/**
	 * 案件最新設備鏈接檔集合
	 */
	private Map<String, List<SrmCaseAssetLinkDTO>> caseAssetLinkMap;
	/**
	 * 設備歷史集合
	 */
	List<DmmRepositoryHistoryDTO> dmmRepositoryHistoryDTOs;
	/**
	 * 案件類別
	 */
	private String caseCategory;
	/**
	 * 可編輯字段值
	 */
	private Map<String,List<String>> editFildsMap;
	/**
	 * 判斷保存成功的log
	 */
	private Boolean saveFlag;
	
	/**
	 * 日志信息記錄
	 */
	private String logMessage;
	
	/**
	 * 行事曆資料轉入
	 */
	private boolean isTransferCalendar;
	
	/**
	 * 故障參數資料轉入
	 */
	private boolean isTransferFaultParamData;
	
	/**
	 * 公司資料轉入
	 */
	private boolean isTransferCompanyData;
	
	/**
	 * 倉庫據點資料轉入
	 */	
	private boolean isTransferWarehouse;
	
	/**
	 * 設備品項資料轉入
	 */		
	private boolean isTransferAssetType;
	
	/**
	 * 程式版本資料轉入
	 */		
	private boolean isTransferApplicaton;
	
	/**
	 * 客戶特店、表頭資料轉入
	 */			
	private boolean isTransferMerchant;
	
	/**
	 * 合約資料轉入
	 */
	private boolean isTransferContract;
	
	/**
	 * 設備庫存資料轉入
	 */
	private boolean isTransferRepository;
	
	/**
	 * 設備庫存歷史資料
	 */
	private boolean isTransferHistoryRepository;
	
	/**
	 * 案件處理資料
	 */	
	private boolean isTransferCaseHandleInfo;
	
	/**
	 * 使用者帳號資料
	 */	
	private boolean isTransferAdmUser;
	
	/**
	 * 案件最新處理資料
	 */		
	private boolean isTransferCaseNewHandleInfo;
	
	/**
	 * 報修參數資料
	 */	
	private boolean isTransferProblemData;
	/**
	 * 處理中案件轉移筆數
	 */
	private int transferCaseInfoNum;
	/**
	 * 處理中案件歷程轉移筆數
	 */
	private int transferCaseTransactionsNum;
	/**
	 * 處理中案件附加資料轉移筆數
	 */
	private int transferCaseFilesNum;
	/**
	 * 案件最新轉移筆數
	 */
	private int transferCaseNewInfoNum;
	/**
	 * 案件最新歷程轉移筆數
	 */
	private int transferCaseNewLinkNum;
	
	/**
	 * 轉入案件編號
	 */
	private String transferCaseId;
	
	/**
	 * @return the srmCaseHandleInfoDTOs
	 */
	public List<SrmCaseHandleInfoDTO> getSrmCaseHandleInfoDTOs() {
		return srmCaseHandleInfoDTOs;
	}
	/**
	 * @return the caseTransactionMap
	 */
	public Map<String, List<SrmCaseTransactionDTO>> getCaseTransactionMap() {
		return caseTransactionMap;
	}
	/**
	 * @param srmCaseHandleInfoDTOs the srmCaseHandleInfoDTOs to set
	 */
	public void setSrmCaseHandleInfoDTOs(
			List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOs) {
		this.srmCaseHandleInfoDTOs = srmCaseHandleInfoDTOs;
	}
	/**
	 * @param caseTransactionMap the caseTransactionMap to set
	 */
	public void setCaseTransactionMap(
			Map<String, List<SrmCaseTransactionDTO>> caseTransactionMap) {
		this.caseTransactionMap = caseTransactionMap;
	}
	/**
	 * @return the caseAttFileMap
	 */
	public Map<String, List<SrmCaseAttFileDTO>> getCaseAttFileMap() {
		return caseAttFileMap;
	}
	/**
	 * @param caseAttFileMap the caseAttFileMap to set
	 */
	public void setCaseAttFileMap(
			Map<String, List<SrmCaseAttFileDTO>> caseAttFileMap) {
		this.caseAttFileMap = caseAttFileMap;
	}
	/**
	 * @return the tmsCaseInfoMap
	 */
	public Map<String, SrmCaseHandleInfoDTO> getTmsCaseInfoMap() {
		return tmsCaseInfoMap;
	}
	/**
	 * @return the assetCaseInfoListMap
	 */
	public Map<String, SrmCaseHandleInfoDTO> getAssetCaseInfoListMap() {
		return assetCaseInfoListMap;
	}
	/**
	 * @return the caseAssetLinkMap
	 */
	public Map<String, List<SrmCaseAssetLinkDTO>> getCaseAssetLinkMap() {
		return caseAssetLinkMap;
	}
	/**
	 * @param tmsCaseInfoMap the tmsCaseInfoMap to set
	 */
	public void setTmsCaseInfoMap(Map<String, SrmCaseHandleInfoDTO> tmsCaseInfoMap) {
		this.tmsCaseInfoMap = tmsCaseInfoMap;
	}
	/**
	 * @param assetCaseInfoListMap the assetCaseInfoListMap to set
	 */
	public void setAssetCaseInfoListMap(
			Map<String, SrmCaseHandleInfoDTO> assetCaseInfoListMap) {
		this.assetCaseInfoListMap = assetCaseInfoListMap;
	}
	/**
	 * @param caseAssetLinkMap the caseAssetLinkMap to set
	 */
	public void setCaseAssetLinkMap(
			Map<String, List<SrmCaseAssetLinkDTO>> caseAssetLinkMap) {
		this.caseAssetLinkMap = caseAssetLinkMap;
	}
	/**
	 * @return the dmmRepositoryHistoryDTOs
	 */
	public List<DmmRepositoryHistoryDTO> getDmmRepositoryHistoryDTOs() {
		return dmmRepositoryHistoryDTOs;
	}
	/**
	 * @param dmmRepositoryHistoryDTOs the dmmRepositoryHistoryDTOs to set
	 */
	public void setDmmRepositoryHistoryDTOs(
			List<DmmRepositoryHistoryDTO> dmmRepositoryHistoryDTOs) {
		this.dmmRepositoryHistoryDTOs = dmmRepositoryHistoryDTOs;
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
	 * @return the editFildsMap
	 */
	public Map<String, List<String>> getEditFildsMap() {
		return editFildsMap;
	}
	/**
	 * @param editFildsMap the editFildsMap to set
	 */
	public void setEditFildsMap(Map<String, List<String>> editFildsMap) {
		this.editFildsMap = editFildsMap;
	}
	/**
	 * @return the saveFlag
	 */
	public Boolean getSaveFlag() {
		return saveFlag;
	}
	/**
	 * @param saveFlag the saveFlag to set
	 */
	public void setSaveFlag(Boolean saveFlag) {
		this.saveFlag = saveFlag;
	}
	/**
	 * @return the logMessage
	 */
	public String getLogMessage() {
		return logMessage;
	}
	/**
	 * @param logMessage the logMessage to set
	 */
	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}
	/**
	 * @return the isTransferCalendar
	 */
	public boolean getIsTransferCalendar() {
		return isTransferCalendar;
	}
	/**
	 * @return the isTransferFaultParamData
	 */
	public boolean getIsTransferFaultParamData() {
		return isTransferFaultParamData;
	}
	/**
	 * @return the isTransferCompanyData
	 */
	public boolean getIsTransferCompanyData() {
		return isTransferCompanyData;
	}
	/**
	 * @return the isTransferWarehouse
	 */
	public boolean getIsTransferWarehouse() {
		return isTransferWarehouse;
	}
	/**
	 * @return the isTransferAssetType
	 */
	public boolean getIsTransferAssetType() {
		return isTransferAssetType;
	}
	/**
	 * @return the isTransferApplicaton
	 */
	public boolean getIsTransferApplicaton() {
		return isTransferApplicaton;
	}
	/**
	 * @return the isTransferMerchant
	 */
	public boolean getIsTransferMerchant() {
		return isTransferMerchant;
	}
	/**
	 * @return the isTransferContract
	 */
	public boolean getIsTransferContract() {
		return isTransferContract;
	}
	/**
	 * @return the isTransferRepository
	 */
	public boolean getIsTransferRepository() {
		return isTransferRepository;
	}
	/**
	 * @return the isTransferHistoryRepository
	 */
	public boolean getIsTransferHistoryRepository() {
		return isTransferHistoryRepository;
	}
	/**
	 * @return the isTransferCaseHandleInfo
	 */
	public boolean getIsTransferCaseHandleInfo() {
		return isTransferCaseHandleInfo;
	}
	/**
	 * @return the isTransferAdmUser
	 */
	public boolean getIsTransferAdmUser() {
		return isTransferAdmUser;
	}
	/**
	 * @return the isTransferCaseNewHandleInfo
	 */
	public boolean getIsTransferCaseNewHandleInfo() {
		return isTransferCaseNewHandleInfo;
	}
	/**
	 * @return the isTransferProblemData
	 */
	public boolean getIsTransferProblemData() {
		return isTransferProblemData;
	}
	/**
	 * @param isTransferCalendar the isTransferCalendar to set
	 */
	public void setIsTransferCalendar(boolean isTransferCalendar) {
		this.isTransferCalendar = isTransferCalendar;
	}
	/**
	 * @param isTransferFaultParamData the isTransferFaultParamData to set
	 */
	public void setIsTransferFaultParamData(boolean isTransferFaultParamData) {
		this.isTransferFaultParamData = isTransferFaultParamData;
	}
	/**
	 * @param isTransferCompanyData the isTransferCompanyData to set
	 */
	public void setIsTransferCompanyData(boolean isTransferCompanyData) {
		this.isTransferCompanyData = isTransferCompanyData;
	}
	/**
	 * @param isTransferWarehouse the isTransferWarehouse to set
	 */
	public void setIsTransferWarehouse(boolean isTransferWarehouse) {
		this.isTransferWarehouse = isTransferWarehouse;
	}
	/**
	 * @param isTransferAssetType the isTransferAssetType to set
	 */
	public void setIsTransferAssetType(boolean isTransferAssetType) {
		this.isTransferAssetType = isTransferAssetType;
	}
	/**
	 * @param isTransferApplicaton the isTransferApplicaton to set
	 */
	public void setIsTransferApplicaton(boolean isTransferApplicaton) {
		this.isTransferApplicaton = isTransferApplicaton;
	}
	/**
	 * @param isTransferMerchant the isTransferMerchant to set
	 */
	public void setIsTransferMerchant(boolean isTransferMerchant) {
		this.isTransferMerchant = isTransferMerchant;
	}
	/**
	 * @param isTransferContract the isTransferContract to set
	 */
	public void setIsTransferContract(boolean isTransferContract) {
		this.isTransferContract = isTransferContract;
	}
	/**
	 * @param isTransferRepository the isTransferRepository to set
	 */
	public void setIsTransferRepository(boolean isTransferRepository) {
		this.isTransferRepository = isTransferRepository;
	}
	/**
	 * @param isTransferHistoryRepository the isTransferHistoryRepository to set
	 */
	public void setIsTransferHistoryRepository(boolean isTransferHistoryRepository) {
		this.isTransferHistoryRepository = isTransferHistoryRepository;
	}
	/**
	 * @param isTransferCaseHandleInfo the isTransferCaseHandleInfo to set
	 */
	public void setIsTransferCaseHandleInfo(boolean isTransferCaseHandleInfo) {
		this.isTransferCaseHandleInfo = isTransferCaseHandleInfo;
	}
	/**
	 * @param isTransferAdmUser the isTransferAdmUser to set
	 */
	public void setIsTransferAdmUser(boolean isTransferAdmUser) {
		this.isTransferAdmUser = isTransferAdmUser;
	}
	/**
	 * @param isTransferCaseNewHandleInfo the isTransferCaseNewHandleInfo to set
	 */
	public void setIsTransferCaseNewHandleInfo(boolean isTransferCaseNewHandleInfo) {
		this.isTransferCaseNewHandleInfo = isTransferCaseNewHandleInfo;
	}
	/**
	 * @param isTransferProblemData the isTransferProblemData to set
	 */
	public void setIsTransferProblemData(boolean isTransferProblemData) {
		this.isTransferProblemData = isTransferProblemData;
	}
	/**
	 * @return the transferCaseInfoNum
	 */
	public int getTransferCaseInfoNum() {
		return transferCaseInfoNum;
	}
	/**
	 * @param transferCaseInfoNum the transferCaseInfoNum to set
	 */
	public void setTransferCaseInfoNum(int transferCaseInfoNum) {
		this.transferCaseInfoNum = transferCaseInfoNum;
	}
	/**
	 * @return the transferCaseTransactionsNum
	 */
	public int getTransferCaseTransactionsNum() {
		return transferCaseTransactionsNum;
	}
	/**
	 * @param transferCaseTransactionsNum the transferCaseTransactionsNum to set
	 */
	public void setTransferCaseTransactionsNum(int transferCaseTransactionsNum) {
		this.transferCaseTransactionsNum = transferCaseTransactionsNum;
	}
	/**
	 * @return the transferCaseFilesNum
	 */
	public int getTransferCaseFilesNum() {
		return transferCaseFilesNum;
	}
	/**
	 * @param transferCaseFilesNum the transferCaseFilesNum to set
	 */
	public void setTransferCaseFilesNum(int transferCaseFilesNum) {
		this.transferCaseFilesNum = transferCaseFilesNum;
	}
	/**
	 * @return the transferCaseNewInfoNum
	 */
	public int getTransferCaseNewInfoNum() {
		return transferCaseNewInfoNum;
	}
	/**
	 * @param transferCaseNewInfoNum the transferCaseNewInfoNum to set
	 */
	public void setTransferCaseNewInfoNum(int transferCaseNewInfoNum) {
		this.transferCaseNewInfoNum = transferCaseNewInfoNum;
	}
	/**
	 * @return the transferCaseNewLinkNum
	 */
	public int getTransferCaseNewLinkNum() {
		return transferCaseNewLinkNum;
	}
	/**
	 * @param transferCaseNewLinkNum the transferCaseNewLinkNum to set
	 */
	public void setTransferCaseNewLinkNum(int transferCaseNewLinkNum) {
		this.transferCaseNewLinkNum = transferCaseNewLinkNum;
	}
	/**
	 * @return the transferCaseId
	 */
	public String getTransferCaseId() {
		return transferCaseId;
	}
	/**
	 * @param transferCaseId the transferCaseId to set
	 */
	public void setTransferCaseId(String transferCaseId) {
		this.transferCaseId = transferCaseId;
	}
}
