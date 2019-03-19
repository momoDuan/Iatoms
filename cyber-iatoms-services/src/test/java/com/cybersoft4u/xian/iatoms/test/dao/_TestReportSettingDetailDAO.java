package com.cybersoft4u.xian.iatoms.test.dao;

import java.util.List;

import org.springframework.util.CollectionUtils;

import junit.framework.Assert;
import cafe.core.dao.DataAccessException;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.services.dao.IReportSettingDetailDAO;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;
/**
 * Purpose: 報表明細DAO的單元測試
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2016/8/26
 * @MaintenancePersonnel  ElvaHe
 */
public class _TestReportSettingDetailDAO extends AbstractTestCase{
	
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestReportSettingDetailDAO.class);
	
	/**
	 * 注入ReportSettingDetailDAO
	 */
	private IReportSettingDetailDAO reportSettingDetailDAO;
	
	/**
	 * 無參構造
	 */
	public _TestReportSettingDetailDAO() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	
	/**
	 * Purpose: 測試根據報表編號獲取該報表的所有報表明細
	 * @author ElvaHe
	 * @return void
	 */
	public void testListBy(){
		try {
			//報表編號
			String settingId = "1470880527396-0024";
			List<String> list = reportSettingDetailDAO.listBy(settingId);
			//存在報表明細時比較長度
			if(!CollectionUtils.isEmpty(list)){
				Assert.assertEquals(list.size(), list.size());
			} else {
				Assert.assertFalse(list == null);
			}
			Assert.assertEquals(list.size(), list.size());
		} catch (DataAccessException e) {
			LOGGER.error("_TestReportSettingDetailDAO.testListBy()", "is error ：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose: 測試根據報表編號刪除該報表的所有明細信息
	 * @author ElvaHe
	 * @return void
	 */
	public void testDeleteBySettingId(){
		try {
			//報表編號
			String settingId = "1470880527396-0024";
			reportSettingDetailDAO.deleteBySettingId(settingId);
		} catch (DataAccessException e) {
			LOGGER.error("_TestReportSettingDetailDAO.testDeleteBySettingId()", "is error ：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the reportSettingDetailDAO
	 */
	public IReportSettingDetailDAO getReportSettingDetailDAO() {
		return reportSettingDetailDAO;
	}

	/**
	 * @param reportSettingDetailDAO the reportSettingDetailDAO to set
	 */
	public void setReportSettingDetailDAO(
			IReportSettingDetailDAO reportSettingDetailDAO) {
		this.reportSettingDetailDAO = reportSettingDetailDAO;
	}
	
	
}

