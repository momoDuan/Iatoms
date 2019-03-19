<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/jsp/common/common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.RepairReportFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.RepairReportDTO"%>
<%
	//初始化加載下拉框數據
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	RepairReportFormDTO formDTO = null;
	String useCaseNo = null;
	String roleAttribute = null;
	String logonUserCompanyId = null;
	if (ctx != null) {
		formDTO = (RepairReportFormDTO) ctx.getRequestParameter();
		if (formDTO != null) {
			useCaseNo = formDTO.getUseCaseNo();
			roleAttribute = formDTO.getRoleAttribute();
			logonUserCompanyId = formDTO.getLogonUserCompanyId();
		}
		
	}
	//客戶信息下拉框
	List<Parameter> customerInfoList = (List<Parameter>) SessionHelper.getAttribute(request, useCaseNo, RepairReportFormDTO.PARAM_COMPANY_LIST);
	//客戶信息下拉框
	List<Parameter> customerAllList = (List<Parameter>) SessionHelper.getAttribute(request, useCaseNo, RepairReportFormDTO.PARAM_ALL_COMPANY_LIST);
	//刷卡機類型
	List<Parameter> edcTypeList = (List<Parameter>) SessionHelper.getAttribute(request, useCaseNo, IATOMS_PARAM_TYPE.EDC_TYPE.getCode());
 %>
 <c:set var="customerInfoList" value="<%= customerInfoList %>" scope="page"></c:set>
 <c:set var="customerAllList" value="<%= customerAllList %>" scope="page"></c:set>
  <c:set var="edcTypeList" value="<%= edcTypeList %>" scope="page"></c:set>
  <c:set var="edcListSize" value="<%= edcTypeList.size() %>" scope="page"></c:set>
  <c:set var="roleAttribute" value="<%=roleAttribute%>" scope="page"></c:set>
<c:set var="logonUserCompanyId" value="<%=logonUserCompanyId%>" scope="page"></c:set>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%@include file="/jsp/common/easyui-common.jsp"%>
</head>
<body>
    <div id="p" class="easyui-panel" title="報修問題分析報表" style="width: auto; height: auto;">
        <div title="" style="padding: 10px">
            <div id="tb" style="padding: 2px 5px;">
            	 <form action="repairReport.do" id="searchForm" name="searchForm" method="post">
            	 	<input type="hidden" id="actionId" name="actionId" />
					<input type="hidden" id="serviceId" name="serviceId" />
					<input type="hidden" id="useCaseNo" name="useCaseNo" />
	                客戶:<span class="red">*</span>
	                <c:choose>
				      	 <c:when test="${not empty logonUserCompanyId}">
					            <cafe:droplisttag 
					    			name="<%=RepairReportFormDTO.QUERY_CUSTOMER%>"
					    			id="<%=RepairReportFormDTO.QUERY_CUSTOMER%>"
					    			css="easyui-combobox"
					    			result="${customerAllList}"
					    			selectedValue="${logonUserCompanyId}"
					    			hasBlankValue="true"
						            blankName="" 
						            style="width:120px"
					    			javascript="editable='false' disabled=disabled"
					    		>
					    		</cafe:droplisttag>
					    	</c:when>
					    	<c:otherwise>
				                <cafe:droplisttag 
					               id="<%=RepairReportFormDTO.QUERY_CUSTOMER%>"
					               name="<%=RepairReportFormDTO.QUERY_CUSTOMER%>" 
					               css="easyui-combobox"
					               result="${customerInfoList}"
					               hasBlankValue="true"
					               blankName="請選擇"
					               style="width: 110px"
					               javascript="editable='false'">
					            </cafe:droplisttag>
			            </c:otherwise>
					</c:choose>
	                結案時間起:&nbsp;
		            	<input class="easyui-datebox" 
	                        	 style="width: 110px"
	                             data-options="validType:['maxLength[10]','validDate',],onChange:function(newValue,oldValue) {
                            			$('#<%=RepairReportFormDTO.CLOSED_TIME_END %>').timespinner('isValid');
                             	}"
	                             id="<%=RepairReportFormDTO.CLOSED_TIME_START%>"
	                             name="<%=RepairReportFormDTO.CLOSED_TIME_START%>"> 
	                     </input>
	                結案時間迄:&nbsp;
	                	<input class="easyui-datebox" 
			            	id="<%=RepairReportFormDTO.CLOSED_TIME_END%>" 
			            	name="<%=RepairReportFormDTO.CLOSED_TIME_END%>" 
			            	data-options="validType:['maxLength[10]','validDate','compareDateStartEnd[\'#closedTimeStart\']']" 
			            	style="width: 110px">
		            	</input>
	                特店代號:&nbsp;
	                	<input class="easyui-textbox" 
			            	id="<%=RepairReportFormDTO.QUERY_MERCHANT_CODE%>" 
			            	name="<%=RepairReportFormDTO.QUERY_MERCHANT_CODE%>" 
			            	type="text" 
			            	validType="maxLength[20]" 
			            	maxlength="20" 
			            	style="width: 100px">
		            	</input>
	                機型:&nbsp;
			           <cafe:droplisttag 
			               id="<%=RepairReportFormDTO.QUERY_EDC_TYPE%>"
			               name="<%=RepairReportFormDTO.QUERY_EDC_TYPE%>" 
			               css="easyui-combobox"
			               hasBlankValue="true"
			               result="${edcTypeList}"
			               blankName="請選擇(復選)"
			               style="width: 180px"
			               javascript="editable=false multiple=true">
			           </cafe:droplisttag>
	                <a class="easyui-linkbutton" href="#" iconcls="icon-search" id="btnQuery">查詢</a>
	                <a href="#" class="easyui-linkbutton" id="btnExport"  onclick="exportList()">匯出</a>
	                <div><span id="msg" class="red"></span></div>
                </form>
            </div>
            <table id="dgRepair" class="easyui-datagrid" title="報修問題" style="width: auto; height: auto"
                data-options="
		         	    rownumbers:true,
		                pagination:true,
		                pageList:[15,30,50,100],
						pageSize:15,
						iconCls: 'icon-edit',
						method: 'post',
						pageNumber:0,
						showFooter:true,
						nowrap:false,
						fitColumns:false,
						">
                <thead>
                    <tr>
                        <th rowspan="2" data-options="field:'customerName',sortable:true,halign:'center'">客戶</th>
                        <th rowspan="2" data-options="field:'repairType',sortable:true,halign:'center'">類別</th>
                        <th rowspan="2" data-options="field:'repairItem',sortable:true,halign:'center'">項目</th>
                        <th rowspan="2" data-options="field:'repairCount',sortable:true,halign:'center'">報修次數</th>
                        <th colspan="${edcListSize}" data-options="field:'edcType',sortable:true,halign:'center'">刷卡機型</th>
                    </tr>
                    <tr>
                        <c:forEach items="${edcTypeList}" var="edcType" >
                        	<th data-options="field:'${edcType.value}',sortable:true,halign:'center',align:'right'">${edcType.name}</th>
						</c:forEach>
                    </tr>
                </thead>
            </table>
            
             <div style="margin-top: 10px;"></div>
            <div><span id="msgSolve" class="red"></span></div>
             <table id="dgSolve" class="easyui-datagrid" title="處理方式" style="width: auto; height: auto"
                data-options="
			         	rownumbers:true,
		                pagination:true,
						iconCls: 'icon-edit',
						method: 'post',
						pageNumber:0,
						nowrap:false,
						showFooter:true,
						">
                <thead>
                    <tr>
                        <th rowspan="2" data-options="field:'customer',sortable:true">客戶</th>
                        <th rowspan="2" data-options="field:'type',sortable:true">類別</th>
                        <th rowspan="2" data-options="field:'project',sortable:true">項目</th>
                        <th rowspan="2" data-options="field:'account',sortable:true">報修次數</th>
                        <th colspan="${edcListSize}" data-options="field:'edcType',sortable:true,halign:'center'">刷卡機型</th>
                    </tr>
                    <tr>
                        <c:forEach items="${edcTypeList}" var="edcType" >
                        	<th data-options="field:'${edcType.name}',sortable:true,halign:'center',align:'right'">${edcType.name}</th>
						</c:forEach>
                    </tr>
                </thead>
            </table> 
        </div> 
    </div>
    <style type="text/css">
        #fm {
            margin: 0;
            padding: 10px 30px;
        }

        .ftitle {
            font-size: 14px;
            font-weight: bold;
            padding: 5px 0;
            margin-bottom: 10px;
            border-bottom: 1px solid #ccc;
        }

        .fitem {
            margin-bottom: 5px;
            text-align: center;
        }

            .fitem label {
                display: inline-block;
                width: 80px;
            }

            .fitem input {
                width: 160px;
            }
    </style>
    <script type="text/javascript">
    $.extend($.fn.validatebox.defaults.rules, {
        validDate : {     
   		validator: function(value) {
   			if (value.length != 10) {
   				return false;
   			} else {
   				var y = value.substring(0,4);
   				var sep1 = value[4];
   				var m = value.substring(5,7);
   				var sep2 = value[7];
   				var d = value.substring(8,value.length);
   				if(sep1 != '/' || sep2 != '/'){
   					return false;
   				}
   				if(parseInt(m) > 12){
   					return false;
   				}
   				if (m == '00') {
   					return false;
   				}
   				if(parseInt(d) > 31){
   					return false;
   				}
   				if (d == '00') {
   					return false;
   				}
   				if(m == '04' || m == '06'|| m == '09'|| m == '11'){
   					if(parseInt(d) > 30){
   						return false;
   					}
   				}
   				if(parseFloat(y) % 4 == 0){
   					if(m == '02'){
   						if(parseInt(d) > 29){
   							return false;
   						}
   					}
   				}else{
   					if(m == '02'){
   						if(parseInt(d) > 28){
   							return false;
   						}
   					}
   				}
   			}
   			return true;
   		},     
   		message: '結案日期格式限YYYY/MM/DD'    
   	}, 
   	compareDateStartEnd: {
        validator:function(value,param) {
        	var data = $(param[0]).datebox("getValue");
        		if(value>=data){
	        		return true;
	        	}else{
	        		if (param.length == 2) {
						$.fn.validatebox.defaults.rules.compareDateStartEnd.message = param[1];
					}
	        		return false;
	        	}	
        },
        message:' 結案時間迄不能小於結案時間起'
    },
     });
    
    $(function (){
    	//$("#btnExport").linkbutton('disable');
    	$("#btnQuery").click(function() {
    		$("#msg").text("");
    		$("#msgSolve").text("");
    		if (!$("#searchForm").form('validate')) {
    			return false;
    		}
			queryRepair(1, true);
			querySolve(1, true);
		});			
	});
  	//查詢報修問題
    function queryRepair(pageIndex, isCleanMsg) {
    	// 清空選中的行
		$("#dgRepair").datagrid("unselectAll");
		var columnFields = $("#dgRepair").datagrid("getColumnFields");
		 $("#dgRepair").datagrid({
			url : "${contextPath}/repairReport.do?actionId=<%=IAtomsConstants.ACTION_QUERY%>",
			queryParams : $("#searchForm").form("getData"),
			//當前頁
			pageNumber : pageIndex,
			//返回出錯信息
			onLoadError : function(data) {
				$.messager.progress('close');
				$.messager.alert('提示', '查詢報修問題出錯', 'error');
			},
			//返回成功信息
		   onLoadSuccess : function (data) {
				if (isCleanMsg) {
					$("#msg").text("");
				}
				if (data.total == 0) {
					// 提示查無數據
					$("#msg").text(data.msg);
				} else {
					var arr =[{mergeFiled:"customerName",premiseFiled:"customerName"},
		        	          {mergeFiled:"repairType",premiseFiled:"repairType"}];
		        	//合并列的field数组及对应前提条件filed（为空则直接内容合并）  
					mergeCells(arr,"dgRepair");
					$("#btnExport").linkbutton('enable');
				}
				isCleanMsg = true;
			}
    	});
	}
	//查詢處理問題
	function querySolve(pageIndex, isCleanMsg) {
    	// 清空選中的行
		$("#dgSolve").datagrid("unselectAll");
		var columnFields = $("#dgSolve").datagrid("getColumnFields");
		 $("#dgSolve").datagrid({
			url : "${contextPath}/repairReport.do?actionId=<%=IAtomsConstants.ACTION_LIST%>",
			queryParams : $("#searchForm").form("getData"),
			//當前頁
			pageNumber : pageIndex,
			//返回出錯信息
			onLoadError : function(data) {
				$.messager.progress('close');
				$.messager.alert('提示', '查詢處理方式出錯', 'error');
			},
			//返回成功信息
		   onLoadSuccess : function (data) {
				if (isCleanMsg) {
					$("#msgSolve").text("");
				}
				if (data.total == 0) {
					// 提示查無數據
					$("#msgSolve").text(data.msg);
				} else {
					$("#btnExport").linkbutton('enable');
				}
				isCleanMsg = true;
				
			}
    	});
    }
	//匯出
	function exportList() {
		$("#msg").empty();
		$("#msgSolve").empty();
		var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		$.blockUI(blockStyle);
		
		actionClicked(document.forms["searchForm"], '<%=IAtomsConstants.UC_NO_SRM_05080%>', '', 'exportList');
		
		ajaxService.getExportFlag('<%=IAtomsConstants.UC_NO_SRM_05080%>',function(data){
			$.unblockUI();
		});
	}
</script>
</body>
</html>