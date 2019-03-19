package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Message;
import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetInInfoDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IAssetInInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetInInfo;
/**
 * 
 * Purpose:設備入庫主檔DAO 
 * @author CarrieDuan
 * @since  JDK 1.6
 * @date   2016/7/5
 * @MaintenancePersonnel CarrieDuan
 */
public class AssetInInfoDAO extends GenericBaseDAO<DmmAssetInInfo> implements
		IAssetInInfoDAO {
	/**
	 * 
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(AssetInInfoDAO.class);
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetInInfoDAO#listAssetInId(java.lang.Boolean)
	 */
	public List<Parameter> listAssetInId(String isDone) throws DataAccessException {
		List<Parameter> returnList = null; 
		//是否入庫
		LOGGER.debug(".listAssetInId().Parameter---> isDone:", isDone);
		try {
			//獲取schame
			String schame = this.getMySchema();
			//拼接sql
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("a.ASSET_IN_ID", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("a.ASSET_IN_ID", Parameter.FIELD_NAME);
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schame).append(".DMM_ASSET_IN_INFO a");
			sqlStatement.addFromExpression(fromBuffer.toString());
	//		sqlStatement.addWhereClause("a.DELETED = :deleted", IAtomsConstants.NO);
			//條件---是否入庫
			if (StringUtils.hasText(isDone)) {
				if (IAtomsConstants.YES.equals(isDone)) {
					sqlStatement.addWhereClause("a.IS_DONE = :isDone", isDone);
				} else {
					if (isDone != null) {
						sqlStatement.addWhereClause("a.IS_DONE = :isDone", isDone);
					}
				}
			}
			sqlStatement.setOrderByExpression("a.ASSET_IN_ID DESC");
			SqlQueryBean sql = sqlStatement.createSqlQueryBean();
			/*SqlQueryBean sql = new SqlQueryBean();
			sql.append("SELECT a.ASSET_IN_ID as value, a.ASSET_IN_ID as name ");
			sql.append(" FROM " + schame + ".DMM_ASSET_IN_INFO a");
			sql.append("where (a.DELETED = :deleted or a.DELETED is null)");
			sql.setParameter(AssetInInfoDTO.ATTRIBUTE.DELETED.getValue(), IAtomsConstants.NO);
			//條件---是否入庫
			if (StringUtils.hasText(isDone)) {
				if (IAtomsConstants.YES.equals(isDone)) {
					sql.append("and a.IS_DONE = :isDone");
				} else {
					sql.append("and (a.IS_DONE = :isDone or a.IS_DONE is null)");
				}
				//設置參數
				sql.setParameter(AssetInInfoDTO.ATTRIBUTE.IS_DONE.getValue(),isDone);
			}
			sql.append(" order by a.ASSET_IN_ID DESC");*/
			//aliasBean
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			//記錄sql
			LOGGER.debug(".listAssetInId()---SQL---> ", sql.toString());
			//查詢
			returnList = this.getDaoSupport().findByNativeSql(sql, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listAssetInId()", "Service Exception", e);
			throw new DataAccessException(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.QUERY_FAILURE));
		}
		return returnList;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetInInfoDAO#getCount(java.lang.String, java.lang.String)
	 */
	public Integer getCount(String queryContractId, String queryAssetInId, String queryCompanyId) throws DataAccessException {
		//返回總條數
		Integer count = null;
		//查詢待入庫資料時，入庫批號為空不做查詢
		try {
			String schema = this.getMySchema();	
			//查询总条数
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("COUNT(1)");
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".DMM_ASSET_IN_INFO ain");
			fromBuffer.append(" inner join ").append(schema).append(".DMM_ASSET_IN_LIST_HISTORY ail on ail.ASSET_IN_ID = ain.ASSET_IN_ID");
			sqlStatement.addFromExpression(fromBuffer.toString());

			if (StringUtils.hasText(queryAssetInId)) {
				sqlStatement.addWhereClause("ain.ASSET_IN_ID = :queryAssetInId", queryAssetInId);
			}
			if (StringUtils.hasText(queryCompanyId)) {
				sqlStatement.addWhereClause("ain.CUSTOMER_ID = :queryCompanyId", queryCompanyId);
			}
			if ( StringUtils.hasText(queryContractId)) {
				sqlStatement.addWhereClause("ain.CONTRACT_ID = :queryContractId", queryContractId);
			}
			sqlStatement.addWhereClause("ain.IS_DONE = :yes", IAtomsConstants.YES);
			
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug("SQL---------->", sqlQueryBean.toString());
			List<Integer> list = super.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(list)){
				count = list.get(0);
			}	
		} catch (Exception e) {
			LOGGER.error("getCount()", "Service Exception", e);
			throw new DataAccessException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return count;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetInInfoDAO#listAssetInInfoDTO(java.lang.String, java.lang.String, java.lang.String, java.lang.String, Integer, Integer)
	 */
	public List<AssetInInfoDTO> listAssetInInfoDTO(String queryContractId, String queryAssetInId, String queryCompanyId, String order, String sort, Integer page, Integer rows)
			throws DataAccessException {
		//返回入庫集合
		List<AssetInInfoDTO> list = null;
		try {
			//schema
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("ail.ASSET_IN_ID", AssetInInfoDTO.ATTRIBUTE.ASSET_IN_ID.getValue());
			sqlStatement.addSelectClause("ail.SERIAL_NUMBER", AssetInInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("ail.PROPERTY_ID", AssetInInfoDTO.ATTRIBUTE.PROPERTY_ID.getValue());
			sqlStatement.addSelectClause("ail.IS_CHECKED", AssetInInfoDTO.ATTRIBUTE.IS_CHECKED.getValue());
			sqlStatement.addSelectClause("ain.CONTRACT_ID", AssetInInfoDTO.ATTRIBUTE.CONTRACT_ID.getValue());
			sqlStatement.addSelectClause("contract.CONTRACT_CODE", AssetInInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue());
			sqlStatement.addSelectClause("ain.KEEPER_NAME", AssetInInfoDTO.ATTRIBUTE.KEEPER_NAME.getValue());
			sqlStatement.addSelectClause("ain.MA_TYPE", AssetInInfoDTO.ATTRIBUTE.MA_TYPE.getValue());
			sqlStatement.addSelectClause("base.ITEM_NAME", AssetInInfoDTO.ATTRIBUTE.MA_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("ail.IS_CUSTOMER_CHECKED", AssetInInfoDTO.ATTRIBUTE.IS_CUSTOMER_CHECKED.getValue());
			sqlStatement.addSelectClause("ain.ASSET_TYPE_ID", AssetInInfoDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			sqlStatement.addSelectClause("ain.UPDATED_BY_NAME", AssetInInfoDTO.ATTRIBUTE.UPDATE_USER_NAME.getValue());
			sqlStatement.addSelectClause("ain.UPDATED_DATE", AssetInInfoDTO.ATTRIBUTE.UPDATE_DATE.getValue());
			sqlStatement.addSelectClause("ain.Customer_Approve_Date", AssetInInfoDTO.ATTRIBUTE.CUSTOME_APPROVE_DATE.getValue());
			sqlStatement.addSelectClause("ain.FACTORY_WARRANTY_DATE", AssetInInfoDTO.ATTRIBUTE.FACTORY_WARRANTY_DATE.getValue());
			sqlStatement.addSelectClause("ain.CUSTOMER_WARRANTY_DATE", AssetInInfoDTO.ATTRIBUTE.CUSTOMER_WARRANTY_DATE.getValue());
			sqlStatement.addSelectClause("company.short_name + '-' +warehouse.NAME", AssetInInfoDTO.ATTRIBUTE.WAREHOUSE_NAME.getValue());
			sqlStatement.addSelectClause("type.NAME", AssetInInfoDTO.ATTRIBUTE.ASSET_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("STUFF((SELECT ','+ ltrim(company.company_id) FROM dbo.BIM_COMPANY company,dbo.BIM_CONTRACT_VENDOR vendor"
					.concat(" WHERE company.COMPANY_ID=vendor.COMPANY_ID and vendor.CONTRACT_ID=ain.CONTRACT_ID FOR XML PATH('')), 1, 1, '')"), AssetInInfoDTO.ATTRIBUTE.VENDOR.getValue());
			sqlStatement.addSelectClause("ain.WAREHOUSE_ID", AssetInInfoDTO.ATTRIBUTE.WAREHOUSE_ID.getValue());
			sqlStatement.addSelectClause("ain.USER_ID", AssetInInfoDTO.ATTRIBUTE.USE_EMPLOYEE_NAME.getValue());
			sqlStatement.addSelectClause("ain.OWNER", AssetInInfoDTO.ATTRIBUTE.ASSETS_OWNER.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".DMM_ASSET_IN_INFO ain");
			fromBuffer.append(" left join ").append(schema).append(".BIM_CONTRACT contract on contract.CONTRACT_ID = ain.CONTRACT_ID");
			fromBuffer.append(" left join ").append(schema).append(".DMM_ASSET_IN_LIST_HISTORY ail on ail.ASSET_IN_ID = ain.ASSET_IN_ID");
			fromBuffer.append(" left join ").append(schema).append(".DMM_ASSET_TYPE type on type.ASSET_TYPE_ID = ain.ASSET_TYPE_ID");
			fromBuffer.append(" left join ").append(schema).append(".BIM_WAREHOUSE warehouse on warehouse.WAREHOUSE_ID = ain.WAREHOUSE_ID");
			fromBuffer.append(" left join ").append(schema).append(".BIM_COMPANY company on warehouse.COMPANY_ID = company.COMPANY_ID");
			fromBuffer.append(" left join ").append(schema).append(".BIM_COMPANY_TYPE companyType ON companyType.COMPANY_ID = company.COMPANY_ID");
			fromBuffer.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF base on base.BPTD_CODE = :maType and base.ITEM_VALUE = ain.MA_TYPE");
			sqlStatement.addFromExpression(fromBuffer.toString());
			/*sqlStatement.addFromExpression(schema + ".DMM_ASSET_IN_INFO ain" 
					+ " left join " + schema + ".BIM_CONTRACT contract on contract.CONTRACT_ID = ain.CONTRACT_ID"
					+ " left join " + schema + ".DMM_ASSET_IN_LIST ail on ail.ASSET_IN_ID = ain.ASSET_IN_ID"
					+ " left join " + schema + ".DMM_ASSET_TYPE type on type.ASSET_TYPE_ID = ain.ASSET_TYPE_ID"
					+ " left join " + schema + ".BASE_PARAMETER_ITEM_DEF base on base.BPTD_CODE = :maType and base.ITEM_VALUE = ain.MA_TYPE");*/
			
			if (StringUtils.hasText(queryAssetInId)) {
				sqlStatement.addWhereClause("ail.ASSET_IN_ID = :queryAssetInId", queryAssetInId);
			}
			if (StringUtils.hasText(queryCompanyId)) {
				sqlStatement.addWhereClause("ain.CUSTOMER_ID = :queryCompanyId", queryCompanyId);
			}
			if (StringUtils.hasText(queryContractId)) {
				sqlStatement.addWhereClause("ain.CONTRACT_ID = :queryContractId", queryContractId);
			}
	//		sqlStatement.addWhereClause("ain.DELETED = :no", IAtomsConstants.NO);
			sqlStatement.addWhereClause("ain.IS_DONE = :yes", IAtomsConstants.YES);
			sqlStatement.addWhereClause("companyType.COMPANY_TYPE =:type", IAtomsConstants.PARAM_COMPANY_TYPE_VENDOR);
			if (StringUtils.hasText(sort) && StringUtils.hasText(order)) {
				sqlStatement.setOrderByExpression(sort.concat(" ").concat(order));
			}
			sqlStatement.setPageSize(rows.intValue());
			sqlStatement.setStartPage((page.intValue()) - 1);
			AliasBean aliasBean = sqlStatement.createAliasBean(AssetInInfoDTO.class);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter(AssetInInfoDTO.ATTRIBUTE.MA_TYPE.getValue(), IAtomsConstants.PARAM_BPTD_CODE_MA_TYPE);
			//查詢
			list = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listAssetInInfoDTO()", "Service Exception", e);
			throw new DataAccessException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
 		return list;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetInInfoDAO#updateCyberApprovedDate(java.lang.String, java.sql.Timestamp)
	 */
	public void updateCyberApprovedDate(String assetId, Timestamp cyberApprovedDate, IAtomsLogonUser logonUser) throws DataAccessException {
		try {
			String schema = this.getMySchema();
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("update".concat(schema).concat(".DMM_ASSET_IN_INFO set"));
			if (cyberApprovedDate != null) {
				sqlQueryBean.append(" CYBER_APPROVED_DATE = :cyberApprovedDate").append(IAtomsConstants.MARK_SEPARATOR);
			}
			sqlQueryBean.append(" UPDATED_BY_ID = :updateUser").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(" UPDATED_BY_NAME = :updateName").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(" UPDATED_DATE = :updateDate");
			sqlQueryBean.append(" where ASSET_IN_ID = (select repository.ASSET_IN_ID from ".concat(schema).concat(".DMM_REPOSITORY repository where repository.ASSET_ID = :assetId)"));
			sqlQueryBean.setParameter("cyberApprovedDate", cyberApprovedDate);
			sqlQueryBean.setParameter("updateUser", logonUser.getId());
			sqlQueryBean.setParameter("updateName", logonUser.getName());
			sqlQueryBean.setParameter("updateDate", DateTimeUtils.getCurrentTimestamp());
			sqlQueryBean.setParameter("assetId", assetId);
			LOGGER.debug("updateCyberApprovedDate ---->sql:", sqlQueryBean.toString() );
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
		} catch (Exception e) {
			LOGGER.error("updateCyberApprovedDate()", "Service Exception", e);
			throw new DataAccessException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetInInfoDAO#listAssetInInfoDTOByIds(java.util.List)
	 */
	public List<AssetInInfoDTO> listAssetInInfoDTOByIds(List<String> assetInIds)throws DataAccessException {
		List<AssetInInfoDTO> assetInInfoDTOs = null;
		try{
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("info.ASSET_IN_ID", AssetInInfoDTO.ATTRIBUTE.ASSET_IN_ID.getValue());
			sqlStatement.addSelectClause("info.CYBER_APPROVED_DATE", AssetInInfoDTO.ATTRIBUTE.CYBER_APPROVE_DATE.getValue());
			sqlStatement.addSelectClause("info.CUSTOMER_APPROVE_DATE", AssetInInfoDTO.ATTRIBUTE.CUSTOME_APPROVE_DATE.getValue());
			sqlStatement.addSelectClause("info.MA_TYPE", AssetInInfoDTO.ATTRIBUTE.MA_TYPE.getValue());
			sqlStatement.addFromExpression(schema + ".DMM_ASSET_IN_INFO info");
			if (!CollectionUtils.isEmpty(assetInIds)){
				sqlStatement.addWhereClause("info.ASSET_IN_ID in ( :assetInId )");
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if (!CollectionUtils.isEmpty(assetInIds)){
				sqlQueryBean.setParameter(AssetInInfoDTO.ATTRIBUTE.ASSET_IN_ID.getValue(), assetInIds);
			}
			AliasBean aliasBean = sqlStatement.createAliasBean(AssetInInfoDTO.class);
			assetInInfoDTOs = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return assetInInfoDTOs;
		}catch (Exception e) {
			LOGGER.error(".listContractDTOByIds() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAssetInInfoDAO#deleteAssetIn(java.util.Date)
	 */
	@Override
	public void deleteAssetIn(Date deleteDate) throws DataAccessException {
		try {
			// 沒傳入日期不處理
			if(deleteDate != null){
				String schema = this.getMySchema();
				SqlQueryBean sqlQueryBean = new SqlQueryBean("delete ");
				sqlQueryBean.append(schema).append(".DMM_ASSET_IN_LIST_HISTORY ");
				// 刪除設定日期之前的所有設備入庫信息
				sqlQueryBean.append(" where UPDATED_DATE<= :deleteDate");
				
				sqlQueryBean.append("delete ");
				sqlQueryBean.append(schema).append(".DMM_ASSET_IN_INFO ");
				// 刪除設定日期之前的所有設備入庫信息
				sqlQueryBean.append(" where UPDATED_DATE<= :deleteDate");
				
				sqlQueryBean.setParameter("deleteDate", deleteDate);
				LOGGER.debug("deleteAssetIn()", "sql:" + sqlQueryBean.toString());
				this.getDaoSupport().updateByNativeSql(sqlQueryBean);
				this.getDaoSupport().flush();
			}
		} catch (Exception e) {
			LOGGER.error("deleteAssetIn()", e.getMessage(), e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
	}
}
