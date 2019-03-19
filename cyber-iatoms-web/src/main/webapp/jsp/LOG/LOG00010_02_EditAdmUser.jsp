<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%@include file="/jsp/common/easyui-common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.SystemLogFormDTO"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	SystemLogFormDTO formDTO = null;
	String ucNo = null;
	if (ctx != null) {
		formDTO = (SystemLogFormDTO) ctx.getResponseResult();
		if (formDTO != null) { 
			// 获得UseCaseNo
			ucNo = formDTO.getUseCaseNo();
		} else {
			ucNo = IAtomsConstants.UC_NO_AMD_01040;
			formDTO = new SystemLogFormDTO();
		}
	} else {
		ucNo = IAtomsConstants.UC_NO_AMD_01040;
		formDTO = new SystemLogFormDTO();
	}
%>
<html>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>AdmUser Edit</title>
</head>
<body>	
<script type="text/javascript">
//var myList;
$(function(){
	 var params;
	 // 處理便利頁面欄位顯示值
	 <c:if test="${not empty formDTO.logContent}">
		params = ${formDTO.logContent};
		$("#dlgCompanyName").append("<option value='Value'>"+ params.companyName +"</option>");
		$("#dlgDeptName").append("<option value='Value'>"+ params.deptName +"</option>");
		$("#dlgAccount").val(params.account);
		$("#dlgCname").val(params.cname);
		$("#dlgEname").val(params.ename);
		$("#dlgTel").val(params.tel);
		$("#dlgMobile").val(params.mobile);
		$("#dlgEmail").val(params.email);
		$("#dlgManagerEmail").val(params.managerEmail);
		$("#dlgUserDesc").val(params.userDesc);
		$("#dlgRetry").val(params.retry);
		$("#dlgStatusName").append("<option value='Value'>"+ params.statusName +"</option>");
		if(!isEmpty(params.userId)){
			$("#dlgCompanyName").attr("disabled",true);
		}
		if(isEmpty(params.userId)){
			$("#dlgStatusName").attr("disabled",true);
		}
		if(params.requiredPwd == 'Y'){
			$("#dlgPwdLabel").html("密碼:<span class=\"red\">*</span>");
			$("#dlgComfirmPwdLabel").html("確認密碼:<span class=\"red\">*</span>");
		}
		if(params.disabledPwd == 'Y' && params.requiredPwd == 'N'){
			$("#dlgPwd").attr("disabled",true);
			$("#dlgComfirmPwd").attr("disabled",true);
		}
		if(params.dataAcl == 'Y'){
			$("#dlgDataAcl").attr("checked",true);
		}
		if(!isEmpty(params.userId)){
			$("#dlgAccount").attr("disabled",true);
		} else {
			$("#dlgAccount").attr("data-options","buttonIcon:'icon-search'");
		}
		if(params.pwdShowText){
			$("#dlgPwd").val(params.pwdShowText);
			$("#dlgComfirmPwd").val(params.pwdShowText);
		}
	 </c:if>
	$('#roleMultipleSelect').multiSelect({});
	$('#warehouseMultipleSelect').multiSelect({});
	$(".ms-container").css({"width":"260px"});
	$(".ms-list").css({"height":"180px"});
	// 處理多選下拉框選中值
	if(params.functionStrAll){
		var roleStrAll = JSON.parse(params.functionStrAll);
		$("#roleMultipleSelect").multiSelect('addOption',roleStrAll);
	}
	if(params.selectFunctionStr){
		var selectRoleStr = JSON.parse(params.selectFunctionStr);
		for (var i = 0; i < selectRoleStr.length; i++) {
			$("#roleMultipleSelect").multiSelect('select',selectRoleStr[i].value);
		}
	}
	
	if(params.warehouseStrAll){
		var warehouseStrAll = JSON.parse(params.warehouseStrAll);
		$("#warehouseMultipleSelect").multiSelect('addOption',warehouseStrAll);
	}
	
	if(params.selectWarehouseStr){
		var selectWarehouseStr = JSON.parse(params.selectWarehouseStr);
		for (var i = 0; i < selectWarehouseStr.length; i++) {
			$("#warehouseMultipleSelect").multiSelect('select',selectWarehouseStr[i].value);
		}
	}
	// 處理多選下拉框禁用
	$('#roleMultipleSelectAll').attr("style","color:blue;cursor: default;");
	$('#roleMultipleDeselectAll').attr("style","color:blue;cursor: default;");
	$('#warehouseMultipleSelectAll').attr("style","color:blue;cursor: default;");
	$('#warehouseMultipleDeselectAll').attr("style","color:blue;cursor: default;");
	$("#ms-roleMultipleSelect").find("li").addClass("disabled");
	$("#ms-warehouseMultipleSelect").find("li").addClass("disabled");
	//  禁用部分區塊
	disabledBlock();
});
/**
 * 禁用部分區塊
 */
function disabledBlock(){
	// 內容部分全部遮罩
	var blockOptions = {message:null,overlayCSS:{backgroundColor:'#fff',cursor:'default',opacity:'0.1'}};
	$("#disabledBlock").block(blockOptions);
}
</script>
        <div style="width: auto; height: auto; padding: 10px 20px; overflow-y: hidden" data-options="region:'center'">
            <div class="ftitle">使用者帳號管理</div>
            <form id="editForm" method="post" class="logFormStyle" data-options="novalidate:true" >
            <div id="disabledBlock">
                <table cellpadding="5">
                    <tr>
                        <td>
                            <label>公司:<span class="red">*</span></label>
                        </td>
                        <td>
							<select id="dlgCompanyName" class="easyui-combobox" readonly style="width:200px">
							</select>
                        </td>
                        <td>
                            <label>部門:</label>
                        </td>
                        <td>
                            <select id="dlgDeptName" class="easyui-combobox" readonly style="width:200px">
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>帳號:<span class="red">*</span></label>
                        </td>
                        <td>
                           <input id="dlgAccount" class="easyui-textbox" style="width:200px" value="" readonly>
                        </td>
                        <td>
                            <label>中文姓名:<span class="red">*</span></label>
                        </td>
                        <td>
                            <input id="dlgCname" class="easyui-textbox" style="width:200px" required="true" value="" readonly>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>英文姓名:</label>
                        </td>
                        <td>
                            <input id="dlgEname" class="easyui-textbox" style="width:200px" value="" readonly>
                        </td>
                        <td>
                            <label>電話:</label>
                        </td>
                        <td>
                            <input id="dlgTel" class="easyui-textbox" style="width:200px" value="" readonly>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>行動電話:</label>
                        </td>
                        <td>
                            <input id="dlgMobile" class="easyui-textbox" style="width:200px" value="" readonly>
                        </td>
                        <td>
                            <label>EMail:</label>
                        </td>
                        <td>
                            <input id="dlgEmail" class="easyui-textbox" style="width:200px" value="" readonly>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>主管EMail:</label>
                        </td>
                        <td>
                            <input id="dlgManagerEmail" class="easyui-textbox" style="width:200px" value="" readonly>
                        </td>
                        <td>
                            <label>備註:</label>
                        </td>
                        <td>
                            <textarea id="dlgUserDesc" class="easyui-textbox"  data-options="multiline:true" style="height: 50px;width:200px" value="" readonly>
                            </textarea>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>登入錯誤次數:</label>
                        </td>
                        <td>
                            <input id="dlgRetry" class="easyui-textbox" style="width:200px" value="" disabled>
                        </td>
                        <td>
                            <label>狀態:</label>
                        </td>
                        <td>
                            <select id="dlgStatusName" class="easyui-combobox" readonly style="width:200px">
                            </select>
                        </td>
                    </tr>
                     <tr>
                        <td>
                            <label id="dlgPwdLabel">密碼:</label>
                        </td>
                        <td>
                            <input type="password" id="dlgPwd" class="easyui-textbox" style="width:200px" readonly >
                        </td>
                        <td>
                            <label id="dlgComfirmPwdLabel">確認密碼:</label>
                        </td>
                        <td>
                             <input type="password" id="dlgComfirmPwd" class="easyui-textbox" style="width:200px" readonly>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2"></td>
                        <td colspan="2">
						<input type="checkbox" id="dlgDataAcl">
                            控管資料權限 (勾選-控管;未勾選-可見所有倉庫)
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td style="width:260px"></td>
                        <td></td>
                        <td style="width:260px"></td>
                    </tr>
				</table>
			</div>
				<table cellpadding="5">
				<!-- </div> -->
                    <tr>
                        <td>
                            <label>角色權限:</label>
                        </td>
                        <td>
                            <a href='#' id='roleMultipleSelectAll'>全選</a>
                            <a href='#' id='roleMultipleDeselectAll'>取消全選</a>
                            <select  multiple="multiple" id="roleMultipleSelect" name="roleMultipleSelect[]">
                            </select>
                        </td>
                        <td style="width:60px"></td>
                        <td>
                            <a href='#' id='warehouseMultipleSelectAll'>全選</a>
                            <a href='#' id='warehouseMultipleDeselectAll'>取消全選</a>
                            <select  multiple="multiple" id="warehouseMultipleSelect" name="warehouseMultipleSelect[]" >
                            </select>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
</body>
</html>