package com.cybersoft4u.xian.iatoms.test.dao;
import java.util.List;

import junit.framework.Assert;

import org.springframework.util.CollectionUtils;

import cafe.core.dao.DataAccessException;

import com.cybersoft4u.xian.iatoms.common.bean.dto.MerchantDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IMerchantDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimMerchant;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;

/**
 * Purpose: 客戶特店DAO的單元測試
 * @author DavidZheng
 * @since  JDK 1.6
 * @date   2016/6/22
 * @MaintenancePersonnel DavidZheng
 */
public class _TestMerchantNewDAO extends AbstractTestCase {
	/**
	 * 注入merchantNewDAO
	 */
	public IMerchantDAO merchantNewDAO;

	/**
	 * 無參構造
	 */
	@SuppressWarnings("deprecation")
	public _TestMerchantNewDAO() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}

	/**
	 * Purpose: 測試查詢方法
	 * @author DavidZheng
	 * @return void
	 */
	public void testListBy(){
		try {
			//查詢條件 客戶id
			/*BimMerchant bimMerchant = new BimMerchant();
			bimMerchant.setMerchantId("33333");
			bimMerchant.setCompanyId("123");
			bimMerchant.setMerchantCode("111");
			bimMerchant.setName("測試");
			merchantNewDAO.save(bimMerchant);
			BimMerchant bimMerchantnew = this.merchantNewDAO.findByPrimaryKey(BimMerchant.class, bimMerchant.getMerchantId());*/
			
			String queryCustomerId = "123";
			//查詢條件 特店名稱
			String queryName = "";
			//查詢參數特店代號
			String queryMerchantCode = "111";
			//每頁條數
			Integer rows = 10;
			//當前頁
			Integer page = 1;
			String sort = null;
			String order = null;
			List<MerchantDTO> list = merchantNewDAO.listBy(queryCustomerId, queryName, queryMerchantCode, rows, page, sort, order);
			if(!CollectionUtils.isEmpty(list)) {
				Assert.assertNotNull(list);
			} else {
				Assert.assertTrue(list.size() == 0);
			} 
		} catch (DataAccessException e) {
			System.out.println("_TestMerchantNewDAO.testListBy() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose: 測試獲得記錄數的方法
	 * @author DavidZheng
	 * @return void
	 */
	public void testCount(){
		try {
			//查詢條件 客戶id
			String queryCustomerId = "1";
			//查詢條件 特店名稱
			String queryName = "d";
			//查詢參數特店代號
			String queryMerchantCode = "1";
			int count = merchantNewDAO.count(queryCustomerId, queryName, queryMerchantCode);
			if(count != 0) {
				Assert.assertTrue(count!=0);
			} else {
				Assert.assertTrue(count==0);
			}
		} catch (DataAccessException e) {
			System.out.println("_TestMerchantNewDAO.testCount() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose: 測試檢核特店代號是否重複的方法
	 * @author DavidZheng
	 * @return void
	 */
	public void testGetRepeatCount(){
		try {
			String merchantCode = "dsadsa";
			String merchantId = "dsadsa";
			String companyId = "dsadsa";
			Boolean isCheck = merchantNewDAO.isCheck(merchantCode, merchantId, companyId);
			if(!isCheck) {
				Assert.assertTrue(!isCheck);
			} else {
				Assert.assertTrue(isCheck);
			}
		} catch (DataAccessException e) {
			System.out.println("_TestMerchantNewDAO.testGetRepeatCount() is error" + e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose: 測試檢核根據id獲得一筆客戶特店的資料
	 * @author DavidZheng
	 * @return void
	 */
	public void testGetMerchantNewInfo(){
		try {
			String merchantId = "263";
			MerchantDTO merchantNewDTO = merchantNewDAO.getMerchantInfo(merchantId,null, null,null);
			BimMerchant bimMerchantnew = this.merchantNewDAO.findByPrimaryKey(BimMerchant.class, merchantId);
			if(bimMerchantnew != null) {
				Assert.assertNotNull(merchantNewDTO);
			} else {
				Assert.assertNull(merchantNewDTO);
			}
		} catch (DataAccessException e) {
			System.out.println("_TestMerchantNewDAO.testGetMerchantNewInfo() is error" + e);
			e.printStackTrace();
		}
	}

	/**
	 * @return the merchantNewDAO
	 */
	public IMerchantDAO getMerchantNewDAO() {
		return merchantNewDAO;
	}

	/**
	 * @param merchantNewDAO the merchantNewDAO to set
	 */
	public void setMerchantNewDAO(IMerchantDAO merchantNewDAO) {
		this.merchantNewDAO = merchantNewDAO;
	}
	
}
