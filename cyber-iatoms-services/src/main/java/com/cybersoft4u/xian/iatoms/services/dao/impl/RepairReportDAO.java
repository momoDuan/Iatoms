package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import cafe.core.bean.CoreMessageCode;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.bean.dto.RepairReportDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IRepairReportDAO;
/**
 * Purpose: 報修問題分析報表DAO
 * @author echomou
 * @since  JDK 1.6
 * @date   2016/11/14
 * @MaintenancePersonnel echomou
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class RepairReportDAO extends GenericBaseDAO implements IRepairReportDAO {
	
	/**
	 * 系统日志记录物件 
	 */
	private static final CafeLog logger = CafeLogFactory.getLog(RepairReportDAO.class);
	/**
	 * Constructor: 無參構造
	 */
	public RepairReportDAO() {
		super();
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IRepairReportDAO#listby(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, boolean, java.lang.String, java.lang.String)
	 */
	@Override
	public List<RepairReportDTO> listby(String queryCustomer, String closedTimeStart, String closedTimeEnd, String queryMerchantCode, String queryEdcType, Integer currentPage, Integer pageSize, boolean isPage, String sort, String order) throws DataAccessException {
		logger.debug(this.getClass().getName() + "queryCustomer" + queryCustomer);
		logger.debug(this.getClass().getName() + "closedTimeStart" + closedTimeStart);
		logger.debug(this.getClass().getName() + "closedTimeEnd" + closedTimeEnd);
		logger.debug(this.getClass().getName() + "queryMerchantCode" + queryMerchantCode);
		logger.debug(this.getClass().getName() + "queryEdcType" + queryEdcType);
		List<RepairReportDTO> listRepair = null;
		String schema = this.getMySchema();
		SqlStatement sqlStatement = new SqlStatement();
		try {
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ".listby() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return listRepair;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IRepairReportDAO#getCount(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public int getCount(String queryCustomer, String closedTimeStart, String closedTimeEnd, String queryMerchantCode, String queryEdcType) throws DataAccessException {
		logger.debug(this.getClass().getName() + "queryCustomer" + queryCustomer);
		logger.debug(this.getClass().getName() + "closedTimeStart" + closedTimeStart);
		logger.debug(this.getClass().getName() + "closedTimeEnd" + closedTimeEnd);
		logger.debug(this.getClass().getName() + "queryMerchantCode" + queryMerchantCode);
		logger.debug(this.getClass().getName() + "queryEdcType" + queryEdcType);
		String schema = this.getMySchema();	
		SqlStatement sqlStatement = new SqlStatement();
		try {
			//sqlQueryBean.setParameter(BimMerchantHeaderDTO.ATTRIBUTE.AREA.getValue(), IATOMS_PARAM_TYPE.REGION.getCode());
			//logger.debug(this.getClass().getName() + "SQL---------->" + sqlQueryBean.toString());
			//List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			//return result.get(0).intValue();
			return  0;
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ".getCount() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IRepairReportDAO#listSolve(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, boolean, java.lang.String, java.lang.String)
	 */
	@Override
	public List<RepairReportDTO> listSolve(String queryCustomer, String closedTimeStart, String closedTimeEnd, String queryMerchantCode, String queryEdcType, Integer currentPage, Integer pageSize, boolean isPage, String sort, String order) throws DataAccessException {
		logger.debug(this.getClass().getName() + "queryCustomer" + queryCustomer);
		logger.debug(this.getClass().getName() + "closedTimeStart" + closedTimeStart);
		logger.debug(this.getClass().getName() + "closedTimeEnd" + closedTimeEnd);
		logger.debug(this.getClass().getName() + "queryMerchantCode" + queryMerchantCode);
		logger.debug(this.getClass().getName() + "queryEdcType" + queryEdcType);
		List<RepairReportDTO> listSolve = null;
		String schema = this.getMySchema();
		SqlStatement sqlStatement = new SqlStatement();
		try {
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ".listby() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return listSolve;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IRepairReportDAO#geSolvetCount(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public int geSolvetCount(String queryCustomer, String closedTimeStart, String closedTimeEnd, String queryMerchantCode, String queryEdcType) throws DataAccessException {
		logger.debug(this.getClass().getName() + "queryCustomer" + queryCustomer);
		logger.debug(this.getClass().getName() + "closedTimeStart" + closedTimeStart);
		logger.debug(this.getClass().getName() + "closedTimeEnd" + closedTimeEnd);
		logger.debug(this.getClass().getName() + "queryMerchantCode" + queryMerchantCode);
		logger.debug(this.getClass().getName() + "queryEdcType" + queryEdcType);
		String schema = this.getMySchema();	
		SqlStatement sqlStatement = new SqlStatement();
		try {
			//sqlQueryBean.setParameter(BimMerchantHeaderDTO.ATTRIBUTE.AREA.getValue(), IATOMS_PARAM_TYPE.REGION.getCode());
			//logger.debug(this.getClass().getName() + "SQL---------->" + sqlQueryBean.toString());
			//List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			//return result.get(0).intValue();
			return  0;
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ".getCount() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

}
