<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.EdcPaymentReportFormDTO"%>
<%@include file="/jsp/common/common.jsp"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	EdcPaymentReportFormDTO formDTO = null;
	String ucNo = "";
	String logonUserCompanyId = null;
	if (ctx != null) {
			formDTO = (EdcPaymentReportFormDTO) ctx.getRequestParameter();
			if(formDTO != null){
				ucNo = formDTO.getUseCaseNo();
				//後台返回的查詢結果轉換過的json字符串
				//當前登入者公司id
				logonUserCompanyId = formDTO.getLogonUserCompanyId();
			}
	}
	//登入着不是客戶的公司list
	List<Parameter> companyList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo,IAtomsConstants.PARAM_COMPANY_LIST);
	//登入着為客戶的公司list
	List<Parameter> customerList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo,IAtomsConstants.PARAM_CUSTOMER_LIST);
%>
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$CASE_CATEGORY" var="caseCategoryAttr" />
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="ucNo" value="<%=ucNo%>" scope="page"></c:set>
<!-- DataLoader -->
<c:set var="companyList" value="<%=companyList%>" scope="page"></c:set>
<c:set var="customerList" value="<%=customerList%>" scope="page"></c:set>
<c:set var="logonUserCompanyId" value="<%=logonUserCompanyId%>" scope="page"></c:set>
<!-- DataLoader -->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/jsp/common/easyui-common.jsp"%>
<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
<title>Insert title here</title>
</head>
<body>
<div style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
    <div id="p" class="easyui-panel" title="EDC維護費用付款報表" style="width: 98%; height: auto;">
        <div title="" style="padding: 10px">
        <form  id="searchForm" name="searchForm" method="post" novalidate>
        	<input type="hidden" id="serviceId" name="serviceId" value=""/>
        	<input type="hidden" id="actionId" name="actionId" value=""/>
        	<input type="hidden" id="useCaseNo" name="useCaseNo" value=""/>
        	<input type="hidden" id="exportFlag" name="exportFlag" value=""/>
        	<input type="hidden" id="exportQueryCustomer" name="exportQueryCustomer" value=""/>
            <table>
                <tr>
                	<td>客戶:<span class="red">*</span></td>
                	<td>
					<c:choose>
						 <c:when test="${not empty logonUserCompanyId}">
						<cafe:droplisttag 
							name="queryCustomer"
							id="queryCustomer"
							css="easyui-combobox"
							result="${customerList}"
							selectedValue="${logonUserCompanyId}"
							hasBlankValue="true"
							blankName="" 
							style="width:180px"
							javascript="editable='false' disabled=disabled"
						>
						</cafe:droplisttag>
					</c:when>
					<c:otherwise>
				           <cafe:droplisttag 
				   			name="queryCustomer"
				   			id="queryCustomer"
				   			css="easyui-combobox"
				   			result="${companyList}"
				   			selectedValue=""
				   			blankName="請選擇"
				   			hasBlankValue="true"
				   			style="width:180px"
				   			javascript="editable='false' validType=\"requiredDropList\" invalidMessage=\"請輸入客戶\""
				   		>
				   		</cafe:droplisttag>
				   	</c:otherwise>
				   </c:choose>
				    </td>
                	<td>完成日期:<span class="red">*</span></td>
                        <td>
                           <input missingMessage="請輸入完成日期起" id="queryCompleteDateStart" name="queryCompleteDateStart" class="easyui-datebox" maxlength="10" style="width: 150px" data-options="
                           validType:['date[\'完成日期格式限YYYY/MM/DD\']'],
                            	onChange:function(newValue,oldValue) {
                            			$('#queryCompleteDateEnd').timespinner('isValid');
                             	}" required="true">～
		                  
                            <input required="true" missingMessage="請輸入完成日期迄" id="queryCompleteDateEnd" name="queryCompleteDateEnd" class="easyui-datebox" maxlength="10" style="width: 150px" data-options="validType:['date[\'完成日期格式限YYYY/MM/DD\']','compareDateStartEnd[\'#queryCompleteDateStart\',\'完成日期起不可大於應完成日期迄\']']"> 
                    </td>
                    <td>
                        <a href="javascript:void(0)" id="btnQuery" class="easyui-linkbutton" style="width: 90px" data-options="iconCls:'icon-search'">查詢</a>
                    	<input type="hidden" id="isInstant" value="Y"/>
                    </td>
                    <!-- <td>資料模式:</td>
                    <td>
                        <input type="radio" name="queryDateMode" id="isInstant" value="2" checked="checked">即時</input>
                        <input type="radio" name="queryDateMode" id="isHistory" value="1">歷史</input>
                    </td> -->
                </tr>
                <tr>
                    <td colspan="4">
                        <span class="red">*</span>於案件簽收後方有設備資訊。
                    	</td>
                </tr>
            	</table>
            </form> 
            <div style="width: 100%;display: inline-block;">
	           	<span id="msg" class="red" style="width: 50%;display: inline-block;"></span>
	           	<span style="width: 50%;display: inline-block;float: right;text-align: right">
	           		<a href="javascript:void(0)" id="export" style="width: 150px" onclick="exportData()">匯出</a>
	           	</span>
	        </div>
	        <div><span id="message" class="red">${msg }</span></div>
            <table id="installDataGrid" class="easyui-datagrid" style="width: 100%; height: auto" title="裝機費用資料清單"
                data-options="
			    rownumbers:true,
				pagination:true,
				pageList:[15,30,50,100],
				pageSize:15,
				iconCls: 'icon-edit',
				singleSelect: true,
				selectOnCheck: true,
				method: 'get',
				pageNumber:0,
				sortOrder:'asc',
				nowrap : false,
				toolbar:'#tb'
			    ">
                <thead>
                    <tr>
                        <th data-options="field:'customerName',width:160,halign:'center',align:'left',sortable:true">客戶</th>
                        <th data-options="field:'requirementNo',width:140,halign:'center',align:'left',sortable:true">需求單號</th>
                        <th data-options="field:'caseId',width:140,halign:'center',align:'left',sortable:true">案件編號</th>
                        <th data-options="field:'aoName',width:140,halign:'center',align:'left',sortable:true">業務人員</th>
                        <th data-options="field:'caseCategory',width:100,halign:'center',align:'left',sortable:true">案件類別</th>
                        <th data-options="field:'caseType',width:160,halign:'center',align:'left',sortable:true">案件類型</th>
                        <th data-options="field:'createdDate',width:160,halign:'center',align:'center',formatter:formatToTimeStampIgnoreSecond,width:180,sortable:true">進件日期</th>
                        <th data-options="field:'dispatchDate',width:160,halign:'center',align:'center',formatter:formatToTimeStampIgnoreSecond,width:180,sortable:true">派工日期</th>
                        <th data-options="field:'acceptableFinishDate',width:160,halign:'center',align:'center',formatter:formatToTimeStampIgnoreSecond,width:200,sortable:true">應完修時間</th>
                        <th data-options="field:'completeDate',width:160,halign:'center',align:'center',formatter:formatToTimeStampIgnoreSecond,width:200,sortable:true">實際完成日</th>

                        <th data-options="field:'processType',width:160,halign:'center',align:'left',sortable:true">處理方式</th>
                        <th data-options="field:'installType',width:140,halign:'center',align:'left',sortable:true">裝機類型</th>
                        <th data-options="field:'attendanceTimes',halign:'center',align:'right',width:140,sortable:true">到場次數</th>
                        <th data-options="field:'attendanceDesc',halign:'center',align:'left',width:140,sortable:true">到場說明</th>
                        <th data-options="field:'rushRepairDesc',halign:'center',align:'left',width:140,sortable:true">延期說明</th>
                        <th data-options="field:'merchantCode',halign:'center',align:'left',width:100,sortable:true">特店代號</th>
                        <th data-options="field:'merchantName',halign:'center',align:'left',width:160,sortable:true">特店名稱</th>
                        <th data-options="field:'area',width:180,halign:'center',align:'left',sortable:true">特店區域</th>
                        <th data-options="field:'bussionAddress',width:200,halign:'center',align:'left',sortable:true">特店地址</th>

                        <th data-options="field:'assetName',halign:'center',align:'left',width:160,sortable:true">刷卡機型</th>
                        <th data-options="field:'model',width:160,halign:'center',align:'left',sortable:true">設備型號</th>
                        <th data-options="field:'wareHouseName',width:140,halign:'center',align:'left',sortable:true">倉別</th>
                        <th data-options="field:'installedAddress',width:140,halign:'center',align:'left',sortable:true">裝機地址</th>
                        <th data-options="field:'companyName',width:100,halign:'center',align:'left',sortable:true,styler: function (value, row, index) {
              return 'background-color:yellow;';
           }">裝機廠商</th>

                        <th data-options="field:'dtid',width:160,halign:'center',align:'left',sortable:true">DTID</th>
                        <th data-options="field:'tid',width:140,halign:'center',align:'left',sortable:true">TID</th>
                        <th data-options="field:'enableDate',width:180,align:'center',formatter:formaterTimeStampToyyyyMMDD,sortable:true">設備啟用日</th>
                        <th data-options="field:'serialNumber',width:200,halign:'center',align:'left',sortable:true">設備序號</th>
                        <th data-options="field:'propertyId',width:200,halign:'center',align:'left',sortable:true">財產編號</th>

                        <th data-options="field:'contractId',width:160,halign:'center',align:'left',sortable:true">合約編號</th>
                        <th data-options="field:'peripheralsName',halign:'center',align:'left',width:140,sortable:true">週邊設備1</th>
                        <th data-options="field:'peripheralsSerialNumber',halign:'center',align:'left',width:140,sortable:true">週邊設備1序號</th>
                        <th data-options="field:'peripheralsContractCode',halign:'center',align:'left',width:200,sortable:true">週邊設備1合約編號</th>
                        <th data-options="field:'peripherals2Name',width:100,halign:'center',align:'left',sortable:true">週邊設備2</th>
                        <th data-options="field:'peripherals2SerialNumber',halign:'center',align:'left',width:140,sortable:true">週邊設備2序號</th>
                        <th data-options="field:'peripherals2ContractCode',halign:'center',align:'left',width:200,sortable:true">週邊設備2合約編號</th>
                        <th data-options="field:'peripherals3Name',width:100,halign:'center',align:'left',sortable:true">週邊設備3</th>
                        <th data-options="field:'peripherals3SerialNumber',halign:'center',align:'left',width:140,sortable:true">週邊設備3序號</th>
                        <th data-options="field:'peripherals3ContractCode',halign:'center',align:'left',width:200,sortable:true">週邊設備3合約編號</th>
                        <th data-options="field:'functionTypeList',width:160,halign:'center',align:'left',sortable:true">設備開啟功能清單</th>

                        <th data-options="field:'ecrLine',width:180,halign:'center',align:'left',formatter:fomatterPositiveInt, sortable:true">ECR線</th>
                        <th data-options="field:'netLine',width:180,halign:'center',align:'left',formatter:fomatterPositiveInt,sortable:true">網路線</th>
                        <th data-options="field:'otherLine',width:180,halign:'center',align:'left',formatter:fomatterPositiveInt,sortable:true">耗材品項、個數、費用</th>
                        <th data-options="field:'delayTime',width:180,halign:'center',align:'right',formatter:formatCapacity,sortable:true">完修逾期時間</th>
                        <th data-options="field:'isFirstInstalled',width:180,halign:'center',formatter:fomatterYesOrNo,align:'left',sortable:true">是否為首裝</th>
                        <th data-options="field:'posPrice',width:200,halign:'center',align:'right',formatter:fomatterPositiveInt,sortable:true">POS機成本</th>
                    </tr>
                </thead>
            </table>
             <div style="text-align: right;padding: 10px"></div>
            <table id="uninstallDataGrid" class="easyui-datagrid" style="width: 100%; height: auto" title="拆機費用資料清單"
                data-options="
			    rownumbers:true,
                pagination:true,
                pageList:[15,30,50,100],
				pageSize:15,
			    iconCls: 'icon-edit',
			    singleSelect: true,
				selectOnCheck: true,
				pageNumber:0,
				nowrap : false,
			    method: 'get'
			    ">
                <thead>
                    <tr>
                        <th data-options="field:'customerName',width:160,halign:'center',align:'left',sortable:true">客戶</th>
                        <th data-options="field:'requirementNo',width:140,halign:'center',align:'left',sortable:true">需求單號</th>
                        <th data-options="field:'caseId',width:140,halign:'center',align:'left',sortable:true">案件編號</th>
                        <th data-options="field:'aoName',width:140,halign:'center',align:'left',sortable:true">業務人員</th>
                        <th data-options="field:'caseCategory',width:100,halign:'center',align:'left',sortable:true">案件類別</th>
                        <th data-options="field:'caseType',width:160,halign:'center',align:'left',sortable:true">案件類型</th>
                        <th data-options="field:'createdDate',width:160,align:'center',formatter:formatToTimeStampIgnoreSecond,width:180,sortable:true">進件日期</th>
                        <th data-options="field:'dispatchDate',width:160,align:'center',formatter:formatToTimeStampIgnoreSecond,width:180,sortable:true">派工日期</th>
                        <th data-options="field:'acceptableFinishDate',width:160,align:'center',formatter:formatToTimeStampIgnoreSecond,width:200,sortable:true">應完修時間</th>
                        <th data-options="field:'completeDate',width:200,align:'center',formatter:formatToTimeStampIgnoreSecond,sortable:true">實際完成日</th>

                        <th data-options="field:'processType',width:160,halign:'center',align:'left',sortable:true">處理方式</th>
                        <th data-options="field:'installType',width:140,halign:'center',align:'left',sortable:true">裝機類型</th>
                        <th data-options="field:'attendanceTimes',width:140,halign:'center',align:'right',sortable:true">到場次數</th>
                        <th data-options="field:'attendanceDesc',width:140,halign:'center',align:'left',sortable:true">到場說明</th>
                        <th data-options="field:'rushRepairDesc',width:140,halign:'center',align:'left',sortable:true">延期說明</th>
                        <th data-options="field:'merchantCode',width:100,halign:'center',align:'left',sortable:true">特店代號</th>
                        <th data-options="field:'merchantName',width:160,halign:'center',align:'left',sortable:true">特店名稱</th>
                        <th data-options="field:'area',width:180,halign:'center',align:'left',sortable:true">特店區域</th>
                        <th data-options="field:'bussionAddress',width:200,halign:'center',align:'left',sortable:true">特店地址</th>

                        <th data-options="field:'assetName',width:160,halign:'center',align:'left',sortable:true">刷卡機型</th>
                         <th data-options="field:'model',width:160,halign:'center',align:'left',sortable:true">設備型號</th>
                        <th data-options="field:'wareHouseName',width:140,halign:'center',align:'left',sortable:true">倉別</th>
                        <th data-options="field:'installedAddress',halign:'center',align:'left',width:140,sortable:true">裝機地址</th>
                        <th data-options="field:'companyName',width:100,halign:'center',align:'left',sortable:true,styler: function (value, row, index) {
              return 'background-color:yellow;';
           }">裝機廠商</th>
                        <th data-options="field:'unInstalledAddress',halign:'center',align:'left',width:140,sortable:true">拆機地址</th>
                        <th data-options="field:'unInstalledCompanyName',halign:'center',align:'left',width:100,sortable:true">拆機廠商</th>
                        <th data-options="field:'dtid',width:160,halign:'center',align:'left',sortable:true">DTID</th>
                        <th data-options="field:'tid',width:160,halign:'center',align:'left',sortable:true">TID</th>
                        <th data-options="field:'enableDate',width:200,align:'center',formatter:formaterTimeStampToyyyyMMDD,sortable:true">設備啟用日</th>
                        <th data-options="field:'serialNumber',width:200,halign:'center',align:'left',sortable:true">設備序號</th>
                        
						<th data-options="field:'propertyId',width:160,halign:'center',align:'left',sortable:true">財產編號</th>
                        <th data-options="field:'contractId',width:160,halign:'center',align:'left',sortable:true">合約編號</th>
                        <th data-options="field:'peripheralsName',width:140,halign:'center',align:'left',sortable:true">週邊設備1</th>
                        <th data-options="field:'peripheralsSerialNumber',width:140,halign:'center',align:'left',sortable:true">週邊設備1序號</th>
                        <th data-options="field:'peripheralsContractCode',width:200,halign:'center',align:'left',sortable:true">週邊設備1合約編號</th>
                        <th data-options="field:'peripherals2Name',width:140,halign:'center',align:'left',sortable:true">週邊設備2</th>
                        <th data-options="field:'peripherals2SerialNumber',width:140,halign:'center',align:'left',sortable:true">週邊設備2序號</th>
                        <th data-options="field:'peripherals2ContractCode',width:200,halign:'center',align:'left',sortable:true">週邊設備2合約編號</th>
                        <th data-options="field:'peripherals3Name',width:140,halign:'center',align:'left',sortable:true">週邊設備3</th>
                        <th data-options="field:'peripherals3SerialNumber',width:140,halign:'center',align:'left',sortable:true">週邊設備3序號</th>
                        <th data-options="field:'peripherals3ContractCode',width:200,halign:'center',align:'left',sortable:true">週邊設備3合約編號</th>
                        <th data-options="field:'functionTypeList',width:160,halign:'center',align:'left',sortable:true">設備開啟功能清單</th>
                        <th data-options="field:'installedDate',width:180,align:'center',formatter:formatToTimeStamp,sortable:true">裝機日期</th>
                        <th data-options="field:'uninstalledDate',width:180,align:'center',formatter:formatToTimeStamp,sortable:true">拆機日期</th>
                        <th data-options="field:'userdDays90',width:220,halign:'center',align:'left',formatter:fomatterYesOrNoInNinety,sortable:true">裝機-拆機是否未滿三個月</th>
                        <th data-options="field:'userdDays120',width:220,halign:'center',align:'left',formatter:fomatterYesOrNoIn120,sortable:true">裝機-拆機是否未滿四個月</th>
                        <th data-options="field:'delayTime',width:200,halign:'center',align:'right',formatter:formatCapacity,sortable:true">完修逾期時間</th>
                    </tr>
                </thead>
            </table>
           
           <div style="text-align: right;padding: 10px"></div>
            <table id="otherDataGrid" class="easyui-datagrid" style="width: 100%; height: auto" title="其他費用資料清單(併機、異動)"
                data-options="
			    rownumbers:true,
                pagination:true,
                pageList:[15,30,50,100],
				pageSize:15,
			    iconCls: 'icon-edit',
			    singleSelect: true,
				selectOnCheck: true,
				pageNumber:0,
				nowrap : false,
			    method: 'get'
			    ">
                <thead>
                    <tr>
                        <th data-options="field:'customerName',width:160,halign:'center',align:'left',sortable:true">客戶</th>
                        <th data-options="field:'requirementNo',width:140,halign:'center',align:'left',sortable:true">需求單號</th>
                        <th data-options="field:'caseId',width:140,halign:'center',align:'left',sortable:true">案件編號</th>
                        <th data-options="field:'aoName',width:140,halign:'center',align:'left',sortable:true">業務人員</th>
                        <th data-options="field:'caseCategory',width:100,halign:'center',align:'left',sortable:true">案件類別</th>
                        <th data-options="field:'caseType',width:160,halign:'center',align:'left',sortable:true">案件類型</th>
                        <th data-options="field:'createdDate',width:180,align:'center',formatter:formatToTimeStampIgnoreSecond,sortable:true">進件日期</th>
                        <th data-options="field:'dispatchDate',width:180,align:'center',formatter:formatToTimeStampIgnoreSecond,sortable:true">派工日期</th>
                        <th data-options="field:'acceptableFinishDate',width:180,align:'center',formatter:formatToTimeStampIgnoreSecond,width:200,sortable:true">應完修時間</th>
                        <th data-options="field:'completeDate',width:200,align:'center',formatter:formatToTimeStampIgnoreSecond,sortable:true">實際完成日</th>

                        <th data-options="field:'processType',width:160,halign:'center',align:'left',sortable:true">處理方式</th>
                        <th data-options="field:'installType',width:140,halign:'center',align:'left',sortable:true">裝機類型</th>
                        <th data-options="field:'attendanceTimes',width:140,halign:'center',align:'right',sortable:true">到場次數</th>
                        <th data-options="field:'attendanceDesc',width:140,halign:'center',align:'left',sortable:true">到場說明</th>
                        <th data-options="field:'rushRepairDesc',width:140,halign:'center',align:'left',sortable:true">延期說明</th>
                        <th data-options="field:'merchantCode',width:100,halign:'center',align:'left',sortable:true">特店代號</th>
                        <th data-options="field:'merchantName',width:160,halign:'center',align:'left',sortable:true">特店名稱</th>
                        <th data-options="field:'area',width:180,halign:'center',align:'left',sortable:true">特店區域</th>
                        <th data-options="field:'bussionAddress',width:200,halign:'center',align:'left',sortable:true">特店地址</th>

                        <th data-options="field:'assetName',width:160,halign:'center',align:'left',sortable:true">刷卡機型</th>
                        <th data-options="field:'model',width:160,halign:'center',align:'left',sortable:true">設備型號</th>
                        <th data-options="field:'wareHouseName',width:140,halign:'center',align:'left',sortable:true">倉別</th>
                        <th data-options="field:'installedAddress',width:140,halign:'center',align:'left',sortable:true">裝機地址</th>
                        <th data-options="field:'companyName',width:100,halign:'center',align:'left',sortable:true,styler: function (value, row, index) {
              return 'background-color:yellow;';
           }">裝機廠商</th>

                        <th data-options="field:'dtid',width:160,halign:'center',align:'left',sortable:true">DTID</th>
                        <th data-options="field:'tid',width:140,halign:'center',align:'left',sortable:true">TID</th>
                        <th data-options="field:'enableDate',width:180,align:'center',formatter:formaterTimeStampToyyyyMMDD,sortable:true">設備啟用日</th>
                        <th data-options="field:'serialNumber',halign:'center',align:'left',width:200,sortable:true">設備序號</th>
                        <th data-options="field:'propertyId',halign:'center',align:'left',width:200,sortable:true">財產編號</th>

                        <th data-options="field:'contractId',width:160,halign:'center',align:'left',sortable:true">合約編號</th>
                        <th data-options="field:'peripheralsName',width:140,halign:'center',align:'left',sortable:true">週邊設備1</th>
                        <th data-options="field:'peripheralsSerialNumber',halign:'center',align:'left',width:140,sortable:true">週邊設備1序號</th>
                        <th data-options="field:'peripheralsContractCode',halign:'center',align:'left',width:200,sortable:true">週邊設備1合約編號</th>
                        <th data-options="field:'peripherals2Name',width:100,halign:'center',align:'left',sortable:true">週邊設備2</th>
                        <th data-options="field:'peripherals2SerialNumber',halign:'center',align:'left',width:140,sortable:true">週邊設備2序號</th>
                        <th data-options="field:'peripherals2ContractCode',halign:'center',align:'left',width:200,sortable:true">週邊設備2合約編號</th>
                        <th data-options="field:'peripherals3Name',width:100,halign:'center',align:'left',sortable:true">週邊設備3</th>
                        <th data-options="field:'peripherals3SerialNumber',halign:'center',align:'left',width:140,sortable:true">週邊設備3序號</th>
                        <th data-options="field:'peripherals3ContractCode',halign:'center',align:'left',width:200,sortable:true">週邊設備3合約編號</th>
                        <th data-options="field:'functionTypeList',width:160,halign:'center',align:'left',sortable:true">設備開啟功能清單</th>
						
						<th data-options="field:'installedDate',width:180,align:'center',formatter:formatToTimeStamp,sortable:true">裝機日期</th>
						<th data-options="field:'updatedDescription',width:180,halign:'center',align:'left',sortable:true">參數異動說明</th>
						<th data-options="field:'description',width:180,halign:'center',align:'left',sortable:true">案件說明</th>
                        <th data-options="field:'ecrLine',width:180,halign:'center',align:'left',formatter:fomatterPositiveInt,sortable:true">ECR線</th>
                        <th data-options="field:'netLine',width:180,halign:'center',align:'left',formatter:fomatterPositiveInt,sortable:true">網路線</th>
                        <th data-options="field:'otherLine',width:180,halign:'center',align:'left',formatter:fomatterPositiveInt,sortable:true">耗材品項、個數、費用</th>
                        <th data-options="field:'delayTime',width:180,halign:'center',align:'right',formatter:formatCapacity,sortable:true">完修逾期時間</th>
                    </tr>
                </thead>
            </table> 
           <div style="text-align: right;padding: 10px"></div>
           <table id="checkDataGrid" class="easyui-datagrid" style="width: 100%; height: auto" title="查核費用資料清單"
                data-options="
			    rownumbers:true,
                pagination:true,
                pageList:[15,30,50,100],
				pageSize:15,
			    iconCls: 'icon-edit',
			    singleSelect: true,
			    selectOnCheck: true,
				pageNumber:0,
				nowrap : false,
			    method: 'get'
			    ">
                <thead>
                    <tr>
                       <th data-options="field:'customerName',width:160,halign:'center',align:'left',sortable:true">客戶</th>
                        <th data-options="field:'requirementNo',width:140,halign:'center',align:'left',sortable:true">需求單號</th>
                        <th data-options="field:'caseId',width:140,halign:'center',align:'left',sortable:true">案件編號</th>
                        <th data-options="field:'aoName',width:140,halign:'center',align:'left',sortable:true">業務人員</th>
                        <th data-options="field:'caseCategory',width:100,halign:'center',align:'left',sortable:true">案件類別</th>
                        <th data-options="field:'caseType',width:160,halign:'center',align:'left',sortable:true">案件類型</th>
                        <th data-options="field:'createdDate',width:180,align:'center',formatter:formatToTimeStampIgnoreSecond,sortable:true">進件日期</th>
                        <th data-options="field:'dispatchDate',width:180,align:'center',formatter:formatToTimeStampIgnoreSecond,sortable:true">派工日期</th>
                        <th data-options="field:'acceptableFinishDate',width:200,align:'center',formatter:formatToTimeStampIgnoreSecond,sortable:true">應完修時間</th>
                        <th data-options="field:'completeDate',width:200,align:'center',formatter:formatToTimeStampIgnoreSecond,sortable:true">實際完成日</th>

                        <th data-options="field:'processType',width:160,halign:'center',align:'left',sortable:true">處理方式</th>
                        <th data-options="field:'installType',width:140,halign:'center',align:'left',sortable:true">裝機類型</th>
                        <th data-options="field:'attendanceTimes',width:140,halign:'center',align:'right',sortable:true">到場次數</th>
                        <th data-options="field:'attendanceDesc',width:140,halign:'center',align:'left',sortable:true">到場說明</th>
                        <th data-options="field:'rushRepairDesc',width:140,halign:'center',align:'left',sortable:true">延期說明</th>
                        <th data-options="field:'merchantCode',width:100,halign:'center',align:'left',sortable:true">特店代號</th>
                        <th data-options="field:'merchantName',width:160,halign:'center',align:'left',sortable:true">特店名稱</th>
                        <th data-options="field:'area',width:180,halign:'center',align:'left',sortable:true">特店區域</th>
                        <th data-options="field:'bussionAddress',width:200,halign:'center',align:'left',sortable:true">特店地址</th>

                        <th data-options="field:'assetName',width:160,halign:'center',align:'left',sortable:true">刷卡機型</th>
                        <th data-options="field:'model',width:160,halign:'center',align:'left',sortable:true">設備型號</th>
                        <th data-options="field:'wareHouseName',width:140,halign:'center',align:'left',sortable:true">倉別</th>
                        <th data-options="field:'installedAddress',width:140,halign:'center',align:'left',sortable:true">裝機地址</th>
                        <th data-options="field:'companyName',width:100,halign:'center',align:'left',sortable:true,styler: function (value, row, index) {
              return 'background-color:yellow;';
           }">裝機廠商</th>

                        <th data-options="field:'dtid',width:160,halign:'center',align:'left',sortable:true">DTID</th>
                        <th data-options="field:'tid',width:140,halign:'center',align:'left',sortable:true">TID</th>
                        <th data-options="field:'enableDate',width:180,align:'center',formatter:formaterTimeStampToyyyyMMDD,sortable:true">設備啟用日</th>
                        <th data-options="field:'serialNumber',width:200,halign:'center',align:'left',sortable:true">設備序號</th>
                        <th data-options="field:'propertyId',width:200,halign:'center',align:'left',sortable:true">財產編號</th>

                        <th data-options="field:'contractId',width:160,halign:'center',align:'left',sortable:true">合約編號</th>

                    </tr>
                </thead>
            </table>
        </div>
    </div>
</div>


<script type="text/javascript">
	$(function (){
		$("#exportFlag").val("");
		//數據為0，初始化頁面或者 查無資料
		if($('#installDataGrid').datagrid('getData').total == 0 
			&& $('#uninstallDataGrid').datagrid('getData').total == 0
			&& $('#otherDataGrid').datagrid('getData').total == 0
			&& $('#checkDataGrid').datagrid('getData').total == 0){
			//給匯出設置樣式
			$('#export').attr("onclick","return false;");
			$('#export').attr("style","color:gray;");
		}
		$("#btnQuery").click(function(){
			//每頁筆數
			query(1,true);
		});
	});
	var flag = false;
	var queryCount = 0;
	//查詢數據 pageIndex 頁碼 isCleanMsg：是否自己點擊查詢按鈕的標誌
	function query(pageIndex, isCleanMsg) {
		queryCount = 0;
		flag = false;
		//var isInstant = $("#isInstant").prop("checked");
		var queryParam = $("#searchForm").form("getData");
		queryParam.queryCustomer = $("#queryCustomer").combobox('getValue');
		/* if(isInstant) {
			queryParam.isInstant = 'Y';
		} else {
			queryParam.isInstant = 'N';
		} */
		queryParam.isInstant = $("#isInstant").val();
		var controls = ['queryCustomer'];
		var queryForm = $("#searchForm");
		if(validateForm(controls) && queryForm.form("validate")){
			//裝機
			queryCaseInfo(pageIndex, isCleanMsg, queryParam, "installDataGrid");
			//拆機
			queryCaseInfo(pageIndex, isCleanMsg, queryParam, "uninstallDataGrid");
			//其他
			queryCaseInfo(pageIndex, isCleanMsg, queryParam, "otherDataGrid");
			//查檢
			queryCaseInfo(pageIndex, isCleanMsg, queryParam, "checkDataGrid");
		}
	}
	function queryCaseInfo(pageIndex, isCleanMsg, queryParam, datagrid){
		var queryAction = null;
		//判斷是那種案件類別
		if(datagrid == "installDataGrid") {
			//裝機
			queryParam.caseCategory = '${caseCategoryAttr.INSTALL.code }';
			queryAction = "queryInstall";
		} else if(datagrid == "uninstallDataGrid") {
			//拆機
			queryParam.caseCategory = '${caseCategoryAttr.UNINSTALL.code }';
			queryAction = "queryUnInstall";
		} else if(datagrid == "otherDataGrid") {
			//其他
			queryParam.caseCategory = "other";
			queryAction = "queryOther";
		} else {
			//查核
			queryParam.caseCategory = '${caseCategoryAttr.CHECK.code }';
			queryAction = "queryCheck";
		}
		var options = {
			url : "${contextPath}/edcPaymentReport.do?actionId="+queryAction,
			queryParams :queryParam,
			method:'post',
			pageNumber:pageIndex,
			//ignoreFirstLoad:true,
			onLoadSuccess:function(data){
				queryCount ++;
				if (isCleanMsg) {
					$("#message").text("");
					if (data.total == 0) {
						// 提示查無數據
						//$("#message").text(data.msg);
					} else {
						flag = true;
					}
					if(queryCount == 4) {
						if(!flag) {
							$("#message").text("查無資料");
							$('#export').attr("onclick","return false;");
							$('#export').attr("style","color:gray;");
						} else {
							$('#export').attr("onclick","exportData()");
							$('#export').attr("style","color:blue;");
						}
					}
				}
				isCleanMsg = true;
			},
			onLoadError : function() {
				$("#message").text("查詢失敗！請聯繫管理員");
			}
		}
		// 清空點選排序
		if(isCleanMsg){
			options.sortName = null;
		}
		openDlgGridQuery(datagrid, options);
	}
	//匯出
	function exportData(){
		var controls = ['queryCustomer'];
		var queryForm = $("#searchForm");
		//驗證表單頁面必輸欄位
		if(validateForm(controls) && queryForm.form("validate")){
			var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			$.blockUI(blockStyle);
			
			//var isInstant = $("#isInstant").prop("checked");
			var queryCustomer = $("#queryCustomer").combobox('getValue');
			$("#exportQueryCustomer").val(queryCustomer);
			/* if(isInstant) {
				$("#exportFlag").val('Y');
			} else {
				$("#exportFlag").val('N');
			} */
			//queryParam.isInstant = $("#isInstant").val();
			$("#exportFlag").val($("#isInstant").val());
			//提交表單
			actionClicked( document.forms["searchForm"],
				'${ucNo}',
				'',
				'<%=IAtomsConstants.ACTION_EXPORT%>');
			
			ajaxService.getExportFlag('${ucNo}',function(data){
				$.unblockUI();
			});
			
			$('#export').attr("onclick","return exportData();");
			$('#export').attr("style","color:blue;");
		}
	}
function fomatterYesOrNoInNinety(val, row){
	if(val == null) {
		return "";
	} else if (val < 90) {
		return "是";
	} else {
		return "";
	}
}
function fomatterYesOrNoIn120(val, row){
	if(val == null) {
		return "";
	} else if (val < 120) {
		return "是";
	} else if (val > 120){
		return "否";
	} 
}
function fomatterPositiveInt(val, row){
	if(val == null) {
		return "";
	} else {
		var stringVal = val.split(",");
		var tempval = "";
		for(var i =0; i<stringVal.length; i++) {
			val += stringVal[i].split(".")[0] + ",";
			if(tempval == "") {
				tempval = stringVal[i].split(".")[0] + ",";
			} else {
				tempval += stringVal[i].split(".")[0] + ",";
			}
		}
		val = tempval.substring(0,tempval.length-1);
		return val;
	}
}
</script>
</body>
</html>