package com.cybersoft4u.xian.iatoms.test.dao;

import java.util.List;

import junit.framework.Assert;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.RepoStatusReportDTO;
import com.cybersoft4u.xian.iatoms.services.dao.ICustomerRepoDAO;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;

public class _TestCustomerRepoDAO extends AbstractTestCase {
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestCustomerRepoDAO.class);
	/**
	 * 客戶設備總表DAO
	 */
	private ICustomerRepoDAO customerRepoDAO;

	/**
	 * 無參構造
	 */
	@SuppressWarnings("deprecation")
	public _TestCustomerRepoDAO() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	/**
	 * Purpose:客戶設備總表的查詢
	 * @author HermanWang
	 * @return void
	 */
	public void testListBy(){
		try {
			//查詢條件 表名稱
			String tableName = IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY;
			//查詢條件 是否為客戶屬性
			Boolean isCustomerAttribute = false;
			//查詢條件 userid
			String userId = "1473311383499-0060";
			//是否倉庫庫控管
			String dataAcl = "N";
			String yyyyMM = "201705";
			//客戶id
			String customerId = "1486106270734-0341";
			//維護模式
			String maTypeCode = "LEASE";
			Integer pageSize = 10;
			Integer page = 1;
			String sort = null;
			String order = null;
			List<RepoStatusReportDTO> list = customerRepoDAO.list(tableName, isCustomerAttribute, userId, dataAcl,
					yyyyMM, customerId, maTypeCode, pageSize, page, sort, order);
			if(!CollectionUtils.isEmpty(list)) {
				Assert.assertNotNull(list);
			} else {
				Assert.assertTrue(list.size() == 0);
			} 
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testListBy()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:客戶設備總表的獲取數據
	 * @author HermanWang
	 * @return void
	 */
	public void testCount(){
		try {
			//查詢條件 表名稱
			String tableName = IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY;
			//查詢條件 是否為客戶屬性
			Boolean isCustomerAttribute = false;
			//查詢條件 userid
			String userId = "1473311383499-0060";
			//是否倉庫控管
			String dataAcl = "N";
			//查詢時間
			String yyyyMM = "201705";
			//客戶id
			String customerId = "1486106270734-0341";
			//維護類型
			String maTypeCode = "LEASE";
			List<RepoStatusReportDTO> repoStatusReportDTOs = customerRepoDAO.getAssetList(tableName, isCustomerAttribute, userId, dataAcl,
					yyyyMM, customerId, maTypeCode);
			if(!CollectionUtils.isEmpty(repoStatusReportDTOs)) {
				Assert.assertNotNull(repoStatusReportDTOs);
			} else {
				Assert.assertTrue(repoStatusReportDTOs.size()==0);
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testCount()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:查詢符合條件的設別列表
	 * @author HermanWang
	 * @return void
	 */
	public void testGetAssetNameList(){
		try {
			//查詢條件 表名稱
			String tableName = IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY;
			//客戶id
			String customerId = "1486106270734-0341";
			//維護類型
			String maTypeCode = "LEASE";
			//耗材分類list
			List<Parameter> list = customerRepoDAO.getAssetNameList(tableName, customerId, maTypeCode);
			if(!CollectionUtils.isEmpty(list)) {
				Assert.assertNotNull(list);
			} else {
				Assert.assertTrue(list.size() == 0);
			} 
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testGetAssetNameList()", e);
			e.printStackTrace();
		}
	}
	/**
	 * @return the customerRepoDAO
	 */
	public ICustomerRepoDAO getCustomerRepoDAO() {
		return customerRepoDAO;
	}

	/**
	 * @param customerRepoDAO the customerRepoDAO to set
	 */
	public void setCustomerRepoDAO(ICustomerRepoDAO customerRepoDAO) {
		this.customerRepoDAO = customerRepoDAO;
	}
	
}
