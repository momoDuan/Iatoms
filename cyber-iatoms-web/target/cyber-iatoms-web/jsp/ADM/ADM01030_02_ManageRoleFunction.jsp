<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.AdmFunctionTypeDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.AdmRoleFormDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	AdmRoleFormDTO formDTO = null;
	if (ctx != null) {
		formDTO = (AdmRoleFormDTO) ctx.getResponseResult();
	} else {
		formDTO = new AdmRoleFormDTO();
	}
	//獲取useCaseNo
	String ucNo = formDTO.getUseCaseNo();
	//功能權限列表
	List<Parameter> accessRights = (List<Parameter>) SessionHelper.getAttribute(request,
			ucNo, IATOMS_PARAM_TYPE.FUNCTION_TYPE.getCode());
	String editPermission = (String) SessionHelper.getAttribute(request,
			ucNo, IAtomsConstants.PARAM_FUNCTION_PERMISSION_MAP);
	String userNameFunction = logonUser.getName();
%>
<html>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="accessRights" value="<%=accessRights%>" scope="page"></c:set>
<c:set var="editPermission" value="<%=editPermission%>" scope="page"></c:set>
<c:set var="userNameFunction" value="<%=userNameFunction%>" scope="page"></c:set>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
	<div id="divRoleFunction" data-options="region:'center'" style="width: auto; height:90%; padding: 10px 20px;" class="topSoller">
	<div class="dialogtitle"><%=formDTO.getRoleCode()%>-<%=formDTO.getRoleName()%></div>
	<div><span id="dialogMsg" class="red"></span></div>
	<form id="roleFunctionFrom" method="post" class="roleFunction" style="height: 90%;">
		<input type="hidden" id="roleId" value="<%=formDTO.getRoleId()%>"/>
		<table id="dlgDataGrid" class="easyui-datagrid" style="width: 100%; height: 100%; " align="center"
			data-options="
				onDblClickRow:dlgRowClick,
				onClickRow:dlgOnClickRow,
				onEndEdit: onEndEditDlg,
				pagination:false,
				singleSelect:true,
				remoteSort:true,
				nowrap:false,
				toolbar : '#dlgToolbar',
				rownumbers:true,
				checkOnSelect: false,
				selectOnCheck: false,
				iconCls: 'icon-edit'">
			<thead frozen="true">
				<tr>
					<th data-options="field:'parentFunctionId',
							halign:'center',align:'left',width:170,
							formatter:function(value,row){
								return row.parentFunctionName;
							},
							editor:{
								type:'combobox',
								options:{
									editable:false,
									valueField:'value',
									textField:'name',
									method:'get',
									data:parentFunctionList,
									onChange:selectChange,
									required:true,
									missingMessage:'請選擇模組',
									panelHeight:'auto',
									validType:'ignore[\'請選擇\']',invalidMessage:'請輸入模組'
								}
							}">模組</th>
					<th id="functionId" data-options="field:'functionId',halign:'center',align:'left',width:170,
							formatter:function(value,row){
								return row.functionName;
							},
							editor:{
								type:'combobox',
								options:{
									editable:false,
									valueField:'value',
									textField:'name',
									method:'get',
									onChange:functionIdChange,
									required:true,
									missingMessage:'請選擇功能名稱',
									validType:'ignore[\'請選擇\']', invalidMessage:'請輸入功能名稱'
								}
						}">功能名稱</th>
				</tr>
			</thead>
			<thead>
				<tr>
					<c:forEach items="${accessRights}" var="accessRight">
						<c:choose>
							<c:when test="${fn:length(accessRight.name) < 3}">
								<c:set var="accessLen" value="90"/>								
							</c:when>
							<c:when test="${fn:length(accessRight.name) < 5}">
								<c:set var="accessLen" value="110"/>								
							</c:when>
							<c:when test="${fn:length(accessRight.name) < 8}">
								<c:set var="accessLen" value="150"/>								
							</c:when>
							<c:otherwise>
								<c:set var="accessLen" value="180"/>
							</c:otherwise>
						</c:choose>							
						<th data-options="field:'${accessRight.value}',width:<c:out value='${accessLen }'/>,align:'center',halign:'center',
								formatter:function formatterCheckbox(value,row,index){
									return settingCheckbox(value,row,index,'${accessRight.value}');
								}"> ${accessRight.name}&nbsp;<input type="checkbox" id="${accessRight.value}" class="chkDetailAll">
						</th>
					</c:forEach>
					<th data-options="field:'updatedByName',width:150,sortable:true,align:'left',halign:'center'">異動人員</th>
					<th data-options="field:'updatedDate',width:190,sortable:true,formatter:formatToTimeStamp,align:'center',halign:'center'">異動日期</th>
				</tr>
			</thead>
		</table>
		<div id="dlgToolbar" style="padding: 2px 5px;">
			<a href="javascript:void(0)" id="btnAddDetail" class="easyui-linkbutton" iconcls="icon-add" onclick="btnAddDetail()">新增</a>
			<a href="javascript:void(0)" id="btnDeleteDetail" class="easyui-linkbutton" iconcls="icon-remove" disabled="true" onclick="btnDeleteDetail()">刪除</a>
			<a href="javascript:void(0)" id="btnSaveDetail" class="easyui-linkbutton" iconcls="icon-save" onclick="btnSaveDetail()">儲存</a>
			<a href="javascript:void(0)" id="btnCancelDetail" class="easyui-linkbutton" iconcls="icon-cancel" onclick="btnCancelDetail()">取消</a>
			<input type="hidden" id="detialRoleCode" name="detialRoleCode" value="<%=formDTO.getRoleCode()%>" >
			<input type="hidden" id="detialRoleName" name="detialRoleName" value="<%=formDTO.getRoleName()%>" >
			<input type="hidden" id="detialRowIndex" name="detialRowIndex" value="" >
		</div>
	</form>
	</div>
	<script type="text/javascript">
	var viewDlg;
	var dlgEditIndex = -1;
	var isOnClick = false;
	var detialJson;
	
	/**
	* 在頁面加載成功之後執行
	*/
	function initValue() {
		var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		$("#dlgDataGrid").datagrid({
			url : '${contextPath}/admRole.do',
			loadMsg:'',
			// 拿到查詢參數
			queryParams : {
				'actionId':'<%=AdmRoleFormDTO.ACTION_LOAD_DATA%>',
				'roleId':'<%=formDTO.getRoleId()%>'
			},
			onBeforeLoad : function () {
				$('#dlgDataGrid').closest('div.panel').block(blockStyle);
			},
			// 查詢出現錯誤時的訊息
			onLoadError : function(data) {
				$('#dlgDataGrid').closest('div.panel').unblock();
			},
			onLoadSuccess : function(data){
				dlgGridLoadSuccess();
				$('#dlgDataGrid').closest('div.panel').unblock();
			}
		});
	}
	/**
	* 彈出框明細grid加載成功事件
	*/
	function dlgGridLoadSuccess() {
		var options = $("#dlgDataGrid").datagrid("options");
		detialJson = $("#dlgDataGrid").datagrid('getData');
		detialJson.detialRoleCode = $("#detialRoleCode").val();
		detialJson.detialRoleName = $("#detialRoleName").val();
		detialJson.columnsFileds = options.columns;
		var logContent = JSON.stringify(detialJson);
		ajaxService.saveSystemLog('<%=IAtomsConstants.ACTION_INIT_DETAIL%>', logContent, '<%=IAtomsConstants.UC_NO_ADM_01030 %>', function(result){
		});
		$('#dlgDataGrid').datagrid('acceptChanges');
		dlgEditIndex = -1;
		$(".chkDetailAll").unbind().click(function(){checkAll(this,$(this).attr("id"));});
		//獲取該name下選中的複選框個數
		var checkrows = "";
		//獲取該name下的所有複選框個數
		var row = $('#dlgDataGrid').datagrid('getRows');
		if (row != 0) {
			for (var j=0; j<accessRightList.length; j++) {
				checkrows = $("input[name='" + accessRightList[j].value + "']:checked").length;
				var totolrows = $("input[name='"+accessRightList[j].value+"']").length;
				if (checkrows == totolrows && totolrows != 0) {
					document.getElementById(accessRightList[j].value).checked = "checked";
				}
			}
		}
		var roleId = $("#roleId").val();
	}
	var updateColumn = ${editPermission};
	var editColumns;
	/**
	* 初始化複選框
	*/
	var tempIndex = -1;
	function settingCheckbox(value,row,index, name){
		if (index == 2) {
		}
		
		var functionId = row.functionId;
		var editColumns=updateColumn[functionId];
		if (editColumns != undefined) {
			if (editColumns.contains(name)) {
				if (value == true || value == "1" || value == "true") {
					return '<input type="checkbox" checked="checked" onclick = "checkRoleFunction(this);" name="'+name+'">';
				} else {
					return '<input type="checkbox" onclick = "checkRoleFunction(this);" name="'+name+'">';
				}
			}
		} else {
			return '<input type="checkbox" onclick = "checkRoleFunction(this);" name="'+name+'">';
		}
	}
	/**
	* 對話框中datagrid表格增加一行
	*/
	function btnAddDetail() {
		$("div.topSoller").unbind("scroll.validate");
		$("#dialogMsg").empty();
		var controls = ['parentFunctionId','functionId'];
		if (!validateFormInRow('dlgDataGrid', dlgEditIndex, controls)) {
			return false;
		}
		if (endEditingDlg()) {
			//新增一行
			$('#dlgDataGrid').datagrid('appendRow', {
				parentFunctionId:"",
				updatedByName:'${userNameFunction}',
				updatedDate:new Date()
			});
			var rowIndex = $('#dlgDataGrid').datagrid('getRows').length - 1;
			//直接可以編輯新增的行
			$('#dlgDataGrid').datagrid('selectRow', rowIndex).datagrid(
				'beginEdit', rowIndex);
			dlgEditIndex = rowIndex;
			$("#detialRowIndex").val(dlgEditIndex);
			var functionIdCode = $('#dlgDataGrid').datagrid('getEditor', { 'index': rowIndex, field: 'functionId'});
			$(functionIdCode.target).combobox('loadData', initSelect(null));
			//在新增一行時，取消全選
			$(".chkDetailAll").removeAttr("checked");
			$("#btnDeleteDetail").linkbutton('enable');
		}
	}
	/**
	* 角色明細-全選與取消全選
	*/
	function checkAll(obj,name){
		//全選框是否選中
		var isState = obj.checked;
		var rows = $('#dlgDataGrid').datagrid('getRows');
		$("input[name='" + name + "']").each(function(index,obj1){
			$(obj1).prop("checked",isState);
			var index = $(obj1).parents("tr[datagrid-row-index]").attr("datagrid-row-index");
			rows[index][name] = isState ? '1' : '0';
		});
	}
	/**
	* 雙擊datagrid表格觸發的事件
	*/
	function dlgRowClick(index, field) {
		$("div.topSoller").unbind("scroll.validate");
		isOnClick = true;
		$("#dialogMsg").empty();
		$("#dlgBtnRemove").linkbutton('enable');
		if (dlgEditIndex != index) {
			if (endEditingDlg()) {
				dlgEditIndex = index;
				$('#dlgDataGrid').datagrid('selectRow', index).datagrid(
						'beginEdit', index);
				var ed = $('#dlgDataGrid').datagrid('getEditor', {
					index : index,
					field : field
				});
				if (ed) {
					($(ed.target).data('textbox') ? $(ed.target).textbox(
							'textbox') : $(ed.target)).focus();
				}
				var parentFunctionCode = $('#dlgDataGrid').datagrid('getEditor', { 'index': index, field: 'parentFunctionId'});
				var functionCode = $('#dlgDataGrid').datagrid('getEditor', { 'index': index, field: 'functionId' });
				//在編輯時，將功能模組與功能清單欄位變為不可編輯狀態
				$(parentFunctionCode.target).combobox('disable');
				$(functionCode.target).combobox('disable');
				$("#detialRowIndex").val(dlgEditIndex);
				$("#btnDeleteDetail").linkbutton('enable');
			}
		}
		isOnClick = false;
	}
	/**
	* 點擊複選框事件，判斷是否需要勾選或者取消勾選該列的全選框
	*/
	function checkRoleFunction(obj){
		var checkName = obj.name;
		//獲取該name下選中的複選框個數
		var checkrows = $("input[name='"+checkName+"']:checked").length;
		//獲取該name下的所有複選框個數
		var totolrows = $("input[name='"+checkName+"']").length;
		//如果複選框全部選中，則勾選全選複選框，否則取消全選
		$("#"+checkName).prop("checked",checkrows == totolrows?true:false);
		var isState = obj.checked;
		var rows = $('#dlgDataGrid').datagrid('getRows');
		obj.checked = isState;
		var index = $(obj).parents("tr[datagrid-row-index]").attr("datagrid-row-index");
		rows[index][checkName] = isState ? '1' : '0';
	}
	/**
	* 功能名稱異動
	*/
	function functionIdChange(newValue, oldValue) {
		var column = $("#dlgDataGrid").datagrid("getColumnFields");
		var trs = $("#dlgDataGrid").prev().find('div.datagrid-body').find('tr');
		var rows = $('#dlgDataGrid').datagrid('getRows');
		if (newValue != "" && !isOnClick) {
			var updateColumn = ${editPermission};
			var editColumns=eval(updateColumn[newValue]);
			if (editColumns != undefined) {
				for (var i=0;i<column.length-2;i++) {
					$($(trs[dlgEditIndex].cells[i+1]).prev().find('div')).html("");
					rows[dlgEditIndex][column[i]] = '0';
					if (editColumns.contains(column[i])){
						$($(trs[dlgEditIndex].cells[i+1]).prev().find('div')).html("<input type=\"checkbox\" onclick = \"checkRoleFunction(this);\" name=\""+column[i]+"\" >");
					}
				} 
			}
		} else {
			if (newValue == "") {
				for (var i=0;i<column.length-2;i++) {
					$($(trs[dlgEditIndex].cells[i+1]).prev().find('div')).html("<input type=\"checkbox\" onclick = \"checkRoleFunction(this);\" name=\""+column[i]+"\" >");
				} 
			}
		}
		for (var i=0;i<column.length-2;i++) {
			var checkrows = $("input[name='"+column[i]+"']:checked").length;
			//獲取該name下的所有複選框個數
			var totolrows = $("input[name='"+column[i]+"']").length;
			//如果複選框全部選中，則勾選全選複選框，否則取消全選
			$("#"+column[i]).prop("checked",checkrows == totolrows && totolrows!=0?true:false);
		}
		
	}
	/**
	* 單擊表格事件
	*/
	function dlgOnClickRow(){
		$("div.topSoller").unbind("scroll.validate");
		$("#msg").empty();
		$("#btnDeleteDetail").linkbutton('enable');
		if(dlgEditIndex >= 0) {
			$('#dlgDataGrid').datagrid('selectRow', dlgEditIndex);
		}
	}	
	/**
	* 保存角色明細
	*/
	function btnSaveDetail() {
		
		$("div.topSoller").unbind("scroll.validate");
		//判斷表單是否驗證成功
		var controls = ['parentFunctionId','functionId'];
		var isChecked = false;
		//拿到集合的所有的行
		if (dlgEditIndex != -1) {
			if (!endEditingDlg()) {
				if(!validateFormInRow('dlgDataGrid', dlgEditIndex, controls)){
					return;
				}
				$("div.topSoller").unbind("scroll.validate");
				return;
			}
		}
		var array = [];
		var obj = new Object();
		$('#dlgDataGrid').datagrid('endEdit', dlgEditIndex);
		var rows = $('#dlgDataGrid').datagrid('getRows');
		var isHave = false;
		var updateColumn = ${editPermission};
		for (var i =0; i<rows.length; i++) {
			isChecked = false;
			var editColumns=eval(updateColumn[rows[i]['functionId']]);
			if (editColumns.contains('all')) {
				obj = new Object();
				obj.functionId = rows[i]['functionId'];
				obj.accessRight = "ALL";
				array.push(obj);
				continue;
			}
			for (var j=0; j<accessRightList.length; j++) {
				if (rows[i][accessRightList[j].value] == '1') {
					isChecked = true;
					obj = new Object();
					obj.functionId = rows[i]['functionId'];
					obj.accessRight = accessRightList[j].value;
					array.push(obj);
				}
			}
			if (!isChecked) {
				dlgRowClick(i,'functionId');
				$("#dialogMsg").text("請選擇資料權限，請重新輸入");
				return false;
			}
		}
		var hasSelectRow = false;
		var isEditRow = false;
		var detialRowIndex;
		var editRow = $('#dlgDataGrid').datagrid('getSelected');
		if(editRow != null){
			hasSelectRow = true;
			detialRowIndex = $("#detialRowIndex").val();
			if((editRow.insertFlag != undefined) && (editRow.updateFlag != undefined) && (editRow.deleteFlag != undefined)){
				isEditRow = true;
			}
		}
		var logParams = {
				'hasSelectRow':hasSelectRow,
				'isEditRow':isEditRow,
				'detialRowIndex':detialRowIndex
		};
		saveRolePermission(array,logParams);
	}
	/**
	* 保存角色信息
	*/
	function saveRolePermission(param,logParams) {
		$("#dialogMsg").text("");
		detialJson = $("#dlgDataGrid").datagrid('getData');
		detialJson.detialRoleCode = $("#detialRoleCode").val();
		detialJson.detialRoleName = $("#detialRoleName").val();
		detialJson.hasSelectRow = logParams.hasSelectRow;
		detialJson.isEditRow = logParams.isEditRow;
		detialJson.detialRowIndex = logParams.detialRowIndex;
		var logContent = JSON.stringify(detialJson);
		var rolePermission = {
			actionId  : '<%=IAtomsConstants.ACTION_SAVE_ROLE_PERMISSION%>',
			roleId    : $("#roleId").val(),
			logContent : logContent,
			<%=AdmRoleFormDTO.PARAM_PERMISSION_LIST%> : JSON.stringify(param)
		};
		var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		// 形成遮罩
		$('#divRoleFunction').closest('div.panel').block(blockStyle1);
		$.ajax({
			url : '${contextPath}/admRole.do',
			data : rolePermission,
			type : 'post', 
			cache : false, 
			dataType : 'json', 
			success : function(json) {
				if (json.success) {
					viewDlg.dialog('close');
					$("#msg").text(json.msg);
				}else{
					$("#dialogMsg").text(json.msg);
				}
				// 去除遮罩
				$('#divRoleFunction').closest('div.panel').unblock();
			},
			error:function(){
				// 去除遮罩
				$('#divRoleFunction').closest('div.panel').unblock();
				$("#detailMsg").text("保存角色權限失敗");
			}
		});
	}
	/**
	* 模組聯動功能名稱
	*/
	function selectChange(newValue, oldValue) {
		var row = $('#dlgDataGrid').datagrid('getSelected');
		var rowIndex = $('#dlgDataGrid').datagrid('getRowIndex',row);
		if (!isEmpty(newValue)){
			//服務端查找子功能信息
			ajaxService.getFunctionByParentId(newValue,function(data){
				if (data != null) {
					//查找所有的row
					var rows = $('#dlgDataGrid').datagrid('getRows');
					for (var i = 0; i < rows.length; i++) {
						if (i == rowIndex) {
							//當前行不比對
							continue;
						}
						if (rows[i].parentFunctionId == newValue) {
							//和選擇的行值一樣, 需過濾掉當前值                        		
							for (var j = 0; j < data.length; j++) {
								if (data[j].value == rows[i].functionId) {
									data.splice(j,1);
									break;
								}
							} 
						}
					}
					var currentEditor = $('#dlgDataGrid').datagrid('getEditor', {  
							index : rowIndex,  
							field : 'functionId'
					});
					var currentValue = currentEditor.target.combobox('getValue');
					currentEditor.target.combobox('loadData', initSelect(data));
					if (data.length > 0 && newValue == row.parentFunctionId) {
						currentEditor.target.combobox('setValue', currentValue);
					} else {
						currentEditor.target.combobox('setValue', '');
					}
				}
			});			
		} else {
			var currentEditor = $('#dlgDataGrid').datagrid('getEditor', {  
				index : rowIndex,  
				field : 'functionId'
			});
			currentEditor.target.combobox('setValue','');
			currentEditor.target.combobox('loadData',initSelect(null));
		}
	}
	/**
	* 當對話框中datagrid結束編輯時
	*/
	function onEndEditDlg(index, row) {
		$("div.topSoller").unbind("scroll.validate");
		var functionEd = $("#dlgDataGrid").datagrid('getEditor', {
			index : index,
			field : 'functionId'
		});
		row.functionName = $(functionEd.target).combobox('getText');
		var parentFunctionEd = $("#dlgDataGrid").datagrid('getEditor', {
			index : index,
			field : 'parentFunctionId'
		});
		row.parentFunctionName = $(parentFunctionEd.target).combobox('getText');
	}
	/**
	* 結束datagrid表格數據
	*/
	function endEditingDlg() {
		if (dlgEditIndex == -1) {
			return true;
		}
		if ($('#dlgDataGrid').datagrid('validateRow', dlgEditIndex)) {
			var isChecked = false;
			var isHaveCheckBox = false;
			var rows = $('#dlgDataGrid').datagrid('getRows');
			var updateColumn = ${editPermission};
			var functionEd = $("#dlgDataGrid").datagrid('getEditor', {
				index : dlgEditIndex,
				field : 'functionId'
			});
			var editColumns=eval(updateColumn[$(functionEd.target).combobox('getValue')]);
			for (var j=0; j<accessRightList.length; j++) {
				if (editColumns.contains('all')) {
					isHaveCheckBox = true;
					break;
				}
				if (rows[dlgEditIndex][accessRightList[j].value] == 1) {
					isChecked = true;
				}
			}
			//如果沒有功能權限處於選中狀態，則不允許結束編輯
			if (!isChecked && !isHaveCheckBox) {
				$('#dlgDataGrid').datagrid('selectRow', dlgEditIndex);
				$("#dialogMsg").text("請選擇資料權限，請重新輸入");
				return false;
			} else {
				$('#dlgDataGrid').datagrid('endEdit', dlgEditIndex);
				dlgEditIndex = -1;
				return true;
			}
		} else {
			$('#dlgDataGrid').datagrid('selectRow', dlgEditIndex);
			var code = $('#dlgDataGrid').datagrid('getEditor', {index:dlgEditIndex, field:'parentFunctionId'});
			var value = $(code.target).combobox('getValue');
			if (value == "") {
				return false;
			}
			code = $('#dlgDataGrid').datagrid('getEditor', {index:dlgEditIndex, field:'functionId'});
			value = $(code.target).combobox('getValue');
			if (value == "") {
				return false;
			}
			return false;
		}
	}	
		
		// 刪除一行
		function btnDeleteDetail(){
			$("div.topSoller").unbind("scroll.validate");
			$("#dialogMsg").empty();
			var row = $('#dlgDataGrid').datagrid('getSelected');
			if (row) {
				var rowIndex = $('#dlgDataGrid').datagrid('getRowIndex',row);
				$('#dlgDataGrid').datagrid('deleteRow',	rowIndex);
				dlgEditIndex = -1;
			} else {
				$.messager.alert('提示','請勾選要操作的記錄!','warning');
			}
			var rows = $('#dlgDataGrid').datagrid('getRows');
			if (rows.length == 0) {
				$('#dlgDataGrid').datagrid('loadData', { total: 0, rows: [] });
			}
			$("#btnDeleteDetail").linkbutton('disable');
			//獲取該name下選中的複選框個數
			var checkrows = "";
			//獲取該name下的所有複選框個數
			var row = $('#dlgDataGrid').datagrid('getRows');
			if (row != 0) {
				for (var j=0; j<accessRightList.length; j++) {
					checkrows = $("input[name='" + accessRightList[j].value + "']:checked").length;
					var totolrows = $("input[name='"+accessRightList[j].value+"']").length;
					if (checkrows == totolrows && totolrows != 0) {
						document.getElementById(accessRightList[j].value).checked = "checked";
					}
				}
			}
		}
		//點擊取消按鈕
		function btnCancelDetail(){
			$("div.topSoller").unbind("scroll.validate");
			confirmCancel(function(){
				viewDlg.dialog('close');
			});
		}
		/* //彈出畫面grid設置rowindex, 用於刪除某行後, 重新設置rowindex
		function dlgGridRowSetRowIndex() {
			var rows = $('#dlgDataGrid').datagrid('getRows');
			for(var i = 0; i < rows.length; i ++) {
				//查找模組select編輯框
				var editParentSelect = $('#dlgDataGrid').datagrid('getEditor',{
						index:i,
						field:'parentFunctionId'
					});
				if (editParentSelect) {
					//模組select動態添加onchange事件, 增加行號傳入
					var target = editParentSelect.target;
					target.attr("data-id", i);
				}
			}	
		} */
		
	</script>
	<!-- <style type="text/css">
		#roleFunction {
			margin: 0;
			padding: 3px 3px;
		}
	</style> -->
</body>
</html>