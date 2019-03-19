package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetTransListDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetTransList;
/**
 * Purpose: 設備轉倉作業明細DAO
 * @author Amanda Wang
 * @since  JDK 1.6
 * @date   2016/7/12
 * @MaintenancePersonnel Amanda Wang
 */
public class DmmAssetTransListDAO extends GenericBaseDAO<DmmAssetTransList> implements IDmmAssetTransListDAO {

	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(DmmAssetTransListDAO.class);
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetTransInfoDAO#deleteAssetTransListByIds(java.lang.String)
	 */
	@Override
	public void deleteAssetTransListById(String assetTransListId, String assetTransId) throws DataAccessException{
		LOGGER.debug("AssetTransListDAO.deleteAssetTransListById()", "assetTransListId:", assetTransListId);
		LOGGER.debug("AssetTransListDAO.deleteAssetTransListById()", "assetTransId:", assetTransId);
		try {
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append("DELETE From ").append(schema).append(".DMM_ASSET_TRANS_LIST ");
			if (StringUtils.hasText(assetTransListId)) {
				//條件--轉倉明細檔id
				sql.append(" WHERE ID = :assetTransListId");
				sql.setParameter("assetTransListId", assetTransListId);
			}
			if (StringUtils.hasText(assetTransId)) {
				sql.append(" WHERE ASSET_TRANS_ID = :assetTransId");
				sql.setParameter("assetTransId", assetTransId);
			}
			//刪除
			super.getDaoSupport().updateByNativeSql(sql);
		} catch (Exception e) {
			LOGGER.error("deleteAssetListByIds()", "Exception --->", e);
			throw new DataAccessException(IAtomsMessageCode.DELETE_FAILURE, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetTransListDAO#isCheckHasSerialNumber(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean isCheckHasSerialNumber(String serialNumber) throws DataAccessException {
		LOGGER.debug("isCheckHasSerialNumber()", "serialNumber:", serialNumber);
		//檢核轉倉清單下是否存在該設備序號
		boolean hasSerialNumber = false;
		List<Integer> result = null;
		try{
			//得到schema
			String schema = this.getMySchema();
			SqlStatement sqlStateMent = new SqlStatement();
			StringBuffer stringBuffer = new StringBuffer();
			sqlStateMent.addSelectClause("count(*)");
			stringBuffer.append(schema).append(".DMM_ASSET_TRANS_LIST a left join ").append(schema).append(".DMM_ASSET_TRANS_INFO info on a.ASSET_TRANS_ID = info.ASSET_TRANS_ID");
			sqlStateMent.addFromExpression(stringBuffer.toString());
			/*sqlStateMent.addFromExpression(schema + ".DMM_ASSET_TRANS_LIST a left join "
					+ schema+".DMM_ASSET_TRANS_INFO info on a.ASSET_TRANS_ID = info.ASSET_TRANS_ID");*/
			if(StringUtils.hasText(serialNumber)){
				sqlStateMent.addWhereClause("a.SERIAL_NUMBER =:serialNumber", serialNumber);
			} 
			//取得尚未確認轉倉的批號
			sqlStateMent.addWhereClause("info.IS_LIST_DONE =:isTransDone", IAtomsConstants.NO);
			stringBuffer.delete(0, stringBuffer.length());
			stringBuffer.append("ISNULL(info.IS_CANCEL, '").append(IAtomsConstants.NO).append("') =:isCancel");
			//取得未取消轉倉的信息
			sqlStateMent.addWhereClause(stringBuffer.toString(), IAtomsConstants.NO);
			//sqlStateMent.addWhereClause("ISNULL(info.IS_CANCEL, 'N') =:isCancel", IAtomsConstants.NO);
			//記錄sql語句
			LOGGER.debug("isCheckHasSerialNumber()", "sql:", sqlStateMent.toString());
			//創建sqlQueryBean
			SqlQueryBean sqlQueryBean = sqlStateMent.createSqlQueryBean();
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				int num = result.get(0).intValue();
				if(num > 0){
					hasSerialNumber = true;
				}
			}
			return hasSerialNumber;
		} catch(Exception e) {
			LOGGER.error("isCheckCompanyHasAdmUser()", "is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_ASSET_TRANS_LIST)}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetTransListDAO#addDmmAssetTransList(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addDmmAssetTransList(String assetTransId, String assetTransListId, String serialNumbers, String comments,
			String createId, String createName) throws DataAccessException {
		LOGGER.debug("addDmmAssetTransList()", "assetTransId:", assetTransId);
		LOGGER.debug("addDmmAssetTransList()", "assetTransListId:", assetTransListId);
		LOGGER.debug("addDmmAssetTransList()", "serialNumbers:", serialNumbers);
		LOGGER.debug("addDmmAssetTransList()", "comments:", comments);
		LOGGER.debug("addDmmAssetTransList()", "createId:", createId);
		LOGGER.debug("addDmmAssetTransList()", "createName:", createName);
		try {
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("exec dbo.usp_Save_Asset_Trans_List :assetTransId, :assetTransListId, :serialNumbers, :createId, :createName, :comments");
			sqlQueryBean.setParameter("assetTransId", assetTransId);
			sqlQueryBean.setParameter("assetTransListId", assetTransListId);
			sqlQueryBean.setParameter("serialNumbers", serialNumbers);
			sqlQueryBean.setParameter("createId", createId);
			sqlQueryBean.setParameter("comments", comments);
			sqlQueryBean.setParameter("createName", createName);
			LOGGER.debug("updateAssetTransInfo()", "sql:", sqlQueryBean.toString() );
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("addDmmAssetTransList()", "is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_ASSET_TRANS_LIST)}, e);
		}
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetTransListDAO#checkSerialNumber(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkSerialNumber(String assetTransId, String serialNumber) throws DataAccessException {
		LOGGER.debug("checkSerialNumber()", "serialNumber:", serialNumber);
		LOGGER.debug("checkSerialNumber()", "assetTransId:", assetTransId);
		//檢核轉倉清單下是否存在該設備序號
		boolean hasSerialNumber = false;
		List<Integer> result = null;
		try{
			//得到schema
			String schema = this.getMySchema();
			SqlStatement sqlStateMent = new SqlStatement();
			StringBuffer stringBuffer = new StringBuffer();
			sqlStateMent.addSelectClause("count(1)");
			stringBuffer.append(schema).append(".DMM_ASSET_TRANS_LIST a left join ").append(schema).append(".DMM_ASSET_TRANS_INFO info on a.ASSET_TRANS_ID = info.ASSET_TRANS_ID");
			sqlStateMent.addFromExpression(stringBuffer.toString());
			if(StringUtils.hasText(serialNumber)){
				sqlStateMent.addWhereClause("a.SERIAL_NUMBER =:serialNumber", serialNumber);
			} 
			if(StringUtils.hasText(assetTransId)){
				sqlStateMent.addWhereClause("a.ASSET_TRANS_ID =:assetTransId", assetTransId);
			}
			//記錄sql語句
			LOGGER.debug("checkSerialNumber()", "sql:", sqlStateMent.toString());
			//創建sqlQueryBean
			SqlQueryBean sqlQueryBean = sqlStateMent.createSqlQueryBean();
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				int num = result.get(0).intValue();
				if(num > 0){
					hasSerialNumber = true;
				}
			}
			return hasSerialNumber;
		} catch(Exception e) {
			LOGGER.error("checkSerialNumber()", "is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_ASSET_TRANS_LIST)}, e);
		}
	}
}
