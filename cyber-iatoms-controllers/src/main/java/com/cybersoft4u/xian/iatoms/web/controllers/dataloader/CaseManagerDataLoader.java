package com.cybersoft4u.xian.iatoms.web.controllers.dataloader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.Parameter;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.dataloader.BaseInitialDataLoader;
import cafe.core.web.controller.util.SessionHelper;
import cafe.core.web.locator.IServiceLocatorProxy;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ApplicationDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetTypeDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractAssetDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimDepartmentDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimMerchantHeaderDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ContractSlaDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmTransactionParameterItemDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.BaseParameterManagerFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractManageFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractSlaFormDTO;
import com.cybersoft4u.xian.iatoms.web.controllers.ajax.IIAtomsAjaxService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Purpose:案件處理 InitialDataLoader 
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016年12月13日
 * @MaintenancePersonnel CrissZhang
 */
public class CaseManagerDataLoader extends BaseInitialDataLoader<IIAtomsAjaxService> {
	/**
	 * 日誌掛件
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.COMMON, CaseManagerDataLoader.class);
	
	/**
	 * service代理
	 */
	private IServiceLocatorProxy serviceLocator;

	/**
	 * Constructor:無參構造函數
	 */
	public CaseManagerDataLoader() {
	}

	/** (non-Javadoc)
	 * @see cafe.core.web.controller.dataloader.BaseInitialDataLoader#load(javax.servlet.http.HttpServletRequest, cafe.core.context.SessionContext)
	 */
	@Override
	public void load(HttpServletRequest request, SessionContext sessionContext)
			throws Exception {
		long startDataLoaderTime = System.currentTimeMillis();
		super.load(request, sessionContext);
		long endSuperTime = System.currentTimeMillis();
		LOGGER.debug("calculate time --> load", "DataLoader superTime:" + (endSuperTime - startDataLoaderTime));
		try {
			if (sessionContext != null && sessionContext.getReturnMessage() != null) {
				String ucNo = this.getUseCaseNo(sessionContext);
				CaseManagerFormDTO formDTO = (CaseManagerFormDTO) sessionContext.getRequestParameter();
				IAtomsLogonUser logonUser = (IAtomsLogonUser) SessionHelper.getLogonUser(request);
				SrmCaseHandleInfoDTO srmCaseHandleInfoDTO = formDTO.getSrmCaseHandleInfoDTO();
				MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			//	SimpleDateFormat sf = new SimpleDateFormat(DateTimeUtils.DT_FMT_YYYYMMDD_DASH);
				SessionContext returnCtx = null;
				List<Parameter> customers = null;
				
				Gson gsonss = new GsonBuilder().create();
				if (IAtomsConstants.ACTION_INIT_DETAIL.equals(sessionContext.getActionId())
						|| IAtomsConstants.ACTION_INIT_EDIT.equals(sessionContext.getActionId())
						|| IAtomsConstants.ACTION_INIT_DTID.equals(sessionContext.getActionId())) {
						//|| IAtomsConstants.ACTION_SHOW_DETAIL_INFO.equals(sessionContext.getActionId())) {
					//新裝機操作
					//客戶列表   裝機為客戶下拉菜單，其他為文本框
					//類型為客戶
					if (formDTO.getIsVendorAttribute()) {
						param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
						//登錄驗證方式為iatoms
						param.addParameter(CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue(), IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE);
						returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_COMPANY_SERVICE,
								IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
						customers = (List<Parameter>) returnCtx.getResponseResult();
						//Task #3583  客戶廠商     
					} else if (formDTO.getIsCustomerAttribute() || formDTO.getIsCustomerVendorAttribute()) {
						//客戶
						if (logonUser != null) {
							customers = new ArrayList<Parameter>();
							customers.add(new Parameter(logonUser.getCompanyName(), logonUser.getCompanyId()));
						}
					} else {
						customers = new ArrayList<Parameter>();
					}
					//Task #3583  客戶廠商
					if (!formDTO.getIsVendorAttribute() && formDTO.getIsCustomerVendorAttribute()) {
						
					} else if(IAtomsConstants.ACTION_INIT_EDIT.equals(sessionContext.getActionId())){
						param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
						//登錄驗證方式為iatoms
						param.addParameter(CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue(), IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE);
						returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_COMPANY_SERVICE,
								IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
						customers = (List<Parameter>) returnCtx.getResponseResult();
					}
				}
				
				long startQueryTime = System.currentTimeMillis();
				
				if (IAtomsConstants.ACTION_INIT_DETAIL.equals(sessionContext.getActionId())
						|| IAtomsConstants.ACTION_INIT_EDIT.equals(sessionContext.getActionId())) {
					// 所有公司
					List<Parameter> allCompanyList = null;
					param = new MultiParameterInquiryContext();
					List<String> tempString = new ArrayList<String>();
					tempString.add(IAtomsConstants.PARAM_COMPANY_TYPE_VENDOR);
					/*tempString.add(IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
					tempString.add(IAtomsConstants.PARAM_COMPANY_TYPE_HARDWARE_VENDOR);*/
					param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), tempString);
					returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_COMPANY_SERVICE,
							IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
					allCompanyList = (List<Parameter>) returnCtx.getResponseResult();
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_ALL_COMPANY_LIST, allCompanyList);
					
					// 支援功能json字符串
					List<Parameter> supportedFunctionList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.SUPPORTED_FUNCTION.getCode());
				
					//将集合转化为JSON字符串
					String supportedFunctionListStr = gsonss.toJson(supportedFunctionList);
					SessionHelper.setAttribute(request, ucNo, CaseManagerFormDTO.PARAM_SUPPORTED_FUNCTION_LIST_STR, supportedFunctionListStr);
				}
				
				if (IAtomsConstants.ACTION_INIT_DETAIL.equals(sessionContext.getActionId()) ||
						IAtomsConstants.ACTION_INIT_EDIT.equals(sessionContext.getActionId()) ||
						IAtomsConstants.ACTION_SHOW_DETAIL_INFO.equals(sessionContext.getActionId())) {
					String versionDate = null;
					versionDate = DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDD_DASH);
					//案件類型為其他不加載交易參數區塊
					//加載交易參數數據交易參數項目list
					param = new MultiParameterInquiryContext();
					param.addParameter(SrmTransactionParameterItemDTO.ATTRIBUTE.EFFECTIVE_DATE.getValue(), versionDate);
					returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE,
							IAtomsConstants.ACTION_GET_TRANSACTION_PARAMETER_ITEM_LIST, param);
					List<Parameter> transationParameterList = (List<Parameter>) returnCtx.getResponseResult();
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_TRANSACTION_PARAMETER_ITEM_LIST, transationParameterList);
					
					//首先取出案件類別
					String caseCategory = formDTO.getCaseCategory();
					//加載案件類型可以修改的交易參數欄位列表
					param = new MultiParameterInquiryContext();
					param.addParameter(BaseParameterManagerFormDTO.PARAMETER_PARENT_BPTD_CODE, IATOMS_PARAM_TYPE.TICKET_TYPE.getCode());
					param.addParameter(BaseParameterManagerFormDTO.PARAMETER_PARENT_ITEM_VALUE, caseCategory);
					param.addParameter(BaseParameterManagerFormDTO.PARAMETER_CHILDREN_BPTD_CODE, IATOMS_PARAM_TYPE.TRANSACTION_CATEGORY.getCode());
					param.addParameter(BaseParameterManagerFormDTO.EDIT_EFFECITVE_DATE, DateTimeUtils.parseDate(versionDate, DateTimeUtils.DT_FMT_YYYYMMDD_DASH));
					returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_BASE_PARAMETER_MANAGER_SERVICE, IAtomsConstants.ACTION_GET_PARAMETER_ITEMS_BY_PARENT, param);
					List<Parameter> transationTypeParameterList = (List<Parameter>) returnCtx.getResponseResult();
					String transationTypeParameterListString = gsonss.toJson(transationTypeParameterList);
					SessionHelper.setAttribute(request, ucNo, IATOMS_PARAM_TYPE.TRANSACTION_CATEGORY.getCode(), transationTypeParameterListString);
					
					//加載不同交易類別，可修改列名。
					param = new MultiParameterInquiryContext();
					param.addParameter(SrmTransactionParameterItemDTO.ATTRIBUTE.EFFECTIVE_DATE.getValue(), versionDate);
					returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE,
							IAtomsConstants.ACTION_GET_EDIT_FIELDS_GROUP_BY_TRANSACTION_TYPE, param);
					Map<String,List<String>> editFildsMap = (Map<String,List<String>>) returnCtx.getResponseResult();
					String editFildsMapString = gsonss.toJson(editFildsMap);
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_TRANSACTION_PARAMETER_EDIT_FIELDS_MAP, editFildsMapString);
					
					//Task #3433 (處理報修原因列表)
					List<Parameter> repairReasonList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.REPAIR_REASON.getCode());
					param = new MultiParameterInquiryContext();
					param.addParameter(BaseParameterManagerFormDTO.EDIT_BPTD_CODE, IATOMS_PARAM_TYPE.REPAIR_REASON.getCode());
					param.addParameter(BaseParameterManagerFormDTO.PARAMETER_TEXT_FIELD1, IAtomsConstants.PARAM_TSB_EDC);
					returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_BASE_PARAMETER_MANAGER_SERVICE,
							IAtomsConstants.ACTION_GET_PARAMETER_ITEMS_BY_TEXT_FIELD, param);
					List<Parameter> repairReasonTaiXins = (List<Parameter>) returnCtx.getResponseResult();
					if (!CollectionUtils.isEmpty(repairReasonTaiXins)) {
						for (Parameter repairReasonTaiXin : repairReasonTaiXins) {
							for (Parameter repairReason : repairReasonList) {
								if (StringUtils.hasText((String)repairReason.getValue())
										&& repairReason.getValue().equals(repairReasonTaiXin.getValue())) {
									repairReasonList.remove(repairReason);
									break;
								}
							}
						}
					}
					SessionHelper.setAttribute(request, ucNo, CaseManagerFormDTO.REPAIR_REASON_LIST, repairReasonList);
					SessionHelper.setAttribute(request, ucNo, CaseManagerFormDTO.REPAIR_REASON_TAIXIN_LIST, repairReasonTaiXins);
					if (IAtomsConstants.ACTION_SHOW_DETAIL_INFO.equals(sessionContext.getActionId())) {
						//Task #3483
						//獲取裝機
						String location = srmCaseHandleInfoDTO.getInstalledAdressLocation();
						param = new MultiParameterInquiryContext();
						param.addParameter(BimMerchantHeaderDTO.ATTRIBUTE.LOCATION.getValue(), location);
						returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_MERCHANT_HEADER_SERVICE, "getPostCodeList", param);
						List<Parameter> postCodeList = (List<Parameter>) returnCtx.getResponseResult();
						SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_INSTALLED_POST_CODE_LIST, postCodeList);
						//獲取聯系
						location = srmCaseHandleInfoDTO.getContactAddressLocation();
						if (StringUtils.hasText(location)) {
							param = new MultiParameterInquiryContext();
							param.addParameter(BimMerchantHeaderDTO.ATTRIBUTE.LOCATION.getValue(), location);
							returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_MERCHANT_HEADER_SERVICE, "getPostCodeList", param);
							postCodeList = (List<Parameter>) returnCtx.getResponseResult();
							SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_CONTACT_POST_CODE_LIST, postCodeList);
						}
						//獲取營業地址
						location = srmCaseHandleInfoDTO.getLocation();
						if (StringUtils.hasText(location)) {
							param = new MultiParameterInquiryContext();
							param.addParameter(BimMerchantHeaderDTO.ATTRIBUTE.LOCATION.getValue(), location);
							returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_MERCHANT_HEADER_SERVICE, "getPostCodeList", param);
							postCodeList = (List<Parameter>) returnCtx.getResponseResult();
							SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_LOCATION_POST_CODE_LIST, postCodeList);
						}
					}
				}
				
				if (IAtomsConstants.ACTION_INIT.equals(sessionContext.getActionId())) {
					if (formDTO.getIsVendorAttribute()) {
						param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
						//登錄驗證方式為iatoms
						param.addParameter(CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue(), IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE);
						returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_COMPANY_SERVICE,
								IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
						customers = (List<Parameter>) returnCtx.getResponseResult();
						//Task #3583  客戶廠商     
					} else if (formDTO.getIsCustomerAttribute() || formDTO.getIsCustomerVendorAttribute()) {
						//客戶
						if (logonUser != null) {
							customers = new ArrayList<Parameter>();
							customers.add(new Parameter(logonUser.getCompanyName(), logonUser.getCompanyId()));
						}
					} else {
						customers = new ArrayList<Parameter>();
					}
					
					//PARAM_EDC_TYPE_LIST
					// EDC設備列表
					List<Parameter> edcTypeList = null;
					param.addParameter(BimContractAssetDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), IAtomsConstants.ASSET_CATEGORY_EDC);
					returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE, IAtomsConstants.ACTION_GET_ASSET_TYPE_LIST, param);
					edcTypeList = (List<Parameter>) returnCtx.getResponseResult();
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_EDC_TYPE_LIST, edcTypeList);
					//PARAM_VENDOR_LIST
					// 維護廠商
					List<Parameter> vendorList = null;
					param = new MultiParameterInquiryContext();
					param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_VENDOR);
					returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_COMPANY_SERVICE,
							IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
					vendorList = (List<Parameter>) returnCtx.getResponseResult();
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_VENDOR_LIST, vendorList);
					//PARAM_DEPT_LIST
					List<Parameter> departmentList = new ArrayList<Parameter>();
					List<Parameter> departmentCybList = new ArrayList<Parameter>();
					param = new MultiParameterInquiryContext();
					List<Parameter> caseGroupList = (List<Parameter>) SessionHelper.getAttribute(request,
							ucNo, IATOMS_PARAM_TYPE.CASE_GROUP.getCode());
					
					// 若為客服、TMS、QA，取得所有維護廠商下的【部門維護】資料之【部門名稱】依部門名稱升序顯示 // CR #2951 廠商客服
					if (formDTO.getIsQA()||formDTO.getIsTMS()||formDTO.getIsCustomerService()||formDTO.getIsVendorService()) {
						returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_BIM_DEPARTMENT_SERVICE, IAtomsConstants.ACTION_GET_CDEPARTMENT_LIST, param);
						departmentList = (List<Parameter>) returnCtx.getResponseResult();
						//Task3460
						param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_ID.getValue(), "10000000-01");
						returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_BIM_DEPARTMENT_SERVICE, IAtomsConstants.ACTION_GET_CDEPARTMENT_LIST, param);
						departmentCybList = (List<Parameter>) returnCtx.getResponseResult();
					} else {
						param = new MultiParameterInquiryContext();
						//如果當前登錄這為廠商agent
						//CR #2394 加cyberagent角色 update by 2017/09/13 //Task #3578 客戶廠商客服
						if (formDTO.getIsVendorAgent() || formDTO.getIsCyberAgent()||formDTO.getIsCusVendorService()) {
							param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_ID.getValue(), logonUser.getCompanyId());
							returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_BIM_DEPARTMENT_SERVICE, IAtomsConstants.ACTION_GET_CDEPARTMENT_LIST, param);
							departmentList = (List<Parameter>) returnCtx.getResponseResult();
							departmentCybList = this.depthCopy(departmentList);
							//或者當前登錄這為部門agent 或 工程師
						} else if (formDTO.getIsAgent() || formDTO.getIsEngineer()) {
							param.addParameter(BimDepartmentDTO.ATTRIBUTE.DEPT_CODE.getValue(), logonUser.getAdmUserDTO().getDeptCode());
							returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_BIM_DEPARTMENT_SERVICE, IAtomsConstants.ACTION_GET_CDEPARTMENT_LIST, param);
							departmentList = (List<Parameter>) returnCtx.getResponseResult();
							departmentCybList = this.depthCopy(departmentList);
							//其他角色
						}
						if (!"10000000-01".equals(logonUser.getCompanyId())) {
							departmentCybList = new ArrayList<Parameter>(); 
						}
					}
					//將客服，TMS，QA拼入派工單位下拉選單中
					if (!CollectionUtils.isEmpty(departmentList)) {
						departmentList.addAll(caseGroupList);
					}
					SessionHelper.setAttribute(request, ucNo, CaseManagerFormDTO.PARAM_DEPT_LIST, departmentList);
					
					//将集合转化为JSON字符串
				    String departmentListString = gsonss.toJson(departmentList);
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_DEPARTMENT_LIST, departmentListString);
					
					//加載CMS所需派工單爲下拉列表
					/*param = new MultiParameterInquiryContext();
					param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_ID.getValue(), "10000000-01");
					returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_BIM_DEPARTMENT_SERVICE, IAtomsConstants.ACTION_GET_CDEPARTMENT_LIST, param);*/
					/*departmentList = (List<Parameter>) returnCtx.getResponseResult();*/
					//將客服，TMS，QA拼入派工單位下拉選單中
					if (!CollectionUtils.isEmpty(departmentCybList)) {
						departmentCybList.addAll(caseGroupList);
					}
					departmentListString = gsonss.toJson(departmentCybList);
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_DEPARTMENT_CYB_LIST, departmentListString);
					
					// 維護部門下拉框
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_CASE_PART_GROUP_LIST, caseGroupList);
					//将集合转化为JSON字符串
					String casePartGroupListStr = gsonss.toJson(caseGroupList);
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_CASE_PART_GROUP_LIST_STR, casePartGroupListStr);
										
					// 查核結果
					List<Parameter> checkResultsList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.CHECK_RESULTS.getCode());
					String checkResultsListStr = gsonss.toJson(checkResultsList);
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.CHECK_RESULTS_LIST_STR, checkResultsListStr);
					
					// 問題原因列表
					param = new MultiParameterInquiryContext();
					param.addParameter(CaseManagerFormDTO.CUSTOMER_IS_TSB_EDC, Boolean.FALSE);
					returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_GET_PROBLEM_REASON_LIST, param);
					List<Parameter> problemReasonList = (List<Parameter>) returnCtx.getResponseResult();
					String problemReasonListStr = gsonss.toJson(problemReasonList);
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PROBLEM_REASON_LIST_STR, problemReasonListStr);
					// 問題原因(台新)列表
					param = new MultiParameterInquiryContext();
					param.addParameter(CaseManagerFormDTO.CUSTOMER_IS_TSB_EDC, Boolean.TRUE);
					returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_GET_PROBLEM_REASON_LIST, param);
					problemReasonList = (List<Parameter>) returnCtx.getResponseResult();
					problemReasonListStr = gsonss.toJson(problemReasonList);
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PROBLEM_REASON_TSB_LIST_STR, problemReasonListStr);
					
					// 問題解決方式
					param = new MultiParameterInquiryContext();
					param.addParameter(CaseManagerFormDTO.CUSTOMER_IS_TSB_EDC, Boolean.FALSE);
					returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_GET_PROBLEM_SOLUTION_LIST, param);
					List<Parameter> problemSolutionList = (List<Parameter>) returnCtx.getResponseResult();
					String problemSolutionListStr = gsonss.toJson(problemSolutionList);
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PROBLEM_SOLUTION_LIST_STR, problemSolutionListStr);
					// 問題解決方式(台新)列表
					param = new MultiParameterInquiryContext();
					param.addParameter(CaseManagerFormDTO.CUSTOMER_IS_TSB_EDC, Boolean.TRUE);
					returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_GET_PROBLEM_SOLUTION_LIST, param);
					problemSolutionList = (List<Parameter>) returnCtx.getResponseResult();
					problemSolutionListStr = gsonss.toJson(problemSolutionList);
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PROBLEM_SOLUTION_TSB_LIST_STR, problemSolutionListStr);
					
					// 責任歸屬
					List<Parameter> responsibityList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.RESPON_ATTRIBUTION.getCode());
					String responsibityListStr = gsonss.toJson(responsibityList);
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.RESPONSIBITY_LIST_STR, responsibityListStr);
					
					// 案件狀態集合
					List<String> caseStatusListStr = new ArrayList<String>();
					caseStatusListStr.add(IAtomsConstants.CASE_STATUS.WAIT_DISPATCH.getCode());
					caseStatusListStr.add(IAtomsConstants.CASE_STATUS.DISPATCHED.getCode());
					caseStatusListStr.add(IAtomsConstants.CASE_STATUS.RESPONSED.getCode());
					caseStatusListStr.add(IAtomsConstants.CASE_STATUS.DELAYING.getCode());
					caseStatusListStr.add(IAtomsConstants.CASE_STATUS.ARRIVED.getCode());
					caseStatusListStr.add(IAtomsConstants.CASE_STATUS.COMPLETED.getCode());
					// Task #2540 [案件狀態]預設選項，移除 待結案審查
					//	caseStatusListStr.add(IAtomsConstants.CASE_STATUS.WAIT_CLOSE.getCode());
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_CASE_STATUS_LIST_STR, caseStatusListStr);
					
					//案件類型
					List<Parameter> ticketModeList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.TICKET_MODE.getCode());
					String ticketModeListString = gsonss.toJson(ticketModeList);
					SessionHelper.setAttribute(request, ucNo, CaseManagerFormDTO.PARAM_TICKET_MODE, ticketModeListString);
					
					
					// 案件匯入類別
					String accessRghts = logonUser.getAccRghts().get(ucNo);
					if(StringUtils.hasText(accessRghts)){
						List<String> rghtList = StringUtils.toList(accessRghts, IAtomsConstants.MARK_SEPARATOR);
						//案件類別
						List<Parameter> ticketTypeList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.TICKET_TYPE.getCode());
						// 案件匯入類別
						List<Parameter> importTicketTypes = new ArrayList<Parameter>();
						for(Parameter parameter : ticketTypeList){
							//由于其他案件类型目前不支持汇入功能，所以去除下拉列表的中其他
							if (IAtomsConstants.CASE_CATEGORY.OTHER.getCode().equals(parameter.getValue())) {
								continue;
							}
							for(String tempRght : rghtList){
								if(((String)parameter.getValue()).equals(tempRght)){
									importTicketTypes.add(parameter);
									break;
								}
							}
						}
						SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_IMPORT_TICKET_TYPES, importTicketTypes);
					}
					// 責任歸屬
					List<Parameter> logisticsVendors = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.LOGISTICS_VENDOR.getCode());
					String logisticsVendorStr = gsonss.toJson(logisticsVendors);
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.LOGISTICS_VENDOR_LIST_STR, logisticsVendorStr);
				}
				SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_CUSTOMER_LIST, customers);
				
				if (IAtomsConstants.ACTION_INIT.equals(sessionContext.getActionId()) || 
						IAtomsConstants.ACTION_INIT_EDIT.equals(sessionContext.getActionId()) ||
						IAtomsConstants.ACTION_INIT_DETAIL.equals(sessionContext.getActionId())) {
					//工單範本集合
					returnCtx = this.serviceLocator.doService(null, 
							IAtomsConstants.SERVICE_SRM_CASE_TEMPLATES_SERVICE,
							IAtomsConstants.ACTION_GET_TEMPLATES_LATES, new MultiParameterInquiryContext());
					List<Parameter> templatesList = (List<Parameter>) returnCtx.getResponseResult();
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_TEMPLATES_LIST, templatesList);
				}
				
				if (IAtomsConstants.ACTION_INIT_CHOOSE_EDC.equals(sessionContext.getActionId())) {
					// 設備名稱列表
					List<Parameter> assetNameList = null;
					param.addParameter(BimContractAssetDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), null);
					returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE, IAtomsConstants.PARAM_ASSET_LIST, param);
					assetNameList = (List<Parameter>) returnCtx.getResponseResult();
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_ASSET_ANME_LIST, assetNameList);
					
					// 仓库据点列表
					returnCtx = this.serviceLocator.doService(null, 
							IAtomsConstants.SERVICE_WAREHOUSE_SERVICE,
							IAtomsConstants.ACTION_GET_WAREHOUSE_BY_USER_ID, IAtomsConstants.MARK_EMPTY_STRING);
					List<Parameter> warehouseList = (List<Parameter>) returnCtx.getResponseResult();
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_WAREHOUSE_LIST, warehouseList);
				}				
				
				
				
				
				// 編輯頁面查詢聯動信息
				if(IAtomsConstants.ACTION_INIT_EDIT.equals(sessionContext.getActionId())){
					// 裝機
					if(IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())
							|| IAtomsConstants.CASE_CATEGORY.OTHER.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())){
						// 客戶
						String customerId = srmCaseHandleInfoDTO.getCustomerId();
						
						/*客戶聯動合約處理*/
						// 忽略刪除
						boolean ignoreDeletedContract = true;
						if(IAtomsConstants.YES.equals(srmCaseHandleInfoDTO.getFomsCase())){
							ignoreDeletedContract = false;
						}
						param = new MultiParameterInquiryContext();
						// 設置客户编号
						param.addParameter(ContractSlaFormDTO.ORIGINAL_CUSTOMER_ID, customerId);
						// 設置合約狀態
						param.addParameter(BimContractDTO.ATTRIBUTE.CONTRACT_STATUS.getValue(), null);
						// 是否有sla信息
						param.addParameter(ContractSlaDTO.ATTRIBUTE.IS_HAVE_SLA.getValue(), false);
						// 忽略刪除
						param.addParameter(IAtomsConstants.PARAM_IGNORE_DELETED, ignoreDeletedContract);
						returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.PARAM_CONTRACT_MANAGE_SERVICE, IAtomsConstants.ACTION_GET_CONTRACT_CODE_LIST, param);
						List<Parameter> contractList = (List<Parameter>) returnCtx.getResponseResult();
						SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_CONTRACT_LIST, contractList);
						
						/*// 待派工、派工
						if(IAtomsConstants.CASE_STATUS.WAIT_DISPATCH.getCode().equals(srmCaseHandleInfoDTO.getCaseStatus())
								|| IAtomsConstants.CASE_STATUS.DISPATCHED.getCode().equals(srmCaseHandleInfoDTO.getCaseStatus())){
							// 合約
							String contractId = srmCaseHandleInfoDTO.getContractId();
							合約維護廠商處理
							param = new MultiParameterInquiryContext();
							param.addParameter(BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue(), contractId);
							returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.PARAM_CONTRACT_MANAGE_SERVICE, IAtomsConstants.ACTION_GET_VENDORS, param);
							List<Parameter> vendors = (List<Parameter>) returnCtx.getResponseResult();
							SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_VENDORS, vendors);
						}*/
					// 其他案件類別
					} else {
						
					}
					
					// Task #3028 併機 異動 拆機 查核 專案 報修，建案之維護廠商，DTID預設帶出後要能再調整
					// 待派工、派工
					if(IAtomsConstants.CASE_STATUS.WAIT_DISPATCH.getCode().equals(srmCaseHandleInfoDTO.getCaseStatus())
							|| IAtomsConstants.CASE_STATUS.DISPATCHED.getCode().equals(srmCaseHandleInfoDTO.getCaseStatus())){
						// 合約
						String contractId = srmCaseHandleInfoDTO.getContractId();
						/*合約維護廠商處理*/
						param = new MultiParameterInquiryContext();
						param.addParameter(BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue(), contractId);
						returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.PARAM_CONTRACT_MANAGE_SERVICE, IAtomsConstants.ACTION_GET_VENDORS, param);
						List<Parameter> vendors = (List<Parameter>) returnCtx.getResponseResult();
						SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_VENDORS, vendors);
					}
					
					
					// 裝機+異動+倂幾
					if(IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())
							|| IAtomsConstants.CASE_CATEGORY.UPDATE.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())
							|| IAtomsConstants.CASE_CATEGORY.MERGE.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())
							|| IAtomsConstants.CASE_CATEGORY.OTHER.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())
							|| IAtomsConstants.CASE_CATEGORY.PROJECT.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())){
						// 客戶
						String customerId = srmCaseHandleInfoDTO.getCustomerId();
						// 刷卡機型
						String edcType = srmCaseHandleInfoDTO.getEdcType();
						// 裝機+異動
						if(!IAtomsConstants.CASE_CATEGORY.MERGE.getCode().equals(srmCaseHandleInfoDTO.getCaseCategory())){
							// 特店代號
							String merchantId = srmCaseHandleInfoDTO.getMerchantCode();
							/*特店特店表頭*/
							param = new MultiParameterInquiryContext();
							param.addParameter(BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_ID.getValue(), merchantId);
							param.addParameter(BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), null);
							param.addParameter(BimMerchantHeaderDTO.ATTRIBUTE.COMPANY_ID.getValue(), null);
							returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_MERCHANT_HEADER_SERVICE, IAtomsConstants.GET_MERCHANT_HEADER_LIST, param);
							List<Parameter> merchantHeaders = (List<Parameter>) returnCtx.getResponseResult();
							SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_MERCHANT_HEADERS, merchantHeaders);
							
							/*客戶刷卡機型處理*/
							// 忽略刪除
							boolean ignoreDeletedEdc = true;
							if(IAtomsConstants.YES.equals(srmCaseHandleInfoDTO.getFomsCase())){
								ignoreDeletedEdc = false;
							}
							param = new MultiParameterInquiryContext();
							// Task #2945 立即結案、結案查詢設備不要使用人條件
							if(IAtomsConstants.CASE_STATUS.IMMEDIATE_CLOSE.getCode().equals(srmCaseHandleInfoDTO.getCaseStatus())
									|| IAtomsConstants.CASE_STATUS.CLOSED.getCode().equals(srmCaseHandleInfoDTO.getCaseStatus())){
								param.addParameter(AssetTypeDTO.ATTRIBUTE.COMPANY_ID.getValue(), null);
							} else {
								param.addParameter(AssetTypeDTO.ATTRIBUTE.COMPANY_ID.getValue(), customerId);
							}
						//	param.addParameter(AssetTypeDTO.ATTRIBUTE.COMPANY_ID.getValue(), customerId);
							// 設備類別 Task #2496 
							param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), IAtomsConstants.ASSET_CATEGORY_EDC);
							// 忽略刪除
							param.addParameter(IAtomsConstants.PARAM_IGNORE_DELETED, ignoreDeletedEdc);
							returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE, IAtomsConstants.ACTION_GET_ASSET_LIST_FOR_CASE, param);
							List<Parameter> edcAssets = (List<Parameter>) returnCtx.getResponseResult();
							SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_EDC_ASSETS, edcAssets);
							
							/*客戶聯動周邊設備處理*/
/*							param = new MultiParameterInquiryContext();
							// Task #2945 立即結案、結案、作廢查詢設備不要使用人條件
							if(IAtomsConstants.CASE_STATUS.IMMEDIATE_CLOSE.getCode().equals(srmCaseHandleInfoDTO.getCaseStatus())
									|| IAtomsConstants.CASE_STATUS.CLOSED.getCode().equals(srmCaseHandleInfoDTO.getCaseStatus())
									|| IAtomsConstants.CASE_STATUS.VOIDED.getCode().equals(srmCaseHandleInfoDTO.getCaseStatus())){
								param.addParameter(AssetTypeDTO.ATTRIBUTE.COMPANY_ID.getValue(), null);
							} else {
								param.addParameter(AssetTypeDTO.ATTRIBUTE.COMPANY_ID.getValue(), customerId);
							}
						//	param.addParameter(AssetTypeDTO.ATTRIBUTE.COMPANY_ID.getValue(), customerId);
							// 設備類別 Task #2496 
							param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), IAtomsConstants.ASSET_CATEGORY_RODUND_ASSET);
							// 忽略刪除
							param.addParameter(IAtomsConstants.PARAM_IGNORE_DELETED, ignoreDeletedEdc);*/
							
							// 設備類別 
							param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), IAtomsConstants.ASSET_CATEGORY_RODUND_ASSET);
							returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE, IAtomsConstants.ACTION_GET_ASSET_LIST_FOR_CASE, param);
							List<Parameter> peripheralsList = (List<Parameter>) returnCtx.getResponseResult();
							SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_PERIPHERALS_LIST, peripheralsList);
							
							// 有刷卡機型
							if(StringUtils.hasText(edcType)){
								/*刷卡機型聯動內建功能處理*/
								param = new MultiParameterInquiryContext();
								param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), edcType);
								returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE, IAtomsConstants.ACTION_GET_BUILT_IN_FEATURE, param);
								List<Parameter> builtInFeatures = (List<Parameter>) returnCtx.getResponseResult();
								SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_BUILT_IN_FEATURES, builtInFeatures);
								
								/*刷卡機型聯動連線方式處理*/
								param = new MultiParameterInquiryContext();
								param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), edcType);
								returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE, IAtomsConstants.ACTION_GET_CONNECTION_TYPE_LIST, param);
								List<Parameter> connectionTypes = (List<Parameter>) returnCtx.getResponseResult();
								SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_CONNECTION_TYPES, connectionTypes);
							} else {
								SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_BUILT_IN_FEATURES, null);
								SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_CONNECTION_TYPES, null);
							}
							
							// 周邊設備1
							String peripherals = srmCaseHandleInfoDTO.getPeripherals();
							if(StringUtils.hasText(peripherals)){
								/*周邊設備1聯動周邊設備1功能處理*/
								param = new MultiParameterInquiryContext();
								param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), peripherals);
								returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE, IAtomsConstants.ACTION_GET_BUILT_IN_FEATURE, param);
								List<Parameter> peripheralsFunctions = (List<Parameter>) returnCtx.getResponseResult();
								SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_PERIPHERALS_FUNCTIONS, peripheralsFunctions);
							} else {
								SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_PERIPHERALS_FUNCTIONS, null);
							}
							// 周邊設備2
							String peripherals2 = srmCaseHandleInfoDTO.getPeripherals2();
							if(StringUtils.hasText(peripherals2)){
								/*周邊設備2聯動周邊設備1功能處理*/
								param = new MultiParameterInquiryContext();
								param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), peripherals2);
								returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE, IAtomsConstants.ACTION_GET_BUILT_IN_FEATURE, param);
								List<Parameter> peripheralsFunction2s = (List<Parameter>) returnCtx.getResponseResult();
								SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_PERIPHERALS_FUNCTION2S, peripheralsFunction2s);
							} else {
								SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_PERIPHERALS_FUNCTION2S, null);
							}
							// 周邊設備3
							String peripherals3 = srmCaseHandleInfoDTO.getPeripherals3();
							if(StringUtils.hasText(peripherals3)){
								/*周邊設備3聯動周邊設備1功能處理*/
								param = new MultiParameterInquiryContext();
								param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), peripherals3);
								returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE, IAtomsConstants.ACTION_GET_BUILT_IN_FEATURE, param);
								List<Parameter> peripheralsFunction3s = (List<Parameter>) returnCtx.getResponseResult();
								SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_PERIPHERALS_FUNCTION3S, peripheralsFunction3s);
							} else {
								SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_PERIPHERALS_FUNCTION3S, null);
							}
						}
						/*客戶聯動軟體版本處理*/
						String searchDeletedFlag = IAtomsConstants.NO;
						if(IAtomsConstants.YES.equals(srmCaseHandleInfoDTO.getFomsCase())){
							searchDeletedFlag = IAtomsConstants.YES;
						}
						param = new MultiParameterInquiryContext();
						param.addParameter(ApplicationDTO.ATTRIBUTE.CUSTOMER_ID.getValue(), customerId);
						param.addParameter(ApplicationDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), edcType);
						param.addParameter(ApplicationDTO.ATTRIBUTE.SEARCH_DELETED_FLAG.getValue(), searchDeletedFlag);
						returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_APPLICATION_SERVICE, IAtomsConstants.ACTION_GET_SOFTWAREVERSIONS, param);
						List<Parameter> softwareVersions = (List<Parameter>) returnCtx.getResponseResult();
						SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_SOFTWARE_VERSIONS, softwareVersions);
					}
					// 維護廠商
					String companyId = srmCaseHandleInfoDTO.getCompanyId();
					
					/*維護廠商部門處理*/
					// 忽略刪除
					boolean ignoreDeletedDept = true;
					if(IAtomsConstants.YES.equals(srmCaseHandleInfoDTO.getFomsCase())){
						ignoreDeletedDept = false;
					}
					param = new MultiParameterInquiryContext();
					param.addParameter(AdmUserDTO.ATTRIBUTE.COMPANY_ID.getValue(),companyId);
					// 忽略刪除
					param.addParameter(IAtomsConstants.PARAM_IGNORE_DELETED, ignoreDeletedDept);
					returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_BIM_DEPARTMENT_SERVICE, IAtomsConstants.ACTION_GET_DEPT_LIST, param);
					List<Parameter> deptList = (List<Parameter>) returnCtx.getResponseResult();
					deptList.add(new Parameter(i18NUtil.getName(IAtomsConstants.FIELD_CASE_ROLE_CUSTOMER_SERVICE), IAtomsConstants.CASE_ROLE.CUSTOMER_SERVICE.getCode()));
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_DEPT_LIST, deptList);
					//Task #3483
					//獲取裝機
					String location = srmCaseHandleInfoDTO.getInstalledAdressLocation();
					param = new MultiParameterInquiryContext();
					param.addParameter(BimMerchantHeaderDTO.ATTRIBUTE.LOCATION.getValue(), location);
					returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_MERCHANT_HEADER_SERVICE, "getPostCodeList", param);
					List<Parameter> postCodeList = (List<Parameter>) returnCtx.getResponseResult();
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_INSTALLED_POST_CODE_LIST, postCodeList);
					//獲取聯系
					location = srmCaseHandleInfoDTO.getContactAddressLocation();
					if (StringUtils.hasText(location)) {
						param = new MultiParameterInquiryContext();
						param.addParameter(BimMerchantHeaderDTO.ATTRIBUTE.LOCATION.getValue(), location);
						returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_MERCHANT_HEADER_SERVICE, "getPostCodeList", param);
						postCodeList = (List<Parameter>) returnCtx.getResponseResult();
						SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_CONTACT_POST_CODE_LIST, postCodeList);
					}
					//獲取營業地址
					location = srmCaseHandleInfoDTO.getLocation();
					if (StringUtils.hasText(location)) {
						param = new MultiParameterInquiryContext();
						param.addParameter(BimMerchantHeaderDTO.ATTRIBUTE.LOCATION.getValue(), location);
						returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_MERCHANT_HEADER_SERVICE, "getPostCodeList", param);
						postCodeList = (List<Parameter>) returnCtx.getResponseResult();
						SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_LOCATION_POST_CODE_LIST, postCodeList);
					}
				}
				
				
				// 案件建案（客戶角色）
				if(IAtomsConstants.ACTION_INIT_DETAIL.equals(sessionContext.getActionId())){
					// 裝機
					if(IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(formDTO.getCaseCategory())
						|| IAtomsConstants.CASE_CATEGORY.OTHER.getCode().equals(formDTO.getCaseCategory())){
						// 客戶角色	//Task #3583  客戶廠商     
						if(!formDTO.getIsVendorAttribute() && (formDTO.getIsCustomerAttribute() || formDTO.getIsCustomerVendorAttribute())){
							// 客戶
							String customerId = logonUser.getCompanyId();
/*							// 設置客户编号
							param.addParameter(ContractSlaFormDTO.ORIGINAL_CUSTOMER_ID, customerId);
							// 設置合約狀態
							param.addParameter(BimContractDTO.ATTRIBUTE.CONTRACT_STATUS.getValue(), null);
							// 是否有sla信息
							param.addParameter(ContractSlaDTO.ATTRIBUTE.IS_HAVE_SLA.getValue(), false);
							// 忽略刪除
							param.addParameter(IAtomsConstants.PARAM_IGNORE_DELETED, true);*/
							// Bug #3020 案件管理-合約編號未排序
							param.addParameter(ContractSlaFormDTO.ORIGINAL_CUSTOMER_ID, customerId);
							param.addParameter(IAtomsConstants.QUERY_PAGE_ORDER, ContractManageFormDTO.PARAM_ORDER_BY_CONTRACT_CODE);
							param.addParameter(IAtomsConstants.PARAM_PAGE_ORDER, IAtomsConstants.PARAM_PAGE_ORDER);
							
							returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.PARAM_CONTRACT_MANAGE_SERVICE, IAtomsConstants.ACTION_GET_CONTRACT_CODE_LIST, param);
							List<Parameter> contractList = (List<Parameter>) returnCtx.getResponseResult();
							SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_CONTRACT_LIST, contractList);
							
							// 拿到第一筆有edc的合約
							param = new MultiParameterInquiryContext();
							param.addParameter(BimContractDTO.ATTRIBUTE.COMPANY_ID.getValue(), customerId);
							returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.PARAM_CONTRACT_MANAGE_SERVICE, IAtomsConstants.ACTION_GET_HAVE_EDC_CONTRACT, param);
							
							// 合約
							String contractId = (String) returnCtx.getResponseResult();
							SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_CONTRACT_ID, contractId);
							if(StringUtils.hasText(contractId)){
								/*合約維護廠商處理*/
								param = new MultiParameterInquiryContext();
								param.addParameter(BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue(), contractId);
								returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.PARAM_CONTRACT_MANAGE_SERVICE, IAtomsConstants.ACTION_GET_VENDORS, param);
								List<Parameter> vendors = (List<Parameter>) returnCtx.getResponseResult();
								SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_VENDORS, vendors);
							}
							
							
							/*客戶刷卡機型處理*/
							param = new MultiParameterInquiryContext();
							param.addParameter(AssetTypeDTO.ATTRIBUTE.COMPANY_ID.getValue(), customerId);
							// 設備類別 Task #2496 
							param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), IAtomsConstants.ASSET_CATEGORY_EDC);
							// 忽略刪除
							param.addParameter(IAtomsConstants.PARAM_IGNORE_DELETED, true);
							returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE, IAtomsConstants.ACTION_GET_ASSET_LIST_FOR_CASE, param);
							List<Parameter> edcAssets = (List<Parameter>) returnCtx.getResponseResult();
							SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_EDC_ASSETS, edcAssets);
							
							/*客戶聯動周邊設備處理*/
							param = new MultiParameterInquiryContext();
							param.addParameter(AssetTypeDTO.ATTRIBUTE.COMPANY_ID.getValue(), customerId);
							// 設備類別 Task #2496 
							param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), IAtomsConstants.ASSET_CATEGORY_RODUND_ASSET);
							// 忽略刪除
							param.addParameter(IAtomsConstants.PARAM_IGNORE_DELETED, true);
							returnCtx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_ASSET_TYPE_SERVICE, IAtomsConstants.ACTION_GET_ASSET_LIST_FOR_CASE, param);
							List<Parameter> peripheralsList = (List<Parameter>) returnCtx.getResponseResult();
							SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_PERIPHERALS_LIST, peripheralsList);
						}
					}
				}
				
				// 初始化頁面以及欄位頁面
				if(IAtomsConstants.ACTION_INIT_COLUMN_BLOCK.equals(sessionContext.getActionId()) || IAtomsConstants.ACTION_INIT.equals(sessionContext.getActionId())){
					param = new MultiParameterInquiryContext();
					param.addParameter(AdmUserDTO.ATTRIBUTE.USER_ID.getValue(), logonUser.getId());
					// 獲取用戶所有欄位模板
					returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_SRM_QUERY_TEMPLATE_SERVICE,
							IAtomsConstants.ACTION_GET_CURRENT_COLUMN_TEMPLATE, param);
					Parameter currentColumnTemplate = (Parameter) returnCtx.getResponseResult();
					SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_CURRENT_COLUMN_TEMPLATE, currentColumnTemplate);
					
					// 初始化欄位區塊
					if(IAtomsConstants.ACTION_INIT_COLUMN_BLOCK.equals(sessionContext.getActionId())){
						// 獲取用戶所有欄位模板
						returnCtx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_SRM_QUERY_TEMPLATE_SERVICE,
								IAtomsConstants.ACTION_GET_USER_COLUMN_TEMPLATE_LIST, param);
						List<Parameter> columnTemplateList = (List<Parameter>) returnCtx.getResponseResult();
						SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_COLUMN_TEMPLATE_LIST, columnTemplateList);
						
						// 獲取所有欄位集合
						List<Parameter> allColumnsList = new ArrayList<Parameter>();
						String allColumns = null;
						StringBuilder builder = new StringBuilder();
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_STATUS_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_CONDITION.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_CONDITION.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_CONDITION.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_STATUS.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_STATUS.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_STATUS.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_TYPE_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALL_TYPE_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE_CONTRACT.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.IS_PROJECT.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.PROJECT_CODE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.PROJECT_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.VENDOR_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.VENDOR_STAFF.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.SAME_INSTALLED.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.ATTENDANCE_TIMES.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.TID.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.HEADER_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.OLD_MERCHANT_CODE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.AREA_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_TEL.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_TEL2.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						//Task #3343
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_EMAIL.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.PHONE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.BUSINESS_HOURS.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.AO_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.AO_EMAIL.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						//Task #3117 新增 案件區域欄位
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_AREA.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.BUSINESS_ADDRESS.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_PHONE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_PHONE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						//Task #3343
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_MOBILE_PHONE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_MOBILE_PHONE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_EMAIL.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_EMAIL.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_SERIAL_NUMBER.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.ENABLE_DATE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.WAREHOUSE_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.OPEN_FUNCTION_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.APPLICATION_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.MULTI_MODULE_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.ECR_CONNECTION.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.NETWORK_LINE_NUMBER.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_SERIAL_NUMBER.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_CONTRACT.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION2_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_SERIAL_NUMBER.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_CONTRACT.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION3_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_SERIAL_NUMBER.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_CONTRACT.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.CONNECTION_TYPE_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.LOGO_STYLE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.IS_OPEN_ENCRYPT.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_PAY_PLATFORM.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_INVOICE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_QUICK_PASS.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.NET_VENDOR_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.LOCALHOST_IP.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.NETMASK.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.GATEWAY.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.DESCRIPTION.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE_OTHER.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_TRANS_TYPE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.DCC_TRANS_TYPE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.AE_MID.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.AE_TID.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.TMS_PARAM_DESC.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATED_DESCRIPTION.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.REPAIR_REASON_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_REASON_CATEGORY_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_REASON_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_SOLUTION_CATEGORY_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.PROBLEM_SOLUTION_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSIBITY_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_RESPONSE_DATE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_ARRIVE_DATE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_FINISH_DATE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_USER_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_DATE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_USER_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.RESPONSE_DATE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_USER_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.ARRIVE_DATE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_USER_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DEPARTMENT_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.ANALYZE_USER_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.ANALYZE_DATE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.CLOSE_USER_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.CLOSE_DATE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.PROCESS_TYPE_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_PROCESS_USERNAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.LOGISTICS_VENDOR.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.LOGISTICS_NUMBER.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.RECEIPT_TYPE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.UPDATED_DATE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_BY_NAME.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.CREATED_DATE.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						//CR #2869 新增三個欄位 2017/11/22
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.FIRST_DESCRIPTION.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.SECOND_DESCRIPTION.getValue()).append(IAtomsConstants.MARK_SEPARATOR);
						builder.append(SrmCaseHandleInfoDTO.ATTRIBUTE.THIRD_DESCRIPTION.getValue());
						// Task #3205 是否執行過延期
						builder.append(IAtomsConstants.MARK_SEPARATOR).append(SrmCaseHandleInfoDTO.ATTRIBUTE.HAS_DELAY.getValue());
						allColumns = builder.toString();
						for(String tempColumn : StringUtils.toList(allColumns, IAtomsConstants.MARK_SEPARATOR)){
							allColumnsList.add(new Parameter(i18NUtil.getName(tempColumn), tempColumn));
						}
						SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_ALL_COLUMNS_LIST, allColumnsList);
					}
				}
			}
		} catch(Exception e) {
			LOGGER.error("load()", e.getMessage(), e);
			throw new Exception(IAtomsMessageCode.BASE_PARAMETER_VALUE_IS_NULL, e);
		}
		long endDataLoaderTime = System.currentTimeMillis();
		LOGGER.debug("calculate time --> load", "DataLoader totalTime:" + (endDataLoaderTime - startDataLoaderTime));
	}

	private List<Parameter> depthCopy(List<Parameter> fromObject) {
		List<Parameter> toObject = new ArrayList<Parameter>();
		if (!CollectionUtils.isEmpty(fromObject)) {
			for (Parameter parameter : fromObject) {
				toObject.add(parameter);
			}
		}
		return toObject;
	}
	
	/**
	 * @return the serviceLocator
	 */
	public IServiceLocatorProxy getServiceLocator() {
		return serviceLocator;
	}

	/**
	 * @param serviceLocator the serviceLocator to set
	 */
	public void setServiceLocator(IServiceLocatorProxy serviceLocator) {
		this.serviceLocator = serviceLocator;
	}
}