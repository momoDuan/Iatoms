<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.PropertyNumberImportFormDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.web.taglib.FileUpLoad"%>
<%@page import="cafe.workflow.web.controller.util.WfSessionHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	PropertyNumberImportFormDTO formDTO = null;
	if (ctx != null) {
		formDTO = (PropertyNumberImportFormDTO) ctx.getResponseResult();
	} 
	if (formDTO == null) {
		formDTO = new PropertyNumberImportFormDTO();
	}
	//獲取useCaseNo
	String ucNo = formDTO.getUseCaseNo();
	//获取上传最大限制
	String uploadFileSize = formDTO.getUploadFileSize();
	
	List<Parameter> monthYears = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, PropertyNumberImportFormDTO.UPDATE_MONTH_YEAR_LISY);
%>
<!DOCTYPE html >
<%@ include file="/jsp/common/common.jsp" %>
<html>
<c:set var="uploadFileSize" value="<%=uploadFileSize%>" scope="page"></c:set>
<c:set var="monthYears" value="<%=monthYears%>" scope="page"></c:set>

<head>
	<%@include file="/jsp/common/meta.jsp"%>
	<%@include  file="/jsp/common/easyui-common.jsp" %>
 	<link rel="stylesheet" href="${contextPath}/css/fineuploader-5.0.2.css"/>
	<script type="text/javascript" src="${contextPath}/js/fineuploader-5.0.2.js"></script>
</head>
<body>
<div style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
	<!-- <div id="p" class="easyui-panel" style="width: 98%; height: auto;"> -->
		<!-- <div title="" style="padding: 10px"> -->
		<table id="dataGrid" >
        </table>
       <!--  </div> -->
        <div id="tb" style="padding: 2px 5px;">
        	<table style="width: 98%; height: auto;">
        		<tr>
        			<td style="width: 8%">
        				異動目的地:
        			</td>
        			<td style="width: 14%">
        				<input type="radio" name="updateTable" id="newAssetTable" value = "1" checked="checked" onclick="checkUpdateTable(this);"/>設備檔
						<input type="radio" name="updateTable" id="monthAssetTable" value = "2" onclick="checkUpdateTable(this);"/>設備月檔
        			</td>
        			<td>
        				<cafe:droplisttag
							id="monthYear"
							name="monthYear" 
							css="easyui-combobox"
							hasBlankValue="true"
							blankName="請選擇"
							result="${monthYears }"
							disabled="true"
							style="width:100px"
							javascript="validType='requiredDropList' editable=false required=true  invalidMessage=\"請輸入月份\" panelheight=\"auto\""
						></cafe:droplisttag>
        			</td>
        		</tr>
        		<tr>
        			<td>
        				異動欄位:
        			</td>
        			<td colspan="2">
        				<input type="checkbox" name="updateColumn" value = "assetTypeName"/>設備名稱
						<input type="checkbox" name="updateColumn" value = "propertyId"/>財產編號
						<input type="checkbox" name="updateColumn" value = "contractCode"/>合約編號
						<input type="checkbox" name="updateColumn" value = "simEnableNo"/>租賃編號
						<input type="checkbox" name="updateColumn" value = "enableDate"/>設備啟用日
						<input type="checkbox" name="updateColumn" value = "assetOwnerName"/>資產Owner
						<input type="checkbox" name="updateColumn" value = "assetUserName"/>使用人
						<input type="checkbox" name="updateColumn" value = "cyberApprovedDate"/>Cyber驗收日期
						<input type="checkbox" name="updateColumn" value = "assetInTime"/>入庫日期
						<input type="checkbox" name="updateColumn" value = "simEnableDate"/>租賃期滿日
						<input type="checkbox" name="updateColumn" value = "counter"/>櫃位
						<input type="checkbox" name="updateColumn" value = "cartonNo"/>箱號
        			</td>
        		</tr>
        		<tr>
        			<td>
        			</td>
        			<td colspan="2">
        				
						<input type="checkbox" name="updateColumn" value = "brand"/>設備廠牌
						<input type="checkbox" name="updateColumn" value = "model"/>設備型號
						<input type="checkbox" name="updateColumn" value = "maintenanceMode"/>維護模式
						<input type="checkbox" name="updateColumn" id="taixinLeaseMaintenance" value = "taixinMaintenance"/>台新租賃維護
						<input type="checkbox" name="updateColumn" id="jdwMaintenance" value = "jdwMaintenance"/>捷達威維護
        			</td>
        		</tr>
        	</table>
        	<!-- <div style="display: inline;">
				異動欄位:
				<input type="checkbox" name="updateColumn" value = "assetTypeName"/>設備名稱
				<input type="checkbox" name="updateColumn" value = "propertyId"/>財產編號
				<input type="checkbox" name="updateColumn" value = "contractCode"/>合約編號
				<input type="checkbox" name="updateColumn" value = "simEnableNo"/>租賃編號
				<input type="checkbox" name="updateColumn" value = "enableDate"/>設備啟用日
				<input type="checkbox" name="updateColumn" value = "assetOwnerName"/>資產Owner
				<input type="checkbox" name="updateColumn" value = "assetUserName"/>使用人
				<input type="checkbox" name="updateColumn" value = "cyberApprovedDate"/>Cyber驗收日期
				<input type="checkbox" name="updateColumn" value = "assetInTime"/>入庫日期
				<input type="checkbox" name="updateColumn" value = "simEnableDate"/>租賃期滿日
				<br/>
				<input type="checkbox" name="updateColumn" value = "counter"/>櫃位
				<input type="checkbox" name="updateColumn" value = "cartonNo"/>箱號
				<input type="checkbox" name="updateColumn" value = "brand"/>設備廠牌
				<input type="checkbox" name="updateColumn" value = "model"/>設備型號
				<input type="checkbox" name="updateColumn" value = "maintenanceMode"/>維護模式
				<input type="checkbox" name="updateColumn" value = "updateTableName"/>異動目的地
				<input type="checkbox" name="updateColumn" id="taixinLeaseMaintenance" value = "taixinMaintenance"/>台新租賃維護
				<input type="checkbox" name="updateColumn" id="jdwMaintenance" value = "jdwMaintenance"/>捷達威維護
			</div> -->
			
			
			<div style="text-align: right; padding: 10px;">
				<a href="#" onclick="downLoad('');" id="btnDownload" style="color: blue;">匯入格式下載</a>&nbsp;&nbsp;
				<cafe:fileuploadTag 
					id="fileupLoad"
					name="fileupLoad"
					uploadUrl="${contextPath}/propertyImport.do" 
					showFileList="false"
					acceptFiles=".xls,.xlsx"
					allowedExtensions="'xls', 'xlsx'"
					messageId = "msg"
					sizeLimit = "${uploadFileSize }"
					showName="匯入"
					messageAlert="false"
					isCustomError = "true"
					whetherImport="true"
					whetherDelete="false"
					multiple="false"
					whetherDownLoad="false"
					showUnderline = "true"
					javaScript="onSubmit:upLoad,onComplete:showMessage"
					>
 				</cafe:fileuploadTag>
				<a id="btnConfirmImport" class="easyui-linkbutton" style="" data-options="iconCls:'icon-ok'" onclick="save()" disabled = "true">確認匯入</a>
			</div>
			<span id="msg" class="red"></span>
			</div>
		<div style="text-align: right;padding: 10px"></div>	
		<table id="dataGridTaiXin" >
		</table>
		<div style="text-align: right;padding: 10px"></div>	
		<table id="dataGridJdw" >
        </table>
        <input type="hidden" id="propertyFileName"/>
		<!-- </div> -->
	<!-- </div> -->
</div>
</body>
<script type="text/javascript">
	//隐藏提示讯息栏位
	$(function(){
		$("#fileupLoad").click(function(){
			$('#msg').text('');
			var updateTable = $('input[name=\"updateTable\"]:checked').val();
			if (updateTable == '2') {
				var controls = ['monthYear'];
				if(!validateForm(controls)) {
					return false;
				}
			}
			var updateColumn = $('input[name=\"updateColumn\"]:checked').length;
			if (updateColumn == '0') {
				$('#msg').text('請輸入異動欄位');
				return false;
			}
		});
		// 查詢datagrid初始化
		setTimeout("initBatchDatagrid();",5);
		setTimeout("initTaiXinDatagrid();",10);
		setTimeout("initJdwDatagrid();",15);
	});

	/*
	*初始化查詢datagrid
	*/
	function initBatchDatagrid(){
		var grid = $("#dataGrid");
		if(!grid.hasClass("easyui-datagrid")){
			grid.attr("style", "width: 98%; height: auto;");
			// 查詢列
			var datagridColumns = [
				{field:'serialNumber',halign:'center',width:150,title:"設備序號"},
				{field:'assetTypeName',halign:'center',width:150,title:"設備名稱"},
				{field:'oldAssetType',halign:'center',sortable:true,hidden:true,width:150,title:"原設備名稱"},
				{field:'propertyId',halign:'center',sortable:true,hidden:true,width:120,title:"財產編號"},
				{field:'oldPropertyId',halign:'center',sortable:true,hidden:true,width:120,title:"原財產編號"},
				{field:'contractCode',halign:'center',sortable:true,hidden:true,width:120,title:"合約編號"},
				{field:'oldContractCode',halign:'center',sortable:true,hidden:true,width:120,title:"原合約編號"},
				{field:'simEnableNo',halign:'center',sortable:true,hidden:true,width:120,title:"租賃編號"},
				{field:'oldSimEnableNo',halign:'center',sortable:true,hidden:true,width:120,title:"原租賃編號"},
				{field:'enableDate',halign:'center',align:'center',sortable:true,formatter:formaterTimeStampToyyyyMMDD,hidden:true,width:100,title:"設備啟用日"},
				{field:'oldEnableDate',halign:'center',align:'center',sortable:true,formatter:formaterTimeStampToyyyyMMDD,hidden:true,width:100,title:"原設備啟用日"},
				{field:'assetOwnerName',halign:'center',sortable:true,hidden:true,width:120,title:"資產Owner"},
				{field:'oldAssetOwner',sortable:true,hidden:true,width:120,title:"原資產Owner"},
				{field:'assetUserName',halign:'center',sortable:true,hidden:true,width:120,title:"使用人"},
				{field:'oldAssetUser',halign:'center',sortable:true,hidden:true,width:120,title:"原使用人"},
				{field:'cyberApprovedDate',halign:'center',align:'center',sortable:true,formatter:formaterTimeStampToyyyyMMDD,hidden:true,width:120,title:"Cyber驗收日期"},
				{field:'oldCyberApprovedDate',align:'center',sortable:true,formatter:formaterTimeStampToyyyyMMDD,hidden:true,width:130,title:"原Cyber驗收日期"},
				{field:'assetInTime',halign:'center',align:'center',sortable:true,formatter:formaterTimeStampToyyyyMMDD,hidden:true,width:100,title:"入庫日期"},
				{field:'oldAssetInTime',halign:'center',align:'center',sortable:true, formatter:formaterTimeStampToyyyyMMDD,hidden:true,width:120,title:"原入庫日期"},
				{field:'simEnableDate',halign:'center',align:'center',sortable:true,hidden:true,formatter:formaterTimeStampToyyyyMMDD,hidden:true,width:120,title:"租賃期滿日"},
				{field:'oldSimEnableDate',halign:'center',align:'center',sortable:true,hidden:true,formatter:formaterTimeStampToyyyyMMDD,hidden:true,width:120,title:"原租賃期滿日"},
				
				{field:'counter',halign:'center',sortable:true,hidden:true,width:120,title:"櫃位"},
				{field:'oldCounter',sortable:true,hidden:true,width:120,title:"原櫃位"},
				{field:'cartonNo',halign:'center',sortable:true,hidden:true,width:120,title:"箱號"},
				{field:'oldCartonNo',halign:'center',sortable:true,hidden:true,width:120,title:"原箱號"},
				
				{field:'brand',halign:'center',sortable:true,hidden:true,width:120,title:"設備廠牌"},
				{field:'oldBrand',sortable:true,hidden:true,width:120,title:"原設備廠牌"},
				{field:'model',halign:'center',sortable:true,hidden:true,width:120,title:"設備型號"},
				{field:'oldModel',halign:'center',sortable:true,hidden:true,width:120,title:"原設備型號"},
				{field:'maintenanceMode',halign:'center',sortable:true,hidden:true,width:120,title:"維護模式"},
				{field:'oldMaintenanceMode',halign:'center',sortable:true,hidden:true,width:120,title:"原維護模式"},
				{field:'updateTableName',halign:'center',align:'center',sortable:true,hidden:true,width:130,title:"異動目的地"},
				
				{field:'message',halign:'center',align:'center',sortable:true,hidden:true,width:80,title:"訊息"},
				{field:'assetTypeId',halign:'center',sortable:true,hidden:true,hidden:true,title:"設備ID"},
				{field:'contractId',halign:'center',sortable:true,hidden:true,hidden:true,title:"合約ID"},
				{field:'assetOwner',halign:'center',sortable:true,hidden:true,hidden:true,title:"資產OwnerID"},
				{field:'assetUser',halign:'center',sortable:true,hidden:true,hidden:true,title:"使用人ID"},
				{field:'assetId',halign:'center',sortable:true,hidden:true,hidden:true,title:"ID"},
				{field:'assetInId',halign:'center',hidden:true,title:"assetInId"},
				{field:'contractId',halign:'center',hidden:true,title:"contractId"}
			];
			// 創建datagrid
			grid.datagrid({
				title:"設備資料批次異動",
				columns:[datagridColumns],
                rownumbers:true,
                pagination:true,
				iconCls: 'icon-edit',
				nowrap : false,
				method: 'get',
				pageList:[15,30,50,100],
				pageSize:15,
				singleSelect:true,
				pageNumber:0,
				remoteSort:true,
                sortOrder:'asc',
				toolbar:'#tb'
			});
			
		}
	}
	
	/*
	*初始化查詢datagrid2
	*/
	function initTaiXinDatagrid(){
		var grid = $("#dataGridTaiXin");
		if(!grid.hasClass("easyui-datagrid")){
			grid.attr("style", "width: 98%; height: auto;");
			// 查詢列
			var datagridColumns = [
				{field:'serialNumber',halign:'center',width:150,title:"設備序號"},
				{field:'dtid',halign:'center',width:100,title:"DTID"},
				{field:'oldDtid',halign:'center',width:100,title:"原DTID"},
				{field:'caseId',halign:'center',width:120,title:"案件編號"},
				{field:'oldCaseId',halign:'center',width:130,title:"原案件編號"},
				{field:'merchantCode',halign:'center',width:130,title:"特店代號"},
				{field:'oldMerchantCode',halign:'center',width:140,title:"原特店代號"},
				{field:'headerName',halign:'center',width:160,title:"表頭（同對外名稱）"},
				{field:'oldMerchantHeaderId',halign:'center',width:180,title:"原表頭（同對外名稱）"},
				{field:'maintainCompanyName',halign:'center',width:100,title:"維護廠商"},
				{field:'oldMaintainCompany',halign:'center',width:120,title:"原維護廠商"},
				{field:'enableDate',halign:'center',align:'center',width:100,sortable:true,formatter:formaterTimeStampToyyyyMMDD,title:"設備啟用日"},
				{field:'oldEnableDate',halign:'center',align:'center',width:110,sortable:true,formatter:formaterTimeStampToyyyyMMDD,title:"原設備啟用日"},
				{field:'maintainUserName',halign:'center',width:100,title:"維護工程師"},
				{field:'oldMaintainUser',halign:'center',width:110,title:"原維護工程師"},
				{field:'caseCompletionDate',halign:'center',width:100,align:'center',sortable:true,formatter:formaterTimeStampToyyyyMMDD,title:"完修日期"},
				{field:'oldCaseCompletionDate',halign:'center',width:100,align:'center',sortable:true,formatter:formaterTimeStampToyyyyMMDD,title:"原完修日期"},
				{field:'analyzeDate',halign:'center',width:100,align:'center',sortable:true,formatter:formaterTimeStampToyyyyMMDD,title:"簽收日期"},
				{field:'oldAnalyzeDate',halign:'center',width:100,align:'center',sortable:true,formatter:formaterTimeStampToyyyyMMDD,title:"原簽收日期"},
				{field:'isBussinessAddress',halign:'center',align:'center',width:180,formatter:formatterCheckbox,title:"裝機地址-同營業地址"},
				{field:'installedAdressLocationName',halign:'center',width:140,title:"裝機地址-縣市"},
				{field:'oldInstalledAdressLocation',halign:'center',width:150,title:"原裝機地址-縣市"},
				{field:'installedAdress',halign:'center',width:200,title:"裝機地址"},
				{field:'oldInstalledAdress',halign:'center',width:200,title:"原裝機地址"},
				{field:'isCup',halign:'center',align:'center',formatter:formatterCheckbox,title:"銀聯"},
				{field:'oldIsCup',halign:'center',align:'center',formatter:formatterCheckbox,title:"原銀聯"},
				
				{field:'merchantId',halign:'center',hidden:true,title:"merchantId"},
				{field:'maintainCompany',halign:'center',hidden:true,title:"maintainCompany"},
				{field:'merchantHeaderId',halign:'center',hidden:true,title:"merchantHeaderId"},
				{field:'maintainUser',halign:'center',hidden:true,title:"maintainUser"},
				{field:'installedAdressLocation',halign:'center',hidden:true,title:"installedAdressLocation"},
				{field:'status',halign:'center',hidden:true,title:"status"},
				{field:'isEnabled',halign:'center',hidden:true,title:"isEnabled"},
				{field:'assetId',halign:'center',hidden:true,title:"assetId"}
			];
			// 創建datagrid
			grid.datagrid({
				title:"台新租賃維護",
				columns:[datagridColumns],
				rownumbers:true,
				pagination:true,
				iconCls: 'icon-edit',
				nowrap : false,
				method: 'get',
				pageList:[15,30,50,100],
				pageSize:15,
				singleSelect:true,
				pageNumber:0,
				remoteSort:true,
				sortOrder:'asc'
			});
			
		}
	}
	
	/*
	*初始化查詢datagrid3
	*/
	function initJdwDatagrid(){
		var grid = $("#dataGridJdw");
		if(!grid.hasClass("easyui-datagrid")){
			grid.attr("style", "width: 98%; height: auto;");
			// 查詢列
			var datagridColumns = [
				{field:'serialNumber',halign:'center',width:150,title:"設備序號"},
				{field:'dtid',halign:'center',width:100,title:"DTID"},
				{field:'oldDtid',halign:'center',width:100,title:"原DTID"},
				{field:'caseId',halign:'center',width:120,title:"案件編號"},
				{field:'oldCaseId',halign:'center',width:130,title:"原案件編號"},
				{field:'merchantCode',halign:'center',width:130,title:"特店代號"},
				{field:'oldMerchantCode',halign:'center',width:130,title:"原特店代號"},
				{field:'headerName',halign:'center',width:160,title:"表頭（同對外名稱）"},
				{field:'oldMerchantHeaderId',halign:'center',width:180,title:"原表頭（同對外名稱）"},
				{field:'maintainCompanyName',halign:'center',width:100,title:"維護廠商"},
				{field:'oldMaintainCompany',halign:'center',width:120,title:"原維護廠商"},
				{field:'enableDate',halign:'center',align:'center',width:100,sortable:true,formatter:formaterTimeStampToyyyyMMDD,title:"設備啟用日"},
				{field:'oldEnableDate',halign:'center',align:'center',width:110,sortable:true,formatter:formaterTimeStampToyyyyMMDD,title:"原設備啟用日"},
				{field:'maintainUserName',halign:'center',width:100,title:"維護工程師"},
				{field:'oldMaintainUser',halign:'center',width:110,title:"原維護工程師"},
				{field:'caseCompletionDate',halign:'center',width:100,align:'center',sortable:true,formatter:formaterTimeStampToyyyyMMDD,title:"完修日期"},
				{field:'oldCaseCompletionDate',halign:'center',width:100,align:'center',sortable:true,formatter:formaterTimeStampToyyyyMMDD,title:"原完修日期"},
				{field:'analyzeDate',halign:'center',width:100,align:'center',sortable:true,formatter:formaterTimeStampToyyyyMMDD,title:"簽收日期"},
				{field:'oldAnalyzeDate',halign:'center',width:100,align:'center',sortable:true,formatter:formaterTimeStampToyyyyMMDD,title:"原簽收日期"},
				{field:'isBussinessAddress',halign:'center',align:'center',width:180,formatter:formatterCheckbox,title:"裝機地址-同營業地址"},
				{field:'installedAdressLocationName',halign:'center',width:140,title:"裝機地址-縣市"},
				{field:'oldInstalledAdressLocation',halign:'center',width:150,title:"原裝機地址-縣市"},
				{field:'installedAdress',halign:'center',width:200,title:"裝機地址"},
				{field:'oldInstalledAdress',halign:'center',width:200,title:"原裝機地址"},
				{field:'isCup',halign:'center',align:'center',formatter:formatterCheckbox,title:"銀聯"},
				{field:'oldIsCup',halign:'center',align:'center',formatter:formatterCheckbox,title:"原銀聯"}
			];
			// 創建datagrid
			grid.datagrid({
				title:"捷達威維護",
				columns:[datagridColumns],
				rownumbers:true,
				pagination:true,
				iconCls: 'icon-edit',
				nowrap : false,
				method: 'get',
				pageList:[15,30,50,100],
				pageSize:15,
				singleSelect:true,
				pageNumber:0,
				remoteSort:true,
				sortOrder:'asc'
			});
			
		}
	}
	
	//獲取需要異動的欄位
	function getUpdateColumns(isCkeck) {
		var updateColumn = document.getElementsByName("updateColumn");
		var array = [];
		var map = {}
		for (var i=0; i<updateColumn.length; i++) {
			if (updateColumn[i].checked) {
				if (isCkeck) {
					map[updateColumn[i].value] = updateColumn[i].value;
				} else {
					array.push(updateColumn[i].value);
				}
			}
		}
		if (isCkeck) {
			return map;
		} else {
			return array;
		}
		
	}
	
	function upLoad(id,name) {
		var updateColumn = $("input[name='updateColumn']:checked").length;
		var updateTable = $('input[name=\"updateTable\"]:checked').val();
		var monthYear;
		if (updateTable == '2') {
			monthYear = $("#monthYear").combobox("getValue");
		}
		if (updateColumn == '0') {
			$("#msg").text("請輸入異動欄位");
		} else {
			var map = getUpdateColumns(true);
			fileupLoad.setParams({'actionId':'checkAssetData', <%=PropertyNumberImportFormDTO.UPDATE_COLUMN_LIST %>:JSON.stringify(map),
				updateTable:updateTable, monthYear:monthYear});
		}
	}
	//匯入返回處理
	function showMessage(id, fileName, response, xhr) {
		//如果session過期
		if(!sessionTimeOut(xhr)) {
			return false;
		}
		//按照異動欄位修改列
		if ($("#dataGrid").datagrid("options").pageNumber == 0) {
		//	$('#dataGrid').datagrid({pageNumber:1});	
			$("#dataGrid").datagrid("options").pageNumber = 1;
		}
		$('#dataGrid').datagrid({pageNumber:1});
		if ($("#dataGridTaiXin").datagrid("options").pageNumber == 0) {
			$('#dataGridTaiXin').datagrid({pageNumber:1});	
		}
		$('#dataGridTaiXin').datagrid({pageNumber:1});
		if ($("#dataGridJdw").datagrid("options").pageNumber == 0) {
			$('#dataGridJdw').datagrid({pageNumber:1});	
		}
		$('#dataGridJdw').datagrid({pageNumber:1});
		$("#msg").empty();
		$("#msg").text(response.msg);
		//列的字段
		var allCols = $("#dataGrid").datagrid("getColumnFields");
		//動態顯示
		var array = getUpdateColumns(false);
		for (var m = 0; m < allCols.length; m ++) {
			$("#dataGrid").datagrid("showColumn",allCols[m]);
		}
		for(var i=0; i<array.length; i++){
			var fieldName = array[i];
			for (var j = 0; j < allCols.length; j ++) {
				if (fieldName == allCols[j]) {
					allCols.splice(j,1);
					allCols.splice(j++,1);
					break;
				}
			}
		}
		for(var i=0; i<allCols.length; i++) {
			$("#dataGrid").datagrid("hideColumn",allCols[i]);
		}
		$("#dataGrid").datagrid("showColumn",'serialNumber');
		$("#dataGrid").datagrid("showColumn",'assetTypeName');
		$("#dataGrid").datagrid("showColumn",'message');
		if (response.success) {
			if (response.total != 0 || response.taixinListTotal != 0 ||response.jdwMaintenanceTotal != 0) {
				$("#propertyFileName").val(response.fileName);
				$('#btnConfirmImport').linkbutton('enable');
				//加載基礎數據
				$('#dataGrid').datagrid({loadFilter:loadDataBaseInfo}).datagrid('loadData', response);
				//如果勾選了台新租賃維護，則加載台新租賃數據
				if ($('#taixinLeaseMaintenance').is(':checked')) {
					if (response.taixinList) {
						$('#dataGridTaiXin').datagrid({loadFilter:loadDataTaiXin}).datagrid('loadData', response.taixinList);
					}
				} else {
					$('#dataGridTaiXin').datagrid('loadData', { total: 0, rows: [] });
				}
				//如果勾選了捷達威維護，則加載捷達威維護數據
				if ($('#jdwMaintenance').is(':checked')) {
					if (response.jdwMaintenances) {
						$('#dataGridJdw').datagrid({loadFilter:loadDataJdw}).datagrid('loadData', response.jdwMaintenances);
					}
				} else {
					$('#dataGridJdw').datagrid('loadData', { total: 0, rows: [] });
				}
			} else {
				refreshGridDate();
				$('#btnConfirmImport').linkbutton('disable');
			}
		} else {
			if (response.errorFileName != undefined && response.errorFilePath != undefined) {
				$('#btnConfirmImport').linkbutton('disable');
				refreshGridDate();
				$.messager.confirm('確認對話框','匯入失敗，是否下載匯入錯誤檔？', function(result){
					if (result) {
						createSubmitForm("${contextPath}/propertyImport.do","actionId=<%=IAtomsConstants.ACTION_DOWNLOAD_ERROR_FILE %>&errorFileName=" + response.errorFileName + "&errorFilePath=" + response.errorFilePath,"post");
					}
				});	
			} else {
				refreshGridDate();
				$('#btnConfirmImport').linkbutton('disable');
			}
		}
	}
	
	function loadDataBaseInfo(data){
		var dg = $("#dataGrid");
		return pagerFilter(data, dg);
	}
	
	function loadDataTaiXin(data) {
		var dg = $("#dataGridTaiXin");
		return pagerFilter(data, dg);
	}
	
	function loadDataJdw(data) {
		var dg = $("#dataGridJdw");
		return pagerFilter(data, dg);
	}
	
	function pagerFilter(data, dg){
		if (typeof data.length == 'number' && typeof data.splice == 'function'){    // 判断数据是否是数组
			data = {
				total: data.length,
				rows: data
			}
		}
		var opts = dg.datagrid('options');
		var pager = dg.datagrid('getPager');
		pager.pagination({
			onSelectPage:function(pageNum, pageSize){
				$("#msg").text("");
				opts.pageNumber = pageNum;
				opts.pageSize = pageSize;
				pager.pagination('refresh',{
					pageNumber:pageNum,
					pageSize:pageSize
				});
				dg.datagrid('loadData',data);
			}
		});
		if (!data.originalRows){
			data.originalRows = (data.rows);
		}
		var start = (opts.pageNumber-1)*parseInt(opts.pageSize);
		var end = start + parseInt(opts.pageSize);
		data.rows = (data.originalRows.slice(start, end));
		return data;
	}
	
	
	//下载
	function downLoad(fileName){
		$("#msg").empty();
		ajaxService.checkExsitDownLoadFile(fileName,function(data){
			if (data){
				createSubmitForm("${contextPath}/propertyImport.do","actionId=download","post");
			} else {
				$("#msg").text('文件不存在');
			}
		});	
	}
	
	//將財產編號匯入
	function save () {
		$("#msg").empty();
		//拿到集合的所有的行
		var assetRows = $("#dataGrid").datagrid('getData');
		var taiXinRows = $("#dataGridTaiXin").datagrid('getData');
		var jdwRows = $("#dataGridJdw").datagrid('getData');
		//var allCols = $("#dataGrid").datagrid("getColumnFields");
		var map = getUpdateColumns(true);
		var name = $("#propertyFileName").val();
		//將所有的行數據轉化為JSON格式
		var assetListRow = JSON.stringify(assetRows.originalRows);
		var assetListTaiXinRow = JSON.stringify(taiXinRows.originalRows);
		var assetListJdwRow = JSON.stringify(jdwRows.originalRows);
		var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		// 形成遮罩
		$.blockUI(blockStyle1);
		$.ajax({
			url:"${contextPath}/propertyImport.do?actionId=<%=IAtomsConstants.ACTION_UPLOAD%>",
			type:'post', 
			cache:false, 
			data:{fileName:name,
					<%=PropertyNumberImportFormDTO.UPDATE_COLUMN_LIST %>:JSON.stringify(map)},
			dataType:'json', 
			success:function(json) {
				if (json.success) {
					refreshGridDate();
					$("#msg").text(json.msg);	
					$('#btnConfirmImport').linkbutton('disable');
				}else{
					$("#msg").text(json.msg);
				}
				// 去除遮罩
				$.unblockUI();
			},
			error:function(){
				parent.$.messager.progress('close');
				$.messager.alert('提示', "設備資料批次匯入失敗", 'error');
				// 去除遮罩
				$.unblockUI();
			}
		});
	}
	function formatterCheckbox(value,row,index){
		if (value == 'Y') {
			return '<input type="checkbox" checked="checked" disabled>';
		} else {
			return '<input type="checkbox" disabled>';
		}
	}
	/*
	* 刷新三個表格數據
	*/
	function refreshGridDate() {
		$('#dataGrid').datagrid('loadData', { total: 0, rows: [] });
		$('#dataGridTaiXin').datagrid('loadData', { total: 0, rows: [] });
		$('#dataGridJdw').datagrid('loadData', { total: 0, rows: [] });
	}
	
	/*
	點選單筆按鈕，輸入欄位制空且變為不可編輯狀態
	*/
	function checkUpdateTable(obj) {
		if(obj.id == "newAssetTable") {
			$("#monthYear").combobox("setValue","");
			$("#monthYear").combobox("disable");
			$("#monthYear").combobox("disableValidation");
		} else {
			$("#monthYear").combobox("enable");
			$("#monthYear").combobox("enableValidation");
		}
	}
</script>
</html>	