<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ include file="/jsp/common/easyui-common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractSlaFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.ContractSlaDTO"%>
<%
	SessionContext ctx = (SessionContext)request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	ContractSlaFormDTO contractSlaFormDTO = null;
	ContractSlaDTO contractSlaDTO = null;
	String ucNo = null;
	if (ctx != null) {
		// 得到FormDTO
		contractSlaFormDTO = (ContractSlaFormDTO) ctx.getRequestParameter();
		if (contractSlaFormDTO == null) {
			ucNo = IAtomsConstants.UC_NO_BIM_02040;
			contractSlaFormDTO = new ContractSlaFormDTO();
		} else {
			// 获得UseCaseNo
			ucNo = contractSlaFormDTO.getUseCaseNo();
			// 得到DTO
			contractSlaDTO = contractSlaFormDTO.getContractSlaDTO();
			if (contractSlaDTO == null){
				contractSlaDTO = new ContractSlaDTO();
			}
		}
	} else {
		ucNo = IAtomsConstants.UC_NO_BIM_02040;
		contractSlaFormDTO = new ContractSlaFormDTO();
		contractSlaDTO = new ContractSlaDTO();
	}
	// 案件類別列表下拉框
	List<Parameter> ticketTypeList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.TICKET_TYPE.getCode());
	// 客戶列表下拉框
	List<Parameter> customerList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_CUSTOMER_LIST);
	// 地區列表下拉框
	List<Parameter> locationList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.REGION.getCode());
	// 案件類型列表下拉框
	List<Parameter> ticketModeList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, ContractSlaFormDTO.PARAM_TICKET_MODE_LIST);
	// 是否上班日/当天件下拉框
	List<Parameter> yesOrNoList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.YES_OR_NO.getCode());
%>
 
<c:set var="ticketTypeList" value="<%=ticketTypeList%>" scope="page"></c:set>
<c:set var="customerList" value="<%=customerList%>" scope="page"></c:set>
<c:set var="locationList" value="<%=locationList%>" scope="page"></c:set>
<c:set var="ticketModeList" value="<%=ticketModeList%>" scope="page"></c:set>
<c:set var="yesOrNoList" value="<%=yesOrNoList%>" scope="page"></c:set>
<c:set var="contractSlaDTO" value="<%=contractSlaDTO%>" scope="page"></c:set>
<c:set var="contractSlaFormDTO" value="<%=contractSlaFormDTO%>" scope="page"></c:set>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
<script type="text/javascript">
$(function(){
	getContractByCustomer();
	/*
	* 當天件選擇結果
	*/
	 $('#<%=ContractSlaDTO.ATTRIBUTE.IS_THAT_DAY.getValue()%>').combobox({
		onChange: function(){
			// 拿到當天件下拉框的值
			var isThatDay = $('#<%=ContractSlaDTO.ATTRIBUTE.IS_THAT_DAY.getValue()%>').combobox('getValue');
			var thatDayTimeOptions = $("#<%=ContractSlaDTO.ATTRIBUTE.THAT_DAY_TIME.getValue()%>").timespinner('options');
			if(isThatDay == '<%=IAtomsConstants.YES%>'){
				// 選擇是對時間框的影響
				<%-- $('#<%=ContractSlaDTO.ATTRIBUTE.THAT_DAY_TIME.getValue()%>').timespinner({
						required: true,
						disabled: false
				}); --%>
				thatDayTimeOptions.invalidMessage='當天件建案時間限 格式：HH:mm,24小時制';
				thatDayTimeOptions.validType = ['compareMerchantTime'];
				thatDayTimeOptions.required=true;
				thatDayTimeOptions.disabled=false;
			} else {
				// 選擇否對時間框的影響
				<%-- $('#<%=ContractSlaDTO.ATTRIBUTE.THAT_DAY_TIME.getValue()%>').timespinner({
					disabled: true
				}).timespinner('setValue',''); --%>
				thatDayTimeOptions.invalidMessage='';
				thatDayTimeOptions.validType = [];
				thatDayTimeOptions.disabled=true;
				thatDayTimeOptions.value='';
			}
			$("#<%=ContractSlaDTO.ATTRIBUTE.THAT_DAY_TIME.getValue()%>").timespinner(thatDayTimeOptions);
			textBoxDefaultSetting($('#<%=ContractSlaDTO.ATTRIBUTE.THAT_DAY_TIME.getValue()%>'));
		}
	}); 
	/*
	* 合同編號根據客戶下拉框選值聯動
	*/
	 $('#<%=ContractSlaDTO.ATTRIBUTE.CUSTOMER_ID.getValue()%>').combobox({
  		onChange: function(){
  			$('#<%=ContractSlaDTO.ATTRIBUTE.CONTRACT_ID.getValue()%>').combobox('setValue','');
  			// 拿到客户编号
  			var customerId = $('#<%=ContractSlaDTO.ATTRIBUTE.CUSTOMER_ID.getValue()%>').combobox('getValue');
  			var status = '<%=IAtomsConstants.PARAM_CONTRACT_STATUS_IN_EFFECT%>';
  			if(!isEmpty(customerId)){
  				// 根据客户编号得到该客户的所有合约编号
 				ajaxService.getContractCodeList(customerId, status, function(data){
 					// 设置默认值
 					var data = initSelect(data);
 					// 将结果放到合约编号下拉框
 					$('#<%=ContractSlaDTO.ATTRIBUTE.CONTRACT_ID.getValue()%>').combobox('loadData',data);
 				}); 
 			} else {
 				var data = initSelect();
 				$('#<%=ContractSlaDTO.ATTRIBUTE.CONTRACT_ID.getValue()%>').combobox('loadData',data);
 			} 
  		}
  	});
}); 
	/**
	 * 得到客戶名下合約編號
	 */
	function getContractByCustomer(){
		var companyId = '${contractSlaDTO.customerId}';
		var contractId = '${contractSlaDTO.contractId}';
		var status = '<%=IAtomsConstants.PARAM_CONTRACT_STATUS_IN_EFFECT%>';
		if(!isEmpty(companyId)){
			ajaxService.getContractCodeList(companyId, status, function(data){
				// 處理合約編號可能刪除的情況
				var flag = false;
				for(var i = 0; i < data.length; i++){
					if(data[i].value == contractId){
						flag = true;
						break;
					}
				}
				// 设置默认值
				var data = initSelect(data);
				// 将结果放到合约编号下拉框
				$('#<%=ContractSlaDTO.ATTRIBUTE.CONTRACT_ID.getValue()%>').combobox('loadData',data);
				if(flag){
					$('#<%=ContractSlaDTO.ATTRIBUTE.CONTRACT_ID.getValue()%>').combobox('select',contractId);
				}
			}); 
		}
	}
	/*
	* 當天件否當天件建案時間禁用
	*/
	function setTimespinnerDisabled(){
		var isThatDay = '${contractSlaDTO.isThatDay}';
		var thatDayTimeOptions = $("#<%=ContractSlaDTO.ATTRIBUTE.THAT_DAY_TIME.getValue()%>").timespinner('options');
		if(isThatDay == '<%=IAtomsConstants.YES%>'){
			// 選擇否對時間框的影響
			<%-- $('#<%=ContractSlaDTO.ATTRIBUTE.THAT_DAY_TIME.getValue()%>').timespinner({
				disabled: true
			}).timespinner('setValue',''); --%>
			
			thatDayTimeOptions.invalidMessage='當天件建案時間限 格式：HH:mm,24小時制';
			thatDayTimeOptions.validType = ['compareMerchantTime'];
			thatDayTimeOptions.required=true;
			thatDayTimeOptions.disabled=false;
		} else {
			thatDayTimeOptions.disabled=true;
			thatDayTimeOptions.value='';
		}
		$("#<%=ContractSlaDTO.ATTRIBUTE.THAT_DAY_TIME.getValue()%>").timespinner(thatDayTimeOptions);
		textBoxDefaultSetting($('#<%=ContractSlaDTO.ATTRIBUTE.THAT_DAY_TIME.getValue()%>'));
	}
</script>
<!-- <div data-options="region:'center'" style="width: auto; height: auto; padding: 10px 20px; background: #fff; overflow-y: hidden"> -->
<div style="width: auto; height: auto; padding: 10px 20px; overflow-y: hidden" data-options="region:'center'">
          <div class="dialogtitle">
	      	合約SLA設定
	      </div>
            <span id="saveDlgMsg" class="red"></span>
            <form id="saveForm" class="formStyle" method="post" novalidate>
            	<table cellpadding="4">
                    <tr>
                        <td style="width: 90px">客戶:<span class="red">*</span></td>
                        <td>
							<c:choose>
	                        	<c:when test="${contractSlaFormDTO.actionId eq 'initAdd' }">
	                        		<cafe:droplisttag css="easyui-combobox" id="<%=ContractSlaDTO.ATTRIBUTE.CUSTOMER_ID.getValue() %>"
										name="<%=ContractSlaDTO.ATTRIBUTE.CUSTOMER_ID.getValue() %>" result="${customerList}"
										hasBlankValue="true" blankName="請選擇"  style="width: 120px"
										javascript="editable='false' validType=\"ignore['請選擇']\" invalidMessage=\"請輸入客戶\" required=true missingMessage=\"請輸入客戶\"">
									</cafe:droplisttag>
	                        	</c:when>
	                        	<c:otherwise>
	                        		<cafe:droplisttag css="easyui-combobox" result="${customerList}" 
		                     	    	id="<%=ContractSlaDTO.ATTRIBUTE.CUSTOMER_ID.getValue() %>" name="<%=ContractSlaDTO.ATTRIBUTE.CUSTOMER_ID.getValue() %>"
										selectedValue="${contractSlaDTO.customerId}" disabled="true" style="width: 120px"
										hasBlankValue="true" blankName="請選擇" >
									</cafe:droplisttag>
									<%-- <input type="hidden" id="<%=ContractSlaDTO.ATTRIBUTE.CUSTOMER_ID.getValue() %>" name="<%=ContractSlaDTO.ATTRIBUTE.CUSTOMER_ID.getValue() %>" value="${contractSlaDTO.customerId}"> --%>
	                        	</c:otherwise>
	                        </c:choose>
						</td>
						<td >合約編號:<span class="red">*</span></td>
						<td>
							<cafe:droplisttag css="easyui-combobox" id="<%=ContractSlaDTO.ATTRIBUTE.CONTRACT_ID.getValue()%>"
								name="<%=ContractSlaDTO.ATTRIBUTE.CONTRACT_ID.getValue()%>" 
								selectedValue="${contractSlaDTO.contractId}"
								style="width: 120px"
								hasBlankValue="true" blankName="請選擇" 
								javascript="editable='false' validType=\"ignore['請選擇']\" required=true valueField='value' textField='name' missingMessage=\"請輸入合約編號\" invalidMessage=\"請輸入合約編號\"">
							</cafe:droplisttag>
						</td>
                    </tr>
                    <tr>
                        <td>案件類別:<span class="red">*</span></td>
                        <td>
								<cafe:droplisttag css="easyui-combobox" id="<%=ContractSlaDTO.ATTRIBUTE.TICKET_TYPE.getValue() %>"
									name="<%=ContractSlaDTO.ATTRIBUTE.TICKET_TYPE.getValue() %>" result="${ticketTypeList}"
									selectedValue="${contractSlaDTO.ticketType}" disabled="false"
									hasBlankValue="true" blankName="請選擇" style="width: 120px"
									javascript="editable='false' panelheight=\"auto\" validType=\"ignore['請選擇']\" invalidMessage=\"請輸入案件類別\" required=true missingMessage=\"請輸入案件類別\"">
								</cafe:droplisttag>
						</td>
                        <td>區域:<span class="red">*</span></td>
                        <td>
								<cafe:droplisttag css="easyui-combobox" id="<%=ContractSlaDTO.ATTRIBUTE.LOCATION.getValue() %>"
									name="<%=ContractSlaDTO.ATTRIBUTE.LOCATION.getValue() %>" result="${locationList}"
									selectedValue="${contractSlaDTO.location}" disabled="false"
									hasBlankValue="true" blankName="請選擇" style="width: 120px"
									javascript="editable='false' panelheight=\"auto\" validType=\"ignore['請選擇']\" invalidMessage=\"請輸入區域\" required=true missingMessage=\"請輸入區域\"">
								</cafe:droplisttag>
						</td>
                    </tr>
                    <tr>
                        <td>案件類型:<span class="red">*</span></td>
                        <td >
								<cafe:droplisttag css="easyui-combobox" id="<%=ContractSlaDTO.ATTRIBUTE.TICKET_MODE.getValue() %>"
									name="<%=ContractSlaDTO.ATTRIBUTE.TICKET_MODE.getValue() %>" result="${ticketModeList}"
									selectedValue="${contractSlaDTO.ticketMode}" disabled="false"
									hasBlankValue="true" blankName="請選擇" style="width: 120px"
									javascript="editable='false' panelheight=\"auto\" validType=\"ignore['請選擇']\" invalidMessage=\"請輸入案件類型\" required=true missingMessage=\"請輸入案件類型\"">
								</cafe:droplisttag>
                   		</td>
                   		<td>上班日:<span class="red">*</span></td>
                        <td>
							<cafe:droplisttag css="easyui-combobox" id="<%=ContractSlaDTO.ATTRIBUTE.IS_WORK_DAY.getValue() %>"
								name="<%=ContractSlaDTO.ATTRIBUTE.IS_WORK_DAY.getValue() %>" result="${yesOrNoList}"
								selectedValue="${contractSlaDTO.isWorkDay}" disabled="false"
								hasBlankValue="true" blankName="請選擇" 
								 style="width: 120px"
								javascript="editable='false' panelheight=\"auto\" validType=\"ignore['請選擇']\" invalidMessage=\"請輸入上班日\" required=true missingMessage=\"請輸入上班日\"">
							</cafe:droplisttag> 
                   		</td>
                    </tr>
                    <tr>
                        <td>當天件:<span class="red">*</span></td>
                        <td colspan="3">
                        <cafe:droplisttag css="easyui-combobox" id="<%=ContractSlaDTO.ATTRIBUTE.IS_THAT_DAY.getValue() %>"
							name="<%=ContractSlaDTO.ATTRIBUTE.IS_THAT_DAY.getValue() %>" result="${yesOrNoList}"
							disabled="false" selectedValue="${contractSlaDTO.isThatDay}"
							hasBlankValue="true" blankName="請選擇" style="width: 80px"
							javascript="editable='false' validType=\"ignore['請選擇']\" panelheight=\"auto\" invalidMessage=\"請輸入當天件\" required=true missingMessage=\"請輸入當天件\"">
						</cafe:droplisttag>
                        <input class="easyui-timespinner" maxlength="5" type="text" style="width: 80px" missingMessage="請輸入當天件建案時間" id="<%=ContractSlaDTO.ATTRIBUTE.THAT_DAY_TIME.getValue() %>" 
                        name="<%=ContractSlaDTO.ATTRIBUTE.THAT_DAY_TIME.getValue() %>" value = "${contractSlaDTO.thatDayTime}" ${contractSlaFormDTO.actionId eq 'initAdd' ? 'disabled':''}></input>
                        <span class="red">當天件為'是'時，到場時效、完修時效以24小時為單位。</span>
                        </td>
                    </tr>
                    <tr>
                        <td>回應時效:</td>
                        <td>
                        	<input class="easyui-textbox" type="text" style="width: 80px" maxlength="5" data-options="validType:['positiveInt[\'回應時效限輸入正整數\']','maxLength[5]']" name="<%=ContractSlaDTO.ATTRIBUTE.RESPONSE_HOUR.getValue() %>" id="<%=ContractSlaDTO.ATTRIBUTE.RESPONSE_HOUR.getValue() %>" value="<fmt:parseNumber value="${contractSlaDTO.responseHour}" type="number"  integerOnly="true"/>"></input>hr
                        </td>
                        <td>回應警示:</td>
                        <td>
                        	<input class="easyui-textbox" type="text" style="width: 80px" maxlength="5" data-options="validType:['positiveInt[\'回應警示限輸入正整數\']','maxLength[5]','beforeHour[\'responseHour\',\'回應警示不能大於回應時效\']']" name="<%=ContractSlaDTO.ATTRIBUTE.RESPONSE_WARNNING.getValue() %>" id="<%=ContractSlaDTO.ATTRIBUTE.RESPONSE_WARNNING.getValue() %>" value="<fmt:parseNumber value="${contractSlaDTO.responseWarnning}" type="number" integerOnly="true"/>"></input>hr前警示
                        </td>
                    </tr>
                    <tr>
                        <td>到場時效:<span class="red">*</span></td>
                        <td>
                        	<input class="easyui-textbox" type="text" style="width: 80px" missingMessage="請輸入到場時效" maxlength="5" data-options="required:true,validType:['positiveInt[\'到場時效限輸入正整數\']','maxLength[5]',['multipleOfTwentyFour[\'isThatDay\',\'“當天件”， 到場時效需為24倍數(天)，請重新輸入\']'] ]" name="<%=ContractSlaDTO.ATTRIBUTE.ARRIVE_HOUR.getValue() %>" id="<%=ContractSlaDTO.ATTRIBUTE.ARRIVE_HOUR.getValue() %>" value="<fmt:parseNumber value="${contractSlaDTO.arriveHour}" type="number"  integerOnly="true"/>"></input>hr
                        </td>
                        <td>到場警示:<span class="red">*</span></td>
                        <td>
                        	<input class="easyui-textbox" type="text" style="width: 80px" missingMessage="請輸入到場警示" maxlength="5" data-options="required:true,validType:['positiveInt[\'到場警示限輸入正整數\']','maxLength[5]','beforeHour[\'arriveHour\',\'到場警示不能大於到場時效\']']" name="<%=ContractSlaDTO.ATTRIBUTE.ARRIVE_WARNNING.getValue() %>" id="<%=ContractSlaDTO.ATTRIBUTE.ARRIVE_WARNNING.getValue() %>" value="<fmt:parseNumber value="${contractSlaDTO.arriveWarnning}" type="number"  integerOnly="true"/>"></input>hr前警示
                        </td>
                    </tr>
                    <tr>
                        <td>完修時效:<span class="red">*</span></td>
                        <td>
                        	<input class="easyui-textbox" type="text" style="width: 80px" missingMessage="請輸入完修時效" maxlength="5" data-options="required:true, validType:['positiveInt[\'完修時效限輸入正整數\']','maxLength[5]',['multipleOfTwentyFour[\'isThatDay\',\'“當天件”， 完修時效需為24倍數(天)，請重新輸入\']'] ]" name="<%=ContractSlaDTO.ATTRIBUTE.COMPLETE_HOUR.getValue() %>" id="<%=ContractSlaDTO.ATTRIBUTE.COMPLETE_HOUR.getValue() %>" value="<fmt:parseNumber value="${contractSlaDTO.completeHour}" type="number"  integerOnly="true"/>"></input>hr
                        </td>
                        <td>完修警示:<span class="red">*</span></td>
                        <td>
                        	<input class="easyui-textbox" type="text" style="width: 80px" missingMessage="請輸入完修警示" maxlength="5" data-options="required:true,validType:['positiveInt[\'完修警示限輸入正整數\']','maxLength[5]','beforeHour[\'completeHour\',\'完修警示不能大於完修時效\']']" name="<%=ContractSlaDTO.ATTRIBUTE.COMPLETE_WARNNING.getValue() %>" id="<%=ContractSlaDTO.ATTRIBUTE.COMPLETE_WARNNING.getValue() %>" value="<fmt:parseNumber value="${contractSlaDTO.completeWarnning}" type="number"  integerOnly="true"/>"></input>hr前警示
                        </td>
                    </tr>
                    <tr>
                        <td>說明:</td>
                        <td colspan="3">
                        	<%-- <input name="<%=ContractSlaDTO.ATTRIBUTE.COMMENT.getValue() %>" id="<%=ContractSlaDTO.ATTRIBUTE.COMMENT.getValue() %>" value="<c:out value='${contractSlaDTO.comment}'/>" class="easyui-textbox" maxlength="200" data-options="multiline:true,validType:['maxLength[200]']" invalidMessage="說明輸入過長，請重新輸入"  style="height: 50px; width: 400px">  --%>
                        	<textarea name="<%=ContractSlaDTO.ATTRIBUTE.COMMENT.getValue() %>" id="<%=ContractSlaDTO.ATTRIBUTE.COMMENT.getValue() %>" 
                        	class="easyui-textbox" maxlength="200" data-options="multiline:true,validType:['maxLength[200]']" 
                        	invalidMessage="說明輸入過長，請重新輸入"  style="height: 50px; width: 400px"><c:out value='${contractSlaDTO.comment}'/></textarea>
                        </td>
						<input type="hidden" id="<%=ContractSlaFormDTO.PARAM_SLA_ID %>" name="<%=ContractSlaFormDTO.PARAM_SLA_ID %>" value="<c:out value='${contractSlaDTO.slaId}'/>"> 
                    </tr>
                </table>
            </form>
        </div>
</body>
</html>