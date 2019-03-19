package com.cybersoft4u.xian.iatoms.test.service;


import junit.framework.Assert;

import cafe.core.context.SessionContext;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.RepoStatusReportFormDTO;
import com.cybersoft4u.xian.iatoms.services.IRepoStatusReportService;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;
/**
 * Purpose: 設備狀態報表Service的單元測試
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2016/6/6
 * @MaintenancePersonnel ElvaHe
 */
public class _TestRepoStatusReportService  extends AbstractTestCase {
	
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestRepoStatusReportService.class);
	
	/**
	 * 注入設備狀態報表的service
	 *
	 */
	private IRepoStatusReportService repoStatusReportService;
	
	/**
	 * 使用者帳號管理DAO
	 */
	private IAdmUserDAO admUserDAO;
	
	/**
	 * 無參構造
	 */
	public _TestRepoStatusReportService() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}

	/**
	 * Purpose:測試查詢方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testQuery(){
		try {
			SessionContext  ctx =  new SessionContext();
			RepoStatusReportFormDTO formDTO = new RepoStatusReportFormDTO();
			/**
			 * 存在登錄者消息
			 */
			IAtomsLogonUser logonUser = new IAtomsLogonUser();
			logonUser.setId("1473076241770-0048");
			formDTO.setLogonUser(logonUser);
			/**
			 * 公共參數
			 */
			//設備名稱
			formDTO.setQueryAssetNames("'-----','0.0.0.0.'");
			//通訊模式
			formDTO.setQueryCommModes("'Dial_Up','TCP_IP'");
			//客戶
			formDTO.setQueryCustomer("1475139958276-0141");
			//維護模式
			formDTO.setQueryMaType("LEASE");
			//查詢日期
			formDTO.setQueryDate("2017/06");
			formDTO.setYyyyMM("201706");
			//排序方式
			formDTO.setOrder("asc");
			formDTO.setSort("companyId");
			//當前頁碼
			formDTO.setPage(Integer.valueOf(1));
			//每頁顯示多少筆
			formDTO.setRows(Integer.valueOf(10));
			//得到系統當前月份
			String strCurrentDate = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), DateTimeUtils.DT_FMT_YYYYMM);
			//查詢月份小於系統當前月份，則從設備資料月檔抓取資料；反之，則從設備最新資料檔抓取資料。
			String queryTableName = IAtomsConstants.IATOMS_TB_NAME_REPOSITORY_HIST_MONTHLY;
			
			/**
			 * 不存在客戶
			 */
			ctx.setRequestParameter(formDTO);
			this.repoStatusReportService.query(ctx);
			if (IAtomsMessageCode.DATA_NOT_FOUND.equals(ctx.getReturnMessage().getCode())) {
				Assert.assertTrue(true);
			} else {
				Assert.assertFalse(true);
			}
			
			/**
			 * 存在客戶
			 */
			//客戶
			formDTO.setQueryCustomer("1475139958276-0141");
			ctx.setRequestParameter(formDTO);
			this.repoStatusReportService.query(ctx);
			if (IAtomsMessageCode.DATA_NOT_FOUND.equals(ctx.getReturnMessage().getCode())) {
				Assert.assertTrue(true);
			} else {
				Assert.assertFalse(true);
			}
		} catch (Exception e) {
			LOGGER.error("_TestRepoStatusReportService.testQuery()", "is error", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the repoStatusReportService
	 */
	public IRepoStatusReportService getRepoStatusReportService() {
		return repoStatusReportService;
	}

	/**
	 * @param repoStatusReportService the repoStatusReportService to set
	 */
	public void setRepoStatusReportService(
			IRepoStatusReportService repoStatusReportService) {
		this.repoStatusReportService = repoStatusReportService;
	}

	/**
	 * @return the admUserDAO
	 */
	public IAdmUserDAO getAdmUserDAO() {
		return admUserDAO;
	}

	/**
	 * @param admUserDAO the admUserDAO to set
	 */
	public void setAdmUserDAO(IAdmUserDAO admUserDAO) {
		this.admUserDAO = admUserDAO;
	}
	
}
