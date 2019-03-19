package com.cybersoft4u.xian.iatoms.test.dao;

import java.util.List;

import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BimMerchantHeaderDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.MerchantHeaderFormDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IMerchantHeaderDAO;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;

/**
 * Purpose: MerchantHeaderDAO類單元測試
 * @author jasonzhou
 * @since  JDK 1.6
 * @date   2016-6-28
 * @MaintenancePersonnel jasonzhou
 */
public class _TestMerchantHeaderDAO extends AbstractTestCase{
	
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog logger = (CafeLog)CafeLogFactory.getLog(_TestMerchantHeaderDAO.class);

	/**
	 * 特店表頭維護DAO
	 */
	private IMerchantHeaderDAO merchantHeaderDAO;

	/**
	 * Purpose:查詢方法測試
	 * @author jasonzhou
	 * @return void
	 */
	public void testListBy(){
		try {
			MerchantHeaderFormDTO merchantHeaderFormDTO = new MerchantHeaderFormDTO();
			merchantHeaderFormDTO.setQueryCustomerId("1");
			merchantHeaderFormDTO.setQueryIsVip("Y");
			merchantHeaderFormDTO.setHeaderName("");
			merchantHeaderFormDTO.setSort("");
			merchantHeaderFormDTO.setOrder("CUSTOMER_ID");
			merchantHeaderFormDTO.setRows(10);
			merchantHeaderFormDTO.setPage(1);
			List<BimMerchantHeaderDTO> merchantHeaderList = this.merchantHeaderDAO.listby("", "", "", "", "", "", "",0, 10, true, "", "", "", "");
			//assertNotNull(merchantHeaderList);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+".testQuery", e);
		}
	}
	
	/**
	 * Purpose:獲取總筆數方法測試
	 * @author jasonzhou
	 * @return void
	 */
	public void testCount(){
		try {
			String queryCustomerId = "1";
			String queryMerCode = "1";
			String queryRegisteredName = "1";
			String businessAddress = "";
			String queryAnnouncedName = "1";
			String queryIsVip = "Y";
			Integer count = this.merchantHeaderDAO.getCount(queryCustomerId, queryMerCode, queryRegisteredName, queryAnnouncedName,businessAddress, queryIsVip);
			//assertNotNull(count);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+".testCount", e);
		}
	}
	
	/**
	 * Purpose:判斷同一特店代號下特店表頭是否重復
	 * @author jasonzhou
	 * @return void
	 */
	public void testIsRepeat(){
		try {
			String mid = "zhengwei1";
			String merchantAnnouncedName = "1233";
			boolean isRepeat = this.merchantHeaderDAO.check("", "", "");
			//assertTrue(isRepeat);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+".testIsRepeat", e);
		}
	}
	/**
	 * @return the merchantHeaderDAO
	 */
	public IMerchantHeaderDAO getMerchantHeaderDAO() {
		return merchantHeaderDAO;
	}

	/**
	 * @param merchantHeaderDAO the merchantHeaderDAO to set
	 */
	public void setMerchantHeaderDAO(IMerchantHeaderDAO merchantHeaderDAO) {
		this.merchantHeaderDAO = merchantHeaderDAO;
	}
}
