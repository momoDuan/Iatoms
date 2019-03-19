package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import cafe.core.bean.CoreMessageCode;
import cafe.core.config.GenericConfigManager;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.bean.dto.ReportSettingDetailDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IReportSettingDetailDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimReportSettingDetail;
/**
 * Purpose: 報表發送設定關於明細的DAO實現類
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2016-8-24
 * @MaintenancePersonnel ElvaHe
 */
public class ReportSettingDetailDAO  extends GenericBaseDAO<BimReportSettingDetail> implements IReportSettingDetailDAO {

	/**
	 * 日志記錄物件  
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.DAO, ReportSettingDetailDAO.class);
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IReportSettingDetailDAO#listBy(java.lang.String)
	 */
	public List<String> listBy(String settingId) throws DataAccessException {
		List<String> reportDetatilDTOs = null;
		try {
			//打印傳遞的參數
			LOGGER.debug("listBy()", "parameter : settingId = ", settingId);
			SqlStatement sqlStatement = new SqlStatement();
			StringBuffer stringBuffer = new StringBuffer();
			String schema = this.getMySchema();
			//拼接SQL語句
			sqlStatement.addSelectClause(" report.REPORT_DETAIL AS VALUE");
			/*sqlStatement.addFromExpression(
					schema + ".BIM_REPORT_SETTING_DETAIL report ");*/
			stringBuffer.append(schema).append(".BIM_REPORT_SETTING_DETAIL report ");
			sqlStatement.addFromExpression(stringBuffer.toString());
			//若存在settingId
			if(StringUtils.hasText(settingId)){
				sqlStatement.addWhereClause(" report.SETTING_ID =:settingId", settingId);
			}
			//打印SQL語句
			LOGGER.debug("listBy()", "SQL : ", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			reportDetatilDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean);
		} catch (Exception e) {
			LOGGER.error("listBy()", "is error :", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return reportDetatilDTOs;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IReportSettingDetailDAO#deleteReportDetails(java.lang.String)
	 */
	@Override
	public void deleteBySettingId(String settingId) throws DataAccessException {
		try {
			LOGGER.debug("deleteBySettingId()", "settingId : ", settingId);
			//若存在SettingId
			if(StringUtils.hasText(settingId)){
				SqlQueryBean sqlQueryBean = new SqlQueryBean();
				String schema = this.getMySchema();
			/*	StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append("delete from ").append(schema).append(".BIM_REPORT_SETTING_DETAIL where ");
				sqlQueryBean.append(stringBuffer.toString());*/
				sqlQueryBean.append("delete from ").append(schema).append(".BIM_REPORT_SETTING_DETAIL where ");
				sqlQueryBean.append("SETTING_ID =:settingId");
				sqlQueryBean.setParameter(ReportSettingDetailDTO.ATTRIBUTE.SETTING_ID.getValue(), settingId);
				this.getDaoSupport().updateByNativeSql(sqlQueryBean);
				LOGGER.debug("deleteReportDetails()", "SQL : ", sqlQueryBean.toString());
			}
		} catch (Exception e) {
			LOGGER.error("deleteReportDetails()", "is error :", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	
}
