package com.cybersoft4u.xian.iatoms.test.service;

import cafe.core.context.SessionContext;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractSlaFormDTO;
import com.cybersoft4u.xian.iatoms.services.IContractSlaService;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;

public class _TestContractSlaService extends AbstractTestCase{

	/**
	 * 日志記錄物件
	 */
	private static final CafeLog logger = (CafeLog)CafeLogFactory.getLog(_TestContractSlaService.class);
	
	/**
	 * 合約SLAService
	 */
	private IContractSlaService contractSlaService;

	/**
	 * Constructor:無參構造函數
	 */
	public _TestContractSlaService() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}

	/**
	 * Purpose:測試複製方法
	 * @author Amanda Wang
	 * @return void
	 */
	public void testCopy() {
		try {
			SessionContext  ctx =  new SessionContext();
			ContractSlaFormDTO formDTO = new ContractSlaFormDTO();
			formDTO.setContractId("22");
			formDTO.setCopyContractId("FDFDF");
			ctx.setRequestParameter(formDTO);
			//contractSlaService.copy(ctx);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+".testCopy()",e);
		}
	}
	
	/**
	 * Purpose:測試刪除方法
	 * @author Amanda Wang
	 * @return void
	 */
	public void testDelete() {
		try {
			SessionContext  ctx =  new SessionContext();
			ContractSlaFormDTO formDTO = new ContractSlaFormDTO();
			formDTO.setContractId("22");
			formDTO.setTicketType("2");
			//formDTO.setImportance("2");
			formDTO.setLocation("7");
			ctx.setRequestParameter(formDTO);
		//	contractSlaService.delete(ctx);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+".testDelete()",e);
		}
	}
	
	/**
	 * Purpose:測試保存方法
	 * @author Amanda Wang
	 * @return void
	 */
	public void testSave() {
		try {
			SessionContext  ctx =  new SessionContext();
			ContractSlaFormDTO formDTO = new ContractSlaFormDTO();
			//formDTO.setIsEdit(true); 
			formDTO.setContractId("123231"); 
			ctx.setRequestParameter(formDTO);
		//	contractSlaService.save(ctx);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+".testSave()",e);
		}
	}
	
	/**
	 * Purpose:測試查詢方法
	 * @author Amanda Wang
	 * @return void
	 */
	public void testQuery() {
		try {
			SessionContext  ctx =  new SessionContext();
			ContractSlaFormDTO formDTO = new ContractSlaFormDTO();
			formDTO.setQueryCustomerId("1"); 
			formDTO.setQueryContractId("22"); 
			//formDTO.setQueryLocationName("7");
			formDTO.setRows(10);
			formDTO.setPage(1);
			formDTO.setSort("");
			formDTO.setOrder("contract_id");
			ctx.setRequestParameter(formDTO);
		//	contractSlaService.query(ctx);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+".testQuery()",e);
		}
	}
	
	/**
	 * Purpose:測試初始化方法
	 * @author Amanda Wang
	 * @return void
	 */
	public void testInitEdit() {
		try {
			SessionContext  ctx =  new SessionContext();
			ContractSlaFormDTO formDTO = new ContractSlaFormDTO();
			formDTO.setContractId("22");
			formDTO.setTicketType("2");
		//	formDTO.setImportance("2");
			formDTO.setLocation("7");
			ctx.setRequestParameter(formDTO);
		//	contractSlaService.initEdit(ctx);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+".testInitEdit()",e);
		}
	}
	/**
	 * Purpose:測試初始化方法
	 * @author Amanda Wang
	 * @return void
	 */
	public void testInitCopy() {
		try {
			SessionContext  ctx =  new SessionContext();
			ContractSlaFormDTO formDTO = new ContractSlaFormDTO();
			ctx.setRequestParameter(formDTO);
		//	contractSlaService.initCopy(ctx);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+".testInitCopy()",e);
		}
	}
	
	/**
	 * @return the contractSlaService
	 */
	public IContractSlaService getContractSlaService() {
		return contractSlaService;
	}

	/**
	 * @param contractSlaService the contractSlaService to set
	 */
	public void setContractSlaService(IContractSlaService contractSlaService) {
		this.contractSlaService = contractSlaService;
	}

}
