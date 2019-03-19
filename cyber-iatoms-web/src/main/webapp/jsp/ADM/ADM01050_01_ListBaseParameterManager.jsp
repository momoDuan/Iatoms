<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.BaseParameterItemDefDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.BaseParameterManagerFormDTO"%>

<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	BaseParameterManagerFormDTO formDTO = null;
	String ucNo = "";
	if (ctx != null) {
		formDTO = (BaseParameterManagerFormDTO) ctx.getResponseResult();
		if(formDTO != null){
			ucNo = formDTO.getUseCaseNo();
		} else {
			ucNo = IAtomsConstants.UC_NO_AMD_01050;
			formDTO = new BaseParameterManagerFormDTO();
		}
	} else {
		ucNo = IAtomsConstants.UC_NO_AMD_01050;
		formDTO = new BaseParameterManagerFormDTO();
	}
	List<Parameter> parameterTypeList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, BaseParameterManagerFormDTO.PARAMETER_TYPE);
%>
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE" var="papamTypes" />
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<!-- DataLoader -->
<c:set var="parameterTypeList" value="<%=parameterTypeList%>" scope="page"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系統參數維護</title>
<%@include file="/jsp/common/easyui-common.jsp"%>
<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
<script type="text/javascript">
	// 查詢
	function query(currentPage,hidden) {
		var queryParam = {
				queryParamType : $("#<%=BaseParameterManagerFormDTO.QUERY_PAGE_PARAM_TYPE%>").combobox('getValue'),
				queryParamCode : $("#<%=BaseParameterManagerFormDTO.QUERY_PAGE_PARAM_CODE%>").textbox('getValue'),
				queryParamName : $("#<%=BaseParameterManagerFormDTO.QUERY_PAGE_PARAM_NAME%>").textbox('getValue')
		};
		var options = {
				url : "${contextPath}/baseParameterManager.do?actionId=<%=IAtomsConstants.ACTION_QUERY%>",
				queryParams :queryParam,
				pageNumber:currentPage,
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
				},
				onSelect: function(index,row){
					$("#msg").text("");
					var deleted = row.deleted;
					if(deleted == '<%=IAtomsConstants.YES%>'){
						$('#btnEdit').linkbutton('disable');
						$('#btnDelete').linkbutton('disable');
					} else {
						$('#btnEdit').linkbutton('enable');
						$('#btnDelete').linkbutton('enable');
					}
				}
			}
		// 清空點選排序(注：若初始化直接使用datagrid的sortName进行排序的请再次赋初值)
		if(hidden){
			options.sortName = null;
		}
		openDlgGridQuery("dataGrid", options);
	}
	//初始化编辑
	function initEdit(operation){
		$("#msg").text("");
		var editBptdCode = "";
		var editBpidId = "";
		var editEffectiveDate = "";
		var title = "";
		if(operation == '<%=IAtomsConstants.ACTION_UPDATE%>'){
			var row = $('#dataGrid').datagrid('getSelected');
			if(row == null){
				$.messager.alert({
					title : '警告',
					msg : '請選擇要處理的資料'
				});
				return false;
			}else{
				title = "修改系統參數";
				editBptdCode = row.bptdCode;
				editBpidId = row.bpidId;
				editEffectiveDate = row.effectiveDate;
			}
		}else {
			title = "新增系統參數";
			editBptdCode = "";
			editBpidId = "";
			editEffectiveDate = "";
		}
		// 彈出新增修改的對話框
		$('#editDialog').dialog({
			title : title,
			width : 700,
			height : 380,
			top:10,
			closed : false,
			cache : false,
			queryParams : {
				actionId 	   		: "<%=IAtomsConstants.ACTION_INIT_EDIT%>",
				editBptdCode 		: editBptdCode,
				editEffectiveDate 	: editEffectiveDate,
				editBpidId 			: editBpidId
			},
			href : "${contextPath}/baseParameterManager.do",
			modal : true,
			onLoad : function() {
				textBoxSetting("editDialog");
			},
			buttons : [{
				text : "儲存",
				width : 70,
				iconCls : 'icon-ok',
				handler : save
			},{
				text : "取消",
				width : 70,
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
	
	//獲取保存數據
	function getSaveParams(){
		var saveParams = {
			editBptdCode : $("#saveForm").find("#editBptdCode").val(),
			editBpidId : $("#saveForm").find("#editBpidId").val(),
			bptdCode : $("#saveForm").find("input[name=bptdCodeShow]").val(),
			itemValue : $("#saveForm").find("#itemValue").val(),
			itemName : $("#saveForm").find("#itemName").val(),
			textField1 : $("#saveForm").find("#textField1").val(),
			itemOrder : $("#saveForm").find("#itemOrder").val(),
			itemDesc : $("#saveForm").find("#itemDesc").val()
		}
		return saveParams;
	}
	//保存數據
	function save() {
		$("#dialogMsg").text("");
		var saveForm = $("#saveForm");
		var controls = ['bptdCodeShow','itemValue','itemName','textField1','itemOrder','itemDesc'];
		if(validateForm(controls) && saveForm.form("validate")){
			// 調保存遮罩
			commonSaveLoading('editDialog');
			var saveParams = getSaveParams();
			if (editBptdCode == '${papamTypes.SYB_REMAIN_TIME.code}' ||
					editBptdCode == '${papamTypes.OLD_REMAIN_TIME.code}' ||
					editBptdCode == '${papamTypes.FREE_REMAIN_TIME.code}') {
				<%-- if ($("#saveForm").find("#<%=BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD_DATE1.getValue()%>")) {
					saveParams.textField1 = $("#<%=BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD_DATE1.getValue()%>").datebox("getValue");
				} --%>
			}
			saveParams.textField1 = $("#<%=BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD1.getValue()%>").textbox("getValue");
			saveParams.actionId = "<%=IAtomsConstants.ACTION_SAVE%>";
			$.ajax({
				url: '${contextPath}/baseParameterManager.do',
				data:saveParams,
				type:'post', 
				cache:false, 
				dataType:'json', 
				success: function (result) {
					// 去除保存遮罩
					commonCancelSaveLoading('editDialog');
					if (result.success) {
						$('#editDialog').dialog('close'); // close the dialog
						var pageIndex = getGridCurrentPagerIndex("dataGrid");
						query(pageIndex,false); 
						$("#msg").text(result.msg);
					}else{
						$("#dialogMsg").text(result.msg);
					}
				},
				error:function(){
					// 去除保存遮罩
					commonCancelSaveLoading('editDialog');
					//$.messager.alert('提示', '儲存失敗!', 'error');	
					var msg;
					if (isEmpty(saveParams.editBpidId)) {
						msg = "新增失敗";
					} else {
						msg = "修改失敗";
					}
					$("#dialogMsg").text(msg);
				}
			});
		}
	}
	 /**
	  * 設置datagrid內部複選框選中
	  */
	 function settingCheckbox(value){
			if (value == "Y") {
				return '<input type="checkbox" checked="checked" disabled>';
			} else {
				return '<input type="checkbox" disabled>';
			}
		}
	// 刪除
	function deleteData(){
		$("#msg").text("");
		var row = $('#dataGrid').datagrid('getSelected');
		var params = {
				actionId : '<%=IAtomsConstants.ACTION_DELETE%>',
				editBptdCode : row.bptdCode,
				editBpidId : row.bpidId,
				editEffectiveDate : row.effectiveDate
			};
		var url = '${contextPath}/baseParameterManager.do';
		var successFunction = function(data) {
			if (data.success) {
				//計算要顯示的頁碼
				var pageIndex = calDeletePagerIndex("dataGrid");
				query(pageIndex, false);
			} 
			$("#msg").text(data.msg);
		};
		var errorFunction = function(){
			$("#msg").text("刪除失敗");
		};
		commonDelete(params,url,successFunction,errorFunction);
	}
</script>
</head>
<body>
<div style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
	<!-- <div style="width: auto; height: auto; padding: 1px; overflow: hidden" region="center" > -->
		<table id="dataGrid" class="easyui-datagrid" title="系統參數維護" style="width: 98%; height: auto" 
				data-options="
	           		rownumbers:true,
	                pagination:true,
	                pageList:[15,30,50,100],
					pageSize:15,
	                pageNumber:0,
	                fitColumns:false,
					iconCls: 'icon-edit',
					singleSelect: true,
					nowrap : false,
					toolbar:'#toolbar'"
			>
			<thead>
				<tr>
					<th data-options="field:'bpidId',hidden:true">類別代碼</th>
					<th data-options="field:'effectiveDate',hidden:true">生效日期</th>
					<th data-options="field:'ptName',align:'left',halign:'center',width:200,sortable:true">參數類別</th>
					<th data-options="field:'itemValue',width:260,align:'left',halign:'center',sortable:true">參數代碼</th>
					<th data-options="field:'itemName',align:'left',width:380,halign:'center',sortable:true">參數名稱</th>
					<th data-options="field:'itemOrder',width:80,align:'right',sortable:true,halign:'center',formatter:formatCapacity">順序</th>
					<th data-options="field:'textField1',align:'left',width:200,halign:'center',sortable:true">附加欄位</th>
					<th data-options="field:'itemDesc',align:'left',width:250,halign:'center',sortable:true">備註</th>
					<th data-options="field:'updatedByName',width:170,align:'left',halign:'center',sortable:true">異動人員</th>
					<th data-options="field:'updatedDate',width:180,sortable:true,align:'center',halign:'center',formatter:formatToTimeStamp">異動日期</th>
					<!-- <th data-options="field:'deleted',width:60,sortable:true,align:'center',halign:'center'">已刪除</th> -->
					<th data-options="field:'deleted',width:65,align:'center',halign:'center',formatter:function(value,row,index){return settingCheckbox(value);}">已刪除</th>
	        		<th data-options="field:'deletedDate',width:180,sortable:true,align:'center',halign:'center',formatter:formatToTimeStamp">刪除日期</th>
				</tr>
			</thead>
		</table>
		<div id="toolbar" style="padding: 2px 5px;">
			<form id="queryForm" style="width: auto;height: auto;" method="post" novalidate>
				參數類別:&nbsp; <cafe:droplisttag css="easyui-combobox"
							id="<%=BaseParameterManagerFormDTO.QUERY_PAGE_PARAM_TYPE%>"
							name="<%=BaseParameterManagerFormDTO.QUERY_PAGE_PARAM_TYPE%>" 
							result="${parameterTypeList}"
							blankName="請選擇" 
							hasBlankValue="true"
							selectedValue="${formDTO.queryParamType}"
							style="width: 210px"
							javascript="editable='false'"
							>
							</cafe:droplisttag>
				參數代碼:&nbsp; <input  class="easyui-textbox" 
							id="<%=BaseParameterManagerFormDTO.QUERY_PAGE_PARAM_CODE%>"
							name="<%=BaseParameterManagerFormDTO.QUERY_PAGE_PARAM_CODE%>" 
							style="width: 110px" maxlength="50"
							validType="maxLength[50]"/>
				參數名稱:&nbsp; <input class="easyui-textbox" 
							id="<%=BaseParameterManagerFormDTO.QUERY_PAGE_PARAM_NAME%>"
							name="<%=BaseParameterManagerFormDTO.QUERY_PAGE_PARAM_NAME%>" 
							class="easyui-textbox"
							style="width: 110px" maxlength="50"
							validType="maxLength[50]" />&nbsp; 
				<a href="javascript:void(0)" 
					id="btnQuery" 
					class="easyui-linkbutton"
					data-options="iconCls:'icon-search'"
					onclick="query(1,true)">查詢</a>&nbsp; 
				<a href="javascript:void(0)"
					id="btnAdd" 
					class="easyui-linkbutton"
					data-options="iconCls:'icon-add'"
					onclick="initEdit('create')">新增</a>&nbsp; 
				<a	href="javascript:void(0)" 
					id="btnEdit" 
					class="easyui-linkbutton"
					data-options="iconCls:'icon-edit'"
					onclick="initEdit('update')">修改</a>&nbsp; 
				<a	href="javascript:void(0)" 
					id="btnDelete" 
					class="easyui-linkbutton"
					data-options="iconCls:'icon-remove'"
					onclick="deleteData()">刪除</a>
				<div><span id="msg" class="red"></span></div>
			</form>
		</div>
		<div id="editDialog"></div>
	</div>
</body>
</html>