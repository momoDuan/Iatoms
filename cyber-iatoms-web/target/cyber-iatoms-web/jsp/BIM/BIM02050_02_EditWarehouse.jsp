<%@page import="cafe.workflow.web.controller.util.WfSessionHelper"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.WarehouseDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.WarehouseFormDTO"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	List<Parameter> vendorList = (List<Parameter>) WfSessionHelper.getAttribute(
		request,IAtomsConstants.UC_NO_BIM_02050, IAtomsConstants.PARAM_COMPANY_LIST);
	WarehouseFormDTO formDTO = null;
	WarehouseDTO warehouseDTO = null;
	if (ctx != null) {
		formDTO = (WarehouseFormDTO) ctx.getResponseResult();
		 if(formDTO != null){
			//若FormDTO存在，獲取DTO
			warehouseDTO = formDTO.getWarehouseDTO();
		} 
	} else {
		formDTO = new WarehouseFormDTO();
	}	
	//倉庫地址下拉框的內容
	List<Parameter> locations = 
		(List<Parameter>) WfSessionHelper.getAttribute(request, IAtomsConstants.UC_NO_BIM_02050, IATOMS_PARAM_TYPE.LOCATION.getCode());
%>
<html>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="vendorList" value="<%=vendorList%>" scope="page"></c:set>
<c:set var="locations" value="<%=locations%>" scope="page"></c:set>
<c:set var="warehouseDTO" value="<%=warehouseDTO%>" scope="page"></c:set>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<div data-options="region:'center'"
		style="width: auto; height: auto; padding: 10px 20px; background: #fff; overflow-y: hidden">
	<div class="dialogtitle">倉庫據點維護</div>
	<div><span id="dialogMsg" class="red"></span></div>
		<form id="fm" method="post" novalidate>
			<table cellpadding="4">
				<tr>
					<td width="20%">維護廠商:<span class="red">*</span></td>
					<td>
						<c:choose>
						<c:when test="${empty formDTO.warehouseId}">
							<cafe:droplisttag css="easyui-combobox" id="<%=WarehouseDTO.ATTRIBUTE.COMPANY_ID.getValue()%>"
								name="<%=WarehouseDTO.ATTRIBUTE.COMPANY_ID.getValue()%>" result="${vendorList}" blankName="請選擇"
								hasBlankValue="true" selectedValue="${warehouseDTO.companyId}" style="width: 150px"
								javascript="validType=\"requiredDropList\" editable=false required=true invalidMessage=\"請輸入維護廠商\"">
							</cafe:droplisttag>
						</c:when>
						<c:otherwise>
							<cafe:droplisttag css="easyui-combobox" id="<%=WarehouseDTO.ATTRIBUTE.COMPANY_ID.getValue()%>"
								name="<%=WarehouseDTO.ATTRIBUTE.COMPANY_ID.getValue()%>" result="${vendorList}" blankName="請選擇"
								hasBlankValue="true" selectedValue="${warehouseDTO.companyId}" style="width: 150px" disabled="true"
								javascript="validType=\"requiredDropList\" editable=false required=true invalidMessage=\"請輸入維護廠商\"">
							</cafe:droplisttag>
						</c:otherwise>
						</c:choose>
					</td>
					<td width="20%">倉庫名稱:<span class="red">*</span></td>
					<td>
						<input id="<%=WarehouseDTO.ATTRIBUTE.NAME.getValue()%>" name="<%=WarehouseDTO.ATTRIBUTE.NAME.getValue()%>"
							class="easyui-textbox" maxlength = "50" data-options="required:true, missingMessage:'請輸入倉庫名稱', validType:['maxLength[50]']"
							value="<c:out value='${warehouseDTO.name}'/>" ></input>
					</td>
				</tr>
				<tr>
					<td style="width: 60px">聯絡人:<span class="red">*</span></td>
					<td>
						<input id="<%=WarehouseDTO.ATTRIBUTE.CONTACT.getValue()%>" name="<%=WarehouseDTO.ATTRIBUTE.CONTACT.getValue()%>"
							class="easyui-textbox" maxlength = "50" data-options="required:true, missingMessage:'請輸入聯絡人', validType:['maxLength[50]']" style="width: 150px"
							value="<c:out value='${warehouseDTO.contact}'/>"></input>
					</td>
					<td style="width: 60px">電話:<span class="red">*</span></td>
					<td>
						<input class="easyui-textbox" id="<%=WarehouseDTO.ATTRIBUTE.TEL.getValue()%>" name="<%=WarehouseDTO.ATTRIBUTE.TEL.getValue()%>"
							maxlength = "20" data-options="required:true, missingMessage:'請輸入電話', validType:['maxLength[20]']" value="<c:out value='${warehouseDTO.tel}'/>"></input>
					</td>
				</tr>
				<tr>
					<td style="width: 60px">傳真:</td>
					<td>
						<input class="easyui-textbox" id="<%=WarehouseDTO.ATTRIBUTE.FAX.getValue()%>" name="<%=WarehouseDTO.ATTRIBUTE.FAX.getValue()%>"
							style="width: 150px" maxlength = "20" value="<c:out value='${warehouseDTO.fax}'/>" data-options="validType:['maxLength[20]']"></input>
					</td>
				</tr>
				<tr>
					<td style="width: 60px">倉庫地址:<span class="red">*</span></td>
					<td colspan="3">
						<cafe:droplisttag css="easyui-combobox" id="<%=WarehouseDTO.ATTRIBUTE.LOCATION.getValue()%>"
							name="<%=WarehouseDTO.ATTRIBUTE.LOCATION.getValue()%>" result="${locations}" blankName="請選擇"
							hasBlankValue="true" selectedValue="${warehouseDTO.location}"
							style="width: 150px" javascript="validType=\'requiredDropList\', invalidMessage=\"請輸入倉庫地址-縣市\" editable=false">
						</cafe:droplisttag>
						<input class="easyui-textbox" id="<%=WarehouseDTO.ATTRIBUTE.ADDRESS.getValue()%>" name="<%=WarehouseDTO.ATTRIBUTE.ADDRESS.getValue()%>"
							style="width: 250px" maxlength = "100" data-options=" required:true, missingMessage:'請輸入倉庫地址', validType:['maxLength[100]'] " value="<c:out value='${warehouseDTO.address}'/>"></input>
					</td>
				</tr>
				<tr>
					<td style="width: 60px">說明:</td>
					<td colspan="3">
						<textarea id="<%=WarehouseDTO.ATTRIBUTE.COMMENT.getValue()%>" name="<%=WarehouseDTO.ATTRIBUTE.COMMENT.getValue()%>"
							class="easyui-textbox"  maxlength = "200" data-options="multiline:true, validType:['maxLength[200]']" style="height: 98px; width:400px"><c:out value='${warehouseDTO.comment}'/></textarea>
				</tr>
				<!-- 倉庫編號隱藏域 -->
					<input id="<%=WarehouseDTO.ATTRIBUTE.WAREHOUSE_ID.getValue()%>" name="<%=WarehouseDTO.ATTRIBUTE.WAREHOUSE_ID.getValue()%>"
						type="hidden" value="<c:out value='${warehouseDTO.warehouseId}'/>"/>
				<!-- 倉庫編號隱藏域 -->
			</table>
		</form>
	</div>
</body>
</html>