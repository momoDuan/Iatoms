<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractAttachedFileDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractManageFormDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/jsp/common/common.jsp"%>
<%
	SessionContext ctx = (SessionContext)request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	ContractManageFormDTO formDTO = null;
	BimContractDTO contractManageDTO = null;
	List<BimContractAttachedFileDTO> contractAttachedFileDTOs = null;
	String payMode = IAtomsConstants.PAY_MODE_YEAR;
	String contractStatus = IAtomsConstants.PARAM_CONTRACT_STATUS_IN_EFFECT;
	if(ctx != null) {
		formDTO = (ContractManageFormDTO) ctx.getResponseResult();
		if (formDTO != null ) {
	contractManageDTO = formDTO.getContractManageDTO();
		}  else {
	formDTO = new ContractManageFormDTO();
		}
	} 
	String lease = IAtomsConstants.CONTRACT_TYPE_LEASE;
	String buy = IAtomsConstants.CONTRACT_TYPE_BUY;
	//獲取useCaseNo
	String ucNo = formDTO.getUseCaseNo();
	//客户集合
	List<Parameter> customerList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_CUSTOMER_LIST);
	//厂商集合
	List<Parameter> manuFacturerList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_MANU_FACTURER_LIST);
	//廠商value集合
	List<String> manuFacturerValueList = (List<String>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_MANU_FACTURER_VALUE_LIST);
	//合約類型集合
	List<Parameter> contractTypeList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.CONTRACT_TYPE);
	//合約狀態集合
	List<Parameter> contractStatusList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.CONTRACT_STATUS);
	//付款方式集合
	List<Parameter> payModeList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PAY_MODE);
	//获取上传最大限制
	String uploadFileSize = formDTO.getUploadFileSize();
%>

<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="contractManageDTO" value="<%=contractManageDTO%>" scope="page"></c:set>
<c:set var="customerList" value="<%=customerList%>" scope="page"></c:set>
<c:set var="manuFacturerList" value="<%=manuFacturerList%>" scope="page"></c:set>
<c:set var="contractTypeList" value="<%=contractTypeList%>" scope="page"></c:set>
<c:set var="contractStatusList" value="<%=contractStatusList%>" scope="page"></c:set>
<c:set var="payModeList" value="<%=payModeList%>" scope="page"></c:set>
<c:set var="payMode" value="<%=payMode%>" scope="page"></c:set>
<c:set var="contractStatus" value="<%=contractStatus%>" scope="page"></c:set>
<c:set var="manuFacturerValueList" value="<%=manuFacturerValueList%>" scope="page"></c:set>
<c:set var="lease" value="<%=lease%>" scope="page"></c:set> 
<c:set var="buy" value="<%=buy%>" scope="page"></c:set> 
<c:set var="uploadFileSize" value="<%=uploadFileSize%>" scope="page"></c:set> 
<head>
	<script type="text/javascript" src="${contextPath}/js/fineuploader-5.0.2.js"></script>
	<link rel="stylesheet" href="${contextPath}/css/fineuploader-5.0.2.css"/>
</head>
<body >
	<div data-options="region:'center',fit:true" style="width: auto; height: auto; padding: 10px 20px; overflow-y: hidden" class="topSoller">
            <div class="dialogtitle">合約維護</div>
            <div style="color: red">
				<span id="msg1" class="red"></span>
			</div>
            <form id="fm" method="post" class="easyui-form">
            	<table cellpadding="4" width="100%">
                    <tr>
                        <td width="17%">客戶:<span class="red">*</span></td>
                        <td>
							<c:if test="${!(formDTO.actionId eq 'initEdit') }">
								<cafe:droplisttag
									id="<%=BimContractDTO.ATTRIBUTE.COMPANY_ID.getValue() %>"
									name="<%=BimContractDTO.ATTRIBUTE.COMPANY_ID.getValue() %>" 
									css="easyui-combobox"
									selectedValue="${contractManageDTO.companyId}"
									result="${customerList}"
									hasBlankValue="true"
									blankName="請選擇"
									style="width: 150px"
									javascript="validType='requiredDropList' editable=false required=true  invalidMessage=\"請輸入客戶\""
								></cafe:droplisttag>
								
							</c:if>
							<c:if test="${formDTO.actionId eq 'initEdit' }">
							
								<cafe:droplisttag
									id="<%=BimContractDTO.ATTRIBUTE.COMPANY_ID.getValue() %>"
									name="<%=BimContractDTO.ATTRIBUTE.COMPANY_ID.getValue() %>" 
									css="easyui-combobox"
									selectedValue="${contractManageDTO.companyId}"
									result="${customerList}"
									hasBlankValue="true"
									disabled="true"
									blankName="請選擇"
									style="width: 150px"
									javascript="validType='requiredDropList' editable=false required=true"
								></cafe:droplisttag>
								
							</c:if>
							
						</td>
						<td>維護廠商:</td>
						<td>
                            <cafe:droplistchecktag
	                            id="<%=BimContractDTO.ATTRIBUTE.COMPANY_IDS.getValue()%>"
	                            name="<%=BimContractDTO.ATTRIBUTE.COMPANY_IDS.getValue()%>" 
	                            css="easyui-combobox"
	                            result="${manuFacturerList}"
	                            selectedValues="${empty contractManageDTO.contractId ? manuFacturerValueList:contractManageDTO.vendors}"
	                            hasBlankValue="true"
	                            blankName="請選擇(複選)"
	                            style="width: 150px"
                           		javascript="multiple=true editable=false multiple=true required=true invalidMessage=\"請輸入廠商\""
                            ></cafe:droplistchecktag>
                   		</td>
                    </tr>
                    <tr>
                        <td>統一編號:</td>
                        <td>
							<input name="<%=BimContractDTO.ATTRIBUTE.UNITY_NUMBER.getValue()%>" id="<%=BimContractDTO.ATTRIBUTE.UNITY_NUMBER.getValue()%>" class="easyui-textbox" type="text" disabled="disabled" data-options="required:true,validType:'length[1,50]'" value="<c:out value='${contractManageDTO.unityNumber }'/>" />
							<input name="<%=BimContractDTO.ATTRIBUTE.UNITY_NUMBER.getValue()%>" id="<%=BimContractDTO.ATTRIBUTE.UNITY_NUMBER.getValue()%>" type="hidden" value="<c:out value='${contractManageDTO.unityNumber }'/>" />
						</td>
						<td>合約編號:<span class="red">*</span></td>
						<td>
							<input id="<%=BimContractDTO.ATTRIBUTE.CONTRACT_CODE.getValue().toString() %>" name="<%=BimContractDTO.ATTRIBUTE.CONTRACT_CODE.getValue() %>" 
								class="easyui-textbox" type="text" maxlength="50"
                            	data-options="required:true,missingMessage:'請輸入合約編號',
                            	validType:['maxLength[50]']"
                                value="<c:out value='${contractManageDTO.contractCode }'/>" />
                            	<input name="hiddenContractId" type="hidden"  value="<c:out value='${contractManageDTO.contractId }'/>"/>
                       	</td>
                    </tr>
                    <tr>
                        <td>合約類型:</td>
                        <td colspan="3">
                            <cafe:droplistchecktag
                            	css="easyui-combobox"
                            	id="<%=BimContractDTO.ATTRIBUTE.CONTRACT_TYPE_ID.getValue() %>"
	                            name="<%=BimContractDTO.ATTRIBUTE.CONTRACT_TYPE_ID.getValue() %>" 
								result="${contractTypeList }"
								selectedValues="${contractManageDTO.contractTypes}"
								javascript=" editable=false multiple=true panelheight=\"auto\""
								blankName="請選擇(複選)"
								hasBlankValue="true"
								style="width:150px">
                           	</cafe:droplistchecktag>（租賃，採購(買斷)，維護）
                            
                   		</td>
                    </tr>
                    <tr>
                        <td>合約起迄:</td>
                        <td>
                            <input id="<%=BimContractDTO.ATTRIBUTE.START_DATE.getValue() %>" name="<%=BimContractDTO.ATTRIBUTE.START_DATE.getValue() %>" class="easyui-datebox"  type="text" style="width: 99px" maxlength="10"  data-options="
                            	validType:[
                            		'maxLength[10]',
                            		'date[\'合約起迄格式限YYYY/MM/DD\']'],
                            	onChange:function(newValue,oldValue) {
                            		changeRequired(newValue, $('#<%=BimContractDTO.ATTRIBUTE.END_DATE.getValue() %>'), '請輸入合約迄');
                            			$('#<%=BimContractDTO.ATTRIBUTE.END_DATE.getValue() %>').timespinner('isValid');
                             	}" 
                            	value="${contractManageDTO.startDate }"/>～
                            <input id="<%=BimContractDTO.ATTRIBUTE.END_DATE.getValue() %>" name="<%=BimContractDTO.ATTRIBUTE.END_DATE.getValue() %>" maxlength="10" class="easyui-datebox" type="text" style="width: 99px"  data-options="
                            	validType:['maxLength[10]','date[\'合約起迄格式限YYYY/MM/DD\']','compareDateStartEnd[\'#<%=BimContractDTO.ATTRIBUTE.START_DATE.getValue() %>\',\'合約起不可大於合約迄\']'],
                            	onChange:function(newValue,oldValue) {
                            		changeRequired(newValue, $('#<%=BimContractDTO.ATTRIBUTE.START_DATE.getValue() %>'), '請輸入合約起');
                            			$('#<%=BimContractDTO.ATTRIBUTE.END_DATE.getValue() %>').timespinner('isValid');
                             	}" value="${contractManageDTO.endDate }" />
                        </td>
                        <td>解約日:</td>
                        <td >
                            <input id="<%=BimContractDTO.ATTRIBUTE.CANCEL_DATE.getValue() %>" name="<%=BimContractDTO.ATTRIBUTE.CANCEL_DATE.getValue() %>" class="easyui-datebox" type="text" maxlength="10" style="width: 150px" data-options="validType:['maxLength[10]','date[\'解約日格式限YYYY/MM/DD\']']" value="${contractManageDTO.cancelDate }" />
                        </td>
                    </tr>
                    <tr>
                       <td>合約狀態:</td>
						<td>
                            <cafe:droplisttag
	                            id="<%=BimContractDTO.ATTRIBUTE.CONTRACT_STATUS.getValue() %>"
	                            name="<%=BimContractDTO.ATTRIBUTE.CONTRACT_STATUS.getValue() %>"
	                            css="easyui-combobox"
	                            selectedValue="${empty contractManageDTO.contractId ? contractStatus : contractManageDTO.contractStatus}"
	                            result="${contractStatusList}"
	                            hasBlankValue="true"
	                            blankName="請選擇"
	                            style="width: 150px"
	                            javascript=" editable=false panelheight=\"auto\""
                            ></cafe:droplisttag>
                   		</td>
                   		<td>合約金額:</td>
                        <td>
                            <input id="<%=BimContractDTO.ATTRIBUTE.CONTRACT_PRICE.getValue() %>" name="<%=BimContractDTO.ATTRIBUTE.CONTRACT_PRICE.getValue() %>" class="easyui-textbox" maxLength="10" type="text" data-options="validType:['number[\'合約金額限輸入正整數，請重新輸入\']','maxLength[10]']" value="${contractManageDTO.contractPrice }""/></td>
                    </tr>
                    
                    <tr>
                       <td>付款方式:</td>
						<td>
							<cafe:droplisttag 
	                            id="<%=BimContractDTO.ATTRIBUTE.PAY_MODE.getValue() %>"
	                            name="<%=BimContractDTO.ATTRIBUTE.PAY_MODE.getValue() %>" 
	                            css="easyui-combobox"
	                            selectedValue="${empty contractManageDTO.contractId ? payMode : contractManageDTO.payMode}"
	                            result="${payModeList}"
	                            hasBlankValue="true"
	                            blankName="請選擇"
	                            style="width: 150px"
	                            javascript="editable=false required=true panelheight=\"auto\""
                            ></cafe:droplisttag>
                   		</td>
                   		<td>付款條件:</td>
                        <td>
                            <input id="<%=BimContractDTO.ATTRIBUTE.PAY_REQUIRE.getValue() %>" maxLength="100" name="<%=BimContractDTO.ATTRIBUTE.PAY_REQUIRE.getValue() %>" data-options="validType:'maxLength[100]'" class="easyui-textbox" type="text" value="<c:out value='${contractManageDTO.payRequire }'/>" ></input></td>
                    </tr>
                    <tr>
                        <td><label id="factoryWarrantyLabel">原廠保固期限:</label></td>
                        <td>
							<input id="<%=BimContractDTO.ATTRIBUTE.FACTORY_WARRANTY.getValue() %>" name="<%=BimContractDTO.ATTRIBUTE.FACTORY_WARRANTY.getValue() %>" class="easyui-textbox" type="text" maxlength="3" style="width: 150px" data-options="validType:['number[\'原廠保固期限輸入正整數，請重新輸入\']']" value="${contractManageDTO.factoryWarranty }" />(月)
						<td><label id="customerWarrantyLabel">客戶保固期限:</label></td>
						<td>
							<input id="<%=BimContractDTO.ATTRIBUTE.CUSTOMER_WARRANTY.getValue() %>" name="<%=BimContractDTO.ATTRIBUTE.CUSTOMER_WARRANTY.getValue() %>" class="easyui-textbox" type="text" maxlength="3" style="width: 150px" data-options="validType:['number[\'客戶保固期限輸入正整數，請重新輸入\']']" value="${contractManageDTO.customerWarranty }" />(月)
                    </tr>
                    <tr>
                        <td>約定上班時間1:</td>
                        <td>
                        	<input id="hidden_<%=BimContractDTO.ATTRIBUTE.WORK_HOUR_START_1.getValue() %>"
                         				type="hidden" value="<c:out value='${contractManageDTO.workHourStart1 }'/>">
                            <input id="<%=BimContractDTO.ATTRIBUTE.WORK_HOUR_START_1.getValue() %>" maxlength="5" name="<%=BimContractDTO.ATTRIBUTE.WORK_HOUR_START_1.getValue() %>" value="${contractManageDTO.workHourStart1 }" class="easyui-timespinner" style="width: 100px" 
                           		data-options="
                            		validType:['maxLength[5]',
                            		'compareTime[\'上班時間1格式限HH:mm，0.5小時為單位\']'],
                             		increment:30,
                             		onChange:function(newValue,oldValue) {
                             			<%-- changeRequiredTimespinner(newValue, $('#<%=BimContractDTO.ATTRIBUTE.WORK_HOUR_END_1.getValue() %>'), '請輸入上班時間1迄'); --%>
                             			$('#<%=BimContractDTO.ATTRIBUTE.WORK_HOUR_END_1.getValue() %>').timespinner('isValid');
                             		},
                             		onSpinUp:function(){
                              			spinTimes(this,true);
                            		 },
                             		onSpinDown:function(){
                              			spinTimes(this,false);
                             		}
                            ">
                            </input>~
                            <input id="hidden_<%=BimContractDTO.ATTRIBUTE.WORK_HOUR_END_1.getValue() %>"
                         				type="hidden" value="<c:out value='${contractManageDTO.workHourEnd1 }'/>">
                            <input id="<%=BimContractDTO.ATTRIBUTE.WORK_HOUR_END_1.getValue() %>" maxlength="5" name="<%=BimContractDTO.ATTRIBUTE.WORK_HOUR_END_1.getValue() %>" value="${contractManageDTO.workHourEnd1 }" class="easyui-timespinner" style="width: 100px" 
                            	data-options="
                            		validType:['maxLength[5]',
                            		'compareTimeSize[\'#<%=BimContractDTO.ATTRIBUTE.WORK_HOUR_START_1.getValue() %>\',\'上班時間1起需小於上班時間1迄\']',
                            		'length[1,5]',
                            		'compareTime[\'上班時間1格式限HH:mm，0.5小時為單位\']'],
                            		increment:30,
                            		onChange:function(newValue,oldValue) {
                            			<%-- changeRequiredTimespinner(newValue, $('#<%=BimContractDTO.ATTRIBUTE.WORK_HOUR_START_1.getValue() %>'), '請輸入上班時間1起'); --%>
                            			$('#<%=BimContractDTO.ATTRIBUTE.WORK_HOUR_START_2.getValue() %>').timespinner('isValid');
                             		},
                             		onSpinUp:function(){
                              			spinTimes(this,true);
                            		 },
                             		onSpinDown:function(){
                              			spinTimes(this,false);
                             		}			
                            "></input></td>
                        <td>約定上班時間2:</td>
                        <td colspan="3">
                        	 <input id="hidden_<%=BimContractDTO.ATTRIBUTE.WORK_HOUR_START_2.getValue() %>" 
                         				type="hidden" value="<c:out value='${contractManageDTO.workHourStart2 }'/>">
                            <input id="<%=BimContractDTO.ATTRIBUTE.WORK_HOUR_START_2.getValue() %>" maxlength="5" name="<%=BimContractDTO.ATTRIBUTE.WORK_HOUR_START_2.getValue() %>" value="${contractManageDTO.workHourStart2 }" class="easyui-timespinner" style="width: 100px" 
                            	data-options="
                            		validType:['maxLength[5]',
                            		'compareTimeSize[\'#<%=BimContractDTO.ATTRIBUTE.WORK_HOUR_END_1.getValue() %>\',\'上班時間1需小於上班時間2\', \'#<%=BimContractDTO.ATTRIBUTE.WORK_HOUR_END_1.getValue() %>\']',
                            		'length[1,5]',
                            		'compareTime[\'上班時間2格式限HH:mm，0.5小時為單位\']'],
                            		increment:30,
                            		onChange:function(newValue,oldValue) {
                            			<%-- changeRequiredTimespinner(newValue, $('#<%=BimContractDTO.ATTRIBUTE.WORK_HOUR_END_2.getValue() %>'), '請輸入上班時間2迄'); --%>
                            			$('#<%=BimContractDTO.ATTRIBUTE.WORK_HOUR_END_2.getValue() %>').timespinner('isValid');
                             		},
                             		onSpinUp:function(){
                              			spinTimes(this,true);
                            		 },
                             		onSpinDown:function(){
                              			spinTimes(this,false);
                             		}
                             	
                            ">
                            </input>~
                             <input id="hidden_<%=BimContractDTO.ATTRIBUTE.WORK_HOUR_END_2.getValue() %>" 
                         				type="hidden" value="${contractManageDTO.workHourEnd2 }">
                            <input id="<%=BimContractDTO.ATTRIBUTE.WORK_HOUR_END_2.getValue() %>" maxlength="5" name="<%=BimContractDTO.ATTRIBUTE.WORK_HOUR_END_2.getValue() %>" value="${contractManageDTO.workHourEnd2 }" class="easyui-timespinner" style="width: 100px" 
                            	data-options="
                            		validType:['maxLength[5]',
                            		'compareTimeSize[\'#<%=BimContractDTO.ATTRIBUTE.WORK_HOUR_START_2.getValue() %>\',\'上班時間2起需小於上班時間2迄\']',
                            		'length[1,5]',
                            		'compareTime[\'上班時間2格式限HH:mm，0.5小時為單位\']'],
                            		increment:30,
                            		<%-- onChange:function(newValue,oldValue) {
                            			changeRequiredTimespinner(newValue, $('#<%=BimContractDTO.ATTRIBUTE.WORK_HOUR_START_2.getValue() %>'), '請輸入上班時間2起');
                             		}, --%>
                             		onSpinUp:function(){
                              			spinTimes(this,true);
                            		 },
                             		onSpinDown:function(){
                              			spinTimes(this,false);
                             		}
                            		"></input></td>
                    </tr>
                    <tr>
                       <td>窗口1:</td>
						<td>
							<input id="<%=BimContractDTO.ATTRIBUTE.WINDOW_ONE.getValue() %>" name="<%=BimContractDTO.ATTRIBUTE.WINDOW_ONE.getValue() %>" maxLength="50" class="easyui-textbox" type="text" data-options="validType:'maxLength[50]'" value="<c:out value='${contractManageDTO.window1 }'/>" ></input></td>
                   		<td>窗口1聯繫方式:</td>
                        <td>
                            <input id="<%=BimContractDTO.ATTRIBUTE.WINDOW_ONE_CONN.getValue() %>" name="<%=BimContractDTO.ATTRIBUTE.WINDOW_ONE_CONN.getValue() %>" maxLength="20" class="easyui-textbox" type="text" value="<c:out value='${contractManageDTO.window1Connection }'/>" data-options="validType:['maxLength[20]']" ></input></td>
                    </tr>
                    <tr id="iddd">
                       <td>窗口2:</td>
						<td>
							<input id="<%=BimContractDTO.ATTRIBUTE.WINDOW_TWO.getValue() %>" name="<%=BimContractDTO.ATTRIBUTE.WINDOW_TWO.getValue() %>" maxLength="50" class="easyui-textbox" type="text" value="<c:out value='${contractManageDTO.window2}'/>" data-options="validType:'maxLength[50]'" ></input></td>
                   		<td>窗口2聯繫方式:</td>
                        <td>
                            <input id="<%=BimContractDTO.ATTRIBUTE.WINDOW_TWO_CONN.getValue() %>" name="<%=BimContractDTO.ATTRIBUTE.WINDOW_TWO_CONN.getValue() %>" maxLength="20" class="easyui-textbox" type="text" value="<c:out value='${contractManageDTO.window2Connection }'/>" data-options="validType:['maxLength[20]']" ></input></td>
                    </tr>
                    <tr>
                        <td>說明:</td>
                        <td colspan="3">
                           <textarea rows="4" cols="30"  
                           		id="<%=BimContractDTO.ATTRIBUTE.COMMENT.getValue() %>" 
                           		name="<%=BimContractDTO.ATTRIBUTE.COMMENT.getValue() %>" 
                           		maxLength="200" 
                           		data-options="multiline:true, validType:'maxLength[200]'" 
                           		class="easyui-textbox" 
                           		style="height: 88px;width:400px" 
                           		><c:out value='${contractManageDTO.comment }'/></textarea></td>
                    </tr>
                    <tr>
					<td>附加檔案:</td>
						<td colspan="3">
							<div id="file">
								<cafe:fileuploadTag
									id="file_upload"
									name="file_upload"
									allowedExtensions="all"
									showFileList="true"
									uploadUrl="${contextPath}/contract.do?actionId=saveFile&fileName=${formDTO.fileName}"
									sizeLimit = "${uploadFileSize }"
									messageId = "msg2"
									showName="上傳"
									isCustomError = "true"
									autoUpload="false"
									messageAlert="false"
									multiple="true"
									deleletParams="{actionId:'deleteTempFile',fileName:'${formDTO.fileName}'}"
									whetherDelete="true"
									whetherDownLoad="false"
									deleteUrl="${contextPath}/contract.do"
									javaScript="onAllComplete:uploadAllComplete, onComplete:showMessageContract"
									> 
	                           	</cafe:fileuploadTag>
	                        	<c:if test="${!empty formDTO.contractAttachedFileDTOs}">
								<div class="qq-upload-list-selector qq-upload-list">
									<div class="qq-upload-success"
										style="border-bottom-color: gray; border-bottom-width: 1px; border-bottom-style: dotted;">
											<c:forEach items="${formDTO.contractAttachedFileDTOs}" var="attachedFile">
												<div>
												<a class="qq-upload-file ucFileUploaderLinkButton" onclick = "downloadFile('${attachedFile.attachedFileId}');"
													href="#">${attachedFile.fileName}</a> 
												<a class="qq-upload-file ucFileUploaderLinkButton" onclick = "deleteFile(this,'${attachedFile.attachedFileId}');"
													href="#">刪除</a> <br/>
												</div>
											</c:forEach> 
									</div>
								</div>
							</c:if>
							<input type="hidden" id = "deleteFileId" name = "deleteFileId"/>
							</div>
						</td>
                    </tr>
                    <tr>
                    	<td colspan="3" style="color: red">
                    		<span id="msg2" class="red"></span>
                    	</td>
                    </tr>
                    <tr>
                    	<td>
                    		<input type="hidden" name="fileName" id="fileName" value = "${formDTO.fileName}">
                    	</td>
                    </tr>
                    <tr>
                    	<td colspan="4"> 
                    		 <div style="color: red">
								<span id="assetMsg" ></span>
							</div>
                    		<table id="assetTable" class="easyui-datagrid" title="" style="width:520px;height:auto"
									data-options="
										iconCls: 'icon-edit',
										singleSelect: true,
										toolbar: '#tb3',
										url: '${contextPath}/contract.do?actionId=getContractAssetList',
										method: 'get',
										onDblClickRow: onDblClickRow,
										onClickRow:onClickRow,
										striped:true,
										onEndEdit: onEndEdit,
										nowrap:false,
										queryParams :{
											contractId:'${empty contractManageDTO.contractId? null : contractManageDTO.contractId}'
										}
									">
								<thead>
									<tr>
										<th data-options="field:'assetCategory',width:120,halign:'center',
												formatter:function(value,row){
													return row.assetCategoryName;
												},
												editor:{
													type:'combobox',
													options:{
														valueField:'value',
														textField:'name',
														method:'get',
														data:assetCategoryList,
														editable:false,
														onChange:getAssetTypeList
													}
													
												}">設備類別</th>
										<th data-options="field:'assetTypeId',width:120,halign:'center',
												formatter:function(value,row){
													return row.assetTypeName;
												},
												editor:{
													type:'combobox',
													options:{
														valueField:'value',
														textField:'name',
														method:'get',
														required:true,
														editable:false,
														validType:'ignore[\'請選擇\']',
														missingMessage:'請輸入設備名稱',
														invalidMessage:'請輸入設備名稱'
													}
												}">設備名稱</th>
										<th data-options="field:'amount',width:90,halign:'center',align:'right',
											editor:{type:'textbox',
													options:{
														validType:['number[\'數量限輸入正整數，請重新輸入\']'],
														onChange:amountChange }
													}">數量</th>
										<th data-options="field:'safetyStock',width:90,halign:'center',align:'right',
											editor:{type:'textbox',
												options:{
													validType:['number[\'安全庫存限輸入正整數，請重新輸入\']',
													'comparativeSize[\'#assetTable\',editIndex,\'amount\',\'數量應大於安全庫存，請重新輸入\']']
													}
													}">安全庫存</th>
										<th data-options="field:'price',width:90,halign:'center',align:'right',
											editor:{type:'textbox',
												options:{
													validType:['number[\'售價限輸入正整數，請重新輸入\']'], 
													}
												}">售價</th>
									</tr>
								</thead>
							</table>
							<div id="tb3" style="height:auto">
								<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="appendRow();">增加</a>
								<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" id="deletedAsset" onclick="removeit();" disabled = "true">刪除</a>

							</div>
                    	</td>
                    </tr>
                </table>
                <input type="hidden" id="assetListRow" name="assetListRow"/>
            </form>
        </div>
	<script type="text/javascript">		
	var isDblClickRow = false;
	var editIndex = undefined;
	var selectValues;
	$(function () {
		$("#companyIds").combobox({
			onSelect : function(newValue) {
				selectMultiple(newValue,"companyIds");
			},
			onUnselect : function(newValue) {
				unSelectMultiple(newValue,"companyIds");
			},
		});
		$("#<%=BimContractDTO.ATTRIBUTE.CONTRACT_TYPE_ID.getValue()%>").combobox({
			onSelect : function(newValue) {
				selectMultiple(newValue,"<%=BimContractDTO.ATTRIBUTE.CONTRACT_TYPE_ID.getValue()%>");
			},
			onUnselect : function(newValue) {
				unSelectMultiple(newValue,"<%=BimContractDTO.ATTRIBUTE.CONTRACT_TYPE_ID.getValue()%>");
			},
			onChange : function(newValue,name) {
				contractTypeChange(newValue);
			},
		});
	});
	$(function(){
		// 客戶下拉框修改時同步改變統一編號
		$('#<%=BimContractDTO.ATTRIBUTE.COMPANY_ID.getValue()%>').combobox({
			onChange: function(newValue){
				if (newValue != "") {
					ajaxService.getUnityNameByCompanyId(newValue, function(data){
						if(data){
							$('#<%=BimContractDTO.ATTRIBUTE.UNITY_NUMBER.getValue()%>').textbox('setValue',data);
						} else {
							$('#<%=BimContractDTO.ATTRIBUTE.UNITY_NUMBER.getValue()%>').textbox('setValue',"");
						}
					});
				} else {
					$('#<%=BimContractDTO.ATTRIBUTE.UNITY_NUMBER.getValue()%>').textbox('setValue',"");
				}
			}
		});

	});
	/**
	* 合約類型異動后，判斷原廠保固期限、客戶保固期限是否必填
	*/
	function contractTypeChange(newValue){
		var contractType = newValue + "";
		var isType = false;
		var contractTypeId = new Array();
		contractTypeId = (contractType).split(",");
		for (var i=0; i<contractTypeId.length; i++) {
			if (contractTypeId[i] == '${lease}' || contractTypeId[i] == '${buy}') {
				
				$('#<%=BimContractDTO.ATTRIBUTE.FACTORY_WARRANTY.getValue()%>').textbox({
					required:true,missingMessage:'請輸入原廠保固期限',
				});
				$('#<%=BimContractDTO.ATTRIBUTE.CUSTOMER_WARRANTY.getValue()%>').textbox({
					required:true,missingMessage:'請輸入客戶保固期限',
				});
				$("#factoryWarrantyLabel").html("原廠保固期限:<span class=\"red\">*</span>");
				$("#customerWarrantyLabel").html("客戶保固期限:<span class=\"red\">*</span>");
				isType = true;
				break;
			} 
		}
		if (!isType) {
			$('#<%=BimContractDTO.ATTRIBUTE.FACTORY_WARRANTY.getValue()%>').textbox({
				required:false,
			});
			$('#<%=BimContractDTO.ATTRIBUTE.CUSTOMER_WARRANTY.getValue()%>').textbox({
				required:false,
			});
			document.getElementById("factoryWarrantyLabel").innerHTML="原廠保固期限:";
			document.getElementById("customerWarrantyLabel").innerHTML="客戶保固期限:";
		}
		textBoxDefaultSetting($('#<%=BimContractDTO.ATTRIBUTE.FACTORY_WARRANTY.getValue()%>'));
		textBoxDefaultSetting($('#<%=BimContractDTO.ATTRIBUTE.CUSTOMER_WARRANTY.getValue()%>'));
	}
	/**
	* 文件下載
	*/
	function downloadFile(attachedFileId) {
		ajaxService.checkDownLoadFile(attachedFileId, function(data){
			if (data) {
				createSubmitForm("${contextPath}/contract.do","actionId=<%=IAtomsConstants.ACTION_DOWNLOAD%>&attachedFileId=" + attachedFileId,"post");
			} else {
				$("#msg2").text('文件不存在');
			}
		});
	}
	/**
	* 刪除文件
	*/
	function deleteFile(obj,fileId) {
		var deleteFile = $(obj).parent();
		deleteFile.hide();
		if ($("#deleteFileId").val() == "") {
			$("#deleteFileId").val(fileId);
		} else {
			$("#deleteFileId").val($("#deleteFileId").val() + "," + fileId);
		}
	}
	/**
	* 設備類別下拉框聯動設備名稱，並去除已經選擇的設備名稱
	*/
	function getAssetTypeList(newValue, oldValue) {
		var row = $('#assetTable').datagrid('getSelected');
		var rowIndex = $('#assetTable').datagrid('getRowIndex',row);
		/* if (newValue != "") { */
			ajaxService.getAssetTypeList(newValue, function(result) {
				if (result != null) {
					selectValues = result;
					//查找所有的row
					var rows = $('#assetTable').datagrid('getRows');
					for (var i = 0; i < rows.length; i++) {
						if (i == rowIndex) {
							//當前行不比對
							continue;
						}
						/* if (rows[i].assetCategory == newValue) {
							//和選擇的行值一樣, 需過濾掉當前值                        		
							for (var j = 0; j < result.length; j++) {
								if (result[j].value == rows[i].assetTypeId) {
									result.splice(j,1);
									break;
								}
							} 
						} */
						//和選擇的行值一樣, 需過濾掉當前值                        		
						for (var j = 0; j < result.length; j++) {
							if (result[j].value == rows[i].assetTypeId) {
								result.splice(j,1);
								break;
							}
						} 
					}
					var currentEditor = $('#assetTable').datagrid('getEditor', {  
							index : rowIndex,  
							field : 'assetTypeId'
					});
					/* if ((oldValue != "" || ${empty contractManageDTO.contractId }) && !isDblClickRow) {
						currentEditor.target.combobox('setValue', "");
					} else {
						isDblClickRow = false;
					} */
					if (!isDblClickRow) {
						currentEditor.target.combobox('setValue', "");
					} else {
						isDblClickRow = false;
					}
					currentEditor.target.combobox('loadData', initSelect(result));
				} else {
					var currentEditor = $('#assetTable').datagrid('getEditor', {  
						index : rowIndex,  
						field : 'assetTypeId'
					});
					currentEditor.target.combobox('setValue', "");
					currentEditor.target.combobox('loadData', initSelect(null));
				}
			});
	}
	/**
	* 結束datagrid表格數據
	*/
	function endEditing() {
		$("#assetMsg").text("");
		$("div").unbind("scroll.validate");
		if (editIndex == undefined) {
			return true;
		}
		if(!validateFormInContract()){
			return false;
		}
		if ($('#assetTable').datagrid('validateRow', editIndex)) {
			$('#assetTable').datagrid('endEdit', editIndex);
			var row = $('#assetTable').datagrid('getSelected');
			var rowIndex = $('#assetTable').datagrid('getRowIndex',row);
			var rows = $('#assetTable').datagrid('getRows');
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}
	/**
	* 雙擊datagrid表格觸發的事件
	*/
	function onDblClickRow(index) {
		if (editIndex != index) {
			if (endEditing()) {
				var rows = $("#assetTable").datagrid("getRows");
				if (rows) {
					if (rows[index].assetCategory != "") {
						isDblClickRow = true;
					}
				}
				$('#assetTable').datagrid('selectRow', index).datagrid(
						'beginEdit', index);
				var ed = $('#assetTable').datagrid('getEditor', {
					index : index,
					field : 'assetCategory'
						});
				var assetCategoryValue = $(ed.target).combobox('getValue');
				if (assetCategoryValue == "") {
					ajaxService.getAssetTypeList(assetCategoryValue, function(result) {
						if (result != null) {
							selectValues = result;
							//查找所有的row
							var rows = $('#assetTable').datagrid('getRows');
							for (var i = 0; i < rows.length-1; i++) {
								if (i == editIndex) {
									//當前行不比對
									continue;
								}
								//和選擇的行值一樣, 需過濾掉當前值                        		
								for (var j = 0; j < result.length; j++) {
									if (result[j].value == rows[i].assetTypeId) {
										result.splice(j,1);
										break;
									}
								} 
							}
							var currentEditor = $('#assetTable').datagrid('getEditor', {  
									index : editIndex,  
									field : 'assetTypeId'
							});
							var selectValue = $(ed.target).combobox('getValue');
							var isExist = checkExistValue(selectValues, selectValue);
							if (!isExist) {
								$(ed.target).combobox('setValue', '');
							}
							currentEditor.target.combobox('loadData', initSelect(result));
						}
					});
				} else {
					ed = $('#assetTable').datagrid('getEditor', {
						index : index,
						field : 'assetTypeId'
							});
					var selectValue = $(ed.target).combobox('getValue');
					var isExist = checkExistValue(selectValues, selectValue);
					if (!isExist) {
						$(ed.target).combobox('setText', '');
					}
				}
				editIndex = index;
				checkTextBox(editIndex);
				$("#deletedAsset").linkbutton('enable');
				
			} else {
				setTimeout(function() {
					$('#assetTable').datagrid('selectRow', editIndex);
				}, 0);
			}
		}
	}
	/**
	* 點擊datagrid表格觸發的事件
	*/
	function onClickRow(index, field){
		if (editIndex == undefined) {
				$('#assetTable').datagrid('selectRow', index);
		} else {
			$('#assetTable').datagrid('selectRow', editIndex);
		}
		$("#deletedAsset").linkbutton('enable');
	}
	/**
	* 當結束編輯時
	*/
	function onEndEdit(index, row) {
		$("div.topSoller").unbind("scroll.validate");
		var ed = $(this).datagrid('getEditor', {
			index : index,
			field : 'assetTypeId'
		});
		row.assetTypeName = $(ed.target).combobox('getText');
		ed = $(this).datagrid('getEditor', {
			index : index,
			field : 'assetCategory'
		});
		if ($(ed.target).combobox('getText') != "請選擇") {
			row.assetCategoryName = $(ed.target).combobox('getText');
		} else {
			row.assetCategoryName = null;
		}
		/* ed = $(this).datagrid('getEditor', {
			index : index,
			field : 'price'
		});
		if ($(ed.target).textbox('getValue') == ""){
			row.price = null;
		} */
		checkValueIsNull(row, index, "price");
		checkValueIsNull(row, index, "safetyStock");
		checkValueIsNull(row, index, "amount");
	}
	/**
	* 核檢數量、安全庫存、售價是否為空
	*/
	function checkValueIsNull(row, index, field) {
		var ed = $("#assetTable").datagrid('getEditor', {
			index : index,
			field : field
		});
		if ($(ed.target).textbox('getValue') == ""){
			row[field] = null;
		}
	}
	
	
	/**
	* datagrid表格增加一行
	*/
	function appendRow() {	
		if (endEditing()) {
			$('#assetTable').datagrid('appendRow', {assetTypeId:'請選擇',assetCategory:''});
			editIndex = $('#assetTable').datagrid('getRows').length - 1;
			$('#assetTable').datagrid('selectRow', editIndex).datagrid(
					'beginEdit', editIndex);
			checkTextBox(editIndex);
			ajaxService.getAssetTypeList("", function(result) {
				if (result != null) {
					selectValues = result;
					//查找所有的row
					var rows = $('#assetTable').datagrid('getRows');
					for (var i = 0; i < rows.length-1; i++) {
						if (i == editIndex) {
							//當前行不比對
							continue;
						}
						//和選擇的行值一樣, 需過濾掉當前值                        		
						for (var j = 0; j < result.length; j++) {
							if (result[j].value == rows[i].assetTypeId) {
								result.splice(j,1);
								break;
							}
						} 
					}
					var currentEditor = $('#assetTable').datagrid('getEditor', {  
							index : editIndex,  
							field : 'assetTypeId'
					});
					currentEditor.target.combobox('setValue', "");
					currentEditor.target.combobox('loadData', initSelect(result));
				}
			});
			$("#deletedAsset").linkbutton('enable');
			//新增時，始終跳到最后行
			$('#dlg').scrollTop( $('#dlg')[0].scrollHeight );
		}
	}
	/**
	* 刪除datagrid表格一行數據
	*/
	function removeit() {
		$("div.topSoller").unbind("scroll.validate");
		$("#assetMsg").text("");
		if (editIndex == undefined) {
			// 沒有正在編輯的row，獲取選中row
			var row = $('#assetTable').datagrid('getSelected');
			if (row){
				var rowIndex = $('#assetTable').datagrid('getRowIndex',row);
				$('#assetTable').datagrid('deleteRow',rowIndex);
				$("#deletedAsset").linkbutton('disable');
			}
			return;
		}
		$('#assetTable').datagrid('cancelEdit', editIndex).datagrid('deleteRow',
				editIndex);
		editIndex = undefined;
		$("#deletedAsset").linkbutton('disable');
	}
	/**
	* 檢驗textBox框長度以及取消空格
	*/
	function checkTextBox(index) {
		var amount = $('#assetTable').datagrid('getEditor', { 'index': index, field: 'amount' });
		var safetyStock = $('#assetTable').datagrid('getEditor', { 'index': index, field: 'safetyStock' });
		var price = $('#assetTable').datagrid('getEditor', { 'index': index, field: 'price' });
		$(amount.target).textbox('textbox').attr('maxlength',10);
		$(safetyStock.target).numberbox('textbox').attr('maxlength',10);
		$(price.target).numberbox('textbox').attr('maxlength',10);
	}
	/**
	* 數量修改事件，觸發安全庫存的驗證。
	*/
	function amountChange() {
		var row = $('#assetTable').datagrid('getSelected');
			if (row){
			var rowIndex = $('#assetTable').datagrid('getRowIndex',row);
			var currentEditor = $('#assetTable').datagrid('getEditor', {  
				index : rowIndex,  
				field : 'safetyStock'
			});
			$(currentEditor.target).textbox('isValid');
		} 
	}
	/**
	* 異動合約起止時，將另一個也變也為必填或者非必填。
	*/
	function changeRequired(newValue, obj,msg){
		if (newValue != '') {
			obj.textbox({
				required:true,missingMessage:msg,
			});
		} else {
			obj.textbox({
				required:false,
			});
		}
		textBoxDefaultSetting(obj);
	}
	/**
	* 驗證合約存儲頁面的表單提交
	*/
	function validateFormInContract () {
		if (editIndex == undefined) {
			return true;
		}
		var controls = ['assetCategory','assetTypeId','amount','safetyStock','price'];
		if(!validateFormInRow('assetTable', editIndex, controls)){
			return false;
		} else {
			$("div.topSoller").unbind("scroll.validate");
			//editIndex = undefined;
			return true;
		}
	}
	</script>
</body>
</html>