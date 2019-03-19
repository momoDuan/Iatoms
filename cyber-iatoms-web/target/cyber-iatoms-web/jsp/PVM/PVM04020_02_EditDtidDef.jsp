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
	} 
	//update by hermanwang 2017-05-17
	if(formDTO == null) {
		formDTO = new DtidDefFormDTO();
	}
	PvmDtidDefDTO pvmDtidDefDTO = formDTO.getDtidDefDTO();
	String useCaseNo = formDTO.getUseCaseNo();
	//客戶下拉菜單
	List<Parameter> customers = (List<Parameter>)WfSessionHelper.getAttribute(request, useCaseNo, IAtomsConstants.PARAM_CUSTOMER_LIST);
	//獲取設備名稱下拉菜單
	List<Parameter> assetNames = (List<Parameter>)WfSessionHelper.getAttribute(request, useCaseNo, IAtomsConstants.PARAM_METHOD_GET_ASSET_LIST);
%>

<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="customers" value="<%=customers%>" scope="page"></c:set>
<c:set var="assetNames" value="<%=assetNames %>"></c:set>
<c:set var="pvmDtidDefDTO" value="<%=pvmDtidDefDTO %>"></c:set>
<html>
<head>
<script type="text/javascript" src="${contextPath}/js/easyui-extend.js"></script>

</head>
<body>
		<div data-options="region:'center',fit:true" style="width: auto; height: auto; padding: 10px 20px; overflow-y: hidden">
			<div class="dialogtitle">DTID號碼管理</div>
			<div style="color: red">
				<span id="msg1" class="red"></span>
			</div>
			<form id="fm" method="post" novalidate>
				<table cellpadding="4">
					<tr>
						<td style="width: 85px">客戶:<span class="red">*</span></td>
						<td>
							<c:if test="${!(formDTO.actionId eq 'initEdit') }">
								<cafe:droplisttag
									id="<%=PvmDtidDefDTO.ATTRIBUTE.COMPANY_ID.getValue() %>"
									name="<%=PvmDtidDefDTO.ATTRIBUTE.COMPANY_ID.getValue() %>" 
									css="easyui-combobox"
									selectedValue="${pvmDtidDefDTO.companyId}"
									result="${customers}"
									hasBlankValue="true"
									blankName="請選擇"
									style="width: 150px"
									javascript="validType='requiredDropList' editable=false required=true  invalidMessage=\"請輸入客戶\""
								></cafe:droplisttag>
								
							</c:if>
							<c:if test="${formDTO.actionId eq 'initEdit' }">
							
								<cafe:droplisttag
									id="<%=PvmDtidDefDTO.ATTRIBUTE.COMPANY_ID.getValue() %>"
									name="<%=PvmDtidDefDTO.ATTRIBUTE.COMPANY_ID.getValue() %>" 
									css="easyui-combobox"
									selectedValue="${pvmDtidDefDTO.companyId}"
									result="${customers}"
									hasBlankValue="true"
									blankName="請選擇"
									disabled="true"
									style="width: 150px"
									javascript="validType='requiredDropList' editable=false required=true"
								></cafe:droplisttag>
								
							</c:if>
						
						
							
						</td>
						<td style="width: 80px">設備名稱:<span class="red">*</span></td>
						<td>
							<c:if test="${!(formDTO.actionId eq 'initEdit') }">
								<cafe:droplisttag
									id="<%=PvmDtidDefDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue() %>"
									name="<%=PvmDtidDefDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue() %>"
									css="easyui-combobox"
									selectedValue="${pvmDtidDefDTO.assetTypeId}"
									result="${assetNames}"
									hasBlankValue="true"
									blankName="請選擇"
									style="width: 150px"
									javascript="validType='requiredDropList' editable=false required=true  invalidMessage=\"請輸入客戶\""
								></cafe:droplisttag>
								
							</c:if>
							<c:if test="${formDTO.actionId eq 'initEdit' }">
							
								<cafe:droplisttag
									id="<%=PvmDtidDefDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue() %>"
									name="<%=PvmDtidDefDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue() %>"
									css="easyui-combobox"
									selectedValue="${pvmDtidDefDTO.assetTypeId}"
									result="${assetNames}"
									hasBlankValue="true"
									blankName="請選擇"
									disabled="true"
									style="width: 150px"
									javascript="validType='requiredDropList' editable=false required=true"
								></cafe:droplisttag>
							</c:if>
						</td>
					</tr>
					<tr id="trDTID">
						<td>目前號碼:<span class="red">*</span></td>
						<td colspan="3">
							<input class="easyui-textbox" value="${pvmDtidDefDTO.currentNumber }" id = "number" style="width: 150px" type="text" name="number" data-options="required:true" disabled="disabled"></input>
							<input type="hidden" id="<%=PvmDtidDefDTO.ATTRIBUTE.CURRENT_NUMBER.getValue() %>" name="<%=PvmDtidDefDTO.ATTRIBUTE.CURRENT_NUMBER.getValue() %>" value="${pvmDtidDefDTO.currentNumber }"/>
						</td>
					</tr>
					<tr>
						<td>DTID起迄:<span class="red">*</span></td>
						<td colspan="3">
							<input class="easyui-textbox" style="width: 150px" type="text" id = "<%=PvmDtidDefDTO.ATTRIBUTE.DTID_START.getValue() %>" name="<%=PvmDtidDefDTO.ATTRIBUTE.DTID_START.getValue() %>" maxLength="8" data-options="required:true, missingMessage:'請輸入DTID起', validType:['maxLength[8]','numberBeginZero[\'DTID起限輸入長度為8碼的數字，請重新輸入\']','numberLengthEquals[\'8\',\'DTID起限輸入長度為8碼的數字，請重新輸入\']'], 
							onChange:function(newValue,oldValue) {
                            			$('#<%=PvmDtidDefDTO.ATTRIBUTE.DTID_END.getValue() %>').timespinner('isValid');
                             		}" value = "${pvmDtidDefDTO.dtidStart }"></input> ~ 
							<input class="easyui-textbox" style="width: 150px" type="text" id="<%=PvmDtidDefDTO.ATTRIBUTE.DTID_END.getValue() %>" name="<%=PvmDtidDefDTO.ATTRIBUTE.DTID_END.getValue() %>" maxLength="8" data-options="required:true, missingMessage:'請輸入DTID迄', validType:['maxLength[8]','numberBeginZero[\'DTID迄限輸入長度為8碼的數字，請重新輸入\']','compareNumberSize[\'#<%=PvmDtidDefDTO.ATTRIBUTE.DTID_START.getValue() %>\',\'DTID起不可大於DTID迄\']', 'numberLengthEquals[\'8\',\'DTID迄限輸入長度為8碼的數字，請重新輸入\']']" value = "${pvmDtidDefDTO.dtidEnd }"></input>
						</td>
					</tr>
					<tr>
						<td>說明:</td>
						<td colspan="3">
							<textarea 
							name="<%=PvmDtidDefDTO.ATTRIBUTE.COMMENT.getValue() %>" 
							class="easyui-textbox" 
							data-options="multiline:true" 
							maxLength="200" 
							style="height: 88px; width: 400px" 
							validType="maxLength[200]"><c:out value='${pvmDtidDefDTO.comment}'/></textarea>
							<input type="hidden" name="<%=PvmDtidDefDTO.ATTRIBUTE.ID.getValue() %>" value="<c:out value='${pvmDtidDefDTO.id }'/>"/>
						</td>
					</tr>
				</table>
			</form>
			
		</div>
	
</body>

</html>