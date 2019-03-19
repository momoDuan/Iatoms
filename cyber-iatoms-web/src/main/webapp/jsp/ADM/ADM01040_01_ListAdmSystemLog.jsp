<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.SystemLogFormDTO"%>
<%@include file="/jsp/common/common.jsp"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	SystemLogFormDTO formDTO = null;
	String ucNo = "";
	String currentDate = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), "yyyy/MM/dd");
	if (ctx != null) {
		formDTO = (SystemLogFormDTO) ctx.getResponseResult();
		if(formDTO != null){
			ucNo = formDTO.getUseCaseNo();
		}
	}
%>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="ucNo" value="<%=ucNo%>" scope="page"></c:set>
<c:set var="currentDate" value="<%=currentDate%>" scope="page"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系統log查詢</title>
<link rel="stylesheet" type="text/css" href="${contextPath}/assets/css/multi-select.css">
<%@include file="/jsp/common/easyui-common.jsp"%>
<script type="text/javascript" src="${contextPath}/assets/js/jquery.multi-select.js"></script>
<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
<script type="text/javascript">
	//查詢log
	function query(currentPage){
		var queryForm = $("#queryForm");
		//驗證
		if(!queryForm.form('validate')){
			return false;
		}
		$("#msg").text("");
		//new 一個 datagrid對象
		var options = {
			url : "${contextPath}/systemLog.do",
			queryParams : {
					actionId 	   	: "<%=IAtomsConstants.ACTION_QUERY%>",
					queryAccount 	: $("#queryAccount").textbox('getValue'),
					queryFromDate 	: $("#queryFromDate").textbox('getValue'),
					queryToDate 	: $("#queryToDate").textbox('getValue'),
			},
			autoRowHeight:true,
			pageNumber : currentPage,
			onLoadError : function() {
				$.messager.alert('提示','查詢系統參數資料出錯','error');
			},
			onLoadSuccess : function(data){
				if(data.total == 0){
					$("#msg").text(data.msg);
					$('#btnExport').attr("onclick","return false;");
					$('#btnExport').attr("style","color:gray");
					$("#queryForm").find("#queryFlag").val("EMPTY");
				}else{
					$('#btnExport').attr("style","color:blue");
					$('#btnExport').attr("onclick","doExport(this)");
					$("#queryForm").find("#queryFlag").val("Y");
				}
				
				$('a[name=lbtSearch]').linkbutton({ plain: true, iconCls: 'icon-search' });
			}
		};
		// 清空點選排序(注：若初始化直接使用datagrid的sortName进行排序的请再次赋初值)
		options.sortName = null;
		openDlgGridQuery("dataGrid", options);
	}
	//匯出
	function doExport(obj){
		var queryFlag = $("#queryForm").find("#queryFlag");
		if(queryFlag.val() == "N"){
			$('#btnExport').attr("style","color:gray");
			return false;
		}else if(queryFlag.val() == "EMPTY"){
			$('#btnExport').attr("style","color:gray");
			return false;
		}else {
			var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			$.blockUI(blockStyle);
			
			actionClicked( document.forms["queryForm"],
				'${ucNo}',
				'',
				'<%=IAtomsConstants.ACTION_EXPORT%>');
			
			ajaxService.getExportFlag('${ucNo}',function(data){
				$.unblockUI();
			});
		}	
	}
</script>
</head>
<body>
<div style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
	<!-- <div region="center" style="width: auto; height: auto; padding: 1px; overflow-y: hidden"> -->
		<table id="dataGrid" class="easyui-datagrid" title="系統log查詢" style="width: 98%; height: auto"
					data-options="
		           		rownumbers:true,
		                pagination:true,
		                pageNumber:0,
		                pageList:[15,30,50,100],
						pageSize:15,
						nowrap:false,
		                fitColumns:false,
						iconCls: 'icon-edit',
						singleSelect: true,
						toolbar:'#toolbar'">
			<thead>
				<tr>
					<th data-options="field:'userName',width:200,align:'left',halign:'center',sortable:true">登入帳號</th>
					<th data-options="field:'operationTime',width:190,align:'center',halign:'center',sortable:true,formatter:formatToTimeStamp">日期時間</th>
					<th data-options="field:'ip',width:120,align:'left',halign:'center',sortable:true">User端IP</th>
					<th data-options="field:'logCategreName',width:120,align:'left',halign:'center',sortable:true">紀錄類別</th>
					<th data-options="field:'functionName',width:140,align:'left',halign:'center',sortable:true">程式名稱</th>
					<th data-options="field:'content',width:140,align:'center',halign:'center',formatter:imgIcon">執行内容</th>
					<th data-options="field:'result',width:200,align:'left',halign:'center',sortable:true">執行結果</th>
				</tr>
			</thead>
		</table> 
		<div id="toolbar" style="padding: 2px 5px;">
			<form action="systemLog.do" id="queryForm" name="queryForm" method="post">
				<input type="hidden" id="actionId" name="actionId" />
				<input type="hidden" id="serviceId" name="serviceId" />
				<input type="hidden" id="useCaseNo" name="useCaseNo" />
				<input type="hidden" name="queryFlag" id="queryFlag" value="N"/>
				帳號:
				<input class="easyui-textbox" panelheight="auto" style="width: 150px" id="queryAccount" name="queryAccount" maxlength="20" validType="maxLength[20]"/>
				日期(YYYY/MM/DD):<span class="red">*</span>
				<input class="easyui-datebox" id="queryFromDate" name="queryFromDate" maxlength="10"
							data-options="formatter:formaterTimeStampToyyyyMMDD,validType:['maxLength[10]','date']" 
							required value="${currentDate}"
							missingMessage="請輸入日期起"/> ～ 
				<input class="easyui-datebox" id="queryToDate" name="queryToDate" maxlength="10"
							data-options="formatter:formaterTimeStampToyyyyMMDD,validType:['maxLength[10]','date','compareDateStartEnd[\'#queryFromDate\',\'日期起不可大於日期迄\']']" 
							required value="${currentDate}"
							missingMessage="請輸入日期迄"/>	
				<a class="easyui-linkbutton" id="btnQuery" href="#" iconcls="icon-search" onclick="query(1)">查詢</a>
			</form>
			<div><span id="msg" class="red">${msg }</span></div>
			<div style="text-align: right"><a href="javascript:void(0)" id="btnExport" style="width: 150px" onclick="doExport(this)">匯出</a></div>
			<div id="imgDialog" fit="true" ></div>
			<div></div>
		</div>
	</div> 
<script type="text/javascript">
	/*
	* 系统log查询执行内容
	*/
	function imgIcon(val,row){
		var icon = "";
		if(row != null){
			if((row.functionId == 'ADM01010' || row.functionId == 'ADM01030') && row.logCategre != 'CREATE'){ 
			/* 	icon = "<img src='${contextPath}/assets/jquery-easyui-1.4.3/themes/icons/search.png' onclick='openLogDialog("+row.logId+");' />";  */
				icon = "<a href='javascript:void(0)' onclick='openLogDialog("+row.logId+");'  name='lbtSearch'></a>";
			}
		}
		return icon;
	}
	/*
	* 打開执行内容對話框
	*/
	function openLogDialog(logId){
		var viewDlg = $('#imgDialog').dialog({
		    title: '系統log查詢-執行內容',    
		  	width: 840,
            height: 620,
		    closed: false,    
		    cache: false,
		    resizable:true,
		    method:'post', 
		    queryParams :{actionId:'openLogDialog',logId : logId},
			href: "${contextPath}/systemLog.do",
		    modal: true,
		    buttons : [{
				text:'關閉',
				width:'88',
				iconCls:'icon-cancel',
				handler: function(){
					viewDlg.dialog('close');
				}
			}]
		}); 
	}
</script>
</body>
</html>