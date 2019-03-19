<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/common/meta.jsp"%>
<%@include file="/jsp/mobile/mobile-common.jsp"%>
<%@include file="/jsp/common/taglibs.jsp"%>

<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	CaseManagerFormDTO formDTO = null;	
	String ucNo = null;
	List<String> rights = null;
	String accessRights = null;
	//是否是客服
	boolean isCustomerService = false;
	// CR #2951 廠商客服
	boolean isVendorService = false;
	String actionId = null;
	if (ctx != null) {
		// 得到FormDTO
		formDTO = (CaseManagerFormDTO) ctx.getRequestParameter();
		if (formDTO != null) {
			// 获得UseCaseNo
			ucNo = formDTO.getUseCaseNo();
			isCustomerService = formDTO.getIsCustomerService();
			isVendorService = formDTO.getIsVendorService();
			actionId = formDTO.getActionId();
			accessRights = logonUser.getAccRghts().get(ucNo);
			rights = StringUtils.toList(accessRights, ",");
		} else {
			ucNo = IAtomsConstants.UC_NO_SRM_05020;
			formDTO = new CaseManagerFormDTO();
			rights = new ArrayList<String>();
		}
	} else {
		ucNo = IAtomsConstants.UC_NO_SRM_05020;
		formDTO = new CaseManagerFormDTO();
		rights = new ArrayList<String>();
	}
	
	//客戶下拉菜單
	List<Parameter> customerList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_CUSTOMER_LIST);
	//案件區域
	List<Parameter> locations = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.LOCATION.getCode());	
	// 案件狀態列表
	List<Parameter> caseStatusList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.CASE_STATUS.getCode());
	// 案件狀態集合
	List<String> caseStatusListStr = (List<String>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_CASE_STATUS_LIST_STR);
	// 問題原因列表
	List<Parameter> problemReasonList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PROBLEM_REASON_LIST_STR);
	// 問題解決方式
	List<Parameter> problemSolutionList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PROBLEM_SOLUTION_LIST_STR);
	// 問題原因(台新)列表
	List<Parameter> problemReasonTsbList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PROBLEM_REASON_TSB_LIST_STR);
	// 問題解決方式(台新)
	List<Parameter> problemSolutionTsbList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PROBLEM_SOLUTION_TSB_LIST_STR);
	// 責任歸屬
	List<Parameter> responsibityList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.RESPONSIBITY_LIST_STR);
	
%>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="isCustomerService" value="<%=isCustomerService%>" scope="page"></c:set>
<c:set var="isVendorService" value="<%=isVendorService%>" scope="page"></c:set>
<c:set var="customerList" value="<%=customerList%>" scope="page"></c:set>
<c:set var="locations" value="<%=locations%>" scope="page"></c:set>
<c:set var="caseStatusList" value="<%=caseStatusList%>" scope="page"></c:set>
<c:set var="caseStatusListStr" value="<%=caseStatusListStr%>" scope="page"></c:set>
<c:set var="problemReasonList" value="<%=problemReasonList%>" scope="page"></c:set>
<c:set var="problemSolutionList" value="<%=problemSolutionList%>" scope="page"></c:set>
<c:set var="problemReasonTsbList" value="<%=problemReasonTsbList%>" scope="page"></c:set>
<c:set var="problemSolutionTsbList" value="<%=problemSolutionTsbList%>" scope="page"></c:set>
<c:set var="responsibityList" value="<%=responsibityList%>" scope="page"></c:set>
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$CASE_CATEGORY" var="caseCategoryAttr" />
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$FUNCTION_TYPE" var="functionType" />
<html>
<head>

</head>
<body>
	 <div id="dataQuery" class="easyui-navpanel" style="position:relative;padding:20px">
        <header>
            <div class="m-toolbar">
                <div class="m-title">案件處理</div>
				<div class="m-right">
					<a href="${contextPath}/mlogoff.do" class="easyui-linkbutton" style="width:60px">登出</a>
				</div>
            </div>
        </header>
        
       	<form id="queryForm" method="post" novalidate>
       	<table style="width: 90%">
	       	<tr>
	       		<td style="width: 30%">
					<label>客戶</label></td><td style="width: 50%">
					<cafe:droplisttag
						id="queryCompanyId"
						name="queryCompanyId" 
						css="easyui-combobox"
						hasBlankValue="true"
						blankName="請選擇"
						result="${customerList }"
						selectedValue="${formDTO.isVendorAttribute == false && (formDTO.isCustomerAttribute == true || formDTO.isCustomerVendorAttribute == true)?empty customerList?'':customerList[0].value:''}"
						style="width:200px"
						javascript="editable=false 
							${formDTO.isVendorAttribute == false && (formDTO.isCustomerAttribute == true || formDTO.isCustomerVendorAttribute == true)?'disabled=true':'' }"
					></cafe:droplisttag>
				</td>
			</tr>
			<tr>
				<td style="width: 30%">
					<label>案件區域</label></td><td style="width: 50%">
					<cafe:droplisttag
						css="easyui-combobox easyui-mutil-select"
						id="queryLocation"
						name="queryLocation"
						result="${locations}"
						style="width:200px"
						blankName="請選擇(複選)" 
						hasBlankValue="true"
						javascript="multiple=true editable=false data-options=\"valueField:'value',textField:'name'\" "
					></cafe:droplisttag>
				</td>
			</tr>
			<tr>
				<td style="width: 30%">
					<label>案件狀態</label></td><td style="width: 50%">
					<cafe:droplistchecktag
						css="easyui-combobox easyui-mutil-select"
						id="queryCaseStatus"
						name="queryCaseStatus"
						result="${caseStatusList }"
						selectedValues="${caseStatusListStr}"
						style="width:200px"
						blankName="請選擇(複選)" 
						hasBlankValue="true"
						javascript="multiple=true editable=false"
					></cafe:droplistchecktag>
				</td>
			</tr>
			<tr>
				<td style="width: 30%">
					<label>特店代號</label></td><td style="width: 50%">
					<input name="queryMerchatCode" style="width:100%" id="queryMerchatCode" class="easyui-textbox" maxlength="20" data-options="validType:['maxLength[20]']" value=""></input>
				</td>
			</tr>
			<tr>
				<td style="width: 30%">
					<label>DTID</label></td><td style="width: 50%">
					<input name="queryDtid" style="width:100%" id="queryDtid" class="easyui-textbox" maxlength="8" data-options="validType:['maxLength[8]']" value=""></input>
				</td>
			</tr>
			<tr>
			<tr>
				<td style="width: 30%">
					<label>CyberEDC</label>
				</td>
				<td style="width: 50%">
					<input type="radio" name="queryMicro" id="isMicro" value="2">是</input>
	                <input type="radio" name="queryMicro" id="notMicro" value="1" checked="checked">否</input>
                </td>
            </tr>
       	</table>
			
			<div style="text-align:center;margin-top:30px">
				<a href="javascript:void(0)" id="button_query" class="easyui-linkbutton" iconcls="icon-search" style="padding:5px 0px; width:100%;">查詢</a>
			</div>
			<input type="hidden" id="actionId" name="actionId" value="queryCase"/>
			<input type="hidden" id="serviceId" name="serviceId" />
			<input type="hidden" id="useCaseNo" name="useCaseNo" />
			<input type="hidden" id="exportField" name="exportField" value="caseCategoryName,caseStatusName,customerName,merchantCode,merchantName,headerName,contactAddress,contactUser,contactUserPhone,phone,dtid,tid,edcTypeName,peripheralsName,peripherals2Name,peripherals3Name,ecrConnectionName,applicationName,connectionTypeName,netVendorName,localhostIp,gateway,netmask,cmsCase,description"/>
			<input type="hidden" id="exportQueryCompanyId" name="exportQueryCompanyId"/>
			<input type="hidden" id="tsbCustomer" name="tsbCustomer" />
		</form>
        
	</div>
	
    <div id="dataList" class="easyui-navpanel">
    	<div><span id="msg" class="red"></span></div>
        <header>
            <div class="m-toolbar">
                <div class="m-title" id="caseList">案件清單<input type="hidden" id="hideBatchFlag"/></div>
                <div class="m-left">
                    <a href="javascript:void(0)" class="easyui-linkbutton m-back" data-options="plain:true,outline:true" onclick="backQuery();" style="width:60px">Back</a>
                </div>
                <div class="m-right">
                    <a href="javascript:void(0)" id="button_detail" style="width:80px;display:none;color:red" class="easyui-linkbutton" data-options="plain:true,outline:true" onclick="openBatchDialog();">批次處理</a>
                </div>
            </div>
        </header>
        <div style="margin:0px 0 0;text-align:center">
        	<table id="dataGrid" class="easyui-datagrid"
				style="width: 100%; height: auto"
				data-options="
					fitColumns:false,
					border:false,
					nowrap : false,
					pagination:true,
					remoteSort:true,
					singleSelect:true,
					pageList:[15,30,50,100],
					pageSize:15,
					selectOnCheck : true,
					remoteSort:true,
					onDblClickRow:showDataDetail
					">
				<thead>
					<tr>
						<th data-options="field:'v',width:'5%',checkbox:true,hidden:true"></th> 
						<th data-options="field:'customerName',width:'30%',align:'center'">客戶</th>  
		                <th data-options="field:'caseStatusName',width:'19%',align:'center'">狀態</th>  
		                <th data-options="field:'dtid',width:'21%',align:'center'">DTID</th>  
		                <th data-options="field:'merchantCode',width:'35%',align:'center'">特店代碼</th> 
					</tr>
				</thead>
			</table>
        </div>
    </div>
    <div id="dataDetail" class="easyui-navpanel" style="position:relative">
    	<div style="margin-bottom:10px">
			<div><span id="resMsg" class="red"></span></div>
		</div>
    	<header>
			<div class="m-toolbar">
				<div class="m-title">案件資訊</div>
				<div class="m-left">
                    <a href="javascript:void(0)" id="button_bak" class="easyui-linkbutton m-back" data-options="plain:true,outline:true" onclick="backList();" style="width:60px;">Back</a>
                </div>
                <div class="m-right">
                    <a href="javascript:void(0)" id="button_mail" class="easyui-linkbutton" data-options="plain:true,outline:true" onclick="showMailDialog();" style="width:80px;">mail客服</a>
                </div>
            </div>
		</header>
        <footer>
            <div class="m-buttongroup m-buttongroup-justified" style="width:100%">
            	<c:if test="${fn:contains(logonUser.accRghts['SRM05020'], functionType.RESPONSED.code )}">
                	<a href="javascript:void(0)" id="button_response" disabled class="easyui-linkbutton" data-options="iconCls:'icon-large-smartart',size:'large',iconAlign:'top',plain:true" onclick="doCaseConfirm('回應', 'response', 'Responsed','')">回應</a>
                </c:if>
                <c:if test="${fn:contains(logonUser.accRghts['SRM05020'], functionType.ARRIVE.code )}">
                	<a href="javascript:void(0)" id="button_arrive" disabled class="easyui-linkbutton" data-options="iconCls:'icon-large-clipart',size:'large',iconAlign:'top',plain:true" onclick="doCaseConfirm('到場', 'arrive', 'Arrived','')">到場</a>
                </c:if>
                <c:if test="${fn:contains(logonUser.accRghts['SRM05020'], functionType.COMPLETE.code )}">
                	<a href="javascript:void(0)" id="button_complete" disabled class="easyui-linkbutton" data-options="iconCls:'icon-large-shapes',size:'large',iconAlign:'top',plain:true" onclick="doCaseConfirm('完修', 'complete', 'Completed','')">完修</a>
                </c:if>
                <c:if test="${fn:contains(logonUser.accRghts['SRM05020'], functionType.ONLINE_EXCLUSION.code )}">
                	<a href="javascript:void(0)" id="button_onlineExclusion" disabled class="easyui-linkbutton" data-options="iconCls:'icon-large-chart',size:'large',iconAlign:'top',plain:true" onclick="showOnlineExclusion('');">線上排除</a>
           		</c:if>
            </div>
        </footer>
		<div style="text-align:center;margin:30px 30px">
		<form id="dataForm" method="post" validate>
			<table style="width: 100%">
				<tr>
					<td align="right" style="width: 32%">案件類別:</td>
					<td align="left" style="width: 68%"><span id="s_caseCategoryName"></span></td>
				</tr>
				<tr>
					<td align="right">客戶名稱:</td>
					<td align="left"><span id="s_customerName"></span></td>
				</tr>
				<tr>
					<td align="right">特店代碼:</td>
					<td align="left"><span id="s_merchantCode"></span></td>
				</tr>
				<tr>
					<td align="right">特店名稱:</td>
					<td align="left"><span id="s_merchantName"></span></td>
				</tr>
				<tr>
					<td align="right">表頭:</td>
					<td align="left"><span id="s_headerName"></span></td>
				</tr>
				<tr>
					<td align="right">聯繫地址:</td>
					<td align="left"><span id="s_contactAddress"></span></td>
				</tr>
				<tr>
					<td align="right">聯絡人姓名:</td>
					<td align="left"><span id="s_contactUser"></span></td>
				</tr>
				<tr>
					<td align="right">聯絡人電話:</td>
					<td align="left"><span id="s_contactUserPhone"></span></td>
				</tr>
				<tr>
					<td align="right">行動電話:</td>
					<td align="left"><span id="s_phone"></span></td>
				</tr>
				<tr>
					<td align="right">DTID:</td>
					<td align="left"><span id="s_dtid"></span></td>
				</tr>
				<tr>
					<td align="right">TID:</td>
					<td align="left"><span id="s_tid"></span></td>
				</tr>
				<tr>
					<td align="right">設備名:</td>
					<td align="left"><span id="s_edcTypeName"></span></td>
				</tr>
		
				<tr>
					<td align="right">週邊設備1:</td>
					<td align="left"><span id="s_peripherals"></span></td>
				</tr>
				<tr>
					<td align="right">週邊設備2:</td>
					<td align="left"><span id="s_peripherals2"></span></td>
				</tr>
				<tr>
					<td align="right">週邊設備3:</td>
					<td align="left"><span id="s_peripherals3"></span></td>
				</tr>
				<tr>
					<td align="right">ECR連線:</td>
					<td align="left"><span id="s_ecrConnection"></span></td>
				</tr>
				<tr>
					<td align="right">軟體版本:</td>
					<td align="left"><span id="s_softwareVersion"></span></td>
				</tr>
				<tr>
					<td align="right">連接方式:</td>
					<td align="left"><span id="s_connectionType"></span></td>
				</tr>
				<tr>
					<td align="right">寬頻連線:</td>
					<td align="left"><span id="s_netVendorId"></span></td>
				</tr>
				<tr>
					<td align="right">本機IP:</td>
					<td align="left"><span id="s_localhostIp"></span></td>
				</tr>
				<tr>
					<td align="right">Gateway:</td>
					<td align="left"><span id="s_gateway"></span></td>
				</tr>
				<tr>
					<td align="right"> Netmask:</td>
					<td align="left"><span id="s_netmask"></span></td>
				</tr>
				<tr>
					<td align="right">其他說明:</td>
					<td align="left"><span id="s_description"></span></td>
				</tr>
				<tr>
					<td align="right">交易參數:</td>
					<td align="left">
					<u id ="s_Paramter" style="font-size:20px"></u>
						<%-- <cafe:droplisttag
							css="easyui-combobox"
							id="s_Paramter"
							name="s_Paramter"
							style="width:98%"
							blankName="請選擇" 
							hasBlankValue="true"
							javascript="editable=false data-options=\"valueField:'value',textField:'name'\" "
						>
						</cafe:droplisttag> --%>
					</td>
				</tr>
				<tr>
					<td align="right">實際執行日期:</td>
					<td align="left">
						<input class="easyui-datebox" id='dealDate' style="width:98%;"  
							data-options ="validType:'date',
										   invalidMessage:'日期格式限YYYY/MM/DD'">
									
					</td>
				</tr>
				<tr><td align="right">實際執行時間:</td>
					<td align="left">
						<cafe:droplisttag
							css="easyui-combobox"
							id="dealDateHour"
							name="dealDateHour"
							
							style="width:40%"
							blankName="請選擇" 
							hasBlankValue="true"
							javascript="editable=false data-options=\"valueField:'value',textField:'name'\" "
						></cafe:droplisttag>時<label>    </label>
						<cafe:droplisttag
							css="easyui-combobox"
							id="dealDateMin"
							name="dealDateMin"
							
							style="width:40%"
							blankName="請選擇" 
							hasBlankValue="true"
							javascript="editable=false data-options=\"valueField:'value',textField:'name'\" "
						></cafe:droplisttag>分
					</td>
				</tr>
				<tr>
					<td align="right">處理說明:</td>
					<td align="left"><textarea class='textbox-dealDescription easyui-textbox' id='dealDescription' maxlength='200' style='border-radius: 4px; border: #95B8E7 1px solid;height: 100px; width: 98%' data-options='multiline:true'></textarea></td>
				</tr>
				<tr>
					<td align="right">延期日期:</td>
					<td align="left">
						<input id="hideUpdateDate" type="hidden"/>
						<input id="hideCaseId" type="hidden"/>
						<input id="hideCaseStatus" type="hidden"/>
						<input class="easyui-datebox" id='delayDate' style="width:78%;"  
							data-options ="validType:['date','compareDateWithNowDate']">
					<c:if test="${fn:contains(logonUser.accRghts['SRM05020'], functionType.DELAY.code )}">
					<a href="javascript:void(0)" disabled id="button_delay" class="easyui-linkbutton" data-options="iconCls:'icon-large-picture',size:'large',iconAlign:'top',plain:true" onclick="doCaseConfirm('延期', 'delay', 'Delaying','')">延期</a>
					</c:if></td>
				</tr>
			</table></form>
		</div>
	</div>
	
	<div id="dataBatch" class="easyui-navpanel" style="position:relative">
    	<div style="margin-bottom:10px">
			<div><span id="b_resMsg" class="red"></span></div>
		</div>
    	<header>
			<div class="m-toolbar">
				<div class="m-title">案件批次處理</div>
				<div class="m-left">
                    <a href="javascript:void(0)" id="button_bak" class="easyui-linkbutton m-back" data-options="plain:true,outline:true" onclick="backList();" style="width:60px;">Back</a>
                </div>
                <!-- <div class="m-right">
                    <a href="javascript:void(0)" id="button_mail" class="easyui-linkbutton" data-options="plain:true,outline:true" onclick="showMailDialog();" style="width:80px;">mail客服</a>
                </div> -->
            </div>
		</header>
        <footer>
            <div class="m-buttongroup m-buttongroup-justified" style="width:100%">
            	<c:if test="${fn:contains(logonUser.accRghts['SRM05020'], functionType.RESPONSED.code )}">
                	<a href="javascript:void(0)" id="b_button_response" disabled class="easyui-linkbutton" data-options="iconCls:'icon-large-smartart',size:'large',iconAlign:'top',plain:true" onclick="doCaseConfirm('回應', 'response', 'Responsed','b_')">回應</a>
                </c:if>
                <c:if test="${fn:contains(logonUser.accRghts['SRM05020'], functionType.ARRIVE.code )}">
                	<a href="javascript:void(0)" id="b_button_arrive" disabled class="easyui-linkbutton" data-options="iconCls:'icon-large-clipart',size:'large',iconAlign:'top',plain:true" onclick="doCaseConfirm('到場', 'arrive', 'Arrived','b_')">到場</a>
                </c:if>
                <c:if test="${fn:contains(logonUser.accRghts['SRM05020'], functionType.COMPLETE.code )}">
                	<a href="javascript:void(0)" id="b_button_complete" disabled class="easyui-linkbutton" data-options="iconCls:'icon-large-shapes',size:'large',iconAlign:'top',plain:true" onclick="doCaseConfirm('完修', 'complete', 'Completed','b_')">完修</a>
                </c:if>
                <c:if test="${fn:contains(logonUser.accRghts['SRM05020'], functionType.ONLINE_EXCLUSION.code )}">
                	<a href="javascript:void(0)" id="b_button_onlineExclusion" disabled class="easyui-linkbutton" data-options="iconCls:'icon-large-chart',size:'large',iconAlign:'top',plain:true" onclick="showOnlineExclusion('b_');">線上排除</a>
           		</c:if>
            </div>
        </footer>
		<div style="text-align:center;margin:30px 20px 10px 10px">
		<form id="b_dataForm" method="post" validate>
		
			<table style="width: 100%">
				<tr>
					<td align="right" style="width: 30%">實際執行日期:</td>
					<td align="left" style="width: 70%">
						<input class="easyui-datebox" id='b_dealDate' style="width:98%;"  
							data-options ="validType:'date',
										   invalidMessage:'日期格式限YYYY/MM/DD'">
									
					</td>
				</tr>
				<tr><td align="right">實際執行時間:</td>
					<td align="left">
						<cafe:droplisttag
							css="easyui-combobox"
							id="b_dealDateHour"
							name="b_dealDateHour"
							
							style="width:42%"
							blankName="12" 
							hasBlankValue="true"
							javascript="editable=false data-options=\"valueField:'value',textField:'name'\" "
						></cafe:droplisttag>時<label>    </label>
						<cafe:droplisttag
							css="easyui-combobox"
							id="b_dealDateMin"
							name="b_dealDateMin"
							
							style="width:42%"
							blankName="30" 
							hasBlankValue="true"
							javascript="editable=false data-options=\"valueField:'value',textField:'name'\" "
						></cafe:droplisttag>分
					</td>
				</tr>
				<tr>
					<td align="right">處理說明:</td>
					<td align="left"><textarea class='textbox-dealDescription easyui-textbox' id='b_dealDescription' maxlength='200' style='border-radius: 4px; border: #95B8E7 1px solid;height: 100px; width: 98%' data-options='multiline:true'></textarea></td>
				</tr>
				<tr>
					<td align="right">延期日期:</td>
					<td align="left">
						<input class="easyui-datebox" id='b_delayDate' style="width:80%;"  
							data-options ="validType:['date','compareDateWithNowDate']"><label>    </label>
					
						<c:if test="${fn:contains(logonUser.accRghts['SRM05020'], functionType.DELAY.code )}">
						<a href="javascript:void(0)" align="right" disabled id="b_button_delay" class="easyui-linkbutton" data-options="iconCls:'icon-large-picture',size:'large',iconAlign:'top',plain:true" onclick="doCaseConfirm('延期', 'delay', 'Delaying','b_')">延期</a>
						</c:if> 
					</td>
				</tr>
				
			</table></form>
		</div>
	</div>
	
	<div id="dataOnline" style="display:none; padding: 10px 10px">
		<form id="onlineForm" method="post" validate><input type="hidden" id="onlineBatchFlag"/>
			<table style="width: 100%">
				<tr>
					<td align="right">處理工程師:</td>
					<td align="left">
						<cafe:droplisttag
							css="easyui-combobox"
							id="agentsId"
							name="agentsId"
							style="width:98%"
							blankName="請選擇" 
							hasBlankValue="true"
							javascript="editable=false required=true validType=\"ignore['請選擇']\" missingMessage=\"請輸入問題解決方式\" invalidMessage=\"請輸入處理工程師\" data-options=\"valueField:'value',textField:'name'\" "
						></cafe:droplisttag>
				</tr>
				<tr>
					<td align="right">問題原因:</td>
					<td align="left">
						<cafe:droplisttag
							css="easyui-combobox"
							id="problemReason"
							name="problemReason"
							style="width:98%"
							result="${problemReasonList }"
							blankName="請選擇" 
							hasBlankValue="true"
							javascript="editable=false required=true validType=\"ignore['請選擇']\" missingMessage=\"請輸入問題解決方式\" invalidMessage=\"請輸入問題原因\" data-options=\"valueField:'value',textField:'name'\" "
						></cafe:droplisttag>
				</tr>
				<tr>
					<td align="right">問題解決方式:</td>
					<td align="left">
						<cafe:droplisttag
							css="easyui-combobox"
							id="problemSolution"
							name="problemSolution"
							style="width:98%"
							blankName="請選擇" 
							hasBlankValue="true"
							result="${problemSolutionList }"
							javascript="editable=false required=true validType=\"ignore['請選擇']\" invalidMessage=\"請輸入問題解決方式\" missingMessage=\"請輸入問題解決方式\" data-options=\"valueField:'value',textField:'name'\" "
						></cafe:droplisttag>
				</tr>
				<tr>
					<td align="right">責任歸屬:</td>
					<td align="left">
						<cafe:droplisttag
							css="easyui-combobox"
							id="responsibity"
							name="responsibity"
							style="width:98%"
							blankName="請選擇" 
							hasBlankValue="true"
							result="${responsibityList }"
							javascript="editable=false data-options=\"valueField:'value',textField:'name'\" "
						></cafe:droplisttag>
				</tr>
			</table>
			</form>
			<div id="online-buttons" style="text-align: right;">
        		<a href="javascript:void(0)" id="button_online_comfirm" class="easyui-linkbutton" style="width: 90px" onclick="doCaseConfirm('線上排除', 'onlineExclusion', 'WaitClose','')">確認</a>
        		<a href="javascript:void(0)" id="mail_online_cancel" onclick="backData();"class="easyui-linkbutton" style="width: 90px">取消</a>
    		</div>
	</div>
 	
	<div id=mailDialog style="display:none; padding: 10px 10px">
		<span id="msgMail" style="color:red"></span>
        <textarea class='textbox-dealDescription easyui-textbox' id='mailDescription' maxlength='200' style='border-radius: 4px; border: #95B8E7 1px solid;height: 75%; width: 98%' data-options='multiline:true'></textarea>  
		<div id="dlgQ-buttons" style="text-align: right;">
      		<a href="javascript:void(0)" id="mail_linkbutton_comfirm" class="easyui-linkbutton" onclick="sendmail();" style="width: 90px">確認</a>
        	<a href="javascript:void(0)" id="mail_linkbutton_cancel" class="easyui-linkbutton" onclick="cancelMail();" style="width: 90px">取消</a>
    	</div>
	</div>
 	<div id=paramDialog style="display:none; padding: 10px 10px">
		<table style="width: 100%">
			<tr>
					<td align="right" style="width: 50%">特店代號:</td>
					<td align="left" style="width: 50%"><span id="p_merchantCode"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">MID2:</td>
					<td align="left" style="width: 50%"><span id="p_merchantCodeOther"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">TID:</td>
					<td align="left" style="width: 50%"><span id="p_tid"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">台新TID(綠界用):</td>
					<td align="left" style="width: 50%"><span id="p_TSTID"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">台新MID(綠界用):</td>
					<td align="left" style="width: 50%"><span id="p_TSMID"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">銷售交易:</td>
					<td align="left" style="width: 50%"><span id="p_saleTransaction"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">取消交易:</td>
					<td align="left" style="width: 50%"><span id="p_cancelTransaction"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">結帳交易:</td>
					<td align="left" style="width: 50%"><span id="p_checkoutTransaction"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">退貨交易:</td>
					<td align="left" style="width: 50%"><span id="p_returnTransaction"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">人工輸入:</td>
					<td align="left" style="width: 50%"><span id="p_manualInput"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">交易補登:</td>
					<td align="left" style="width: 50%"><span id="p_transactionFill"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">交易補登(Online):</td>
					<td align="left" style="width: 50%"><span id="p_transactionFillOnline"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">開放櫃號:</td>
					<td align="left" style="width: 50%"><span id="p_openNumber"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">調整金額:</td>
					<td align="left" style="width: 50%"><span id="p_adjustmentAmount"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">預先授權:</td>
					<td align="left" style="width: 50%"><span id="p_preAuthorization"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">預授完成:</td>
					<td align="left" style="width: 50%"><span id="p_preAuthorized"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">銀聯預先授權取消:</td>
					<td align="left" style="width: 50%"><span id="p_cupCancelPreAuth"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">銀聯預先授權完成取消:</td>
					<td align="left" style="width: 50%"><span id="p_cupCancelPreAuthed"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">自動撥號:</td>
					<td align="left" style="width: 50%"><span id="p_autoCall"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">小費交易:</td>
					<td align="left" style="width: 50%"><span id="p_tipTransaction"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">4DBC:</td>
					<td align="left" style="width: 50%"><span id="p_4DBC"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">分行代碼(BRANCH):</td>
					<td align="left"><span id="p_branch"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">MCC:</td>
					<td align="left" style="width: 50%"><span id="p_MCC"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">VEPS(免簽)$800:</td>
					<td align="left" style="width: 50%"><span id="p_veps"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">MASTER(免簽)S1,500:</td>
					<td align="left" style="width: 50%"><span id="p_master"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">JCB(免簽)$700:</td>
					<td align="left" style="width: 50%"><span id="p_jcb"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">CUP(免簽)S800:</td>
					<td align="left" style="width: 50%"><span id="p_cup"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">交易電話1:</td>
					<td align="left" style="width: 50%"><span id="p_phone1"></span></td>
			</tr>
			<tr>
					<td align="right" style="width: 50%">交易電話2:</td>
					<td align="left" style="width: 50%"><span id="p_phone2"></span></td>
			</tr>
		</table>
		<div id="param-buttons" style="text-align: right;">
        	<a href="javascript:void(0)" id="param_linkbutton_cancel" class="easyui-linkbutton" onclick="closeParam();" style="width: 90px">關閉</a>
    	</div>
	</div>
    <script>

    $(function(){
    	//Task #3356 增加多筆批次處理功能
        var oDiv = document.getElementById('caseList');
        var myVar;
        //長按 ‘caseList’ 啟動批次處理功能 
        oDiv.onmousedown = function () {
            myVar = setTimeout(function () {
            	if($("#hideBatchFlag").val()=='' || $("#hideBatchFlag").val()=='0'){
            		dealBatchDatagrid('1');
            	} else {
            		dealBatchDatagrid('0');
            	}
            }, 1000);
        }
        oDiv.onmouseup = function () {
            clearTimeout(myVar);
        }
    	//查詢
	 	$("#button_query").click(function(){
	 		$.mobile.go('#dataList');
			queryData(1,true);
		});
	 	loadData();
	 	javascript:dwr.engine.setAsync(false);
	 	//Task #3560
	 	ajaxService.getCompanyByCompanyCode("TSB-EDC", function(result){
			if (result != null){
				$("#tsbCustomer").val(result.companyId);
			}
		});
	 	javascript:dwr.engine.setAsync(true);
    });
  	//打開交易參數 內容的畫面
    function showParamDialog(newValue, title){
    	$("#paramDialog").css("display","");
    	$('#paramDialog').animate({scrollTop:0},"normal");
    	var vHeight = window.innerHeight;
        var vWidth = window.innerWidth;
    	$("#paramDialog").dialog({ 
 			top: 80,
 		    width: vWidth*4/5,    
 		    height: vHeight*4/5,
 		    closed: false,    
 		    buttons: "#param-buttons",    
 		    title: title,
 		    modal: true   
 		});  
    	for (var i=0; i<tradingParams.length; i++) {
    		if(tradingParams[i].transactionType==newValue){
    			if(tradingParams[i].merchantCode!=null && tradingParams[i].merchantCode!=''){
    				$("#p_merchantCode").text(tradingParams[i].merchantCode);
    			} else {
    				$("#p_merchantCode").text('');
    			}
    			if(tradingParams[i].merchantCodeOther!=null && tradingParams[i].merchantCodeOther!=''){
    				$("#p_merchantCodeOther").text(tradingParams[i].merchantCodeOther);
    			} else {
    				$("#p_merchantCodeOther").text('');
    			}
    			if(tradingParams[i].tid!=null && tradingParams[i].tid!=''){
    				$("#p_tid").text(tradingParams[i].tid);
    			} else {
    				$("#p_tid").text('');
    			}
    			var itemValue;
				itemValue = tradingParams[i].itemValue;
				if (itemValue == null || isEmpty(itemValue)) {
					itemValue = new Object();
				} else {
					itemValue=JSON.parse(itemValue);
				}
				//console.info(itemValue);
				jQuery.each(itemValue, function(i, val) {  
					if(val!=null && val!=''){
	    				$("#p_" + i).text(val);
	    			} else {
	    				$("#p_" + i).text('');
	    			}
				}); 
    		}
    	}
    }
  	//關閉交易參數詳情彈出框
    function closeParam(){
    	$("#paramDialog").css("display","none");	
		$('#paramDialog').dialog('close');
		$('#paramDialog').find('span[id^="p_"]').text('');
    }
    /**
    *查詢結果是否批次處理,隱藏或者顯示複選框與批次處理按鈕
    * flag為1：批次處理，flag不為1：不是批次處理
    */
    function dealBatchDatagrid(flag){
    	var dg = $('#dataGrid');
    	//判斷行數：0 時無需執行批量與單筆的轉換 
    	if(dg.datagrid("getRows").length>0){
    		var col = dg.datagrid('getColumnOption', 'merchantCode');
        	if(flag =='1'){
        		 $('#dataGrid').datagrid("showColumn", 'v');
                 $("#button_detail").css("display","");
                 $("#hideBatchFlag").val('1');
                 col.width = '28%';
                 dg.datagrid({singleSelect:false,onDblClickRow:function(){return false;}});
        	} else {
        		$('#dataGrid').datagrid("hideColumn", 'v');
                $("#button_detail").css("display","none");
                $("#hideBatchFlag").val('0');
                col.width = '35%';
                dg.datagrid({singleSelect:true,onDblClickRow:showDataDetail,});
        	}
        	dg.datagrid();
    	}
    }
    //打開批次處理頁面
    function openBatchDialog(){
    	if(validateSelectCase()){
    		var caseIds = '';
    		var updatedDateString = '';
    		var dtid = '';
    		var rows = $("#dataGrid").datagrid('getSelections');
    		$.mobile.go("#dataBatch");
    		//主頁面 存放異動時間
			for (var i = 0; i<rows.length; i++) {
				if(rows.length >=1 && i == rows.length -1 ){
					caseIds = caseIds + rows[i].caseId;
					updatedDateString = updatedDateString + rows[i].updatedDate+";"+ rows[i].caseId;
					dtid = dtid + rows[i].dtid;
				} else {
					caseIds = caseIds + rows[i].caseId + ",";
					updatedDateString = updatedDateString + rows[i].updatedDate + ";" + rows[i].caseId + ",";
					dtid = dtid + rows[i].dtid + ",";
				}
			}
    		//
    		$("#hideUpdateDate").val(updatedDateString);
    		$("#hideCaseStatus").val(rows[0].caseStatus);
    		
    		$("#hideCaseId").val(caseIds);
    		
    		btnControl(rows[0].caseStatus,rows[0].caseCategory,'b_');
    		checkCaseCategory = rows[0].caseCategory;
    		checkDtid = dtid;
    		checkCaseId = caseIds;
    	}
    }
    /*
	* 驗證案件狀態，類別的
	*/
	function validateSelectCase(){
		var rows = $("#dataGrid").datagrid('getSelections');
		if(!rows || rows == null ||rows.length == 0){
			$.messager.alert('提示訊息','請勾選資料');
			return false; 
		} else {
			//第一個案件類別
			var tempCaseCategory = rows[0].caseCategory;
			var tempCaseStatus = rows[0].caseStatus;
			var tempCustomerId = rows[0].customerId;
			for (var i = 0; i<rows.length; i++) {
				if ((tempCaseCategory != rows[i].caseCategory) || (tempCaseStatus != rows[i].caseStatus)) {
					$.messager.alert('提示訊息','請選擇同一案件狀態、案件類別的案件');
					return false;
				}
				//Task #3560     台新報修案不可與非台新報修案一同處理
				if(tempCaseCategory == '${caseCategoryAttr.REPAIR.code }'){
					if((tempCustomerId != rows[i].customerId) && (tempCustomerId==$("#tsbCustomer").val() || rows[i].customerId==$("#tsbCustomer").val())){
						$.messager.alert('提示訊息','台新報修案不可與非台新報修案一同處理','warning');
						return false;
					}
				}
			}
			//Task #3560  台新報修案
			if(tempCustomerId == $("#tsbCustomer").val() && tempCaseCategory == '${caseCategoryAttr.REPAIR.code }'){
				var problemReasonTsbList = eval('(' + '${fn:replace(problemReasonTsbList, "=", ":")}' + ')'); 
				$("#problemReason").combobox('loadData',initSelect(problemReasonTsbList));
				var problemSolutionTsbList = eval('(' + '${fn:replace(problemSolutionTsbList, "=", ":")}' + ')'); 
				$("#problemSolution").combobox('loadData',initSelect(problemSolutionTsbList));
			} else {
				var problemReasonList = eval('(' + '${fn:replace(problemReasonList, "=", ":")}' + ')'); 
				$("#problemReason").combobox('loadData',initSelect(problemReasonList));
				var problemSolutionList = eval('(' + '${fn:replace(problemSolutionList, "=", ":")}' + ')'); 
				$("#problemSolution").combobox('loadData',initSelect(problemSolutionList));
			}
		} 
		return true;
	}
	
    //發送mail給客服群組
    function sendmail(){
    	var row = $("#dataGrid").datagrid("getSelected");
    	var mailDescription = $("#mailDescription").textbox('getValue');
    	if(mailDescription==''|| mailDescription==null){
    		$.messager.alert("訊息","請輸入mail內容");
    		return false;
    	}
    	var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		$.blockUI(blockStyle);
    	var mailParam={
    			caseId:row.caseId,
    			caseStatus:$("#hideCaseStatus").val(),
    			mailInfo:mailDescription,
    	};
    	var isError = false;
    	$.ajax({
			url: "${contextPath}/mCaseHandle.do?actionId=sendTo",
			async: false,
			data: mailParam,
			type:'post', 
			cache:false, 
			dataType:'json', 
			success:function(json){
				if (json.success) {
					$("#mailDialog").css("display","none");	
					$('#mailDialog').dialog('close');
					$('#mailDescription').textbox('clear');
					$.unblockUI();
					$.messager.alert("訊息",json.msg);
				} else {
					isError = true;
				}
			},
			error:function(){
				isError = true;
			}
		});
    	if (isError) {
    		$.unblockUI();
    		$("#msgMail").text("mail發送失敗");
			return false;
		}
    }
    //取消發送mail 
    function cancelMail(){
    	$("#mailDialog").css("display","none");	
		$('#mailDialog').dialog('close');
		$('#mailDescription').textbox('clear');
    }

    //打開填寫mail內容的畫面
    function showMailDialog(){
    	$("#mailDialog").css("display","");
    	$("#msgMail").text('');
    	var vHeight = window.innerHeight;
        var vWidth = window.innerWidth;
    	$("#mailDialog").dialog({ 
 			top: 50,
 		    width: vWidth/2+50,    
 		    height: vHeight/2+40,
 		    closed: false,    
 		    buttons: "#dlgQ-buttons",    
 		    title: '請輸入mail內容：',    
 		    modal: true   
 		});  
    	$("#mailDescription").textbox('textbox').attr('maxlength',200);
    }
    
 // 保存查詢結果的對象
	var dataJson;
	/*
	* 查詢
	*/
	function queryData(pageIndex, hidden) {
		var grid = $("#dataGrid");
		var dgOptions = grid.datagrid("options");
		var queryParam = $('#queryForm').form("getData");
		var queryCompanyId = $("#queryCompanyId").combobox("getValue");
		if(queryCompanyId!='' && queryCompanyId != null){
			queryParam.queryCompanyId = queryCompanyId;
		}
		queryParam.IsMicro = false;
		//Task #3560 1. 可查詢到CMS案件且為有到場的狀況(看到場註記)
		queryParam.mobileQueryFlag = 'Y';
		queryParam.isMicro = $("#isMicro").prop("checked");
		var pageIndexNow = getGridCurrentPagerIndex("dataGrid");
		if(pageIndex != null){
			pageIndexNow = pageIndex;
		}
		var options = {
				url : "${contextPath}/mCaseHandle.do?actionId=queryCase",
				queryParams :queryParam,
				pageNumber:pageIndexNow,
				onLoadSuccess:function(data){
					// 保存查詢條件與結果數據
					//dataJson = $("#dataGrid").datagrid("getData");
					//var options = $("#dataGrid").datagrid("options");
					/* dataJson.pageSize = dgOptions.pageSize;
					dataJson.pageNumber = dgOptions.pageNumber;
					dataJson.queryCompanyId = $("#queryCompanyId").combobox('getValue');
					dataJson.queryLocation = $("#queryLocation").combobox('getValue');
					dataJson.queryMerchatCode = $("#queryMerchatCode").combobox('getText');
					dataJson.queryDtid = $("#queryDtid").combobox('getText'); */
				 	
					if (hidden) {
						//var logContent = JSON.stringify(dataJson);
						if (data.total == 0) {
							// 提示查無數據
							//$("#msg").text(data.msg);
							$.messager.alert("訊息",data.msg);
						} 
					}
					hidden = true;
				},
				onLoadError : function() {
					$.messager.alert("訊息","查詢資料出錯");
					//$("#msg").text("查詢資料出錯");
				}
			}
		// 清空點選排序(注：若初始化直接使用datagrid的sortName进行排序的请再次赋初值)
		if(hidden){
			options.sortName = null;
		}
		// 將查詢option追加至datagrid options
		//$.extend( dgOptions, options);
		// 加載options
		grid.datagrid(options);
		
	}
    var checkCaseCategory,checkDtid,checkCaseId,tradingParams;
    
    /**
    *打開線上排除畫面
    */
    function showOnlineExclusion(batch){
    	if(!$("#"+batch+"dataForm").form('validate')){
    		return false;
    	}
    	var dealDate = $("#"+batch+"dealDate").datebox('getValue');
    	var dealDateHour = $("#"+batch+"dealDateHour").combobox('getValue');
    	var dealDateMin = $("#"+batch+"dealDateMin").combobox('getValue');
    	if(dealDate!=null && dealDate != ''){
    		if(dealDateHour==null || dealDateHour == ''){
    			$.messager.alert("訊息",'請輸入實際執行時間');
        		return false;
    		}
    		if(dealDateMin==null || dealDateMin == ''){
    			$.messager.alert("訊息",'請輸入實際執行時間');
        		return false;
    		}
    	}
    	var cmsCase = null;
    	if(batch=='b_'){
    		var rows = $("#dataGrid").datagrid("getSelections");
    		cmsCase = rows[0].cmsCase;
    		for (var i = 0; i<rows.length; i++) {
    			if(!checkOnlineExclusion(rows[i].caseId,rows[i].dtid,rows[i].caseCategory)){
            		return;
            	}
    		}
    	}else{
    		var row = $("#dataGrid").datagrid("getSelected");
    		cmsCase = row.cmsCase;
        	if(!checkOnlineExclusion(row.caseId,row.dtid,row.caseCategory)){
        		return;
        	}
    	}
    	$("#dataOnline").css("display","");
    	var vHeight = window.innerHeight;
        var vWidth = window.innerWidth;
    	$("#dataOnline").dialog({ 
 			top: 50,
 		    width: vWidth*0.8,    
 		    height: vHeight/2+80,
 		    closed: false,    
 		    buttons: "#online-buttons",    
 		    title: '線上排除',    
 		    modal: true   
 		});   
    	javascript:dwr.engine.setAsync(false);
    	//如果是批次 則將線上排除flag設為0
    	if(batch=='b_'){
    		$("#onlineBatchFlag").val('0');
    	} else {
    		//如果不是批次 則將線上排除flag設為1
    		$("#onlineBatchFlag").val('1');
    	}
    	if(cmsCase == 'Y'){
			$("#responsibity").combobox({
				required:true,
				missingMessage:'請輸入責任歸屬',
				validType:'ignore[\'請選擇\']',
				invalidMessage:'請輸入責任歸屬'
			});
    	}
    	if(cmsCase == 'Y' && '${logonUser.companyId}'!='10000000-01'){
    		$("#agentsId").combobox('loadData',initSelect());
    		$("#agentsId").combobox('setValue','');
		} else {
			ajaxService.getUserListByCompany('${logonUser.companyId}', function(data){
				if (data != null) {
					data.insert(0,{name:'請選擇',value:''});
					$("#agentsId").combobox('loadData',data);
				} else {
					$("#agentsId").combobox('loadData',initSelect());
					$("#agentsId").combobox('setValue','');
				}
			});
		}
    	javascript:dwr.engine.setAsync(true);
    }
    /*
	* 查詢詳細 
	*/
	function showDataDetail(){
		$("#resMsg").text("");
		$("#dealDate").datebox("setValue","");
		$("#dealDescription").textbox("setValue","");
		$("#dealDateHour").combobox("setValue","");
		$("#dealDateMin").combobox("setValue","");
		$("#delayDate").datebox("setValue","");
		$("#button_arrive").linkbutton("disable");
		$("#button_complete").linkbutton("disable");
		$("#button_response").linkbutton("disable");
		$.mobile.go("#dataDetail");
		var row = $("#dataGrid").datagrid("getSelected");
		$("#s_caseCategoryName").text(row.caseCategoryName!=null?row.caseCategoryName:'');
		$("#s_customerName").text(row.customerName!=null?row.customerName:'');
		$("#s_merchantCode").text(row.merchantCode!=null?row.merchantCode:'');
		$("#s_merchantName").text(row.merchantName!=null?row.merchantName:'');
		$("#s_headerName").text(row.headerName!=null?row.headerName:'');
		$("#s_contactAddress").text(row.contactAddress!=null?row.contactAddress:'');
		$("#s_contactUser").text(row.contactUser!=null?row.contactUser:'');
		$("#s_contactUserPhone").text(row.contactUserPhone!=null?row.contactUserPhone:'');
		$("#s_phone").text(row.phone!=null?row.phone:'');
		$("#s_dtid").text(row.dtid!=null?row.dtid:'')
		$("#s_tid").text(row.tid!=null?row.tid:'');
		$("#s_edcTypeName").text(row.edcTypeName!=null?row.edcTypeName:'');
		$("#s_description").text(row.description!=null?row.description:'');
		//
		$("#hideUpdateDate").val(row.updatedDate +";"+row.caseId);
		$("#hideCaseStatus").val(row.caseStatus);
		
		$("#hideCaseId").val(row.caseId);
		//Task #3566 (Mobile)_增加顯示案件資訊
		$("#s_peripherals").text(row.peripheralsName!=null?row.peripheralsName:'');
		$("#s_peripherals2").text(row.peripherals2Name!=null?row.peripherals2Name:'');
		$("#s_peripherals3").text(row.peripherals3Name!=null?row.peripherals3Name:'');
		$("#s_ecrConnection").text(row.ecrConnectionName!=null?row.ecrConnectionName:'');
		$("#s_softwareVersion").text(row.softwareVersionName!=null?row.softwareVersionName:'');
		$("#s_connectionType").text(row.connectionTypeName!=null?row.connectionTypeName:'');
		$("#s_netVendorId").text(row.netVendorName!=null?row.netVendorName:'');
		$("#s_localhostIp").text(row.localhostIp!=null?row.localhostIp:'');
		$("#s_gateway").text(row.gateway!=null?row.gateway:'');
		$("#s_netmask").text(row.netmask!=null?row.netmask:'');
		javascript:dwr.engine.setAsync(false);
		// 'P'查詢案件交易參數內容 
		ajaxService.getCaseInfoById(row.caseId,'P', function(data){
			if(data){
				//
				tradingParams = data.caseTransactionParameterStr;
				var transactionTypeArr = [];
				$("#s_Paramter").html("");
				tradingParams = JSON.parse(tradingParams);
				if(tradingParams){
					for (var i=0; i<tradingParams.length; i++) {
						$("#s_Paramter").html($("#s_Paramter").html()+"<span onclick='showParamDialog(\""+tradingParams[i].transactionType+"\",\""+tradingParams[i].transactionTypeName+"\")'>"+tradingParams[i].transactionTypeName+"</span></br>");
						//transactionTypeArr.push({value:tradingParams[i].transactionType,name:tradingParams[i].transactionTypeName});	 	
					}	
					//交易參數下拉框 
					/* $("#s_Paramter").combobox({
						data:initSelect(transactionTypeArr),
						onChange: function(newVlaue,oldValue){
							if(newVlaue!= null && newVlaue!=''){
								showParamDialog(tradingParams, newVlaue);
							}
						},
					});
					$("#s_Paramter").combobox('setValue', ''); */
				} else {
					/* $("#s_Paramter").combobox('loadData', {value:'', name:'請選擇'});
					$("#s_Paramter").combobox('setValue', ''); */
					$("#s_Paramter").html("");
				}
			}
		});
		javascript:dwr.engine.setAsync(true);
		
		
		btnControl(row.caseStatus,row.caseCategory,'');
		checkCaseCategory = row.caseCategory;
		checkDtid = row.dtid;
		checkCaseId = row.caseId;
		//Task #3560  台新報修案
		if(row.customerId == $("#tsbCustomer").val() && row.caseCategory=='${caseCategoryAttr.REPAIR.code }'){
			var problemReasonTsbList = eval('(' + '${fn:replace(problemReasonTsbList, "=", ":")}' + ')'); 
			$("#problemReason").combobox('loadData',initSelect(problemReasonTsbList));
			var problemSolutionTsbList = eval('(' + '${fn:replace(problemSolutionTsbList, "=", ":")}' + ')'); 
			$("#problemSolution").combobox('loadData',initSelect(problemSolutionTsbList));
		} else {
			var problemReasonList = eval('(' + '${fn:replace(problemReasonList, "=", ":")}' + ')'); 
			$("#problemReason").combobox('loadData',initSelect(problemReasonList));
			var problemSolutionList = eval('(' + '${fn:replace(problemSolutionList, "=", ":")}' + ')'); 
			$("#problemSolution").combobox('loadData',initSelect(problemSolutionList));
		}
	}
    
    // 完修檢核
    function checkComplete() {
    	//CR #2551   驗證重複進建 當前一筆為裝機時，前案件必須完修  2017/12/07
		if (checkCaseCategory != '${caseCategoryAttr.INSTALL.code }'&& checkCaseCategory != '${caseCategoryAttr.OTHER.code }') {
			var flag = false;
			var checkDtidArray = checkDtid.split(',');
			var checkCaseIdArray = checkCaseId.split(',');
			for(var i = 0; i < checkCaseIdArray.length; i++){
				javascript:dwr.engine.setAsync(false);
				ajaxService.getCountByInstall(checkDtidArray[i], checkCaseIdArray[i], function(data){
					if(data=='Y'){
						$.messager.alert('提示訊息',"來自"+checkCaseIdArray[i]+"訊息：DTID之裝機案件不存在，請派工客服作廢",'');
						flag = true;
					}
				})
				javascript:dwr.engine.setAsync(true);
				if(flag){
					return false;
				}
			
				javascript:dwr.engine.setAsync(false);
				ajaxService.getCaseRepeatByInstallUncomplete(checkDtidArray[i], checkCaseIdArray[i], function(data){
					if(data.flag){
						if(data.caseId != null && data.caseId != ''){
							$.messager.alert('提示訊息',"來自"+checkCaseIdArray[i]+"訊息：請客服先將 "+ data.caseId +"結案<br>若已結案，因缺少設備資料，請聯繫系統管理員。",'');
							flag = true;
						}
					}
				})
				javascript:dwr.engine.setAsync(true);
				if(flag){
					return false;
				} 
			}
		}
		return true;
    }
    
    var handleAct,handleSt,batchFlag
	function doCaseConfirm(act, actionId, caseStatus, batch) {
    	//如果線上排除，且flag為0，則為批次處理
    	if(actionId == 'onlineExclusion' && $("#onlineBatchFlag").val()=='0'){
    		//批次處理的 id（與非批次不能共用的 ） 都有'b_'前綴 
    		batch = 'b_';
    	}
    	if ("Completed" == caseStatus) {
    		if (!checkComplete()) {
    			return;
    		}
    	}
    	var delayTime = $("#"+batch+"delayDate").datebox('getValue')
    	if(actionId=='delay'&&(delayTime==''||delayTime==null)){
    		$.messager.alert("訊息",'請輸入延期日期');
    		return false;
    	}
    	if(actionId=='onlineExclusion'&& !$('#onlineForm').form('validate')){
    		return false;
    	}
    	if(!$("#"+batch+"dataForm").form('validate')){
    		return false;
    	}
    	
    	var dealDate = $("#"+batch+"dealDate").datebox('getValue');
    	var dealDateHour = $("#"+batch+"dealDateHour").combobox('getValue');
    	var dealDateMin = $("#"+batch+"dealDateMin").combobox('getValue');
    	if(dealDate!=null && dealDate != ''){
    		if(dealDateHour==null || dealDateHour == ''){
    			$.messager.alert("訊息",'請輸入實際執行時間');
        		return false;
    		}
    		if(dealDateMin==null || dealDateMin == ''){
    			$.messager.alert("訊息",'請輸入實際執行時間');
        		return false;
    		}
    	}
    	handleAct = actionId;
    	handleSt = caseStatus;
    	batchFlag = batch;
		var confirm = $.messager.confirm(act,'確定送出?',callback);
	}
	function callback(btn){
    	if (btn) {doHandle(handleAct, handleSt, batchFlag);}
    }
    
    /*
	* 處理
	*/
	function doHandle(actionId, status, batch){
		var row = null;
		var rows = null;
		var caseIds = "";
		var caseCategory = "";
		var updatedDateString = $("#hideUpdateDate").val();
		//批次處理
    	if(batch == 'b_'){
    		rows = $("#dataGrid").datagrid('getSelections');
    		row = rows[0];
    		for (var i = 0; i<rows.length; i++) {
    			if(rows.length >=1 && i == rows.length -1 ){
					caseIds = caseIds + rows[i].caseId;
				} else {
					caseIds = caseIds + rows[i].caseId + ",";
				}
    		}
    		caseCategory = rows[0].caseCategory;
    		//單筆處理
    	} else {
    		row = $("#dataGrid").datagrid('getSelected');
    		caseIds = row.caseId;
    		caseCategory = row.caseCategory
    	} 
    	var dealDate = $("#"+batch+"dealDate").datebox('getValue');
    	var dealDateHour = $("#"+batch+"dealDateHour").combobox('getValue');
    	var dealDateMin = $("#"+batch+"dealDateMin").combobox('getValue');
    	var caseStatus = status;
   	   	if(dealDate && dealDate != ''){
   	   		dealDate = dealDate +' '+dealDateHour+':'+dealDateMin+':00';
    	}
   	   	//完修後到場狀態仍是完修
   	   	if ("Completed"==$("#hideCaseStatus").val() && "Arrived" == status) {
   	   		caseStatus = "Completed";
   	   	}
    	var actParam = {
    			caseActionId:actionId,
   	    		caseCategory:caseCategory,
   	    		caseId:caseIds,
   	    		caseStatus:caseStatus,
   	    		dealDate:dealDate,
   	    		description:$("#"+batch+"dealDescription").val(),
   	    		nowCaseStatus:$("#hideCaseStatus").val(),
   	    		updatedDateString:updatedDateString
    	};
    	if(actionId=='delay'|| actionId=='response'){
    		actParam.delayTime=$("#"+batch+"delayDate").datebox('getValue');
    	}
    	if(actionId=='onlineExclusion'){
    		var responsibityName = $("#responsibity").combobox('getText');
    		if(responsibityName=='請選擇'){
    			responsibityName = '';
    		}
    		actParam.agentsId=$("#agentsId").combobox('getValue');
    		actParam.problemReason=$("#problemReason").combobox('getValue');
    		actParam.problemSolution=$("#problemSolution").combobox('getValue');
    		actParam.responsibity=$("#responsibity").combobox('getValue');
    		actParam.problemReasonName=$("#problemReason").combobox('getText');
    		actParam.problemSolutionName=$("#problemSolution").combobox('getText');
    		actParam.responsibityName=responsibityName;
    		actParam.mobileFlag='Y';
    	}
    	if(actionId!='onlineExclusion'){
    		var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'100px',top:'40%',left:'40%',height:'40px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
        	$.blockUI(blockStyle);
    	}else{
    		commonSaveLoading("dataOnline");
    	}
    	var url = "${contextPath}/mCaseHandle.do?actionId="+actionId;
		//save
		$.ajax({
			url: url,
			data:actParam,
			type:'post', 
			cache:false, 
			dataType:'json', 
			success:function(json){
				if(json.success){
					$("#"+batch+"dealDate").datebox("setValue","");
					$("#"+batch+"dealDescription").textbox("setValue","");
					$("#"+batch+"dealDateHour").combobox("setValue","");
					$("#"+batch+"dealDateMin").combobox("setValue","");
					$("#"+batch+"delayDate").datebox("setValue","");
					$("#mailDescription").textbox("setValue","");
				}
				
				if(actionId=='onlineExclusion'){
					commonCancelSaveLoading("dataOnline");
					backData();
				}else{
					$.unblockUI();
				}
				//alert(JSON.stringify(json))
				$.messager.alert("訊息",json.msg);
				updateCaseInfo(caseIds, batch);
				//btnControl($("#hideCaseStatus").val(),row.caseCategory, batch);
				
			},
			error:function(){
				$("#"+batch+"resMsg").text("失敗");
				if(actionId=='onlineExclusion'){
					commonCancelSaveLoading("dataOnline");
				}else{
					$.unblockUI();
				}
			}
		});
	}
    //按鈕控制
    function updateCaseInfo(caseId, batch){
    	var caseStatus = '';
    	var caseCategory ='';
    	var updatedDateString= '';
    	javascript:dwr.engine.setAsync(false);
    	if(caseId != null && caseId!=''){
    		var caseIdArray = caseId.split(',');
    		//存放異動時間
			for (var i = 0; i<caseIdArray.length; i++) {
				// 查詢 案件資料和交易參數的內容
				ajaxService.getCaseInfoById(caseIdArray[i],'Y', function(data){
					if(data){
						if(caseIdArray.length >=1 && i == caseIdArray.length -1 ){
							updatedDateString = updatedDateString + data.updatedDate+";"+ caseIdArray[i];
						} else {
							updatedDateString = updatedDateString + data.updatedDate + ";" + caseIdArray[i] + ",";
						}
						$("#hideUpdateDate").val(updatedDateString);
						//
						if(i==0){
							caseStatus = data.caseStatus;
							caseCategory = data.caseCategory;
							btnControl(caseStatus,caseCategory, batch);
							$("#hideCaseStatus").val(data.caseStatus);
							//不是批次處理時，賦值
							if(batch!='b_'){
								$("#s_caseCategoryName").text(data.caseCategoryName);
								$("#s_customerName").text(data.customerName);
								$("#s_merchantCode").text(data.merchantCode);
								$("#s_merchantName").text(data.merchantName);
								$("#s_headerName").text(data.headerName);
								$("#s_contactAddress").text(data.contactAddress);
								$("#s_contactUser").text(data.contactUser);
								$("#s_contactUserPhone").text(data.contactUserPhone);
								$("#s_phone").text(data.phone);
								$("#s_dtid").text(data.dtid)
								$("#s_tid").text(data.tid);
								$("#s_edcTypeName").text(data.edcTypeName);
								$("#s_description").text(data.srmCaseHandleInfo.description!=null?data.srmCaseHandleInfo.description:'');
								//Task #3566 (Mobile)_增加顯示案件資訊
								$("#s_peripherals").text(data.srmCaseHandleInfo.peripheralsName!=null?data.srmCaseHandleInfo.peripheralsName:'');
								$("#s_peripherals2").text(data.srmCaseHandleInfo.peripherals2Name!=null?data.srmCaseHandleInfo.peripherals2Name:'');
								$("#s_peripherals3").text(data.srmCaseHandleInfo.peripherals3Name!=null?data.srmCaseHandleInfo.peripherals3Name:'');
								$("#s_ecrConnection").text(data.srmCaseHandleInfo.ecrConnectionName!=null?data.srmCaseHandleInfo.ecrConnectionName:'');
								$("#s_softwareVersion").text(data.srmCaseHandleInfo.softwareVersionName!=null?data.srmCaseHandleInfo.softwareVersionName:'');
								$("#s_connectionType").text(data.srmCaseHandleInfo.connectionTypeName!=null?data.srmCaseHandleInfo.connectionTypeName:'');
								$("#s_netVendorId").text(data.srmCaseHandleInfo.netVendorName!=null?data.srmCaseHandleInfo.netVendorName:'');
								$("#s_localhostIp").text(data.srmCaseHandleInfo.localhostIp!=null?data.srmCaseHandleInfo.localhostIp:'');
								$("#s_gateway").text(data.srmCaseHandleInfo.gateway!=null?data.srmCaseHandleInfo.gateway:'');
								$("#s_netmask").text(data.srmCaseHandleInfo.netmask!=null?data.srmCaseHandleInfo.netmask:'');
								tradingParams = data.srmCaseHandleInfo.caseTransactionParameterStr;
								$("#s_Paramter").html("");
								var transactionTypeArr = {};
								tradingParams = JSON.parse(tradingParams);
								if(tradingParams){
									for (var j=0; j<tradingParams.length; i++) {
										$("#s_Paramter").html($("#s_Paramter").html()+"<span onclick='showParamDialog(\""+tradingParams[i].transactionType+"\",\""+tradingParams[i].transactionTypeName+"\")'>"+tradingParams[i].transactionTypeName+"</span></br>");
										//transactionTypeArr.push({value:tradingParams[j].transactionType,name:tradingParams[j].transactionTypeName});
									}
									//交易參數下拉框 
									/* $("#s_Paramter").combobox({
										data:initSelect(transactionTypeArr),
										onChange: function(newVlaue,oldValue){
											if(newVlaue!= null && newVlaue!=''){
												showParamDialog(tradingParams, newVlaue);
											}
										},
									});
									$("#s_Paramter").combobox('setValue', ''); */
								} else {
									/* $("#s_Paramter").combobox('loadData', {value:'', name:'請選擇'});
									$("#s_Paramter").combobox('setValue', ''); */
									$("#s_Paramter").html("");
								}
							}
						}
					}
				});
			}
    	}
		javascript:dwr.engine.setAsync(true);
		
		
    }
    function backQuery(){
    	$.mobile.go('#dataQuery','slide','right');
    	var grid = $("#dataGrid");
		grid.datagrid("hideColumn", 'v');
		var col = grid.datagrid('getColumnOption', 'merchantCode');
        $("#button_detail").css("display","none");
        $("#hideBatchFlag").val('0');
        col.width = '35%';
       	var options = grid.datagrid('options');
       	options.total= 0;
       	options.rows=[];
        options.singleSelect=true;
        options.onDblClickRow=showDataDetail;
    }
    
    function backList(){
    	$.mobile.go('#dataList','slide','right');
    	dealBatchDatagrid('0');
    	queryData(null, true);
    }
    function backData(){
    	$("#dataOnline").css("display","none");	
		$('#dataOnline').dialog('close');
    	$("#agentsId").combobox('setValue','');
		$("#problemReason").combobox('setValue','');
		$("#problemSolution").combobox('setValue','');
		$("#responsibity").combobox('setValue','');
    }
    //按鈕控制 
    function btnControl(caseStatus,caseCategory, batch){
    	//若案件狀態不為“已回應”或“已到場” 或“延期中” 或“完修”，則顯示訊息「案件狀態不符，不可進行到場」
		////若案件狀態不為“已回應”或“已到場”，則顯示訊息「案件狀態不符，不可進行延期」。//Task #3123 延期也可以延期
		//// 若勾選資料之案件狀態為“待結案審查、已作廢、立即結案、結案”，則顯示訊息「案件狀態不符，不可進行線上排除」
		//若已回應過，不可再回應，不為“待派工”或“已派工” ,則顯示訊息「案件狀態不符，不可進行回應」
		 //若案件狀態不為 “已到場”，則顯示訊息「案件狀態不為已到場，不可進行完修」
    	if ("Responsed"==caseStatus || "Delaying"==caseStatus || "Arrived"==caseStatus || "Completed"==caseStatus) {
			$("#"+batch+"button_arrive").linkbutton('enable'); 
		}else{
			$("#"+batch+"button_arrive").linkbutton('disable'); 
		}
		if ("Arrived"==caseStatus) {
			$("#"+batch+"button_complete").linkbutton('enable'); 
		}else{
			$("#"+batch+"button_complete").linkbutton('disable'); 
		}
		if("WaitDispatch"==caseStatus || "Dispatched"==caseStatus){
			$("#"+batch+"button_response").linkbutton('enable'); 
		}else{
			$("#"+batch+"button_response").linkbutton('disable'); 
		}
		if("Responsed"==caseStatus || "Arrived"==caseStatus || "Delaying"==caseStatus){
			$("#"+batch+"button_delay").linkbutton('enable'); 
		}else{
			$("#"+batch+"button_delay").linkbutton('disable'); 
		}
		if(("WaitClose"==caseStatus || "Voided"==caseStatus || "ImmediateClose"==caseStatus || "Closed"==caseStatus || "Completed"==caseStatus)
				|| 'REPAIR'!=caseCategory){
			$("#"+batch+"button_onlineExclusion").linkbutton('disable'); 
		}else{
			$("#"+batch+"button_onlineExclusion").linkbutton('enable'); 
		}
    }
   //檢核線上排除
    function checkOnlineExclusion(caseId,dtid,caseCategory){
    	var row = $("#dataGrid").datagrid('getSelected');
    	//Bug #3067 裝機也要卡重複進件
		if (caseCategory != 'INSTALL' && caseCategory != 'OTHER') {
			javascript:dwr.engine.setAsync(false);
			flag = false;
			ajaxService.getCountByInstall(dtid, caseId, function(data){
				if(data=='Y'){
					$.messager.alert('提示訊息',"來自"+caseId+"訊息：DTID之裝機案件不存在，請派工客服作廢",'');
					flag = true;
				}
			})
			javascript:dwr.engine.setAsync(true);
			if(flag){
				return false;
			}
		}
		if (caseCategory != 'OTHER') {
			// 驗證重復進件
			javascript:dwr.engine.setAsync(false);
			
			var flag = false;
			ajaxService.getCaseRepeatList(dtid, caseId, false, function(data){
				if(data.flag){
					for(var key in data) { 
						if(key != "flag"){
							$.messager.alert('提示訊息',"來自"+key+"訊息：請客服先將"+ data[key] +"結案",'');
							flag = true;
							break;
						}
					} 
				}
			})
			javascript:dwr.engine.setAsync(true);
			if(flag){
				return false;
			}
		}
		
		if (caseCategory != 'INSTALL' && caseCategory != 'OTHER') {
			//判斷改案件的設備關聯是否被修改
			javascript:dwr.engine.setAsync(false);
			var isChange = false;
			var hasUpdateCase = false;
			
			ajaxService.getCaseLinkIsChange(dtid, caseId, function(data){
				if(data.flag){
					//最新資料檔有設備時 2018/01/30
					if(data.initEditCheckUpdate=='N'){
						// CR #2951 廠商客服			//Task #3578 新增 客戶廠商客服
						if (${isCustomerService || formDTO.isCusVendorService}) {
							$.messager.alert('提示訊息',"來自"+caseId+"訊息： "+ data["caseId"] +"於"
							+ data["closeDate"] +"已更新案件最新設備連接資料，請至處理頁面點✔重新帶入最新資料",'');
						} else {
							//CR #2551 3. 設備資料 與 最新資料檔 不一致，提醒USER 聯繫客服 2017/12/07
							// CR #2951 廠商客服
							if (${formDTO.isVendorService}) {
								if(row.vendorServiceCustomer == '${logonUser.companyId }'){
									$.messager.alert('提示訊息',"來自"+caseId+"訊息： "+ data["caseId"] +"於"
											+ data["closeDate"] +"已更新案件最新設備連接資料，請至處理頁面點✔重新帶入最新資料",'');
								} else {
									$.messager.alert('提示訊息',"來自"+caseId+"訊息： "+ data["caseId"] +"於"
											+ data["closeDate"] +"異動設備與此案件不符，請聯繫Cyber客服確認",'');
								}
							} else {
								$.messager.alert('提示訊息',"來自"+caseId+"訊息： "+ data["caseId"] +"於"
										+ data["closeDate"] +"異動設備與此案件不符，請聯繫客服確認",'');
							}
						}
					} else {
						$.messager.alert('提示訊息',"來自"+caseId+"訊息： 本案件dtid之EDC設備已拆除，請退回客服作廢",'');
					}
					isChange = true;
				} else {
					if(data.assetLinkIsChange){
						hasUpdateCase = true
					} 
				}
			});
			if(hasUpdateCase){
				//2551 更新安檢設備連接檔
				javascript:dwr.engine.setAsync(false);
				ajaxService.getChangeCaseInfoById(caseId, function(data){
					if(data){
						//如果是批次中的 最後一筆案件 或 只有一筆案件
						if(confirmEnding($("#hideUpdateDate").val(), ";" + caseId)){
							//只有一筆案件
							if($("#hideUpdateDate").val().indexOf(",") < 0){
								$("#hideUpdateDate").val(data.updatedDate + ";" + caseId);
							} else {
								//是批次中的 最後一筆案件
								$("#hideUpdateDate").val($("#hideUpdateDate").val().substring(0, $("#hideUpdateDate").val().lastIndexOf(',')));
								$("#hideUpdateDate").val($("#hideUpdateDate").val()+","+data.updatedDate + ";" + caseId);
							}
						} else {
							//不是批次中的 最後一筆案件 且有多筆， 肯定有逗號  依 ';caseId,' 分隔開
							var hideUpdateDateArray = $("#hideUpdateDate").val().split(";" + caseId+',');
							if(hideUpdateDateArray.length>1){
								//前邊沒有逗號，是 第一筆案件
								if(hideUpdateDateArray[0].indexOf(",") < 0){
									$("#hideUpdateDate").val(data.updatedDate + ";" + caseId+','+hideUpdateDateArray[1]);
								}else{//不是第一筆 也不是最後一筆
									hideUpdateDateArray[0] = hideUpdateDateArray[0].substring(0, hideUpdateDateArray[0].lastIndexOf(','));
									$("#hideUpdateDate").val(hideUpdateDateArray[0]+','+data.updatedDate + ";" + caseId + ',' + hideUpdateDateArray[1]);
								}
							}
						}
					}
				});
			} 
			javascript:dwr.engine.setAsync(true);
			if(isChange){
				return false;
			}
		}
		return true;
    }
    //判斷是否以指定字符串結尾
    function confirmEnding(str, target) {
	 	// 请把你的代码写在这里
	 	var start = str.length-target.length;
	 	var arr = str.substr(start,target.length);
	 	if(arr == target){
	 	   return true;
	 	}
	 	return false;
    }
  //向時和分賦值
    function loadData(){
    	var hour = [];
    	hour.push({name:'請選擇',value:''});
    	for(var i = 0; i < 25; i++){
    		if(i<10){
    			hour.push({name:'0'+i,value:'0'+i});
    		}else {
    			hour.push({name:i,value:i});
    		}
    	}
    	$("#dealDateHour").combobox('loadData',hour);
    	$("#b_dealDateHour").combobox('loadData',hour);
    	var min = [];
    	min.push({name:'請選擇',value:''});
    	for(var i = 0; i < 61; i++){
    		if(i<10){
    			min.push({name:'0'+i,value:'0'+i});
    		}else{
    			min.push({name:i,value:i});
    		}
    	}
    	$("#dealDateMin").combobox('loadData',min);
    	$("#b_dealDateMin").combobox('loadData',min);
    }
    </script>
	 
</body>
</html>