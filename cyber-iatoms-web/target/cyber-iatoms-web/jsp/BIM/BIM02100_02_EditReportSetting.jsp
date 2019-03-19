<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.ReportSettingFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.ReportSettingDTO"%>
<%
	//初始化加載下拉框數據
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	//獲取客戶下拉框的內容
	List<Parameter> customerNames = 
		(List<Parameter>) SessionHelper.getAttribute(request,IAtomsConstants.UC_NO_BIM_02100, IAtomsConstants.PARAM_CUSTOMER_LIST);
	//獲取報表名稱下拉框的值
	List<Parameter> reportList = 
		(List<Parameter>) SessionHelper.getAttribute(request,IAtomsConstants.UC_NO_BIM_02100, IATOMS_PARAM_TYPE.REPORT_CODE.getCode());
	ReportSettingFormDTO formDTO = null;
	ReportSettingDTO reportSettingDTO = null;
	List<Parameter> reportCodeList = null;
	String actionId = null;
	if (ctx != null) {
		formDTO = (ReportSettingFormDTO) ctx.getResponseResult();
		 if(formDTO != null){
			//若FormDTO存在，獲取DTO、useCaseNO以及存在的明細
			reportSettingDTO = formDTO.getReportSettingDTO();
			actionId = formDTO.getActionId();
		} 
	} else {
		formDTO = new ReportSettingFormDTO();
	}
	//修改時被選中的報表明細
	List<String> reportDetailList =
		(List<String>)SessionHelper.getAttribute(request, IAtomsConstants.UC_NO_BIM_02100, IAtomsConstants.PARAM_REPORT_DETAIL_LIST);
%>
<c:set var="reportSettingDTO" value="<%=reportSettingDTO%>" scope="page"></c:set>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="customerNames" value="<%=customerNames%>" scope="page"></c:set>
<c:set var="reportList" value="<%=reportList%>" scope="page"></c:set>
<c:set var="reportDetailList" value="<%=reportDetailList%>" scope="page"></c:set>
<c:set var="actionId" value="<%=actionId%>" scope="page"></c:set>
<html> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="/jsp/common/easyui-common.jsp"%>
<title></title>
</head>
<body>
	<div data-options="region:'center'"
		style="width: auto; height: auto; padding: 10px 20px; background: #fff; overflow-y: hidden">
		<div class="dialogtitle">報表發送功能設定</div>
		<div><span id="errorMsg" class="red"></span> </div>
		<form id="fm" method="post" novalidate>
		<input id="<%=ReportSettingDTO.ATTRIBUTE.SETTING_ID.getValue()%>" 
					name="<%=ReportSettingDTO.ATTRIBUTE.SETTING_ID.getValue()%>"
					type="hidden" value="<c:out value='${reportSettingDTO.settingId}'/>">
		<input id="<%=ReportSettingFormDTO.PARAM_REPORT_SENDFLAG%>" 
					name="<%=ReportSettingFormDTO.PARAM_REPORT_SENDFLAG%>"
					type="hidden" value="<%=IAtomsConstants.NO%>">
            	<table cellpadding="3">
                    <tr>
                        <td>客戶:<span class="red">*</span></td>
                        <td colspan="3">
                        	<c:choose>
                        		<c:when test="${formDTO.settingId == null}">
                        			<cafe:droplisttag 
						               id="<%=ReportSettingDTO.ATTRIBUTE.COMPANY_ID.getValue()%>"
						               name="<%=ReportSettingDTO.ATTRIBUTE.CUSTOMER_NAME.getValue()%>" 
						               css="easyui-combobox"
						               result="${customerNames}"
						               hasBlankValue="true"
						               blankName="請選擇"
						               style="width: 250px"
						               javascript="validType=\"requiredDropList\" editable=false required=true invalidMessage=\"請輸入客戶\"">
				               		</cafe:droplisttag>
                        		</c:when>
                        		<c:otherwise>
                        			<cafe:droplisttag 
						               id="<%=ReportSettingDTO.ATTRIBUTE.COMPANY_ID.getValue()%>"
						               name="<%=ReportSettingDTO.ATTRIBUTE.CUSTOMER_NAME.getValue()%>" 
						               css="easyui-combobox" disabled="true"
						               result="${customerNames}"
						               selectedValue="${reportSettingDTO.companyId}"
						               hasBlankValue="true"
						               blankName="請選擇"
						               style="width: 250px"
						               javascript="validType=\"requiredDropList\" editable=false required=true invalidMessage=\"請輸入客戶\"">
					             	</cafe:droplisttag>
                        		</c:otherwise>
                        	</c:choose>
                    	</td>
                    </tr>
                    <tr>
                    	<td>報表名稱:<span class="red">*</span></td>
                        <td>
                        	<c:choose>
                        		<c:when test="${formDTO.settingId == null}">
                        			<cafe:droplisttag
										css="easyui-combobox" style="width: 250px"
										id="<%=ReportSettingDTO.ATTRIBUTE.REPORT_CODE.getValue()%>" 
										name="<%=ReportSettingDTO.ATTRIBUTE.REPORT_CODE.getValue()%>"
										hasBlankValue="true"
										blankName="請選擇"
										result="${reportList}"
										selectedValue="${reportSettingDTO.reportCode}"
										javascript="validType='requiredDropList' editable=false required=true invalidMessage=\"請輸入報表名稱\"">
									</cafe:droplisttag>
                        		</c:when>
                        		<c:otherwise>
                        			<cafe:droplisttag
										css="easyui-combobox" style="width: 250px"
										id="<%=ReportSettingDTO.ATTRIBUTE.REPORT_CODE.getValue()%>" 
										name="<%=ReportSettingDTO.ATTRIBUTE.REPORT_CODE.getValue()%>"
										hasBlankValue="true"
										blankName="請選擇"
										disabled="true"
										result="${reportList}"
										selectedValue="${reportSettingDTO.reportCode}"
										javascript="validType='requiredDropList' editable=false required=true invalidMessage=\"請輸入報表名稱\"">
								</cafe:droplisttag>
                        		</c:otherwise>
                        	</c:choose>
                    	</td>
                    </tr>
                    <tr id="trReportDetail">
						<td>報表明細:<span class="red">*</span></td>
						<td id="detail">
							<div id="checkReportDetail" class="div-list" data-list-required='請輸入報表明細'></div>
						</td>
					</tr>
                    <tr>
                        <td>收件人:<span class="red">*</span></td>
                        <td>
							<textarea
								name="<%=ReportSettingDTO.ATTRIBUTE.RECIPIENT.getValue()%>" 
								id="<%=ReportSettingDTO.ATTRIBUTE.RECIPIENT.getValue()%>"
								maxLength="200" 
								class="easyui-textbox" 
								data-options="multiline:true,validType:['manyEmail[\'收件人格式有誤，多筆請用分號區隔，請重新輸入\']','maxLength[200]'],missingMessage:'請輸入收件人',required:'true'"
								style="height: 70px; width: 330px"><c:out value='${reportSettingDTO.recipient}'/></textarea>
                        </td>
                    </tr>
                    <tr>
						<td>副本:</td>
						<td>
								<textarea
								name="<%=ReportSettingDTO.ATTRIBUTE.COPY.getValue()%>" 
								id="<%=ReportSettingDTO.ATTRIBUTE.COPY.getValue()%>"
								maxLength="200" 
								class="easyui-textbox" 
								data-options="multiline:true,validType:['manyEmail[\'副本格式有誤，多筆請用分號區隔，請重新輸入\']','maxLength[200]']"
								style="height: 70px; width: 330px"><c:out value='${reportSettingDTO.copy}'/></textarea>
						</td>
					</tr>
					<tr>
						<td>備註:</td>
						<td>
							<textarea 
							name="<%=ReportSettingDTO.ATTRIBUTE.REMARK.getValue()%>" 
							id="<%=ReportSettingDTO.ATTRIBUTE.REMARK.getValue()%>"
							maxLength="200" 
							class="easyui-textbox" 
							data-options="multiline:true" 
							style="height: 70px; width: 330px"  
							validType="maxLength[200]"><c:out value='${reportSettingDTO.remark}'/></textarea>
						</td>
					</tr>
                </table>
            </form>
	</div>
<script type="text/javascript">
	//隱藏報表明細
	$('#trReportDetail').hide();
	//修改操作
	if("${actionId}" == '<%=IAtomsConstants.ACTION_INIT_EDIT%>'){
		//獲取報表名稱的值
		var reportName = "${reportSettingDTO.reportCode}";
		//獲取到的已選擇的明細信息
		var ss = "${reportDetailList}";
		//將字符串轉換為數組
		var s = eval(ss);
		var bptdCode = "<%=IATOMS_PARAM_TYPE.REPORT_CODE.getCode()%>";
		var detailCode = "<%=IATOMS_PARAM_TYPE.REPORT_DETAIL.getCode()%>";
		//調用ajaxSevice的方法判斷報表明細是否可用
		ajaxService.getReportDetailList(reportName,bptdCode,detailCode,function(data){
			if(data != ""){
				//顯示報表明細
				$('#trReportDetail').show();
				if(data.length > 0 ){
					document.getElementById("checkReportDetail").innerHTML = "";
					var htmlText = "";
					for(i=0;i<data.length;i++){
						//拼接複選框的選項
						htmlText += '<input type="checkbox" name="reportDetail"';
						//將查尋到的結果與複選框的值依次進行對比
						for(j=0;j<s.length;j++){
							//若存在值與查到的值相等
							if(s[j] == data[i].value){
								htmlText += ' checked="checked" ';
							}
						}
						htmlText += ' value="' + data[i].value + '" />' +data[i].name
					}
					document.getElementById("checkReportDetail").innerHTML = htmlText;
					checkedDivListOnchange();
				}
			} else {
				//隱藏報表明細
				$('#trReportDetail').hide();
				document.getElementById("checkReportDetail").innerHTML = "";
			}
		});
		
	} else {
		//隱藏提示驗證消息
		hideDivListValidate();
	}
	//獲取複選框
	var as = document.getElementsByName('reportDetail'); 
	//複選框的長度
	var length = as.length;
	//獲取報表名稱的值
	var reportCode = document.getElementById("reportCode").value;
	$(function(){
		//選定客戶后顯示該客戶未選擇過的報表名稱
		$('#<%=ReportSettingDTO.ATTRIBUTE.COMPANY_ID.getValue()%>').combobox({
			onChange: function(customerId){
				//調用service的方法獲取報表名稱需要的值
				ajaxService.getReportNameList(customerId,function(data){
					if(data){
						data = initSelect(data,"請選擇");
						//清除之前下拉框的值
						$("#<%=ReportSettingDTO.ATTRIBUTE.REPORT_CODE.getValue()%>").combobox('clear');
						//將後台得到的值賦值進相應的ID
						$("#<%=ReportSettingDTO.ATTRIBUTE.REPORT_CODE.getValue()%>").combobox({
																	data:data,
																	valueField:'value',
																	textField:'name'
																	});
					}
				});
			}
		});
		//依所選的報表名稱顯示不同的報表明細
		$('#<%=ReportSettingDTO.ATTRIBUTE.REPORT_CODE.getValue()%>').combobox({
			onChange: function(reportCode){
				$('#trReportDetail').hide();
				var bptdCode = "<%=IATOMS_PARAM_TYPE.REPORT_CODE.getCode()%>";
				var detailCode = "<%=IATOMS_PARAM_TYPE.REPORT_DETAIL.getCode()%>";
				//調用ajaxSevice的方法判斷報表明細是否可用
				ajaxService.getReportDetailList(reportCode,bptdCode,detailCode,function(data){
					if(data != null){
						if(data.length > 0 ){
							$('#trReportDetail').show();
							document.getElementById("checkReportDetail").innerHTML = "";
							var htmlText = "";
							for(i=0;i<data.length;i++){
								htmlText += '<input type="checkbox" name="reportDetail" value="' + data[i].value + '" />' +data[i].name;
							}
							document.getElementById("checkReportDetail").innerHTML = htmlText;
							checkedDivListOnchange();
						}
					} else {
						document.getElementById("checkReportDetail").innerHTML = "";
					}
				});
			}
		});
	})
</script>
</body>
</html>