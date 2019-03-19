package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.config.GenericConfigManager;
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
import com.cybersoft4u.xian.iatoms.common.bean.dto.PasswordSettingDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IPasswordSettingDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmSecurityDef;

/**
 * Purpose:密碼原則設定DAO
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2016/5/20
 * @MaintenancePersonnel HermanWang
 */
public class PasswordSettingDAO extends GenericBaseDAO<AdmSecurityDef> implements IPasswordSettingDAO{

	/**
	 * 日志记录物件  
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.DAO, PasswordSettingDAO.class);
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IPasswordSettingDAO#getPasswordSettingInfo()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public PasswordSettingDTO getPasswordSettingInfo() throws DataAccessException {
		SqlStatement sql = new SqlStatement();
		String schema = this.getMySchema();
		try {
			sql.addSelectClause("ID", PasswordSettingDTO.ATTRIBUTE.ID.getValue());
			sql.addSelectClause("PWD_LEN_BG", PasswordSettingDTO.ATTRIBUTE.PWD_LEN_BG.getValue());
			sql.addSelectClause("PWD_LEN_ND", PasswordSettingDTO.ATTRIBUTE.PWD_LEN_ND.getValue());
			sql.addSelectClause("PWD_VALID_DAY", PasswordSettingDTO.ATTRIBUTE.PWD_VALID_DAY.getValue());
			sql.addSelectClause("ID_VALID_DAY", PasswordSettingDTO.ATTRIBUTE.ID_VALID_DAY.getValue());
			sql.addSelectClause("PWD_ERR_CNT", PasswordSettingDTO.ATTRIBUTE.PWD_ERR_CNT.getValue());
			sql.addSelectClause("PWD_RP_CNT", PasswordSettingDTO.ATTRIBUTE.PWD_RP_CNT.getValue());
			sql.addSelectClause("PWD_CHG_FLAG", PasswordSettingDTO.ATTRIBUTE.PWD_CHG_FLAG.getValue());
			sql.addSelectClause("UPDATED_BY_ID", PasswordSettingDTO.ATTRIBUTE.UPDATED_BY_ID.getValue());
			sql.addSelectClause("UPDATED_BY_NAME", PasswordSettingDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			sql.addSelectClause("UPDATED_DATE", PasswordSettingDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			sql.addSelectClause("PWD_ALERT_DAY", PasswordSettingDTO.ATTRIBUTE.PWD_ALERT_DAY.getValue());
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(".ADM_SECURITY_DEF");
			sql.addFromExpression(buffer.toString());
			LOGGER.debug("SQL---------->" + sql.toString());
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			AliasBean aliasBean = sql.createAliasBean(PasswordSettingDTO.class);
			List<PasswordSettingDTO> passwordSettingDTOs = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(passwordSettingDTOs)) {
				return passwordSettingDTOs.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			LOGGER.error(".getPasswordSettingInfo() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_SECURITY_DEF)}, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IPasswordSettingDAO#deletePwdHistory(java.util.Date)
	 */
	@Override
	public void deletePwdHistory(Date deleteDate) throws DataAccessException {
		try {
			// 沒傳入日期不處理
			if(deleteDate != null){
				String schema = this.getMySchema();
				SqlQueryBean sqlQueryBean = new SqlQueryBean("delete ");
				sqlQueryBean.append(schema).append(".ADM_PWD_HISTORY ");
				// 刪除設定日期之前的所有歷史密碼
				sqlQueryBean.append(" where CREATE_DATE<= :deleteDate");
				sqlQueryBean.setParameter("deleteDate", deleteDate);
				LOGGER.debug("deletePwdHistory()", "sql:" + sqlQueryBean.toString());
				this.getDaoSupport().updateByNativeSql(sqlQueryBean);
				this.getDaoSupport().flush();
			}
		} catch (Exception e) {
			LOGGER.error("deletePwdHistory()", e.getMessage(), e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
	}
}
