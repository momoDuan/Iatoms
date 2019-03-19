<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%@include file="/jsp/common/easyui-common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.SystemLogFormDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.AdmRoleFormDTO"%>
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
	
	//功能權限列表
	List<Parameter> accessRights = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.FUNCTION_TYPE.getCode());
	String editPermission = (String) SessionHelper.getAttribute(request,
			ucNo, IAtomsConstants.PARAM_FUNCTION_PERMISSION_MAP);
	
	//父功能列表
	String accessRightListString = (String) SessionHelper.getAttribute(request,
			ucNo, AdmRoleFormDTO.PARAM_ACCESS_RIGHT_LIST);
%>
<html>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="accessRights" value="<%=accessRights%>" scope="page"></c:set>
<c:set var="editPermission" value="<%=editPermission%>" scope="page"></c:set>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>AdmUser Edit</title>
</head>
<body>	
<script type="text/javascript">
var jsonData;
var detialRoleCode;
var detialRoleName;
var params;
/**
* 
*/
var accessRightList = initSelect(<%=accessRightListString%>);
 $(function(){
	 // 顯示明細頁面值
	 <c:if test="${not empty formDTO.logContent}">
		params = ${formDTO.logContent};
		//params = JSON.parse('${formDTO.logContent}');
		 var queryResultJson = {
					rows:params.rows,
					total:params.total
				};
		jsonData = queryResultJson; 
		if(jsonData && jsonData.total){
			if(jsonData.total == 0){
				$("#dialogMsg").text("查無資料");
			} 
		}
		detialRoleCode = params.detialRoleCode;
		detialRoleName = params.detialRoleName;
		$("#roleTitle").html(detialRoleCode + "-" + detialRoleName);
	 </c:if>
	// 進入明細頁面與結束明細頁面區分處理
	 if('${formDTO.logCategre}' == 'initDetail'){
		$("#dlgDataGrid").datagrid({
			onLoadSuccess:function(){
				// 設置datagrid標題複選框選中
				setHeaderCheckbox();
				// 隱藏部分列
			//	hiddenFiled();
				// 禁用部分區塊
				forbiddenBlock();
			}
		});
	} else if('${formDTO.logCategre}' == 'saveDetail'){
		 $("#dlgDataGrid").datagrid({
			onLoadSuccess:function(){
				// 處理選中行
				if(params.hasSelectRow){
					if(params.detialRowIndex){
						$("#dlgDataGrid").datagrid('selectRow', params.detialRowIndex).datagrid(
								'beginEdit', params.detialRowIndex);
						if(params.isEditRow){
							var parentFunctionBox = $('#dlgDataGrid').datagrid('getEditor', { 'index': params.detialRowIndex, field: 'parentFunctionName' });
							$(parentFunctionBox.target).combobox('disable');
							var functionBox = $('#dlgDataGrid').datagrid('getEditor', { 'index': params.detialRowIndex, field: 'functionName' });
							$(functionBox.target).combobox('disable');
						}
					}
				} else {
					$("#dlgBtnRemove").attr("disabled",true);
				}
				// 設置datagrid標題複選框選中
				setHeaderCheckbox();
				// 隱藏部分列
			//	hiddenFiled();
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
	var tableWidth = $("#dlgDataGrid").datagrid("getPanel").find('div.datagrid-view2 table').css("width");
	// 遮罩樣式
	var blockOptions = {message:null,overlayCSS:{backgroundColor:'#fff',cursor:'default',opacity:'0.1',width:tableWidth}};
	// 調整件禁用
	$("#dlgToolbar").block(blockOptions);
	// 標題禁用
	var header = $("#dlgDataGrid").datagrid("getPanel").find('div.datagrid-header');
	header.block(blockOptions);
	// body禁用
	var body = $("#dlgDataGrid").datagrid("getPanel").find('div.datagrid-body');
	body.block(blockOptions);
	// 分頁禁用
	var pager = $("#dlgDataGrid").datagrid("getPager");
	pager.block(blockOptions);
}

 /**
  * 設置datagrid內部複選框選中
  */
	function settingCheckbox(value,row,index, name){
		var functionId = row.functionId;
		var updateColumn = ${editPermission};
		var editColumns=updateColumn[functionId];
		if (editColumns != undefined) {
			if (editColumns.contains(name)) {
				if (value == true || value == "1" || value == "true") {
					return '<input type="checkbox" checked="checked" name="'+name+'">';
				} else {
					return '<input type="checkbox" name="'+name+'">';
				}
			}
		} else {
			return '<input type="checkbox" name="'+name+'">';
		}
	}
 /**
  * 設置datagrid標題複選框選中
  */
  function setHeaderCheckbox(){
	  if(params.rows && params.rows.length){
			//獲取該name下選中的複選框個數
			var checkrows = "";
			//獲取該name下的所有複選框個數
			var row = params.rows;
			for (var j=0; j<accessRightList.length; j++) {
				checkrows = $("input[name='" + accessRightList[j].value + "']:checked").length;
				var totolrows = $("input[name='"+accessRightList[j].value+"']").length;
				if (checkrows == totolrows && totolrows != 0) {
					document.getElementById(accessRightList[j].value).checked = "checked";
				}
			}
		}
	}
</script>
<div data-options="region:'center'" style="width: auto; height:90%; padding: 10px 20px;" class="topSoller">
 	<div class="ftitle" style ="font-size: 18px"><span id="roleTitle"></span> </div>
	<div><span id="dialogMsg" class="red"></span></div>
	<form id="detialForm" method="post" class="roleFunction" style="height: 90%;">
		<!-- <table id="dlgDataGrid" class="easyui-datagrid" style="width: 740px; height: auto "  -->
		 <table id="dlgDataGrid" class="easyui-datagrid" style="width: 100%; height: 100%; " align="center"
			data-options="
				pagination:false,
				singleSelect:true,
				remoteSort:true,					
				nowrap : false,
				data:jsonData,
				toolbar : '#dlgToolbar',
				rownumbers:true,
				checkOnSelect: false, selectOnCheck: false,
				iconCls: 'icon-edit'">
			<thead frozen="true">
				<tr>
					<th data-options="field:'parentFunctionName',width:150,halign:'center',align:'left',editor:'textbox',
						editor:{
						type:'combobox',
						options:{
						valueField:'id',
						textField:'text',
						}
						}">模組</th>
					<th data-options="field:'functionName',width:150,halign:'center',align:'left',editor:'textbox',
						editor:{
						type:'combobox',
						options:{
						valueField:'id',
						textField:'text',
						}
						}">功能名稱</th>
				</tr>
			</thead>
			<thead>	
				<tr>
					<c:forEach items="${accessRights}" var="accessRight">
						<c:choose>
							<c:when test="${fn:length(accessRight.name) < 3}">
								<c:set var="accessLen" value="70"/>								
							</c:when>
							<c:when test="${fn:length(accessRight.name) < 5}">
								<c:set var="accessLen" value="90"/>								
							</c:when>
							<c:otherwise>
								<c:set var="accessLen" value="110"/>
							</c:otherwise>
						</c:choose>							
						<th data-options="field:'${accessRight.value}',width:<c:out value='${accessLen }'/>,align:'center',halign:'center',
								formatter:function formatterCheckbox(value,row,index){
									return settingCheckbox(value,row,index,'${accessRight.value}');
								}"> ${accessRight.name}&nbsp;<input type="checkbox" id="${accessRight.value}" class="chkDetailAll">
						</th>
					</c:forEach>
					<th data-options="field:'updatedByName',width:150,align:'left',halign:'center'">異動人員</th>
					<th data-options="field:'updatedDate',width:150,formatter:formatToTimeStamp,align:'center',halign:'center'">異動日期</th>
				</tr>
			</thead>
		</table>
		<div id="dlgToolbar" style="padding: 2px 5px;">
			<a href="javascript:void(0)" id="dlgBtnAdd" class="easyui-linkbutton" iconcls="icon-add">新增</a>
			<a href="javascript:void(0)" id="dlgBtnRemove" class="easyui-linkbutton" iconcls="icon-remove" <c:if test="${formDTO.logCategre eq 'initDetail'}">disabled</c:if> >刪除</a>
			<a href="javascript:void(0)" id="dlgBtnSave" class="easyui-linkbutton" iconcls="icon-save">儲存</a>
			<a href="javascript:void(0)" id="dlgBtnCancel" class="easyui-linkbutton" iconcls="icon-cancel">取消</a>
		</div>
	</form>
	</div>
</body>
</html>