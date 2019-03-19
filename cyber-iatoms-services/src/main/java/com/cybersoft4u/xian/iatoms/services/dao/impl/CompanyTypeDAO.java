package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.CoreMessageCode;
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
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyTypeDTO;
import com.cybersoft4u.xian.iatoms.services.dao.ICompanyTypeDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCompanyType;
/**
 * Purpose: 公司類型DAO
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2016/8/30
 * @MaintenancePersonnel ElvaHe
 */
public class CompanyTypeDAO extends GenericBaseDAO<BimCompanyType> implements ICompanyTypeDAO {

	/**
	 * 日誌記錄物件
	 */
	private static final CafeLog LOGEER = CafeLogFactory.getLog(CompanyTypeDAO.class);
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ICompanyTypeDAO#listByCompanyId(java.lang.String)
	 */
	@Override
	public List<CompanyTypeDTO> listByCompanyId(String companyId) throws DataAccessException {
		List<CompanyTypeDTO> companyTypeDTOs = null;
		try {
			LOGEER.debug("listByCompanyId()","companyId : ", companyId);
			SqlStatement sqlSatement = new SqlStatement();
			String schema = this.getMySchema();
			StringBuffer stringBuffer = new StringBuffer();
			//拼接SQL
			sqlSatement.addSelectClause("companyType.COMPANY_TYPE", CompanyTypeDTO.ATTRIBUTE.COMPANY_TYPE.getValue());
			stringBuffer.append(schema).append(".BIM_COMPANY_TYPE companyType");
			sqlSatement.addFromExpression(stringBuffer.toString());
			//存在公司編號
			if (StringUtils.hasText(companyId)) {
				sqlSatement.addWhereClause("companyType.COMPANY_ID = :companyId", companyId);
			}
			SqlQueryBean sqlQueryBean = sqlSatement.createSqlQueryBean();
			AliasBean aliasBean = new AliasBean(CompanyTypeDTO.class);
			//符合查詢條件的集合
			companyTypeDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGEER.error("listByCompanyId()","error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,new String[] {i18NUtil.getName(IAtomsConstants.REB_IATOMS_TB_NAME_COMPANY)},e);
		}
		return companyTypeDTOs;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ICompanyTypeDAO#deleteByCompanyId(java.lang.String)
	 */
	@Override
	public void deleteByCompanyId(String companyId) throws DataAccessException {
		try {
			LOGEER.debug("deleteByCompanyId()","companyId : ", companyId);
			//若存在公司編號
			if(StringUtils.hasText(companyId)){
				SqlQueryBean sqlQueryBean = new SqlQueryBean();
				String schema = this.getMySchema();
				sqlQueryBean.append("delete from ").append(schema).append(".BIM_COMPANY_TYPE where ");
				sqlQueryBean.append("COMPANY_ID =:companyId");
				sqlQueryBean.setParameter(CompanyTypeDTO.ATTRIBUTE.COMPANY_ID.getValue(), companyId);
				LOGEER.debug("deleteByCompanyId()","SQL : ", sqlQueryBean.toString());
				this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			}
		} catch (Exception e) {
			LOGEER.error("deleteByCompanyId()","error :",e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ICompanyTypeDAO#checkCompanyType(java.lang.String, java.lang.String)
	 */
	public boolean checkCompanyType(String companyName, String companyType) throws DataAccessException {
		try {
			LOGEER.debug("checkCompanyType()","companyName : ", companyName);
			LOGEER.debug("checkCompanyType()","companyType : ", companyType);
			String schma = this.getMySchema();	
			SqlStatement sqlStatement = new SqlStatement();
			StringBuffer stringBuffer = new StringBuffer();
			sqlStatement.addSelectClause("count(1)");
			stringBuffer.append(schma).append(".BIM_COMPANY_TYPE type left join ").append(schma).append(".BIM_COMPANY company on type.COMPANY_ID = company.COMPANY_ID");
			sqlStatement.addFromExpression(stringBuffer.toString());
			//存在公司簡稱
			if (StringUtils.hasText(companyName)) {
				sqlStatement.addWhereClause("company.SHORT_NAME = :companyName", companyName);
			}
			//公司類型
			if (StringUtils.hasText(companyType)) {
				sqlStatement.addWhereClause("type.COMPANY_TYPE = :companyType", companyType);
			}
			sqlStatement.addWhereClause(" company.DELETED = :deleted", IAtomsConstants.NO);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGEER.debug("checkCompanyType()", "sql:", sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				//存在查詢結果時返回true
				if (result.get(0).intValue() > 0) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			LOGEER.error("checkCompanyType()","error :",e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,e);
		}
	}
	

}
