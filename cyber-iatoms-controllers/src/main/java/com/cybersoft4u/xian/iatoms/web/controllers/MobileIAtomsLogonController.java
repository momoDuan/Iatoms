package com.cybersoft4u.xian.iatoms.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;

import cafe.core.bean.Parameter;
import cafe.core.bean.identity.LogonUser;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.BaseParameterInquiryContext;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.exception.CommonException;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.core.web.controller.util.SessionHelper;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ReportSettingDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ReportSettingFormDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCompany;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 * Purpose:  帐号登录Controller
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016/3/30
 * @MaintenancePersonnel CrissZhang
 */
@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
public class MobileIAtomsLogonController<T extends IAtomsLogonUser> extends IAtomsLogonController<T> {
	
	/**
	 * 系统日志记录工具
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.CONTROLLER, MobileIAtomsLogonController.class);
	
	
	/**
	 * (non-Javadoc)
	 * @see cafe.core.web.controller.AbstractBaseFormController#postSubmited(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, cafe.core.context.SessionContext)
	 */
	@Override
	public ModelAndView postSubmited(HttpServletRequest request,
			HttpServletResponse response, SessionContext ctx)
			throws CommonException {
		LOGGER.debug("Start to enter "+this.getClass().getSimpleName()+" postSubmited..");
		CaseManagerFormDTO formDTO = new CaseManagerFormDTO();
		formDTO.setActionId(IAtomsConstants.ACTION_INIT);// casemanager init
		String ucNo = IAtomsConstants.UC_NO_SRM_05020;
		formDTO.setUseCaseNo(ucNo);
		IAtomsLogonUser logonUser = (IAtomsLogonUser) SessionHelper.getLogonUser(request);
		setUserRoleFlag(formDTO, logonUser);
		
		ctx.setResponseResult(formDTO);
		ctx.setRequestParameter(formDTO);
		
		//SessionContext returnCtx = null;
		List<Parameter> customers = new ArrayList<Parameter>();
		List<Parameter> locations = new ArrayList<Parameter>();
		List<String> caseStatusList = new ArrayList<String>();
		List<String> caseStatusListStr = new ArrayList<String>();
		Gson gsonss = new GsonBuilder().create();
		MultiParameterInquiryContext param = new MultiParameterInquiryContext();

		
		try {
			param = new MultiParameterInquiryContext();
			param.addParameter("logonUser", logonUser);
			//判斷是客戶還是廠商
			ctx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, "getUserRoleAttribute", param);
			boolean isCustomerAttribute = (Boolean) ctx.getAttribute("isCustomerAttribute");
			boolean isVendorAttribute = (Boolean) ctx.getAttribute("isVendorAttribute");
			// Task #3578 客戶廠商客服
			boolean isCustomerVendorAttribute = (Boolean) ctx.getAttribute("isCustomerVendorAttribute");
			formDTO.setIsCustomerAttribute(isCustomerAttribute);
			formDTO.setIsVendorAttribute(isVendorAttribute);
			formDTO.setIsCustomerVendorAttribute(isCustomerVendorAttribute);
			//類型為客戶
			if (formDTO.getIsVendorAttribute()) {
				param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
				//登錄驗證方式為iatoms
				param.addParameter(CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue(), IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE);
				ctx = this.serviceLocator.doService(null, IAtomsConstants.SERVICE_COMPANY_SERVICE,
						IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
				customers = (List<Parameter>) ctx.getResponseResult();
				// Task #3578 客戶廠商客服
			} else if (formDTO.getIsCustomerAttribute()|| formDTO.getIsCustomerVendorAttribute()) {
				//客戶
				if (logonUser != null) {
					customers = new ArrayList<Parameter>();
					customers.add(new Parameter(logonUser.getCompanyName(), logonUser.getCompanyId()));
				}
			} else {
				customers = new ArrayList<Parameter>();
			}
			
			//ctx = this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_COMPANY_SERVICE, IAtomsConstants.ACTION_GET_COMPANY_LIST, param);
			//customers = (List<Parameter>) ctx.getResponseResult();
			LOGGER.debug("customers ==> "+customers.size());
			SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_CUSTOMER_LIST, customers);
			//獲取縣市下拉列表
			BaseParameterInquiryContext inquiryContext = new BaseParameterInquiryContext();
			inquiryContext.setParameterType("LOCATION");
			ctx= this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_BASE_PARAMETER_SERVICE, IAtomsConstants.ACTION_GET_PARAMETER_ITEMS, inquiryContext);
			locations = (List<Parameter>) ctx.getResponseResult();
			LOGGER.debug("locations ==> "+locations.size());
			SessionHelper.setAttribute(request, ucNo, IATOMS_PARAM_TYPE.LOCATION.getCode(), locations);
			
			// 案件狀態集合
			inquiryContext.setParameterType("CASE_STATUS");
			ctx= this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_BASE_PARAMETER_SERVICE, IAtomsConstants.ACTION_GET_PARAMETER_ITEMS, inquiryContext);
			caseStatusList = (List<String>) ctx.getResponseResult();
			LOGGER.debug("caseStatusList ==> "+caseStatusList.size()+" : "+caseStatusList.toString());
			SessionHelper.setAttribute(request, ucNo, IATOMS_PARAM_TYPE.CASE_STATUS.getCode(), caseStatusList);
			caseStatusListStr.add(IAtomsConstants.CASE_STATUS.RESPONSED.getCode());
			caseStatusListStr.add(IAtomsConstants.CASE_STATUS.ARRIVED.getCode());
			LOGGER.debug("caseStatusListStr ==> "+caseStatusListStr.size());
			SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PARAM_CASE_STATUS_LIST_STR, caseStatusListStr);
			// 問題原因列表 CR #3239
			param = new MultiParameterInquiryContext();
			param.addParameter(CaseManagerFormDTO.CUSTOMER_IS_TSB_EDC, Boolean.FALSE);
			ctx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_GET_PROBLEM_REASON_LIST, param);
			List<Parameter> problemReasonList = (List<Parameter>) ctx.getResponseResult();
			SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PROBLEM_REASON_LIST_STR, problemReasonList);		
			//Task #3560 問題原因(台新)列表
			param = new MultiParameterInquiryContext();
			param.addParameter(CaseManagerFormDTO.CUSTOMER_IS_TSB_EDC, Boolean.TRUE);
			ctx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_GET_PROBLEM_REASON_LIST, param);
			problemReasonList = (List<Parameter>) ctx.getResponseResult();
			SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PROBLEM_REASON_TSB_LIST_STR, problemReasonList);
			
			// 問題解決方式
			param = new MultiParameterInquiryContext();
			param.addParameter(CaseManagerFormDTO.CUSTOMER_IS_TSB_EDC, Boolean.FALSE);
			ctx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_GET_PROBLEM_SOLUTION_LIST, param);
			List<Parameter> problemSolutionList = (List<Parameter>) ctx.getResponseResult();
			for(int i = 0; i < problemSolutionList.size(); i++) {
				if(problemSolutionList.get(i).getValue().equals("PROBLEM_SOLUTION_MER-PROVIDE_NEW_EDC")){
					problemSolutionList.remove(problemSolutionList.get(i));
				}
				if(problemSolutionList.get(i).getValue().equals("PROBLEM_SOLUTION_EDC-CHANGE_EDC")){
					problemSolutionList.remove(problemSolutionList.get(i));
				}
				if(problemSolutionList.get(i).getValue().equals("PROBLEM_SOLUTION_OTHER-07")){
					problemSolutionList.remove(problemSolutionList.get(i));
				}
			}
			SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PROBLEM_SOLUTION_LIST_STR, problemSolutionList);
			
			//Task #3560 問題解決方式(台新)列表
			param = new MultiParameterInquiryContext();
			param.addParameter(CaseManagerFormDTO.CUSTOMER_IS_TSB_EDC, Boolean.TRUE);
			ctx = super.getServiceLocator().doService(null, IAtomsConstants.SERVICE_CASE_MANAGER_SERVICE, IAtomsConstants.ACTION_GET_PROBLEM_SOLUTION_LIST, param);
			problemSolutionList = (List<Parameter>) ctx.getResponseResult();
			for(int i = 0; i < problemSolutionList.size(); i++) {
				if(problemSolutionList.get(i).getValue().equals("PROBLEM_SOLUTION_MER-PROVIDE_NEW_EDC_CMS")){
					problemSolutionList.remove(problemSolutionList.get(i));
				}
				if(problemSolutionList.get(i).getValue().equals("PROBLEM_SOLUTION_EDC-CHANGE_EDC_CMS")){
					problemSolutionList.remove(problemSolutionList.get(i));
				}
				if(problemSolutionList.get(i).getValue().equals("PROBLEM_SOLUTION_OTHER-CMS006")){
					problemSolutionList.remove(problemSolutionList.get(i));
				}
			}
			SessionHelper.setAttribute(request, ucNo, IAtomsConstants.PROBLEM_SOLUTION_TSB_LIST_STR, problemSolutionList);
			
			// 責任歸屬
			inquiryContext = new BaseParameterInquiryContext();
			inquiryContext.setParameterType(IATOMS_PARAM_TYPE.RESPON_ATTRIBUTION.getCode());
			ctx= this.serviceLocator.doService(logonUser, IAtomsConstants.SERVICE_BASE_PARAMETER_SERVICE, IAtomsConstants.ACTION_GET_PARAMETER_ITEMS, inquiryContext);
			List<Parameter> responsibityList = (List<Parameter>) ctx.getResponseResult();
			LOGGER.debug("RESPON_ATTRIBUTION ==> "+responsibityList.size()+" : "+responsibityList.toString());
			SessionHelper.setAttribute(request, ucNo, IAtomsConstants.RESPONSIBITY_LIST_STR, responsibityList);
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Purpose: 設置角色狀態標記
	 * @author amandawang
	 * @param formDTO：CaseManagerFormDTO
	 * @return void
	 */
	private void setUserRoleFlag(CaseManagerFormDTO formDTO, IAtomsLogonUser iAtomsLogonUser) {
		if (iAtomsLogonUser != null) {
			List<AdmRoleDTO> admRoleDTOs = iAtomsLogonUser.getUserFunctions();
			if (!CollectionUtils.isEmpty(admRoleDTOs)) {
				for (AdmRoleDTO admRoleDTO : admRoleDTOs) {	
					if (IAtomsConstants.VECTOR_ROLE_ATTRIBUTE.equals(admRoleDTO.getAttribute()) &&
							IAtomsConstants.WORK_FLOW_ROLE_CYBER_AGENT.equals(admRoleDTO.getWorkFlowRole()) &&
							IAtomsConstants.CASE_ROLE.CUSTOMER_SERVICE.getCode().equals(admRoleDTO.getRoleCode())) {
						//廠商角色 + cyberAgent + 角色代碼為CUSTOMER_SERVICE == 客服
						formDTO.setIsCustomerService(true);
					} else if (IAtomsConstants.VECTOR_ROLE_ATTRIBUTE.equals(admRoleDTO.getAttribute()) &&
							IAtomsConstants.WORK_FLOW_ROLE_CYBER_AGENT.equals(admRoleDTO.getWorkFlowRole()) &&
							IAtomsConstants.CASE_ROLE.VENDOR_SERVICE.getCode().equals(admRoleDTO.getRoleCode())) {
						// CR #2951 廠商客服
						formDTO.setIsVendorService(true);
					} else if (IAtomsConstants.CUSTOMER_ROLE_ATTRIBUTE.equals(admRoleDTO.getAttribute()) &&
							IAtomsConstants.WORK_FLOW_ROLE_BANK_AGENT.equals(admRoleDTO.getWorkFlowRole())) {
						//客服角色 + bankAgent == 客戶窗口
						formDTO.setIsCustomer(true);
					} else if (IAtomsConstants.VECTOR_ROLE_ATTRIBUTE.equals(admRoleDTO.getAttribute()) &&
							IAtomsConstants.WORK_FLOW_ROLE_CYBER_AGENT.equals(admRoleDTO.getWorkFlowRole()) &&
							IAtomsConstants.ROLE_NAME_QA.equals(admRoleDTO.getRoleCode())) {
						//廠商角色 + CyberAgent + 角色代碼為QA == QA窗口
						formDTO.setIsQA(true);
					} else if (IAtomsConstants.VECTOR_ROLE_ATTRIBUTE.equals(admRoleDTO.getAttribute()) &&
							IAtomsConstants.WORK_FLOW_ROLE_CYBER_AGENT.equals(admRoleDTO.getWorkFlowRole()) &&
							IAtomsConstants.ROLE_NAME_TMS.equals(admRoleDTO.getRoleCode())) {
						//廠商角色 + CyberAgent + 角色代碼為TMS == TMS窗口
						formDTO.setIsTMS(true);
						//Task #3583 角色屬性-新增 客戶廠商
					} else if ((IAtomsConstants.VECTOR_ROLE_ATTRIBUTE.equals(admRoleDTO.getAttribute()) || IAtomsConstants.CUSTOMER_VECTOR_ROLE_ATTRIBUTE.equals(admRoleDTO.getAttribute())) &&
							(IAtomsConstants.WORK_FLOW_ROLE_DEPT_AGENT.equals(admRoleDTO.getWorkFlowRole()) || IAtomsConstants.WORK_FLOW_ROLE_CUS_DEPT_AGENT.equals(admRoleDTO.getWorkFlowRole()))) {
						//廠商角色 + 部門Agent == 部門AGENT窗口
						formDTO.setIsAgent(true);
						//Task #3583 角色屬性-新增 客戶廠商
					} else if ((IAtomsConstants.VECTOR_ROLE_ATTRIBUTE.equals(admRoleDTO.getAttribute()) || IAtomsConstants.CUSTOMER_VECTOR_ROLE_ATTRIBUTE.equals(admRoleDTO.getAttribute())) &&
							(IAtomsConstants.WORK_FLOW_ROLE_VENDOR_AGENT.equals(admRoleDTO.getWorkFlowRole()) || IAtomsConstants.WORK_FLOW_ROLE_VENDOR_CUS_AGENT.equals(admRoleDTO.getWorkFlowRole()))) {
						//廠商角色 + 廠商Agent == 廠商AGENT窗口
						formDTO.setIsVendorAgent(true);
						//Task #3583 角色屬性-新增 客戶廠商
					} else if ((IAtomsConstants.VECTOR_ROLE_ATTRIBUTE.equals(admRoleDTO.getAttribute()) || IAtomsConstants.CUSTOMER_VECTOR_ROLE_ATTRIBUTE.equals(admRoleDTO.getAttribute())) &&
							(IAtomsConstants.WORK_FLOW_ROLE_ENGINEER.equals(admRoleDTO.getWorkFlowRole()) || IAtomsConstants.WORK_FLOW_ROLE_CUS_ENGINEER.equals(admRoleDTO.getWorkFlowRole()))) {
						//廠商角色 + 工程師 == 工程師窗口
						formDTO.setIsEngineer(true);
					} else if (IAtomsConstants.VECTOR_ROLE_ATTRIBUTE.equals(admRoleDTO.getAttribute()) &&
							IAtomsConstants.WORK_FLOW_ROLE_CYBER_AGENT.equals(admRoleDTO.getWorkFlowRole()) &&
							IAtomsConstants.WORK_FLOW_ROLE_CYBER_AGENT.equals(admRoleDTO.getRoleCode())) {
						//廠商角色 + CyberAgent + 角色代碼為CYBER_AGENT == CYBER_AGENT
						formDTO.setIsCyberAgent(true);
					} else if (IAtomsConstants.CUSTOMER_VECTOR_ROLE_ATTRIBUTE.equals(admRoleDTO.getAttribute()) &&
							IAtomsConstants.WORK_FLOW_ROLE_CUS_VENDOR_AGENT.equals(admRoleDTO.getWorkFlowRole()) &&
							IAtomsConstants.CASE_ROLE.CUS_VENDOR_SERVICE.getCode().equals(admRoleDTO.getRoleCode())) {
						//Task #3578 客戶廠商客服
						//客戶廠商角色 + 客戶廠商Agent + 角色代碼為CUS_VENDOR_SERVICE == CUS_VENDOR_SERVICE
						formDTO.setIsCusVendorService(true);
					} 
				}
			}
		}
	}
}