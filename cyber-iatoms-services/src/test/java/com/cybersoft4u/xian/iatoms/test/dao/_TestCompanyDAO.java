package com.cybersoft4u.xian.iatoms.test.dao;


import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import junit.framework.Assert;
import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;

/**
 * Purpose:公司基本訊息維護DAO的單元測試
 * @author ElvaHe
 * @since JDK 1.6
 * @date 2017/05/11
 * @MaintenancePersonnel ElvaHe
 */
public class _TestCompanyDAO extends AbstractTestCase{
	
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestCompanyDAO.class);
	
	/**
	 * 注入公司基本訊息維護DAO
	 */
	private ICompanyDAO companyDAO;
	
	/**
	 * 無參構造
	 */
	public _TestCompanyDAO() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	
	/**
	 * Purpose:測試查詢公司基本資料訊息的方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testListBy(){
		try {
			//公司類型
			String companyType = "2";
			//公司簡稱
			String shortName = "耐克";
			//排列依據
			String sort = "";
			//排序方式
			String order = "";
			int pageSize = 10;
			int pageIndex = 1;
			//符合查詢條件的公司信息集合
			List<CompanyDTO> companyDTOs = this.companyDAO.listBy(companyType, shortName, sort, order, pageSize, pageIndex);
			if(!CollectionUtils.isEmpty(companyDTOs)){
				Assert.assertNotNull(companyDTOs);
			} else {
				Assert.assertFalse(companyDTOs == null);
			}
			
			//公司類型
			companyType = "";
			//公司簡稱
			shortName = "";
			//排列依據
			 sort = " companyTypeName,companyCode";
			//排序方式
			order = "asc";
			pageSize = 10;
			pageIndex = 1;
			//符合查詢條件的公司信息集合
			companyDTOs = this.companyDAO.listBy(companyType, shortName, sort, order, pageSize, pageIndex);
			if(!CollectionUtils.isEmpty(companyDTOs)){
				Assert.assertNotNull(companyDTOs);
			} else {
				Assert.assertFalse(companyDTOs == null);
			}
		} catch (DataAccessException e) {
			LOGGER.error("_TestCompanyDAO.testListBy()", "is error ：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試獲取符合條件資料總筆數的方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testCount(){
		try {
			//公司類型
			String queryCompanyType = "sasfas";
			//公司簡稱
			String queryShortName = "";
			Integer count = this.companyDAO.count(queryCompanyType, queryShortName);
			if(count != 0){
				Assert.assertTrue(count!=0);
			} else {
				Assert.assertFalse(count!=0);
			}
		} catch (Exception e) {
			LOGGER.error("_TestCompanyDAO.testCount()", "is error ：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試檢核是否存在重複的公司代號或客戶碼的方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testIsCheck(){
		try {
			//公司編號
			String companyId = "";
			//公司代號
			String companyCode = "";
			//公司簡稱
			String shortName = "";
			//客戶碼
			String customerCode = "=";
			boolean isCheck = this.companyDAO.isCheck(companyId, companyCode, shortName, customerCode);
			if(isCheck){
				Assert.assertTrue(isCheck);
			} else {
				Assert.assertFalse(isCheck);
			}
		} catch (Exception e) {
			LOGGER.error("_TestCompanyDAO.testIsCheck()", "is error ：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試根據條件的到公司下拉框列表的方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetCompanyList(){
		try {
			//公司類型
			List<String> companyTypeList = new ArrayList<String>();
			companyTypeList.add("HARDWARE_VENDOR");
			companyTypeList.add("MAINTENANCE_VENDOR");
			companyTypeList.add("CUSTOMER");
			//登入驗證方式
			String authenticationType = "";
			//是否有sla信息
			Boolean isHaveSla = true;
			List<Parameter> companyList = this.companyDAO.getCompanyList(companyTypeList, authenticationType, isHaveSla, null);
			if(CollectionUtils.isEmpty(companyList)){
				Assert.assertNotNull(companyList);
			} else {
				Assert.assertFalse(companyList == null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestCompanyDAO.testGetCompanyList()", "is error ：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試根據公司編號以及刪除標誌查詢符合條件的數量的方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testCountUserByCompanyId(){
		try {
			//公司ID
			String companyId= "thyhyh";
			//刪除標誌位
			String deleted = "";
			Integer count = this.companyDAO.countUserByCompanyId(companyId, deleted);
			if(count != 0){
				Assert.assertTrue(count!=0);
			} else {
				Assert.assertFalse(count!=0);
			}
		} catch (Exception e) {
			LOGGER.error("_TestCompanyDAO.testCountUserByCompanyId()", "is error ：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試根據公司名稱獲取公司ID的方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetCompanyIdByName(){
		try {
			//公司名稱
			String name = "英雄聯盟12";
			//要獲取的公司id
			String companyId = this.companyDAO.getCompanyIdByName(name);
			if(!StringUtils.isEmpty(companyId)){
				Assert.assertNotNull(companyId);
			} else {
				Assert.assertFalse(companyId != null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestCompanyDAO.testGetCompanyIdByName()", "is error ：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試根據公司ID獲取公司信息的方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetCompanyDTOByCompanyId(){
		try {
			//客戶ID
			String companyId = "1470649357293-00031";
			//該客戶ID的相關公司信息
			CompanyDTO companyDTO = this.companyDAO.getCompanyDTOByCompanyId(companyId);
			if(companyDTO != null){
				Assert.assertNotNull(companyDTO);
			} else {
				Assert.assertFalse(companyDTO != null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestCompanyDAO.testGetCompanyDTOByCompanyId()", "is error ：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the companyDAO
	 */
	public ICompanyDAO getCompanyDAO() {
		return companyDAO;
	}

	/**
	 * @param companyDAO the companyDAO to set
	 */
	public void setCompanyDAO(ICompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}
	
}
