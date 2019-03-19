package com.cybersoft4u.xian.iatoms.test.dao;

import java.util.List;

import junit.framework.Assert;

import org.springframework.util.CollectionUtils;

import cafe.core.dao.DataAccessException;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmSystemLoggingDTO;
import com.cybersoft4u.xian.iatoms.services.dao.impl.SystemLogDAO;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;
/**
 * Purpose: 系統logDAO的單元測試
 * @author amandawang
 * @since  JDK 1.6
 * @date   2017/5/24
 * @MaintenancePersonnel amandawang
 */
public class _TestSystemLogDAO extends AbstractTestCase {
	
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestSystemLogDAO.class);
	/**
	 * 注入系統logDAO
	 */
	private SystemLogDAO systemLogDAO;

	/**
	 * Constructor: 無參構造函數
	 */
	@SuppressWarnings("deprecation")
	public _TestSystemLogDAO() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}

	/**
	 * Purpose: 測試查詢方法
	 * @author amandawang
	 * @return void
	 */
	public void testListBy() {
		try {
			String account = "amandawang";
			String formDate = "2017/05/24";
			String toDate = "2017/05/24";
			String sort = "operationTime";
			String order = "asc";		
			Integer currentPage = 0;
			Integer pageSize = 10;
			List<AdmSystemLoggingDTO> list = this.systemLogDAO.listBy(account, formDate, toDate, sort, order, currentPage, pageSize);
			if(!CollectionUtils.isEmpty(list)){
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
	 * Purpose: 測試查詢數量方法
	 * @author amandawang
	 * @return void
	 */
	/*public void testCount() {
		try {
			String account = "amandawang";
			String formDate = "2017/05/24";
			String toDate = "2017/05/24";
			Integer count = this.systemLogDAO.count(account, formDate, toDate);
			if(count != 0){
				Assert.assertEquals(count, count);
			} else {
				Assert.assertFalse(count != 0);
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testCount()", e);
			e.printStackTrace();
		}
	}*/

	/**
	 * @return the systemLogDAO
	 */
	public SystemLogDAO getSystemLogDAO() {
		return systemLogDAO;
	}

	/**
	 * @param systemLogDAO the systemLogDAO to set
	 */
	public void setSystemLogDAO(SystemLogDAO systemLogDAO) {
		this.systemLogDAO = systemLogDAO;
	}
	
	
}
