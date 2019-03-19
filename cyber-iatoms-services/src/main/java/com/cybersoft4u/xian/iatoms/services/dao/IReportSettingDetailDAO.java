package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.services.dmo.BimReportSettingDetail;
/**
 * Purpose:報表發送功能設定關於明細DAO接口 
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2016年8月24日
 * @MaintenancePersonnel ElvaHe
 */
public interface IReportSettingDetailDAO extends IGenericBaseDAO<BimReportSettingDetail>{
	/**
	 * Purpose: 根據報表編號獲取該報表的所有報表明細
	 * @author ElvaHe
	 * @param settingId 報表編號
	 * @throws DataAccessException 出錯時拋出DataAccessException異常
	 * @return List<ReportSettingDetatilDTO> 存放明細的集合
	 */
	public List<String> listBy(String settingId) throws DataAccessException;
	
	/**
	 * Purpose:根據報表編號刪除該報表的所有明細信息
	 * @author ElvaHe
	 * @param settingId 報表編號
	 * @throws DataAccessException 出錯時拋出DataAccessException異常
	 */
	public void deleteBySettingId(String settingId) throws DataAccessException;
	
}
