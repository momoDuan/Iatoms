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
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ApiCmsRepositoryDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IApiCmsRepositoryDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.ApiCmsRepository;

public class ApiCmsRepositoryDAO extends GenericBaseDAO<ApiCmsRepository> implements IApiCmsRepositoryDAO {
	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(ApiCmsRepositoryDAO.class);
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IApiCmsRepositoryDAO#listBy()
	 */
	@Override
	public List<ApiCmsRepositoryDTO> listBy() throws DataAccessException {
		List<ApiCmsRepositoryDTO> result = null;
		//得到schema
		String schema = this.getMySchema();
		SqlStatement sqlStatement = new SqlStatement();
		try {
			sqlStatement.addSelectClause("SYSTEM_ID", ApiCmsRepositoryDTO.ATTRIBUTE.SYSTEM_ID.getValue());
			sqlStatement.addSelectClause("SERIAL_NUMBER", ApiCmsRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("MERCHANT_CODE", ApiCmsRepositoryDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("DTID", ApiCmsRepositoryDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("MEMO", ApiCmsRepositoryDTO.ATTRIBUTE.MEMO.getValue());
			sqlStatement.addSelectClause("JOB_TYPE", ApiCmsRepositoryDTO.ATTRIBUTE.JOB_TYPE.getValue());
			sqlStatement.addSelectClause("NOTIFY_FLAG", ApiCmsRepositoryDTO.ATTRIBUTE.NOTIFY_FLAG.getValue());
			sqlStatement.addSelectClause("UPDATED_DATE", ApiCmsRepositoryDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			sqlStatement.addSelectClause("CREATED_DATE", ApiCmsRepositoryDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sqlStatement.addSelectClause("(case when JOB_TYPE='05' then '已停用' else '使用中' end )", ApiCmsRepositoryDTO.ATTRIBUTE.STATUS.getValue());
			sqlStatement.addFromExpression(schema + ".API_CMS_REPOSITORY");
			
			sqlStatement.addWhereClause("NOTIFY_FLAG = :flag", "0");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(ApiCmsRepositoryDTO.class);
			LOGGER.debug(".listBy()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return result;
		} catch (Exception e) {
			LOGGER.error(".listBy()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_USER)}, e);
		}
		
	}
}
