<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.AssetStockReportDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetStockReportFormDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ include file="/jsp/common/easyui-common.jsp"%>
<%
	SessionContext ctx = (SessionContext)request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	//空的列表
	List<Parameter> emptyList = new ArrayList<Parameter>();
	String ucNo = null;
	AssetStockReportFormDTO formDTO = null;
	AssetStockReportDTO assetStockReportDTO = null;
	String roleAttribute = null;
	String logonUserCompanyId = null;
	if(ctx != null) {
		formDTO = (AssetStockReportFormDTO) ctx.getResponseResult();
		if (formDTO != null ) {
			ucNo = formDTO.getUseCaseNo();
			roleAttribute = formDTO.getRoleAttribute();
			logonUserCompanyId = formDTO.getLogonUserCompanyId();
			assetStockReportDTO = formDTO.getAssetStockReportDTO();
		} else {
			formDTO = new AssetStockReportFormDTO();
		}
	}
	if (assetStockReportDTO == null) {
		assetStockReportDTO = new AssetStockReportDTO();
	}
	if (ucNo == null) {
		ucNo = IAtomsConstants.UC_NO_DMM_03100;
	}
	//客戶列表
	List<Parameter> customerList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_CUSTOMER_LIST);
	//公司列表
	List<Parameter> companyList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_COMPANY_LIST);
	//維護模式
	List<Parameter> maTypeList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_MA_TYPE);
%>
<c:set var="customerList" value="<%=customerList%>" scope="page"></c:set>
<c:set var="companyList" value="<%=companyList%>" scope="page"></c:set>
<c:set var="maTypeList" value="<%=maTypeList%>" scope="page"></c:set>
<c:set var="roleAttribute" value="<%=roleAttribute%>" scope="page"></c:set>
<c:set var="logonUserCompanyId" value="<%=logonUserCompanyId%>" scope="page"></c:set>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="emptyList" value="<%=emptyList%>" scope="page"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
<!-- 	<div style="width: auto; height: auto; padding: 1px; overflow-y: hidden" region="center"> -->
<div style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
		<div class="easyui-panel" title="設備庫存表" style="width: 98%; height: auto">
			 <div style="padding: 10px 60px 20px 60px">
				 <form id="queryForm" action="assetStockReport.do" class="easyui-form" method="post" novalidate>
				 <div><span id="msg" class="red"></span></div>
				 	<input type="hidden" id="actionId" name="actionId" />
					<input type="hidden" id="serviceId" name="serviceId" />
					<input type="hidden" id="useCaseNo" name="useCaseNo" /> 
              		 <table cellpadding="4" >
              		 	<tr >
              		 		<td>客戶:</td>
              		 		<td>
	             		 		<c:choose>
						      		<c:when test="${not empty logonUserCompanyId}">
							           <c:if test="${formDTO.isNoRoles == true}">
							            	<cafe:droplisttag 
								            	name="<%=AssetStockReportFormDTO.QUERY_CUSTOMSER_ID %>"
								    			id="<%=AssetStockReportFormDTO.QUERY_CUSTOMSER_ID %>"
								    			css="easyui-combobox"
								    			result="${emptyList}"
								    			selectedValue=""
								    			hasBlankValue="true"
									            blankName="請選擇" 
									            style="width:150px"
								    			javascript="editable='false' panelheight=\"auto\""
							    			>
							    			</cafe:droplisttag>
							            </c:if>
							    		 <c:if test="${formDTO.isNoRoles == false}">
							            	<cafe:droplisttag 
								            	name="<%=AssetStockReportFormDTO.QUERY_CUSTOMSER_ID %>"
								    			id="<%=AssetStockReportFormDTO.QUERY_CUSTOMSER_ID %>"
								    			css="easyui-combobox"
								    			result="${customerList}"
								    			selectedValue="${logonUserCompanyId}"
								    			hasBlankValue="true"
									            blankName="請選擇" 
									            style="width:150px"
								    			javascript="editable='false' disabled=disabled"
							    			>
							    			</cafe:droplisttag>
							            </c:if>	 
							    		<input type="hidden" id="<%=AssetStockReportFormDTO.QUERY_CUSTOMSER_ID %>" name ="<%=AssetStockReportFormDTO.QUERY_CUSTOMSER_ID %>" value="<c:out value='${logonUserCompanyId}'/>"/>
							    	</c:when>
							    	<c:otherwise>
							            <cafe:droplisttag 
							    			name="<%=AssetStockReportFormDTO.QUERY_CUSTOMSER_ID %>"
							    			id="<%=AssetStockReportFormDTO.QUERY_CUSTOMSER_ID %>"
							    			css="easyui-combobox"
							    			result="${companyList}"
							    			selectedValue=""
							    			blankName="請選擇"
							    			hasBlankValue="true"
							    			style="width:150px"
							    			javascript="editable='false'"
							    		>
							    		</cafe:droplisttag>
							    	</c:otherwise>
								</c:choose>
								<input type="hidden" id="<%=AssetStockReportFormDTO.PARAM_IS_NO_ROLES %>" name ="<%=AssetStockReportFormDTO.PARAM_IS_NO_ROLES %>" value="<c:out value='${formDTO.isNoRoles}'/>"/>
								<input type="hidden" id="<%=AssetStockReportFormDTO.PARAM_ROLE_ATTRIBUTE %>" name="<%=AssetStockReportFormDTO.PARAM_ROLE_ATTRIBUTE %>" value="<c:out value='${formDTO.roleAttribute}'/>"/> 
							</td>
              		 		<td>
              		 			維護模式:
              		 		</td>
              		 		<td>
             		 			<cafe:droplisttag
	                            	css="easyui-combobox"
	                            	id="<%=AssetStockReportFormDTO.QUERY_MAINTAIN_MODE %>"
									name="<%=AssetStockReportFormDTO.QUERY_MAINTAIN_MODE %>" 
									result="${maTypeList }"
									javascript="editable=false panelheight=\"auto\""
									blankName="請選擇"
									hasBlankValue="true"
									style="width:250px">
	                           	</cafe:droplisttag>
             		 		</td>
              		 	</tr>
              		 	<tr>
              		 		<td>查詢月份:<span class="red">*</span></td>
				    	 	<td>
					    	 	<input class="easyui-datebox" maxlength="7"
	       							name="<%=AssetStockReportFormDTO.QUERY_MONTH%>" id="<%=AssetStockReportFormDTO.QUERY_MONTH%>"
	       		 					data-options="validType:'validDateYearMonth',required:'true',missingMessage:'請輸入查詢月份'" 
	       		 					style="width: 150px" 
	       		 					value="${empty formDTO.queryMonth? formDTO.currentDate: formDTO.queryMonth }" 
	       		 					/>
                           	</td>
              		 	</tr>
              		 </table>
              		 <div style="width: 100%;display: inline-block;">
		            	<span style="width: 50%;display: inline-block;float: right;text-align: right">
		            		<a href="javascript:void(0)" style="width: 150px" onclick="exportData()">匯出</a>
		            	</span>
		            </div>
          		  </form>
       		 </div>
    	</div>      
	                
	</div>
</body>
<script type="text/javascript">
	
   	$(function () {
   		//設定查詢月份欄位的日曆控件為年月
		createMonthDataBox('<%=AssetStockReportFormDTO.QUERY_MONTH%>');
   		textBoxDefaultSetting($("#<%=AssetStockReportFormDTO.QUERY_MONTH%>"));
   	});
   	//匯出
	function exportData(){
		$("#msg").text("");
   		if($('#queryForm').form("validate")){
   			var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			$.blockUI(blockStyle);
   			var queryParam = $('#queryForm').form("getData");
   			$.ajax({
   				url: "${contextPath}/assetStockReport.do?actionId=<%=IAtomsConstants.ACTION_QUERY%>",
   				data:queryParam,
   				type:'post', 
   				cache:false, 
   				dataType:'json', 
   				success:function(data){
   					var length = getJsonLength(data);
   					if(length > 2){
   						$.unblockUI();
   						$.messager.progress('close');
   						$("#msg").text(data.msg);
   					} else {
   						 var useCaseNo = '<%=IAtomsConstants.UC_NO_DMM_03100%>';
						actionClicked(document.forms["queryForm"],
							useCaseNo,
							'',
							'export');
						
						ajaxService.getExportFlag(useCaseNo,function(data){
							$.unblockUI();
						});
   					}
   				},
   			});
   		}
	}
   	// 拿到json對象的長度
	function getJsonLength(jsonData){
		var jsonLength = 0; 
		for(var item in jsonData){ 
			jsonLength++; 
		} 
		return jsonLength; 
	}
</script>
</html>