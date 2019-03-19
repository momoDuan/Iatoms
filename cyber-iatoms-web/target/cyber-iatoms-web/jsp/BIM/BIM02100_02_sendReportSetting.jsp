<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.ReportSettingFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.ReportSettingDTO"%>
<%
	//初始化加載下拉框數據
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	ReportSettingFormDTO formDTO = null;
	ReportSettingDTO reportSettingDTO = null;
	if (ctx != null) {
		formDTO = (ReportSettingFormDTO) ctx.getResponseResult();
		 if(formDTO != null){
			//若FormDTO存在，獲取DTO、useCaseNO以及存在的明細
			reportSettingDTO = formDTO.getReportSettingDTO();
		} 
	} else {
		formDTO = new ReportSettingFormDTO();
	}
	//獲取客戶下拉框的內容
	List<Parameter> customerNames = 
		(List<Parameter>) SessionHelper.getAttribute(request,IAtomsConstants.UC_NO_BIM_02100, IAtomsConstants.PARAM_CUSTOMER_LIST);
	//獲取報表名稱下拉框的值
	List<Parameter> reportList = 
		(List<Parameter>) SessionHelper.getAttribute(request,IAtomsConstants.UC_NO_BIM_02100, IATOMS_PARAM_TYPE.REPORT_CODE.getCode());
	//新增時被選中的報表明細
	List<String> reportDetailList =
		(List<String>)SessionHelper.getAttribute(request, IAtomsConstants.UC_NO_BIM_02100, IAtomsConstants.PARAM_REPORT_DETAIL_LIST);
%>
<c:set var="reportSettingDTO" value="<%=reportSettingDTO%>" scope="page"></c:set>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="reportDetailList" value="<%=reportDetailList%>" scope="page"></c:set>
<c:set var="customerNames" value="<%=customerNames%>" scope="page"></c:set>
<c:set var="reportList" value="<%=reportList%>" scope="page"></c:set>
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
						type="hidden" value="<%=IAtomsConstants.YES%>">
			<input id="<%=ReportSettingDTO.ATTRIBUTE.REPORT_DETAIL.getValue()%>" 
						name="<%=ReportSettingDTO.ATTRIBUTE.REPORT_DETAIL.getValue()%>"
						type="hidden" value="<c:out value='${reportDetailList}'/>">
            	<table cellpadding="3">
                    <tr>
                        <td>客戶:<span class="red">*</span></td>
                        <td colspan="3">
                        			<cafe:droplisttag 
						               id="<%=ReportSettingDTO.ATTRIBUTE.COMPANY_ID.getValue()%>"
						               name="<%=ReportSettingDTO.ATTRIBUTE.CUSTOMER_NAME.getValue()%>" 
						               css="easyui-combobox" disabled="true" result="${customerNames}"
						               selectedValue="${reportSettingDTO.companyId}"
						               hasBlankValue="true"
						               blankName="請選擇"
						               style="width: 250px"
						               javascript="validType=\"requiredDropList\" editable=false required=true invalidMessage=\"請輸入客戶\"">
					             	</cafe:droplisttag>
                    	</td>
                    </tr>
                    <tr>
                    	<td>報表名稱:<span class="red">*</span></td>
                        <td>
                        	<cafe:droplisttag
								css="easyui-combobox" style="width: 250px"
								id="<%=ReportSettingDTO.ATTRIBUTE.REPORT_CODE.getValue()%>" 
								name="<%=ReportSettingDTO.ATTRIBUTE.REPORT_CODE.getValue()%>"
								hasBlankValue="true" result="${reportList}"
								blankName="請選擇"
								disabled="true"
								selectedValue="${reportSettingDTO.reportCode}"
								javascript="validType='requiredDropList' editable=false required=true invalidMessage=\"請輸入報表名稱\"">
							</cafe:droplisttag>
                    	</td>
                    </tr>
                    <tr id="trReportDetail">
						<td>報表明細:<span class="red">*</span></td>
						<td id="detail"></td>
					</tr>
					<tr>
						<td>報表日期:<span class="red">*</span></td>
						<td>
							<c:choose>
								<c:when test="${reportSettingDTO.reportCode eq '2' || reportSettingDTO.reportCode eq '4' || reportSettingDTO.reportCode eq '10'
									|| reportSettingDTO.reportCode eq 'feeReportForGp' || reportSettingDTO.reportCode eq 'feeReportForJdw' 
									|| reportSettingDTO.reportCode eq '15'  || reportSettingDTO.reportCode eq '16'  
									 || reportSettingDTO.reportCode eq '17' || reportSettingDTO.reportCode eq '19'
									  || reportSettingDTO.reportCode eq '20'}">
									<c:choose>
										<c:when test="${reportSettingDTO.reportCode eq 'feeReportForJdw'
											|| reportSettingDTO.reportCode eq '19'}">
											<input style="width: 120px" maxlength="7" class="easyui-datebox"
												data-options="validType:'validDateYearMonth',required:'true',missingMessage:'請輸入報表日期'" 
												id="<%=ReportSettingDTO.ATTRIBUTE.CREATED_DATE_STRING.getValue()%>"
												name="<%=ReportSettingDTO.ATTRIBUTE.CREATED_DATE_STRING.getValue()%>"
												disabled="disabled"
											/>
										</c:when>
										<c:otherwise>
										<input style="width: 120px" maxlength="7" class="easyui-datebox"
											data-options="validType:'validDateYearMonth',required:'true',missingMessage:'請輸入報表日期'" 
											id="<%=ReportSettingDTO.ATTRIBUTE.CREATED_DATE_STRING.getValue()%>"
											name="<%=ReportSettingDTO.ATTRIBUTE.CREATED_DATE_STRING.getValue()%>"
										/>
									</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<input class="easyui-datebox" style="width: 120px" 
										data-options="validType:'date',required:'true',missingMessage:'請輸入報表日期'"
										id="<%=ReportSettingDTO.ATTRIBUTE.CREATED_DATE_STRING.getValue()%>"
										name="<%=ReportSettingDTO.ATTRIBUTE.CREATED_DATE_STRING.getValue()%>"
										validMessage="日期格式限YYYY/MM/DD" maxlength="10"
										/>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
                    <tr>
                        <td>收件人:<span class="red">*</span></td>
                        <td>
							<input class="easyui-textbox" 
								id="<%=ReportSettingDTO.ATTRIBUTE.RECIPIENT.getValue()%>" 
								name="<%=ReportSettingDTO.ATTRIBUTE.RECIPIENT.getValue()%>" 
								type="text" data-options="validType:['manyEmail[\'收件人\']','length[0,200]'],missingMessage:'請輸入收件人',required:'true', multiline:true" 
								value="<c:out value='${reportSettingDTO.recipient}'/>"
								disabled = "true"
								style="height: 50px; width: 300px">
							</input>
                        </td>
                    </tr>
                    <tr>
						<td>副本:</td>
						<td>
								<input class="easyui-textbox" type="text" 
									id="<%=ReportSettingDTO.ATTRIBUTE.COPY.getValue()%>"
									name="<%=ReportSettingDTO.ATTRIBUTE.COPY.getValue()%>"
									value="<c:out value='${reportSettingDTO.copy}'/>"  
									disabled = "true"
									style="height: 50px; width: 300px"
									data-options="validType:['manyEmail[\'副本\']','length[0,200]'], multiline:true">
								</input>
						</td>
					</tr>
					<tr>
						<td>備註:</td>
						<td>
							<input class="easyui-textbox" type="text" 
								id="<%=ReportSettingDTO.ATTRIBUTE.REMARK.getValue()%>"
								name="<%=ReportSettingDTO.ATTRIBUTE.REMARK.getValue()%>"
								value="<c:out value='${reportSettingDTO.remark}'/>" 
								disabled = "true"
								style="height: 50px; width: 300px"
								data-options="validType:'length[0,200]', multiline:true">
							</input>
						</td>
					</tr>
                </table>
            </form>
	</div>
<script type="text/javascript">
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
			$('#trReportDetail').show();
			if(data.length > 0 ){
				document.getElementById("detail").innerHTML = "";
				var htmlText = "";
				for(i=0;i<data.length;i++){
					//拼接複選框的選項
					htmlText += '<input type="checkbox" name="reportDetail"  disabled="true" ';
					//將查尋到的結果與複選框的值依次進行對比
					for(j=0;j<s.length;j++){
						//若存在值與查到的值相等
						if(s[j] == data[i].value){
							htmlText += ' checked="checked" ';
						}
					}
					htmlText += ' value="' + data[i].value + '" />' +data[i].name
				}
				document.getElementById("detail").innerHTML = htmlText;
			}
		} else {
			$('#trReportDetail').hide();
			document.getElementById("detail").innerHTML = "";
		}
	});
	
	$(function(){
		var createdDate = '${formDTO.createdDateString}';
		//當報表名稱為“EDC報表服務需求案件(維護)分析月報”等月報時,報表日期為當前的月份
		if("<%=IAtomsConstants.REPORT_NAME_MONTH_REPORT%>" == reportName 
				|| "<%=IAtomsConstants.REPORT_NAME_EDC_CONTRACT_EXPIRE_INFO_REPORT%>" == reportName 
				|| "<%=IAtomsConstants.REPORT_NAME_CASE_REPAIR_DETAIL_REPORT%>" == reportName
				|| "<%=IAtomsConstants.REPORT_NAME_EDC_FEE_FOR_JDW%>" == reportName 
				|| "<%=IAtomsConstants.FEE_REPORT_FOR_GP%>" == reportName
				|| "<%=IAtomsConstants.REPORT_TCB_EDC_FIFTEEN%>" == reportName
				|| "<%=IAtomsConstants.REPORT_TCB_GREEN_WORLD_SIXTEEN%>" == reportName
				|| "<%=IAtomsConstants.REPORT_TCB_SCSB_SEVENTEEN%>" == reportName
				|| "<%=IAtomsConstants.REPORT_TCB_SYB_NINETEEN%>" == reportName
				|| "<%=IAtomsConstants.REPORT_TCB_CHB_TWENTY%>" == reportName){
			document.getElementById('createdDateString').value = createdDate;
			createMonthDataBox('createdDateString');
		} else {
			document.getElementById('createdDateString').value = createdDate;
		}
	})
</script>
</body>
</html>