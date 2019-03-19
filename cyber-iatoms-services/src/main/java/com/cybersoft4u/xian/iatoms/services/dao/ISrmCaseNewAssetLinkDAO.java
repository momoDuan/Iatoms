package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAssetLinkDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseNewAssetLink;

/**
 * Purpose: SRM_案件最新設備連接檔數據訪問接口
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2017-4-7
 * @MaintenancePersonnel CrissZhang
 */
public interface ISrmCaseNewAssetLinkDAO extends IGenericBaseDAO<SrmCaseNewAssetLink>{

	/**
	 * Purpose:案件最新設備連接檔
	 * @author CrissZhang
	 * @param caseId : 案件編號
	 * @throws DataAccessException ： 出錯時拋出DataAccessException
	 * @return List<SrmCaseAssetLinkDTO> ： 得到案件最新設備連接檔資料集合
	 */
	public List<SrmCaseAssetLinkDTO> listBy(String caseId, boolean isNewHave) throws DataAccessException;
	/**
	 * Purpose:根據caseid查詢案件最新設備連接檔關聯的設備
	 * @author HermanWang
	 * @param caseId: 案件編號
	 * @throws DataAccessException ： 出錯時拋出DataAccessException
	 * @return List<SrmCaseAssetLinkDTO> ： 得到案件最新設備連接檔資料集合
	 */
	public List<SrmCaseAssetLinkDTO> listByCaseId(String caseId) throws DataAccessException;
	/**
	 * Purpose:根據dtid查詢最新鏈接檔的設備
	 * @author HermanWang
	 * @param dtid：dtid
	 * @throws DataAccessException： 出錯時拋出DataAccessException
	 * @return List<SrmCaseAssetLinkDTO>： 得到案件最新設備連接檔資料集合
	 */
	public List<SrmCaseAssetLinkDTO> getAssetLinkListByDtid(String dtid) throws DataAccessException;
	/**
	 * Purpose:根據設備序號與案件編號查詢該案件下edc已移除而周邊設備未移除的設備list
	 * @author amandawang
	 * @param caseId：案件編號
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return String:安檢設備連接檔主鍵
	 */
	public Boolean updateCaseSerialNumberInfo(String serialNumber, String caseId, String content, String action) throws DataAccessException;
	/**
	 * Purpose:根據設備序號與案件編號查詢該案件下edc已移除而周邊設備未移除的設備list
	 * @author amandawang
	 * @param caseId：案件編號
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return String:安檢設備連接檔主鍵
	 */
	public Boolean updateCaseNewSerialNumberInfo(String serialNumber, String caseId, String content, String action) throws DataAccessException;

	/**
	 * Purpose:查詢該案件下已解綁的edc設備數量
	 * @author amandawang
	 * @param caseId:案件編號
	 * @throws DataAccessException
	 * @return Integer:條數
	 */
	public Integer countByCaseId(String caseId) throws DataAccessException;
	
	
	/**
	 * Purpose:新增最新設備鏈接檔信息
	 * @author CrissZhang
	 * @param dtid
	 * @param srmCaseHandleInfoDTO
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return void
	 */
	public void insertAssetLink(String dtid, SrmCaseHandleInfoDTO srmCaseHandleInfoDTO) throws DataAccessException;

	/**
	 * Purpose:更新案件SIM設備
	 * @author CrissZhang
	 * @param serialNumber : 設備序號
	 * @param assetTypeId ： 設備品項主鍵
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return void
	 */
	public void updateSimAssetLink(String serialNumber, String assetTypeId) throws DataAccessException;
}
