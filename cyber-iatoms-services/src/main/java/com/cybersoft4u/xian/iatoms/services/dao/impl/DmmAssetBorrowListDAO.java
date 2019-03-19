package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.springframework.util.CollectionUtils;

import cafe.core.config.GenericConfigManager;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetBorrowListDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetBorrowListDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetBorrowList;
/**
 * Purpose: 設備借用列表DAO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2018年8月1日
 * @MaintenancePersonnel CarrieDuan
 */
public class DmmAssetBorrowListDAO extends GenericBaseDAO<DmmAssetBorrowList> implements IDmmAssetBorrowListDAO {
	/**
	 * 系统日志记录物件  
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.DAO, DmmAssetBorrowListDAO.class);

	/**
	 * Constructor:無參構造器
	 */
	public DmmAssetBorrowListDAO() {
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetBorrowListDAO#listBy(java.lang.String)
	 */
	@Override
	public Integer listBy(String serialNumber) throws DataAccessException {
		// TODO Auto-generated method stub
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("count(1)");
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".DMM_ASSET_BORROW_LIST borrowList ");
			fromBuffer.append("left join ").append(schema).append(".DMM_ASSET_BORROW_INFO borrInfo on borrowList.BORROW_CASE_ID = borrInfo.BORROW_CASE_ID");
			sqlStatement.addFromExpression(fromBuffer.toString());
			sqlStatement.addWhereClause("borrowList.SERIAL_NUMBER = :serialNumber", serialNumber);
			sqlStatement.addWhereClause("borrInfo.BORROW_STATUS = :borrowStatus", IAtomsConstants.FIELD_ASSET_BORROW_STATUS_WAIT_PROCESS);
			sqlStatement.addWhereClause("borrInfo.BORROW_CATEGORY = :borrowCategory", IAtomsConstants.FIELD_ASSET_BORROW_CATEGORY_BORROW);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				return result.get(0).intValue();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}
