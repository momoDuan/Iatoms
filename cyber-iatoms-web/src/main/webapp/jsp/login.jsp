<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/common/common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode"%>
<html>
<head>
    <%@include file="/jsp/common/meta.jsp"%>
	<title>Welcome To iATOMS</title>
    <link rel="stylesheet" type="text/css" href="assets/jquery-easyui-1.4.3/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="assets/jquery-easyui-1.4.3/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="assets/css/style-easyui.css">
    <script type="text/javascript" src="assets/jquery-easyui-1.4.3/jquery.min.js"></script>
    <script type="text/javascript" src="assets/jquery-easyui-1.4.3/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${contextPath}/assets/jquery-easyui-1.4.3/locale/easyui-lang-zh_TW.js"></script>
    <script type="text/javascript" src="assets/plugins/jquery.backstretch.min.js"></script>
    <script type="text/javascript" src="${contextPath}/js/easyui-extend.js"></script>
    <script type="text/javascript" src="${contextPath}/assets/plugins/jquery.blockui.js"></script>
    <script type="text/javascript" src="${contextPath}/js/jquery.client.js"></script>
    <link rel="shortcut icon" href="assets/img/cyber.ico" type="image/gif" />
    <script type="text/javascript" language="JavaScript" defer="defer">
	//<!--登入失敗彈出登入失敗錯誤訊息 -->
	function init(){
		var status = '${message.status}';
		var code = '${message.code}';
		if (status =='<%=Message.STATUS.FAILURE%>'){
			alertErrorCommon("<spring:message code='${message.code}' arguments='${message.argumentList}'/>", function(){
				$('#userCode').textbox('textbox').focus();
			});
		} else if (status =='<%=Message.STATUS.SUCCESS%>') {
			//錯誤處理, 提示
			var msgDesc = "<spring:message code='${message.code}' arguments='${message.argumentList}'/>";
			if (code == '<%=IAtomsMessageCode.NEW_ACCOUNT_PWD_CHANGE%>' || code == '<%=IAtomsMessageCode.RESET_PWD_NEED_EDIT%>') {
				//非Cyber員工，員工且為首次登入
				alertPromptCommon(msgDesc, false, function(){
					//顯示密碼彈出框
					changePwd(true);
				});
			} else if (code == '<%=IAtomsMessageCode.PWD_DUE_WARING%>') {
				//非Cyber員工若密碼即將過期(【密碼原則設定-密碼到期提示天數】) ，提示使用者「您的密碼將在XX天後到期，是否進行變更？(密碼過期後，須請管理員重新設定)」
				$.messager.confirm({
					title:'確認對話框',
					msg:msgDesc,
					closable:false,
					fn:function(b){
							if (b) {
								//修改密碼, 顯示修改密碼彈出框
								changePwd(false);
							} else {
								//暫不修改, 進入index畫面
								var loginform = $("#loginform");
								$("#<%=IAtomsLogonUser.FIELD_USER_CODE%>").textbox("setValue",'${logonUser.userCode}');
								$("#logUserPwd").textbox("setValue",'${logonUser.logUserPwd}');
								$("#passwordIgnoreFlag").val('Y');
								loginform.get(0).submit();
								//window.location.href="${contextPath}/logon.do";
							}
						}
				}); 
			} 
		}
		return;
	}
	//修改密碼
	//first:是否是第一次修改密碼
	function changePwd (first) {
		var viewDlg = $('#dlg').dialog({
		    title: '修改密碼',    
		    width: 380,
		    height:280,
		    minimizable:false,
		    maximizable:false,		    
		    closed: false,    
		    cache: false,
		    queryParams : {
		    },
		   href: "${contextPath}/changePassword.do",
		    onLoad:function(){
		    	textBoxSetting("dlg"); 
		    	 $("#openDialog").val("Y");
		    },
		    onClose: function () {
		    	$("#loginform").attr("action","${contextPath}/logoff.do").get(0).submit();
		    }  
		}).dialog("center");
	} 
	function pwdEidtSuccess(saveParam){
		var loginform = $("#loginform");
		$("#<%=IAtomsLogonUser.FIELD_USER_CODE%>").textbox("setValue",'${logonUser.userCode}');
		$("#logUserPwd").textbox("setValue",saveParam.newPassword);
		loginform.get(0).submit();
		//window.location.href="${contextPath}/logon.do";				
	}
	//<!--登錄-->
	function login(){
		var loginform = $("#loginform");
		var validate = loginform.form('enableValidation').form('validate');
		if (validate) {
			var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			$.blockUI(blockStyle);
			loginform.get(0).submit();
		}
	}
	//<!--关闭视窗讯息-->
	window.onbeforeunload = myunload;
	function myunload(event)
	{
	    if(event.clientX > document.body.clientWidth && event.clientY < 0 || event.altKey)
	    {
	        window.event.returnValue = "確認離開";//關閉時,彈出視窗的訊息
	    }
	}
	//按下回车键登入
	$(function() {
	//	$('#userCode').textbox().next('span').find('input').focus();
		$('#userCode').textbox('textbox').focus();  
		//Bug #2137
	//	$("#loginform").form('enableValidation').form('validate');
		$("#loginform input").keypress(function(event){  
			if (event.keyCode == 13){
				login();
			}
		});
		
		// client信息
		$('#operatingSystem').val('作業系統:' + $.client.os);
		$('#browser').val('瀏覽器:' + $.client.browser);
		$('#version').val('瀏覽器版本:' + $.client.browser + " " + $.client.version);
		$('#showWidth').val('視窗寬度:' + $(window).width() + 'px');
		$('#showHeight').val('視窗高度:' + $(window).height() + 'px');
	});
	
	</script>
</head>
<body class="easyui-layout" onload="init();">
    <div class="wrapper">
		<form name="loginform" id="loginform" autocomplete ="off" 
			class="form-vertical no-padding no-margin easyui-form" ACTION="${contextPath}/logon.do" method="POST">
			<div style="margin:150px 770px" >
				<div class="easyui-panel" title="iATOMS Portal" style="width: 400px; padding: 30px 70px 20px 70px">
					<div style="margin-bottom: 10px">
						<input id="<%=IAtomsLogonUser.FIELD_USER_CODE%>" 
							name="<%=IAtomsLogonUser.FIELD_USER_CODE%>"
							setting="hidden"
				            class="easyui-textbox" value="houhou"
				            style="width: 100%; height: 40px; padding: 12px" type="text" 
				            required="true" missingMessage="請輸入帳號" maxlength="20"
				            data-options="prompt:'Username',validType:['maxLength[20]'],iconCls:'icon-man',iconWidth:38">
					</div>
					<div style="margin-bottom: 10px">
						<input id="logUserPwd" 
							name="logUserPwd" 
				            class="easyui-textbox" 
				            setting="hidden"
				            type="password" value="1qa2ws"
				            style="width: 100%; height: 40px; padding: 12px"
				            required="true" missingMessage="請輸入密碼" maxlength="20"
				            data-options="prompt:'Password',validType:['maxLength[20]'],iconCls:'icon-lock',iconWidth:38">
					</div> 
					<div>
						<input type="hidden" id="passwordIgnoreFlag" name="passwordIgnoreFlag"/>
						<a id="logon"  class="easyui-linkbutton" data-options="iconCls:'icon-ok'" 
							onclick="login()" style="padding: 5px 0px; width: 100%;">
							<span>登入系統</span>
						</a>
						<!-- client信息隱藏域 -->
						<input type="hidden" id="operatingSystem" name="operatingSystem"/>
						<input type="hidden" id="browser" name="browser"/>
						<input type="hidden" id="version" name="version"/>
						<input type="hidden" id="showWidth" name="showWidth"/>
						<input type="hidden" id="showHeight" name="showHeight"/>
					</div>
				</div>
			</div>	    	
		</form>
	</div>
	<div id="dlg"></div>
	<div data-options="fit:false,region:'south'" style="overflow: hidden; height: 35px; line-height: 35px; width: 100%;background-color:RGB(22,81,111);color: white; font-size: 15px">
			●建議瀏覽器版本:IE11或Chrome 51.0 <span style="margin-left: 50px">●最佳解析度1366X768 </span>
	    	<!-- ●建議瀏覽器版本:IE9或Chrome16或Safari5或FireFox10以上 <span style="margin-left: 50px">●最佳解析度1024X768 </span> --> 
	    	<span style="margin-left: 650px">●版号：<%@ include file="//version.txt"%> </span>  
	</div>
</body>
<!-- 增加頁面背景圖片 -->
<script type="text/javascript">
  $.backstretch("${contextPath}/assets/img/imgo.jpeg");
</script>
</html>
