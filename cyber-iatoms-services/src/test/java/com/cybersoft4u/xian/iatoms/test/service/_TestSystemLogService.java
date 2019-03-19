package com.cybersoft4u.xian.iatoms.test.service;

import junit.framework.Assert;

import org.springframework.util.CollectionUtils;

import cafe.core.context.SessionContext;
import cafe.core.dao.DataAccessException;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.SystemLogFormDTO;
import com.cybersoft4u.xian.iatoms.services.ISystemLogService;
import com.cybersoft4u.xian.iatoms.services.dao.impl.SystemLogDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmSystemLogging;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;
/**
 * Purpose: 系統logService的單元測試
 * @author amandawang
 * @since  JDK 1.6
 * @date   2017/5/24
 * @MaintenancePersonnel amandawang
 */
public class _TestSystemLogService extends AbstractTestCase {

	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestSystemLogService.class);
	/**
	 * 注入warehouseService
	 */
	public ISystemLogService systemLogService;
	/**
	 * 注入系統logDAO
	 */
	private SystemLogDAO systemLogDAO;
	/**
	 * 無參構造
	 */
	public _TestSystemLogService() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	/**
	 * Purpose:測試初始化方法
	 * @author amandawang
	 * @return void
	 */
	public void testInit() {
		try {
			SessionContext  ctx =  new SessionContext();
			SystemLogFormDTO formDTO = new SystemLogFormDTO();
			ctx.setRequestParameter(formDTO);
			systemLogService.init(ctx);
			Assert.assertTrue(IAtomsMessageCode.INIT_PAGE_SUCCESS.equals(ctx.getReturnMessage().getCode()));
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testInit()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:測試查詢方法
	 * @author amandawang
	 * @return void
	 */
	public void testQuery() {
		try {
			SessionContext  ctx =  new SessionContext();
			SystemLogFormDTO formDTO = new SystemLogFormDTO();
			
			//查詢條件
			formDTO.setQueryFromDate("2017/05/24");
			formDTO.setQueryToDate("2017/05/24");
			formDTO.setQueryAccount("amandawang");
			formDTO.setSort("operationTime");
			formDTO.setOrder("asc");
			formDTO.getPageNavigation().setCurrentPage(0);
			formDTO.getPageNavigation().setPageSize(10);
			ctx.setRequestParameter(formDTO);
			systemLogService.query(ctx);
			formDTO = (SystemLogFormDTO) ctx.getResponseResult();
			if(!CollectionUtils.isEmpty(formDTO.getList())) {
				Assert.assertNotNull(formDTO.getList());
			} else {
				Assert.assertTrue(formDTO.getList().size() == 0);
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testQuery()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:測試打開log執行內容的對話框方法
	 * @author amandawang
	 * @return void
	 */
	public void testOpenLogDialog() {
		try {
			SessionContext  ctx =  new SessionContext();
			SystemLogFormDTO formDTO = new SystemLogFormDTO();
			Integer logId = 50571;
			formDTO.setLogId(logId);
			
			ctx.setRequestParameter(formDTO);
			ctx = systemLogService.openLogDialog(ctx);
			AdmSystemLogging admSystemLogging = (AdmSystemLogging) this.systemLogDAO.findByPrimaryKey(AdmSystemLogging.class, logId);

			if(admSystemLogging != null) {
				Assert.assertTrue(IAtomsMessageCode.INIT_PAGE_SUCCESS.equals(ctx.getReturnMessage().getCode()));
			} else {
				Assert.assertTrue(admSystemLogging == null);
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testOpenLogDialog()", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the systemLogService
	 */
	public ISystemLogService getSystemLogService() {
		return systemLogService;
	}

	/**
	 * @param systemLogService the systemLogService to set
	 */
	public void setSystemLogService(ISystemLogService systemLogService) {
		this.systemLogService = systemLogService;
	}

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
