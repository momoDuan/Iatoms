package com.cybersoft4u.xian.iatoms.test.service;

import java.util.Date;

import junit.framework.Assert;

import cafe.core.util.DateTimeUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.utils.IAtomsDateTimeUtils;
import com.cybersoft4u.xian.iatoms.services.IReportService;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;

/**
 * Purpose：批次報表 - EDC合約到期提示報表的單元測試
 * @author ElvaHe
 * @since JDK1.6
 * @date 2017/05/31
 * @MaintenancePersonnel ElvaHe
 */
public class _TestEdcContractExpireService extends AbstractTestCase{
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestEdcContractExpireService.class);
	
	/**
	 * 注入Service接口
	 */
	private IReportService edcContractExpireService;
	/**
	 *系統中設定的幾個月 
	 */
	private int expireMonths;
	/**
	 * 無參構造
	 */
	public _TestEdcContractExpireService() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	
	/**
	 * Purpose：測試發送郵件的公用方法
	 */
	public void testSendReportMail(){/*
		try {
			//報表名稱
			String reportCode = IAtomsConstants.REPORT_NAME_EDC_CONTRACT_EXPIRE_INFO_REPORT;
			
			//不存在符合條件的報表信息
			//發送時間
			Date sendDate = DateTimeUtils.getCurrentDate();
			//客戶編碼
			String customerId = "12qwaszx";
			//根據系統設定獲取查詢的結束時間
			Date endDate = IAtomsDateTimeUtils.addCalendar(sendDate, 0, this.expireMonths, 0);
			this.edcContractExpireService.sendReportMail(sendDate, customerId, reportCode);
			Assert.assertTrue(true);
			
			//存在符合條件的報表信息 - 不存在合約信息
			sendDate = null;
			endDate = IAtomsDateTimeUtils.addCalendar(sendDate, 0, this.expireMonths, 0);
			this.edcContractExpireService.sendReportMail(sendDate, customerId, reportCode);
			Assert.assertTrue(true);
			
			//存在符合條件的報表信息 - 存在合約信息
			sendDate = DateTimeUtils.getCurrentDate();
			endDate = IAtomsDateTimeUtils.addCalendar(sendDate, 0, this.expireMonths, 0);
			customerId = "1475140315196-0142";
			this.edcContractExpireService.sendReportMail(sendDate, customerId, reportCode);
			Assert.assertTrue(true);
		} catch (Exception e) {
			LOGGER.error("_TestEdcContractExpireService.testSendReportMail()", "is error :", e);
			e.printStackTrace();
		}
	*/}


	/**
	 * @return the edcContractExpireService
	 */
	public IReportService getEdcContractExpireService() {
		return edcContractExpireService;
	}

	/**
	 * @param edcContractExpireService the edcContractExpireService to set
	 */
	public void setEdcContractExpireService(IReportService edcContractExpireService) {
		this.edcContractExpireService = edcContractExpireService;
	}

	/**
	 * @return the expireMonths
	 */
	public int getExpireMonths() {
		return expireMonths;
	}

	/**
	 * @param expireMonths the expireMonths to set
	 */
	public void setExpireMonths(int expireMonths) {
		this.expireMonths = expireMonths;
	}
	
}
