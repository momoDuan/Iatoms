<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.AdmUserFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO"%>

<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	AdmUserFormDTO formDTO = null;
	String ucNo = null;
	if (ctx != null) {
		// 得到FormDTO
		formDTO = (AdmUserFormDTO) ctx.getRequestParameter();
		if (formDTO != null) {
			// 获得UseCaseNo
			ucNo = formDTO.getUseCaseNo();
		} else {
			ucNo = IAtomsConstants.UC_NO_ADM_01010;
			formDTO = new AdmUserFormDTO();
		}
	} else {
		ucNo = IAtomsConstants.UC_NO_ADM_01010;
		formDTO = new AdmUserFormDTO();
	}
	
	//公司列表
	List<Parameter> companyList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, AdmUserFormDTO.PARAM_COMPANY_LIST);
	//角色列表
	List<Parameter> roleList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, AdmUserFormDTO.PARAM_ROLE_LIST);
	//狀態
	List<Parameter> statusList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.ACCOUNT_STATUS.getCode());
%>
<html>

<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="companyList" value="<%=companyList%>" scope="page"></c:set>
<c:set var="roleList" value="<%=roleList%>" scope="page"></c:set>
<c:set var="statusList" value="<%=statusList%>" scope="page"></c:set>
<c:set var="ucNo" value="<%=ucNo%>" scope="page"></c:set>
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$FUNCTION_TYPE" var="functionType" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>AdmUser List</title>
<link rel="stylesheet" type="text/css" href="${contextPath}/assets/css/multi-select.css">
<%@include file="/jsp/common/easyui-common.jsp"%>
<script type="text/javascript" src="${contextPath}/assets/js/jquery.multi-select.js"></script>
<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>

<script type="text/javascript">	
	$(function(){
		//查詢
	 	$("#btnQuery").click(function(){
			queryData(1,true);
		}); 
		//新增
		$("#btnAdd").linkbutton({
			onClick : function (){
				viewEditData('新增使用者帳號','<%=IAtomsConstants.ACTION_INIT_ADD%>', '');
			}
		});
		/*
		* 进入编辑頁面时
		*/
		$("#btnEdit").linkbutton({
			onClick : function (){
				var params = getSelectedParams();
				var status = params.status;
				var userId = params.userId;
				var companyId = params.companyId;
				if('<%=IAtomsConstants.ACCOUNT_STATUS_DISABLED %>' == status){
					$("#msg").text("此帳號已終止使用，不可修改");
				} else {
					viewEditData('修改使用者帳號','<%=IAtomsConstants.ACTION_INIT_EDIT%>', userId);
				}
			}
		});
		//刪除
		$("#btnDelete").linkbutton({
			onClick : function (){
				confirmDelete();
			}
		});
	});
	// 獲得操作行的userId与status
	function getSelectedParams() {
		var params = '';
		var row = $("#dataGrid").datagrid('getSelected');
		if (row != null) {
			var params = {
				userId : row.userId,
				status : row.status,
				companyId : row.companyId
			}
			return params;
		} else {
			$("#msg").text("請勾選要操作的記錄!");
			return null;
		}
	}
	// 保存查詢結果的對象
	var dataJson;
	/*
	* 查詢
	*/
	function queryData(pageIndex, hidden) {
		var queryParam = $('#searchForm').form("getData");
		var options = {
				url : "${contextPath}/admUser.do?actionId=<%=IAtomsConstants.ACTION_QUERY%>",
				queryParams :queryParam,
				pageNumber:pageIndex,
				onLoadSuccess:function(data){
					// 保存查詢條件與結果數據
					dataJson = $("#dataGrid").datagrid("getData");
					var options = $("#dataGrid").datagrid("options");
					dataJson.pageSize = options.pageSize;
					dataJson.pageNumber = options.pageNumber;
					dataJson.queryCompanyName = $("#queryCompany").combobox('getText');
					dataJson.queryRoleName = $("#queryRole").combobox('getText');
					dataJson.queryStatusName = $("#queryStatus").combobox('getText');
				 	dataJson.queryAccount = $("#queryAccount").textbox('getValue');
					dataJson.queryCname = $("#queryCname").textbox('getValue');
					dataJson.queryCompany = $("#queryCompany").combobox('getValue');
					dataJson.queryRole = $("#queryRole").combobox('getValue');
					dataJson.queryStatus = $("#queryStatus").combobox('getValue'); 
					if (hidden) {
						var logContent = JSON.stringify(dataJson);
						// 保存查詢log
						ajaxService.saveSystemLog('<%=IAtomsConstants.ACTION_QUERY%>', logContent, '<%=IAtomsConstants.UC_NO_ADM_01010 %>', function(result){
						}); 
						$("#msg").text("");
						if (data.total == 0) {
							// 提示查無數據
							$("#msg").text(data.msg);
						} 
					}
					if (data.total == 0) {
						$('#btnExport').attr("onclick","return false;");
						$('#btnExport').attr("style","color:gray;");
					} else {
						$('#btnExport').attr("onclick","exportData()");
						$('#btnExport').attr("style","color:blue;");
					}
					hidden = true;
				},
				onLoadError : function() {
					$("#msg").text("查詢使用者資料出錯");
				}
			}
		// 清空點選排序(注：若初始化直接使用datagrid的sortName进行排序的请再次赋初值)
		if(hidden){
			options.sortName = null;
		}
		openDlgGridQuery("dataGrid", options);
	}
	// 保存進入編輯頁面數據對象
	var initEditJson;
	/*
	* 進入編輯與新增頁面
	*/
	function viewEditData(title, actionId, userId) {
		$("#msg").text("");
		var viewDlg = $('#dlg').dialog({    
		    title: title,    
		    width: 1000,
		    height:530,
		    top:10,
		    closed: false,    
		    cache: false,
		    queryParams : {
		    	actionId : actionId,
		    	userId : userId
		    },
		    href: "${contextPath}/admUser.do",
		    modal: true,
		    onLoad : function() {
		    	// 頁面加載完畢時保存當前數據
		    	 //setTimeout(function(){
					if('<%=IAtomsConstants.ACTION_INIT_EDIT%>' == actionId){
						// 保存進入編輯頁面數據
						initEditJson = $('#saveForm').form("getData");
						//修改編輯頁面帳號的id相同的問題  2018/01/02
						if( initEditJson.account == undefined && initEditJson.editAccount!= undefined){
							initEditJson.account = initEditJson.editAccount;
						}
				    	initEditJson.companyId = $("#<%=AdmUserDTO.ATTRIBUTE.COMPANY_ID.getValue()%>").combobox('getValue');
				    	initEditJson.companyName = $("#<%=AdmUserDTO.ATTRIBUTE.COMPANY_ID.getValue()%>").combobox('getText');
				    	initEditJson.deptCode = $("#<%=AdmUserDTO.ATTRIBUTE.DEPT_CODE.getValue()%>").combobox('getValue');
				    	initEditJson.deptName = $("#<%=AdmUserDTO.ATTRIBUTE.DEPT_CODE.getValue()%>").combobox('getText');
				    	initEditJson.statusName = $("#<%=AdmUserDTO.ATTRIBUTE.STATUS.getValue()%>").combobox('getText');
				    	initEditJson.retry = $("#<%=AdmUserDTO.ATTRIBUTE.RETRY.getValue()%>").textbox('getValue');
				    	if(initEditJson.dataAcl){
				    		initEditJson.dataAcl = 'Y';
						} else {
							initEditJson.dataAcl = 'N';
						}
						// 已選角色權限
						var selectRoleStr = $('#saveForm').find("#roleMultipleSelect").children("option[selected]");
						var selectRoleStrArr = new Array();
						selectRoleStr.each(function(index,obj){
							selectRoleStrArr.push({"text":$(obj).html(),"value":$(obj).val()});
						})
						initEditJson.selectFunctionStr = JSON.stringify(selectRoleStrArr);
						// 全部角色權限
						var roleStrAll = $('#saveForm').find("#roleMultipleSelect").children("option");
						var roleStrAllArr = new Array();
						roleStrAll.each(function(index,obj){
							roleStrAllArr.push({"text":$(obj).html(),"value":$(obj).val()});
						})
						initEditJson.functionStrAll = JSON.stringify(roleStrAllArr);
						// 已選倉庫控管
						var selectWarehouseStr = $('#saveForm').find("#warehouseMultipleSelect").children("option[selected]");
						var selectWarehouseStrArr = new Array();
						selectWarehouseStr.each(function(index,obj){
							selectWarehouseStrArr.push({"text":$(obj).html(),"value":$(obj).val()});
						})
						initEditJson.selectWarehouseStr = JSON.stringify(selectWarehouseStrArr);
						
						// 全部倉庫控管
						var warehouseStrAll = $('#saveForm').find("#warehouseMultipleSelect").children("option");
						var warehouseStrAllArr = new Array();
						warehouseStrAll.each(function(index,obj){
							warehouseStrAllArr.push({"text":$(obj).html(),"value":$(obj).val()});
						})
						initEditJson.warehouseStrAll = JSON.stringify(warehouseStrAllArr);
						// 記錄系統log
						var logContent = JSON.stringify(initEditJson);
						ajaxService.saveSystemLog('<%=IAtomsConstants.ACTION_INIT_EDIT%>', logContent, '<%=IAtomsConstants.UC_NO_ADM_01010 %>', function(result){
						});
					}
				//},1000);
		    	// 检查多选框是否被选中 處理倉庫控管下拉框顯示
				if(typeof checkMultipleSelect != 'undefined' && checkMultipleSelect instanceof Function){
				    checkMultipleSelect();
				}
		    	// 密碼框內容改變時觸發 編輯時輸入密碼后必須輸入確認密碼
		    	if(typeof passwordChanged != 'undefined' && passwordChanged instanceof Function){
		    		passwordChanged();
				}
		    	// 编辑时判斷是否禁用密碼框
		    	if(typeof disabledPassword != 'undefined' && disabledPassword instanceof Function){
		    		disabledPassword();
				}
				// maxlength屬性生效方法
		    	textBoxSetting("dlg");
			},
		    buttons : [{
				text:'儲存',
				width:'88',
				iconCls:'icon-ok',
				handler: function(){
					// 判斷密碼輸入確認密碼必輸
					var password = $("#<%=AdmUserDTO.ATTRIBUTE.PASSWORD.getValue()%>").textbox('getValue');
					var rePassword = $("#<%=AdmUserDTO.ATTRIBUTE.REPASSWORD.getValue()%>").textbox('getValue');
					if(password){
						if(!rePassword || isEmpty(rePassword)){
							var repwdOptions = $('#<%=AdmUserDTO.ATTRIBUTE.REPASSWORD.getValue()%>').textbox('options');
							repwdOptions.required = true;
							$('#<%=AdmUserDTO.ATTRIBUTE.REPASSWORD.getValue()%>').textbox(repwdOptions);
						}
					}
					// 保存驗證頁面
					var controls = ['companyId','deptCode','account','cname','ename','tel','mobile','email',
							'managerEmail', 'userDesc', 'retry', 'status', 'password', 'repassword'];
					if(validateForm(controls) && $('#saveForm').form("validate")){
						// 調用公用方法 處理保存按鈕點擊形成遮罩
						commonSaveLoading('dlg');
						// 保存數據方法
						saveData(userId);
					}
				}
			},{
				text:'取消',
				width:'88',
				iconCls:'icon-cancel',
				handler: function(){
					$.messager.confirm('確認對話框','確認取消?', function(confirm) {
						if (confirm) {
							viewDlg.dialog('close');
						}
					});
				}
			}]
		});
	}
	// 保存關閉編輯頁面數據對象
	var endEditJson;
	/*
	* 保存数据
	*/
	function saveData(userId){
		$("#dialogMsg").text("");
		var saveParam = $('#saveForm').form("getData");
		//修改編輯頁面 帳號的id相同的問題  將隱藏域account 改為 editAccount  2018/01/02
		if( saveParam.account == undefined && saveParam.editAccount != undefined){
			saveParam.account = saveParam.editAccount;
		}
		// 角色權限
		var roleGroup = $('#saveForm').find("#roleMultipleSelect").val();
		// 倉管權限
		var warehouseGroup = $('#saveForm').find("#warehouseMultipleSelect").val();
		if (roleGroup == null) {
			roleGroup = '';
		} else {
			var selectRoleStrArr = new Array();
			for(var i=0; i<roleGroup.length; i++){
				selectRoleStrArr.push({"text":'',"value":roleGroup[i]});
			}
			saveParam.selectFunctionStr = JSON.stringify(selectRoleStrArr); 
		}
		if (warehouseGroup == null){
			warehouseGroup = '';
		} else {
			var selectWarehouseStrArr = new Array();
			for(var i=0; i<warehouseGroup.length; i++){
				selectWarehouseStrArr.push({"text":'',"value":warehouseGroup[i]});
			}
			saveParam.selectWarehouseStr = JSON.stringify(selectWarehouseStrArr);
		}
		if(saveParam.dataAcl){
			saveParam.dataAcl = 'Y';
		} else {
			saveParam.dataAcl = 'N';
		}
		// 保存頁面得到數據
		saveParam.companyId = $("#<%=AdmUserDTO.ATTRIBUTE.COMPANY_ID.getValue()%>").combobox('getValue');
		saveParam.companyName = $("#<%=AdmUserDTO.ATTRIBUTE.COMPANY_ID.getValue()%>").combobox('getText');
		saveParam.deptCode = $("#<%=AdmUserDTO.ATTRIBUTE.DEPT_CODE.getValue()%>").combobox('getValue');
		saveParam.deptName = $("#<%=AdmUserDTO.ATTRIBUTE.DEPT_CODE.getValue()%>").combobox('getText');
		saveParam.statusName = $("#<%=AdmUserDTO.ATTRIBUTE.STATUS.getValue()%>").combobox('getText');
		saveParam.retry = $("#<%=AdmUserDTO.ATTRIBUTE.RETRY.getValue()%>").textbox('getValue');
		
		var password = $("#<%=AdmUserDTO.ATTRIBUTE.PASSWORD.getValue()%>").textbox('getText');
		var pwdShowText ='';
		if(password){
			for(var i=0; i<password.length; i++){
				pwdShowText +='*';
			}
		}
		saveParam.pwdShowText = pwdShowText;
		// 全部角色權限
		var roleStrAll = $('#saveForm').find("#roleMultipleSelect").children("option");
		var roleStrAllArr = new Array();
		roleStrAll.each(function(index,obj){
			roleStrAllArr.push({"text":$(obj).html(),"value":$(obj).val()});
		})
		saveParam.functionStrAll = JSON.stringify(roleStrAllArr);
		// 全部倉庫控管
		var warehouseStrAll = $('#saveForm').find("#warehouseMultipleSelect").children("option");
		var warehouseStrAllArr = new Array();
		warehouseStrAll.each(function(index,obj){
			warehouseStrAllArr.push({"text":$(obj).html(),"value":$(obj).val()});
		})
		saveParam.warehouseStrAll = JSON.stringify(warehouseStrAllArr);
		endEditJson = saveParam;
		// 保存系統log
		var logContent = JSON.stringify(endEditJson);
		saveParam.logContent = logContent;
		var url = "${contextPath}/admUser.do?actionId=<%=IAtomsConstants.ACTION_SAVE%>&functionGroup="+ roleGroup +"&warehouseGroup="+ warehouseGroup;
		//save
		$.ajax({
			url: url,
			data:saveParam,
			type:'post', 
			cache:false, 
			dataType:'json', 
			success:function(json){
				// 去除保存遮罩
				commonCancelSaveLoading('dlg');
				if (json.success) {
					$("#dlg").dialog('close');
					$("#msg").text(json.msg);
					// 調查詢方法
					var pageIndex = getGridCurrentPagerIndex("dataGrid");
					queryData(pageIndex, false);
				} else {
					handleScrollTop('dlg');
					$("#dialogMsg").text(json.msg);
				}
			},
			error:function(){
				// 去除保存遮罩
				commonCancelSaveLoading('dlg');
				var msg;
				if (isEmpty(userId)) {
					msg = "新增失敗";
				} else {
					msg = "修改失敗";
				}
				$("#dialogMsg").text(msg);
			}
		});
	}
	
	/*
	* 確認刪除
	*/
	function confirmDelete(){
		$("#msg").text("");
		var params = getSelectedParams();
		var userId = params.userId;
		if (userId) {
			$.messager.confirm('確認對話框','確認刪除?', function(confirm) {
				if (confirm) {
					var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
					$.blockUI(blockStyle);
					dataJson.rowIndex = $('#dataGrid').datagrid('getRowIndex', $("#dataGrid").datagrid('getSelected'));
					var logContent = JSON.stringify(dataJson);
					deleteData(userId,logContent);
				}
			});
		}
	}

	/*
	* 刪除數據
	*/
	function deleteData(userId,logContent){
		$.ajax({
			url:'${contextPath}/admUser.do?actionId=<%=IAtomsConstants.ACTION_DELETE%>',
			data:{userId : userId,logContent:logContent},
			type:'post', 
			cache:false, 
			dataType:'json',
			success:function(json) {
				$.unblockUI();
				if (json.success) {
					$("#msg").text(json.msg);
					//計算要顯示的頁碼
					var pageIndex = calDeletePagerIndex("dataGrid");
					queryData(pageIndex, false);
				} else {
					$("#msg").text(json.msg);
				}
			},
			error:function(){
				$.unblockUI();	
				$("#msg").text("刪除失敗.");							
			}
		});
	}
	
	/*
	* 執行匯出
	*/
	function executeExport(url,queryParam){
		var param;
		if(queryParam){
			var param = parseParam(queryParam);
		}
		window.location.href = url + '&' + param;
/* 		// 遮罩樣式
		var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		$.blockUI(blockStyle);
		// 瀏覽器響應
		completeResponse(); */
	}
	/*
	* 匯出
	*/
	function exportData(){
		$("#msg").text("");
		var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		$.blockUI(blockStyle);
		
		var queryParam = $('#searchForm').form("getData");
		executeExport('${contextPath}/admUser.do?actionId=<%=IAtomsConstants.ACTION_EXPORT%>',queryParam);
		
		ajaxService.getExportFlag('${ucNo}',function(data){
			$.unblockUI();
		});
	}
	/*
	* 將參數解析為字串
	*/
	function parseParam(param, key){
		var paramStr = "";
		if(param instanceof String||param instanceof Number||param instanceof Boolean){
			if(param instanceof String){
				paramStr+="&"+key+"="+encodeURIComponent(encodeURIComponent(param));
			} else {
				paramStr+="&"+key+"="+encodeURIComponent(param);
			}
		} else {
			$.each(param,function(i){
				var k = key==null ? i:key + (param instanceof Array?"["+i+"]":"."+i);
				paramStr += '&' + parseParam(this, k);
			});
		}
		return paramStr.substr(1);
	};
	
	// 瀏覽器響應
	function completeResponse(){
		 if (typeof ScriptEngine == 'function') { //IE
	         document.onreadystatechange = function () {
	             if (document.readyState == "interactive") {
	            	 $.unblockUI();
	             }
	         }
		}
      	if (isChrome = navigator.userAgent.indexOf("Chrome") > 0) {
      		setTimeout(function(){
      			$.unblockUI();
			},1000); 
        }
      	
      	 if (isFirefox = navigator.userAgent.indexOf("Firefox") > 0) { 
      		setTimeout(function(){
      			$.unblockUI();
			},1000); 
         }
	} 
</script>
</head>
<body>	
	<div style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
		<table id="dataGrid" class="easyui-datagrid" title="使用者帳號管理"
			style="width: 98%; height: auto"
			data-options="
				fitColumns:false,
				border:true,
				nowrap : false,
				pagination:true,
				idField:'account',
				remoteSort:true,
				singleSelect:true,
				pageNumber:0,
				pageList:[15,30,50,100],
				pageSize:15,
				selectOnCheck : true,
				toolbar : '#toolbar',
				rownumbers:true,
				iconCls: 'icon-edit',
				remoteSort:true
				">
			<thead>
				<tr>
					<th data-options="field:'account',width:210,sortable:true,halign:'center',align:'left'">帳號</th>
					<th data-options="field:'cname',sortable:true,width:150,halign:'center',align:'left'">中文姓名</th>
					<th data-options="field:'company',sortable:true,width:260,halign:'center',align:'left'">公司</th>
					<th data-options="field:'email',sortable:true,width:311,halign:'center',align:'left'">EMail</th>
					<th data-options="field:'functionGroup',sortable:true,width:800,halign:'center',align:'left'">角色權限</th>
					<th data-options="field:'deptName',sortable:true,width:400,halign:'center',align:'left'">部門</th>
					<th data-options="field:'dataAcl',width:120,sortable:true,halign:'center',align:'left'" formatter="fomatterYesOrNo">控管資料權限</th>
					<th data-options="field:'accountStatus',width:80,sortable:true,halign:'center',align:'left'">狀態</th>
					<th data-options="field:'createdByName',sortable:true,width:150,halign:'center',align:'left'">建檔人員</th>
					<th data-options="field:'updatedByName',sortable:true,width:150,halign:'center',align:'left'" >異動人員</th>
					<th data-options="field:'updatedDate',width:180,sortable:true,halign:'center',align:'center',formatter:formatToTimeStamp">異動日期</th>
					<th data-options="field:'status',width:80,hidden:'true'">狀態</th>
					<th data-options="field:'companyId',width:80,hidden:'true'">公司</th>
				</tr>
			</thead>
		</table>
		<div id="toolbar" style="padding: 2px 5px;">
		<form id="searchForm" style="margin: 4px 0px 0px 0px">
				<label>帳號:&nbsp;</label><input id="<%=AdmUserFormDTO.QUERY_ACCOUNT%>"
					name="<%=AdmUserFormDTO.QUERY_ACCOUNT%>" class="easyui-textbox"
					style="width: 80px;" 
					maxlength="20" data-options="validType:['maxLength[20]']"/> 
				<label>中文姓名:&nbsp;</label><input
					id="<%=AdmUserFormDTO.QUERY_CNAME%>"
					name="<%=AdmUserFormDTO.QUERY_CNAME%>" class="easyui-textbox"
					style="width: 80px;" 
					maxlength="50" data-options="validType:['maxLength[50]']"/> 
				<label>公司:&nbsp;</label>
				<cafe:droplisttag css="easyui-combobox"
					id="<%=AdmUserFormDTO.QUERY_COMPANY %>"
					name="<%=AdmUserFormDTO.QUERY_COMPANY %>" result="${companyList }"
					blankName="請選擇" hasBlankValue="true"
					selectedValue="${formDTO.queryCompany }"
					style="width: 150px"
					javascript="editable='false'"></cafe:droplisttag>
				<label>角色權限:&nbsp;</label>
				<cafe:droplisttag css="easyui-combobox"
					id="<%=AdmUserFormDTO.QUERY_ROLE %>"
					name="<%=AdmUserFormDTO.QUERY_ROLE %>" result="${roleList }"
					blankName="請選擇(複選)" hasBlankValue="true"
					selectedValue="${formDTO.queryRole }" style="width: 150px"
					javascript="editable='false' multiple=true"></cafe:droplisttag>
				<label>狀態:&nbsp;</label>		
				<cafe:droplisttag css="easyui-combobox"
					id="<%=AdmUserFormDTO.QUERY_STATUS %>"
					name="<%=AdmUserFormDTO.QUERY_STATUS %>" result="${statusList }"
					blankName="請選擇" hasBlankValue="true"
					selectedValue="${formDTO.queryStatus }" style="width: 80px"
					javascript="editable='false' panelheight='auto' "></cafe:droplisttag>&nbsp;
				<c:if test="${fn:contains(logonUser.accRghts[ucNo], functionType.QUERY.code )}"><a href="javascript:void(0)" id="btnQuery" class="easyui-linkbutton"
					iconcls="icon-search" >查詢</a>&nbsp; </c:if>
				<c:if test="${fn:contains(logonUser.accRghts[ucNo], functionType.ADD.code )}"><a href="javascript:void(0)"
					id="btnAdd" class="easyui-linkbutton"
					data-options="iconCls:'icon-add'">新增</a>&nbsp;</c:if>
				<c:if test="${fn:contains(logonUser.accRghts[ucNo], functionType.EDIT.code )}"><a	href="javascript:void(0)" id="btnEdit" class="easyui-linkbutton"
					data-options="iconCls:'icon-edit'">修改</a>&nbsp; </c:if>
				<c:if test="${fn:contains(logonUser.accRghts[ucNo], functionType.DELETE.code )}"><a	href="javascript:void(0)" id="btnDelete" class="easyui-linkbutton"
					data-options="iconCls:'icon-remove'">刪除</a></c:if>
				<div style="height: 10px"></div>
				<div><span id="msg" class="red"></span></div>
				<div style="text-align: right;">
					<c:if test="${fn:contains(logonUser.accRghts[ucNo], functionType.EXPORT.code )}"><a href="javascript:void(0)" id="btnExport" style="width: 150px" onclick="exportData()">匯出</a></c:if>
				</div>
		</form>
		</div>
		<!-- 新增修改彈出框 -->
		<div id="dlg"></div>
	</div>
	<script type="text/javascript">
		$(function () {
			$("#<%=AdmUserFormDTO.QUERY_ROLE %>").combobox({
				onSelect : function(newValue) {
					selectMultiple(newValue,"<%=AdmUserFormDTO.QUERY_ROLE %>");
				},
				onUnselect : function(newValue) {
					unSelectMultiple(newValue,"<%=AdmUserFormDTO.QUERY_ROLE %>");
				},
			});
		});
	</script>
</body>
</html>