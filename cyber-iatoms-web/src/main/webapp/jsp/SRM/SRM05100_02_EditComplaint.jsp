<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@include file="/jsp/common/easyui-common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.IAtomsConstants"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.ComplaintFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.ComplaintDTO"%>
<%
	//初始化加載頁面數據
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	ComplaintFormDTO formDTO = null;
	if (ctx != null) {
		formDTO = (ComplaintFormDTO) ctx.getResponseResult();
	} else {
		formDTO = new ComplaintFormDTO();
	}

	ComplaintDTO complaintDTO = formDTO.getComplaintDTO();
	if(complaintDTO == null) {
		complaintDTO = new ComplaintDTO();
	}
	//客戶下拉菜單
	List<Parameter> customerList = (List<Parameter>)SessionHelper.getAttribute(request, IAtomsConstants.UC_NO_SRM_05100, IAtomsConstants.PARAM_CUSTOMER_LIST);
	
	//歸責廠商
	List<Parameter> vendorList = (List<Parameter>)SessionHelper.getAttribute(request, IAtomsConstants.UC_NO_SRM_05100, IAtomsConstants.PARAM_VENDOR_LIST);
	
	//檔案上傳最大限制
	String uploadFileSize = formDTO.getUploadFileSize();
	
	//問題分類
	List<Parameter> questionTypeList = (List<Parameter>)SessionHelper.getAttribute(request, IAtomsConstants.UC_NO_SRM_05100, IATOMS_PARAM_TYPE.QUESTION_TYPE.getCode());
	
	//是否下拉框
	List<Parameter> yesOrNoList = (List<Parameter>)SessionHelper.getAttribute(request, IAtomsConstants.UC_NO_SRM_05100, IAtomsConstants.YES_OR_NO);
%>
<html>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="complaintDTO" value="<%=complaintDTO%>" scope="page"></c:set>
<c:set var="customerList" value="<%=customerList%>" scope="page"></c:set>
<c:set var="vendorList" value="<%=vendorList%>" scope="page"></c:set>
<c:set var="uploadFileSize" value="<%=uploadFileSize%>" scope="page"></c:set>
<c:set var="questionTypeList" value="<%=questionTypeList%>" scope="page"></c:set>
<c:set var="yesOrNoList" value="<%=yesOrNoList%>" scope="page"></c:set>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
	<script type="text/javascript" src="${contextPath}/js/fineuploader-5.0.2.js"></script>
	<link rel="stylesheet" href="${contextPath}/css/fineuploader-5.0.2.css"/>
<title></title>
</head>
<body>
	<div id="editDlg" data-options="region:'center', fit:true" 
        style="width: auto; height: auto; padding: 10px 20px; overflow-y: hidden">
        <div class="dialogtitle">客訴管理</div>
            <span id="errorMsg" class="red" style="font-weight: normal;"></span>
            <form id="editForm" method="post" novalidate>
                <table cellpadding="3">
                    <tr>
                        <td><label id="complNum">客訴案號:</label></td>
						<input type="hidden" id="<%=ComplaintDTO.ATTRIBUTE.CASE_ID.getValue() %>"
						name="<%=ComplaintDTO.ATTRIBUTE.CASE_ID.getValue() %>"
						value="${empty complaintDTO.caseId ? '' : complaintDTO.caseId}"/>
                        <c:choose>
                        	<c:when test="${empty complaintDTO.caseId}">
                        		<td>(儲存後由系統產生)</td>
                        	</c:when>
                        	<c:otherwise>
                        		<td>${complaintDTO.caseId }</td>
                        	</c:otherwise>
                        </c:choose>
                        <td>申訴人員:<span class="red">*</span></td>
                        <td>
                            <input class="easyui-textbox" type="text"
	                            id="<%=ComplaintDTO.ATTRIBUTE.COMPLAINT_STAFF.getValue() %>"
	                            name="<%=ComplaintDTO.ATTRIBUTE.COMPLAINT_STAFF.getValue() %>"
	                            value="<c:out value='${complaintDTO.complaintStaff}'/>"
	                            style="width:185px;height:20px;" maxlength="50"
	                            required="true" missingMessage="請輸入申訴人員"
	                            data-options="validType:['maxLength[50]']"/>
                        </td>
                    </tr>
                    <tr>
                        <td>發生日期:<span class="red">*</span></td>
                        <td>
                        	<input id="<%=ComplaintDTO.ATTRIBUTE.COMPLAINT_DATE.getValue() %>"
								name="<%=ComplaintDTO.ATTRIBUTE.COMPLAINT_DATE.getValue() %>"
								class="easyui-datebox" value="${complaintDTO.complaintDate }"
								maxlength="10" style="width: 185px"
								required="true" missingMessage="請輸入發生日期"
								data-options="formatter:formaterTimeStampToyyyyMMDD, validType:['maxLength[10]', 'date[\'日期格式限YYYY/MM/DD\']']">
                        </td>
                        <td>聯繫方式:</td>
                        <td>
                        	<input class="easyui-textbox" type="text"
                            	id="<%=ComplaintDTO.ATTRIBUTE.CONTACT_WAY.getValue() %>"
								name="<%=ComplaintDTO.ATTRIBUTE.CONTACT_WAY.getValue() %>"
                        		value="${complaintDTO.contactWay}"
                            	style="width:185px;height:20px;" maxlength="100"
                        		data-options="validType:['maxLength[100]']"/>
                        </td>
                    </tr>
                    <tr>
                    	<td>客戶:<span class="red">*</span></td>
                    	<td>
			               <cafe:droplisttag
								id="<%=ComplaintDTO.ATTRIBUTE.CUSTOMER_ID.getValue() %>"
				                name="<%=ComplaintDTO.ATTRIBUTE.CUSTOMER_ID.getValue() %>"
				                result="${customerList }"
								selectedValue="${complaintDTO.customerId}"
				                css="easyui-combobox"
				                style="width:185px"
				                blankName="請選擇" 
				                hasBlankValue="true"
				                javascript="editable=false validType=\"ignore['請選擇']\" invalidMessage=\"請選擇客戶\""
				           ></cafe:droplisttag>
                    	</td>
                    	<td>VIP特店:</td>
                    	<td>
                   			<cafe:checklistTag
								name="<%=ComplaintDTO.ATTRIBUTE.IS_VIP.getValue() %>" 
								id="<%=ComplaintDTO.ATTRIBUTE.IS_VIP.getValue() %>" 
								type="radio"
								result="${yesOrNoList}"
								checkedValues='<%=StringUtils.toList((complaintDTO != null && StringUtils.hasText(complaintDTO.getCaseId())) ? complaintDTO.getIsVip() : IAtomsConstants.NO, ",")%>'
								javascript=""
							></cafe:checklistTag>
                    	</td>
                    </tr>
                    <tr>
                    	<td>特店代號:<span class="red">*</span></td>
                        <td colspan="3">
                            <input class="easyui-textbox" type="text"
                            	id="<%=ComplaintDTO.ATTRIBUTE.MERCHANT_CODE.getValue() %>"
								name="<%=ComplaintDTO.ATTRIBUTE.MERCHANT_CODE.getValue() %>"
                        		value="${complaintDTO.merchantCode}" required="true"
                            	style="width:185px;height:20px;" maxlength="20" missingMessage="請輸入特店代號"
                        		data-options="validType:['maxLength[20]']"/>
                        		
                        	<a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-ok" id="getMerchantBtn" onclick="checkInput();">&nbsp;</a>
                            <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-search" id="queryMerchantBtn" onclick="chooseMID();">查詢</a><br>
			            	<a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-add" id="addMerchant" onclick="addMerchant();">新增特店資料</a>
                            <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-edit" id="editMerchantBtn" onclick="updateMerchant();">修改特店資料</a>
                            
							<input id="<%=ComplaintDTO.ATTRIBUTE.MERCHANT_ID.getValue() %>"
								name="<%=ComplaintDTO.ATTRIBUTE.MERCHANT_ID.getValue() %>" 
								value="<c:out value='${complaintDTO.merchantId}'/>" type="hidden"/>
                        </td>
                    </tr>
                    <tr>
                        <td>特店名稱:</td>
                        <td>
                        	<input class="easyui-textbox" type="text"
                        		id="<%=ComplaintDTO.ATTRIBUTE.MERCHANT_NAME.getValue() %>"
                        		name="<%=ComplaintDTO.ATTRIBUTE.MERCHANT_NAME.getValue() %>"
                        		value="<c:out value='${complaintDTO.merchantName}'/>"
	                        	disabled="true" style="width:185px;height:20px;"/>
                        </td>
	                    <td>TID/DTID:<span class="red">*</span></td>
	                    <td>
	                    	<input class="easyui-textbox" type="text"
                            	id="<%=ComplaintDTO.ATTRIBUTE.TID.getValue() %>"
								name="<%=ComplaintDTO.ATTRIBUTE.TID.getValue() %>"
                        		value="${complaintDTO.tid}" required="true"
                            	style="width:185px;height:20px;" maxlength="8" missingMessage="請輸入TID/DTID"
                        		data-options="validType:['englishOrNumber[\'TID/DTID限輸入英數字，請重新輸入\']', 'maxLength[8]']" />
	                    </td>
                    </tr>
                    <tr>
                        <td>客訴內容:</td>
                        <td colspan="3">
                            <textarea class="easyui-textbox"
								id="<%=ComplaintDTO.ATTRIBUTE.COMPLAINT_CONTENT.getValue()%>"
								name="<%=ComplaintDTO.ATTRIBUTE.COMPLAINT_CONTENT.getValue()%>"
								maxlength="2000"
								data-options="multiline:true, validType:'maxLength[2000]'"
								style="height: 70px; width: 102%"><c:out value='${complaintDTO.complaintContent}'/></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td>處理過程:</td>
                        <td colspan="3">
                            <textarea class="easyui-textbox"
	                            id="<%=ComplaintDTO.ATTRIBUTE.HANDLE_CONTENT.getValue() %>"
	                            name="<%=ComplaintDTO.ATTRIBUTE.HANDLE_CONTENT.getValue() %>"
                                maxlength="1000"
                                data-options="multiline:true, validType:['maxLength[1000]']" 
                                style="height: 70px;width: 100%"><c:out value='${complaintDTO.handleContent}'/></textarea>
                        </td>
                    </tr>
                    <tr>
                    	<td>歸責廠商:<span class="red">*</span></td>
						<td>
							<cafe:droplisttag css="easyui-combobox"
								id="<%=ComplaintDTO.ATTRIBUTE.COMPANY_ID.getValue() %>"
								name="<%=ComplaintDTO.ATTRIBUTE.COMPANY_ID.getValue() %>"
								result="${vendorList }"
								selectedValue="${complaintDTO.companyId}"
								blankName="請選擇"
								style="width:185px"
								hasBlankValue="true"
								javascript="editable=false validType=\"ignore['請選擇']\" invalidMessage=\"請選擇歸責廠商\"">
							</cafe:droplisttag>
						</td>
						<td>歸責人員:</td>
						<td>
							<input class="easyui-textbox" type="text"
								id="<%=ComplaintDTO.ATTRIBUTE.USER_NAME.getValue() %>"
								name="<%=ComplaintDTO.ATTRIBUTE.USER_NAME.getValue() %>"
								value="${complaintDTO.userName}" maxlength="100"
                        		data-options="validType:['maxLength[100]']"
								style="width:185px;height:20px;"/>
						</td>
                    </tr>
                    <tr>
                    	<td>問題分類:<span class="red">*</span></td>
                    	<td colspan="3">
                   			<cafe:checklistTag
								name="<%=ComplaintDTO.ATTRIBUTE.QUESTION_TYPE.getValue() %>" 
								id="<%=ComplaintDTO.ATTRIBUTE.QUESTION_TYPE.getValue() %>" 
								type="radio"
								result="${questionTypeList}"
								checkedValues='<%=StringUtils.toList((complaintDTO != null && StringUtils.hasText(complaintDTO.getCaseId())) ? complaintDTO.getQuestionType() : IAtomsConstants.MARK_SPACE, ",")%>'
							></cafe:checklistTag>
                    	</td>
                    </tr>
                    <tr>
                    	<td>賠償客戶:</td>
                    	<td>
                   			<cafe:checklistTag
								name="<%=ComplaintDTO.ATTRIBUTE.IS_CUSTOMER.getValue() %>" 
								id="<%=ComplaintDTO.ATTRIBUTE.IS_CUSTOMER.getValue() %>" 
								type="radio"
								result="${yesOrNoList}"
								checkedValues='<%=StringUtils.toList((complaintDTO != null && StringUtils.hasText(complaintDTO.getCaseId())) ? complaintDTO.getIsCustomer() : IAtomsConstants.NO, ",")%>'
							></cafe:checklistTag>
                    	</td>
                    	<td>賠償金額:</td>
                    	<td>
                    		<input id="<%=ComplaintDTO.ATTRIBUTE.CUSTOMER_AMOUNT.getValue() %>"
                    			name="<%=ComplaintDTO.ATTRIBUTE.CUSTOMER_AMOUNT.getValue() %>"
                    			class="easyui-numberbox" disabled="true" value="${complaintDTO.customerAmount}"
                    			maxlength="7" data-options="validType:['maxLength[7]', 'positiveInt[\'賠償金額限正整數，請重新輸入\']']"
                    			style="width:185px;height:20px;">
                    	</td>
                    </tr>
                    <tr>
                    	<td>廠商罰款:</td>
                    	<td>
                   			<cafe:checklistTag
								name="<%=ComplaintDTO.ATTRIBUTE.IS_VENDOR.getValue() %>" 
								id="<%=ComplaintDTO.ATTRIBUTE.IS_VENDOR.getValue() %>" 
								type="radio"
								result="${yesOrNoList}"
								checkedValues='<%=StringUtils.toList((complaintDTO != null && StringUtils.hasText(complaintDTO.getCaseId())) ? complaintDTO.getIsVendor() : IAtomsConstants.NO, ",")%>'
							></cafe:checklistTag>
                    	</td>
                    	<td>罰款金額:</td>
                    	<td>
                    		<input id="<%=ComplaintDTO.ATTRIBUTE.VENDOR_AMOUNT.getValue() %>"
                    			name="<%=ComplaintDTO.ATTRIBUTE.VENDOR_AMOUNT.getValue() %>"
                    			class="easyui-numberbox" disabled="true" value="${complaintDTO.vendorAmount}"
                    			maxlength="7" data-options="validType:['maxLength[7]', 'positiveInt[\'罰款金額限正整數，請重新輸入\']']"
                    			style="width:185px;height:20px;">
                    	</td>
                    </tr>
                    <tr>
                    	<td>問題報告<br>客戶賠款請求單:</td>
                  		<td>
                    		<cafe:fileuploadTag
								id="file_upload"
								name="file_upload"
								acceptFiles=".txt, .rtf, .doc, .docx, .ppt, .pptx, .xls, .xlsx, .csv, .pdf, .7z, .zip, .rar, .png, .jpg, .jpeg, .gif, .mp4, .m4v, .avi, .mov, .rm, .rmvb, .wmv, .wma, .mp3, .amr, .3ga, .m4a"
								allowedExtensions="'txt', 'rtf', 'doc', 'docx', 'ppt', 'pptx', 'xls', 'xlsx', 'csv', 'pdf', '7z', 'zip', 'rar', 'png', 'jpg', 'jpeg', 'gif', 'mp4', 'm4v', 'avi', 'mov', 'rm', 'rmvb', 'wmv', 'wma', 'mp3', 'amr', '3ga', 'm4a'"
								showFileList="true"
								uploadUrl="${contextPath}/complaintManage.do?actionId=saveFile&fileName=${formDTO.fileName}"
								sizeLimit="${uploadFileSize }"
								messageId="errorMsg"
								showName="上傳檔案"
								isCustomError="true"
								autoUpload="false"
								messageAlert="false"
								multiple="true"
								whetherDelete="true"
								whetherDownLoad="false"
								> 
                           	</cafe:fileuploadTag>
                           	<input type="hidden" id="fileName" name="fileName" value="${formDTO.fileName}">
                    	</td>
	                	<td colspan="2">
		                	<c:forEach items="${formDTO.srmComplaintCaseFileDTOs}" var="complaintFile">
		                		<div>
		                			<a class="qq-upload-file ucFileUploaderLinkButton" onclick="downloadCaseFileShow('${complaintFile.fileId}');" 
		                				href="javascript:void(0)">${complaintFile.fileName}</a>
		                			<a class="qq-upload-file ucFileUploaderLinkButton" onclick = "deleteFile(this,'${complaintFile.fileId}');"
										href="#">刪除</a>
		                		</div>
		                	</c:forEach>
		                    <input type="hidden" id="deleteFileId" name="deleteFileId"/>
	                	</td>
                    </tr>
                </table>
            </form>
        </div>
        <div id="merchanInfoDlg"></div>
		<div id="merchanEditDlg"></div>
		<div id="dlvSearch"></div>
    </div>
    <script type="text/javascript">
    /**
	 * 下載附件檔案
	 * id：附件檔案ID
	 */
	function downloadCaseFileShow(id){
		//判斷該文件是否存在
		ajaxService.checkComplaintFile(id, function(data){
			if (data) {
				//下載文件
				createSubmitForm("${contextPath}/complaintManage.do","actionId=<%=IAtomsConstants.ACTION_DOWNLOAD%>&fileId=" + id, "post");
			} else {
				$("#errorMsg").html("文件不存在");
			}
		});
	}
    
    /**
     * 初始化頁面信息
     */
	$(function() {
		//修改頁面
		if ('${complaintDTO.caseId}' != "") {
			//有更改客戶的話就把店資料清除
			$('#customerId').combobox({
				onSelect:function(row) {
					$("#merchantId").val('');
					$("#merchantCode").textbox("setValue", '');
					$("#merchantName").textbox("setValue", '');
				}
			});
			//根據賠償客戶與廠商罰款來初始化賠償金額與罰款金額
			if ('${complaintDTO.isCustomer}' == "Y") {
				$('#<%=ComplaintDTO.ATTRIBUTE.CUSTOMER_AMOUNT.getValue() %>').textbox({disabled:false});
			}
			if ('${complaintDTO.isVendor}' == "Y") {
				$('#<%=ComplaintDTO.ATTRIBUTE.VENDOR_AMOUNT.getValue() %>').textbox({disabled:false});
			}
		}
		//當賠償客戶與廠商罰款有改變時，變更賠償金額與罰款金額的disabled 屬性
		$('input:radio[name="<%=ComplaintDTO.ATTRIBUTE.IS_CUSTOMER.getValue() %>"]').change(function() {
			if ($(this).val() == "Y") {
				$('#<%=ComplaintDTO.ATTRIBUTE.CUSTOMER_AMOUNT.getValue() %>').textbox({disabled:false});
			} else {
				$('#<%=ComplaintDTO.ATTRIBUTE.CUSTOMER_AMOUNT.getValue() %>').textbox({value:'', disabled:true});
			}
			textBoxDefaultSetting($('#customerAmount'));
		});
		$('input:radio[name="<%=ComplaintDTO.ATTRIBUTE.IS_VENDOR.getValue() %>"]').change(function() {
			if ($(this).val() == "Y") {
				$('#<%=ComplaintDTO.ATTRIBUTE.VENDOR_AMOUNT.getValue() %>').textbox({disabled:false});
			} else {
				$('#<%=ComplaintDTO.ATTRIBUTE.VENDOR_AMOUNT.getValue() %>').textbox({value:'', disabled:true});
			}
			textBoxDefaultSetting($('#vendorAmount'));
		});
		//發生日期限制在系統當日之前
		var calendar = $('#<%=ComplaintDTO.ATTRIBUTE.COMPLAINT_DATE.getValue() %>').datebox().datebox('calendar');
		calendar.calendar('options').validator = function(date) {
			var nowDate = new Date();
			if (date <= nowDate) {
				return true;
			} else {
				return false;
			}
		};
	});
	
	//查詢特店的dialog
	var viewChooseMid;
 	
	/**
	 * 確認客戶與特店代號有無完整輸入
	 */
	function checkInput() {
		$("#errorMsg").html("");
		var customerId = $("#customerId").combobox("getValue");
		var merchantCode = $("#merchantCode").val();
		if (customerId == null || customerId == "") {
			$("#errorMsg").html("請輸入客戶");
			return false;
		} else if(merchantCode == null || merchantCode == "") {
			$("#errorMsg").html("請輸入特店代號");
			return false;
		}
		javascript:dwr.engine.setAsync(false);
		//根據客戶與特店代號獲取特店信息
		ajaxService.getMerchantDTOById("", merchantCode, customerId, function(result){
			if (result != null) {
				//特店主鍵、特店代號、特店名稱
				$("#merchantId").val(result.merchantId);
				$("#merchantCode").textbox("setValue", merchantCode);
				$("#merchantName").textbox("setValue", result.name);
			} else {
				$("#merchantName").textbox("setValue", "");
				$("#errorMsg").html("查無資料");
			}
		});
		javascript:dwr.engine.setAsync(true);
	}
	
	/**
	 * 點選特店代號後的查詢按鈕，查詢所選客戶下的特店信息
	 */
	function chooseMID() {
		$("#errorMsg").html("");
		//獲取客戶ID，如果客戶ID為空，則提示請輸入客戶
		var companyId = $("#customerId").combobox("getValue");
		if (companyId == null || companyId == "") {
			$("#errorMsg").html("請輸入客戶");
			return;
		} else {
			$("#customerId").val(companyId);
		}

		viewChooseMid = $('#merchanInfoDlg').dialog({
			title: "選擇 MID", 
			width: 840, 
			height: 495,
			top:10,
			closed: false, 
			cache: false, 
			queryParams : {
				actionId : 'initMid',
				queryCompanyId: companyId
			}, 
			href: "${contextPath}/merchant.do",
			modal: true,
			onBeforeLoad : function() {
				// clear查詢Mid對話框
				if(viewChooseMid){
					viewChooseMid.panel('clear');
				}
			},
			onBeforeOpen : function() {
				// clear查詢Mid對話框
				if(viewChooseMid) {
					viewChooseMid.panel('clear');
				}
			},
			onLoad : function() {
				textBoxSetting("merchanInfoDlg");
				if(typeof settingDialogPanel != 'undefined' && settingDialogPanel instanceof Function) {
					settingDialogPanel('merchanInfoDlg');
				}
			},
		});
	}
	
	/**
	 * chooseMid選中事件
	 */
	function chooseMidOnCheck(index, row) {
		if (row == null) {
			$("#queryMidMsg").html("請選擇一筆資料");
			return false;
		}
		var merchantId = row.merchantId;
		javascript:dwr.engine.setAsync(false);
		//根據所選擇特店代號的特店表頭，獲取特店信息
		ajaxService.getMerchantDTOById(merchantId, "", "", function(result){
			//將查詢結果賦值到特店資訊各欄位
			$("#merchantId").val(result.merchantId);
			$("#merchantCode").textbox("setValue", result.merchantCode);
			$("#merchantName").textbox("setValue", result.name);
			viewChooseMid.dialog('close');
		});
		javascript:dwr.engine.setAsync(true);
	}
	
	/**
	 * 新增特店信息
	 */
	function addMerchant() {
		$("#errorMsg").html("");
		var customerId = $("#customerId").combobox("getValue");
		var merchantCode = $("#merchantCode").val();
		//獲取客戶ID，如果客戶ID為空，則提示請輸入客戶
		if (customerId == "") {
			$("#errorMsg").html("請輸入客戶");
		} else {
			javascript:dwr.engine.setAsync(false);
			//根據客戶與特店代號獲取特店信息
			ajaxService.getMerchantDTOById("", merchantCode, customerId, function(result){
				if (result == null) {
					initEditMerchant('initAdd', '');
				} else {
					$("#errorMsg").html("此特店已存在");
				}
			});
			javascript:dwr.engine.setAsync(true);
		}
	}
	
	/**
	 * 修改特店信息--點選修改特店按鈕
	 */
	function updateMerchant() {
		$("#errorMsg").html("");
		var customerId = $("#customerId").combobox("getValue");
		var merchantCode = $("#merchantCode").val();
		if (customerId == "") {
			$("#errorMsg").html("請輸入客戶");
		} else {
			if (merchantCode != "") {
				//根據特店代號以及客戶獲取對應的特店信息。用於獲取該特店ID
				ajaxService.getMerchantDTOById("", merchantCode, customerId, function(result){
					if (result != null) {
						$("#merchantId").val(result.merchantId);
						//初始化修改特店信息頁面
						initEditMerchant('initEdit', result.merchantId);
					} else {
						$("#errorMsg").html("查無資料");
					}
				});
			} else {
				$("#errorMsg").html("請輸入特店代號");
			}
		}
	}
	
	/**
	 * 初始化修改特店信息頁面
	 * merchantId：特店ID
	 */
	function initEditMerchant(actionId, merchantId) {
		var title = "";
		if (merchantId != '') {
			title = '修改客戶特店維護';
		} else {
			title = '新增客戶特店維護';
		}
		var viewEditMerchat = $('#merchanEditDlg').dialog({
			title: title, 
			width: 700, 
			height: 400, 
			top:10,
			closed: false, 
			cache: false, 
			queryParams : {
				actionId : actionId,
				merchantId : merchantId
			}, 
			href: "${contextPath}/merchant.do",
			modal: true,
			onLoad : function() {
				textBoxSetting("merchanEditDlg");
				if (actionId == 'initAdd') {
					var f = viewEditMerchat.find("form");
					$("#merchantId").val('');
					f.find("#companyId").combobox({value:$("#customerId").combobox("getValue"), disabled:true});
					f.find("#merchantCode").textbox('setValue', $('#merchantCode').textbox('getValue'));
				}
			},
			buttons : [{
				text:'儲存',
				width:88,
				iconCls:'icon-ok',
				handler: function(){					
					var f = viewEditMerchat.find("form");
					if (f.form("validate")) {
						//取值
						var saveParam = f.form("getData");
						if (saveParam.companyId != '') {
							saveParam.companyId = f.find("#companyId").combobox('getValue');
						}
						if (saveParam.merchantCode != '') {
							saveParam.merchantCode = f.find("#merchantCode").val();
						}
						if (saveParam.name != '') {
							saveParam.name = f.find("#name").val();
						}
						//save
						$.ajax({
							url: "${contextPath}/merchant.do?actionId=save",
							data:saveParam,
							type:'post', 
							cache:false, 
							dataType:'json', 
							success:function(result) {
								if (result.success) {
									var merchantId = $("#merchantId").val();
									if (merchantId != '' && merchantId != null) {
										//獲取修改完成后的特店信息，更新特店信息
										ajaxService.getMerchantDTOById(merchantId, "", "", function(result){
											if (result != null) {
												$("#merchantId").val(result.merchantId);
												$("#merchantCode").textbox("setValue", result.merchantCode);
												$("#merchantName").textbox("setValue", result.name);
											}
										});
									} else {
										//獲取新增完成後的特店信息，更新特店信息
										var customerId = $("#customerId").combobox("getValue");
										var merchantCode = $("#merchantCode").val();
										ajaxService.getMerchantsByCodeAndCompamyId(merchantCode, customerId, function(result) {
											if (result != null) {
												$("#merchantId").val(result[0].value);
												$("#merchantCode").textbox("setValue", saveParam.merchantCode);
												$("#merchantName").textbox("setValue", saveParam.name);
											}
										});
									}
									viewEditMerchat.dialog('close');
								}else{
									$("#msg").text(result.msg);
								}
							},
							error:function() {
								if (actionId == 'initEdit') {
									$.messager.alert('提示', "客戶特店資料修改失敗", 'error');
								} else {
									$.messager.alert('提示', "客戶特店資料新增失敗", 'error');
								}
							}
						});
					}
				}
			},{
				text:'取消',
				width:88,
				iconCls:'icon-cancel',
				handler: function() {
					$.messager.confirm('確認對話框','確認取消?', function(isCancle) {
						if (isCancle) {
							viewEditMerchat.dialog('close');
						}	
					});
				}
			}]
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
    </script>
</body>
</html>