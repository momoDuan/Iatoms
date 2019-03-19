package com.cybersoft4u.xian.iatoms.test.service;
import junit.framework.Assert;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.identity.LogonUser;
import cafe.core.context.SessionContext;
import cafe.core.service.ServiceException;

import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.MerchantDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.MerchantFormDTO;
import com.cybersoft4u.xian.iatoms.services.IMerchantService;
import com.cybersoft4u.xian.iatoms.services.dao.IMerchantDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimMerchant;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;

/**
 * Purpose: 客戶特店Service的單元測試
 * @author DavidZheng
 * @since  JDK 1.6
 * @date   2016/6/22
 * @MaintenancePersonnel DavidZheng
 */
public class _TestMerchantNewService extends AbstractTestCase {
	/**
	 * 注入merchantNewService
	 */
	public IMerchantService merchantNewService;
	/**
	 * 注入merchantNewDAO
	 */
	public IMerchantDAO merchantNewDAO;

	/**
	 * 無參構造
	 */
	@SuppressWarnings("deprecation")
	public _TestMerchantNewService() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}

	/**
	 * Purpose:測試查詢方法
	 * @author DavidZheng
	 * @return void
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void testQuery(){
		try {
		SessionContext  ctx =  new SessionContext();
		MerchantFormDTO formDTO = new MerchantFormDTO();
		formDTO.setQueryCompanyId("1");
		formDTO.setQueryName("d");
		formDTO.setQueryMerchantCode("123123");
		formDTO.setRows(10);
		formDTO.setPage(1);
		formDTO.setSort(null);
		formDTO.setOrder(null);
		ctx.setRequestParameter(formDTO);
		ctx = merchantNewService.query(ctx);
		formDTO = (MerchantFormDTO) ctx.getResponseResult();
		if(!CollectionUtils.isEmpty(formDTO.getList())) {
			Assert.assertNotNull(formDTO.getList());
		} else {
			Assert.assertTrue(IAtomsMessageCode.DATA_NOT_FOUND.equals(ctx.getReturnMessage().getCode()));
		}
		} catch (ServiceException e) {
			System.out.println("_TestMerchantNewService.testQuery() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試保存信息的方法
	 * @author DavidZheng
	 * @return void
	 */
	//@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testSaveMerchantNew(){
		try {
			SessionContext ctx =  new SessionContext();
			MerchantFormDTO formDTO = new MerchantFormDTO();
			MerchantDTO merchantNewDTO = new MerchantDTO();
			LogonUser logonUser = new LogonUser();
			
			//測試修改
			//獲取主鍵
			merchantNewDTO.setMerchantId("jdisoaj");
			//獲取MId
			merchantNewDTO.setMerchantCode("newWarehouse");
			//客戶Id
			merchantNewDTO.setCompanyId("123212");
			formDTO.setBimMerchantDTO(merchantNewDTO);
			formDTO.setLogonUser(logonUser);
			ctx.setRequestParameter(formDTO);
			ctx = merchantNewService.save(ctx);
			if(logonUser == null) {
				Assert.assertTrue(IAtomsMessageCode.SESSION_INVALIDATED_TIME_OUT.equals(ctx.getReturnMessage().getCode()));
			} else {
				boolean isRepeat = this.merchantNewDAO.isCheck(merchantNewDTO.getMerchantCode(), merchantNewDTO.getMerchantId(), merchantNewDTO.getCompanyId());
				if(!isRepeat) {
					Assert.assertTrue(IAtomsMessageCode.MERCHANT_CODE_REPEAT.equals(ctx.getReturnMessage().getCode()));
				} else {
					BimMerchant bimMerchant = this.merchantNewDAO.findByPrimaryKey(BimMerchant.class, merchantNewDTO.getMerchantId());
					if(bimMerchant == null) {
						Assert.assertTrue(IAtomsMessageCode.UPDATE_FAILURE.equals(ctx.getReturnMessage().getCode()));
					} else {
						Assert.assertTrue(IAtomsMessageCode.UPDATE_SUCCESS.equals(ctx.getReturnMessage().getCode()));
					}
				}
			} 
			
			//測試新增
			//主鍵】
			merchantNewDTO.setMerchantId("");
			//獲取MId
			merchantNewDTO.setMerchantCode("newWarehouse");
			//客戶Id
			merchantNewDTO.setCompanyId("123212");
			formDTO.setBimMerchantDTO(merchantNewDTO);
			formDTO.setLogonUser(logonUser);
			ctx.setRequestParameter(formDTO);
			ctx = merchantNewService.save(ctx);
			if(logonUser == null) {
				Assert.assertTrue(IAtomsMessageCode.SESSION_INVALIDATED_TIME_OUT.equals(ctx.getReturnMessage().getCode()));
			} else {
				boolean isRepeat = this.merchantNewDAO.isCheck(merchantNewDTO.getMerchantCode(), merchantNewDTO.getMerchantId(), merchantNewDTO.getCompanyId());
				if(!isRepeat) {
					Assert.assertTrue(IAtomsMessageCode.MERCHANT_CODE_REPEAT.equals(ctx.getReturnMessage().getCode()));
				} else {
					Assert.assertTrue(IAtomsMessageCode.INSERT_SUCCESS.equals(ctx.getReturnMessage().getCode()));
				}
			} 
		} catch (ServiceException e) {
			System.out.println("_TestMerchantNewService.testSaveMerchantNew() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試初始化修改的方法
	 * @author DavidZheng
	 * @return void
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testInitEdit(){
		try {
			SessionContext  ctx =  new SessionContext();
			MerchantFormDTO formDTO = new MerchantFormDTO();
			formDTO.setMerchantId("163");
			ctx.setRequestParameter(formDTO);
			merchantNewService.initEdit(ctx);
		} catch (ServiceException e) {
			System.out.println("_TestMerchantNewService.testInitEdit() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試刪除的方法
	 * @author DavidZheng
	 * @return void
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testDelete(){
		try {
		SessionContext  ctx =  new SessionContext();
		MerchantFormDTO formDTO = new MerchantFormDTO();
		//無id刪除
		formDTO.setMerchantId("");
		ctx.setRequestParameter(formDTO);
		ctx = merchantNewService.delete(ctx);
		Assert.assertTrue(IAtomsMessageCode.DELETE_FAILURE.equals(ctx.getReturnMessage().getCode()));
		//有ID刪除
		formDTO.setMerchantId("1231231");
		ctx.setRequestParameter(formDTO);
		ctx = merchantNewService.delete(ctx);
		BimMerchant bimMerchant = this.merchantNewDAO.findByPrimaryKey(BimMerchant.class, formDTO.getMerchantId());
		if(bimMerchant != null) {
			Assert.assertTrue(IAtomsMessageCode.DELETE_SUCCESS.equals(ctx.getReturnMessage().getCode()));
		} else {
			Assert.assertTrue(IAtomsMessageCode.DELETE_FAILURE.equals(ctx.getReturnMessage().getCode()));
		}
		} catch (ServiceException e) {
			System.out.println("_TestMerchantNewService.testDelete() is error" + e);
			e.printStackTrace();
		}
	}
	/**
	 * @return the merchantNewService
	 */
	public IMerchantService getMerchantNewService() {
		return merchantNewService;
	}

	/**
	 * @param merchantNewService the merchantNewService to set
	 */
	public void setMerchantNewService(IMerchantService merchantNewService) {
		this.merchantNewService = merchantNewService;
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
