package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
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

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmRoleDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmRole;

/**
 * Purpose: 角色DAO interface
 * @author CarrieDuan
 * @since  JDK 1.6
 * @date   2016年3月30日
 * @MaintenancePersonnel CarrieDuan
 */
public class AdmRoleDAO extends GenericBaseDAO<AdmRole> implements IAdmRoleDAO {

	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(AdmRoleDAO.class);
	
	
	/**
	 * Constructor:無參構造
	 */
	public AdmRoleDAO() {
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmRoleDAO#listby()
	 */
	@Override
	public List<Parameter> listby() throws DataAccessException {
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("ROLE_ID", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("CONCAT( CONVERT( VARCHAR( 20 ) ,ROLE_CODE ,111 ) ,'-' ,CONVERT( VARCHAR( 50 ) ,ROLE_NAME ,111 ) )", Parameter.FIELD_NAME);
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".ADM_ROLE");
			sqlStatement.addFromExpression(fromBuffer.toString());
			sqlStatement.setOrderByExpression("ROLE_CODE");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			List<Parameter> roleList = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			LOGGER.debug("listby()", "sql:", sqlQueryBean.toString());
			return roleList;
		} catch (Exception e) {
			LOGGER.error(":listby() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.atoms.services.dao.IAdmRoleDAO#listByUserId(java.lang.String)
	 */
	@Override
	public List<Parameter> listByUserId(String userId) throws DataAccessException {
		try {
			LOGGER.debug(".listByUserId()", "parameters:userId=", userId);
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("r.role_id", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("r.role_name", Parameter.FIELD_NAME);
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".adm_role r");
			if (StringUtils.hasText(userId)) {
				fromBuffer.append(IAtomsConstants.MARK_SEPARATOR).append(schema).append(".adm_user_role ur");
				sqlStatement.addFromExpression(fromBuffer.toString());
				sqlStatement.addWhereClause("r.role_id = ur.role_id");
				sqlStatement.addWhereClause("ur.user_id = :userId", userId);
			} else {
				sqlStatement.addFromExpression(fromBuffer.toString());
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			LOGGER.debug("listByUserId()", "sql:", sqlQueryBean.toString());			
			List<Parameter> roleList = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return roleList;
		} catch (Exception e) {
			LOGGER.error("listByUserId() is error.", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmRoleDAO#listRoleCode()
	 */
	@Override
	public List<Parameter> listRoleCode() throws DataAccessException {
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("ROLE_CODE", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("CONCAT( CONVERT( VARCHAR( 20 ) ,ROLE_CODE ,111 ) ,'-' ,CONVERT( VARCHAR( 50 ) ,ROLE_NAME ,111 ) )", Parameter.FIELD_NAME);
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".ADM_ROLE ORDER BY ROLE_CODE");
			sqlStatement.addFromExpression(fromBuffer.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			List<Parameter> roleCodeList = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			LOGGER.debug("listRoleCode()", "sql:", sqlQueryBean.toString());
			return roleCodeList;
		} catch (Exception e) {
			LOGGER.error("listRoleCode() is error.", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmRoleDAO#count(java.lang.String, java.lang.String)
	 */
	@Override
	public Integer count(String queryRoleCode, String queryRoleName) throws DataAccessException {
		try {
			LOGGER.debug("count()", "parameters:queryRoleCode=", queryRoleCode);
			LOGGER.debug("count()", "parameters:queryRoleName=", queryRoleName);
			String schma = this.getMySchema();
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			// 查詢記錄數
			sqlQueryBean.append("select count(1) as counter from ");
			sqlQueryBean.append(schma).append(".ADM_ROLE r where 1 = 1 ");			
			// 角色代碼不為空，拼接查詢條件
			if (StringUtils.hasText(queryRoleCode)) {
				sqlQueryBean.append("and r.ROLE_CODE = :roleCode");
				sqlQueryBean.setParameter(AdmRoleDTO.ATTRIBUTE.ROLE_CODE.getValue(), queryRoleCode);
			}
			// 角色名稱不為空，拼接模糊查詢條件
			if (StringUtils.hasText(queryRoleName)) {
				sqlQueryBean.append("and r.ROLE_NAME like :roleName");
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append(queryRoleName).append(IAtomsConstants.MARK_PERCENT);
				sqlQueryBean.setParameter(AdmRoleDTO.ATTRIBUTE.ROLE_NAME.getValue(), stringBuffer.toString());
			}
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				return result.get(0);
			}
			LOGGER.debug(".count()", "sql:", sqlQueryBean.toString());
			return Integer.valueOf(0);
		} catch (Exception e) {
			LOGGER.error("count() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmRoleDAO#listBy(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Boolean)
	 */
	@Override
	public List<AdmRoleDTO> listBy(String queryRoleCode, String queryRoleName,
			Integer pageSize, Integer pageIndex, String sort, String orderby,
			Boolean isPage) throws DataAccessException {
		List<AdmRoleDTO> result = null;
		try {
			LOGGER.debug("listBy()", "parameters:queryRoleCode=", queryRoleCode);
			LOGGER.debug("listBy()", "parameters:queryRoleName=", queryRoleName);
			LOGGER.debug("listBy()", "parameters:pageSize=", pageSize.longValue());
			LOGGER.debug("listBy()", "parameters:pageIndex=", pageIndex.longValue());
			LOGGER.debug("listBy()", "parameters:sort=", sort);
			LOGGER.debug("listBy()", "parameters:orderby=", orderby);
			LOGGER.debug("listBy()", "parameters:isPage=", isPage.toString());
			
			String schma = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("r.ROLE_ID", AdmRoleDTO.ATTRIBUTE.ROLE_ID.getValue());
			sqlStatement.addSelectClause("r.ROLE_CODE", AdmRoleDTO.ATTRIBUTE.ROLE_CODE.getValue());
			sqlStatement.addSelectClause("r.ROLE_NAME", AdmRoleDTO.ATTRIBUTE.ROLE_NAME.getValue());
			sqlStatement.addSelectClause("r.ROLE_DESC", AdmRoleDTO.ATTRIBUTE.ROLE_DESC.getValue());
			sqlStatement.addSelectClause("r.ATTRIBUTE", AdmRoleDTO.ATTRIBUTE.ATTRIBUTE.getValue());
			sqlStatement.addSelectClause("attr.ITEM_NAME", AdmRoleDTO.ATTRIBUTE.ATTRIBUTE_NAME.getValue());
			sqlStatement.addSelectClause("r.WORK_FLOW_ROLE", AdmRoleDTO.ATTRIBUTE.WORK_FLOW_ROLE.getValue());
			sqlStatement.addSelectClause("wfr.ITEM_NAME", AdmRoleDTO.ATTRIBUTE.WORK_FLOW_ROLE_NAME.getValue());
			sqlStatement.addSelectClause("r.CREATED_BY_ID", AdmRoleDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			sqlStatement.addSelectClause("r.CREATED_BY_NAME", AdmRoleDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("r.CREATED_DATE", AdmRoleDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sqlStatement.addSelectClause("r.UPDATED_BY_ID", AdmRoleDTO.ATTRIBUTE.UPDATED_BY_ID.getValue());
			sqlStatement.addSelectClause("r.UPDATED_BY_NAME", AdmRoleDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("r.UPDATED_DATE", AdmRoleDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			StringBuffer bufferFrom = new StringBuffer();
			bufferFrom.append(schma).append(" .ADM_ROLE as r ");
			bufferFrom.append(" LEFT join ").append(schma).append(".BASE_PARAMETER_ITEM_DEF attr on r.ATTRIBUTE = attr.ITEM_VALUE and attr.BPTD_CODE = :attributeName");
			bufferFrom.append(" LEFT join ").append(schma).append(".BASE_PARAMETER_ITEM_DEF wfr on r.WORK_FLOW_ROLE = wfr.ITEM_VALUE and wfr.BPTD_CODE = :workFlowRoleName");
			sqlStatement.addFromExpression(bufferFrom.toString());
			// 角色代碼不為空，拼接查詢條件
			if (StringUtils.hasText(queryRoleCode)) {
				sqlStatement.addWhereClause("r.ROLE_CODE = :roleCode", queryRoleCode);
			}
			// 角色名稱不為空，拼接模糊查詢條件
			if (StringUtils.hasText(queryRoleName)) {
				sqlStatement.addWhereClause("r.ROLE_NAME like :roleName");
			}
			sqlStatement.setOrderByExpression(sort + IAtomsConstants.MARK_SPACE + orderby);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter(AdmRoleDTO.ATTRIBUTE.ATTRIBUTE_NAME.getValue(), IATOMS_PARAM_TYPE.ROLE_ATTRIBUTE.getCode());
			sqlQueryBean.setParameter(AdmRoleDTO.ATTRIBUTE.WORK_FLOW_ROLE_NAME.getValue(), IATOMS_PARAM_TYPE.WORK_FLOW_ROLE.getCode());
			if (StringUtils.hasText(queryRoleName)) {
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append(queryRoleName).append(IAtomsConstants.MARK_PERCENT);
				sqlQueryBean.setParameter(AdmRoleDTO.ATTRIBUTE.ROLE_NAME.getValue(), stringBuffer.toString());
			}
			//sqlQueryBean.append("order by ").append(sort).append(" ").append(orderby);
			//查詢數據
			/*sqlQueryBean.append("select * from (");
			sqlQueryBean.append("select ");
			sqlQueryBean.append("r.ROLE_ID as ").append(AdmRoleDTO.ATTRIBUTE.ROLE_ID.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("r.ROLE_CODE as ").append(AdmRoleDTO.ATTRIBUTE.ROLE_CODE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("r.ROLE_NAME as ").append(AdmRoleDTO.ATTRIBUTE.ROLE_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("r.ROLE_DESC as ").append(AdmRoleDTO.ATTRIBUTE.ROLE_DESC.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("r.ATTRIBUTE as ").append(AdmRoleDTO.ATTRIBUTE.ATTRIBUTE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("ATTR.ITEM_NAME as ").append(AdmRoleDTO.ATTRIBUTE.ATTRIBUTE_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("r.WORK_FLOW_ROLE as ").append(AdmRoleDTO.ATTRIBUTE.WORK_FLOW_ROLE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("WFR.ITEM_NAME as ").append(AdmRoleDTO.ATTRIBUTE.WORK_FLOW_ROLE_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("r.CREATED_BY_ID as ").append(AdmRoleDTO.ATTRIBUTE.CREATED_BY_ID.getValue()).append(IAtomsConstants.MARK_SEPARATOR);			
			sqlQueryBean.append("r.CREATED_BY_NAME as ").append(AdmRoleDTO.ATTRIBUTE.CREATED_BY_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("r.CREATED_DATE as ").append(AdmRoleDTO.ATTRIBUTE.CREATED_DATE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("r.UPDATED_BY_ID as ").append(AdmRoleDTO.ATTRIBUTE.UPDATED_BY_ID.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("r.UPDATED_BY_NAME as ").append(AdmRoleDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("r.UPDATED_DATE as ").append(AdmRoleDTO.ATTRIBUTE.UPDATED_DATE.getValue()).append(" ");
			sqlQueryBean.append(" from ").append(schma).append(".ADM_ROLE as r ");
			sqlQueryBean.append(" LEFT join ").append(schma).append(".BASE_PARAMETER_ITEM_DEF ATTR on r.ATTRIBUTE = ATTR.ITEM_VALUE and ATTR.BPTD_CODE =:attributeName");
			sqlQueryBean.append(" LEFT join ").append(schma).append(".BASE_PARAMETER_ITEM_DEF WFR on r.WORK_FLOW_ROLE = WFR.ITEM_VALUE and WFR.BPTD_CODE =:workFlowRoleName");*/
			// 方便拼接查詢條件
			/*sqlQueryBean.append(" where 1 = 1 ");
			sqlQueryBean.setParameter(AdmRoleDTO.ATTRIBUTE.ATTRIBUTE_NAME.getValue(), IATOMS_PARAM_TYPE.ROLE_ATTRIBUTE.getCode());
			sqlQueryBean.setParameter(AdmRoleDTO.ATTRIBUTE.WORK_FLOW_ROLE_NAME.getValue(), IATOMS_PARAM_TYPE.WORK_FLOW_ROLE.getCode());
			// 角色代碼不為空，拼接查詢條件
			if (StringUtils.hasText(queryRoleCode)) {
				sqlQueryBean.append("and r.ROLE_CODE = :roleCode");
				sqlQueryBean.setParameter(AdmRoleDTO.ATTRIBUTE.ROLE_CODE.getValue(), queryRoleCode);
			}
			// 角色名稱不為空，拼接模糊查詢條件
			if (StringUtils.hasText(queryRoleName)) {
				sqlQueryBean.append("and r.ROLE_NAME like :roleName");
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append(queryRoleName).append(IAtomsConstants.MARK_PERCENT);
				sqlQueryBean.setParameter(AdmRoleDTO.ATTRIBUTE.ROLE_NAME.getValue(), stringBuffer.toString());
			}
			sqlQueryBean.append(")a");*/
			// 頁面上的排序字段條件
			/*sqlQueryBean.append("order by ").append(sort).append(" ").append(orderby);
			
			aliasBean.addScalar(AdmRoleDTO.ATTRIBUTE.ROLE_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmRoleDTO.ATTRIBUTE.ROLE_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmRoleDTO.ATTRIBUTE.ROLE_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmRoleDTO.ATTRIBUTE.ROLE_DESC.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmRoleDTO.ATTRIBUTE.ATTRIBUTE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmRoleDTO.ATTRIBUTE.ATTRIBUTE_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmRoleDTO.ATTRIBUTE.WORK_FLOW_ROLE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmRoleDTO.ATTRIBUTE.WORK_FLOW_ROLE_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmRoleDTO.ATTRIBUTE.CREATED_BY_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmRoleDTO.ATTRIBUTE.CREATED_BY_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmRoleDTO.ATTRIBUTE.CREATED_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(AdmRoleDTO.ATTRIBUTE.UPDATED_BY_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmRoleDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmRoleDTO.ATTRIBUTE.UPDATED_DATE.getValue(), TimestampType.INSTANCE);*/
			AliasBean aliasBean = sqlStatement.createAliasBean(AdmRoleDTO.class);
			// 分頁
			if (isPage.booleanValue()) {
				sqlQueryBean.setPageSize(pageSize.intValue());
				sqlQueryBean.setStartPage((pageIndex.intValue()) - 1);
			}
			LOGGER.debug("listBy()", "sql:", sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listBy() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return result;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmRoleDAO#checkUseRole(java.lang.String)
	 */
	public Boolean checkUseRole(String roleId) throws DataAccessException {
		String schema = this.getMySchema();
		List<Integer> roleList = null;
		try {
			LOGGER.debug("checkUseRole()", "parameters:roleId=", roleId);
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".ADM_USER_ROLE userRole");
			fromBuffer.append(" left join ").append(schema).append(".ADM_USER userMsg on userMsg.USER_ID = userRole.USER_ID");
			sqlStatement.addFromExpression(fromBuffer.toString());
			sqlStatement.addWhereClause("userRole.ROLE_ID = :roleId", roleId);
			sqlStatement.addWhereClause("isnull(userMsg.DELETED,'N') = :deleted", IAtomsConstants.NO);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug(".checkUseRole()", "sql:", sqlQueryBean.toString());
			roleList = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(roleList)) {
				if (roleList.get(0).intValue() == 0) {
					return Boolean.FALSE;
				}
				return Boolean.TRUE;
			}
		} catch (Exception e) {
			LOGGER.error("checkUseRole() Exception-->", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return Boolean.FALSE;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmRoleDAO#checkRepeat(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Boolean checkRepeat(String roleCode, String roleName, String roleId) throws DataAccessException {
		try {
			LOGGER.debug("checkRepeat()", "parameters:roleCode=", roleCode);
			LOGGER.debug("checkRepeat()", "parameters:roleName=", roleName);
			LOGGER.debug("checkRepeat()", "parameters:roleId=", roleId);
			String schma = this.getMySchema();
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			// 查詢角色代碼重複的角色編號
			sqlQueryBean.append("select count(1) from ");
			if (StringUtils.hasText(roleCode)) {
				sqlQueryBean.append(schma).append(".ADM_ROLE where ROLE_CODE = :roleCode");
				sqlQueryBean.setParameter(AdmRoleDTO.ATTRIBUTE.ROLE_CODE.getValue(), roleCode);
			} else if (StringUtils.hasText(roleName)) {
				sqlQueryBean.append(schma).append(".ADM_ROLE where ROLE_NAME = :roleName");
				sqlQueryBean.setParameter(AdmRoleDTO.ATTRIBUTE.ROLE_NAME.getValue(), roleName);
			}
			if (StringUtils.hasText(roleId)) {
				sqlQueryBean.append(" and ROLE_ID <> :roleId");
				sqlQueryBean.setParameter(AdmRoleDTO.ATTRIBUTE.ROLE_ID.getValue(), roleId);
			}
			LOGGER.debug(".checkRepeat()", "sql:", sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				if (result.get(0).intValue() > 0) {
					return Boolean.TRUE;
				}
			}
		} catch (Exception e) {
			LOGGER.error(":checkRoleCode() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return Boolean.FALSE;
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmRoleDAO#getRoleById(java.lang.String)
	 */
	@Override
	public AdmRoleDTO getRoleById(String roleId) throws DataAccessException {
		AdmRoleDTO admRoleDTO = null;
		try {
			LOGGER.debug("getRoleById", "parameters:roleId=", roleId);
			String schma = this.getMySchema();
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			// 查詢名稱重複的角色編號
			sqlQueryBean.append("select ");
			sqlQueryBean.append("r.ROLE_CODE as ").append(AdmRoleDTO.ATTRIBUTE.ROLE_CODE.getValue()).append(",");
			sqlQueryBean.append("r.ROLE_NAME as ").append(AdmRoleDTO.ATTRIBUTE.ROLE_NAME.getValue()).append(" ");
			sqlQueryBean.append(" from ").append(schma).append(".ADM_ROLE as r ");
			if (StringUtils.hasText(roleId)) {
				sqlQueryBean.append(" where ROLE_ID = :roleId");
				sqlQueryBean.setParameter(AdmRoleDTO.ATTRIBUTE.ROLE_ID.getValue(), roleId);
			}
			AliasBean aliasBean = new AliasBean(AdmRoleDTO.class);
			aliasBean.addScalar(AdmRoleDTO.ATTRIBUTE.ROLE_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmRoleDTO.ATTRIBUTE.ROLE_NAME.getValue(), StringType.INSTANCE);
			List<AdmRoleDTO> result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(result)) {
				admRoleDTO = result.get(0);
			}
			LOGGER.debug("getRoleById()", "sql:", sqlQueryBean.toString());
		} catch (Exception e) {
			LOGGER.error(":getRoleById() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return admRoleDTO;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmRoleDAO#listWorkFlowByAttribute(java.lang.String)
	 */
	public List<Parameter> listWorkFlowByAttribute(String attribute) throws DataAccessException {
		String schema = this.getMySchema();
		List<Parameter> roleFunctions = null;
		try {
			LOGGER.debug("listWorkFlowByAttribute()", "paramters:attributeName=", attribute);
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("bp1.ITEM_NAME", Parameter.FIELD_NAME);
			sqlStatement.addSelectClause("bp1.ITEM_VALUE", Parameter.FIELD_VALUE);
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".BASE_PARAMETER_ITEM_DEF bp1 left outer join ");
			fromBuffer.append(schema).append(".BASE_PARAMETER_ITEM_DEF bp2 on bp2.BPID_ID = bp1.PARENT_BPID_ID");
			sqlStatement.addFromExpression(fromBuffer.toString());
			sqlStatement.addWhereClause("bp2.BPTD_CODE='ROLE_ATTRIBUTE' and bp2.ITEM_VALUE = :attributeValue", attribute);
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug(".listWorkFlowByAttribute()", "sql:", sqlQueryBean.toString());
			roleFunctions = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listWorkFlowByAttribute() Exception-->", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return roleFunctions;
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmRoleDAO#getUserByDepartmentAndRole(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Parameter> getUserByDepartmentAndRole(String deptCode, String roleCode, boolean flag,
			boolean isDeptAgent) throws DataAccessException {
		String schema = this.getMySchema();
		List<Parameter> users = null;
		try {
			LOGGER.debug("getUserByDepartmentAndRole()", "paramters:deptCode=", deptCode);
			LOGGER.debug("getUserByDepartmentAndRole()", "paramters:roleCode=", roleCode);
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("distinct u.USER_ID", Parameter.FIELD_VALUE);
			if(flag) {
				sqlStatement.addSelectClause("u.EMAIL", Parameter.FIELD_NAME);
			} else {
				sqlStatement.addSelectClause("u.CNAME", Parameter.FIELD_NAME);
			}
			sqlStatement.addFromExpression(schema + ".ADM_USER u ");
			StringBuffer whereBuffer = new StringBuffer();
			whereBuffer.append("exists(select 1 from ").append(schema).append(".ADM_USER_ROLE r where r.USER_ID = u.USER_ID ");
			// 客服(CUSTOMER_SERVICE) = 廠商角色 + CyberAgent + 角色为CUSTOMER_SERVICE
			if (IAtomsConstants.CASE_ROLE.CUSTOMER_SERVICE.getCode().equals(roleCode)) {
				whereBuffer.append(" and (exists(select 1 from ").append(schema).append(".ADM_ROLE customerService where customerService.ATTRIBUTE =:vendor");
			//	whereBuffer.append(" and customerService.WORK_FLOW_ROLE =:cyberAgent and customerService.ROLE_CODE=:customerService and customerService.ROLE_ID=r.ROLE_Id) ");
				whereBuffer.append(" and customerService.WORK_FLOW_ROLE =:cyberAgent and (customerService.ROLE_CODE=:customerService or customerService.ROLE_CODE=:vendorService) and customerService.ROLE_ID=r.ROLE_Id) ");
				//	Task #3578 客戶廠商客服
				whereBuffer.append(" or exists(select 1 from ").append(schema).append(".ADM_ROLE cusVendorService where cusVendorService.ATTRIBUTE =:cusVendor");
				whereBuffer.append(" and cusVendorService.WORK_FLOW_ROLE =:cusVendorAgent and cusVendorService.ROLE_CODE=:cusVendorService and cusVendorService.ROLE_ID=r.ROLE_Id)) ");
			}
			//Task #3568 只查詢cyber客服
			if (IAtomsConstants.CASE_ROLE.CYBER_SERVICE.getCode().equals(roleCode)) {
				whereBuffer.append(" and exists(select 1 from ").append(schema).append(".ADM_ROLE customerService where customerService.ATTRIBUTE =:vendor");
				whereBuffer.append(" and customerService.WORK_FLOW_ROLE =:cyberAgent and customerService.ROLE_CODE=:customerService and customerService.ROLE_ID=r.ROLE_Id) ");
			}
			// TMS = 廠商角色 + CyberAgent + 角色为TMS
			if (IAtomsConstants.CASE_ROLE.TMS.getCode().equals(roleCode)) {
				whereBuffer.append(" and exists(select 1 from ").append(schema).append(".ADM_ROLE tms where tms.ATTRIBUTE =:vendor and tms.WORK_FLOW_ROLE =:cyberAgent ");
				whereBuffer.append(" and tms.ROLE_CODE=:tms and tms.ROLE_ID=r.ROLE_ID ) ");
			}
			// QA = 廠商角色 + CyberAgent + 角色为QA
			if (IAtomsConstants.CASE_ROLE.QA.getCode().equals(roleCode)) {
				whereBuffer.append(" and exists(select 1 from ").append(schema).append(".ADM_ROLE qa where qa.ATTRIBUTE =:vendor and qa.WORK_FLOW_ROLE =:cyberAgent ");
				whereBuffer.append(" and qa.ROLE_CODE=:qa and qa.ROLE_ID = r.ROLE_ID ) ");
			}
			// 部門工程師 = 廠商角色 + 工程師 或者 廠商角色 + 部門Agent		//Task #3583 
			if (cafe.core.util.StringUtils.hasText(deptCode)) {
				whereBuffer.append(" and exists(select 1 from ").append(schema).append(".BIM_DEPARTMENT b where b.DEPT_CODE =u.DEPT_CODE");
				whereBuffer.append(" and exists(select 1 from ").append(schema).append(".ADM_ROLE engineer where (engineer.ATTRIBUTE =:vendor or engineer.ATTRIBUTE =:cusVendor) and (engineer.WORK_FLOW_ROLE =:deptEngineer or engineer.WORK_FLOW_ROLE =:cusDeptEngineer) ");
				whereBuffer.append(" and engineer.ROLE_ID=r.ROLE_ID)) ");
				sqlStatement.addWhereClause("u.DEPT_CODE =:deptCode", deptCode);
			}
			// 廠商倉管 VENDOR_WAREHOUSE	//Task #3583 
			if (IAtomsConstants.FIELD_ROLE_CODE_VENDOR_WAREHOUSE.equals(roleCode)) {
				whereBuffer.append(" and exists (select 1 from  ");
				whereBuffer.append(schema);
				whereBuffer.append(".ADM_ROLE admRole where (admRole.ROLE_CODE = :vendorWarehouse or (admRole.ROLE_CODE = :cusRoleCode and admRole.ATTRIBUTE=:cusVendor)) and admRole.ROLE_ID = r.ROLE_ID)");
			}
			whereBuffer.append(") "); 
			sqlStatement.addWhereClause(whereBuffer.toString());
			sqlStatement.addWhereClause("u.STATUS =:status", IAtomsConstants.ACCOUNT_STATUS_NORMAL);
			sqlStatement.addWhereClause("isNUll(u.DELETED, 'N') =:deleted", IAtomsConstants.PARAM_NO);
			SqlQueryBean sql = sqlStatement.createSqlQueryBean();
			if (IAtomsConstants.CASE_ROLE.CUSTOMER_SERVICE.getCode().equals(roleCode)) {
				sql.setParameter("cyberAgent", IAtomsConstants.WORK_FLOW_ROLE_CYBER_AGENT);
				sql.setParameter("vendor", IAtomsConstants.VECTOR_ROLE_ATTRIBUTE);
				sql.setParameter("cusVendor", IAtomsConstants.CUSTOMER_VECTOR_ROLE_ATTRIBUTE);
				sql.setParameter("cusVendorAgent", IAtomsConstants.WORK_FLOW_ROLE_CUS_VENDOR_AGENT);
				sql.setParameter("cusVendorService", IAtomsConstants.CASE_ROLE.CUS_VENDOR_SERVICE.getCode());
				
				sql.setParameter("vendorService", IAtomsConstants.CASE_ROLE.VENDOR_SERVICE.getCode());
				sql.setParameter("customerService", IAtomsConstants.CASE_ROLE.CUSTOMER_SERVICE.getCode());
			}
			if ("CYBER_SERVICE".equals(roleCode)) {
				sql.setParameter("cyberAgent", IAtomsConstants.WORK_FLOW_ROLE_CYBER_AGENT);
				sql.setParameter("vendor", IAtomsConstants.VECTOR_ROLE_ATTRIBUTE);
				sql.setParameter("customerService", IAtomsConstants.CASE_ROLE.CUSTOMER_SERVICE.getCode());
			}
			if (IAtomsConstants.CASE_ROLE.TMS.getCode().equals(roleCode)) {
				sql.setParameter("vendor", IAtomsConstants.VECTOR_ROLE_ATTRIBUTE);
				sql.setParameter("cyberAgent", IAtomsConstants.WORK_FLOW_ROLE_CYBER_AGENT);
				sql.setParameter("tms", IAtomsConstants.CASE_ROLE.TMS.getCode());
			}
			if (IAtomsConstants.CASE_ROLE.QA.getCode().equals(roleCode)) {
				sql.setParameter("vendor", IAtomsConstants.VECTOR_ROLE_ATTRIBUTE);
				sql.setParameter("cyberAgent", IAtomsConstants.WORK_FLOW_ROLE_CYBER_AGENT);
				sql.setParameter("qa", IAtomsConstants.CASE_ROLE.QA.getCode());
			}
			// 廠商倉管 VENDOR_WAREHOUSE	//Task #3583 客戶廠商倉管 也要出現在通知人員中
			if (IAtomsConstants.FIELD_ROLE_CODE_VENDOR_WAREHOUSE.equals(roleCode)) {
				sql.setParameter("vendorWarehouse", IAtomsConstants.FIELD_ROLE_CODE_VENDOR_WAREHOUSE);
				sql.setParameter("cusRoleCode", IAtomsConstants.FIELD_ROLE_CODE_CUS_VENDOR_WAREHOUSE) ;
				sql.setParameter("cusVendor", IAtomsConstants.CUSTOMER_VECTOR_ROLE_ATTRIBUTE) ;
			}
			if (StringUtils.hasText(deptCode)) {
				sql.setParameter("vendor", IAtomsConstants.VECTOR_ROLE_ATTRIBUTE);
				sql.setParameter("cusVendor", IAtomsConstants.CUSTOMER_VECTOR_ROLE_ATTRIBUTE);
				//部門agent
				if(isDeptAgent) {
					sql.setParameter("deptEngineer", IAtomsConstants.WORK_FLOW_ROLE_DEPT_AGENT);
					sql.setParameter("cusDeptEngineer", IAtomsConstants.WORK_FLOW_ROLE_CUS_DEPT_AGENT);
				} else {
					//工程師
					sql.setParameter("deptEngineer", IAtomsConstants.WORK_FLOW_ROLE_ENGINEER);
					sql.setParameter("cusDeptEngineer", IAtomsConstants.WORK_FLOW_ROLE_CUS_ENGINEER);
				}
			}
			
			AliasBean alias = new AliasBean(Parameter.class);
			alias.addScalar(Parameter.FIELD_NAME, StringType.INSTANCE);
			alias.addScalar(Parameter.FIELD_VALUE, StringType.INSTANCE);
			LOGGER.debug(".getUserByDepartmentAndRole()", "sql:", sql.toString());
			users = this.getDaoSupport().findByNativeSql(sql, alias);
			
			//users = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("getUserByDepartmentAndRole() Exception-->", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return users;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IAdmRoleDAO#getRoleGroup(java.lang.String)
	 */
	public List<AdmRoleDTO> getRoleGroup(String id, boolean isQueryUser, String ignoreRoleId, List<String> userIdList) throws DataAccessException{
		List<AdmRoleDTO> admRoleDTOs = null;
		try {
			LOGGER.debug("getRoleGroup", "parameters:id="+ id);
			LOGGER.debug("getRoleGroup", "parameters:isQueryUser="+ isQueryUser);
			LOGGER.debug("getRoleGroup", "parameters:ignoreRoleId="+ ignoreRoleId);
			LOGGER.debug("getRoleGroup", "parameters:userIdList="+ userIdList);
			// 得到schma
			String schma = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			StringBuilder builder = new StringBuilder();
			// 查詢客戶標志位爲true 查使用者信息
			if(isQueryUser){
				sqlStatement.addSelectClause("admUser.USER_ID", AdmRoleDTO.ATTRIBUTE.USER_ID.getValue());
				sqlStatement.addSelectClause("admUser.ACCOUNT", AdmRoleDTO.ATTRIBUTE.ACCOUNT.getValue());
			}
			//廠商角色 + cyberAgent + CUSTOMER_SERVICE == 客服
			builder.delete(0, builder.length());
			builder.append("case when (admRole.ATTRIBUTE = :vendorParam and admRole.WORK_FLOW_ROLE = :cyberAgentParam and admRole.ROLE_CODE = :customerServiceParam) then 'true' else 'false' end");
			sqlStatement.addSelectClause(builder.toString(), AdmRoleDTO.ATTRIBUTE.IS_CUSTOMER_SERVICE.getValue());
			
			// CR #2951 廠商客服
			builder.delete(0, builder.length());
			builder.append("case when (admRole.ATTRIBUTE = :vendorParam and admRole.WORK_FLOW_ROLE = :cyberAgentParam and admRole.ROLE_CODE = :vendorServiceParam) then 'true' else 'false' end");
			sqlStatement.addSelectClause(builder.toString(), AdmRoleDTO.ATTRIBUTE.IS_VENDOR_SERVICE.getValue());
			//Task #3578 客戶廠商客服
			//客戶廠商角色 + 客戶廠商Agent + 角色代碼為CUS_VENDOR_SERVICE == CUS_VENDOR_SERVICE
			builder.delete(0, builder.length());
			builder.append("case when (admRole.ATTRIBUTE = :customerVendorParam and admRole.WORK_FLOW_ROLE = :cusVendorAgentParam and admRole.ROLE_CODE = :cusVendorServiceParam) then 'true' else 'false' end");
			sqlStatement.addSelectClause(builder.toString(), AdmRoleDTO.ATTRIBUTE.IS_CUS_VENDOR_SERVICE.getValue());

			//客服角色 + bankAgent == 客戶窗口
			builder.delete(0, builder.length());
			builder.append("case when (admRole.ATTRIBUTE = :customerParam and admRole.WORK_FLOW_ROLE = :bankAgentParam) then 'true' else 'false' end");
			sqlStatement.addSelectClause(builder.toString(), AdmRoleDTO.ATTRIBUTE.IS_CUSTOMER.getValue());
			//廠商角色 + CyberAgent + 角色代碼為QA == QA窗口
			builder.delete(0, builder.length());
			builder.append("case when (admRole.ATTRIBUTE = :vendorParam and admRole.WORK_FLOW_ROLE = :cyberAgentParam and admRole.ROLE_CODE = :QAParam) then 'true' else 'false' end");
			sqlStatement.addSelectClause(builder.toString(), AdmRoleDTO.ATTRIBUTE.IS_QA.getValue());
			//廠商角色 + CyberAgent + 角色代碼為TMS == TMS窗口
			builder.delete(0, builder.length());
			builder.append("case when (admRole.ATTRIBUTE = :vendorParam and admRole.WORK_FLOW_ROLE = :cyberAgentParam and admRole.ROLE_CODE = :TMSParam) then 'true' else 'false' end");
			sqlStatement.addSelectClause(builder.toString(), AdmRoleDTO.ATTRIBUTE.IS_TMS.getValue());
			//廠商角色 + 部門Agent == 部門AGENT窗口
			builder.delete(0, builder.length());
			//Task #3583 角色屬性-新增 客戶廠商
			builder.append("case when ((admRole.ATTRIBUTE = :vendorParam or admRole.ATTRIBUTE = :customerVendorParam) and (admRole.WORK_FLOW_ROLE = :deptAgentParam or admRole.WORK_FLOW_ROLE = :cusDeptAgentParam)) then 'true' else 'false' end");
			sqlStatement.addSelectClause(builder.toString(), AdmRoleDTO.ATTRIBUTE.IS_AGENT.getValue());
			//廠商角色 + 部門Agent == 廠商AGENT窗口
			builder.delete(0, builder.length());
			//Task #3583 角色屬性-新增 客戶廠商
			builder.append("case when ((admRole.ATTRIBUTE = :vendorParam or admRole.ATTRIBUTE = :customerVendorParam) and (admRole.WORK_FLOW_ROLE = :vendorAgentParam or admRole.WORK_FLOW_ROLE = :vendorCusAgentParam)) then 'true' ");
			builder.append(" when (admRole.ATTRIBUTE = :vendorParam and admRole.WORK_FLOW_ROLE = :cyberAgentParam and admRole.ROLE_CODE = :cyberAgentParam) then 'true' else 'false' end");
			sqlStatement.addSelectClause(builder.toString(), AdmRoleDTO.ATTRIBUTE.IS_VENDOR_AGENT.getValue());
			//廠商角色 + 工程師 == 工程師窗口	//Task #3583 
			builder.delete(0, builder.length());
			builder.append("case when ((admRole.ATTRIBUTE = :vendorParam or admRole.ATTRIBUTE = :customerVendorParam) and (admRole.WORK_FLOW_ROLE = :engineerParam or admRole.WORK_FLOW_ROLE = :cusEngineerParam)) then 'true' else 'false' end");
			sqlStatement.addSelectClause(builder.toString(), AdmRoleDTO.ATTRIBUTE.IS_ENGINEER.getValue());
			// 廠商角色 + CyberAgent + CYBER_AGENT == CyberAgent
			/*builder.delete(0, builder.length());
			builder.append("case when (admRole.ATTRIBUTE = :vendorParam and admRole.WORK_FLOW_ROLE = :cyberAgentParam and admRole.ROLE_CODE = :cyberAgentParam) then 'true' else 'false' end");
			sqlStatement.addSelectClause(builder.toString(), AdmRoleDTO.ATTRIBUTE.IS_VENDOR_AGENT.getValue());*/
			if(isQueryUser){
				builder.delete(0, builder.length());
				builder.append(schma).append(".ADM_USER admUser left join ");
				builder.append(schma).append(".ADM_USER_ROLE userRole on admUser.USER_ID = userRole.USER_ID left join ");
				builder.append(schma).append(".ADM_ROLE admRole on userRole.ROLE_ID = admRole.ROLE_ID ");
				sqlStatement.addFromExpression(builder.toString());
				// 表示按照使用者編號查詢該使用者的角色group信息
				if(StringUtils.hasText(id)){
					sqlStatement.addWhereClause("admUser.USER_ID =:userId", id);
				}
				// 查詢使用者信息時過濾傳入的角色編號
				if(StringUtils.hasText(ignoreRoleId)){
					sqlStatement.addWhereClause("admRole.ROLE_ID <> :ignoreRoleId", ignoreRoleId);
				}
				// 查詢使用者信息時按照傳入使用者編號集合查找角色group信息
				if(!CollectionUtils.isEmpty(userIdList)){
					sqlStatement.addWhereClause("admUser.USER_ID in (:userIdList)");
				}
				// 按照使用者帳號進行排序
				sqlStatement.setOrderByExpression("admUser.ACCOUNT asc");
			} else {
				// 查詢角色信息
				sqlStatement.addFromExpression(schma + ".ADM_ROLE admRole");
				// 按照所傳入角色編號查詢角色group信息
				if(StringUtils.hasText(id)){
					sqlStatement.addWhereClause("admRole.ROLE_ID in (:roleId)");
				}
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			// 查詢包含使用者信息
			if(isQueryUser){
				// 傳入的使用者編號
				if(!CollectionUtils.isEmpty(userIdList)){
					sqlQueryBean.setParameter("userIdList", userIdList);
				}
			// 只查角色代碼信息
			} else {
				// 傳入的角色代碼
				if(StringUtils.hasText(id)){
					sqlQueryBean.setParameter("roleId", StringUtils.toList(id, IAtomsConstants.MARK_SEPARATOR));
				}
			}

			sqlQueryBean.setParameter("vendorParam", IAtomsConstants.VECTOR_ROLE_ATTRIBUTE);
			sqlQueryBean.setParameter("customerParam", IAtomsConstants.CUSTOMER_ROLE_ATTRIBUTE);
			sqlQueryBean.setParameter("customerVendorParam", IAtomsConstants.CUSTOMER_VECTOR_ROLE_ATTRIBUTE);
			sqlQueryBean.setParameter("cyberAgentParam", IAtomsConstants.WORK_FLOW_ROLE_CYBER_AGENT);
			sqlQueryBean.setParameter("bankAgentParam", IAtomsConstants.WORK_FLOW_ROLE_BANK_AGENT);
			sqlQueryBean.setParameter("deptAgentParam", IAtomsConstants.WORK_FLOW_ROLE_DEPT_AGENT);
			sqlQueryBean.setParameter("vendorAgentParam", IAtomsConstants.WORK_FLOW_ROLE_VENDOR_AGENT);
			sqlQueryBean.setParameter("engineerParam", IAtomsConstants.WORK_FLOW_ROLE_ENGINEER);
			sqlQueryBean.setParameter("customerServiceParam", IAtomsConstants.CASE_ROLE.CUSTOMER_SERVICE.getCode());
			sqlQueryBean.setParameter("vendorServiceParam", IAtomsConstants.CASE_ROLE.VENDOR_SERVICE.getCode());
			sqlQueryBean.setParameter("cusVendorServiceParam", IAtomsConstants.CASE_ROLE.CUS_VENDOR_SERVICE.getCode());
			sqlQueryBean.setParameter("cusVendorAgentParam", IAtomsConstants.WORK_FLOW_ROLE_CUS_VENDOR_AGENT);
			sqlQueryBean.setParameter("cusEngineerParam", IAtomsConstants.WORK_FLOW_ROLE_CUS_ENGINEER);
			sqlQueryBean.setParameter("vendorCusAgentParam", IAtomsConstants.WORK_FLOW_ROLE_VENDOR_CUS_AGENT);
			sqlQueryBean.setParameter("cusDeptAgentParam", IAtomsConstants.WORK_FLOW_ROLE_CUS_DEPT_AGENT);

			sqlQueryBean.setParameter("QAParam", IAtomsConstants.CASE_ROLE.QA.getCode());
			sqlQueryBean.setParameter("TMSParam", IAtomsConstants.CASE_ROLE.TMS.getCode());
			AliasBean aliasBean = sqlStatement.createAliasBean(AdmRoleDTO.class);
			admRoleDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			LOGGER.debug("getRoleGroup()", "sql:", sqlQueryBean.toString());
		} catch (Exception e) {
			LOGGER.error("getRoleGroup() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return admRoleDTOs;
	}
	
}
