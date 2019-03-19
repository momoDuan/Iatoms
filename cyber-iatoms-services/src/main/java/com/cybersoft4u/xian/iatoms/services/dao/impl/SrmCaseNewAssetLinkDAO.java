package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.CoreMessageCode;
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
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAssetLinkDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseNewAssetLinkDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseNewAssetLink;

/**
 * Purpose: SRM_案件最新設備連接檔數據訪問實現類
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2017-4-7
 * @MaintenancePersonnel CrissZhang
 */
public class SrmCaseNewAssetLinkDAO extends GenericBaseDAO<SrmCaseNewAssetLink> implements ISrmCaseNewAssetLinkDAO{

	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(SrmCaseNewAssetLinkDAO.class);
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseNewAssetLinkDAO#listBy(java.lang.String)
	 */
	@Override
	public List<SrmCaseAssetLinkDTO> listBy(String caseId, boolean isNewHave) throws DataAccessException {
		List<SrmCaseAssetLinkDTO> srmCaseAssetLinkDTOs = null;
		try{
			LOGGER.debug("listBy()", "parameters:caseId=" + caseId);
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("caseAssetLink.ASSET_LINK_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.ASSET_LINK_ID.getValue());
			sqlStatement.addSelectClause("caseAssetLink.CASE_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("caseAssetLink.ITEM_TYPE", SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_TYPE.getValue());
			sqlStatement.addSelectClause("caseAssetLink.ITEM_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_ID.getValue());
			sqlStatement.addSelectClause("caseAssetLink.ITEM_CATEGORY", SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_CATEGORY.getValue());
			sqlStatement.addSelectClause("caseAssetLink.SERIAL_NUMBER", SrmCaseAssetLinkDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("caseAssetLink.SERIAL_NUMBER", SrmCaseAssetLinkDTO.ATTRIBUTE.UNINSTALL_SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("caseAssetLink.NUMBER", SrmCaseAssetLinkDTO.ATTRIBUTE.NUMBER.getValue());
			sqlStatement.addSelectClause("caseAssetLink.ACTION", SrmCaseAssetLinkDTO.ATTRIBUTE.ACTION.getValue());
			sqlStatement.addSelectClause("caseAssetLink.CONTENT", SrmCaseAssetLinkDTO.ATTRIBUTE.CONTENT.getValue());
			sqlStatement.addSelectClause("caseAssetLink.IS_LINK", SrmCaseAssetLinkDTO.ATTRIBUTE.IS_LINK.getValue());
			sqlStatement.addSelectClause("caseAssetLink.CREATED_BY_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			sqlStatement.addSelectClause("caseAssetLink.CREATED_BY_NAME", SrmCaseAssetLinkDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("caseAssetLink.CREATED_DATE", SrmCaseAssetLinkDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sqlStatement.addSelectClause("caseAssetLink.ENABLE_DATE", SrmCaseAssetLinkDTO.ATTRIBUTE.ENABLE_DATE.getValue());
			sqlStatement.addSelectClause("caseAssetLink.WAREHOUSE_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.WAREHOUSE_ID.getValue());
			sqlStatement.addSelectClause("caseAssetLink.CONTRACT_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.CONTRACT_ID.getValue());
			sqlStatement.addSelectClause("caseAssetLink.PRICE", SrmCaseAssetLinkDTO.ATTRIBUTE.PRICE.getValue());
			sqlStatement.addSelectClause("caseAssetLink.HISTORY_ASSET_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.HISTORY_ASSET_ID.getValue());
			sqlStatement.addSelectClause("caseAssetLink.PROPERTY_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.PROPERTY_ID.getValue());
			sqlStatement.addSelectClause("assetType.NAME", SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_NAME.getValue());
			sqlStatement.addSelectClause("baseParam.ITEM_NAME", SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_CATEGORY_NAME.getValue());
			StringBuffer buffer = new StringBuffer();
			if(isNewHave){
				buffer.append(schema).append(".SRM_CASE_NEW_ASSET_LINK caseAssetLink ");
			} else {
				buffer.append(schema).append(".SRM_CASE_ASSET_LINK caseAssetLink ");
			}
			buffer.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF baseParam on caseAssetLink.ITEM_CATEGORY = baseParam.ITEM_VALUE and baseParam.BPTD_CODE = :paramType ");
			buffer.append(" left join ").append(schema).append(".DMM_ASSET_TYPE assetType ON assetType.ASSET_TYPE_ID = caseAssetLink.ITEM_ID ");
			sqlStatement.addFromExpression(buffer.toString());
			if (StringUtils.hasText(caseId)) {
				sqlStatement.addWhereClause("caseAssetLink.CASE_ID = :caseId", caseId);
			}
			// 查詢耗材類型與設備類型
			sqlStatement.addWhereClause("caseAssetLink.ITEM_TYPE != :flag", IAtomsConstants.PARAM_CASE_LINK_SUPPLIES);
			if(!isNewHave){
				// Bug #3055
				sqlStatement.addWhereClause("caseAssetLink.IS_LINK <> :d", IAtomsConstants.CASE_DELETE_ASSET);
				sqlStatement.addWhereClause("caseAssetLink.IS_LINK <> :r", IAtomsConstants.CASE_REMOVE_ASSET);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("paramType", IATOMS_PARAM_TYPE.ASSET_CATEGORY.getCode());
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseAssetLinkDTO.class);
			LOGGER.debug("listBy()", "sql:" + sqlQueryBean.toString());
			srmCaseAssetLinkDTOs = super.getDaoSupport().findByNativeSql(sqlQueryBean,aliasBean);
		} catch (Exception e) {
			LOGGER.error("listBy() is error." + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return srmCaseAssetLinkDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseNewAssetLinkDAO#listByCaseId(java.lang.String)
	 */
	@Override
	public List<SrmCaseAssetLinkDTO> listByCaseId(String caseId)
			throws DataAccessException {
		List<SrmCaseAssetLinkDTO> SrmCaseAssetLinkDTOS = null;
		try{
			LOGGER.debug(this.getClass().getName() + ".listByCaseId()", "parameters:caseId=" + caseId);
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("caseAssetLink.ASSET_LINK_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.ASSET_LINK_ID.getValue());
			sqlStatement.addSelectClause("caseAssetLink.SERIAL_NUMBER", SrmCaseAssetLinkDTO.ATTRIBUTE.UNINSTALL_SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("caseAssetLink.SERIAL_NUMBER", SrmCaseAssetLinkDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("caseAssetLink.ITEM_TYPE", SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_TYPE.getValue());
			sqlStatement.addSelectClause("caseAssetLink.ITEM_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_ID.getValue());
			sqlStatement.addSelectClause("caseAssetLink.ITEM_CATEGORY", SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_CATEGORY.getValue());
			sqlStatement.addSelectClause("caseAssetLink.IS_LINK", SrmCaseAssetLinkDTO.ATTRIBUTE.IS_LINK.getValue());
			sqlStatement.addSelectClause("assetType.NAME", SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_NAME.getValue());
			sqlStatement.addSelectClause("caseAssetLink.NUMBER", SrmCaseAssetLinkDTO.ATTRIBUTE.NUMBER.getValue());
			sqlStatement.addSelectClause("caseAssetLink.CONTENT", SrmCaseAssetLinkDTO.ATTRIBUTE.CONTENT.getValue());
			sqlStatement.addSelectClause("baseParam.ITEM_NAME", SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_CATEGORY_NAME.getValue());
			sqlStatement.addSelectClause("caseAssetLink.CASE_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("caseAssetLink.ACTION", SrmCaseAssetLinkDTO.ATTRIBUTE.ACTION.getValue());
			sqlStatement.addSelectClause("caseAssetLink.CREATED_BY_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			sqlStatement.addSelectClause("caseAssetLink.CREATED_BY_NAME", SrmCaseAssetLinkDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("caseAssetLink.CREATED_DATE", SrmCaseAssetLinkDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sqlStatement.addSelectClause("caseAssetLink.ENABLE_DATE", SrmCaseAssetLinkDTO.ATTRIBUTE.ENABLE_DATE.getValue());
			sqlStatement.addSelectClause("caseAssetLink.WAREHOUSE_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.WAREHOUSE_ID.getValue());
			sqlStatement.addSelectClause("caseAssetLink.CONTRACT_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.CONTRACT_ID.getValue());
			sqlStatement.addSelectClause("caseAssetLink.PRICE", SrmCaseAssetLinkDTO.ATTRIBUTE.PRICE.getValue());
			sqlStatement.addSelectClause("caseAssetLink.HISTORY_ASSET_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.HISTORY_ASSET_ID.getValue());
			sqlStatement.addSelectClause("caseAssetLink.PROPERTY_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.PROPERTY_ID.getValue());
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(".SRM_CASE_NEW_ASSET_LINK caseAssetLink ");
			buffer.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF baseParam on caseAssetLink.ITEM_CATEGORY = baseParam.ITEM_VALUE and baseParam.BPTD_CODE = :paramType ");
			buffer.append(" left join ").append(schema).append(".DMM_ASSET_TYPE assetType ON assetType.ASSET_TYPE_ID = caseAssetLink.ITEM_ID ");
			sqlStatement.addFromExpression(buffer.toString());
			if (StringUtils.hasText(caseId)) {
				sqlStatement.addWhereClause("caseAssetLink.CASE_ID = :caseId", caseId);
			}
			// 查詢耗材類型與設備類型
			sqlStatement.addWhereClause("caseAssetLink.ITEM_TYPE != :flag", IAtomsConstants.PARAM_CASE_LINK_SUPPLIES);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("paramType", IATOMS_PARAM_TYPE.ASSET_CATEGORY.getCode());
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseAssetLinkDTO.class);
			LOGGER.debug(this.getClass().getName() + ".listByCaseId()", "sql:" + sqlQueryBean.toString());
			SrmCaseAssetLinkDTOS = super.getDaoSupport().findByNativeSql(sqlQueryBean,aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+":listByCaseId() is error." + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return SrmCaseAssetLinkDTOS;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseNewAssetLinkDAO#getAssetLinkListByDtid(java.lang.String)
	 */
	@Override
	public List<SrmCaseAssetLinkDTO> getAssetLinkListByDtid(String dtid)
			throws DataAccessException {
		List<SrmCaseAssetLinkDTO> SrmCaseAssetLinkDTOS = null;
		try{
			LOGGER.debug(this.getClass().getName() + ".getAssetLinkListByDtid()", "parameters:dtid=" + dtid);
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("newLink.SERIAL_NUMBER", SrmCaseAssetLinkDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("newLink.ITEM_TYPE", SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_TYPE.getValue());
			sqlStatement.addSelectClause("newLink.CASE_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("newLink.ITEM_ID", SrmCaseAssetLinkDTO.ATTRIBUTE.ITEM_ID.getValue());
			//CR #2551 2017/12/07  設備資料 與 最新資料檔 不一致，提醒USER 聯繫客服 消息為：來自C2017120700013訊息：I2017120700006於2017/12/07異動設備與此案件不符，請聯繫客服確認
			sqlStatement.addSelectClause("newLink.CREATED_DATE", SrmCaseAssetLinkDTO.ATTRIBUTE.CREATED_DATE.getValue());
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(".SRM_CASE_NEW_ASSET_LINK newLink, dbo.SRM_CASE_NEW_HANDLE_INFO newinfo ");
			if (StringUtils.hasText(dtid)) {
				buffer.append(" WHERE newLink.CASE_ID = newinfo.CASE_ID and newinfo.DTID = :dtid and newLink.IS_LINK != :delete ");
			}
			sqlStatement.addFromExpression(buffer.toString());
			sqlStatement.setOrderByExpression(" newLink.ITEM_TYPE ");
			// 查詢耗材類型與設備類型
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if (StringUtils.hasText(dtid)) {
				sqlQueryBean.setParameter("dtid", dtid);
				sqlQueryBean.setParameter("delete", IAtomsConstants.NO);
			}
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseAssetLinkDTO.class);
			LOGGER.debug(this.getClass().getName() + ".getAssetLinkListByDtid()", "sql:" + sqlQueryBean.toString());
			SrmCaseAssetLinkDTOS = super.getDaoSupport().findByNativeSql(sqlQueryBean,aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+":getAssetLinkListByDtid() is error." + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return SrmCaseAssetLinkDTOS;
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseNewAssetLinkDAO#updateCaseSerialNumberInfo(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Boolean updateCaseNewSerialNumberInfo(String serialNumber,
			String caseId, String content, String action) throws DataAccessException {
		Boolean flag = false;
		try{
			LOGGER.debug(this.getClass().getName() + ".updateCaseNewSerialNumberInfo()", "parameters:serialNumber=" + serialNumber);
			LOGGER.debug(this.getClass().getName() + ".updateCaseNewSerialNumberInfo()", "parameters:caseId=" + caseId);
			LOGGER.debug("updateCaseNewSerialNumberInfo()", "parameters:itemType=" + content);
			if (StringUtils.hasText(caseId)) {
				SqlQueryBean sqlQueryBean = new SqlQueryBean("UPDATE ");
				String schema = this.getMySchema();
				sqlQueryBean.append(schema).append(".SRM_CASE_NEW_ASSET_LINK  SET IS_LINK = :removeParam, CONTENT = :content, ACTION=:action ");
				sqlQueryBean.append("where 1=1");
				if(StringUtils.hasText(caseId)){
					sqlQueryBean.append(" and CASE_ID = :caseId ");
					sqlQueryBean.setParameter("caseId", caseId);
				}
				
				if(StringUtils.hasText(serialNumber)){
					sqlQueryBean.append(" and SERIAL_NUMBER = :serialNumber ");
					sqlQueryBean.setParameter("serialNumber", serialNumber);
				}
				sqlQueryBean.setParameter("content", content);
				sqlQueryBean.setParameter("action", action);
				sqlQueryBean.setParameter("removeParam", IAtomsConstants.CASE_DELETE_ASSET);
				LOGGER.debug("updateCaseNewSerialNumberInfo()", "sql:" + sqlQueryBean.toString());
				this.getDaoSupport().updateByNativeSql(sqlQueryBean);
				//this.getDaoSupport().flush();
				flag = true;
			}
		} catch (Exception e) {
			LOGGER.error("updateCaseNewSerialNumberInfo()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return flag;	
	}
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseNewAssetLinkDAO#getUnLinkAssetListBySerialNumber(java.lang.String, java.lang.String)
	 */
	@Override
	public Boolean updateCaseSerialNumberInfo(String serialNumber, String caseId, String content, String action) throws DataAccessException {
		Boolean flag = false;
		try{
			LOGGER.debug(this.getClass().getName() + ".updateCaseSerialNumberInfo()", "parameters:serialNumber=" + serialNumber);
			LOGGER.debug(this.getClass().getName() + ".updateCaseSerialNumberInfo()", "parameters:caseId=" + caseId);
			LOGGER.debug("updateCaseSerialNumberInfo()", "parameters:itemType=" + content);
			if (StringUtils.hasText(caseId)) {
				SqlQueryBean sqlQueryBean = new SqlQueryBean("UPDATE ");
				String schema = this.getMySchema();
				sqlQueryBean.append(schema).append(".SRM_CASE_ASSET_LINK  SET IS_LINK = :removeParam, CONTENT = :content, ACTION=:action ");
				sqlQueryBean.append("where 1=1");
				if(StringUtils.hasText(caseId)){
					sqlQueryBean.append(" and CASE_ID = :caseId ");
					sqlQueryBean.setParameter("caseId", caseId);
				}
				
				if(StringUtils.hasText(serialNumber)){
					sqlQueryBean.append(" and SERIAL_NUMBER = :serialNumber ");
					sqlQueryBean.setParameter("serialNumber", serialNumber);
				}
				sqlQueryBean.setParameter("action", action);
				sqlQueryBean.setParameter("content", content);
				sqlQueryBean.setParameter("removeParam", IAtomsConstants.CASE_DELETE_ASSET);
				LOGGER.debug("updateCaseSerialNumberInfo()", "sql:" + sqlQueryBean.toString());
				this.getDaoSupport().updateByNativeSql(sqlQueryBean);
				//this.getDaoSupport().flush();
				flag = true;
			}
		} catch (Exception e) {
			LOGGER.error("updateCaseSerialNumberInfo()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return flag;	
	}
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO#countByCaseId(java.lang.String)
	 */
	@Override
	public Integer countByCaseId(String caseId) throws DataAccessException {
		LOGGER.debug(".countByCaseId()", "parameters:caseId=", caseId);
		Integer result = Integer.valueOf(0);
		try {
			String schema = this.getMySchema();	
			//查询总条数
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("COUNT(1)");
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".SRM_CASE_HANDLE_INFO info left join ").
			append(schema).append(".SRM_CASE_ASSET_LINK link on info.CASE_ID = link.CASE_ID");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if ( StringUtils.hasText(caseId)) {
				sqlStatement.addWhereClause(" info.CASE_ID = :caseId", caseId);
			}
			sqlStatement.addWhereClause(" info.CASE_CATEGORY =:uninstall", IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode());
			sqlStatement.addWhereClause(" link.IS_LINK =:D", IAtomsConstants.CASE_DELETE_ASSET);
			sqlStatement.addWhereClause(" link.ITEM_TYPE =:number", IAtomsConstants.PARAM_CASE_LINK_EDC_TYPE);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug("countByCaseId() SQL---------->", sqlQueryBean.toString());
			List<Integer> list = super.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(list)){
				result = list.get(0);
			}	
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".countByCaseId() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return result;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseNewAssetLinkDAO#insertAssetLink(java.lang.String, com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO)
	 */
	@Override
	public void insertAssetLink(String dtid, SrmCaseHandleInfoDTO srmCaseHandleInfoDTO) throws DataAccessException {
		try{
			LOGGER.debug("insertAssetLink()", "parameters:dtid=" + dtid);
			if (StringUtils.hasText(dtid)) {
				SqlQueryBean sqlQueryBean = new SqlQueryBean();
				String schema = this.getMySchema();
				sqlQueryBean.append(" declare @caseId VARCHAR(50),");
				sqlQueryBean.append(" @dtid VARCHAR(50),");
				sqlQueryBean.append(" @serialNumberEdc VARCHAR(50),");
				sqlQueryBean.append(" @serialNumberPer VARCHAR(50),");
				sqlQueryBean.append(" @serialNumberPer2 VARCHAR(50),");
				sqlQueryBean.append(" @serialNumberPer3 VARCHAR(50),");
				sqlQueryBean.append(" @assetId VARCHAR(50),");
				sqlQueryBean.append(" @wareHouseId VARCHAR(50),");
				sqlQueryBean.append(" @contractId VARCHAR(50),");
				sqlQueryBean.append(" @propertyId VARCHAR(50),");
				sqlQueryBean.append(" @assetTypeId VARCHAR(50),");
				sqlQueryBean.append(" @enableDate datetime,");
				sqlQueryBean.append(" @historyAssetId VARCHAR(50),");
				sqlQueryBean.append(" @count int");

						
				sqlQueryBean.append(" SET @dtid =:dtid");
				sqlQueryBean.setParameter("dtid", dtid);
				if(StringUtils.hasText(srmCaseHandleInfoDTO.getEdcSerialNumber())){
					sqlQueryBean.append(" SET @serialNumberEdc =:serialNumberEdc");
					sqlQueryBean.setParameter("serialNumberEdc", srmCaseHandleInfoDTO.getEdcSerialNumber());
				} else {
					sqlQueryBean.append(" SET @serialNumberEdc = null");
				}
				if(StringUtils.hasText(srmCaseHandleInfoDTO.getPeripheralsSerialNumber())){
					sqlQueryBean.append(" SET @serialNumberPer =:serialNumberPer");
					sqlQueryBean.setParameter("serialNumberPer", srmCaseHandleInfoDTO.getPeripheralsSerialNumber());
				} else {
					sqlQueryBean.append(" SET @serialNumberPer = null");
				}
				if(StringUtils.hasText(srmCaseHandleInfoDTO.getPeripherals2SerialNumber())){
					sqlQueryBean.append(" SET @serialNumberPer2 =:serialNumberPer2");
					sqlQueryBean.setParameter("serialNumberPer2", srmCaseHandleInfoDTO.getPeripherals2SerialNumber());
				} else {
					sqlQueryBean.append(" SET @serialNumberPer2 = null");
				}
				if(StringUtils.hasText(srmCaseHandleInfoDTO.getPeripherals3SerialNumber())){
					sqlQueryBean.append(" SET @serialNumberPer3 =:serialNumberPer3");
					sqlQueryBean.setParameter("serialNumberPer3", srmCaseHandleInfoDTO.getPeripherals3SerialNumber());
				} else {
					sqlQueryBean.append(" SET @serialNumberPer3 = null");
				}
				
				sqlQueryBean.append(" SET @caseId = null");
				
				sqlQueryBean.append(" select @caseId = newInfo.CASE_ID");
				sqlQueryBean.append(" from ").append(schema).append(".SRM_CASE_NEW_HANDLE_INFO newInfo");
				sqlQueryBean.append(" where newInfo.DTID = @dtid");
				
				sqlQueryBean.append(" if @caseId is not null");
				sqlQueryBean.append(" BEGIN");
				
				// 刪除未關聯的 IS_LINK = 'N'
				sqlQueryBean.append(" DELETE FROM  ").append(schema).append(".SRM_CASE_NEW_ASSET_LINK WHERE CASE_ID=@caseId and IS_LINK = 'N'");
				
				// edc
				if(StringUtils.hasText(srmCaseHandleInfoDTO.getEdcSerialNumber())){
					// 重置計數器
					sqlQueryBean.append(" SET @count = 0");
					// 判斷是否存在
					sqlQueryBean.append(" SELECT @count = COUNT(1)");
					sqlQueryBean.append(" from dbo.SRM_CASE_NEW_ASSET_LINK ");
					sqlQueryBean.append(" where CASE_ID = @caseId");
					sqlQueryBean.append(" and ITEM_TYPE = '10'");
					sqlQueryBean.append(" if @count = 0");
					sqlQueryBean.append(" BEGIN");
					

					sqlQueryBean.append(" SET @assetId = null");
					sqlQueryBean.append(" SET @wareHouseId = null");
					sqlQueryBean.append(" SET @contractId = null");
					sqlQueryBean.append(" SET @propertyId = null");
					sqlQueryBean.append(" SET @assetTypeId = null");
					sqlQueryBean.append(" SET @enableDate = null");
					sqlQueryBean.append(" SET @historyAssetId = null");
					
					sqlQueryBean.append(" select @assetId = r.ASSET_ID,");
					sqlQueryBean.append(" @wareHouseId = r.WAREHOUSE_ID,");
					sqlQueryBean.append(" @contractId = r.CONTRACT_ID,");
					sqlQueryBean.append(" @propertyId = r.PROPERTY_ID,");
					sqlQueryBean.append(" @assetTypeId = r.ASSET_TYPE_ID,");
					sqlQueryBean.append(" @enableDate = r.ENABLE_DATE");
					sqlQueryBean.append(" from ").append(schema).append(".DMM_REPOSITORY r");
					sqlQueryBean.append(" where r.SERIAL_NUMBER = @serialNumberEdc");
					
					sqlQueryBean.append(" if @assetId is not null");
					sqlQueryBean.append(" BEGIN");
					sqlQueryBean.append(" select @historyAssetId = HISTORY_ID");
					sqlQueryBean.append(" FROM(");
					sqlQueryBean.append(" select top 1 his.HISTORY_ID");
					sqlQueryBean.append(" from ").append(schema).append(".DMM_REPOSITORY_HISTORY his");
					sqlQueryBean.append(" where his.ASSET_ID = @assetId");
					sqlQueryBean.append(" and his.STATUS = 'IN_USE'");
					sqlQueryBean.append(" order by his.UPDATE_DATE desc");
					sqlQueryBean.append(" )t");
					
					// 插入設備鏈接檔數據
					sqlQueryBean.append(" INSERT INTO ").append(schema).append(".SRM_CASE_NEW_ASSET_LINK");
					sqlQueryBean.append(" (ASSET_LINK_ID, CASE_ID, ITEM_TYPE, ITEM_ID, ITEM_CATEGORY, SERIAL_NUMBER, NUMBER, ACTION, CONTENT, IS_LINK, CREATED_BY_ID, CREATED_BY_NAME, CREATED_DATE, ENABLE_DATE, WAREHOUSE_ID, CONTRACT_ID, PRICE, HISTORY_ASSET_ID, PROPERTY_ID)");
					sqlQueryBean.append(" VALUES(@caseId + '_10', @caseId, '10', @assetTypeId, 'EDC', @serialNumberEdc, NULL, NULL, NULL, 'Y', NULL, NULL, NULL, @enableDate, @wareHouseId, @contractId, NULL, @historyAssetId, @propertyId);");
					sqlQueryBean.append(" END ");
					
					sqlQueryBean.append(" END ");
				}
					
				// 周邊1
				if(StringUtils.hasText(srmCaseHandleInfoDTO.getPeripheralsSerialNumber())){
					// 重置計數器
					sqlQueryBean.append(" SET @count = 0");
					// 判斷是否存在
					sqlQueryBean.append(" SELECT @count = COUNT(1)");
					sqlQueryBean.append(" from dbo.SRM_CASE_NEW_ASSET_LINK ");
					sqlQueryBean.append(" where CASE_ID = @caseId");
					sqlQueryBean.append(" and ITEM_TYPE = '11'");
					sqlQueryBean.append(" if @count = 0");
					sqlQueryBean.append(" BEGIN");
					

					sqlQueryBean.append(" SET @assetId = null");
					sqlQueryBean.append(" SET @wareHouseId = null");
					sqlQueryBean.append(" SET @contractId = null");
					sqlQueryBean.append(" SET @propertyId = null");
					sqlQueryBean.append(" SET @assetTypeId = null");
					sqlQueryBean.append(" SET @enableDate = null");
					sqlQueryBean.append(" SET @historyAssetId = null");
					
					sqlQueryBean.append(" select @assetId = r.ASSET_ID,");
					sqlQueryBean.append(" @wareHouseId = r.WAREHOUSE_ID,");
					sqlQueryBean.append(" @contractId = r.CONTRACT_ID,");
					sqlQueryBean.append(" @propertyId = r.PROPERTY_ID,");
					sqlQueryBean.append(" @assetTypeId = r.ASSET_TYPE_ID,");
					sqlQueryBean.append(" @enableDate = r.ENABLE_DATE");
					sqlQueryBean.append(" from ").append(schema).append(".DMM_REPOSITORY r");
					sqlQueryBean.append(" where r.SERIAL_NUMBER = @serialNumberPer");
					
					sqlQueryBean.append(" if @assetId is not null");
					sqlQueryBean.append(" BEGIN");
					sqlQueryBean.append(" select @historyAssetId = HISTORY_ID");
					sqlQueryBean.append(" FROM(");
					sqlQueryBean.append(" select top 1 his.HISTORY_ID");
					sqlQueryBean.append(" from ").append(schema).append(".DMM_REPOSITORY_HISTORY his");
					sqlQueryBean.append(" where his.ASSET_ID = @assetId");
					sqlQueryBean.append(" and his.STATUS = 'IN_USE'");
					sqlQueryBean.append(" order by his.UPDATE_DATE desc");
					sqlQueryBean.append(" )t");
					
					// 插入設備鏈接檔數據
					sqlQueryBean.append(" INSERT INTO ").append(schema).append(".SRM_CASE_NEW_ASSET_LINK");
					sqlQueryBean.append(" (ASSET_LINK_ID, CASE_ID, ITEM_TYPE, ITEM_ID, ITEM_CATEGORY, SERIAL_NUMBER, NUMBER, ACTION, CONTENT, IS_LINK, CREATED_BY_ID, CREATED_BY_NAME, CREATED_DATE, ENABLE_DATE, WAREHOUSE_ID, CONTRACT_ID, PRICE, HISTORY_ASSET_ID, PROPERTY_ID)");
					sqlQueryBean.append(" VALUES(@caseId + '_11', @caseId, '11', @assetTypeId, 'Related_Products', @serialNumberPer, NULL, NULL, NULL, 'Y', NULL, NULL, NULL, @enableDate, @wareHouseId, @contractId, NULL, @historyAssetId, @propertyId);");
					
					// 案件主檔更改周邊設備
					sqlQueryBean.append(" UPDATE ").append(schema).append(".SRM_CASE_NEW_HANDLE_INFO set PERIPHERALS = @assetTypeId where CASE_ID = @caseId");
					sqlQueryBean.append(" END ");
					
					sqlQueryBean.append(" END ");
				}
				
				// 周邊2
				if(StringUtils.hasText(srmCaseHandleInfoDTO.getPeripherals2SerialNumber())){
					// 重置計數器
					sqlQueryBean.append(" SET @count = 0");
					// 判斷是否存在
					sqlQueryBean.append(" SELECT @count = COUNT(1)");
					sqlQueryBean.append(" from dbo.SRM_CASE_NEW_ASSET_LINK ");
					sqlQueryBean.append(" where CASE_ID = @caseId");
					sqlQueryBean.append(" and ITEM_TYPE = '12'");
					sqlQueryBean.append(" if @count = 0");
					sqlQueryBean.append(" BEGIN");
					

					sqlQueryBean.append(" SET @assetId = null");
					sqlQueryBean.append(" SET @wareHouseId = null");
					sqlQueryBean.append(" SET @contractId = null");
					sqlQueryBean.append(" SET @propertyId = null");
					sqlQueryBean.append(" SET @assetTypeId = null");
					sqlQueryBean.append(" SET @enableDate = null");
					sqlQueryBean.append(" SET @historyAssetId = null");
					
					sqlQueryBean.append(" select @assetId = r.ASSET_ID,");
					sqlQueryBean.append(" @wareHouseId = r.WAREHOUSE_ID,");
					sqlQueryBean.append(" @contractId = r.CONTRACT_ID,");
					sqlQueryBean.append(" @propertyId = r.PROPERTY_ID,");
					sqlQueryBean.append(" @assetTypeId = r.ASSET_TYPE_ID,");
					sqlQueryBean.append(" @enableDate = r.ENABLE_DATE");
					sqlQueryBean.append(" from ").append(schema).append(".DMM_REPOSITORY r");
					sqlQueryBean.append(" where r.SERIAL_NUMBER = @serialNumberPer2");
					
					sqlQueryBean.append(" if @assetId is not null");
					sqlQueryBean.append(" BEGIN");
					sqlQueryBean.append(" select @historyAssetId = HISTORY_ID");
					sqlQueryBean.append(" FROM(");
					sqlQueryBean.append(" select top 1 his.HISTORY_ID");
					sqlQueryBean.append(" from ").append(schema).append(".DMM_REPOSITORY_HISTORY his");
					sqlQueryBean.append(" where his.ASSET_ID = @assetId");
					sqlQueryBean.append(" and his.STATUS = 'IN_USE'");
					sqlQueryBean.append(" order by his.UPDATE_DATE desc");
					sqlQueryBean.append(" )t");
					
					// 插入設備鏈接檔數據
					sqlQueryBean.append(" INSERT INTO ").append(schema).append(".SRM_CASE_NEW_ASSET_LINK");
					sqlQueryBean.append(" (ASSET_LINK_ID, CASE_ID, ITEM_TYPE, ITEM_ID, ITEM_CATEGORY, SERIAL_NUMBER, NUMBER, ACTION, CONTENT, IS_LINK, CREATED_BY_ID, CREATED_BY_NAME, CREATED_DATE, ENABLE_DATE, WAREHOUSE_ID, CONTRACT_ID, PRICE, HISTORY_ASSET_ID, PROPERTY_ID)");
					sqlQueryBean.append(" VALUES(@caseId + '_12', @caseId, '12', @assetTypeId, 'Related_Products', @serialNumberPer2, NULL, NULL, NULL, 'Y', NULL, NULL, NULL, @enableDate, @wareHouseId, @contractId, NULL, @historyAssetId, @propertyId);");
					
					// 案件主檔更改周邊設備
					sqlQueryBean.append(" UPDATE ").append(schema).append(".SRM_CASE_NEW_HANDLE_INFO set PERIPHERALS2 = @assetTypeId where CASE_ID = @caseId");
					sqlQueryBean.append(" END ");
					
					sqlQueryBean.append(" END ");
				}
				
				// 周邊3
				if(StringUtils.hasText(srmCaseHandleInfoDTO.getPeripherals3SerialNumber())){
					// 重置計數器
					sqlQueryBean.append(" SET @count = 0");
					// 判斷是否存在
					sqlQueryBean.append(" SELECT @count = COUNT(1)");
					sqlQueryBean.append(" from dbo.SRM_CASE_NEW_ASSET_LINK ");
					sqlQueryBean.append(" where CASE_ID = @caseId");
					sqlQueryBean.append(" and ITEM_TYPE = '13'");
					sqlQueryBean.append(" if @count = 0");
					sqlQueryBean.append(" BEGIN");
					
					
					sqlQueryBean.append(" SET @assetId = null");
					sqlQueryBean.append(" SET @wareHouseId = null");
					sqlQueryBean.append(" SET @contractId = null");
					sqlQueryBean.append(" SET @propertyId = null");
					sqlQueryBean.append(" SET @assetTypeId = null");
					sqlQueryBean.append(" SET @enableDate = null");
					sqlQueryBean.append(" SET @historyAssetId = null");
					
					sqlQueryBean.append(" select @assetId = r.ASSET_ID,");
					sqlQueryBean.append(" @wareHouseId = r.WAREHOUSE_ID,");
					sqlQueryBean.append(" @contractId = r.CONTRACT_ID,");
					sqlQueryBean.append(" @propertyId = r.PROPERTY_ID,");
					sqlQueryBean.append(" @assetTypeId = r.ASSET_TYPE_ID,");
					sqlQueryBean.append(" @enableDate = r.ENABLE_DATE");
					sqlQueryBean.append(" from ").append(schema).append(".DMM_REPOSITORY r");
					sqlQueryBean.append(" where r.SERIAL_NUMBER = @serialNumberPer3");
					
					sqlQueryBean.append(" if @assetId is not null");
					sqlQueryBean.append(" BEGIN");
					sqlQueryBean.append(" select @historyAssetId = HISTORY_ID");
					sqlQueryBean.append(" FROM(");
					sqlQueryBean.append(" select top 1 his.HISTORY_ID");
					sqlQueryBean.append(" from ").append(schema).append(".DMM_REPOSITORY_HISTORY his");
					sqlQueryBean.append(" where his.ASSET_ID = @assetId");
					sqlQueryBean.append(" and his.STATUS = 'IN_USE'");
					sqlQueryBean.append(" order by his.UPDATE_DATE desc");
					sqlQueryBean.append(" )t");
					
					// 插入設備鏈接檔數據
					sqlQueryBean.append(" INSERT INTO ").append(schema).append(".SRM_CASE_NEW_ASSET_LINK");
					sqlQueryBean.append(" (ASSET_LINK_ID, CASE_ID, ITEM_TYPE, ITEM_ID, ITEM_CATEGORY, SERIAL_NUMBER, NUMBER, ACTION, CONTENT, IS_LINK, CREATED_BY_ID, CREATED_BY_NAME, CREATED_DATE, ENABLE_DATE, WAREHOUSE_ID, CONTRACT_ID, PRICE, HISTORY_ASSET_ID, PROPERTY_ID)");
					sqlQueryBean.append(" VALUES(@caseId + '_13', @caseId, '13', @assetTypeId, 'Related_Products', @serialNumberPer3, NULL, NULL, NULL, 'Y', NULL, NULL, NULL, @enableDate, @wareHouseId, @contractId, NULL, @historyAssetId, @propertyId);");
					
					// 案件主檔更改周邊設備
					sqlQueryBean.append(" UPDATE ").append(schema).append(".SRM_CASE_NEW_HANDLE_INFO set PERIPHERALS3 = @assetTypeId where CASE_ID = @caseId");
					sqlQueryBean.append(" END ");
					
					sqlQueryBean.append(" END ");
				}
				sqlQueryBean.append(" END ");

				LOGGER.debug("insertAssetLink()", "sql:" + sqlQueryBean.toString());
				this.getDaoSupport().updateByNativeSql(sqlQueryBean);
				this.getDaoSupport().flush();
			}
		} catch (Exception e) {
			LOGGER.error("insertAssetLink()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseNewAssetLinkDAO#updateSimAssetLink(java.lang.String, java.lang.String)
	 */
	@Override
	public void updateSimAssetLink(String serialNumber, String assetTypeId) throws DataAccessException {
		try{
			LOGGER.debug("insertAssetLink()", "parameters:serialNumber=" + serialNumber);
			LOGGER.debug("insertAssetLink()", "parameters:assetTypeId=" + assetTypeId);
			if (StringUtils.hasText(serialNumber) && StringUtils.hasText(assetTypeId)) {
				SqlQueryBean sqlQueryBean = new SqlQueryBean();
				String schema = this.getMySchema();
				// 依據設備序號調整案件設備信息
				sqlQueryBean.append(" declare @caseId VARCHAR(50),");
				sqlQueryBean.append(" @assetLinkId VARCHAR(50),");
				sqlQueryBean.append(" @itemType VARCHAR(50),");
				sqlQueryBean.append(" @assetTypeId VARCHAR(50),");
				sqlQueryBean.append(" @serialNumberPer VARCHAR(50),");
				sqlQueryBean.append(" @assetTableName varchar(100),");
				sqlQueryBean.append(" @caseTableName varchar(100),");
				sqlQueryBean.append(" @columnName varchar(100),");
				sqlQueryBean.append(" @execSql varchar(500),");
				sqlQueryBean.append(" @caseCount int,");
				sqlQueryBean.append(" @isNew VARCHAR(50),");
				sqlQueryBean.append(" @i int");
						
				sqlQueryBean.append(" set @serialNumberPer =:serialNumberPer");
				sqlQueryBean.setParameter("serialNumberPer", serialNumber);
				
				sqlQueryBean.append(" set @assetTypeId =:assetTypeId");
				sqlQueryBean.setParameter("assetTypeId", assetTypeId);
				
				sqlQueryBean.append(" if @serialNumberPer is not NULL ");
				sqlQueryBean.append(" BEGIN");
				// 臨時表判斷
				sqlQueryBean.append(" if object_id('tempdb..#tempTable') is not null");
				sqlQueryBean.append(" Begin");
				sqlQueryBean.append(" drop table #tempTable");
				sqlQueryBean.append(" End");
				
				// 需要調整的案件增加至臨時表
				sqlQueryBean.append(" select ASSET_LINK_ID, CASE_ID, ITEM_TYPE, IS_NEW into #tempTable");
				sqlQueryBean.append(" from (");
				sqlQueryBean.append(" SELECT");
				sqlQueryBean.append(" link.ASSET_LINK_ID,");
				sqlQueryBean.append(" link.CASE_ID,");
				sqlQueryBean.append(" link.ITEM_TYPE,");
				sqlQueryBean.append(" 'N' AS IS_NEW");
				sqlQueryBean.append(" from");
				sqlQueryBean.append(schema).append(".SRM_CASE_ASSET_LINK link left join ").append(schema).append(".SRM_CASE_HANDLE_INFO info on");
				sqlQueryBean.append(" link.CASE_ID = info.CASE_ID");
				sqlQueryBean.append(" where");
				sqlQueryBean.append(" link.SERIAL_NUMBER = @serialNumberPer");
				sqlQueryBean.append(" and info.CASE_STATUS not in('Closed','ImmediateClose','Voided')");
				
				sqlQueryBean.append(" union all ");
				
				sqlQueryBean.append(" select");
				sqlQueryBean.append(" newLink.ASSET_LINK_ID,");
				sqlQueryBean.append(" newLink.CASE_ID,");
				sqlQueryBean.append(" newLink.ITEM_TYPE,");
				sqlQueryBean.append(" 'Y' AS IS_NEW");
				sqlQueryBean.append(" from");
				sqlQueryBean.append(schema).append(".SRM_CASE_NEW_ASSET_LINK newLink left join ").append(schema).append(".SRM_CASE_NEW_HANDLE_INFO newInfo on");
				sqlQueryBean.append(" newLink.CASE_ID = newInfo.CASE_ID");
				sqlQueryBean.append(" where");
				sqlQueryBean.append(" newLink.SERIAL_NUMBER = @serialNumberPer");
				sqlQueryBean.append(" )a ");

				sqlQueryBean.append(" SELECT @caseCount = COUNT(1) FROM #tempTable");
				sqlQueryBean.append(" set @i = 0 ");
				// 循環處理案件
				sqlQueryBean.append(" while @i < @caseCount");
				sqlQueryBean.append(" BEGIN");
				sqlQueryBean.append(" SET @assetLinkId = null");
				sqlQueryBean.append(" SET @caseId = null");
				sqlQueryBean.append(" SET @itemType = null");
				sqlQueryBean.append(" SET @isNew = null");
				// 查詢案件等資料
				sqlQueryBean.append(" SELECT top 1 @assetLinkId = ASSET_LINK_ID, @caseId = CASE_ID, @itemType = ITEM_TYPE, @isNew = IS_NEW FROM #tempTable");
				sqlQueryBean.append(" if @caseId is not null and @assetLinkId is not null and @itemType is not null");
				sqlQueryBean.append(" BEGIN");
				sqlQueryBean.append(" SET @assetTableName = null");
				sqlQueryBean.append(" SET @caseTableName = null");
				sqlQueryBean.append(" SET @columnName = null");
				sqlQueryBean.append(" SET @execSql = null");
				sqlQueryBean.append(" if @isNew = 'Y'");
				sqlQueryBean.append(" BEGIN");
				sqlQueryBean.append(" SET @assetTableName = 'SRM_CASE_NEW_ASSET_LINK'");
				sqlQueryBean.append(" SET @caseTableName = 'SRM_CASE_NEW_HANDLE_INFO'");
				sqlQueryBean.append(" END ");
				sqlQueryBean.append(" else ");
				sqlQueryBean.append(" BEGIN");
				sqlQueryBean.append(" SET @assetTableName = 'SRM_CASE_ASSET_LINK'");
				sqlQueryBean.append(" SET @caseTableName = 'SRM_CASE_HANDLE_INFO'");
				sqlQueryBean.append(" END ");
				// 更新設備鏈接檔信息
				sqlQueryBean.append(" set @execSql = 'UPDATE ").append(schema).append(".' + @assetTableName + ' SET ITEM_ID = ''' + @assetTypeId + ''' where ASSET_LINK_ID = ''' + @assetLinkId + ''''");
				sqlQueryBean.append(" exec(@execSql)");
				
				sqlQueryBean.append(" if @itemType = '11'");
				sqlQueryBean.append(" BEGIN");
				sqlQueryBean.append(" SET @columnName = 'PERIPHERALS'");
				sqlQueryBean.append(" END ");
				sqlQueryBean.append(" else if @itemType = '12'");
				sqlQueryBean.append(" BEGIN");
				sqlQueryBean.append(" SET @columnName = 'PERIPHERALS2'");
				sqlQueryBean.append(" END");
				sqlQueryBean.append(" else if @itemType = '13'");
				sqlQueryBean.append(" BEGIN");
				sqlQueryBean.append(" SET @columnName = 'PERIPHERALS3'");
				sqlQueryBean.append(" END");
				sqlQueryBean.append(" if @columnName is not null");
				sqlQueryBean.append(" BEGIN");
				sqlQueryBean.append(" set @execSql = 'UPDATE ").append(schema).append(".' + @caseTableName + ' SET ' + @columnName + ' = ''' + @assetTypeId + ''' where CASE_ID = ''' + @caseId + ''''");
				sqlQueryBean.append(" exec(@execSql)");
				sqlQueryBean.append(" END ");
				// 刪除臨時表該條記錄	
				sqlQueryBean.append(" delete #tempTable where ASSET_LINK_ID = @assetLinkId and IS_NEW = @isNew");
				sqlQueryBean.append(" END");
				sqlQueryBean.append(" set @i = @i + 1");
				sqlQueryBean.append(" END ");
				sqlQueryBean.append(" END ");

				LOGGER.debug("insertAssetLink()", "sql:" + sqlQueryBean.toString());
				this.getDaoSupport().updateByNativeSql(sqlQueryBean);
				this.getDaoSupport().flush();
			}
		} catch (Exception e) {
			LOGGER.error("insertAssetLink()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
}
