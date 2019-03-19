package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.MailListDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IMailListDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimMailList;

/**
 * 
 * Purpose: 電子郵件群組維護
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2016-7-4
 * @MaintenancePersonnel CarrieDuan
 */
public class MailListDAO extends GenericBaseDAO<BimMailList> implements IMailListDAO {

	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(MailListDAO.class);
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IMailListDAO#listBy(java.lang.String, java.lang.String, int, int, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MailListDTO> listBy(String mailGroup, String name, int pageIndex, int pageSize, String sort, String order) throws DataAccessException {
		List<MailListDTO> list = null;
		try {
			LOGGER.debug(".listBy()", "parameters:mailGroup=", mailGroup);
			LOGGER.debug(".listBy()", "parameters:name=", name);
			LOGGER.debug(".listBy()", "parameters:pageSize=", pageSize);
			LOGGER.debug(".listBy()", "parameters:pageIndex=", pageIndex);
			LOGGER.debug(".listBy()", "parameters:sort=", sort);
			LOGGER.debug(".listBy()", "parameters:order=", order);
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("m.MAIL_GROUP", MailListDTO.ATTRIBUTE.MAIL_GROUP.getValue());
			sqlStatement.addSelectClause("m.MAIL_ID", MailListDTO.ATTRIBUTE.MAIL_ID.getValue());
			sqlStatement.addSelectClause("bp.ITEM_NAME", MailListDTO.ATTRIBUTE.MAIL_GROUP_NAME.getValue());
			sqlStatement.addSelectClause("m.NAME", MailListDTO.ATTRIBUTE.NAME.getValue());
			sqlStatement.addSelectClause("m.EMAIL", MailListDTO.ATTRIBUTE.EMAIL.getValue());
			sqlStatement.addSelectClause("m.UPDATED_BY_ID", MailListDTO.ATTRIBUTE.UPDATED_BY_ID.getValue());
			sqlStatement.addSelectClause("m.UPDATED_BY_NAME", MailListDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("m.UPDATED_DATE", MailListDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".BIM_MAIL_LIST m");
			fromBuffer.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF bp  on m.MAIL_GROUP = bp.ITEM_VALUE");
			sqlStatement.addFromExpression(fromBuffer.toString());
			sqlStatement.addWhereClause("bp.BPTD_CODE = :sign", IAtomsConstants.PARAM_BPTD_CODE_MAIL_GROUP);
			if (StringUtils.hasText(mailGroup)) {
				sqlStatement.addWhereClause("m.MAIL_GROUP = :mailGroup", mailGroup);
			}
			if (StringUtils.hasText(name)) {
				sqlStatement.addWhereClause("m.NAME like :name");
			}
			if (StringUtils.hasText(order) && StringUtils.hasText(sort)) {
				sqlStatement.setOrderByExpression(sort + " " + order);
			}
			sqlStatement.setPageSize(pageSize);
			sqlStatement.setStartPage(pageIndex - 1);
			AliasBean aliasBean = sqlStatement.createAliasBean(MailListDTO.class);
			SqlQueryBean sql = sqlStatement.createSqlQueryBean();
			if (StringUtils.hasText(name)) {
				sql.setParameter("name", name.concat(IAtomsConstants.MARK_PERCENT));
			}
			LOGGER.debug("SQL-------------->", sql.toString());
			list = super.getDaoSupport().findByNativeSql(sql, aliasBean);
		} catch (Exception e) {
			LOGGER.error(".listBy() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_MAIL_LIST)}, e);
		}
		return list;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IMailListDAO#count(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Integer count(String mailGroup, String name) throws DataAccessException {
		try {
			LOGGER.debug(".count()", "parameters:mailGroup=", mailGroup);
			LOGGER.debug(".count()", "parameters:name=", name);
			String schema = this.getMySchema();
			//查詢總條數
			SqlStatement sqlStatement  = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			sqlStatement.addFromExpression(schema.concat(".BIM_MAIL_LIST"));
			if (StringUtils.hasText(mailGroup)) {
				sqlStatement.addWhereClause("MAIL_GROUP =:mailGroup", mailGroup);
			}
			if (StringUtils.hasText(name)) {
				sqlStatement.addWhereClause("NAME like :name");
			}
			SqlQueryBean sql = sqlStatement.createSqlQueryBean();
			if (StringUtils.hasText(name)) {
				sql.setParameter("name", name.concat(IAtomsConstants.MARK_PERCENT));
			}
			LOGGER.debug("SQL-------------->", sql.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sql);
			if (!CollectionUtils.isEmpty(result)) {
				return result.get(0);
			}	
		} catch (Exception e) {
			LOGGER.error(".count() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_MAIL_LIST)}, e);
		}
		return Integer.valueOf(0);
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IMailListDAO#isCheck(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Boolean isCheck(String mailGroup, String mail, String mailId) throws DataAccessException {
		try {
			LOGGER.debug(".check()", "parameters:mailGroup=", mailGroup);
			LOGGER.debug(".check()", "parameters:mail=", mail);
			LOGGER.debug(".check()", "parameters:mailId=", mailId);
			String schma = this.getMySchema();	
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("COUNT(1)");
			sqlStatement.addFromExpression(schma.concat(".BIM_MAIL_LIST"));
			if (StringUtils.hasText(mailGroup)) {
				sqlStatement.addWhereClause("MAIL_GROUP = :mailGroup", mailGroup);
			}
			if (StringUtils.hasText(mail)) {
				sqlStatement.addWhereClause("EMAIL = :mail", mail);
			}
			if (StringUtils.hasText(mailId)) {
				sqlStatement.addWhereClause("MAIL_ID <> :mailId", mailId);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				if (result.get(0).intValue() > 0) {
					return Boolean.TRUE;
				}
			}
			return Boolean.FALSE;
		} catch (Exception e) {
			LOGGER.error(".check() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_MAIL_LIST)}, e);
		}
	}
}
