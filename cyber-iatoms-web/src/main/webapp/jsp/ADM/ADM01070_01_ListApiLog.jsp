<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ include file="/jsp/common/easyui-common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.ApiLogFormDTO" %>
<%
	//初始化加載頁面數據
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	ApiLogFormDTO formDTO = null;
	if (ctx != null) {
		formDTO = (ApiLogFormDTO) ctx.getResponseResult();
	} else {
		formDTO = new ApiLogFormDTO();
	}
%>
<html>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>電文記錄查詢</title>
	<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
</head>
<body>
	<div style="width: 100%;overflow-x:hidden; height: auto; padding: 1px; overflow-y: auto" class="setting-panel-height topSoller">
		<table id="apiDataGrid" class="easyui-datagrid" title="電文記錄查詢">
			<thead>
				<tr>
					<th data-options="field:'id', hidden:true"></th>
					<th data-options="field:'createdDate', formatter:transToTimeStamp, width:200, sortable:true, halign:'center', align:'center'">日期</th>
                    <th data-options="field:'functionName', width:150, halign:'center', align:'left'">功能</th>
                    <th data-options="field:'clientCode', width:100, halign:'center', align:'left'">Client_Code</th>
                    <th data-options="field:'msgType', width:100, halign:'center', align:'left'">電文類型</th>
                    <th data-options="field:'failReasonDesc', formatter:wrapFormatter, width:250, halign:'center', align:'left'">回覆訊息</th>
                    <th data-options="field:'result', width:100, halign:'center', align:'left'">回覆結果</th>
                    <th data-options="field:'content', formatter:contentFormatter, width:100, halign:'center', align:'center'">交易內容</th>
                    <th data-options="field:'message', hidden:true"></th>
                    <th data-options="field:'masterId', width:150, halign:'center', align:'left'">交易主檔ID</th>
                    <th data-options="field:'detailId', width:200, halign:'center', align:'left'">交易明細檔ID</th>
                    <th data-options="field:'createdByName', width:150, halign:'center', align:'left'">建檔人員</th>
				</tr>
			</thead>
		</table>
        <div id="toolbar" style="padding: 2px 5px;">
        	<form action="apiLog.do" id="queryForm" name="queryForm" method="post">
        		Client_Code:
        		<input id="<%=ApiLogFormDTO.QUERY_CLIENT_CODE %>" name="<%=ApiLogFormDTO.QUERY_CLIENT_CODE %>" 
        			class="easyui-textbox" panelheight="auto" style="width: 150px" 
        			maxlength="20" validType="maxLength[20]"/>
        		日期:
				<input class="easyui-datetimebox" id="<%=ApiLogFormDTO.QUERY_START_DATE %>" name="<%=ApiLogFormDTO.QUERY_START_DATE %>" maxlength="16"
							data-options="formatter:formatToTimeStampIgnoreSecond, validType:['maxLength[16]','dateTime']" required="true"
							missingMessage="請輸入日期起" showSeconds="false"/> ～ 
				<input class="easyui-datetimebox" id="<%=ApiLogFormDTO.QUERY_END_DATE %>" name="<%=ApiLogFormDTO.QUERY_END_DATE %>" maxlength="16" required="true"
							data-options="formatter:formatToTimeStampIgnoreSecond, validType:['maxLength[16]','dateTime','compareDateStartEnd[\'#queryStartDate\',\'日期起不可大於日期迄\']']"
							missingMessage="請輸入日期迄" showSeconds="false"/>	
				<a href="javascript:void(0)" class="easyui-linkbutton" id="btnQuery" iconcls="icon-search">查詢</a>
        		<div class="red" id="message" data-options="multiline:true"></div>
        	</form>
        </div>
	</div>
    <div id="contentDlg" modal ="true"></div>
    <script type="text/javascript">
	    //初始化datagrid
	    $("#apiDataGrid").datagrid({
	        width:'100%', height:'auto',
	        nowrap:false,
	        pagination:true,
	        singleSelect:true,
	        fitColumn:false,
	        pageNumber:0,
	        pageList:[15, 30, 50, 100],
	        pageSize:15,
	        toolbar:'#toolbar',
	        rownumbers:true,
	        iconCls:'icon-edit'
	    });
	    
	    $(function() {
	    	$('#btnQuery').click(function() {
				queryData(1,true);
			});
	    });
	    
	    /**
		 * 查詢數據
		 */
	    function queryData(pageNum,hidden) {
			//清空選中
			$("#apiDataGrid").datagrid("clearSelections");
	    	
			var queryParam = $("#queryForm").form("getData");
			var options = {
					url:"${contextPath}/<%=IAtomsConstants.API_LOG%>?actionId=<%=IAtomsConstants.ACTION_QUERY%>",
					queryParams:queryParam,
					pageNumber:pageNum,
					autoRowHeight:true,
					onLoadSuccess:function(data){
						if (hidden) {
							$("#message").text("");
							if (data.total == 0) {
								// 提示查無數據
								$("#message").text(data.msg);
							}
						}
						hidden = true
					},
					onLoadError : function() {
						$("#message").text("查詢失敗！請聯繫管理員");
					}
			}
			// 清空點選排序(注：若初始化直接使用datagrid的sortName進行排序的請再次賦予初值)
			if(hidden){
				options.sortName = null;
			}
			if ($("#queryForm").form("validate")) {
				openDlgGridQuery("apiDataGrid", options);
			}
	    }
	    
	    /**
		 * 內容按鈕 formatter函數
		 */
		function contentFormatter(value, row, index) {
			return '<a href="javascript:void(0)" onclick="viewContent(' + index + ');">內容</a>';
	    }
	    
	    /**
	     * 電文內容
	     */
	    function viewContent(index) {
	    	//取得選取row
			var temp = $('#apiDataGrid').datagrid('selectRow', index);
			var row = $('#apiDataGrid').datagrid('getSelected');
			
			if (row.message != '' && row.message != null) {
				var msg = row.message;
				var viewDlg = $('#contentDlg').dialog({
					title : '電文內容',    
				    width : '700px',
				    height : '600px',
				    top : 10,
				    closed : false,    
				    cache : false,
				    modal : true,
				    resizable : true,
				    buttons : [{
				    	text:'關閉',
						iconCls:'icon-cancel',
						width :'90px',
						handler: function() {
							viewDlg.dialog('close');
						}
				    }]
				});
				msg = msg.replace(/"/g, '');
				msg = msg.replace(/\\/g, '');
				msg = msg.replace(/,/g, '<br>');
				$('#contentDlg').html(msg);
			} else {
				$("#message").text("無內容");
			}
	    }
    </script>
</body>
</html>