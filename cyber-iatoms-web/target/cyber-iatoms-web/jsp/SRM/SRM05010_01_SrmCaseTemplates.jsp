<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%@ include file="/jsp/common/easyui-common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.SrmCaseTemplatesFormDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	SrmCaseTemplatesFormDTO formDTO = null;
	SrmCaseHandleInfoDTO srmCaseHandleInfoDTO = null;
	String ucNo = null;
	if (ctx != null) {
		formDTO = (SrmCaseTemplatesFormDTO) ctx.getResponseResult();
	} 
	if (formDTO == null) {
		formDTO = new SrmCaseTemplatesFormDTO();
	}else{
		ucNo = formDTO.getUseCaseNo();
		srmCaseHandleInfoDTO = formDTO.getSrmCaseHandleInfoDTO();
	}
	//srmCaseHandleInfoDTO = new SrmCaseHandleInfoDTO();
	List<Parameter> caseTemplatesList = (List<Parameter>) SessionHelper.getAttribute(request,ucNo,IATOMS_PARAM_TYPE.CASE_TEMPLATE_CATEGORY.getCode());
	//獲取交易參數可以編輯的列名，以交易參數分組
	String editFildsMap = (String)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_TRANSACTION_PARAMETER_EDIT_FIELDS_MAP);
	//交易類別
	String transationCategoryList = (String)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.TRANSACTION_CATEGORY.getCode());
	//交易參數項目列表
	List<Parameter> transationParameterItemList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_TRANSACTION_PARAMETER_ITEM_LIST);
%>
<c:set var="caseTemplatesList" value="<%=caseTemplatesList %>" scope="page"></c:set>
<c:set var="formDTO" value="<%=formDTO %>" scope="page"></c:set>
<c:set var="editFildsMap" value="<%=editFildsMap %>" scope="page"></c:set>
<c:set var="transationCategoryList" value="<%=transationCategoryList %>" scope="page"></c:set>
<c:set var="transationParameterItemList" value="<%=transationParameterItemList %>" scope="page"></c:set>
<c:set var="srmCaseHandleInfoDTO" value="<%=srmCaseHandleInfoDTO %>" scope="page"></c:set>
<html>
<head>
<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
<script type="text/javascript" src="${contextPath}/js/fineuploader-5.0.2.js"></script>
	<link rel="stylesheet" href="${contextPath}/css/fineuploader-5.0.2.css"/>
</head>
<body>
<div style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
    <!-- <div region="center" style="width: auto; height: auto; padding: 1px; overflow-y: hidden"> -->
        <div id="p" class="easyui-panel" title="工單範本維護" style="width: 98%; height: auto; overflow: hidden;padding: 10px 10px">
       
                <div id="Div1" class="easyui-panel" title="範本" style="width: auto; height: auto; overflow: hidden">
	                <form id="searchForm">
	                <table>
	                <div align="left"><span id="message" class="red"></span></div>
	                <tr>
	                <td>
	                    範本類別:<span class="red">*</span>
	                    </td>
	                    <td>
							<cafe:droplisttag 
								id="category"
								name="category" 
								css="easyui-combobox"
								style="width: 240px"
								result="${caseTemplatesList}"
								javascript="panelHeight='auto' editable=false invalidMessage=\"請輸入範本類別\" data-options=\"validType:'requiredDropList'\""
								hasBlankValue="true" 
								blankName="請選擇">
							</cafe:droplisttag>
							</td>
							<td>
							範本: 
							</td>
							<td>
							<cafe:fileuploadTag
									id="uploadFiled"
									name="uploadFiled"
									uploadUrl="${contextPath}/workTemplate.do" 
									showFileList="false"
									sizeLimit = "5000000"
									showName="上傳"
									acceptFiles=".xls,.xlsx"
	                           		allowedExtensions="'xls', 'xlsx'"
									isCustomError = "true"
									errorMsg = "xls、xlsx"
									autoUpload="true"
									messageAlert="false"
									multiple="false"
									whetherDelete="true"
									whetherDownLoad="false"
									javaScript="onComplete:showMessage,onError:function(id, name, reason, maybeXhrOrXdr){
										$('#message').text(reason);
									},onSubmit:submitUploadata"
									> 
	                           	</cafe:fileuploadTag>
	                           	</td>
	                           	</tr>
                      </table>    
								<div class="qq-upload-list-selector qq-upload-list">
									<div class="qq-upload-success"  id="srmCaseTemplatesList"
										style="border-bottom-color: gray; border-bottom-width: 1px; border-bottom-style: dotted;">
											<c:if test="${!empty formDTO.srmCaseTemplatesDTOList}">
												<c:forEach items="${formDTO.srmCaseTemplatesDTOList}" var="attachedFile">
													<div>
														<a class="qq-upload-file ucFileUploaderLinkButton" onclick = "downloadFile('${attachedFile.id}');"
															href="#">${attachedFile.fileName}</a> 
														<a class="qq-upload-file ucFileUploaderLinkButton" onclick = "deleteFile(this,'${attachedFile.id}');"
															href="#">刪除</a> <br/>
													</div>
												</c:forEach> 
											</c:if> 
									</div>
								</div>
							<input type="hidden" id = "uploadFileId" name = "uploadFileId" value=""/>
							</form>
					</div>
                	<div><span class="red">*</span>僅支援 xls、xlsx 格式</div>
		            <div style="margin-top: 10px;"></div>
		            <div style="margin-top: 10px;"></div>
		            <div>
			            <div><span class="red">*</span>黑色遮罩為不支援功能</div>
						<!-- 交易參數 -->
						 <tiles:insertAttribute name="tradingParameters">
							<tiles:putAttribute name="caseCategory" value="UNINSTALL"></tiles:putAttribute>
							<tiles:putAttribute name="transactionParameterItemDTOs" value="${transationParameterItemList }"></tiles:putAttribute>
							<tiles:putAttribute name="editabledTradingTypes" value="${transationCategoryList }"></tiles:putAttribute>
							<tiles:putAttribute name="editabledFields" value="${editFildsMap }"></tiles:putAttribute>
							<tiles:putAttribute name="caseHandleInfoDTO" value="${srmCaseHandleInfoDTO }"></tiles:putAttribute>
							<tiles:putAttribute name="hasUpdate" value="N"></tiles:putAttribute>
						</tiles:insertAttribute> 
					</div>
			</div>
      </div>
    <script type="text/javascript">
	var categoryValue;
	$(function(){
		//範本類別
		$("#category").combobox({
    		onChange:function(newValue, oldValue){
    			$("#message").text("");
    			categoryValue = newValue;
    		}
    	});
	})
	//上傳是傳遞參數和驗證   categoryValue:範本類別
	function submitUploadata(id, name){
		var controls = ['category'];
		//驗證範本類別
		if(!validateForm(controls)){
			return false;
		}
		/*if(categoryValue == undefined || categoryValue == ""){
			$("#message").text("請輸入範本類別");	
			return false;
		}*/
		var uploadFileParam = $("#uploadFileId").val();
		//傳參數
		if(uploadFileParam == "") {
			uploadFileParam = id+"/"+categoryValue+"/"+name;
		} else {
			uploadFileParam = uploadFileParam+","+id+"/"+categoryValue+"/"+name;
		}
		//給隱藏域設置參數值
		$("#uploadFileId").val(uploadFileParam);
		uploadFiled.setParams({
			actionId:'upload',
			categoryId:categoryValue 
		});  
	}
	//匯入返回處理
	function showMessage(id, fileName, response) {
		var uploadFileid = $("#uploadFileId").val();
		//本頁面最新上傳的所有文檔
		var uploadFileParam = [];
		//每個文檔的具體類容
		var uploadFileParams = [];
		//文檔的範本類別
		var uploadCategory = "";
		uploadFileParam = uploadFileid.split(",");
		for(var i = 0; i < uploadFileParam.length; i++) {
			uploadFileParams = uploadFileParam[i].split("/");
			if(id == uploadFileParams[0] && fileName == uploadFileParams[2]) {
				uploadCategory = uploadFileParams[1];
			}
		}
		var uploadTemplatesId = "";
		$("#message").text("");	
		if (response.success) {
			$("#message").text(response.msg);
			//uploadCategory  範本類別  ，fileName 範本名稱
			javascript:dwr.engine.setAsync(false);
			ajaxService.getUploadTemplatesId(uploadCategory, fileName, function(data){
				if(data){
					//獲取上傳成功數據
					uploadTemplatesId = data;
				}
			});
			javascript:dwr.engine.setAsync(true);
			var div = document.getElementById("srmCaseTemplatesList");
			//上傳完成，拼接上傳完成的html。
			div.innerHTML=div.innerHTML+"<div style='display: block;'><a class='qq-upload-file ucFileUploaderLinkButton' href='#' onclick=\"downloadFile('"+ uploadTemplatesId +"');\">" + fileName + "</a><a class='qq-upload-file ucFileUploaderLinkButton' href='#' onclick=\"deleteFile(this,'"+ uploadTemplatesId +"');\">刪除</a><br></div>";
		} else {
			if(response.msg && response.errorFileName == null){
				$("#message").text(response.msg);
			} 
		}
	}
	// 文件下載
	function downloadFile(id) {
		ajaxService.checkCaseTemplatesDownLoadFile(id, function(data){
			if (data) {
				$("#message").text("");
				createSubmitForm("${contextPath}/workTemplate.do","actionId=<%=IAtomsConstants.ACTION_DOWNLOAD%>&fileId=" + id,"post");
				 <%--popH = $.messager.show({
						msg:'Loading...',
						modal:true,
						showType:'slide',
						showSpeed:1,
						width:150,
						height:50,
						closable:false,
						noheader:true,
						showType:'fade',
						style:{
						    }
					}); --%>
			} else {
				$("#message").text('文件不存在');
			}
		});
	}
	//刪除文件 obj 刪除的文件對象 id 要刪除的文件的id
	function deleteFile(obj,id) {
		//下載以上傳的文件時，根據文件ID獲取路徑以及名稱，判斷文件是否存在
		ajaxService.checkCaseTemplatesDownLoadFile(id, function(data){
			if (data) {
				$("#message").text("");
				var params = {
						fileId:id,
						actionId:'<%=IAtomsConstants.ACTION_DELETE%>'
				};
				var url = '${contextPath}/workTemplate.do';
				var successFunction = function(json) {
					if (json.success) {
						$("#dataGrid").datagrid("reload");
						var deleteFile = $(obj).parent();
							deleteFile.hide();
						$("#message").text(json.msg);	
					}else{
						$("#message").text(json.msg);
					}
				};
				var errorFunction = function(){
					$("#message").text("刪除失敗");
				};
				commonDelete(params,url,successFunction,errorFunction);
			} else {
				$("#message").text('文件不存在');
			}
		});
	 }
</script>
</body>
</html>