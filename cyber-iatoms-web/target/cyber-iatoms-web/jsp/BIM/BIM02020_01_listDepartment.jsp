<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ include file="/jsp/common/easyui-common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.DepartmentFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.BimDepartmentDTO"%>
<%
	//獲取sessionContext
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	//獲取公司列表
	List<Parameter> companyList = (List<Parameter>) SessionHelper.getAttribute(request,IAtomsConstants.UC_NO_BIM_02020,IAtomsConstants.ACTION_GET_COMPANY_LIST);
	DepartmentFormDTO formDTO = null;
	if (ctx != null) {
		formDTO = (DepartmentFormDTO) ctx.getResponseResult();
	} else {
		formDTO = new DepartmentFormDTO();
	}
%>
<html>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="companyList" value="<%=companyList%>" scope="page"></c:set>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
<script type="text/javascript">
/**
 * 入口函數
 */
$(function(){
//	textBoxMaxlengthSetting();
	//點擊查詢
	$("#btnQuery").click(function(){
		queryData(1,true);
	});
	//點擊新增
	$("#btnAdd").click(function(){
		viewEditData('新增部門維護','<%=IAtomsConstants.ACTION_INIT_EDIT%>', '');
	});
	
});

/**
 * 修改
 */
function update() {
	var row = $("#dataGrid").datagrid('getSelected');
	var deptCode = getSelectedDeptCode();
	if (deptCode) {
		viewEditData('修改部門維護','<%=IAtomsConstants.ACTION_INIT_EDIT%>', deptCode);
	}
}
/**
 * 獲得操作行的deptCode
 */
 function getSelectedDeptCode() {
	var deptCode = '';
	var row = $("#dataGrid").datagrid('getSelected');
	if (row == null){		
		$("#gridMsg").empty();
		$.messager.alert('提示','請選擇要操作的記錄!','info'); 
		return false;
	} else { 
		deptCode = row.deptCode;   
	} 
	return deptCode;
} 
/*
* 删除时拿到主键值
*/
function getDeletedParam(actionId) {
	var row = $("#dataGrid").datagrid('getSelected');
	var param ;
	if (row != null) {
		param = {
			actionId : actionId,	
			deptCode : row.deptCode
		};
		return param;
	} else {
		$("#msg").text('請勾選要操作的記錄!');
		return null;
	}
}
/**
 * 查詢數據
 */
function queryData(pageNum,hidden) {
	$("#gridMsg").empty();
	//清空選中
	$("#dataGrid").datagrid("clearSelections");
	var queryParam = $("#queryForm").form("getData");
	var options = {
			url : "${contextPath}/<%=IAtomsConstants.DEPARTMENT_MANANGEMENT%>?actionId=<%=IAtomsConstants.ACTION_QUERY%>",
			queryParams :queryParam,
			pageNumber:pageNum,
			onLoadSuccess:function(data){
				if (hidden) {
					$("#gridMsg").text("");
					if (data.total == 0) {
						// 提示查無數據
						$("#gridMsg").text(data.msg);
					}
				}
				hidden = true;
			},
			onLoadError : function() {
				$("#gridMsg").text("查詢失敗！請聯繫管理員");
			}
		}
	// 清空點選排序(注：若初始化直接使用datagrid的sortName进行排序的请再次赋初值)
	if(hidden){
		options.sortName = null;
	}
	openDlgGridQuery("dataGrid", options);
}

/**
 * 保存，修改
 */
function viewEditData(title, actionId, deptCode) {
	$("#gridMsg").empty();
	var viewDlg = $('#dialog').dialog({
		title : title,
		width : 720,
		height : 370,
		top:10,
		closed : false,
		cache : false,
		queryParams : {
			actionId : actionId,
			deptCode : deptCode
		},
		href : "${contextPath}/" + "<%=IAtomsConstants.DEPARTMENT_MANANGEMENT%>",
		modal : true,
		onLoad : function() {
		//	textBoxMaxlengthSetting();
					textBoxSetting("dialog");
  		  		},
		buttons : [{
				text : '儲存',
				width :'90px',
				iconCls : 'icon-ok',
				handler : function() {
					var controls = 
						['<%=BimDepartmentDTO.ATTRIBUTE.COMPANY_ID.getValue()%>',
							'<%=BimDepartmentDTO.ATTRIBUTE.DEPT_NAME.getValue()%>'];
					if (validateForm(controls) && $("#fm").form("validate")) {
					  	// 調用保存遮罩，傳入參數爲當前dialog的id
                        commonSaveLoading('dialog');
						//提交表單時將公司下拉框變為enable
						$("#<%=BimDepartmentDTO.ATTRIBUTE.COMPANY_ID.getValue()%>").combobox("enable");
						$.ajax({
						url : "${contextPath}/<%=IAtomsConstants.DEPARTMENT_MANANGEMENT%>?actionId=<%=IAtomsConstants.ACTION_SAVE%>",
						data : $("#fm").form("getData"),
						type : 'post', 
						cache : false, 
						dataType :'json',
						success : function(json) {
							// 去除保存遮罩
                         	commonCancelSaveLoading('dialog');
							$("#dataGrid").datagrid("clearSelections");
							if (json.success) {
								viewDlg.dialog('close');
								//得到當前頁，進行再次查詢 
								var pageIndex = getGridCurrentPagerIndex("dataGrid");
								queryData(pageIndex,false);
								//顯示消息
								$("#gridMsg").text(json.msg);
							} else {
								$("#msg").text(json.msg);
							}
						},
						error : function(){
							// 去除保存遮罩
                         	commonCancelSaveLoading('dialog');
							var msg;
							if (isEmpty(deptCode)) {
								msg = "新增失敗";
							} else {
								msg = "修改失敗";
							}
							$("#msg").text(msg);					
						}	
						});
					}
					if (!isEmpty(deptCode)) {
						$("#<%=BimDepartmentDTO.ATTRIBUTE.COMPANY_ID.getValue()%>").combobox("disable");
					}
				}
			}, 
			{
				text : '取消',
				width :'90px',
				iconCls : 'icon-cancel',
				handler : function() {
				confirmCancel(function() {
						viewDlg.dialog('close');
				});
			}
		}]
	});
}

/**
 * 删除部門資料
 */
function Delete() {
	$("#gridMsg").empty(); 
	var deptCode = getSelectedDeptCode();
	if (deptCode) {
		var params = getDeletedParam("<%=IAtomsConstants.ACTION_DELETE%>");
		var url = "${contextPath}/"+"<%=IAtomsConstants.DEPARTMENT_MANANGEMENT%>";
		var successFunction = function(data) {
			if (data.success) {
				var pageIndex = calDeletePagerIndex("dataGrid");
				queryData(pageIndex, false);
			} 
			$("#gridMsg").text(data.msg);
		};
		var errorFunction = function(){
			$("#gridMsg").text("刪除失敗");
		};
		commonDelete(params,url,successFunction,errorFunction);
	}
} 
/**
 *　點選datagrid中的一行時
 */
function onClickRow() {
	//修改和刪除按鈕變為enable狀態
	$("#btnEdit").linkbutton('enable')
	$("#btnDelete").linkbutton('enable')
}
</script>
</head>
<body>
<!-- 	<div data-options="region:'center'"
		style="width: auto; height: auto; padding: 1px; overflow-y: hidden"> -->
<div style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
		<table id="dataGrid" class="easyui-datagrid" title="部門維護"
			style="width: 98%; height: auto"
			data-options="
				fitColumns:false,
				border:true,
				pagination:true,
				pageNumber:0,
				pageList:[15,30,50,100],
				pageSize:15,
				idField:'deptCode',
				datas:[],
				remoteSort:true,
				singleSelect:true,
				nowrap : false,
				toolbar : '#toolbar',
				rownumbers:true,
				onClickRow : onClickRow,
				iconCls: 'icon-edit'
				">
			<thead>
				<tr>
					<th data-options="field:'companyName',width:260,sortable:true,halign:'center',align:'left'">公司</th>
					
					<th data-options="field:'deptName',width:400,sortable:true,halign:'center'">部門名稱</th>
					
					<th data-options="field:'contact',width:200,sortable:true,halign:'center'">聯絡人</th>
					
					<th data-options="field:'contactTel',width:180,sortable:true,halign:'center',align:'left'">聯絡人電話</th>
					
					<th data-options="field:'contactFax',width:180,sortable:true,halign:'center',align:'left'">聯絡人傳真</th>
					
					<th data-options="field:'contactEmail',width:300,sortable:true,halign:'center'">聯絡人Email</th>
					
					<th data-options="field:'address',width:300,sortable:true,halign:'center',formatter:function(value,row){if(row.location == null){return row.address;}else{ return row.location + row.address;}}">部門地址</th>
					
					<th data-options="field:'remark',width:500,sortable:false,halign:'center'">說明</th>
				</tr>
			</thead>
		</table>
			<div id="toolbar"  style="padding: 2px 5px;">
			<form id="queryForm" method="post" novalidate>
				公司:&nbsp;
				<cafe:droplisttag css="easyui-combobox"
					id="<%=DepartmentFormDTO.QUERY_COMPANY %>"
					name="<%=DepartmentFormDTO.QUERY_COMPANY %>" 
					result="${companyList }" javascript="editable=false"
					blankName="請選擇" hasBlankValue="true"
					selectedValue="${formDTO.queryCompany }">
				</cafe:droplisttag>
				部門名稱:&nbsp;
				<input id="<%=DepartmentFormDTO.QUERY_DEPT_NAME%>"
					name="<%=DepartmentFormDTO.QUERY_DEPT_NAME%>" 
					class="easyui-textbox"
					style="width: 110px" 
					maxlength="50"
					validType="maxLength[50]"/>&nbsp;
				<a id="btnQuery"
					href="javascript:void(0)" class="easyui-linkbutton"
					iconcls="icon-search">查詢</a> &nbsp;
				<a id="btnAdd"
					href="javascript:void(0)" class="easyui-linkbutton"
					iconcls="icon-add">新增</a>&nbsp;
				<a id="btnEdit" 
					href="javascript:void(0)" class="easyui-linkbutton" 
					iconcls="icon-edit" onclick="update();">修改 </a>&nbsp;
				<a id="btnDelete" 
					href="javascript:void(0)" class="easyui-linkbutton" 
					iconcls="icon-remove" onclick="Delete();">刪除</a>
			</form>	
				<div><span id="gridMsg" class="red"></div>
			</div>
		<div id="dialog"></div>
	</div>
</body>
</html>