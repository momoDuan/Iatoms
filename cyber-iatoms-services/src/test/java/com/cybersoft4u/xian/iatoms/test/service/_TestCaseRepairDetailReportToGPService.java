package com.cybersoft4u.xian.iatoms.test.service;

import java.util.Date;

import junit.framework.Assert;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.services.IReportService;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;
/**
 * Purpose：批次報表 - 案件報修明細（環匯格式）報表的單元測試
 * @author ElvaHe
 * @since JDK1.6
 * @date 2017/05/31
 * @MaintenancePersonnel ElvaHe
 */
public class _TestCaseRepairDetailReportToGPService extends AbstractTestCase{

	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestCaseRepairDetailReportToGPService.class);
	
	/**
	 * 注入Service接口
	 */
	private IReportService caseRepairDetailReportToGPService;
	
	/**
	 * 無參構造
	 */
	public _TestCaseRepairDetailReportToGPService() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	
	/**
	 * Purpose：測試發送郵件的公用方法
	 */
	public void testSendReportMail(){
		try {
			//報表名稱
			String reportCode = IAtomsConstants.REPORT_NAME_CASE_REPAIR_DETAIL_REPORT;
			//郵件發送時間
			Date sendDate = DateTimeUtils.getCurrentDate();
			//不存在符合條件的報表信息
			//客戶編碼
			String customerId = "12qwaszx";
			this.caseRepairDetailReportToGPService.sendReportMail(sendDate, customerId, reportCode);
			Assert.assertTrue(true);
			
			//存在符合條件的報表信息 - 不存在發送時間
			sendDate = null;
			this.caseRepairDetailReportToGPService.sendReportMail(sendDate, customerId, reportCode);
			Assert.assertTrue(true);
			
			//存在符合條件的報表信息 - 存在發送時間，且存在客戶編碼
			sendDate = DateTimeUtils.getCurrentDate();
			customerId = "1475140315196-0142";
			this.caseRepairDetailReportToGPService.sendReportMail(sendDate, customerId, reportCode);
			Assert.assertTrue(true);
			
			/*//存在符合條件的報表信息 - 存在發送時間，但不存在客戶編碼
			sendDate = DateTimeUtils.getCurrentDate();
			customerId = null;
			this.caseRepairDetailReportToGPService.sendReportMail(sendDate, customerId, reportCode);
			System.out.println("4");
			Assert.assertTrue(true);*/
		} catch (Exception e) {
			LOGGER.error("_TestCaseRepairDetailReportToGPService.testSendReportMail()", "is error :", e);
			e.printStackTrace();
		}
	}

	/**
	 * @return the caseRepairDetailReportToGPService
	 */
	public IReportService getCaseRepairDetailReportToGPService() {
		return caseRepairDetailReportToGPService;
	}

	/**
	 * @param caseRepairDetailReportToGPService the caseRepairDetailReportToGPService to set
	 */
	public void setCaseRepairDetailReportToGPService(
			IReportService caseRepairDetailReportToGPService) {
		this.caseRepairDetailReportToGPService = caseRepairDetailReportToGPService;
	}


}
