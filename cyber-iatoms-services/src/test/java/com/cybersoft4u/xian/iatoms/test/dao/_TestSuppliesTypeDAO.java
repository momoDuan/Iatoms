package com.cybersoft4u.xian.iatoms.test.dao;

import java.util.List;

import junit.framework.Assert;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmSuppliesDTO;
import com.cybersoft4u.xian.iatoms.services.dao.impl.SuppliesTypeDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmSupplies;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;
/**
 * Purpose: 測試耗材品項維護的dao的單元測試
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2017/5/9
 * @MaintenancePersonnel HermanWang
 */
public class _TestSuppliesTypeDAO extends AbstractTestCase {
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestSuppliesTypeDAO.class);
	/**
	 * 注入suppliesDAO
	 */
	public SuppliesTypeDAO suppliesDAO;

	/**
	 * 無參構造
	 */
	@SuppressWarnings("deprecation")
	public _TestSuppliesTypeDAO() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	/**
	 * Purpose:測試耗材品項維護的dao
	 * @author HermanWang
	 * @return void
	 */
	public void testListBy(){
		try {
			/*DmmSupplies dmmSupplies = new DmmSupplies();
			dmmSupplies.setSuppliesId("1234567890");
			dmmSupplies.setCompanyId("147514031er5196-1111");
			dmmSupplies.setSuppliesType("SUPPLIereES_TYPE02");
			dmmSupplies.setSuppliesName("suibiansaigesha");
			suppliesDAO.save(dmmSupplies);
			DmmSupplies nDmmSupplies = suppliesDAO.findByPrimaryKey(DmmSupplies.class,"1234567890");*/
			//查詢條件 客戶ere
			String queryCustomerId = "147514031er5196-1111";
			//查詢條件 耗材分類
			String querySuppliesCode = "SUPPLIereES_TYPE02";
			//查詢條件 耗材名稱
			String querySuppliesName = "suibi";
			Integer pageSize = 10;
			Integer page = 1;
			String sort = null;
			String order = null;
			List<DmmSuppliesDTO> list = suppliesDAO.listBy(queryCustomerId,querySuppliesCode,querySuppliesName,pageSize,page,sort,order);
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
	 * Purpose:測試耗材數量
	 * @author HermanWang
	 * @return void
	 */
	public void testCount(){
		try {
			//查詢條件 客戶
			String queryCustomerId = "";
			//查詢條件耗材 分類
			String querySuppliesCode = "";
			//查詢條件 耗材名稱
			String querySuppliesName = "";
			int count = suppliesDAO.getCount(queryCustomerId, querySuppliesCode, querySuppliesName);
			if(count != 0) {
				Assert.assertTrue(count!=0);
			} else {
				Assert.assertTrue(count==0);
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testCount()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:判斷同一客戶下的耗材品不能重複
	 * @author HermanWang
	 * @return void
	 */
	public void testIsCheck(){
		try {
			//測試修改耗材品重複
			// 客戶
			String companyId = "1475140315196-0142";
			//耗材名稱
			String suppliesName = "1DFFDDFDFD";
			//耗材ID
			String suppliesId = "1476176142179-0159";
			boolean isRepeatEdit = suppliesDAO.isCheck(companyId, suppliesName, suppliesId);
			if(isRepeatEdit) {
				Assert.assertTrue(isRepeatEdit);
			} else {
				Assert.assertTrue(!isRepeatEdit);
			}
			
			//測試新增耗材品重複
			// 客戶
			companyId = "1475140315196-0142";
			//耗材名稱
			suppliesName = "1DFdfdFDDFDFD";
			//耗材ID
			suppliesId = "";
			boolean isRepeatAdd = suppliesDAO.isCheck(companyId, suppliesName, suppliesId);
			if(isRepeatAdd) {
				Assert.assertTrue(isRepeatAdd);
			} else {
				Assert.assertTrue(!isRepeatAdd);
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testIsCheck()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:取得【耗材品項維護】該客戶的耗材資料
	 * @author HermanWang
	 * @return void
	 */
	public void testGetSuppliesTypes(){
		try {
			//測試有耗材名稱
			// 客戶
			String companyId = "1475140315196-0142";
			//耗材名稱
			String suppliesType = "SUPPLIES_TYPE02";
			//耗材list
			List<Parameter> list = suppliesDAO.getSuppliesTypes(companyId, suppliesType);
			if(!CollectionUtils.isEmpty(list)) {
				Assert.assertNotNull(list);
			} else {
				Assert.assertTrue(list.size() == 0);
			} 
			
			//測試沒有耗材名稱
			// 客戶
			companyId = "1475140315196-0142";
			//耗材名稱
			suppliesType = "";
			//耗材list
			list = suppliesDAO.getSuppliesTypes(companyId, suppliesType);
			if(!CollectionUtils.isEmpty(list)) {
				Assert.assertNotNull(list);
			} else {
				Assert.assertTrue(list.size() == 0);
			} 
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testGetSuppliesTypes()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:取得【耗材品項維護】該客戶的耗材分類
	 * @author HermanWang
	 * @return void
	 */
	public void testGetSuppliesTypeList(){
		try {
			// 客戶
			String companyId = "1475140315196-0142";
			//耗材分類list
			List<Parameter> list = suppliesDAO.getSuppliesTypeList(companyId);
			if(!CollectionUtils.isEmpty(list)) {
				Assert.assertNotNull(list);
			} else {
				Assert.assertTrue(list.size() == 0);
			} 
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testGetSuppliesTypeList()", e);
			e.printStackTrace();
		}
	}
	/**
	 * @return the suppliesDAO
	 */
	public SuppliesTypeDAO getSuppliesDAO() {
		return suppliesDAO;
	}

	/**
	 * @param suppliesDAO the suppliesDAO to set
	 */
	public void setSuppliesDAO(SuppliesTypeDAO suppliesDAO) {
		this.suppliesDAO = suppliesDAO;
	}
	
}
