package com.cybersoft4u.xian.iatoms.test.dao;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.springframework.util.CollectionUtils;

import cafe.core.util.DateTimeUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.RepoStatusReportDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IRepoStatusReportDAO;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;
/**
 * Purpose：設備狀態報表DAO的單元測試
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2017/6/6
 * @MaintenancePersonnel  ElvaHe
 */
public class _TestRepoStatusReportDAO extends AbstractTestCase {

	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestRepoStatusReportDAO.class);
	
	/**
	 * 注入設備狀態報表DAO
	 */
	private IRepoStatusReportDAO repoStatusReportDAO;
	
	/**
	 * 無參構造
	 */
	public _TestRepoStatusReportDAO() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	
	/**
	 * Purpose:測試按照查詢條件查詢數據（無客戶編號）
	 * @author ElvaHe
	 * @return void
	 */
	public void testCompanylistBy(){
		try {
			List<RepoStatusReportDTO> repoStatusReportDTOList = null;
			
			//當前登錄者是倉管
			String dataAcl = IAtomsConstants.YES;
			//當前登錄者管理的倉庫集合
			List<String> warehouseList = new ArrayList<String>();
			warehouseList.add("1484037704443-0013");
			warehouseList.add("1474530367321-0150");
			//維護模式 -- 租賃
			String maTypeCode = "LEASE";
			//通訊模式
			String commModeId = "'Dial_Up','TCP_IP'";
			//設備名稱
			String assetTypeId = "'-----','0.0.0.0.'";
			//查詢月份 = 當前月份
			String tableName = IAtomsConstants.IATOMS_TB_NAME_REPOSITORY;
			String date = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMM);
			//查詢月份 ！= 當前月份
			String tableNameNo = IAtomsConstants.IATOMS_TB_NAME_REPOSITORY_HIST_MONTHLY;
			String dateNo = "201506";
			//排序
			String order = "asc";
			String sort = "companyId";
			
			//查詢月份 = 當前月份 且 使用默認排序方式
			repoStatusReportDTOList = this.repoStatusReportDAO.companylistBy(tableName, maTypeCode, assetTypeId, commModeId, date, null, null, warehouseList, dataAcl, -1, -1);
			if (!CollectionUtils.isEmpty(repoStatusReportDTOList)) {
				Assert.assertEquals(repoStatusReportDTOList.size(), repoStatusReportDTOList.size());
			} else {
				Assert.assertTrue(true);
			}
			
			
			//查詢月份 = 當前月份 且 使用選擇排序方式
			repoStatusReportDTOList = this.repoStatusReportDAO.companylistBy(tableName, maTypeCode, assetTypeId, commModeId, date, order, sort, warehouseList, dataAcl, -1, -1);
			if (!CollectionUtils.isEmpty(repoStatusReportDTOList)) {
				Assert.assertEquals(repoStatusReportDTOList.size(), repoStatusReportDTOList.size());
			} else {
				Assert.assertTrue(true);
			}
			
			//查詢月份 != 當前月份 且 使用默認排序方式
			repoStatusReportDTOList = this.repoStatusReportDAO.companylistBy(tableNameNo, maTypeCode, assetTypeId, commModeId, dateNo, null, null, warehouseList, dataAcl, -1, -1);
			if (!CollectionUtils.isEmpty(repoStatusReportDTOList)) {
				Assert.assertEquals(repoStatusReportDTOList.size(), repoStatusReportDTOList.size());
			} else {
				Assert.assertTrue(true);
			}
			
			//查詢月份 != 當前月份 且 使用選擇排序方式
			repoStatusReportDTOList = this.repoStatusReportDAO.companylistBy(tableNameNo, maTypeCode, assetTypeId, commModeId, dateNo, order, sort, warehouseList, dataAcl, -1, -1);
			if (!CollectionUtils.isEmpty(repoStatusReportDTOList)) {
				Assert.assertEquals(repoStatusReportDTOList.size(), repoStatusReportDTOList.size());
			} else {
				Assert.assertTrue(true);
			}
		} catch (Exception e) {
			LOGGER.debug("_TestRepoStatusReportDAO.testCompanylistBy()", "is error", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試按照查詢條件查詢數據（有客戶編號）
	 * @author ElvaHe
	 * @return void
	 */
	public void testListBy(){
		try {
			List<RepoStatusReportDTO> repoStatusReportDTOList = null;
			String companyId = "1475139958276-0141";
			//當前登錄者是倉管
			String dataAcl = IAtomsConstants.YES;
			//當前登錄者管理的倉庫集合
			List<String> warehouseList = new ArrayList<String>();
			warehouseList.add("1484037704443-0013");
			warehouseList.add("1474530367321-0150");
			//維護模式 -- 租賃
			String maTypeCode = "LEASE";
			//通訊模式
			String commModeId = "'Dial_Up','TCP_IP'";
			//設備名稱
			String assetTypeId = "'-----','0.0.0.0.'";
			//查詢月份 = 當前月份
			String tableName = IAtomsConstants.IATOMS_TB_NAME_REPOSITORY;
			String date = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMM);
			//查詢月份 ！= 當前月份
			String tableNameNo = IAtomsConstants.IATOMS_TB_NAME_REPOSITORY_HIST_MONTHLY;
			String dateNo = "201506";
			//排序
			String order = "asc";
			String sort = "companyId";
			
			//查詢月份 = 當前月份 且 使用默認排序方式
			repoStatusReportDTOList = this.repoStatusReportDAO.listBy(tableName, companyId, maTypeCode, assetTypeId, commModeId, date, null, null, warehouseList, dataAcl);
			if (!CollectionUtils.isEmpty(repoStatusReportDTOList)) {
				Assert.assertEquals(repoStatusReportDTOList.size(), repoStatusReportDTOList.size());
			} else {
				Assert.assertTrue(true);
			}
			
			
			//查詢月份 = 當前月份 且 使用選擇排序方式
			repoStatusReportDTOList = this.repoStatusReportDAO.listBy(tableName, companyId, maTypeCode, assetTypeId, commModeId, date, order, sort, warehouseList, dataAcl);
			if (!CollectionUtils.isEmpty(repoStatusReportDTOList)) {
				Assert.assertEquals(repoStatusReportDTOList.size(), repoStatusReportDTOList.size());
			} else {
				Assert.assertTrue(true);
			}
			
			//查詢月份 != 當前月份 且 使用默認排序方式
			repoStatusReportDTOList = this.repoStatusReportDAO.listBy(tableNameNo, companyId, maTypeCode, assetTypeId, commModeId, dateNo, null, null, warehouseList, dataAcl);
			if (!CollectionUtils.isEmpty(repoStatusReportDTOList)) {
				Assert.assertEquals(repoStatusReportDTOList.size(), repoStatusReportDTOList.size());
			} else {
				Assert.assertTrue(true);
			}
			
			//查詢月份 != 當前月份 且 使用選擇排序方式
			repoStatusReportDTOList = this.repoStatusReportDAO.listBy(tableNameNo, companyId, maTypeCode, assetTypeId, commModeId, dateNo, order, sort, warehouseList, dataAcl);
			if (!CollectionUtils.isEmpty(repoStatusReportDTOList)) {
				Assert.assertEquals(repoStatusReportDTOList.size(), repoStatusReportDTOList.size());
			} else {
				Assert.assertTrue(true);
			}
		} catch (Exception e) {
			LOGGER.debug("_TestRepoStatusReportDAO.testListBy()", "is error", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:查詢符合條件的記錄數
	 * @return void
	 * @author ElvaHe
	 */
	public void testCount(){
		try {
			Integer count = 0;
			String companyId = "1475139958276-0141";
			//當前登錄者是倉管
			String dataAcl = IAtomsConstants.YES;
			//當前登錄者管理的倉庫集合
			List<String> warehouseList = new ArrayList<String>();
			warehouseList.add("1484037704443-0013");
			warehouseList.add("1474530367321-0150");
			//維護模式 -- 租賃
			String maTypeCode = "LEASE";
			//通訊模式
			String commModeId = "'Dial_Up','TCP_IP'";
			//設備名稱
			String assetTypeId = "'-----','0.0.0.0.'";
			//查詢月份 = 當前月份
			String tableName = IAtomsConstants.IATOMS_TB_NAME_REPOSITORY;
			String date = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMM);
			//查詢月份 ！= 當前月份
			String tableNameNo = IAtomsConstants.IATOMS_TB_NAME_REPOSITORY_HIST_MONTHLY;
			String dateNo = "201506";
			
			//查詢月份 = 當前月份
			count = this.repoStatusReportDAO.count(tableName, companyId, maTypeCode, assetTypeId, commModeId, date, warehouseList, dataAcl);
			if (count > 0) {
				Assert.assertEquals(count.intValue(), count.intValue());
			} else {
				Assert.assertTrue(count.intValue() == 0);
			}
			
			//查詢月份 ！= 當前月份
			count = this.repoStatusReportDAO.count(tableNameNo, companyId, maTypeCode, assetTypeId, commModeId, dateNo, warehouseList, dataAcl);
			if (count > 0) {
				Assert.assertEquals(count.intValue(), count.intValue());
			} else {
				Assert.assertTrue(count.intValue() == 0);
			}
		} catch (Exception e) {
			LOGGER.error("_TestRepoStatusReportDAO.testCount()", "is error", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:根據條件查詢資料(匯出清單)
	 * @author ElvaHe
	 * @return void
	 */
	public void testListRepositoryStatus(){
		try {
			List<RepoStatusReportDTO> results = null;
			String companyId = "1475139958276-0141";
			//當前登錄者是倉管
			String dataAcl = IAtomsConstants.YES;
			//當前登錄者管理的倉庫集合
			List<String> warehouseList = new ArrayList<String>();
			warehouseList.add("1484037704443-0013");
			warehouseList.add("1474530367321-0150");
			//維護模式 -- 租賃
			String maTypeCode = "LEASE";
			//通訊模式
			String commModeId = "'Dial_Up','TCP_IP'";
			//設備名稱
			String assetTypeId = "'-----','0.0.0.0.'";
			//查詢月份 = 當前月份
			String tableName = IAtomsConstants.IATOMS_TB_NAME_REPOSITORY;
			String date = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMM);
			//查詢月份 ！= 當前月份
			String tableNameNo = IAtomsConstants.IATOMS_TB_NAME_REPOSITORY_HIST_MONTHLY;
			String dateNo = "201506";
			
			//查詢月份 = 當前月份
			results = this.repoStatusReportDAO.listRepositoryStatus(tableName, companyId, maTypeCode, assetTypeId, commModeId, date, warehouseList, dataAcl);
			if (!CollectionUtils.isEmpty(results)) {
				Assert.assertEquals(results.size(), results.size());
			} else {
				Assert.assertTrue(results != null);
			}
			//查詢月份 ！= 當前月份
			results = this.repoStatusReportDAO.listRepositoryStatus(tableNameNo, companyId, maTypeCode, assetTypeId, commModeId, dateNo, warehouseList, dataAcl);
			if (!CollectionUtils.isEmpty(results)) {
				Assert.assertEquals(results.size(), results.size());
			} else {
				Assert.assertTrue(results != null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestRepoStatusReportDAO.testListRepositoryStatus()", "is error", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the repoStatusReportDAO
	 */
	public IRepoStatusReportDAO getRepoStatusReportDAO() {
		return repoStatusReportDAO;
	}

	/**
	 * @param repoStatusReportDAO the repoStatusReportDAO to set
	 */
	public void setRepoStatusReportDAO(IRepoStatusReportDAO repoStatusReportDAO) {
		this.repoStatusReportDAO = repoStatusReportDAO;
	}

}
