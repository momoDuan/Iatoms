package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.hibernate.type.DateType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.util.CollectionUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryHistoryDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryDAO;

/**
 * Purpose: 設備歷史記錄查詢DAO
 * @author ericdu
 * @since  JDK 1.7
 * @date   2016年7月20日
 * @MaintenancePersonnel ericdu
 */
@SuppressWarnings("rawtypes")
public class DmmRepositoryHistoryDAO extends GenericBaseDAO implements
		IDmmRepositoryHistoryDAO {

	/**
	 * 系統日志記錄
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(DmmRepositoryHistoryDAO.class);

	/**
	 * Constructor: 無參構造
	 */
	public DmmRepositoryHistoryDAO() {
		super();
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryDAO#list(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	public List<DmmRepositoryHistoryDTO> list(String histId, String serialNumber, String tid,
			String dtid, String sort, String order, Integer currentPage,
			Integer pageSize) throws DataAccessException {
		LOGGER.error(this.getClass().getName()+".list() --> histId: "+histId);
		LOGGER.error(this.getClass().getName()+".list() --> serialNumber: "+serialNumber);
		LOGGER.error(this.getClass().getName()+".list() --> tid: "+tid);
		LOGGER.error(this.getClass().getName()+".list() --> dtid: "+dtid);
		LOGGER.error(this.getClass().getName()+".list() --> sort: "+sort);
		LOGGER.error(this.getClass().getName()+".list() --> order: "+order);
		LOGGER.error(this.getClass().getName()+".list() --> currentPage: "+currentPage);
		LOGGER.error(this.getClass().getName()+".list() --> pageSize: "+pageSize);
		List<DmmRepositoryHistoryDTO> results = null;
		try{
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append("SELECT * FROM(");
			sql.append("SELECT hist.HIST_ID as histId,"
							+ " hist.STATUS as status,"
							+ " bpid.ITEM_NAME as statusName,"
							+ " hist.SERIAL_NUMBER as serialNumber,"
							+ " asst.NAME as name,"
							+ " hist.ADDRESS as address,"
							+ " maType.ITEM_NAME as maTypeName,"
							+ " hist.IS_ENABLED as isEnabled,"
							+ " hist.ENABLE_DATE as enableDate,"
							+ " hist.ACTION as action,"
							+ " hist.DESCRIPTION as description,"
							+ " hist.CARRIER as carrier,"
							+ " hist.CARRY_START as carryStart,"
							+ " hist.CARRY_END as carryEnd,"
							+ " hist.CARRY_COMMENT as carryComment,"
							+ " hist.BORROWER as borrower,"
							+ " hist.BORROWER_START as borrowStart,"
							+ " hist.BORROWER_END as borrowEnd,"
							+ " hist.BORROWER_COMMENT as borrowComment,"
							+ " hist.BACK_DATE as backDate,"
							+ " hist.TICKET_ID as ticketId,"
							+ " hist.TICKET_COMPLETION_DATE as ticketCompletionDate,"
							+ " hist.DTID as dtid,"
							+ " hist.MID as mid,"
							+ " hist.MER_REGISTERED_NAME as merRegisteredName,"
							+ " hist.MER_ANNOUNCED_NAME as merAnnouncedName,"
							+ " hist.MER_INSTALL_ADDRESS as merInstallAddress,"
							+ " hist.MER_LOCATION as merLocation,"
							+ " hist.MER_INSTALL_POSITION_DESC as merInstallPositionDesc,"
							+ " hist.ASSET_IN_ID as assetInId,"
							+ " hist.APP_NAME as appName,"
							+ " hist.APP_VERSION as appVersion,"
							+ " hist.FACTORY_WARRANTY_DATE as factoryWarrantyDate,"
							+ " hist.CUSTOMER_WARRANTY_DATE as customerWarrantyDate,"
							+ " hist.UPDATE_USER_NAME as updateUserName,"
							+ " hist.UPDATE_DATE as updateDate,"
							+ " hist.PROPERTY_ID as propertyId,"
							+ " warehouse.NAME as wareHouseName");
			sql.append(" FROM "+schema+".REPOSITORY_HIST hist");
			sql.append("LEFT JOIN "+schema+".DMM_ASSET_TYPE asst "
							+ " ON asst.ASSET_TYPE_ID = hist.ASSET_TYPE_ID");
			sql.append("LEFT JOIN "+schema+".BIM_WAREHOUSE warehouse "
							+ " ON hist.WAREHOUSE_ID = warehouse.WAREHOUSE_ID");
			sql.append("LEFT JOIN "+schema+".BASE_PARAMETER_ITEM_DEF bpid"
							+ " ON bpid.ITEM_VALUE = hist.STATUS"
							+ " AND bpid.BPTD_CODE = 'ASSET_STATUS'"
			//				+ " AND bpid.APPROVED_FLAG = 'Y'");
							// 處理approvedFlag
							+ " AND isnull(bpid.DELETED, 'N') = 'N'");
			sql.append("LEFT JOIN "+schema+".BASE_PARAMETER_ITEM_DEF maType"
							+ " ON maType.ITEM_VALUE = hist.MA_TYPE"
							+ " AND maType.BPTD_CODE = 'MA_TYPE'"
			//				+ " AND maType.APPROVED_FLAG = 'Y'");
							// 處理approvedFlag
							+ " AND isnull(maType.DELETED, 'N') = 'N'");
			sql.append(" WHERE 1=1");
			if(StringUtils.hasText(histId)){
				sql.append(" AND hist.HIST_ID = :histId");
				sql.setParameter(DmmRepositoryHistoryDTO.ATTRIBUTE.HISTORY_ID.getValue(), histId);
				LOGGER.error(this.getClass().getName()+".list() --> histId: "+histId);
			}
			if(StringUtils.hasText(serialNumber)){
				sql.append(" AND hist.SERIAL_NUMBER like :serialNumber");
				sql.setParameter(DmmRepositoryHistoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), serialNumber);
				LOGGER.error(this.getClass().getName()+".list() --> serialNumber: "+serialNumber);
			}
			if(StringUtils.hasText(tid)){
				sql.append(" AND hist.TID like :tid");
				sql.setParameter(DmmRepositoryHistoryDTO.ATTRIBUTE.TID.getValue(), tid);
				LOGGER.error(this.getClass().getName()+".list() --> tid: "+tid);
			}
			if(StringUtils.hasText(dtid)){
				sql.append(" AND hist.DTID like :dtid");
				sql.setParameter(DmmRepositoryHistoryDTO.ATTRIBUTE.DTID.getValue(), dtid);
				LOGGER.error(this.getClass().getName()+".list() --> dtid: "+dtid);
			}
			sql.setStartPage(currentPage);
			sql.setPageSize(pageSize);
			sql.append(")temp");
			if(StringUtils.hasText(sort) && StringUtils.hasText(order)){
				sql.append(" ORDER BY temp."+sort+" " +order);
				LOGGER.debug(this.getClass().getName()+".list() --> sort:"+sort);
				LOGGER.debug(this.getClass().getName()+".list() --> order:"+order);
			}
			AliasBean alias = new AliasBean(DmmRepositoryHistoryDTO.class);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.HISTORY_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.STATUS.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.STATUS_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.PROPERTY_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.ADDRESS.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.MA_TYPE_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.IS_ENABLED.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.ENABLE_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.WAREHOUSE_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.ACTION.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.DESCRIPTION.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.CARRIER.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.CARRY_START.getValue(), TimestampType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.CARRY_END.getValue(), TimestampType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.CARRY_COMMENT.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.BORROWER.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.BORROW_START.getValue(), TimestampType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.BORROW_END.getValue(), TimestampType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.BORROW_COMMENT.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.BACK_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.TICKET_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.TICKET_COMPLETION_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.MID.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.MER_REGISTERED_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.MER_ANNOUNCED_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.MER_LOCATION.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.MER_INSTALL_POSITION_DESC.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.ASSET_IN_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.APP_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.APP_VERSION.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.FACTORY_WARRANTY_DATE.getValue(), DateType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.CUSTOMER_WARRANTY_DATE.getValue(), DateType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.UPDATE_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.UPDATE_USER_NAME.getValue(), StringType.INSTANCE);
			results = this.getDaoSupport().findByNativeSql(sql, alias);
			LOGGER.error(this.getClass().getName()+".list() --> sql: "+sql);
		}catch(Exception e){
			LOGGER.error(this.getClass().getName()+".list() is error: "+e, e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
		return results;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryDAO#count(java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public Integer count(String histId, String serialNumber, String tid, String dtid)
			throws DataAccessException {
		LOGGER.error(this.getClass().getName()+".count() --> histId: "+histId);
		LOGGER.error(this.getClass().getName()+".count() --> serialNumber: "+serialNumber);
		LOGGER.error(this.getClass().getName()+".count() --> tid: "+tid);
		LOGGER.error(this.getClass().getName()+".count() --> dtid: "+dtid);
		Integer result = 0;
		try{
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append(" SELECT COUNT(hist.HIST_ID)");
			sql.append(" FROM "+schema+".REPOSITORY_HIST hist");
			sql.append(" WHERE 1=1");
			if(StringUtils.hasText(histId)){
				sql.append(" AND hist.HIST_ID = :histId");
				sql.setParameter(DmmRepositoryHistoryDTO.ATTRIBUTE.HISTORY_ID.getValue(), histId);
				LOGGER.error(this.getClass().getName()+".count() --> histId: "+histId);
			}
			if(StringUtils.hasText(serialNumber)){
				sql.append(" AND hist.SERIAL_NUMBER like :serialNumber");
				sql.setParameter(DmmRepositoryHistoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), serialNumber);
				LOGGER.error(this.getClass().getName()+".count() --> serialNumber: "+serialNumber);
			}
			if(StringUtils.hasText(tid)){
				sql.append(" AND hist.TID like :tid");
				sql.setParameter(DmmRepositoryHistoryDTO.ATTRIBUTE.TID.getValue(), tid);
				LOGGER.error(this.getClass().getName()+".count() --> tid: "+tid);
			}
			if(StringUtils.hasText(dtid)){
				sql.append(" AND hist.DTID like :dtid");
				sql.setParameter(DmmRepositoryHistoryDTO.ATTRIBUTE.DTID.getValue(), dtid);
				LOGGER.error(this.getClass().getName()+".count() --> dtid: "+dtid);
			}
			List<Integer> list = this.getDaoSupport().findByNativeSql(sql);
			LOGGER.error(this.getClass().getName()+".count() --> sql: "+sql);
			if(!CollectionUtils.isEmpty(list)){
				result = list.get(0).intValue();
			}
		}catch(Exception e){
			LOGGER.error(this.getClass().getName()+".count() is error: "+e, e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
		return result;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryDAO#copyToMonthTable(java.lang.String)
	 */
	@Override
	public void copyToMonthTable(String isHis) throws DataAccessException {
		try {
			//打印參數
			LOGGER.debug(this.getClass().getSimpleName() + "copyToMonthTable", "isHis:" + isHis);
			//創建SqlStatement
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("exec dbo.usp_copyAssetHistoryToMonthTable :isHis");
			//sqlQueryBean.append("exec dbo.usp_copyAssetHistoryToMonthTable :isHis, :copyTime");
			//sqlQueryBean.setParameter("copyTime", DateTimeUtils.getCurrentTimestamp());
			sqlQueryBean.setParameter("isHis", isHis);
			LOGGER.debug(this.getClass().getName() + "copyToMonthTable ---->sql:" + sqlQueryBean.toString() );
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("dmmRepositoryHistoryDAO.copyToMonthTable() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryDAO#getStatusByHistoryId(java.lang.String)
	 */
	@Override
	public DmmRepositoryHistoryDTO getStatusByHistoryId(String historyId)
			throws DataAccessException {
		LOGGER.error(this.getClass().getName()+".getStatusByHistoryId() --> historyId: "+historyId);
		DmmRepositoryHistoryDTO result = null;
		try{
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append(" SELECT top 1 repohistory.STATUS AS status, ");
			sql.append(" repohistory.IS_ENABLED as isEnabled, ");
			sql.append(" repohistory.ENABLE_DATE as enableDate, ");
			sql.append(" repohistory.SIM_ENABLE_DATE as simEnableDate, ");
			sql.append(" repohistory.CUSTOMER_WARRANTY_DATE as customerWarrantyDate ");
			sql.append(" FROM ").append(schema).append(".DMM_REPOSITORY_HISTORY repohistory");
			sql.append(" inner join ").append(schema).append(".DMM_REPOSITORY_HISTORY repohistory1 on repohistory.ASSET_ID = repohistory1.ASSET_ID ");
			sql.append(" WHERE 1=1");
			if(StringUtils.hasText(historyId)){
				sql.append(" AND repohistory1.HISTORY_ID = :historyId");
				sql.setParameter(DmmRepositoryHistoryDTO.ATTRIBUTE.HISTORY_ID.getValue(), historyId);
				LOGGER.error(this.getClass().getName()+".getStatusByHistoryId() --> historyId: "+historyId);
				sql.append(" AND repohistory.UPDATE_DATE < repohistory1.UPDATE_DATE ");
				sql.append(" and (repohistory.STATUS ='IN_APPLY' or repohistory.STATUS ='BORROWING') ");
			}
			sql.append("  order by repohistory.UPDATE_DATE desc ");
			AliasBean alias = new AliasBean(DmmRepositoryHistoryDTO.class);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.STATUS.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.IS_ENABLED.getValue(), StringType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.ENABLE_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.SIM_ENABLE_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.CUSTOMER_WARRANTY_DATE.getValue(), DateType.INSTANCE);
			List<DmmRepositoryHistoryDTO> dmmRepositoryHistoryDTOs = this.getDaoSupport().findByNativeSql(sql, alias);
			LOGGER.error(this.getClass().getName()+".getStatusByHistoryId() --> sql: "+sql);
			if(!CollectionUtils.isEmpty(dmmRepositoryHistoryDTOs)){
				result = dmmRepositoryHistoryDTOs.get(0);
			}
		} catch (Exception e) {
			LOGGER.error("dmmRepositoryHistoryDAO.getStatusByHistoryId() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		return result;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryDAO#insertRepositoryHistory(java.util.List, java.lang.String)
	 */
	public void insertRepositoryHistory(List<String> repositoryIds, String historyId) throws DataAccessException {
		try {
			//打印參數
			LOGGER.debug("insertRepositoryHistory", "repositoryIds:", repositoryIds.toString());
			//打印參數
			LOGGER.debug("insertRepositoryHistory", "historyId:", historyId);
			String schema = this.getMySchema();
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("insert into");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY (");
			sqlQueryBean.append("HISTORY_ID,").append("ASSET_ID,");
			sqlQueryBean.append("SERIAL_NUMBER,").append("ASSET_TYPE_ID,");
			sqlQueryBean.append("BRAND,").append("PROPERTY_ID,");
			sqlQueryBean.append("WAREHOUSE_ID,").append("CONTRACT_ID,");
			sqlQueryBean.append("MA_TYPE,").append("STATUS,");
			sqlQueryBean.append("IS_ENABLED,").append("ENABLE_DATE,");
			//sqlQueryBean.append("IS_SIM_ENABLE,").append("SIM_ENABLE_DATE,");
			sqlQueryBean.append("SIM_ENABLE_DATE,");
			sqlQueryBean.append("SIM_ENABLE_NO,").append("CARRIER,");
			sqlQueryBean.append("CARRY_DATE,").append("BORROWER,");
			sqlQueryBean.append("BORROWER_START,").append("BORROWER_END,");
			sqlQueryBean.append("BORROWER_EMAIL,").append("BORROWER_MGR_EMAIL,");
			sqlQueryBean.append("BACK_DATE,").append("ASSET_OWNER,");
			sqlQueryBean.append("ASSET_USER,").append("IS_CUP,");
			sqlQueryBean.append("RETIRE_REASON_CODE,").append("CASE_ID,");
			sqlQueryBean.append("CASE_COMPLETION_DATE,");
			sqlQueryBean.append("TID,").append("DTID,");
			sqlQueryBean.append("APPLICATION_ID,").append("MERCHANT_ID,");
			sqlQueryBean.append("ASSET_IN_ID,").append("ASSET_IN_TIME,");
			sqlQueryBean.append("ASSET_TRANS_ID,").append("MAINTAIN_COMPANY,");
			sqlQueryBean.append("REPAIR_VENDOR,").append("DESCRIPTION,");
			sqlQueryBean.append("ACTION,").append("FAULT_COMPONENT,");
			sqlQueryBean.append("FAULT_DESCRIPTION,").append("CUSTOMER_WARRANTY_DATE,");
			sqlQueryBean.append("FACTORY_WARRANTY_DATE,").append("CREATE_USER,");
			sqlQueryBean.append("CREATE_USER_NAME,").append("CREATE_DATE,");
			sqlQueryBean.append("UPDATE_USER,").append("UPDATE_USER_NAME,");
			sqlQueryBean.append("UPDATE_DATE,");
			sqlQueryBean.append("CHECKED_DATE ,").append("ASSET_MODEL ,");
			sqlQueryBean.append("INSTALLED_ADRESS ,").append("INSTALL_TYPE ,");
			sqlQueryBean.append("MERCHANT_HEADER_ID ,").append("CYBER_APPROVED_DATE,");
			sqlQueryBean.append("KEEPER_NAME,").append("INSTALLED_ADRESS_LOCATION, ");
			sqlQueryBean.append("DEPARTMENT_ID,").append("REPAIR_COMPANY, ");
			sqlQueryBean.append("IS_CUSTOMER_CHECKED,").append("IS_CHECKED, ");
			sqlQueryBean.append("COUNTER,").append("CARTON_NO ");
			
			sqlQueryBean.append(")select");
			sqlQueryBean.append(":historyId + '-' + cast(ROW_NUMBER() over (order by ASSET_ID) as varchar),");
			sqlQueryBean.append("ASSET_ID,");
			sqlQueryBean.append("SERIAL_NUMBER,").append("ASSET_TYPE_ID,");
			sqlQueryBean.append("BRAND,").append("PROPERTY_ID,");
			sqlQueryBean.append("WAREHOUSE_ID,").append("CONTRACT_ID,");
			sqlQueryBean.append("MA_TYPE,").append("STATUS,");
			sqlQueryBean.append("IS_ENABLED,").append("ENABLE_DATE,");
			//sqlQueryBean.append("IS_SIM_ENABLE,").append("SIM_ENABLE_DATE,");
			sqlQueryBean.append("SIM_ENABLE_DATE,");
			sqlQueryBean.append("SIM_ENABLE_NO,").append("CARRIER,");
			sqlQueryBean.append("CARRY_DATE,").append("BORROWER,");
			sqlQueryBean.append("BORROWER_START,").append("BORROWER_END,");
			sqlQueryBean.append("BORROWER_EMAIL,").append("BORROWER_MGR_EMAIL,");
			sqlQueryBean.append("BACK_DATE,").append("ASSET_OWNER,");
			sqlQueryBean.append("ASSET_USER,").append("IS_CUP,");
			sqlQueryBean.append("RETIRE_REASON_CODE,").append("CASE_ID,");
			sqlQueryBean.append("CASE_COMPLETION_DATE,");
			sqlQueryBean.append("TID,").append("DTID,");
			sqlQueryBean.append("APPLICATION_ID,").append("MERCHANT_ID,");
			sqlQueryBean.append("ASSET_IN_ID,").append("ASSET_IN_TIME,");
			sqlQueryBean.append("ASSET_TRANS_ID,").append("MAINTAIN_COMPANY,");
			sqlQueryBean.append("REPAIR_VENDOR,").append("DESCRIPTION,");
			sqlQueryBean.append(" :action,").append("FAULT_COMPONENT,");
			sqlQueryBean.append("FAULT_DESCRIPTION,").append("CUSTOMER_WARRANTY_DATE,");
			sqlQueryBean.append("FACTORY_WARRANTY_DATE,").append("CREATE_USER,");
			sqlQueryBean.append("CREATE_USER_NAME,").append("CREATE_DATE,");
			sqlQueryBean.append("UPDATE_USER,").append("UPDATE_USER_NAME,");
			sqlQueryBean.append("UPDATE_DATE,");
			sqlQueryBean.append("CHECKED_DATE ,").append("ASSET_MODEL ,");
			sqlQueryBean.append("INSTALLED_ADRESS ,").append("INSTALL_TYPE ,");
			sqlQueryBean.append("MERCHANT_HEADER_ID ,").append("CYBER_APPROVED_DATE,");
			sqlQueryBean.append("KEEPER_NAME,").append("INSTALLED_ADRESS_LOCATION,");
			sqlQueryBean.append("DEPARTMENT_ID,").append("REPAIR_COMPANY, ");
			sqlQueryBean.append("IS_CUSTOMER_CHECKED,").append("IS_CHECKED, ");
			sqlQueryBean.append("COUNTER,").append("CARTON_NO ");
			sqlQueryBean.append(" from ").append(schema).append(".DMM_REPOSITORY");
			sqlQueryBean.append("where ASSET_ID in ( :assetId )");
			
			sqlQueryBean.append(" insert into ").append(schema).append(".DMM_REPOSITORY_HISTORY_COMM (HISTORY_ID, FAULT_COMPONENT) ");
			sqlQueryBean.append("select history.HISTORY_ID, com.FAULT_COMPONENT ");
			sqlQueryBean.append("from ").append(schema).append(".DMM_REPOSITORY_HISTORY history ");
			sqlQueryBean.append(" inner join ").append(schema).append(".DMM_REPOSITORY_FAULT_COM com on com.ASSET_ID = history.ASSET_ID ");
			sqlQueryBean.append("where ");
			sqlQueryBean.append("history.HISTORY_ID in (select ");
			sqlQueryBean.append(":historyId + '-' + CAST(ROW_NUMBER() OVER( ORDER BY ASSET_ID) AS VARCHAR) ");
			sqlQueryBean.append("from ").append(schema).append(".DMM_REPOSITORY where ASSET_ID IN( :assetId ) )");
			
			sqlQueryBean.append(" insert into ").append(schema).append(".DMM_REPOSITORY_HISTORY_DESC (HISTORY_ID, FAULT_DESCRIPTION) ");
			sqlQueryBean.append("select history1.HISTORY_ID, fault.FAULT_DESCRIPTION ");
			sqlQueryBean.append("from ").append(schema).append(".DMM_REPOSITORY_HISTORY history1 ");
			sqlQueryBean.append(" inner join ").append(schema).append(".DMM_REPOSITORY_FAULT_DESC fault on fault.ASSET_ID = history1.ASSET_ID ");
			sqlQueryBean.append("where ");
			sqlQueryBean.append("history1.HISTORY_ID in (select ");
			sqlQueryBean.append(":historyId + '-' + CAST(ROW_NUMBER() OVER( ORDER BY ASSET_ID) AS VARCHAR) ");
			sqlQueryBean.append("from ").append(schema).append(".DMM_REPOSITORY where ASSET_ID IN( :assetId ) )");
			
			sqlQueryBean.setParameter(DmmRepositoryHistoryDTO.ATTRIBUTE.HISTORY_ID.getValue(), historyId);
			sqlQueryBean.setParameter(DmmRepositoryHistoryDTO.ATTRIBUTE.ASSET_ID.getValue(), repositoryIds);
			sqlQueryBean.setParameter(DmmRepositoryHistoryDTO.ATTRIBUTE.ACTION.getValue(), IAtomsConstants.PARAM_ACTION_BATCH_UPDATE);
			LOGGER.debug("insertRepositoryHistory ---->sql:" + sqlQueryBean.toString() );
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
		} catch (Exception e) {
			LOGGER.error("dmmRepositoryHistoryDAO.insertRepositoryHistory() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryDAO#isNoData()
	 */
	@Override
	public Integer countData() throws DataAccessException {
		Integer count = 0;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("COUNT(1)");
			sqlStatement.addFromExpression(schema + ".DMM_REPOSITORY_HISTORY ");
			//打印SQL語句
			LOGGER.debug("DmmRepositoryHistoryDAO.countData()", "SQL : ", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				count = result.get(0);
			}
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryHistoryDAO.countData()", "error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE);
		}
		return count;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryDAO#deleteTransferData()
	 */
	@Override
	public void deleteTransferData() throws DataAccessException {
		try {
			String schema = this.getMySchema();
			SqlQueryBean sqlQueryBean = new SqlQueryBean("delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY_DESC; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY_COMM; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY; ");
			LOGGER.debug("deleteTransferData()", "sql:" + sqlQueryBean.toString());
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("deleteTransferData()", e.getMessage(), e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
	}
}
