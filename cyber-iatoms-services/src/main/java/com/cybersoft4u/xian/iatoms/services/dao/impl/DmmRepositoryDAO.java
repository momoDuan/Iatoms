package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.type.DateType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.util.CollectionUtils;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.Parameter;
import cafe.core.bean.identity.LogonUser;
import cafe.core.config.GenericConfigManager;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.GenericBaseDAO;
import cafe.core.dao.criterion.SqlStatement;
import cafe.core.dao.support.AliasBean;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryHistoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetManageFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.CrossTabReportDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmRepository;
/**
 * Purpose: 庫存主檔DAO
 * @author amandawang
 * @since  JDK 1.6
 * @date   2016/8/4
 * @MaintenancePersonnel amandawang
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DmmRepositoryDAO extends GenericBaseDAO<DmmRepository> implements IDmmRepositoryDAO {

	/**
	 * 系统日志记录物件  
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.DAO, DmmRepositoryDAO.class);

	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#listBy(com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetManageFormDTO)
	 */
	@Override
	public List<DmmRepositoryDTO> listBy(AssetManageFormDTO assetManageFormDTO) throws DataAccessException {
		LOGGER.debug("DmmRepositoryDAO.listBy.userId:'" + assetManageFormDTO.getUserId() + "'");
		LOGGER.debug("DmmRepositoryDAO.listBy.companyId:'" + assetManageFormDTO.getCompanyId() + "'");
		LOGGER.debug("DmmRepositoryDAO.listBy.queryUser:'" + assetManageFormDTO.getQueryUser() + "'");
		LOGGER.debug("DmmRepositoryDAO.listBy.action:'" + assetManageFormDTO.getQueryAction() + "'");
		LOGGER.debug("DmmRepositoryDAO.listBy.queryAssetCategory:'" + assetManageFormDTO.getQueryAssetCategory() + "'");
		LOGGER.debug("DmmRepositoryDAO.listBy.warehouseId:'" + assetManageFormDTO.getStorage() + "'");
		LOGGER.debug("DmmRepositoryDAO.listBy.assetId:'" + assetManageFormDTO.getQueryAssetId() + "'");
		LOGGER.debug("DmmRepositoryDAO.listBy.merRegisteredName:'" + assetManageFormDTO.getQueryMerName() + "'");
		LOGGER.debug("DmmRepositoryDAO.listBy.merAnnouncedName:'" + assetManageFormDTO.getQueryHeaderName() + "'");
		LOGGER.debug("DmmRepositoryDAO.listBy.mid:'" + assetManageFormDTO.getQueryMerchantCode() + "'");
		LOGGER.debug("DmmRepositoryDAO.listBy.merInstallAddress:'" + assetManageFormDTO.getQueryMerInstallAddress() + "'");
		LOGGER.debug("DmmRepositoryDAO.listBy.area:'" + assetManageFormDTO.getQueryArea() + "'");
		LOGGER.debug("DmmRepositoryDAO.listBy.contractId:'" + assetManageFormDTO.getQueryContractId() + "'");
		LOGGER.debug("DmmRepositoryDAO.listBy.queryMaType:'" + assetManageFormDTO.getQueryMaType() + "'");
		LOGGER.debug("DmmRepositoryDAO.listBy.isEnabled:'" + assetManageFormDTO.getQueryIsEnabled() + "'");
		LOGGER.debug("DmmRepositoryDAO.listBy.name:'" + assetManageFormDTO.getAssetTypeName() + "'");
		LOGGER.debug("DmmRepositoryDAO.listBy.keeperName:'" + assetManageFormDTO.getQueryKeeperName() + "'");
		LOGGER.debug("DmmRepositoryDAO.listBy.tid:'" + assetManageFormDTO.getQueryTid() + "'");
		LOGGER.debug("DmmRepositoryDAO.listBy.assetOwner:'" + assetManageFormDTO.getQueryAssetOwner() + "'");
		LOGGER.debug("DmmRepositoryDAO.listBy.assetUser:'" + assetManageFormDTO.getQueryAssetUser() + "'");
		LOGGER.debug("DmmRepositoryDAO.listBy.dtid:'" + assetManageFormDTO.getQueryDtid() + "'");
		LOGGER.debug("DmmRepositoryDAO.listBy.queryPropertyIds:'" + assetManageFormDTO.getQueryPropertyIds() + "'");
		LOGGER.debug("DmmRepositoryDAO.listBy.querySerialNumbers:'" + assetManageFormDTO.getQuerySerialNumbers() + "'");
		LOGGER.debug("DmmRepositoryDAO.listBy.queryIsCup:'" + assetManageFormDTO.getQueryIsCup() + "'");
		LOGGER.debug("DmmRepositoryDAO.listBy.afterTicketCompletionDate:'" + assetManageFormDTO.getAfterTicketCompletionDate() + "'");
		LOGGER.debug("DmmRepositoryDAO.listBy.beforeTicketCompletionDate:'" + assetManageFormDTO.getBeforeTicketCompletionDate() + "'");
		LOGGER.debug("DmmRepositoryDAO.listBy.afterUpdateDate:'" + assetManageFormDTO.getAfterUpdateDate() + "'");
		LOGGER.debug("DmmRepositoryDAO.listBy.beforeUpdateDate:'" + assetManageFormDTO.getBeforeUpdateDate() + "'");
		try {
			List<DmmRepositoryDTO> repositoryDTOs = null;
			String schema = this.getMySchema();
			SqlStatement sql = new SqlStatement();
			sql.addSelectClause("b.ITEM_NAME", DmmRepositoryDTO.ATTRIBUTE.STATUS.getValue()); 
			sql.addSelectClause("b.ITEM_VALUE", DmmRepositoryDTO.ATTRIBUTE.ITEM_VALUE.getValue()); 
			sql.addSelectClause("compa.SHORT_NAME + '-' + w.NAME", DmmRepositoryDTO.ATTRIBUTE.ITEM_NAME.getValue()); 
			sql.addSelectClause("r.ASSET_MODEL", DmmRepositoryDTO.ATTRIBUTE.MODEL.getValue()); 
			sql.addSelectClause("r.CREATE_USER_NAME", DmmRepositoryDTO.ATTRIBUTE.CREATE_USER_NAME.getValue()); 
			sql.addSelectClause("r.CREATE_DATE", DmmRepositoryDTO.ATTRIBUTE.CREATE_DATE.getValue()); 
			sql.addSelectClause("r.SERIAL_NUMBER", DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue()); 
			sql.addSelectClause("a.NAME", DmmRepositoryDTO.ATTRIBUTE.NAME.getValue()); 
			sql.addSelectClause("r.PROPERTY_ID", DmmRepositoryDTO.ATTRIBUTE.PROPERTY_ID.getValue()); 
			sql.addSelectClause("pp.ITEM_NAME", DmmRepositoryDTO.ATTRIBUTE.MA_TYPE.getValue()); 
			sql.addSelectClause("r.IS_ENABLED", DmmRepositoryDTO.ATTRIBUTE.IS_ENABLED.getValue()); 
			sql.addSelectClause("r.ENABLE_DATE", DmmRepositoryDTO.ATTRIBUTE.ENABLE_DATE.getValue()); 
			sql.addSelectClause("r.DESCRIPTION", DmmRepositoryDTO.ATTRIBUTE.DESCRIPTION.getValue()); 
			sql.addSelectClause("r.DESCRIPTION", DmmRepositoryDTO.ATTRIBUTE.BORROWER_COMMENT.getValue()); 
			sql.addSelectClause("adm.CNAME", DmmRepositoryDTO.ATTRIBUTE.CARRIER.getValue()); 
			sql.addSelectClause("us.CNAME", DmmRepositoryDTO.ATTRIBUTE.BORROWER.getValue()); 
			sql.addSelectClause("r.BORROWER_START", DmmRepositoryDTO.ATTRIBUTE.BORROWER_START.getValue()); 
			sql.addSelectClause("r.BORROWER_END", DmmRepositoryDTO.ATTRIBUTE.BORROWER_END.getValue()); 
			sql.addSelectClause("r.BACK_DATE", DmmRepositoryDTO.ATTRIBUTE.BACK_DATE.getValue()); 
			sql.addSelectClause("r.ASSET_OWNER", DmmRepositoryDTO.ATTRIBUTE.ASSET_OWNER.getValue()); 
			sql.addSelectClause("r.ASSET_USER", DmmRepositoryDTO.ATTRIBUTE.ASSET_USER.getValue()); 
			sql.addSelectClause("r.IS_CUP", DmmRepositoryDTO.ATTRIBUTE.IS_CUP.getValue()); 
			sql.addSelectClause("r.ASSET_ID", DmmRepositoryDTO.ATTRIBUTE.ASSET_ID.getValue()); 
			sql.addSelectClause("r.CASE_ID", DmmRepositoryDTO.ATTRIBUTE.CASE_ID.getValue()); 
			sql.addSelectClause("r.CASE_COMPLETION_DATE", DmmRepositoryDTO.ATTRIBUTE.CASE_COMPLETION_DATE.getValue()); 
			sql.addSelectClause("r.DTID", DmmRepositoryDTO.ATTRIBUTE.DTID.getValue()); 
			sql.addSelectClause("p.NAME", DmmRepositoryDTO.ATTRIBUTE.APP_NAME.getValue()); 
			sql.addSelectClause("p.VERSION", DmmRepositoryDTO.ATTRIBUTE.APP_VERSION.getValue()); 
			sql.addSelectClause("c.SHORT_NAME", DmmRepositoryDTO.ATTRIBUTE.SHORT_NAME.getValue()); 
			//CUSTOMER_APPROVE_DATE 為 CHECKED_DATE
			sql.addSelectClause("r.CHECKED_DATE", DmmRepositoryDTO.ATTRIBUTE.CUSTOMER_APPROVE_DATE.getValue()); 
			sql.addSelectClause("cp.SHORT_NAME", DmmRepositoryDTO.ATTRIBUTE.ASSET_USER_NAME.getValue()); 
			sql.addSelectClause("co.SHORT_NAME", DmmRepositoryDTO.ATTRIBUTE.ASSET_OWNER_NAME.getValue()); 
			sql.addSelectClause("r.FACTORY_WARRANTY_DATE", DmmRepositoryDTO.ATTRIBUTE.FACTORY_WARRANTY_DATE.getValue()); 
			sql.addSelectClause("r.UPDATE_DATE", DmmRepositoryDTO.ATTRIBUTE.UPDATE_DATE.getValue()); 
			sql.addSelectClause("r.UPDATE_USER_NAME", DmmRepositoryDTO.ATTRIBUTE.UPDATE_USER_NAME.getValue()); 
			sql.addSelectClause("r.CUSTOMER_WARRANTY_DATE", DmmRepositoryDTO.ATTRIBUTE.CUSTOMER_WARRANTY_DATE.getValue()); 
			//CHECKED_DATE為客戶驗收日期
			sql.addSelectClause("r.CYBER_APPROVED_DATE", DmmRepositoryDTO.ATTRIBUTE.CHECKED_DATE.getValue()); 
			sql.addSelectClause("r.RETIRE_REASON_CODE", DmmRepositoryDTO.ATTRIBUTE.RETIRE_REASON_CODE.getValue()); 
			sql.addSelectClause("r.CONTRACT_ID", DmmRepositoryDTO.ATTRIBUTE.CONTRACT_ID.getValue()); 
			sql.addSelectClause("r.ASSET_IN_TIME", DmmRepositoryDTO.ATTRIBUTE.ASSET_IN_TIME.getValue()); 
			sql.addSelectClause("r.SIM_ENABLE_DATE", DmmRepositoryDTO.ATTRIBUTE.SIM_ENABLE_DATE.getValue()); 
			sql.addSelectClause("r.TID",DmmRepositoryDTO.ATTRIBUTE.TID.getValue()); 
			sql.addSelectClause("comp.SHORT_NAME", DmmRepositoryDTO.ATTRIBUTE.MAINTAIN_COMPANY.getValue()); 
			sql.addSelectClause("pc.ITEM_NAME", DmmRepositoryDTO.ATTRIBUTE.ACTION.getValue()); 
			sql.addSelectClause("r.ACTION", DmmRepositoryDTO.ATTRIBUTE.ACTION_VALUE.getValue()); 
			sql.addSelectClause("r.SIM_ENABLE_NO", DmmRepositoryDTO.ATTRIBUTE.SIM_ENABLE_NO.getValue()); 
			sql.addSelectClause("com.SHORT_NAME", DmmRepositoryDTO.ATTRIBUTE.REPAIR_VENDOR.getValue()); 
			sql.addSelectClause("r.REPAIR_VENDOR", DmmRepositoryDTO.ATTRIBUTE.REPAIR_NENDOR_ID.getValue()); 
			sql.addSelectClause("r.FAULT_COMPONENT", DmmRepositoryDTO.ATTRIBUTE.FAULT_COMPONENT_ID.getValue()); 
			sql.addSelectClause("r.FAULT_DESCRIPTION", DmmRepositoryDTO.ATTRIBUTE.FAULT_DESCRIPTION_ID.getValue()); 
	//		sql.addSelectClause("r.DELETED", DmmRepositoryDTO.ATTRIBUTE.DELETED.getValue()); 
			sql.addSelectClause("bc.CONTRACT_CODE", DmmRepositoryDTO.ATTRIBUTE.CONTRACT_CODE.getValue()); 
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append("case when r.STATUS=:repertory or r.STATUS=:repair or r.STATUS=:pendingDisabled or r.STATUS=:disabled or r.STATUS=:destroy then compa.SHORT_NAME + '-' + w.NAME when ");
			fromBuffer.append("r.STATUS=:borrowing then us.CNAME ");
			fromBuffer.append("when r.STATUS=:inApply then adm.CNAME when r.STATUS=:inUse or r.STATUS=:stop then bt.NAME ");
			fromBuffer.append("when r.STATUS=:maintenance then repairVendor.SHORT_NAME when r.STATUS=:RETURNED then maintainUser.CNAME else compa.SHORT_NAME + '-' + w.NAME end ");
			sql.addSelectClause(fromBuffer.toString(), DmmRepositoryDTO.ATTRIBUTE.BRAND.getValue()); 
			sql.addSelectClause("r.ASSET_IN_ID", DmmRepositoryDTO.ATTRIBUTE.ASSET_IN_ID.getValue()); 
			sql.addSelectClause("STUFF((SELECT ',' + bpid.ITEM_NAME FROM  " + schema + ".BASE_PARAMETER_ITEM_DEF bpid WHERE bpid.BPTD_CODE=:FAULT_COMPONENT and charindex(bpid.ITEM_VALUE,r.FAULT_COMPONENT) > 0 FOR XML PATH('')) ,1 ,1 ,'')", DmmRepositoryDTO.ATTRIBUTE.FAULT_COMPONENT.getValue()); 
			sql.addSelectClause("STUFF((SELECT ',' + bpids.ITEM_NAME FROM  " + schema + ".BASE_PARAMETER_ITEM_DEF bpids WHERE bpids.BPTD_CODE=:FAULT_DESCRIPTION and charindex(bpids.ITEM_VALUE,r.FAULT_DESCRIPTION) > 0 FOR XML PATH('')) ,1 ,1 ,'')", DmmRepositoryDTO.ATTRIBUTE.FAULT_DESCRIPTION.getValue()); 
			//Bug #2644
			sql.addSelectClause("r.IS_CHECKED", DmmRepositoryDTO.ATTRIBUTE.IS_CHECKED.getValue()); 
			sql.addSelectClause("r.IS_CUSTOMER_CHECKED", DmmRepositoryDTO.ATTRIBUTE.IS_CUSTOMER_CHECKED.getValue()); 
			sql.addSelectClause("r.CREATE_USER", DmmRepositoryDTO.ATTRIBUTE.CREATE_USER.getValue()); 
			sql.addSelectClause("r.ASSET_TYPE_ID", DmmRepositoryDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue()); 
			sql.addSelectClause("r.WAREHOUSE_ID", DmmRepositoryDTO.ATTRIBUTE.WAREHOUSE_ID.getValue()); 			
			sql.addSelectClause("r.INSTALLED_ADRESS", DmmRepositoryDTO.ATTRIBUTE.MER_INSTALL_ADDRESS.getValue()); 
			sql.addSelectClause("installType.ITEM_NAME", DmmRepositoryDTO.ATTRIBUTE.INSTALL_TYPE.getValue()); 
			sql.addSelectClause("header.HEADER_NAME", DmmRepositoryDTO.ATTRIBUTE.HEADER_NAME.getValue());
			sql.addSelectClause("bt.MERCHANT_CODE", DmmRepositoryDTO.ATTRIBUTE.MERCHANT_CODE.getValue()); 
			sql.addSelectClause("bt.NAME", DmmRepositoryDTO.ATTRIBUTE.MER_NAME.getValue()); 
			//裝機區域
			sql.addSelectClause("baseparam.ITEM_NAME", DmmRepositoryDTO.ATTRIBUTE.AREA_NAME.getValue()); 
			sql.addSelectClause("r.INSTALLED_ADRESS_LOCATION", DmmRepositoryDTO.ATTRIBUTE.AREA.getValue()); 
			sql.addSelectClause("case when r.STATUS=:borrowing then us.CNAME when r.STATUS=:inApply then adm.CNAME else '' end ", DmmRepositoryDTO.ATTRIBUTE.CARRY.getValue()); 
			//update by 2017/08/02 Bug #2040 Amandawang
			sql.addSelectClause("case when r.DEPARTMENT_ID='CUSTOMER_SERVICE' then '客服' when r.DEPARTMENT_ID='TMS' then 'TMS' when r.DEPARTMENT_ID='QA' then 'QA' else dept.DEPT_NAME end", DmmRepositoryDTO.ATTRIBUTE.DEPARTMENT_ID.getValue());
			sql.addSelectClause("r.BRAND", DmmRepositoryDTO.ATTRIBUTE.ASSET_BRAND.getValue());
			//維護人員
			sql.addSelectClause("maintainUser.CNAME", DmmRepositoryDTO.ATTRIBUTE.VENDOR_STAFF.getValue());
			sql.addSelectClause("r.ANALYZE_DATE", DmmRepositoryDTO.ATTRIBUTE.ANALYZE_DATE.getValue());
			sql.addSelectClause("r.UNINSTALL_OR_REPAIR_REASON", DmmRepositoryDTO.ATTRIBUTE.PROBLEM_REASON.getValue());
			
			sql.addSelectClause("r.COUNTER", DmmRepositoryDTO.ATTRIBUTE.COUNTER.getValue());
			sql.addSelectClause("r.CARTON_NO", DmmRepositoryDTO.ATTRIBUTE.CARTON_NO.getValue());
			sql.addSelectClause("cp.COMPANY_CODE", DmmRepositoryDTO.ATTRIBUTE.COMPANY_CODE.getValue());
			sql.addSelectClause("a.ASSET_CATEGORY", DmmRepositoryDTO.ATTRIBUTE.ASSET_CATEGORY.getValue());
			//Task #2675 案件編號，若沒有對應案件可檢視，不要提供超連結 2017/10/23
			if (IAtomsConstants.PARAM_YES.equals(assetManageFormDTO.getQueryCaseFlag())) {
				sql.addSelectClause("case when r.CASE_ID is not null and r.CASE_ID <>'' and exists (select 1 from dbo.SRM_CASE_HANDLE_INFO s where s.CASE_ID =r.CASE_ID) then (case when exists (select 1 from dbo.SRM_CASE_HANDLE_INFO info where info.CASE_ID =r.CASE_ID and info.CMS_CASE='Y') then 'C' else 'B' end) else 'N' end ", DmmRepositoryDTO.ATTRIBUTE.QUERY_CASE_FLAG.getValue());
			}
			if ("Y".equals(assetManageFormDTO.getQueryFtpsFlag())) {
				sql.addSelectClause("r.CARRY_COMMENT", DmmRepositoryDTO.ATTRIBUTE.CARRY_COMMENT.getValue());
				sql.addSelectClause("r.DISABLED_COMMENT", DmmRepositoryDTO.ATTRIBUTE.DISABLED_COMMENT.getValue());
				sql.addSelectClause("r.REPAIR_COMMENT", DmmRepositoryDTO.ATTRIBUTE.REPAIR_COMMENT.getValue());
				sql.addSelectClause("r.KEEPER_NAME", DmmRepositoryDTO.ATTRIBUTE.KEEPER_NAME.getValue());
				sql.addSelectClause("r.UPDATE_USER", DmmRepositoryDTO.ATTRIBUTE.UPDATE_USER.getValue());
				sql.addSelectClause("r.CARRY_DATE", DmmRepositoryDTO.ATTRIBUTE.CARRY_DATE.getValue());
			}
			
			
			fromBuffer = new StringBuffer();
			fromBuffer.append(schema);
			fromBuffer.append(".DMM_REPOSITORY r left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".DMM_ASSET_TYPE a on r.ASSET_TYPE_ID=a.ASSET_TYPE_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BASE_PARAMETER_ITEM_DEF b on r.STATUS=b.ITEM_VALUE AND b.BPTD_CODE =:assetStatus left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_COMPANY c on c.COMPANY_ID=r.MAINTAIN_COMPANY left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_CONTRACT bc on bc.CONTRACT_ID=r.CONTRACT_ID ");
			fromBuffer.append(" left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BASE_PARAMETER_ITEM_DEF pp ON r.MA_TYPE = pp.ITEM_VALUE AND pp.BPTD_CODE =:maType left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_WAREHOUSE w on r.WAREHOUSE_ID=w.WAREHOUSE_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_COMPANY co on r.ASSET_OWNER=co.COMPANY_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_COMPANY compa on w.COMPANY_ID=compa.COMPANY_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_COMPANY  cp on r.ASSET_USER=cp.COMPANY_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_MERCHANT bt on r.MERCHANT_ID = bt.MERCHANT_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_MERCHANT_HEADER header on header.MERCHANT_HEADER_ID = r.MERCHANT_HEADER_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".PVM_APPLICATION p on r.APPLICATION_ID = p.APPLICATION_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BASE_PARAMETER_ITEM_DEF pc ON (substring(r.ACTION,charindex('-',r.ACTION)+1,LEN(r.ACTION))) = pc.ITEM_VALUE AND pc.BPTD_CODE = :ACTION left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BASE_PARAMETER_ITEM_DEF pid ON r.FAULT_COMPONENT = pid.ITEM_VALUE AND pid.BPTD_CODE = :FAULT_COMPONENT left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BASE_PARAMETER_ITEM_DEF par ON r.FAULT_DESCRIPTION = par.ITEM_VALUE AND par.BPTD_CODE = :FAULT_DESCRIPTION LEFT JOIN ");
			//Bug #2644
			//fromBuffer.append(schema);
			//fromBuffer.append(".DMM_ASSET_IN_LIST al on r.SERIAL_NUMBER = al.SERIAL_NUMBER left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_COMPANY  com on r.REPAIR_VENDOR=com.COMPANY_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_COMPANY  comp on r.MAINTAIN_COMPANY=comp.COMPANY_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".ADM_USER us on us.USER_ID=r.BORROWER left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".ADM_USER adm on adm.USER_ID=r.CARRIER left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".ADM_USER  maintainUser on maintainUser.USER_ID=r.MAINTAIN_USER left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_DEPARTMENT dept on dept.DEPT_CODE=r.DEPARTMENT_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BASE_PARAMETER_ITEM_DEF baseparam on r.INSTALLED_ADRESS_LOCATION=baseparam.ITEM_VALUE and baseparam.BPTD_CODE =:headerArea left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BASE_PARAMETER_ITEM_DEF installType on r.INSTALL_TYPE=installType.ITEM_VALUE and installType.BPTD_CODE =:INSTALL_TYPE left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_COMPANY repairVendor on r.REPAIR_COMPANY=repairVendor.COMPANY_ID ");

			if (StringUtils.hasText(assetManageFormDTO.getUserId())) {
				fromBuffer.append(" left join ");
				fromBuffer.append(schema).append(".ADM_USER user1 on user1.USER_ID =:userId ");
			}
			sql.setFromExpression(fromBuffer.toString());
			if (assetManageFormDTO.getRows() != null) {
				sql.setPageSize(assetManageFormDTO.getRows());
				sql.setStartPage(assetManageFormDTO.getPage() - 1);
			}
			StringBuffer buffer = new StringBuffer();
			
			if (StringUtils.hasText(assetManageFormDTO.getUserId())) {
				sql.addWhereClause(" ( exists (select 1 from " + schema + ".ADM_USER_WAREHOUSE uWarehouse where uWarehouse.USER_ID = user1.USER_ID and uWarehouse.WAREHOUSE_ID = r.WAREHOUSE_ID))");
			}
			//dtid
			if (StringUtils.hasText(assetManageFormDTO.getQueryDtid())) {
				sql.addWhereClause("r.DTID like :dtid", assetManageFormDTO.getQueryDtid() + IAtomsConstants.MARK_PERCENT);
			}
			
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetIds())) {
				String assetIds=assetManageFormDTO.getQueryAssetIds();
				// 若輸入中文逗號替換為英文逗號
				assetIds = assetIds.replaceAll("，", ",");
				buffer = new StringBuffer();
				String [] assetId = assetIds.split(",");
				buffer.append(" r.ASSET_ID in(");
				for (int i = 0; i < assetId.length - 1; i++) {
					if (StringUtils.hasText(assetId[i])) {
						buffer.append("'" + assetId[i] + "',");
					}
				}
				buffer.append("'" + assetId[assetId.length - 1] + "'");
				buffer.append(" ) ");
				sql.addWhereClause(buffer.toString());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetIdList())) {
				String assetIds=assetManageFormDTO.getQueryAssetIdList();
				// 若輸入中文逗號替換為英文逗號
				assetIds = assetIds.replaceAll("，", ",");
				buffer = new StringBuffer();
				String [] assetId = assetIds.split(",");
				buffer.append("r.ASSET_ID in(");
				for (int i = 0; i < assetId.length - 1; i++) {
					if (StringUtils.hasText(assetId[i])) {
						buffer.append("'" + assetId[i] + "',");
					}
				}
				buffer.append("'" + assetId[assetId.length - 1] + "'");
				buffer.append(" ) ");
				sql.addWhereClause(buffer.toString());
			}
			String serialNumberCodeGun = null;
			if (StringUtils.hasText(assetManageFormDTO.getQuerySerialNumbers())) {
				String serialNumbers=assetManageFormDTO.getQuerySerialNumbers();
				// 若輸入中文逗號替換為英文逗號
				serialNumbers = serialNumbers.replaceAll("，", ","); 
				String [] serialNumber = serialNumbers.split(",");
				buffer = new StringBuffer();
				buffer.append("r.SERIAL_NUMBER in(");
				serialNumberCodeGun = "'";
				for (int i = 0; i < serialNumber.length - 1; i++) {
					if (StringUtils.hasText(serialNumber[i])) {
						buffer.append("'" + serialNumber[i] + "',");
						serialNumberCodeGun = serialNumberCodeGun + ("*" + serialNumber[i] + "*,");
					}
				}
				buffer.append("'" + serialNumber[serialNumber.length - 1] + "'");
				serialNumberCodeGun = serialNumberCodeGun + "*" + serialNumber[serialNumber.length - 1] + "*')";
				buffer.append(" ) ");
				sql.addWhereClause(buffer.toString());
			}
			String propertyIdCodeGun = null;
			//財產編號
			if (StringUtils.hasText(assetManageFormDTO.getQueryPropertyIds())) {
				String propertyIds=assetManageFormDTO.getQueryPropertyIds();
				// 若輸入中文逗號替換為英文逗號
				propertyIds = propertyIds.replaceAll("，", ",");
				buffer = new StringBuffer();
				String [] propertyId = propertyIds.split(",");
				buffer.append(" r.PROPERTY_ID in(");
				propertyIdCodeGun = "'";
				for (int i = 0; i < propertyId.length - 1; i++) {
					if (StringUtils.hasText(propertyId[i])) {
						buffer.append("'" + propertyId[i] + "',");
						propertyIdCodeGun = propertyIdCodeGun + ("*" + propertyId[i] + "*,");

					}
				}
				buffer.append("'" + propertyId[propertyId.length - 1] + "'");
				propertyIdCodeGun = propertyIdCodeGun + "*" + propertyId[propertyId.length - 1] + "*')";

				buffer.append(" ) ");
				sql.addWhereClause(buffer.toString());
			}
			//設備類型 //Task #2991
			if (StringUtils.hasText(assetManageFormDTO.getAssetTypeName())) {
				String assetTypeIds=assetManageFormDTO.getAssetTypeName();
				String [] assetTypeId = assetTypeIds.split(",");
				StringBuffer serialBuffer = new StringBuffer();
				for (int i = 0; i < assetTypeId.length - 1; i++) {
					if (StringUtils.hasText(assetTypeId[i])) {
						serialBuffer.append("'" + assetTypeId[i] + "',");
					}
				}
				serialBuffer.append("'" + assetTypeId[assetTypeId.length - 1] + "'");
				serialBuffer.append(" ) ");
				sql.addWhereClause("r.ASSET_TYPE_ID in(" + serialBuffer);
				//sql.addWhereClause("r.ASSET_TYPE_ID =:name", );
			}
			//動作
			if (StringUtils.hasText(assetManageFormDTO.getQueryAction())) {
				String actions=assetManageFormDTO.getQueryAction();
				String [] action = actions.split(",");
				StringBuffer serialBuffer = new StringBuffer();
				for (int i = 0; i < action.length - 1; i++) {
					if (StringUtils.hasText(action[i])) {
						serialBuffer.append("'" + action[i] + "',");
					}
				}
				serialBuffer.append("'" + action[action.length - 1] + "'");
				serialBuffer.append(" ) ");
				sql.addWhereClause("r.ACTION in(" + serialBuffer);
				//sql.addWhereClause("r.ACTION =:action", assetManageFormDTO.getQueryAction());
			}
			//使用人
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetUser())) {
				String assetUsers=assetManageFormDTO.getQueryAssetUser();
				String [] assetUser = assetUsers.split(",");
				StringBuffer serialBuffer = new StringBuffer();
				for (int i = 0; i < assetUser.length - 1; i++) {
					if (StringUtils.hasText(assetUser[i])) {
						serialBuffer.append("'" + assetUser[i] + "',");
					}
				}
				serialBuffer.append("'" + assetUser[assetUser.length - 1] + "'");
				serialBuffer.append(" ) ");
				sql.addWhereClause("r.ASSET_USER in(" + serialBuffer);
				//sql.addWhereClause("r.ASSET_USER = :assetUser", assetManageFormDTO.getQueryAssetUser());
			} else {
				//Task #3046
				if (StringUtils.hasText(assetManageFormDTO.getHideQueryAssetUser())) {
					sql.addWhereClause("r.ASSET_USER = :assetUser", assetManageFormDTO.getHideQueryAssetUser());
				}
			}
			//資產owner
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetOwner())) {
				sql.addWhereClause("r.ASSET_OWNER = :assetOwner", assetManageFormDTO.getQueryAssetOwner());
			}
			//櫃位
			if (StringUtils.hasText(assetManageFormDTO.getQueryCounter())) {
				sql.addWhereClause("r.COUNTER like :counter", assetManageFormDTO.getQueryCounter() + IAtomsConstants.MARK_PERCENT);
			}
			//箱號
			if (StringUtils.hasText(assetManageFormDTO.getQueryCartonNo())) {
				sql.addWhereClause("r.CARTON_NO like :cartonNo", assetManageFormDTO.getQueryCartonNo() + IAtomsConstants.MARK_PERCENT);
			}
			//合約編號
			if (StringUtils.hasText(assetManageFormDTO.getQueryContractId())) {
				sql.addWhereClause("r.CONTRACT_ID = :contractId", assetManageFormDTO.getQueryContractId());
			}
			//狀態
			if (StringUtils.hasText(assetManageFormDTO.getQueryStatus())) {
				sql.addWhereClause("r.STATUS =:status", assetManageFormDTO.getQueryStatus());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryUser())) {
				sql.addWhereClause("bc.COMPANY_ID =:queryUser", assetManageFormDTO.getQueryUser());
			}
			//類別
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetCategory())) {
				sql.addWhereClause("a.ASSET_CATEGORY =:queryAssetCategory", assetManageFormDTO.getQueryAssetCategory());
			}			
			//倉庫
			if (StringUtils.hasText(assetManageFormDTO.getQueryStorage())) {
				sql.addWhereClause("r.WAREHOUSE_ID =:warehouseId", assetManageFormDTO.getQueryStorage());
			}
			//設備編號
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetId())) {
				sql.addWhereClause("r.ASSET_ID = :assetId", assetManageFormDTO.getQueryAssetId());
			}
			//特店名稱
			if (StringUtils.hasText(assetManageFormDTO.getQueryMerName())) {
				sql.addWhereClause("bt.NAME like :merRegisteredName", assetManageFormDTO.getQueryMerName() + IAtomsConstants.MARK_PERCENT);
			}
			//特店表頭
			if (StringUtils.hasText(assetManageFormDTO.getQueryHeaderName())) {
				sql.addWhereClause("header.HEADER_NAME like :merAnnouncedName", assetManageFormDTO.getQueryHeaderName() + IAtomsConstants.MARK_PERCENT);
			}
			//特店代號
			if (StringUtils.hasText(assetManageFormDTO.getQueryMerchantCode())) {
				sql.addWhereClause("bt.MERCHANT_CODE like :mid", assetManageFormDTO.getQueryMerchantCode() + IAtomsConstants.MARK_PERCENT);
			} 
			//裝機地址
			if (StringUtils.hasText(assetManageFormDTO.getQueryMerInstallAddress())) {
				sql.addWhereClause("r.INSTALLED_ADRESS like :merInstallAddress", assetManageFormDTO.getQueryMerInstallAddress() + IAtomsConstants.MARK_PERCENT);
			}
			//裝機類別
			if (StringUtils.hasText(assetManageFormDTO.getQueryMerInstallAddress())) {
				sql.addWhereClause("r.INSTALL_TYPE like :merInstallAddress", assetManageFormDTO.getQueryMerInstallAddress() + IAtomsConstants.MARK_PERCENT);
			}
			//裝機區域
			if (StringUtils.hasText(assetManageFormDTO.getQueryArea())) {
				sql.addWhereClause("r.INSTALLED_ADRESS_LOCATION like :area", assetManageFormDTO.getQueryArea() + IAtomsConstants.MARK_PERCENT);
			}
			//維護模式
			if (StringUtils.hasText(assetManageFormDTO.getQueryMaType())) {
				sql.addWhereClause("r.MA_TYPE = :queryMaType", assetManageFormDTO.getQueryMaType());
			}
			//是否啟用
			if (StringUtils.hasText(assetManageFormDTO.getQueryIsEnabled())) {
				sql.addWhereClause("r.IS_ENABLED = :isEnabled", assetManageFormDTO.getQueryIsEnabled());
			}
			//保管人
			if (StringUtils.hasText(assetManageFormDTO.getQueryKeeperName())) {
				sql.addWhereClause("r.KEEPER_NAME like :keeperName", assetManageFormDTO.getQueryKeeperName() + IAtomsConstants.MARK_PERCENT);
			}
			//tid
			if (StringUtils.hasText(assetManageFormDTO.getQueryTid())) {
				sql.addWhereClause("r.TID like :tid", assetManageFormDTO.getQueryTid() + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryIsCup())) {
				sql.addWhereClause("r.IS_CUP =:queryIsCup", assetManageFormDTO.getQueryIsCup());
			}

			if (StringUtils.hasText(assetManageFormDTO.getBeforeTicketCompletionDate())) {
				sql.addWhereClause("CONVERT(varchar(10),r.CASE_COMPLETION_DATE,111)>=:beforeTicketCompletionDate", assetManageFormDTO.getBeforeTicketCompletionDate());
			}
			if (StringUtils.hasText(assetManageFormDTO.getAfterTicketCompletionDate())) {
				sql.addWhereClause("CONVERT(varchar(10),r.CASE_COMPLETION_DATE,111)<=:afterTicketCompletionDate", assetManageFormDTO.getAfterTicketCompletionDate());
			}
			if (StringUtils.hasText(assetManageFormDTO.getBeforeUpdateDate())) {
				sql.addWhereClause("CONVERT(varchar(10),r.UPDATE_DATE,111)>=:beforeUpdateDate", assetManageFormDTO.getBeforeUpdateDate());
			} 
			if (StringUtils.hasText(assetManageFormDTO.getAfterUpdateDate())) {
				sql.addWhereClause("CONVERT(varchar(10),r.UPDATE_DATE,111)<=:afterUpdateDate", assetManageFormDTO.getAfterUpdateDate());
			}
			//Task #3127
			if (StringUtils.hasText(assetManageFormDTO.getQueryModel())) {
				sql.addWhereClause("r.ASSET_MODEL =:queryModel", assetManageFormDTO.getQueryModel());
			}
			if (DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue().equals(assetManageFormDTO.getCodeGunFlag())) {
				sql.setOrderByExpression("CHARINDEX('*'+Rtrim(cast(r.SERIAL_NUMBER as varchar(20))+'*'), " + " " + serialNumberCodeGun);
			} else if (DmmRepositoryDTO.ATTRIBUTE.PROPERTY_ID.getValue().equals(assetManageFormDTO.getCodeGunFlag())) {
				sql.setOrderByExpression("CHARINDEX('*'+Rtrim(cast(r.PROPERTY_ID as varchar(20))+'*'), " + " " + propertyIdCodeGun);
			} else if ((StringUtils.hasText(assetManageFormDTO.getSort())) && (StringUtils.hasText(assetManageFormDTO.getOrder()))) {
				sql.setOrderByExpression(assetManageFormDTO.getSort() + " " + assetManageFormDTO.getOrder());
			} else {
				sql.setOrderByExpression(AssetManageFormDTO.PARAM_PAGE_SORT + " " + IAtomsConstants.PARAM_PAGE_ORDER);
			}
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			sqlQueryBean.setParameter("assetStatus", IAtomsConstants.PARAM_ASSET_STATUS);
			sqlQueryBean.setParameter("maType", IAtomsConstants.PARAM_MA_TYPE);
			sqlQueryBean.setParameter("repertory", IAtomsConstants.PARAM_ASSET_STATUS_REPERTORY);
			sqlQueryBean.setParameter("repair", IAtomsConstants.PARAM_ASSET_STATUS_REPAIR);
			sqlQueryBean.setParameter("disabled", IAtomsConstants.PARAM_ASSET_STATUS_DISABLED);
			sqlQueryBean.setParameter("destroy", IAtomsConstants.PARAM_ASSET_STATUS_DESTROY);
			sqlQueryBean.setParameter("borrowing", IAtomsConstants.PARAM_ASSET_STATUS_BORROWING);
			sqlQueryBean.setParameter("pendingDisabled", IAtomsConstants.PARAM_ASSET_STATUS_PENDING_DISABLED);
			if (StringUtils.hasText(assetManageFormDTO.getUserId())) {
				sqlQueryBean.setParameter("userId", assetManageFormDTO.getUserId());
			}
			sqlQueryBean.setParameter("inApply", IAtomsConstants.PARAM_ASSET_STATUS_IN_APPLY);
			sqlQueryBean.setParameter("inUse", IAtomsConstants.PARAM_ASSET_STATUS_IN_USE);
			sqlQueryBean.setParameter("maintenance", IAtomsConstants.PARAM_ASSET_STATUS_MAINTENANCE);
			//Task #3509 設備狀態=停用中，位置是 特店
			sqlQueryBean.setParameter("stop", IAtomsConstants.PARAM_ASSET_STATUS_STOP);
			sqlQueryBean.setParameter("FAULT_COMPONENT", IATOMS_PARAM_TYPE.FAULT_COMPONENT.getCode());
			sqlQueryBean.setParameter("FAULT_DESCRIPTION", IATOMS_PARAM_TYPE.FAULT_DESCRIPTION.getCode());
			sqlQueryBean.setParameter("ACTION", IATOMS_PARAM_TYPE.ACTION.getCode());
			sqlQueryBean.setParameter("headerArea", IATOMS_PARAM_TYPE.LOCATION.getCode());
			sqlQueryBean.setParameter("INSTALL_TYPE", IATOMS_PARAM_TYPE.INSTALL_TYPE.getCode());
			sqlQueryBean.setParameter("RETURNED", IAtomsConstants.PARAM_ASSET_STATUS_RETURNED);
			
			LOGGER.debug("DmmRepositoryDAO.listBy()", "sql:" + sql.toString());
			AliasBean aliasBean = new AliasBean(DmmRepositoryDTO.class);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.STATUS.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.ITEM_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.ITEM_VALUE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.ASSET_ID.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.SHORT_NAME.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.ASSET_IN_TIME.getValue(), TimestampType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.ASSET_USER.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.ASSET_OWNER.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.CUSTOMER_WARRANTY_DATE.getValue(), DateType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.MODEL.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.ASSET_OWNER_NAME.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.ASSET_USER_NAME.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.CUSTOMER_APPROVE_DATE.getValue(), DateType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.PROPERTY_ID.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.CONTRACT_ID.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.DESCRIPTION.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.BORROWER.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.BORROWER_START.getValue(), TimestampType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.BORROWER_END.getValue(), TimestampType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.BACK_DATE.getValue(), TimestampType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.IS_CUP.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.CHECKED_DATE.getValue(), DateType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.FAULT_COMPONENT.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.FAULT_DESCRIPTION.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.RETIRE_REASON_CODE.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.NAME.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.MA_TYPE.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.IS_ENABLED.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.ACTION.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.CARRIER.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.ENABLE_DATE.getValue(), TimestampType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.CASE_COMPLETION_DATE.getValue(), TimestampType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.APP_NAME.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.APP_VERSION.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.FACTORY_WARRANTY_DATE.getValue(), DateType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.UPDATE_DATE.getValue(), TimestampType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.UPDATE_USER_NAME.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.CREATE_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.CREATE_USER_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.PROPERTY_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.SIM_ENABLE_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.CASE_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.INSTALL_TYPE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.TID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.ASSET_IN_TIME.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.MER_NAME.getValue(), StringType.INSTANCE);
			//aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.IS_SIM_ENABLE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.SIM_ENABLE_NO.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.HEADER_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.REPAIR_VENDOR.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.ASSET_IN_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.MAINTAIN_COMPANY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.FAULT_COMPONENT.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.FAULT_DESCRIPTION.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.IS_CHECKED.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.IS_CUSTOMER_CHECKED.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.REPAIR_NENDOR_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.FAULT_COMPONENT_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.FAULT_DESCRIPTION_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.CREATE_USER.getValue(), StringType.INSTANCE);
//			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.DELETED.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.CONTRACT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.MER_INSTALL_ADDRESS.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.HEADER_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.AREA.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.ANALYZE_DATE.getValue(), DateType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.DEPARTMENT_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.VENDOR_STAFF.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.PROBLEM_REASON.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.BRAND.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.CARRY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.ASSET_BRAND.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.BORROWER_COMMENT.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.AREA_NAME.getValue(), StringType.INSTANCE);
		
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.COUNTER.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.CARTON_NO.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.COMPANY_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.ACTION_VALUE.getValue(), StringType.INSTANCE);
			if (IAtomsConstants.PARAM_YES.equals(assetManageFormDTO.getQueryCaseFlag())) {
				aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.QUERY_CASE_FLAG.getValue(), StringType.INSTANCE);
			}
			
			if ("Y".equals(assetManageFormDTO.getQueryFtpsFlag())) {
				aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.CARRY_COMMENT.getValue(), StringType.INSTANCE);
				aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.DISABLED_COMMENT.getValue(), StringType.INSTANCE);
				aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.REPAIR_COMMENT.getValue(), StringType.INSTANCE);
				aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.KEEPER_NAME.getValue(), StringType.INSTANCE);
				aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.UPDATE_USER.getValue(), StringType.INSTANCE);
			}
			//AliasBean aliasBean = sql.createAliasBean(DmmRepositoryDTO.class);
			LOGGER.debug("DmmRepositoryDAO.listBy()", "sql:" + sqlQueryBean.toString());
			repositoryDTOs = (List<DmmRepositoryDTO>) super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return repositoryDTOs;
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ":Failed-- listBy()---" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#count(com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetManageFormDTO)
	 */
	@Override
	public Integer count(AssetManageFormDTO assetManageFormDTO) throws DataAccessException {
		try {
			String schema = this.getMySchema();
			SqlStatement sql = new SqlStatement();
			StringBuffer fromBuffer = new StringBuffer();
			sql.addSelectClause("count(1)");			
			fromBuffer.append(schema);
			fromBuffer.append(".DMM_REPOSITORY r left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".DMM_ASSET_TYPE a on r.ASSET_TYPE_ID=a.ASSET_TYPE_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_MERCHANT bt on r.MERCHANT_ID = bt.MERCHANT_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_MERCHANT_HEADER header on header.MERCHANT_HEADER_ID = r.MERCHANT_HEADER_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_CONTRACT bc on bc.CONTRACT_ID=r.CONTRACT_ID  ");
			if (StringUtils.hasText(assetManageFormDTO.getUserId())) {
				fromBuffer.append(" left join ");
				fromBuffer.append(schema).append(".ADM_USER user1 on user1.USER_ID =:userId ");
			}
			sql.addFromExpression(fromBuffer.toString());
			
			if (StringUtils.hasText(assetManageFormDTO.getQuerySerialNumbers())) {
				String serialNumbers=assetManageFormDTO.getQuerySerialNumbers();
				// 若輸入中文逗號替換為英文逗號
				serialNumbers = serialNumbers.replaceAll("，",","); 
				String [] serialNumber = serialNumbers.split(",");
				StringBuffer serialBuffer = new StringBuffer();
				for (int i = 0; i < serialNumber.length - 1; i++) {
					if (StringUtils.hasText(serialNumber[i])) {
						serialBuffer.append("'" + serialNumber[i] + "',");
					}
				}
				serialBuffer.append("'" + serialNumber[serialNumber.length - 1] + "'");
				serialBuffer.append(" ) ");
				sql.addWhereClause("r.SERIAL_NUMBER in(" + serialBuffer);
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryDtid())) {
				sql.addWhereClause("r.DTID like :dtid", assetManageFormDTO.getQueryDtid() + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryContractId())) {
				sql.addWhereClause("r.CONTRACT_ID = :contractId", assetManageFormDTO.getQueryContractId());
			}
			//Task #2991
			if (StringUtils.hasText(assetManageFormDTO.getAssetTypeName())) {
				String assetTypeIds=assetManageFormDTO.getAssetTypeName();
				String [] assetTypeId = assetTypeIds.split(",");
				StringBuffer serialBuffer = new StringBuffer();
				for (int i = 0; i < assetTypeId.length - 1; i++) {
					if (StringUtils.hasText(assetTypeId[i])) {
						serialBuffer.append("'" + assetTypeId[i] + "',");
					}
				}
				serialBuffer.append("'" + assetTypeId[assetTypeId.length - 1] + "'");
				serialBuffer.append(" ) ");
				sql.addWhereClause("r.ASSET_TYPE_ID in(" + serialBuffer);
				//sql.addWhereClause("r.ASSET_TYPE_ID =:name", assetManageFormDTO.getAssetTypeName());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetUser())) {
				String assetUsers=assetManageFormDTO.getQueryAssetUser();
				String [] assetUser = assetUsers.split(",");
				StringBuffer serialBuffer = new StringBuffer();
				for (int i = 0; i < assetUser.length - 1; i++) {
					if (StringUtils.hasText(assetUser[i])) {
						serialBuffer.append("'" + assetUser[i] + "',");
					}
				}
				serialBuffer.append("'" + assetUser[assetUser.length - 1] + "'");
				serialBuffer.append(" ) ");
				sql.addWhereClause("r.ASSET_USER in(" + serialBuffer);
				//sql.addWhereClause("r.ASSET_USER = :assetUser", assetManageFormDTO.getQueryAssetUser());
			} else {
				//Task #3046
				if (StringUtils.hasText(assetManageFormDTO.getHideQueryAssetUser())) {
					sql.addWhereClause("r.ASSET_USER = :assetUser", assetManageFormDTO.getHideQueryAssetUser());
				}
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryAction())) {
				String actions=assetManageFormDTO.getQueryAction();
				String [] action = actions.split(",");
				StringBuffer serialBuffer = new StringBuffer();
				for (int i = 0; i < action.length - 1; i++) {
					if (StringUtils.hasText(action[i])) {
						serialBuffer.append("'" + action[i] + "',");
					}
				}
				serialBuffer.append("'" + action[action.length - 1] + "'");
				serialBuffer.append(" ) ");
				sql.addWhereClause("r.ACTION in(" + serialBuffer);
				//sql.addWhereClause("r.ACTION =:action", assetManageFormDTO.getQueryAction());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryStatus())) {
				sql.addWhereClause("r.STATUS =:status", assetManageFormDTO.getQueryStatus());
			}

			if (StringUtils.hasText(assetManageFormDTO.getUserId())) {
				sql.addWhereClause("(exists (select 1 from " + schema + ".ADM_USER_WAREHOUSE uWarehouse where uWarehouse.USER_ID = user1.USER_ID and uWarehouse.WAREHOUSE_ID = r.WAREHOUSE_ID))");
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryUser())) {
				sql.addWhereClause("bc.COMPANY_ID =:queryUser", assetManageFormDTO.getQueryUser());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetCategory())) {
				sql.addWhereClause("a.ASSET_CATEGORY =:queryAssetCategory", assetManageFormDTO.getQueryAssetCategory());
			}
			
			if (StringUtils.hasText(assetManageFormDTO.getQueryStorage())) {
				sql.addWhereClause("r.WAREHOUSE_ID =:warehouseId", assetManageFormDTO.getQueryStorage());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetId())) {
				sql.addWhereClause("r.ASSET_ID = :assetId", assetManageFormDTO.getQueryAssetId());
			}
			//櫃位
			if (StringUtils.hasText(assetManageFormDTO.getQueryCounter())) {
				sql.addWhereClause("r.COUNTER like :counter", assetManageFormDTO.getQueryCounter() + IAtomsConstants.MARK_PERCENT);
			}
			//箱號
			if (StringUtils.hasText(assetManageFormDTO.getQueryCartonNo())) {
				sql.addWhereClause("r.CARTON_NO like :cartonNo", assetManageFormDTO.getQueryCartonNo() + IAtomsConstants.MARK_PERCENT);
			}
			//特店名稱
			if (StringUtils.hasText(assetManageFormDTO.getQueryMerName())) {
				sql.addWhereClause("bt.NAME like :merRegisteredName", assetManageFormDTO.getQueryMerName() + IAtomsConstants.MARK_PERCENT);
			}
			//特店表頭
			if (StringUtils.hasText(assetManageFormDTO.getQueryHeaderName())) {
				sql.addWhereClause("header.HEADER_NAME like :merAnnouncedName", assetManageFormDTO.getQueryHeaderName() + IAtomsConstants.MARK_PERCENT);
			}
			//特店代號
			if (StringUtils.hasText(assetManageFormDTO.getQueryMerchantCode())) {
				sql.addWhereClause("bt.MERCHANT_CODE like :mid", assetManageFormDTO.getQueryMerchantCode() + IAtomsConstants.MARK_PERCENT);
			} 
			//裝機地址
			if (StringUtils.hasText(assetManageFormDTO.getQueryMerInstallAddress())) {
				sql.addWhereClause("r.INSTALLED_ADRESS like :merInstallAddress", assetManageFormDTO.getQueryMerInstallAddress() + IAtomsConstants.MARK_PERCENT);
			}
			//裝機類別
			if (StringUtils.hasText(assetManageFormDTO.getQueryMerInstallAddress())) {
				sql.addWhereClause("r.INSTALL_TYPE like :merInstallAddress", assetManageFormDTO.getQueryMerInstallAddress() + IAtomsConstants.MARK_PERCENT);
			}
			//裝機區域
			if (StringUtils.hasText(assetManageFormDTO.getQueryArea())) {
				sql.addWhereClause("r.INSTALLED_ADRESS_LOCATION like :area", assetManageFormDTO.getQueryArea() + IAtomsConstants.MARK_PERCENT);
			}
			
			if (StringUtils.hasText(assetManageFormDTO.getQueryMaType())) {
				sql.addWhereClause("r.MA_TYPE = :queryMaType", assetManageFormDTO.getQueryMaType());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryIsEnabled())) {
				sql.addWhereClause("r.IS_ENABLED = :isEnabled", assetManageFormDTO.getQueryIsEnabled());
			}
			
			//保管人
			if (StringUtils.hasText(assetManageFormDTO.getQueryKeeperName())) {
				sql.addWhereClause("r.KEEPER_NAME like :keeperName", assetManageFormDTO.getQueryKeeperName() + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryTid())) {
				sql.addWhereClause("r.TID like :tid", assetManageFormDTO.getQueryTid() + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetOwner())) {
				sql.addWhereClause("r.ASSET_OWNER = :assetOwner", assetManageFormDTO.getQueryAssetOwner());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetIds())) {
				String assetIds=assetManageFormDTO.getQueryAssetIds();
				// 若輸入中文逗號替換為英文逗號
				assetIds = assetIds.replaceAll("，", ",");
				String [] assetIdList = assetIds.split(",");
				StringBuffer assetBuffer = new StringBuffer();
				for (int i = 0; i < assetIdList.length - 1; i++) {
					if (StringUtils.hasText(assetIdList[i])) {
						assetBuffer.append("'" + assetIdList[i] + "',");
					}
				}
				assetBuffer.append("'" + assetIdList[assetIdList.length - 1] + "'");
				assetBuffer.append(" ) ");
				sql.addWhereClause("r.ASSET_ID in(" + assetBuffer);
				//sql.addWhereClause("r.ASSET_ID in(:assetIds)", StringUtils.toList(assetIds, IAtomsConstants.MARK_SEPARATOR));
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetIdList())) {
				String assetIds=assetManageFormDTO.getQueryAssetIdList();
				// 若輸入中文逗號替換為英文逗號
				assetIds = assetIds.replaceAll("，",",");
				String [] assetIdList = assetIds.split(",");
				StringBuffer assetBuffer = new StringBuffer();
				for (int i = 0; i < assetIdList.length - 1; i++) {
					if (StringUtils.hasText(assetIdList[i])) {
						assetBuffer.append("'" + assetIdList[i] + "',");
					}
				}
				assetBuffer.append("'" + assetIdList[assetIdList.length - 1] + "'");
				assetBuffer.append(" ) ");
				sql.addWhereClause("r.ASSET_ID in(" + assetBuffer);
				//sql.addWhereClause("r.ASSET_ID in(:assetIdList)", StringUtils.toList(assetIds, IAtomsConstants.MARK_SEPARATOR));
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryPropertyIds())) {
				String propertyIds=assetManageFormDTO.getQueryPropertyIds();
				// 若輸入中文逗號替換為英文逗號
				propertyIds = propertyIds.replaceAll("，",",");
				String [] propertyId = propertyIds.split(",");
				StringBuffer propertyBuffer = new StringBuffer();
				for (int i = 0; i < propertyId.length - 1; i++) {
					if (StringUtils.hasText(propertyId[i])) {
						propertyBuffer.append("'" + propertyId[i] + "',");
					}
				}
				propertyBuffer.append("'" + propertyId[propertyId.length - 1] + "'");
				propertyBuffer.append(" ) ");
				sql.addWhereClause("r.PROPERTY_ID in(" + propertyBuffer);
			}
			
			if (StringUtils.hasText(assetManageFormDTO.getQueryIsCup())) {
				sql.addWhereClause("r.IS_CUP =:queryIsCup", assetManageFormDTO.getQueryIsCup());
			}
			if (StringUtils.hasText(assetManageFormDTO.getBeforeTicketCompletionDate())) {
				sql.addWhereClause("CONVERT(varchar(10),r.CASE_COMPLETION_DATE,111)>=:beforeTicketCompletionDate", assetManageFormDTO.getBeforeTicketCompletionDate());
			}
			if (StringUtils.hasText(assetManageFormDTO.getAfterTicketCompletionDate())) {
				sql.addWhereClause("CONVERT(varchar(10),r.CASE_COMPLETION_DATE,111)<=:afterTicketCompletionDate", assetManageFormDTO.getAfterTicketCompletionDate());
			}
			if (StringUtils.hasText(assetManageFormDTO.getBeforeUpdateDate())) {
				sql.addWhereClause("CONVERT(varchar(10),r.UPDATE_DATE,111)>=:beforeUpdateDate", assetManageFormDTO.getBeforeUpdateDate());
			} 
			if (StringUtils.hasText(assetManageFormDTO.getAfterUpdateDate())) {
				sql.addWhereClause("CONVERT(varchar(10),r.UPDATE_DATE,111)<=:afterUpdateDate", assetManageFormDTO.getAfterUpdateDate());
			}
			//Task #3127
			if (StringUtils.hasText(assetManageFormDTO.getQueryModel())) {
				sql.addWhereClause("r.ASSET_MODEL =:queryModel", assetManageFormDTO.getQueryModel());
			}
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			if (StringUtils.hasText(assetManageFormDTO.getUserId())) {
				sqlQueryBean.setParameter("userId", assetManageFormDTO.getUserId());
			}
			LOGGER.debug("DmmRepositoryDAO.count()", "sql:" + sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				return result.get(0).intValue();
			}
			return 0;
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.count() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#getListByAssetId(com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetManageFormDTO)
	 */
	@Override
	public List<DmmRepositoryHistoryDTO> getListByAssetId(AssetManageFormDTO assetManageFormDTO) throws DataAccessException {
		
		try {
			List<DmmRepositoryHistoryDTO> dmmRepositoryHistoryDTOs = null;
			String schema = this.getMySchema();
			SqlStatement sql = new SqlStatement();
			sql.addSelectClause("r.HISTORY_ID", DmmRepositoryHistoryDTO.ATTRIBUTE.HISTORY_ID.getValue()); 
			
			sql.addSelectClause("b.ITEM_NAME", DmmRepositoryHistoryDTO.ATTRIBUTE.STATUS.getValue()); 
			sql.addSelectClause("b.ITEM_VALUE", DmmRepositoryHistoryDTO.ATTRIBUTE.ITEM_VALUE.getValue()); 
			sql.addSelectClause("compa.SHORT_NAME + '-' + w.NAME", DmmRepositoryHistoryDTO.ATTRIBUTE.ITEM_NAME.getValue()); 
			sql.addSelectClause("r.ASSET_MODEL", DmmRepositoryHistoryDTO.ATTRIBUTE.MODEL.getValue()); 
			sql.addSelectClause("r.CREATE_USER_NAME", DmmRepositoryHistoryDTO.ATTRIBUTE.CREATE_USER_NAME.getValue()); 
			sql.addSelectClause("r.CREATE_DATE", DmmRepositoryHistoryDTO.ATTRIBUTE.CREATE_DATE.getValue()); 
			sql.addSelectClause("r.SERIAL_NUMBER", DmmRepositoryHistoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue()); 
			sql.addSelectClause("a.NAME", DmmRepositoryHistoryDTO.ATTRIBUTE.NAME.getValue()); 
			sql.addSelectClause("r.PROPERTY_ID", DmmRepositoryHistoryDTO.ATTRIBUTE.PROPERTY_ID.getValue()); 
			sql.addSelectClause("pp.ITEM_NAME", DmmRepositoryHistoryDTO.ATTRIBUTE.MA_TYPE.getValue()); 
			sql.addSelectClause("r.IS_ENABLED", DmmRepositoryHistoryDTO.ATTRIBUTE.IS_ENABLED.getValue()); 
			sql.addSelectClause("r.ENABLE_DATE", DmmRepositoryHistoryDTO.ATTRIBUTE.ENABLE_DATE.getValue()); 
			sql.addSelectClause("r.DESCRIPTION", DmmRepositoryHistoryDTO.ATTRIBUTE.DESCRIPTION.getValue()); 
			sql.addSelectClause("r.DESCRIPTION", DmmRepositoryHistoryDTO.ATTRIBUTE.BORROWER_COMMENT.getValue()); 
			sql.addSelectClause("adm.CNAME", DmmRepositoryHistoryDTO.ATTRIBUTE.CARRIER.getValue()); 
			sql.addSelectClause("us.CNAME", DmmRepositoryHistoryDTO.ATTRIBUTE.BORROWER.getValue()); 
			sql.addSelectClause("r.BORROWER_START", DmmRepositoryHistoryDTO.ATTRIBUTE.BORROWER_START.getValue()); 
			sql.addSelectClause("r.BORROWER_END", DmmRepositoryHistoryDTO.ATTRIBUTE.BORROWER_END.getValue()); 
			sql.addSelectClause("r.BACK_DATE", DmmRepositoryHistoryDTO.ATTRIBUTE.BACK_DATE.getValue()); 
			sql.addSelectClause("r.ASSET_OWNER", DmmRepositoryHistoryDTO.ATTRIBUTE.ASSET_OWNER.getValue()); 
			sql.addSelectClause("r.ASSET_USER", DmmRepositoryHistoryDTO.ATTRIBUTE.ASSET_USER.getValue()); 
			sql.addSelectClause("r.IS_CUP", DmmRepositoryHistoryDTO.ATTRIBUTE.IS_CUP.getValue()); 
			sql.addSelectClause("r.ASSET_ID", DmmRepositoryHistoryDTO.ATTRIBUTE.ASSET_ID.getValue()); 
			sql.addSelectClause("r.CASE_ID", DmmRepositoryHistoryDTO.ATTRIBUTE.CASE_ID.getValue()); 
			sql.addSelectClause("r.CASE_COMPLETION_DATE", DmmRepositoryHistoryDTO.ATTRIBUTE.CASE_COMPLETION_DATE.getValue()); 
			sql.addSelectClause("r.DTID", DmmRepositoryHistoryDTO.ATTRIBUTE.DTID.getValue()); 
			sql.addSelectClause("r.ASSET_IN_ID", DmmRepositoryHistoryDTO.ATTRIBUTE.ASSET_IN_ID.getValue()); 
			sql.addSelectClause("p.NAME", DmmRepositoryHistoryDTO.ATTRIBUTE.APP_NAME.getValue()); 
			sql.addSelectClause("p.VERSION", DmmRepositoryHistoryDTO.ATTRIBUTE.APP_VERSION.getValue()); 
			sql.addSelectClause("c.SHORT_NAME", DmmRepositoryHistoryDTO.ATTRIBUTE.SHORT_NAME.getValue()); 
			//客戶驗收日期
			sql.addSelectClause("r.CHECKED_DATE", DmmRepositoryHistoryDTO.ATTRIBUTE.CUSTOMER_APPROVE_DATE.getValue()); 
			sql.addSelectClause("cp.SHORT_NAME", DmmRepositoryHistoryDTO.ATTRIBUTE.ASSET_USER_NAME.getValue()); 
			sql.addSelectClause("co.SHORT_NAME", DmmRepositoryHistoryDTO.ATTRIBUTE.ASSET_OWNER_NAME.getValue()); 

			sql.addSelectClause("r.FACTORY_WARRANTY_DATE", DmmRepositoryHistoryDTO.ATTRIBUTE.FACTORY_WARRANTY_DATE.getValue()); 
			sql.addSelectClause("r.UPDATE_DATE", DmmRepositoryHistoryDTO.ATTRIBUTE.UPDATE_DATE.getValue()); 
			sql.addSelectClause("r.UPDATE_USER_NAME", DmmRepositoryHistoryDTO.ATTRIBUTE.UPDATE_USER_NAME.getValue()); 
			sql.addSelectClause("r.CUSTOMER_WARRANTY_DATE", DmmRepositoryHistoryDTO.ATTRIBUTE.CUSTOMER_WARRANTY_DATE.getValue()); 
			//cyber驗收日期
			sql.addSelectClause("r.CYBER_APPROVED_DATE", DmmRepositoryHistoryDTO.ATTRIBUTE.CHECKED_DATE.getValue()); 
			sql.addSelectClause("r.RETIRE_REASON_CODE", DmmRepositoryHistoryDTO.ATTRIBUTE.RETIRE_REASON_CODE.getValue()); 

			sql.addSelectClause("r.CONTRACT_ID", DmmRepositoryHistoryDTO.ATTRIBUTE.CONTRACT_ID.getValue()); 
			sql.addSelectClause("r.ASSET_IN_TIME", DmmRepositoryHistoryDTO.ATTRIBUTE.ASSET_IN_TIME.getValue()); 
			sql.addSelectClause("r.SIM_ENABLE_DATE", DmmRepositoryHistoryDTO.ATTRIBUTE.SIM_ENABLE_DATE.getValue()); 
			sql.addSelectClause("r.TID", DmmRepositoryHistoryDTO.ATTRIBUTE.TID.getValue()); 
			sql.addSelectClause("comp.SHORT_NAME", DmmRepositoryHistoryDTO.ATTRIBUTE.MAINTAIN_COMPANY.getValue()); 
			sql.addSelectClause("pc.ITEM_NAME", DmmRepositoryHistoryDTO.ATTRIBUTE.ACTION.getValue()); 
			//sql.addSelectClause("r.IS_SIM_ENABLE", DmmRepositoryHistoryDTO.ATTRIBUTE.IS_SIM_ENABLE.getValue()); 
			sql.addSelectClause("r.SIM_ENABLE_NO", DmmRepositoryHistoryDTO.ATTRIBUTE.SIM_ENABLE_NO.getValue()); 
			sql.addSelectClause("com.SHORT_NAME", DmmRepositoryHistoryDTO.ATTRIBUTE.REPAIR_VENDOR.getValue()); 
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append("case when r.STATUS=:repertory or r.STATUS=:repair or r.STATUS=:pendingDisabled or r.STATUS=:disabled or r.STATUS=:destroy then compa.SHORT_NAME + '-' + w.NAME when ");
			fromBuffer.append("r.STATUS=:borrowing then us.CNAME ");
			fromBuffer.append("when r.STATUS=:inApply then adm.CNAME when r.STATUS=:inUse or r.STATUS=:stop then bt.NAME ");
			//已拆回：維護人員
			fromBuffer.append("when r.STATUS=:maintenance then repairVendor.SHORT_NAME when r.STATUS=:RETURNED then maintainUser.CNAME else compa.SHORT_NAME + '-' + w.NAME end ");
			sql.addSelectClause(fromBuffer.toString(), DmmRepositoryHistoryDTO.ATTRIBUTE.BRAND.getValue()); 
			sql.addSelectClause("r.ASSET_IN_ID",DmmRepositoryHistoryDTO.ATTRIBUTE.ASSET_IN_ID.getValue()); 
			sql.addSelectClause("STUFF((SELECT ',' + bpid.ITEM_NAME FROM  " + schema + ".BASE_PARAMETER_ITEM_DEF bpid WHERE bpid.BPTD_CODE=:FAULT_COMPONENT and charindex(bpid.ITEM_VALUE,r.FAULT_COMPONENT) > 0 FOR XML PATH('')) ,1 ,1 ,'')", DmmRepositoryDTO.ATTRIBUTE.FAULT_COMPONENT.getValue()); 
			sql.addSelectClause("STUFF((SELECT ',' + bpids.ITEM_NAME FROM  " + schema + ".BASE_PARAMETER_ITEM_DEF bpids WHERE bpids.BPTD_CODE=:FAULT_DESCRIPTION and charindex(bpids.ITEM_VALUE,r.FAULT_DESCRIPTION) > 0 FOR XML PATH('')) ,1 ,1 ,'')", DmmRepositoryDTO.ATTRIBUTE.FAULT_DESCRIPTION.getValue()); 
			//sql.addSelectClause("r.MAINTAIN_COMPANY", DmmRepositoryHistoryDTO.ATTRIBUTE.MAINTAIN_COMPANY.getValue()); 
			sql.addSelectClause("pid.ITEM_NAME", DmmRepositoryHistoryDTO.ATTRIBUTE.FAULT_COMPONENT.getValue()); 
			sql.addSelectClause("par.ITEM_NAME", DmmRepositoryHistoryDTO.ATTRIBUTE.FAULT_DESCRIPTION.getValue()); 
			//Bug #2644
			sql.addSelectClause("r.IS_CHECKED", DmmRepositoryHistoryDTO.ATTRIBUTE.IS_CHECKED.getValue()); 
			sql.addSelectClause("r.IS_CUSTOMER_CHECKED", DmmRepositoryHistoryDTO.ATTRIBUTE.IS_CUSTOMER_CHECKED.getValue()); 
			sql.addSelectClause("r.REPAIR_VENDOR", DmmRepositoryHistoryDTO.ATTRIBUTE.REPAIR_NENDOR_ID.getValue()); 
			sql.addSelectClause("r.FAULT_COMPONENT", DmmRepositoryHistoryDTO.ATTRIBUTE.FAULT_COMPONENT_ID.getValue()); 
			sql.addSelectClause("r.FAULT_DESCRIPTION", DmmRepositoryHistoryDTO.ATTRIBUTE.FAULT_DESCRIPTION_ID.getValue());
			sql.addSelectClause("bc.CONTRACT_CODE", DmmRepositoryHistoryDTO.ATTRIBUTE.CONTRACT_CODE.getValue()); 
			sql.addSelectClause("r.INSTALLED_ADRESS", DmmRepositoryHistoryDTO.ATTRIBUTE.MER_INSTALL_ADDRESS.getValue()); 
			sql.addSelectClause("installType.ITEM_NAME", DmmRepositoryHistoryDTO.ATTRIBUTE.INSTALL_TYPE.getValue()); 
			//舊資料轉檔所需欄位
			//Task #2588 2017/10/17
			sql.addSelectClause("header.HEADER_NAME", DmmRepositoryHistoryDTO.ATTRIBUTE.HEADER_NAME.getValue());
			sql.addSelectClause("bt.MERCHANT_CODE", DmmRepositoryHistoryDTO.ATTRIBUTE.MERCHANT_CODE.getValue()); 
			sql.addSelectClause("bt.NAME", DmmRepositoryHistoryDTO.ATTRIBUTE.MER_NAME.getValue()); 
			//裝機區域
			sql.addSelectClause("baseparam.ITEM_NAME", DmmRepositoryHistoryDTO.ATTRIBUTE.AREA_NAME.getValue()); 
			sql.addSelectClause("r.INSTALLED_ADRESS_LOCATION", DmmRepositoryHistoryDTO.ATTRIBUTE.AREA.getValue());
			//update by 2017/08/02 Bug #2040 amandawang
			sql.addSelectClause("case when r.DEPARTMENT_ID='CUSTOMER_SERVICE' then '客服' when r.DEPARTMENT_ID='TMS' then 'TMS' when r.DEPARTMENT_ID='QA' then 'QA' else dept.DEPT_NAME end", DmmRepositoryHistoryDTO.ATTRIBUTE.DEPARTMENT_ID.getValue()); 
			sql.addSelectClause("case when r.STATUS=:borrowing then us.CNAME when r.STATUS=:inApply then adm.CNAME else '' end ", DmmRepositoryHistoryDTO.ATTRIBUTE.CARRY.getValue()); 
			sql.addSelectClause("r.BRAND", DmmRepositoryHistoryDTO.ATTRIBUTE.ASSET_BRAND.getValue());
			//維護人員
			sql.addSelectClause("maintainUser.CNAME", DmmRepositoryHistoryDTO.ATTRIBUTE.VENDOR_STAFF.getValue());
			sql.addSelectClause("r.ANALYZE_DATE", DmmRepositoryHistoryDTO.ATTRIBUTE.ANALYZE_DATE.getValue());
			sql.addSelectClause("r.UNINSTALL_OR_REPAIR_REASON", DmmRepositoryHistoryDTO.ATTRIBUTE.PROBLEM_REASON.getValue());
			sql.addSelectClause("r.COUNTER", DmmRepositoryHistoryDTO.ATTRIBUTE.COUNTER.getValue());
			sql.addSelectClause("r.CARTON_NO", DmmRepositoryHistoryDTO.ATTRIBUTE.CARTON_NO.getValue());
			sql.addSelectClause("cp.COMPANY_CODE", DmmRepositoryHistoryDTO.ATTRIBUTE.COMPANY_CODE.getValue());
			sql.addSelectClause("a.ASSET_CATEGORY", DmmRepositoryHistoryDTO.ATTRIBUTE.ASSET_CATEGORY.getValue());
			sql.addSelectClause("r.ACTION", DmmRepositoryHistoryDTO.ATTRIBUTE.ACTION_VALUE.getValue()); 
			//Task #2675 案件編號，若沒有對應案件可檢視，不要提供超連結 2017/10/23
			if (IAtomsConstants.PARAM_YES.equals(assetManageFormDTO.getQueryCaseFlag())) {
				sql.addSelectClause("case when r.CASE_ID is not null and r.CASE_ID <>'' and exists (select 1 from dbo.SRM_CASE_HANDLE_INFO s where s.CASE_ID =r.CASE_ID) then (case when exists (select 1 from dbo.SRM_CASE_HANDLE_INFO info where info.CASE_ID =r.CASE_ID and info.CMS_CASE='Y') then 'C' else 'B' end) else 'N' end ", DmmRepositoryHistoryDTO.ATTRIBUTE.QUERY_CASE_FLAG.getValue());
			}
			fromBuffer = new StringBuffer();
			fromBuffer.append(schema);
			fromBuffer.append(".DMM_REPOSITORY_HISTORY r left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".DMM_ASSET_TYPE a on r.ASSET_TYPE_ID=a.ASSET_TYPE_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BASE_PARAMETER_ITEM_DEF b on r.STATUS=b.ITEM_VALUE AND b.BPTD_CODE =:assetStatus left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_COMPANY c on c.COMPANY_ID=r.MAINTAIN_COMPANY left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_CONTRACT bc on bc.CONTRACT_ID=r.CONTRACT_ID ");
			fromBuffer.append(" left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BASE_PARAMETER_ITEM_DEF pp ON r.MA_TYPE = pp.ITEM_VALUE AND pp.BPTD_CODE =:maType left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_WAREHOUSE w on r.WAREHOUSE_ID=w.WAREHOUSE_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_COMPANY co on r.ASSET_OWNER=co.COMPANY_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_COMPANY compa on w.COMPANY_ID=compa.COMPANY_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_COMPANY  cp on r.ASSET_USER=cp.COMPANY_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_MERCHANT bt on r.MERCHANT_ID = bt.MERCHANT_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_MERCHANT_HEADER header on header.MERCHANT_HEADER_ID = r.MERCHANT_HEADER_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".PVM_APPLICATION p on r.APPLICATION_ID = p.APPLICATION_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BASE_PARAMETER_ITEM_DEF pc ON (substring(r.ACTION,charindex('-',r.ACTION)+1,LEN(r.ACTION))) = pc.ITEM_VALUE AND pc.BPTD_CODE = :ACTION left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BASE_PARAMETER_ITEM_DEF pid ON r.FAULT_COMPONENT = pid.ITEM_VALUE AND pid.BPTD_CODE = :FAULT_COMPONENT left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BASE_PARAMETER_ITEM_DEF par ON r.FAULT_DESCRIPTION = par.ITEM_VALUE AND par.BPTD_CODE = :FAULT_DESCRIPTION LEFT JOIN ");
			//fromBuffer.append(schema);
			//fromBuffer.append(".DMM_ASSET_IN_LIST al on r.SERIAL_NUMBER = al.SERIAL_NUMBER left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_COMPANY  com on r.REPAIR_VENDOR=com.COMPANY_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_COMPANY  comp on r.MAINTAIN_COMPANY=comp.COMPANY_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".ADM_USER us on us.USER_ID=r.BORROWER left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".ADM_USER adm on adm.USER_ID=r.CARRIER left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".ADM_USER  maintainUser on maintainUser.USER_ID=r.MAINTAIN_USER left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_DEPARTMENT dept on dept.DEPT_CODE=r.DEPARTMENT_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BASE_PARAMETER_ITEM_DEF baseparam on r.INSTALLED_ADRESS_LOCATION=baseparam.ITEM_VALUE and baseparam.BPTD_CODE =:headerArea left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BASE_PARAMETER_ITEM_DEF installType on r.INSTALL_TYPE=installType.ITEM_VALUE and installType.BPTD_CODE =:INSTALL_TYPE left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_COMPANY repairVendor on r.REPAIR_COMPANY=repairVendor.COMPANY_ID ");

			if (StringUtils.hasText(assetManageFormDTO.getUserId())) {
				fromBuffer.append(" left join ");
				fromBuffer.append(schema).append(".ADM_USER user1 on user1.USER_ID =:userId ");
			}
			sql.setFromExpression(fromBuffer.toString());
			if (assetManageFormDTO.getRows() != null) {
				sql.setPageSize(assetManageFormDTO.getRows());
				sql.setStartPage(assetManageFormDTO.getPage() - 1);
			}
			StringBuffer buffer = new StringBuffer();
			
			if (StringUtils.hasText(assetManageFormDTO.getUserId())) {
				sql.addWhereClause(" ( exists (select 1 from " + schema + ".ADM_USER_WAREHOUSE uWarehouse where uWarehouse.USER_ID = user1.USER_ID and uWarehouse.WAREHOUSE_ID = r.WAREHOUSE_ID))");
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryHistId())) {
				sql.addWhereClause("r.HISTORY_ID =:histId", assetManageFormDTO.getQueryHistId());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryUser())) {
				sql.addWhereClause("bc.COMPANY_ID =:queryUser", assetManageFormDTO.getQueryUser());
			}
			//Task #2991
			if (StringUtils.hasText(assetManageFormDTO.getQueryAction())) {
				String actions=assetManageFormDTO.getQueryAction();
				String [] action = actions.split(",");
				StringBuffer serialBuffer = new StringBuffer();
				for (int i = 0; i < action.length - 1; i++) {
					if (StringUtils.hasText(action[i])) {
						serialBuffer.append("'" + action[i] + "',");
					}
				}
				serialBuffer.append("'" + action[action.length - 1] + "'");
				serialBuffer.append(" ) ");
				sql.addWhereClause("r.ACTION in(" + serialBuffer);
				//sql.addWhereClause("r.ACTION =:action", assetManageFormDTO.getQueryAction());
			}
			if (StringUtils.hasText(assetManageFormDTO.getAssetTypeName())) {
				String assetTypeIds=assetManageFormDTO.getAssetTypeName();
				String [] assetTypeId = assetTypeIds.split(",");
				StringBuffer serialBuffer = new StringBuffer();
				for (int i = 0; i < assetTypeId.length - 1; i++) {
					if (StringUtils.hasText(assetTypeId[i])) {
						serialBuffer.append("'" + assetTypeId[i] + "',");
					}
				}
				serialBuffer.append("'" + assetTypeId[assetTypeId.length - 1] + "'");
				serialBuffer.append(" ) ");
				sql.addWhereClause("r.ASSET_TYPE_ID in(" + serialBuffer);
				//sql.addWhereClause("r.ASSET_TYPE_ID =:name", assetManageFormDTO.getAssetTypeName());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetUser())) {
				String assetUsers=assetManageFormDTO.getQueryAssetUser();
				String [] assetUser = assetUsers.split(",");
				StringBuffer serialBuffer = new StringBuffer();
				for (int i = 0; i < assetUser.length - 1; i++) {
					if (StringUtils.hasText(assetUser[i])) {
						serialBuffer.append("'" + assetUser[i] + "',");
					}
				}
				serialBuffer.append("'" + assetUser[assetUser.length - 1] + "'");
				serialBuffer.append(" ) ");
				sql.addWhereClause("r.ASSET_USER in(" + serialBuffer);
				//sql.addWhereClause("r.ASSET_USER = :assetUser", assetManageFormDTO.getQueryAssetUser());
			} else {
				//Task #3046
				if (StringUtils.hasText(assetManageFormDTO.getHideQueryAssetUser())) {
					sql.addWhereClause("r.ASSET_USER = :assetUser", assetManageFormDTO.getHideQueryAssetUser());
				}
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetCategory())) {
				sql.addWhereClause("a.ASSET_CATEGORY =:queryAssetCategory", assetManageFormDTO.getQueryAssetCategory());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryStatus())) {
				sql.addWhereClause("r.STATUS =:status", assetManageFormDTO.getQueryStatus());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryStorage())) {
				sql.addWhereClause("r.WAREHOUSE_ID =:warehouseId", assetManageFormDTO.getQueryStorage());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetId())) {
				sql.addWhereClause("r.ASSET_ID = :assetId", assetManageFormDTO.getQueryAssetId());
			}
			//櫃位
			if (StringUtils.hasText(assetManageFormDTO.getQueryCounter())) {
				sql.addWhereClause("r.COUNTER like :counter", assetManageFormDTO.getQueryCounter() + IAtomsConstants.MARK_PERCENT);
			}
			//箱號
			if (StringUtils.hasText(assetManageFormDTO.getQueryCartonNo())) {
				sql.addWhereClause("r.CARTON_NO like :cartonNo", assetManageFormDTO.getQueryCartonNo() + IAtomsConstants.MARK_PERCENT);
			}
			//特店名稱
			if (StringUtils.hasText(assetManageFormDTO.getQueryMerName())) {
				sql.addWhereClause("bt.NAME like :merRegisteredName", assetManageFormDTO.getQueryMerName() + IAtomsConstants.MARK_PERCENT);
			}
			//特店表頭
			if (StringUtils.hasText(assetManageFormDTO.getQueryHeaderName())) {
				sql.addWhereClause("header.HEADER_NAME like :merAnnouncedName", assetManageFormDTO.getQueryHeaderName() + IAtomsConstants.MARK_PERCENT);
			}
			//特店代號
			if (StringUtils.hasText(assetManageFormDTO.getQueryMerchantCode())) {
				sql.addWhereClause("bt.MERCHANT_CODE like :mid", assetManageFormDTO.getQueryMerchantCode() + IAtomsConstants.MARK_PERCENT);
			} 
			//裝機地址
			if (StringUtils.hasText(assetManageFormDTO.getQueryMerInstallAddress())) {
				sql.addWhereClause("r.INSTALLED_ADRESS like :merInstallAddress", assetManageFormDTO.getQueryMerInstallAddress() + IAtomsConstants.MARK_PERCENT);
			}
			//裝機類別
			if (StringUtils.hasText(assetManageFormDTO.getQueryMerInstallAddress())) {
				sql.addWhereClause("r.INSTALL_TYPE like :merInstallAddress", assetManageFormDTO.getQueryMerInstallAddress() + IAtomsConstants.MARK_PERCENT);
			}
			//裝機區域
			if (StringUtils.hasText(assetManageFormDTO.getQueryArea())) {
				sql.addWhereClause("r.INSTALLED_ADRESS_LOCATION like :area", assetManageFormDTO.getQueryArea() + IAtomsConstants.MARK_PERCENT);
			}
			
			if (StringUtils.hasText(assetManageFormDTO.getQueryContractId())) {
				sql.addWhereClause("r.CONTRACT_ID = :contractId", assetManageFormDTO.getQueryContractId());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryMaType())) {
				sql.addWhereClause("r.MA_TYPE = :queryMaType", assetManageFormDTO.getQueryMaType());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryIsEnabled())) {
				sql.addWhereClause("r.IS_ENABLED = :isEnabled", assetManageFormDTO.getQueryIsEnabled());
			}
			//保管人
			if (StringUtils.hasText(assetManageFormDTO.getQueryKeeperName())) {
				sql.addWhereClause("r.KEEPER_NAME like :keeperName", assetManageFormDTO.getQueryKeeperName() + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryTid())) {
				sql.addWhereClause("r.TID like :tid", assetManageFormDTO.getQueryTid() + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetOwner())) {
				sql.addWhereClause("r.ASSET_OWNER = :assetOwner", assetManageFormDTO.getQueryAssetOwner());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryDtid())) {
				sql.addWhereClause("r.DTID like :dtid", assetManageFormDTO.getQueryDtid() + IAtomsConstants.MARK_PERCENT);
			}
			String propertyIdCodeGun = null;
			if (StringUtils.hasText(assetManageFormDTO.getQueryPropertyIds())) {
				String propertyIds=assetManageFormDTO.getQueryPropertyIds();
				// 若輸入中文逗號替換為英文逗號
				propertyIds = propertyIds.replaceAll("，",",");
				buffer = new StringBuffer();
				String [] propertyId = propertyIds.split(",");
				buffer.append(" r.PROPERTY_ID in(");
				propertyIdCodeGun = "'";
				for (int i = 0; i < propertyId.length - 1; i++) {
					if (StringUtils.hasText(propertyId[i])) {
						buffer.append("'" + propertyId[i] + "',");
						propertyIdCodeGun = propertyIdCodeGun + ("*" + propertyId[i] + "*,");
					}
				}
				buffer.append("'" + propertyId[propertyId.length - 1] + "'");
				propertyIdCodeGun = propertyIdCodeGun + "*" + propertyId[propertyId.length - 1] + "*')";
				buffer.append(" ) ");
				sql.addWhereClause(buffer.toString());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetIds())) {
				String assetIds=assetManageFormDTO.getQueryAssetIds();
				// 若輸入中文逗號替換為英文逗號
				assetIds = assetIds.replaceAll("，",",");
				buffer = new StringBuffer();
				String [] assetId = assetIds.split(",");
				buffer.append(" r.ASSET_ID in(");
				for (int i = 0; i < assetId.length - 1; i++) {
					if (StringUtils.hasText(assetId[i])) {
						buffer.append("'" + assetId[i] + "',");
					}
				}
				buffer.append("'" + assetId[assetId.length - 1] + "'");
				buffer.append(" ) ");
				sql.addWhereClause(buffer.toString());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetIdList())) {
				String assetIds=assetManageFormDTO.getQueryAssetIdList();
				// 若輸入中文逗號替換為英文逗號
				assetIds = assetIds.replaceAll("，",",");
				buffer = new StringBuffer();
				String [] assetId = assetIds.split(",");
				buffer.append("r.ASSET_ID in(");
				for (int i = 0; i < assetId.length - 1; i++) {
					if (StringUtils.hasText(assetId[i])) {
						buffer.append("'" + assetId[i] + "',");
					}
				}
				buffer.append("'" + assetId[assetId.length - 1] + "'");
				buffer.append(" ) ");
				sql.addWhereClause(buffer.toString());
			}
			String serialNumberCodeGun = null;
			if (StringUtils.hasText(assetManageFormDTO.getQuerySerialNumbers())) {
				String serialNumbers=assetManageFormDTO.getQuerySerialNumbers();
				// 若輸入中文逗號替換為英文逗號
				serialNumbers = serialNumbers.replaceAll("，",","); 
				String [] serialNumber = serialNumbers.split(",");
				buffer = new StringBuffer();
				buffer.append("r.SERIAL_NUMBER in(");
				serialNumberCodeGun = "'";
				for (int i = 0; i < serialNumber.length - 1; i++) {
					if (StringUtils.hasText(serialNumber[i])) {
						buffer.append("'" + serialNumber[i] + "',");
						serialNumberCodeGun = serialNumberCodeGun + ("*" + serialNumber[i] + "*,");
					}
				}
				buffer.append("'" + serialNumber[serialNumber.length - 1] + "'");
				serialNumberCodeGun = serialNumberCodeGun + "*" + serialNumber[serialNumber.length - 1] + "*')";
				buffer.append(" ) ");
				sql.addWhereClause(buffer.toString());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryIsCup())) {
				sql.addWhereClause("r.IS_CUP =:queryIsCup", assetManageFormDTO.getQueryIsCup());
			}

			if (StringUtils.hasText(assetManageFormDTO.getBeforeTicketCompletionDate())) {
				sql.addWhereClause("CONVERT(varchar(10),r.CASE_COMPLETION_DATE,111)>=:beforeTicketCompletionDate", assetManageFormDTO.getBeforeTicketCompletionDate());
			}
			if (StringUtils.hasText(assetManageFormDTO.getAfterTicketCompletionDate())) {
				sql.addWhereClause("CONVERT(varchar(10),r.CASE_COMPLETION_DATE,111)<=:afterTicketCompletionDate", assetManageFormDTO.getAfterTicketCompletionDate());
			}
			if (StringUtils.hasText(assetManageFormDTO.getBeforeUpdateDate())) {
				sql.addWhereClause("CONVERT(varchar(10),r.UPDATE_DATE,111)>=:beforeUpdateDate", assetManageFormDTO.getBeforeUpdateDate());
			} 
			if (StringUtils.hasText(assetManageFormDTO.getAfterUpdateDate())) {
				sql.addWhereClause("CONVERT(varchar(10),r.UPDATE_DATE,111)<=:afterUpdateDate", assetManageFormDTO.getAfterUpdateDate());
			}
			//Task #3127
			if (StringUtils.hasText(assetManageFormDTO.getQueryModel())) {
				sql.addWhereClause("r.ASSET_MODEL =:queryModel", assetManageFormDTO.getQueryModel());
			}
			//
			if (DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue().equals(assetManageFormDTO.getCodeGunFlag())) {
				sql.setOrderByExpression("CHARINDEX('*'+Rtrim(cast(r.SERIAL_NUMBER as varchar(20))+'*'), " + " " + serialNumberCodeGun+", updateDate desc");
			} else if (DmmRepositoryDTO.ATTRIBUTE.PROPERTY_ID.getValue().equals(assetManageFormDTO.getCodeGunFlag())) {
				sql.setOrderByExpression("CHARINDEX('*'+Rtrim(cast(r.PROPERTY_ID as varchar(20))+'*'), " + " " + propertyIdCodeGun);
			} else if ((!StringUtils.hasText(assetManageFormDTO.getSort())) && (!StringUtils.hasText(assetManageFormDTO.getOrder()))) {
				if (StringUtils.hasText(assetManageFormDTO.getQueryHistory())) {
					sql.setOrderByExpression("updateDate desc, historyId asc ");
				} else {
					sql.setOrderByExpression("serialNumber asc, updateDate desc, historyId asc ");
				}
			} else {
				sql.setOrderByExpression(assetManageFormDTO.getSort() + " " + assetManageFormDTO.getOrder());
			}
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			sqlQueryBean.setParameter("assetStatus", IAtomsConstants.PARAM_ASSET_STATUS);
			sqlQueryBean.setParameter("maType", IAtomsConstants.PARAM_MA_TYPE);
			sqlQueryBean.setParameter("repertory", IAtomsConstants.PARAM_ASSET_STATUS_REPERTORY);
			sqlQueryBean.setParameter("repair", IAtomsConstants.PARAM_ASSET_STATUS_REPAIR);
			sqlQueryBean.setParameter("disabled", IAtomsConstants.PARAM_ASSET_STATUS_DISABLED);
			sqlQueryBean.setParameter("destroy", IAtomsConstants.PARAM_ASSET_STATUS_DESTROY);
			sqlQueryBean.setParameter("borrowing", IAtomsConstants.PARAM_ASSET_STATUS_BORROWING);
			sqlQueryBean.setParameter("pendingDisabled", IAtomsConstants.PARAM_ASSET_STATUS_PENDING_DISABLED);
			if (StringUtils.hasText(assetManageFormDTO.getUserId())) {
				sqlQueryBean.setParameter("userId", assetManageFormDTO.getUserId());
			}
			sqlQueryBean.setParameter("inApply", IAtomsConstants.PARAM_ASSET_STATUS_IN_APPLY);
			sqlQueryBean.setParameter("inUse", IAtomsConstants.PARAM_ASSET_STATUS_IN_USE);
			sqlQueryBean.setParameter("maintenance", IAtomsConstants.PARAM_ASSET_STATUS_MAINTENANCE);
			//Task #3509 設備狀態=停用中，位置是 特店
			sqlQueryBean.setParameter("stop", IAtomsConstants.PARAM_ASSET_STATUS_STOP);
			sqlQueryBean.setParameter("FAULT_COMPONENT", IATOMS_PARAM_TYPE.FAULT_COMPONENT.getCode());
			sqlQueryBean.setParameter("FAULT_DESCRIPTION", IATOMS_PARAM_TYPE.FAULT_DESCRIPTION.getCode());
			sqlQueryBean.setParameter("ACTION", IATOMS_PARAM_TYPE.ACTION.getCode());
			//sqlQueryBean.setParameter("PROBLEM_REASON_CATEGORY", IATOMS_PARAM_TYPE.PROBLEM_REASON_CATEGORY.getCode());
			sqlQueryBean.setParameter("headerArea", IATOMS_PARAM_TYPE.LOCATION.getCode());
			sqlQueryBean.setParameter("INSTALL_TYPE", IATOMS_PARAM_TYPE.INSTALL_TYPE.getCode());
			
			sqlQueryBean.setParameter("RETURNED", IAtomsConstants.PARAM_ASSET_STATUS_RETURNED);

			LOGGER.debug("DmmRepositoryDAO.getListByAssetId()", "sql:" + sql.toString());
			AliasBean aliasBean = new AliasBean(DmmRepositoryHistoryDTO.class);
			
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.STATUS.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.ITEM_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.ITEM_VALUE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.ASSET_ID.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.SHORT_NAME.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.ASSET_IN_TIME.getValue(), TimestampType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.ASSET_USER.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.ASSET_OWNER.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.CUSTOMER_WARRANTY_DATE.getValue(), DateType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.MODEL.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.ASSET_OWNER_NAME.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.ASSET_USER_NAME.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.CUSTOMER_APPROVE_DATE.getValue(), DateType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.PROPERTY_ID.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.CONTRACT_ID.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.DESCRIPTION.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.BORROWER.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.BORROWER_START.getValue(), TimestampType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.BORROWER_END.getValue(), TimestampType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.BACK_DATE.getValue(), TimestampType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.IS_CUP.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.CHECKED_DATE.getValue(), DateType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.FAULT_COMPONENT.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.FAULT_DESCRIPTION.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.RETIRE_REASON_CODE.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.NAME.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.MA_TYPE.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.IS_ENABLED.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.ACTION.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.CARRIER.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.ENABLE_DATE.getValue(), TimestampType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.CASE_COMPLETION_DATE.getValue(), TimestampType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.APP_NAME.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.APP_VERSION.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.FACTORY_WARRANTY_DATE.getValue(), DateType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.UPDATE_DATE.getValue(), TimestampType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.UPDATE_USER_NAME.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.CREATE_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.CREATE_USER_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.PROPERTY_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.SIM_ENABLE_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.CASE_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.TID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.ASSET_IN_TIME.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.MER_NAME.getValue(), StringType.INSTANCE);
			//aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.IS_SIM_ENABLE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.SIM_ENABLE_NO.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.HEADER_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.REPAIR_VENDOR.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.ASSET_IN_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.MAINTAIN_COMPANY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.FAULT_COMPONENT.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.FAULT_DESCRIPTION.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.IS_CHECKED.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.IS_CUSTOMER_CHECKED.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.HISTORY_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.REPAIR_NENDOR_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.FAULT_COMPONENT_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.FAULT_DESCRIPTION_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.CONTRACT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.MER_INSTALL_ADDRESS.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.HEADER_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.AREA.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.FAULT_COMPONENT.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.FAULT_DESCRIPTION.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.ANALYZE_DATE.getValue(), DateType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.DEPARTMENT_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.VENDOR_STAFF.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.PROBLEM_REASON.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.BRAND.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.CARRY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.ASSET_BRAND.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.BORROWER_COMMENT.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.AREA_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.INSTALL_TYPE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.COUNTER.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.CARTON_NO.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.COMPANY_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.ACTION_VALUE.getValue(), StringType.INSTANCE);
			if (IAtomsConstants.PARAM_YES.equals(assetManageFormDTO.getQueryCaseFlag())) {
				aliasBean.addScalar(DmmRepositoryHistoryDTO.ATTRIBUTE.QUERY_CASE_FLAG.getValue(), StringType.INSTANCE);
			}

			LOGGER.debug("DmmRepositoryDAO.getListByAssetId()", "sql:" + sqlQueryBean.toString());
			dmmRepositoryHistoryDTOs = (List<DmmRepositoryHistoryDTO>) super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return dmmRepositoryHistoryDTOs;
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ":Failed-- getListByAssetId()---" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY)}, e);
		}
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#getCountByAssetId(com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetManageFormDTO)
	 */
	@Override
	public Integer getCountByAssetId(AssetManageFormDTO assetManageFormDTO)
			throws DataAccessException {
		try {
			String schema = this.getMySchema();
			SqlStatement sql = new SqlStatement();
			StringBuffer fromBuffer = new StringBuffer();
			sql.addSelectClause("count(1)");			
			fromBuffer.append(schema);
			fromBuffer.append(".DMM_REPOSITORY_HISTORY r left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".DMM_ASSET_TYPE a on r.ASSET_TYPE_ID=a.ASSET_TYPE_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_MERCHANT bt on r.MERCHANT_ID = bt.MERCHANT_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_MERCHANT_HEADER header on header.MERCHANT_HEADER_ID = r.MERCHANT_HEADER_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_CONTRACT bc on bc.CONTRACT_ID=r.CONTRACT_ID  ");
			if (StringUtils.hasText(assetManageFormDTO.getUserId())) {
				fromBuffer.append(" left join ");
				fromBuffer.append(schema).append(".ADM_USER user1 on user1.USER_ID =:userId ");
			}
			sql.addFromExpression(fromBuffer.toString());
			
			if (StringUtils.hasText(assetManageFormDTO.getQuerySerialNumbers())) {
				String serialNumbers=assetManageFormDTO.getQuerySerialNumbers();
				// 若輸入中文逗號替換為英文逗號
				serialNumbers.replaceAll("，",","); 
				String [] serialNumber = serialNumbers.split(",");
				StringBuffer serialBuffer = new StringBuffer();
				for (int i = 0; i < serialNumber.length - 1; i++) {
					if (StringUtils.hasText(serialNumber[i])) {
						serialBuffer.append("'" + serialNumber[i] + "',");
					}
				}
				serialBuffer.append("'" + serialNumber[serialNumber.length - 1] + "'");
				serialBuffer.append(" ) ");
				sql.addWhereClause("r.SERIAL_NUMBER in(" + serialBuffer);
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryDtid())) {
				sql.addWhereClause("r.DTID like :dtid", assetManageFormDTO.getQueryDtid() + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryContractId())) {
				sql.addWhereClause("r.CONTRACT_ID = :contractId", assetManageFormDTO.getQueryContractId());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryStatus())) {
				sql.addWhereClause("r.STATUS =:status", assetManageFormDTO.getQueryStatus());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryHistId())) {
				sql.addWhereClause("r.HISTORY_ID =:histId", assetManageFormDTO.getQueryHistId());
			}
			if (StringUtils.hasText(assetManageFormDTO.getUserId())) {
				sql.addWhereClause("(exists (select 1 from " + schema + ".ADM_USER_WAREHOUSE uWarehouse where uWarehouse.USER_ID = user1.USER_ID and uWarehouse.WAREHOUSE_ID = r.WAREHOUSE_ID))");
			}
			//Task #2991
			if (StringUtils.hasText(assetManageFormDTO.getAssetTypeName())) {
				String assetTypeIds=assetManageFormDTO.getAssetTypeName();
				String [] assetTypeId = assetTypeIds.split(",");
				StringBuffer serialBuffer = new StringBuffer();
				for (int i = 0; i < assetTypeId.length - 1; i++) {
					if (StringUtils.hasText(assetTypeId[i])) {
						serialBuffer.append("'" + assetTypeId[i] + "',");
					}
				}
				serialBuffer.append("'" + assetTypeId[assetTypeId.length - 1] + "'");
				serialBuffer.append(" ) ");
				sql.addWhereClause("r.ASSET_TYPE_ID in(" + serialBuffer);
				//sql.addWhereClause("r.ASSET_TYPE_ID =:name", assetManageFormDTO.getAssetTypeName());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetUser())) {
				String assetUsers=assetManageFormDTO.getQueryAssetUser();
				String [] assetUser = assetUsers.split(",");
				StringBuffer serialBuffer = new StringBuffer();
				for (int i = 0; i < assetUser.length - 1; i++) {
					if (StringUtils.hasText(assetUser[i])) {
						serialBuffer.append("'" + assetUser[i] + "',");
					}
				}
				serialBuffer.append("'" + assetUser[assetUser.length - 1] + "'");
				serialBuffer.append(" ) ");
				sql.addWhereClause("r.ASSET_USER in(" + serialBuffer);
				//sql.addWhereClause("r.ASSET_USER = :assetUser", assetManageFormDTO.getQueryAssetUser());
			} else {
				//Task #3046
				if (StringUtils.hasText(assetManageFormDTO.getHideQueryAssetUser())) {
					sql.addWhereClause("r.ASSET_USER = :assetUser", assetManageFormDTO.getHideQueryAssetUser());
				}
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryAction())) {
				String actions=assetManageFormDTO.getQueryAction();
				String [] action = actions.split(",");
				StringBuffer serialBuffer = new StringBuffer();
				for (int i = 0; i < action.length - 1; i++) {
					if (StringUtils.hasText(action[i])) {
						serialBuffer.append("'" + action[i] + "',");
					}
				}
				serialBuffer.append("'" + action[action.length - 1] + "'");
				serialBuffer.append(" ) ");
				sql.addWhereClause("r.ACTION in(" + serialBuffer);
				//sql.addWhereClause("r.ACTION =:action", assetManageFormDTO.getQueryAction());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryUser())) {
				sql.addWhereClause("bc.COMPANY_ID =:queryUser", assetManageFormDTO.getQueryUser());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetCategory())) {
				sql.addWhereClause("a.ASSET_CATEGORY =:queryAssetCategory", assetManageFormDTO.getQueryAssetCategory());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryStorage())) {
				sql.addWhereClause("r.WAREHOUSE_ID =:warehouseId", assetManageFormDTO.getQueryStorage());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetId())) {
				sql.addWhereClause("r.ASSET_ID = :assetId", assetManageFormDTO.getQueryAssetId());
			}
			//櫃位
			if (StringUtils.hasText(assetManageFormDTO.getQueryCounter())) {
				sql.addWhereClause("r.COUNTER like :counter", assetManageFormDTO.getQueryCounter() + IAtomsConstants.MARK_PERCENT);
			}
			//箱號
			if (StringUtils.hasText(assetManageFormDTO.getQueryCartonNo())) {
				sql.addWhereClause("r.CARTON_NO like :cartonNo", assetManageFormDTO.getQueryCartonNo() + IAtomsConstants.MARK_PERCENT);
			}
			//特店名稱
			if (StringUtils.hasText(assetManageFormDTO.getQueryMerName())) {
				sql.addWhereClause("bt.NAME like :merRegisteredName", assetManageFormDTO.getQueryMerName() + IAtomsConstants.MARK_PERCENT);
			}
			//特店表頭
			if (StringUtils.hasText(assetManageFormDTO.getQueryHeaderName())) {
				sql.addWhereClause("header.HEADER_NAME like :merAnnouncedName", assetManageFormDTO.getQueryHeaderName() + IAtomsConstants.MARK_PERCENT);
			}
			//特店代號
			if (StringUtils.hasText(assetManageFormDTO.getQueryMerchantCode())) {
				sql.addWhereClause("bt.MERCHANT_CODE like :mid", assetManageFormDTO.getQueryMerchantCode() + IAtomsConstants.MARK_PERCENT);
			} 
			//裝機地址
			if (StringUtils.hasText(assetManageFormDTO.getQueryMerInstallAddress())) {
				sql.addWhereClause("r.INSTALLED_ADRESS like :merInstallAddress", assetManageFormDTO.getQueryMerInstallAddress() + IAtomsConstants.MARK_PERCENT);
			}
			//裝機類別
			if (StringUtils.hasText(assetManageFormDTO.getQueryMerInstallAddress())) {
				sql.addWhereClause("r.INSTALL_TYPE like :merInstallAddress", assetManageFormDTO.getQueryMerInstallAddress() + IAtomsConstants.MARK_PERCENT);
			}
			//裝機區域
			if (StringUtils.hasText(assetManageFormDTO.getQueryArea())) {
				sql.addWhereClause("r.INSTALLED_ADRESS_LOCATION like :area", assetManageFormDTO.getQueryArea() + IAtomsConstants.MARK_PERCENT);
			}
			
			if (StringUtils.hasText(assetManageFormDTO.getQueryMaType())) {
				sql.addWhereClause("r.MA_TYPE = :queryMaType", assetManageFormDTO.getQueryMaType());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryIsEnabled())) {
				sql.addWhereClause("r.IS_ENABLED = :isEnabled", assetManageFormDTO.getQueryIsEnabled());
			}
			//保管人
			if (StringUtils.hasText(assetManageFormDTO.getQueryKeeperName())) {
				sql.addWhereClause("r.KEEPER_NAME like :keeperName", assetManageFormDTO.getQueryKeeperName() + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryTid())) {
				sql.addWhereClause("r.TID like :tid", assetManageFormDTO.getQueryTid() + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetOwner())) {
				sql.addWhereClause("r.ASSET_OWNER = :assetOwner", assetManageFormDTO.getQueryAssetOwner());
			}			
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetIds())) {
				String assetIds=assetManageFormDTO.getQueryAssetIds();
				// 若輸入中文逗號替換為英文逗號
				assetIds.replaceAll("，", ",");
				String [] assetIdList = assetIds.split(",");
				StringBuffer assetBuffer = new StringBuffer();
				for (int i = 0; i < assetIdList.length - 1; i++) {
					if (StringUtils.hasText(assetIdList[i])) {
						assetBuffer.append("'" + assetIdList[i] + "',");
					}
				}
				assetBuffer.append("'" + assetIdList[assetIdList.length - 1] + "'");
				assetBuffer.append(" ) ");
				sql.addWhereClause("r.ASSET_ID in(" + assetBuffer);
				//sql.addWhereClause("r.ASSET_ID in(:assetIds)", StringUtils.toList(assetIds, IAtomsConstants.MARK_SEPARATOR));
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetIdList())) {
				String assetIds=assetManageFormDTO.getQueryAssetIdList();
				// 若輸入中文逗號替換為英文逗號
				assetIds.replaceAll("，",",");
				String [] assetIdList = assetIds.split(",");
				StringBuffer assetBuffer = new StringBuffer();
				for (int i = 0; i < assetIdList.length - 1; i++) {
					if (StringUtils.hasText(assetIdList[i])) {
						assetBuffer.append("'" + assetIdList[i] + "',");
					}
				}
				assetBuffer.append("'" + assetIdList[assetIdList.length - 1] + "'");
				assetBuffer.append(" ) ");
				sql.addWhereClause("r.ASSET_ID in(" + assetBuffer);
				//sql.addWhereClause("r.ASSET_ID in(:assetIdList)", StringUtils.toList(assetIds, IAtomsConstants.MARK_SEPARATOR));
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryPropertyIds())) {
				String propertyIds=assetManageFormDTO.getQueryPropertyIds();
				// 若輸入中文逗號替換為英文逗號
				propertyIds.replaceAll("，",",");
				String [] propertyId = propertyIds.split(",");
				StringBuffer propertyBuffer = new StringBuffer();
				for (int i = 0; i < propertyId.length - 1; i++) {
					if (StringUtils.hasText(propertyId[i])) {
						propertyBuffer.append("'" + propertyId[i] + "',");
					}
				}
				propertyBuffer.append("'" + propertyId[propertyId.length - 1] + "'");
				propertyBuffer.append(" ) ");
				sql.addWhereClause("r.PROPERTY_ID in(" + propertyBuffer);
			}
			
			if (StringUtils.hasText(assetManageFormDTO.getQueryIsCup())) {
				sql.addWhereClause("r.IS_CUP =:queryIsCup", assetManageFormDTO.getQueryIsCup());
			}
			if (StringUtils.hasText(assetManageFormDTO.getBeforeTicketCompletionDate())) {
				sql.addWhereClause("CONVERT(varchar(10),r.CASE_COMPLETION_DATE,111)>=:beforeTicketCompletionDate", assetManageFormDTO.getBeforeTicketCompletionDate());
			}
			if (StringUtils.hasText(assetManageFormDTO.getAfterTicketCompletionDate())) {
				sql.addWhereClause("CONVERT(varchar(10),r.CASE_COMPLETION_DATE,111)<=:afterTicketCompletionDate", assetManageFormDTO.getAfterTicketCompletionDate());
			}
			if (StringUtils.hasText(assetManageFormDTO.getBeforeUpdateDate())) {
				sql.addWhereClause("CONVERT(varchar(10),r.UPDATE_DATE,111)>=:beforeUpdateDate", assetManageFormDTO.getBeforeUpdateDate());
			} 
			if (StringUtils.hasText(assetManageFormDTO.getAfterUpdateDate())) {
				sql.addWhereClause("CONVERT(varchar(10),r.UPDATE_DATE,111)<=:afterUpdateDate", assetManageFormDTO.getAfterUpdateDate());
			}
			//Task #3127
			if (StringUtils.hasText(assetManageFormDTO.getQueryModel())) {
				sql.addWhereClause("r.ASSET_MODEL =:queryModel", assetManageFormDTO.getQueryModel());
			}
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			if (StringUtils.hasText(assetManageFormDTO.getUserId())) {
				sqlQueryBean.setParameter("userId", assetManageFormDTO.getUserId());
			}		
			LOGGER.debug("DmmRepositoryDAO.getCountByAssetId()", "sql:" + sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				return result.get(0).intValue();
			}
			return 0;
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.getCountByAssetId() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY)}, e);
		}	
	}
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#getEmailByUserId()
	 */
	@Override
	public AdmUserDTO getEmailByUserId(String userId) throws DataAccessException {
		LOGGER.debug("DmmRepositoryDAO.getEmailByUserId.userId:'" + userId + "'");
		try {
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append("select a.CNAME as cname,a.ENAME as ename,a.EMAIL as email,a.MANAGER_EMAIL as managerEmail,a.USER_ID as userId, a.COMPANY_ID as companyId, a.ACCOUNT as account from ");
			sql.append(schema).append(".ADM_USER a ");
			sql.append("where 1=1 ");
			if (StringUtils.hasText(userId)) {
				sql.append("and (a.ACCOUNT =:userId or a.USER_ID =:userId) ");
				sql.setParameter("userId", userId);
			}
			sql.append(" AND a.STATUS <>:status");
			//Task #2478
			sql.setParameter("status", IAtomsConstants.ACCOUNT_STATUS_DISABLED);
			sql.append(" AND a.DELETED =:deleted");
			sql.setParameter("deleted", IAtomsConstants.NO);
			LOGGER.debug("DmmRepositoryDAO.getEmailByUserId()", "sql:" + sql.toString());
			AliasBean aliasBean = new AliasBean(AdmUserDTO.class);
			
			List<AdmUserDTO> result = (List<AdmUserDTO>) this.getDaoSupport().findByNativeSql(sql, aliasBean);
			if (!CollectionUtils.isEmpty(result)) {
				return result.get(0);
			}
			return null;
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.getEmailByUserId() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_ADM_USER)}, e);
		}	
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#getUserIdByAccount(java.lang.String)
	 */
	@Override
	public String getUserIdByAccount(String account) throws DataAccessException {
		LOGGER.debug("DmmRepositoryDAO.getUserIdByAccount.account:'" + account + "'");
		try {
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append("select a.USER_ID from ");
			sql.append(schema).append(".ADM_USER a ");
			sql.append("where 1=1 ");
			if (StringUtils.hasText(account)) {
				sql.append("and a.ACCOUNT = :account");
				sql.setParameter("account", account);
			}
			LOGGER.debug("DmmRepositoryDAO.getUserIdByAccount()", "sql:" + sql.toString());
			//AliasBean aliasBean = new AliasBean(Parameter.class);
			List<String> result = this.getDaoSupport().findByNativeSql(sql);
			if (!CollectionUtils.isEmpty(result)) {
				return result.get(0);
			}
			return null;
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.getEmailByUserId() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_ADM_USER)}, e);
		}	
	}
	public List<DmmRepositoryDTO> getUnBackBorrowers() throws DataAccessException {
		try {
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append("select at.NAME as name, d.SERIAL_NUMBER as serialNumber, d.ASSET_ID as assetId, d.BORROWER as borrower, d.BORROWER_START as borrowerStart, d.BORROWER_END as borrowerEnd, ");
			sql.append("d.BORROWER_EMAIL as borrowerEmail,a.CNAME as borrowerName, d.BORROWER_MGR_EMAIL as borrowerMgrEmail from ");
			sql.append(schema).append(".DMM_REPOSITORY d left join ");
			sql.append(schema).append(".ADM_USER a on a.USER_ID=d.BORROWER left join ");
			sql.append(schema).append(".DMM_ASSET_TYPE at on d.ASSET_TYPE_ID=at.ASSET_TYPE_ID ");
			sql.append("where 1=1 ");
			sql.append("AND d.BORROWER_END<=getDate() ");
			sql.append("AND DATEDIFF(day,d.BORROWER_END, getDate()) <=1 ");

			LOGGER.debug("DmmRepositoryDAO.getUnBackBorrowers()", "sql:" + sql.toString());
			AliasBean aliasBean = new AliasBean(DmmRepositoryDTO.class);
			aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.BORROWER.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.BORROWER_START.getValue(), TimestampType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.BORROWER_END.getValue(), TimestampType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.ASSET_ID.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.BORROWER_EMAIL.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.BORROWER_NAME.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.BORROWER_MGR_EMAIL.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.NAME.getValue(), StringType.INSTANCE);
            aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);

			List<DmmRepositoryDTO> result = this.getDaoSupport().findByNativeSql(sql, aliasBean);
			if (!CollectionUtils.isEmpty(result)) {
				return result;
			}
			return null;
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.getUnBackBorrowers() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_ADM_USER)}, e);
		}	
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#checkPropertyId(java.lang.String)
	 */
	@Override
	public boolean isRepeatPropertyId(String propertyId,String assetId)
			throws DataAccessException {
		LOGGER.debug("DmmRepositoryDAO.checkPropertyId.propertyId:'" + propertyId + "'");
		try {
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append("select PROPERTY_ID from ");
			sql.append(schema).append(".DMM_REPOSITORY ");
			sql.append("where 1=1 ");
			if (StringUtils.hasText(propertyId)) {
				sql.append("and PROPERTY_ID = :propertyId");
				sql.setParameter("propertyId", propertyId);
			}
			if (StringUtils.hasText(assetId)) {
				sql.append("and ASSET_ID !='" + assetId + "'");
			}
			LOGGER.debug("DmmRepositoryDAO.checkPropertyId()", "sql:" + sql.toString());
			List<String> result =(List<String>) this.getDaoSupport().findByNativeSql(sql);
			if (!CollectionUtils.isEmpty(result)) {
				return false;
			}
			return true;
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.checkPropertyId() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}	
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#isRepeatDtid(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isRepeatDtid(String dtid, String assetId) throws DataAccessException {
		LOGGER.debug("DmmRepositoryDAO.checkDtid.dtid:'" + dtid + "'");
		LOGGER.debug("DmmRepositoryDAO.checkDtid.assetId:'" + assetId + "'");
		try {
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append("select DTID from ");
			sql.append(schema).append(".DMM_REPOSITORY ");
			sql.append("where 1=1 ");
			if (StringUtils.hasText(dtid)) {
				sql.append("and DTID = :dtid");
				sql.setParameter("dtid", dtid);
			}
			if (StringUtils.hasText(assetId)) {
				sql.append("and ASSET_ID !=:assetId");
				sql.setParameter("assetId", assetId);
			}
			LOGGER.debug("DmmRepositoryDAO.checkDtid()", "sql:" + sql.toString());
			List<String> result =(List<String>) this.getDaoSupport().findByNativeSql(sql);
			if (!CollectionUtils.isEmpty(result)) {
				return false;
			}
			return true;
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.checkDtid() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}	
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetTransInfoDAO#getRepositoryBySerialNumber(java.lang.String)
	 */
	@Override
	public DmmRepositoryDTO getRepositoryBySerialNumber(String serialNumber, String fromWarehouseId, String state) throws DataAccessException {
		LOGGER.debug(this.getClass().getSimpleName() + "getRepositoryBySerialNumber", "serialNumber:" + serialNumber);
		LOGGER.debug(this.getClass().getSimpleName() + "getRepositoryBySerialNumber", "fromWarehouseId:" + fromWarehouseId);
		//庫存信息
		DmmRepositoryDTO repositoryDTO = null;
		List<DmmRepositoryDTO> result = null;
		//得到schema
		String schema = this.getMySchema();
		//創建SqlStatement
		SqlStatement sqlStatement = new SqlStatement();
		try{
			sqlStatement.addSelectClause("repository.ASSET_ID", DmmRepositoryDTO.ATTRIBUTE.ASSET_ID.getValue());
			sqlStatement.addSelectClause("repository.SERIAL_NUMBER", DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("repository.ASSET_TYPE_ID", DmmRepositoryDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			sqlStatement.addSelectClause("type.NAME", DmmRepositoryDTO.ATTRIBUTE.NAME.getValue());
			sqlStatement.addSelectClause("PROPERTY_ID", DmmRepositoryDTO.ATTRIBUTE.PROPERTY_ID.getValue());
			sqlStatement.addSelectClause("repository.WAREHOUSE_ID", DmmRepositoryDTO.ATTRIBUTE.WAREHOUSE_ID.getValue());
			sqlStatement.addSelectClause("repository.CONTRACT_ID", DmmRepositoryDTO.ATTRIBUTE.CONTRACT_ID.getValue());
			sqlStatement.addSelectClause("contract.CONTRACT_CODE", DmmRepositoryDTO.ATTRIBUTE.CONTRACT_CODE.getValue());
			sqlStatement.addSelectClause("repository.MA_TYPE", DmmRepositoryDTO.ATTRIBUTE.MA_TYPE.getValue());
			sqlStatement.addSelectClause("repository.STATUS", DmmRepositoryDTO.ATTRIBUTE.STATUS.getValue());
			sqlStatement.addSelectClause("repository.IS_ENABLED", DmmRepositoryDTO.ATTRIBUTE.IS_ENABLED.getValue());
			sqlStatement.addSelectClause("repository.ENABLE_DATE", DmmRepositoryDTO.ATTRIBUTE.ENABLE_DATE.getValue());
			//sqlStatement.addSelectClause("repository.IS_SIM_ENABLE", DmmRepositoryDTO.ATTRIBUTE.IS_SIM_ENABLE.getValue());
			sqlStatement.addSelectClause("repository.SIM_ENABLE_DATE", DmmRepositoryDTO.ATTRIBUTE.SIM_ENABLE_DATE.getValue());
			sqlStatement.addSelectClause("repository.SIM_ENABLE_NO", DmmRepositoryDTO.ATTRIBUTE.SIM_ENABLE_NO.getValue());
			sqlStatement.addSelectClause("repository.CARRIER", DmmRepositoryDTO.ATTRIBUTE.CARRIER.getValue());
			sqlStatement.addSelectClause("repository.CARRY_DATE", DmmRepositoryDTO.ATTRIBUTE.CARRY_DATE.getValue());
			sqlStatement.addSelectClause("repository.BORROWER", DmmRepositoryDTO.ATTRIBUTE.BORROWER.getValue());
			sqlStatement.addSelectClause("repository.BORROWER_START", DmmRepositoryDTO.ATTRIBUTE.BORROWER_START.getValue());
			sqlStatement.addSelectClause("repository.BORROWER_END", DmmRepositoryDTO.ATTRIBUTE.BORROWER_END.getValue());
			sqlStatement.addSelectClause("repository.BORROWER_EMAIL", DmmRepositoryDTO.ATTRIBUTE.BORROWER_EMAIL.getValue());
			sqlStatement.addSelectClause("repository.BORROWER_MGR_EMAIL", DmmRepositoryDTO.ATTRIBUTE.BORROWER_MGR_EMAIL.getValue());
			sqlStatement.addSelectClause("repository.BACK_DATE", DmmRepositoryDTO.ATTRIBUTE.BACK_DATE.getValue());
			sqlStatement.addSelectClause("repository.ASSET_OWNER", DmmRepositoryDTO.ATTRIBUTE.ASSET_OWNER.getValue());
			sqlStatement.addSelectClause("company.SHORT_NAME", DmmRepositoryDTO.ATTRIBUTE.ASSET_OWNER_NAME.getValue());
			sqlStatement.addSelectClause("repository.ASSET_USER", DmmRepositoryDTO.ATTRIBUTE.ASSET_USER.getValue());
			sqlStatement.addSelectClause("companyUser.SHORT_NAME", DmmRepositoryDTO.ATTRIBUTE.ASSET_USER_NAME.getValue());
			sqlStatement.addSelectClause("companyUser.COMPANY_CODE", DmmRepositoryDTO.ATTRIBUTE.COMPANY_CODE.getValue());
			sqlStatement.addSelectClause("repository.IS_CUP", DmmRepositoryDTO.ATTRIBUTE.IS_CUP.getValue());
			/*sqlStatement.addSelectClause("repository.RETIRE_REASON_CODE", DmmRepositoryDTO.ATTRIBUTE.RETIRE_REASON_CODE.getValue());
			sqlStatement.addSelectClause("repository.CASE_ID", DmmRepositoryDTO.ATTRIBUTE.CASE_ID.getValue());*/
			/*sqlStatement.addSelectClause("repository.RETIRE_COMMENT", DmmRepositoryDTO.ATTRIBUTE.REPAIR_COMMENT.getValue());*/
			sqlStatement.addSelectClause("repository.RETIRE_REASON_CODE", DmmRepositoryDTO.ATTRIBUTE.RETIRE_REASON_CODE.getValue());
			sqlStatement.addSelectClause("repository.CASE_ID", DmmRepositoryDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("repository.CASE_COMPLETION_DATE", DmmRepositoryDTO.ATTRIBUTE.CASE_COMPLETION_DATE.getValue());
//			sqlStatement.addSelectClause("repository.CASE_CLOSE_DATE", DmmRepositoryDTO.ATTRIBUTE.CASE_CLOSE_DATE.getValue());
			sqlStatement.addSelectClause("repository.TID", DmmRepositoryDTO.ATTRIBUTE.TID.getValue());
			sqlStatement.addSelectClause("repository.DTID", DmmRepositoryDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("repository.APPLICATION_ID", DmmRepositoryDTO.ATTRIBUTE.APPLICATION_ID.getValue());
			sqlStatement.addSelectClause("repository.MERCHANT_ID", DmmRepositoryDTO.ATTRIBUTE.MERCHANT_ID.getValue());
			sqlStatement.addSelectClause("merchant.MERCHANT_CODE", DmmRepositoryDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("merchantHeader.HEADER_NAME", DmmRepositoryDTO.ATTRIBUTE.HEADER_NAME.getValue());
			sqlStatement.addSelectClause("repository.ASSET_IN_ID", DmmRepositoryDTO.ATTRIBUTE.ASSET_IN_ID.getValue());
			sqlStatement.addSelectClause("repository.ASSET_IN_TIME", DmmRepositoryDTO.ATTRIBUTE.ASSET_IN_TIME.getValue());
			sqlStatement.addSelectClause("repository.ASSET_TRANS_ID", DmmRepositoryDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue());
			sqlStatement.addSelectClause("repository.MAINTAIN_COMPANY", DmmRepositoryDTO.ATTRIBUTE.MAINTAIN_COMPANY.getValue());
			sqlStatement.addSelectClause("companyMaintain.SHORT_NAME", DmmRepositoryDTO.ATTRIBUTE.MAINTAIN_COMPANY_NAME.getValue());
			sqlStatement.addSelectClause("repository.REPAIR_VENDOR", DmmRepositoryDTO.ATTRIBUTE.REPAIR_VENDOR.getValue());
			sqlStatement.addSelectClause("repository.DESCRIPTION", DmmRepositoryDTO.ATTRIBUTE.DESCRIPTION.getValue());
			sqlStatement.addSelectClause("repository.ACTION", DmmRepositoryDTO.ATTRIBUTE.ACTION.getValue());
			sqlStatement.addSelectClause("repository.FAULT_COMPONENT", DmmRepositoryDTO.ATTRIBUTE.FAULT_COMPONENT.getValue());
			sqlStatement.addSelectClause("repository.FAULT_DESCRIPTION", DmmRepositoryDTO.ATTRIBUTE.FAULT_DESCRIPTION.getValue());
			sqlStatement.addSelectClause("repository.CUSTOMER_WARRANTY_DATE", DmmRepositoryDTO.ATTRIBUTE.CUSTOMER_WARRANTY_DATE.getValue());
			sqlStatement.addSelectClause("repository.FACTORY_WARRANTY_DATE", DmmRepositoryDTO.ATTRIBUTE.FACTORY_WARRANTY_DATE.getValue());
			sqlStatement.addSelectClause("repository.CREATE_USER", DmmRepositoryDTO.ATTRIBUTE.CREATE_USER.getValue());
			sqlStatement.addSelectClause("repository.CREATE_USER_NAME", DmmRepositoryDTO.ATTRIBUTE.CREATE_USER_NAME.getValue());
			sqlStatement.addSelectClause("repository.CREATE_DATE", DmmRepositoryDTO.ATTRIBUTE.CREATE_DATE.getValue());
			sqlStatement.addSelectClause("repository.UPDATE_USER", DmmRepositoryDTO.ATTRIBUTE.UPDATE_USER.getValue());
			sqlStatement.addSelectClause("repository.UPDATE_USER_NAME", DmmRepositoryDTO.ATTRIBUTE.UPDATE_USER_NAME.getValue());
			sqlStatement.addSelectClause("repository.UPDATE_DATE", DmmRepositoryDTO.ATTRIBUTE.UPDATE_DATE.getValue());
//			sqlStatement.addSelectClause("repository.DELETED", DmmRepositoryDTO.ATTRIBUTE.DELETED.getValue());
			sqlStatement.addSelectClause("repository.CHECKED_DATE", DmmRepositoryDTO.ATTRIBUTE.CHECKED_DATE.getValue());
			sqlStatement.addSelectClause("repository.ASSET_MODEL", DmmRepositoryDTO.ATTRIBUTE.ASSET_MODEL.getValue());
			sqlStatement.addSelectClause("repository.INSTALLED_ADRESS", DmmRepositoryDTO.ATTRIBUTE.INSTALL_ADRESS.getValue());
			sqlStatement.addSelectClause("repository.INSTALL_TYPE", DmmRepositoryDTO.ATTRIBUTE.INSTALL_TYPE.getValue());
			sqlStatement.addSelectClause("repository.MERCHANT_HEADER_ID", DmmRepositoryDTO.ATTRIBUTE.MERCHANT_HEADER_ID.getValue());
			sqlStatement.addSelectClause("repository.CYBER_APPROVED_DATE", DmmRepositoryDTO.ATTRIBUTE.CYBER_APPROVED_DATE.getValue());
			sqlStatement.addSelectClause("repository.KEEPER_NAME", DmmRepositoryDTO.ATTRIBUTE.KEEPER_NAME.getValue());
			sqlStatement.addSelectClause("repository.INSTALLED_ADRESS_LOCATION", DmmRepositoryDTO.ATTRIBUTE.INSTALLED_ADRESS_LOCATION.getValue());
			sqlStatement.addSelectClause("repository.IS_CHECKED", DmmRepositoryDTO.ATTRIBUTE.IS_CHECKED.getValue());
			sqlStatement.addSelectClause("repository.IS_CUSTOMER_CHECKED", DmmRepositoryDTO.ATTRIBUTE.IS_CUSTOMER_CHECKED.getValue());
			sqlStatement.addSelectClause("repository.DEPARTMENT_ID", DmmRepositoryDTO.ATTRIBUTE.DEPARTMENT_ID.getValue());
			sqlStatement.addSelectClause("repository.REPAIR_COMPANY", DmmRepositoryDTO.ATTRIBUTE.REPAIR_COMPANY.getValue());
			sqlStatement.addSelectClause("repository.BRAND", DmmRepositoryDTO.ATTRIBUTE.BRAND.getValue());
			sqlStatement.addSelectClause("repository.MAINTAIN_USER", DmmRepositoryDTO.ATTRIBUTE.MAINTAIN_USER.getValue());
			sqlStatement.addSelectClause("admUser.CNAME", DmmRepositoryDTO.ATTRIBUTE.MAINTAIN_USER_NAME.getValue());
			sqlStatement.addSelectClause("admUser.ENAME", DmmRepositoryDTO.ATTRIBUTE.ENAME.getValue());
			sqlStatement.addSelectClause("repository.ANALYZE_DATE", DmmRepositoryDTO.ATTRIBUTE.ANALYZE_DATE.getValue());
			sqlStatement.addSelectClause("repository.UNINSTALL_OR_REPAIR_REASON", DmmRepositoryDTO.ATTRIBUTE.UNINSTALL_OR_REPAIRREASON.getValue());
			
			sqlStatement.addSelectClause("repository.COUNTER", DmmRepositoryDTO.ATTRIBUTE.COUNTER.getValue());
			sqlStatement.addSelectClause("repository.CARTON_NO", DmmRepositoryDTO.ATTRIBUTE.CARTON_NO.getValue());
			sqlStatement.addSelectClause("repository.INSTALLED_DEPT_ID", DmmRepositoryDTO.ATTRIBUTE.INSTALLED_DEPT_ID.getValue());
			sqlStatement.addSelectClause("itemType.ITEM_NAME", DmmRepositoryDTO.ATTRIBUTE.INSTALLED_ADRESS_LOCATION_NAME.getValue());
			sqlStatement.addSelectClause("repository.CARRY_COMMENT", DmmRepositoryDTO.ATTRIBUTE.CARRY_COMMENT.getValue());
			sqlStatement.addSelectClause("repository.DISABLED_COMMENT", DmmRepositoryDTO.ATTRIBUTE.DISABLED_COMMENT.getValue());
			sqlStatement.addSelectClause("repository.REPAIR_COMMENT", DmmRepositoryDTO.ATTRIBUTE.REPAIR_COMMENT.getValue());
  
			sqlStatement.addFromExpression(schema + ".DMM_REPOSITORY repository"
							+ " left join " + schema + ".DMM_ASSET_TYPE AS type ON repository.ASSET_TYPE_ID = type.ASSET_TYPE_ID"
							+ " left join " + schema + ".BIM_COMPANY company on company.COMPANY_ID = repository.ASSET_OWNER"
							+ " left join " + schema + ".BIM_COMPANY companyUser on companyUser.COMPANY_ID = repository.ASSET_USER"
							+ " left join " + schema + ".BIM_COMPANY companyMaintain on companyMaintain.COMPANY_ID = repository.MAINTAIN_COMPANY"
							+ " left join " + schema + ".BIM_MERCHANT merchant on merchant.MERCHANT_ID = repository.MERCHANT_ID"
							+ " left join " + schema + ".BIM_MERCHANT_HEADER merchantHeader on merchantHeader.MERCHANT_HEADER_ID = repository.MERCHANT_HEADER_ID"
							+ " left join " + schema + ".ADM_USER admUser on admUser.USER_ID = repository.MAINTAIN_USER"
							+ " left join " + schema + ".BIM_CONTRACT contract on contract.CONTRACT_ID = repository.CONTRACT_ID"
							+ " left join " + schema + ".BASE_PARAMETER_ITEM_DEF itemType on itemType.ITEM_VALUE = repository.INSTALLED_ADRESS_LOCATION and itemType.BPTD_CODE = :location");
			if (StringUtils.hasText(serialNumber)) {
				sqlStatement.addWhereClause("repository.SERIAL_NUMBER =:serialNumber", serialNumber);
			}
			if (StringUtils.hasText(fromWarehouseId)) {
				sqlStatement.addWhereClause("repository.WAREHOUSE_ID =:fromWarehouseId", fromWarehouseId);
			}
			//設備狀態為庫存
			if (StringUtils.hasText(state)) {
				sqlStatement.addWhereClause("repository.STATUS =:status", state);
			}
			//判斷刪除標誌
//			sqlStatement.addWhereClause("ISNULL(repository.DELETED, 'N') !=:deleted",IAtomsConstants.YES);
			//記錄sql語句
			LOGGER.debug(this.getClass().getName() + "list ---->sql:" + sqlStatement.toString() );
			//創建SqlQueryBean
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("location", IATOMS_PARAM_TYPE.LOCATION.getCode());
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmRepositoryDTO.class);
			//執行sql語句
			result = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(result)) {
				repositoryDTO = result.get(0);
			}
		} catch(Exception e) {
			LOGGER.error("DmmRepositoryDAO.getRepositoryBySerialNumber() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		return repositoryDTO;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#isPropertyIdRepeat(java.lang.String, java.lang.String)
	 */
	public boolean isPropertyIdRepeat(String propertyId ,String serialNumber) throws DataAccessException {
		String schema = this.getMySchema();
		List<Integer> repositorys = null;
		try {
			LOGGER.debug("DmmRepositoryDAO.checkPropertyIdRepeat()", "parameters:propertyId=" + propertyId);
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			sqlStatement.addFromExpression(schema + ".DMM_REPOSITORY repository");
			if (StringUtils.hasText(serialNumber)) {
				sqlStatement.addWhereClause("repository.SERIAL_NUMBER <> :serialNumber", serialNumber);
			}
			if (StringUtils.hasText(propertyId)) {
				sqlStatement.addWhereClause("repository.PROPERTY_ID = :propertyId", propertyId);
			}
//			sqlStatement.addWhereClause("ISNULL(repository.DELETED, 'N') != :deleted",IAtomsConstants.YES);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug("DmmRepositoryDAO.checkPropertyIdRepeat()", "sql:" + sqlQueryBean.toString());
			repositorys = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(repositorys)) {
				if (repositorys.get(0).intValue() == 0) {
					return false;
				}
				return true;
			}
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.checkPropertyIdRepeat() Exception-->" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return false;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#isDtidInUse(java.lang.String, java.lang.String)
	 */
	/*public boolean isDtidInUse(String dtidStart, String dtidEnd) throws DataAccessException {
		String schema = this.getMySchema();
		List<Integer> repositorys = null;
		try {
			LOGGER.debug("DmmRepositoryDAO.checkDtidIsUse()", "parameters:dtidStart=" + dtidStart);
			LOGGER.debug("DmmRepositoryDAO.checkDtidIsUse()", "parameters:dtidEnd=" + dtidEnd);
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			sqlStatement.addFromExpression(schema + ".DMM_REPOSITORY repository");
			if (StringUtils.hasText(dtidStart) && StringUtils.hasText(dtidEnd)) {
				sqlStatement.addWhereClause("repository.dtid >= :dtidStart", Integer.valueOf(dtidStart));
				sqlStatement.addWhereClause("repository.dtid <= :dtidEnd", Integer.valueOf(dtidEnd));
			}
			sqlStatement.addWhereClause("repository.deleted = :deleted", IAtomsConstants.NO);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug("DmmRepositoryDAO.checkDtidIsUse()", "sql:" + sqlQueryBean.toString());
			repositorys = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(repositorys)) {
				if (repositorys.get(0).intValue() == 0) {
					return false;
				}
				return true;
			}
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.checkDtidIsUse() Exception-->" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return false;
	}*/
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#getRepositoryByPropertyId(java.lang.String)
	 */
	@Override
	public DmmRepositoryDTO getRepositoryByPropertyId(String propertyId) throws DataAccessException {
		LOGGER.debug(this.getClass().getSimpleName() + "getRepositoryByPropertyId", "propertyId:" + propertyId);
		//庫存信息
		DmmRepositoryDTO repositoryDTO = null;
		List<DmmRepositoryDTO> result = null;
		//得到schema
		String schema = this.getMySchema();
		//創建SqlStatement
		SqlStatement sql = new SqlStatement();
		try{
			sql.addSelectClause("repository.ASSET_ID", DmmRepositoryDTO.ATTRIBUTE.ASSET_ID.getValue());
			sql.addSelectClause("repository.SERIAL_NUMBER", DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sql.addSelectClause("assetType.NAME", DmmRepositoryDTO.ATTRIBUTE.NAME.getValue());
			sql.addSelectClause("assetType.ASSET_TYPE_ID", DmmRepositoryDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			sql.addSelectClause("convert(nvarchar,repository.IS_CUP)", DmmRepositoryDTO.ATTRIBUTE.IS_CUP.getValue());
			sql.addSelectClause("convert(nvarchar,repository.IS_ENABLED)", DmmRepositoryDTO.ATTRIBUTE.IS_ENABLED.getValue());
			sql.addSelectClause("repository.STATUS", DmmRepositoryDTO.ATTRIBUTE.STATUS.getValue());
			sql.addSelectClause("contract.CONTRACT_ID", DmmRepositoryDTO.ATTRIBUTE.CONTRACT_ID.getValue());
			sql.addFromExpression(schema+".DMM_REPOSITORY repository" +
			" left join " + schema + ".BIM_CONTRACT contract on contract.CONTRACT_ID = repository.CONTRACT_ID" +
			" left join " + schema + ".DMM_ASSET_TYPE assetType on assetType.ASSET_TYPE_ID = repository.ASSET_TYPE_ID");
			//設備狀態為庫存
			if (StringUtils.hasText(propertyId)) {
				sql.addWhereClause("repository.PROPERTY_ID =:status", propertyId);
			}
			//判斷刪除標誌
//			sql.addWhereClause("ISNULL(repository.DELETED, 'N') !=:deleted",IAtomsConstants.YES);
			//記錄sql語句
			LOGGER.debug(this.getClass().getName() + "list ---->sql:" + sql.toString() );
			//創建SqlQueryBean
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			AliasBean aliasBean = new AliasBean(DmmRepositoryDTO.class);
			//執行sql語句
			result = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(result)) {
				repositoryDTO = result.get(0);
			}
		} catch(Exception e) {
			LOGGER.error("DmmRepositoryDAO.getRepositoryByPropertyId() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		return repositoryDTO;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#saveRepository(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void saveRepository(String assetId, String historyId, String userId, String userName, String assetInId, Timestamp cyberApprovedDate) throws DataAccessException {
		try {
			//打印參數
			LOGGER.debug(this.getClass().getSimpleName() + "saveRepository", "assetId:" + assetId);
			LOGGER.debug(this.getClass().getSimpleName() + "saveRepository", "historyId:" + historyId);
			LOGGER.debug(this.getClass().getSimpleName() + "saveRepository", "userId:" + userId);
			LOGGER.debug(this.getClass().getSimpleName() + "saveRepository", "userName:" + userName);
			LOGGER.debug(this.getClass().getSimpleName() + "saveRepository", "assetInId:" + assetInId);
			//得到schema
			String schema = this.getMySchema();
			//創建SqlStatement
			Timestamp timestamp = DateTimeUtils.getCurrentTimestamp();
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("SELECT :assetId + '-' + cast(ROW_NUMBER()over (order by ail.id) as varchar) as assetId").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("getdate() as assetInTime").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(":historyId + '-' + cast(ROW_NUMBER()over (order by ail.id) as varchar) as historyId").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("ail.ASSET_IN_ID as assetInId").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("ail.SERIAL_NUMBER as serialNumber").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("ail.PROPERTY_ID as propertyId").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("ain.CONTRACT_ID as contractId").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("ain.MA_TYPE as myType").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("ain.ASSET_MODEL as assetModel").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("ain.ASSET_TYPE_ID as assetTypeId").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("ain.FACTORY_WARRANTY_DATE as factoryDate").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("ain.CUSTOMER_WARRANTY_DATE as customerDate").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("ain.WAREHOUSE_ID as warehouseId").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("ain.USER_ID as userId").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("ain.KEEPER_NAME as keeperName").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("ain.OWNER as owner").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("ain.CYBER_APPROVED_DATE as cyberApprovedDate").append(IAtomsConstants.MARK_SEPARATOR);
			
			
			
			sqlQueryBean.append(" :action" + " as action").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(" :status" + " as status").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(" :userId as createUser").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(" :userName as createUserName").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("getdate() as createDate").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(" :userId as updateUser").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(" :userName as updateUserName").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("getdate() as updateDate").append(IAtomsConstants.MARK_SEPARATOR);
//			sqlQueryBean.append(" :deleted as deleted").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(" :enabled as enabled").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(" :isCup as isCup").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(" ain.BRAND as brand").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("ail.IS_CHECKED as isChecked").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("ail.IS_CUSTOMER_CHECKED as isCustomerChecked").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("ain.CUSTOMER_APPROVE_DATE as checkedDate").append(" into #temp");
			//sqlQueryBean.append(" :checkedDate as checkedDate").append(" into #temp");
			sqlQueryBean.append(" from " + schema + ".DMM_ASSET_IN_INFO ain");
			sqlQueryBean.append(" LEFT JOIN " + schema + ".DMM_ASSET_IN_LIST ail ON ail.ASSET_IN_ID = ain.ASSET_IN_ID");
			sqlQueryBean.append(" where ail.ASSET_IN_ID = :assetInId");
			sqlQueryBean.append(" insert into " +schema +".DMM_REPOSITORY(");
			sqlQueryBean.append("ASSET_ID, ASSET_IN_TIME, ASSET_IN_ID, SERIAL_NUMBER, PROPERTY_ID, CONTRACT_ID,"
							+ " MA_TYPE,  ASSET_MODEL, ASSET_TYPE_ID, FACTORY_WARRANTY_DATE, CUSTOMER_WARRANTY_DATE,"
							+ " WAREHOUSE_ID, ASSET_USER, KEEPER_NAME, ASSET_OWNER,CYBER_APPROVED_DATE, [ACTION], [STATUS], CREATE_USER, CREATE_USER_NAME,"  
							+ " CREATE_DATE, UPDATE_USER, UPDATE_USER_NAME, UPDATE_DATE, IS_ENABLED,"
							+ " IS_CUP, BRAND,IS_CHECKED,IS_CUSTOMER_CHECKED,CHECKED_DATE) ");
			sqlQueryBean.append(" select assetId, assetInTime, assetInId, serialNumber, propertyId, contractId, myType, assetModel,"
							+ "assetTypeId, factoryDate, customerDate, warehouseId, userId, keeperName, owner,cyberApprovedDate, action, status,"
							+ "createUser, createUserName, createDate, updateUser, updateUserName, updateDate,"
							+ " enabled, isCup, brand,isChecked,isCustomerChecked, checkedDate");
			sqlQueryBean.append("from #temp");
			sqlQueryBean.append(" insert into " +schema +".DMM_REPOSITORY_HISTORY(");
			sqlQueryBean.append("ASSET_ID, ASSET_IN_TIME, HISTORY_ID ,ASSET_IN_ID, SERIAL_NUMBER, PROPERTY_ID, CONTRACT_ID,"
							+ " MA_TYPE, ASSET_MODEL,ASSET_TYPE_ID, FACTORY_WARRANTY_DATE, CUSTOMER_WARRANTY_DATE,"
							+ " WAREHOUSE_ID, ASSET_USER, KEEPER_NAME, ASSET_OWNER,CYBER_APPROVED_DATE, [ACTION], [STATUS], CREATE_USER, CREATE_USER_NAME,"  
							+ " CREATE_DATE, UPDATE_USER, UPDATE_USER_NAME, UPDATE_DATE, IS_ENABLED,"
							+ " IS_CUP, BRAND,IS_CHECKED,IS_CUSTOMER_CHECKED, CHECKED_DATE) ");
			sqlQueryBean.append(" select *");
			sqlQueryBean.append("from #temp");
			sqlQueryBean.append(" drop table #temp");
			sqlQueryBean.setParameter("assetId", assetId);
			sqlQueryBean.setParameter("historyId", historyId);
			sqlQueryBean.setParameter("assetInId", assetInId);
			sqlQueryBean.setParameter("userId", userId);
			sqlQueryBean.setParameter("userName", userName);
//			sqlQueryBean.setParameter("deleted", IAtomsConstants.NO);
			sqlQueryBean.setParameter("enabled", IAtomsConstants.NO);
			sqlQueryBean.setParameter("isCup", IAtomsConstants.NO);
			//sqlQueryBean.setParameter("checkedDate", cyberApprovedDate);
			sqlQueryBean.setParameter("action", IAtomsConstants.PARAM_ACTION_IN_ASSET);
			sqlQueryBean.setParameter("status", IAtomsConstants.PARAM_ASSET_STATUS_REPERTORY);
			LOGGER.debug(this.getClass().getName() + "saveRepository ---->sql:" + sqlQueryBean.toString() );
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			Timestamp timestamp2 = DateTimeUtils.getCurrentTimestamp();
			System.out.println(timestamp + "----" + timestamp2);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.saveRepository() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#getRepositoryDTOBy(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public DmmRepositoryDTO getRepositoryDTOBy(String contractCode,String assetName, String owner, String user)throws DataAccessException {
		LOGGER.debug(this.getClass().getSimpleName() + "DmmRepositoryDTO", "contractCode:" + contractCode);
		LOGGER.debug(this.getClass().getSimpleName() + "DmmRepositoryDTO", "assetName:" + assetName);
		LOGGER.debug(this.getClass().getSimpleName() + "DmmRepositoryDTO", "owner:" + owner);
		LOGGER.debug(this.getClass().getSimpleName() + "DmmRepositoryDTO", "user:" + user);
		//庫存信息
		DmmRepositoryDTO repositoryDTO = null;
		List<DmmRepositoryDTO> result = null;
		//得到schema
		String schema = this.getMySchema();
		//創建SqlStatement
		SqlStatement sql = new SqlStatement();
		try{
			sql.addSelectClause("contract.CONTRACT_ID", DmmRepositoryDTO.ATTRIBUTE.CONTRACT_ID.getValue());
			sql.addSelectClause("company1.COMPANY_ID", DmmRepositoryDTO.ATTRIBUTE.ASSET_OWNER.getValue());
			sql.addSelectClause("company2.COMPANY_ID", DmmRepositoryDTO.ATTRIBUTE.ASSET_USER.getValue());
			sql.addSelectClause("type.ASSET_TYPE_ID", DmmRepositoryDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			sql.addFromExpression(schema + ".BIM_CONTRACT contract,"
						+ schema + ".DMM_ASSET_TYPE type," 
						+ schema + ".BIM_COMPANY company1" 
						+ " left join " + schema + ".BIM_COMPANY_TYPE contractType on contractType.COMPANY_ID = company1.COMPANY_ID," 
						+ schema + ".BIM_COMPANY company2"
						+ " left join " + schema + ".BIM_COMPANY_TYPE contractType2 on contractType2.COMPANY_ID = company2.COMPANY_ID");
			if (StringUtils.hasText(contractCode)) {
				sql.addWhereClause("contract.CONTRACT_CODE = :contractCode", contractCode);
				sql.addWhereClause("contract.DELETED = :deleted", IAtomsConstants.NO);
			}
			if (StringUtils.hasText(assetName)) {
				sql.addWhereClause("type.NAME = :assetName", assetName);
				sql.addWhereClause("type.DELETED = :deleted", IAtomsConstants.NO);
			}
			if (StringUtils.hasText(owner)) {
				sql.addWhereClause("company1.SHORT_NAME = :owner", owner);
				sql.addWhereClause("company1.DELETED = :deleted", IAtomsConstants.NO);
				sql.addWhereClause("contractType.COMPANY_TYPE = :companyType", IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
			}
			if (StringUtils.hasText(user)) {
				sql.addWhereClause("company2.SHORT_NAME = :user", user);
				sql.addWhereClause("company2.DELETED = :deleted", IAtomsConstants.NO);
				sql.addWhereClause("contractType2.COMPANY_TYPE = :companyType", IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
			}
			//記錄sql語句
			LOGGER.debug(this.getClass().getName() + "list ---->sql:" + sql.toString() );
			//創建SqlQueryBean
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			AliasBean aliasBean = new AliasBean(DmmRepositoryDTO.class);
			//執行sql語句
			result = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(result)) {
				repositoryDTO = result.get(0);
			}
		} catch(Exception e) {
			LOGGER.error("DmmRepositoryDAO.getRepositoryByPropertyId() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		return repositoryDTO;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#getPropertyIds()
	 */
	public List<Parameter> getPropertyIds() throws DataAccessException {
		List<Parameter> propertyIds = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("repository.SERIAL_NUMBER",Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("repository.PROPERTY_ID", Parameter.FIELD_NAME);
			sqlStatement.addFromExpression(schema + ".DMM_REPOSITORY repository");
			
//			sqlStatement.addWhereClause("ISNULL(repository.DELETED, 'N') != :deleted",IAtomsConstants.YES);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			LOGGER.debug("DmmRepositoryDAO.checkPropertyIdRepeat()", "sql:" + sqlQueryBean.toString());
			propertyIds = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.getPropertyIds() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		return propertyIds;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#getCyberApproveDate()
	 */
	public List<Parameter> getCyberApproveDate() throws DataAccessException {
		List<Parameter> cyberApproveDates = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("repository.SERIAL_NUMBER",Parameter.FIELD_NAME);
			sqlStatement.addSelectClause("assetIn.CYBER_APPROVED_DATE", Parameter.FIELD_VALUE);
			sqlStatement.addFromExpression(schema + ".DMM_REPOSITORY repository "
					+ "left join " + schema + ".DMM_ASSET_IN_INFO assetIn on assetIn.ASSET_IN_ID = repository.ASSET_IN_ID");
			
//			sqlStatement.addWhereClause("ISNULL(repository.DELETED, 'N') != :deleted",IAtomsConstants.YES);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			LOGGER.debug("DmmRepositoryDAO.checkPropertyIdRepeat()", "sql:" + sqlQueryBean.toString());
			cyberApproveDates = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.getPropertyIds() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		return cyberApproveDates;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#updateRepository(java.util.List)
	 */
	public void updateRepository(DmmRepositoryDTO repositoryDTO, Boolean isCounter, Boolean isCartonNo, Boolean isEnableDate, 
			Boolean isBrand, Boolean isModel, Boolean isMaintenanceMode) throws DataAccessException {
		try {
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			if (StringUtils.hasText(repositoryDTO.getUpdateTable())) {
				sql.append(" update " + schema + "." + repositoryDTO.getUpdateTable() + " set");
			} else {
				sql.append(" update " + schema + ".DMM_REPOSITORY set");
			}
			if (StringUtils.hasText(repositoryDTO.getAssetTypeId())) {
				sql.append(" ASSET_TYPE_ID = :assetTypeId").append(IAtomsConstants.MARK_SEPARATOR);
				sql.setParameter("assetTypeId", repositoryDTO.getAssetTypeId());
			}
			if (StringUtils.hasText(repositoryDTO.getPropertyId())) {
				sql.append(" PROPERTY_ID = :propertyId").append(IAtomsConstants.MARK_SEPARATOR);
				sql.setParameter("propertyId", repositoryDTO.getPropertyId());
			}
			if (StringUtils.hasText(repositoryDTO.getContractId())) {
				sql.append(" CONTRACT_ID = :contractId").append(IAtomsConstants.MARK_SEPARATOR);
				sql.setParameter("contractId", repositoryDTO.getContractId());
				if (repositoryDTO.getFactoryWarrantyDate() != null) {
					sql.append(" FACTORY_WARRANTY_DATE = :factoryWarrantyDate").append(IAtomsConstants.MARK_SEPARATOR);
					sql.setParameter(DmmRepositoryDTO.ATTRIBUTE.FACTORY_WARRANTY_DATE.getValue(), repositoryDTO.getFactoryWarrantyDate());
				}
				if (repositoryDTO.getCustomerWarrantyDate() != null) {
					sql.append(" CUSTOMER_WARRANTY_DATE = :customerWarrantyDate").append(IAtomsConstants.MARK_SEPARATOR);
					sql.setParameter(DmmRepositoryDTO.ATTRIBUTE.CUSTOMER_WARRANTY_DATE.getValue(), repositoryDTO.getCustomerWarrantyDate());
				}
			}
			if (StringUtils.hasText(repositoryDTO.getSimEnableNo())) {
				sql.append(" SIM_ENABLE_NO = :simEnableNo").append(IAtomsConstants.MARK_SEPARATOR);
				sql.setParameter("simEnableNo", repositoryDTO.getSimEnableNo());
			}
			/*if (repositoryDTO.getEnableDate() != null) {
				sql.append(" ENABLE_DATE = :enableDate").append(IAtomsConstants.MARK_SEPARATOR);
				sql.append(" IS_ENABLED = :isEnabled").append(IAtomsConstants.MARK_SEPARATOR);
				sql.setParameter("enableDate", repositoryDTO.getEnableDate());
				sql.setParameter(DmmRepositoryDTO.ATTRIBUTE.IS_ENABLED.getValue(), IAtomsConstants.YES);
			}*/
			if (isEnableDate) {
				if (repositoryDTO.getEnableDate() != null) {
					sql.append(" ENABLE_DATE = :enableDate").append(IAtomsConstants.MARK_SEPARATOR);
					sql.setParameter("enableDate", repositoryDTO.getEnableDate());
				} else {
					sql.append(" ENABLE_DATE = null").append(IAtomsConstants.MARK_SEPARATOR);
				}
				
				sql.append(" IS_ENABLED = :isEnabled").append(IAtomsConstants.MARK_SEPARATOR);
				//sql.setParameter("enableDate", repositoryDTO.getEnableDate() == null?"":repositoryDTO.getEnableDate());
				sql.setParameter(DmmRepositoryDTO.ATTRIBUTE.IS_ENABLED.getValue(), 
						repositoryDTO.getEnableDate() == null?IAtomsConstants.NO:IAtomsConstants.YES);
			}
			if (StringUtils.hasText(repositoryDTO.getAssetOwner())) {
				sql.append(" ASSET_OWNER = :assetOwner").append(IAtomsConstants.MARK_SEPARATOR);
				sql.setParameter("assetOwner", repositoryDTO.getAssetOwner());
			}
			if (StringUtils.hasText(repositoryDTO.getAssetUser())) {
				sql.append(" ASSET_USER = :assetUser").append(IAtomsConstants.MARK_SEPARATOR);
				sql.setParameter("assetUser", repositoryDTO.getAssetUser());
			}
			if (repositoryDTO.getSimEnableDate() != null) {
				sql.append(" SIM_ENABLE_DATE = :simEnableDate").append(IAtomsConstants.MARK_SEPARATOR);
				sql.setParameter("simEnableDate", repositoryDTO.getSimEnableDate());

				sql.append(" MA_TYPE = :maType").append(IAtomsConstants.MARK_SEPARATOR);
				sql.setParameter("maType", IAtomsConstants.MA_TYPE_LEASE_TO_BUYOUT);
			}
			if (repositoryDTO.getCyberApprovedDate() != null) {
				sql.append(" CYBER_APPROVED_DATE = :checkedDate").append(IAtomsConstants.MARK_SEPARATOR);
				if (!StringUtils.hasText(repositoryDTO.getContractId()) && repositoryDTO.getFactoryWarrantyDate() != null) {
					sql.append(" FACTORY_WARRANTY_DATE = :factoryWarrantyDate").append(IAtomsConstants.MARK_SEPARATOR);
					sql.setParameter(DmmRepositoryDTO.ATTRIBUTE.FACTORY_WARRANTY_DATE.getValue(), repositoryDTO.getFactoryWarrantyDate());
				}
				sql.setParameter("checkedDate", repositoryDTO.getCyberApprovedDate());
			}
			if (repositoryDTO.getAssetInTime() != null) {
				sql.append(" ASSET_IN_TIME = :assetInTime").append(IAtomsConstants.MARK_SEPARATOR);
				sql.setParameter("assetInTime", repositoryDTO.getAssetInTime());
			}
			if (isCounter) {
				sql.append(" COUNTER = :counter").append(IAtomsConstants.MARK_SEPARATOR);
				sql.setParameter("counter", repositoryDTO.getCounter() == null ? "" : repositoryDTO.getCounter());
			}
			if (isCartonNo) {
				sql.append(" CARTON_NO = :cartonNo").append(IAtomsConstants.MARK_SEPARATOR);
				sql.setParameter("cartonNo", repositoryDTO.getCartonNo() == null ? "" : repositoryDTO.getCartonNo());
			}
			if (isBrand) {
				sql.append(" BRAND = :brand").append(IAtomsConstants.MARK_SEPARATOR);
				sql.setParameter("brand", !StringUtils.hasText(repositoryDTO.getBrand()) ? "" : repositoryDTO.getBrand());
			}
			if (isModel) {
				sql.append(" ASSET_MODEL = :assetModel").append(IAtomsConstants.MARK_SEPARATOR);
				sql.setParameter("assetModel", !StringUtils.hasText(repositoryDTO.getModel()) ? "" : repositoryDTO.getModel());
			}
			//Task #3393 異動欄位-新增維護模式
			if (isMaintenanceMode) {
				sql.append(" MA_TYPE = :maintenanceModeCode").append(IAtomsConstants.MARK_SEPARATOR);
				sql.setParameter("maintenanceModeCode", !StringUtils.hasText(repositoryDTO.getMaintenanceModeCode()) ? "" : repositoryDTO.getMaintenanceModeCode());
			}
			sql.append(" UPDATE_USER = :updateUser").append(IAtomsConstants.MARK_SEPARATOR);
			sql.append(" UPDATE_USER_NAME = :updateName").append(IAtomsConstants.MARK_SEPARATOR);
			sql.append(" UPDATE_DATE = :updateDate").append(IAtomsConstants.MARK_SEPARATOR);
			sql.append(" ACTION = :action");
			sql.setParameter("updateUser", repositoryDTO.getUpdateUser());
			sql.setParameter("updateName", repositoryDTO.getUpdateUserName());
			sql.setParameter("updateDate", repositoryDTO.getUpdateDate());
			sql.append(" where ASSET_ID = :assetId");
			if (IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY_MONTHLY.equals(repositoryDTO.getUpdateTable())) {
				sql.append(" and MONTH_YEAR = :monthYear");
				sql.setParameter("monthYear", repositoryDTO.getMonthYear());
			}
			sql.setParameter("assetId", repositoryDTO.getAssetId());
			sql.setParameter(DmmRepositoryDTO.ATTRIBUTE.ACTION.getValue(), IAtomsConstants.PARAM_ACTION_BATCH_UPDATE);
			LOGGER.debug(this.getClass().getName() + "updateRepository ---->sql:" + sql.toString() );
			this.getDaoSupport().updateByNativeSql(sql);
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.updateRepository() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#getDmmRepositoryDTOs()
	 */
	public List<DmmRepositoryDTO> getDmmRepositoryDTOs() throws DataAccessException {
		List<DmmRepositoryDTO> repositoryDTOs = null;
		try {
			String schema = this.getMySchema();	 
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("SERIAL_NUMBER", DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("PROPERTY_ID", DmmRepositoryDTO.ATTRIBUTE.PROPERTY_ID.getValue());
			sqlStatement.addFromExpression(schema + ".DMM_REPOSITORY" );
//			sqlStatement.addWhereClause("DELETED= :deleted", IAtomsConstants.NO);
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmRepositoryDTO.class);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug("SQL---------->"+sqlQueryBean.toString());
			repositoryDTOs = super.getDaoSupport().findByNativeSql(sqlQueryBean,aliasBean);
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.listBy() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_CONTRACT)} ,e);
		}
		return repositoryDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#getEDC(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<DmmRepositoryDTO> getEDC(String queryAssetType, String queryAssetName, String queryPeople, String queryCommet, String queryHouse, String queryNumber, String sort, String order, Integer currentPage, Integer pageSize) throws DataAccessException {
		LOGGER.debug(this.getClass().getName() + "queryAssetType" + queryAssetType);
		LOGGER.debug(this.getClass().getName() + "queryAssetName" + queryAssetName);
		LOGGER.debug(this.getClass().getName() + "queryPeople" + queryPeople);
		LOGGER.debug(this.getClass().getName() + "queryCommet" + queryCommet);
		LOGGER.debug(this.getClass().getName() + "queryHouse" + queryHouse);
		LOGGER.debug(this.getClass().getName() + "queryNumber" + queryNumber);
		List<DmmRepositoryDTO> listEDC = null;
		String schema = this.getMySchema();
		SqlStatement sqlStatement = new SqlStatement();
		try {
			sqlStatement.addSelectClause("repo.SERIAL_NUMBER", DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("type.NAME", DmmRepositoryDTO.ATTRIBUTE.NAME.getValue());
			sqlStatement.addSelectClause("def.ITEM_NAME",  DmmRepositoryDTO.ATTRIBUTE.ASSET_CATEGORY.getValue());
			sqlStatement.addSelectClause("house.NAME", DmmRepositoryDTO.ATTRIBUTE.ITEM_NAME.getValue());
			sqlStatement.addSelectClause("repo.STATUS", DmmRepositoryDTO.ATTRIBUTE.STATUS.getValue());
			sqlStatement.addSelectClause(" ( CASE WHEN repo.STATUS = 'BORROWING' THEN repo.BORROWER WHEN repo.STATUS = 'CARRIER' THEN repo.CARRIER ELSE '' END ) ", DmmRepositoryDTO.ATTRIBUTE.EDC_PEOPLE.getValue());
			sqlStatement.addSelectClause(" ( CASE WHEN repo.STATUS = 'BORROWING' THEN repo.BORROWER_COMMENT WHEN repo.STATUS = 'CARRIER' THEN repo.CARRY_COMMENT ELSE '' END ) ", DmmRepositoryDTO.ATTRIBUTE.EDC_COMMENT.getValue());
			sqlStatement.addFromExpression(schema + ".DMM_REPOSITORY repo LEFT JOIN " + schema + ".DMM_ASSET_TYPE type ON repo.ASSET_TYPE_ID = type.ASSET_TYPE_ID " +
					" left join " + schema + ".BASE_PARAMETER_ITEM_DEF def ON def.ITEM_VALUE = type.ASSET_CATEGORY AND def.BPTD_CODE = 'ASSET_CATEGORY' " +
					" left join " + schema + ".BIM_WAREHOUSE house ON house.WAREHOUSE_ID = repo.WAREHOUSE_ID ");
			if (StringUtils.hasText(queryAssetType)) {
				sqlStatement.addWhereClause("type.ASSET_CATEGORY= :queryAssetType", queryAssetType);
			}
			if (StringUtils.hasText(queryAssetName)) {
				sqlStatement.addWhereClause("repo.ASSET_TYPE_ID = :queryAssetName", queryAssetName);
			}
			if (StringUtils.hasText(queryPeople)) {
				sqlStatement.addWhereClause("repo.BORROWER like :queryPeople or repo.CARRIER like :queryPeople");
			}
			if (StringUtils.hasText(queryCommet)) {
				sqlStatement.addWhereClause("repo.BORROWER_COMMENT like :queryCommet or repo.CARRY_COMMENT like :queryCommet");
			}
			if (StringUtils.hasText(queryHouse)) {
				sqlStatement.addWhereClause("repo.WAREHOUSE_ID= :queryHouse", queryHouse);
			}
			if (StringUtils.hasText(queryNumber)) {
				sqlStatement.addWhereClause("repo.SERIAL_NUMBER like :queryNumber", queryNumber + IAtomsConstants.MARK_PERCENT);
			}
			sqlStatement.setPageSize(pageSize);
			sqlStatement.setStartPage(currentPage - 1);
			//設置排序表達式
			if (!StringUtils.hasText(sort) && !StringUtils.hasText(order)) {
				sqlStatement.setOrderByExpression("repo.SERIAL_NUMBER asc");
			} else if (StringUtils.hasText(sort) && StringUtils.hasText(order)) {
				sqlStatement.setOrderByExpression(sort + IAtomsConstants.MARK_SPACE + order);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if (StringUtils.hasText(queryPeople)) {
				sqlQueryBean.setParameter("queryPeople", queryPeople + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(queryCommet)) {
				sqlQueryBean.setParameter("queryCommet", queryCommet + IAtomsConstants.MARK_PERCENT);
			}
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmRepositoryDTO.class);
			LOGGER.debug(this.getClass().getName() + "SQL---------->" + sqlQueryBean.toString());
			listEDC = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.getDTID() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		return listEDC;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#getEDCCount(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Integer getEDCCount(String queryAssetType, String queryAssetName, String queryPeople, String queryCommet, String queryHouse, String queryNumber) throws DataAccessException {
		LOGGER.debug(this.getClass().getName() + "queryAssetType" + queryAssetType);
		LOGGER.debug(this.getClass().getName() + "queryAssetName" + queryAssetName);
		LOGGER.debug(this.getClass().getName() + "queryPeople" + queryPeople);
		LOGGER.debug(this.getClass().getName() + "queryCommet" + queryCommet);
		LOGGER.debug(this.getClass().getName() + "queryHouse" + queryHouse);
		LOGGER.debug(this.getClass().getName() + "queryNumber" + queryNumber);
		String schema = this.getMySchema();
		SqlStatement sqlStatement = new SqlStatement();
		try {
			sqlStatement.addSelectClause("count(*)");
			sqlStatement.addFromExpression(schema + ".DMM_REPOSITORY repo LEFT JOIN " + schema + ".DMM_ASSET_TYPE type ON repo.ASSET_TYPE_ID = type.ASSET_TYPE_ID " +
					" left join " + schema + ".BASE_PARAMETER_ITEM_DEF def ON def.ITEM_VALUE = type.ASSET_CATEGORY AND def.BPTD_CODE = 'ASSET_CATEGORY' " +
					" left join " + schema + ".BIM_WAREHOUSE house ON house.WAREHOUSE_ID = repo.WAREHOUSE_ID ");
			if (StringUtils.hasText(queryAssetType)) {
				sqlStatement.addWhereClause("type.ASSET_CATEGORY= :queryAssetType", queryAssetType);
			}
			if (StringUtils.hasText(queryAssetName)) {
				sqlStatement.addWhereClause("repo.ASSET_TYPE_ID = :queryAssetName", queryAssetName);
			}
			if (StringUtils.hasText(queryPeople)) {
				sqlStatement.addWhereClause("repo.BORROWER like :queryPeople or repo.CARRIER like :queryPeople");
			}
			/*if (StringUtils.hasText(queryCommet)) {
				sqlStatement.addWhereClause("repo.BORROWER_COMMENT like :queryCommet or repo.CARRY_COMMENT like :queryCommet");
			}*/
			if (StringUtils.hasText(queryHouse)) {
				sqlStatement.addWhereClause("repo.WAREHOUSE_ID= :queryHouse", queryHouse);
			}
			if (StringUtils.hasText(queryNumber)) {
				sqlStatement.addWhereClause("repo.SERIAL_NUMBER like :queryNumber", queryNumber + IAtomsConstants.MARK_PERCENT);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if (StringUtils.hasText(queryPeople)) {
				sqlQueryBean.setParameter("queryPeople", queryPeople + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(queryCommet)) {
				sqlQueryBean.setParameter("queryCommet", queryCommet + IAtomsConstants.MARK_PERCENT);
			}
			LOGGER.debug(this.getClass().getName() + "SQL---------->" + sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			return result.get(0).intValue();
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.getDTIDCount() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#updateRepository(java.lang.String, cafe.core.bean.identity.LogonUser, java.lang.String)
	 */
	public void updateRepository(String serialNumber, LogonUser logonUser, String assetTransId) throws DataAccessException {
		try {
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append(" update " + schema + ".DMM_REPOSITORY set");
			sql.append(" ASSET_TRANS_ID = :assetTransId").append(IAtomsConstants.MARK_SEPARATOR);
			sql.append(" STATUS = :status").append(IAtomsConstants.MARK_SEPARATOR);	
			sql.append(" UPDATE_USER = :updateUser").append(IAtomsConstants.MARK_SEPARATOR);
			sql.append(" UPDATE_USER_NAME = :updateName").append(IAtomsConstants.MARK_SEPARATOR);
			sql.append(" UPDATE_DATE = :updateDate");
			sql.setParameter("assetTransId", assetTransId);
			sql.setParameter("status", IAtomsConstants.PARAM_ASSET_STATUS_ON_WAY);
			sql.setParameter("updateUser", logonUser.getId());
			sql.setParameter("updateName", logonUser.getName());
			sql.setParameter("updateDate", DateTimeUtils.getCurrentTimestamp());
			sql.append(" where 1=1");
			if (StringUtils.hasText(serialNumber)) {
				sql.append( " and SERIAL_NUMBER = :serialNumber");
				sql.setParameter(DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), serialNumber);
			}
			LOGGER.debug(this.getClass().getName() + "updateRepository ---->sql:" + sql.toString() );
			this.getDaoSupport().updateByNativeSql(sql);
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.updateRepository() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#confirmStorage(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, cafe.core.bean.identity.LogonUser)
	 */
	public void confirmStorage(String assetTransId, String status, String isTransDone, String historyId, LogonUser logonUser, String transAction) throws DataAccessException {
		try {
			//打印參數
			LOGGER.debug(this.getClass().getSimpleName() + "confirmStorage", "assetTransId:" + assetTransId);
			LOGGER.debug(this.getClass().getSimpleName() + "confirmStorage", "status:" + status);
			LOGGER.debug(this.getClass().getSimpleName() + "confirmStorage", "isTransDone:" + isTransDone);
			LOGGER.debug(this.getClass().getSimpleName() + "confirmStorage", "historyId:" + historyId);
			LOGGER.debug(this.getClass().getSimpleName() + "confirmStorage", "logonUser:" + logonUser);
			//得到schema
			String schema = this.getMySchema();
			//創建SqlStatement
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("exec dbo.usp_Asset_Tranfer_Out :assetTransId, :status, :isListDone, :isTransDone, :isCancel, :historyId, :userId, :userName, :transAction, ''");
			sqlQueryBean.setParameter("assetTransId", assetTransId);
			sqlQueryBean.setParameter("status", StringUtils.hasText(status)?status:"");
			sqlQueryBean.setParameter("isTransDone", isTransDone);
			sqlQueryBean.setParameter("isListDone", IAtomsConstants.NO);
			sqlQueryBean.setParameter("isCancel", IAtomsConstants.NO);
			sqlQueryBean.setParameter("historyId", historyId);
			sqlQueryBean.setParameter("userId", logonUser.getId());
			sqlQueryBean.setParameter("userName", logonUser.getName());
			sqlQueryBean.setParameter("transAction", transAction);
			LOGGER.debug(this.getClass().getName() + "saveRepository ---->sql:" + sqlQueryBean.toString() );
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.confirmStorage() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#confirmStorage(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, cafe.core.bean.identity.LogonUser)
	 */
	public void confirmStorageAll(String assetTransId, String status, String isTransDone, String historyId, LogonUser logonUser, String transAction, String transferSuccess) throws DataAccessException {
		try {
			//打印參數
			LOGGER.debug(this.getClass().getSimpleName() + "confirmStorage", "assetTransId:" + assetTransId);
			LOGGER.debug(this.getClass().getSimpleName() + "confirmStorage", "status:" + status);
			LOGGER.debug(this.getClass().getSimpleName() + "confirmStorage", "isTransDone:" + isTransDone);
			LOGGER.debug(this.getClass().getSimpleName() + "confirmStorage", "historyId:" + historyId);
			LOGGER.debug(this.getClass().getSimpleName() + "confirmStorage", "logonUser:" + logonUser);
			LOGGER.debug(this.getClass().getSimpleName() + "confirmStorage", "transAction:" + transAction);
			LOGGER.debug(this.getClass().getSimpleName() + "confirmStorage", "transferSuccess:" + transferSuccess);
			//得到schema
			String schema = this.getMySchema();
			//創建SqlStatement
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("exec dbo.usp_Asset_Tranfer_Out_All :assetTransId, :status, :isListDone, :isTransDone, :isCancel, :historyId, :userId, :userName, :transAction, '', '',:transferSuccess");
			sqlQueryBean.setParameter("assetTransId", assetTransId);
			sqlQueryBean.setParameter("status", StringUtils.hasText(status)?status:"");
			sqlQueryBean.setParameter("isTransDone", isTransDone);
			sqlQueryBean.setParameter("isListDone", IAtomsConstants.NO);
			sqlQueryBean.setParameter("isCancel", IAtomsConstants.NO);
			sqlQueryBean.setParameter("historyId", historyId);
			sqlQueryBean.setParameter("userId", logonUser.getId());
			sqlQueryBean.setParameter("userName", logonUser.getName());
			sqlQueryBean.setParameter("transAction", transAction);
			sqlQueryBean.setParameter("transferSuccess", transferSuccess);
			LOGGER.debug(this.getClass().getName() + "saveRepository ---->sql:" + sqlQueryBean.toString() );
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.confirmStorage() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#getBySerialNumber(java.lang.String)
	 */
	public DmmRepositoryDTO getBySerialNumber(String serialNumber)
			throws DataAccessException {
		List<DmmRepositoryDTO> dmmRepositoryDTOs = null;
		try {
			//打印參數
			LOGGER.debug(this.getClass().getSimpleName() + "getBySerialNumber", "serialNumber:" + serialNumber);
			//得到schema
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("repository.SERIAL_NUMBER", DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("repository.ASSET_TYPE_ID", DmmRepositoryDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			sqlStatement.addSelectClause("repository.STATUS", DmmRepositoryDTO.ATTRIBUTE.STATUS.getValue());
			sqlStatement.addSelectClause("repository.ASSET_ID", DmmRepositoryDTO.ATTRIBUTE.ASSET_ID.getValue());
			sqlStatement.addFromExpression(schema.concat(".DMM_REPOSITORY repository"));
			if (StringUtils.hasText(serialNumber)) {
				sqlStatement.addWhereClause("(repository.SERIAL_NUMBER = :serialNumber or repository.PROPERTY_ID = :serialNumber)");
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if (StringUtils.hasText(serialNumber)) {
				sqlQueryBean.setParameter(DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), serialNumber);
			}
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmRepositoryDTO.class);
			LOGGER.debug(this.getClass().getName() + "getBySerialNumber ---->sql:" + sqlQueryBean.toString() );
			dmmRepositoryDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(dmmRepositoryDTOs)) {
				return dmmRepositoryDTOs.get(0);
			}
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.getBySerialNumber() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		return null;
	}
	
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#saveRepositoryHist(java.lang.String, java.lang.String, cafe.core.bean.identity.LogonUser)
	 */
	@Override
	public void saveRepositoryHist(String assetId, String historyId) throws DataAccessException {
		try {
			//打印參數
			LOGGER.debug(this.getClass().getSimpleName() + "saveRepositoryHist", "assetId:" + assetId);
			LOGGER.debug(this.getClass().getSimpleName() + "saveRepositoryHist", "historyId:" + historyId);
			saveRepositoryHist(assetId, historyId, IAtomsConstants.MARK_EMPTY_STRING);
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.saveRepositoryhist() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#edcInstallNumReport(java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<CrossTabReportDTO> edcInstallNumReport(String status, String assetCategory, String orderBy) throws DataAccessException {
		
		List<CrossTabReportDTO> crossTabReportDTOs = null;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			
			if ("SHORT_NAME".equals(orderBy)) {
				sqlStatement.addSelectClause("dat.NAME", CrossTabReportDTO.ATTRIBUTE.COLUMN_NAME.getValue());
				sqlStatement.addSelectClause("count(1)", CrossTabReportDTO.ATTRIBUTE.INT_CONTENT.getValue());
				sqlStatement.addSelectClause("bcy.SHORT_NAME", CrossTabReportDTO.ATTRIBUTE.ROW_NAME.getValue());
			} else {
				sqlStatement.addSelectClause("bcy.SHORT_NAME", CrossTabReportDTO.ATTRIBUTE.COLUMN_NAME.getValue());
				sqlStatement.addSelectClause("count(1)", CrossTabReportDTO.ATTRIBUTE.INT_CONTENT.getValue());
				sqlStatement.addSelectClause("bd.DEPT_NAME", CrossTabReportDTO.ATTRIBUTE.ROW_NAME.getValue());
			}
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".DMM_REPOSITORY dr ");
			fromBuffer.append("left join ").append(schema).append(".DMM_ASSET_TYPE dat ON dat.ASSET_TYPE_ID = dr.ASSET_TYPE_ID ");
			fromBuffer.append("left join ").append(schema).append(".BIM_CONTRACT bct ON bct.CONTRACT_ID = dr.CONTRACT_ID ");
			fromBuffer.append("left join ").append(schema).append(".BIM_COMPANY bcy ON bcy.COMPANY_ID = bct.COMPANY_ID ");
			if (!"SHORT_NAME".equals(orderBy)) {
				fromBuffer.append("left join ").append(schema).append(".BIM_DEPARTMENT bd ON bd.DEPT_CODE = dr.INSTALLED_DEPT_ID ");
			}
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(status)) {
				sqlStatement.addWhereClause("dr.STATUS = :status", status);
			}
			if (StringUtils.hasText(assetCategory)) {
				sqlStatement.addWhereClause("dat.ASSET_CATEGORY = :assetCategory", assetCategory);
			}
			if ("SHORT_NAME".equals(orderBy)) {
				sqlStatement.setGroupByExpression("dat.NAME,bcy.SHORT_NAME");
				sqlStatement.setOrderByExpression("bcy.SHORT_NAME");
			} else {
				sqlStatement.addWhereClause("dr.INSTALLED_DEPT_ID is not null");
				sqlStatement.setGroupByExpression("bd.DEPT_NAME,bcy.SHORT_NAME");
				sqlStatement.setOrderByExpression("bd.DEPT_NAME");
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(CrossTabReportDTO.class);
			crossTabReportDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("edcInstallNumReport.saveRepositoryhist() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		return crossTabReportDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO#listByAsset(java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@Override
	public List<DmmRepositoryDTO> listByAsset(String chooseEdcAssetId,
			String chooseEdcAssetCategory, String chooseEdcSerialNumber, String chooseEdcCarrierOrBorrower, 
			String chooseEdcCarrierOrBorrowerComment, String chooseEdcWarehouseId, String chooseEdcDispatchProcessUser, 
			String chooseEdcCustomerId, Integer pageSize, Integer page, String order, String sort, List<String> selectSnsList) throws DataAccessException {
		List<DmmRepositoryDTO> dmmRepositoryDTOS = null;
		try{
			LOGGER.debug(".listByAsset()", "parameters:chooseEdcAssetId=" + chooseEdcAssetId);
			LOGGER.debug(".listByAsset()", "parameters:itemCategory=" + chooseEdcAssetCategory);
			LOGGER.debug(".listByAsset()", "row:", pageSize.intValue() );
			LOGGER.debug(".listByAsset()", "page:", page.intValue() );
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("repository.SERIAL_NUMBER", DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("repository.PROPERTY_ID", DmmRepositoryDTO.ATTRIBUTE.PROPERTY_ID.getValue());
			sqlStatement.addSelectClause("repository.WAREHOUSE_ID", DmmRepositoryDTO.ATTRIBUTE.WAREHOUSE_ID.getValue());
			sqlStatement.addSelectClause("repository.CONTRACT_ID", DmmRepositoryDTO.ATTRIBUTE.CONTRACT_ID.getValue());
			sqlStatement.addSelectClause("assetCategory.ITEM_NAME", DmmRepositoryDTO.ATTRIBUTE.ITEM_CATEGORY_NAME.getValue());
			sqlStatement.addSelectClause("assetType.NAME", DmmRepositoryDTO.ATTRIBUTE.ITEM_NAME.getValue());
			sqlStatement.addSelectClause("(case when repository.STATUS = :assetStatusInApply then (carrierUser.CNAME) else (borrowerUser.CNAME) end) ", DmmRepositoryDTO.ATTRIBUTE.EDC_PEOPLE.getValue());
			sqlStatement.addSelectClause(" repository.DESCRIPTION ", DmmRepositoryDTO.ATTRIBUTE.EDC_COMMENT.getValue());
			sqlStatement.addSelectClause("warehouse.NAME", DmmRepositoryDTO.ATTRIBUTE.WAREHOUSE_NAME.getValue());
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(".DMM_REPOSITORY repository ");
			buffer.append(" left join ").append(schema).append(".DMM_ASSET_TYPE assetType on repository.ASSET_TYPE_ID = assetType.ASSET_TYPE_ID ");
			buffer.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF assetCategory ON assetCategory.ITEM_VALUE = assetType.ASSET_CATEGORY and assetCategory.BPTD_CODE = :asset_category ");
			buffer.append(" left join ").append(schema).append(".BIM_WAREHOUSE warehouse on warehouse.WAREHOUSE_ID = repository.WAREHOUSE_ID ");
			buffer.append(" left join ").append(schema).append(".ADM_USER carrierUser on carrierUser.USER_ID = repository.CARRIER ");
			buffer.append(" left join ").append(schema).append(".ADM_USER borrowerUser on borrowerUser.USER_ID = repository.BORROWER ");
			sqlStatement.addFromExpression(buffer.toString());
			//設備序號
			if (StringUtils.hasText(chooseEdcSerialNumber)) {
				sqlStatement.addWhereClause("repository.SERIAL_NUMBER LIKE :chooseEdcSerialNumber", chooseEdcSerialNumber.concat(IAtomsConstants.MARK_PERCENT));
			}
			//倉庫
			if (StringUtils.hasText(chooseEdcWarehouseId)) {
				sqlStatement.addWhereClause("repository.WAREHOUSE_ID = :chooseEdcWarehouseId", chooseEdcWarehouseId);
			}
			//借用/領用人
			if (StringUtils.hasText(chooseEdcCarrierOrBorrower)) {
				sqlStatement.addWhereClause("(carrierUser.CNAME LIKE :chooseEdcCarrierOrBorrower or borrowerUser.CNAME LIKE :chooseEdcCarrierOrBorrower )");
			}
			//借用/領用說明
			if (StringUtils.hasText(chooseEdcCarrierOrBorrowerComment)) {
				sqlStatement.addWhereClause("(repository.DESCRIPTION LIKE :chooseEdcCarrierOrBorrowerComment )");
			}
			//設備名稱
			if (StringUtils.hasText(chooseEdcAssetId)) {
				sqlStatement.addWhereClause("repository.ASSET_TYPE_ID = :chooseEdcAssetId", chooseEdcAssetId);
			}
			//設備類別
			if (StringUtils.hasText(chooseEdcAssetCategory)) {
				sqlStatement.addWhereClause("assetType.ASSET_CATEGORY = :chooseEdcAssetCategory", chooseEdcAssetCategory);
			}
			if(!CollectionUtils.isEmpty(selectSnsList)) {
				sqlStatement.addWhereClause("repository.SERIAL_NUMBER not in ( :selectSnsList )");
			}
			//sqlStatement.addWhereClause(" caseInfo.CASE_ID = :caseId", caseId);
			sqlStatement.addWhereClause("( repository.STATUS = :assetStatusInApply OR repository.STATUS = :assetStatusBorrowing)");
			sqlStatement.addWhereClause("( repository.CARRIER = :chooseEdcDispatchProcessUser OR repository.BORROWER = :chooseEdcDispatchProcessUser)");
			sqlStatement.addWhereClause(" repository.ASSET_USER = :chooseEdcCustomerId ");
			if (StringUtils.hasText(sort) && StringUtils.hasText(order)) {
				StringBuffer stringBufferbuffer = new StringBuffer();
				stringBufferbuffer.append(sort).append(IAtomsConstants.MARK_SPACE).append(order);
				sqlStatement.setOrderByExpression(stringBufferbuffer.toString());
			} else {
				sqlStatement.setOrderByExpression("repository.SERIAL_NUMBER ASC ");
			}
			sqlStatement.setPageSize(pageSize.intValue());
			sqlStatement.setStartPage(page.intValue() - 1);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			// 設備狀態-領用中
			sqlQueryBean.setParameter("assetStatusInApply", IAtomsConstants.PARAM_ASSET_STATUS_IN_APPLY);
			// 設備狀態-借用中
			sqlQueryBean.setParameter("assetStatusBorrowing", IAtomsConstants.PARAM_ASSET_STATUS_BORROWING);
			// 設備類別
			sqlQueryBean.setParameter("asset_category", IATOMS_PARAM_TYPE.ASSET_CATEGORY.getCode());
			//派工人員
			sqlQueryBean.setParameter("chooseEdcDispatchProcessUser", chooseEdcDispatchProcessUser);
			//建案客戶
			sqlQueryBean.setParameter("chooseEdcCustomerId", chooseEdcCustomerId);
			if(!CollectionUtils.isEmpty(selectSnsList)) {
				sqlQueryBean.setParameter("selectSnsList", selectSnsList);
			}
			if(StringUtils.hasText(chooseEdcCarrierOrBorrower)) {
				sqlQueryBean.setParameter("chooseEdcCarrierOrBorrower", chooseEdcCarrierOrBorrower + IAtomsConstants.MARK_PERCENT);
			}
			if(StringUtils.hasText(chooseEdcCarrierOrBorrowerComment)) {
				sqlQueryBean.setParameter("chooseEdcCarrierOrBorrowerComment", chooseEdcCarrierOrBorrowerComment + IAtomsConstants.MARK_PERCENT);
			}
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmRepositoryDTO.class);
			LOGGER.debug(this.getClass().getName() + ".listByAsset()", "sql:" + sqlQueryBean.toString());
			dmmRepositoryDTOS = super.getDaoSupport().findByNativeSql(sqlQueryBean,aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+":listByAsset() is error." + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return dmmRepositoryDTOS;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#saveRepositoryHistInCaseAssetLink(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void saveRepositoryHist(String assetId,String historyId, String status) throws DataAccessException {
		try {
			//打印參數
			LOGGER.debug(this.getClass().getSimpleName() + "saveRepositoryHist", "assetId:" + assetId);
			LOGGER.debug(this.getClass().getSimpleName() + "saveRepositoryHist", "historyId:" + historyId);
			LOGGER.debug(this.getClass().getSimpleName() + "saveRepositoryHist", "status:" + status);
			//創建SqlStatement
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("exec dbo.usp_Repository_CopyTo_History :assetId, :historyId, :status");
			sqlQueryBean.setParameter("assetId", assetId);
			sqlQueryBean.setParameter("historyId", historyId);
			sqlQueryBean.setParameter("status", status);
			LOGGER.debug(this.getClass().getName() + "saveRepositoryHist ---->sql:" + sqlQueryBean.toString() );
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.saveRepositoryHist() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
	}
	
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#saveRepositoryByBorrowZip(java.lang.String, java.sql.Timestamp, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void saveRepositoryByBorrowZip(String assetIds, Timestamp nowTime, String userId, String userName, String historyId) throws DataAccessException {
		try {
			
			//創建SqlStatement
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("exec dbo.usp_RepositoryAssetBorrowZip :assetIds, :nowTime, :userId, :userName, :historyId");
			sqlQueryBean.setParameter("assetIds", assetIds);
			sqlQueryBean.setParameter("nowTime", nowTime);
			sqlQueryBean.setParameter("userId", userId);
			sqlQueryBean.setParameter("userName", userName);
			sqlQueryBean.setParameter("historyId", historyId);
			LOGGER.debug(this.getClass().getName() + "saveRepositoryHist ---->sql:" + sqlQueryBean.toString() );
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.saveRepositoryByBorrowZip() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#assetValidateByProcedure(java.lang.String, java.lang.String)
	 */
	@Override
	public Object[] assetValidateByProcedure(String action, String assetIds) throws DataAccessException{
		Object[] objects = null;
		try {
			//創建SqlStatement
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("declare @output varchar(200) declare @flag varchar(1) exec dbo.usp_RepositoryAssetValidate :actionId,:assetIds, @output output, @flag output");
			sqlQueryBean.append("select @output as output, @flag as flag ");
			sqlQueryBean.setParameter("actionId", StringUtils.hasText(action) ? action : "");
			sqlQueryBean.setParameter("assetIds", StringUtils.hasText(assetIds) ? assetIds : "");
			LOGGER.debug(this.getClass().getName() + "SQL---------->" + sqlQueryBean.toString());
			List list = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if(!CollectionUtils.isEmpty(list)){
				objects = (Object[]) list.get(0);
			}
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.saveRepositoryByProcedure() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		return objects;
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#saveRepositoryByProcedure(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.sql.Date, java.sql.Date, java.lang.String, java.lang.String, java.sql.Timestamp)
	 */
	@Override
	public Object[] saveRepositoryByProcedure(String action, String assetIds, String transactionId, String historyId,
			String userId, String userName, String maintainCompany, java.util.Date analyzeDate, Timestamp caseCompletionDate,
			String maintainUser, String description,String flag, Timestamp nowTime, String nowStatus, String faultComponent, 
			String repairVendor, String faultDescription, String carrier, Date carryDate, String borrower,
			Date borrowerEnd, String borrowerMgrEmail, String borrowerEmail, Date borrowerStart, Date enableDate, String isEnabled,
			String dtid, String caseId, String merchantId, String merchantHeaderId, String installedAdress,
			String installedAdressLocation, String IsCup, String retireReasonCode) throws DataAccessException {
		Object[] objects = null;
		try {
			//打印參數
			//LOGGER.debug(this.getClass().getSimpleName() + "saveRepositoryHist", "assetId:" + assetId);
			//LOGGER.debug(this.getClass().getSimpleName() + "saveRepositoryHist", "historyId:" + historyId);
			//LOGGER.debug(this.getClass().getSimpleName() + "saveRepositoryHist", "status:" + status);
			//創建SqlStatement
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			
			sqlQueryBean.append("declare @output varchar(200) declare @flag varchar(1) exec dbo.usp_RepositoryAssetManage :actionName, :assetIds, :transactionId, "+
						":historyId, :userId, :userName, :maintainCompany, :analyzeDate,"+
						":caseCompletionDate, :maintainUser, :description, :nowTime,"+
						":nowStatus, :faultComponent, :repairVendor, :faultDescription, :carrier, :carryDate, :borrower, "+
						":borrowerEnd, :borrowerMgrEmail, :borrowerEmail, :borrowerStart, :enableDate, :isEnabled,"+
						":dtid, :caseId, :merchantId, :merchantHeaderId, :installedAdress,"+
						":installedAdressLocation, :IsCup, :retireReasonCode, @output output, @flag output");
			sqlQueryBean.append("select @output as output, @flag as flag ");
			sqlQueryBean.setParameter("actionName", StringUtils.hasText(action) ? action : "");
			sqlQueryBean.setParameter("assetIds", StringUtils.hasText(assetIds) ? assetIds : "");
			sqlQueryBean.setParameter("transactionId", StringUtils.hasText(transactionId) ? transactionId : "");
			sqlQueryBean.setParameter("historyId", StringUtils.hasText(historyId) ? historyId : "");
			sqlQueryBean.setParameter("userId", StringUtils.hasText(userId) ? userId : "");
			sqlQueryBean.setParameter("userName", StringUtils.hasText(userName) ? userName : "");
			sqlQueryBean.setParameter("maintainCompany", StringUtils.hasText(maintainCompany) ? maintainCompany : "");
			sqlQueryBean.setParameter("analyzeDate", analyzeDate!=null?analyzeDate:"");
			sqlQueryBean.setParameter("caseCompletionDate", caseCompletionDate!=null?caseCompletionDate:"");
			sqlQueryBean.setParameter("maintainUser", StringUtils.hasText(maintainUser) ? maintainUser : "");
			sqlQueryBean.setParameter("description", StringUtils.hasText(description) ? description : "");
			sqlQueryBean.setParameter("nowTime", nowTime);
			sqlQueryBean.setParameter("nowStatus", StringUtils.hasText(nowStatus) ? nowStatus : "");
			sqlQueryBean.setParameter("faultComponent",  StringUtils.hasText(faultComponent) ? faultComponent : "");
			sqlQueryBean.setParameter("repairVendor",  StringUtils.hasText(repairVendor) ? repairVendor : "");
			sqlQueryBean.setParameter("faultDescription",  StringUtils.hasText(faultDescription) ? faultDescription : "");
			sqlQueryBean.setParameter("carrier",  StringUtils.hasText(carrier) ? carrier : "");
			sqlQueryBean.setParameter("carryDate", carryDate!=null?carryDate:"");
			sqlQueryBean.setParameter("borrower",  StringUtils.hasText(borrower) ? borrower : "");
			sqlQueryBean.setParameter("borrowerEnd",  borrowerEnd!=null?borrowerEnd:"");
			sqlQueryBean.setParameter("borrowerMgrEmail",  StringUtils.hasText(borrowerMgrEmail) ? borrowerMgrEmail : "");
			sqlQueryBean.setParameter("borrowerEmail",  StringUtils.hasText(borrowerEmail) ? borrowerEmail : "");
			sqlQueryBean.setParameter("borrowerStart",  borrowerStart!=null?borrowerStart:"");
			sqlQueryBean.setParameter("borrowerEmail",  StringUtils.hasText(borrowerEmail) ? borrowerEmail : "");
			
			sqlQueryBean.setParameter("enableDate",   enableDate!=null?enableDate:"");
			sqlQueryBean.setParameter("isEnabled",  StringUtils.hasText(isEnabled) ? isEnabled : "");
			sqlQueryBean.setParameter("dtid",  StringUtils.hasText(dtid) ? dtid : "");
			sqlQueryBean.setParameter("caseId",  StringUtils.hasText(caseId) ? caseId : "");
			sqlQueryBean.setParameter("merchantId",  StringUtils.hasText(merchantId) ? merchantId : "");
			sqlQueryBean.setParameter("merchantHeaderId",  StringUtils.hasText(merchantHeaderId) ? merchantHeaderId : "");
			sqlQueryBean.setParameter("installedAdress",  StringUtils.hasText(installedAdress) ? installedAdress : "");
			sqlQueryBean.setParameter("installedAdressLocation",  StringUtils.hasText(installedAdressLocation) ? installedAdressLocation : "");
			sqlQueryBean.setParameter("IsCup",  StringUtils.hasText(IsCup) ? IsCup : "");
			sqlQueryBean.setParameter("retireReasonCode",  StringUtils.hasText(retireReasonCode) ? retireReasonCode : "");

			LOGGER.debug(this.getClass().getName() + "SQL---------->" + sqlQueryBean.toString());
			List list = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if(!CollectionUtils.isEmpty(list)){
				objects = (Object[]) list.get(0);
			}
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.saveRepositoryByProcedure() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		return objects;
	}
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#isNoData()
	 */
	@Override
	public Integer countData() throws DataAccessException {
		Integer count = 0;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("COUNT(1)");
			sqlStatement.addFromExpression(schema + ".DMM_REPOSITORY");
			//打印SQL語句
			LOGGER.debug("DmmRepositoryDAO.countData()", "SQL : ", sqlStatement.toString());
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				count = result.get(0);
				
			}
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.countData()", "error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE);
		}
		return count;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#listByAssetId(java.lang.String)
	 */
	public List<DmmRepositoryDTO> listByAssetId(List<String> assetId) throws DataAccessException {
		try {
			//打印參數
			LOGGER.debug(this.getClass().getSimpleName() + "saveRepositoryHist", "assetId:" + assetId);
			//創建SqlStatement
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("ASSET_ID", DmmRepositoryDTO.ATTRIBUTE.ASSET_ID.getValue());
			sqlStatement.addSelectClause("CHECKED_DATE", DmmRepositoryDTO.ATTRIBUTE.CHECKED_DATE.getValue());
			sqlStatement.addSelectClause("CYBER_APPROVED_DATE", DmmRepositoryDTO.ATTRIBUTE.CYBER_APPROVED_DATE.getValue());
			sqlStatement.addSelectClause("MA_TYPE", DmmRepositoryDTO.ATTRIBUTE.MA_TYPE.getValue());
			sqlStatement.addFromExpression(schema + ".DMM_REPOSITORY");
			sqlStatement.addWhereClause("ASSET_ID in ( :assetId )");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmRepositoryDTO.class);
			sqlQueryBean.setParameter("assetId", assetId);
			LOGGER.debug(this.getClass().getName() + "listByAssetId ---->sql:" + sqlQueryBean.toString() );
			List<DmmRepositoryDTO> dmmRepositoryDTOS = super.getDaoSupport().findByNativeSql(sqlQueryBean,aliasBean);
			return dmmRepositoryDTOS;
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.saveRepositoryHistInCaseAssetLink() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#deleteTransferData()
	 */
	@Override
	public void deleteTransferData() throws DataAccessException {
		try {
			String schema = this.getMySchema();
			SqlQueryBean sqlQueryBean = new SqlQueryBean("delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY_DESC; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY_COMM; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_HISTORY; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_FAULT_DESC; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY_FAULT_COM; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".DMM_REPOSITORY; ");
			
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
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#checkAssetUserIsTaixinRent(java.util.List)
	 */
	/*public List<DmmRepositoryDTO> checkAssetUserIsTaixinRent(
			List<String> serialNumbers) throws DataAccessException {
		try{
			//打印參數
			LOGGER.debug(this.getClass().getSimpleName() + "checkAssetUserIsTaixinRent", "assetId:" + serialNumbers.toString());
			//創建SqlStatement
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("company.SHORT_NAME", DmmRepositoryDTO.ATTRIBUTE.ASSET_USER_NAME.getValue());
			StringBuffer formBuffer = new StringBuffer();
			formBuffer.append(schema).append(".DMM_REPOSITORY repository");
			formBuffer.append("left join ").append(schema).append(".BIM_COMPANY company on company.COMPANY_ID = repository.ASSET_USER");
			sqlStatement.addFromExpression(formBuffer.toString());
			sqlStatement.addWhereClause("SERIAL_NUMBER in ( :serialNumber )");
			sqlStatement.addWhereClause("company.COMPANY_CODE != :companyCode", IAtomsConstants.PARAM_TSB_EDC);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter(DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), serialNumbers);
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmRepositoryDTO.class);
			LOGGER.debug(this.getClass().getName() + "listByAssetId ---->sql:" + sqlQueryBean.toString() );
			List<DmmRepositoryDTO> dmmRepositoryDTOS = super.getDaoSupport().findByNativeSql(sqlQueryBean,aliasBean);
			return dmmRepositoryDTOS;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}*/
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#getAssetIdList(com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetManageFormDTO)
	 */
	@Override
	public List<String> getAssetIdList(AssetManageFormDTO assetManageFormDTO) throws DataAccessException {
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.userId:'" + assetManageFormDTO.getUserId() + "'");
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.companyId:'" + assetManageFormDTO.getCompanyId() + "'");
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.queryUser:'" + assetManageFormDTO.getQueryUser() + "'");
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.action:'" + assetManageFormDTO.getQueryAction() + "'");
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.queryAssetCategory:'" + assetManageFormDTO.getQueryAssetCategory() + "'");
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.warehouseId:'" + assetManageFormDTO.getStorage() + "'");
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.assetId:'" + assetManageFormDTO.getQueryAssetId() + "'");
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.merRegisteredName:'" + assetManageFormDTO.getQueryMerName() + "'");
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.merAnnouncedName:'" + assetManageFormDTO.getQueryHeaderName() + "'");
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.mid:'" + assetManageFormDTO.getQueryMerchantCode() + "'");
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.merInstallAddress:'" + assetManageFormDTO.getQueryMerInstallAddress() + "'");
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.area:'" + assetManageFormDTO.getQueryArea() + "'");
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.contractId:'" + assetManageFormDTO.getQueryContractId() + "'");
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.queryMaType:'" + assetManageFormDTO.getQueryMaType() + "'");
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.isEnabled:'" + assetManageFormDTO.getQueryIsEnabled() + "'");
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.name:'" + assetManageFormDTO.getAssetTypeName() + "'");
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.keeperName:'" + assetManageFormDTO.getQueryKeeperName() + "'");
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.tid:'" + assetManageFormDTO.getQueryTid() + "'");
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.assetOwner:'" + assetManageFormDTO.getQueryAssetOwner() + "'");
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.assetUser:'" + assetManageFormDTO.getQueryAssetUser() + "'");
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.dtid:'" + assetManageFormDTO.getQueryDtid() + "'");
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.queryPropertyIds:'" + assetManageFormDTO.getQueryPropertyIds() + "'");
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.querySerialNumbers:'" + assetManageFormDTO.getQuerySerialNumbers() + "'");
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.queryIsCup:'" + assetManageFormDTO.getQueryIsCup() + "'");
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.afterTicketCompletionDate:'" + assetManageFormDTO.getAfterTicketCompletionDate() + "'");
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.beforeTicketCompletionDate:'" + assetManageFormDTO.getBeforeTicketCompletionDate() + "'");
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.afterUpdateDate:'" + assetManageFormDTO.getAfterUpdateDate() + "'");
		LOGGER.debug("DmmRepositoryDAO.getAssetIdList.beforeUpdateDate:'" + assetManageFormDTO.getBeforeUpdateDate() + "'");
		try {
			List<String> repositoryDTOs = null;
			String schema = this.getMySchema();
			SqlStatement sql = new SqlStatement();
			sql.addSelectClause("r.ASSET_ID", DmmRepositoryDTO.ATTRIBUTE.ASSET_ID.getValue()); 
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema);
			fromBuffer.append(".DMM_REPOSITORY r left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".DMM_ASSET_TYPE a on r.ASSET_TYPE_ID=a.ASSET_TYPE_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BASE_PARAMETER_ITEM_DEF b on r.STATUS=b.ITEM_VALUE AND b.BPTD_CODE =:assetStatus left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_COMPANY c on c.COMPANY_ID=r.MAINTAIN_COMPANY left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_CONTRACT bc on bc.CONTRACT_ID=r.CONTRACT_ID ");
			fromBuffer.append(" left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BASE_PARAMETER_ITEM_DEF pp ON r.MA_TYPE = pp.ITEM_VALUE AND pp.BPTD_CODE =:maType left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_WAREHOUSE w on r.WAREHOUSE_ID=w.WAREHOUSE_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_COMPANY co on r.ASSET_OWNER=co.COMPANY_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_COMPANY compa on w.COMPANY_ID=compa.COMPANY_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_COMPANY  cp on r.ASSET_USER=cp.COMPANY_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_MERCHANT bt on r.MERCHANT_ID = bt.MERCHANT_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_MERCHANT_HEADER header on header.MERCHANT_HEADER_ID = r.MERCHANT_HEADER_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".PVM_APPLICATION p on r.APPLICATION_ID = p.APPLICATION_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BASE_PARAMETER_ITEM_DEF pc ON (substring(r.ACTION,charindex('-',r.ACTION)+1,LEN(r.ACTION))) = pc.ITEM_VALUE AND pc.BPTD_CODE = :ACTION left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BASE_PARAMETER_ITEM_DEF pid ON r.FAULT_COMPONENT = pid.ITEM_VALUE AND pid.BPTD_CODE = :FAULT_COMPONENT left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BASE_PARAMETER_ITEM_DEF par ON r.FAULT_DESCRIPTION = par.ITEM_VALUE AND par.BPTD_CODE = :FAULT_DESCRIPTION LEFT JOIN ");
			//fromBuffer.append(schema);
			//fromBuffer.append(".DMM_ASSET_IN_LIST al on r.SERIAL_NUMBER = al.SERIAL_NUMBER left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_COMPANY  com on r.REPAIR_VENDOR=com.COMPANY_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_COMPANY  comp on r.MAINTAIN_COMPANY=comp.COMPANY_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".ADM_USER us on us.USER_ID=r.BORROWER left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".ADM_USER adm on adm.USER_ID=r.CARRIER left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".ADM_USER  maintainUser on maintainUser.USER_ID=r.MAINTAIN_USER left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_DEPARTMENT dept on dept.DEPT_CODE=r.DEPARTMENT_ID left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BASE_PARAMETER_ITEM_DEF baseparam on r.INSTALLED_ADRESS_LOCATION=baseparam.ITEM_VALUE and baseparam.BPTD_CODE =:headerArea left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BASE_PARAMETER_ITEM_DEF installType on r.INSTALL_TYPE=installType.ITEM_VALUE and installType.BPTD_CODE =:INSTALL_TYPE left join ");
			fromBuffer.append(schema);
			fromBuffer.append(".BIM_COMPANY repairVendor on r.REPAIR_COMPANY=repairVendor.COMPANY_ID ");

			if (StringUtils.hasText(assetManageFormDTO.getUserId())) {
				fromBuffer.append(" left join ");
				fromBuffer.append(schema).append(".ADM_USER user1 on user1.USER_ID =:userId ");
			}
			sql.setFromExpression(fromBuffer.toString());
			if (assetManageFormDTO.getRows() != null) {
				sql.setPageSize(assetManageFormDTO.getRows());
				sql.setStartPage(assetManageFormDTO.getPage() - 1);
			}
			StringBuffer buffer = new StringBuffer();
			
			if (StringUtils.hasText(assetManageFormDTO.getUserId())) {
				sql.addWhereClause(" ( exists (select 1 from " + schema + ".ADM_USER_WAREHOUSE uWarehouse where uWarehouse.USER_ID = user1.USER_ID and uWarehouse.WAREHOUSE_ID = r.WAREHOUSE_ID))");
			}
			//dtid
			if (StringUtils.hasText(assetManageFormDTO.getQueryDtid())) {
				sql.addWhereClause("r.DTID like :dtid", assetManageFormDTO.getQueryDtid() + IAtomsConstants.MARK_PERCENT);
			}
			
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetIds())) {
				String assetIds=assetManageFormDTO.getQueryAssetIds();
				// 若輸入中文逗號替換為英文逗號
				assetIds = assetIds.replaceAll("，", ",");
				buffer = new StringBuffer();
				String [] assetId = assetIds.split(",");
				buffer.append(" r.ASSET_ID in(");
				for (int i = 0; i < assetId.length - 1; i++) {
					if (StringUtils.hasText(assetId[i])) {
						buffer.append("'" + assetId[i] + "',");
					}
				}
				buffer.append("'" + assetId[assetId.length - 1] + "'");
				buffer.append(" ) ");
				sql.addWhereClause(buffer.toString());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetIdList())) {
				String assetIds=assetManageFormDTO.getQueryAssetIdList();
				// 若輸入中文逗號替換為英文逗號
				assetIds = assetIds.replaceAll("，", ",");
				buffer = new StringBuffer();
				String [] assetId = assetIds.split(",");
				buffer.append("r.ASSET_ID in(");
				for (int i = 0; i < assetId.length - 1; i++) {
					if (StringUtils.hasText(assetId[i])) {
						buffer.append("'" + assetId[i] + "',");
					}
				}
				buffer.append("'" + assetId[assetId.length - 1] + "'");
				buffer.append(" ) ");
				sql.addWhereClause(buffer.toString());
			}
			String serialNumberCodeGun = null;
			if (StringUtils.hasText(assetManageFormDTO.getQuerySerialNumbers())) {
				String serialNumbers=assetManageFormDTO.getQuerySerialNumbers();
				// 若輸入中文逗號替換為英文逗號
				serialNumbers = serialNumbers.replaceAll("，", ","); 
				String [] serialNumber = serialNumbers.split(",");
				buffer = new StringBuffer();
				buffer.append("r.SERIAL_NUMBER in(");
				serialNumberCodeGun = "'";
				for (int i = 0; i < serialNumber.length - 1; i++) {
					if (StringUtils.hasText(serialNumber[i])) {
						buffer.append("'" + serialNumber[i] + "',");
						serialNumberCodeGun = serialNumberCodeGun + ("*" + serialNumber[i] + "*,");
					}
				}
				buffer.append("'" + serialNumber[serialNumber.length - 1] + "'");
				serialNumberCodeGun = serialNumberCodeGun + "*" + serialNumber[serialNumber.length - 1] + "*')";
				buffer.append(" ) ");
				sql.addWhereClause(buffer.toString());
			}
			String propertyIdCodeGun = null;
			//財產編號
			if (StringUtils.hasText(assetManageFormDTO.getQueryPropertyIds())) {
				String propertyIds=assetManageFormDTO.getQueryPropertyIds();
				// 若輸入中文逗號替換為英文逗號
				propertyIds = propertyIds.replaceAll("，", ",");
				buffer = new StringBuffer();
				String [] propertyId = propertyIds.split(",");
				buffer.append(" r.PROPERTY_ID in(");
				propertyIdCodeGun = "'";
				for (int i = 0; i < propertyId.length - 1; i++) {
					if (StringUtils.hasText(propertyId[i])) {
						buffer.append("'" + propertyId[i] + "',");
						propertyIdCodeGun = propertyIdCodeGun + ("*" + propertyId[i] + "*,");

					}
				}
				buffer.append("'" + propertyId[propertyId.length - 1] + "'");
				propertyIdCodeGun = propertyIdCodeGun + "*" + propertyId[propertyId.length - 1] + "*')";

				buffer.append(" ) ");
				sql.addWhereClause(buffer.toString());
			}
			//設備類型//Task #2991
			if (StringUtils.hasText(assetManageFormDTO.getAssetTypeName())) {
				String assetTypeIds=assetManageFormDTO.getAssetTypeName();
				String [] assetTypeId = assetTypeIds.split(",");
				StringBuffer serialBuffer = new StringBuffer();
				for (int i = 0; i < assetTypeId.length - 1; i++) {
					if (StringUtils.hasText(assetTypeId[i])) {
						serialBuffer.append("'" + assetTypeId[i] + "',");
					}
				}
				serialBuffer.append("'" + assetTypeId[assetTypeId.length - 1] + "'");
				serialBuffer.append(" ) ");
				sql.addWhereClause("r.ASSET_TYPE_ID in(" + serialBuffer);
				//sql.addWhereClause("r.ASSET_TYPE_ID =:name", assetManageFormDTO.getAssetTypeName());
			}
			//動作
			if (StringUtils.hasText(assetManageFormDTO.getQueryAction())) {
				String actions=assetManageFormDTO.getQueryAction();
				String [] action = actions.split(",");
				StringBuffer serialBuffer = new StringBuffer();
				for (int i = 0; i < action.length - 1; i++) {
					if (StringUtils.hasText(action[i])) {
						serialBuffer.append("'" + action[i] + "',");
					}
				}
				serialBuffer.append("'" + action[action.length - 1] + "'");
				serialBuffer.append(" ) ");
				sql.addWhereClause("r.ACTION in(" + serialBuffer);
				//sql.addWhereClause("r.ACTION =:action", assetManageFormDTO.getQueryAction());
			}
			//使用人
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetUser())) {
				String assetUsers=assetManageFormDTO.getQueryAssetUser();
				String [] assetUser = assetUsers.split(",");
				StringBuffer serialBuffer = new StringBuffer();
				for (int i = 0; i < assetUser.length - 1; i++) {
					if (StringUtils.hasText(assetUser[i])) {
						serialBuffer.append("'" + assetUser[i] + "',");
					}
				}
				serialBuffer.append("'" + assetUser[assetUser.length - 1] + "'");
				serialBuffer.append(" ) ");
				sql.addWhereClause("r.ASSET_USER in(" + serialBuffer);
				//sql.addWhereClause("r.ASSET_USER = :assetUser", assetManageFormDTO.getQueryAssetUser());
			} else {
				//Task #3046
				if (StringUtils.hasText(assetManageFormDTO.getHideQueryAssetUser())) {
					sql.addWhereClause("r.ASSET_USER = :assetUser", assetManageFormDTO.getHideQueryAssetUser());
				}
			}
			//櫃位
			if (StringUtils.hasText(assetManageFormDTO.getQueryCounter())) {
				sql.addWhereClause("r.COUNTER like :counter", assetManageFormDTO.getQueryCounter() + IAtomsConstants.MARK_PERCENT);
			}
			//箱號
			if (StringUtils.hasText(assetManageFormDTO.getQueryCartonNo())) {
				sql.addWhereClause("r.CARTON_NO like :cartonNo", assetManageFormDTO.getQueryCartonNo() + IAtomsConstants.MARK_PERCENT);
			}
			//合約編號
			if (StringUtils.hasText(assetManageFormDTO.getQueryContractId())) {
				sql.addWhereClause("r.CONTRACT_ID = :contractId", assetManageFormDTO.getQueryContractId());
			}
			//狀態
			if (StringUtils.hasText(assetManageFormDTO.getQueryStatus())) {
				sql.addWhereClause("r.STATUS =:status", assetManageFormDTO.getQueryStatus());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryUser())) {
				sql.addWhereClause("bc.COMPANY_ID =:queryUser", assetManageFormDTO.getQueryUser());
			}
			//類別
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetCategory())) {
				sql.addWhereClause("a.ASSET_CATEGORY =:queryAssetCategory", assetManageFormDTO.getQueryAssetCategory());
			}			
			//倉庫
			if (StringUtils.hasText(assetManageFormDTO.getQueryStorage())) {
				sql.addWhereClause("r.WAREHOUSE_ID =:warehouseId", assetManageFormDTO.getQueryStorage());
			}
			//設備編號
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetId())) {
				sql.addWhereClause("r.ASSET_ID = :assetId", assetManageFormDTO.getQueryAssetId());
			}
			//特店名稱
			if (StringUtils.hasText(assetManageFormDTO.getQueryMerName())) {
				sql.addWhereClause("bt.NAME like :merRegisteredName", assetManageFormDTO.getQueryMerName() + IAtomsConstants.MARK_PERCENT);
			}
			//特店表頭
			if (StringUtils.hasText(assetManageFormDTO.getQueryHeaderName())) {
				sql.addWhereClause("header.HEADER_NAME like :merAnnouncedName", assetManageFormDTO.getQueryHeaderName() + IAtomsConstants.MARK_PERCENT);
			}
			//特店代號
			if (StringUtils.hasText(assetManageFormDTO.getQueryMerchantCode())) {
				sql.addWhereClause("bt.MERCHANT_CODE like :mid", assetManageFormDTO.getQueryMerchantCode() + IAtomsConstants.MARK_PERCENT);
			} 
			//裝機地址
			if (StringUtils.hasText(assetManageFormDTO.getQueryMerInstallAddress())) {
				sql.addWhereClause("r.INSTALLED_ADRESS like :merInstallAddress", assetManageFormDTO.getQueryMerInstallAddress() + IAtomsConstants.MARK_PERCENT);
			}
			//裝機類別
			if (StringUtils.hasText(assetManageFormDTO.getQueryMerInstallAddress())) {
				sql.addWhereClause("r.INSTALL_TYPE like :merInstallAddress", assetManageFormDTO.getQueryMerInstallAddress() + IAtomsConstants.MARK_PERCENT);
			}
			//裝機區域
			if (StringUtils.hasText(assetManageFormDTO.getQueryArea())) {
				sql.addWhereClause("r.INSTALLED_ADRESS_LOCATION like :area", assetManageFormDTO.getQueryArea() + IAtomsConstants.MARK_PERCENT);
			}
			//維護模式
			if (StringUtils.hasText(assetManageFormDTO.getQueryMaType())) {
				sql.addWhereClause("r.MA_TYPE = :queryMaType", assetManageFormDTO.getQueryMaType());
			}
			//是否啟用
			if (StringUtils.hasText(assetManageFormDTO.getQueryIsEnabled())) {
				sql.addWhereClause("r.IS_ENABLED = :isEnabled", assetManageFormDTO.getQueryIsEnabled());
			}
			//保管人
			if (StringUtils.hasText(assetManageFormDTO.getQueryKeeperName())) {
				sql.addWhereClause("r.KEEPER_NAME like :keeperName", assetManageFormDTO.getQueryKeeperName() + IAtomsConstants.MARK_PERCENT);
			}
			//tid
			if (StringUtils.hasText(assetManageFormDTO.getQueryTid())) {
				sql.addWhereClause("r.TID like :tid", assetManageFormDTO.getQueryTid() + IAtomsConstants.MARK_PERCENT);
			}
			//資產owner
			if (StringUtils.hasText(assetManageFormDTO.getQueryAssetOwner())) {
				sql.addWhereClause("r.ASSET_OWNER = :assetOwner", assetManageFormDTO.getQueryAssetOwner());
			}
			if (StringUtils.hasText(assetManageFormDTO.getQueryIsCup())) {
				sql.addWhereClause("r.IS_CUP =:queryIsCup", assetManageFormDTO.getQueryIsCup());
			}

			if (StringUtils.hasText(assetManageFormDTO.getBeforeTicketCompletionDate())) {
				sql.addWhereClause("CONVERT(varchar(10),r.CASE_COMPLETION_DATE,111)>=:beforeTicketCompletionDate", assetManageFormDTO.getBeforeTicketCompletionDate());
			}
			if (StringUtils.hasText(assetManageFormDTO.getAfterTicketCompletionDate())) {
				sql.addWhereClause("CONVERT(varchar(10),r.CASE_COMPLETION_DATE,111)<=:afterTicketCompletionDate", assetManageFormDTO.getAfterTicketCompletionDate());
			}
			if (StringUtils.hasText(assetManageFormDTO.getBeforeUpdateDate())) {
				sql.addWhereClause("CONVERT(varchar(10),r.UPDATE_DATE,111)>=:beforeUpdateDate", assetManageFormDTO.getBeforeUpdateDate());
			} 
			if (StringUtils.hasText(assetManageFormDTO.getAfterUpdateDate())) {
				sql.addWhereClause("CONVERT(varchar(10),r.UPDATE_DATE,111)<=:afterUpdateDate", assetManageFormDTO.getAfterUpdateDate());
			}
			//Task #3127
			if (StringUtils.hasText(assetManageFormDTO.getQueryModel())) {
				sql.addWhereClause("r.ASSET_MODEL =:queryModel", assetManageFormDTO.getQueryModel());
			}
			if (DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue().equals(assetManageFormDTO.getCodeGunFlag())) {
				sql.setOrderByExpression("CHARINDEX('*'+Rtrim(cast(r.SERIAL_NUMBER as varchar(20))+'*'), " + " " + serialNumberCodeGun);
			} else if (DmmRepositoryDTO.ATTRIBUTE.PROPERTY_ID.getValue().equals(assetManageFormDTO.getCodeGunFlag())) {
				sql.setOrderByExpression("CHARINDEX('*'+Rtrim(cast(r.PROPERTY_ID as varchar(20))+'*'), " + " " + propertyIdCodeGun);
			} else if ((StringUtils.hasText(assetManageFormDTO.getSort())) && (StringUtils.hasText(assetManageFormDTO.getOrder()))) {
				sql.setOrderByExpression(assetManageFormDTO.getSort() + " " + assetManageFormDTO.getOrder());
			} else {
				sql.setOrderByExpression(AssetManageFormDTO.PARAM_PAGE_SORT + " " + IAtomsConstants.PARAM_PAGE_ORDER);
			}
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			sqlQueryBean.setParameter("assetStatus", IAtomsConstants.PARAM_ASSET_STATUS);
			sqlQueryBean.setParameter("maType", IAtomsConstants.PARAM_MA_TYPE);
			//sqlQueryBean.setParameter("repertory", IAtomsConstants.PARAM_ASSET_STATUS_REPERTORY);
			//sqlQueryBean.setParameter("repair", IAtomsConstants.PARAM_ASSET_STATUS_REPAIR);
			//sqlQueryBean.setParameter("disabled", IAtomsConstants.PARAM_ASSET_STATUS_DISABLED);
			//sqlQueryBean.setParameter("destroy", IAtomsConstants.PARAM_ASSET_STATUS_DESTROY);
			//sqlQueryBean.setParameter("borrowing", IAtomsConstants.PARAM_ASSET_STATUS_BORROWING);
			//sqlQueryBean.setParameter("pendingDisabled", IAtomsConstants.PARAM_ASSET_STATUS_PENDING_DISABLED);
			if (StringUtils.hasText(assetManageFormDTO.getUserId())) {
				sqlQueryBean.setParameter("userId", assetManageFormDTO.getUserId());
			}
			//sqlQueryBean.setParameter("inApply", IAtomsConstants.PARAM_ASSET_STATUS_IN_APPLY);
			//sqlQueryBean.setParameter("inUse", IAtomsConstants.PARAM_ASSET_STATUS_IN_USE);
			//sqlQueryBean.setParameter("maintenance", IAtomsConstants.PARAM_ASSET_STATUS_MAINTENANCE);
			sqlQueryBean.setParameter("FAULT_COMPONENT", IATOMS_PARAM_TYPE.FAULT_COMPONENT.getCode());
			sqlQueryBean.setParameter("FAULT_DESCRIPTION", IATOMS_PARAM_TYPE.FAULT_DESCRIPTION.getCode());
			sqlQueryBean.setParameter("ACTION", IATOMS_PARAM_TYPE.ACTION.getCode());
			sqlQueryBean.setParameter("headerArea", IATOMS_PARAM_TYPE.LOCATION.getCode());
			sqlQueryBean.setParameter("INSTALL_TYPE", IATOMS_PARAM_TYPE.INSTALL_TYPE.getCode());
			
			LOGGER.debug("DmmRepositoryDAO.listBy()", "sql:" + sql.toString());
			//AliasBean aliasBean = new AliasBean();
			//aliasBean.addScalar(DmmRepositoryDTO.ATTRIBUTE.ASSET_ID.getValue(), StringType.INSTANCE);
			//AliasBean aliasBean = sql.createAliasBean(DmmRepositoryDTO.class);
			LOGGER.debug("DmmRepositoryDAO.listBy()", "sql:" + sqlQueryBean.toString());
			repositoryDTOs = (List<String>) super.getDaoSupport().findByNativeSql(sqlQueryBean);
			return repositoryDTOs;
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ":Failed-- listBy()---" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#dmmRepositoryDTO(java.lang.String, java.lang.String)
	 */
	@Override
	public DmmRepositoryDTO checkAssetIsInWarehouse(String serialNumber, String fromWarehouseId) throws DataAccessException {
		LOGGER.debug(this.getClass().getSimpleName() + "checkAssetIsInWarehouse", "serialNumber:" + serialNumber);
		LOGGER.debug(this.getClass().getSimpleName() + "checkAssetIsInWarehouse", "fromWarehouseId:" + fromWarehouseId);
		//庫存信息
		List<DmmRepositoryDTO> result = null;
		//得到schema
		String schema = this.getMySchema();
		DmmRepositoryDTO repositoryDTO = null;
		//創建SqlStatement
		SqlStatement sqlStatement = new SqlStatement();
		try{
			sqlStatement.addSelectClause("repository.STATUS", DmmRepositoryDTO.ATTRIBUTE.STATUS.getValue());
			sqlStatement.addFromExpression(schema + ".DMM_REPOSITORY repository");
			if (StringUtils.hasText(serialNumber)) {
				sqlStatement.addWhereClause("repository.SERIAL_NUMBER =:serialNumber", serialNumber);
			}
			if (StringUtils.hasText(fromWarehouseId)) {
				sqlStatement.addWhereClause("repository.WAREHOUSE_ID =:fromWarehouseId", fromWarehouseId);
			}
			//記錄sql語句
			LOGGER.debug(this.getClass().getName() + "checkAssetIsInWarehouse ---->sql:" + sqlStatement.toString() );
			//創建SqlQueryBean
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmRepositoryDTO.class);
			//執行sql語句
			result = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(result)) {
				repositoryDTO = result.get(0);
			}
		} catch(Exception e) {
			LOGGER.error("DmmRepositoryDAO.getRepositoryBySerialNumber() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		return repositoryDTO;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#confirmInStorage(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, cafe.core.bean.identity.LogonUser)
	 */
	public void confirmInStorage(String assetTransId, String historyId, LogonUser logonUser, String transferAction, String serialNumbers, String transferSuccess, String cancelSuccess, String endFlag, String cancelAction) throws DataAccessException {
		try {
			//打印參數
			LOGGER.debug(this.getClass().getSimpleName() + "confirmInStorage", "assetTransId:" + assetTransId);
			LOGGER.debug(this.getClass().getSimpleName() + "confirmInStorage", "transferAction:" + transferAction);
			LOGGER.debug(this.getClass().getSimpleName() + "confirmInStorage", "serialNumbers:" + serialNumbers);
			LOGGER.debug(this.getClass().getSimpleName() + "confirmInStorage", "historyId:" + historyId);
			LOGGER.debug(this.getClass().getSimpleName() + "confirmInStorage", "logonUser:" + logonUser);
			LOGGER.debug(this.getClass().getSimpleName() + "confirmInStorage", "cancelSuccess:" + cancelSuccess);
			LOGGER.debug(this.getClass().getSimpleName() + "confirmInStorage", "transferSuccess:" + transferSuccess);
			LOGGER.debug(this.getClass().getSimpleName() + "confirmInStorage", "endFlag:" + endFlag);
			LOGGER.debug(this.getClass().getSimpleName() + "confirmInStorage", "cancelAction:" + cancelAction);
			//得到schema
			String schema = this.getMySchema();
			
			//創建SqlStatement
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("exec dbo.usp_Asset_Tranfer_Out_And_Cancel :assetTransId, :isTransDone, :historyId, :userId, :userName, :tranferAction, :serialNumbers, :transferSuccess, :cancelSuccess, :endFlag, :cancelAction");
			sqlQueryBean.setParameter("assetTransId", assetTransId);
			sqlQueryBean.setParameter("isTransDone", IAtomsConstants.YES);
			sqlQueryBean.setParameter("historyId", historyId);
			sqlQueryBean.setParameter("userId", logonUser.getId());
			sqlQueryBean.setParameter("userName", logonUser.getName());
			sqlQueryBean.setParameter("tranferAction", transferAction);
			sqlQueryBean.setParameter("serialNumbers", serialNumbers);
			sqlQueryBean.setParameter("transferSuccess", transferSuccess);
			sqlQueryBean.setParameter("cancelSuccess", cancelSuccess);
			sqlQueryBean.setParameter("endFlag", endFlag);
			sqlQueryBean.setParameter("cancelAction", cancelAction);
			LOGGER.debug(this.getClass().getName() + "saveRepository ---->sql:" + sqlQueryBean.toString());
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.confirmInStorage() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#repositoryAssetManageTSBAndJdw(java.lang.String, java.sql.Timestamp, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.sql.Timestamp, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.sql.Timestamp, java.sql.Timestamp, java.lang.String, java.lang.String)
	 */
	@Override
	public Object[] repositoryAssetManageTSBAndJdw(String serialNumber, Timestamp nowTime, String userId, String userName,
			String description, String hId, String actionId, String nowStatus, Timestamp enableDate, String isEnabled, String dtid, String caseId,
			String merchantId, String merchantHeaderId, String installedAdress, String installedAdressLocation, String isCup, String maintainCompany, 
			Date analyzeDate, Timestamp caseCompletionDate, String maintainUser, String jdwFlag, String updateTableFlag, String selectMonyhYear) throws DataAccessException {
		Object[] objects = null;
		try {
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			
			sqlQueryBean.append("declare @output varchar(200) declare @flag varchar(1) exec dbo.usp_RepositoryAssetManageTSB :serialNumber, @output output, :nowTime, :userId, "+
						":userName, :description, :hId, :actionId, :nowStatus, @flag output, "+
						":enableDate, :isEnabled, :dtid, :caseId,"+
						":merchantId, :merchantHeaderId, :installedAdress, :installedAdressLocation, :isCup, :maintainCompany, :analyzeDate, "+
						":caseCompletionDate, :maintainUser, :jdwFlag, :updateTableFlag, :selectMonyhYear");
			sqlQueryBean.append("select @output as output, @flag as flag ");
			sqlQueryBean.setParameter("serialNumber", StringUtils.hasText(serialNumber) ? serialNumber : "");
			sqlQueryBean.setParameter("nowTime", nowTime);
			sqlQueryBean.setParameter("userId", StringUtils.hasText(userId) ? userId : "");
			sqlQueryBean.setParameter("userName", StringUtils.hasText(userName) ? userName : "");
			sqlQueryBean.setParameter("description", StringUtils.hasText(description) ? description : "");
			sqlQueryBean.setParameter("hId", StringUtils.hasText(hId) ? hId : "");
			sqlQueryBean.setParameter("actionId", StringUtils.hasText(actionId) ? actionId : "");
			sqlQueryBean.setParameter("nowStatus", StringUtils.hasText(nowStatus) ? nowStatus : "");
			sqlQueryBean.setParameter("enableDate", enableDate!=null?enableDate:"");
			sqlQueryBean.setParameter("isEnabled", StringUtils.hasText(isEnabled) ? isEnabled : "");
			sqlQueryBean.setParameter("dtid", StringUtils.hasText(dtid) ? dtid : "");
			sqlQueryBean.setParameter("caseId", StringUtils.hasText(caseId) ? caseId : "");
			sqlQueryBean.setParameter("merchantId", StringUtils.hasText(merchantId) ? merchantId : "");
			sqlQueryBean.setParameter("merchantHeaderId",  StringUtils.hasText(merchantHeaderId) ? merchantHeaderId : "");
			sqlQueryBean.setParameter("installedAdress",  StringUtils.hasText(installedAdress) ? installedAdress : "");
			sqlQueryBean.setParameter("installedAdressLocation",  StringUtils.hasText(installedAdressLocation) ? installedAdressLocation : "");
			sqlQueryBean.setParameter("isCup",  StringUtils.hasText(isCup) ? isCup : "");
			sqlQueryBean.setParameter("maintainCompany", StringUtils.hasText(maintainCompany) ? maintainCompany : "");
			sqlQueryBean.setParameter("analyzeDate",  analyzeDate!=null?analyzeDate:"");
			sqlQueryBean.setParameter("caseCompletionDate",  caseCompletionDate!=null?caseCompletionDate:"");
			sqlQueryBean.setParameter("maintainUser",  StringUtils.hasText(maintainUser) ? maintainUser : "");
			sqlQueryBean.setParameter("jdwFlag",  StringUtils.hasText(jdwFlag) ? jdwFlag : "");
			sqlQueryBean.setParameter("updateTableFlag",  StringUtils.hasText(updateTableFlag) ? updateTableFlag : "");
			sqlQueryBean.setParameter("selectMonyhYear",  StringUtils.hasText(selectMonyhYear) ? selectMonyhYear : "");

			LOGGER.debug(this.getClass().getName() + "SQL---------->" + sqlQueryBean.toString());
			List list = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if(!CollectionUtils.isEmpty(list)){
				objects = (Object[]) list.get(0);
			}
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.repositoryAssetManageTSBAndJdw() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		return objects;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#getRepositoryByHql(java.lang.String)
	 */
	@Override
	public DmmRepository getRepositoryByHql(String serialNumber) throws DataAccessException {
		DmmRepository dmmRepository = null;
		List<DmmRepository> result = null;
		SqlQueryBean sqlQueryBean = new SqlQueryBean();
	try {
		sqlQueryBean.append("from DmmRepository r where r.serialNumber=:serialNumber");
		sqlQueryBean.setParameter("serialNumber", serialNumber);
		LOGGER.debug(this.getClass().getName() + "getRepositoryByHql SQL---------->" + sqlQueryBean.toString());
		result = (List<DmmRepository>) this.getDaoSupport().findByHql(sqlQueryBean);
		if (!CollectionUtils.isEmpty(result)) {
			dmmRepository = result.get(0);
		}
	} catch (Exception e) {
		LOGGER.error("DmmRepositoryDAO.getRepositoryByHql() is error" + e, e);
		throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
	}
	return dmmRepository;				
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#updateRepositoryByCaseId(java.lang.String, java.util.Date, java.lang.String)
	 */
	@Override
	public void updateRepositoryByCaseId(String caseId, Date completeDate, String isHistory) throws DataAccessException {
		try {
			//打印參數
			LOGGER.debug(this.getClass().getSimpleName() + "updateRepositoryByCaseId", "caseId:" + caseId);
			LOGGER.debug(this.getClass().getSimpleName() + "updateRepositoryByCaseId", "completeDate:" + completeDate);
			LOGGER.debug(this.getClass().getSimpleName() + "updateRepositoryByCaseId", "isHistory:" + isHistory);
			//得到schema
			String schema = this.getMySchema();
			//創建SqlStatement
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("exec ").append(schema).append(".usp_updateRepositoryByCaseId :caseId, :completeDate, :isHistory");
			sqlQueryBean.setParameter("caseId", caseId);
			sqlQueryBean.setParameter("completeDate", completeDate);
			sqlQueryBean.setParameter("isHistory", isHistory);
			LOGGER.debug(this.getClass().getName() + "updateRepositoryByCaseId ---->sql:" + sqlQueryBean.toString() );
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.updateRepositoryByCaseId() is error" + e, e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#getBorrowDetail()
	 */
	@Override
	public List<DmmRepositoryDTO> getBorrowDetail() throws DataAccessException {
		// TODO Auto-generated method stub
		try {
			List<DmmRepositoryDTO> repositoryDTOs = null;
			String schema = this.getMySchema();
			SqlStatement sql = new SqlStatement();
			sql.addSelectClause("r.BORROWER", DmmRepositoryDTO.ATTRIBUTE.BORROWER.getValue());
			sql.addSelectClause("u.CNAME", DmmRepositoryDTO.ATTRIBUTE.BORROWER_NAME.getValue());
			sql.addSelectClause("i.ITEM_NAME", DmmRepositoryDTO.ATTRIBUTE.ASSET_TYPE_NAME.getValue());
			sql.addSelectClause("a.NAME", DmmRepositoryDTO.ATTRIBUTE.NAME.getValue());
			sql.addSelectClause("r.SERIAL_NUMBER", DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sql.addSelectClause("r.BORROWER_START", DmmRepositoryDTO.ATTRIBUTE.BORROWER_START.getValue()); 
			sql.addSelectClause("r.BORROWER_END", DmmRepositoryDTO.ATTRIBUTE.BORROWER_END.getValue());
			sql.setFromExpression(" " + schema + ".DMM_REPOSITORY r " +
					"join " + schema + ".DMM_ASSET_TYPE a on a.ASSET_TYPE_ID = r.ASSET_TYPE_ID and r.STATUS = 'BORROWING' " +
					"join " + schema + ".BASE_PARAMETER_ITEM_DEF i on a.ASSET_CATEGORY = i.ITEM_VALUE and i.BPTD_CODE = 'ASSET_CATEGORY' " +
					"join " + schema + ".ADM_USER u on r.BORROWER = u.USER_ID");
			sql.setOrderByExpression("r.BORROWER, i.ITEM_ORDER, a.ASSET_TYPE_ID");

			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			AliasBean aliasBean = sql.createAliasBean(DmmRepositoryDTO.class);
			repositoryDTOs = (List<DmmRepositoryDTO>) super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if(!CollectionUtils.isEmpty(repositoryDTOs)) {
				return repositoryDTOs;
			}
		} catch(Exception e) {
			LOGGER.error("DmmRepositoryDAO.getBorrowDetail() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO#checkBorrowSerialNumber(java.lang.String, java.lang.String)
	 */
	@Override
	public List<DmmRepositoryDTO> checkBorrowSerialNumber(String assetTypeId, String serialNumber) throws DataAccessException {
		LOGGER.debug(this.getClass().getSimpleName() + "checkBorrowSerialNumber", "assetTypeId:" + assetTypeId);
		LOGGER.debug(this.getClass().getSimpleName() + "checkBorrowSerialNumber", "serialNumber:" + serialNumber);
		List<DmmRepositoryDTO> dmmRepositoryDTOs = null;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("repo.ASSET_ID", DmmRepositoryDTO.ATTRIBUTE.ASSET_ID.getValue());
			sqlStatement.addSelectClause("repo.STATUS", DmmRepositoryDTO.ATTRIBUTE.STATUS.getValue());
			sqlStatement.addFromExpression(schema + ".DMM_REPOSITORY repo");
			if (StringUtils.hasText(assetTypeId)) {
				sqlStatement.addWhereClause("repo.ASSET_TYPE_ID = :assetTypeId", assetTypeId);
			}
			if (StringUtils.hasText(serialNumber)) {
				sqlStatement.addWhereClause("repo.SERIAL_NUMBER = :serialNumber", serialNumber);
			}
			/*sqlStatement.addWhereClause("repo.STATUS = :status", IAtomsConstants.PARAM_ASSET_STATUS_REPERTORY);*/
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(DmmRepositoryDTO.class);
			dmmRepositoryDTOs = (List<DmmRepositoryDTO>) super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("DmmRepositoryDAO.checkBorrowSerialNumber() is error" + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		return dmmRepositoryDTOs;
	}
}
