package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import cafe.core.bean.CoreMessageCode;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAttFileDTO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAttFileDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseAttFile;
/**
 * Purpose: 案件附加檔 
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016年12月16日
 * @MaintenancePersonnel CrissZhang
 */
public class SrmCaseAttFileDAO extends GenericBaseDAO<SrmCaseAttFile> implements ISrmCaseAttFileDAO {

	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(SrmCaseAttFileDAO.class);
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAttFileDAO#listByCaseId(java.lang.String)
	 */
	@Override
	public List<SrmCaseAttFileDTO> listByCaseId(String caseId, String attFileId, String isHistory)throws DataAccessException {
		List<SrmCaseAttFileDTO> caseAttFileDTOs = null;
		try{
			LOGGER.debug("listByCaseId()", "parameters:caseId=" + caseId);
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("caseAttFile.ATT_FILE_ID", SrmCaseAttFileDTO.ATTRIBUTE.ATT_FILE_ID.getValue());
			sqlStatement.addSelectClause("caseAttFile.CASE_ID", SrmCaseAttFileDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("caseAttFile.FILE_NAME", SrmCaseAttFileDTO.ATTRIBUTE.FILE_NAME.getValue());
			sqlStatement.addSelectClause("caseAttFile.FILE_PATH", SrmCaseAttFileDTO.ATTRIBUTE.FILE_PATH.getValue());
			sqlStatement.addSelectClause("caseAttFile.CREATED_BY_ID", SrmCaseAttFileDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			sqlStatement.addSelectClause("caseAttFile.CREATED_BY_NAME", SrmCaseAttFileDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("caseAttFile.CREATED_DATE", SrmCaseAttFileDTO.ATTRIBUTE.CREATED_DATE.getValue());
			// 判斷標志位
			if(StringUtils.hasText(isHistory) && IAtomsConstants.YES.equals(isHistory)){
				sqlStatement.addFromExpression(schema + ".SRM_HISTORY_CASE_ATT_FILE caseAttFile");
			} else {
				sqlStatement.addFromExpression(schema + ".SRM_CASE_ATT_FILE caseAttFile");
			}
			if (StringUtils.hasText(caseId)) {
				sqlStatement.addWhereClause("caseAttFile.CASE_ID = :caseId", caseId);
			}
			if (StringUtils.hasText(attFileId)) {
				sqlStatement.addWhereClause("caseAttFile.ATT_FILE_ID = :attFileId", attFileId);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseAttFileDTO.class);
			LOGGER.debug("listByCaseId()", "sql:" + sqlQueryBean.toString());
			caseAttFileDTOs = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listByCaseId()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return caseAttFileDTOs;
	}

}
