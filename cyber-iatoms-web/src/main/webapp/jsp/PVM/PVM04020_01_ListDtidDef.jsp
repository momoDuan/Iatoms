<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.PvmDtidDefDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.DtidDefFormDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<%
	SessionContext ctx = (SessionContext)request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	DtidDefFormDTO formDTO = null;
	if(ctx != null) {
		formDTO = (DtidDefFormDTO) ctx.getResponseResult();
	} else {
		formDTO = new DtidDefFormDTO();
	}
	//String useCaseNo = formDTO.getUseCaseNo();
	//客戶下拉菜單
	List<Parameter> customers = (List<Parameter>)SessionHelper.getAttribute(request, IAtomsConstants.UC_NO_PVM_04020, IAtomsConstants.PARAM_CUSTOMER_LIST);
	//獲取設備名稱下拉菜單
	List<Parameter> assetNames = (List<Parameter>)SessionHelper.getAttribute(request, IAtomsConstants.UC_NO_PVM_04020, IAtomsConstants.PARAM_METHOD_GET_ASSET_LIST);
%>

<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="customers" value="<%=customers%>" scope="page"></c:set>
<c:set var="assetNames" value="<%=assetNames %>"></c:set>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<%@include file="/jsp/common/easyui-common.jsp"%>
	<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
</head>
<body>
<!-- 	<div region="center" style="width: 98%; height: auto; "> -->
<div style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
		<table id="dataGrid" class="easyui-datagrid" title="DTID號碼管理" style="width: 98%; height: auto"
			data-options="
				rownumbers:true,
				pagination:true,
				pageList:[15,30,50,100],
				pageSize:15,
				iconCls: 'icon-edit',
				singleSelect: true,
				method: 'get',
				onClickRow:onClickRow,
				sortOrder:'asc',
				pageNumber:0,
				rownumbers:true,
				nowrap:false,
				toolbar:'#tb'">
			<thead>
				<tr>
					<!--<th data-options="field:'ck',checkbox:true"></th>-->
					<th data-options="field:'companyName',halign:'center',width:'200px',align:'left',sortable:true">客戶</th>
					<th data-options="field:'assetName',halign:'center',width:'200px',align:'left',sortable:true">設備名稱</th>
					<th data-options="field:'dtidStartEnd',halign:'center',width:'200px',align:'center',sortable:true">DTID起迄</th>
					<th data-options="field:'currentNumber',halign:'center',align:'right',width:100,sortable:true">目前號碼</th>
					<th data-options="field:'comment',halign:'center',width:500,align:'left',sortable:true">說明</th>
					<th data-options="field:'updatedByName',halign:'center',align:'left',width:160,sortable:true">異動人員</th>
					<th data-options="field:'updatedDate',halign:'center',align:'center',width:190,sortable:true,formatter:formatToTimeStamp">異動日期</th>
				</tr>
			</thead>
		</table>
		<div id="tb" style="padding: 2px 5px;">
		<form id="searchForm" method="post" novalidate>
				<input type="hidden" id="actionId" name="actionId" />
					<input type="hidden" id="serviceId" name="serviceId" />
					<input type="hidden" id="useCaseNo" name="useCaseNo" />
			客戶:&nbsp;<cafe:droplisttag css="easyui-combobox"
					id="<%=DtidDefFormDTO.QUERY_CUSTOMERS_ID %>"
					name="<%=DtidDefFormDTO.QUERY_CUSTOMERS_ID %>" 
					blankName="請選擇"
					hasBlankValue="true"
					result="${customers }"
					style="width:170px"
					javascript="editable=false" 
					>
				</cafe:droplisttag>
			設備名稱:&nbsp;<cafe:droplisttag css="easyui-combobox"
					id="<%=DtidDefFormDTO.QUERY_ASSET_NAME %>"
					name="<%=DtidDefFormDTO.QUERY_ASSET_NAME %>" 
					blankName="請選擇"
					hasBlankValue="true"
					result="${assetNames }"
					style="width:170px"
					javascript="editable=false" 
					>
				</cafe:droplisttag>
			DTID:&nbsp;<input class="easyui-textbox" style="width: 110px" id ="<%=DtidDefFormDTO.QUERY_DTID_START %>" name="<%=DtidDefFormDTO.QUERY_DTID_START %>" data-options="
				missingMessage:'請輸入DTID起',validType:['maxLength[8]','numberBeginZero[\'DTID起限輸入長度為8碼的數字，請重新輸入\']','numberLengthEquals[\'8\',\'DTID起限輸入長度為8碼的數字，請重新輸入\']']" maxlength="8"> ~ 
			<input class="easyui-textbox" style="width: 110px" name="<%=DtidDefFormDTO.QUERY_DTID_END %>" id ="<%=DtidDefFormDTO.QUERY_DTID_END %>" data-options="
				missingMessage:'請輸入DTID迄', validType:['maxLength[8]','numberBeginZero[\'DTID迄限輸入長度為8碼的數字，請重新輸入\']', 'compareNumberSize[\'#<%=DtidDefFormDTO.QUERY_DTID_START %>\',\'DTID起不可大於DTID迄\']','numberLengthEquals[\'8\',\'DTID迄限輸入長度為8碼的數字，請重新輸入\']']" maxlength="8">
			<a class="easyui-linkbutton" href="#" id="btnQuery" iconcls="icon-search" onclick="queryData(1, true)">查詢</a>&nbsp;
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add'" id="btnAdd" onclick="add()">新增</a>&nbsp;
			<a href="javascript:void(0)" class="easyui-linkbutton" id = "btnEdit" data-options="iconCls:'icon-edit'" onclick="update()" disabled = "true">修改</a>&nbsp;
			<a href="javascript:void(0)" class="easyui-linkbutton" id = "btnDelete" data-options="iconCls:'icon-remove'" onclick="deleteCompanyinfo()" disabled = "true">刪除</a>
			<div style="text-align: right;">
				<a href="javascript:void(0)" id="btnExport" style="width: 150px">匯出</a>
			</div>
			<div style="color: red">
				<span id="msg" class="red"></span>
			</div>
			</form>
		</div>
		<div id="dlg" modal ="true"></div>
	</div>
	<script type="text/javascript">
		var editIndex = undefined;
		$(function(){
			$('#btnExport').attr("onclick","return false;");
			$('#btnExport').attr("style","color:gray;");
		});
		function getQueryParam() {
			var querParam = {
				actionId : "<%=IAtomsConstants.ACTION_QUERY%>",
				queryCustomer : $("#<%=DtidDefFormDTO.QUERY_CUSTOMERS_ID %>").combobox('getValue'),
				queryAssetTypeId : $("#<%=DtidDefFormDTO.QUERY_ASSET_NAME %>").combobox('getValue'),
				queryDtidStart : $("#<%=DtidDefFormDTO.QUERY_DTID_START %>").textbox('getValue'),
				queryDtidEnd : $("#<%=DtidDefFormDTO.QUERY_DTID_END %>").textbox('getValue')
			};
			return querParam;	
			
		}
		//發生單擊事件時
		function onClickRow() {
			$("#msg").empty();
			$("#btnEdit").linkbutton('enable');
			$("#btnDelete").linkbutton('enable');
		}
		//查询数据
		function queryData(pageIndex,isHidden){
			$("#msg").empty();
			if (!$("#searchForm").form("validate")) {
				return false;
			}
			var queryParam = getQueryParam();
			var options = {
				url : "${contextPath}/dtidDef.do",
				queryParams :queryParam,
				pageNumber:pageIndex,
				autoRowHeight:true,
				method:'POST',
				rejectChanges:true,
				onLoadError : function(data) {
					$.messager.alert('提示','查詢資料出錯','error');
					editIndex = undefined;
				},
				onLoadSuccess:function(data){	
					if (isHidden) {
						$("#msg").text("");
					}
					if (data.total == 0) {
						if (isHidden) {
							$("#msg").text(data.msg);
						}
						$('#btnExport').attr("onclick","return false;");
			    		$('#btnExport').attr("style","color:gray;");
					} else {
						$('#btnExport').attr("onclick","exportDtid()");
						$('#btnExport').attr("style","color:blue;");
					}
					$("#btnEdit").linkbutton('disable');
					$("#btnDelete").linkbutton('disable');
					isHidden = true;
					//分页，跳轉前結束編輯
					editIndex = undefined;
				}
			};
			// 清空點選排序
			if(isHidden){
				options.sortName = null;
			}
			openDlgGridQuery("dataGrid", options);
		}
		// 新增
		function add() {
			addAndEditContract("新增DTID號碼管理", '<%=IAtomsConstants.ACTION_INIT_ADD%>', '', 330);
		}
		// 修改
		function update() {
			var row = $('#dataGrid').datagrid('getSelected');
			if (row) {
				var id = row.id;
				addAndEditContract("修改DTID號碼管理",'<%=IAtomsConstants.ACTION_INIT_EDIT%>', id, 370);
			} else {
				$("#msg").empty();
				$.messager.alert('提示','請勾選要操作的記錄!','warning');
				return false;
			}
		}
		var viewDlg;
		function addAndEditContract (title, actionId, id, heigth) {
			$("#msg").empty();
			//如果ID為空，表示進行添加操作，清除所選擇的行
			if (id != null || id != '') {
				var row = $('#dataGrid').datagrid('getSelected');
			}
			viewDlg = $('#dlg').dialog({ 
				title: title,
				width: 640,
				height:heigth,
				top:10,
				cache: false,
				loadingMessage:'正在加載中...',
				//update by hermanwang 2017/05/17 添加dtidId
				queryParams : {
					actionId : actionId,
					id : id,
					dtidId : id
				},
				onLoad :function(){
				//	textBoxMaxlengthSetting();
					textBoxSetting("dlg");
					if (actionId == 'initAdd') {
						if ($('#trDTID').length > 0) {
							$('#trDTID').hide();
						}
					} else {
						<%-- if ($("#<%=PvmDtidDefDTO.ATTRIBUTE.COMPANY_ID.getValue() %>").length > 0) {
							$("#<%=PvmDtidDefDTO.ATTRIBUTE.COMPANY_ID.getValue() %>").combobox('disable');
							$("#<%=PvmDtidDefDTO.ATTRIBUTE.COMPANY_ID.getValue() %>").
						} --%>
						<%-- if ($("#<%=PvmDtidDefDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue() %>").length > 0) {
							$("#<%=PvmDtidDefDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue() %>").combobox('disable');
						} --%>
						if ($("#<%=PvmDtidDefDTO.ATTRIBUTE.CURRENT_NUMBER.getValue() %>").length > 0) {
							var currentNumber = $("#<%=PvmDtidDefDTO.ATTRIBUTE.CURRENT_NUMBER.getValue() %>").val();
							if (currentNumber != "") {
								$("#<%=PvmDtidDefDTO.ATTRIBUTE.DTID_START.getValue() %>").textbox('disable');
							}
						}
					}
				},
				href: "${contextPath}/dtidDef.do",
				buttons : [{
					text:'儲存',
					iconCls:'icon-ok',
					width: '88',
					handler: function(){
						$("#msg1").text("");
						var controls = ['<%=PvmDtidDefDTO.ATTRIBUTE.COMPANY_ID.getValue() %>','<%=PvmDtidDefDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue() %>'];
						if(!validateForm(controls)) {
							return false;
						}
						var form = viewDlg.find("#fm");
						if (form.form("validate")) {
							// 取form表單的所有的值
							var saveParam = form.form("getData");
							if (actionId == "initEdit") {
								saveParam.companyId = $("#<%=PvmDtidDefDTO.ATTRIBUTE.COMPANY_ID.getValue() %>").combobox("getValue");
								saveParam.assetTypeId = $("#<%=PvmDtidDefDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue() %>").combobox("getValue");
							}
							var currentNumber = saveParam.currentNumber;
							if (currentNumber != "" && actionId == "initEdit") {
							 	var dtidEnd = saveParam.dtidEnd;
								if (parseInt(dtidEnd) < parseInt(currentNumber)) {
									$("#msg1").text("DTID迄不可小於當前號碼，請重新輸入");
									return false;
								}
							}
							commonSaveLoading('dlg');
							$.ajax({
								url: "${contextPath}/dtidDef.do?actionId=save",
								data:saveParam, 
								type:'post', 
								cache:false, 
								dataType:'json', 
								success:function(json){
									commonCancelSaveLoading('dlg');
									if (json.success) {
										viewDlg.dialog('close');
										var pageIndex = getGridCurrentPagerIndex("dataGrid");
										if (pageIndex < 1) {
											pageIndex = 1;
										}
										queryData(pageIndex,false);
										$("#msg").text(json.msg);
										$("#btnEdit").linkbutton('disable');
										$("#btnDelete").linkbutton('disable');
									}else{
										$("#msg1").text(json.msg);
									}
									
								},
								error:function(){
									commonCancelSaveLoading('dlg');
									$.messager.alert('提示', "保存失敗,請聯繫管理員.", 'error');
								}
							});
						}
					}
				},{
					text:'取消',
					iconCls:'icon-cancel',
					width: '88',
					handler: function(){
						confirmCancel(function(){
							viewDlg.dialog('close');
						});
					}
				}]
			});
		}
		//刪除DTID
		function deleteCompanyinfo() {
			var row = $('#dataGrid').datagrid('getSelected');
			var id = row.id;
			ajaxService.isUseDtid(id, function(result){
				if (result) {
					$("#msg").text("該DTID起迄之DTID號碼已使用，無法刪除");
				} else {
					var url = "${contextPath}/dtidDef.do?actionId=delete";
					//update by hermanwang 2017/05/17 添加dtidId
					var params = {id : id , dtidId : id};
					var successFunction = function(json){
						if (json.success) {
							var pageIndex = getGridCurrentPagerIndex("dataGrid");
							if (pageIndex < 1) {
								pageIndex = 1;
							}
							queryData(pageIndex,false);
								$("#msg").text(json.msg);
						}else{
							$("#msg").text(json.msg);
						}
					};
					var errorFunction = function(){
						$.messager.alert('提示', "刪除失敗,請聯繫管理員.", 'error');	
					};
					commonDelete(params,url,successFunction,errorFunction);
				}
			});
		}
		
		
		//保存DTID
		function save() {
			
		}
		
		function reject() {
			
		}
		
		function exportDtid() {
			<%-- var rows = $("#dataGrid").datagrid("getData");	
			var queryParam = $('#searchForm').form("getData");
			//將參數解析為字串
			var url = parseParam(queryParam);
			if(rows.total > 0){
				window.location.href = '${contextPath}//dtidDef.do?actionId=<%=IAtomsConstants.ACTION_EXPORT%>&' + url;
			} --%>
			var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			$.blockUI(blockStyle);
			
			var useCaseNo = '<%=IAtomsConstants.UC_NO_PVM_04020%>';
			actionClicked(document.forms["searchForm"],
				useCaseNo,
				'',
				'export');
			
			ajaxService.getExportFlag(useCaseNo,function(data){
				$.unblockUI();
			});
		}
		
		//將參數解析為字串
	var parseParam = function(param, key){
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
	
	
	//選擇DTID
	$("#chooseDTID").click(function(){
		viewEditData('選擇DTID','<%=IAtomsConstants.ACTION_INIT_VIEW%>');
	});
	
	 function viewEditData(title, actionId) {
	 	var viewDlg = $('#dlg').dialog({    
		    title : title,    
		    width : 800,
		    height : 500,
		    closed : false,
		    cache : false,
		    href : "${contextPath}/dtidDef.do?actionId=initView",
		    modal : true,
		    buttons : [{
					text:'確認',
					iconCls:'icon-ok',
					width: '90',
					handler : function() {
							var row = $('#dgDTID').datagrid('getSelected');
							if (row == null) {
								$.messager.alert('提示', '請選擇記錄!', 'warning');
								return false;
							} else {
								$('#dlg').dialog('close');
								$('#dtid').textbox('setValue', row.dtid);
							}
						}
					},{
					text:'取消',
					iconCls:'icon-cancel',
					width: '90',
					handler: function() {
						confirmCancel(function() {
							viewDlg.dialog('close');
						});
					}
				}]
		}).dialog("center");
		
	}
	//選擇EDC
		$("#chooseEDC").click(function(){
			viewChooseEDC('選擇EDC','<%=IAtomsConstants.ACTION_INIT_VIEW%>');
		});
		 function viewChooseEDC(title, actionId) {
			 	var viewDlg = $('#dlg').dialog({    
					title : title,    
					width : 800,
					height : 500,
					closed : false,
					cache : false,
					href : "${contextPath}/assetManage.do?actionId=initChooseEDC",
					modal : true,
					buttons : [{
							text:'確認',
							iconCls:'icon-ok',
							width: '90',
							handler : function() {
									var row = $('#dgEDC').datagrid('getSelected');
									if (row == null) {
										$.messager.alert('提示', '請選擇記錄!', 'warning');
										return false;
									} else {
										$('#dlg').dialog('close');
										$('#dtid').textbox('setValue', row.name);
									}
								}
							},{
							text:'取消',
							iconCls:'icon-cancel',
							width: '90',
							handler: function() {
								confirmCancel(function() {
									viewDlg.dialog('close');
								});
							}
						}]
				}).dialog("center");
				
			}
	$("#<%=DtidDefFormDTO.QUERY_DTID_START %>").textbox({
		onChange:function(newValue, oldValue){
			changeRequired(newValue, $('#<%=DtidDefFormDTO.QUERY_DTID_END %>'), "請輸入DTID迄");
			$('#<%=DtidDefFormDTO.QUERY_DTID_END %>').textbox('isValid');
		}
	});
	
	$("#<%=DtidDefFormDTO.QUERY_DTID_END %>").textbox({
		onChange:function(newValue, oldValue){
			changeRequired(newValue, $('#<%=DtidDefFormDTO.QUERY_DTID_START %>'), "請輸入DTID起");
		}
	});
	
	function changeRequired(newValue, obj,msg){
		var options = obj.textbox('options');
		if (newValue != '') {
			options.required = true;
			/* if(!options.required){
				obj.textbox({
					required:true,
				});
			} */
		} else {
			options.required = false;
			/* obj.textbox({
				required:false,
			}); */
		}
		obj.textbox(options);
		textBoxDefaultSetting(obj);
	}
	</script>
</body>
</html>