package com.cybersoft4u.xian.iatoms.dataTransfer.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.util.CollectionUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.config.GenericConfigManager;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ApplicationDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetTypeDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BaseParameterItemDefDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimCalendarDayDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimCalendarYearDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimMerchantHeaderDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryHistoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.MerchantDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAssetLinkDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAttFileDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTransactionDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.WarehouseDTO;
import com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmUser;
/**
 * Purpose: 舊資料轉檔數據訪問層實現類
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2017-7-13
 * @MaintenancePersonnel CrissZhang
 */
public class OldDataTransferDAO extends GenericBaseDAO implements IOldDataTransferDAO{
	
	/**
	 * 系统日志文件控件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, OldDataTransferDAO.class);

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#listCalendarYear()
	 */
	@Override
	public List<BimCalendarYearDTO> listCalendarYear()throws DataAccessException {
		long startTime = System.currentTimeMillis();
		
		List<BimCalendarYearDTO> result = null;
		//得到schema
		String schema = this.getSchema("iatoms-foms");
		SqlStatement sqlStatement = new SqlStatement();
		StringBuilder builder = new StringBuilder();
		try {
			sqlStatement.addSelectClause("a.year", BimCalendarYearDTO.ATTRIBUTE.YEAR.getValue());
			sqlStatement.addSelectClause("case when a.monday=0 then 'N' else 'Y' end", BimCalendarYearDTO.ATTRIBUTE.MONDAY.getValue());
			sqlStatement.addSelectClause("case when a.tuesday=0 then 'N' else 'Y' end", BimCalendarYearDTO.ATTRIBUTE.TUESDAY.getValue());
			sqlStatement.addSelectClause("case when a.wednesday=0 then 'N' else 'Y' end", BimCalendarYearDTO.ATTRIBUTE.WEDNESDAY.getValue());
			sqlStatement.addSelectClause("case when a.thursday=0 then 'N' else 'Y' end", BimCalendarYearDTO.ATTRIBUTE.THURSDAY.getValue());
			sqlStatement.addSelectClause("case when a.friday=0 then 'N' else 'Y' end", BimCalendarYearDTO.ATTRIBUTE.FRIDAY.getValue());
			sqlStatement.addSelectClause("case when a.saturday=0 then 'N' else 'Y' end", BimCalendarYearDTO.ATTRIBUTE.SATURDAY.getValue());
			sqlStatement.addSelectClause("case when a.sunday=0 then 'N' else 'Y' end", BimCalendarYearDTO.ATTRIBUTE.SUNDAY.getValue());
			// 新增人員編號
			sqlStatement.addSelectClause("a.MODIFYINFO_ADD_USER_ID", BimCalendarYearDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			// 新增人員姓名
			sqlStatement.addSelectClause("b.name", BimCalendarYearDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			// 新增日期
			sqlStatement.addSelectClause("a.MODIFYINFO_ADD_DATE", BimCalendarYearDTO.ATTRIBUTE.CREATED_DATE.getValue());
			// 異動人員編號
			sqlStatement.addSelectClause("a.MODIFYINFO_MODIFY_USER_ID", BimCalendarYearDTO.ATTRIBUTE.UPDATED_BY_ID.getValue());
			// 異動人員姓名
			sqlStatement.addSelectClause("c.name", BimCalendarYearDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			// 異動日期
			sqlStatement.addSelectClause("a.MODIFYINFO_MODIFY_DATE", BimCalendarYearDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			
			builder.append(schema).append(".CCM_Year a left join ");
			builder.append(schema).append(".ORG_PARTY b on a.MODIFYINFO_ADD_USER_ID=b.identifier left join ");
			builder.append(schema).append(".ORG_PARTY c on a.MODIFYINFO_MODIFY_USER_ID=c.identifier");
			sqlStatement.addFromExpression(builder.toString());
			
			builder.delete(0, builder.length());
			builder.append("a.year in (convert(int,substring(convert(varchar, getdate(),112),1,4)),");
			builder.append("(convert(int,substring(convert(varchar, getdate(),112),1,4)) + 1)) ");
			builder.append(" and a.deleted=0 ");
			sqlStatement.addWhereClause(builder.toString());
		//	sqlStatement.addWhereClause("a.year in (2017,2018) and a.deleted=0");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(BimCalendarYearDTO.class);
			LOGGER.debug("listCalendarYear()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			
			long endTime = System.currentTimeMillis();
			LOGGER.debug("listCalendarYear()", "testSpeedTime DAO totalTime:" + (endTime - startTime));
			
			return result;
		} catch (Exception e) {
			LOGGER.error("listCalendarYear()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#listCalendarDate()
	 */
	@Override
	public List<BimCalendarDayDTO> listCalendarDate()throws DataAccessException {
		long startTime = System.currentTimeMillis();
		
		List<BimCalendarDayDTO> result = null;
		//得到schema
		String schema = this.getSchema("iatoms-foms");
		SqlStatement sqlStatement = new SqlStatement();
		StringBuilder builder = new StringBuilder();
		try {
			// 日期
			sqlStatement.addSelectClause("a.date", BimCalendarDayDTO.ATTRIBUTE.DAY.getValue());
			// 是否為假日
		//	sqlStatement.addSelectClause("case when a.Special=0 then 'N' else 'Y' end", BimCalendarDayDTO.ATTRIBUTE.IS_HOLIDAY.getValue());
			sqlStatement.addSelectClause("case when a.Special is null then 'N' else 'Y' end", BimCalendarDayDTO.ATTRIBUTE.IS_HOLIDAY.getValue());
			// 說明
			sqlStatement.addSelectClause("a.remark", BimCalendarDayDTO.ATTRIBUTE.COMMENT.getValue());
			// 新增人員編號
			sqlStatement.addSelectClause("a.MODIFYINFO_ADD_USER_ID", BimCalendarDayDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			// 新增人員姓名
			sqlStatement.addSelectClause("b.name", BimCalendarDayDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			// 新增日期
			sqlStatement.addSelectClause("a.MODIFYINFO_ADD_DATE", BimCalendarDayDTO.ATTRIBUTE.CREATED_DATE.getValue());
			// 異動人員編號
			sqlStatement.addSelectClause("a.MODIFYINFO_MODIFY_USER_ID", BimCalendarDayDTO.ATTRIBUTE.UPDATED_BY_ID.getValue());
			// 異動人員姓名
			sqlStatement.addSelectClause("c.name", BimCalendarDayDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			// 異動日期
			sqlStatement.addSelectClause("a.MODIFYINFO_MODIFY_DATE", BimCalendarDayDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			
			builder.append(schema).append(".CCM_Holiday a left join ");
			builder.append(schema).append(".ORG_PARTY b on a.MODIFYINFO_ADD_USER_ID=cast(b.[identifier] as nvarchar) left join ");
			builder.append(schema).append(".ORG_PARTY c on a.MODIFYINFO_MODIFY_USER_ID=cast(c.[identifier] as nvarchar)");
			sqlStatement.addFromExpression(builder.toString());
			
/*			builder.delete(0, builder.length());
			builder.append("a.YEAR_ID in (select identifier from ");
			builder.append(schema).append(".CCM_Year where year in (2017,2018) and deleted=0) and a.deleted=0");
			sqlStatement.addWhereClause(builder.toString());*/
			
			builder.delete(0, builder.length());
			builder.append("a.YEAR_ID in (select identifier from ");
			builder.append(schema).append(".CCM_Year where year in (convert(int,");
			builder.append("substring(convert(varchar, getdate(),112),1,4)),");
			builder.append("(convert(int,substring(convert(varchar, getdate(),112),1,4)) + 1))");
			builder.append(" and deleted=0) and a.deleted=0");
			sqlStatement.addWhereClause(builder.toString());
			
			sqlStatement.setOrderByExpression("a.date");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(BimCalendarDayDTO.class);
			LOGGER.debug("listCalendarDate()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			
			long endTime = System.currentTimeMillis();
			LOGGER.debug("listCalendarDate()", "testSpeedTime DAO totalTime:" + (endTime - startTime));
			
			return result;
		} catch (Exception e) {
			LOGGER.error("listCalendarDate()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#listFaultComponentData()
	 */
	@Override
	public List<BaseParameterItemDefDTO> listFaultComponentData() throws DataAccessException {
		long startTime = System.currentTimeMillis();
		
		List<BaseParameterItemDefDTO> result = null;
		//得到schema
		String schema = this.getSchema("iatoms-aims");
		SqlStatement sqlStatement = new SqlStatement();
		StringBuilder builder = new StringBuilder();
		try {
			sqlStatement.addSelectClause("Option_Item", BaseParameterItemDefDTO.ATTRIBUTE.ITEM_NAME.getValue());
			
			builder.append("AIMS").append(IAtomsConstants.MARK_NO);
			builder.append(schema).append(".Core_Option");
			sqlStatement.addFromExpression(builder.toString());
			
			sqlStatement.addWhereClause("Option_type='Parts'");
			
			sqlStatement.setOrderByExpression("id");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(BaseParameterItemDefDTO.class);
			LOGGER.debug("listFaultComponentData()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			
			long endTime = System.currentTimeMillis();
			LOGGER.debug("listFaultComponentData()", "testSpeedTime DAO totalTime:" + (endTime - startTime));
			
			return result;
		} catch (Exception e) {
			LOGGER.error("listFaultComponentData()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#listFaultDescriptionData()
	 */
	@Override
	public List<BaseParameterItemDefDTO> listFaultDescriptionData() throws DataAccessException {
		long startTime = System.currentTimeMillis();
		
		List<BaseParameterItemDefDTO> result = null;
		//得到schema
		String schema = this.getSchema("iatoms-aims");
		SqlStatement sqlStatement = new SqlStatement();
		StringBuilder builder = new StringBuilder();
		try {
			sqlStatement.addSelectClause("Option_Item", BaseParameterItemDefDTO.ATTRIBUTE.ITEM_NAME.getValue());
			
			builder.append("AIMS").append(IAtomsConstants.MARK_NO);
			builder.append(schema).append(".Core_Option");
			sqlStatement.addFromExpression(builder.toString());
			
			sqlStatement.addWhereClause("Option_type='Condition'");
			
			sqlStatement.setOrderByExpression("id");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(BaseParameterItemDefDTO.class);
			LOGGER.debug("listFaultComponentData()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			
			long endTime = System.currentTimeMillis();
			LOGGER.debug("listFaultComponentData()", "testSpeedTime DAO totalTime:" + (endTime - startTime));
			
			return result;
		} catch (Exception e) {
			LOGGER.error("listFaultComponentData()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#listCompanyData()
	 */
	@Override
	public List<CompanyDTO> listCompanyData() throws DataAccessException {
		long startTime = System.currentTimeMillis();
		
		List<CompanyDTO> result = null;
		//得到schema
		String schema = this.getSchema("iatoms-foms");
		SqlQueryBean sqlQueryBean = new SqlQueryBean();
		try {
			sqlQueryBean.append("select a.compShortCode as companyCode,")
			.append("b.name as shortName,")
			.append("a.CORPORATION_ID as unityNumber,")
			.append("a.INVOICE_TITLE as invoiceHeader,")
			.append("a.BOSS as leader,")
			.append("a.TELEPHONE as tel,")
			.append("a.FAX as fax,")
			.append("a.APPLY_AMOUNT_DATE as applyDate,")
			.append("a.PAY_DATE as payDate,")
			.append("a.LIAISON as contact,")
			.append("a.LIAISON_TELEPHONE as contactTel,")
			.append("a.LIAISON_EMAIL as contactEmail,")
			.append("case when a.CORPORATION_ID='70543978' then null else a.shortCode end as customerCode,")
			.append("a.EMAIL as companyEmail,")
			
		//	.append("case when CORPORATION_ID='70543978' then null when CORPORATION_ID='03036306' then 'AUTO' else 'SAME' end as dtidType,")
			.append("case when CORPORATION_ID='70543978' then null")
		//	.append(" when CORPORATION_ID in ('03036306','16091049') then 'AUTO'")
			.append(" when CORPORATION_ID in ('03036306','16091049','12753335') then 'AUTO'")
			.append(" else 'SAME' end as dtidType,")
			
			.append("case when CORPORATION_ID='70543978' then 'CYBER_AUTHEN' else 'IATOMS_AUTHEN' end as authenticationType,")
			.append("case when substring(a.ADDRESS,1,3)='台北縣' then '新北市' when substring(a.ADDRESS,1,3)='臺北市' then '台北市' ")
			.append(" else substring(a.ADDRESS,1,3) end as addressLocation,")
			.append("substring(a.ADDRESS,4,len(a.ADDRESS)-3) as address,")
			.append("case when substring(a.INVOICE_ADDRESS,1,3)='台北縣' then '新北市' when substring(a.INVOICE_ADDRESS,1,3)='臺北市' then '台北市' ")
			.append("else substring(a.INVOICE_ADDRESS,1,3) end as invoiceAddressLocation,")
			.append("substring(a.INVOICE_ADDRESS,4,len(a.INVOICE_ADDRESS)-3) as invoiceAddress,")
			.append("a.REMARK as remark,")
			.append("a.MODIFYINFO_ADD_USER_ID as createdById,")
			.append("c.name as createdByName,")
			.append("a.MODIFYINFO_ADD_DATE as createdDate,")
			.append("a.MODIFYINFO_MODIFY_USER_ID as updatedById,")
			.append("d.name as updatedByName,")
			.append("a.MODIFYINFO_MODIFY_DATE as updatedDate,")
			.append("'CUSTOMER' as companyType,")
			.append("case when b.deleted=0 then 'N' else 'Y' end as deleted")
			.append(" from").append(schema).append(".CUM_CORPORATION a ")
			.append(" left join ").append(schema).append(".ORG_PARTY b on a.identifier=b.identifier")
			.append(" left join ").append(schema).append(".ORG_PARTY c on a.MODIFYINFO_ADD_USER_ID = cast(c.[identifier] as nvarchar)")
			.append(" left join ").append(schema).append(".ORG_PARTY d on a.MODIFYINFO_MODIFY_USER_ID = cast(d.[identifier] as nvarchar)")
			.append(" where (a.servicetype='1' or a.CORPORATION_ID in ('70543978')) ")
			
			.append(" union ")
			
			.append("select a.compShortCode as companyCode,")
			.append("a.INVOICE_TITLE as invoiceHeader,")
			.append("a.FACTORY_ID as unityNumber,")
			.append("a.INVOICE_TITLE as shortName,")
			.append("a.BOSS as leader,")
			.append("a.TELEPHONE as tel,")
			.append("a.FAX as fax,")
			.append("null as applyDate,")
			.append("null as payDate,")
			.append("c.name as contact,")
			.append("null as contactTel,")
			.append("null as contactEmail,")
			.append("null as customerCode,")
			.append("a.EMAIL as companyEmail,")
			.append("null as dtidType,")
			.append("'IATOMS_AUTHEN' as authenticationType,")
			.append("case when substring(a.ADDRESS,1,3)='臺北市' then '台北市' else substring(a.ADDRESS,1,3) end as addressLocation,")
			.append("substring(a.ADDRESS,4,len(a.ADDRESS)-3) as address,")
			.append("null as invoiceAddressLocation,")
			.append("null as invoiceAddress,")
			.append("a.remark as remark,")
			.append("a.MODIFYINFO_ADD_USER_ID createdById,")
			.append("d.name as createdByName,")
			.append("a.MODIFYINFO_ADD_DATE as createdDate,")
			.append("a.MODIFYINFO_MODIFY_USER_ID as updatedById,")
			.append("e.name as updatedByName,")
			.append("a.MODIFYINFO_MODIFY_DATE as updatedDate,")
			.append("'MAINTENANCE_VENDOR' as companyType,")
			.append("'N' as deleted")
			.append(" from").append(schema).append(".VEM_FACTORY a ")
			.append(" left join ").append(schema).append(".ORG_PARTY b on a.identifier=b.identifier")
			.append(" left join ").append(schema).append(".ORG_PARTY c on a.LIAISON_ID=c.identifier")
			.append(" left join ").append(schema).append(".ORG_PARTY d on a.MODIFYINFO_ADD_USER_ID = cast(d.[identifier] as nvarchar)")
			.append(" left join ").append(schema).append(".ORG_PARTY e on a.MODIFYINFO_MODIFY_USER_ID = cast(e.[identifier] as nvarchar)")
			.append(" where a.FACTORY_ID='12786785' and b.deleted=0 ")
			
			//Task #2751 公司基本資料檔 新增 unoin 2017/11/03
			.append(" union ")
			.append(" select a.compShortCode as companyCode, ")
			.append(" '華經資訊' as shortName, ")
			.append(" a.FACTORY_ID as unityNumber, ")
			.append(" a.INVOICE_TITLE as shortName, ")
			.append(" a.BOSS as leader, ")
			.append(" a.TELEPHONE as tel, ")
			.append(" a.FAX as fax, ")
			.append(" null as applyDate, ")
			.append(" null as payDate, ")
			.append(" c.name as contact, ")
			.append(" null as contactTel, ")
			.append(" null as contactEmail, ")
			.append(" null as customerCode, ")
			.append(" a.EMAIL as companyEmail, ")
			.append(" null as dtidType, ")
			.append(" 'IATOMS_AUTHEN' as authenticationType, ")
			.append(" null as addressLocation, ")
			.append(" null as address, ")
			.append(" null as invoiceAddressLocation, ")
			.append(" null as invoiceAddress, ")
			.append(" a.remark as remark, ")
			.append(" a.MODIFYINFO_ADD_USER_ID as createdById, ")
			.append(" d.name as createdByName, ")
			.append(" a.MODIFYINFO_ADD_DATE as createdDate, ")
			.append(" a.MODIFYINFO_MODIFY_USER_ID as updatedById, ")
			.append(" e.name  as updatedByName, ")
			.append(" a.MODIFYINFO_MODIFY_DATE as updatedDate, ")
			.append(" 'MAINTENANCE_VENDOR' as companyType, ")
			.append(" 'N' as deleted ")
			.append(" from ").append(schema).append(".VEM_FACTORY a  ")
			.append(" left join ").append(schema).append(".ORG_PARTY b on a.identifier=b.identifier ")
			.append(" left join ").append(schema).append(".ORG_PARTY c on a.LIAISON_ID=c.identifier ")
			.append(" left join ").append(schema).append(".ORG_PARTY d on a.MODIFYINFO_ADD_USER_ID = cast(d.[identifier] as nvarchar) ")
			.append(" left join ").append(schema).append(".ORG_PARTY e on a.MODIFYINFO_MODIFY_USER_ID = cast(e.[identifier] as nvarchar) ")
			.append(" where a.FACTORY_ID='12115702' and b.deleted=0 ")

			.append(" union ")
			
			.append("select 'PAX' as companyCode,")
			.append("'百富科技' as shortName,")
			.append("null as unityNumber,")
			.append("null as invoiceHeader,")
			.append("null as leader,")
			.append("null as tel,")
			.append("null as fax,")
			.append("null as applyDate,")
			.append("null as payDate,")
			.append("null as contact,")
			.append("null as contactTel,")
			.append("null as contactEmail,")
			.append("null as customerCode,")
			.append("null as companyEmail,")
			.append("null as dtidType,")
			.append("'IATOMS_AUTHEN' as authenticationType,")
			.append("null as addressLocation,")
			.append("null as address,")
			.append("null as invoiceAddressLocation,")
			.append("null as invoiceAddress,")
			.append("null as remark,")
			.append("null as createdById,")
			.append("null as createdByName,")
			.append("null as createdDate,")
			.append("null as updatedById,")
			.append("null as updatedByName,")
			.append("null as updatedDate,")
			.append("'HARDWARE_VENDOR' as companyType,")
			.append("'N' as deleted");
			
			AliasBean aliasBean = new AliasBean(CompanyDTO.class);
			aliasBean.addScalar("companyCode", StringType.INSTANCE);
			aliasBean.addScalar("shortName", StringType.INSTANCE);
			aliasBean.addScalar("unityNumber", StringType.INSTANCE);
			aliasBean.addScalar("invoiceHeader", StringType.INSTANCE);
			aliasBean.addScalar("leader", StringType.INSTANCE);
			aliasBean.addScalar("tel", StringType.INSTANCE);
			aliasBean.addScalar("fax", StringType.INSTANCE);
			aliasBean.addScalar("applyDate", StringType.INSTANCE);
			aliasBean.addScalar("payDate", StringType.INSTANCE);
			aliasBean.addScalar("contact", StringType.INSTANCE);
			aliasBean.addScalar("contactTel", StringType.INSTANCE);
			aliasBean.addScalar("contactEmail", StringType.INSTANCE);
			aliasBean.addScalar("customerCode", StringType.INSTANCE);
			aliasBean.addScalar("companyEmail", StringType.INSTANCE);
			aliasBean.addScalar("dtidType", StringType.INSTANCE);
			aliasBean.addScalar("authenticationType", StringType.INSTANCE);
			aliasBean.addScalar("addressLocation", StringType.INSTANCE);
			aliasBean.addScalar("address", StringType.INSTANCE);
			aliasBean.addScalar("invoiceAddressLocation", StringType.INSTANCE);
			aliasBean.addScalar("invoiceAddress", StringType.INSTANCE);
			aliasBean.addScalar("remark", StringType.INSTANCE);
			aliasBean.addScalar("createdById", StringType.INSTANCE);
			aliasBean.addScalar("createdByName", StringType.INSTANCE);
			aliasBean.addScalar("createdDate", TimestampType.INSTANCE);
			aliasBean.addScalar("updatedById", StringType.INSTANCE);
			aliasBean.addScalar("updatedByName", StringType.INSTANCE);
			aliasBean.addScalar("updatedDate", TimestampType.INSTANCE);
			aliasBean.addScalar("companyType", StringType.INSTANCE);
			aliasBean.addScalar("deleted", StringType.INSTANCE);
			LOGGER.debug("listCompanyData()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			
			long endTime = System.currentTimeMillis();
			LOGGER.debug("listCompanyData()", "testSpeedTime DAO totalTime:" + (endTime - startTime));
			
			return result;
		} catch (Exception e) {
			LOGGER.error("listCompanyData()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#listWarehouseData()
	 */
	@Override
	public List<WarehouseDTO> listWarehouseData() throws DataAccessException {
		long startTime = System.currentTimeMillis();
		
		List<WarehouseDTO> result = null;
		//得到schema
		String schema = this.getSchema("iatoms-aims");
		SqlStatement sqlStatement = new SqlStatement();
		StringBuilder builder = new StringBuilder();
		try {
			//Task #2753 2017/11/03
			builder.append("case when a.name in ('系統轉換','經貿主倉','經貿維修倉','北二倉','經貿備品倉','原廠備品倉') ");
			builder.append(" then 'CYB'  when a.name in ('A201倉') then 'FIS' else 'PRINDA' end");
			sqlStatement.addSelectClause(builder.toString(), WarehouseDTO.ATTRIBUTE.COMPANY_ID.getValue());
			sqlStatement.addSelectClause("a.name", WarehouseDTO.ATTRIBUTE.NAME.getValue());
			sqlStatement.addSelectClause("b.cname", WarehouseDTO.ATTRIBUTE.CONTACT.getValue());
			sqlStatement.addSelectClause("null", WarehouseDTO.ATTRIBUTE.TEL.getValue());
			sqlStatement.addSelectClause("null", WarehouseDTO.ATTRIBUTE.FAX.getValue());
		//	sqlStatement.addSelectClause("capacity", WarehouseDTO.ATTRIBUTE.CAPACITY.getValue());
			
			builder.delete(0, builder.length());
			//Task #2753 2017/11/03
			builder.append("case when len(a.address)>1 then (case when a.name='A201倉' then substring(replace(a.address,' ',''),6,3) ");
			builder.append(" when a.name='南二倉' then substring(replace(a.address,' ',''),1,3) else substring(replace(a.address,' ',''),4,3) end ) else a.address end");
			sqlStatement.addSelectClause(builder.toString(), WarehouseDTO.ATTRIBUTE.LOCATION.getValue());
			
			builder.delete(0, builder.length());
			//Task #2753 2017/11/03
			builder.append("case when len(a.address)>1 then (case when len(a.address) > 0 and a.name in ('A201倉') then substring(replace(a.address,' ',''),9,len(a.address)-8) ");
			builder.append(" when len(a.address) > 0 and a.name in ('南二倉') then substring(replace(a.address,' ',''),4,len(a.address)-3) ");
			builder.append(" else substring(replace(a.address,' ',''),7,len(a.address)-6) end) else a.address end");
			sqlStatement.addSelectClause(builder.toString(), WarehouseDTO.ATTRIBUTE.ADDRESS.getValue());
			
			sqlStatement.addSelectClause("null", WarehouseDTO.ATTRIBUTE.COMMENT.getValue());
			sqlStatement.addSelectClause("a.create_account_id", WarehouseDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			sqlStatement.addSelectClause("c.cname", WarehouseDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("a.create_date", WarehouseDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sqlStatement.addSelectClause("a.modify_account_id", WarehouseDTO.ATTRIBUTE.UPDATED_BY_ID.getValue());
			sqlStatement.addSelectClause("d.cname", WarehouseDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("a.modify_date", WarehouseDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			sqlStatement.addSelectClause("case when (a.valid=1 and a.name<>'系統轉換') then 'N' else 'Y' end", WarehouseDTO.ATTRIBUTE.DELETED.getValue());
			
			builder.delete(0, builder.length());
			builder.append("AIMS.").append(schema).append(".storehous a");
			builder.append(" left join ").append("AIMS.").append(schema).append(".account b on a.owner_account_id=b.id");
			builder.append(" left join ").append("AIMS.").append(schema).append(".account c on a.create_account_id=c.id");
			builder.append(" left join ").append("AIMS.").append(schema).append(".account d on a.modify_account_id=d.id");
			sqlStatement.addFromExpression(builder.toString());
			
	//		sqlStatement.addWhereClause("a.name <> '系統轉換'");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(WarehouseDTO.class);
			LOGGER.debug("listWarehouseData()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			
			long endTime = System.currentTimeMillis();
			LOGGER.debug("listWarehouseData()", "testSpeedTime DAO totalTime:" + (endTime - startTime));
			
			return result;
		} catch (Exception e) {
			LOGGER.error("listWarehouseData()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#listMerchantData()
	 */
	@Override
	public List<MerchantDTO> listMerchantData() throws DataAccessException {
		List<MerchantDTO> result = null;
		//得到schema
		String schema = this.getSchema("iatoms-foms");
		SqlStatement sqlStatement = new SqlStatement();
		StringBuilder builder = new StringBuilder();
		try {
			sqlStatement.addSelectClause("b.compShortCode", MerchantDTO.ATTRIBUTE.COMPANY_ID.getValue());
			sqlStatement.addSelectClause("a.MARCHANT_ID", MerchantDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("a.MARCHANT_NAME", MerchantDTO.ATTRIBUTE.NAME.getValue());
			sqlStatement.addSelectClause("a.REMARK", MerchantDTO.ATTRIBUTE.REMARK.getValue());
			
			// 新增人員編號
			sqlStatement.addSelectClause("a.MODIFYINFO_ADD_USER_ID", MerchantDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			// 新增人員姓名
			sqlStatement.addSelectClause("c.name", MerchantDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			// 新增日期
			sqlStatement.addSelectClause("a.MODIFYINFO_ADD_DATE", MerchantDTO.ATTRIBUTE.CREATED_DATE.getValue());
			// 異動人員編號
			sqlStatement.addSelectClause("a.MODIFYINFO_MODIFY_USER_ID", MerchantDTO.ATTRIBUTE.UPDATED_BY_ID.getValue());
			// 異動人員姓名
			sqlStatement.addSelectClause("d.name", MerchantDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			// 異動日期
			sqlStatement.addSelectClause("a.MODIFYINFO_MODIFY_DATE", MerchantDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			
			sqlStatement.addSelectClause("case when a.DELETED=0 then 'N' else 'Y' end", MerchantDTO.ATTRIBUTE.DELETED.getValue());
			// 主鍵
			sqlStatement.addSelectClause("a.identifier", MerchantDTO.ATTRIBUTE.MERCHANT_ID.getValue());
		
			builder.append(schema).append(".CUM_MERCHANT a join ");
			builder.append(schema).append(".CUM_CORPORATION b on a.CORPORATION_ID=b.identifier left join ");
			builder.append(schema).append(".ORG_PARTY c on a.MODIFYINFO_ADD_USER_ID=cast(c.[identifier] as nvarchar) left join ");
			builder.append(schema).append(".ORG_PARTY d on a.MODIFYINFO_MODIFY_USER_ID=cast(d.[identifier] as nvarchar)");
			sqlStatement.addFromExpression(builder.toString());
			
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(MerchantDTO.class);
			LOGGER.debug("listMerchantData()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return result;
		} catch (Exception e) {
			LOGGER.error("listMerchantData()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#listMerchantHanderData(java.lang.String)
	 */
	@Override
	public List<BimMerchantHeaderDTO> listMerchantHanderData() throws DataAccessException {
		List<BimMerchantHeaderDTO> result = null;
		//得到schema
		String schema = this.getSchema("iatoms-foms");
		SqlStatement sqlStatement = new SqlStatement();
		StringBuilder builder = new StringBuilder();
		try {
			builder.append("case when a.RED_MAN_ID=9031 then 'Y' ").append(" when a.RED_MAN_ID=9032 then 'N' ");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), BimMerchantHeaderDTO.ATTRIBUTE.IS_VIP.getValue());
			sqlStatement.addSelectClause("a.LOCATION_NAME", BimMerchantHeaderDTO.ATTRIBUTE.HEADER_NAME.getValue());
			
			// 特店主鍵
			sqlStatement.addSelectClause("MARCHANT_ID", BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_ID.getValue());
			// 表頭主鍵
			sqlStatement.addSelectClause("a.identifier", BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_HEADER_ID.getValue());
			
			builder.delete(0, builder.length());
			builder.append("case when a.LOCATION_TYPE_ID=301 then 'DOWNTOWN' ");
			builder.append(" when a.LOCATION_TYPE_ID=302 then 'NON_DOWNTOWN' ");
			builder.append(" when a.LOCATION_TYPE_ID=303 then 'CHOKKASHI' ");
			builder.append(" when a.LOCATION_TYPE_ID=304 then 'COUNTY_MUNICIPALITY' ");
			builder.append(" when a.LOCATION_TYPE_ID=305 then 'OTHER_TOWNS' ");
			builder.append(" when a.LOCATION_TYPE_ID=306 then 'ISLAND_AREA' ");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), BimMerchantHeaderDTO.ATTRIBUTE.AREA.getValue());

			sqlStatement.addSelectClause("a.LIAISON_NAME", BimMerchantHeaderDTO.ATTRIBUTE.CONTACT.getValue());
			sqlStatement.addSelectClause("a.CELLPHONE", BimMerchantHeaderDTO.ATTRIBUTE.PHONE.getValue());

			builder.delete(0, builder.length());
			builder.append("case when replace(a.LOCATION_CITY,' ','')='世貿一' then '台北市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='北市' then '台北市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='103' then '台北市' ");
			builder.append(" when replace(a.LOCATION_CITY,' ','')='花博會' then '台北市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='台北' then '台北市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='臺北市' then '台北市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='台北巿' then '台北市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='北市濱' then '台北市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='台北世' then '台北市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='台北世貿' then '台北市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='台北中' then '台北市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='台北縣' then '新北市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='臺北縣' then '新北市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='中和市' then '新北市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='新莊市' then '新北市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='板橋市' then '新北市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='台中縣' then '台中市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='臺中縣' then '台中市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='臺中市' then '台中市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='440' then '台中市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='台中' then '台中市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='豐原市' then '台中市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='臺南市' then '台南市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='臺南' then '台南市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='台南縣' then '台南市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='臺南縣' then '台南市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='高雄縣' then '高雄市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='高雄' then '高雄市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='桃園縣' then '桃園市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='中壢市' then '桃園市' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='屏東市' then '屏東縣' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='花蓮市' then '花蓮縣' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='竹北市' then '新竹縣' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='南投市' then '南投縣' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='台東市' then '台東縣' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='臺東縣' then '台東縣' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='宜蘭市' then '宜蘭縣' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='雲林鎮' then '雲林縣' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='斗六市' then '雲林縣' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='彰化縣彰化市' then '彰化縣' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='彰化市' then '彰化縣' ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='苗栗市' then '苗栗縣' ");
			builder.append("else replace(a.LOCATION_CITY,' ','') end");
			sqlStatement.addSelectClause(builder.toString(), BimMerchantHeaderDTO.ATTRIBUTE.LOCATION.getValue());
			sqlStatement.addSelectClause("LIAISON_TEL1", BimMerchantHeaderDTO.ATTRIBUTE.CONTACT_TEL.getValue());

			builder.delete(0, builder.length());
			builder.append("case when replace(a.LOCATION_CITY,' ','')='440' then a.LOCATION_DETAIL_ADDRESS ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='南投市' then a.LOCATION_ADDRESS ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='世貿一' then a.LOCATION_ADDRESS ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='宜蘭市' then replace(a.LOCATION_ADDRESS,'宜蘭縣','') ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='台東市' then a.LOCATION_ADDRESS ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='高雄' then replace(a.LOCATION_ADDRESS,'高雄市','') ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='中和市' then a.LOCATION_ADDRESS ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='花蓮市' then a.LOCATION_ADDRESS ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='彰化縣彰化市' then replace(a.LOCATION_ADDRESS,'彰化縣','') ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='新莊市' then a.LOCATION_ADDRESS ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='中壢市' then a.LOCATION_ADDRESS ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='板橋市' then a.LOCATION_ADDRESS ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='彰化市' then a.LOCATION_ADDRESS ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='花博會' then a.LOCATION_ADDRESS ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='北市濱' then replace(a.LOCATION_ADDRESS,'北市','') ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='斗六市' then a.LOCATION_ADDRESS ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='台北世' then replace(a.LOCATION_ADDRESS,'台北','') ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='台北世貿' then replace(a.LOCATION_ADDRESS,'台北','') ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='豐原市' then a.LOCATION_ADDRESS ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='苗栗市' then a.LOCATION_ADDRESS ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='竹北市' then a.LOCATION_ADDRESS ");
			builder.append("when replace(a.LOCATION_CITY,' ','')='台北中' then replace(a.LOCATION_ADDRESS,'台北','') ");
			builder.append("else (replace(ISNULL(a.LOCATION_REGION,''),' ','') + replace(ISNULL(a.LOCATION_DETAIL_ADDRESS,''),' ','')) end");
			sqlStatement.addSelectClause(builder.toString(), BimMerchantHeaderDTO.ATTRIBUTE.BUSINESS_ADDRESS.getValue());

			sqlStatement.addSelectClause("a.SHOP_HOURS_START", BimMerchantHeaderDTO.ATTRIBUTE.OPEN_HOUR.getValue());
			sqlStatement.addSelectClause("a.SHOP_HOURS_END", BimMerchantHeaderDTO.ATTRIBUTE.CLOSE_HOUR.getValue());
			sqlStatement.addSelectClause("a.AO_NAME", BimMerchantHeaderDTO.ATTRIBUTE.AO_NAME.getValue());
			sqlStatement.addSelectClause("a.AO_EMAIL", BimMerchantHeaderDTO.ATTRIBUTE.AO_EMAIL.getValue());
			sqlStatement.addSelectClause("a.MODIFYINFO_ADD_USER_ID", BimMerchantHeaderDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			sqlStatement.addSelectClause("b.name", BimMerchantHeaderDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("a.MODIFYINFO_ADD_DATE", BimMerchantHeaderDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sqlStatement.addSelectClause("a.MODIFYINFO_MODIFY_USER_ID", BimMerchantHeaderDTO.ATTRIBUTE.UPDATED_BY_ID.getValue());
			sqlStatement.addSelectClause("c.name", BimMerchantHeaderDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("a.MODIFYINFO_MODIFY_DATE", BimMerchantHeaderDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			sqlStatement.addSelectClause("a.LIAISON_TEL2", BimMerchantHeaderDTO.ATTRIBUTE.CONTACT_TEL2.getValue());
			sqlStatement.addSelectClause("case when a.DELETED=0 then 'N' else 'Y' end", BimMerchantHeaderDTO.ATTRIBUTE.DELETED.getValue());
			
			builder.delete(0, builder.length());
			builder.append(schema).append(".CUM_Title a ");
			builder.append(" left join ").append(schema).append(".ORG_PARTY b on a.MODIFYINFO_ADD_USER_ID=cast(b.[identifier] as nvarchar)");
			builder.append(" left join ").append(schema).append(".ORG_PARTY c on a.MODIFYINFO_MODIFY_USER_ID=cast(c.[identifier] as nvarchar)");
			sqlStatement.addFromExpression(builder.toString());

			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(BimMerchantHeaderDTO.class);
			LOGGER.debug("listMerchantHanderData()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return result;
		} catch (Exception e) {
			LOGGER.error("listMerchantHanderData()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#listApplicationData()
	 */
	@Override
	public List<ApplicationDTO> listApplicationData()throws DataAccessException {
		long startTime = System.currentTimeMillis();
		
		List<ApplicationDTO> result = null;
		//得到schema
		String schema = this.getSchema("iatoms-foms");
		SqlStatement sqlStatement = new SqlStatement();
		StringBuilder builder = new StringBuilder();
		try {
			sqlStatement.addSelectClause("b.compShortCode", ApplicationDTO.ATTRIBUTE.CUSTOMER_ID.getValue());
			sqlStatement.addSelectClause("a.posCode", ApplicationDTO.ATTRIBUTE.VERSION.getValue());
			sqlStatement.addSelectClause("c.value", ApplicationDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());

			builder.append(schema).append(".FOMS_SFM_Request a join ");
			builder.append(schema).append(".CUM_CORPORATION b on a.corporationId=b.identifier and ");
			builder.append(" b.serviceType=1 and b.compShortCode <> 'TSB-EDC'");
			builder.append(" left join ").append(schema).append(".CORE_OPTION c on a.deviceType_ID=c.ID");
			sqlStatement.addFromExpression(builder.toString());

			builder.delete(0, builder.length());
			builder.append("a.status in ('StatusCancel','StatusCancelByUser','StatusComplete','StatusEnd')");
			builder.append(" and a.posCode is not null and posCode <> '' ");
			sqlStatement.addWhereClause(builder.toString());
					
			sqlStatement.setGroupByExpression("b.compShortCode,a.posCode,c.value");
			
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(ApplicationDTO.class);
			LOGGER.debug("listApplicationData()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);

			long endTime = System.currentTimeMillis();
			LOGGER.debug("listApplicationData()", "testSpeedTime DAO totalTime:" + (endTime - startTime));
			
			return result;
		} catch (Exception e) {
			LOGGER.error("listApplicationData()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#listContractData()
	 */
	@Override
	public List<BimContractDTO> listContractData() throws DataAccessException {
		long startTime = System.currentTimeMillis();
		
		List<BimContractDTO> result = null;
		//得到schema
		String aimsSchema = this.getSchema("iatoms-aims");
		String fomsSchema = this.getSchema("iatoms-foms");
		SqlQueryBean sqlQueryBean = new SqlQueryBean();
		try {
			sqlQueryBean.append("select b.compShortCode as companyId,")
			.append("a.contractNo as contractCode,")
			.append("a.startDate as startDate,")
			.append("a.endDate as endDate,")
			.append("null as cancelDate,")
			.append("case when a.CONTRACT_STATUS_OPTION_ID=561 then 'NOT_EFFECT'")
			.append(" when a.CONTRACT_STATUS_OPTION_ID=562 then 'IN_EFFECT'")
			.append(" when a.CONTRACT_STATUS_OPTION_ID=563 then 'INVALID'")
			.append(" else null end as contractStatus,")
			.append("null as contractPrice,")
			.append("case when a.PAY_FREQUENCY_OPTION_ID=581 then 'MONTH'")
			.append(" when a.PAY_FREQUENCY_OPTION_ID=582 then 'QUARTER'")
			.append(" when a.PAY_FREQUENCY_OPTION_ID=583 then 'SIX_MONTH'")
			.append(" when a.PAY_FREQUENCY_OPTION_ID=584 then 'YEAR'")
			.append(" else null end as payMode,")
			.append("a.payCondition as payRequire,")
			.append("null as factoryWarranty,")
			.append("null as customerWarranty,")
			.append("null as workHourStart1,")
			.append("null as workHourEnd1,")
			.append("null as workHourStart2,")
			.append("null as workHourEnd2,")
			.append("d.name as window1,")
			.append("null as window1Connection,")
			.append("case when a.businessEmployeeId=a.maintainEmployeeId then null")
			.append(" else e.name end as window2,")
			.append("null as window2Connection,")
			.append("a.remark as comment,")
			.append("a.MODIFYINFO_ADD_USER_ID as createdById,")
			.append("f.name as createdByName,")
			.append("a.MODIFYINFO_ADD_DATE as createdDate,")
			.append("a.MODIFYINFO_MODIFY_USER_ID as updatedById,")
			.append("g.name as updatedByName,")
			.append("a.MODIFYINFO_MODIFY_DATE as updatedDate,")
			.append("'Y' as deleted")
		//	.append("'Y' as deleted,")
		//	.append("convert(varchar,a.identifier) as DataKey,")
		//	.append("'FOMS' as Source ")
			
			.append(" from ").append(fomsSchema).append(".CCM_CORPORATIONCONTRACT a")
			.append(" join ").append(fomsSchema).append(".CUM_CORPORATION b on a.corporationId=b.identifier and b.servicetype='1'")
			.append(" left join ").append(fomsSchema).append(".CCM_SLA c on a.SLA_ID=c.identifier")
			.append(" left join ").append(fomsSchema).append(".ORG_PARTY d on a.businessEmployeeId=d.identifier")
			.append(" left join ").append(fomsSchema).append(".ORG_PARTY e on a.maintainEmployeeId=e.identifier")
			.append(" left join ").append(fomsSchema).append(".ORG_PARTY f on a.MODIFYINFO_ADD_USER_ID=f.identifier")
			.append(" left join ").append(fomsSchema).append(".ORG_PARTY g on a.MODIFYINFO_MODIFY_USER_ID=g.identifier")
			
			.append(" union ")
			
			.append(" select case when a.customer_name='經貿聯網' then 'CYB'")
			.append(" when a.customer_name='歐付寶' then 'GreenWorld'")
			.append(" when a.customer_name='台新銀行' then 'TSB-EDC'")
			.append(" when a.customer_name='彰化銀行' then 'CHB_EDC'")
			.append(" when a.customer_name='大眾銀行' then 'TCB_EDC'")
			.append(" when a.customer_name='上海商業儲蓄銀行' then 'SCSB'")
			.append(" when a.customer_name='陽信銀行' then 'SYB-EDC'")
			.append(" when a.customer_name='環匯' then 'GP'")
			.append(" when a.customer_name='捷達威數位科技股份有限公司' then 'FIS'")
			.append(" else null end as companyId,")
			.append("a.id as contractCode,")
			.append("a.start_date as startDate,")
			.append("a.end_date as endDate,")
			.append("a.cancel_date as cancelDate,")
			.append("null as contractStatus,")
			.append("null as contractPrice,")
			.append("null as payMode,")
			.append("null as payRequire,")
			.append("null as factoryWarranty,")
			.append("null as customerWarranty,")
			.append("null as workHourStart1,")
			.append("null as workHourEnd1,")
			.append("null as workHourStart2,")
			.append("null as workHourEnd2,")
			.append("null as window1,")
			.append("null as window1Connection,")
			.append("null as window2,")
			.append("null as window2Connection,")
			.append("null as comment,")
			.append("a.create_account_id as createdById,")
			.append("b.cname as createdByName,")
			.append("a.create_date as createdDate,")
			.append("a.modify_account_id as updatedById,")
			.append("c.cname as updatedByName,")
			.append("a.modify_date as updatedDate,")
			.append("case when a.valid=1 then 'N'")
			.append(" else 'Y' end as deleted")
/*			.append(" else 'Y' end as deleted,")
			.append("a.id as DataKey,")
			.append("'AIMS' as Source")*/
			
			.append(" from AIMS.").append(aimsSchema).append(".contract a ")
			.append(" left join AIMS.").append(aimsSchema).append(".account b on a.create_account_id=b.id")
			.append(" left join AIMS.").append(aimsSchema).append(".account c on a.modify_account_id=c.id")
			
			// 按排除
			.append(" order by companyId,contractCode ");
			
			AliasBean aliasBean = new AliasBean(BimContractDTO.class);
			aliasBean.addScalar("companyId", StringType.INSTANCE);
			aliasBean.addScalar("contractCode", StringType.INSTANCE);
			aliasBean.addScalar("startDate", DateType.INSTANCE);
			aliasBean.addScalar("endDate", DateType.INSTANCE);
			aliasBean.addScalar("cancelDate", DateType.INSTANCE);
			aliasBean.addScalar("contractStatus", StringType.INSTANCE);
			aliasBean.addScalar("contractPrice", LongType.INSTANCE);
			aliasBean.addScalar("payMode", StringType.INSTANCE);
			aliasBean.addScalar("payRequire", StringType.INSTANCE);
			aliasBean.addScalar("factoryWarranty", IntegerType.INSTANCE);
			aliasBean.addScalar("customerWarranty", IntegerType.INSTANCE);
			aliasBean.addScalar("workHourStart1", StringType.INSTANCE);
			aliasBean.addScalar("workHourEnd1", StringType.INSTANCE);
			aliasBean.addScalar("workHourStart2", StringType.INSTANCE);
			aliasBean.addScalar("workHourEnd2", StringType.INSTANCE);
			aliasBean.addScalar("window1", StringType.INSTANCE);
			aliasBean.addScalar("window1Connection", StringType.INSTANCE);
			aliasBean.addScalar("window2", StringType.INSTANCE);
			aliasBean.addScalar("window2Connection", StringType.INSTANCE);
			aliasBean.addScalar("comment", StringType.INSTANCE);
			aliasBean.addScalar("createdById", StringType.INSTANCE);
			aliasBean.addScalar("createdByName", StringType.INSTANCE);
			aliasBean.addScalar("createdDate", TimestampType.INSTANCE);
			aliasBean.addScalar("updatedById", StringType.INSTANCE);
			aliasBean.addScalar("updatedByName", StringType.INSTANCE);
			aliasBean.addScalar("updatedDate", TimestampType.INSTANCE);
			aliasBean.addScalar("deleted", StringType.INSTANCE);
			
			LOGGER.debug("listContractData()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);

			long endTime = System.currentTimeMillis();
			LOGGER.debug("listContractData()", "testSpeedTime DAO totalTime:" + (endTime - startTime));
			
			return result;
		} catch (Exception e) {
			LOGGER.error("listContractData()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#listAssetTypeData()
	 */
	@Override
	public List<AssetTypeDTO> listAssetTypeData() throws DataAccessException {
		long startTime = System.currentTimeMillis();
		
		List<AssetTypeDTO> result = null;
		//得到schema
		String schema = this.getSchema("iatoms-aims");
		SqlStatement sqlStatement = new SqlStatement();
		StringBuilder builder = new StringBuilder();
		try {
			sqlStatement.addSelectClause("a.name", AssetTypeDTO.ATTRIBUTE.NAME.getValue());
			sqlStatement.addSelectClause("a.BRAND", AssetTypeDTO.ATTRIBUTE.BRAND.getValue());
			sqlStatement.addSelectClause("a.MODEL", AssetTypeDTO.ATTRIBUTE.MODEL.getValue());
			
			builder.append("case when a.asset_type_id=7 then 'EDC' ");
			builder.append(" when a.asset_type_id=8 then 'Related_Products'");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), AssetTypeDTO.ATTRIBUTE.ASSET_CATEGORY.getValue());
			
			sqlStatement.addSelectClause("null", AssetTypeDTO.ATTRIBUTE.UNIT.getValue());
			sqlStatement.addSelectClause("null", AssetTypeDTO.ATTRIBUTE.SAFETY_STOCK.getValue());
			sqlStatement.addSelectClause("a.comment", AssetTypeDTO.ATTRIBUTE.REMARK.getValue());
			sqlStatement.addSelectClause("a.create_account_id", AssetTypeDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			sqlStatement.addSelectClause("b.cname", AssetTypeDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("a.create_date", AssetTypeDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sqlStatement.addSelectClause("a.modify_account_id", AssetTypeDTO.ATTRIBUTE.UPDATED_BY_ID.getValue());
			sqlStatement.addSelectClause("c.cname", AssetTypeDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("a.modify_date", AssetTypeDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			sqlStatement.addSelectClause("case when a.valid=1 then 'N' else 'Y' end", AssetTypeDTO.ATTRIBUTE.DELETED.getValue());
			sqlStatement.addSelectClause("case when a.valid<>1 then a.modify_account_id else null end", AssetTypeDTO.ATTRIBUTE.DELETED_BY_ID.getValue());
			sqlStatement.addSelectClause("case when a.valid<>1 then c.cname else null end", AssetTypeDTO.ATTRIBUTE.DELETED_BY_NAME.getValue());
			sqlStatement.addSelectClause("case when a.valid<>1 then a.modify_date else null end", AssetTypeDTO.ATTRIBUTE.DELETED_DATE.getValue());
//			sqlStatement.addSelectClause("'AIMS' as Source", AssetTypeDTO.ATTRIBUTE.CUSTOMER_ID.getValue());
			sqlStatement.addSelectClause("a.id", AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			
			builder.delete(0, builder.length());
			builder.append("AIMS.").append(schema).append(".asset_category a ");
			builder.append(" left join AIMS.").append(schema).append(".account b on a.create_account_id=b.id ");
			builder.append(" left join AIMS.").append(schema).append(".account c on a.modify_account_id=c.id ");
			sqlStatement.addFromExpression(builder.toString());

			builder.delete(0, builder.length());
			sqlStatement.addWhereClause("a.asset_type_id in (7,8)");
			
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(AssetTypeDTO.class);
			LOGGER.debug("listAssetTypeData()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);

			long endTime = System.currentTimeMillis();
			LOGGER.debug("listAssetTypeData()", "testSpeedTime DAO totalTime:" + (endTime - startTime));
			
			return result;
		} catch (Exception e) {
			LOGGER.error("listAssetTypeData()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#listBrandById(java.lang.String)
	 */
	@Override
	public List<String> listBrandById(String assetId) throws DataAccessException {
		LOGGER.debug(".listBrandById()", "parameters:assetId=" + assetId);
		List<String> result = null;
		//得到schema
		String schema = this.getSchema("iatoms-aims");
		SqlQueryBean sqlQueryBean = new SqlQueryBean();
		try {
			if(StringUtils.hasText(assetId)){
				List<AssetTypeDTO> assetTypeDTOs = null;
				result = new ArrayList<String>();
				sqlQueryBean.append("select brand")
				.append(" from AIMS.").append(schema).append(".asset_category ")
				.append(" where brand is not null and brand<>'' and id=:assetId")
				.append(" union ")
				.append(" select brand ")
				.append(" from AIMS.").append(schema).append(".repository ")
				.append(" where brand is not null and brand<>'' and asset_category_id=:assetId")
				.append(" group by brand ")
				.append(" union ")
				.append(" select brand ")
				.append(" from AIMS.").append(schema).append(".repository_History ")
				.append(" where brand is not null and brand<>'' and asset_category_id=:assetId")
				.append(" group by brand ");
				AliasBean aliasBean = new AliasBean(AssetTypeDTO.class);
				aliasBean.addScalar("brand", StringType.INSTANCE);
				sqlQueryBean.setParameter("assetId", assetId);
				LOGGER.debug("listBrandById()", "sql:" + sqlQueryBean.toString());
				assetTypeDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
				if(!CollectionUtils.isEmpty(assetTypeDTOs)){
					for(AssetTypeDTO assetTypeDTO : assetTypeDTOs){
						result.add(assetTypeDTO.getBrand());
					}
				}
			}
			return result;
		} catch (Exception e) {
			LOGGER.error("listBrandById()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#listModelById(java.lang.String)
	 */
	@Override
	public List<String> listModelById(String assetId) throws DataAccessException {
		LOGGER.debug(".listModelById()", "parameters:assetId=" + assetId);
		List<String> result = null;
		//得到schema
		String schema = this.getSchema("iatoms-aims");
		SqlQueryBean sqlQueryBean = new SqlQueryBean();
		try {
			if(StringUtils.hasText(assetId)){
				List<AssetTypeDTO> assetTypeDTOs = null;
				result = new ArrayList<String>();
				sqlQueryBean.append("select model")
				.append(" from AIMS.").append(schema).append(".asset_category ")
				.append(" where model is not null and model<>'' and id=:assetId")
				.append(" union ")
				.append(" select model ")
				.append(" from AIMS.").append(schema).append(".repository ")
				.append(" where model is not null and model<>'' and asset_category_id=:assetId")
				.append(" group by model ")
				.append(" union ")
				.append(" select model ")
				.append(" from AIMS.").append(schema).append(".repository_History ")
				.append(" where model is not null and model<>'' and asset_category_id=:assetId")
				.append(" group by model ");
				AliasBean aliasBean = new AliasBean(AssetTypeDTO.class);
				aliasBean.addScalar("model", StringType.INSTANCE);
				sqlQueryBean.setParameter("assetId", assetId);
				LOGGER.debug("listBrandById()", "sql:" + sqlQueryBean.toString());
				assetTypeDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
				if(!CollectionUtils.isEmpty(assetTypeDTOs)){
					for(AssetTypeDTO assetTypeDTO : assetTypeDTOs){
						result.add(assetTypeDTO.getModel());
					}
				}
			}
			return result;
		} catch (Exception e) {
			LOGGER.error("listModelById()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#listRepository()
	 */
	@Override
	public List<DmmRepositoryDTO> listRepository() throws DataAccessException {

		List<DmmRepositoryDTO> result = null;
		//得到schema
		String aimsSchema = this.getSchema("iatoms-aims");
		String fomsSchema = this.getSchema("iatoms-foms");
		SqlQueryBean sqlQueryBean = new SqlQueryBean();
		try {
			sqlQueryBean.append("select a.sn as assetId, a.sn as serialNumber,")
			.append(" b.name as assetTypeId,")
			//Bug #2941
			.append(" case when b.asset_type_id=7 then 'EDC' ")
			.append(" when b.asset_type_id=8 then 'Related_Products' end as assetCateGory,")
			.append(" a.brand as brand,")
			.append(" a.sn2 as propertyId,")
			//--將storehous換為warehouseId
			.append(" c.name as warehouseId,")
			.append(" case when a.contract_id='CS-1702-10' then 'CS1702-10' else a.contract_id end as contractId,")
			//--將MA_TYPE換為maType
			.append(" case when a.owner=a.customer then 'BUYOUT' else 'LEASE' end as maType,")
			//--將status_id換為status
//			.append(" case when a.status_id=2 then 'REPERTORY'  when a.status_id=4 then 'ON_WAY' when a.status_id=5 then 'IN_APPLY' when a.status_id=6 then 'IN_USE' when a.status_id=7 and a.repair_vendor in ('經貿','經貿維修倉','維修倉') then 'REPAIR'  when a.status_id=7 and a.repair_vendor in ('百富') then 'MAINTENANCE' when a.status_id=7 and a.repair_vendor is null then 'REPAIR'  when a.status_id=8 then 'BORROWING'  when a.status_id=12 then 'DISABLED' when a.status_id=17 then 'PENDING_DISABLED' else null end as status,")
			// Task #2572
			.append(" case when a.status_id=2 then 'REPERTORY'  when a.status_id=4 then 'ON_WAY' when a.status_id=5 then 'IN_APPLY' when a.status_id=6 then 'IN_USE' when a.status_id=7 then 'MAINTENANCE' when a.status_id=8 then 'BORROWING'  when a.status_id=12 then 'DISABLED' when a.status_id=17 then 'PENDING_DISABLED' else null end as status,")
			
			//--將IS_ENABLED換為isEnabled
			.append(" case when a.enable_date is not null then 'Y' else 'N' end as isEnabled,")
			.append("a.enable_date as enableDate,")
			//--將SIM_ENABLE_DATE換為simEnableDate
			.append(" null as simEnableDate,")
			//--這玩意居然是租賃編號。。。
			.append(" a.serial_number as simEnableNo,")
			//5=領用
			.append(" case when a.status_id=5 then a.CARRIER else null end as carrier,")
//			.append(" a.fn_select_date as carryDate,")
			// Task #2573
			.append(" case when a.status_id=5 then a.fn_select_date else null end as carryDate,")
			
			//8=借用
			.append(" case when a.status_id=8 then a.CARRIER else null end as borrower,")
//			.append(" a.lease_date_start as borrowerStart,")
//			.append(" a.lease_date_end as borrowerEnd,")
			// Task #2573
			.append(" case when a.status_id=8 then a.lease_date_start else null end as borrowerStart,")
			.append(" case when a.status_id=8 then a.lease_date_end else null end as borrowerEnd,")
			
			.append(" null as borrowerEmail,")
			.append(" null as borrowerMgrEmail,")
			.append(" a.lease_date_back as backDate,")
			//-- 將owner 換為 assetOwner
			.append(" case when a.owner='台新銀行' then 'TSB-EDC' when a.owner='經貿聯網' then 'CYB' when a.owner='百富科技' then 'PAX' when a.owner='環匯' then 'GP' when a.owner='捷達威' then 'JDW-EDC' when a.owner='彰銀' then 'CHB_EDC' when a.owner='大眾銀行' then 'TCB_EDC' when a.owner='上海商銀' then 'SCSB' when a.owner='歐付寶' then 'GreenWorld' when a.owner='陽信銀行' then 'SYB-EDC' else null end as assetOwner,")
			//--  將customer 換為 assetUser
			.append(" case when a.customer='台新銀行' then 'TSB-EDC' when a.customer='經貿聯網' then 'CYB' when a.customer='百富科技' then 'PAX' when a.customer='環匯' then 'GP' when a.customer='捷達威' then 'JDW-EDC' when a.customer='彰銀' then 'CHB_EDC' when a.customer='大眾銀行' then 'TCB_EDC' when a.customer='上海商銀' then 'SCSB' when a.customer='歐付寶' then 'GreenWorld' when a.customer='陽信銀行' then 'SYB-EDC' else null end as assetUser,")
			//-- 將flag_cup 換為 isCup
			.append(" case when a.flag_cup=1 then 'Y' else 'N' end as isCup, ")
			//--  將RETIRE_REASON_CODE 換為 retireReasonCode
			.append("null as retireReasonCode,")
			//Task #2584 
			.append(" (case when e.requestNo is not null and e.requestNo <>'' then e.requestNo else a.working_no end) as caseId,")
			//Task #2584 
			.append(" (case when e.realDoneDay is not null and e.realDoneDay <>'' then e.realDoneDay else a.repaired_date end) as caseCompletionDate,")
			//.append(" e.closeTime as caseCloseDate,")
			.append(" f.tid1 as tid,")
			//Task #2584 
			.append(" (case when f.dtid is not null and f.dtid <>'' then f.dtid else a.dtid end) as dtid,")
			//--暫時不加
			.append("null as applicationId,")
			//.append(" h.MARCHANT_ID as merchantId,")
			//Task #2584 
			.append(" (case when h.identifier is not null then  CAST(h.identifier AS varchar) else a.mer_mid end) as merchantId,")
			//舊資料轉檔foms特點資料無值時，存儲aims特點代號的標誌位 Task #2584 
			.append(" (case when h.identifier is not null then 'N' else 'Y' end) as isNoMerId,")
			.append(" (case when h.identifier is not null then null else a.mer_name end) as merName,")
			//.append(" g.LOCATION_NAME as merchantHeaderId,")
			//Task #2584 
			.append(" (case when g.identifier is not null then CAST( g.identifier AS varchar ) else a.mer_edctitle end) as merchantHeaderId,")
			//舊資料轉檔foms特點資料無值時，存儲aims特點代號的標誌位 Task #2584 
			.append(" (case when g.identifier is not null then 'N' else 'Y' end) as isNoMerHeaderId,")
			.append(" a.batch_inv_no as assetInId,")
			.append(" a.create_date as assetInTime,")
			.append(" null as assetTransId,")
			// -- 將 compShortCode 換為  maintainCompany
			.append(" case when i.name='普林達' then 'PRINDA' when i.name='經貿聯網' then 'CYB' when i.name='普鴻資訊' then 'PRINDA' else null end as maintainCompany,")
			//--  將 repair_vendor 換為  repairVendor
//			.append(" case when a.repair_vendor in ('經貿','經貿維修倉','維修倉') then 'CYB' else null end as repairVendor,")
			.append(" case when a.status_id=7 then 'PAX' else null end as repairVendor,")
			//  --將 lease_comment 換為 description
			.append(" case when a.status_id in (5,8) and len(a.carrier) > 0 and len(a.lease_comment) > 0 then a.carrier + ',' + a.lease_comment when a.status_id in (5,8) and len(a.carrier) > 0 and len(a.lease_comment) <= 0 then a.carrier else a.lease_comment end as description,")
			//  --  將 return_reason  換為 action
			.append(" CASE WHEN return_reason = '報廢' THEN 'DISABLED' WHEN return_reason = '借用' THEN 'BORROW' WHEN return_reason = '領用' THEN 'APPLY' WHEN return_reason = '歸還' THEN 'BACK' WHEN return_reason = '送修' THEN 'MAINTENANCE'	WHEN return_reason = '遺失' THEN 'LOSS' else 'OTHER_EDIT' end as action,")
			//--將 parts_name 換為  faultComponent(name)
			.append(" l.Option_Item as faultComponent,")
			// --將 faultDescription 換為  condition_name
			.append(" m.Option_Item as faultDescription,")
			.append(" a.guarantee_date_cust as customerWarrantyDate,")
			.append(" a.guarantee_date as factoryWarrantyDate,")
			.append(" a.create_account_id as createUser,")
			// -- 將 createname 換為 createUserName
			.append(" j.cname as createUserName,")
			.append(" a.create_date as createDate,")
			.append(" a.modify_account_id as updateUser,")
			//--將 updatename 換為 updateUserName
			.append(" k.cname as updateUserName,  ")
			.append(" a.modify_date as updateDate,")
			//-- 將  valid 換為 deleted
			//.append(" case when a.valid=1 then 'N' else 'Y' end as deleted,")
			// --驗收日期
			//Task #2768 客戶驗收時間=入庫驗收時間  驗收日期 改抓 app_date 2017/11/06
			.append(" a.app_date as checkedDate,")
			.append(" a.model as assetModel,")
			// --將 INSTALLED_ADRESS 換為 merInstallAddress  
			//Task #2584 如果foms里無裝機地址縣市，則在aims里查詢 //增加一個e.INSTALL_ADDRESS>2 的判斷 2017/12/12
			.append(" (case when e.INSTALL_ADDRESS is not null and e.INSTALL_ADDRESS <>'' then (case when len(replace(e.INSTALL_ADDRESS,' ','')) < = 0 then replace(e.INSTALL_ADDRESS,' ','')	when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3) in ('淡水區','南投市','世貿旅','(南崁','北市中','世貿一','臺北世','台北世','北市復','淡水區','台北華','晶華酒','世貿三','世貿3','台中世','北市大', '中市太','103','高雄展','世貿旅','北市民','台北松','台北大','新北新', '五股區','中和市','台中區','高市鼓','高市鳳','民族一','台東市','民族一', '桃園二','彰化市','花蓮市','德安資','宜蘭市','台北南','300','404','屏東市','羅斯福','高雄區','台中西','特賣裝','台南南','墨攻新','台北中','台北長','高雄苓','台中旅','圓山花','台北信','高市左','北市南','臺南永','台中烏', '草屯鎮','北市中','新店中','高雄鳳','411','南投市','新竹竹','220','403', '(頂溪','(南崁','(市府','(新埔','總公司','(總公','雲林鎮','新北投','401', '世貿1','改:台','高雄巨','324','資訊室','林啟宏','407','圓山爭','南港展', '813','806','105','台中朝','草屯分','大台中','北市信','竹北市') then replace(e.INSTALL_ADDRESS,' ','') else (case when len( replace( e.INSTALL_ADDRESS, ' ', '' ) )>2 THEN substring( replace( e.INSTALL_ADDRESS, ' ', '' ), 4, len( replace( e.INSTALL_ADDRESS, ' ', '' ))- 3 ) else e.INSTALL_ADDRESS END) end)")
			//Task #2584 2017/10/23 加新sql
			.append(" else ( case ")
			.append(" when substring(a.mer_address,1,3) in ('台中河','花蓮市','北市南','臺東市','南港展','松山區','薩摩亞','北市南','南投市','屏東市','萬華區','潮肉壽','中市北','八德路','屏東市','台北中','台北生','南投市','中山區','宜蘭市','北投區','高雄巨','嘉義鄉','台北世','(大直','彰化市','世貿一','北市中','台北世','北市內','永和區','台東市','新店區','松山區','高雄設','市淡水','中正區') then a.mer_address ")
			.append(" when substring(a.mer_address,1,4) in ('2臺北市','4臺北市') then substring(a.mer_address,5,len(a.mer_address)-4) ")
			.append(" when substring(a.mer_address,1,5) in ('保險套情人','103大同') then a.mer_address ")
			.append(" when substring(a.mer_address,1,5) in ('00臺北市','51南投縣','00新竹市') then substring(a.mer_address,6,len(a.mer_address)-5)")
			.append(" when substring(a.mer_address,1,6) in ('彰化市金馬路','竹北市新泰路','彰化市中興路','000812') then a.mer_address")
			.append(" when substring(a.mer_address,1,6) in ('300新竹市') then substring(a.mer_address,7,len(a.mer_address)-6)")
			.append(" when substring(a.mer_address,1,8) in ('24892新北市','11491台北市') then substring(a.mer_address,9,len(a.mer_address)-8)")
			.append(" when substring(a.mer_address,1,1) in ('0','1','2','3','4','5','6','7','8','9','０','１','２','３','４','５','６','７','８','９') then substring(a.mer_address,7,len(a.mer_address)-6)")
		    //增加一個a.mer_address>2 的判斷 2017/12/12
			.append(" else   (case when len( a.mer_address )>2 THEN substring( a.mer_address, 4, len( a.mer_address )- 3 ) else a.mer_address END) end ) end) as installedAdress,")
			// -- INSTALL_TYPE    installType
			.append(" null as installType,")
			.append(" a.app_date as cyberApprovedDate,")
			.append(" null as keeperName,")
			//Task #2584 
			//如果foms里無裝機地址，則在aims里查詢
			.append(" (case when e.INSTALL_ADDRESS is not null and e.INSTALL_ADDRESS <>'' then (case when len(replace(e.INSTALL_ADDRESS,' ','')) < 1 then replace(e.INSTALL_ADDRESS,' ','') when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='臺北市' then '台北市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,2)='世貿' then '台北市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,2)='北市' then '台北市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,2)='台北' then '台北市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,2)='臺北' then '台北市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,2)='圓山' then '台北市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='德安資' then '台北市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='(市府' then '台北市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='墨攻新' then '台北市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='新北投' then '台北市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='南港展' then '台北市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='105' then '台北市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='103' then '台北市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='羅斯福' then '台北市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='晶華酒' then '台北市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='改:台' then '台北市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='(總公' then '台北市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,2)='臺中' then '台中市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,2)='中市' then '台中市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='大台中' then '台中市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,2)='台中' then '台中市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='403' then '台中市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='407' then '台中市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='401' then '台中市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='411' then '台中市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,5)='404臺中' then '台中市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,2)='桃園' then '桃園市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='(南崁' then '桃園市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='324' then '桃園市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='淡水區' then '新北市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='台北縣' then '新北市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='臺北縣' then '新北市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='五股區' then '新北市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='中和市' then '新北市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='(新埔' then '新北市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='(頂溪' then '新北市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,2)='新北' then '新北市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,2)='新店' then '新北市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='220' then '新北市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='南投市' then '南投縣'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,2)='草屯' then '南投縣'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,2)='台南' then '台南市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,2)='臺南' then '台南市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='民族一' then '高雄市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,2)='高雄' then '高雄市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,2)='高市' then '高雄市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='813' then '高雄市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='806' then '高雄市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='特賣裝' then '高雄市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,5)='404高雄' then '高雄市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='新竹線' then '新竹縣'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='新竹竹' then '新竹縣'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='竹北市' then '新竹縣'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='資訊室' then '新竹市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='林啟宏' then '新竹市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,3)='300' then '新竹市'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,2)='彰化' then '彰化縣'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,2)='臺東' then '台東縣'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,2)='台東' then '台東縣'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,2)='宜蘭' then '宜蘭縣'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,2)='屏東' then '屏東縣'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,2)='雲林' then '雲林縣'")
			.append(" when substring(replace(e.INSTALL_ADDRESS,' ',''),1,2)='花蓮' then '花蓮縣'")
			.append(" else substring(replace(e.INSTALL_ADDRESS,' ',''),1,3) end ) ")
			//Task #2584 2017/10/23 加新sql
			.append(" else (  case when a.mer_address='' or a.mer_address is null  then null ")
			.append(" when substring(a.mer_address,1,3) in ('台中河','八德路','大同區','北市南','北市中','中山區','北投區','世貿一','北市內','中正區','(大直','北市中','台北中','台北世','台北生','台北巿','台北市','永和區','松山區','南港展','萬華區') then '台北市' ")
			.append(" when substring(a.mer_address,1,3) in ('中市北','台中巿','台中巿','台中縣') then '台北市'")
			.append(" when substring(a.mer_address,1,3) in ('台北縣','市淡水','新店區') then '新北市'")
			.append(" when substring(a.mer_address,1,3) in ('臺東市','台東市') then '台東縣'")
			.append(" when substring(a.mer_address,1,3) in ('宜蘭市') then '宜蘭縣'")
			.append(" when substring(a.mer_address,1,3) in ('竹北市') then '新竹縣'")
			.append(" when substring(a.mer_address,1,3) in ('花蓮市') then '花蓮縣'")
			.append(" when substring(a.mer_address,1,3) in ('南投市') then '南投縣'")
			.append(" when substring(a.mer_address,1,3) in ('屏東市') then '屏東縣'")
			.append(" when substring(a.mer_address,1,3) in ('雲林鎮') then '雲林縣'")
			.append(" when substring(a.mer_address,1,3) in ('新竹巿') then '新竹市'")
			.append(" when substring(a.mer_address,1,3) in ('嘉義鄉') then '嘉義縣'")
			.append(" when substring(a.mer_address,1,3) in ('彰化市') then '彰化縣'")
			.append(" when substring(a.mer_address,1,3) in ('薩摩亞') then '高雄市'")
			.append(" when substring(a.mer_address,1,3) in ('高雄巿','高雄巨','高雄市','高雄設') then '高雄市'")
			.append(" when replace(substring(a.mer_address,1,3),'臺','台')='桃園縣' then '桃園市'")
			.append(" when substring(a.mer_address,1,4) in ('2臺北市','4臺北市') then '台北市'")
			.append(" when substring(a.mer_address,1,5) in ('11491','00臺北市') then '台北市'")
			.append(" when substring(a.mer_address,1,5) in ('24892') then '新北市'")
			.append(" when substring(a.mer_address,1,5) in ('00新竹市') then '新竹市'")
			.append(" when substring(a.mer_address,1,5) in ('51南投縣') then '南投縣'")
			.append(" when substring(a.mer_address,1,5) in ('保險套情人','潮肉壽喜燒') then null")
			.append(" when substring(a.mer_address,1,6) in ('000812') then null")
			.append(" when substring(a.mer_address,1,6) in ('103大同區') then '台北市'")
			.append(" when substring(a.mer_address,4,3)='桃園縣' then '桃園市'")
			.append(" when substring(a.mer_address,1,1) in ('0','1','2','3','4','5','6','7','8','9','０','１','２','３','４','５','６','７','８','９') then replace(substring(a.mer_address,4,3),'臺','台')")
			.append(" else replace(substring(a.mer_address,1,3),'臺','台') end ) end) installedAdressLocation,")
			//Bug #2575 2017/10/09
			.append(" case when a.approved=1 then 'Y' else 'N' end as isChecked,")
			.append(" case when a.approved_cust=1 then 'Y' else 'N' end as isCustomerChecked,")
			//.append(" case when a.approved=0 then 'N' else 'Y' end as isChecked,")
			//.append(" case when a.approved_cust=0 then 'N' else 'Y' end as isCustomerChecked,")
			// --    DEPARTMENT_Name  departmentId
			.append(" case when n.name ='維護管理部' then 'CUSTOMER_SERVICE' else n.name end as departmentId,")
			//-- repair_vendor2 repairCompany
//			.append(" case when a.repair_vendor='百富' then 'PAX' else null end as repairCompany, ")
			.append(" case when a.status_id=7 then 'PAX' else null end as repairCompany, ")
			//新增三個欄位
			.append(" nn.name as maintainUser, x.MODIFYINFO_MODIFY_DATE as analyzeDate,null as uninstallOrRepairReason, ")
			 //Bug #2583 新增之欄位-裝機部門，沒轉入，欄位資料 2017/10/13
			.append(" n1.name as installedDeptId ")
			.append(" from AIMS.").append(aimsSchema).append(".repository a")
			.append(" join AIMS.").append(aimsSchema).append(".asset_category b on a.asset_category_id=b.id")
			.append(" left join AIMS.").append(aimsSchema).append(".storehous c on a.storehous_id=c.id")
			.append(" left join ").append(fomsSchema).append(".FOMS_EDC_DEVICE f on a.dtid=f.dtid and f.identifier in (select max(bb.identifier) from ")
			.append(fomsSchema).append(".FOMS_SFM_Request aa join ")
			.append(fomsSchema).append(".FOMS_EDC_DEVICE bb on aa.identifier=bb.requestId and bb.dtid=a.dtid where aa.status in ('StatusComplete','StatusEnd'))")
			.append(" left join ").append(fomsSchema).append(".FOMS_SFM_Request e on f.requestId=e.identifier")
			.append(" left join ").append(fomsSchema).append(".FOMS_EDC_EdcWorkInventoryFlow d on e.identifier=d.requestId and d.identifier in (select max(aa.identifier) from ")
			.append(fomsSchema).append(".FOMS_EDC_EdcWorkInventoryFlow aa where aa.requestId=e.identifier)")
			.append(" left join ").append(fomsSchema).append(".CUM_Title g on f.merchantIdentifier=g.identifier")
			.append(" left join ").append(fomsSchema).append(".CUM_MERCHANT h on g.MARCHANT_ID=h.identifier")
			.append(" left join AIMS.").append(aimsSchema).append(".vendor i on a.ma_vendor_id=i.id")
			.append(" left join AIMS.").append(aimsSchema).append(".account j on a.create_account_id=j.id")
			.append(" left join AIMS.").append(aimsSchema).append(".account k on a.modify_account_id=k.id")
			.append(" left join AIMS.").append(aimsSchema).append(".Core_Option l on a.parts_id=l.id")
			.append(" left join AIMS.").append(aimsSchema).append(".Core_Option m on a.condition_id=m.id")
			.append(" left join ").append(fomsSchema).append(".ORG_PARTY n on d.factoryIdentifier=n.Identifier")
			.append(" left join ").append(fomsSchema).append(".ORG_PARTY nn on d.assignedEngineerIdentifier=nn.Identifier left join (select aa.EdcWorkInventoryFlow_Identifier as FlowKey,max(aa.identifier) as ProcessKey from ")
			.append(fomsSchema).append(".FOMS_EDC_EdcWorkInventoryFlowLog aa where aa.processtype in ('censor') group by aa.EdcWorkInventoryFlow_Identifier) w on d.identifier=w.FlowKey")
			.append(" left join ").append(fomsSchema).append(".FOMS_EDC_EdcWorkInventoryFlowLog x on w.ProcessKey=x.identifier")
			//Bug #2583 新增之欄位-裝機部門，沒轉入，欄位資料 2017/10/13
			.append(" left join (select b1.dtid,max(b1.identifier) as DeviceKey1 from ")
			.append(fomsSchema).append(".FOMS_SFM_Request a1 join ")
			.append(fomsSchema).append(".FOMS_EDC_DEVICE b1 on a1.identifier=b1.requestId and b1.dtid is not null and b1.dtid<>'' where a1.requestType_ID=367 and a1.status in ('StatusComplete','StatusEnd') group by b1.dtid ) as fomsData on a.dtid=fomsData.dtid left join ")
			.append(fomsSchema).append(".FOMS_EDC_DEVICE f1 on fomsData.DeviceKey1=f1.identifier  left join ")
			.append(fomsSchema).append(".FOMS_SFM_Request e1 on f1.requestId=e1.identifier left join (select aa1.requestId,max(aa1.identifier) as FormKey1 from ")
			.append(fomsSchema).append(".FOMS_EDC_EdcWorkInventoryFlow aa1 join ")
			.append(fomsSchema).append(".FOMS_SFM_Request bb1 on aa1.requestId=bb1.identifier where bb1.requestType_ID=367 and bb1.status in ('StatusComplete','StatusEnd') group by aa1.requestId) ee1 on e1.identifier=ee1.requestId left join ")
			.append(fomsSchema).append(".FOMS_EDC_EdcWorkInventoryFlow d1 on ee1.FormKey1=d1.identifier left join ")
			.append(fomsSchema).append(".ORG_PARTY n1 on d1.factoryIdentifier=n1.Identifier")
			//Task #2854 不轉的那兩筆設備也不查出來
			.append(" where a.valid=1 and a.status_id in (2,4,5,6,7,8,12,17) and a.owner not in ('三川行','其他') and ((a.owner<>'台新銀行') or (a.owner='台新銀行' and a.customer<>'台新銀行')) and a.sn <> '64998216' and a.sn <> '64998215' ");
			
			AliasBean aliasBean = new AliasBean(DmmRepositoryDTO.class);
			aliasBean.addScalar("assetId", StringType.INSTANCE);//1
			aliasBean.addScalar("serialNumber", StringType.INSTANCE);//1
			aliasBean.addScalar("assetTypeId", StringType.INSTANCE);//1
			aliasBean.addScalar("brand", StringType.INSTANCE);//1
			aliasBean.addScalar("propertyId", StringType.INSTANCE);//1
			aliasBean.addScalar("warehouseId", StringType.INSTANCE);//1
			aliasBean.addScalar("contractId", StringType.INSTANCE);//1
			aliasBean.addScalar("maType", StringType.INSTANCE);//1
			aliasBean.addScalar("status", StringType.INSTANCE);//1
			aliasBean.addScalar("isEnabled", StringType.INSTANCE);//1
			aliasBean.addScalar("enableDate", TimestampType.INSTANCE);//1
			aliasBean.addScalar("simEnableDate", TimestampType.INSTANCE);
			aliasBean.addScalar("simEnableNo", StringType.INSTANCE);//1
			aliasBean.addScalar("carrier", StringType.INSTANCE);//1
			aliasBean.addScalar("carryDate", TimestampType.INSTANCE);//1
			aliasBean.addScalar("borrower", StringType.INSTANCE);//1
			aliasBean.addScalar("borrowerStart", TimestampType.INSTANCE);//1
			aliasBean.addScalar("borrowerEnd", TimestampType.INSTANCE);//1
			aliasBean.addScalar("borrowerEmail", StringType.INSTANCE);
			aliasBean.addScalar("borrowerMgrEmail", StringType.INSTANCE);
			aliasBean.addScalar("backDate", TimestampType.INSTANCE);//1
			aliasBean.addScalar("assetOwner", StringType.INSTANCE);//1
			aliasBean.addScalar("assetUser", StringType.INSTANCE);//1
			aliasBean.addScalar("isCup", StringType.INSTANCE);//1
			aliasBean.addScalar("retireReasonCode", StringType.INSTANCE);
			aliasBean.addScalar("caseId", StringType.INSTANCE);//1
			aliasBean.addScalar("caseCompletionDate", TimestampType.INSTANCE);//1
			//aliasBean.addScalar("caseCloseDate", TimestampType.INSTANCE);//1
			aliasBean.addScalar("tid", StringType.INSTANCE);//1
			aliasBean.addScalar("dtid", StringType.INSTANCE);//1
			aliasBean.addScalar("applicationId", StringType.INSTANCE);
			aliasBean.addScalar("merchantId", StringType.INSTANCE);//1
			aliasBean.addScalar("merchantHeaderId", StringType.INSTANCE);//1
			aliasBean.addScalar("assetInId", StringType.INSTANCE);//1
			aliasBean.addScalar("assetInTime", TimestampType.INSTANCE);//1
			aliasBean.addScalar("assetTransId", StringType.INSTANCE);
			aliasBean.addScalar("maintainCompany", StringType.INSTANCE);//1
			aliasBean.addScalar("repairVendor", StringType.INSTANCE);//1
			aliasBean.addScalar("description", StringType.INSTANCE);//1
			aliasBean.addScalar("action", StringType.INSTANCE);//1
			aliasBean.addScalar("faultComponent", StringType.INSTANCE);//1
			aliasBean.addScalar("faultDescription", StringType.INSTANCE);//1
			aliasBean.addScalar("customerWarrantyDate", TimestampType.INSTANCE);//1
			aliasBean.addScalar("factoryWarrantyDate", TimestampType.INSTANCE);//1
			aliasBean.addScalar("createUser", StringType.INSTANCE);//1
			aliasBean.addScalar("createUserName", StringType.INSTANCE);//1
			aliasBean.addScalar("createDate", TimestampType.INSTANCE);//1
			aliasBean.addScalar("updateUser", StringType.INSTANCE);//1
			aliasBean.addScalar("updateUserName", StringType.INSTANCE);//1
			aliasBean.addScalar("updateDate", TimestampType.INSTANCE);//1
			//aliasBean.addScalar("deleted", StringType.INSTANCE);//1
			aliasBean.addScalar("checkedDate", TimestampType.INSTANCE);//1
			aliasBean.addScalar("assetModel", StringType.INSTANCE);//1
			aliasBean.addScalar("installedAdress", StringType.INSTANCE);//1
			aliasBean.addScalar("installType", StringType.INSTANCE);
			aliasBean.addScalar("cyberApprovedDate", TimestampType.INSTANCE);//1
			aliasBean.addScalar("keeperName", StringType.INSTANCE);
			aliasBean.addScalar("installedAdressLocation", StringType.INSTANCE);//1
			aliasBean.addScalar("isChecked", StringType.INSTANCE);//1
			aliasBean.addScalar("isCustomerChecked", StringType.INSTANCE);//1
			aliasBean.addScalar("departmentId", StringType.INSTANCE);//1
			aliasBean.addScalar("repairCompany", StringType.INSTANCE);//1
			aliasBean.addScalar("maintainUser", StringType.INSTANCE);//1
			aliasBean.addScalar("analyzeDate", DateType.INSTANCE);//1
			aliasBean.addScalar("uninstallOrRepairReason", StringType.INSTANCE);//1  
			aliasBean.addScalar("installedDeptId", StringType.INSTANCE);
			aliasBean.addScalar("isNoMerId", StringType.INSTANCE);
			aliasBean.addScalar("isNoMerHeaderId", StringType.INSTANCE);
			aliasBean.addScalar("merName", StringType.INSTANCE);
			aliasBean.addScalar("assetCateGory", StringType.INSTANCE);
			LOGGER.debug("listRepository()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return result;
		} catch (Exception e) {
			LOGGER.error("listRepository()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#listHistoryRepository()
	 */
	@Override
	public List<DmmRepositoryHistoryDTO> listHistoryRepository() throws DataAccessException {
		List<DmmRepositoryHistoryDTO> result = null;
		//得到schema
		String aimsSchema = this.getSchema("iatoms-aims");
		String fomsSchema = this.getSchema("iatoms-foms");
		SqlQueryBean sqlQueryBean = new SqlQueryBean();
		try {
			sqlQueryBean.append("select a.id as historyId, a.sn as assetId, a.sn as serialNumber,")
			.append(" b.name as assetTypeId,")
			//Bug #2941
			.append(" case when b.asset_type_id=7 then 'EDC' ")
			.append(" when b.asset_type_id=8 then 'Related_Products' end as assetCategory,")
			.append(" a.sn2 as propertyId,")
			//--將storehous換為warehouseId
			.append(" c.name as warehouseId,")
			.append(" case when a.contract_id='CS-1702-10' then 'CS1702-10' when a.contract_id='SCSB-80' then 'SCSB-S80' else a.contract_id end as contractId,")
			//--將MA_TYPE換為maType
			.append(" case when a.owner=a.customer then 'BUYOUT' else 'LEASE' end as maType,")
			//--將status_id換為status
//			.append(" case when a.status_id=2 then 'REPERTORY'  when a.status_id=4 then 'ON_WAY' when a.status_id=5 then 'IN_APPLY' when a.status_id=6 then 'IN_USE' when a.status_id=7 and a.repair_vendor in ('經貿','經貿維修倉','維修倉','普林達','經貿聯網','') then 'REPAIR'  when a.status_id=7 and a.repair_vendor in ('百富') then 'MAINTENANCE' when a.status_id=7 and a.repair_vendor is null then 'REPAIR'  when a.status_id=8 then 'BORROWING'  when a.status_id=12 then 'DISABLED' when a.status_id=17 then 'PENDING_DISABLED' else null end as status,")
			// Task #2585 
			.append(" case when a.status_id=2 then 'REPERTORY'  when a.status_id=4 then 'ON_WAY' when a.status_id=5 then 'IN_APPLY' when a.status_id=6 then 'IN_USE' when a.status_id=7 then 'MAINTENANCE' when a.status_id=8 then 'BORROWING'  when a.status_id=12 then 'DISABLED' when a.status_id=17 then 'PENDING_DISABLED' else null end as status,")
			
			//--將IS_ENABLED換為isEnabled
			.append(" case when a.enable_date is not null then 'Y' else 'N' end as isEnabled,")
			.append("a.enable_date as enableDate,")
			//--將SIM_ENABLE_DATE換為simEnableDate
			.append(" null as simEnableDate,")
			//--這玩意居然是租賃編號。。。
			.append(" a.serial_number as simEnableNo,")
			//5=領用
			.append(" case when a.status_id=5 then a.CARRIER else null end as carrier,")
//			.append(" a.fn_select_date as carryDate,")
			// Task #2573
			.append(" case when a.status_id=5 then a.fn_select_date else null end as carryDate,")
			
			//8=借用
			.append(" case when a.status_id=8 then a.CARRIER else null end as borrower,")
//			.append(" a.lease_date_start as borrowerStart,")
//			.append(" a.lease_date_end as borrowerEnd,")
			// Task #2573
			.append(" case when a.status_id=8 then a.lease_date_start else null end as borrowerStart,")
			.append(" case when a.status_id=8 then a.lease_date_end else null end as borrowerEnd,")
			
			.append(" null as borrowerEmail,")
			.append(" null as borrowerMgrEmail,")
			.append(" a.lease_date_back as backDate,")
			//-- 將owner 換為 assetOwner
			.append(" case when a.owner='台新銀行' then 'TSB-EDC' when a.owner='經貿聯網' then 'CYB' when a.owner='百富科技' then 'PAX' when a.owner='環匯' then 'GP' when a.owner='捷達威' then 'JDW-EDC' when a.owner='彰銀' then 'CHB_EDC'  when a.owner='大眾銀行' then 'TCB_EDC' when a.owner='上海商銀' then 'SCSB' when a.owner='歐付寶' then 'GreenWorld'  when a.owner='陽信銀行' then 'SYB-EDC' else null end as assetOwner,")
			//--  將customer 換為 assetUser
			.append(" case when a.customer='台新銀行' then 'TSB-EDC' when a.customer='經貿聯網' then 'CYB'  when a.customer='百富科技' then 'PAX' when a.customer='環匯' then 'GP' when a.customer='捷達威' then 'JDW-EDC' when a.customer='彰銀' then 'CHB_EDC' when a.customer='大眾銀行' then 'TCB_EDC' when a.customer='上海商銀' then 'SCSB'  when a.customer='歐付寶' then 'GreenWorld' when a.customer='陽信銀行' then 'SYB-EDC' else null end as assetUser,")
			//-- 將flag_cup 換為 isCup
			.append(" case when a.flag_cup=1 then 'Y' else 'N' end as isCup, ")
			//--  將RETIRE_REASON_CODE 換為 retireReasonCode
			.append(" null as retireReasonCode,")
			.append(" a.working_no as caseId,")
			//Task #2587    2017/10/12
			.append(" a.repaired_date as caseCompletionDate,")
			//.append(" null as caseCloseDate,")
			.append(" null as tid,")
			.append(" a.dtid as dtid,")
			//--暫時不加
			.append("null as applicationId,")
			//.append(" h.MARCHANT_ID as merchantId,")
			/*.append(" a.mer_mid as merchantId,")*/
			.append(" a.mer_name as merName, ")
			//.append(" g.identifier as merchantHeaderId,")
			/*.append(" a.mer_edctitle as merchantHeaderId,")*/
			.append(" mer.identifier as merchantId, ")
			.append(" hander.identifier as merchantHeaderId, ")
			//查不到的存入old欄位
			//Task #2588 若對應不到資料，新增特店、表頭資料 2017/10/16
			.append(" (case when mer.identifier is null and a.mer_mid is not null then a.mer_mid else null end) as oldMerchantCode,")
			.append(" (case when mer.identifier is null and a.mer_mid is not null then a.mer_name else null end) as oldMerchantName,")
            .append(" (case when mer.identifier is null and a.mer_edctitle is not null then a.mer_edctitle else null end) as oldMerchantHeader,")
 
			.append(" a.batch_inv_no as assetInId,")
			.append(" a.create_date as assetInTime,")
			.append(" null as assetTransId,")
			// -- 將 compShortCode 換為  maintainCompany
			.append(" case when i.name='普林達' then 'PRINDA' when i.name='經貿聯網' then 'CYB' when i.name='普鴻資訊' then 'PRINDA' else null end as maintainCompany,")
			//--  將 repair_vendor 換為  repairVendor
//			.append(" case when a.repair_vendor in ('經貿','經貿維修倉','維修倉','普林達','經貿聯網') then 'CYB' else null end as repairVendor,")
			.append(" case when a.status_id=7 then 'PAX' else null end as repairVendor,")
			//  --將 lease_comment 換為 description
			.append(" case when a.status_id in (5,8) and len(a.carrier) > 0 and len(a.lease_comment) > 0 then a.carrier + ',' + a.lease_comment when a.status_id in (5,8) and len(a.carrier) > 0 and len(a.lease_comment) <= 0 then a.carrier else a.lease_comment end as description,")
			//  --  將 return_reason  換為 action
			.append(" case when return_reason='報廢' then 'DISABLED' when return_reason='借用' then 'BORROW' when return_reason='領用' then 'APPLY' when return_reason='送修' then 'MAINTENANCE' when return_reason='遺失' then 'LOSS' when return_reason='送修' then 'MAINTENANCE' when return_reason in ('歸還','歸庫','備機歸還','備卡歸還','教育訓練機歸還') then 'BACK' when return_reason in ('維修','轉維修','轉經貿維修倉','故障轉寄維修倉') then 'REPAIR'  else 'OTHER_EDIT' end as action,")
			//--將 parts_name 換為  faultComponent(name)
			
			.append(" l.Option_Item as faultComponent,")
			// --將 faultDescription 換為  condition_name
			.append(" m.Option_Item as faultDescription,")
			.append(" a.guarantee_date_cust as customerWarrantyDate,")
			.append(" a.guarantee_date as factoryWarrantyDate,")
			.append(" a.create_account_id as createUser,")
			// -- 將 createname 換為 createUserName
			.append(" j.cname as createUserName,")
			.append(" a.create_date as createDate,")
			.append(" a.modify_account_id as updateUser,")
			//--將 updatename 換為 updateUserName
			.append(" k.cname as updateUserName,  ")
			.append(" a.modify_date as updateDate,")
			//-- 將  valid 換為 deleted
			//.append(" case when a.valid=1 then 'N' else 'Y' end as deleted,")
			// --驗收日期
			//Task #2769 客戶驗收時間=入庫驗收時間  驗收日期 改抓 app_date 2017/11/06
			.append(" a.app_date as checkedDate,")
			.append(" a.model as assetModel,")
			// --將 INSTALLED_ADRESS 換為 merInstallAddress  
			.append(" case when len(a.mer_address)=1 then a.mer_address when len(replace(a.mer_address,' ','')) < = 0 then replace(a.mer_address,' ','') when substring(a.mer_address,1,2) in ('中市','北市','園市') then substring(a.mer_address,3,len(a.mer_address)-2) ")
			.append(" when substring(a.mer_address,1,3) in (")
			.append(" '台北世','板橋市','(大直','000','00新','EM2','八德路','大安區','中山區','中正區','中和市',")
			.append(" '中壢市','仁愛路','內湖路','斗六市','世貿一','世貿二','世貿三','世貿展','北平東','北投區','台中世',")
			.append(" '台中區','台北小','台北世','台北生','台東市','台新銀','永和市','永和區','竹北市','克拉美','宜蘭市',")
			.append(" '板橋市','板橋區','松山區','松菸一','花博會','花蓮市','保險套','南投市','南京東','南港展','屏東市',")
			.append(" '苗栗市','桃園機','高雄巨','高雄展','高雄設','康技資','華山１','園市中','新光三','新店區','新莊市',")
			.append(" '裝機地','彰化市','臺北世','臺東市','臺灣大','薩摩亞','豐原市') ")
			.append(" then a.mer_address when substring(a.mer_address,1,3) in ('台中西','台中河','台北大','台北中','台北松','台北信', '台北華','台南歸','竹縣竹','高雄三','臺南永')")
			.append(" then substring(a.mer_address,3,len(a.mer_address)-2)")
			.append(" when substring(a.mer_address,1,4) in ('0新北市','2新北市','2臺北市','4臺北市','5臺北市','6臺北市') then substring(a.mer_address,5,len(a.mer_address)-4)")
			.append(" when substring(a.mer_address,1,5) in ('00臺北市','04臺中市','04臺北市','06嘉義縣','21嘉義縣','51南投縣')  then substring(a.mer_address,6,len(a.mer_address)-5)")
            .append(" when substring(a.mer_address,1,6) in ('100台北市','100臺北市','１００臺北市','１００台北市',")
            .append(" '１０３臺北市','１０３台北市','103臺北市','103台北市','104台北市','104臺北市','１０４臺北市',")
			.append(" '１０４台北市','105臺北市','105台北市','１０５臺北市','１０５台北市','１０６臺北市','１０６台北市',")
			.append(" '106臺北市','106台北市','108臺北市','108台北市','１０８臺北市','１０８台北市','110台北市','110臺北市',")
			.append(" '１１０台北市','１１０臺北市','111台北市','111臺北市','１１１台北市','１１１臺北市','１１２臺北市','１１２台北市',")
			.append(" '112臺北市','112台北市','１１４臺北市','１１４台北市','114臺北市','114台北市','１１５臺北市','１１５台北市',")
			.append(" '115臺北市','115台北市','１１６臺北市','１１６台北市','116臺北市','116台北市','200基隆市','201基隆市',")
			.append(" '203基隆市','220新北市','221新北市','231新北市','234新北市','235新北市','236新北市','237新北市',")
			.append(" '238新北市','241新北市','２４１新北市','242新北市','２４２新北市','243新北市','244新北市','247新北市',")
			.append(" '248新北市','251新北市','260宜蘭縣','265宜蘭縣','267宜蘭縣','270宜蘭縣','300新竹市','３００新竹市',")
			.append(" '302新竹縣','３０２新竹縣','303新竹縣','305新竹縣','306新竹縣','320桃園縣','３２０桃園縣','320桃園市',")
			.append(" '３２０桃園市','324桃園縣','324桃園市','３２５桃園縣','３２５桃園市','325桃園縣','325桃園市','326桃園市',")
			.append(" '330桃園縣','330桃園市','３３０桃園縣','３３０桃園市','３３３桃園縣','３３３桃園市','333桃園縣','333桃園市',")
			.append(" '334桃園縣','334桃園市','３３７桃園縣','３３７桃園市','337桃園縣','337桃園市','338桃園縣','338桃園市',")
			.append(" '３３８桃園縣','３３８桃園市','350苗栗縣','351苗栗縣','357苗栗縣','360苗栗縣','367苗栗縣','400臺中市',")
			.append(" '４００臺中市','401臺中市','402臺中市','403臺中市','４０４臺中市','404臺中市','406臺中市','407台中市',")
			.append(" '407臺中市','408臺中市','４０８臺中市','411臺中市','412臺中市','４１２臺中市','413臺中市','414臺中市',")
			.append(" '420臺中市','421臺中市','424臺中市','427臺中市','428臺中市','428台中市','429臺中市','432臺中市',")
			.append(" '433臺中市','４３３臺中市','435臺中市','436臺中市','４３７臺中市','437臺中市','500彰化縣','５００彰化縣',")
			.append(" '503彰化縣','505彰化縣','506彰化縣','508彰化縣','５０８彰化縣','５１０彰化縣','510彰化縣','511彰化縣',")
			.append(" '513彰化縣','515彰化縣','521彰化縣','522彰化縣','523彰化縣','526彰化縣','540南投縣','５４２南投縣',")
			.append(" '５４２南投縣','545南投縣','５４５南投縣','555南投縣','557南投縣','600嘉義市','６００嘉義市','602嘉義縣',")
			.append(" '603嘉義縣','605嘉義縣','606嘉義縣','612嘉義縣','616嘉義縣','621嘉義縣','６２１嘉義縣','632雲林縣',")
			.append(" '638雲林縣','640雲林縣','647雲林縣','651雲林縣','700台南市','700臺南市','７００臺南市','７００台南市',")
			.append(" '701臺南市','701台南市','７０１臺南市','７０１台南市','702臺南市','703臺南市','704臺南市','704台南市',")
			.append(" '708臺南市','709臺南市','710臺南市','711臺南市','７１１臺南市','717臺南市','721臺南市','722臺南市',")
			.append(" '726臺南市','730臺南市','741臺南市','742臺南市','743臺南市','744臺南市','745臺南市','８００高雄市',")
			.append(" '800高雄市','801高雄市','８０１高雄市','802高雄市','８０２高雄市','803高雄市','804高雄市','805高雄市',")
			.append(" '806高雄市','８０６高雄市','807高雄市','811高雄市','８１１高雄市','812高雄市','８１２高雄市','813高雄市',")
			.append(" '814高雄市','815高雄市','820高雄市','８２０高雄市','821高雄市','824高雄市','829高雄市','830高雄市',")
			.append(" '８３０高雄市','831高雄市','832高雄市','833高雄市','840高雄市','842高雄市','８４２高雄市','849高雄市',")
			.append(" '852高雄市','880澎湖縣','884澎湖縣','891金門縣','892金門縣','900屏東縣','905屏東縣','909屏東縣',")
			.append(" '912屏東縣','920屏東縣','927屏東縣','928屏東縣','９２８屏東縣','946屏東縣','９４６屏東縣','950臺東縣',")
			.append(" '９５０臺東縣','954臺東縣','955臺東縣','959臺東縣','970花蓮縣','９７０花蓮縣','971花蓮縣','973花蓮縣')")
            .append(" then substring(a.mer_address,7,len(a.mer_address)-6) when substring(a.mer_address,1,8) in ('11491台北市','11491臺北市','4403 台中市') ")  
            .append(" then substring(a.mer_address,9,len(a.mer_address)-8) else substring(a.mer_address,4,len(a.mer_address)-3) end as installedAdress,")
			// -- INSTALL_TYPE    installType
			.append(" null as installType,")
			.append(" a.app_date as cyberApprovedDate,")
			.append(" null as keeperName,")
			.append(" case when len(a.mer_address)=1 THEN null when n.name='臺北市' then '台北市'")
			.append(" when n.name='基隆' then '基隆市'")
			.append(" when n.name='桃園' then '桃園市'")
            .append(" when n.name='苗栗' then '苗栗縣'")
            .append(" when n.name='臺中' then '台中市'")
            .append(" when n.name='南投' then '南投縣'")
            .append(" when n.name='彰化' then '彰化縣'")
			.append(" when n.name='雲林' then '雲林縣'")
			.append(" when n.name='臺南' then '台南市'")
			.append(" when n.name='高雄縣' then '高雄市'")
			.append(" when n.name='宜蘭' then '宜蘭縣'")
			.append(" when n.name='花蓮' then '花蓮縣'")
			.append(" when n.name='臺東' then '台東縣'")
			.append(" when n.name='屏東' then '屏東縣'")
			.append(" when n.name='澎湖' then '澎湖縣'")
			.append(" when n.name='連江' then '連江縣'")
			.append(" when n.name='新竹' and substring(a.mer_address,1,3) in ('新竹市','新竹巿') then '新竹市'")
			.append(" when n.name='新竹' and substring(a.mer_address,1,3) in ('新竹縣','竹北市','竹縣竹') then '新竹縣'")
			.append(" when n.name='新竹' and substring(a.mer_address,1,6) in ('300新竹市','３００新竹市') then '新竹市'")
			.append(" when n.name='新竹' and substring(a.mer_address,1,6) in ('302新竹縣','３０２新竹縣','303新竹縣','305新竹縣','306新竹縣') then '新竹縣'")
			.append(" when n.name='嘉義' and substring(a.mer_address,1,3) in ('嘉義市') then '嘉義市'")
			.append(" when n.name='嘉義' and substring(a.mer_address,1,3) in ('嘉義縣') then '嘉義縣'")
			.append(" when n.name='嘉義' and substring(a.mer_address,1,6) in ('600嘉義市','６００嘉義市') then '嘉義市'")
			.append(" when n.name='嘉義' and substring(a.mer_address,1,6) in ('06嘉義縣中','602嘉義縣','605嘉義縣','606嘉義縣','612嘉義縣','616嘉義縣','621嘉義縣','21嘉義縣民','６２１嘉義縣') then '嘉義縣'")
			.append(" else null end as installedAdressLocation,")
			//Task #2589 2017/10/09
			.append(" case when a.approved=1 then 'Y' else 'N' end as isChecked,")
			.append(" case when a.approved_cust=1 then 'Y' else 'N' end as isCustomerChecked,")
			// --    DEPARTMENT_Name  departmentId
			.append(" null as departmentId,")
			//-- repair_vendor2 repairCompany
//			.append(" case when a.repair_vendor='百富' then 'PAX' else null end as repairCompany,")
			.append(" case when a.status_id=7 then 'PAX' else null end as repairCompany, ")
			.append(" a.brand as brand,")
			//新增三個欄位
			.append(" null as maintainUser, null as analyzeDate, null as uninstallOrRepairReason ")			
			.append(" from AIMS.").append(aimsSchema).append(".repository_History a")
			.append(" join AIMS.").append(aimsSchema).append(".asset_category b on a.asset_category_id=b.id")
			.append(" left join AIMS.").append(aimsSchema).append(".storehous c on a.storehous_id=c.id")
			.append(" left join AIMS.").append(aimsSchema).append(".vendor i on a.ma_vendor_id=i.id")
			.append(" left join AIMS.").append(aimsSchema).append(".account j on a.create_account_id=j.id")
			.append(" left join AIMS.").append(aimsSchema).append(".account k on a.modify_account_id=k.id")
			.append(" left join AIMS.").append(aimsSchema).append(".Core_Option l on a.parts_id=l.id")
			.append(" left join AIMS.").append(aimsSchema).append(".Core_Option m on a.condition_id=m.id")
			.append(" left join AIMS.").append(aimsSchema).append(".location n on a.mer_loc_id=n.id")
			//
			.append(" left join ").append("(select max(aum.identifier) as identifier,aum.MARCHANT_ID as MARCHANT_ID,aum.CORPORATION_ID as CORPORATION_ID, c.compShortCode as compShortCode from ") 
			.append(fomsSchema).append(".CUM_MERCHANT aum left join ")
			.append(fomsSchema).append(".CUM_CORPORATION c on aum.CORPORATION_ID=c.identifier group by aum.CORPORATION_ID,compShortCode, aum.MARCHANT_ID ) mer on a.mer_mid=mer.MARCHANT_ID and mer.compShortCode = ")
			.append(" (case when a.customer='台新銀行' then 'TSB-EDC'")
			.append(" when a.customer='經貿聯網' then 'CYB'")
			.append(" when a.customer='百富科技' then 'PAX'")
			.append(" when a.customer='環匯' then 'GP'")
			.append(" when a.customer='捷達威' then 'JDW-EDC'")
			.append(" when a.customer='彰銀' then 'CHB_EDC'")
			.append(" when a.customer='大眾銀行' then 'TCB_EDC'")
			.append(" when a.customer='上海商銀' then 'SCSB'")
			.append(" when a.customer='歐付寶' then 'GreenWorld'")
			.append(" when a.customer='陽信銀行' then 'SYB-EDC'")
			.append(" else null end ) ")
			//.append(" left join ").append(fomsSchema).append(".CUM_CORPORATION corp on mer.CORPORATION_ID=corp.identifier and corp.compShortCode = a.customer")
			.append(" left join ").append(fomsSchema).append(".CUM_Title hander on CAST ( hander.MARCHANT_ID AS varchar )=a.mer_mid and hander.LOCATION_NAME = a.mer_edctitle")

			.append(" where a.sn in (select sn from ")
			//Task #2854 不轉的那兩筆設備也不查出來
			.append(" AIMS.").append(aimsSchema).append(".repository where valid=1 and  status_id in (2,4,5,6,7,8,12,17) and ((owner not in ('三川行','其他','台新銀行')) or (owner='台新銀行' and customer<>'台新銀行'))) and a.status_id in (2,4,5,6,7,8,12,17) and a.sn <> '64998216' and a.sn <> '64998215' ");

			AliasBean aliasBean = new AliasBean(DmmRepositoryHistoryDTO.class);
			aliasBean.addScalar("historyId", StringType.INSTANCE);//1
			aliasBean.addScalar("assetId", StringType.INSTANCE);//1
			aliasBean.addScalar("serialNumber", StringType.INSTANCE);//1
			aliasBean.addScalar("assetTypeId", StringType.INSTANCE);//1
			aliasBean.addScalar("propertyId", StringType.INSTANCE);//1
			aliasBean.addScalar("warehouseId", StringType.INSTANCE);//1
			aliasBean.addScalar("contractId", StringType.INSTANCE);//1
			aliasBean.addScalar("maType", StringType.INSTANCE);//1
			aliasBean.addScalar("status", StringType.INSTANCE);//1
			aliasBean.addScalar("isEnabled", StringType.INSTANCE);//1
			aliasBean.addScalar("enableDate", TimestampType.INSTANCE);//1
			aliasBean.addScalar("simEnableDate", TimestampType.INSTANCE);
			aliasBean.addScalar("simEnableNo", StringType.INSTANCE);//1
			aliasBean.addScalar("carrier", StringType.INSTANCE);//1
			aliasBean.addScalar("carryDate", TimestampType.INSTANCE);//1
			aliasBean.addScalar("borrower", StringType.INSTANCE);//1
			aliasBean.addScalar("borrowerStart", TimestampType.INSTANCE);//1
			aliasBean.addScalar("borrowerEnd", TimestampType.INSTANCE);//1
			aliasBean.addScalar("borrowerEmail", StringType.INSTANCE);
			aliasBean.addScalar("borrowerMgrEmail", StringType.INSTANCE);
			aliasBean.addScalar("backDate", TimestampType.INSTANCE);//1
			aliasBean.addScalar("assetOwner", StringType.INSTANCE);//1
			aliasBean.addScalar("assetUser", StringType.INSTANCE);//1
			aliasBean.addScalar("isCup", StringType.INSTANCE);//1
			aliasBean.addScalar("retireReasonCode", StringType.INSTANCE);
			aliasBean.addScalar("caseId", StringType.INSTANCE);//1
			aliasBean.addScalar("caseCompletionDate", TimestampType.INSTANCE);//1
			//aliasBean.addScalar("caseCloseDate", TimestampType.INSTANCE);//1
			aliasBean.addScalar("tid", StringType.INSTANCE);//1
			aliasBean.addScalar("dtid", StringType.INSTANCE);//1
			aliasBean.addScalar("applicationId", StringType.INSTANCE);
			aliasBean.addScalar("merchantId", StringType.INSTANCE);//1 
			aliasBean.addScalar("merName", StringType.INSTANCE);
			aliasBean.addScalar("merchantHeaderId", StringType.INSTANCE);//1
			aliasBean.addScalar("assetInId", StringType.INSTANCE);//1
			aliasBean.addScalar("assetInTime", TimestampType.INSTANCE);//1
			aliasBean.addScalar("assetTransId", StringType.INSTANCE);
			aliasBean.addScalar("maintainCompany", StringType.INSTANCE);//1
			aliasBean.addScalar("repairVendor", StringType.INSTANCE);//1
			aliasBean.addScalar("description", StringType.INSTANCE);//1
			aliasBean.addScalar("action", StringType.INSTANCE);//1
			aliasBean.addScalar("faultComponent", StringType.INSTANCE);//1
			aliasBean.addScalar("faultDescription", StringType.INSTANCE);//1
			aliasBean.addScalar("customerWarrantyDate", TimestampType.INSTANCE);//1
			aliasBean.addScalar("factoryWarrantyDate", TimestampType.INSTANCE);//1
			aliasBean.addScalar("createUser", StringType.INSTANCE);//1
			aliasBean.addScalar("createUserName", StringType.INSTANCE);//1
			aliasBean.addScalar("createDate", TimestampType.INSTANCE);//1
			aliasBean.addScalar("updateUser", StringType.INSTANCE);//1
			aliasBean.addScalar("updateUserName", StringType.INSTANCE);//1
			aliasBean.addScalar("updateDate", TimestampType.INSTANCE);//1
			//aliasBean.addScalar("deleted", StringType.INSTANCE);//1
			aliasBean.addScalar("checkedDate", TimestampType.INSTANCE);//1
			aliasBean.addScalar("assetModel", StringType.INSTANCE);//1
			aliasBean.addScalar("installedAdress", StringType.INSTANCE);//1
			aliasBean.addScalar("installType", StringType.INSTANCE);
			aliasBean.addScalar("cyberApprovedDate", TimestampType.INSTANCE);//1
			aliasBean.addScalar("keeperName", StringType.INSTANCE);
			aliasBean.addScalar("installedAdressLocation", StringType.INSTANCE);//1
			aliasBean.addScalar("isChecked", StringType.INSTANCE);//1
			aliasBean.addScalar("isCustomerChecked", StringType.INSTANCE);//1
			aliasBean.addScalar("departmentId", StringType.INSTANCE);//1
			aliasBean.addScalar("repairCompany", StringType.INSTANCE);//1
			aliasBean.addScalar("brand", StringType.INSTANCE);//1
			
			aliasBean.addScalar("maintainUser", StringType.INSTANCE);//1
			aliasBean.addScalar("analyzeDate", DateType.INSTANCE);//1
			aliasBean.addScalar("uninstallOrRepairReason", StringType.INSTANCE);//1
			aliasBean.addScalar("oldMerchantCode", StringType.INSTANCE);//1
			aliasBean.addScalar("oldMerchantHeader", StringType.INSTANCE);//1
			aliasBean.addScalar("oldMerchantName", StringType.INSTANCE);//1
			aliasBean.addScalar("assetCategory", StringType.INSTANCE);
			LOGGER.debug("listHistoryRepository()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return result;
		} catch (Exception e) {
			LOGGER.error("listHistoryRepository()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#listCaseHandleInfo()
	 */
	@Override
	public List<SrmCaseHandleInfoDTO> listCaseHandleInfo(String transferCaseId)throws DataAccessException {
		List<SrmCaseHandleInfoDTO> result = null;
		//得到schema
		String schema = this.getSchema("iatoms-foms");
		SqlStatement sqlStatement = new SqlStatement();
		StringBuilder builder = new StringBuilder();
		try {
			sqlStatement.addSelectClause("a.requestNo", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue());

			builder.append(" case when a.requestType_ID=367 then 'INSTALL'");
			builder.append(" when a.requestType_ID=368 then 'MERGE'");
			builder.append(" when a.requestType_ID=369 then 'UNINSTALL'");
			builder.append(" when a.requestType_ID=370 then 'PROJECT'");
			builder.append(" when a.requestType_ID=371 then 'REPAIR'");
			builder.append(" when a.requestType_ID=372 then 'PROJECT'");
			builder.append(" when a.requestType_ID=373 then 'CHECK'");
			builder.append(" when a.requestType_ID=374 then 'UPDATE'");
			builder.append(" when a.requestType_ID=376 then 'PROJECT'");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue());

			sqlStatement.addSelectClause("b.compShortCode", SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue());
			sqlStatement.addSelectClause("d.contractNo", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTRACT_ID.getValue());
			sqlStatement.addSelectClause("a.CASENUM", SrmCaseHandleInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue());

			builder.delete(0, builder.length());
/*			builder.append(" case when a.status='StatusComplete' then 'ARRIVE_PROCESS'");
			builder.append(" when a.status='StatusEnd' and l.identifier is null then 'NO_DISPATCH'");
			builder.append(" when a.status='StatusEnd' and l.identifier is not null then 'SOFT_DISPATCH'");
			builder.append(" else null end");*/
			
			// Task #2637
/*			builder.append(" case when a.status='StatusComplete' and s1.ProcessKey is not null and l.identifier is not null then 'SOFT_DISPATCH'");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is not null and l.identifier is null then 'NO_DISPATCH'");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is null then 'ARRIVE_PROCESS' ");
			builder.append(" when a.status='StatusEnd' and l.identifier is null then 'NO_DISPATCH'");
			builder.append(" when a.status='StatusEnd' and l.identifier is not null then 'SOFT_DISPATCH'");
			builder.append(" else null end");*/
			// Task #2695
			builder.append(" case when a.status='StatusComplete' and s1.ProcessKey is not null and l.identifier is not null then 'SOFT_DISPATCH'");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is not null and l.identifier is null then 'NO_DISPATCH'");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is null and v.identifier is not null then 'ARRIVE_PROCESS'");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is null and v.identifier is null and l.identifier is not null then 'SOFT_DISPATCH'");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is null and v.identifier is null and l.identifier is null then 'NO_DISPATCH'");
			builder.append(" when a.status='StatusEnd' and l.identifier is null then 'NO_DISPATCH'");
			builder.append(" when a.status='StatusEnd' and l.identifier is not null then 'SOFT_DISPATCH'");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PROCESS_TYPE.getValue());

			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_TYPE.getValue());
			sqlStatement.addSelectClause("c.dtid", SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue());

			builder.delete(0, builder.length());
/*			builder.append(" case when k.name='EDC管理部' then 'CYB'");
			builder.append(" when k.name='維護五組' then 'PRINDA'");
			builder.append(" when k.name='維護四組' then 'PRINDA'");
			builder.append(" when k.name='維護八組' then 'PRINDA'");
			builder.append(" when k.name='維護管理部' then 'CYB'");
			// 默認CYB
		//	builder.append(" else null end");
			builder.append(" else 'CYB' end");*/
			// Task #2757 文檔
			builder.append(" case when k.name='EDC管理部' then 'CYB'");
			builder.append(" when k.name='維護五組' then 'PRINDA'");
			builder.append(" when k.name='維護四組' then 'PRINDA'");
			builder.append(" when k.name='維護八組' then 'PRINDA'");
			builder.append(" when k.name='維護管理部' then 'CYB'");
			builder.append(" when k.name='維護六組' then 'FIS'");
			builder.append(" else 'CYB' end");
			
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_ID.getValue());

			// 維護部門不設
		//	sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_ID.getValue());
			//	Bug #2595
/*			builder.delete(0, builder.length());
			builder.append(" case when k.name ='維護管理部' then 'CUSTOMER_SERVICE'");
			builder.append(" when (k.name not in('EDC管理部', '維護五組', '維護四組', '維護八組')) then 'CUSTOMER_SERVICE'");
			builder.append(" else k.name end");*/
			
			// Task #2611
/*			builder.delete(0, builder.length());
			builder.append(" case when a.status='StatusEnd' then 'CUSTOMER_SERVICE'");
			builder.append(" when a.status='StatusComplete' then k.name");
			builder.append(" else null end");*/
			
			// Task #2697
			builder.delete(0, builder.length());
/*			builder.append(" case when a.status='StatusEnd' then '維護管理部'");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is not null then k.name");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is null and v.identifier is not null then k.name");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is null and v.identifier is null then '維護管理部'");
			builder.append(" else null end");*/
/*			builder.append(" case when a.status='StatusEnd' then 'CUSTOMER_SERVICE'");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is not null then k.name");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is null and v.identifier is not null then k.name");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is null and v.identifier is null then 'CUSTOMER_SERVICE'");
			builder.append(" else null end");*/
			builder.append(" case when a.status='StatusEnd' then 'CUSTOMER_SERVICE'");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is not null then (case when isnull(k.name,'') = '維護管理部' then 'CUSTOMER_SERVICE' else k.name end) ");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is null and v.identifier is not null then (case when isnull(k.name,'') = '維護管理部' then 'CUSTOMER_SERVICE' else k.name end) ");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is null and v.identifier is null then 'CUSTOMER_SERVICE'");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_ID.getValue());
			
		//	sqlStatement.addSelectClause("k.name", SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_ID.getValue());
		//	sqlStatement.addSelectClause("case when k.name ='維護管理部' then 'CUSTOMER_SERVICE' else k.name end", SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_ID.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_PROJECT.getValue());

			builder.delete(0, builder.length());
			builder.append(" case when a.requestType_ID in (370,372,376) then e.VALUE");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PROJECT_CODE.getValue());
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PROJECT_NAME.getValue());

			builder.delete(0, builder.length());
			builder.append(" case when a.caseType in (1,2) then 'APPOINTMENT'");
			builder.append(" when a.caseType in (0) and importanceType_ID=501 then 'COMMON'");
			builder.append(" when a.caseType in (0) and importanceType_ID=502 then 'FAST'");
			builder.append(" when a.caseType in (0) and importanceType_ID=503 then 'EXTRA'");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE.getValue());

			sqlStatement.addSelectClause("a.appointDate", SrmCaseHandleInfoDTO.ATTRIBUTE.EXPECTED_COMPLETION_DATE.getValue());
			// 直接查詢特店主鍵 
		//	sqlStatement.addSelectClause("g.MARCHANT_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("g.identifier", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("c.tid2", SrmCaseHandleInfoDTO.ATTRIBUTE.OLD_MERCHANT_CODE.getValue());
			// 直接查詢特店表頭主鍵 
		//	sqlStatement.addSelectClause("f.LOCATION_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_HEADER_ID.getValue());
			sqlStatement.addSelectClause("f.identifier", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_HEADER_ID.getValue());

			builder.delete(0, builder.length());
			builder.append(" case when len(replace(a.INSTALL_ADDRESS,' ','')) < 1 then replace(a.INSTALL_ADDRESS,' ','') ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='臺北市' then '台北市'");
		//	builder.append(" case when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='臺北市' then '台北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='世貿' then '台北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='北市' then '台北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='台北' then '台北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='臺北' then '台北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='圓山' then '台北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='德安資' then '台北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='(市府' then '台北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='墨攻新' then '台北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='新北投' then '台北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='南港展' then '台北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='105' then '台北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='103' then '台北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='羅斯福' then '台北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='晶華酒' then '台北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='改:台' then '台北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='(總公' then '台北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='臺中' then '台中市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='中市' then '台中市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='大台中' then '台中市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='台中' then '台中市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='403' then '台中市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='407' then '台中市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='401' then '台中市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='411' then '台中市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,5)='404臺中' then '台中市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='桃園' then '桃園市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='(南崁' then '桃園市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='324' then '桃園市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='淡水區' then '新北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='台北縣' then '新北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='臺北縣' then '新北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='五股區' then '新北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='中和市' then '新北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='(新埔' then '新北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='(頂溪' then '新北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='新北' then '新北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='新店' then '新北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='220' then '新北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='南投市' then '南投縣'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='草屯' then '南投縣'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='台南' then '台南市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='臺南' then '台南市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='民族一' then '高雄市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='高雄' then '高雄市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='高市' then '高雄市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='813' then '高雄市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='806' then '高雄市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='特賣裝' then '高雄市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,5)='404高雄' then '高雄市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='新竹線' then '新竹縣'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='新竹竹' then '新竹縣'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='竹北市' then '新竹縣'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='資訊室' then '新竹市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='林啟宏' then '新竹市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='300' then '新竹市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='彰化' then '彰化縣'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='臺東' then '台東縣'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='台東' then '台東縣'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='宜蘭' then '宜蘭縣'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='屏東' then '屏東縣'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='雲林' then '雲林縣'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='花蓮' then '花蓮縣'");
			builder.append(" else substring(replace(a.INSTALL_ADDRESS,' ',''),1,3) end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS_LOCATION.getValue());
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS_LOCATION.getValue());

			builder.delete(0, builder.length());
			builder.append(" case when len(replace(a.INSTALL_ADDRESS,' ','')) < = 0 then replace(a.INSTALL_ADDRESS,' ','')");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3) in");
		//	builder.append(" case when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3) in");
			builder.append(" ('淡水區','南投市','世貿旅','(南崁','北市中','世貿一','臺北世','台北世',");
			builder.append(" '北市復','淡水區','台北華','晶華酒','世貿三','世貿3','台中世','北市大',");
			builder.append(" '中市太','103','高雄展','世貿旅','北市民','台北松','台北大','新北新',");
			builder.append(" '五股區','中和市','台中區','高市鼓','高市鳳','民族一','台東市','民族一',");
			builder.append(" '桃園二','彰化市','花蓮市','德安資','宜蘭市','台北南','300','404','屏東市',");
			builder.append(" '羅斯福','高雄區','台中西','特賣裝','台南南','墨攻新','台北中','台北長',");
			builder.append(" '高雄苓','台中旅','圓山花','台北信','高市左','北市南','臺南永','台中烏',");
			builder.append(" '草屯鎮','北市中','新店中','高雄鳳','411','南投市','新竹竹','220','403',");
			builder.append(" '(頂溪','(南崁','(市府','(新埔','總公司','(總公','雲林鎮','新北投','401',");
			builder.append(" '世貿1','改:台','高雄巨','324','資訊室','林啟宏','407','圓山爭','南港展',");
			builder.append(" '813','806','105','台中朝','草屯分','大台中','北市信','竹北市') then replace(a.INSTALL_ADDRESS,' ','')");
			builder.append(" else substring(replace(a.INSTALL_ADDRESS,' ',''),4,len(replace(a.INSTALL_ADDRESS,' ',''))-3) end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue());
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS.getValue());

			sqlStatement.addSelectClause("f.LIAISON_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT.getValue());
			sqlStatement.addSelectClause("f.LIAISON_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER.getValue());
			
			sqlStatement.addSelectClause("f.LIAISON_TEL1", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_PHONE.getValue());
			sqlStatement.addSelectClause("f.LIAISON_TEL1", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_PHONE.getValue());
			
			sqlStatement.addSelectClause("'N'", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_BUSSINESS_ADDRESS.getValue());
			sqlStatement.addSelectClause("'N'", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_IS_BUSSINESS_ADDRESS.getValue());
			
			sqlStatement.addSelectClause("'Y'", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_BUSSINESS_CONTACT.getValue());
			sqlStatement.addSelectClause("'Y'", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_IS_BUSSINESS_CONTACT.getValue());
			
			sqlStatement.addSelectClause("'Y'", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_BUSSINESS_CONTACT_PHONE.getValue());
			sqlStatement.addSelectClause("'Y'", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_IS_BUSSINESS_CONTACT_PHONE.getValue());
			
			sqlStatement.addSelectClause("h.VALUE", SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE.getValue());
			sqlStatement.addSelectClause("a.posCode", SrmCaseHandleInfoDTO.ATTRIBUTE.SOFTWARE_VERSION.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.BUILT_IN_FEATURE.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.MULTI_MODULE.getValue());
			sqlStatement.addSelectClause("a.R50", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION.getValue());

			builder.delete(0, builder.length());
			builder.append(" case when a.ECR=1 then 'haveEcrLine'");
			builder.append(" else 'noEcrLine' end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.ECR_CONNECTION.getValue());

			sqlStatement.addSelectClause("a.PINPAD", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION2.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.CONNECTION_TYPE.getValue());
			sqlStatement.addSelectClause("a.S200", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION3.getValue());
			sqlStatement.addSelectClause("c.edcIp", SrmCaseHandleInfoDTO.ATTRIBUTE.LOCALHOST_IP.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.NET_VENDOR_ID.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.GATEWAY.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.NETMASK.getValue());
			sqlStatement.addSelectClause("a.problemDecription", SrmCaseHandleInfoDTO.ATTRIBUTE.DESCRIPTION.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATED_DESCRIPTION.getValue());
			sqlStatement.addSelectClause("isnull(i.on_site,0)", SrmCaseHandleInfoDTO.ATTRIBUTE.ATTENDANCE_TIMES.getValue());
			sqlStatement.addSelectClause("a.quicklyTotalCount", SrmCaseHandleInfoDTO.ATTRIBUTE.REPAIR_TIMES.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.REPAIR_REASON.getValue());

			builder.delete(0, builder.length());
			builder.append(" case when a.requestType_ID=369 and isnull(i.on_site,0) > 0 then 'ARRIVE_UNINSTALL'");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALL_TYPE.getValue());

		//	sqlStatement.addSelectClause("m.processTime", SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_FINISH_DATE.getValue());
			// Task #2602
			sqlStatement.addSelectClause("case when m.processTime is not null then m.processTime else a.updated end", SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_FINISH_DATE.getValue());
			
			sqlStatement.addSelectClause("a.acceptableResponseTime", SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_RESPONSE_DATE.getValue());
			sqlStatement.addSelectClause("a.acceptableArriveTime", SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_ARRIVE_DATE.getValue());
			sqlStatement.addSelectClause("a.acceptableFinishTime", SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_FINISH_DATE.getValue());

			builder.delete(0, builder.length());
//			builder.append(" case when a.status='StatusCancel' then 'voidCase'");
//			builder.append(" when a.status='StatusCancelByUser' then 'voidCase'");
//			builder.append(" when a.status='StatusComplete' then 'closed'");
//			builder.append(" when a.status='StatusEnd' and l.identifier is null then 'immediatelyClosing'");
//			builder.append(" when a.status='StatusEnd' and l.identifier is not null then 'onlineExclusion'");
//			builder.append(" else null end");
		//	Voided
			builder.append(" case when a.status='StatusComplete' then 'Closed'");
		//	Task #2604
		//	builder.append(" when a.status='StatusEnd' and l.identifier is null then 'ImmediateClose'");
		//	builder.append(" when a.status='StatusEnd' and l.identifier is not null then 'Closed'");
			builder.append(" when a.status='StatusEnd' then 'ImmediateClose'");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS.getValue());

		//	sqlStatement.addSelectClause("k.name", SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_DEPT_ID.getValue());
		//	sqlStatement.addSelectClause("case when k.name ='維護管理部' then 'CUSTOMER_SERVICE' else k.name end", SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_DEPT_ID.getValue());
		//	Bug #2595
/*			builder.delete(0, builder.length());
			builder.append(" case when k.name ='維護管理部' then 'CUSTOMER_SERVICE'");
			builder.append(" when (k.name not in('EDC管理部', '維護五組', '維護四組', '維護八組')) then 'CUSTOMER_SERVICE'");
			builder.append(" else k.name end");*/
			// Task #2611
/*			builder.append(" case when a.status='StatusEnd' then 'CUSTOMER_SERVICE'");
			builder.append(" when a.status='StatusComplete' then k.name");
			builder.append(" else null end");*/
			
			// Task #2697
			builder.delete(0, builder.length());
/*			builder.append(" case when a.status='StatusEnd' then '維護管理部'");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is not null then k.name");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is null and v.identifier is not null then k.name");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is null and v.identifier is null then '維護管理部'");
			builder.append(" else null end");*/
/*			builder.append(" case when a.status='StatusEnd' then 'CUSTOMER_SERVICE'");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is not null then k.name");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is null and v.identifier is not null then k.name");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is null and v.identifier is null then 'CUSTOMER_SERVICE'");
			builder.append(" else null end");*/
			builder.append(" case when a.status='StatusEnd' then 'CUSTOMER_SERVICE'");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is not null then (case when isnull(k.name,'') = '維護管理部' then 'CUSTOMER_SERVICE' else k.name end) ");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is null and v.identifier is not null then (case when isnull(k.name,'') = '維護管理部' then 'CUSTOMER_SERVICE' else k.name end) ");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is null and v.identifier is null then 'CUSTOMER_SERVICE'");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_DEPT_ID.getValue());
			
/*			builder.delete(0, builder.length());
			builder.append(" case when a.status='StatusComplete' then n.assignedEngineerIdentifier");
			builder.append(" when a.status='StatusEnd' then a.createEmployeeId");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_PROCESS_USER.getValue());*/
			// Task #2611
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_PROCESS_USER.getValue());

			builder.delete(0, builder.length());
/*			builder.append(" case when a.status='StatusComplete' then o.name ");
			builder.append(" when a.status='StatusEnd' then  p.name");
			builder.append(" else null end");*/
			
			// Task #2611
/*			builder.append(" case when a.status='StatusEnd' then y1.serviceEmployeeName");
			builder.append(" when a.status='StatusComplete' then k1.MODIFYINFO_MODIFY_USER_ID");
			builder.append(" else null end");*/
			// Task #2697
			builder.append(" case when a.status='StatusEnd' then y1.serviceEmployeeName");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is not null then k1.MODIFYINFO_MODIFY_USER_ID");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is null and v.identifier is not null then k1.MODIFYINFO_MODIFY_USER_ID");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is null and v.identifier is null then y1.serviceEmployeeName");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_PROCESS_USERNAME.getValue());

			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_USER.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_USER_NAME.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_DATE.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_USER.getValue());
			sqlStatement.addSelectClause("r.MODIFYINFO_MODIFY_USER_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_USER_NAME.getValue());
			sqlStatement.addSelectClause("r.MODIFYINFO_MODIFY_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_DATE.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_USER.getValue());
			sqlStatement.addSelectClause("t.MODIFYINFO_MODIFY_USER_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_USER_NAME.getValue());
			sqlStatement.addSelectClause("t.MODIFYINFO_MODIFY_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_DATE.getValue());
			
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_USER.getValue());
			sqlStatement.addSelectClause("v.MODIFYINFO_MODIFY_USER_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_USER_NAME.getValue());
			sqlStatement.addSelectClause("a.realDoneDay", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.ANALYZE_USER.getValue());
			sqlStatement.addSelectClause("x.MODIFYINFO_MODIFY_USER_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.ANALYZE_USER_NAME.getValue());
			sqlStatement.addSelectClause("x.MODIFYINFO_MODIFY_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.ANALYZE_DATE.getValue());
			
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.CLOSE_USER.getValue());
			sqlStatement.addSelectClause("z.serviceEmployeeName", SrmCaseHandleInfoDTO.ATTRIBUTE.CLOSE_USER_NAME.getValue());
			sqlStatement.addSelectClause("z.processTime", SrmCaseHandleInfoDTO.ATTRIBUTE.CLOSE_DATE.getValue());
			
			sqlStatement.addSelectClause("a.createEmployeeId", SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			sqlStatement.addSelectClause("p.name", SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("a.created", SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sqlStatement.addSelectClause("null", BaseParameterItemDefDTO.ATTRIBUTE.UPDATED_BY_ID.getValue());
			sqlStatement.addSelectClause("z.serviceEmployeeName", SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("z.processTime", SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_DEPT_ID.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_USER.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_TMS.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_REASON_CODE.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_REASON.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_REASON.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_SOLUTION.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSIBITY.getValue());
			
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_WARNNING.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_WARNNING.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_WARNNING.getValue());

			builder.delete(0, builder.length());
			builder.append(" case when a.requestType_ID=367 then a.realDoneDay");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_DATE.getValue());

			builder.delete(0, builder.length());
			builder.append(" case when a.requestType_ID=369 then a.realDoneDay");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALLED_DATE.getValue());
			
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_ENABLE_DATE.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_DISABLE_DATE.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATE_ITEM.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_INVOICE.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_QUICK_PASS.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.SAME_INSTALLED.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.TMS_PARAM_DESC.getValue());
			
			sqlStatement.addSelectClause("'Y'", SrmCaseHandleInfoDTO.ATTRIBUTE.FOMS_CASE.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_FIRST_INSTALLED.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.LOGO_STYLE.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_OPEN_ENCRYPT.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_PAY_PLATFORM.getValue());
			
			sqlStatement.addSelectClause("a.unionPayFlag", SrmCaseHandleInfoDTO.ATTRIBUTE.UNION_PAY_FLAG.getValue());
			sqlStatement.addSelectClause("a.smartpay", SrmCaseHandleInfoDTO.ATTRIBUTE.SMART_PAY_FLAG.getValue());
			sqlStatement.addSelectClause("a.SMID", SrmCaseHandleInfoDTO.ATTRIBUTE.SMID.getValue());
			sqlStatement.addSelectClause("a.STID", SrmCaseHandleInfoDTO.ATTRIBUTE.STID.getValue());
			sqlStatement.addSelectClause("c.tid1", SrmCaseHandleInfoDTO.ATTRIBUTE.TID.getValue());
			// Task #2865 一般交易的MID，是案件的MID
			sqlStatement.addSelectClause("g.MARCHANT_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.AE_MID.getValue());
			// Task #2606 增加線上排除
			builder.delete(0, builder.length());
			builder.append(" case when s1.ProcessKey is not null then 'Y'");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.HAS_ONLINE_EXCLUSION.getValue());
			
			builder.delete(0, builder.length());
			builder.append(schema).append(".FOMS_SFM_Request a");
			builder.append(" join ").append(schema).append(".CUM_CORPORATION b ");
			builder.append(" on a.corporationId=b.identifier and");
			builder.append(" b.serviceType=1 and");
			builder.append(" b.compShortCode<>'TSB-EDC'");
			builder.append(" join ").append(schema).append(".FOMS_EDC_DEVICE c on a.identifier=c.requestId");
			builder.append(" left join ").append(schema).append(".CCM_CORPORATIONCONTRACT d on a.customerContractId=d.identifier"); 
			builder.append(" left join ").append(schema).append(".CORE_OPTION e on a.requestType_ID=e.ID");
			builder.append(" left join ").append(schema).append(".CUM_Title f on c.merchantIdentifier=f.identifier");
			builder.append(" left join ").append(schema).append(".CUM_MERCHANT g on f.MARCHANT_ID=g.identifier ");
			builder.append(" left join ").append(schema).append(".CORE_OPTION h on a.deviceType_ID=h.ID");
			builder.append(" left join (");
			builder.append(" select bb.requestId as CaseKey,count(aa.identifier) as on_site");
			builder.append(" from ").append(schema).append(".FOMS_EDC_EdcWorkInventoryFlowLog aa");
			builder.append(" join ").append(schema).append(".FOMS_EDC_EdcWorkInventoryFlow bb on aa.EdcWorkInventoryFlow_Identifier=bb.identifier");
			builder.append(" where aa.processtype='on_site'");
			builder.append(" group by bb.requestId) i on a.identifier=i.CaseKey");
			
			builder.append(" left join (select requestId as CaseKey,max(identifier) as ProcessKey");
			builder.append(" from ").append(schema).append(".FOMS_SFM_RequestProcessLog");
			builder.append(" where processType='dispatchEdc' or ");
			builder.append(" processType='edcDispatch' or ");
			builder.append(" processType='transger_edcAgent'");
			builder.append(" group by requestId) jj on a.identifier=jj.CaseKey");
			
			builder.append(" left join ").append(schema).append(".FOMS_SFM_RequestProcessLog j on jj.ProcessKey=j.identifier");
			builder.append(" left join ").append(schema).append(".ORG_PARTY k on j.assignedVendorId=k.identifier"); 
			
			builder.append(" left join (select aa.requestId as CaseKey,max(aa.identifier) as ProcessKey");
			builder.append(" from ").append(schema).append(".FOMS_EDC_EdcWorkInventory aa");
			builder.append(" join ").append(schema).append(".FOMS_EDC_EdcWorkInventoryLog bb");
			builder.append(" on aa.identifier=bb.EdcWorkInventory_Identifier and ");
			builder.append(" bb.role='1'");
			builder.append(" group by aa.requestId) ll on a.identifier=ll.CaseKey");
			builder.append(" left join ").append(schema).append(".FOMS_EDC_EdcWorkInventory l on ll.ProcessKey=l.identifier");
			builder.append(" left join (select requestId as CaseKey,min(identifier) as ProcessKey");
			builder.append(" from ").append(schema).append(".FOMS_SFM_RequestProcessLog");
			builder.append(" where processType='edcDispatch' or");
			builder.append(" processType='dispatchEdc'");
			builder.append(" group by requestId) as mm on a.identifier=mm.CaseKey");
			builder.append(" left join ").append(schema).append(".FOMS_SFM_RequestProcessLog m on mm.ProcessKey=m.identifier ");
			builder.append(" left join (select requestId as CaseKey,max(identifier) as FlowKey");
			builder.append(" from ").append(schema).append(".FOMS_EDC_EdcWorkInventoryFlow");
			builder.append(" group by requestId) nn on a.identifier=nn.CaseKey");
			builder.append(" left join ").append(schema).append(".FOMS_EDC_EdcWorkInventoryFlow n on nn.FlowKey=n.identifier");
			builder.append(" left join ").append(schema).append(".ORG_PARTY o on n.assignedEngineerIdentifier=o.identifier");
			builder.append(" left join ").append(schema).append(".ORG_PARTY p on a.createEmployeeId=p.identifier");
			builder.append(" left join (select bb.requestId as CaseKey,max(aa.identifier) as ProcessKey");
			builder.append(" from ").append(schema).append(".FOMS_EDC_EdcWorkInventoryFlowLog aa");
			builder.append(" join ").append(schema).append(".FOMS_EDC_EdcWorkInventoryFlow bb on aa.EdcWorkInventoryFlow_Identifier=bb.identifier");
			builder.append(" where aa.processtype in ('accept','agent_response')");
			builder.append(" group by bb.requestId) q on a.identifier=q.CaseKey ");
			builder.append(" left join ").append(schema).append(".FOMS_EDC_EdcWorkInventoryFlowLog r on q.ProcessKey=r.identifier");
			builder.append(" left join (select bb.requestId as CaseKey,max(aa.identifier) as ProcessKey");
			builder.append(" from ").append(schema).append(".FOMS_EDC_EdcWorkInventoryFlowLog aa");
			builder.append(" join ").append(schema).append(".FOMS_EDC_EdcWorkInventoryFlow bb on aa.EdcWorkInventoryFlow_Identifier=bb.identifier");
			builder.append(" where aa.processtype in ('on_site')");   
			builder.append(" group by bb.requestId) s on a.identifier=s.CaseKey ");
			builder.append(" left join ").append(schema).append(".FOMS_EDC_EdcWorkInventoryFlowLog t on s.ProcessKey=t.identifier");
			builder.append(" left join (select bb.requestId as CaseKey,max(aa.identifier) as ProcessKey");
			builder.append(" from ").append(schema).append(".FOMS_EDC_EdcWorkInventoryFlowLog aa");
			builder.append(" join ").append(schema).append(".FOMS_EDC_EdcWorkInventoryFlow bb on aa.EdcWorkInventoryFlow_Identifier=bb.identifier");
			builder.append(" where aa.processtype in ('complete')");
			builder.append(" group by bb.requestId) u on a.identifier=u.CaseKey");
			builder.append(" left join ").append(schema).append(".FOMS_EDC_EdcWorkInventoryFlowLog v on u.ProcessKey=v.identifier");
			builder.append(" left join (select bb.requestId as CaseKey,max(aa.identifier) as ProcessKey");
			builder.append(" from ").append(schema).append(".FOMS_EDC_EdcWorkInventoryFlowLog aa");
			builder.append(" join ").append(schema).append(".FOMS_EDC_EdcWorkInventoryFlow bb on aa.EdcWorkInventoryFlow_Identifier=bb.identifier");
			builder.append(" where aa.processtype in ('censor')");
			builder.append(" group by bb.requestId) w on a.identifier=w.CaseKey");
			builder.append(" left join ").append(schema).append(".FOMS_EDC_EdcWorkInventoryFlowLog x on w.ProcessKey=x.identifier");
		
			builder.append(" left join (select aa.requestId as CaseKey,max(aa.identifier) as ProcessKey");
			builder.append(" from ").append(schema).append(".FOMS_SFM_RequestProcessLog aa");
			// Task #2628
		//	builder.append(" where aa.processtype in ('qualified','solve_on_phone','coordinate')");
			builder.append(" where aa.processtype in  ('qualified','solve_on_phone','coordinate','edcEnd','finish')");
			builder.append(" group by aa.requestId) y on a.identifier=y.CaseKey");
			builder.append(" left join  ").append(schema).append(".FOMS_SFM_RequestProcessLog z on y.ProcessKey=z.identifier");
			
			// Task #2606 增加線上排除
			builder.append(" left join (select bb.requestId as CaseKey,max(aa.identifier) as ProcessKey");
			builder.append(" from ").append(schema).append(".FOMS_EDC_EdcWorkInventoryFlowLog aa");
			builder.append(" join ").append(schema).append(".FOMS_EDC_EdcWorkInventoryFlow bb on aa.EdcWorkInventoryFlow_Identifier=bb.identifier");
			builder.append(" where aa.processtype in ('solveOnLine') ");
			builder.append(" group by bb.requestId) s1 on a.identifier=s1.CaseKey");
			
			// Task #2611
/*			builder.append(" left join (select requestId as CaseKey,max(identifier) as flowKey");
			builder.append(" from ").append(schema).append(".FOMS_SFM_RequestProcessLog");
			builder.append(" group by requestId) as z1 on a.identifier=z1.CaseKey");
			builder.append(" left join ").append(schema).append(".FOMS_SFM_RequestProcessLog y1 on z1.flowKey=y1.identifier");*/
			
			// Task #2697
			builder.append(" left join (select requestId as CaseKey,max(identifier) as flowKey");
			builder.append(" from ").append(schema).append(".FOMS_SFM_RequestProcessLog");
			builder.append(" where processtype in ('solve_on_phone','coordinate','edcEnd','finish')");
			builder.append(" group by requestId) as z1 on a.identifier=z1.CaseKey");
			builder.append(" left join ").append(schema).append(".FOMS_SFM_RequestProcessLog y1 on z1.flowKey=y1.identifier");
			
			builder.append(" left join (select bb.requestId as CaseKey,max(aa.identifier) as ProcessKey");
			builder.append(" from ").append(schema).append(".FOMS_EDC_EdcWorkInventoryFlowLog aa");
			builder.append(" join ").append(schema).append(".FOMS_EDC_EdcWorkInventoryFlow bb on aa.EdcWorkInventoryFlow_Identifier=bb.identifier");
			builder.append(" where aa.processtype in ('censor') ");
			builder.append(" group by bb.requestId) w1 on a.identifier=w1.CaseKey");
			builder.append(" left join ").append(schema).append(".FOMS_EDC_EdcWorkInventoryFlowLog k1 on w1.ProcessKey=k1.identifier");
			
			sqlStatement.addFromExpression(builder.toString());

			sqlStatement.addWhereClause("a.status in ('StatusComplete','StatusEnd')");
			
			// Task #3066 只轉入特定案件編號案件
		//	sqlStatement.addWhereClause("a.requestNo in ('HS1712206527','HS1712206528')");
			
			// Task #3091 根據案件編號轉入案件
			if(StringUtils.hasText(transferCaseId)){
				sqlStatement.addWhereClause("a.requestNo in (:transferCaseId)");
			}

			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			
			// Task #3091 根據案件編號轉入案件
			if(StringUtils.hasText(transferCaseId)){
				sqlQueryBean.setParameter("transferCaseId", StringUtils.toList(transferCaseId, IAtomsConstants.MARK_SEPARATOR));
			}
			
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseHandleInfoDTO.class);
			LOGGER.debug("listCaseHandleInfo()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return result;
		} catch (Exception e) {
			LOGGER.error("listCaseHandleInfo()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#listCaseFiles()
	 */
	@Override
	public List<SrmCaseAttFileDTO> listCaseFiles(String transferCaseId) throws DataAccessException {
		List<SrmCaseAttFileDTO> result = null;
		//得到schema
		String schema = this.getSchema("iatoms-foms");
		SqlStatement sqlStatement = new SqlStatement();
		StringBuilder builder = new StringBuilder();
		try {
			sqlStatement.addSelectClause("a.requestNo", SrmCaseAttFileDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("b.appendFileName", SrmCaseAttFileDTO.ATTRIBUTE.FILE_NAME.getValue());
		//	sqlStatement.addSelectClause("b.appendFileExplanation", SrmCaseAttFileDTO.ATTRIBUTE.DESCRIPTION.getValue());
			sqlStatement.addSelectClause("b.MODIFYINFO_ADD_USER_ID", SrmCaseAttFileDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			sqlStatement.addSelectClause("c.name", SrmCaseAttFileDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("b.MODIFYINFO_ADD_DATE", SrmCaseAttFileDTO.ATTRIBUTE.CREATED_DATE.getValue());
			
			builder.append(schema).append(".FOMS_SFM_Request a");
			builder.append(" join ").append(schema).append(".CUM_CORPORATION e ");
			builder.append(" on a.corporationId=e.identifier and");
			builder.append(" e.serviceType=1 and ");
			builder.append(" e.compShortCode<>'TSB-EDC'");
			builder.append(" join ").append(schema).append(".AppendFile b on a.identifier=b.requestId");
			builder.append(" left join ").append(schema).append(".ORG_PARTY c on b.MODIFYINFO_ADD_USER_ID=c.identifier");
			builder.append(" left join ").append(schema).append(".ORG_PARTY d on b.MODIFYINFO_MODIFY_USER_ID=d.identifier");
			sqlStatement.addFromExpression(builder.toString());
			
			sqlStatement.addWhereClause("a.status in ('StatusComplete','StatusEnd') and b.deleted=0");
			
			
			// Task #3066 只轉入特定案件編號案件
		//	sqlStatement.addWhereClause("a.requestNo in ('HS1712206527','HS1712206528')");
			
			// Task #3091 根據案件編號轉入案件
			if(StringUtils.hasText(transferCaseId)){
				sqlStatement.addWhereClause("a.requestNo in (:transferCaseId)");
			}
			
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			
			// Task #3091 根據案件編號轉入案件
			if(StringUtils.hasText(transferCaseId)){
				sqlQueryBean.setParameter("transferCaseId", StringUtils.toList(transferCaseId, IAtomsConstants.MARK_SEPARATOR));
			}
						
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseAttFileDTO.class);
			LOGGER.debug("listCaseFiles()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return result;
		} catch (Exception e) {
			LOGGER.error("listCaseFiles()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#listCaseTransactions()
	 */
	@Override
	public List<SrmCaseTransactionDTO> listCaseTransactions(String transferCaseId)throws DataAccessException {
		List<SrmCaseTransactionDTO> result = null;
		//得到schema
		String schema = this.getSchema("iatoms-foms");
		SqlQueryBean sqlQueryBean = new SqlQueryBean();
		try {
			sqlQueryBean.append("select a.requestNo as caseId,")
			
				// 案件動作
			//	.append("b.processType as actionId,")
				.append("(case when b.processType = '' then ''")
				.append(" when b.processType is null then ''")
				.append(" else ('foms_' +  b.processType) end) as actionId,")
				// 案件狀態
			//	.append("b.status as caseStatus,")
				.append("(case when b.status = '' then ''")
				.append(" when b.status is null then ''")
				.append(" else ('foms_' +  b.status) end) as caseStatus,")
				
				// 派工單位
				.append("(case when c.name ='維護管理部' then 'CUSTOMER_SERVICE' ")
				.append(" when c.name ='0' then 'WAREHOUSE_KEEPER' ")
				.append(" when c.name ='1' then 'QA' ")
				.append(" when c.name ='2' then 'TMS' ")
				.append(" when c.name ='3' then 'LOGISTICS' ")
				.append(" else c.name end) as deptCode,")
			//	.append("c.name as deptCode,")
				.append("null as dealById,")
				.append("b.serviceEmployeeName as dealByName,")
				// Task #2627
//				.append("b.processTime as dealDate,")
				.append("(case when a.status='StatusEnd' and b.processType in ('solve_on_phone','edcEnd','coordinate','finish') and a.realDoneDay is not null then a.realDoneDay")
				.append(" else b.processTime end) as dealDate,")
				
				// Task #2863
			//	.append("a.requestNo  + '\n' + convert(varchar,isnull(b.processDescription,'')) as description,")
				.append("a.requestNo + '\n' + convert(varchar(5000),isnull(b.processDescription,'')) as description,")
				
				.append("null as caseStage,")
				.append("null as caseStageName,")
				.append("null as nextCaseStage,")
				.append("null as nextCaseStageName,")
				.append("null as delayTime,")
				.append("null as checkResult,")
				.append("null as problemReasonCode,")
				.append("null as problemReason,")
				.append("null as problemSolutionCode,")
				.append("null as problemSolution,")
				.append("null as responsibity,")
				.append("null as caseType,")
				.append("null as expectedCompletionDate,")
				.append("null as createdById,")
				.append("b.serviceEmployeeName as createdByName,")
				.append("b.processTime as createdDate,")
				.append("null as updateItem")
				.append(" from ").append(schema).append(".FOMS_SFM_Request a")
				.append(" join ").append(schema).append(".CUM_CORPORATION co ")
				.append(" on a.corporationId=co.identifier and")
				.append(" co.serviceType=1 and ")
				.append(" co.compShortCode<>'TSB-EDC'")
				.append(" join ").append(schema).append(".FOMS_SFM_RequestProcessLog b on a.identifier=b.requestId")
				.append(" left join ").append(schema).append(".ORG_PARTY c on b.assignedVendorId=c.identifier")
				.append(" left join ").append(schema).append(".ORG_PARTY d on b.assignedEngineerId=d.identifier")
				.append(" where a.status in ('StatusComplete','StatusEnd') ")
				
				// Task #3066 只轉入特定案件編號案件
			//	.append(" and a.requestNo in ('HS1712206527','HS1712206528') ")
				
				// Task #3091 根據案件編號轉入案件
				.append(StringUtils.hasText(transferCaseId) ? " and a.requestNo in (:transferCaseId) " : "")
				
				.append(" union all")
				
				.append(" select a.requestNo as caseId,")
				
				// 案件動作
			//	.append("c.processType as actionId,")
				.append("(case when c.processType = '' then ''")
				.append(" when c.processType is null then ''")
				.append(" else ('foms_' +  c.processType) end) as actionId,")
				// 案件狀態
			//	.append("c.processStatus as caseStatus,")
				.append("(case when c.processStatus = '' then ''")
				.append(" when c.processStatus is null then ''")
				.append(" else ('foms_' +  c.processStatus) end) as caseStatus,")
				
				// 派工單位
				.append("(case when c.role ='維護管理部' then 'CUSTOMER_SERVICE' ")
				.append(" when c.role ='0' then 'WAREHOUSE_KEEPER' ")
				.append(" when c.role ='1' then 'QA' ")
				.append(" when c.role ='2' then 'TMS' ")
				.append(" when c.role ='3' then 'LOGISTICS' ")
				.append(" else c.role end) as deptCode,")
			//	.append("c.role as deptCode,")
				.append("null as dealById,")
				.append("c.MODIFYINFO_MODIFY_USER_ID as dealByName,")
				.append("c.MODIFYINFO_MODIFY_DATE as dealDate,")
				
				// Task #2863
			//	.append("b.edcWorkInventoryNo + '\n' + convert(varchar,isnull(c.processDescription,'')) as description,")
				.append("b.edcWorkInventoryNo + '\n' + convert(varchar(5000),isnull(c.processDescription,'')) as description,")
				
				.append("null as caseStage,")
				.append("null as caseStageName,")
				.append("null as nextCaseStage,")
				.append("null as nextCaseStageName,")
				.append("null as delayTime,")
				.append("null as checkResult,")
				.append("null as problemReasonCode,")
				.append("null as problemReason,")
				.append("null as problemSolutionCode,")
				.append("null as problemSolution,")
				.append("null as responsibity,")
				.append("null as caseType,")
				.append("null as expectedCompletionDate,")
				.append("null as createdById,")
				.append("c.MODIFYINFO_MODIFY_USER_ID as createdByName,")
				.append("c.MODIFYINFO_MODIFY_DATE as createdDate,")
				.append("null as updateItem")
				.append(" from ").append(schema).append(".FOMS_SFM_Request a")
				.append(" join ").append(schema).append(".CUM_CORPORATION co ")
				.append(" on a.corporationId=co.identifier and")
				.append(" co.serviceType=1 and ")
				.append(" co.compShortCode<>'TSB-EDC'")
				.append(" join ").append(schema).append(".FOMS_EDC_EdcWorkInventory b on a.identifier=b.requestId")
				.append(" join ").append(schema).append(".FOMS_EDC_EdcWorkInventoryLog c on b.identifier=c.EdcWorkInventory_Identifier")
				.append(" left join ").append(schema).append(".ORG_PARTY d on c.serviceEmployeeName=d.identifier")
				.append(" where a.status in ('StatusComplete','StatusEnd') ")
				
				// Task #3066 只轉入特定案件編號案件
			//	.append(" and a.requestNo in ('HS1712206527','HS1712206528') ")
				
				// Task #3091 根據案件編號轉入案件
				.append(StringUtils.hasText(transferCaseId) ? " and a.requestNo in (:transferCaseId) " : "")
				
				.append(" union all")
				
				.append(" select a.requestNo as caseId,")
				
				// 案件動作
			//	.append("c.processType as actionId,")
				.append("(case when c.processType = '' then ''")
				.append(" when c.processType is null then ''")
				.append(" else ('foms_' +  c.processType) end) as actionId,")
				// 案件狀態
			//	.append("c.processStatus as caseStatus,")
				.append("(case when c.processStatus = '' then ''")
				.append(" when c.processStatus is null then ''")
				.append(" else ('foms_' +  c.processStatus) end) as caseStatus,")
				
				// 派工單位
				.append("(case when d.name ='維護管理部' then 'CUSTOMER_SERVICE' ")
				.append(" when d.name ='0' then 'WAREHOUSE_KEEPER' ")
				.append(" when d.name ='1' then 'QA' ")
				.append(" when d.name ='2' then 'TMS' ")
				.append(" when d.name ='3' then 'LOGISTICS' ")
				.append(" else d.name end) as deptCode,")
			//	.append("d.name as deptCode,")
				.append("null as dealById,")
				.append("c.MODIFYINFO_MODIFY_USER_ID as dealByName,")
				// Task #2627
//				.append("c.MODIFYINFO_MODIFY_DATE as dealDate,")
				.append("(case when a.status='StatusComplete' and c.processType='complete' and a.realDoneDay is not null then a.realDoneDay")
				.append(" else c.MODIFYINFO_MODIFY_DATE end) as dealDate,")
				
				// Task #2863
			//	.append("b.edcWorkInventoryFlowNo + '\n' + convert(varchar,isnull(c.processDescription,'')) as description,")
				.append("b.edcWorkInventoryFlowNo + '\n' + convert(varchar(5000),isnull(c.processDescription,'')) as description,")
				
				.append("null as caseStage,")
				.append("null as caseStageName,")
				.append("null as nextCaseStage,")
				.append("null as nextCaseStageName,")
				.append("null as delayTime,")
				.append("null as checkResult,")
				.append("null as problemReasonCode,")
				.append("null as problemReason,")
				.append("null as problemSolutionCode,")
				.append("null as problemSolution,")
				.append("null as responsibity,")
				.append("null as caseType,")
				.append("null as expectedCompletionDate,")
				.append("null as createdById,")
				.append("c.MODIFYINFO_MODIFY_USER_ID as createdByName,")
				.append("c.MODIFYINFO_MODIFY_DATE as createdDate,")
				.append("null as updateItem ")
				.append(" from ").append(schema).append(".FOMS_SFM_Request a")
				.append(" join ").append(schema).append(".CUM_CORPORATION co ")
				.append(" on a.corporationId=co.identifier and")
				.append(" co.serviceType=1 and  ")
				.append(" co.compShortCode<>'TSB-EDC'")
				.append(" join ").append(schema).append(".FOMS_EDC_EdcWorkInventoryFlow b on a.identifier=b.requestId")
				.append(" join ").append(schema).append(".FOMS_EDC_EdcWorkInventoryFlowLog c on b.identifier=c.EdcWorkInventoryFlow_Identifier")
				.append(" left join ").append(schema).append(".ORG_PARTY d on b.factoryIdentifier=d.identifier")
				// Task #3066 只轉入特定案件編號案件
				.append(" where a.status in ('StatusComplete','StatusEnd')")//;
			
				// Task #3066 只轉入特定案件編號案件
			//	.append(" and a.requestNo in ('HS1712206527','HS1712206528') ");
				
				// Task #3091 根據案件編號轉入案件
				.append(StringUtils.hasText(transferCaseId) ? " and a.requestNo in (:transferCaseId) " : "");
			
				// Task #3091 根據案件編號轉入案件
				if(StringUtils.hasText(transferCaseId)){
					sqlQueryBean.setParameter("transferCaseId", StringUtils.toList(transferCaseId, IAtomsConstants.MARK_SEPARATOR));
				}
			AliasBean aliasBean = new AliasBean(SrmCaseTransactionDTO.class);
			aliasBean.addScalar("caseId", StringType.INSTANCE);
			aliasBean.addScalar("actionId", StringType.INSTANCE);
			aliasBean.addScalar("caseStatus", StringType.INSTANCE);
			aliasBean.addScalar("deptCode", StringType.INSTANCE);
			aliasBean.addScalar("dealById", StringType.INSTANCE);
			aliasBean.addScalar("dealByName", StringType.INSTANCE);
			aliasBean.addScalar("dealDate", TimestampType.INSTANCE);
			aliasBean.addScalar("description", StringType.INSTANCE);
			aliasBean.addScalar("caseStage", StringType.INSTANCE);
			aliasBean.addScalar("caseStageName", StringType.INSTANCE);
			aliasBean.addScalar("nextCaseStage", StringType.INSTANCE);
			aliasBean.addScalar("nextCaseStageName", StringType.INSTANCE);
			aliasBean.addScalar("delayTime", TimestampType.INSTANCE);
			aliasBean.addScalar("checkResult", StringType.INSTANCE);
			aliasBean.addScalar("problemReasonCode", StringType.INSTANCE);
			aliasBean.addScalar("problemReason", StringType.INSTANCE);
			aliasBean.addScalar("problemSolutionCode", StringType.INSTANCE);
			aliasBean.addScalar("problemSolution", StringType.INSTANCE);
			aliasBean.addScalar("responsibity", StringType.INSTANCE);
			aliasBean.addScalar("caseType", StringType.INSTANCE);
			aliasBean.addScalar("expectedCompletionDate", TimestampType.INSTANCE);
			aliasBean.addScalar("createdById", StringType.INSTANCE);
			aliasBean.addScalar("createdByName", StringType.INSTANCE);
			aliasBean.addScalar("createdDate", TimestampType.INSTANCE);
			aliasBean.addScalar("updateItem", StringType.INSTANCE);
			
			LOGGER.debug("listCaseTransactions()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return result;
		} catch (Exception e) {
			LOGGER.error("listCaseTransactions()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#listAdmUser()
	 */
	@Override
	public List<AdmUser> listAdmUser() throws DataAccessException {
		List<AdmUser> result = null;
		//得到schema
		String schema = this.getSchema("iatoms-aims");
		SqlStatement sqlStatement = new SqlStatement();
		StringBuilder builder = new StringBuilder();
		try {
		//	sqlStatement.addSelectClause("a.carrier", AdmUserDTO.ATTRIBUTE.CNAME.getValue());
			builder.append("case when len(a.carrier) > 2 then substring(a.carrier,1,3)");
			builder.append(" else a.carrier end");
			sqlStatement.addSelectClause(builder.toString(), AdmUserDTO.ATTRIBUTE.CNAME.getValue());
			
			sqlStatement.addFromExpression("AIMS." + schema + ".repository a");
			
			builder.delete(0, builder.length());
			builder.append(" a.valid=1 and ");
			builder.append(" a.status_id in (5,8) and");
			builder.append(" a.owner not in ('三川行','其他') and");
			builder.append(" ((a.owner<>'台新銀行') or (a.owner='台新銀行' and a.customer<>'台新銀行'))");
			sqlStatement.addWhereClause(builder.toString());
			
			sqlStatement.setGroupByExpression("a.carrier");
			
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(AdmUser.class);
			LOGGER.debug("listAdmUser()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return result;
		} catch (Exception e) {
			LOGGER.error("listAdmUser()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#listNewCaseHandleInfo()
	 */
	public List<SrmCaseHandleInfoDTO> listCaseNewHandleInfo(boolean isNoAsset)throws DataAccessException {
		List<SrmCaseHandleInfoDTO> result = null;
		//得到schema
		String fomschema = this.getSchema("iatoms-foms");
		String aimschema = this.getSchema("iatoms-aims");
		SqlStatement sqlStatement = new SqlStatement();
		StringBuilder builder = new StringBuilder();
		try {
			sqlStatement.addSelectClause("a.requestNo", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue());

			builder.append(" case when a.requestType_ID=367 then 'INSTALL'");
			builder.append(" when a.requestType_ID=368 then 'MERGE'");
			builder.append(" when a.requestType_ID=369 then 'UNINSTALL'");
			builder.append(" when a.requestType_ID=370 then 'PROJECT'");
			builder.append(" when a.requestType_ID=371 then 'REPAIR'");
			builder.append(" when a.requestType_ID=372 then 'PROJECT'");
			builder.append(" when a.requestType_ID=373 then 'CHECK'");
			builder.append(" when a.requestType_ID=374 then 'UPDATE'");
			builder.append(" when a.requestType_ID=376 then 'PROJECT'");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue());

			sqlStatement.addSelectClause("b.compShortCode", SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue());
			// 合約ID 對應案件客戶的第一筆合約
			
			sqlStatement.addSelectClause("a.CASENUM", SrmCaseHandleInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue());

			builder.delete(0, builder.length());
/*			builder.append(" case when a.status='StatusComplete' then 'ARRIVE_PROCESS'");
			builder.append(" when a.status='StatusEnd' and l.identifier is null then 'NO_DISPATCH'");
			builder.append(" when a.status='StatusEnd' and l.identifier is not null then 'SOFT_DISPATCH'");
			builder.append(" else null end");*/
			
			// Task #2638
			builder.append(" case when a.status='StatusComplete' and s1.ProcessKey is not null and l.identifier is not null then 'SOFT_DISPATCH'");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is not null and l.identifier is null then 'NO_DISPATCH'");
			builder.append(" when a.status='StatusComplete' and s1.ProcessKey is null then 'ARRIVE_PROCESS' ");
			builder.append(" when a.status='StatusEnd' and l.identifier is null then 'NO_DISPATCH'");
			builder.append(" when a.status='StatusEnd' and l.identifier is not null then 'SOFT_DISPATCH'");
			builder.append(" else null end");
			
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PROCESS_TYPE.getValue());

			sqlStatement.addSelectClause("case when zz.FormKey is not null then '1'  else null end", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_TYPE.getValue());

			sqlStatement.addSelectClause("c.dtid", SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue());

			builder.delete(0, builder.length());
/*			builder.append(" case when k.name='EDC管理部' then 'CYB'");
			builder.append(" when k.name='維護五組' then 'PRINDA'");
			builder.append(" when k.name='維護四組' then 'PRINDA'");
			builder.append(" when k.name='維護八組' then 'PRINDA'");
			builder.append(" when k.name='維護管理部' then 'CYB'");
			builder.append(" else 'CYB' end");*/
			// Task #2757 文檔
			builder.append(" case when k.name='EDC管理部' then 'CYB'");
			builder.append(" when k.name='維護五組' then 'PRINDA'");
			builder.append(" when k.name='維護四組' then 'PRINDA'");
			builder.append(" when k.name='維護八組' then 'PRINDA'");
			builder.append(" when k.name='維護管理部' then 'CYB'");
			builder.append(" when k.name='維護六組' then 'FIS'");
			builder.append(" else 'CYB' end");
			
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_ID.getValue());
			// 維護部門
		//	sqlStatement.addSelectClause("case when a.status='StatusComplete' then k.name else '維護管理部' end", SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_ID.getValue());
		//	sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_ID.getValue());
//			Bug #2595
/*			builder.delete(0, builder.length());
			builder.append(" case when k.name ='維護管理部' then 'CUSTOMER_SERVICE'");
			builder.append(" when (k.name not in('EDC管理部', '維護五組', '維護四組', '維護八組')) then 'CUSTOMER_SERVICE'");
			builder.append(" else k.name end");*/
			
			// Task #2639
			builder.delete(0, builder.length());
/*			builder.append(" case when a.status='StatusEnd' then 'CUSTOMER_SERVICE'");
			builder.append(" when a.status='StatusComplete' then k.name");
			builder.append(" else null end");*/
			builder.append(" case when a.status='StatusEnd' then 'CUSTOMER_SERVICE'");
			builder.append(" when a.status='StatusComplete' then (case when isnull(k.name,'') = '維護管理部' then 'CUSTOMER_SERVICE' else k.name end)");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_ID.getValue());

			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_PROJECT.getValue());

			builder.delete(0, builder.length());
			builder.append(" case when a.requestType_ID in (370,372,376) then e.VALUE ");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PROJECT_CODE.getValue());
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PROJECT_NAME.getValue());

			builder.delete(0, builder.length());
			builder.append(" case when a.caseType in (1,2) then 'APPOINTMENT' ");
/*			builder.append(" when a.caseType in (0) and importanceType_ID=501 then 'COMMON' ");
			builder.append(" when a.caseType in (0) and importanceType_ID=502 then 'FAST' ");
			builder.append(" when a.caseType in (0) and importanceType_ID=503 then 'EXTRA' ");*/
			// Task #2647 增加別名調用，避免指代不明
			builder.append(" when a.caseType in (0) and a.importanceType_ID=501 then 'COMMON' ");
			builder.append(" when a.caseType in (0) and a.importanceType_ID=502 then 'FAST' ");
			builder.append(" when a.caseType in (0) and a.importanceType_ID=503 then 'EXTRA' ");
			
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE.getValue());

			sqlStatement.addSelectClause("a.appointDate", SrmCaseHandleInfoDTO.ATTRIBUTE.EXPECTED_COMPLETION_DATE.getValue());
			// 直接查詢特店主鍵 
//			sqlStatement.addSelectClause("g.MARCHANT_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("g.identifier", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("c.tid2", SrmCaseHandleInfoDTO.ATTRIBUTE.OLD_MERCHANT_CODE.getValue());
			// 直接查詢特店表頭主鍵 
//			sqlStatement.addSelectClause("f.LOCATION_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_HEADER_ID.getValue());
			sqlStatement.addSelectClause("f.identifier", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_HEADER_ID.getValue());

			builder.delete(0, builder.length());
			builder.append(" case when len(replace(a.INSTALL_ADDRESS,' ','')) < 1 then replace(a.INSTALL_ADDRESS,' ','') ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='臺北市' then '台北市'");
//			builder.append(" case when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='臺北市' then '台北市'");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='世貿' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='北市' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='台北' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='臺北' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='圓山' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='德安資' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='(市府' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='墨攻新' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='新北投' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='南港展' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='105' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='103' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='羅斯福' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='晶華酒' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='改:台' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='(總公' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='臺中' then '台中市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='中市' then '台中市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='大台中' then '台中市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='台中' then '台中市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='403' then '台中市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='407' then '台中市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='401' then '台中市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='411' then '台中市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,5)='404臺中' then '台中市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='桃園' then '桃園市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='(南崁' then '桃園市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='324' then '桃園市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='淡水區' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='台北縣' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='臺北縣' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='五股區' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='中和市' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='(新埔' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='(頂溪' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='新北' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='新店' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='220' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='南投市' then '南投縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='草屯' then '南投縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='台南' then '台南市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='臺南' then '台南市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='民族一' then '高雄市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='高雄' then '高雄市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='高市' then '高雄市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='813' then '高雄市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='806' then '高雄市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='特賣裝' then '高雄市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,5)='404高雄' then '高雄市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='新竹線' then '新竹縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='新竹竹' then '新竹縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='竹北市' then '新竹縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='資訊室' then '新竹市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='林啟宏' then '新竹市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='300' then '新竹市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='彰化' then '彰化縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='臺東' then '台東縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='台東' then '台東縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='宜蘭' then '宜蘭縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='屏東' then '屏東縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='雲林' then '雲林縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='花蓮' then '花蓮縣' ");
			builder.append(" else substring(replace(a.INSTALL_ADDRESS,' ',''),1,3) end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS_LOCATION.getValue());
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS_LOCATION.getValue());

			builder.delete(0, builder.length());
			builder.append(" case when len(replace(a.INSTALL_ADDRESS,' ','')) < = 0 then replace(a.INSTALL_ADDRESS,' ','')");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3) in");
//			builder.append(" case when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3) in");
			builder.append(" ('淡水區','南投市','世貿旅','(南崁','北市中','世貿一','臺北世','台北世', ");
			builder.append(" '北市復','淡水區','台北華','晶華酒','世貿三','世貿3','台中世','北市大', ");
			builder.append(" '中市太','103','高雄展','世貿旅','北市民','台北松','台北大','新北新', ");
			builder.append(" '五股區','中和市','台中區','高市鼓','高市鳳','民族一','台東市','民族一', ");
			builder.append(" '桃園二','彰化市','花蓮市','德安資','宜蘭市','台北南','300','404','屏東市', ");
			builder.append(" '羅斯福','高雄區','台中西','特賣裝','台南南','墨攻新','台北中','台北長', ");
			builder.append(" '高雄苓','台中旅','圓山花','台北信','高市左','北市南','臺南永','台中烏', ");
			builder.append(" '草屯鎮','北市中','新店中','高雄鳳','411','南投市','新竹竹','220','403', ");
			builder.append(" '(頂溪','(南崁','(市府','(新埔','總公司','(總公','雲林鎮','新北投','401', ");
			builder.append(" '世貿1','改:台','高雄巨','324','資訊室','林啟宏','407','圓山爭','南港展', ");
			builder.append(" '813','806','105','台中朝','草屯分','大台中','北市信','竹北市') then replace(a.INSTALL_ADDRESS,' ','') ");
			builder.append(" else substring(replace(a.INSTALL_ADDRESS,' ',''),4,len(replace(a.INSTALL_ADDRESS,' ',''))-3) end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue());
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS.getValue());
			
			sqlStatement.addSelectClause("f.LIAISON_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT.getValue());
			sqlStatement.addSelectClause("f.LIAISON_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER.getValue());

			sqlStatement.addSelectClause("f.LIAISON_TEL1", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_PHONE.getValue());
			sqlStatement.addSelectClause("f.LIAISON_TEL1", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_PHONE.getValue());

			sqlStatement.addSelectClause("'N'", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_BUSSINESS_ADDRESS.getValue());
			sqlStatement.addSelectClause("'N'", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_IS_BUSSINESS_ADDRESS.getValue());

			sqlStatement.addSelectClause("'Y'", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_BUSSINESS_CONTACT.getValue());
			sqlStatement.addSelectClause("'Y'", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_IS_BUSSINESS_CONTACT.getValue());

			sqlStatement.addSelectClause("'Y'", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_BUSSINESS_CONTACT_PHONE.getValue());
			sqlStatement.addSelectClause("'Y'", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_IS_BUSSINESS_CONTACT_PHONE.getValue());
			
			sqlStatement.addSelectClause("op.VALUE", SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE.getValue());
			
			sqlStatement.addSelectClause("c.edcIp", SrmCaseHandleInfoDTO.ATTRIBUTE.LOCALHOST_IP.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.NET_VENDOR_ID.getValue());

			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.GATEWAY.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.NETMASK.getValue());
			sqlStatement.addSelectClause("a.problemDecription", SrmCaseHandleInfoDTO.ATTRIBUTE.DESCRIPTION.getValue());
			
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATED_DESCRIPTION.getValue());
			sqlStatement.addSelectClause("isnull(i.on_site,0)", SrmCaseHandleInfoDTO.ATTRIBUTE.ATTENDANCE_TIMES.getValue());
			sqlStatement.addSelectClause("a.quicklyTotalCount", SrmCaseHandleInfoDTO.ATTRIBUTE.REPAIR_TIMES.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.REPAIR_REASON.getValue());
			
			builder.delete(0, builder.length());
			builder.append(" case when a.requestType_ID=369 and isnull(i.on_site,0) > 0 then 'ARRIVE_UNINSTALL'");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALL_TYPE.getValue());

			sqlStatement.addSelectClause("a.acceptableResponseTime", SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_RESPONSE_DATE.getValue());
			sqlStatement.addSelectClause("a.acceptableArriveTime", SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_ARRIVE_DATE.getValue());
			sqlStatement.addSelectClause("a.acceptableFinishTime", SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_FINISH_DATE.getValue());
			// 結案時間
			sqlStatement.addSelectClause("z.processTime", SrmCaseHandleInfoDTO.ATTRIBUTE.CLOSE_DATE.getValue());

//			sqlStatement.addSelectClause("case when a.status='StatusComplete' then k.name else '維護管理部' end", SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_DEPT_ID.getValue());
			// Task #2639
			builder.delete(0, builder.length());
/*			builder.append(" case when a.status='StatusEnd' then 'CUSTOMER_SERVICE'");
			builder.append(" when a.status='StatusComplete' then k.name");
			builder.append(" else null end");*/
			builder.append(" case when a.status='StatusEnd' then 'CUSTOMER_SERVICE'");
			builder.append(" when a.status='StatusComplete' then (case when isnull(k.name,'') = '維護管理部' then 'CUSTOMER_SERVICE' else k.name end)");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_DEPT_ID.getValue());
			
/*			builder.delete(0, builder.length());
			builder.append(" case when a.status='StatusComplete' then n.assignedEngineerIdentifier");
			builder.append(" when a.status='StatusEnd' then a.createEmployeeId");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_PROCESS_USER.getValue());*/
			// Task #2639
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_PROCESS_USER.getValue());

			builder.delete(0, builder.length());
/*			builder.append(" case when a.status='StatusComplete' then o.name ");
			builder.append(" when a.status='StatusEnd' then  p.name");
			builder.append(" else null end");*/
			// Task #2639
			builder.append(" case when a.status='StatusEnd' then y1.serviceEmployeeName");
			builder.append(" when a.status='StatusComplete' then k1.MODIFYINFO_MODIFY_USER_ID");
			builder.append(" else null end");
			
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_PROCESS_USERNAME.getValue());

			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_USER.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_USER_NAME.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_DATE.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_USER.getValue());
			
			sqlStatement.addSelectClause("r.MODIFYINFO_MODIFY_USER_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_USER_NAME.getValue());
			sqlStatement.addSelectClause("r.MODIFYINFO_MODIFY_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_DATE.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_USER.getValue());
			sqlStatement.addSelectClause("t.MODIFYINFO_MODIFY_USER_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_USER_NAME.getValue());
			sqlStatement.addSelectClause("t.MODIFYINFO_MODIFY_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_DATE.getValue());
			
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_USER.getValue());
			sqlStatement.addSelectClause("v.MODIFYINFO_MODIFY_USER_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_USER_NAME.getValue());
			sqlStatement.addSelectClause("a.realDoneDay", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.ANALYZE_USER.getValue());
			sqlStatement.addSelectClause("x.MODIFYINFO_MODIFY_USER_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.ANALYZE_USER_NAME.getValue());
			sqlStatement.addSelectClause("x.MODIFYINFO_MODIFY_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.ANALYZE_DATE.getValue());
			
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.CLOSE_USER.getValue());
			sqlStatement.addSelectClause("z.serviceEmployeeName", SrmCaseHandleInfoDTO.ATTRIBUTE.CLOSE_USER_NAME.getValue());

			builder.delete(0, builder.length());
//			builder.append(" case when a.status='StatusCancel' then 'voidCase'");
//			builder.append(" when a.status='StatusCancelByUser' then 'voidCase'");
//			builder.append(" when a.status='StatusComplete' then 'closed'");
//			builder.append(" when a.status='StatusEnd' and l.identifier is null then 'immediatelyClosing'");
//			builder.append(" when a.status='StatusEnd' and l.identifier is not null then 'onlineExclusion'");
//			builder.append(" else null end");
			
			builder.append(" case when a.status='StatusComplete' then 'Closed'");
//			builder.append(" when a.status='StatusEnd' and l.identifier is null then 'ImmediateClose'");
//			builder.append(" when a.status='StatusEnd' and l.identifier is not null then 'Closed'");
		//	Task #2604
			builder.append(" when a.status='StatusEnd' then 'ImmediateClose'");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS.getValue());

			sqlStatement.addSelectClause("a.createEmployeeId", SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			sqlStatement.addSelectClause("p.name", SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("a.created", SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sqlStatement.addSelectClause("null", BaseParameterItemDefDTO.ATTRIBUTE.UPDATED_BY_ID.getValue());
			sqlStatement.addSelectClause("z.serviceEmployeeName", SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("z.processTime", SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			
//			sqlStatement.addSelectClause("z.processTime", SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_FINISH_DATE.getValue());
			// Task #2646
			sqlStatement.addSelectClause("case when m.processTime is not null then m.processTime else a.updated end", SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_FINISH_DATE.getValue());
			
			
//			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_DEPT_ID.getValue());
			// Task #2647 裝機部門
			sqlStatement.addSelectClause("k12.name", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_DEPT_ID.getValue());
			
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_USER.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_TMS.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_REASON_CODE.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_REASON.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_REASON.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_SOLUTION.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSIBITY.getValue());
			
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_WARNNING.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_WARNNING.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_WARNNING.getValue());

/*			builder.delete(0, builder.length());
			builder.append(" case when a.requestType_ID=367 then a.realDoneDay");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_DATE.getValue());*/
			
			// Task #2647 裝機日期 
			sqlStatement.addSelectClause("Req.realDoneDay", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_DATE.getValue());
			
			
/*			builder.delete(0, builder.length());
			builder.append(" case when a.requestType_ID=369 then a.realDoneDay");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALLED_DATE.getValue());*/
			
			// Task #2647 拆機日期-->改為null
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALLED_DATE.getValue());

			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_ENABLE_DATE.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_DISABLE_DATE.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATE_ITEM.getValue());
			
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.SAME_INSTALLED.getValue());
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.TMS_PARAM_DESC.getValue());
			sqlStatement.addSelectClause("'Y'", SrmCaseHandleInfoDTO.ATTRIBUTE.FOMS_CASE.getValue());
			
			sqlStatement.addSelectClause("null", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_FIRST_INSTALLED.getValue());
			
			// Task #2606 增加線上排除 Task #2646
			builder.delete(0, builder.length());
			builder.append(" case when s1.ProcessKey is not null then 'Y'");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.HAS_ONLINE_EXCLUSION.getValue());
			
			builder.delete(0, builder.length());
			builder.append(fomschema).append(".FOMS_SFM_Request a");
			builder.append(" join (select bb.dtid,max(aa.identifier) as CaseKey");
			builder.append(" from ").append(fomschema).append(".FOMS_SFM_Request aa");
			builder.append(" join ").append(fomschema).append(".FOMS_EDC_DEVICE bb on aa.identifier=bb.requestId ");
			builder.append(" where aa.corporationId in (select identifier from ").append(fomschema).append(".CUM_CORPORATION where serviceType=1 and compShortCode not in ('TSB-EDC')) and");
			builder.append("  aa.status in ('StatusComplete','StatusEnd')");
			builder.append(" group by bb.dtid) a1 on a.identifier=a1.CaseKey ");
			
			
			// Task #2964 查詢無設備的最新案件
			if(isNoAsset){
				builder.append(" join ").append(fomschema).append(".CUM_CORPORATION b on a.corporationId=b.identifier and b.serviceType=1 and b.compShortCode<>'TSB-EDC' ");
				builder.append(" join ").append(fomschema).append(".FOMS_EDC_DEVICE c on a.identifier=c.requestId ");
			} else {
				builder.append(" join (select dtid");
				builder.append(" from AIMS.").append(aimschema).append(".repository");
				builder.append(" where status_id=6 and dtid is not null and dtid<>'' and valid=1 and ((owner not in ('三川行','其他','台新銀行')) or (owner='台新銀行' and customer<>'台新銀行'))");
				builder.append(" group by dtid) AIMS on a1.dtid=AIMS.dtid");
				builder.append(" join ").append(fomschema).append(".CUM_CORPORATION b on a.corporationId=b.identifier");
				builder.append(" join ").append(fomschema).append(".FOMS_EDC_DEVICE c on a.identifier=c.requestId");
			}
			
			
			builder.append(" left join ").append(fomschema).append(".CCM_CORPORATIONCONTRACT d on a.customerContractId=d.identifier");
			builder.append(" left join ").append(fomschema).append(".CORE_OPTION e on a.requestType_ID=e.ID");
			builder.append(" left join ").append(fomschema).append(".CUM_Title f on c.merchantIdentifier=f.identifier");
			builder.append(" left join ").append(fomschema).append(".CUM_MERCHANT g on f.MARCHANT_ID=g.identifier");
			builder.append(" left join (select bb.requestId as CaseKey,count(aa.identifier) as on_site");
			builder.append(" from ").append(fomschema).append(".FOMS_EDC_EdcWorkInventoryFlowLog aa");
			builder.append(" join ").append(fomschema).append(".FOMS_EDC_EdcWorkInventoryFlow bb ");
			builder.append(" on aa.EdcWorkInventoryFlow_Identifier=bb.identifier");
			builder.append(" where aa.processtype='on_site'");
			builder.append(" group by bb.requestId) i on a.identifier=i.CaseKey "); 
			builder.append(" left join (select requestId as CaseKey,max(identifier) as ProcessKey");
			builder.append(" from ").append(fomschema).append(".FOMS_SFM_RequestProcessLog");
			builder.append(" where processType='dispatchEdc' or ");
			builder.append(" processType='edcDispatch' or ");
			builder.append(" processType='transger_edcAgent'");
			builder.append(" group by requestId) jj on a.identifier=jj.CaseKey");
			builder.append(" left join ").append(fomschema).append(".FOMS_SFM_RequestProcessLog j on jj.ProcessKey=j.identifier ");
			builder.append(" left join ").append(fomschema).append(".ORG_PARTY k on j.assignedVendorId=k.identifier");
			builder.append(" left join (select aa.requestId as CaseKey,max(aa.identifier) as ProcessKey");
			builder.append(" from ").append(fomschema).append(".FOMS_EDC_EdcWorkInventory aa");
			builder.append(" join ").append(fomschema).append(".FOMS_EDC_EdcWorkInventoryLog bb");
			builder.append(" on aa.identifier=bb.EdcWorkInventory_Identifier and ");
			builder.append(" bb.role='1'");
			builder.append(" group by aa.requestId) ll on a.identifier=ll.CaseKey");
			builder.append(" left join ").append(fomschema).append(".FOMS_EDC_EdcWorkInventory l on ll.ProcessKey=l.identifier");
			builder.append(" left join (select requestId as CaseKey,min(identifier) as ProcessKey");
			builder.append(" from ").append(fomschema).append(".FOMS_SFM_RequestProcessLog");
			builder.append(" where processType='edcDispatch' or");
			builder.append(" processType='dispatchEdc'");
			builder.append(" group by requestId) as mm on a.identifier=mm.CaseKey");
			builder.append(" left join ").append(fomschema).append(".FOMS_SFM_RequestProcessLog m on mm.ProcessKey=m.identifier ");
			builder.append(" left join (select requestId as CaseKey,max(identifier) as FlowKey");
			builder.append(" from ").append(fomschema).append(".FOMS_EDC_EdcWorkInventoryFlow");
			builder.append(" group by requestId) nn on a.identifier=nn.CaseKey");
			builder.append(" left join ").append(fomschema).append(".FOMS_EDC_EdcWorkInventoryFlow n on nn.FlowKey=n.identifier");
			builder.append(" left join ").append(fomschema).append(".ORG_PARTY o on n.assignedEngineerIdentifier=o.identifier");
			builder.append(" left join ").append(fomschema).append(".ORG_PARTY p on a.createEmployeeId=p.identifier   ");
			builder.append(" left join (select bb.requestId as CaseKey,max(aa.identifier) as ProcessKey");
			builder.append(" from ").append(fomschema).append(".FOMS_EDC_EdcWorkInventoryFlowLog aa");
			builder.append(" join ").append(fomschema).append(".FOMS_EDC_EdcWorkInventoryFlow bb ");
			builder.append(" on aa.EdcWorkInventoryFlow_Identifier=bb.identifier");
			builder.append(" where aa.processtype in ('accept','agent_response')   ");
			builder.append(" group by bb.requestId) q on a.identifier=q.CaseKey   ");       
			builder.append(" left join ").append(fomschema).append(".FOMS_EDC_EdcWorkInventoryFlowLog r on q.ProcessKey=r.identifier");
			builder.append(" left join (select bb.requestId as CaseKey,max(aa.identifier) as ProcessKey");
			builder.append(" from ").append(fomschema).append(".FOMS_EDC_EdcWorkInventoryFlowLog aa");
			builder.append(" join ").append(fomschema).append(".FOMS_EDC_EdcWorkInventoryFlow bb ");
			builder.append(" on aa.EdcWorkInventoryFlow_Identifier=bb.identifier");
			builder.append(" where aa.processtype in ('on_site')   ");
			builder.append(" group by bb.requestId) s on a.identifier=s.CaseKey       ");                       
			builder.append(" left join ").append(fomschema).append(".FOMS_EDC_EdcWorkInventoryFlowLog t on s.ProcessKey=t.identifier  ");                           
			builder.append(" left join (select bb.requestId as CaseKey,max(aa.identifier) as ProcessKey");
			builder.append(" from ").append(fomschema).append(".FOMS_EDC_EdcWorkInventoryFlowLog aa");
			builder.append(" join ").append(fomschema).append(".FOMS_EDC_EdcWorkInventoryFlow bb ");
			builder.append(" on aa.EdcWorkInventoryFlow_Identifier=bb.identifier");
			builder.append("  where aa.processtype in ('complete')   ");
			builder.append(" group by bb.requestId) u on a.identifier=u.CaseKey     ");                        
			builder.append(" left join ").append(fomschema).append(".FOMS_EDC_EdcWorkInventoryFlowLog v on u.ProcessKey=v.identifier");
			builder.append(" left join (select bb.requestId as CaseKey,max(aa.identifier) as ProcessKey");
			builder.append(" from ").append(fomschema).append(".FOMS_EDC_EdcWorkInventoryFlowLog aa");
			builder.append(" join ").append(fomschema).append(".FOMS_EDC_EdcWorkInventoryFlow bb ");
			builder.append(" on aa.EdcWorkInventoryFlow_Identifier=bb.identifier");
			builder.append(" where aa.processtype in ('censor')   ");
			builder.append(" group by bb.requestId) w on a.identifier=w.CaseKey");
			builder.append(" left join ").append(fomschema).append(".FOMS_EDC_EdcWorkInventoryFlowLog x on w.ProcessKey=x.identifier      ");     
			builder.append(" left join (select aa.requestId as CaseKey,max(aa.identifier) as ProcessKey");
			builder.append(" from ").append(fomschema).append(".FOMS_SFM_RequestProcessLog aa");
			// Task #2628
		//	builder.append("  where aa.processtype in ('qualified','solve_on_phone','coordinate')  ");    
			builder.append("  where aa.processtype in  ('qualified','solve_on_phone','coordinate','edcEnd','finish') ");  
			
			builder.append(" group by aa.requestId) y on a.identifier=y.CaseKey");
			builder.append(" left join  ").append(fomschema).append(".FOMS_SFM_RequestProcessLog z on y.ProcessKey=z.identifier");
			builder.append(" left join (select bb.dtid,max(bb.requestId) as FormKey");
			builder.append(" from ").append(fomschema).append(".FOMS_SFM_Request aa");
			builder.append(" join ").append(fomschema).append(".FOMS_EDC_DEVICE bb on aa.identifier=bb.requestId");
			builder.append(" where aa.corporationId in (select identifier from ").append(fomschema).append(".CUM_CORPORATION where serviceType=1 and compShortCode not in ('TSB-EDC')) and");
			builder.append(" aa.requestType_ID=367 and     ");
			builder.append(" aa.status in ('StatusComplete','StatusEnd')");
			
			// Task #2964 查詢無設備的最新案件
			if(isNoAsset){
				builder.append(" group by bb.dtid) zz on c.dtid=zz.dtid");
			} else {
				builder.append(" group by bb.dtid) zz on AIMS.dtid=zz.dtid");
			}
		
			builder.append(" left join ").append(fomschema).append(".CORE_OPTION op on a.deviceType_ID =op.ID");
			
			// Task #2606 增加線上排除
			builder.append(" left join (select bb.requestId as CaseKey,max(aa.identifier) as ProcessKey");
			builder.append(" from ").append(fomschema).append(".FOMS_EDC_EdcWorkInventoryFlowLog aa");
			builder.append(" join ").append(fomschema).append(".FOMS_EDC_EdcWorkInventoryFlow bb on aa.EdcWorkInventoryFlow_Identifier=bb.identifier");
			builder.append(" where aa.processtype in ('solveOnLine') ");
			builder.append(" group by bb.requestId) s1 on a.identifier=s1.CaseKey");
			
			// Task #2639
			builder.append(" left join (select requestId as CaseKey,max(identifier) as flowKey");
			builder.append(" from ").append(fomschema).append(".FOMS_SFM_RequestProcessLog");
			builder.append(" group by requestId) as z1 on a.identifier=z1.CaseKey");
			builder.append(" left join ").append(fomschema).append(".FOMS_SFM_RequestProcessLog y1 on z1.flowKey=y1.identifier");
			builder.append(" left join (select bb.requestId as CaseKey,max(aa.identifier) as ProcessKey");
			builder.append(" from ").append(fomschema).append(".FOMS_EDC_EdcWorkInventoryFlowLog aa");
			builder.append(" join ").append(fomschema).append(".FOMS_EDC_EdcWorkInventoryFlow bb on aa.EdcWorkInventoryFlow_Identifier=bb.identifier");
			builder.append(" where aa.processtype in ('censor') ");
			builder.append(" group by bb.requestId) w1 on a.identifier=w1.CaseKey");
			builder.append(" left join ").append(fomschema).append(".FOMS_EDC_EdcWorkInventoryFlowLog k1 on w1.ProcessKey=k1.identifier");
			
			// Task #2647 裝機部門、裝機日期
			builder.append(" left join (select de1.dtid,max(re1.identifier) as CaseKey");
			builder.append(" from ").append(fomschema).append(".FOMS_SFM_Request re1");
			builder.append(" join ").append(fomschema).append(".FOMS_EDC_DEVICE de1 on re1.identifier=de1.requestId");
			builder.append(" where re1.requestType_ID=367 and re1.status in ('StatusComplete','StatusEnd')");
			
			// Task #2964 查詢無設備的最新案件
			if(isNoAsset){
				builder.append(" group by de1.dtid) as inst on c.dtid=inst.dtid");
			} else {
				builder.append(" group by de1.dtid) as inst on AIMS.dtid=inst.dtid");
			}

			builder.append(" left join ").append(fomschema).append(".FOMS_SFM_Request Req on inst.CaseKey=Req.identifier");
			builder.append(" left join (select requestId as CaseKey,max(identifier) as ProcessKey");
			builder.append(" from ").append(fomschema).append(".FOMS_SFM_RequestProcessLog");
			builder.append(" where processType='dispatchEdc' or ");
			builder.append(" processType='edcDispatch' or ");
			builder.append(" processType='transger_edcAgent'");
			builder.append(" group by requestId) jj1 on Req.identifier=jj1.CaseKey");
			builder.append(" left join ").append(fomschema).append(".FOMS_SFM_RequestProcessLog j12 on jj1.ProcessKey=j12.identifier ");
			builder.append(" left join ").append(fomschema).append(".ORG_PARTY k12 on j12.assignedVendorId=k12.identifier");


			sqlStatement.addFromExpression(builder.toString());
			
			// Task #2964 查詢無設備的最新案件
			if(isNoAsset){
				builder.delete(0, builder.length());
				builder.append(" c.dtid not in (select distinct dtid ");
				builder.append(" from AIMS.").append(aimschema).append(".repository ");
				builder.append(" where status_id=6 and dtid is not null and dtid<>'' and valid=1 and ((owner not in ('三川行','其他','台新銀行')) or (owner='台新銀行' and customer<>'台新銀行'))) ");
				sqlStatement.addWhereClause(builder.toString());
			}
			
			
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseHandleInfoDTO.class);
			LOGGER.debug("listCaseNewHandleInfo()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return result;
		} catch (Exception e) {
			LOGGER.error("listCaseNewHandleInfo()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#listNotCloseCaseInstallOrUpdate(java.lang.String)
	 */
	@Override
	public List<SrmCaseHandleInfoDTO> listNotCloseCaseInstallOrUpdate(String caseCategory) throws DataAccessException {
		List<SrmCaseHandleInfoDTO> result = null;
		try {
			//得到schema
			String schema = this.getSchema("iatoms-foms");
			SqlStatement sqlStatement = new SqlStatement();
			StringBuilder builder = new StringBuilder();
			sqlStatement.addSelectClause("a.CASENUM", SrmCaseHandleInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue());
			sqlStatement.addSelectClause("c.name", SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_NAME.getValue());
			if(StringUtils.hasText(caseCategory) && caseCategory.equals(IAtomsConstants.CASE_CATEGORY.INSTALL.getCode())) {
				sqlStatement.addSelectClause("'一般特店'", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_TYPE.getValue());
				builder.append("case when k.name='EDC管理部' then 'Cybersoft' ");
				builder.append("when k.name='維護四組' then '普林達' ");
				//Task #2759新增
				builder.append("when k.name='維護六組' then '華經資訊' ");
				builder.append("else null end");
				sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_NAME.getValue());
				sqlStatement.addSelectClause("'否'", SrmCaseHandleInfoDTO.ATTRIBUTE.PROJECT_CODE.getValue());
			} else if(StringUtils.hasText(caseCategory) && caseCategory.equals(IAtomsConstants.CASE_CATEGORY.UPDATE.getCode())){
				sqlStatement.addSelectClause("g.dtid", SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue());
				//Task #2693（'' as IS_PROJECT, --> 'N' as IS_PROJECT,）
				sqlStatement.addSelectClause("'否'", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_PROJECT.getValue());
				sqlStatement.addSelectClause("g.tid2", SrmCaseHandleInfoDTO.ATTRIBUTE.OLD_MERCHANT_CODE.getValue());
				sqlStatement.addSelectClause("'否'", SrmCaseHandleInfoDTO.ATTRIBUTE.SAME_INSTALLED.getValue());
			}
			builder.delete(0, builder.length());
			//Task #2759修改（新增‘維護六組’）
			builder.append("case when k.name in ('EDC管理部','維護四組','維護六組') then k.name ");
			//Task #2693(case when k.name in ('EDC管理部','維護四組') then k.name else '' end--> case when k.name in ('EDC管理部','維護四組') then k.name else '客服')
			if (StringUtils.hasText(caseCategory) && caseCategory.equals(IAtomsConstants.CASE_CATEGORY.UPDATE.getCode())) {
				builder.append("else '客服' end");
			} else {
				builder.append("else null end");
			}
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_NAME.getValue());
			
			
			
			builder.delete(0, builder.length());
			builder.append("case when a.caseType in (1,2) then '預約' ");
			builder.append("when a.caseType in (0) and importanceType_ID=501 then '一般' ");
			builder.append("when a.caseType in (0) and importanceType_ID=502 then '急件' ");
			builder.append("when a.caseType in (0) and importanceType_ID=503 then '特急件' ");
			builder.append("else null end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE_NAME.getValue());
			
			sqlStatement.addSelectClause("a.appointDate", SrmCaseHandleInfoDTO.ATTRIBUTE.EXPECTED_COMPLETION_DATE.getValue());
			sqlStatement.addSelectClause("e.MARCHANT_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("d.LOCATION_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.HEADER_NAME.getValue());
			sqlStatement.addSelectClause("'否'", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_BUSSINESS_ADDRESS.getValue());
			
			builder.delete(0, builder.length());
			builder.append(" case when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='臺北市' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='世貿' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='北市' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='台北' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='臺北' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='圓山' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='德安資' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='(市府' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='墨攻新' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='新北投' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='南港展' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='105' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='103' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='羅斯福' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='晶華酒' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='改:台' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='(總公' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='臺中' then '台中市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='中市' then '台中市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='大台中' then '台中市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='台中' then '台中市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='403' then '台中市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='407' then '台中市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='401' then '台中市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='411' then '台中市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,5)='404臺中' then '台中市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='桃園' then '桃園市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='(南崁' then '桃園市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='324' then '桃園市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='淡水區' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='台北縣' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='臺北縣' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='五股區' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='中和市' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='(新埔' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='(頂溪' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='新北' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='新店' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='220' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='南投市' then '南投縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='草屯' then '南投縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='台南' then '台南市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='臺南' then '台南市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='民族一' then '高雄市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='高雄' then '高雄市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='高市' then '高雄市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='813' then '高雄市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='806' then '高雄市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='特賣裝' then '高雄市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,5)='404高雄' then '高雄市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='新竹線' then '新竹縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='新竹竹' then '新竹縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='竹北市' then '新竹縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='資訊室' then '新竹市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='林啟宏' then '新竹市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='300' then '新竹市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='彰化' then '彰化縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='臺東' then '台東縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='台東' then '台東縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='宜蘭' then '宜蘭縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='屏東' then '屏東縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='雲林' then '雲林縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='花蓮' then '花蓮縣' ");
			builder.append(" else substring(replace(a.INSTALL_ADDRESS,' ',''),1,3) end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS_LOCATION.getValue());
			
			builder.delete(0, builder.length());
			builder.append("case when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3) in ");
			builder.append("('淡水區','南投市','世貿旅','(南崁','北市中','世貿一','臺北世','台北世',");
			builder.append("'北市復','淡水區','台北華','晶華酒','世貿三','世貿3','台中世','北市大',");
			builder.append("'中市太','103','高雄展','世貿旅','北市民','台北松','台北大','新北新',");
			builder.append("'五股區','中和市','台中區','高市鼓','高市鳳','民族一','台東市','民族一',");
			builder.append("'桃園二','彰化市','花蓮市','德安資','宜蘭市','台北南','300','404','屏東市',");
			builder.append("'羅斯福','高雄區','台中西','特賣裝','台南南','墨攻新','台北中','台北長',");
			builder.append("'高雄苓','台中旅','圓山花','台北信','高市左','北市南','臺南永','台中烏',");
			builder.append("'草屯鎮','北市中','新店中','高雄鳳','411','南投市','新竹竹','220','403',");
			builder.append("'(頂溪','(南崁','(市府','(新埔','總公司','(總公','雲林鎮','新北投','401',");
			builder.append("'世貿1','改:台','高雄巨','324','資訊室','林啟宏','407','圓山爭','南港展',");
			builder.append("'813','806','105','台中朝','草屯分','大台中','北市信','竹北市') ");
			builder.append("then replace(a.INSTALL_ADDRESS,' ','') ");
			builder.append(" else substring(replace(a.INSTALL_ADDRESS,' ',''),4,len(replace(a.INSTALL_ADDRESS,' ',''))-3) ");
			builder.append("end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue());
			
			sqlStatement.addSelectClause("'是'", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_BUSSINESS_CONTACT.getValue());
			sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT.getValue());
			sqlStatement.addSelectClause("'是'", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_BUSSINESS_CONTACT_PHONE.getValue());
			sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_PHONE.getValue());
			if (StringUtils.hasText(caseCategory) && caseCategory.equals(IAtomsConstants.CASE_CATEGORY.INSTALL.getCode())) {
				sqlStatement.addSelectClause("f.value", SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE_NAME.getValue());
				builder.delete(0, builder.length());
				builder.append("case when g.R50=1 then 'S300Q' ");
				builder.append("else '' end");
				sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_NAME.getValue());
				
//				builder.delete(0, builder.length());
//				builder.append("case when g.R50=1 then 'Dongle' ");
//				builder.append("else '' end");
//				sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION_NAME.getValue());
				sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION_NAME.getValue());
				
				builder.delete(0, builder.length());
				builder.append("case when g.ECR=1 then '有ECR連線' ");
				builder.append("else '無ECR連線' end");
				sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.ECR_CONNECTION_NAME.getValue());
				
				builder.delete(0, builder.length());
				builder.append("case when g.PINPAD=1 then 'SP20' ");
				builder.append("else '' end");
				sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_NAME.getValue());
				
				/*builder.delete(0, builder.length());
				builder.append("case when g.PINPAD=1 then 'Pinpad' ");
				builder.append("else '' end");
				sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION2_NAME.getValue());*/
				sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION2_NAME.getValue());
				
				builder.delete(0, builder.length());
				builder.append("case when g.S200=1 then 'S200' ");
				builder.append("else '' end");
				sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_NAME.getValue());
				
				/*builder.delete(0, builder.length());
				builder.append("case when g.S200=1 then 'Dongle,Pinpad' ");
				builder.append("else '' end");
				sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION3_NAME.getValue());*/
				sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION3_NAME.getValue());
				
			} else if(StringUtils.hasText(caseCategory) && caseCategory.equals(IAtomsConstants.CASE_CATEGORY.UPDATE.getCode())){
				sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_NAME.getValue());
				sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION_NAME.getValue());
				sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.ECR_CONNECTION_NAME.getValue());
				sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_NAME.getValue());
				sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION2_NAME.getValue());
				sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_NAME.getValue());
				sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_NAME.getValue());
			}
			//sqlStatement.addSelectClause("f.value", SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.SOFTWARE_VERSION_NAME.getValue());
			sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.BUILT_IN_FEATURE_NAME.getValue());
			sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.MULTI_MODULE_NAME.getValue());
			
			/*builder.delete(0, builder.length());
			builder.append("case when g.R50=1 then 'R50' ");
			builder.append("else '' end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_NAME.getValue());
			
			builder.delete(0, builder.length());
			builder.append("case when g.R50=1 then 'Dongle' ");
			builder.append("else '' end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION_NAME.getValue());
			
			builder.delete(0, builder.length());
			builder.append("case when g.ECR=1 then '有ECR連線' ");
			builder.append("else '無ECR連線' end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.ECR_CONNECTION_NAME.getValue());
			
			builder.delete(0, builder.length());
			builder.append("case when g.PINPAD=1 then 'SP20' ");
			builder.append("else '' end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_NAME.getValue());
			
			builder.delete(0, builder.length());
			builder.append("case when g.PINPAD=1 then 'Pinpad' ");
			builder.append("else '' end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION2_NAME.getValue());*/
			
			sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.CONNECTION_TYPE_NAME.getValue());
			
			/*builder.delete(0, builder.length());
			builder.append("case when g.S200=1 then 'S200' ");
			builder.append("else '' end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_NAME.getValue());
			
			builder.delete(0, builder.length());
			builder.append("case when g.S200=1 then 'Dongle,Pinpad' ");
			builder.append("else '' end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION3_NAME.getValue());*/
			
			sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.LOGO_STYLE.getValue());
			sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_OPEN_ENCRYPT.getValue());
			sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_PAY_PLATFORM.getValue());
			sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_INVOICE.getValue());
			sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_QUICK_PASS.getValue());
			sqlStatement.addSelectClause("g.edcIp", SrmCaseHandleInfoDTO.ATTRIBUTE.LOCALHOST_IP.getValue());
			sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.NET_VENDOR_ID.getValue());
			sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.GATEWAY.getValue());
			sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.NETMASK.getValue());
			sqlStatement.addSelectClause("a.problemDecription", SrmCaseHandleInfoDTO.ATTRIBUTE.DESCRIPTION.getValue());
			sqlStatement.addSelectClause("'是'", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_TMS.getValue());
			sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.TMS_PARAM_DESC.getValue());
			
			builder.delete(0, builder.length());
			builder.append(schema).append(".FOMS_SFM_Request a ");
			builder.append("join ").append(schema).append(".CUM_CORPORATION b on a.corporationId=b.identifier and b.serviceType=1 and b.compShortCode<>'TSB-EDC' ");
			builder.append("join ").append(schema).append(".ORG_PARTY c on b.identifier=c.identifier ");
			builder.append("join ").append(schema).append(".CUM_Title d on a.depositaryEmployeeId=d.identifier ");
			builder.append("join ").append(schema).append(".CUM_MERCHANT e on d.MARCHANT_ID=e.identifier ");
			builder.append("join ").append(schema).append(".CORE_OPTION f on a.deviceType_ID=f.ID ");
			builder.append("join ").append(schema).append(".FOMS_EDC_DEVICE g on a.identifier=g.requestId ");
			builder.append("left join (select ");
			builder.append("requestId as CaseKey,");
			builder.append("max(identifier) as ProcessKey ");
			builder.append("from ").append(schema).append(".FOMS_SFM_RequestProcessLog ");
			builder.append("where ");
			builder.append("processType='dispatchEdc' or processType='edcDispatch' or processType='transger_edcAgent'");
			builder.append("group by requestId) jj on a.identifier=jj.CaseKey ");
			builder.append("left join ").append(schema).append(".FOMS_SFM_RequestProcessLog j on jj.ProcessKey=j.identifier ");
			builder.append("left join ").append(schema).append(".ORG_PARTY k on j.assignedVendorId=k.identifier ");
			sqlStatement.addFromExpression(builder.toString());
			if(StringUtils.hasText(caseCategory) && caseCategory.equals(IAtomsConstants.CASE_CATEGORY.INSTALL.getCode())) {
				sqlStatement.addWhereClause("a.requestType_ID=367");
			} else if(StringUtils.hasText(caseCategory) && caseCategory.equals(IAtomsConstants.CASE_CATEGORY.UPDATE.getCode())){
				sqlStatement.addWhereClause("a.requestType_ID=374");
			}
			
			sqlStatement.addWhereClause("a.status not in ('StatusCancel','StatusCancelByUser','StatusComplete','StatusEnd')");
			sqlStatement.setOrderByExpression("c.name,a.created");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseHandleInfoDTO.class);
			LOGGER.debug("listCaseHandleInfo()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listNotCloseCaseInfo()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return result;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#listAssetForCase()
	 */
	@Override
	public List<SrmCaseHandleInfoDTO> listAssetForCase() throws DataAccessException {
		List<SrmCaseHandleInfoDTO> result = null;
		//得到schema
		String schema = this.getSchema("iatoms-aims");
		SqlStatement sqlStatement = new SqlStatement();
		StringBuilder builder = new StringBuilder();
		try {
			sqlStatement.addSelectClause("a.dtid", SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("b.name", SrmCaseHandleInfoDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			
			builder.append("AIMS.").append(schema).append(".repository a");
			builder.append(" join AIMS.").append(schema).append(".asset_category b on a.asset_category_id=b.id and b.asset_type_id=8");
			sqlStatement.addFromExpression(builder.toString());
			
			builder.delete(0, builder.length());
			builder.append(" a.status_id=6 and ");
			builder.append(" a.dtid is not null and ");
			builder.append(" a.dtid<>'' and ");
			builder.append(" a.valid=1 and ");
			builder.append(" ((a.owner not in ('三川行','其他','台新銀行')) or (a.owner='台新銀行' and a.customer<>'台新銀行'))");
			sqlStatement.addWhereClause(builder.toString());
			
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseHandleInfoDTO.class);
			LOGGER.debug("listAssetForCase()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return result;
		} catch (Exception e) {
			LOGGER.error("listAssetForCase()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#listNotCloseCaseOthers(java.lang.String)
	 */
	@Override
	public List<SrmCaseHandleInfoDTO> listNotCloseCaseOthers(String caseCategory) throws DataAccessException {
		List<SrmCaseHandleInfoDTO> result = null;
		try {
			//得到schema
			String schema = this.getSchema("iatoms-foms");
			SqlStatement sqlStatement = new SqlStatement();
			StringBuilder builder = new StringBuilder();
			sqlStatement.addSelectClause("a.CASENUM", SrmCaseHandleInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue());
			sqlStatement.addSelectClause("c.DTID", SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("d.name", SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_NAME.getValue());
			if (IAtomsConstants.CASE_CATEGORY.MERGE.getCode().equals(caseCategory)){
				sqlStatement.addSelectClause("'否'", SrmCaseHandleInfoDTO.ATTRIBUTE.SAME_INSTALLED.getValue());
				//Task #2694('' as DEPARTMENT_ID, --> '客服' as DEPARTMENT_ID)
				sqlStatement.addSelectClause("'客服'", SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_NAME.getValue());
			} else if (IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode().equals(caseCategory)) {
				sqlStatement.addSelectClause("'到場拆機'", SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALL_TYPE_NAME.getValue());
				sqlStatement.addSelectClause("k.name", SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_NAME.getValue());
			} else if (IAtomsConstants.CASE_CATEGORY.CHECK.getCode().equals(caseCategory)) {
				sqlStatement.addSelectClause("k.name", SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_NAME.getValue());
			} else if (IAtomsConstants.CASE_CATEGORY.PROJECT.getCode().equals(caseCategory)) {
				sqlStatement.addSelectClause("k.name", SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_NAME.getValue());
				sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.PROJECT_CODE.getValue());
				sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.PROJECT_NAME.getValue());
			} else if (IAtomsConstants.CASE_CATEGORY.REPAIR.getCode().equals(caseCategory)) {
				sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.REPAIR_REASON_NAME.getValue());
				sqlStatement.addSelectClause("k.name", SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_NAME.getValue());
			}
			
			builder.append("case when a.caseType in (1,2) then '預約' ");
			builder.append("when a.caseType in (0) and importanceType_ID=501 then '一般' ");
			builder.append("when a.caseType in (0) and importanceType_ID=502 then '急件' ");
			builder.append("when a.caseType in (0) and importanceType_ID=503 then '特急件' ");
			builder.append("else null end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE_NAME.getValue());
			
			sqlStatement.addSelectClause("a.appointDate", SrmCaseHandleInfoDTO.ATTRIBUTE.EXPECTED_COMPLETION_DATE.getValue());
			sqlStatement.addSelectClause("'否'", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_IS_BUSSINESS_ADDRESS.getValue());
			
			builder.delete(0, builder.length());
			builder.append(" case when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='臺北市' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='世貿' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='北市' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='台北' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='臺北' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='圓山' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='德安資' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='(市府' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='墨攻新' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='新北投' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='南港展' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='105' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='103' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='羅斯福' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='晶華酒' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='改:台' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='(總公' then '台北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='臺中' then '台中市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='中市' then '台中市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='大台中' then '台中市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='台中' then '台中市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='403' then '台中市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='407' then '台中市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='401' then '台中市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='411' then '台中市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,5)='404臺中' then '台中市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='桃園' then '桃園市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='(南崁' then '桃園市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='324' then '桃園市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='淡水區' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='台北縣' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='臺北縣' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='五股區' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='中和市' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='(新埔' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='(頂溪' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='新北' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='新店' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='220' then '新北市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='南投市' then '南投縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='草屯' then '南投縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='台南' then '台南市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='臺南' then '台南市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='民族一' then '高雄市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='高雄' then '高雄市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='高市' then '高雄市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='813' then '高雄市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='806' then '高雄市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='特賣裝' then '高雄市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,5)='404高雄' then '高雄市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='新竹線' then '新竹縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='新竹竹' then '新竹縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='竹北市' then '新竹縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='資訊室' then '新竹市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='林啟宏' then '新竹市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3)='300' then '新竹市' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='彰化' then '彰化縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='臺東' then '台東縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='台東' then '台東縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='宜蘭' then '宜蘭縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='屏東' then '屏東縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='雲林' then '雲林縣' ");
			builder.append(" when substring(replace(a.INSTALL_ADDRESS,' ',''),1,2)='花蓮' then '花蓮縣' ");
			builder.append(" else substring(replace(a.INSTALL_ADDRESS,' ',''),1,3) end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS_LOCATION.getValue());
			
			builder.delete(0, builder.length());
			builder.append("case when substring(replace(a.INSTALL_ADDRESS,' ',''),1,3) in ");
			builder.append("('淡水區','南投市','世貿旅','(南崁','北市中','世貿一','臺北世','台北世',");
			builder.append("'北市復','淡水區','台北華','晶華酒','世貿三','世貿3','台中世','北市大',");
			builder.append("'中市太','103','高雄展','世貿旅','北市民','台北松','台北大','新北新',");
			builder.append("'五股區','中和市','台中區','高市鼓','高市鳳','民族一','台東市','民族一',");
			builder.append("'桃園二','彰化市','花蓮市','德安資','宜蘭市','台北南','300','404','屏東市',");
			builder.append("'羅斯福','高雄區','台中西','特賣裝','台南南','墨攻新','台北中','台北長',");
			builder.append("'高雄苓','台中旅','圓山花','台北信','高市左','北市南','臺南永','台中烏',");
			builder.append("'草屯鎮','北市中','新店中','高雄鳳','411','南投市','新竹竹','220','403',");
			builder.append("'(頂溪','(南崁','(市府','(新埔','總公司','(總公','雲林鎮','新北投','401',");
			builder.append("'世貿1','改:台','高雄巨','324','資訊室','林啟宏','407','圓山爭','南港展',");
			builder.append("'813','806','105','台中朝','草屯分','大台中','北市信','竹北市') ");
			builder.append("then replace(a.INSTALL_ADDRESS,' ','') ");
			builder.append(" else substring(replace(a.INSTALL_ADDRESS,' ',''),4,len(replace(a.INSTALL_ADDRESS,' ',''))-3) ");
			builder.append("end");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS.getValue());
			
			
			sqlStatement.addSelectClause("'是'", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_IS_BUSSINESS_CONTACT.getValue());
			sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER.getValue());
			sqlStatement.addSelectClause("'是'", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_IS_BUSSINESS_CONTACT_PHONE.getValue());
			sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_PHONE.getValue());
			if (IAtomsConstants.CASE_CATEGORY.MERGE.getCode().equals(caseCategory)){
				sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.SOFTWARE_VERSION_NAME.getValue());
				sqlStatement.addSelectClause("'是'", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_TMS.getValue());
				sqlStatement.addSelectClause("", SrmCaseHandleInfoDTO.ATTRIBUTE.TMS_PARAM_DESC.getValue());
			}
			sqlStatement.addSelectClause("a.problemDecription", SrmCaseHandleInfoDTO.ATTRIBUTE.DESCRIPTION.getValue());
			builder.delete(0, builder.length());
			builder.append(schema).append(".FOMS_SFM_Request a ");
			builder.append("join ").append(schema).append(".CUM_CORPORATION b on a.corporationId=b.identifier and b.serviceType=1 and b.compShortCode<>'TSB-EDC' ");
			builder.append("join ").append(schema).append(".FOMS_EDC_DEVICE c on a.identifier=c.requestId ");
			builder.append("join ").append(schema).append(".ORG_PARTY d on b.identifier=d.identifier ");
			if (IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode().equals(caseCategory)
					|| IAtomsConstants.CASE_CATEGORY.CHECK.getCode().equals(caseCategory)
					|| IAtomsConstants.CASE_CATEGORY.PROJECT.getCode().equals(caseCategory)
					|| IAtomsConstants.CASE_CATEGORY.REPAIR.getCode().equals(caseCategory)) {
				builder.append("left join (select ");
				builder.append("requestId as CaseKey,");
				builder.append("max(identifier) as ProcessKey ");
				builder.append("from ").append(schema).append(".FOMS_SFM_RequestProcessLog ");
				builder.append("where ");
				builder.append("processType='dispatchEdc' or processType='edcDispatch' or processType='transger_edcAgent'");
				builder.append("group by requestId) jj on a.identifier=jj.CaseKey ");
				builder.append("left join ").append(schema).append(".FOMS_SFM_RequestProcessLog j on jj.ProcessKey=j.identifier ");
				builder.append("left join ").append(schema).append(".ORG_PARTY k on j.assignedVendorId=k.identifier ");
			}
			sqlStatement.addFromExpression(builder.toString());
			if(StringUtils.hasText(caseCategory) && caseCategory.equals(IAtomsConstants.CASE_CATEGORY.MERGE.getCode())) {
				sqlStatement.addWhereClause("a.requestType_ID=368");
			} else if(StringUtils.hasText(caseCategory) && caseCategory.equals(IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode())){
				sqlStatement.addWhereClause("a.requestType_ID=369");
			} else if (StringUtils.hasText(caseCategory) && caseCategory.equals(IAtomsConstants.CASE_CATEGORY.CHECK.getCode())) {
				sqlStatement.addWhereClause("a.requestType_ID=373");
			} else if (IAtomsConstants.CASE_CATEGORY.PROJECT.getCode().equals(caseCategory)) {
				sqlStatement.addWhereClause("a.requestType_ID in (370,372,376) ");
			} else if (IAtomsConstants.CASE_CATEGORY.REPAIR.getCode().equals(caseCategory)) {
				sqlStatement.addWhereClause("a.requestType_ID=371 ");
			}
			
			sqlStatement.addWhereClause("a.status not in ('StatusCancel','StatusCancelByUser','StatusComplete','StatusEnd')");
			sqlStatement.setOrderByExpression("d.name");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseHandleInfoDTO.class);
			LOGGER.debug("listCaseHandleInfo()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listNotCloseCaseInfo()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return result;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#listCaseNewAssetLink()
	 */
	@Override
	public List<SrmCaseAssetLinkDTO> listCaseNewAssetLink()throws DataAccessException {
		List<SrmCaseAssetLinkDTO> result = null;
		//得到schema
		String schema = this.getSchema("iatoms-aims");
		SqlStatement sqlStatement = new SqlStatement();
		StringBuilder builder = new StringBuilder();
		try {
			sqlStatement.addSelectClause("b.asset_type_id", SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_TYPE.getValue());
			sqlStatement.addSelectClause("b.name", SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_ID.getValue());
			
			sqlStatement.addSelectClause("a.sn", SrmCaseAssetLinkDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("1", SrmCaseAssetLinkDTO.ATTRIBUTE.NUMBER.getValue());
			sqlStatement.addSelectClause("null", SrmCaseAssetLinkDTO.ATTRIBUTE.ACTION.getValue());
			sqlStatement.addSelectClause("null", SrmCaseAssetLinkDTO.ATTRIBUTE.CONTENT.getValue());
			sqlStatement.addSelectClause("'Y'", SrmCaseAssetLinkDTO.ATTRIBUTE.IS_LINK.getValue());
			sqlStatement.addSelectClause("null", SrmCaseAssetLinkDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			sqlStatement.addSelectClause("null", SrmCaseAssetLinkDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("a.ENABLE_DATE", SrmCaseAssetLinkDTO.ATTRIBUTE.ENABLE_DATE.getValue());
			sqlStatement.addSelectClause("c.name", SrmCaseAssetLinkDTO.ATTRIBUTE.WAREHOUSE_ID.getValue());
			sqlStatement.addSelectClause("null", SrmCaseAssetLinkDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sqlStatement.addSelectClause("a.contract_id ", SrmCaseAssetLinkDTO.ATTRIBUTE.CONTRACT_ID.getValue());
			sqlStatement.addSelectClause("null", SrmCaseAssetLinkDTO.ATTRIBUTE.PRICE.getValue());
			sqlStatement.addSelectClause("d.History_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.HISTORY_ASSET_ID.getValue());
			
			sqlStatement.addSelectClause("a.sn2", SrmCaseAssetLinkDTO.ATTRIBUTE.PROPERTY_ID.getValue());
			
			sqlStatement.addSelectClause("a.dtid", SrmCaseAssetLinkDTO.ATTRIBUTE.DTID.getValue());
			
			builder.append(" AIMS.").append(schema).append(".repository a");
			builder.append(" join AIMS.").append(schema).append(".asset_category b on a.asset_category_id=b.id and b.asset_type_id in (7,8)");
			builder.append(" left join AIMS.").append(schema).append(".storehous c on a.storehous_id=c.id ");
			builder.append(" left join (select sn,max(id) as History_ID");
			builder.append(" from AIMS.").append(schema).append(".repository_history");
			builder.append(" where status_id=6 and");
			builder.append(" dtid is not null and ");
			builder.append(" dtid<>'' and");
			builder.append(" valid=1 and ");
			builder.append(" ((owner not in ('三川行','其他','台新銀行')) or (owner='台新銀行' and customer<>'台新銀行'))");
			builder.append(" group by sn) d on a.sn=d.sn");
			sqlStatement.addFromExpression(builder.toString());
			
			builder.delete(0, builder.length());
			builder.append(" a.status_id=6 and"); 
			builder.append(" a.dtid is not null and"); 
			builder.append(" a.dtid<>'' and ");
			builder.append(" a.valid=1 and ");
			builder.append(" ((a.owner not in ('三川行','其他','台新銀行')) or (a.owner='台新銀行' and a.customer<>'台新銀行'))");
			sqlStatement.addWhereClause(builder.toString());
			
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseAssetLinkDTO.class);
			LOGGER.debug("listAssetForCase()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return result;
		} catch (Exception e) {
			LOGGER.error("listAssetForCase()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#listProblemReasonData()
	 */
	@Override
	public List<BaseParameterItemDefDTO> listProblemReasonData() throws DataAccessException {
		long startTime = System.currentTimeMillis();
		List<BaseParameterItemDefDTO> result = null;
		//得到schema
		String schema = this.getSchema("iatoms-foms");
		SqlStatement sqlStatement = new SqlStatement();
		StringBuilder builder = new StringBuilder();
		try {
			builder.append("case when a.identifier=1 then 'PROBLEM_REASON_EDC'");
			builder.append(" when a.identifier=2 then 'PROBLEM_REASON_LINE'");
			builder.append(" when a.identifier=3 then 'PROBLEM_REASON_MER'");
			builder.append(" when a.identifier=4 then 'PROBLEM_REASON_OTHER'");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), BaseParameterItemDefDTO.ATTRIBUTE.BPTD_CODE.getValue());
			
			sqlStatement.addSelectClause("b.questionDescription", BaseParameterItemDefDTO.ATTRIBUTE.ITEM_NAME.getValue());
			
			builder.delete(0, builder.length());
			builder.append(schema).append(".CSM_QuestionAnalyse a ");
			builder.append(" join ").append(schema).append(".CSM_QuestionAnalyse b on a.identifier=b.parentId ");
			builder.append(" join ").append(schema).append(".CORE_OPTION c on b.QUESTIONMODULE_TYPE_OPTION_ID=c.ID ");
			sqlStatement.addFromExpression(builder.toString());
			
			builder.delete(0, builder.length());
			builder.append(" a.serviceType=1 and ");
			builder.append(" a.parentId is null and ");
			builder.append(" a.deleted=0 and ");
			builder.append(" b.deleted=0 and ");
			builder.append(" b.QUESTIONMODULE_TYPE_OPTION_ID=9235 ");
			sqlStatement.addWhereClause(builder.toString());
			
			sqlStatement.setOrderByExpression("b.displayOrder");
			
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(BaseParameterItemDefDTO.class);
			LOGGER.debug("listProblemReasonData()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			
			long endTime = System.currentTimeMillis();
			LOGGER.debug("listProblemReasonData()", "testSpeedTime DAO totalTime:" + (endTime - startTime));
			
			return result;
		} catch (Exception e) {
			LOGGER.error("listProblemReasonData()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#listProblemSolutionData()
	 */
	@Override
	public List<BaseParameterItemDefDTO> listProblemSolutionData() throws DataAccessException {
		long startTime = System.currentTimeMillis();
		List<BaseParameterItemDefDTO> result = null;
		//得到schema
		String schema = this.getSchema("iatoms-foms");
		SqlStatement sqlStatement = new SqlStatement();
		StringBuilder builder = new StringBuilder();
		try {
			builder.append("case when a.identifier=1 then 'PROBLEM_SOLUTION_EDC'");
			builder.append(" when a.identifier=2 then 'PROBLEM_SOLUTION_LINE'");
			builder.append(" when a.identifier=3 then 'PROBLEM_SOLUTION_MER'");
			builder.append(" when a.identifier=4 then 'PROBLEM_SOLUTION_OTHER'");
			builder.append(" else null end");
			sqlStatement.addSelectClause(builder.toString(), BaseParameterItemDefDTO.ATTRIBUTE.BPTD_CODE.getValue());
			
			sqlStatement.addSelectClause("b.questionDescription", BaseParameterItemDefDTO.ATTRIBUTE.ITEM_NAME.getValue());
			
			builder.delete(0, builder.length());
			builder.append(schema).append(".CSM_QuestionAnalyse a ");
			builder.append(" join ").append(schema).append(".CSM_QuestionAnalyse b on a.identifier=b.parentId ");
			builder.append(" join ").append(schema).append(".CORE_OPTION c on b.QUESTIONMODULE_TYPE_OPTION_ID=c.ID ");
			sqlStatement.addFromExpression(builder.toString());
			
			builder.delete(0, builder.length());
			builder.append(" a.serviceType=1 and ");
			builder.append(" a.parentId is null and ");
			builder.append(" a.deleted=0 and ");
			builder.append(" b.deleted=0 and ");
			builder.append(" b.QUESTIONMODULE_TYPE_OPTION_ID=9236 ");
			sqlStatement.addWhereClause(builder.toString());
			
			sqlStatement.setOrderByExpression("b.displayOrder");
			
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(BaseParameterItemDefDTO.class);
			LOGGER.debug("listProblemSolutionData()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			
			long endTime = System.currentTimeMillis();
			LOGGER.debug("listProblemSolutionData()", "testSpeedTime DAO totalTime:" + (endTime - startTime));
			
			return result;
		} catch (Exception e) {
			LOGGER.error("listProblemSolutionData()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#testSpeed()
	 */
	@Override
	public List<BimCalendarDayDTO> testSpeed() throws DataAccessException {
		long startTime = System.currentTimeMillis();
		List<BimCalendarDayDTO> result = null;
		//得到schema
		String schema = this.getSchema("iatoms-foms");
		SqlStatement sqlStatement = new SqlStatement();
		StringBuilder builder = new StringBuilder();
		try {
			// 日期
			sqlStatement.addSelectClause("top 100 a.date", BimCalendarDayDTO.ATTRIBUTE.DAY.getValue());
			// 是否為假日
		//	sqlStatement.addSelectClause("case when a.Special=0 then 'N' else 'Y' end", BimCalendarDayDTO.ATTRIBUTE.IS_HOLIDAY.getValue());
			sqlStatement.addSelectClause("case when a.Special is null then 'N' else 'Y' end", BimCalendarDayDTO.ATTRIBUTE.IS_HOLIDAY.getValue());
			// 說明
			sqlStatement.addSelectClause("a.remark", BimCalendarDayDTO.ATTRIBUTE.COMMENT.getValue());
			// 新增人員編號
			sqlStatement.addSelectClause("a.MODIFYINFO_ADD_USER_ID", BimCalendarDayDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			// 新增人員姓名
			sqlStatement.addSelectClause("b.name", BimCalendarDayDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			// 新增日期
			sqlStatement.addSelectClause("a.MODIFYINFO_ADD_DATE", BimCalendarDayDTO.ATTRIBUTE.CREATED_DATE.getValue());
			// 異動人員編號
			sqlStatement.addSelectClause("a.MODIFYINFO_MODIFY_USER_ID", BimCalendarDayDTO.ATTRIBUTE.UPDATED_BY_ID.getValue());
			// 異動人員姓名
			sqlStatement.addSelectClause("c.name", BimCalendarDayDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			// 異動日期
			sqlStatement.addSelectClause("a.MODIFYINFO_MODIFY_DATE", BimCalendarDayDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			
			builder.append(schema).append(".CCM_Holiday a left join ");
			builder.append(schema).append(".ORG_PARTY b on a.MODIFYINFO_ADD_USER_ID=cast(b.[identifier] as nvarchar) left join ");
			builder.append(schema).append(".ORG_PARTY c on a.MODIFYINFO_MODIFY_USER_ID=cast(c.[identifier] as nvarchar)");
			sqlStatement.addFromExpression(builder.toString());
			
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(BimCalendarDayDTO.class);
			LOGGER.debug("testSpeed()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			
			long endTime = System.currentTimeMillis();
			LOGGER.debug("testSpeed", "testSpeedTime DAO totalTime:" + (endTime - startTime));
			
			return result;
		} catch (Exception e) {
			LOGGER.error("testSpeed()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO#queryEnableCaseNewLik()
	 */
	@Override
	public int queryEnableCaseNewLik() throws DataAccessException {
		int count = 0;
		//得到schema
		String fomschema = this.getSchema("iatoms-foms");
		String aimschema = this.getSchema("iatoms-aims");
		try {
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append(" select count(*) ");
			sqlQueryBean.append(" from AIMS.").append(aimschema).append(".repository ");
			sqlQueryBean.append(" where status_id=6 and dtid is not null and dtid<>'' and valid=1 and ((owner not in ('三川行','其他','台新銀行')) or (owner='台新銀行' and customer<>'台新銀行')) ");
			sqlQueryBean.append(" and DTID in ( ");
			sqlQueryBean.append(" select AIMS.dtid ");
			sqlQueryBean.append(" from ").append(fomschema).append(".FOMS_SFM_Request a ");
			sqlQueryBean.append(" join (select bb.dtid,max(aa.identifier) as CaseKey ");
			sqlQueryBean.append(" from ").append(fomschema).append(".FOMS_SFM_Request aa ");
			sqlQueryBean.append(" join ").append(fomschema).append(".FOMS_EDC_DEVICE bb on aa.identifier=bb.requestId  ");
			sqlQueryBean.append(" where aa.corporationId in (select identifier from ").append(fomschema).append(".CUM_CORPORATION where serviceType=1 and compShortCode not in ('TSB-EDC')) and ");
			sqlQueryBean.append(" aa.status in ('StatusComplete','StatusEnd') ");
			sqlQueryBean.append(" group by bb.dtid) a1 on a.identifier=a1.CaseKey ");
			sqlQueryBean.append(" join (select dtid ");
			sqlQueryBean.append(" from AIMS.").append(aimschema).append(".repository ");
			sqlQueryBean.append(" where status_id=6 and dtid is not null and dtid<>'' and valid=1 and ((owner not in ('三川行','其他','台新銀行')) or (owner='台新銀行' and customer<>'台新銀行')) ");
			sqlQueryBean.append(" group by dtid) AIMS on a1.dtid=AIMS.dtid ");
			sqlQueryBean.append(" join ").append(fomschema).append(".CUM_CORPORATION b on a.corporationId=b.identifier ");
			sqlQueryBean.append(" join ").append(fomschema).append(".FOMS_EDC_DEVICE c on a.identifier=c.requestId) ");
			LOGGER.debug("queryEnableCaseNewLik()", "sql:" + sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				count = result.get(0).intValue();
			}
			return count;
		} catch (Exception e) {
			LOGGER.error("queryEnableCaseNewLik()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
}