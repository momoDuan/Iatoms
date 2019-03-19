package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.util.CollectionUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.workflow.dao.identity.impl.WfIdentityDAO;

import com.cybersoft4u.xian.iatoms.common.IAtomsAccessControl;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmFunctionTypeDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.RolePermissionDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IIAtomsIdentityDAO;

/**
 * Purpose:识别管理DAO介面实现 
 * @author candicechen
 * @since  JDK 1.6
 * @date   2015年4月20日
 * @MaintenancePersonnel candicechen
 */
@SuppressWarnings("unchecked")
public class IAtomsIdentityDAO extends WfIdentityDAO implements IIAtomsIdentityDAO {
	
	private static final CafeLog log = CafeLogFactory.getLog(IAtomsIdentityDAO.class);
	/**
	 * Constructor:无参构造函数
	 */
	public IAtomsIdentityDAO() {
		super();
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IIAtomsIdentityDAO#listRoleByUserId(java.lang.String)
	 */
	@Override
	public List<AdmRoleDTO> listRoleByUserId(String userId)
			throws DataAccessException {
		List<AdmRoleDTO> roles = null;
		try{
			String schema = this.getMySchema();
			SqlStatement sql = new SqlStatement();
			sql.addSelectClause("r.ROLE_ID",AdmRoleDTO.ATTRIBUTE.ROLE_ID.getValue().toString());
			sql.addSelectClause("r.ROLE_NAME",AdmRoleDTO.ATTRIBUTE.ROLE_NAME.getValue().toString());
			sql.addSelectClause("r.ROLE_CODE",AdmRoleDTO.ATTRIBUTE.ROLE_CODE.getValue().toString());
			sql.addSelectClause("r.ROLE_DESC",AdmRoleDTO.ATTRIBUTE.ROLE_DESC.getValue().toString());
			sql.addSelectClause("r.ATTRIBUTE",AdmRoleDTO.ATTRIBUTE.ATTRIBUTE.getValue().toString());
			sql.addSelectClause("r.WORK_FLOW_ROLE",AdmRoleDTO.ATTRIBUTE.WORK_FLOW_ROLE.getValue().toString());
			sql.addSelectClause("r.CREATED_BY_ID",AdmRoleDTO.ATTRIBUTE.CREATED_BY_ID.getValue().toString());
			sql.addSelectClause("r.CREATED_BY_NAME",AdmRoleDTO.ATTRIBUTE.CREATED_BY_NAME.getValue().toString());
			sql.addSelectClause("r.CREATED_DATE",AdmRoleDTO.ATTRIBUTE.CREATED_DATE.getValue().toString());
			sql.addSelectClause("r.UPDATED_BY_ID",AdmRoleDTO.ATTRIBUTE.UPDATED_BY_ID.getValue().toString());
			sql.addSelectClause("r.UPDATED_BY_NAME",AdmRoleDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue().toString());
			sql.addSelectClause("r.UPDATED_DATE",AdmRoleDTO.ATTRIBUTE.UPDATED_DATE.getValue().toString());
			sql.addFromExpression(schema+".ADM_ROLE r "
								+" left join "+schema+".ADM_USER_ROLE ur "
								+" on ur.ROLE_ID = r.ROLE_ID ");
			sql.addWhereClause("ur.USER_ID = :userId",userId);
			AliasBean aliasBean = sql.createAliasBean(AdmRoleDTO.class);
			roles = this.getDaoSupport().findByNativeSql(sql.createSqlQueryBean(), aliasBean);
		}catch(Throwable e){			
			log.debug(this.getClass().getName()+".listRoleByUserId is error -->", e);
        	throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,e);
		}
		return roles;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IIAtomsIdentityDAO#getAdmUserDTOByAccount(java.lang.String)
	 */
	@Override
	public AdmUserDTO getAdmUserDTOByAccount(String account) throws DataAccessException {
		try {
			log.debug(this.getClass().getName() + ".getAdmUserDTOByAccount()", "parameters:account=" + account);
			if (!StringUtils.hasText(account)) {
				return null;
			}
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("u.USER_ID",AdmUserDTO.ATTRIBUTE.USER_ID.getValue());
			sqlStatement.addSelectClause("u.ACCOUNT",AdmUserDTO.ATTRIBUTE.ACCOUNT.getValue());
			sqlStatement.addSelectClause("u.COMPANY_ID",AdmUserDTO.ATTRIBUTE.COMPANY_ID.getValue());
			sqlStatement.addSelectClause("u.DEPT_CODE",AdmUserDTO.ATTRIBUTE.DEPT_CODE.getValue());
			sqlStatement.addSelectClause("u.PASSWORD",AdmUserDTO.ATTRIBUTE.PASSWORD.getValue());
			sqlStatement.addSelectClause("u.CNAME",AdmUserDTO.ATTRIBUTE.CNAME.getValue());
			sqlStatement.addSelectClause("u.ENAME",AdmUserDTO.ATTRIBUTE.ENAME.getValue());
			sqlStatement.addSelectClause("u.MANAGER_EMAIL",AdmUserDTO.ATTRIBUTE.MANAGER_EMAIL.getValue());
			sqlStatement.addSelectClause("u.TEL",AdmUserDTO.ATTRIBUTE.TEL.getValue());
			sqlStatement.addSelectClause("u.MOBILE",AdmUserDTO.ATTRIBUTE.MOBILE.getValue());
			sqlStatement.addSelectClause("u.EMAIL",AdmUserDTO.ATTRIBUTE.EMAIL.getValue());
			sqlStatement.addSelectClause("u.MANAGER_EMAIL",AdmUserDTO.ATTRIBUTE.MANAGER_EMAIL.getValue());
			sqlStatement.addSelectClause("u.USER_DESC",AdmUserDTO.ATTRIBUTE.USER_DESC.getValue());
			sqlStatement.addSelectClause("u.DATA_ACL",AdmUserDTO.ATTRIBUTE.DATA_ACL.getValue());
			sqlStatement.addSelectClause("u.RETRY",AdmUserDTO.ATTRIBUTE.RETRY.getValue());
			sqlStatement.addSelectClause("u.STATUS",AdmUserDTO.ATTRIBUTE.STATUS.getValue());
			sqlStatement.addSelectClause("u.LAST_LOGIN_DATE",AdmUserDTO.ATTRIBUTE.LAST_LOGIN_DATE.getValue());
			sqlStatement.addSelectClause("u.CHANGE_PWD_DATE",AdmUserDTO.ATTRIBUTE.CHANGE_PWD_DATE.getValue());			
			sqlStatement.addSelectClause("u.DELETED",AdmUserDTO.ATTRIBUTE.DELETED.getValue());
			sqlStatement.addSelectClause("u.CREATED_BY_ID",AdmUserDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			sqlStatement.addSelectClause("u.CREATED_BY_NAME",AdmUserDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("u.CREATED_DATE",AdmUserDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sqlStatement.addSelectClause("u.UPDATED_BY_ID",AdmUserDTO.ATTRIBUTE.UPDATED_BY_ID.getValue());
			sqlStatement.addSelectClause("u.UPDATED_BY_NAME",AdmUserDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("u.UPDATED_DATE",AdmUserDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			sqlStatement.addSelectClause("u.RESET_PWD",AdmUserDTO.ATTRIBUTE.RESET_PWD.getValue());
			String schma = this.getMySchema();			
			sqlStatement.addFromExpression(schma + ".ADM_USER as u");
			sqlStatement.addWhereClause("u.ACCOUNT = :account", account);
			sqlStatement.addWhereClause("ISNULL(u.DELETED, 'N') = :deleted", IAtomsConstants.NO);
			AliasBean aliasBean = sqlStatement.createAliasBean(AdmUserDTO.class);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			log.debug(this.getClass().getName() + ".getAdmUserDTOByAccount()", "sql:" + sqlQueryBean.toString());
			List<AdmUserDTO> result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(result)) {
				return result.get(0);
			}
			return null;
		} catch (Exception e) {
			log.error(this.getClass().getName() + ".getAdmUserDTOByAccount() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_USER)},e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IIAtomsIdentityDAO#getAvailableAccessControlList(com.cybersoft4u.xian.FotileLogonUser.common.CmsLogonUser)
	 */
	public List<IAtomsAccessControl> getAvailableAccessControlList(IAtomsLogonUser logonUser) throws DataAccessException{
		List<IAtomsAccessControl> accessControls = null;
		try {
			IAtomsLogonUser iAtomsLogonUser = (IAtomsLogonUser)logonUser;
			if(iAtomsLogonUser == null){
				log.error(this.getClass().getName()+".getAvailableAccessControlList is error -->cmsLogonUser is null");
	        	throw new DataAccessException(CoreMessageCode.LOGIN_ID_NOT_FOUND);
			}
			SqlQueryBean sql = new SqlQueryBean();
			String schema = this.getMySchema();
			/*sql.append(" SELECT DISTINCT T1.FUNCTION_CATEGORY AS resourceType,")     
		      .append(" T1.FUNCTION_CODE AS id,T1.FUNCTION_NAME AS name,")         
		      .append(" T1.FUNCTION_URL AS resourceUrl,T1.FUNCTION_ORDER AS sequence,")        
		      .append(" T2.ACCESS_RIGHT AS accessRight,")
		      .append(" T3.FUNCTION_CODE AS parentId,  ")
		      .append(" T3.FUNCTION_NAME AS parentName ")
		      .append(" FROM "+schema+".ADM_FUNCTION_TYPE T1")
		      .append(" LEFT JOIN "+schema+".ADM_FUNCTION_TYPE T3 ON T3.FUNCTION_ID = T1.PARENT_FUNCTION_ID")
		      .append(" left join "+schema+".ADM_PERMISSION T2 on T2.FUNCTION_ID = T1.FUNCTION_ID")
		      .append(" left join "+schema+".ADM_ROLE_PERMISSION T4 on  T4.TYPE =:type AND T4.STATUS =:status and T4.PERMISSION_ID = t2.PERMISSION_ID") 
		      .append(" left join "+schema+".ADM_USER_ROLE t5 on t5.ROLE_ID = t4.ROLE_ID")
		      .append(" where T1.STATUS =:status")
		      .append(" and t5.USER_ID = :userId ")
		      .append(" ORDER BY sequence asc,resourceType,parentId");*/
			
			sql.append("SELECT DISTINCT T1.FUNCTION_CATEGORY AS resourceType ,")
			  .append(" T1.FUNCTION_CODE AS id,T1.FUNCTION_NAME AS name,")         
		      .append(" T1.FUNCTION_URL AS resourceUrl,T1.FUNCTION_ORDER AS sequence,")        
		      .append(" T2.ACCESS_RIGHT AS accessRight,")
		      .append("(case when temp.PERMISSION_ID is null then 'N' else 'Y' end) as functionRight,")
		      .append(" T3.FUNCTION_CODE AS parentId,  ")
		      .append(" T3.FUNCTION_NAME AS parentName ")
		      .append(" FROM "+schema+".ADM_FUNCTION_TYPE T1")
		      .append(" LEFT JOIN "+schema+".ADM_FUNCTION_TYPE T3 ON T3.FUNCTION_ID = T1.PARENT_FUNCTION_ID")
		      .append(" LEFT JOIN "+schema+".ADM_PERMISSION T2 on T2.FUNCTION_ID = T1.FUNCTION_ID")
		      .append(" LEFT JOIN (select distinct t4.PERMISSION_ID from "+schema+".ADM_ROLE_PERMISSION T4 ")
		      .append(" LEFT JOIN "+schema+".ADM_USER_ROLE t5 ON t5.ROLE_ID = t4.ROLE_ID ")
//		      .append(" where T4.TYPE =:type AND T4.STATUS =:status and t5.USER_ID =:userId) temp ")
		      .append(" where t5.USER_ID =:userId) temp ")
		      .append(" on t2.PERMISSION_ID = temp.PERMISSION_ID ")
		      .append(" where T1.STATUS =:status")
		      //.append(" and t5.USER_ID = :userId ")
		      .append(" and T1.FUNCTION_CATEGORY =:functionCategory")
		      .append(" ORDER BY sequence asc,resourceType,parentId");
			
			
			
			
			sql.setParameter(AdmFunctionTypeDTO.ATTRIBUTE.STATUS.getValue(), IAtomsConstants.STATUS_ACTIVE);
	//		sql.setParameter(RolePermissionDTO.ATTRIBUTE.TYPE.getValue(), IAtomsConstants.PARTICIPANT_TYPE_ROLE);
			sql.setParameter(AdmUserDTO.ATTRIBUTE.USER_ID.getValue(), iAtomsLogonUser.getId());
			sql.setParameter(RolePermissionDTO.ATTRIBUTE.FUNCTION_CATEGORY.getValue(), IAtomsConstants.MENU_TYPE_MENU);

			AliasBean aliasBean = new AliasBean(IAtomsAccessControl.class);
			aliasBean.addScalar(IAtomsAccessControl.FIELD_RESOURCE_TYPE, StringType.INSTANCE);
			aliasBean.addScalar(IAtomsAccessControl.FIELD_ID, StringType.INSTANCE);
			aliasBean.addScalar(IAtomsAccessControl.FIELD_NAME, StringType.INSTANCE);
			aliasBean.addScalar(IAtomsAccessControl.FIELD_RESOURCE_URL, StringType.INSTANCE);
			aliasBean.addScalar(IAtomsAccessControl.FIELD_SEQUENCE, IntegerType.INSTANCE);
			aliasBean.addScalar(IAtomsAccessControl.FIELD_PARENT_ID, StringType.INSTANCE);
			aliasBean.addScalar(IAtomsAccessControl.FIELD_ACCESS_RIGHT, StringType.INSTANCE);
			aliasBean.addScalar(IAtomsAccessControl.FIELD_PARENT_NAME, StringType.INSTANCE);
			aliasBean.addScalar(IAtomsAccessControl.FIELD_FUNCTION_RIGHT, StringType.INSTANCE);

			log.debug("sql---->"+sql.toString());
			accessControls = this.getDaoSupport().findByNativeSql(sql,aliasBean);
		} catch (Exception e) {
			log.debug(this.getClass().getName()+".getAvailableAccessControlList is error -->", e);
        	throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,e);
		}
		return accessControls;
	}	
}
