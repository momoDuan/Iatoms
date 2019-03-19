<%-- 
	案件附加檔案元件
	author：carrieDuan 
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/common/taglibs.jsp"%>
<%@page import="java.util.*,java.lang.*"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.IAtomsConstants"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAttFileDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO"%>
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$CASE_CATEGORY" var="caseCategoryAttr" />
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$CASE_STATUS" var="caseStatusAttr" />
<%-- 案件類別 --%>
<tiles:useAttribute id="caseManagerFormDTO" name="caseManagerFormDTO" classname="com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO"/>
<tiles:useAttribute id="caseCategory" name="caseCategory" classname="java.lang.String" ignore="true"/>
<%-- 案件處理DTO  SrmCaseHandleInfoDTO--%>
<tiles:useAttribute id="caseHandleInfoDTO" name="caseHandleInfoDTO" classname="com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO" ignore="true" />
<%-- 上传文件最大限制 --%>
<tiles:useAttribute id="uploadFileSize" name="uploadFileSize" classname="java.lang.String" ignore="true" />
<%-- 客戶下拉列表 List<Parameter> --%>
<tiles:useAttribute id="customers" name="customers" classname="java.util.List" ignore="true"/>

<tiles:useAttribute id="contextPathAddition" name="contextPathAddition" classname="java.lang.String" ignore="true" />
<%
	if (caseHandleInfoDTO == null) {
		caseHandleInfoDTO = new SrmCaseHandleInfoDTO();
	}
	if (caseHandleInfoDTO.getCaseAttFileDTOs() == null) {
		caseHandleInfoDTO.setCaseAttFileDTOs(new ArrayList<SrmCaseAttFileDTO>());
	}
%>
<c:set var="caseHandleInfoDTO" value="<%=caseHandleInfoDTO %>" scope="page"></c:set>
<c:set var="stuff" value="case" scope="page"></c:set>
<c:set var="gpCustomerId" value="<%=caseManagerFormDTO.getGpCustomerId() %>" scope="page"></c:set>
<c:set var="tsbCustomerId" value="<%=caseManagerFormDTO.getTsbCustomerId() %>" scope="page"></c:set>
	<div id="caseAdd">
		<table style="width: 100%">
			<tr>
				<td width="10%">其他說明:</td>
				<td colspan="2">
					<textarea id="${stuff }_description" name="${stuff }_description" 
					><c:out value='${caseHandleInfoDTO.description }'/></textarea>
				</td>
				<c:if test="${caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.MERGE.code eq caseCategory}">
						<c:choose>
							<c:when test="${empty caseHandleInfoDTO.caseId or caseHandleInfoDTO.caseStatus eq caseStatusAttr.WAIT_DISPATCH.code}">
							</c:when>
							<c:otherwise>
								<td width="10%">異動說明:</td>
								<td colspan="2">
									<textarea id="${stuff }_updatedDescription_vo" name="${stuff }_updatedDescription_vo"
									 disabled }><c:out value='${caseHandleInfoDTO.updatedDescription }'/></textarea>
								</td>
							</c:otherwise>
						</c:choose>
						<input type="hidden" id="${stuff }_updatedDescription" name="${stuff }_updatedDescription" value="<c:out value='${caseHandleInfoDTO.description }'/>"/>
				</c:if>
			</tr>
			<tr>
				<td width="10%">附加檔案: </td>
				<td>
					<cafe:fileuploadTag
						id="file_upload"
						name="file_upload"
						allowedExtensions="all"
						showFileList="true"
						uploadUrl="${contextPathAddition }/caseHandle.do?actionId=saveFile&fileName=${caseManagerFormDTO.fileName}"
						sizeLimit = "${uploadFileSize }"
						messageId = "errorMsg"
						showName="上傳"
						isCustomError = "true"
						autoUpload="false"
						messageAlert="false"
						multiple="true"
						deleletParams="{actionId:'deleteTempFile',fileName:'${caseManagerFormDTO.fileName}'}"
						deleteUrl="${contextPathAddition }/caseHandle.do"
						javaScript="onAllComplete:uploadAllComplete"
					></cafe:fileuploadTag>
				<div>
					<c:choose>
						<c:when test="${not empty caseHandleInfoDTO.caseAttFileDTOs}">
							<c:forEach items="${caseHandleInfoDTO.caseAttFileDTOs}" var="attachedFile">
								<div>
									<a class="qq-upload-file ucFileUploaderLinkButton" onclick = "downloadCaseFile('${attachedFile.attFileId}', '${caseHandleInfoDTO.isHistory }');"
										href="#">${attachedFile.fileName}</a> 
									<a class="qq-upload-file ucFileUploaderLinkButton delete-button" onclick = "deleteCaseFile(this,'${attachedFile.attFileId}', '${caseHandleInfoDTO.isHistory }');"
										href="#">刪除</a> <br/>
								</div>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<c:forEach items="${caseHandleInfoDTO.caseAttFileDTOs}" var="attachedFile">
								<div>
									<a class="qq-upload-file ucFileUploaderLinkButton" onclick = "" href="#">${attachedFile.fileName}</a> 
									<a class="qq-upload-file ucFileUploaderLinkButton delete-button" onclick = "" href="#">刪除</a> <br/>
								</div>
							</c:forEach> 
						</c:otherwise>
					</c:choose>
				</div>
					<input type="hidden" id = "deleteFileId" name = "deleteFileId"/>		
				</td>
			</tr>
			<input type="hidden" name="fileName" id="fileName" value = "${caseManagerFormDTO.fileName}">
			<tr>
				<td colspan="2" style="font-size: 16px;color: red">
					<span id="errorMsg" class="red"></span>
				</td>
			</tr>
			<tr>
				<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory }">
					<td width="10%">到場次數:</td>
					<td width="30%">
						<input id="${stuff }_attendanceTimes_vo" name="${stuff }_attendanceTimes_vo" value="${caseHandleInfoDTO.attendanceTimes==null?'0':caseHandleInfoDTO.attendanceTimes }" disabled />次
						<input type="hidden" id="${stuff }_attendanceTimes" name="${stuff }_attendanceTimes" value="${caseHandleInfoDTO.attendanceTimes==null?'0':caseHandleInfoDTO.attendanceTimes }"/>
					</td>
					<td>DTID:</td>
					<td><input id="${stuff }_showDtid"
						name="${stuff }_showDtid" value="<c:out value='${caseHandleInfoDTO.dtid }'/>" disabled></td>
				</c:if>
				<c:if test="${caseCategoryAttr.INSTALL.code ne caseCategory }">
					<td>到場次數:</td>
					<td>
						<input id="${stuff }_attendanceTimes_vo" name="${stuff }_attendanceTimes_vo" value="${caseHandleInfoDTO.attendanceTimes==null?'0':caseHandleInfoDTO.attendanceTimes }" disabled />次
						<input type="hidden" id="${stuff }_attendanceTimes" name="${stuff }_attendanceTimes" value="${caseHandleInfoDTO.attendanceTimes==null?'0':caseHandleInfoDTO.attendanceTimes }"/>
					</td>
				</c:if>
			</tr>
			<input type="hidden" name="oldCaseId" id="oldCaseId" value = "">
			<c:choose>
				<c:when test="${ empty caseHandleInfoDTO.caseId }">
					<tr>
							<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory }">
								<td width="10%">
									案件筆數:<span class="red">*</span>
								</td>
								<td width="30%">
									<input type="radio" name="caseNum" id="complex" checked="checked" onclick="checkComplex(this);"/>單筆
									<input type="radio" name="caseNum" id="complexNum" onclick="checkComplexNum(this);"/>複製多筆
									<input name="caseNumber" id ="caseNumber" disabled="disabled">筆
								</td>
							</c:if>
							<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.MERGE.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
									<td>派工TMS:</td>
									<td>
										<input type="checkbox" name="radio" id="isTms" checked="checked"/>派工
									</td>
							</c:if>
					</tr>
				</c:when>
				<c:otherwise>
					<c:if test="${caseHandleInfoDTO.caseStatus eq caseStatusAttr.WAIT_DISPATCH.code}">
						<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.MERGE.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
							<tr>
								<td >派工TMS:</td>
								<td >
									<c:if test="${caseHandleInfoDTO.isTms eq 'Y'}">
										<input type="checkbox" name="radio" id="isTms" checked="checked" />派工
									</c:if>
									<c:if test="${caseHandleInfoDTO.isTms ne 'Y'}">
										<input type="checkbox" name="radio" id="isTms" />派工
									</c:if>
								</td>
							</tr>
						</c:if>
					</c:if>
				</c:otherwise>
			</c:choose>
	</table>
	</div>
	<script type="text/javascript">
	$(function () {
		// 加載案件附加資料區塊
		setTimeout('loadingCaseAddInfo();',10);
	//	loadingCaseAddInfo()
	});
		/*
		* 加載案件附加資料區塊
		*/
		function loadingCaseAddInfo(){
			// panel
			$("#caseAdd").panel({title:'案件附加資料',width:'99%'});
			// 其他說明
			$('#${stuff }_description').textbox({
				multiline:true,
				width: '490px', 
				height: '120px'
			});
			$('#${stuff }_description').textbox('textbox').attr('maxlength', 1000);
			// 異動說明
			if ($('#${stuff }_updatedDescription_vo').length > 0) {
				$('#${stuff }_updatedDescription_vo').textbox({
					multiline:true,
					width: '490px', 
					height: '120px'
				});
				$('#${stuff }_updatedDescription_vo').textbox('textbox').attr('maxlength', 200);
			}
			// 到場次數
			$('#${stuff }_attendanceTimes_vo').textbox();
			// DTID
			if ($('#${stuff }_showDtid').length > 0) {
				$('#${stuff }_showDtid').textbox();
			}
			// 案件筆數
			if ($('#caseNumber').length > 0) {
				$('#caseNumber').numberbox();
				$('#caseNumber').textbox('textbox').attr('maxlength', 3);
			}
		}
	
	
		/*
		根據選擇的DTID，將查詢結果賦值到案件附加資料
		result：案件資料
		*/
		function loadCaseAdditionInfoElement(result) {
			//將數據賦值到各欄位
			if(('${caseCategory }' == '${caseCategoryAttr.MERGE.code}')
						|| ('${caseCategory }'== '${caseCategoryAttr.UPDATE.code}')){
				$("#${stuff }_updatedDescription_vo").textbox("setValue", "");
				$("#${stuff }_updatedDescription").val();
			}
			$("#oldCaseId").val(result==null?"":result.caseId);
			$("#${stuff }_showDtid").textbox("setValue", result==null?"":result.dtid);
		}

		/*
		下載附件資料
		id：附件資料ID
		isHistory：是否查歷史表
		*/
		function downloadCaseFile(id, isHistory){
			$("#errorMsg").html("");
			//判斷該文件是否存在
			ajaxService.checkCaseFile(id, isHistory, function(data){
				if (data) {
					//下載文件
					createSubmitForm("${contextPathAddition}/caseHandle.do","actionId=<%=IAtomsConstants.ACTION_DOWNLOAD%>&attFileId=" + id + "&isHistory=" + isHistory + "&caseCategory=" + '${caseCategory}',"post");
				} else {
					$("#errorMsg").html("文件不存在");
				}
			});
		}

		/*
		刪除附件資料
		*/
		 function deleteCaseFile(obj,fileId) {
			$("#errorMsg").html("");
		 	var deleteFile = $(obj).parent();
		 	deleteFile.hide();
		 	if ($("#deleteFileId").val() == "") {
		 		$("#deleteFileId").val(fileId);
		 	} else {
		 		$("#deleteFileId").val($("#deleteFileId").val() + "," + fileId);
		 	}
		 }
		
		
		/*
		點選單筆按鈕，輸入欄位制空且變為不可編輯狀態
		*/
		function checkComplex(obj) {
			if(obj.checked == true) {
				$("#caseNumber").textbox("setValue","");
				$("#caseNumber").textbox("disable");
				$("#caseNumber").textbox({
					required:false,
				});
			}
		}
		
		/*
		點選複製多筆按鈕，輸入欄位可變己且必填
		*/
		function checkComplexNum(obj) {
			if (obj.checked == true) {
				$("#caseNumber").textbox("enable");
				$("#caseNumber").textbox({
					required:true,missingMessage:'請輸入案件筆數',
					validType:'number[\'複製筆數限正整數\']'
				});
				textBoxDefaultSetting($('#caseNumber'));
			}
		}
		
		/*
		拿到案件筆數及流程控制部分的值
		*/
		function getCaseProcedureParams(){
			var caseProcedureParams = {};
			// 裝機
			if(${caseCategoryAttr.INSTALL.code eq caseCategory }){
				caseProcedureParams.isSingle = $("#complex").prop("checked");
				caseProcedureParams.caseNumber =  $("#caseNumber").textbox('getValue');
			// 其他
			} else {
				caseProcedureParams.isSingle = true;
			}
			return caseProcedureParams;
		}
		/*
		拿到案件派工TMS參數值
		*/
		function getTmsParam(){
			var isTmsParam;
			var isTmsObject = $("#isTms");
			// 處理isTms欄位值
			if(isTmsObject.length > 0){
				if(isTmsObject.prop("checked")){
					isTmsParam = 'Y';
				} else {
					isTmsParam = 'N';
				}
			}
			return isTmsParam;
		}
		/*
		* 裝機案件類別驗證dtid號碼是否夠用
		customerId：客戶編號
		edcType：刷卡機型
		*/
		function dtidValidate(customerId, edcType){
			javascript:dwr.engine.setAsync(false);
			var flag = false;
			var caseNumber;
			var isSingle;
			if(isEmpty('${srmCaseHandleInfoDTO.caseId }')){
				caseNumber = $("#caseNumber").textbox('getValue');
				isSingle = $("#complex").prop("checked");
			}
			// 裝機案件類別驗證dtid號碼
			ajaxService.checkDtidNumber(caseNumber, customerId, edcType, isSingle, function(data){
				if(data.success){
					flag = true;
				} else {
					$("#caseDialogMsg").text("客戶："+ data.customerName +"，機型："+ data.edcTypeName +"無可使用之DTID，請于【DTID號碼管理】設定");
					handleScrollTop('createDlg','caseDialogMsg');
					handleScrollTop('editDlg','caseDialogMsg');
				}
			});
			javascript:dwr.engine.setAsync(true);
			return flag;
		}
		/**
		* 匯入返回處理
		*/
		function showMessage(id, fileName, response, maybeXhrOrXdr) {
			//保存數據時，在上傳文件前驗證SESSION是否過期
			if (maybeXhrOrXdr) {
				if (!sessionTimeOut(maybeXhrOrXdr)) {
					return false;
				}
			}
		}
	</script>