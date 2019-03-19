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
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseNewTransactionParameterDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.EdcParameterFormDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IEdcParameterDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseNewHandleInfo;
/**
 * Purpose: EDC交易參數數據訪問層實現類
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016/9/22
 * @MaintenancePersonnel CrissZhang
 */
public class EdcParameterDAO extends GenericBaseDAO<SrmCaseNewHandleInfo> implements IEdcParameterDAO{

	/**
	 * 系统日志记录物件  
	 */
	private static CafeLog logger = CafeLogFactory.getLog(GenericConfigManager.DAO, EdcParameterDAO.class);
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IEdcParameterDAO#listBy(com.cybersoft4u.xian.iatoms.common.bean.formDTO.EdcParameterFormDTO)
	 */
	@Override
	public List<SrmCaseHandleInfoDTO> listBy(EdcParameterFormDTO edcParameterFormDTO) throws DataAccessException {
		logger.debug("listBy()", "parameters:queryMerchantCode = " + edcParameterFormDTO.getQueryMerchantCode());
		logger.debug("listBy()", "parameters:queryMerchantName = " + edcParameterFormDTO.getQueryMerchantName());
		logger.debug("listBy()", "parameters:queryMerAnnouncedName = " + edcParameterFormDTO.getQueryMerAnnouncedName());
		logger.debug("listBy()", "parameters:queryTid = " + edcParameterFormDTO.getQueryTid());
		logger.debug("listBy()", "parameters:queryDtid = " + edcParameterFormDTO.getQueryDtid());
		logger.debug("listBy()", "parameters:queryEdcType = " + edcParameterFormDTO.getQueryEdcType());
		logger.debug("listBy()", "parameters:queryPeripheralEquipment = " + edcParameterFormDTO.getQueryPeripheralEquipment());
		logger.debug("listBy()", "parameters:queryAssetSupportedFunction = " + edcParameterFormDTO.getQueryAssetSupportedFunction());
		logger.debug("listBy()", "parameters:queryAssetOpenMode = " + edcParameterFormDTO.getQueryAssetOpenMode());
		logger.debug("listBy()", "parameters:queryAssetStatus = " + edcParameterFormDTO.getQueryAssetStatus());
		logger.debug("listBy()", "parameters:queryOpenTransaction = " + edcParameterFormDTO.getQueryOpenTransaction());
		logger.debug("listBy()", "parameters:rows = " + edcParameterFormDTO.getRows());
		logger.debug("listBy()", "parameters:page = " + edcParameterFormDTO.getPage());
		logger.debug("listBy()", "parameters:sort = " + edcParameterFormDTO.getSort());
		logger.debug("listBy()", "parameters:order = " + edcParameterFormDTO.getOrder());
		List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOs = null;
		try {
			// 拿到schema
			String schema = this.getMySchema();
			SqlStatement sql = new SqlStatement();
			// DTID
			sql.addSelectClause("caseHandleInfo.DTID", SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue());
			// 客戶
			sql.addSelectClause("caseHandleInfo.CUSTOMER_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue());
			sql.addSelectClause("company.SHORT_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_NAME.getValue());
			// 案件編號
			sql.addSelectClause("caseHandleInfo.CASE_ID", SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue());
			// 特店代號
			sql.addSelectClause("merchant.MERCHANT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
			// 特店分期代號
		//	sql.addSelectClause("merchant.STAGES_MERCHANT_CODE", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_STAGES_CODE.getValue());
			// 特店名稱
			sql.addSelectClause("merchant.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue());
			// 表頭(同對外名稱)
			sql.addSelectClause("merchantHead.HEADER_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.HEADER_NAME.getValue());
			// 裝機日期
			sql.addSelectClause("caseHandleInfo.INSTALLED_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_DATE.getValue());
//			sql.addSelectClause("CONVERT(varchar(10), caseHandleInfo.INSTALLED_DATE, 111)", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_DATE.getValue());
			// 拆機日期
			sql.addSelectClause("caseHandleInfo.UNINSTALLED_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALLED_DATE.getValue());
//			sql.addSelectClause("CONVERT(varchar(10), caseHandleInfo.UNINSTALLED_DATE, 111)", SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALLED_DATE.getValue());
			// CUP 啟用日期
			sql.addSelectClause("caseHandleInfo.CUP_ENABLE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_ENABLE_DATE.getValue());
//			sql.addSelectClause("CONVERT(varchar(10), caseHandleInfo.CUP_ENABLE_DATE, 111)", SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_ENABLE_DATE.getValue());
			// CUP 移除日期
			sql.addSelectClause("caseHandleInfo.CUP_DISABLE_DATE", SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_DISABLE_DATE.getValue());
//			sql.addSelectClause("CONVERT(varchar(10), caseHandleInfo.CUP_DISABLE_DATE, 111)", SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_DISABLE_DATE.getValue());
			// 已開放交易清單
			StringBuilder builder = new StringBuilder();
			builder.append(" stuff((SELECT ',' + RTRIM( baseItem.itemName) FROM ").append(schema).append(".SRM_CASE_NEW_TRANSACTION_PARAMETER transParam ");
			builder.append(" LEFT JOIN (select base.ITEM_NAME as itemName, base.ITEM_VALUE as itemValue from ");
			builder.append(schema).append(".BASE_PARAMETER_ITEM_DEF base inner join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF parentBase ");
			builder.append(" on base.PARENT_BPID_ID = parentBase.BPID_ID and parentBase.BPTD_CODE = :ticketTypeParam ");
			builder.append(" and parentBase.ITEM_VALUE = :installCaseCategory where base.BPTD_CODE = :transactionCategoryParam ");
		//	builder.append(" and isnull(base.APPROVED_FLAG,'N') = 'Y' and isnull(base.DELETED,'N') = 'N' ");
			// 處理approvedFlag
			builder.append(" and isnull(base.DELETED,'N') = 'N' ");
			builder.append(" and base.EFFECTIVE_DATE = (select max(maxbase.EFFECTIVE_DATE) FROM ").append(schema);
			builder.append(".BASE_PARAMETER_ITEM_DEF maxbase where maxbase.BPTD_CODE = :transactionCategoryParam ");
		//	builder.append(" and maxbase.PARENT_BPID_ID = parentBase.BPID_ID and isnull(maxbase.APPROVED_FLAG,'N') = 'Y' ");
			// 處理approvedFlag
			builder.append(" and maxbase.PARENT_BPID_ID = parentBase.BPID_ID and isnull(maxbase.DELETED,'N') = 'N' ");
			builder.append(" and isnull(maxbase.DELETED,'N') = 'N' and maxbase.EFFECTIVE_DATE <= getdate() )");
			builder.append(" ) baseItem on baseItem.itemValue = transParam.TRANSACTION_TYPE ");
			builder.append(" WHERE transParam.CASE_ID = caseHandleInfo.CASE_ID FOR XML path('') ) , 1 , 1 , '') ");
			sql.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.OPEN_TRANSACTION_LIST.getValue());
			// 裝機地址
			sql.addSelectClause("installTypeLocation.ITEM_NAME + '-' + caseHandleInfo.INSTALLED_ADRESS", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue());
			// 營業地址
			sql.addSelectClause("businessLocation.ITEM_NAME + '-' + merchantHead.BUSINESS_ADDRESS", SrmCaseHandleInfoDTO.ATTRIBUTE.BUSINESS_ADDRESS.getValue());
			// 特店聯絡人
			sql.addSelectClause("merchantHead.CONTACT", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT.getValue());
			// 特店聯絡人電話1
			sql.addSelectClause("merchantHead.CONTACT_TEL", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_TEL.getValue());
			// 特店聯絡人電話2
			sql.addSelectClause("merchantHead.CONTACT_TEL2", SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_TEL2.getValue());
			// 特店聯絡人行動電話
			sql.addSelectClause("merchantHead.PHONE", SrmCaseHandleInfoDTO.ATTRIBUTE.PHONE.getValue());
			// AO 人員
			sql.addSelectClause("merchantHead.AO_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.AO_NAME.getValue());
			// 營業時間
			sql.addSelectClause("(case when merchantHead.OPEN_HOUR IS NOT NULL AND  merchantHead.CLOSE_HOUR IS NOT NULL then merchantHead.OPEN_HOUR + '-' + merchantHead.CLOSE_HOUR end )", SrmCaseHandleInfoDTO.ATTRIBUTE.BUSINESS_HOURS.getValue());
			// 維護廠商
			sql.addSelectClause("vendor.SHORT_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_NAME.getValue());
			// 刷卡機型
		//	sql.addSelectClause("caseHandleInfo.EDC_TYPE", SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE.getValue());
			sql.addSelectClause("edcType.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE_NAME.getValue());
			// 軟體版本
			sql.addSelectClause("application.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.APPLICATION_NAME.getValue());
			// 內建功能
			builder.delete(0, builder.length());
			builder.append("stuff(( SELECT ',' + RTRIM( supportedFun.ITEM_NAME ) FROM ").append(schema).append(".SRM_CASE_NEW_ASSET_FUNCTION caseAssetFun  ");
			builder.append(" LEFT JOIN ").append(schema).append(".BASE_PARAMETER_ITEM_DEF supportedFun on supportedFun.BPTD_CODE = :supportedFunctionParam ");
			builder.append(" and supportedFun.ITEM_VALUE = caseAssetFun.FUNCTION_ID where caseAssetFun.CASE_ID = caseHandleInfo.CASE_ID ");
			builder.append(" and caseAssetFun.FUNCTION_CATEGORY = :caseLinkEdcType FOR XML path('') ) ,1 , 1 ,'') ");
			sql.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.OPEN_FUNCTION_NAME.getValue());
			// 雙模組模式
			sql.addSelectClause("doubleModule.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.MULTI_MODULE_NAME.getValue());
			// ECR 連線
			sql.addSelectClause("ecrConnection.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.ECR_CONNECTION_NAME.getValue());
			// 通訊模式
		//	sql.addSelectClause("commMode.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.CONNECTION_TYPE_NAME.getValue());
			builder.delete(0, builder.length());
			builder.append("stuff(( SELECT '，' + RTRIM( commMode.ITEM_NAME ) FROM ").append(schema).append(".SRM_CASE_NEW_COMM_MODE caseCommMode ");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF commMode on commMode.BPTD_CODE = :commModeParam");
			builder.append(" and commMode.ITEM_VALUE = caseCommMode.COMM_MODE_ID where caseCommMode.CASE_ID = caseHandleInfo.CASE_ID ");
			builder.append(" FOR XML path('') ) ,1 , 1 ,'')");
			sql.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONNECTION_TYPE_NAME.getValue());
			// 寬頻連線
			sql.addSelectClause("netVendor.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.NET_VENDOR_NAME.getValue());
			// 刷卡機 IP 位址
			sql.addSelectClause("caseHandleInfo.LOCALHOST_IP", SrmCaseHandleInfoDTO.ATTRIBUTE.LOCALHOST_IP.getValue());
			// 刷卡機 GateWay
			sql.addSelectClause("caseHandleInfo.GATEWAY", SrmCaseHandleInfoDTO.ATTRIBUTE.GATEWAY.getValue());
			// 刷卡機 Netmask
			sql.addSelectClause("caseHandleInfo.NETMASK", SrmCaseHandleInfoDTO.ATTRIBUTE.NETMASK.getValue());
			// 週邊設備1
			sql.addSelectClause("peripherals.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_NAME.getValue());
			// 週邊設備功能1
			builder.delete(0, builder.length());
			builder.append("stuff(( SELECT ',' + RTRIM( supportedFun.ITEM_NAME ) FROM ").append(schema).append(".SRM_CASE_NEW_ASSET_FUNCTION caseAssetFun  ");
			builder.append(" LEFT JOIN ").append(schema).append(".BASE_PARAMETER_ITEM_DEF supportedFun on supportedFun.BPTD_CODE = :supportedFunctionParam ");
			builder.append(" and supportedFun.ITEM_VALUE = caseAssetFun.FUNCTION_ID where caseAssetFun.CASE_ID = caseHandleInfo.CASE_ID ");
			builder.append(" and caseAssetFun.FUNCTION_CATEGORY = :caseLinkPeripherals FOR XML path('') ) ,1 , 1 ,'') ");
			sql.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION_NAME.getValue());
			// 週邊設備2
			sql.addSelectClause("peripherals2.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_NAME.getValue());
			// 週邊設備功能2
			builder.delete(0, builder.length());
			builder.append("stuff(( SELECT ',' + RTRIM( supportedFun.ITEM_NAME ) FROM ").append(schema).append(".SRM_CASE_NEW_ASSET_FUNCTION caseAssetFun  ");
			builder.append(" LEFT JOIN ").append(schema).append(".BASE_PARAMETER_ITEM_DEF supportedFun on supportedFun.BPTD_CODE = :supportedFunctionParam ");
			builder.append(" and supportedFun.ITEM_VALUE = caseAssetFun.FUNCTION_ID where caseAssetFun.CASE_ID = caseHandleInfo.CASE_ID ");
			builder.append(" and caseAssetFun.FUNCTION_CATEGORY = :caseLinkPeripherals2 FOR XML path('') ) ,1 , 1 ,'') ");
			sql.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION2_NAME.getValue());
			// 週邊設備3
			sql.addSelectClause("peripherals3.NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_NAME.getValue());
			// 週邊設備功能3
			builder.delete(0, builder.length());
			builder.append("stuff(( SELECT ',' + RTRIM( supportedFun.ITEM_NAME ) FROM ").append(schema).append(".SRM_CASE_NEW_ASSET_FUNCTION caseAssetFun  ");
			builder.append(" LEFT JOIN ").append(schema).append(".BASE_PARAMETER_ITEM_DEF supportedFun on supportedFun.BPTD_CODE = :supportedFunctionParam ");
			builder.append(" and supportedFun.ITEM_VALUE = caseAssetFun.FUNCTION_ID where caseAssetFun.CASE_ID = caseHandleInfo.CASE_ID ");
			builder.append(" and caseAssetFun.FUNCTION_CATEGORY = :caseLinkPeripherals3 FOR XML path('') ) ,1 , 1 ,'') ");
			sql.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION3_NAME.getValue());
			// 電子發票載具
			sql.addSelectClause("caseHandleInfo.ELECTRONIC_INVOICE", SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_INVOICE.getValue());
			// 銀聯閃付
			sql.addSelectClause("caseHandleInfo.CUP_QUICK_PASS", SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_QUICK_PASS.getValue());
			// LOGO
			sql.addSelectClause("caseHandleInfo.LOGO_STYLE", SrmCaseHandleInfoDTO.ATTRIBUTE.LOGO_STYLE.getValue());
			// 是否開啟加密
			sql.addSelectClause("caseHandleInfo.IS_OPEN_ENCRYPT", SrmCaseHandleInfoDTO.ATTRIBUTE.IS_OPEN_ENCRYPT.getValue());
			// 電子化繳費平台
			sql.addSelectClause("caseHandleInfo.ELECTRONIC_PAY_PLATFORM", SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_PAY_PLATFORM.getValue());
			// 異動項目
		//	sql.addSelectClause("caseHandleInfo.UPDATE_ITEM", SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATE_ITEM.getValue());
			builder.delete(0, builder.length());
			builder.append("(case when caseHandleInfo.CASE_CATEGORY = 'MERGE' then caseHandleInfo.UPDATED_DESCRIPTION");
			builder.append(" else caseHandleInfo.UPDATE_ITEM end)");
			sql.addSelectClause(builder.toString(), SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATE_ITEM.getValue());
			// 裝機類型
			sql.addSelectClause("installType.ITEM_NAME", SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_TYPE_NAME.getValue());
			builder.delete(0, builder.length());
			builder.append(schema).append(".SRM_CASE_NEW_HANDLE_INFO caseHandleInfo ");
			builder.append(" left join ").append(schema).append(".BIM_COMPANY company ON caseHandleInfo.CUSTOMER_ID = company.COMPANY_ID");
			builder.append(" left join ").append(schema).append(".BIM_MERCHANT merchant ON caseHandleInfo.MERCHANT_CODE = merchant.MERCHANT_ID");
			builder.append(" left join ").append(schema).append(".BIM_MERCHANT_HEADER merchantHead ON caseHandleInfo.MERCHANT_HEADER_ID = merchantHead.MERCHANT_HEADER_ID");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF installTypeLocation on installTypeLocation.BPTD_CODE = :locationParam");
			builder.append(" and installTypeLocation.ITEM_VALUE = caseHandleInfo.INSTALLED_ADRESS_LOCATION");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF businessLocation on businessLocation.BPTD_CODE = :locationParam");
			builder.append(" and businessLocation.ITEM_VALUE = merchantHead.LOCATION");
			builder.append(" left join ").append(schema).append(".BIM_COMPANY vendor ON caseHandleInfo.COMPANY_ID = vendor.COMPANY_ID");
			builder.append(" left join ").append(schema).append(".DMM_ASSET_TYPE edcType ON edcType.ASSET_TYPE_ID = caseHandleInfo.EDC_TYPE");
			builder.append(" left join ").append(schema).append(".PVM_APPLICATION application ON application.APPLICATION_ID = caseHandleInfo.SOFTWARE_VERSION");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF doubleModule on doubleModule.BPTD_CODE = :doubleModuleParam");
			builder.append(" and doubleModule.ITEM_VALUE = caseHandleInfo.MULTI_MODULE");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF ecrConnection on ecrConnection.BPTD_CODE = :ecrLineParam");
			builder.append(" and ecrConnection.ITEM_VALUE = caseHandleInfo.ECR_CONNECTION");
//			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF commMode ON commMode.ITEM_VALUE = caseHandleInfo.CONNECTION_TYPE");
//			builder.append(" AND commMode.BPTD_CODE = :commModeParam ");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF netVendor ON netVendor.ITEM_VALUE = caseHandleInfo.NET_VENDOR_ID");
			builder.append(" AND netVendor.BPTD_CODE = :netVendorParam ");
			builder.append(" left join ").append(schema).append(".DMM_ASSET_TYPE peripherals on peripherals.ASSET_TYPE_ID = caseHandleInfo.PERIPHERALS");
			builder.append(" left join ").append(schema).append(".DMM_ASSET_TYPE peripherals2 on peripherals2.ASSET_TYPE_ID = caseHandleInfo.PERIPHERALS2 ");
			builder.append(" left join ").append(schema).append(".DMM_ASSET_TYPE peripherals3 on peripherals3.ASSET_TYPE_ID = caseHandleInfo.PERIPHERALS3 ");
			builder.append(" left join ").append(schema).append(".BASE_PARAMETER_ITEM_DEF installType ON installType.ITEM_VALUE = caseHandleInfo.INSTALL_TYPE");
			builder.append(" AND installType.BPTD_CODE = :installTypeParam ");
			sql.addFromExpression(builder.toString());
			// 查詢參數,DTID
			if (StringUtils.hasText(edcParameterFormDTO.getQueryDtid())) {
				sql.addWhereClause(" caseHandleInfo.DTID like :dtid", edcParameterFormDTO.getQueryDtid() + IAtomsConstants.MARK_PERCENT);
			}
			// 查詢參數,客戶
			if (StringUtils.hasText(edcParameterFormDTO.getQueryCustomerId())) {
				sql.addWhereClause(" caseHandleInfo.CUSTOMER_ID=:customerId", edcParameterFormDTO.getQueryCustomerId());
			}
			// 查詢參數,特店代號
			if (StringUtils.hasText(edcParameterFormDTO.getQueryMerchantCode())) {
				sql.addWhereClause("merchant.MERCHANT_CODE like :merchantCode", edcParameterFormDTO.getQueryMerchantCode() + IAtomsConstants.MARK_PERCENT);
			}
			// 查詢參數,特點名稱
			if (StringUtils.hasText(edcParameterFormDTO.getQueryMerchantName())) {
				sql.addWhereClause("merchant.NAME like :merchantName", edcParameterFormDTO.getQueryMerchantName() + IAtomsConstants.MARK_PERCENT);
			}
			// 查詢參數,表頭（同對外名稱）
			if (StringUtils.hasText(edcParameterFormDTO.getQueryMerAnnouncedName())) {
				sql.addWhereClause(" merchantHead.HEADER_NAME like :merAnnouncedName", edcParameterFormDTO.getQueryMerAnnouncedName() + IAtomsConstants.MARK_PERCENT);
			}
			// 查詢參數,TID
			if (StringUtils.hasText(edcParameterFormDTO.getQueryTid())) {
				sql.addWhereClause(" exists(select * from " + schema + ".SRM_CASE_NEW_TRANSACTION_PARAMETER transParameter where transParameter.CASE_ID = caseHandleInfo.CASE_ID and transParameter.TID like :queryTid) ");
			}
			
			// 查詢參數,EDC 機型
			if (StringUtils.hasText(edcParameterFormDTO.getQueryEdcType())) {
				sql.addWhereClause(" caseHandleInfo.EDC_TYPE in(:queryEdcType) ");
			}
			// 查詢參數,週邊設備機型
			if (StringUtils.hasText(edcParameterFormDTO.getQueryPeripheralEquipment())) {
				sql.addWhereClause(" (caseHandleInfo.PERIPHERALS in (:queryPeripheralEquipment) or caseHandleInfo.PERIPHERALS2 in (:queryPeripheralEquipment) or caseHandleInfo.PERIPHERALS3 in (:queryPeripheralEquipment)) ");
			}
			// 查詢參數,設備支援功能
			if (StringUtils.hasText(edcParameterFormDTO.getQueryAssetSupportedFunction())) {
				sql.addWhereClause(" exists(select 1 from " + schema + ".SRM_CASE_NEW_ASSET_FUNCTION assetFunction where assetFunction.CASE_ID = caseHandleInfo.CASE_ID and assetFunction.FUNCTION_ID in (:queryAssetSupportedFunction)) ");
			}
			// 查詢參數,已開放交易
			if (StringUtils.hasText(edcParameterFormDTO.getQueryOpenTransaction())) {
				sql.addWhereClause(" exists(select * from " + schema + ".SRM_CASE_NEW_TRANSACTION_PARAMETER transParameter where transParameter.CASE_ID = caseHandleInfo.CASE_ID and transParameter.TRANSACTION_TYPE in (:queryOpenTransaction))  ");
			}
			// 設備開啟模式
			if(StringUtils.hasText(edcParameterFormDTO.getQueryAssetOpenMode())){
				// 選擇內建
				if(edcParameterFormDTO.getQueryAssetOpenMode().contains(IAtomsConstants.PARAM_BUILT_IN)){
					sql.addWhereClause(" caseHandleInfo.BUILT_IN_FEATURE is not null and caseHandleInfo.BUILT_IN_FEATURE <>'' ");
				}
				// 選擇外接
				if(edcParameterFormDTO.getQueryAssetOpenMode().contains(IAtomsConstants.PARAM_EXTERNAL)){
					builder.delete(0, builder.length());
					builder.append("((caseHandleInfo.PERIPHERALS is not null and caseHandleInfo.PERIPHERALS <>'') ");
					builder.append(" or (caseHandleInfo.PERIPHERALS2 is not null and caseHandleInfo.PERIPHERALS2 <>'') ");
					builder.append(" or (caseHandleInfo.PERIPHERALS3 is not null and caseHandleInfo.PERIPHERALS3 <>'')) ");
					sql.addWhereClause(builder.toString());
				//	sql.addWhereClause(" (caseHandleInfo.PERIPHERALS is not null or caseHandleInfo.PERIPHERALS2 is not null or caseHandleInfo.PERIPHERALS3 is not null )");
				}
			}
			// 排序
			if(StringUtils.hasText(edcParameterFormDTO.getSort()) && StringUtils.hasText(edcParameterFormDTO.getOrder())){
				sql.setOrderByExpression(edcParameterFormDTO.getSort() + " " + edcParameterFormDTO.getOrder());
			} else {
				sql.setOrderByExpression("company.SHORT_NAME, merchant.MERCHANT_CODE asc");
			}
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			
			sqlQueryBean.setParameter("ticketTypeParam", IATOMS_PARAM_TYPE.TICKET_TYPE.getCode());
			sqlQueryBean.setParameter("transactionCategoryParam", IATOMS_PARAM_TYPE.TRANSACTION_CATEGORY.getCode());
			sqlQueryBean.setParameter("supportedFunctionParam", IATOMS_PARAM_TYPE.SUPPORTED_FUNCTION.getCode());
			sqlQueryBean.setParameter("caseLinkEdcType", IAtomsConstants.PARAM_CASE_LINK_EDC_TYPE);
			sqlQueryBean.setParameter("caseLinkPeripherals", IAtomsConstants.PARAM_CASE_LINK_PERIPHERALS);
			sqlQueryBean.setParameter("caseLinkPeripherals2", IAtomsConstants.PARAM_CASE_LINK_PERIPHERALS2);
			sqlQueryBean.setParameter("caseLinkPeripherals3", IAtomsConstants.PARAM_CASE_LINK_PERIPHERALS3);
			sqlQueryBean.setParameter("locationParam", IATOMS_PARAM_TYPE.LOCATION.getCode());
			sqlQueryBean.setParameter("doubleModuleParam", IATOMS_PARAM_TYPE.DOUBLE_MODULE.getCode());
			sqlQueryBean.setParameter("ecrLineParam", IATOMS_PARAM_TYPE.ECR_LINE.getCode());
			sqlQueryBean.setParameter("commModeParam", IATOMS_PARAM_TYPE.COMM_MODE.getCode());
			sqlQueryBean.setParameter("netVendorParam", IATOMS_PARAM_TYPE.NET_VENDOR.getCode());
			sqlQueryBean.setParameter("installTypeParam", IATOMS_PARAM_TYPE.INSTALL_TYPE.getCode());
			
			sqlQueryBean.setParameter("installCaseCategory", IAtomsConstants.CASE_CATEGORY.INSTALL.getCode());
			
			// 分頁設置
			if(edcParameterFormDTO.getRows() != null && edcParameterFormDTO.getPage() != null){
				sqlQueryBean.setPageSize(edcParameterFormDTO.getRows());
				sqlQueryBean.setStartPage(edcParameterFormDTO.getPage() - 1);
			}
			// 查詢參數,TID
			if (StringUtils.hasText(edcParameterFormDTO.getQueryTid())) {
				sqlQueryBean.setParameter("queryTid", edcParameterFormDTO.getQueryTid() + IAtomsConstants.MARK_PERCENT);
			}
			// 查詢參數,TID
			if (StringUtils.hasText(edcParameterFormDTO.getQueryTid())) {
				sqlQueryBean.setParameter("queryTid", edcParameterFormDTO.getQueryTid() + IAtomsConstants.MARK_PERCENT);
			}
			// 查詢參數,EDC 機型
			if (StringUtils.hasText(edcParameterFormDTO.getQueryEdcType())) {
				sqlQueryBean.setParameter("queryEdcType", StringUtils.toList(edcParameterFormDTO.getQueryEdcType(), IAtomsConstants.MARK_SEPARATOR));
			}
			// 查詢參數,週邊設備機型
			if (StringUtils.hasText(edcParameterFormDTO.getQueryPeripheralEquipment())) {
				sqlQueryBean.setParameter("queryPeripheralEquipment", StringUtils.toList(edcParameterFormDTO.getQueryPeripheralEquipment(), IAtomsConstants.MARK_SEPARATOR));
			}
			// 查詢參數,設備支援功能
			if (StringUtils.hasText(edcParameterFormDTO.getQueryAssetSupportedFunction())) {
				sqlQueryBean.setParameter("queryAssetSupportedFunction", StringUtils.toList(edcParameterFormDTO.getQueryAssetSupportedFunction(), IAtomsConstants.MARK_SEPARATOR));
			}
			// 查詢參數,已開放交易
			if (StringUtils.hasText(edcParameterFormDTO.getQueryOpenTransaction())) {
				sqlQueryBean.setParameter("queryOpenTransaction", StringUtils.toList(edcParameterFormDTO.getQueryOpenTransaction(), IAtomsConstants.MARK_SEPARATOR));
			}
			logger.debug("listBy()", "sql:" + sql.toString());
			AliasBean aliasBean = sql.createAliasBean(SrmCaseHandleInfoDTO.class);
			srmCaseHandleInfoDTOs = (List<SrmCaseHandleInfoDTO>) this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			logger.error(".listBy()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return srmCaseHandleInfoDTOs;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IEdcParameterDAO#count(com.cybersoft4u.xian.iatoms.common.bean.formDTO.EdcParameterFormDTO)
	 */
	@Override
	public Integer count(EdcParameterFormDTO edcParameterFormDTO) throws DataAccessException {
		logger.debug("listBy()", "parameters:queryMerchantCode = " + edcParameterFormDTO.getQueryMerchantCode());
		logger.debug("listBy()", "parameters:queryMerchantName = " + edcParameterFormDTO.getQueryMerchantName());
		logger.debug("listBy()", "parameters:queryMerAnnouncedName = " + edcParameterFormDTO.getQueryMerAnnouncedName());
		logger.debug("listBy()", "parameters:queryTid = " + edcParameterFormDTO.getQueryTid());
		logger.debug("listBy()", "parameters:queryDtid = " + edcParameterFormDTO.getQueryDtid());
		logger.debug("listBy()", "parameters:queryEdcType = " + edcParameterFormDTO.getQueryEdcType());
		logger.debug("listBy()", "parameters:queryPeripheralEquipment = " + edcParameterFormDTO.getQueryPeripheralEquipment());
		logger.debug("listBy()", "parameters:queryAssetSupportedFunction = " + edcParameterFormDTO.getQueryAssetSupportedFunction());
		logger.debug("listBy()", "parameters:queryAssetOpenMode = " + edcParameterFormDTO.getQueryAssetOpenMode());
		logger.debug("listBy()", "parameters:queryAssetStatus = " + edcParameterFormDTO.getQueryAssetStatus());
		logger.debug("listBy()", "parameters:queryOpenTransaction = " + edcParameterFormDTO.getQueryOpenTransaction());
		try {
			// 拿到schema
			String schema = this.getMySchema();
			SqlStatement sql = new SqlStatement();
			// 查询语句
			sql.addSelectClause("count(1)");
			StringBuilder builder = new StringBuilder();
			builder.append(schema).append(".SRM_CASE_NEW_HANDLE_INFO caseHandleInfo ");
			builder.append(" left join ").append(schema).append(".BIM_MERCHANT merchant on caseHandleInfo.MERCHANT_CODE = merchant.MERCHANT_ID ");
			builder.append(" left join ").append(schema).append(".BIM_MERCHANT_HEADER merchantHead on caseHandleInfo.MERCHANT_HEADER_ID = merchantHead.MERCHANT_HEADER_ID" );
			sql.setFromExpression(builder.toString());
			// 查詢參數,DTID
			if (StringUtils.hasText(edcParameterFormDTO.getQueryDtid())) {
				sql.addWhereClause(" caseHandleInfo.DTID like :dtid", edcParameterFormDTO.getQueryDtid() + IAtomsConstants.MARK_PERCENT);
			}
			// 查詢參數,客戶
			if (StringUtils.hasText(edcParameterFormDTO.getQueryCustomerId())) {
				sql.addWhereClause(" caseHandleInfo.CUSTOMER_ID=:customerId", edcParameterFormDTO.getQueryCustomerId());
			}
			// 查詢參數,特店代號
			if (StringUtils.hasText(edcParameterFormDTO.getQueryMerchantCode())) {
				sql.addWhereClause("merchant.MERCHANT_CODE like :merchantCode", edcParameterFormDTO.getQueryMerchantCode() + IAtomsConstants.MARK_PERCENT);
			}
			// 查詢參數,特點名稱
			if (StringUtils.hasText(edcParameterFormDTO.getQueryMerchantName())) {
				sql.addWhereClause("merchant.NAME like :merchantName", edcParameterFormDTO.getQueryMerchantName() + IAtomsConstants.MARK_PERCENT);
			}
			// 查詢參數,表頭（同對外名稱）
			if (StringUtils.hasText(edcParameterFormDTO.getQueryMerAnnouncedName())) {
				sql.addWhereClause(" merchantHead.HEADER_NAME like :merAnnouncedName", edcParameterFormDTO.getQueryMerAnnouncedName() + IAtomsConstants.MARK_PERCENT);
			}
			// 查詢參數,TID
			if (StringUtils.hasText(edcParameterFormDTO.getQueryTid())) {
				sql.addWhereClause(" exists(select 1 from " + schema + ".SRM_CASE_NEW_TRANSACTION_PARAMETER transParameter where transParameter.CASE_ID = caseHandleInfo.CASE_ID and transParameter.TID like :queryTid) ");
			}
			
			// 查詢參數,EDC 機型
			if (StringUtils.hasText(edcParameterFormDTO.getQueryEdcType())) {
				sql.addWhereClause(" caseHandleInfo.EDC_TYPE in(:queryEdcType) ");
			}
			// 查詢參數,週邊設備機型
			if (StringUtils.hasText(edcParameterFormDTO.getQueryPeripheralEquipment())) {
				sql.addWhereClause(" (caseHandleInfo.PERIPHERALS in (:queryPeripheralEquipment) or caseHandleInfo.PERIPHERALS2 in (:queryPeripheralEquipment) or caseHandleInfo.PERIPHERALS3 in (:queryPeripheralEquipment)) ");
			}
			// 查詢參數,設備支援功能
			if (StringUtils.hasText(edcParameterFormDTO.getQueryAssetSupportedFunction())) {
				sql.addWhereClause(" exists(select 1 from " + schema + ".SRM_CASE_NEW_ASSET_FUNCTION assetFunction where assetFunction.CASE_ID = caseHandleInfo.CASE_ID and assetFunction.FUNCTION_ID in (:queryAssetSupportedFunction)) ");
			}
			// 查詢參數,已開放交易
			if (StringUtils.hasText(edcParameterFormDTO.getQueryOpenTransaction())) {
				sql.addWhereClause(" exists(select 1 from " + schema + ".SRM_CASE_NEW_TRANSACTION_PARAMETER transParameter where transParameter.CASE_ID = caseHandleInfo.CASE_ID and transParameter.TRANSACTION_TYPE in (:queryOpenTransaction))  ");
			}
			// 設備開啟模式
			if(StringUtils.hasText(edcParameterFormDTO.getQueryAssetOpenMode())){
				// 選擇內建
				if(edcParameterFormDTO.getQueryAssetOpenMode().contains(IAtomsConstants.PARAM_BUILT_IN)){
					sql.addWhereClause(" caseHandleInfo.BUILT_IN_FEATURE is not null and caseHandleInfo.BUILT_IN_FEATURE <>'' ");
				}
				// 選擇外接
				if(edcParameterFormDTO.getQueryAssetOpenMode().contains(IAtomsConstants.PARAM_EXTERNAL)){
					builder.delete(0, builder.length());
					builder.append("((caseHandleInfo.PERIPHERALS is not null and caseHandleInfo.PERIPHERALS <>'') ");
					builder.append(" or (caseHandleInfo.PERIPHERALS2 is not null and caseHandleInfo.PERIPHERALS2 <>'') ");
					builder.append(" or (caseHandleInfo.PERIPHERALS3 is not null and caseHandleInfo.PERIPHERALS3 <>'')) ");
					sql.addWhereClause(builder.toString());
				//	sql.addWhereClause(" (caseHandleInfo.PERIPHERALS is not null or caseHandleInfo.PERIPHERALS2 is not null or caseHandleInfo.PERIPHERALS3 is not null )");
				}
			}
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			// 查詢參數,TID
			if (StringUtils.hasText(edcParameterFormDTO.getQueryTid())) {
				sqlQueryBean.setParameter("queryTid", edcParameterFormDTO.getQueryTid() + IAtomsConstants.MARK_PERCENT);
			}
			// 查詢參數,EDC 機型
			if (StringUtils.hasText(edcParameterFormDTO.getQueryEdcType())) {
				sqlQueryBean.setParameter("queryEdcType", StringUtils.toList(edcParameterFormDTO.getQueryEdcType(), IAtomsConstants.MARK_SEPARATOR));
			}
			// 查詢參數,週邊設備機型
			if (StringUtils.hasText(edcParameterFormDTO.getQueryPeripheralEquipment())) {
				sqlQueryBean.setParameter("queryPeripheralEquipment", StringUtils.toList(edcParameterFormDTO.getQueryPeripheralEquipment(), IAtomsConstants.MARK_SEPARATOR));
			}
			// 查詢參數,設備支援功能
			if (StringUtils.hasText(edcParameterFormDTO.getQueryAssetSupportedFunction())) {
				sqlQueryBean.setParameter("queryAssetSupportedFunction", StringUtils.toList(edcParameterFormDTO.getQueryAssetSupportedFunction(), IAtomsConstants.MARK_SEPARATOR));
			}
			// 查詢參數,已開放交易
			if (StringUtils.hasText(edcParameterFormDTO.getQueryOpenTransaction())) {
				sqlQueryBean.setParameter("queryOpenTransaction", StringUtils.toList(edcParameterFormDTO.getQueryOpenTransaction(), IAtomsConstants.MARK_SEPARATOR));
			}
			logger.debug("count()", "sql:" + sql.toString());
			List<Integer> result = this.getDaoSupport().findByNativeSql(sqlQueryBean);
			if (!CollectionUtils.isEmpty(result)) {
				return result.get(0).intValue();
			}
			return 0;
		} catch (Exception e) {
			logger.error("count()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IEdcParameterDAO#getItemList()
	 */
	@Override
	public List<Parameter> getItemList() throws DataAccessException {
		List<Parameter> transactionParameterItemList = null;
		try {
			// 拿到schema
			String schema = this.getMySchema();
			SqlStatement sql = new SqlStatement();
			// 查询语句
			sql.addSelectClause("parameterItem.PARAMTER_ITEM_CODE", Parameter.FIELD_VALUE);
			sql.addSelectClause("parameterItem.PARAMTER_ITEM_NAME", Parameter.FIELD_NAME);
			sql.setFromExpression(schema + ".SRM_TRANSACTION_PARAMETER_ITEM parameterItem");
			sql.setOrderByExpression("parameterItem.ITEM_ORDER ");
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			logger.debug("getItemList()", "sql:" + sql.toString());
			AliasBean aliasBean = sql.createAliasBean(Parameter.class);
			transactionParameterItemList = (List<Parameter>) this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			logger.error(".getItemList()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return transactionParameterItemList;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.IEdcParameterDAO#getTransParamsByDtid(java.util.List)
	 */
	public List<SrmCaseNewTransactionParameterDTO> getTransParamsByDtid(List<String> dtidList) throws DataAccessException {
		List<SrmCaseNewTransactionParameterDTO> transactionParameterDTOs = null;
		try {
			// 拿到schema
			String schema = this.getMySchema();
			SqlStatement sql = new SqlStatement();
			sql.addSelectClause("transParameter.TRANSACTION_TYPE", SrmCaseNewTransactionParameterDTO.ATTRIBUTE.TRANSACTION_TYPE.getValue());
			sql.addSelectClause("bptd.ITEM_NAME", SrmCaseNewTransactionParameterDTO.ATTRIBUTE.TRANSACTION_TYPE_NAME.getValue());
			sql.addSelectClause("transParameter.MERCHANT_CODE", SrmCaseNewTransactionParameterDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
		//	sql.addSelectClause("transParameter.MERCHANT_CODE_OTHER", SrmCaseNewTransactionParameterDTO.ATTRIBUTE.MERCHANT_CODE_OTHER.getValue());
			sql.addSelectClause("transParameter.TID", SrmCaseNewTransactionParameterDTO.ATTRIBUTE.TID.getValue());
			sql.addSelectClause("transParameter.ITEM_VALUE", SrmCaseNewTransactionParameterDTO.ATTRIBUTE.ITEM_VALUE.getValue());
			sql.addSelectClause("transParameter.DTID", SrmCaseNewTransactionParameterDTO.ATTRIBUTE.DTID.getValue());
			StringBuilder builder = new StringBuilder();
			builder.append(schema).append(".SRM_CASE_NEW_TRANSACTION_PARAMETER transParameter (select distinct ITEM_VALUE,ITEM_NAME from ");
			builder.append(schema).append(".BASE_PARAMETER_ITEM_DEF WHERE BPTD_CODE = 'TRANSACTION_CATEGORY')bptd on bptd.ITEM_VALUE = transParameter.TRANSACTION_TYPE");
			if(dtidList.size() == 1){
				sql.addWhereClause("transParameter.dtid =:queryDtid", dtidList.get(0));
			} else {
				sql.addWhereClause("transParameter.dtid in (:queryDtidList)");
			}
			SqlQueryBean sqlQueryBean = sql.createSqlQueryBean();
			if(dtidList.size() != 1){
				sqlQueryBean.setParameter("queryDtidList", dtidList);
			}
			logger.debug(".listBy()", "sql:"+ sql.toString());
			AliasBean aliasBean = sql.createAliasBean(SrmCaseNewTransactionParameterDTO.class);
			transactionParameterDTOs = (List<SrmCaseNewTransactionParameterDTO>) this.getDaoSupport().findByNativeSql(sqlQueryBean, aliasBean);
		} catch (Exception e) {
			logger.error(".getItemList()", e.getMessage(), e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
		return transactionParameterDTOs;
	}
}
