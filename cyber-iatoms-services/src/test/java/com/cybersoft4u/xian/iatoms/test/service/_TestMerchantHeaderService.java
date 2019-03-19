package com.cybersoft4u.xian.iatoms.test.service;

import cafe.core.context.SessionContext;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.MerchantHeaderFormDTO;
import com.cybersoft4u.xian.iatoms.services.IMerchantHeaderService;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;

/**
 * Purpose: MerchantHeaderService類單元測試
 * @author jasonzhou
 * @since  JDK 1.6
 * @date   2016-6-27
 * @MaintenancePersonnel jasonzhou
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class _TestMerchantHeaderService extends AbstractTestCase{
	
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog logger = (CafeLog)CafeLogFactory.getLog(_TestMerchantHeaderService.class);
	
	/**
	 * 特店表頭維護service
	 */
	private IMerchantHeaderService merchantHeaderService;
	
	@SuppressWarnings("deprecation")
	public _TestMerchantHeaderService(){
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase#testInit()
	 */
	public void testInit(){
		try {
			SessionContext sessionContext = new SessionContext();
			MerchantHeaderFormDTO merchantHeaderFormDTO = new MerchantHeaderFormDTO();
			sessionContext.setRequestParameter(merchantHeaderFormDTO);
			merchantHeaderFormDTO = (MerchantHeaderFormDTO) this.merchantHeaderService.init(sessionContext).getResponseResult();
			//assertNotNull(merchantHeaderFormDTO);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+".testInit()", e);
		}
	}
	
	/**
	 * Purpose:初始化新增方法测试
	 * @author jasonzhou
	 * @return void
	 */
	public void testInitAdd(){
		try {
			SessionContext sessionContext = new SessionContext();
			MerchantHeaderFormDTO merchantHeaderFormDTO = new MerchantHeaderFormDTO();
			merchantHeaderFormDTO.setMerchantId("404");
			sessionContext.setRequestParameter(merchantHeaderFormDTO);
			merchantHeaderFormDTO = (MerchantHeaderFormDTO) this.merchantHeaderService.initAdd(sessionContext).getResponseResult();
//			assertNotNull(merchantHeaderFormDTO);
//			assertNotNull(merchantHeaderFormDTO.getMerchantHeaderDTO());
//			assertNotNull(merchantHeaderFormDTO.getMerchantHeaderDTO().getMerchantId());
		} catch (Exception e) {
			logger.error(this.getClass().getName()+".testInitAdd()", e);
		}
	}
	
	/**
	 * Purpose:初始化修改方法测试
	 * @author jasonzhou
	 * @return void
	 */
	public void testInitEdit(){
		try {
			SessionContext sessionContext = new SessionContext();
			MerchantHeaderFormDTO merchantHeaderFormDTO = new MerchantHeaderFormDTO();
			merchantHeaderFormDTO.setMerchantHeaderId("107");
			merchantHeaderFormDTO.setActionId(IAtomsConstants.ACTION_INIT_EDIT);
			sessionContext.setRequestParameter(merchantHeaderFormDTO);
			merchantHeaderFormDTO = (MerchantHeaderFormDTO) this.merchantHeaderService.initEdit(sessionContext).getResponseResult();
//			assertNotNull(merchantHeaderFormDTO);
//			assertNotNull(merchantHeaderFormDTO.getMerchantHeaderDTO());
//			assertNotNull(merchantHeaderFormDTO.getMerchantHeaderDTO().getMerchantHeaderId());
		} catch (Exception e) {
			logger.error(this.getClass().getName()+".testInitEdit()", e);
		}
	}
	
	/**
	 * Purpose:保存特店表頭方法测试
	 * @author jasonzhou
	 * @return void
	 */
	/*public void testSave(){
		try {
			SessionContext sessionContext = new SessionContext();
			MerchantHeaderFormDTO merchantHeaderFormDTO = new MerchantHeaderFormDTO();
			BimMerchantHeaderDTO merchantHeaderDTO = new BimMerchantHeaderDTO();
			merchantHeaderDTO.setMerchantHeaderId(123);
			merchantHeaderDTO.setIsVip("Y");
			merchantHeaderDTO.setMid("zhengwei1");
			merchantHeaderDTO.setMerchantAnnouncedName("zhangsan");
			merchantHeaderDTO.setMerchantArea(1);
			merchantHeaderDTO.setMerchantContact("lisi");
			merchantHeaderDTO.setMerchantContactPhone("13245678901");
			merchantHeaderDTO.setMerchantBusinessAddr("某某市某某街");
			merchantHeaderDTO.setOpenHour("08:30");
			merchantHeaderDTO.setCloseHour("20:00");
			merchantHeaderDTO.setAoName("wangwu");
			merchantHeaderDTO.setAoEmail("12345@126.com");
			merchantHeaderDTO.setRemark("beizhu");
			merchantHeaderFormDTO.setMerchantHeaderDTO(merchantHeaderDTO);
			sessionContext.setRequestParameter(merchantHeaderFormDTO);
			merchantHeaderFormDTO = (MerchantHeaderFormDTO) this.merchantHeaderService.save(sessionContext).getResponseResult();
			assertNotNull(merchantHeaderFormDTO);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+".testSave()", e);
		}
	}*/
	
	/**
	 * Purpose:查詢特店表頭方法測試
	 * @author jasonzhou
	 * @return void
	 */
	public void testQuery(){
		try {
			SessionContext sessionContext = new SessionContext();
			MerchantHeaderFormDTO merchantHeaderFormDTO = new MerchantHeaderFormDTO();
			merchantHeaderFormDTO.setQueryCustomerId("1");
			merchantHeaderFormDTO.setQueryIsVip("Y");
			//merchantHeaderFormDTO.setQueryRegisteredName("");
			merchantHeaderFormDTO.setSort("");
			merchantHeaderFormDTO.setOrder("CUSTOMER_ID");
			merchantHeaderFormDTO.setRows(10);
			merchantHeaderFormDTO.setPage(1);
			sessionContext.setRequestParameter(merchantHeaderFormDTO);
			merchantHeaderFormDTO = (MerchantHeaderFormDTO) this.merchantHeaderService.query(sessionContext).getResponseResult();
//			assertNotNull(merchantHeaderFormDTO);
//			assertNotNull(merchantHeaderFormDTO.getMerchantHeaderList());
		} catch (Exception e) {
			logger.error(this.getClass().getName()+".testQuery()", e);
		}
	}
	
	/**
	 * Purpose:刪除特店表頭方法測試
	 * @author jasonzhou
	 * @return void
	 */
	public void testDelete(){
		try {
			SessionContext sessionContext = new SessionContext();
			MerchantHeaderFormDTO merchantHeaderFormDTO = new MerchantHeaderFormDTO();
			merchantHeaderFormDTO.setMerchantHeaderId("1");
			sessionContext.setRequestParameter(merchantHeaderFormDTO);
			merchantHeaderFormDTO = (MerchantHeaderFormDTO) this.merchantHeaderService.delete(sessionContext).getResponseResult();
			//assertNotNull(merchantHeaderFormDTO);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+".testDelete()", e);
		}
	}
	
	/**
	 * Purpose:獲取客戶特店信息方法測試
	 * @author jasonzhou
	 * @return void
	 */
	public void testGetMerchantNewInfo(){
		try {
			SessionContext sessionContext = new SessionContext();
			MerchantHeaderFormDTO merchantHeaderFormDTO = new MerchantHeaderFormDTO();
			merchantHeaderFormDTO.setMerchantCode("zhengwei1");
			sessionContext.setRequestParameter(merchantHeaderFormDTO);
			merchantHeaderFormDTO = (MerchantHeaderFormDTO) this.merchantHeaderService.getMerchantInfo(sessionContext).getResponseResult();
			//assertNotNull(merchantHeaderFormDTO);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+".testGetMerchantNewInfo", e);
		}
	}
	/**
	 * @return the merchantHeaderService
	 */
	public IMerchantHeaderService getMerchantHeaderService() {
		return merchantHeaderService;
	}

	/**
	 * @param merchantHeaderService the merchantHeaderService to set
	 */
	public void setMerchantHeaderService(IMerchantHeaderService merchantHeaderService) {
		this.merchantHeaderService = merchantHeaderService;
	}
}
