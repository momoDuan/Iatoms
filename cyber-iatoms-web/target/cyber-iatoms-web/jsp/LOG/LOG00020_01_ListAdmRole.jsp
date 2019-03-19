<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%@include file="/jsp/common/easyui-common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.SystemLogFormDTO"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	SystemLogFormDTO formDTO = null;
	String ucNo = null;
	if (ctx != null) {
		formDTO = (SystemLogFormDTO) ctx.getResponseResult();
		if (formDTO != null) { 
			// 获得UseCaseNo
			ucNo = formDTO.getUseCaseNo();
		} else {
			ucNo = IAtomsConstants.UC_NO_AMD_01040;
			formDTO = new SystemLogFormDTO();
		}
	} else {
		ucNo = IAtomsConstants.UC_NO_AMD_01040;
		formDTO = new SystemLogFormDTO();
	}
%>
<html>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>AdmUser Edit</title>
</head>
<body>	
<script type="text/javascript">
var jsonData;
var pageSize;
var pageNumber;
 $(function(){
	 var params;
	 // 處理頁面顯示查詢值
	 <c:if test="${not empty formDTO.logContent}">
		params = ${formDTO.logContent};
		 var queryResultJson = {
					rows:params.rows,
					total:params.total
				};
		 if(params.pageSize){
			 pageSize = params.pageSize;
		 }
		 if(params.pageNumber){
			 pageNumber = params.pageNumber;
		 }
		jsonData = queryResultJson; 
		
		if(jsonData && jsonData.total){
			if(jsonData.total == 0){
				$("#queryLogMsg").text("查無資料");
			} 
		}
		if(params.queryRoleName){
			$("#dlgQueryRoleName").val(params.queryRoleName);
		}
		if(params.queryRoleCodeName){
			$("#dlgQueryRoleCodeName").append("<option value='Value'>"+ params.queryRoleCodeName +"</option>"); 
		}
	 </c:if>
	 // 處理各種情況該頁面顯示樣式
	 if('${formDTO.logCategre}' == 'query'){
		$("#digDataGrid").datagrid({
			onLoadSuccess:function(){
				forbiddenBlock();
			}
		});
	} else if('${formDTO.logCategre}' == 'delete'){
		 $("#digDataGrid").datagrid({
			onLoadSuccess:function(){
				$("#digDataGrid").datagrid('selectRow',params.rowIndex);
				forbiddenBlock();
			}
		});
	} else if('${formDTO.logCategre}' == 'update'){
		 $("#digDataGrid").datagrid({
				onLoadSuccess:function(){
					$("#digDataGrid").datagrid('selectRow', params.rowIndex).datagrid(
							'beginEdit', params.rowIndex);
					//獲取選中rolecode節點
					var code = $('#digDataGrid').datagrid('getEditor', { 'index': params.rowIndex, field: 'roleCode' });
					//在編輯時，將角色代碼欄位變為不可編輯狀態
					$(code.target).textbox('disable');
					forbiddenBlock();
				}
		});
		//bug2359 update by 2017/09/06
	} else if('${formDTO.logCategre}' == 'UPDATE.save' 
			|| '${formDTO.logCategre}' == 'CREATE.save'
			|| '${formDTO.logCategre}' == 'save'){
		$("#digDataGrid").datagrid({
			onLoadSuccess:function(){
				if(params.editJson){
					var editParam = JSON.parse(params.editJson);
					jsonData.rows[params.rowIndex] = editParam;
				}
				$("#digDataGrid").datagrid('selectRow', params.rowIndex).datagrid(
						'beginEdit', params.rowIndex);
				if(params.editJson && JSON.parse(params.editJson).roleId){
					//獲取選中rolecode節點
					var code = $('#digDataGrid').datagrid('getEditor', { 'index': params.rowIndex, field: 'roleCode' });
					//在編輯時，將角色代碼欄位變為不可編輯狀態
					$(code.target).textbox('disable');
				}
				// 禁用部分區塊
				forbiddenBlock();
			}
		});
	} 
});
/**
 * 禁用部分區塊
 */
 function forbiddenBlock(){
		// table長度
		var tableWidth = $("#digDataGrid").datagrid("getPanel").find('div.datagrid-view2 table').css("width");
		// 遮罩樣式
		var blockOptions = {message:null,overlayCSS:{backgroundColor:'#fff',cursor:'default',opacity:'0.1',width:tableWidth}};
		// 查詢條件禁用
		$("#dlgToolBar").block(blockOptions);
		var header = $("#digDataGrid").datagrid("getPanel").find('div.datagrid-header');
		// table標題禁用
		header.block(blockOptions);
		// table的body禁用
		var body = $("#digDataGrid").datagrid("getPanel").find('div.datagrid-body');
		body.block(blockOptions);
		// 分頁部分禁用
		var pager = $("#digDataGrid").datagrid("getPager");
		pager.block(blockOptions);
	}
</script>
 		<div region="center" style="width: auto; height: auto; padding: 1px;  overflow-y: hidden">
		<table id="digDataGrid" class="easyui-datagrid" title="系統角色管理" style="width:98%; height: auto;"
				data-options="
					border:true,
					pagination:true,
					idField:'roleId',
					remoteSort:true,
					pageNumber:pageNumber,
					singleSelect:true,
					nowrap : false,
					data:jsonData,
					method: 'post',
					toolbar:'#dlgToolBar',
					rownumbers:true,
					iconCls: 'icon-edit'
				">
			<thead>
				<tr>
					<th data-options="field:'roleCode',halign:'center',align:'left',width:250,editor:'textbox'">角色代碼</th>
                    <th data-options="field:'roleName',halign:'center',align:'left',width:300,editor:'textbox'">角色名稱</th>
                    <th data-options="field:'roleDesc',halign:'center',align:'left',width:250,editor:'textbox'">角色說明</th>
                    <th data-options="field:'attributeName',halign:'center',width:150,
						editor:{
						type:'combobox',
						options:{
						valueField:'id',
						textField:'text',
						}
						}">角色屬性</th>
                    <th data-options="field:'workFlowRoleName',halign:'center',width:150,
						editor:{
						type:'combobox',
						options:{
						valueField:'id',
						textField:'text',
						}
						}">表單角色</th>
                    <th data-options="field:'updatedByName',halign:'center',width:150,align:'left'">異動人員</th>
					<th data-options="field:'updatedDate',halign:'center',align:'center',width:150,formatter:formatToTimeStamp">異動日期</th>
				</tr>
			</thead>
		</table>
		<div id="dlgToolBar" style="padding: 2px 5px;">
				<label>角色代碼:</label>&nbsp;
						<select class="easyui-combobox" panelheight="auto" style="width: 200px" id="dlgQueryRoleCodeName" readonly>
						</select>
				<label style="margin-left: 1px">角色名稱:</label>&nbsp;
				<input class="easyui-textbox" type="text" value="" id="dlgQueryRoleName" readonly>&nbsp;
				<%-- <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" <c:if test="${formDTO.logCategre eq 'update'}">disabled</c:if> >查詢</a>&nbsp; --%>
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" <c:if test="${formDTO.logCategre eq 'update' or formDTO.logCategre eq 'save' or formDTO.logCategre eq 'UPDATE.save' or formDTO.logCategre eq 'CREATE.save'}">disabled</c:if> >查詢</a>&nbsp;
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add'" <c:if test="${formDTO.logCategre eq 'update' or formDTO.logCategre eq 'save' or formDTO.logCategre eq 'UPDATE.save' or formDTO.logCategre eq 'CREATE.save'}">disabled</c:if> >新增</a>&nbsp;
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'" <c:if test="${formDTO.logCategre eq 'query' or formDTO.logCategre eq 'delete'}">disabled</c:if> >儲存</a>&nbsp;
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" <c:if test="${formDTO.logCategre eq 'query' or formDTO.logCategre eq 'delete'}">disabled</c:if> >取消</a>&nbsp;
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" <c:if test="${formDTO.logCategre eq 'query' or formDTO.logCategre eq 'update' or formDTO.logCategre eq 'save'}">disabled</c:if> >刪除</a>&nbsp;
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-more'" <c:if test="${formDTO.logCategre eq 'query' or formDTO.logCategre eq 'update' or formDTO.logCategre eq 'save'}">disabled</c:if> >明細</a>
		        <div><span id="queryLogMsg" class="red"></span></div>
	   </div>
</body>
</html>