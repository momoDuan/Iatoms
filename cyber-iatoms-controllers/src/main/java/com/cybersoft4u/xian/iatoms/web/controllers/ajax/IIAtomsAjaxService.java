package com.cybersoft4u.xian.iatoms.web.controllers.ajax;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cafe.core.bean.Parameter;
import cafe.core.service.ServiceException;
import cafe.core.web.ajax.IAjaxServiceProxy;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetInInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimMerchantHeaderDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetBorrowInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetStacktakeListDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetTransInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryHistoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.MerchantDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentItemDTO;
/**
 * Purpose: Ajax处理Service interface
 * @author Allenchen
 * @since  JDK 1.7
 * @date   2016/4/8
 * @MaintenancePersonnel Allenchen
 */
@SuppressWarnings("rawtypes")
public interface IIAtomsAjaxService extends IAjaxServiceProxy {
	
	/**
	 * 
	 * Purpose:根據廠商獲取合約編號
	 * @author hungli
	 * @param vendor:廠商編號
	 * @return
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>：合約編號集合
	 */
	public List<Parameter> getContractListByVendorId(String vendor) throws ServiceException;
	
	/**
	 * Purpose:根據客戶編號獲取統一編號
	 * @author CarrieDuan
	 * @param companyId
	 * @throws ServiceException
	 * @return String
	 */
	public String getUnityNameByCompanyId(String companyId) throws ServiceException;
	
	/**
	 * Purpose:根據公司code獲取公司資料
	 * @author amandawang
	 * @param companyCode 公司code
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return CompanyDTO：公司dto
	 */
	public CompanyDTO getCompanyByCompanyCode(String companyCode) throws ServiceException;
	/**
	 * 
	 * Purpose:根據合約編號聯動設備,維護模式,資產Owner等
	 * @author hungli
	 * @param contractNo ：合約編號
	 * @return
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return AssetInInfoDTO ：設備入庫主檔
	 */
	public List<Parameter> getAssetInfoByContractNo(String contractNo) throws ServiceException;
	/**
	 * Purpose:由特店表頭ID獲得特店DTO
	 * @author amandawang
	 * @param merchantHeaderId:特店表頭ID
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return MerchantDTO特店DTO
	 */
	public MerchantDTO getMerchantHeader(String merchantHeaderId) throws ServiceException;
	
	/**
	 * 
	 * Purpose:根據入庫批號獲取入庫基本信息
	 * @author hungli
	 * @param assetInId:入庫批號
	 * @return
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return AssetInInfoDTO：設備入庫主檔
	 */
	public AssetInInfoDTO getAssetInInfoByAssetInId(String assetInId) throws ServiceException;
	/**
	 * 
	 * Purpose: 獲取入庫批號
	 * @author hungli
	 * @param isDone 是否入庫
	 * @return
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>：入庫批號集合
	 */
	public List<Parameter> getAssetInIdList(String isDone) throws ServiceException;
	/**
	 * Purpose:獲取此客戶特店的表頭集合
	 * @author HermanWang
	 * @param merchantId：特店ID
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>：返回一個list集合
	 */
	public Map<String, Object> getMerchantList(String merchantId) throws ServiceException;
	/**
	 * Purpose:獲取此設備的庫存列表
	 * @author HermanWang
	 * @param editAssetTypeId：設備品相id
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>：返回一個list集合
	 */
	public Map<String, Object> getAssetList(String editAssetTypeId) throws ServiceException;
	/**
	 * Purpose:根據客戶獲取合約編號
	 * @author amandawang
	 * @param customer：客戶
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>：合約編號集合
	 */
	public List<Parameter> getContractList(String customer) throws ServiceException;
	/**
	 * Purpose:根據客戶獲取已有SLA的合約編號
	 * @author amandawang
	 * @param customer：客戶
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>：合約編號集合
	 */
	public List<Parameter> getContractIdListByCustomer(String customer) throws ServiceException;
	
	/**
	 * Purpose:根據客戶獲取已有SLA的合約編號
	 * @author amandawang
	 * @param contractId：合約ID
	 * @param assetCategory：設備類別
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>：合約編號集合
	 */
	public List<Parameter> getAssetTypeIdByContractId(String contractId, String assetCategory) throws ServiceException;
	
	/**
	 * Purpose:根據用戶ID獲得Email
	 * @author amandawang
	 * @param userId：用戶ID
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return Parameter：Email
	 */
	public AdmUserDTO getEmailByUserId(String userId, String maintenanceCompany) throws ServiceException;
	/**
	 * Purpose:由合約ID獲得設備名稱
	 * @author amandawang
	 * @param contractId:合約ID
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>
	 */
	public List<Parameter> getAssetTypeIdList(String contractId) throws ServiceException;
	/**
	 * Purpose:根據父功能編號查詢功能列表
	 * @author HaimingWang
	 * @param parentFunctionId:父功能編號
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>:功能列表
	 */
	public List<Parameter> getFunctionByParentId(String parentFunctionId) throws ServiceException;
	/**
	 * 
	 * Purpose:抓取盤點批號集合　
	 * @author allenchen
	 * @throws ServiceException：出錯時拋出ServiceException
	 * @return List<Parameter>:查詢盤點批號集合
	 */
	public List<Parameter> getInventoryNumberList() throws ServiceException;

	/**
	 * 
	 * Purpose:根據轉倉批號獲取轉倉基本信息
	 * @author amandawang
	 * @param assetTransId:轉倉批號
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return AssetInInfoDTO：設備入庫主檔
	 */
	public DmmAssetTransInfoDTO getAssetTransInfoByAssetTransId(String assetTransId) throws ServiceException;

	/**
	 * Purpose: 獲取轉倉批號
	 * @author amandawang
	 * @param userId 當前登錄者ID
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>：入庫批號集合
	 */
	public List<Parameter> getAssetTransIdList(String tabType, String userId) throws ServiceException;
	
	/**
	 * 
	 * Purpose:根據盤點批號查詢盤點詳細信息
	 * @author allenchen
	 * @param stocktackId:設備盤點批號
	 * @return
	 * @throws ServiceException：出錯時拋出ServiceException
	 * @return AssetStocktackInfoDTO:設備盤點主檔DTO
	 */
	public DmmAssetStacktakeListDTO getAssetStocktackInfoByStocktackId(String stocktackId) throws ServiceException;
	/**
	 * 
	 * Purpose:獲取轉倉批號列表
	 * @author starwang
	 * @param userId
	 * @param queryFromDateStart
	 * @param queryFromDateEnd
	 * @param queryToDateStart
	 * @param queryToDateEnd
	 * @param queryFromWarehouseId
	 * @param queryToWarehouseId
	 * @throws ServiceException
	 * @return List<Parameter>
	 */
	public List<Parameter> getAssetInfoList(String userId, String queryFromDateStart,String queryFromDateEnd,String queryToDateStart,String queryToDateEnd,String queryFromWarehouseId,String queryToWarehouseId)
			throws ServiceException;
	/**
	 * 
	 * Purpose:獲取轉倉批號資料
	 * @author starwang
	 * @param assetTransId
	 * @return
	 * @throws ServiceException
	 * @return String
	 */
	public String getAssetTransInfoById(String assetTransId) throws ServiceException;
	/**
	 * Purpose:獲得角色代碼列表
	 * @author evanliu
	 * @throws ServiceException:出錯後, 丟出ServiceException
	 * @return List<Parameter>:角色代碼列表, value-roleId, name-roleCode + roleName
	 */
	public List<Parameter> getRoleCode() throws ServiceException;
	
	/**
	 * Purpose:根據庫存歷史檔Id獲取庫存歷史信息
	 * @author amandawang
	 * @param histId:庫存歷史檔Id
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return DmmRepositoryHistoryDTO：設備庫存歷史檔
	 */
	public DmmRepositoryHistoryDTO getRepositoryHistDTOByHistId(String histId) throws ServiceException;
	
	/**
	 * Purpose:根据所选公司联动部门
	 * @author CrissZhang
	 * @param companyId ：公司编号
	 * @param ignoreDeleted ：忽略刪除
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>：部门列表
	 */
	public List<Parameter> getDeptList(String companyId, Boolean ignoreDeleted) throws ServiceException;
	
	/**
	 * Purpose:根据所选部门联动用戶
	 * @author amandawang
	 * @param departmentId ：部門编号
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>：用戶列表
	 */
	public List<Parameter> getUserByDeparment(String departmentId) throws ServiceException;
	/**
	 * Purpose: 根據功能屬性選擇對應的表單角色列表
	 * @author CarrieDuan
	 * @param attributeValue：角色屬性
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter> 返回表單角色類表
	 */
	public List<Parameter> getWorkFlowRoleList(String attributeValue) throws ServiceException;
	
	/**
	 * Purpose: 檢測該角色是否已經被員工使用
	 * @author CarrieDuan
	 * @param roleId 角色ID
	 * @throws SecurityException:出錯時拋出ServiceException
	 * @return Boolean 返回結果的boolean
	 */
	public Boolean checkUseRole(String roleId) throws SecurityException;
	
	/**
	 * Purpose:根據客戶編號獲取已選報表以外的報表名稱
	 * @author ElvaHe
	 * @param customerId 客戶編號
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<BaseParameterItemDefDTO>:查詢列表
	 */
	public List<Parameter> getReportNameList(String customerId) throws ServiceException;
	
	/**
	 * Purpose:依所選的報表名稱顯示相應的報表明細
	 * @author ElvaHe
	 * @param reportCode：報表名稱編號
	 * @param bptdCode：報表名稱的bptd_code
	 * @param detailCode：明細的bptd_code
	 * @throws ServiceException：出錯時拋出ServiceException
	 * @return List<Parameter>：顯示的明細List
	 */
	public List<Parameter> getReportDetailList(String reportCode, String bptdCode, String detailCode) throws ServiceException;
	/**
	 * Purpose: 檢測文件是否存在
	 * @author KevinShen
	 * @param fileName：文件全路徑
	 * @throws ServiceException： 出錯時拋出ServiceException
	 * @return boolean：是否存在
	 */
	public boolean checkExsitDownLoadFile(String fileName) throws ServiceException;
	
	/**
	 * Purpose:根據選擇的客戶獲取其所有合约编号
	 * @author CrissZhang
	 * @param customerId ：選擇的客戶編號
	 * @param status ：合約狀態
	 * @param companyType ：公司類型
	 * @throws ServiceException ：出錯時拋出ServiceException
	 * @return List<Parameter> ：返回類型為下拉列表
	 */
	public List<Parameter> getContractByCustomerAndStatus(String customerId, String status, String companyType) throws ServiceException;
	
	/**
	 * Purpose:根據客戶編號、合約狀態、以及是否有sla信息選擇客戶
	 * @author CrissZhang
	 * @param customerId ：客戶編號
	 * @param status ：合約狀態
	 * @param isHaveSla ：是否有sla信息
	 * @param ignoreDeleted ：忽略刪除
	 * @throws ServiceException：出錯時拋出ServiceException
	 * @return List<Parameter> ：返回類型為下拉列表
	 */
	public List<Parameter> getContractCodeList(String customerId, String status, Boolean isHaveSla, Boolean ignoreDeleted) throws ServiceException;
	
	/**
	 * Purpose:根據設備類型獲取對應的設備類表
	 * @author CarrieDuan
	 * @param assetCategory ： 設備類型ID
	 * @throws SecurityException ：出錯時拋出ServiceException
	 * @return List<Parameter>：返回類型為下拉列表
	 */
	public List<Parameter> getAssetTypeList(String assetCategoryId) throws SecurityException;
	
	/**
	 * Purpose:根據合約ID獲取保固日期
	 * @author CarrieDuan
	 * @param contractId ：合約ID
	 * @throws SecurityException ：出錯時拋出ServiceException ：
	 * @return String ：返回
	 */
	public Integer getContractWarranty(String contractId, String factoryWarranty, String customerWarranty) throws SecurityException;
	/**
	 * Purpose:根據轉倉批號得到轉倉信息
	 * @author ElvaHe
	 * @param assetTransId：轉倉批號
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return AssetTransInfoDTO：設備轉倉信息DTO
	 */
	//public DmmAssetTransInfoDTO getAssetTransInfoByKeyId(String assetTransId) throws ServiceException;
	
	/**
	 * Purpose:得到"轉入驗收"頁面的轉倉批號列表
	 * @author ElvaHe
	 * @userId：當前登錄者ID
	 * @param tabType:頁簽
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>：轉倉批號信息列表
	 */
	public List<Parameter> getAssetTransIds(String tabType, String userId) throws ServiceException;
	/**
	 * Purpose:由倉庫和設備獲取盤點批號
	 * @author amandawang
	 * @param warehouseId:倉庫Id
	 * @param assetTypeIdList:設備Id列表
	 * @throws ServiceException
	 * @return List<Parameter>盤點批號集合
	 */
	public List<Parameter> getStocktackIdByWarehouseAndAssetType(String warehouseId, String assetTypeIdList) throws ServiceException;
	/**
	 * Purpose:根據客戶得到相應的設備類別
	 * @author echomou
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>：設備類別列表
	 */
	public List<Parameter> getAssetListTypeByCustomer(String customerId) throws ServiceException;
	
	/**
	 * Purpose: 得到確認轉倉時的通知人員下拉框
	 * @author barryzhang
	 * @param assetTransId：轉倉批號
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>：通知人員列表
	 */
	public List<Parameter> getWareHouseUserNameList(String assetTransId) throws ServiceException;
	
	/**
	 * Purpose: 下載以上傳的文件時，根據文件ID獲取路徑以及名稱，判斷文件是否存在
	 * @author CarrieDuan
	 * @param fileId 文件ID
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return boolean ：文件是否存在
	 */
	public boolean checkDownLoadFile(String fileId) throws ServiceException;
	/**
	 * Purpose: 下載以上傳的文件時，根據文件ID獲取路徑以及名稱，判斷文件是否存在
	 * @author HermanWang
	 * @param fileId 文件ID
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return boolean ：文件是否存在
	 */
	public boolean checkCaseTemplatesDownLoadFile(String fileId) throws ServiceException;
	
	/**
	 * Purpose: 根據設備id獲取設備型號以及設備廠牌
	 * @author CarrieDuan
	 * @param assetId ：設備名稱
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return Map<String, List<Parameter>> ：設備類型列表
	 */
	public Map<String, List<Parameter>> getAssetModelAndBrand(String assetId) throws ServiceException;
	
	/**
	 * Purpose: 根據ID獲取表頭信息--案件處理
	 * @author CarrieDuan
	 * @param merchantHeaderId 特點表頭ID
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return BimMerchantHeaderDTO
	 */
	public BimMerchantHeaderDTO getMerchantHeaderDTOById (String merchantHeaderId) throws ServiceException;
	
	/**
	 * Purpose: 根據ID獲取特店信息--案件處理
	 * @author CarrieDuan
	 * @param merchantId ：特店ID
	 * @param customerId : 客戶ID
	 * @param merchantCode：特點代號
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return MerchantDTO：特點DTO
	 */
	public MerchantDTO getMerchantDTOById(String merchantId, String merchantCode,String customerId) throws ServiceException;
	
	/**
	 * Purpose: 根據特店代號與客戶ID獲取特店信息
	 * @author NickLin
	 * @param merchantCode：特店代號
	 * @param customerId : 客戶ID
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>：特店信息
	 */
	public List<Parameter> getMerchantsByCodeAndCompamyId(String merchantCode,String customerId) throws ServiceException;
		
	/**
	 * Purpose:獲取表頭下拉框--案件處理
	 * @author CarrieDuan
	 * @param merchantId ：特店ID
	 * @param customerId : 客戶ID
	 * @param merchantCode：特點代號
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>：表頭集合
	 */
	public List<Parameter> getMerchantHeaderList(String customerId, String merchantCode, String merchantId) throws ServiceException;
	
	/** 
	 * Purpose: 根據id獲取對應的信息--案件處理
	 * @author CarrieDuan
	 * @param dtid ：案件處理歷史資料檔dtid
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return CaseHandleInfoDTO:案件處理歷史資料檔
	 */
	public SrmCaseHandleInfoDTO getCaseMessageByDTID(String dtid, String customerId,String isNewFlag, String isCheck) throws ServiceException;
	
	/**
	 * Purpose: 依刷卡機型獲取對應的內建功能
	 * @author CarrieDuan
	 * @param edcType：刷卡機型
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>：內建功能
	 */
	public List<Parameter> getBuiltInFeature(String edcType) throws ServiceException;
	
	/**
	 * Purpose:根據合約ID獲取對應的周邊設備
	 * @author CarrieDuan
	 * @param contractId：合約ID
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>：周邊設別
	 */
	/*public Map getComboboxValueByContractId(String contractId, String type) throws ServiceException;*/
	
	/**
	 * Purpose:根據客戶和刷卡機行獲取軟體版本
	 * @author CarrieDuan
	 * @param customerId:客戶ID
	 * @param edcType：刷卡機行
	 * @param searchDeletedFlag：是否查詢已刪除標誌位
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>：軟體版本列表
	 */
	public List<Parameter> getSoftwareVersions(String customerId, String edcType, String searchDeletedFlag) throws ServiceException;
	
	/**
	 * Purpose:保存系統日誌
	 * @author CrissZhang
	 * @param actionId ： actionId
	 * @param logContent ： 日誌數據詳細
	 * @param ucNo ：功能編號
	 * @param request ： 請求
	 * @throws ServiceException : 出錯時拋出ServiceException
	 * @return void ：無返回值
	 */
	public void saveSystemLog(String actionId, String logContent, String ucNo, HttpServletRequest request) throws ServiceException;
	
	/**
	 * Purpose:獲取通訊模式下拉列表
	 * @author CarrieDuan
	 * @param assetTypeId：設備ID
	 * @throws ServiceException : 出錯時拋出ServiceException
	 * @return List<Parameter>：通訊模式下拉列表
	 */
	public List<Parameter> getConnectionType(String assetTypeId) throws ServiceException;
	
	/**
	 * Purpose:核檢案件處理中，附加資料當資料是否存在
	 * @author CarrieDuan
	 * @param attFileId：資料ID
	 * @throws ServiceException : 出錯時拋出ServiceException
	 * @return Boolean：是否存在
	 */
	public Boolean checkCaseFile(String attFileId, String isHistory) throws ServiceException;
	
	/**
	 * Purpose:根據客戶判斷該客戶的DTID生成方式是否為同TID
	 * @author CarrieDuan
	 * @param companyId ：公司ID
	 * @throws ServiceException: 出錯時拋出ServiceException
	 * @return Boolean：是否同TID
	 */
	public Boolean checkDtidType(String companyId) throws ServiceException;
	
	/**
	 * Purpose:判斷DTID是否已被使用
	 * @author CarrieDuan
	 * @param id ：DTID號碼管理ID
	 * @throws ServiceException: 出錯時拋出ServiceException
	 * @return Boolean：是否已被使用
	 */
	public Boolean isUseDtid(String id) throws ServiceException;
	
	/**
	 * Purpose:驗證dtid可用號碼數
	 * @author CrissZhang
	 * @param caseNumber ： 複製筆數
	 * @param customerId ： 客戶編號
	 * @param edcType ： 刷卡機型
	 * @throws ServiceException : 出錯時拋出ServiceException
	 * @return  Map<String, Object> ： 返回一個map類型
	 */
	public  Map<String, Object>  checkDtidNumber(String caseNumber, String customerId, String edcType, Boolean isSingle) throws ServiceException;
	
	/**
	 * Purpose: 根據所選擇的DTID獲取對應的求償資料信息
	 * @author CarrieDuan
	 * @param customerId :客戶ID
	 * @param dtid ：dtid
	 * @param historyId:歷史ID
	 * @throws ServiceException: 出錯時拋出ServiceException
	 * @return SrmHistoryCaseHandleInfoDTO
	 */
	public SrmCaseHandleInfoDTO getPayInfo(String dtid, String customerId, String caseId) throws ServiceException;
	
	/**
	 * Purpose:根據客戶獲取耗材品信息
	 * @author CarrieDuan
	 * @param customerId：客戶ID
	 * @throws ServiceException: 出錯時拋出ServiceException
	 * @return String：耗材品下拉列表
	 */
	public List<Parameter> getSuppliesTypeNameList(String customerId, String suppliesType) throws ServiceException;
	/**
	 * Purpose: 送出等操作時根據求償編號獲取求償列表信息
	 * @author CarrieDuan
	 * @param paymentId ：求償編號
	 * @throws ServiceException: 出錯時拋出ServiceException
	 * @return List<SrmPaymentInfoDTO>：求償列表DTO
	 */
	public List<SrmPaymentItemDTO> getPaymentItemByItemIds (String itemId, String updateDate) throws ServiceException;
	/**
	 * Purpose:根據部門獲取工程師，根據TMS,QA,CUSTOMER_SERVICE獲取人員
	 * @author amandawang
	 * @param deptCode ：部門id
	 * @param roleCode : 角色id
	 * @throws ServiceException : 出錯時拋出ServiceException
	 * @return List<Parameter> : 人員下拉列表
	 */
	public List<Parameter> getUserByDepartmentAndRole(String deptCode, String roleCode, boolean flag, boolean isDeptAgent) throws ServiceException;
	/**
	 * Purpose:獲取ao人員以及 建案人員的mail
	 * @author HermanWang
	 * @param caseId :案件編號
	 * @throws ServiceException: 出錯時拋出ServiceException
	 * @return List<Parameter>：人員mail下拉列表
	 */
	public Map<String, Object> getAoEmailByCaseId(String caseId) throws ServiceException;
	/**
	 * Purpose:獲取案件相關的mail
	 * @author HermanWang
	 * @param caseId :案件編號
	 * @throws ServiceException: 出錯時拋出ServiceException
	 * @return List<Parameter>：人員mail下拉列表
	 */
	public Map<String, Object> getcaseInfoEmailByCaseId(String caseId, String actionId) throws ServiceException;
	/**
	 * Purpose:根據公司編號獲取維護部門列表
	 * @author CrissZhang
	 * @param companyId : 公司編號
	 * @throws ServiceException : 出錯時拋出ServiceException
	 * @return List<Parameter> : 維護部門列表
	 */
	public List<Parameter> getDeptByCompanyId(String companyId) throws ServiceException;
	/**
	 * Purpose:獲取該案件對應的重複進建的caseID
	 * @author HermanWang
	 * @param dtid：dtid
	 * @param caseId：案件編號
	 * @param isSignFlag：是否簽收
	 * @throws ServiceException: 出錯時拋出ServiceException
	 * @return Map<String,Object>:返回一個map對象
	 */
	public Map<String, Object> getCaseRepeatList(String dtid, String caseId, boolean isSignFlag) throws ServiceException;
	/**
	 * Purpose:驗證該案件數據是否被通dtid的案件修改。
	 * @author HermanWang
	 * @param dtid：dtid
	 * @param caseId：案件編號
	 * @throws ServiceException: 出錯時拋出ServiceException
	 * @return Map<String,Object>:返回一個map對象
	 */
	public Map<String, Object> getCaseLinkIsChange(String dtid, String caseId) throws ServiceException;
	/**
	 * Purpose:驗證該筆dtid下比本案件建安早的裝機案件是否存在
	 * @author amandawang
	 * @param dtid：dtid
	 * @param caseId：案件編號
	 * @throws ServiceException: 出錯時拋出ServiceException
	 * @return Map<String,Object>:返回一個map對象
	 */
	public String getCountByInstall(String dtid, String caseId) throws ServiceException;
	/**
	 * Purpose:檢核該筆案件的上一筆是否為裝機，如果為裝機案件 則上一筆案件 須要完修
	 * @author amandawang
	 * @param dtid：dtid
	 * @param caseId:案件編號
	 * @throws ServiceException: 出錯時拋出ServiceException
	 * @return Map<String,Object>:返回一個map對象
	 */
	public Map<String, Object> getCaseRepeatByInstallUncomplete(String dtid, String caseId) throws ServiceException;
	/**
	 * Purpose:根據客戶ID獲取該客戶的耗材分類
	 * @author HermanWang
	 * @param customerId 客戶ID
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>：返回一個list集合
	 */
	public Map<String, Object>  getSuppliesListByCustomseId(String customerId) throws ServiceException;
	/**
	 * Purpose:根據客戶ID和耗材分類獲取耗材名稱
	 * @author HermanWang
	 * @param customerId：客戶id
	 * @param suppliesType：耗材名稱
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return Map<String,Object>：返回一個list集合
	 */
	public Map<String, Object>  getSuppliesNameList(String customerId, String suppliesType) throws ServiceException;
	/**
	 * Purpose:根據範本類別和範本名稱獲取上傳的範本ID
	 * @author HermanWang
	 * @param uploadCategory：範本類別
	 * @param fileName:範本名稱
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return String返回一個String 範本ID
	 */
	public String getUploadTemplatesId(String uploadCategory, String fileName) throws ServiceException;
	/**
	 * Purpose:根據案件編號得到案件信息
	 * @author CrissZhang
	 * @param caseId ： 案件編號
	 * @param dtoFlag：是否查詢案件dto放入map中
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SrmCaseHandleInfo:返回一個案件處理DMO
	 */
	public Map getCaseInfoById(String caseId, String dtoFlag) throws ServiceException;
	/**
	 * Purpose:根據案件編號得到修改後的案件信息
	 * @author amandawang
	 * @param caseId ： 案件編號
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return SrmCaseHandleInfo:返回一個案件處理DMO
	 */
	public Map getChangeCaseInfoById(String caseId) throws ServiceException ;
	/**
	 * Purpose:案件處理建案中，依據所選客戶獲取該客戶下的EDC設備或周邊設備（並且該設備的領用人為當前客戶）
	 * @author CarrieDuan
	 * @param customerId:客戶ID
	 * @param ignoreDeleted ：忽略刪除
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>：設備列表
	 */
	public List<Parameter> getAssetListForCase(String customerId, String assetCategory, Boolean ignoreDeleted) throws ServiceException;
	/**
	 * Purpose:建案時，依據合約編號獲取對應的維護廠商
	 * @author CarrieDuan
	 * @param contractId：合約編號
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return List<Parameter>：維護廠商列表
	 */
	public List<Parameter> getVendersByContractId (String contractId) throws ServiceException;
	/**
	 * Purpose:求償作業-修改資料驗證是否已被修改。
	 * @author CarrieDuan
	 * @param paymentId：求償資料ID
	 * @param updatedDate：頁面資料異動時間
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return Boolean：是否進行了其他操作
	 */
	public Boolean initEditCheckUpdate(String paymentId, Timestamp updatedDate)throws ServiceException;
	/**
	 * Purpose:保存角色明細時，核檢角色代碼，角色名稱是否重複。
	 * @author CarrieDuan
	 * @param roleCode：角色CODE
	 * @param roleName: 角色名稱
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return String：重複欄位
	 */
	public String checkRoleRepeat(String roleCode, String roleName, String roleId) throws ServiceException;
	/**
	 * Purpose:核檢使用人是否等於台新
	 * @author CarrieDuan
	 * @param serialNumbers：設備序號
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return String：存在不等於台新的，返回首個不等於台新的公司名稱，否則返回空
	 */
	public String checkAssetUserIsTaixinRent(String serialNumbers) throws ServiceException;
	/**
	 * Purpose:獲取通知mail人員
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return String:通知mail人員json字符串
	 */
	public List<Parameter> getMailGroupList() throws ServiceException;
	/**
	 * Purpose:獲取通知mail人員name
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return String:通知mail人員json字符串
	 */
	public String getNameList() throws ServiceException;
	/**
	 * Purpose:驗證是否異動設備
	 * @author CrissZhang
	 * @param caseId : 案件編號
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return boolean：返回boolean
	 */
	public boolean isChangeAsset(String caseId) throws ServiceException;
	
	/**
	 * Purpose:獲得匯出的返回標記
	 * @author CrissZhang
	 * @throws ServiceException:出錯時拋出ServiceException
	 * @return boolean：返回boolean
	 */
	public boolean getExportFlag(String ucNo) throws ServiceException;
	/**
	 * Purpose:根據當前登入着的公司id獲取該公司下的所有工程師
	 * @author Hermanwang
	 * @param companyId
	 * @return
	 * @throws ServiceException
	 * @return List<Parameter>
	 */
	public List<Parameter> getUserListByCompany(String companyId) throws ServiceException;
	
	/**
	 * Purpose:獲取客戶下第一個有EDC設備的合約編號
	 * @author CrissZhang
	 * @param companyId ： 客戶編號
	 * @throws ServiceException
	 * @return String : 返回第一個有EDC的合約編號
	 */
	public String getHaveEdcContract(String companyId) throws ServiceException;
	
	/**
	 * Purpose:得到用戶欄位模板
	 * @author CrissZhang
	 * @param templateId ： 用戶看為模板id
	 * @throws ServiceException
	 * @return String : 返回用戶欄位模板
	 */
	public Map getUserColumnTemplate(String templateId) throws ServiceException;
	/**
	 * Purpose:檢核該轉倉批號下是否有該設備序號
	 * @author amamdawang
	 * @param assetTransId ： 客戶編號
	 * @param serialNumber：
	 * @throws ServiceException
	 * @return String : 返回設備序號
	 */
	public String checkSerialNumber(String assetTransId, String serialNumber) throws ServiceException; 
	/**
	 * Purpose:根據設備id查詢設備型號
	 * @author amandawang
	 * @param assetTypeId
	 * @throws ServiceException
	 * @return List<Parameter>：設備型號集合
	 */
	public List<Parameter> getAssetModelList(String assetTypeId) throws ServiceException;	
	/**
	 * Purpose:核檢客訴管理處理中，附加資料檔是否存在
	 * @author nicklin
	 * @param  fileId：資料ID
	 * @throws ServiceException : 出錯時拋出ServiceException
	 * @return Boolean：是否存在
	 */
	public Boolean checkComplaintFile(String fileId) throws ServiceException;
	/**
	 * Purpose:取得批次號碼
	 * @author tonychen
	 * @throws ServiceException
	 * @return String : 批次號碼
	 */
	public String createBatchNum() throws ServiceException;
	
	/**
	 * Purpose:案件處理頁面多筆勾選，並以案件標號多筆POST至TMS API
	 * @author TonyChen
	 * @param  row：勾選數量
	 * @param  caseId:案件編號
	 * @throws ServiceException : 出錯時拋出ServiceException
	 * @return 回傳結果
	 */
	public Map TMSParaContents(String caseId, String ucNo, HttpServletRequest request) throws ServiceException;
	
	/**
	 * Purpose:獲取郵遞區號下拉框
	 * @author CarrieDuan
	 * @param  location：特店區域
	 * @throws ServiceException : 出錯時拋出ServiceException
	 * @return 回傳結果
	 */
	public List<Parameter> getPostCodeList(String location) throws ServiceException;
	/**
	 * Purpose:獲取裝機案件編號
	 * @author amandawang
	 * @param  location：特店區域
	 * @throws ServiceException : 出錯時拋出ServiceException
	 * @return 回傳結果
	 */
	public String getInstallCaseId(String dtid)throws ServiceException;
	/**
	 * Purpose:核檢user 輸入的檔案名稱是否存在
	 * @author nicklin
	 * @param  fileType：log 檔案類型
	 * @param  fileName:log 檔案名稱
	 * @throws ServiceException : 出錯時拋出ServiceException
	 * @return Boolean：是否存在
	 */
	public Boolean checkLogFileExist(String fileType, String fileName) throws ServiceException;

	/**
	 * Purpose:根據id獲取借用詳情
	 * @author CarrieDuan
	 * @param borrowId:設備借用id
	 * @throws ServiceException: 出錯時拋出ServiceException
	 * @return List<DmmAssetBorrowInfoDTO>
	 */
	public List<DmmAssetBorrowInfoDTO> getBorrowAssetItemByIds(String borrowId) throws ServiceException;
	
	/**
	 * Purpose:設備借用－核檢輸入的設備序號是否正確
	 * @author CarrieDuan
	 * @param assetTypeId：設備id
	 * @param serialNumber：設備序號
	 * @throws ServiceException: 出錯時拋出ServiceException
	 * @return String：返回錯誤信息
	 */
	public boolean checkBorrowSerialNumber(String assetTypeId, String serialNumber) throws ServiceException;
	
	public String checkAssetIsBorrow(String serialNumber) throws ServiceException;
}
