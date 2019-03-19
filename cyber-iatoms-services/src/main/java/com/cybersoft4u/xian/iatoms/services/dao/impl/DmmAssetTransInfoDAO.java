package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Parameter;
import cafe.core.config.GenericConfigManager;
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
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetTransInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetTransListDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.WarehouseDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetTransInfoDAO;

/**
 * Purpose: 設備轉倉作業DAO
 * @author barryzhang
 * @since  JDK 1.6
 * @date   2016/7/12
 * @MaintenancePersonnel Amanda Wang
 */
@SuppressWarnings("rawtypes")
public class DmmAssetTransInfoDAO extends GenericBaseDAO implements IDmmAssetTransInfoDAO {
	/**
	 * 系统日志记录物件  
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.DAO, DmmAssetTransInfoDAO.class);
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetTransInfoDAO#listBy()
	 */
	@Override
	public List<Parameter> listBy(String userId) throws DataAccessException {
		LOGGER.debug("listBy()", "userId:", userId);
		//轉倉批號列表
		List<Parameter> result = null;
		//得到schema
		String schema = this.getMySchema();
		//創建SqlStatement
		SqlStatement sql = new SqlStatement();
		StringBuffer stringBuffer = new StringBuffer();
		try {
			sql.addSelectClause("distinct asset.ASSET_TRANS_ID", Parameter.FIELD_VALUE);
			stringBuffer.append("asset.ASSET_TRANS_ID + (case asset.IS_BACK when '").append(IAtomsConstants.YES).append("' then '-退回' else '' end)");
			sql.addSelectClause(stringBuffer.toString(), Parameter.FIELD_NAME);

			
			stringBuffer.delete(0, stringBuffer.length());
			stringBuffer.append(schema).append(". DMM_ASSET_TRANS_INFO asset, ").append(schema).append(".ADM_USER admUser");
			sql.setFromExpression(stringBuffer.toString());
			sql.addWhereClause("(asset.IS_LIST_DONE = :isListDone or (asset.IS_BACK = :isBack AND asset.IS_CANCEL= :isCancel))");
		//	stringBuffer.delete(0, stringBuffer.length());
		//	stringBuffer.append("ISNULL(asset.DELETED, '").append(IAtomsConstants.NO).append("') !=:deleted");
		//	sql.addWhereClause(stringBuffer.toString(), IAtomsConstants.YES);
			if (StringUtils.hasText(userId)) {
				sql.addWhereClause("admUser.USER_ID = :userId", userId);
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append("(isnull( admUser.DATA_ACL ,'").append(IAtomsConstants.NO).append("') = :dataAcl OR EXISTS(SELECT 1 FROM ").append(schema);
				stringBuffer.append(".ADM_USER_WAREHOUSE uWarehouse WHERE uWarehouse.USER_ID = admUser.USER_ID and uWarehouse.WAREHOUSE_ID = asset.FROM_WAREHOUSE_ID))");
				sql.addWhereClause(stringBuffer.toString());
			}
			//排序
			sql.setOrderByExpression("asset.ASSET_TRANS_ID desc");
			//記錄sql語句
			LOGGER.debug("listBy()", "sql:", sql.toString() );
			//創建SqlQueryBean
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			sqlQueryBean.setParameter(DmmAssetTransInfoDTO.ATTRIBUTE.IS_LIST_DONE.getValue(), IAtomsConstants.NO);
			sqlQueryBean.setParameter(DmmAssetTransInfoDTO.ATTRIBUTE.IS_BACK.getValue(), IAtomsConstants.YES);
			sqlQueryBean.setParameter(DmmAssetTransInfoDTO.ATTRIBUTE.IS_CANCLE.getValue(), IAtomsConstants.NO);
			if (StringUtils.hasText(userId)) {
				sqlQueryBean.setParameter(AdmUserDTO.ATTRIBUTE.DATA_ACL.getValue(), IAtomsConstants.NO);
				sqlQueryBean.setParameter(AdmUserDTO.ATTRIBUTE.USER_ID.getValue(), userId);
			}
			AliasBean aliasBean = new AliasBean(Parameter.class);		
			result = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listBy()", "is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ASSET_TRANS_INFO)}, e);
		}
		return result;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetTransInfoDAO#listBy(com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetTransInfoFormDTO)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DmmAssetTransListDTO> listBy(String assetTransId, Boolean isHistory, String order, String sort, Integer page, Integer rows, Boolean historyFlag, Boolean isShowStatus) throws DataAccessException {
		LOGGER.debug("listBy()", "assetTransId:", assetTransId);
		LOGGER.debug("listBy()", "order:", order);
		LOGGER.debug("listBy()", "sort:", sort);
		LOGGER.debug("listBy()", "page:", page.intValue());
		LOGGER.debug("listBy()", "rows:", rows.intValue());
		//轉倉清單信息
		List<DmmAssetTransListDTO> assetTransList = null;
		SqlStatement sql = new SqlStatement();
		//schema
		String schema = this.getMySchema();
		StringBuffer stringBuffer = new StringBuffer();
		try {
			//sqlStatement添加條件
			sql.addSelectClause("trans.ID", DmmAssetTransListDTO.ATTRIBUTE.ASSET_TRANS_LIST_ID.getValue());
			sql.addSelectClause("trans.ASSET_TRANS_ID", DmmAssetTransListDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue());
			sql.addSelectClause("trans.SERIAL_NUMBER", DmmAssetTransListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sql.addSelectClause("assetType.NAME", DmmAssetTransListDTO.ATTRIBUTE.NAME.getValue());
			sql.addSelectClause("trans.IS_CUP", DmmAssetTransListDTO.ATTRIBUTE.IS_CUP_CHAR.getValue());
			sql.addSelectClause("trans.IS_ENABLED", DmmAssetTransListDTO.ATTRIBUTE.IS_ENABLE_CHAR.getValue());
			sql.addSelectClause("trans.COMMENT", DmmAssetTransListDTO.ATTRIBUTE.COMMENT.getValue());
			sql.addSelectClause("trans.CONTRACT_ID", DmmAssetTransListDTO.ATTRIBUTE.CONTRACT_ID.getValue());
			sql.addSelectClause("contract.CONTRACT_CODE", DmmAssetTransListDTO.ATTRIBUTE.CONTRACT_CODE.getValue());
			sql.addSelectClause("trans.UPDATED_BY_NAME", DmmAssetTransListDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			sql.addSelectClause("trans.ASSET_USER", DmmAssetTransListDTO.ATTRIBUTE.ASSET_USER.getValue());
			sql.addSelectClause("company.SHORT_NAME", DmmAssetTransListDTO.ATTRIBUTE.ASSET_USER_NAME.getValue());
			//CR #2703 歷史轉倉列印出貨單，僅計算成功轉倉資料  2017/11/07
			if (isShowStatus) {
				sql.addSelectClause("status.ITEM_NAME", DmmAssetTransListDTO.ATTRIBUTE.TRANS_STATUS.getValue());
			}
			sql.addSelectClause("CONVERT(varchar(100), trans.UPDATED_DATE, 120)", DmmAssetTransListDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			if (isHistory) {
				stringBuffer.append(schema).append(".DMM_ASSET_TRANS_LIST_HISTORY trans left join ").append(schema);
			} else {
				stringBuffer.append(schema).append(".DMM_ASSET_TRANS_LIST trans left join ").append(schema);
			}
			stringBuffer.append(".BIM_CONTRACT contract on contract.CONTRACT_ID = trans.CONTRACT_ID left join ").append(schema);
			stringBuffer.append(".DMM_REPOSITORY repository on repository.SERIAL_NUMBER = trans.SERIAL_NUMBER left join ").append(schema).append(".DMM_ASSET_TYPE assetType on  assetType.ASSET_TYPE_ID = repository.ASSET_TYPE_ID ");
			stringBuffer.append(" left join ").append(schema).append(".BIM_COMPANY company on company.COMPANY_ID = trans.ASSET_USER ");
			if (isShowStatus) {
				stringBuffer.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF status on status.BPTD_CODE = 'TRANS_STATUS' and status.ITEM_VALUE=trans.TRANS_STATUS ");
			}
			sql.addFromExpression(stringBuffer.toString());
			if (StringUtils.hasText(assetTransId)){
				sql.addWhereClause("trans.ASSET_TRANS_ID =:assetTransId", assetTransId);
			}
			//CR #2703 歷史轉倉列印出貨單，僅計算成功轉倉資料  2017/11/07
			if (historyFlag) {
				sql.addWhereClause("trans.TRANS_STATUS =:transStatus", IAtomsConstants.PARAM_TRANS_STATUS_TRANSFER_SUCCESS);
			}
			//設置排序表達式
			if(StringUtils.hasText(sort) && StringUtils.hasText(order)){
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append(sort).append(IAtomsConstants.MARK_SPACE).append(order);
				sql.setOrderByExpression(stringBuffer.toString());
			} else {
				sql.setOrderByExpression("trans.SERIAL_NUMBER ");
			}
			//設置分頁
			sql.setPageSize(rows.intValue());
			sql.setStartPage(page.intValue() - 1);
			//記錄sql語句
			LOGGER.debug("listBy()", "sql:", sql.toString());
			//創建sqlQueryBean
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			AliasBean aliasBean = sql.createAliasBean(DmmAssetTransListDTO.class);
			//執行查詢
			assetTransList = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listBy()", "is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ASSET_TRANS_INFO)}, e);
		}
		return assetTransList;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetTransInfoDAO#getCount(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Integer getCount(String assetTransId, Boolean isHistory) throws DataAccessException {
		LOGGER.debug("getCount()", "parameters:assetTransId:", assetTransId);
		List<Integer> result = null;
		Integer count = null;
		//得到schema
		String schema = this.getMySchema();
		//創建sqlStatement
		SqlStatement sql = new SqlStatement();
		StringBuffer stringBuffer = new StringBuffer();
		try {
			sql.addSelectClause("count(*)");
			if (isHistory) {
				stringBuffer.append(schema).append(".DMM_ASSET_TRANS_LIST_HISTORY transList");
			} else {
				stringBuffer.append(schema).append(".DMM_ASSET_TRANS_LIST transList");
			}
			sql.setFromExpression(stringBuffer.toString());
			if (StringUtils.hasText(assetTransId)) {
				sql.addWhereClause("transList.ASSET_TRANS_ID =:assetTransId", assetTransId);
			}
			//記錄sql語句
			LOGGER.debug("getCount()", "sql:", sql.toString() );
			//創建SqlQueryBean
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			result = super.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				count = result.get(0);
			}
			//返回查詢總筆數
			return count;
		} catch (Exception e) {
			LOGGER.error("getCount()", "is error:", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ASSET_TRANS_INFO)}, e);
		}
	}
	

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetTransInfoDAO#deleteAssetTransListByAssetTransId(java.lang.String)
	 */
	@Override
	public void deleteAssetTransListByAssetTransId(String assetTransId) throws DataAccessException {
		LOGGER.debug("deleteAssetTransListByAssetTransId()", "parameters:assetTransId:", assetTransId);
		try {
			//得到schema
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append("delete from ").append(schema).append(".DMM_ASSET_TRANS_LIST ");
			if (StringUtils.hasText(assetTransId)) {
				sql.append(" where ASSET_TRANS_ID = :assetTransId");
				sql.setParameter(DmmAssetTransListDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue(), assetTransId);
				super.getDaoSupport().updateByNativeSql(sql);
			}
			//記錄sql語句
			LOGGER.debug("deleteAssetTransListByAssetTransId()", "sql:", sql.toString());
		} catch (Exception e) {
			LOGGER.error("deleteAssetTransListByAssetTransId()", "is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ASSET_TRANS_INFO)}, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetTransInfoDAO#getAssetTransInfoById(java.lang.String)
	 */
	@Override
	 public DmmAssetTransInfoDTO getAssetTransInfoById(String assetTransId) throws DataAccessException {
		LOGGER.debug("getAssetTransInfoById()", "assetTransId:", assetTransId);
		DmmAssetTransInfoDTO result = null;
		String schema = this.getMySchema();
		SqlStatement sqlStatement = new SqlStatement();
		StringBuffer stringBuffer = new StringBuffer();
		try {
			sqlStatement.addSelectClause("info.FROM_WAREHOUSE_ID", DmmAssetTransInfoDTO.ATTRIBUTE.FROM_WAREHOUSE_ID.getValue());
			sqlStatement.addSelectClause("info.TO_WAREHOUSE_ID", DmmAssetTransInfoDTO.ATTRIBUTE.TO_WAREHOUSE_ID.getValue());
			sqlStatement.addSelectClause("info.IS_TRANS_DONE", DmmAssetTransInfoDTO.ATTRIBUTE.IS_TRANS_DONE.getValue());
			sqlStatement.addSelectClause("info.IS_BACK", DmmAssetTransInfoDTO.ATTRIBUTE.IS_BACK.getValue());
			sqlStatement.addSelectClause("info.COMMENT", DmmAssetTransInfoDTO.ATTRIBUTE.COMMENT.getValue());
			sqlStatement.addSelectClause("info.IS_CANCEL", DmmAssetTransInfoDTO.ATTRIBUTE.IS_CANCLE.getValue());
			sqlStatement.addSelectClause("isnull(com.short_name,'') + '-' + house.NAME", DmmAssetTransInfoDTO.ATTRIBUTE.FROM_WAREHOUSE_NAME.getValue());
			sqlStatement.addSelectClause("isnull(company.short_name,'') + '-' + wareHouse.NAME", DmmAssetTransInfoDTO.ATTRIBUTE.TO_WAREHOUSE_NAME.getValue());
			sqlStatement.addSelectClause("isnull(fromHouse.ITEM_NAME,'') + house.ADDRESS", DmmAssetTransInfoDTO.ATTRIBUTE.FROM_WAREHOUSE_ADD.getValue());
			sqlStatement.addSelectClause("isnull(toHouse.item_name,'') + wareHouse.ADDRESS", DmmAssetTransInfoDTO.ATTRIBUTE.TO_WAREHOUSE_ADD.getValue());
			sqlStatement.addSelectClause("house.TEL", DmmAssetTransInfoDTO.ATTRIBUTE.FROM_WAREHOUSE_TEL.getValue());
			sqlStatement.addSelectClause("wareHouse.TEL", DmmAssetTransInfoDTO.ATTRIBUTE.TO_WAREHOUSE_TEL.getValue());
			sqlStatement.addSelectClause("info.CREATED_DATE", DmmAssetTransInfoDTO.ATTRIBUTE.FROM_DATE.getValue());
			stringBuffer.append(schema).append(".DMM_ASSET_TRANS_INFO info left join ").append(schema).append(".BIM_WAREHOUSE house on house.WAREHOUSE_ID = info.FROM_WAREHOUSE_ID ");
			stringBuffer.append(" left join ").append(schema).append(".BIM_WAREHOUSE wareHouse on wareHouse.WAREHOUSE_ID = info.TO_WAREHOUSE_ID left join ").append(schema);
			stringBuffer.append(".BIM_COMPANY company  on wareHouse.COMPANY_ID = company.COMPANY_ID left join ").append(schema).append(".BIM_COMPANY com  on house.COMPANY_ID = com.COMPANY_ID left join ");
			stringBuffer.append(schema).append(".BASE_PARAMETER_ITEM_DEF fromHouse on house.LOCATION = fromHouse.ITEM_VALUE AND fromHouse.BPTD_CODE = 'LOCATION' left join ");
			stringBuffer.append(schema).append(".BASE_PARAMETER_ITEM_DEF toHouse on toHouse.ITEM_VALUE = wareHouse.LOCATION and toHouse.BPTD_CODE='LOCATION'");
			sqlStatement.addFromExpression(stringBuffer.toString());
			if (StringUtils.hasText(assetTransId)) {
				sqlStatement.addWhereClause("info.ASSET_TRANS_ID = :assetTransId", assetTransId);
			}
		//	stringBuffer.delete(0, stringBuffer.length());
		//	stringBuffer.append("ISNULL(info.DELETED, '").append(IAtomsConstants.NO).append("') !=:deleted");
		//	sqlStatement.addWhereClause(stringBuffer.toString(), IAtomsConstants.YES);
			
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmAssetTransInfoDTO.class);
			LOGGER.debug("getAssetTransInfoById()", "SQL---------->", sqlQueryBean.toString());
			List<DmmAssetTransInfoDTO> list = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(list)) {
				result = list.get(0);
			}
		} catch (Exception e) {
			LOGGER.error("getAssetTransInfoById()", "Exception ---->", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return result;
	}
	
	/**
	 * 	@Override(non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO#getWareHouseUserNameList()
	 */
	public List<Parameter> getWareHouseUserNameList(String wareHouseId, Boolean hasWarehouserManager) throws DataAccessException {
		LOGGER.debug("getWareHouseUserNameList()", "wareHouseId", wareHouseId);
		LOGGER.debug("getWareHouseUserNameList()", "hasWarehouserManager", hasWarehouserManager.toString());
		//轉倉批號列表
		List<Parameter> result = null;
		//得到schema
		String schema = this.getMySchema();
		//創建SqlStatement
		SqlStatement sqlStatement = new SqlStatement();
		StringBuffer stringBuffer = new StringBuffer();
		try {
			sqlStatement.addSelectClause("DISTINCT admUser.CNAME", Parameter.FIELD_NAME);
			sqlStatement.addSelectClause("admUser.USER_ID", Parameter.FIELD_VALUE);
			sqlStatement.addFromExpression(schema.concat(".ADM_USER admUser"));
			stringBuffer.append("exists (select 1 from  ");
			stringBuffer.append(schema);
			//Task #3583 客戶廠商倉管 也要出現在通知人員中
			stringBuffer.append(".ADM_USER_ROLE userRole left join dbo.ADM_ROLE admRole on admRole.ROLE_ID = userRole.ROLE_ID where (admRole.ROLE_CODE = :roleCode or (admRole.ROLE_CODE = :cusRoleCode and admRole.ATTRIBUTE=:cusVendor)) and admUser.USER_ID = userRole.USER_ID )");
			stringBuffer.append(" and admUser.DELETED = :deleted and admUser.STATUS = :status");
			stringBuffer.append(" and (admUser.DATA_ACL = :yesOrNo or exists (select 1 from ");
			stringBuffer.append(schema);
			stringBuffer.append(".ADM_USER_WAREHOUSE ware where admUser.USER_ID = ware.USER_ID and ware.WAREHOUSE_ID = :warehouseId))");
			sqlStatement.addWhereClause(stringBuffer.toString());
			//排序
			sqlStatement.setOrderByExpression("admUser.CNAME");
			//記錄sql語句
			LOGGER.debug("getWareHouseUserNameList()", "sql:", sqlStatement.toString() );
			//創建SqlQueryBean
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			//bug2339 update by 2017/08/31 廠商倉管
			sqlQueryBean.setParameter(AdmRoleDTO.ATTRIBUTE.ROLE_CODE.getValue(), IAtomsConstants.FIELD_ROLE_CODE_VENDOR_WAREHOUSE);
			sqlQueryBean.setParameter(AdmUserDTO.ATTRIBUTE.DELETED.getValue(), IAtomsConstants.NO);
			sqlQueryBean.setParameter(AdmUserDTO.ATTRIBUTE.STATUS.getValue(), IAtomsConstants.ACCOUNT_STATUS_NORMAL);
			sqlQueryBean.setParameter("yesOrNo", IAtomsConstants.NO);
			sqlQueryBean.setParameter(WarehouseDTO.ATTRIBUTE.WAREHOUSE_ID.getValue(), wareHouseId);
			sqlQueryBean.setParameter("cusRoleCode", IAtomsConstants.FIELD_ROLE_CODE_CUS_VENDOR_WAREHOUSE) ;
			sqlQueryBean.setParameter("cusVendor", IAtomsConstants.CUSTOMER_VECTOR_ROLE_ATTRIBUTE) ;
			AliasBean aliasBean = new AliasBean(Parameter.class);				
			result = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("getWareHouseUserNameList()", "is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_USER)}, e);
		}
		return result;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetTransInfoDAO#updateAssetTransInfo(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void updateAssetTransInfo(String assetTransId, String status,
			String isListDone, String isCancel, String historyId,
			String userId, String userName, String transAction, String toMailId, String transStatus) throws DataAccessException {
		try {
			//打印參數
			LOGGER.debug("updateAssetTransInfo()", "historyId:",  historyId);
			LOGGER.debug("updateAssetTransInfo()", "assetTransId:", assetTransId);
			LOGGER.debug("updateAssetTransInfo()", "status:", status);
			LOGGER.debug("updateAssetTransInfo()", "isListDone:", isListDone);
			LOGGER.debug("updateAssetTransInfo()", "isCancel:" , isCancel);
			LOGGER.debug("updateAssetTransInfo()", "historyId:", historyId);
			LOGGER.debug("updateAssetTransInfo()", "userId:", userId);
			LOGGER.debug("updateAssetTransInfo()", "userName:", userName);
			LOGGER.debug("updateAssetTransInfo()", "transStatus:", transStatus);
			//得到schema
			//String schema = this.getMySchema();
			//創建SqlStatement
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("exec dbo.usp_Asset_Tranfer_Out :assetTransId, :status, :isListDone, :isTransDone, :isCancel, :historyId, :userId, :userName, :transAction, :toMailId, :transStatus");
			sqlQueryBean.setParameter("assetTransId", assetTransId);
			sqlQueryBean.setParameter("historyId", historyId);
			sqlQueryBean.setParameter("status", StringUtils.hasText(status)?status:"");
			sqlQueryBean.setParameter("isListDone", isListDone);
			sqlQueryBean.setParameter("isTransDone", IAtomsConstants.NO);
			sqlQueryBean.setParameter("isCancel", isCancel);
			sqlQueryBean.setParameter("userId", userId);
			sqlQueryBean.setParameter("userName", userName);
			sqlQueryBean.setParameter("transAction", transAction);
			sqlQueryBean.setParameter("toMailId", toMailId);
			sqlQueryBean.setParameter("transStatus", transStatus);
			LOGGER.debug("updateAssetTransInfo()", "sql:", sqlQueryBean.toString() );
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("updateAssetTransInfo()", "is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE);
		}
		
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetTransInfoDAO#listToWarehouseBy(java.lang.String)
	 */
	public List<Parameter> listToWarehouseBy(String userId) throws DataAccessException {
		LOGGER.debug("listToWarehouseBy()", "userId:", userId);
		List<Parameter> results = null;
		try{
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			StringBuffer stringBuffer = new StringBuffer();
			sqlStatement.addSelectClause("distinct asset.ASSET_TRANS_ID", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("asset.ASSET_TRANS_ID", Parameter.FIELD_NAME);
			stringBuffer.append(schema).append(". DMM_ASSET_TRANS_INFO asset, ").append(schema).append(".ADM_USER admUser");
			sqlStatement.addFromExpression(stringBuffer.toString());
			sqlStatement.addWhereClause(" asset.IS_LIST_DONE = :isListDone");
			sqlStatement.addWhereClause(" asset.IS_TRANS_DONE = :isTransDone");
			sqlStatement.addWhereClause("asset.IS_BACK = :isBack");
		//	stringBuffer.delete(0, stringBuffer.length());
		//	stringBuffer.append(" isnull(asset.DELETED, '").append(IAtomsConstants.NO).append("') = :deleted");
		//	sqlStatement.addWhereClause(stringBuffer.toString());
			if (StringUtils.hasText(userId)) {
				sqlStatement.addWhereClause("admUser.USER_ID = :userId", userId);
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append("(isnull( admUser.DATA_ACL ,'").append(IAtomsConstants.NO).append("') = :dataAcl OR EXISTS(SELECT 1 FROM ").append(schema);
				stringBuffer.append(".ADM_USER_WAREHOUSE uWarehouse  WHERE uWarehouse.USER_ID = admUser.USER_ID and uWarehouse.WAREHOUSE_ID = asset.TO_WAREHOUSE_ID))");
				sqlStatement.addWhereClause(stringBuffer.toString());
			}
			//按轉參批號進行降序排列
			sqlStatement.setOrderByExpression("asset.ASSET_TRANS_ID desc");
			//打印SQL語句
			LOGGER.debug("listToWarehouseBy()", "SQL:", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("isListDone", IAtomsConstants.YES);
			sqlQueryBean.setParameter("isTransDone", IAtomsConstants.NO);
			sqlQueryBean.setParameter("isBack", IAtomsConstants.NO);
		//	sqlQueryBean.setParameter("deleted", IAtomsConstants.NO);
			if (StringUtils.hasText(userId)) {
				sqlQueryBean.setParameter(AdmUserDTO.ATTRIBUTE.DATA_ACL.getValue(), IAtomsConstants.NO);
				sqlQueryBean.setParameter(AdmUserDTO.ATTRIBUTE.USER_ID.getValue(), userId);
			}
			AliasBean alias = new AliasBean(Parameter.class);
			results = this.getDaoSupport().findByNativeSql(sqlQueryBean, alias);
		}catch(Exception e){
			LOGGER.error("listToWarehouseBy()", "is error: ", e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
		return results;
	}	
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetTransInfoDAO#getAssetInfoList(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<Parameter> getAssetInfoList(String userId, Timestamp queryFromDateStart, Timestamp queryFromDateEnd, Timestamp queryToDateStart, Timestamp queryToDateEnd, String queryFromWarehouseId, String queryToWarehouseId)throws DataAccessException{
		LOGGER.debug("getAssetInfoList()", "queryFromDateStart:", queryFromDateStart!=null?queryFromDateStart.toString():null);
		LOGGER.debug("getAssetInfoList()", "queryFromDateEnd:", queryFromDateEnd!=null?queryFromDateEnd.toString():null);
		LOGGER.debug("getAssetInfoList()", "queryToDateStart:", queryToDateStart!=null?queryToDateStart.toString():null);
		LOGGER.debug("getAssetInfoList()", "queryToDateEnd:", queryToDateEnd!=null?queryToDateEnd.toString():null);
		LOGGER.debug("getAssetInfoList()", "queryFromWarehouseId:", queryFromWarehouseId);
		LOGGER.debug("getAssetInfoList()", "queryToWarehouseId:", queryToWarehouseId);
		//轉倉批號列表
		List<Parameter> result = null;
		//得到schema
		String schema = this.getMySchema();
		//創建SqlStatement
		SqlStatement sql = new SqlStatement();
		StringBuffer stringBuffer = new StringBuffer();
		try {
			sql.addSelectClause("distinct info.ASSET_TRANS_ID", Parameter.FIELD_VALUE);
			sql.addSelectClause("info.ASSET_TRANS_ID", Parameter.FIELD_NAME);
			if (StringUtils.hasText(queryToWarehouseId) || StringUtils.hasText(queryToWarehouseId)) {
				stringBuffer.append(schema).append(". DMM_ASSET_TRANS_INFO info ");
				sql.addFromExpression(stringBuffer.toString());
			} else {
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append(schema).append(". DMM_ASSET_TRANS_INFO info, ").append(schema).append(".ADM_USER admUser");
				sql.addFromExpression(stringBuffer.toString());
			}
			//轉出倉
			if (StringUtils.hasText(queryFromWarehouseId)) {
				sql.addWhereClause("info.FROM_WAREHOUSE_ID = :queryFromWarehouseId", queryFromWarehouseId);
			}	
			//轉入倉
			if (StringUtils.hasText(queryToWarehouseId)) {
				sql.addWhereClause("info.TO_WAREHOUSE_ID = :queryToWarehouseId", queryToWarehouseId);
			}
			if (queryFromDateStart != null) {
				sql.addWhereClause("info.TRANS_OUT_DATE >= :queryFromDateStart", queryFromDateStart);
			}
			if (queryFromDateEnd != null) {
				sql.addWhereClause("info.TRANS_OUT_DATE < :queryFromDateEnd", queryFromDateEnd);
			}
			if (queryToDateStart != null) {
				sql.addWhereClause("info.ACCEPTANCE_DATE >= :queryToDateStart", queryToDateStart);
			}
			if (queryToDateEnd != null) {
				sql.addWhereClause("info.ACCEPTANCE_DATE < :queryToDateEnd", queryToDateEnd);
			}
			
			stringBuffer.delete(0, stringBuffer.length());
			stringBuffer.append("(isnull(info.IS_TRANS_DONE,'").append(IAtomsConstants.NO).append("') = :isTransDone or isnull(info.IS_CANCEL,'").append(IAtomsConstants.NO).append("') = :isCancel)");
			sql.addWhereClause(stringBuffer.toString());
			if (!StringUtils.hasText(queryToWarehouseId) && !StringUtils.hasText(queryFromWarehouseId)) {
				sql.addWhereClause("admUser.USER_ID = :userId", userId);
				stringBuffer.delete(0, stringBuffer.length());
				stringBuffer.append("(isnull( admUser.DATA_ACL ,'").append(IAtomsConstants.NO).append("') = :yesOrNo OR EXISTS(SELECT 1 FROM ").append(schema);
				stringBuffer.append(".ADM_USER_WAREHOUSE uWarehouse  WHERE uWarehouse.USER_ID = admUser.USER_ID and (uWarehouse.WAREHOUSE_ID = info.FROM_WAREHOUSE_ID or uWarehouse.WAREHOUSE_ID = info.TO_WAREHOUSE_ID)))");
				sql.addWhereClause(stringBuffer.toString());
			}
			sql.setOrderByExpression("info.ASSET_TRANS_ID desc");
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			if (!StringUtils.hasText(queryToWarehouseId) && !StringUtils.hasText(queryFromWarehouseId)) {
				sqlQueryBean.setParameter("yesOrNo", IAtomsConstants.NO);
			}
			sqlQueryBean.setParameter(DmmAssetTransInfoDTO.ATTRIBUTE.IS_TRANS_DONE.getValue(), IAtomsConstants.YES);
			sqlQueryBean.setParameter(DmmAssetTransInfoDTO.ATTRIBUTE.IS_CANCLE.getValue(), IAtomsConstants.YES);
			LOGGER.debug("getAssetInfoList()", "SQL---------->", sql.toString());
			AliasBean aliasBean = new AliasBean(Parameter.class);
			result = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		}catch (Exception e){
			LOGGER.error("getAssetInfoList()", "is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.REB_IATOMS_TB_NAME_SUPPLIES)}, e);
		}
		return result;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetTransInfoDAO#deleteAssetTransOut(java.util.Date)
	 */
	@Override
	public void deleteAssetTransOut(Date deleteDate) throws DataAccessException {
		try {
			// 沒傳入日期不處理
			if(deleteDate != null){
				String schema = this.getMySchema();
				SqlQueryBean sqlQueryBean = new SqlQueryBean("delete ");
				sqlQueryBean.append(schema).append(".DMM_ASSET_TRANS_LIST_HISTORY ");
				// 刪除設定日期之前的所有設備入庫信息
				sqlQueryBean.append(" where UPDATED_DATE<= :deleteDate");
				
				sqlQueryBean.append("delete ");
				sqlQueryBean.append(schema).append(".DMM_ASSET_TRANS_INFO ");
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
