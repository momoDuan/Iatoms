<%@page import="java.sql.Timestamp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.PasswordSettingDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.PasswordSettingFormDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	PasswordSettingFormDTO formDTO = null;
	String ucNo = null;
	PasswordSettingDTO passwordSettingDTO = new PasswordSettingDTO();
	if (ctx != null) {
		formDTO = (PasswordSettingFormDTO) ctx.getResponseResult();
		if(formDTO != null){
			passwordSettingDTO = formDTO.getPasswordSettingDTO();
			ucNo = formDTO.getUseCaseNo();
		}
	}
	//密碼開始和結束長度
	List<Parameter> pwdLength = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.PASSWORD_LENGTH.getCode());
	List<Parameter> integerList = (List<Parameter>) SessionHelper.getAttribute(request,ucNo, "INTEGER");
	//容許錯誤次數
	List<Parameter> errCount = integerList.subList(3, 11);
	//重複次數
	List<Parameter> rpCount = integerList.subList(3, 11);
	//當前時間
	Timestamp currentDate = DateTimeUtils.getCurrentTimestamp(); 
 %>
<html>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="passwordSettingDTO" value="<%=passwordSettingDTO%>" scope="page"></c:set>
<c:set var="pwdLength" value="<%=pwdLength%>" scope="page"></c:set>
<c:set var="errCount" value="<%=errCount%>" scope="page"></c:set>
<c:set var="rpCount" value="<%=rpCount%>" scope="page"></c:set>
<c:set var="currentDate" value="<%=currentDate%>" scope="page"></c:set>
<head>
<meta charset="UTF-8">
<%@include file="/jsp/common/easyui-common.jsp"%>
</head>
<body>
<div style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
	<!-- <div region="center" style="width: 100%; height: auto; padding: 1px; background: #eee; overflow-y: hidden"> -->
		<div class="easyui-panel" title="密碼原則設定" style="width: 98%; height: auto">
		<div align="left"><span id="saveMessage" class="red"></span></div>
			<div style="padding: 10px 60px 20px 60px">
				<form id="pwdSettingForm"  method="post">
					<table cellpadding="2">
						<tr>
							<td>密碼長度:<span class="red">*</span></td>
							<td>
								<cafe:droplisttag css="easyui-combobox"
		                            id="<%=PasswordSettingDTO.ATTRIBUTE.PWD_LEN_BG.getValue()%>"
		                            name="<%=PasswordSettingDTO.ATTRIBUTE.PWD_LEN_BG.getValue()%>" 
		                            result="${pwdLength}"
		                            hasBlankValue="false"
		                            selectedValue="${passwordSettingDTO.pwdLenBg}"
		                            defaultValue="6"
		                            style="width: 50px"
		                            javascript="panelHeight='auto' editable=false required=true"
		                        >
		                        </cafe:droplisttag>
								 ~ 
								 <cafe:droplisttag 
		                            id="<%=PasswordSettingDTO.ATTRIBUTE.PWD_LEN_ND.getValue()%>"
		                            name="<%=PasswordSettingDTO.ATTRIBUTE.PWD_LEN_ND.getValue()%>" 
		                            css="easyui-combobox"
		                            result="${pwdLength}"
		                            hasBlankValue="false"
		                            selectedValue="${passwordSettingDTO.pwdLenNd}"
		                            defaultValue="10"
		                            style="width: 50px"
		                            javascript="panelHeight='auto' validType=mycompare['#pwdLenBg'] editable=false required=true">
	                            </cafe:droplisttag>
							</td>
							<td>容許錯誤次數:<span class="red">*</span></td>
							<td>
								<cafe:droplisttag  css="easyui-combobox"
		                            id="<%=PasswordSettingDTO.ATTRIBUTE.PWD_ERR_CNT.getValue()%>"
		                            name="<%=PasswordSettingDTO.ATTRIBUTE.PWD_ERR_CNT.getValue()%>" 
		                            result="${errCount}"
		                            hasBlankValue="false"
		                            selectedValue="${passwordSettingDTO.pwdErrCnt}"
		                            defaultValue="3"
		                            style="width: 50px"
		                            javascript="panelHeight='auto' editable=false required=true">
	                            </cafe:droplisttag>
							</td>
						</tr>
						<tr>
							<td>密碼有效週期(天):<span class="red">*</span></td>
							<td>
								<input  class="easyui-textbox" type="text" 
										maxlength="3" 
										value="${passwordSettingDTO.pwdValidDay != null && passwordSettingDTO.pwdValidDay != ''?passwordSettingDTO.pwdValidDay:'180'}" 
										data-options="validType:['positiveInt[\'密碼有效週期限正整數，請重新輸入\']','maxLength[3]']" 
										required="true" 
										missingMessage="請輸入密碼有效週期(天)"
										id="<%=PasswordSettingDTO.ATTRIBUTE.PWD_VALID_DAY.getValue().toString() %>" 
										name="<%=PasswordSettingDTO.ATTRIBUTE.PWD_VALID_DAY.getValue().toString() %>">
								</input>
							</td>
							<td>首次登入需修改密碼:</td>
							<td>
								<input type="checkbox"  id="changeBox" 
									<c:if test="${empty passwordSettingDTO || passwordSettingDTO.pwdChgFlag != 'N'}">checked</c:if>
								 	onchange="isChange()">
								 </input>
								<input type="hidden" 
									name="<%=PasswordSettingDTO.ATTRIBUTE.PWD_CHG_FLAG.getValue().toString() %>" 
									id="<%=PasswordSettingDTO.ATTRIBUTE.PWD_CHG_FLAG.getValue().toString() %>"
								 	value="${passwordSettingDTO.pwdChgFlag}" >
								</td>
							</td>
						</tr>
						<tr>
							<td>帳號停權週期(天):<span class="red">*</span></td>
							<td>
								<input class="easyui-textbox" min="1" 
									value="${passwordSettingDTO.idValidDay != null && passwordSettingDTO.idValidDay != ''?passwordSettingDTO.idValidDay:'30'}"  
									required="true" 
									maxlength="3"
									data-options="validType:['positiveInt[\'帳號停權週期限正整數，請重新輸入\']','maxLength[3]']"
									missingMessage="請輸入帳號停權週期(天)"
									id="<%=PasswordSettingDTO.ATTRIBUTE.ID_VALID_DAY.getValue().toString() %>" 
									name="<%=PasswordSettingDTO.ATTRIBUTE.ID_VALID_DAY.getValue().toString() %>">
								</input>
							</td>
							<td>新密碼不可與前<span class="red">*</span></td>
							<td>
								<cafe:droplisttag  css="easyui-combobox"
									id="<%=PasswordSettingDTO.ATTRIBUTE.PWD_RP_CNT.getValue()%>"
									name="<%=PasswordSettingDTO.ATTRIBUTE.PWD_RP_CNT.getValue()%>" 
									result="${rpCount}"
									hasBlankValue="false"
									defaultValue="3"
									selectedValue="${passwordSettingDTO.pwdRpCnt}"
									style="width: 50px"
									javascript="panelHeight='auto' editable=false required=true">
								</cafe:droplisttag>
							次密碼重複</td>
						</tr>
						<tr>
							<td>密碼到期提示(天):<span class="red">*</span></td>
							<td>
								<input class="easyui-textbox" 
									value="${passwordSettingDTO.pwdAlertDay != null && passwordSettingDTO.pwdAlertDay != ''?passwordSettingDTO.pwdAlertDay:'7'}" 
									required="true" 
									maxlength="3"
									data-options="validType:['positiveInt[\'密碼到期提示限正整數，請重新輸入\']','maxLength[3]']"
									missingMessage="請輸入密碼到期提示(天)" 
									id="<%=PasswordSettingDTO.ATTRIBUTE.PWD_ALERT_DAY.getValue().toString() %>"
									name="<%=PasswordSettingDTO.ATTRIBUTE.PWD_ALERT_DAY.getValue().toString() %>">
								</input>
							</td>
						</tr>
						<tr>
							<td>異動人員:</td>
							<td>
								<input class="easyui-textbox" 
									type="text" value="<c:out value='${empty passwordSettingDTO.updatedByName ?logonUser.name:passwordSettingDTO.updatedByName}'/>" 
									id="<%=PasswordSettingDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue().toString()%>"
									name="<%=PasswordSettingDTO.ATTRIBUTE.UPDATED_BY_NAME.getValue().toString() %>" 
									disabled="true">
								</input>
							</td>
							<td>異動日期:</td>
							<td><input class="easyui-textbox" type="text"
									name="<%=PasswordSettingDTO.ATTRIBUTE.UPDATED_DATE.getValue().toString() %>" 
									id="<%=PasswordSettingDTO.ATTRIBUTE.UPDATED_DATE.getValue().toString() %>" 
									disabled="true"
									value="<fmt:formatDate value="${empty passwordSettingDTO.updatedDate? currentDate:passwordSettingDTO.updatedDate}" pattern="yyyy/MM/dd HH:mm:ss"/>"></input>
								<input type="hidden" name="<%=PasswordSettingDTO.ATTRIBUTE.ID.getValue()%>" value="<c:out value='${passwordSettingDTO.id}'/>"></input>
							</td>
						</tr>
						<tr>
							<td colspan="4" align="right" style="padding-top: 10px"><a
								 class="easyui-linkbutton"
								data-options="iconCls:'icon-save'" id="btnSave" onclick="submitForm()">儲存</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	//提交頁面數據
	function submitForm() {
		var controls = ['pwdLenNd'];
		//驗證表單
		if (!validateForm(controls) || !$('#pwdSettingForm').form("validate")) {
			$("#saveMessage").empty();
			return;
		} else {
			// 遮罩样式
			var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			// 形成遮罩
			$.blockUI(blockStyle);
			$.ajax({
				url: "${contextPath}/passwordSetting.do?actionId=<%=IAtomsConstants.ACTION_SAVE_SETTING%>",
				data:$('#pwdSettingForm').form("getData"),
				type:'post', 
				cache:false, 
				dataType:'json', 
				success:function(data){
					// 去除遮罩
					$.unblockUI();
					var updatedDate = formatToTimeStamp(data.UpdatedDate);
					$("#saveMessage").text(data.msg);
					$('#pwdSettingForm').form("load",data.row);
					$("#updatedDate").textbox('setValue',updatedDate);
					$("#updatedByName").textbox('setValue',data.UpdatedByName);
				},
				error:function(){
					// 去除遮罩
					$.unblockUI();
					var msg = "儲存失敗"
					$.messager.alert('提示', msg, 'error');
				}
			});
		} 
	}
	//最小密碼長度onchange
	$(document).ready(function () { 
		$('#pwdLenBg').combobox({
			onChange:function(newValue,oldValue) {
				$('#pwdSettingForm').form("validate");
			}
		});
	});
	//對checkBox設值，選中為'Y',未選中為'N'
	function isChange() {
		//var pwdChangeFlag = document.getElementById("changeBox");
		if ($("#changeBox").prop("checked")) {
			$("#<%=PasswordSettingDTO.ATTRIBUTE.PWD_CHG_FLAG.getValue().toString() %>").val("<%=IAtomsConstants.YES%>");
		} else {
			$("#<%=PasswordSettingDTO.ATTRIBUTE.PWD_CHG_FLAG.getValue().toString() %>").val("<%=IAtomsConstants.NO%>");
		}
	}
</script>
</body>
</html>