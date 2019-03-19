<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.AdmRoleFormDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO"%>
<%@page import="java.util.Date" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	AdmRoleFormDTO formDTO = null;
	String ucNo = null;
	if (ctx != null) {
		formDTO = (AdmRoleFormDTO) ctx.getResponseResult();
	} 
	if (formDTO == null ) {
		formDTO = new AdmRoleFormDTO();
		ucNo = IAtomsConstants.UC_NO_ADM_01030;
	} else {
		ucNo = formDTO.getUseCaseNo();
	}
	String userName = logonUser.getName();
	if(StringUtils.hasText(userName) && userName.indexOf("(") >0 && userName.indexOf(")") > 0){
		userName = userName.substring(userName.indexOf("(") + 1, userName.indexOf(")"));
	}
	//角色列表
	List<Parameter> roleCodeList = (List<Parameter>) SessionHelper.getAttribute(request,
			ucNo, AdmRoleFormDTO.PARAM_ADM_ROLE_CODE_LIST);
	//角色屬性
	String admRoleAttributeListString = (String) SessionHelper.getAttribute(request,
			ucNo, AdmRoleFormDTO.PARAM_ADM_ROLE_ATTRIBUTE_LIST);
	//父功能列表
	String parentFunctionListString = (String) SessionHelper.getAttribute(request,
			ucNo, AdmRoleFormDTO.PARAM_PARENT_FUNCTION_LIST);
	//父功能列表
	String accessRightListString = (String) SessionHelper.getAttribute(request,
			ucNo, AdmRoleFormDTO.PARAM_ACCESS_RIGHT_LIST);
%>
<html>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="userName" value="<%=userName%>" scope="page"></c:set>
<c:set var="roleCodeList" value="<%=roleCodeList%>" scope="page"></c:set>
<c:set var="ucNo" value="<%=ucNo%>" scope="page"></c:set>
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$FUNCTION_TYPE" var="functionType" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
	<%@include file="/jsp/common/easyui-common.jsp"%>
	<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
</head>
<body>
<div id="admRoleDiv" style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height topSoller">
	<!-- <div data-options="fit:true, region:'center'" style="width: 98%; height: auto;"> -->
		<!-- <form id="formRole" method="post" novalidate> -->
		<table id="dataGrid" class="easyui-datagrid" title="系統角色管理" style="width:98%; height: auto;"
				data-options="
					onDblClickRow:onDblClickRow,
					onClickRow:onClickRow,
					onBeforeEdit : commonOnBeforeEdit,
					onEndEdit : commonOnEndEdit,
					onCancelEdit : commonOnCancelEdit,
					onBeforeSelect:function(index, row) {
						if(editIndex >= 0) {
							return false;
						}
					},
					border:true,
					pagination:true,
					idField:'roleId',
					pageList:[15,30,50,100],
					pageSize:15,
					remoteSort:true,
					singleSelect:true,
					pageNumber:0,
					nowrap : false,
					toolbar : '#toolbar',
					rownumbers:true,
					iconCls: 'icon-edit'
				">
			<thead>
				<tr>
					<th data-options="field:'roleCode',halign:'center',align:'left',width:250,sortable:true,maxlenght:'20',
										editor:{type:'textbox',
										options:{
											required:true,
											missingMessage:'請輸入角色代碼'
										}
									}">
					角色代碼</th>
					<th data-options="field:'roleName',halign:'center',align:'left',width:250,sortable:true,
										editor:{type:'textbox',
											options:{
												required:true,
												missingMessage:'請輸入角色名稱'
											}
										}">
					角色名稱</th>
					<th data-options="field:'roleDesc',halign:'center',align:'left',width:250,sortable:true,
										editor:{
											type:'textbox',
										}">角色說明</th>
					<th data-options="field:'attribute',halign:'center',width:150,sortable:true,
										formatter:function(value,row){
											return row.attributeName;
										},
										editor:{
											type:'combobox',
											options:{
												editable:false,
												valueField:'value',
												textField:'name',
												onChange:getworkFlowRole,
												data:admRoleAttribute,
												panelHeight:'auto'
											}
										}">角色屬性</th>
					<th data-options="field:'workFlowRole',halign:'center',width:150,sortable:true,
										formatter:function(value,row){
											return row.workFlowRoleName;
										},
										editor:{
											type:'combobox',
											options:{
												editable:false,
												valueField:'value',
												textField:'name',
												panelHeight:'auto'
											}
										}">表單角色</th>
					<th data-options="field:'updatedByName',halign:'center',width:150,align:'left',sortable:true">異動人員</th>
					<th data-options="field:'updatedDate',halign:'center',width:190,align:'center',sortable:true,formatter:formatToTimeStamp">異動日期</th>
				</tr>
			</thead>
		</table>
		<!-- </form>	 -->	
		<div id="toolbar" style="padding: 2px 5px;">
			<form id="searchForm" style="margin: 4px 0px 0px 0px">
				<label>角色代碼:</label>&nbsp;
				<cafe:droplisttag css="easyui-combobox"
						id="<%=AdmRoleFormDTO.QUERY_ROLE_CODE %>"
						name="<%=AdmRoleFormDTO.QUERY_ROLE_CODE %>"
						result="${roleCodeList }"
						blankName="請選擇" 
						hasBlankValue="true"
						style="width: 200px"
						selectedValue="${formDTO.queryRoleCode }"
						javascript="editable=false data-options=\"valueField:'value',textField:'name'\""
						></cafe:droplisttag>
				<label style="margin-left: 1px">角色名稱:</label>&nbsp;
				<input id="<%=AdmRoleFormDTO.QUERY_ROLE_NAME%>" name="<%=AdmRoleFormDTO.QUERY_ROLE_NAME%>" maxlength="50"
					class="easyui-textbox" type="text" validType="maxLength[50]"></input>&nbsp;
				<c:if test="${fn:contains(logonUser.accRghts[ucNo], functionType.QUERY.code )}"><a href="javascript:void(0)" id="btnQuery" class="easyui-linkbutton" onclick="btnQuery()" iconcls="icon-search">查詢</a>&nbsp;</c:if>
				<c:if test="${fn:contains(logonUser.accRghts[ucNo], functionType.ADD.code )}"><a href="javascript:void(0)" id="btnAdd" class="easyui-linkbutton" onclick="btnAdd()" iconcls="icon-add">新增</a>&nbsp;</c:if>
		        <c:if test="${fn:contains(logonUser.accRghts[ucNo], functionType.SAVE.code )}"><a href="javascript:void(0)" id="btnSave" class="easyui-linkbutton" onClick="btnSave()" disabled="true" iconcls="icon-save">儲存</a>&nbsp;
		        <a href="javascript:void(0)" id="btnCancle" class="easyui-linkbutton" onclick="btnCancel()" disabled="true" iconcls="icon-cancel">取消</a>&nbsp;</c:if>
		        <c:if test="${fn:contains(logonUser.accRghts[ucNo], functionType.DELETE.code )}"><a href="javascript:void(0)" id="btnDelete" class="easyui-linkbutton" onClick="btnDelete()" disabled="true" iconcls="icon-remove">刪除</a>&nbsp;</c:if>
		        <c:if test="${fn:contains(logonUser.accRghts[ucNo], functionType.DETAIL.code )}"><a href="javascript:void(0)" id="btnDetail" class="easyui-linkbutton" onClick="btnDetail()" disabled="true" iconcls="icon-more">明細</a></c:if>
		        <div><span id="msg" class="red"></span></div>
		    </form>
	   </div>
      
       <div id="dlg"></div>
    </div>
    <script type="text/javascript">
	/**
	* 功能模組列表
	*/
	var parentFunctionList = initSelect(<%=parentFunctionListString %>);
	var dataGrid = null;
	/**
	* 角色屬性列表
	*/
	var admRoleAttribute = initSelect(<%=admRoleAttributeListString%>);
	/**
	* 
	*/
	var accessRightList = initSelect(<%=accessRightListString%>);
	var editIndex = -1;	
	// 查詢數據
	var dataJson;
	/**
	* 點擊查詢按鈕-查詢數據
	*/
	function btnQuery() {
		$("div.topSoller").unbind("scroll.validate");
		queryData(1, true);
	}
	 
	/**
	* 數據查詢
	*/
	function queryData(pageIndex, hidden) {
		// 清空顯示提示訊息的span
		$("#msg").empty();
		// 清空選中的行
		$("#dataGrid").datagrid("clearSelections");
		//獲取查詢條件數據
		var queryParam = $("#searchForm").form("getData");
		queryParam.actionId = "<%=IAtomsConstants.ACTION_QUERY%>";
		var ignoreFirstLoad = hidden;
		var options = {
			// 到admRole.do
			url : '${contextPath}/admRole.do',
			// 拿到查詢參數
			queryParams : queryParam,
			// 傳入頁碼
			pageNumber : pageIndex,
			// 查詢出現錯誤時的訊息
			onLoadError : function(data) {
				$.messager.alert('提示','查詢系統角色資料出錯','error');
			},
			onLoadSuccess : function(data){
				$("#dataGrid").datagrid("clearSelections");
				$("#btnAdd").linkbutton('enable');
				$("#btnRemove").linkbutton('disable');
				$("#btnDetail").linkbutton('disable');
				$("#btnSave").linkbutton('disable');
				$("#btnCancle").linkbutton('disable');
				$("#btnQuery").linkbutton('enable');
				// 保存查詢消息
				dataJson = $("#dataGrid").datagrid("getData");
				var options = $("#dataGrid").datagrid("options");
				dataJson.pageSize = options.pageSize;
				dataJson.pageNumber = options.pageNumber;
				dataJson.queryRoleCodeName = $("#queryRoleCode").combobox('getText');
				dataJson.queryRoleName = $("#queryRoleName").textbox('getValue'); 
				if (hidden) {
					// 保存查詢系統log
					var logContent = JSON.stringify(dataJson);
					ajaxService.saveSystemLog('<%=IAtomsConstants.ACTION_QUERY%>', logContent, '<%=IAtomsConstants.UC_NO_ADM_01030 %>', function(result){
					}); 
					$("#msg").empty();
					endEditing();
				}
				if (data.total == 0) {
					// 提示查無資料
					if (hidden) {
						$("#msg").text(data.msg);
					}
				}
				hidden = true;
			},
			onBeforeEdit : commonOnBeforeEdit,
			onEndEdit : commonOnEndEdit,
			onCancelEdit : commonOnCancelEdit
		};
		// 清空點選排序
		if(hidden){
			options.sortName = null;
		}
		options.ignoreFirstLoad = ignoreFirstLoad;
		rowEditGridQuery("dataGrid", options, 'msg');
		options.ignoreFirstLoad = true;
		$("#dataGrid").datagrid('acceptChanges');
	}
	/**
	* 點選明細、刪除按鈕時，獲取角色ID
	*/
	function getSelectedRoleId() {
		$("#msg").empty();
		// 獲得選中的行
		var row = $("#dataGrid").datagrid('getSelected');
		return row.roleId;
	}
	/**
	* 刪除角色信息，首先進行核檢角色是否已被使用。如果
	* 已被使用，則無法進行刪除。否則則進行數據刪除操作。
	*/
	function btnDelete() {
		$("div.topSoller").unbind("scroll.validate");
		var roleId = getSelectedRoleId();
		//核檢角色是否已被使用
		ajaxService.checkUseRole(roleId, function(result){
			if (result) {
				$("#msg").text("該角色已被使用者帳號使用，不可刪除");
			} else {
				dataJson.rowIndex = $('#dataGrid').datagrid('getRowIndex', $("#dataGrid").datagrid('getSelected'));
				var logContent = JSON.stringify(dataJson);
				deleteData(logContent,roleId);
			}
		});
	}
	
	/**
	* 刪除角色信息
	*/
	function deleteData(logContent,roleId){
		// 點擊刪除按鈕清空顯示提示訊息的span
		$("#msg").empty();
		// 彈出“數據正在處理”的訊息
		// 將actionId和角色編號傳入role.do進行刪除
		var url = "${contextPath}/admRole.do?actionId=<%=IAtomsConstants.ACTION_DELETE%>";
		var params = {logContent:logContent, roleId : roleId};
		var deleteSuccess = function(json) {
			// 刪除成功關閉數據正在處理的對話框
			if (json.success) {
				javascript:dwr.engine.setAsync(false);
				queryRoleCodeList();
				// 計算要顯示的頁碼
				var pageIndex = calDeletePagerIndex("dataGrid");
				// 刪除后的查詢
				queryData(pageIndex, false);
				javascript:dwr.engine.setAsync(true);								
			}
			// 提示刪除成功
			$("#msg").text(json.msg);
		};
		var deleteError = function(){
			$("#msg").text("刪除失敗");
		};
		commonDelete(params,url,deleteSuccess,deleteError);
	}
	/**
	* 保存角色信息
	*/
	function btnSave() {
		$("div.topSoller").unbind("scroll.validate");
		//驗證
		var controls = ['roleCode','roleName'];
		if(validateFormInRow('dataGrid', editIndex, controls)){
			var index = editIndex;
			var currentEditor = $('#dataGrid').datagrid('getEditor', {  
				index : index,  
				field : 'roleCode'
			});
			var roleCode = currentEditor.target.textbox('getValue');
			currentEditor = $('#dataGrid').datagrid('getEditor', {  
				index : index,  
				field : 'roleName'
			});
			var roleName = currentEditor.target.textbox('getValue');
			mailListRowJson = $("#dataGrid").datagrid('getSelected');
			//bug2359 update by 2017/09/06
			var attributeNameEditor = $('#dataGrid').datagrid('getEditor', {  
				index : index,  
				field : 'attribute'
			});
			var attributeName = attributeNameEditor.target.combobox('getText');
			var workFlowRoleNameEditor = $('#dataGrid').datagrid('getEditor', {  
				index : index,  
				field : 'workFlowRole'
			});
			var workFlowRoleName = workFlowRoleNameEditor.target.combobox('getText');		
			var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			$.blockUI(blockStyle1);
			ajaxService.checkRoleRepeat(roleCode, roleName, mailListRowJson.roleId, function(result){
				if (result == null || result == "") {
					//拿到集合的所有的行
					endEditing();
					//得到新增或修改的行的数据
					var roleListRow = $("#dataGrid").datagrid('getChanges');
					var roleListRowJson;
					if (roleListRow.length > 0) {
						roleListRowJson = roleListRow[0];
					} else if(dataJson && dataJson.rows && dataJson.rows[index]){
						roleListRowJson = dataJson.rows[index]
					}
					//bug2359 update by 2017/09/06
					roleListRowJson.attributeName = attributeName;
					roleListRowJson.workFlowRoleName = workFlowRoleName;
					if(roleListRowJson.attributeName == '' || roleListRowJson.attributeName == null){
						roleListRowJson.attributeName = '請選擇';
					}
					if(roleListRowJson.workFlowRoleName == '' || roleListRowJson.workFlowRoleName == null){
						roleListRowJson.workFlowRoleName = '請選擇';
					}					
					roleListRowJson.index = index;
					dataJson.editJson = JSON.stringify(roleListRowJson);
					var logContent = JSON.stringify(dataJson);
					roleListRowJson.logContent = logContent;
					saveData(roleListRowJson);
				} else {
					$("#msg").text(result);
					$.unblockUI();
				}
			});
		}else{
			$("div.topSoller").unbind("scroll.validate");
		}
	}
	/**
	* 進行刪除、添加操作之後更新角色代碼下拉框的值
	*/
	function queryRoleCodeList(){
		ajaxService.getRoleCode(function(data){
			if (data != null) {
				//標記符
				var isSign = false;
				//獲取當前下拉框選中的值
				var value = $("#<%=AdmRoleFormDTO.QUERY_ROLE_CODE %>").combobox('getValue');
				//如果是下拉的值與刪除的數據相同，則清除下拉框選中的值
				for (var i = 0; i<data.length; i++) {
					if (data[i].value == value) {
						isSign = true;
						break;
					}
				}
				var datas = initSelect(data);
				if (!isSign) {
					$("#<%=AdmRoleFormDTO.QUERY_ROLE_CODE %>").combobox('setValue', "");
				}
				$("#<%=AdmRoleFormDTO.QUERY_ROLE_CODE %>").combobox('loadData', datas);
			}
		});
	}
	/**
	* 結束編輯判斷,用於行編輯,新增,刪除前的判斷
	*/
	function endEditing() {
		$("div.topSoller").unbind("scroll.validate");
		if (editIndex < 0) {
			return true;
		}
		if ($('#dataGrid').datagrid('validateRow', editIndex)) {
			$('#dataGrid').datagrid('endEdit', editIndex);
			editIndex = -1;
			return true;
		} else {
			return false;
		}
	}
	/**
	* 角色屬性聯動表單角色
	*/
	var getworkFlowRole = function(newValue, oldValue) {
		//獲取當前選中的列
		var row = $("#dataGrid").datagrid("getSelected");
		//獲取當前選中的行號
		var rowIndex = $("#dataGrid").datagrid("getRowIndex",row);
		if (!isEmpty(newValue)) {
			ajaxService.getWorkFlowRoleList(newValue, function(result){
				if(result != null) {
					var currentEditor = $('#dataGrid').datagrid('getEditor', {  
							index : rowIndex,  
							field : 'workFlowRole'
					});
					if (oldValue != "") {
						currentEditor.target.combobox('setValue', "");
					}
					currentEditor.target.combobox('loadData', initSelect(result));
				}
			});
		} else {
			var currentEditor = $('#dataGrid').datagrid('getEditor', {  
							index : rowIndex,  
							field : 'workFlowRole'
					});
			
			currentEditor.target.combobox('loadData', initSelect(null));
			currentEditor.target.combobox('setValue', "");
		}	
	}

	/**
	* 保存數據
	*/
	function saveData(roleListRowJson) {
		var index = roleListRowJson.index;
		$.ajax({
			url: '${contextPath}/admRole.do?actionId=<%=IAtomsConstants.ACTION_SAVE%>',
			data:roleListRowJson,
			type:'post', 
			cache:false, 
			dataType:'json', 
			success:function(json){
				if (json.success) {
					endEditing();
					//服務端查找角色代碼, 重新加載角色代碼下拉列表
					queryRoleCodeList();
					var pageIndex = getGridCurrentPagerIndex("dataGrid");
					// 新增修改后的查詢，停留在當前頁
					queryData(pageIndex, false);
				} else {
					editRow(index);
					$("#btnSave").linkbutton('enable');
					$("#btnCancle").linkbutton('enable');
					$("#btnAdd").linkbutton('disable');
					$("#btnQuery").linkbutton('disable');
					handleScrollTop('admRoleDiv');
				}
				$.unblockUI();
				// 提示新增/修改成功
				$("#msg").text(json.msg);
			},
			error:function(){
				$("#msg").text("系統角色儲存失敗,請聯繫管理員");	
				$('#dataGrid').datagrid('selectRow', index).datagrid(
					'beginEdit', index);
				$("#btnSave").linkbutton('enable');
				$("#btnCancle").linkbutton('enable');
				$("#btnAdd").linkbutton('disable');	
				$("#btnQuery").linkbutton('disable');
				$.unblockUI();
			}
		});
	}
	/**
	* 發生單擊事件時，清空提示信息
	*/
	function onClickRow() {
		$("#msg").empty();
	}
	/**
	* 雙擊datagrid表格觸發的事件
	*/
	function onDblClickRow(index, field) {		
		if (editIndex >= 0) {
			return false;
		} else {
			$("#msg").empty();
			editRow(index);
		}
	}
	/**
	* 將資料變為可編輯狀態
	*/
	function editRow(index) {
		if (editIndex != index) {
			if (endEditing()) {
				//修改異動時間異動人員為當前登錄信息
				$('#dataGrid').datagrid('updateRow', {
								index:index, 
								row:{updatedByName:'${userName}',
									updatedDate:new Date()}});
				$('#dataGrid').datagrid('selectRow', index).datagrid(
						'beginEdit', index);
				var roleId = getSelectedRoleId();
				if (roleId != undefined) {
					//獲取選中rolecode節點
					var code = $('#dataGrid').datagrid('getEditor', { 'index': index, field: 'roleCode' });
					//在編輯時，將角色代碼欄位變為不可編輯狀態
					$(code.target).textbox('disable');
				}
				checkTextBox(index);
				editIndex = index;
				var currentEditor = $('#dataGrid').datagrid('getEditor', {  
					index : index,  
					field : 'attribute'
				});
				var floeRole = currentEditor.target.combobox('getValue');
				if (floeRole == "") {
					currentEditor = $('#dataGrid').datagrid('getEditor', {  
						index : index,  
						field : 'workFlowRole'
					});
					currentEditor.target.combobox('loadData', initSelect(null));
					currentEditor.target.combobox('setValue', "");
				}
				//bug2359 update by 2017/09/06
				if(dataJson && dataJson.rows && (editIndex || editIndex>=0) && dataJson.rows[editIndex]){
					if(dataJson.rows[editIndex].workFlowRoleName == '' || dataJson.rows[editIndex].workFlowRoleName == null){
						dataJson.rows[editIndex].workFlowRoleName = '請選擇';
					}
					if(dataJson.rows[editIndex].attributeName == '' || dataJson.rows[editIndex].attributeName == null){
						dataJson.rows[editIndex].attributeName = '請選擇';
					}
				}
				dataJson.rowIndex = editIndex;
				// 保存編輯系統log
				var logContent = JSON.stringify(dataJson);
				ajaxService.saveSystemLog('<%=IAtomsConstants.ACTION_UPDATE%>', logContent, '<%=IAtomsConstants.UC_NO_ADM_01030 %>', function(result){
				}); 
				
			} else {
				setTimeout(function() {
					$('#dataGrid').datagrid('selectRow', editIndex);
				}, 0);
			}
			$("#btnAdd").linkbutton('disable');
			$("#btnRemove").linkbutton('disable');
			$("#btnDetail").linkbutton('disable');
			$("#btnSave").linkbutton('enable');
			$("#btnCancle").linkbutton('enable');
			$("#btnQuery").linkbutton('disable');
		}
	}

	/**
	* 表格增加一行
	*/
	function btnAdd() {	
		$("div.topSoller").unbind("scroll.validate");	
		$("#msg").empty();		
		if (editIndex < 0) {
			$("#dataGrid").datagrid('acceptChanges');
			$("#dataGrid").datagrid("clearSelections");
			if ($("#dataGrid").datagrid("options").pageNumber == 0) {
				$('#dataGrid').datagrid({pageNumber:1});	
			}
			var newIndex = $('#dataGrid').datagrid('getRows').length;
			if (newIndex == 0) {
				var addInfoList = [];
				var addInfo = new Object();
				addInfo.attribute = "";
				addInfo.updatedByName = '${userName}';
				addInfo.updatedDate = new Date();
				addInfoList.push(addInfo);
				$('#dataGrid').datagrid('loadData',{total:1, rows:addInfoList});
			} else {
				$('#dataGrid').datagrid('appendRow', {
					attribute : "",
					updatedByName:'${userName}',
					updatedDate:new Date()
				});
				newIndex = $('#dataGrid').datagrid('getRows').length - 1;
			}
			$('#dataGrid').datagrid('selectRow', newIndex);
			editIndex = newIndex;
			$('#dataGrid').datagrid('beginEdit', editIndex);
			var roleCode = $('#dataGrid').datagrid('getEditor', { 'index': editIndex, field: 'roleCode' });
			$(roleCode.target).textbox('textbox').attr('maxlength',20);
			var workFlowRoleCode = $('#dataGrid').datagrid('getEditor', { 'index': editIndex, field: 'workFlowRole' });
			$(workFlowRoleCode.target).combobox('loadData', initSelect(null));
			checkTextBox(editIndex);
			if(dataJson == undefined){
				dataJson = $("#dataGrid").datagrid("getData");
				var options = $("#dataGrid").datagrid("options");
				dataJson.pageSize = options.pageSize;
				dataJson.pageNumber = options.pageNumber;
			}
			dataJson.rowIndex = editIndex;
			
			$("#btnRemove").linkbutton('disable');
			$("#btnDetail").linkbutton('disable');
			$("#btnSave").linkbutton('enable');
			$("#btnCancle").linkbutton('enable');
			$("#btnAdd").linkbutton('disable');
			$("#btnQuery").linkbutton('disable');
			$('#admRoleDiv').scrollTop( $('#admRoleDiv')[0].scrollHeight );
		} 
	}
	
	/**
	* 取消編輯
	*/
	function btnCancel() {	
		$("div.topSoller").unbind("scroll.validate");
		confirmCancel(function(){
			$("#msg").empty();
			var rows = $("#dataGrid").datagrid("getRows");
			if ((rows[0].roleId == null || rows[0].roleId ==  "") && rows.length == 1) {
				$('#dataGrid').datagrid('deleteRow', 0);
				$('#dataGrid').datagrid('loadData', {total:0,rows:[]});
			} else {
				$('#dataGrid').datagrid('rejectChanges');
			}
			$("#dataGrid").datagrid('clearSelections');
			editIndex = -1;
			$("#btnSave").linkbutton('disable');
			$("#btnCancle").linkbutton('disable');
			$("#btnDetail").linkbutton('disable');
			$("#btnDelete").linkbutton('disable');
			$("#btnAdd").linkbutton('enable');
			$("#btnQuery").linkbutton('enable');
		});
	}
	
	/**
	* 檢驗textBox框長度以及取消空格
	*/
	function checkTextBox(index) {
		var roleName = $('#dataGrid').datagrid('getEditor', { 'index': index, field: 'roleName' });
		var roleDesc = $('#dataGrid').datagrid('getEditor', { 'index': index, field: 'roleDesc' });
		
		$(roleName.target).textbox('textbox').attr('maxlength',50).bind('blur', function(e){
				$(roleName.target).textbox('setValue', $.trim($(this).val()));
			});
		$(roleDesc.target).textbox('textbox').attr('maxlength',200).bind('blur', function(e){
				$(roleDesc.target).textbox('setValue', $.trim($(this).val()));
			});
	}
	/**
	* 點選明細按鈕
	*/
	function btnDetail() {
		$("div.topSoller").unbind("scroll.validate");
		// 獲得選中的角色編號
		var roleId = getSelectedRoleId();
		if (roleId) {
			// 彈出資料權限清單頁面，並帶入信息
			manageDetail('資料權限清單', '<%=IAtomsConstants.ACTION_INIT_DETAIL%>', roleId);
		}
	}
	var viewDlg;
	//彈出明細畫面
	function manageDetail(title, actionId, roleId) {
		$("#msg").empty();
		var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		$.blockUI(blockStyle);
		viewDlg = $('#dlg').dialog({
			title : title,
			width : 900,
			height : 500,
			closed : false,
			maximizable: true,
			modal: true,
			cache : false,
			queryParams : {
				// 傳入actionId
				actionId : actionId,
				// 角色編號
				roleId : roleId,
			}, 
			onLoad:function(){
				//initValue();
				if(typeof initValue != 'undefined' && initValue instanceof Function) {
					initValue();
				}
				$.unblockUI();
			},
			// 到role.do
			href : "${contextPath}/admRole.do"
		}).dialog("maximize").dialog("center");
	}
</script>
</body>
</html>