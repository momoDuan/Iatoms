package com.cybersoft4u.xian.iatoms.services.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.type.BigDecimalType;
import org.hibernate.type.IntegerType;
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
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAssetLinkDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmHistoryCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseHandleInfo;
/**
 * Purpose: SRM_案件處理資料檔DAO實現類
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016年11月10日
 * @MaintenancePersonnel CrissZhang
 */
public class SrmCaseHandleInfoDAO extends GenericBaseDAO<SrmCaseHandleInfo> implements ISrmCaseHandleInfoDAO {

	/**
	 * 系统日志记录物件   
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(SrmCaseHandleInfoDAO.class);
	
	/**
	 * (non-Javadoc) 
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#queryListBy(com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO)
	 */
	public List<SrmCaseHandleInfoDTO> queryListBy(CaseManagerFormDTO formDTO)throws DataAccessException {
		long startQueryListTime = System.currentTimeMillis();
		List<SrmCaseHandleInfoDTO> result = null;
		try {
			// Task #2615 當案件進入 協調完成、線上排除、完修 時 查詢結果之 回應、到場、完修 無需顯示 警示或逾期 符號
			StringBuilder ignoreCaseStatus = new StringBuilder();
			// 結案
			ignoreCaseStatus.append(IAtomsConstants.CASE_STATUS.CLOSED.getCode()).append(IAtomsConstants.MARK_SEPARATOR);
			// 協調完成
			ignoreCaseStatus.append(IAtomsConstants.CASE_STATUS.IMMEDIATE_CLOSE.getCode()).append(IAtomsConstants.MARK_SEPARATOR);
			// 作廢
			ignoreCaseStatus.append(IAtomsConstants.CASE_STATUS.VOIDED.getCode()).append(IAtomsConstants.MARK_SEPARATOR);
			// 完修
			ignoreCaseStatus.append(IAtomsConstants.CASE_STATUS.COMPLETED.getCode()).append(IAtomsConstants.MARK_SEPARATOR);
			// 待結案審查
			ignoreCaseStatus.append(IAtomsConstants.CASE_STATUS.WAIT_CLOSE.getCode());
			
			// 得到schema
			String schema = this.getMySchema();
			// 查詢條件map集合
			Map<String, String> queryColumnMap = formDTO.getQueryColumnMap();
			if(queryColumnMap == null){
				// 若無值則創建
				queryColumnMap = new HashMap<String, String>();
			}
			SqlStatement sqlStatement = new SqlStatement();
			StringBuilder selectBuilder = new StringBuilder();
			StringBuilder fromBuilder = new StringBuilder();
			StringBuilder whereBuilder = new StringBuilder();
			if (IAtomsConstants.PARAM_YES.equals(formDTO.getQueryTsbFlag())) {
				sqlStatement.addSelectClause("caseHandle.CREATED_BY_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
				sqlStatement.addSelectClause("caseHandle.UPDATED_BY_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATED_BY_ID.getValue());
				sqlStatement.addSelectClause("caseHandle.DISPATCH_USER", SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_USER.getValue());
				sqlStatement.addSelectClause("caseHandle.COMPLETE_USER", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_USER.getValue());
				sqlStatement.addSelectClause("caseHandle.RESPONSE_USER", SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_USER.getValue());
				sqlStatement.addSelectClause("caseHandle.ARRIVE_USER", SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_USER.getValue());
				sqlStatement.addSelectClause("caseHandle.ANALYZE_USER", SrmCaseHandleInfoDTO.ATTRIBUTE.ANALYZE_USER.getValue());
				sqlStatement.addSelectClause("caseHandle.CLOSE_USER", SrmCaseHandleInfoDTO.ATTRIBUTE.CLOSE_USER.getValue());
				sqlStatement.addSelectClause("caseHandle.INSTALL_CASE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_CASE_ID.getValue());
				sqlStatement.addSelectClause("caseHandle.INSTALL_COMPLETE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_COMPLETE_DATE.getValue());

			}
			// 必需查詢的列 - 案件狀態
			sqlStatement.addSelectClause("caseHandle.CASE_STATUS", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS.getValue());
			// 必需查詢的列 - 案件編號
			sqlStatement.addSelectClause("caseHandle.CASE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue());
			// 必需查詢的列 - 是否查詢歷史表
			if(formDTO.getIsInstant()){
				sqlStatement.addSelectClause("'N'", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_HISTORY.getValue());
			} else {
				sqlStatement.addSelectClause("'Y'", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_HISTORY.getValue());
			}
			
			// 必需查詢的列 - 催修件
			selectBuilder.delete(0, selectBuilder.length());
			selectBuilder.append("(CASE WHEN caseHandle.CASE_STATUS <> :closedCaseType ");
			selectBuilder.append(" and caseHandle.REPAIR_TIMES >= 2 THEN ('Y') ELSE NULL END)");
			sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.RUSH_REPAIR.getValue());
			// 必需查詢的列 - 維護部門編號
			sqlStatement.addSelectClause("caseHandle.COMPANY_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_ID.getValue());
			// 必需查詢的列 - 派工單位名稱
			sqlStatement.addSelectClause("(case when dept.DEPT_NAME is not null then dept.DEPT_NAME else caseGroup.ITEM_NAME end)", SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_NAME.getValue());
			
			// 必需查詢的列 - DTID
			sqlStatement.addSelectClause("caseHandle.DTID", SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue());
			// 必需查詢的列 - 客戶編號
			sqlStatement.addSelectClause("caseHandle.CUSTOMER_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue());
			// 必需查詢的列 - 案件類別
			sqlStatement.addSelectClause("caseHandle.CASE_CATEGORY", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue());
			
			// 必需查詢的列 - TID
			selectBuilder.delete(0, selectBuilder.length());
			selectBuilder.append("stuff((SELECT '，' + RTRIM( tidParam.TID) FROM (select distinct transParam.TID from ");
			// 是否查歷史案件
			if(formDTO.getIsInstant()){
				selectBuilder.append(schema).append(".SRM_CASE_TRANSACTION_PARAMETER transParam ");
			} else {
				selectBuilder.append(schema).append(".SRM_HISTORY_CASE_TRANSACTION_PARAMETER transParam ");
			}
			selectBuilder.append(" WHERE transParam.CASE_ID = caseHandle.CASE_ID and isnull(transParam.TID, '') <> '')tidParam");
			selectBuilder.append(" FOR XML path('') ) , 1 , 1 , '')");
			sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.TID.getValue());
			// 必需查詢的列 - 責任歸屬
			sqlStatement.addSelectClause("caseHandle.RESPONSIBITY", SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSIBITY.getValue());
			
			// 必需查詢的列 - 報修問題原因
			sqlStatement.addSelectClause("caseHandle.PROBLEM_REASON", SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_REASON.getValue());
			// 必需查詢的列 - 報修問題原因分類
			sqlStatement.addSelectClause("caseHandle.PROBLEM_REASON_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_REASON_CODE.getValue());
			// 必需查詢的列 - 報修解決方式
			sqlStatement.addSelectClause("caseHandle.PROBLEM_SOLUTION", SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_SOLUTION.getValue());
			// 必需查詢的列 - 報修解決方式分類
			sqlStatement.addSelectClause("caseHandle.PROBLEM_SOLUTION_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_SOLUTION_CODE.getValue());
			
			// 必需查詢的列 - 最後異動日期
			sqlStatement.addSelectClause("caseHandle.UPDATED_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			// 必需查詢的列 - 案件類型
			sqlStatement.addSelectClause("caseHandle.CASE_TYPE", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE.getValue());
			// 必需查詢的列 - IS_TMS
			sqlStatement.addSelectClause("caseHandle.IS_TMS", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_TMS.getValue());
			// 案件建案人員所在公司
			sqlStatement.addSelectClause("vendorService.COMPANY_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.VENDOR_SERVICE_CUSTOMER.getValue());
			//Task #3323 是否授權確認
			sqlStatement.addSelectClause("caseHandle.CONFIRM_AUTHORIZES", SrmCaseHandleInfoDTO.ATTRIBUTE.CONFIRM_AUTHORIZES.getValue());
			//Task #3336 需判斷是否為CMS案件
			sqlStatement.addSelectClause("caseHandle.CMS_CASE", SrmCaseHandleInfoDTO.ATTRIBUTE.CMS_CASE.getValue());
			sqlStatement.addSelectClause("caseHandle.INSTALL_TYPE", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_TYPE.getValue());
			// 客戶
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_NAME.getValue())){
				sqlStatement.addSelectClause("company.SHORT_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_NAME.getValue());
				
				fromBuilder.append(" left join ").append(schema).append(".BIM_COMPANY company on caseHandle.CUSTOMER_ID = company.COMPANY_ID ");
			}
			// 案件狀態
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS_NAME.getValue())){
				sqlStatement.addSelectClause("caseStatus.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS_NAME.getValue());
				
				fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF caseStatus on caseStatus.BPTD_CODE = :caseStatusParam ");
				fromBuilder.append(" and caseStatus.ITEM_VALUE = caseHandle.CASE_STATUS ");
			}
			// 案件類型
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE_NAME.getValue())){
				selectBuilder.delete(0, selectBuilder.length());
				selectBuilder.append("(CASE WHEN caseHandle.CASE_STATUS <> :closedCaseType ");
				selectBuilder.append(" and caseHandle.REPAIR_TIMES >= 2 THEN (ticketMode.ITEM_NAME + '(催修)') " );
				selectBuilder.append(" ELSE ticketMode.ITEM_NAME END)");
				sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE_NAME.getValue());
				
				fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF ticketMode on ticketMode.BPTD_CODE = :ticketModeParam ");
				fromBuilder.append(" and ticketMode.ITEM_VALUE = caseHandle.CASE_TYPE ");
			}
			// 回應 --'OVER_HOUR'表示已過時效，'OVER_WARNNING'已過警示
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_CONDITION.getValue())){
				// Task #2489 專案與查核案件，無須計算SLA Task #2615 當案件進入 協調完成、線上排除、完修 時 查詢結果之 回應、到場、完修 無需顯示 警示或逾期 符號
				selectBuilder.delete(0, selectBuilder.length());
				selectBuilder.append(" (case when (caseHandle.CASE_CATEGORY =:projectCaseCategory or caseHandle.CASE_CATEGORY =:checkCaseCategory) then '' " );
				selectBuilder.append(" when caseHandle.CASE_STATUS in( :ignoreCaseStatus ) then '' " );
				selectBuilder.append(" when caseHandle.ACCEPTABLE_RESPONSE_DATE is not null and " );
				selectBuilder.append(" (isnull(caseHandle.RESPONSE_DATE,getdate()) > caseHandle.ACCEPTABLE_RESPONSE_DATE) then 'OVER_HOUR' ");
				selectBuilder.append(" when (caseHandle.ACCEPTABLE_RESPONSE_DATE is not null and caseHandle.RESPONSE_WARNNING is not null and ");
				selectBuilder.append(" (isnull(caseHandle.RESPONSE_DATE,getdate()) > dateadd(hour,-caseHandle.RESPONSE_WARNNING, caseHandle.ACCEPTABLE_RESPONSE_DATE))) then 'OVER_WARNNING' ");
				selectBuilder.append(" else '' end)");
				sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_CONDITION.getValue());
			}
			// 到場 --'OVER_HOUR'表示已過時效，'OVER_WARNNING'已過警示
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_CONDITION.getValue())){
				// Task #2489 專案與查核案件，無須計算SLA Task #2615 當案件進入 協調完成、線上排除、完修 時 查詢結果之 回應、到場、完修 無需顯示 警示或逾期 符號
				selectBuilder.delete(0, selectBuilder.length());
				selectBuilder.append(" (case when (caseHandle.CASE_CATEGORY =:projectCaseCategory or caseHandle.CASE_CATEGORY =:checkCaseCategory) then '' " );
				selectBuilder.append(" when caseHandle.CASE_STATUS in( :ignoreCaseStatus ) then '' " );
				selectBuilder.append(" when isnull(caseHandle.ARRIVE_DATE,getdate()) > caseHandle.ACCEPTABLE_ARRIVE_DATE then 'OVER_HOUR' ");
				selectBuilder.append(" when isnull(caseHandle.ARRIVE_DATE,getdate()) > dateadd(hour,-isnull(caseHandle.ARRIVE_WARNNING, 0), caseHandle.ACCEPTABLE_ARRIVE_DATE) then 'OVER_WARNNING' ");
				selectBuilder.append(" else '' end)");
				sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_CONDITION.getValue());
			}
			// 完修 --'OVER_HOUR'表示已過時效，'OVER_WARNNING'已過警示
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_CONDITION.getValue())){
				// Task #2489 專案與查核案件，無須計算SLA Task #2615 當案件進入 協調完成、線上排除、完修 時 查詢結果之 回應、到場、完修 無需顯示 警示或逾期 符號
				selectBuilder.delete(0, selectBuilder.length());
				selectBuilder.append(" (case when (caseHandle.CASE_CATEGORY =:projectCaseCategory or caseHandle.CASE_CATEGORY =:checkCaseCategory) then '' " );
				selectBuilder.append(" when caseHandle.CASE_STATUS in( :ignoreCaseStatus ) then '' " );
				selectBuilder.append(" when isnull(caseHandle.COMPLETE_DATE,getdate()) > caseHandle.ACCEPTABLE_FINISH_DATE then 'OVER_HOUR' ");
				selectBuilder.append(" when isnull(caseHandle.COMPLETE_DATE,getdate()) > dateadd(hour,-isnull(caseHandle.COMPLETE_WARNNING, 0), caseHandle.ACCEPTABLE_FINISH_DATE) then 'OVER_WARNNING' ");
				selectBuilder.append(" else '' end)");
				sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_CONDITION.getValue());
			}
			// 回應狀態
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_STATUS.getValue())){
				sqlStatement.addSelectClause("(case when caseHandle.RESPONSE_DATE is null then '' else 'response' end)", SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_STATUS.getValue());
			}
			// 到場狀態
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_STATUS.getValue())){
				sqlStatement.addSelectClause("(case when caseHandle.ARRIVE_DATE is null then '' else 'arrive' end)", SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_STATUS.getValue());
			}
			// 完修狀態
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_STATUS.getValue())){
				sqlStatement.addSelectClause("(case when caseHandle.COMPLETE_DATE is null then '' else 'complete' end)", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_STATUS.getValue());
			}
			// 裝機類型
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_TYPE_NAME.getValue())){
				sqlStatement.addSelectClause("installType.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_TYPE_NAME.getValue());
				fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF installType on installType.BPTD_CODE = :installTypeParam ");
				fromBuilder.append(" and installType.ITEM_VALUE = caseHandle.INSTALL_TYPE ");
			}
			// 拆機類型
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALL_TYPE_NAME.getValue())){
				sqlStatement.addSelectClause("uninstallType.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALL_TYPE_NAME.getValue());
				
				fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF uninstallType on uninstallType.BPTD_CODE = :uninstallTypeParam ");
				fromBuilder.append(" and uninstallType.ITEM_VALUE = caseHandle.UNINSTALL_TYPE ");
			}
			// 案件類別
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY_NAME.getValue())){
				sqlStatement.addSelectClause("ticketType.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY_NAME.getValue());
				
				fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF ticketType on ticketType.BPTD_CODE = :ticketTypeParam ");
				fromBuilder.append(" and ticketType.ITEM_VALUE = caseHandle.CASE_CATEGORY ");
			}
			// 需求單號
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue())){
				sqlStatement.addSelectClause("caseHandle.REQUIREMENT_NO", SrmCaseHandleInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue());
			}
			// 案件編號
			
			// 案件合約編號
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue())){
				sqlStatement.addSelectClause("contract.CONTRACT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue());
				
				fromBuilder.append(" left join ").append(schema).append(".BIM_CONTRACT contract on contract.CONTRACT_ID = caseHandle.CONTRACT_ID ");
			}
			// 設備合約編號
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE_CONTRACT.getValue())){
				sqlStatement.addSelectClause("edcContract.CONTRACT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE_CONTRACT.getValue());
				
				// 是否查歷史案件
				if(formDTO.getIsInstant()){
					fromBuilder.append(" left join ").append(schema).append(".SRM_CASE_ASSET_LINK edcLink on edcLink.CASE_ID = caseHandle.CASE_ID and edcLink.ITEM_TYPE = :caseLinkEdcType ");
				} else {
					fromBuilder.append(" left join ").append(schema).append(".SRM_HISTORY_CASE_ASSET_LINK edcLink on edcLink.CASE_ID = caseHandle.CASE_ID and edcLink.ITEM_TYPE = :caseLinkEdcType ");
				}
				fromBuilder.append(" and edcLink.IS_LINK = 'Y' ");
				fromBuilder.append(" left join ").append(schema).append(".BIM_CONTRACT edcContract on edcContract.CONTRACT_ID = edcLink.CONTRACT_ID ");
			}
			// 專案
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.IS_PROJECT.getValue())){
				sqlStatement.addSelectClause("caseHandle.IS_PROJECT", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_PROJECT.getValue());
			}
			// 專案代號
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PROJECT_CODE.getValue())){
				sqlStatement.addSelectClause("caseHandle.PROJECT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.PROJECT_CODE.getValue());
			}
			// 專案名稱
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PROJECT_NAME.getValue())){
				sqlStatement.addSelectClause("caseHandle.PROJECT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.PROJECT_NAME.getValue());
			}
			// 派工廠商
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_NAME.getValue())){
				selectBuilder.delete(0, selectBuilder.length());
				selectBuilder.append("(case when (isnull(caseHandle.DISPATCH_DEPT_ID, '') <> '' and isnull(caseHandle.DISPATCH_PROCESS_USERNAME, '') <> '') ");
				selectBuilder.append(" THEN( vendor.SHORT_NAME + '('+ (case when dept.DEPT_NAME is not null then dept.DEPT_NAME else caseGroup.ITEM_NAME end) + '/' + caseHandle.DISPATCH_PROCESS_USERNAME + ')' )");
				selectBuilder.append(" when (isnull(caseHandle.DISPATCH_DEPT_ID, '') <> '' and isnull(caseHandle.DISPATCH_PROCESS_USERNAME, '') = '') ");
				selectBuilder.append(" THEN( vendor.SHORT_NAME + '('+ (case when dept.DEPT_NAME is not null then dept.DEPT_NAME else caseGroup.ITEM_NAME end) + ')' )");
				selectBuilder.append(" else vendor.SHORT_NAME end) ");
				sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_NAME.getValue());
				
				fromBuilder.append(" left join ").append(schema).append(".BIM_COMPANY vendor on vendor.COMPANY_ID = caseHandle.COMPANY_ID ");
			}
			// 廠商名稱
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.VENDOR_NAME.getValue())){
				sqlStatement.addSelectClause("vendor.SHORT_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.VENDOR_NAME.getValue());
				
				// 若未關聯維護廠商公司
				if(!queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_NAME.getValue())){
					fromBuilder.append(" left join ").append(schema).append(".BIM_COMPANY vendor on vendor.COMPANY_ID = caseHandle.COMPANY_ID ");
				}
			}
			// 派工部門
			
			// 廠商人員
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.VENDOR_STAFF.getValue())){
				sqlStatement.addSelectClause("caseHandle.DISPATCH_PROCESS_USERNAME", SrmCaseHandleInfoDTO.ATTRIBUTE.VENDOR_STAFF.getValue());
			}
			// 是否同裝機作業
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.SAME_INSTALLED.getValue())){
				sqlStatement.addSelectClause("caseHandle.SAME_INSTALLED", SrmCaseHandleInfoDTO.ATTRIBUTE.SAME_INSTALLED.getValue());
			}
			// 到場次數
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.ATTENDANCE_TIMES.getValue())){
				sqlStatement.addSelectClause("caseHandle.ATTENDANCE_TIMES", SrmCaseHandleInfoDTO.ATTRIBUTE.ATTENDANCE_TIMES.getValue());
			}
			// DTID
			
			// TID
			
			// 特店代號、特店名稱、查詢條件：特店代號、查詢條件：特店名稱
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue())
					|| StringUtils.hasText(formDTO.getQueryMerchatCode())
					|| StringUtils.hasText(formDTO.getQueryMerchatName())){
				// 特店代號
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue())){
					sqlStatement.addSelectClause("merchant.MERCHANT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
				}
				// 特店名稱
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue())){
					sqlStatement.addSelectClause("merchant.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue());
				}
				
				fromBuilder.append(" left join ").append(schema).append(".BIM_MERCHANT merchant on caseHandle.MERCHANT_CODE = merchant.MERCHANT_ID");
			}
			// 表頭（同對外名稱）、特店區域、特店聯絡人、特店聯絡人電話1、特店聯絡人電話2、特店聯絡人行動電話、特店營業時間、AO人員
			// AO EMAIL、特店營業地址、特店裝機地址、特店聯系地址、特店裝機聯絡人、特店聯繫聯絡人、特店裝機電話、特店聯繫聯絡人電話
			// 查詢條件：表頭（同對外名稱）、查詢條件：AO人員、 列印-isVip
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.HEADER_NAME.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.AREA_NAME.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_TEL.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_TEL2.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PHONE.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.BUSINESS_HOURS.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.AO_NAME.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.AO_EMAIL.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.BUSINESS_ADDRESS.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_PHONE.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_PHONE.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_MOBILE_PHONE.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_MOBILE_PHONE.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_EMAIL.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_EMAIL.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_EMAIL.getValue())
					|| StringUtils.hasText(formDTO.getQueryMerHeader())
					|| StringUtils.hasText(formDTO.getQueryAoName())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.IS_VIP.getValue())
					|| StringUtils.hasText(formDTO.getQueryLocation())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_AREA.getValue())){
				// 表頭（同對外名稱）
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.HEADER_NAME.getValue())){
					sqlStatement.addSelectClause("merHeader.HEADER_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.HEADER_NAME.getValue());
				}
				// 特店區域
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.AREA_NAME.getValue())){
					sqlStatement.addSelectClause("region.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.AREA_NAME.getValue());
				}
				// 特店聯絡人
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT.getValue())){
					sqlStatement.addSelectClause("merHeader.CONTACT", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT.getValue());
				}
				// 特店聯絡人電話1
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_TEL.getValue())){
					sqlStatement.addSelectClause("merHeader.CONTACT_TEL", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_TEL.getValue());
				}
				// 特店聯絡人電話2
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_TEL2.getValue())){
					sqlStatement.addSelectClause("merHeader.CONTACT_TEL2", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_TEL2.getValue());
				}
				// 特店聯絡人行動電話
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PHONE.getValue())){
					sqlStatement.addSelectClause("merHeader.PHONE", SrmCaseHandleInfoDTO.ATTRIBUTE.PHONE.getValue());
				}
				//特店聯絡人Email
				if (queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_EMAIL.getValue())) {
					sqlStatement.addSelectClause("merHeader.CONTACT_EMAIL", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_EMAIL.getValue());
				}
				// 特店營業時間
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.BUSINESS_HOURS.getValue())){
					selectBuilder.delete(0, selectBuilder.length());
					selectBuilder.append("(case when isnull(merHeader.OPEN_HOUR, '') <> '' AND isnull(merHeader.CLOSE_HOUR, '') <> '' ");
					selectBuilder.append(" then (merHeader.OPEN_HOUR + '~' + merHeader.CLOSE_HOUR) end)");
					sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.BUSINESS_HOURS.getValue());
				}
				// AO人員
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.AO_NAME.getValue())){
					sqlStatement.addSelectClause("merHeader.AO_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.AO_NAME.getValue());
				}
				// AO EMAIL
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.AO_EMAIL.getValue())){
					sqlStatement.addSelectClause("merHeader.AOEmail", SrmCaseHandleInfoDTO.ATTRIBUTE.AO_EMAIL.getValue());
				}
				// 特店營業地址 //Task #3483 格式 104 台北市 某某公園 
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.BUSINESS_ADDRESS.getValue())){
					sqlStatement.addSelectClause("(case when baseParamterPostCode.POST_CODE <>'' and baseParamterPostCode.POST_CODE is not null then (baseParamterPostCode.POST_CODE + ' ' + businessLocation.ITEM_NAME + ' ' + merHeader.BUSINESS_ADDRESS) else (businessLocation.ITEM_NAME + ' ' + merHeader.BUSINESS_ADDRESS) end)", SrmCaseHandleInfoDTO.ATTRIBUTE.BUSINESS_ADDRESS.getValue());
				}
				// 特店裝機地址//Task #3483 格式 104 台北市 某某公園 
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue())){
					selectBuilder.delete(0, selectBuilder.length());
					selectBuilder.append("(case when (isnull(caseHandle.IS_BUSSINESS_ADDRESS, '') = 'Y') then ( (case when baseParamterPostCode.POST_CODE <>'' and baseParamterPostCode.POST_CODE is not null then (baseParamterPostCode.POST_CODE + ' ' + businessLocation.ITEM_NAME + ' ' + merHeader.BUSINESS_ADDRESS) else (businessLocation.ITEM_NAME + ' ' + merHeader.BUSINESS_ADDRESS) end) ) ");
					selectBuilder.append(" else ( (case when baseParamterPostCodeInstalled.POST_CODE <>'' and baseParamterPostCodeInstalled.POST_CODE is not null then (baseParamterPostCodeInstalled.POST_CODE + ' ' + installedLocation.ITEM_NAME + ' ' + caseHandle.INSTALLED_ADRESS) else (installedLocation.ITEM_NAME + ' ' + caseHandle.INSTALLED_ADRESS) end) ) end)");
					sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue());
					
//					fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF installedLocation ON installedLocation.BPTD_CODE = :locationParam ");
//					fromBuilder.append(" and installedLocation.ITEM_VALUE = caseHandle.INSTALLED_ADRESS_LOCATION ");
				}
				// 特店聯系地址//Task #3483 格式 104 台北市 某某公園 
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS.getValue())){
					selectBuilder.delete(0, selectBuilder.length());
					//Bug #3187
					//selectBuilder.append("(case when caseHandle.CASE_CATEGORY='INSTALL' or caseHandle.CASE_CATEGORY='UPDATE' then installedLocation.ITEM_NAME + '-' + caseHandle.INSTALLED_ADRESS else (case when (isnull(caseHandle.CONTACT_IS_BUSSINESS_ADDRESS, '') = 'Y') then (businessLocation.ITEM_NAME + '-' + merHeader.BUSINESS_ADDRESS) ");
					selectBuilder.append("(case when caseHandle.CASE_CATEGORY='INSTALL' or caseHandle.CASE_CATEGORY='UPDATE' then ");
					selectBuilder.append("(case when (isnull(caseHandle.IS_BUSSINESS_ADDRESS, '') = 'Y') then ( (case when baseParamterPostCode.POST_CODE <>'' and baseParamterPostCode.POST_CODE is not null then (baseParamterPostCode.POST_CODE + ' ' + businessLocation.ITEM_NAME + ' ' + merHeader.BUSINESS_ADDRESS) else (businessLocation.ITEM_NAME + ' ' + merHeader.BUSINESS_ADDRESS) end) ) ");
					selectBuilder.append(" else ( (case when baseParamterPostCodeInstalled.POST_CODE <>'' and baseParamterPostCodeInstalled.POST_CODE is not null then (baseParamterPostCodeInstalled.POST_CODE + ' ' + installedLocation.ITEM_NAME + ' ' + caseHandle.INSTALLED_ADRESS) else (installedLocation.ITEM_NAME + ' ' + caseHandle.INSTALLED_ADRESS) end) ) end) ");
					selectBuilder.append("else (case when (isnull(caseHandle.CONTACT_IS_BUSSINESS_ADDRESS, '') = 'Y') then (           (case when baseParamterPostCode.POST_CODE <>'' and baseParamterPostCode.POST_CODE is not null then (baseParamterPostCode.POST_CODE + ' ' + businessLocation.ITEM_NAME + ' ' + merHeader.BUSINESS_ADDRESS) else (businessLocation.ITEM_NAME + ' ' + merHeader.BUSINESS_ADDRESS) end) ) ");
					//Task #3073
					selectBuilder.append("when caseHandle.CONTACT_IS_BUSSINESS_ADDRESS = 'E' then ");
					selectBuilder.append("case when(isnull(caseHandle.IS_BUSSINESS_ADDRESS,'')= 'Y') ");
					selectBuilder.append("then( (case when baseParamterPostCode.POST_CODE <>'' and baseParamterPostCode.POST_CODE is not null then (baseParamterPostCode.POST_CODE + ' ' + businessLocation.ITEM_NAME +  ' ' + merHeader.BUSINESS_ADDRESS) else (businessLocation.ITEM_NAME +  ' ' + merHeader.BUSINESS_ADDRESS) end) ) ");
					selectBuilder.append("else( (case when baseParamterPostCodeInstalled.POST_CODE <>'' and baseParamterPostCodeInstalled.POST_CODE is not null then (baseParamterPostCodeInstalled.POST_CODE + ' ' + installedLocation.ITEM_NAME + ' ' + caseHandle.INSTALLED_ADRESS) else (installedLocation.ITEM_NAME + ' ' + caseHandle.INSTALLED_ADRESS) end) ) ");
					selectBuilder.append("end ");
					
					selectBuilder.append(" else ( (case when baseParamterPostCodeContact.POST_CODE <>'' and baseParamterPostCodeContact.POST_CODE is not null then (baseParamterPostCodeContact.POST_CODE + ' ' + contactLocation.ITEM_NAME + ' ' + caseHandle.CONTACT_ADDRESS) else (contactLocation.ITEM_NAME + ' ' + caseHandle.CONTACT_ADDRESS) end) ) end) end)");
					sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS.getValue());
					
					/*fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF contactLocation ON contactLocation.BPTD_CODE = :locationParam ");
					fromBuilder.append(" and contactLocation.ITEM_VALUE = caseHandle.CONTACT_ADDRESS_LOCATION ");*/
				}
				
				// 裝機地址查詢
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue())
						|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS.getValue())
						|| StringUtils.hasText(formDTO.getQueryLocation())
						|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_AREA.getValue())){
					fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF installedLocation ON installedLocation.BPTD_CODE = :locationParam ");
					fromBuilder.append(" and installedLocation.ITEM_VALUE = caseHandle.INSTALLED_ADRESS_LOCATION ");
				}
				// 聯繫地址查詢
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS.getValue())
						|| StringUtils.hasText(formDTO.getQueryLocation())
						|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_AREA.getValue())){
					fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF contactLocation ON contactLocation.BPTD_CODE = :locationParam ");
					fromBuilder.append(" and contactLocation.ITEM_VALUE = caseHandle.CONTACT_ADDRESS_LOCATION ");
				}
				
				// 特店裝機聯絡人
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT.getValue())){
					selectBuilder.delete(0, selectBuilder.length());
					selectBuilder.append("(case when (isnull(caseHandle.IS_BUSSINESS_CONTACT, '') = 'Y') then merHeader.CONTACT ");
					selectBuilder.append(" else caseHandle.INSTALLED_CONTACT end)");
					sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT.getValue());
				}
				// 特店聯繫聯絡人
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER.getValue())){
					selectBuilder.delete(0, selectBuilder.length());
					//Bug #3187
					//selectBuilder.append("(case when caseHandle.CASE_CATEGORY='INSTALL' or caseHandle.CASE_CATEGORY='UPDATE' then caseHandle.INSTALLED_CONTACT else (case when (isnull(caseHandle.CONTACT_IS_BUSSINESS_CONTACT, '') = 'Y') then merHeader.CONTACT ");
					selectBuilder.append("(case when caseHandle.CASE_CATEGORY='INSTALL' or caseHandle.CASE_CATEGORY='UPDATE' then ");
					selectBuilder.append("(case when (isnull(caseHandle.IS_BUSSINESS_CONTACT, '') = 'Y') then merHeader.CONTACT ");
					selectBuilder.append(" else caseHandle.INSTALLED_CONTACT end) ");
					selectBuilder.append("else (case when (isnull(caseHandle.CONTACT_IS_BUSSINESS_CONTACT, '') = 'Y') then merHeader.CONTACT ");
					//Task #3073
					selectBuilder.append("when caseHandle.CONTACT_IS_BUSSINESS_CONTACT = 'E' then ");
					selectBuilder.append("case when isnull(caseHandle.IS_BUSSINESS_CONTACT,'')= 'Y' then merHeader.CONTACT ");
					selectBuilder.append("else caseHandle.INSTALLED_CONTACT end ");
					
					selectBuilder.append(" else caseHandle.CONTACT_USER end) end)");
					sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER.getValue());
				}
				// 特店裝機電話
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_PHONE.getValue())){
					selectBuilder.delete(0, selectBuilder.length());
					selectBuilder.append("(case when (isnull(caseHandle.IS_BUSSINESS_CONTACT_PHONE, '') = 'Y') then merHeader.CONTACT_TEL ");
					selectBuilder.append(" else caseHandle.INSTALLED_CONTACT_PHONE end)");
					sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_PHONE.getValue());
				}
				// 特店聯繫聯絡人電話
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_PHONE.getValue())){
					selectBuilder.delete(0, selectBuilder.length());
					//Bug #3187
					//selectBuilder.append("(case when caseHandle.CASE_CATEGORY='INSTALL' or caseHandle.CASE_CATEGORY='UPDATE' then caseHandle.INSTALLED_CONTACT_PHONE else (case when (isnull(caseHandle.CONTACT_IS_BUSSINESS_CONTACT_PHONE, '') = 'Y') then merHeader.CONTACT_TEL ");
					selectBuilder.append("(case when caseHandle.CASE_CATEGORY='INSTALL' or caseHandle.CASE_CATEGORY='UPDATE' then ");
					selectBuilder.append("(case when (isnull(caseHandle.IS_BUSSINESS_CONTACT_PHONE, '') = 'Y') then merHeader.CONTACT_TEL ");
					selectBuilder.append(" else caseHandle.INSTALLED_CONTACT_PHONE end) ");
					selectBuilder.append("else (case when (isnull(caseHandle.CONTACT_IS_BUSSINESS_CONTACT_PHONE, '') = 'Y') then merHeader.CONTACT_TEL ");
					//Task #3073
					selectBuilder.append("when caseHandle.CONTACT_IS_BUSSINESS_CONTACT_PHONE  = 'E' then ");
					selectBuilder.append("case when isnull(caseHandle.IS_BUSSINESS_CONTACT_PHONE,'')= 'Y' then merHeader.CONTACT_TEL ");
					selectBuilder.append("else caseHandle.INSTALLED_CONTACT_PHONE end ");
					
					selectBuilder.append(" else caseHandle.CONTACT_USER_PHONE end) end)");
					sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_PHONE.getValue());
				}
				//--Task #3343(新增裝機聯絡人手機、裝機聯絡人Email、聯繫聯絡人手機、聯繫聯絡人Email)
				// 特店裝機聯絡人手機
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_MOBILE_PHONE.getValue())){
					selectBuilder.delete(0, selectBuilder.length());
					selectBuilder.append("(case when (isnull(caseHandle.IS_BUSSINESS_CONTACT_MOBILE_PHONE, '') = 'Y') then merHeader.PHONE ");
					selectBuilder.append(" else caseHandle.INSTALLED_CONTACT_MOBILE_PHONE end)");
					sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_MOBILE_PHONE.getValue());
				}
				// 特店聯繫聯絡人手機
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_MOBILE_PHONE.getValue())){
					selectBuilder.delete(0, selectBuilder.length());
					//Bug #3187
					selectBuilder.append("(case when caseHandle.CASE_CATEGORY='INSTALL' or caseHandle.CASE_CATEGORY='UPDATE' then ");
					selectBuilder.append("(case when (isnull(caseHandle.IS_BUSSINESS_CONTACT_MOBILE_PHONE, '') = 'Y') then merHeader.PHONE ");
					selectBuilder.append(" else caseHandle.INSTALLED_CONTACT_MOBILE_PHONE end) ");
					selectBuilder.append("else (case when (isnull(caseHandle.CONTACT_IS_BUSSINESS_CONTACT_MOBILE_PHONE, '') = 'Y') then merHeader.PHONE ");
					//Task #3073
					selectBuilder.append("when caseHandle.CONTACT_IS_BUSSINESS_CONTACT_MOBILE_PHONE  = 'E' then ");
					selectBuilder.append("case when isnull(caseHandle.IS_BUSSINESS_CONTACT_MOBILE_PHONE,'')= 'Y' then merHeader.PHONE ");
					selectBuilder.append("else caseHandle.INSTALLED_CONTACT_MOBILE_PHONE end ");
					selectBuilder.append(" else caseHandle.CONTACT_MOBILE_PHONE end) end)");
					sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_MOBILE_PHONE.getValue());
				}
				// 特店裝機聯絡人Email
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_EMAIL.getValue())){
					selectBuilder.delete(0, selectBuilder.length());
					selectBuilder.append("(case when (isnull(caseHandle.IS_BUSSINESS_CONTACT_EMAIL, '') = 'Y') then merHeader.CONTACT_EMAIL ");
					selectBuilder.append(" else caseHandle.INSTALLED_CONTACT_EMAIL end)");
					sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_EMAIL.getValue());
				}
				// 特店聯繫聯絡人Email
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_EMAIL.getValue())){
					selectBuilder.delete(0, selectBuilder.length());
					//Bug #3187
					//selectBuilder.append("(case when caseHandle.CASE_CATEGORY='INSTALL' or caseHandle.CASE_CATEGORY='UPDATE' then caseHandle.INSTALLED_CONTACT_EMAIL else (case when (isnull(caseHandle.CONTACT_IS_BUSSINESS_CONTACT_EMAIL, '') = 'Y') then merHeader.CONTACT_EMAIL ");
					selectBuilder.append("(case when caseHandle.CASE_CATEGORY='INSTALL' or caseHandle.CASE_CATEGORY='UPDATE' then ");
					selectBuilder.append("(case when (isnull(caseHandle.IS_BUSSINESS_CONTACT_EMAIL, '') = 'Y') then merHeader.CONTACT_EMAIL ");
					selectBuilder.append(" else caseHandle.INSTALLED_CONTACT_EMAIL end) ");
					selectBuilder.append("else (case when (isnull(caseHandle.CONTACT_IS_BUSSINESS_CONTACT_EMAIL, '') = 'Y') then merHeader.CONTACT_EMAIL ");
					//Task #3073
					selectBuilder.append("when caseHandle.CONTACT_IS_BUSSINESS_CONTACT_EMAIL  = 'E' then ");
					selectBuilder.append("case when isnull(caseHandle.IS_BUSSINESS_CONTACT_EMAIL,'')= 'Y' then merHeader.CONTACT_EMAIL ");
					selectBuilder.append("else caseHandle.INSTALLED_CONTACT_EMAIL end ");
					selectBuilder.append(" else caseHandle.CONTACT_USER_EMAIL end) end)");
					sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_EMAIL.getValue());
				}
				// 列印-isVip
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.IS_VIP.getValue())){
					sqlStatement.addSelectClause("merHeader.IS_VIP", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_VIP.getValue());
				}
				
				
				
				// 關聯特店表頭
				fromBuilder.append(" left join ").append(schema).append(".BIM_MERCHANT_HEADER merHeader on caseHandle.MERCHANT_HEADER_ID = merHeader.MERCHANT_HEADER_ID");
				// 特店區域
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.AREA_NAME.getValue())){
					fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF region on region.BPTD_CODE = :regionParam and region.ITEM_VALUE = merHeader.AREA ");
				}
				// 特店營業地址、特店裝機地址、特店聯系地址
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.BUSINESS_ADDRESS.getValue())
						|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue())
						|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS.getValue())
						|| StringUtils.hasText(formDTO.getQueryLocation())
						|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_AREA.getValue())){
					fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF businessLocation ON businessLocation.BPTD_CODE = :locationParam and businessLocation.ITEM_VALUE = merHeader.LOCATION ");
					
					//Task #3483 營業郵遞區號、裝機郵遞區號
					fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_POST_CODE baseParamterPostCode ON baseParamterPostCode.POST_CODE_ID = merHeader.POST_CODE_ID ");
					fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_POST_CODE baseParamterPostCodeContact ON baseParamterPostCodeContact.POST_CODE_ID = caseHandle.CONTACT_POST_CODE ");
					fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_POST_CODE baseParamterPostCodeInstalled ON baseParamterPostCodeInstalled.POST_CODE_ID = caseHandle.INSTALLED_POST_CODE ");
				}
				//Bug #3187
				if (queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_AREA.getValue())) {
					selectBuilder.delete(0, selectBuilder.length());
					selectBuilder.append("(case when caseHandle.CASE_CATEGORY='INSTALL' or caseHandle.CASE_CATEGORY='UPDATE' then installedLocation.ITEM_NAME else (case ");
					selectBuilder.append("when(isnull(caseHandle.CONTACT_IS_BUSSINESS_ADDRESS,'')= 'Y') then businessLocation.ITEM_NAME ");
					selectBuilder.append("when caseHandle.CONTACT_IS_BUSSINESS_ADDRESS = 'E' then ");
					selectBuilder.append("case when(isnull(caseHandle.IS_BUSSINESS_ADDRESS,'')= 'Y') ");
					selectBuilder.append("then businessLocation.ITEM_NAME else installedLocation.ITEM_NAME ");
					selectBuilder.append("end else contactLocation.ITEM_NAME end ) end)");
					sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_AREA.getValue());
				}
			}
			// 舊特店代號
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.OLD_MERCHANT_CODE.getValue())){
				sqlStatement.addSelectClause("caseHandle.OLD_MERCHANT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.OLD_MERCHANT_CODE.getValue());
			}
			// 刷卡機型
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE_NAME.getValue())){
				sqlStatement.addSelectClause("edcType.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE_NAME.getValue());
				
				fromBuilder.append(" left join ").append(schema).append(".DMM_ASSET_TYPE edcType on edcType.ASSET_TYPE_ID = caseHandle.EDC_TYPE ");
			}
			// 設備序號、設備啟用日、倉別、財產編號
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_SERIAL_NUMBER.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.ENABLE_DATE.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.WAREHOUSE_NAME.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PROPERTY_ID.getValue())){
				// 設備序號
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_SERIAL_NUMBER.getValue())){
					sqlStatement.addSelectClause("edcLink.SERIAL_NUMBER", SrmCaseHandleInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
					sqlStatement.addSelectClause("edcLink.SERIAL_NUMBER", SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_SERIAL_NUMBER.getValue());
				}
				// 設備啟用日
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.ENABLE_DATE.getValue())){
					sqlStatement.addSelectClause("edcLink.ENABLE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.ENABLE_DATE.getValue());
				}
				// 倉別
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.WAREHOUSE_NAME.getValue())){
					sqlStatement.addSelectClause("wareHouse.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.WAREHOUSE_NAME.getValue());
				}
				// 財產編號
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PROPERTY_ID.getValue())){
					sqlStatement.addSelectClause("edcLink.PROPERTY_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.PROPERTY_ID.getValue());
				}
				
				// 若edc未關聯鏈接當
				if(!queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE_CONTRACT.getValue())){
					// 是否查歷史案件
					if(formDTO.getIsInstant()){
						fromBuilder.append(" left join ").append(schema).append(".SRM_CASE_ASSET_LINK edcLink on edcLink.CASE_ID = caseHandle.CASE_ID and edcLink.ITEM_TYPE = :caseLinkEdcType ");
					} else {
						fromBuilder.append(" left join ").append(schema).append(".SRM_HISTORY_CASE_ASSET_LINK edcLink on edcLink.CASE_ID = caseHandle.CASE_ID and edcLink.ITEM_TYPE = :caseLinkEdcType ");
					}
					fromBuilder.append(" and edcLink.IS_LINK = 'Y' ");
				}
				// 倉別
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.WAREHOUSE_NAME.getValue())){
					fromBuilder.append(" left join ").append(schema).append(".BIM_WAREHOUSE wareHouse on wareHouse.WAREHOUSE_ID = edcLink.WAREHOUSE_ID ");
				}
			}
			// 設備開啟的功能
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.OPEN_FUNCTION_NAME.getValue())){
				selectBuilder.delete(0, selectBuilder.length());
				// 是否查歷史案件
				if(formDTO.getIsInstant()){
					selectBuilder.append("stuff(( SELECT ',' + RTRIM( supportedFun.ITEM_NAME ) FROM ").append(schema).append(".SRM_CASE_ASSET_FUNCTION caseAssetFun ");
				} else {
					selectBuilder.append("stuff(( SELECT ',' + RTRIM( supportedFun.ITEM_NAME ) FROM ").append(schema).append(".SRM_HISTORY_CASE_ASSET_FUNCTION caseAssetFun ");
				}
				selectBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF supportedFun on supportedFun.BPTD_CODE = :supportedFunctionParam");
				selectBuilder.append(" and supportedFun.ITEM_VALUE = caseAssetFun.FUNCTION_ID where caseAssetFun.CASE_ID = caseHandle.CASE_ID ");
				selectBuilder.append(" and caseAssetFun.FUNCTION_CATEGORY = :caseLinkEdcType FOR XML path('') ) ,1 , 1 ,'')");
				sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.OPEN_FUNCTION_NAME.getValue());
			}
			// 程式名稱
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.APPLICATION_NAME.getValue())){
				selectBuilder.delete(0, selectBuilder.length());
				selectBuilder.append("case when isnull(caseHandle.SOFTWARE_VERSION, '') = '' then null ");
				selectBuilder.append(" else (CONCAT( CONVERT( VARCHAR( 100 ) ,application.NAME ,111 ) ,'【' ,CONVERT( VARCHAR( 100 ) ,application.VERSION ,111 ) ,'】')) end");
				sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.APPLICATION_NAME.getValue());
				sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.SOFTWARE_VERSION_NAME.getValue());
				
				fromBuilder.append(" left join ").append(schema).append(".PVM_APPLICATION application ON application.APPLICATION_ID = caseHandle.SOFTWARE_VERSION ");
			}
			// 雙模組模式
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.MULTI_MODULE_NAME.getValue())){
				sqlStatement.addSelectClause("doubleModule.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.MULTI_MODULE_NAME.getValue());
				
				fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF doubleModule on doubleModule.BPTD_CODE = :doubleModuleParam ");
				fromBuilder.append(" and doubleModule.ITEM_VALUE = caseHandle.MULTI_MODULE ");
			}
			// ECR連線
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.ECR_CONNECTION.getValue())){
				sqlStatement.addSelectClause("caseHandle.ECR_CONNECTION", SrmCaseHandleInfoDTO.ATTRIBUTE.ECR_CONNECTION.getValue());
			}
			// ECR連線名稱
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.ECR_CONNECTION_NAME.getValue())){
				sqlStatement.addSelectClause("ecrLine.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.ECR_CONNECTION_NAME.getValue());
				
				fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF ecrLine on ecrLine.BPTD_CODE = :ecrLineParam ");
				fromBuilder.append(" and ecrLine.ITEM_VALUE = caseHandle.ECR_CONNECTION ");
			}
			// 網路線
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.NETWORK_LINE_NUMBER.getValue())){
				selectBuilder.delete(0, selectBuilder.length());
				// 是否查歷史案件
				if(formDTO.getIsInstant()){
					selectBuilder.append(" (select sum(suppliesLink.NUMBER) from ").append(schema).append(".SRM_CASE_ASSET_LINK suppliesLink ");
				} else {
					selectBuilder.append(" (select sum(suppliesLink.NUMBER) from ").append(schema).append(".SRM_HISTORY_CASE_ASSET_LINK suppliesLink ");
				}
				selectBuilder.append(" where caseHandle.CASE_ID = suppliesLink.CASE_ID ");
				selectBuilder.append(" and suppliesLink.ITEM_TYPE = :caseLinkSupplies and suppliesLink.ITEM_CATEGORY = :networkLineParam)");
				sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.NETWORK_LINE_NUMBER.getValue());
			}
			// 週邊設備1
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_NAME.getValue())){
				sqlStatement.addSelectClause("peripherals.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_NAME.getValue());
				
				fromBuilder.append(" left join ").append(schema).append(".DMM_ASSET_TYPE peripherals on peripherals.ASSET_TYPE_ID = caseHandle.PERIPHERALS ");
			}
			// 週邊設備1功能
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION_NAME.getValue())){
				selectBuilder.delete(0, selectBuilder.length());
				// 是否查歷史案件
				if(formDTO.getIsInstant()){
					selectBuilder.append("stuff(( SELECT ',' + RTRIM( supportedFun.ITEM_NAME ) FROM ").append(schema).append(".SRM_CASE_ASSET_FUNCTION caseAssetFun ");
				} else {
					selectBuilder.append("stuff(( SELECT ',' + RTRIM( supportedFun.ITEM_NAME ) FROM ").append(schema).append(".SRM_HISTORY_CASE_ASSET_FUNCTION caseAssetFun ");
				}
				selectBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF supportedFun on supportedFun.BPTD_CODE = :supportedFunctionParam");
				selectBuilder.append(" and supportedFun.ITEM_VALUE = caseAssetFun.FUNCTION_ID where caseAssetFun.CASE_ID = caseHandle.CASE_ID ");
				selectBuilder.append(" and caseAssetFun.FUNCTION_CATEGORY = :caseLinkPeripherals FOR XML path('') ) ,1 , 1 ,'')");
				sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION_NAME.getValue());
			}
			// 週邊設備1序號、週邊設備1合約編號
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_SERIAL_NUMBER.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_CONTRACT.getValue())){
				// 週邊設備1序號
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_SERIAL_NUMBER.getValue())){
					sqlStatement.addSelectClause("peripheralsLink.SERIAL_NUMBER", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_SERIAL_NUMBER.getValue());
				}
				// 週邊設備1合約編號
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_CONTRACT.getValue())){
					sqlStatement.addSelectClause("peripheralsContract.CONTRACT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_CONTRACT.getValue());
				}
				
				// 是否查歷史案件
				if(formDTO.getIsInstant()){
					fromBuilder.append(" left join ").append(schema).append(".SRM_CASE_ASSET_LINK peripheralsLink on peripheralsLink.CASE_ID = caseHandle.CASE_ID ");
				} else {
					fromBuilder.append(" left join ").append(schema).append(".SRM_HISTORY_CASE_ASSET_LINK peripheralsLink on peripheralsLink.CASE_ID = caseHandle.CASE_ID ");
				}
				fromBuilder.append(" and peripheralsLink.ITEM_TYPE = :caseLinkPeripherals and peripheralsLink.IS_LINK = 'Y' ");
				// 週邊設備1合約編號
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_CONTRACT.getValue())){
					fromBuilder.append(" left join ").append(schema).append(".BIM_CONTRACT peripheralsContract on peripheralsContract.CONTRACT_ID = peripheralsLink.CONTRACT_ID ");
				}
			}
			// 週邊設備2
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_NAME.getValue())){
				sqlStatement.addSelectClause("peripherals2.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_NAME.getValue());

				fromBuilder.append(" left join ").append(schema).append(".DMM_ASSET_TYPE peripherals2 on peripherals2.ASSET_TYPE_ID = caseHandle.PERIPHERALS2 ");
			}
			// 週邊設備2功能
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION2_NAME.getValue())){
				selectBuilder.delete(0, selectBuilder.length());
				// 是否查歷史案件
				if(formDTO.getIsInstant()){
					selectBuilder.append("stuff(( SELECT ',' + RTRIM( supportedFun.ITEM_NAME ) FROM ").append(schema).append(".SRM_CASE_ASSET_FUNCTION caseAssetFun ");
				} else {
					selectBuilder.append("stuff(( SELECT ',' + RTRIM( supportedFun.ITEM_NAME ) FROM ").append(schema).append(".SRM_HISTORY_CASE_ASSET_FUNCTION caseAssetFun ");
				}
				selectBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF supportedFun on supportedFun.BPTD_CODE = :supportedFunctionParam");
				selectBuilder.append(" and supportedFun.ITEM_VALUE = caseAssetFun.FUNCTION_ID where caseAssetFun.CASE_ID = caseHandle.CASE_ID ");
				selectBuilder.append(" and caseAssetFun.FUNCTION_CATEGORY = :caseLinkPeripherals2 FOR XML path('') ) ,1 , 1 ,'')");
				sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION2_NAME.getValue());
			}
			// 週邊設備2序號、週邊設備2合約編號
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_SERIAL_NUMBER.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_CONTRACT.getValue())){
				// 週邊設備2序號
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_SERIAL_NUMBER.getValue())){
					sqlStatement.addSelectClause("peripherals2Link.SERIAL_NUMBER", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_SERIAL_NUMBER.getValue());
				}
				// 週邊設備2合約編號
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_CONTRACT.getValue())){
					sqlStatement.addSelectClause("peripherals2Contract.CONTRACT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_CONTRACT.getValue());
				}
				
				// 是否查歷史案件
				if(formDTO.getIsInstant()){
					fromBuilder.append(" left join ").append(schema).append(".SRM_CASE_ASSET_LINK peripherals2Link on peripherals2Link.CASE_ID = caseHandle.CASE_ID ");
				} else {
					fromBuilder.append(" left join ").append(schema).append(".SRM_HISTORY_CASE_ASSET_LINK peripherals2Link on peripherals2Link.CASE_ID = caseHandle.CASE_ID ");
				}
				fromBuilder.append(" and peripherals2Link.ITEM_TYPE = :caseLinkPeripherals2 and peripherals2Link.IS_LINK = 'Y' ");
				// 週邊設備2合約編號
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_CONTRACT.getValue())){
					fromBuilder.append(" left join ").append(schema).append(".BIM_CONTRACT peripherals2Contract on peripherals2Contract.CONTRACT_ID = peripherals2Link.CONTRACT_ID ");
				}
			}
			
			// 週邊設備3
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_NAME.getValue())){
				sqlStatement.addSelectClause("peripherals3.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_NAME.getValue());

				fromBuilder.append(" left join ").append(schema).append(".DMM_ASSET_TYPE peripherals3 on peripherals3.ASSET_TYPE_ID = caseHandle.PERIPHERALS3 ");
			}
			// 週邊設備3功能
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION3_NAME.getValue())){
				selectBuilder.delete(0, selectBuilder.length());
				// 是否查歷史案件
				if(formDTO.getIsInstant()){
					selectBuilder.append("stuff(( SELECT ',' + RTRIM( supportedFun.ITEM_NAME ) FROM ").append(schema).append(".SRM_CASE_ASSET_FUNCTION caseAssetFun ");
				} else {
					selectBuilder.append("stuff(( SELECT ',' + RTRIM( supportedFun.ITEM_NAME ) FROM ").append(schema).append(".SRM_HISTORY_CASE_ASSET_FUNCTION caseAssetFun ");
				}
				selectBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF supportedFun on supportedFun.BPTD_CODE = :supportedFunctionParam");
				selectBuilder.append(" and supportedFun.ITEM_VALUE = caseAssetFun.FUNCTION_ID where caseAssetFun.CASE_ID = caseHandle.CASE_ID ");
				selectBuilder.append(" and caseAssetFun.FUNCTION_CATEGORY = :caseLinkPeripherals3 FOR XML path('') ) ,1 , 1 ,'')");
				sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION3_NAME.getValue());
			}
			// 週邊設備3序號、週邊設備3合約編號
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_SERIAL_NUMBER.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_CONTRACT.getValue())){
				// 週邊設備3序號
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_SERIAL_NUMBER.getValue())){
					sqlStatement.addSelectClause("peripherals3Link.SERIAL_NUMBER", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_SERIAL_NUMBER.getValue());
				}
				// 週邊設備3合約編號
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_CONTRACT.getValue())){
					sqlStatement.addSelectClause("peripherals3Contract.CONTRACT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_CONTRACT.getValue());
				}
				
				// 是否查歷史案件
				if(formDTO.getIsInstant()){
					fromBuilder.append(" left join ").append(schema).append(".SRM_CASE_ASSET_LINK peripherals3Link on peripherals3Link.CASE_ID = caseHandle.CASE_ID ");
				} else {
					fromBuilder.append(" left join ").append(schema).append(".SRM_HISTORY_CASE_ASSET_LINK peripherals3Link on peripherals3Link.CASE_ID = caseHandle.CASE_ID ");
				}
				fromBuilder.append(" and peripherals3Link.ITEM_TYPE = :caseLinkPeripherals3 and peripherals3Link.IS_LINK = 'Y' ");
				// 週邊設備3合約編號
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_CONTRACT.getValue())){
					fromBuilder.append(" left join ").append(schema).append(".BIM_CONTRACT peripherals3Contract on peripherals3Contract.CONTRACT_ID = peripherals3Link.CONTRACT_ID ");
				}
			}
			// 連線方式
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CONNECTION_TYPE_NAME.getValue())){
				selectBuilder.delete(0, selectBuilder.length());
				// 是否查歷史案件
				if(formDTO.getIsInstant()){
					selectBuilder.append("stuff(( SELECT '，' + RTRIM( commMode.ITEM_NAME ) FROM ").append(schema).append(".SRM_CASE_COMM_MODE caseCommMode ");
				} else {
					selectBuilder.append("stuff(( SELECT '，' + RTRIM( commMode.ITEM_NAME ) FROM ").append(schema).append(".SRM_HISTORY_CASE_COMM_MODE caseCommMode ");
				}
				selectBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF commMode on commMode.BPTD_CODE = :commModeParam");
				selectBuilder.append(" and commMode.ITEM_VALUE = caseCommMode.COMM_MODE_ID where caseCommMode.CASE_ID = caseHandle.CASE_ID ");
				selectBuilder.append(" FOR XML path('') ) ,1 , 1 ,'')");
				sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONNECTION_TYPE_NAME.getValue());
			}
			// LOGO
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.LOGO_STYLE.getValue())){
				sqlStatement.addSelectClause("caseHandle.LOGO_STYLE", SrmCaseHandleInfoDTO.ATTRIBUTE.LOGO_STYLE.getValue());
			}
			// 是否開啟加密
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.IS_OPEN_ENCRYPT.getValue())){
				sqlStatement.addSelectClause("caseHandle.IS_OPEN_ENCRYPT", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_OPEN_ENCRYPT.getValue());
			}
			// 電子化繳費平台
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_PAY_PLATFORM.getValue())){
				sqlStatement.addSelectClause("caseHandle.ELECTRONIC_PAY_PLATFORM", SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_PAY_PLATFORM.getValue());
			}
			// 電子發票載具
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_INVOICE.getValue())){
				sqlStatement.addSelectClause("caseHandle.ELECTRONIC_INVOICE", SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_INVOICE.getValue());
			}
			// 銀聯閃付
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_QUICK_PASS.getValue())){
				sqlStatement.addSelectClause("caseHandle.CUP_QUICK_PASS", SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_QUICK_PASS.getValue());
			}
			// 寬頻連線
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.NET_VENDOR_NAME.getValue())){
				sqlStatement.addSelectClause("netVendor.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.NET_VENDOR_NAME.getValue());
				
				fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF netVendor on netVendor.BPTD_CODE = :netVendorParam ");
				fromBuilder.append(" and netVendor.ITEM_VALUE = caseHandle.NET_VENDOR_ID ");
			}
			// 本機IP
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.LOCALHOST_IP.getValue())){
				sqlStatement.addSelectClause("caseHandle.LOCALHOST_IP", SrmCaseHandleInfoDTO.ATTRIBUTE.LOCALHOST_IP.getValue());
			}
			// Netmask
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.NETMASK.getValue())){
				sqlStatement.addSelectClause("caseHandle.NETMASK", SrmCaseHandleInfoDTO.ATTRIBUTE.NETMASK.getValue());
			}
			// Getway
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.GATEWAY.getValue())){
				sqlStatement.addSelectClause("caseHandle.GATEWAY", SrmCaseHandleInfoDTO.ATTRIBUTE.GATEWAY.getValue());
			}
			// 其他說明
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.DESCRIPTION.getValue())){
				sqlStatement.addSelectClause("caseHandle.DESCRIPTION", SrmCaseHandleInfoDTO.ATTRIBUTE.DESCRIPTION.getValue());
			}
			// MID2
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE_OTHER.getValue())){
				selectBuilder.delete(0, selectBuilder.length());
				// 是否查歷史案件
				if(formDTO.getIsInstant()){
					selectBuilder.append("stuff((SELECT '，' + RTRIM( transParam.MERCHANT_CODE_OTHER) FROM ").append(schema).append(".SRM_CASE_TRANSACTION_PARAMETER transParam ");
				} else {
					selectBuilder.append("stuff((SELECT '，' + RTRIM( transParam.MERCHANT_CODE_OTHER) FROM ").append(schema).append(".SRM_HISTORY_CASE_TRANSACTION_PARAMETER transParam ");
				}
				selectBuilder.append(" WHERE transParam.CASE_ID = caseHandle.CASE_ID and isnull(transParam.MERCHANT_CODE_OTHER, '') <> '' FOR XML path('') ) , 1 , 1 , '')");
				sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE_OTHER.getValue());
			}
			// CUP交易
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_TRANS_TYPE.getValue())){
				selectBuilder.delete(0, selectBuilder.length());
				// 是否查歷史案件
				if(formDTO.getIsInstant()){
					selectBuilder.append("(case when ( SELECT count(1) from ").append(schema).append(".SRM_CASE_TRANSACTION_PARAMETER transParam ");
				} else {
					selectBuilder.append("(case when ( SELECT count(1) from ").append(schema).append(".SRM_HISTORY_CASE_TRANSACTION_PARAMETER transParam ");
				}
				selectBuilder.append(" where transParam.CASE_ID = caseHandle.CASE_ID and transParam.TRANSACTION_TYPE = :cupParam) > 0 ");
				selectBuilder.append(" then( 'V' ) ELSE( null ) END)");
				sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_TRANS_TYPE.getValue());
			}
			// DCC交易
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.DCC_TRANS_TYPE.getValue())){
				selectBuilder.delete(0, selectBuilder.length());
				// 是否查歷史案件
				if(formDTO.getIsInstant()){
					selectBuilder.append("(case when ( SELECT count(1) from ").append(schema).append(".SRM_CASE_TRANSACTION_PARAMETER transParam ");
				} else {
					selectBuilder.append("(case when ( SELECT count(1) from ").append(schema).append(".SRM_HISTORY_CASE_TRANSACTION_PARAMETER transParam ");
				}
				selectBuilder.append(" where transParam.CASE_ID = caseHandle.CASE_ID and transParam.TRANSACTION_TYPE = :dccParam) > 0 ");
				selectBuilder.append(" then( 'V' ) ELSE( null ) END)");
				sqlStatement.addSelectClause(selectBuilder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.DCC_TRANS_TYPE.getValue());
			}
			// AE-MID、AE-TID
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.AE_MID.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.AE_TID.getValue())){
				// AE-MID
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.AE_MID.getValue())){
					sqlStatement.addSelectClause("aeTrans.MERCHANT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.AE_MID.getValue());
				}
				// AE-TID
				if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.AE_TID.getValue())){
					sqlStatement.addSelectClause("aeTrans.TID", SrmCaseHandleInfoDTO.ATTRIBUTE.AE_TID.getValue());
				}
				
				// 是否查歷史案件
				if(formDTO.getIsInstant()){
					fromBuilder.append(" left join ").append(schema).append(".SRM_CASE_TRANSACTION_PARAMETER aeTrans on aeTrans.CASE_ID = caseHandle.CASE_ID and aeTrans.TRANSACTION_TYPE = :aeParam ");
				} else {
					fromBuilder.append(" left join ").append(schema).append(".SRM_HISTORY_CASE_TRANSACTION_PARAMETER aeTrans on aeTrans.CASE_ID = caseHandle.CASE_ID and aeTrans.TRANSACTION_TYPE = :aeParam ");
				}
			}
			// TMS參數說明
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.TMS_PARAM_DESC.getValue())){
				sqlStatement.addSelectClause("caseHandle.TMS_PARAM_DESC", SrmCaseHandleInfoDTO.ATTRIBUTE.TMS_PARAM_DESC.getValue());
			}
			// 異動說明
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATED_DESCRIPTION.getValue())){
				sqlStatement.addSelectClause("caseHandle.UPDATED_DESCRIPTION", SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATED_DESCRIPTION.getValue());
			}
			// 報修原因
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.REPAIR_REASON_NAME.getValue())){
				sqlStatement.addSelectClause("repairReason.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.REPAIR_REASON_NAME.getValue());
				
				fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF repairReason on repairReason.BPTD_CODE = :repairReasonParam ");
				fromBuilder.append(" and repairReason.ITEM_VALUE = caseHandle.REPAIR_REASON ");
			}
			
			// 責任歸屬
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSIBITY_NAME.getValue())){
				sqlStatement.addSelectClause("responAttribution.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSIBITY_NAME.getValue());
				
				fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF responAttribution on responAttribution.BPTD_CODE = :responAttributionParam ");
				fromBuilder.append(" and responAttribution.ITEM_VALUE = caseHandle.RESPONSIBITY ");
			}
			// 報修問題原因
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_REASON_NAME.getValue())){
				sqlStatement.addSelectClause("problemReason.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_REASON_NAME.getValue());
				
				// 報修問題原因
				fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF problemReason ON problemReason.ITEM_VALUE = caseHandle.PROBLEM_REASON");
				fromBuilder.append(" and caseHandle.PROBLEM_REASON_CODE is not null and problemReason.BPTD_CODE = caseHandle.PROBLEM_REASON_CODE ");
			}
			// 報修問題原因分類
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_REASON_CATEGORY_NAME.getValue())){
				sqlStatement.addSelectClause("problemReasonType.PT_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_REASON_CATEGORY_NAME.getValue());
				
				// 若未查詢報修問題原因
				if(!queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_REASON_NAME.getValue())){
					// 報修問題原因
					fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF problemReason ON problemReason.ITEM_VALUE = caseHandle.PROBLEM_REASON");
					fromBuilder.append(" and caseHandle.PROBLEM_REASON_CODE is not null and problemReason.BPTD_CODE = caseHandle.PROBLEM_REASON_CODE ");
				}
				// 報修問題原因分類
				fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_TYPE_DEF problemReasonType on problemReason.BPTD_CODE = problemReasonType.BPTD_CODE");
				fromBuilder.append(" AND problemReasonType.VALUE_SCOPE_OPERATOR2 =:problemReasonCategoryParam ");
			}
			// 報修解決方式
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_SOLUTION_NAME.getValue())){
				sqlStatement.addSelectClause("problemSolution.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_SOLUTION_NAME.getValue());
				
				// 報修解決方式
				fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF problemSolution ON problemSolution.ITEM_VALUE = caseHandle.PROBLEM_SOLUTION");
				fromBuilder.append(" and caseHandle.PROBLEM_SOLUTION_CODE is not null and problemSolution.BPTD_CODE = caseHandle.PROBLEM_SOLUTION_CODE ");
			}
			// 報修解決方式分類
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_SOLUTION_CATEGORY_NAME.getValue())){
				sqlStatement.addSelectClause("problemSolutionType.PT_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_SOLUTION_CATEGORY_NAME.getValue());
				
				// 若未查詢報修解決方式
				if(!queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_SOLUTION_NAME.getValue())){
					// 報修解決方式
					fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF problemSolution ON problemSolution.ITEM_VALUE = caseHandle.PROBLEM_SOLUTION");
					fromBuilder.append(" and caseHandle.PROBLEM_SOLUTION_CODE is not null and problemSolution.BPTD_CODE = caseHandle.PROBLEM_SOLUTION_CODE ");
				}
				// 報修解決方式分類
				fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_TYPE_DEF problemSolutionType on problemSolution.BPTD_CODE = problemSolutionType.BPTD_CODE");
				fromBuilder.append(" AND problemSolutionType.VALUE_SCOPE_OPERATOR2 =:problemSolutionCategoryParam ");
			}
			
			// 應回應時間	//Bug #3604
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_RESPONSE_DATE.getValue()) || queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_CONDITION.getValue())){
				sqlStatement.addSelectClause("caseHandle.ACCEPTABLE_RESPONSE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_RESPONSE_DATE.getValue());
			}
			// 應到場時間	//Bug #3604
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_ARRIVE_DATE.getValue()) ||queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_CONDITION.getValue())){
				sqlStatement.addSelectClause("caseHandle.ACCEPTABLE_ARRIVE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_ARRIVE_DATE.getValue());
			}
			// 應完修時間	//Bug #3604
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_FINISH_DATE.getValue()) || queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_CONDITION.getValue())){
				sqlStatement.addSelectClause("caseHandle.ACCEPTABLE_FINISH_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_FINISH_DATE.getValue());
			}
			// 執行派工人員
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_USER_NAME.getValue())){
				sqlStatement.addSelectClause("caseHandle.DISPATCH_USER_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_USER_NAME.getValue());
			}
			// 派工時間
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_DATE.getValue())){
				sqlStatement.addSelectClause("caseHandle.DISPATCH_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_DATE.getValue());
			}
			// 執行回應人員
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_USER_NAME.getValue())){
				sqlStatement.addSelectClause("caseHandle.RESPONSE_USER_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_USER_NAME.getValue());
			}
			// 回應時間	//Bug #3604
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_DATE.getValue()) || queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_CONDITION.getValue())){
				sqlStatement.addSelectClause("caseHandle.RESPONSE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_DATE.getValue());
			}
			// 執行到場人員
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_USER_NAME.getValue())){
				sqlStatement.addSelectClause("caseHandle.ARRIVE_USER_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_USER_NAME.getValue());
			}
			// 到場時間	//Bug #3604
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_DATE.getValue()) || queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_CONDITION.getValue())){
				sqlStatement.addSelectClause("caseHandle.ARRIVE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_DATE.getValue());
			}
			// 執行完修人員
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_USER_NAME.getValue())){
				sqlStatement.addSelectClause("caseHandle.COMPLETE_USER_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_USER_NAME.getValue());
			}
			// 執行完修部門
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DEPARTMENT_NAME.getValue())){
				sqlStatement.addSelectClause("(case when completeDept.DEPT_NAME is not null then completeDept.DEPT_NAME else completeGroup.ITEM_NAME end)", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DEPARTMENT_NAME.getValue());
				
				fromBuilder.append(" left join ").append(schema).append(".BIM_DEPARTMENT completeDept on completeDept.DEPT_CODE = caseHandle.COMPLETE_DEPARTMENT_ID ");
				fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF completeGroup on completeGroup.BPTD_CODE = :caseGroupParam ");
				fromBuilder.append(" and completeGroup.ITEM_VALUE = caseHandle.COMPLETE_DEPARTMENT_ID ");
			}
			// 完修時間	//Bug #3604
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue()) || queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_CONDITION.getValue())){
				sqlStatement.addSelectClause("caseHandle.COMPLETE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue());
			}
			// 執行簽收人員
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.ANALYZE_USER_NAME.getValue())){
				sqlStatement.addSelectClause("caseHandle.ANALYZE_USER_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.ANALYZE_USER_NAME.getValue());
			}
			// 簽收時間
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.ANALYZE_DATE.getValue())){
				sqlStatement.addSelectClause("caseHandle.ANALYZE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.ANALYZE_DATE.getValue());
			}
			// 執行結案人員
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CLOSE_USER_NAME.getValue())){
				sqlStatement.addSelectClause("caseHandle.CLOSE_USER_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CLOSE_USER_NAME.getValue());
			}
			// 結案時間
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CLOSE_DATE.getValue())){
				sqlStatement.addSelectClause("caseHandle.CLOSE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.CLOSE_DATE.getValue());
			}
			// 處理方式
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PROCESS_TYPE_NAME.getValue())){
				sqlStatement.addSelectClause("processType.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.PROCESS_TYPE_NAME.getValue());
				
				fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF processType on processType.BPTD_CODE = :processTypeParam ");
				fromBuilder.append(" and processType.ITEM_VALUE = caseHandle.PROCESS_TYPE ");
			}
			// 目前處理人
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_PROCESS_USERNAME.getValue())){
				sqlStatement.addSelectClause("caseHandle.DISPATCH_PROCESS_USERNAME", SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_PROCESS_USERNAME.getValue());
			}
			// 最後異動人
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue())){
				sqlStatement.addSelectClause("caseHandle.UPDATED_BY_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			}
			// 最後異動日期
			
			// 進件人員
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_BY_NAME.getValue())){
				sqlStatement.addSelectClause("caseHandle.CREATED_BY_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			}
			// 進件日期
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue())){
				sqlStatement.addSelectClause("caseHandle.CREATED_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue());
			}
			//CR #2869 新增三個欄位 2017/11/22
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.FIRST_DESCRIPTION.getValue())){
				sqlStatement.addSelectClause("caseHandle.FIRST_DESCRIPTION", SrmCaseHandleInfoDTO.ATTRIBUTE.FIRST_DESCRIPTION.getValue());
			}
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.SECOND_DESCRIPTION.getValue())){
				sqlStatement.addSelectClause("caseHandle.SECOND_DESCRIPTION", SrmCaseHandleInfoDTO.ATTRIBUTE.SECOND_DESCRIPTION.getValue());
			}
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.THIRD_DESCRIPTION.getValue())){
				sqlStatement.addSelectClause("caseHandle.THIRD_DESCRIPTION", SrmCaseHandleInfoDTO.ATTRIBUTE.THIRD_DESCRIPTION.getValue());
			}			
			
			// 列印-預計完成日
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.EXPECTED_COMPLETION_DATE.getValue())){
				sqlStatement.addSelectClause("caseHandle.EXPECTED_COMPLETION_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.EXPECTED_COMPLETION_DATE.getValue());
			}
			// Task #3205 是否執行過延期
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.HAS_DELAY.getValue())){
				sqlStatement.addSelectClause("caseHandle.HAS_DELAY", SrmCaseHandleInfoDTO.ATTRIBUTE.HAS_DELAY.getValue());
			}
			//物流廠商
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.LOGISTICS_VENDOR.getValue())){
				sqlStatement.addSelectClause("logisticsVendor.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.LOGISTICS_VENDOR.getValue());
				fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF logisticsVendor on logisticsVendor.BPTD_CODE = :logisticsVendor ");
				fromBuilder.append(" and logisticsVendor.ITEM_VALUE = caseHandle.LOGISTICS_VENDOR ");
			}
			//物流編號
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.LOGISTICS_NUMBER.getValue())){
				sqlStatement.addSelectClause("caseHandle.LOGISTICS_NUMBER", SrmCaseHandleInfoDTO.ATTRIBUTE.LOGISTICS_NUMBER.getValue());
			}
			// Receipt_type
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.RECEIPT_TYPE.getValue())){
				sqlStatement.addSelectClause("receiptTypeItem.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.RECEIPT_TYPE_NAME.getValue());
				sqlStatement.addSelectClause("caseHandle.RECEIPT_TYPE", SrmCaseHandleInfoDTO.ATTRIBUTE.RECEIPT_TYPE.getValue());
				fromBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF receiptTypeItem on receiptTypeItem.BPTD_CODE = :receiptType ");
				fromBuilder.append(" and receiptTypeItem.ITEM_VALUE = caseHandle.RECEIPT_TYPE ");
			}
			
			// 關聯表 from條件
			selectBuilder.delete(0, selectBuilder.length());
			// 是否查歷史案件
			if(formDTO.getIsInstant()){
				selectBuilder.append(schema).append(".SRM_CASE_HANDLE_INFO caseHandle ");
			} else {
				selectBuilder.append(schema).append(".SRM_HISTORY_CASE_HANDLE_INFO caseHandle ");
			}
			// 派工單位名稱
			selectBuilder.append(" left join ").append(schema).append(".BIM_DEPARTMENT dept on dept.DEPT_CODE = caseHandle.DISPATCH_DEPT_ID ");
			selectBuilder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF caseGroup on caseGroup.BPTD_CODE = :caseGroupParam ");
			selectBuilder.append(" and caseGroup.ITEM_VALUE = caseHandle.DISPATCH_DEPT_ID ");
			
			// 案件建案人員所在公司
			selectBuilder.append(" left join ").append(schema).append(".ADM_USER vendorService on vendorService.USER_ID = isnull(caseHandle.CREATED_BY_ID,'') ");
			
			// 追加查詢列關聯條件
			selectBuilder.append(fromBuilder.toString());
			sqlStatement.addFromExpression(selectBuilder.toString());
			
			
			// 查詢where條件
			// 查詢條件：進件日期 起
			if(formDTO.getQueryCreateDateStart() != null){
				sqlStatement.addWhereClause("caseHandle.CREATED_DATE >= :queryCreateDateStart", formDTO.getQueryCreateDateStart());
			}
			// 查詢條件：進件日期 迄
			if(formDTO.getQueryCreateDateEnd() != null){
				sqlStatement.addWhereClause("caseHandle.CREATED_DATE < :queryCreateDateEnd", formDTO.getQueryCreateDateEnd());
			}
			//Task #3584 (客戶=台新)或(客戶=宣揚&需求單號有值&裝機案件編號有值)
			if (IAtomsConstants.PARAM_YES.equals(formDTO.getQueryTsbFlag())){
				if(StringUtils.hasText(formDTO.getBccCustomerId())) {
					sqlStatement.addWhereClause("((caseHandle.CUSTOMER_ID ='"
								+ formDTO.getBccCustomerId()
								+"' and caseHandle.INSTALL_CASE_ID is not null and caseHandle.INSTALL_CASE_ID<>'' and (charindex('EI', caseHandle.REQUIREMENT_NO)=1 or charindex('EC', caseHandle.REQUIREMENT_NO)=1 or charindex('EM', caseHandle.REQUIREMENT_NO)=1 or charindex('EU', caseHandle.REQUIREMENT_NO)=1 or charindex('EA', caseHandle.REQUIREMENT_NO)=1 or charindex('ER', caseHandle.REQUIREMENT_NO)=1 ) ) or caseHandle.CUSTOMER_ID ='" 
								+ formDTO.getQueryCompanyId() + "' ) ");
				} else {
					sqlStatement.addWhereClause("caseHandle.CUSTOMER_ID =:customerId", formDTO.getQueryCompanyId());
				}
			} else {
				// 查詢條件：客戶
				if(StringUtils.hasText(formDTO.getQueryCompanyId())){
					sqlStatement.addWhereClause("caseHandle.CUSTOMER_ID =:customerId", formDTO.getQueryCompanyId());
				}
			}
			// 查詢條件：需求單號
			if(StringUtils.hasText(formDTO.getQueryRequirementNo())){
				sqlStatement.addWhereClause("caseHandle.REQUIREMENT_NO like :requirementNo", formDTO.getQueryRequirementNo() + IAtomsConstants.MARK_PERCENT);
			}
			// 查詢條件：案件編號
			if(StringUtils.hasText(formDTO.getQueryCaseId())){
				// 列印
				if(formDTO.getIsExport()){
					sqlStatement.addWhereClause(" caseHandle.CASE_ID in (:caseId) ");
				// 查詢
				} else {
					sqlStatement.addWhereClause("caseHandle.CASE_ID like :caseId", formDTO.getQueryCaseId() + IAtomsConstants.MARK_PERCENT);
				}
			}
			// 查詢條件：案件類別
			if(StringUtils.hasText(formDTO.getQueryCaseCategory())){
				sqlStatement.addWhereClause("caseHandle.CASE_CATEGORY in( :queryCaseCategory )");
			}
			// 查詢條件：刷卡機型
			if(StringUtils.hasText(formDTO.getQueryEdcType())){
				sqlStatement.addWhereClause("caseHandle.EDC_TYPE in( :queryEdcType )");
			}
			// 查詢條件：維護廠商
			if(StringUtils.hasText(formDTO.getQueryVendorId())){
				sqlStatement.addWhereClause("caseHandle.COMPANY_ID in( :queryVendorId )");
			}
			// 查詢條件：維護部門
			if(StringUtils.hasText(formDTO.getQueryVendorDept())){
				sqlStatement.addWhereClause("caseHandle.DISPATCH_DEPT_ID in( :queryVendorDept )");
			}
			// 查詢條件：應完成日期 起
			if(formDTO.getQueryAcceptableCompleteDateStart() != null){
				sqlStatement.addWhereClause("caseHandle.ACCEPTABLE_FINISH_DATE >= :queryAcceptableCompleteDateStart", formDTO.getQueryAcceptableCompleteDateStart());
			}
			// 查詢條件：應完成日期 迄
			if(formDTO.getQueryAcceptableCompleteDateEnd() != null){
				sqlStatement.addWhereClause("caseHandle.ACCEPTABLE_FINISH_DATE < :queryAcceptableCompleteDateEnd", formDTO.getQueryAcceptableCompleteDateEnd());
			}
			// 查詢條件：完修日期 起
			if(formDTO.getQueryCompleteDateStart() != null){
				sqlStatement.addWhereClause("caseHandle.COMPLETE_DATE >= :queryCompleteStartDate", formDTO.getQueryCompleteDateStart());
			}
			// 查詢條件：完修日期 迄
			if(formDTO.getQueryCompleteDateEnd() != null){
				sqlStatement.addWhereClause("caseHandle.COMPLETE_DATE < :queryCompleteEndDate", formDTO.getQueryCompleteDateEnd());
			}
			// 查詢條件：案件狀態
			if(StringUtils.hasText(formDTO.getQueryCaseStatus())){
				//Task #3349
				if (IAtomsConstants.PARAM_YES.equals(formDTO.getQueryTsbFlag())) {
					//DATEDIFF(second,'2008-12-29','2008-12-30')
					sqlStatement.addWhereClause("(((caseHandle.CASE_STATUS='ImmediateClose' or caseHandle.CASE_STATUS='Closed' or caseHandle.CASE_STATUS='Voided') and DATEDIFF(second,caseHandle.UPDATED_DATE,'" + DateTimeUtils.getCurrentTimestamp() + "') < 86400 ) or (caseHandle.CASE_STATUS in( :queryCaseStatus )))");
				}else{
					sqlStatement.addWhereClause("caseHandle.CASE_STATUS in( :queryCaseStatus )");
				}
			}
			// 查詢條件：特店代號
			if(StringUtils.hasText(formDTO.getQueryMerchatCode())){
				sqlStatement.addWhereClause("merchant.MERCHANT_CODE like :merchantCode", formDTO.getQueryMerchatCode() + IAtomsConstants.MARK_PERCENT);
			}
			// 查詢條件：特店名稱
			if(StringUtils.hasText(formDTO.getQueryMerchatName())){
				sqlStatement.addWhereClause("merchant.NAME like :merchantName", formDTO.getQueryMerchatName() + IAtomsConstants.MARK_PERCENT);
			}
			// 查詢條件：表頭（同對外名稱）
			if(StringUtils.hasText(formDTO.getQueryMerHeader())){
				sqlStatement.addWhereClause("merHeader.HEADER_NAME like :merHeaderName", formDTO.getQueryMerHeader() + IAtomsConstants.MARK_PERCENT);
			}
			// 查詢條件：是否專案
			if(StringUtils.hasText(formDTO.getQueryIsProject())){
				if(IAtomsConstants.YES.equals(formDTO.getQueryIsProject())){
					sqlStatement.addWhereClause("caseHandle.IS_PROJECT = :queryIsProject", formDTO.getQueryIsProject());
				} else if(IAtomsConstants.NO.equals(formDTO.getQueryIsProject())){
					sqlStatement.addWhereClause("caseHandle.IS_PROJECT = :queryIsProject", formDTO.getQueryIsProject());
				} else if(formDTO.getQueryIsProject().contains(IAtomsConstants.YES) && formDTO.getQueryIsProject().contains(IAtomsConstants.NO)){
					sqlStatement.addWhereClause(" isnull(caseHandle.IS_PROJECT, '') <> '' ");
				}
			}
			// 查詢條件：專案代碼
			if(StringUtils.hasText(formDTO.getQueryProjectCode())){
				sqlStatement.addWhereClause("caseHandle.PROJECT_CODE like :queryProjectCode", formDTO.getQueryProjectCode() + IAtomsConstants.MARK_PERCENT);
			}
			// 查詢條件：專案名稱
			if(StringUtils.hasText(formDTO.getQueryProjectName())){
				sqlStatement.addWhereClause("caseHandle.PROJECT_NAME like :queryProjectName", formDTO.getQueryProjectName() + IAtomsConstants.MARK_PERCENT);
			}
			// 設備開啟模式
			if(StringUtils.hasText(formDTO.getQueryOpenMode())){
				// 選擇內建
				if(formDTO.getQueryOpenMode().contains(IAtomsConstants.PARAM_BUILT_IN)){
					sqlStatement.addWhereClause(" isnull(caseHandle.BUILT_IN_FEATURE, '') <> '' ");
				}
				// 選擇外接
				if(formDTO.getQueryOpenMode().contains(IAtomsConstants.PARAM_EXTERNAL)){
					whereBuilder.delete(0, whereBuilder.length());
					whereBuilder.append(" ( (isnull(caseHandle.PERIPHERALS_FUNCTION, '') <> '')");
					whereBuilder.append(" or (isnull(caseHandle.PERIPHERALS_FUNCTION2, '') <> '')");
					whereBuilder.append(" or (isnull(caseHandle.PERIPHERALS_FUNCTION3, '') <> '') )");
					sqlStatement.addWhereClause(whereBuilder.toString());
				}
			}
			// 查詢條件：DTID
			if(StringUtils.hasText(formDTO.getQueryDtid())){
				sqlStatement.addWhereClause("caseHandle.DTID like :dtid", formDTO.getQueryDtid() + IAtomsConstants.MARK_PERCENT);
			}
			// 查詢條件：TID
			if(StringUtils.hasText(formDTO.getQueryTid())){
				whereBuilder.delete(0, whereBuilder.length());
				// 是否查歷史案件
				if(formDTO.getIsInstant()){
					whereBuilder.append(" exists(select 1 from ").append(schema).append(".SRM_CASE_TRANSACTION_PARAMETER");
				} else {
					whereBuilder.append(" exists(select 1 from ").append(schema).append(".SRM_HISTORY_CASE_TRANSACTION_PARAMETER");
				}
				whereBuilder.append(" transParameter where transParameter.CASE_ID = caseHandle.CASE_ID and transParameter.tid like :queryTid) ");
				sqlStatement.addWhereClause(whereBuilder.toString());
			}
			// 查詢條件：刷卡機暨週邊設備支援功能
			if(StringUtils.hasText(formDTO.getQuerySupportedFun())){
				whereBuilder.delete(0, whereBuilder.length());
				// 是否查歷史案件
				if(formDTO.getIsInstant()){
					whereBuilder.append(" exists(select 1 from ").append(schema).append(".SRM_CASE_ASSET_FUNCTION");
				} else {
					whereBuilder.append(" exists(select 1 from ").append(schema).append(".SRM_HISTORY_CASE_ASSET_FUNCTION");
				}
				whereBuilder.append(" assetFun where assetFun.CASE_ID = caseHandle.CASE_ID and assetFun.FUNCTION_ID in ( :queryFunctionId )) ");
				sqlStatement.addWhereClause(whereBuilder.toString());
			}
			// 查詢條件：大於或小於的限制條件
			if(StringUtils.hasText(formDTO.getQueryConditionOperator()) && formDTO.getQueryRepairTimes() != null){
				// 大於等於
				if(IAtomsConstants.MARK_EQUALANDPRE.equals(formDTO.getQueryConditionOperator())){
					sqlStatement.addWhereClause("caseHandle.REPAIR_TIMES >= :queryRepairTimes", formDTO.getQueryRepairTimes());
					// 等於
				} else if(IAtomsConstants.MARK_EQUALS.equals(formDTO.getQueryConditionOperator())){
					sqlStatement.addWhereClause("caseHandle.REPAIR_TIMES = :queryRepairTimes", formDTO.getQueryRepairTimes());
					// 小於等於
				} else if(IAtomsConstants.MARK_EQUALANDAFTER.equals(formDTO.getQueryConditionOperator())){
					sqlStatement.addWhereClause("caseHandle.REPAIR_TIMES <= :queryRepairTimes", formDTO.getQueryRepairTimes());
				}
			}
			// 查詢條件：SLA警示件查詢
			if(StringUtils.hasText(formDTO.getQueryWarningSla())){
				// 選擇SLA警示件
				if(IAtomsConstants.YES.equals(formDTO.getQueryWarningSla())){
					whereBuilder.delete(0, whereBuilder.length());
					// Task #2489 專案與查核案件，無須計算SLA
					whereBuilder.append("(caseHandle.CASE_CATEGORY <>:projectCaseCategory and ");
					whereBuilder.append(" caseHandle.CASE_CATEGORY <>:checkCaseCategory and ");
					// Task #2615 當案件進入 協調完成、線上排除、完修 時 查詢結果之 回應、到場、完修 無需顯示 警示或逾期 符號
					whereBuilder.append("(caseHandle.CASE_STATUS not in ( :ignoreCaseStatus ) and ( ");
					whereBuilder.append("(caseHandle.ACCEPTABLE_RESPONSE_DATE is not null ");
					whereBuilder.append(" and isnull(caseHandle.RESPONSE_DATE,isnull(caseHandle.COMPLETE_DATE,getdate())) >= caseHandle.ACCEPTABLE_RESPONSE_DATE) ");
					whereBuilder.append(" or (caseHandle.ACCEPTABLE_RESPONSE_DATE is not null and caseHandle.RESPONSE_WARNNING is not null ");
					whereBuilder.append(" and isnull(caseHandle.RESPONSE_DATE,isnull(caseHandle.COMPLETE_DATE,getdate())) >= dateadd(hour,-caseHandle.RESPONSE_WARNNING, caseHandle.ACCEPTABLE_RESPONSE_DATE))");
					whereBuilder.append(" or (isnull(caseHandle.COMPLETE_DATE,getdate()) >= dateadd(hour,-caseHandle.COMPLETE_WARNNING, caseHandle.ACCEPTABLE_FINISH_DATE))");
					whereBuilder.append(" )))");
					sqlStatement.addWhereClause(whereBuilder.toString());
				}
			}
			// 查詢條件：AO人員
			if(StringUtils.hasText(formDTO.getQueryAoName())){
				sqlStatement.addWhereClause("merHeader.AO_NAME like :aoName", formDTO.getQueryAoName() + IAtomsConstants.MARK_PERCENT);
			}
			// 有派工維護廠商
			if(!CollectionUtils.isEmpty(formDTO.getVendorIdList())){
				whereBuilder.delete(0, whereBuilder.length());
				if(CollectionUtils.isEmpty(formDTO.getDeptCodeList())){
					whereBuilder.append("(caseHandle.COMPANY_ID in (:vendorIdList)  AND caseHandle.DISPATCH_DATE is not null");
					whereBuilder.append(" and isnull(caseHandle.DISPATCH_DEPT_ID, '') = '' )");
				} else {
					whereBuilder.append("(caseHandle.COMPANY_ID in (:vendorIdList) AND caseHandle.DISPATCH_DATE is not null");
					whereBuilder.append(" and (isnull(caseHandle.DISPATCH_DEPT_ID, '') = '' or caseHandle.DISPATCH_DEPT_ID in (:deptCodeLists) ))");
				}
				sqlStatement.addWhereClause(whereBuilder.toString());
			}
			// 有派工部門
			if(StringUtils.hasText(formDTO.getQueryDispatchDeptId())){
				sqlStatement.addWhereClause("caseHandle.DISPATCH_DEPT_ID = :queryDispatchDeptId", formDTO.getQueryDispatchDeptId());
			}
			// 有派工人員
			if(StringUtils.hasText(formDTO.getQueryDispatchProcessUser())){
				sqlStatement.addWhereClause("caseHandle.DISPATCH_PROCESS_USER = :queryDispatchProcessUser", formDTO.getQueryDispatchProcessUser());
			}
			// 查詢線上排除
			if(formDTO.getQueryOnlineExclusion()){
				sqlStatement.addWhereClause(" isnull(caseHandle.HAS_ONLINE_EXCLUSION, '') = :onlineExclusionParam ");
			}
			// 查詢退回记录--Task #3110 2017/01/18
			if(formDTO.getQueryForBack()){
				sqlStatement.addWhereClause(" isnull(caseHandle.HAS_RETREAT, '') = :forBackParam ");
			}
			// Bug #2582 CyberAgent、廠商Agent、TMS、QA 不應該看到未派工的案件
			if(formDTO.getIgnoreWaitDispatch()){
				sqlStatement.addWhereClause(" caseHandle.CASE_STATUS <> :waitDispatchCase", IAtomsConstants.CASE_STATUS.WAIT_DISPATCH.getCode());
			}
			//區域
			if (StringUtils.hasText(formDTO.getQueryLocation())) {
				whereBuilder.delete(0, whereBuilder.length());
				//Bug #3187
				whereBuilder.append("((case when caseHandle.CASE_CATEGORY='INSTALL' or caseHandle.CASE_CATEGORY='UPDATE' then installedLocation.ITEM_VALUE else (case ");
				whereBuilder.append("when(isnull(caseHandle.CONTACT_IS_BUSSINESS_ADDRESS,'')= 'Y') then businessLocation.ITEM_VALUE ");
				whereBuilder.append("when caseHandle.CONTACT_IS_BUSSINESS_ADDRESS = 'E' then ");
				whereBuilder.append("case when(isnull(caseHandle.IS_BUSSINESS_ADDRESS,'')= 'Y') ");
				whereBuilder.append("then businessLocation.ITEM_VALUE else installedLocation.ITEM_VALUE ");
				whereBuilder.append("end else contactLocation.ITEM_VALUE end ");
			//	whereBuilder.append(") = :location )");
				// Task #3082 區域縣市複選
				whereBuilder.append(") end) in (:location) )");
				sqlStatement.addWhereClause(whereBuilder.toString());
			}
			// Task #3205 是否執行過延期
			if(formDTO.getQueryDelayRecord()){
				sqlStatement.addWhereClause(" isnull(caseHandle.HAS_DELAY, '') = 'Y'");
			}
			if (!formDTO.getIsExport() && !IAtomsConstants.PARAM_YES.equals(formDTO.getQueryTsbFlag())) {
				//Task #3560 1. 可查詢到CMS案件且為有到場的狀況(看到場註記)
				if (IAtomsConstants.PARAM_YES.equals(formDTO.getMobileQueryFlag())) {
					//Task #3452 微型商戶
					if(formDTO.getIsMicro()){
						sqlStatement.addWhereClause(" caseHandle.CMS_CASE = 'Y'");
						//Task #3549
						sqlStatement.addWhereClause(" caseHandle.HAS_ARRIVE = 'Y'");
					} else {
						sqlStatement.addWhereClause(" caseHandle.CMS_CASE = 'N'");
					}
				} else {
					//Task #3452 微型商戶
					if(formDTO.getIsMicro()){
						sqlStatement.addWhereClause(" caseHandle.CMS_CASE = 'Y'");
						//Task #3549
						if(formDTO.getIsMicroArrive()){
							sqlStatement.addWhereClause(" caseHandle.HAS_ARRIVE = 'Y'");
						} else {
							sqlStatement.addWhereClause(" caseHandle.HAS_ARRIVE = 'N'");
						}
					} else {
						sqlStatement.addWhereClause(" caseHandle.CMS_CASE = 'N'");
					}
				}
			}
			// SqlQueryBean對象
			SqlQueryBean sqlQueryBean =sqlStatement.createSqlQueryBean();
			// 佔位配置
			sqlQueryBean.setParameter("closedCaseType", IAtomsConstants.CASE_STATUS.CLOSED.getCode());
			sqlQueryBean.setParameter("caseGroupParam", IATOMS_PARAM_TYPE.CASE_GROUP.getCode());
			
			// 案件狀態
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS_NAME.getValue())){
				sqlQueryBean.setParameter("caseStatusParam", IATOMS_PARAM_TYPE.CASE_STATUS.getCode());
			}
			// 案件類型
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE_NAME.getValue())){
				sqlQueryBean.setParameter("ticketModeParam", IATOMS_PARAM_TYPE.TICKET_MODE.getCode());
			}
			// 回應、到場、完修、 查詢條件：選擇SLA警示件
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_CONDITION.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_CONDITION.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_CONDITION.getValue())
					|| (StringUtils.hasText(formDTO.getQueryWarningSla())) && IAtomsConstants.YES.equals(formDTO.getQueryWarningSla())){
				sqlQueryBean.setParameter("projectCaseCategory", IAtomsConstants.CASE_CATEGORY.PROJECT.getCode());
				sqlQueryBean.setParameter("checkCaseCategory", IAtomsConstants.CASE_CATEGORY.CHECK.getCode());
				// Task #2615 當案件進入 協調完成、線上排除、完修 時 查詢結果之 回應、到場、完修 無需顯示 警示或逾期 符號
				sqlQueryBean.setParameter("ignoreCaseStatus", StringUtils.toList(ignoreCaseStatus.toString(), IAtomsConstants.MARK_SEPARATOR));
			}
			// 裝機類型
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_TYPE_NAME.getValue())){
				sqlQueryBean.setParameter("installTypeParam", IATOMS_PARAM_TYPE.INSTALL_TYPE.getCode());
			}
			// 拆機類型
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALL_TYPE_NAME.getValue())){
				sqlQueryBean.setParameter("uninstallTypeParam", IATOMS_PARAM_TYPE.UNINSTALL_TYPE.getCode());
			}
			// 案件類別
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY_NAME.getValue())){
				sqlQueryBean.setParameter("ticketTypeParam", IATOMS_PARAM_TYPE.TICKET_TYPE.getCode());
			}
			// 設備合約編號、設備序號、設備啟用日、倉別、設備開啟的功能、財產編號
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE_CONTRACT.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_SERIAL_NUMBER.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.ENABLE_DATE.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.WAREHOUSE_NAME.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.OPEN_FUNCTION_NAME.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PROPERTY_ID.getValue())){
				sqlQueryBean.setParameter("caseLinkEdcType", IAtomsConstants.PARAM_CASE_LINK_EDC_TYPE);
			}
			// 特店區域
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.AREA_NAME.getValue())){
				sqlQueryBean.setParameter("regionParam", IATOMS_PARAM_TYPE.REGION.getCode());
			}
			// 特店營業地址、特店裝機地址、特店聯系地址
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.BUSINESS_ADDRESS.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS.getValue())
					|| StringUtils.hasText(formDTO.getQueryLocation())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_AREA.getValue())){
				sqlQueryBean.setParameter("locationParam", IATOMS_PARAM_TYPE.LOCATION.getCode());
			}
			// 雙模組模式
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.MULTI_MODULE_NAME.getValue())){
				sqlQueryBean.setParameter("doubleModuleParam", IATOMS_PARAM_TYPE.DOUBLE_MODULE.getCode());
			}
			// 網路線
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.NETWORK_LINE_NUMBER.getValue())){
				sqlQueryBean.setParameter("caseLinkSupplies", IAtomsConstants.PARAM_CASE_LINK_SUPPLIES);
				sqlQueryBean.setParameter("networkLineParam", IAtomsConstants.PARAM_NET_WORK_LINE);
			}
			// 設備開啟的功能、週邊設備1功能、週邊設備2功能、週邊設備3功能
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.OPEN_FUNCTION_NAME.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION_NAME.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION2_NAME.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION3_NAME.getValue())){
				sqlQueryBean.setParameter("supportedFunctionParam", IATOMS_PARAM_TYPE.SUPPORTED_FUNCTION.getCode());
			}
			// 週邊設備1功能、週邊設備1序號
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION_NAME.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_SERIAL_NUMBER.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_CONTRACT.getValue())){
				sqlQueryBean.setParameter("caseLinkPeripherals", IAtomsConstants.PARAM_CASE_LINK_PERIPHERALS);
			}
			// 週邊設備2功能
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION2_NAME.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_SERIAL_NUMBER.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_CONTRACT.getValue())){
				sqlQueryBean.setParameter("caseLinkPeripherals2", IAtomsConstants.PARAM_CASE_LINK_PERIPHERALS2);
			}
			// 週邊設備3功能
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION3_NAME.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_SERIAL_NUMBER.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_CONTRACT.getValue())){
				sqlQueryBean.setParameter("caseLinkPeripherals3", IAtomsConstants.PARAM_CASE_LINK_PERIPHERALS3);
			}
			// 連線方式
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CONNECTION_TYPE_NAME.getValue())){
				sqlQueryBean.setParameter("commModeParam", IATOMS_PARAM_TYPE.COMM_MODE.getCode());
			}
			// 寬頻連線
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.NET_VENDOR_NAME.getValue())){
				sqlQueryBean.setParameter("netVendorParam", IATOMS_PARAM_TYPE.NET_VENDOR.getCode());
			}
			// CUP交易
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_TRANS_TYPE.getValue())){
				sqlQueryBean.setParameter("cupParam", IAtomsConstants.TRANSACTION_CATEGORY.CUP.getCode());
			}
			// DCC交易
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.DCC_TRANS_TYPE.getValue())){
				sqlQueryBean.setParameter("dccParam", IAtomsConstants.TRANSACTION_CATEGORY.DCC.getCode());
			}
			// AE-MID、AE-TID
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.AE_MID.getValue())
					|| queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.AE_TID.getValue())){
				sqlQueryBean.setParameter("aeParam", IAtomsConstants.TRANSACTION_CATEGORY.AE.getCode());
			}
			// 報修原因
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.REPAIR_REASON_NAME.getValue())){
				sqlQueryBean.setParameter("repairReasonParam", IATOMS_PARAM_TYPE.REPAIR_REASON.getCode());
			}
			// 責任歸屬
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSIBITY_NAME.getValue())){
				sqlQueryBean.setParameter("responAttributionParam", IATOMS_PARAM_TYPE.RESPON_ATTRIBUTION.getCode());
			}
			// 報修問題原因分類
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_REASON_CATEGORY_NAME.getValue())){
				sqlQueryBean.setParameter("problemReasonCategoryParam", IATOMS_PARAM_TYPE.PROBLEM_REASON_CATEGORY.getCode());
			}
			// 報修解決方式分類
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_SOLUTION_CATEGORY_NAME.getValue())){
				sqlQueryBean.setParameter("problemSolutionCategoryParam", IATOMS_PARAM_TYPE.PROBLEM_SOLUTION_CATEGORY.getCode());
			}
			// 處理方式
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.PROCESS_TYPE_NAME.getValue())){
				sqlQueryBean.setParameter("processTypeParam", IATOMS_PARAM_TYPE.PROCESS_TYPE.getCode());
			}
			// ECR連線名稱
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.ECR_CONNECTION_NAME.getValue())){
				sqlQueryBean.setParameter("ecrLineParam", IATOMS_PARAM_TYPE.ECR_LINE.getCode());
			}
			
			
			// 查詢條件：案件編號、列印
			if(StringUtils.hasText(formDTO.getQueryCaseId()) && formDTO.getIsExport()){
				sqlQueryBean.setParameter("caseId", StringUtils.toList(formDTO.getQueryCaseId(), IAtomsConstants.MARK_SEPARATOR));
			}
			// 給查詢條件TID放置參數
			if(StringUtils.hasText(formDTO.getQueryTid())){
				sqlQueryBean.setParameter("queryTid", formDTO.getQueryTid() + IAtomsConstants.MARK_PERCENT);
			}
			// 查詢條件：案件類別
			if(StringUtils.hasText(formDTO.getQueryCaseCategory())){
				sqlQueryBean.setParameter("queryCaseCategory", StringUtils.toList(formDTO.getQueryCaseCategory(), IAtomsConstants.MARK_SEPARATOR));
			}
			// 查詢條件：刷卡機型
			if(StringUtils.hasText(formDTO.getQueryEdcType())){
				sqlQueryBean.setParameter("queryEdcType", StringUtils.toList(formDTO.getQueryEdcType(), IAtomsConstants.MARK_SEPARATOR));
			}
			// 查詢條件：維護廠商
			if(StringUtils.hasText(formDTO.getQueryVendorId())){
				sqlQueryBean.setParameter("queryVendorId", StringUtils.toList(formDTO.getQueryVendorId(), IAtomsConstants.MARK_SEPARATOR));
			}
			// 查詢條件：維護部門
			if(StringUtils.hasText(formDTO.getQueryVendorDept())){
				sqlQueryBean.setParameter("queryVendorDept", StringUtils.toList(formDTO.getQueryVendorDept(), IAtomsConstants.MARK_SEPARATOR));
			}
			// 查詢條件：案件狀態
			if(StringUtils.hasText(formDTO.getQueryCaseStatus())){
				sqlQueryBean.setParameter("queryCaseStatus", StringUtils.toList(formDTO.getQueryCaseStatus(), IAtomsConstants.MARK_SEPARATOR));
			}
			// 查詢條件：刷卡機暨週邊設備支援功能
			if(StringUtils.hasText(formDTO.getQuerySupportedFun())){
				sqlQueryBean.setParameter("queryFunctionId", StringUtils.toList(formDTO.getQuerySupportedFun(), IAtomsConstants.MARK_SEPARATOR));
			}
			// 有派工維護廠商
			if(!CollectionUtils.isEmpty(formDTO.getVendorIdList())){
				sqlQueryBean.setParameter("vendorIdList", formDTO.getVendorIdList());
				if(!CollectionUtils.isEmpty(formDTO.getDeptCodeList())){
					sqlQueryBean.setParameter("deptCodeLists", formDTO.getDeptCodeList());
				}
			}
			//區域
			if (StringUtils.hasText(formDTO.getQueryLocation())) {
			//	sqlQueryBean.setParameter("location", formDTO.getQueryLocation());
				// Task #3082 區域縣市複選
				sqlQueryBean.setParameter("location", StringUtils.toList(formDTO.getQueryLocation(), IAtomsConstants.MARK_SEPARATOR));
			}
			//物流廠商
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.LOGISTICS_VENDOR.getValue())){
				sqlQueryBean.setParameter("logisticsVendor", IATOMS_PARAM_TYPE.LOGISTICS_VENDOR.getCode());
			}
			// Receipt_type
			if(queryColumnMap.containsKey(SrmCaseHandleInfoDTO.ATTRIBUTE.RECEIPT_TYPE.getValue())){
				sqlQueryBean.setParameter("receiptType", IATOMS_PARAM_TYPE.RECEIPT_TYPE.getCode());
			}
			/*// 查詢條件：SLA警示件查詢
			if(StringUtils.hasText(formDTO.getQueryWarningSla())){
				// 選擇SLA警示件
				if(IAtomsConstants.YES.equals(formDTO.getQueryWarningSla())){
					// Task #2615 當案件進入 協調完成、線上排除、完修 時 查詢結果之 回應、到場、完修 無需顯示 警示或逾期 符號
					sqlQueryBean.setParameter("ignoreCaseStatus", StringUtils.toList(ignoreCaseStatus.toString(), IAtomsConstants.MARK_SEPARATOR));
					sqlQueryBean.setParameter("projectCaseCategory", IAtomsConstants.CASE_CATEGORY.PROJECT.getCode());
					sqlQueryBean.setParameter("checkCaseCategory", IAtomsConstants.CASE_CATEGORY.CHECK.getCode());
				}
			}*/
			// 查詢線上排除
			if(formDTO.getQueryOnlineExclusion()){
				sqlQueryBean.setParameter("onlineExclusionParam", IAtomsConstants.PARAM_YES);
			}
			// 查詢退回记录--Task #3110 2017/01/18
			if(formDTO.getQueryForBack()){
				sqlQueryBean.setParameter("forBackParam", IAtomsConstants.PARAM_YES);
			}
			// 分頁
			if((formDTO.getRows() != null) && (formDTO.getPage() != null)){
				sqlQueryBean.setPageSize(formDTO.getRows());
				sqlQueryBean.setStartPage(formDTO.getPage() - 1);
			}
			// 排序
			if(StringUtils.hasText(formDTO.getOrder()) && StringUtils.hasText(formDTO.getSort())){
				sqlQueryBean.append(" order by ").append(formDTO.getSort()).append(IAtomsConstants.MARK_SPACE).append(formDTO.getOrder());
			} else {
				sqlQueryBean.append(" order by caseHandle.CREATED_DATE asc");
			}
			
			
			LOGGER.debug("queryListBy()", "sql:" + sqlQueryBean.toString());
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseHandleInfoDTO.class);
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("queryListBy()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		long endQueryListTime = System.currentTimeMillis();
		LOGGER.debug("calculate time --> queryCase", "DAO queryListTime:" + (endQueryListTime - startQueryListTime));
		return result;
	}
	
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#count(com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO)
	 */
	@Override
	public int count(CaseManagerFormDTO formDTO) throws DataAccessException {
		try {
			long startQueryCountTime = System.currentTimeMillis();
			// 得到schema
			String schema = this.getMySchema();
			// 記錄查詢條件
			LOGGER.debug("listBy()", "parameters : queryCompanyId =" + formDTO.getQueryCompanyId());
			LOGGER.debug("listBy()", "parameters : queryRequirementNo =" + formDTO.getQueryRequirementNo());
			LOGGER.debug("listBy()", "parameters : queryCaseId =" + formDTO.getQueryCaseId());
			LOGGER.debug("listBy()", "parameters : queryCaseCategory =" + formDTO.getQueryCaseCategory());
			LOGGER.debug("listBy()", "parameters : queryEdcType =" + formDTO.getQueryEdcType());
			LOGGER.debug("listBy()", "parameters : queryVendorId =" + formDTO.getQueryVendorId());
			LOGGER.debug("listBy()", "parameters : queryVendorDept =" + formDTO.getQueryVendorDept());
			LOGGER.debug("listBy()", "parameters : queryCreateDateStart =" + formDTO.getQueryCreateDateStart());
			LOGGER.debug("listBy()", "parameters : queryCreateDateEnd =" + formDTO.getQueryCreateDateEnd());
			LOGGER.debug("listBy()", "parameters : queryAcceptableCompleteDateStart =" + formDTO.getQueryAcceptableCompleteDateStart());
			LOGGER.debug("listBy()", "parameters : queryAcceptableCompleteDateEnd =" + formDTO.getQueryAcceptableCompleteDateEnd());
			LOGGER.debug("listBy()", "parameters : queryCompleteDateStart =" + formDTO.getQueryCompleteDateStart());
			LOGGER.debug("listBy()", "parameters : queryCompleteDateEnd =" + formDTO.getQueryCompleteDateEnd());
			LOGGER.debug("listBy()", "parameters : queryCaseStatus =" + formDTO.getQueryCaseStatus());
			LOGGER.debug("listBy()", "parameters : queryMerchatCode =" + formDTO.getQueryMerchatCode());
			LOGGER.debug("listBy()", "parameters : queryMerchatName =" + formDTO.getQueryMerchatName());
			LOGGER.debug("listBy()", "parameters : queryIsProject =" + formDTO.getQueryIsProject());
			LOGGER.debug("listBy()", "parameters : queryProjectCode =" + formDTO.getQueryProjectCode());
			LOGGER.debug("listBy()", "parameters : queryProjectName =" + formDTO.getQueryProjectName());
			LOGGER.debug("listBy()", "parameters : queryOpenMode =" + formDTO.getQueryOpenMode());
			LOGGER.debug("listBy()", "parameters : queryDtid =" + formDTO.getQueryDtid());
			LOGGER.debug("listBy()", "parameters : queryTid =" + formDTO.getQueryTid());
			LOGGER.debug("listBy()", "parameters : querySupportedFun =" + formDTO.getQuerySupportedFun());
			LOGGER.debug("listBy()", "parameters : queryConditionOperator =" + formDTO.getQueryConditionOperator());
			LOGGER.debug("listBy()", "parameters : queryWarningSla =" + formDTO.getQueryWarningSla());
			LOGGER.debug("listBy()", "parameters : queryAoName =" + formDTO.getQueryAoName());
			LOGGER.debug("listBy()", "parameters : isInstant =" + formDTO.getIsInstant());
			LOGGER.debug("listBy()", "parameters : queryDispatchDeptId =" + formDTO.getQueryDispatchDeptId());
			LOGGER.debug("listBy()", "parameters : queryDispatchProcessUser =" + formDTO.getQueryDispatchProcessUser());
			
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			StringBuffer buffer = new StringBuffer();
			// 案件交易參數
			String caseTransactionParameter = null;
			// 案件設備支援功能
			String caseAssetFunction = null;
			// 案件歷程檔
			String caseTransaction = null;
			if(formDTO.getIsInstant()){
				buffer.append(schema).append(".SRM_CASE_HANDLE_INFO caseHandle ");
				caseTransactionParameter = ".SRM_CASE_TRANSACTION_PARAMETER";
				caseAssetFunction = ".SRM_CASE_ASSET_FUNCTION";
				caseTransaction = ".SRM_CASE_TRANSACTION";
			} else {
				buffer.append(schema).append(".SRM_HISTORY_CASE_HANDLE_INFO caseHandle ");
				caseTransactionParameter = ".SRM_HISTORY_CASE_TRANSACTION_PARAMETER";
				caseAssetFunction = ".SRM_HISTORY_CASE_ASSET_FUNCTION";
				caseTransaction = ".SRM_HISTORY_CASE_TRANSACTION";
			}
			buffer.append(" left join ").append(schema).append(".BIM_MERCHANT merchant on merchant.MERCHANT_ID = caseHandle.MERCHANT_CODE ");
			buffer.append(" left join ").append(schema).append(".BIM_MERCHANT_HEADER merHeader on merHeader.MERCHANT_HEADER_ID = caseHandle.MERCHANT_HEADER_ID ");
			if (StringUtils.hasText(formDTO.getQueryLocation())) {
				// 裝機地址查詢
				buffer.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF installedLocation ON installedLocation.BPTD_CODE = :locationParam ");
				buffer.append(" and installedLocation.ITEM_VALUE = caseHandle.INSTALLED_ADRESS_LOCATION ");
				// 聯繫地址查詢
				buffer.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF contactLocation ON contactLocation.BPTD_CODE = :locationParam ");
				buffer.append(" and contactLocation.ITEM_VALUE = caseHandle.CONTACT_ADDRESS_LOCATION ");
					
				buffer.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF businessLocation ON businessLocation.BPTD_CODE = :locationParam and businessLocation.ITEM_VALUE = merHeader.LOCATION ");
			}
			
			
			sqlStatement.addFromExpression(buffer.toString());
			// 查詢條件：進件日期 起
			if(formDTO.getQueryCreateDateStart() != null){
				sqlStatement.addWhereClause("caseHandle.CREATED_DATE >= :queryCreateDateStart", formDTO.getQueryCreateDateStart());
			}
			// 查詢條件：進件日期 迄
			if(formDTO.getQueryCreateDateEnd() != null){
				sqlStatement.addWhereClause("caseHandle.CREATED_DATE < :queryCreateDateEnd", formDTO.getQueryCreateDateEnd());
			}
			// 查詢條件：客戶
			if(StringUtils.hasText(formDTO.getQueryCompanyId())){
				sqlStatement.addWhereClause("caseHandle.CUSTOMER_ID =:customerId", formDTO.getQueryCompanyId());
			}
			// 查詢條件：需求單號
			if(StringUtils.hasText(formDTO.getQueryRequirementNo())){
				sqlStatement.addWhereClause("caseHandle.REQUIREMENT_NO like :requirementNo", formDTO.getQueryRequirementNo() + IAtomsConstants.MARK_PERCENT);
			}
			// 查詢條件：案件編號
			if(StringUtils.hasText(formDTO.getQueryCaseId())){
				sqlStatement.addWhereClause("caseHandle.CASE_ID like :caseId", formDTO.getQueryCaseId() + IAtomsConstants.MARK_PERCENT);
			}
			// 查詢條件：案件類別
			if(StringUtils.hasText(formDTO.getQueryCaseCategory())){
				sqlStatement.addWhereClause("caseHandle.CASE_CATEGORY in( :queryCaseCategory )");
			}
			// 查詢條件：刷卡機型
			if(StringUtils.hasText(formDTO.getQueryEdcType())){
				sqlStatement.addWhereClause("caseHandle.EDC_TYPE in( :queryEdcType )");
			}
			// 查詢條件：維護廠商
			if(StringUtils.hasText(formDTO.getQueryVendorId())){
				sqlStatement.addWhereClause("caseHandle.COMPANY_ID in( :queryVendorId )");
			}
			// 查詢條件：維護部門
			if(StringUtils.hasText(formDTO.getQueryVendorDept())){
			//	sqlStatement.addWhereClause("caseHandle.DEPARTMENT_ID in( :queryVendorDept )");
				/*buffer.delete(0, buffer.length());
				buffer.append("((caseHandle.DISPATCH_DATE is null and ");
				buffer.append(" caseHandle.DEPARTMENT_ID in( :queryVendorDept ))");
				buffer.append(" or (caseHandle.DISPATCH_DATE is not null and ");
				buffer.append(" caseHandle.DISPATCH_DEPT_ID in( :queryVendorDept )))");*/
				sqlStatement.addWhereClause("caseHandle.DISPATCH_DEPT_ID in( :queryVendorDept )");
			//	sqlStatement.addWhereClause(buffer.toString());
			}
			// 查詢條件：應完成日期 起
			if(formDTO.getQueryAcceptableCompleteDateStart() != null){
				sqlStatement.addWhereClause("caseHandle.ACCEPTABLE_FINISH_DATE >= :queryAcceptableCompleteDateStart", formDTO.getQueryAcceptableCompleteDateStart());
			}
			// 查詢條件：應完成日期 迄
			if(formDTO.getQueryAcceptableCompleteDateEnd() != null){
				sqlStatement.addWhereClause("caseHandle.ACCEPTABLE_FINISH_DATE < :queryAcceptableCompleteDateEnd", formDTO.getQueryAcceptableCompleteDateEnd());
			}
			// 查詢條件：完修日期 起
			if(formDTO.getQueryCompleteDateStart() != null){
				sqlStatement.addWhereClause("caseHandle.COMPLETE_DATE >= :queryCompleteStartDate", formDTO.getQueryCompleteDateStart());
			}
			// 查詢條件：完修日期 迄
			if(formDTO.getQueryCompleteDateEnd() != null){
				sqlStatement.addWhereClause("caseHandle.COMPLETE_DATE < :queryCompleteEndDate", formDTO.getQueryCompleteDateEnd());
			}
			// 查詢條件：案件狀態
			if(StringUtils.hasText(formDTO.getQueryCaseStatus())){
				sqlStatement.addWhereClause("caseHandle.CASE_STATUS in( :queryCaseStatus )");
			}
			// 查詢條件：特店代號
			if(StringUtils.hasText(formDTO.getQueryMerchatCode())){
				sqlStatement.addWhereClause("merchant.MERCHANT_CODE like :merchantCode", formDTO.getQueryMerchatCode() + IAtomsConstants.MARK_PERCENT);
			}
			// 查詢條件：特店名稱
			if(StringUtils.hasText(formDTO.getQueryMerchatName())){
				sqlStatement.addWhereClause("merchant.NAME like :merchantName", formDTO.getQueryMerchatName() + IAtomsConstants.MARK_PERCENT);
			}
			// 查詢條件：表頭（同對外名稱）
			if(StringUtils.hasText(formDTO.getQueryMerHeader())){
				sqlStatement.addWhereClause("merHeader.HEADER_NAME like :merHeaderName", formDTO.getQueryMerHeader() + IAtomsConstants.MARK_PERCENT);
			}
			// 查詢條件：是否專案
			if(StringUtils.hasText(formDTO.getQueryIsProject())){
				if(IAtomsConstants.YES.equals(formDTO.getQueryIsProject())){
					sqlStatement.addWhereClause("caseHandle.IS_PROJECT = :queryIsProject", formDTO.getQueryIsProject());
				} else if(IAtomsConstants.NO.equals(formDTO.getQueryIsProject())){
					sqlStatement.addWhereClause("caseHandle.IS_PROJECT = :queryIsProject", formDTO.getQueryIsProject());
				} else if(formDTO.getQueryIsProject().contains(IAtomsConstants.YES) && formDTO.getQueryIsProject().contains(IAtomsConstants.NO)){
					sqlStatement.addWhereClause("caseHandle.IS_PROJECT is not null and caseHandle.IS_PROJECT <> '' ");
				}
			}
			// 查詢條件：專案代碼
			if(StringUtils.hasText(formDTO.getQueryProjectCode())){
				sqlStatement.addWhereClause("caseHandle.PROJECT_CODE like :queryProjectCode", formDTO.getQueryProjectCode() + IAtomsConstants.MARK_PERCENT);
			}
			// 查詢條件：專案名稱
			if(StringUtils.hasText(formDTO.getQueryProjectName())){
				sqlStatement.addWhereClause("caseHandle.PROJECT_NAME like :queryProjectName", formDTO.getQueryProjectName() + IAtomsConstants.MARK_PERCENT);
			}
			// 設備開啟模式
			if(StringUtils.hasText(formDTO.getQueryOpenMode())){
				// 選擇內建
				if(formDTO.getQueryOpenMode().contains(IAtomsConstants.PARAM_BUILT_IN)){
					sqlStatement.addWhereClause(" (caseHandle.BUILT_IN_FEATURE is not null and caseHandle.BUILT_IN_FEATURE <>'') ");
				}
				// 選擇外接
				if(formDTO.getQueryOpenMode().contains(IAtomsConstants.PARAM_EXTERNAL)){
					buffer.delete(0, buffer.length());
					buffer.append(" ((caseHandle.PERIPHERALS_FUNCTION is not null and caseHandle.PERIPHERALS_FUNCTION <> '')");
					buffer.append(" or (caseHandle.PERIPHERALS_FUNCTION2 is not null and caseHandle.PERIPHERALS_FUNCTION2 <> '')");
					buffer.append(" or (caseHandle.PERIPHERALS_FUNCTION3 is not null and caseHandle.PERIPHERALS_FUNCTION3 <> ''))");
					sqlStatement.addWhereClause(buffer.toString());
				//	sqlStatement.addWhereClause(" (caseHandle.PERIPHERALS_FUNCTION is not null or caseHandle.PERIPHERALS_FUNCTION2 is not null or caseHandle.PERIPHERALS_FUNCTION3 is not null)");
				}
			}
			// 查詢條件：DTID
			if(StringUtils.hasText(formDTO.getQueryDtid())){
				sqlStatement.addWhereClause("caseHandle.DTID like :dtid", formDTO.getQueryDtid() + IAtomsConstants.MARK_PERCENT);
			}
			// 查詢條件：TID
			if(StringUtils.hasText(formDTO.getQueryTid())){
				buffer.delete(0, buffer.length());
				buffer.append(" exists(select 1 from ").append(schema).append(caseTransactionParameter);
				buffer.append(" transParameter where transParameter.CASE_ID = caseHandle.CASE_ID and transParameter.tid like :queryTid) ");
				sqlStatement.addWhereClause(buffer.toString());
			//	sqlStatement.addWhereClause(" exists(select 1 from dbo.SRM_CASE_TRANSACTION_PARAMETER transParameter where transParameter.CASE_ID = caseHandle.CASE_ID and transParameter.tid like :queryTid) ");
			}
			// 查詢條件：刷卡機暨週邊設備支援功能
			if(StringUtils.hasText(formDTO.getQuerySupportedFun())){
				buffer.delete(0, buffer.length());
				buffer.append(" exists(select 1 from ").append(schema).append(caseAssetFunction);
				buffer.append(" assetFun where assetFun.CASE_ID = caseHandle.CASE_ID and assetFun.FUNCTION_ID in ( :queryFunctionId )) ");
				sqlStatement.addWhereClause(buffer.toString());
			//	sqlStatement.addWhereClause(" exists(select 1 from dbo.SRM_CASE_ASSET_FUNCTION assetFun where assetFun.CASE_ID = caseHandle.CASE_ID and assetFun.FUNCTION_ID in ( :queryFunctionId ))");
			}
			// 查詢條件：大於或小於的限制條件
			if(StringUtils.hasText(formDTO.getQueryConditionOperator()) && formDTO.getQueryRepairTimes() != null){
				// 大於等於
				if(IAtomsConstants.MARK_EQUALANDPRE.equals(formDTO.getQueryConditionOperator())){
					sqlStatement.addWhereClause("caseHandle.REPAIR_TIMES >= :queryRepairTimes", formDTO.getQueryRepairTimes());
					// 等於
				} else if(IAtomsConstants.MARK_EQUALS.equals(formDTO.getQueryConditionOperator())){
					sqlStatement.addWhereClause("caseHandle.REPAIR_TIMES = :queryRepairTimes", formDTO.getQueryRepairTimes());
					// 小於等於
				} else if(IAtomsConstants.MARK_EQUALANDAFTER.equals(formDTO.getQueryConditionOperator())){
					sqlStatement.addWhereClause("caseHandle.REPAIR_TIMES <= :queryRepairTimes", formDTO.getQueryRepairTimes());
				}
			}
			// 查詢條件：SLA警示件查詢
			if(StringUtils.hasText(formDTO.getQueryWarningSla())){
				// 選擇SLA警示件
				if(IAtomsConstants.YES.equals(formDTO.getQueryWarningSla())){
					buffer.delete(0, buffer.length());
					// Task #2489 專案與查核案件，無須計算SLA
					buffer.append("(caseHandle.CASE_CATEGORY <>:projectCaseCategory and ");
					buffer.append(" caseHandle.CASE_CATEGORY <>:checkCaseCategory and ");
					// Task #2615 當案件進入 協調完成、線上排除、完修 時 查詢結果之 回應、到場、完修 無需顯示 警示或逾期 符號
//					buffer.append("(caseHandle.CASE_STATUS <> :voidedType and ( ");
					buffer.append("(caseHandle.CASE_STATUS not in ( :ignoreCaseStatus ) and ( ");
					
					buffer.append("(caseHandle.ACCEPTABLE_RESPONSE_DATE is not null ");
					buffer.append(" and isnull(caseHandle.RESPONSE_DATE,isnull(caseHandle.COMPLETE_DATE,getdate())) >= caseHandle.ACCEPTABLE_RESPONSE_DATE) ");
					buffer.append(" or (caseHandle.ACCEPTABLE_RESPONSE_DATE is not null and caseHandle.RESPONSE_WARNNING is not null ");
					buffer.append(" and isnull(caseHandle.RESPONSE_DATE,isnull(caseHandle.COMPLETE_DATE,getdate())) >= dateadd(hour,-caseHandle.RESPONSE_WARNNING, caseHandle.ACCEPTABLE_RESPONSE_DATE))");
					buffer.append(" or (isnull(caseHandle.ARRIVE_DATE,isnull(caseHandle.COMPLETE_DATE,getdate())) >= dateadd(hour,-caseHandle.ARRIVE_WARNNING, caseHandle.ACCEPTABLE_ARRIVE_DATE)) ");
					buffer.append(" or (isnull(caseHandle.COMPLETE_DATE,getdate()) >= dateadd(hour,-caseHandle.COMPLETE_WARNNING, caseHandle.ACCEPTABLE_FINISH_DATE))");
				//	buffer.append(" ))");
					buffer.append(" )))");
					sqlStatement.addWhereClause(buffer.toString());
				}
			}
			// 查詢條件：AO人員
			if(StringUtils.hasText(formDTO.getQueryAoName())){
				sqlStatement.addWhereClause("merHeader.AO_NAME like :aoName", formDTO.getQueryAoName() + IAtomsConstants.MARK_PERCENT);
			}
			// 有派工維護廠商
			if(!CollectionUtils.isEmpty(formDTO.getVendorIdList())){
			//	sqlStatement.addWhereClause("caseHandle.COMPANY_ID = :queryDispatchVendorId", formDTO.getQueryDispatchVendorId());
				buffer.delete(0, buffer.length());
				if(CollectionUtils.isEmpty(formDTO.getDeptCodeList())){
					buffer.append("(caseHandle.COMPANY_ID in (:vendorIdList)  AND caseHandle.DISPATCH_DATE is not null");
					buffer.append(" and (caseHandle.DISPATCH_DEPT_ID is null or caseHandle.DISPATCH_DEPT_ID=''))");
				} else {
					buffer.append("(caseHandle.COMPANY_ID in (:vendorIdList) AND caseHandle.DISPATCH_DATE is not null");
					buffer.append(" and (caseHandle.DISPATCH_DEPT_ID is null or caseHandle.DISPATCH_DEPT_ID = '' or caseHandle.DISPATCH_DEPT_ID in (:deptCodeLists) ))");
				}
				sqlStatement.addWhereClause(buffer.toString());
			}
			// 有派工部門
			if(StringUtils.hasText(formDTO.getQueryDispatchDeptId())){
				sqlStatement.addWhereClause("caseHandle.DISPATCH_DEPT_ID = :queryDispatchDeptId", formDTO.getQueryDispatchDeptId());
			}
			// 有派工人員
			if(StringUtils.hasText(formDTO.getQueryDispatchProcessUser())){
				sqlStatement.addWhereClause("caseHandle.DISPATCH_PROCESS_USER = :queryDispatchProcessUser", formDTO.getQueryDispatchProcessUser());
			}
			// 查詢線上排除
			if(formDTO.getQueryOnlineExclusion()){
				buffer.delete(0, buffer.length());
/*				buffer.append("EXISTS(select trans.CASE_ID from ").append(schema).append(caseTransaction);
				buffer.append(" trans where trans.CASE_ID = caseHandle.CASE_ID and trans.ACTION_ID = :onlineExclusionParam)");*/
				
				// Task #2606 案件線上排除修改
				buffer.append(" isnull(caseHandle.HAS_ONLINE_EXCLUSION, '') = :onlineExclusionParam ");
				sqlStatement.addWhereClause(buffer.toString());
			}
			// 查詢退回记录--Task #3110 2017/01/18
			if(formDTO.getQueryForBack()){
				buffer.delete(0, buffer.length());
				buffer.append(" isnull(caseHandle.HAS_RETREAT, '') = :forBackParam ");
				sqlStatement.addWhereClause(buffer.toString());
			}
			// Bug #2582 CyberAgent、廠商Agent、TMS、QA 不應該看到未派工的案件
			if(formDTO.getIgnoreWaitDispatch()){
				sqlStatement.addWhereClause(" caseHandle.CASE_STATUS <> :waitDispatchCase", IAtomsConstants.CASE_STATUS.WAIT_DISPATCH.getCode());
			}
			//Bug #3187
			if (StringUtils.hasText(formDTO.getQueryLocation())) {
				buffer.delete(0, buffer.length());
				buffer.append("((case when caseHandle.CASE_CATEGORY='INSTALL' or caseHandle.CASE_CATEGORY='UPDATE' then installedLocation.ITEM_VALUE else (case ");
				buffer.append("when(isnull(caseHandle.CONTACT_IS_BUSSINESS_ADDRESS,'')= 'Y') then businessLocation.ITEM_VALUE ");
				buffer.append("when caseHandle.CONTACT_IS_BUSSINESS_ADDRESS = 'E' then ");
				buffer.append("case when(isnull(caseHandle.IS_BUSSINESS_ADDRESS,'')= 'Y') ");
				buffer.append("then businessLocation.ITEM_VALUE else installedLocation.ITEM_VALUE ");
				buffer.append("end else contactLocation.ITEM_VALUE end ");
			//	buffer.append(") = :location )");
				// Task #3082 區域縣市複選
				buffer.append(") end) in (:location) )");
				sqlStatement.addWhereClause(buffer.toString());
			}
			// Task #3205 是否執行過延期
			if(formDTO.getQueryDelayRecord()){
				sqlStatement.addWhereClause(" isnull(caseHandle.HAS_DELAY, '') = 'Y'");
			}
			//Task #3560 1. 可查詢到CMS案件且為有到場的狀況(看到場註記)
			if (IAtomsConstants.PARAM_YES.equals(formDTO.getMobileQueryFlag())) {
				//Task #3452 微型商戶
				if(formDTO.getIsMicro()){
					sqlStatement.addWhereClause(" caseHandle.CMS_CASE = 'Y'");
					//Task #3549
					sqlStatement.addWhereClause(" caseHandle.HAS_ARRIVE = 'Y'");
				} else {
					sqlStatement.addWhereClause(" caseHandle.CMS_CASE = 'N'");
				}
			} else {
				//Task #3452 微型商戶
				if(formDTO.getIsMicro()){
					sqlStatement.addWhereClause(" caseHandle.CMS_CASE = 'Y'");
					//Task #3549
					if(formDTO.getIsMicroArrive()){
						sqlStatement.addWhereClause(" caseHandle.HAS_ARRIVE = 'Y'");
					} else {
						sqlStatement.addWhereClause(" caseHandle.HAS_ARRIVE = 'N'");
					}
				} else {
					sqlStatement.addWhereClause(" caseHandle.CMS_CASE = 'N'");
				}
			}
			
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			// 給查詢條件TID放置參數
			if(StringUtils.hasText(formDTO.getQueryTid())){
				sqlQueryBean.setParameter("queryTid", formDTO.getQueryTid() + IAtomsConstants.MARK_PERCENT);
			}
			// 查詢條件：案件類別
			if(StringUtils.hasText(formDTO.getQueryCaseCategory())){
				sqlQueryBean.setParameter("queryCaseCategory", StringUtils.toList(formDTO.getQueryCaseCategory(), IAtomsConstants.MARK_SEPARATOR));
			}
			// 查詢條件：刷卡機型
			if(StringUtils.hasText(formDTO.getQueryEdcType())){
				sqlQueryBean.setParameter("queryEdcType", StringUtils.toList(formDTO.getQueryEdcType(), IAtomsConstants.MARK_SEPARATOR));
			}
			// 查詢條件：維護廠商
			if(StringUtils.hasText(formDTO.getQueryVendorId())){
				sqlQueryBean.setParameter("queryVendorId", StringUtils.toList(formDTO.getQueryVendorId(), IAtomsConstants.MARK_SEPARATOR));
			}
			// 查詢條件：維護部門
			if(StringUtils.hasText(formDTO.getQueryVendorDept())){
				sqlQueryBean.setParameter("queryVendorDept", StringUtils.toList(formDTO.getQueryVendorDept(), IAtomsConstants.MARK_SEPARATOR));
			}
			// 查詢條件：案件狀態
			if(StringUtils.hasText(formDTO.getQueryCaseStatus())){
				sqlQueryBean.setParameter("queryCaseStatus", StringUtils.toList(formDTO.getQueryCaseStatus(), IAtomsConstants.MARK_SEPARATOR));
			}
			// 查詢條件：刷卡機暨週邊設備支援功能
			if(StringUtils.hasText(formDTO.getQuerySupportedFun())){
				sqlQueryBean.setParameter("queryFunctionId", StringUtils.toList(formDTO.getQuerySupportedFun(), IAtomsConstants.MARK_SEPARATOR));
			}
			// 有派工維護廠商
			if(!CollectionUtils.isEmpty(formDTO.getVendorIdList())){
				sqlQueryBean.setParameter("vendorIdList", formDTO.getVendorIdList());
				if(!CollectionUtils.isEmpty(formDTO.getDeptCodeList())){
					sqlQueryBean.setParameter("deptCodeLists", formDTO.getDeptCodeList());
				}
			}
			// 查詢條件：SLA警示件查詢
			if(StringUtils.hasText(formDTO.getQueryWarningSla())){
				// 選擇SLA警示件
				if(IAtomsConstants.YES.equals(formDTO.getQueryWarningSla())){
					// Task #2615 當案件進入 協調完成、線上排除、完修 時 查詢結果之 回應、到場、完修 無需顯示 警示或逾期 符號
					StringBuilder ignoreCaseStatus = new StringBuilder();
					// 結案
					ignoreCaseStatus.append(IAtomsConstants.CASE_STATUS.CLOSED.getCode()).append(IAtomsConstants.MARK_SEPARATOR);
					// 協調完成
					ignoreCaseStatus.append(IAtomsConstants.CASE_STATUS.IMMEDIATE_CLOSE.getCode()).append(IAtomsConstants.MARK_SEPARATOR);
					// 作廢
					ignoreCaseStatus.append(IAtomsConstants.CASE_STATUS.VOIDED.getCode()).append(IAtomsConstants.MARK_SEPARATOR);
					// 完修
					ignoreCaseStatus.append(IAtomsConstants.CASE_STATUS.COMPLETED.getCode()).append(IAtomsConstants.MARK_SEPARATOR);
					// 待結案審查
					ignoreCaseStatus.append(IAtomsConstants.CASE_STATUS.WAIT_CLOSE.getCode());
					
					// Task #2615 當案件進入 協調完成、線上排除、完修 時 查詢結果之 回應、到場、完修 無需顯示 警示或逾期 符號
					sqlQueryBean.setParameter("ignoreCaseStatus", StringUtils.toList(ignoreCaseStatus.toString(), IAtomsConstants.MARK_SEPARATOR));
					
				//	sqlQueryBean.setParameter("voidedType", IAtomsConstants.CASE_STATUS.VOIDED.getCode());
					sqlQueryBean.setParameter("projectCaseCategory", IAtomsConstants.CASE_CATEGORY.PROJECT.getCode());
					sqlQueryBean.setParameter("checkCaseCategory", IAtomsConstants.CASE_CATEGORY.CHECK.getCode());
				}
			}
			// 查詢線上排除
			if(formDTO.getQueryOnlineExclusion()){
//				sqlQueryBean.setParameter("onlineExclusionParam", IAtomsConstants.CASE_ACTION.ONLINE_EXCLUSION.getCode());
				// Task #2606 案件線上排除修改
				sqlQueryBean.setParameter("onlineExclusionParam", IAtomsConstants.PARAM_YES);
			}
			// 查詢退回记录--Task #3110 2017/01/18
			if(formDTO.getQueryForBack()){
				sqlQueryBean.setParameter("forBackParam", IAtomsConstants.PARAM_YES);
			}
			if (StringUtils.hasText(formDTO.getQueryLocation())) {
			//	sqlQueryBean.setParameter("location", formDTO.getQueryLocation());
				// Task #3082 區域縣市複選
				sqlQueryBean.setParameter("location", StringUtils.toList(formDTO.getQueryLocation(), IAtomsConstants.MARK_SEPARATOR));
				sqlQueryBean.setParameter("locationParam", IATOMS_PARAM_TYPE.LOCATION.getCode());
			}
			LOGGER.debug("count()", "sql:" + sqlQueryBean.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				return result.get(0).intValue();
			}
			long endQueryCountTime = System.currentTimeMillis();
			LOGGER.debug("calculate time --> queryCase", "DAO queryCountTime:" + (endQueryCountTime - startQueryCountTime));
			return 0;
		} catch (Exception e) {
			LOGGER.error("count()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE,e);
		}
	}

	public List<SrmCaseHandleInfoDTO> getChangeCaseInfoById(String caseId, String isHistory) throws DataAccessException {
		LOGGER.debug("getChangeCaseInfoById()", "parameters : caseId =" + caseId);
		String schema = this.getMySchema();
		StringBuilder builder = new StringBuilder();
		try{
			SqlStatement  sqlStatement = new SqlStatement();
			
			sqlStatement.addSelectClause("info.CASE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("info.EDC_TYPE", SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE.getValue());
			sqlStatement.addSelectClause("info.PERIPHERALS", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS.getValue());
			sqlStatement.addSelectClause("assetPer.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_NAME.getValue());
			sqlStatement.addSelectClause("info.PERIPHERALS_FUNCTION", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION.getValue());
			sqlStatement.addSelectClause("info.PERIPHERALS2", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2.getValue());
			sqlStatement.addSelectClause("assetPer2.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_NAME.getValue());
			sqlStatement.addSelectClause("info.PERIPHERALS_FUNCTION2", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION2.getValue());
			sqlStatement.addSelectClause("info.PERIPHERALS3", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3.getValue());
			sqlStatement.addSelectClause("assetPer3.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_NAME.getValue());
			sqlStatement.addSelectClause("info.PERIPHERALS_FUNCTION3", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION3.getValue());
			
			sqlStatement.addSelectClause("info.UPDATED_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			sqlStatement.addSelectClause("info.UPDATED_BY_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("info.UPDATED_BY_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATED_BY_ID.getValue());
			sqlStatement.addSelectClause("contract1.CONTRACT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_CONTRACT.getValue());
			sqlStatement.addSelectClause("contract2.CONTRACT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_CONTRACT.getValue());
			sqlStatement.addSelectClause("contract3.CONTRACT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_CONTRACT.getValue());
			sqlStatement.addSelectClause("link.SERIAL_NUMBER", SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("link.ENABLE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.ENABLE_DATE.getValue());
			sqlStatement.addSelectClause("edcWarehouse.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.WAREHOUSE_NAME.getValue());
			
			sqlStatement.addSelectClause("link1.SERIAL_NUMBER", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("link2.SERIAL_NUMBER", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("link3.SERIAL_NUMBER", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("info.FIRST_DESCRIPTION", SrmCaseHandleInfoDTO.ATTRIBUTE.FIRST_DESCRIPTION.getValue());
			sqlStatement.addSelectClause("info.SECOND_DESCRIPTION", SrmCaseHandleInfoDTO.ATTRIBUTE.SECOND_DESCRIPTION.getValue());
			sqlStatement.addSelectClause("info.THIRD_DESCRIPTION", SrmCaseHandleInfoDTO.ATTRIBUTE.THIRD_DESCRIPTION.getValue());
			// 周邊設備功能名稱
			builder.delete(0, builder.length());
			builder.append("stuff(( SELECT ',' + RTRIM( supportedFun.ITEM_NAME ) FROM ").append(schema);
			// 判斷標志位
			if(StringUtils.hasText(isHistory) && IAtomsConstants.YES.equals(isHistory)){
				builder.append(".SRM_HISTORY_CASE_ASSET_FUNCTION caseAssetFun ");
			} else {
				builder.append(".SRM_CASE_ASSET_FUNCTION caseAssetFun ");
			}
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF supportedFun on supportedFun.BPTD_CODE = :supportedFunctionParam");
			builder.append(" and supportedFun.ITEM_VALUE = caseAssetFun.FUNCTION_ID where caseAssetFun.CASE_ID = info.CASE_ID ");
			builder.append(" and caseAssetFun.FUNCTION_CATEGORY = '11' FOR XML path('') ) ,1 , 1 ,'')");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION_NAME.getValue());
			
			// 周邊設備2功能名稱
			builder.delete(0, builder.length());
			builder.append("stuff(( SELECT ',' + RTRIM( supportedFun.ITEM_NAME ) FROM ").append(schema);
			// 判斷標志位
			if(StringUtils.hasText(isHistory) && IAtomsConstants.YES.equals(isHistory)){
				builder.append(".SRM_HISTORY_CASE_ASSET_FUNCTION caseAssetFun ");
			} else {
				builder.append(".SRM_CASE_ASSET_FUNCTION caseAssetFun ");
			}
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF supportedFun on supportedFun.BPTD_CODE = :supportedFunctionParam");
			builder.append(" and supportedFun.ITEM_VALUE = caseAssetFun.FUNCTION_ID where caseAssetFun.CASE_ID = info.CASE_ID ");
			builder.append(" and caseAssetFun.FUNCTION_CATEGORY = '12' FOR XML path('') ) ,1 , 1 ,'')");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION2_NAME.getValue());
			
			// 周邊設備3功能名稱
			builder.delete(0, builder.length());
			builder.append("stuff(( SELECT ',' + RTRIM( supportedFun.ITEM_NAME ) FROM ").append(schema);
			// 判斷標志位
			if(StringUtils.hasText(isHistory) && IAtomsConstants.YES.equals(isHistory)){
				builder.append(".SRM_HISTORY_CASE_ASSET_FUNCTION caseAssetFun ");
			} else {
				builder.append(".SRM_CASE_ASSET_FUNCTION caseAssetFun ");
			}
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF supportedFun on supportedFun.BPTD_CODE = :supportedFunctionParam");
			builder.append(" and supportedFun.ITEM_VALUE = caseAssetFun.FUNCTION_ID where caseAssetFun.CASE_ID  = info.CASE_ID ");
			builder.append(" and caseAssetFun.FUNCTION_CATEGORY = '13' FOR XML path('') ) ,1 , 1 ,'')");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION3_NAME.getValue());
			
			
			builder.delete(0, builder.length());
			// 判斷標志位
			if(StringUtils.hasText(isHistory) && IAtomsConstants.YES.equals(isHistory)){
				builder.append(schema).append(".SRM_HISTORY_CASE_HANDLE_INFO info");
			} else {
				builder.append(schema).append(".SRM_CASE_HANDLE_INFO info");
			}
			
			builder.append(" left join ").append(schema).append(".DMM_ASSET_TYPE assetPer on assetPer.ASSET_TYPE_ID = info.PERIPHERALS");
			builder.append(" left join ").append(schema).append(".DMM_ASSET_TYPE assetPer2 on assetPer2.ASSET_TYPE_ID = info.PERIPHERALS2");
			builder.append(" left join ").append(schema).append(".DMM_ASSET_TYPE assetPer3 on assetPer3.ASSET_TYPE_ID = info.PERIPHERALS3");
			
			builder.append(" left join ").append(schema).append(".SRM_CASE_ASSET_LINK link on link.CASE_ID = info.CASE_ID and link.ITEM_TYPE='10'");
			builder.append(" left join ").append(schema).append(".SRM_CASE_ASSET_LINK link1 on link1.CASE_ID = info.CASE_ID and link1.ITEM_TYPE='11'");
			builder.append(" left join ").append(schema).append(".SRM_CASE_ASSET_LINK link2 on link2.CASE_ID = info.CASE_ID and link2.ITEM_TYPE='12'");
			builder.append(" left join ").append(schema).append(".SRM_CASE_ASSET_LINK link3 on link3.CASE_ID = info.CASE_ID and link3.ITEM_TYPE='13'");
			
			
			builder.append(" left join ").append(schema).append(".BIM_CONTRACT contract1 on link1.CONTRACT_ID = contract1.CONTRACT_ID");
			builder.append(" left join ").append(schema).append(".BIM_CONTRACT contract2 on link2.CONTRACT_ID = contract2.CONTRACT_ID");
			builder.append(" left join ").append(schema).append(".BIM_CONTRACT contract3 on link3.CONTRACT_ID = contract3.CONTRACT_ID");
			
			builder.append(" LEFT JOIN ").append(schema).append(".BIM_WAREHOUSE edcWarehouse  ON link.WAREHOUSE_ID = edcWarehouse.WAREHOUSE_ID ");
			sqlStatement.addFromExpression(builder.toString());
			if (StringUtils.hasText(caseId)) {
				sqlStatement.addWhereClause("info.CASE_ID in ( :caseId)");
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("caseId", StringUtils.toList(caseId, IAtomsConstants.MARK_SEPARATOR));
			sqlQueryBean.setParameter("supportedFunctionParam", IATOMS_PARAM_TYPE.SUPPORTED_FUNCTION.getCode());
			
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseHandleInfoDTO.class);
			LOGGER.debug("getChangeCaseInfoById()", "sql:" + sqlQueryBean.toString());
			List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(caseHandleInfoDTOs)){
				return caseHandleInfoDTOs;
			}
		} catch (Exception e) {
			LOGGER.error("getChangeCaseInfoById()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		return null;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#getCaseInfoById(java.lang.String)
	 */
	@Override
	public List<SrmCaseHandleInfoDTO> getCaseInfoById(String caseId, String isHistory) throws DataAccessException {
		LOGGER.debug("getCaseInfoById()", "parameters : caseId =" + caseId);
		String schema = this.getMySchema();
		StringBuilder builder = new StringBuilder();
		try{
			SqlStatement  sqlStatement = new SqlStatement();
			// Bug #2297
			if(!IAtomsConstants.YES.equals(isHistory)){
				sqlStatement.addSelectClause("info.IS_UPDATE_ASSET", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_UPDATE_ASSET.getValue());
			}
			sqlStatement.addSelectClause("info.CASE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("STUFF((SELECT DISTINCT ','+ ltrim(trans.TID) FROM " + schema + ".SRM_CASE_TRANSACTION_PARAMETER trans "
					.concat(" WHERE trans.CASE_ID = info.CASE_ID and trans.TID is not null and trans.TID <> '' and trans.TRANSACTION_TYPE in ('COMMON_VM','COMMON_VMJ','COMMON_VMJU')  FOR XML PATH('')), 1, 1, '')"), SrmCaseHandleInfoDTO.ATTRIBUTE.TID.getValue());
			sqlStatement.addSelectClause("info.REQUIREMENT_NO", SrmCaseHandleInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue());
			sqlStatement.addSelectClause("info.CONTRACT_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTRACT_ID.getValue());
			sqlStatement.addSelectClause("info.CASE_CATEGORY", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue());
			sqlStatement.addSelectClause("contract.CONTRACT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue());
			sqlStatement.addSelectClause("info.CUSTOMER_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue());
			sqlStatement.addSelectClause("company.SHORT_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_NAME.getValue());
			sqlStatement.addSelectClause("company.COMPANY_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_CODE.getValue());
			//Task #3323 (Start)
			sqlStatement.addSelectClause("merchant.UNITY_NUMBER", SrmCaseHandleInfoDTO.ATTRIBUTE.UNITY_NUMBER.getValue());
			//Task #3323 (end)
			sqlStatement.addSelectClause("vendor.SHORT_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_NAME.getValue());
			sqlStatement.addSelectClause("info.DEPARTMENT_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_ID.getValue());
//			sqlStatement.addSelectClause("dept.DEPT_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_NAME.getValue());
			sqlStatement.addSelectClause("(case when dept.DEPT_NAME is not null then dept.DEPT_NAME else deptCaseGroup.ITEM_NAME end)", SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_NAME.getValue());
			
			sqlStatement.addSelectClause("info.DTID", SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("info.INSTALL_TYPE", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_TYPE.getValue());
			sqlStatement.addSelectClause("instalType.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("info.COMPANY_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_ID.getValue());
			sqlStatement.addSelectClause("info.REPAIR_REASON", SrmCaseHandleInfoDTO.ATTRIBUTE.REPAIR_REASON.getValue());
			sqlStatement.addSelectClause("repairReason.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.REPAIR_REASON_NAME.getValue());
			sqlStatement.addSelectClause("info.UNINSTALL_TYPE", SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALL_TYPE.getValue());
			sqlStatement.addSelectClause("uninstallType.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALL_TYPE_NAME.getValue());
			
			sqlStatement.addSelectClause("(case when info.CONTACT_IS_BUSSINESS_ADDRESS = 'Y' then headerLocation.ITEM_NAME " +
					"else contactLocation.ITEM_NAME end)", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS_LOCATION_NAME.getValue());
			sqlStatement.addSelectClause("(case when info.CONTACT_IS_BUSSINESS_ADDRESS = 'Y' then header.LOCATION " +
					"else info.CONTACT_ADDRESS_LOCATION end)", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS_LOCATION.getValue());
			sqlStatement.addSelectClause("(case when info.CONTACT_IS_BUSSINESS_ADDRESS = 'Y' then header.POST_CODE_ID " +
					"else info.CONTACT_POST_CODE end)", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_POST_CODE.getValue());
			sqlStatement.addSelectClause("(case when info.CONTACT_IS_BUSSINESS_ADDRESS = 'Y' then header.BUSINESS_ADDRESS " +
					"else info.CONTACT_ADDRESS end)", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS.getValue());
			
			
//			sqlStatement.addSelectClause("info.CONTACT_ADDRESS", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS.getValue());
//			sqlStatement.addSelectClause("info.CONTACT_ADDRESS_LOCATION", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS_LOCATION.getValue());
			/*sqlStatement.addSelectClause("contactLocation.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS_LOCATION_NAME.getValue());*/
			sqlStatement.addSelectClause("info.CONTACT_IS_BUSSINESS_ADDRESS", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_IS_BUSSINESS_ADDRESS.getValue());
			
			sqlStatement.addSelectClause("(case when info.CONTACT_IS_BUSSINESS_CONTACT = 'Y' then header.CONTACT " +
					"else info.CONTACT_USER end)", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER.getValue());
			
			/*sqlStatement.addSelectClause("info.CONTACT_USER", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER.getValue());*/
			sqlStatement.addSelectClause("info.CONTACT_IS_BUSSINESS_CONTACT", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_IS_BUSSINESS_CONTACT.getValue());
			
			sqlStatement.addSelectClause("(case when info.CONTACT_IS_BUSSINESS_CONTACT_PHONE = 'Y' then header.CONTACT_TEL " +
					"else info.CONTACT_USER_PHONE end)", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_PHONE.getValue());
			
			/*sqlStatement.addSelectClause("info.CONTACT_USER_PHONE", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_PHONE.getValue());*/
			sqlStatement.addSelectClause("info.CONTACT_IS_BUSSINESS_CONTACT_PHONE", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_IS_BUSSINESS_CONTACT_PHONE.getValue());
			sqlStatement.addSelectClause("info.CASE_TYPE", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE.getValue());
			sqlStatement.addSelectClause("ticketMode.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("info.EXPECTED_COMPLETION_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.EXPECTED_COMPLETION_DATE.getValue());
			sqlStatement.addSelectClause("merchant.MERCHANT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.MER_MID.getValue());
			sqlStatement.addSelectClause("merchant.MERCHANT_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("merchant.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue());
			sqlStatement.addSelectClause("info.MERCHANT_HEADER_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_HEADER_ID.getValue());
			sqlStatement.addSelectClause("header.HEADER_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.HEADER_NAME.getValue());
			sqlStatement.addSelectClause("header.IS_VIP", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_VIP.getValue());
			sqlStatement.addSelectClause("header.AREA", SrmCaseHandleInfoDTO.ATTRIBUTE.AREA.getValue());
			sqlStatement.addSelectClause("region.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.AREA_NAME.getValue());
			sqlStatement.addSelectClause("header.HEADER_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_HEADER_NAME.getValue());
			sqlStatement.addSelectClause("header.CONTACT", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT.getValue());
			sqlStatement.addSelectClause("header.CONTACT_TEL", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_TEL.getValue());
			sqlStatement.addSelectClause("header.CONTACT_TEL2", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_TEL2.getValue());
			sqlStatement.addSelectClause("header.PHONE", SrmCaseHandleInfoDTO.ATTRIBUTE.PHONE.getValue());
			sqlStatement.addSelectClause("header.BUSINESS_ADDRESS", SrmCaseHandleInfoDTO.ATTRIBUTE.BUSINESS_ADDRESS.getValue());
			sqlStatement.addSelectClause("header.OPEN_HOUR", SrmCaseHandleInfoDTO.ATTRIBUTE.OPEN_HOUR.getValue());
			sqlStatement.addSelectClause("header.CLOSE_HOUR", SrmCaseHandleInfoDTO.ATTRIBUTE.CLOSE_HOUR.getValue());
			sqlStatement.addSelectClause("header.AOEmail", SrmCaseHandleInfoDTO.ATTRIBUTE.AO_EMAIL.getValue());
			sqlStatement.addSelectClause("header.AO_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.AO_NAME.getValue());
			sqlStatement.addSelectClause("header.LOCATION", SrmCaseHandleInfoDTO.ATTRIBUTE.LOCATION.getValue());
			sqlStatement.addSelectClause("headerLocation.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.LOCATION_NAME.getValue());
			sqlStatement.addSelectClause("header.POST_CODE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.LOCATION_POST_CODE.getValue());
			//Task #3343
			sqlStatement.addSelectClause("header.CONTACT_EMAIL", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_EMAIL.getValue());
			sqlStatement.addSelectClause("info.IS_BUSSINESS_CONTACT_MOBILE_PHONE", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_BUSSINESS_CONTACT_MOBILE_PHONE.getValue());
			sqlStatement.addSelectClause("info.IS_BUSSINESS_CONTACT_EMAIL", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_BUSSINESS_CONTACT_EMAIL.getValue());
			sqlStatement.addSelectClause("info.INSTALLED_CONTACT_MOBILE_PHONE", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_MOBILE_PHONE.getValue());
			sqlStatement.addSelectClause("info.INSTALLED_CONTACT_EMAIL", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_EMAIL.getValue());
			//sqlStatement.addSelectClause("info.INSTALLED_POST_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_POST_CODE.getValue());
			sqlStatement.addSelectClause("info.CONTACT_IS_BUSSINESS_CONTACT_MOBILE_PHONE", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_IS_BUSSINESS_CONTACT_MOBILE_PHONE.getValue());
			sqlStatement.addSelectClause("info.CONTACT_IS_BUSSINESS_CONTACT_EMAIL", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_IS_BUSSINESS_CONTACT_EMAIL.getValue());
			sqlStatement.addSelectClause("info.CONTACT_MOBILE_PHONE", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_MOBILE_PHONE.getValue());
			sqlStatement.addSelectClause("info.CONTACT_USER_EMAIL", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_EMAIL.getValue());
			//sqlStatement.addSelectClause("info.CONTACT_POST_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_POST_CODE.getValue());
			//Task #3322
			sqlStatement.addSelectClause("info.CMS_CASE", SrmCaseHandleInfoDTO.ATTRIBUTE.CMS_CASE.getValue());
			//Task #3404
			sqlStatement.addSelectClause("info.IS_IATOMS_CREATE_CMS", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_IATOMS_CREATE_CMS.getValue());
			/*sqlStatement.addSelectClause("info.INSTALLED_ADRESS", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue());*/
			/*sqlStatement.addSelectClause("info.INSTALLED_ADRESS_LOCATION", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS_LOCATION.getValue());*/
			StringBuffer seleBuffer = new StringBuffer();
			seleBuffer.append("(case when info.CASE_CATEGORY = 'OTHER' then info.INSTALLED_ADRESS else ");
			seleBuffer.append("(case when info.IS_BUSSINESS_ADDRESS = 'Y' then headerLocation.ITEM_NAME ");
			seleBuffer.append("else installLocation.ITEM_NAME end) end) ");
			sqlStatement.addSelectClause(seleBuffer.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS_LOCATION_NAME.getValue());
			/*sqlStatement.addSelectClause("(case when info.IS_BUSSINESS_ADDRESS = 'Y' then headerLocation.ITEM_NAME " +
					"else installLocation.ITEM_NAME end)", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS_LOCATION_NAME.getValue());*/
			seleBuffer = new StringBuffer();
			seleBuffer.append("(case when info.CASE_CATEGORY = 'OTHER' then info.INSTALLED_ADRESS_LOCATION else ");
			seleBuffer.append("(case when info.IS_BUSSINESS_ADDRESS = 'Y' then header.LOCATION ");
			seleBuffer.append("else info.INSTALLED_ADRESS_LOCATION end) end) ");
			sqlStatement.addSelectClause(seleBuffer.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS_LOCATION.getValue());
			
			seleBuffer = new StringBuffer();
			seleBuffer.append("(case when info.CASE_CATEGORY = 'OTHER' then info.INSTALLED_POST_CODE else ");
			seleBuffer.append("(case when info.IS_BUSSINESS_ADDRESS = 'Y' then header.POST_CODE_ID ");
			seleBuffer.append("else info.INSTALLED_POST_CODE end) end) ");
			sqlStatement.addSelectClause(seleBuffer.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_POST_CODE.getValue());
			/*sqlStatement.addSelectClause("(case when info.IS_BUSSINESS_ADDRESS = 'Y' then header.LOCATION " +
					"else info.INSTALLED_ADRESS_LOCATION end)", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS_LOCATION.getValue());*/
			seleBuffer = new StringBuffer();
			seleBuffer.append("(case when info.CASE_CATEGORY = 'OTHER' then info.INSTALLED_ADRESS else ");
			seleBuffer.append("(case when info.IS_BUSSINESS_ADDRESS = 'Y' then header.BUSINESS_ADDRESS ");
			seleBuffer.append("else info.INSTALLED_ADRESS end) end) ");
			sqlStatement.addSelectClause(seleBuffer.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue());
			/*sqlStatement.addSelectClause("(case when info.IS_BUSSINESS_ADDRESS = 'Y' then header.BUSINESS_ADDRESS " +
					"else info.INSTALLED_ADRESS end)", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue());*/
			/*sqlStatement.addSelectClause("installLocation.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS_LOCATION_NAME.getValue());*/
			sqlStatement.addSelectClause("info.IS_BUSSINESS_ADDRESS", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_BUSSINESS_ADDRESS.getValue());
			sqlStatement.addSelectClause("(case when info.IS_BUSSINESS_CONTACT = 'Y' then header.CONTACT " +
					"else info.INSTALLED_CONTACT end)", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT.getValue());
			/*sqlStatement.addSelectClause("info.INSTALLED_CONTACT", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT.getValue());*/
			sqlStatement.addSelectClause("info.IS_BUSSINESS_CONTACT", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_BUSSINESS_CONTACT.getValue());
			sqlStatement.addSelectClause("(case when info.IS_BUSSINESS_CONTACT_PHONE = 'Y' then header.CONTACT_TEL " +
					"else info.INSTALLED_CONTACT_PHONE end)", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_PHONE.getValue());
			/*sqlStatement.addSelectClause("info.INSTALLED_CONTACT_PHONE", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_PHONE.getValue());*/
			sqlStatement.addSelectClause("info.IS_BUSSINESS_CONTACT_PHONE", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_BUSSINESS_CONTACT_PHONE.getValue());
			sqlStatement.addSelectClause("info.EDC_TYPE", SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE.getValue());
			sqlStatement.addSelectClause("assetEdc.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("info.SOFTWARE_VERSION", SrmCaseHandleInfoDTO.ATTRIBUTE.SOFTWARE_VERSION.getValue());
			
			sqlStatement.addSelectClause("case when isnull(info.SOFTWARE_VERSION, '') = '' then null "
				+" else (CONCAT( CONVERT( VARCHAR( 100 ) ,application.NAME ,111 ) ,'【' ,CONVERT( VARCHAR( 100 ) ,application.VERSION ,111 ) ,'】')) "
				+ " end", SrmCaseHandleInfoDTO.ATTRIBUTE.SOFTWARE_VERSION_NAME.getValue());
			
		//	sqlStatement.addSelectClause("CONCAT( CONVERT( VARCHAR( 100 ) ,application.NAME ,111 ) ,'【' ,CONVERT( VARCHAR( 100 ) ,application.VERSION ,111 ) ,'】')", SrmCaseHandleInfoDTO.ATTRIBUTE.SOFTWARE_VERSION_NAME.getValue());
		//	sqlStatement.addSelectClause("CONCAT( CONVERT( VARCHAR( 100 ) ,application.VERSION ,111 ) ,'[' ,CONVERT( VARCHAR( 100 ) ,application.NAME ,111 ) ,']')", SrmCaseHandleInfoDTO.ATTRIBUTE.SOFTWARE_VERSION_NAME.getValue());
			sqlStatement.addSelectClause("info.BUILT_IN_FEATURE", SrmCaseHandleInfoDTO.ATTRIBUTE.BUILT_IN_FEATURE.getValue());
			sqlStatement.addSelectClause("info.MULTI_MODULE", SrmCaseHandleInfoDTO.ATTRIBUTE.MULTI_MODULE.getValue());
			sqlStatement.addSelectClause("item.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.MULTI_MODULE_NAME.getValue());
			sqlStatement.addSelectClause("info.PERIPHERALS", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS.getValue());
			sqlStatement.addSelectClause("assetPer.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_NAME.getValue());
			sqlStatement.addSelectClause("info.PERIPHERALS_FUNCTION", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION.getValue());
			sqlStatement.addSelectClause("info.PERIPHERALS2", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2.getValue());
			sqlStatement.addSelectClause("assetPer2.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_NAME.getValue());
			sqlStatement.addSelectClause("info.PERIPHERALS_FUNCTION2", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION2.getValue());
			sqlStatement.addSelectClause("info.PERIPHERALS3", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3.getValue());
			sqlStatement.addSelectClause("assetPer3.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_NAME.getValue());
			sqlStatement.addSelectClause("info.PERIPHERALS_FUNCTION3", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION3.getValue());
			sqlStatement.addSelectClause("info.ECR_CONNECTION", SrmCaseHandleInfoDTO.ATTRIBUTE.ECR_CONNECTION.getValue());
			sqlStatement.addSelectClause("itemEcr.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.ECR_CONNECTION_NAME.getValue());
			sqlStatement.addSelectClause("info.CONNECTION_TYPE", SrmCaseHandleInfoDTO.ATTRIBUTE.CONNECTION_TYPE.getValue());
			
			sqlStatement.addSelectClause("info.LOGISTICS_VENDOR", SrmCaseHandleInfoDTO.ATTRIBUTE.LOGISTICS_VENDOR.getValue());
			sqlStatement.addSelectClause("info.LOGISTICS_NUMBER", SrmCaseHandleInfoDTO.ATTRIBUTE.LOGISTICS_NUMBER.getValue());
			
//			sqlStatement.addSelectClause("itemConnec.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CONNECTION_TYPE_NAME.getValue());
			builder.delete(0, builder.length());
			// 連線方式名稱
			builder.append("stuff(( SELECT ',' + RTRIM( itemConnec.ITEM_NAME ) FROM ").append(schema);
			// 判斷標志位
			if(StringUtils.hasText(isHistory) && IAtomsConstants.YES.equals(isHistory)){
				builder.append(".SRM_HISTORY_CASE_COMM_MODE caseCommMode ");
			} else {
				builder.append(".SRM_CASE_COMM_MODE caseCommMode ");
			}
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF itemConnec on itemConnec.BPTD_CODE = :connection");
			builder.append(" and itemConnec.DELETED <> 'Y' and itemConnec.ITEM_VALUE = caseCommMode.COMM_MODE_ID where caseCommMode.CASE_ID = info.CASE_ID ");
			builder.append("  FOR XML path('') ) ,1 , 1 ,'')");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONNECTION_TYPE_NAME.getValue());
			
			sqlStatement.addSelectClause("info.LOCALHOST_IP", SrmCaseHandleInfoDTO.ATTRIBUTE.LOCALHOST_IP.getValue());
			sqlStatement.addSelectClause("info.NET_VENDOR_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.NET_VENDOR_ID.getValue());
			sqlStatement.addSelectClause("netVendor.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.NET_VENDOR_NAME.getValue());
			sqlStatement.addSelectClause("info.GATEWAY", SrmCaseHandleInfoDTO.ATTRIBUTE.GATEWAY.getValue());
			sqlStatement.addSelectClause("info.NETMASK", SrmCaseHandleInfoDTO.ATTRIBUTE.NETMASK.getValue());
			sqlStatement.addSelectClause("info.DESCRIPTION", SrmCaseHandleInfoDTO.ATTRIBUTE.DESCRIPTION.getValue());
			sqlStatement.addSelectClause("info.ATTENDANCE_TIMES", SrmCaseHandleInfoDTO.ATTRIBUTE.ATTENDANCE_TIMES.getValue());
			sqlStatement.addSelectClause("info.CASE_STATUS", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS.getValue());
			sqlStatement.addSelectClause("info.IS_PROJECT", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_PROJECT.getValue());
			sqlStatement.addSelectClause("caseStatus.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS_NAME.getValue());
			sqlStatement.addSelectClause("info.IS_TMS", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_TMS.getValue());
			sqlStatement.addSelectClause("info.REPAIR_TIMES", SrmCaseHandleInfoDTO.ATTRIBUTE.REPAIR_TIMES.getValue());
			sqlStatement.addSelectClause("installName.CNAME", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_USER_NAME.getValue());
			sqlStatement.addSelectClause("info.ACCEPTABLE_ARRIVE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_ARRIVE_DATE.getValue());
			sqlStatement.addSelectClause("info.ACCEPTABLE_FINISH_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_FINISH_DATE.getValue());
			sqlStatement.addSelectClause("info.ACCEPTABLE_RESPONSE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_RESPONSE_DATE.getValue());

			sqlStatement.addSelectClause("info.CREATED_BY_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_BY_ID.getValue());
			sqlStatement.addSelectClause("info.CREATED_BY_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("info.CREATED_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue());
			
			sqlStatement.addSelectClause("info.ARRIVE_USER", SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_USER.getValue());
			sqlStatement.addSelectClause("info.ARRIVE_USER_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_USER_NAME.getValue());
			sqlStatement.addSelectClause("info.ARRIVE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_DATE.getValue());
			sqlStatement.addSelectClause("info.RESPONSE_USER", SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_USER.getValue());
			sqlStatement.addSelectClause("info.RESPONSE_USER_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_USER_NAME.getValue());
			sqlStatement.addSelectClause("info.RESPONSE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_DATE.getValue());
			sqlStatement.addSelectClause("info.DISPATCH_PROCESS_USER", SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_PROCESS_USER.getValue());
			sqlStatement.addSelectClause("info.DISPATCH_PROCESS_USERNAME", SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_PROCESS_USERNAME.getValue());
			sqlStatement.addSelectClause("info.DISPATCH_USER", SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_USER.getValue());
			sqlStatement.addSelectClause("info.DISPATCH_USER_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_USER_NAME.getValue());
			sqlStatement.addSelectClause("info.DISPATCH_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_DATE.getValue());
			sqlStatement.addSelectClause("info.UPDATE_ITEM", SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATE_ITEM.getValue());
			sqlStatement.addSelectClause("info.CUP_DISABLE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_DISABLE_DATE.getValue());
			sqlStatement.addSelectClause("info.CUP_ENABLE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_ENABLE_DATE.getValue());
			sqlStatement.addSelectClause("info.UNINSTALLED_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALLED_DATE.getValue());
			sqlStatement.addSelectClause("info.INSTALLED_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_DATE.getValue());
			sqlStatement.addSelectClause("info.COMPLETE_WARNNING", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_WARNNING.getValue());
			sqlStatement.addSelectClause("info.ARRIVE_WARNNING", SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_WARNNING.getValue());
			sqlStatement.addSelectClause("info.RESPONSE_WARNNING", SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_WARNNING.getValue());
			sqlStatement.addSelectClause("info.RESPONSIBITY", SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSIBITY.getValue());
			sqlStatement.addSelectClause("info.PROBLEM_SOLUTION", SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_SOLUTION.getValue());
			sqlStatement.addSelectClause("info.PROBLEM_REASON", SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_REASON.getValue());
			sqlStatement.addSelectClause("info.INSTALLED_USER", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_USER.getValue());
			sqlStatement.addSelectClause("info.INSTALLED_DEPT_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_DEPT_ID.getValue());
			//裝機部門名稱
			sqlStatement.addSelectClause("installDept.DEPT_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_DEPT_NAME.getValue());
			sqlStatement.addSelectClause("info.CLOSE_USER_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CLOSE_USER_NAME.getValue());
			sqlStatement.addSelectClause("info.CLOSE_USER", SrmCaseHandleInfoDTO.ATTRIBUTE.CLOSE_USER.getValue());
			sqlStatement.addSelectClause("info.ANALYZE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.ANALYZE_DATE.getValue());
			sqlStatement.addSelectClause("info.ANALYZE_USER_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.ANALYZE_USER_NAME.getValue());
			sqlStatement.addSelectClause("info.ANALYZE_USER", SrmCaseHandleInfoDTO.ATTRIBUTE.ANALYZE_USER.getValue());
			sqlStatement.addSelectClause("info.COMPLETE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue());
			sqlStatement.addSelectClause("info.COMPLETE_USER_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_USER_NAME.getValue());
			sqlStatement.addSelectClause("info.COMPLETE_USER", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_USER.getValue());
			sqlStatement.addSelectClause("info.CONTACT_ADDRESS_LOCATION", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS_LOCATION.getValue());
			sqlStatement.addSelectClause("info.UPDATED_DESCRIPTION", SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATED_DESCRIPTION.getValue());
			sqlStatement.addSelectClause("info.DISPATCH_DEPT_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_DEPT_ID.getValue());
			sqlStatement.addSelectClause("info.CLOSE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.CLOSE_DATE.getValue());
			sqlStatement.addSelectClause("info.CREATED_FINISH_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_FINISH_DATE.getValue());
			sqlStatement.addSelectClause("info.UPDATED_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATED_DATE.getValue());
			sqlStatement.addSelectClause("info.UPDATED_BY_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			sqlStatement.addSelectClause("info.UPDATED_BY_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATED_BY_ID.getValue());
			sqlStatement.addSelectClause("info.EDC_TYPE", SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE.getValue());
			sqlStatement.addSelectClause("info.OLD_MERCHANT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.OLD_MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("info.PROJECT_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.PROJECT_NAME.getValue());
			sqlStatement.addSelectClause("info.PROJECT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.PROJECT_CODE.getValue());
			sqlStatement.addSelectClause("info.PROCESS_TYPE", SrmCaseHandleInfoDTO.ATTRIBUTE.PROCESS_TYPE.getValue());
			
			sqlStatement.addSelectClause("info.PROBLEM_REASON_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_REASON_CODE.getValue());
			sqlStatement.addSelectClause("info.PROBLEM_SOLUTION_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_SOLUTION_CODE.getValue());
			sqlStatement.addSelectClause("info.ELECTRONIC_INVOICE", SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_INVOICE.getValue());
			sqlStatement.addSelectClause("info.CUP_QUICK_PASS", SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_QUICK_PASS.getValue());
			sqlStatement.addSelectClause("(case when dispatchDept.DEPT_NAME is not null then dispatchDept.DEPT_NAME else caseGroup.ITEM_NAME end)", SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_DEPT_NAME.getValue());
			sqlStatement.addSelectClause("info.SAME_INSTALLED", SrmCaseHandleInfoDTO.ATTRIBUTE.SAME_INSTALLED.getValue());
			sqlStatement.addSelectClause("info.TMS_PARAM_DESC", SrmCaseHandleInfoDTO.ATTRIBUTE.TMS_PARAM_DESC.getValue());
			sqlStatement.addSelectClause("info.INSTALLED_DEPT_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_DEPT_ID.getValue());
			sqlStatement.addSelectClause("info.COMPLETE_DEPARTMENT_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DEPARTMENT_ID.getValue());
			sqlStatement.addSelectClause("info.HAS_ONLINE_EXCLUSION", SrmCaseHandleInfoDTO.ATTRIBUTE.HAS_ONLINE_EXCLUSION.getValue());
			
			sqlStatement.addSelectClause("postCode.POST_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_POST_AREA.getValue());
			sqlStatement.addSelectClause("postCode.POST_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_AREA_CODE.getValue());
			
			// LOGO
			sqlStatement.addSelectClause("info.LOGO_STYLE", SrmCaseHandleInfoDTO.ATTRIBUTE.LOGO_STYLE.getValue());
			// 是否開啟加密
			sqlStatement.addSelectClause("info.IS_OPEN_ENCRYPT", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_OPEN_ENCRYPT.getValue());
			// 電子化繳費平台
			sqlStatement.addSelectClause("info.ELECTRONIC_PAY_PLATFORM", SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_PAY_PLATFORM.getValue());
			//是否首裝
			sqlStatement.addSelectClause("info.IS_FIRST_INSTALLED", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_FIRST_INSTALLED.getValue());
			// FOMS_CASE
			sqlStatement.addSelectClause("info.FOMS_CASE", SrmCaseHandleInfoDTO.ATTRIBUTE.FOMS_CASE.getValue());
			//CR #2869 新增三個欄位 2017/11/22
			sqlStatement.addSelectClause("info.FIRST_DESCRIPTION", SrmCaseHandleInfoDTO.ATTRIBUTE.FIRST_DESCRIPTION.getValue());
			sqlStatement.addSelectClause("info.SECOND_DESCRIPTION", SrmCaseHandleInfoDTO.ATTRIBUTE.SECOND_DESCRIPTION.getValue());
			sqlStatement.addSelectClause("info.THIRD_DESCRIPTION", SrmCaseHandleInfoDTO.ATTRIBUTE.THIRD_DESCRIPTION.getValue());
			
			// 案件建案人員所在公司
			sqlStatement.addSelectClause("vendorService.COMPANY_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.VENDOR_SERVICE_CUSTOMER.getValue());
			// Task #3110 退回标记
			sqlStatement.addSelectClause("info.HAS_RETREAT", SrmCaseHandleInfoDTO.ATTRIBUTE.HAS_RETREAT.getValue());
			// Task #3205 是否執行過延期
			sqlStatement.addSelectClause("info.HAS_DELAY", SrmCaseHandleInfoDTO.ATTRIBUTE.HAS_DELAY.getValue());
			sqlStatement.addSelectClause("info.CONFIRM_AUTHORIZES", SrmCaseHandleInfoDTO.ATTRIBUTE.CONFIRM_AUTHORIZES.getValue());
			//Task #3349 
			sqlStatement.addSelectClause("info.INSTALL_CASE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_CASE_ID.getValue());
			sqlStatement.addSelectClause("info.INSTALL_COMPLETE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_COMPLETE_DATE.getValue());
			//Task #3390
			sqlStatement.addSelectClause("info.PRELOAD_SERIAL_NUMBER", SrmCaseHandleInfoDTO.ATTRIBUTE.PRELOAD_SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("info.SIM_SERIAL_NUMBER", SrmCaseHandleInfoDTO.ATTRIBUTE.SIM_SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("info.RECEIPT_TYPE", SrmCaseHandleInfoDTO.ATTRIBUTE.RECEIPT_TYPE.getValue());
			sqlStatement.addSelectClause("receiptItem.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.RECEIPT_TYPE_NAME.getValue());
			if(!(StringUtils.hasText(isHistory) && IAtomsConstants.YES.equals(isHistory))){
				//Task #3548
				sqlStatement.addSelectClause("info.HAS_ARRIVE", SrmCaseHandleInfoDTO.ATTRIBUTE.HAS_ARRIVE.getValue());
			}
			builder.delete(0, builder.length());
			// 內建功能名稱
			builder.append("stuff(( SELECT ',' + RTRIM( supportedFun.ITEM_NAME ) FROM ").append(schema);
			// 判斷標志位
			if(StringUtils.hasText(isHistory) && IAtomsConstants.YES.equals(isHistory)){
				builder.append(".SRM_HISTORY_CASE_ASSET_FUNCTION caseAssetFun ");
			} else {
				builder.append(".SRM_CASE_ASSET_FUNCTION caseAssetFun ");
			}
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF supportedFun on supportedFun.BPTD_CODE = :supportedFunctionParam");
			builder.append(" and supportedFun.ITEM_VALUE = caseAssetFun.FUNCTION_ID where caseAssetFun.CASE_ID = info.CASE_ID ");
			builder.append(" and caseAssetFun.FUNCTION_CATEGORY = '10' FOR XML path('') ) ,1 , 1 ,'')");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.BUILT_IN_FEATURE_NAME.getValue());
			
			// 周邊設備功能名稱
			builder.delete(0, builder.length());
			builder.append("stuff(( SELECT ',' + RTRIM( supportedFun.ITEM_NAME ) FROM ").append(schema);
			// 判斷標志位
			if(StringUtils.hasText(isHistory) && IAtomsConstants.YES.equals(isHistory)){
				builder.append(".SRM_HISTORY_CASE_ASSET_FUNCTION caseAssetFun ");
			} else {
				builder.append(".SRM_CASE_ASSET_FUNCTION caseAssetFun ");
			}
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF supportedFun on supportedFun.BPTD_CODE = :supportedFunctionParam");
			builder.append(" and supportedFun.ITEM_VALUE = caseAssetFun.FUNCTION_ID where caseAssetFun.CASE_ID = info.CASE_ID ");
			builder.append(" and caseAssetFun.FUNCTION_CATEGORY = '11' FOR XML path('') ) ,1 , 1 ,'')");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION_NAME.getValue());
			
			// 周邊設備2功能名稱
			builder.delete(0, builder.length());
			builder.append("stuff(( SELECT ',' + RTRIM( supportedFun.ITEM_NAME ) FROM ").append(schema);
			// 判斷標志位
			if(StringUtils.hasText(isHistory) && IAtomsConstants.YES.equals(isHistory)){
				builder.append(".SRM_HISTORY_CASE_ASSET_FUNCTION caseAssetFun ");
			} else {
				builder.append(".SRM_CASE_ASSET_FUNCTION caseAssetFun ");
			}
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF supportedFun on supportedFun.BPTD_CODE = :supportedFunctionParam");
			builder.append(" and supportedFun.ITEM_VALUE = caseAssetFun.FUNCTION_ID where caseAssetFun.CASE_ID = info.CASE_ID ");
			builder.append(" and caseAssetFun.FUNCTION_CATEGORY = '12' FOR XML path('') ) ,1 , 1 ,'')");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION2_NAME.getValue());
			
			// 周邊設備3功能名稱
			builder.delete(0, builder.length());
			builder.append("stuff(( SELECT ',' + RTRIM( supportedFun.ITEM_NAME ) FROM ").append(schema);
			// 判斷標志位
			if(StringUtils.hasText(isHistory) && IAtomsConstants.YES.equals(isHistory)){
				builder.append(".SRM_HISTORY_CASE_ASSET_FUNCTION caseAssetFun ");
			} else {
				builder.append(".SRM_CASE_ASSET_FUNCTION caseAssetFun ");
			}
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF supportedFun on supportedFun.BPTD_CODE = :supportedFunctionParam");
			builder.append(" and supportedFun.ITEM_VALUE = caseAssetFun.FUNCTION_ID where caseAssetFun.CASE_ID  = info.CASE_ID ");
			builder.append(" and caseAssetFun.FUNCTION_CATEGORY = '13' FOR XML path('') ) ,1 , 1 ,'')");
			sqlStatement.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION3_NAME.getValue());
			
			sqlStatement.addSelectClause("(CONCAT(installedPostCode.POST_CODE  ,'(' ,installedPostCode.POST_NAME ,')')) ", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_POST_CODE_NAME.getValue());
			sqlStatement.addSelectClause("(CONCAT(contactPostCode.POST_CODE  ,'(' ,contactPostCode.POST_NAME ,')')) ", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_POST_CODE_NAME.getValue());
			builder.delete(0, builder.length());
			// 判斷標志位
			if(StringUtils.hasText(isHistory) && IAtomsConstants.YES.equals(isHistory)){
				builder.append(schema).append(".SRM_HISTORY_CASE_HANDLE_INFO info");
			} else {
				builder.append(schema).append(".SRM_CASE_HANDLE_INFO info");
			}
			builder.append(" left join ").append(schema).append(".BIM_CONTRACT contract on contract.CONTRACT_ID=info.CONTRACT_ID");
			builder.append(" left join ").append(schema).append(".PVM_APPLICATION application on application.APPLICATION_ID = info.SOFTWARE_VERSION");
			builder.append(" left join ").append(schema).append(".BIM_COMPANY company on company.COMPANY_ID = info.CUSTOMER_ID");
			builder.append(" left join ").append(schema).append(".BIM_COMPANY vendor on vendor.COMPANY_ID = info.COMPANY_ID");	
			builder.append(" left join ").append(schema).append(".DMM_ASSET_TYPE assetEdc on assetEdc.ASSET_TYPE_ID = info.EDC_TYPE");
			builder.append(" left join ").append(schema).append(".DMM_ASSET_TYPE assetPer on assetPer.ASSET_TYPE_ID = info.PERIPHERALS");
			builder.append(" left join ").append(schema).append(".DMM_ASSET_TYPE assetPer2 on assetPer2.ASSET_TYPE_ID = info.PERIPHERALS2");
			builder.append(" left join ").append(schema).append(".DMM_ASSET_TYPE assetPer3 on assetPer3.ASSET_TYPE_ID = info.PERIPHERALS3");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF item on item.BPTD_CODE = :double and item.ITEM_VALUE = info.MULTI_MODULE");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF itemEcr on itemEcr.BPTD_CODE = :ecr and itemEcr.ITEM_VALUE = info.ECR_CONNECTION");
			
//			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF itemConnec on itemConnec.BPTD_CODE = :connection and itemConnec.ITEM_VALUE = info.CONNECTION_TYPE");
			
			builder.append(" left join ").append(schema).append(".BIM_MERCHANT_HEADER header on header.MERCHANT_HEADER_ID = info.MERCHANT_HEADER_ID");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF region on region.BPTD_CODE = :region and region.ITEM_VALUE = header.AREA");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF headerLocation on headerLocation.BPTD_CODE = :location and headerLocation.ITEM_VALUE = header.LOCATION");
			builder.append(" left join ").append(schema).append(".BIM_MERCHANT merchant on merchant.MERCHANT_ID = info.MERCHANT_CODE");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF netVendor on netVendor.BPTD_CODE = :netVendor and netVendor.ITEM_VALUE = info.NET_VENDOR_ID");
			builder.append(" left join ").append(schema).append(".BIM_DEPARTMENT dept on dept.DEPT_CODE = info.DEPARTMENT_ID");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF deptCaseGroup on deptCaseGroup.BPTD_CODE = :caseGroupParam ");
			builder.append(" and deptCaseGroup.ITEM_VALUE = info.DEPARTMENT_ID ");
			
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF caseStatus on caseStatus.BPTD_CODE = :caseStatus and caseStatus.ITEM_VALUE = info.CASE_STATUS");
			builder.append(" left join ").append(schema).append(".ADM_USER installName on installName.USER_ID = info.INSTALLED_USER");
			builder.append(" left join ").append(schema).append(".BIM_DEPARTMENT dispatchDept on dispatchDept.DEPT_CODE = info.DISPATCH_DEPT_ID ");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF caseGroup on caseGroup.BPTD_CODE = :caseGroupParam ");
			builder.append(" and caseGroup.ITEM_VALUE = info.DISPATCH_DEPT_ID ");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF installLocation on installLocation.BPTD_CODE = :location and installLocation.ITEM_VALUE = info.INSTALLED_ADRESS_LOCATION ");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF contactLocation on contactLocation.BPTD_CODE = :location and contactLocation.ITEM_VALUE = info.CONTACT_ADDRESS_LOCATION ");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF instalType on instalType.BPTD_CODE = :instalType and instalType.ITEM_VALUE = info.INSTALL_TYPE ");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF ticketMode on ticketMode.BPTD_CODE = :ticketMode and ticketMode.ITEM_VALUE = info.CASE_TYPE ");
			builder.append(" left join ").append(schema).append(".BIM_DEPARTMENT installDept on installDept.DEPT_CODE =info.INSTALLED_DEPT_ID ");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF uninstallType on uninstallType.BPTD_CODE = :uninstallType and uninstallType.ITEM_VALUE = info.UNINSTALL_TYPE ");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF repairReason on repairReason.BPTD_CODE = :repairReason and repairReason.ITEM_VALUE = info.REPAIR_REASON ");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_POST_CODE postCode on postCode.POST_CODE_ID = header.POST_CODE_ID");
			builder.append(" left join ").append(schema).append(".ADM_USER vendorService on vendorService.USER_ID = isnull(info.CREATED_BY_ID,'') ");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_POST_CODE installedPostCode on installedPostCode.POST_CODE_ID = info.INSTALLED_POST_CODE");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_POST_CODE contactPostCode on contactPostCode.POST_CODE_ID = info.CONTACT_POST_CODE");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF receiptItem on receiptItem.ITEM_VALUE = info.RECEIPT_TYPE and receiptItem.BPTD_CODE = :receiptType");
			sqlStatement.addFromExpression(builder.toString());
			if (StringUtils.hasText(caseId)) {
				sqlStatement.addWhereClause("info.CASE_ID in ( :caseId)");
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("caseId", StringUtils.toList(caseId, IAtomsConstants.MARK_SEPARATOR));
			sqlQueryBean.setParameter("double", IATOMS_PARAM_TYPE.DOUBLE_MODULE.getCode());
			sqlQueryBean.setParameter("ecr", IATOMS_PARAM_TYPE.ECR_LINE.getCode());
			sqlQueryBean.setParameter("connection", IATOMS_PARAM_TYPE.COMM_MODE.getCode());
			sqlQueryBean.setParameter("region", IATOMS_PARAM_TYPE.REGION.getCode());
			sqlQueryBean.setParameter("netVendor", IATOMS_PARAM_TYPE.NET_VENDOR.getCode());
			sqlQueryBean.setParameter("caseStatus", IATOMS_PARAM_TYPE.CASE_STATUS.getCode());
			sqlQueryBean.setParameter("caseGroupParam", IATOMS_PARAM_TYPE.CASE_GROUP.getCode());
			sqlQueryBean.setParameter("supportedFunctionParam", IATOMS_PARAM_TYPE.SUPPORTED_FUNCTION.getCode());
			sqlQueryBean.setParameter("location", IATOMS_PARAM_TYPE.LOCATION.getCode());
			sqlQueryBean.setParameter("instalType", IATOMS_PARAM_TYPE.INSTALL_TYPE.getCode());
			sqlQueryBean.setParameter("ticketMode", IATOMS_PARAM_TYPE.TICKET_MODE.getCode());
			sqlQueryBean.setParameter("uninstallType", IATOMS_PARAM_TYPE.UNINSTALL_TYPE.getCode());
			sqlQueryBean.setParameter("repairReason", IATOMS_PARAM_TYPE.REPAIR_REASON.getCode());
			sqlQueryBean.setParameter("receiptType", IATOMS_PARAM_TYPE.RECEIPT_TYPE.getCode());
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseHandleInfoDTO.class);
			LOGGER.debug("getCaseInfoById()", "sql:" + sqlQueryBean.toString());
			List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(caseHandleInfoDTOs)){
				return caseHandleInfoDTOs;
			}
		} catch (Exception e) {
			LOGGER.error("getCaseInfoById()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		return null;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#getCaseRepeatList(java.lang.String, java.lang.String)
	 */
	@Override
	public List<SrmCaseHandleInfoDTO> getCaseRepeatList(String dtid,
			String caseId, List<String> caseStatusList) throws DataAccessException {
		LOGGER.debug("getCaseRepeatList()", "parameters : dtid =" + dtid);
		LOGGER.debug("getCaseRepeatList()", "parameters : caseId =" + caseId);
		LOGGER.debug("getCaseRepeatList()", "parameters : caseStatusList =" + caseStatusList);
		List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOs = null;
		try{
			srmCaseHandleInfoDTOs = getCaseRepeatList(dtid, caseId, caseStatusList, null);
		} catch (Exception e) {
			LOGGER.error("getCaseRepeatList()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		return srmCaseHandleInfoDTOs;
	}
	public List<SrmCaseHandleInfoDTO> getCaseRepeatList(String dtid,
			String caseId, List<String> caseStatusList, String caseCategory) throws DataAccessException {
		LOGGER.debug("getCaseRepeatList()", "parameters : dtid =" + dtid);
		LOGGER.debug("getCaseRepeatList()", "parameters : caseId =" + caseId);
		LOGGER.debug("getCaseRepeatList()", "parameters : caseStatusList =" + caseStatusList);
		LOGGER.debug("getCaseRepeatList()", "parameters : caseCategory =" + caseCategory);

		String schema = this.getMySchema();
		try{
			SqlStatement  sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("info.CASE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("info.DTID", SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("handelInfo.CASE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.REPEAT_CASE_ID.getValue());
			if (StringUtils.hasText(caseCategory)) {
				sqlStatement.addSelectClause("handelInfo.COMPLETE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue());
			}
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(".SRM_CASE_HANDLE_INFO info  INNER JOIN ").append(schema).append(".SRM_CASE_HANDLE_INFO handelInfo ON info.CASE_ID <> handelInfo.CASE_ID ");
			sqlStatement.addFromExpression(buffer.toString());
			if (StringUtils.hasText(caseId)) {
				sqlStatement.addWhereClause("info.CASE_ID in ( :caseId)");
			}
			sqlStatement.addWhereClause("info.DTID = handelInfo.DTID");
			//Task #3392 其他案件類別 建案-新增DTID欄位，非必填，有輸入依DTID帶出資料，可再修改
			sqlStatement.addWhereClause("info.CASE_CATEGORY <> 'OTHER'");
			sqlStatement.addWhereClause("handelInfo.CASE_CATEGORY <> 'OTHER'");
			sqlStatement.addWhereClause("handelInfo.CREATED_DATE < info.CREATED_DATE");
			if (!CollectionUtils.isEmpty(caseStatusList)) {
				sqlStatement.addWhereClause("handelInfo.CASE_STATUS not in ( :caseStatus)");
			}
			if (StringUtils.hasText(caseCategory)) {
				sqlStatement.addWhereClause("handelInfo.CASE_CATEGORY =:caseCategory");
			}
			if (StringUtils.hasText(dtid)) {
				sqlStatement.addWhereClause("info.DTID =:dtid");
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			//sqlQueryBean.setParameter("dtid", dtid);
			if (StringUtils.hasText(caseId)) {
				sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue(), StringUtils.toList(caseId, IAtomsConstants.MARK_SEPARATOR));
			}
			if (!CollectionUtils.isEmpty(caseStatusList)) {
				sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS.getValue(), caseStatusList);
			}
			if (StringUtils.hasText(caseCategory)) {
				sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue(), caseCategory);
			}
			if (StringUtils.hasText(dtid)) {
				sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue(), dtid);
			}
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseHandleInfoDTO.class);
			LOGGER.debug("getCaseRepeatList()", "sql:" + sqlQueryBean.toString());
			List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(caseHandleInfoDTOs)){
				return caseHandleInfoDTOs;
			}
		} catch (Exception e) {
			LOGGER.error("getCaseRepeatList()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		return null;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#getCountByInstall(java.lang.String, java.lang.String)
	 */
	@Override
	public int getCountByInstall(String dtid, String caseId, boolean isNew) throws DataAccessException {
		LOGGER.debug(".getCountByInstall()", "parameters:dtid:", dtid);
		LOGGER.debug(".getCountByInstall()", "parameters:caseId:", caseId);
		try {
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			Integer result = null;
			//查詢分頁最底下顯示的總條數
			sql.append("SELECT COUNT(1)");
			if(isNew) { 
				sql.append(" from ").append(schema).append(".SRM_CASE_NEW_HANDLE_INFO info ");
				LOGGER.debug(".getCountByInstall()", "parameters:isNew: true");
			} else {
				sql.append(" from ").append(schema).append(".SRM_CASE_HANDLE_INFO info ");
			}
			sql.append(" JOIN  ").append(schema).append(".SRM_CASE_HANDLE_INFO own on own.DTID = info.DTID ");
			sql.append(" where 1 = 1 ");
			//查詢條件 客戶id
			if(StringUtils.hasText(dtid)){
				sql.append(" AND info.DTID = :dtid ");
				sql.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue(), dtid);
			}
			//查詢條件 客戶id
			if(StringUtils.hasText(caseId)){
				sql.append(" AND own.CASE_ID = :caseId ");
				sql.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue(), caseId);
			}
			sql.append(" AND own.CREATED_DATE > info.CREATED_DATE ");
			if(!isNew) { 
				sql.append(" AND info.CASE_CATEGORY = :category");
				//作廢的也不讓過
				sql.append(" AND info.CASE_STATUS <> 'Voided'");
				sql.setParameter("category", IAtomsConstants.CASE_CATEGORY.INSTALL.getCode());
			}
			LOGGER.debug(".getCountByInstall()", " SQL---------->", sql.toString());
			List<Integer> list = this.getDaoSupport().findByNativeSql(sql);
			if(!CollectionUtils.isEmpty(list)){
				result = list.get(0);
			}
			return result;
		} catch (Exception e) {
			LOGGER.error(".getCountByInstall() is error", "DAO Exception", e);
			throw new DataAccessException(CoreMessageCode.QUERY_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.REB_IATOMS_TB_NAME_SUPPLIES)}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#listEdcReqCaseAnalysisMonReport(java.lang.String, java.util.Date, java.util.Date, java.lang.String)
	 */
	public List<SrmCaseHandleInfoDTO> listEdcReqCaseAnalysisMonReport(String customerId, Date startDate, Date endDate, String orderByCode)
			throws DataAccessException {
		List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = null;
		try {
			String schema = this.getMySchema();
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("select ");
			if (IAtomsConstants.ORDER_BY_CASE_CATEGORY.equals(orderByCode)) {
				sqlQueryBean.append("bpid.ITEM_NAME as ").append(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			}
			if (IAtomsConstants.ORDER_BY_CONTRACT_CODE.equals(orderByCode)) {
				sqlQueryBean.append("contract.CONTRACT_CODE").append(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			}
			if (IAtomsConstants.ORDER_BY_DEPT_CODE.equals(orderByCode)) {
				sqlQueryBean.append("case when tempTabble.deparamentId = :customerService then :customerServiceName ");
				sqlQueryBean.append("WHEN tempTabble.deparamentId = :QA THEN :QA ");
				sqlQueryBean.append("WHEN tempTabble.deparamentId = :TMS THEN :TMS ");
				sqlQueryBean.append("WHEN tempTabble.deparamentId = '' or tempTabble.deparamentId is null THEN :other ");
				sqlQueryBean.append("else deparament.DEPT_NAME end ").append(SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			}
			sqlQueryBean.append("sum(tempTabble.caseCount)").append(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_COUNT.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("sum(tempTabble.closeCount)").append(SrmCaseHandleInfoDTO.ATTRIBUTE.CLOSE_COUNT.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("sum(tempTabble.responseCount)").append(SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_COUNT.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("case sum(tempTabble.closeCount) when 0 then '0.00%' else ");
			sqlQueryBean.append("ltrim(Convert(numeric(9,2),sum(tempTabble.responseCount)*100.0/sum(tempTabble.closeCount)))+'%' end").append(SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_PER.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("sum(tempTabble.finishCount)").append(SrmCaseHandleInfoDTO.ATTRIBUTE.FINISH_COUNT.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("case sum(tempTabble.closeCount) when 0 then '0.00%' else ");
			sqlQueryBean.append("ltrim(Convert(numeric(9,2),sum(tempTabble.finishCount)*100.0/sum(tempTabble.closeCount)))+'%' end ").append(SrmCaseHandleInfoDTO.ATTRIBUTE.FINISH_PER.getValue());
			sqlQueryBean.append("from(");
			sqlQueryBean.append("select ");
			if (IAtomsConstants.ORDER_BY_CASE_CATEGORY.equals(orderByCode)) {
				sqlQueryBean.append("history.CASE_CATEGORY AS caseCategory").append(IAtomsConstants.MARK_SEPARATOR);
			}
			if (IAtomsConstants.ORDER_BY_CONTRACT_CODE.equals(orderByCode)) {
				sqlQueryBean.append("link.CONTRACT_ID as contractId").append(IAtomsConstants.MARK_SEPARATOR);
			}
			if (IAtomsConstants.ORDER_BY_DEPT_CODE.equals(orderByCode)) {
				sqlQueryBean.append("history.DISPATCH_DEPT_ID AS deparamentId").append(IAtomsConstants.MARK_SEPARATOR);
			}
			sqlQueryBean.append("0 as caseCount").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("COUNT(1) as closeCount").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("sum(case ");
			sqlQueryBean.append("WHEN history.RESPONSE_DATE is not null and CONVERT(varchar(100), history.ACCEPTABLE_RESPONSE_DATE, 121) < CONVERT(varchar(100), history.RESPONSE_DATE, 121) THEN 1 ");
			sqlQueryBean.append("WHEN history.RESPONSE_DATE is null and CONVERT(varchar(100), history.ACCEPTABLE_RESPONSE_DATE, 121) < CONVERT(varchar(100), GETDATE(), 121) THEN 1 ELSE 0 END) AS responseCount").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("sum(case when history.ACCEPTABLE_FINISH_DATE < history.COMPLETE_DATE then 1 else 0 end) as finishCount ");
			/*sqlQueryBean.append("from ").append(schema).append(".SRM_HISTORY_CASE_HANDLE_INFO history");*/
			sqlQueryBean.append("from ").append(schema).append(".SRM_CASE_HANDLE_INFO history ");
			if (IAtomsConstants.ORDER_BY_CONTRACT_CODE.equals(orderByCode)) {
				sqlQueryBean.append("left join ").append(schema).append(".SRM_CASE_ASSET_LINK link on link.CASE_ID = history.CASE_ID and link.ITEM_TYPE = '10' and isnull(link.IS_LINK,'N') = :isLink");
			}
			sqlQueryBean.append("where history.CLOSE_DATE >= :startDate");
			sqlQueryBean.append(" AND history.CLOSE_DATE < :endDate");
			sqlQueryBean.append(" AND history.CUSTOMER_ID = :customerId");
			//sqlQueryBean.append(" AND history.CASE_CATEGORY = :caseCategory");
			
			sqlQueryBean.append(" AND history.CASE_CATEGORY = :caseCategory");
			sqlQueryBean.append(" AND (history.CASE_STATUS='Closed' OR history.CASE_STATUS='ImmediateClose')");
			if (IAtomsConstants.ORDER_BY_CASE_CATEGORY.equals(orderByCode)) {
				sqlQueryBean.append(" group by history.CASE_CATEGORY");
			}
			if (IAtomsConstants.ORDER_BY_CONTRACT_CODE.equals(orderByCode)) {
				sqlQueryBean.append(" group by link.CONTRACT_ID");
			}
			if (IAtomsConstants.ORDER_BY_DEPT_CODE.equals(orderByCode)) {
				sqlQueryBean.append(" group by history.DISPATCH_DEPT_ID");
			}
			sqlQueryBean.append(" UNION ALL ");
			//--獲取案件處理表的進件數
			sqlQueryBean.append("select ");
			if (IAtomsConstants.ORDER_BY_CASE_CATEGORY.equals(orderByCode)) {
				sqlQueryBean.append("schi.CASE_CATEGORY AS caseCategory").append(IAtomsConstants.MARK_SEPARATOR);
			}
			if (IAtomsConstants.ORDER_BY_CONTRACT_CODE.equals(orderByCode)) {
				sqlQueryBean.append("link.CONTRACT_ID as contractId").append(IAtomsConstants.MARK_SEPARATOR);
			}
			if (IAtomsConstants.ORDER_BY_DEPT_CODE.equals(orderByCode)) {
				sqlQueryBean.append("schi.DISPATCH_DEPT_ID AS deparamentId").append(IAtomsConstants.MARK_SEPARATOR);
			}
			sqlQueryBean.append("COUNT(1) as caseCount").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("0 as closeCount").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("0 as responseCount").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("0 as finishCount ");
			sqlQueryBean.append("from ").append(schema).append(".SRM_CASE_HANDLE_INFO schi ");
			if (IAtomsConstants.ORDER_BY_CONTRACT_CODE.equals(orderByCode)) {
				sqlQueryBean.append("left join ").append(schema).append(".SRM_CASE_ASSET_LINK link on link.CASE_ID = schi.CASE_ID and link.ITEM_TYPE = '10' and isnull(link.IS_LINK,'N') = :isLink");
			}
			sqlQueryBean.append("where ");
			sqlQueryBean.append("schi.CREATED_DATE >= :startDate ");
			sqlQueryBean.append(" AND schi.CREATED_DATE < :endDate");
			sqlQueryBean.append(" AND schi.CUSTOMER_ID = :customerId");
			sqlQueryBean.append(" AND schi.CASE_CATEGORY = :caseCategory");
			sqlQueryBean.append(" AND schi.CASE_STATUS <> 'Voided' ");
			if (IAtomsConstants.ORDER_BY_CASE_CATEGORY.equals(orderByCode)) {
				sqlQueryBean.append(" group by schi.CASE_CATEGORY");
			}
			if (IAtomsConstants.ORDER_BY_CONTRACT_CODE.equals(orderByCode)) {
				sqlQueryBean.append(" group by link.CONTRACT_ID");
			}
			if (IAtomsConstants.ORDER_BY_DEPT_CODE.equals(orderByCode)) {
				sqlQueryBean.append(" group by schi.DISPATCH_DEPT_ID");
			}
			sqlQueryBean.append(") tempTabble ");
			if (IAtomsConstants.ORDER_BY_CASE_CATEGORY.equals(orderByCode)) {
				sqlQueryBean.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid ON tempTabble.caseCategory = bpid.ITEM_VALUE AND bpid.BPTD_CODE = :ticketType ");
				sqlQueryBean.append(" group by bpid.ITEM_NAME");
				sqlQueryBean.setParameter("ticketType", IATOMS_PARAM_TYPE.TICKET_TYPE.getCode());
			}
			if (IAtomsConstants.ORDER_BY_CONTRACT_CODE.equals(orderByCode)) {
				sqlQueryBean.append("left join ").append(schema).append(".BIM_CONTRACT contract on contract.CONTRACT_ID = tempTabble.contractId");
				sqlQueryBean.append(" group by contract.CONTRACT_CODE");
			}
			if (IAtomsConstants.ORDER_BY_DEPT_CODE.equals(orderByCode)) {
				sqlQueryBean.append("left join ").append(schema).append(".BIM_DEPARTMENT deparament on deparament.DEPT_CODE = tempTabble.deparamentId");
				sqlQueryBean.append(" group by tempTabble.deparamentId,deparament.DEPT_NAME");
				sqlQueryBean.append(" order by ").append(SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_NAME.getValue());
				sqlQueryBean.setParameter("customerService", IAtomsConstants.CASE_ROLE.CUSTOMER_SERVICE.getCode());
				sqlQueryBean.setParameter("customerServiceName", i18NUtil.getName(IAtomsConstants.FIELD_CASE_ROLE_CUSTOMER_SERVICE));
				sqlQueryBean.setParameter(IAtomsConstants.CASE_ROLE.QA.getCode(), IAtomsConstants.CASE_ROLE.QA.getCode());
				sqlQueryBean.setParameter(IAtomsConstants.CASE_ROLE.TMS.getCode(), IAtomsConstants.CASE_ROLE.TMS.getCode());
				sqlQueryBean.setParameter("other", i18NUtil.getName(IAtomsConstants.IS_NOT_DISPATCHING_TO_DEPTMENT));
			}
			sqlQueryBean.setParameter("startDate", startDate);
			sqlQueryBean.setParameter("endDate", endDate);
			sqlQueryBean.setParameter("caseCategory", IAtomsConstants.TICKET_TYPE_REPAIR);
			sqlQueryBean.setParameter("customerId", customerId);
			AliasBean aliasBean = new AliasBean(SrmCaseHandleInfoDTO.class);
			if (IAtomsConstants.ORDER_BY_CASE_CATEGORY.equals(orderByCode)) {
				aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY_NAME.getValue(), StringType.INSTANCE);
			}
			if (IAtomsConstants.ORDER_BY_CONTRACT_CODE.equals(orderByCode)) {
				aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue(), StringType.INSTANCE);
				sqlQueryBean.setParameter(SrmCaseAssetLinkDTO.ATTRIBUTE.IS_LINK.getValue(), IAtomsConstants.YES);
			}
			if (IAtomsConstants.ORDER_BY_DEPT_CODE.equals(orderByCode)) {
				aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_NAME.getValue(), StringType.INSTANCE);
			}
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_COUNT.getValue(), IntegerType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CLOSE_COUNT.getValue(), IntegerType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_COUNT.getValue(), IntegerType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.FINISH_COUNT.getValue(), IntegerType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_PER.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.FINISH_PER.getValue(), StringType.INSTANCE);
			LOGGER.debug("getCaseInfoById()", "sql:" + sqlQueryBean.toString());
			caseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listEdcReqCaseAnalysisMonReport()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[]{i18NUtil.getName(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY)}, e);
		}
		return caseHandleInfoDTOs;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#listUnfinishedReportToTCB(java.lang.String, java.util.Date, java.util.List, java.util.List)
	 */
	public List<SrmCaseHandleInfoDTO> listUnfinishedReportToTCB(String customerId, Date expectedCompletionDate,
			List<String> caseCategoryArray, List<String> caseStatusArray, Boolean isPublic, List<String> installedCaseCategorys) throws DataAccessException {
		List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = null;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("bpid1.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY_NAME.getValue());
			sqlStatement.addSelectClause("bpid2.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS_NAME.getValue());
			sqlStatement.addSelectClause("bm.MERCHANT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("bm.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue());
			//sqlStatement.addSelectClause("CONCAT( CONVERT( VARCHAR( 100 ) ,bpid3.ITEM_NAME ,111 ) ,' ' ,CONVERT( VARCHAR( 200 ) ,schi.INSTALLED_ADRESS ,111 ) )", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue());
			if (isPublic){
				StringBuffer whereBuffer = new StringBuffer();
				whereBuffer.append("case when schi.CASE_CATEGORY in ( :installedCaseCategorys ) then ");
				whereBuffer.append("CONCAT(CONVERT( VARCHAR( 100 ) ,bpid3.ITEM_NAME ,111 ) ,' ' ,CONVERT( VARCHAR( 200 ) ,schi.INSTALLED_ADRESS ,111 )) ");
				whereBuffer.append("else ");
				whereBuffer.append("CONCAT( CONVERT( VARCHAR( 100 ) ,bpid4.ITEM_NAME ,111 ) ,' ' ,CONVERT( VARCHAR( 200 ) ,schi.CONTACT_ADDRESS ,111 ) ) END  ");
				sqlStatement.addSelectClause(whereBuffer.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue());
			} else {
				sqlStatement.addSelectClause("CONCAT( CONVERT( VARCHAR( 100 ) ,bpid3.ITEM_NAME ,111 ) ,' ' ,CONVERT( VARCHAR( 200 ) ,schi.INSTALLED_ADRESS ,111 ) )", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue());
			}
			sqlStatement.addSelectClause("schi.DTID", SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("mhi.AO_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.AO_NAME.getValue());
			sqlStatement.addSelectClause("schi.CREATED_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sqlStatement.addSelectClause("mhi.AO_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.AO_NAME.getValue());
			sqlStatement.addSelectClause("schi.ACCEPTABLE_FINISH_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.EXPECTED_COMPLETION_DATE.getValue());
			if (!isPublic) {
				sqlStatement.addSelectClause("schi.REQUIREMENT_NO", SrmCaseHandleInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue());
				sqlStatement.addSelectClause("schi.DESCRIPTION", SrmCaseHandleInfoDTO.ATTRIBUTE.DESCRIPTION.getValue());
				sqlStatement.addSelectClause("sct.DEAL_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.DEAL_DATE.getValue());
				sqlStatement.addSelectClause("sct.DESCRIPTION", SrmCaseHandleInfoDTO.ATTRIBUTE.TRANSACTION_DESCRIPTION.getValue());
				sqlStatement.addSelectClause("schi.CASE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue());
			}
			StringBuffer fromBuffer = new StringBuffer();
			if (isPublic) {
				fromBuffer.append(schema).append(".SRM_CASE_HANDLE_INFO schi ");
				fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid4 ON bpid4.ITEM_VALUE = schi.CONTACT_ADDRESS_LOCATION AND bpid4.BPTD_CODE = :location ");
			} else {
				fromBuffer.append(schema).append(".SRM_CASE_TRANSACTION sct ");
				fromBuffer.append("left join ").append(schema).append(".SRM_CASE_HANDLE_INFO schi ON schi.CASE_ID = sct.CASE_ID ");
			}
			
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid1 ON bpid1.ITEM_VALUE = schi.CASE_CATEGORY AND bpid1.BPTD_CODE = :ticketType ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid2 ON bpid2.ITEM_VALUE = schi.CASE_STATUS AND bpid2.BPTD_CODE = :caseStatus ");
			fromBuffer.append("left join ").append(schema).append(".BIM_MERCHANT bm ON bm.COMPANY_ID = schi.CUSTOMER_ID AND bm.MERCHANT_ID = schi.MERCHANT_CODE ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid3 ON bpid3.ITEM_VALUE = schi.INSTALLED_ADRESS_LOCATION AND bpid3.BPTD_CODE = :location ");
			fromBuffer.append("left join ").append(schema).append(".BIM_MERCHANT_HEADER mhi ON mhi.MERCHANT_HEADER_ID = schi.MERCHANT_HEADER_ID");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(customerId)) {
				sqlStatement.addWhereClause("schi.CUSTOMER_ID = :customerId", customerId);
			}
			if (!CollectionUtils.isEmpty(caseCategoryArray)) {
				sqlStatement.addWhereClause("schi.CASE_CATEGORY not in ( :caseCategory )");
			}
			if (!CollectionUtils.isEmpty(caseStatusArray)) {
				sqlStatement.addWhereClause("schi.CASE_STATUS not in ( :caseStatusArray )");
			}
			if (!isPublic) {
				sqlStatement.addWhereClause("sct.DESCRIPTION IS NOT NULL AND sct.DESCRIPTION <>''");
			}
			
			if (expectedCompletionDate != null) {
				sqlStatement.addWhereClause("schi.ACCEPTABLE_FINISH_DATE < :expectedCompletionDate", expectedCompletionDate);
			}
			
			if (isPublic) {
				sqlStatement.setOrderByExpression("schi.CREATED_DATE");
			} else {
				sqlStatement.setOrderByExpression("schi.CREATED_DATE, sct.DEAL_DATE ");
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("ticketType", IATOMS_PARAM_TYPE.TICKET_TYPE.getCode());
			sqlQueryBean.setParameter("caseStatus", IATOMS_PARAM_TYPE.CASE_STATUS.getCode());
			sqlQueryBean.setParameter("location", IATOMS_PARAM_TYPE.LOCATION.getCode());
			sqlQueryBean.setParameter("caseCategory", caseCategoryArray);
			sqlQueryBean.setParameter("caseStatusArray", caseStatusArray);
			if (isPublic) {
				sqlQueryBean.setParameter("installedCaseCategorys", installedCaseCategorys);
			}
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseHandleInfoDTO.class);
			LOGGER.debug("listUnfinishedReportToTCB()", "sql:" + sqlQueryBean.toString());
			caseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listUnfinishedReportToTCB()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return caseHandleInfoDTOs;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#getProblemReasonList()
	 */
	@Override
	public List<Parameter> getProblemReasonList(boolean isTSB) throws DataAccessException {
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			StringBuffer buffer = new StringBuffer();
			sqlStatement.addSelectClause("problemReason.BPTD_CODE + '-' + problemReason.ITEM_VALUE", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("problemReasonType.PT_NAME + '-' + problemReason.ITEM_NAME", Parameter.FIELD_NAME);
			buffer.append(schema).append(".BASE_PARAMETER_ITEM_DEF problemReason");
			buffer.append(" left join ").append(schema).append(".BASE_PARAMETER_TYPE_DEF problemReasonType on problemReason.BPTD_CODE = problemReasonType.BPTD_CODE  ");
			sqlStatement.addFromExpression(buffer.toString());
		//	sqlStatement.addWhereClause("problemReasonType.VALUE_SCOPE_OPERATOR2 =:problemReasonCategory and problemReason.APPROVED_FLAG = :approvedFlag ");
			// 處理approvedFlag
			sqlStatement.addWhereClause("problemReasonType.VALUE_SCOPE_OPERATOR2 =:problemReasonCategory and isnull(problemReason.DELETED, 'N') = 'N' ");
			if (isTSB) {
				sqlStatement.addWhereClause("problemReason.TEXT_FIELD1 = :tsb", IAtomsConstants.PARAM_TSB_EDC);
			} else {
				sqlStatement.addWhereClause("problemReason.TEXT_FIELD1 is null");
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("problemReasonCategory", IATOMS_PARAM_TYPE.PROBLEM_REASON_CATEGORY.getCode());
			// 處理approvedFlag
		//	sqlQueryBean.setParameter("approvedFlag", IAtomsConstants.YES);
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			LOGGER.debug("getProblemReasonList()", "sql:", sqlQueryBean.toString());
			List<Parameter> problemReasonList = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return problemReasonList;
		} catch (Exception e) {
			LOGGER.error(":getProblemReasonList() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#getProblemSolutionList()
	 */
	@Override
	public List<Parameter> getProblemSolutionList(boolean isTSB) throws DataAccessException {
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			StringBuffer buffer = new StringBuffer();
			sqlStatement.addSelectClause("problemSolution.BPTD_CODE + '-' + problemSolution.ITEM_VALUE", Parameter.FIELD_VALUE);
			sqlStatement.addSelectClause("problemSolutionType.PT_NAME + '-' + problemSolution.ITEM_NAME", Parameter.FIELD_NAME);
			buffer.append(schema).append(".BASE_PARAMETER_ITEM_DEF problemSolution");
			buffer.append(" left join ").append(schema).append(".BASE_PARAMETER_TYPE_DEF problemSolutionType on problemSolution.BPTD_CODE = problemSolutionType.BPTD_CODE  ");
			sqlStatement.addFromExpression(buffer.toString());
		//	sqlStatement.addWhereClause("problemSolutionType.VALUE_SCOPE_OPERATOR2 = :problemSolutionCategory and problemSolution.APPROVED_FLAG = :approvedFlag ");
			// 處理approvedFlag
			sqlStatement.addWhereClause("problemSolutionType.VALUE_SCOPE_OPERATOR2 = :problemSolutionCategory and isnull(problemSolution.DELETED, 'N') = 'N' ");
			if (isTSB) {
				sqlStatement.addWhereClause("problemSolution.TEXT_FIELD1 = :tsb", IAtomsConstants.PARAM_TSB_EDC);
			} else {
				sqlStatement.addWhereClause("problemSolution.TEXT_FIELD1 is null");
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("problemSolutionCategory", IATOMS_PARAM_TYPE.PROBLEM_SOLUTION_CATEGORY.getCode());
			// 處理approvedFlag
		//	sqlQueryBean.setParameter("approvedFlag", IAtomsConstants.YES);
			AliasBean aliasBean = sqlStatement.createAliasBean(Parameter.class);
			LOGGER.debug("getProblemSolutionList()", "sql:", sqlQueryBean.toString());
			List<Parameter> problemSolutionList = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			return problemSolutionList;
		} catch (Exception e) {
			LOGGER.error(":getProblemSolutionList() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#listFinishedReportToGP(java.lang.String, java.util.Date, java.util.List, java.util.List)
	 */
	public List<SrmCaseHandleInfoDTO> listFinishedReportToGP(String customerId,
			String completeDate, List<String> caseCategoryArray, List<String> caseStatusArray) throws DataAccessException {
		List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOs = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("schi.REQUIREMENT_NO", SrmCaseHandleInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue());
			sqlStatement.addSelectClause("schi.DTID", SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("CASE WHEN schi.OLD_MERCHANT_CODE IS NOT NULL AND schi.OLD_MERCHANT_CODE <> '' THEN schi.OLD_MERCHANT_CODE "
					+ "ELSE bm.MERCHANT_CODE END", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("schi.COMPLETE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue());
			sqlStatement.addSelectClause("assetLink.SERIAL_NUMBER", SrmCaseHandleInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("schi.CASE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".SRM_CASE_HANDLE_INFO schi ");
			fromBuffer.append("left join ").append(schema).append(".BIM_MERCHANT bm ON bm.MERCHANT_ID = schi.MERCHANT_CODE ");
			fromBuffer.append("left join ").append(schema).append(".SRM_CASE_ASSET_LINK assetLink ON assetLink.CASE_ID = schi.CASE_ID ");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(customerId)) {
				sqlStatement.addWhereClause("schi.CUSTOMER_ID = :customerId", customerId);
			}
			/*if (StringUtils.hasText(completeDate)) {
				sqlStatement.addWhereClause("CONVERT(VARCHAR(12) ,schi.COMPLETE_DATE ,111) = :completeDate", completeDate);
			}*/
			if (!CollectionUtils.isEmpty(caseCategoryArray)) {
				sqlStatement.addWhereClause("schi.CASE_CATEGORY IN( :caseCategory )");
			}
			if (!CollectionUtils.isEmpty(caseStatusArray)) {
				sqlStatement.addWhereClause("schi.CASE_STATUS IN( :caseStatus )");
			}
			sqlStatement.addWhereClause("assetLink.ITEM_CATEGORY = :edc", IAtomsConstants.ASSET_CATEGORY_EDC);
			//Bug #2443 查詢條件之中的完修時間 改為 執行完修時 案件歷程的創建時間 update by 2017/09/19
			if (StringUtils.hasText(completeDate)) {
					fromBuffer = new StringBuffer();
					fromBuffer.append("schi.CASE_ID in ( ");
					fromBuffer.append("select trans.CASE_ID from ");
					fromBuffer.append(schema).append(".SRM_CASE_TRANSACTION trans where ");
					fromBuffer.append("trans.ACTION_ID in ('complete', 'onlineExclusion', 'immediatelyClosing') and CONVERT( VARCHAR(12) ,trans.CREATED_DATE ,111) = :completeDate) ");
					sqlStatement.addWhereClause(fromBuffer.toString());
			}
			sqlStatement.setOrderByExpression("schi.REQUIREMENT_NO");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if (StringUtils.hasText(completeDate)) {
				sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue(), completeDate);
			}
			if (!CollectionUtils.isEmpty(caseCategoryArray)) {
				sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue(), caseCategoryArray);
			}
			if (!CollectionUtils.isEmpty(caseStatusArray)) {
				sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS.getValue(), caseStatusArray);
			}
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseHandleInfoDTO.class);
			/*fromBuffer.append("(");
			fromBuffer.append("SELECT ");
			fromBuffer.append("schi.CASE_ID").append(IAtomsConstants.MARK_SEPARATOR);
			fromBuffer.append("schi.REQUIREMENT_NO").append(IAtomsConstants.MARK_SEPARATOR);
			fromBuffer.append("assetLink.SERIAL_NUMBER").append(IAtomsConstants.MARK_SEPARATOR);
			fromBuffer.append("(case when schi.OLD_MERCHANT_CODE IS NOT NULL and schi.OLD_MERCHANT_CODE <> '' THEN schi.OLD_MERCHANT_CODE ELSE bm.MERCHANT_CODE END) as ").append(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			fromBuffer.append("schi.DTID").append(IAtomsConstants.MARK_SEPARATOR);
			fromBuffer.append("schi.COMPLETE_DATE ");
			fromBuffer.append("from ").append(schema).append(".SRM_CASE_HANDLE_INFO schi ");
			fromBuffer.append("left join ").append(schema).append(".BIM_MERCHANT bm ON bm.MERCHANT_ID = schi.MERCHANT_CODE ");
			fromBuffer.append("left join ").append(schema).append(".SRM_CASE_ASSET_LINK assetLink ON assetLink.CASE_ID = schi.CASE_ID ");
			fromBuffer.append("where ");
			fromBuffer.append("schi.CUSTOMER_ID = :customerId ");
			fromBuffer.append("AND CONVERT(varchar(12), schi.COMPLETE_DATE, 111) = :completeDate ");
			fromBuffer.append("AND schi.CASE_CATEGORY IN ( :caseCategory ) ");
			fromBuffer.append("AND schi.CASE_STATUS IN ( :caseStatus ) ");
			fromBuffer.append("AND assetLink.ITEM_CATEGORY = :assetCategory ");
			fromBuffer.append("UNION ALL ");
			
			fromBuffer.append("SELECT ");
			fromBuffer.append("shchi.CASE_ID").append(IAtomsConstants.MARK_SEPARATOR);
			fromBuffer.append("shchi.REQUIREMENT_NO").append(IAtomsConstants.MARK_SEPARATOR);
			fromBuffer.append("histCaseLink.SERIAL_NUMBER").append(IAtomsConstants.MARK_SEPARATOR);
			fromBuffer.append("(case when shchi.OLD_MERCHANT_CODE IS NOT NULL and shchi.OLD_MERCHANT_CODE <> '' THEN shchi.OLD_MERCHANT_CODE ELSE bm.MERCHANT_CODE END) as ").append(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			fromBuffer.append("shchi.DTID").append(IAtomsConstants.MARK_SEPARATOR);
			fromBuffer.append("shchi.COMPLETE_DATE ");
			fromBuffer.append("from ").append(schema).append(".SRM_HISTORY_CASE_HANDLE_INFO shchi ");
			fromBuffer.append("left join ").append(schema).append(".BIM_MERCHANT bm ON bm.MERCHANT_ID = shchi.MERCHANT_CODE ");
			fromBuffer.append("left join ").append(schema).append(".SRM_HISTORY_CASE_ASSET_LINK histCaseLink ON histCaseLink.CASE_ID = shchi.CASE_ID  ");
			fromBuffer.append("where ");
			fromBuffer.append("shchi.CUSTOMER_ID = :customerId ");
			fromBuffer.append("AND CONVERT(varchar(12), shchi.COMPLETE_DATE, 111) = :completeDate ");
			fromBuffer.append("AND shchi.CASE_CATEGORY IN ( :caseCategory ) ");
			fromBuffer.append("AND shchi.CASE_STATUS IN ( :caseStatus ) ");
			fromBuffer.append("AND histCaseLink.ITEM_CATEGORY = :assetCategory ");
			fromBuffer.append(") a ");
			sqlStatement.addFromExpression(fromBuffer.toString());
			sqlStatement.setOrderByExpression("a.REQUIREMENT_NO");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseHandleInfoDTO.class);
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue(), completeDate);
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue(), caseCategoryArray);
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS.getValue(), caseStatusArray);
			sqlQueryBean.setParameter(AssetTypeDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), IAtomsConstants.ASSET_CATEGORY_EDC);*/
			LOGGER.debug("listFinishedReportToGP()", "sql:", sqlQueryBean.toString());
			srmCaseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(":listFinishedReportToGP() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return srmCaseHandleInfoDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#listFinishedReportToTCB(java.lang.String, java.lang.String, java.util.List, java.util.List)
	 */
	public List<SrmCaseHandleInfoDTO> listFinishedReportToTCB(String customerId,
			String completeDate, List<String> caseCategoryArray, List<String> caseStatusArray) throws DataAccessException {
		List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOs = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("a.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue());
			sqlStatement.addSelectClause("a.MERCHANT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("a.DTID", SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("a.COMPLETE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue());
			sqlStatement.addSelectClause("a.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY_NAME.getValue());
			sqlStatement.addSelectClause("a.CASE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			//fromBuffer.append("select *");
			fromBuffer.append("( SELECT ");
			fromBuffer.append("schi.CASE_ID").append(IAtomsConstants.MARK_SEPARATOR);
			fromBuffer.append("mer.NAME").append(IAtomsConstants.MARK_SEPARATOR);
			fromBuffer.append("bpid1.ITEM_NAME").append(IAtomsConstants.MARK_SEPARATOR);
			fromBuffer.append("mer.MERCHANT_CODE ").append(IAtomsConstants.MARK_SEPARATOR);
			fromBuffer.append("schi.DTID").append(IAtomsConstants.MARK_SEPARATOR);
			fromBuffer.append("schi.COMPLETE_DATE ");
			fromBuffer.append("from ").append(schema).append(".SRM_CASE_HANDLE_INFO schi ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid1 ON bpid1.ITEM_VALUE = schi.CASE_CATEGORY AND bpid1.BPTD_CODE = :ticket_type ");
			fromBuffer.append("left join ").append(schema).append(".BIM_MERCHANT mer ON mer.COMPANY_ID = schi.CUSTOMER_ID AND mer.MERCHANT_ID = schi.MERCHANT_CODE ");
			fromBuffer.append("where ");
			fromBuffer.append("schi.CUSTOMER_ID = :customerId ");
			fromBuffer.append("and schi.CASE_ID in ( ");
			fromBuffer.append("select trans.CASE_ID from ");
			fromBuffer.append(schema).append(".SRM_CASE_TRANSACTION trans where ");
			fromBuffer.append("trans.ACTION_ID in ('complete', 'onlineExclusion','immediatelyClosing') and CONVERT( VARCHAR(12) ,trans.CREATED_DATE ,111) = :completeDate) ");
			/*fromBuffer.append("AND CONVERT(varchar(12), schi.COMPLETE_DATE, 111) = :completeDate ");*/
			fromBuffer.append("AND schi.CASE_CATEGORY IN ( :caseCategory ) ");
			// Task #3113 完修可以退回客服 放開注釋內容
			fromBuffer.append("AND schi.CASE_STATUS IN ( :caseStatus ) ");
			/*fromBuffer.append("UNION ALL ");
			
			fromBuffer.append("SELECT ");
			fromBuffer.append("shchi.CASE_ID").append(IAtomsConstants.MARK_SEPARATOR);
			fromBuffer.append(" mer.NAME").append(IAtomsConstants.MARK_SEPARATOR);
			fromBuffer.append("bpid1.ITEM_NAME").append(IAtomsConstants.MARK_SEPARATOR);
			fromBuffer.append("mer.MERCHANT_CODE ").append(IAtomsConstants.MARK_SEPARATOR);
			fromBuffer.append("shchi.DTID").append(IAtomsConstants.MARK_SEPARATOR);
			fromBuffer.append("shchi.COMPLETE_DATE ");
			fromBuffer.append("from ").append(schema).append(".SRM_HISTORY_CASE_HANDLE_INFO shchi ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid1 ON bpid1.ITEM_VALUE = shchi.CASE_CATEGORY AND bpid1.BPTD_CODE = :ticket_type ");
			fromBuffer.append("left join ").append(schema).append(".BIM_MERCHANT mer ON mer.COMPANY_ID = shchi.CUSTOMER_ID AND mer.MERCHANT_ID = shchi.MERCHANT_CODE ");
			fromBuffer.append("where ");
			fromBuffer.append("shchi.CUSTOMER_ID = :customerId ");
			fromBuffer.append("AND CONVERT(varchar(12), shchi.COMPLETE_DATE, 111) = :completeDate ");
			fromBuffer.append("AND shchi.CASE_CATEGORY IN ( :caseCategory ) ");
			fromBuffer.append("AND shchi.CASE_STATUS IN ( :caseStatus ) ");*/
			fromBuffer.append(") a ");
			sqlStatement.addFromExpression(fromBuffer.toString());
			sqlStatement.setOrderByExpression("a.CASE_ID");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseHandleInfoDTO.class);
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue(), completeDate);
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue(), caseCategoryArray);
			// Task #3113 完修可以退回客服 放開注釋內容
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS.getValue(), caseStatusArray);
			sqlQueryBean.setParameter("ticket_type", IATOMS_PARAM_TYPE.TICKET_TYPE.getCode());
			LOGGER.debug("listFinishedReportToGP()", "sql:", sqlQueryBean.toString());
			srmCaseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(":listFinishedReportToGP() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return srmCaseHandleInfoDTOs;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#copyToHis(java.lang.String)
	 */
	@Override
	public void copyToHis(String caseId, String ditd, String isToNew, String isCloseCopy) throws DataAccessException {
		LOGGER.debug("copyToHis()", "parameter:caseId=", caseId);
		LOGGER.debug("copyToHis()", "parameter:ditd=", ditd);
		LOGGER.debug("copyToHis()", "parameter:isToNew=", isToNew);
		LOGGER.debug("copyToHis()", "parameter:isCloseCopy=", isCloseCopy);
		try {
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("exec usp_Copy_Case_Handle_Info :caseId, :ditd, :isToNew, :isCloseCopy");
			sqlQueryBean.setParameter("caseId", caseId);
			sqlQueryBean.setParameter("ditd", ditd);
			sqlQueryBean.setParameter("isToNew", isToNew);
			sqlQueryBean.setParameter("isCloseCopy", isCloseCopy);
			LOGGER.debug("copyToHis()", "sql---->", sqlQueryBean.toString() );
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("copyToHis() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
	}
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#moveCaseToHistroy(java.util.Date)
	 */
	@Override
	public void moveCaseToHistroy(Date moveDate) throws DataAccessException {
		LOGGER.debug("moveCaseToHistroy", "parameter:moveDate=", moveDate.toString());
		try {
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("exec usp_Move_To_History_Case_Handle_Info :startDate");
			sqlQueryBean.setParameter("startDate", moveDate);
			LOGGER.debug("moveCaseToHistroy", "sql---->", sqlQueryBean.toString() );
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("moveCaseToHistroy() do PROCEDURE usp_Move_To_History_Case_Handle_Info is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
	}
		
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#listCompleteOverdueRate(java.lang.String, java.util.Date, java.util.Date)
	 */
	public List<SrmCaseHandleInfoDTO> listCompleteOverdueRate(String customerId, Date startDate, Date endDate) throws DataAccessException {
		//TODO
		List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = null;
		LOGGER.debug("listCompleteOverdueRate()", "parameter:customerId=", customerId);
		LOGGER.debug("listCompleteOverdueRate()", "parameter:startDate=", startDate.toString());
		LOGGER.debug("listCompleteOverdueRate()", "parameter:endDate=", endDate.toString());
		try{
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("select ");
			sqlQueryBean.append("shchi.* into #HISTORY_TEMP ");
			sqlQueryBean.append("FROM ").append(schema).append(".SRM_CASE_HANDLE_INFO shchi ");
			sqlQueryBean.append("where ");
			sqlQueryBean.append("shchi.CUSTOMER_ID = :customerId ");
			sqlQueryBean.append("and shchi.CLOSE_DATE >= :startDate ");
			sqlQueryBean.append("and shchi.CLOSE_DATE < :endDate ");
			sqlQueryBean.append("and shchi.CASE_CATEGORY = :caseCategory ");
			sqlQueryBean.append("and (shchi.CASE_STATUS = :closed OR shchi.CASE_STATUS = :immediatelyClosing); ");
			sqlQueryBean.append("SELECT ");
			sqlQueryBean.append("t1.DISPATCH_DEPT_ID as ").append(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_ID.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("case when t1.DISPATCH_DEPT_ID = :customerService then :customerServiceName ");
			sqlQueryBean.append("WHEN t1.DISPATCH_DEPT_ID = :QA THEN :QA ");
			sqlQueryBean.append("WHEN t1.DISPATCH_DEPT_ID = :TMS THEN :TMS ");
			sqlQueryBean.append("WHEN t1.DISPATCH_DEPT_ID = '' or t1.DISPATCH_DEPT_ID is null THEN :other ");
			sqlQueryBean.append("else dept.DEPT_NAME end ").append(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("sum(t1.caseCount) as").append(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_COUNT.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("sum(t1.hisCloseCaseCount) as ").append(SrmCaseHandleInfoDTO.ATTRIBUTE.CLOSE_COUNT.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("sum(t1.overdueCount) as ").append(SrmCaseHandleInfoDTO.ATTRIBUTE.FINISH_COUNT.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("case sum(t1.hisCloseCaseCount) when 0 then '0.00%' else");
			sqlQueryBean.append("ltrim(Convert(numeric(9,2),sum(t1.overdueCount)*100.0/sum(t1.hisCloseCaseCount)))+'%' end as ").append(SrmCaseHandleInfoDTO.ATTRIBUTE.FINISH_PER.getValue() );
			sqlQueryBean.append("FROM ( ");
			sqlQueryBean.append("SELECT ");
			sqlQueryBean.append("schi.DISPATCH_DEPT_ID").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("COUNT(schi.CASE_ID) as caseCount").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("0 as hisCloseCaseCount").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("0 as overdueCount ");
			sqlQueryBean.append("FROM ").append(schema).append(".SRM_CASE_HANDLE_INFO schi ");
			sqlQueryBean.append("WHERE ");
			sqlQueryBean.append("schi.CREATED_DATE >= :startDate ");
			sqlQueryBean.append("and schi.CREATED_DATE < :endDate ");
			sqlQueryBean.append("and schi.CUSTOMER_ID = :customerId ");
			sqlQueryBean.append("and schi.CASE_CATEGORY = :caseCategory ");
			sqlQueryBean.append("and schi.CASE_STATUS <> :voided ");
			sqlQueryBean.append("GROUP BY schi.DISPATCH_DEPT_ID ");
			sqlQueryBean.append("UNION ALL ");
			sqlQueryBean.append("SELECT ");
			sqlQueryBean.append("DISPATCH_DEPT_ID").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("0 as caseCount").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("0 as hisCloseCaseCount").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("sum(case when COMPLETE_DATE > ACCEPTABLE_FINISH_DATE then 1 else 0 end) as overdueCount ");
			sqlQueryBean.append("FROM ").append("#HISTORY_TEMP ");
			sqlQueryBean.append("GROUP BY DISPATCH_DEPT_ID ");
			sqlQueryBean.append("UNION ALL ");
			sqlQueryBean.append("SELECT ");
			sqlQueryBean.append("DISPATCH_DEPT_ID").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("0 as caseCount").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("COUNT(1) as hisCloseCaseCount").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("0 as overdueCount ");
			sqlQueryBean.append("FROM ").append("#HISTORY_TEMP ");
			sqlQueryBean.append("GROUP BY DISPATCH_DEPT_ID ");
			sqlQueryBean.append(") t1 left join ").append(schema).append(".BIM_DEPARTMENT dept on dept.DEPT_CODE = t1.DISPATCH_DEPT_ID ");
			sqlQueryBean.append("GROUP BY t1.DISPATCH_DEPT_ID, dept.DEPT_NAME ");
			sqlQueryBean.append("order BY ").append(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_NAME.getValue());
			sqlQueryBean.append("drop table #HISTORY_TEMP; ");
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
			sqlQueryBean.setParameter("startDate", startDate);
			sqlQueryBean.setParameter("endDate", endDate);
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue(), IAtomsConstants.CASE_CATEGORY.REPAIR.getCode());
			sqlQueryBean.setParameter("closed", IAtomsConstants.CASE_STATUS.CLOSED.getCode());
			sqlQueryBean.setParameter("immediatelyClosing", IAtomsConstants.CASE_STATUS.IMMEDIATE_CLOSE.getCode());
			sqlQueryBean.setParameter("voided", IAtomsConstants.CASE_STATUS.VOIDED.getCode());
			sqlQueryBean.setParameter("customerService", IAtomsConstants.CASE_ROLE.CUSTOMER_SERVICE.getCode());
			sqlQueryBean.setParameter("customerServiceName", i18NUtil.getName(IAtomsConstants.FIELD_CASE_ROLE_CUSTOMER_SERVICE));
			sqlQueryBean.setParameter(IAtomsConstants.CASE_ROLE.QA.getCode(), IAtomsConstants.CASE_ROLE.QA.getCode());
			sqlQueryBean.setParameter(IAtomsConstants.CASE_ROLE.TMS.getCode(), IAtomsConstants.CASE_ROLE.TMS.getCode());
			sqlQueryBean.setParameter("other", i18NUtil.getName(IAtomsConstants.IS_NOT_DISPATCHING_TO_DEPTMENT));
			AliasBean aliasBean = new AliasBean(SrmCaseHandleInfoDTO.class);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_COUNT.getValue(), IntegerType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CLOSE_COUNT.getValue(), IntegerType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.FINISH_COUNT.getValue(), IntegerType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.FINISH_PER.getValue(), StringType.INSTANCE);
			LOGGER.debug("listCreateCaseToOverdueReport()", "sql:", sqlQueryBean.toString());
			caseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listCompleteOverdueRate() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return caseHandleInfoDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#listCreateCaseToOverdueReport(java.lang.String, java.util.Date, java.util.Date)
	 */
	public List<SrmCaseHandleInfoDTO> listCreateCaseToOverdueReport(String customerId, Date startDate, Date endDate) throws DataAccessException {
		List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = null;
		LOGGER.debug("listCreateCaseToOverdueReport()", "parameter:customerId=", customerId);
		LOGGER.debug("listCreateCaseToOverdueReport()", "parameter:startDate=", startDate.toString());
		LOGGER.debug("listCreateCaseToOverdueReport()", "parameter:endDate=", endDate.toString());
		try{
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("select ");
			sqlQueryBean.append("t.caseCategory as ").append(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("t.caseId as ").append(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("t.createDate as ").append(SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("t.merchantName as ").append(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("t.merchantCode as ").append(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("t.dtid as ").append(SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("t.serialNumber as ").append(SrmCaseHandleInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("t.description as ").append(SrmCaseHandleInfoDTO.ATTRIBUTE.DESCRIPTION.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("t.acceptableFinishDate as ").append(SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_FINISH_DATE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("t.completeDate as ").append(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("t.caseCategoryName as ").append(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("t.caseStatusName as ").append(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS_NAME.getValue());
			sqlQueryBean.append(" from(");
			sqlQueryBean.append("SELECT ");
			sqlQueryBean.append("schi.CASE_CATEGORY as caseCategory").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("schi.CASE_ID as caseId").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("schi.CREATED_DATE as createDate").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("bm.NAME as merchantName").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("bm.MERCHANT_CODE as merchantCode").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("schi.DTID as dtid").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("scal.SERIAL_NUMBER as serialNumber").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("schi.DESCRIPTION as description").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("schi.ACCEPTABLE_FINISH_DATE as acceptableFinishDate").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("COMPLETE_DATE as completeDate").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("bpid1.ITEM_NAME AS caseCategoryName ").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("bpid2.ITEM_NAME as caseStatusName ");
			sqlQueryBean.append("from ").append(schema).append(".SRM_CASE_HANDLE_INFO schi ");
			sqlQueryBean.append("left join ").append(schema).append(".BIM_MERCHANT bm ON bm.COMPANY_ID = schi.CUSTOMER_ID AND bm.MERCHANT_ID = schi.MERCHANT_CODE ");
			sqlQueryBean.append("left join ").append(schema).append(".SRM_CASE_ASSET_LINK scal ON scal.CASE_ID = schi.CASE_ID AND scal.ITEM_TYPE = '10' ");
			sqlQueryBean.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid1 ON bpid1.ITEM_VALUE = schi.CASE_CATEGORY AND bpid1.BPTD_CODE = 'TICKET_TYPE' ");
			sqlQueryBean.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid2 on bpid2.ITEM_VALUE = schi.CASE_STATUS and bpid2.BPTD_CODE ='CASE_STATUS' ");
			sqlQueryBean.append("where ");
			sqlQueryBean.append("schi.CREATED_DATE >= :startDate ");
			sqlQueryBean.append("and schi.CREATED_DATE < :endDate ");
			sqlQueryBean.append("and schi.CUSTOMER_ID = :customerId ");
			sqlQueryBean.append("and schi.CASE_STATUS <> :voided ");
			sqlQueryBean.append("and schi.CASE_CATEGORY = :caseCategory ");
			sqlQueryBean.append("and scal.IS_LINK = :isLink ");
			sqlQueryBean.append(") t");
			sqlQueryBean.append(" order BY t.createDate");
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
			sqlQueryBean.setParameter("startDate", startDate);
			sqlQueryBean.setParameter("endDate", endDate);
			sqlQueryBean.setParameter("voided", IAtomsConstants.CASE_STATUS.VOIDED.getCode());
			sqlQueryBean.setParameter("isLink", IAtomsConstants.YES);
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue(), IAtomsConstants.CASE_CATEGORY.REPAIR.getCode());
			AliasBean aliasBean = new AliasBean(SrmCaseHandleInfoDTO.class);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.DESCRIPTION.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_FINISH_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS_NAME.getValue(), StringType.INSTANCE);
 			LOGGER.debug("listCreateCaseToOverdueReport()", "sql:", sqlQueryBean.toString());
			caseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listCreateCaseToOverdueReport() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return caseHandleInfoDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#listClosedCaseToOverdueReport(java.lang.String, java.util.Date, java.util.Date, java.lang.Boolean)
	 */
	public List<SrmCaseHandleInfoDTO> listClosedCaseToOverdueReport(String customerId, Date startDate, Date endDate, Boolean isOverdue) throws DataAccessException {
		List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = null;
		LOGGER.debug("listClosedCaseToOverdueReport()", "parameter:customerId=", customerId);
		LOGGER.debug("listClosedCaseToOverdueReport()", "parameter:startDate=", startDate.toString());
		LOGGER.debug("listClosedCaseToOverdueReport()", "parameter:endDate=", endDate.toString());
		LOGGER.debug("listClosedCaseToOverdueReport()", "parameter:isOverdue=", isOverdue.toString());
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("shchi.CASE_CATEGORY", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue());
			sqlStatement.addSelectClause("shchi.CASE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("shchi.CREATED_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sqlStatement.addSelectClause("bm.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue());
			sqlStatement.addSelectClause("bm.MERCHANT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("shchi.DTID", SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("scal.SERIAL_NUMBER", SrmCaseHandleInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("shchi.DESCRIPTION", SrmCaseHandleInfoDTO.ATTRIBUTE.DESCRIPTION.getValue());
			sqlStatement.addSelectClause("shchi.ACCEPTABLE_FINISH_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_FINISH_DATE.getValue());
			sqlStatement.addSelectClause("shchi.COMPLETE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue());
			sqlStatement.addSelectClause("bpid1.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY_NAME.getValue());
			sqlStatement.addSelectClause("bpid2.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS_NAME.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".SRM_CASE_HANDLE_INFO shchi ");
			fromBuffer.append("LEFT JOIN ").append(schema).append(".BIM_MERCHANT bm ON bm.COMPANY_ID = shchi.CUSTOMER_ID AND bm.MERCHANT_ID = shchi.MERCHANT_CODE ");
			fromBuffer.append("LEFT JOIN ").append(schema).append(".SRM_CASE_ASSET_LINK scal ON scal.CASE_ID = shchi.CASE_ID AND scal.ITEM_TYPE = '10' and scal.IS_LINK = :isLink ");
			fromBuffer.append("LEFT JOIN ").append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid1 ON bpid1.ITEM_VALUE = shchi.CASE_CATEGORY AND bpid1.BPTD_CODE = 'TICKET_TYPE' ");
			fromBuffer.append("LEFT JOIN ").append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid2 on bpid2.ITEM_VALUE = shchi.CASE_STATUS and bpid2.BPTD_CODE ='CASE_STATUS' ");
			sqlStatement.addFromExpression(fromBuffer.toString());
			sqlStatement.addWhereClause("shchi.CLOSE_DATE >= :closeDate", startDate);
			sqlStatement.addWhereClause("shchi.CLOSE_DATE < :endDate", endDate);
			sqlStatement.addWhereClause("shchi.CUSTOMER_ID = :customerId", customerId);
			sqlStatement.addWhereClause("shchi.CASE_CATEGORY = :caseCategory", IAtomsConstants.CASE_CATEGORY.REPAIR.getCode());
			sqlStatement.addWhereClause("(shchi.CASE_STATUS = :closed OR shchi.CASE_STATUS = :immediatelyClosing)");
			if (isOverdue) {
				sqlStatement.addWhereClause("shchi.COMPLETE_DATE > shchi.ACCEPTABLE_FINISH_DATE ");
			}
			sqlStatement.setOrderByExpression("shchi.CLOSE_DATE");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("closed", IAtomsConstants.CASE_STATUS.CLOSED.getCode());
			sqlQueryBean.setParameter("immediatelyClosing", IAtomsConstants.CASE_STATUS.IMMEDIATE_CLOSE.getCode());
			sqlQueryBean.setParameter("isLink", IAtomsConstants.YES);
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseHandleInfoDTO.class);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.DESCRIPTION.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_FINISH_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY_NAME.getValue(), StringType.INSTANCE);
			LOGGER.debug("listClosedCaseToOverdueReport()", "sql:", sqlQueryBean.toString());
			caseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listClosedCaseToOverdueReport() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return caseHandleInfoDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#listFinishedReportToOFB(java.lang.String, java.lang.String, java.util.List, java.util.List)
	 */
	public List<SrmCaseHandleInfoDTO> listFinishedReportToOFB(String customerId, String completeDate,
			List<String> caseCategoryArray, List<String> caseStatusArray) throws DataAccessException {
		List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = null;
		LOGGER.debug("listFinishedReportToOFB()", "parameter:customerId=", customerId);
		LOGGER.debug("listFinishedReportToOFB()", "parameter:completeDate=", completeDate.toString());
		LOGGER.debug("listFinishedReportToOFB()", "parameter:caseCategoryArray=", caseCategoryArray.toString());
		LOGGER.debug("listFinishedReportToOFB()", "parameter:caseStatusArray=", caseStatusArray.toString());
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("schi.CASE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("bpid1.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY_NAME.getValue());
			sqlStatement.addSelectClause("bm.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_HEADER_NAME.getValue());
			//當同營業地址勾選，查詢特店地址，當未勾選，查詢案件裝機地址 update by 2017-07-21 bug 2026
			sqlStatement.addSelectClause("(case when schi.IS_BUSSINESS_ADDRESS = 'Y' then bpid3.ITEM_NAME + bmh.BUSINESS_ADDRESS else bpid2.ITEM_NAME + schi.INSTALLED_ADRESS end)", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue());
			sqlStatement.addSelectClause("bm.MERCHANT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("schi.DTID", SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("schi.COMPLETE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".SRM_CASE_HANDLE_INFO schi ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid1 ON bpid1.ITEM_VALUE = schi.CASE_CATEGORY AND bpid1.BPTD_CODE = :ticketType ");
			fromBuffer.append("left join ").append(schema).append(".BIM_MERCHANT bm ON bm.COMPANY_ID = schi.CUSTOMER_ID  AND bm.MERCHANT_ID = schi.MERCHANT_CODE ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid2 ON bpid2.ITEM_VALUE = schi.INSTALLED_ADRESS_LOCATION AND bpid2.BPTD_CODE = :location ");
			//查詢特店資料
			fromBuffer.append("left join ").append(schema).append(".BIM_MERCHANT_HEADER bmh on schi.MERCHANT_HEADER_ID=bmh.MERCHANT_HEADER_ID ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF bpid3 on bpid3.ITEM_VALUE = bmh.LOCATION AND bpid3.BPTD_CODE =:location ");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(customerId)) {
				sqlStatement.addWhereClause("schi.CUSTOMER_ID = :customerId", customerId);
			}
			if (completeDate != null) {
				//sqlStatement.addWhereClause("CONVERT(varchar(12), schi.COMPLETE_DATE, 111) = :completeDate", completeDate);
				StringBuffer whereBuffer = new StringBuffer();
				whereBuffer.append("schi.CASE_ID in ( ");
				whereBuffer.append("select trans.CASE_ID from ");
				whereBuffer.append(schema).append(".SRM_CASE_TRANSACTION trans where ");
				whereBuffer.append("trans.ACTION_ID in ('complete', 'onlineExclusion','immediatelyClosing') and CONVERT( VARCHAR(12) ,trans.CREATED_DATE ,111) = :completeDate) ");
				sqlStatement.addWhereClause(whereBuffer.toString());
			}
			// Task #3113 完修可以退回客服 放開注釋內容
			if (!CollectionUtils.isEmpty(caseStatusArray)) {
				sqlStatement.addWhereClause("schi.CASE_STATUS in ( :caseStatusArray )");
			}
			
			if (!CollectionUtils.isEmpty(caseCategoryArray)) {
				sqlStatement.addWhereClause("schi.CASE_CATEGORY in ( :caseCategoryArray )");
			}
			sqlStatement.setOrderByExpression("bm.MERCHANT_CODE, schi.DTID");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("ticketType", IATOMS_PARAM_TYPE.TICKET_TYPE.getCode()) ;
			sqlQueryBean.setParameter("location", IATOMS_PARAM_TYPE.LOCATION.getCode());
			if (completeDate != null) {
				sqlQueryBean.setParameter("completeDate", completeDate);
			}
			// Task #3113 完修可以退回客服 放開注釋內容
			if (!CollectionUtils.isEmpty(caseStatusArray)) {
				sqlQueryBean.setParameter("caseStatusArray", caseStatusArray);
			}
			if (!CollectionUtils.isEmpty(caseCategoryArray)) {
				sqlQueryBean.setParameter("caseCategoryArray", caseCategoryArray);
			}
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseHandleInfoDTO.class);
			LOGGER.debug("listFinishedReportToOFB()", "sql:", sqlQueryBean.toString());
			caseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listFinishedReportToOFB() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return caseHandleInfoDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#listCaseIdByDtid(java.lang.String)
	 */
	public List<SrmCaseHandleInfoDTO> listCaseIdByDtid(String dtid) throws DataAccessException {
		List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = null;
		try {
			LOGGER.debug("listCaseIdByDtid()", "parameter:dtid=", dtid);
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("info.CASE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("info.CASE_STATUS", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS.getValue());
			sqlStatement.addFromExpression(schema + ".SRM_CASE_HANDLE_INFO info");
			if (StringUtils.hasText(dtid)) {
				sqlStatement.addWhereClause("info.DTID = :dtid", dtid);
			}
			sqlStatement.addWhereClause("(info.CASE_STATUS <> :closed and info.CASE_STATUS <> :immediateclose and info.CASE_STATUS <> :voided and info.CASE_CATEGORY <> 'OTHER')");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("closed", IAtomsConstants.CASE_STATUS.CLOSED.getCode());
			sqlQueryBean.setParameter("immediateclose", IAtomsConstants.CASE_STATUS.IMMEDIATE_CLOSE.getCode());
			sqlQueryBean.setParameter("voided", IAtomsConstants.CASE_STATUS.VOIDED.getCode());
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseHandleInfoDTO.class);
			caseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listCaseIdByDtid() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return caseHandleInfoDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#installListBy(java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@Override
	public List<SrmCaseHandleInfoDTO> installListBy(String queryCustomerId, String queryCompleteDateStart, 
			String queryCompleteDateEnd, String isInstant, Integer pageIndex, Integer pageSize, String sort,
			String order) throws DataAccessException {
		List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = null;
		try {
			LOGGER.debug("installListBy()", "parameter:queryCustomerId=", queryCustomerId);
			LOGGER.debug("installListBy()", "parameter:queryCompleteDateStart=", queryCompleteDateStart);
			LOGGER.debug("installListBy()", "parameter:queryCompleteDateEnd=", queryCompleteDateEnd);
			LOGGER.debug("installListBy()", "parameter:sort=", sort);
			LOGGER.debug("installListBy()", "parameter:order=", order);
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append(" SELECT");
			//客戶
			sql.append(" customer.SHORT_NAME AS customerName , ");
			//需求單號
			sql.append(" info.REQUIREMENT_NO AS requirementNo, ");	
			//案件編號
			sql.append(" info.CASE_ID AS caseId, ");
			//業務人員
			sql.append(" header.AO_NAME AS aoName, ");
			//案件類別
			sql.append(" categoryItem.ITEM_NAME AS caseCategory , ");
			//案件類型
			sql.append(" caseTypeItem.ITEM_NAME AS caseType , ");
			//進件日期
			sql.append(" info.CREATED_DATE AS createdDate, ");
			//派工日期
			sql.append(" ( SELECT  MIN(transcation.DEAL_DATE) ");
			if(IAtomsConstants.YES.equals(isInstant)) {
				sql.append(" FROM ").append(schema).append(" .SRM_CASE_TRANSACTION transcation ");
			} else {
				sql.append(" FROM ").append(schema).append(" .SRM_HISTORY_CASE_TRANSACTION transcation ");
			}
			sql.append(" WHERE transcation.CASE_ID = info.CASE_ID ");
			sql.append(" and (  transcation.ACTION_ID = 'dispatching' OR transcation.ACTION_ID = 'autoDispatching' )) AS dispatchDate , ");
			//應完修時間
			sql.append(" info.ACCEPTABLE_FINISH_DATE AS acceptableFinishDate, ");
			//實際完成日
			sql.append(" info.COMPLETE_DATE AS completeDate, ");
			//處理方式
			sql.append(" processItem.ITEM_NAME AS processType , ");
			//裝機類型
			sql.append(" installTypeItem.ITEM_NAME AS installType , ");
			//到場次數
			sql.append(" info.ATTENDANCE_TIMES AS attendanceTimes , ");
			//到場說明
			sql.append("  ( CASE  WHEN info.ATTENDANCE_TIMES > 1 ");
			sql.append("  THEN STUFF( ( ").append(" SELECT ").append(" ',' + LTRIM( attendance.DESCRIPTION ) ");
			if(IAtomsConstants.YES.equals(isInstant)) {
				sql.append(" FROM ").append(schema).append(" .SRM_CASE_TRANSACTION attendance ");
			} else {
				sql.append(" FROM ").append(schema).append(" .SRM_HISTORY_CASE_TRANSACTION attendance ");
			}
			sql.append(" WHERE ").append("  attendance.CASE_ID = info.CASE_ID ").append(" AND attendance.ACTION_ID = 'arrive'  AND attendance.DESCRIPTION <> '' FOR XML PATH('') ");
			sql.append(" ) ,1 , 1 , '' ) ELSE '' END ) AS attendanceDesc ,");
			//延期說明
			if(IAtomsConstants.YES.equals(isInstant)) {
				sql.append(" ( SELECT TOP 1 tra.DESCRIPTION ").append(" FROM ").append(schema).append(" .SRM_CASE_TRANSACTION tra ");
			} else {
				sql.append(" ( SELECT TOP 1 tra.DESCRIPTION ").append(" FROM ").append(schema).append(" .SRM_HISTORY_CASE_TRANSACTION tra ");
			}
			sql.append(" WHERE ").append(" tra.CASE_ID = info.CASE_ID ").append(" AND tra.ACTION_ID = 'delay' ");
			sql.append(" ORDER BY  DEAL_DATE DESC ) AS rushRepairDesc , ");
			//特店代號
			sql.append(" mer.MERCHANT_CODE AS merchantCode , ");
			//特店名稱
			sql.append(" mer.NAME AS merchantName , ");
			//特店區域
			sql.append(" areaItem.ITEM_NAME AS area , ");
			//特店地址
			sql.append(" locationItem.ITEM_NAME + header.BUSINESS_ADDRESS AS bussionAddress , ");
			//刷卡機型
			sql.append(" asset.NAME AS assetName , ");
			//設備型號
			sql.append(" repoEdc.ASSET_MODEL AS model, ");
			//倉別
			sql.append(" edcWarehouse.NAME AS wareHouseName , ");
			//裝機地址
			sql.append(" installLocationItem.ITEM_NAME + info.INSTALLED_ADRESS AS installedAddress , ");
			//裝機廠商
			sql.append(" company.SHORT_NAME AS companyName , ");
			//DTID
			sql.append(" info.DTID AS dtid , ");
			//TID
			sql.append(" transPara.TID AS tid , ");
			//設備啟用日
			sql.append(" edcLink.ENABLE_DATE AS enableDate ,");
			//設備序號
			sql.append(" edcLink.SERIAL_NUMBER AS serialNumber, ");
			//財產編號
			sql.append(" ( CASE WHEN repoEdc.MA_TYPE = 'BUYOUT' ").append(" THEN repoEdc.PROPERTY_ID ");
			sql.append(" ELSE repoEdc.SIM_ENABLE_NO ");
			sql.append(" END ) AS propertyId , ");
			//合約編號
			sql.append(" edcContract.CONTRACT_CODE AS contractId, ");
			//週邊設備1
			sql.append(" perAsset.NAME AS peripheralsName, ");
			//週邊設備1序號
			sql.append(" perLink.SERIAL_NUMBER AS peripheralsSerialNumber, ");
			//週邊設備1合約編號
			sql.append(" perContract.CONTRACT_CODE AS peripheralsContractCode , ");
			//週邊設備2 
			sql.append(" perAsset2.NAME AS peripherals2Name , ");
			//週邊設備2序號
			sql.append(" perLink2.SERIAL_NUMBER AS peripherals2SerialNumber , ");
			//週邊設備2合約編號
			sql.append(" perContract2.CONTRACT_CODE AS peripherals2ContractCode , ");
			//週邊設備3 
			sql.append(" perAsset3.NAME AS peripherals3Name , ");
			//週邊設備3序號
			sql.append(" perLink3.SERIAL_NUMBER AS peripherals3SerialNumber , ");
			//週邊設備3合約編號
			sql.append(" perContract3.CONTRACT_CODE AS peripherals3ContractCode , ");
			//設備開啟功能清單
			sql.append(" STUFF( ( SELECT ',' + LTRIM( functionItem1.ITEM_NAME ) ").append(" FROM ");
			if(IAtomsConstants.YES.equals(isInstant)) {
				sql.append(schema).append(" .SRM_CASE_ASSET_FUNCTION function1");
			} else {
				sql.append(schema).append(" .SRM_HISTORY_CASE_ASSET_FUNCTION function1");
			}
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF functionItem1 ");
			sql.append(" ON function1.FUNCTION_ID = functionItem1.ITEM_VALUE");
			sql.append(" AND functionItem1.BPTD_CODE = :supported_function");
			sql.append(" AND function1.CASE_ID = info.CASE_ID FOR XML PATH('') ) , 1 , 1 , '') AS functionTypeList , ");
			//ECR線
			sql.append(" STUFF( (  SELECT ',' + LTRIM( CAST( assetLinkEdcLine.NUMBER AS VARCHAR ) + '-' + CAST( ( assetLinkEdcLine.NUMBER * assetLinkEdcLine.PRICE) AS VARCHAR ) ) ").append(" from ");
			if(IAtomsConstants.YES.equals(isInstant)) {
				sql.append(schema).append(" .SRM_CASE_ASSET_LINK assetLinkEdcLine ");
			} else {
				sql.append(schema).append(" .SRM_HISTORY_CASE_ASSET_LINK assetLinkEdcLine ");
			}
			sql.append(" WHERE assetLinkEdcLine.ITEM_TYPE = '20' ");
			sql.append(" AND assetLinkEdcLine.ITEM_CATEGORY = 'ECRLINE' ");
			sql.append(" AND assetLinkEdcLine.CASE_ID = info.CASE_ID FOR XML PATH('') ");
			sql.append(" ) , 1 , 1 , '') AS ecrLine , ");
			//網路線
			sql.append(" STUFF( ( SELECT ',' + LTRIM( CAST( assetLinkNetLine.NUMBER AS VARCHAR ) + '-' + CAST( (assetLinkNetLine.NUMBER * assetLinkNetLine.PRICE) AS VARCHAR ) ) ").append(" from ");
			if(IAtomsConstants.YES.equals(isInstant)) {
				sql.append(schema).append(" .SRM_CASE_ASSET_LINK assetLinkNetLine ");
			} else {
				sql.append(schema).append(" .SRM_HISTORY_CASE_ASSET_LINK assetLinkNetLine ");
			}
			sql.append(" WHERE assetLinkNetLine.ITEM_TYPE = '20' ");
			sql.append(" AND assetLinkNetLine.ITEM_CATEGORY = 'NetworkRoute' ");
			sql.append(" AND assetLinkNetLine.CASE_ID = info.CASE_ID FOR XML PATH('') ");
			sql.append(" ) , 1 ,1 , '') AS netLine , ");
			//耗材品項、個數、費用
			sql.append(" STUFF( ( SELECT ',' + LTRIM( ss.SUPPLIES_NAME + '-' + CAST( assetLinkOther.NUMBER AS VARCHAR ) + '-' + CAST( (assetLinkOther.NUMBER * assetLinkOther.PRICE) AS VARCHAR ) )");
			if(IAtomsConstants.YES.equals(isInstant)) {
				sql.append(" FROM ").append(schema).append(" .SRM_CASE_ASSET_LINK assetLinkOther ");
			} else {
				sql.append(" FROM ").append(schema).append(" .SRM_HISTORY_CASE_ASSET_LINK assetLinkOther ");
			}
			sql.append(" INNER JOIN ").append(schema).append(" .DMM_SUPPLIES ss ON assetLinkOther.ITEM_ID = ss.SUPPLIES_ID ");
			sql.append(" WHERE assetLinkOther.ITEM_TYPE = '20' ");
			sql.append(" AND assetLinkOther.ITEM_CATEGORY <> 'NetworkRoute' ");
			sql.append(" AND assetLinkOther.ITEM_CATEGORY <> 'ECRLINE' ");
			sql.append(" AND assetLinkOther.CASE_ID = info.CASE_ID FOR XML PATH('') ");
			sql.append(" ) ,1 ,1 ,'' ) AS otherLine , ");
			//完修逾期時間
			sql.append(" ( CASE WHEN info.ACCEPTABLE_FINISH_DATE < info.COMPLETE_DATE ");
			sql.append(" THEN datediff( MINUTE , info.ACCEPTABLE_FINISH_DATE , ");
			sql.append(" info.COMPLETE_DATE ) ");
			sql.append(" ELSE NULL END ) AS delayTime , ");
			sql.append(" info.IS_FIRST_INSTALLED AS isFirstInstalled, ");
			sql.append(" conAsset.PRICE as posPrice ");
			if(IAtomsConstants.YES.equals(isInstant)) {
				sql.append(" FROM ").append(schema).append(" .SRM_CASE_HANDLE_INFO info  ");
			} else {
				sql.append(" FROM ").append(schema).append(" .SRM_HISTORY_CASE_HANDLE_INFO info  ");
			}
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_COMPANY customer ON info.CUSTOMER_ID = customer.COMPANY_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_COMPANY company ON info.COMPANY_ID = company.COMPANY_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer ON info.MERCHANT_CODE = mer.MERCHANT_ID");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT_HEADER header  ON info.MERCHANT_HEADER_ID = header.MERCHANT_HEADER_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE asset ON info.EDC_TYPE = asset.ASSET_TYPE_ID ");
			if(IAtomsConstants.YES.equals(isInstant)) {
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_ASSET_LINK edcLink ON info.CASE_ID = edcLink.CASE_ID AND edcLink.ITEM_TYPE = '10' ");
			} else {
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_HISTORY_CASE_ASSET_LINK edcLink ON info.CASE_ID = edcLink.CASE_ID AND edcLink.ITEM_TYPE = '10' ");
			}
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_WAREHOUSE edcWarehouse  ON edcLink.WAREHOUSE_ID = edcWarehouse.WAREHOUSE_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT edcContract ON edcLink.CONTRACT_ID = edcContract.CONTRACT_ID");
			if(IAtomsConstants.YES.equals(isInstant)) {
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_TRANSACTION_PARAMETER transPara  ON transPara.CASE_ID = info.CASE_ID AND ( transPara.TRANSACTION_TYPE = 'COMMON_VM' ");
				sql.append(" or transPara.TRANSACTION_TYPE = 'COMMON_VMJ' or transPara.TRANSACTION_TYPE = 'COMMON_VMJU' )");
			} else {
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_HISTORY_CASE_TRANSACTION_PARAMETER transPara  ON transPara.CASE_ID = info.CASE_ID AND ( transPara.TRANSACTION_TYPE = 'COMMON_VM' ");
				sql.append(" or transPara.TRANSACTION_TYPE = 'COMMON_VMJ' or transPara.TRANSACTION_TYPE = 'COMMON_VMJU' )");
			}
			sql.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE perAsset ON info.PERIPHERALS = perAsset.ASSET_TYPE_ID");
			if(IAtomsConstants.YES.equals(isInstant)) {
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_ASSET_LINK perLink  ON info.CASE_ID = perLink.CASE_ID AND perLink.ITEM_TYPE = '11'  AND perLink.IS_LINK = 'Y' ");
			} else {
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_HISTORY_CASE_ASSET_LINK perLink  ON info.CASE_ID = perLink.CASE_ID AND perLink.ITEM_TYPE = '11' AND perLink.IS_LINK = 'Y' ");
			}
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT perContract ON perLink.CONTRACT_ID = perContract.CONTRACT_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE perAsset2 ON info.PERIPHERALS2 = perAsset2.ASSET_TYPE_ID");
			if(IAtomsConstants.YES.equals(isInstant)) {
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_ASSET_LINK perLink2  ON info.CASE_ID = perLink2.CASE_ID AND perLink2.ITEM_TYPE = '12' AND perLink2.IS_LINK = 'Y' ");
			} else {
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_HISTORY_CASE_ASSET_LINK perLink2  ON info.CASE_ID = perLink2.CASE_ID AND perLink2.ITEM_TYPE = '12' AND perLink2.IS_LINK = 'Y' ");
			}
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT perContract2 ON perLink2.CONTRACT_ID = perContract2.CONTRACT_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE perAsset3 ON info.PERIPHERALS3 = perAsset3.ASSET_TYPE_ID ");
			if(IAtomsConstants.YES.equals(isInstant)) {
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_ASSET_LINK perLink3 ON info.CASE_ID = perLink3.CASE_ID AND perLink3.ITEM_TYPE = '13' AND perLink3.IS_LINK = 'Y' ");
			} else {
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_HISTORY_CASE_ASSET_LINK perLink3 ON info.CASE_ID = perLink3.CASE_ID AND perLink3.ITEM_TYPE = '13' AND perLink3.IS_LINK = 'Y' ");
			}
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT perContract3 ON perLink3.CONTRACT_ID = perContract3.CONTRACT_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF categoryItem ON categoryItem.BPTD_CODE = 'TICKET_TYPE' ");
		//	sql.append(" AND info.CASE_CATEGORY = categoryItem.ITEM_VALUE AND categoryItem.APPROVED_FLAG = 'Y'  ");
			// 處理approvedFlag
			sql.append(" AND info.CASE_CATEGORY = categoryItem.ITEM_VALUE AND isnull(categoryItem.DELETED, 'N') = 'N'  ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF caseTypeItem ON caseTypeItem.BPTD_CODE = 'TICKET_MODE'");
		//	sql.append(" AND info.CASE_TYPE = caseTypeItem.ITEM_VALUE  AND caseTypeItem.APPROVED_FLAG = 'Y'  ");
			// 處理approvedFlag
			sql.append(" AND info.CASE_TYPE = caseTypeItem.ITEM_VALUE  AND isnull(caseTypeItem.DELETED, 'N') = 'N' ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF processItem ON processItem.BPTD_CODE = 'PROCESS_TYPE'");
		//	sql.append(" AND info.PROCESS_TYPE = processItem.ITEM_VALUE AND processItem.APPROVED_FLAG = 'Y'  ");
			// 處理approvedFlag
			sql.append(" AND info.PROCESS_TYPE = processItem.ITEM_VALUE AND isnull(processItem.DELETED, 'N') = 'N'  ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF installTypeItem ON installTypeItem.BPTD_CODE = 'INSTALL_TYPE'");
		//	sql.append(" AND info.INSTALL_TYPE = installTypeItem.ITEM_VALUE AND installTypeItem.APPROVED_FLAG = 'Y' ");
			// 處理approvedFlag
			sql.append(" AND info.INSTALL_TYPE = installTypeItem.ITEM_VALUE AND isnull(installTypeItem.DELETED, 'N') = 'N' ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF areaItem  ON areaItem.BPTD_CODE = 'REGION'");
		//	sql.append(" AND header.AREA = areaItem.ITEM_VALUE AND areaItem.APPROVED_FLAG = 'Y' ");
			// 處理approvedFlag
			sql.append(" AND header.AREA = areaItem.ITEM_VALUE AND isnull(areaItem.DELETED, 'N') = 'N' ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF locationItem ON locationItem.BPTD_CODE = 'LOCATION'");
		//	sql.append(" AND header.LOCATION = locationItem.ITEM_VALUE AND locationItem.APPROVED_FLAG = 'Y'  ");
			// 處理approvedFlag
			sql.append(" AND header.LOCATION = locationItem.ITEM_VALUE AND isnull(locationItem.DELETED, 'N') = 'N'  ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF installLocationItem ON installLocationItem.BPTD_CODE = 'LOCATION' ");
		//	sql.append(" AND info.INSTALLED_ADRESS_LOCATION = installLocationItem.ITEM_VALUE AND installLocationItem.APPROVED_FLAG = 'Y'  ");
			// 處理approvedFlag
			sql.append(" AND info.INSTALLED_ADRESS_LOCATION = installLocationItem.ITEM_VALUE AND isnull(installLocationItem.DELETED, 'N') = 'N'  ");
			sql.append(" LEFT JOIN ").append(schema).append(" .DMM_REPOSITORY_HISTORY repoEdc ON repoEdc.HISTORY_ID = edcLink.HISTORY_ASSET_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT_ASSET conAsset ON conAsset.CONTRACT_ID = edcLink.CONTRACT_ID AND conAsset.ASSET_TYPE_ID = info.EDC_TYPE");
			sql.append(" WHERE 1 = 1 ");
			//查詢條件 開始時間
			if(StringUtils.hasText(queryCompleteDateStart)){
				sql.append(" AND info.COMPLETE_DATE > :startDate ");
				sql.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.STARTDATE.getValue(), queryCompleteDateStart);
				LOGGER.debug(this.getClass().getName(), ".listByDtid() --> queryCompleteDateStart: ", queryCompleteDateStart);
			}
			//查詢條件 結束時間
			if(StringUtils.hasText(queryCompleteDateEnd)){
				sql.append(" AND info.COMPLETE_DATE < dateadd( DAY , 1 , :endDate ) ");
				sql.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.ENDDATE.getValue(), queryCompleteDateEnd);
				LOGGER.debug(this.getClass().getName(), ".listByDtid() --> queryCompleteDateEnd: ", queryCompleteDateEnd);
			}
			//查詢條件 客戶id
			if(StringUtils.hasText(queryCustomerId)){
				sql.append(" AND info.CUSTOMER_ID = :customerId ");
				sql.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), queryCustomerId);
				LOGGER.debug(this.getClass().getName(), ".listByDtid() --> queryCustomerId: ", queryCustomerId);
			}
			sql.append(" AND info.CASE_CATEGORY = :install ");
			//sql.append(" AND (info.CASE_STATUS = :closed or info.CASE_STATUS = :immediateClose) ");
			sql.setParameter("install", IAtomsConstants.CASE_CATEGORY.INSTALL.getCode());
			//sql.setParameter("closed", IAtomsConstants.CASE_STATUS.CLOSED.getCode());
			//sql.setParameter("immediateClose", IAtomsConstants.CASE_STATUS.IMMEDIATE_CLOSE.getCode());
			sql.append(" AND info.COMPLETE_DATE is not null and info.COMPLETE_DATE <> '' ");
			
			//排序的兩個參數判斷是否為空
			if (StringUtils.hasText(sort) && StringUtils.hasText(order)) {
				sql.append(" ORDER BY ").append(sort).append(IAtomsConstants.MARK_SPACE).append(order);
			} else {
				sql.append(" order by info.CASE_ID ASC, customer.SHORT_NAME ASC ");
			}
			sql.setParameter("supported_function", IATOMS_PARAM_TYPE.SUPPORTED_FUNCTION.getCode());
			
			sql.setPageSize(pageSize);
			sql.setStartPage(pageIndex - 1);
			AliasBean alias = new AliasBean(SrmCaseHandleInfoDTO.class);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.AO_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_FINISH_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue(), TimestampType.INSTANCE);
			
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PROCESS_TYPE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_TYPE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ATTENDANCE_TIMES.getValue(), IntegerType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ATTENDANCEDESC.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.RUSHREPAIRDESC.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.AREA.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.BUSSION_ADDRESS.getValue(), StringType.INSTANCE);
			
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ASSET_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.MODEL.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.WAREHOUSE_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADDRESS.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_NAME.getValue(), StringType.INSTANCE);
			
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.TID.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ENABLE_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PROPERTY_ID.getValue(), StringType.INSTANCE);
			
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTRACT_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_CONTRACT_CODE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_CONTRACT_CODE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_CONTRACT_CODE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.FUNCTIONTYPE_LIST.getValue(), StringType.INSTANCE);
			
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ECRLINE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.NETLINE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.OTHERLINE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.DELAYTIME.getValue(), BigDecimalType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.IS_FIRST_INSTALLED.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.POSPRICE.getValue(), StringType.INSTANCE);
			caseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sql, alias);
			LOGGER.debug(this.getClass().getName(), "listBy() --> sql: ", sql.toString()); 
		} catch (Exception e) {
			LOGGER.error("installListBy() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return caseHandleInfoDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#installCount(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public int installCount(String queryCustomerId,
			String queryCompleteDateStart, String queryCompleteDateEnd,String isInstant)
			throws DataAccessException {
		LOGGER.debug(".installCount()", "parameters:queryCustomerId:", queryCustomerId);
		LOGGER.debug(".installCount()", "parameters:queryCompleteDateStart:", queryCompleteDateStart);
		LOGGER.debug(".installCount()", "parameters:queryCompleteDateEnd:", queryCompleteDateEnd);
		try {
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			Integer result = null;
			//查詢分頁最底下顯示的總條數
			sql.append(" SELECT COUNT(1)  ");
			if(IAtomsConstants.YES.equals(isInstant)) {
				sql.append(" from ").append(schema).append(" .SRM_CASE_HANDLE_INFO info ");
			} else {
				sql.append(" from ").append(schema).append(" .SRM_HISTORY_CASE_HANDLE_INFO info ");
			}
			sql.append(" where 1 = 1 ");
			//查詢條件 開始時間
			if(StringUtils.hasText(queryCompleteDateStart)){
				sql.append(" AND info.COMPLETE_DATE > :startDate ");
				sql.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.STARTDATE.getValue(), queryCompleteDateStart);
				LOGGER.debug(this.getClass().getName(), ".installCount() --> queryCompleteDateStart: ", queryCompleteDateStart);
			}
			//查詢條件 結束時間
			if(StringUtils.hasText(queryCompleteDateEnd)){
				sql.append(" AND info.COMPLETE_DATE < dateadd( DAY , 1 , :endDate ) ");
				sql.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.ENDDATE.getValue(), queryCompleteDateEnd);
				LOGGER.debug(this.getClass().getName(), ".installCount() --> queryCompleteDateEnd: ", queryCompleteDateEnd);
			}
			//查詢條件 客戶id
			if(StringUtils.hasText(queryCustomerId)){
				sql.append(" AND info.CUSTOMER_ID = :customerId ");
				sql.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), queryCustomerId);
				LOGGER.debug(this.getClass().getName(), ".installCount() --> queryCustomerId: ", queryCustomerId);
			}
			sql.append(" AND info.COMPLETE_DATE is not null and info.COMPLETE_DATE <> '' ");
			sql.append(" AND info.CASE_CATEGORY = :install ");
			//sql.append(" AND (info.CASE_STATUS = :closed or info.CASE_STATUS = :immediateClose) ");
			sql.setParameter("install", IAtomsConstants.CASE_CATEGORY.INSTALL.getCode());
			//sql.setParameter("closed", IAtomsConstants.CASE_STATUS.CLOSED.getCode());
			//sql.setParameter("immediateClose", IAtomsConstants.CASE_STATUS.IMMEDIATE_CLOSE.getCode());
			
			LOGGER.debug(".installCount()", " Native SQL---------->", sql.toString());
			List<Integer> list = this.getDaoSupport().findByNativeSql(sql);
			LOGGER.debug(".installCount() --> sql: ", sql.toString());
			if(!CollectionUtils.isEmpty(list)){
				result = list.get(0);
			}
			return result;
		} catch (Exception e) {
			LOGGER.error(".installCount() is error", "DAO Exception", e);
			throw new DataAccessException(CoreMessageCode.QUERY_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.REB_IATOMS_TB_NAME_SUPPLIES)}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#unInstallListBy(java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@Override
	public List<SrmCaseHandleInfoDTO> unInstallListBy(String queryCustomerId, String queryCompleteDateStart, String queryCompleteDateEnd,String isInstant,
			Integer pageIndex, Integer pageSize, String sort, String order) throws DataAccessException {
		List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = null;
		try {
			LOGGER.debug("installListBy()", "parameter:queryCustomerId=", queryCustomerId);
			LOGGER.debug("installListBy()", "parameter:queryCompleteDateStart=", queryCompleteDateStart);
			LOGGER.debug("installListBy()", "parameter:queryCompleteDateEnd=", queryCompleteDateEnd);
			LOGGER.debug("installListBy()", "parameter:sort=", sort);
			LOGGER.debug("installListBy()", "parameter:order=", order);
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append(" SELECT");
			//客戶
			sql.append(" customer.SHORT_NAME AS customerName , ");
			//需求單號
			sql.append(" info.REQUIREMENT_NO AS requirementNo, ");	
			//案件編號
			sql.append(" info.CASE_ID AS caseId, ");
			//業務人員
			sql.append(" header.AO_NAME AS aoName, ");
			//案件類別
			sql.append(" categoryItem.ITEM_NAME AS caseCategory , ");
			//案件類型
			sql.append(" caseTypeItem.ITEM_NAME AS caseType , ");
			//進件日期
			sql.append(" info.CREATED_DATE AS createdDate, ");
			//派工日期
			sql.append(" ( SELECT  MIN(transcation.DEAL_DATE) ");
			if(IAtomsConstants.YES.equals(isInstant)) {
				sql.append(" FROM ").append(schema).append(" .SRM_CASE_TRANSACTION transcation ");
			} else {
				sql.append(" FROM ").append(schema).append(" .SRM_HISTORY_CASE_TRANSACTION transcation ");
			}
			sql.append(" WHERE transcation.CASE_ID = info.CASE_ID ");
			sql.append(" and (  transcation.ACTION_ID = 'dispatching' OR transcation.ACTION_ID = 'autoDispatching' )) AS dispatchDate , ");
			//應完修時間
			sql.append(" info.ACCEPTABLE_FINISH_DATE AS acceptableFinishDate, ");
			//實際完成日
			sql.append(" info.COMPLETE_DATE AS completeDate, ");
			//處理方式
			sql.append(" processItem.ITEM_NAME AS processType , ");
			//裝機類型
			sql.append(" installTypeItem.ITEM_NAME AS installType , ");
			//到場次數
			sql.append(" info.ATTENDANCE_TIMES AS attendanceTimes , ");
			//到場說明
			sql.append("  ( CASE  WHEN info.ATTENDANCE_TIMES > 1 ");
			sql.append("  THEN STUFF( ( ").append(" SELECT ").append(" ',' + LTRIM( attendance.DESCRIPTION ) ");
			if(IAtomsConstants.YES.equals(isInstant)) { 
				sql.append(" FROM ").append(schema).append(" .SRM_CASE_TRANSACTION attendance ");
			} else {
				sql.append(" FROM ").append(schema).append(" .SRM_HISTORY_CASE_TRANSACTION attendance ");
			}
			sql.append(" WHERE ").append("  attendance.CASE_ID = info.CASE_ID ").append(" AND attendance.ACTION_ID = 'arrive' AND attendance.DESCRIPTION <> '' FOR XML PATH('') ");
			sql.append(" ) ,1 , 1 , '' ) ELSE '' END ) AS attendanceDesc ,");
			//延期說明
			if(IAtomsConstants.YES.equals(isInstant)) { 
				sql.append(" ( SELECT TOP 1 tra.DESCRIPTION ").append(" FROM ").append(schema).append(" .SRM_CASE_TRANSACTION tra ");
			} else {
				sql.append(" ( SELECT TOP 1 tra.DESCRIPTION ").append(" FROM ").append(schema).append(" .SRM_HISTORY_CASE_TRANSACTION tra ");
			}
			sql.append(" WHERE ").append(" tra.CASE_ID = info.CASE_ID ").append(" AND tra.ACTION_ID = 'delay' ");
			sql.append(" ORDER BY  DEAL_DATE DESC ) AS rushRepairDesc , ");
			//特店代號
			sql.append(" mer.MERCHANT_CODE AS merchantCode , ");
			//特店名稱
			sql.append(" mer.NAME AS merchantName , ");
			//特店區域
			sql.append(" areaItem.ITEM_NAME AS area , ");
			//特店地址
			sql.append(" locationItem.ITEM_NAME + header.BUSINESS_ADDRESS AS bussionAddress , ");
			//刷卡機型
			sql.append(" asset.NAME AS assetName , ");
			//設備型號
			sql.append(" repoEdc.ASSET_MODEL AS model, ");
			//倉別
			sql.append(" edcWarehouse.NAME AS wareHouseName , ");
			//裝機地址
			sql.append(" installLocationItem.ITEM_NAME + info.INSTALLED_ADRESS AS installedAddress , ");
			//裝機廠商
			sql.append(" company.SHORT_NAME AS companyName , ");
			//拆機地址
			sql.append(" unInstallLocationItem.ITEM_NAME + info.CONTACT_ADDRESS AS unInstalledAddress , ");
			//拆機廠商
			sql.append(" company.SHORT_NAME AS unInstalledCompanyName , ");
			//DTID
			sql.append(" info.DTID AS dtid , ");
			//TID
			sql.append(" transPara.TID AS tid , ");
			//設備啟用日
			sql.append(" edcLink.ENABLE_DATE AS enableDate , ");
			//設備序號
			sql.append(" edcLink.SERIAL_NUMBER AS serialNumber, ");
			//財產編號
			sql.append(" ( CASE WHEN repoEdc.MA_TYPE = 'BUYOUT' ").append(" THEN repoEdc.PROPERTY_ID ");
			sql.append(" ELSE repoEdc.SIM_ENABLE_NO ");
			sql.append(" END ) AS propertyId , ");
			//合約編號
			sql.append(" edcContract.CONTRACT_CODE AS contractId, ");
			//週邊設備1
			sql.append(" perAsset.NAME AS peripheralsName, ");
			//週邊設備1序號
			sql.append(" perLink.SERIAL_NUMBER AS peripheralsSerialNumber, ");
			//週邊設備1合約編號
			sql.append(" perContract.CONTRACT_CODE AS peripheralsContractCode , ");
			//週邊設備2 
			sql.append(" perAsset2.NAME AS peripherals2Name , ");
			//週邊設備2序號
			sql.append(" perLink2.SERIAL_NUMBER AS peripherals2SerialNumber , ");
			//週邊設備2合約編號
			sql.append(" perContract2.CONTRACT_CODE AS peripherals2ContractCode , ");
			//週邊設備3 
			sql.append(" perAsset3.NAME AS peripherals3Name , ");
			//週邊設備3序號
			sql.append(" perLink3.SERIAL_NUMBER AS peripherals3SerialNumber , ");
			//週邊設備3合約編號
			sql.append(" perContract3.CONTRACT_CODE AS peripherals3ContractCode , ");
			//設備開啟功能清單
			sql.append(" STUFF( ( SELECT ',' + LTRIM( functionItem1.ITEM_NAME ) ").append(" FROM ");
			if(IAtomsConstants.YES.equals(isInstant)) { 
				sql.append(schema).append(" .SRM_CASE_ASSET_FUNCTION function1");
			} else {
				sql.append(schema).append(" .SRM_HISTORY_CASE_ASSET_FUNCTION function1");
			}
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF functionItem1 ");
			sql.append(" ON function1.FUNCTION_ID = functionItem1.ITEM_VALUE");
			sql.append(" AND functionItem1.BPTD_CODE = :supported_function");
			sql.append(" AND function1.CASE_ID = info.CASE_ID FOR XML PATH('') ) , 1 , 1 , '') AS functionTypeList , ");
			//裝機日期
			sql.append(" info.INSTALLED_DATE AS installedDate, ");
			//拆機日期
			sql.append(" info.UNINSTALLED_DATE AS uninstalledDate, ");
			//裝機-拆機是否未滿三個月
			sql.append(" datediff( DAY ,info.INSTALLED_DATE ,info.UNINSTALLED_DATE ) AS userdDays90 , ");
			sql.append(" datediff( DAY ,info.INSTALLED_DATE ,info.UNINSTALLED_DATE ) AS userdDays120 , ");
			//完修逾期時間
			sql.append(" ( CASE WHEN info.ACCEPTABLE_FINISH_DATE < info.COMPLETE_DATE ");
			sql.append(" THEN datediff( MINUTE , info.ACCEPTABLE_FINISH_DATE , ");
			sql.append(" info.COMPLETE_DATE ) ");
			sql.append(" ELSE NULL END ) AS delayTime ");
			if(IAtomsConstants.YES.equals(isInstant)) { 
				sql.append(" FROM ").append(schema).append(" .SRM_CASE_HANDLE_INFO info   ");
			} else {
				sql.append(" FROM ").append(schema).append(" .SRM_HISTORY_CASE_HANDLE_INFO info   ");
			}
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_COMPANY customer ON info.CUSTOMER_ID = customer.COMPANY_ID  ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_COMPANY company  ON info.COMPANY_ID = company.COMPANY_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer ON info.MERCHANT_CODE = mer.MERCHANT_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT_HEADER header ON info.MERCHANT_HEADER_ID = header.MERCHANT_HEADER_ID  ");
			sql.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE asset ON info.EDC_TYPE = asset.ASSET_TYPE_ID  ");
			if(IAtomsConstants.YES.equals(isInstant)) { 
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_ASSET_LINK edcLink  ON info.CASE_ID = edcLink.CASE_ID AND edcLink.ITEM_TYPE = '10'  ");
			} else {
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_HISTORY_CASE_ASSET_LINK edcLink  ON info.CASE_ID = edcLink.CASE_ID AND edcLink.ITEM_TYPE = '10'  ");
			}
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_WAREHOUSE edcWarehouse ON edcLink.WAREHOUSE_ID = edcWarehouse.WAREHOUSE_ID  ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT edcContract ON edcLink.CONTRACT_ID = edcContract.CONTRACT_ID ");
			if(IAtomsConstants.YES.equals(isInstant)) { 
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_TRANSACTION_PARAMETER transPara ON transPara.CASE_ID = info.CASE_ID AND ( transPara.TRANSACTION_TYPE = 'COMMON_VM' ");
				sql.append(" or transPara.TRANSACTION_TYPE = 'COMMON_VMJ' or transPara.TRANSACTION_TYPE = 'COMMON_VMJU' )");
			} else {
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_HISTORY_CASE_TRANSACTION_PARAMETER transPara ON transPara.CASE_ID = info.CASE_ID AND ( transPara.TRANSACTION_TYPE = 'COMMON_VM' ");
				sql.append(" or transPara.TRANSACTION_TYPE = 'COMMON_VMJ' or transPara.TRANSACTION_TYPE = 'COMMON_VMJU' )");
			}
			sql.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE perAsset ON info.PERIPHERALS = perAsset.ASSET_TYPE_ID ");
			if(IAtomsConstants.YES.equals(isInstant)) { 
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_ASSET_LINK perLink  ON info.CASE_ID = perLink.CASE_ID AND perLink.ITEM_TYPE = '11' ");
			} else {
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_HISTORY_CASE_ASSET_LINK perLink  ON info.CASE_ID = perLink.CASE_ID AND perLink.ITEM_TYPE = '11' ");
			}
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT perContract ON perLink.CONTRACT_ID = perContract.CONTRACT_ID  ");
			sql.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE perAsset2 ON info.PERIPHERALS2 = perAsset2.ASSET_TYPE_ID ");
			if(IAtomsConstants.YES.equals(isInstant)) { 
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_ASSET_LINK perLink2 ON info.CASE_ID = perLink2.CASE_ID AND perLink2.ITEM_TYPE = '12' ");
			} else {
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_HISTORY_CASE_ASSET_LINK perLink2 ON info.CASE_ID = perLink2.CASE_ID AND perLink2.ITEM_TYPE = '12' ");
			}
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT perContract2 ON perLink2.CONTRACT_ID = perContract2.CONTRACT_ID  ");
			sql.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE perAsset3 ON info.PERIPHERALS3 = perAsset3.ASSET_TYPE_ID ");
			if(IAtomsConstants.YES.equals(isInstant)) { 
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_ASSET_LINK perLink3 ON info.CASE_ID = perLink3.CASE_ID AND perLink3.ITEM_TYPE = '13' ");
			} else {
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_HISTORY_CASE_ASSET_LINK perLink3 ON info.CASE_ID = perLink3.CASE_ID AND perLink3.ITEM_TYPE = '13' ");
			}
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT perContract3 ON perLink3.CONTRACT_ID = perContract3.CONTRACT_ID  ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF categoryItem ON categoryItem.BPTD_CODE = 'TICKET_TYPE' ");
		//	sql.append(" AND info.CASE_CATEGORY = categoryItem.ITEM_VALUE AND categoryItem.APPROVED_FLAG = 'Y' ");
			// 處理approvedFlag
			sql.append(" AND info.CASE_CATEGORY = categoryItem.ITEM_VALUE AND isnull(categoryItem.DELETED, 'N') = 'N' ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF caseTypeItem ON caseTypeItem.BPTD_CODE = 'TICKET_MODE' ");
		//	sql.append(" AND info.CASE_TYPE = caseTypeItem.ITEM_VALUE AND caseTypeItem.APPROVED_FLAG = 'Y'  ");
			// 處理approvedFlag
			sql.append(" AND info.CASE_TYPE = caseTypeItem.ITEM_VALUE AND isnull(caseTypeItem.DELETED, 'N') = 'N'  ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF processItem ON processItem.BPTD_CODE = 'PROCESS_TYPE' ");
		//	sql.append(" AND info.PROCESS_TYPE = processItem.ITEM_VALUE AND processItem.APPROVED_FLAG = 'Y' ");
			// 處理approvedFlag
			sql.append(" AND info.PROCESS_TYPE = processItem.ITEM_VALUE AND isnull(processItem.DELETED, 'N') = 'N' ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF installTypeItem ON installTypeItem.BPTD_CODE = 'INSTALL_TYPE'");
		//	sql.append(" AND info.INSTALL_TYPE = installTypeItem.ITEM_VALUE AND installTypeItem.APPROVED_FLAG = 'Y' ");
			// 處理approvedFlag
			sql.append(" AND info.INSTALL_TYPE = installTypeItem.ITEM_VALUE AND isnull(installTypeItem.DELETED, 'N') = 'N' ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF areaItem ON areaItem.BPTD_CODE = 'REGION'");
		//	sql.append(" AND header.AREA = areaItem.ITEM_VALUE AND areaItem.APPROVED_FLAG = 'Y' ");
			// 處理approvedFlag
			sql.append(" AND header.AREA = areaItem.ITEM_VALUE AND isnull(areaItem.DELETED, 'N') = 'N' ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF locationItem ON locationItem.BPTD_CODE = 'LOCATION'");
		//	sql.append(" AND header.LOCATION = locationItem.ITEM_VALUE AND locationItem.APPROVED_FLAG = 'Y'  ");
			// 處理approvedFlag
			sql.append(" AND header.LOCATION = locationItem.ITEM_VALUE AND isnull(locationItem.DELETED, 'N') = 'N'  ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF installLocationItem ON installLocationItem.BPTD_CODE = 'LOCATION' ");
		//	sql.append(" AND info.INSTALLED_ADRESS_LOCATION = installLocationItem.ITEM_VALUE AND installLocationItem.APPROVED_FLAG = 'Y' ");
			// 處理approvedFlag
			sql.append(" AND info.INSTALLED_ADRESS_LOCATION = installLocationItem.ITEM_VALUE AND isnull(installLocationItem.DELETED, 'N') = 'N' ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF unInstallLocationItem ON unInstallLocationItem.BPTD_CODE = 'LOCATION' ");
		//	sql.append("  AND info.CONTACT_ADDRESS_LOCATION = unInstallLocationItem.ITEM_VALUE AND unInstallLocationItem.APPROVED_FLAG = 'Y' ");
			// 處理approvedFlag
			sql.append("  AND info.CONTACT_ADDRESS_LOCATION = unInstallLocationItem.ITEM_VALUE AND isnull(unInstallLocationItem.DELETED, 'N') = 'N' ");
			sql.append(" LEFT JOIN ").append(schema).append(" .DMM_REPOSITORY_HISTORY repoEdc ON repoEdc.HISTORY_ID = edcLink.HISTORY_ASSET_ID ");
			sql.append(" WHERE 1 = 1 ");
			//查詢條件 開始時間
			if(StringUtils.hasText(queryCompleteDateStart)){
				sql.append(" AND info.COMPLETE_DATE > :startDate ");
				sql.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.STARTDATE.getValue(), queryCompleteDateStart);
				LOGGER.debug(this.getClass().getName(), ".listByDtid() --> queryCompleteDateStart: ", queryCompleteDateStart);
			}
			//查詢條件 結束時間
			if(StringUtils.hasText(queryCompleteDateEnd)){
				sql.append(" AND info.COMPLETE_DATE < dateadd( DAY , 1 , :endDate ) ");
				sql.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.ENDDATE.getValue(), queryCompleteDateEnd);
				LOGGER.debug(this.getClass().getName(), ".listByDtid() --> queryCompleteDateEnd: ", queryCompleteDateEnd);
			}
			//查詢條件 客戶id
			if(StringUtils.hasText(queryCustomerId)){
				sql.append(" AND info.CUSTOMER_ID = :customerId ");
				sql.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), queryCustomerId);
				LOGGER.debug(this.getClass().getName(), ".listByDtid() --> queryCustomerId: ", queryCustomerId);
			}
			sql.append(" AND info.CASE_CATEGORY = :uninstall ");
			sql.append(" AND info.COMPLETE_DATE is not null and info.COMPLETE_DATE <> '' ");
			//sql.append(" AND (info.CASE_STATUS = :closed or info.CASE_STATUS = :immediateClose) ");
			sql.setParameter("uninstall", IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode());
			//sql.setParameter("closed", IAtomsConstants.CASE_STATUS.CLOSED.getCode());
			//sql.setParameter("immediateClose", IAtomsConstants.CASE_STATUS.IMMEDIATE_CLOSE.getCode());
			
			//排序的兩個參數判斷是否為空
			if (StringUtils.hasText(sort) && StringUtils.hasText(order)) {
				sql.append(" ORDER BY ").append(sort).append(IAtomsConstants.MARK_SPACE).append(order);
			} else {
				sql.append(" order by info.CASE_ID ASC, customer.SHORT_NAME ASC ");
			}
			sql.setParameter("supported_function", IATOMS_PARAM_TYPE.SUPPORTED_FUNCTION.getCode());
			sql.setPageSize(pageSize);
			sql.setStartPage(pageIndex - 1);
			AliasBean alias = new AliasBean(SrmCaseHandleInfoDTO.class);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.AO_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_FINISH_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue(), TimestampType.INSTANCE);
			
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PROCESS_TYPE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_TYPE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ATTENDANCE_TIMES.getValue(), IntegerType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ATTENDANCEDESC.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.RUSHREPAIRDESC.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.AREA.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.BUSSION_ADDRESS.getValue(), StringType.INSTANCE);
			
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ASSET_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.MODEL.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.WAREHOUSE_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADDRESS.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALLED_ADDRESS.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALLED_COMPANYNAME.getValue(), StringType.INSTANCE);
			
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.TID.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ENABLE_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PROPERTY_ID.getValue(), StringType.INSTANCE);
			
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTRACT_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_CONTRACT_CODE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_CONTRACT_CODE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_CONTRACT_CODE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.FUNCTIONTYPE_LIST.getValue(), StringType.INSTANCE);
			
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALLED_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.USERDDAYS120.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.USERDDAYS90.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.DELAYTIME.getValue(), BigDecimalType.INSTANCE);
			LOGGER.debug(this.getClass().getName(), "listBy() --> sql: ", sql.toString()); 
			caseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sql, alias);
			LOGGER.debug(this.getClass().getName(), "listBy() --> sql: ", sql.toString()); 
		} catch (Exception e) {
			LOGGER.error("installListBy() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return caseHandleInfoDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#unInstallCount(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public int unInstallCount(String queryCustomerId,
			String queryCompleteDateStart, String queryCompleteDateEnd, String isInstant)
			throws DataAccessException {
		LOGGER.debug(".unInstallCount()", "parameters:queryCustomerId:", queryCustomerId);
		LOGGER.debug(".unInstallCount()", "parameters:queryCompleteDateStart:", queryCompleteDateStart);
		LOGGER.debug(".unInstallCount()", "parameters:queryCompleteDateEnd:", queryCompleteDateEnd);
		try {
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			Integer result = null;
			//查詢分頁最底下顯示的總條數
			sql.append(" SELECT COUNT(1)  ");
			if(IAtomsConstants.YES.equals(isInstant)) { 
				sql.append(" from ").append(schema).append(" .SRM_CASE_HANDLE_INFO info ");
			} else {
				sql.append(" from ").append(schema).append(" .SRM_HISTORY_CASE_HANDLE_INFO info ");
			}
			sql.append(" where 1 = 1 ");
			//查詢條件 開始時間
			if(StringUtils.hasText(queryCompleteDateStart)){
				sql.append(" AND info.COMPLETE_DATE > :startDate ");
				sql.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.STARTDATE.getValue(), queryCompleteDateStart);
				LOGGER.debug(this.getClass().getName(), ".unInstallCount() --> queryCompleteDateStart: ", queryCompleteDateStart);
			}
			//查詢條件 結束時間
			if(StringUtils.hasText(queryCompleteDateEnd)){
				sql.append(" AND info.COMPLETE_DATE < dateadd( DAY , 1 , :endDate ) ");
				sql.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.ENDDATE.getValue(), queryCompleteDateEnd);
				LOGGER.debug(this.getClass().getName(), ".unInstallCount() --> queryCompleteDateEnd: ", queryCompleteDateEnd);
			}
			//查詢條件 客戶id
			if(StringUtils.hasText(queryCustomerId)){
				sql.append(" AND info.CUSTOMER_ID = :customerId ");
				sql.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), queryCustomerId);
				LOGGER.debug(this.getClass().getName(), ".unInstallCount() --> queryCustomerId: ", queryCustomerId);
			}
			sql.append(" AND info.CASE_CATEGORY = :uninstall ");
			sql.append(" AND info.COMPLETE_DATE is not null and info.COMPLETE_DATE <> '' ");
			//sql.append(" AND (info.CASE_STATUS = :closed or info.CASE_STATUS = :immediateClose) ");
			sql.setParameter("uninstall", IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode());
			//sql.setParameter("closed", IAtomsConstants.CASE_STATUS.CLOSED.getCode());
			//sql.setParameter("immediateClose", IAtomsConstants.CASE_STATUS.IMMEDIATE_CLOSE.getCode());
			
			LOGGER.debug(".unInstallCount()", " Native SQL---------->", sql.toString());
			List<Integer> list = this.getDaoSupport().findByNativeSql(sql);
			LOGGER.debug(".unInstallCount() --> sql: ", sql.toString());
			if(!CollectionUtils.isEmpty(list)){
				result = list.get(0);
			}
			return result;
		} catch (Exception e) {
			LOGGER.error(".unInstallCount() is error", "DAO Exception", e);
			throw new DataAccessException(CoreMessageCode.QUERY_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.REB_IATOMS_TB_NAME_SUPPLIES)}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#otherListBy(java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@Override
	public List<SrmCaseHandleInfoDTO> otherListBy(String queryCustomerId, String queryCompleteDateStart, String queryCompleteDateEnd,String isInstant,
			Integer pageIndex, Integer pageSize, String sort, String order) throws DataAccessException {
		List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = null;
		try {
			LOGGER.debug("otherListBy()", "parameter:queryCustomerId=", queryCustomerId);
			LOGGER.debug("otherListBy()", "parameter:queryCompleteDateStart=", queryCompleteDateStart);
			LOGGER.debug("otherListBy()", "parameter:queryCompleteDateEnd=", queryCompleteDateEnd);
			LOGGER.debug("otherListBy()", "parameter:sort=", sort);
			LOGGER.debug("otherListBy()", "parameter:order=", order);
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append(" SELECT");
			//客戶
			sql.append(" customer.SHORT_NAME AS customerName , ");
			//需求單號
			sql.append(" info.REQUIREMENT_NO AS requirementNo, ");	
			//案件編號
			sql.append(" info.CASE_ID AS caseId, ");
			//業務人員
			sql.append(" header.AO_NAME AS aoName, ");
			//案件類別
			sql.append(" categoryItem.ITEM_NAME AS caseCategory , ");
			//案件類型
			sql.append(" caseTypeItem.ITEM_NAME AS caseType , ");
			//進件日期
			sql.append(" info.CREATED_DATE AS createdDate, ");
			//派工日期
			sql.append(" ( SELECT  MIN(transcation.DEAL_DATE) ");
			if(IAtomsConstants.YES.equals(isInstant)) { 
				sql.append(" FROM ").append(schema).append(" .SRM_CASE_TRANSACTION transcation ");
			} else {
				sql.append(" FROM ").append(schema).append(" .SRM_HISTORY_CASE_TRANSACTION transcation ");
			}
			sql.append(" WHERE transcation.CASE_ID = info.CASE_ID ");
			sql.append(" and (  transcation.ACTION_ID = 'dispatching' OR transcation.ACTION_ID = 'autoDispatching' )) AS dispatchDate , ");
			//應完修時間
			sql.append(" info.ACCEPTABLE_FINISH_DATE AS acceptableFinishDate, ");
			//實際完成日
			sql.append(" info.COMPLETE_DATE AS completeDate, ");
			//處理方式
			sql.append(" processItem.ITEM_NAME AS processType , ");
			//裝機類型
			sql.append(" installTypeItem.ITEM_NAME AS installType , ");
			//到場次數
			sql.append(" info.ATTENDANCE_TIMES AS attendanceTimes , ");
			//到場說明
			sql.append("  ( CASE  WHEN info.ATTENDANCE_TIMES > 1 ");
			sql.append("  THEN STUFF( ( ").append(" SELECT ").append(" ',' + LTRIM( attendance.DESCRIPTION ) ");
			if(IAtomsConstants.YES.equals(isInstant)) { 
				sql.append(" FROM ").append(schema).append(" .SRM_CASE_TRANSACTION attendance ");
			} else {
				sql.append(" FROM ").append(schema).append(" .SRM_HISTORY_CASE_TRANSACTION attendance ");
			}
			sql.append(" WHERE ").append("  attendance.CASE_ID = info.CASE_ID ").append(" AND attendance.ACTION_ID = 'arrive' AND attendance.DESCRIPTION <> '' FOR XML PATH('') ");
			sql.append(" ) ,1 , 1 , '' ) ELSE '' END ) AS attendanceDesc ,");
			//延期說明
			if(IAtomsConstants.YES.equals(isInstant)) { 
				sql.append(" ( SELECT TOP 1 tra.DESCRIPTION ").append(" FROM ").append(schema).append(" .SRM_CASE_TRANSACTION tra ");
			} else {
				sql.append(" ( SELECT TOP 1 tra.DESCRIPTION ").append(" FROM ").append(schema).append(" .SRM_HISTORY_CASE_TRANSACTION tra ");
			}
			sql.append(" WHERE ").append(" tra.CASE_ID = info.CASE_ID ").append(" AND tra.ACTION_ID = 'delay' ");
			sql.append(" ORDER BY  DEAL_DATE DESC ) AS rushRepairDesc , ");
			//特店代號
			sql.append(" mer.MERCHANT_CODE AS merchantCode , ");
			//特店名稱
			sql.append(" mer.NAME AS merchantName , ");
			//特店區域
			sql.append(" areaItem.ITEM_NAME AS area , ");
			//特店地址
			sql.append(" locationItem.ITEM_NAME + header.BUSINESS_ADDRESS AS bussionAddress , ");
			//刷卡機型
			sql.append(" asset.NAME AS assetName , ");
			//設備型號
			sql.append(" repoEdc.ASSET_MODEL AS model, ");
			//倉別
			sql.append(" edcWarehouse.NAME AS wareHouseName , ");
			//裝機地址
			sql.append(" installLocationItem.ITEM_NAME + info.INSTALLED_ADRESS AS installedAddress , ");
			//裝機廠商
			sql.append(" company.SHORT_NAME AS companyName , ");
			//DTID
			sql.append(" info.DTID AS dtid , ");
			//TID
			sql.append(" transPara.TID AS tid , ");
			//設備啟用日
			sql.append(" edcLink.ENABLE_DATE AS enableDate ,");
			//設備序號
			sql.append(" edcLink.SERIAL_NUMBER AS serialNumber, ");
			//財產編號
			sql.append(" ( CASE WHEN repoEdc.MA_TYPE = 'BUYOUT' ").append(" THEN repoEdc.PROPERTY_ID ");
			sql.append(" ELSE repoEdc.SIM_ENABLE_NO ");
			sql.append(" END ) AS propertyId , ");
			//合約編號
			sql.append(" edcContract.CONTRACT_CODE AS contractId, ");
			//週邊設備1
			sql.append(" perAsset.NAME AS peripheralsName, ");
			//週邊設備1序號
			sql.append(" perLink.SERIAL_NUMBER AS peripheralsSerialNumber, ");
			//週邊設備1合約編號
			sql.append(" perContract.CONTRACT_CODE AS peripheralsContractCode , ");
			//週邊設備2 
			sql.append(" perAsset2.NAME AS peripherals2Name , ");
			//週邊設備2序號
			sql.append(" perLink2.SERIAL_NUMBER AS peripherals2SerialNumber , ");
			//週邊設備2合約編號
			sql.append(" perContract2.CONTRACT_CODE AS peripherals2ContractCode , ");
			//週邊設備3 
			sql.append(" perAsset3.NAME AS peripherals3Name , ");
			//週邊設備3序號
			sql.append(" perLink3.SERIAL_NUMBER AS peripherals3SerialNumber , ");
			//週邊設備3合約編號
			sql.append(" perContract3.CONTRACT_CODE AS peripherals3ContractCode , ");
			//設備開啟功能清單
			sql.append(" STUFF( ( SELECT ',' + LTRIM( functionItem1.ITEM_NAME ) ").append(" FROM ");
			if(IAtomsConstants.YES.equals(isInstant)) { 
				sql.append(schema).append(" .SRM_CASE_ASSET_FUNCTION function1");
			} else {
				sql.append(schema).append(" .SRM_HISTORY_CASE_ASSET_FUNCTION function1");
			}
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF functionItem1 ");
			sql.append(" ON function1.FUNCTION_ID = functionItem1.ITEM_VALUE");
			sql.append(" AND functionItem1.BPTD_CODE = :supported_function");
			sql.append(" AND function1.CASE_ID = info.CASE_ID FOR XML PATH('') ) , 1 , 1 , '') AS functionTypeList , ");
			//裝機日期
			sql.append(" info.INSTALLED_DATE AS installedDate, ");
			//參數異動說明
			sql.append(" info.UPDATED_DESCRIPTION AS updatedDescription, ");
			//案件說明
			sql.append(" info.DESCRIPTION  AS description, ");
			//ECR線
			sql.append(" STUFF( (  SELECT ',' + LTRIM( CAST( assetLinkEdcLine.NUMBER AS VARCHAR ) + '-' + CAST( (assetLinkEdcLine.NUMBER * assetLinkEdcLine.PRICE) AS VARCHAR ) ) ").append(" from ");
			if(IAtomsConstants.YES.equals(isInstant)) { 
				sql.append(schema).append(" .SRM_CASE_ASSET_LINK assetLinkEdcLine ");
			} else {
				sql.append(schema).append(" .SRM_HISTORY_CASE_ASSET_LINK assetLinkEdcLine ");
			}
			sql.append(" WHERE assetLinkEdcLine.ITEM_TYPE = '20' ");
			sql.append(" AND assetLinkEdcLine.ITEM_CATEGORY = 'ECRLINE' ");
			sql.append(" AND assetLinkEdcLine.CASE_ID = info.CASE_ID FOR XML PATH('') ");
			sql.append(" ) , 1 , 1 , '') AS ecrLine , ");
			//網路線
			sql.append(" STUFF( ( SELECT ',' + LTRIM( CAST( assetLinkNetLine.NUMBER AS VARCHAR ) + '-' + CAST( (assetLinkNetLine.NUMBER * assetLinkNetLine.PRICE) AS VARCHAR ) ) ").append(" from ");
			if(IAtomsConstants.YES.equals(isInstant)) { 
				sql.append(schema).append(" .SRM_CASE_ASSET_LINK assetLinkNetLine ");
			} else {
				sql.append(schema).append(" .SRM_HISTORY_CASE_ASSET_LINK assetLinkNetLine ");
			}
			sql.append(" WHERE assetLinkNetLine.ITEM_TYPE = '20' ");
			sql.append(" AND assetLinkNetLine.ITEM_CATEGORY = 'NetworkRoute' ");
			sql.append(" AND assetLinkNetLine.CASE_ID = info.CASE_ID FOR XML PATH('') ");
			sql.append(" ) , 1 ,1 , '') AS netLine , ");
			//耗材品項、個數、費用
			sql.append(" STUFF( ( SELECT ',' + LTRIM( ss.SUPPLIES_NAME + '-' + CAST( assetLinkOther.NUMBER AS VARCHAR ) + '-' + CAST( (assetLinkOther.NUMBER * assetLinkOther.PRICE) AS VARCHAR ) )");
			if(IAtomsConstants.YES.equals(isInstant)) { 
				sql.append(" FROM ").append(schema).append(" .SRM_CASE_ASSET_LINK assetLinkOther ");
			} else {
				sql.append(" FROM ").append(schema).append(" .SRM_HISTORY_CASE_ASSET_LINK assetLinkOther ");
			}
			sql.append(" INNER JOIN ").append(schema).append(" .DMM_SUPPLIES ss ON assetLinkOther.ITEM_ID = ss.SUPPLIES_ID");
			sql.append(" WHERE assetLinkOther.ITEM_TYPE = '20' ");
			sql.append(" AND assetLinkOther.ITEM_CATEGORY <> 'NetworkRoute' ");
			sql.append(" AND assetLinkOther.ITEM_CATEGORY <> 'ECRLINE' ");
			sql.append(" AND assetLinkOther.CASE_ID = info.CASE_ID FOR XML PATH('') ");
			sql.append(" ) ,1 ,1 ,'' ) AS otherLine , ");
			//完修逾期時間
			sql.append(" ( CASE WHEN info.ACCEPTABLE_FINISH_DATE < info.COMPLETE_DATE ");
			sql.append(" THEN datediff( MINUTE , info.ACCEPTABLE_FINISH_DATE , ");
			sql.append(" info.COMPLETE_DATE )  ");
			sql.append(" ELSE NULL END ) AS delayTime ");
			if(IAtomsConstants.YES.equals(isInstant)) { 
				sql.append(" FROM ").append(schema).append(" .SRM_CASE_HANDLE_INFO info  ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BIM_COMPANY customer ON info.CUSTOMER_ID = customer.COMPANY_ID ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BIM_COMPANY company ON info.COMPANY_ID = company.COMPANY_ID ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer ON info.MERCHANT_CODE = mer.MERCHANT_ID ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT_HEADER header ON info.MERCHANT_HEADER_ID = header.MERCHANT_HEADER_ID ");
				sql.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE asset  ON info.EDC_TYPE = asset.ASSET_TYPE_ID ");
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_ASSET_LINK edcLink ON info.CASE_ID = edcLink.CASE_ID AND edcLink.ITEM_TYPE = '10' AND edcLink.IS_LINK = 'Y'");
				sql.append(" LEFT JOIN ").append(schema).append(" .BIM_WAREHOUSE edcWarehouse  ON edcLink.WAREHOUSE_ID = edcWarehouse.WAREHOUSE_ID ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT edcContract ON edcLink.CONTRACT_ID = edcContract.CONTRACT_ID ");
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_TRANSACTION_PARAMETER transPara  ON transPara.CASE_ID = info.CASE_ID AND ( transPara.TRANSACTION_TYPE = 'COMMON_VM' ");
				sql.append(" or transPara.TRANSACTION_TYPE = 'COMMON_VMJ' or transPara.TRANSACTION_TYPE = 'COMMON_VMJU' )");
				sql.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE perAsset ON info.PERIPHERALS = perAsset.ASSET_TYPE_ID ");
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_ASSET_LINK perLink ON info.CASE_ID = perLink.CASE_ID AND perLink.ITEM_TYPE = '11'  AND perLink.IS_LINK = 'Y'");
				sql.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT perContract ON perLink.CONTRACT_ID = perContract.CONTRACT_ID ");
				sql.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE perAsset2 ON info.PERIPHERALS2 = perAsset2.ASSET_TYPE_ID ");
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_ASSET_LINK perLink2 ON info.CASE_ID = perLink2.CASE_ID AND perLink2.ITEM_TYPE = '12' AND perLink2.IS_LINK = 'Y' ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT perContract2 ON perLink2.CONTRACT_ID = perContract2.CONTRACT_ID ");
				sql.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE perAsset3 ON info.PERIPHERALS3 = perAsset3.ASSET_TYPE_ID ");
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_ASSET_LINK perLink3  ON info.CASE_ID = perLink3.CASE_ID AND perLink3.ITEM_TYPE = '13' AND perLink3.IS_LINK = 'Y' ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT perContract3 ON perLink3.CONTRACT_ID = perContract3.CONTRACT_ID  ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF categoryItem ON categoryItem.BPTD_CODE = 'TICKET_TYPE' ");
			//	sql.append(" AND info.CASE_CATEGORY = categoryItem.ITEM_VALUE AND categoryItem.APPROVED_FLAG = 'Y'  ");
				// 處理approvedFlag
				sql.append(" AND info.CASE_CATEGORY = categoryItem.ITEM_VALUE AND isnull(categoryItem.DELETED, 'N') = 'N'  ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF caseTypeItem ON caseTypeItem.BPTD_CODE = 'TICKET_MODE' ");
			//	sql.append(" AND info.CASE_TYPE = caseTypeItem.ITEM_VALUE AND caseTypeItem.APPROVED_FLAG = 'Y' ");
				// 處理approvedFlag
				sql.append(" AND info.CASE_TYPE = caseTypeItem.ITEM_VALUE AND isnull(caseTypeItem.DELETED, 'N') = 'N' ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF processItem  ON processItem.BPTD_CODE = 'PROCESS_TYPE' ");
			//	sql.append(" AND info.PROCESS_TYPE = processItem.ITEM_VALUE AND processItem.APPROVED_FLAG = 'Y' ");
				// 處理approvedFlag
				sql.append(" AND info.PROCESS_TYPE = processItem.ITEM_VALUE AND isnull(processItem.DELETED, 'N') = 'N' ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF installTypeItem ON installTypeItem.BPTD_CODE = 'INSTALL_TYPE'");
			//	sql.append(" AND info.INSTALL_TYPE = installTypeItem.ITEM_VALUE AND installTypeItem.APPROVED_FLAG = 'Y' ");
				// 處理approvedFlag
				sql.append(" AND info.INSTALL_TYPE = installTypeItem.ITEM_VALUE AND isnull(installTypeItem.DELETED, 'N') = 'N' ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF areaItem ON areaItem.BPTD_CODE = 'REGION'");
			//	sql.append(" AND header.AREA = areaItem.ITEM_VALUE AND areaItem.APPROVED_FLAG = 'Y' ");
				// 處理approvedFlag
				sql.append(" AND header.AREA = areaItem.ITEM_VALUE AND isnull(areaItem.DELETED, 'N') = 'N' ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF locationItem ON locationItem.BPTD_CODE = 'LOCATION'");
			//	sql.append(" AND header.LOCATION = locationItem.ITEM_VALUE AND locationItem.APPROVED_FLAG = 'Y' ");
				// 處理approvedFlag
				sql.append(" AND header.LOCATION = locationItem.ITEM_VALUE AND isnull(locationItem.DELETED, 'N') = 'N' ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF installLocationItem ON installLocationItem.BPTD_CODE = 'LOCATION' ");
			//	sql.append(" AND info.INSTALLED_ADRESS_LOCATION = installLocationItem.ITEM_VALUE AND installLocationItem.APPROVED_FLAG = 'Y' ");
				// 處理approvedFlag
				sql.append(" AND info.INSTALLED_ADRESS_LOCATION = installLocationItem.ITEM_VALUE AND isnull(installLocationItem.DELETED, 'N') = 'N' ");
				sql.append(" LEFT JOIN ").append(schema).append(" .DMM_REPOSITORY_HISTORY repoEdc ON repoEdc.HISTORY_ID = edcLink.HISTORY_ASSET_ID ");
			} else {
				sql.append(" FROM ").append(schema).append(" .SRM_HISTORY_CASE_HANDLE_INFO info  ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BIM_COMPANY customer ON info.CUSTOMER_ID = customer.COMPANY_ID ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BIM_COMPANY company ON info.COMPANY_ID = company.COMPANY_ID ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer ON info.MERCHANT_CODE = mer.MERCHANT_ID ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT_HEADER header ON info.MERCHANT_HEADER_ID = header.MERCHANT_HEADER_ID ");
				sql.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE asset  ON info.EDC_TYPE = asset.ASSET_TYPE_ID ");
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_HISTORY_CASE_ASSET_LINK edcLink ON info.CASE_ID = edcLink.CASE_ID AND edcLink.ITEM_TYPE = '10' AND edcLink.IS_LINK = 'Y' ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BIM_WAREHOUSE edcWarehouse  ON edcLink.WAREHOUSE_ID = edcWarehouse.WAREHOUSE_ID ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT edcContract ON edcLink.CONTRACT_ID = edcContract.CONTRACT_ID ");
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_HISTORY_CASE_TRANSACTION_PARAMETER transPara  ON transPara.CASE_ID = info.CASE_ID AND ( transPara.TRANSACTION_TYPE = 'COMMON_VM' ");
				sql.append(" or transPara.TRANSACTION_TYPE = 'COMMON_VMJ' or transPara.TRANSACTION_TYPE = 'COMMON_VMJU' )");
				sql.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE perAsset ON info.PERIPHERALS = perAsset.ASSET_TYPE_ID ");
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_HISTORY_CASE_ASSET_LINK perLink ON info.CASE_ID = perLink.CASE_ID AND perLink.ITEM_TYPE = '11' AND perLink.IS_LINK = 'Y' ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT perContract ON perLink.CONTRACT_ID = perContract.CONTRACT_ID ");
				sql.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE perAsset2 ON info.PERIPHERALS2 = perAsset2.ASSET_TYPE_ID ");
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_HISTORY_CASE_ASSET_LINK perLink2 ON info.CASE_ID = perLink2.CASE_ID AND perLink2.ITEM_TYPE = '12' AND perLink2.IS_LINK = 'Y' ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT perContract2 ON perLink2.CONTRACT_ID = perContract2.CONTRACT_ID ");
				sql.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE perAsset3 ON info.PERIPHERALS3 = perAsset3.ASSET_TYPE_ID ");
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_HISTORY_CASE_ASSET_LINK perLink3  ON info.CASE_ID = perLink3.CASE_ID AND perLink3.ITEM_TYPE = '13' AND perLink3.IS_LINK = 'Y' ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT perContract3 ON perLink3.CONTRACT_ID = perContract3.CONTRACT_ID  ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF categoryItem ON categoryItem.BPTD_CODE = 'TICKET_TYPE' ");
			//	sql.append(" AND info.CASE_CATEGORY = categoryItem.ITEM_VALUE AND categoryItem.APPROVED_FLAG = 'Y'  ");
				// 處理approvedFlag
				sql.append(" AND info.CASE_CATEGORY = categoryItem.ITEM_VALUE AND isnull(categoryItem.DELETED, 'N') = 'N'  ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF caseTypeItem ON caseTypeItem.BPTD_CODE = 'TICKET_MODE' ");
			//	sql.append(" AND info.CASE_TYPE = caseTypeItem.ITEM_VALUE AND caseTypeItem.APPROVED_FLAG = 'Y' ");
				// 處理approvedFlag
				sql.append(" AND info.CASE_TYPE = caseTypeItem.ITEM_VALUE AND isnull(caseTypeItem.DELETED, 'N') = 'N' ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF processItem  ON processItem.BPTD_CODE = 'PROCESS_TYPE' ");
			//	sql.append(" AND info.PROCESS_TYPE = processItem.ITEM_VALUE AND processItem.APPROVED_FLAG = 'Y' ");
				// 處理approvedFlag
				sql.append(" AND info.PROCESS_TYPE = processItem.ITEM_VALUE AND isnull(processItem.DELETED, 'N') = 'N' ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF installTypeItem ON installTypeItem.BPTD_CODE = 'INSTALL_TYPE'");
			//	sql.append(" AND info.INSTALL_TYPE = installTypeItem.ITEM_VALUE AND installTypeItem.APPROVED_FLAG = 'Y' ");
				// 處理approvedFlag
				sql.append(" AND info.INSTALL_TYPE = installTypeItem.ITEM_VALUE AND isnull(installTypeItem.DELETED, 'N') = 'N' ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF areaItem ON areaItem.BPTD_CODE = 'REGION'");
			//	sql.append(" AND header.AREA = areaItem.ITEM_VALUE AND areaItem.APPROVED_FLAG = 'Y' ");
				// 處理approvedFlag
				sql.append(" AND header.AREA = areaItem.ITEM_VALUE AND isnull(areaItem.DELETED, 'N') = 'N' ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF locationItem ON locationItem.BPTD_CODE = 'LOCATION'");
			//	sql.append(" AND header.LOCATION = locationItem.ITEM_VALUE AND locationItem.APPROVED_FLAG = 'Y' ");
				// 處理approvedFlag
				sql.append(" AND header.LOCATION = locationItem.ITEM_VALUE AND isnull(locationItem.DELETED, 'N') = 'N' ");
				sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF installLocationItem ON installLocationItem.BPTD_CODE = 'LOCATION' ");
			//	sql.append(" AND info.INSTALLED_ADRESS_LOCATION = installLocationItem.ITEM_VALUE AND installLocationItem.APPROVED_FLAG = 'Y' ");
				// 處理approvedFlag
				sql.append(" AND info.INSTALLED_ADRESS_LOCATION = installLocationItem.ITEM_VALUE AND isnull(installLocationItem.DELETED, 'N') = 'N' ");
				sql.append(" LEFT JOIN ").append(schema).append(" .DMM_REPOSITORY_HISTORY repoEdc ON repoEdc.HISTORY_ID = edcLink.HISTORY_ASSET_ID ");
			}
			sql.append(" WHERE 1 = 1 ");
			//查詢條件 開始時間
			if(StringUtils.hasText(queryCompleteDateStart)){
				sql.append(" AND info.COMPLETE_DATE > :startDate ");
				sql.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.STARTDATE.getValue(), queryCompleteDateStart);
				LOGGER.debug(this.getClass().getName(), ".otherListBy() --> queryCompleteDateStart: ", queryCompleteDateStart);
			}
			//查詢條件 結束時間
			if(StringUtils.hasText(queryCompleteDateEnd)){
				sql.append(" AND info.COMPLETE_DATE < dateadd( DAY , 1 , :endDate ) ");
				sql.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.ENDDATE.getValue(), queryCompleteDateEnd);
				LOGGER.debug(this.getClass().getName(), ".otherListBy() --> queryCompleteDateEnd: ", queryCompleteDateEnd);
			}
			//查詢條件 客戶id
			if(StringUtils.hasText(queryCustomerId)){
				sql.append(" AND info.CUSTOMER_ID = :customerId ");
				sql.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), queryCustomerId);
				LOGGER.debug(this.getClass().getName(), ".otherListBy() --> queryCustomerId: ", queryCustomerId);
			}
			sql.append(" AND ( info.CASE_CATEGORY = :category1 OR info.CASE_CATEGORY = :category2 ) ");
			sql.append(" AND info.COMPLETE_DATE is not null and info.COMPLETE_DATE <> '' ");
			//sql.append(" AND (info.CASE_STATUS = :closed or info.CASE_STATUS = :immediateClose) ");
			sql.setParameter("category1", IAtomsConstants.CASE_CATEGORY.MERGE.getCode());
			sql.setParameter("category2", IAtomsConstants.CASE_CATEGORY.UPDATE.getCode());
			//sql.setParameter("closed", IAtomsConstants.CASE_STATUS.CLOSED.getCode());
			//sql.setParameter("immediateClose", IAtomsConstants.CASE_STATUS.IMMEDIATE_CLOSE.getCode());
			
			//排序的兩個參數判斷是否為空
			if (StringUtils.hasText(sort) && StringUtils.hasText(order)) {
				sql.append(" ORDER BY ").append(sort).append(IAtomsConstants.MARK_SPACE).append(order);
			} else {
				sql.append(" order by info.CASE_ID ASC, customer.SHORT_NAME ASC ");
			}
			sql.setParameter("supported_function", IATOMS_PARAM_TYPE.SUPPORTED_FUNCTION.getCode());
			sql.setPageSize(pageSize);
			sql.setStartPage(pageIndex - 1);
			AliasBean alias = new AliasBean(SrmCaseHandleInfoDTO.class);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.AO_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_FINISH_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue(), TimestampType.INSTANCE);
			
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PROCESS_TYPE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_TYPE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ATTENDANCE_TIMES.getValue(), IntegerType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ATTENDANCEDESC.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.RUSHREPAIRDESC.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.AREA.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.BUSSION_ADDRESS.getValue(), StringType.INSTANCE);
			
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ASSET_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.MODEL.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.WAREHOUSE_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADDRESS.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_NAME.getValue(), StringType.INSTANCE);
			
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.TID.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ENABLE_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PROPERTY_ID.getValue(), StringType.INSTANCE);
			
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTRACT_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_CONTRACT_CODE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_CONTRACT_CODE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_CONTRACT_CODE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.FUNCTIONTYPE_LIST.getValue(), StringType.INSTANCE);
			
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATED_DESCRIPTION.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.DESCRIPTION.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ECRLINE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.NETLINE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.OTHERLINE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.DELAYTIME.getValue(), BigDecimalType.INSTANCE);
			LOGGER.debug(this.getClass().getName(), "otherListBy() --> sql: ", sql.toString()); 
			caseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sql, alias);
			LOGGER.debug(this.getClass().getName(), "otherListBy() --> sql: ", sql.toString()); 
		} catch (Exception e) {
			LOGGER.error("installListBy() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return caseHandleInfoDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#otherCount(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public int otherCount(String queryCustomerId,
			String queryCompleteDateStart, String queryCompleteDateEnd,String isInstant) throws DataAccessException {
		LOGGER.debug(".otherCount()", "parameters:queryCustomerId:", queryCustomerId);
		LOGGER.debug(".otherCount()", "parameters:queryCompleteDateStart:", queryCompleteDateStart);
		LOGGER.debug(".otherCount()", "parameters:queryCompleteDateEnd:", queryCompleteDateEnd);
		try {
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			Integer result = null;
			//查詢分頁最底下顯示的總條數
			sql.append("SELECT COUNT(1)");
			if(IAtomsConstants.YES.equals(isInstant)) { 
				sql.append(" from ").append(schema).append(" .SRM_CASE_HANDLE_INFO info ");
			} else {
				sql.append(" from ").append(schema).append(" .SRM_HISTORY_CASE_HANDLE_INFO info ");
			}
			sql.append(" where 1 = 1 ");
			//查詢條件 開始時間
			if(StringUtils.hasText(queryCompleteDateStart)){
				sql.append(" AND info.COMPLETE_DATE > :startDate ");
				sql.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.STARTDATE.getValue(), queryCompleteDateStart);
				LOGGER.debug(this.getClass().getName(), ".otherCount() --> queryCompleteDateStart: ", queryCompleteDateStart);
			}
			//查詢條件 結束時間
			if(StringUtils.hasText(queryCompleteDateEnd)){
				sql.append(" AND info.COMPLETE_DATE < dateadd( DAY , 1 , :endDate ) ");
				sql.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.ENDDATE.getValue(), queryCompleteDateEnd);
				LOGGER.debug(this.getClass().getName(), ".otherCount() --> queryCompleteDateEnd: ", queryCompleteDateEnd);
			}
			//查詢條件 客戶id
			if(StringUtils.hasText(queryCustomerId)){
				sql.append(" AND info.CUSTOMER_ID = :customerId ");
				sql.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), queryCustomerId);
				LOGGER.debug(this.getClass().getName(), ".otherCount() --> queryCustomerId: ", queryCustomerId);
			}
			sql.append(" AND ( info.CASE_CATEGORY = :category1 OR info.CASE_CATEGORY = :category2 )");
			sql.append(" AND info.COMPLETE_DATE is not null and info.COMPLETE_DATE <> '' ");
			//sql.append(" AND (info.CASE_STATUS = :closed or info.CASE_STATUS = :immediateClose) ");
			sql.setParameter("category1", IAtomsConstants.CASE_CATEGORY.MERGE.getCode());
			sql.setParameter("category2", IAtomsConstants.CASE_CATEGORY.UPDATE.getCode());
			//sql.setParameter("closed", IAtomsConstants.CASE_STATUS.CLOSED.getCode());
			//sql.setParameter("immediateClose", IAtomsConstants.CASE_STATUS.IMMEDIATE_CLOSE.getCode());
			
			LOGGER.debug(".otherCount()", " Native SQL---------->", sql.toString());
			List<Integer> list = this.getDaoSupport().findByNativeSql(sql);
			LOGGER.debug(".otherCount() --> sql: ", sql.toString());
			if(!CollectionUtils.isEmpty(list)){
				result = list.get(0);
			}
			return result;
		} catch (Exception e) {
			LOGGER.error(".otherCount() is error", "DAO Exception", e);
			throw new DataAccessException(CoreMessageCode.QUERY_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.REB_IATOMS_TB_NAME_SUPPLIES)}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#checkListBy(java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@Override
	public List<SrmCaseHandleInfoDTO> checkListBy(String queryCustomerId, String queryCompleteDateStart, String queryCompleteDateEnd, String isInstant,
			Integer pageIndex, Integer pageSize, String sort, String order)
			throws DataAccessException {
		List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = null;
		try {
			LOGGER.debug("otherListBy()", "parameter:queryCustomerId=", queryCustomerId);
			LOGGER.debug("otherListBy()", "parameter:queryCompleteDateStart=", queryCompleteDateStart);
			LOGGER.debug("otherListBy()", "parameter:queryCompleteDateEnd=", queryCompleteDateEnd);
			LOGGER.debug("otherListBy()", "parameter:sort=", sort);
			LOGGER.debug("otherListBy()", "parameter:order=", order);
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			sql.append(" SELECT");
			//客戶
			sql.append(" customer.SHORT_NAME AS customerName , ");
			//需求單號
			sql.append(" info.REQUIREMENT_NO AS requirementNo, ");	
			//案件編號
			sql.append(" info.CASE_ID AS caseId, ");
			//業務人員
			sql.append(" header.AO_NAME AS aoName, ");
			//案件類別
			sql.append(" categoryItem.ITEM_NAME AS caseCategory , ");
			//案件類型
			sql.append(" caseTypeItem.ITEM_NAME AS caseType , ");
			//進件日期
			sql.append(" info.CREATED_DATE AS createdDate, ");
			//派工日期
			sql.append(" ( SELECT  MIN(transcation.DEAL_DATE) ");
			if(IAtomsConstants.YES.equals(isInstant)) { 
				sql.append(" FROM ").append(schema).append(" .SRM_CASE_TRANSACTION transcation ");
			} else {
				sql.append(" FROM ").append(schema).append(" .SRM_HISTORY_CASE_TRANSACTION transcation ");
			}
			sql.append(" WHERE transcation.CASE_ID = info.CASE_ID ");
			sql.append(" and (  transcation.ACTION_ID = 'dispatching' OR transcation.ACTION_ID = 'autoDispatching' )) AS dispatchDate , ");
			//應完修時間
			sql.append(" info.ACCEPTABLE_FINISH_DATE AS acceptableFinishDate, ");
			//實際完成日
			sql.append(" info.COMPLETE_DATE AS completeDate, ");
			//處理方式
			sql.append(" processItem.ITEM_NAME AS processType , ");
			//裝機類型
			sql.append(" installTypeItem.ITEM_NAME AS installType , ");
			//到場次數
			sql.append(" info.ATTENDANCE_TIMES AS attendanceTimes , ");
			//到場說明
			sql.append("  ( CASE  WHEN info.ATTENDANCE_TIMES > 1 ");
			sql.append("  THEN STUFF( ( ").append(" SELECT ").append(" ',' + LTRIM( attendance.DESCRIPTION ) ");
			if(IAtomsConstants.YES.equals(isInstant)) { 
				sql.append(" FROM ").append(schema).append(" .SRM_CASE_TRANSACTION attendance ");
			} else {
				sql.append(" FROM ").append(schema).append(" .SRM_HISTORY_CASE_TRANSACTION attendance ");
			}
			sql.append(" WHERE ").append("  attendance.CASE_ID = info.CASE_ID ").append(" AND attendance.ACTION_ID = 'arrive' AND attendance.DESCRIPTION <> '' FOR XML PATH('') ");
			sql.append(" ) ,1 , 1 , '' ) ELSE '' END ) AS attendanceDesc ,");
			//延期說明
			if(IAtomsConstants.YES.equals(isInstant)) { 
				sql.append(" ( SELECT TOP 1 tra.DESCRIPTION ").append(" FROM ").append(schema).append(" .SRM_CASE_TRANSACTION tra ");
			} else {
				sql.append(" ( SELECT TOP 1 tra.DESCRIPTION ").append(" FROM ").append(schema).append(" .SRM_HISTORY_CASE_TRANSACTION tra ");
			}
			sql.append(" WHERE ").append(" tra.CASE_ID = info.CASE_ID ").append(" AND tra.ACTION_ID = 'delay' ");
			sql.append(" ORDER BY  DEAL_DATE DESC ) AS rushRepairDesc , ");
			//特店代號
			sql.append(" mer.MERCHANT_CODE AS merchantCode , ");
			//特店名稱
			sql.append(" mer.NAME AS merchantName , ");
			//特店區域
			sql.append(" areaItem.ITEM_NAME AS area , ");
			//特店地址
			sql.append(" locationItem.ITEM_NAME + header.BUSINESS_ADDRESS AS bussionAddress , ");
			//刷卡機型
			sql.append(" asset.NAME AS assetName , ");
			//設備型號
			sql.append(" repoEdc.ASSET_MODEL AS model, ");
			//倉別
			sql.append(" edcWarehouse.NAME AS wareHouseName , ");
			//裝機地址
			sql.append(" installLocationItem.ITEM_NAME + info.INSTALLED_ADRESS AS installedAddress , ");
			//裝機廠商
			sql.append(" company.SHORT_NAME AS companyName , ");
			//DTID
			sql.append(" info.DTID AS dtid , ");
			//TID
			sql.append(" transPara.TID AS tid , ");
			//設備啟用日
			sql.append(" edcLink.ENABLE_DATE AS enableDate ,");
			//設備序號
			sql.append(" edcLink.SERIAL_NUMBER AS serialNumber, ");
			//財產編號
			sql.append(" ( CASE WHEN repoEdc.MA_TYPE = 'BUYOUT' ").append(" THEN repoEdc.PROPERTY_ID ");
			sql.append(" ELSE repoEdc.SIM_ENABLE_NO ");
			sql.append(" END ) AS propertyId , ");
			//合約編號
			sql.append(" edcContract.CONTRACT_CODE AS contractId ");
			if(IAtomsConstants.YES.equals(isInstant)) { 
				sql.append(" FROM ").append(schema).append(" .SRM_CASE_HANDLE_INFO info  ");
			} else {
				sql.append(" FROM ").append(schema).append(" .SRM_HISTORY_CASE_HANDLE_INFO info  ");
			}
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_COMPANY customer ON info.CUSTOMER_ID = customer.COMPANY_ID  ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_COMPANY company  ON info.COMPANY_ID = company.COMPANY_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer ON info.MERCHANT_CODE = mer.MERCHANT_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT_HEADER header ON info.MERCHANT_HEADER_ID = header.MERCHANT_HEADER_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE asset ON info.EDC_TYPE = asset.ASSET_TYPE_ID ");
			if(IAtomsConstants.YES.equals(isInstant)) { 
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_ASSET_LINK edcLink ON info.CASE_ID = edcLink.CASE_ID AND edcLink.ITEM_TYPE = '10' AND edcLink.IS_LINK = 'Y'");
			} else {
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_HISTORY_CASE_ASSET_LINK edcLink ON info.CASE_ID = edcLink.CASE_ID AND edcLink.ITEM_TYPE = '10' AND edcLink.IS_LINK = 'Y'");
			}
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_WAREHOUSE edcWarehouse  ON edcLink.WAREHOUSE_ID = edcWarehouse.WAREHOUSE_ID ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT edcContract ON edcLink.CONTRACT_ID = edcContract.CONTRACT_ID ");
			if(IAtomsConstants.YES.equals(isInstant)) { 
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_TRANSACTION_PARAMETER transPara  ON transPara.CASE_ID = info.CASE_ID AND ( transPara.TRANSACTION_TYPE = 'COMMON_VM' ");
				sql.append(" or transPara.TRANSACTION_TYPE = 'COMMON_VMJ' or transPara.TRANSACTION_TYPE = 'COMMON_VMJU' )");
			} else {
				sql.append(" LEFT JOIN ").append(schema).append(" .SRM_HISTORY_CASE_TRANSACTION_PARAMETER transPara  ON transPara.CASE_ID = info.CASE_ID AND ( transPara.TRANSACTION_TYPE = 'COMMON_VM' ");
				sql.append(" or transPara.TRANSACTION_TYPE = 'COMMON_VMJ' or transPara.TRANSACTION_TYPE = 'COMMON_VMJU' )");
			}
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF categoryItem ON categoryItem.BPTD_CODE = 'TICKET_TYPE' ");
		//	sql.append(" AND info.CASE_CATEGORY = categoryItem.ITEM_VALUE AND categoryItem.APPROVED_FLAG = 'Y' ");
			// 處理approvedFlag
			sql.append(" AND info.CASE_CATEGORY = categoryItem.ITEM_VALUE AND isnull(categoryItem.DELETED, 'N') = 'N' ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF caseTypeItem ON caseTypeItem.BPTD_CODE = 'TICKET_MODE' ");
		//	sql.append(" AND info.CASE_TYPE = caseTypeItem.ITEM_VALUE AND caseTypeItem.APPROVED_FLAG = 'Y' ");
			// 處理approvedFlag
			sql.append(" AND info.CASE_TYPE = caseTypeItem.ITEM_VALUE AND isnull(caseTypeItem.DELETED, 'N') = 'N' ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF processItem  ON processItem.BPTD_CODE = 'PROCESS_TYPE' ");
		//	sql.append(" AND info.PROCESS_TYPE = processItem.ITEM_VALUE AND processItem.APPROVED_FLAG = 'Y' ");
			// 處理approvedFlag
			sql.append(" AND info.PROCESS_TYPE = processItem.ITEM_VALUE AND isnull(processItem.DELETED, 'N') = 'N' ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF installTypeItem ON installTypeItem.BPTD_CODE = 'INSTALL_TYPE' ");
		//	sql.append(" AND info.INSTALL_TYPE = installTypeItem.ITEM_VALUE AND installTypeItem.APPROVED_FLAG = 'Y'  ");
			// 處理approvedFlag
			sql.append(" AND info.INSTALL_TYPE = installTypeItem.ITEM_VALUE AND isnull(installTypeItem.DELETED, 'N') = 'N'  ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF areaItem ON areaItem.BPTD_CODE = 'REGION'");
		//	sql.append("  AND header.AREA = areaItem.ITEM_VALUE AND areaItem.APPROVED_FLAG = 'Y' ");
			// 處理approvedFlag
			sql.append("  AND header.AREA = areaItem.ITEM_VALUE AND isnull(areaItem.DELETED, 'N') = 'N' ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF locationItem ON locationItem.BPTD_CODE = 'LOCATION' ");
		//	sql.append(" AND header.LOCATION = locationItem.ITEM_VALUE AND locationItem.APPROVED_FLAG = 'Y' ");
			// 處理approvedFlag
			sql.append(" AND header.LOCATION = locationItem.ITEM_VALUE AND isnull(locationItem.DELETED, 'N') = 'N' ");
			sql.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF installLocationItem ON installLocationItem.BPTD_CODE = 'LOCATION' ");
		//	sql.append(" AND info.INSTALLED_ADRESS_LOCATION = installLocationItem.ITEM_VALUE AND installLocationItem.APPROVED_FLAG = 'Y' ");
			// 處理approvedFlag
			sql.append(" AND info.INSTALLED_ADRESS_LOCATION = installLocationItem.ITEM_VALUE AND isnull(installLocationItem.DELETED, 'N') = 'N' ");
			sql.append(" LEFT JOIN ").append(schema).append(" .DMM_REPOSITORY_HISTORY repoEdc  ON repoEdc.HISTORY_ID = edcLink.HISTORY_ASSET_ID ");
			sql.append(" WHERE 1 = 1 ");
			//查詢條件 開始時間
			if(StringUtils.hasText(queryCompleteDateStart)){
				sql.append(" AND info.COMPLETE_DATE > :startDate ");
				sql.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.STARTDATE.getValue(), queryCompleteDateStart);
				LOGGER.debug(this.getClass().getName(), ".otherListBy() --> queryCompleteDateStart: ", queryCompleteDateStart);
			}
			//查詢條件 結束時間
			if(StringUtils.hasText(queryCompleteDateEnd)){
				sql.append(" AND info.COMPLETE_DATE < dateadd( DAY , 1 , :endDate ) ");
				sql.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.ENDDATE.getValue(), queryCompleteDateEnd);
				LOGGER.debug(this.getClass().getName(), ".otherListBy() --> queryCompleteDateEnd: ", queryCompleteDateEnd);
			}
			//查詢條件 客戶id
			if(StringUtils.hasText(queryCustomerId)){
				sql.append(" AND info.CUSTOMER_ID = :customerId ");
				sql.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), queryCustomerId);
				LOGGER.debug(this.getClass().getName(), ".otherListBy() --> queryCustomerId: ", queryCustomerId);
			}
			sql.append(" AND info.CASE_CATEGORY = :category1 ");
			//sql.append(" AND (info.CASE_STATUS = :closed or info.CASE_STATUS = :immediateClose) ");
			sql.setParameter("category1", IAtomsConstants.CASE_CATEGORY.CHECK.getCode());
			sql.append(" AND info.COMPLETE_DATE is not null and info.COMPLETE_DATE <> '' ");
			//sql.setParameter("closed", IAtomsConstants.CASE_STATUS.CLOSED.getCode());
			//sql.setParameter("immediateClose", IAtomsConstants.CASE_STATUS.IMMEDIATE_CLOSE.getCode());
			
			//排序的兩個參數判斷是否為空
			if (StringUtils.hasText(sort) && StringUtils.hasText(order)) {
				sql.append(" ORDER BY ").append(sort).append(IAtomsConstants.MARK_SPACE).append(order);
			} else {
				sql.append(" order by info.CASE_ID ASC, customer.SHORT_NAME ASC ");
			}
			sql.setPageSize(pageSize);
			sql.setStartPage(pageIndex - 1);
			AliasBean alias = new AliasBean(SrmCaseHandleInfoDTO.class);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.AO_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_FINISH_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue(), TimestampType.INSTANCE);
			
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PROCESS_TYPE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_TYPE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ATTENDANCE_TIMES.getValue(), IntegerType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ATTENDANCEDESC.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.RUSHREPAIRDESC.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.AREA.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.BUSSION_ADDRESS.getValue(), StringType.INSTANCE);
			
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ASSET_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.MODEL.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.WAREHOUSE_NAME.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADDRESS.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_NAME.getValue(), StringType.INSTANCE);
			
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.TID.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ENABLE_DATE.getValue(), TimestampType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PROPERTY_ID.getValue(), StringType.INSTANCE);
			
			alias.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTRACT_ID.getValue(), StringType.INSTANCE);
			LOGGER.debug(this.getClass().getName(), "otherListBy() --> sql: ", sql.toString()); 
			caseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sql, alias);
			LOGGER.debug(this.getClass().getName(), "otherListBy() --> sql: ", sql.toString()); 
		} catch (Exception e) {
			LOGGER.error("installListBy() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return caseHandleInfoDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#checkCount(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public int checkCount(String queryCustomerId, String queryCompleteDateStart, String queryCompleteDateEnd, String isInstant)
			throws DataAccessException {
		LOGGER.debug(".otherCount()", "parameters:queryCustomerId:", queryCustomerId);
		LOGGER.debug(".otherCount()", "parameters:queryCompleteDateStart:", queryCompleteDateStart);
		LOGGER.debug(".otherCount()", "parameters:queryCompleteDateEnd:", queryCompleteDateEnd);
		try {
			String schema = this.getMySchema();
			SqlQueryBean sql = new SqlQueryBean();
			Integer result = null;
			//查詢分頁最底下顯示的總條數
			sql.append("SELECT COUNT(1)");
			if(IAtomsConstants.YES.equals(isInstant)) { 
				sql.append(" from ").append(schema).append(" .SRM_CASE_HANDLE_INFO info ");
			} else {
				sql.append(" from ").append(schema).append(" .SRM_HISTORY_CASE_HANDLE_INFO info ");
			}
			sql.append(" where 1 = 1 ");
			//查詢條件 開始時間
			if(StringUtils.hasText(queryCompleteDateStart)){
				sql.append(" AND info.COMPLETE_DATE > :startDate ");
				sql.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.STARTDATE.getValue(), queryCompleteDateStart);
				LOGGER.debug(this.getClass().getName(), ".checkCount() --> queryCompleteDateStart: ", queryCompleteDateStart);
			}
			//查詢條件 結束時間
			if(StringUtils.hasText(queryCompleteDateEnd)){
				sql.append(" AND info.COMPLETE_DATE < dateadd( DAY , 1 , :endDate ) ");
				sql.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.ENDDATE.getValue(), queryCompleteDateEnd);
				LOGGER.debug(this.getClass().getName(), ".checkCount() --> queryCompleteDateEnd: ", queryCompleteDateEnd);
			}
			//查詢條件 客戶id
			if(StringUtils.hasText(queryCustomerId)){
				sql.append(" AND info.CUSTOMER_ID = :customerId ");
				sql.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), queryCustomerId);
				LOGGER.debug(this.getClass().getName(), ".checkCount() --> queryCustomerId: ", queryCustomerId);
			}
			sql.append(" AND info.CASE_CATEGORY = :category1 ");
			sql.append(" AND info.COMPLETE_DATE is not null and info.COMPLETE_DATE <> '' ");
			//sql.append(" AND (info.CASE_STATUS = :closed or info.CASE_STATUS = :immediateClose) ");
			sql.setParameter("category1", IAtomsConstants.CASE_CATEGORY.CHECK.getCode());
			//sql.setParameter("closed", IAtomsConstants.CASE_STATUS.CLOSED.getCode());
			//sql.setParameter("immediateClose", IAtomsConstants.CASE_STATUS.IMMEDIATE_CLOSE.getCode());
			
			LOGGER.debug(".checkCount()", " Native SQL---------->", sql.toString());
			List<Integer> list = this.getDaoSupport().findByNativeSql(sql);
			LOGGER.debug(".checkCount() --> sql: ", sql.toString());
			if(!CollectionUtils.isEmpty(list)){
				result = list.get(0);
			}
			return result;
		} catch (Exception e) {
			LOGGER.error(".otherCount() is error", "DAO Exception", e);
			throw new DataAccessException(CoreMessageCode.QUERY_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.REB_IATOMS_TB_NAME_SUPPLIES)}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#listBy(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	public List<SrmCaseHandleInfoDTO> listBy(String historyId, String companyId, String merchantCode, String dtid, String tid,
			String serialNumber, String caseId, String sort, String order, Integer pageSize, Integer pageIndex) throws DataAccessException {
		LOGGER.debug("listBy()", "historyId:", historyId);
		LOGGER.debug("listBy()", "companyId:", companyId);
		LOGGER.debug("listBy()", "merchantCode:", merchantCode);
		LOGGER.debug("listBy()", "dtid:", dtid);
		LOGGER.debug("listBy()", "tid:", tid);
		LOGGER.debug("listBy()", "serialNumber:", serialNumber);
		LOGGER.debug("listBy()", "caseId:", caseId);
		LOGGER.debug("listBy()", "sort:", sort);
		LOGGER.debug("listBy()", "pageSize:", pageSize.intValue());
		LOGGER.debug("listBy()", "order:", order);
		LOGGER.debug("listBy()", "pageIndex:", pageIndex.intValue());
		List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOs = null;
		String schema = this.getMySchema();
		SqlStatement sqlStatement = new SqlStatement();
		try {
			sqlStatement.addSelectClause("caseInfo.CUSTOMER_ID", SrmHistoryCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue());
			sqlStatement.addSelectClause("caseInfo.DTID", SrmHistoryCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("STUFF((SELECT DISTINCT ','+ ltrim(trans.TID) FROM " + schema + ".SRM_CASE_TRANSACTION_PARAMETER trans "
					.concat(" WHERE trans.CASE_ID = caseInfo.CASE_ID and trans.TID is not null and trans.TID <> '' FOR XML PATH('')), 1, 1, '')"), SrmHistoryCaseHandleInfoDTO.ATTRIBUTE.TID.getValue());
			sqlStatement.addSelectClause("caseInfo.REQUIREMENT_NO", SrmCaseHandleInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue());
			sqlStatement.addSelectClause("caseInfo.MERCHANT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_ID.getValue());
			sqlStatement.addSelectClause("merchant.MERCHANT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("merchant.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue());
			sqlStatement.addSelectClause("merchant.MERCHANT_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_ID.getValue());
			sqlStatement.addSelectClause("caseInfo.CASE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("caseInfo.COMPLETE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_FINISH_DATE.getValue());
			sqlStatement.addSelectClause("caseInfo.CREATED_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sqlStatement.addSelectClause("type.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.ASSET_NAME.getValue());
			sqlStatement.addSelectClause("repository.ASSET_TYPE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
			sqlStatement.addSelectClause("assetLink.SERIAL_NUMBER", SrmCaseHandleInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("repository.CONTRACT_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTRACT_ID.getValue());
			sqlStatement.addSelectClause("contract.CONTRACT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue());
			sqlStatement.addSelectClause("merHeader.AO_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.AO_NAME.getValue());
			sqlStatement.addSelectClause("merHeader.MERCHANT_HEADER_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_HEADER_ID.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".SRM_CASE_HANDLE_INFO caseInfo");
			fromBuffer.append(" left join ").append(schema).append(".SRM_CASE_ASSET_LINK assetLink on caseInfo.CASE_ID = assetLink.CASE_ID");
			fromBuffer.append(" left join ").append(schema).append(".DMM_REPOSITORY repository on repository.SERIAL_NUMBER = assetLink.SERIAL_NUMBER");
			fromBuffer.append(" left join ").append(schema).append(".DMM_ASSET_TYPE type on type.ASSET_TYPE_ID = repository.ASSET_TYPE_ID");
			fromBuffer.append(" left join ").append(schema).append(".BIM_CONTRACT contract on repository.CONTRACT_ID = contract.CONTRACT_ID");
			fromBuffer.append(" left join ").append(schema).append(".BIM_MERCHANT merchant on merchant.MERCHANT_ID = caseInfo.MERCHANT_CODE");
			fromBuffer.append(" left join ").append(schema).append(".BIM_MERCHANT_HEADER merHeader on merHeader.MERCHANT_HEADER_ID = caseInfo.MERCHANT_HEADER_ID");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(historyId)) {
				sqlStatement.addWhereClause("caseInfo.HISTORY_ID = :historyId", historyId);
			}
			if (StringUtils.hasText(companyId)) {
				sqlStatement.addWhereClause("caseInfo.CUSTOMER_ID = :companyId", companyId);
			}
			if (StringUtils.hasText(merchantCode)) {
				sqlStatement.addWhereClause("merchant.MERCHANT_CODE like :merchantCode", merchantCode + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(dtid)) {
				sqlStatement.addWhereClause("caseInfo.DTID like :caseDtid", dtid + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(tid)) {
				//sqlStatement.addWhereClause("trans.TID = :tid", tid);
				sqlStatement.addWhereClause("EXISTS (select 1 from " + schema + ".SRM_CASE_TRANSACTION_PARAMETER trans1 where trans1.CASE_ID = caseInfo.CASE_ID and trans1.TID like :tid)");
			}
			if (StringUtils.hasText(caseId)) {
				sqlStatement.addWhereClause("caseInfo.CASE_ID like :caseId", caseId + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(serialNumber)) {
				sqlStatement.addWhereClause("repository.SERIAL_NUMBER like :serialNumber", serialNumber + IAtomsConstants.MARK_PERCENT);
			}
			if (pageSize == -1 && !StringUtils.hasText(caseId)) {
				StringBuffer whereBuffer = new StringBuffer();
				whereBuffer.append("(select DISTINCT info.CASE_ID from ").append(schema);
				whereBuffer.append(".SRM_CASE_HANDLE_INFO info where info.CLOSE_DATE = (");
				whereBuffer.append("select max(dateInfo.CLOSE_DATE) from ").append(schema);
				whereBuffer.append(".SRM_CASE_HANDLE_INFO dateInfo ").append("left join ").append(schema).append(".SRM_CASE_ASSET_LINK link on link.CASE_ID = dateInfo.CASE_ID");
				whereBuffer.append(" where dateInfo.DTID = :dtid");
				whereBuffer.append(" AND(link.ACTION = :linkRemove OR link.ACTION = :linkLoss )");
				whereBuffer.append(" AND(dateInfo.CASE_STATUS = :caseClosed OR dateInfo.CASE_STATUS = :caseImmediateclose )");
				whereBuffer.append("))");
				sqlStatement.addWhereClause("caseInfo.CASE_ID = " + whereBuffer.toString());
			}
			sqlStatement.addWhereClause("(assetLink.ACTION = :remove or assetLink.ACTION = :loss)");
			sqlStatement.addWhereClause("(caseInfo.CASE_STATUS = :closed or caseInfo.CASE_STATUS = :immediateclose)");
			//分頁
			sqlStatement.setPageSize(pageSize.intValue());
			sqlStatement.setStartPage(pageIndex.intValue() - 1);
			if (StringUtils.hasText(sort) && StringUtils.hasText(order)) {
				sqlStatement.setOrderByExpression(sort + IAtomsConstants.MARK_SPACE + order);
			}/* else {
				sqlStatement.setOrderByExpression("caseInfo.CLOSE_DATE DESC");
			}*/
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseHandleInfoDTO.class);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if (StringUtils.hasText(tid)) {
				sqlQueryBean.setParameter("tid", tid + IAtomsConstants.MARK_PERCENT);
			}
			sqlQueryBean.setParameter("remove", IAtomsConstants.ACTION_REMOVE);
			sqlQueryBean.setParameter("loss", IAtomsConstants.ACTION_LOSS);
			sqlQueryBean.setParameter("closed", IAtomsConstants.CASE_STATUS.CLOSED.getCode());
			sqlQueryBean.setParameter("immediateclose", IAtomsConstants.CASE_STATUS.IMMEDIATE_CLOSE.getCode());
			if (pageSize == -1 && !StringUtils.hasText(caseId)) {
				sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue(), dtid);
				sqlQueryBean.setParameter("linkRemove", IAtomsConstants.ACTION_REMOVE);
				sqlQueryBean.setParameter("linkLoss", IAtomsConstants.ACTION_LOSS);
				sqlQueryBean.setParameter("caseClosed", IAtomsConstants.CASE_STATUS.CLOSED.getCode());
				sqlQueryBean.setParameter("caseImmediateclose", IAtomsConstants.CASE_STATUS.IMMEDIATE_CLOSE.getCode());
			}
			//打印SQL語句
			LOGGER.debug("listBy()", "SQL:", sqlQueryBean.toString());
			srmCaseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.debug("listBy()", "error:", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return srmCaseHandleInfoDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#getCount(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public Integer getCount(String historyId, String companyId,String merchantCode, String dtid, String tid, String serialNumber,
			String caseId) throws DataAccessException {
		LOGGER.debug("listBy()", "historyId:", historyId);
		LOGGER.debug("listBy()", "companyId:", companyId);
		LOGGER.debug("listBy()", "merchantCode:", merchantCode);
		LOGGER.debug("listBy()", "dtid:", dtid);
		LOGGER.debug("listBy()", "tid:", tid);
		LOGGER.debug("listBy()", "serialNumber:", serialNumber);
		LOGGER.debug("listBy()", "caseId:", caseId);
		Integer count = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".SRM_CASE_HANDLE_INFO caseInfo");
			fromBuffer.append(" left join ").append(schema).append(".SRM_CASE_ASSET_LINK assetLink on caseInfo.CASE_ID = assetLink.CASE_ID");
			fromBuffer.append(" left join ").append(schema).append(".DMM_ASSET_TYPE type on type.ASSET_TYPE_ID = assetLink.ITEM_ID");
			fromBuffer.append(" left join ").append(schema).append(".DMM_REPOSITORY repository on repository.SERIAL_NUMBER = assetLink.SERIAL_NUMBER");
			fromBuffer.append(" left join ").append(schema).append(".BIM_CONTRACT contract on repository.CONTRACT_ID = contract.CONTRACT_ID");
			fromBuffer.append(" left join ").append(schema).append(".BIM_MERCHANT merchant on merchant.MERCHANT_ID = caseInfo.MERCHANT_CODE");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(historyId)) {
				sqlStatement.addWhereClause("caseInfo.HISTORY_ID = :historyId", historyId);
			}
			if (StringUtils.hasText(companyId)) {
				sqlStatement.addWhereClause("caseInfo.CUSTOMER_ID = :companyId", companyId);
			}
			if (StringUtils.hasText(merchantCode)) {
				sqlStatement.addWhereClause("merchant.MERCHANT_CODE like :merchantCode", merchantCode + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(dtid)) {
				sqlStatement.addWhereClause("caseInfo.DTID like :dtid", dtid + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(tid)) {
				//sqlStatement.addWhereClause("trans.TID = :tid", tid);
				sqlStatement.addWhereClause("EXISTS (select 1 from " + schema + ".SRM_CASE_TRANSACTION_PARAMETER trans1 where trans1.CASE_ID = caseInfo.CASE_ID and trans1.TID like :tid)");
			}
			if (StringUtils.hasText(caseId)) {
				sqlStatement.addWhereClause("caseInfo.CASE_ID like :caseId", caseId + IAtomsConstants.MARK_PERCENT);
			}
			if (StringUtils.hasText(serialNumber)) {
				sqlStatement.addWhereClause("repository.SERIAL_NUMBER like :serialNumber", serialNumber + IAtomsConstants.MARK_PERCENT);
			}
			sqlStatement.addWhereClause("(assetLink.ACTION = :remove or assetLink.ACTION = :loss)");
			sqlStatement.addWhereClause("(caseInfo.CASE_STATUS = :closed or caseInfo.CASE_STATUS = :immediateclose)");
			//sqlStatement.addWhereClause("(trans.TRANSACTION_TYPE in ( 'COMMON_VM','COMMON_VMJ','COMMON_VMJU'))");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			if (StringUtils.hasText(tid)) {
				sqlQueryBean.setParameter("tid", tid + IAtomsConstants.MARK_PERCENT);
			}
			sqlQueryBean.setParameter("remove", IAtomsConstants.ACTION_REMOVE);
			sqlQueryBean.setParameter("loss", IAtomsConstants.ACTION_LOSS);
			sqlQueryBean.setParameter("closed", IAtomsConstants.CASE_STATUS.CLOSED.getCode());
			sqlQueryBean.setParameter("immediateclose", IAtomsConstants.CASE_STATUS.IMMEDIATE_CLOSE.getCode());
			//sqlQueryBean.setParameter(SrmHistoryCaseHandleInfoDTO.ATTRIBUTE.TRANSACTION_TYPE.getValue(), IAtomsConstants.TRANSACTION_CATEGORY.COMMON.getCode());
			//打印SQL語句
			LOGGER.debug("listBy()", "SQL:", sqlQueryBean.toString());
			List<Integer> list = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(list)) {
				return list.get(0);
			}
		} catch (Exception e) {
			LOGGER.debug("getCount()", "error:", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return count;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#listComplateCaseList(java.lang.String, java.util.List, java.util.Date, java.util.Date)
	 */
	@Override
	public List<SrmCaseHandleInfoDTO> listComplateCaseList(String customerId, List<String> caseStatus, List<String> commonTransactionType, Date startDate, Date endDate) throws DataAccessException {
		LOGGER.debug("listComplateCaseList()", "customerId:", customerId);
		LOGGER.debug("listComplateCaseList()", "caseStatus:", caseStatus.toString());
		LOGGER.debug("listComplateCaseList()", "caseStatus:", commonTransactionType.toString());
		LOGGER.debug("listComplateCaseList()", "startDate:", startDate.toString());
		LOGGER.debug("listComplateCaseList()", "endDate:", endDate.toString());
		List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = null;
		try{
			String schema = this.getMySchema();
			StringBuffer buffer = new StringBuffer();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("DISTINCT info.CASE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("caseCategory.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY_NAME.getValue());
			sqlStatement.addSelectClause("info.CASE_CATEGORY", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue());
			sqlStatement.addSelectClause("assetType.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.ASSET_NAME.getValue());
			sqlStatement.addSelectClause("mer.MERCHANT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("mer.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue());
			sqlStatement.addSelectClause("merAdd.ITEM_NAME + header.BUSINESS_ADDRESS", SrmCaseHandleInfoDTO.ATTRIBUTE.BUSSINESS_ADDRESS.getValue());
			sqlStatement.addSelectClause("info.CREATED_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sqlStatement.addSelectClause("info.CREATED_FINISH_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_FINISH_DATE.getValue());
			sqlStatement.addSelectClause("info.COMPLETE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue());
			sqlStatement.addSelectClause("caseType.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("info.DTID", SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("info.IS_FIRST_INSTALLED", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_FIRST_INSTALLED.getValue());
			sqlStatement.addSelectClause("contract.CONTRACT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue());
			buffer.append("select top 1 par.TID ");
			buffer.append("from ").append(schema).append(".SRM_CASE_TRANSACTION_PARAMETER par ");
			buffer.append("where ");
			buffer.append("par.CASE_ID = info.CASE_ID and par.TRANSACTION_TYPE in ( :transactionType )");
			sqlStatement.addSelectClause(buffer.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.TID.getValue());
			buffer.delete(0, buffer.length());
			buffer.append("select top 1 tra.DESCRIPTION ");
			buffer.append("from ").append(schema).append(".SRM_CASE_TRANSACTION tra ");
			buffer.append("where ");
			buffer.append("tra.CASE_ID=info.CASE_ID and tra.ACTION_ID = :caseAction order by DEAL_DATE desc");
			sqlStatement.addSelectClause(buffer.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.RUSHREPAIRDESC.getValue());
			buffer.delete(0, buffer.length());
			buffer.append(schema).append(".SRM_CASE_HANDLE_INFO info ");
			//buffer.append("left join ").append(schema).append(".BIM_COMPANY c on info.CUSTOMER_ID = c.COMPANY_ID ");
			buffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF caseCategory on caseCategory.BPTD_CODE = :caseCategory and caseCategory.ITEM_VALUE = info.CASE_CATEGORY ");
			buffer.append("left join ").append(schema).append(".DMM_ASSET_TYPE assetType on assetType.ASSET_TYPE_ID = info.EDC_TYPE ");
			buffer.append("left join ").append(schema).append(".BIM_MERCHANT mer on mer.MERCHANT_ID = info.MERCHANT_CODE ");
			buffer.append("left join ").append(schema).append(".BIM_MERCHANT_HEADER header on header.MERCHANT_HEADER_ID = info.MERCHANT_HEADER_ID ");
			buffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF merAdd on merAdd.BPTD_CODE = :location and merAdd.ITEM_VALUE = header.LOCATION ");
			buffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF caseType on caseType.BPTD_CODE = :caseType and caseType.ITEM_VALUE = info.CASE_TYPE ");
			buffer.append("left join ").append(schema).append(".SRM_CASE_ASSET_LINK assetLink on assetLink.CASE_ID = info.CASE_ID and assetLink.ITEM_TYPE = :itemType ");
			buffer.append("left join ").append(schema).append(".BIM_CONTRACT contract on contract.CONTRACT_ID = assetLink.CONTRACT_ID ");
			sqlStatement.addFromExpression(buffer.toString());
			if (startDate != null) {
				sqlStatement.addWhereClause("info.COMPLETE_DATE > :startDate", startDate);
			}
			if (endDate != null) {
				sqlStatement.addWhereClause("info.COMPLETE_DATE < :endDate", endDate);
			}
			if (CollectionUtils.isEmpty(caseStatus)) {
				sqlStatement.addWhereClause("info.CASE_STATUS in ( :caseStatus )");
			}
			sqlStatement.addWhereClause("info.CUSTOMER_ID = :customerId", customerId);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue(), IATOMS_PARAM_TYPE.TICKET_TYPE.getCode());
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.LOCATION.getValue(), IATOMS_PARAM_TYPE.LOCATION.getCode());
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE.getValue(), IATOMS_PARAM_TYPE.TICKET_MODE.getCode());
			sqlQueryBean.setParameter("transactionType", commonTransactionType);
			sqlQueryBean.setParameter("itemType", IAtomsConstants.LEAVE_CASE_STATUS_TEN);
			sqlQueryBean.setParameter("caseAction", IAtomsConstants.CASE_ACTION.DELAY.getCode());
			if (CollectionUtils.isEmpty(caseStatus)) {
				sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS.getValue(), caseStatus);
			}
			//打印SQL語句
			LOGGER.debug("listBy()", "SQL:", sqlQueryBean.toString());
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseHandleInfoDTO.class);
			caseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.debug("listComplateCaseList()", "error:", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return caseHandleInfoDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#listComplateCaseList(java.lang.String, java.util.Date, java.util.Date)
	 */
	public List<SrmCaseHandleInfoDTO> listComplateCaseList(List<String> caseStatusArray, List<String> commonTransactionTypeArray, 
			String customerId, Date startDate, Date endDate) throws DataAccessException {
		List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = null;
		LOGGER.debug("listComplateCaseList()", "parameter:customerId=", customerId);
		LOGGER.debug("listComplateCaseList()", "parameter:startDate=", startDate.toString());
		LOGGER.debug("listComplateCaseList()", "parameter:endDate=", endDate.toString());
		try{
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("select ");
			sqlQueryBean.append(" ROW_NUMBER() OVER( ORDER BY info.CASE_ID ) AS rowId , ");
			sqlQueryBean.append(" info.CASE_CATEGORY AS caseCategory , ");
			sqlQueryBean.append(" caseCategory.ITEM_NAME AS caseCategoryName , ");
			sqlQueryBean.append(" assetType.NAME AS assetName , ");
			sqlQueryBean.append(" mer.MERCHANT_CODE AS merchantCode, ");
			sqlQueryBean.append(" mer.NAME AS merchantName , ");
			sqlQueryBean.append(" merAdd.ITEM_NAME + header.BUSINESS_ADDRESS AS bussinessAddress , ");
			sqlQueryBean.append(" contactAdd.ITEM_NAME + '-' + info.CONTACT_ADDRESS AS contactAddress , ");
			sqlQueryBean.append(" info.CONTACT_IS_BUSSINESS_ADDRESS AS contactIsBussinessAddress, ");
			sqlQueryBean.append(" info.CREATED_DATE AS createdDate, ");
			sqlQueryBean.append(" info.ACCEPTABLE_FINISH_DATE AS acceptableFinishDate, ");
			sqlQueryBean.append(" info.COMPLETE_DATE AS completeDate, ");
			sqlQueryBean.append(" info.CASE_TYPE AS caseType, ");
			sqlQueryBean.append(" info.UNINSTALL_TYPE AS uninstallType, ");
			sqlQueryBean.append(" info.IS_FIRST_INSTALLED AS isFirstInstalled, ");
			sqlQueryBean.append(" info.PROCESS_TYPE as processType, ");
			sqlQueryBean.append(" info.SAME_INSTALLED as sameInstalled, ");
			sqlQueryBean.append(" caseType.ITEM_NAME AS caseTypeName , ");
			sqlQueryBean.append(" info.ATTENDANCE_TIMES  AS attendanceTimes, ");
			sqlQueryBean.append(" ( CASE WHEN info.ATTENDANCE_TIMES > 1 THEN( STUFF( ");
			sqlQueryBean.append(" ( SELECT ',' + LTRIM( tra.DESCRIPTION )  FROM dbo.SRM_CASE_TRANSACTION tra  WHERE ");
			sqlQueryBean.append(" tra.CASE_ID = info.CASE_ID AND tra.ACTION_ID = 'arrive' ");
			sqlQueryBean.append(" AND tra.DESCRIPTION != '' FOR XML PATH('') ) , 1 , 1 ,  '') ) ELSE '' END ) AS description , ");
			sqlQueryBean.append(" header.AO_NAME AS aoName, ");
			sqlQueryBean.append(" conAsset.PRICE as posPriceInFee, ");
			sqlQueryBean.append(" ( SELECT TOP 1 par.TID  FROM ").append(schema).append(" .SRM_CASE_TRANSACTION_PARAMETER par ");
			sqlQueryBean.append(" WHERE par.CASE_ID = info.CASE_ID AND par.TRANSACTION_TYPE IN ( :commonTransactionType ) ) AS tid ,");
			sqlQueryBean.append(" link.SERIAL_NUMBER AS serialNumber, ");
			//sqlQueryBean.append(" link.PROPERTY_ID AS propertyId, ");
			//財產編號
			sqlQueryBean.append(" ( CASE WHEN repoEdc.MA_TYPE = 'BUYOUT' ").append(" THEN repoEdc.PROPERTY_ID ");
			sqlQueryBean.append(" ELSE repoEdc.SIM_ENABLE_NO ");
			sqlQueryBean.append(" END ) AS propertyId , ");
			sqlQueryBean.append(" info.DTID AS dtid, ");
			sqlQueryBean.append(" info.CASE_ID AS caseId , ");
			sqlQueryBean.append(" info.INSTALLED_ADRESS + info.INSTALLED_ADRESS_LOCATION AS installedAdress , ");
			sqlQueryBean.append(" con.CONTRACT_CODE AS contractCode, ");
			sqlQueryBean.append(" ( SELECT TOP 1 tra.DESCRIPTION FROM ").append(schema).append(" .SRM_CASE_TRANSACTION tra ");
			sqlQueryBean.append(" WHERE tra.CASE_ID = info.CASE_ID AND tra.ACTION_ID = :delay ");
			sqlQueryBean.append(" ORDER BY DEAL_DATE DESC) AS rushRepairDesc, ");
			//周邊異動
			sqlQueryBean.append(" (CASE  WHEN info. CASE_CATEGORY =:update ");
			sqlQueryBean.append("  THEN dbo.ufun_CasePeripheralsIsUpdated( info.DTID , ");
			sqlQueryBean.append(" info.CREATED_DATE ,  info.PERIPHERALS , info.PERIPHERALS2 , info.PERIPHERALS3 ");
			sqlQueryBean.append("  ) ELSE '' END ) AS peripheralsUpdate ");
			
			sqlQueryBean.append(" FROM ").append(schema).append(" .SRM_CASE_HANDLE_INFO info ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_COMPANY c  ON info.CUSTOMER_ID = c.COMPANY_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF caseCategory ON caseCategory.BPTD_CODE = 'TICKET_TYPE' ");
			sqlQueryBean.append(" AND caseCategory.ITEM_VALUE = info.CASE_CATEGORY ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE assetType ON assetType.ASSET_TYPE_ID = info.EDC_TYPE ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer  ON mer.MERCHANT_ID = info.MERCHANT_CODE ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT_HEADER header ON header.MERCHANT_HEADER_ID = info.MERCHANT_HEADER_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF merAdd ON merAdd.BPTD_CODE = 'LOCATION' AND merAdd.ITEM_VALUE = header.LOCATION ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF contactAdd ON contactAdd.BPTD_CODE = 'LOCATION'  AND contactAdd.ITEM_VALUE = info.CONTACT_ADDRESS_LOCATION  ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF caseType  ON caseType.BPTD_CODE = 'TICKET_MODE' AND caseType.ITEM_VALUE = info.CASE_TYPE  ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_ASSET_LINK link ON info.CASE_ID = link.CASE_ID AND link.ITEM_TYPE = '10' ");
			sqlQueryBean.append(" and (link.IS_LINK = 'Y' or (link.IS_LINK = 'D' and info.CASE_CATEGORY = :uninstall )) ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .DMM_REPOSITORY_HISTORY repoEdc ON repoEdc.HISTORY_ID = link.HISTORY_ASSET_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT con ON link.CONTRACT_ID = con.CONTRACT_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT_ASSET conAsset ON conAsset.CONTRACT_ID = link.CONTRACT_ID AND conAsset.ASSET_TYPE_ID = info.EDC_TYPE");

			sqlQueryBean.append(" WHERE ");
			sqlQueryBean.append(" info.COMPLETE_DATE >= :startDate");
			sqlQueryBean.append(" AND info.COMPLETE_DATE < :endDate ");
			sqlQueryBean.append(" AND info.CASE_STATUS IN (:caseStatus) ");
			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND c.COMPANY_ID = :customerId ");
				sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
				LOGGER.debug(this.getClass().getName(), ".listComplateCaseList() --> customerId: ", customerId);
			}
			sqlQueryBean.setParameter("startDate", startDate);
			sqlQueryBean.setParameter("endDate", endDate);
			sqlQueryBean.setParameter("update", IAtomsConstants.CASE_CATEGORY.UPDATE.getCode());
			sqlQueryBean.setParameter("delay", IAtomsConstants.CASE_ACTION.DELAY.getCode());
			sqlQueryBean.setParameter("uninstall", IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode());
			sqlQueryBean.setParameter("commonTransactionType", commonTransactionTypeArray);
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS.getValue(), caseStatusArray);
			AliasBean aliasBean = new AliasBean(SrmCaseHandleInfoDTO.class);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ROWID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_IS_BUSSINESS_ADDRESS.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALL_TYPE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PROCESS_TYPE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.SAME_INSTALLED.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.IS_FIRST_INSTALLED.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ASSET_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.BUSSINESS_ADDRESS.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_FINISH_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ATTENDANCE_TIMES.getValue(), IntegerType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.DESCRIPTION.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.AO_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.POSPRICE_INFEE.getValue(), IntegerType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.TID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PROPERTY_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.RUSHREPAIRDESC.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_UPDATE.getValue(), StringType.INSTANCE);
			LOGGER.debug("listComplateCaseList()", "sql:", sqlQueryBean.toString());
			caseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listComplateCaseList() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return caseHandleInfoDTOs;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseNewHandleInfoDAO#deleteTransferData()
	 */
	@Override
	public void deleteTransferData() throws DataAccessException {
		try {
			String schema = this.getMySchema();
			SqlQueryBean sqlQueryBean = new SqlQueryBean("delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_ASSET_FUNCTION; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_ASSET_LINK; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_TRANSACTION_PARAMETER; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_TRANSACTION; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_COMM_MODE; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_ATT_FILE; ");
			
			sqlQueryBean.append(" delete ");
			sqlQueryBean.append(schema).append(".SRM_CASE_HANDLE_INFO; ");
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
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#isChangeAsset(java.lang.String)
	 */
	@Override
	public boolean isChangeAsset(String caseId) throws ServiceException {
		boolean result = false;
		try {
			String schema = this.getMySchema();
			StringBuilder stringBuilder = new StringBuilder();
			SqlStatement sqlStatement = new SqlStatement();
			sqlStatement.addSelectClause("count(1)");
			
			stringBuilder.append(schema).append(".SRM_CASE_HANDLE_INFO info left join ");
			stringBuilder.append(schema).append(".SRM_CASE_NEW_HANDLE_INFO newInfo on info.DTID = newInfo.DTID ");
			sqlStatement.addFromExpression(stringBuilder.toString());
			
			stringBuilder.delete(0, stringBuilder.length());
			stringBuilder.append("info.CASE_ID =:caseId");
			stringBuilder.append(" and (isnull(info.PERIPHERALS,'') <> isnull(newInfo.PERIPHERALS,'')");
			stringBuilder.append("or isnull(info.PERIPHERALS2,'') <> isnull(newInfo.PERIPHERALS2,'') ");
			stringBuilder.append("or isnull(info.PERIPHERALS3,'') <> isnull(newInfo.PERIPHERALS3,''))");
			sqlStatement.addWhereClause(stringBuilder.toString());
			
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			sqlQueryBean.setParameter("caseId", caseId);
			
			LOGGER.debug("isChangeAsset()", "sql:" + sqlQueryBean.toString());
			List<Integer> resultCount = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(resultCount) && resultCount.get(0) != 0) {
				result = true;
			}
		} catch (Exception e) {
			LOGGER.error("isChangeAsset()", e.getMessage(), e);
			throw new DataAccessException(IAtomsMessageCode.DATA_ACCESS_FAILURE);
		}
		return result;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#getCaseListInGreenWorld(java.util.List, java.util.List, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<SrmCaseHandleInfoDTO> getCaseListInGreenWorld(List<String> caseStatusArray,  List<String> commonTransactionType,
			String customerId, Date startDate, Date endDate, boolean isTaiXin, String TaiXin) throws DataAccessException {
		List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = null;
		LOGGER.debug("getCaseListInGreenWorld()", "parameter:customerId=", customerId);
		LOGGER.debug("getCaseListInGreenWorld()", "parameter:startDate=", startDate.toString());
		LOGGER.debug("getCaseListInGreenWorld()", "parameter:endDate=", endDate.toString());
		try{
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("select ");
			sqlQueryBean.append(" ROW_NUMBER() OVER( ORDER BY info.CASE_CATEGORY,info.COMPLETE_DATE  ) AS rowId , ");
			sqlQueryBean.append(" info.CASE_CATEGORY AS caseCategory , ");
			sqlQueryBean.append(" caseCategory.ITEM_NAME AS caseCategoryName , ");
			sqlQueryBean.append(" assetType.NAME AS assetName , ");
			sqlQueryBean.append(" mer.MERCHANT_CODE AS merchantCode, ");
			sqlQueryBean.append(" mer.NAME AS merchantName , ");
			sqlQueryBean.append(" merAdd.ITEM_NAME + header.BUSINESS_ADDRESS AS bussinessAddress , ");
			sqlQueryBean.append(" installAdd.ITEM_NAME + '-' + info.INSTALLED_ADRESS AS installedAdress , ");
			sqlQueryBean.append(" contactAdd.ITEM_NAME + '-' + info.CONTACT_ADDRESS AS contactAddress , ");
			sqlQueryBean.append(" info.CONTACT_IS_BUSSINESS_ADDRESS AS contactIsBussinessAddress, ");
			sqlQueryBean.append(" info.CREATED_DATE AS createdDate, ");
			
			sqlQueryBean.append(" info.CREATED_FINISH_DATE AS createdFinishDate, ");
			sqlQueryBean.append(" info.SAME_INSTALLED as sameInstalled, ");
			sqlQueryBean.append(" info.COMPLETE_DATE AS completeDate, ");
			sqlQueryBean.append(" info.UNINSTALL_TYPE AS uninstallType, ");
			sqlQueryBean.append(" info.INSTALL_TYPE AS installType, ");
			sqlQueryBean.append(" info.CASE_TYPE AS caseType, ");
			sqlQueryBean.append(" info.PROCESS_TYPE as processType, ");
			sqlQueryBean.append(" caseType.ITEM_NAME AS caseTypeName , ");
			//首裝買斷機
			sqlQueryBean.append(" ( CASE WHEN info.CASE_CATEGORY = 'INSTALL'  AND info.IS_FIRST_INSTALLED = 'Y' ");
			sqlQueryBean.append("  AND EXISTS( SELECT  1 FROM dbo.DMM_REPOSITORY_HISTORY repoH ");
			sqlQueryBean.append("  WHERE repoH.HISTORY_ID = link.HISTORY_ASSET_ID AND repoH.MA_TYPE = 'BUYOUT' ");
			sqlQueryBean.append(" AND repoH.ASSET_MODEL NOT  LIKE :chat) THEN 'Y' ELSE 'N'  END ) AS firstInstall , ");
			//tid
			sqlQueryBean.append(" ( SELECT TOP 1 par.TID  FROM ").append(schema).append(" .SRM_CASE_TRANSACTION_PARAMETER par ");
			sqlQueryBean.append(" WHERE par.CASE_ID = info.CASE_ID AND par.TRANSACTION_TYPE IN ( :commonTransactionType ) ) AS tid ,");
			//首拆買斷機
			sqlQueryBean.append(" ( CASE WHEN info.CASE_CATEGORY = 'UNINSTALL'  AND info.IS_FIRST_INSTALLED = 'Y' ");
			sqlQueryBean.append("  AND EXISTS( SELECT  1 FROM dbo.DMM_REPOSITORY_HISTORY repoH ");
			sqlQueryBean.append("  WHERE repoH.HISTORY_ID = link.HISTORY_ASSET_ID AND repoH.MA_TYPE = 'BUYOUT' ");
			sqlQueryBean.append(" AND repoH.ASSET_MODEL NOT  LIKE :chat ) THEN 'Y' ELSE 'N'  END ) AS firstUnInstall , ");
			
			sqlQueryBean.append(" info.ATTENDANCE_TIMES  AS attendanceTimes, ");
			sqlQueryBean.append(" ( CASE WHEN info.ATTENDANCE_TIMES > 1 THEN( STUFF( ");
			sqlQueryBean.append(" ( SELECT ',' + LTRIM( tra.DESCRIPTION ) FROM dbo.SRM_CASE_TRANSACTION tra  WHERE ");
			sqlQueryBean.append(" tra.CASE_ID = info.CASE_ID AND tra.ACTION_ID = 'arrive' ");
			sqlQueryBean.append(" AND tra.DESCRIPTION != '' FOR XML PATH('') ) , 1 , 1 ,  '') ) ELSE '' END ) AS description , ");
			sqlQueryBean.append(" repoEdc.ASSET_MODEL AS model , ");
			sqlQueryBean.append(" link.SERIAL_NUMBER AS serialNumber, ");
			sqlQueryBean.append(" (select repo.ASSET_OWNER from  ").append(schema);
			sqlQueryBean.append(" .DMM_REPOSITORY repo where repo.SERIAL_NUMBER = link.SERIAL_NUMBER ) AS assetOwner ,") ;
			//sqlQueryBean.append(" link.PROPERTY_ID AS propertyId, ");
			sqlQueryBean.append(" (CASE WHEN repo.MA_TYPE = 'BUYOUT' THEN repo.PROPERTY_ID ");
			sqlQueryBean.append(" ELSE repo.SIM_ENABLE_NO END ) AS propertyId , ");
			sqlQueryBean.append(" info.DTID AS dtid, ");
			sqlQueryBean.append(" info.CASE_ID AS caseId , ");
			sqlQueryBean.append(" ( SELECT TOP 1 tra.DESCRIPTION FROM ").append(schema).append(" .SRM_CASE_TRANSACTION tra ");
			sqlQueryBean.append(" WHERE tra.CASE_ID = info.CASE_ID AND tra.ACTION_ID = :delay ");
			sqlQueryBean.append(" ORDER BY DEAL_DATE DESC) AS rushRepairDesc, ");
			sqlQueryBean.append(" (CASE  WHEN info. CASE_CATEGORY =:update ");
			sqlQueryBean.append("  THEN dbo.ufun_CasePeripheralsIsUpdated( info.DTID , ");
			sqlQueryBean.append(" info.CREATED_DATE ,  info.PERIPHERALS , info.PERIPHERALS2 , info.PERIPHERALS3 ");
			sqlQueryBean.append("  ) ELSE '' END ) AS PeripheralsUpdate ");
			sqlQueryBean.append(" FROM ").append(schema).append(" .SRM_CASE_HANDLE_INFO info ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF caseCategory ON caseCategory.BPTD_CODE = 'TICKET_TYPE' ");
			sqlQueryBean.append(" AND caseCategory.ITEM_VALUE = info.CASE_CATEGORY ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE assetType ON assetType.ASSET_TYPE_ID = info.EDC_TYPE  ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer  ON mer.MERCHANT_ID = info.MERCHANT_CODE ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT_HEADER header ON header.MERCHANT_HEADER_ID = info.MERCHANT_HEADER_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF merAdd ON merAdd.BPTD_CODE = :location AND merAdd.ITEM_VALUE = header.LOCATION ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF installAdd ON installAdd.BPTD_CODE = :location  AND installAdd.ITEM_VALUE = info.INSTALLED_ADRESS_LOCATION  ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF contactAdd ON contactAdd.BPTD_CODE = :location  AND contactAdd.ITEM_VALUE = info.CONTACT_ADDRESS_LOCATION  ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF caseType ON caseType.BPTD_CODE = :ticket_mode AND caseType.ITEM_VALUE = info.CASE_TYPE ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_ASSET_LINK link ON info.CASE_ID = link.CASE_ID AND link.ITEM_TYPE = '10' ");
			sqlQueryBean.append(" AND( link.IS_LINK = 'Y' OR( link. IS_LINK = 'D' AND info. CASE_CATEGORY = :uninstall)) ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .DMM_REPOSITORY_HISTORY repoEdc ON repoEdc.HISTORY_ID = link.HISTORY_ASSET_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .DMM_REPOSITORY repo ON repo.SERIAL_NUMBER = link.SERIAL_NUMBER ");
			sqlQueryBean.append(" WHERE ");
			sqlQueryBean.append(" info.COMPLETE_DATE >= :startDate");
			sqlQueryBean.append(" AND info.COMPLETE_DATE < :endDate ");
			sqlQueryBean.append(" AND info.CASE_STATUS IN (:caseStatus) ");
			if(isTaiXin) {
//				sqlQueryBean.append(" AND repo.ASSET_OWNER = :taixin ");
//				sqlQueryBean.setParameter("taixin", TaiXin);
//				LOGGER.debug(this.getClass().getName(), ".getCaseListInGreenWorld() --> customerId: ", TaiXin);
			}
			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND info.CUSTOMER_ID = :customerId ");
				sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
				LOGGER.debug(this.getClass().getName(), ".getCaseListInGreenWorld() --> customerId: ", customerId);
			}
			sqlQueryBean.append(" order by info.CASE_CATEGORY,info.COMPLETE_DATE ");
			sqlQueryBean.setParameter("startDate", startDate);
			sqlQueryBean.setParameter("endDate", endDate);
			sqlQueryBean.setParameter("delay", IAtomsConstants.CASE_ACTION.DELAY.getCode());
			sqlQueryBean.setParameter("uninstall", IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode());
			sqlQueryBean.setParameter("update", IAtomsConstants.CASE_CATEGORY.UPDATE.getCode());
			sqlQueryBean.setParameter("location", IATOMS_PARAM_TYPE.LOCATION.getCode());
			sqlQueryBean.setParameter("ticket_mode", IATOMS_PARAM_TYPE.TICKET_MODE.getCode());
			sqlQueryBean.setParameter("chat", IAtomsConstants.MARK_PERCENT + "聯天" + IAtomsConstants.MARK_PERCENT);
			sqlQueryBean.setParameter("commonTransactionType", commonTransactionType);
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS.getValue(), caseStatusArray);
			AliasBean aliasBean = new AliasBean(SrmCaseHandleInfoDTO.class);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ROWID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ASSET_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ASSET_OWNER.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.SAME_INSTALLED.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PROCESS_TYPE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.BUSSINESS_ADDRESS.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_IS_BUSSINESS_ADDRESS.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_FINISH_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALL_TYPE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_TYPE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.FIRST_INSTALL.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.FIRST_UNINSTALL.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ATTENDANCE_TIMES.getValue(), IntegerType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.DESCRIPTION.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.MODEL.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PROPERTY_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.TID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.RUSHREPAIRDESC.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_UPDATE.getValue(), StringType.INSTANCE);
			LOGGER.debug("getCaseListInGreenWorld()", "sql:", sqlQueryBean.toString());
			caseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("getCaseListInGreenWorld() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return caseHandleInfoDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#listCaseWorkDetailForGp(java.util.List, java.lang.String, java.util.Date, java.util.Date, java.util.List)
	 */
	@Override
	public List<SrmCaseHandleInfoDTO> listCaseWorkDetailForGp(List<String> caseStatus, Map<String, Integer> priceMap, String customerId, Date startDate,
			Date endDate, List<String> transactionTypes)
			throws DataAccessException {
		List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = null;
		LOGGER.debug("listCaseWorkDetailForGp()", "parameter:caseStatus=", caseStatus.toString());
		LOGGER.debug("listCaseWorkDetailForGp()", "parameter:startDate=", startDate.toString());
		LOGGER.debug("listCaseWorkDetailForGp()", "parameter:endDate=", endDate.toString());
		LOGGER.debug("listCaseWorkDetailForGp()", "parameter:customerId=", customerId);
		LOGGER.debug("listCaseWorkDetailForGp()", "parameter:transactionTypes=", transactionTypes.toString());
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("ROW_NUMBER() over (order by info.CASE_ID)", SrmCaseHandleInfoDTO.ATTRIBUTE.ROW_INDEX.getValue());
			sqlStatement.addSelectClause("caseCategory.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY_NAME.getValue());
			sqlStatement.addSelectClause("info.CASE_CATEGORY", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue());
			sqlStatement.addSelectClause("info.REQUIREMENT_NO", SrmCaseHandleInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue());
			sqlStatement.addSelectClause("info.CASE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("assetType.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.ASSET_NAME.getValue());
			sqlStatement.addSelectClause("header.AO_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.AO_NAME.getValue());
			sqlStatement.addSelectClause("mer.MERCHANT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("mer.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue());
			sqlStatement.addSelectClause("info.DTID", SrmCaseHandleInfoDTO.ATTRIBUTE.TID.getValue());
			sqlStatement.addSelectClause("info.CREATED_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sqlStatement.addSelectClause("info.CREATED_FINISH_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_FINISH_DATE.getValue());
			sqlStatement.addSelectClause("info.COMPLETE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue());
			sqlStatement.addSelectClause("merAdd.ITEM_NAME + header.BUSINESS_ADDRESS", SrmCaseHandleInfoDTO.ATTRIBUTE.BUSSINESS_ADDRESS.getValue());
			sqlStatement.addSelectClause("info.DESCRIPTION", SrmCaseHandleInfoDTO.ATTRIBUTE.DESCRIPTION.getValue());
			sqlStatement.addSelectClause("info.CASE_TYPE", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE.getValue());
			sqlStatement.addSelectClause("caseType.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("processType.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.PROCESS_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("(case when link.ITEM_CATEGORY = 'ECRLINE' and link.\"NUMBER\" is not  null and link.PRICE is not  null  then (link.\"NUMBER\" * link.PRICE) else 0 end )", 
					SrmCaseHandleInfoDTO.ATTRIBUTE.ECR_LINE_IN_FEE.getValue());
			sqlStatement.addSelectClause("(case when link.ITEM_CATEGORY = 'NetworkRoute'  and link.\"NUMBER\" is not null and link.PRICE is not null  then (link.\"NUMBER\" * link.PRICE) else 0 end )", 
					SrmCaseHandleInfoDTO.ATTRIBUTE.NET_LINE_IN_FEE.getValue());
			
			if (!CollectionUtils.isEmpty(priceMap)) {
				sqlStatement.addSelectClause("(case when info.CASE_TYPE = :fastType then :fastPrice when info.CASE_TYPE = :extraType then :extraPrice else '' end )", 
						SrmCaseHandleInfoDTO.ATTRIBUTE.FEE_PRICE.getValue());
			}
			sqlStatement.addSelectClause("(case when info. CASE_CATEGORY = :update then dbo.ufun_CasePeripheralsIsUpdated(info.DTID,info.CREATED_DATE,info.PERIPHERALS,info.PERIPHERALS2,info.PERIPHERALS3) else '' end)",
					SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_UPDATE.getValue());
			StringBuffer fromBuffer = new StringBuffer();
			fromBuffer.append(schema).append(".SRM_CASE_HANDLE_INFO info ");
			//fromBuffer.append("left join ").append(schema).append(".BIM_COMPANY c on info.CUSTOMER_ID = c.COMPANY_ID ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF caseCategory on caseCategory.BPTD_CODE = :caseCategory and caseCategory.ITEM_VALUE = info.CASE_CATEGORY ");
			fromBuffer.append("left join ").append(schema).append(".DMM_ASSET_TYPE assetType on assetType.ASSET_TYPE_ID = info.EDC_TYPE ");
			fromBuffer.append("left join ").append(schema).append(".BIM_MERCHANT mer on mer.MERCHANT_ID = info.MERCHANT_CODE ");
			fromBuffer.append("left join ").append(schema).append(".BIM_MERCHANT_HEADER header on header.MERCHANT_HEADER_ID = info.MERCHANT_HEADER_ID ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF merAdd on merAdd.BPTD_CODE = :location and merAdd.ITEM_VALUE = header.LOCATION ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF processType on processType.BPTD_CODE = :processType and processType.ITEM_VALUE = info.PROCESS_TYPE ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF caseType on caseType.BPTD_CODE = :caseType and caseType.ITEM_VALUE = info.CASE_TYPE ");
			fromBuffer.append("left join ").append(schema).append(".SRM_CASE_ASSET_LINK link on link.CASE_ID = info.CASE_ID and link.ITEM_TYPE = '20' and link.ITEM_CATEGORY in ('ECRLINE', 'NetworkRoute') ");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (startDate != null) {
				sqlStatement.addWhereClause("info.COMPLETE_DATE >= :startDate", startDate);
			}
			if (endDate != null) {
				sqlStatement.addWhereClause("info.COMPLETE_DATE < :endDate", endDate);
			}
			if (!CollectionUtils.isEmpty(caseStatus)) {
				sqlStatement.addWhereClause("info.CASE_STATUS in ( :caseStatus )");
			}
			if (StringUtils.hasText(customerId)) {
				sqlStatement.addWhereClause("info.CUSTOMER_ID = :customerId", customerId);
			}
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseHandleInfoDTO.class);
			sqlQueryBean.setParameter("update", IAtomsConstants.CASE_CATEGORY.UPDATE.getCode());
			sqlQueryBean.setParameter("location", IATOMS_PARAM_TYPE.LOCATION.getCode());
			sqlQueryBean.setParameter("caseType", IATOMS_PARAM_TYPE.TICKET_MODE.getCode());
			sqlQueryBean.setParameter("caseCategory", IATOMS_PARAM_TYPE.TICKET_TYPE.getCode());
			sqlQueryBean.setParameter("processType", IATOMS_PARAM_TYPE.PROCESS_TYPE.getCode());
			if (!CollectionUtils.isEmpty(caseStatus)) {
				sqlQueryBean.setParameter("caseStatus", caseStatus);
			}
			if (!CollectionUtils.isEmpty(priceMap)) {
				sqlQueryBean.setParameter("fastType", IAtomsConstants.TICKET_MODE_FAST);
				sqlQueryBean.setParameter("fastPrice", priceMap.get(IAtomsConstants.TICKET_MODE_FAST));
				sqlQueryBean.setParameter("extraType", IAtomsConstants.TICKET_MODE_EXTRA);
				sqlQueryBean.setParameter("extraPrice", priceMap.get(IAtomsConstants.TICKET_MODE_EXTRA));
			}
			LOGGER.debug("listCaseWorkDetailForGp()", "sql:", sqlQueryBean.toString());
			caseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("listCaseWorkDetailForGp() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return caseHandleInfoDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#getCaseListInScsb(java.util.List, java.lang.String, java.util.Date, java.util.Date, java.util.List)
	 */
	@Override
	public List<SrmCaseHandleInfoDTO> getCaseListInScsb(List<String> caseStatusArray, String customerId, 
			Date startDate, Date endDate, List<String> transactionTypes) throws DataAccessException {
		List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = null;
		LOGGER.debug("getCaseListInScsb()", "parameter:customerId=", customerId);
		LOGGER.debug("getCaseListInScsb()", "parameter:startDate=", startDate.toString());
		LOGGER.debug("getCaseListInScsb()", "parameter:endDate=", endDate.toString());
		try{
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("select ");
			sqlQueryBean.append(" ROW_NUMBER() OVER( ORDER BY info.CASE_ID ) AS rowId , ");
			sqlQueryBean.append(" info.CASE_CATEGORY AS caseCategory , ");
			sqlQueryBean.append(" caseCategory.ITEM_NAME AS caseCategoryName , ");
			sqlQueryBean.append(" info.UNINSTALL_TYPE AS uninstallType, ");
			sqlQueryBean.append(" info.PROCESS_TYPE as processType, ");
			sqlQueryBean.append(" assetType.NAME AS assetName , ");
			sqlQueryBean.append(" mer.MERCHANT_CODE AS merchantCode, ");
			sqlQueryBean.append(" mer.NAME AS merchantName , ");
			sqlQueryBean.append(" merAdd.ITEM_NAME + header.BUSINESS_ADDRESS AS bussinessAddress , ");
			sqlQueryBean.append(" info.CREATED_DATE AS createdDate, ");
			sqlQueryBean.append(" info.CREATED_FINISH_DATE AS createdFinishDate, ");
			sqlQueryBean.append(" info.COMPLETE_DATE AS completeDate, ");
			sqlQueryBean.append(" caseType.ITEM_NAME AS caseTypeName , ");
			sqlQueryBean.append(" info.ATTENDANCE_TIMES  AS attendanceTimes, ");
			sqlQueryBean.append(" info.INSTALLED_DATE AS installedDate , ");
			sqlQueryBean.append(" info.CASE_TYPE AS caseType , ");
			sqlQueryBean.append(" info.SAME_INSTALLED AS sameInstalled , ");
			//未滿三個月拆機費
			sqlQueryBean.append(" (CASE WHEN info.CASE_CATEGORY = :uninstall ");
			sqlQueryBean.append(" THEN ( CASE WHEN dateadd(  DAY , 90 , info.INSTALLED_DATE ) > info.UNINSTALLED_DATE THEN 'Y'ELSE '' END) ELSE '' END ) AS userdDays90 , ");
			
			sqlQueryBean.append(" info.DTID AS dtid, ");
			//tid
			sqlQueryBean.append(" ( SELECT TOP 1 par.TID ");
			sqlQueryBean.append(" from ").append(schema).append(" .SRM_CASE_TRANSACTION_PARAMETER par ");
			sqlQueryBean.append(" WHERE par.CASE_ID = info.CASE_ID AND par.TRANSACTION_TYPE IN( :transactionType ) ) AS tid , ");
			//設備序號
			sqlQueryBean.append(" link.SERIAL_NUMBER AS serialNumber, ");
			sqlQueryBean.append(" info.CASE_ID AS caseId , ");
			//延期說明
			sqlQueryBean.append(" ( SELECT TOP 1 tra.DESCRIPTION FROM ").append(schema).append(" .SRM_CASE_TRANSACTION tra ");
			sqlQueryBean.append(" WHERE tra.CASE_ID = info.CASE_ID AND tra.ACTION_ID = :delay ");
			sqlQueryBean.append(" ORDER BY DEAL_DATE DESC) AS rushRepairDesc, ");
			//周邊異動
			sqlQueryBean.append(" (CASE  WHEN info. CASE_CATEGORY =:update ");
			sqlQueryBean.append("  THEN dbo.ufun_CasePeripheralsIsUpdated( info.DTID , ");
			sqlQueryBean.append(" info.CREATED_DATE ,  info.PERIPHERALS , info.PERIPHERALS2 , info.PERIPHERALS3 ");
			sqlQueryBean.append("  ) ELSE '' END ) AS PeripheralsUpdate ");
			
			sqlQueryBean.append(" FROM ").append(schema).append(" .SRM_CASE_HANDLE_INFO info ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF caseCategory ON caseCategory.BPTD_CODE = 'TICKET_TYPE' ");
			sqlQueryBean.append(" AND caseCategory.ITEM_VALUE = info.CASE_CATEGORY ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE assetType ON assetType.ASSET_TYPE_ID = info.EDC_TYPE  ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer  ON mer.MERCHANT_ID = info.MERCHANT_CODE ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT_HEADER header ON header.MERCHANT_HEADER_ID = info.MERCHANT_HEADER_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF merAdd ON merAdd.BPTD_CODE = :location AND merAdd.ITEM_VALUE = header.LOCATION ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF caseType ON caseType.BPTD_CODE = :ticket_mode AND caseType.ITEM_VALUE = info.CASE_TYPE ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_ASSET_LINK link ON info.CASE_ID = link.CASE_ID AND link.ITEM_TYPE = '10' ");
			sqlQueryBean.append(" AND( link.IS_LINK = 'Y' OR( link. IS_LINK = 'D' AND info. CASE_CATEGORY = :uninstall)) ");
			sqlQueryBean.append(" WHERE ");
			sqlQueryBean.append(" info.COMPLETE_DATE >= :startDate");
			sqlQueryBean.append(" AND info.COMPLETE_DATE < :endDate ");
			sqlQueryBean.append(" AND info.CASE_STATUS IN (:caseStatus) ");
			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND info.CUSTOMER_ID = :customerId ");
				sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
				LOGGER.debug(this.getClass().getName(), ".getCaseListInScsb() --> customerId: ", customerId);
			}
			sqlQueryBean.setParameter("startDate", startDate);
			sqlQueryBean.setParameter("endDate", endDate);
			sqlQueryBean.setParameter("delay", IAtomsConstants.CASE_ACTION.DELAY.getCode());
			sqlQueryBean.setParameter("uninstall", IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode());
			sqlQueryBean.setParameter("update", IAtomsConstants.CASE_CATEGORY.UPDATE.getCode());
			sqlQueryBean.setParameter("location", IATOMS_PARAM_TYPE.LOCATION.getCode());
			sqlQueryBean.setParameter("ticket_mode", IATOMS_PARAM_TYPE.TICKET_MODE.getCode());
			sqlQueryBean.setParameter("transactionType", transactionTypes);
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS.getValue(), caseStatusArray);
			AliasBean aliasBean = new AliasBean(SrmCaseHandleInfoDTO.class);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ROWID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALL_TYPE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PROCESS_TYPE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.SAME_INSTALLED.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ASSET_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.BUSSINESS_ADDRESS.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_FINISH_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ATTENDANCE_TIMES.getValue(), IntegerType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.USERDDAYS90.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.TID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.RUSHREPAIRDESC.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_UPDATE.getValue(), StringType.INSTANCE);
			LOGGER.debug("getCaseListInScsb()", "sql:", sqlQueryBean.toString());
			caseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("getCaseListInScsb() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return caseHandleInfoDTOs;
	}
	@Override
	public List<SrmCaseHandleInfoDTO> getCaseListInSyb(List<String> caseStatusArray, String customerId, Date startDate, 
			Date endDate) throws DataAccessException {
		List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = null;
		LOGGER.debug("getCaseListInSyb()", "parameter:customerId=", customerId);
		LOGGER.debug("getCaseListInSyb()", "parameter:startDate=", startDate.toString());
		LOGGER.debug("getCaseListInSyb()", "parameter:endDate=", endDate.toString());
		try{
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("select ");
			sqlQueryBean.append(" ROW_NUMBER() OVER( ORDER BY info.CASE_ID ) AS rowId , ");
			sqlQueryBean.append(" info.CASE_CATEGORY AS caseCategory , ");
			sqlQueryBean.append(" caseCategory.ITEM_NAME AS caseCategoryName , ");
			sqlQueryBean.append(" assetType.NAME AS assetName , ");
			sqlQueryBean.append(" mer.MERCHANT_CODE AS merchantCode, ");
			sqlQueryBean.append(" mer.NAME AS merchantName , ");
			sqlQueryBean.append(" merAdd.ITEM_NAME + header.BUSINESS_ADDRESS AS bussinessAddress , ");
			sqlQueryBean.append(" info.CREATED_DATE AS createdDate, ");
			sqlQueryBean.append(" info.CREATED_FINISH_DATE AS createdFinishDate, ");
			sqlQueryBean.append(" info.COMPLETE_DATE AS completeDate, ");
			sqlQueryBean.append(" info.CASE_TYPE AS caseType , ");
			sqlQueryBean.append(" caseType.ITEM_NAME AS caseTypeName , ");
			sqlQueryBean.append(" info.SAME_INSTALLED AS sameInstalled , ");
			sqlQueryBean.append(" info.ATTENDANCE_TIMES  AS attendanceTimes, ");
			sqlQueryBean.append(" info.PROCESS_TYPE as processType, ");
			sqlQueryBean.append(" info.UNINSTALL_TYPE AS uninstallType, ");
			sqlQueryBean.append(" info.INSTALL_TYPE AS installType, ");
			sqlQueryBean.append(" unInstallType.ITEM_NAME AS uninstallTypeName, ");
			sqlQueryBean.append(" installType.ITEM_NAME AS installTypeName, ");
			
			//到場說明
			sqlQueryBean.append("  ( CASE  WHEN info.ATTENDANCE_TIMES > 1 ");
			sqlQueryBean.append("  THEN STUFF( ( ").append(" SELECT ").append(" ',' + LTRIM( trans.DESCRIPTION ) ");
			sqlQueryBean.append(" FROM ").append(schema).append(" .SRM_CASE_TRANSACTION trans ");
			sqlQueryBean.append(" WHERE ").append("  trans.CASE_ID = info.CASE_ID ").append(" AND trans.ACTION_ID = 'arrive' AND len(trans.DESCRIPTION) > 0 FOR XML PATH('') ");
			sqlQueryBean.append(" ) ,1 , 1 , '' ) ELSE '' END ) AS description ,");
			//dtid
			sqlQueryBean.append(" info.DTID AS dtid, ");
			//設備序號
			sqlQueryBean.append(" link.SERIAL_NUMBER AS serialNumber, ");
			sqlQueryBean.append("  c.CONTRACT_CODE AS contractCode, ");
			sqlQueryBean.append(" info.CASE_ID AS caseId , ");
			//延期說明
			sqlQueryBean.append(" ( SELECT TOP 1 tra.DESCRIPTION FROM ").append(schema).append(" .SRM_CASE_TRANSACTION tra ");
			sqlQueryBean.append(" WHERE tra.CASE_ID = info.CASE_ID AND tra.ACTION_ID = :delay ");
			sqlQueryBean.append(" ORDER BY DEAL_DATE DESC) AS rushRepairDesc");
			
			sqlQueryBean.append(" FROM ").append(schema).append(" .SRM_CASE_HANDLE_INFO info ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF caseCategory ON caseCategory.BPTD_CODE = 'TICKET_TYPE' ");
			sqlQueryBean.append(" AND caseCategory.ITEM_VALUE = info.CASE_CATEGORY ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE assetType ON assetType.ASSET_TYPE_ID = info.EDC_TYPE ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer  ON mer.MERCHANT_ID = info.MERCHANT_CODE ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT_HEADER header ON header.MERCHANT_HEADER_ID = info.MERCHANT_HEADER_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF merAdd ON merAdd.BPTD_CODE = :location AND merAdd.ITEM_VALUE = header.LOCATION ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF caseType ON caseType.BPTD_CODE = :ticket_mode AND caseType.ITEM_VALUE = info.CASE_TYPE ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF installType ON installType.BPTD_CODE = :install_type AND installType.ITEM_VALUE = info.INSTALL_TYPE ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF unInstallType ON unInstallType.BPTD_CODE = :uninstall_type AND unInstallType.ITEM_VALUE = info.UNINSTALL_TYPE ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_ASSET_LINK link ON info.CASE_ID = link.CASE_ID AND link.ITEM_TYPE = '10' ");
			sqlQueryBean.append(" AND( link.IS_LINK = 'Y' OR( link. IS_LINK = 'D' AND info. CASE_CATEGORY = :uninstall)) ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT c ON link.CONTRACT_ID = c.CONTRACT_ID ");
			sqlQueryBean.append(" WHERE ");
			sqlQueryBean.append(" info.COMPLETE_DATE >= :startDate");
			sqlQueryBean.append(" AND info.COMPLETE_DATE < :endDate ");
			sqlQueryBean.append(" AND info.CASE_STATUS IN (:caseStatus) ");
			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND info.CUSTOMER_ID = :customerId ");
				sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
				LOGGER.debug(this.getClass().getName(), ".getCaseListInSyb() --> customerId: ", customerId);
			}
			sqlQueryBean.setParameter("startDate", startDate);
			sqlQueryBean.setParameter("endDate", endDate);
			sqlQueryBean.setParameter("delay", IAtomsConstants.CASE_ACTION.DELAY.getCode());
			sqlQueryBean.setParameter("uninstall", IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode());
			sqlQueryBean.setParameter("location", IATOMS_PARAM_TYPE.LOCATION.getCode());
			sqlQueryBean.setParameter("ticket_mode", IATOMS_PARAM_TYPE.TICKET_MODE.getCode());
			sqlQueryBean.setParameter("install_type", IATOMS_PARAM_TYPE.INSTALL_TYPE.getCode());
			sqlQueryBean.setParameter("uninstall_type", IATOMS_PARAM_TYPE.UNINSTALL_TYPE.getCode());
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS.getValue(), caseStatusArray);
			AliasBean aliasBean = new AliasBean(SrmCaseHandleInfoDTO.class);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ROWID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ASSET_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.BUSSINESS_ADDRESS.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_FINISH_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.SAME_INSTALLED.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ATTENDANCE_TIMES.getValue(), IntegerType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.DESCRIPTION.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PROCESS_TYPE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALL_TYPE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_TYPE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALL_TYPE_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_TYPE_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.RUSHREPAIRDESC.getValue(), StringType.INSTANCE);
			LOGGER.debug("getCaseListInSyb()", "sql:", sqlQueryBean.toString());
			caseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("getCaseListInSyb() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return caseHandleInfoDTOs;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#getCaseListInChb(java.util.List, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<SrmCaseHandleInfoDTO> getCaseListInChb(List<String> commonTransactionTypeArray, List<String> caseStatusArray, 
			String customerId, Date startDate, Date endDate) throws DataAccessException {
		List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = null;
		LOGGER.debug("getCaseListInChb()", "parameter:customerId=", customerId);
		LOGGER.debug("getCaseListInChb()", "parameter:startDate=", startDate.toString());
		LOGGER.debug("getCaseListInChb()", "parameter:endDate=", endDate.toString());
		try{
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("select ");
			sqlQueryBean.append(" ROW_NUMBER() OVER( ORDER BY info.CASE_ID ) AS rowId , ");
			sqlQueryBean.append(" info.CASE_CATEGORY AS caseCategory , ");
			sqlQueryBean.append(" caseCategory.ITEM_NAME AS caseCategoryName , ");
			//裝機拆機120天
			sqlQueryBean.append(" (CASE WHEN info.CASE_CATEGORY = 'UNINSTALL' THEN datediff( DAY ,info.INSTALLED_DATE ,info.UNINSTALLED_DATE ) ELSE '' END ) AS userdDays120 , ");
			sqlQueryBean.append(" assetType.NAME AS assetName , ");
			sqlQueryBean.append(" mer.MERCHANT_CODE AS merchantCode, ");
			sqlQueryBean.append(" mer.NAME AS merchantName , ");
			sqlQueryBean.append(" merAdd.ITEM_NAME + header.BUSINESS_ADDRESS AS bussinessAddress , ");
			sqlQueryBean.append(" info.CREATED_DATE AS createdDate, ");
			sqlQueryBean.append(" info.CREATED_FINISH_DATE AS createdFinishDate, ");
			sqlQueryBean.append(" info.COMPLETE_DATE AS completeDate, ");
			sqlQueryBean.append(" info.CASE_TYPE AS caseType , ");
			sqlQueryBean.append(" caseType.ITEM_NAME AS caseTypeName , ");
			sqlQueryBean.append(" info.INSTALLED_DATE AS  installedDate ,");
			sqlQueryBean.append(" info.PROCESS_TYPE as processType, ");
			sqlQueryBean.append(" info.UNINSTALL_TYPE AS uninstallType, ");
			sqlQueryBean.append(" info.SAME_INSTALLED AS sameInstalled , ");
			//dtid
			sqlQueryBean.append(" info.DTID AS dtid, ");
			//TID
			sqlQueryBean.append(" (select top 1 par.TID from dbo.SRM_CASE_TRANSACTION_PARAMETER par where par.CASE_ID = info.CASE_ID and par.TRANSACTION_TYPE in ( :commonTransactionTypeArray)) as tid, ");
			//設備序號
			sqlQueryBean.append(" link.SERIAL_NUMBER AS serialNumber, ");
			sqlQueryBean.append("  c.CONTRACT_CODE AS contractCode, ");
			sqlQueryBean.append(" info.INSTALL_TYPE AS installType, ");
			sqlQueryBean.append(" info.CASE_ID AS caseId  ");
			
			sqlQueryBean.append(" FROM ").append(schema).append(" .SRM_CASE_HANDLE_INFO info ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF caseCategory ON caseCategory.BPTD_CODE = 'TICKET_TYPE' ");
			sqlQueryBean.append(" AND caseCategory.ITEM_VALUE = info.CASE_CATEGORY ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .DMM_ASSET_TYPE assetType ON assetType.ASSET_TYPE_ID = info.EDC_TYPE ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT mer  ON mer.MERCHANT_ID = info.MERCHANT_CODE ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_MERCHANT_HEADER header ON header.MERCHANT_HEADER_ID = info.MERCHANT_HEADER_ID ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF merAdd ON merAdd.BPTD_CODE = :location AND merAdd.ITEM_VALUE = header.LOCATION ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BASE_PARAMETER_ITEM_DEF caseType ON caseType.BPTD_CODE = :ticket_mode AND caseType.ITEM_VALUE = info.CASE_TYPE ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .SRM_CASE_ASSET_LINK link ON info.CASE_ID = link.CASE_ID AND link.ITEM_TYPE = '10' ");
			sqlQueryBean.append(" AND( link.IS_LINK = 'Y' OR( link. IS_LINK = 'D' AND info. CASE_CATEGORY = :uninstall)) ");
			sqlQueryBean.append(" LEFT JOIN ").append(schema).append(" .BIM_CONTRACT c ON link.CONTRACT_ID = c.CONTRACT_ID ");
			sqlQueryBean.append(" WHERE ");
			sqlQueryBean.append(" info.COMPLETE_DATE >= :startDate");
			sqlQueryBean.append(" AND info.COMPLETE_DATE < :endDate ");
			sqlQueryBean.append(" AND info.CASE_STATUS IN (:caseStatus) ");
			if(StringUtils.hasText(customerId)){
				sqlQueryBean.append(" AND info.CUSTOMER_ID = :customerId ");
				sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
				LOGGER.debug(this.getClass().getName(), ".getCaseListInChb() --> customerId: ", customerId);
			}
			sqlQueryBean.setParameter("startDate", startDate);
			sqlQueryBean.setParameter("endDate", endDate);
			sqlQueryBean.setParameter("commonTransactionTypeArray", commonTransactionTypeArray);
			sqlQueryBean.setParameter("uninstall", IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode());
			sqlQueryBean.setParameter("location", IATOMS_PARAM_TYPE.LOCATION.getCode());
			sqlQueryBean.setParameter("ticket_mode", IATOMS_PARAM_TYPE.TICKET_MODE.getCode());
			sqlQueryBean.setParameter(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS.getValue(), caseStatusArray);
			AliasBean aliasBean = new AliasBean(SrmCaseHandleInfoDTO.class);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ROWID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.USERDDAYS120.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.ASSET_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.BUSSINESS_ADDRESS.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_TYPE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_FINISH_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.SAME_INSTALLED.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE_NAME.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_DATE.getValue(), TimestampType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.TID.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.PROCESS_TYPE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALL_TYPE.getValue(), StringType.INSTANCE);
			aliasBean.addScalar(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue(), StringType.INSTANCE);
			LOGGER.debug("getCaseListInChb()", "sql:", sqlQueryBean.toString());
			caseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("getCaseListInChb() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return caseHandleInfoDTOs;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#queryTransferCaseNum()
	 */
	public List<Integer> queryTransferCaseNum(String caseId)throws DataAccessException {
		List<Integer> result = null;
		try {
			String schema = this.getMySchema();
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append(" select count(1)");
			sqlQueryBean.append(" from "+schema + ".SRM_CASE_HANDLE_INFO");
			if(StringUtils.hasText(caseId)){
				sqlQueryBean.append(" where CASE_ID in(:caseId)");
			}
			
			sqlQueryBean.append(" union all");
			
			sqlQueryBean.append(" select count(1)");
			sqlQueryBean.append(" from "+schema + ".SRM_CASE_TRANSACTION");
			if(StringUtils.hasText(caseId)){
				sqlQueryBean.append(" where CASE_ID in(:caseId)");
			}
			
			sqlQueryBean.append(" union all");
			
			sqlQueryBean.append(" select count(1)");
			sqlQueryBean.append(" from "+schema + ".SRM_CASE_ATT_FILE");
			if(StringUtils.hasText(caseId)){
				sqlQueryBean.append(" where CASE_ID in(:caseId)");
			}
			
			if(StringUtils.hasText(caseId)){
				sqlQueryBean.setParameter("caseId", StringUtils.toList(caseId, IAtomsConstants.MARK_SEPARATOR));
			}
			
			LOGGER.debug("queryTransferCaseNum()", "sql:" + sqlQueryBean.toString());
			result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			return result;
		} catch (Exception e) {
			LOGGER.error("queryTransferCaseNum()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#copyNewAssetLinkToCase(java.lang.String,java.lang.String)
	 */
	@Override
	public void copyNewAssetLinkToCase(String caseId, String linkId, String transactionId) throws DataAccessException {
		LOGGER.debug("copyNewAssetLinkToCase", "parameter:caseId=", caseId);
		LOGGER.debug("copyNewAssetLinkToCase", "parameter:linkId=", linkId);
		LOGGER.debug("copyNewAssetLinkToCase", "parameter:transactionId=", transactionId);
		try {
			Timestamp time =  DateTimeUtils.getCurrentTimestamp();
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("exec usp_Copy_Case_Asset_Link :caseId, :linkId, :transactionId, :nowTime");
			sqlQueryBean.setParameter("caseId", caseId);
			sqlQueryBean.setParameter("linkId", linkId);
			sqlQueryBean.setParameter("transactionId", transactionId);
			sqlQueryBean.setParameter("nowTime", time);
			LOGGER.debug("copyNewAssetLinkToCase", "sql---->", sqlQueryBean.toString() );
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("copyNewAssetLinkToCase() do PROCEDURE copyNewAssetLinkToCase is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
	}


	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#getCaseInfoById(java.lang.String)
	 */
	@Override
	public List<SrmCaseHandleInfoDTO> getCaseInfoById(String caseId) throws DataAccessException {
		try {
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			StringBuffer fromBuffer = new StringBuffer();
			sqlStatement.addSelectClause("caseInfo.CASE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("caseInfo.CASE_CATEGORY", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue());
			sqlStatement.addSelectClause("caseInfo.CONFIRM_AUTHORIZES", SrmCaseHandleInfoDTO.ATTRIBUTE.CONFIRM_AUTHORIZES.getValue());
			sqlStatement.addSelectClause("caseInfo.IS_IATOMS_CREATE_CMS", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_IATOMS_CREATE_CMS.getValue());
			sqlStatement.addSelectClause("caseInfo.CMS_CASE", SrmCaseHandleInfoDTO.ATTRIBUTE.CMS_CASE.getValue());
			sqlStatement.addSelectClause("caseInfo.IS_PROJECT", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_PROJECT.getValue());
			sqlStatement.addSelectClause("caseInfo.CASE_TYPE", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE.getValue());
			sqlStatement.addSelectClause("caseInfo.ECR_CONNECTION", SrmCaseHandleInfoDTO.ATTRIBUTE.ECR_CONNECTION.getValue());
			sqlStatement.addSelectClause("caseInfo.DISPATCH_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_DATE.getValue());
			sqlStatement.addSelectClause("merchant.MERCHANT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.MER_MID.getValue());
			sqlStatement.addSelectClause("merchant.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue());
			sqlStatement.addSelectClause("merchant.UNITY_NUMBER", SrmCaseHandleInfoDTO.ATTRIBUTE.UNITY_NUMBER.getValue());
			sqlStatement.addSelectClause("company.SHORT_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_NAME.getValue());
			sqlStatement.addSelectClause("caseInfo.DTID", SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("caseInfo.CONTACT_USER", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER.getValue());
			sqlStatement.addSelectClause("caseInfo.CONTACT_USER_PHONE", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_PHONE.getValue());
			sqlStatement.addSelectClause("caseInfo.CONTACT_MOBILE_PHONE", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_MOBILE_PHONE.getValue());
			sqlStatement.addSelectClause("caseInfo.CONTACT_USER_EMAIL", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_EMAIL.getValue());
			sqlStatement.addSelectClause("contactItem.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS_LOCATION_NAME.getValue());
			sqlStatement.addSelectClause("caseInfo.CONTACT_ADDRESS", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS.getValue());
			sqlStatement.addSelectClause("repairReasonItem.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.REPAIR_REASON_NAME.getValue());
			sqlStatement.addSelectClause("STUFF((SELECT DISTINCT ','+ ltrim(trans.TID) FROM " + schema + ".SRM_CASE_TRANSACTION_PARAMETER trans "
					.concat(" WHERE trans.CASE_ID = caseInfo.CASE_ID and trans.TID is not null and trans.TID <> '' and trans.TRANSACTION_TYPE in ('COMMON_VM','COMMON_VMJ','COMMON_VMJU')  FOR XML PATH('')), 1, 1, '')"), SrmCaseHandleInfoDTO.ATTRIBUTE.TID.getValue());
			//特店信息
			sqlStatement.addSelectClause("header.CONTACT", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT.getValue());
			sqlStatement.addSelectClause("header.CONTACT_TEL", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_TEL.getValue());
			sqlStatement.addSelectClause("header.CONTACT_EMAIL", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_EMAIL.getValue());
			sqlStatement.addSelectClause("location.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.LOCATION_NAME.getValue());
			sqlStatement.addSelectClause("header.BUSINESS_ADDRESS", SrmCaseHandleInfoDTO.ATTRIBUTE.BUSINESS_ADDRESS.getValue());
			sqlStatement.addSelectClause("header.PHONE", SrmCaseHandleInfoDTO.ATTRIBUTE.PHONE.getValue());
			sqlStatement.addSelectClause("header.AO_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.AO_NAME.getValue());
			sqlStatement.addSelectClause("header.AOEmail", SrmCaseHandleInfoDTO.ATTRIBUTE.AO_EMAIL.getValue());
			sqlStatement.addSelectClause("assetType.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE_NAME.getValue());
			sqlStatement.addSelectClause("caseInfo.INSTALL_TYPE", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_TYPE.getValue());
			
			//裝機地址、聯絡人、電話的等信息，不關聯特店
			sqlStatement.addSelectClause("installLocation.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS_LOCATION_NAME.getValue());
			sqlStatement.addSelectClause("installPostCode.POST_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_CONTACT_AREA_NAME.getValue());
			sqlStatement.addSelectClause("installPostCode.POST_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_CONTACT_AREA_CODE.getValue());
			sqlStatement.addSelectClause("caseInfo.INSTALLED_ADRESS", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADDRESS.getValue());
			sqlStatement.addSelectClause("caseInfo.INSTALLED_CONTACT", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT.getValue());
			sqlStatement.addSelectClause("caseInfo.INSTALLED_CONTACT_PHONE", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_PHONE.getValue());
			sqlStatement.addSelectClause("caseInfo.INSTALLED_CONTACT_MOBILE_PHONE", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_MOBILE_PHONE.getValue());
			sqlStatement.addSelectClause("caseInfo.INSTALLED_CONTACT_EMAIL", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_EMAIL.getValue());
	
			//Task #3506 查詢結果新增郵遞區號、郵遞區域
			sqlStatement.addSelectClause("(case when caseInfo.CASE_CATEGORY = 'INSTALL' or caseInfo.CASE_CATEGORY = 'UPDATE' then postCode2.POST_NAME else postCode1.POST_NAME end )", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_POST_AREA.getValue());
			sqlStatement.addSelectClause("(case when caseInfo.CASE_CATEGORY = 'INSTALL' or caseInfo.CASE_CATEGORY = 'UPDATE' then postCode2.POST_CODE else postCode1.POST_CODE end )", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_AREA_CODE.getValue());
			
			sqlStatement.addSelectClause("assetLink.SERIAL_NUMBER", SrmCaseHandleInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			
			fromBuffer.append(schema).append(".SRM_CASE_HANDLE_INFO caseInfo ");
			fromBuffer.append("left join ").append(schema).append(".BIM_MERCHANT merchant on merchant.MERCHANT_ID = caseInfo.MERCHANT_CODE ");
			fromBuffer.append("left join ").append(schema).append(".BIM_COMPANY company on company.COMPANY_ID = caseInfo.CUSTOMER_ID ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF contactItem on contactItem.BPTD_CODE = :contactLocation and contactItem.ITEM_VALUE = caseInfo.CONTACT_ADDRESS_LOCATION ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF repairReasonItem on repairReasonItem.BPTD_CODE = :repairReason and repairReasonItem.ITEM_VALUE = caseInfo.REPAIR_REASON ");
			//Task #3506 查詢結果新增郵遞區號、郵遞區域
			fromBuffer.append("left join ").append(schema).append(".BIM_MERCHANT_HEADER header on caseInfo.MERCHANT_HEADER_ID = header.MERCHANT_HEADER_ID ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_POST_CODE postCode1 on caseInfo.CONTACT_POST_CODE = postCode1.POST_CODE_ID ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_POST_CODE postCode2 on header.POST_CODE_ID = postCode2.POST_CODE_ID ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF location on location.BPTD_CODE = :location and location.ITEM_VALUE = header.LOCATION ");
			fromBuffer.append("left join ").append(schema).append(".DMM_ASSET_TYPE assetType on assetType.ASSET_TYPE_ID = caseInfo.EDC_TYPE ");
			/*fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF contactLocati on contactLocati.BPTD_CODE = :location"
					+ "on and contactLocati.ITEM_VALUE = header.LOCATION ");*/
			/*fromBuffer.append("left join ").append(schema).append(".DMM_ASSET_TYPE assetType on assetType.ASSET_TYPE_ID = caseInfo.EDC_TYPE ");*/
		   
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF installLocation on installLocation.BPTD_CODE = :contactLocation and installLocation.ITEM_VALUE = caseInfo.INSTALLED_ADRESS_LOCATION ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_POST_CODE installPostCode on installPostCode.POST_CODE_ID = caseInfo.INSTALLED_POST_CODE ");
			/*fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF installLocation on installLocation.BPTD_CODE = :contactLocation and installLocation.ITEM_VALUE = caseInfo.INSTALLED_ADRESS_LOCATION ");
			fromBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_POST_CODE installPostCode on installPostCode.POST_CODE_ID = caseInfo.INSTALLED_POST_CODE ");*/
			
			fromBuffer.append("left join ").append(schema).append(".SRM_CASE_ASSET_LINK assetLink on assetLink.CASE_ID = caseInfo.CASE_ID and assetLink.ITEM_TYPE = '10' and assetLink.IS_LINK = 'Y'");
			sqlStatement.addFromExpression(fromBuffer.toString());
			if (StringUtils.hasText(caseId)) {
				sqlStatement.addWhereClause("caseInfo.CASE_ID in ( :caseId)");
				//sqlStatement.addWhereClause("caseInfo.CASE_ID = :caseId", caseId);
				SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
				sqlQueryBean.setParameter("contactLocation", IATOMS_PARAM_TYPE.LOCATION.getCode());
				sqlQueryBean.setParameter("location", IATOMS_PARAM_TYPE.LOCATION.getCode());
				sqlQueryBean.setParameter("repairReason", IATOMS_PARAM_TYPE.REPAIR_REASON.getCode());
				sqlQueryBean.setParameter("caseId", StringUtils.toList(caseId, IAtomsConstants.MARK_SEPARATOR));
				AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseHandleInfoDTO.class);
				List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
				return caseHandleInfoDTOs;
			}
		} catch (Exception e) {
			LOGGER.error("getCaseInfoById() do PROCEDURE getCaseInfoById is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
		return null;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#getAssetByCompanyId(java.lang.String, java.lang.String)
	 */
	@Override
	public List<SrmCaseHandleInfoDTO> getAssetByCompanyId(String assetUserId, String companyId)throws DataAccessException {
		List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOS = null;
		try{
			LOGGER.debug(this.getClass().getName() + ".getAssetByCompanyId()", "assetUserId=" + assetUserId);
			LOGGER.debug(this.getClass().getName() + ".getAssetByCompanyId()", "companyId=" + companyId);
			SqlStatement sqlStatement = new SqlStatement();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("r.SERIAL_NUMBER", SrmCaseHandleInfoDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			sqlStatement.addSelectClause("r.CASE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("info.REQUIREMENT_NO", SrmCaseHandleInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue());
			sqlStatement.addSelectClause("info.CASE_CATEGORY", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue());
			sqlStatement.addSelectClause("info.CUSTOMER_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue());
			//Task #3584 reopen 一般交易項目之TID
			sqlStatement.addSelectClause("param.TID", SrmCaseHandleInfoDTO.ATTRIBUTE.TID.getValue());

			sqlStatement.addSelectClause("info.CLOSE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.CLOSE_DATE.getValue());
			sqlStatement.addSelectClause("info.INSTALL_COMPLETE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_COMPLETE_DATE.getValue());
			sqlStatement.addSelectClause("info.INSTALL_CASE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_CASE_ID.getValue());
			StringBuffer buffer = new StringBuffer();
			buffer.append(schema).append(".DMM_REPOSITORY r left join ").append(schema).append(".SRM_CASE_HANDLE_INFO info on info.CASE_ID= r.CASE_ID ");
			buffer.append(" left join ").append(schema).append(".SRM_CASE_TRANSACTION_PARAMETER param on r.CASE_ID = param.CASE_ID and charindex('COMMON_', param.TRANSACTION_TYPE)=1");
			buffer.append(" left join ").append(schema).append(".DMM_ASSET_TYPE type on r.ASSET_TYPE_ID = type.ASSET_TYPE_ID ");
			sqlStatement.addFromExpression(buffer.toString());
			//查詢條件
			//Task #3584 設備:(狀態=使用中、停用中)&設備對應之案件={(客戶=台新)或(客戶=宣揚&需求單號有值&裝機案件編號有值)}
			if (StringUtils.hasText(assetUserId)) {
				if (StringUtils.hasText(companyId)) {
					sqlStatement.addWhereClause("((info.CUSTOMER_ID ='"
							+ companyId
							+"' and info.INSTALL_CASE_ID is not null and info.INSTALL_CASE_ID<>'' and (charindex('EI', info.REQUIREMENT_NO)=1 or charindex('EC', info.REQUIREMENT_NO)=1 or charindex('EM', info.REQUIREMENT_NO)=1 or charindex('EU', info.REQUIREMENT_NO)=1 or charindex('EA', info.REQUIREMENT_NO)=1 or charindex('ER', info.REQUIREMENT_NO)=1 ) ) or info.CUSTOMER_ID ='" 
							+ assetUserId + "' ) ");
				} else {
					sqlStatement.addWhereClause("info.CUSTOMER_ID = :assetUserId", assetUserId);
				}
			}
			//Task #3584
			//sqlStatement.addWhereClause(" info.CMS_CASE = 'Y' ");
			sqlStatement.addWhereClause(" (r.STATUS ='IN_USE' or r.STATUS ='STOP')");
			sqlStatement.addWhereClause(" type.ASSET_CATEGORY <>'Related_Products' ");
			sqlStatement.setOrderByExpression(" r.SERIAL_NUMBER ");
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseHandleInfoDTO.class);
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			LOGGER.debug(".getAssetByCompanyId()", "sql:", sqlQueryBean.toString());
			srmCaseHandleInfoDTOS = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+":getAssetByCompanyId() is error." + e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return srmCaseHandleInfoDTOS;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#getCaseIdListByDispatching(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getCaseIdListByDispatching(String companyId,
			String dispatchDeptId) throws DataAccessException {
		// TODO Auto-generated method stub
		LOGGER.debug("getCaseIdListByDispatching", "parameter:companyId=", companyId);
		LOGGER.debug("getCaseIdListByDispatching", "parameter:dispatchDeptId=", dispatchDeptId);
		List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOs = null;
		List<String> caseIdList = new ArrayList<String>();
		try {
			String schema = this.getMySchema();
			SqlStatement sql = new SqlStatement();
			sql.addSelectClause("CASE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue());
			sql.addFromExpression(schema + ".SRM_CASE_HANDLE_INFO");
			if (StringUtils.hasText(companyId)) {
				sql.addWhereClause("COMPANY_ID = :companyId", companyId);
			}
			if (StringUtils.hasText(dispatchDeptId)) {
				sql.addWhereClause("DISPATCH_DEPT_ID = :dispatchDeptId", dispatchDeptId);
			}
			sql.addWhereClause("UPDATED_DATE >= CONVERT(datetime, CONVERT(varchar(10), dateadd(d, -1, getdate()), 126) + ' 23:30:00')");
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			AliasBean aliasBean = sql.createAliasBean(SrmCaseHandleInfoDTO.class);
			LOGGER.debug("listBy()", "sql=" + sqlQueryBean.toString());
			srmCaseHandleInfoDTOs = super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			for(int i = 0; i < srmCaseHandleInfoDTOs.size(); i++) {
				caseIdList.add(srmCaseHandleInfoDTOs.get(i).getCaseId());
			}
		} catch (Exception e) {
			LOGGER.error("getCaseIdListByDispatching()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return caseIdList;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#getOverdueCaseInfo(java.util.Date, java.util.Date)
	 */
	@Override
	public List<SrmCaseHandleInfoDTO> getOverdueCaseInfo(Date queryStartDate, Date queryEndDate) throws DataAccessException {
		List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = null;
		try {
			SqlStatement sqlStatement = new SqlStatement();
			StringBuffer tempBuffer = new StringBuffer();
			String schema = this.getMySchema();
			sqlStatement.addSelectClause("info.CASE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue());
			sqlStatement.addSelectClause("info.REQUIREMENT_NO", SrmCaseHandleInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue());
			sqlStatement.addSelectClause("company.SHORT_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_NAME.getValue());
			sqlStatement.addSelectClause("info.CREATED_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue());
			sqlStatement.addSelectClause("info.CONTACT_USER", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER.getValue());
			sqlStatement.addSelectClause("info.CONTACT_USER_PHONE", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_PHONE.getValue());
			sqlStatement.addSelectClause("header.PHONE", SrmCaseHandleInfoDTO.ATTRIBUTE.PHONE.getValue());
			sqlStatement.addSelectClause("info.DTID", SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue());
			sqlStatement.addSelectClause("(case when info.CONTACT_IS_BUSSINESS_ADDRESS = 'Y' then headerLocation.ITEM_NAME " +
					"else contactLocation.ITEM_NAME end)", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS_LOCATION_NAME.getValue());
			sqlStatement.addSelectClause("(case when info.CONTACT_IS_BUSSINESS_ADDRESS = 'Y' then header.LOCATION " +
					"else info.CONTACT_ADDRESS_LOCATION end)", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS_LOCATION.getValue());
			sqlStatement.addSelectClause("(case when info.CONTACT_IS_BUSSINESS_ADDRESS = 'Y' then header.POST_CODE_ID " +
					"else info.CONTACT_POST_CODE end)", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_POST_CODE.getValue());
			sqlStatement.addSelectClause("(case when info.CONTACT_IS_BUSSINESS_ADDRESS = 'Y' then header.BUSINESS_ADDRESS " +
					"else info.CONTACT_ADDRESS end)", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS.getValue());
			tempBuffer.append("STUFF((SELECT DISTINCT ','+ ltrim(trans.TID) FROM ");
			tempBuffer.append(schema).append(".SRM_CASE_TRANSACTION_PARAMETER trans ");
			tempBuffer.append("where ").append("trans.CASE_ID = info.CASE_ID ");
			tempBuffer.append("and trans.TID is not null and trans.TID <> '' ");
			tempBuffer.append("and trans.TRANSACTION_TYPE in ('COMMON_VM','COMMON_VMJ','COMMON_VMJU') FOR XML PATH('')), 1, 1, '') ");
			sqlStatement.addSelectClause(tempBuffer.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.TID.getValue());
			sqlStatement.addSelectClause("merchant.MERCHANT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			sqlStatement.addSelectClause("merchant.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue());
			sqlStatement.addSelectClause("header.HEADER_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_HEADER_NAME.getValue());
			sqlStatement.addSelectClause("info.ACCEPTABLE_FINISH_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_FINISH_DATE.getValue());
			sqlStatement.addSelectClause("assetType.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE_NAME.getValue());
			
			sqlStatement.addSelectClause("info.COMPLETE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue());
			sqlStatement.addSelectClause("info.DESCRIPTION", SrmCaseHandleInfoDTO.ATTRIBUTE.DESCRIPTION.getValue());
			sqlStatement.addSelectClause("info.UPDATED_BY_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue());
			
			sqlStatement.addSelectClause("info.DISPATCH_DEPT_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_DEPT_ID.getValue());
			sqlStatement.addSelectClause("info.DISPATCH_PROCESS_USER", SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_PROCESS_USER.getValue());

			tempBuffer.delete(0, tempBuffer.length());
			tempBuffer.append(schema).append(".SRM_CASE_HANDLE_INFO info ");
			tempBuffer.append("left join ").append(schema).append(".BIM_COMPANY company on company.COMPANY_ID = info.CUSTOMER_ID ");
			tempBuffer.append("left join ").append(schema).append(".BIM_MERCHANT_HEADER header on header.MERCHANT_HEADER_ID = info.MERCHANT_HEADER_ID ");
			tempBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF headerLocation on headerLocation.BPTD_CODE = :location and headerLocation.ITEM_VALUE = header.LOCATION ");
			tempBuffer.append("left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF contactLocation on contactLocation.BPTD_CODE = :location and contactLocation.ITEM_VALUE = info.CONTACT_ADDRESS_LOCATION ");
			tempBuffer.append("left join ").append(schema).append(".BIM_MERCHANT merchant on merchant.MERCHANT_ID = info.MERCHANT_CODE ");
			tempBuffer.append("left join ").append(schema).append(".SRM_CASE_ASSET_LINK assetLink on assetLink.CASE_ID = info.CASE_ID and assetLink.ITEM_TYPE = '10' and assetLink.IS_LINK = 'Y' ");
			tempBuffer.append("left join ").append(schema).append(".DMM_REPOSITORY repository on repository.SERIAL_NUMBER = assetLink.SERIAL_NUMBER ");
			tempBuffer.append("left join ").append(schema).append(".DMM_ASSET_TYPE assetType on assetType.ASSET_TYPE_ID = repository.ASSET_TYPE_ID ");
			sqlStatement.addFromExpression(tempBuffer.toString());
			
			if (queryStartDate != null && queryEndDate != null) {
				tempBuffer.delete(0, tempBuffer.length());
				tempBuffer.append("((info.ACCEPTABLE_RESPONSE_DATE between :queryStartDate and :queryEndDate and ISNULL(info.RESPONSE_DATE,'')='')");
				tempBuffer.append("or (info.ACCEPTABLE_ARRIVE_DATE between :queryStartDate and :queryEndDate and ISNULL(info.ARRIVE_DATE,'')='') ");
				tempBuffer.append("or (info.ACCEPTABLE_FINISH_DATE between :queryStartDate and :queryEndDate and ISNULL(info.COMPLETE_DATE,'')='') ) ");
				sqlStatement.addWhereClause(tempBuffer.toString());
			}
			sqlStatement.addWhereClause("info.CASE_CATEGORY = :caseCategory", IAtomsConstants.CASE_CATEGORY.REPAIR.getCode());
			sqlStatement.addWhereClause("info.HAS_DELAY = :hasDelay", IAtomsConstants.NO);
			sqlStatement.addWhereClause("info.CASE_STATUS not in ('WaitClose','Closed','ImmediateClose','Voided')");
			SqlQueryBean sqlQueryBean = sqlStatement.createSqlQueryBean();
			AliasBean aliasBean = sqlStatement.createAliasBean(SrmCaseHandleInfoDTO.class);
			if (queryStartDate != null && queryEndDate != null) {
				sqlQueryBean.setParameter("queryStartDate", queryStartDate);
				sqlQueryBean.setParameter("queryEndDate", queryEndDate);
			}
			sqlQueryBean.setParameter("location", IATOMS_PARAM_TYPE.LOCATION.getCode());
			caseHandleInfoDTOs = this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			LOGGER.error("getOverdueCaseInfo()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return caseHandleInfoDTOs;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#getInstallCaseId(java.lang.String)
	 */
	@Override
	public String getInstallCaseId(String dtid) throws DataAccessException {
		// Task #3584 需要獲取 宣揚 符合條件的裝機案件單號
		LOGGER.debug("getInstallCaseId", "parameter:dtid=", dtid);
		String installCaseId = null;
		try {
			String schema = this.getMySchema();
			SqlStatement sql = new SqlStatement();
			sql.addSelectClause("INSTALL_CASE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_CASE_ID.getValue());
			sql.addFromExpression(schema + ".SRM_CASE_HANDLE_INFO");
			if (StringUtils.hasText(dtid)) {
				sql.addWhereClause("DTID = :dtid", dtid);
			}
			sql.addWhereClause("charindex('EI',INSTALL_CASE_ID) = 1 ");
			sql.addWhereClause("CASE_STATUS <> 'Voided'");
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			AliasBean aliasBean = sql.createAliasBean(SrmCaseHandleInfoDTO.class);
			LOGGER.debug("getInstallCaseId()", "sql =" + sqlQueryBean.toString());
			List<SrmCaseHandleInfoDTO> result = (List<SrmCaseHandleInfoDTO>) super.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
			if (!CollectionUtils.isEmpty(result)) {
				installCaseId = result.get(0).getInstallCaseId();
			}
		} catch (Exception e) {
			LOGGER.error("getInstallCaseId()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return installCaseId;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO#changeInstallCaseId(java.lang.String)
	 */
	@Override
	public void changeInstallCaseId(String caseId, String dtid, String requirementNo, Date installCompleteDate, String flag) throws DataAccessException {
		LOGGER.debug("changeInstallCaseId", "parameter:caseId=", caseId);
		LOGGER.debug("changeInstallCaseId", "parameter:dtid=", dtid);
		LOGGER.debug("changeInstallCaseId", "parameter:requirementNo=", requirementNo);
		LOGGER.debug("changeInstallCaseId", "parameter:flag=", flag);
		LOGGER.debug("changeInstallCaseId", "parameter:installCompleteDate=", installCompleteDate!=null ? installCompleteDate.toString() : IAtomsConstants.MARK_EMPTY_STRING);
		try {
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			sqlQueryBean.append("exec usp_Change_Install_Case_Id :caseId, :dtid, :requirementNo,:installCompleteDate, :flag");
			sqlQueryBean.setParameter("caseId", caseId);
			sqlQueryBean.setParameter("dtid", dtid);
			sqlQueryBean.setParameter("requirementNo", StringUtils.hasText(requirementNo) ? requirementNo : IAtomsConstants.MARK_EMPTY_STRING);
			sqlQueryBean.setParameter("installCompleteDate", installCompleteDate!=null ? installCompleteDate : IAtomsConstants.MARK_EMPTY_STRING);
			sqlQueryBean.setParameter("flag", StringUtils.hasText(flag) ? flag : IAtomsConstants.MARK_EMPTY_STRING);
			LOGGER.debug("changeInstallCaseId", "sql---->", sqlQueryBean.toString() );
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
			this.getDaoSupport().flush();
		} catch (Exception e) {
			LOGGER.error("changeInstallCaseId() do procedure: usp_Change_Install_Case_Id is error...", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, new String[] {i18NUtil.getName(IAtomsConstants.RSB_IATOMS_TB_NAME_ADM_ROLE)}, e);
		}
	}
}
