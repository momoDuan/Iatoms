<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetInInfoFormDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.AssetInInfoDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.web.taglib.FileUpLoad"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html >
<%@ include file="/jsp/common/common.jsp" %>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	AssetInInfoFormDTO formDTO = null;
	String ucNo = null;
	if (ctx != null) {
		formDTO = (AssetInInfoFormDTO) ctx.getResponseResult();
	} 
	if (formDTO == null) {
		formDTO = new AssetInInfoFormDTO();
		ucNo = IAtomsConstants.UC_NO_DMM_03040;
	} else {
		ucNo = formDTO.getUseCaseNo();
	}
	//廠商集合
	List<Parameter> vendorList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IAtomsConstants.ACTION_GET_VENDOR_LIST);
 	//倉庫集合
	List<Parameter> warehouseList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IAtomsConstants.ACTION_GET_WAREHOUSE_LIST);
 	//入庫批號集合
	List<Parameter> preAssetInList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IAtomsConstants.ACTION_GET_ASSET_IN_ID_LIST);
	//已入庫批號集合
	List<Parameter> assetInList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IAtomsConstants.ACTION_LIST_ASSET_IN_LIST);
	//合約編號集合
	List<Parameter> contractList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IAtomsConstants.ACTION_GET_CONTRACT_LIST);
 	//客戶
 	List<Parameter> customerList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_COMPANY_LIST);
 	//使用人
 	List<Parameter> ownerList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, AssetInInfoFormDTO.PARAM_ASSETS_OWNER_AND_USE_EMPLOYEE_NAME_LIST);
 	//當前登錄時間
 	String currentTime = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
 	//當前登錄者名稱
 	String userName = logonUser.getName();
 	//獲取當前登錄者中文名稱
	if(StringUtils.hasText(userName) && userName.indexOf("(") >0 && userName.indexOf(")") > 0){
		userName = userName.substring(userName.indexOf("(") + 1, userName.indexOf(")"));
	}
	//維護模式
	List<Parameter> maTypeList = (List<Parameter>) SessionHelper.getAttribute(request,  AssetInInfoFormDTO.USE_CASE_NO, IAtomsConstants.PARAM_MA_TYPE);
	//租賃
	String lease = IAtomsConstants.MA_TYPE_LEASE;
	//買斷
	String maTypeBuyout = IAtomsConstants.MA_TYPE_BUYOUT;
	//租賃轉買斷
	String maTypeLeaseToBuyout = IAtomsConstants.MA_TYPE_LEASE_TO_BUYOUT;
	//获取上传最大限制
	String uploadFileSize = formDTO.getUploadFileSize();
%>
<c:set var="vendorList" value="<%=vendorList%>"></c:set>
<c:set var="formDTO" value="<%=formDTO%>"></c:set>
<c:set var="userName" value="<%=userName%>"></c:set>
<c:set var="warehouseList" value="<%=warehouseList%>"></c:set>
<c:set var="preAssetInList" value="<%=preAssetInList%>"></c:set>
<c:set var="assetInList" value="<%=assetInList%>"></c:set>
<c:set var="contractList" value="<%=contractList%>"></c:set>
<c:set var="maTypeList" value="<%=maTypeList%>"></c:set>
<c:set var="currentTime" value="<%=currentTime%>"></c:set>
<c:set var="customerList" value="<%=customerList%>"></c:set>
<c:set var="lease" value="<%=lease%>"></c:set>
<c:set var="ownerList" value="<%=ownerList%>"></c:set>
<c:set var="maTypeBuyout" value="<%=maTypeBuyout%>"></c:set>
<c:set var="maTypeLeaseToBuyout" value="<%=maTypeLeaseToBuyout%>"></c:set>
<c:set var="uploadFileSize" value="<%=uploadFileSize%>"></c:set>
<html>
<head>
	<%@include file="/jsp/common/meta.jsp"%>
	<%@include  file="/jsp/common/easyui-common.jsp" %>
	<script type="text/javascript" src="${contextPath}/js/fineuploader-5.0.2.js"></script>
	<link rel="stylesheet" href="${contextPath}/css/fineuploader-5.0.2.css"/>
</head>
<body>
<div id="assetInDiv"style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
	  <div id="assetIn" class="easyui-panel" title="設備入庫作業" style="width: 98%; height: auto; " >
        <div class="easyui-tabs" id="tabs" style="width: 100%; height: auto;">
            <div title="設備入庫" style="padding: 10px 10px; ">
            <div><span id="assetInMsg" class="red"></span></div>
            	<form  method="post" id="assetInForm">
            		<input type="hidden" id = "hiddenContractId" value=""/>
            		<input type="hidden" id = "hiddenAssetTypeId" value=""/>
            		<input type="hidden" id = "hiddenAssetModel" value=""/>
            		<input type="hidden" id = "hiddenAssetBrand" value=""/>
                    <table cellpadding="6">
                        <tr>
                            <td>硬體廠商:<span class="red">*</span></td>
                            <td>
                            <cafe:droplisttag
                            	css="easyui-combobox"
                            	id="<%=AssetInInfoDTO.ATTRIBUTE.VENDOR_ID.getValue() %>"
								name="<%=AssetInInfoDTO.ATTRIBUTE.VENDOR_ID.getValue() %>" 
								result="${vendorList }"
								javascript="editable=false invalidMessage=\"請輸入硬體廠商\" data-options=\"valueField:'value',textField:'name',validType:'requiredDropList'\""
								blankName="請選擇" 
								hasBlankValue="true"
								style="width:150px"
                            ></cafe:droplisttag> 
                            </td>
                            <td>入庫批號:<span class="red">*</span></td>
                            <td>
                        	<cafe:droplisttag
                            	css="easyui-combobox"
                            	id="<%=AssetInInfoDTO.ATTRIBUTE.ASSET_IN_ID.getValue() %>"
								name="<%=AssetInInfoDTO.ATTRIBUTE.ASSET_IN_ID.getValue() %>" result="${preAssetInList }"
								javascript="editable=false invalidMessage=\"請輸入入庫批號\" data-options=\"valueField:'value',textField:'name',validType:'requiredDropList'\""
								blankName="新增批號" hasBlankValue="true"
								style="width:150px"
                            ></cafe:droplisttag>
                            </td>

                            <td>維護模式:<span class="red">*</span></td>
                            <td>
                                 <cafe:droplisttag
	                            	css="easyui-combobox"
	                            	id="<%=AssetInInfoDTO.ATTRIBUTE.MA_TYPE.getValue() %>"
									name="<%=AssetInInfoDTO.ATTRIBUTE.MA_TYPE.getValue() %>" result="${maTypeList }"
									javascript="panelheight=\"auto\" editable=false invalidMessage=\"請輸入維護模式\" data-options=\"required:true, valueField:'value',textField:'name',width:150,validType:'requiredDropList'\""
									style="width:150px"
									blankName="請選擇" hasBlankValue="true"
                            	></cafe:droplisttag>
                            	<%-- <input type="hidden" id="<%=AssetInInfoDTO.ATTRIBUTE.MA_TYPE.getValue() %>" name="<%=AssetInInfoDTO.ATTRIBUTE.MA_TYPE.getValue() %>"/>
                            	<input class="easyui-textbox" type="text"id="<%=AssetInInfoDTO.ATTRIBUTE.MA_TYPE_NAME.getValue()%>" name="<%=AssetInInfoDTO.ATTRIBUTE.MA_TYPE_NAME.getValue()%>" disabled="disabled"/> --%>
                            </td>
                        </tr>
                        <tr>
                         <td>客戶:</td>
                            <td>
                               <cafe:droplisttag
	                            	css="easyui-combobox"
	                            	id="<%=AssetInInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue()%>"
									name="<%=AssetInInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue()%>" 
									javascript="editable=false data-options=\"required:true, valueField:'value',textField:'name',width:150\" "
									result="${customerList }"
									style="width:150px"
									blankName="請選擇" hasBlankValue="true"
                            	></cafe:droplisttag> 
                            </td>
                            <td>合約編號:<span class="red">*</span></td>
                            <td>
                               <cafe:droplisttag
	                            	css="easyui-combobox"
	                            	id="<%=AssetInInfoDTO.ATTRIBUTE.CONTRACT_ID.getValue()%>"
									name="<%=AssetInInfoDTO.ATTRIBUTE.CONTRACT_ID.getValue()%>" 
									javascript="editable=false invalidMessage=\"請輸入合約編號\" data-options=\"valueField:'value',textField:'name',width:150,validType:'requiredDropList'\" "
									result="${contractList }"
									style="width:170px"
									blankName="請選擇" hasBlankValue="true"
								></cafe:droplisttag> 
							</td>
							<td>倉庫名稱:<span class="red">*</span></td>
							<td>
								<cafe:droplisttag
									css="easyui-combobox"
									id="<%=AssetInInfoDTO.ATTRIBUTE.WAREHOUSE_ID.getValue() %>"
									name="<%=AssetInInfoDTO.ATTRIBUTE.WAREHOUSE_ID.getValue() %>" result="${warehouseList }"
									javascript="editable=false required=true invalidMessage=\"請輸入倉庫名稱\" validType=\"requiredDropList\""
									blankName="請選擇"
									hasBlankValue="true"
									style="width:150px"
								></cafe:droplisttag>
							</td>
						</tr>

						<tr>
							<td>設備名稱:<span class="red">*</span></td>
							<td>
								<cafe:droplisttag
									css="easyui-combobox"
									id="<%=AssetInInfoDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue()%>"
									name="<%=AssetInInfoDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue()%>" 
									javascript="editable=false invalidMessage=\"請輸入設備名稱\" data-options=\"required:true, valueField:'value',textField:'name',width:150,validType:'requiredDropList'\" "
									style="width:150px"
									blankName="請選擇" hasBlankValue="true"
								></cafe:droplisttag> 
							</td>
							<td>設備廠牌:</td>
							<td>
								<cafe:droplisttag
									css="easyui-combobox"
									id="<%=AssetInInfoDTO.ATTRIBUTE.BRAND.getValue()%>"
									name="<%=AssetInInfoDTO.ATTRIBUTE.BRAND.getValue()%>" 
									javascript="editable=false data-options=\"valueField:'value',textField:'name',width:150\" "
									style="width:150px"
									blankName="請選擇" hasBlankValue="true"
								></cafe:droplisttag> 
							</td>
							<td>設備型號:</td>
							<td>
								<cafe:droplisttag
									css="easyui-combobox"
									id="<%=AssetInInfoDTO.ATTRIBUTE.ASSET_MODEL.getValue()%>"
									name="<%=AssetInInfoDTO.ATTRIBUTE.ASSET_MODEL.getValue()%>"
									javascript="editable=false invalidMessage=\"請輸入設備型號\" data-options=\"valueField:'value',textField:'name',width:150\" "
									style="width:150px"
									blankName="請選擇" hasBlankValue="true"
								></cafe:droplisttag> 
							</td>
						</tr>
                        <tr>      
                        	 <td>資產Owner:<span class="red">*</span></td>
                            <td>
                           	   <cafe:droplisttag
	                            	css="easyui-combobox"
	                            	id="<%=AssetInInfoDTO.ATTRIBUTE.ASSETS_OWNER.getValue()%>"
									name="<%=AssetInInfoDTO.ATTRIBUTE.ASSETS_OWNER.getValue()%>" 
									javascript="editable=false invalidMessage=\"請輸入資產Owner\" data-options=\"required:true, valueField:'value',textField:'name',width:150,validType:'requiredDropList'\" "
									result="${ownerList }"
									style="width:150px"
									blankName="請選擇" hasBlankValue="true"
                            	></cafe:droplisttag> 
                            <td>使用人:<span class="red">*</span></td>
                            <td>
                           		<cafe:droplisttag css="easyui-combobox"
	                            	id="<%=AssetInInfoDTO.ATTRIBUTE.USE_EMPLOYEE_NAME.getValue()%>"
									name="<%=AssetInInfoDTO.ATTRIBUTE.USE_EMPLOYEE_NAME.getValue()%>" 
									javascript="editable=false invalidMessage=\"請輸入使用人\" data-options=\"required:true, valueField:'value',textField:'name',width:150,validType:'requiredDropList'\" "
									result="${ownerList}"
									style="width:150px"
									blankName="請選擇" hasBlankValue="true"
                            	></cafe:droplisttag>
                            </td>
                        	 <td>保管人:</td>
                        	 <td>
                                <input class="easyui-textbox" type="text" id="<%=AssetInInfoDTO.ATTRIBUTE.KEEPER_NAME.getValue()%>" maxLength="50" name="<%=AssetInInfoDTO.ATTRIBUTE.KEEPER_NAME.getValue()%>" validType="maxLength[50]"></input>
                            </td> 
                           
                        </tr>
                        <tr>
                                            
                            <td>Cyber驗收日期:<span class="red">*</span></td>
                            <td>
                                <input class="easyui-datebox" maxlength="10" style="width: 150px" name="<%=AssetInInfoDTO.ATTRIBUTE.APPROVE_DATE.getValue()%>"  id="<%=AssetInInfoDTO.ATTRIBUTE.APPROVE_DATE.getValue()%>" required="true"  validType="date" missingMessage="請輸入Cyber驗收日期"/>
                            </td>
                            <td>客戶驗收日期:</td>
                            <td>
                                <input class="easyui-datebox" maxlength="10" id="<%=AssetInInfoDTO.ATTRIBUTE.CUSTOME_APPROVE_DATE.getValue()%>" name="<%=AssetInInfoDTO.ATTRIBUTE.CUSTOME_APPROVE_DATE.getValue()%>" style="width: 150px" validType="date"/>
                            </td>
                        </tr>
                        <tr>
                            <td>建檔人員:</td>
                            <td>
                                <input class="easyui-textbox" type="text" id="<%=AssetInInfoDTO.ATTRIBUTE.CREATE_USER_NAME.getValue() %>"  name="<%=AssetInInfoDTO.ATTRIBUTE.CREATE_USER_NAME.getValue() %>"  value="<c:out value='${userName}'/>" disabled="disabled"></input>
                            	<input  type="hidden"  id="<%=AssetInInfoDTO.ATTRIBUTE.CREATE_USER.getValue() %>" name="<%=AssetInInfoDTO.ATTRIBUTE.CREATE_USER.getValue() %>" value="<c:out value='${logonUser.employeeId}'/>" disabled="disabled">
                            </td>
                            <td>建檔日期:</td>
                            <td>                            	
                                <input class="easyui-datebox" style="width: 150px" id="<%=AssetInInfoDTO.ATTRIBUTE.CREATE_DATE.getValue() %>" name="<%=AssetInInfoDTO.ATTRIBUTE.CREATE_DATE.getValue() %>" disabled="true"/>
                            </td>
                        </tr>
                        <tr>
                            <td>說明:</td>
                            <td colspan="3">
                                <input name="<%=AssetInInfoDTO.ATTRIBUTE.COMMENT.getValue()%>" id="<%=AssetInInfoDTO.ATTRIBUTE.COMMENT.getValue()%>" class="easyui-textbox" data-options="multiline:true" value="" maxLength='200' style="height: 88px; width: 450px" validType="maxLength[200]">
                        </tr>
                        <tr>
							<td colspan="6" style="text-align: right;">
								<a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-ok" onclick="save()" id="btnSave">儲存</a>
								<a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-cancel" onclick="deleteInfo();" id = "btnDelete" disabled = "true">刪除</a>
							</td>
                        </tr>
                    </table>
               	</form>
                <form id="assetInListForm" method="post" novalidate style="width: 100%; height: auto; ">
                    <div id="tb1" style="padding: 2px 5px;">
                    	<div><span id="assetListMsg" class="red"></span></div>
                        <div>
                            設備序號:<span class="red">*</span>
                            <input class="easyui-textbox" type="text" maxLength="20"  id="addEquipmentNo" required="required" data-options="validType:['maxLength[20]','englishOrNumber[\'設備序號限英數字，請重新輸入\']']" missingMessage="請輸入設備序號" disabled="disabled"></input>
                            <input type="checkbox"  id="checkPropertyNo" onclick="checkProperty();" style="display: none"/>
                            財產編號: 
                            <input class="easyui-textbox" type="text" id="addPropertyNo" maxLength="20" data-options="disabled:true,validType:['maxLength[20]','englishOrNumber[\'財產編號限英數字，請重新輸入\']']"></input>
                            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="appendStorageRow()" id = "btnAddAsset" disabled = "true">新增</a>
                            <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-remove" onclick="deleteAssetList()" id="btnDeleteAsset" disabled = "true">刪除</a>&nbsp;
                            <div id="btnUpload" style ="display: none">
	                            <a href="#" onclick="downLoad('');" style="width: 150px;color: blue" id="downLoad">匯入格式下載</a>&nbsp;
	                           	<cafe:fileuploadTag 
	                           		id="fileupLoad"
	                           		name="fileupLoad"
	                           		uploadUrl="${contextPath}/assetIn.do" 
	                           		acceptFiles=".xls,.xlsx"
	                           		allowedExtensions="'xls', 'xlsx'"
	                           		showFileList="false"
	                           		sizeLimit = "${uploadFileSize }"
	                           		showName="匯入"
	                           		messageAlert="false"
	                           		whetherImport="true"
	                           		whetherDelete="false"
	                           		multiple="false"
	                           		whetherDownLoad="false"
	                           		showUnderline = "true"
	                           		javaScript="onSubmit:function(id,name){$('#assetListMsg').text(\"\");fileupLoad.setParams({'actionId':'upload', 'assetInId': $('#assetInId').combobox('getValue') });$('#assetListMsg').text('');},
	                           		onError:function(id, name, reason, maybeXhrOrXdr){
								 	$('#assetListMsg').text(reason);},
	                           			onComplete:showMessage"
	                           		>
	                           	</cafe:fileuploadTag>		
                            </div>
							</div>
							<form id="warrantyDateForm" method="post" class="easyui-form" novalidate style="width: 100%; height: auto;">
                        <div>
                        	<table style="width: 100%">
                        		<tr>
                        			<td width="40%">
                        				<a href="#" class="easyui-linkbutton" onclick="actualAcceptance();" id = "btnActualAcceptance" disabled = "true">實際驗收</a>
                            			<a href="#" class="easyui-linkbutton" id="btnCustomerChecked" onclick="customesAcceptance();" disabled="true">客戶實際驗收</a>
                           				<a href="#" class="easyui-linkbutton" onclick="finishAcceptance();" disabled="true" id = "btnFinishAcceptance">驗收完成</a> 
                        			</td>
                        			<td width="60%" style="text-align: right;">
                        			
                        				客戶保固日期<input class="easyui-datebox" id="<%=AssetInInfoDTO.ATTRIBUTE.CUSTOMER_WARRANTY_DATE.getValue()%>" maxlength="10" disabled="disabled" style="width: 150px" data-options="validType:'date'" >
										原廠保固日期<input class="easyui-datebox" id="<%=AssetInInfoDTO.ATTRIBUTE.FACTORY_WARRANTY_DATE.getValue()%>" maxlength="10" disabled="disabled" style="width: 150px" data-options="validType:'date'" >
										<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="storage()" id="btnStorage" disabled = "true">確認入庫</a>
                        			
                        			</td>
                        		</tr>
                        	</table>
                            
                        	<!-- <div style="display: inline; margin-right: 0px;">
                        		<form id="warrantyDateForm" method="post" class="easyui-form" novalidate style="width: 100%; height: auto;">
								
                			</form>
                        	</div> -->
                        	
                        </div>
						</form>
				</div>
				</form>
				
				<form method="post" class="easyui-form" id="storageForm">
                    <table id="storageTb" >
					</table>
				</form>
				<%-- <form id="warrantyDateForm" method="post" class="easyui-form" novalidate style="width: 100%; height: auto">
					<div style="text-align: right;">
						客戶保固日期<input class="easyui-datebox" id="<%=AssetInInfoDTO.ATTRIBUTE.CUSTOMER_WARRANTY_DATE.getValue()%>" maxlength="10" disabled="disabled" style="width: 150px" data-options="validType:'date'" >
						原廠保固日期<input class="easyui-datebox" id="<%=AssetInInfoDTO.ATTRIBUTE.FACTORY_WARRANTY_DATE.getValue()%>" maxlength="10" disabled="disabled" style="width: 150px" data-options="validType:'date'" >
						<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="storage()" id="btnStorage" disabled = "true">確認入庫</a>
					</div>
                </form> --%>
            </div>
            <div title="歷史入庫查詢" data-options="href:'${contextPath}/assetInHis.do?actionId=initHis',closed:true" style="padding:10px"></div>
        </div>
    </div>
</div>
    <script type="text/javascript">    
	//標記是否進行了新增
	var storageOrDelete = "";
	$(function(){
		//tabs改變時
		$("#tabs").tabs({
			onSelect:function(title,index){
				if(index<1){
					$("#assetInMsg").text('');
				} else {
					$("#assetListMsg").text('');
				}
			}
		});
		$("#<%=AssetInInfoDTO.ATTRIBUTE.CREATE_DATE.getValue()%>").datebox('setValue','${currentTime}');
		//入庫批號改變時
		$("#assetInId").combobox({
			onChange:function(newValue, oldValue){
				$("#assetListMsg").text("");
				$("#assetInMsg").text("");
				storageOrDelete = "";
				$("#checkPropertyNo").removeAttr("checked");
				$("#addPropertyNo").textbox('disable');
				$("#<%=AssetInInfoDTO.ATTRIBUTE.FACTORY_WARRANTY_DATE.getValue()%>").datebox('setValue', "");
				$("#<%=AssetInInfoDTO.ATTRIBUTE.CUSTOMER_WARRANTY_DATE.getValue()%>").datebox('setValue', "");
				$("#addPropertyNo").textbox('setValue', "");
				$("#addEquipmentNo").textbox('setValue', "");
				
				if (!isEmpty(newValue)) {
					ajaxService.getAssetInInfoByAssetInId(newValue, function(data){
						if (data != null) {
							javascript:dwr.engine.setAsync(false);
							var isSign = true;
							//合約編號
							$("#hiddenContractId").val(data.contractId);
							$("#hiddenAssetTypeId").val(data.assetTypeId);
							$("#hiddenAssetModel").val(data.assetModel);
							$("#hiddenAssetBrand").val(data.brand);
							$("#<%=AssetInInfoDTO.ATTRIBUTE.CONTRACT_ID.getValue()%>").combobox('setValue', "");
							//為廠商欄位賦值，判斷如果廠商下拉列表中不包含此廠商，則默認請選擇。
							selectValue(data.companyId, '<%=AssetInInfoDTO.ATTRIBUTE.VENDOR_ID.getValue() %>');
							//維護模式
							$("#<%=AssetInInfoDTO.ATTRIBUTE.MA_TYPE.getValue()%>").combobox('setValue', data.maType);
							//為倉庫欄位賦值，判斷如果倉庫下拉列表中不包含此倉庫，則默認請選擇。
							selectValue(data.warehouseId, '<%=AssetInInfoDTO.ATTRIBUTE.WAREHOUSE_ID.getValue()%>');
							//為資產OWNER欄位賦值，判斷如果資產OWNER下拉列表中不包含此資產OWNER，則默認請選擇。
							selectValue(data.owner, '<%=AssetInInfoDTO.ATTRIBUTE.ASSETS_OWNER.getValue()%>');
							//為使用人欄位賦值，判斷如果使用人下拉列表中不包含此使用人，則默認請選擇。
							selectValue(data.userId, '<%=AssetInInfoDTO.ATTRIBUTE.USE_EMPLOYEE_NAME.getValue()%>');
							//保管者
							$("#<%=AssetInInfoDTO.ATTRIBUTE.KEEPER_NAME.getValue()%>").textbox('setValue',data.keeperName);
							//創建人
							$("#<%=AssetInInfoDTO.ATTRIBUTE.CREATE_USER_NAME.getValue()%>").textbox('setValue',data.createdByName);
							$("#<%=AssetInInfoDTO.ATTRIBUTE.CREATE_USER.getValue()%>").val(data.createdById);
							//創建日期   				
							$("#<%=AssetInInfoDTO.ATTRIBUTE.CREATE_DATE.getValue()%>").datebox('setValue',data.createdDate );
							//說明
							$("#<%=AssetInInfoDTO.ATTRIBUTE.COMMENT.getValue()%>").textbox('setValue',data.comment);
							//為客戶欄位賦值，判斷如果客戶下拉列表中不包含此客戶，則默認請選擇。
							selectValue(data.customerId, '<%=AssetInInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue()%>');
							if ($("#<%=AssetInInfoDTO.ATTRIBUTE.CUSTOMER_ID.getValue()%>").val() == "") {
								selectValue(data.contractId, '<%=AssetInInfoDTO.ATTRIBUTE.CONTRACT_ID.getValue()%>');
							}
							//cyber驗收日期
							$("#<%=AssetInInfoDTO.ATTRIBUTE.APPROVE_DATE.getValue()%>").datebox('setValue',data.cyberApprovedDate);
							//客戶驗收日期
							$("#<%=AssetInInfoDTO.ATTRIBUTE.CUSTOME_APPROVE_DATE.getValue()%>").datebox('setValue',data.customerApproveDate);
							changeFactoryWarrantyDate(data.contractId, data.cyberApprovedDate);
							changeCustomerWarrantyDate(data.contractId, data.customerApproveDate);
							$("#addEquipmentNo").textbox('enable');
							$("#addEquipmentNo").textbox('enableValidation');			
							$("#<%=AssetInInfoDTO.ATTRIBUTE.FACTORY_WARRANTY_DATE.getValue()%>").datebox('enable');
							$("#<%=AssetInInfoDTO.ATTRIBUTE.FACTORY_WARRANTY_DATE.getValue()%>").datebox('enableValidation');
							$("#checkPropertyNo").css('display', "inline-block");
							$("#btnUpload").css("display", "inline-block");
							$("#btnActualAcceptance").linkbutton('enable');
							$("#btnAddAsset").linkbutton('enable');
							$('#btnDelete').linkbutton('enable');
							$("#addEquipmentNo").textbox('textbox').focus();
						} else {
							
						}
					});
				} else {
					$("#hiddenContractId").val("");
					$("#hiddenAssetTypeId").val("");
					$("#hiddenAssetModel").val("");
					$("#hiddenAssetBrand").val("");
					//置空表單數據
					$("#<%=AssetInInfoDTO.ATTRIBUTE.VENDOR_ID.getValue() %>").combobox("setValue","");
					$("#assetInId").combobox("setValue","");
					$("#<%=AssetInInfoDTO.ATTRIBUTE.MA_TYPE.getValue()%>").combobox('setValue', "");
					$("#customerId").combobox("setValue","");
					$("#contractId").combobox("setValue","");
					$("#<%=AssetInInfoDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue() %>").combobox("setValue","");
					$("#<%=AssetInInfoDTO.ATTRIBUTE.WAREHOUSE_ID.getValue() %>").combobox("setValue","");
					$("#<%=AssetInInfoDTO.ATTRIBUTE.ASSETS_OWNER.getValue() %>").combobox("setValue","");
					$("#<%=AssetInInfoDTO.ATTRIBUTE.ASSET_MODEL.getValue() %>").combobox("setValue","");
					$("#<%=AssetInInfoDTO.ATTRIBUTE.BRAND.getValue() %>").combobox("setValue","");
					$("#<%=AssetInInfoDTO.ATTRIBUTE.USE_EMPLOYEE_NAME.getValue() %>").combobox("setValue","");
					$("#<%=AssetInInfoDTO.ATTRIBUTE.KEEPER_NAME.getValue() %>").textbox("setValue","");
					$("#<%=AssetInInfoDTO.ATTRIBUTE.APPROVE_DATE.getValue() %>").datebox("setValue","");
					$("#<%=AssetInInfoDTO.ATTRIBUTE.CUSTOME_APPROVE_DATE.getValue() %>").datebox("setValue","");
					$("#<%=AssetInInfoDTO.ATTRIBUTE.COMMENT.getValue() %>").textbox("setValue","");
					$("#<%=AssetInInfoDTO.ATTRIBUTE.CREATE_USER_NAME.getValue() %>").textbox("setValue",'${userName}');
					$("#<%=AssetInInfoDTO.ATTRIBUTE.CREATE_DATE.getValue() %>").textbox("setValue",'${currentTime}');
					$("#<%=AssetInInfoDTO.ATTRIBUTE.CUSTOMER_WARRANTY_DATE.getValue()%>").datebox('disable');
					$("#<%=AssetInInfoDTO.ATTRIBUTE.FACTORY_WARRANTY_DATE.getValue()%>").datebox('disable');
					$("#addEquipmentNo").textbox('disable');
					$("#addEquipmentNo").textbox('disableValidation');
					$("#checkPropertyNo").css('display', "none");
					$("#btnUpload").css("display", "none");
					$("#btnCustomerChecked").linkbutton('disable');
					$("#btnDelete").linkbutton('disable');
					$("#btnActualAcceptance").linkbutton('disable');
					$("#btnFinishAcceptance").linkbutton('disable');
					$("#btnAddAsset").linkbutton('disable');
					$("#btnStorage").linkbutton('disable');
					$("#btnDeleteAsset").linkbutton('disable');
				}
				//獲取待入庫清單
				getAssetInList(newValue);
				javascript:dwr.engine.setAsync(true);
			}
		});
		/**
		* 根據客戶聯動合約
		*/
		$("#customerId").combobox({
			onChange:function(newValue, oldValue){
				ajaxService.getContractListByVendorId(newValue,function(data){
					if (data != null) {
						data = initSelect(data);
						$("#contractId").combobox("loadData",data);
						var selectVale = $("#hiddenContractId").val();
						if (!isEmpty(selectVale) && checkExistValue(data,selectVale)) {
							$("#contractId").combobox("setValue",selectVale);
						} else {
							$("#contractId").combobox("setValue","");
						}
						$("#hiddenContractId").val('');
						} 
					});			
				}
			});
		/**
		* 根據合約編號聯動設備名稱等
		*/
		$("#contractId").combobox({
			onChange:function(newValue, oldValue){
				if (!isEmpty(newValue)){
					ajaxService.getAssetInfoByContractNo(newValue,function(result){
						if (result != null) {
							//設備名稱
							$("#<%=AssetInInfoDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue()%>").combobox('setValue',"");
							var loadData = initSelect(result);
							//設備名稱下拉選單添加請選擇
							$("#<%=AssetInInfoDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue()%>").combobox("loadData", loadData);
							var selectVale = $("#hiddenAssetTypeId").val();
							//判斷選中值是否存在於下拉單中,存在則選中
							if (!isEmpty(selectVale) && checkExistValue(loadData,selectVale)) {
								$("#<%=AssetInInfoDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue()%>").combobox('setValue',selectVale);
							}
							$("#hiddenAssetTypeId").val("");
						}
					});
				} else {
					//置空
					$("#<%=AssetInInfoDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue()%>").combobox("setValue","");
					$("#<%=AssetInInfoDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue()%>").combobox("loadData",initSelect(null));		
				}
			} 
		});
		/**
		* 根據設備名稱聯動設備型號
		*/
		$("#assetTypeId").combobox({
			onChange:function(newValue, oldValue){
				if (!isEmpty(newValue)){
					ajaxService.getAssetModelAndBrand(newValue,function(data){
						if (data != null) {
							$("#<%=AssetInInfoDTO.ATTRIBUTE.ASSET_MODEL.getValue()%>").combobox("setValue",'');
							//設備名稱下拉選單添加請選擇
							var loadData = initSelect(data.model);
							$("#<%=AssetInInfoDTO.ATTRIBUTE.ASSET_MODEL.getValue()%>").combobox("loadData",loadData);
							var selectVale = $("#hiddenAssetModel").val();
							//判斷選中值是否存在於下拉單中,存在則選中
							if (!isEmpty(selectVale) && checkExistValue(loadData,selectVale)) {
								$("#<%=AssetInInfoDTO.ATTRIBUTE.ASSET_MODEL.getValue()%>").combobox('setValue',selectVale);
							} else {
								$("#<%=AssetInInfoDTO.ATTRIBUTE.ASSET_MODEL.getValue()%>").combobox('setValue',"");
							}
							$("#hiddenAssetModel").val("");
							//為廠牌欄位賦值
							loadData = initSelect(data.brand);
							$("#<%=AssetInInfoDTO.ATTRIBUTE.BRAND.getValue()%>").combobox("loadData",loadData);
							var selectVale = $("#hiddenAssetBrand").val();
							//判斷選中值是否存在於下拉單中,存在則選中
							if (!isEmpty(selectVale) && checkExistValue(loadData, selectVale)) {
								$("#<%=AssetInInfoDTO.ATTRIBUTE.BRAND.getValue()%>").combobox('setValue',selectVale);
							} else {
								$("#<%=AssetInInfoDTO.ATTRIBUTE.BRAND.getValue()%>").combobox('setValue',"");
							}
							$("#hiddenAssetBrand").val("");
						}
					});
				} else {
					//置空
					$("#<%=AssetInInfoDTO.ATTRIBUTE.ASSET_MODEL.getValue()%>").combobox("setValue","");
					$("#<%=AssetInInfoDTO.ATTRIBUTE.ASSET_MODEL.getValue()%>").combobox("loadData",initSelect(null));
					$("#<%=AssetInInfoDTO.ATTRIBUTE.BRAND.getValue()%>").combobox("setValue","");
					$("#<%=AssetInInfoDTO.ATTRIBUTE.BRAND.getValue()%>").combobox("loadData",initSelect(null));
				}
			} 
		});
		
		// 查詢datagrid初始化
		setTimeout("initQueryDatagrid();",5);
	});
	
	/*
	*初始化查詢datagrid1
	*/
	function initQueryDatagrid(){
		var grid = $("#storageTb");
		if(!grid.hasClass("easyui-datagrid")){
			grid.attr("style", "width: 100%; height: auto;");
			// 查詢列
			var datagridColumns = [
				{field:'v',width:40,halign:'center',checkbox:true},
				{field:'serialNumber',halign:'center',width:170,sortable:true,title:"設備序號"},
				{field:'propertyId',halign:'center',width:170,sortable:true,title:"財產編號"},
				{field:'assetName',halign:'center',width:160,sortable:true,title:"設備名稱"},
				{field:'keeperName',halign:'center',width:140,sortable:true,title:"保管人"},
				{field:'isChecked',halign:'center',width:90,align:'center',
                	formatter:function formatterCheckbox(value,row,index){
						return settingCheckbox(value,row,index);
				},title:"實際驗收"},
				{field:'isCustomerChecked',halign:'center',align:'center',width:110,
					formatter:function formatterCheckbox(value,row,index){
						return settingCheckbox(value,row,index);
				},title:"客戶實際驗收"},
				{field:'updateUserName',halign:'center',width:140,sortable:true,title:"異動人員"},
				{field:'updateDate',halign:'center',width:190,align:'center',formatter:formatToTimeStamp,sortable:true,sortable:true,title:"異動日期"},
				{field:'assetInId',halign:'center',hidden:true}
			];
			// 創建datagrid
			grid.datagrid({
				title:"待入庫清單",
				columns:[datagridColumns],
			    iconCls: 'icon-edit',
		        rownumbers:true,
		        pagination:true,
			    singleSelect: false,
			    pageNumber:0,
			    method: 'post',
			    fitColumns:false,
			    id:'assetId',
			    pageList:[15,30,50,100],
				pageSize:15,
			    onClickRow: deleteButton,
				onSelect: deleteButton,
				onCheck: deleteButton,
				onCheckAll: deleteButton,
				onUncheck: deleteButton,
				onUncheckAll: deleteButton,
				nowrap:false,
			    sortOrder:'asc'
			});
			
		}
	}
	
	
	/**
	* 為下拉框賦值，如果傳入的值存在於下拉列表中，則選中。否則默認請選擇。
	* value：下拉框需要選中的值
	* id：需要賦值的下拉框id
	*/
	function selectValue(value, id) {
		//獲取需要賦值的下拉列表
		var selectOptions = document.getElementById(id).options;
		//核檢該下拉列表是否包含此值。
		var isSign = checkExistValue(selectOptions, value);
		if (isSign) {
			$("#"+id).combobox('setValue', value);
		} else {
			$("#"+id).combobox('setValue', "");
		}
	}
	/**
	* 保存以及初始化入庫批號時，給原廠保固日期賦值
	*/
	function changeFactoryWarrantyDate (contractId, cyberApprovedDate) {
		var date = new Date(cyberApprovedDate);
		ajaxService.getContractWarranty(contractId,"factoryWarranty",null, function(result){
			if (result == null) {
				result = 0;
			}
			date.setMonth(date.getMonth() + result);
			$("#<%=AssetInInfoDTO.ATTRIBUTE.FACTORY_WARRANTY_DATE.getValue()%>").datebox('setValue', date);
		});
	}
	/**
	* 保存以及初始化入庫批號時，給客戶保固日期賦值
	*/
	function changeCustomerWarrantyDate (contractId, customerApproveDate) {
		var maType = $("#<%=AssetInInfoDTO.ATTRIBUTE.MA_TYPE.getValue() %>").combobox("getValue");
		if (maType == '${lease}' || maType == '${maTypeLeaseToBuyout}') {
			$("#<%=AssetInInfoDTO.ATTRIBUTE.CUSTOMER_WARRANTY_DATE.getValue()%>").datebox('setValue',"");
			$("#<%=AssetInInfoDTO.ATTRIBUTE.CUSTOMER_WARRANTY_DATE.getValue()%>").datebox('disable');
		} else {
			$("#<%=AssetInInfoDTO.ATTRIBUTE.CUSTOMER_WARRANTY_DATE.getValue()%>").datebox('enable');
			if (customerApproveDate != "" && customerApproveDate != null) {
				var date = new Date(customerApproveDate);
				ajaxService.getContractWarranty(contractId,null, "customerWarranty", function(result){
					if (result == null) {
						result = 0;
					}
					date.setMonth(date.getMonth() + result);
					$("#<%=AssetInInfoDTO.ATTRIBUTE.CUSTOMER_WARRANTY_DATE.getValue()%>").datebox('setValue', date);
				});
			}else {
				$("#<%=AssetInInfoDTO.ATTRIBUTE.CUSTOMER_WARRANTY_DATE.getValue()%>").datebox('setValue', "");
			}
		}
	} 
	/**
	* 勾選財產編號checkbobox
	*/
	function checkProperty(){
		var checkPropertyNo = document.getElementById("checkPropertyNo");
		if (checkPropertyNo.checked) {
			$("#addPropertyNo").textbox('enable');
			$("#addPropertyNo").textbox('enableValidation');
		} else {
			$("#addPropertyNo").textbox('setValue','');
			$("#addPropertyNo").textbox('disable');
		}
	}
	/**
	* 保存入庫基本信息
	*/
	function save() {
		//驗證表單
		$("#assetInMsg").text("");
		var controls = ['companyId','maType','contractId','warehouseId',
			'warehouseId','assetTypeId','owner','userId','cyberApprovedDate'];
		if (validateForm(controls) && $("#assetInForm").form('validate')) {
			//獲取表單數據
			var param = $('#assetInForm').serialize();
			var owner = $("#owner").combobox("getValue");
			var user = $("#userId").combobox("getValue");
			if (owner == user) {
				var maType = $("#maType").combobox("getValue");
				if (maType != "${maTypeBuyout}") {
					$("#assetInMsg").text("若OWNER=使用人，維護模式必須是買斷");
					return false;
				}
			}
			var assetInId = $("#assetInId").combobox("getValue");
			var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			// 形成遮罩
			$.blockUI(blockStyle1);
			$.ajax({
				url:"${contextPath}/assetIn.do?actionId=<%=IAtomsConstants.ACTION_SAVE%>",
				data:param,
				type:'post', 
				cache:false, 
				dataType:'json', 
				success:function(json) {
					if (json.success) {
						javascript:dwr.engine.setAsync(false);
						ajaxService.getAssetInIdList("N",function(data){
							data = initSelect(data,"新增批號");
							$("#assetInId").combobox('loadData', data);
							$('#assetInId').combobox('setValue', json.queryAssetInId);
						});
						if (assetInId != "") {
							getAssetInList(assetInId);
							changeFactoryWarrantyDate($("#<%=AssetInInfoDTO.ATTRIBUTE.CONTRACT_ID.getValue()%>").combobox("getValue"), 
									$("#<%=AssetInInfoDTO.ATTRIBUTE.APPROVE_DATE.getValue()%>").datebox("getValue"));
							changeCustomerWarrantyDate($("#<%=AssetInInfoDTO.ATTRIBUTE.CONTRACT_ID.getValue()%>").combobox("getValue"),
									$("#<%=AssetInInfoDTO.ATTRIBUTE.CUSTOME_APPROVE_DATE.getValue()%>").datebox("getValue"));
							$("#addEquipmentNo").textbox('textbox').focus();
						}
						$("#assetInMsg").text(json.msg);
						javascript:dwr.engine.setAsync(true);
						// 去除遮罩
						$.unblockUI();
					} else {
						$("#assetInMsg").text(json.msg);
						// 去除遮罩
						$.unblockUI();
					}
				},
				error:function(){
					$.messager.alert('提示', "保存資料出錯", 'error');
				}
			});
		} 
	}
	/**
	* 獲取刪除入庫基本信息參數
	*/
	function getParams(){
		var params = {
			assetInId:$("#<%=AssetInInfoDTO.ATTRIBUTE.ASSET_IN_ID.getValue()%>").combobox('getValue')
		};
		return params;
	};
	/**
	* 刪除入庫基本信息	
	*/
	function deleteInfo(){
		$("#assetListMsg").text("");
		var deleteAssetInId = $("#assetInId").combobox('getValue');
		if (isEmpty(deleteAssetInId)) {
			$.messager.alert('提示', "請選擇入庫批號", 'info');
			return false;
		}
		comfirmDelete(function() {
			var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			// 形成遮罩
			$.blockUI(blockStyle);
			var params = getParams();
			$.ajax({
				url:"${contextPath}/assetIn.do?actionId=<%=IAtomsConstants.ACTION_DELETE%>",
				data:params,
				type:'post', 
				cache:false,
				dataType:'json',
				success:function(json) {
					// 去除遮罩
					$.unblockUI();
					if (json.success) {
						javascript:dwr.engine.setAsync(false);
						//重新加載入庫批號下拉框
						ajaxService.getAssetInIdList("N",function(data){
							data = initSelect(data,"新增批號");
							$("#assetInId").combobox('loadData',data);
							$("#assetInId").combobox('setValue','');
							getAssetInList('');
						});
						$("#assetInMsg").text(json.msg);
						javascript:dwr.engine.setAsync(true);
					} else {
						$("#assetInMsg").text(json.msg);
					}
				},
				error:function(){
					// 去除遮罩
					$.unblockUI();
					$.messager.alert('提示', "刪除資料出錯", 'error');
				}
			});
		});
		
	}
	/*
	* 新增設備序號
	*/
	function appendStorageRow() {
		$("#assetInMsg").text("");
		$("#assetListMsg").text("");
		//入庫批號
		var assetInId = $("#assetInId").combobox('getValue');
		if(isEmpty(assetInId)){
			$.messager.alert("提示","請選擇入庫批號","info");
			return false;
		}
		//財產編號
		var addPropertyNo = $("#addPropertyNo").textbox('getValue');
		//設備序號
		var addEquipmentNo = $("#addEquipmentNo").textbox('getValue');
		//參數
		var param = {
			actionId:'<%=IAtomsConstants.ACTION_ADD_ASSET_IN_LIST%>',
			assetInId:assetInId,
			serialNumber:addEquipmentNo,
			propertyId:addPropertyNo
		};
		if ($("#assetInListForm").form('validate'))	{
			$.ajax({
				url:"${contextPath}/assetIn.do",
				data:param,
				type:'post',
				cache:false,
				dataType:'json',
				success:function(json) {
					//清空設備序號，財產編號
					$("#addPropertyNo").textbox('setValue',"");
					$("#addPropertyNo").textbox("disable");
					$("#addEquipmentNo").textbox('setValue',"");
					$("#checkPropertyNo").removeAttr("checked");
					$("#addEquipmentNo").textbox('textbox').focus();
					if (json.success) {
						//重新加載 入庫清單
						$("#storageTb").datagrid('reload');
					} else {
						$("#assetListMsg").text(json.msg);
					}
				},
				error:function(){
					$.messager.alert('提示', "新增資料出錯", 'error');
				}
			});	
		}
	}
	/**
	* 刪除設備入庫明細
	*/
	function deleteAssetList() {
		//獲取選中行
		$("#assetInMsg").text("");
		$("#assetListMsg").text("");
		var rows = $('#storageTb').datagrid('getSelections');
		//入庫批號
		var assetInId = $("#assetInId").combobox('getValue');
		if (rows.length > 0){
		comfirmDelete(function() {
			var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			$.blockUI(blockStyle);
			//需要刪除的id
			var deleteId = getUpdateAssetListIds();
			<%-- for (var i = 0; i < rows.length; i++){
				deleteId += "<%=IAtomsConstants.MARK_QUOTES%>" 
					+ rows[i].assetId 
					+ "<%=IAtomsConstants.MARK_QUOTES%>" 
					+ "<%=IAtomsConstants.MARK_SEPARATOR%>";
			} --%>
			//deleteId = deleteId.substring(0,deleteId.length-1);
			$.ajax({
				url:"${contextPath}/assetIn.do",
				data:{actionId:'<%=IAtomsConstants.ACTION_DELETE_ASSET_IN_LIST%>', deleteAssetListIds: deleteId, queryAssetInId:assetInId},
				type:'post',
				cache:false,
				dataType:'json',
				success:function(json) {
					$.unblockUI();
					if (json.success) {
						//待入庫清單reload
						$('#storageTb').datagrid('reload');
						$("#addEquipmentNo").textbox('textbox').focus();
					} else {
						$("#assetListMsg").text(json.msg);
					}
				},
				error:function(){
					$.unblockUI();
					$("#assetListMsg").text("刪除資料出錯");
				}
			});
		});
		} else {
			$("#assetListMsg").text("請勾選資料");
		}
	}
	function getUpdateAssetListIds() {
		var rows = $('#storageTb').datagrid('getSelections');
		var id = null;;
		var assetInListIds = [];
		for (var i = 0; i < rows.length; i++){
			id = rows[i].assetId;
			assetInListIds.push(id);
			<%-- deleteId += "<%=IAtomsConstants.MARK_QUOTES%>" 
				+ rows[i].assetId 
				+ "<%=IAtomsConstants.MARK_QUOTES%>" 
				+ "<%=IAtomsConstants.MARK_SEPARATOR%>"; --%>
		}
		return JSON.stringify(assetInListIds);
	}
	/**
	* 初始化實際驗收與客戶實際驗收複選框
	*/
	function settingCheckbox(value, row, index){
		if (value == "Y") {
			return '<input type="checkbox" checked="checked" disabled>';
		} else {
			return '<input type="checkbox" disabled>';
		}
	}
	/**
	* 實際驗收
	*/
	function actualAcceptance(){
		$("#assetInMsg").text("");
		$("#assetListMsg").text("");
		//獲取選中行
		var rows = $('#storageTb').datagrid('getSelections');
		var assetInfoId = $("#<%=AssetInInfoDTO.ATTRIBUTE.ASSET_IN_ID.getValue() %>").combobox('getValue');
		if (rows.length >0) {
			var id = getUpdateAssetListIds();
			var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			// 形成遮罩
			$.blockUI(blockStyle1);
			$.ajax({
				url:"${contextPath}/assetIn.do",
				data:{actionId:'<%=IAtomsConstants.ACTION_ACTUAL_ACCEPTANCE%>', deleteAssetListIds:id, queryAssetInId:assetInfoId},
				type:'post',
				cache:false,
				dataType:'json',
				success:function(json) {
					if (json.success) {
						if (json.actualAcceptance) {
							$("#btnCustomerChecked").linkbutton('enable');
							$("#btnFinishAcceptance").linkbutton('enable');
						}
						//待入庫清單reload
						$('#storageTb').datagrid('reload');
						$("#assetListMsg").text(json.msg);
						} else {
							$("#assetListMsg").text(json.msg);
						}
						// 去除遮罩
						$.unblockUI();
					},
				error:function(){
					$.messager.alert('提示', "實際驗收失敗", 'error');
					// 去除遮罩
					$.unblockUI();
				}
			});	 		
		} else {
			$("#assetListMsg").text("請勾選資料");
		}
	}
	/**
	* 客戶驗收
	*/
	function customesAcceptance(){
		$("#assetInMsg").text("");
		$("#assetListMsg").text("");
		//獲取選中行
		var rows = $('#storageTb').datagrid('getSelections');
		if (rows.length > 0) {
			var updateId = getUpdateAssetListIds();
			var param = {
				actionId:"<%=IAtomsConstants.ACTION_CUSTOMES_ACCEPTANCE%>",
				deleteAssetListIds:updateId,
				queryAssetInId:$("#assetInId").combobox('getValue'),
				customerWarrantyDate:$("#<%=AssetInInfoDTO.ATTRIBUTE.CUSTOMER_WARRANTY_DATE.getValue()%>").datebox('getValue')
			};
			var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			// 形成遮罩
			$.blockUI(blockStyle1);
			$.ajax({
				url:"${contextPath}/assetIn.do",
				data:param,
				type:'post',
				cache:false,
				dataType:'json',
				success:function(json) {
					$.messager.progress('close');
					if (json.success) {
						//待入庫清單reload
						$('#storageTb').datagrid('reload');
						$("#assetListMsg").text(json.msg);
					} else {
						$("#assetListMsg").text(json.msg);
					}
					// 去除遮罩
					$.unblockUI();
				},
				error:function(){
					$.messager.progress('close');
					$.messager.alert('提示', "客戶驗收失敗", 'error');
					// 去除遮罩
					$.unblockUI();
				}
			});	
		} else {
			$("#assetListMsg").text("請勾選資料");
		}
	}
	/**
	* 驗收完成
	*/
	function finishAcceptance(){
		$("#assetInMsg").text("");
		$("#assetListMsg").text("");
		//入庫批號
		var assetInId = $("#assetInId").combobox('getValue');
		var guaranteeDate = $("#<%=AssetInInfoDTO.ATTRIBUTE.FACTORY_WARRANTY_DATE.getValue()%>").datebox('getValue');
		if (!isEmpty(assetInId)) {
			var param = {
				actionId:"<%=IAtomsConstants.ACTION_FINISH_ACCEPTANCE%>",
				assetInId:assetInId,
				factoryWarrantyDate:guaranteeDate
			};
			var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			// 形成遮罩
			$.blockUI(blockStyle1);
			$.ajax({
				url:"${contextPath}/assetIn.do",
				data:param,
				type:'post',
				cache:false,
				dataType:'json',
				success:function(json) {
					if (json.success) {
						$("#btnStorage").linkbutton('enable');
						$("#assetListMsg").text(json.msg);
					} else {
						$("#assetListMsg").text(json.msg);
					}
					// 去除遮罩
					$.unblockUI();
				},
				error:function(){
					// 去除遮罩
					$.unblockUI();
					$.messager.alert('提示', "驗收資料出錯", 'error');
				}
			});	
		} else {
			$.messager.alert("提示","請選擇入庫批號",'info');
		}
	}
	/**
	* 匯入返回處理
	*/
	function showMessage(id, fileName, response, maybeXhrOrXdr) {
		$("#assetListMsg").text("");
		$("#assetInMsg").text("");
		//保存數據時，在上傳文件前驗證SESSION是否過期
		if (maybeXhrOrXdr) {
			if (!sessionTimeOut(maybeXhrOrXdr)) {
				return false;
			}
		}
		if (response.success) {
			$('#storageTb').datagrid('reload');
			$("#assetListMsg").text(response.msg);
		} else {
			if (response.fileName != null){
				$.messager.confirm('確認對話框','匯入失敗，是否下載匯入錯誤檔？', function(result){
					if (result) {
						downLoad(response.fileName, response.errorFilePath);
					}
				});
			} else {
				$("#assetListMsg").text(response.msg);
			}
		}
	}
	/**
	* 下載文本
	*/
	function downLoad(fileName, filePath){
		$("#assetInMsg").text('');
		$("#assetListMsg").text("");
		var path = "";
		var name = "";
		if(isEmpty(fileName)){//匯入格式下載
			ajaxService.checkExsitDownLoadFile("<%=AssetInInfoFormDTO.UPLOAD_FILE_NAME_EN%>",function(data){
			if (data){
				createSubmitForm("${contextPath}/assetIn.do","actionId=<%=IAtomsConstants.ACTION_DOWNLOAD%>&fileName="+fileName,"post");
			} else {
				$("#assetListMsg").text('文件不存在');
			}
		});	
		} else {//匯入錯誤訊息文本下載
			createSubmitForm("${contextPath}/assetIn.do","actionId=<%=IAtomsConstants.ACTION_DOWNLOAD%>&fileName="+fileName+"&errorFilePath="+filePath,"post");
		}
	}
	/**
	* 入庫
	*/
	function storage(){
		//入庫批號
		//驗證表單
		$("#assetInMsg").text("");
		$("#assetListMsg").text("");
		var controls = ['companyId','maType','contractId','warehouseId',
			'warehouseId','assetTypeId','owner','userId','cyberApprovedDate','customerWarrantyDate','factoryWarrantyDate'];
		if (validateForm(controls) && $("#assetInForm").form('validate')) {
			var assetInId = $("#assetInId").combobox('getValue');
			var owner = $("#owner").combobox("getValue");
			var user = $("#userId").combobox("getValue");
			if (owner == user) {
				var maType = $("#maType").combobox("getValue");
				if (maType != "${maTypeBuyout}") {
					$("#assetInMsg").text("若OWNER=使用人，維護模式必須是買斷");
					return false;
				}
			}
			if (!isEmpty(assetInId)) {
				var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
				$.messager.confirm('確認對話框','確認入庫?', function(confirm) {
					if (confirm) {
						// 形成遮罩
						$.blockUI(blockStyle1);
						
						var customerApproveDate = $("#<%=AssetInInfoDTO.ATTRIBUTE.CUSTOME_APPROVE_DATE.getValue()%>").datebox('getValue');	
						if (isEmpty(customerApproveDate)) {
							var maType = $("#<%=AssetInInfoDTO.ATTRIBUTE.MA_TYPE.getValue() %>").combobox("getValue");
							if (maType != '${lease}' && maType != '${maTypeLeaseToBuyout}') {
								// 去除遮罩
								$.unblockUI();
								$("#assetInMsg").text("請輸入客戶驗收日期");
								$("#assetInDiv").animate({scrollTop: 0}, 100);
								return false;
							}
						}
						var param = $('#assetInForm').serialize();
						param.customerWarrantyDate = $("#<%=AssetInInfoDTO.ATTRIBUTE.FACTORY_WARRANTY_DATE.getValue()%>").datebox('getValue');
						param.customerWarrantyDate = $("#<%=AssetInInfoDTO.ATTRIBUTE.CUSTOMER_WARRANTY_DATE.getValue()%>").datebox('getValue');
					$.ajax({
							url:"${contextPath}/assetIn.do?actionId=<%=IAtomsConstants.ACTION_STORAGE%>",
							data:param,
							type:'post',
							cache:false,
							dataType:'json',
							success:function(json) {
								if (json.success) {
									javascript:dwr.engine.setAsync(false);
									storageOrDelete = "storage";
									//重新加載入庫批號下拉框
									ajaxService.getAssetInIdList("N",function(data){
										data = initSelect(data,"新增批號")
										$("#assetInId").combobox('loadData',data);
										$("#assetInId").combobox('setValue',"");
										getAssetInList('');
									});
									//重新加載入庫批號查詢下拉選
									ajaxService.getAssetInIdList("Y",function(data){
										data = initSelect(data);
										$("#<%=AssetInInfoFormDTO.QUERY_ASSET_IN_ID%>").combobox('loadData',data);
										$("#<%=AssetInInfoFormDTO.QUERY_ASSET_IN_ID%>").combobox('setValue','');
									});
									$("#assetInMsg").text(json.msg);
									javascript:dwr.engine.setAsync(true);
								} else {
									$("#assetListMsg").text(json.msg);
								}
								// 去除遮罩
								$.unblockUI();
							},
							error:function(){
								$.messager.progress('close');
								$.messager.alert('提示', "驗收資料出錯", 'error');
								// 去除遮罩
								$.unblockUI();
							}
						}); 
						
					}
				});	
			} else {
				$.messager.alert("提示","請選擇入庫批號",'info');
			}
			
		}
	}
	/**
	* 獲取待入庫清單
	*/
	function getAssetInList(assetInfoId){
		$("#assetListMsg").text("");
		var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		$("#storageTb").datagrid({
			url:'${contextPath}/assetIn.do?actionId=<%=IAtomsConstants.ACTION_LIST_ASSET_IN_LIST%>&queryAssetInId='+assetInfoId,
			autoRowHeight:true,
			pageNumber:1,
			loadMsg: '',
			sortName:'',
			onLoadError : function(data) {
				// 去除遮罩
				$.unblockUI();
			},
			onBeforeLoad : function () {
				// 形成遮罩
				$.blockUI(blockStyle);
				$("#assetListMsg").text("");
				var pageIndex = getGridCurrentPagerIndex("storageTb");
				if (pageIndex != 1) {
					$("#assetInMsg").text("");
				}
			},
			onLoadSuccess : function(data) {
				// 去除遮罩
				$.unblockUI();
				if (data.actualAcceptance) {
					$("#btnCustomerChecked").linkbutton('enable');
					$("#btnFinishAcceptance").linkbutton('enable');
					$('#btnDeleteAsset').linkbutton('enable');
					if (data.finishAcceptance) {
						$("#btnStorage").linkbutton('enable');
					} else {
						$("#btnStorage").linkbutton('disable');
					}
				} else {
					$("#btnCustomerChecked").linkbutton('disable');
					$("#btnFinishAcceptance").linkbutton('disable');
					$("#btnStorage").linkbutton('disable');
				}
			}
		});
	}
	/**
	 *對轉倉清單的刪除按鈕進行的操作
	*/
	function deleteButton() {
		var rows = $("#storageTb").datagrid("getSelections");
		if (rows !=''){
			//刪除按鈕變為enable狀態
			$("#btnDeleteAsset").linkbutton('enable');
		} else {
			$('#btnDeleteAsset').linkbutton('disable');
		}
	}
	/**
	 *	資產人owner節點的事件
	 */
	<%-- $("#<%=AssetInInfoDTO.ATTRIBUTE.ASSETS_OWNER.getValue()%>").combobox({
		onChange:function(){
			setMaTypeValue();
		}
	}) --%>
	/**
	 *	使用人節點的事件
	 */
	<%-- $("#<%=AssetInInfoDTO.ATTRIBUTE.USE_EMPLOYEE_NAME.getValue()%>").combobox({
		onChange:function(){
			setMaTypeValue();
		}
	}) --%>
	
	/**
	 *根據使用人和資產人owner的值決定維護模式的值
	 */
	<%-- function setMaTypeValue(){
		//清空維護模式的值
		$('#<%=AssetInInfoDTO.ATTRIBUTE.MA_TYPE_NAME.getValue()%>').textbox("setValue", "");
		document.getElementById('<%=AssetInInfoDTO.ATTRIBUTE.MA_TYPE.getValue()%>').value = "";
		//獲取資產人owner節點的值
		var ownerValue = $('#<%=AssetInInfoDTO.ATTRIBUTE.ASSETS_OWNER.getValue()%>').combobox("getValue");
		//獲取使用人節點的值
		var userValue = $('#<%=AssetInInfoDTO.ATTRIBUTE.USE_EMPLOYEE_NAME.getValue()%>').combobox("getValue");
		//若資產人owner和使用人都有值時，給維護模式賦值
		if(userValue != '' && ownerValue != ''){
			//若資產人owner == 使用人時，維護模式為買斷模式，資產人owner ！= 使用人時，維護模式為租賃模式
			if(userValue == ownerValue){
				$('#<%=AssetInInfoDTO.ATTRIBUTE.MA_TYPE_NAME.getValue()%>').textbox("setValue", "採購(買斷)");
				document.getElementById('<%=AssetInInfoDTO.ATTRIBUTE.MA_TYPE.getValue()%>').value = "<%=IAtomsConstants.MA_TYPE_BUYOUT%>";
			} else {
				$('#<%=AssetInInfoDTO.ATTRIBUTE.MA_TYPE_NAME.getValue()%>').textbox("setValue", "租賃");
				document.getElementById('<%=AssetInInfoDTO.ATTRIBUTE.MA_TYPE.getValue()%>').value = "<%=IAtomsConstants.MA_TYPE_LEASE%>";
			}
		}
	} --%>
	</script>
</body>
</html>