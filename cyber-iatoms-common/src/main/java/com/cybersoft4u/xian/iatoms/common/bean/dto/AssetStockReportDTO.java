package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.util.Date;

import cafe.core.bean.dto.DataTransferObject;


/**
 * Purpose: 設備庫存表DTO
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016-7-28
 * @MaintenancePersonnel CrissZhang
 */
public class AssetStockReportDTO extends DataTransferObject<String>{

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 6876889904472058265L;
	
	/**
	 * Purpose: 設備庫存ATTRIBUTE
	 * @author CrissZhang
	 * @since  JDK 1.6
	 * @date   2017/1/23
	 * @MaintenancePersonnel CrissZhang
	 */
	public static enum ATTRIBUTE {
		YYYYMM("yyyymm"),
		MA_TYPE_NAME("maTypeName"),
		MA_TYPE("maType"),
		ASSET_TYPE_ID("assetTypeId"),
		ASSET_TYPE_NAME("assetTypeName"),
		SUPPORED_FUNCTION("supporedFunction"),
		SUPPORED_FUNCTION_NAME("supporedFunctionName"),
		COMPANY_ID("companyId"),
		COMPANY_NAME("companyName"),
		PURCHASE_YEAR_MONTH("purchaseYearMonth"),
		CONTRACT_ID("contractId"),
		PURCHASE_NUMBER("purchaseNumber"),
		STORAGE_NUMBER("storageNumber"),
		PREPARATION_NUMBER("preparationNumber"),
		DESTORYED_NUMBER("destoryedNumber"),
		SCRAPED_NUMBER("scrapedNumber"),
		LOSE_NUMBER("loseNumber"),
		MAINTENANCE_NUMBER("maintenanceNumber"),
		REPAIRED_NUMBER("repairedNumber"),
		STEP_NUMBER("stepNumber"),
		AVAILABLE_NUMBER("availableNumber"),
		ASSET_CATEGORY("assetCategory"),
		ASSET_CATEGORY_NAME("assetCategoryName"),
		CURRENT_DATE("currentDate"),
		SUM_PURCHASE_NUMBER("sumPurchaseNumber"),
		SUM_STORAGE_NUMBER("sumStorageNumber"),
		SUM_PREPARATION_NUMBER("sumPreparationNumber"),
		SUM_DESTORYED_NUMBER("sumDestoryedNumber"),
		SUM_SCRAPED_NUMBER("sumScrapedNumber"),
		SUM_LOSE_NUMBER("sumLoseNumber"),
		SUM_MAINTENANCE_NUMBER("sumMaintenanceNumber"),
		SUM_STEP_NUMBER("sumStepNumber"),
		SUM_AVAILABLE_NUMBER("sumAvailableNumber"),
		TITLE_NAME("titleName"),
		CONTRACT("contract");

		/**
		 * value值
		 */
		private String value;
		
		/**
		 * Constructor:構造函數
		 */
		ATTRIBUTE(String value) {
			this.value = value;
		};
		
		/**
		 * @return the value
		 */
		public String getValue() {
			return this.value;
		}
	};
	
	/**
	 * Constructor:空的構造函數
	 */
	public AssetStockReportDTO() {
	}
	/**
	 * 日期格式
	 */
	private String yyyymm;
	/**
	 * 通訊模式名稱
	 */
	private String maTypeName;
	/**
	 * 通訊模式
	 */
	private String maType;
	/**
	 * 機型
	 */
	private String assetTypeId;
	/**
	 * 機型名稱
	 */
	private String assetTypeName;
	/**
	 * 設備支援功能
	 */
	private String supporedFunction;
	/**
	 * 設備支援功能名稱
	 */
	private String supporedFunctionName;
	/**
	 * 廠商
	 */
	private String companyId;
	/**
	 * 廠商名稱
	 */
	private String companyName;
	/**
	 * 采购年月
	 */
	private Date purchaseYearMonth;
	/**
	 * 合约编号
	 */
	private String contractId;
	/**
	 * 采购臺數
	 */
	private long purchaseNumber;
	/**
	 * 入庫臺數
	 */
	private Integer storageNumber;
	/**
	 * 備機臺數
	 */
	private Integer preparationNumber;
	/**
	 * 已銷毀
	 */
	private Integer destoryedNumber;
	/**
	 * 已報廢
	 */
	private Integer scrapedNumber;
	/**
	 * 丟失
	 */
	private Integer loseNumber;
	/**
	 * 送修中
	 */
	private Integer maintenanceNumber;
	
	/**
	 * 維修中
	 */
	private Integer repairedNumber;
	/**
	 * 已步臺數
	 */
	private Integer stepNumber;
	/**
	 * 可用臺數
	 */
	private Integer availableNumber;
	/**
	 * 设备状态
	 */
	private String assetCategory;
	/**
	 * 设备状态名称
	 */
	private String assetCategoryName;
	/**
	 * 當前日期
	 */
	private String currentDate;
	/**
	 * 总计采购臺數
	 */
	private long sumPurchaseNumber;
	/**
	 * 总计入庫臺數
	 */
	private Integer sumStorageNumber;
	/**
	 * 总计備機臺數
	 */
	private Integer sumPreparationNumber;
	/**
	 * 总计已銷毀臺數
	 */
	private Integer sumDestoryedNumber;
	/**
	 * 总计已報廢臺數
	 */
	private Integer sumScrapedNumber;
	/**
	 * 总计丟失臺數
	 */
	private Integer sumLoseNumber;
	/**
	 * 总计丟失臺數
	 */
	private Integer sumMaintenanceNumber;
	/**
	 * 总计已步臺數
	 */
	private Integer sumStepNumber;
	/**
	 * 总计可用臺數
	 */
	private Integer sumAvailableNumber;
	
	/**
	 * 总计維修臺數
	 */
	private Integer sumRepairedNumber;
	/**
	 * 標題名稱
	 */
	private String titleName;
	/**
	 * 合約id
	 */
	private String contract;
	
	
	/**
	 * Constructor:有參構造函數
	 */
	public AssetStockReportDTO(String yyyymm, String maTypeName, String maType,
			String assetTypeId, String assetTypeName, String supporedFunction,
			String supporedFunctionName, String companyId, String companyName,
			Date purchaseYearMonth, String contractId, Integer purchaseNumber,
			Integer storageNumber, Integer preparationNumber,
			Integer destoryedNumber, Integer scrapedNumber, Integer loseNumber,
			Integer maintenanceNumber, Integer stepNumber,
			Integer availableNumber, String assetCategory,
			String assetCategoryName, String currentDate,
			long sumPurchaseNumber, Integer sumStorageNumber,
			Integer sumPreparationNumber, Integer sumDestoryedNumber,
			Integer sumScrapedNumber, Integer sumLoseNumber,
			Integer sumMaintenanceNumber, Integer sumStepNumber,
			Integer sumAvailableNumber, String titleName) {
		super();
		this.yyyymm = yyyymm;
		this.maTypeName = maTypeName;
		this.maType = maType;
		this.assetTypeId = assetTypeId;
		this.assetTypeName = assetTypeName;
		this.supporedFunction = supporedFunction;
		this.supporedFunctionName = supporedFunctionName;
		this.companyId = companyId;
		this.companyName = companyName;
		this.purchaseYearMonth = purchaseYearMonth;
		this.contractId = contractId;
		this.purchaseNumber = purchaseNumber;
		this.storageNumber = storageNumber;
		this.preparationNumber = preparationNumber;
		this.destoryedNumber = destoryedNumber;
		this.scrapedNumber = scrapedNumber;
		this.loseNumber = loseNumber;
		this.maintenanceNumber = maintenanceNumber;
		this.stepNumber = stepNumber;
		this.availableNumber = availableNumber;
		this.assetCategory = assetCategory;
		this.assetCategoryName = assetCategoryName;
		this.currentDate = currentDate;
		this.sumPurchaseNumber = sumPurchaseNumber;
		this.sumStorageNumber = sumStorageNumber;
		this.sumPreparationNumber = sumPreparationNumber;
		this.sumDestoryedNumber = sumDestoryedNumber;
		this.sumScrapedNumber = sumScrapedNumber;
		this.sumLoseNumber = sumLoseNumber;
		this.sumMaintenanceNumber = sumMaintenanceNumber;
		this.sumStepNumber = sumStepNumber;
		this.sumAvailableNumber = sumAvailableNumber;
		this.titleName = titleName;
	}
	/**
	 * @return the maTypeName
	 */
	public String getMaTypeName() {
		return maTypeName;
	}
	/**
	 * @return the maType
	 */
	public String getMaType() {
		return maType;
	}
	/**
	 * @return the assetTypeId
	 */
	public String getAssetTypeId() {
		return assetTypeId;
	}
	
	/**
	 * @return the assetCategoryName
	 */
	public String getAssetCategoryName() {
		return assetCategoryName;
	}
	
	/**
	 * @return the sumPurchaseNumber
	 */
	public Long getSumPurchaseNumber() {
		return sumPurchaseNumber;
	}
	/**
	 * @param sumPurchaseNumber the sumPurchaseNumber to set
	 */
	public void setSumPurchaseNumber(Long sumPurchaseNumber) {
		this.sumPurchaseNumber = sumPurchaseNumber;
	}
	/**
	 * @return the currentDate
	 */
	public String getCurrentDate() {
		return currentDate;
	}
	/**
	 * @param currentDate the currentDate to set
	 */
	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}
	/**
	 * @param assetCategoryName the assetCategoryName to set
	 */
	public void setAssetCategoryName(String assetCategoryName) {
		this.assetCategoryName = assetCategoryName;
	}
	/**
	 * @return the assetCategory
	 */
	public String getAssetCategory() {
		return assetCategory;
	}
	/**
	 * @param assetCategory the assetCategory to set
	 */
	public void setAssetCategory(String assetCategory) {
		this.assetCategory = assetCategory;
	}
	/**
	 * @return the yyyymm
	 */
	public String getYyyymm() {
		return yyyymm;
	}
	/**
	 * @param yyyymm the yyyymm to set
	 */
	public void setYyyymm(String yyyymm) {
		this.yyyymm = yyyymm;
	}
	/**
	 * @return the assetTypeName
	 */
	public String getAssetTypeName() {
		return assetTypeName;
	}
	/**
	 * @return the supporedFunction
	 */
	public String getSupporedFunction() {
		return supporedFunction;
	}
	/**
	 * @return the supporedFunctionName
	 */
	public String getSupporedFunctionName() {
		return supporedFunctionName;
	}
	/**
	 * @return the companyId
	 */
	public String getCompanyId() {
		return companyId;
	}
	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}
	/**
	 * @return the purchaseYearMonth
	 */
	public Date getPurchaseYearMonth() {
		return purchaseYearMonth;
	}
	/**
	 * @return the contractId
	 */
	public String getContractId() {
		return contractId;
	}
	/**
	 * @return the purchaseNumber
	 */
	public Long getPurchaseNumber() {
		return purchaseNumber;
	}
	/**
	 * @return the storageNumber
	 */
	public Integer getStorageNumber() {
		return storageNumber;
	}
	/**
	 * @return the preparationNumber
	 */
	public Integer getPreparationNumber() {
		return preparationNumber;
	}
	/**
	 * @return the scrapedNumber
	 */
	public Integer getScrapedNumber() {
		return scrapedNumber;
	}
	/**
	 * @return the loseNumber
	 */
	public Integer getLoseNumber() {
		return loseNumber;
	}
	/**
	 * @return the maintenanceNumber
	 */
	public Integer getMaintenanceNumber() {
		return maintenanceNumber;
	}
	/**
	 * @return the stepNumber
	 */
	public Integer getStepNumber() {
		return stepNumber;
	}
	/**
	 * @return the availableNumber
	 */
	public Integer getAvailableNumber() {
		return availableNumber;
	}
	/**
	 * @param maTypeName the maTypeName to set
	 */
	public void setMaTypeName(String maTypeName) {
		this.maTypeName = maTypeName;
	}
	/**
	 * @param maType the maType to set
	 */
	public void setMaType(String maType) {
		this.maType = maType;
	}
	/**
	 * @param assetTypeId the assetTypeId to set
	 */
	public void setAssetTypeId(String assetTypeId) {
		this.assetTypeId = assetTypeId;
	}
	/**
	 * @param assetTypeName the assetTypeName to set
	 */
	public void setAssetTypeName(String assetTypeName) {
		this.assetTypeName = assetTypeName;
	}
	/**
	 * @param supporedFunction the supporedFunction to set
	 */
	public void setSupporedFunction(String supporedFunction) {
		this.supporedFunction = supporedFunction;
	}
	/**
	 * @param supporedFunctionName the supporedFunctionName to set
	 */
	public void setSupporedFunctionName(String supporedFunctionName) {
		this.supporedFunctionName = supporedFunctionName;
	}
	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	/**
	 * @param purchaseYearMonth the purchaseYearMonth to set
	 */
	public void setPurchaseYearMonth(Date purchaseYearMonth) {
		this.purchaseYearMonth = purchaseYearMonth;
	}
	/**
	 * @param contractId the contractId to set
	 */
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	/**
	 * @param purchaseNumber the purchaseNumber to set
	 */
	public void setPurchaseNumber(Long purchaseNumber) {
		this.purchaseNumber = purchaseNumber;
	}
	/**
	 * @param storageNumber the storageNumber to set
	 */
	public void setStorageNumber(Integer storageNumber) {
		this.storageNumber = storageNumber;
	}
	/**
	 * @param preparationNumber the preparationNumber to set
	 */
	public void setPreparationNumber(Integer preparationNumber) {
		this.preparationNumber = preparationNumber;
	}
	/**
	 * @param scrapedNumber the scrapedNumber to set
	 */
	public void setScrapedNumber(Integer scrapedNumber) {
		this.scrapedNumber = scrapedNumber;
	}
	/**
	 * @param loseNumber the loseNumber to set
	 */
	public void setLoseNumber(Integer loseNumber) {
		this.loseNumber = loseNumber;
	}
	/**
	 * @param maintenanceNumber the maintenanceNumber to set
	 */
	public void setMaintenanceNumber(Integer maintenanceNumber) {
		this.maintenanceNumber = maintenanceNumber;
	}
	/**
	 * @param stepNumber the stepNumber to set
	 */
	public void setStepNumber(Integer stepNumber) {
		this.stepNumber = stepNumber;
	}
	/**
	 * @param availableNumber the availableNumber to set
	 */
	public void setAvailableNumber(Integer availableNumber) {
		this.availableNumber = availableNumber;
	}
	/**
	 * @return the destoryedNumber
	 */
	public Integer getDestoryedNumber() {
		return destoryedNumber;
	}
	/**
	 * @param destoryedNumber the destoryedNumber to set
	 */
	public void setDestoryedNumber(Integer destoryedNumber) {
		this.destoryedNumber = destoryedNumber;
	}
	/**
	 * @return the sumStorageNumber
	 */
	public Integer getSumStorageNumber() {
		return sumStorageNumber;
	}
	/**
	 * @return the sumPreparationNumber
	 */
	public Integer getSumPreparationNumber() {
		return sumPreparationNumber;
	}
	/**
	 * @return the sumDestoryedNumber
	 */
	public Integer getSumDestoryedNumber() {
		return sumDestoryedNumber;
	}
	/**
	 * @return the sumScrapedNumber
	 */
	public Integer getSumScrapedNumber() {
		return sumScrapedNumber;
	}
	/**
	 * @return the sumLoseNumber
	 */
	public Integer getSumLoseNumber() {
		return sumLoseNumber;
	}
	/**
	 * @return the sumMaintenanceNumber
	 */
	public Integer getSumMaintenanceNumber() {
		return sumMaintenanceNumber;
	}
	/**
	 * @return the sumStepNumber
	 */
	public Integer getSumStepNumber() {
		return sumStepNumber;
	}
	/**
	 * @return the sumAvailableNumber
	 */
	public Integer getSumAvailableNumber() {
		return sumAvailableNumber;
	}
	/**
	 * @param sumStorageNumber the sumStorageNumber to set
	 */
	public void setSumStorageNumber(Integer sumStorageNumber) {
		this.sumStorageNumber = sumStorageNumber;
	}
	/**
	 * @param sumPreparationNumber the sumPreparationNumber to set
	 */
	public void setSumPreparationNumber(Integer sumPreparationNumber) {
		this.sumPreparationNumber = sumPreparationNumber;
	}
	/**
	 * @param sumDestoryedNumber the sumDestoryedNumber to set
	 */
	public void setSumDestoryedNumber(Integer sumDestoryedNumber) {
		this.sumDestoryedNumber = sumDestoryedNumber;
	}
	/**
	 * @param sumScrapedNumber the sumScrapedNumber to set
	 */
	public void setSumScrapedNumber(Integer sumScrapedNumber) {
		this.sumScrapedNumber = sumScrapedNumber;
	}
	/**
	 * @param sumLoseNumber the sumLoseNumber to set
	 */
	public void setSumLoseNumber(Integer sumLoseNumber) {
		this.sumLoseNumber = sumLoseNumber;
	}
	/**
	 * @param sumMaintenanceNumber the sumMaintenanceNumber to set
	 */
	public void setSumMaintenanceNumber(Integer sumMaintenanceNumber) {
		this.sumMaintenanceNumber = sumMaintenanceNumber;
	}
	/**
	 * @param sumStepNumber the sumStepNumber to set
	 */
	public void setSumStepNumber(Integer sumStepNumber) {
		this.sumStepNumber = sumStepNumber;
	}
	/**
	 * @param sumAvailableNumber the sumAvailableNumber to set
	 */
	public void setSumAvailableNumber(Integer sumAvailableNumber) {
		this.sumAvailableNumber = sumAvailableNumber;
	}
	/**
	 * @return the titleName
	 */
	public String getTitleName() {
		return titleName;
	}
	/**
	 * @param titleName the titleName to set
	 */
	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}
	/**
	 * @return the contract
	 */
	public String getContract() {
		return contract;
	}
	/**
	 * @param contract the contract to set
	 */
	public void setContract(String contract) {
		this.contract = contract;
	}
	/**
	 * @return the repairedNumber
	 */
	public Integer getRepairedNumber() {
		return repairedNumber;
	}
	/**
	 * @param repairedNumber the repairedNumber to set
	 */
	public void setRepairedNumber(Integer repairedNumber) {
		this.repairedNumber = repairedNumber;
	}
	/**
	 * @return the sumRepairedNumber
	 */
	public Integer getSumRepairedNumber() {
		return sumRepairedNumber;
	}
	/**
	 * @param sumRepairedNumber the sumRepairedNumber to set
	 */
	public void setSumRepairedNumber(Integer sumRepairedNumber) {
		this.sumRepairedNumber = sumRepairedNumber;
	}
	
}
