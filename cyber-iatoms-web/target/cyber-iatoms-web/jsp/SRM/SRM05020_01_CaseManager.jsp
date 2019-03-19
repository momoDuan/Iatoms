<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser"%>
<%@page import="cafe.core.config.GenericConfigManager"%>

<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	CaseManagerFormDTO formDTO = null;	
	String ucNo = null;
	List<String> rights = null;
	String accessRights = null;
	//是否是客服
	boolean isCustomerService = false;
	// CR #2951 廠商客服
	boolean isVendorService = false;
	//	Task #3578
	boolean isCusVendorService = false;
	String actionId = null;
	if (ctx != null) {
		// 得到FormDTO
		formDTO = (CaseManagerFormDTO) ctx.getRequestParameter();
		if (formDTO != null) {
			// 获得UseCaseNo
			ucNo = formDTO.getUseCaseNo();
			isCustomerService = formDTO.getIsCustomerService();
			isVendorService = formDTO.getIsVendorService();
			isCusVendorService = formDTO.getIsCusVendorService();
			actionId = formDTO.getActionId();
			accessRights = logonUser.getAccRghts().get(ucNo);
			rights = StringUtils.toList(accessRights, ",");
		} else {
			ucNo = IAtomsConstants.UC_NO_SRM_05020;
			formDTO = new CaseManagerFormDTO();
			rights = new ArrayList<String>();
		}
	} else {
		ucNo = IAtomsConstants.UC_NO_SRM_05020;
		formDTO = new CaseManagerFormDTO();
		rights = new ArrayList<String>();
	}
	
	//案件類別
	List<Parameter> caseCategoryList = (List<Parameter>) SessionHelper.getAttribute(request,
			IAtomsConstants.UC_NO_SRM_05020, IATOMS_PARAM_TYPE.TICKET_TYPE.getCode());	
	
	//客戶下拉菜單
	List<Parameter> customerList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_CUSTOMER_LIST);
	
	// EDC機型列表
	List<Parameter> edcTypeList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_EDC_TYPE_LIST);
	
	// 維護廠商列表
	List<Parameter> vendorList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_VENDOR_LIST);
	
	// 維護部門列表
	List<Parameter> deptList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, CaseManagerFormDTO.PARAM_DEPT_LIST);
	
	// 是否專案
	List<Parameter> yesOrNoList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.YES_OR_NO.getCode());
	
	// 設備支援功能列表下拉框
	List<Parameter> supportedFunList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.SUPPORTED_FUNCTION.getCode());
	
	// 設備開啟模式列表
	List<Parameter> openModeList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.ASSET_OPEN_MODE.getCode());
	//派工單位
	String departmentList = (String) SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_DEPARTMENT_LIST);
	//派工單位(經貿聯網)
	String departmentCybList = (String) SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_DEPARTMENT_CYB_LIST);
	// 限制條件
	List<Parameter> limitConditionList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.LIMIT_CONDITION.getCode());
	//通知類別下拉列表
	List<Parameter> noticeTypeList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.NOTICE_TYPE.getCode());
	//mail通知種類集合
	//List<Parameter> mailgroupList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.MAIL_GROUP.getCode());
	//正常帳號的mail
	//List<Parameter> empMailList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_EMP_MAIL_LIST);
	
	//案件關聯設備動作集合
	List<Parameter> actionsList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.ACTIONS.getCode());
	//工單範本集合
	List<Parameter> templatesList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_TEMPLATES_LIST);
	//維護部門
	List<Parameter> casePartGroupList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_CASE_PART_GROUP_LIST);
	//維護部門
	List<Parameter> locations = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.LOCATION.getCode());
	Parameter cardReader = new Parameter();
	//往工單列印的下拉框裡面添加刷卡機參數表的值
	cardReader.setValue(IAtomsConstants.REPORT_NAME_EDC_CONTRACT_EXPIRE_INFO_REPORT);
	cardReader.setName(IAtomsConstants.CARD_READER_PARAMETER);
	if(IAtomsConstants.ACTION_INIT.equals(actionId)) {
		templatesList.add(cardReader);
	}
	Gson gsonss = new GsonBuilder().create();
	//String mailgroupListString = gsonss.toJson(mailgroupList);
	String actionsListString = gsonss.toJson(actionsList);
	//List<String> nameList = new ArrayList<String>();
	/* for(int i = 0; i<mailgroupList.size(); i++){
		nameList.add(mailgroupList.get(i).getName());
	} */
	
	// 案件狀態列表
	List<Parameter> caseStatusList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.CASE_STATUS.getCode());
	// 案件動作列表
	List<Parameter> caseActionList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.CASE_ACTION.getCode());
	// 物流廠商列表
	String logisticsVendors = (String)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.LOGISTICS_VENDOR_LIST_STR);
	
	// 查核結果
	String checkResultsListStr = (String)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.CHECK_RESULTS_LIST_STR);
	// 問題原因列表
	String problemReasonListStr = (String)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PROBLEM_REASON_LIST_STR);
	// 問題原因(台新)列表
	String problemReasonTsbListStr = (String)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PROBLEM_REASON_TSB_LIST_STR);
	// 問題解決方式
	String problemSolutionListStr = (String)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PROBLEM_SOLUTION_LIST_STR);
	// 問題解決方式(台新)
	String problemSolutionTsbListStr = (String)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PROBLEM_SOLUTION_TSB_LIST_STR);
	// 責任歸屬
	String responsibityListStr = (String)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.RESPONSIBITY_LIST_STR);
	// 案件類型列表
	String ticketModeList = (String)SessionHelper.getAttribute(request, ucNo, CaseManagerFormDTO.PARAM_TICKET_MODE);
	// 案件類型列表
	String casePartGroupListStr = (String)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_CASE_PART_GROUP_LIST_STR);
	// 案件類別集合
//	List<String> caseCategoryListStr = (List<String>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_CASE_CATEGORY_LIST_STR);
	// 案件狀態集合
	List<String> caseStatusListStr = (List<String>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_CASE_STATUS_LIST_STR);
	//获取上传最大限制
	String uploadFileSize = formDTO.getUploadFileSize();
	
	// 獲得用戶當前模板
	Parameter currentColumnTemplate = (Parameter)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_CURRENT_COLUMN_TEMPLATE);
	
	// 案件匯入類別
	List<Parameter> importTicketTypes = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_IMPORT_TICKET_TYPES);
	// 案件進度
	List<Parameter> progressList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, "CASE_PROGRESS");
	
%>
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$CASE_CATEGORY" var="caseCategoryAttr" />
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants" var="iatomsContants" />
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$CASE_ACTION" var="caseActionAttr" />
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$CASE_STATUS" var="caseStatusAttr" />
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$CASE_ROLE" var="caseRoleAttr" />
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$FUNCTION_TYPE" var="functionType" />

<html>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>

<c:set var="caseCategoryList" value="<%=caseCategoryList%>" scope="page"></c:set>
<c:set var="edcTypeList" value="<%=edcTypeList%>" scope="page"></c:set>
<c:set var="customerList" value="<%=customerList%>" scope="page"></c:set>
<c:set var="vendorList" value="<%=vendorList%>" scope="page"></c:set>
<c:set var="yesOrNoList" value="<%=yesOrNoList%>" scope="page"></c:set>
<c:set var="supportedFunList" value="<%=supportedFunList%>" scope="page"></c:set>
<c:set var="openModeList" value="<%=openModeList%>" scope="page"></c:set>
<c:set var="noticeTypeList" value="<%=noticeTypeList%>" scope="page"></c:set>
<c:set var="deptList" value="<%=deptList%>" scope="page"></c:set>
<c:set var="caseActionList" value="<%=caseActionList%>" scope="page"></c:set>
<c:set var="caseStatusList" value="<%=caseStatusList%>" scope="page"></c:set>
<c:set var="limitConditionList" value="<%=limitConditionList%>" scope="page"></c:set>
<c:set var="checkResultsList" value="<%=checkResultsListStr%>" scope="page"></c:set>
<c:set var="problemReasonList" value="<%=problemReasonListStr%>" scope="page"></c:set>
<c:set var="problemSolutionList" value="<%=problemSolutionListStr%>" scope="page"></c:set>
<c:set var="responsibityList" value="<%=responsibityListStr%>" scope="page"></c:set>
<c:set var="actionsListString" value="<%=actionsListString%>" scope="page"></c:set>
<c:set var="actionsList" value="<%=actionsList%>" scope="page"></c:set>
<c:set var="ticketModeList" value="<%=ticketModeList%>" scope="page"></c:set>
<c:set var="templatesList" value="<%=templatesList%>" scope="page"></c:set>
<c:set var="isCustomerService" value="<%=isCustomerService%>" scope="page"></c:set>
<c:set var="isVendorService" value="<%=isVendorService%>" scope="page"></c:set>
<c:set var="isCusVendorService" value="<%=isCusVendorService%>" scope="page"></c:set>
<c:set var="casePartGroupList" value="<%=casePartGroupList%>" scope="page"></c:set>
<c:set var="casePartGroupListStr" value="<%=casePartGroupListStr%>" scope="page"></c:set>
<c:set var="rights" value="<%=rights%>" scope="page"></c:set>
<c:set var="caseStatusListStr" value="<%=caseStatusListStr%>" scope="page"></c:set>
<c:set var="uploadFileSize" value="<%=uploadFileSize%>" scope="page"></c:set>
<c:set var="currentColumnTemplate" value="<%=currentColumnTemplate%>" scope="page"></c:set>
<c:set var="importTicketTypes" value="<%=importTicketTypes%>" scope="page"></c:set>
<c:set var="locations" value="<%=locations%>" scope="page"></c:set>
<c:set var="progressList" value="<%=progressList%>" scope="page"></c:set>
<c:set var="stuff" value="case" scope="page"></c:set>
<c:set var="ucNo" value="<%=ucNo%>" scope="page"></c:set>
<c:set var="problemReasonTsbListStr" value="<%=problemReasonTsbListStr%>" scope="page"></c:set>
<c:set var="problemSolutionTsbListStr" value="<%=problemSolutionTsbListStr%>" scope="page"></c:set>
<c:set var="logisticsVendors" value="<%=logisticsVendors%>" scope="page"></c:set>
<!-- DataLoader -->
<!-- DataLoader -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>案件處理</title>
<%@include file="/jsp/common/easyui-common.jsp"%>
<script type="text/javascript" src="${contextPath}/assets/jquery-easyui-1.4.3/datagrid-detailview.js"></script>
<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
<script type="text/javascript" src="${contextPath}/js/caseManager-1.4.min.js"></script>
<link rel="stylesheet" type="text/css" href="${contextPath}/assets/css/jquery.multiselect2side.css">
<script type="text/javascript" src="${contextPath}/assets/js/jquery.multiselect2side.js"></script>
<link rel="stylesheet" href="${contextPath}/css/fineuploader-5.0.2.css"/>
<script type="text/javascript" src="${contextPath}/js/fineuploader-5.0.2.js"></script>

</head>
<body>
<div id="caseDivId" style="width: auto; overflow-x:hidden; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height topSoller">
<div id="p" region="center" class="easyui-panel" title="案件處理" style="width: 98%; height: auto; overflow-x:hidden;" >
        <div title="" style="padding: 10px">
        <div><span id="buildMsg" class="red"></span></div>
            <div class="ftitle" style="text-align: right;padding:0px 50px 5px 0px">
            	<c:set var="flag" value="0"></c:set>
            	<c:set var="isDispatch" value="${false }"/>
				<c:set var="isAutoDispatching" value="${false }"/>
            	<c:forEach var="item" items="${rights }" >
            		<c:if test="${functionType.INSTALL.code eq item 
            			or functionType.MERGE.code eq item
            			or functionType.UPDATE.code eq item
            			or functionType.UNINSTALL.code eq item
            			or functionType.CHECK.code eq item
            			or functionType.PROJECT.code eq item
            			or functionType.REPAIR.code eq item}">
            			<c:set var="flag" value="1"></c:set>
            		</c:if>
            		<c:if test="${functionType.DISPATCH.code eq item }">
						<c:set var="isDispatch" value="${true }"></c:set>
					</c:if>
					<c:if test="${functionType.AUTO_DISPATCH.code eq item }">
						<c:set var="isAutoDispatching" value="${true }"></c:set>
					</c:if>
            	</c:forEach>
            	<c:if test="${flag eq '1' }">建檔:
	               	<c:forEach var="caseCategory" items="${caseCategoryList}">
						<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], caseCategory.value )}">
		               		<c:choose>
		               			<c:when test="${caseCategoryAttr.REPAIR.code eq caseCategory.value}">
		               				<a href="javascript:void(0)" class="easyui-linkbutton red" onclick="createCase('${caseCategory.value}')">${caseCategory.name}</a>
		               			</c:when>
		               			<c:otherwise>
		               				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="createCase('${caseCategory.value}')">${caseCategory.name}</a>
		               			</c:otherwise>
		               		</c:choose>
						</c:if>
					</c:forEach>
				</c:if>               
            </div>
            <form id="queryForm" method="post" novalidate>
                <table cellpadding="4">
                    <tr>
                        <td>客戶:</td>
                        <td>
			         		<cafe:droplisttag
								id="queryCompanyId"
								name="queryCompanyId" 
								css="easyui-combobox"
								hasBlankValue="true"
								blankName="請選擇"
								result="${customerList }"
								selectedValue="${formDTO.isVendorAttribute == false && (formDTO.isCustomerAttribute == true || formDTO.isCustomerVendorAttribute == true)?empty customerList?'':customerList[0].value:''}"
								style="width:200px"
								javascript="editable=false 
									${formDTO.isVendorAttribute == false && (formDTO.isCustomerAttribute == true || formDTO.isCustomerVendorAttribute == true) == true?'disabled=true':'' }"
							></cafe:droplisttag>
                        </td>
                        <td>需求單號:</td>
                        <td>
                            <input id="queryRequirementNo" style="width:200px" name="queryRequirementNo" class="easyui-textbox" maxlength="30" data-options="validType:['maxLength[30]']" value=""></input></td>
                        <td>案件編號:</td>
                        <td>
                            <input id="queryCaseId" style="width:200px" name="queryCaseId" class="easyui-textbox" maxlength="15" data-options="validType:['maxLength[15]']" value=""></input>
                        </td>
                    </tr>
                    <tr>
                        <td>案件類別:</td>
                        <td>
							<cafe:droplistchecktag
								id="queryCaseCategory"
								name="queryCaseCategory" 
								css="easyui-combobox easyui-mutil-select"
								hasBlankValue="true"
								result="${caseCategoryList}"
								blankName="請選擇(複選)"
								style="width:200px"
								javascript="multiple=true editable=false panelheight=\"auto\""
							></cafe:droplistchecktag>
                        </td>
                        <td>刷卡機型:</td>
                        <td>
			         		<cafe:droplisttag 
				               id="queryEdcType"
				               name="queryEdcType" 
				               css="easyui-combobox easyui-mutil-select"
				               result="${edcTypeList}"
				               hasBlankValue="true"
				               blankName="請選擇(複選)"
				               style="width:200px"
				               javascript="multiple=true editable=false">
			         		</cafe:droplisttag>
                        </td>
                        <td>派工廠商: </td>
						<td>
							<cafe:droplisttag
								css="easyui-combobox easyui-mutil-select"
								id="queryVendorId"
								name="queryVendorId"
								result="${vendorList }"
								style="width:200px"
								blankName="請選擇(複選)" 
								hasBlankValue="true"
								javascript="multiple=true editable=false"
							></cafe:droplisttag>
						</td>
					</tr>	
					<tr>	
						<td>派工部門: </td>
						<td>
							<cafe:droplisttag
								css="easyui-combobox easyui-mutil-select"
								id="queryVendorDept"
								name="queryVendorDept"
								result="${casePartGroupList }"
								style="width:200px"
								blankName="請選擇(複選)" 
								hasBlankValue="true"
								javascript="multiple=true editable=false data-options=\"valueField:'value',textField:'name'\" "
							></cafe:droplisttag>
						</td>
						<td>進件日期:</td>
						<td>
                            <input id="queryCreateDateStartStr" name="queryCreateDateStartStr" class="easyui-datebox" value="" maxlength="10" style="width: 110px" missingMessage="請輸入進件日期起" data-options="validType:['date[\'進件日期格式限YYYY/MM/DD\']'],
                            	onChange:function(newValue,oldValue) {
                            			$('#queryCreateDateEndStr').timespinner('isValid');
                             	}">～<input id="queryCreateDateEndStr" name="queryCreateDateEndStr" class="easyui-datebox" maxlength="10" value="" style="width: 110px" missingMessage="請輸入進件日期迄" data-options="validType:['date[\'進件日期格式限YYYY/MM/DD\']','compareDateStartEnd[\'#queryCreateDateStartStr\',\'進件日期起不可大於進件日期迄\']']"> 
                        </td>
                        <td>應完成日期:</td>
                        <td>
                           <input id="queryAcceptableCompleteDateStartStr" name="queryAcceptableCompleteDateStartStr" class="easyui-datebox" maxlength="10" style="width: 110px" data-options="
                           validType:['date[\'應完成日期格式限YYYY/MM/DD\']'],
                            	onChange:function(newValue,oldValue) {
                            			$('#queryAcceptableCompleteDateEndStr').timespinner('isValid');
                             	}">～<input id="queryAcceptableCompleteDateEndStr" name="queryAcceptableCompleteDateEndStr" class="easyui-datebox" maxlength="10" style="width: 110px" data-options="validType:['date[\'應完成日期格式限YYYY/MM/DD\']','compareDateStartEnd[\'#queryAcceptableCompleteDateStartStr\',\'應完成日期起不可大於應完成日期迄\']']"> 
                        </td>
					</tr>
					<tr>
                        <td>完修日期:</td>
                        <td>
                            <input id="queryCompleteDateStartStr" name="queryCompleteDateStartStr" class="easyui-datebox" maxlength="10" style="width: 110px" data-options="
                            validType:['date[\'完修日期格式限YYYY/MM/DD\']'],
                            	onChange:function(newValue,oldValue) {
                            			$('#queryCompleteDateEndStr').timespinner('isValid');
                             	}">～<input id="queryCompleteDateEndStr" name="queryCompleteDateEndStr" class="easyui-datebox" maxlength="10" style="width: 110px" data-options="validType:['date[\'完修日期格式限YYYY/MM/DD\']','compareDateStartEnd[\'#queryCompleteDateStartStr\',\'完修日期起不可大於完修日期迄\']']"> 
                        </td>
                        <td>案件狀態:</td>
                        <td>
							<cafe:droplistchecktag
								css="easyui-combobox easyui-mutil-select"
								id="queryCaseStatus"
								name="queryCaseStatus"
								result="${caseStatusList }"
								selectedValues="${caseStatusListStr}"
								style="width:200px"
								blankName="請選擇(複選)" 
								hasBlankValue="true"
								javascript="multiple=true editable=false"
							></cafe:droplistchecktag>
                        </td>
                        
                        <td>案件進度:</td>
                        <td>
							<cafe:checklistTag 
								name="queryCaseProgress" 
								id="queryCaseProgress" 
								type="checkbox"
								result="${progressList}"
								javascript=""
							>
							</cafe:checklistTag>
                        </td>
                        
                    </tr>
                    <tr>
                    	<td>特店代號:</td>
                        <td>
                            <input id="queryMerchatCode" style="width:200px" name="queryMerchatCode" class="easyui-textbox" maxlength="20" data-options="validType:['maxLength[20]']" value=""></input>
                        </td>
                        <td>特店名稱:</td>
                        <td>
                            <input id="queryMerchatName" style="width:200px" name="queryMerchatName" class="easyui-textbox" maxlength="50" data-options="validType:['maxLength[50]']" value=""></input></td>
                        <td>表頭（同對外名稱）:</td>
                        <td>
                            <input id="queryMerHeader" style="width:200px" name="queryMerHeader" class="easyui-textbox" maxlength="50" data-options="validType:['maxLength[50]']" value=""></input></td>
                    </tr>
                    <tr>
                         <td>專案代碼:</td>
                        <td>
                            <input name="queryProjectCode" style="width:200px" id="queryProjectCode" class="easyui-textbox" value="" maxlength="50" data-options="validType:['maxLength[50]']"></input></td>
                        <td>專案名稱:</td>
                        <td>
                            <input name="queryProjectName" style="width:200px" id="queryProjectName" class="easyui-textbox" value="" maxlength="100" data-options="validType:['maxLength[100]']"></input></td>
                         <td>專案:</td>
                        <td>
							<cafe:checklistTag 
								name="queryIsProject" 
								id="queryIsProject" 
								type="checkbox"
								result="${yesOrNoList}"
								javascript=""
							>
							</cafe:checklistTag>
                        </td>
                         
                    </tr>
                    <tr>
                       <td>DTID:</td>
                        <td>
                            <input name="queryDtid" style="width:200px" id="queryDtid" class="easyui-textbox" maxlength="8" data-options="validType:['maxLength[8]']" value=""></input></td>
                        <td>TID:</td>
                        <td>
                            <input name="queryTid" style="width:200px" id="queryTid" class="easyui-textbox" maxlength="8" data-options="validType:['maxLength[8]']" value=""></input></td>
                            <td>設備開啟模式:</td>
                        <td>
                            <cafe:checklistTag 
								name="queryOpenMode" 
								id="queryOpenMode" 
								type="checkbox"
								result="${openModeList}"
								javascript=""
							>
						</cafe:checklistTag>
                        </td>
                        
                    </tr>
                    <tr>
                        <td>報修次數:</td>
                        <td>
                            <cafe:droplisttag
								css="easyui-combobox"
								id="queryConditionOperator"
								name="queryConditionOperator"
								result="${limitConditionList}"
								style="width:120px"
								blankName="請選擇" 
								hasBlankValue="true"
								javascript="editable=false panelheight=\"auto\" data-options=\"valueField:'value',textField:'name',width:150\" "
							></cafe:droplisttag>
                            <input name="queryRepairTimes" style="width:75px" id="queryRepairTimes" class="easyui-textbox" maxlength="2" data-options="validType:['maxLength[2]','positiveInt[\'報修次數限正整數，請重新輸入\']']" value="" style="width: 50px"  missingMessage="請輸入報修次數" ></input></td>
                        <td>SLA警示件查詢:</td>
                        <td>
                            <input id="queryWarningSla" name="queryWarningSla" type="checkbox">SLA 警示件查詢</input>
                        </td>
                        <td>刷卡機暨週邊設備支援功能:</td>
                        <td>
                            <cafe:droplisttag
								css="easyui-combobox easyui-mutil-select"
								id="querySupportedFun"
								name="querySupportedFun"
								result="${supportedFunList}"
								style="width:200px"
								blankName="請選擇(複選)" 
								hasBlankValue="true"
								javascript="editable=false multiple=true panelheight=\"auto\""
							></cafe:droplisttag>
                        </td>
                        
                    </tr>
                    <tr>
                    	<td>AO人員:</td>
                        <td>
                            <input id="queryAoName" style="width:200px" name="queryAoName" class="easyui-textbox" maxlength="50" data-options="validType:['maxLength[50]']" value=""></input></td>
                        
                        <td>線上排除:</td>
                        <td>
                            <input id="queryOnlineExclusion" name="queryOnlineExclusion" type="checkbox">線上排除</input>
                        </td>
                        <td>案件區域:</td>
                        <td>
                            <cafe:droplisttag
								css="easyui-combobox easyui-mutil-select"
								id="queryLocation"
								name="queryLocation"
								result="${locations}"
								style="width:200px"
								blankName="請選擇(複選)" 
								hasBlankValue="true"
								javascript="multiple=true editable=false data-options=\"valueField:'value',textField:'name'\" "
							></cafe:droplisttag>
                        </td>
                    </tr>
                    <tr>
                    <td>資料模式:</td>
                        <td>
                            <input type="radio" name="queryDateMode" id="isInstant" value="2" checked="checked">即時(${formDTO.transferMonth }個月內)</input>
                            <input type="radio" name="queryDateMode" id="isHistory" value="1">歷史</input>
                        </td>
                        <td>退回紀錄:</td>
                        <td>
                            <input id="queryForBack" name="queryForBack" type="checkbox">退回紀錄</input>
                        </td>
                        <td>延期紀錄:</td>
                        <td>
                            <input id="queryDelayRecord" name="queryDelayRecord" type="checkbox">延期紀錄</input>
                        </td>
                     </tr>
                     <tr>
	                      <td>CyberEDC:</td>
						  <td>
                            	<input type="radio" name="queryMicro" id="isMicro" value="2">是</input>
                            	<input type="radio" name="queryMicro" id="notMicro" value="1" checked="checked">否</input>
                          </td>
                          <td>CyberEDC到場:</td>
						  <td>
                            	<input type="radio" name="queryMicroArrive" id="isMicroArrive" value="2" disabled>是</input>
                            	<input type="radio" name="queryMicroArrive" id="notMicroArrive" value="1" disabled>否</input>
                          </td>
                     </tr>
                    <tr>
                        <td colspan="6"></td>
                        <td>
                            <a href="javascript:void(0)" id="button_query" class="easyui-linkbutton c6" iconcls="icon-search" onclick="query(1, true)" style="width: 90px">查詢</a>
                        </td>
                    </tr>
                     <input type="hidden" id="actionId" name="actionId" />
					<input type="hidden" id="serviceId" name="serviceId" />
					<input type="hidden" id="useCaseNo" name="useCaseNo" />
					<input type="hidden" id="exportField" name="exportField"/>
					<input type="hidden" id="exportQueryCompanyId" name="exportQueryCompanyId"/>
					 <input type="hidden" id="hideCyberEdc" />
                </table>
            </form>
            <div><span id="dgResponse-msg" class="red">${msg }</span></div>
            <div class="ftitle">
<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.CONFIRMAUTHORIZES.code )}"><a href="javascript:void(0)" id="button_confirmAuthorizes" class="easyui-linkbutton c6" onclick="confirmAuthorizes(false, '${contextPath}');" style="width: auto">租賃授權確認</a></c:if>
<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.CANCELCONFIRMAUTHORIZES.code )}"><a href="javascript:void(0)" id="button_cancelConfirmAuthorizes" class="easyui-linkbutton c6" onclick="cancelConfirmAuthorizes(false, '${contextPath}');" style="width: auto">租賃授權取消</a></c:if>
<%-- <a href="javascript:void(0)" id="button_confirmAuthorizes" class="easyui-linkbutton c6" onclick="confirmAuthorizes(false, '${contextPath}');" style="width: auto; display: none;" >授權確認</a> --%>
<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.CASE_IMPORT.code )}"><a href="javascript:void(0)" id="button_caseimport" class="easyui-linkbutton c6" onclick="showUploadView();"
					style="width: auto">案件匯入</a></c:if>
<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.COORDINATEDIMPORT.code )}">
	<a href="javascript:void(0)"  class="easyui-linkbutton c6" id="importCaseEnd" onclick="showCaseEndImportView();" style="width: auto">協調完成匯入</a>
</c:if>                
<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.ADD_RECORD.code )}"><a href="javascript:void(0)" id="button_addrecord" class="easyui-linkbutton c6" onclick="showActionView('addRecord')" style="width: auto">新增記錄</a></c:if>
<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.SHOW_DETAIL.code )}"><a href="javascript:void(0)" id="button_showdetail" class="easyui-linkbutton c6" style="width: auto" onclick="showSubView()">顯示記錄</a></c:if>
<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.DISPATCH.code ) and isDispatch}"><a href="javascript:void(0)" id="button_dispatch" class="easyui-linkbutton c6" style="width: auto" onclick="showActionView('dispatching')">派工</a></c:if>
<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.AUTO_DISPATCH.code ) and isAutoDispatching}"><a href="javascript:void(0)" id="button_autodispatch" class="easyui-linkbutton c6" style="width: auto" onclick="showActionView('autoDispatching')">自動派工</a></c:if>
<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.RESPONSED.code )}"><a href="javascript:void(0)" id="button_responsed" class="easyui-linkbutton c6" onclick="showActionView('response');" style="width: auto">回應</a></c:if>
<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.ARRIVE.code )}"><a href="javascript:void(0)" id="button_arrive" class="easyui-linkbutton c6" onclick="showActionView('arrive');" style="width: auto">到場</a></c:if>
<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.COMPLETE.code )}"><a href="javascript:void(0)" id="button_completed" class="easyui-linkbutton c6" onclick="showActionView('complete');" style="width: auto">完修</a></c:if>
<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.SIGN.code )}"><a href="javascript:void(0)" id="button_sign" class="easyui-linkbutton c6" onclick="showActionView('sign');" style="width: auto">簽收</a></c:if>
<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.ONLINE_EXCLUSION.code )}"><a href="javascript:void(0)" id="button_onlineexclusion" class="easyui-linkbutton c6" onclick="showActionView('onlineExclusion');" style="width: auto">線上排除</a></c:if>
<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.BACK.code )}"><a href="javascript:void(0)" id="button_back" class="easyui-linkbutton c6" onclick="showActionView('retreat');" style="width: auto">退回</a></c:if>
<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.DELAY.code )}"><a href="javascript:void(0)" id="button_delay" class="easyui-linkbutton c6" onclick="showActionView('delay');" style="width: auto">延期</a></c:if>
<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.RUSH_REPAIR.code )}"><a href="javascript:void(0)" id="button_rushrepair" class="easyui-linkbutton c6" onclick="showActionView('rushRepair');" style="width: auto">催修</a></c:if>
                
<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.CHANGE_CASE_TYPE.code )}"><a href="javascript:void(0)" id="button_changecasetype" class="easyui-linkbutton c6" style="width: auto" onclick="showActionView('changeCaseType');">修改案件類型</a></c:if>
<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.CLOSED.code )}"><a href="javascript:void(0)" id="button_closed" class="easyui-linkbutton c6" onclick="showActionView('closed');" style="width: auto">結案</a></c:if>
<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.VOID_CASE.code )}"><a href="javascript:void(0)" id="button_voidcase" class="easyui-linkbutton c6" onclick="showVoidView('dgResponse');" style="width: auto">作廢</a></c:if>
<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.IMMEDIATELY_CLOSING.code )}"><a href="javascript:void(0)" id="button_immediatelyclosing" class="easyui-linkbutton c6" onclick="showActionView('immediatelyClosing');" style="width: auto">協調完成</a></c:if>
<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.CHANGE_COMPLETE_DATE.code )}"><a href="javascript:void(0)" id="button_changecompletedate" class="easyui-linkbutton c6" onclick="showActionView('changeCompleteDate');" style="width: auto">修改實際完修時間</a></c:if>
<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.CHANGE_CREATE_DATE.code )}"><a href="javascript:void(0)" id="button_changecreatedate" class="easyui-linkbutton c6" onclick="showActionView('changeCreateDate');" style="width: auto">修改進件時間</a></c:if>
<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.PAYMENT.code )}"><a href="javascript:void(0)" id="button_payment" class="easyui-linkbutton c6" onclick="showActionView('payment');" style="width: auto">到貨檢測</a></c:if>
<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.LEASE_PRELOAD.code )}"><a href="javascript:void(0)" id="button_leasepreload" class="easyui-linkbutton c6" onclick="showActionView('leasePreload');" style="width: auto">租賃預載</a></c:if>
<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.DISTRIBUTION.code )}"><a href="javascript:void(0)" id="button_distribution" class="easyui-linkbutton c6" onclick="showActionView('distribution');" style="width: auto">租賃配送中</a></c:if>
<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.LEASE_SIGN.code )}"><a href="javascript:void(0)" id="button_leasesign" class="easyui-linkbutton c6" onclick="showActionView('onlineExclusion','Y');" style="width: auto">租賃簽收</a></c:if>
<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.PRINT.code )}"><a href="javascript:void(0)" id="button_print" class="easyui-menubutton" menu="#mm1" style="width: auto">列印工單</a></c:if>
            </div>
            <div id="mm1" style="width: 250px;">
            	<c:if test="${!empty templatesList}">
            		<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.PRINT.code )}">
	            		<c:forEach items="${templatesList}" var="templates">
	            			<div class="menu-sep"></div>
	            			<div onclick="exportData('${templates.name }','${templates.value }');">${templates.name }</div>
	            		</c:forEach>
            		</c:if>
            	</c:if>
            </div>
            <div closed="true" id="response" style="width: 98%; height: auto; display:none;">
                <table id="dgResponse" style="width: 100%; height: auto">
                </table>
            </div>
            <div closed="true" title="協調完成匯入"  id="uploadCaseEnd" style="display:none;">
				<table style="width: 100%; height: auto">
					<tr>
						<td width="10%" >匯入文檔路徑:</td>
						<td width="10%">	
							<span ><cafe:fileuploadTag 
								id="uploadCaseFiled"
								name="uploadCaseFiled"
								uploadUrl="${contextPath}/caseHandle.do" 
								uploadParams="{actionId:'uploadCoordinatedCompletion'}"
								allowedExtensions="'xls','xlsx'" 
								acceptFiles = ".xls,.xlsx" 
								sizeLimit = "${uploadFileSize }"
								showFileList="false"        
								
								multiple="false"
								showUnderline = "true"
								showName="上傳"
								messageId = "dgResponse-msg"
								isCustomError = "true"
								messageAlert="false"
								whetherDownLoad="false"
								whetherDelete="false"
								javaScript="onComplete:onCompleteByCaseEnd" >
							</cafe:fileuploadTag></span>
						</td>
						<td valign="middle">
							<a href="javascript:void(0)" id="" onclick="createSubmitForm('${contextPath}/caseHandle.do','actionId=downloadCoordinatedCompletion','post');" style="width: 10%;color: blue;" >匯入格式下載</a>&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
					</tr>
				</table>
			</div>
            <div closed="true" title="案件匯入"  id="uploadCaeInfo" style="display:none;">
			<form id="caseCategoryUpload" method="post">
				<table style="width: 100%; height: auto">
					<tr>
						<td width="70px">案件類別:</td>
						<td width="250px">
							<cafe:droplisttag
								id="caseCategory"
								name="caseCategory" 
								hasBlankValue="true"
								result="${importTicketTypes}"
								blankName="請選擇"
								style="width: 200px"
								javascript="editable=false panelheight=\"auto\" required=true validType='requiredDropList' invalidMessage=\"請輸入案件類別\""
							></cafe:droplisttag>
						</td>
						<td width="120px">匯入文檔路徑:</td>
						<td width="230px">
 							<cafe:fileuploadTag
									id="file_upload_import"
									name="file_upload_import"
									acceptFiles=".xls,.xlsx"
									allowedExtensions="'xls','xlsx'"
									showFileList="true"
									uploadUrl="${contextPath}/caseHandle.do?actionId=upload"
									sizeLimit = "${uploadFileSize }"
									showName="上傳"
									messageId = "dgResponse-msg"
									isCustomError = "true"
									autoUpload="false"
									messageAlert="false"
									whetherDownLoad="false"
									whetherDelete="true"
									javaScript="onComplete:onComplete"
									> 
							</cafe:fileuploadTag>
						</td>
						<td >
							<a href="javascript:void(0)" id="" onclick="templateDownLoad('${contextPath}');" style="width: 150px;color: blue;" >匯入格式下載</a>&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
					</tr>
					<tr>
						<td width="70px"></td>
						<td width="250px"></td>
						<td width="90px"></td>
						<td width="200px">
							<cafe:fileuploadTag
									id="file_upload_import_Additional"
									name="file_upload_import_Additional"
									allowedExtensions="all"
									showFileList="true"
									uploadUrl="${contextPath}/caseHandle.do?actionId=saveFile&fileName=${formDTO.fileName}"
									sizeLimit = "${uploadFileSize }"
									showName="附加檔案"
									messageId = "dgResponse-msg"
									isCustomError = "true"
									autoUpload="false"
									messageAlert="false"
									whetherDownLoad="false"
									whetherDelete="true"
									multiple="true"
									javaScript="onAllComplete:onAllComplete"
									> 
							</cafe:fileuploadTag>
						</td>
						<td></td>
					</tr>
					<tr>
						<td width="70px"></td>
						<td width="250px"></td>
						<td width="90px"></td>
						<td width="200px"></td>
						<td>
							<a href="javascript:void(0)" id="btnUpload" onClick="uploadCaseCategory();" iconcls="icon-save">匯入</a>&nbsp;
							<a href="javascript:void(0)" id="btnCancle" onclick="btnCancelUpload();" iconcls="icon-cancel">取消</a>&nbsp;
						</td>
					</tr>
				</table>
				<input type="hidden" value="${formDTO.fileName }" id="tempFileName"/>
				</form>
			</div>
            <div style="height: 10px"></div>
            <div style="width: 98%;text-align: right;">
                <a href="javascript:void(0)" style="width: 150px" id="filterBlank" onclick="openColumnBlock()">欄位</a>&nbsp;
                
                <a href="javascript:void(0)" style="width: 150px" id="btnExport" onclick="variableExport()">匯出</a>
            </div>
                <table id="dg" >
                </table>
             <div id="createDlg" class="topSoller"></div>
            <div id="editDlg" fit="true" class="topSoller"></div>
            <div id="showEditInfo" fit="true"></div>
            
            <div id="columnBlockDlg"></div>
            
				<div id="edcDialog"></div>
				 <input type="hidden" id="assetLinkParamerValue" name="assetLinkParamerValue" value=""/>
				 <input type="hidden" id="assetLinkParamerAssetLinkDatagridID" name="assetLinkParamerAssetLinkDatagridID" value=""/>
				 <input type="hidden" id="assetLinkParamerCaseId" name="assetLinkParamerCaseId" value=""/>
				 <input type="hidden" id="assetLinkParamerIndex" name="assetLinkParamerIndex" value=""/>
				<input type="hidden" id="assetLinkParamerCaseCategory" name="assetLinkParamerCaseCategory" value=""/>
				 <input type="hidden" id="saveEmailParamer" name="saveEmailParamer" value=""/>
				 <input type="hidden" id="tempToMail" name="tempToMail" value=""/>
				 <input type="hidden" id="nextActivitiToMail" name="nextActivitiToMail" value=""/>
				 <input type="hidden" id="deleteCaseSuppliesLinkIds" name="deleteCaseSuppliesLinkIds" value=""/>
                 <input type="hidden" id="deleteCaseAssetLinkIds" name="deleteCaseAssetLinkIds" value=""/>
               	 <input type="hidden" id="caseAssetLinkSerialNumbers" name="caseAssetLinkSerialNumbers" value=""/>
               	  <input type="hidden" id="selectSn" name="selectSn" value=""/>
               	  <input type="hidden" id="hideLeaseSignFlag" name="hideLeaseSignFlag" value=""/>
               <div id="dlgMail"  modal ="true" style="width: 700px; height: 400px; padding: 10px 20px; top: 10px;display:none">
					                <div><span id="dialogMsg" class="red"></span></div>
                	<form id="saveForm" class="easyui-form" method="post">
                	<input type="hidden" id="mailShowFlag"  value=""/>
                    	<table cellpadding="5">
	                    	<tr>
	                    		<td>
	                    			<label>通知類型:</label>
	                    		</td>
	                    		<td>
	                    			<div id="checkNoticeType" class="div-list" data-list-required='請輸入通知類型'style="display:inline">
				        	       <cafe:checklistTag 
				        	       		id="noticeType"
			                            name="noticeType" 
										result="${noticeTypeList}"
										javascript="onchange=controlNoticeTime()"
				        	       		type="checkbox"
				        	       		checkedValues='<%=StringUtils.toList("EMAIL", ",")%>'
				        	       		/>
				        	       	</div>
				        	       	<input  maxlength="10" id="remindStart" name="remindStart" style="width: 200px" type="text"/>
		        	         		<label>~</label>
		        	         		<input maxlength="10" id="remindEnd" name="remindEnd" style="width: 200px" type="text"/>
		        	         	</td>
	                    	</tr>
	                        <tr>
	                            <td>
	                            	<input id="jsonmail" name="jsonmail" value="" type="hidden"/>
	                                <label>Email 通知設定:</label>
	                            </td>
	                            <td><%-- css="easyui-combobox"
										result="${mailgroupList}" --%>
	                                <cafe:droplisttag 
										id="mailNotice" 
										name="mailNotice"
										
										blankName="請選擇" 
										hasBlankValue="true"
										style="width: 400px"
										selectedValue=""
										 >
									</cafe:droplisttag>
	                                <a href="javascript:void(0)" id="addEmail_linkbutton" >新增</a>
	                                <a href="javascript:void(0)" id="remoreEmail_linkbutton" >清除</a>
	                            </td>
	                        </tr>
	                        <tr>
	                            <td colspan="3">
	                            <input  
									id="toMail" 
									name="toMail" 
									style="height: 70px; width: 600px"
									type="text"
									value=""
									>
								</input>
	                            </td>
	                        </tr>
	                        <tr>
	                            <td>
	                                <label>
	                                    *收件者格式示例: 
	                                </label>
	                            </td>
	                        </tr>
	                        <tr>
	                            <td colspan="3">
	                                <label>TicketMailGroup(一般案件訊息通知群組); MerchantAO (特店 AO); atoms@mail.com:</label>
	                            </td>
	                        </tr>
	                    </table>
                </form>
                </div>
        </div>
    </div>
    </div>
    <script type="text/javascript">

    /*
	* 頁面加載完成函數
	*/
	$(function(){
		$("#deleteCaseSuppliesLinkIds").val("");
    	$("#deleteCaseAssetLinkIds").val("");
    	$("#selectSn").val("");
    	// Task #3086 開放案件處理及設備管理欄位模板先編輯再查詢
		// 禁用欄位按鈕與匯出按鈕
    	/* $('#filterBlank').attr("onclick","return false;");
    	$('#filterBlank').attr("style","color:gray"); */
    	$('#btnExport').attr("onclick","return false;");
    	$('#btnExport').attr("style","color:gray");
		/*
		* 維護廠商聯動維護部門
		*/
		$("#queryVendorId").combobox({
			onChange : function() {
				$("#queryVendorDept").combobox("setValue", '');
				var companyIds = $("#queryVendorId").combobox("getValues");
				javascript:dwr.engine.setAsync(false);
				if(companyIds != '' && companyIds != []){
					// 調後臺方法
					ajaxService.getDeptByCompanyId(companyIds.toString(), function(data){
						if (data != null) {
							$("#queryVendorDept").combobox("loadData",initSelectMultiple(data))
						}
					});	
				} else {
					$("#queryVendorDept").combobox("loadData",initSelectMultiple(${casePartGroupListStr}));
				}
				javascript:dwr.engine.setAsync(true);
			}
		});
		/*
		* 若選擇報修次數條件 聯動處理
		*/
		 $('#queryConditionOperator').combobox({
			onChange: function(){
				// 報修次數條件的值
				var conditionOperator = $('#queryConditionOperator').combobox('getValue');
				if(conditionOperator != ''){
					// 選擇是對時間框的影響
					$('#queryRepairTimes').textbox({
							required: true
					});
				} else {
					// 選擇否對時間框的影響
					$('#queryRepairTimes').textbox({
						required: false
					});
					$('#queryRepairTimes').textbox('setValue', '');
				}
				// 限制欄位長度
				textBoxDefaultSetting($('#queryRepairTimes'));
			}
		}); 
		 /*
		* 案件進度聯動處理
		*/
		$('[name=queryCaseProgress]').each(function(i,obj) {
			$(obj).bind('change', function(e){
				// 清空選中值
				$("#queryCaseStatus").combobox("setValue", "");
				// 選中案件狀態
				$("#queryCaseStatus").combobox("setValues", checkedProcessFun(true));
			});
		});
		/*
		* 案件狀態聯動處理
		*/
		$('#queryCaseStatus').combobox({
			onChange: function(newValue){
				if(newValue && newValue.toString() != ""){
					var checkedProgress = checkedProcessFun(false);
					if(checkedProgress && checkedProgress.length != 0){
						// 對數組進行排序比較
						if(newValue.sort().toString() != checkedProgress.sort().toString()){
							// 取消按鈕選中
							$('[name=queryCaseProgress]:checked').each(function(i,obj) {
								$(obj).attr("checked", false);
							});
						}
					}
				}
			}
		});
		 /*
		* datalist全選按鈕綁定事件
		*/
		 $("#exportCheckAll").click(function(){
			 // 調用datalist全選按鈕綁定事件 傳入datalistid與全選按鈕id
			 checkAllForDatalist('exportList', 'exportCheckAll');
		 });
		 // 查詢datagrid初始化
		 setTimeout("initQueryDatagrid();",5);
		// initQueryDatagrid();
		//Task #3500
		//2. 若不是CyberEDC，則不顯示 租賃開頭的按鈕
		cyberEdcBtnControl("none");
		//3. 若不是CyberEDC，到貨檢測 按鈕 顯示為 求償     
		if($("#button_payment").length > 0){
			$("#button_payment").find(".l-btn-text").html("求償");
			//arrivalInspection
			$("#button_payment").attr('onclick', '').unbind('click').click( function () { showActionView('arrivalInspection'); });
		}
		$('input[type=radio][name=queryMicro]').change(function() {
	        if (this.value == '2') {
	        	//Task #3549 若CyberEDC=是，開放 選擇 CyberEDC到場 是/否，預設否
				var notMicroArrive = $("#notMicroArrive");
				if(notMicroArrive.prop("disabled")){
					$("[name=queryMicroArrive]").prop("disabled",false);
					notMicroArrive.prop("checked", true);
				} 
	        } else if (this.value == '1') {
	        	$("[name=queryMicroArrive]").prop("disabled",true);
	        	$("[name=queryMicroArrive]").prop("checked", false);
	        }
	    });
		//重寫協調完成匯入 loading方法
		if(typeof uploadCaseFiled!="undefined"){
			uploadCaseFiled._options.callbacks.onStatusChange = function(id,oldStatus,newStatus) {
				if (newStatus == qq.status.SUBMITTED) {
					var blockStyle = {message:'loading...',css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
					$.blockUI(blockStyle); 
				} else if (newStatus == qq.status.UPLOAD_SUCCESSFUL) {
					$.unblockUI(); 
				} else if (newStatus == qq.status.UPLOAD_FAILED || newStatus == qq.status.CANCELED || newStatus == qq.status.REJECTED) {
					$.unblockUI();
				}
			};
		}
	});
    //Task #3500 調用方法 ：根據 CyberEDC 判斷 查詢畫面 按鈕顯示與隱藏 是CyberEDC: status='',不是: status='none'
    function cyberEdcBtnControl(status){
    	//租賃授權確認
		if($("#button_confirmAuthorizes").length > 0){
			$("#button_confirmAuthorizes").css("display",status);
		}
		//租賃預載
		if($("#button_leasepreload").length > 0){
			$("#button_leasepreload").css("display",status);
		}
		//租賃配送中
		if($("#button_distribution").length > 0){
			$("#button_distribution").css("display",status);
		}
		//租賃簽收
		if($("#button_leasesign").length > 0){
			$("#button_leasesign").css("display",status);
		}
		//租賃授權取消
		if($("#button_cancelConfirmAuthorizes").length > 0){
			$("#button_cancelConfirmAuthorizes").css("display",status);
		}
    	if(status == 'none'){
    		status = '';
    	}else{
    		status = 'none';
    	}
    	if($("#button_responsed").length > 0){
			$("#button_responsed").css("display",status);
		}
		if($("#button_arrive").length > 0){
			$("#button_arrive").css("display",status);
		}
		if($("#button_completed").length > 0){
			$("#button_completed").css("display",status);
		}
		if($("#button_sign").length > 0){
			$("#button_sign").css("display",status);
		}
		if($("#button_back").length > 0){
			$("#button_back").css("display",status);
		}
		if($("#button_delay").length > 0){
			$("#button_delay").css("display",status);
		}
		if($("#button_rushrepair").length > 0){
			$("#button_rushrepair").css("display",status);
		}
		if($("#button_changecasetype").length > 0){
			$("#button_changecasetype").css("display",status);
		}
		if($("#button_closed").length > 0){
			$("#button_closed").css("display",status);
		}
		if($("#button_changecompletedate").length > 0){
			$("#button_changecompletedate").css("display",status);
		}
		if($("#button_changecreatedate").length > 0){
			$("#button_changecreatedate").css("display",status);
		}
		//Task #3269
		if($("#importCaseEnd").length > 0){
			$("#importCaseEnd").css("display",status);
		}
    }
    /*
    *案件進度處理函數
    */
    function checkedProcessFun(flag){
    	var checkedProgress =[];
    	var caseStatusArr = [];
		// 得到案件進度選中值
		$('[name=queryCaseProgress]:checked').each(function() {
			checkedProgress.push($(this).val());
		});
		if(checkedProgress && checkedProgress.length != 0){
			for(var i = 0; i < checkedProgress.length; i++){
				// 進行中：待派工、已派工、已回應、延期中、已到場、完修
				if("PROGRESS" == checkedProgress[i]){
					caseStatusArr = caseStatusArr.concat(["WaitDispatch","Dispatched", "Responsed", "Delaying", "Arrived", "Completed"]);
				} else if ("WAIT_CLOSE" == checkedProgress[i]){
					// 待結案審查：待結案審查
					caseStatusArr.push("WaitClose");
				} else if ("CLOSED" == checkedProgress[i]){
					// 已結案：結案、協調完成
					caseStatusArr = caseStatusArr.concat(["Closed","ImmediateClose"]);
				}
			}
		} else {
			if(flag){
				// 未選擇 預設 待派工、已派工、已回應、延期中、已到場、完修
				caseStatusArr = ["WaitDispatch","Dispatched", "Responsed", "Delaying", "Arrived", "Completed"];
			}
		}
		return caseStatusArr;
    }
    /*
    *展開欄位面板
    */
	function openColumnBlock(){
		$('#columnBlockDlg').dialog({
			title : '欄位',
			width : 780,
			height : 410,
			top:10,
			closed : false,
			cache : false,
			method:'post',
			queryParams : {
				actionId : "<%=IAtomsConstants.ACTION_INIT_COLUMN_BLOCK%>"
			},
			href : "${contextPath}/caseHandle.do",
			modal : true,
			onLoad :function(){
				textBoxSetting("columnBlockDlg");
			},
			onBeforeClose : function(){
				// 欄位區塊關閉函數
				columnBlockCloseFun();
			},
			onClose : function(){
				$('#columnBlockDlg').dialog('clear');
			},
			buttons : [{
				text:'確定',
				width:'88',
				iconCls:'icon-ok',
				handler: function(){
					saveColumnTemplate(true);
				}
			},{
				text:'取消',
				width:'88',
				iconCls:'icon-cancel',
				handler: function(){
					$.messager.confirm('確認對話框','確認取消?', function(confirm) {
		     			if (confirm) {
		     				$("#columnBlockDlg").dialog('close');
		     			}
		     		});
				}
			}]
		}); 
	}
	
    /*
     *展示案件查詢欄位方法
     */
    function showUserColumn(contentColumn){
    	var userColumnOptions = [
    		{field:'checked',width:40,align:'center',halign:'center',formatter:function(value,row,index){return settingCheckbox(row,index);},title:"<input type=\"checkbox\" id=\"allCheckButton\" style=\"display:none;\" >"},
    		{field:'src',width:60,formatter:dealCaseFormatter,halign:'center',align:'center',title:"處理"},
    		{field:'processType',hidden:true}
    	];
    	// 處理欄位按鈕點擊打開選擇顯示欄位對話框
		var allColumnOptions = getAllColumnOptions();
    	// 取出當前列options
		var dgOptions = $("#dg").datagrid("options");
    	// 得到column 的options
		var columnOptions = dgOptions.columns;
		// 顯示模板列
		contentColumn.push("confirmAuthorizes");
		contentColumn.push("cmsCase");
		if(contentColumn.length != 0){
			// 顯示當前模板
			for(var i = 0; i < contentColumn.length; i++){
				for(var j = 0; j < allColumnOptions.length; j++){
					var tempColumn = allColumnOptions[j];
					if(tempColumn.field == contentColumn[i]){
						if (contentColumn[i] != "confirmAuthorizes" && contentColumn[i] != "cmsCase") {
							tempColumn.hidden = false;
						}
						userColumnOptions.push(tempColumn);
						break;
					}
				}
			}
			// 傳入參數
			$("#exportField").val(contentColumn.toString());
		}
		// 重新加載column 的options
		(columnOptions)[0] = userColumnOptions;
		// 重新加載查詢欄位字段
		if((dgOptions.queryParams).exportField){
			(dgOptions.queryParams).exportField = contentColumn.toString();
		}
		$("#dg").datagrid(columnOptions);
    }
    
    /*
    *初始化案件datagrid
    */
    function initQueryDatagrid(){
		var dgOptions;
		var grid = $("#dg");
		if(!grid.hasClass("easyui-datagrid")){
			grid.attr("style", "width: 98%; height: auto;");
			grid.addClass("easyui-datagrid");
			
			// 展示查詢列
    		var userCurrentTemplate = '${currentColumnTemplate.name}';
    		// 默認預設列
    		var userColumnOptions = [
        		{field:'checked',width:40,align:'center',halign:'center',formatter:function(value,row,index){return settingCheckbox(row,index);},title:"<input type=\"checkbox\" id=\"allCheckButton\" style=\"display:none;\" >"},
        		{field:'src',width:60,formatter:dealCaseFormatter,halign:'center',align:'center',title:"處理"},
        		{field:'processType',hidden:true}
        	];
        	if(userCurrentTemplate != ''){
        		var userColumnArray = userCurrentTemplate.split(',');
            	// 處理欄位按鈕點擊打開選擇顯示欄位對話框
        		var allColumnOptions = getAllColumnOptions();
        		userColumnArray.push("confirmAuthorizes");
        		userColumnArray.push("cmsCase");
        		// 顯示模板列
        		if(userColumnArray.length != 0){
        			// 顯示當前模板
        			for(var i = 0; i < userColumnArray.length; i++){
        				for(var j = 0; j < allColumnOptions.length; j++){
        					var tempColumn = allColumnOptions[j];
        					if(tempColumn.field == userColumnArray[i]){
        						if (userColumnArray[i] != "confirmAuthorizes" && userColumnArray[i] != "cmsCase") {
        							tempColumn.hidden = false;
        						}
        						//tempColumn.hidden = false;
        						userColumnOptions.push(tempColumn);
        						break;
        					}
        				}
        			}
        			// 傳入參數
        			$("#exportField").val(userColumnArray.toString());
        		}
        	}
        	// 創建datagrid
        	grid.datagrid({
    			title:"案件清單",
                fitColumns:false,
    			rownumbers:true,
    			nowrap : false,
    			pagination:true,
    			iconCls: 'icon-edit',
    			columns:[userColumnOptions],
    			pageList:[15,30,50,100],
				pageSize:15,
    			pageNumber:0,
    			onCheck:checkSign,
    			onUncheck:function(index,row){onUncheck(index, row, '${contextPath}');},
    			onSelect:dataGridOnSelect,
				onUnselect:dataGridUnSelect,
				cache : false,
    			singleSelect: false
    		});
    		
    	}
    }
    
	/*
	* 案件清單處理按鈕 formatter函數
	*/
	function dealCaseFormatter(value,row,index){
		var isAllowEdit = true;
		/* if(${formDTO.isCustomerService }){
			if(!isEmpty(row.caseStatus) && ('${caseStatusAttr.VOIDED.code }' == row.caseStatus)){
				isAllowEdit = false;
			}
		} else if(${formDTO.isCustomer } && ${!formDTO.isCustomerService }){
			if(!isEmpty(row.caseStatus) && ('${caseStatusAttr.WAIT_DISPATCH.code }' != row.caseStatus)){
				isAllowEdit = false;
			}
		} else if(${!formDTO.isCustomer } && ${!formDTO.isCustomerService } ){
			isAllowEdit = false;
		} */
		// CR #2951 廠商客服	//Task #3578 新增 客戶廠商客服
		if(${formDTO.isCustomerService or formDTO.isCusVendorService }){
			if(!isEmpty(row.caseStatus) && ('${caseStatusAttr.VOIDED.code }' == row.caseStatus)){
				isAllowEdit = false;
			}
		} else if(${formDTO.isVendorService } && ${!formDTO.isCustomerService }){
			// 建案廠商給客服公司不等於當前
			if(row.vendorServiceCustomer != '${logonUser.companyId }' || (!isEmpty(row.caseStatus) && ('${caseStatusAttr.VOIDED.code }' == row.caseStatus))){
				isAllowEdit = false;
			}
		} else if(${formDTO.isCustomer } && ${!formDTO.isCustomerService } && ${!formDTO.isVendorService } && ${!formDTO.isCusVendorService }){
			if(!isEmpty(row.caseStatus) && ('${caseStatusAttr.WAIT_DISPATCH.code }' != row.caseStatus)){
				isAllowEdit = false;
			}
		} else if(${!formDTO.isCustomer } && ${!formDTO.isCustomerService } && ${!formDTO.isVendorService } && ${!formDTO.isCusVendorService }){
			isAllowEdit = false;
		}
		// 顯示不同值
		if(isAllowEdit){
			return "<a href='javascript:void(0)' onclick=\"popEditCaseWindows(event,'" + encodeURIComponent(row.caseId) + "','" + row.isHistory +"','"+ row.caseStatus + "','"+ index + "','"+'${contextPath}'+"')\"  name='lbtEdit'></a>";
		} else {
			return "<a href='javascript:void(0)' onclick=\"popEditCaseWindows(event,'" + encodeURIComponent(row.caseId) + "','" + row.isHistory +"','"+ row.caseStatus + "','"+ index + "','"+'${contextPath}'+"')\"  name='lbtShow'></a>";
		}
	}

	/*
	*目的：案件簽收和線上排除時候，設備鏈接datagrid的動作下拉框拼值
	*row:正在格式化的設備鏈接的datagrid行.
	*assetLinkDatagridID：設備鏈接datagridId，
	*index：正在格式化的設備鏈接的datagrid行號
	*caseCategory：案件類別
	*/
	function caseAssetLinkActionformatter(value,row,assetLinkDatagridID, caseCategory){
		var isAction = row.isAction;
		var assetLinkId = row.assetLinkId+assetLinkDatagridID;
		if(isAction == '${iatomsContants.YES}') {
			var html = "";
				html = " <select class=\"easyui-combobox paymentAsset\" editable=\"false\" panelHeight=\"auto\" require=\"true\" validType=\"ignore['請選擇']\" invalidMessage=\"請輸入動作\" id='"+assetLinkId+"' style='border-radius: 4px; border: #95B8E7 1px solid;width: 100px'>";
				html += "<option value=\"\">請選擇</option>"
				<c:forEach var="claimInfo" items="${actionsList}" varStatus="claimInfos">
					html += "<option value=\"${claimInfo.value}\">${claimInfo.name}</option>"
				</c:forEach>
				html += "</select>";
				return html;
		} else {
			return;
		}
	}

	/*
	*新建案件。 caseCategory:案件類型。
	*/
	function createCase(caseCategory) {
		var title = "案件建案-";
		<c:forEach var="caseCategory" items="${caseCategoryList}" varStatus="itemStatus">
		<c:if test="${!itemStatus.first}">else</c:if> if(caseCategory == '${caseCategory.value}') {
			title += '${caseCategory.name}';
		}
		</c:forEach>
		popCreateCaseWindows(title,caseCategory);
	}
	
	/*
	* 判斷上傳文件
	*/
	function uploadAllComplete(succeeded,failed){
		if(!failed || failed) {
			// 調保存方法
			saveCaseDetailData(isDispach,function(msg){
				var msgDesc = '';
				if(isDispach == "${iatomsContants.ACTION_NEW_DISPATCH}") {
					$.messager.alert('提示訊息',msg,'', function(){
						viewDlg.dialog('close');
						var pageIndex = getGridCurrentPagerIndex("dg");
						query(pageIndex, false);
					});
				} else {
					// 提示消息框
					alertPromptCommon(msg, false, function(){
						viewDlg.dialog('close');
						var pageIndex = getGridCurrentPagerIndex("dg");
						// 調查詢方法
						query(pageIndex, false);
					});
				}
			});
		} else {
			$.messager.alert('提示訊息', '保存失敗,請聯繫管理員.', 'error'); 
		}
	}
	var isDispach = '';
	// 查詢dtid的dialog
	var viewQueryDtid = undefined;
	// 查詢特店的dialog
	var viewChooseMid = undefined;
	/**
	*處理頁面待派工案件執行派工 
	*/
	function comfirmDispatch(caseCategory){
		comfirmCommon('確認派工？',function(){
						isDispach = "${iatomsContants.ACTION_NEW_DISPATCH}";
						saveCase(null, caseCategory);
			});
	}
	/**	彈出新增案件畫面	title：畫面標題; type：案件類型
	*/
	function popCreateCaseWindows(title,caseCategory) {
		$("#dgResponse-msg").text("");
		viewDlg = $('#createDlg').dialog({
			title : title,
			width : 900,
			height : 500,
			modal : true,
			closed : false,
			maximizable: true,
			cache : false,
			queryParams : {
				actionId : "${iatomsContants.ACTION_INIT_DETAIL}",
				caseCategory : caseCategory
			},
			onBeforeLoad : function(){
				// clear當前對話框
				if(viewDlg){
					viewDlg.panel('clear');
				}	
			},
			onLoad :function(){
				// 文本框限制長度
		    	textBoxSetting("createDlg");
				// 日期框限制設定
		    	dateboxSetting("createDlg"); 
		    	// 調用多選下拉框選中事件
		    	mutilComboSelectEvent("createDlg");
		    	// clear查詢dtid對話框
				if(viewQueryDtid){
					viewQueryDtid.panel('clear');
				}
				// clear查詢Mid對話框
				if(viewChooseMid){
					viewChooseMid.panel('clear');
				}
				/* if (caseCategory != '${caseCategoryAttr.INSTALL.code}'){
					if(typeof disabledForDtid != 'undefined' && disabledForDtid instanceof Function){
						disabledForDtid(caseCategory, true);
					}
				} */
			},
			onClose : function(){
				// clear當前案件打開對話框
				viewDlg.panel('clear');
				// clear查詢dtid對話框
				if(viewQueryDtid){
					viewQueryDtid.panel('clear');
				}
				// clear查詢Mid對話框
				if(viewChooseMid){
					viewChooseMid.panel('clear');
				}
				$(".qq-uploader-selector").unblock();
			}, 
			href : "${contextPath}/caseHandle.do",
			modal : true,
			buttons : [{
				text : '儲存',
				width :'90px',
				iconCls : 'icon-ok',
				handler : function() {
					isDispach = "${iatomsContants.ACTION_CREATE_CASE}";
					// 調案件保存方法
					saveCase(null, caseCategory);
				}
			},
			<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.DISPATCH.code )}">{ 
				text : '派工',
				width :'90px',
				iconCls : 'icon-redo',
				handler : function() {
					if (caseCategory == "${caseCategoryAttr.INSTALL.code}") {
						var installType = $("#${stuff }_installType").combobox("getValue");
						var confirmAuthorizes = $("#hideConfirmAuthorizes").val();
						if (confirmAuthorizes != 'Y' && installType == '4') {
							$.messager.alert('提示訊息', '尚未授權確認，不可派工','warning');
							return false;
						}
					}
					comfirmCommon('確認派工？',function(){
						isDispach = "${iatomsContants.ACTION_NEW_DISPATCH}";
						saveCase(null, caseCategory);
					});
				}
			},</c:if>
			{
				text:'取消',
				width:'90px',
				iconCls:'icon-cancel',
				handler: function(){
					confirmCancel(function(){
						viewDlg.dialog('close');
					});
				}
			}],
		}).dialog("maximize").dialog("center");
	}
	
	
	/*
	* 保存案件方法
	*/
	function saveCase(isSaveCase, caseCategory){
		if (saveCaseDetailData) {
			if (isSaveCase) {
				isDispach = "${iatomsContants.ACTION_CREATE_CASE}";
			}
			if (caseCategory != '${caseCategoryAttr.OTHER.code}') {
				var controls = ['transactionType','merchantCode','merchantCodeOther','tid'];
				if(!validateFormInRow('transDataGrid', editIndex, controls)){
					return;
				} else {
					$("div").unbind("scroll.validate");
				}
			}
			$("div.topSoller").unbind("scroll.validate");
			// 案件保存方法 包含驗證消息等
			saveCaseDetailData(isDispach,function(msg){
				var msgDesc = '';
				if(isDispach == "${iatomsContants.ACTION_NEW_DISPATCH}") {
					$.messager.alert('提示訊息',msg,'', function(){
						viewDlg.dialog('close');
						var pageIndex = getGridCurrentPagerIndex("dg");
						query(pageIndex, false);
					});
				} else {
					alertPromptCommon(msg, false, function(){
						viewDlg.dialog('close');
						var pageIndex = getGridCurrentPagerIndex("dg");
						query(pageIndex, false);
					});
				}
			});
		}
	};
    /*
	* 案件查詢 pageIndex：頁碼 hidden：標志位
	*/
	function query(pageIndex, hidden){
		var grid = $("#dg");
		btnCancelUpload();
		//Task #3269
		if($('#uploadCaseEnd').hasClass("easyui-panel")){
			$('#uploadCaseEnd').panel('close');
		}
		isSign = false;
		// 驗證表單數據是否正確
		if(!$('#queryForm').form('validate')){
			// 點擊查詢
			if(hidden){
				return false;
			} else {
				// 當前頁面msg消息框內容
				var msgText = $("#dgResponse-msg").text();
				// 當前頁面msg消息框內容
				var dialogMsgText = $("#dataGridResponse-msg").text();
				// 處理頁面動作消息不爲空
				if(!isEmpty(dialogMsgText)){
					// 將處理頁面消息賦值
					msgText = dialogMsgText;
				}
				// 調整消息內容
				if(isEmpty(msgText)){
					msgText = "查詢條件驗證未通過，請修正後重新查詢";
				} else {
					msgText += "，查詢條件驗證未通過，請修正後重新查詢";
				}
				// 清空datagrid
				if(grid.hasClass("easyui-datagrid")){
					grid.datagrid('loadData', { total: 0, rows: [] });
				}
				$("#dgResponse-msg").text(msgText);
				return false;
			}
		}
		// 得到排序字段
		var sortName;
		if(hidden){
			sortName = '';
		} else {
			if(grid.hasClass("easyui-datagrid")){
				sortName = grid.datagrid("options").sortName;
			}
		}
		var queryParam = $('#queryForm').form("getData");
		// 處理客戶權限時查詢客戶欄位	//Task #3578 客戶廠商客服 
		if(${formDTO.isCustomerAttribute || formDTO.isCustomerVendorAttribute}){
			queryParam.queryCompanyId = $("#queryCompanyId").combobox('getValue');
		}
		// 處理查詢即時資料
		queryParam.isInstant = $("#isInstant").prop("checked");
		//Task #3452 微型商戶
		queryParam.isMicro = $("#isMicro").prop("checked");
		//Task #3549 查詢條件 新增-CyberEDC到場
		queryParam.isMicroArrive = $("#isMicroArrive").prop("checked");
		// 處理查詢sla警示件
		if($("#queryWarningSla").prop("checked")){
			queryParam.queryWarningSla = 'Y';
		} else {
			queryParam.queryWarningSla = 'N';
		}
		// 遮罩样式
		var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
	
		// datagrid options
		var dgOptions = grid.datagrid("options");
		// 查詢的options
		var dgQueryOptions = {
			url : "${contextPath}/caseHandle.do?actionId=queryCase",
			queryParams :queryParam,
			pageNumber:pageIndex,
			autoRowHeight:true,
			loadMsg : '',
			onBeforeLoad : function() {
				// 加遮罩
				$.blockUI(blockStyle);
			},
			sortName : sortName,
			view: detailview,
			detailFormatter: function (index, row) {
				return '<div style="padding:2px"><table class="ddv" id="ddv-' + index + '"></table><table class="dataGridSuppliesLink" id="dataGridSuppliesLink-' + index + '"></table></div>';
			},
			onLoadError : function(data) {
				// 去除遮罩
				$.unblockUI();
				$("#dgResponse-msg").text("查詢案件資料出錯");
			},
			onLoadSuccess : function (data) {
				$(this).datagrid("fixRownumber","dg"); 
				//清空設備序號隱藏域 2018/01/31
				$("#selectSn").val("");
				$("#hideLeaseSignFlag").val('');
				// 處理datagrid列標題收縮
				var isLoad = false;
				// datagrid列樣式
				var styleHtml = grid.next().html();
				if(styleHtml && !isEmpty(styleHtml)){
					var reg = /-src/g;
					var arr = styleHtml.match(reg);
					if(arr && arr.length == 1){
					} else {
						// datagrid列樣有重複或不存在需要重新加載樣式
						isLoad = true;
					}
				} else {
					// datagrid列樣式無則需要重新加載樣式
					isLoad = true;
				}
				// 若列標題收縮則重新加載樣式
				if(isLoad){
					grid.datagrid();
				}
				// 去除遮罩
				$.unblockUI();
				//Task #3500 
				//1. 若是CyberEDC，顯示 租賃授權確認、案件匯入、新增記錄、顯示記錄、派工、自動派工、線上排除、作廢、協調完成、到貨檢測、租賃預載、租賃配送中、租賃簽收、列印工單
				if($('#isMicro').is(':checked')){
					//若是CyberEDC 隱藏域設為 1 
					$("#hideCyberEdc").val("1");
					cyberEdcBtnControl("");
					//3. 若是CyberEDC，到貨檢測 按鈕 顯示為 到貨檢測     
					if($("#button_payment").length > 0){
						$("#button_payment").find(".l-btn-text").html("到貨檢測");
						$("#button_payment").attr('onclick', '').unbind('click').click( function () { showActionView('payment'); });
					}

					//Task #3549 若CyberEDC=是，開放 選擇 CyberEDC到場 是/否，預設否
					var notMicroArrive = $("#notMicroArrive");
					if(notMicroArrive.prop("disabled")){
					} else {
						//CyberEDC=是 & CyberEDC到場=是，操作按鈕，比照 CyberEDC=否，然後移除 協調完成匯入、然後 新增 租賃授權確認、租賃授權取消、租賃預載
						if($("input:radio[name='queryMicroArrive']:checked").val()=='2'){
							cyberEdcBtnControl("none");
							if($("#button_payment").length > 0){
								$("#button_payment").find(".l-btn-text").text("求償");
								$("#button_payment").attr('onclick', '').unbind('click').click( function () { showActionView('arrivalInspection'); });
							}
							if($("#importCaseEnd").length > 0){
								$("#importCaseEnd").css("display","none");
							}
							//租賃授權確認
							if($("#button_confirmAuthorizes").length > 0){
								$("#button_confirmAuthorizes").css("display","");
							}
							//租賃授權取消
							if($("#button_cancelConfirmAuthorizes").length > 0){
								$("#button_cancelConfirmAuthorizes").css("display","");
							}
							//租賃預載
							if($("#button_leasepreload").length > 0){
								$("#button_leasepreload").css("display","");
							}
						} else {
							//CyberEDC=是 & CyberEDC到場=否，操作按鈕維持現狀
						}
					}
					
				}else{
					//若不是CyberEDC 隱藏域設為 0 
					$("#hideCyberEdc").val("0");
					//2. 若不是CyberEDC，則不顯示 租賃開頭的按鈕
					cyberEdcBtnControl("none");
					//3. 若不是CyberEDC，到貨檢測 按鈕 顯示為 求償     
					if($("#button_payment").length > 0){
						$("#button_payment").find(".l-btn-text").text("求償");
						$("#button_payment").attr('onclick', '').unbind('click').click( function () { showActionView('arrivalInspection'); });
					}
				}
				// 處理逾時按鈕顯示樣式
				$('a[name=iconInfo]').linkbutton({ plain: true, iconCls: 'icon-info' });
				$('a[name=iconWarnning]').linkbutton({ plain: true, iconCls: 'icon-warnning' });
				// 處理編輯按鈕顯示樣式
				$('a[name=lbtEdit]').linkbutton({ plain: true, iconCls: 'icon-edit' });
				$('a[name=lbtShow]').linkbutton({ plain: true, iconCls: 'icon-search' });
			// 關閉案件動作datagrid
				//	$('#dgResponse').closest('.easyui-panel').panel('close');
				if($("#response").hasClass("easyui-panel")){
					var rowsLength = $('#dgResponse').datagrid("getRows").length;
					if (rowsLength != 0) {
						$('#dgResponse').datagrid('deleteRow', 0);
					}
					$('#dgResponse').closest('.easyui-panel').panel('close');
				} else {
					$("#response").addClass("easyui-panel");
					$("#response").attr("style", "width: 98%; height: auto;");
					$("#response").panel();
					$("#dgResponse").addClass("easyui-datagrid");
					$("#dgResponse").datagrid();
					$("#response").children(':first').attr("style", "display:none;");
				}
				if (hidden) {
					$("#dgResponse-msg").text("");
					if (data.total == 0) {
						// 提示查無數據
						$("#dgResponse-msg").text(data.msg);
					}
				}
				// 處理欄位與匯出按鈕顯示
				if (data.total == 0) {
					// Task #3086 開放案件處理及設備管理欄位模板先編輯再查詢
					/* $('#filterBlank').attr("onclick","return false;");
					$('#filterBlank').attr("style","color:gray"); */
					$('#btnExport').attr("onclick","return false;");
					$('#btnExport').attr("style","color:gray");
				} else {
					// Task #3086 開放案件處理及設備管理欄位模板先編輯再查詢
/* 					$('#filterBlank').attr("onclick","openColumnBlock();");
					$('#filterBlank').attr("style","color:blue"); */
					$('#btnExport').attr("onclick","variableExport();");
					$('#btnExport').attr("style","color:blue");
				}
				// 調顯示記錄的detailview
				addCaseLogExpand('${contextPath}');
				hidden = true;
				isSign = false;
				
				$("#allCheckButton").unbind().click(function(){checkAllFun();});
				// 處理全選按鈕
				var checkBtnRow = grid.closest(".datagrid-wrap").find(".datagrid-view2").find("input[name='checkBtn']");
				// 處理全選按鈕隱藏
				if(checkBtnRow.length == 0){
					$("#allCheckButton").hide();
				} else {
					$("#allCheckButton").show();
				}
				
			},
			rowStyler :  function (index,row) {
				// 催修件 該行爲黃色
				if (row.rushRepair == 'Y'){
					return 'background-color:yellow;';
				}
			}
		};
		// 將查詢option追加至datagrid options
		$.extend( dgOptions, dgQueryOptions);
		// 加載options
		grid.datagrid(dgOptions);
	}
	 /**
	  * 設置datagrid內部複選框選中
	  */
	 function settingCheckbox(row, index){
		var isShowCheckbox = true;
		/* // CR #2951 廠商客服
		if (${formDTO.isCustomerService} || ${formDTO.isVendorService}) {
		} else {
			//CR #2394 增加cyberAgent update by 2017/09/13
			//不是VendorAgent 且 不是CyberAgent
			if((!${formDTO.isVendorAgent}) && (!${formDTO.isCyberAgent})){
				//既是tms又是QA時
				if (${formDTO.isTMS} && ${formDTO.isQA} && row.departmentName !='${caseRoleAttr.TMS.code}' && row.departmentName !='${caseRoleAttr.QA.code}') {
					isShowCheckbox = false;
					//是tms時
				} else if (${formDTO.isTMS} && ${!formDTO.isQA} && row.departmentName !='${caseRoleAttr.TMS.code}') {
					isShowCheckbox = false;
					//是QA時
				} else if (${formDTO.isQA} && ${!formDTO.isTMS} && row.departmentName !='${caseRoleAttr.QA.code}') {
					isShowCheckbox = false;
				}
			} else {
				if(row.caseStatus !='WaitDispatch'){
					//維護廠商不為登陸者公司
					if(row.companyId != '${logonUser.companyId}') {
						if((!${formDTO.isTMS}) && (!${formDTO.isQA})){
							isShowCheckbox = false;
						}
						//既是tms又是QA時
						else if (${formDTO.isTMS} && ${formDTO.isQA} && row.departmentName !='${caseRoleAttr.TMS.code}' && row.departmentName !='${caseRoleAttr.QA.code}') {
							isShowCheckbox = false;
							//是tms時
						} else if (${formDTO.isTMS} && ${!formDTO.isQA} && row.departmentName !='${caseRoleAttr.TMS.code}') {
							isShowCheckbox = false;
							//是QA時
						} else if (${formDTO.isQA} && ${!formDTO.isTMS} && row.departmentName !='${caseRoleAttr.QA.code}') {
							isShowCheckbox = false;
						}
					} else {
						//維護廠商為登陸者公司
						if (row.departmentName =='${caseRoleAttr.TMS.code}' 
									|| row.departmentName =='${caseRoleAttr.QA.code}' 
									|| row.departmentName =='客服'){
							//$.messager.alert('提示訊息','TMS，QA只能處理派工給TMS或QA的案件','warning');
							if(row.departmentName =='${caseRoleAttr.TMS.code}' && ${!formDTO.isTMS}){
								isShowCheckbox = false;
							}
							if(row.departmentName =='${caseRoleAttr.QA.code}' && ${!formDTO.isQA}){
								isShowCheckbox = false;
							}
							if(row.departmentName =='客服'){
								isShowCheckbox = false;
							}
						}
					}
				} else {
					isShowCheckbox = false;
				}
			}
		} */
		// Task #3070 案件管理 TMS等可新增記錄 CyberAgent不變        					//Task #3578 客戶廠商客服
		if (${!formDTO.isCustomerService} && ${!formDTO.isVendorService} && ${!formDTO.isCusVendorService} && ${!formDTO.isTMS} && ${!formDTO.isQA} && ${formDTO.isCyberAgent}) {
			//維護廠商不為登陸者公司
			if(row.companyId != '${logonUser.companyId}') {
				isShowCheckbox = false;
			}
		}
		// 處理復選按鈕顯示
		if(isShowCheckbox){
			return '<input name="checkBtn" type="checkbox" >';
		}
	}

		/*
		* 	目的：簽收和線上排除時候 設備鏈接檔datagrid生成所需列
		*	caseId:案件編號
		*	customerId：客戶id
		*	assetLinkDatagridID：設備鏈接當的datagridId
		*	caseCategory：案件類別
		*/
		function createCaseAssetLinkDatagridColums(caseId, customerId, assetLinkDatagridID, caseCategory) {
			var datagridColums = [];
			datagridColums.push({ field: 'removeContent', title: '拆回說明', width: 100,hidden:true,type:'textbox' });
			datagridColums.push({ field: 'isAction', title: '是否顯示動作', width: 100,hidden:true,type:'textbox' });
			datagridColums.push({ field: 'isRepeatLink', title: '是否重複連接', width: 100,hidden:true,type:'textbox' });
			datagridColums.push({ field: 'actionValue', title: '動作value', width: 100,hidden:true,type:'textbox' });
			datagridColums.push({ field: 'warehouseId', title: '倉庫ID', width: 300,hidden:true,type:'textbox' });
			datagridColums.push({ field: 'contractId', title: '合約ID', width: 300,hidden:true,type:'textbox' });
			datagridColums.push({ field: 'assetLinkId', title: 'ID', width: 300,hidden:true,type:'textbox' });
			datagridColums.push({ field: 'itemType', title: '類別', width: 300,hidden:true,type:'textbox' });
			datagridColums.push({ field: 'propertyId', title: '財產編號', width: 300,hidden:true,type:'textbox' });
			datagridColums.push({ field: 'uninstallSerialNumber', title: '拆機設備序號', width: 300,hidden:true,type:'textbox' });
			datagridColums.push({ field: 'serialNumber', title: '設備序號', halign:'center', width: 160 });
			datagridColums.push({ field: 'itemId', title: '設備名稱', halign:'center', type:'textbox',width: 160, formatter: function (value, row) {
					return row.itemName;
				}
			});
			datagridColums.push({ field: 'itemCategory', title: '設備類別', halign:'center', type:'textbox',width: 140, formatter: function (value, row) {
					return row.itemCategoryName;
				}
			});
			//案件類別為裝機時隱藏。
			if(caseCategory != '${caseCategoryAttr.INSTALL.code }'){
				datagridColums.push({
					field: 'action', title: '動作', width: 130 , halign:'center', formatter:function(value,row,index){
						return caseAssetLinkActionformatter(value,row,assetLinkDatagridID, caseCategory);
					}
				});
				datagridColums.push({
					field: 'content', title: '說明', halign:'center', formatter: caseAssetLinkContentFormatter
				});
			}
			datagridColums.push({
				field: 'isLink', title: '設定連結', width: 100, halign:'center', formatter: function(value,row,index){
					return caseAssetLinkFormatter( value,row, index,caseId, assetLinkDatagridID,caseCategory);
				}
			});
			return datagridColums;
		}

		/*
		* 目的：簽收和線上排除時候 創建設備鏈接和耗材的options
		* isExpandRow:是否展開行的時候觸發 （判斷是主頁面還是處理頁面，主頁面傳入True）
		* AssetLinkDatagrid ： 設備鏈接的datagridID
		* suppliesLinkDatagrid : 耗材鏈接的datagridId
		* datagridColums： 設備鏈接的datagridColums
		* row：展開的行數據
		* index：展開的行index
		* queryData：傳回後台的數據json格式
		*/
		function createAssetAndSuppliesLinkOptions(isExpandRow,assetLinkDatagrid,suppliesLinkDatagrid, datagridColums,row,index, queryData){
			//設備鏈接的查詢數據
			var queryDatas;
			if(isExpandRow) {
				queryDatas = {caseId : row.caseId, dtid : row.dtid};
			} else {
				queryDatas = queryData;
			}
			var customerId = row.customerId;
			//耗材鏈接的查詢數據
			var querySuppliesData = {caseId : row.caseId,customerId : row.customerId,caseCategory:row.caseCategory};
			//獲取設備鏈接的datagrid的options
			var caseAssetLinkOptions = expandAssetLinkDatagrid(isExpandRow, queryDatas, datagridColums, index, '${contextPath}');
			assetLinkDatagrid.datagrid(caseAssetLinkOptions); 
			
			var datagridId;
    		if(isExpandRow) {
    			datagridId = "#dataGridSuppliesLink-"+index;
    		} else {
    			datagridId = "#dataGridSuppliesLink";
    		}
    		//獲取耗材鏈接的datagrid的options
			var caseSuppliesLinkOptions = expandSuppliesLinkDatagrid(querySuppliesData, datagridId, isExpandRow, customerId, index, '${contextPath}')
			suppliesLinkDatagrid.datagrid(caseSuppliesLinkOptions); 
		}
		/*
		* 目的：簽收和線上排除時候 創建簽收的設備鏈接和耗材的datagrid
		*/
		function addCaseAssetAndSuppliesLink() {
			var options = $("#dg").datagrid('options');
			options.onExpandRow = function (index, row) {
				if (isSign) {
					//var rows = $("#dg").datagrid("getSelections");
					//if (rows) {
						if (signCaseCategory != row.caseCategory || row.caseStatus != signCaseStatus) {
							$.messager.alert('提示訊息','請選擇同一案件狀態、案件類別的案件','warning');
							$("#dg").datagrid("collapseRow",index);
							return false;
						}
					//}
				}
				//設備鏈接的datagrid
				var assetLinkDatagrid = $("#dg").datagrid('getRowDetail', index).find('table.ddv');
               	//設備鏈接的datagridID
                var assetLinkDatagridID = assetLinkDatagrid.attr("id");
                //說明的下拉框
                var actionsList = initSelect(<%=actionsListString %>);
                var selectRows = $("#dg").datagrid('getSelections');
                var caseCategory = null;
              //案件類別
                if (selectRows.length > 0) {
                	caseCategory = selectRows[0].caseCategory;
                } else {
                	caseCategory = signCaseCategory;
                }
                var datagridColums = [];
                var isExpandRow = true;
                //耗材鏈接的datagrid
                var suppliesLinkDatagrid = $(this).datagrid('getRowDetail', index).find('table.dataGridSuppliesLink');
               	//耗材鏈接的datagridColums
                datagridColums = createCaseAssetLinkDatagridColums(row.caseId, row.customerId, assetLinkDatagridID,caseCategory); 
                createAssetAndSuppliesLinkOptions(isExpandRow,assetLinkDatagrid,suppliesLinkDatagrid, datagridColums,row,index);
			}
		}
	
        /*
        *	初始化選擇EDC界面
        *	obj : 按鈕 this  ，
        *	index：正在處理的行號，
        *	assetLinkDatagridID：正在處理的datagridId
        *	caseId：正在處理行的案件編號 ，
        *	caseCategory ：正在處理行的案件類別
        */
      function linkCaseAsset(obj, value,index,assetLinkDatagridID, caseId, caseCategory) {
      		var index = $(obj).parents("tr[datagrid-row-index]").attr("datagrid-row-index");
      		var rows = $("#"+assetLinkDatagridID).datagrid('getRows');
			var row = rows[index];
			var itemId = row.itemId;
			var itemCategory = row.itemCategory;
			var assetLinkId = row.assetLinkId;
      		$("#assetLinkParamerValue").val(value);
      		$("#assetLinkParamerAssetLinkDatagridID").val(assetLinkDatagridID);
      		$("#assetLinkParamerCaseId").val(caseId);
      		$("#assetLinkParamerIndex").val(index);
      		$("#assetLinkParamerCaseCategory").val(caseCategory);
      		$("#message").text("");
			var title = "選擇EDC";
			// 彈出新增的對話框
			$('#edcDialog').dialog({
				title : title,
				width : 960,
				height : 600,
				top:10,
				closed : false,
				cache : false,
				method:'post',
				queryParams : {
					actionId : "<%=IAtomsConstants.ACTION_INIT_CHOOSE_EDC%>",
					chooseEdcAssetId : itemId,
					chooseEdcAssetCategory : itemCategory,
					caseId : caseId
				},
				href : "${contextPath}/caseHandle.do",
				modal : true,
				onLoad :function(){
					textBoxSetting("edcDialog");
					settingDialogPanel('edcDialog');
					var params = {
							chooseEdcAssetCategory : itemCategory,
							chooseEdcAssetId : itemId,
							caseId : caseId
						}
					if(typeof queryChooseEdcData != 'undefined' && queryChooseEdcData instanceof Function){
						// 查詢EDC
						queryChooseEdcData(1, params);
					}
					
				},
				onClose : function(){
					$('#edcDialog').dialog('clear');
				}
			}); 
      }
	
	/*
	* 案件處理-動作按鈕打開 actionId:當前動作id
	*/
	function showActionView(actionId, leaseSignFlag) {
		//Task #3358 當leaseSignFlag=Y 時 為 租賃簽收
		if((actionId == '${caseActionAttr.ONLINE_EXCLUSION.code }') && leaseSignFlag=='Y'){
			$("#hideLeaseSignFlag").val('Y');
		} else {
			$("#hideLeaseSignFlag").val('N');
		}
		isSign = false;
		if($('#uploadCaeInfo').hasClass("easyui-panel")){
			$('#uploadCaeInfo').panel('close');
		} else {
			$("#uploadCaeInfo").addClass("easyui-panel");
			$("#uploadCaeInfo").attr("style", "width: 98%; height: auto;");
			$("#uploadCaeInfo").panel();
		}
		//Task #3269
		if($('#uploadCaseEnd').hasClass("easyui-panel")){
			$('#uploadCaseEnd').panel('close');
		}
		// $('#uploadCaeInfo').panel('close');
	//	$("#caseCategory").combobox("setValue", "");
		if($('#caseCategory').hasClass("easyui-combobox")){
			$("#caseCategory").combobox("setValue", "");
		}
		$(".qq-upload-list-selector").html("");
		$("#dgResponse" +  '-msg').text("");
		// 驗證選中案件的案件信息等
		if(validateSelectCase(actionId, 'dgResponse')){
			var rows = $("#dg").datagrid('getSelections');
			if (actionId == '${caseActionAttr.ONLINE_EXCLUSION.code }') {
				/* var tempRow = checkConfirmAuthorizes(rows);
				if (tempRow != null && tempRow != '') {
					if($("#hideLeaseSignFlag").val()=='Y'){
						$("#hideLeaseSignFlag").val('N');
						$.messager.alert('提示訊息',"來自"+tempRow.caseId+"訊息："+'雲端租賃設備，不可租賃簽收','warning');
					}else{
						$.messager.alert('提示訊息',"來自"+tempRow.caseId+"訊息："+'雲端租賃設備，不可線上排除','warning');
					}
					return false;
				} */
			}
			if (actionId == '${caseActionAttr.AUTO_DISPATCHING.code }') {
				var tempRow = checkConfirmAuthorizes(rows);
				if (tempRow != null && tempRow != '') {
					$.messager.alert('提示訊息',"來自"+tempRow.caseId+"訊息："+'尚未授權確認，不可派工','warning');
					return false;
				}
			}
			// 案件狀態
			var caseStatus = $("#dg").datagrid('getSelections')[0].caseStatus;
			// 案件類別
			var caseCategory = $("#dg").datagrid('getSelections')[0].caseCategory;
			// 是否tms
			var isTms = $("#dg").datagrid('getSelections')[0].isTms;
			// 部門名稱
			var departmentName = $("#dg").datagrid('getSelections')[0].departmentName;
			var responsibity = $("#dg").datagrid('getSelections')[0].responsibity;
			var problemReason = $("#dg").datagrid('getSelections')[0].problemReason;
			var problemReasonCode = $("#dg").datagrid('getSelections')[0].problemReasonCode;
			var problemSolution = $("#dg").datagrid('getSelections')[0].problemSolution;
			var problemSolutionCode = $("#dg").datagrid('getSelections')[0].problemSolutionCode;
			var cmsCase = $("#dg").datagrid('getSelections')[0].cmsCase;
			var customerId = $("#dg").datagrid('getSelections')[0].customerId;
			// 打開案件動作處理panel
			$('#response').panel('open');
			// 處理案件動作datagrid
		//	dealCreateGrid(actionId, 'dgResponse', caseStatus, caseCategory);
			var allParams = {
					actionId : actionId,
					datagridId : 'dgResponse',
					panelId : 'response',
					caseStatus : caseStatus,
					caseCategory : caseCategory,
					isTms : isTms,
					departmentName : departmentName,
					responsibity:responsibity,
					problemReason:problemReasonCode+"-"+problemReason,
					problemSolution:problemSolutionCode+"-"+problemSolution,
					customerId:customerId,
					cmsCase : cmsCase,
			};
			// 處理創建grid的資料
			dealCreateGridInfo(allParams);
			// 折疊detail
			collapseSubView('${contextPath}');
			// 籤收、線上排除detail
			if((actionId == '${caseActionAttr.ONLINE_EXCLUSION.code }') 
				|| (actionId == '${caseActionAttr.SIGN.code }')){
				//案件類別不是查檢才需展示
				if(caseCategory != '${caseCategoryAttr.CHECK.code}' && caseCategory != '${caseCategoryAttr.OTHER.code}') {
					addCaseAssetAndSuppliesLink();
					showAssetAndSupplies();
					isSign = true;
					signCaseCategory = caseCategory;
					signCaseStatus = caseStatus;
				}
			}
		}
	}
	
	/*
     *案件處理 -- 作廢按鈕
     */
	function showVoidView(datagridId,caseId, caseStatus){
		//$('#dgResponse').closest('.easyui-panel').panel('close');
		//修改未查询之前 点击作废会将主画面 panel关掉的问题 2018/01/19 
		//改为判断 当response的panel存在时 才关掉 panel 2018/01/19 
		if($("#response").hasClass("easyui-panel")){
			var rowsLength = $('#dgResponse').datagrid("getRows").length;
			if (rowsLength != 0) {
				$('#dgResponse').datagrid('deleteRow', 0);
			}
			$('#dgResponse').closest('.easyui-panel').panel('close');
		}
		collapseSubView('${contextPath}');
		addCaseLogExpand('${contextPath}');
		var flag = false;
		if(caseId){
			flag = true;
		} else {
			if(validateSelectCase('voidCase',datagridId)){
				flag = true;
			}
		}
		var updatedDateString = '';
		//判斷是否滿足作廢的條件
		if(flag){
			//確定-發送請求，取消-留在本頁面
			comfirmCommon("確認作廢？", function(){
				var selectRow = $("#dg").datagrid('getSelections');
				var nowCaseStatus;
				var caseIds = "";
				if(caseId) {
					caseIds = caseId;
					updatedDateString = $("#hideUpdateDate").val()+";"+caseId;
					nowCaseStatus = caseStatus;
				} else {
					for (var i = 0; i< selectRow.length; i++) {
						if(!nowCaseStatus){
							nowCaseStatus = selectRow[i].caseStatus;
						}
						if(selectRow.length >=1 && i == selectRow.length -1 ){
							updatedDateString = updatedDateString + selectRow[i].updatedDate +";"+ selectRow[i].caseId;
							caseIds = caseIds + selectRow[i].caseId;
						} else {
							updatedDateString = updatedDateString + selectRow[i].updatedDate +";"+ selectRow[i].caseId + ",";
							caseIds = caseIds + selectRow[i].caseId + ",";
						}
					}
				}
				var saveParam = {
						caseId : caseIds,
						caseStatus : '${caseStatusAttr.VOIDED.code }',
						caseActionId : '${caseActionAttr.VOID_CASE.code }',
						updatedDateString : updatedDateString,
						nowCaseStatus : nowCaseStatus
					}
				var saveParamJson = JSON.stringify(saveParam);
				var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
				if(caseId){
					// 調保存遮罩
					commonSaveLoading('editDlg');
				} else {
					//加遮罩
					$.blockUI(blockStyle);
				}
				//加遮罩
				$.blockUI(blockStyle);
				$.ajax({
					url : "${contextPath}/caseHandle.do?actionId=" + '${caseActionAttr.VOID_CASE.code}',
					data : saveParam,
					type : 'post',
					cache : false, 
					dataType :'json',
					success : function(json) {
						//關閉處理頁面
						if(datagridId == 'dataGridResponse') {
							viewDlg.dialog('close');
							// 處理頁面修改時更新案件相關欄位
			    			updateCase(caseId, '${caseActionAttr.VOID_CASE.code }');
						}
						//查詢
						var pageIndex = getGridCurrentPagerIndex("dg");
						if (json.success) {
							query(pageIndex, false);
							//call CMS API成功提示
							if (json.cmsResult != undefined && !json.cmsResult) {
								$.messager.alert('提示訊息',json.msg,'');
							}else{
								$("#dgResponse-msg").text(json.msg);
							}
						} else {
							$("#dgResponse-msg").text(json.msg);
							// 去除遮罩
							$.unblockUI();
						}
					},
					error : function(){
						$("#" + (datagridId + '-msg')).text(json.msg);
						if(caseId){
							// 調取消保存遮罩
							commonCancelSaveLoading('editDlg');
						} else {
							// 去除遮罩
							$.unblockUI();
						}
					}	
				});
			});
		} 
	} 
	/*
	*派工自動派工驗證：TMS，QA只能處理派工給TMS或QA的案件
	*/
	/* function validateQaAndTms(param){
		//"dgResponse""dataGridResponse"
		//當前登錄者是客服時不走以下驗證 // CR #2951 廠商客服
		if (${formDTO.isCustomerService} || ${formDTO.isVendorService}) {
			return true;
		} else {
			//CR #2394 增加cyberAgent update by 2017/09/13
			//不是VendorAgent 且 不是CyberAgent
			if((!${formDTO.isVendorAgent}) && (!${formDTO.isCyberAgent})){
				//既是tms又是QA時
				if (${formDTO.isTMS} && ${formDTO.isQA} && param.departmentName !='${caseRoleAttr.TMS.code}' && param.departmentName !='${caseRoleAttr.QA.code}') {
					$.messager.alert('提示訊息','TMS，QA只能處理派工給TMS或QA的案件','warning');
					return false;
					//是tms時
				} else if (${formDTO.isTMS} && ${!formDTO.isQA} && param.departmentName !='${caseRoleAttr.TMS.code}') {
					$.messager.alert('提示訊息','TMS，QA只能處理派工給TMS或QA的案件','warning');
					return false;
					//是QA時
				} else if (${formDTO.isQA} && ${!formDTO.isTMS} && param.departmentName !='${caseRoleAttr.QA.code}') {
					$.messager.alert('提示訊息','TMS，QA只能處理派工給TMS或QA的案件','warning');
					return false;
				}
			} else {
				if(param.caseStatus !='WaitDispatch'){
					//維護廠商不為登陸者公司
					if(param.companyId != '${logonUser.companyId}') {
						if((!${formDTO.isTMS}) && (!${formDTO.isQA})){
								return false;
						} 
						//既是tms又是QA時
						else if (${formDTO.isTMS} && ${formDTO.isQA} && param.departmentName !='${caseRoleAttr.TMS.code}' && param.departmentName !='${caseRoleAttr.QA.code}') {
							$.messager.alert('提示訊息','TMS，QA只能處理派工給TMS或QA的案件','warning');
							return false;
							//是tms時
						} else if (${formDTO.isTMS} && ${!formDTO.isQA} && param.departmentName !='${caseRoleAttr.TMS.code}') {
							$.messager.alert('提示訊息','TMS，QA只能處理派工給TMS或QA的案件','warning');
							return false;
							//是QA時
						} else if (${formDTO.isQA} && ${!formDTO.isTMS} && param.departmentName !='${caseRoleAttr.QA.code}') {
							$.messager.alert('提示訊息','TMS，QA只能處理派工給TMS或QA的案件','warning');
							return false;
						}
					} else {
						//維護廠商為登陸者公司
						if (param.departmentName =='${caseRoleAttr.TMS.code}' 
							|| param.departmentName =='${caseRoleAttr.QA.code}' 
							|| param.departmentName =='客服'){
							//$.messager.alert('提示訊息','TMS，QA只能處理派工給TMS或QA的案件','warning');
							if(param.departmentName =='${caseRoleAttr.TMS.code}' && ${!formDTO.isTMS}){
								return false;
							}
							if(param.departmentName =='${caseRoleAttr.QA.code}' && ${!formDTO.isQA}){
								return false;
							}
							if(param.departmentName =='客服'){
								return false;
							}
						}
					}
				} else {
					return false;
				}
			}
		}
		return true;
	} */
	function validateQaAndTms(param){
		//"dgResponse""dataGridResponse"
		//當前登錄者是客服時不走以下驗證 // CR #2951 廠商客服  					//Task #3578 客戶廠商客服
		if (${formDTO.isCustomerService} || ${formDTO.isVendorService} || ${formDTO.isCusVendorService}) {
			return true;
		} else {
			//CR #2394 增加cyberAgent update by 2017/09/13
			//不是VendorAgent 且 不是CyberAgent
			if((!${formDTO.isVendorAgent}) && (!${formDTO.isCyberAgent})){
				//既是tms又是QA時
				if (${formDTO.isTMS} && ${formDTO.isQA} && param.departmentName !='${caseRoleAttr.TMS.code}' && param.departmentName !='${caseRoleAttr.QA.code}') {
				//	$.messager.alert('提示訊息','TMS，QA只能處理派工給TMS或QA的案件','warning');
					$.messager.alert('提示訊息',"來自"+param.caseId+"訊息："+'此案件已派工至其他角色，僅可新增記錄','warning');
					return false;
					//是tms時
				} else if (${formDTO.isTMS} && ${!formDTO.isQA} && param.departmentName !='${caseRoleAttr.TMS.code}') {
				//	$.messager.alert('提示訊息','TMS，QA只能處理派工給TMS或QA的案件','warning');
					$.messager.alert('提示訊息',"來自"+param.caseId+"訊息："+'此案件已派工至其他角色，僅可新增記錄','warning');
					return false;
					//是QA時
				} else if (${formDTO.isQA} && ${!formDTO.isTMS} && param.departmentName !='${caseRoleAttr.QA.code}') {
				//	$.messager.alert('提示訊息','TMS，QA只能處理派工給TMS或QA的案件','warning');
					$.messager.alert('提示訊息',"來自"+param.caseId+"訊息："+'此案件已派工至其他角色，僅可新增記錄','warning');
					return false;
				}
			} else {
				if(param.caseStatus !='WaitDispatch'){
					//維護廠商不為登陸者公司
					if(param.companyId != '${logonUser.companyId}') {
						if((!${formDTO.isTMS}) && (!${formDTO.isQA})){
							$.messager.alert('提示訊息',"來自"+param.caseId+"訊息："+'此案件已派工至其他角色，僅可新增記錄','warning');
								return false;
						} 
						//既是tms又是QA時
						else if (${formDTO.isTMS} && ${formDTO.isQA} && param.departmentName !='${caseRoleAttr.TMS.code}' && param.departmentName !='${caseRoleAttr.QA.code}') {
						//	$.messager.alert('提示訊息','TMS，QA只能處理派工給TMS或QA的案件','warning');
							$.messager.alert('提示訊息',"來自"+param.caseId+"訊息："+'此案件已派工至其他角色，僅可新增記錄','warning');
							return false;
							//是tms時
						} else if (${formDTO.isTMS} && ${!formDTO.isQA} && param.departmentName !='${caseRoleAttr.TMS.code}') {
						//	$.messager.alert('提示訊息','TMS，QA只能處理派工給TMS或QA的案件','warning');
							$.messager.alert('提示訊息',"來自"+param.caseId+"訊息："+'此案件已派工至其他角色，僅可新增記錄','warning');
							return false;
							//是QA時
						} else if (${formDTO.isQA} && ${!formDTO.isTMS} && param.departmentName !='${caseRoleAttr.QA.code}') {
						//	$.messager.alert('提示訊息','TMS，QA只能處理派工給TMS或QA的案件','warning');
							$.messager.alert('提示訊息',"來自"+param.caseId+"訊息："+'此案件已派工至其他角色，僅可新增記錄','warning');
							return false;
						}
					} else {
						//維護廠商為登陸者公司
						if (param.departmentName =='${caseRoleAttr.TMS.code}' 
							|| param.departmentName =='${caseRoleAttr.QA.code}' 
							|| param.departmentName =='客服'){
							//$.messager.alert('提示訊息','TMS，QA只能處理派工給TMS或QA的案件','warning');
							if(param.departmentName =='${caseRoleAttr.TMS.code}' && ${!formDTO.isTMS}){
								$.messager.alert('提示訊息',"來自"+param.caseId+"訊息："+'此案件已派工至其他角色，僅可新增記錄','warning');
								return false;
							}
							if(param.departmentName =='${caseRoleAttr.QA.code}' && ${!formDTO.isQA}){
								$.messager.alert('提示訊息',"來自"+param.caseId+"訊息："+'此案件已派工至其他角色，僅可新增記錄','warning');
								return false;
							}
							if(param.departmentName =='客服'){
								$.messager.alert('提示訊息',"來自"+param.caseId+"訊息："+'此案件已派工至其他角色，僅可新增記錄','warning');
								return false;
							}
						}
					}
				} else {
					$.messager.alert('提示訊息',"來自"+param.caseId+"訊息："+'此案件已派工至其他角色，僅可新增記錄','warning');
					return false;
				}
			}
		}
		return true;
	}
	/*
	*自動派工作業
	*/
	function autoDispatchingAction(datagridId, flag){
		// 關閉案件動作datagrid
		// 之前設定耗材的grid節點
		if (flag) {
			$('#dgResponse').closest('.easyui-panel').panel('close');
			collapseSubView('${contextPath}');
			addCaseLogExpand('${contextPath}');
		} else {
			$('#responsePanel').closest('.easyui-panel').panel('close');
			$("#dataGridAssetLink").closest('.panel').attr("style","display:none;");
			$("#dataGridSuppliesLink").closest('.panel').attr("style","display:none;");
		}
		
		var tempCaseStatus = null;
		if($('#uploadCaeInfo').hasClass("easyui-panel")){
			$('#uploadCaeInfo').panel('close');
		} else {
			$("#uploadCaeInfo").addClass("easyui-panel");
			$("#uploadCaeInfo").attr("style", "width: 98%; height: auto;");
			$("#uploadCaeInfo").panel();
		}
		//Task #3269
		if($('#uploadCaseEnd').hasClass("easyui-panel")){
			$('#uploadCaseEnd').panel('close');
		}
	//	$('#uploadCaeInfo').panel('close');
	//	$("#caseCategory").combobox("setValue", "");
		if($('#caseCategory').hasClass("easyui-combobox")){
			$("#caseCategory").combobox("setValue", "");
		}
		$(".qq-upload-list-selector").html("");
		$("#" + datagridId +  '-msg').text("");
		//外邊的自動派工
		if(flag){
			if(validateSelectCase('autoDispatching',datagridId)){
				var rows = $("#dg").datagrid('getSelections');
				tempCaseStatus = rows[0].caseStatus;
				$.messager.confirm('確認對話框','確認自動派工？', function(isDispatch) {
					if (isDispatch) {
						waitDispatchAndAutoDispatchAction(tempCaseStatus, datagridId, 'autoDispatching');
					}
				});
			}
		} else {
			// 得到案件編號
			var caseId = $("#hideCaseId").val();
			var departmentName = null;
			var companyId = null;
			javascript:dwr.engine.setAsync(false);
			// 處理頁面使用當前的案件編號查詢出來的案件狀態
			ajaxService.getCaseInfoById(caseId, function(data){
				if(data){
					tempCaseStatus = data.caseStatus;
					departmentName = data.dispatchDeptName;
					companyName = data.companyName;
				}
			});
			javascript:dwr.engine.setAsync(true);
			var param = {
					departmentName : departmentName,
					companyId : companyId,
					caseStatus :tempCaseStatus,	
			};
			if(validateQaAndTms(param)){
				if(tempCaseStatus != null){
					$.messager.confirm('確認對話框','確認自動派工？', function(isDispatch) {
						if (isDispatch) {
							waitDispatchAndAutoDispatchAction(tempCaseStatus, datagridId, 'autoDispatching', caseId);
						}
					});
				}
			}
		}	
	}
	/*
	* 驗證案件狀態等
	* actionId：案件動作 datagridId：當前datagridid
	*/
	function validateSelectCase(actionId, datagridId){
		var rows = $("#dg").datagrid('getSelections');
		if(!rows || rows == null ||rows.length == 0){
			$.messager.alert('提示訊息','請勾選資料','warning');
			return false; 
		} else {
			//第一個案件類別
			var tempCaseCategory = rows[0].caseCategory;
			var tempCaseStatus = rows[0].caseStatus;
			//修改 報修結案預設 抓第一條的問題 2017/11/14
			var responsibityVal = rows[0].responsibity;
			var problemSolutionVal = rows[0].problemSolution;
			var problemReasonVal = rows[0].problemReason;
			var noTidIndex = "案件編號";
			var tempCustomerId = rows[0].customerId;
			for (var i = 0; i<rows.length; i++) {
				if ((tempCaseCategory != rows[i].caseCategory) || (tempCaseStatus != rows[i].caseStatus)) {
					$.messager.alert('提示訊息','請選擇同一案件狀態、案件類別的案件','warning');
					return false;
				}
				if((tempCaseCategory == '${caseCategoryAttr.REPAIR.code }') 
							&& (actionId == '${caseActionAttr.SIGN.code }' || actionId == '${caseActionAttr.ONLINE_EXCLUSION.code }')){
					if((tempCustomerId != rows[i].customerId) && (tempCustomerId=='${formDTO.tsbCustomerId}' || rows[i].customerId=='${formDTO.tsbCustomerId}')){
						$.messager.alert('提示訊息','台新報修件，問題原因與解決方式選項與其他客戶不同，無法一同處理','warning');
						return false;
					}
				}
				// Task #3070 案件管理 TMS等可新增記錄
				/* if((actionId == '${caseActionAttr.DISPATCHING.code }') || (actionId =='${caseActionAttr.AUTO_DISPATCHING.code }')){
					if(!validateQaAndTms(rows[i])){
						return false;
					}
				} */
				if(actionId != '${caseActionAttr.ADD_RECORD.code }'){
					if(!validateQaAndTms(rows[i])){
						return false;
					}
				}
				
				// Task #2721 拆機 查核 專案 報修 這幾種類別，不需要檢核
				if(tempCaseCategory == '${caseCategoryAttr.INSTALL.code }' || tempCaseCategory == '${caseCategoryAttr.MERGE.code }'
						|| tempCaseCategory == '${caseCategoryAttr.UPDATE.code }'){
					if (actionId == '${caseActionAttr.DISPATCHING.code }' || actionId == '${caseActionAttr.AUTO_DISPATCHING.code }'
						|| actionId == '${caseActionAttr.RESPONSE.code }' || actionId == '${caseActionAttr.ONLINE_EXCLUSION.code }' 
						|| actionId == '${caseActionAttr.IMMEDIATELY_CLOSING.code }'){
						if (rows[i].tid == '' || rows[i].tid == null) {
							noTidIndex = noTidIndex + rows[i].caseId + ",";
						}
					}
				}
				//Bug #2276    修改 報修結案預設 只抓第一條的問題  當 問題原因、問題解決方式、責任歸屬 不一致時 提示消息   2017/11/14
				if(actionId == '${caseActionAttr.CLOSED.code }' && tempCaseCategory == '${caseCategoryAttr.REPAIR.code }'){
					if(responsibityVal != rows[i].responsibity
							|| problemSolutionVal != rows[i].problemSolution
							|| problemReasonVal != rows[i].problemReason){
						$.messager.alert('提示訊息','請選擇同一問題原因、問題解決方式、責任歸屬的案件','warning');
						return false;
					}
				}
			}
			if (noTidIndex.length > 4) {
				$.messager.alert('提示訊息', noTidIndex.substring("0", noTidIndex.length-1) + '，請設定交易參數', 'warning');
				return false;
			}
			// 自動派工動作
			if(actionId == '${caseActionAttr.AUTO_DISPATCHING.code }'){
				// 若勾選資料之案件狀態為 “待結案審查、已作廢、立即結案、結案”，則顯示訊息「案件狀態不符，不可進行派工」
				if(tempCaseStatus == '${caseStatusAttr.WAIT_CLOSE.code }' 
						|| tempCaseStatus == '${caseStatusAttr.VOIDED.code }'
						|| tempCaseStatus == '${caseStatusAttr.IMMEDIATE_CLOSE.code }'
						|| tempCaseStatus == '${caseStatusAttr.CLOSED.code }') {
					$.messager.alert('提示訊息','案件狀態不符，不可進行派工','warning');
					return false;
				}/*  else  if (actionId == '${caseActionAttr.AUTO_DISPATCHING.code }') {
					$.messager.confirm('確認對話框','確認自動派工？', function(isDispatch) {
						if (isDispatch) {
							waitDispatchAndAutoDispatchAction(tempCaseStatus, datagridId, actionId);
						}
					});
					return false;
				} */
				// 派工動作
			} else if(actionId == '${caseActionAttr.DISPATCHING.code }'){
				// 若勾選資料之案件狀態為 “待結案審查、已作廢、立即結案、結案”，則顯示訊息「案件狀態不符，不可進行派工」
				if(tempCaseStatus == '${caseStatusAttr.WAIT_CLOSE.code }' 
						|| tempCaseStatus == '${caseStatusAttr.VOIDED.code }'
						|| tempCaseStatus == '${caseStatusAttr.IMMEDIATE_CLOSE.code }'
						|| tempCaseStatus == '${caseStatusAttr.CLOSED.code }') {
					$.messager.alert('提示訊息','案件狀態不符，不可進行派工','warning');
					return false;
				} else if (tempCaseStatus == '${caseStatusAttr.WAIT_DISPATCH.code }'
						|| tempCaseStatus == '${caseStatusAttr.RESPONSED.code }') {
					var tempRow = checkConfirmAuthorizes(rows);
					if (tempRow != null && tempRow != '') {
						$.messager.alert('提示訊息',"來自"+tempRow.caseId+"訊息："+'尚未授權確認，不可派工','warning');
						return false;
					}
					if (tempCaseStatus == '${caseStatusAttr.WAIT_DISPATCH.code }') {
						$.messager.confirm('確認對話框','確認派工？', function(isDispatch) {
							if (isDispatch) {
								waitDispatchAndAutoDispatchAction(tempCaseStatus, datagridId, actionId);
							}
						});
						return false;
					}
					
				}
				// 新增記錄動作
			} else if(actionId == '${caseActionAttr.ADD_RECORD.code }'){
				// 若案件狀態為“待派工、已作廢、立即結案、結案”，則顯示訊息「案件狀態不符，不可進行新增記錄」
				// Task #2559 立即結案、結案 可新增記錄
				if(tempCaseStatus == '${caseStatusAttr.WAIT_DISPATCH.code }' 
						|| tempCaseStatus == '${caseStatusAttr.VOIDED.code }') {
					$.messager.alert('提示訊息','案件狀態不符，不可進行新增記錄','warning');
					return false;
				}
				// 簽收動作
			} else if(actionId == '${caseActionAttr.SIGN.code }') {
				//若案件狀態不為“完修”，則顯示訊息「案件狀態不為完修，不可進行簽收」
				if(tempCaseStatus != '${caseStatusAttr.COMPLETED.code }'){
					$.messager.alert('提示訊息','案件狀態不為完修，不可進行簽收','warning');
					return false;
				//CR #2551 
				} else if(!validateRepeatCaseinfo(rows)){
					return false;
				//CR #2551 
				} else if(!validateCaseChangeAsset(rows)){
					return false;
				}
			}else if(actionId == '${caseActionAttr.RESPONSE.code }'){
				//若已回應過，不可再回應，不為“待派工”或“已派工” ,則顯示訊息「案件狀態不符，不可進行回應」
				if(tempCaseStatus != '${caseStatusAttr.WAIT_DISPATCH.code }'&& 
								tempCaseStatus != '${caseStatusAttr.DISPATCHED.code }'){
					$.messager.alert('提示訊息','案件狀態不符，不可進行回應','warning');
					return false;
				}
				// 到場動作
			} else if(actionId == '${caseActionAttr.ARRIVE.code }'){
				//若案件狀態不為“已回應”或“已到場” 或“延期中” 或“完修”，則顯示訊息「案件狀態不符，不可進行到場」
				if(tempCaseStatus != '${caseStatusAttr.RESPONSED.code }'
						&& tempCaseStatus != '${caseStatusAttr.ARRIVED.code }'
						&& tempCaseStatus != '${caseStatusAttr.DELAYING.code }'
						&& tempCaseStatus != '${caseStatusAttr.COMPLETED.code }'){
					$.messager.alert('提示訊息','案件狀態不符，不可進行到場','warning');
					return false;
				}
			} else if(actionId == '${caseActionAttr.RETREAT.code }'){
				//若案件狀態不為待結案審查，則顯示訊息「案件狀態為待結案審查，才可進行退回(完修)」
				/* if(tempCaseStatus != '${caseStatusAttr.WAIT_CLOSE.code }'){
					$.messager.alert('提示訊息','案件狀態為待結案審查，才可進行退回(完修)','warning');
					return false;
				} */
				// Task #3113 完修退回至客服
				if((tempCaseStatus != '${caseStatusAttr.WAIT_CLOSE.code }')
						&& (tempCaseStatus != '${caseStatusAttr.COMPLETED.code }')){
					$.messager.alert('提示訊息','案件狀態不為待結案審查、完修，不可進行退回','warning');
					return false;
				}
				// Task #3113 拆機件簽收、線上排除后不可完修退回
				if((tempCaseStatus == '${caseStatusAttr.COMPLETED.code }') 
						&& (tempCaseCategory == '${caseCategoryAttr.UNINSTALL.code }')){
					for (var i = 0; i<rows.length; i++) {
						javascript:dwr.engine.setAsync(false);
						var breakFlag = false;
						ajaxService.getCaseInfoById(rows[i].caseId, function(data){
							if(data){
								// Task #3113 拆機件簽收、線上排除后不可完修退回
								if((data.caseStatus == '${caseStatusAttr.COMPLETED.code }') && (data.hasRetreat == 'Y')){
									$.messager.alert('提示訊息',"來自"+rows[i].caseId+"訊息： "+'拆機案件已經過簽收或線上排除，不可進行退回','warning');
									breakFlag = true;
								}
							}
						});
						javascript:dwr.engine.setAsync(true);
						if(breakFlag){
							return false;
						};
					}
				}
				//結案動作
			} else if(actionId == '${caseActionAttr.CLOSED.code}'){
				//若案件狀態不為"待結案審查"，則顯示訊息「案件狀態為待結案審查，才可進行結案」。
				if(tempCaseStatus != '${caseStatusAttr.WAIT_CLOSE.code }'){
					$.messager.alert('提示訊息','案件狀態為待結案審查，才可進行結案','warning');
					return false;
				}
				//延期動作
			} else if(actionId == '${caseActionAttr.DELAY.code }'){
				//若案件狀態不為“已回應”或“已到場”，則顯示訊息「案件狀態不符，不可進行延期」。//Task #3123 延期也可以延期
				if(tempCaseStatus != '${caseStatusAttr.RESPONSED.code }' && tempCaseStatus != '${caseStatusAttr.ARRIVED.code }' && tempCaseStatus != '${caseStatusAttr.DELAYING.code }'){
					$.messager.alert('提示訊息','案件狀態不符，不可進行延期','warning');
					return false;
				}
				// 線上排除動作+完修+立即結案
			} else if((actionId == '${caseActionAttr.ONLINE_EXCLUSION.code }') || (actionId == '${caseActionAttr.COMPLETE.code }')
				 || (actionId == '${caseActionAttr.IMMEDIATELY_CLOSING.code }')){
				// 線上排除
				if((actionId == '${caseActionAttr.ONLINE_EXCLUSION.code }')){
					//Task #3358 當leaseSignFlag=Y 時 為 租賃簽收
					if($("#hideLeaseSignFlag").val()=='Y' && tempCaseStatus != '${caseStatusAttr.DISPATCHED.code }' && tempCaseStatus != '${caseStatusAttr.RESPONSED.code }'){
						$("#hideLeaseSignFlag").val('N');
						$.messager.alert('提示訊息','案件狀態不符，不可進行租賃簽收','warning');
						return false;
					}
					// 若勾選資料之案件狀態為“待結案審查、已作廢、立即結案、結案”，則顯示訊息「案件狀態不符，不可進行線上排除」
					// Task #3205 案件僅能完修一次，即完修後不能線上排除
					if(tempCaseStatus == '${caseStatusAttr.WAIT_CLOSE.code }' || tempCaseStatus == '${caseStatusAttr.VOIDED.code }'
						|| tempCaseStatus == '${caseStatusAttr.IMMEDIATE_CLOSE.code }' || tempCaseStatus == '${caseStatusAttr.CLOSED.code }'
						|| tempCaseStatus == '${caseStatusAttr.COMPLETED.code }'){
						$.messager.alert('提示訊息','案件狀態不符，不可進行線上排除','warning');
						return false;
					} else if(!validateChangeInstallDtid(rows)){
						return false;
					} 
					// 完修
				} else if (actionId == '${caseActionAttr.COMPLETE.code }'){
					// 若案件狀態不為 “已到場”，則顯示訊息「案件狀態不為已到場，不可進行完修」
					if(tempCaseStatus != '${caseStatusAttr.ARRIVED.code }') {
						$.messager.alert('提示訊息','案件狀態不為已到場，不可進行完修','warning');
						return false;
					} else if(!validateChangeInstallDtid(rows)){
						return false;
					} 
					
				//立即結案
				} else if(actionId == '${caseActionAttr.IMMEDIATELY_CLOSING.code }'){
					
					//若案件狀態不為“待派工”或目前處理人員不為客服，則顯示訊息“案件狀態不符，不可進行立即結案”
					/* if((tempCaseStatus != '${caseStatusAttr.WAIT_DISPATCH.code }') || (!${isCustomerService})){
						$.messager.alert('提示訊息','案件狀態不符，不可進行立即結案','warning');
						return false;
					} */
					// Bug #2331 若案件狀態不為“待派工”或目前處理人員不為客服，則顯示訊息“案件狀態不符，不可進行立即結案”
					// 是客服 // CR #2951 廠商客服    //Task #3578 客戶廠商客服
					if(${isCustomerService or isVendorService or isCusVendorService}){
						// 不爲待派工、已派、已回應、已到場、延期
						if((tempCaseStatus != '${caseStatusAttr.WAIT_DISPATCH.code }') && (tempCaseStatus != '${caseStatusAttr.DISPATCHED.code }')
								&& (tempCaseStatus != '${caseStatusAttr.RESPONSED.code }') && (tempCaseStatus != '${caseStatusAttr.ARRIVED.code }')
								&& (tempCaseStatus != '${caseStatusAttr.DELAYING.code }')){
							$.messager.alert('提示訊息','案件狀態不符，不可進行協調完成','warning');
							return false;
						}
					// 不是客服
					} else {
						// 不爲待派工
						if(tempCaseStatus != '${caseStatusAttr.WAIT_DISPATCH.code }'){
							$.messager.alert('提示訊息','案件狀態不符，不可進行協調完成','warning');
							return false;
						}
					}
					if(!validateChangeInstallDtid(rows)){
						return false;
					} 
				}
				//若重複進件(案件類別=倂機、異動、拆機、專案、報修)，則需將之前重複進件的案件結案，
				//才可簽收，顯示訊息「來自{當前案件編號}訊息：請先將{之前重複進件的案件編號}結案」
				var dtid;
				var caseId = "";
				for (var i = 0; i<rows.length; i++) {
					if(caseId == "") {
						caseId = rows[i].caseId;
					} else {
						caseId = caseId + "," + rows[i].caseId;
					}
				}
				if(actionId != '${caseActionAttr.COMPLETE.code }'){
					/* //驗證重複進建
					for (var i = 0; i<rows.length; i++) {
						if ((rows[i].caseCategory == '${caseCategoryAttr.MERGE.code }') 
								|| (rows[i].caseCategory == '${caseCategoryAttr.UPDATE.code }')
								|| (rows[i].caseCategory == '${caseCategoryAttr.UNINSTALL.code }')
								|| (rows[i].caseCategory == '${caseCategoryAttr.PROJECT.code }')
								|| (rows[i].caseCategory == '${caseCategoryAttr.REPAIR.code }')) {
							javascript:dwr.engine.setAsync(false);
							var flag = false;
							ajaxService.getCaseRepeatList(rows[i].dtid, rows[i].caseId, function(data){
								if(data.flag){
									for(var key in data) { 
										if(key != "flag"){
											$.messager.alert('提示訊息',"來自"+key+"訊息：請先將 "+ data[key] +"結案",'');
											flag = true;
											break;
										}
									} 
								}
							})
							javascript:dwr.engine.setAsync(true);
							if(flag){
								return false;
							}
						}
					} */
					//驗證重複進建
					if(!validateRepeatCaseinfo(rows)){
						return false;
					}
				} else {
					//CR #2551   驗證重複進建 當前一筆為裝機時，前案件必須完修  2017/12/07
					for (var i = 0; i<rows.length; i++) {
						if (rows[i].caseCategory != '${caseCategoryAttr.INSTALL.code }' && rows[i].caseCategory != '${caseCategoryAttr.OTHER.code }') {
							javascript:dwr.engine.setAsync(false);
							var flag = false;
							ajaxService.getCaseRepeatByInstallUncomplete(rows[i].dtid,rows[i].caseId, function(data){
								if(data.flag){
									if(data.caseId != null && data.caseId != ''){
										$.messager.alert('提示訊息',"來自"+rows[i].caseId+"訊息：請客服先將 "+ data.caseId +"結案<br>若已結案，因缺少設備資料，請聯繫系統管理員。",'');
										flag = true;
										javascript:dwr.engine.setAsync(true);
										return false;
									}
								}
							})
							javascript:dwr.engine.setAsync(true);
							if(flag){
								return false;
							}
						}
					}
				}
				//CR #2551 
				if(actionId != '${caseActionAttr.COMPLETE.code }'){
					//驗證該案件設備是否被之前同dtid的案件修改過  
					/* for (var i = 0; i<rows.length; i++) {
						if ((rows[i].caseCategory == '${caseCategoryAttr.MERGE.code }') 
								|| (rows[i].caseCategory == '${caseCategoryAttr.UPDATE.code }')
								|| (rows[i].caseCategory == '${caseCategoryAttr.UNINSTALL.code }')
								|| (rows[i].caseCategory == '${caseCategoryAttr.PROJECT.code }')
								|| (rows[i].caseCategory == '${caseCategoryAttr.REPAIR.code }')) {
							javascript:dwr.engine.setAsync(false);
							var flag = false;
							ajaxService.getCaseLinkIsChange(rows[i].dtid, rows[i].caseId, function(data){
								if(data.flag){
									if (${formDTO.isCustomerService}) {
										$.messager.alert('提示訊息',"來自"+rows[i].caseId+"訊息： "+ data["caseId"] +"於"
										+ data["closeDate"] +"已更新案件最新設備連接資料，請至處理頁面點✔重新帶入最新資料",'');
									} else {
										//CR #2551 3. 設備資料 與 最新資料檔 不一致，提醒USER 聯繫客服 2017/12/07
										// CR #2951 廠商客服
										if (${formDTO.isVendorService }) {
											$.messager.alert('提示訊息',"來自"+caseId+"訊息： "+ data["caseId"] +"於"
													+ data["closeDate"] +"異動設備與此案件不符，請聯繫Cyber客服確認",'');
										} else {
											$.messager.alert('提示訊息',"來自"+caseId+"訊息： "+ data["caseId"] +"於"
													+ data["closeDate"] +"異動設備與此案件不符，請聯繫客服確認",'');
										}
									}
									flag = true;
								}
							});
							javascript:dwr.engine.setAsync(true);
							if(flag){
								return false;
							};
						}
					} */
					//驗證該案件設備是否被之前同dtid的案件修改過
					if(!validateCaseChangeAsset(rows)){
						return false;
					}
				}
				
				// Bug #2331 若設備異動則不能立即結案
				if(actionId == '${caseActionAttr.IMMEDIATELY_CLOSING.code }'){ //Task #3578 客戶廠商客服
					if(${isCustomerService or isVendorService or isCusVendorService }){
						for (var i = 0; i<rows.length; i++) {
							javascript:dwr.engine.setAsync(false);
							var breakFlag = false;
							ajaxService.getCaseInfoById(rows[i].caseId, function(data){
								if(data){
									//bug 2331 reopen 修改為派工回客服則可處理
									if((data.caseStatus != '${caseStatusAttr.WAIT_DISPATCH.code }')
											&& (data.dispatchDeptId != 'CUSTOMER_SERVICE')){
										$.messager.alert('提示訊息','案件狀態不符，不可進行協調完成','warning');
										breakFlag = true;
									} else {
										// Task #3113 完修可以退回客服 簽收設備后不能作廢
										if(data.hasRetreat == 'Y'){
											$.messager.alert('提示訊息',"來自"+rows[i].caseId+"訊息： "+'此案件已經過簽收或線上排除，不可進行協調完成','warning');
											breakFlag = true;
										}
									}
								}
							});
							javascript:dwr.engine.setAsync(true);
							if(breakFlag){
								return false;
							};
						}
					}
					if(tempCaseCategory != '${caseCategoryAttr.INSTALL.code }' && tempCaseCategory != '${caseCategoryAttr.OTHER.code }'){
						//驗證該案件設備是否被之前同dtid的案件修改過
						for (var i = 0; i<rows.length; i++) {
							javascript:dwr.engine.setAsync(false);
							var flag = false;
							ajaxService.isChangeAsset(rows[i].caseId, function(data){
								if(data){
									$.messager.alert('提示訊息',"來自"+rows[i].caseId+"訊息： "+"案件設備有異動，不可進行協調完成",'warning');
									flag = true;
								}
							});
							javascript:dwr.engine.setAsync(true);
							if(flag){
								return false;
							};
						}
					}
				}
			} else if(actionId == '${caseActionAttr.CHANGE_CASE_TYPE.code }'){
				//若案件狀態不為“待派工”、“已派工”、“已回應”、“已到場”、“延期中”，則顯示訊息「案件狀態不符，不可進行案件類型修改」
				// Task #3227 結案后可修改案件類型
				if(tempCaseStatus != '${caseStatusAttr.WAIT_DISPATCH.code }'
						&& tempCaseStatus != '${caseStatusAttr.DISPATCHED.code }'
						&& tempCaseStatus != '${caseStatusAttr.RESPONSED.code }'
						&& tempCaseStatus != '${caseStatusAttr.ARRIVED.code }'
						&& tempCaseStatus != '${caseStatusAttr.DELAYING.code }'
						&& tempCaseStatus != '${caseStatusAttr.CLOSED.code }'
						&& tempCaseStatus != '${caseStatusAttr.IMMEDIATE_CLOSE.code }'){
					$.messager.alert('提示訊息','案件狀態不符，不可進行案件類型修改','warning');
					return false;
				}
			} else if (actionId == '${caseActionAttr.RUSH_REPAIR.code }') {
				//若案件狀態不為"待派工”、“已派工”、“已回應”、“已到場”、“延期中”，則顯示訊息「案件狀態不符，不可進行案件類型修改」
				if(tempCaseStatus != '${caseStatusAttr.WAIT_DISPATCH.code }'
						&& tempCaseStatus != '${caseStatusAttr.DISPATCHED.code }'
						&& tempCaseStatus != '${caseStatusAttr.RESPONSED.code }'
						&& tempCaseStatus != '${caseStatusAttr.ARRIVED.code }'
						&& tempCaseStatus != '${caseStatusAttr.DELAYING.code }'){
					$.messager.alert('提示訊息','案件狀態不符，不可進行催修','warning');
					return false;
				}
			//作廢動作
			} else if (actionId == '${caseActionAttr.VOID_CASE.code }'){
				/* //若案件狀態不為“待派工”或目前處理人員不為客服，則顯示訊息“案件狀態不符，不可進行作廢”
				if((tempCaseStatus != '${caseStatusAttr.WAIT_DISPATCH.code }') || (!${isCustomerService})){
					$.messager.alert('提示訊息','案件狀態不符，不可進行作廢','warning');
					return false;
				} */
				// Bug #2331 若案件狀態不為“待派工”或目前處理人員不為客服，則顯示訊息“案件狀態不符，不可進行立即結案”
				// 是客服 // CR #2951 廠商客服   //Task #3578 客戶廠商客服
				if(${isCustomerService or isVendorService or isCusVendorService}){
					// 不爲待派工、已派、已回應、已到場、延期
					if((tempCaseStatus != '${caseStatusAttr.WAIT_DISPATCH.code }') && (tempCaseStatus != '${caseStatusAttr.DISPATCHED.code }')
							&& (tempCaseStatus != '${caseStatusAttr.RESPONSED.code }') && (tempCaseStatus != '${caseStatusAttr.ARRIVED.code }')
							&& (tempCaseStatus != '${caseStatusAttr.DELAYING.code }')){
						$.messager.alert('提示訊息','案件狀態不符，不可進行作廢','warning');
						return false;
					}
				// 不是客服
				} else {
					// 不爲待派工
					if(tempCaseStatus != '${caseStatusAttr.WAIT_DISPATCH.code }'){
						$.messager.alert('提示訊息','案件狀態不符，不可進行作廢','warning');
						return false;
					}
				}
				// CR #2951 廠商客服   //Task #3578 客戶廠商客服
				if(${isCustomerService or isVendorService or isCusVendorService}){
					for (var i = 0; i<rows.length; i++) {
						javascript:dwr.engine.setAsync(false);
						var breakFlag = false;
						ajaxService.getCaseInfoById(rows[i].caseId, function(data){
							if(data){
							//bug 2331 reopen 修改為派工回客服則可處理
								if((data.caseStatus != '${caseStatusAttr.WAIT_DISPATCH.code }')
										&& (data.dispatchDeptId != 'CUSTOMER_SERVICE')){
									$.messager.alert('提示訊息','案件狀態不符，不可進行作廢','warning');
									breakFlag = true;
								} else {
									// Task #3113 完修可以退回客服 簽收設備后不能作廢
									if(data.hasRetreat == 'Y'){
										$.messager.alert('提示訊息',"來自"+rows[i].caseId+"訊息： "+'此案件已經過簽收或線上排除，不可進行作廢','warning');
										breakFlag = true;
									}
								}
							}
						});
						javascript:dwr.engine.setAsync(true);
						if(breakFlag){
							return false;
						};
					}
				}
				//修改實際完修時間
			} else if (actionId == '${caseActionAttr.CHANGE_COMPLETE_DATE.code }'){
				if(tempCaseStatus != '${caseStatusAttr.CLOSED.code }' && tempCaseStatus != '${caseStatusAttr.IMMEDIATE_CLOSE.code }'){
					$.messager.alert('提示訊息','案件狀態不符，不可進行修改實際完修時間','warning');
					return false;
				}
				//修改進件時間
			} else if (actionId == '${caseActionAttr.CHANGE_CREATE_DATE.code }'){
				if(tempCaseStatus != '${caseStatusAttr.CLOSED.code }' && tempCaseStatus != '${caseStatusAttr.IMMEDIATE_CLOSE.code }'){
					$.messager.alert('提示訊息','案件狀態不符，不可進行修改進件時間','warning');
					return false;
				}
			}
		}
		return true;
	}
	/**
	*簽收檢核設備是否異動  CR #2551 
	*/
	function validateCaseChangeAsset(rows){
		var hasUpdateCase = false;
		var caseId = '';
		//驗證該案件設備是否被之前同dtid的案件修改過
		if (rows[0].caseCategory != '${caseCategoryAttr.INSTALL.code }' && rows[0].caseCategory != '${caseCategoryAttr.OTHER.code }') {
			for (var i = 0; i<rows.length; i++) {
				javascript:dwr.engine.setAsync(false);
				var flag = false;
				ajaxService.getCaseLinkIsChange(rows[i].dtid, rows[i].caseId, function(data){});
				ajaxService.getCaseLinkIsChange(rows[i].dtid, rows[i].caseId, function(data){
					if(data.flag){
						if(data.initEditCheckUpdate=='N'){   //Task #3578 客戶廠商客服
							if (${formDTO.isCustomerService or formDTO.isCusVendorService}) {
								$.messager.alert('提示訊息',"來自"+rows[i].caseId+"訊息： "+ data["caseId"] +"於"
										+ data["closeDate"] +"已更新案件最新設備連接資料，請至處理頁面點✔重新帶入最新資料",'');
							
							} else {
								//CR #2551 3. 設備資料 與 最新資料檔 不一致，提醒USER 聯繫客服 2017/12/07
								// CR #2951 廠商客服 
								if (${formDTO.isVendorService}) {
									if(rows[i].vendorServiceCustomer == '${logonUser.companyId }'){
										$.messager.alert('提示訊息',"來自"+rows[i].caseId+"訊息： "+ data["caseId"] +"於"
												+ data["closeDate"] +"已更新案件最新設備連接資料，請至處理頁面點✔重新帶入最新資料",'');
									} else {
										$.messager.alert('提示訊息',"來自"+rows[i].caseId+"訊息： "+ data["caseId"] +"於"
												+ data["closeDate"] +"異動設備與此案件不符，請聯繫Cyber客服確認",'');
									}
								} else {
									$.messager.alert('提示訊息',"來自"+rows[i].caseId+"訊息： "+ data["caseId"] +"於"
											+ data["closeDate"] +"異動設備與此案件不符，請聯繫客服確認",'');
								}
							}
						} else {
							$.messager.alert('提示訊息',"來自"+rows[i].caseId+"訊息： 本案件dtid之EDC設備已拆除，請退回客服作廢",'');
						}
						flag = true;
					} else {
						if(data.assetLinkIsChange){
							
							hasUpdateCase = true
						}
					}
				});
				javascript:dwr.engine.setAsync(true);
				if(flag){
					return false;
				};
			}
		}
		if(hasUpdateCase){
			var exportField = $("#exportField").val();
			var field = exportField.split(','); 
			//var row = null;
			for (var i = 0; i<rows.length; i++) {
			javascript:dwr.engine.setAsync(false);
				ajaxService.getChangeCaseInfoById(rows[i].caseId, function(data){
					if(data){
						var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
						//加遮罩
						$.blockUI(blockStyle);
						for (var j = 0; j<field.length; j++) {
							
							//設備序號peripheralsSerialNumber
							if(field[j]=='peripheralsSerialNumber'){
								rows[i].peripheralsSerialNumber = data.peripheralsSerialNumber;
							}
							else if(field[j]=='peripherals3SerialNumber'){
								rows[i].peripherals3SerialNumber = data.peripherals3SerialNumber;
							}
							else if(field[j]=='peripherals2SerialNumber'){
								rows[i].peripherals2SerialNumber = data.peripherals2SerialNumber;
							}
							else if(field[j]=='updatedDate'){
								rows[i].updatedDate = parseInt(data.updatedDate,10);
							}
							else if(field[j]=='updatedByName'){
								rows[i].updatedByName = data.updatedByName;
							}
							else if(field[j]=='edcSerialNumber'){
								rows[i].edcSerialNumber = data.edcSerialNumber;
							}
							else if(field[j]=='firstDescription'){
								rows[i].firstDescription = data.firstDescription;
							}
							else if(field[j]=='secondDescription'){
								rows[i].secondDescription = data.secondDescription;
							}
							else if(field[j]=='thirdDescription'){
								rows[i].thirdDescription = data.thirdDescription;
							}
							else if(field[j]=='enableDate'){
								rows[i].enableDate = data.enableDate;
							}
							else if(field[j]=='wareHouseName'){
								rows[i].wareHouseName = data.wareHouseName;
							}
							else if(field[j]=='peripheralsContract'){
								rows[i].peripheralsContract = data.peripheralsContract;
							}
							else if(field[j]=='peripherals2Contract'){
								rows[i].peripherals2Contract = data.peripherals2Contract;
							}
							else if(field[j]=='peripherals3Contract'){
								rows[i].peripherals3Contract = data.peripherals3Contract;
							}
							    
						}
						var index = $("#dg").datagrid('getRowIndex',rows[i]);
						//setTimeout(function () {
						
							$("#dg").datagrid('updateRow',   
							{
								index:index,
								row:rows[i]  
								
							});
							$('a[name=lbtEdit]').linkbutton({ plain: true, iconCls: 'icon-edit' });
							$('a[name=lbtShow]').linkbutton({ plain: true, iconCls: 'icon-search' });
							var selectedCHeckbox=$(".datagrid-row[datagrid-row-index=" + index + "] input[name='checkBtn']")[0];
							selectedCHeckbox.checked = true
						// 去除遮罩
						$.unblockUI();
					}
				});
			}
		} 
		javascript:dwr.engine.setAsync(true);
		return true;
	}
	/**
	*驗證裝機案是否改dtid
	*/
	function validateChangeInstallDtid(rows){
		javascript:dwr.engine.setAsync(false);
		//驗證重複進建
		for (var i = 0; i<rows.length; i++) {
			if (rows[i].caseCategory != '${caseCategoryAttr.INSTALL.code }' && rows[i].caseCategory != '${caseCategoryAttr.OTHER.code }') {
				javascript:dwr.engine.setAsync(false);
				var flag = false;
				ajaxService.getCountByInstall(rows[i].dtid, rows[i].caseId, function(data){
					if(data=='Y'){
						$.messager.alert('提示訊息',"來自"+rows[i].caseId+"訊息：DTID之裝機案件不存在，請派工客服作廢",'');
						flag = true;
					}
				})
				javascript:dwr.engine.setAsync(true);
				if(flag){
					return false;
				}
			}
		}
		javascript:dwr.engine.setAsync(true);
		return true;
	}
	/**
	*驗證是否重複進件 
	*/
	function validateRepeatCaseinfo(rows){
		javascript:dwr.engine.setAsync(false);
		//驗證重複進建
		if (rows[0].caseCategory != '${caseCategoryAttr.OTHER.code }') {
			for (var i = 0; i<rows.length; i++) {
			//Bug #3067
				javascript:dwr.engine.setAsync(false);
				var flag = false;
				ajaxService.getCaseRepeatList(rows[i].dtid, rows[i].caseId, true, function(data){
					if(data.flag){
						for(var key in data) { 
							if(key != "flag"){
								$.messager.alert('提示訊息',"來自"+key+"訊息：請客服先將 "+ data[key] +"結案",'');
								flag = true;
								break;
							}
						} 
					}
				});
				javascript:dwr.engine.setAsync(true);
				if(flag){
					return false;
				}
			}
		}
		javascript:dwr.engine.setAsync(true);
		return true;
	}
	/**
	*待派工案件直接派工與自動派工
	*/
	function waitDispatchAndAutoDispatchAction(caseStatus, datagridId, actionId, caseId) {
		var updatedDateString = '';
		var caseIds = "";
		if (caseId != null && caseId != '') {
			caseIds = caseId;
			updatedDateString = $("#hideUpdateDate").val()+";"+caseId;
		} else {
			var selectRow = $("#dg").datagrid('getSelections');
			for (var i = 0; i<selectRow.length; i++) {
				if(selectRow.length >=1 && i == selectRow.length -1 ){
					updatedDateString = updatedDateString + selectRow[i].updatedDate+";"+selectRow[i].caseId;
					caseIds = caseIds + selectRow[i].caseId;
				} else {
					updatedDateString = updatedDateString + selectRow[i].updatedDate+";"+selectRow[i].caseId + ",";
					caseIds = caseIds + selectRow[i].caseId + ",";
				}
			}
		}
		var saveParam = {
				caseId : caseIds,
				caseStatus : caseStatus,
				caseActionId : actionId,
				updatedDateString : updatedDateString,
				nowCaseStatus : caseStatus,
				toMail:'被指派的廠商;被指派的廠商部門;被指派的工程師;被指派的角色'
		}
		var saveParamJson = JSON.stringify(saveParam);
		
		var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		if(caseId){
			// 調保存遮罩
			commonSaveLoading('editDlg');
		} else {
			//加遮罩
			$.blockUI(blockStyle);
		}
		$.ajax({
			url : "${contextPath}/caseHandle.do?actionId="+actionId,
			data : saveParam,
			type : 'post',
			cache : false, 
			dataType :'json',
			success : function(json) {
				var pageIndex = getGridCurrentPagerIndex("dg");
				query(pageIndex, false);
				//$("#dg").datagrid("clearSelections");
				var msg;
				if (json.success) {
					//成功提示
					//成功提示
					if (json.cmsResult != undefined && !json.cmsResult) {
						$.messager.alert('提示訊息',json.msg,'');
					} else {
						$("#" + (datagridId + '-msg')).text(json.msg);
					}
					if(datagridId == 'dataGridResponse'){
						// 處理按鈕顯示
						dealButtonShow(caseStatus, caseId);
						// 處理控件顯示
						dealDisabledControl(caseStatus);
						// 查案件記錄
					//	queryCaseRecord();
						queryCaseRecord('N', true);
						// 處理頁面修改時更新案件相關欄位
		    			updateCase(caseId, actionId);
					}
				} else {
					//$('#' + datagridId).datagrid('deleteRow', 0);  
					//失敗提示
					//msg = '派工失敗';
					$("#" + (datagridId + '-msg')).text(json.msg);
					//$('#' + datagridId).closest('.easyui-panel').panel('close');
					if(caseId){
						// 調取消保存遮罩
						commonCancelSaveLoading('editDlg');
					} else {
						// 去除遮罩
						$.unblockUI();
					}
				}
			},
			error : function(){
				$('#' + datagridId).text("頁面檢核失敗");
				if(caseId){
					// 調取消保存遮罩
					commonCancelSaveLoading('editDlg');
				} else {
					// 去除遮罩
					$.unblockUI();
				}
			}	
		});
	}
	
	
	/**
	*	TMS參數匯入
	*	單筆匯入
	*	author: tonychen
	*/
// 	function tmsprocess(caseId) {
// 		var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
//  		javascript:dwr.engine.setAsync(false);	
//  		$.blockUI(blockStyle);  
			
// 		ajaxService.createBatchNum(function(data){	
//  				if(data){
//  					batchNum = data;	
//  				}
//  			}); 
//  		if(batchNum.length == 0){
//  		   $.unblockUI();	
//  		   $.messager.alert("匯入失敗","TMS Batch Number 取號失敗,\r\n請重新執行",'');
//  		   javascript:dwr.engine.setAsync(true);
//  		   return;
 			
//  		}	
//  		//javascript:dwr.engine.setAsync(true);		
// 		var dtid = $("#case_showDtid").val();	
// 		var customerId = $("#case_customer").val();
// 		if(customerId != ''){
// 			customerId = $("#case_customer").find("option:selected").text();
// 		}
// 		var caseCategory = "裝機"; 
// 		var installType = $("#case_installType").val();
// 		if(installType != ''){
// 			installType = $("#case_installType").find("option:selected").text();
// 		}
// 		var merchantHeaderId = $("#case_merchantHeaderId").val();
// 		if(merchantHeaderId != ''){
// 			merchantHeaderId = $("#case_merchantHeaderId").find("option:selected").text();
// 		}
// 		var installedAdressLocation = $("#case_installedAdressLocation").val();
// 		if(installedAdressLocation != ''){
// 			installedAdressLocation = $("#case_installedAdressLocation").find("option:selected").text();
// 		} 
// 		var installedAdress = $("#case_merInstallAddress").val(); 
// 		var isOpenEncrypt = $("#case_isOpenEncrypt").val();
// 		var electronicPayPlatform = $("#case_electronicPayPlatform").val();
// 		var edcType = $("#case_edcType").val();
// 		if(edcType != ''){
// 			edcType = $("#case_edcType").find("option:selected").text();
// 		}  
// 		var softwareVersion = $("#case_softwareVersion").val();
// 		if(softwareVersion != ''){
// 			softwareVersion = $("#case_softwareVersion").find("option:selected").text();
// 		}   
// 		var connectiontype = $("#case_connectionType").val();
// 		if(connectiontype != ''){
// 			var connectiontype = $("#case_connectionType").combobox("getText");
// 		}

// 		var ecrConnection = $("#case_ecrConnection").val();//ECR連線 (noEcrLine)
// 		var electronicInvoice = $("#case_electronicInvoice").val();//電子發票載具 ("")
// 		var netVendorId = $("#case_netVendorId").val();//寬頻連線  ("")
// 		if(netVendorId != ''){
// 			netVendorId = $("#case_netVendorId").find("option:selected").text();
// 		}
// 		var builtInFeature = $("#case_builtInFeature").val();//內建功能  ("")
// 		if(builtInFeature != ''){
// 			var builtInFeature = $("#case_builtInFeature").combobox("getText");
// 		}
// 		var peripherals = $("#case_peripherals").val();//周邊設備1
// 		if(peripherals != ''){
// 			peripherals = $("#case_peripherals").find("option:selected").text();
// 		}
// 		var peripheralsFunction = $("#case_peripheralsFunction").val();//周邊設備功能1
// 		if(peripheralsFunction != ''){
// 			var peripheralsFunction = $("#case_peripheralsFunction").combobox("getText");
// 		}		
// 		var peripherals2 = $("#case_peripherals2").val();
// 		if(peripherals2 != ''){
// 			peripherals2 = $("#case_peripherals2").find("option:selected").text();
// 		}
// 		var peripheralsFunction2 = $("#case_peripheralsFunction2").val();
// 		if(peripheralsFunction2 != ''){
// 			peripheralsFunction2 = $("#case_peripheralsFunction2").combobox("getText");
// 		}
// 		var peripherals3 = $("#case_peripherals3").val();
// 		if(peripherals3 != ''){
// 			peripherals3 = $("#case_peripherals3").find("option:selected").text();
// 		}
// 		var peripheralsFunction3 = $("#case_peripheralsFunction3").val();
// 		if(peripheralsFunction3 != ''){
// 			peripheralsFunction3 = $("#case_peripheralsFunction3").combobox("getText");
// 		}
// 		var cupQuickPass = $("#case_cupQuickPass").val();//銀聯閃付   ("") 	
// 		var rows = $('#transDataGrid').datagrid('getRows');//交易參數  //取得資料列 (幾筆資料)

// 		var paraContent = {
				
// 				batch_num : batchNum,
// 				CASE_ID : caseId,				
// 				DTID : dtid,
// 				CUSTOMER_ID : customerId,
// 				CASE_CATEGORY : caseCategory,
// 				INSTALL_TYPE : installType,
// 				MERCHANT_HEADER_ID : merchantHeaderId,
// 				INSTALLED_ADRESS_LOCATION : installedAdressLocation,
// 				INSTALLED_ADRESS : installedAdress,			
// 				IS_OPEN_ENCRYPT : isOpenEncrypt,
// 				ELECTRONIC_PAY_PLATFORM : electronicPayPlatform,
// 				EDC_TYPE : edcType,
// 				SOFTWARE_VERSION : softwareVersion,
// 				CONNECTION_TYPE : connectiontype,
// 				ECR_CONNECTION : ecrConnection,
// 				ELECTRONIC_INVOICE : electronicInvoice,
// 				NET_VENDOR_ID : netVendorId,
// 				BUILT_IN_FEATURE : builtInFeature,
// 				PERIPHERALS : peripherals,
// 				PERIPHERALS_FUNCTION : peripheralsFunction,
// 				PERIPHERALS2 : peripherals2,
// 				PERIPHERALS_FUNCTION2 : peripheralsFunction2,
// 				PERIPHERALS3 : peripherals3,
// 				PERIPHERALS_FUNCTION3 : peripheralsFunction3,
// 				CUP_QUICK_PASS : cupQuickPass,
// 				TXParam :　rows
// 		}
// 		//ajax跨域問題 : 加上"="解決
// 		//paraContent = '=' + JSON.stringify(paraContent);	
// 		paraContent = JSON.stringify(paraContent);	
// 		var sURL = "${TMSURL}";	
// 		var flag = null;
// 		var result = null;
// 		var msg = null;
// 		ajaxService.TMSPost(sURL,paraContent,function(data){	
//  						if(data){
//  							if(data.flag){
//  								if(data.result == 0){
//  									$.messager.alert("匯入成功",data.msg,'');		
//  								}else{
//  									$.messager.alert("匯入失敗",data.msg,'');		
//  								}
//  							}else{
//  								$.messager.alert("匯入失敗",'系統錯誤，處理失敗','');	
//  							}
//  						}else{
//  							$.messager.alert("匯入失敗",'系統錯誤，無回傳值','');
//  						}
//  					});
// 		$.unblockUI();	
// 		javascript:dwr.engine.setAsync(true);					
// 	}

		/**
	*	TMS參數匯入
	*	單筆匯入
	*	author: tonychen
	*/
	function tmsprocess(caseId) {
		var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
 		//API_LOG 功能代碼	FUNCTION_CODE 使用
		var ucNo = "${ucNo}";
 		javascript:dwr.engine.setAsync(false);	
 		$.blockUI(blockStyle);		
 		ajaxService.TMSParaContents(caseId, ucNo, function(data){
 				if(data){
 					if(data.flag){
							$.messager.alert("匯入結果",data.resultMsg,'');
 					}else{
 							$.messager.alert("匯入失敗",'系統錯誤，處理失敗','');	
 					}
 				}else{
 					$.messager.alert("匯入失敗",'系統錯誤，無回傳值','');
 					}
 				});	
		$.unblockUI();	
		javascript:dwr.engine.setAsync(true);					
	}
	
	/**
	*	TMS參數匯入
	*	多筆整批匯入 Multiple
	*	author: tonychen
	*/
	function tmsprocessMu(datagridId) {
		var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}}; 		 
 		var caseIds = '';
 		//API_LOG 功能代碼	FUNCTION_CODE 使用
		var ucNo = "${ucNo}";
		//多筆整批，取得勾選數量、案件編號
 		var selectRow = $("#dg").datagrid('getSelections');
		for(var i = 0; i<selectRow.length; i++){
			if(i==0){
				caseIds = selectRow[i].caseId;
			}else{
				caseIds += "," + selectRow[i].caseId;
			}							
		}		
 		javascript:dwr.engine.setAsync(false);	
 		$.blockUI(blockStyle);  
		/*傳入:	勾選數量、案件編號
		* 執行:	1.以案件編號取得案件資料
		*    	2.製成paraContent並迴圈POST至TMS API
		* 回傳:	POST結果(多筆)
		*/
 		ajaxService.TMSParaContents(caseIds, ucNo, function(data){
 				if(data){
 					if(data.flag){
 						if(data.messageSize){
 							$.messager.alert({
 							title:'匯入結果',
 							msg:'<div  style="width:550px;height:250px;overflow:auto;">' + data.resultMsg + '</div>',
 							width:550,
 							height:350
 							})
						}else{
							$.messager.alert("匯入結果",data.resultMsg,'');
						}
 					}else{
 							$.messager.alert("系統錯誤，處理失敗",data.resultMsg,'');
 					}
 				}else{
 					$.messager.alert("匯入失敗",'系統錯誤，無回傳值','');
 					}
 				});	
		$.unblockUI();	
		javascript:dwr.engine.setAsync(true);					
	}

	/**
	* 案件動作保存
	* datagridId：當前展開datagrid的id flag：標志位
	* caseId：案件編號 caseCategory：案件類別
	*/
	function saveAction(datagridId, flag, caseId, caseCategory, oldCaseType) {
		// 對處理頁面的案件編號進行編碼
		if(flag && caseId){
			caseId = decodeURIComponent(caseId);
		}
		var row = $('#' + datagridId).datagrid('getData').rows[0];
		var controls = ['dispatchUnit', 'caseType', 'delayTime', 'agentsId', 'checkResult', 'problemReason', 'problemSolution', 'responsibity', 'completeDate', 'createdDate', 'logisticsVendor' ,'logisticsNumber','serialNumber'];
		var newCaseTypeEditor = $('#' + datagridId).datagrid('getEditor', {  
			index : '0',  
			field : 'caseType'
		});
		var newCaseType = '';
		if(newCaseTypeEditor && newCaseTypeEditor != null){
			newCaseType = newCaseTypeEditor.target.combobox('getValue');
		}
		if('${iatomsContants.TICKET_MODE_APPOINTMENT}' == newCaseType){
			controls = ['dispatchUnit', 'caseType', 'delayTime', 'expectedCompleteDate'];
		} 
		if(!validateFormInRow(datagridId, 0, controls)) {
			return false;
		} else {
			$("div").unbind("scroll.validate");
		}
		$("#" + (datagridId + '-msg')).text("");
		var saveActionFlag = false;
		if((row.caseActionId == '${caseActionAttr.SIGN.code }') || (row.caseActionId == '${caseActionAttr.ONLINE_EXCLUSION.code }')){
			if(row.caseCategory == '${caseCategoryAttr.CHECK.code}' || row.caseCategory == '${caseCategoryAttr.OTHER.code}') {
				var controls = [];
				if (row.caseCategory == '${caseCategoryAttr.CHECK.code}') {
					controls = ['checkResult'];
				}
				if(!validateFormInRow(datagridId, 0, controls)){
					return;
				} else {
					$("div").unbind("scroll.validate");
				}
			}
			if(row.caseCategory == '${caseCategoryAttr.REPAIR.code}'){
				var controls = ['problemReason','problemSolution','responsibity'];
				if(!validateFormInRow(datagridId, 0, controls)){
					return;
				} else {
					$("div").unbind("scroll.validate");
				}
			}
		}
		if((row.caseActionId == '${caseActionAttr.ONLINE_EXCLUSION.code }')) {
			var controls = ['agentsId'];
			if(!validateFormInRow(datagridId, 0, controls)){
				return;
			} else {
				$("div").unbind("scroll.validate");
			}
		}
		if($('#' + datagridId).datagrid('validateRow', 0)){
			
			//問題原因描述
			var problemReasonEditor = $('#' + datagridId).datagrid('getEditor', {  
				index : '0',  
				field : 'problemReason'
			});
			var problemReasonName = "";
			if(problemReasonEditor && problemReasonEditor != null){
				problemReasonName = problemReasonEditor.target.combobox('getText');
			}
			row.problemReasonName = problemReasonName;
			//解决方式描述
			var problemSolutionEditor = $('#' + datagridId).datagrid('getEditor', {  
				index : '0',  
				field : 'problemSolution'
			});
			var problemSolutionName = "";
			//Task #2797 解決方式
			var problemSolution = "";
			if(problemSolutionEditor && problemSolutionEditor != null){
				problemSolutionName = problemSolutionEditor.target.combobox('getText');
				problemSolution = problemSolutionEditor.target.combobox('getValue');
			}
			row.problemSolutionName = problemSolutionName;
			
			//查核結果描述
			var checkResultEditor = $('#' + datagridId).datagrid('getEditor', {  
				index : '0',  
				field : 'checkResult'
			});
			var checkResultName = "";
			if(checkResultEditor && checkResultEditor != null){
				checkResultName = checkResultEditor.target.combobox('getText');
			}
			row.checkResultName = checkResultName;
			//責任歸屬描述
			var responsibityEditor = $('#' + datagridId).datagrid('getEditor', {  
				index : '0',  
				field : 'responsibity'
			});
			var responsibityName = "";
			if(responsibityEditor && responsibityEditor != null){
				responsibityName = responsibityEditor.target.combobox('getText');
			}
			if(responsibityName=='請選擇'){
				responsibityName = '';
			}
			row.responsibityName = responsibityName;
			//物流廠商描述
			var logisticsVendorEditor = $('#' + datagridId).datagrid('getEditor', {  
				index : '0',  
				field : 'logisticsVendor'
			});
			var logisticsVendorName = "";
			if(logisticsVendorEditor && logisticsVendorEditor != null){
				logisticsVendorName = logisticsVendorEditor.target.combobox('getText');
			}
			row.logisticsVendorName = logisticsVendorName;
			if(flag){
				saveActionFlag = true;
			} else {
				// 驗證選中案件的案件信息等
				if(validateSelectCase(row.caseActionId)){
					saveActionFlag = true;
				}
			}
			//線上排除代理處理工程師
			var agentsName = "";
			var agentsId = "";
			var agentsResult = $("#" + datagridId).datagrid('getEditor', {
				   index : 0,
				   field : 'agentsId'
				});
			if(agentsResult && agentsResult != null){
				row.agentsName = $(agentsResult.target).combobox('getText');
				row.agentsId = $(agentsResult.target).combobox('getValue');
			}
			if(saveActionFlag){
				// 檢測驗證
	 			var selectRow = $("#dg").datagrid('getSelections');
	 			var ddvAssetLinkParameters = [];
	 			var signIsValidata = true;
	 			if(((row.caseActionId == '${caseActionAttr.SIGN.code }') || (row.caseActionId == '${caseActionAttr.ONLINE_EXCLUSION.code }')) 
	 					&& (row.caseCategory != '${caseCategoryAttr.CHECK.code}' && row.caseCategory != '${caseCategoryAttr.OTHER.code}')){
		 			signIsValidata = false;
		 			if(flag) {
		 				ddvAssetLinkParameters = saveSignParamerInCaseAction(caseCategory,row, caseId);
		 				if(!ddvAssetLinkParameters){
			 				return false;
			 			}
			 			//驗證設備鏈接周邊設備是否取消完整
			 			var isvalidata = validatePeripheralsIsCompleteInCaseAction(caseCategory,row,caseId);
			 			
			 			//Task #2797解決方式=特店-提供新刷卡機 ，但無替換主機設備，請警示，解決方式=特店-提供新刷卡機，請替換設備 2017/11/14
			 			if(caseCategory=='REPAIR' && isvalidata != 'D' && validateRepairOnlineExclusion(selectRow,problemSolution,false,caseCategory)=='Y'){
			 				return false;
			 			}
			 			if(isvalidata != '${iatomsContants.YES}' && isvalidata != '${iatomsContants.CASE_DELETE_ASSET}') {
			 				$.messager.confirm('確認對話框','案件編號：'+isvalidata+'，週邊設備未移除完整，確認執行?', function(comfirm){
								if (comfirm) {
									var rowsDatagrid = $('#dg').datagrid('getSelections');
						 			// 簽收、線上排除-設備設定連結及耗材設定連接 
									for (var i = 0; i < rowsDatagrid.length; i++) {
										$('#dg').datagrid("collapseRow", $('#dg').datagrid('getRowIndex', rowsDatagrid[i]));
									}
									saveActionParamer(datagridId, flag, caseId, caseCategory, oldCaseType,
	 									selectRow,ddvAssetLinkParameters,row);
									
								} else {
									editIndex = undefined;
			 						return false;
								}
							});
			 			} else if (isvalidata == '${iatomsContants.CASE_DELETE_ASSET}'){
			 				editIndex = undefined;
			 				return false;
			 			} else {
			 				var rowsDatagrid = $('#dg').datagrid('getSelections');
				 			// 簽收、線上排除-設備設定連結及耗材設定連接 
							for (var i = 0; i < rowsDatagrid.length; i++) {
								$('#dg').datagrid("collapseRow", $('#dg').datagrid('getRowIndex', rowsDatagrid[i]));
							}
							saveActionParamer(datagridId, flag, caseId, caseCategory, oldCaseType,
	 							selectRow,ddvAssetLinkParameters,row);
			 			}
		 			} else {
		 				//主頁面
			 			ddvAssetLinkParameters = saveSignParamer(selectRow,row,datagridId);
			 			if(!ddvAssetLinkParameters){
			 				return false;
			 			}
			 			//驗證設備鏈接周邊設備是否取消完整
			 			var isvalidata = validatePeripheralsIsComplete(selectRow,row,datagridId);
			 			
			 			//Task #2797解決方式=特店-提供新刷卡機 ，但無替換主機設備，請警示，解決方式=特店-提供新刷卡機，請替換設備 2017/11/14
			 			if(selectRow[0].caseCategory =='REPAIR' && isvalidata != 'D' && validateRepairOnlineExclusion(selectRow,problemSolution,true) == 'Y'){
			 				return false;
			 			}
			 			
			 			if(isvalidata != '${iatomsContants.YES}' && isvalidata != '${iatomsContants.CASE_DELETE_ASSET}') {
			 				$.messager.confirm('確認對話框','案件編號：'+isvalidata+'，週邊設備未移除完整，確認執行?', function(comfirm){
								if (comfirm) {
									var rowsDatagrid = $('#dg').datagrid('getSelections');
						 			// 簽收、線上排除-設備設定連結及耗材設定連接 
									for (var i = 0; i < rowsDatagrid.length; i++) {
										$('#dg').datagrid("collapseRow", $('#dg').datagrid('getRowIndex', rowsDatagrid[i]));
									}
									saveActionParamer(datagridId, flag, caseId, caseCategory, oldCaseType,
	 									selectRow,ddvAssetLinkParameters,row);
									
								} else {
									editIndex = undefined;
			 						return false;
								}
							});
			 			} else if (isvalidata == '${iatomsContants.CASE_DELETE_ASSET}'){
			 				editIndex = undefined;
			 				return false;
			 			} else {
			 				var rowsDatagrid = $('#dg').datagrid('getSelections');
				 			// 簽收、線上排除-設備設定連結及耗材設定連接 
							for (var i = 0; i < rowsDatagrid.length; i++) {
								$('#dg').datagrid("collapseRow", $('#dg').datagrid('getRowIndex', rowsDatagrid[i]));
							}
							saveActionParamer(datagridId, flag, caseId, caseCategory, oldCaseType,
	 							selectRow,ddvAssetLinkParameters,row);
			 			}
		 			}
	 			}
	 			if(signIsValidata) {
	 				saveActionParamer(datagridId, flag, caseId, caseCategory, oldCaseType,
	 				selectRow,ddvAssetLinkParameters,row);
	 			}
			}
		}
	}
	/**
	*	存儲的時候獲取 參數.  datagridId：datagrid，區別主頁面和處理頁面
	*	flag： true說明來自處理頁面
	*	caseId：案件編號 
	*	caseCategory:案件類別
	*	oldCaseType：舊的案件類型
	*	selectRow：選擇的行
	*	ddvAssetLinkParameters：簽收線上排除的參數
	*	row：返回後台的數據集合
	*/
	function saveActionParamer(datagridId, flag, caseId, caseCategory, oldCaseType,selectRow,ddvAssetLinkParameters, row) {
		var dealDescription = $("#dealDescription").val();
		var caseIds = "";
		row.updatedDateString = '';
		if(flag){
			caseIds = caseId;
			//處理頁面存放異動時間
			row.updatedDateString = $("#hideUpdateDate").val() + ";" +caseId;
		} else {
			//主頁面 存放異動時間
			for (var i = 0; i<selectRow.length; i++) {
				if(selectRow.length >=1 && i == selectRow.length -1 ){
					caseIds = caseIds + selectRow[i].caseId;
					row.updatedDateString = row.updatedDateString + selectRow[i].updatedDate+";"+ selectRow[i].caseId;
				} else {
					caseIds = caseIds + selectRow[i].caseId + ",";
					row.updatedDateString = row.updatedDateString + selectRow[i].updatedDate + ";" + selectRow[i].caseId + ",";
				}
			}
		}
		var personName;
		var personId;
		// 處理人員
		var processPersonEditor = $('#' + datagridId).datagrid('getEditor', {  
			index : '0',  
			field : 'dealById'
		});
		if(processPersonEditor && processPersonEditor != null){
			personName = processPersonEditor.target.combobox('getText');
			personId = processPersonEditor.target.combobox('getValue');
		}
		// 派工單位
		var departmentEditor = $('#' + datagridId).datagrid('getEditor', {  
			index : '0',  
			field : 'dispatchUnit'
		});
		// 案件類型
		var caseTypeEditor = $('#' + datagridId).datagrid('getEditor', {  
			index : '0',  
			field : 'caseType'
		});
		var caseType;
		if(caseTypeEditor && caseTypeEditor != null){
			caseType= caseTypeEditor.target.combobox('getValue');
		}
		var deptName;
		var deptId;
		if(departmentEditor && departmentEditor != null){
			deptName = departmentEditor.target.combobox('getText');
			deptId = departmentEditor.target.combobox('getValue');
		}
		// 處理時間
		var dealDate;
		var dealDateEditor = $('#' + datagridId).datagrid('getEditor', {  
			index : '0',  
			field : 'dealDate'
		});
		if(dealDateEditor && dealDateEditor != null){
			dealDate = dealDateEditor.target.datetimebox('getValue');
			if(dealDate && dealDate != ''){
				dealDate = dealDate + ':00';
			}
		}
		//修改案件類型
		if(row.caseActionId && '${caseActionAttr.CHANGE_CASE_TYPE.code }' == row.caseActionId){
			if (flag) {
				if(caseType == oldCaseType){
					msg = '案件類型未修改，請重新選擇';
					$("#" + (datagridId + '-msg')).text(msg);
					return false;
				}
			} else {
				for (var i = 0; i<selectRow.length; i++) {
					if(selectRow[i].caseType == caseType){
						msg = '案件類型未修改，請重新選擇';
						$("#" + (datagridId + '-msg')).text(msg);
						return false;
					}
				}
			}
		}
		$('#' + datagridId).datagrid('endEdit', 0);
//		var row = $('#' + datagridId).datagrid('getData').rows[0];
		// 派工
		if(row.caseActionId && '${caseActionAttr.DISPATCHING.code }' == row.caseActionId){
			row.deptName = deptName;
			if(personName!='請選擇'){
				row.dealByName = personName;
			}
		}
		if(row.caseActionId && '${caseActionAttr.CHANGE_CASE_TYPE.code }' == row.caseActionId){
			row.caseType = caseType;
			//row.caseStatus = selectRow[0].caseStatus;
		}
		row.description = dealDescription;
		row.dealDate = dealDate;
		row.caseId = caseIds;
		row.caseAssetLinkParameters = JSON.stringify(ddvAssetLinkParameters);
		//row.nowCaseStatus = row.caseStatus;
		//獲取通知時設定的通知參數（存放在隱藏域）
		var saveEmailParamer = $("#saveEmailParamer").val();
		//通知種類
		row.noticeType = JSON.parse(saveEmailParamer).noticeType;
		//通知時 提醒時間起
		row.remindStart = JSON.parse(saveEmailParamer).remindStart;
		//通知時 提醒時間迄
		row.remindEnd = JSON.parse(saveEmailParamer).remindEnd;
		//通知時，接受郵件的人員
		row.toMail = JSON.parse(saveEmailParamer).toMail;
		//刪除掉的耗材id
		var deleteCaseSuppliesLinkIds = $("#deleteCaseSuppliesLinkIds").val();
		row.deleteCaseSuppliesLinkIds = deleteCaseSuppliesLinkIds;
		//刪除掉的設備鏈接id 移除R
		var deleteCaseAssetLinkIds = $("#deleteCaseAssetLinkIds").val();
		row.deleteCaseAssetLinkIds = deleteCaseAssetLinkIds;
		if(row.responsibityName!= null && row.responsibityName=='請選擇'){
			row.responsibityName = '';
		}
			
		var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		// 加遮罩
		if(flag){
			// 調保存遮罩
			commonSaveLoading('editDlg');
		} else {
			$.blockUI(blockStyle);
		}
		//Task #3358 當leaseSignFlag=Y 時 為 租賃簽收
		if(row.caseActionId=='onlineExclusion' && $("#hideLeaseSignFlag").val()=='Y'){
			row.caseActionId = 'leaseSign';
		}
		$.ajax({
			url : "${contextPath}/caseHandle.do?actionId="+row.caseActionId,
			data : row,
			type : 'post',
			cache : false, 
			dataType :'json',
			success : function(json) {
				var pageIndex = getGridCurrentPagerIndex("dg");
				var msg;
				if (json.success) {
					$("#hideLeaseSignFlag").val('N');
					$("#deleteCaseSuppliesLinkIds").val("");
					$("#deleteCaseAssetLinkIds").val("");
					$("#selectSn").val("");
					$('#' + datagridId).datagrid('deleteRow', 0);  
					//成功提示
					if (json.cmsResult != undefined && !json.cmsResult) {
						$.messager.alert('提示訊息',json.msg,'');
					} else {
						$("#" + (datagridId + '-msg')).text(json.msg);
					}
					query(pageIndex, false, json.msg);
					if(flag){
						$('#responsePanel').panel('close');
					} else {
						$('#response').panel('close');
					}
					// 處理頁面
					if(flag){
						if (!(json.cmsResult != undefined && !json.cmsResult)){
							// 處理按鈕顯示
							//#3359 若為報修件且為CMS案件，若執行線上排除按鈕成功后，狀態應更新為結案
							if(json.closedByOnlineExclusion){
								dealButtonShow('${caseStatusAttr.CLOSED.code }', row.caseId);
							}else{
								dealButtonShow(row.caseStatus, row.caseId);
							}
						}
						// 處理控件顯示
						dealDisabledControl(row.caseStatus);
						// 結案時查案件歷史記錄，非結案查當前
						queryCaseRecord('N', true);
		    			// 處理頁面修改時更新案件相關欄位
		    			updateCase(caseId, row.caseActionId);
					}
					isSign = false;
				} else {
					// 去除遮罩
					if(flag){
						// 調取消保存遮罩
						commonCancelSaveLoading('editDlg');
					} else {
						$.unblockUI();
					}
					$("#deleteCaseSuppliesLinkIds").val("");
					$("#deleteCaseAssetLinkIds").val("");
					$("#selectSn").val("");
					//失敗提示
					$("#" + (datagridId + '-msg')).text(json.msg);
					$('#' + datagridId).datagrid('beginEdit', 0);
					if (row.caseActionId == '${caseActionAttr.ONLINE_EXCLUSION.code }') {
						ajaxService.getUserListByCompany('${logonUser.companyId}', function(data){
							if (data != null) {
								var currentEditor = $("#" + datagridId).datagrid('getEditor', {  
									index : 0,  
									field : 'agentsId'
								});
								currentEditor.target.combobox('loadData',initSelect(data));
							}
						});
					}
				}
			},
			error : function(){
				// 去除遮罩
				if(flag){
					// 調取消保存遮罩
					commonCancelSaveLoading('editDlg');
				} else {
					$.unblockUI();
				}
				$('#' + datagridId).text("頁面檢核失敗");							
			}	
		});
	}
	
	/*
	* 處理創建DataGrid allParams：存放當前傳入的參數
	*/
	function dealCreateGridInfo(allParams){
		javascript:dwr.engine.setAsync(false);
		// 處理頁面使用當前的案件編號查詢出來的案件狀態、案件類別與派工部門
		if(allParams.flag){
			ajaxService.getCaseInfoById(allParams.caseId, function(data){
				if(data){
					allParams.caseStatus = data.caseStatus;
					allParams.caseCategory = data.caseCategory;
					allParams.departmentName = data.dispatchDeptName;
					allParams.caseType = data.caseType;
					allParams.cmsCase = data.cmsCase;
					allParams.customerId=data.customerId;
				}
			});
		}
		javascript:dwr.engine.setAsync(true);
		var datagridId = allParams.datagridId;
		var actionId = allParams.actionId;
		<c:forEach var="caseAction" items="${caseActionList}" varStatus="itemStatus">
			if('${caseAction.value}' == actionId){
				allParams.title = '${caseAction.name}';
				// 調用創建grid的方法
				createCaseGrid(allParams);
			}
		</c:forEach>
		$('#' + datagridId).datagrid('beginEdit', 0);
		// 處理欄位限制長度
		checkTextBox(0, datagridId, actionId);
		if(actionId == '${caseActionAttr.CHANGE_CASE_TYPE.code }'){
			var processPersonEditor = $('#' + datagridId).datagrid('getEditor', {  
				index : '0',  
				field : 'expectedCompleteDate'
			});
			processPersonEditor.target.datebox("disableValidation");
		}
		//Task #3358 
		if (actionId == ('${caseActionAttr.LEASE_PRELOAD.code }')){
			var rows = $("#dg").datagrid('getSelections');
			var cmsCaseFlag = false;
			var dgRow = $('#dgResponse').datagrid('getData').rows[0];
			if(!allParams.flag && dgRow!=null && rows.length>0){
				for (var i = 0 ; i < rows.length; i++) {
					if (rows[i].cmsCase == "Y") {
						cmsCaseFlag = true;
						break;
					}
				}
			}
			var serialNumberEditor = $("#" + datagridId).datagrid('getEditor', {  
				index : 0,  
				field : 'serialNumber'
			});
			if((allParams.flag && allParams.cmsCase=='Y') || cmsCaseFlag){
				$(serialNumberEditor.target).textbox({required:true,missingMessage:'請輸入設備序號(EDC)',});
			} 
			$(serialNumberEditor.target).next().children().focus();
			$(serialNumberEditor.target).textbox('textbox').attr('maxlength',20);
			
			var simSerialNumberEditor = $("#" + datagridId).datagrid('getEditor', {  
				index : 0,  
				field : 'simSerialNumber'
			});
			if((allParams.flag && allParams.cmsCase=='Y') || cmsCaseFlag){
				$(simSerialNumberEditor.target).textbox({required:true,missingMessage:'請輸入設備序號(SIM卡)',});
			} 
			//$(serialNumberEditor.target).next().children().focus();
			$(simSerialNumberEditor.target).textbox('textbox').attr('maxlength',20);
		}
		if (actionId == ('${caseActionAttr.DISTRIBUTION.code }')){
			var serialNumberEditor = $("#" + datagridId).datagrid('getEditor', {  
			    index : 0,  
			    field : 'logisticsNumber'
			});
			$(serialNumberEditor.target).textbox('textbox').attr('maxlength',50);
		}
		//Task #3460
		if(actionId == '${caseActionAttr.ONLINE_EXCLUSION.code }' && allParams.cmsCase == '${iatomsContants.PARAM_YES }' && '${logonUser.companyId}'!='10000000-01'){
			var currentEditor = $("#" + datagridId).datagrid('getEditor', {  
				index : 0,  
				field : 'agentsId'
			});
			currentEditor.target.combobox('loadData',initSelect(null));
		}
		// Task #3359若為報修件且為CMS案件，若執行線上排除按鈕 責任歸屬為必填
		if (actionId == '${caseActionAttr.ONLINE_EXCLUSION.code }' &&
				allParams.caseCategory == '${caseCategoryAttr.REPAIR.code }'){
			var rows = $("#dg").datagrid('getSelections');
			var cmsCaseFlag = false;
			var dgRow = $('#dgResponse').datagrid('getData').rows[0];
			if(!allParams.flag && dgRow!=null && rows.length>0){
				for (var i = 0 ; i < rows.length; i++) {
					if (rows[i].cmsCase == "Y") {
						cmsCaseFlag = true;
						break;
					}
				}
			}
			if((allParams.flag && allParams.cmsCase=='Y') || cmsCaseFlag){
				var responsibityEditor = $("#" + datagridId).datagrid('getEditor', {  
					index : 0,  
					field : 'responsibity'
				});
				//console.info(responsibityEditor.target);
				responsibityEditor.target.combobox({required:true,missingMessage:'請輸入責任歸屬',validType:'ignore[\'請選擇\']',invalidMessage:'請輸入責任歸屬'});
			}
		}
	}
	
	/*
	* 處理創建DataGrid allParams:json格式數據，存放所有所需數據
	*/
	function createCaseGrid(allParams){
		//清空設備序號隱藏域 2018/01/31
		$("#selectSn").val("");
		// 拿到所有所需數據
		var title = allParams.title;
		var actionId = allParams.actionId;
		var datagridId = allParams.datagridId;
		var panelId = allParams.panelId;
		var caseStatus = allParams.caseStatus;
		var caseCategory = allParams.caseCategory;
		var responsibityVal = allParams.responsibity;
		var problemSolutionVal = allParams.problemSolution;
		var problemReasonVal = allParams.problemReason;
		var flag = allParams.flag;
		var caseId = allParams.caseId;
		var oldCaseType = allParams.caseType;
		// datagrid數據
		var resultData = {};
		var rows = [];
		var editRow = {};
		var selectRow = $('#dg').datagrid('getSelections');
		// 動作
		editRow.caseActionName = title;
		if(($("#hideLeaseSignFlag").val()=='Y') && (actionId == '${caseActionAttr.ONLINE_EXCLUSION.code }')){
			title = '租賃簽收'
			editRow.caseActionName = "簽收";
		}
		// Task #3113 完修退回至客服
		/* if (title=='退回') {
			editRow.caseActionName = '結案審查退回';
		} */
		editRow.nowCaseStatus = caseStatus;
		editRow.caseActionId = actionId;
		// 放置案件類別
		editRow.caseCategory = caseCategory;
		var caseStatusName;
		var datagridColums = [];
		// 放置字段
		datagridColums.push({ field: 'caseActionName', title: '動作', halign:'center',align:'left', width: 150 });
		datagridColums.push({
			field: 'afterCaseStatus', title: '動作後狀態', halign:'center',align:'left', width: 150
		});
		//Task #3336
		if(actionId == ('${caseActionAttr.LEASE_PRELOAD.code }')){
			datagridColums.push({
				field: 'serialNumber', title: '設備序號(EDC)', halign:'center', width: 150,
				editor: {
					type: 'textbox',
					options:{
						maxlength:20
						
					}
				}
			});
			editRow.serialNumber='';
			datagridColums.push({
				field: 'simSerialNumber', title: '設備序號(SIM卡)', halign:'center', width: 150,
				editor: {
					type: 'textbox',
					options:{
						maxlength:20
						
					}
				}
			});
			editRow.serialNumber='';
		}
		if(actionId == ('${caseActionAttr.DISPATCHING.code }')){
			var departmentList = null;
			if (allParams.cmsCase == "Y") {
				departmentList = initSelect(<%=departmentCybList %>);
			} else {
				departmentList = initSelect(<%=departmentList %>);
			}
			
			//若案件類別為“拆機”，則派工單位增加“原裝機單位及人員”選項，供派工給原裝機人員處理拆機案件
			if(caseCategory=='${caseCategoryAttr.UNINSTALL.code }'){
				departmentList.push({"name":"原裝機單位及人員","value":"oldInstalledDeptAndUser"});
			} 
			//Bug #2363 update by 2017/09/05 --派工單位、處理人員 等欄位大一點 
			//if (datagridId == 'dataGridResponse') {
				// 派工增加項
				datagridColums.push({
					field: 'dispatchUnit', title: '派工單位', halign:'center', width: 180,
					formatter:function(value,row){
				        return row.dispatchUnitName;
				       },
					editor: {
						type: 'combobox',
						options: {
							valueField: 'value',
							textField: 'name',
							onChange : selectChange,
							data: departmentList,
							required:true,
							editable:false,
							missingMessage:'請輸入派工單位',
							validType:'ignore[\'請選擇\']',invalidMessage:'請輸入派工單位'
						}
					}
				});
				if (IsQaConfirm(allParams,selectRow)) {
					editRow.dispatchUnit = 'QA';
				} else {
					editRow.dispatchUnit = '';
				}
			datagridColums.push({
				field: 'dealById', title: '處理人員', halign:'center', width: 160,
				formatter:function(value,row){
			        return row.dealByIdName;
			       },
				editor: {
					type: 'combobox',
					options: {
						valueField: 'value',
						textField: 'name',
						editable:false,
						data: initSelect(null)
					}
				}
			});
			editRow.dealById = '';
		} else if((actionId == '${caseActionAttr.ONLINE_EXCLUSION.code }') || (actionId == '${caseActionAttr.CLOSED.code }')
				|| (actionId == '${caseActionAttr.SIGN.code }')){
			// 線上排除、結案、簽收 顯示列
			if((actionId == '${caseActionAttr.ONLINE_EXCLUSION.code }' && caseCategory == '${caseCategoryAttr.CHECK.code}') || (actionId == '${caseActionAttr.SIGN.code }' && caseCategory == '${caseCategoryAttr.CHECK.code}')){
				// 查核結果
				var checkResultsList = initSelect(${checkResultsList});
				editRow.checkResult = "";
				// 顯示查核結果
				datagridColums.push({
					field: 'checkResult', title: '查核結果', halign:'center', width: 150,
					formatter:function(value,row){
				        return row.checkResultName;
				       },
					editor: {
						type: 'combobox',
						options: {
							valueField: 'value',
							textField: 'name',
							data: checkResultsList,
							editable:false,
							required:true,
							missingMessage:'請輸入查核結果',
							validType:'ignore[\'請選擇\']',invalidMessage:'請輸入查核結果'
						}
					}
				});
			}
			if(actionId == '${caseActionAttr.ONLINE_EXCLUSION.code }') {
				// 顯示查核結果
				datagridColums.push({
					field: 'agentsId', title: '處理工程師', halign:'center', width: 150,
					formatter:function(value,row){
						return row.agentsName;
					},
					editor: {
						type: 'combobox',
						options: {
							valueField: 'value',
							textField: 'name',
							data: initSelect(null),
							editable:false,
							required:true,
							missingMessage:'請輸入處理工程師',
							validType:'ignore[\'請選擇\']',invalidMessage:'請輸入處理工程師'
						}
					}
				});
				//Task #3460
				if(allParams.cmsCase == '${iatomsContants.PARAM_YES }' && '${logonUser.companyId}'!='10000000-01'){
				} else {
					ajaxService.getUserListByCompany('${logonUser.companyId}', function(data){
						if (data != null) {
							var currentEditor = $("#" + datagridId).datagrid('getEditor', {  
								index : 0,  
								field : 'agentsId'
							});
							currentEditor.target.combobox('loadData',initSelect(data));
						}
					});
				}
			}
			var customerId = allParams.customerId;
			var problemReasonList = null;
			var problemSolutionList = null;
			//console.info(${problemReasonTsbListStr});
			//console.info(${problemReasonList});
			//Task #3433
			if(customerId=='${formDTO.tsbCustomerId}'){
				// 問題原因
				problemReasonList = initSelect(${problemReasonTsbListStr});
				// 問題解決方式
				problemSolutionList = initSelect(${problemSolutionTsbListStr});
			}else{
				// 問題原因
				problemReasonList = initSelect(${problemReasonList});
				// 問題解決方式
				problemSolutionList = initSelect(${problemSolutionList});
			}
			
			// 責任歸屬
			var responsibityList = initSelect(${responsibityList});
			editRow.problemReason = '';
			editRow.problemSolution = '';
			editRow.responsibity = '';
			//保修
			if(caseCategory == '${caseCategoryAttr.REPAIR.code}'){
				//結案  Bug #2276
				if(actionId == ('${caseActionAttr.CLOSED.code }')){
					editRow.problemReason = problemReasonVal;
					editRow.problemSolution = problemSolutionVal;
					editRow.responsibity = responsibityVal;
				}
				datagridColums.push({
					field: 'problemReason', title: '問題原因', width: 200, halign:'center',
					formatter:function(value,row){
				        return row.problemReasonName;
				       },
					editor: {
						type: 'combobox',
						options: {
							valueField: 'value',
							textField: 'name',
							data: problemReasonList,
							required:true,
							editable:false,
							selectedValue:editRow.problemReason,
							missingMessage:'請輸入問題原因',
							validType:'ignore[\'請選擇\']',invalidMessage:'請輸入問題原因'
						}
					}
				});
				datagridColums.push({
					field: 'problemSolution', title: '問題解決方式', width: 200, halign:'center',
					formatter:function(value,row){
				        return row.problemSolutionName;
				       },
					editor: {
						type: 'combobox',
						options: {
							valueField: 'value',
							textField: 'name',
							data: problemSolutionList,
							required:true,
							editable:false,
							selectedValue:editRow.problemSolution,
							missingMessage:'請輸入問題解決方式',
							validType:'ignore[\'請選擇\']',invalidMessage:'請輸入問題解決方式'
						}
					}
				});
				
				// Task #3065 案件處理-工程師簽收責任歸屬 工程師簽收時無法裁定責任歸屬 結案必填 //Task #3358 
				if(actionId == '${caseActionAttr.CLOSED.code }' || $("#hideLeaseSignFlag").val()=="Y"){
					datagridColums.push({
						field: 'responsibity', title: '責任歸屬', width: 140, halign:'center',
						formatter:function(value,row){
					        return row.responsibityName;
					       },
						editor: {
							type: 'combobox',
							options: {
								valueField: 'value',
								textField: 'name',
								data: responsibityList,
								panelHeight: 'auto',
								editable:false,
								required:true,
								editable:false,
								selectedValue:editRow.responsibity,
								missingMessage:'請輸入責任歸屬',
								validType:'ignore[\'請選擇\']',invalidMessage:'請輸入責任歸屬'
							}
						}
					});
				} else {
					datagridColums.push({
						field: 'responsibity', title: '責任歸屬', width: 140, halign:'center',
						formatter:function(value,row){
					        return row.responsibityName;
					       },
						editor: {
							type: 'combobox',
							options: {
								valueField: 'value',
								textField: 'name',
								data: responsibityList,
								panelHeight: 'auto',
								editable:false,
								editable:false,
								selectedValue:editRow.responsibity
							}
						}
					});
				}
				
			}
		} else if(actionId == '${caseActionAttr.DELAY.code }'
				|| actionId == '${caseActionAttr.RESPONSE.code }'){
			var isRequired = (actionId == '${caseActionAttr.DELAY.code }') ? true : false;
			// 延期 顯示列
			datagridColums.push({
				field: 'delayTime', title: '延期', width: 130, halign:'center',
				editor: {
					type: 'datebox',
					options: {
						required:isRequired,
						missingMessage:'請輸入延期',
						validType:['date','compareDateWithNowDate']
					}
					
				}
			});
		} else if (actionId == ('${caseActionAttr.DISTRIBUTION.code }')) {
			var logisticsVendorList = initSelect(${logisticsVendors});
			datagridColums.push({
				field: 'logisticsVendor', title: '物流廠商', width: 140, halign:'center',
				formatter:function(value,row){
			        return row.logisticsVendor;
			       },
				editor: {
					type: 'combobox',
					options: {
						valueField: 'value',
						textField: 'name',
						required:true,
						data: logisticsVendorList,
						panelHeight: 'auto',
						validType:'ignore[\'請選擇\']',invalidMessage:'請輸入物流廠商'
					}
				}
			});
			editRow.logisticsVendor='';
			datagridColums.push({
				field: 'logisticsNumber', title: '物流編號', width: 140, halign:'center',
				editor: {
					type: 'textbox',
					options: {
						required:true,
						missingMessage:'請輸入物流編號',
					}
				}
			});
			editRow.logisticsNumber='';
		} else if(actionId == ('${caseActionAttr.CHANGE_CASE_TYPE.code }')){
			var ticketModes = initSelect(<%=ticketModeList%>);
			// 修改案件類型 顯示列
			datagridColums.push({
				field: 'caseType', title: '案件類型', width: 140, halign:'center',
				formatter:function(value,row){
			        return row.caseTypeName;
			       },
				editor: {
					type: 'combobox',
					options: {
						valueField: 'value',
						textField: 'name',
						required:true,
						onChange : appointmentChange,
						data: ticketModes,
						editable:false,
						validType:'ignore[\'請選擇\']',
						missingMessage:'請輸入案件類型',
						invalidMessage:'請輸入案件類型'
					}
				}
			});
			editRow.caseType='';
			datagridColums.push({
				field: 'expectedCompleteDate', title: '預計完成日', halign:'center', width: 140,
				editor: {
					type: 'datebox',
					options:{
						required:true,
						validType:'date',
						invalidMessage:'日期格式限YYYY/MM/DD',
						missingMessage:'請輸入預計完成日',
						disabled:true
					}
				}
			});
			editRow.expectedCompleteDate='';
			//修改實際完修時間
		} else if (actionId == ('${caseActionAttr.CHANGE_COMPLETE_DATE.code }')) {
			datagridColums.push({
				field: 'completeDate', title: '實際完修時間', halign:'center', width: 180,
				editor: {
					type: 'datetimebox',
					options:{
						required:true,
						validType:'dateTimeValid',
						missingMessage:'請輸入實際完修時間',
						invalidMessage:"實際完修時間格式限YYYY/MM/DD HH:mm:ss"
					}
				}
			});
			editRow.completeDate='';
		} else if (actionId == ('${caseActionAttr.CHANGE_CREATE_DATE.code }')) {
			datagridColums.push({
				field: 'createdDate', title: '進件時間', halign:'center', width: 180,
				editor: {
					type: 'datetimebox',
					options:{
						required:true,
						validType:'dateTimeValid',
						missingMessage:'請輸入進件時間',
						invalidMessage:'進件時間格式限YYYY/MM/DD HH:mm:ss'
					}
				}
			});
			editRow.completeDate='';
		}
		//待派工案件自動派工時，顯示 已派工
			var tempStatus = null;
			var tempFlag = false;
		// 處理狀態顯示
		if(actionId == '${caseActionAttr.LEASE_PRELOAD.code }'){
			<c:forEach var="caseStatus" items="${caseStatusList}" varStatus="itemStatus">
				if(caseStatus == '${caseStatus.value}'){
					caseStatusName = '${caseStatus.name}' + '(不變)';
				}
			</c:forEach>
		}
		//Task #2542
		if((actionId == '${caseActionAttr.ADD_RECORD.code }') || (actionId == '${caseActionAttr.DISPATCHING.code }') || actionId == ('${caseActionAttr.AUTO_DISPATCHING.code }') 
					|| (actionId == '${caseActionAttr.RUSH_REPAIR.code }') || (actionId == '${caseActionAttr.CHANGE_CASE_TYPE.code }')
					|| (actionId == '${caseActionAttr.CHANGE_COMPLETE_DATE.code }') || (actionId == '${caseActionAttr.CHANGE_CREATE_DATE.code }')
					|| (actionId == '${caseActionAttr.PAYMENT.code }')|| (actionId == '${caseActionAttr.ARRIVAL_INSPECTION.code }') || (actionId == '${caseActionAttr.DISTRIBUTION.code }')){
			<c:forEach var="caseStatus" items="${caseStatusList}" varStatus="itemStatus">
				if((caseStatus == '${caseStatusAttr.WAIT_DISPATCH.code }') && ('${caseStatusAttr.WAIT_DISPATCH.code }' == '${caseStatus.value}')){
					if((actionId == '${caseActionAttr.ADD_RECORD.code }') || (actionId == '${caseActionAttr.CHANGE_CASE_TYPE.code }')
							|| (actionId == '${caseActionAttr.RUSH_REPAIR.code }') || (actionId == '${caseActionAttr.PAYMENT.code }')|| (actionId == '${caseActionAttr.ARRIVAL_INSPECTION.code }')
							|| (actionId == '${caseActionAttr.DISTRIBUTION.code }')){
						caseStatusName = '${caseStatus.name}' + '(不變)';
					} else {
						caseStatusName = '已派工';
						tempFlag = true;
						tempStatus = '${caseStatusAttr.DISPATCHED.code }';
					}
				} else {
					if(caseStatus == '${caseStatus.value}'){
						caseStatusName = '${caseStatus.name}' + '(不變)';
					}
				}
			</c:forEach>
			if(tempFlag){
				caseStatus = tempStatus;
			}
			// 完修狀態顯示
		} else if(actionId == '${caseActionAttr.COMPLETE.code }'){
			caseStatusName = '已完修';
			caseStatus = '${caseStatusAttr.COMPLETED.code }';
			//回應狀態顯示
		} else if(actionId == '${caseActionAttr.RESPONSE.code }'){
			caseStatusName = '已回應';
			caseStatus = '${caseStatusAttr.RESPONSED.code }';
			//到場狀態顯示
		} else if(actionId == '${caseActionAttr.ARRIVE.code }'){
			if (caseStatus == '${caseStatusAttr.RESPONSED.code }' || caseStatus == '${caseStatusAttr.DELAYING.code }') {
				caseStatusName = '已到場';
				caseStatus = '${caseStatusAttr.ARRIVED.code }';
			} else if (caseStatus == '${caseStatusAttr.ARRIVED.code }') {
				caseStatusName = '已到場(不變)';
				caseStatus = '${caseStatusAttr.ARRIVED.code }';
			} else {
				caseStatusName = '完修(不變)';
				caseStatus = '${caseStatusAttr.COMPLETED.code }';
			}
			// 退回狀態顯示
		} else if(actionId == '${caseActionAttr.RETREAT.code }'){
		//	caseStatusName = '完修';
		//	caseStatus = '${caseStatusAttr.COMPLETED.code }';
			
			// Task #3113 完修退回至客服
			if (caseStatus == '${caseStatusAttr.COMPLETED.code }') {
				caseStatusName = '已派工';
				caseStatus = '${caseStatusAttr.DISPATCHED.code }';
			} else {
				caseStatusName = '完修';
				caseStatus = '${caseStatusAttr.COMPLETED.code }';
			}
			//延期狀態顯示   //Task #3123 延期也可以延期
		} else if(actionId == '${caseActionAttr.DELAY.code }'){
			if (caseStatus == '${caseStatusAttr.DELAYING.code }') {
				caseStatusName = '延期中(不變)';
			} else {
				caseStatusName = '延期中';
			}
			caseStatus = '${caseStatusAttr.DELAYING.code }';
		} else if(actionId == '${caseActionAttr.CLOSED.code }'){
			caseStatusName = '結案';
			caseStatus = '${caseStatusAttr.CLOSED.code }';
			// 線上排除狀態顯示
		} else if((actionId == '${caseActionAttr.ONLINE_EXCLUSION.code }') || (actionId == '${caseActionAttr.SIGN.code }')){
			if(($("#hideLeaseSignFlag").val()=='Y') && (actionId == '${caseActionAttr.ONLINE_EXCLUSION.code }')){
				caseStatusName = '結案';
				caseStatus = '${caseStatusAttr.CLOSED.code }';
			}else if((actionId == '${caseActionAttr.ONLINE_EXCLUSION.code }') 
					&& (allParams.caseCategory == '${caseCategoryAttr.REPAIR.code }')
					&& (allParams.cmsCase == '${iatomsContants.PARAM_YES }')){
				caseStatusName = '結案';
				caseStatus = '${caseStatusAttr.CLOSED.code }';
			}else{
				caseStatusName = '待結案審查';
				caseStatus = '${caseStatusAttr.WAIT_CLOSE.code }';
			}
			//立即結案狀態顯示
		} else if(actionId == '${caseActionAttr.IMMEDIATELY_CLOSING.code }'){
			caseStatusName = '協調完成';
			caseStatus = '${caseStatusAttr.IMMEDIATE_CLOSE.code }';
		} 
		datagridColums.push({
			field: 'description', title: '處理說明', halign:'center', width: 400,formatter: function (value, row, index) {
				var textArea = "<textarea class='textbox-dealDescription easyui-textbox' id='dealDescription' maxlength='200' style='border-radius: 4px; border: #95B8E7 1px solid;height: 60px; width: 98%' data-options='multiline:true' ></textarea>";
			//	var textArea = "<textarea class='textbox-dealDescription easyui-textbox' id='dealDescription' maxlength='200' style='height: 60px; width: 98%' data-options='multiline:true' ></textarea>";
				return textArea;
			} 
		});
		if (actionId != ('${caseActionAttr.CHANGE_COMPLETE_DATE.code }') && actionId != ('${caseActionAttr.CHANGE_CREATE_DATE.code }')) {
			datagridColums.push({
				field: 'dealDate', title: '實際執行時間', halign:'center', width: 170,
				editor: {
					type: 'datetimebox',
					options: {
						validType:'dateTime',
						invalidMessage:'時間格式限YYYY/MM/DD HH:mm',
						showSeconds : false
					}
				}
			});
			datagridColums.push({
				field: 'report', title: '通知', width: 120, halign:'center', align: 'center', 
				formatter: function (value, row, index) {
					if (row.editing) {
						var mail = '<a href="javascript:void(0)" style= "color:blue" onclick="notice(\''+actionId+"','"+datagridId+'\')">通知</a> ';
						return mail;
					}
				}
			});
		}
		if(flag) {
			datagridColums.push({
				//擴增width大小，由 120to200 update by tony
				field: 'actionCase', title: '執行', halign:'center', width: 200, align: 'center',
				formatter: function (value, row, index) {
					if (row.editing) {
						var s = '<a href="javascript:void(0)" style= "color:blue" onclick="saveAction(\''+datagridId+"','"+flag+"','"+ encodeURIComponent(caseId)+"','"+ caseCategory +"','"+ oldCaseType + '\');">儲存</a> ';
						//Task #2987
						var c = '<a href="javascript:void(0)" style= "color:blue" onclick="deleteAction(\''+datagridId+"','"+ '${contextPath}' + "','" + panelId+ '\');">取消</a> ';
						if(caseCategory == 'INSTALL' && caseStatus == 'Dispatched' && actionId=='dispatching'){
							var t = '<a href="javascript:void(0)" style= "color:blue" onclick="tmsprocess(\''+caseId+ '\');">TMS參數匯入</a> ';
							return s + c + t ;
					    }else{
							return s + c ;
						}
					}
				}
			});
		} else {
			datagridColums.push({
				//擴增width大小，由 120to200 update by tony
				field: 'actionCase', title: '執行', halign:'center', width: 200, align: 'center',
				formatter: function (value, row, index) {
					if (row.editing) {
						var s = '<a href="javascript:void(0)" style= "color:blue" onclick="saveAction(\''+datagridId+'\');">儲存</a> ';
						//Task #2987
						var c = '<a href="javascript:void(0)" style= "color:blue" onclick="deleteAction(\''+datagridId+"','"+ '${contextPath}' + "','" + panelId+ '\');">取消</a> ';
						if(caseCategory == 'INSTALL' && caseStatus == 'Dispatched' && actionId=='dispatching'){
							var t = '<a href="javascript:void(0)" style= "color:blue" onclick="tmsprocessMu(\''+datagridId+ '\');">TMS參數匯入</a> ';
							return s + c + t ;
					    }else{
							return s + c ;
						}
					}
				}
			});
		}
		$('#' + datagridId).datagrid({
			title: title,
			columns: [datagridColums],
			onBeforeEdit: dgBeforeEdit,
			onAfterEdit: dgAfterEdit,
			onLoadSuccess: function(data){
				$('#' + datagridId).datagrid();
			},
			onCancelEdit: dgCancelEdit
		});
		// 狀態
		editRow.caseStatus = caseStatus;
		// 執行后狀態
		editRow.afterCaseStatus = caseStatusName;
		rows.push(editRow);
		resultData.rows = rows;
		var options = $('#' + datagridId).datagrid('options');
		//結束編輯後置動作
		options.onEndEdit = function onEndEditDlg(index, row){
			if(actionId == ('${caseActionAttr.DISPATCHING.code }')){
				var dispatchUnit = $("#" + datagridId).datagrid('getEditor', {
					   index : 0,
					   field : 'dispatchUnit'
					});
				row.dispatchUnitName = $(dispatchUnit.target).combobox('getText');
				var dealById = $("#" + datagridId).datagrid('getEditor', {
					   index : 0,
					   field : 'dealById'
					});
				row.dealByIdName = $(dealById.target).combobox('getText'); 
				if (row.dealByIdName == '請選擇') {
					row.dealByIdName = '';
				}
			}
			if(actionId == ('${caseActionAttr.CHANGE_CASE_TYPE.code }')){
				var caseType = $("#" + datagridId).datagrid('getEditor', {
					   index : 0,
					   field : 'caseType'
					});
				row.caseTypeName = $(caseType.target).combobox('getText');
			}
			if((actionId == '${caseActionAttr.ONLINE_EXCLUSION.code }') || (actionId == '${caseActionAttr.CLOSED.code }')
					|| (actionId == '${caseActionAttr.SIGN.code }')){
				// 線上排除、結案、簽收 
				if((actionId == '${caseActionAttr.ONLINE_EXCLUSION.code }' && caseCategory == '${caseCategoryAttr.CHECK.code}') || (actionId == '${caseActionAttr.SIGN.code }' && caseCategory == '${caseCategoryAttr.CHECK.code}')){
					var checkResult = $("#" + datagridId).datagrid('getEditor', {
						   index : 0,
						   field : 'checkResult'
						});
					row.checkResultName = $(checkResult.target).combobox('getText');
					// 調整查核案件簽收問題 線上排除得到處理工程師信息
					if(actionId == '${caseActionAttr.ONLINE_EXCLUSION.code }'){
						var agentsResult = $("#" + datagridId).datagrid('getEditor', {
							   index : 0,
							   field : 'agentsId'
							});
						row.agentsName = $(agentsResult.target).combobox('getText');
						row.agentsId = $(agentsResult.target).combobox('getValue');
					}
				}
				if(caseCategory == '${caseCategoryAttr.REPAIR.code}'){
					var problemReason = $("#" + datagridId).datagrid('getEditor', {
						   index : 0,
						   field : 'problemReason'
						});
					row.problemReasonName = $(problemReason.target).combobox('getText');
					
					var problemSolution = $("#" + datagridId).datagrid('getEditor', {
						   index : 0,
						   field : 'problemSolution'
						});
					row.problemSolutionName = $(problemSolution.target).combobox('getText');
					
					var responsibity = $("#" + datagridId).datagrid('getEditor', {
						   index : 0,
						   field : 'responsibity'
						});
					row.responsibityName = $(responsibity.target).combobox('getText');
				}
			}
		};
		resultData.total = 1;
		$('#' + datagridId).datagrid('loadData', resultData);
		//Task #2542
		if(actionId == ('${caseActionAttr.DISPATCHING.code }') || actionId == ('${caseActionAttr.AUTO_DISPATCHING.code }')){
			$("#nextActivitiToMail").val("被指派的廠商;被指派的廠商部門;被指派的工程師;被指派的角色");
		//簽收
		//Task #2512 完修：建案之客服；建案AO人員 補上 線上排除、立即結案 也要，文件一併調整
		//Task #2514 建案AO人員 不要了
		} else if(actionId == ('${caseActionAttr.COMPLETE.code }')||actionId == ('${caseActionAttr.ONLINE_EXCLUSION.code }')||actionId == ('${caseActionAttr.IMMEDIATELY_CLOSING.code }')) {
			$("#nextActivitiToMail").val("建案之客服");
		}else if(actionId == ('${caseActionAttr.RETREAT.code }')) {
			$("#nextActivitiToMail").val("被指派的廠商部門;被指派的工程師");
		} else if(actionId == ('${caseActionAttr.RUSH_REPAIR.code }')) {
			$("#nextActivitiToMail").val("被指派的客服;被指派的工程師;被指派的廠商部門");
		} else {
			$("#nextActivitiToMail").val("");
		}
		var nextActivitiToMail = $("#nextActivitiToMail").val();
		$("#toMail").val(nextActivitiToMail);
		var params = {
			toMail : nextActivitiToMail
		}
		$("#saveEmailParamer").val(JSON.stringify(params));
		$("#tempToMail").val("");
	}

	 /*
	* 列印工單
	*/
    function exportData(name, value, flag,id){
    	var isInstant = $("#isInstant").prop("checked");
   		var caseId = "";
    	if(flag) {
    		caseId = id;
    	} else {
	    	var row = $('#dg').datagrid('getSelected');
			if(row == null){
				//$("#dgResponse-msg").text("請勾選資料");
				$.messager.alert('提示訊息','請勾選資料','warning');
				return false;
			} else {
				var selectRow = $("#dg").datagrid('getSelections');
			//	var noTidIndex = "案件編號";	
				for(var i =0; i<selectRow.length; i++){
					// Task #2721 拆機 查核 專案 報修 這幾種類別，不需要檢核 所有類別都要能列印
					/* if (selectRow[i].tid == '' || selectRow[i].tid == null) {
						noTidIndex = noTidIndex + selectRow[i].caseId + ",";
					} */
					if(caseId == "") {
						caseId += selectRow[i].caseId;
					} else {
						caseId += "," + selectRow[i].caseId;
					}
				}
				// Task #2721 拆機 查核 專案 報修 這幾種類別，不需要檢核 所有類別都要能列印
/* 				if (noTidIndex.length > 4) {
					$.messager.alert('提示訊息', noTidIndex.substring("0", noTidIndex.length-1) + '，未設定交易參數，不可進行列印', 'warning');
					return false;
				} */
				//var exportParam = {
						//caseId : caseId,
						//actionId : 'export'
				//};
			}
    	}
		$("#msg").empty();
		var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		if(flag){
			// 調保存遮罩
			commonSaveLoading('editDlg');
		} else {
			$.blockUI(blockStyle);
		}
		if(name == '<%=IAtomsConstants.CARD_READER_PARAMETER%>') {
			createSubmitForm("${contextPath}/caseHandle.do","actionId=<%=IAtomsConstants.ACTION_EXPORT_CARD_READER%>&caseId="+encodeURIComponent(caseId)+"&templatesName="+name+"&templatesId="+value+"&isInstant="+isInstant,"post");
		} else {
			createSubmitForm("${contextPath}/caseHandle.do","actionId=<%=IAtomsConstants.ACTION_EXPORT%>&caseId="+encodeURIComponent(caseId)+"&templatesName="+name+"&templatesId="+value+"&isInstant="+isInstant,"post");
		}
		
		ajaxService.getExportFlag('<%=IAtomsConstants.UC_NO_SRM_05020 %>',function(data){
			// $.unblockUI();
			if(flag){
				// 調取消保存遮罩
				commonCancelSaveLoading('editDlg');
			} else {
				$.unblockUI();
			}
		});
	}

	//匯入返回處理
	function onComplete(id, fileName, response, maybeXhrOrXdr) {
		$.unblockUI();
		if (maybeXhrOrXdr) {
			if (!sessionTimeOut(maybeXhrOrXdr)) {
				return false;
			}
		}
		$(".qq-upload-list-selector").html("");
		if (response.success) {
			if($('#caseCategory').hasClass("easyui-combobox")){
				$("#caseCategory").combobox("setValue", "");
			}
			if($('#uploadCaeInfo').hasClass("easyui-panel")){
				$('#uploadCaeInfo').panel('close');
			} else {
				$("#uploadCaeInfo").addClass("easyui-panel");
				$("#uploadCaeInfo").attr("style", "width: 98%; height: auto;");
				$("#uploadCaeInfo").panel();
			}
			//Task #3269
			if($('#uploadCaseEnd').hasClass("easyui-panel")){
				$('#uploadCaseEnd').panel('close');
			}
			var pageIndex = getGridCurrentPagerIndex("dg");
			query(pageIndex, false);
			$("#dgResponse-msg").text(response.msg);
		} else {
			if (response.errorFileName != null){
				$.messager.confirm('確認對話框','匯入失敗，是否下載匯入錯誤檔？', function(result){
					if (result) {
						createSubmitForm("${contextPath}/caseHandle.do","actionId=<%=IAtomsConstants.ACTION_DOWNLOAD_ERROR_FILE %>&errorFileName=" + response.errorFileName + "&errorFilePath=" + response.errorFilePath,"post");
					}
				});
			} else {
				$("#dgResponse-msg").text(response.msg);
			}
		} 
	}
	/*
	* 生成案件匯入模塊樣式
	*/
	function createUploadStyle(){
		if(!$('#caseCategory').hasClass("easyui-combobox")){
			$('#caseCategory').addClass("easyui-combobox");
			$('#caseCategory').combobox();
			$('#btnUpload').linkbutton();
			$('#btnCancle').linkbutton();
		}
	}
	/*
	* 生成結案匯入模塊樣式Task #3269
	*/
	function showCaseEndImportView(){
		$('#dgResponse-msg').html('');
		if($("#response").hasClass("easyui-panel")){
			$('#dgResponse').closest('.easyui-panel').panel('close');
		} else {
			$("#response").addClass("easyui-panel");
			$("#response").attr("style", "width: 98%; height: auto;");
			$("#response").panel();
			$("#dgResponse").addClass("easyui-datagrid");
			$("#dgResponse").datagrid();
			$("#response").children(':first').attr("style", "display:none;");
		}
		if($('#uploadCaeInfo').hasClass("easyui-panel")){
			$('#uploadCaeInfo').panel('close');
		}
		if($('#uploadCaseEnd').hasClass("easyui-panel")){
			$('#uploadCaseEnd').panel('open');
		} else {
			$("#uploadCaseFiled").find(".qq-upload-button").css("top","0px");    
			$("#uploadCaseEnd").addClass("easyui-panel");
			$("#uploadCaseEnd").attr("style", "width: 98%; height: auto;");
			$("#uploadCaseEnd").panel({closable:true});
			$('#uploadCaseEnd').panel('open');
		}
	}
	//Task #3269 結案匯入返回處理
	function onCompleteByCaseEnd(id, fileName, response, maybeXhrOrXdr) {
		$.unblockUI();
		if (maybeXhrOrXdr) {
			if (!sessionTimeOut(maybeXhrOrXdr)) {
				return false;
			}
		}
		$(".qq-upload-list-selector").html("");
		if (response.success) {
			if($('#uploadCaseEnd').hasClass("easyui-panel")){
				$('#uploadCaseEnd').panel('close');
			} else {
				$("#uploadCaseEnd").addClass("easyui-panel");
				$("#uploadCaseEnd").attr("style", "width: 98%; height: auto;");
				$("#uploadCaseEnd").panel();
			}
			var pageIndex = getGridCurrentPagerIndex("dg");
			query(pageIndex, false);
			$("#dgResponse-msg").text(response.msg);
		} else {
			if (response.errorFileName != null){
				$.messager.confirm('確認對話框','匯入失敗，是否下載匯入錯誤檔？', function(result){
					if (result) {
						createSubmitForm("${contextPath}/caseHandle.do","actionId=<%=IAtomsConstants.ACTION_DOWNLOAD_ERROR_FILE %>&errorFileName=" + response.errorFileName + "&errorFilePath=" + response.errorFilePath,"post");
					}
				});
			} else {
				$("#dgResponse-msg").text(response.msg);
			}
		} 
	}
	</script>
</body>
</html>
