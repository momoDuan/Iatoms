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
<link href="${contextPath}/css/iatoms_style.css" type="text/css" rel="stylesheet"/>
<title>AdmUser List</title>
</head>
<body>	
 <div region="center" style="width: auto; height: auto; padding: 1px;  overflow-y: hidden">

        <table id="digDataGrid" class="easyui-datagrid" title="使用者帳號管理" style="width: 98%; height: auto"
            data-options="
                rownumbers:true,
                pagination:true,
				iconCls: 'icon-edit',
				singleSelect: true,
				pageNumber:pageNumber,
				data:jsonData,
				method: 'post',
				nowrap:false,
				toolbar:'#dlgToolBar'">
            <thead>
                <tr>
                   <th data-options="field:'account',width:210,sortable:true,halign:'center',align:'left'">帳號</th>
					<th data-options="field:'cname',sortable:true,width:150,halign:'center',align:'left'">中文姓名</th>
					<th data-options="field:'company',sortable:true,width:260,halign:'center',align:'left'">公司</th>
					<th data-options="field:'email',sortable:true,width:311,halign:'center',align:'left'">EMail</th>
					<th data-options="field:'functionGroup',sortable:true,halign:'center',align:'left'">角色權限</th>
					<th data-options="field:'deptName',sortable:true,width:400,halign:'center',align:'left'">部門</th>
					<th data-options="field:'dataAcl',width:100,sortable:true,halign:'center',align:'left'" formatter="fomatterYesOrNo">控管資料權限</th>
					<th data-options="field:'accountStatus',width:80,sortable:true,halign:'center',align:'left'">狀態</th>
					<th data-options="field:'createdByName',sortable:true,width:150,halign:'center',align:'left'">建檔人員</th>
					<th data-options="field:'updatedByName',sortable:true,width:150,halign:'center',align:'left'" >異動人員</th>
					<th data-options="field:'updatedDate',width:180,sortable:true,halign:'center',align:'center',formatter:formatToTimeStamp">異動日期</th>
                </tr>
            </thead>
        </table>
        <div id="dlgToolBar" style="padding: 2px 5px;">
            帳號:
            <input class="easyui-textbox" style="width: 110px" value="" id="dlgQueryAccount" readonly>
            中文姓名:
            <input class="easyui-textbox" style="width: 110px" value="" id="dlgQueryCname" readonly>
            公司: 
        <select class="easyui-combobox" panelheight="auto" style="width: 100px" id="dlgQueryCompanyName" readonly>
            <!-- <option value=""></option> -->
        </select>
            角色權限: 
        <select class="easyui-combobox" panelheight="auto" style="width: 100px" id="dlgQueryRoleName" readonly>
            <!-- <option value=""></option> -->
        </select>
            狀態: 
        <select class="easyui-combobox" panelheight="auto" style="width: 100px" id="dlgQueryStatusName" readonly>
            <!-- <option value=""></option> -->
        </select>
            <a href="#" class="easyui-linkbutton" iconcls="icon-search">查詢</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add'">新增</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-edit" <c:if test="${formDTO.logCategre eq 'query'}">disabled</c:if> id="logBtnEdit" >修改</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" id="logBtnDelete" <c:if test="${formDTO.logCategre eq 'query'}">disabled</c:if>>刪除</a>
            <div style="height: 10px"></div>
            <div><span id="queryLogMsg" class="red"></span></div>
            <div style="text-align: right;">
                <a href="#" style="width: 150px" id="digBtnExport">匯出</a>
            </div>
        </div>
</div>
<script type="text/javascript">
var jsonData;
var pageSize;
var pageNumber;
 $(function(){
	 var params;
	 // 處理查詢結果的顯示
	 <c:if test="${not empty formDTO.logContent}">
		params = ${formDTO.logContent};
		//params = JSON.parse('${formDTO.logContent}');
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
		if(jsonData){
			if(jsonData.total == 0){
				$('#digBtnExport').attr("style","color:gray;");
				$("#queryLogMsg").text("查無資料");
			} else {
				$('#digBtnExport').attr("style","color:blue;");
			}
		}
		$("#dlgQueryAccount").val(params.queryAccount);
		$("#dlgQueryCname").val(params.queryCname);
		$("#dlgQueryCompanyName").append("<option value='Value'>"+ params.queryCompanyName +"</option>"); 
		$("#dlgQueryRoleName").append("<option value='Value'>"+ params.queryRoleName +"</option>"); 
		$("#dlgQueryStatusName").append("<option value='Value'>"+ params.queryStatusName +"</option>"); 
	 </c:if>
	 
	 if('${formDTO.logCategre}' == 'query'){
		$("#digDataGrid").datagrid({
			onLoadSuccess:function(){
				// 禁用部分區塊
				forbiddenBlock();
			}
		});
	} else {
		 $("#digDataGrid").datagrid({
			onLoadSuccess:function(){
				$("#digDataGrid").datagrid('selectRow',params.rowIndex);
				// 禁用部分區塊
				forbiddenBlock();
			}
		});
	}
}); 
 /*
 * 禁用區塊
 */
function forbiddenBlock(){
	 // table長度
	var tableWidth = $("#digDataGrid").datagrid("getPanel").find('div.datagrid-view2 table').css("width");
	 // 遮罩樣式
	var blockOptions = {message:null,overlayCSS:{backgroundColor:'#fff',cursor:'default',opacity:'0.1',width:tableWidth}};
	 // 禁用查詢條件區塊
	$("#dlgToolBar").block(blockOptions);
	 // 禁用table標題
	var header = $("#digDataGrid").datagrid("getPanel").find('div.datagrid-header');
	header.block(blockOptions);
	// 禁用table的body
	var body = $("#digDataGrid").datagrid("getPanel").find('div.datagrid-body');
	body.block(blockOptions);
	// 禁用table頁碼
	var pager = $("#digDataGrid").datagrid("getPager");
	pager.block(blockOptions);
}
</script>
</body>
</html>