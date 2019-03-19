package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimMerchantHeaderDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IMerchantHeaderDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimMerchantHeader;
/**
 * Purpose: 特店表頭維護DAO
 * @author echomou
 * @since  JDK 1.6
 * @date   2016-6-20
 * @MaintenancePersonnel echomou
 */
@SuppressWarnings("unchecked")
public class MerchantHeaderDAO extends GenericBaseDAO<BimMerchantHeader> implements IMerchantHeaderDAO{
	/**
	 * 系统日志记录物件 
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(MerchantHeaderDAO.class);
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IMerchantHeaderDAO#listby(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<BimMerchantHeaderDTO> listby(String queryCustomerId, String queryMerCode, String queryStagesMerCode, String queryName, String queryHeaderName, String businessAddress, String queryIsVip, int currentPage, int pageSize, boolean isPage, String sort, String order, String merchantHeaderId, String merchentId) throws DataAccessException {
		LOGGER.debug(this.getClass().getName() + "queryCustomerId" + queryCustomerId);
		LOGGER.debug(this.getClass().getName() + "queryMerCode" + queryMerCode);
		LOGGER.debug(this.getClass().getName() + "queryStagesMerCode" + queryStagesMerCode);
		LOGGER.debug(this.getClass().getName() + "queryName" + queryName);
		LOGGER.debug(this.getClass().getName() + "queryIsVip" + queryIsVip);
		LOGGER.debug(this.getClass().getName() + "queryheaderName" + queryHeaderName);
		LOGGER.debug(this.getClass().getName() + "merchantHeaderId" + merchantHeaderId);
		LOGGER.debug(this.getClass().getName() + "merchentId" + merchentId);
		List<BimMerchantHeaderDTO> listHeader = null;
		String schema = this.getMySchema();
		SqlStatement sqlStatement = new SqlStatement();
		try {
			sqlStatement.addSelectClause("company.COMPANY_ID", BimMerchantHeaderDTO.ATTRIBUTE.COMPANY_ID.getValue());
			sqlStatement.addSelectClause("company.SHORT_NAME", BimMerchantHeaderDTO.ATTRIBUTE.CUSTOMER_NAME.getValue());
			sqlStatement.addSelectClause("header.MERCHANT_HEADER_ID", BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_HEADER_ID.getValue());
			sqlStatement.addSelectClause("merchant.MERCHANT_CODE", BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			//Task #3249
			sqlStatement.addSelectClause("merchant.UNITY_NUMBER", BimMerchantHeaderDTO.ATTRIBUTE.UNITY_NUMBER.getValue());
			sqlStatement.addSelectClause("merchant.NAME", BimMerchantHeaderDTO.ATTRIBUTE.NAME.getValue());
			sqlStatement.addSelectClause("header.IS_VIP", BimMerchantHeaderDTO.ATTRIBUTE.IS_VIP.getValue());
			sqlStatement.addSelectClause("header.AREA", BimMerchantHeaderDTO.ATTRIBUTE.AREA.getValue());
			sqlStatement.addSelectClause("base.ITEM_NAME", BimMerchantHeaderDTO.ATTRIBUTE.AREA_NAME.getValue());
			sqlStatement.addSelectClause("base.ITEM_NAME", BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_AREA_NAME.getValue());
			sqlStatement.addSelectClause("header.HEADER_NAME", BimMerchantHeaderDTO.ATTRIBUTE.HEADER_NAME.getValue());
			sqlStatement.addSelectClause("header.CONTACT", BimMerchantHeaderDTO.ATTRIBUTE.CONTACT.getValue());
			sqlStatement.addSelectClause("header.CONTACT_TEL", BimMerchantHeaderDTO.ATTRIBUTE.CONTACT_TEL.getValue());
			sqlStatement.addSelectClause("header.CONTACT_TEL2", BimMerchantHeaderDTO.ATTRIBUTE.CONTACT_TEL2.getValue());
			sqlStatement.addSelectClause("header.CONTACT_EMAIL", BimMerchantHeaderDTO.ATTRIBUTE.CONTACT_EMAIL.getValue());
			sqlStatement.addSelectClause("header.PHONE", BimMerchantHeaderDTO.ATTRIBUTE.PHONE.getValue());
			sqlStatement.addSelectClause("header.LOCATION", BimMerchantHeaderDTO.ATTRIBUTE.LOCATION.getValue());
			sqlStatement.addSelectClause("parameter.ITEM_NAME", BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_LOCATION_NAME.getValue());
			sqlStatement.addSelectClause("header.BUSINESS_ADDRESS", BimMerchantHeaderDTO.ATTRIBUTE.BUSINESS_ADDRESS.getValue());
			sqlStatement.addSelectClause("header.MERCHANT_ID", BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_ID.getValue());
			sqlStatement.addSelectClause("header.OPEN_HOUR", BimMerchantHeaderDTO.ATTRIBUTE.OPEN_HOUR.getValue());
			sqlStatement.addSelectClause("header.CLOSE_HOUR", BimMerchantHeaderDTO.ATTRIBUTE.CLOSE_HOUR.getValue());
			sqlStatement.addSelectClause("header.AO_NAME", BimMerchantHeaderDTO.ATTRIBUTE.AO_NAME.getValue());
			sqlStatement.addSelectClause("header.AOEMAIL", BimMerchantHeaderDTO.ATTRIBUTE.AO_EMAIL.getValue());
			sqlStatement.addSelectClause("merchant.REMARK", BimMerchantHeaderDTO.ATTRIBUTE.REMARK.getValue());
			sqlStatement.addSelectClause("postCode.POST_NAME", BimMerchantHeaderDTO.ATTRIBUTE.POST_NAME.getValue());
			sqlStatement.addSelectClause("postCode.POST_CODE", BimMerchantHeaderDTO.ATTRIBUTE.POST_CODE.getValue());
			sqlStatement.addSelectClause("postCode.POST_CODE_ID", BimMerchantHeaderDTO.ATTRIBUTE.POST_CODE_ID.getValue());
			sqlStatement.addFromExpression(schema + ".BIM_MERCHANT_HEADER header inner join " + schema + ".BIM_MERCHANT merchant on header.MERCHANT_ID = merchant.MERCHANT_ID" +
			" left join " + schema + ".BIM_COMPANY company on merchant.COMPANY_ID = company.COMPANY_ID" +
			" left join " + schema + ".BASE_PARAMETER_ITEM_DEF base on base.BPTD_CODE = :area AND base.ITEM_VALUE = header.AREA" + 
			" left join " + schema + ".BASE_PARAMETER_ITEM_DEF parameter on parameter.BPTD_CODE = :location AND parameter.ITEM_VALUE = header.LOCATION" +
			" left join " + schema + ".BASE_PARAMETER_POST_CODE postCode on postCode.POST_CODE_ID = header.POST_CODE_ID");
			if (StringUtils.hasText(merchentId)) {
				sqlStatement.addWhereClause("header.MERCHANT_ID  = :merchentId", merchentId);
			}
			if (StringUtils.hasText(queryHeaderName)) {
				sqlStatement.addWhereClause("header.HEADER_NAME like :queryHeaderName", queryHeaderName + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(queryCustomerId)) {
				sqlStatement.addWhereClause("merchant.COMPANY_ID = :customerId", queryCustomerId);
			}
			if (StringUtils.hasText(businessAddress)) {
				sqlStatement.addWhereClause("header.BUSINESS_ADDRESS like :businessAddress", businessAddress + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(queryMerCode)) {
				sqlStatement.addWhereClause("merchant.MERCHANT_CODE like :queryMerCode", queryMerCode + IAtomsConstants.MARK_PERCENT);
			}
			/*if (StringUtils.hasText(queryStagesMerCode)) {
				sqlStatement.addWhereClause("merchant.STAGES_MERCHANT_CODE like :queryStagesMerCode", queryStagesMerCode + IAtomsConstants.MARK_PERCENT);
			}*/
			if (StringUtils.hasText(queryIsVip)) {
				sqlStatement.addWhereClause("header.IS_VIP = :queryVipId", queryIsVip);
			}
			if (StringUtils.hasText(queryName)) {
				sqlStatement.addWhereClause("merchant.NAME like :queryName", queryName + IAtomsConstants.MARK_PERCENT);
			}			
			
			if (isPage) {
				sqlStatement.setPageSize(pageSize);
				sqlStatement.setStartPage(currentPage - 1);
			}
			if (StringUtils.hasText(merchantHeaderId)) {
				sqlStatement.addWhereClause("header.MERCHANT_HEADER_ID = :merchantHeaderId", merchantHeaderId);
			}
			sqlStatement.addWhereClause(" isnull(header.DELETED,'N') =:deleted", IAtomsConstants.NO);
			//設置排序表達式
			if (!StringUtils.hasText(sort) && !StringUtils.hasText(order)) {
				sqlStatement.setOrderByExpression("company.SHORT_NAME, merchant.MERCHANT_CODE asc");
			} else if (StringUtils.hasText(sort) && StringUtils.hasText(order)) {
				sqlStatement.setOrderByExpression(sort + IAtomsConstants.MARK_SPACE + order);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter(BimMerchantHeaderDTO.ATTRIBUTE.AREA.getValue(), IATOMS_PARAM_TYPE.REGION.getCode());
			sqlQueryBean.setParameter(BimMerchantHeaderDTO.ATTRIBUTE.LOCATION.getValue(), IATOMS_PARAM_TYPE.LOCATION.getCode());
			AliasBean aliasBean = sqlStatement.createAliasBean(BimMerchantHeaderDTO.class);
			LOGGER.debug(this.getClass().getName() + "SQL---------->" + sqlQueryBean.toString());
			listHeader = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".listby() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return listHeader;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IMerchantHeaderDAO#getCount(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Integer getCount(String queryCustomerId, String queryMerCode, String queryName, String queryHeaderName, String businessAddress, String queryIsVip) throws DataAccessException {
		LOGGER.debug(this.getClass().getName() + "queryCustomerId" + queryCustomerId);
		LOGGER.debug(this.getClass().getName() + "queryMerCode" + queryMerCode);
		LOGGER.debug(this.getClass().getName() + "queryName" + queryName);
		LOGGER.debug(this.getClass().getName() + "queryIsVip" + queryIsVip);
		LOGGER.debug(this.getClass().getName() + "queryHeaderName" + queryHeaderName);
		String schema = this.getMySchema();	
		SqlStatement sqlStatement = new SqlStatement();
		try {
			sqlStatement.addSelectClause("count(*)");
			sqlStatement.addFromExpression(schema + ".BIM_MERCHANT_HEADER header inner join " + schema + ".BIM_MERCHANT merchant on header.MERCHANT_ID = merchant.MERCHANT_ID" + 
					" left join " + schema + ".BIM_COMPANY company on merchant.COMPANY_ID = company.COMPANY_ID" +
					" left join " + schema + ".BASE_PARAMETER_ITEM_DEF base on base.BPTD_CODE = :area AND base.ITEM_VALUE = header.AREA");
			if (StringUtils.hasText(queryCustomerId)) {
				sqlStatement.addWhereClause("merchant.COMPANY_ID = :customerId", queryCustomerId);
			}	
			if (StringUtils.hasText(businessAddress)) {
				sqlStatement.addWhereClause("header.BUSINESS_ADDRESS like :businessAddress", businessAddress + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(queryMerCode)) {
				sqlStatement.addWhereClause("merchant.MERCHANT_CODE like :queryMerCode", queryMerCode + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(queryIsVip)) {
				sqlStatement.addWhereClause("header.IS_VIP = :queryVipId", queryIsVip);
			}
			if (StringUtils.hasText(queryName)) {
				sqlStatement.addWhereClause("merchant.NAME like :queryName", queryName + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(queryHeaderName)) {
				sqlStatement.addWhereClause("header.HEADER_NAME like :queryHeaderName", queryHeaderName + IAtomsConstants.MARK_PERCENT);
			}
			sqlStatement.addWhereClause(" isnull(header.DELETED,'N') =:deleted", IAtomsConstants.NO);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter(BimMerchantHeaderDTO.ATTRIBUTE.AREA.getValue(), IATOMS_PARAM_TYPE.REGION.getCode());
			LOGGER.debug(this.getClass().getName() + "SQL---------->" + sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			return result.get(0);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".getCount() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IMerchantHeaderDAO#check(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean check(String merchantId, String merchantHeaderName, String merchantHeaderId) throws DataAccessException {
		LOGGER.debug(this.getClass().getName() + "merchantId" + merchantId);
		LOGGER.debug(this.getClass().getName() + "merchantHeaderName" + merchantHeaderName);
		LOGGER.debug(this.getClass().getName() + "merchantHeaderId" + merchantHeaderId);
		String schema = this.getMySchema();	
		SqlStatement sqlStatement = new SqlStatement();
		try {
			//查詢語句
			sqlStatement.addSelectClause("count(*)");
			sqlStatement.addFromExpression(schema + ".BIM_MERCHANT_HEADER header," + schema + ".BIM_MERCHANT merchant");
			sqlStatement.addWhereClause("header.MERCHANT_ID = merchant.MERCHANT_ID");
			if (StringUtils.hasText(merchantId)) {
				sqlStatement.addWhereClause("merchant.MERCHANT_ID = :merchantId", merchantId);
			}
			if (StringUtils.hasText(merchantHeaderName)) {
				sqlStatement.addWhereClause("header.HEADER_NAME = :merchantHeaderName", merchantHeaderName);
			}
			if (StringUtils.hasText(merchantHeaderId)) {
				sqlStatement.addWhereClause("header.MERCHANT_HEADER_ID <> :merchantHeaderId", merchantHeaderId);
			}
			sqlStatement.addWhereClause(" isnull(header.DELETED,'N') =:deleted", IAtomsConstants.NO);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			//打印sql語句
			LOGGER.debug(this.getClass().getName() + "SQL---------->" + sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (result.get(0).intValue() > 0) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".check() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return false;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IMerchantHeaderDAO#getMerchantHeaders(java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<Parameter> getMerchantHeaders(String customerId, String merchantCode, String merchantId) throws DataAccessException {
		LOGGER.debug(this.getClass().getName() + "merchantId" + merchantId);
		String schema = this.getMySchema();	
		SqlStatement sqlStatement = new SqlStatement();
		List<Parameter> merchantHeaders = null;
		try {
			//查詢語句
			sqlStatement.addSelectClause("header.HEADER_NAME", Parameter.FIELD_NAME);
			sqlStatement.addSelectClause("header.MERCHANT_HEADER_ID", Parameter.FIELD_VALUE);
			if (StringUtils.hasText(merchantId)) {
				sqlStatement.addFromExpression( schema + ".BIM_MERCHANT_HEADER header");
				sqlStatement.addWhereClause("header.MERCHANT_ID = :merchantId", merchantId);
			}
			if (StringUtils.hasText(customerId) && StringUtils.hasText(merchantCode)) {
				sqlStatement.addFromExpression( schema + ".BIM_MERCHANT_HEADER header"
						+ " left join " + schema + ".BIM_MERCHANT merchant on merchant.MERCHANT_ID = header.MERCHANT_ID");
				sqlStatement.addWhereClause("merchant.COMPANY_ID = :customerId", customerId);
				sqlStatement.addWhereClause("merchant.MERCHANT_CODE = :merchantCode", merchantCode);
			}
			sqlStatement.addWhereClause(" isnull(header.DELETED,'N') =:deleted", IAtomsConstants.NO);
			sqlStatement.setOrderByExpression("HEADER_NAME");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			//打印sql語句
			LOGGER.debug(this.getClass().getName() + "SQL---------->" + sqlQueryBean.toString());
			AliasBean aliasBean = new AliasBean(Parameter.class);	
			merchantHeaders = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".getMerchantHeaders() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return merchantHeaders;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IMerchantHeaderDAO#getMerchantHeaderDTOBy(java.lang.String, java.lang.String)
	 */
	public BimMerchantHeaderDTO getMerchantHeaderDTOBy(String merchantId, String headerName, String merchantCode) throws DataAccessException {
		LOGGER.debug(this.getClass().getName() + "merchantId" + merchantId);
		LOGGER.debug(this.getClass().getName() + "headerName" + headerName);
		String schema = this.getMySchema();	
		SqlStatement sqlStatement = new SqlStatement();
		try {
			//查詢語句
			sqlStatement.addSelectClause("header.MERCHANT_HEADER_ID", BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_HEADER_ID.getValue());
			sqlStatement.addSelectClause("header.AO_NAME", BimMerchantHeaderDTO.ATTRIBUTE.AO_NAME.getValue());
			sqlStatement.addSelectClause("header.HEADER_NAME", BimMerchantHeaderDTO.ATTRIBUTE.HEADER_NAME.getValue());
			sqlStatement.addSelectClause("header.AREA", BimMerchantHeaderDTO.ATTRIBUTE.AREA.getValue());
			sqlStatement.addSelectClause("header.LOCATION", BimMerchantHeaderDTO.ATTRIBUTE.LOCATION.getValue());
			sqlStatement.addSelectClause("parameter.ITEM_NAME", BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_LOCATION_NAME.getValue());
			sqlStatement.addSelectClause("header.BUSINESS_ADDRESS", BimMerchantHeaderDTO.ATTRIBUTE.BUSINESS_ADDRESS.getValue());
			
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".BIM_MERCHANT_HEADER header ");
			fromBuffer.append("left join ").append(schema).append(".BIM_MERCHANT merchant on header.MERCHANT_ID = merchant.MERCHANT_ID ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF parameter on parameter.BPTD_CODE = :location AND parameter.ITEM_VALUE = header.LOCATION ");
			sqlStatement.addFromExpression(fromBuffer.toString());
			//sqlStatement.addWhereClause("header.MERCHANT_ID = merchant.MERCHANT_ID");
			if (StringUtils.hasText(merchantId)) {
				sqlStatement.addWhereClause("merchant.MERCHANT_ID = :merchantId", merchantId);
			}
			if (StringUtils.hasText(headerName)) {
				sqlStatement.addWhereClause("header.HEADER_NAME = :merchantHeaderName", headerName);
			}
			if (StringUtils.hasText(merchantCode)) {
				sqlStatement.addWhereClause("merchant.MERCHANT_CODE = :merchantCode", merchantCode);
			}
			sqlStatement.addWhereClause(" isnull(header.DELETED,'N') =:deleted", IAtomsConstants.NO);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter(BimMerchantHeaderDTO.ATTRIBUTE.LOCATION.getValue(), IATOMS_PARAM_TYPE.LOCATION.getCode());
			AliasBean aliasBean = sqlStatement.createAliasBean(BimMerchantHeaderDTO.class);
			//打印sql語句
			LOGGER.debug(this.getClass().getName() + "SQL---------->" + sqlQueryBean.toString());
			List<BimMerchantHeaderDTO> result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(result)) {
				return result.get(0);
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".check() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return null;
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IMerchantHeaderDAO#getMerchantHeadersBy(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Parameter> getMerchantHeadersBy(String merchantId, String headerName) throws DataAccessException {
		LOGGER.debug(this.getClass().getName() + ".getMerchantHeadersBy() merchantId" + merchantId);
		LOGGER.debug(this.getClass().getName() + ".getMerchantHeadersBy() headerName" + headerName);
		String schema = this.getMySchema();	
		SqlStatement sqlStatement = new SqlStatement();
		List<Parameter> result = null;
		try {
			//查詢語句
			sqlStatement.addSelectClause("header.MERCHANT_HEADER_ID", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("header.HEADER_NAME", Parameter.FIELD_NAME);
			sqlStatement.addFromExpression(schema + ".BIM_MERCHANT_HEADER header");
			if (StringUtils.hasText(merchantId)) {
				sqlStatement.addWhereClause("header.MERCHANT_ID = :merchantId", merchantId);
			}
			if (StringUtils.hasText(headerName)) {
				sqlStatement.addWhereClause("header.HEADER_NAME = :merchantHeaderName", headerName);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			//打印sql語句
			LOGGER.debug(this.getClass().getName() + ".getMerchantHeadersBy() SQL---------->" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return result;
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".getMerchantHeadersBy() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IMerchantHeaderDAO#getMerchantHeaderList()
	 */
	@Override
	public List<BimMerchantHeaderDTO> getMerchantHeaderList()throws DataAccessException {
		String schema = this.getMySchema();	
		SqlStatement sqlStatement = new SqlStatement();
		List<BimMerchantHeaderDTO> result = null;
		try {
			//查詢語句
			sqlStatement.addSelectClause("header.MERCHANT_HEADER_ID", BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_HEADER_ID.getValue());
			sqlStatement.addSelectClause("header.MERCHANT_ID", BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_ID.getValue());
			sqlStatement.addSelectClause("header.HEADER_NAME", BimMerchantHeaderDTO.ATTRIBUTE.HEADER_NAME.getValue());
			
			sqlStatement.addFromExpression(schema + ".BIM_MERCHANT_HEADER header");
			
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(BimMerchantHeaderDTO.class);
			//打印sql語句
			LOGGER.debug(this.getClass().getName() + ".getMerchantHeadersBy() SQL---------->" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return result;
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".getMerchantHeadersBy() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IMerchantHeaderDAO#getMerchantHeaderById(java.lang.String)
	 */
	@Override
	public BimMerchantHeaderDTO getMerchantHeaderById(String id) throws DataAccessException {
		LOGGER.debug(this.getClass().getName() + ".getMerchantHeaderById() id" + id);
		BimMerchantHeaderDTO headerDTO = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("header.LOCATION", BimMerchantHeaderDTO.ATTRIBUTE.LOCATION.getValue());
			sqlStatement.addSelectClause("itemType.ITEM_NAME", BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_LOCATION_NAME.getValue());
			sqlStatement.addSelectClause("header.CONTACT", BimMerchantHeaderDTO.ATTRIBUTE.CONTACT.getValue());
			sqlStatement.addSelectClause("header.BUSINESS_ADDRESS", BimMerchantHeaderDTO.ATTRIBUTE.BUSINESS_ADDRESS.getValue());
			sqlStatement.addSelectClause("header.CONTACT_EMAIL", BimMerchantHeaderDTO.ATTRIBUTE.CONTACT_EMAIL.getValue());
			sqlStatement.addSelectClause("header.PHONE", BimMerchantHeaderDTO.ATTRIBUTE.PHONE.getValue());
			sqlStatement.addSelectClause("header.CONTACT_TEL", BimMerchantHeaderDTO.ATTRIBUTE.CONTACT_TEL.getValue());
			sqlStatement.addSelectClause("header.POST_CODE_ID", BimMerchantHeaderDTO.ATTRIBUTE.POST_CODE_ID.getValue());
			sqlStatement.addSelectClause("(CONCAT(postCode.POST_CODE  ,'(' ,postCode.POST_NAME ,')'))", BimMerchantHeaderDTO.ATTRIBUTE.POST_NAME.getValue());
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(".BIM_MERCHANT_HEADER header ");
			buffer.append("left join ").append(schema).append(".BASE_PARAMETER_POST_CODE postCode on postCode.POST_CODE_ID = header.POST_CODE_ID ");
			buffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF itemType on itemType.BPTD_CODE = :location and itemType.ITEM_VALUE = header.LOCATION ");
			sqlStatement.addFromExpression(buffer.toString());
			if (StringUtils.hasText(id)) {
				sqlStatement.addWhereClause("header.MERCHANT_HEADER_ID = :id", id);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(BimMerchantHeaderDTO.class);
			sqlQueryBean.setParameter(BimMerchantHeaderDTO.ATTRIBUTE.LOCATION.getValue(), IATOMS_PARAM_TYPE.LOCATION.getCode());
			List<BimMerchantHeaderDTO> dtos = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(dtos)) {
				headerDTO = dtos.get(0);
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".getMerchantHeaderById() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return headerDTO;
	}
}
