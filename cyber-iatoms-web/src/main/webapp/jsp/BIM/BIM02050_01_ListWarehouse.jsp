<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.WarehouseDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.WarehouseFormDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	List<Parameter> vendorList = 
		(List<Parameter>) SessionHelper.getAttribute(request, IAtomsConstants.UC_NO_BIM_02050, IAtomsConstants.PARAM_COMPANY_LIST);
	WarehouseFormDTO formDTO = null;
	if (ctx != null) {
		formDTO = (WarehouseFormDTO) ctx.getResponseResult();
	} else {
		formDTO = new WarehouseFormDTO();
	}
%>
<html>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="vendorList" value="<%=vendorList%>" scope="page"></c:set>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="/jsp/common/easyui-common.jsp"%>
<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
<title></title>
<script type="text/javascript">
	//若存在選中的行就啟用修改、刪除按鈕
	function onClickRow(){
		$("#btnEdit").linkbutton('enable');
		$("#btnDelete").linkbutton('enable');
	}
	var dataGrid = null;
	$(function(){
		$("#btnQuery").click(function(){
			// 點擊查詢按鈕，查詢信息,並傳入頁碼為1
			queryData(1, true);
		});
		$("#btnAdd").click(function(){
			// 點擊新增按鈕，彈出對話框
			editData('新增倉庫據點維護', '<%=IAtomsConstants.ACTION_INIT_ADD%>', '');
		});
		//將修改和刪除按鈕變為disable狀態
		$("#btnEdit").linkbutton('disable')
		$("#btnDelete").linkbutton('disable')
	});

	//修改
	function update(){
		// 獲得選中的倉庫編號
		var warehouseId = getSelectedWarehouseId();
			// 彈出修改的對話框，並帶入查詢到的倉庫信息
			editData('修改倉庫據點維護', '<%=IAtomsConstants.ACTION_INIT_EDIT%>', warehouseId);
	}
	// 查詢數據
	function queryData(pageIndex, hidden) {
		// 清空選中的行
		$("#dataGrid").datagrid("clearSelections");
		var queryParam = $("#searchForm").form("getData");
		var options = {
				url : "${contextPath}/warehouse.do?actionId=<%=IAtomsConstants.ACTION_QUERY%>",
				queryParams :queryParam,
				pageNumber:pageIndex,
				onLoadSuccess:function(data){
					if (hidden) {
						$("#msg").text("");
						if (data.total == 0) {
							// 提示查無數據
							$("#msg").text(data.msg);
						}
					}
					hidden = true;
				},
				onLoadError : function() {
					$("#msg").text("查詢失敗！請聯繫管理員");
				}
			}
		// 清空點選排序
		if(hidden){
			options.sortName = null;
		}
		openDlgGridQuery("dataGrid", options);
	}
	
	// 修改和刪除時如果未選中，進行提示，選中后獲得倉庫編號
	function getSelectedWarehouseId() {
		$("#msg").empty();
		var warehouseId = '';
		// 獲得選中的行
		var row = $("#dataGrid").datagrid('getSelected');
		// 如果行為空，彈出“請勾選要操作的項”的訊息
		if (row == null) {
			$.messager.alert('提示', '請勾選要操作的記錄!', 'warning');
			return false;
		} else {
			// 如果有，獲得倉庫編號
			warehouseId = row.warehouseId;
			return warehouseId;
		}
	}
	
	// 新增/修改（頁面標題、actionId和倉庫編號）
	function editData(title, actionId, warehouseId) {
		// 新增修改初始化頁面時清空顯示提示訊息的span
		$("#msg").empty();
		// 彈出新增修改的對話框
		var viewDlg = $('#dlg').dialog({
			title : title,
			width : 760,
			height : 420,
			top:10,
			closed : false,
			cache : false,
			queryParams : {
				// 傳入actionId
				actionId : actionId,
				// 倉庫編號
				warehouseId : warehouseId
			},
			// 到warehouse.do
			href : "${contextPath}/warehouse.do",
			modal : true,
			onLoad : function() {
                 textBoxSetting("dlg");
            },
			buttons : [{
				width :'90px',
				// 按鈕上顯示儲存
				text : '儲存',
				iconCls : 'icon-ok',
				handler : function(){
					var controls = 
						['<%=WarehouseDTO.ATTRIBUTE.COMPANY_ID.getValue()%>',
							'<%=WarehouseDTO.ATTRIBUTE.NAME.getValue()%>',
							'<%=WarehouseDTO.ATTRIBUTE.CONTACT.getValue()%>',
							'<%=WarehouseDTO.ATTRIBUTE.TEL.getValue()%>',
							'<%=WarehouseDTO.ATTRIBUTE.LOCATION.getValue()%>',
							'<%=WarehouseDTO.ATTRIBUTE.ADDRESS.getValue()%>'];
					if (validateForm(controls) && $("#fm").form("validate")) {
						// 調用保存遮罩，傳入參數爲當前dialog的id
                        commonSaveLoading('dlg');
						//判斷公司地址-縣市是否輸入
						var location = $("#location").combo('getValue');
						if(location != ''){
							var url = "${contextPath}/warehouse.do?actionId=<%=IAtomsConstants.ACTION_SAVE%>";
							var datas = $("#fm").form('getData'); 
							datas.companyId = $("#<%=WarehouseDTO.ATTRIBUTE.COMPANY_ID.getValue()%>").combobox('getValue');
							// 將保存參數傳到warehouse.do進行保存
							$.ajax({
								url : url,
								data : datas,
								type : 'post', 
								cache : false, 
								dataType : 'json', 
								success : function(json){
									// 去除保存遮罩
									commonCancelSaveLoading('dlg');
									if (json.success) {
										// 新增修改成功后關閉對話框
										viewDlg.dialog('close');
										// 將當前頁設置為查詢的頁碼
										var pageIndex = getGridCurrentPagerIndex("dataGrid");
										// 新增修改后的查詢，停留在當前頁
										queryData(pageIndex, false);
										// 提示新增/修改成功
										$("#msg").text(json.msg);
									} else{
										// 新增/修改失敗的訊息
										$("#dialogMsg").text(json.msg);							
									}
								},
								error:function(){
									// 去除保存遮罩
									commonCancelSaveLoading('dlg');
									var msg;
									if (warehouseId == null || warehouseId == "") {
										msg = "新增失敗";
									} else {
										msg = "修改失敗";
									}
									$.messager.alert('提示', msg, 'error');							
								}
							});
						} 
					}
				}
			},
			// 取消按鈕及確認是否取消
			{
				width : '90px',
				text : '取消',
				iconCls : 'icon-cancel',
				handler : function(){
					confirmCancel(function(){
						viewDlg.dialog('close');
					});
				}
			}]
		});    
	}
	
	// 刪除數據
	function deleteData(){
		// 點擊刪除按鈕清空顯示提示訊息的span
		$("#msg").empty();
		//var params = getSelectedWarehouseId();
		var warehouseId = getSelectedWarehouseId();
		var params = {warehouseId : warehouseId};
		var url = '${contextPath}/warehouse.do?actionId=<%=IAtomsConstants.ACTION_DELETE%>';
		var successFunction = function(data) {
			$("#msg").text(data.msg);
			if (data.success) {
				//計算要顯示的頁碼
				var pageIndex = calDeletePagerIndex("dataGrid");
				queryData(pageIndex,false);
			} 
		};
		var errorFunction = function(){
			$.messager.alert('提示', "刪除失敗", 'error');	
		};
		commonDelete(params,url,successFunction,errorFunction);
	}
</script>
</head>
<body>
<!-- 	<div data-options="region:'center'"
		style="width: auto; height: auto; padding:1px; overflow-y: hidden"> -->
<div style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
		<table id="dataGrid" class="easyui-datagrid" title="倉庫據點維護"
			style="width:98%; height: auto;"
			data-options="
				pagination:true,
				iconCls: 'icon-edit',
				idField:'warehouseId',
				pageNumber:0,
				pageList:[15,30,50,100],
				pageSize:15,
				singleSelect:true,
				toolbar : '#toolbar',
				rownumbers:true,
				nowrap:false,
				onClickRow: onClickRow
				">
			<thead>
				<tr>
					<th data-options="field:'companyName',width:260,halign:'center',align:'left', sortable:true">維護廠商</th>
					<th data-options="field:'name',width:400,halign:'center',align:'left',sortable:true">倉庫名稱</th>
					<th data-options="field:'contact',width:200,halign:'center',align:'left',sortable:true">聯絡人</th>
					<th data-options="field:'tel',width:180,halign:'center',align:'left',sortable:true">電話</th>
					<th data-options="field:'fax',width:180,halign:'center',align:'left',sortable:true">傳真</th>
					<th data-options="field:'address',width:300,halign:'center',align:'left',sortable:true">倉庫地址</th>
					<th data-options="field:'comment',width:500,halign:'center',align:'left',sortable:true">說明</th>
					<th data-options="field:'companyId',sortable:true,halign:'center',align:'left'" hidden="true">廠商編號</th>
				</tr>
			</thead>
		</table>
			<div id="toolbar" style="padding: 2px 5px;">
				<form id="searchForm" style="margin: 4px 0px 0px 0px">
					維護廠商:
					<cafe:droplisttag css="easyui-combobox" id="<%=WarehouseFormDTO.QUERY_COMPANY_ID%>"
						name="<%=WarehouseFormDTO.QUERY_COMPANY_ID%>" result="${vendorList}"
						blankName="請選擇" hasBlankValue="true" style="width: 150px"
						javascript="editable=false">
					</cafe:droplisttag>
					倉庫名稱:
					<input id="<%=WarehouseFormDTO.QUERY_NAME%>" name="<%=WarehouseFormDTO.QUERY_NAME%>"
						class="easyui-textbox" type="text" style="width: 150px" maxlength ="50" data-options="validType:['maxLength[50]']"></input>
					<a href="javascript:void(0)" id="btnQuery" class="easyui-linkbutton"
						iconcls="icon-search">查詢</a>
					<a href="javascript:void(0)" id="btnAdd" class="easyui-linkbutton"
						iconCls="icon-add">新增</a>
					<a href="javascript:void(0)" id="btnEdit" class="easyui-linkbutton"
						iconCls="icon-edit" onclick="update();">修改</a>
					<a href="javascript:void(0)" id="btnDelete" class="easyui-linkbutton"
						iconCls="icon-remove" onclick="deleteData();">刪除</a>
					<div><span id="msg" class="red"></span></div>
				</form>
			</div>
		<div id="dlg"></div>
	</div>
</body>
</html>