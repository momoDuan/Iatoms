<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	CaseManagerFormDTO formDTO = null;
	SrmCaseHandleInfoDTO srmCaseHandleInfoDTO = null;
	String ucNo = null;
	String actionId = null;
	List<String> userRights = null;
	if (ctx != null) {
		formDTO = (CaseManagerFormDTO) ctx.getResponseResult();
		if (formDTO != null) {
			// 获得UseCaseNo
			ucNo = formDTO.getUseCaseNo();
			srmCaseHandleInfoDTO = formDTO.getSrmCaseHandleInfoDTO();
			actionId = formDTO.getActionId();
			userRights = StringUtils.toList(logonUser.getAccRghts().get(ucNo), ",");
		} else {
			ucNo = IAtomsConstants.UC_NO_SRM_05020;
			formDTO = new CaseManagerFormDTO();
			srmCaseHandleInfoDTO = new SrmCaseHandleInfoDTO();
			userRights = new ArrayList<String>();
		}
	} else {
		ucNo = IAtomsConstants.UC_NO_SRM_05020;
		formDTO = new CaseManagerFormDTO();
		srmCaseHandleInfoDTO = new SrmCaseHandleInfoDTO();
		userRights = new ArrayList<String>();
	}
	//客戶下拉菜單
	List<Parameter> customerList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_CUSTOMER_LIST);
	
	//獲取維護廠商下拉菜單
	List<Parameter> allCompanyList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_ALL_COMPANY_LIST);
	//處理方式
	List<Parameter> handledTypeList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.PROCESS_TYPE.getCode());
	//案件類型
	List<Parameter> ticketModeList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.TICKET_MODE.getCode());
	//裝機類別
	List<Parameter> installTypeList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.INSTALL_TYPE.getCode());
	//是否下拉框
	List<Parameter> yesOrNoList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.YES_OR_NO.getCode());
	//雙模組模式
	List<Parameter> doubleModuleList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.DOUBLE_MODULE.getCode());
	//ECR連線
	List<Parameter> ecrLineList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.ECR_LINE.getCode());
	//拆機類型
	List<Parameter> uninstallTypeList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.UNINSTALL_TYPE.getCode());
	//報修原因
	/* List<Parameter> repairReasonList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.REPAIR_REASON.getCode()); */
	List<Parameter> repairReasonList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, CaseManagerFormDTO.REPAIR_REASON_LIST);
	List<Parameter> repairReasonTaiXinList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, CaseManagerFormDTO.REPAIR_REASON_TAIXIN_LIST);
	//通訊模式
	List<Parameter> commModeList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.COMM_MODE.getCode());
	//交易參數項目列表
	List<Parameter> transationParameterItemList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_TRANSACTION_PARAMETER_ITEM_LIST);
	//交易類別
	String transationCategoryList = (String)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.TRANSACTION_CATEGORY.getCode());
	//獲取交易參數可以編輯的列名，以交易參數分組
	String editFildsMap = (String)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_TRANSACTION_PARAMETER_EDIT_FIELDS_MAP);
	//縣市下拉框
	List<Parameter> locationList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.LOCATION.getCode());
	//寬頻連接下拉框
	List<Parameter> netVendorList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.NET_VENDOR.getCode());
	// 支援功能json字符串
	String supportedFunStr = (String)SessionHelper.getAttribute(request, ucNo, formDTO.PARAM_SUPPORTED_FUNCTION_LIST_STR);
	//工單範本集合
	List<Parameter> templatesList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_TEMPLATES_LIST);
	//Logo
	List<Parameter> logos = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.CASE_LOGO.getCode());
	//获取上传最大限制
	String uploadFileSize = formDTO.getUploadFileSize();
	Parameter cardReader = new Parameter();
	//往工單列印的下拉框裡面添加刷卡機參數表的值
	cardReader.setValue(IAtomsConstants.REPORT_NAME_EDC_CONTRACT_EXPIRE_INFO_REPORT);
	cardReader.setName(IAtomsConstants.CARD_READER_PARAMETER);
	if(IAtomsConstants.ACTION_INIT_EDIT.equals(actionId)) {
		templatesList.add(cardReader);
	}
	
	
	// 編輯頁面取值
	/*客戶聯動合約處理*/
	List<Parameter> editContractList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_CONTRACT_LIST);
	/*合約維護廠商處理*/
	List<Parameter> editVendors = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_VENDORS);
	/*維護廠商部門處理*/
	List<Parameter> editDeptList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_DEPT_LIST);
	/*特店特店表頭*/
	List<Parameter> editMerchantHeaders = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_MERCHANT_HEADERS);
	/*客戶刷卡機型處理*/
	List<Parameter> editEdcAssets = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_EDC_ASSETS);
	/*客戶聯動軟體版本處理*/
	List<Parameter> editSoftwareVersions = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_SOFTWARE_VERSIONS);
	/*客戶聯動周邊設備處理*/
	List<Parameter> editPeripheralsList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_PERIPHERALS_LIST);
	
	/*刷卡機型聯動內建功能處理*/
	List<Parameter> editBuiltInFeatures = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_BUILT_IN_FEATURES);
	/*刷卡機型聯動連線方式處理*/
	List<Parameter> editConnectionTypes = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_CONNECTION_TYPES);
	/*周邊設備1聯動周邊設備1功能處理*/
	List<Parameter> editPeripheralsFunctions = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_PERIPHERALS_FUNCTIONS);
	/*周邊設備2聯動周邊設備2功能處理*/
	List<Parameter> editPeripheralsFunction2s = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_PERIPHERALS_FUNCTION2S);
	/*周邊設備3聯動周邊設備3功能處理*/
	List<Parameter> editPeripheralsFunction3s = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_PERIPHERALS_FUNCTION3S);
	/*縣市聯動郵遞區號處理(裝機)*/
	List<Parameter> installedPostCodes = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_INSTALLED_POST_CODE_LIST);
	/*縣市聯動郵遞區號處理(聯系)*/
	List<Parameter> contactPostCodes = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_CONTACT_POST_CODE_LIST);
	/*縣市聯動郵遞區號處理(營業)*/
	List<Parameter> locationPostCodes = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_LOCATION_POST_CODE_LIST);
	// 客戶權限下第一筆有設備的合約
	String editContractId = (String)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_EDIT_CONTRACT_ID);
	
	// Receipt_type
	List<Parameter> receiptTypes= (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.RECEIPT_TYPE.getCode());
%>
<c:set var="formDTO" value="<%=formDTO %>" scope="page"></c:set>
<c:set var="customerList" value="<%=customerList %>" scope="page"></c:set>
<c:set var="allCompanyList" value="<%=allCompanyList %>" scope="page"></c:set>
<c:set var="handledTypeList" value="<%=handledTypeList %>" scope="page"></c:set>
<c:set var="ticketModeList" value="<%=ticketModeList %>" scope="page"></c:set>
<c:set var="installTypeList" value="<%=installTypeList %>" scope="page"></c:set>
<c:set var="yesOrNoList" value="<%=yesOrNoList %>" scope="page"></c:set>
<c:set var="doubleModuleList" value="<%=doubleModuleList %>" scope="page"></c:set>
<c:set var="ecrLineList" value="<%=ecrLineList %>" scope="page"></c:set>
<c:set var="uninstallTypeList" value="<%=uninstallTypeList %>" scope="page"></c:set>
<c:set var="repairReasonList" value="<%=repairReasonList %>" scope="page"></c:set>
<c:set var="commModeList" value="<%=commModeList %>" scope="page"></c:set>
<c:set var="transationParameterItemList" value="<%=transationParameterItemList %>" scope="page"></c:set>
<c:set var="transationCategoryList" value="<%=transationCategoryList %>" scope="page"></c:set>
<c:set var="editFildsMap" value="<%=editFildsMap %>" scope="page"></c:set>
<c:set var="locationList" value="<%=locationList %>" scope="page"></c:set>
<c:set var="netVendorList" value="<%=netVendorList %>" scope="page"></c:set>
<c:set var="srmCaseHandleInfoDTO" value="<%=srmCaseHandleInfoDTO %>" scope="page"></c:set>
<c:set var="supportedFunStr" value="<%=supportedFunStr %>" scope="page"></c:set>
<c:set var="stuff" value="case" scope="page"></c:set>
<c:set var="templatesList" value="<%=templatesList %>" scope="page"></c:set>
<c:set var="logos" value="<%=logos %>" scope="page"></c:set>
<c:set var="uploadFileSize" value="<%=uploadFileSize%>" scope="page"></c:set> 


<c:set var="editContractList" value="<%=editContractList%>" scope="page"></c:set>
<c:set var="editVendors" value="<%=editVendors%>" scope="page"></c:set> 
<c:set var="editDeptList" value="<%=editDeptList%>" scope="page"></c:set>
<c:set var="editMerchantHeaders" value="<%=editMerchantHeaders%>" scope="page"></c:set>
<c:set var="editEdcAssets" value="<%=editEdcAssets%>" scope="page"></c:set>
<c:set var="editSoftwareVersions" value="<%=editSoftwareVersions%>" scope="page"></c:set>
<c:set var="editPeripheralsList" value="<%=editPeripheralsList%>" scope="page"></c:set>

<c:set var="editBuiltInFeatures" value="<%=editBuiltInFeatures%>" scope="page"></c:set>
<c:set var="editConnectionTypes" value="<%=editConnectionTypes%>" scope="page"></c:set>
<c:set var="editPeripheralsFunctions" value="<%=editPeripheralsFunctions%>" scope="page"></c:set>
<c:set var="editPeripheralsFunction2s" value="<%=editPeripheralsFunction2s%>" scope="page"></c:set>
<c:set var="editPeripheralsFunction3s" value="<%=editPeripheralsFunction3s%>" scope="page"></c:set>
<c:set var="editContractId" value="<%=editContractId%>" scope="page"></c:set>
<c:set var="userRights" value="<%=userRights%>" scope="page"></c:set>
<c:set var="repairReasonTaiXinList" value="<%=repairReasonTaiXinList%>" scope="page"></c:set>
<c:set var="installedPostCodes" value="<%=installedPostCodes%>" scope="page"></c:set>
<c:set var="contactPostCodes" value="<%=contactPostCodes%>" scope="page"></c:set>
<c:set var="locationPostCodes" value="<%=locationPostCodes%>" scope="page"></c:set>

<c:set var="receiptTypes" value="<%=receiptTypes%>" scope="page"></c:set>
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$CASE_CATEGORY" var="caseCategoryAttr" />
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants" var="iatomsContants" />
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>案件資料</title>
	<script type="text/javascript" src="${contextPath}/js/fineuploader-5.0.2.js"></script>
	<link rel="stylesheet" href="${contextPath}/css/fineuploader-5.0.2.css"/>
</head>
<body>
	<div style="width: auto; height: auto; padding: 10px 10px; overflow-y: hidden" data-options="region:'center'" class="topSoller">
		<form id="caseDetailForm" >
			<div><span id="caseDialogMsg" class="red"></span></div>
			<%-- 案件資訊 --%>
			<tiles:insertAttribute name="caseInformation">
				<tiles:putAttribute name="caseManagerFormDTO" value="${formDTO }"></tiles:putAttribute>
				<tiles:putAttribute name="customers" value="${customerList }"></tiles:putAttribute>
				<tiles:putAttribute name="ticketModes" value="${ticketModeList }"></tiles:putAttribute>
				<tiles:putAttribute name="installTypes" value="${installTypeList }"></tiles:putAttribute>
				<tiles:putAttribute name="handledTypes" value="${handledTypeList }"></tiles:putAttribute>
				<tiles:putAttribute name="uninstallTypes" value="${uninstallTypeList }"></tiles:putAttribute>
				<tiles:putAttribute name="repairReasons" value="${repairReasonList }"></tiles:putAttribute>
				<tiles:putAttribute name="repairReasonTaiXins" value="${repairReasonTaiXinList }"></tiles:putAttribute>
				<tiles:putAttribute name="isVipList" value="${yesOrNoList }"></tiles:putAttribute>
				<tiles:putAttribute name="allVendorList" value="${allCompanyList }"></tiles:putAttribute>
				<tiles:putAttribute name="caseHandleInfoDTO" value="${srmCaseHandleInfoDTO }"></tiles:putAttribute>
				
				
				<tiles:putAttribute name="editContractList" value="${editContractList }"></tiles:putAttribute>
				<tiles:putAttribute name="editVendors" value="${editVendors }"></tiles:putAttribute>
				<tiles:putAttribute name="editDeptList" value="${editDeptList }"></tiles:putAttribute>
				<tiles:putAttribute name="editContractId" value="${editContractId }"></tiles:putAttribute>
				<tiles:putAttribute name="contextPathCaseInfo" value="${contextPath }"></tiles:putAttribute>
			</tiles:insertAttribute>
			<!-- 特店資料 -->
			<tiles:insertAttribute name="merchantInfo">
				<tiles:putAttribute name="caseManagerFormDTO" value="${formDTO }"></tiles:putAttribute>
				<tiles:putAttribute name="caseCategory" value="${formDTO.caseCategory }"></tiles:putAttribute>
				<tiles:putAttribute name="isVipList" value="${yesOrNoList }"></tiles:putAttribute>
				<tiles:putAttribute name="locationList" value="${locationList }"></tiles:putAttribute>
				<tiles:putAttribute name="caseHandleInfoDTO" value="${srmCaseHandleInfoDTO }"></tiles:putAttribute>
				
				
				<tiles:putAttribute name="editMerchantHeaders" value="${editMerchantHeaders }"></tiles:putAttribute>
				<tiles:putAttribute name="contextPathMerchant" value="${contextPath }"></tiles:putAttribute>
				
				<tiles:putAttribute name="installedPostCodes" value="${installedPostCodes }"></tiles:putAttribute>
				<tiles:putAttribute name="contactPostCodes" value="${contactPostCodes }"></tiles:putAttribute>
				<tiles:putAttribute name="locationPostCodes" value="${locationPostCodes }"></tiles:putAttribute>
			</tiles:insertAttribute>
			<!-- EDC資訊 -->
			<tiles:insertAttribute name="edcInformation">
				<tiles:putAttribute name="caseManagerFormDTO" value="${formDTO }"></tiles:putAttribute>
				<tiles:putAttribute name="caseCategory" value="${formDTO.caseCategory }"></tiles:putAttribute>
				<tiles:putAttribute name="connectionTypes" value="${commModeList }"></tiles:putAttribute>
				<tiles:putAttribute name="multiModules" value="${doubleModuleList }"></tiles:putAttribute>
				<tiles:putAttribute name="ecrConnections" value="${ecrLineList }"></tiles:putAttribute>
				<tiles:putAttribute name="netVendorList" value="${netVendorList }"></tiles:putAttribute>
				<tiles:putAttribute name="caseHandleInfoDTO" value="${srmCaseHandleInfoDTO }"></tiles:putAttribute>
				<tiles:putAttribute name="supportedFunStr" value="${supportedFunStr }"></tiles:putAttribute>
				<tiles:putAttribute name="yesOrNoList" value="${yesOrNoList }"></tiles:putAttribute>
				<tiles:putAttribute name="logos" value="${logos }"></tiles:putAttribute>
				
				
				<tiles:putAttribute name="editEdcAssets" value="${editEdcAssets }"></tiles:putAttribute>
				<tiles:putAttribute name="editPeripheralsList" value="${editPeripheralsList }"></tiles:putAttribute>
				<tiles:putAttribute name="editSoftwareVersions" value="${editSoftwareVersions }"></tiles:putAttribute>
				<tiles:putAttribute name="editBuiltInFeatures" value="${editBuiltInFeatures }"></tiles:putAttribute>
				<tiles:putAttribute name="editConnectionTypes" value="${editConnectionTypes }"></tiles:putAttribute>
				<tiles:putAttribute name="editPeripheralsFunctions" value="${editPeripheralsFunctions }"></tiles:putAttribute>
				<tiles:putAttribute name="editPeripheralsFunction2s" value="${editPeripheralsFunction2s }"></tiles:putAttribute>
				<tiles:putAttribute name="editPeripheralsFunction3s" value="${editPeripheralsFunction3s }"></tiles:putAttribute>
				<tiles:putAttribute name="contextPath" value="${contextPath }"></tiles:putAttribute>
				<tiles:putAttribute name="receiptTypes" value="${receiptTypes }"></tiles:putAttribute>
				<tiles:putAttribute name="contextPath" value="${contextPath}"></tiles:putAttribute>
			</tiles:insertAttribute>
			<!-- 案件附加資料 -->
			<tiles:insertAttribute name="caseAdditionalInfo">
				<tiles:putAttribute name="customers" value="${customerList }"></tiles:putAttribute>
				<tiles:putAttribute name="caseManagerFormDTO" value="${formDTO }"></tiles:putAttribute>
				<tiles:putAttribute name="caseCategory" value="${formDTO.caseCategory }"></tiles:putAttribute>
				<tiles:putAttribute name="uploadFileSize" value="${uploadFileSize }"></tiles:putAttribute>
				<tiles:putAttribute name="caseHandleInfoDTO" value="${srmCaseHandleInfoDTO }"></tiles:putAttribute>
				<tiles:putAttribute name="contextPathAddition" value="${contextPath }"></tiles:putAttribute>
			</tiles:insertAttribute>
			
			<c:if test="${caseCategoryAttr.OTHER.code ne formDTO.caseCategory }">
				<!-- 交易參數 -->
				<tiles:insertAttribute name="tradingParameters">
					<tiles:putAttribute name="caseCategory" value="${formDTO.caseCategory }"></tiles:putAttribute>
					<tiles:putAttribute name="transactionParameterItemDTOs" value="${transationParameterItemList }"></tiles:putAttribute>
					<tiles:putAttribute name="editabledTradingTypes" value="${transationCategoryList }"></tiles:putAttribute>
					<tiles:putAttribute name="editabledFields" value="${editFildsMap }"></tiles:putAttribute>
					<tiles:putAttribute name="hasUpdate" value="Y"></tiles:putAttribute>
					<tiles:putAttribute name="caseHandleInfoDTO" value="${srmCaseHandleInfoDTO }"></tiles:putAttribute>
					<tiles:putAttribute name="caseManagerFormDTO" value="${formDTO }"></tiles:putAttribute>
				</tiles:insertAttribute>
			</c:if>
			<!-- 案件動作控制 -->
			<tiles:insertAttribute name="caseAction">
				<tiles:putAttribute name="caseHandleInfoDTO" value="${srmCaseHandleInfoDTO }"></tiles:putAttribute>
				<tiles:putAttribute name="templatesList" value="${templatesList }"></tiles:putAttribute>
				<tiles:putAttribute name="caseManagerFormDTO" value="${formDTO }"></tiles:putAttribute>
				<tiles:putAttribute name="userRights" value="${userRights }"></tiles:putAttribute>
				<tiles:putAttribute name="contextPathAction" value="${contextPath }"></tiles:putAttribute>
			</tiles:insertAttribute>
		</form>
	</div>
	<script type="text/javascript">
	$(function () {
		// 加載案件資訊區塊
		<c:choose>
			<c:when test="${empty srmCaseHandleInfoDTO}">
				<c:if test="${caseCategoryAttr.INSTALL.code ne formDTO.caseCategory && caseCategoryAttr.OTHER.code ne formDTO.caseCategory}">
					setTimeout("disabledForDtid('${formDTO.caseCategory}', true);",20);
				//	disabledForDtid('${caseManagerFormDTO.caseCategory}', true);
				</c:if>
			</c:when>
			<c:otherwise>
				setTimeout("dealDisabledControl('${srmCaseHandleInfoDTO.caseStatus }');",20);
			//	dealDisabledControl('${caseHandleInfoDTO.caseStatus }');
			</c:otherwise>
		</c:choose>
	//	disabledForDtid('${caseManagerFormDTO.caseCategory}', true);
		$("#process").panel({width:'99%'});
		$("#special").panel({width:'99%'});
		$("#edc").panel({width:'99%'});
		$("#trading").panel({width:'99%'});
		$("#caseAdd").panel({width:'99%'});
	});
	
		/**儲存頁面數據
		successFun:成功後的處理方法，failureFun:失敗後的處理方法
		**/
		function saveCaseDetailData(actionId,successFun,failureFun) {
			// 清空所有消息
			$("#caseDialogMsg").text("");
/* 			$("#msgCaseInfo").text("");
			$("#msgSpecial").text("");
			$("#msgEdc").text("");
			$("#caseProcedureMsg").text("");
			$("#msgTrading").text(""); */
			$("#errorMsg").html("");
			$("#dgResponse-msg").text("");
			// 保存時驗證消息彈出
			var controls = ['${stuff }_customer', '${stuff }_requirementNo', '${stuff }_contractId', '${stuff }_installType', '${stuff }_companyId','${stuff }_departmentId','${stuff }_dtid',
					'${stuff }_uninstallType', '${stuff }_repairReason', '${stuff }_caseType', '${stuff }_expectedCompletionDate',
					'${stuff }_merMid', '${stuff }_merchantHeaderId', '${stuff }_merAoName', '${stuff }_installedAdressLocation', '${stuff }_merInstallAddress',
					'${stuff }_merInstallUser', '${stuff }_merInstallPhone', '${stuff }_contactAddressLocation', '${stuff }_contactAddress',
					'${stuff }_contactUser', '${stuff }_contactUserPhone', '${stuff }_edcType', '${stuff }_softwareVersion',
					'${stuff }_ecrConnection', '${stuff }_connectionType', 'caseNumber', '${stuff}_installedPostCode', '${stuff}_contactPostCode', '${stuff }_receiptType'];
			//頁面檢核
			if(validateForm(controls) && $('#caseDetailForm').form("validate")){
				//獲取編輯畫面數據
				var saveParam = $('#caseDetailForm').form("getData");
				//其他案件類別，帶入DTID #3392
				<c:if test="${caseCategoryAttr.OTHER.code eq formDTO.caseCategory}">
					saveParam.case_dtid = $("#${stuff }_dtid").val();
				</c:if>
				//是否選擇dtid帶值 2018/01/30
				saveParam.isCheckDtidFlag = $("#isCheckDtidFlag").val();
				var isUpdate = $("#${stuff }_isUpdate").val();
			//	var isUpdateAsset = saveParam.${stuff}_isUpdateAsset;
				var isUpdateAsset = $("#${stuff }_isUpdateAsset").val();
				<c:if test="${caseCategoryAttr.INSTALL.code ne formDTO.caseCategory}">
					if (isUpdate == '' && isUpdateAsset == 'Y') {
						$("#caseDialogMsg").text("此案件DTID最新資料已更新，請點擊✔重新帶入最新資料");
						handleScrollTop('createDlg','caseDialogMsg');
						handleScrollTop('editDlg','caseDialogMsg');
						return false;
					}
				</c:if>
				<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory}">
					//處理專案欄位消息 
					if($("#${stuff }_isProject").prop('checked')){
						saveParam.${stuff }_isProject = 'Y';
					} else {
						saveParam.${stuff }_isProject = 'N';
					}
				</c:if>
				<c:if test="${caseCategoryAttr.MERGE.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory}">
					//處理	是否同裝機作業欄位消息 
					if($("#${stuff }_sameInstalled").prop('checked')){
						saveParam.${stuff }_sameInstalled = 'Y';
					} else {
						saveParam.${stuff }_sameInstalled = 'N';
					}
				</c:if>
				// 非裝機件驗證dtid是否調整
				if (('${formDTO.caseCategory }' != '${caseCategoryAttr.INSTALL.code}') && ('${formDTO.caseCategory }' != '${caseCategoryAttr.OTHER.code}')){
					var currentDtid = $("#${stuff }_dtid").textbox("getValue");
					var hiddenDtid = $("#changeDtid").val();
					if(currentDtid != hiddenDtid){
						$("#caseDialogMsg").text("DTID已變更，請重新帶入資料");
						handleScrollTop('createDlg','caseDialogMsg');
						handleScrollTop('editDlg','caseDialogMsg');
						return false;
					}
				}
				if('${formDTO.caseCategory }' != '${caseCategoryAttr.INSTALL.code}'){
					saveParam.${stuff }_installType = $("#${stuff }_installType").val();
				}
				// 裝機異動 特店資料調整
				if(('${formDTO.caseCategory }' == '${caseCategoryAttr.INSTALL.code}') 
						|| ('${formDTO.caseCategory }' == '${caseCategoryAttr.UPDATE.code}')
						|| ('${formDTO.caseCategory }' == '${caseCategoryAttr.OTHER.code}')
						|| ('${formDTO.caseCategory }' == '${caseCategoryAttr.PROJECT.code}')){
					var currentMerCode = $("#${stuff }_merMid").textbox("getValue");
					var hiddenMerCode = $("#changeMerchat").val();
					if(currentMerCode != hiddenMerCode){
						$("#caseDialogMsg").text("特店代號已變更，請重新帶入資料");
						handleScrollTop('createDlg');
						handleScrollTop('editDlg');
						return false;
					}
				}
				// 拿到特店資料的信息
				var merchantParams = getMerchantParams();
				if(merchantParams){
					// 特店代號
					saveParam.${stuff }_merchantCode = merchantParams.merchantCode;
					// 特店表頭
					saveParam.${stuff }_merchantHeaderId = merchantParams.merchantHeaderId;
					if('${formDTO.caseCategory }' == '${caseCategoryAttr.INSTALL.code}' 
						|| '${formDTO.caseCategory }'== '${caseCategoryAttr.UPDATE.code}'){
						// 裝機地址同營業地址
						saveParam.${stuff }_isBussinessAddress = merchantParams.isBussinessAddress;
						// 裝機聯絡人
						saveParam.${stuff }_installedContact = merchantParams.installedContact;
						// 裝機聯絡人同特店聯絡人
						saveParam.${stuff }_isBussinessContact = merchantParams.isBussinessContact;
						// 裝機聯絡人電話
						saveParam.${stuff }_installedContactPhone = merchantParams.installedContactPhone;
						// 裝機聯絡人電話同特店聯絡人電話
						saveParam.${stuff }_isBussinessContactPhone = merchantParams.isBussinessContactPhone;
						// 裝機聯絡人電話
						saveParam.${stuff }_installedContactPhone = merchantParams.installedContactPhone;
						// 裝機聯絡人電話同特店聯絡人電話
						saveParam.${stuff }_isBussinessContactPhone = merchantParams.isBussinessContactPhone;
						
						
						// 裝機聯絡人手機
						saveParam.${stuff }_installedContactMobilePhone = merchantParams.installedContactMobilePhone;
						// 裝機聯絡人手機同特店聯絡人行動電話
						saveParam.${stuff }_isBussinessContactMobilePhone = merchantParams.isBussinessContactMobilePhone;
						// 裝機聯絡人EMAIL
						saveParam.${stuff }_installedContactEmail = merchantParams.installedContactEmail;
						// 裝機聯絡人EMAIL同特店聯絡人EMAIL
						saveParam.${stuff }_isBussinessContactEmail = merchantParams.isBussinessContactEmail;
						//最新案件装机 异动时 更新contact字段 2018/01/15
						/* if($("#hideForUpdateContactFlag").length>0 && $("#hideForUpdateContactFlag").val() == 'Y'){
							saveParam.hideForUpdateContactFlag = 'Y';
							// 聯繫地址-縣市
							saveParam.${stuff }_contactAddressLocation = merchantParams.contactAddressLocation;
							// 聯繫地址
							saveParam.${stuff }_contactAddress = merchantParams.contactAddress;
							// 聯繫地址同特店地址
							saveParam.${stuff }_contactIsBussinessAddress = merchantParams.contactIsBussinessAddress;
							// 聯繫聯絡人
							saveParam.${stuff }_contactUser = merchantParams.contactUser;
							// 聯繫聯絡人同特店聯絡人
							saveParam.${stuff }_contactIsBussinessContact = merchantParams.contactIsBussinessContact;
							// 聯繫聯絡人電話
							saveParam.${stuff }_contactUserPhone = merchantParams.contactUserPhone;
							// 聯繫聯絡人電話同特店聯絡人電話
							saveParam.${stuff }_contactIsBussinessContactPhone = merchantParams.contactIsBussinessContactPhone;
						} */
					} else {
						//最新案件非装机 非异动时 更新install字段  2018/01/15
						/* if($("#hideForUpdateInstalledFlag").length>0 && $("#hideForUpdateInstalledFlag").val() == 'Y'){
							saveParam.hideForUpdateContactFlag = 'Y';
							// 裝機地址同營業地址
							saveParam.${stuff }_isBussinessAddress = merchantParams.isBussinessAddress;
							// 裝機聯絡人
							saveParam.${stuff }_installedContact = merchantParams.installedContact;
							// 裝機聯絡人同特店聯絡人
							saveParam.${stuff }_isBussinessContact = merchantParams.isBussinessContact;
							// 裝機聯絡人電話
							saveParam.${stuff }_installedContactPhone = merchantParams.installedContactPhone;
							// 裝機聯絡人電話同特店聯絡人電話
							saveParam.${stuff }_isBussinessContactPhone = merchantParams.isBussinessContactPhone;
							// 裝機地址-縣市
							saveParam.${stuff }_installedAdressLocation = merchantParams.installedAdressLocation;
							// 裝機地址
							saveParam.${stuff }_installedAdress = merchantParams.installedAdress;
						} */
						// 聯繫地址-縣市
						saveParam.${stuff }_contactAddressLocation = merchantParams.contactAddressLocation;
						saveParam.${stuff }_contactPostCode = merchantParams.contactPostCode;
						// 聯繫地址
						saveParam.${stuff }_contactAddress = merchantParams.contactAddress;
						// 聯繫地址同特店地址
						saveParam.${stuff }_contactIsBussinessAddress = merchantParams.contactIsBussinessAddress;
						// 聯繫聯絡人
						saveParam.${stuff }_contactUser = merchantParams.contactUser;
						// 聯繫聯絡人同特店聯絡人
						saveParam.${stuff }_contactIsBussinessContact = merchantParams.contactIsBussinessContact;
						// 聯繫聯絡人電話
						saveParam.${stuff }_contactUserPhone = merchantParams.contactUserPhone;
						// 聯繫聯絡人電話同特店聯絡人電話
						saveParam.${stuff }_contactIsBussinessContactPhone = merchantParams.contactIsBussinessContactPhone;
						
						// 聯繫聯絡人手機
						saveParam.${stuff }_contactMobilePhone = merchantParams.contactMobilePhone;
						// 聯繫聯絡人手機同特店聯絡人行動電話
						saveParam.${stuff }_contactIsBussinessContactMobilePhone = merchantParams.contactIsBussinessContactMobilePhone;
						// 聯繫聯絡人EMAIL
						saveParam.${stuff }_contactUserEmail = merchantParams.contactUserEmail;
						// 聯繫聯絡人EMAIL同特店聯絡人EMAIL
						saveParam.${stuff }_contactIsBussinessContactEmail = merchantParams.contactIsBussinessContactEmail;
					}
					// 裝機地址-縣市
					saveParam.${stuff }_installedAdressLocation = merchantParams.installedAdressLocation;
					// 裝機地址
					saveParam.${stuff }_installedAdress = merchantParams.installedAdress;
					saveParam.${stuff }_installedPostCode = merchantParams.installedPostCode;
				}
				// 得到TMS參數值
				var tmsPram = getTmsParam();
				if(tmsPram){
					saveParam.${stuff }_isTms = tmsPram;
				}
				//Task #3584 建案,修改  檢核 宣揚 需求單號 
				//此DTID台新裝機件尚未建案
				//此DTID台新裝機件已存在
				var chackdtid = null;
				if( $("#${stuff }_dtid").length>0){
					chackdtid = $("#${stuff }_dtid").textbox("getValue");
				} else if($("#${stuff }_showDtid").length>0){
					chackdtid = $("#${stuff }_showDtid").textbox("getValue");
				}
				if(!checkRequirementNo($("#${stuff }_requirementNo").textbox('getValue'), chackdtid)){
					return false;
				}
				// 拿到案件筆數及流程控制部分的值
				if(isEmpty('${srmCaseHandleInfoDTO.caseId }')){
					// 處理案件復制欄位部分值
					var caseProcedureParams = getCaseProcedureParams();
					if(caseProcedureParams){
						saveParam.${stuff }_isSingle = caseProcedureParams.isSingle;
						saveParam.${stuff }_caseNumber = caseProcedureParams.caseNumber;
					}
				} else {
					// 案件編號
					saveParam.${stuff }_caseId = $("#hideCaseId").val();
					// 案件狀態
					saveParam.${stuff }_caseStatus = $("#hideCaseStatus").val();
					
					// 案件類型
					saveParam.${stuff }_caseType = $("#${stuff }_caseType").combobox("getValue");
					// Task #3081 預計完成日，只有預約件可以改，其他不能改，將系統算出之SLA應完修時間顯示在預計完成日欄位
					// 預計完成日
					if('APPOINTMENT' == saveParam.${stuff }_caseType){
						saveParam.${stuff }_expectedCompletionDate = $("#${stuff }_expectedCompletionDate").datebox("getValue");
					} else {
						saveParam.${stuff }_expectedCompletionDate = null;
					}
					
					// 查歷史標記
					saveParam.${stuff }_isHistory = '${caseHandleInfoDTO.isHistory}';
				}
				saveParam.caseCategory = '${formDTO.caseCategory }';
				// 驗證交易參數
				if ('${formDTO.caseCategory }' != '${caseCategoryAttr.OTHER.code}') {
					if(tradingParameterValidate){
						if(!tradingParameterValidate('${formDTO.caseCategory }')){
							return false;
						}
					}
				}
				
				// 驗證EDC信息
				if('${formDTO.caseCategory }' == '${caseCategoryAttr.INSTALL.code}'
					|| '${formDTO.caseCategory }'== '${caseCategoryAttr.UPDATE.code}'
					|| '${formDTO.caseCategory }'== '${caseCategoryAttr.PROJECT.code}'){
					if(checkEDCInfo){
						if(!checkEDCInfo()){
							return false;
						}
					}
				}
				if ('${formDTO.caseCategory }' != '${caseCategoryAttr.OTHER.code}') {
					if (getTradingParametersData) {
						//獲取交易參數頁面數據。
						saveParam.caseTransactionParameters = getTradingParametersData();
					}
				}
				if(('${formDTO.caseCategory }' == '${caseCategoryAttr.INSTALL.code}')
						|| ('${formDTO.caseCategory }' == '${caseCategoryAttr.OTHER.code}')){
					// 客戶
					saveParam.${stuff }_customerId = $("#${stuff }_customer").combobox("getValue");
					saveParam.${stuff }_customerName = $("#${stuff }_customer").combobox("getText");
					// 合約編號
					saveParam.${stuff }_contractId = $("#${stuff }_contractId").combobox("getValue");
					saveParam.${stuff }_contractCode = $("#${stuff }_contractId").combobox("getText");
					// Task #3028 併機 異動 拆機 查核 專案 報修，建案之維護廠商，DTID預設帶出後要能再調整
/* 					// 維護廠商
					saveParam.${stuff }_companyId = $("#${stuff }_companyId").combobox("getValue");
					saveParam.${stuff }_companyName = $("#${stuff }_companyId").combobox("getText"); */
					// 驗證dtid
					if ('${formDTO.caseCategory }' != '${caseCategoryAttr.OTHER.code}') {
						if(isEmpty('${srmCaseHandleInfoDTO.caseId }')){
							if (dtidValidate) {
								$("#caseDialogMsg").text("");
								if (!dtidValidate(saveParam.${stuff }_customerId, saveParam.${stuff }_edcType)) {
									return false;
								}
							}
						}
					}
					// 刷卡機型
					saveParam.${stuff }_edcType = $("#${stuff }_edcType").combobox("getValue");
					// 週邊設備1
					saveParam.${stuff }_peripherals = $("#${stuff }_peripherals").combobox("getValue");
					// 週邊設備2
					saveParam.${stuff }_peripherals2 = $("#${stuff }_peripherals2").combobox("getValue");
					// 週邊設備3
					saveParam.${stuff }_peripherals3 = $("#${stuff }_peripherals3").combobox("getValue");
					// receiptType
					saveParam.${stuff }_receiptType = $("#${stuff }_receiptType").combobox("getValue");
				// 處理欄位被禁用時的取值
				} else {
					// dtid
					saveParam.${stuff }_dtid = $("#${stuff }_dtid").textbox("getValue");
					if ('${formDTO.caseCategory }'== '${caseCategoryAttr.UPDATE.code}' 
							|| '${formDTO.caseCategory }'== '${caseCategoryAttr.PROJECT.code}'){
						// 週邊設備1
						saveParam.${stuff }_peripherals = $("#${stuff }_peripherals").combobox("getValue");
						// 週邊設備2
						saveParam.${stuff }_peripherals2 = $("#${stuff }_peripherals2").combobox("getValue");
						// 週邊設備3
						saveParam.${stuff }_peripherals3 = $("#${stuff }_peripherals3").combobox("getValue");
						// receiptType
						saveParam.${stuff }_receiptType = $("#${stuff }_receiptType").combobox("getValue");
					}
				}
				
				// 維護部門
				saveParam.${stuff }_departmentId = $("#${stuff }_departmentId").combobox("getValue");
				// Task #3028 併機 異動 拆機 查核 專案 報修，建案之維護廠商，DTID預設帶出後要能再調整
				// 維護廠商
				saveParam.${stuff }_companyId = $("#${stuff }_companyId").combobox("getValue");
				if(!isEmpty($("#${stuff }_companyId").combobox("getValue"))){
					saveParam.${stuff }_companyName = $("#${stuff }_companyId").combobox("getText");
				}
				// 異動時要獲取的一些欄位顯示值
				if (('${formDTO.caseCategory }' == '${caseCategoryAttr.INSTALL.code}')
						|| ('${formDTO.caseCategory }'== '${caseCategoryAttr.UPDATE.code}')
						|| ('${formDTO.caseCategory }'== '${caseCategoryAttr.OTHER.code}')
						|| ('${formDTO.caseCategory }'== '${caseCategoryAttr.PROJECT.code}')) {
					// 案件類型 名稱
					if(!isEmpty($("#${stuff }_caseType").combobox("getValue"))){
						saveParam.${stuff }_caseTypeName = $("#${stuff }_caseType").combobox("getText");
					}
					if ('${formDTO.caseCategory }' == '${caseCategoryAttr.OTHER.code}' 
							|| '${formDTO.caseCategory }' == '${caseCategoryAttr.PROJECT.code}') {
						// 裝機地址-縣市 名稱
						if(!isEmpty($("#${stuff }_contactAddressLocation").combobox("getValue"))){
							saveParam.${stuff }_contactAddressLocationName = $("#${stuff }_contactAddressLocation").combobox("getText");
						}
						if(!isEmpty($("#${stuff }_contactPostCode").combobox("getValue"))){
							saveParam.${stuff }_contactPostCodeName = $("#${stuff }_contactPostCode").combobox("getText");
						}
					} else {
						// 裝機地址-縣市 名稱
						if(!isEmpty($("#${stuff }_installedAdressLocation").combobox("getValue"))){
							saveParam.${stuff }_installedAdressLocationName = $("#${stuff }_installedAdressLocation").combobox("getText");
						}
						if(!isEmpty($("#${stuff }_installedPostCode").combobox("getValue"))){
							saveParam.${stuff }_installedPostCodeName = $("#${stuff }_installedPostCode").combobox("getText");
						}
					}
					if(!isEmpty($("#${stuff }_receiptType").combobox("getValue"))){
						saveParam.${stuff }_receiptTypeName = $("#${stuff }_receiptType").combobox("getText");
					}
					// 裝機
					if ('${formDTO.caseCategory }' == '${caseCategoryAttr.INSTALL.code}'
						|| ('${formDTO.caseCategory }'== '${caseCategoryAttr.OTHER.code}')){
						if ('${formDTO.caseCategory }' == '${caseCategoryAttr.INSTALL.code}'){
							// 裝機類型 名稱
							if(!isEmpty($("#${stuff }_installType").combobox("getValue"))){
								saveParam.${stuff }_installTypeName = $("#${stuff }_installType").combobox("getText");
							}
						}
						// 刷卡機型 名稱
						if(!isEmpty($("#${stuff }_edcType").combobox("getValue"))){
							saveParam.${stuff }_edcTypeName = $("#${stuff }_edcType").combobox("getText");
						}
					}
					// 刷卡機型
					saveParam.${stuff }_edcType = $("#${stuff }_edcType").combobox("getValue");
					// 維護部門 名稱
					if(!isEmpty($("#${stuff }_departmentId").combobox("getValue"))){
						saveParam.${stuff }_departmentName = $("#${stuff }_departmentId").combobox("getText");
					}
					// 表頭（同對外名稱）名稱
					if(!isEmpty($("#${stuff }_merchantHeaderId").combobox("getValue"))){
						saveParam.${stuff }_headerName = $("#${stuff }_merchantHeaderId").combobox("getText");
					}
					// 軟體版本 名稱
					if(!isEmpty($("#${stuff }_softwareVersion").combobox("getValue"))){
						saveParam.${stuff }_softwareVersionName = $("#${stuff }_softwareVersion").combobox("getText");
					}
					// 內建功能 名稱
					if(!isEmpty($("#${stuff }_builtInFeature").combobox("getValue"))){
						saveParam.${stuff }_builtInFeatureName = $("#${stuff }_builtInFeature").combobox("getText");
					}
					// 雙模組模式 名稱
					if(!isEmpty($("#${stuff }_multiModule").combobox("getValue"))){
						saveParam.${stuff }_multiModuleName = $("#${stuff }_multiModule").combobox("getText");
					}
					// 週邊設備1 名稱
					if(!isEmpty($("#${stuff }_peripherals").combobox("getValue"))){
						saveParam.${stuff }_peripheralsName = $("#${stuff }_peripherals").combobox("getText");
					}
					// 週邊設備功能1 名稱
					if(!isEmpty($("#${stuff }_peripheralsFunction").combobox("getValue"))){
						saveParam.${stuff }_peripheralsFunctionName = $("#${stuff }_peripheralsFunction").combobox("getText");
					}
					// ECR連線 名稱
					if(!isEmpty($("#${stuff }_ecrConnection").combobox("getValue"))){
						saveParam.${stuff }_ecrConnectionName = $("#${stuff }_ecrConnection").combobox("getText");
					}
					// 週邊設備2 名稱
					if(!isEmpty($("#${stuff }_peripherals2").combobox("getValue"))){
						saveParam.${stuff }_peripherals2Name = $("#${stuff }_peripherals2").combobox("getText");
					}
					// 週邊設備功能2 名稱
					if(!isEmpty($("#${stuff }_peripheralsFunction2").combobox("getValue"))){
						saveParam.${stuff }_peripheralsFunction2Name = $("#${stuff }_peripheralsFunction2").combobox("getText");
					}
					// 連接方式
					if(!isEmpty($("#${stuff }_connectionType").combobox("getValue"))){
						saveParam.${stuff }_connectionTypeName = $("#${stuff }_connectionType").combobox("getText");
					}
					// 週邊設備功能3 名稱
					if(!isEmpty($("#${stuff }_peripherals3").combobox("getValue"))){
						saveParam.${stuff }_peripherals3Name = $("#${stuff }_peripherals3").combobox("getText");
					}
					// 週邊設備3 名稱
					if(!isEmpty($("#${stuff }_peripheralsFunction3").combobox("getValue"))){
						saveParam.${stuff }_peripheralsFunction3Name = $("#${stuff }_peripheralsFunction3").combobox("getText");
					}
					// 寬頻連線
					if(!isEmpty($("#${stuff }_netVendorId").combobox("getValue"))){
						saveParam.${stuff }_netVendorName = $("#${stuff }_netVendorId").combobox("getText");
					}
				} else {
					//案件類別
					if(!isEmpty($("#${stuff }_caseType").combobox("getValue"))){
						saveParam.${stuff }_caseTypeName = $("#${stuff }_caseType").combobox("getText");
					}
					// 裝機地址-縣市 名稱
					if(!isEmpty($("#${stuff }_contactAddressLocation").combobox("getValue"))){
						saveParam.${stuff }_contactAddressLocationName = $("#${stuff }_contactAddressLocation").combobox("getText");
					}
					if(!isEmpty($("#${stuff }_contactPostCode").combobox("getValue"))){
						saveParam.${stuff }_contactPostCodeName = $("#${stuff }_contactPostCode").combobox("getText");
					}
					//維護部門
					if(!isEmpty($("#${stuff }_departmentId").combobox("getValue"))){
						saveParam.${stuff }_departmentName = $("#${stuff }_departmentId").combobox("getText");
					}
					// 併機
					if ('${formDTO.caseCategory }' == '${caseCategoryAttr.MERGE.code}'){
						// 軟體版本 名稱
						if(!isEmpty($("#${stuff }_softwareVersion").combobox("getValue"))){
							saveParam.${stuff }_softwareVersionName = $("#${stuff }_softwareVersion").combobox("getText");
						}
					// 拆機
					} else if('${formDTO.caseCategory }' == '${caseCategoryAttr.UNINSTALL.code}'){
						// 拆機類型 名稱
						if(!isEmpty($("#${stuff }_uninstallType").combobox("getValue"))){
							saveParam.${stuff }_uninstallTypeName = $("#${stuff }_uninstallType").combobox("getText");
						}
					// 報修
					} else if('${formDTO.caseCategory }' == '${caseCategoryAttr.REPAIR.code}'){
						// 報修原因 名稱
						if(!isEmpty($("#${stuff }_repairReason").combobox("getValue"))){
							saveParam.${stuff }_repairReasonName = $("#${stuff }_repairReason").combobox("getText");
						}
					} 
				}
				saveParam.${stuff }_aoName = $("#${stuff }_merAoName").textbox("getValue");
				// 案件建案直接派工時驗證dtid
				if (actionId=="${iatomsContants.ACTION_NEW_DISPATCH}") {
					if(!checkDtidAndNumber(saveParam.${stuff }_customerId)){
						return false;
					}
					//Bug #2373 update by 2017/09/14
					saveParam.toMail='被指派的廠商;被指派的廠商部門;被指派的工程師;被指派的角色';
				}
				if (!(typeof(file_upload)=="undefined") && file_upload._storedIds!=""){
					file_upload.uploadStoredFiles();
				} else {
					if(isEmpty('${srmCaseHandleInfoDTO.caseId }')){
						// 調保存遮罩
						commonSaveLoading('createDlg');
					} else {
						// 調保存遮罩
						commonSaveLoading('editDlg');
					}
					var url = "${contextPath}/caseHandle.do?actionId=" + actionId;
					//save
					$.ajax({
						url: url,
						data:saveParam,
						type:'post', 
						cache:false, 
						dataType:'json', 
						success:function(json){
							if(isEmpty('${srmCaseHandleInfoDTO.caseId }')){
								// 調取消保存遮罩
								commonCancelSaveLoading('createDlg');
							} else {
								// 調取消保存遮罩
								commonCancelSaveLoading('editDlg');
							}
							if (json.success) {
								if (successFun) {
									successFun(json.msg);
									//successFun(json.caseId);
								}
							} else {
								if(isEmpty('${srmCaseHandleInfoDTO.caseId }')){
									handleScrollTop('createDlg');
								} else {
									handleScrollTop('editDlg');
								}
								handleScrollTop('createDlg');
								$("#caseDialogMsg").text(json.msg);
							}
						},
						error:function(){
							if(isEmpty('${srmCaseHandleInfoDTO.caseId }')){
								// 調取消保存遮罩
								commonCancelSaveLoading('createDlg');
							} else {
								// 調取消保存遮罩
								commonCancelSaveLoading('editDlg');
							}
							if(actionId=="${iatomsContants.ACTION_CREATE_CASE}") {
								$("#caseDialogMsg").text("儲存失敗");
							}
							if(actionId=="${iatomsContants.ACTION_NEW_DISPATCH}") {
								$("#caseDialogMsg").text("派工失敗");
							}
						}
					});
				}
			}
		}
		//判斷客戶的DTID生成方式是否同TID
		function checkDtidAndNumber(companyId){
			javascript:dwr.engine.setAsync(false);
			var isSame = false;
			ajaxService.checkDtidType(companyId, function(result){
				if(result && $("#complexNum").is(":checked")){
					$("#caseDialogMsg").text("複製導致DTID重複，請儲存調整後再派工");
					handleScrollTop('createDlg','caseDialogMsg');
					handleScrollTop('editDlg','caseDialogMsg');
					isSame = false;
				} else {
					isSame =  true;
				}
			});
			javascript:dwr.engine.setAsync(true);
			return isSame;
		} 
		/*
		* 未選擇dtid時禁用按鈕
		*/
		function disabledForDtid(caseCategory, isDisabled){
			// 需求單號
			$("#${stuff }_requirementNo").textbox(isDisabled?'disable':'enable');
			// 案件類型
			$("#${stuff }_caseType").combobox(isDisabled?'disable':'enable');
			// 其他說明
			$("#${stuff }_description").textbox(isDisabled?'disable':'enable');
			
			// Task #3028 併機 異動 拆機 查核 專案 報修，建案之維護廠商，DTID預設帶出後要能再調整
			// 維護部門
			$("#${stuff }_companyId").combobox(isDisabled?'disable':'enable');
			
			// 維護部門
			$("#${stuff }_departmentId").combobox(isDisabled?'disable':'enable');
			// 交易參數新增按鈕
			if($("#btnAppendRow").length > 0){
				$("#btnAppendRow").linkbutton(isDisabled?'disable':'enable');
			}
			// 交易參數刪除按鈕
			if($("#btnDelTrans").length > 0){
			//	$("#btnDelTrans").linkbutton(isDisabled?'disable':'enable');
				$("#btnDelTrans").linkbutton('disable');
			}
			if (isDisabled) {
				var blockOptions = {message:null,overlayCSS:{backgroundColor:'#fff',cursor:'default',opacity:'0.1',width:'100%'}};
				// 附加檔案
				$("#caseAdd").block(blockOptions);
			} else {
				$("#caseAdd").unblock();
			}
			//除裝機\異動外
			if ('${caseCategoryAttr.UPDATE.code}' != caseCategory && '${caseCategoryAttr.INSTALL.code}' != caseCategory) {
				// 聯繫地址 同營業地址
				$("#${stuff }_contactIsBussinessAddress").attr('disabled', isDisabled);
				// 聯繫地址 縣市
				$("#${stuff }_contactAddressLocation").combobox('readonly', isDisabled);
				// 聯繫地址 縣市(郵遞區號)
				$("#${stuff }_contactPostCode").combobox('readonly', isDisabled);
				// 聯繫地址
				$("#${stuff }_contactAddress").textbox('readonly', isDisabled);
				// 聯繫聯絡人 同特店聯絡人
				$("#${stuff }_contactIsBussinessContact").attr('disabled', isDisabled);
				// 聯繫聯絡人
				$("#${stuff }_contactUser").textbox(isDisabled?'disable':'enable');
				// 聯繫聯絡人電話 同特店聯絡人電話
				$("#${stuff }_contactIsBussinessContactPhone").attr('disabled', isDisabled);
				// 聯繫聯絡人電話
				$("#${stuff }_contactUserPhone").textbox(isDisabled?'disable':'enable');
				// 聯繫聯絡人 同特店聯絡人行動電話
				$("#${stuff }_contactIsBussinessContactMobilePhone").attr('disabled', isDisabled);
				// 聯繫聯絡人Email
				$("#${stuff }_contactMobilePhone").textbox(isDisabled?'disable':'enable');
				// 聯繫聯絡人電話 同特店聯絡人Email
				$("#${stuff }_contactIsBussinessContactEmail").attr('disabled', isDisabled);
				// 聯繫聯絡人Email
				$("#${stuff }_contactUserEmail").textbox(isDisabled?'disable':'enable');
			}
			
			
			// 異動
			if('${caseCategoryAttr.UPDATE.code}' == caseCategory ||'${caseCategoryAttr.PROJECT.code}' == caseCategory){
				// 舊特店代號
				$("#${stuff }_oldMerchantCode").textbox(isDisabled?'disable':'enable');
				// 是否同裝機作業
				$("#${stuff }_sameInstalled").combobox(isDisabled?'disable':'enable');
				// 特店代號勾選按鈕
				$("#getMerchantBtn").linkbutton(isDisabled?'disable':'enable');
				// 特店代號查詢按鈕
				$("#queryMerchantBtn").linkbutton(isDisabled?'disable':'enable');
				// 特店代號修改按鈕
				$("#editMerchantBtn").linkbutton(isDisabled?'disable':'enable');
				// 特店表頭修改按鈕
				$("#editMerHeaderBtn").linkbutton(isDisabled?'disable':'enable');
				// 特店代號
				$("#${stuff }_merMid").textbox('readonly', isDisabled);
				// 表頭（同對外名稱）
				$("#${stuff }_merchantHeaderId").combobox('readonly', isDisabled);
				// 軟體版本
				//	$("#${stuff }_softwareVersion").combobox('disable');
				$("#${stuff }_softwareVersion").combobox(isDisabled?'disable':'enable');
				// 內建功能
				$("#${stuff }_builtInFeature").combobox(isDisabled?'disable':'enable');
				// 雙模組模式
				$("#${stuff }_multiModule").combobox(isDisabled?'disable':'enable');
				// 週邊設備1
				$("#${stuff }_peripherals").combobox(isDisabled?'disable':'enable');
				// 週邊設備功能1
				$("#${stuff }_peripheralsFunction").combobox(isDisabled?'disable':'enable');
				// ECR連線
				$("#${stuff }_ecrConnection").combobox(isDisabled?'disable':'enable');
				// 週邊設備2
				$("#${stuff }_peripherals2").combobox(isDisabled?'disable':'enable');
				// 週邊設備功能2
				$("#${stuff }_peripheralsFunction2").combobox(isDisabled?'disable':'enable');
				// 連接方式
				$("#${stuff }_connectionType").combobox(isDisabled?'disable':'enable');
				// 週邊設備3
				$("#${stuff }_peripherals3").combobox(isDisabled?'disable':'enable');
				// 週邊設備功能3
				$("#${stuff }_peripheralsFunction3").combobox(isDisabled?'disable':'enable');
				// 是否開啟加密
				$("#${stuff }_isOpenEncrypt").combobox(isDisabled?'disable':'enable');
				// 電子化繳費平台
				$("#${stuff }_electronicPayPlatform").combobox(isDisabled?'disable':'enable');
				// LOGO
				$("#${stuff }_logoStyle").combobox(isDisabled?'disable':'enable');
				// 本機IP
				$("#${stuff }_localhostIp").textbox(isDisabled?'disable':'enable');
				// 寬頻連線
				$("#${stuff }_netVendorId").combobox(isDisabled?'disable':'enable');
				// Gateway
				$("#${stuff }_gateway").textbox(isDisabled?'disable':'enable');
				// Netmask
				$("#${stuff }_netmask").textbox(isDisabled?'disable':'enable');
				// 電子發票載具
				$("#${stuff }_electronicInvoice").combobox(isDisabled?'disable':'enable');
				// 銀聯閃付
				$("#${stuff }_cupQuickPass").combobox(isDisabled?'disable':'enable');
				// TMS參數說明
				$("#${stuff }_tmsParamDesc").textbox(isDisabled?'disable':'enable');
				if ('${caseCategoryAttr.UPDATE.code}' == caseCategory) {
					// 專案
					var isProjects = document.getElementsByName("${stuff }_isProject");
					if(isProjects != null && isProjects.length >0){
						for(var i=0; i < isProjects.length; i++){
							isProjects[i].disabled = isDisabled;
						}
					}
					// 裝機地址 同營業地址
					$("#${stuff }_isBussinessAddress").attr('disabled', isDisabled);
					// 裝機地址 縣市
					$("#${stuff }_installedAdressLocation").combobox('readonly', isDisabled);
					// 裝機地址 縣市(郵遞區號)
					$("#${stuff }_installedPostCode").combobox('readonly', isDisabled);
					// 裝機地址
					$("#${stuff }_merInstallAddress").textbox('readonly', isDisabled);
					
					// 裝機聯絡人 同特店聯絡人
					$("#${stuff }_equalsContact").attr('disabled', isDisabled);
					// 裝機聯絡人
					$("#${stuff }_merInstallUser").textbox(isDisabled?'disable':'enable');
					// 裝機聯絡人電話 同特店聯絡人電話
					$("#${stuff }_equalsMerMobilePhone").attr('disabled', isDisabled);
					// 裝機聯絡人電話
					$("#${stuff }_merInstallPhone").textbox(isDisabled?'disable':'enable');
					// 裝機聯絡人 同特店聯絡人行動電話
					$("#${stuff }_isBussinessContactMobilePhone").attr('disabled', isDisabled);
					// 裝機聯絡人手機
					$("#${stuff }_installedContactMobilePhone").textbox(isDisabled?'disable':'enable');
					// 裝機聯絡人電話 同特店聯絡人Email
					$("#${stuff }_isBussinessContactEmail").attr('disabled', isDisabled);
					// 裝機聯絡人Email
					$("#${stuff }_installedContactEmail").textbox(isDisabled?'disable':'enable');
					// 軟體版本
				//	$("#${stuff }_softwareVersion").combobox('disable');
					/* $("#${stuff }_softwareVersion").combobox(isDisabled?'disable':'enable');
					// 內建功能
					$("#${stuff }_builtInFeature").combobox(isDisabled?'disable':'enable');
					// 雙模組模式
					$("#${stuff }_multiModule").combobox(isDisabled?'disable':'enable');
					// 週邊設備1
					$("#${stuff }_peripherals").combobox(isDisabled?'disable':'enable');
					// 週邊設備功能1
					$("#${stuff }_peripheralsFunction").combobox(isDisabled?'disable':'enable');
					// ECR連線
					$("#${stuff }_ecrConnection").combobox(isDisabled?'disable':'enable');
					// 週邊設備2
					$("#${stuff }_peripherals2").combobox(isDisabled?'disable':'enable');
					// 週邊設備功能2
					$("#${stuff }_peripheralsFunction2").combobox(isDisabled?'disable':'enable');
					// 連接方式
					$("#${stuff }_connectionType").combobox(isDisabled?'disable':'enable');
					
					// 週邊設備3
					$("#${stuff }_peripherals3").combobox(isDisabled?'disable':'enable');
					// 週邊設備功能3
					$("#${stuff }_peripheralsFunction3").combobox(isDisabled?'disable':'enable');
					
					
					// 是否開啟加密
					$("#${stuff }_isOpenEncrypt").combobox(isDisabled?'disable':'enable');
					// 電子化繳費平台
					$("#${stuff }_electronicPayPlatform").combobox(isDisabled?'disable':'enable');
					// LOGO
					$("#${stuff }_logoStyle").combobox(isDisabled?'disable':'enable');
					
					
					// 本機IP
					$("#${stuff }_localhostIp").textbox(isDisabled?'disable':'enable');
					// 寬頻連線
					$("#${stuff }_netVendorId").combobox(isDisabled?'disable':'enable');
					// Gateway
					$("#${stuff }_gateway").textbox(isDisabled?'disable':'enable');
					// Netmask
					$("#${stuff }_netmask").textbox(isDisabled?'disable':'enable'); */
					// 其他說明
					$("#${stuff }_description").textbox(isDisabled?'disable':'enable');
					
					/* // 電子發票載具
					$("#${stuff }_electronicInvoice").combobox(isDisabled?'disable':'enable');
					// 銀聯閃付
					$("#${stuff }_cupQuickPass").combobox(isDisabled?'disable':'enable'); */
					
					
					//Receipt_type
					$("#${stuff }_receiptType").combobox(isDisabled?'disable':'enable');
				} else {
					//Receipt_type
					$("#${stuff }_receiptType").combobox(isDisabled?'disable':'enable');
					/* // 聯繫地址 同營業地址
					$("#${stuff }_contactIsBussinessAddress").attr('disabled', isDisabled);
					// 聯繫地址 縣市
					$("#${stuff }_contactAddressLocation").combobox('readonly', isDisabled);
					// 聯繫地址 縣市(郵遞區號)
					$("#${stuff }_contactPostCode").combobox('readonly', isDisabled);
					// 聯繫地址
					$("#${stuff }_contactAddress").textbox('readonly', isDisabled);
					// 聯繫聯絡人 同特店聯絡人
					$("#${stuff }_contactIsBussinessContact").attr('disabled', isDisabled);
					// 聯繫聯絡人
					$("#${stuff }_contactUser").textbox(isDisabled?'disable':'enable');
					// 聯繫聯絡人電話 同特店聯絡人電話
					$("#${stuff }_contactIsBussinessContactPhone").attr('disabled', isDisabled);
					// 聯繫聯絡人電話
					$("#${stuff }_contactUserPhone").textbox(isDisabled?'disable':'enable'); */
					
					// 專案代碼
					$("#${stuff }_projectCode").textbox(isDisabled?'disable':'enable');
					// 專案名稱
					$("#${stuff }_projectName").textbox(isDisabled?'disable':'enable');
				}
			} else {
				// 併機
				if('${caseCategoryAttr.MERGE.code}' == caseCategory){
					/* // 聯繫地址 同營業地址
					$("#${stuff }_contactIsBussinessAddress").attr('disabled', isDisabled);
					// 聯繫地址 縣市
					$("#${stuff }_contactAddressLocation").combobox('readonly', isDisabled);
					// 聯繫地址 縣市(郵遞區號)
					$("#${stuff }_contactPostCode").combobox('readonly', isDisabled);
					// 聯繫地址
					$("#${stuff }_contactAddress").textbox('readonly', isDisabled);
					// 聯繫聯絡人 同特店聯絡人
					$("#${stuff }_contactIsBussinessContact").attr('disabled', isDisabled);
					// 聯繫聯絡人
					$("#${stuff }_contactUser").textbox('readonly', isDisabled);
					// 聯繫聯絡人電話 同特店聯絡人電話
					$("#${stuff }_contactIsBussinessContactPhone").attr('disabled', isDisabled);
					// 聯繫聯絡人電話
					$("#${stuff }_contactUserPhone").textbox('readonly', isDisabled); */
					// 軟體版本
					$("#${stuff }_softwareVersion").combobox(isDisabled?'disable':'enable');
					// 是否同裝機作業
					$("#${stuff }_sameInstalled").combobox(isDisabled?'disable':'enable');
					// TMS參數說明
					$("#${stuff }_tmsParamDesc").textbox(isDisabled?'disable':'enable');
				// 拆機
				} else if('${caseCategoryAttr.UNINSTALL.code}'  == caseCategory){
					/* // 聯繫地址 同營業地址
					$("#${stuff }_contactIsBussinessAddress").attr('disabled', isDisabled);
					// 聯繫地址 縣市
					$("#${stuff }_contactAddressLocation").combobox('readonly', isDisabled);
					// 聯繫地址 縣市(郵遞區號)
					$("#${stuff }_contactPostCode").combobox('readonly', isDisabled);
					// 聯繫地址
					$("#${stuff }_contactAddress").textbox('readonly', isDisabled);
					// 聯繫聯絡人 同特店聯絡人
					$("#${stuff }_contactIsBussinessContact").attr('disabled', isDisabled);
					// 聯繫聯絡人
					$("#${stuff }_contactUser").textbox('readonly');
					// 聯繫聯絡人電話 同特店聯絡人電話
					$("#${stuff }_contactIsBussinessContactPhone").attr('disabled', isDisabled);
					// 聯繫聯絡人電話
					$("#${stuff }_contactUserPhone").textbox('readonly'); */
					
					// 拆機類型
					$("#${stuff }_uninstallType").combobox(isDisabled?'disable':'enable');
				// 查核
				} else if('${caseCategoryAttr.CHECK.code}' == caseCategory){
					/* // 聯繫地址 同營業地址
					$("#${stuff }_contactIsBussinessAddress").attr('disabled', isDisabled);
					// 聯繫地址 縣市
					$("#${stuff }_contactAddressLocation").combobox('readonly', isDisabled);
					// 聯繫地址 縣市(郵遞區號)
					$("#${stuff }_contactPostCode").combobox('readonly', isDisabled);
					// 聯繫地址
					$("#${stuff }_contactAddress").textbox('readonly', isDisabled);
					// 聯繫聯絡人 同特店聯絡人
					$("#${stuff }_contactIsBussinessContact").attr('disabled', isDisabled);
					// 聯繫聯絡人
					$("#${stuff }_contactUser").textbox(isDisabled?'disable':'enable');
					// 聯繫聯絡人電話 同特店聯絡人電話
					$("#${stuff }_contactIsBussinessContactPhone").attr('disabled', isDisabled);
					// 聯繫聯絡人電話
					$("#${stuff }_contactUserPhone").textbox(isDisabled?'disable':'enable'); */
				// 專案
				} else if('${caseCategoryAttr.PROJECT.code}' == caseCategory){
					/* //Receipt_type
					$("#${stuff }_receiptType").combobox(isDisabled?'disable':'enable');
					// 聯繫地址 同營業地址
					$("#${stuff }_contactIsBussinessAddress").attr('disabled', isDisabled);
					// 聯繫地址 縣市
					$("#${stuff }_contactAddressLocation").combobox('readonly', isDisabled);
					// 聯繫地址 縣市(郵遞區號)
					$("#${stuff }_contactPostCode").combobox('readonly', isDisabled);
					// 聯繫地址
					$("#${stuff }_contactAddress").textbox('readonly', isDisabled);
					// 聯繫聯絡人 同特店聯絡人
					$("#${stuff }_contactIsBussinessContact").attr('disabled', isDisabled);
					// 聯繫聯絡人
					$("#${stuff }_contactUser").textbox(isDisabled?'disable':'enable');
					// 聯繫聯絡人電話 同特店聯絡人電話
					$("#${stuff }_contactIsBussinessContactPhone").attr('disabled', isDisabled);
					// 聯繫聯絡人電話
					$("#${stuff }_contactUserPhone").textbox(isDisabled?'disable':'enable');
					
					// 專案代碼
					$("#${stuff }_projectCode").textbox(isDisabled?'disable':'enable');
					// 專案名稱
					$("#${stuff }_projectName").textbox(isDisabled?'disable':'enable'); */
				// 報修
				} else if('${caseCategoryAttr.REPAIR.code}' == caseCategory){
					/* // 聯繫地址 同營業地址
					$("#${stuff }_contactIsBussinessAddress").attr('disabled', isDisabled);
					// 聯繫地址 縣市
					$("#${stuff }_contactAddressLocation").combobox('readonly', isDisabled);
					// 聯繫地址 縣市(郵遞區號)
					$("#${stuff }_contactPostCode").combobox('readonly', isDisabled);
					// 聯繫地址
					$("#${stuff }_contactAddress").textbox('readonly', isDisabled);
					// 聯繫聯絡人 同特店聯絡人
					$("#${stuff }_contactIsBussinessContact").attr('disabled', isDisabled);
					// 聯繫聯絡人
					$("#${stuff }_contactUser").textbox(isDisabled?'disable':'enable');
					// 聯繫聯絡人電話 同特店聯絡人電話
					$("#${stuff }_contactIsBussinessContactPhone").attr('disabled', isDisabled);
					// 聯繫聯絡人電話
					$("#${stuff }_contactUserPhone").textbox(isDisabled?'disable':'enable'); */
					// Task #3044 報修件「報修原因」為必填 改為 非必填
					$("#${stuff }_repairReason").combobox(isDisabled?'disable':'enable');
				}
			}
		}
		/* <c:if test="${caseCategoryAttr.OTHER.code eq caseCategory} "> */
		/*
		* 處理禁用控件 客服、客戶才有權限操作按鈕，其他全部禁用
		* caseStatus ： 案件狀態
		*/
		
	/* 	</c:if> */
		//Task #3584
		//此DTID台新裝機件尚未建案
		//此DTID台新裝機件已存在
		function checkRequirementNo(requirementNo, dtid){
			//返回結果flag
			var isCheck = true;
			javascript:dwr.engine.setAsync(false);
			//需要查詢flag
			var needQuery = false;
			//ei開頭flag
			var isEi = false;
			//eu，ea...開頭flag
			var isEOther = false;
			//新建flag
			var isCreate = false;
			//宣揚 id
			var customerId = null;
			if($("#${stuff }_customer").length>0){
				customerId = $("#${stuff }_customer").combobox("getValue");
			} else if($("#${stuff }_customerId").length>0){
				customerId = $("#${stuff }_customerId").val();
			}
			//宣揚 && requirementNo不為空，(如果為空 後臺 判斷符合條件后 調用存儲過程 刪除installcaseid)
			if('${formDTO.bccCustomerId}'==customerId && !isEmpty(requirementNo)){
				if(isEmpty('${srmCaseHandleInfoDTO.caseId }')){
					isCreate = true;
				} else{
					isCreate = false;
				}
				if(requirementNo.length > 1){
					if(requirementNo.substring(0,2).toLowerCase() == 'ei'){
						needQuery = true;
						isEi = true;
					} else if(requirementNo.substring(0,2).toLowerCase()=='ec'
									|| requirementNo.substring(0,2).toLowerCase()=='em'
									|| requirementNo.substring(0,2).toLowerCase()=='eu'
									|| requirementNo.substring(0,2).toLowerCase()=='ea'
									|| requirementNo.substring(0,2).toLowerCase()=='er'){
						needQuery = true;
						isEOther = true;
					}
				}
				if(needQuery){
					ajaxService.getInstallCaseId(dtid, function(result){
						//是新建案 
						if(isCreate){
							//ei開頭
							if(isEi){
								//同dtid下已有ei開頭的 installcaseid
								if(result){
									$("#caseDialogMsg").text("此DTID台新裝機件已存在");
									handleScrollTop('createDlg','caseDialogMsg');
									handleScrollTop('editDlg','caseDialogMsg');
									isCheck = false;
								} else {
									//沒有ei開頭的 installcaseid
									isCheck = true;
								}
								//eu，ea...開頭
							} else if(isEOther){
								//同dtid下已有ei開頭的 installcaseid
								if(result){
									isCheck = true;
								} else {
									//沒有ei開頭的 installcaseid
									$("#caseDialogMsg").text("此DTID台新裝機件尚未建案");
									handleScrollTop('createDlg','caseDialogMsg');
									handleScrollTop('editDlg','caseDialogMsg');
									isCheck = false;
								}
							} else {
								//不符合邏輯的 不檢核 
								isCheck = true;
							}
						} else {
							//是 修改 且 需求單號 有異動 
							if(requirementNo != '${srmCaseHandleInfoDTO.requirementNo }'){
								//ei開頭
								if(isEi){
									//同dtid下已有ei開頭的 installcaseid 且 等於本案件修改前的 requirementNo
									//本來是裝機 修改之後還是裝機 
									if(result && result=='${srmCaseHandleInfoDTO.requirementNo }'){
										isCheck = true;
									//同dtid下已有ei開頭的 installcaseid 且 不等於本案件修改前的 requirementNo 
									//此DTID台新裝機件已存在 不能再有一筆裝幾件 
									} else if(result && result!='${srmCaseHandleInfoDTO.requirementNo }'){
										$("#caseDialogMsg").text("此DTID台新裝機件已存在");
										handleScrollTop('createDlg','caseDialogMsg');
										handleScrollTop('editDlg','caseDialogMsg');
										isCheck = false;
									} else {
										//同dtid下沒有ei開頭的 installcaseid 
										isCheck = true;
									}
									//eu，ea...開頭
								} else if(isEOther){
									/* 
									*同dtid下已有ei開頭的 installcaseid. 
									*如果srmCaseHandleInfoDTO.requirementNo 不為ei 正常狀態.
									*如果srmCaseHandleInfoDTO.requirementNo 為ei 現在改爲了 eother
									*(後臺判斷符合條件后 調用存儲過程 刪除installcaseid).
									*/
									if(result){
										isCheck = true;
									} else {
										//同dtid下沒有ei開頭的 installcaseid 
										$("#caseDialogMsg").text("此DTID台新裝機件尚未建案");
										handleScrollTop('createDlg','caseDialogMsg');
										handleScrollTop('editDlg','caseDialogMsg');
										isCheck = false;
									}
								}
							}
						}
					});
				}	 		
			}
			javascript:dwr.engine.setAsync(true);
			return isCheck;
		}
	</script>
</body>
</html>