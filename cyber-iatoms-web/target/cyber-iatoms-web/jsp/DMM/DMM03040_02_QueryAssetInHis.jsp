<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetInInfoFormDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.AssetInInfoDTO"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	AssetInInfoFormDTO formDTO = null;
	String ucNo = null;
	if (ctx != null) {
		formDTO = (AssetInInfoFormDTO) ctx.getResponseResult();
	} 
	if (formDTO == null) {
		formDTO = new AssetInInfoFormDTO();
		ucNo = IAtomsConstants.UC_NO_DMM_03040;
	} else {
		ucNo = formDTO.getUseCaseNo();
	}
	//合約編號集合
	List<Parameter> contractList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IAtomsConstants.ACTION_GET_CONTRACT_LIST);
	//客戶
	List<Parameter> customerList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_COMPANY_LIST);
	//已入庫批號集合
	List<Parameter> assetInList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IAtomsConstants.ACTION_LIST_ASSET_IN_LIST);
%>
<c:set var="contractList" value="<%=contractList %>"></c:set>
<c:set var="customerList" value="<%=customerList %>"></c:set>
<c:set var="assetInList" value="<%=assetInList %>"></c:set>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<%@include  file="/jsp/common/easyui-common.jsp" %>
</head>
<body>
	<div title="歷史入庫查詢"">
		<div>
			<span id="hisMsg" class="red"></span>
		</div>
		<table id="assetHisDataGrid" >
		</table>
		<div id="tb" style="padding: 2px 5px;">
			入庫批號:
			<cafe:droplisttag
				css="easyui-combobox"
				id="<%=AssetInInfoFormDTO.QUERY_ASSET_IN_ID %>"
				name="<%=AssetInInfoFormDTO.QUERY_ASSET_IN_ID %>" result="${assetInList }"
				javascript="editable=false data-options=\"valueField:'value',width:170,textField:'name'\""
				blankName="請選擇" hasBlankValue="true"
			></cafe:droplisttag>
			客戶:
			<cafe:droplisttag
				css="easyui-combobox"
				id="<%=AssetInInfoFormDTO.QUERY_COMPANY_ID %>"
				name="<%=AssetInInfoFormDTO.QUERY_COMPANY_ID %>" result="${customerList }"
				javascript="editable=false data-options=\"valueField:'value',width:190,textField:'name'\""
				blankName="請選擇" hasBlankValue="true"
			></cafe:droplisttag>
			合約編號: 
			<cafe:droplisttag
				css="easyui-combobox"
				id="<%=AssetInInfoFormDTO.QUERY_CONTRACT_ID %>"
				name="<%=AssetInInfoFormDTO.QUERY_CONTRACT_ID %>" result="${contractList }"
				blankName="請選擇" hasBlankValue="true"
				javascript="editable=false data-options=\"valueField:'value',textField:'name',width:190\""
			></cafe:droplisttag>
			<a class="easyui-linkbutton"  onclick="query();" iconcls="icon-search">查詢</a>
		</div>
	</div>
	<script type="text/javascript">
		//初始化實際驗收與客戶實際驗收複選框
		function settingCheckbox(value, row, index){
			if (value == "Y") {
				return '<input type="checkbox" checked="checked" disabled>';
			} else {
				return '<input type="checkbox" disabled>';
			}
		}
		//歷史入庫資料查詢條件
		function getQueryParams(){
			var queryParams={
				queryContractId:$("#queryContractId").combobox('getValue'),
				queryAssetInId:$("#queryAssetInId").combobox('getValue'),
				queryCompanyId:$("#queryCompanyId").combobox('getValue'),
				actionId:"<%=IAtomsConstants.ACTION_QUERY%>"
			};
			return queryParams;	
		}
		//查詢歷史入庫資料
		function query(){
			$("#hisMsg").text("");
			var queryParam = getQueryParams();  
			if (queryParam.queryContractId == "" && queryParam.queryAssetInId == "") {
				$("#hisMsg").text("請選擇入庫批號或合約編號");
				return false;
			}
			var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			$("#assetHisDataGrid").datagrid({
				url : "${contextPath}/assetInHis.do",
				queryParams :queryParam,
				pageNumber:1,
				loadMsg:'',
				sortName:'',
				autoRowHeight:true,
				onBeforeLoad :function () {
					// 形成遮罩
					$.blockUI(blockStyle);
				},
				onLoadError : function(data) {
					$("#hisMsg").text('查詢歷史入庫資料出錯');
					$.unblockUI();
				},
				onLoadSuccess:function(data){
					$("#hisMsg").text(data.msg);
					$.unblockUI();
				}
			});
		}
		
		/*
		*初始化查詢datagrid1
		*/
		function initAssetHisDatagrid(){
			var grid = $("#assetHisDataGrid");
			if(!grid.hasClass("easyui-datagrid")){
				grid.attr("style", "width: 100%; height: auto;");
				// 查詢列
				var datagridColumns = [
					{field:'serialNumber', halign:'center',width:150,sortable:true,title:"設備序號"},
					{field:'propertyId',width:150,halign:'center',sortable:true,title:"財產編號"},
					{field:'assetTypeName',width:150,halign:'center',sortable:true,title:"設備名稱"},
					{field:'warehouseName',width:200,halign:'center',sortable:true,title:"入庫倉庫"},
					{field:'assetInId',width:160,halign:'center',sortable:true,title:"入庫批號"},
					{field:'factoryWarrantyDate',halign:'center',width:120,halign:'center',align:'center',formatter:formaterTimeStampToyyyyMMDD,sortable:true,title:"原廠保固日期"},
					{field:'customerWarrantyDate',halign:'center',width:120,halign:'center',align:'center',formatter:formaterTimeStampToyyyyMMDD,sortable:true,title:"客戶保固日期"},
					{field:'contractCode',width:180,halign:'center',sortable:true,title:"合約編號"},
					{field:'maTypeName',width:150,halign:'center',sortable:true,title:"維護模式"},
					{field:'keeperName',width:150,halign:'center',sortable:true,title:"保管人"},
					{field:'isChecked',width:80,halign:'center',align:'center',sortable:true,
							formatter:function formatterCheckbox(value,row,index){
								return settingCheckbox(value,row,index);
							},title:"實際驗收"},
					{field:'isCustomerChecked',halign:'center',width:100,align:'center',sortable:true,
							formatter:function formatterCheckbox(value,row,index){
								return settingCheckbox(value,row,index);
							},title:"客戶實際驗收"},
					{field:'updatedByName',halign:'center',width:150,sortable:true,title:"異動人員"},
					{field:'updatedDate',halign:'center',width:190,align:'center',formatter:formatToTimeStamp,sortable:true,title:"異動日期"}
				];
				// 創建datagrid
				grid.datagrid({
					columns:[datagridColumns],
					pagination:true,
					remoteSort:true,
					pageNumber:0,
					pageList:[15,30,50,100],
					pageSize:15,
					singleSelect:true,
					nowrap : false,
					toolbar:'#tb',
					rownumbers:true,
					sortOrder:'asc',
					iconCls: 'icon-edit'
				});
				
			}
		}
		$(function(){
			//根據客戶聯動合約
			$("#<%=AssetInInfoFormDTO.QUERY_COMPANY_ID %>").combobox({
				onChange:function(newValue, oldValue){
					$("#<%=AssetInInfoFormDTO.QUERY_CONTRACT_ID %>").combobox("setValue", "");
					ajaxService.getContractListByVendorId(newValue, function(data){
						if (data != null) {
							data = initSelect(data);
							$("#<%=AssetInInfoFormDTO.QUERY_CONTRACT_ID %>").combobox("loadData", data);
						} 
					});			
				}
			});
			// 查詢datagrid初始化
			setTimeout("initAssetHisDatagrid();",5);
		});
	</script>
</body>
</html>