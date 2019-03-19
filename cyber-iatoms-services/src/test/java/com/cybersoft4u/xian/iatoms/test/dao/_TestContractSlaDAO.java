package com.cybersoft4u.xian.iatoms.test.dao;

import java.util.List;

import junit.framework.Assert;
import cafe.core.dao.DataAccessException;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.bean.dto.ContractSlaDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractSlaFormDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IContractSlaDAO;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;

public class _TestContractSlaDAO extends AbstractTestCase{

	private IContractSlaDAO contractSlaDAO;
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog logger = (CafeLog)CafeLogFactory.getLog(_TestContractSlaDAO.class);
	
	/**
	 * 無參構造
	 */
	public _TestContractSlaDAO() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	
	/**
	 * Purpose:測試查詢方法
	 * @author Amanda Wang
	 * @return void
	 */
	public void testListBy() {
		try {
			ContractSlaFormDTO contractSlaFormDTO = new ContractSlaFormDTO();
			contractSlaFormDTO.setQueryContractId("22");
			contractSlaFormDTO.setQueryCustomerId("2");
		//	contractSlaFormDTO.setQueryLocationName("4");
			contractSlaFormDTO.setRows(10);
			contractSlaFormDTO.setPage(1);
			contractSlaFormDTO.setSort("contract_id");
			contractSlaFormDTO.setOrder("asc");
			
		//	List<BimSlaDTO> list = contractSlaDAO.listBy(contractSlaFormDTO);
		//	Assert.assertEquals(1, list.size());
		} catch (DataAccessException e) {
			logger.error(this.getClass().getName()+".testListBy()", e);
		}
	}
	
	/**
	 * Purpose:測試检查主键是否重复
	 * @author Amanda Wang
	 * @return void
	 */
	public void testCheckRepeat() {
		try {
			ContractSlaDTO contractSlaDTO = new ContractSlaDTO();
			String ticketType = "1";
			String location = "4";
		//	contractSlaDTO = contractSlaDAO.getContractSlaBy(ticketType, location);
		//	assertNotNull(contractSlaDTO);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+".testCheckRepeat()", e);
			
		}
	}
	/**
	 * Purpose:測試檢查聯合唯一鍵是否重複
	 * @author Amanda Wang
	 * @return void
	 */
	public void testCheck() {
		try {
			ContractSlaDTO contractSlaDTO = new ContractSlaDTO();
			String ticketType = "1";
			String location = "4";
		//	contractSlaDTO = contractSlaDAO.getContractSlaBy(ticketType, location);
		//	assertNotNull(contractSlaDTO);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+".testCheck()", e);
			
		}
	}
	/**
	 * Purpose:測試通過客戶和合同獲取其SLA信息
	 * @author Amanda Wang
	 * @return void
	 */
	public void testGetSlaByCustomerAndContract() {
		try {
			String contractId = "22";
		//	Integer count = contractSlaDAO.getCountByContractId(contractId);
		//	Assert.assertEquals(contractId, count);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+".testGetSlaByCustomerAndContract()", e);
			
		}
	}
	
	/**
	 * Purpose:測試查询总笔数
	 * @author Amanda Wang
	 * @return void
	 */
	public void testCount() {
		try {
			String queryCustomerId = "1"; 
			String queryContractId = "22"; 
			String queryLocationName = "7";
			Integer row = 1;
		//	Integer count = contractSlaDAO.count(queryCustomerId, queryContractId, queryLocationName);
		//	Assert.assertEquals(row, count);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+".testCount()", e);
			
		}
	}
	/**
	 * @return the contractSlaDAO
	 */
	public IContractSlaDAO getContractSlaDAO() {
		return contractSlaDAO;
	}

	/**
	 * @param contractSlaDAO the contractSlaDAO to set
	 */
	public void setContractSlaDAO(IContractSlaDAO contractSlaDAO) {
		this.contractSlaDAO = contractSlaDAO;
	}
	
}
