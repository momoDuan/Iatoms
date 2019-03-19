<%@page import="com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html ">
<%@ include file="/jsp/common/common.jsp"%>
<%@ include file="/jsp/common/easyui-common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.DepartmentFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.BimDepartmentDTO"%>
<%
	// 獲取sessionContext
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	//獲取公司下拉框
	List<Parameter> companyList = (List<Parameter>) SessionHelper.getAttribute(request, IAtomsConstants.UC_NO_BIM_02020, IAtomsConstants.ACTION_GET_COMPANY_LIST);
	//獲取地址-縣市下拉框
	List<Parameter> locationList = (List<Parameter>) SessionHelper.getAttribute(request, IAtomsConstants.UC_NO_BIM_02020, IATOMS_PARAM_TYPE.LOCATION.getCode());
	DepartmentFormDTO formDTO = null;
	if (ctx != null) {
		formDTO = (DepartmentFormDTO) ctx.getResponseResult();
	} else {
		formDTO = new DepartmentFormDTO();
	}
	BimDepartmentDTO departmentManageDTO = formDTO.getBimDepartmentDTO();
	if (departmentManageDTO == null) {
		departmentManageDTO = new BimDepartmentDTO();
	}
%>
<html>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="departmentManageDTO" value="<%=departmentManageDTO%>" scope="page"></c:set>
<c:set var="companyList" value="<%=companyList%>" scope="page"></c:set>
<c:set var="locationList" value="<%=locationList%>" scope="page"></c:set>
<head>
<title></title>
</head>
<body>
	<div data-options="region:'center', fit:true" 
		style="width: auto; height: auto; padding: 10px 20px; overflow-y: hidden">
		<div class="dialogtitle">部門維護</div>
		<div><span id="msg" class="red"></span></div>
		<form id="fm" method="post" novalidate>
			<input type="hidden"
				id="<%=BimDepartmentDTO.ATTRIBUTE.DEPT_CODE.getValue()%>"
				name="<%=BimDepartmentDTO.ATTRIBUTE.DEPT_CODE.getValue()%>"
				value="<c:out value='${departmentManageDTO.deptCode}'/>" >
			</input>
			<table cellpadding="2">
				<tr>
					<td>
						公司:<span class="red">*</span>
					</td>
					<td>
					<c:choose>
						<c:when test="${empty departmentManageDTO.companyId }">
							<cafe:droplisttag css="easyui-combobox"
								id="<%=BimDepartmentDTO.ATTRIBUTE.COMPANY_ID.getValue()%>"
								name="<%=BimDepartmentDTO.ATTRIBUTE.COMPANY_ID.getValue()%>" 
								result="${companyList }"
								blankName="請選擇"
								hasBlankValue="true" 
								style="width:185px"
								javascript="editable=false required=true validType=\"ignore['請選擇']\" invalidMessage=\"請輸入公司\""
								selectedValue="${departmentManageDTO.companyId}">
				       		</cafe:droplisttag>
						</c:when>
						<c:otherwise>
							<cafe:droplisttag css="easyui-combobox"
								id="<%=BimDepartmentDTO.ATTRIBUTE.COMPANY_ID.getValue()%>"
								name="<%=BimDepartmentDTO.ATTRIBUTE.COMPANY_ID.getValue()%>" 
								result="${companyList }"
								blankName="請選擇"
								disabled="true"
								style="width:185px"
								hasBlankValue="true" 
								selectedValue="${departmentManageDTO.companyId}">
				       		</cafe:droplisttag>
						</c:otherwise>
					</c:choose>
					</td>
					<td>
						部門名稱:<span class="red">*</span>
					</td>
					<td>
						<input class="easyui-textbox" type="text" maxlength="50"
							id="<%=BimDepartmentDTO.ATTRIBUTE.DEPT_NAME.getValue()%>"
							name="<%=BimDepartmentDTO.ATTRIBUTE.DEPT_NAME.getValue()%>"
							data-options="required:true,validType:'maxLength[50]'"
							missingMessage="請輸入部門名稱"
							value="<c:out value='${departmentManageDTO.deptName}'/>">
						</input>
					</td>
				</tr>
				<tr>
					<td>
						聯絡人:
					</td>
					<td>
						<input class="easyui-textbox" type="text"  maxlength="50"
							id="<%=BimDepartmentDTO.ATTRIBUTE.CONTACT.getValue()%>"
							name="<%=BimDepartmentDTO.ATTRIBUTE.CONTACT.getValue()%>"
							data-options="validType:'maxLength[50]'"
							value="<c:out value='${departmentManageDTO.contact}'/>">
						</input>
					</td>
					<td>
						聯絡人電話:
					</td>
					<td>
						<input class="easyui-textbox" type="text"  maxlength="20"
							id="<%=BimDepartmentDTO.ATTRIBUTE.CONTACT_TEL.getValue()%>"
							name="<%=BimDepartmentDTO.ATTRIBUTE.CONTACT_TEL.getValue()%>"
							value="<c:out value='${departmentManageDTO.contactTel}'/>">
						</input>
					</td>
				</tr>
				<tr>
					<td>
						聯絡人傳真:
					</td>
					<td>
						<input class="easyui-textbox" type="text"  maxlength="20"
							id="<%=BimDepartmentDTO.ATTRIBUTE.CONTACT_FAX.getValue()%>"
							name="<%=BimDepartmentDTO.ATTRIBUTE.CONTACT_FAX.getValue()%>"
							value="<c:out value='${departmentManageDTO.contactFax}'/>">
						</input>
					</td>
					<td >
						聯絡人Email:
					</td>
					<td>
						<input class="easyui-textbox" type="text"  maxlength="50"
							id="<%=BimDepartmentDTO.ATTRIBUTE.CONTACT_EMAIL.getValue()%>"
							name="<%=BimDepartmentDTO.ATTRIBUTE.CONTACT_EMAIL.getValue()%>"
							data-options="validType:['email[\'聯絡人Email格式有誤，請重新輸入\']','maxLength[50]']"
							value="${departmentManageDTO.contactEmail}">
						</input>
					</td>
				</tr>
				<tr>
					<td>
						部門地址:
					</td>
					<td colspan="3">
						<cafe:droplisttag  
							id="<%=BimDepartmentDTO.ATTRIBUTE.LOCATION.getValue()%>"
							name="<%=BimDepartmentDTO.ATTRIBUTE.LOCATION.getValue()%>" 
							css="easyui-combobox"
							hasBlankValue="true" 
							result="${locationList}"
							selectedValue="${departmentManageDTO.location}"
							style="width:150px" 
							blankName="請選擇"
							javascript="editable=false ">
						</cafe:droplisttag>
						<input class="easyui-textbox" type="text"  maxlength="100"
							id="<%=BimDepartmentDTO.ATTRIBUTE.ADDRESS.getValue()%>"
							name="<%=BimDepartmentDTO.ATTRIBUTE.ADDRESS.getValue()%>"
							style="width: 280px" value="<c:out value='${departmentManageDTO.address}'/>"
							data-options="validType:'maxLength[100]'">
						</input>
					</td>
				</tr>
				<tr>
					<td>
						說明:
					</td>
					<td colspan="3">
						<textarea class="easyui-textbox"
						id="<%=BimDepartmentDTO.ATTRIBUTE.REMARK.getValue()%>"
						name="<%=BimDepartmentDTO.ATTRIBUTE.REMARK.getValue()%>"
						data-options="multiline:true,validType:'maxLength[200]'"
						style="height: 50px; width: 400px"
						maxlength="200"><c:out value='${departmentManageDTO.remark}'/></textarea>
					</td>
				</tr>
			</table>
		</form>
	</div> 
</body>
</html>