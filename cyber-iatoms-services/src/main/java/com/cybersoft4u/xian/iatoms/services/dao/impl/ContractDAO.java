package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IContractDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimContract;

/**
 * Purpose: 合约DAO
 * @author CarrieDuan
 * @since  JDK 1.6
 * @date   2016年3月30日
 * @MaintenancePersonnel CarrieDuan
 */
public class ContractDAO extends GenericBaseDAO<BimContract> implements IContractDAO {

	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(ContractDAO.class);
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractDAO#count(java.lang.String, java.lang.String)
	 */
	@Override
	public Integer count(String customerId, String companyId) throws DataAccessException {
		LOGGER.debug("count()", "parameters:customerId=", customerId);
		LOGGER.debug(".count()", "parameters:companyId=", companyId);
		Integer result = Integer.valueOf(0);
		try {
			String schema = this.getMySchema();	
			//查询总条数
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("COUNT(1)");
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".BIM_CONTRACT c ");
			fromBuffer.append("left join ").append(schema).append(".BIM_COMPANY bm on c.COMPANY_ID = bm.COMPANY_ID");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if ( StringUtils.hasText(customerId)) {
				sqlStatement.addWhereClause("c.COMPANY_ID =:customerId", customerId);
			}
			if (StringUtils.hasText(companyId)) {
				StringBuffer whereBuffer = new StringBuffer();
				whereBuffer.append("c.CONTRACT_ID in (select contract_id from ").append(schema);
				whereBuffer.append(".BIM_CONTRACT_VENDOR where company_id = :companyId)");
				sqlStatement.addWhereClause(whereBuffer.toString());
			}
			sqlStatement.addWhereClause("c.DELETED = :status", IAtomsConstants.PARAM_NO);
			
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if (StringUtils.hasText(companyId)) {
				sqlQueryBean.setParameter("companyId", companyId);
			}
			LOGGER.debug("SQL---------->", sqlQueryBean.toString());
			List list = super.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(list)){
				result = (Integer) list.get(0);
			}	
		} catch (Exception e) {
			LOGGER.error(".count() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_CONTRACT)}, e);
		}
		return result;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractDAO#listBy(java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	@Override
	public List<BimContractDTO> listBy(String customerId, String companyId, String contractId, String order, String sort, int page, int rows) throws DataAccessException {
		List<BimContractDTO> listContract = null;
		try {
			LOGGER.debug(".listBy()", "parameters:customerId=", customerId);
			LOGGER.debug(".listBy()", "parameters:companyId=", companyId);
			LOGGER.debug(".listBy()", "parameters:order=", order);
			LOGGER.debug(".listBy()", "parameters:sort=", sort);
			LOGGER.debug(".listBy()", "parameters:page=", page);
			LOGGER.debug(".listBy()", "parameters:rows=", rows);
			String schema = this.getMySchema();	
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("bc.CONTRACT_ID", BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue());
			sqlStatement.addSelectClause("bc.COMPANY_ID", BimContractDTO.ATTRIBUTE.COMPANY_ID.getValue());
			sqlStatement.addSelectClause("bm.SHORT_NAME", BimContractDTO.ATTRIBUTE.CUSTOMER_NAME.getValue());
			if (StringUtils.hasText(contractId)) {
				sqlStatement.addSelectClause("STUFF((SELECT ','+ ltrim(company.COMPANY_ID) FROM dbo.BIM_COMPANY company,dbo.BIM_CONTRACT_VENDOR vendor,dbo.BIM_COMPANY_TYPE type "
						.concat(" WHERE company.COMPANY_ID=vendor.COMPANY_ID and vendor.CONTRACT_ID=bc.CONTRACT_ID and company.DELETED = :status and type.COMPANY_ID = vendor.COMPANY_ID and type.COMPANY_TYPE = :companyType order by company.SHORT_NAME FOR XML PATH('')), 1, 1, '')"), BimContractDTO.ATTRIBUTE.COMPANY_IDS.getValue());
				sqlStatement.addSelectClause("STUFF((SELECT ','+ ltrim(company.SHORT_NAME) FROM dbo.BIM_COMPANY company,dbo.BIM_CONTRACT_VENDOR vendor,dbo.BIM_COMPANY_TYPE type "
						.concat(" WHERE company.COMPANY_ID=vendor.COMPANY_ID and vendor.CONTRACT_ID=bc.CONTRACT_ID and company.DELETED = :status and type.COMPANY_ID = vendor.COMPANY_ID and type.COMPANY_TYPE = :companyType order by company.SHORT_NAME FOR XML PATH('')), 1, 1, '')"), BimContractDTO.ATTRIBUTE.COMPANY_NAMES.getValue());
			} else {
				sqlStatement.addSelectClause("STUFF((SELECT ','+ ltrim(company.COMPANY_ID) FROM dbo.BIM_COMPANY company,dbo.BIM_CONTRACT_VENDOR vendor "
						.concat(" WHERE company.COMPANY_ID=vendor.COMPANY_ID and vendor.CONTRACT_ID=bc.CONTRACT_ID order by company.SHORT_NAME FOR XML PATH('')), 1, 1, '')"), BimContractDTO.ATTRIBUTE.COMPANY_IDS.getValue());
				sqlStatement.addSelectClause("STUFF((SELECT ','+ ltrim(company.SHORT_NAME) FROM dbo.BIM_COMPANY company,dbo.BIM_CONTRACT_VENDOR vendor "
						.concat(" WHERE company.COMPANY_ID=vendor.COMPANY_ID and vendor.CONTRACT_ID=bc.CONTRACT_ID order by company.SHORT_NAME FOR XML PATH('')), 1, 1, '')"), BimContractDTO.ATTRIBUTE.COMPANY_NAMES.getValue());
			}
			sqlStatement.addSelectClause("bm.UNITY_NUMBER", BimContractDTO.ATTRIBUTE.UNITY_NUMBER.getValue());
			sqlStatement.addSelectClause("bc.CONTRACT_CODE", BimContractDTO.ATTRIBUTE.CONTRACT_CODE.getValue());
			sqlStatement.addSelectClause("STUFF((SELECT ','+ ltrim(baseType.ITEM_VALUE) FROM dbo.BASE_PARAMETER_ITEM_DEF baseType,dbo.BIM_CONTRACT_TYPE conType "
					.concat(" WHERE conType.CONTRACT_ID=bc.CONTRACT_ID and conType.CONTRACT_TYPE=baseType.ITEM_VALUE and baseType.BPTD_CODE='CONTRACT_TYPE' FOR XML PATH('')), 1, 1, '')"), BimContractDTO.ATTRIBUTE.CONTRACT_TYPE_ID.getValue());
			sqlStatement.addSelectClause("STUFF((SELECT ','+ ltrim(baseType.ITEM_NAME) FROM dbo.BASE_PARAMETER_ITEM_DEF baseType,dbo.BIM_CONTRACT_TYPE conType "
					.concat(" WHERE conType.CONTRACT_ID=bc.CONTRACT_ID and conType.CONTRACT_TYPE=baseType.ITEM_VALUE and baseType.BPTD_CODE='CONTRACT_TYPE' FOR XML PATH('')), 1, 1, '')"), BimContractDTO.ATTRIBUTE.CONTRACT_TYPE_NAME.getValue());
			if (StringUtils.hasText(contractId)) {
				sqlStatement.addSelectClause("bc.START_DATE", BimContractDTO.ATTRIBUTE.START_DATE.getValue());
				sqlStatement.addSelectClause("bc.END_DATE", BimContractDTO.ATTRIBUTE.END_DATE.getValue());
				sqlStatement.addSelectClause("bc.WORK_HOUR_START_1", BimContractDTO.ATTRIBUTE.WORK_HOUR_START_1.getValue());
				sqlStatement.addSelectClause("bc.WORK_HOUR_END_1", BimContractDTO.ATTRIBUTE.WORK_HOUR_END_1.getValue());
				sqlStatement.addSelectClause("bc.WORK_HOUR_START_2", BimContractDTO.ATTRIBUTE.WORK_HOUR_START_2.getValue());
				sqlStatement.addSelectClause("bc.WORK_HOUR_END_2", BimContractDTO.ATTRIBUTE.WORK_HOUR_END_2.getValue());
			} else {
				sqlStatement.addSelectClause("CONCAT( CONVERT( VARCHAR( 100 ) ,bc.START_DATE ,111 ) ,'~' ,CONVERT( VARCHAR( 100 ) ,bc.END_DATE ,111 ) )", BimContractDTO.ATTRIBUTE.CONTRACT_DATE.getValue());
				sqlStatement.addSelectClause("CONCAT( CONVERT( VARCHAR( 100 ) ,bc.WORK_HOUR_START_1 ,111 ) ,'~' ,CONVERT( VARCHAR( 100 ) ,bc.WORK_HOUR_END_1 ,111 ) )", BimContractDTO.ATTRIBUTE.WORK_HOUR_1.getValue());
				sqlStatement.addSelectClause("CONCAT( CONVERT( VARCHAR( 100 ) ,bc.WORK_HOUR_START_2 ,111 ) ,'~' ,CONVERT( VARCHAR( 100 ) ,bc.WORK_HOUR_END_2 ,111 ) )", BimContractDTO.ATTRIBUTE.WORK_HOUR_2.getValue());
			}
			sqlStatement.addSelectClause("bc.CANCEL_DATE", BimContractDTO.ATTRIBUTE.CANCEL_DATE.getValue());
			sqlStatement.addSelectClause("bi.ITEM_NAME", BimContractDTO.ATTRIBUTE.CONTRACT_STATUS_NAME.getValue());
			sqlStatement.addSelectClause("bi.ITEM_VALUE", BimContractDTO.ATTRIBUTE.CONTRACT_STATUS.getValue());
			sqlStatement.addSelectClause("bc.CONTRACT_PRICE", BimContractDTO.ATTRIBUTE.CONTRACT_PRICE.getValue());
			sqlStatement.addSelectClause("b.ITEM_NAME", BimContractDTO.ATTRIBUTE.PAY_MODE_NAME.getValue());
			sqlStatement.addSelectClause("bc.PAY_MODE", BimContractDTO.ATTRIBUTE.PAY_MODE.getValue());
			sqlStatement.addSelectClause("bc.PAY_REQUIRE", BimContractDTO.ATTRIBUTE.PAY_REQUIRE.getValue());
			sqlStatement.addSelectClause("bc.FACTORY_WARRANTY", BimContractDTO.ATTRIBUTE.FACTORY_WARRANTY.getValue());
			sqlStatement.addSelectClause("bc.CUSTOMER_WARRANTY", BimContractDTO.ATTRIBUTE.CUSTOMER_WARRANTY.getValue());
			sqlStatement.addSelectClause("bc.WINDOW1", BimContractDTO.ATTRIBUTE.WINDOW_ONE.getValue());
			sqlStatement.addSelectClause("bc.WINDOW1_CONNECTION", BimContractDTO.ATTRIBUTE.WINDOW_ONE_CONN.getValue());
			sqlStatement.addSelectClause("bc.WINDOW2", BimContractDTO.ATTRIBUTE.WINDOW_TWO.getValue());
			sqlStatement.addSelectClause("bc.WINDOW2_CONNECTION", BimContractDTO.ATTRIBUTE.WINDOW_TWO_CONN.getValue());
			sqlStatement.addSelectClause("bc.COMMENT", BimContractDTO.ATTRIBUTE.COMMENT.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".BIM_CONTRACT bc");
			fromBuffer.append(" left join ").append(schema).append(".BIM_COMPANY bm on bc.COMPANY_ID = bm.COMPANY_ID");
/*			fromBuffer.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF baseStatus on bc.CONTRACT_STATUS = baseStatus.ITEM_VALUE and baseStatus.BPTD_CODE = :contractStatus");
*/			fromBuffer.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF basePay on bc.PAY_MODE = basePay.ITEM_VALUE and basePay.BPTD_CODE = :contractStatus");
			fromBuffer.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF b on b.BPTD_CODE = :payMode and b.ITEM_VALUE = bc.PAY_MODE");
			fromBuffer.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF bi on bi.BPTD_CODE= :contractStatus and bi.ITEM_VALUE = bc.CONTRACT_STATUS");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(contractId)) {
				sqlStatement.addWhereClause("bc.CONTRACT_ID = :contractId", contractId);
				
			}
			if (StringUtils.hasText(customerId)) {
				sqlStatement.addWhereClause("bc.COMPANY_ID = :customerId", customerId);
			}
			
			if (StringUtils.hasText(companyId)) {
				StringBuffer whereBuffer = new StringBuffer();
				whereBuffer.append("exists(select 1 from ").append(schema);
				whereBuffer.append(".BIM_CONTRACT_VENDOR where contract_id = bc.CONTRACT_ID and company_id = :companyId)");
				
				//whereBuffer.append("bc.CONTRACT_ID in (select contract_id from ").append(schema);
				//whereBuffer.append(".BIM_CONTRACT_VENDOR where company_id = :companyId)");
				sqlStatement.addWhereClause(whereBuffer.toString());
			}
			
			if (StringUtils.hasText(sort) && StringUtils.hasText(order)) {
				sqlStatement.setOrderByExpression(sort.concat(" ").concat(order));
			}
			sqlStatement.addWhereClause("bc.DELETED = :status", IAtomsConstants.NO);
			sqlStatement.setPageSize(rows);
			sqlStatement.setStartPage(page - 1);
			AliasBean aliasBean = sqlStatement.createAliasBean(BimContractDTO.class);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if (StringUtils.hasText(contractId)) {
				sqlQueryBean.setParameter("status", IAtomsConstants.NO);
				sqlQueryBean.setParameter("companyType", IAtomsConstants.PARAM_COMPANY_TYPE_VENDOR);
			}
			if (StringUtils.hasText(companyId)) {
				sqlQueryBean.setParameter(BimContractDTO.ATTRIBUTE.COMPANY_ID.getValue(), companyId);
			}
			sqlQueryBean.setParameter(BimContractDTO.ATTRIBUTE.PAY_MODE.getValue(), IAtomsConstants.PARAM_BPTD_CODE_PAY_MODE);
			sqlQueryBean.setParameter(BimContractDTO.ATTRIBUTE.CONTRACT_STATUS.getValue(), IAtomsConstants.PARAM_BPTD_CODE_CONTRACT_STATUS);
			LOGGER.debug("SQL---------->", sqlQueryBean.toString());
			listContract = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(".listBy() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_CONTRACT)}, e);
		}
		return listContract;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractDAO#isCheck(java.lang.String, java.lang.String)
	 */
	@Override
	public Boolean isCheck(String contractCode, String contractId) throws DataAccessException {
		LOGGER.debug(".check()", contractCode);
		try {
			String schma = this.getMySchema();	
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schma).append(".BIM_CONTRACT");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(contractId)) {
				sqlStatement.addWhereClause("CONTRACT_ID <> :contractId", contractId);
			}
			if (StringUtils.hasText(contractCode)) {
				sqlStatement.addWhereClause("CONTRACT_CODE = :contractCode", contractCode);
			}
			
			sqlStatement.addWhereClause("isnull(DELETED,'N') = :deleted", IAtomsConstants.NO);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug(".checkRepeat()", "sql:", sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				if (result.get(0).intValue() > 0) {
					return Boolean.TRUE;
				}
			}
			return Boolean.FALSE;
		} catch (Exception e) {
			LOGGER.error(".check() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_CONTRACT)}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractDAO#getCustomerIdBycontractId(java.lang.String)
	 */
	@Override
	public String getCustomerIdBycontractId(String contractId) throws DataAccessException {
		LOGGER.debug("ContractDAO.getCustomerIdBycontractId()", contractId);
		try {
			String schema = this.getMySchema();
			
			//拿到合約列表
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("c.CUSTOMER_ID", BimContractDTO.ATTRIBUTE.CUSTOMER_ID.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".BIM_CONTRACT c");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(contractId)) {
				sqlStatement.addWhereClause("CONTRACT_ID =:contractId", contractId);
			}
			SqlQueryBean sql = sqlStatement.createSqlQueryBean();
			LOGGER.debug("SQL---------->", sql.toString());
			AliasBean aliasBean = new AliasBean(BimContractDTO.class);
			List<BimContractDTO> contractManageDTOs= this.getDaoSupport().findByNativeSql(sql, aliasBean);
			if (!CollectionUtils.isEmpty(contractManageDTOs)) {
				return contractManageDTOs.get(0).getCustomerId();
			}
		} catch (Exception e) {
			LOGGER.error(".getCustomerIdBycontractId is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_CONTRACT)}, e);
		}
		return contractId;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractDAO#getContractByCustomerAndStatus(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Parameter> getContractByCustomerAndStatus(String customerId, String contractStatus, String companyType) throws DataAccessException {
		LOGGER.debug(".getContractByCustomerAndStatus()", "parameters:customerId=", customerId);
		LOGGER.debug(".getContractByCustomerAndStatus()", "parameters:contractStatus=", contractStatus);
		List<Parameter> contractList = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sql = new SqlStatement();
			sql.addSelectClause("con.CONTRACT_ID", Parameter.FIELD_VALUE);
			sql.addSelectClause("con.CONTRACT_CODE", Parameter.FIELD_NAME);
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".BIM_CONTRACT con");
			fromBuffer.append(" LEFT JOIN ").append(schema).append(".BIM_COMPANY com ON con.COMPANY_ID = com.COMPANY_ID");
			fromBuffer.append(" LEFT JOIN ").append(schema).append(".BIM_COMPANY_TYPE t on com.COMPANY_ID = t.COMPANY_ID ");
			sql.setFromExpression(fromBuffer.toString());
			// 有客戶編號
			if (StringUtils.hasText(customerId)) {
				sql.addWhereClause(" con.COMPANY_ID=:customerId", customerId);
			}
			// 限制合約狀態
			if (StringUtils.hasText(contractStatus)) {
				sql.addWhereClause(" con.CONTRACT_STATUS=:contractStatus", contractStatus);
			}
			// 有客戶編號
			if (StringUtils.hasText(companyType)) {
				sql.addWhereClause(" t.COMPANY_TYPE=:companyType", companyType);
			}
			sql.addWhereClause("ISNULL(con.DELETED, 'N') =:deleted", IAtomsConstants.NO);
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			LOGGER.debug(".getContractByCustomerAndStatus() SQL---------->", sql.toString());
			AliasBean aliasBean = sql.createAliasBean(Parameter.class);
			contractList = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			} catch (Exception e) {
				LOGGER.error(".getContractByCustomerAndStatus() is error", e);
				throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
			} 
		return contractList;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractDAO#getContractCodeList(java.lang.String, java.lang.String, java.lang.Boolean, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Parameter> getContractCodeList(String customerId, String contractStatus, Boolean isHaveSla, String order, String sort, Boolean ignoreDeleted) throws DataAccessException {
		LOGGER.debug(".getContractCodeList()", "parameters:customerId=", customerId);
		LOGGER.debug(".getContractCodeList()", "parameters:contractStatus=", contractStatus);
		LOGGER.debug(".getContractCodeList()", "parameters:isHaveSla=" + isHaveSla);
		LOGGER.debug(".getContractCodeList()", "parameters:isHaveSla=" + ignoreDeleted);
		long startQueryContractTime = System.currentTimeMillis();
		List<Parameter> contractCodeList = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sql = new SqlStatement();
			sql.addSelectClause("contract.CONTRACT_ID", Parameter.FIELD_VALUE);
			sql.addSelectClause("contract.CONTRACT_CODE", Parameter.FIELD_NAME);
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".BIM_CONTRACT contract");
			sql.setFromExpression(fromBuffer.toString());
			if(ignoreDeleted){
				sql.addWhereClause("ISNULL(contract.DELETED, 'N') =:deleted", IAtomsConstants.NO);
			}
			// 有客戶編號
			if (StringUtils.hasText(customerId)) {
				sql.addWhereClause(" contract.COMPANY_ID=:customerId", customerId);
			}
			// 限制合約狀態
			if (StringUtils.hasText(contractStatus)) {
				sql.addWhereClause(" contract.CONTRACT_STATUS=:contractStatus", contractStatus);
			}
			// 是否有sla信息
			if (isHaveSla) {
				StringBuffer whereBuffer = new StringBuffer();
				whereBuffer.append("EXISTS(SELECT 1 FROM ").append(schema).append(".BIM_SLA sla WHERE contract.CONTRACT_ID = sla.CONTRACT_ID)");
				sql.addWhereClause(whereBuffer.toString());
			}
			if (StringUtils.hasText(order) && StringUtils.hasText(order)) {
				sql.setOrderByExpression(order + " " + sort);
			}
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			LOGGER.debug(".getContractCodeList() SQL---------->", sql.toString());
			AliasBean aliasBean = sql.createAliasBean(Parameter.class);
			contractCodeList = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(".getContractCodeList() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		long endQueryContractTime = System.currentTimeMillis();
		LOGGER.debug("calculate time --> load", "DAO getContractCodeList:" + (endQueryContractTime - startQueryContractTime));
		return contractCodeList;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractDAO#listEdcContractExpireInformReport(java.lang.String, java.util.Date)
	 */
	@Override
	public List<BimContractDTO> listEdcContractExpireInformReport(
			String contractStatus, Date startDate, Date endDate) throws DataAccessException {
		//打印傳遞的參數
		LOGGER.debug("listEdcContractExpireInformReport()", "parameters:contractStatus", contractStatus);
		LOGGER.debug("listEdcContractExpireInformReport()", "parameters:endDate", endDate.toString());
		List<BimContractDTO> contractDTOs = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			StringBuffer stringBuffer = new StringBuffer();
			sqlStatement.addSelectClause("bc.CONTRACT_ID", BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue().toString());
			sqlStatement.addSelectClause("bcy1.SHORT_NAME", BimContractDTO.ATTRIBUTE.COMPANY_NAME.getValue().toString());
			stringBuffer.append("stuff( ( select '").append(IAtomsConstants.MARK_SEPARATOR).append("' + LTRIM( bcy.SHORT_NAME ) FROM ");
			stringBuffer.append(schema).append(".BIM_CONTRACT_VENDOR bcv LEFT JOIN ").append(schema).append(".BIM_COMPANY bcy ON bcv.COMPANY_ID = bcy.COMPANY_ID WHERE ");
			stringBuffer.append("bc.CONTRACT_ID = bcv.CONTRACT_ID FOR XML PATH('') )").append(IAtomsConstants.MARK_SEPARATOR).append("1,1,'' )");
			sqlStatement.addSelectClause(stringBuffer.toString(), BimContractDTO.ATTRIBUTE.VENDOR_NAME.getValue().toString());
			sqlStatement.addSelectClause("bc.CONTRACT_CODE", BimContractDTO.ATTRIBUTE.CONTRACT_CODE.getValue().toString());
			sqlStatement.addSelectClause("CONVERT( VARCHAR( 10 ) ,bc.END_DATE ,111 )", BimContractDTO.ATTRIBUTE.END_DATE_STRING.getValue().toString());
			stringBuffer.delete(0, stringBuffer.length());
			stringBuffer.append(schema).append(".BIM_CONTRACT bc LEFT JOIN ").append(schema).append(".BIM_COMPANY bcy1 ON bc.COMPANY_ID = bcy1.COMPANY_ID");
			sqlStatement.addFromExpression(stringBuffer.toString());
			if(StringUtils.hasText(contractStatus)){
				sqlStatement.addWhereClause("bc.CONTRACT_STATUS = :contractStatus", contractStatus);
			}
			if(startDate != null){
				sqlStatement.addWhereClause("bc.END_DATE >= :startDate", startDate);
			}
			if(endDate != null){
				sqlStatement.addWhereClause("bc.END_DATE < :endDate", endDate);
			}
			sqlStatement.addWhereClause("ISNULL(bc.DELETED, 'N') =:deleted", IAtomsConstants.NO);
			sqlStatement.setOrderByExpression("bc.END_DATE");
			SqlQueryBean sql = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(BimContractDTO.class);
			LOGGER.debug(".listEdcContractExpireInformReport()", "sql ---> ", sql.toString());
			contractDTOs = super.getDaoSupport().findByNativeSql(sql, aliasBean);
		} catch (Exception e) {
			LOGGER.error(".listEdcContractExpireInformReport() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return contractDTOs;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractDAO#getContractDTOByContractCode(java.lang.String)
	 */
	public BimContractDTO getContractDTOByContractCode(String contractCode) throws DataAccessException {
		//打印傳遞的參數
		LOGGER.debug("listEdcContractExpireInformReport()", "parameters:contractCode", contractCode);
		List<BimContractDTO> contractDTOs = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			StringBuffer stringBuffer = new StringBuffer();
			sqlStatement.addSelectClause("contract.CONTRACT_ID", BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue().toString());
			sqlStatement.addSelectClause("contract.COMPANY_ID", BimContractDTO.ATTRIBUTE.COMPANY_ID.getValue().toString());
			stringBuffer.append(schema).append(".BIM_CONTRACT contract");
			sqlStatement.addFromExpression(stringBuffer.toString());
			if(StringUtils.hasText(contractCode)){
				sqlStatement.addWhereClause("contract.CONTRACT_CODE = :contractCode", contractCode);
			}
			sqlStatement.addWhereClause("contract.DELETED = :deleted", IAtomsConstants.NO);
			SqlQueryBean sql = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(BimContractDTO.class);
			LOGGER.debug(".listEdcContractExpireInformReport()", "sql ---> ", sql.toString());
			contractDTOs = super.getDaoSupport().findByNativeSql(sql, aliasBean);
			if (!CollectionUtils.isEmpty(contractDTOs)) {
				return contractDTOs.get(0);
			}
		} catch (Exception e) {
			LOGGER.error(".listEdcContractExpireInformReport() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractDAO#getVendorBy(java.lang.String, java.lang.String)
	 */
	public BimContractDTO getVendorBy(String customerName, String contractId) throws DataAccessException {
		//打印傳遞的參數
			LOGGER.debug("listEdcContractExpireInformReport()", "parameters:contractId", contractId);
			LOGGER.debug("listEdcContractExpireInformReport()", "parameters:customerName", customerName);
			List<BimContractDTO> contractDTOs = null;
			try {
				String schema = this.getMySchema();
				SqlStatement sqlStatement = new SqlStatement();
				sqlStatement.addSelectClause("vendor.COMPANY_ID", BimContractDTO.ATTRIBUTE.COMPANY_ID.getValue().toString());
				StringBuffer fromBuffer = new StringBuffer();
				fromBuffer.append(schema).append(".BIM_CONTRACT contract ");
				fromBuffer.append("left join ").append(schema).append(".BIM_CONTRACT_VENDOR vendor on vendor.CONTRACT_ID = contract.CONTRACT_ID ");
				fromBuffer.append("left join ").append(schema).append(".BIM_COMPANY company on company.COMPANY_ID = vendor.COMPANY_ID");
				sqlStatement.addFromExpression(fromBuffer.toString());
				if(StringUtils.hasText(customerName)){
					sqlStatement.addWhereClause("company.SHORT_NAME = :customerName", customerName);
				}
				if(StringUtils.hasText(contractId)){
					sqlStatement.addWhereClause("contract.CONTRACT_ID = :contractId", contractId);
				}
				sqlStatement.addWhereClause("contract.DELETED = :deleted", IAtomsConstants.NO);
				SqlQueryBean sql = sqlStatement.createSqlQueryBean();
				AliasBean aliasBean = sqlStatement.createAliasBean(BimContractDTO.class);
					LOGGER.debug(".listEdcContractExpireInformReport()", "sql ---> ", sql.toString());
				contractDTOs = super.getDaoSupport().findByNativeSql(sql, aliasBean);
				if (!CollectionUtils.isEmpty(contractDTOs)) {
					return contractDTOs.get(0);
				}
			} catch (Exception e) {
				LOGGER.error(".listEdcContractExpireInformReport() is error", e);
				throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
			}
			return null;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractDAO#listContractDTOByIds(java.util.List)
	 */
	public List<BimContractDTO> listContractDTOByIds(List<String> contractIds) throws DataAccessException {
		List<BimContractDTO> contractDTOs = null;
		try{
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("contract.CUSTOMER_WARRANTY", BimContractDTO.ATTRIBUTE.CUSTOMER_WARRANTY.getValue());
			sqlStatement.addSelectClause("contract.FACTORY_WARRANTY", BimContractDTO.ATTRIBUTE.FACTORY_WARRANTY.getValue());
			sqlStatement.addSelectClause("contract.CONTRACT_ID", BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue());
			sqlStatement.addFromExpression(schema + ".BIM_CONTRACT contract");
			if (!CollectionUtils.isEmpty(contractIds)){
				sqlStatement.addWhereClause("contract.CONTRACT_ID in ( :contractId )");
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if (!CollectionUtils.isEmpty(contractIds)){
				sqlQueryBean.setParameter(BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue(), contractIds);
			}
			AliasBean aliasBean = sqlStatement.createAliasBean(BimContractDTO.class);
			contractDTOs = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return contractDTOs;
		}catch (Exception e) {
			LOGGER.error(".listContractDTOByIds() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractDAO#isNoData()
	 */
	@Override
	public boolean isNoData() throws DataAccessException {
		boolean isNoData = false;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("COUNT(1)");
			sqlStatement.addFromExpression(schema + ".BIM_CONTRACT");
			//打印SQL語句
			LOGGER.debug("isNoData()", "SQL : ", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				Integer count = result.get(0);
				if(count == 0){
					isNoData = true;
				}
			}
		} catch (Exception e) {
			LOGGER.error("isNoData()", "error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE);
		}
		return isNoData;
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractDAO#getContractByCode(java.lang.String)
	 */
	@Override
	public List<Parameter> getContractByCode(String contractCode) throws DataAccessException {
		LOGGER.debug(".getContractByCode()", "parameters:warehouseName=" + contractCode);
		List<Parameter> result = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("contract.CONTRACT_ID", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("contract.CONTRACT_CODE", Parameter.FIELD_NAME);
			sqlStatement.addFromExpression(schema + ".BIM_CONTRACT contract");
			if (StringUtils.hasText(contractCode)) {
				sqlStatement.addWhereClause("contract.CONTRACT_CODE =:contractCode", contractCode);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			LOGGER.debug(".getContractByCode()", "SQL---------->"+sqlStatement.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".getContractByCode() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return result;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractDAO#getContractList()
	 */
	@Override
	public List<BimContractDTO> getContractList() throws DataAccessException {
		List<BimContractDTO> contractDTOs = null;
		try{
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("contract.CONTRACT_CODE", BimContractDTO.ATTRIBUTE.CONTRACT_CODE.getValue());
			sqlStatement.addSelectClause("contract.COMPANY_ID", BimContractDTO.ATTRIBUTE.COMPANY_ID.getValue());
			sqlStatement.addSelectClause("contract.CONTRACT_ID", BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue());
			sqlStatement.addFromExpression(schema + ".BIM_CONTRACT contract");
			
			// 設置排序
			sqlStatement.setOrderByExpression("companyId, contractCode");
				
			sqlStatement.addWhereClause("contract.DELETED = :isDeleted", IAtomsConstants.NO);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug(".getContractList()", "SQL---------->"+sqlStatement.toString());
			AliasBean aliasBean = sqlStatement.createAliasBean(BimContractDTO.class);
			contractDTOs = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return contractDTOs;
		}catch (Exception e) {
			LOGGER.error(".getContractList() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractDAO#deleteTransferData()
	 */
	@Override
	public void deleteTransferData() throws DataAccessException {
		try {
			String schema = this.getMySchema();
			SqlQueryBean sqlQueryBean = new SqlQueryBean("delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_ASSET_FUNCTION; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_ASSET_LINK; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_TRANSACTION_PARAMETER; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_TRANSACTION; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_ATT_FILE; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_COMM_MODE; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_HANDLE_INFO; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_NEW_ASSET_LINK; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_NEW_ASSET_FUNCTION; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_NEW_TRANSACTION_PARAMETER; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_NEW_COMM_MODE; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_NEW_HANDLE_INFO; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_FAULT_DESC; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_FAULT_COM; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY_DESC; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY_COMM; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".BIM_SLA; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_ASSET_IN_LIST; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_ASSET_IN_INFO; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".BIM_CONTRACT_VENDOR; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".BIM_CONTRACT_TYPE; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".BIM_CONTRACT_ATTACHED_FILE; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".BIM_CONTRACT_ASSET; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".BIM_CONTRACT; ");
			
			LOGGER.debug("deleteTransferData()", "sql:" + sqlQueryBean.toString());
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("deleteTransferData()", e.getMessage(), e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractDAO#getEdcAssetContract()
	 */
	@Override
	public List<BimContractDTO> getEdcAssetContract(String customerId) throws DataAccessException {
		LOGGER.debug(".getEdcAssetContract()", "parameters:customerId=" + customerId);
		List<BimContractDTO> contractDTOs = null;
		try{
			String schema = this.getMySchema();
			StringBuilder builder = new StringBuilder();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("top 1 contract.CONTRACT_CODE", BimContractDTO.ATTRIBUTE.CONTRACT_CODE.getValue());
			sqlStatement.addSelectClause("contract.CONTRACT_ID", BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue());
			
			builder.delete(0, builder.length());
			builder.append("(case when assetType.ASSET_CATEGORY = 'EDC' then 1 ");
			builder.append(" when assetType.ASSET_CATEGORY = 'Related_Products' then 2 else 3 end) ");
			sqlStatement.addSelectClause(builder.toString(), "index1");
			
			builder.delete(0, builder.length());
			builder.append(schema).append(".BIM_CONTRACT contract left join ");
			builder.append(schema).append(".BIM_CONTRACT_ASSET contractAsset on contractAsset.CONTRACT_ID = contract.CONTRACT_ID left join ");
			builder.append(schema).append(".DMM_ASSET_TYPE assetType on assetType.ASSET_TYPE_ID = contractAsset.ASSET_TYPE_ID ");
			sqlStatement.addFromExpression(builder.toString());
			
			if(StringUtils.hasText(customerId)){
				sqlStatement.addWhereClause("contract.COMPANY_ID =:customerId", customerId);
			}
			sqlStatement.addWhereClause("contract.DELETED =:isDeleted", IAtomsConstants.NO);
			
			sqlStatement.setOrderByExpression("index1,contract.CONTRACT_CODE");
			
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug(".getEdcAssetContract()", "SQL---------->"+sqlStatement.toString());
			AliasBean aliasBean = sqlStatement.createAliasBean(BimContractDTO.class);
			contractDTOs = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return contractDTOs;
		}catch (Exception e) {
			LOGGER.error(".getEdcAssetContract() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractDAO#getContractByCustomer(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Parameter> getContractByCustomer(String customerId, String contractStatus) throws DataAccessException {
		LOGGER.debug(".getContractByCustomer()", "parameters:customerId=", customerId);
		LOGGER.debug(".getContractByCustomer()", "parameters:contractStatus=", contractStatus);
		List<Parameter> contractList = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sql = new SqlStatement();
			sql.addSelectClause("con.CONTRACT_ID", Parameter.FIELD_VALUE);
			sql.addSelectClause("con.CONTRACT_CODE", Parameter.FIELD_NAME);
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".BIM_CONTRACT con");
			sql.setFromExpression(fromBuffer.toString());
			// 有客戶編號
			if (StringUtils.hasText(customerId)) {
				sql.addWhereClause(" con.COMPANY_ID=:customerId", customerId);
			}
			// 限制合約狀態
			if (StringUtils.hasText(contractStatus)) {
				sql.addWhereClause(" con.CONTRACT_STATUS=:contractStatus", contractStatus);
			}
			sql.addWhereClause("ISNULL(con.DELETED, 'N') =:deleted", IAtomsConstants.NO);
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			LOGGER.debug(".getContractByCustomer() SQL---------->", sql.toString());
			AliasBean aliasBean = sql.createAliasBean(Parameter.class);
			contractList = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			} catch (Exception e) {
				LOGGER.error(".getContractByCustomer() is error", e);
				throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
			} 
		return contractList;
	}
}
