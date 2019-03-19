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
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTemplatesDTO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseTemplatesDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseTemplates;
/**
 * Purpose: 工單範本維護DAO
 * @author echomou
 * @since  JDK 1.6
 * @date   2016/9/23
 * @MaintenancePersonnel echomou
 */
public class SrmCaseTemplatesDAO  extends GenericBaseDAO<SrmCaseTemplates> implements ISrmCaseTemplatesDAO{

	/**
	 * 系统日志记录物件 
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(SrmCaseTemplatesDAO.class);
	/**
	 * 
	 * Constructor:構造器
	 */
	public SrmCaseTemplatesDAO() {
		super();
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseTemplatesDAO#listBy(java.lang.String, java.lang.String)
	 */
	@Override
	public List<SrmCaseTemplatesDTO> listBy(String fileName, String category)
			throws DataAccessException {
		LOGGER.debug(".listBy()", "parameters:fileName:", fileName );
		LOGGER.debug(".listBy()", "parameters:category:", category );
		List<SrmCaseTemplatesDTO> listsrmCaseTemplatesDTO= null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectColumn("caseTemp.ID", SrmCaseTemplatesDTO.ATTRIBUTE.ID.getValue());
			sqlStatement.addSelectColumn("caseTemp.FILE_PATH", SrmCaseTemplatesDTO.ATTRIBUTE.FILE_PATH.getValue());
			sqlStatement.addSelectColumn("caseTemp.FILE_NAME", SrmCaseTemplatesDTO.ATTRIBUTE.FILE_NAME.getValue());
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(" .SRM_CASE_TEMPLATES caseTemp ");
			sqlStatement.addFromExpression(buffer.toString());
			//範本名稱
			if (StringUtils.hasText(fileName)) {
				sqlStatement.addWhereClause("caseTemp.FILE_NAME = :fileName", fileName);
			}
			//範本類別
			if (StringUtils.hasText(category)) {
				sqlStatement.addWhereClause("caseTemp.CATEGORY = :category", category);
			}
			sqlStatement.addWhereClause("ISNULL(caseTemp.DELETED, 'N') =:deleted", IAtomsConstants.NO);
			//記錄sql語句
			LOGGER.debug(".listBy()", " Native SQL---------->", sqlStatement.toString());
			//根據行和頁分出來.
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseTemplatesDTO.class);
			listsrmCaseTemplatesDTO = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(".listBy() is error", "DAO Exception", e);
			throw new DataAccessException(CoreMessageCode.QUERY_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.REB_IATOMS_TB_NAME_SUPPLIES)}, e);
		}
		return listsrmCaseTemplatesDTO;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseTemplatesDAO#isRepeat(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isRepeat(String fileName, String category)
			throws DataAccessException {
		try {
			LOGGER.debug(".isRepeat()", "parameters:fileName=", fileName);
			LOGGER.debug(".isRepeat()", "parameters:category=", category);
			String schema = this.getMySchema();	
			SqlStatement sqlStatement = new SqlStatement();
			StringBuffer buffer = new StringBuffer();
			sqlStatement.addSelectClause("count(1)");
			buffer.append(schema).append(" .SRM_CASE_TEMPLATES caseTemp ");
			sqlStatement.addFromExpression(buffer.toString());
			//範本名稱
			if (StringUtils.hasText(fileName)) {
				sqlStatement.addWhereClause("caseTemp.FILE_NAME = :fileName", fileName);
			}
			//範本類別
			if (StringUtils.hasText(category)) {
				sqlStatement.addWhereClause("caseTemp.CATEGORY = :category", category);
			}
			sqlStatement.addWhereClause("ISNULL(caseTemp.DELETED, 'N') =:deleted", IAtomsConstants.NO);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug(".isRepeat()", "sql:", sqlQueryBean.toString());
			List<Integer> results = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			//如果查到返回false
			if (!CollectionUtils.isEmpty(results)) {
				if (results.get(0).intValue() > 0) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			LOGGER.error(".isRepeat() is error", "DAO Exception", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseTemplatesDAO#getUploadTemplatesId(java.lang.String, java.lang.String)
	 */
	@Override
	public List<SrmCaseTemplatesDTO> getUploadTemplatesId(String category, String fileName)
			throws DataAccessException {
		LOGGER.debug(".listBy()", "parameters:fileName:", fileName );
		LOGGER.debug(".listBy()", "parameters:category:", category );
		List<SrmCaseTemplatesDTO> srmCaseTemplatesDTO= null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectColumn("caseTemp.ID", SrmCaseTemplatesDTO.ATTRIBUTE.ID.getValue());
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(" .SRM_CASE_TEMPLATES caseTemp ");
			sqlStatement.addFromExpression(buffer.toString());
			//範本名稱
			if (StringUtils.hasText(fileName)) {
				sqlStatement.addWhereClause("caseTemp.FILE_NAME = :fileName", fileName);
			}
			//範本類別
			if (StringUtils.hasText(category)) {
				sqlStatement.addWhereClause("caseTemp.CATEGORY = :category", category);
			}
			sqlStatement.addWhereClause("ISNULL(caseTemp.DELETED, 'N') =:deleted", IAtomsConstants.NO);
			//記錄sql語句
			LOGGER.debug(".getUploadTemplatesId()", " Native SQL---------->", sqlStatement.toString());
			//根據行和頁分出來.
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseTemplatesDTO.class);
			srmCaseTemplatesDTO = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(".getUploadTemplatesId() is error", "DAO Exception", e);
			throw new DataAccessException(CoreMessageCode.QUERY_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.REB_IATOMS_TB_NAME_SUPPLIES)}, e);
		}
		return srmCaseTemplatesDTO;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseTemplatesDAO#getTemplatesList()
	 */
	@Override
	public List<Parameter> getTemplatesList() throws DataAccessException {
		List<Parameter> list = null;
		long startQueryTemplatesTime = System.currentTimeMillis();
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("caseTemp.ID", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("caseTemp.FILE_NAME", Parameter.FIELD_NAME);
			sqlStatement.addFromExpression(schema + ".SRM_CASE_TEMPLATES caseTemp");
			sqlStatement.addWhereClause("ISNULL(caseTemp.DELETED, 'N') =:deleted", IAtomsConstants.NO);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			LOGGER.debug(".getTemplatesList()", "sql:", sqlQueryBean.toString());
			list = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(".getTemplatesList() is error", "DAO Exception", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		long endQueryTemplatesTime = System.currentTimeMillis();
		LOGGER.debug("calculate time --> load", "DAO getTemplatesList:" + (endQueryTemplatesTime - endQueryTemplatesTime));
		return list;
	}
}
