<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.MerchantFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.MerchantDTO"%>
	<%
		SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
		MerchantFormDTO formDTO = null;
		MerchantDTO bimMerchantDTO = null;
		String ucNo = null;
		if (ctx != null) {
			formDTO = (MerchantFormDTO) ctx.getRequestParameter();
		} 
		if (formDTO == null) {
			formDTO = new MerchantFormDTO();
		}else{
			ucNo = formDTO.getUseCaseNo();
			bimMerchantDTO = formDTO.getBimMerchantDTO();
		}
		List<Parameter> customerList = (List<Parameter>) SessionHelper.getAttribute(request,ucNo,IAtomsConstants.PARAM_CUSTOMER_LIST);
	%>
<html>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="bimMerchantDTO" value="<%=bimMerchantDTO%>" scope="page"></c:set>
<c:set var="customerList" value="<%=customerList%>" scope="page"></c:set>
<head>
<%@ include file="/jsp/common/easyui-common.jsp"%>
</head>
<body>
		<div id="editMerchantDiv" data-options="region:'center'" style="width: auto; height: auto; padding: 10px 20px; background: #fff; overflow-y: hidden">
		<div class="dialogtitle">客戶特店維護</div>
		<div><span id="msg" class="red"></span></div>
			<form id="fm1" method="post" class="formStyle">
				<table cellpadding="4">
					<tr>
						<td>客戶:<span class="red">*</span></td>
						<td colspan="1">
							<cafe:droplisttag 
								id="<%=MerchantDTO.ATTRIBUTE.COMPANY_ID.getValue()%>" 
								name="<%=MerchantDTO.ATTRIBUTE.COMPANY_ID.getValue()%>"
								css="easyui-combobox"
								result="${customerList}"
								blankName="請選擇" 
								hasBlankValue="true"
								disabled="${empty bimMerchantDTO.companyId ? ((formDTO.roleAttribute eq 'CUSTOMER_VENDOR')?true:false) :true}"
								selectedValue="${empty bimMerchantDTO.companyId ?((formDTO.roleAttribute eq 'CUSTOMER_VENDOR')?logonUser.companyId:''): bimMerchantDTO.companyId}"
								style="width: 150px"
								javascript="editable='false' validType=\"ignore['請選擇']\" invalidMessage=\"請選擇客戶\"">
							</cafe:droplisttag>
						</td>
					</tr>
					<tr>
						<td>特店代號:<span class="red">*</span></td>
						<td colspan="1">
							<input maxlength="20" style="width: 150px" id="<%=MerchantDTO.ATTRIBUTE.MERCHANT_CODE.getValue() %>" name="<%=MerchantDTO.ATTRIBUTE.MERCHANT_CODE.getValue() %>" value="${bimMerchantDTO.merchantCode }" 
									class="easyui-textbox" type="text" required="true" data-options="validType:['englishOrNumber[\'特店代號限輸入英數字，請重新輸入\']','maxLength[20]']" missingMessage="請輸入特店代號" />
						</td>
						<td>統一編號:</td>
						<td>
							<input class="easyui-textbox" type="text" maxlength = "10"
								value="<c:out value='${bimMerchantDTO.unityNumber}'/>"  
								id="<%=MerchantDTO.ATTRIBUTE.UNITY_NUMBER.getValue()%>"
								name="<%=MerchantDTO.ATTRIBUTE.UNITY_NUMBER.getValue()%>" 
								data-options="validType:['englishOrNumber[\'統一編號限輸入英數字，請重新輸入\']','maxLength[10]']"
								/>
						</td> 
					</tr>
					<tr>
						<td>特店名稱:<span class="red">*</span></td>
						<td colspan="3">
							<input maxlength="50" id="<%=MerchantDTO.ATTRIBUTE.NAME.getValue() %>" name="<%=MerchantDTO.ATTRIBUTE.NAME.getValue()%>" class="easyui-textbox" style="height: 25px; width: 480px" type="text" value="<c:out value='${bimMerchantDTO.name }'/>"
								required="true" data-options="validType:['maxLength[50]']" missingMessage="請輸入特店名稱" />
							</input>
						</td>
					</tr>
					<tr>
						<td>備註:</td>
						<td colspan="3">
							<textarea 
							id="<%=MerchantDTO.ATTRIBUTE.REMARK.getValue()%>" 
							name="<%=MerchantDTO.ATTRIBUTE.REMARK.getValue() %>" 
							maxlength="200" 
							class="easyui-textbox" 
							data-options="multiline:true"
							validType="maxLength[200]"
							style="height: 98px; width: 480px" ><c:out value='${bimMerchantDTO.remark }'/></textarea>
						</td>
					</tr>
					<tr>
						 <input type="easyui-textbox"id="<%=MerchantDTO.ATTRIBUTE.MERCHANT_ID.getValue()%>" name="<%=MerchantDTO.ATTRIBUTE.MERCHANT_ID.getValue()%>" value="<c:out value='${bimMerchantDTO.merchantId }'/>" hidden='true' />
					</tr>
			</table>
		</form>
</body>
</html>