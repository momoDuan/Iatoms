<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.AssetTypeDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetTypeFormDTO"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	AssetTypeFormDTO formDTO = null;
	String ucNo = "";
	AssetTypeDTO assetTypeDTO = null;
	List<String> functionsList = null;;
	if (ctx != null) {
		formDTO = (AssetTypeFormDTO) ctx.getResponseResult();
		if(formDTO != null){
			ucNo = formDTO.getUseCaseNo();
			assetTypeDTO = formDTO.getAssetTypeDTO();
			if(assetTypeDTO != null) {
				//取出支援功能 string的set	
				Set<String> functionIds = assetTypeDTO.getFunctionIds();
				if(functionIds == null) {
					functionsList = new ArrayList<String>();
				} else {
					//轉化為array
					functionsList = new ArrayList<String>(assetTypeDTO.getFunctionIds());
				}
			} else {
				functionsList = new ArrayList<String>();
			}
		}
	}
	//設備類別list
	List<Parameter> assetCategoryList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, "ASSET_CATEGORY");
	//公司
	List<Parameter> companyList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, "COMPANY");
	//支援功能list
	List<Parameter> functionList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, "SUPPORTED_FUNCTION");
	//通訊模式
	List<Parameter> commModeList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, "COMM_MODE");
%>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="ucNo" value="<%=ucNo%>" scope="page"></c:set>
<c:set var="dto" value="<%=assetTypeDTO%>" scope="page"></c:set>
<c:set var="assetCategoryList" value="<%=assetCategoryList%>" scope="page"></c:set>
<c:set var="companyList" value="<%=companyList%>" scope="page"></c:set>
<c:set var="functionList" value="<%=functionList%>" scope="page"></c:set>
<c:set var="commModeList" value="<%=commModeList%>" scope="page"></c:set>
<c:set var="functionsList" value="<%=functionsList%>" scope="page"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>設備品項維護</title>
</head>
<body>
	<div data-options="region:'center'" style="width: auto; height: auto; padding: 10px 20px; background: #fff; overflow-y: hidden">
	<div class="dialogtitle">設備品項維護</div>
	<div><span id="dialogMsg" class="red"></span></div>
	<form id="saveForm" method="post" class="easyui-form">
		<input type="hidden" id="editAssetTypeId" name="editAssetTypeId" value="<c:out value='${formDTO.editAssetTypeId}'/>"/>
		<table cellpadding="4">
	    	<tr>
	           	<input type="easyui-textbox"id="assetTypeId" name="assetTypeId" value="<c:out value='${dto.assetTypeId}'/>" hidden='true' />
				<td>設備名稱:<span class="red">*</span></td>
				<td>
					<input class="easyui-textbox"
						name="name" 
						id="name"
						maxlength="50"
						value="<c:out value='${dto.name}'/>" 
						required="required" 
						style="width: 250px"
						data-options="validType:['maxLength[50]']"
						missingMessage="請輸入設備名稱"/>
	       	 	</td>
			</tr>
	        <tr>
	        	<td>設備廠商: </td>
	            <td>	
	            	<a href='javascript:void(0)' id='companySelectAll'>全選</a>
                    <a href='javascript:void(0)' id='companyDeselectAll'>取消全選</a>
                    <div class="div-list" style="width:270px">
                   	<select multiple="multiple"  id="companyMultipleSelect" name="companyMultipleSelect[]">
						<c:forEach items="${companyList}" var="company">
							<c:set var="select" value="${false}"/>
							<c:forEach items="${dto.companyIds}" var="cn">
								<c:if test="${cn eq company.value}">
									<c:set var="select" value="${true}"/>
								</c:if>
							</c:forEach>
							<option value="${company.value }" <c:if test="${select}">selected</c:if> >${company.name }</option>
						</c:forEach>
        			</select>
        			</div>
	            </td>
	            <td>通訊模式:</td>
	            <td >
	            	<a href='javascript:void(0)' id='commModeSelectAll'>全選</a>
	            	<a href='javascript:void(0)' id='commModeDeselectAll'>取消全選</a>
               		<select multiple="multiple"  id="commModeMultipleSelect" name="commModeMultipleSelect[]">
               			<c:forEach items="${commModeList}" var="commMode">
               				<c:set value="${false}" var="select"/>
               				<c:forEach items="${dto.commModeIds}" var="cmn">
               					<c:if test="${commMode.value == cmn }">
               						<c:set value="${true}" var="select"/>
               					</c:if>
               				</c:forEach>
               				<option value="${commMode.value}" <c:if test="${select }">selected</c:if>>${commMode.name}</option>
               			</c:forEach>
        			</select>
	            </td>
			</tr>
			<tr>
				<td>設備廠牌:</td>
               	<td>
               		<input class="easyui-textbox" 
						name="brand" 
						id="brand"
						maxlength="500"
						style="width: 250px" 
						value="<c:out value='${dto.brand}'/>"
						data-options="validType:['maxLength[500]']"/>
				</td>
				<td>設備型號:</td>
				<td>
					<input class="easyui-textbox" 
						name="model" 
						id="model"
						maxlength="500"
						style="width: 250px" 
						value="<c:out value='${dto.model}'/>"
						data-options="validType:['maxLength[500]']"/>
				</td>
			</tr>
	   		<tr>
				<td>支援功能:</td>
				<td >
					<cafe:droplistchecktag
						css="easyui-combobox"
						id="functionId"
						name="functionId" 
						result="${functionList }"
						selectedValues="${functionsList }"
						javascript="panelHeight='auto'  editable=false multiple=true"
						blankName="請選擇(複選)"
						hasBlankValue="true"
						style="width:250px">
					</cafe:droplistchecktag> 
					<%--<select class="easyui-combobox" id="functionId" name="functionId"
							 multiple="multiple" style="width: 200px" data-options="editable:false">
						<option value="">請選擇(複選)</option>
						<c:forEach items="${functionList}" var="function">
							<c:set value="${false}" var="select"/>
							<c:forEach items="${dto.functionIds}" var="fn">
								<c:if test="${function.value eq fn}">
									<c:set value="${true}" var="select"/>
								</c:if>
							</c:forEach>
							<option value="${function.value}" <c:if test="${select}">selected</c:if>>${function.name}</option>
						</c:forEach>
					</select>--%>
				</td>
				<td>設備類別:<span class="red">*</span></td>
				<td >
					<cafe:droplisttag 
						css="easyui-combobox"
						name="assetCategory"
						id="assetCategory"
						result="${assetCategoryList}"
						blankName="請選擇"
						defaultValue=""
						hasBlankValue="true"
						style="width:250px"
						javascript="panelHeight='auto' editable='false' validType=\"requiredDropList\" invalidMessage=\"請輸入設備類別\""
						selectedValue="${dto.assetCategory}"
						>
					</cafe:droplisttag>
				</td>
			</tr>
			<tr>
				<td>單位:</td>
				<td>
					<input class="easyui-textbox" 
						name="unit" 
						id="unit"
						maxlength="10"
						value="<c:out value='${dto.unit}'/>" 
						style="width: 250px"
						validType="maxLength[10]"/>
				</td>
				<td>安全庫存:</td>
				<td >
					<input class="easyui-textbox" 
						name="safetyStock" 
						id="safetyStock"
						maxlength="10"
						value="${dto.safetyStock}" 
						style="width: 250px"
						data-options="validType:['number[\'安全庫存限正整數，請重新輸入\']','maxLength[10]']"
					/>
				</td>
			</tr>
			<tr>
				<td>說明:</td>
				<td colspan="3">
					<textarea rows="4" cols="30" 
						name="remark" 
						id="remark"
						maxlength="200"
						class="easyui-textbox" 
						data-options="multiline:true" 
						style="height: 95px;width:600px"
						validType="maxLength[200]"><c:out value='${dto.remark}'/></textarea>
				</td>
			</tr>
		</table>
	</form>
<script type="text/javascript">
	$(function(){
		//設備廠商添加複選下拉框的全選與非全選
		$('#companyMultipleSelect').multiSelect();
		//設備廠商全選點擊事件
		$('#companySelectAll').click(function(){
			$('#companyMultipleSelect').multiSelect('select_all');
			return false;
		});
		//設備廠商取消全選點擊事件
		$('#companyDeselectAll').click(function(){
			$('#companyMultipleSelect').multiSelect('deselect_all');
			return false;
		});
		//設置多選框
		$('#commModeMultipleSelect').multiSelect();
		//通訊模式全選點擊事件
		$('#commModeSelectAll').click(function(){
			$('#commModeMultipleSelect').multiSelect('select_all');
			return false;
		});
		//通訊模式取消全選點擊事件
		$('#commModeDeselectAll').click(function(){
			$('#commModeMultipleSelect').multiSelect('deselect_all');
			return false;
		});
		//設置多選框的樣式大小 css
		$(".ms-container").css({"width":"260px"});
		$(".ms-list").css({"height":"130px"});
		//支援功能複選框
		$("#functionId").combobox({
			//選擇事件		
			onSelect: function (newValue) {
				selectMultiple(newValue,"functionId");
			},
			//取消選擇事件
			onUnselect : function(newValue) {
				unSelectMultiple(newValue,"functionId");
			},
		}); 
	});
</script>
</body>
</html>