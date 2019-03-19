<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/jsp/common/common.jsp"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO"%>
<%
	SessionContext ctx = (SessionContext)request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	CaseManagerFormDTO formDTO = null;
	if(ctx != null) {
		formDTO = (CaseManagerFormDTO) ctx.getResponseResult();
	} else {
		formDTO = new CaseManagerFormDTO();
	}
	String customerDefaultValue = formDTO.getCustomerDefaultValue();
	String useCaseNo = formDTO.getUseCaseNo();
	//客戶下拉菜單
	List<Parameter> customers =(List<Parameter>)SessionHelper.getAttribute(request, useCaseNo, IAtomsConstants.PARAM_CUSTOMER_LIST);
%>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="customers" value="<%=customers%>" scope="page"></c:set>
<c:set var="customerDefaultValue" value="<%=customerDefaultValue%>" scope="page"></c:set>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%@include file="/jsp/common/easyui-common.jsp"%>
    <script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
</head>
<body>
<div region="center" style="margin-left:2%; width: auto; height: auto; padding: 10px; overflow-y: auto" class="setting-dialog-panel-height">
    <!-- <div region="center" style="margin-left:2%;width: auto; height: auto; padding: 10px; overflow-y: hidden"> -->
            <table id="chooseDtidDatagrid" class="easyui-datagrid" region="center" title="選擇DTID" style="width: 98%; height: auto"
               data-options="
               	rownumbers:true,
                pagination:true,
                pageList:[15,30,50,100],
				pageSize:15,
				iconCls: 'icon-edit',
				onCheck:chooseDtidOnCheck,
				singleSelect: true,
				method: 'post',
				pageNumber:0,
				sortOrder:'asc',
				nowrap:false,
				toolbar:'#tbDTID'">
				<thead>
					<tr>
<!-- 						<th data-options="field:'ck',checkbox:true"></th>
 -->						<th data-options="field:'caseId',sortable:true,halign:'center',hidden:true">主鍵</th>
						<th data-options="field:'dtid',sortable:true,width:130,halign:'center'">DTID</th>
						<th data-options="field:'tid',sortable:true,halign:'center'">TID清單</th>
						<th data-options="field:'merchantCode',sortable:true,halign:'center'">特店代號清單</th>
						<th data-options="field:'assetName',sortable:true,width:160,halign:'center'">設備名稱</th>
						<th data-options="field:'serialNumber',sortable:true,width:220,halign:'center'">刷卡機序號</th>
						<th data-options="field:'merchantName',sortable:true,width:160,halign:'center'">特店名稱</th>
						<th data-options="field:'merchantHeaderName',width:240,sortable:true,halign:'center'">表頭（同對外名稱）</th>
						<th data-options="field:'bussinessAddress',width:240,sortable:true,halign:'center'">營業地址</th>
						<th data-options="field:'companyName',width:240,sortable:true,halign:'center'">維護廠商</th>
					</tr>
				</thead>
			</table>
			<input type="hidden" value="" id="isQuery"/>
			<div id="tbDTID" style="padding: 2px 5px;">
				<form id="queryDTIDform" >
					<table cellpadding="4">
						<tr>
							<td>客戶:</td>
							<td>
								<cafe:droplisttag css="easyui-combobox"
									id="<%=CaseManagerFormDTO.QUERY_CUSTOMER_ID %>"
									name="<%=CaseManagerFormDTO.QUERY_CUSTOMER_ID %>" 
									blankName="請選擇"
									hasBlankValue="true"
									style="width: 150px"
									result="${customers }"
									defaultValue="${customerDefaultValue }"
									javascript="editable=false ${customerDefaultValue eq ''?'':'disabled=true' }" 
								>
							</cafe:droplisttag>
							<td>特店代號:</td>
							<td>
								<input class="easyui-textbox" 
									id="<%=CaseManagerFormDTO.QUERY_MERCHANT_CODE%>" 
									name="<%=CaseManagerFormDTO.QUERY_MERCHANT_CODE%>" 
									type="text" 
									validType="maxLength[20]" 
									maxlength="20" 
									style="width: 150px"></input>
							</td>
							<td>DTID:</td>
							<td>
								<input class="easyui-textbox" 
									id="<%=CaseManagerFormDTO.QUERY_DTID%>" 
									name="<%=CaseManagerFormDTO.QUERY_DTID%>" 
									type="text" 
									validType="maxLength[8]" 
									maxlength="8" 
									style="width: 150px"></input>
							</td>
						</tr>
						<tr>
							<td>TID:</td>
							<td>
								<input class="easyui-textbox" 
									id="<%=CaseManagerFormDTO.QUERY_TID%>" 
									name="<%=CaseManagerFormDTO.QUERY_TID%>" 
									type="text" 
									validType="maxLength[8]" 
									maxlength="8" 
									style="width: 150px"></input>
							</td>
							<td>特店名稱:</td>
							<td>
								<input class="easyui-textbox" 
									id="<%=CaseManagerFormDTO.QUERY_MERCHANT_NAME%>" 
									name="<%=CaseManagerFormDTO.QUERY_MERCHANT_NAME%>" 
									type="text" 
									validType="maxLength[50]" 
									maxlength="50" 
									style="width: 150px"></input>
							</td>
							<td>刷卡機序號:</td>
							<td>
								<input class="easyui-textbox" 
									id="<%=CaseManagerFormDTO.QUERY_EDC_NUMBER%>" 
									name="<%=CaseManagerFormDTO.QUERY_EDC_NUMBER%>" 
									type="text" 
									validType="maxLength[20]" 
									maxlength="20" 
									style="width: 150px"></input>
							</td>
						</tr>
						<tr>
							<td>表頭（同對外名稱）:</td>
							<td colspan="4">
								<input class="easyui-textbox" 
									id="<%=CaseManagerFormDTO.QUERY_HEADER_NAME%>" 
									name="<%=CaseManagerFormDTO.QUERY_HEADER_NAME%>" 
									type="text" 
									validType="maxLength[50]" 
									maxlength="50" 
									style="width: 150px"></input>
							</td>
							<td style="text-align: right;"><a href="javascript:void(0)" id="buttonQuery" class="easyui-linkbutton" iconcls="icon-search" >查詢</a></td>
						</tr>
					</table>
					<div><span id="DTIDmsg" class="red"></span></div>
				</form>
				<div style="color: red">
					<span id="msgDtid" class="red"></span>
				</div>	
			</div>

	</div>
	<script type="text/javascript">
    $(function(){
   	 	$("#DTIDmsg").text("");
    	//查詢
		$("#buttonQuery").click(function(){
			queryDTID(1,true);
		}); 
      });
      
      //查詢數據
     function queryDTID(pageIndex, isCleanMsg) {
    	 $("#DTIDmsg").text("");
    	// 清空選中的行
		 $("#chooseDtidDatagrid").datagrid("unselectAll");
    	var param = $("#queryDTIDform").form("getData");
    	param.queryEDCNumber = $("#<%=CaseManagerFormDTO.QUERY_EDC_NUMBER%>").textbox("getValue");
    	param.queryCustomerId = $("#queryCustomerId").combobox("getValue");
		var options = {
    			url : "${contextPath}/caseHandle.do?actionId=queryDTID",
    			queryParams :param ,
    			//當前頁
    			pageNumber:pageIndex,
				isOpenDialog:true,
				sortName : '',
				onLoadSuccess:function(data){
					$(this).datagrid("fixRownumber","chooseDtidDatagrid"); 
					
					$(".datagrid-row").mouseover(function(){ 
   						$(this).css("cursor", "pointer");
   					});
   					$(".datagrid-row").mouseout(function(){ 
   						$(this).css("cursor", "auto");
   					});
					if (isCleanMsg) {
						$("#DTIDmsg").text("");
					}
					if (data.total == 0) {
						//提示查無數據
						$("#DTIDmsg").text(data.msg);
					}
					isCleanMsg = true;
				},
				onLoadError : function() {
					$("#DTIDmsg").text("查詢失敗！請聯繫管理員");
				}
			}
		openDlgGridQuery("chooseDtidDatagrid", options);
     }
	</script>
</body>
</html>
