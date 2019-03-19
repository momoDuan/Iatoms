<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ include file="/jsp/common/easyui-common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.LogFileDownloadFormDTO" %>
<%
	//初始化加載頁面數據
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	LogFileDownloadFormDTO formDTO = null;
	if (ctx != null) {
		formDTO = (LogFileDownloadFormDTO) ctx.getResponseResult();
	} else {
		formDTO = new LogFileDownloadFormDTO();
	}
%>
<html>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>錯誤記錄檔下載</title>
</head>
<body>
	<div style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
		<div class="easyui-panel" title="錯誤記錄檔下載" style="width: 98%; height: auto">
        	<div class="red" id="message" data-options="multiline:true"></div>
			<form id="queryForm"  method="post">
				<table cellpadding="2" >
					<tr>
						<td>檔案類型:<span class="red">*</span></td>
						<td>
							<input type="radio" name=<%=LogFileDownloadFormDTO.QUERY_LOG_FILE_TYPE %>
								id="apLog" value = "ap" checked="checked"/>AP Log
							<input type="radio" name=<%=LogFileDownloadFormDTO.QUERY_LOG_FILE_TYPE %>
								id="tomcatLog" value = "tomcat"/>Tomcat Log
						</td>
					</tr>
					<tr>
						<td>下載檔名:<span class="red">*</span></td>
						<td>
							<input id="<%=LogFileDownloadFormDTO.QUERY_LOG_FILE_NAME %>" name="<%=LogFileDownloadFormDTO.QUERY_LOG_FILE_NAME %>" 
			        			class="easyui-textbox" panelheight="auto" style="width: 300px" 
			        			required="true" missingMessage="請輸入下載檔名" maxlength="30" validType="maxLength[30]"/>
						</td>
					</tr>
					<tr>
						<td></td>
						<td align="right">
							<a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-redo" id="btnDownload" onclick="downloadFile();">下載</a>
						</td>
					</tr>
				</table>
    			<div id="dlg" modal ="true"></div>
			</form>
		</div>
	</div>
	<script  type="text/javascript">
	function downloadFile() {
		$("#message").text("");
		var fileType = $("#queryForm").form("getData")['queryLogFileType'];
		var fileName = $("#queryForm").form("getData")['queryLogFileName'];
		var queryParam = '&queryLogFileType=' + fileType　 + '&queryLogFileName=' + fileName;
		
		if (fileName != '' && fileName != null) {
			if ($('#queryForm').form('validate')) {
				//調保存遮罩
				commonSaveLoading('dlg');
				ajaxService.checkLogFileExist(fileType, fileName, function(data) {
					if (data) {
						//去除保存遮罩
		             	commonCancelSaveLoading('dlg');
						createSubmitForm("${contextPath}/logFileDownload.do", "actionId=<%=IAtomsConstants.ACTION_DOWNLOAD%>" + queryParam, "post");
					} else {
						//去除保存遮罩
		             	commonCancelSaveLoading('dlg');
						$("#message").text("文件不存在");
					}
				});
			}
		} else {
			$("#message").text("請輸入下載檔名");
		}
	}
	</script>
</body>
</html>