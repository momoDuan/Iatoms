package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.util.List;

import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmFunctionTypeDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.RolePermissionDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IFunctionTypeDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmFunctionType;
	/**
	 * 
	 * Purpose:功能設定实现DAO
	 * @author CarrieDuan
	 * @since  JDK 1.6
	 * @date   2015/6/25
	 * @MaintenancePersonnel CarrieDuan
	 */
public class FunctionTypeDAO  extends GenericBaseDAO<AdmFunctionType> implements IFunctionTypeDAO {
	/**
	 * 系统日志记录物件  
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(FunctionTypeDAO.class);	
	
	/**
	 * Constructor:無參構造
	 */
	public FunctionTypeDAO() {
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IFunctionTypeDAO#listFunctionTypeDTOsByRoleId(java.lang.String)
	 */
	@Override
	public List<AdmFunctionTypeDTO> listFunctionTypeDTOsByRoleId (String roleId) {
		List<AdmFunctionTypeDTO> functionTypeDTOs= null;
		try {
			LOGGER.debug("listFunctionTypeDTOsByRoleId()", "parameters:roleId=", roleId);
			AliasBean aliasBean = new AliasBean(AdmFunctionTypeDTO.class);
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("fun.FUNCTION_ID", AdmFunctionTypeDTO.ATTRIBUTE.FUNCTION_ID.getValue());
			sqlStatement.addSelectClause("fun.FUNCTION_NAME", AdmFunctionTypeDTO.ATTRIBUTE.FUNCTION_NAME.getValue());
			sqlStatement.addSelectClause("fun.PARENT_FUNCTION_ID", AdmFunctionTypeDTO.ATTRIBUTE.PARENT_FUNCTION_ID.getValue());
			sqlStatement.addSelectClause("parFun.FUNCTION_NAME", AdmFunctionTypeDTO.ATTRIBUTE.PARENT_FUNCTION_NAME.getValue());
			
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'ADD' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'ADD' THEN 0 ELSE NULL END)", 
											"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.ADD.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'DELETE' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'DELETE' THEN 0 ELSE NULL END)",
											"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.DELETE.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'EDIT' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'EDIT' THEN 0 ELSE NULL END)",
											"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.EDIT.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'QUERY' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'QUERY' THEN 0 ELSE NULL END)",
												AdmFunctionTypeDTO.ATTRIBUTE.QUERY.getValue());
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'EXPORT' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'EXPORT' THEN 0 ELSE NULL END)",
												AdmFunctionTypeDTO.ATTRIBUTE.EXPORT.getValue());
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'SAVE' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'SAVE' THEN 0 ELSE NULL END)", 
					"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.SAVE.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'DETAIL' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'DETAIL' THEN 0 ELSE NULL END)",
								"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.DETAIL.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'CARRY' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'CARRY' THEN 0 ELSE NULL END)",
								"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.CARRY.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'BORROW' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'BORROW' THEN 0 ELSE NULL END)",
									AdmFunctionTypeDTO.ATTRIBUTE.BORROW.getValue());
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'TOORIGINOWNER' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'CARRYBACK' THEN 0 ELSE NULL END)",
									AdmFunctionTypeDTO.ATTRIBUTE.CARRY_BACK.getValue());
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'ASSETIN' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'ASSETIN' THEN 0 ELSE NULL END)", 
					"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.ASSET_IN.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'SENDREPAIR' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'SENDREPAIR' THEN 0 ELSE NULL END)",
								"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.SEND_REPAIR.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'PENDINGDISABLE' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'PENDINGDISABLED' THEN 0 ELSE NULL END)",
								"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.PENDING_DISABLED.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'DISABLED' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'DISABLED' THEN 0 ELSE NULL END)",
									AdmFunctionTypeDTO.ATTRIBUTE.DISABLED.getValue());
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'BACK' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'BACK' THEN 0 ELSE NULL END)",
									AdmFunctionTypeDTO.ATTRIBUTE.BACK.getValue());
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'DESTROY' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'DESTROY' THEN 0 ELSE NULL END)", 
					"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.DESTROY.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'OTHEREDIT' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'OTHEREDIT' THEN 0 ELSE NULL END)",
								"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.OTHER_EDIT.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'PRINT' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'PRINT' THEN 0 ELSE NULL END)",
								"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.PRINT.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'INSTALL' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'INSTALL' THEN 0 ELSE NULL END)",
									AdmFunctionTypeDTO.ATTRIBUTE.INSTALL.getValue());
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'MERGE' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'MERGE' THEN 0 ELSE NULL END)",
					"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.MERGE.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'UPDATE' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'UPDATE' THEN 0 ELSE NULL END)", 
					"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.UPDATE.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'UNINSTALL' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'UNINSTALL' THEN 0 ELSE NULL END)",
								"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.UNINSTALL.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'CHECK' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'CHECK' THEN 0 ELSE NULL END)",
								"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.CHECK.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'PROJECT' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'PROJECT' THEN 0 ELSE NULL END)",
									AdmFunctionTypeDTO.ATTRIBUTE.PROJECT.getValue());
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'REPAIR' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'REPAIR' THEN 0 ELSE NULL END)",
									AdmFunctionTypeDTO.ATTRIBUTE.REPAIR.getValue());
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'CASEIMPORT' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'CASEIMPORT' THEN 0 ELSE NULL END)", 
					"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.CASE_IMPORT.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'ADDRECORD' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'ADDRECORD' THEN 0 ELSE NULL END)",
								"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.ADD_RECORD.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'DISPATCH' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'DISPATCH' THEN 0 ELSE NULL END)",
								"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.DISPATCH.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'SHOWDETAIL' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'SHOWDETAIL' THEN 0 ELSE NULL END)",
									AdmFunctionTypeDTO.ATTRIBUTE.SHOW_DETAIL.getValue());
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'RESPONSED' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'RESPONSED' THEN 0 ELSE NULL END)",
									AdmFunctionTypeDTO.ATTRIBUTE.RESPONSED.getValue());
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'COMPLETED' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'COMPLETED' THEN 0 ELSE NULL END)", 
					"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.COMPLETED.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'SIGN' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'SIGN' THEN 0 ELSE NULL END)",
								"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.SIGN.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'ONLINEEXCLUSION' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'ONLINEEXCLUSION' THEN 0 ELSE NULL END)",
								"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.ONLINE_EXCLUSION.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'DELAY' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'DELAY' THEN 0 ELSE NULL END)",
									AdmFunctionTypeDTO.ATTRIBUTE.DELAY.getValue());
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'RUSHREPAIR' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'RUSHREPAIR' THEN 0 ELSE NULL END)",
									AdmFunctionTypeDTO.ATTRIBUTE.RUSH_REPAIR.getValue());
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'CHANGECASETYPE' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'CHANGECASETYPE' THEN 0 ELSE NULL END)", 
					"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.CHANGE_CASE_TYPE.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'CLOSED' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'CLOSED' THEN 0 ELSE NULL END)",
								"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.CLOSED.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'VOIDCASE' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'VOIDCASE' THEN 0 ELSE NULL END)",
								"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.VOID_CASE.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'IMMEDIATELYCLOSING' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'IMMEDIATELYCLOSING' THEN 0 ELSE NULL END)",
									AdmFunctionTypeDTO.ATTRIBUTE.IMMEDIATELY_CLOSING.getValue());
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'ARRIVE' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'ARRIVE' THEN 0 ELSE NULL END)",
									AdmFunctionTypeDTO.ATTRIBUTE.ARRIVE.getValue());
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'SEND' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'SEND' THEN 0 ELSE NULL END)", 
					"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.SEND.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'LOCK' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'LOCK' THEN 0 ELSE NULL END)",
								"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.LOCK.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'COMPLETE' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'COMPLETE' THEN 0 ELSE NULL END)",
								"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.COMPLETE.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'REPAY' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'REPAY' THEN 0 ELSE NULL END)",
									AdmFunctionTypeDTO.ATTRIBUTE.REPAY.getValue());
			
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'CONFIRMSEND' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'CONFIRMSEND' THEN 0 ELSE NULL END)",
					"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.CONFIRMSEND.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'JDWMAINTENANCE' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'JDWMAINTENANCE' THEN 0 ELSE NULL END)",
						AdmFunctionTypeDTO.ATTRIBUTE.JDWMAINTENANCE.getValue());
			
			
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'AUTODISPATCH' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'AUTODISPATCH' THEN 0 ELSE NULL END)", 
					"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.AUTODISPATCH.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'TAIXINRENT' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'TAIXINRENT' THEN 0 ELSE NULL END)",
								"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.TAIXINRENT.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'UNBOUND' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'UNBOUND' THEN 0 ELSE NULL END)",
								"\"".concat(AdmFunctionTypeDTO.ATTRIBUTE.UNBOUND.getValue()).concat("\""));
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'REPAIR2' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'REPAIR2' THEN 0 ELSE NULL END)",
									AdmFunctionTypeDTO.ATTRIBUTE.REPAIR2.getValue());
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'OTHER' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'OTHER' THEN 0 ELSE NULL END)",
					AdmFunctionTypeDTO.ATTRIBUTE.OTHER.getValue());
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'CHANGECOMPLETEDATE' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'CHANGECOMPLETEDATE' THEN 0 ELSE NULL END)",
					AdmFunctionTypeDTO.ATTRIBUTE.CHANGECOMPLETEDATE.getValue());
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'CHANGECREATEDATE' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'CHANGECREATEDATE' THEN 0 ELSE NULL END)",
					AdmFunctionTypeDTO.ATTRIBUTE.CHANGECREATEDATE.getValue());
			
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'LEASEPRELOAD' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'LEASEPRELOAD' THEN 0 ELSE NULL END)",
					AdmFunctionTypeDTO.ATTRIBUTE.LEASEPRELOAD.getValue());
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'LEASESIGN' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'LEASESIGN' THEN 0 ELSE NULL END)",
					AdmFunctionTypeDTO.ATTRIBUTE.LEASESIGN.getValue());
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'DISTRIBUTION' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'DISTRIBUTION' THEN 0 ELSE NULL END)",
					AdmFunctionTypeDTO.ATTRIBUTE.DISTRIBUTION.getValue());
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'PAYMENT' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'PAYMENT' THEN 0 ELSE NULL END)",
					AdmFunctionTypeDTO.ATTRIBUTE.PAYMENT.getValue());
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'CONFIRMAUTHORIZES' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'CONFIRMAUTHORIZES' THEN 0 ELSE NULL END)",
					AdmFunctionTypeDTO.ATTRIBUTE.CONFIRMAUTHORIZES.getValue());
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'CANCELCONFIRMAUTHORIZES' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'CANCELCONFIRMAUTHORIZES' THEN 0 ELSE NULL END)",
					AdmFunctionTypeDTO.ATTRIBUTE.CANCELCONFIRMAUTHORIZES.getValue());
			sqlStatement.addSelectClause("SUM(case when per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'COORDINATEDIMPORT' AND RP.ROLE_ID IS NOT NULL THEN 1 WHEN per.PERMISSION_ID IS NOT NULL AND per.ACCESS_RIGHT = 'COORDINATEDIMPORT' THEN 0 ELSE NULL END)",
					AdmFunctionTypeDTO.ATTRIBUTE.COORDINATEDIMPORT.getValue());
			
			
			
			
			sqlStatement.addSelectClause("MAX(RP.UPDATED_DATE)", AdmFunctionTypeDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			sqlStatement.addSelectClause("MAX(RP.UPDATED_BY_NAME)", AdmFunctionTypeDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".ADM_FUNCTION_TYPE fun");
			fromBuffer.append(" left JOIN ").append(schema).append(".ADM_FUNCTION_TYPE parFun ON fun.PARENT_FUNCTION_ID = parFun.FUNCTION_ID and parFun.FUNCTION_CATEGORY='FOLDER'");
			fromBuffer.append(" left JOIN ").append(schema).append(".ADM_PERMISSION per on fun.FUNCTION_ID = per.FUNCTION_ID");
			fromBuffer.append(" left JOIN ").append(schema).append(".ADM_ROLE_PERMISSION RP ON PER.PERMISSION_ID = RP.PERMISSION_ID AND RP.ROLE_ID = :roleId");
			sqlStatement.addFromExpression(fromBuffer.toString());
			StringBuffer whereBuffer = new StringBuffer();
			whereBuffer.append("fun.FUNCTION_CATEGORY = 'MENU'");
			
			whereBuffer.append(" AND fun.STATUS = :status");
			whereBuffer.append(" AND parFun.STATUS = :status");
			
			whereBuffer.append(" GROUP BY fun.FUNCTION_ID, fun.FUNCTION_NAME,fun.FUNCTION_CODE,fun.PARENT_FUNCTION_ID,parFun.FUNCTION_NAME");
			whereBuffer.append(" having fun.FUNCTION_ID  in (select DISTINCT function_id FROM ").append(schema).append(".ADM_PERMISSION  left outer join ");
			whereBuffer.append(schema).append(".ADM_ROLE_PERMISSION ON ADM_PERMISSION.PERMISSION_ID = ADM_ROLE_PERMISSION.PERMISSION_ID");
			whereBuffer.append(" where dbo.ADM_ROLE_PERMISSION.ROLE_ID = :roleId )");
			/*sqlStatement.addWhereClause("fun.FUNCTION_CATEGORY = 'MENU'" +
					" GROUP BY fun.FUNCTION_ID, fun.FUNCTION_NAME,fun.FUNCTION_CODE,fun.PARENT_FUNCTION_ID,parFun.FUNCTION_NAME" + "" +
					" having fun.FUNCTION_ID  in (select DISTINCT function_id FROM " + schema + ".ADM_PERMISSION  left outer join " + schema + ". ADM_ROLE_PERMISSION ON ADM_PERMISSION.PERMISSION_ID = ADM_ROLE_PERMISSION.PERMISSION_ID" +
					" where dbo.ADM_ROLE_PERMISSION.ROLE_ID = :roleId )");*/
			sqlStatement.addWhereClause(whereBuffer.toString());
			StringBuffer orderBuffer = new StringBuffer();
			orderBuffer.append(AdmFunctionTypeDTO.ATTRIBUTE.PARENT_FUNCTION_ID.getValue());
			orderBuffer.append(IAtomsConstants.MARK_SEPARATOR);
			orderBuffer.append(AdmFunctionTypeDTO.ATTRIBUTE.FUNCTION_ID.getValue());
			sqlStatement.setOrderByExpression(orderBuffer.toString());
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.FUNCTION_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.FUNCTION_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.PARENT_FUNCTION_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.PARENT_FUNCTION_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.UPDATED_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.ADD.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.EDIT.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.DELETE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.EXPORT.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.QUERY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.SAVE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.DETAIL.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.CARRY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.BORROW.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.CARRY_BACK.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.ASSET_IN.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.SEND_REPAIR.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.PENDING_DISABLED.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.DISABLED.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.BACK.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.DESTROY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.OTHER_EDIT.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.PRINT.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.INSTALL.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.MERGE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.UPDATE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.UNINSTALL.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.CHECK.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.PROJECT.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.REPAIR.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.CASE_IMPORT.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.ADD_RECORD.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.DISPATCH.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.SHOW_DETAIL.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.RESPONSED.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.COMPLETED.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.SIGN.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.ONLINE_EXCLUSION.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.DELAY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.RUSH_REPAIR.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.CHANGE_CASE_TYPE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.CLOSED.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.VOID_CASE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.IMMEDIATELY_CLOSING.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.ARRIVE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.SEND.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.LOCK.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.COMPLETE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.REPAY.getValue(), StringType.INSTANCE);
			
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.AUTODISPATCH.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.TAIXINRENT.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.UNBOUND.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.REPAIR2.getValue(), StringType.INSTANCE);
			
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.CONFIRMSEND.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.JDWMAINTENANCE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.OTHER.getValue(), StringType.INSTANCE);
			
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.CHANGECOMPLETEDATE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.CHANGECREATEDATE.getValue(), StringType.INSTANCE);
			
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.LEASEPRELOAD.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.LEASESIGN.getValue(), StringType.INSTANCE);
			
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.DISTRIBUTION.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.PAYMENT.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.CONFIRMAUTHORIZES.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.CANCELCONFIRMAUTHORIZES.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(AdmFunctionTypeDTO.ATTRIBUTE.COORDINATEDIMPORT.getValue(), StringType.INSTANCE);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
//			sqlQueryBean.setParameter(RolePermissionDTO.ATTRIBUTE.STATUS.getValue(), IAtomsConstants.STATUS_ACTIVE);
			sqlQueryBean.setParameter(AdmFunctionTypeDTO.ATTRIBUTE.STATUS.getValue(), IAtomsConstants.STATUS_ACTIVE);
			sqlQueryBean.setParameter("roleId", roleId);
			LOGGER.debug(".listFunctionTypeDTOsByRoleId()", "sql:", sqlQueryBean.toString());
			functionTypeDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return functionTypeDTOs;
		} catch (Exception e) {
			LOGGER.error(".listFunctionTypeDTOsByRoleId() Exception-->", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	/**
	 * Purpose:判斷列表是否包含key
	 * @author evanliu
	 * @param list:功能列表
	 * @param key:功能列表編號
	 * @return FunctionTypeDTO:功能dto
	 */
	public AdmFunctionTypeDTO contains (List<AdmFunctionTypeDTO> list, String key) {
		for (AdmFunctionTypeDTO functionTypeDTO : list) {
			if (functionTypeDTO.getFunctionId().equals(key)) {
				return functionTypeDTO;
			}
		}
		return null;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IFunctionTypeDAO#listFunctionByFunctionId(java.lang.String)
	 */
	@Override
	public List<Parameter> listFunctionByFunctionId(String functionId) throws DataAccessException{
		String schema = this.getMySchema();
		List<Parameter> roleFunctions = null;
		try {
			LOGGER.debug("listFunctionByFunctionId()", "parameters:functionId=", functionId);
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("FUNCTION_ID", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("FUNCTION_NAME", Parameter.FIELD_NAME);
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".ADM_FUNCTION_TYPE");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(functionId)) {
				sqlStatement.addWhereClause("FUNCTION_CATEGORY = :menu", IAtomsConstants.MENU_TYPE_MENU);
				sqlStatement.addWhereClause("PARENT_FUNCTION_ID = :functionId", functionId);
			} else {
				sqlStatement.addWhereClause("FUNCTION_CATEGORY = :folder", IAtomsConstants.MENU_TYPE_FOLDER);
			}
			sqlStatement.addWhereClause("STATUS = :status", IAtomsConstants.STATUS_ACTIVE);
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug(".listFunctionByFunctionId()", "sql:", sqlQueryBean.toString());
			roleFunctions = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(".listFunctionByFunctionId() Exception-->", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return roleFunctions;
	}
}
