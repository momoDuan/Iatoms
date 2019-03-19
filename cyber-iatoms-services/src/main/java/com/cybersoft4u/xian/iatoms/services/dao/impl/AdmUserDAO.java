package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.hibernate.type.StringType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.i18NUtil;
import cafe.core.util.convert.SimpleDtoDmoTransformer;
import cafe.core.util.convert.Transformer;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmUser;

/**
 * Purpose: 使用者DAO
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016年3月30日
 * @MaintenancePersonnel CrissZhang
 */
public class AdmUserDAO extends GenericBaseDAO implements IAdmUserDAO {

	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(AdmUserDAO.class);
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.atoms.services.dao.IAdmUserDAO#listby(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Boolean)
	 */
	@Override
	public List<AdmUserDTO> listby(String account, String cName,
			String companyId, List<String> roleId, String accountStatus,
			Integer pageSize, Integer pageIndex, String sort, String orderby,
			Boolean isPage) throws DataAccessException {
		List<AdmUserDTO> result = null;
		try {
			LOGGER.debug("listBy()", "parameters:account=" + account);
			LOGGER.debug("listBy()", "parameters:cName=" + cName);
			LOGGER.debug("listBy()", "parameters:companyId=" + companyId);
			LOGGER.debug("listBy()", "parameters:roleId=" + roleId);
			LOGGER.debug("listBy()", "parameters:accountStatus=" + accountStatus);
			LOGGER.debug("listBy()", "parameters:pageSize=" + pageSize);
			LOGGER.debug("listBy()", "parameters:pageIndex=" + pageIndex);
			LOGGER.debug("listBy()", "parameters:sort=" + sort);
			LOGGER.debug("listBy()", "parameters:orderby=" + orderby);
			
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("u.USER_ID", AdmUserDTO.ATTRIBUTE.USER_ID.getValue());
			sqlStatement.addSelectClause("u.ACCOUNT", AdmUserDTO.ATTRIBUTE.ACCOUNT.getValue());
			sqlStatement.addSelectClause("u.COMPANY_ID", AdmUserDTO.ATTRIBUTE.COMPANY_ID.getValue());
			sqlStatement.addSelectClause("u.DEPT_CODE", AdmUserDTO.ATTRIBUTE.DEPT_CODE.getValue());
			sqlStatement.addSelectClause("u.PASSWORD", AdmUserDTO.ATTRIBUTE.PASSWORD.getValue());
			sqlStatement.addSelectClause("u.CNAME", AdmUserDTO.ATTRIBUTE.CNAME.getValue());
			sqlStatement.addSelectClause("u.ENAME", AdmUserDTO.ATTRIBUTE.ENAME.getValue());
			sqlStatement.addSelectClause("u.EMAIL", AdmUserDTO.ATTRIBUTE.EMAIL.getValue());
			sqlStatement.addSelectClause("u.MANAGER_EMAIL", AdmUserDTO.ATTRIBUTE.MANAGER_EMAIL.getValue());
			sqlStatement.addSelectClause("u.TEL", AdmUserDTO.ATTRIBUTE.TEL.getValue());
			sqlStatement.addSelectClause("u.MOBILE", AdmUserDTO.ATTRIBUTE.MOBILE.getValue());
			sqlStatement.addSelectClause("u.USER_DESC", AdmUserDTO.ATTRIBUTE.USER_DESC.getValue());
			sqlStatement.addSelectClause("u.DATA_ACL", AdmUserDTO.ATTRIBUTE.DATA_ACL.getValue());
			sqlStatement.addSelectClause("u.RETRY", AdmUserDTO.ATTRIBUTE.RETRY.getValue());
			sqlStatement.addSelectClause("u.STATUS", AdmUserDTO.ATTRIBUTE.STATUS.getValue());
			sqlStatement.addSelectClause("u.LAST_LOGIN_DATE", AdmUserDTO.ATTRIBUTE.LAST_LOGIN_DATE.getValue());
			sqlStatement.addSelectClause("u.CHANGE_PWD_DATE", AdmUserDTO.ATTRIBUTE.CHANGE_PWD_DATE.getValue());			
			sqlStatement.addSelectClause("u.DELETED", AdmUserDTO.ATTRIBUTE.DELETED.getValue());
			sqlStatement.addSelectClause("u.CREATED_BY_ID", AdmUserDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			sqlStatement.addSelectClause("u.CREATED_BY_NAME", AdmUserDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("u.CREATED_DATE", AdmUserDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sqlStatement.addSelectClause("u.UPDATED_BY_ID", AdmUserDTO.ATTRIBUTE.UPDATED_BY_ID.getValue());
			sqlStatement.addSelectClause("u.UPDATED_BY_NAME", AdmUserDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
		//	sqlStatement.addSelectClause("u.UPDATED_DATE", AdmUserDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			sqlStatement.addSelectClause("CONVERT(varchar(100), u.UPDATED_DATE, 120)", AdmUserDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			sqlStatement.addSelectClause("u.RESET_PWD", AdmUserDTO.ATTRIBUTE.RESET_PWD.getValue());
			String schema = this.getMySchema();
			StringBuffer buffer = new StringBuffer();
			buffer.append("STUFF((SELECT ','+ ltrim(r.ROLE_CODE + '-' + r.role_name) FROM ").append(schema).append(".ADM_USER_ROLE ur,");
			buffer.append(schema).append(".ADM_ROLE r ");
			buffer.append("WHERE User_ID=u.User_ID and ur.Role_id = r.Role_id FOR XML PATH('')), 1, 1, '')");
			sqlStatement.addSelectClause(buffer.toString(), AdmUserDTO.ATTRIBUTE.ROLE_GROUP.getValue());
			sqlStatement.addSelectClause("com.SHORT_NAME", AdmUserDTO.ATTRIBUTE.COMPANY.getValue());
			sqlStatement.addSelectClause("dept.DEPT_NAME", AdmUserDTO.ATTRIBUTE.DEPT_NAME.getValue());
			sqlStatement.addSelectClause("base.ITEM_NAME", AdmUserDTO.ATTRIBUTE.ACCOUNT_STATUS.getValue());
			
			buffer.delete(0, buffer.length());
			buffer.append(schema).append(".ADM_USER u ");
			buffer.append("left join ").append(schema).append(".BIM_COMPANY as com on u.COMPANY_ID = com.COMPANY_ID ");
			buffer.append("left join ").append(schema).append(".BIM_DEPARTMENT as dept on u.DEPT_CODE = dept.DEPT_CODE ");
			buffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF as base on u.STATUS = base.ITEM_VALUE and base.BPTD_CODE=:accountStatus");
			sqlStatement.addFromExpression(buffer.toString());
			if (StringUtils.hasText(account)) {
				sqlStatement.addWhereClause("u.ACCOUNT like :account", account + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(cName)) {
				sqlStatement.addWhereClause("u.CNAME like :cName", cName + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(companyId)) {
				sqlStatement.addWhereClause("u.COMPANY_ID = :companyId", companyId);
			} 
			if (!CollectionUtils.isEmpty(roleId)) {
				sqlStatement.addWhereClause("exists(select * from " + schema + ".ADM_USER_ROLE ur where u.USER_ID = ur.USER_ID and ur.ROLE_ID in ( :roleId ))");
			} 
			if (StringUtils.hasText(accountStatus)) {
				sqlStatement.addWhereClause("u.STATUS = :status", accountStatus);
			}
			sqlStatement.addWhereClause("ISNULL(u.DELETED, 'N') =:deleted", IAtomsConstants.NO);
			sqlStatement.setOrderByExpression(sort + " " + orderby);
			
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter(AdmUserDTO.ATTRIBUTE.ACCOUNT_STATUS.getValue(), IATOMS_PARAM_TYPE.ACCOUNT_STATUS.getCode());
			if (!CollectionUtils.isEmpty(roleId)) {
				sqlQueryBean.setParameter(AdmUserDTO.ATTRIBUTE.ROLE_ID.getValue(), roleId);
			} 
			AliasBean aliasBean = sqlStatement.createAliasBean(AdmUserDTO.class);
			if (isPage != null && isPage.booleanValue()) {
				sqlQueryBean.setPageSize(pageSize.intValue());
				sqlQueryBean.setStartPage(pageIndex.intValue() - 1);
			}
			LOGGER.debug("listBy()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listby()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_USER)}, e);
		}
		return result;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.atoms.services.dao.IAdmUserDAO#count(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public int count(String account, String cName, String companyId,
			List<String> roleId, String accountStatus) throws DataAccessException {
		int count = 0;
		try {
			LOGGER.debug("count()", "parameters:account=" + account);
			LOGGER.debug("count()", "parameters:cName=" + cName);
			LOGGER.debug("count()", "parameters:companyId=" + companyId);
			LOGGER.debug("count()", "parameters:roleId=" + roleId);
			LOGGER.debug("count()", "parameters:accountStatus=" + accountStatus);
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			sqlStatement.addFromExpression(schema + ".ADM_USER u");
			if (StringUtils.hasText(account)) {
				sqlStatement.addWhereClause("u.ACCOUNT like :account", account + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(cName)) {
				sqlStatement.addWhereClause("u.CNAME like :cName", cName + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(companyId)) {
				sqlStatement.addWhereClause("u.COMPANY_ID = :companyId", companyId);
			} 
			if (!CollectionUtils.isEmpty(roleId)) {
				sqlStatement.addWhereClause("exists(select * from " + schema + ".ADM_USER_ROLE ur where u.USER_ID = ur.USER_ID and ur.ROLE_ID in ( :roleId ))");
			} 
			if (StringUtils.hasText(accountStatus)) {
				sqlStatement.addWhereClause("u.STATUS = :status", accountStatus);
			}
			sqlStatement.addWhereClause("ISNULL(u.DELETED, 'N') =:deleted", IAtomsConstants.NO);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if (!CollectionUtils.isEmpty(roleId)) {
				sqlQueryBean.setParameter(AdmUserDTO.ATTRIBUTE.ROLE_ID.getValue(), roleId);
			} 
			LOGGER.debug("count()", "sql:" + sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				count = result.get(0).intValue();
			}
			return count;
		} catch (Exception e) {
			LOGGER.error("count()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_USER)}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.atoms.services.dao.IAdmUserDAO#isRepeat(java.lang.String)
	 */
	@Override
	public boolean isRepeat(String account) throws DataAccessException {
		try {
			LOGGER.debug("isRepeat()", "parameters:account=" + account);
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			sqlStatement.addFromExpression(schema + ".ADM_USER u");
			sqlStatement.addWhereClause("ISNULL(u.DELETED, 'N') =:deleted", IAtomsConstants.NO);
			if (StringUtils.hasText(account)) {
				sqlStatement.addWhereClause("u.ACCOUNT = :account", account);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug("isRepeat()", "sql:" + sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				if (result.get(0).intValue() > 0) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			LOGGER.error("isRepeat()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_USER)}, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO#updateUser(com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO)
	 */
	@Override
	public void updateUser(AdmUserDTO admUserDTO) throws DataAccessException {
		try {
			Transformer transformer = new SimpleDtoDmoTransformer();
			AdmUser admUser = new AdmUser();
			transformer.transform(admUserDTO, admUser);
			admUser.setId(admUser.getUserId());
			update(admUser);
		} catch (Exception e) {
			LOGGER.error("updateUser()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_USER)}, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO#listBy(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AdmUserDTO> listBy(String companyId, String deptCode) throws DataAccessException {
		LOGGER.debug("listBy()", "parameters:companyId=" + companyId);
		LOGGER.debug("listBy()", "parameters:deptCode=" + deptCode);
		List<AdmUserDTO> result = null;
		//得到schema
		String schema = this.getMySchema();
		SqlStatement sqlStatement = new SqlStatement();
		try {
			sqlStatement.addSelectClause("u.USER_ID", AdmUserDTO.ATTRIBUTE.USER_ID.getValue());
			sqlStatement.addSelectClause("u.EMAIL", AdmUserDTO.ATTRIBUTE.EMAIL.getValue());
			sqlStatement.addFromExpression(schema + ".ADM_USER u");
			if (StringUtils.hasText(companyId)) {
				sqlStatement.addWhereClause("u.COMPANY_ID = :companyId", companyId);
			} 
			if (StringUtils.hasText(deptCode)) {
				sqlStatement.addWhereClause("u.DEPT_CODE = :deptCode", deptCode);
			} 
			//判斷刪除標誌
			sqlStatement.addWhereClause("ISNULL(u.DELETED, 'N') !=:deleted", IAtomsConstants.YES);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(AdmUserDTO.class);
			LOGGER.debug("listBy()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return result;
		} catch (Exception e) {
			LOGGER.error("listBy()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_USER)}, e);
		}
		
	}
	
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO#getUserList(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@Override
	public List<AdmUserDTO> getUserList(String companyId, String deptCode, String account, String cName, Integer pageSize, Integer pageIndex, String sort, String orderby) throws DataAccessException {
		LOGGER.debug("getUserList()", "parameters:companyId=" + companyId);
		LOGGER.debug("getUserList()", "parameters:deptCode=" + deptCode);
		LOGGER.debug("getUserList()", "parameters:account=" + account);
		LOGGER.debug("getUserList()", "parameters:cName=" + cName);
		LOGGER.debug("getUserList()", "parameters:pageSize=" + pageSize);
		LOGGER.debug("getUserList()", "parameters:pageIndex=" + pageIndex);
		LOGGER.debug("getUserList()", "parameters:sort=" + sort);
		LOGGER.debug("getUserList()", "parameters:orderby=" + orderby);
		List<AdmUserDTO> result = null;
		//得到schema
		String schema = this.getMySchema();
		SqlStatement sqlStatement = new SqlStatement();
		try {
			sqlStatement.addSelectClause("com.SHORT_NAME", AdmUserDTO.ATTRIBUTE.COMPANY.getValue());
			sqlStatement.addSelectClause("dept.DEPT_NAME", AdmUserDTO.ATTRIBUTE.DEPT_NAME.getValue());
			sqlStatement.addSelectClause("u.ACCOUNT", AdmUserDTO.ATTRIBUTE.ACCOUNT.getValue());
			sqlStatement.addSelectClause("u.CNAME", AdmUserDTO.ATTRIBUTE.CNAME.getValue());
			sqlStatement.addSelectClause("u.ENAME", AdmUserDTO.ATTRIBUTE.ENAME.getValue());
			sqlStatement.addSelectClause("u.USER_ID", AdmUserDTO.ATTRIBUTE.USER_ID.getValue());
			sqlStatement.addSelectClause("u.EMAIL", AdmUserDTO.ATTRIBUTE.EMAIL.getValue());
			sqlStatement.addSelectClause("u.MANAGER_EMAIL", AdmUserDTO.ATTRIBUTE.MANAGER_EMAIL.getValue());
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema + ".ADM_USER u");
			buffer.append(" LEFT JOIN ");
			buffer.append(schema).append(".BIM_COMPANY com on u.COMPANY_ID = com.COMPANY_ID ");
			buffer.append(" LEFT JOIN ");
			buffer.append(schema).append(".BIM_DEPARTMENT dept on u.DEPT_CODE = dept.DEPT_CODE ");
			sqlStatement.setFromExpression(buffer.toString());
			if (StringUtils.hasText(companyId)) {
				sqlStatement.addWhereClause("u.COMPANY_ID = :companyId", companyId);
			} 
			if (StringUtils.hasText(deptCode)) {
				sqlStatement.addWhereClause("u.DEPT_CODE = :deptCode", deptCode);
			} 
			if (StringUtils.hasText(account)) {
				sqlStatement.addWhereClause("u.ACCOUNT like :account", account + IAtomsConstants.MARK_PERCENT);
			} 
			if (StringUtils.hasText(cName)) {
				sqlStatement.addWhereClause("u.CNAME like :cName", cName + IAtomsConstants.MARK_PERCENT);
			} 
			//判斷刪除標誌
			sqlStatement.addWhereClause("ISNULL(u.DELETED, 'N') <>:deleted", IAtomsConstants.YES);
			//Task #2478
			sqlStatement.addWhereClause("u.STATUS <>:status", IAtomsConstants.ACCOUNT_STATUS_DISABLED);
			if (StringUtils.hasText(sort) && StringUtils.hasText(orderby)) {
				sqlStatement.setOrderByExpression(sort + " " + orderby);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setPageSize(pageSize.intValue());
			sqlQueryBean.setStartPage(pageIndex.intValue() - 1);
			
			AliasBean aliasBean = sqlStatement.createAliasBean(AdmUserDTO.class);
			LOGGER.debug("getUserList()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return result;
		} catch (Exception e) {
			LOGGER.error("getUserList()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_USER)}, e);
		}
	}
	
	public int countUser(String companyId, String deptCode, String account, String cName) throws DataAccessException {
		int count = 0;
		try {
			LOGGER.debug("countUser()", "parameters:account=" + account);
			LOGGER.debug("countUser()", "parameters:cName=" + cName);
			LOGGER.debug("countUser()", "parameters:companyId=" + companyId);
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema + ".ADM_USER u");
			buffer.append(" LEFT JOIN ");
			buffer.append(schema).append(".BIM_COMPANY com on u.COMPANY_ID = com.COMPANY_ID ");
			buffer.append(" LEFT JOIN ");
			buffer.append(schema).append(".BIM_DEPARTMENT dept on u.DEPT_CODE = dept.DEPT_CODE ");
			sqlStatement.setFromExpression(buffer.toString());
			if (StringUtils.hasText(companyId)) {
				sqlStatement.addWhereClause("u.COMPANY_ID = :companyId", companyId);
			} 
			if (StringUtils.hasText(deptCode)) {
				sqlStatement.addWhereClause("u.DEPT_CODE = :deptCode", deptCode);
			} 
			if (StringUtils.hasText(account)) {
				sqlStatement.addWhereClause("u.ACCOUNT like :account", account + IAtomsConstants.MARK_PERCENT);
			} 
			if (StringUtils.hasText(cName)) {
				sqlStatement.addWhereClause("u.CNAME like :cName", cName + IAtomsConstants.MARK_PERCENT);
			} 
			//判斷刪除標誌
			sqlStatement.addWhereClause("ISNULL(u.DELETED, 'N') <>:deleted", IAtomsConstants.YES);
			//Task #2478
			sqlStatement.addWhereClause("u.STATUS <>:status", IAtomsConstants.ACCOUNT_STATUS_DISABLED);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug("countUser()", "sql:" + sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				count = result.get(0).intValue();
			}
			return count;
		} catch (Exception e) {
			LOGGER.error("countUser()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_USER)}, e);
		}
	}
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO#getUserByDept(java.lang.String)
	 */
	@Override
	public List<Parameter> getUserByDept(String deptCode)
			throws DataAccessException {
		LOGGER.debug("getUserByDept()", "parameters:deptCode=" + deptCode);
		List<Parameter> result = null;
		//得到schema
		String schema = this.getMySchema();
		SqlStatement sqlStatement = new SqlStatement();
		try {
			sqlStatement.addSelectClause("u.USER_ID as value");
			sqlStatement.addSelectClause("u.ACCOUNT as name");
			sqlStatement.addFromExpression(schema + ".ADM_USER u");
			if (StringUtils.hasText(deptCode)) {
				sqlStatement.addWhereClause("u.DEPT_CODE = :deptCode", deptCode);
			} 
			sqlStatement.addWhereClause("u.STATUS = 'NORMAL'");
			//判斷刪除標誌
			sqlStatement.addWhereClause("ISNULL(u.DELETED, 'N') !=:deleted", IAtomsConstants.YES);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			LOGGER.debug("listBy()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return result;
		} catch (Exception e) {
			LOGGER.error("getUserByDept()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_USER)}, e);
		}
	}
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO#getNormalUserEmailList(java.lang.String)
	 */
	@Override
	public List<Parameter> getNormalUserEmailList(String status)
			throws DataAccessException {
		long startQueryEmpEmailTime = System.currentTimeMillis();
		List<Parameter> results = null;
		try{
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append(" SELECT");	
			sql.append(" adm.USER_ID as value ,");
			sql.append(" adm.EMAIL" + "+'('+" +" adm.ACCOUNT " + " +'-'+ " + " adm.CNAME " + "+')'").append("as name ");
			sql.append(" FROM ").append(schema).append( ".ADM_USER adm");
			sql.append(" where adm.EMAIL  is not null and adm.EMAIL  <> '' ");
			//Task #3581 不查已刪除 和 狀態=終止 的mail
			sql.append(" and adm.DELETED='N' and adm.STATUS <> 'DISABLED' ");
			sql.append(" order by adm.EMAIL asc ");
			AliasBean alias = new AliasBean(Parameter.class);
			alias.addScalar(Parameter.FIELD_NAME, StringType.INSTANCE);
			alias.addScalar(Parameter.FIELD_VALUE, StringType.INSTANCE);
			results = this.getDaoSupport().findByNativeSql(sql, alias);
			LOGGER.debug("getNormalUserEmailList()", "sql:" + sql.toString());
		}catch(Exception e){
			LOGGER.error("getNormalUserEmailList()", e.getMessage(), e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
		long endQueryEmpEmailTime = System.currentTimeMillis();
		LOGGER.debug("calculate time --> load", "DAO getNormalUserEmailList:" + (endQueryEmpEmailTime - startQueryEmpEmailTime));
		return results;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO#getUserDTOsBy(java.lang.String)
	 */
	public List<AdmUserDTO> getUserDTOsBy(List<String> roles) throws DataAccessException {
		List<AdmUserDTO> admUserDTOs = null;
		try{
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("DISTINCT admUser.USER_ID", AdmUserDTO.ATTRIBUTE.USER_ID.getValue());
			sqlStatement.addSelectClause("admUser.ACCOUNT", AdmUserDTO.ATTRIBUTE.ACCOUNT.getValue());
			sqlStatement.addSelectClause("admUser.CNAME", AdmUserDTO.ATTRIBUTE.CNAME.getValue());
			sqlStatement.addSelectClause("admUser.EMAIL", AdmUserDTO.ATTRIBUTE.EMAIL.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".ADM_USER admUser ");
			fromBuffer.append("left join ").append(schema).append(".ADM_USER_ROLE userRole on userRole.USER_ID = admUser.USER_ID ");
			fromBuffer.append("left join ").append(schema).append(".ADM_ROLE admRole on admRole.ROLE_ID = userRole.ROLE_ID");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (!CollectionUtils.isEmpty(roles)) {
				sqlStatement.addWhereClause("admRole.ROLE_CODE in ( :roles )");
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if (!CollectionUtils.isEmpty(roles)) {
				sqlQueryBean.setParameter("roles", roles);
			}
			AliasBean aliasBean = sqlStatement.createAliasBean(AdmUserDTO.class);
			LOGGER.debug(".getUserDTOsBy() --> sql: ", sqlQueryBean.toString());
			admUserDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		}catch(Exception e){
			LOGGER.error(".getUserDTOsBy() is error: ", e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
		return admUserDTOs;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO#getUserByRoleId(java.lang.String)
	 */
	public List<AdmUserDTO> getUserByRoleId(String roleId) throws DataAccessException {
		List<AdmUserDTO> admUserDTOs = null;
		try{
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("admUser.USER_ID", AdmUserDTO.ATTRIBUTE.USER_ID.getValue());
			sqlStatement.addSelectClause("admUser.ACCOUNT", AdmUserDTO.ATTRIBUTE.ACCOUNT.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".ADM_USER admUser ");
			fromBuffer.append("left join ").append(schema).append(".ADM_USER_ROLE userRole on userRole.USER_ID = admUser.USER_ID ");
			fromBuffer.append("left join ").append(schema).append(".ADM_ROLE admRole on admRole.ROLE_ID = userRole.ROLE_ID");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(roleId)) {
				sqlStatement.addWhereClause("admRole.ROLE_ID = :roleId", roleId);
			}
			sqlStatement.addWhereClause("ISNULL(admUser.DELETED, 'N') !=:deleted", IAtomsConstants.YES);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(AdmUserDTO.class);
			LOGGER.debug(".getUserByRoleId() --> sql: ", sqlQueryBean.toString());
			admUserDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch(Exception e){
			LOGGER.error(".getUserByRoleId() is error: ", e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
		return admUserDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO#getDeptAgentByCompanyId(java.lang.String)
	 */
	@Override
	public List<Parameter> getDeptAgentByCompanyId(String companyId, Boolean cyberFlag)
			throws DataAccessException {
		List<Parameter> admUserDTOs = null;
		try{
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append(" SELECT distinct ");
			sql.append(" admuser.USER_ID as value, ");
			sql.append(" admuser.EMAIL as name");
			sql.append(" FROM ");
			sql.append(schema).append(".ADM_USER admuser ");
			sql.append(" LEFT JOIN ").append(schema).append(" .ADM_USER_ROLE admrole on admuser.USER_ID = admrole.USER_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .ADM_ROLE role on admrole.ROLE_ID = role.ROLE_ID ");
			sql.append(" where 1=1 ");
			if (StringUtils.hasText(companyId)) {
				sql.append(" AND admuser.COMPANY_ID = :companyId");
				sql.setParameter("companyId", companyId);
			}
			sql.append(" and ((role.ATTRIBUTE = :vendor and role.WORK_FLOW_ROLE = :dept_agent) or (role.ATTRIBUTE = :cusVendor and role.WORK_FLOW_ROLE = :cus_dept_agent)) ");
			if (cyberFlag) {
				//廠商角色 + CyberAgent + 角色代碼為CYBER_AGENT == CYBER_AGENT
				sql.append(" and role.ROLE_CODE = :cyberAgent ");
				//廠商角色 
				sql.setParameter("vendor", IAtomsConstants.VECTOR_ROLE_ATTRIBUTE);
				//角色屬性 CyberAgent
				sql.setParameter("dept_agent", IAtomsConstants.WORK_FLOW_ROLE_CYBER_AGENT);
				//角色代碼為CYBER_AGENT
				sql.setParameter("cyberAgent", IAtomsConstants.CASE_ROLE.CYBER_AGENT.getCode());
			} else {
				//廠商
				sql.setParameter("vendor", IAtomsConstants.VECTOR_ROLE_ATTRIBUTE);
				//部門agent
				sql.setParameter("dept_agent", IAtomsConstants.WORK_FLOW_ROLE_VENDOR_AGENT);
			}
			//客戶廠商
			sql.setParameter("cusVendor", IAtomsConstants.CUSTOMER_VECTOR_ROLE_ATTRIBUTE);
			//(客戶)廠商Agent
			sql.setParameter("cus_dept_agent", IAtomsConstants.WORK_FLOW_ROLE_VENDOR_CUS_AGENT);
			AliasBean alias = new AliasBean(Parameter.class);
			alias.addScalar(Parameter.FIELD_NAME, StringType.INSTANCE);
			alias.addScalar(Parameter.FIELD_VALUE, StringType.INSTANCE);
			admUserDTOs = this.getDaoSupport().findByNativeSql(sql, alias);
			LOGGER.debug("getNormalUserEmailList()", "sql:" + sql.toString());
		} catch(Exception e){
			LOGGER.error(".getUserByRoleId() is error: ", e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
		return admUserDTOs;
	}	
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO#getLastUser()
	 */
	@Override
	public Parameter getLastUser() throws DataAccessException {
		Parameter userParameter = null;
		List<Parameter> admUsers =null;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("USER_ID", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("CNAME", Parameter.FIELD_NAME);
			sqlStatement.addFromExpression(schema + ".ADM_USER");
			sqlStatement.addWhereClause("USER_ID =( select  MAX(USER_ID) from " + schema + ".ADM_USER where ACCOUNT=' ')");
			//打印SQL語句
			LOGGER.debug("getLastUser()", "SQL : ", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			LOGGER.debug(".getUserByRoleId() --> sql: ", sqlQueryBean.toString());
			admUsers = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(admUsers)) {
				userParameter = admUsers.get(0);
			}
		} catch (Exception e) {
			LOGGER.error("getLastUser()", "error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE);
		}
		return userParameter;
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO#getUserAll()
	 */
	@Override
	public List<Parameter> getUserAll() throws DataAccessException {
		List<Parameter> result = null;
		//得到schema
		String schema = this.getMySchema();
		SqlStatement sqlStatement = new SqlStatement();
		try {
			sqlStatement.addSelectClause("USER_ID as value");
			sqlStatement.addSelectClause("CNAME as name");
			sqlStatement.addFromExpression(schema + ".ADM_USER");
			//sqlStatement.addWhereClause("u.STATUS = 'NORMAL'");
			//判斷刪除標誌
			//sqlStatement.addWhereClause("ISNULL(u.DELETED, 'N') !=:deleted", IAtomsConstants.YES);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			LOGGER.debug("getUserAll()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return result;
		} catch (Exception e) {
			LOGGER.error("getUserAll()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_USER)}, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO#deleteTransferData()
	 */
	@Override
	public void deleteTransferData() throws DataAccessException {
		try {
			String schema = this.getMySchema();
			SqlQueryBean sqlQueryBean = new SqlQueryBean("delete ");
//			sqlQueryBean.append(schema).append(".ADM_USER_ROLE where USER_ID <> '1000000000-0001';");
//			
//			sqlQueryBean.append(" delete ");
//			sqlQueryBean.append(schema).append(".ACT_ID_MEMBERSHIP where USER_ID_ = ' ';");
//			
//			sqlQueryBean.append(" delete ");
//			sqlQueryBean.append(schema).append(".ADM_USER_WAREHOUSE where USER_ID <> '1000000000-0001';");
//			
//			sqlQueryBean.append(" delete ");
//			sqlQueryBean.append(schema).append(".ADM_PWD_HISTORY where USER_ID <> '1000000000-0001';");
//			
//			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".ADM_USER where ACCOUNT = ' ';");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_FAULT_DESC; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_FAULT_COM; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY_DESC; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY_COMM; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY; ");
			
			LOGGER.debug("deleteTransferData()", "sql:" + sqlQueryBean.toString());
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("deleteTransferData()", e.getMessage(), e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO#getUserListByCompany(java.lang.String)
	 */
	@Override
	public List<Parameter> getUserListByCompany(String companyId)
			throws DataAccessException {
		List<Parameter> admUserDTOs = null;
		try{
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append(" SELECT distinct ");
			sql.append(" admuser.USER_ID as value, ");
			sql.append(" admuser.CNAME as name");
			sql.append(" FROM ");
			sql.append(schema).append(".ADM_USER admuser ");
			sql.append(" LEFT JOIN ").append(schema).append(" .ADM_USER_ROLE admrole on admuser.USER_ID = admrole.USER_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .ADM_ROLE role on admrole.ROLE_ID = role.ROLE_ID ");
			sql.append(" where 1=1 ");
			if (StringUtils.hasText(companyId)) {
				sql.append(" AND admuser.COMPANY_ID = :companyId");
				sql.setParameter("companyId", companyId);
			}
			//Task #3583  
			sql.append(" and (role.WORK_FLOW_ROLE = :engineer or role.WORK_FLOW_ROLE = :cusEngineer) ");
			//角色表單屬性-工程師
			sql.setParameter("engineer", IAtomsConstants.WORK_FLOW_ROLE_ENGINEER);
			sql.setParameter("cusEngineer", IAtomsConstants.WORK_FLOW_ROLE_CUS_ENGINEER);
			AliasBean alias = new AliasBean(Parameter.class);
			alias.addScalar(Parameter.FIELD_NAME, StringType.INSTANCE);
			alias.addScalar(Parameter.FIELD_VALUE, StringType.INSTANCE);
			admUserDTOs = this.getDaoSupport().findByNativeSql(sql, alias);
			LOGGER.debug("getUserListByCompany()", "sql:" + sql.toString());
		} catch(Exception e){
			LOGGER.error(".getUserListByCompany() is error: ", e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
		return admUserDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO#getUserDTOByCompanyIdAndAccount(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public AdmUserDTO getUserDTOByCompanyIdAndAccount(String companyId, String account, String cName) throws DataAccessException {
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("admUser.USER_ID", AdmUserDTO.ATTRIBUTE.USER_ID.getValue());
			sqlStatement.addSelectClause("admUser.CNAME", AdmUserDTO.ATTRIBUTE.CNAME.getValue());
			sqlStatement.addSelectClause("admUser.ENAME", AdmUserDTO.ATTRIBUTE.ENAME.getValue());
			sqlStatement.addFromExpression(schema + ".ADM_USER admUser");
			if (StringUtils.hasText(companyId)) {
				sqlStatement.addWhereClause("admUser.COMPANY_ID = :companyId", companyId);
			}
			if (StringUtils.hasText(account)) {
				sqlStatement.addWhereClause("admUser.ACCOUNT = :account", account);
			}
			if (StringUtils.hasText(cName)) {
				sqlStatement.addWhereClause("admUser.CNAME = :cName", cName);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(AdmUserDTO.class);
			List<AdmUserDTO> admUserDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(admUserDTOs)) {
				return admUserDTOs.get(0);
			}
			LOGGER.debug("getUserDTOByCompanyIdAndAccount()", "sql:" + sqlQueryBean.toString());
		} catch (Exception e) {
			LOGGER.error(".getUserDTOByCompanyIdAndAccount() is error: ", e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
		return null;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO#getUserEmailById(java.util.List)
	 */
	@Override
	public List<AdmUserDTO> getUserEmailById(List<String> userIds) throws DataAccessException {
		List<AdmUserDTO> admUserDTOs = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("admUser.USER_ID", AdmUserDTO.ATTRIBUTE.USER_ID.getValue());
			sqlStatement.addSelectClause("admUser.EMAIL", AdmUserDTO.ATTRIBUTE.EMAIL.getValue());
			sqlStatement.addFromExpression(schema + ".ADM_USER admUser");
			if (userIds != null) {
				sqlStatement.addWhereClause("admUser.USER_ID in ( :userIds )");
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if (userIds != null) {
				sqlQueryBean.setParameter("userIds", userIds);
			}
			AliasBean aliasBean = sqlStatement.createAliasBean(AdmUserDTO.class);
			admUserDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(".getUserEmailById() is error: ", e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
		return admUserDTOs;
	}
}
