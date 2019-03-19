<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.CompanyFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyTypeDTO" %>
<%@ page import="com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE"%>
<%
	//初始化加載頁面數據
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	CompanyFormDTO companyFormDTO = null;
	CompanyDTO companyDTO = null;
	List<String> companyTypes = null;
	if (ctx != null){
		companyFormDTO = (CompanyFormDTO)ctx.getResponseResult();
		if (companyFormDTO != null){
			//獲取公司DTO
			companyDTO = companyFormDTO.getCompanyDTO();
			if(companyDTO != null){
				//獲取公司類型
				companyTypes = companyDTO.getCompanyTypes();
			}
		}
	}else{
		companyFormDTO = new CompanyFormDTO();
	}
	//公司類型集合
	List<Parameter> companyTypeList = (List<Parameter>) SessionHelper.getAttribute(request,IAtomsConstants.UC_NO_BIM_02010, IATOMS_PARAM_TYPE.COMPANY_TYPE.getCode());
	//客戶DTID方式集合
	List<Parameter> dtidTypeList = (List<Parameter>) SessionHelper.getAttribute(request,IAtomsConstants.UC_NO_BIM_02010, IATOMS_PARAM_TYPE.DTID_TYPE.getCode());
	//公司地址集合
	List<Parameter> locationList = (List<Parameter>) SessionHelper.getAttribute(request,IAtomsConstants.UC_NO_BIM_02010, IATOMS_PARAM_TYPE.LOCATION.getCode());
	//登入驗證方式集合
	List<Parameter> authTypeList = (List<Parameter>) SessionHelper.getAttribute(request,IAtomsConstants.UC_NO_BIM_02010, IATOMS_PARAM_TYPE.AUTHENTICATION_TYPE.getCode());
	//是否下拉框
	List<Parameter> yesOrNoList = (List<Parameter>) SessionHelper.getAttribute(request, IAtomsConstants.UC_NO_BIM_02010, IAtomsConstants.YES_OR_NO);
%>
<c:set var="companyDTO" value="<%=companyDTO%>" scope="page"></c:set>
<c:set var="companyFormDTO" value="<%=companyFormDTO%>" scope="page"></c:set>
<c:set var="companyTypeList" value="<%=companyTypeList%>" scope="page"></c:set>
<c:set var="dtidTypeList" value="<%=dtidTypeList%>" scope="page"></c:set>
<c:set var="locationList" value="<%=locationList%>" scope="page"></c:set>
<c:set var="authTypeList" value="<%=authTypeList%>" scope="page"></c:set>
<c:set var="companyTypes" value="<%=companyTypes%>" scope="page"></c:set>
<c:set var="yesOrNoList" value="<%=yesOrNoList%>" scope="page"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="/jsp/common/easyui-common.jsp"%>
<title></title>
</head>
<body>
	<div data-options="region:'center', fit:true" 
		style="width: auto; height: auto; padding: 10px 20px; overflow-y: hidden">
		<div class="dialogtitle">公司基本訊息維護</div>
		<span id="errorMsg" class="red" style="font-weight: normal;"></span>
		<form id="fm" method="post" novalidate>
			<input type="hidden" value="<c:out value='${companyDTO.companyId}'/>" name="<%=CompanyDTO.ATTRIBUTE.COMPANY_ID.getValue()%>" id="<%=CompanyDTO.ATTRIBUTE.COMPANY_ID.getValue()%>" >
			<table cellpadding="2">
				<tr>
					<td>公司類型:<span class="red">*</span></td>
					<td>
						<div id="checkCompanyType" class="div-list" data-list-required='請輸入公司類型'>
							<cafe:checklistTag 
								name="<%=CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue()%>" 
								id="<%=CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue()%>" 
								type="checkbox"
								checkedValues="<%=companyTypes%>"
								result="${companyTypeList}"
								disabled="${not empty companyDTO}"
								javascript="onchange=controlCustomerCode()"
								>
							</cafe:checklistTag>
						</div>
					</td>
					<td>公司代號:<span class="red">*</span></td>
					<td>
						<input class="easyui-textbox" type="text" 
								value="<c:out value='${companyDTO.companyCode}'/>" 
								id="<%=CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue()%>"
								name="<%=CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue()%>" 
								maxlength = "10"
								required
								data-options="missingMessage:'請輸入公司代號 ', validType:['noChinese[\'公司代號限英文符號、英文、數字，請重新輸入\']','maxLength[10]']"
								<c:if test="${not empty companyDTO}">disabled</c:if>
								/>
					</td>
				</tr>
				<tr>
					<td>公司簡稱:<span class="red">*</span></td>
					<td>
						<input class="easyui-textbox" type="text" maxlength = "20"
								value="<c:out value='${companyDTO.shortName}'/>" 
								id="<%=CompanyDTO.ATTRIBUTE.SHORT_NAME.getValue()%>"
								name="<%=CompanyDTO.ATTRIBUTE.SHORT_NAME.getValue()%>"  
								required
								missingMessage="請輸入公司簡稱" data-options="validType:['maxLength[20]']"
								/>
					</td>
					<td>統一編號:</td>
					<td>
						<input class="easyui-textbox" type="text" maxlength = "8"
								value="${companyDTO.unityNumber}"  
								id="<%=CompanyDTO.ATTRIBUTE.UNITY_NUMBER.getValue()%>"
								name="<%=CompanyDTO.ATTRIBUTE.UNITY_NUMBER.getValue()%>" 
								data-options="validType:['englishOrNumber[\'統一編號限輸入英數字，請重新輸入\']','maxLength[8]']"
								/>
					</td>
				</tr>
				<tr>
					<td>發票抬頭:</td>
					<td>
						<input class="easyui-textbox" type="text"  id="<%=CompanyDTO.ATTRIBUTE.INVOICE_HEADER.getValue()%>"
								name="<%=CompanyDTO.ATTRIBUTE.INVOICE_HEADER.getValue()%>" data-options="validType:['maxLength[50]']"
								value="<c:out value='${companyDTO.invoiceHeader}'/>" maxlength = "50"/>
					</td>
					<td>負責人:</td>
					<td>
						<input class="easyui-textbox" type="text" maxlength = "50" id="<%=CompanyDTO.ATTRIBUTE.LEADER.getValue()%>"
								name="<%=CompanyDTO.ATTRIBUTE.LEADER.getValue()%>"  data-options="validType:['maxLength[50]']"
								value="<c:out value='${companyDTO.leader}'/>" />
					</td>
				</tr>
				<tr>
					<td>公司電話:</td>
					<td>
						<input class="easyui-textbox" type="text" maxlength = "20" id="<%=CompanyDTO.ATTRIBUTE.TEL.getValue()%>"
								name="<%=CompanyDTO.ATTRIBUTE.TEL.getValue()%>" data-options="validType:['maxLength[20]']"
								value="<c:out value='${companyDTO.tel}'/>"/>
					</td>
					<td>公司傳真:</td>
					<td>
						<input class="easyui-textbox"  type="text" maxlength = "20" id="<%=CompanyDTO.ATTRIBUTE.FAX.getValue()%>"
								name="<%=CompanyDTO.ATTRIBUTE.FAX.getValue()%>" data-options="validType:['maxLength[20]']"
								value="<c:out value='${companyDTO.fax}'/>" />
					</td>
				</tr>
				<tr>
					<td>請款日:</td>
					<td>
						<input class="easyui-textbox" type="text" maxlength = "20" data-options="validType:['maxLength[20]']"
								id="start" name="<%=CompanyDTO.ATTRIBUTE.APPLY_DATE.getValue()%>"  
								value="<c:out value='${companyDTO.applyDate}'/>" />
					</td>
					<td>付款日:</td>
					<td>
						<input class="easyui-textbox" type="text" maxlength = "20" data-options="validType:['maxLength[20]']"
                            	id="end" name="<%=CompanyDTO.ATTRIBUTE.PAY_DATE.getValue()%>"  
                            	value="<c:out value='${companyDTO.payDate}'/>" />
					</td>
				</tr>
				<tr>
					<td>聯絡人:</td>
					<td>
						<input class="easyui-textbox" type="text" maxlength = "50" id="<%=CompanyDTO.ATTRIBUTE.CONTACT.getValue()%>"
								name="<%=CompanyDTO.ATTRIBUTE.CONTACT.getValue()%>"  data-options="validType:['maxLength[50]']"
								value="<c:out value='${companyDTO.contact}'/>" />
					</td>
					<td>聯絡人電話:</td>
					<td>
						<input class="easyui-textbox" type="text" maxlength = "20" id="<%=CompanyDTO.ATTRIBUTE.CONTACT_TEL.getValue()%>"
								name="<%=CompanyDTO.ATTRIBUTE.CONTACT_TEL.getValue()%>" data-options="validType:['maxLength[20]']"
								value="<c:out value='${companyDTO.contactTel}'/>" />
					</td>
				</tr>
				<tr>
					<td>聯絡人Email:</td>
					<td>
						<input class="easyui-textbox" type="text" maxlength = "50" id="<%=CompanyDTO.ATTRIBUTE.CONTACT_EMAIL.getValue()%>"
								name="<%=CompanyDTO.ATTRIBUTE.CONTACT_EMAIL.getValue()%>" 
								value="${companyDTO.contactEmail}" 
								data-options="validType:['email[\'聯絡人Email格式有誤，請重新輸入\']', 'maxLength[50]']"
								/>
					</td>
					<td>公司Email:</td>
					<td>
						<input class="easyui-textbox" type="text"  maxlength = "255" id="<%=CompanyDTO.ATTRIBUTE.COMPANY_EMAIL.getValue()%>"
								name="<%=CompanyDTO.ATTRIBUTE.COMPANY_EMAIL.getValue()%>" 
								value="${companyDTO.companyEmail}"
								data-options="validType:['manyEmail[\'公司Email格式有誤，多筆請用分號區隔，請重新輸入\']', 'maxLength[255]']"
								/>
					</td>
				</tr>
				<tr>
					<td><label id="cusNum">客戶碼:</label></td>
					<td>
						<input class="easyui-textbox" type="text"
								id="<%=CompanyDTO.ATTRIBUTE.CUSTOMER_CODE.getValue()%>"
								name="<%=CompanyDTO.ATTRIBUTE.CUSTOMER_CODE.getValue()%>" 
								value="<c:out value='${companyDTO.customerCode}'/>"  
								disabled = "true"
								maxlength = "1" data-options="validType:['maxLength[1]']"
								missingMessage="請輸入客戶碼"
								/>
					</td>
					<td>客户DTID方式:</td>
					<td>
						<div>
							<cafe:checklistTag 
								name="<%=CompanyDTO.ATTRIBUTE.DTID_TYPE.getValue()%>" 
								id="<%=CompanyDTO.ATTRIBUTE.DTID_TYPE.getValue()%>" 
								type="radio"
								result="${dtidTypeList}"
								checkedValues='<%=StringUtils.toList((companyDTO != null && StringUtils.hasText(companyDTO.getCompanyId())) ? companyDTO.getDtidType() : IAtomsConstants.PARAM_IATOMS_DTID_TYPE_SAME, ",")%>'
								disabled="true">
							</cafe:checklistTag>
						</div>
					</td>
				</tr>
				<tr>
					<td>登入驗證方式:<span class="red">*</span></td>
					<td colspan="1">
						<div id="checkAuthenticationType" class="div-list" data-list-required='請輸入登入驗證方式'>
							<cafe:checklistTag 
								name="<%=CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue()%>" 
								id="<%=CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue()%>" 
								type="radio"
								result="${authTypeList}"
								checkedValues='<%=StringUtils.toList((companyDTO != null && StringUtils.hasText(companyDTO.getCompanyId())) ? companyDTO.getAuthenticationType() : IAtomsConstants.MARK_SPACE, ",")%>'
								javascript="onchange=controlCustomerCode()"
								disabled="${companyDTO.authenticationType != null && companyDTO.authenticationType != ''}"
								>
							</cafe:checklistTag>
						</div>
					</td>
					<td>通知AO:</td>
						<td>
							<c:if test="${not empty companyDTO }">
								<c:if test="${companyDTO.isNotifyAo eq 'Y'}">
									<cafe:checklistTag
				        	       		name="<%=CompanyDTO.ATTRIBUTE.IS_NOTIFY_AO.getValue()%>"
										id="<%=CompanyDTO.ATTRIBUTE.IS_NOTIFY_AO.getValue()%>" 
										result = "${yesOrNoList}" 
				        	       		type="radio" 
				        	       		checkedValues='<%=StringUtils.toList("Y",",") %>'
										disabled="true"/>
								</c:if>
								<c:if test="${companyDTO.isNotifyAo eq 'N'}">
									<cafe:checklistTag
				        	       		name="<%=CompanyDTO.ATTRIBUTE.IS_NOTIFY_AO.getValue()%>"
										id="<%=CompanyDTO.ATTRIBUTE.IS_NOTIFY_AO.getValue()%>" 
										result = "${yesOrNoList}" 
				        	       		type="radio" 
				        	       		checkedValues='<%=StringUtils.toList("N",",") %>'
										disabled="true"/>
								</c:if>
								<c:if test="${empty companyDTO.isNotifyAo }">
									<cafe:checklistTag
				        	       		name="<%=CompanyDTO.ATTRIBUTE.IS_NOTIFY_AO.getValue()%>"
										id="<%=CompanyDTO.ATTRIBUTE.IS_NOTIFY_AO.getValue()%>" 
										result = "${yesOrNoList}" 
				        	       		type="radio" 
										disabled="true"/>
								</c:if>
							</c:if>
							
							<c:if test="${empty companyDTO }">
									<cafe:checklistTag
				        	       		name="<%=CompanyDTO.ATTRIBUTE.IS_NOTIFY_AO.getValue()%>"
										id="<%=CompanyDTO.ATTRIBUTE.IS_NOTIFY_AO.getValue()%>" 
										result = "${yesOrNoList}" 
				        	       		type="radio" 
				        	       		checkedValues='<%=StringUtils.toList("N",",") %>'
										disabled="true"/>
							</c:if>
						</td>
				</tr>
				<tr>
					<td>公司地址:</td>
					<td colspan="3">
						<cafe:droplisttag 
							id="<%=CompanyDTO.ATTRIBUTE.ADDRESS_LOCATION.getValue()%>"
							name="<%=CompanyDTO.ATTRIBUTE.ADDRESS_LOCATION.getValue()%>"
							css="easyui-combobox"
							hasBlankValue="true"
							blankName="請選擇"
							defaultValue=""
							result="${locationList}"
							selectedValue="${companyDTO.addressLocation}"
							style="width:150px"
							javascript="editable=false"
							>
						</cafe:droplisttag>
						<input class="easyui-textbox" id="<%=CompanyDTO.ATTRIBUTE.ADDRESS.getValue()%>"
								name="<%=CompanyDTO.ATTRIBUTE.ADDRESS.getValue()%>" 
								value="<c:out value='${companyDTO.address}'/>" 
								type="text"  maxlength = "100" data-options="validType:['maxLength[100]']"
								style="width: 300px" missingMessage="請輸入公司地址"/>
					</td>
				</tr>
				<tr>
					<td>發票地址:</td>
					<td colspan="3">
						<cafe:droplisttag 
								id="<%=CompanyDTO.ATTRIBUTE.INVOICE_ADDRESS_LOCATION.getValue()%>"
								name="<%=CompanyDTO.ATTRIBUTE.INVOICE_ADDRESS_LOCATION.getValue()%>"
								css="easyui-combobox"
								hasBlankValue="true"
								blankName="請選擇"
								defaultValue=""
								result="${locationList}"
								selectedValue="${companyDTO.invoiceAddressLocation}"
								style="width: 150px"
								javascript="editable=false"
								>
						</cafe:droplisttag>
						<input class="easyui-textbox" id="<%=CompanyDTO.ATTRIBUTE.INVOICE_ADDRESS.getValue()%>"
								name="<%=CompanyDTO.ATTRIBUTE.INVOICE_ADDRESS.getValue()%>" 
								value="<c:out value='${companyDTO.invoiceAddress}'/>" 
								type="text"  maxlength = "100" data-options="validType:['maxLength[100]']" 
								style="width: 300px" missingMessage="請輸入發票地址"
								/>
					</td>
				</tr>
				<tr>
					<td >說明:</td>
					<td colspan="3">
						<textarea class="easyui-textbox" type="text" id="<%=CompanyDTO.ATTRIBUTE.REMARK.getValue()%>"
								name="<%=CompanyDTO.ATTRIBUTE.REMARK.getValue()%>" 
								maxlength = "200"
								data-options="multiline:true, validType:['maxLength[200]']" 
								style="height: 50px;width:100%"><c:out value='${companyDTO.remark}'/></textarea>
					</td>
				</tr>
			</table>
		</form>
	</div>
<script type="text/javascript">
	$(function(){
		//驗證複選框
		checkedDivListOnchange();
		//隱藏提示驗證消息
		hideDivListValidate();
		//Task #2726 通知AO  
		aoCkeckFunction();
	});
	/**
	*Task #2726 通知AO  
	*編輯時，公司類型包含客戶且為iatoms驗證時，通知ao可編輯 2017/10/31
	*/
	function aoCkeckFunction(){
		if(${not empty companyDTO}){
			//公司類型
			var companyType = getRadioValue('<%=CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue()%>');
			//登入驗證方式
			var authType = getRadioValue('<%=CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue()%>');
			var AOName = $("input[name='<%=CompanyDTO.ATTRIBUTE.IS_NOTIFY_AO.getValue()%>']"); 
			if(('<%=IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER%>' == companyType)
					&& ('<%=IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE%>' == authType)){	
				//Task #2726 通知AO 2017/10/31
				AOName.removeAttr("disabled");
			}
		}
	}
	
	/**
	 * 根據勾選的公司類型以及登入驗證方式決定客戶碼和客戶DTID方式的使用問題
	 */
	function controlCustomerCode(){
		//公司類型
		var companyType = getRadioValue('<%=CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue()%>');
		//登入驗證方式
		var authType = getRadioValue('<%=CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue()%>');
		//客戶碼
		var input = $("#<%=CompanyDTO.ATTRIBUTE.CUSTOMER_CODE.getValue()%>");
		//DTID方式
		var radio = $("input[name='<%=CompanyDTO.ATTRIBUTE.DTID_TYPE.getValue()%>']");
		//同DTID
		var same = radio.get(0); 
		//Task #2726  通知AO 2017/10/31
		var AOName = $("input[name='<%=CompanyDTO.ATTRIBUTE.IS_NOTIFY_AO.getValue()%>']"); 
		var ao = AOName.get(0); 
		for (var i = 0; i < AOName.length; i ++) {
			//若DTID方式為同DTID時
			if ('N' == AOName.eq(i).val()) {
				ao = AOName.get(i);
				break;
			}
		}
		//遍歷DTID方式獲取同DTID的勾選問題
		for (var i = 0; i < radio.length; i ++) {
			//若DTID方式為同DTID時
			if ('<%=IAtomsConstants.PARAM_IATOMS_DTID_TYPE_SAME%>' == radio.eq(i).val()) {
				same = radio.get(i);
				break;
			}
		}
		if(('<%=IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER%>' == companyType)
			&& ('<%=IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE%>' == authType)){
			//當公司類型為“客戶”且驗證方式為“IATOMS”時可編輯，有預設值
			same.checked = true;
			//啟用客戶碼
			input.textbox("enable").textbox("enableValidation");
			//修改客戶碼欄位為必輸樣式
			document.getElementById("cusNum").innerHTML="客戶碼:<span class=\"red\">*</span>";
			$('#<%=CompanyDTO.ATTRIBUTE.CUSTOMER_CODE.getValue()%>').textbox({
   								required:true,
   			});
   			//限制客戶碼輸入長度（maxLength），取消輸入的空格
   			textBoxDefaultSetting($('#customerCode'));
			//啟用DTID方式
			radio.removeAttr("disabled");
			//Task #2726 通知AO 2017/10/31
			ao.checked = true;
			AOName.removeAttr("disabled");
		} else if(('<%=IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER%>' == companyType) 
			&& (authType == '')) {
			//當公司類型為“客戶”且驗證方式為“空”時不可編輯，有預設值
			same.checked = true;
			//清空輸入框的值並將其變為不可編輯
			input.textbox("setValue","");
			input.textbox("disable").textbox("disableValidation");
			//設置客戶碼欄位的樣式為非必輸
			document.getElementById("cusNum").innerHTML="客戶碼:";
			$('#<%=CompanyDTO.ATTRIBUTE.CUSTOMER_CODE.getValue()%>').textbox({
   								required:false,
   			});
   			//禁用DTID方式
			radio.attr("disabled","true");
			//Task #2726  通知AO 2017/10/31
			ao.checked = true;
			AOName.attr("disabled","true");
		} else if(('<%=IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER%>' == companyType) 
			&& ('<%=IAtomsConstants.PARAM_COMPANY_NETWORK_AUTHENTICATION_TYPE%>' == authType)) {
			//當公司類型為“客戶”且驗證方式為“公司網域”時不可編輯，無預設值
			same.checked = false;
			//清空輸入框的值並將其變為不可編輯
			input.textbox("setValue","");
			input.textbox("disable").textbox("disableValidation");
			//設置客戶碼的樣式
			document.getElementById("cusNum").innerHTML="客戶碼:";
			$('#<%=CompanyDTO.ATTRIBUTE.CUSTOMER_CODE.getValue()%>').textbox({
   								required:false,
   			});
   			//取消同DTID的勾選，並將客戶DTID方式欄位變為不可編輯狀態
			radio.attr("checked",false);
			radio.attr("disabled","true");
			//Task #2726  通知AO 2017/10/31
			AOName.attr("checked",false);
			ao.checked = false;
			AOName.attr("disabled","true");
		} else if ((companyType == '') && ('<%=IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE%>' == authType)) {
			//當公司類型為“空”且驗證方式為“IATOMS”時不可編輯，有預設值
			same.checked = true;
			//清空輸入框的值並將其變為不可編輯
			input.textbox("setValue","");
			input.textbox("disable").textbox("disableValidation");
			//設置客戶碼的樣式
			document.getElementById("cusNum").innerHTML="客戶碼:";
			$('#<%=CompanyDTO.ATTRIBUTE.CUSTOMER_CODE.getValue()%>').textbox({
   								required:false,
   			});
			radio.attr("disabled","true");
			//Task #2726  通知AO 2017/10/31
			ao.checked = true;
			AOName.attr("disabled","true");
		} else{
			//當公司類型為“非客戶”且驗證方式為“公司網域”時不可編輯，無預設值
			same.checked = false;
			//清空輸入框的值並將其變為不可編輯
			input.textbox("setValue","");
			input.textbox("disable").textbox("disableValidation");
			//設置客戶碼的樣式
			document.getElementById("cusNum").innerHTML="客戶碼:";
			$('#<%=CompanyDTO.ATTRIBUTE.CUSTOMER_CODE.getValue()%>').textbox({
   								required:false,
   			});
			radio.attr("checked",false);
			radio.attr("disabled","true");
			//Task #2726  通知AO 2017/10/31
			ao.checked = false;
			AOName.attr("checked",false);
			AOName.attr("disabled","true");
		}
	}
	/*
	 *根據欄位獲取選中的值
	 *radioName：欄位名稱
	 */
	function getRadioValue(radioName){
		//獲取name為參數的節點
		var redioComs = $("input[name="+radioName+"]");
		var value = "";
		//循環該欄位的值獲取第一個被選中的值
		for(i=0;i<redioComs.length;i++){
		   var radio = $(redioComs[i]);
		   if(radio.is(":checked")){
			    value = radio.val();
			    break
		   }
		 }
		  return value;

	}
</script>
</body>
</html>