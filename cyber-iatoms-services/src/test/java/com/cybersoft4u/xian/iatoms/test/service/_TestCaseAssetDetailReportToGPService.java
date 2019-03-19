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
 * Purpose：批次報表 - 案件設備明細（環匯格式）報表的單元測試
 * @author ElvaHe
 * @since JDK1.6
 * @date 2017/05/31
 * @MaintenancePersonnel ElvaHe
 */
public class _TestCaseAssetDetailReportToGPService extends AbstractTestCase{
	
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestCaseAssetDetailReportToGPService.class);

	/**
	 * 注入集成的接口
	 */
	private IReportService caseAssetDetailReportToGPService;
	
	/**
	 * 有參構造
	 */
	public _TestCaseAssetDetailReportToGPService(){
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	
	/**
	 * Purpose：測試發送郵件的公用方法
	 */
	public void testSendReportMail(){
		try {
			//報表名稱
			String reportCode = IAtomsConstants.REPORT_NAME_CASE_ASSET_DETAIL;
			//郵件發送時間
			Date sendDate = DateTimeUtils.getCurrentDate();
			
			//不存在符合條件的報表信息
			//客戶編碼
			String customerId = "12qwaszx";
			this.caseAssetDetailReportToGPService.sendReportMail(sendDate, customerId, reportCode);
			Assert.assertTrue(true);
			
			//存在符合條件的報表信息 - 當前時間為前半月日期
			sendDate = DateTimeUtils.toDate("2017/05/15");
			this.caseAssetDetailReportToGPService.sendReportMail(sendDate, customerId, reportCode);
			Assert.assertTrue(true);
			
			//存在符合條件的報表信息 - 當前時間為后半月日期
			sendDate = DateTimeUtils.toDate("2017/05/16");
			this.caseAssetDetailReportToGPService.sendReportMail(sendDate, customerId, reportCode);
			Assert.assertTrue(true);
		} catch (Exception e) {
			LOGGER.error("_TestCaseAssetDetailReportToGPService.testSendReportMail()", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the caseAssetDetailReportToGPService
	 */
	public IReportService getCaseAssetDetailReportToGPService() {
		return caseAssetDetailReportToGPService;
	}

	/**
	 * @param caseAssetDetailReportToGPService the caseAssetDetailReportToGPService to set
	 */
	public void setCaseAssetDetailReportToGPService(
			IReportService caseAssetDetailReportToGPService) {
		this.caseAssetDetailReportToGPService = caseAssetDetailReportToGPService;
	}
	
}
