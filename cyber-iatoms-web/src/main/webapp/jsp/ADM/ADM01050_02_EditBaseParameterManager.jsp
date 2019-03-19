<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.BaseParameterItemDefDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.BaseParameterManagerFormDTO"%>
<%@include file="/jsp/common/common.jsp"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	BaseParameterManagerFormDTO formDTO = null;
	BaseParameterItemDefDTO baseParameterItemDefDTO = null;
	String ucNo = "";
	if (ctx != null) {
		formDTO = (BaseParameterManagerFormDTO) ctx.getResponseResult();
		if(formDTO != null){
			ucNo = formDTO.getUseCaseNo();
			baseParameterItemDefDTO = formDTO.getBaseParameterItemDefDTO();
		} else {
			formDTO = new BaseParameterManagerFormDTO();
			baseParameterItemDefDTO = new BaseParameterItemDefDTO();
			ucNo = IAtomsConstants.UC_NO_AMD_01050;
		}
	} else {
		formDTO = new BaseParameterManagerFormDTO();
		baseParameterItemDefDTO = new BaseParameterItemDefDTO();
		ucNo = IAtomsConstants.UC_NO_AMD_01050;
	}
	List<Parameter> parameterTypeList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, BaseParameterManagerFormDTO.PARAMETER_TYPE);
%>
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants" var="iatomsContants" />
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE" var="papamTypes" />
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="dto" value="<%=baseParameterItemDefDTO%>" scope="page"></c:set>
<!-- DataLoader -->
<c:set var="parameterTypeList2" value="<%=parameterTypeList%>" scope="page"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="/jsp/common/easyui-common.jsp"%>
<title>系統參數維護</title>
</head>
<body>
	<div style="width: auto; height: auto; padding: 10px 20px; overflow-y: hidden" data-options="region:'center'">
	      <div class="dialogtitle">
	      	系統參數維護
	      </div>
	      <div><span id="dialogMsg" class="red"></span></div>
	      <form id="saveForm" class="formStyle" method="post" novalidate="novalidate">
	          <table cellpadding="4">
	              <tr >
	                  <td>
	                  	參數類別:<span class="red">*</span>
	                  </td>
	                  <td >
	                  	<cafe:droplisttag 
	                      	css="easyui-combobox"
							id="bptdCodeShow"
							name="bptdCodeShow" 
							result="${parameterTypeList2}"
							disabled="${not empty dto}"
							blankName="請選擇" 
							hasBlankValue="true"
							defaultValue=""
							selectedValue="${dto.bptdCode}"
							style="width: 210px"
							javascript="editable='false' validType=\"requiredDropList\" invalidMessage=\"請輸入參數類別\" "
							>
						</cafe:droplisttag>
						<input type="hidden" id="editBpidId" name="editBpidId" value="<c:out value='${formDTO.editBpidId}'/>"/>
						<input type="hidden" id="editBptdCode" name="editBptdCode" value="<c:out value='${formDTO.editBptdCode}'/>"/>
						<input type="hidden" id="editEffectiveDate" name="editEffectiveDate" value="<c:out value='${formDTO.editEffectiveDate}'/>"/>
						<input type="hidden" id="editItemValue" name="editItemValue" value="<c:out value='${dto.itemValue}'/>"/>
	                  </td>
	              </tr>
	              <tr >
	                  <td >
	                  	參數代碼:<span class="red">*</span></td>
	                  <td >
	                  	<input class="easyui-textbox" 
	                  			id="<%=BaseParameterItemDefDTO.ATTRIBUTE.ITEM_VALUE.getValue()%>"
	                  			name="<%=BaseParameterItemDefDTO.ATTRIBUTE.ITEM_VALUE.getValue()%>" 
	                  			value="<c:out value='${dto.itemValue}'/>" 
	                  			style="width: 180px" maxlength="50"
	                  			data-options="validType:['maxLength[50]']"
	                  			required="true"
	                  			missingMessage="請輸入參數代碼"
	                  			<c:if test="${not empty dto}">disabled</c:if>
	                  			/>
	                  </td>
	                  
	                  <td >
	                  	<label>參數名稱:</label><span class="red">*</span>
	                  </td>
	                  <td >
	                  	<input class="easyui-textbox" 
	                  			id="<%=BaseParameterItemDefDTO.ATTRIBUTE.ITEM_NAME.getValue()%>"
	                  			name="<%=BaseParameterItemDefDTO.ATTRIBUTE.ITEM_NAME.getValue()%>" 
	                  			value="<c:out value='${dto.itemName }'/>" 
	                  			required="required" 
	                  			style="width: 180px" maxlength="50"
	                  			validType="maxLength[50]"
	                  			missingMessage="請輸入參數名稱"/>
	                  </td>
	              </tr>
	              <tr >
	                  <td>
	                  	<c:choose>
	                  		<c:when test="${not empty dto and (dto.bptdCode eq papamTypes.SYB_REMAIN_TIME.code or
	                 				dto.bptdCode eq papamTypes.OLD_REMAIN_TIME.code or
	                 				dto.bptdCode eq papamTypes.FREE_REMAIN_TIME.code or
	                 				dto.bptdCode eq iatomsContants.PARAM_SYSTEM_LIMIT_CODE) }">
	                 				<label id="fieldLabel">附加欄位:<span class="red">*</span></label>
	                 		</c:when>
	                 		<c:otherwise>
	                 			<label id="fieldLabel">附加欄位:</label>
	                 		</c:otherwise>
	                  	</c:choose>
	                  	
	                  </td>
	                  <td id="tempText">
	                  <c:choose>
	                 	<c:when test="${not empty dto and (dto.bptdCode eq papamTypes.SYB_REMAIN_TIME.code or
	                 				dto.bptdCode eq papamTypes.OLD_REMAIN_TIME.code or
	                 				dto.bptdCode eq papamTypes.FREE_REMAIN_TIME.code) }">
	                 		<input class="easyui-datebox"
		                  		id="<%=BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD1.getValue()%>"
		                  		name="<%=BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD1.getValue()%>"
		                  		value="<c:out value='${dto.textField1 }'/>" 
		                  		required="required" 
		                  		style="width: 180px" maxlength="7"
		                  		missingMessage="請輸入附加欄位"
		                  		data-options="validType:['validDateYearMonth','maxLength[7]']"
		                  		/>
		                 </c:when>
	                 	<c:otherwise>
	                 		<c:choose>
	                 			<c:when test="${not empty dto and dto.bptdCode eq iatomsContants.PARAM_SYSTEM_LIMIT_CODE }">
			                  		<input class="easyui-textbox" 
			                  		id="<%=BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD1.getValue()%>"
			                  		name="<%=BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD1.getValue()%>"
			                  		value="<c:out value='${dto.textField1 }'/>" 
			                  		required="required" 
			                  		style="width: 180px" maxlength="3"
			                  		missingMessage="請輸入附加欄位"
			                  		data-options="validType:['positiveInt[\'附加欄位限輸入正整數，請重新輸入\']','maxLength[3]']"
			                  		/>
			                  	</c:when>
			                  	<c:otherwise>
			                  		<input class="easyui-textbox" 
				                  		id="<%=BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD1.getValue()%>"
				                  		name="<%=BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD1.getValue()%>"
				                  		value="<c:out value='${dto.textField1 }'/>" 
				                  		style="width: 180px" maxlength="50"
				                  		validType="maxLength[50]"
			                  		/>
			                  	</c:otherwise>
	                 		</c:choose>
	                 	</c:otherwise>
	                  </c:choose>
	                  </td>
	                  <td><label>順序:</label></td>
	                  <td>
	                  	<input class="easyui-textbox" 
	                  			id="<%=BaseParameterItemDefDTO.ATTRIBUTE.ITEM_ORDER.getValue()%>"
	                  			name="<%=BaseParameterItemDefDTO.ATTRIBUTE.ITEM_ORDER.getValue()%>" 
	                  			value="${dto.itemOrder }" 
	                  			style="width: 180px" maxlength="2"
	                  			data-options="validType:['positiveInt[\'順序限輸入正整數，請重新輸入\']','maxLength[2]']"
	                  		/>
	                  </td>
	              </tr>
	              <tr style="margin-bottom: 20px">
	              	<td>備註:</td>
	                  <td colspan="3">
<%-- 	                  	<input rows="3" cols="30" 
	                  			id="<%=BaseParameterItemDefDTO.ATTRIBUTE.ITEM_DESC.getValue()%>"
	                  			name="<%=BaseParameterItemDefDTO.ATTRIBUTE.ITEM_DESC.getValue()%>" 
	                  			value="<c:out value='${dto.itemDesc}'/>" 
	                  			class="easyui-textbox" maxlength="200"
	                  			data-options="multiline:true,validType:['maxLength[200]']"
	                  			style="height: 100%; width: 350px"/> --%>
	                  			<textarea 
	                  			id="<%=BaseParameterItemDefDTO.ATTRIBUTE.ITEM_DESC.getValue()%>"
	                  			name="<%=BaseParameterItemDefDTO.ATTRIBUTE.ITEM_DESC.getValue()%>" 
	                  			class="easyui-textbox" maxlength="200"
	                  			data-options="multiline:true,validType:['maxLength[200]']"
	                  			style="height: 88px; width: 450px"><c:out value='${dto.itemDesc}'/></textarea>
	                  </td>
	              </tr>
	          </table>
	      </form>
	</div>
	<script type="text/javascript">
	
	var reportCode = new Array();
	$(function(){
		reportCode.push("${papamTypes.SYB_REMAIN_TIME.code}");
		reportCode.push("${papamTypes.OLD_REMAIN_TIME.code}");
		reportCode.push("${papamTypes.FREE_REMAIN_TIME.code}");
		//當報表名稱為“EDC報表服務需求案件(維護)分析月報”等月報時,報表日期為當前的月份
		if(${not empty dto and (dto.bptdCode eq papamTypes.SYB_REMAIN_TIME.code or dto.bptdCode eq papamTypes.OLD_REMAIN_TIME.code or dto.bptdCode eq papamTypes.FREE_REMAIN_TIME.code)}){
			createMonthDataBox('<%=BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD1.getValue()%>');
		}
		$("#bptdCodeShow").combobox({
			onChange : function(newVlaue, oldValue){
					if((reportCode.contains(newVlaue) && !reportCode.contains(oldValue)) || (!reportCode.contains(newVlaue) && reportCode.contains(oldValue))) {
						var text = $("#textField1");
						var parent = text.parent();
						text.textbox('destroy');
						parent.append("<input id='textField1' name='textField1' />");
						var text = parent.children();
						$("#<%=BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD1.getValue()%>").prop("maxlength", 7);
						if (reportCode.contains(newVlaue)) {
							text.datebox({
								editable:true,
								required:true,
								validType:'validDateYearMonth',
								missingMessage:'請輸入附加欄位',
							});
							createMonthDataBox('<%=BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD1.getValue()%>');
							$("#fieldLabel").html("附加欄位:<span class=\"red\">*</span>");
						} else {
							$("#<%=BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD1.getValue()%>").prop("maxlength", 50);
							text.textbox({
								editable:true,
								required:false,
								validType:"['maxLength[50]']",
							});
							$("#fieldLabel").html("附加欄位:");
						}
						// 限制欄位長度
						textBoxDefaultSetting($("#<%=BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD1.getValue()%>"));
					}
				if(newVlaue == '${iatomsContants.PARAM_SYSTEM_LIMIT_CODE}'){
					var textFieldOptions = $("#<%=BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD1.getValue()%>").textbox('options');
					$("#<%=BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD1.getValue()%>").prop("maxlength", 3);
					textFieldOptions.missingMessage = '請輸入附加欄位';
					textFieldOptions.required = true;
					textFieldOptions.validType = ['positiveInt[\'附加欄位限輸入正整數，請重新輸入\']','maxLength[3]'];
					$("#<%=BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD1.getValue()%>").textbox(textFieldOptions);
					// 限制欄位長度
					textBoxDefaultSetting($("#<%=BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD1.getValue()%>"));
					// 標記
					$("#fieldLabel").html("附加欄位:<span class=\"red\">*</span>");
					// 借用通知倉管
					if(oldValue == '${iatomsContants.PARAM_BORROW_ADVICE_CODE}'){
						// 借用通知倉管
						var itemValueOptions = $("#<%=BaseParameterItemDefDTO.ATTRIBUTE.ITEM_VALUE.getValue()%>").textbox('options');
						itemValueOptions.validType = ['maxLength[50]'];
						itemValueOptions.invalidMessage='';
						$("#<%=BaseParameterItemDefDTO.ATTRIBUTE.ITEM_VALUE.getValue()%>").textbox(itemValueOptions);
						// 限制欄位長度
						textBoxDefaultSetting($("#<%=BaseParameterItemDefDTO.ATTRIBUTE.ITEM_VALUE.getValue()%>"));
					}
				} else {
					if (!reportCode.contains(newVlaue)) {
						if(oldValue == '${iatomsContants.PARAM_SYSTEM_LIMIT_CODE}'){
							// 案件轉移+系統log處理
							var textFieldOptions = $("#<%=BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD1.getValue()%>").textbox('options');
							textFieldOptions.missingMessage = '';
							textFieldOptions.required = false;
							textFieldOptions.validType = ['maxLength[50]'];
							$("#<%=BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD1.getValue()%>").prop("maxlength", 50);
							$("#<%=BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD1.getValue()%>").textbox(textFieldOptions);
							// 限制欄位長度
							textBoxDefaultSetting($("#<%=BaseParameterItemDefDTO.ATTRIBUTE.TEXT_FIELD1.getValue()%>"));
							// 標記
							$("#fieldLabel").html("附加欄位:");
						} else if(oldValue == '${iatomsContants.PARAM_BORROW_ADVICE_CODE}'){
							// 借用通知倉管
							var itemValueOptions = $("#<%=BaseParameterItemDefDTO.ATTRIBUTE.ITEM_VALUE.getValue()%>").textbox('options');
							itemValueOptions.validType = ['maxLength[50]'];
							itemValueOptions.invalidMessage='';
							$("#<%=BaseParameterItemDefDTO.ATTRIBUTE.ITEM_VALUE.getValue()%>").textbox(itemValueOptions);
							// 限制欄位長度
							textBoxDefaultSetting($("#<%=BaseParameterItemDefDTO.ATTRIBUTE.ITEM_VALUE.getValue()%>"));
						}
						// 借用通知倉管
						if(newVlaue == '${iatomsContants.PARAM_BORROW_ADVICE_CODE}'){
							var itemValueOptions = $("#<%=BaseParameterItemDefDTO.ATTRIBUTE.ITEM_VALUE.getValue()%>").textbox('options');
							itemValueOptions.validType = ['email', 'maxLength[50]'];
							itemValueOptions.invalidMessage="Email格式有誤，請重新輸入";
							$("#<%=BaseParameterItemDefDTO.ATTRIBUTE.ITEM_VALUE.getValue()%>").textbox(itemValueOptions);
							// 限制欄位長度
							textBoxDefaultSetting($("#<%=BaseParameterItemDefDTO.ATTRIBUTE.ITEM_VALUE.getValue()%>"));
						}
					}
				}
			}
		})
	})
	
	</script>
</body>
</html>