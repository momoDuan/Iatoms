<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/taglibs.jsp"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO"%>
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$CASE_CATEGORY" var="caseCategoryAttr" />
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$CASE_STATUS" var="caseStatusAttr" />
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants" var="iatomsContantsAttr" />


<%@include file="/jsp/common/common.jsp"%>
<%
	SessionContext ctx = (SessionContext)request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	CaseManagerFormDTO formDTO = null;
	if(ctx != null) {
		formDTO = (CaseManagerFormDTO) ctx.getResponseResult();
	} else {
		formDTO = new CaseManagerFormDTO();
	}
	if (formDTO == null) {
		formDTO = new CaseManagerFormDTO();
	}
	SrmCaseHandleInfoDTO caseHandleInfoDTO = formDTO.getSrmCaseHandleInfoDTO();
	if(caseHandleInfoDTO == null) {
		caseHandleInfoDTO = new SrmCaseHandleInfoDTO();
	}
	String ucNo = formDTO.getUseCaseNo();
	String caseCategory = formDTO.getCaseCategory();
	//交易參數項目列表
	List<Parameter> transationParameterItems = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_TRANSACTION_PARAMETER_ITEM_LIST);
	//交易類別
	String transationCategorys = (String)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.TRANSACTION_CATEGORY.getCode());
	//獲取交易參數可以編輯的列名，以交易參數分組
	String editFildsMapShow = (String)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_TRANSACTION_PARAMETER_EDIT_FIELDS_MAP);
	//是否下拉框
	List<Parameter> yesOrNoList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.YES_OR_NO.getCode());
	//裝機類別
	List<Parameter> installTypeList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.INSTALL_TYPE.getCode());
	//案件類型
	List<Parameter> ticketModeList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.TICKET_MODE.getCode());
	//縣市下拉框
	List<Parameter> locationList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.LOCATION.getCode());
	//拆機類型
	List<Parameter> uninstallTypeList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.UNINSTALL_TYPE.getCode());
	//報修原因
	List<Parameter> repairReasonList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.REPAIR_REASON.getCode());
	//台新客戶保修原因下拉列表
	List<Parameter> repairReasonTaiXins = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, CaseManagerFormDTO.REPAIR_REASON_TAIXIN_LIST);
	//Logo
	List<Parameter> showLogos = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.CASE_LOGO.getCode());
	/*縣市聯動郵遞區號處理(裝機)*/
	List<Parameter> installedPostCodes = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_INSTALLED_POST_CODE_LIST);
	/*縣市聯動郵遞區號處理(聯系)*/
	List<Parameter> contactPostCodes = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_CONTACT_POST_CODE_LIST);
	/*縣市聯動郵遞區號處理(營業)*/
	List<Parameter> locationPostCodes = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_LOCATION_POST_CODE_LIST);
	
%>
<c:set var="caseHandleInfoDTO" value="<%=caseHandleInfoDTO%>" scope="page"></c:set>
<c:set var="show" value="showInfo" scope="page"></c:set>
<c:set var="caseCategory" value="<%=caseCategory%>" scope="page"></c:set>
<c:set var="editFildsMapShow" value="<%=editFildsMapShow%>" scope="page"></c:set>
<c:set var="transationCategorys" value="<%=transationCategorys%>" scope="page"></c:set>
<c:set var="tradingParamsDatas" value="<%=transationParameterItems%>" scope="page"></c:set>
<c:set var="isVipList" value="<%=yesOrNoList%>" scope="page"></c:set>
<c:set var="installTypes" value="<%=installTypeList%>" scope="page"></c:set>
<c:set var="ticketModes" value="<%=ticketModeList%>" scope="page"></c:set>
<c:set var="locationList" value="<%=locationList%>" scope="page"></c:set>
<c:set var="repairReasons" value="<%=repairReasonList%>" scope="page"></c:set>
<c:set var="uninstallTypes" value="<%=uninstallTypeList %>" scope="page"></c:set>
<c:set var="showLogos" value="<%=showLogos %>" scope="page"></c:set>
<c:set var="repairReasonTaiXins" value="<%=repairReasonTaiXins %>" scope="page"></c:set>
<c:set var="combobox" value="<%=IAtomsConstants.PARAMTER_ITEM_TYPE_COMBOBOX %>" scope="page"></c:set>
<c:set var="click" value="<%=IAtomsConstants.PARAMTER_ITEM_TYPE_CLICK %>" scope="page"></c:set>
<c:set var="installedPostCodes" value="<%=installedPostCodes%>" scope="page"></c:set>
<c:set var="contactPostCodes" value="<%=contactPostCodes%>" scope="page"></c:set>
<c:set var="locationPostCodes" value="<%=locationPostCodes%>" scope="page"></c:set>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>案件資料</title>
	<script type="text/javascript" src="${contextPath}/js/fineuploader-5.0.2.js"></script>
	<link rel="stylesheet" href="${contextPath}/css/fineuploader-5.0.2.css"/>
</head>
<body>
<div style="width: auto; height: auto; padding: 10px 10px; overflow-y: hidden" data-options="region:'center'" >
	<div id="process1" class="easyui-panel" title="案件資訊" style="width: 99%">
		<table style="width: 100%;">
			<tr>
				<td width="10%">
					案件編號:
				</td>
				<td width="25%">${caseHandleInfoDTO.caseId }</td>
				<td width="12%">
					需求單號:
				</td>
				<td width="23%">
					<input class="easyui-textbox"  name="${show }_requirementNo" value="<c:out value='${caseHandleInfoDTO.requirementNo }'/> " disabled>
				</td>
				<td width="12%">
					案件類別:
				</td>
				<td width="23%">
					<c:choose>
						<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory }">裝機</c:when>
						<c:when test="${caseCategoryAttr.MERGE.code eq caseCategory }">倂機	</c:when>
						<c:when test="${caseCategoryAttr.UPDATE.code eq caseCategory }">異動	</c:when>
						<c:when test="${caseCategoryAttr.UNINSTALL.code eq caseCategory }">拆機	</c:when>
						<c:when test="${caseCategoryAttr.CHECK.code eq caseCategory }">查核	</c:when>
						<c:when test="${caseCategoryAttr.PROJECT.code eq caseCategory }">專案</c:when>
						<c:when test="${caseCategoryAttr.REPAIR.code eq caseCategory }">報修</c:when>
						<c:when test="${caseCategoryAttr.OTHER.code eq caseCategory }">其他</c:when>
					</c:choose>
				</td>
			</tr>
			<c:choose>
				<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory }">
					<tr>
						<td>
							客戶: <span class="red">*</span>
						</td>
						<td>
							<input class="easyui-textbox" name="${show }_customerName" value="<c:out value='${caseHandleInfoDTO.customerName }'/>" disabled/>
						</td>
						<td>
							合約編號:
						</td>
						<td>
							<input class="easyui-textbox" name="${show }_contractCode" value="<c:out value='${caseHandleInfoDTO.contractCode }'/>" disabled/>
						</td>
					</tr>
				</c:when>
				<c:otherwise>
					<tr>
						<td>
							DTID: <span class="red">*</span>
						</td>
						<td>
							<input class="easyui-textbox" name="${show }_dtid" value="<c:out value='${caseHandleInfoDTO.dtid }'/>" disabled/>
						</td>
						<td>
							客戶:
						</td>
						<td>
							<input class="easyui-textbox" name="${show }_customerName" value="<c:out value='${caseHandleInfoDTO.customerName }'/>" disabled/>
						</td>
						<td>
							合約編號:
						</td>
						<td>
							<input class="easyui-textbox" name="${show }_contractCode" value="<c:out value='${caseHandleInfoDTO.contractCode }'/>" disabled/>
						</td>
					</tr>
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${caseCategoryAttr.PROJECT.code ne caseCategory }">
					<tr>
						<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory }">
							<td>
								裝機類型: <span class="red">*</span>
							</td>
							<td>
								<cafe:droplisttag
									css="easyui-combobox"
									id="${show }_installType"
									name="${show }_installType"
									style="width:170px"
									selectedValue="${empty caseHandleInfoDTO.caseId ? '1':caseHandleInfoDTO.installType}"
									result="${installTypes }"
									blankName="請選擇" hasBlankValue="true"
									javascript="disabled"
								></cafe:droplisttag>
							</td>
						</c:if>
						<c:if test="${caseCategoryAttr.UPDATE.code eq caseCategory }">
							<td>專案:</td>
							<td>
								<c:choose>
									<c:when test="${not empty caseHandleInfoDTO.caseId and caseHandleInfoDTO.isProject eq 'Y'}">
										<cafe:checklistTag
											name="${show }_isProject"
											id="${show }_isProject" 
											result = "${isVipList}" 
											type="radio" 
											checkedValues='<%=StringUtils.toList("Y",",") %>'
											javascript="disabled"
										/>
									</c:when>
									<c:otherwise>
										<cafe:checklistTag
											name="${show }_isProject"
											id="${show }_isProject" 
											result = "${isVipList}" 
											type="radio" 
											checkedValues='<%=StringUtils.toList("N",",") %>'
											javascript="disabled"
										/>
									</c:otherwise>
								</c:choose>
							</td>
						</c:if>
						<c:if test="${caseCategoryAttr.UNINSTALL.code eq caseCategory  }">
							<td>拆機類型: <span class="red">*</span></td>
							<td>
								<cafe:droplisttag
									css="easyui-combobox"
									id="${show }_uninstallType"
									name="${show }_uninstallType"
									javascript="disabled"
									style="width:170px"
									selectedValue="${empty caseHandleInfoDTO.caseId ? 'ARRIVE_UNINSTALL':caseHandleInfoDTO.uninstallType}"
									result="${uninstallTypes }"
									blankName="請選擇" hasBlankValue="true"
								></cafe:droplisttag>
							</td>
						</c:if>
						<c:if test="${caseCategoryAttr.REPAIR.code eq caseCategory }">
							<td>報修原因: </td>
							<td>
								<c:choose>
									<c:when test="${not empty caseHandleInfoDTO.caseId and caseHandleInfoDTO.companyCode eq iatomsContantsAttr.PARAM_TSB_EDC}">
										<cafe:droplisttag
											css="easyui-combobox"
											id="${show }_repairReason"
											name="${show }_repairReason"
											javascript="disabled"
											style="width:170px"
											result="${repairReasonTaiXins }"
											selectedValue="${caseHandleInfoDTO.repairReason }"
											blankName="請選擇" hasBlankValue="true"
										></cafe:droplisttag>
									</c:when>
									<c:otherwise>
										<cafe:droplisttag
											css="easyui-combobox"
											id="${show }_repairReason"
											name="${show }_repairReason"
											javascript="disabled"
											style="width:170px"
											result="${repairReasons }"
											selectedValue="${caseHandleInfoDTO.repairReason }"
											blankName="請選擇" hasBlankValue="true"
										></cafe:droplisttag>
									</c:otherwise>
								</c:choose>
								
							</td>
						</c:if>
						<td>維護廠商: <span class="red">*</span></td>
						<td>
							<input class="easyui-textbox" name="${show }_companyName" value="<c:out value='${caseHandleInfoDTO.companyName }'/>" disabled/>
						</td>
						<td>維護部門: <span class="red">*</span></td>
						<td>
							<input class="easyui-textbox" name="${show }_departmentName" value="<c:out value='${caseHandleInfoDTO.departmentName }'/>" disabled/>
						</td>
						<c:if test="${caseCategoryAttr.MERGE.code eq caseCategory }">
							<td>是否同裝機作業:</td>
							<td>
								<input class="easyui-textbox"
									value="${caseHandleInfoDTO.sameInstalled eq 'Y'? '是' : caseHandleInfoDTO.sameInstalled eq 'N' ? '否' : ''}" disabled>
							</td>
						</c:if>
					</tr>
				</c:when>
				<c:otherwise>
					<tr>
						<td>專案代碼: </td>
						<td>
							<input class="easyui-textbox" name="${show }_projectCode" value="<c:out value='${caseHandleInfoDTO.projectCode }'/>" disabled/>
						</td>
						<td>專案名稱: </td>
						<td>
							<input class="easyui-textbox" name="${show }_projectName" value="<c:out value='${caseHandleInfoDTO.projectName }'/>" disabled/>
						</td>
						<td>維護廠商: <span class="red">*</span></td>
						<td>
							<input class="easyui-textbox" name="${show }_companyName" value="<c:out value='${caseHandleInfoDTO.companyName }'/>" disabled= />
						</td>
					</tr>
				</c:otherwise>
			</c:choose>
			<tr>
				<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory }">
					<td>專案: <span class="red">*</span></td>
					<td>
						<c:choose>
							<c:when test="${not empty caseHandleInfoDTO.caseId and caseHandleInfoDTO.isProject eq 'Y'}">
								<cafe:checklistTag
									name="${show }_isProject"
									id="${show }_isProject" 
									result = "${isVipList}" 
									type="radio" 
									checkedValues='<%=StringUtils.toList("Y",",") %>'
									javascript="disabled"
								/>
							</c:when>
							<c:otherwise>
								<cafe:checklistTag
									name="${show }_isProject"
									id="${show }_isProject" 
									result = "${isVipList}" 
									type="radio" 
									checkedValues='<%=StringUtils.toList("N",",") %>'
									javascript="disabled"
								/>
							</c:otherwise>
						</c:choose>
					</td>
				</c:if>
				<c:if test="${caseCategoryAttr.PROJECT.code eq caseCategory }">
					<td>維護部門: <span class="red">*</span></td>
					<td>
						<input class="easyui-textbox" name="${show }_departmentName" value="<c:out value='${caseHandleInfoDTO.departmentName }'/>" disabled/>
					</td>
				</c:if>
				<td>案件類型:<span class="red">*</span></td>
				<td>
					<cafe:droplisttag
						css="easyui-combobox"
						id="${show }_caseType"
						name="${show }_caseType"
						result="${ticketModes }"
						style="width:170px"
						selectedValue="${empty caseHandleInfoDTO.caseId ? 'COMMON':caseHandleInfoDTO.caseType}"
						blankName="請選擇" hasBlankValue="true"
						javascript="disabled"
					></cafe:droplisttag>
				</td>
				<td>預計完成日: </td>
				<td>
					<input class="easyui-datebox"
								value="${caseHandleInfoDTO.caseType eq 'APPOINTMENT' ? caseHandleInfoDTO.expectedCompletionDate : caseHandleInfoDTO.acceptableFinishDate}" disabled/>
				</td>
				<c:if test="${caseCategoryAttr.UPDATE.code eq caseCategory }">
					<td>是否同裝機作業:</td>
					<td>
						<input class="easyui-textbox"
							value="${caseHandleInfoDTO.sameInstalled eq 'Y'? '是' : caseHandleInfoDTO.sameInstalled eq 'N' ? '否' : ''}" disabled>
					</td>
				</c:if>
			</tr>
		</table>
	</div>
	<div id="special1" class="easyui-panel" title="特店資料" style="width: 99%">
		<table style="width: 100%;">
			<tr>
				<td width="10%">特店代號:</td>
				<td width="25%">
					<input class="easyui-textbox" name="${show }_merMid" value="<c:out value='${caseHandleInfoDTO.merMid }'/>" disabled/>
				</td>
				<td width="12%">特店名稱:</td>
				<td width="23%">
					<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.merchantName }'/>" disabled/>
				</td>
				<td width="12%">表頭（同對外名稱):</td>
				<td colspan="3">
					<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.merchantHeaderName }'/>" disabled/>&nbsp;
				</td>
			</tr>
			<tr>
				<c:if test="${caseCategoryAttr.UPDATE.code eq caseCategory }">
					<td>舊特店代號: </td>
					<td>
						<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.oldMerchantCode }'/>" disabled/>
					</td>
				</c:if>
				<td>VIP: </td>
				<td>
					<c:choose>
						<c:when test="${not empty caseHandleInfoDTO.caseId and caseHandleInfoDTO.isVip eq 'Y'}">
							<cafe:checklistTag
								name="${show }_isVip"
								id="${show }_isVip" 
								result = "${isVipList}" 
								type="radio" 
								checkedValues='<%=StringUtils.toList("Y",",") %>'
								javascript='disabled=true'
							/>
						</c:when>
						<c:otherwise>
							<cafe:checklistTag
								name="${show }_isVip"
								id="${show }_isVip" 
								result = "${isVipList}" 
								type="radio" 
								checkedValues='<%=StringUtils.toList("N",",") %>'
								javascript='disabled=true'
							/>
						</c:otherwise>
					</c:choose>
				</td>
				<td>特店區域: </td>
				<td>
					<input class="easyui-textbox"  value="<c:out value='${caseHandleInfoDTO.areaName }'/>" disabled>
				</td>
				<c:if test="${caseCategoryAttr.UPDATE.code ne caseCategory }">
					<td>特店聯絡人: </td>
					<td>
						<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.contact }'/>" disabled>
					</td>
				</c:if>
			</tr>
			<tr>
				<c:if test="${caseCategoryAttr.UPDATE.code eq caseCategory }">
					<td>特店聯絡人: </td>
					<td>
						<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.contact }'/>" disabled>
					</td>
				</c:if>
				<td>特店聯絡人電話1: </td>
				<td>
					<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.contactTel }'/>" disabled>
				</td>
				<td>特店聯絡人電話2: </td>
				<td>
					<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.contactTel2 }'/>" disabled>
				</td>
				<c:if test="${caseCategoryAttr.UPDATE.code ne caseCategory }">
					<td>特店聯絡人行動電話: </td>
					<td>
						<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.phone }'/>" disabled>
					</td>
				</c:if>
			</tr>
			<tr>
				<c:if test="${caseCategoryAttr.UPDATE.code eq caseCategory }">
					<td>特店聯絡人行動電話: </td>
					<td>
						<input class="easyui-textbox" value="${caseHandleInfoDTO.phone }" disabled>
					</td>
				</c:if>
				<c:if test="${caseCategoryAttr.UPDATE.code ne caseCategory }">
					<td>特店聯絡人Email: </td>
					<td>
						<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.contactEmail }'/>" disabled="disabled">
					</td>
				</c:if>
				<td>營業時間起: </td>
				<td>
					<input class="easyui-textbox" value="${caseHandleInfoDTO.openHour }" disabled>
				</td>
				<td>營業時間迄: </td>
				<td>
					<input class="easyui-textbox" value="${caseHandleInfoDTO.closeHour }" disabled>
				</td>
				<%-- <c:if test="${caseCategoryAttr.UPDATE.code ne caseCategory && caseCategoryAttr.INSTALL.code ne caseCategory}">
					<td>AO人員: </td>
					<td>
						<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.aoName }'/>" disabled>
					</td>
				</c:if> --%>
			</tr>
			<tr>
				<c:if test="${caseCategoryAttr.UPDATE.code eq caseCategory || caseCategoryAttr.INSTALL.code eq caseCategory}">
					<td>特店聯絡人Email: </td>
					<td>
						<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.contactEmail }'/>" disabled="disabled">
					</td>
					<td>AO人員: </td>
					<td>
						<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.aoName }'/>" disabled="disabled">
					</td>
					<td>AO Email: </td>
					<td colspan="3">
						<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.aoemail }'/>" disabled>
					</td>
				</c:if>
				
				<c:if test="${caseCategoryAttr.UPDATE.code ne caseCategory && caseCategoryAttr.INSTALL.code ne caseCategory}">
					<td>營業地址: </td>
					<td>
						<cafe:droplisttag
							id="${show }_location"
							name="${show }_location" 
							css="easyui-combobox"
							hasBlankValue="true"
							blankName="縣市"
							selectedValue="${caseHandleInfoDTO.location }"
							result="${locationList }"
							style="width: 80px"
							javascript="disabled=true"
						></cafe:droplisttag>
						<cafe:droplisttag
							id="${show }_locationPostCode"
							name="${show }_locationPostCode" 
							css="easyui-combobox"
							hasBlankValue="true"
							blankName="郵遞區號"
							selectedValue="${caseHandleInfoDTO.postCodeId }"
							result="${locationPostCodes }"
							style="width: 120px"
							javascript="disabled=true"
						></cafe:droplisttag>
						<input class="easyui-textbox" style="width: 200px" value="<c:out value='${caseHandleInfoDTO.businessAddress }'/>" disabled>
					</td>
					<td>AO人員: </td>
					<td>
						<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.aoName }'/>" disabled>
					</td>
					<td>AO Email: </td>
					<td>
						<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.aoemail }'/>" disabled>
					</td>
					
				</c:if>
			</tr>
			<tr>
				<c:if test="${caseCategoryAttr.UPDATE.code eq caseCategory || caseCategoryAttr.INSTALL.code eq caseCategory}">
					<td>營業地址: </td>
					<td>
						<cafe:droplisttag
							id="${show }_location"
							name="${show }_location" 
							css="easyui-combobox"
							hasBlankValue="true"
							blankName="縣市"
							selectedValue="${caseHandleInfoDTO.location }"
							result="${locationList }"
							style="width: 100px"
							javascript="disabled"
						></cafe:droplisttag>
						<cafe:droplisttag
							id="${show }_locationPostCode"
							name="${show }_locationPostCode" 
							css="easyui-combobox"
							hasBlankValue="true"
							blankName="郵遞區號"
							selectedValue="${caseHandleInfoDTO.postCodeId }"
							result="${locationPostCodes }"
							style="width: 120px"
							javascript="disabled=true"
						></cafe:droplisttag>
						<input class="easyui-textbox" style="width: 200px" value="<c:out value='${caseHandleInfoDTO.businessAddress }'/>" disabled>
					</td>
					<td>裝機地址: <span class="red">*</span></td>
					<td colspan="3">
						<input type="checkbox" ${caseHandleInfoDTO.isBussinessAddress eq 'Y'?'checked':'' } disabled/>
						<label style="width:200px" >同營業地址</label>
						<cafe:droplisttag
							id="${show }_installedAdressLocation"
							name="${show }_installedAdressLocation" 
							css="easyui-combobox"
							hasBlankValue="true"
							blankName="縣市"
							selectedValue="${caseHandleInfoDTO.isBussinessAddress eq 'Y'? caseHandleInfoDTO.location : caseHandleInfoDTO.installedAdressLocation }"
							result="${locationList }"
							style="width: 80px"
							javascript="disabled"
						></cafe:droplisttag> 
						<cafe:droplisttag
							id="${show }_installedPostCode"
							name="${show }_installedPostCode" 
							css="easyui-combobox"
							hasBlankValue="true"
							blankName="郵遞區號"
							selectedValue="${caseHandleInfoDTO.isBussinessAddress eq 'Y'? caseHandleInfoDTO.postCodeId : caseHandleInfoDTO.installedPostCode }"
							result="${installedPostCodes }"
							style="width: 120px"
							javascript="disabled"
						></cafe:droplisttag> 
						<input class="easyui-textbox" disabled value="<c:out value='${caseHandleInfoDTO.isBussinessAddress eq \'Y\'? caseHandleInfoDTO.businessAddress : caseHandleInfoDTO.installedAdress }'/>" style="width: 200px">
					</td>
				</c:if>
				
				<c:if test="${caseCategoryAttr.UPDATE.code ne caseCategory && caseCategoryAttr.INSTALL.code ne caseCategory}">
					<td>裝機地址: </td>
					<td>
						<cafe:droplisttag
							id="${show }_installedAdressLocation"
							name="${show }_installedAdressLocation" 
							css="easyui-combobox"
							hasBlankValue="true"
							blankName="縣市"
							selectedValue="${caseHandleInfoDTO.isBussinessAddress eq 'Y'? caseHandleInfoDTO.location : caseHandleInfoDTO.installedAdressLocation }"
							result="${locationList }"
							style="width: 80px"
							javascript="disabled"
						></cafe:droplisttag> 
						<cafe:droplisttag
							id="${show }_installedPostCode"
							name="${show }_installedPostCode" 
							css="easyui-combobox"
							hasBlankValue="true"
							blankName="郵遞區號"
							selectedValue="${caseHandleInfoDTO.isBussinessAddress eq 'Y'? caseHandleInfoDTO.postCodeId : caseHandleInfoDTO.installedPostCode }"
							result="${installedPostCodes }"
							style="width: 120px"
							javascript="disabled"
						></cafe:droplisttag> 
						<input class="easyui-textbox"  disabled value="${caseHandleInfoDTO.isBussinessAddress eq 'Y'? caseHandleInfoDTO.businessAddress : caseHandleInfoDTO.installedAdress }" style="width: 200px">
					</td>
					<td>聯繫地址: ${caseCategoryAttr.MERGE.code ne caseCategory ?'<span class="red">*</span>':''}</td>
					<td colspan="3">
						<input type="checkbox" ${caseHandleInfoDTO.contactIsBussinessAddress eq 'Y'?'checked':'' } disabled/>
						<label style="width:200px" >同營業地址</label>
						<cafe:droplisttag
							id="${show }_contactAddressLocation"
							name="${show }_contactAddressLocation" 
							css="easyui-combobox"
							hasBlankValue="true"
							blankName="縣市"
							selectedValue="${caseHandleInfoDTO.contactIsBussinessAddress eq 'Y'? caseHandleInfoDTO.location : caseHandleInfoDTO.contactAddressLocation }"
							result="${locationList }"
							style="width: 100px"
							javascript="disabled"
						></cafe:droplisttag>
						<cafe:droplisttag
							id="${show }_contactPostCode"
							name="${show }_contactPostCode" 
							css="easyui-combobox"
							hasBlankValue="true"
							blankName="郵遞區號"
							selectedValue="${caseHandleInfoDTO.isBussinessAddress eq 'Y'? caseHandleInfoDTO.postCodeId : caseHandleInfoDTO.contactPostCode }"
							result="${contactPostCodes }"
							style="width: 120px"
							javascript="disabled"
						></cafe:droplisttag> 
						<input class="easyui-textbox" disabled value="<c:out value='${caseHandleInfoDTO.contactIsBussinessAddress eq \'Y\'? caseHandleInfoDTO.businessAddress : caseHandleInfoDTO.contactAddress }'/>" style="width: 200px">
					</td>
				</c:if>
			</tr>
			<tr>
				<c:choose>
					<c:when test="${caseCategoryAttr.UPDATE.code eq caseCategory || caseCategoryAttr.INSTALL.code eq caseCategory}">
						<td>裝機聯絡人:<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory}"><span class="red">*</span></c:if></td>
						<td>
							<input type="checkbox" 
									${caseHandleInfoDTO.isBussinessContact eq 'Y'?'checked':'' } disabled/>同特店聯絡人
							<input style="width: 200px" class="easyui-textbox" disabled value="<c:out value='${caseHandleInfoDTO.isBussinessContact eq \'Y\'? caseHandleInfoDTO.contact : caseHandleInfoDTO.installedContact }'/>" }
							">
						</td>
						<td>裝機聯絡人電話:<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory}"><span class="red">*</span></c:if></td>
						<td colspan="3">
							<input type="checkbox" 
									${caseHandleInfoDTO.isBussinessContactPhone eq 'Y'?'checked':'' } disabled/>同特店聯絡人電話
						<input style="width: 250px" class="easyui-textbox" disabled value="<c:out value='${caseHandleInfoDTO.isBussinessContactPhone eq \'Y\'? caseHandleInfoDTO.contactTel : caseHandleInfoDTO.installedContactPhone }'/>" "/>
						</td>
					</c:when>
					<c:otherwise>
						<td>聯繫聯絡人: ${caseCategoryAttr.MERGE.code eq caseCategory or caseCategoryAttr.UNINSTALL.code eq caseCategory?'<span class="red">*</span>':''}</td>
						<td>
							<input type="checkbox" 
									${caseHandleInfoDTO.contactIsBussinessContact eq 'Y'?'checked':'' } disabled/>同特店聯絡人
						<input style="width: 200px" class="easyui-textbox" disabled value="<c:out value='${caseHandleInfoDTO.contactIsBussinessContact eq \'Y\'? caseHandleInfoDTO.contact : caseHandleInfoDTO.contactUser }'/>"">
						</td>
						<td>聯繫聯絡人電話: ${caseCategoryAttr.MERGE.code eq caseCategory or caseCategoryAttr.UNINSTALL.code eq caseCategory ?'<span class="red">*</span>':''}</td>
						<td colspan="3">
							<input type="checkbox" 
									${caseHandleInfoDTO.contactIsBussinessContactPhone eq 'Y'?'checked':'' } disabled/>同特店聯絡人電話
							<input style="width: 250px" class="easyui-textbox" disabled value="<c:out value='${caseHandleInfoDTO.contactIsBussinessContactPhone eq \'Y\'? caseHandleInfoDTO.contactTel : caseHandleInfoDTO.contactUserPhone }'/>" />
						</td>
					</c:otherwise>
				</c:choose>
			</tr>
			<tr>
				<c:choose>
					<c:when test="${caseCategoryAttr.UPDATE.code eq caseCategory || caseCategoryAttr.INSTALL.code eq caseCategory}">
						<td>裝機聯絡人手機:</td>
						<td>
							<input type="checkbox" 
									${caseHandleInfoDTO.isBussinessContactMobilePhone eq 'Y'?'checked':'' } disabled/>同特店聯絡人
							<input style="width: 200px" class="easyui-textbox" disabled value="<c:out value='${caseHandleInfoDTO.isBussinessContactMobilePhone eq \'Y\'? caseHandleInfoDTO.phone : caseHandleInfoDTO.installedContactMobilePhone }'/>" }
							">
						</td>
						<td>裝機聯絡人Email:</td>
						<td colspan="3">
							<input type="checkbox" 
									${caseHandleInfoDTO.isBussinessContactEmail eq 'Y'?'checked':'' } disabled/>同特店聯絡人電話
						<input style="width: 250px" class="easyui-textbox" disabled value="<c:out value='${caseHandleInfoDTO.isBussinessContactEmail eq \'Y\'? caseHandleInfoDTO.contactEmail : caseHandleInfoDTO.installedContactEmail }'/>" "/>
						</td>
					</c:when>
					<c:otherwise>
						<td>聯繫聯絡人手機:</td>
						<td>
							<input type="checkbox" 
									${caseHandleInfoDTO.contactIsBussinessContactMobilePhone eq 'Y'?'checked':'' } disabled/>同特店聯絡人
						<input style="width: 200px" class="easyui-textbox" disabled value="<c:out value='${caseHandleInfoDTO.contactIsBussinessContactMobilePhone eq \'Y\'? caseHandleInfoDTO.phone : caseHandleInfoDTO.contactMobilePhone }'/>"">
						</td>
						<td>聯繫聯絡人Email:</td>
						<td colspan="3">
							<input type="checkbox" 
									${caseHandleInfoDTO.contactIsBussinessContactEmail eq 'Y'?'checked':'' } disabled/>同特店聯絡人電話
							<input style="width: 250px" class="easyui-textbox" disabled value="<c:out value='${caseHandleInfoDTO.contactIsBussinessContactEmail eq \'Y\'? caseHandleInfoDTO.contactEmail : caseHandleInfoDTO.contactUserEmail }'/>" />
						</td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
	</div>
	<div id="edc1" class="easyui-panel" title="EDC資訊" style="width: 99%">
		<table style="width: 100%">
			<tr>
				<td width="10%">刷卡機型:<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory}"><span class="red">*</span></c:if></td>
				<td width="25%">
					<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.edcTypeName }'/>" disabled>
				</td>
				<td width="12%">軟體版本:<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory}"><span class="red">*</span></c:if></td>
				<td width="23%">
					<input width="23%" class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.softwareVersionName }'/>" disabled>
				</td>
				<td width="12%">內建功能: </td>
				<td width="23%">
					<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.builtInFeatureName }'/>" disabled>
				</td>
			</tr>
			<tr>
				<td>雙模組模式: </td>
				<td>
					<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.multiModuleName }'/>" disabled>
				</td>
				<td>週邊設備1: </td>
				<td>
					<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.peripheralsName }'/>" disabled>
				</td>
				<td>週邊設備功能1: </td>
				<td>
					<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.peripheralsFunctionName }'/>" disabled>
				</td>
			</tr>
			<tr>
				<td>ECR連線:<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory}"><span class="red">*</span></c:if> </td>
				<td>
					<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.ecrConnectionName }'/>" disabled>
				</td>
				<td>週邊設備2: </td>
				<td>
					<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.peripherals2Name }'/>" disabled>
				</td>
				<td>週邊設備功能2: </td>
				<td>
					<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.peripheralsFunction2Name }'/>" disabled>
				</td>
			</tr>
			<tr>
				<td>連接方式:<span class="red">*</span> </td>
				<td>
					<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.connectionTypeName }'/>" disabled>
				</td>
				<td>週邊設備3: </td>
				<td>
					<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.peripherals3Name }'/>" disabled>
				</td>
				<td>週邊設備功能3: </td>
				<td>
					<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.peripheralsFunction3Name }'/>" disabled>
				</td>
			</tr>
			<tr>
				<td>LOGO: </td>
					<td>
						<input class="easyui-textbox" value="${caseHandleInfoDTO.logoStyle eq 'ONLY_LOGO'? '僅LOGO' : caseHandleInfoDTO.logoStyle eq 'LOGO_AND_MERCHANT_HEADER' ? 'LOGO+表頭' : caseHandleInfoDTO.logoStyle eq 'ONLY_MERCHANT_HEADER' ? '僅表頭' : ''}" 
							disabled>
					</td>
				<td>是否開啟加密: </td>
					<td>
						<input class="easyui-textbox" value="${caseHandleInfoDTO.isOpenEncrypt eq 'Y' ? '是' : caseHandleInfoDTO.isOpenEncrypt eq 'N' ? '否' : ''}" 
							disabled>
					</td>
				<td>電子化繳費平台: </td>
					<td>
						<input class="easyui-textbox" value="${caseHandleInfoDTO.electronicPayPlatform eq 'Y' ? '是' : caseHandleInfoDTO.electronicPayPlatform eq 'N' ? '否' : ''}" 
							disabled>
					</td>
			</tr>
			<tr>
				<td>電子發票載具: </td>
				<td>
					<input class="easyui-textbox"
						value="${caseHandleInfoDTO.electronicInvoice eq 'Y'? '是' : caseHandleInfoDTO.electronicInvoice eq 'N' ? '否' : ''}" disabled>
				</td>
				<td>銀聯閃付: </td>
				<td>
					<input class="easyui-textbox"
						value="${caseHandleInfoDTO.cupQuickPass eq 'Y' ? '是' : caseHandleInfoDTO.cupQuickPass eq 'N' ? '否' : ''}"  disabled>
				</td>
				<td>Receipt_type: </td>
				<td>
					<input class="easyui-textbox" value="${caseHandleInfoDTO.receiptType}"  disabled>
				</td>
			</tr>
			<tr>
				<td>本機IP: </td>
				<td>
					<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.localhostIp }'/>" disabled>
				</td>
				<td>寬頻連線: </td>
				<td>
					<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.netVendorName }'/>" disabled>
				</td>
			</tr>
			<tr>
				<td>Gateway: </td>
				<td>
					<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.gateway }'/>" disabled>
				</td>
				<td>Netmask: </td>
				<td>
					<input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.netmask }'/>" disabled>
				</td>
			</tr>
		</table>
	</div>
	<div id="caseAdd1" class="easyui-panel" title="案件附加資料" style="width: 99%">
		<table  style="width: 100%">
			<tr>
				<td width="10%">其他說明: </td>
				<td colspan="2">
					<textarea class="easyui-textbox" data-options="multiline:true" style="width: 490px; height: 120px" disabled><c:out value='${caseHandleInfoDTO.description }'/></textarea>
					<%-- <input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.description }'/>" data-options="multiline:true" style="width: 500px; height: 50px" disabled> --%>
				</td>
				<c:if test="${caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.MERGE.code eq caseCategory}">
						<c:choose>
							<c:when test="${empty caseHandleInfoDTO.caseId or caseHandleInfoDTO.caseStatus eq caseStatusAttr.WAIT_DISPATCH.code}">
							</c:when>
							<c:otherwise>
								<td width="10%">異動說明:</td>
								<td colspan="2">
									<textarea class="easyui-textbox" data-options="multiline:true" style="width: 490px; height: 120px" disabled }><c:out value='${caseHandleInfoDTO.updatedDescription }'/></textarea>
								</td>
							</c:otherwise>
						</c:choose>
						<%-- <input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.updatedDescription }'/>" data-options="multiline:true" style="width: 500px; height: 50px" disabled }> --%>
				</c:if>
			</tr>
			<tr>
				<td>附加檔案: </td>
				<td>
					<c:if test="${not empty caseHandleInfoDTO.caseAttFileDTOs}">
						<c:forEach items="${caseHandleInfoDTO.caseAttFileDTOs}" var="attachedFile">
							<div>
								<a class="qq-upload-file ucFileUploaderLinkButton" onclick = "downloadCaseFileShow('${attachedFile.attFileId}', '${caseHandleInfoDTO.isHistory }');"
										href="#">${attachedFile.fileName}</a> 
							</div>
						</c:forEach>
					</c:if>
				</td>
			</tr>
			<tr>
				<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory }">
					<td width="10%">到場次數:</td>
					<td width="30%">
						<input class="easyui-textbox" value="${caseHandleInfoDTO.attendanceTimes==null?'0':caseHandleInfoDTO.attendanceTimes }" disabled }/>次
					</td>
					<td>DTID:</td>
					<td><input class="easyui-textbox" value="<c:out value='${caseHandleInfoDTO.dtid }'/>" disabled></td>
				</c:if>
				<c:if test="${caseCategoryAttr.INSTALL.code ne caseCategory }">
					<td>到場次數:</td>
					<td>
						<input class="easyui-textbox" value="${caseHandleInfoDTO.attendanceTimes==null?'0':caseHandleInfoDTO.attendanceTimes }" disabled }/>次
					</td>
				</c:if>
			</tr>
			<c:choose>
				<c:when test="${ empty caseHandleInfoDTO.caseId }">
					<tr>
						<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory }">
							<td>
								案件筆數:<span class="red">*</span>
							</td>
							<td>
								<input type="radio" checked="checked"/>單筆
								<input type="radio" />複製多筆
								<input class="easyui-numberbox" disabled="disabled">筆
							</td>
						</c:if>
						<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.MERGE.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
								<td>派工TMS:</td>
								<td>
									<input type="checkbox" checked="checked" disabled="disabled"/>派工
								</td>
						</c:if>
					</tr>
				</c:when>
				<c:otherwise>
					<c:if test="${caseHandleInfoDTO.caseStatus eq caseStatusAttr.WAIT_DISPATCH.code}">
						<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.MERGE.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
							<tr>
								<td>派工TMS:</td>
								<td>
									<c:if test="${caseHandleInfoDTO.isTms eq 'Y'}">
										<input type="checkbox" checked="checked"disabled="disabled" />派工
									</c:if>
									<c:if test="${caseHandleInfoDTO.isTms ne 'Y'}">
										<input type="checkbox" disabled="disabled"/>派工
									</c:if>
								</td>
							</tr>
						</c:if>
					</c:if>
				</c:otherwise>
			</c:choose>
		</table>
	</div>
	<c:if test="${caseCategoryAttr.OTHER.code ne caseCategory}">
		<div id="trading1" class="easyui-panel" title="交易參數" style="width: 99%">
			<table id="transDataGrid" class="easyui-datagrid" 
				style="width: auto; height: auto"
				data-options="
					fitColumns:false,
					border:true,
					pagination:false,
					pageList:[15,30,50,100],
					pageSize:15,
					singleSelect: true,
					idField:'transactionParameterId',
					nowrap : false,
					rownumbers:true,
					data:tradingParamsData
					">
				<thead>
					<tr>
						<th data-options="field:'transactionType', width:200 ,halign:'center',align:'left',
											formatter:function(value,row){
												return row.transactionTypeName;
											},
											editor:{
												type:'combobox',
												options:{
													editable:false,
													valueField:'value',
													textField:'name',
													required:true,
													validType:'ignore[\'請選擇\']',
													invalidMessage:'請輸入交易類別',
													data:showAbledTradingTypes
												}
											}">交易類別</th>
						<c:forEach var="columns1" items="${tradingParamsDatas }">
							<c:choose>
								<c:when test="${columns1.paramterItemType eq combobox}">
									<th data-options="field:'${columns1.paramterItemCode }',halign:'center', width:150, align:'left',
										formatter:function(value,row){
											if (value == '請選擇') {
												row['${columns1.paramterItemCode }']='';
												return '';
											}
											return row['${columns1.paramterItemCode }'];
										},
										editor:{
											type:'combobox',
											options:{
												editable:true,
												valueField:'value',
												textField:'name',
												method:'get'
											}
									},styler: function(value,row,index){
										return cellTransStylerShow(value,row,index,'${columns1.paramterItemCode }');
									}">${columns1.paramterItemName }</th>
						 		</c:when>
						 		<c:when test="${columns1.paramterItemType eq click }">
									<th data-options="field:'${columns1.paramterItemCode }',halign:'center', width:150, align:'center',formatter:function(value,row,index){
										return settingFieldShow(value,row,index,'${columns1.paramterItemCode }');
									},styler: function(value,row,index){
										return cellTransStylerShow(value,row,index,'${columns1.paramterItemCode }','');
									}">${columns1.paramterItemName }</th>
								</c:when>
								<c:otherwise>
									<th data-options="field:'${columns1.paramterItemCode }',halign:'center', width:150, align:'center',formatter:function(value,row,index){
										return settingFieldShow(value,row,index,'${columns1.paramterItemCode }');
									}">${columns1.paramterItemName }</th>
								</c:otherwise>
						 	</c:choose>
						</c:forEach>
					</tr>
				</thead>
			</table>
			<table style="width: 100%">
				<tr>
					<td width="10%">TMS參數說明: </td>
					<td>
					<textarea class="easyui-textbox" data-options="multiline:true" style="width: 500px; height: 120px"
								 disabled><c:out value='${caseHandleInfoDTO.tmsParamDesc }'/></textarea>
						<%-- <input class="easyui-textbox" data-options="multiline:true" style="width: 500px; height: 120px"
								value="<c:out value='${caseHandleInfoDTO.tmsParamDesc }'/>" disabled> --%>
					</td>
				</tr>
			</table>
		</div>
	</c:if>
	
	<div id="record" class="easyui-panel" title="案件記錄" style="width: 99%">
			<table id="recordDataGridShow" class="easyui-datagrid" style="width: auto; height: auto"
				data-options="singleSelect: true,
					method: 'post',
					nowrap:false,
					sortOrder:'asc'
					">
				<thead>
					<tr>
						<th data-options="field:'actionName',width:140,halign:'center',align:'left',sortable:true,formatter:function(value,row,index){return actionFormatter(value,row,index);}">動作</th>
						<th data-options="field:'caseStatusName',width:160,halign:'center',align:'left',sortable:true">動作后狀態</th>
						<th data-options="field:'description', width:400,halign:'center',align:'left',sortable:true,formatter:wrapFormatter">處理說明</th>
						<th data-options="field:'dealDate',width:170,halign:'center',align:'center',sortable:true,formatter:formatToTimeStamp">實際執行時間</th>
						<th data-options="field:'createdByName',width:120,halign:'center',align:'left',sortable:true">記錄人員</th>
						<th data-options="field:'mailInfo',width:160,halign:'center',align:'left',hidden:true,sortable:true">mailInfo</th>
						<th data-options="field:'createdDate',width:190,halign:'center',align:'center',sortable:true,formatter:formatToTimeStamp">記錄日期</th>
					</tr>
				</thead>
			</table>
		</div>
</div>
	<script type="text/javascript">
	//编辑行的下拉列表的值，ＪＳＯＮ格式
 	var showAbledTradingTypes = initSelect(<%=transationCategorys%>);
 	var tradingParamsData;
	/*
	* 頁面加載完成函數
	*/
	$(function(){
		<c:if test="${empty caseHandleInfoDTO}">
			tradingParamsData = undefined;
		</c:if>
		<c:if test="${not empty caseHandleInfoDTO and caseHandleInfoDTO.haveTransParameter eq 'Y'}">
			// 處理交易參數值的顯示
			//Bug #2724 由於交易參數 重複進件部份沒有修改，修改交易參數頁面內容 2017/10/31
			var transResult = [];
			var tradingParams = ${caseHandleInfoDTO.caseTransactionParameterStr};
			if(tradingParams){
				for (var i=0; i<tradingParams.length; i++) {
					var itemValue;
					itemValue = tradingParams[i].itemValue;
					if (itemValue == null || isEmpty(itemValue)) {
						itemValue = new Object();
					} else {
						itemValue=JSON.parse(itemValue);
					}
					itemValue.transactionType=tradingParams[i].transactionType;
					itemValue.transactionTypeName=tradingParams[i].transactionTypeName;
					itemValue.MID=tradingParams[i].merchantCode;
					itemValue.MID2=tradingParams[i].merchantCodeOther;
					itemValue.TID=tradingParams[i].tid;
					transResult.push(itemValue);
				}
				tradingParamsData = {
					rows:transResult,
					total:tradingParams.length
				}
			}
		</c:if>
		
		$("#process1").panel({width:'99%'});
		$("#special1").panel({width:'99%'});
		$("#edc1").panel({width:'99%'});
		$("#trading1").panel({width:'99%'});
		$("#caseAdd1").panel({width:'99%'});
	});
	//記錄當前行不可編輯列
	var editColumnsShow = undefined;
	/*
	初始化數據時改變單元格背景顏色。
	value: 需要顯示內容
	row：該行信息
	index:行號
	id：該列的field
	*/
	function cellTransStylerShow(value, row, index, id) {
		//定義只在初始化數據時執行
		if (row.transactionType != "") {
			getEditFieldShow(row, index);
			//如果當前列不再可編輯範圍內，則將單元格背景色變為灰色
			if (editColumnsShow && !editColumnsShow.contains(id)){
				row[id] = "";
				return 'background-color:gray;color:gray';
			} 
		} 
	}
	function getEditFieldShow(row, index){
		//獲取當前交易類別可編輯列
		var updateColumn = ${editFildsMapShow};
		editColumnsShow=eval(updateColumn[row.transactionType]);
	}
	/*
	初始化時，為文本框屬性的列增加lable標籤。
	value：需要顯示的值
	row：該行信息
	index:行號
	field：該列的field
	*/
	function settingFieldShow(value,row,index,field) {
		getEditFieldShow(row, index);
		if (editColumnsShow && !editColumnsShow.contains(field)){
			row[field] = "";
			return "<lable></lable>";
		} else {
			if (value) {
				return "<lable>"+value+"</lable>";
			} else {
				return "<lable></lable>";
			}
		}
	}
	/*
	下載附件資料
	id：附件資料ID
	isHistory：是否查歷史表
	*/
	function downloadCaseFileShow(id, isHistory){
		//判斷該文件是否存在
		ajaxService.checkCaseFile(id, isHistory, function(data){
			if (data) {
				//下載文件
				createSubmitForm("${contextPath}/caseHandle.do","actionId=<%=IAtomsConstants.ACTION_DOWNLOAD%>&attFileId=" + id + "&isHistory=" + isHistory + "&caseCategory=" + '${caseCategory}',"post");
			} else {
				$("#errorMsg").html("文件不存在");
			}
		});
	}
	/*
	* 查詢案件歷程
	* isHistory：是否查歷史表
	*/
	function queryCaseRecordShow(isHistory){
		// 得到案件編號
		var caseId = "${caseHandleInfoDTO.caseId }";
		// 查詢參數
		var params = {caseId : caseId, isHistory : 'N'};
		var options = {
			url : "${contextPath}/caseHandle.do?actionId=<%=IAtomsConstants.ACTION_QUERY_TRANSACTION%>",
			queryParams :params,
			isOpenDialog:true,
			onLoadSuccess:function(data){
			},
			onLoadError : function() {
			}
		}
		// 調用公用查詢方法
		openDlgGridQuery("recordDataGridShow", options);
	}
	function actionFormatter(value,row,index){
		var actionTitle;
		actionTitle = row.mailInfo;
		if(actionTitle == null) {
			return value;
		} else {
			return "<a href='javascript:void(0)' class=\"easyui-tooltip\" title='" + actionTitle + "'>"+value+"</a>";
		}
	}
	</script>
</body>
</html>