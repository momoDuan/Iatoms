<%--
	案件資訊元件
	author：carrieDuan 
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/common/taglibs.jsp"%>
<%@ page import="cafe.core.util.StringUtils"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO"%>
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$CASE_CATEGORY" var="caseCategoryAttr" />
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants" var="iatomsContantsAttr" />
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$CASE_STATUS" var="caseStatusAttr" />
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$CASE_ACTION" var="caseActionAttr" />
<%--
	案件formDTO 需傳入isCustomerService-是否為客服，isCustomer--是否為客戶，
	處理方式有默認值，則需傳入defaultProcessType-處理方式默認值
 --%>
<tiles:useAttribute id="caseManagerFormDTO" name="caseManagerFormDTO" classname="com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO"/>
<%-- 客戶下拉列表 List<Parameter> --%>
<tiles:useAttribute id="customers" name="customers" classname="java.util.List" ignore="true"/>
<%-- 案件類型下拉列表 List<Parameter> --%>
<tiles:useAttribute id="ticketModes" name="ticketModes" classname="java.util.List"/>
<%-- 裝機類型下拉列表 List<Parameter> --%>
<tiles:useAttribute id="installTypes" name="installTypes" classname="java.util.List" ignore="true"/>
<%-- 處理方式下拉列表 List<Parameter> --%>
<tiles:useAttribute id="handledTypes" name="handledTypes" classname="java.util.List"/>
<%-- 拆機類型下拉列表 List<Parameter> --%>
<tiles:useAttribute id="uninstallTypes" name="uninstallTypes" classname="java.util.List" ignore="true"/>
<%-- 報修原因下拉列表 List<Parameter> --%>
<tiles:useAttribute id="repairReasons" name="repairReasons" classname="java.util.List" ignore="true"/>
<%-- 報修原因(台新)下拉列表 List<Parameter> --%>
<tiles:useAttribute id="repairReasonTaiXins" name="repairReasonTaiXins" classname="java.util.List" ignore="true"/>
<%-- 專案列表 List<Parameter> --%>
<tiles:useAttribute id="isVipList" name="isVipList" classname="java.util.List" ignore="true"/>
<%-- 所有維護廠商下拉列表 List<Parameter> --%>
<tiles:useAttribute id="allVendorList" name="allVendorList" classname="java.util.List" ignore="true"/>
<%-- 案件處理DTO SrmCaseHandleInfoDTO --%>
<tiles:useAttribute id="caseHandleInfoDTO" name="caseHandleInfoDTO" classname="com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO" ignore="true" />

<%-- 客戶聯動合約處理 --%>
<tiles:useAttribute id="editContractList" name="editContractList" classname="java.util.List" ignore="true"/>
<%-- 合約維護廠商處理 --%>
<tiles:useAttribute id="editVendors" name="editVendors" classname="java.util.List" ignore="true"/>
<%-- 維護廠商部門處理 --%>
<tiles:useAttribute id="editDeptList" name="editDeptList" classname="java.util.List" ignore="true"/>
<%-- 客戶權限下第一筆有設備的合約 --%>
<tiles:useAttribute id="editContractId" name="editContractId" classname="java.lang.String" ignore="true"/>
<%-- 客戶權限下第一筆有設備的合約 --%>
<tiles:useAttribute id="contextPathCaseInfo" name="contextPathCaseInfo" classname="java.lang.String" ignore="true"/>
<%
	if (caseHandleInfoDTO == null) {
		caseHandleInfoDTO = new SrmCaseHandleInfoDTO();
	}
	//更新時間
	String updatedDateString = caseHandleInfoDTO.getUpdatedDate() != null ? String.valueOf(caseHandleInfoDTO.getUpdatedDate().getTime()) : String.valueOf("");
%>
<c:set var="caseCategory" value="<%=caseManagerFormDTO.getCaseCategory() %>" scope="page"></c:set>
<c:set var="handledTypeValue" value="<%=caseManagerFormDTO.getDefaultProcessType() %>" scope="page"></c:set>
<c:set var="updatedDateString" value="<%=updatedDateString%>" scope="page"></c:set>
<c:set var="gpCustomerId" value="<%=caseManagerFormDTO.getGpCustomerId() %>" scope="page"></c:set>
<c:set var="tsbCustomerId" value="<%=caseManagerFormDTO.getTsbCustomerId() %>" scope="page"></c:set>
<c:set var="stuff" value="case" scope="page"></c:set>
<div id="process">
	<table style="width: 100%">
		<tr>
			<td width="10%">案件編號:</td>
			<c:choose>
				<c:when test="${empty caseHandleInfoDTO.caseId}">
					<td width="25%">(儲存後由系統產生)</td>
				</c:when>
				<c:otherwise>
					<td width="25%">${caseHandleInfoDTO.caseId }</td>
					<input type="hidden"
						value="<c:out value='${caseHandleInfoDTO.caseId }'/>"
						id="hideCaseId" />
					<input type="hidden" value="${updatedDateString}"
						id="hideUpdateDate" />
					<input type="hidden" value="" id="isCheckDtidFlag" />
				</c:otherwise>
			</c:choose>
			<td width="12%"><label id="requirementNoLabel">需求單號:</label></td>
			<td width="18%"><input id="${stuff }_requirementNo"
				name="${stuff }_requirementNo"
				value="<c:out value='${caseHandleInfoDTO.requirementNo }'/>">
			</td>

			<td width="12%">案件類別:</td>
			<td width="23%"><c:choose>
					<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory }">裝機</c:when>
					<c:when test="${caseCategoryAttr.MERGE.code eq caseCategory }">倂機	</c:when>
					<c:when test="${caseCategoryAttr.UPDATE.code eq caseCategory }">異動	</c:when>
					<c:when test="${caseCategoryAttr.UNINSTALL.code eq caseCategory }">拆機	</c:when>
					<c:when test="${caseCategoryAttr.CHECK.code eq caseCategory }">查核	</c:when>
					<c:when test="${caseCategoryAttr.PROJECT.code eq caseCategory }">專案</c:when>
					<c:when test="${caseCategoryAttr.REPAIR.code eq caseCategory }">報修</c:when>
					<c:when test="${caseCategoryAttr.OTHER.code eq caseCategory }">其他</c:when>
				</c:choose></td>
		</tr>
		<input id="isGpCustomer" name="isGpCustomer" value="N" type="hidden" />

		<input id="customerId" name="customerId"
			value="${not empty caseHandleInfoDTO.caseId ? caseHandleInfoDTO.customerId : (caseManagerFormDTO.isVendorAttribute == false && (caseManagerFormDTO.isCustomerAttribute == true || caseManagerFormDTO.isCustomerVendorAttribute == true)? empty customers?'':customers[0].value:'')}"
			type="hidden" />
		<c:choose>
			<c:when
				test="${caseCategoryAttr.INSTALL.code eq caseCategory}">
				<tr>
					<td>客戶:<span class="red">*</span>
					</td>
					<td><cafe:droplisttag id="${stuff }_customer"
							name="${stuff }_customer" hasBlankValue="true" blankName="請選擇"
							result="${customers }"
							selectedValue="${not empty caseHandleInfoDTO.caseId ? caseHandleInfoDTO.customerId : (caseManagerFormDTO.isVendorAttribute == false && (caseManagerFormDTO.isCustomerAttribute == true || caseManagerFormDTO.isCustomerVendorAttribute == true)?empty customers?'':customers[0].value:'')}"></cafe:droplisttag>
					</td>
					<td>合約編號:<span class="red">*</span></td>
					<td><cafe:droplisttag id="${stuff }_contractId"
							name="${stuff }_contractId"
							selectedValue="${empty caseHandleInfoDTO.caseId ? editContractId : caseHandleInfoDTO.contractId }"
							result="${editContractList }" blankName="請選擇"
							hasBlankValue="true"></cafe:droplisttag></td>
				</tr>
			</c:when>
			<c:when
				test="${caseCategoryAttr.OTHER.code eq caseCategory}">
				<tr>
					<td>DTID: 
					</td>
					<td><input id="${stuff }_dtid" name="${stuff }_dtid"
						value="<c:out value='${caseHandleInfoDTO.dtid }'/>"> <a
						href="javascript:void(0)" data-options="iconCls:'icon-ok'"
						id="getCaseInfoBtn" onclick="getCaseManagerInfo('');"></a> <a
						href="javascript:void(0)" data-options="iconCls:'icon-search'"
						id="queryCaseInfoBtn" onclick="queryDTIDInfo();">查詢</a> <input
						type="hidden" id="changeDtid" name="changeDtid"
						value="<c:out value='${caseHandleInfoDTO.dtid }'/>"></td>
						<input type="hidden" id="changeCmsCase" name="changeCmsCase" value="${caseHandleInfoDTO.cmsCase }"></td>
					<td>客戶: <span class="red">*</span>
					</td>
					<td><cafe:droplisttag id="${stuff }_customer"
							name="${stuff }_customer" hasBlankValue="true" blankName="請選擇"
							result="${customers }"
							selectedValue="${not empty caseHandleInfoDTO.caseId ? caseHandleInfoDTO.customerId : (caseManagerFormDTO.isVendorAttribute == false && (caseManagerFormDTO.isCustomerAttribute == true || caseManagerFormDTO.isCustomerVendorAttribute == true)?empty customers?'':customers[0].value:'')}"></cafe:droplisttag>
					</td>
					<td>合約編號:<span class="red">*</span></td>
					<td><cafe:droplisttag id="${stuff }_contractId"
							name="${stuff }_contractId"
							selectedValue="${empty caseHandleInfoDTO.caseId ? editContractId : caseHandleInfoDTO.contractId }"
							result="${editContractList }" blankName="請選擇"
							hasBlankValue="true"></cafe:droplisttag></td>
				</tr>
			</c:when>
			<c:otherwise>
				<tr>
					<td>DTID:<span class="red">*</span>
					</td>
					<td><input id="${stuff }_dtid" name="${stuff }_dtid"
						value="<c:out value='${caseHandleInfoDTO.dtid }'/>"> <a
						href="javascript:void(0)" data-options="iconCls:'icon-ok'"
						id="getCaseInfoBtn" onclick="getCaseManagerInfo('');"></a> <a
						href="javascript:void(0)" data-options="iconCls:'icon-search'"
						id="queryCaseInfoBtn" onclick="queryDTIDInfo();">查詢</a> <input
						type="hidden" id="changeDtid" name="changeDtid"
						value="<c:out value='${caseHandleInfoDTO.dtid }'/>"></td>
						<input type="hidden" id="changeCmsCase" name="changeCmsCase" value=""></td>
					<td>客戶:</td>
					<td><input id="${stuff }_customerName"
						name="${stuff }_customerName"
						value="<c:out value='${caseHandleInfoDTO.customerName }'/>"
						disabled /> <input type="hidden" id="${stuff }_customerId"
						name="${stuff }_customerId"
						value="<c:out value='${caseHandleInfoDTO.customerId }'/>">
					</td>
					<td>合約編號:</td>
					<td><input id="${stuff }_contractCode"
						name="${stuff }_contractCode"
						value="<c:out value='${caseHandleInfoDTO.contractCode }'/>"
						disabled /> <input id="${stuff }_contractId"
						name="${stuff }_contractId"
						value="<c:out value='${caseHandleInfoDTO.contractId }'/>"
						type="hidden" /></td>
				</tr>
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${caseCategoryAttr.PROJECT.code ne caseCategory }">
				<tr>
					<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory }">
						<td>裝機類型:<span class="red">*</span>
						</td>
						<td><cafe:droplisttag id="${stuff }_installType"
								name="${stuff }_installType"
								selectedValue="${empty caseHandleInfoDTO.caseId ? '1':caseHandleInfoDTO.installType}"
								result="${installTypes }" blankName="請選擇" hasBlankValue="true"></cafe:droplisttag>
						</td>
					</c:if>
					<c:if test="${caseCategoryAttr.UPDATE.code eq caseCategory }">
						<td>專案:</td>
						<td><c:choose>
								<c:when
									test="${not empty caseHandleInfoDTO.caseId and caseHandleInfoDTO.isProject eq 'Y'}">
									<cafe:checklistTag name="${stuff }_isProject"
										id="${stuff }_isProject" result="${isVipList}" type="radio"
										checkedValues='<%=StringUtils.toList("Y",",") %>' />
								</c:when>
								<c:otherwise>
									<cafe:checklistTag name="${stuff }_isProject"
										id="${stuff }_isProject" result="${isVipList}" type="radio"
										checkedValues='<%=StringUtils.toList("N",",") %>' />
								</c:otherwise>
							</c:choose></td>
					</c:if>
					<c:if test="${caseCategoryAttr.UNINSTALL.code eq caseCategory  }">
						<td>拆機類型: <span class="red">*</span></td>
						<td><cafe:droplisttag id="${stuff }_uninstallType"
								name="${stuff }_uninstallType"
								selectedValue="${empty caseHandleInfoDTO.caseId ? 'ARRIVE_UNINSTALL':caseHandleInfoDTO.uninstallType}"
								result="${uninstallTypes }" blankName="請選擇" hasBlankValue="true"></cafe:droplisttag>
						</td>
					</c:if>
					<c:if test="${caseCategoryAttr.REPAIR.code eq caseCategory }">
						<td>報修原因:</td>
						<td>
							<c:choose>
								<c:when test="${not empty caseHandleInfoDTO.caseId and caseHandleInfoDTO.companyCode eq iatomsContantsAttr.PARAM_TSB_EDC}">
									<cafe:droplisttag id="${stuff }_repairReason"
									name="${stuff }_repairReason" result="${repairReasonTaiXins }"
									selectedValue="${caseHandleInfoDTO.repairReason }"
									blankName="請選擇" hasBlankValue="true"></cafe:droplisttag>
								</c:when>
								<c:otherwise>
									<cafe:droplisttag id="${stuff }_repairReason"
									name="${stuff }_repairReason" result="${repairReasons }"
									selectedValue="${caseHandleInfoDTO.repairReason }"
									blankName="請選擇" hasBlankValue="true"></cafe:droplisttag>
								</c:otherwise>
							</c:choose>
						</td>
					</c:if>
					<%-- <c:choose>
						<c:when
							test="${caseCategoryAttr.INSTALL.code ne caseCategory && caseCategoryAttr.OTHER.code ne caseCategory}">
							<td>維護廠商:</td>
							<td><cafe:droplisttag id="${stuff }_companyId"
									name="${stuff }_companyId"
									result="${empty caseHandleInfoDTO.caseId or (not empty caseHandleInfoDTO.caseId and caseHandleInfoDTO.caseStatus eq caseStatusAttr.WAIT_DISPATCH.code) ? editVendors : allVendorList }"
									blankName="請選擇" hasBlankValue="true"
									selectedValue="${caseHandleInfoDTO.companyId }"></cafe:droplisttag>
							</td>
						</c:when>
						<c:otherwise> --%>
							<td>維護廠商:<span class="red">*</span></td>
							<td><cafe:droplisttag id="${stuff }_companyId"
									name="${stuff }_companyId"
									result="${empty caseHandleInfoDTO.caseId or (not empty caseHandleInfoDTO.caseId and caseHandleInfoDTO.caseStatus eq caseStatusAttr.WAIT_DISPATCH.code) ? editVendors : allVendorList }"
									blankName="請選擇" hasBlankValue="true"
									selectedValue="${caseHandleInfoDTO.cmsCase eq 'Y'? caseHandleInfoDTO.companyId eq '10000000-01'? caseHandleInfoDTO.companyId :'' :caseHandleInfoDTO.companyId}"></cafe:droplisttag>
							</td>
						<%-- </c:otherwise>
					</c:choose> --%>
					<td>維護部門:<span class="red">*</span></td>
					<td><cafe:droplisttag id="${stuff }_departmentId"
							name="${stuff }_departmentId" blankName="請選擇"
							hasBlankValue="true"
							selectedValue="${caseHandleInfoDTO.departmentId }"
							result="${editDeptList }"></cafe:droplisttag></td>
					<c:if test="${caseCategoryAttr.MERGE.code eq caseCategory }">
						<td>是否同裝機作業:</td>
						<td><cafe:droplisttag id="${stuff }_sameInstalled"
								name="${stuff }_sameInstalled" hasBlankValue="true"
								result="${isVipList }"
								selectedValue="${empty caseHandleInfoDTO.caseId ? 'N':caseHandleInfoDTO.sameInstalled}"
								blankName="請選擇"></cafe:droplisttag></td>
					</c:if>
				</tr>
			</c:when>
			<c:otherwise>
				<tr>
					<td>專案代碼:</td>
					<td><input id="${stuff }_projectCode"
						name="${stuff }_projectCode"
						value="<c:out value='${caseHandleInfoDTO.projectCode }'/>" /></td>
					<td>專案名稱:</td>
					<td><input id="${stuff }_projectName"
						name="${stuff }_projectName"
						value="<c:out value='${caseHandleInfoDTO.projectName }'/>" /></td>
					<td>維護廠商:<span class="red">*</span></td>
					<td><cafe:droplisttag id="${stuff }_companyId"
							name="${stuff }_companyId"
							result="${empty caseHandleInfoDTO.caseId or (not empty caseHandleInfoDTO.caseId and caseHandleInfoDTO.caseStatus eq caseStatusAttr.WAIT_DISPATCH.code ) ? editVendors : allVendorList }"
							blankName="請選擇" hasBlankValue="true"
							selectedValue="${caseHandleInfoDTO.cmsCase eq 'Y'? caseHandleInfoDTO.companyId eq '10000000-01'? caseHandleInfoDTO.companyId :'' :caseHandleInfoDTO.companyId}"></cafe:droplisttag>
					</td>
				</tr>
			</c:otherwise>
		</c:choose>
		<tr>
			<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory }">
				<td>專案:<span class="red">*</span></td>
				<td><c:choose>
						<c:when
							test="${not empty caseHandleInfoDTO.caseId and caseHandleInfoDTO.isProject eq 'Y'}">
							<cafe:checklistTag name="${stuff }_isProject"
								id="${stuff }_isProject" result="${isVipList}" type="radio"
								checkedValues='<%=StringUtils.toList("Y",",") %>' />
						</c:when>
						<c:otherwise>
							<cafe:checklistTag name="${stuff }_isProject"
								id="${stuff }_isProject" result="${isVipList}" type="radio"
								checkedValues='<%=StringUtils.toList("N",",") %>' />
						</c:otherwise>
					</c:choose></td>
			</c:if>
			<c:if test="${caseCategoryAttr.PROJECT.code eq caseCategory }">
				<td>維護部門:<span class="red">*</span></td>
				<td><cafe:droplisttag id="${stuff }_departmentId"
						name="${stuff }_departmentId" blankName="請選擇" hasBlankValue="true"
						result="${editDeptList }"
						selectedValue="${caseHandleInfoDTO.departmentId }"></cafe:droplisttag>
				</td>
			</c:if>
			<td>案件類型:<span class="red">*</span></td>
			<td><cafe:droplisttag id="${stuff }_caseType"
					name="${stuff }_caseType" result="${ticketModes }"
					selectedValue="${empty caseHandleInfoDTO.caseId ? 'COMMON':caseHandleInfoDTO.caseType}"
					blankName="請選擇" hasBlankValue="true"></cafe:droplisttag></td>
			<td><label id="${stuff }_expectedCompletionDateLable">預計完成日:</label>
			</td>
			<td><input id="${stuff }_expectedCompletionDate"
				name="${stuff }_expectedCompletionDate"
				value="${caseHandleInfoDTO.caseType eq 'APPOINTMENT' ? caseHandleInfoDTO.expectedCompletionDate : caseHandleInfoDTO.acceptableFinishDate}" />
			</td>
			<%-- <c:if test="${caseCategoryAttr.MERGE.code eq caseCategory or caseCategoryAttr.UNINSTALL.code eq caseCategory or caseCategoryAttr.CHECK.code eq caseCategory or caseCategoryAttr.REPAIR.code eq caseCategory}">
					<td id="completeDateLabelTd" style="display:none;">
						<label>實際完修時間:</label>
					</td>
					<td id="completeDateTd" style="display:none;" >
						<input id="${stuff }_completeDate" name="${stuff }_completeDate" value="${caseHandleInfoDTO.completeDate }"/>
					</td>
				</c:if> --%>
			<c:if test="${caseCategoryAttr.UPDATE.code eq caseCategory }">
				<td>是否同裝機作業:</td>
				<td><cafe:droplisttag id="${stuff }_sameInstalled"
						name="${stuff }_sameInstalled" hasBlankValue="true"
						result="${isVipList }"
						selectedValue="${empty caseHandleInfoDTO.caseId ? 'N':caseHandleInfoDTO.sameInstalled}"
						blankName="請選擇"></cafe:droplisttag></td>
			</c:if>
		</tr>
		<%-- <c:if test="${caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory or caseCategoryAttr.INSTALL.code eq caseCategory}">
				<tr>
					<td id="completeDateLabelTd" style="display:none;">
						<label>實際完修時間:</label>
					</td>
					<td id="completeDateTd" style="display:none;">
						<input id="${stuff }_completeDate" name="${stuff }_completeDate" value="${caseHandleInfoDTO.completeDate }"/>
					</td>
				</tr>
			</c:if> --%>
	</table>
	<c:if test="${caseCategoryAttr.INSTALL.code ne caseCategory }">
		<input type="hidden" value="${caseHandleInfoDTO.installType }"
			name="${stuff }_installType" id="${stuff }_installType">
	</c:if>
	<input type="hidden" value="${caseHandleInfoDTO.isUpdateAsset }"
		name="${stuff }_isUpdateAsset" id="${stuff }_isUpdateAsset"> <input
		type="hidden" value="" id="${stuff }_isUpdate">
	<div id="caseInfoDlgElement"></div>
</div>
<script type="text/javascript">
	// 是否是編輯頁面標志位
	$(function () {
		// 加載案件資訊區塊
		loadingCaseInformation('${caseManagerFormDTO.caseCategory}');
	});
	
	/*
	* 加載案件資訊區塊
	*/
	function loadingCaseInformation(caseCategory){
		// panel
		$("#process").panel({title:'案件資訊',width:'99%'});
		<c:choose>
			<c:when test="${((not empty caseHandleInfoDTO.caseId and caseHandleInfoDTO.customerId eq gpCustomerId and (caseManagerFormDTO.caseCategory eq caseCategoryAttr.INSTALL.code or caseManagerFormDTO.caseCategory eq caseCategoryAttr.MERGE.code or caseManagerFormDTO.caseCategory eq caseCategoryAttr.UPDATE.code or caseManagerFormDTO.caseCategory eq caseCategoryAttr.UNINSTALL.code)) or (logonUser.companyId eq gpCustomerId and caseManagerFormDTO.caseCategory eq caseCategoryAttr.INSTALL.code)) or (not empty caseHandleInfoDTO.caseId and caseHandleInfoDTO.customerId eq tsbCustomerId and (caseManagerFormDTO.caseCategory eq caseCategoryAttr.INSTALL.code or caseManagerFormDTO.caseCategory eq caseCategoryAttr.UPDATE.code or caseManagerFormDTO.caseCategory eq caseCategoryAttr.MERGE.code))}">
		 		// 需求單號
				$('#${stuff }_requirementNo').textbox({
					required:true,
					missingMessage:"請輸入需求單號",
					validType:'maxLength[17]'
				});
				$('#${stuff }_requirementNo').textbox('textbox').attr('maxlength', 17);
				$('#${stuff }_requirementNo').addClass("easyui-textbox");
				// 標記
				$("#requirementNoLabel").html("需求單號:<span class=\"red\">*</span>");
			</c:when>
			<c:otherwise>
		 		// 需求單號
				$('#${stuff }_requirementNo').textbox({
					validType:'maxLength[17]'
				});
				$('#${stuff }_requirementNo').textbox('textbox').attr('maxlength', 17);
			</c:otherwise>
		</c:choose>
		//Task #3583  客戶廠商  
		<c:if test="${(empty caseHandleInfoDTO.caseId and caseManagerFormDTO.isVendorAttribute == false && (caseManagerFormDTO.isCustomerAttribute == true || caseManagerFormDTO.isCustomerVendorAttribute == true)) and (((logonUser.companyId eq gpCustomerId and (caseCategory eq caseCategoryAttr.INSTALL.code or caseCategory eq caseCategoryAttr.MERGE.code or caseCategory eq caseCategoryAttr.UPDATE.code or caseCategory eq caseCategoryAttr.UNINSTALL.code)) or (logonUser.companyId eq gpCustomerId and caseCategory eq caseCategoryAttr.INSTALL.code)) or (logonUser.companyId eq tsbCustomerId and (caseCategory eq caseCategoryAttr.INSTALL.code or caseCategory eq caseCategoryAttr.UPDATE.code or caseCategory eq caseCategoryAttr.MERGE.code)))}">
			// 需求單號
			$('#${stuff }_requirementNo').textbox({
				required:true,
				missingMessage:"請輸入需求單號",
				validType:'maxLength[17]'
			});
			$('#${stuff }_requirementNo').textbox('textbox').attr('maxlength', 17);
			$('#${stuff }_requirementNo').addClass("easyui-textbox");
			// 標記
			$("#requirementNoLabel").html("需求單號:<span class=\"red\">*</span>");
		</c:if>
		// 裝機
		if(caseCategory == '${caseCategoryAttr.INSTALL.code}'){
			// 客戶
			if ($('#${stuff }_customer').length > 0) {
				$('#${stuff }_customer').combobox({
					editable:false, 
					required:true,
					valueField:'value',
					textField:'name',
					width:'170px',
					onChange:function(newValue,oldValue){customerOnChange(newValue,oldValue);},
					validType:'requiredDropList',
					disabled:"${caseManagerFormDTO.isVendorAttribute == false && (caseManagerFormDTO.isCustomerAttribute == true || caseManagerFormDTO.isCustomerVendorAttribute == true)?'disabled=true':'' }",
					invalidMessage:"請輸入客戶" 
				});
				$('#${stuff }_customer').addClass("easyui-combobox");
			}
			// 合約編號
			if ($('#${stuff }_contractId').length > 0) {
				$('#${stuff }_contractId').combobox({
					editable:false, 
					required:true,
					valueField:'value',
					textField:'name',
					width:'170px',
					onChange:function(newValue,oldValue){contractOnChange(newValue,oldValue);},
					validType:'requiredDropList',
					invalidMessage:"請輸入合約編號" 
				});
				$('#${stuff }_contractId').addClass("easyui-combobox");
			}
			// 維護廠商
			if ($('#${stuff }_companyId').length > 0) {
				$('#${stuff }_companyId').combobox({
					editable:false, 
					required:true,
					valueField:'value',
					textField:'name',
					width:'170px',
					onChange:function(newValue,oldValue){getDeptList(newValue,oldValue);},
					validType:'requiredDropList',
					disabled:"${caseHandleInfoDTO.installType eq '4'?'disabled':''}",
					invalidMessage:"請輸入維護廠商" 
				});
				$('#${stuff }_companyId').addClass("easyui-combobox");
			}
			if(caseCategory == '${caseCategoryAttr.INSTALL.code}'){
				// 裝機類型
				if ($('#${stuff }_installType').length > 0) {
					$('#${stuff }_installType').combobox({
						editable:false, 
						required:true,
						valueField:'value',
						textField:'name',
						width:'170px',
						onChange:function(newValue,oldValue){installTypeOnChange(newValue,oldValue);}, 
						panelHeight:'auto',
						validType:'requiredDropList',
						invalidMessage:"請輸入裝機類型" 
					});
					$('#${stuff }_installType').addClass("easyui-combobox");
				}
			}
		}else if(caseCategory == '${caseCategoryAttr.OTHER.code}'){// #3392 其他
			// 客戶
			if ($('#${stuff }_customer').length > 0) {
				$('#${stuff }_customer').combobox({
					editable:false, 
					required:true,
					valueField:'value',
					textField:'name',
					width:'170px',
					onChange:function(newValue,oldValue){customerOnChange(newValue,oldValue);},
					validType:'requiredDropList',
					disabled:"${caseManagerFormDTO.isVendorAttribute == false && (caseManagerFormDTO.isCustomerAttribute == true || caseManagerFormDTO.isCustomerVendorAttribute == true)?'disabled=true':'' }",
					invalidMessage:"請輸入客戶" 
				});
				$('#${stuff }_customer').addClass("easyui-combobox");
			}
			// DTID
			if ($('#${stuff }_dtid').length > 0) {
				$('#${stuff }_dtid').textbox({
					required:false,
					missingMessage:"請輸入DTID",
					validType:'maxLength[8]'
				});
				$('#${stuff }_dtid').textbox('textbox').attr('maxlength', 8);
				
				$('#getCaseInfoBtn').linkbutton({iconCls:'icon-ok'});
				$('#queryCaseInfoBtn').linkbutton({iconCls:'icon-search'});
			}
			// 合約編號
			if ($('#${stuff }_contractId').length > 0) {
				$('#${stuff }_contractId').combobox({
					editable:false, 
					required:true,
					valueField:'value',
					textField:'name',
					width:'170px',
					onChange:function(newValue,oldValue){contractOnChange(newValue,oldValue);},
					validType:'requiredDropList',
					invalidMessage:"請輸入合約編號" 
				});
				$('#${stuff }_contractId').addClass("easyui-combobox");
			}
			// 維護廠商
			if ($('#${stuff }_companyId').length > 0) {
				$('#${stuff }_companyId').combobox({
					editable:false, 
					required:true,
					valueField:'value',
					textField:'name',
					width:'170px',
					onChange:function(newValue,oldValue){getDeptList(newValue,oldValue);},
					validType:'requiredDropList',
					disabled:"${caseHandleInfoDTO.cmsCase eq 'Y' and caseHandleInfoDTO.companyId eq '10000000-01'?'disabled':''}",
					invalidMessage:"請輸入維護廠商" 
				});
				$('#${stuff }_companyId').addClass("easyui-combobox");
			}
		}else {// 非裝機
			// 客戶
			if ($('#${stuff }_customerName').length > 0) {
				$('#${stuff }_customerName').textbox();
			}
			// DTID
			if ($('#${stuff }_dtid').length > 0) {
				$('#${stuff }_dtid').textbox({
					required:true,
					missingMessage:"請輸入DTID",
					validType:'maxLength[8]'
				});
				$('#${stuff }_dtid').textbox('textbox').attr('maxlength', 8);
				
				$('#getCaseInfoBtn').linkbutton({iconCls:'icon-ok'});
				$('#queryCaseInfoBtn').linkbutton({iconCls:'icon-search'});
			}
			// 合約編號
			if ($('#${stuff }_contractCode').length > 0) {
				$('#${stuff }_contractCode').textbox();
			}
			// 拆機類型
			if ($('#${stuff }_uninstallType').length > 0) {
				$('#${stuff }_uninstallType').combobox({
					editable:false, 
					required:true,
					valueField:'value',
					textField:'name',
					width:'170px',
					panelHeight:'auto',
					validType:'requiredDropList',
					invalidMessage:"請輸入拆機類型" 
				});
				$('#${stuff }_uninstallType').addClass("easyui-combobox");
			}
			// 報修原因
			/* if ($('#${stuff }_repairReason').length > 0) {
				$('#${stuff }_repairReason').combobox({
					editable:false, 
					required:true,
					valueField:'value',
					textField:'name',
					width:'170px',
					validType:'requiredDropList',
					invalidMessage:"請輸入報修原因" 
				});
			} */
			// Task #3044 報修件「報修原因」為必填 改為 非必填
			if ($('#${stuff }_repairReason').length > 0) {
				$('#${stuff }_repairReason').combobox({
					editable:false, 
					valueField:'value',
					textField:'name',
					width:'170px'
				});
				$('#${stuff }_repairReason').addClass("easyui-combobox");
			}
			// 維護廠商
			/* if ($('#${stuff }_companyName').length > 0) {
				$('#${stuff }_companyName').textbox();
			} */
			// Task #3028 併機 異動 拆機 查核 專案 報修，建案之維護廠商，DTID預設帶出後要能再調整
			if ($('#${stuff }_companyId').length > 0) {
				$('#${stuff }_companyId').combobox({
					editable:false, 
					valueField:'value',
					textField:'name',
					width:'170px',
					validType:'requiredDropList',
					invalidMessage:"請輸入維護廠商",
					disabled:"${caseHandleInfoDTO.cmsCase eq 'Y' and caseHandleInfoDTO.companyId eq '10000000-01'?'disabled':''}",
					onChange:function(newValue,oldValue){getDeptList(newValue,oldValue);}
				});
				$('#${stuff }_companyId').addClass("easyui-combobox");
			}
			// 是否同裝機作業
			if ($('#${stuff }_sameInstalled').length > 0) {
				$('#${stuff }_sameInstalled').combobox({
					editable:false, 
					valueField:'value',
					textField:'name',
					panelHeight:'auto',
					width:'170px'
				});
				$('#${stuff }_sameInstalled').addClass("easyui-combobox");
			}
			// 專案代碼
			if ($('#${stuff }_projectCode').length > 0) {
				$('#${stuff }_projectCode').textbox();
				$('#${stuff }_projectCode').textbox('textbox').attr('maxlength', 50);
			}
			// 專案名稱
			if ($('#${stuff }_projectName').length > 0) {
				$('#${stuff }_projectName').textbox();
				$('#${stuff }_projectName').textbox('textbox').attr('maxlength', 100);
			}
		}
		// 維護部門
		if ($('#${stuff }_departmentId').length > 0) {
			$('#${stuff }_departmentId').combobox({
				editable:false, 
				required:true,
				valueField:'value',
				textField:'name',
				width:'170px',
				validType:'requiredDropList',
				invalidMessage:"請輸入維護部門",
			});
			$('#${stuff }_departmentId').addClass("easyui-combobox");
		}
		// 案件類型
		$('#${stuff }_caseType').combobox({
			editable:false, 
			required:true,
			valueField:'value',
			textField:'name',
			width:'170px',
			panelHeight:'auto',
			validType:'requiredDropList',
			invalidMessage:"請輸入案件類型",
			onChange:function(newValue,oldValue){caseTypeOnChange(newValue,oldValue);}
		});
		$('#${stuff }_caseType').addClass("easyui-combobox");
		// 預計完成日
		$('#${stuff }_expectedCompletionDate').datebox({
			required:true,
			width:'170px',
			validType:'date',
			disabled:"${empty caseHandleInfoDTO.caseId ?'disabled':caseHandleInfoDTO.caseType eq iatomsContantsAttr.TICKET_MODE_APPOINTMENT ? '':'disabled' }",
			missingMessage:"請輸入預計完成日" 
		});
		$('#${stuff }_expectedCompletionDate').addClass("easyui-datebox");
		// 預計完成日
		if(${not empty caseHandleInfoDTO.caseId } && ${caseHandleInfoDTO.caseType eq iatomsContantsAttr.TICKET_MODE_APPOINTMENT }){
			$("#${stuff }_expectedCompletionDateLable").html("預計完成日:<span class=\"red\">*</span>");
		}
		// 實際完修時間
		if(caseCategory != '${caseCategoryAttr.OTHER.code}'){
			/* <c:if test="${(not empty caseHandleInfoDTO.caseId and (caseHandleInfoDTO.caseStatus eq caseStatusAttr.IMMEDIATE_CLOSE.code or caseHandleInfoDTO.caseStatus eq caseStatusAttr.CLOSED.code))}">
				// 客服
				var isCustomerService = false;
				// 客戶
				var isCustomer = false;
				// CR #2951 廠商客服
				if(${caseManagerFormDTO.isCustomerService }){
					isCustomerService = true;
				} else if(${caseManagerFormDTO.isVendorService } && ${!caseManagerFormDTO.isCustomerService }){
					// 建案廠商給客服公司不等於當前
					if('${caseHandleInfoDTO.vendorServiceCustomer }' == '${logonUser.companyId }'){
						isCustomerService = true;
					}
				} else if(${caseManagerFormDTO.isCustomer } && ${!caseManagerFormDTO.isCustomerService } && ${!caseManagerFormDTO.isVendorService }){
					isCustomer = true;
				}
				// 客服或客戶
				if(isCustomerService || isCustomer){
					$("#completeDateLabelTd").show();
					$("#completeDateTd").show();
					$('#${stuff }_completeDate').datetimebox({
						required:true,
						width:'195px',
						validType:'dateTimeValid',
						missingMessage:"請輸入實際完修時間", 
						invalidMessage:"實際完修時間格式限YYYY/MM/DD HH:mm:ss"
					});
					$('#${stuff }_completeDate').addClass("easyui-datetimebox");
					$('#${stuff }_completeDate').textbox('textbox').attr('maxlength', 19);
				}
			</c:if> */
		}
		$('#${stuff }_expectedCompletionDate').textbox('textbox').attr('maxlength', 10);
	}	
	
	
	/*
	客戶異動事件
	*/
	function customerOnChange(newValue, oldValue){
		$("#caseDialogMsg").html("");
		$("#customerId").val(newValue);
		$("#merchantId").val("");
		$("#${stuff }_merMid").textbox('setValue', "");
		$("#${stuff }_merchantName").textbox('setValue', "");
		if (newValue != "") {
			if(!isEmpty('${gpCustomerId}')){
				if(newValue == '${gpCustomerId}'){
					// Task #2683 環匯 需求單號 :裝機/異動/併機/拆機 為必填外 , 其餘無須必填
					<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.MERGE.code eq caseCategory or caseCategoryAttr.UNINSTALL.code eq caseCategory}">
						var requirementNoOptions = $("#${stuff }_requirementNo").textbox('options');
						requirementNoOptions.missingMessage = '請輸入需求單號';
						requirementNoOptions.required = true;
						$("#${stuff }_requirementNo").textbox(requirementNoOptions);
						// 限制欄位長度
						//textBoxDefaultSetting($("#${stuff }_requirementNo"));
						$('#${stuff }_requirementNo').textbox('textbox').attr('maxlength', 17);
						$('#${stuff }_requirementNo').addClass("easyui-textbox");
						// 標記
						$("#requirementNoLabel").html("需求單號:<span class=\"red\">*</span>");
					</c:if>
					
					// Task #2616 AO人員必填
					var aoNameOptions = $("#${stuff }_merAoName").textbox('options');
					aoNameOptions.missingMessage = '請輸入AO人員';
					aoNameOptions.required = true;
					aoNameOptions.disabled = false;
					aoNameOptions.readonly = true;
					$("#${stuff }_merAoName").textbox(aoNameOptions);
					//Task #3579 [環匯：裝機、異動、專案必填]
					<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
					var receiptTypeOptions = $("#${stuff }_receiptType").combobox('options');
						receiptTypeOptions.invalidMessage = '請輸入Receipt_type';
						receiptTypeOptions.required = true;
						receiptTypeOptions.validType='requiredDropList';
						$("#${stuff }_receiptType").combobox(receiptTypeOptions);
						$('#${stuff }_receiptType').addClass("easyui-combobox");
						// 標記
						$("#receiptTypeLabel").html("Receipt_type:<span class=\"red\">*</span>");
					</c:if>
					// 限制欄位長度
					textBoxDefaultSetting($("#${stuff }_merAoName"));
					// 標記
					$("#aoNameLabel").html("AO人員:<span class=\"red\">*</span>");
					
					$("#isGpCustomer").val('Y');
				} else {
					if(oldValue == '${gpCustomerId}'){
						// Task #2683 環匯 需求單號 :裝機/異動/併機/拆機 為必填外 , 其餘無須必填
						<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.MERGE.code eq caseCategory or caseCategoryAttr.UNINSTALL.code eq caseCategory}">
							var requirementNoOptions = $("#${stuff }_requirementNo").textbox('options');
							requirementNoOptions.missingMessage = '';
							requirementNoOptions.required = false;
							$("#${stuff }_requirementNo").textbox(requirementNoOptions);
							// 限制欄位長度
							//textBoxDefaultSetting($("#${stuff }_requirementNo"));
							$('#${stuff }_requirementNo').textbox('textbox').attr('maxlength', 17);
							// 標記
							$("#requirementNoLabel").html("需求單號:");
						</c:if>
						
						// Task #2616 AO人員必填
						var aoNameOptions = $("#${stuff }_merAoName").textbox('options');
						aoNameOptions.missingMessage = '';
						aoNameOptions.required = false;
						aoNameOptions.disabled = true;
						$("#${stuff }_merAoName").textbox(aoNameOptions);
						// 限制欄位長度
						textBoxDefaultSetting($("#${stuff }_merAoName"));
						// 標記
						$("#aoNameLabel").html("AO人員:");
						
						//Task #3579 [環匯：除裝機、異動、專案外，非必填]
						<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
							var receiptTypeOptions = $("#${stuff }_receiptType").combobox('options');
							receiptTypeOptions.invalidMessage = '';
							receiptTypeOptions.required = false;
							receiptTypeOptions.validType='';
							$("#${stuff }_receiptType").combobox(receiptTypeOptions);
							$('#${stuff }_receiptType').addClass("easyui-combobox");
							// 標記
							$("#receiptTypeLabel").html("Receipt_type:");
						</c:if>
						
						$("#isGpCustomer").val('N');
					}
				}
			}
			//Task #3335 若客戶=台新銀行，需求單號為必填欄位
			if(!isEmpty('${tsbCustomerId}')){
				if(newValue == '${tsbCustomerId}'){
					<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory}">
						var requirementNoOptions = $("#${stuff }_requirementNo").textbox('options');
						requirementNoOptions.missingMessage = '請輸入需求單號';
						requirementNoOptions.required = true;
						$("#${stuff }_requirementNo").textbox(requirementNoOptions);
						// 限制欄位長度
						//textBoxDefaultSetting($("#${stuff }_requirementNo"));
						$('#${stuff }_requirementNo').textbox('textbox').attr('maxlength', 17);
						$('#${stuff }_requirementNo').addClass("easyui-textbox");
						// 標記
						$("#requirementNoLabel").html("需求單號:<span class=\"red\">*</span>");
					</c:if>
				} else {
					if(oldValue == '${tsbCustomerId}' && (!isEmpty('${gpCustomerId}') && newValue!='${gpCustomerId}')){
						<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory}">
							var requirementNoOptions = $("#${stuff }_requirementNo").textbox('options');
							requirementNoOptions.missingMessage = '';
							requirementNoOptions.required = false;
							$("#${stuff }_requirementNo").textbox(requirementNoOptions);
							// 限制欄位長度
							//textBoxDefaultSetting($("#${stuff }_requirementNo"));
							$('#${stuff }_requirementNo').textbox('textbox').attr('maxlength', 17);
							// 標記
							$("#requirementNoLabel").html("需求單號:");
						</c:if>
					}
				}
			}
			var selectContract = "";
			//根據所選客戶獲取對應的合約列表
			var changeCmsCase = $("#changeCmsCase").val();
			javascript:dwr.engine.setAsync(false);
			if(${caseCategoryAttr.OTHER.code eq caseCategory}){
				$("#${stuff }_contractId").combobox("setValue","");
				$('#${stuff }_companyId').combobox({disabled:''});
				$('#${stuff }_companyId').combobox("setValue","");
				$('#${stuff }_companyId').combobox("loadData",initSelect(null));
			}
			ajaxService.getContractListByVendorId(newValue,function(data){
				$("#${stuff }_contractId").combobox("loadData",initSelect(data));
				if(!${caseCategoryAttr.OTHER.code eq caseCategory}){
					if (data.length != 0) {
						var value = data[1]["value"];
					//	$("#${stuff }_contractId").combobox("setValue", value);
						selectContract = value;
					} else {
					//	$("#${stuff }_contractId").combobox("setValue","");
					}
				}
			});	
			if(!${caseCategoryAttr.OTHER.code eq caseCategory}){
				ajaxService.getHaveEdcContract(newValue,function(returnValue){
					if(returnValue && returnValue != null){
					//	$("#${stuff }_contractId").combobox("setValue", returnValue);
						selectContract = returnValue;
					}
				});
				$("#${stuff }_contractId").combobox("setValue", selectContract);
			}
			javascript:dwr.engine.setAsync(true);
			if(!${caseCategoryAttr.OTHER.code eq caseCategory}){
				ajaxService.getAssetListForCase(newValue, "EDC", true, function(result){
					$('#${stuff }_edcType').combobox('setValue', "");
					$('#${stuff }_edcType').combobox('loadData', initSelect(result));
				});
				// Task #2496 取得【客戶】=【設備使用人】下所有設備且設備類別為周邊之設備品項
				ajaxService.getAssetListForCase(newValue, "Related_Products", true, function(result){
					var resultList = initSelect(result);
					$('#${stuff }_peripherals').combobox('setValue', "");
					$('#${stuff }_peripherals').combobox('loadData', resultList);
					
					$('#${stuff }_peripherals2').combobox('setValue', "");
					$('#${stuff }_peripherals2').combobox('loadData', resultList);
					
					$('#${stuff }_peripherals3').combobox('setValue', "");
					$('#${stuff }_peripherals3').combobox('loadData', resultList);
				});
			}
		} else {
			$("#${stuff }_contractId").combobox('loadData', initSelect(null));
			//update by 2017/07/28 Bug #2052  選擇客戶后，再次選擇為‘請選擇’，合約編號顯示錯誤。
			$("#${stuff }_contractId").combobox("setValue","");
			//刷卡機型
			$("#${stuff }_edcType").combobox('loadData', initSelect(null));
			$('#${stuff }_edcType').combobox('setValue', "");
			//Task #3335 若oldValue=台新銀行，newValue='',
			if(oldValue == '${tsbCustomerId}' && (!isEmpty('${gpCustomerId}') && newValue!='${gpCustomerId}')){
				<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory}">
					var requirementNoOptions = $("#${stuff }_requirementNo").textbox('options');
					requirementNoOptions.missingMessage = '';
					requirementNoOptions.required = false;
					$("#${stuff }_requirementNo").textbox(requirementNoOptions);
					// 限制欄位長度
					//textBoxDefaultSetting($("#${stuff }_requirementNo"));
					$('#${stuff }_requirementNo').textbox('textbox').attr('maxlength', 17);
					// 標記
					$("#requirementNoLabel").html("需求單號:");
				</c:if>
			}
		}
		$("#edc").find(".easyui-combobox").combobox('setValue', "");
		$("#edc").find(".easyui-textbox").textbox('setValue', "");
		// Task #2621
		$("#${stuff }_ecrConnection").combobox('setValue', "noEcrLine");
		//checkDtidType(newValue,-1);
		//客戶改變，需要清空特點信息
		$("#${stuff }_merchantHeaderId").combobox("loadData",initSelect(null));
		$("#${stuff }_merchantHeaderId").combobox("setValue","");
	}
	
	/*
	合約異動事件
	*/
	function contractOnChange(newValue, oldValue){
		$("#caseDialogMsg").html("");
		//根據所選合約獲取週邊設備、刷卡機型以及維護廠商下拉寫表
		contractChange(newValue);
	}
	
	/*
	根據維護廠商聯動維護部門
	*/
	function getDeptList(newValue, oldValue){
		if (newValue != "") {
			$("#${stuff }_departmentId").combobox('setValue', "");
			//獲取維護部門
			ajaxService.getDeptList(newValue,function(data){
				if (data != null) {
					data.push({"name":'客服',"value":'CUSTOMER_SERVICE'});
					$("#${stuff }_departmentId").combobox("loadData",initSelect(data));
				} else {
					$("#${stuff }_departmentId").combobox("loadData","");
				}
			});
		} else {
			$("#${stuff }_departmentId").combobox('loadData', initSelect(null));
			$("#${stuff }_departmentId").combobox('setValue', "");
		}
	}
	/*
	根據合約編號獲取週邊設備、刷卡機型以及維護廠商下拉寫表
	*/
	function contractChange(newValue){
		// 清空維護廠商
		$('#${stuff }_companyId').combobox('setValue', "");
		if (newValue != "") {
			//Task #3460 若裝機類型為微型商戶，則維護廠商只能是經貿聯網
			var changeCmsCase = $("#changeCmsCase").val();
			ajaxService.getVendersByContractId(newValue, function(result){
				//如果在裝機情況下，維護廠商為下拉列表，需要加載下拉數據。其餘案件類別為文本框
				if (${caseCategoryAttr.INSTALL.code eq caseCategory || caseCategoryAttr.OTHER.code eq caseCategory} && result) {
					// 将结果放到維護廠商下拉框
					$('#${stuff }_companyId').combobox('loadData', initSelect(result));
					//Task #3460 若裝機類型為微型商戶，則維護廠商只能是經貿聯網
					if(${caseCategoryAttr.INSTALL.code eq caseCategory}){
						//Task #3460 若裝機類型為微型商戶，則維護廠商只能是經貿聯網
						var installType = $('#${stuff }_installType').combobox('getValue');
						if(installType == 4){
							contractChangeByinstallType(installType);
						}
					}else if('${iatomsContantsAttr.PARAM_YES }' == changeCmsCase){
						contractChangeByinstallType(4);
					}
				}else if('${iatomsContantsAttr.PARAM_YES }' == changeCmsCase){
					contractChangeByinstallType(4);
				}
			});
		} else {
			//如果在裝機情況下，維護廠商為下拉列表，需要清空下拉數據。其餘案件類別為文本框
			if (${caseCategoryAttr.INSTALL.code eq caseCategory}) {
				$('#${stuff }_companyId').combobox({disabled:''});
				$('#${stuff }_companyId').combobox('setValue', "");
				// 清空維護廠商
				$('#${stuff }_companyId').combobox('loadData', initSelect(null));
				
				//Task #3460 若裝機類型為微型商戶，則維護廠商只能是經貿聯網
				/* var installType = $('#${stuff }_installType').combobox('getValue');
				if(installType == 4){
					contractChangeByinstallType(installType);
				} */
			}else if(${caseCategoryAttr.OTHER.code eq caseCategory}){
				$('#${stuff }_companyId').combobox({disabled:''});
				$('#${stuff }_companyId').combobox('setValue', "");
				// 清空維護廠商
				$('#${stuff }_companyId').combobox('loadData', initSelect(null));
			}
		}
	}
	/*
	裝機類型異動事件
	*/
	function installTypeOnChange(newValue, oldValue){
		//Task 3460 若為微型商戶案件，維護廠商只能選 經貿聯網
		var postCodesOptions = $("#${stuff }_installedPostCode").combobox("options");
		var tempValue = $("#${stuff }_installedPostCode").combobox("getValue");
		if(newValue == 4){
			contractChangeByinstallType(newValue);
			postCodesOptions.invalidMessage = '請輸入郵遞區號';
			postCodesOptions.required = true;
			postCodesOptions.validType='requiredDropList[\'郵遞區號\']';
			$("#${stuff }_installedPostCode").combobox(postCodesOptions);
			$("#${stuff }_installedPostCode").addClass("easyui-combobox");
		}else if(oldValue == 4){
			//若由微型商戶變為其他，則重新加載維護廠商的下拉列表
			$('#${stuff }_companyId').combobox("enable");
			var oldContract = $('#${stuff }_contractId').combobox("getValue");
			contractChange(oldContract);
			postCodesOptions.invalidMessage = '';
			postCodesOptions.required = false;
			postCodesOptions.validType='';
			$("#${stuff }_installedPostCode").combobox(postCodesOptions);
			$("#${stuff }_installedPostCode").addClass("easyui-combobox");
		}
		locationChange("${stuff }_installedPostCode", $("#${stuff }_installedAdressLocation").combobox("getValue"), "");
		if (tempValue != null && tempValue != "") {
			$("#${stuff }_installedPostCode").combobox("setValue", tempValue);
		}
		//$("#caseDialogMsg").html("");
	}
	/*
	裝機類型異動事件，若裝機類型為微型商戶，則維護廠商只能選 經貿聯網
	*/
	function contractChangeByinstallType(newValue){
		var contractFlag = false;
		if (newValue != "") {
			var companyIdList = $('#${stuff }_companyId').combobox("getData");
			for(var i = 0; i < companyIdList.length; i++){
				if(companyIdList[i].value == "10000000-01"){
					// 設置維護廠商為經貿聯網，并設置維護廠商欄位為不可編輯
					$('#${stuff }_companyId').combobox('setValue', "10000000-01");
					$('#${stuff }_companyId').combobox("disable"); 
					contractFlag = true;
				}
			}
			if(!contractFlag){
				$('#${stuff }_companyId').combobox({disabled:''});
				$('#${stuff }_companyId').combobox('setValue', "");
				// 清空維護廠商
				$('#${stuff }_companyId').combobox('loadData', initSelect(null));
			}
		}
	}
	/*
	案件類型異動事件
	*/
	function caseTypeOnChange(newValue, oldValue){
		// Task #3081 預計完成日，只有預約件可以改，其他不能改，將系統算出之SLA應完修時間顯示在預計完成日欄位
		$("#${stuff }_expectedCompletionDate").datebox("setValue", "");
		//案件類型為‘預約’，則預計完成日期為必填
		if (newValue == "${iatomsContantsAttr.TICKET_MODE_APPOINTMENT}"){
			$("#${stuff }_expectedCompletionDate").datebox("enable");
			$("#${stuff }_expectedCompletionDate").datebox("enableValidation");
			$("#${stuff }_expectedCompletionDateLable").html("預計完成日:<span class=\"red\">*</span>");
			//加載限制長度輸入的js
			textBoxDefaultSetting($('#${stuff }_expectedCompletionDate'));
		} else {
			//其餘案件類型不可編輯且制空
			$("#${stuff }_expectedCompletionDate").datebox("disableValidation");
			$("#${stuff }_expectedCompletionDateLable").html("預計完成日:");
			$("#${stuff }_expectedCompletionDate").datebox("disable");
			$("#${stuff }_expectedCompletionDate").datebox("setValue", "");
		}
	}
	
	/*
	點選DTID查詢按鈕，彈出查詢DTID框。
	*/
	function queryDTIDInfo() {
		$("#caseDialogMsg").html("");
		var customerDefaultValue = '';
		//如果當前登錄者為客戶角色，則需將客戶VALUE傳入查詢頁面
		if (${(caseManagerFormDTO.isCustomerAttribute == true || caseManagerFormDTO.isCustomerVendorAttribute == true) && !caseManagerFormDTO.isVendorAttribute}) {
			customerDefaultValue = '${customers[0].value}';
		}
		var param = {
			customerDefaultValue: customerDefaultValue,
			isCustomerAttribute:${caseManagerFormDTO.isCustomerAttribute},
			isVendorAttribute:${caseManagerFormDTO.isVendorAttribute},
			isCustomerVendorAttribute:${caseManagerFormDTO.isCustomerVendorAttribute}
		};
		viewQueryDtid = $('#caseInfoDlgElement').dialog({    
			title : "選擇DTID",
			width : 980,
			height : 570,
			top:10,
			closed : false,
			cache : false,
			href : "${contextPathCaseInfo}/caseHandle.do?actionId=initDtid",
			queryParams : param,
			modal : true,
			onBeforeLoad : function(){
				// clear查詢dtid對話框
				if(viewQueryDtid){
					viewQueryDtid.panel('clear');
				}
			},
			onBeforeOpen : function(){
				// clear查詢dtid對話框
				if(viewQueryDtid){
					viewQueryDtid.panel('clear');
				}
			},
			onLoad :function(){
				textBoxSetting("caseInfoDlgElement");
				dateboxSetting("caseInfoDlgElement");
				if(typeof settingDialogPanel != 'undefined' && settingDialogPanel instanceof Function) {
					settingDialogPanel('caseInfoDlgElement');
				}
			},
		});
	}
	/*
	* chooseDtidOnCheck選中事件
	*/
	function chooseDtidOnCheck(index, row){
	//	var row = $('#chooseDtidDatagrid').datagrid('getSelected');
		if (row == null) {
			$("#msgDtid").html("請選擇記錄");
			return false;
		} else {
			var dtid = row.dtid;
			//根據DTID獲取案件資料
			getCaseManagerInfo(dtid);
			viewQueryDtid.dialog('close');
			//viewQueryDtid.dialog('clear');
		}
	}
	/*
	依據所選擇的dtid獲取對應的信息
	dtid：案件資料DTID
	*/
	function getCaseManagerInfo(dtid) {
		$("#caseDialogMsg").html("");
		var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		if(isEmpty('${caseHandleInfoDTO.caseId }')){
			// 調保存遮罩
			$('#createDlg').closest('div.panel').block(blockStyle1);
		} else {
			// 調保存遮罩
			$('#editDlg').closest('div.panel').block(blockStyle1);
		}
		//在點選DTID輸入框后的 對鈎 查詢案件資料時，傳入的DTID值為空，需要獲取輸入框中輸入的DTID。
		if(dtid==""){
			dtid = $("#${stuff }_dtid").textbox("getValue");
		} else {
			$("#${stuff }_dtid").textbox("setValue", dtid);
		}
		if (dtid == "") {
			$("#caseDialogMsg").html("請輸入DTID");
			handleScrollTop('createDlg','caseDialogMsg');
			handleScrollTop('editDlg','caseDialogMsg');
			if(isEmpty('${caseHandleInfoDTO.caseId }')){
				// 調保存遮罩
				$('#createDlg').closest('div.panel').unblock();
			} else {
				// 調保存遮罩
				$('#editDlg').closest('div.panel').unblock();
			}
			return false;
		}
		<c:if test="${not empty caseHandleInfoDTO.caseId and caseCategoryAttr.INSTALL.code ne caseCategory}">
			var isUpdateAsset = $("#${stuff }_isUpdateAsset").val();
			if (isUpdateAsset == 'Y' || isUpdateAsset == '') {
				$("#${stuff }_isUpdate").val("Y");
			}
		</c:if>
		
		var customerId;
		//如果當前登錄者為客戶角色，則需查詢改客戶下dtid
		if (${(caseManagerFormDTO.isCustomerAttribute == true || caseManagerFormDTO.isCustomerVendorAttribute == true) && !caseManagerFormDTO.isVendorAttribute}) {
			customerId = '${customers[0].value}';
		}
		var isNewFlag = null;
		if('${caseHandleInfoDTO.isUpdateAsset }' == 'Y' 
				&& ${caseCategoryAttr.INSTALL.code ne caseCategory}){
			isNewFlag = 'Y';			
		}
		var isCheck = '';
		if(${caseCategoryAttr.CHECK.code eq caseCategory}){
			isCheck = 'Y';
		}
		//獲取該DTID下的案件資料，並將查詢資料賦值到各欄位
		ajaxService.getCaseMessageByDTID(dtid, customerId, isNewFlag, isCheck, function(result){
			var msg = '';
			if (result == null) {
				handleScrollTop('createDlg','caseDialogMsg');
				handleScrollTop('editDlg','caseDialogMsg');
			} else {
				// 放值進隱藏域
				$("#changeDtid").val(dtid);
				$("#changeCmsCase").val(result.cmsCase);
				if (!isEmpty('${caseHandleInfoDTO.caseId }')) {
					//是否選擇dtid帶值 2018/01/30
					$("#isCheckDtidFlag").val('Y');
				}
				if(isNewFlag != 'Y'){
					//判斷dtid是否有未結案的案件
					if (${caseCategoryAttr.CHECK.code ne caseCategory && caseCategoryAttr.OTHER.code ne caseCategory}) {
						var caseHandleInfoDTOs = result.caseHandleInfoDTOs;
						var caseId = "${caseHandleInfoDTO.caseId}";
						if (caseHandleInfoDTOs != null) {
							if (caseHandleInfoDTOs.length == 1 && caseHandleInfoDTOs[0].caseId == caseId) {
								$("#caseDialogMsg").html("");
							} else {
								msg = "重複案件?案件清單:</br>";
								for (var i=0; i<caseHandleInfoDTOs.length; i++) {
									if (caseId == caseHandleInfoDTOs[i].caseId) {
										continue;
									}
									msg += "<a class='qq-upload-file ucFileUploaderLinkButton' href='#' onclick=\"clickNoEndCaseInfo('" + encodeURIComponent(caseHandleInfoDTOs[i].caseId) + "');\">";
									msg += caseHandleInfoDTOs[i].caseId;
									msg += "</a>,";
									if (i%8 == 0 && i != 0) {
										msg += "</br>";
									}
								}
								
							}
						}
					}
					disabledForDtid("${caseCategory}", false);
				}
			}
			if(isNewFlag == 'Y'){
				if (result != null) {
					loadEdcLinkForChange(result);
				}
			} else {
				//將查詢結果賦值與案件資訊
				if (typeof loadCaseProcessElement=="function") {
					loadCaseProcessElement(result);
				}
				//將查詢結果賦值與特點資料
				if (typeof loadMerchantInfoElement=="function") {
					//有案件编号为修改 2018/01/15
					if (!isEmpty('${caseHandleInfoDTO.caseId }')) {
						loadMerchantInfoElement(result, true);
					} else {
						loadMerchantInfoElement(result, false);
					}
				}
				//將查詢結果賦值與EDC資訊
				if (typeof loadEdcInfoElement=="function") {
					loadEdcInfoElement(result);
				} 
				//將查詢結果賦值與附加檔案
				if (typeof loadCaseAdditionInfoElement=="function") {
					loadCaseAdditionInfoElement(result);
				} 
				//其他案件類型的DTID查詢資料，不帶出交易參數板塊
				if(${caseCategoryAttr.OTHER.code ne caseCategory}){
					//將查詢結果賦值與交易參數
					if (typeof loadTradingParamElement=="function") {
						loadTradingParamElement(result);
					}
				}
			}
			
			if (result == null) {
				disabledForDtid("${caseCategory}", true);
				$("#caseDialogMsg").html("查無資料");
			} else {
				$("#caseDialogMsg").html(msg.substring("0", msg.length-1));
				handleScrollTop('createDlg','caseDialogMsg');
				handleScrollTop('editDlg','caseDialogMsg');
			}
			if(isEmpty('${caseHandleInfoDTO.caseId }')){
				// 調保存遮罩
				$('#createDlg').closest('div.panel').unblock();
			} else {
				// 調保存遮罩
				$('#editDlg').closest('div.panel').unblock();
			}
		});
		javascript:dwr.engine.setAsync(true);
		
	}
	
	/**
	* 點選一筆未結案案件
	*/
	function clickNoEndCaseInfo(caseId) {
		showEditInfoPage(caseId, "${contextPathCaseInfo}");
	}
	/*
	* 非裝機件初始化edctype下拉框
	*/
	function initEdcType(customerId){
		if (customerId != "") {
			ajaxService.getAssetListForCase(customerId, "EDC", true, function(result){
				$('#${stuff }_edcType').combobox('loadData', initSelect(result));
			});
			// Task #2496 取得【客戶】=【設備使用人】下所有設備且設備類別為周邊之設備品項
			ajaxService.getAssetListForCase(customerId, "Related_Products", true, function(result){
				var resultList = initSelect(result);
				$('#${stuff }_peripherals').combobox('loadData', resultList);
				$('#${stuff }_peripherals2').combobox('loadData', resultList);
				$('#${stuff }_peripherals3').combobox('loadData', resultList);
			});
		}
	}
	/*
	將依據DTID查詢的案件資料賦值到案件資訊區塊
	result: 該DTID下的案件資料
	*/
 	function loadCaseProcessElement(result){
		javascript:dwr.engine.setAsync(false);
	 	<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.MERGE.code eq caseCategory or caseCategoryAttr.UNINSTALL.code eq caseCategory}">
	 		var requirementNoOptions = $("#${stuff }_requirementNo").textbox('options');
			if(requirementNoOptions.required){
				requirementNoOptions.invalidMessage = '';
				requirementNoOptions.required = false;
				$("#${stuff }_requirementNo").textbox(requirementNoOptions);
				// 限制欄位長度
				textBoxDefaultSetting($("#${stuff }_requirementNo"));
				// 標記
				$("#requirementNoLabel").html("需求單號:");
			}
	 	</c:if>
	 	/* <c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.MERGE.code eq caseCategory or caseCategoryAttr.UNINSTALL.code eq caseCategory}">
			var requirementNoOptions = $("#${stuff }_requirementNo").textbox('options');
			if(requirementNoOptions.required){
				requirementNoOptions.invalidMessage = '';
				requirementNoOptions.required = false;
				$("#${stuff }_requirementNo").textbox(requirementNoOptions);
				// 限制欄位長度
				textBoxDefaultSetting($("#${stuff }_requirementNo"));
				// 標記
				$("#requirementNoLabel").html("需求單號:");
			}
		</c:if>	 */
		<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
			var receiptTypeOptions = $("#${stuff }_receiptType").combobox('options');
			receiptTypeOptions.invalidMessage = '';
			receiptTypeOptions.required = false;
			receiptTypeOptions.validType='';
			$("#${stuff }_receiptType").combobox(receiptTypeOptions);
			$('#${stuff }_receiptType').addClass("easyui-combobox");
			// 標記
			$("#receiptTypeLabel").html("Receipt_type:");
		</c:if>
	 // Task #2616 AO人員必填
		var aoNameOptions = $("#${stuff }_merAoName").textbox('options');
		if(aoNameOptions.required){
			aoNameOptions.missingMessage = '';
			aoNameOptions.required = false;
			aoNameOptions.disabled = true;
			$("#${stuff }_merAoName").textbox(aoNameOptions);
			// 限制欄位長度
			textBoxDefaultSetting($("#${stuff }_merAoName"));
			// 標記
			$("#aoNameLabel").html("AO人員:");
			
			$("#isGpCustomer").val('N');
		}
		if((!isEmpty('${gpCustomerId}') || !isEmpty('${tsbCustomerId}')) && result != null && !isEmpty(result.customerId)){
			if(result.customerId == '${gpCustomerId}'){
				// Task #2683 環匯 需求單號 :裝機/異動/併機/拆機 為必填外 , 其餘無須必填
				<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.MERGE.code eq caseCategory or caseCategoryAttr.UNINSTALL.code eq caseCategory}">
					var requirementNoOptions = $("#${stuff }_requirementNo").textbox('options');
					requirementNoOptions.missingMessage = '請輸入需求單號';
					requirementNoOptions.required = true;
					$("#${stuff }_requirementNo").textbox(requirementNoOptions);
					$('#${stuff }_requirementNo').addClass("easyui-textbox");
					// 限制欄位長度
					textBoxDefaultSetting($("#${stuff }_requirementNo"));
					// 標記
					$("#requirementNoLabel").html("需求單號:<span class=\"red\">*</span>");
				</c:if>
				
				// Task #2616 AO人員必填
				var aoNameOptions = $("#${stuff }_merAoName").textbox('options');
				aoNameOptions.missingMessage = '請輸入AO人員';
				aoNameOptions.required = true;
				aoNameOptions.disabled = false;
				aoNameOptions.readonly = true;
				$("#${stuff }_merAoName").textbox(aoNameOptions);
				// 限制欄位長度
				textBoxDefaultSetting($("#${stuff }_merAoName"));
				// 標記
				$("#aoNameLabel").html("AO人員:<span class=\"red\">*</span>");
				
				//Task #3579 [環匯：裝機、異動、專案必填]
				<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
					var receiptTypeOptions = $("#${stuff }_receiptType").combobox('options');
					receiptTypeOptions.invalidMessage = '請輸入Receipt_type';
					receiptTypeOptions.required = true;
					receiptTypeOptions.validType='requiredDropList';
					$("#${stuff }_receiptType").combobox(receiptTypeOptions);
					$('#${stuff }_receiptType').addClass("easyui-combobox");
					// 標記
					$("#receiptTypeLabel").html("Receipt_type:<span class=\"red\">*</span>");
				</c:if>
				
				$("#isGpCustomer").val('Y');
			} else if (result.customerId == '${tsbCustomerId}'){
				<c:if test="${caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.MERGE.code eq caseCategory}">
					var requirementNoOptions = $("#${stuff }_requirementNo").textbox('options');
					requirementNoOptions.missingMessage = '請輸入需求單號';
					requirementNoOptions.required = true;
					$("#${stuff }_requirementNo").textbox(requirementNoOptions);
					// 限制欄位長度
					//textBoxDefaultSetting($("#${stuff }_requirementNo"));
					$('#${stuff }_requirementNo').textbox('textbox').attr('maxlength', 17);
					$('#${stuff }_requirementNo').addClass("easyui-textbox");
					// 標記
					$("#requirementNoLabel").html("需求單號:<span class=\"red\">*</span>");
				</c:if>
			}
		}
		//如果是報修件，需要判斷客戶是否爲台新
		if (${caseCategoryAttr.REPAIR.code eq caseCategory}) {
			var companyCode = result == null? "":result.companyCode;
			var data = null;
			var myArray=new Array();
			var obj = new Object();
			var i = 0;
			var tempResult = null;
			$("#${stuff }_repairReason").combobox("loadData", initSelect(null));
			$("#${stuff }_repairReason").combobox("setValue", "");
			if (companyCode == '${iatomsContantsAttr.PARAM_TSB_EDC }') {
				<c:forEach var='column' items='${repairReasonTaiXins}' varStatus='status'>
					obj = new Object();
					obj.name='${column.name}';
					obj.value='${column.value}';
					myArray[i++] = obj;
				</c:forEach>
			} else {
				<c:forEach var='column' items='${repairReasons}' varStatus='status'>
					obj = new Object();
					obj.name='${column.name}';
					obj.value='${column.value}';
					myArray[i++] = obj;
				</c:forEach>
			}
			$("#${stuff }_repairReason").combobox("loadData", initSelect(myArray));
		}
		$("#customerId").val(result==null?"":result.customerId);
		//$("#${stuff }_requirementNo").textbox("setValue", result==null?"":result.requirementNo);
	//	$("#${stuff }_dtid").textbox("setValue", result==null?"":result.dtid);
		//如果案件類別為異動，則標頭的顯示為下拉列表，需要加載表頭下拉列表
		// Task #3392
		if (${caseCategoryAttr.UPDATE.code eq caseCategory} || ${caseCategoryAttr.OTHER.code eq caseCategory} || ${caseCategoryAttr.PROJECT.code eq caseCategory}) {
			//根據特點ID查詢特點表頭
			if (result != null) {
				ajaxService.getMerchantHeaderList("","",result.merchantId, function(date){
					if (date != null) {
						$("#${stuff }_merchantHeaderId").combobox("loadData",initSelect(date));
						$("#${stuff }_merchantHeaderId").combobox("setValue","");
					}
				});
			} else {
				$("#${stuff }_merchantHeaderId").combobox("loadData",initSelect(null));
				$("#${stuff }_merchantHeaderId").combobox("setValue","");
			}
		} else {
			//其餘案件類別，表頭的顯示為文本框，則複製標有名稱，且在隱藏域中記錄表頭ID
			$("#${stuff }_merchantHeaderName").textbox("setValue", result==null?"":result.headerName);
			$("#${stuff }_merchantHeaderId").val(result==null?"":result.merchantHeaderId);
		}
		//如果案件類別為異動，則需調用合約改變事件，加載EDC中的下拉列表數據
		// Task #3392
		if (${caseCategoryAttr.UPDATE.code eq caseCategory} || ${caseCategoryAttr.OTHER.code eq caseCategory} || ${caseCategoryAttr.PROJECT.code eq caseCategory}) {
			// 異動不加載維護廠商列表
		//	contractChange(result == null?"":result.contractId);
			initEdcType(result == null?"":result.customerId);
		}
		//如果案件類型為專案，則需要賦值專案類型以及專案名稱
		if (${caseCategoryAttr.PROJECT.code eq caseCategory}) {
			$("#${stuff }_projectCode").textbox("setValue", result==null?"":result.projectCode);
			$("#${stuff }_projectName").textbox("setValue", result==null?"":result.projectName);
		}
		
		// Task #3028 併機 異動 拆機 查核 專案 報修，建案之維護廠商，DTID預設帶出後要能再調整
		$('#${stuff }_companyId').combobox('setValue','');
		//將查詢結果賦值到各個欄位
		//#3392 若建案類型為其他，則客戶、合約編號、維護廠商為下拉選
		var changeCmsCase = $("#changeCmsCase").val();
		if(${caseCategoryAttr.OTHER.code eq caseCategory}){
			$("#${stuff }_customer").combobox("setValue", result==null?"":result.customerId);
			if('${iatomsContantsAttr.PARAM_YES }' == changeCmsCase && $("#${stuff }_contractId").combobox("getValue") == result.contractId){
				contractChangeByinstallType(4);
			}
			$("#${stuff }_contractId").combobox("setValue", result==null?"":result.contractId);
		}else{
			$("#${stuff }_customerName").textbox("setValue", result==null?"":result.customerName);
			$("#${stuff }_contractCode").textbox("setValue", result==null?"":result.contractCode);
			$("#${stuff }_customerId").val(result==null?"":result.customerId);
			$("#${stuff }_contractId").val(result==null?"":result.contractId);
		}
		// 是否有該維護廠商
		var haveVendorCompany = false;
		if(result != null && result.contractId){
			if (result.contractId != "") {
				ajaxService.getVendersByContractId(result.contractId, function(data){
					// 将结果放到維護廠商下拉框
					$('#${stuff }_companyId').combobox('loadData', initSelect(data));
					if(result.companyId){
						// 維護廠商
						var vendorCompany = result.companyId;
						for(var i = 0; i < data.length; i++){
							if(data[i].value == vendorCompany){
								haveVendorCompany = true;
								break;
							}
						}
						if(haveVendorCompany){
							$('#${stuff }_companyId').combobox('select', vendorCompany);
							$('#${stuff }_departmentId').combobox('setValue','');
							ajaxService.getDeptList(result.companyId,function(data){
								if (data != null) {
									data.push({"name":'客服',"value":'CUSTOMER_SERVICE'});
									$("#${stuff }_departmentId").combobox("loadData",initSelect(data));
									if(result.departmentId){
										// 維護部門
										var vendorDept = result.departmentId;
										var haveVendorDept = false;
										for(var i = 0; i < data.length; i++){
											if(data[i].value == vendorDept){
												haveVendorDept = true;
												break;
											}
										}
										if(haveVendorDept){
											$('#${stuff }_departmentId').combobox('select', vendorDept);
										}
									}
								} else {
									$("#${stuff }_departmentId").combobox("loadData","");
								}
							});
						}
					}
				});
			} else {
				//如果在裝機情況下，維護廠商為下拉列表，需要清空下拉數據。其餘案件類別為文本框
				$("#${stuff }_companyId").combobox("loadData",initSelect(null));
			}
		}
		
		//獲取部門下拉列表
	//	getDeptList(result != null?result.companyId:"","");
		
/* 		$("#${stuff }_companyName").textbox("setValue", result==null?"":result.companyName);
		$("#${stuff }_companyId").val(result==null?"":result.companyId); */
		
		$("#${stuff }_requirementNo").textbox("setValue", "");
		//$("#${stuff }_caseType").combobox("setValue", result==null?"":result.caseType==null?"":result.caseType);
		//$("#${stuff }_expectedCompletionDate").datebox("setValue", result==null?"":result.expectedCompletionDate);
		
		// 異動+倂幾時若帶入裝機未結案資料則取值來自處理中案件
		if (${caseCategoryAttr.UPDATE.code eq caseCategory} || ${caseCategoryAttr.MERGE.code eq caseCategory}) {
			$("#${stuff }_sameInstalled").combobox("setValue", result==null ? "N":result.isNewCase == 'N'?'Y':'N');
		}
		
		if (${caseCategoryAttr.INSTALL.code ne caseCategory}) {
			$("#${stuff }_installType").val(result==null?"":result.installType);
		}
		javascript:dwr.engine.setAsync(true);
		//DTID帶值時，若為CMS案件，則判斷裝機類型是否是經貿聯網
		if(!${caseCategoryAttr.OTHER.code eq caseCategory} && '${iatomsContantsAttr.PARAM_YES }' == changeCmsCase){
			contractChangeByinstallType(4);
		}
	}
	
 	/*
	* 案件處理頁面案件類型與預計完成日
	row : 儲存時行資料
	*/
	function updateCase(caseId, caseAction){
		if(caseId){
			javascript:dwr.engine.setAsync(false);
		
			ajaxService.getCaseInfoById(caseId, function(data){
				if(data){
					//修改案件類型
					if(caseAction == '${caseActionAttr.CHANGE_CASE_TYPE.code }'){
						// 更新案件類型欄位
						$("#${stuff }_caseType").combobox('setValue', data.caseType);
						// 更新預計完成日欄位
						$("#${stuff }_expectedCompletionDate").datebox('setValue', data.expectedCompletionDate);
						// Task #3227 結案、立即結案可修改案件類型
						if(data.caseStatus == '${caseStatusAttr.CLOSED.code }' || data.caseStatus == '${caseStatusAttr.IMMEDIATE_CLOSE.code }'){
							$("#${stuff }_expectedCompletionDate").datebox("disable");
						}
	    			//到場
					} else if(caseAction == '${caseActionAttr.ARRIVE.code }'){
						// 更新到場次數欄位
	    				$("#${stuff }_attendanceTimes_vo").textbox('setValue', data.attendanceTimes);
	    			//派工
					} else if(caseAction == '${caseActionAttr.DISPATCHING.code }'){
/* 						<c:choose>
						<c:when test="${caseCategoryAttr.INSTALL.code ne caseCategory }">
							$("#${stuff }_companyName").textbox('setValue', data.companyName);
							$("#${stuff }_companyId").val(data.companyId);
							//聯動維護部門
							getDeptList($("#${stuff }_companyId").val());
							//維護部門
							$("#${stuff }_departmentId").combobox('setValue', data.departmentId);
						</c:when>
						<c:otherwise>
								$("#${stuff }_companyId").combobox('setValue', data.companyId);
								//聯動維護部門
								getDeptList($("#${stuff }_companyId").combobox('getValue'));
								//維護部門
								$("#${stuff }_departmentId").combobox('setValue', data.departmentId);
						</c:otherwise>
					</c:choose> */
						$("#${stuff }_companyId").combobox('setValue', data.companyId);
						//聯動維護部門
						getDeptList($("#${stuff }_companyId").combobox('getValue'));
						//維護部門
						$("#${stuff }_departmentId").combobox('setValue', data.departmentId);
					// Task #3227 結案后可修改案件類型
	    			} else if((caseAction == '${caseActionAttr.CLOSED.code }') || (caseAction == '${caseActionAttr.IMMEDIATELY_CLOSING.code }')){
	    				/* // 客服
	    				var isCustomerService = false;
	    				// 客戶
	    				var isCustomer = false;
	    				// CR #2951 廠商客服
	    				if(${caseManagerFormDTO.isCustomerService }){
	    					isCustomerService = true;
	    				} else if(${caseManagerFormDTO.isVendorService } && ${!caseManagerFormDTO.isCustomerService }){
	    					// 建案廠商給客服公司不等於當前
	    					if('${caseHandleInfoDTO.vendorServiceCustomer }' == '${logonUser.companyId }'){
	    						isCustomerService = true;
	    					}
	    				} else if(${caseManagerFormDTO.isCustomer } && ${!caseManagerFormDTO.isCustomerService } && ${!caseManagerFormDTO.isVendorService }){
	    					isCustomer = true;
	    				}
	    				// 客服或客戶
	    				if(${caseCategoryAttr.OTHER.code ne caseCategory} && (isCustomerService || isCustomer)){
	    					$("#completeDateLabelTd").show();
		    				$("#completeDateTd").show();
		    				$('#${stuff }_completeDate').datetimebox({
		    					required:true,
		    					width:'195px',
		    					validType:'dateTimeValid',
		    					missingMessage:"請輸入實際完修時間", 
		    					invalidMessage:"實際完修時間格式限YYYY/MM/DD HH:mm:ss"
		    				});
		    				$('#${stuff }_completeDate').textbox('textbox').attr('maxlength', 19);
		    				$('#${stuff }_completeDate').datetimebox('setValue', (data.completeDate).format("yyyy/MM/dd HH:mm:ss"));
	    				} */
	    			}	
					$("#hideUpdateDate").val(data.updatedDate);
				}
			});
			javascript:dwr.engine.setAsync(true);	
		}
	}
	
	
	</script>

