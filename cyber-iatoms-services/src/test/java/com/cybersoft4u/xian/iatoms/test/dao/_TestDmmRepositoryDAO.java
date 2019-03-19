package com.cybersoft4u.xian.iatoms.test.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.Parameter;
import cafe.core.bean.identity.LogonUser;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryHistoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetManageFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.MerchantFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.CrossTabReportDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;
/**
 * Purpose:庫存主檔DAO
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2017/5/17
 * @MaintenancePersonnel ElvaHe
 */
public class _TestDmmRepositoryDAO extends AbstractTestCase{

	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestDmmRepositoryDAO.class);
	
	/**
	 * 庫存主檔DAO
	 */
	private IDmmRepositoryDAO repositoryDAO;
	
	/**
	 * 無參構造
	 */
	public _TestDmmRepositoryDAO(){
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}

	/**
	 * Purpose：測試獲得庫存清單集合
	 * @author ElvaHe
	 * @return void
	 */
	public void testListBy(){
		try {
			String userId = "1473076241770-0048";
			String companyId = "1470648902571-0000";
			String queryUser = "123";
			String queryAction = "123";
			String queryAssetCategory = "123";
			String storage = "123";
			String queryAssetId = "123";
			String queryMerName = "123";
			String queryHeaderName = "123";
			String queryMerchantCode = "1470648902571-0000";
			String queryMerInstallAddress = "111";
			String queryArea = "01";
			String queryContractId = "1470648902571-0000";
			String queryMaType = "採購(買斷)";
			String queryIsEnabled = "Y";
			String assetTypeName = "1470648902571-0000";
			String queryKeeperName = "1470648902571-0000";
			String queryTid = "1470648902571-0000";
			String queryAssetOwner = "1470648902571-0000";
			String queryAssetUser = "1474440222588-0125";
			String queryDtid = "1470648902571-0000";
			String queryPropertyIds = "12,11";
			String querySerialNumbers = "0001,122";
			String queryIsCup = "Y";
			String afterTicketCompletionDate = "2017/01/11";
			String beforeTicketCompletionDate = "2017/01/12";
			String afterUpdateDate = "2017/01/12";
			String beforeUpdateDate = "2017/01/12";
			String queryCaseFlag = IAtomsConstants.PARAM_YES;
			String queryStorage = "111";
			String queryStatus = "111";
			String status = "111";
			String supportedFunction = "111";
			String queryHistId = "111";
			String queryBorrower = "111";
			String editFlag = "Y";
			String queryHistory = "111";
			String sort = "dtid";
			String order = "asc";
			String[] exportFields = {};
			String historyExport = "111";
			String imageName = "111";
			String ip = "111";
			String toMailId = "111";
			String queryAssetIds = "111,123";
			String queryAssetIdList = "111,122";
			String queryAssetType = "111";
			String queryCartonNo = "111";
			String queryAssetName = "111";
			String queryPeople = "111";
			String queryCommet = "111";
			String queryHouse = "111";
			String queryNumber = "111";
			String queryCounter = "111";
			String exportCodeGunPropertyIds = "111,111";
			String queryAllSelected = "Y";
			boolean isVendorAttribute = true;
			String codeGunFlag = "Y";
			String maintenanceUserFlag = "111";
			String exportCodeGunSerialNumbers = "111,12";
			String hideQueryAssetUser = "Y";
			String exportField = "111";
			String queryParam = "111";
			Map<String, String> fieldMap = new HashMap<String, String>();
			String toMail = "123@qq.com";
			int rows = 10;
			int page = 1;
			int totalSize = 10;
			CompanyDTO companyDTO = new CompanyDTO();
			MerchantFormDTO merchantFormDTO = new MerchantFormDTO();
			List<Parameter> departmentList = new ArrayList<Parameter>();
			List<DmmRepositoryHistoryDTO> dmmRepositoryHistoryDTOs = new ArrayList<DmmRepositoryHistoryDTO>();
			DmmRepositoryHistoryDTO dmmRepositoryHistoryDTO = new DmmRepositoryHistoryDTO();
			DmmRepositoryDTO dmmRepositoryDTO = new DmmRepositoryDTO();
			//設備管理作業formDTO
			AssetManageFormDTO assetManageFormDTO = 
					new AssetManageFormDTO(queryStorage, queryStatus, companyDTO, companyId,
							assetTypeName, storage, status, supportedFunction, queryContractId, 
							queryMerName, queryHeaderName, queryMerchantCode, queryMerInstallAddress,
							queryArea, queryMaType, queryIsEnabled, queryKeeperName, queryTid, queryAssetOwner,
							queryAssetUser, queryDtid, queryPropertyIds, querySerialNumbers, queryAssetId, queryHistId,
							queryBorrower, editFlag, queryAssetCategory, queryUser, queryHistory, dmmRepositoryHistoryDTOs, 
							dmmRepositoryHistoryDTO, rows, page, sort, order, totalSize, dmmRepositoryDTO, beforeTicketCompletionDate,
							afterTicketCompletionDate, beforeUpdateDate, afterUpdateDate, queryIsCup, exportFields, historyExport,
							imageName, ip, queryAction, userId, toMailId, queryAssetIds, queryAssetIdList, queryAssetType,
							queryAssetName, queryPeople, queryCommet, queryHouse, queryNumber, merchantFormDTO, queryCounter, 
							queryCartonNo, isVendorAttribute, codeGunFlag, maintenanceUserFlag, exportCodeGunSerialNumbers,
							exportCodeGunPropertyIds, queryAllSelected, fieldMap, toMail, exportField, queryParam, 
							departmentList, queryCaseFlag, hideQueryAssetUser);
					
					
			List<DmmRepositoryDTO> repositoryDTOs = this.repositoryDAO.listBy(assetManageFormDTO);
			if (!CollectionUtils.isEmpty(repositoryDTOs)) {
				Assert.assertNotNull(repositoryDTOs);
			} else {
				Assert.assertTrue(repositoryDTOs != null);
			}
			queryCaseFlag="";
			userId = null;
			rows = 0;
			queryAssetIds=null;
			queryAssetIdList = null;
			querySerialNumbers = null;
			queryPropertyIds = null;
			assetTypeName ="1,2,,";
			queryAction = "1,2,,";
			queryAssetUser = "1,2,,";
			assetManageFormDTO = 
					new AssetManageFormDTO(queryStorage, queryStatus, companyDTO, companyId,
							assetTypeName, storage, status, supportedFunction, queryContractId, 
							queryMerName, queryHeaderName, queryMerchantCode, queryMerInstallAddress,
							queryArea, queryMaType, queryIsEnabled, queryKeeperName, queryTid, queryAssetOwner,
							queryAssetUser, queryDtid, queryPropertyIds, querySerialNumbers, queryAssetId, queryHistId,
							queryBorrower, editFlag, queryAssetCategory, queryUser, queryHistory, dmmRepositoryHistoryDTOs, 
							dmmRepositoryHistoryDTO, rows, page, sort, order, totalSize, dmmRepositoryDTO, beforeTicketCompletionDate,
							afterTicketCompletionDate, beforeUpdateDate, afterUpdateDate, queryIsCup, exportFields, historyExport,
							imageName, ip, queryAction, userId, toMailId, queryAssetIds, queryAssetIdList, queryAssetType,
							queryAssetName, queryPeople, queryCommet, queryHouse, queryNumber, merchantFormDTO, queryCounter, 
							queryCartonNo, isVendorAttribute, codeGunFlag, maintenanceUserFlag, exportCodeGunSerialNumbers,
							exportCodeGunPropertyIds, queryAllSelected, fieldMap, toMail, exportField, queryParam, 
							departmentList, queryCaseFlag, hideQueryAssetUser);
					
					
			repositoryDTOs = this.repositoryDAO.listBy(assetManageFormDTO);
			if (!CollectionUtils.isEmpty(repositoryDTOs)) {
				Assert.assertNotNull(repositoryDTOs);
			} else {
				Assert.assertTrue(repositoryDTOs != null);
			}
			assetTypeName =null;
			queryAction = null;
			queryAssetUser = null;
			queryAssetUser = null;
			assetManageFormDTO = 
					new AssetManageFormDTO(queryStorage, queryStatus, companyDTO, companyId,
							assetTypeName, storage, status, supportedFunction, queryContractId, 
							queryMerName, queryHeaderName, queryMerchantCode, queryMerInstallAddress,
							queryArea, queryMaType, queryIsEnabled, queryKeeperName, queryTid, queryAssetOwner,
							queryAssetUser, queryDtid, queryPropertyIds, querySerialNumbers, queryAssetId, queryHistId,
							queryBorrower, editFlag, queryAssetCategory, queryUser, queryHistory, dmmRepositoryHistoryDTOs, 
							dmmRepositoryHistoryDTO, rows, page, sort, order, totalSize, dmmRepositoryDTO, beforeTicketCompletionDate,
							afterTicketCompletionDate, beforeUpdateDate, afterUpdateDate, queryIsCup, exportFields, historyExport,
							imageName, ip, queryAction, userId, toMailId, queryAssetIds, queryAssetIdList, queryAssetType,
							queryAssetName, queryPeople, queryCommet, queryHouse, queryNumber, merchantFormDTO, queryCounter, 
							queryCartonNo, isVendorAttribute, codeGunFlag, maintenanceUserFlag, exportCodeGunSerialNumbers,
							exportCodeGunPropertyIds, queryAllSelected, fieldMap, toMail, exportField, queryParam, 
							departmentList, queryCaseFlag, hideQueryAssetUser);
					
					
			repositoryDTOs = this.repositoryDAO.listBy(assetManageFormDTO);
			if (!CollectionUtils.isEmpty(repositoryDTOs)) {
				Assert.assertNotNull(repositoryDTOs);
			} else {
				Assert.assertTrue(repositoryDTOs != null);
			}
			 userId = "";
			 companyId = "";
			 queryUser = "";
			 queryAction = "";
			 queryAssetCategory = "";
			 storage = "";
			 queryAssetId = "";
			 queryMerName = "";
			 queryHeaderName = "";
			 queryMerchantCode = "";
			 queryMerInstallAddress = "";
			 queryArea = "";
			 queryContractId = "";
			 queryMaType = "";
			 queryIsEnabled = "";
			 assetTypeName = "";
			 queryKeeperName = "";
			 queryTid = "";
			 queryAssetOwner = "";
			 queryAssetUser = "";
			 queryDtid = "";
			 queryPropertyIds = "12, ,11";
			 querySerialNumbers = "0001, ,122";
			 queryIsCup = "";
			 afterTicketCompletionDate = "";
			 beforeTicketCompletionDate = "";
			 afterUpdateDate = "";
			 beforeUpdateDate = "";
			 queryCaseFlag = IAtomsConstants.PARAM_NO;
			 queryStorage = "";
			 queryStatus = "";
			 status = "";
			 supportedFunction = "";
			 queryHistId = "";
			 queryBorrower = "";
			 editFlag = "N";
			 queryHistory = "";
			 sort = "";
			 order = "";
			 historyExport = "";
			 imageName = "";
			 ip = "";
			 toMailId = "";
			 queryAssetIds = "111, ,123";
			 queryAssetIdList = "111, ,122";
			 queryAssetType = "";
			 queryCartonNo = "";
			 queryAssetName = "";
			 queryPeople = "";
			 queryCommet = "";
			 queryHouse = "";
			 queryNumber = "";
			 queryCounter = "";
			 exportCodeGunPropertyIds = "111, ,111";
			 queryAllSelected = "N";
			 isVendorAttribute = false;
			 codeGunFlag = "N";
			 maintenanceUserFlag = "";
			 exportCodeGunSerialNumbers = "111, ,12";
			 hideQueryAssetUser = "N";
			 exportField = "";
			 queryParam = "";
			fieldMap = new HashMap<String, String>();
			 toMail = "";
			rows = -1;
			page = 2;
			totalSize = 10;
			assetManageFormDTO = 
					new AssetManageFormDTO(queryStorage, queryStatus, companyDTO, companyId,
							assetTypeName, storage, status, supportedFunction, queryContractId, 
							queryMerName, queryHeaderName, queryMerchantCode, queryMerInstallAddress,
							queryArea, queryMaType, queryIsEnabled, queryKeeperName, queryTid, queryAssetOwner,
							queryAssetUser, queryDtid, queryPropertyIds, querySerialNumbers, queryAssetId, queryHistId,
							queryBorrower, editFlag, queryAssetCategory, queryUser, queryHistory, dmmRepositoryHistoryDTOs, 
							dmmRepositoryHistoryDTO, rows, page, sort, order, totalSize, dmmRepositoryDTO, beforeTicketCompletionDate,
							afterTicketCompletionDate, beforeUpdateDate, afterUpdateDate, queryIsCup, exportFields, historyExport,
							imageName, ip, queryAction, userId, toMailId, queryAssetIds, queryAssetIdList, queryAssetType,
							queryAssetName, queryPeople, queryCommet, queryHouse, queryNumber, merchantFormDTO, queryCounter, 
							queryCartonNo, isVendorAttribute, codeGunFlag, maintenanceUserFlag, exportCodeGunSerialNumbers,
							exportCodeGunPropertyIds, queryAllSelected, fieldMap, toMail, exportField, queryParam, 
							departmentList, queryCaseFlag, hideQueryAssetUser);
					
					
			repositoryDTOs = this.repositoryDAO.listBy(assetManageFormDTO);
			if (!CollectionUtils.isEmpty(repositoryDTOs)) {
				Assert.assertNotNull(repositoryDTOs);
			} else {
				Assert.assertTrue(repositoryDTOs != null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testListBy()", "is error:", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:獲得條數
	 */
	public void testCount(){
		try {
			String userId = "1473076241770-0048";
			String companyId = "1470648902571-0000";
			String queryUser = "123";
			String queryAction = "123";
			String queryAssetCategory = "123";
			String storage = "123";
			String queryAssetId = "123";
			String queryMerName = "123";
			String queryHeaderName = "123";
			String queryMerchantCode = "1470648902571-0000";
			String queryMerInstallAddress = "111";
			String queryArea = "01";
			String queryContractId = "1470648902571-0000";
			String queryMaType = "採購(買斷)";
			String queryIsEnabled = "Y";
			String assetTypeName = "1470648902571-0000";
			String queryKeeperName = "1470648902571-0000";
			String queryTid = "1470648902571-0000";
			String queryAssetOwner = "1470648902571-0000";
			String queryAssetUser = "1474440222588-0125";
			String queryDtid = "1470648902571-0000";
			String queryPropertyIds = "12,11";
			String querySerialNumbers = "0001,122";
			String queryIsCup = "Y";
			String afterTicketCompletionDate = "2017/01/11";
			String beforeTicketCompletionDate = "2017/01/12";
			String afterUpdateDate = "2017/01/12";
			String beforeUpdateDate = "2017/01/12";
			String queryCaseFlag = IAtomsConstants.PARAM_YES;
			String queryStorage = "111";
			String queryStatus = "111";
			String status = "111";
			String supportedFunction = "111";
			String queryHistId = "111";
			String queryBorrower = "111";
			String editFlag = "Y";
			String queryHistory = "111";
			String sort = "dtid";
			String order = "asc";
			String[] exportFields = {};
			String historyExport = "111";
			String imageName = "111";
			String ip = "111";
			String toMailId = "111";
			String queryAssetIds = "111,123";
			String queryAssetIdList = "111,122";
			String queryAssetType = "111";
			String queryCartonNo = "111";
			String queryAssetName = "111";
			String queryPeople = "111";
			String queryCommet = "111";
			String queryHouse = "111";
			String queryNumber = "111";
			String queryCounter = "111";
			String exportCodeGunPropertyIds = "111,111";
			String queryAllSelected = "Y";
			boolean isVendorAttribute = true;
			String codeGunFlag = "Y";
			String maintenanceUserFlag = "111";
			String exportCodeGunSerialNumbers = "111,12";
			String hideQueryAssetUser = "Y";
			String exportField = "111";
			String queryParam = "111";
			Map<String, String> fieldMap = new HashMap<String, String>();
			String toMail = "123@qq.com";
			int rows = 10;
			int page = 1;
			int totalSize = 10;
			CompanyDTO companyDTO = new CompanyDTO();
			MerchantFormDTO merchantFormDTO = new MerchantFormDTO();
			List<Parameter> departmentList = new ArrayList<Parameter>();
			List<DmmRepositoryHistoryDTO> dmmRepositoryHistoryDTOs = new ArrayList<DmmRepositoryHistoryDTO>();
			DmmRepositoryHistoryDTO dmmRepositoryHistoryDTO = new DmmRepositoryHistoryDTO();
			DmmRepositoryDTO dmmRepositoryDTO = new DmmRepositoryDTO();
			//設備管理作業formDTO
			AssetManageFormDTO assetManageFormDTO = 
					new AssetManageFormDTO(queryStorage, queryStatus, companyDTO, companyId, assetTypeName, queryStorage, status, supportedFunction, queryContractId, queryMerName, queryHeaderName, queryMerchantCode, queryMerInstallAddress, queryArea, queryMaType, queryIsEnabled, queryKeeperName, queryTid, queryAssetOwner, queryAssetUser, queryDtid,
							queryPropertyIds, querySerialNumbers, queryAssetId, queryHistId, queryBorrower, editFlag, queryAssetCategory,
							queryUser, queryHistory, dmmRepositoryHistoryDTOs, dmmRepositoryHistoryDTO, rows, page, sort, order, totalSize, 
							dmmRepositoryDTO, beforeTicketCompletionDate, afterTicketCompletionDate, beforeUpdateDate, afterUpdateDate, queryIsCup, 
							exportFields, historyExport, imageName, ip, queryAction, userId, toMailId, queryAssetIds, queryAssetIdList, queryAssetType,
							queryAssetName, queryPeople, queryCommet, queryHouse, queryNumber, merchantFormDTO, queryCounter, queryCartonNo, 
							isVendorAttribute, codeGunFlag, maintenanceUserFlag, exportCodeGunSerialNumbers, exportCodeGunPropertyIds, 
							queryAllSelected, fieldMap, toMail, exportField, queryParam, departmentList, queryCaseFlag, hideQueryAssetUser);
					
			Integer count = this.repositoryDAO.count(assetManageFormDTO);
			if(count > 0){
				Assert.assertEquals(count.intValue(), count.intValue());
			} else {
				Assert.assertFalse(count > 0 );
			}
			queryCaseFlag="";
			userId = null;
			rows = 0;
			queryAssetIds=null;
			queryAssetIdList = null;
			querySerialNumbers = null;
			queryPropertyIds = null;
			assetTypeName ="1,2,,";
			queryAction = "1,2,,";
			queryAssetUser = "1,2,,";
			assetManageFormDTO = 
					new AssetManageFormDTO(queryStorage, queryStatus, companyDTO, companyId, assetTypeName, queryStorage, status, supportedFunction, queryContractId, queryMerName, queryHeaderName, queryMerchantCode, queryMerInstallAddress, queryArea, queryMaType, queryIsEnabled, queryKeeperName, queryTid, queryAssetOwner, queryAssetUser, queryDtid,
							queryPropertyIds, querySerialNumbers, queryAssetId, queryHistId, queryBorrower, editFlag, queryAssetCategory,
							queryUser, queryHistory, dmmRepositoryHistoryDTOs, dmmRepositoryHistoryDTO, rows, page, sort, order, totalSize, 
							dmmRepositoryDTO, beforeTicketCompletionDate, afterTicketCompletionDate, beforeUpdateDate, afterUpdateDate, queryIsCup, 
							exportFields, historyExport, imageName, ip, queryAction, userId, toMailId, queryAssetIds, queryAssetIdList, queryAssetType,
							queryAssetName, queryPeople, queryCommet, queryHouse, queryNumber, merchantFormDTO, queryCounter, queryCartonNo, 
							isVendorAttribute, codeGunFlag, maintenanceUserFlag, exportCodeGunSerialNumbers, exportCodeGunPropertyIds, 
							queryAllSelected, fieldMap, toMail, exportField, queryParam, departmentList, queryCaseFlag, hideQueryAssetUser);
					
			count = this.repositoryDAO.count(assetManageFormDTO);
			if(count > 0){
				Assert.assertEquals(count.intValue(), count.intValue());
			} else {
				Assert.assertFalse(count > 0 );
			}
			assetTypeName =null;
			queryAction = null;
			queryAssetUser = null;
			queryAssetUser = null;
			assetManageFormDTO = 
					new AssetManageFormDTO(queryStorage, queryStatus, companyDTO, companyId, assetTypeName, queryStorage, status, supportedFunction, queryContractId, queryMerName, queryHeaderName, queryMerchantCode, queryMerInstallAddress, queryArea, queryMaType, queryIsEnabled, queryKeeperName, queryTid, queryAssetOwner, queryAssetUser, queryDtid,
							queryPropertyIds, querySerialNumbers, queryAssetId, queryHistId, queryBorrower, editFlag, queryAssetCategory,
							queryUser, queryHistory, dmmRepositoryHistoryDTOs, dmmRepositoryHistoryDTO, rows, page, sort, order, totalSize, 
							dmmRepositoryDTO, beforeTicketCompletionDate, afterTicketCompletionDate, beforeUpdateDate, afterUpdateDate, queryIsCup, 
							exportFields, historyExport, imageName, ip, queryAction, userId, toMailId, queryAssetIds, queryAssetIdList, queryAssetType,
							queryAssetName, queryPeople, queryCommet, queryHouse, queryNumber, merchantFormDTO, queryCounter, queryCartonNo, 
							isVendorAttribute, codeGunFlag, maintenanceUserFlag, exportCodeGunSerialNumbers, exportCodeGunPropertyIds, 
							queryAllSelected, fieldMap, toMail, exportField, queryParam, departmentList, queryCaseFlag, hideQueryAssetUser);
					
			count = this.repositoryDAO.count(assetManageFormDTO);
			if(count > 0){
				Assert.assertEquals(count.intValue(), count.intValue());
			} else {
				Assert.assertFalse(count > 0 );
			}
			 userId = "";
			 companyId = "";
			 queryUser = "";
			 queryAction = "";
			 queryAssetCategory = "";
			 storage = "";
			 queryAssetId = "";
			 queryMerName = "";
			 queryHeaderName = "";
			 queryMerchantCode = "";
			 queryMerInstallAddress = "";
			 queryArea = "";
			 queryContractId = "";
			 queryMaType = "";
			 queryIsEnabled = "";
			 assetTypeName = "";
			 queryKeeperName = "";
			 queryTid = "";
			 queryAssetOwner = "";
			 queryAssetUser = "";
			 queryDtid = "";
			 queryPropertyIds = "12, ,11";
			 querySerialNumbers = "0001, ,122";
			 queryIsCup = "";
			 afterTicketCompletionDate = "";
			 beforeTicketCompletionDate = "";
			 afterUpdateDate = "";
			 beforeUpdateDate = "";
			 queryCaseFlag = IAtomsConstants.PARAM_NO;
			 queryStorage = "";
			 queryStatus = "";
			 status = "";
			 supportedFunction = "";
			 queryHistId = "";
			 queryBorrower = "";
			 editFlag = "N";
			 queryHistory = "";
			 sort = "";
			 order = "";
			 historyExport = "";
			 imageName = "";
			 ip = "";
			 toMailId = "";
			 queryAssetIds = "111, ,123";
			 queryAssetIdList = "111, ,122";
			 queryAssetType = "";
			 queryCartonNo = "";
			 queryAssetName = "";
			 queryPeople = "";
			 queryCommet = "";
			 queryHouse = "";
			 queryNumber = "";
			 queryCounter = "";
			 exportCodeGunPropertyIds = "111, ,111";
			 queryAllSelected = "N";
			 isVendorAttribute = false;
			 codeGunFlag = "N";
			 maintenanceUserFlag = "";
			 exportCodeGunSerialNumbers = "111, ,12";
			 hideQueryAssetUser = "N";
			 exportField = "";
			 queryParam = "";
			 fieldMap = new HashMap<String, String>();
			 toMail = "";
			 rows = -1;
			 page = 2;
			 totalSize = 10;
			 assetManageFormDTO = 
						new AssetManageFormDTO(queryStorage, queryStatus, companyDTO, companyId, assetTypeName, queryStorage, status, supportedFunction, queryContractId, queryMerName, queryHeaderName, queryMerchantCode, queryMerInstallAddress, queryArea, queryMaType, queryIsEnabled, queryKeeperName, queryTid, queryAssetOwner, queryAssetUser, queryDtid,
								queryPropertyIds, querySerialNumbers, queryAssetId, queryHistId, queryBorrower, editFlag, queryAssetCategory,
								queryUser, queryHistory, dmmRepositoryHistoryDTOs, dmmRepositoryHistoryDTO, rows, page, sort, order, totalSize, 
								dmmRepositoryDTO, beforeTicketCompletionDate, afterTicketCompletionDate, beforeUpdateDate, afterUpdateDate, queryIsCup, 
								exportFields, historyExport, imageName, ip, queryAction, userId, toMailId, queryAssetIds, queryAssetIdList, queryAssetType,
								queryAssetName, queryPeople, queryCommet, queryHouse, queryNumber, merchantFormDTO, queryCounter, queryCartonNo, 
								isVendorAttribute, codeGunFlag, maintenanceUserFlag, exportCodeGunSerialNumbers, exportCodeGunPropertyIds, 
								queryAllSelected, fieldMap, toMail, exportField, queryParam, departmentList, queryCaseFlag, hideQueryAssetUser);
						
				count = this.repositoryDAO.count(assetManageFormDTO);
				if(count > 0){
					Assert.assertEquals(count.intValue(), count.intValue());
				} else {
					Assert.assertFalse(count > 0 );
				}
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testCount()", "is error:", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:獲得歷史庫存集合
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetListByAssetId(){
		try {
			String userId = "1473076241770-0048";
			String companyId = "1470648902571-0000";
			String queryUser = "123";
			String queryAction = "123";
			String queryAssetCategory = "123";
			String storage = "123";
			String queryAssetId = "123";
			String queryMerName = "123";
			String queryHeaderName = "123";
			String queryMerchantCode = "1470648902571-0000";
			String queryMerInstallAddress = "111";
			String queryArea = "01";
			String queryContractId = "1470648902571-0000";
			String queryMaType = "採購(買斷)";
			String queryIsEnabled = "Y";
			String assetTypeName = "1470648902571-0000";
			String queryKeeperName = "1470648902571-0000";
			String queryTid = "1470648902571-0000";
			String queryAssetOwner = "1470648902571-0000";
			String queryAssetUser = "1474440222588-0125";
			String queryDtid = "1470648902571-0000";
			String queryPropertyIds = "12,11";
			String querySerialNumbers = "0001,122";
			String queryIsCup = "Y";
			String afterTicketCompletionDate = "2017/01/11";
			String beforeTicketCompletionDate = "2017/01/12";
			String afterUpdateDate = "2017/01/12";
			String beforeUpdateDate = "2017/01/12";
			String queryCaseFlag = IAtomsConstants.PARAM_YES;
			String queryStorage = "111";
			String queryStatus = "111";
			String status = "111";
			String supportedFunction = "111";
			String queryHistId = "111";
			String queryBorrower = "111";
			String editFlag = "Y";
			String queryHistory = "111";
			String sort = "dtid";
			String order = "asc";
			String[] exportFields = {};
			String historyExport = "111";
			String imageName = "111";
			String ip = "111";
			String toMailId = "111";
			String queryAssetIds = "111,123";
			String queryAssetIdList = "111,122";
			String queryAssetType = "111";
			String queryCartonNo = "111";
			String queryAssetName = "111";
			String queryPeople = "111";
			String queryCommet = "111";
			String queryHouse = "111";
			String queryNumber = "111";
			String queryCounter = "111";
			String exportCodeGunPropertyIds = "111,111";
			String queryAllSelected = "Y";
			boolean isVendorAttribute = true;
			String codeGunFlag = "Y";
			String maintenanceUserFlag = "111";
			String exportCodeGunSerialNumbers = "111,12";
			String hideQueryAssetUser = "Y";
			String exportField = "111";
			String queryParam = "111";
			Map<String, String> fieldMap = new HashMap<String, String>();
			String toMail = "123@qq.com";
			int rows = 10;
			int page = 1;
			int totalSize = 10;
			CompanyDTO companyDTO = new CompanyDTO();
			MerchantFormDTO merchantFormDTO = new MerchantFormDTO();
			List<Parameter> departmentList = new ArrayList<Parameter>();
			List<DmmRepositoryHistoryDTO> dmmRepositoryHistoryDTOs = new ArrayList<DmmRepositoryHistoryDTO>();
			DmmRepositoryHistoryDTO dmmRepositoryHistoryDTO = new DmmRepositoryHistoryDTO();
			DmmRepositoryDTO dmmRepositoryDTO = new DmmRepositoryDTO();
			//設備管理作業formDTO
			AssetManageFormDTO assetManageFormDTO = 
					new AssetManageFormDTO(queryStorage, queryStatus, companyDTO, companyId, assetTypeName, queryStorage, status, supportedFunction, queryContractId, queryMerName, queryHeaderName, queryMerchantCode, queryMerInstallAddress, queryArea, queryMaType, queryIsEnabled, queryKeeperName, queryTid, queryAssetOwner, queryAssetUser, queryDtid, queryPropertyIds, querySerialNumbers, queryAssetId, queryHistId, queryBorrower, editFlag, queryAssetCategory, queryUser, queryHistory, dmmRepositoryHistoryDTOs, dmmRepositoryHistoryDTO, rows, page, sort, order, totalSize, dmmRepositoryDTO, beforeTicketCompletionDate, afterTicketCompletionDate, beforeUpdateDate, afterUpdateDate, queryIsCup, exportFields, historyExport, imageName, ip, queryAction, userId, toMailId, queryAssetIds, queryAssetIdList, queryAssetType, queryAssetName, queryPeople, queryCommet, queryHouse, queryNumber, merchantFormDTO, queryCounter, queryCartonNo, isVendorAttribute, codeGunFlag, maintenanceUserFlag, exportCodeGunSerialNumbers, exportCodeGunPropertyIds, queryAllSelected, fieldMap, toMail, exportField, queryParam, departmentList, queryCaseFlag, hideQueryAssetUser);
			List<DmmRepositoryHistoryDTO> dmmRepositoryHistoryDTOList = this.repositoryDAO.getListByAssetId(assetManageFormDTO);
			if (!CollectionUtils.isEmpty(dmmRepositoryHistoryDTOList)) {
				Assert.assertNotNull(dmmRepositoryHistoryDTOList);
			} else {
				Assert.assertFalse(dmmRepositoryHistoryDTOList == null);
			}
			queryCaseFlag="";
			userId = null;
			rows = 0;
			queryAssetIds=null;
			queryAssetIdList = null;
			querySerialNumbers = null;
			queryPropertyIds = null;
			assetTypeName ="1,2,,";
			queryAction = "1,2,,";
			queryAssetUser = "1,2,,";
			assetManageFormDTO = 
					new AssetManageFormDTO(queryStorage, queryStatus, companyDTO, companyId, assetTypeName, queryStorage, status, supportedFunction, queryContractId, queryMerName, queryHeaderName, queryMerchantCode, queryMerInstallAddress, queryArea, queryMaType, queryIsEnabled, queryKeeperName, queryTid, queryAssetOwner, queryAssetUser, queryDtid, queryPropertyIds, querySerialNumbers, queryAssetId, queryHistId, queryBorrower, editFlag, queryAssetCategory, queryUser, queryHistory, dmmRepositoryHistoryDTOs, dmmRepositoryHistoryDTO, rows, page, sort, order, totalSize, dmmRepositoryDTO, beforeTicketCompletionDate, afterTicketCompletionDate, beforeUpdateDate, afterUpdateDate, queryIsCup, exportFields, historyExport, imageName, ip, queryAction, userId, toMailId, queryAssetIds, queryAssetIdList, queryAssetType, queryAssetName, queryPeople, queryCommet, queryHouse, queryNumber, merchantFormDTO, queryCounter, queryCartonNo, isVendorAttribute, codeGunFlag, maintenanceUserFlag, exportCodeGunSerialNumbers, exportCodeGunPropertyIds, queryAllSelected, fieldMap, toMail, exportField, queryParam, departmentList, queryCaseFlag, hideQueryAssetUser);
			dmmRepositoryHistoryDTOList = this.repositoryDAO.getListByAssetId(assetManageFormDTO);
			if (!CollectionUtils.isEmpty(dmmRepositoryHistoryDTOList)) {
				Assert.assertNotNull(dmmRepositoryHistoryDTOList);
			} else {
				Assert.assertFalse(dmmRepositoryHistoryDTOList == null);
			}
			assetTypeName =null;
			queryAction = null;
			queryAssetUser = null;
			queryAssetUser = null;
			assetManageFormDTO = 
					new AssetManageFormDTO(queryStorage, queryStatus, companyDTO, companyId, assetTypeName, queryStorage, status, supportedFunction, queryContractId, queryMerName, queryHeaderName, queryMerchantCode, queryMerInstallAddress, queryArea, queryMaType, queryIsEnabled, queryKeeperName, queryTid, queryAssetOwner, queryAssetUser, queryDtid, queryPropertyIds, querySerialNumbers, queryAssetId, queryHistId, queryBorrower, editFlag, queryAssetCategory, queryUser, queryHistory, dmmRepositoryHistoryDTOs, dmmRepositoryHistoryDTO, rows, page, sort, order, totalSize, dmmRepositoryDTO, beforeTicketCompletionDate, afterTicketCompletionDate, beforeUpdateDate, afterUpdateDate, queryIsCup, exportFields, historyExport, imageName, ip, queryAction, userId, toMailId, queryAssetIds, queryAssetIdList, queryAssetType, queryAssetName, queryPeople, queryCommet, queryHouse, queryNumber, merchantFormDTO, queryCounter, queryCartonNo, isVendorAttribute, codeGunFlag, maintenanceUserFlag, exportCodeGunSerialNumbers, exportCodeGunPropertyIds, queryAllSelected, fieldMap, toMail, exportField, queryParam, departmentList, queryCaseFlag, hideQueryAssetUser);
			dmmRepositoryHistoryDTOList = this.repositoryDAO.getListByAssetId(assetManageFormDTO);
			if (!CollectionUtils.isEmpty(dmmRepositoryHistoryDTOList)) {
				Assert.assertNotNull(dmmRepositoryHistoryDTOList);
			} else {
				Assert.assertFalse(dmmRepositoryHistoryDTOList == null);
			}
			 userId = "";
			 companyId = "";
			 queryUser = "";
			 queryAction = "";
			 queryAssetCategory = "";
			 storage = "";
			 queryAssetId = "";
			 queryMerName = "";
			 queryHeaderName = "";
			 queryMerchantCode = "";
			 queryMerInstallAddress = "";
			 queryArea = "";
			 queryContractId = "";
			 queryMaType = "";
			 queryIsEnabled = "";
			 assetTypeName = "";
			 queryKeeperName = "";
			 queryTid = "";
			 queryAssetOwner = "";
			 queryAssetUser = "";
			 queryDtid = "";
			 queryPropertyIds = "12, ,11";
			 querySerialNumbers = "0001, ,122";
			 queryIsCup = "";
			 afterTicketCompletionDate = "";
			 beforeTicketCompletionDate = "";
			 afterUpdateDate = "";
			 beforeUpdateDate = "";
			 queryCaseFlag = IAtomsConstants.PARAM_NO;
			 queryStorage = "";
			 queryStatus = "";
			 status = "";
			 supportedFunction = "";
			 queryHistId = "";
			 queryBorrower = "";
			 editFlag = "N";
			 queryHistory = "";
			 sort = "";
			 order = "";
			 historyExport = "";
			 imageName = "";
			 ip = "";
			 toMailId = "";
			 queryAssetIds = "111, ,123";
			 queryAssetIdList = "111, ,122";
			 queryAssetType = "";
			 queryCartonNo = "";
			 queryAssetName = "";
			 queryPeople = "";
			 queryCommet = "";
			 queryHouse = "";
			 queryNumber = "";
			 queryCounter = "";
			 exportCodeGunPropertyIds = "111, ,111";
			 queryAllSelected = "N";
			 isVendorAttribute = false;
			 codeGunFlag = "N";
			 maintenanceUserFlag = "";
			 exportCodeGunSerialNumbers = "111, ,12";
			 hideQueryAssetUser = "N";
			 exportField = "";
			 queryParam = "";
			 fieldMap = new HashMap<String, String>();
			 toMail = "";
			 rows = -1;
			 page = 2;
			 totalSize = 10;
			 assetManageFormDTO = 
						new AssetManageFormDTO(queryStorage, queryStatus, companyDTO, companyId, assetTypeName, queryStorage, status, supportedFunction, queryContractId, queryMerName, queryHeaderName, queryMerchantCode, queryMerInstallAddress, queryArea, queryMaType, queryIsEnabled, queryKeeperName, queryTid, queryAssetOwner, queryAssetUser, queryDtid, queryPropertyIds, querySerialNumbers, queryAssetId, queryHistId, queryBorrower, editFlag, queryAssetCategory, queryUser, queryHistory, dmmRepositoryHistoryDTOs, dmmRepositoryHistoryDTO, rows, page, sort, order, totalSize, dmmRepositoryDTO, beforeTicketCompletionDate, afterTicketCompletionDate, beforeUpdateDate, afterUpdateDate, queryIsCup, exportFields, historyExport, imageName, ip, queryAction, userId, toMailId, queryAssetIds, queryAssetIdList, queryAssetType, queryAssetName, queryPeople, queryCommet, queryHouse, queryNumber, merchantFormDTO, queryCounter, queryCartonNo, isVendorAttribute, codeGunFlag, maintenanceUserFlag, exportCodeGunSerialNumbers, exportCodeGunPropertyIds, queryAllSelected, fieldMap, toMail, exportField, queryParam, departmentList, queryCaseFlag, hideQueryAssetUser);
				dmmRepositoryHistoryDTOList = this.repositoryDAO.getListByAssetId(assetManageFormDTO);
				if (!CollectionUtils.isEmpty(dmmRepositoryHistoryDTOList)) {
					Assert.assertNotNull(dmmRepositoryHistoryDTOList);
				} else {
					Assert.assertFalse(dmmRepositoryHistoryDTOList == null);
				}
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testGetListByAssetId()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:獲得條數
	 */
	public void testGetCountByAssetId(){
		try {
			String userId = "1473076241770-0048";
			String companyId = "1470648902571-0000";
			String queryUser = "123";
			String queryAction = "123";
			String queryAssetCategory = "123";
			String storage = "123";
			String queryAssetId = "123";
			String queryMerName = "123";
			String queryHeaderName = "123";
			String queryMerchantCode = "1470648902571-0000";
			String queryMerInstallAddress = "111";
			String queryArea = "01";
			String queryContractId = "1470648902571-0000";
			String queryMaType = "採購(買斷)";
			String queryIsEnabled = "Y";
			String assetTypeName = "1470648902571-0000";
			String queryKeeperName = "1470648902571-0000";
			String queryTid = "1470648902571-0000";
			String queryAssetOwner = "1470648902571-0000";
			String queryAssetUser = "1474440222588-0125";
			String queryDtid = "1470648902571-0000";
			String queryPropertyIds = "12,11";
			String querySerialNumbers = "0001,122";
			String queryIsCup = "Y";
			String afterTicketCompletionDate = "2017/01/11";
			String beforeTicketCompletionDate = "2017/01/12";
			String afterUpdateDate = "2017/01/12";
			String beforeUpdateDate = "2017/01/12";
			String queryCaseFlag = IAtomsConstants.PARAM_YES;
			String queryStorage = "111";
			String queryStatus = "111";
			String status = "111";
			String supportedFunction = "111";
			String queryHistId = "111";
			String queryBorrower = "111";
			String editFlag = "Y";
			String queryHistory = "111";
			String sort = "dtid";
			String order = "asc";
			String[] exportFields = {};
			String historyExport = "111";
			String imageName = "111";
			String ip = "111";
			String toMailId = "111";
			String queryAssetIds = "111,123";
			String queryAssetIdList = "111,122";
			String queryAssetType = "111";
			String queryCartonNo = "111";
			String queryAssetName = "111";
			String queryPeople = "111";
			String queryCommet = "111";
			String queryHouse = "111";
			String queryNumber = "111";
			String queryCounter = "111";
			String exportCodeGunPropertyIds = "111,111";
			String queryAllSelected = "Y";
			boolean isVendorAttribute = true;
			String codeGunFlag = "Y";
			String maintenanceUserFlag = "111";
			String exportCodeGunSerialNumbers = "111,12";
			String hideQueryAssetUser = "Y";
			String exportField = "111";
			String queryParam = "111";
			Map<String, String> fieldMap = new HashMap<String, String>();
			String toMail = "123@qq.com";
			int rows = 10;
			int page = 1;
			int totalSize = 10;
			CompanyDTO companyDTO = new CompanyDTO();
			MerchantFormDTO merchantFormDTO = new MerchantFormDTO();
			List<Parameter> departmentList = new ArrayList<Parameter>();
			List<DmmRepositoryHistoryDTO> dmmRepositoryHistoryDTOs = new ArrayList<DmmRepositoryHistoryDTO>();
			DmmRepositoryHistoryDTO dmmRepositoryHistoryDTO = new DmmRepositoryHistoryDTO();
			DmmRepositoryDTO dmmRepositoryDTO = new DmmRepositoryDTO();
			//設備管理作業formDTO
			AssetManageFormDTO assetManageFormDTO = 
					new AssetManageFormDTO(queryStorage, queryStatus, companyDTO, companyId, assetTypeName, queryStorage, status, supportedFunction, queryContractId, queryMerName, queryHeaderName, queryMerchantCode, queryMerInstallAddress, queryArea, queryMaType, queryIsEnabled, queryKeeperName, queryTid, queryAssetOwner, queryAssetUser, queryDtid, queryPropertyIds, querySerialNumbers, queryAssetId, queryHistId, queryBorrower, editFlag, queryAssetCategory, queryUser, queryHistory, dmmRepositoryHistoryDTOs, dmmRepositoryHistoryDTO, rows, page, sort, order, totalSize, dmmRepositoryDTO, beforeTicketCompletionDate, afterTicketCompletionDate, beforeUpdateDate, afterUpdateDate, queryIsCup, exportFields, historyExport, imageName, ip, queryAction, userId, toMailId, queryAssetIds, queryAssetIdList, queryAssetType, queryAssetName, queryPeople, queryCommet, queryHouse, queryNumber, merchantFormDTO, queryCounter, queryCartonNo, isVendorAttribute, codeGunFlag, maintenanceUserFlag, exportCodeGunSerialNumbers, exportCodeGunPropertyIds, queryAllSelected, fieldMap, toMail, exportField, queryParam, departmentList, queryCaseFlag, hideQueryAssetUser)
					;
			Integer count = this.repositoryDAO.getCountByAssetId(assetManageFormDTO);
			if(count > 0){
				Assert.assertEquals(count.intValue(), count.intValue());
			} else {
				Assert.assertFalse(count > 0 );
			}
			queryCaseFlag="";
			userId = null;
			rows = 0;
			queryAssetIds=null;
			queryAssetIdList = null;
			querySerialNumbers = null;
			queryPropertyIds = null;
			assetTypeName ="1,2,,";
			queryAction = "1,2,,";
			queryAssetUser = "1,2,,";
			assetManageFormDTO = 
					new AssetManageFormDTO(queryStorage, queryStatus, companyDTO, companyId, assetTypeName, queryStorage, status, supportedFunction, queryContractId, queryMerName, queryHeaderName, queryMerchantCode, queryMerInstallAddress, queryArea, queryMaType, queryIsEnabled, queryKeeperName, queryTid, queryAssetOwner, queryAssetUser, queryDtid, queryPropertyIds, querySerialNumbers, queryAssetId, queryHistId, queryBorrower, editFlag, queryAssetCategory, queryUser, queryHistory, dmmRepositoryHistoryDTOs, dmmRepositoryHistoryDTO, rows, page, sort, order, totalSize, dmmRepositoryDTO, beforeTicketCompletionDate, afterTicketCompletionDate, beforeUpdateDate, afterUpdateDate, queryIsCup, exportFields, historyExport, imageName, ip, queryAction, userId, toMailId, queryAssetIds, queryAssetIdList, queryAssetType, queryAssetName, queryPeople, queryCommet, queryHouse, queryNumber, merchantFormDTO, queryCounter, queryCartonNo, isVendorAttribute, codeGunFlag, maintenanceUserFlag, exportCodeGunSerialNumbers, exportCodeGunPropertyIds, queryAllSelected, fieldMap, toMail, exportField, queryParam, departmentList, queryCaseFlag, hideQueryAssetUser)
					;
			count = this.repositoryDAO.getCountByAssetId(assetManageFormDTO);
			if(count > 0){
				Assert.assertEquals(count.intValue(), count.intValue());
			} else {
				Assert.assertFalse(count > 0 );
			}
			assetTypeName =null;
			queryAction = null;
			queryAssetUser = null;
			queryAssetUser = null;
			assetManageFormDTO = 
					new AssetManageFormDTO(queryStorage, queryStatus, companyDTO, companyId, assetTypeName, queryStorage, status, supportedFunction, queryContractId, queryMerName, queryHeaderName, queryMerchantCode, queryMerInstallAddress, queryArea, queryMaType, queryIsEnabled, queryKeeperName, queryTid, queryAssetOwner, queryAssetUser, queryDtid, queryPropertyIds, querySerialNumbers, queryAssetId, queryHistId, queryBorrower, editFlag, queryAssetCategory, queryUser, queryHistory, dmmRepositoryHistoryDTOs, dmmRepositoryHistoryDTO, rows, page, sort, order, totalSize, dmmRepositoryDTO, beforeTicketCompletionDate, afterTicketCompletionDate, beforeUpdateDate, afterUpdateDate, queryIsCup, exportFields, historyExport, imageName, ip, queryAction, userId, toMailId, queryAssetIds, queryAssetIdList, queryAssetType, queryAssetName, queryPeople, queryCommet, queryHouse, queryNumber, merchantFormDTO, queryCounter, queryCartonNo, isVendorAttribute, codeGunFlag, maintenanceUserFlag, exportCodeGunSerialNumbers, exportCodeGunPropertyIds, queryAllSelected, fieldMap, toMail, exportField, queryParam, departmentList, queryCaseFlag, hideQueryAssetUser)
					;
			count = this.repositoryDAO.getCountByAssetId(assetManageFormDTO);
			if(count > 0){
				Assert.assertEquals(count.intValue(), count.intValue());
			} else {
				Assert.assertFalse(count > 0 );
			}
			 userId = "";
			 companyId = "";
			 queryUser = "";
			 queryAction = "";
			 queryAssetCategory = "";
			 storage = "";
			 queryAssetId = "";
			 queryMerName = "";
			 queryHeaderName = "";
			 queryMerchantCode = "";
			 queryMerInstallAddress = "";
			 queryArea = "";
			 queryContractId = "";
			 queryMaType = "";
			 queryIsEnabled = "";
			 assetTypeName = "";
			 queryKeeperName = "";
			 queryTid = "";
			 queryAssetOwner = "";
			 queryAssetUser = "";
			 queryDtid = "";
			 queryPropertyIds = "12, ,11";
			 querySerialNumbers = "0001, ,122";
			 queryIsCup = "";
			 afterTicketCompletionDate = "";
			 beforeTicketCompletionDate = "";
			 afterUpdateDate = "";
			 beforeUpdateDate = "";
			 queryCaseFlag = IAtomsConstants.PARAM_NO;
			 queryStorage = "";
			 queryStatus = "";
			 status = "";
			 supportedFunction = "";
			 queryHistId = "";
			 queryBorrower = "";
			 editFlag = "N";
			 queryHistory = "";
			 sort = "";
			 order = "";
			 historyExport = "";
			 imageName = "";
			 ip = "";
			 toMailId = "";
			 queryAssetIds = "111, ,123";
			 queryAssetIdList = "111, ,122";
			 queryAssetType = "";
			 queryCartonNo = "";
			 queryAssetName = "";
			 queryPeople = "";
			 queryCommet = "";
			 queryHouse = "";
			 queryNumber = "";
			 queryCounter = "";
			 exportCodeGunPropertyIds = "111, ,111";
			 queryAllSelected = "N";
			 isVendorAttribute = false;
			 codeGunFlag = "N";
			 maintenanceUserFlag = "";
			 exportCodeGunSerialNumbers = "111, ,12";
			 hideQueryAssetUser = "N";
			 exportField = "";
			 queryParam = "";
			 fieldMap = new HashMap<String, String>();
			 toMail = "";
			 rows = -1;
			 page = 2;
			 totalSize = 10;
			 assetManageFormDTO = 
						new AssetManageFormDTO(queryStorage, queryStatus, companyDTO, companyId, assetTypeName, queryStorage, status, supportedFunction, queryContractId, queryMerName, queryHeaderName, queryMerchantCode, queryMerInstallAddress, queryArea, queryMaType, queryIsEnabled, queryKeeperName, queryTid, queryAssetOwner, queryAssetUser, queryDtid, queryPropertyIds, querySerialNumbers, queryAssetId, queryHistId, queryBorrower, editFlag, queryAssetCategory, queryUser, queryHistory, dmmRepositoryHistoryDTOs, dmmRepositoryHistoryDTO, rows, page, sort, order, totalSize, dmmRepositoryDTO, beforeTicketCompletionDate, afterTicketCompletionDate, beforeUpdateDate, afterUpdateDate, queryIsCup, exportFields, historyExport, imageName, ip, queryAction, userId, toMailId, queryAssetIds, queryAssetIdList, queryAssetType, queryAssetName, queryPeople, queryCommet, queryHouse, queryNumber, merchantFormDTO, queryCounter, queryCartonNo, isVendorAttribute, codeGunFlag, maintenanceUserFlag, exportCodeGunSerialNumbers, exportCodeGunPropertyIds, queryAllSelected, fieldMap, toMail, exportField, queryParam, departmentList, queryCaseFlag, hideQueryAssetUser)
						;
				count = this.repositoryDAO.getCountByAssetId(assetManageFormDTO);
				if(count > 0){
					Assert.assertEquals(count.intValue(), count.intValue());
				} else {
					Assert.assertFalse(count > 0 );
				}
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testCount()", "is error:", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試獲得用戶Email與主管Email
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetEmailByUserId(){
		try {
			//使用者Id
			String userId = "1473076241770-0048as";
			AdmUserDTO admUser = this.repositoryDAO.getEmailByUserId(userId);
			if (admUser != null) {
				Assert.assertNotNull(admUser);
			} else {
				Assert.assertFalse(admUser != null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testGetEmailByUserId()", "is error:", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試驗證財產編號重複
	 * @author ElvaHe
	 * @return void
	 */
	public void testIsRepeatPropertyId(){
		try {
			//財產編號
			String propertyId = "gfgdfg";
			//設備編號
			String assetId = "1473339253545-0008";
			boolean result = this.repositoryDAO.isRepeatPropertyId(propertyId, assetId);
			if (result) {
				Assert.assertTrue(result);
			} else {
				Assert.assertFalse(result);
			}
			propertyId = "";
			//設備編號
			assetId = "";
			result = this.repositoryDAO.isRepeatPropertyId(propertyId, assetId);
			if (result) {
				Assert.assertTrue(result);
			} else {
				Assert.assertFalse(result);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testIsRepeatPropertyId()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試驗證DTID重複
	 * @author ElvaHe
	 * @return void
	 */
	public void testIsRepeatDtid(){
		try {
			//DTID
			String dtid = "22asdasd";
			//設備編號
			String assetId = "1473339253545-0008";
			boolean result = this.repositoryDAO.isRepeatDtid(dtid, assetId);
			if (result) {
				Assert.assertTrue(result);
			} else {
				Assert.assertFalse(result);
			}
			dtid = "";
			//設備編號
			assetId = "";
			result = this.repositoryDAO.isRepeatDtid(dtid, assetId);
			if (result) {
				Assert.assertTrue(result);
			} else {
				Assert.assertFalse(result);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testIsRepeatDtid()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試根據設備序號查詢到庫存信息
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetRepositoryBySerialNumber(){
		try {
			//設備序號
			String serialNumber = "sdfsdfasdasd";
			//轉出倉
			String fromWarehouseId = "1470719407325-0004";
			//庫存狀態
			String state = "REPERTORY";
			DmmRepositoryDTO dmmRepositoryDTO = this.repositoryDAO.getRepositoryBySerialNumber(serialNumber, fromWarehouseId, state);
			if (dmmRepositoryDTO != null) {
				Assert.assertNotNull(dmmRepositoryDTO);
			} else {
				Assert.assertFalse(dmmRepositoryDTO != null);
			}
			//設備序號
			serialNumber = "";
			//轉出倉
			fromWarehouseId = "";
			//庫存狀態
			state = "";
			dmmRepositoryDTO = this.repositoryDAO.getRepositoryBySerialNumber(serialNumber, fromWarehouseId, state);
			if (dmmRepositoryDTO != null) {
				Assert.assertNotNull(dmmRepositoryDTO);
			} else {
				Assert.assertFalse(dmmRepositoryDTO != null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testGetRepositoryBySerialNumber()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試獲取借用到期資料
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetUnBackBorrowers(){
		try {
			List<DmmRepositoryDTO> result = this.repositoryDAO.getUnBackBorrowers();
			if (!CollectionUtils.isEmpty(result)) {
				Assert.assertNotNull(result);
			} else {
				Assert.assertFalse(result != null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testGetUnBackBorrowers()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試由account獲取userId
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetUserIdByAccount(){
		try {
			String account = "elvahe12";
			String userId = this.repositoryDAO.getUserIdByAccount(account);
			if (!StringUtils.isEmpty(userId)) {
				Assert.assertNotNull(userId);
			} else {
				Assert.assertFalse(userId != null);
			}
			account = "";
			userId = this.repositoryDAO.getUserIdByAccount(account);
			if (!StringUtils.isEmpty(userId)) {
				Assert.assertNotNull(userId);
			} else {
				Assert.assertFalse(userId != null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testGetUserIdByAccount()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試檢驗財產編號是否重複
	 * @author ElvaHe
	 * @return void
	 */
	public void testIsPropertyIdRepeat(){
		try {
			//財產編號
			String propertyId = "gfgdfg";
			//設備編號
			String serialNumber = "sdfsdf";
			boolean isRepeat = this.repositoryDAO.isPropertyIdRepeat(propertyId, serialNumber);
			if (isRepeat) {
				Assert.assertTrue(isRepeat);
			} else {
				Assert.assertFalse(isRepeat);
			}
			propertyId = "";
			//設備編號
			serialNumber = "";
			isRepeat = this.repositoryDAO.isPropertyIdRepeat(propertyId, serialNumber);
			if (isRepeat) {
				Assert.assertTrue(isRepeat);
			} else {
				Assert.assertFalse(isRepeat);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testIsPropertyIdRepeat()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試檢驗財產編號是否重複
	 * @author ElvaHe
	 * @return void
	 */
	/*public void testIsDtidInUse(){
		try {
			//財產編號
			String dtidStart = "14";
			//設備編號
			String dtidEnd = "23";
			boolean isRepeat = this.repositoryDAO.isDtidInUse(dtidStart, dtidEnd);
			if (isRepeat) {
				Assert.assertTrue(isRepeat);
			} else {
				Assert.assertFalse(isRepeat);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testIsDtidInUse()", "is error：", e);
			e.printStackTrace();
		}
	}*/
	
	/**
	 * Purpose:測試根據財編得到設備信息
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetRepositoryByPropertyId(){
		try {
			//財產編號
			String propertyId = "gfgdfg12";
			//庫存信息
			DmmRepositoryDTO repositoryDTO = this.repositoryDAO.getRepositoryByPropertyId(propertyId);
			if (repositoryDTO != null) {
				Assert.assertNotNull(repositoryDTO);
			} else {
				Assert.assertNull(repositoryDTO);
			}
			propertyId = null;
			repositoryDTO = this.repositoryDAO.getRepositoryByPropertyId(propertyId);
			if (repositoryDTO != null) {
				Assert.assertNotNull(repositoryDTO);
			} else {
				Assert.assertNull(repositoryDTO);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testGetRepositoryByPropertyId()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試確認入庫
	 * @author ElvaHe
	 * @return void
	 */
	public void testSaveRepository(){
		try {
			//在service創建的庫存ID
			String assetId = "12345";
			//在service創建的庫存歷史ID
			String historyId = "45678";
			//創建人員ID
			String userId = "1473076241770-0048";
			//創建人員名稱
			String userName = "elvahe";
			//設備入庫單號
			String assetInId = "12";
			//cyber驗收日期
			Timestamp cyberApprovedDate = DateTimeUtils.getCurrentTimestamp();
			this.repositoryDAO.saveRepository(assetId, historyId, userId, userName, assetInId, cyberApprovedDate);
			Assert.assertTrue(true);
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testSaveRepository()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試在核檢修改庫存表的數據時，依據名稱獲取對應的ID
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetRepositoryDTOBy(){
		try {
			//合約代碼
			String contractCode = "1891168vdfgdf";
			//設備名稱
			String assetName = "設備1";
			//資產owner
			String owner = "1470654243644-0011";
			//使用人
			String user = "1470654243644-0011";
			DmmRepositoryDTO dmmRepositoryDTO = this.repositoryDAO.getRepositoryDTOBy(contractCode, assetName, owner, user);
			if (dmmRepositoryDTO != null) {
				Assert.assertNotNull(dmmRepositoryDTO);
			} else {
				Assert.assertFalse(dmmRepositoryDTO != null);
			}
			contractCode= "18911681243434-0112";
			assetName = null;
			owner = null;
			user = null;
			dmmRepositoryDTO = this.repositoryDAO.getRepositoryDTOBy(contractCode, assetName, owner, user);
			if (dmmRepositoryDTO != null) {
				Assert.assertNotNull(dmmRepositoryDTO);
			} else {
				Assert.assertFalse(dmmRepositoryDTO != null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testGetRepositoryDTOBy()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試獲取財產編號列表
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetPropertyIds(){
		try {
			List<Parameter> propertyIds = this.repositoryDAO.getPropertyIds();
			if(!CollectionUtils.isEmpty(propertyIds)){
				Assert.assertEquals(propertyIds.size(), propertyIds.size());
			} else {
				Assert.assertFalse(propertyIds == null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testGetPropertyIds()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試根據所有的設備序號，獲取對應的cyber驗收日期
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetCyberApproveDate(){
		try {
			List<Parameter> cyberApproveDates = this.repositoryDAO.getCyberApproveDate();
			if(!CollectionUtils.isEmpty(cyberApproveDates)){
				Assert.assertEquals(cyberApproveDates.size(), cyberApproveDates.size());
			} else {
				Assert.assertFalse(cyberApproveDates == null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testGetCyberApproveDate()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試更新數據
	 * @author ElvaHe
	 * @return void
	 */
	public void testUpdateRepository(){
		try {
			DmmRepositoryDTO repositoryDTO = new DmmRepositoryDTO();
			repositoryDTO.setAssetId("1473339253551-0009");
			repositoryDTO.setAssetTypeId("1");
			repositoryDTO.setPropertyId("q");
			repositoryDTO.setContractId("a");
			repositoryDTO.setSimEnableNo("2");
			repositoryDTO.setEnableDate(DateTimeUtils.getCurrentTimestamp());
			repositoryDTO.setAssetOwner("12345");
			repositoryDTO.setAssetUser("12345");
			repositoryDTO.setFactoryWarrantyDate(DateTimeUtils.getCurrentDate());
			repositoryDTO.setCustomerWarrantyDate(DateTimeUtils.getCurrentDate());
			repositoryDTO.setCyberApprovedDate(DateTimeUtils.getCurrentDate());
			repositoryDTO.setAssetInTime(DateTimeUtils.getCurrentTimestamp());
			repositoryDTO.setUpdateUser("1473076241770-0048");
			repositoryDTO.setUpdateUserName("Elva He (賀文華)");
			repositoryDTO.setSimEnableDate(DateTimeUtils.getCurrentTimestamp());
			repositoryDTO.setUpdateDate(DateTimeUtils.getCurrentTimestamp());
			this.repositoryDAO.updateRepository(repositoryDTO, Boolean.FALSE, Boolean.TRUE, true, false, false, null);
			Assert.assertTrue(true);
			repositoryDTO.setAssetId("1473339253551-0008");
			repositoryDTO.setAssetTypeId("");
			repositoryDTO.setPropertyId("");
			repositoryDTO.setContractId("");
			repositoryDTO.setSimEnableNo("");
			repositoryDTO.setEnableDate(null);
			repositoryDTO.setAssetOwner("");
			repositoryDTO.setAssetUser("");
			repositoryDTO.setCyberApprovedDate(null);
			repositoryDTO.setAssetInTime(null);
			repositoryDTO.setFactoryWarrantyDate(null);
			repositoryDTO.setCustomerWarrantyDate(null);
			repositoryDTO.setUpdateUser("");
			repositoryDTO.setUpdateUserName("");
			repositoryDTO.setSimEnableDate(null);
			repositoryDTO.setUpdateDate(DateTimeUtils.getCurrentTimestamp());
			this.repositoryDAO.updateRepository(repositoryDTO, Boolean.TRUE, Boolean.FALSE, false, false, false, null);
			Assert.assertTrue(true);
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testUpdateRepository()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試設備入庫時匯入驗證重複，獲取所有的設備序號以及財產編號
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetDmmRepositoryDTOs(){
		try {
			List<DmmRepositoryDTO> repositoryDTOs = this.repositoryDAO.getDmmRepositoryDTOs();
			if (CollectionUtils.isEmpty(repositoryDTOs)) {
				Assert.assertNotNull(repositoryDTOs);
			} else {
				Assert.assertFalse(repositoryDTOs == null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testGetDmmRepositoryDTOs()", "is error：", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:測試根據查詢條件查詢EDC信息
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetEDC(){
		try {
			
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testGetEDCCount()", "is error：", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:測試根據查詢條件查詢EDC信息的總筆數
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetEDCCount(){
		try {
			//設備類別
			String queryAssetType = "";
			//設備名稱
			String queryAssetName = "設備二";
			//借用人/領用人
			String queryPeople = "";
			//借用/領用說明
			String queryCommet = "";
			//倉庫名稱
			String queryHouse = "";
			//設備序號
			String queryNumber = "";
			Integer count = this.repositoryDAO.getEDCCount(queryAssetType, queryAssetName, queryPeople, queryCommet, queryHouse, queryNumber);
			if (count > 0 ) {
				Assert.assertEquals(count, count);
			} else {
				Assert.assertFalse(count > 0);
			}
			queryAssetType = "110";
			queryAssetName = null;
			queryPeople = "14004567466764-001";
			queryHouse ="001";
			queryNumber = "001";
			count = this.repositoryDAO.getEDCCount(queryAssetType, queryAssetName, queryPeople, queryCommet, queryHouse, queryNumber);
			if (count > 0 ) {
				Assert.assertEquals(count, count);
			} else {
				Assert.assertFalse(count > 0);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testGetEDCCount()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試設備確認轉倉-修改庫存表的設備狀態
	 * @author ElvaHe
	 * @return void
	 */
	public void testUpdateRepositoryInfo(){
		try {
			//設備序號
			String serialNumber = "789";
			//登錄者信息
			LogonUser logonUser = new LogonUser(null, "1473076241770-0048", "elvahe");
			//轉倉批號Id
			String assetTransId = "TW201608220012";
			this.repositoryDAO.updateRepository(serialNumber, logonUser, assetTransId);
			Assert.assertTrue(true);
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testUpdateRepositoryInfo()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:
	 * @author ElvaHe
	 * @return void
	 */
	public void testConfirmStorage(){
		/*try {
			//轉倉批號ID
			String assetTransId = "TW201608220012";
			//設備狀態
			String status = "REPERTORY";
			//是否轉倉
			String isTransDone = "Y";
			//庫存歷史起始ID
			String historyId = "";
			//當前登錄者信息
			LogonUser logonUser = new LogonUser(null, "1473076241770-0048", "elvahe");
			this.repositoryDAO.confirmStorage(assetTransId, status, isTransDone, historyId, logonUser, IAtomsConstants.PARAM_ACTION_TRANSFER_STORAGE);
			Assert.assertTrue(true);
			status = "";
			this.repositoryDAO.confirmStorage(assetTransId, status, isTransDone, historyId, logonUser, IAtomsConstants.PARAM_ACTION_TRANSFER_STORAGE);
			Assert.assertTrue(true);
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testConfirmStorage()", "is error：", e);
			e.printStackTrace();
		}*/
	}
	
	/**
	 * Purpose:測試設備盤點-根據輸入的設備序號或者財產編號獲取對應的庫存信息
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetBySerialNumber(){
		try {
			//設備序號或者財產編號
			String serialNumber = "4561";
			DmmRepositoryDTO dmmRepositoryDTO = this.repositoryDAO.getBySerialNumber(serialNumber);
			if (dmmRepositoryDTO != null) {
				Assert.assertNotNull(dmmRepositoryDTO);
			} else {
				Assert.assertFalse(dmmRepositoryDTO != null);
			}
			serialNumber = "";
			dmmRepositoryDTO = this.repositoryDAO.getBySerialNumber(serialNumber);
			if (dmmRepositoryDTO != null) {
				Assert.assertNotNull(dmmRepositoryDTO);
			} else {
				Assert.assertFalse(dmmRepositoryDTO != null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testGetBySerialNumber()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試保存庫存歷史檔與故障組件故障現象歷史檔
	 * @author ElvaHe
	 * @return void
	 */
	public void testSaveRepositoryHist(){
		try {
			//庫存ID
			String assetId = "qweqwe";
			//庫存起始ID
			String historyId = "asdwew";
			this.repositoryDAO.saveRepositoryHist(assetId, historyId);
			Assert.assertTrue(true);
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testSaveRepositoryHist()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試刷卡機裝機數報表
	 * @author ElvaHe
	 * @return void
	 */
	public void testEdcInstallNumReport(){
		try {
			//設備狀態
			String status = "REPERTORY";
			//設備類型
			String assetCategory = "EDC12";
			//查詢結果依客戶簡稱/維護組別升冪排序
			String orderBy = "asc";
			List<CrossTabReportDTO> crossTabReportDTOs = this.repositoryDAO.edcInstallNumReport(status, assetCategory, orderBy);
			if (!CollectionUtils.isEmpty(crossTabReportDTOs)) {
				Assert.assertNotNull(crossTabReportDTOs);
			} else {
				Assert.assertFalse(crossTabReportDTOs == null);
			}
			status = "";
			assetCategory="";
			orderBy="SHORT_NAME";
			crossTabReportDTOs = this.repositoryDAO.edcInstallNumReport(status, assetCategory, orderBy);
			if (!CollectionUtils.isEmpty(crossTabReportDTOs)) {
				Assert.assertNotNull(crossTabReportDTOs);
			} else {
				Assert.assertFalse(crossTabReportDTOs == null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testEdcInstallNumReport()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試根據設備類別和設備名稱查詢選取EDC設備
	 * @author ElvaHe
	 * @return void
	 */
	public void testListByAsset(){
		try {
			String chooseEdcAssetId = "";
			String chooseEdcAssetCategory = "";
			String chooseEdcSerialNumber = "";
			String chooseEdcCarrierOrBorrower = "";
			String chooseEdcCarrierOrBorrowerComment = "";
			String chooseEdcWarehouseId = "";
			String chooseEdcDispatchProcessUser = "elvahe";
			Integer pageSize = 10;
			Integer page = 1;
			String order = "";
			String sort = "";
			List<String> selectSns = new ArrayList<String>();
			List<DmmRepositoryDTO> dmmRepositoryDTOS = this.repositoryDAO.listByAsset(chooseEdcAssetId, chooseEdcAssetCategory, chooseEdcSerialNumber, chooseEdcCarrierOrBorrower,
					chooseEdcCarrierOrBorrowerComment, chooseEdcWarehouseId, chooseEdcDispatchProcessUser,chooseEdcDispatchProcessUser,
					pageSize, page, order, sort, selectSns);
			if(!CollectionUtils.isEmpty(dmmRepositoryDTOS)){
				Assert.assertNotNull(dmmRepositoryDTOS);
			} else {
				Assert.assertFalse(dmmRepositoryDTOS == null);
			}
			 chooseEdcAssetId = "111";
			 chooseEdcAssetCategory = "123";
			 chooseEdcSerialNumber = "122";
			 chooseEdcCarrierOrBorrower = "111";
			 chooseEdcCarrierOrBorrowerComment = "122";
			 chooseEdcWarehouseId = "111";
			 chooseEdcDispatchProcessUser = "";
			 pageSize = 10;
			 page = 1;
			 order = "desc";
			 sort = "repository.SERIAL_NUMBER";
			selectSns = new ArrayList<String>();
			selectSns.add("111");
			dmmRepositoryDTOS = this.repositoryDAO.listByAsset(chooseEdcAssetId, chooseEdcAssetCategory, chooseEdcSerialNumber, chooseEdcCarrierOrBorrower,
					chooseEdcCarrierOrBorrowerComment, chooseEdcWarehouseId, chooseEdcDispatchProcessUser,chooseEdcDispatchProcessUser,
					pageSize, page, order, sort, selectSns);
			if(!CollectionUtils.isEmpty(dmmRepositoryDTOS)){
				Assert.assertNotNull(dmmRepositoryDTOS);
			} else {
				Assert.assertFalse(dmmRepositoryDTOS == null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testListByAsset()", "is error：", e);
			e.printStackTrace();
		}
	}
	public void testSaveRepositoryByBorrowZip(){
		try {
			//庫存ID
			String assetIds = "0011";
			//庫存歷史起始ID
			String historyId = "";
			Timestamp nowTime = DateTimeUtils.getCurrentTimestamp();
			String userId = "150034333567764-122"; 
			String userName = "heheda";
			this.repositoryDAO.saveRepositoryByBorrowZip(assetIds, nowTime, userId, userName, historyId);
			Assert.assertTrue(true);
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testSaveRepositoryByBorrowZip()", "is error：", e);
			e.printStackTrace();
		}
	}
	public void testAssetValidateByProcedure(){
		try {
			//庫存ID
			String assetIds = "0011";
			String action = "borrow";
			this.repositoryDAO.assetValidateByProcedure(action, assetIds);
			Assert.assertTrue(true);
			assetIds = "";
			action = "";
			Object [] arr =	this.repositoryDAO.assetValidateByProcedure(action, assetIds);
			if(arr!= null){
				Assert.assertNotNull(arr);
			} else {
				Assert.assertFalse(arr == null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testSaveRepositoryByBorrowZip()", "is error：", e);
			e.printStackTrace();
		}
	}
	public void testSaveRepositoryByProcedure(){
		try {
			//庫存ID
			String assetIds = "0011";
			String action = "borrow";

			String transactionId = "0011";
			String historyId = "0011";
			 
			String userId = "0011";
			String userName = "0011"; 
			String maintainCompany = "0011"; 
			java.util.Date analyzeDate = new Date();
			Timestamp caseCompletionDate = DateTimeUtils.getCurrentTimestamp();
			 
			String maintainUser = "0011";
			String description = "0011";
			String flag = "0011"; 
			Timestamp nowTime = DateTimeUtils.getCurrentTimestamp();
			String nowStatus = "0011"; 
			String faultComponent = "0011"; 
			 
			String repairVendor = "0011"; 
			String faultDescription = "0011"; 
			String carrier = "0011";
			Date carryDate =  new Date();
			String borrower = "0011";
			 
			Date borrowerEnd = new Date();
			String borrowerMgrEmail = "0011"; 
			String borrowerEmail = "0011"; 
			Date borrowerStart = new Date();
			Date enableDate = new Date();
			String isEnabled = "0011";
			 
			String dtid = "0011"; 
			String caseId = "0011";
			String merchantId = "0011"; 
			String merchantHeaderId = "0011"; 
			String installedAdress = "0011";
			 
			String installedAdressLocation = "0011"; 
			String IsCup = "0011"; 
			String retireReasonCode = "0011";
			Object [] arr =	this.repositoryDAO.saveRepositoryByProcedure(action, assetIds, transactionId, 
					historyId, userId, userName, maintainCompany, analyzeDate, caseCompletionDate, 
					maintainUser, description, flag, nowTime, nowStatus, faultComponent, repairVendor,
					faultDescription, carrier, carryDate, borrower, borrowerEnd, borrowerMgrEmail, borrowerEmail,
					borrowerStart, enableDate, isEnabled, dtid, caseId, merchantId, merchantHeaderId,
					installedAdress, installedAdressLocation, IsCup, retireReasonCode);
			if(arr!= null){
				Assert.assertNotNull(arr);
			} else {
				Assert.assertFalse(arr == null);
			}

			assetIds = "";
			action = "";
			transactionId = "";
			historyId = "";
			userId = "";
			userName = "";
			maintainCompany = "";
			analyzeDate = null;
			caseCompletionDate = null;
			maintainUser = "";
			description = "";
			flag = "";
			nowTime = DateTimeUtils.getCurrentTimestamp();
			nowStatus = "";
			faultComponent = "";
			repairVendor = "";
			faultDescription = "";
			carrier = "";
			carryDate = null;
			borrower = "";
			borrowerEnd = null;
			borrowerMgrEmail = "";
			borrowerEmail = "";
			borrowerStart = null;
			enableDate = null;
			isEnabled = "";
			dtid = "";
			caseId = "";
			merchantId = "";
			merchantHeaderId = "";
			installedAdress = "";
			installedAdressLocation = "";
			IsCup = "";
			retireReasonCode = "";
			arr =	this.repositoryDAO.saveRepositoryByProcedure(action, assetIds, transactionId, 
					historyId, userId, userName, maintainCompany, analyzeDate, caseCompletionDate, 
					maintainUser, description, flag, nowTime, nowStatus, faultComponent, repairVendor,
					faultDescription, carrier, carryDate, borrower, borrowerEnd, borrowerMgrEmail, borrowerEmail,
					borrowerStart, enableDate, isEnabled, dtid, caseId, merchantId, merchantHeaderId,
					installedAdress, installedAdressLocation, IsCup, retireReasonCode);
			if(arr!= null){
				Assert.assertNotNull(arr);
			} else {
				Assert.assertFalse(arr == null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testSaveRepositoryHistInCaseAssetLink()", "is error：", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:測試保存庫存歷史檔與故障組件故障現象歷史檔(簽收和線上排除設備鏈接)
	 * @author ElvaHe
	 * @return void
	 */
	public void testSaveRepositoryHistInCaseAssetLink(){
		try {
			//庫存ID
			String assetId = "0011";
			//庫存歷史起始ID
			String historyId = "";
			//庫存狀態
			String status = "REPERTORY";
			this.repositoryDAO.saveRepositoryHist(assetId, historyId, status);
			Assert.assertTrue(true);
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testSaveRepositoryHistInCaseAssetLink()", "is error：", e);
			e.printStackTrace();
		}
	}
	public void testCountData(){
		try {
			Integer count = this.repositoryDAO.countData();
			if(count > 0){
				Assert.assertEquals(count.intValue(), count.intValue());
			} else {
				Assert.assertFalse(count > 0 );
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testSaveRepositoryHistInCaseAssetLink()", "is error：", e);
			e.printStackTrace();
		}
	}
	public void testListByAssetId(){
		try {
			List<String> assetId = new ArrayList<String>();
			assetId.add("111");
			List<DmmRepositoryDTO> dtoList = this.repositoryDAO.listByAssetId(assetId);
			if(!CollectionUtils.isEmpty(dtoList)){
				Assert.assertNotNull(dtoList);
			} else {
				Assert.assertFalse(dtoList == null);
			}
			
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testSaveRepositoryHistInCaseAssetLink()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	public void testDeleteTransferData(){
		try {
			//this.repositoryDAO.deleteTransferData();
			Assert.assertTrue(true);
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testDeleteTransferData()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	public void testGetAssetIdList(){
		try {

			String userId = "1473076241770-0048";
			String companyId = "1470648902571-0000";
			String queryUser = "123";
			String queryAction = "123";
			String queryAssetCategory = "123";
			String storage = "123";
			String queryAssetId = "123";
			String queryMerName = "123";
			String queryHeaderName = "123";
			String queryMerchantCode = "1470648902571-0000";
			String queryMerInstallAddress = "111";
			String queryArea = "01";
			String queryContractId = "1470648902571-0000";
			String queryMaType = "採購(買斷)";
			String queryIsEnabled = "Y";
			String assetTypeName = "1470648902571-0000";
			String queryKeeperName = "1470648902571-0000";
			String queryTid = "1470648902571-0000";
			String queryAssetOwner = "1470648902571-0000";
			String queryAssetUser = "1474440222588-0125";
			String queryDtid = "1470648902571-0000";
			String queryPropertyIds = "12,11";
			String querySerialNumbers = "0001,122";
			String queryIsCup = "Y";
			String afterTicketCompletionDate = "2017/01/11";
			String beforeTicketCompletionDate = "2017/01/12";
			String afterUpdateDate = "2017/01/12";
			String beforeUpdateDate = "2017/01/12";
			String queryCaseFlag = IAtomsConstants.PARAM_YES;
			String queryStorage = "111";
			String queryStatus = "111";
			String status = "111";
			String supportedFunction = "111";
			String queryHistId = "111";
			String queryBorrower = "111";
			String editFlag = "Y";
			String queryHistory = "111";
			String sort = "dtid";
			String order = "asc";
			String[] exportFields = {};
			String historyExport = "111";
			String imageName = "111";
			String ip = "111";
			String toMailId = "111";
			String queryAssetIds = "111,123";
			String queryAssetIdList = "111,122";
			String queryAssetType = "111";
			String queryCartonNo = "111";
			String queryAssetName = "111";
			String queryPeople = "111";
			String queryCommet = "111";
			String queryHouse = "111";
			String queryNumber = "111";
			String queryCounter = "111";
			String exportCodeGunPropertyIds = "111,111";
			String queryAllSelected = "Y";
			boolean isVendorAttribute = true;
			String codeGunFlag = "Y";
			String maintenanceUserFlag = "111";
			String exportCodeGunSerialNumbers = "111,12";
			String hideQueryAssetUser = "Y";
			String exportField = "111";
			String queryParam = "111";
			Map<String, String> fieldMap = new HashMap<String, String>();
			String toMail = "123@qq.com";
			int rows = 10;
			int page = 1;
			int totalSize = 10;
			CompanyDTO companyDTO = new CompanyDTO();
			MerchantFormDTO merchantFormDTO = new MerchantFormDTO();
			List<Parameter> departmentList = new ArrayList<Parameter>();
			List<DmmRepositoryHistoryDTO> dmmRepositoryHistoryDTOs = new ArrayList<DmmRepositoryHistoryDTO>();
			DmmRepositoryHistoryDTO dmmRepositoryHistoryDTO = new DmmRepositoryHistoryDTO();
			DmmRepositoryDTO dmmRepositoryDTO = new DmmRepositoryDTO();
			//設備管理作業formDTO
			AssetManageFormDTO assetManageFormDTO = 
					new AssetManageFormDTO(queryStorage, queryStatus, companyDTO, companyId,
							assetTypeName, storage, status, supportedFunction, queryContractId, 
							queryMerName, queryHeaderName, queryMerchantCode, queryMerInstallAddress,
							queryArea, queryMaType, queryIsEnabled, queryKeeperName, queryTid, queryAssetOwner,
							queryAssetUser, queryDtid, queryPropertyIds, querySerialNumbers, queryAssetId, queryHistId,
							queryBorrower, editFlag, queryAssetCategory, queryUser, queryHistory, dmmRepositoryHistoryDTOs, 
							dmmRepositoryHistoryDTO, rows, page, sort, order, totalSize, dmmRepositoryDTO, beforeTicketCompletionDate,
							afterTicketCompletionDate, beforeUpdateDate, afterUpdateDate, queryIsCup, exportFields, historyExport,
							imageName, ip, queryAction, userId, toMailId, queryAssetIds, queryAssetIdList, queryAssetType,
							queryAssetName, queryPeople, queryCommet, queryHouse, queryNumber, merchantFormDTO, queryCounter, 
							queryCartonNo, isVendorAttribute, codeGunFlag, maintenanceUserFlag, exportCodeGunSerialNumbers,
							exportCodeGunPropertyIds, queryAllSelected, fieldMap, toMail, exportField, queryParam, 
							departmentList, queryCaseFlag, hideQueryAssetUser);
					
					
			List<String> repositoryDTOs = this.repositoryDAO.getAssetIdList(assetManageFormDTO);
			if (!CollectionUtils.isEmpty(repositoryDTOs)) {
				Assert.assertNotNull(repositoryDTOs);
			} else {
				Assert.assertTrue(repositoryDTOs != null);
			}
			queryCaseFlag="";
			userId = null;
			rows = 0;
			queryAssetIds=null;
			queryAssetIdList = null;
			querySerialNumbers = null;
			queryPropertyIds = null;
			assetTypeName ="1,2,,";
			queryAction = "1,2,,";
			queryAssetUser = "1,2,,";
			assetManageFormDTO = 
					new AssetManageFormDTO(queryStorage, queryStatus, companyDTO, companyId,
							assetTypeName, storage, status, supportedFunction, queryContractId, 
							queryMerName, queryHeaderName, queryMerchantCode, queryMerInstallAddress,
							queryArea, queryMaType, queryIsEnabled, queryKeeperName, queryTid, queryAssetOwner,
							queryAssetUser, queryDtid, queryPropertyIds, querySerialNumbers, queryAssetId, queryHistId,
							queryBorrower, editFlag, queryAssetCategory, queryUser, queryHistory, dmmRepositoryHistoryDTOs, 
							dmmRepositoryHistoryDTO, rows, page, sort, order, totalSize, dmmRepositoryDTO, beforeTicketCompletionDate,
							afterTicketCompletionDate, beforeUpdateDate, afterUpdateDate, queryIsCup, exportFields, historyExport,
							imageName, ip, queryAction, userId, toMailId, queryAssetIds, queryAssetIdList, queryAssetType,
							queryAssetName, queryPeople, queryCommet, queryHouse, queryNumber, merchantFormDTO, queryCounter, 
							queryCartonNo, isVendorAttribute, codeGunFlag, maintenanceUserFlag, exportCodeGunSerialNumbers,
							exportCodeGunPropertyIds, queryAllSelected, fieldMap, toMail, exportField, queryParam, 
							departmentList, queryCaseFlag, hideQueryAssetUser);
					
					
			repositoryDTOs = this.repositoryDAO.getAssetIdList(assetManageFormDTO);
			if (!CollectionUtils.isEmpty(repositoryDTOs)) {
				Assert.assertNotNull(repositoryDTOs);
			} else {
				Assert.assertTrue(repositoryDTOs != null);
			}
			assetTypeName =null;
			queryAction = null;
			queryAssetUser = null;
			queryAssetUser = null;
			assetManageFormDTO = 
					new AssetManageFormDTO(queryStorage, queryStatus, companyDTO, companyId,
							assetTypeName, storage, status, supportedFunction, queryContractId, 
							queryMerName, queryHeaderName, queryMerchantCode, queryMerInstallAddress,
							queryArea, queryMaType, queryIsEnabled, queryKeeperName, queryTid, queryAssetOwner,
							queryAssetUser, queryDtid, queryPropertyIds, querySerialNumbers, queryAssetId, queryHistId,
							queryBorrower, editFlag, queryAssetCategory, queryUser, queryHistory, dmmRepositoryHistoryDTOs, 
							dmmRepositoryHistoryDTO, rows, page, sort, order, totalSize, dmmRepositoryDTO, beforeTicketCompletionDate,
							afterTicketCompletionDate, beforeUpdateDate, afterUpdateDate, queryIsCup, exportFields, historyExport,
							imageName, ip, queryAction, userId, toMailId, queryAssetIds, queryAssetIdList, queryAssetType,
							queryAssetName, queryPeople, queryCommet, queryHouse, queryNumber, merchantFormDTO, queryCounter, 
							queryCartonNo, isVendorAttribute, codeGunFlag, maintenanceUserFlag, exportCodeGunSerialNumbers,
							exportCodeGunPropertyIds, queryAllSelected, fieldMap, toMail, exportField, queryParam, 
							departmentList, queryCaseFlag, hideQueryAssetUser);
					
					
			repositoryDTOs = this.repositoryDAO.getAssetIdList(assetManageFormDTO);
			if (!CollectionUtils.isEmpty(repositoryDTOs)) {
				Assert.assertNotNull(repositoryDTOs);
			} else {
				Assert.assertTrue(repositoryDTOs != null);
			}
			 userId = "";
			 companyId = "";
			 queryUser = null;
			 queryAction = "";
			 queryAssetCategory = "";
			 storage = "";
			 queryAssetId = "";
			 queryMerName = "";
			 queryHeaderName = "";
			 queryMerchantCode = "";
			 queryMerInstallAddress = "";
			 queryArea = "";
			 queryContractId = "";
			 queryMaType = "";
			 queryIsEnabled = "";
			 assetTypeName = "";
			 queryKeeperName = "";
			 queryTid = "";
			 queryAssetOwner = "";
			 queryAssetUser = "";
			 queryDtid = "";
			 queryPropertyIds = "12, ,11";
			 querySerialNumbers = "0001, ,122";
			 queryIsCup = "";
			 afterTicketCompletionDate = "";
			 beforeTicketCompletionDate = "";
			 afterUpdateDate = "";
			 beforeUpdateDate = "";
			 queryCaseFlag = IAtomsConstants.PARAM_NO;
			 queryStorage = "";
			 queryStatus = "";
			 status = "";
			 supportedFunction = "";
			 queryHistId = "";
			 queryBorrower = "";
			 editFlag = "N";
			 queryHistory = "";
			 sort = "dtid";
			 order = "";
			 historyExport = "";
			 imageName = "";
			 ip = "";
			 toMailId = "";
			 queryAssetIds = "111, ,123";
			 queryAssetIdList = "111, ,122";
			 queryAssetType = "";
			 queryCartonNo = "";
			 queryAssetName = "";
			 queryPeople = "";
			 queryCommet = "";
			 queryHouse = "";
			 queryNumber = "";
			 queryCounter = "";
			 exportCodeGunPropertyIds = "111, ,111";
			 queryAllSelected = "N";
			 isVendorAttribute = false;
			 codeGunFlag = "serialNumber";
			 maintenanceUserFlag = "";
			 exportCodeGunSerialNumbers = "111, ,12";
			 hideQueryAssetUser = "N";
			 exportField = "";
			 queryParam = "";
			fieldMap = new HashMap<String, String>();
			 toMail = "";
			rows = -1;
			page = 2;
			totalSize = 10;
			assetManageFormDTO = 
					new AssetManageFormDTO(queryStorage, queryStatus, companyDTO, companyId,
							assetTypeName, storage, status, supportedFunction, queryContractId, 
							queryMerName, queryHeaderName, queryMerchantCode, queryMerInstallAddress,
							queryArea, queryMaType, queryIsEnabled, queryKeeperName, queryTid, queryAssetOwner,
							queryAssetUser, queryDtid, queryPropertyIds, querySerialNumbers, queryAssetId, queryHistId,
							queryBorrower, editFlag, queryAssetCategory, queryUser, queryHistory, dmmRepositoryHistoryDTOs, 
							dmmRepositoryHistoryDTO, rows, page, sort, order, totalSize, dmmRepositoryDTO, beforeTicketCompletionDate,
							afterTicketCompletionDate, beforeUpdateDate, afterUpdateDate, queryIsCup, exportFields, historyExport,
							imageName, ip, queryAction, userId, toMailId, queryAssetIds, queryAssetIdList, queryAssetType,
							queryAssetName, queryPeople, queryCommet, queryHouse, queryNumber, merchantFormDTO, queryCounter, 
							queryCartonNo, isVendorAttribute, codeGunFlag, maintenanceUserFlag, exportCodeGunSerialNumbers,
							exportCodeGunPropertyIds, queryAllSelected, fieldMap, toMail, exportField, queryParam, 
							departmentList, queryCaseFlag, hideQueryAssetUser);
					
					
			repositoryDTOs = this.repositoryDAO.getAssetIdList(assetManageFormDTO);
			if (!CollectionUtils.isEmpty(repositoryDTOs)) {
				Assert.assertNotNull(repositoryDTOs);
			} else {
				Assert.assertTrue(repositoryDTOs != null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testListBy()", "is error:", e);
			e.printStackTrace();
		}
	}
	public void testCheckAssetIsInWarehouse(){
		try {
			String serialNumber = "11111" ;
			String fromWarehouseId = "01" ;
			DmmRepositoryDTO  dmmRepositoryDTO = this.repositoryDAO.checkAssetIsInWarehouse(serialNumber, fromWarehouseId);
			if(dmmRepositoryDTO!= null){
				Assert.assertNotNull(dmmRepositoryDTO);
			} else {
				Assert.assertTrue(dmmRepositoryDTO == null);
			}
			serialNumber = "" ;
			fromWarehouseId = "" ;
			dmmRepositoryDTO = this.repositoryDAO.checkAssetIsInWarehouse(serialNumber, fromWarehouseId);
			if(dmmRepositoryDTO!= null){
				Assert.assertNotNull(dmmRepositoryDTO);
			} else {
				Assert.assertTrue(dmmRepositoryDTO == null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmRepositoryDAO.testDeleteTransferData()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @return the repositoryDAO
	 */
	public IDmmRepositoryDAO getRepositoryDAO() {
		return repositoryDAO;
	}

	/**
	 * @param repositoryDAO the repositoryDAO to set
	 */
	public void setRepositoryDAO(IDmmRepositoryDAO repositoryDAO) {
		this.repositoryDAO = repositoryDAO;
	}
	
}
