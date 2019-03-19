package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Parameter;
import cafe.core.config.GenericConfigManager;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;

import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BimDepartmentDTO;
import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.services.dao.IDepartmentDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimDepartment;

/**
 * Purpose: 部門Dao
 * @author barryzhang
 * @since  JDK 1.6
 * @date   2016/6/3
 * @MaintenancePersonnel barryzhang
 */
public class DepartmentDAO extends GenericBaseDAO<BimDepartment> implements IDepartmentDAO {
	/**
	 * 系统日志记录物件  
	 */
	private static CafeLog logger = CafeLogFactory.getLog(GenericConfigManager.DAO, DepartmentDAO.class);

	/**
	 * Constructor:無參構造器
	 */
	public DepartmentDAO() {
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDepartmentDAO#listBy(java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BimDepartmentDTO> listBy(String deptCode, String companyId, String deptName, 
			Integer pageSize, Integer pageIndex, String sort, String orderby) throws DataAccessException {
		logger.debug("DepartmentDAO.listBy.deptCode:'" + deptCode + "'");
		logger.debug("DepartmentDAO.listBy.deptName:'" + deptName + "'");
		logger.debug("DepartmentDAO.listBy.pageSize:'" + pageSize + "'");
		logger.debug("DepartmentDAO.listBy.pageIndex:'" + pageIndex + "'");
		logger.debug("DepartmentDAO.listBy.sort:'" + sort + "'");
		logger.debug("DepartmentDAO.listBy.orderby:'" + orderby + "'");
		logger.debug("DepartmentDAO.listBy.departmentName:'" + deptName + "'");
		logger.debug("DepartmentDAO.listBy.companyId:'" + companyId + "'");
		SqlStatement sql = new SqlStatement();
		List<BimDepartmentDTO> departmentManageList = null;
		//得到schema
		String schema = this.getMySchema();
		try {
			sql.addSelectClause("dept.CONTACT_FAX", BimDepartmentDTO.ATTRIBUTE.CONTACT_FAX.getValue());
			sql.addSelectClause("dept.DEPT_NAME", BimDepartmentDTO.ATTRIBUTE.DEPT_NAME.getValue());
			sql.addSelectClause("dept.DEPT_CODE", BimDepartmentDTO.ATTRIBUTE.DEPT_CODE.getValue());
			sql.addSelectClause("vendor.SHORT_NAME", BimDepartmentDTO.ATTRIBUTE.COMPANY_NAME.getValue());
			sql.addSelectClause("dept.REMARK", BimDepartmentDTO.ATTRIBUTE.REMARK.getValue());
			sql.addSelectClause("dept.CONTACT", BimDepartmentDTO.ATTRIBUTE.CONTACT.getValue());
			sql.addSelectClause("dept.CONTACT_TEL", BimDepartmentDTO.ATTRIBUTE.CONTACT_TEL.getValue());
			sql.addSelectClause("dept.COMPANY_ID", BimDepartmentDTO.ATTRIBUTE.COMPANY_ID.getValue());
			sql.addSelectClause("base.ITEM_NAME", BimDepartmentDTO.ATTRIBUTE.LOCATION.getValue());
			sql.addSelectClause("isnull(dept.ADDRESS,'')", BimDepartmentDTO.ATTRIBUTE.ADDRESS.getValue());
			sql.addSelectClause("dept.CONTACT_EMAIL", BimDepartmentDTO.ATTRIBUTE.CONTACT_EMAIL.getValue());
			sql.setFromExpression(" " + schema + ".BIM_DEPARTMENT dept " +
			"left join "+ schema + ".BIM_COMPANY vendor on dept.COMPANY_Id=vendor.COMPANY_Id " +
			"left join " + schema +".BASE_PARAMETER_ITEM_DEF base on base.BPTD_CODE = :location AND base.ITEM_VALUE = dept.LOCATION");
			//sql.addWhereClause(" 1 = 1 ");
			if (StringUtils.hasText(companyId)) {
				sql.addWhereClause(" dept.company_Id =:companyId", companyId);
			}
			if (StringUtils.hasText(deptName)) {
				sql.addWhereClause("dept.DEPT_NAME like :queryDeptName", deptName + IAtomsConstants.MARK_PERCENT);
			}			
			//設置排序表達式
			if (StringUtils.hasText(sort) && StringUtils.hasText(orderby)){
				sql.setOrderByExpression(orderby + " " + sort);
			} else {
				sql.setOrderByExpression("vendor.SHORT_NAME, dept.DEPT_NAME");
			}
			//判斷刪除標誌
			sql.addWhereClause("ISNULL(dept.DELETED, 'N') =:deleted", IAtomsConstants.NO);
			sql.setPageSize(pageSize);
			sql.setStartPage(pageIndex - 1);
			//創建SqlQueryBean
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			sqlQueryBean.setParameter("location", IATOMS_PARAM_TYPE.LOCATION.getCode());
			//記錄sql語句
			logger.debug(this.getClass().getName() + ".listBy()", "sql:" + sqlQueryBean.toString());
			AliasBean aliasBean = sql.createAliasBean(BimDepartmentDTO.class);
			departmentManageList = (List<BimDepartmentDTO>) super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ":Failed-- listBy()---" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_BIM_DEPARTMENT)}, e);
		}
		return departmentManageList;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDepartmentDAO#count(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Integer count(String queryDepartmentName, String queryCompany) throws DataAccessException {
		logger.debug("DepartmentDAO.count.queryDeptName:'" + queryDepartmentName + "'");
		logger.debug("DepartmentDAO.count.queryCompany:'" + queryCompany + "'");
		try {
			String schema = this.getMySchema();
			SqlStatement sql = new SqlStatement();		
			sql.addSelectClause( "count(*)");
			sql.setFromExpression(schema + ".BIM_DEPARTMENT dept");
			if (StringUtils.hasText(queryCompany)) {
				sql.addWhereClause("dept.company_Id =:companyId", queryCompany);
			}
			if (StringUtils.hasText(queryDepartmentName)) {
				sql.addWhereClause("dept.DEPT_NAME like :queryDeptName", queryDepartmentName + IAtomsConstants.MARK_PERCENT);
			}			
			//判斷刪除標誌
			sql.addWhereClause("ISNULL(dept.DELETED, 'N') !=:deleted", IAtomsConstants.YES);
			//記錄語句
			logger.debug(this.getClass().getName() + ".count()", "sql:" + sql.toString());
			//創建SqlQueryBean
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				return result.get(0).intValue();
			}
			return 0;
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ".count() is error " + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_BIM_DEPARTMENT)}, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDepartmentDAO#check(java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean check(String companyId, String deptCode, String deptName) throws DataAccessException {
		logger.debug("DepartmentDAO.check.deptName:'" + deptName + "'");
		logger.debug("DepartmentDAO.check.deptCode:'" + deptCode + "'");
		logger.debug("DepartmentDAO.check.companyId:'" + companyId + "'");
		//檢核同一公司下是否存在重複的部門名稱
		Boolean isRepate = false;
		List<Integer> result = null;
		//得到schema
		String schema = this.getMySchema();
		SqlStatement sqlStatement = new SqlStatement();
		try {
			sqlStatement.addSelectClause("count(1)");
			sqlStatement.setFromExpression(schema + ".BIM_DEPARTMENT dept");
			if (StringUtils.hasText(companyId)) {
				sqlStatement.addWhereClause("dept.company_id =:companyId", companyId);
			}			
			if (StringUtils.hasText(deptName)) {
				sqlStatement.addWhereClause("dept.DEPT_NAME =:deptName", deptName);
			}
			if (StringUtils.hasText(deptCode)) {
				sqlStatement.addWhereClause("dept.DEPT_CODE !=:deptCode", deptCode);
			}
			//判斷刪除標誌
			sqlStatement.addWhereClause("ISNULL(dept.DELETED, 'N') !=:deleted", IAtomsConstants.YES);
			//記錄sql語句
			logger.debug(this.getClass().getName() + ".getDepartmentId()", "sql:" + sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				if (result.get(0).intValue() > 0) {
					isRepate = true;
				}
			}
			return isRepate;
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ":Failed-- getDepartmentId()---" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_BIM_DEPARTMENT)}, e);
		}
	}	
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDepartmentDAO#getDeptList(java.lang.String)
	 */
	@Override
	public List<Parameter> getDeptList(String companyId, Boolean ignoreDeleted) throws DataAccessException {
		logger.debug(this.getClass().getName() + ".getDeptList()", "parameters:companyId = " + companyId);
		logger.debug(this.getClass().getName() + ".getDeptList()", "parameters:ignoreDeleted = " + ignoreDeleted);
		String schema = this.getMySchema();
		//創建SqlStatement
		SqlStatement sql = new SqlStatement();
		List<Parameter> deptList = null;
		try {
			sql.addSelectClause("dept.DEPT_CODE", Parameter.FIELD_VALUE);
			sql.addSelectClause("dept.DEPT_NAME", Parameter.FIELD_NAME);
			sql.setFromExpression( schema + ". BIM_DEPARTMENT dept");
			if (StringUtils.hasText(companyId)) {
				sql.addWhereClause(" dept.COMPANY_ID in (:companyId)");
			}
			if(ignoreDeleted){
				//判斷刪除標誌
				sql.addWhereClause("ISNULL(dept.DELETED, 'N') !=:deleted", IAtomsConstants.YES);
			}
			// 記錄sql語句
			logger.debug(this.getClass().getName() + ".getDeptList()", "sql:" + sql.toString());
			//創建SqlQueryBean
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			if (StringUtils.hasText(companyId)) {
				sqlQueryBean.setParameter("companyId",  StringUtils.toList(companyId, IAtomsConstants.MARK_SEPARATOR));
			}
			AliasBean aliasBean = new AliasBean(Parameter.class);			
			deptList = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+":Failed-- getDeptList()---" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_BIM_DEPARTMENT)}, e);
		}
		return deptList;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDepartmentDAO#getDeptByCompany(java.lang.String, java.lang.Boolean)
	 */
	@Override
	public List<Parameter> getDeptByCompany(String companyId, Boolean isSplitInfo) throws DataAccessException {
		logger.debug(this.getClass().getName() + ".getDeptByCompany()", "parameters:companyId = " + companyId);
		logger.debug(this.getClass().getName() + ".getDeptByCompany()", "parameters:isSplitInfo = " + isSplitInfo);
		String schema = this.getMySchema();
		//創建SqlStatement
		SqlStatement sql = new SqlStatement();
		List<Parameter> deptList = null;
		try {
			StringBuffer buffer = new StringBuffer();
			sql.addSelectClause("dept.DEPT_CODE", Parameter.FIELD_VALUE);
			if(isSplitInfo){
				sql.addSelectClause("dept.DEPT_NAME + '-' + company.SHORT_NAME", Parameter.FIELD_NAME);
				buffer.append(schema).append(".BIM_DEPARTMENT dept ");
				buffer.append(" left join ").append(schema).append(".BIM_COMPANY company on company.COMPANY_ID = dept.COMPANY_ID ");
			} else {
				sql.addSelectClause("dept.DEPT_NAME", Parameter.FIELD_NAME);
				buffer.append(schema).append(".BIM_DEPARTMENT dept ");
			}
			sql.addFromExpression(buffer.toString());
			if (StringUtils.hasText(companyId)) {
				sql.addWhereClause(" dept.COMPANY_ID =:companyId", companyId);
			}
			//判斷刪除標誌
			sql.addWhereClause("ISNULL(dept.DELETED, 'N') !=:deleted", IAtomsConstants.YES);
			// 記錄sql語句
			logger.debug(this.getClass().getName() + ".getDeptByCompany()", "sql:" + sql.toString());
			//創建SqlQueryBean
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			AliasBean aliasBean = sql.createAliasBean(Parameter.class);			
			deptList = super.getDaoSupport().findByNativeSql(sqlQueryBean,aliasBean);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+":Failed-- getDeptByCompany()---" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_BIM_DEPARTMENT)}, e);
		}
		return deptList;
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDepartmentDAO#getDepartmentList(java.lang.String)
	 */
	@Override
	public List<Parameter> getDepartmentList(String companyId, String deptCode) throws DataAccessException {
		logger.debug("getDepartmentList()", "parameters:companyId = " + companyId);
		logger.debug("getDepartmentList()", "parameters:deptCode = " + deptCode);
		long startQueryDeptTime = System.currentTimeMillis();
		String schema = this.getMySchema();
		//創建SqlStatement
		SqlStatement sql = new SqlStatement();
		List<Parameter> deptList = null;
		try {
			StringBuffer buffer = new StringBuffer();
			sql.addSelectClause("d.DEPT_CODE", Parameter.FIELD_VALUE);
			sql.addSelectClause("c.SHORT_NAME+'-'+d.DEPT_NAME ", Parameter.FIELD_NAME);
			buffer.append(schema).append(".BIM_DEPARTMENT d ");
			buffer.append(" left join ").append(schema).append(".BIM_COMPANY c on d.COMPANY_ID = c.COMPANY_ID ");
			sql.addFromExpression(buffer.toString());
			//判斷刪除標誌
			sql.addWhereClause("ISNULL(d.DELETED, 'N') !=:deleted", IAtomsConstants.YES);
			buffer.delete(0, buffer.length());
			buffer.append(" exists(select 1 from ").append(".BIM_COMPANY_TYPE comType ");
			buffer.append(" where comType.COMPANY_ID = c.COMPANY_ID and comType.COMPANY_TYPE = 'MAINTENANCE_VENDOR') ");
			sql.addWhereClause(buffer.toString());
			if(StringUtils.hasText(deptCode)){
				sql.addWhereClause(" d.DEPT_CODE =:deptCode");
			}
			if(StringUtils.hasText(companyId)){
				sql.addWhereClause(" d.COMPANY_ID in( :queryCompanyId )");
			}			
			sql.setOrderByExpression("c.SHORT_NAME,d.DEPT_NAME asc");
			// 記錄sql語句
			logger.debug("getDepartmentList()", "sql:" + sql.toString());
			//創建SqlQueryBean
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			sqlQueryBean.setParameter("queryCompanyId", StringUtils.toList(companyId, IAtomsConstants.MARK_SEPARATOR));
			sqlQueryBean.setParameter("deptCode", deptCode);
			AliasBean aliasBean = sql.createAliasBean(Parameter.class);			
			deptList = super.getDaoSupport().findByNativeSql(sqlQueryBean,aliasBean);
		} catch (Exception e) {
			logger.error(":Failed-- getDepartmentList()---" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_BIM_DEPARTMENT)}, e);
		}
		long endQueryDeptTime = System.currentTimeMillis();
		logger.debug("calculate time --> load", "DAO getDepartmentList:" + (endQueryDeptTime - startQueryDeptTime));
		return deptList;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDepartmentDAO#getDepartmentIdByName(java.lang.String)
	 */
	public String getDepartmentIdByName(String departmentName, String companyId) throws DataAccessException {
		logger.debug("getDepartmentIdByName()", "parameters:departmentName = " + departmentName);
		logger.debug("getDepartmentIdByName()", "parameters:companyId = " + companyId);
		String schema = this.getMySchema();
		//創建SqlStatement
		SqlStatement sqlStatement = new SqlStatement();
		List<BimDepartmentDTO> deptList = null;
		try {
			sqlStatement.addSelectClause("depart.DEPT_CODE", BimDepartmentDTO.ATTRIBUTE.DEPT_CODE.getValue());
			sqlStatement.addFromExpression(schema + ".BIM_DEPARTMENT depart");
			if (StringUtils.hasText(departmentName)) {
				sqlStatement.addWhereClause("depart.DEPT_NAME = :departmentName", departmentName);
			}
			if (StringUtils.hasLength(companyId)) {
				sqlStatement.addWhereClause("depart.COMPANY_ID = :companyId", companyId);
			}
			sqlStatement.addWhereClause("depart.DELETED = :deleted", IAtomsConstants.NO);
			// 記錄sql語句
			logger.debug("getDepartmentList()", "sql:" + sqlStatement.toString());
			//創建SqlQueryBean
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(BimDepartmentDTO.class);			
			deptList = super.getDaoSupport().findByNativeSql(sqlQueryBean,aliasBean);
			if (!CollectionUtils.isEmpty(deptList)) {
				return deptList.get(0).getDeptCode();
			}
		} catch (Exception e) {
			logger.error(":Failed-- getDepartmentIdByName()---" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_BIM_DEPARTMENT)}, e);
		}
		return null;
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDepartmentDAO#getDepts(java.lang.String)
	 */
	@Override
	public List<Parameter> getDepts(String companyId) throws DataAccessException {
		logger.debug(this.getClass().getName() + ".getDepts()", "parameters:companyId = " + companyId);
		String schema = this.getMySchema();
		//創建SqlStatement
		SqlStatement sql = new SqlStatement();
		List<Parameter> deptList = null;
		try {
			sql.addSelectClause("dept.DEPT_CODE", Parameter.FIELD_VALUE);
			sql.addSelectClause("dept.DEPT_NAME", Parameter.FIELD_NAME);
			sql.setFromExpression( schema + ". BIM_DEPARTMENT dept");
			if (StringUtils.hasText(companyId)) {
				sql.addWhereClause(" dept.COMPANY_ID =:companyId", companyId);
			}
			//判斷刪除標誌
			//sql.addWhereClause("ISNULL(dept.DELETED, 'N') !=:deleted", IAtomsConstants.YES);
			// 記錄sql語句
			logger.debug(this.getClass().getName() + ".getDepts()", "sql:" + sql.toString());
			//創建SqlQueryBean
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			AliasBean aliasBean = new AliasBean(Parameter.class);			
			deptList = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+":Failed-- getDepts()---" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_BIM_DEPARTMENT)}, e);
		}
		return deptList;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDepartmentDAO#getDeptList()
	 */
	@Override
	public List<BimDepartmentDTO> getDeptList() throws DataAccessException {
		SqlStatement sql = new SqlStatement();
		List<BimDepartmentDTO> departmentManageList = null;
		//得到schema
		String schema = this.getMySchema();
		try {
			sql.addSelectClause("dept.DEPT_CODE", BimDepartmentDTO.ATTRIBUTE.DEPT_CODE.getValue());
			sql.addSelectClause("dept.COMPANY_ID", BimDepartmentDTO.ATTRIBUTE.COMPANY_ID.getValue());
			sql.addSelectClause("dept.DEPT_NAME", BimDepartmentDTO.ATTRIBUTE.DEPT_NAME.getValue());
			sql.addSelectClause("dept.CONTACT", BimDepartmentDTO.ATTRIBUTE.CONTACT.getValue());
			sql.addSelectClause("dept.CONTACT_TEL", BimDepartmentDTO.ATTRIBUTE.CONTACT_TEL.getValue());
			sql.addSelectClause("dept.CONTACT_FAX", BimDepartmentDTO.ATTRIBUTE.CONTACT_FAX.getValue());
			sql.addSelectClause("dept.CONTACT_EMAIL", BimDepartmentDTO.ATTRIBUTE.CONTACT_EMAIL.getValue());
			sql.addSelectClause("dept.LOCATION", BimDepartmentDTO.ATTRIBUTE.LOCATION.getValue());
			sql.addSelectClause("dept.ADDRESS", BimDepartmentDTO.ATTRIBUTE.ADDRESS.getValue());
			sql.addSelectClause("dept.REMARK", BimDepartmentDTO.ATTRIBUTE.REMARK.getValue());
			sql.setFromExpression(schema + ".BIM_DEPARTMENT dept");
			//創建SqlQueryBean
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			//記錄sql語句
			logger.debug(this.getClass().getName() + ".getDeptList()", "sql:" + sqlQueryBean.toString());
			AliasBean aliasBean = sql.createAliasBean(BimDepartmentDTO.class);
			departmentManageList = (List<BimDepartmentDTO>) super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ":Failed-- getDeptList()---" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_BIM_DEPARTMENT)}, e);
		}
		return departmentManageList;
	}
}
