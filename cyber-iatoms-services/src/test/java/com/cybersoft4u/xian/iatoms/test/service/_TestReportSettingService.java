package com.cybersoft4u.xian.iatoms.test.service;


import junit.framework.Assert;

import cafe.core.context.SessionContext;
import cafe.core.service.ServiceException;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ReportSettingDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ReportSettingFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.WarehouseFormDTO;
import com.cybersoft4u.xian.iatoms.services.IReportSettingService;
import com.cybersoft4u.xian.iatoms.services.dao.impl.ReportSettingDAO;
import com.cybersoft4u.xian.iatoms.services.dao.impl.ReportSettingDetailDAO;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;

/**
 * Purpose: 報表Service的單元測試
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2016/8/26
 * @MaintenancePersonnel ElvaHe
 */
public class _TestReportSettingService extends AbstractTestCase {
	
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestReportSettingService.class);
	
	/**
	 * 注入Service
	 */
	private IReportSettingService reportSettingService;
	/**
	 * 報表發送功能設定DAO
	 */
	private ReportSettingDAO reportSettingDAO;
	/**
	 * 報表明細DAO
	 */
	private ReportSettingDetailDAO reportSettingDetailDAO;
	
	
	/**
	 * 無參構造
	 */
	public _TestReportSettingService() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	/**
	 * Purpose:測試查詢方法
	 * @author HaimingWang
	 * @return void
	 */
	public void testQuery(){
		try {
			SessionContext  ctx =  new SessionContext();
			ReportSettingFormDTO formDTO = new ReportSettingFormDTO();
			formDTO.setQueryCustomerId("1472436594570-0041");
			formDTO.setQueryReportCode("1");
			formDTO.setRows(10);
			formDTO.setPage(1);
			formDTO.setSort("");
			formDTO.setOrder("");
			ctx.setRequestParameter(formDTO);
			reportSettingService.query(ctx);
			//查詢結果的總記錄數
			Integer totalSize = this.reportSettingDAO.codeCount(formDTO.getQueryCustomerId(), formDTO.getQueryReportCode());
			//若存在查詢結果
			if (totalSize.intValue() > 0 ) {
				
			} else {
				System.out.println("查無資料");
				Assert.assertTrue(IAtomsMessageCode.DATA_NOT_FOUND.equals(ctx.getReturnMessage().getCode()));
			}
		} catch (ServiceException e) {
			LOGGER.error("_TestReportSettingService.testQuery()", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試初始化修改的方法
	 * @author HaimingWang
	 * @return void
	 */
	public void testInitEdit(){
		try {
			SessionContext  ctx =  new SessionContext();
			ReportSettingFormDTO formDTO = new ReportSettingFormDTO();
			formDTO.setId("1472436594570-0041");
			ctx.setRequestParameter(formDTO);
			//reportSettingService.initEdit(ctx);
		} catch (ServiceException e) {
			System.out.println("_TestWarehouseService.testInitEdit() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試保存信息的方法
	 * @author HaimingWang
	 * @return void
	 */
	public void testSave(){
		try {
		SessionContext  ctx =  new SessionContext();
		ReportSettingFormDTO formDTO = new ReportSettingFormDTO();
		ReportSettingDTO reportSettingDTO = new ReportSettingDTO();
		reportSettingDTO.setSettingId("1472436594570-0141");
		formDTO.setReportSettingDTO(reportSettingDTO);
		//ctx.setRequestParameter(formDTO);
		//reportSettingService.save(ctx);
		} catch (ServiceException e) {
			System.out.println("_TestWarehouseService.testSave() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試刪除的方法
	 * @author HaimingWang
	 * @return void
	 */
	public void testDelete(){
		try {
		SessionContext  ctx =  new SessionContext();
		WarehouseFormDTO formDTO = new WarehouseFormDTO();
		formDTO.setWarehouseId("163");
		ctx.setRequestParameter(formDTO);
		//reportSettingService.delete(ctx);
		} catch (ServiceException e) {
			System.out.println("_TestWarehouseService.testDelete() is error" + e);
			e.printStackTrace();
		}
	}
	/**
	 * @return the reportSettingService
	 */
	public IReportSettingService getReportSettingService() {
		return reportSettingService;
	}

	/**
	 * @param reportSettingService the reportSettingService to set
	 */
	public void setReportSettingService(IReportSettingService reportSettingService) {
		this.reportSettingService = reportSettingService;
	}
	/**
	 * @return the reportSettingDAO
	 */
	public ReportSettingDAO getReportSettingDAO() {
		return reportSettingDAO;
	}
	/**
	 * @param reportSettingDAO the reportSettingDAO to set
	 */
	public void setReportSettingDAO(ReportSettingDAO reportSettingDAO) {
		this.reportSettingDAO = reportSettingDAO;
	}
	/**
	 * @return the reportSettingDetailDAO
	 */
	public ReportSettingDetailDAO getReportSettingDetailDAO() {
		return reportSettingDetailDAO;
	}
	/**
	 * @param reportSettingDetailDAO the reportSettingDetailDAO to set
	 */
	public void setReportSettingDetailDAO(
			ReportSettingDetailDAO reportSettingDetailDAO) {
		this.reportSettingDetailDAO = reportSettingDetailDAO;
	}
	
}
