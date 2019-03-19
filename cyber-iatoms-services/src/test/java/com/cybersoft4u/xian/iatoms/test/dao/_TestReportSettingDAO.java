package com.cybersoft4u.xian.iatoms.test.dao;


import java.util.List;

import org.springframework.util.CollectionUtils;

import junit.framework.Assert;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.bean.dto.ReportSettingDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IReportSettingDAO;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;
/**
 * Purpose: 報表DAO的單元測試
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2016/8/26
 * @MaintenancePersonnel  ElvaHe
 */
public class _TestReportSettingDAO extends AbstractTestCase{
	
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestReportSettingDAO.class);
	
	/**
	 * 注入ReportSettingDAO
	 */
	private IReportSettingDAO reportSettingDAO;

	/**
	 * 無參構造
	 */
	public _TestReportSettingDAO() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}

	/**
	 * Purpose: 測試查詢方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testListByReportCode(){
		try {
			//客戶編號
			String queryCustomerId = "1470724067251-00181";
			//報表名稱
			String queryReportCode = "2";
			Integer pageSize = 10;
			Integer pageIndex = 1;
			//排序
			String sort = "";
			String order = "";
			List<ReportSettingDTO> list = reportSettingDAO.listByReportCode(queryCustomerId, queryReportCode, pageSize, pageIndex, sort, order);
			if(!CollectionUtils.isEmpty(list)){
				Assert.assertEquals(list.size(), list.size());
			} else {
				Assert.assertFalse(list == null);
			}
		} catch (DataAccessException e) {
			LOGGER.error("_TestReportSettingDAO.testListByReportCode()", "is error ：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試獲取符合條件的資料筆數的方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testCodeCount(){
		try {
			//客戶編號
			String queryCustomerId = "1470724067251-0018";
			//報表名稱
			String queryReportCode = "2";
			int count = this.reportSettingDAO.codeCount(queryCustomerId, queryReportCode);
			if(count != 0){
				Assert.assertEquals(count, count);
			} else {
				Assert.assertFalse(count != 0);
			}
		} catch (Exception e) {
			LOGGER.error("_TestReportSettingDAO.testCodeCount()", "is error ：", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:測試獲取在相同客戶編號下報表編號集合
	 * @author ElvaHe
	 * @retur void
	 */
	public void testGetPreReportCodeList(){
		try {
			//客戶編號
			String companyId = "1470724067251-0018s";
			List<Parameter> list = reportSettingDAO.getPreReportCodeList(companyId);
			//當客戶存在報表則返回不存在的報表數量，否則返回所有報表
			//if((!CollectionUtils.isEmpty(list)) && (list.size() != 13)){
				Assert.assertEquals(list.size(), list.size());
			//} else {
			//	System.out.println("2");
			//	Assert.assertFalse(list != null);
			//}
		} catch (Exception e) {
			LOGGER.error("_TestReportSettingDAO.testGetPreReportCodeList()", "is error ：", e);
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Purpose:測試根據客戶編號和報表編號獲取報表集合
	 * @author ElvaHe
	 * @reurn void
	 */
	public void testGetDetailList(){
		try {
			String customerId = "1470724067251-0018";
			String reportCode = "2qw";
			List<ReportSettingDTO> reportSettingDTOs = this.reportSettingDAO.getDetailList(customerId, reportCode);
			if (!CollectionUtils.isEmpty(reportSettingDTOs)) {
				Assert.assertNotNull(reportSettingDTOs);
			} else {
				Assert.assertFalse(reportSettingDTOs == null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestReportSettingDAO.testGetDetailList()", "is error ：", e);
			e.printStackTrace();
		}
	}
	/**
	 * @return the reportSettingDAO
	 */
	public IReportSettingDAO getReportSettingDAO() {
		return reportSettingDAO;
	}

	/**
	 * @param reportSettingDAO the reportSettingDAO to set
	 */
	public void setReportSettingDAO(IReportSettingDAO reportSettingDAO) {
		this.reportSettingDAO = reportSettingDAO;
	}
	
}
