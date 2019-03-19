package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmComplaintCaseFileDTO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmComplaintCaseFileDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmComplaintCaseFile;

import cafe.core.bean.CoreMessageCode;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

/**
 * Purpose:客訴管理附加檔DAO
 * @author nicklin
 * @since  JDK 1.7
 * @date   2018/03/26
 * @MaintenancePersonnel cybersoft
 */
public class SrmComplaintCaseFileDAO extends GenericBaseDAO<SrmComplaintCaseFile> implements ISrmComplaintCaseFileDAO {

	/**
	 * 系统log物件
	 */
	private static final CafeLog logger = CafeLogFactory.getLog(SrmComplaintCaseFileDAO.class);
	
	/** 
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmComplaintCaseFileDAO#listByCaseId(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SrmComplaintCaseFileDTO> listByCaseId(String fileId,
			String caseId) throws DataAccessException {
		// TODO Auto-generated method stub
		List<SrmComplaintCaseFileDTO> srmComplaintCaseFileDTOs = null;
		try {
			logger.debug("listByCaseId()", "parameters:caseId=" + caseId);
			String schema = this.getMySchema();
			SqlStatement sql = new SqlStatement();
			sql.addSelectClause("complFile.FILE_ID", SrmComplaintCaseFileDTO.ATTRIBUTE.FILE_ID.getValue());
			//sql.addSelectClause("complFile.CASE_ID", SrmComplaintCaseFileDTO.ATTRIBUTE.CASE_ID.getValue());
			sql.addSelectClause("complFile.FILE_NAME", SrmComplaintCaseFileDTO.ATTRIBUTE.FILE_NAME.getValue());
			sql.addSelectClause("complFile.FILE_PATH", SrmComplaintCaseFileDTO.ATTRIBUTE.FILE_PATH.getValue());
			sql.addSelectClause("complFile.CREATED_BY_ID", SrmComplaintCaseFileDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			sql.addSelectClause("complFile.CREATED_BY_NAME", SrmComplaintCaseFileDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			sql.addSelectClause("complFile.CREATED_DATE", SrmComplaintCaseFileDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sql.setFromExpression(schema + ".SRM_COMPLAINT_CASE_FILE complFile");
			
			if (StringUtils.hasText(fileId)) {
				sql.addWhereClause("complFile.FILE_ID = :fileId", fileId);
			}
			if (StringUtils.hasText(caseId)) {
				sql.addWhereClause("complFile.CASE_ID = :caseId", caseId);
			}
			
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			AliasBean aliasBean = sql.createAliasBean(SrmComplaintCaseFileDTO.class);
			logger.debug("listByCaseId()", "sql=" + sqlQueryBean.toString());
			srmComplaintCaseFileDTOs = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch(Exception e) {
			logger.error(this.getClass().getName() + ".listByCaseId() is error " + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return srmComplaintCaseFileDTOs;
	}
}
