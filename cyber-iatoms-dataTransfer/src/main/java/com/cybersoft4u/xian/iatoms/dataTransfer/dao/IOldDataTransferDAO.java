package com.cybersoft4u.xian.iatoms.dataTransfer.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.ApplicationDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetTypeDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BaseParameterItemDefDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimCalendarDayDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimCalendarYearDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimMerchantHeaderDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryHistoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.MerchantDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAssetLinkDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAttFileDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTransactionDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.WarehouseDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmUser;

/**
 * Purpose: 舊資料轉檔數據訪問層接口
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2017-7-13
 * @MaintenancePersonnel CrissZhang
 */
public interface IOldDataTransferDAO extends IGenericBaseDAO{

	
	/**
	 * Purpose: 查詢Foms行事曆年檔數據
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<BimCalendarYearDTO>：行事曆年檔列表
	 */
	public List<BimCalendarYearDTO> listCalendarYear() throws DataAccessException;
	
	/**
	 * Purpose: 查詢Foms行事曆日檔數據
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<BimCalendarDayDTO>：行事曆日檔列表
	 */
	public List<BimCalendarDayDTO> listCalendarDate() throws DataAccessException;
	
	/**
	 * Purpose: 查詢故障組件數據
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<BaseParameterItemDefDTO>：基本參數列表
	 */
	public List<BaseParameterItemDefDTO> listFaultComponentData() throws DataAccessException;
	
	/**
	 * Purpose: 查詢故障現象數據
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<BaseParameterItemDefDTO>：基本參數列表
	 */
	public List<BaseParameterItemDefDTO> listFaultDescriptionData() throws DataAccessException;
	
	/**
	 * Purpose: 查詢公司基本信息
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<CompanyDTO>：公司信息列表
	 */
	public List<CompanyDTO> listCompanyData() throws DataAccessException;
	
	/**
	 * Purpose: 查詢倉庫據點信息
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<WarehouseDTO>：倉庫據點信息列表
	 */
	public List<WarehouseDTO> listWarehouseData() throws DataAccessException;
	
	/**
	 * Purpose: 查詢Foms客戶特店數據
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<MerchantDTO>：特店信息列表
	 */
	public List<MerchantDTO> listMerchantData() throws DataAccessException;
	
	/**
	 * Purpose: 查找Foms所有客戶特店表頭信息
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<BimMerchantHeaderDTO>：特店信息列表
	 */
	public List<BimMerchantHeaderDTO> listMerchantHanderData() throws DataAccessException;
	
	/**
	 * Purpose: 查詢程序版本數據
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<ApplicationDTO>：程式版本信息列表
	 */
	public List<ApplicationDTO> listApplicationData() throws DataAccessException;
	
	/**
	 * Purpose: 查詢合約數據
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<BimContractDTO>：合約信息列表
	 */
	public List<BimContractDTO> listContractData() throws DataAccessException;
	
	/**
	 * Purpose: 查詢設備品項數據
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<AssetTypeDTO>：設備品項列表
	 */
	public List<AssetTypeDTO> listAssetTypeData() throws DataAccessException;
	
	/**
	 * Purpose: 根據設備編號查廠牌信息
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<String>：廠牌列表
	 */
	public List<String> listBrandById(String assetId) throws DataAccessException;
	
	/**
	 * Purpose: 根據設備編號查廠牌信息
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<String>：型號列表
	 */
	public List<String> listModelById(String assetId) throws DataAccessException;
	/**
	 * Purpose: 查詢庫存數據
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<DmmRepositoryDTO>：庫存列表
	 */
	public List<DmmRepositoryDTO> listRepository() throws DataAccessException;
	/**
	 * Purpose: 查詢庫存歷史數據
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<DmmRepositoryHistoryDTO>：庫存列表
	 */
	public List<DmmRepositoryHistoryDTO> listHistoryRepository() throws DataAccessException;
	/**
	 * Purpose: 查詢案件資料
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<DmmRepositoryHistoryDTO>：案件信息列表
	 */
	public List<SrmCaseHandleInfoDTO> listCaseHandleInfo(String transferCaseId) throws DataAccessException;

	/**
	 * Purpose:查詢案件附加檔案資料
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<SrmCaseAttFileDTO>:案件附加資料列表
	 */
	public List<SrmCaseAttFileDTO> listCaseFiles(String transferCaseId) throws DataAccessException;
	
	/**
	 * Purpose:查詢案件歷程資料
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<SrmCaseTransactionDTO>:案件附加資料列表
	 */
	public List<SrmCaseTransactionDTO> listCaseTransactions(String transferCaseId) throws DataAccessException;
	
	/**
	 * Purpose:查詢使用者帳號管理資料
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<AdmUser>:使用者帳號管理資料列表
	 */
	public List<AdmUser> listAdmUser() throws DataAccessException;
	
	/**
	 * Purpose:查詢最新案件資料
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<SrmCaseTransactionDTO>:案件附加資料列表
	 */
	public List<SrmCaseHandleInfoDTO> listCaseNewHandleInfo(boolean isNoAsset) throws DataAccessException;
	
	/**
	 * Purpose:查找案件未結案案件--裝機異動
	 * @author CarrieDuan
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<SrmCaseHandleInfoDTO>:案件附加資料列表
	 */
	public List<SrmCaseHandleInfoDTO> listNotCloseCaseInstallOrUpdate(String caseCategory) throws DataAccessException;

	/**
	 * Purpose:獲得案件的設備信息
	 * @author CrissZhang
	 * @throws DataAccessException
	 * @return List<SrmCaseHandleInfoDTO>:dtid信息與設備信息集合
	 */
	public List<SrmCaseHandleInfoDTO> listAssetForCase() throws DataAccessException;
	/**
	 * Purpose:查找案件未結案案件--除裝機異動
	 * @author CarrieDuan
	 * @param caseCategory：案件類別
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<SrmCaseHandleInfoDTO>:案件附加資料列表
	 */
	public List<SrmCaseHandleInfoDTO> listNotCloseCaseOthers(String caseCategory) throws DataAccessException;
	/**
	 * Purpose:獲得最新案件的設備信息
	 * @author CrissZhang
	 * @throws DataAccessException
	 * @return List<SrmCaseAssetLinkDTO>:設備信息集合
	 */
	public List<SrmCaseAssetLinkDTO> listCaseNewAssetLink() throws DataAccessException;
	
	/**
	 * Purpose: 查詢報修原因數據
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<BaseParameterItemDefDTO>：基本參數列表
	 */
	public List<BaseParameterItemDefDTO> listProblemReasonData() throws DataAccessException;
	
	/**
	 * Purpose: 查詢報修解決方式數據
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<BaseParameterItemDefDTO>：基本參數列表
	 */
	public List<BaseParameterItemDefDTO> listProblemSolutionData() throws DataAccessException;
	
	/**
	 * Purpose:測試FOMS DB連接速度
	 * @author CrissZhang
	 * @throws DataAccessException
	 * @return List<BimCalendarDayDTO>
	 */
	public List<BimCalendarDayDTO> testSpeed()throws DataAccessException;
	
	/**
	 * Purpose:查詢可以轉入的設備鏈接檔筆數
	 * @author CrissZhang
	 * @throws DataAccessException
	 * @return int
	 */
	public int queryEnableCaseNewLik()throws DataAccessException;
}