<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ include file="/jsp/common/easyui-common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.EdcLabelFormDTO" %>
<%
//初始化加載頁面數據
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	EdcLabelFormDTO formDTO = null;
	if (ctx != null) {
		formDTO = (EdcLabelFormDTO) ctx.getResponseResult();
	} else {
		formDTO = new EdcLabelFormDTO();
	}
	//檔案上傳最大值
	String uploadFileSize = formDTO.getUploadFileSize();

	String currentDate = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), "yyyy/MM/dd HH:mm");
%>
<html>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="uploadFileSize" value="<%=uploadFileSize%>" scope="page"></c:set>
<c:set var="currentDate" value="<%=currentDate%>" scope="page"></c:set>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>刷卡機標籤管理</title>
	<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
	<script type="text/javascript" src="${contextPath}/js/fineuploader-5.0.2.js"></script>
	<link rel="stylesheet" href="${contextPath}/css/fineuploader-5.0.2.css"/>
</head>
<body>
	<div style="width: 100%;overflow-x:hidden; height: auto; padding: 1px; overflow-y: auto" class="setting-panel-height topSoller">
		<table id="edcLabelGrid" class="easyui-datagrid" title="標籤列印作業">
			<thead>
				<tr>
					<th data-options="field:'check', checkbox:true"></th>
                    <th data-options="field:'seqNo', width:100, sortable:true, halign:'center', align:'left'">序號</th>
                    <th data-options="field:'merchantCode', width:150, sortable:true, halign:'center', align:'left'">特店代號</th>
                    <th data-options="field:'dtid', width:100, halign:'center', align:'left'">DTID</th>
                    <th data-options="field:'merchantType', width:100, halign:'center', align:'left'">館別</th>
                    <th data-options="field:'relation', width:100, halign:'center', align:'left'">幹系</th>
                    <th data-options="field:'status', width:100, halign:'center', align:'left'">型態</th>
                    <th data-options="field:'ip', width:150, halign:'center', align:'center'">IP</th>
                    <th data-options="field:'updatedByName', width:150, halign:'center', align:'left'">匯入人員</th>
					<th data-options="field:'updatedDate', formatter:transToTimeStamp, width:200, sortable:true, halign:'center', align:'center'">匯入日期</th>
				</tr>
			</thead>
		</table>
        <div id="toolbar" style="padding: 2px 5px;">
        	<form action="edcLabelManage.do" id="queryForm" name="queryForm" method="post">
        		<input type="hidden" id="compositeKeys" name="compositeKeys" />
				<input type="hidden" id="actionId" name="actionId" />
				<input type="hidden" id="serviceId" name="serviceId" />
				<input type="hidden" id="useCaseNo" name="useCaseNo" />
        		特店代號:
        		<input id="<%=EdcLabelFormDTO.QUERY_MERCHANT_CODE %>" name="<%=EdcLabelFormDTO.QUERY_MERCHANT_CODE %>" 
        			class="easyui-textbox" panelheight="auto" style="width: 150px" 
        			maxlength="20" validType="maxLength[20]"/>
        		DTID:
        		<input id="<%=EdcLabelFormDTO.QUERY_DTID %>" name="<%=EdcLabelFormDTO.QUERY_DTID %>" 
        			class="easyui-textbox" panelheight="auto" style="width: 150px" 
        			maxlength="10" validType="maxLength[10]"/>
        		日期:
				<input class="easyui-datetimebox" id="<%=EdcLabelFormDTO.QUERY_START_DATE %>" name="<%=EdcLabelFormDTO.QUERY_START_DATE %>" maxlength="16"
							data-options="formatter:formatToTimeStampIgnoreSecond, validType:['maxLength[16]','dateTime']" required="true"
							missingMessage="請輸入日期起" showSeconds="false"/> ～ 
				<input class="easyui-datetimebox" id="<%=EdcLabelFormDTO.QUERY_END_DATE %>" name="<%=EdcLabelFormDTO.QUERY_END_DATE %>" maxlength="16" required="true"
							data-options="formatter:formatToTimeStampIgnoreSecond, validType:['maxLength[16]','dateTime','compareDateStartEnd[\'#queryStartDate\',\'日期起不可大於日期迄\']']"
							missingMessage="請輸入日期迄" showSeconds="false"/>	
				<a href="javascript:void(0)" class="easyui-linkbutton" id="btnQuery" iconcls="icon-search">查詢</a>
				<cafe:fileuploadTag
					id="uploadFiled"
					name="uploadFiled"
					uploadUrl="${contextPath}/edcLabelManage.do" 
					uploadParams="{actionId:'upload'}"
					acceptFiles=".xls, .xlsx"
					allowedExtensions="'xls', 'xlsx'" 
					sizeLimit = "${uploadFileSize }"
					showFileList="false"
					showName="標籤匯入"
					whetherImport="true"
					messageAlert="false"
					whetherDelete="false"
					multiple="false"
					whetherDownLoad="false"
					showUnderline = "true"
					javaScript="onComplete:showMessage,onError:function(id, name, reason, maybeXhrOrXdr){
								$('#message').text(reason);
					}">
				</cafe:fileuploadTag>
				<!-- <a href="javascript:void(0)" class="easyui-linkbutton" id="btnImport" iconcls="icon-redo">標籤匯入</a> -->
		        <a href="#" id="btnTemplateDownload" style="width: 150px">匯入格式下載</a>
        		<div class="red" id="message" data-options="multiline:true"></div>
        	</form>
	        <a href="javascript:void(0)" id="btnPrintEdcLabel" class="easyui-linkbutton c6" onclick="printEdcLabel()">列印標籤</a>
	        <a href="javascript:void(0)" id="btnDeleted" class="easyui-linkbutton c6" onclick="doDeleted()">刪除</a>
        </div>
	</div>
	<script type="text/javascript">
    //初始化datagrid
    $("#edcLabelGrid").datagrid({
        width:'100%', height:'auto',
        nowrap:false,
        pagination:true,
        singleSelect:false,
        fitColumn:false,
        pageNumber:0,
        pageList:[15, 30, 50, 100],
        pageSize:15,
        toolbar:'#toolbar',
        rownumbers:true,
        iconCls:'icon-edit',
		selectOnCheck:true,
		checkOnSelect:true
    });
    
    $(function() {
    	$('#btnQuery').click(function() {
			queryData(1, true);
		});
		$('#btnTemplateDownload').attr("style","color:blue;");
		// 匯入格式下載
		$("#btnTemplateDownload").click(function(){
			downLoad("");
		});
    });
    
    Date.prototype.addDays = function(days) {
		this.setDate(this.getDate() + days);
		return this;
   	}

    /**
	 * 查詢數據
	 */
    function queryData(pageNum,hidden) {
		var queryParam = $("#queryForm").form("getData");
		var options = {
				url:"${contextPath}/<%=IAtomsConstants.EDC_LABEL_MANAGE%>?actionId=<%=IAtomsConstants.ACTION_QUERY%>",
				queryParams:queryParam,
				method:'post',
				pageNumber:pageNum,
				autoRowHeight:true,
				onLoadSuccess:function(data){
					$(this).datagrid("fixRownumber","edcLabelGrid");
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
			openDlgGridQuery("edcLabelGrid", options);
		}
    }
    
    /**
     * 啟用按鈕
     */
    function enableBtn() {
		$("#btnPrintEdcLabel").linkbutton('enable');
		$("#btnDeleted").linkbutton('enable');
    }

    /**
     * 禁用按鈕
     */
    function disableBtn() {
    	$("#btnPrintEdcLabel").linkbutton('disable');
		$("#btnDeleted").linkbutton('disable');
    }
    
	/**
	 * 匯入格式下載
	 */
	function downLoad(fileName){
		$("#message").text("");
		// 匯入格式下載
		createSubmitForm("${contextPath}/edcLabelManage.do","actionId=<%=IAtomsConstants.ACTION_DOWNLOAD%>","post");
	}
	
	/**
	 * 匯入返回處理 fileName：名稱  response：響應
	 */
	function showMessage(id, fileName, response, maybeXhrOrXdr) {
		$("#message").text("");
		//保存數據時，在上傳文件前驗證SESSION是否過期
		if (maybeXhrOrXdr) {
			if (!sessionTimeOut(maybeXhrOrXdr)) {
				return false;
			}
		}
		if (response.success) {
			var pageIndex = getGridCurrentPagerIndex("edcLabelGrid");
			$("#queryStartDate").textbox("setValue", formaterTimeStampToyyyyMMDD(new Date()) + " 00:00");
			$("#queryEndDate").textbox("setValue", formaterTimeStampToyyyyMMDD(new Date().addDays(1)) + " 00:00");
			queryData(pageIndex, false);
			$("#message").text(response.msg);
		} else {
			if(response.msg && response.errorFileName == null){
				$("#message").text(response.msg);
			} else {
				if (response.errorFileName != "" || response.errorFileName){
					$.messager.confirm('確認對話框','匯入失敗，是否下載匯入錯誤檔？', function(confirm) {
						if (confirm) {
							createSubmitForm("${contextPath}/edcLabelManage.do", "actionId=<%=IAtomsConstants.ACTION_DOWNLOAD_ERROR_FILE%>&errorFileName=" + response.errorFileName + "&tempFilePath= " + response.tempFilePath,"post");
						}
					});
				}
			}
		}
	}
	
	/**
	 * 刪除標籤
	 */
	function doDeleted() {
		var dataArray = $('#edcLabelGrid').datagrid('getSelections')
		if (dataArray.length > 0) {
			$('#message').empty();
			var url = '${contextPath}/<%=IAtomsConstants.EDC_LABEL_MANAGE%>'
			var params = null;
			var compositeKeys = '';
			for(var i = 0; i < dataArray.length; i++) {
				if (i == dataArray.length - 1) {
					compositeKeys = compositeKeys + dataArray[i].merchantCode + ',' + dataArray[i].dtid;
				} else {
					compositeKeys = compositeKeys + dataArray[i].merchantCode + ',' + dataArray[i].dtid + ';';
				}
			}
			console.log(compositeKeys);
	    	params = {
    			actionId : '<%=IAtomsConstants.ACTION_DELETE%>',
    			compositeKeys : compositeKeys
    		};
	    	
	    	var successFunction = function(data) {
				if (data.success) {
					var pageIndex = calDeletePagerIndex("edcLabelGrid");
					queryData(pageIndex, false);
				} 
				$('#message').text(data.msg);
			};

			var errorFunction = function() {
				$('#message').text('刪除失敗');
			};
			commonDelete(params,url,successFunction,errorFunction);
		} else {
			$('#message').text('請先選取資料');
		}
	}
	
	/**
	 * 列印標籤
	 */
	function printEdcLabel() {
		var dataArray = $('#edcLabelGrid').datagrid('getSelections')
		var compositeKeys = '';
		if (dataArray.length > 0) {
			for(var i = 0; i < dataArray.length; i++) {
				if (i == dataArray.length - 1) {
					compositeKeys = compositeKeys + dataArray[i].merchantCode + ',' + dataArray[i].dtid;
				} else {
					compositeKeys = compositeKeys + dataArray[i].merchantCode + ',' + dataArray[i].dtid + ';';
				}
			}
			$('#compositeKeys').val(compositeKeys);
			var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			$.blockUI(blockStyle);
			
			actionClicked( document.forms["queryForm"],
					'<%=IAtomsConstants.UC_NO_DMM_03120 %>',
					'',
					'<%=IAtomsConstants.ACTION_PRINT_EDC_LABEL%>');
			
			ajaxService.getExportFlag('<%=IAtomsConstants.UC_NO_DMM_03120 %>', function(data){
				$.unblockUI();
			});
		} else {
			$('#message').text('請先選取資料');
		}
	}
	</script>
</body>
</html>