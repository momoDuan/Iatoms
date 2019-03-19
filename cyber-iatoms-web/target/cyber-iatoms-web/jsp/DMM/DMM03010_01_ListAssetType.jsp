<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetTypeFormDTO"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	AssetTypeFormDTO formDTO = null;
	String ucNo = "";
	if (ctx != null) {
		formDTO = (AssetTypeFormDTO) ctx.getResponseResult();
		if(formDTO != null){
			ucNo = formDTO.getUseCaseNo();
		}
	}
	List<Parameter> assetCategoryList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, "ASSET_CATEGORY");
%>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="ucNo" value="<%=ucNo%>" scope="page"></c:set>
<c:set var="assetCategoryList" value="<%=assetCategoryList%>" scope="page"></c:set>
<link href="${contextPath}/css/iatoms_style.css" type="text/css" rel="stylesheet"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>設備品項維護</title>
<link rel="stylesheet" type="text/css" href="${contextPath}/assets/css/multi-select.css">
<%@include file="/jsp/common/easyui-common.jsp"%>
<script type="text/javascript" src="${contextPath}/assets/js/jquery.multi-select.js"></script>
<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
<script type="text/javascript">
	$(function(){
		$("#btnEdit").linkbutton('disable');
		$("#btnDelete").linkbutton('disable');
		// 查詢datagrid初始化
		setTimeout("initQueryDatagrid();",5);
	})
	//判斷改行數據是否刪除以及刪除和修改按鈕的設置.
	function onClickCell(rowIndex, rowData){
		var deleted = rowData.deleted;
		//如果還沒有刪除 deleted == N
		if(deleted == '<%=IAtomsConstants.NO%>'){
			//可編輯
			$('#btnEdit').linkbutton('enable');
			//可刪除
			$('#btnDelete').linkbutton('enable');
		} else {
			//不可編輯
			$('#btnEdit').linkbutton('disable');
			//不可刪除
			$('#btnDelete').linkbutton('disable');
		}
	}
	
	/*
	*初始化查詢datagrid
	*/
	function initQueryDatagrid(){
		var grid = $("#dataGrid");
		if(!grid.hasClass("easyui-datagrid")){
			grid.attr("style", "width: 98%; height: auto;");
			// 查詢列
			var datagridColumns = [
				{field:'assetTypeId',width:100,align:'left',halign:'center',hidden:'true',sortable:true,title:"設備代碼"},
				{field:'name',width:100,align:'left',halign:'center',sortable:true,title:"設備名稱"},
				{field:'companyName',width:150,align:'left',halign:'center',sortable:true,title:"設備廠商"},
				{field:'brand',width:100,align:'left',halign:'center',sortable:true,title:"設備廠牌"},
				{field:'model',width:100,align:'left',halign:'center',sortable:true,title:"設備型號"},
				{field:'commModeName',width:200,align:'left',halign:'center',sortable:true,title:"通訊模式"},
				{field:'functionName',width:200,align:'left',halign:'center',sortable:true,title:"支援功能"},
				{field:'assetCategoryName',align:'left',halign:'center',width:100,sortable:true,title:"設備類別"},
				{field:'unit',width:100,align:'left',halign:'center',sortable:true,title:"單位"},
				{field:'safetyStock',align:'right',halign:'center',width:100,sortable:true,formatter:formatCapacity,title:"安全庫存"},
				{field:'remark',align:'left',halign:'center',width:200,sortable:true,title:"說明"},
				{field:'deleted',width:65,sortable:true,align:'center',halign:'center',formatter:function(value,row,index){return settingCheckbox(value);},title:"已刪除"},
				{field:'deletedDate',align:'center',halign:'center',width:100,sortable:true,formatter:formatLongToDate,title:"刪除日期"}
			];
			// 創建datagrid
			grid.datagrid({
				title:"設備品項維護",
				columns:[datagridColumns],
	       		rownumbers:true,
                pagination:true,
                pageNumber:0,
                fitColumns:false,
                pageList:[15,30,50,100],
				pageSize:15,
				iconCls: 'icon-edit',
				singleSelect: true,
				nowrap:false,
				toolbar:'#toolbar'
			});
			
		}
	}
	
	//查詢 currentPage：頁碼 hidden ：標誌
	function queryData(currentPage,hidden){
		//獲取表單參數
		var queryParam = $("#searchForm").form("getData");
		var options = {
				url : "${contextPath}/assetType.do?actionId=<%=IAtomsConstants.ACTION_QUERY%>",
				queryParams :queryParam,
				pageNumber:currentPage,
				onLoadSuccess:function(data){
					if (hidden) {
						$("#msg_span").text("");
						if (data.total == 0) {
							// 提示查無數據
							$("#msg_span").text(data.msg);
						}
					}
					hidden = true;
				},
				onLoadError : function() {
					$("#msg_span").text("查詢失敗！請聯繫管理員");
				}
			}
			options.onSelect = onClickCell;
		// 清空點選排序(注：若初始化直接使用datagrid的sortName进行排序的请再次赋初值)
		if(hidden){
			options.sortName = null;
		}
		openDlgGridQuery("dataGrid", options);
	}
	
	//初始化编辑 operation：區分新增和修改
	function initEdit(operation){
		$("#msg_span").empty();
		var editAssetTypeId = "";
		var title = "";
		//修改
		if(operation == '<%=IAtomsConstants.ACTION_UPDATE%>'){
			var row = $('#dataGrid').datagrid('getSelected');
			if(row == null){
				return false;
			}else{
				if(row.deleted == '<%=IAtomsConstants.YES%>'){
					return false;
				}
				title = "修改設備品項維護";
				//修改行的id
				editAssetTypeId = row.assetTypeId;
			}
		}else {
			title = "新增設備品項維護";
			editAssetTypeId = "";
		}
		// 彈出新增修改的對話框
		$('#editDialog').dialog({
			title : title,
			width :800,
			height : 550,
			top:10,
			closed : false,
			cache : false,
			method:'post',
			//參數
			queryParams : {
				actionId : "<%=IAtomsConstants.ACTION_INIT_EDIT%>",
				editAssetTypeId : editAssetTypeId
			},
			href : "${contextPath}/assetType.do",
			modal : true,
			onLoad : function() {
			//	textBoxMaxlengthSetting();
       		},
			buttons : [{
				text : "儲存",
				width : 88,
				iconCls : 'icon-ok',
				handler : save
			},{
				text : "取消",
				width : 88,
				iconCls : 'icon-cancel',
				handler : function (){
					$.messager.confirm('確認對話框', '確認取消？', function(confirm){
						if (confirm){
							$("#editDialog").dialog('close');
						}
					});
				}
			}]
		}); 
	}
	//獲取存儲數據
	function getSaveParams (){
		var saveForm = $("#saveForm");
		//支援功能
		var functionIds = document.getElementsByName("functionId");
		var functionId = new Array();
		//循環支援功能，轉化為array
		if(functionIds != null){
			for(var i=0;i<functionIds.length;i++){
				functionId[i] = functionIds[i].value;
			}
		}
		var params = {
			actionId : '<%=IAtomsConstants.ACTION_SAVE%>',
			//修改的行的id
			editAssetTypeId : saveForm.find("#editAssetTypeId").val(),
			//設備id
			assetTypeId : saveForm.find("#assetTypeId").val(),
			//設備名稱
			name : saveForm.find("#name").val(),
			//公司id
			companyId : saveForm.find("#companyMultipleSelect").val(),
			//通訊模式
			commModeId : saveForm.find("#commModeMultipleSelect").val(),
			//設備廠牌
			brand : saveForm.find("#brand").val(),
			//設備型號
			model : saveForm.find("#model").val(),
			//支援功能
			functionId : functionId,
			//設備類別
			assetCategory : saveForm.find("input[name=assetCategory]").val(),
			//單位
			unit : saveForm.find("#unit").val(),
			//安全庫存
			safetyStockStr : saveForm.find("#safetyStock").val(),
			//說明
			remark : saveForm.find("#remark").val()
		}
		return params;
	}
	//儲存
	function save(){
		var saveForm = $("#saveForm");
		$("#dialogMsg").empty();
		//驗證表單 name設備名稱 assetCategory：設備類別
		var controls = ['name','assetCategory'];
		if(validateForm(controls) && saveForm.form("validate")){
			//通訊模式
			var commModeId = saveForm.find("#commModeMultipleSelect").val();
			//設備類別
			var assetCategory = saveForm.find("input[name=assetCategory]").val();
			var saveParams = getSaveParams();
			//如果設備類別為edc
			if(assetCategory == '<%=IAtomsConstants.ASSET_CATEGORY_EDC%>' ){
				//通訊模式為空
				if(commModeId == null){
					$("#dialogMsg").text("設備類別為EDC，請輸入通訊模式");
					return false;
				}
			}
			//添加遮罩
			commonSaveLoading('editDialog');
			$.ajax({
				url:"${contextPath}/assetType.do",
				data:saveParams,
				type:'post', 
				cache:false, 
				dataType:'json', 
				success: function (result) {
					//去除遮罩
					commonCancelSaveLoading('editDialog');
					if (result.success) {
						$('#editDialog').dialog('close'); // close the dialog
						$("#msg_span").text(result.msg);
						var pageIndex = getGridCurrentPagerIndex("dataGrid");
						queryData(pageIndex,false);
						//編輯按鈕不可編輯 
						$("#btnEdit").linkbutton('disable');
						//刪除按鈕不可編輯 
						$("#btnDelete").linkbutton('disable');
					}else{
						$("#dialogMsg").text(result.msg);
						//編輯按鈕不可編輯 
						$("#btnEdit").linkbutton('disable');
						//刪除按鈕不可編輯 
						$("#btnDelete").linkbutton('disable');
					}
				},
				error:function(){
					//去除遮罩
					commonCancelSaveLoading('editDialog');
					$.messager.alert('提示', '儲存失敗!', 'error');
				}
			});
		}
	}
	 /**
	  * 設置datagrid內部複選框選中
	  */
	 function settingCheckbox(value){
	 	//Y 設置單選框被默認選中，且不可編輯，已刪除
		if (value == "Y") {
			return '<input type="checkbox" checked="checked" disabled>';
		} else {
			return '<input type="checkbox" disabled>';
		}
	}
	//獲得操作行的param
	function getSelectedId(actionId) {
		var editAssetTypeId = '';
		var param;
		//獲取 選中行
		var row = $("#dataGrid").datagrid('getSelected');
		if (row != null) {
			//獲取參數
			param = {
				actionId : actionId,
				editAssetTypeId : row.assetTypeId,
			}
		} else {
			return false;
		}
		return param;
	}
	//刪除
	function deleteData(){
		//要刪除的id
		var editAssetTypeId = "";
		//選中行
		var row = $('#dataGrid').datagrid('getSelected');
		//刪除的id
		editAssetTypeId = row.assetTypeId;
		$("#msg_span").text("");
		//查看該設備下是否有庫存設備
		ajaxService.getAssetList(editAssetTypeId, function(data){
			if(data.flag){
				//提示該設備下尚有庫存，不可刪除
				$("#msg_span").text(data.msg);
				handleScrollTop('assetTypeDiv'); 
			} else {
				var params = getSelectedId("<%=IAtomsConstants.ACTION_DELETE%>");
				var url = '${contextPath}/assetType.do';
				var successFunction = function(data) {
					if (data.success) {
						var pageIndex = calDeletePagerIndex("dataGrid");
						queryData(pageIndex, false);
					} 
					$("#msg_span").text(data.msg);
				};
				var errorFunction = function(){
					$("#msg_span").text("刪除失敗");
				};
				//調用共有刪除方法
				commonDelete(params,url,successFunction,errorFunction);
			}
		})
	}
</script>
</head>
<body>
	<div id="assetTypeDiv" style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
	<!-- <div region="center" style="width: auto; height: auto; padding: 1px; overflow-y: hidden"> -->
		<table id="dataGrid" >
		</table> 
		<div id="toolbar" style="padding: 2px 5px;">
			<form action="assetType.do" id="searchForm" name="searchForm" method="post">
				設備類別:
				<cafe:droplisttag 
					css="easyui-combobox"
					name="queryAssetCategoryCode"
					id="queryAssetCategoryCode"
					result="${assetCategoryList}"
					blankName="請選擇"
					defaultValue=""
					hasBlankValue="true"
					style="width:200px"
					javascript="panelHeight='auto' editable='false'"
					>
				</cafe:droplisttag>
				<a class="easyui-linkbutton" href="javascript:void(0)" iconcls="icon-search" id="btnQuery" onclick="queryData(1,true)">查詢</a>
				<a class="easyui-linkbutton" href="javascript:void(0)" iconcls="icon-add" id="btnAdd" onclick="initEdit('create')">新增</a>
				<a class="easyui-linkbutton" href="javascript:void(0)" iconcls="icon-edit" id="btnEdit"onclick="initEdit('update')">修改</a>
				<a class="easyui-linkbutton" href="javascript:void(0)" iconcls="icon-remove" id="btnDelete"onclick="deleteData()">刪除</a>
			</form>
			<div><span id="msg_span" class="red"></span></div>
		</div>
		<div id="editDialog"></div>
</div> 
</body>
</html>