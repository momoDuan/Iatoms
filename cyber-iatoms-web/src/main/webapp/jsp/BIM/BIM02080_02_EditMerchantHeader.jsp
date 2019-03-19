<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.MerchantHeaderFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.BimMerchantHeaderDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE"%>
<%@ page import="cafe.core.util.StringUtils"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	MerchantHeaderFormDTO formDTO = null;
	String useCaseNo = null;
	if (ctx != null) {
		formDTO = (MerchantHeaderFormDTO) ctx.getRequestParameter();
		useCaseNo = formDTO.getUseCaseNo();
	} else {
		formDTO = new MerchantHeaderFormDTO();
	}
	BimMerchantHeaderDTO merchantHeaderDTO = null;
	if (formDTO != null) { 
		merchantHeaderDTO = formDTO.getMerchantHeaderDTO();
	}
	if (merchantHeaderDTO == null) {
		merchantHeaderDTO = new BimMerchantHeaderDTO();
	}
	//客戶信息下拉框
	List<Parameter> customerInfoList = (List<Parameter>) SessionHelper.getAttribute(request, useCaseNo, IAtomsConstants.ACTION_GET_COMPANY_PARAMETER_LIST);
	//是否VIP下拉框
	List<Parameter> isVipList = (List<Parameter>) SessionHelper.getAttribute(request, useCaseNo, IAtomsConstants.YES_OR_NO);
	//特店區域下拉框
	List<Parameter> deptAddressList = (List<Parameter>) SessionHelper.getAttribute(request, useCaseNo, IATOMS_PARAM_TYPE.REGION.getCode());
 	//縣市下拉框
	List<Parameter> locationList = (List<Parameter>) SessionHelper.getAttribute(request, useCaseNo, IATOMS_PARAM_TYPE.LOCATION.getCode());
%>
<c:set var = "customerInfoList" value="<%= customerInfoList %>" scope="page"></c:set>
<c:set var = "isVipList" value="<%=isVipList%>" scope="page"></c:set>
<c:set var ="deptAddressList" value="<%=deptAddressList%>" scope="page"></c:set>
<c:set var = "locationList" value="<%=locationList%>" scope="page"></c:set>
<c:set var = "merchantHeaderDTO" value="<%=merchantHeaderDTO%>" scope="page"></c:set>
<c:set var = "formDTO" value="<%=formDTO%>" scope="page"></c:set>
<html>
<head>
	 <%@include file="/jsp/common/easyui-common.jsp"%>
</head>
<body>
	<div data-options="region:'center'"
		style="width: auto; height: auto; padding: 10px 20px; background: #fff; overflow-y: hidden">
       <div class="dialogtitle">特店表頭維護</div>
            <div><span id="dialogMsg" class="red"></span></div>
            <form id="edit_Form_Merchant_Header" method="post">
                <table cellpadding="4">
                    <tr>
                        <td>客戶:<span class="red">*</span></td>
                        <td>
                        <c:if test="${formDTO.actionId eq 'initEdit' && empty merchantHeaderDTO.merchantHeaderId}">
	               		  	 <cafe:droplisttag 
	                             id="<%=BimMerchantHeaderDTO.ATTRIBUTE.COMPANY_ID.getValue()%>"
	                             name="<%=BimMerchantHeaderDTO.ATTRIBUTE.COMPANY_ID.getValue()%>" 
	                             css="easyui-combobox"
	                             selectedValue="${empty merchantHeaderDTO.companyId ? ((formDTO.roleAttribute eq 'CUSTOMER_VENDOR')?logonUser.companyId:''): merchantHeaderDTO.companyId}"
	                             result="${customerInfoList}"
	                             hasBlankValue="true"
	                             blankName="請選擇" 
	                             style="width: 184px" 
	                             disabled="${(formDTO.roleAttribute eq 'CUSTOMER_VENDOR')?true:false}"
	                             javascript="validType=\"requiredDropList\" editable=false required=true invalidMessage=\"請輸入客戶\"">
	                         </cafe:droplisttag>
                         </c:if>
                          <c:if test="${formDTO.actionId eq 'initEdit' && not empty merchantHeaderDTO.merchantHeaderId}">
                            	 <cafe:droplisttag 
		                         id="<%=BimMerchantHeaderDTO.ATTRIBUTE.COMPANY_ID.getValue()%>"
		                         name="<%=BimMerchantHeaderDTO.ATTRIBUTE.COMPANY_ID.getValue()%>" 
		                         css="easyui-combobox"
		                         selectedValue="${merchantHeaderDTO.companyId}"
		                         result="${customerInfoList}"
		                         hasBlankValue="true"
		                         blankName="" 
		                         style="width: 184px" 
		                         javascript="required=true disabled=disabled">
	                         </cafe:droplisttag>
                            </c:if>
                         <c:if test="${formDTO.actionId eq 'initAdd'}">
	                         <cafe:droplisttag 
		                         id="<%=BimMerchantHeaderDTO.ATTRIBUTE.COMPANY_ID.getValue()%>"
		                         name="<%=BimMerchantHeaderDTO.ATTRIBUTE.COMPANY_ID.getValue()%>" 
		                         css="easyui-combobox"
		                         selectedValue="${merchantHeaderDTO.companyId}"
		                         result="${customerInfoList}"
		                         hasBlankValue="true"
		                         blankName="" 
		                         style="width: 184px" 
		                         javascript="required=true disabled=disabled">
	                         </cafe:droplisttag>
                         </c:if>
                        </td>
                        <td>特店代號:<span class="red">*</span></td>
                        <td>
                            <input id="<%=BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_CODE.getValue()%>"
                             name="<%=BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_CODE.getValue()%>"
                             class="easyui-textbox" 
                             maxlength="20" 
                             type="text" 
                             style="width: 184px" 
	                         value="<c:out value='${merchantHeaderDTO.merchantCode}'/>" 
                            <c:if test="${formDTO.actionId eq 'initEdit' && empty merchantHeaderDTO.merchantHeaderId}">
                            	data-options="required:true,buttonIcon:'icon-search',missingMessage:'請輸入特店代號',validType:['englishOrNumber[\'特店代號限輸入英數字，請重新輸入\']','maxLength[20]'],onClickButton:check" 
                            </c:if>
                            <c:if test="${formDTO.actionId eq 'initEdit' && not empty merchantHeaderDTO.merchantHeaderId}">
                            	disabled=disabled
                            </c:if>
                            <c:if test="${formDTO.actionId eq 'initAdd'}">
                           		disabled=disabled
                            </c:if>
                            />
                        	<input name="<%=BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_HEADER_ID.getValue()%>"  id="<%=BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_HEADER_ID.getValue()%>" type="hidden"  value="<c:out value='${merchantHeaderDTO.merchantHeaderId}'/>" />
                        	<input name="<%=BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_ID.getValue()%>" id="<%=BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_ID.getValue()%>" type="hidden"  value="<c:out value='${merchantHeaderDTO.merchantId}'/>" />
                        	<input name="tempMerchant" id="tempMerchant" type="hidden"  value="<c:out value='${merchantHeaderDTO.merchantCode}'/>" />
                        	<input id="isAssetManage" type="hidden"></input>
                        </td>
                    </tr>
                    <tr>
                        <td>特店名稱:<span class="red">*</span></td>
                        <td>
                            <input id="<%=BimMerchantHeaderDTO.ATTRIBUTE.NAME.getValue()%>"
                            name="<%=BimMerchantHeaderDTO.ATTRIBUTE.NAME.getValue()%>" 
                            value="<c:out value='${merchantHeaderDTO.name}'/>"
                            style="width: 184px" 
                            class="easyui-textbox" type="text" 
                            data-options="required:true,missingMessage:'請輸入特店名稱',disabled:true"
                            validType="maxLength[100]" >
                            </input>
                        </td>
                        <td>VIP:<span class="red">*</span></td>
	        	         <td>
		        	       <cafe:checklistTag 
		        	       		id="<%=BimMerchantHeaderDTO.ATTRIBUTE.IS_VIP.getValue()%>"
	                            name="<%=BimMerchantHeaderDTO.ATTRIBUTE.IS_VIP.getValue()%>" 
								result="${isVipList}"
		        	       		type="radio" 
		        	       		checkedValues='<%= StringUtils.toList(StringUtils.hasText(merchantHeaderDTO.getIsVip())?merchantHeaderDTO.getIsVip():"N", ",")%>'
		        	       		/>
	        	         </td>
                    </tr>
                    <tr>
                    	 
                        <td>表頭（同對外名稱）:<span class="red">*</span></td>
                        <td>
                           <input name="<%=BimMerchantHeaderDTO.ATTRIBUTE.HEADER_NAME.getValue()%>" 
                            id="<%=BimMerchantHeaderDTO.ATTRIBUTE.HEADER_NAME.getValue()%>" 
                            class="easyui-textbox" 
                            maxlength="100" 
                            value="<c:out value='${merchantHeaderDTO.headerName}'/>" 
                            type="text" 
                            style="width: 184px" 
                            data-options="required:true,missingMessage:'請輸入表頭（同對外名稱）'"
                            validType="maxLength[100]">
                          	</input>
                        </td>
                        <td><label id="areaHtml">特店區域:<span class="red">*</span></label></td>
                        <td>
                           <cafe:droplisttag 
	                            id="<%=BimMerchantHeaderDTO.ATTRIBUTE.AREA.getValue()%>"
	                            name="<%=BimMerchantHeaderDTO.ATTRIBUTE.AREA.getValue()%>" 
	                            css="easyui-combobox"
	                            selectedValue="${merchantHeaderDTO.area}"
	                            result="${deptAddressList}" 
	                            hasBlankValue="true"
	                            blankName="請選擇">
	                           
                           </cafe:droplisttag>
                           </td>
                    </tr>
                    <tr>
                        <td>特店聯絡人:</td>
                        <td>
                             <input name="<%=BimMerchantHeaderDTO.ATTRIBUTE.CONTACT.getValue()%>"
                              maxlength="50" 
                              validType="maxLength[50]"
                               style="width: 184px"  
                              class="easyui-textbox" 
                              type="text" 
                              value="<c:out value='${merchantHeaderDTO.contact}'/>"></input>
                        </td>
                        <td>特店聯絡人電話1:</td>
                        <td>
                            <input name="<%=BimMerchantHeaderDTO.ATTRIBUTE.CONTACT_TEL.getValue()%>" 
                            class="easyui-textbox"
                            maxlength="20" 
							type="text"
							style="width: 184px" 
							value="<c:out value='${merchantHeaderDTO.contactTel}'/>"
							data-options="validType:['maxLength[20]']"></input>
                        </td>
                    </tr>
                    <tr>
                        <td>特店聯絡人電話2:</td>
                        <td>
                            <input name="<%=BimMerchantHeaderDTO.ATTRIBUTE.CONTACT_TEL2.getValue()%>" 
                            class="easyui-textbox"
                            maxlength="20" 
							type="text"
							style="width: 184px" 
							value="<c:out value='${merchantHeaderDTO.contactTel2}'/>"
							data-options="validType:['maxLength[20]']"></input>
                        </td>
                        <td>行動電話:</td>
                        <td>
                            <input name="<%=BimMerchantHeaderDTO.ATTRIBUTE.PHONE.getValue()%>" 
                            class="easyui-textbox"
                            maxlength="10" 
                            style="width: 184px" 
							type="text"
							value="${merchantHeaderDTO.phone}"
							data-options="validType:['maxLength[10]','twMobile']"
                            >
							</input>
                        </td>
                    </tr>
                    <tr>
                    	<td>聯絡人Email:</td>
                    	<td>
							<input name="<%=BimMerchantHeaderDTO.ATTRIBUTE.CONTACT_EMAIL.getValue()%>" 
	                            id="<%=BimMerchantHeaderDTO.ATTRIBUTE.CONTACT_EMAIL.getValue()%>" 
	                            class="easyui-textbox" 
	                            maxlength="50" 
	                            style="width: 184px" 
	                            data-options="validType:['maxLength[50]','email'],invalidMessage:'聯絡人Email格式有誤，請重新輸入'" 
	                            type="text" 
	                            value="${merchantHeaderDTO.contactEmail}">
							</input>
                        </td>
                    </tr>
                    <tr>
                        <td>營業地址:</td>
                         <td colspan="3">
                         	<span>
	                           <cafe:droplisttag 
		                            id="<%=BimMerchantHeaderDTO.ATTRIBUTE.LOCATION.getValue()%>"
		                            name="<%=BimMerchantHeaderDTO.ATTRIBUTE.LOCATION.getValue()%>" 
		                            css="easyui-combobox"
		                            selectedValue="${merchantHeaderDTO.location}"
		                            result="${locationList}"
		                            hasBlankValue="true"
		                            blankName="縣市"
		                            style="width: 120px"
		                            javascript="editable=false">
	                           </cafe:droplisttag>
                           </span>
                           <span>
                           		<cafe:droplisttag 
	                            id="<%=BimMerchantHeaderDTO.ATTRIBUTE.POST_CODE_ID.getValue()%>"
	                            name="<%=BimMerchantHeaderDTO.ATTRIBUTE.POST_CODE_ID.getValue()%>" 
	                            css="easyui-combobox"
	                            selectedValue="${merchantHeaderDTO.postCodeId}"
	                            hasBlankValue="true"
	                            style="width: 120px" 
	                            blankName="郵遞區號"
	                            javascript="editable=false data-options=\"valueField:'value',textField:'name',validType:'requiredDropList'\"">
                           </cafe:droplisttag>
                           </span>
                           <span>
	                            <input name="<%=BimMerchantHeaderDTO.ATTRIBUTE.BUSINESS_ADDRESS.getValue()%>"
	                             validType="maxLength[100]" class="easyui-textbox" 
	                             maxlength="100" 
	                             type="text" 
	                             style="width: 265px" 
	                             value="<c:out value='${merchantHeaderDTO.businessAddress}'/>"></input>
                            </span>
                        </td>
                    </tr>
                    <tr>
                        <td>營業時間起:</td>
                        <td>
                            <input id="<%=BimMerchantHeaderDTO.ATTRIBUTE.OPEN_HOUR.getValue()%>" 
                            name="<%=BimMerchantHeaderDTO.ATTRIBUTE.OPEN_HOUR.getValue()%>" 
                            maxlength="5"
                            type="text" 
                            class="easyui-timespinner" 
                            style="width: 184px" 
                            data-options="validType:['maxLength[5]','compareMerchantTime']" 
                            value="${merchantHeaderDTO.openHour}">
                            </input>
                        </td>
                        <td>營業時間迄:</td>
                        <td>
                             <input id="<%=BimMerchantHeaderDTO.ATTRIBUTE.CLOSE_HOUR.getValue()%>" 
                             name="<%=BimMerchantHeaderDTO.ATTRIBUTE.CLOSE_HOUR.getValue()%>"
                             maxlength="5"  
                             type="text" 
                             class="easyui-timespinner" 
                             style="width: 184px" 
                             data-options="validType:['maxLength[5]','compareMerchantTime','compareTimeSizeInMerchantHeader[\'#<%=BimMerchantHeaderDTO.ATTRIBUTE.OPEN_HOUR.getValue()%>\']']" 
                             value="${merchantHeaderDTO.closeHour}" >
                             </input>
                        </td>
                    </tr>
                    <tr>
                        <td>AO人員:</td>
                        <td>
                           <input name="<%=BimMerchantHeaderDTO.ATTRIBUTE.AO_NAME.getValue()%>" 
                           id="<%=BimMerchantHeaderDTO.ATTRIBUTE.AO_NAME.getValue()%>" 
                           class="easyui-textbox" 
                           validType="maxLength[50]" 
                           maxlength="50" 
                           style="width: 184px" 
                           type="text" 
                           value="<c:out value='${merchantHeaderDTO.aoName}'/>"></input>
                        </td>
                        <td>AOEmail:</td>
                        <td>
                            <input name="<%=BimMerchantHeaderDTO.ATTRIBUTE.AO_EMAIL.getValue()%>" 
                            id="<%=BimMerchantHeaderDTO.ATTRIBUTE.AO_EMAIL.getValue()%>" 
                            class="easyui-textbox" 
                            maxlength="50" 
                            style="width: 184px" 
                            data-options="validType:['maxLength[50]','email'],invalidMessage:'AOEmail格式有誤，請重新輸入'" 
                            type="text" 
                            value="${merchantHeaderDTO.aoemail}"></input>
                        </td>
                    </tr>
                </table>
                
               </form>
               </div>
	 <script type="text/javascript">
	$(function(){
		$('#<%=BimMerchantHeaderDTO.ATTRIBUTE.COMPANY_ID.getValue()%>').combobox({
				onChange: function(newValue, oldValue){
					$('#<%=BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_CODE.getValue()%>').textbox("setValue", '');
					$('#<%=BimMerchantHeaderDTO.ATTRIBUTE.NAME.getValue()%>').textbox("setValue", '');
					$("#tempMerchant").val('');
				}
		});
		$('#<%=BimMerchantHeaderDTO.ATTRIBUTE.LOCATION.getValue()%>').combobox({
				onChange: function(newValue, oldValue){
					$('#<%=BimMerchantHeaderDTO.ATTRIBUTE.POST_CODE_ID.getValue()%>').combobox("loadData", initSelect(null,'郵遞區號'));
					$('#<%=BimMerchantHeaderDTO.ATTRIBUTE.POST_CODE_ID.getValue()%>').combobox("setValue", '郵遞區號');
					if(newValue != ""){
						ajaxService.getPostCodeList(newValue,function(data){
							if (data != null) {
								$('#<%=BimMerchantHeaderDTO.ATTRIBUTE.POST_CODE_ID.getValue()%>').combobox("loadData",initSelect(data,'郵遞區號'));
							}
						});
					}
				}
		});
		//Task #3481 若有顯示信息，則加載對應的郵遞區號下拉選
		var location = $('#<%=BimMerchantHeaderDTO.ATTRIBUTE.LOCATION.getValue()%>').combobox("getValue");
		if(location != null || location != ''){
			ajaxService.getPostCodeList(location,function(data){
				if (data != null) {
					$('#<%=BimMerchantHeaderDTO.ATTRIBUTE.POST_CODE_ID.getValue()%>').combobox("loadData",initSelect(data,'郵遞區號'));
					$('#<%=BimMerchantHeaderDTO.ATTRIBUTE.POST_CODE_ID.getValue()%>').combobox("setValue",'${merchantHeaderDTO.postCodeId}');
				}
			});
		}
	})
	//特店代號檢測
	function check() {
		//特店待號
   		var merchantCode = $('#<%=BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_CODE.getValue()%>').textbox('getValue');
   		//客戶id
   		var customerId = $('#<%=BimMerchantHeaderDTO.ATTRIBUTE.COMPANY_ID.getValue()%>').combobox('getValue');
 		var controls = ['companyId', 'merchantCode'];
		if (!validateForm(controls)) {
			return;
		}
   		if (isEmpty(merchantCode)||isEmpty(customerId)) {
   			$("#dialogMsg").text("");
   			return $("#editForm").form('validate');
   		} 
   		//要檢驗的值
		var checkParam = {
			//特店代號
			merchantCode : merchantCode,
			//公司id
			customerId : customerId
		};
        var url = "${contextPath}/merchantHeader.do?actionId=<%=IAtomsConstants.ACTION_GET_MERCHANT_INFO%>"; 
   		$.ajax({
            url : url,
            data :checkParam,
            success : function(data) { 
           		$("#dialogMsg").text("");
	    		if (data.row != undefined) { 
	    			$("#tempMerchant").val(merchantCode);
	    			//表頭設置內容
	    			$('#<%=BimMerchantHeaderDTO.ATTRIBUTE.NAME.getValue()%>').textbox('setValue', data.row.name);
		    		//特店id
		    		$('#<%=BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_ID.getValue()%>').val(data.row.merchantId);
	    		}else{ 
	    			$('#<%=BimMerchantHeaderDTO.ATTRIBUTE.NAME.getValue()%>').textbox('setValue', '');
	    			$('#<%=BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_CODE.getValue()%>').textbox('setValue', '');
	    			$("#tempMerchant").val('');
	    		} 
	    		$("#dialogMsg").text(data.msg);
			}, 
			error : function() { 
				$.messager.alert('提示', "檢索失敗", 'error');							
			} 
        }); 
     }
	 </script>
</body>
</html>