<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.PasswordSettingDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.PasswordFormDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%
	SessionContext ctx = (SessionContext)request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
   	IAtomsLogonUser iatomsLogonUser = null; 
   	PasswordFormDTO formDTO = null;
	if (ctx != null) {
		formDTO = (PasswordFormDTO) ctx.getResponseResult();
		iatomsLogonUser = (IAtomsLogonUser)ctx.getLogonUser();
	} else {
		formDTO = new PasswordFormDTO();
	}
	PasswordSettingDTO admSecurityDefDTO = formDTO.getAdmSecurityDefDTO();
	if (admSecurityDefDTO == null) {
		admSecurityDefDTO = new PasswordSettingDTO();
	}
%>
<c:set var="passwordSettingDTO" value="<%=admSecurityDefDTO%>" scope="page"></c:set>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Change Password</title>
<%@include file="/jsp/common/easyui-common.jsp"%>
</head>
<body>
	<div region="center" style="width: auto; height: auto; padding: 1px; overflow-y: hidden">
		<div class="easyui-panel" title="使用者密碼變更" style="width: 100%; height: 240px" id="changePwd">
			<form id="fm" class="easyui-form" method="post" autocomplete="off">
			 <div><span id="dialogMsg" class="red"></span></div>
				<div style="padding: 10px 0px 0px 0px">
			         <table cellpadding="2">
			             <tr>
			                 <td>舊密碼:</td>
			                 <td>
			                     <input class="easyui-textbox" type="password" name="oldPwd" id="oldPwd" 
			                     	required="true" missingMessage="請輸入舊密碼" maxlength="20" data-options="validType:['maxLength[20]']"
			                     	>
			                     	</input>
			                 </td>
			             </tr>
			             <tr>
			                 <td>新密碼:</td>
			                 <td>
			                     <input class="easyui-textbox" type="password" name="newPwd" id="newPwd" 
			                     	required="true" missingMessage="請輸入新密碼" maxlength="20"
			                     	data-options="validType:['maxLength[20]','pwdLength[${passwordSettingDTO.pwdLenBg },${passwordSettingDTO.pwdLenNd}]','pwdCharacter','pwdCharacterOrder','pwdNumber','pwdCharRepeat','pwdSameId[\'${logonUser.userCode}\',\'value\']']"></input>
			                </td>
			             </tr>
			             <tr>
			                 <td>確認新密碼:</td>
			                 <td>
			                     <input class="easyui-textbox" type="password" name="rePwd" id="rePwd"
			                     	required="true" missingMessage="請輸入確認新密碼" maxlength="20"
			                     	validType="equalTo['#newPwd','maxLength[20]']" 
			                				invalidMessage="確認新密碼與新密碼不一致，請重新輸入"></input>
			                 </td>
			             </tr>
			         </table>
			     </div>
			     <input type="hidden" id="openDialog" name="openDialog" >
		     </form>
		     <div style="margin-left:180px; padding: 5px">
	                <a href="javascript:void(0)" class="easyui-linkbutton" id="btnSave" iconcls="icon-ok" onclick="changePassword()">變更密碼</a>
		     </div>
	    </div>
	    <script type="text/javascript">
	    $(function(){
	    	<c:if test="${logonUser.isCyberUser}">
		    	$("#dialogMsg").text("CyberSoft帳號請利用個人電腦進行密碼變更，無法於此功能變更密碼");
	    		$("#oldPwd").textbox('disable');
	    		$("#oldPwd").textbox('disableValidation');
	    		$("#rePwd").textbox('disable');
	    		$("#rePwd").textbox('disableValidation');
	    		$("#newPwd").textbox('disable');
	    		$("#newPwd").textbox('disableValidation');
	    		$("#btnSave").linkbutton('disable');
	    	</c:if>
	    });
		//获得保存参数
		function getSaveParameter(form) {
			var saveParam = {
				//舊密碼
				oldPassword:form.find("#oldPwd").val(),
				//確認新密碼
				rePassword:form.find("#rePwd").val(),
				//新密碼
				newPassword:form.find("#newPwd").val(),
				openDialog:form.find("#openDialog").val(),
				//重複次數
				pwdRpCnt: ${passwordSettingDTO.pwdRpCnt},
				actionId:'<%=IAtomsConstants.ACTION_SAVE%>'
			}
			return saveParam;
		}
		//保存修改密码
		function changePassword() {
			$("#dialogMsg").empty();
			var f = $("#fm");
			if(f.form("validate")){
				// 遮罩样式
				var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
				//取值
				var saveParam = getSaveParameter(f);
				// 形成遮罩
				if(saveParam.openDialog == 'Y'){
					$("#changePwd").closest('div.panel').block(blockStyle);
				} else {
					$.blockUI(blockStyle);
				}
				//save
				$.ajax({
					url: "${contextPath}/changePassword.do",
					data:saveParam,
					type:'post', 
					cache:false, 
					dataType:'json', 
					success:function(json){
						// 去除遮罩
						if(saveParam.openDialog == 'Y'){
							$("#changePwd").closest('div.panel').unblock();
						} else {
							$.unblockUI();
						}
						if (json.success) {
							if(saveParam.openDialog == 'Y'){
								pwdEidtSuccess(saveParam);
							} else {
								$("#dialogMsg").text(json.msg);	
								//修改成功，清空舊密碼	
								$("#oldPwd").textbox('setValue','');
								//修改成功，清空新密碼	
								$("#newPwd").textbox('setValue','');
								//修改成功，清空確認新密碼	
								$("#rePwd").textbox('setValue','');
							} 
						} else {
							$("#dialogMsg").text(json.msg);	
							//修改失敗，清空舊密碼	
							$("#oldPwd").textbox('setValue','');
							//修改失敗，清空新密碼	
							$("#newPwd").textbox('setValue','');
							//修改失敗，清空確認新密碼	
							$("#rePwd").textbox('setValue','');
						}
					},
					error:function(){
						// 去除遮罩
						if(saveParam.openDialog == 'Y'){
							$("#changePwd").closest('div.panel').unblock();
						} else {
							$.unblockUI();
						}
						$("#dialogMsg").text(json.msg);
						//修改失敗，清空舊密碼	
						$("#oldPwd").textbox('setValue','');
						//修改失敗，清空新密碼
						$("#newPwd").textbox('setValue','');
						//修改失敗，清空確認新密碼
						$("#rePwd").textbox('setValue','');
					}
				});
			}
		}
	    </script>
	</div>
</body>
</html>