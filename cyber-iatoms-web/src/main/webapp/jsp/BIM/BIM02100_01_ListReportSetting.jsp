<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.ReportSettingFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.ReportSettingDTO"%>
<%
	//初始化加載下拉框數據
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	//獲取客戶下拉框的內容
	List<Parameter> customerNames = 
		(List<Parameter>) SessionHelper.getAttribute(request,IAtomsConstants.UC_NO_BIM_02100, IAtomsConstants.PARAM_CUSTOMER_LIST);
	//獲取報表名稱下拉框的值
	List<Parameter> reportList = 
		(List<Parameter>) SessionHelper.getAttribute(request,IAtomsConstants.UC_NO_BIM_02100, IATOMS_PARAM_TYPE.REPORT_CODE.getCode());
%>
<c:set var="customerNames" value="<%=customerNames%>" scope="page"></c:set>
<c:set var="reportList" value="<%=reportList%>" scope="page"></c:set>
<link href="${contextPath}/css/iatoms_style.css" type="text/css" rel="stylesheet"/> 
<html>
<head>
<meta charset="UTF-8">
<%@include file="/jsp/common/easyui-common.jsp"%>
<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
</head>
<body>
<!-- 	<div region="center" style="width: auto; height: auto; padding: 1px; overflow-y: hidden"> -->
<div style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
	<table id="dg" class="easyui-datagrid" title="報表發送功能設定" style="width: 98%; height: auto"
            data-options="
                rownumbers:false,
                pagination:true,
                pageList:[15,30,50,100],
				pageSize:15,
				iconCls: 'icon-edit',
				pageNumber:0,
				singleSelect: true,
				onClickRow: onClickRow,
				nowrap:false,
				toolbar:'#tb'">
            <thead>
                <tr>
                    <th data-options="field:'rowNumber',halign:'center',width:30,align:'center',styler: function(value,rowData,rowIndex){
                    	return 'background-color:#F4F4F4;';
                    }"></th>
                	<th data-options="field:'settingId',halign:'center',width:140,hidden:true">報表編號</th>
                    <th data-options="field:'customerName',width:260,sortable:true,halign:'center'">客戶</th>
                    <th data-options="field:'reportCode',width:280,sortable:true,hidden:true">報表名稱</th>
                    <th data-options="field:'reportName',width:280,sortable:true,halign:'center'">報表名稱</th>
                    <th data-options="field:'reportDetail',width:200,halign:'center'">報表明細</th>
                    <th data-options="field:'recipient',width:250,sortable:true,halign:'center',formatter:formatMail">收件人</th>
                    <th data-options="field:'copy',halign:'center',width:250,sortable:true,formatter:formatMail">副本</th>
					<th data-options="field:'remark',halign:'center',width:500,sortable:true">備註</th>
                </tr>
            </thead>
        </table>
        <div id="tb" style="padding: 2px 5px;">
        	<form id="queryId">
	            客戶: 
	         	<cafe:droplisttag 
	               id="<%=ReportSettingFormDTO.QUERY_CUSTOMER_ID%>"
	               name="<%=ReportSettingFormDTO.QUERY_CUSTOMER_ID%>" 
	               css="easyui-combobox"
	               result="${customerNames}"
	               hasBlankValue="true"
	               blankName="請選擇"
	               style="width: 180px"
	               javascript="editable=false">
	             </cafe:droplisttag>
	       		報表名稱:
	            <cafe:droplisttag 
	               id="<%=ReportSettingFormDTO.QUERY_REPORT_CODE%>"
	               name="<%=ReportSettingFormDTO.QUERY_REPORT_CODE%>" 
	               css="easyui-combobox"
	               result="${reportList}"
	               hasBlankValue="true"
	               blankName="請選擇"
	               style="width: 200px"
	               javascript="editable=false">
	             </cafe:droplisttag>
	            <a href="#" class="easyui-linkbutton" iconcls="icon-search" id="btnQuery">查詢</a>
	            <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add'" id="btnAdd">新增</a>
	            <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-edit"  id="btnEdit" onclick="update('<%=IAtomsConstants.NO%>')">修改</a>
	            <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" id="btnDelete" onclick="deleteData()">刪除</a>
	            <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-tip'" id="btnSend" onclick="sendData('<%=IAtomsConstants.YES%>')">重送</a>
	        	<div class="red" id="message" ></div>
        	</form>
        </div>
        <div id="dlg"></div>
	</div>
<script type="text/javascript">
	//若存在選中的行就啟用修改、刪除按鈕
	function onClickRow(index, row){
		if (row) {
			//Bug #2859 彰銀格式 code 與 完修通知AO code交換  2017/11/17
			if (row.reportCode != '21') {
				$("#btnSend").linkbutton('enable');
			} else {
				$("#btnSend").linkbutton('disable');
			}
		}
		
	}
	$(function(){
		//查詢
		$("#btnQuery").click(function(){
			query(1,true);
		});
		//跳轉到新增畫面
		$("#btnAdd").click(function(){
			$("#message").text("");
			viewEditData('新增報表發送功能設定','<%=IAtomsConstants.ACTION_INIT_ADD%>', '','<%=IAtomsConstants.ACTION_SAVE%>','<%=IAtomsConstants.NO%>');
		});
		//將修改和刪除按鈕變為disable狀態
		$("#btnSend").linkbutton('disable')
	});
	//修改按鈕
	function update(sendFlag){
		var row = $('#dg').datagrid('getSelected');
		if (row == null) {
			$.messager.alert('提示', '請勾選要操作的記錄!', 'warning');
			return false;
		} else {
	       	$("#message").text("");
	       	viewEditData('修改報表發送功能設定','<%=IAtomsConstants.ACTION_INIT_EDIT%>', row.settingId,'<%=IAtomsConstants.ACTION_SAVE%>',sendFlag);
	    }
	}
	//刪除按鈕
	function deleteData(){
		//獲取被選中的列
		var row = $('#dg').datagrid('getSelected');
		//若無被選中的列時給出提示信息，否則就進行刪除操作
		if(row == null){
			$.messager.alert('提示信息','請選擇您要刪除的信息！','warning');
			return false;
		}else{
			deleteReportSetting();
		}
	}
	//重送按鈕
	function sendData(sendFlag){
		//獲取被選中的列
		var row = $('#dg').datagrid('getSelected');
		//若無被選中的列時給出提示信息，否則跳轉到修改頁面
		if(row == null){
			$.messager.alert('提示信息','請選擇需要重送的數據！','warning');
			return false;
		}else{
			viewEditData('重送報表發送功能設定','<%=IAtomsConstants.ACTION_INIT_SEND%>',row.settingId,'<%=IAtomsConstants.ACTION_SEND%>',sendFlag);
		}
	}
	/**
	 *新增、修改、重送操作
	 * title：頁面的頭部信息
	 * actionId：進行操作的actionId
	 * settingId：選中行的報表編號
	 * backActionId: 返回後台的actionId
	 */
	function viewEditData(title,actionId,settingId,backActionId,sendFlag) {
		var viewDlg = $('#dlg').dialog({    
		    title: title,    
		    width: 680,
		    height:470,
		    top:10,
		    closed: false,    
		    cache: false,
		    queryParams : {
		    	actionId : actionId,
		    	settingId : settingId,
		    },
		    href: "${contextPath}/reportSetting.do",
		    modal: true,
		    onLoad : function() {
				textBoxSetting("dlg");
				dateboxSetting("dlg");
            },
            onClose : function(){
            	$("div[data-list-required]").removeClass("div-tips").addClass("div-list").tooltip("destroy");
            },
		    buttons : [{
				text:'儲存',
				iconCls:'icon-ok',
				width :'90px',
				handler: function(){
					//提交表單時要驗證的欄位id
					var controls = null;
					//根據報表明細欄位的顯示與否決定驗證的欄位ID數組
					if($("#checkReportDetail").is(":hidden")){
						//報表明細欄位不顯示
						controls = [
							'<%=ReportSettingDTO.ATTRIBUTE.COMPANY_ID.getValue()%>',
							'<%=ReportSettingDTO.ATTRIBUTE.REPORT_CODE.getValue()%>',
							'<%=ReportSettingDTO.ATTRIBUTE.RECIPIENT.getValue()%>'];
					} else {
						//報表明細欄位顯示
						controls = [
							'<%=ReportSettingDTO.ATTRIBUTE.COMPANY_ID.getValue()%>',
							'<%=ReportSettingDTO.ATTRIBUTE.REPORT_CODE.getValue()%>',
							'checkReportDetail',
							'<%=ReportSettingDTO.ATTRIBUTE.RECIPIENT.getValue()%>'];
					}
					if (validateForm(controls) && $('#fm').form("validate")){
						// 調保存遮罩
						commonSaveLoading('dlg');
						var url="${contextPath}/reportSetting.do?actionId="+backActionId;
						var saves = $('#fm').form('getData');
						saves.companyId = $("#<%=ReportSettingDTO.ATTRIBUTE.COMPANY_ID.getValue()%>").combobox('getValue');
						saves.reportCode =  $("#reportCode").combobox('getValue');
						if ('<%=IAtomsConstants.ACTION_SEND%>' == backActionId 
								&& (saves.reportCode == '<%=IAtomsConstants.REPORT_NAME_EDC_FEE_FOR_JDW%>'
										|| saves.reportCode == '<%=IAtomsConstants.REPORT_TCB_SYB_NINETEEN%>')) {
							saves.createdDateString = $("#<%=ReportSettingDTO.ATTRIBUTE.CREATED_DATE_STRING.getValue()%>").textbox("getValue");
						}
						if(('<%=IAtomsConstants.REPORT_NAME_DAY_REPORT%>' == saves.reportCode) 
							|| ('<%=IAtomsConstants.REPORT_NAME_COMPLETE_OVERDUE_RATE_REPORT%>' == saves.reportCode)){ 
							 if (!checkedDivListValidate()) {
								 return false; 
							 }
						}
						$.ajax({
							url: url,
							data: saves,
							type:'post', 
							cache:false, 
							dataType:'json', 
							success:function(data){
								// 去除保存遮罩
								commonCancelSaveLoading('dlg');
								if (data.success) {
									$('#dlg').dialog('close');
									$("#message").text(data.msg);
									var pageIndex = getGridCurrentPagerIndex("dg");
									query(pageIndex,false);
								} else {
									$("#errorMsg").text(data.msg);
								}
							},
							error:function(){
								// 去除保存遮罩
								commonCancelSaveLoading('dlg');
								var msg;
								if (actionId == '<%=IAtomsConstants.ACTION_INIT_ADD%>') {
									msg = "新增失敗";
								} else if (actionId == '<%=IAtomsConstants.ACTION_INIT_EDIT%>'){
									msg = "修改失敗";
								} 
								$.messager.alert('提示', msg, 'error');							
							}
						});
			        }
		    	}
			},{
				text:'取消',
				width :'90px',
				iconCls:'icon-cancel',
				handler: function(){
					confirmCancel(function(){
						viewDlg.dialog('close');
					});
				}
			}]
		});    
	}
   	//查詢報表發送功能設定信息
    function query(pageIndex,isSaveOrEdit){
		var queryParam = $("#queryId").form("getData");
		var options = {
				url : "${contextPath}/reportSetting.do?actionId=<%=IAtomsConstants.ACTION_QUERY%>",
				queryParams :queryParam,
				pageNumber:pageIndex,
				onLoadSuccess:function(data){
					if (isSaveOrEdit) {
						$("#message").text("");
						$("#message").text(data.msg);
					}
					//合并列的field数组及对应前提条件filed（为空则直接内容合并）  
					 var arr =[{mergeFiled:"settingId",premiseFiled:"settingId"},
			                  {mergeFiled:"customerName",premiseFiled:"settingId"},
			                  {mergeFiled:"reportName",premiseFiled:"settingId"},
			                  {mergeFiled:"recipient",premiseFiled:"settingId"},
			                  {mergeFiled:"copy",premiseFiled:"settingId"},
			                  {mergeFiled:"rowNumber",premiseFiled:"settingId"},
							  {mergeFiled:"remark",premiseFiled:"settingId"}];
					mergeCells(arr,"dg"); 
					$("#btnSend").linkbutton('disable');
					isSaveOrEdit = true;
					// Bug #2810 重置datagrid大小
					$("#dg").datagrid('resize');
				},
				onLoadError : function() {
					$("#message").text("查詢失敗！請聯繫管理員");
				}
			}
		// 清空點選排序
		if(isSaveOrEdit){
			options.sortName = null;
		}
		openDlgGridQuery("dg", options);
    }
    
  	//刪除報表發送功能設定
	function deleteReportSetting() {
        $("#message").text("");
		var params = getSelectedParam("<%=IAtomsConstants.ACTION_DELETE%>");
		var url = '${contextPath}/reportSetting.do';
		var successFunction = function(data) {
			$("#message").text(data.msg);
			if (data.success) {
				//頁碼
				var pageIndex =getGridCurrentPagerIndex("dg");
				//獲取本頁的所有行
				var rows= $("#dg").datagrid('getRows'); 
				//本頁的所有行數
				var count = rows.length;
				var a = count - 1;
				//要刪除的行的SettingId
				var settingId = row.settingId;
				//依次將除本身以外的每行的SettingId於選中行的SettingId對比
				for(var i = a ; i < count ; i++){
					//若兩者不等頁碼就減一
					if(settingId == rows[i].settingId){
						pageIndex = pageIndex - 1; 
					}
				}
				query(pageIndex,false);
				$("#message").text(data.msg);
			} 
		};
		var errorFunction = function(){
			$.messager.progress('close');
			$.messager.alert('提示', "刪除失敗", 'error');	
		};
		commonDelete(params,url,successFunction,errorFunction);
	}
	/**
	 *Purpose:校驗完后返回後台
	 */
	function saveOrUpdate(backActionId,sendFlag,actionId){
		var url="${contextPath}/reportSetting.do?actionId="+backActionId;
		var saves = $('#fm').form('getData');
		saves.companyId = $("#<%=ReportSettingDTO.ATTRIBUTE.COMPANY_ID.getValue()%>").combobox('getValue');
		$.ajax({
			url: url,
			data: saves,
			type:'post', 
			cache:false, 
			dataType:'json', 
			success:function(data){
				if (data.success) {
					$('#dlg').dialog('close');
					$("#message").text(data.msg);
					var pageIndex = getGridCurrentPagerIndex("dg");
					query(pageIndex,false);
				} else {
					$("#errorMsg").text(data.msg);
				}
			},
			error:function(){
				var msg;
				if (actionId == '<%=IAtomsConstants.ACTION_INIT_ADD%>') {
					msg = "新增失敗";
				} else if (actionId == '<%=IAtomsConstants.ACTION_INIT_EDIT%>'){
					msg = "修改失敗";
				} 
				$.messager.alert('提示', msg, 'error');
			}
		});
	}
	/*
	* 编辑、删除时拿到主键值
	*/
	function getSelectedParam(actionId) {
		var row = $("#dg").datagrid('getSelected');
		var param ;
		if (row != null) {
			param = {
				actionId : actionId,	
				settingId : row.settingId,
			};
			return param;
		} else {
			$("#message").text('請勾選要操作的記錄!');
			return null;
		}
	}
	//Bug #2774 將英文分號改為中文分號，用來換行 2017/11/07
	function formatMail(val, row, index){
		if(val!=null && val.indexOf(';')>=0){
			val = val.replaceAll(';',"；");
		}
		return val;
	}
</script>
</body>
</html>