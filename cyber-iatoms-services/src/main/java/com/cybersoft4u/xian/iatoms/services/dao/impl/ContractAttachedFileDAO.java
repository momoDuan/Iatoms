package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

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
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractAttachedFileDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IContractAttachedFileDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimContractAttachedFile;

/**
 * Purpose: 合約文件DAO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2016/9/14
 * @MaintenancePersonnel CarrieDuan
 */
public class ContractAttachedFileDAO extends GenericBaseDAO<BimContractAttachedFile> implements IContractAttachedFileDAO {

	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(ContractAttachedFileDAO.class);

	/***
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IContractAttachedFileDAO#getContractAttachedFileByContractId(java.lang.String)
	 */
	public List<BimContractAttachedFileDTO> getContractAttachedFileByContractId(String contractId) throws DataAccessException {
		List<BimContractAttachedFileDTO> contractAttachedFileDTOs = null;
		try {
			LOGGER.debug(".getContractAttachedFileByContractId()", "parameters:contractId=", contractId);
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("FILE_NAME", BimContractAttachedFileDTO.ATTRIBUTE.FILE_NAME.getValue());
			sqlStatement.addSelectClause("ATTACHED_FILE_ID", BimContractAttachedFileDTO.ATTRIBUTE.ATTACHED_FILE_ID.getValue());
			sqlStatement.addSelectClause("ATTACHED_FILE", BimContractAttachedFileDTO.ATTRIBUTE.ATTACHED_FILE.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".BIM_CONTRACT_ATTACHED_FILE");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(contractId)) {
				sqlStatement.addWhereClause("CONTRACT_ID = :contractId", contractId);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(BimContractAttachedFileDTO.class);
			LOGGER.debug(".getContractAttachedFileByContractId()", "sql:", sqlQueryBean.toString());	
			contractAttachedFileDTOs = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(":getContractAttachedFileByContractId() is error.", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return contractAttachedFileDTOs;
	}

}
