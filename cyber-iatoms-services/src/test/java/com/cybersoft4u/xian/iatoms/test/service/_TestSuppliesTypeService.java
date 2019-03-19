package com.cybersoft4u.xian.iatoms.test.service;

import java.math.BigDecimal;

import org.springframework.util.CollectionUtils;

import junit.framework.Assert;
import cafe.core.bean.identity.LogonUser;
import cafe.core.context.SessionContext;
import cafe.core.service.ServiceException;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmSuppliesDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.SuppliesTypeFormDTO;
import com.cybersoft4u.xian.iatoms.services.ISuppliesTypeService;
import com.cybersoft4u.xian.iatoms.services.dao.impl.SuppliesTypeDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmSupplies;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;
/**
 * Purpose: 耗材品項維護service單元測試
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2017/5/10
 * @MaintenancePersonnel HermanWang
 */
public class _TestSuppliesTypeService extends AbstractTestCase {
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestSuppliesTypeService.class);
	/**
	 * 注入suppliesTypeService
	 */
	public ISuppliesTypeService suppliesTypeService;
	/**
	 * 注入suppliesDAO
	 */
	public SuppliesTypeDAO suppliesDAO;

	/**
	 * 無參構造
	 */
	@SuppressWarnings("deprecation")
	public _TestSuppliesTypeService() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	/**
	 * Purpose:測試耗材service的查詢方法
	 * @author HermanWang
	 * @return void
	 */
	public void testQuery(){
		try {
			SessionContext  ctx =  new SessionContext();
			/* 查詢結果有值情況  */
			SuppliesTypeFormDTO suppliesFormDTO = new SuppliesTypeFormDTO();
			//查詢條件 客戶
			suppliesFormDTO.setQueryCustomerId("1475140315196-0142");
			//查詢條件 耗材分類
			suppliesFormDTO.setQuerySuppliesCode("SUPPLIES_TYPE02");
			//查詢條件 耗材名稱
			suppliesFormDTO.setQuerySuppliesName("1DFFDDFDFD");
			//查詢條件 行數
			suppliesFormDTO.setRows(10);
			//查詢條件 頁數
			suppliesFormDTO.setPage(1);
			//查詢條件 排序的列
			suppliesFormDTO.setSort(null);
			//查詢條件 排序方式
			suppliesFormDTO.setorder(null);
			ctx.setRequestParameter(suppliesFormDTO);
			ctx = suppliesTypeService.query(ctx);
			suppliesFormDTO = (SuppliesTypeFormDTO) ctx.getResponseResult();
			if(!CollectionUtils.isEmpty(suppliesFormDTO.getList())) {
				Assert.assertNotNull(suppliesFormDTO.getList());
			} else {
				Assert.assertTrue(IAtomsMessageCode.DATA_NOT_FOUND.equals(ctx.getReturnMessage().getCode()));
			}
			/* 查詢結果為空情況  */
			SuppliesTypeFormDTO suppliesFormDTO1 = new SuppliesTypeFormDTO();
			//查詢條件 客戶
			suppliesFormDTO1.setQueryCustomerId("1475140315196-0000");
			//查詢條件 耗材分類
			suppliesFormDTO1.setQuerySuppliesCode("SUPPLIES_TYPE123");
			//查詢條件 耗材名稱
			suppliesFormDTO1.setQuerySuppliesName("1DFFDDFDFD123");
			//查詢條件 行數
			suppliesFormDTO1.setRows(10);
			//查詢條件 頁數
			suppliesFormDTO1.setPage(1);
			//查詢條件 排序的列
			suppliesFormDTO1.setSort(null);
			//查詢條件 排序方式
			suppliesFormDTO1.setorder(null);
			ctx.setRequestParameter(suppliesFormDTO1);
			ctx = suppliesTypeService.query(ctx);
			suppliesFormDTO1 = (SuppliesTypeFormDTO) ctx.getResponseResult();
			if(!CollectionUtils.isEmpty(suppliesFormDTO1.getList())) {
				Assert.assertNotNull(suppliesFormDTO1.getList());
			} else {
				Assert.assertTrue(IAtomsMessageCode.DATA_NOT_FOUND.equals(ctx.getReturnMessage().getCode()));
			}
		} catch (ServiceException e) {
			LOGGER.error(this.getClass().getName()+".testQuery()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:測試耗材service的保存方法
	 * @author HermanWang
	 * @return void
	 */
	public void testSave(){
		try {
			SessionContext  ctx =  new SessionContext();
			SuppliesTypeFormDTO suppliesFormDTO = new SuppliesTypeFormDTO();
			DmmSuppliesDTO dmmSuppliesDTO = new DmmSuppliesDTO();
			LogonUser logonUser = new LogonUser();
			logonUser.setId("1473311383499-0060");
			logonUser.setName("王佳強");
			//測試同一客戶下的耗材品新增 重複
			dmmSuppliesDTO.setSuppliesId("");
			//公司id
			dmmSuppliesDTO.setCompanyId("1475140315196-0142");
			//耗材名稱
			dmmSuppliesDTO.setSuppliesName("1DFFDDFDFD");
			//耗材分類 code
			dmmSuppliesDTO.setSuppliesType("SUPPLIES_TYPE02");
			//耗材單價
			dmmSuppliesDTO.setPrice(new BigDecimal(6));
			suppliesFormDTO.setSuppliesDTO(dmmSuppliesDTO);
			suppliesFormDTO.setLogonUser(logonUser);
			ctx.setRequestParameter(suppliesFormDTO);
			ctx = suppliesTypeService.save(ctx);
			boolean isRepeat = this.suppliesDAO.isCheck(dmmSuppliesDTO.getCompanyId(), dmmSuppliesDTO.getSuppliesName(), dmmSuppliesDTO.getSuppliesId());
			if(isRepeat) {
				Assert.assertTrue(IAtomsMessageCode.SUPPLIES_NAME_REPEAT.equals(ctx.getReturnMessage().getCode()));
			} else {
				Assert.assertTrue(IAtomsMessageCode.INSERT_SUCCESS.equals(ctx.getReturnMessage().getCode()));
			}
			
			//測試同一客戶下的耗材品修改 重複
			dmmSuppliesDTO.setSuppliesId("1493200385011-0221");
			//公司id
			dmmSuppliesDTO.setCompanyId("1493183440938-0357");
			//耗材名稱
			dmmSuppliesDTO.setSuppliesName("555");
			//耗材分類 code
			dmmSuppliesDTO.setSuppliesType("PowerCord");
			//耗材單價
			dmmSuppliesDTO.setPrice(new BigDecimal(6));
			suppliesFormDTO.setSuppliesDTO(dmmSuppliesDTO);
			suppliesFormDTO.setLogonUser(logonUser);
			ctx.setRequestParameter(suppliesFormDTO);
			ctx = suppliesTypeService.save(ctx);
			isRepeat = this.suppliesDAO.isCheck(dmmSuppliesDTO.getCompanyId(), dmmSuppliesDTO.getSuppliesName(), dmmSuppliesDTO.getSuppliesId());
			if(isRepeat) {
				Assert.assertTrue(IAtomsMessageCode.SUPPLIES_NAME_REPEAT.equals(ctx.getReturnMessage().getCode()));
			} else {
				DmmSupplies supplies = this.suppliesDAO.findByPrimaryKey(DmmSupplies.class, dmmSuppliesDTO.getSuppliesId());
				if(supplies != null) {
					Assert.assertTrue(IAtomsMessageCode.UPDATE_SUCCESS.equals(ctx.getReturnMessage().getCode()));
				} else {
					Assert.assertTrue(IAtomsMessageCode.UPDATE_FAILURE.equals(ctx.getReturnMessage().getCode()));
				}
			}
		} catch (ServiceException e) {
			LOGGER.error(this.getClass().getName()+".testSave()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:測試耗材service的刪除方法
	 * @author HermanWang
	 * @return void
	 */
	public void testDelete(){
		try {
			SessionContext  ctx =  new SessionContext();
			SuppliesTypeFormDTO suppliesFormDTO = new SuppliesTypeFormDTO();
			//傳入的刪除id為空 測試刪除
			suppliesFormDTO.setSuppliesId("");
			ctx.setRequestParameter(suppliesFormDTO);
			ctx = suppliesTypeService.delete(ctx);
			Assert.assertTrue(IAtomsMessageCode.DELETE_FAILURE.equals(ctx.getReturnMessage().getCode()));
			
			//傳入的刪除id(判斷傳入的id是否正確)
			suppliesFormDTO.setSuppliesId("1493200343950-02192");
			ctx.setRequestParameter(suppliesFormDTO);
			DmmSupplies supplies = this.suppliesDAO.findByPrimaryKey(DmmSupplies.class, suppliesFormDTO.getSuppliesId());
			ctx = suppliesTypeService.delete(ctx);
			if(supplies != null) {
				Assert.assertTrue(IAtomsMessageCode.DELETE_SUCCESS.equals(ctx.getReturnMessage().getCode()));
			} else {
				Assert.assertTrue(IAtomsMessageCode.DELETE_FAILURE.equals(ctx.getReturnMessage().getCode()));
			}
		} catch (ServiceException e) {
			LOGGER.error(this.getClass().getName()+".testDelete()", e);
			e.printStackTrace();
		}
	}
	/**
	 * @return the suppliesTypeService
	 */
	public ISuppliesTypeService getSuppliesTypeService() {
		return suppliesTypeService;
	}
	/**
	 * @param suppliesTypeService the suppliesTypeService to set
	 */
	public void setSuppliesTypeService(ISuppliesTypeService suppliesTypeService) {
		this.suppliesTypeService = suppliesTypeService;
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
