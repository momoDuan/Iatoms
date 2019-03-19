<%-- 
	EDC資訊元件
	author：carrieDuan 
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/common/taglibs.jsp"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO"%>
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$CASE_CATEGORY" var="caseCategoryAttr" />
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants" var="iAtomsConstants" />
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$TRANSACTION_CATEGORY" var="transactionCategoryAttr" />
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$CASE_STATUS" var="caseStatusAttr" />
<tiles:useAttribute id="caseManagerFormDTO" name="caseManagerFormDTO" classname="com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO"/>
<%-- 案件類別 --%>
<tiles:useAttribute id="caseCategory" name="caseCategory" classname="java.lang.String" ignore="true"/>
<%-- 通訊模式下拉列表 List<Parameter> --%>
<tiles:useAttribute id="connectionTypes" name="connectionTypes" classname="java.util.List" />
<%-- 雙模組模式下拉列表 List<Parameter> --%>
<tiles:useAttribute id="multiModules" name="multiModules" classname="java.util.List" />
<%-- 寬頻連接列表 List<Parameter> --%>
<tiles:useAttribute id="netVendorList" name="netVendorList" classname="java.util.List"/>
<%-- 案件處理DTO  SrmCaseHandleInfoDTO--%>
<tiles:useAttribute id="caseHandleInfoDTO" name="caseHandleInfoDTO" classname="com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO" ignore="true" />
<%-- ECR連線下拉列表 List<Parameter> --%>
<tiles:useAttribute id="ecrConnections" name="ecrConnections" classname="java.util.List" />
<%-- 支援功能json字符串  --%>
<tiles:useAttribute id="supportedFunStr" name="supportedFunStr" classname="java.lang.String" />
<%-- 專案列表 List<Parameter> --%>
<tiles:useAttribute id="yesOrNoList" name="yesOrNoList" classname="java.util.List"/>
<%-- 案件logo List<Parameter> --%>
<tiles:useAttribute id="logos" name="logos" classname="java.util.List"/>

<%-- 客戶刷卡機型處理 --%>
<tiles:useAttribute id="editEdcAssets" name="editEdcAssets" classname="java.util.List" ignore="true"/>
<%-- 客戶聯動軟體版本處理 --%>
<tiles:useAttribute id="editSoftwareVersions" name="editSoftwareVersions" classname="java.util.List" ignore="true"/>
<%-- 客戶聯動周邊設備處理 --%>
<tiles:useAttribute id="editPeripheralsList" name="editPeripheralsList" classname="java.util.List" ignore="true"/>
<%-- 刷卡機型聯動內建功能處理 --%>
<tiles:useAttribute id="editBuiltInFeatures" name="editBuiltInFeatures" classname="java.util.List" ignore="true"/>
<%-- 刷卡機型聯動連線方式處理 --%>
<tiles:useAttribute id="editConnectionTypes" name="editConnectionTypes" classname="java.util.List" ignore="true"/>
<%-- 周邊設備1聯動周邊設備1功能處理 --%>
<tiles:useAttribute id="editPeripheralsFunctions" name="editPeripheralsFunctions" classname="java.util.List" ignore="true"/>
<%-- 周邊設備2聯動周邊設備2功能處理 --%>
<tiles:useAttribute id="editPeripheralsFunction2s" name="editPeripheralsFunction2s" classname="java.util.List" ignore="true"/>
<%-- 周邊設備3聯動周邊設備3功能處理 --%>
<tiles:useAttribute id="editPeripheralsFunction3s" name="editPeripheralsFunction3s" classname="java.util.List" ignore="true"/>
<%-- Receipt_type --%>
<tiles:useAttribute id="receiptTypes" name="receiptTypes" classname="java.util.List" ignore="true"/>

<tiles:useAttribute id="contextPath" name="contextPath" classname="java.lang.String" ignore="true"/>
<%
	if (caseHandleInfoDTO == null) {
		caseHandleInfoDTO = new SrmCaseHandleInfoDTO();
	}
%>
<c:set var="caseHandleInfoDTO" value="<%=caseHandleInfoDTO %>" scope="page"></c:set>
<c:set var="sybCustomerId" value="<%=caseManagerFormDTO.getSybCustomerId() %>" scope="page"></c:set>
<c:set var="chbCustomerId" value="<%=caseManagerFormDTO.getChbCustomerId() %>" scope="page"></c:set>
<c:set var="scsbCustomerId" value="<%=caseManagerFormDTO.getScsbCustomerId() %>" scope="page"></c:set>
<c:set var="gpCustomerId" value="<%=caseManagerFormDTO.getGpCustomerId() %>" scope="page"></c:set>
<c:set var="stuff" value="case" scope="page"></c:set>
	<div id="edc">
	
		<table style="width: 100%">
			<tr>
				<c:choose>
					<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory}">
						<td width="10%">刷卡機型:<span class="red">*</span></td>
					</c:when>
					<c:otherwise>
						<td width="10%">刷卡機型:</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory
									or caseCategoryAttr.OTHER.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
						<td width="25%">
							<cafe:droplisttag
								id="${stuff }_edcType"
								name="${stuff }_edcType"
								selectedValue="${caseHandleInfoDTO.edcType }" 
								result="${editEdcAssets }"
								hasBlankValue="true"
								blankName="請選擇"
							></cafe:droplisttag>
						</td>
					</c:when>
					<c:otherwise>
						<td>
							<input id="${stuff }_edcTypeName_vo" value="<c:out value='${caseHandleInfoDTO.edcTypeName }'/>" name="${stuff }_edcTypeName_vo" disabled>
							<input id="${stuff }_edcType" name="${stuff }_edcType" value="<c:out value='${caseHandleInfoDTO.edcType }'/>" type="hidden"/>
						</td>
					</c:otherwise>
				</c:choose>
				
				<c:choose>
					<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory
										or caseCategoryAttr.MERGE.code eq caseCategory  or caseCategoryAttr.PROJECT.code eq caseCategory}">
						<td width="12%">軟體版本:<span class="red">*</span></td>
					</c:when>
					<c:otherwise>
						<td width="12%">軟體版本:</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.MERGE.code eq caseCategory 
									or caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.OTHER.code eq caseCategory
									 or caseCategoryAttr.PROJECT.code eq caseCategory}">
						<td width="18%">
							<cafe:droplisttag
								id="${stuff }_softwareVersion"
								name="${stuff }_softwareVersion" 
								result="${editSoftwareVersions }"
								hasBlankValue="true"
								selectedValue="${caseHandleInfoDTO.softwareVersion }"
								blankName="請選擇"
							></cafe:droplisttag>
						</td>
					</c:when>
					<c:otherwise>
						<td width="18%">
							<input id="${stuff }_softwareVersionName_vo" value="<c:out value='${caseHandleInfoDTO.softwareVersionName }'/>" name="${stuff }_softwareVersionName_vo" disabled>
							<input id="${stuff }_softwareVersion" name="${stuff }_softwareVersion" value="<c:out value='${caseHandleInfoDTO.softwareVersion }'/>" type="hidden"/>
						</td>
					</c:otherwise>
				</c:choose>
				
				<td width="12%">內建功能: </td>
				<c:choose>
					<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory
										or caseCategoryAttr.OTHER.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
						<td width="23%">
							<cafe:droplisttag
								id="${stuff }_builtInFeature"
								name="${stuff }_builtInFeature" 
								result="${editBuiltInFeatures }"
								hasBlankValue="true"
								blankName="請選擇(複選)"
							></cafe:droplisttag>
						</td>
					</c:when>
					<c:otherwise>
						<td width="23%">
							<input id="${stuff }_builtInFeatureName_vo" value="<c:out value='${caseHandleInfoDTO.builtInFeatureName }'/>" name="${stuff }_builtInFeatureName_vo" disabled>
							<input id="${stuff }_builtInFeature" name="${stuff }_builtInFeature" value="<c:out value='${caseHandleInfoDTO.builtInFeature }'/>" type="hidden"/>
						</td>
					</c:otherwise>
				</c:choose>
			</tr>
			<tr>
				<td>雙模組模式: </td>
				<c:choose>
					<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory
										or caseCategoryAttr.OTHER.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
						<td>
							<cafe:droplisttag
								id="${stuff }_multiModule"
								name="${stuff }_multiModule" 
								selectedValue="${caseHandleInfoDTO.multiModule }"
								result="${multiModules }"
								hasBlankValue="true"
								blankName="請選擇"
							></cafe:droplisttag>
						</td>
					</c:when>
					<c:otherwise>
						<td>
							<input id="${stuff }_multiModuleName_vo" value="<c:out value='${caseHandleInfoDTO.multiModuleName }'/>" name="${stuff }_multiModuleName_vo" disabled>
							<input id="${stuff }_multiModule" name="${stuff }_multiModule" value="<c:out value='${caseHandleInfoDTO.multiModule }'/>" type="hidden"/>
						</td>
					</c:otherwise>
				</c:choose>
				<td>週邊設備1: </td>
				<c:choose>
					<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory 
										or caseCategoryAttr.OTHER.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
						<td>
							<cafe:droplisttag
								id="${stuff }_peripherals"
								name="${stuff }_peripherals" 
								selectedValue="${caseHandleInfoDTO.peripherals }"
								result="${editPeripheralsList }"
								hasBlankValue="true"
								blankName="請選擇"
							></cafe:droplisttag>
						</td>
					</c:when>
					<c:otherwise>
						<td>
							<input id="${stuff }_peripheralsName_vo" value="<c:out value='${caseHandleInfoDTO.peripheralsName }'/>" name="${stuff }_peripheralsName_vo" disabled>
							<input id="${stuff }_peripherals" name="${stuff }_peripherals" value="<c:out value='${caseHandleInfoDTO.peripherals }'/>" type="hidden"/>
						</td>
					</c:otherwise>
				</c:choose>
				
				<td>週邊設備功能1: </td>
				<c:choose>
					<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory 
										or caseCategoryAttr.OTHER.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
						<td>
							<cafe:droplisttag
								id="${stuff }_peripheralsFunction"
								name="${stuff }_peripheralsFunction" 
								hasBlankValue="true"
								result="${editPeripheralsFunctions }"
								blankName="請選擇(複選)"
							></cafe:droplisttag>
						</td>
					</c:when>
					<c:otherwise>
						<td>
							<input id="${stuff }_peripheralsFunctionName_vo" value="<c:out value='${caseHandleInfoDTO.peripheralsFunctionName }'/>" name="${stuff }_peripheralsFunctionName_vo" disabled>
							<input id="${stuff }_peripheralsFunction" name="${stuff }_peripheralsFunction" value="<c:out value='${caseHandleInfoDTO.peripheralsFunction }'/>" type="hidden"/>
						</td>
					</c:otherwise>
				</c:choose>
				
			</tr>
			<tr>
				<c:choose>
					<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory}">
						<td>ECR連線:<span class="red">*</span></td>
					</c:when>
					<c:otherwise>
						<td>ECR連線:</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory 
										or caseCategoryAttr.OTHER.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
						<td>
							<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory}">
								<cafe:droplisttag
									id="${stuff }_ecrConnection"
									name="${stuff }_ecrConnection" 
									selectedValue="${not empty caseHandleInfoDTO.caseId ? caseHandleInfoDTO.ecrConnection : 'noEcrLine'}"
									result="${ecrConnections }"
									hasBlankValue="true"
									blankName="請選擇"
								></cafe:droplisttag>
							</c:if>
							<c:if test="${caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.OTHER.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
								<cafe:droplisttag
									id="${stuff }_ecrConnection"
									name="${stuff }_ecrConnection" 
									selectedValue="${caseHandleInfoDTO.ecrConnection }"
									result="${ecrConnections }"
									hasBlankValue="true"
									blankName="請選擇"
								></cafe:droplisttag>
							</c:if>
						</td>
					</c:when>
					<c:otherwise>
						<td>
							<input id="${stuff }_ecrConnectionName_vo" value="<c:out value='${caseHandleInfoDTO.ecrConnectionName }'/>" name="${stuff }_ecrConnectionName_vo" disabled>
							<input id="${stuff }_ecrConnection" name="${stuff }_ecrConnection" value="<c:out value='${caseHandleInfoDTO.ecrConnection }'/>" type="hidden"/>
						</td>
					</c:otherwise>
				</c:choose>
				
				<td>週邊設備2: </td>
				<c:choose>
					<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory 
										or caseCategoryAttr.OTHER.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
						<td>
							<cafe:droplisttag
								id="${stuff }_peripherals2"
								name="${stuff }_peripherals2" 
								result="${editPeripheralsList }"
								selectedValue="${caseHandleInfoDTO.peripherals2 }"
								hasBlankValue="true"
								blankName="請選擇"
							></cafe:droplisttag>
						</td>
					</c:when>
					<c:otherwise>
						<td>
							<input id="${stuff }_peripherals2Name_vo" value="<c:out value='${caseHandleInfoDTO.peripherals2Name }'/>" name="${stuff }_peripherals2Name_vo" disabled>
							<input id="${stuff }_peripherals2" name="${stuff }_peripherals2" value="<c:out value='${caseHandleInfoDTO.peripherals2 }'/>" type="hidden"/>
						</td>
					</c:otherwise>
				</c:choose>
				
				<td>週邊設備功能2: </td>
				<c:choose>
					<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory 
										or caseCategoryAttr.OTHER.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
						<td>
							<cafe:droplisttag
								id="${stuff }_peripheralsFunction2"
								name="${stuff }_peripheralsFunction2" 
								hasBlankValue="true"
								result="${editPeripheralsFunction2s }"
								blankName="請選擇(複選)"
							></cafe:droplisttag>
						</td>
					</c:when>
					<c:otherwise>
						<td>
							<input id="${stuff }_peripheralsFunction2Name_vo" value="<c:out value='${caseHandleInfoDTO.peripheralsFunction2Name }'/>" name="${stuff }_peripheralsFunction2Name_vo" disabled>
							<input id="${stuff }_peripheralsFunction2" name="${stuff }_peripheralsFunction2" value="<c:out value='${caseHandleInfoDTO.peripheralsFunction2 }'/>" type="hidden"/>
						</td>
					</c:otherwise>
				</c:choose>
				
			</tr>
			<tr>
				<td>連接方式:<span class="red">*</span> </td>
				<c:choose>
					<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory 
										or caseCategoryAttr.OTHER.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
						<td>
							<cafe:droplisttag
								id="${stuff }_connectionType"
								name="${stuff }_connectionType" 
								result="${editConnectionTypes }"
								hasBlankValue="true"
								blankName="請選擇(複選)"
							></cafe:droplisttag>
						</td>
					</c:when>
					<c:otherwise>
						<td>
							<input id="${stuff }_connectionTypeName_vo" value="<c:out value='${caseHandleInfoDTO.connectionTypeName }'/>" name="${stuff }_connectionTypeName_vo" disabled>
							<input id="${stuff }_connectionType" name="${stuff }_connectionType" value="<c:out value='${caseHandleInfoDTO.connectionType }'/>" type="hidden"/>
						</td>
					</c:otherwise>
				</c:choose>
				
				<td>週邊設備3: </td>
				<c:choose>
					<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory 
										or caseCategoryAttr.OTHER.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
						<td>
							<cafe:droplisttag
								id="${stuff }_peripherals3"
								name="${stuff }_peripherals3" 
								selectedValue="${caseHandleInfoDTO.peripherals3 }"
								result="${editPeripheralsList }"
								hasBlankValue="true"
								blankName="請選擇"
							></cafe:droplisttag>
						</td>
					</c:when>
					<c:otherwise>
						<td width="18%">
							<input id="${stuff }_peripherals3Name_vo" value="<c:out value='${caseHandleInfoDTO.peripherals3Name }'/>" name="${stuff }_peripherals3Name_vo" disabled>
							<input id="${stuff }_peripherals3" name="${stuff }_peripherals3" value="<c:out value='${caseHandleInfoDTO.peripherals3 }'/>" type="hidden"/>
						</td>
					</c:otherwise>
				</c:choose>
				
				<td>週邊設備功能3: </td>
				<c:choose>
					<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory 
										or caseCategoryAttr.OTHER.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
						<td>
							<cafe:droplisttag
								id="${stuff }_peripheralsFunction3"
								name="${stuff }_peripheralsFunction3" 
								hasBlankValue="true"
								result="${editPeripheralsFunction3s }"
								blankName="請選擇(複選)"
							></cafe:droplisttag>
						</td>
					</c:when>
					<c:otherwise>
						<td>
							<input id="${stuff }_peripheralsFunction3Name_vo" value="<c:out value='${caseHandleInfoDTO.peripheralsFunction3Name }'/>" name="${stuff }_peripheralsFunction3Name_vo" disabled/>
							<input id="${stuff }_peripheralsFunction3" name="${stuff }_peripheralsFunction3" value="<c:out value='${caseHandleInfoDTO.peripheralsFunction3 }'/>" type="hidden"/>
						</td>
					</c:otherwise>
				</c:choose>
				
			</tr>
			<tr>
				<td>LOGO: </td>
				<c:choose>
					<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory 
										or caseCategoryAttr.OTHER.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
						<td>
							<cafe:droplisttag
								id="${stuff }_logoStyle"
								name="${stuff }_logoStyle" 
								hasBlankValue="true"
								selectedValue="${caseHandleInfoDTO.logoStyle }"
								blankName="請選擇"
								result="${logos }"
							></cafe:droplisttag>
						</td>
					</c:when>
					<c:otherwise>
						<td>
							<input id="${stuff }_logoStyle_vo" 
							value="${caseHandleInfoDTO.logoStyle eq 'ONLY_LOGO'? '僅LOGO' : caseHandleInfoDTO.logoStyle eq 'LOGO_AND_MERCHANT_HEADER' ? 'LOGO+表頭' : caseHandleInfoDTO.logoStyle eq 'ONLY_MERCHANT_HEADER' ? '僅表頭' : ''}" 
							name="${stuff }_logoStyle_vo" disabled>
							<input id="${stuff }_logoStyle" name="${stuff }_logoStyle" value="<c:out value='${caseHandleInfoDTO.logoStyle }'/>" type="hidden"/>
						</td>
					</c:otherwise>
				</c:choose>
				<td>是否開啟加密: </td>
				<c:choose>
					<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory
										or caseCategoryAttr.OTHER.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
						<td>
							<cafe:droplisttag
								id="${stuff }_isOpenEncrypt"
								name="${stuff }_isOpenEncrypt"
								hasBlankValue="true"
								result="${yesOrNoList }"
								selectedValue="${caseHandleInfoDTO.isOpenEncrypt }"
								blankName="請選擇"
							></cafe:droplisttag>
						</td>
					</c:when>
					<c:otherwise>
						<td>
							<input id="${stuff }_isOpenEncrypt_vo" 
								value="${caseHandleInfoDTO.isOpenEncrypt eq 'Y' ? '是' : caseHandleInfoDTO.isOpenEncrypt eq 'N' ? '否' : ''}" 
								name="${stuff }_isOpenEncrypt_vo" disabled>
							<input type="hidden" id="${stuff }_isOpenEncrypt" name="${stuff }_isOpenEncrypt" value="<c:out value='${caseHandleInfoDTO.isOpenEncrypt }'/>">
						</td>
					</c:otherwise>
				</c:choose>
				<td>電子化繳費平台: </td>
				<c:choose>
					<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory
										or caseCategoryAttr.OTHER.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
						<td>
							<cafe:droplisttag
								id="${stuff }_electronicPayPlatform"
								name="${stuff }_electronicPayPlatform"
								hasBlankValue="true"
								result="${yesOrNoList }"
								selectedValue="${caseHandleInfoDTO.electronicPayPlatform }"
								blankName="請選擇"
							></cafe:droplisttag>
						</td>
					</c:when>
					<c:otherwise>
						<td>
							<input id="${stuff }_electronicPayPlatform_vo" 
								value="${caseHandleInfoDTO.electronicPayPlatform eq 'Y' ? '是' : caseHandleInfoDTO.electronicPayPlatform eq 'N' ? '否' : ''}" 
								name="${stuff }_electronicPayPlatform_vo" disabled>
							<input type="hidden" id="${stuff }_electronicPayPlatform" name="${stuff }_electronicPayPlatform" value="<c:out value='${caseHandleInfoDTO.electronicPayPlatform }'/>">
						</td>
					</c:otherwise>
				</c:choose>
			</tr>
			<tr>
				<td>電子發票載具: </td>
					<c:choose>
					<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory
										or caseCategoryAttr.OTHER.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
						<td>
							<cafe:droplisttag
									id="${stuff }_electronicInvoice"
									name="${stuff }_electronicInvoice"
									hasBlankValue="true"
									result="${yesOrNoList }"
									selectedValue="${caseHandleInfoDTO.electronicInvoice }"
									blankName="請選擇"
								></cafe:droplisttag>
						</td>
					</c:when>
					<c:otherwise>
						<td>
							<input id="${stuff }_electronicInvoice_vo" 
								value="${caseHandleInfoDTO.electronicInvoice eq 'Y'? '是' : caseHandleInfoDTO.electronicInvoice eq 'N' ? '否' : ''}" 
								name="${stuff }_electronicInvoice_vo" disabled>
							<input type="hidden" id="${stuff }_electronicInvoice" name="${stuff }_electronicInvoice" value="<c:out value='${caseHandleInfoDTO.electronicInvoice }'/>">
						</td>
					</c:otherwise>
				</c:choose>
				<td>銀聯閃付: </td>
					<c:choose>
						<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory
											or caseCategoryAttr.OTHER.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
							<td>
								<cafe:droplisttag
									id="${stuff }_cupQuickPass"
									name="${stuff }_cupQuickPass"
									hasBlankValue="true"
									result="${yesOrNoList }"
									selectedValue="${caseHandleInfoDTO.cupQuickPass }"
									blankName="請選擇"
								></cafe:droplisttag>
							</td>
						</c:when>
						<c:otherwise>
							<td>
								<input id="${stuff }_cupQuickPass_vo" 
									value="${caseHandleInfoDTO.cupQuickPass eq 'Y' ? '是' : caseHandleInfoDTO.cupQuickPass eq 'N' ? '否' : ''}" 
									name="${stuff }_cupQuickPass_vo" disabled>
								<input type="hidden" id="${stuff }_cupQuickPass" name="${stuff }_cupQuickPass" value="<c:out value='${caseHandleInfoDTO.cupQuickPass }'/>">
							</td>
						</c:otherwise>
					</c:choose>
					<td width="10%"><lable id="receiptTypeLabel">Receipt_type:</lable></td>
					<c:choose>
						<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory
											or caseCategoryAttr.PROJECT.code eq caseCategory or caseCategoryAttr.OTHER.code eq caseCategory}">
							<td>
								<cafe:droplisttag
									id="${stuff }_receiptType"
									name="${stuff }_receiptType"
									hasBlankValue="true"
									result="${receiptTypes }"
									selectedValue="${caseHandleInfoDTO.receiptType }"
									blankName="請選擇"
								></cafe:droplisttag>
							</td>
						</c:when>
						<c:otherwise>
							<td>
								<input id="${stuff }_receiptType_vo" 
									value="${caseHandleInfoDTO.receiptType  }" 
									name="${stuff }_receiptType_vo" disabled>
								<input type="hidden" id="${stuff }_receiptType" name="${stuff }_receiptType" value="<c:out value='${caseHandleInfoDTO.receiptType }'/>">
							</td>
						</c:otherwise>
					</c:choose>
			</tr>
			
			<tr>
				<td>本機IP: </td>
				<td>
					<c:choose>
						<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory
											or caseCategoryAttr.OTHER.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
							<input id="${stuff }_localhostIp" value="<c:out value='${caseHandleInfoDTO.localhostIp }'/>" name="${stuff }_localhostIp"/>
						</c:when>
						<c:otherwise>
							<input id="${stuff }_localhostIp_vo" value="<c:out value='${caseHandleInfoDTO.localhostIp }'/>" name="${stuff }_localhostIp_vo" disabled/>
							<input id="${stuff }_localhostIp" name="${stuff }_localhostIp" value="<c:out value='${caseHandleInfoDTO.localhostIp }'/>" type="hidden"/>
						</c:otherwise>
					</c:choose>
				</td>
				<td>寬頻連線: </td>
				<c:choose>
					<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory
										or caseCategoryAttr.OTHER.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
						<td>
							<cafe:droplisttag
								id="${stuff }_netVendorId"
								name="${stuff }_netVendorId"
								hasBlankValue="true"
								result="${netVendorList }"
								selectedValue="${caseHandleInfoDTO.netVendorId }"
								blankName="請選擇"
							></cafe:droplisttag>
						</td>
					</c:when>
					<c:otherwise>
						<td>
							<input id="${stuff }_netVendorName_vo" 
								value="<c:out value='${caseHandleInfoDTO.netVendorName }'/>" maxlength="50" name="${stuff }_netVendorName_vo" disabled>
							<input type="hidden" id="${stuff }_netVendorId" name="${stuff }_netVendorId" value="<c:out value='${caseHandleInfoDTO.netVendorId }'/>">
						</td>
					</c:otherwise>
				</c:choose>
				
			</tr>
			<tr>
				<td>Gateway: </td>
				<td>
					<c:choose>
						<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory
											or caseCategoryAttr.OTHER.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
							<input id="${stuff }_gateway" value="<c:out value='${caseHandleInfoDTO.gateway }'/>" name="${stuff }_gateway"/>
						</c:when>
						<c:otherwise>
							<input value="<c:out value='${caseHandleInfoDTO.gateway }'/>" name="${stuff }_gateway_vo" id="${stuff }_gateway_vo" disabled>
							<input type="hidden" id="${stuff }_gateway" name="${stuff }_gateway" value="<c:out value='${caseHandleInfoDTO.gateway }'/>">
						</c:otherwise>
					</c:choose>
				</td>
				<td>Netmask: </td>
				<td>
					<c:choose>
						<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory
											or caseCategoryAttr.OTHER.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
							<input id="${stuff }_netmask" value="<c:out value='${caseHandleInfoDTO.netmask }'/>" name="${stuff }_netmask"/>
						</c:when>
						<c:otherwise>
							<input value="<c:out value='${caseHandleInfoDTO.netmask }'/>" name="${stuff }_netmask_vo" id="${stuff }_netmask_vo" disabled>
							<input type="hidden" id="${stuff }_netmask" name="${stuff }_netmask" value="<c:out value='${caseHandleInfoDTO.netmask }'/>">
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			
		</table>
	</div>
	<script type="text/javascript">
		$(function(){
			<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory && empty caseHandleInfoDTO.caseId}">
				<c:if test="${caseManagerFormDTO.isVendorAttribute == false && (caseManagerFormDTO.isCustomerAttribute == true || caseManagerFormDTO.isCustomerVendorAttribute == true)}">
					if ("${customers[0].value}" != "") {
						if("${customers[0].value}" == '${gpCustomerId}'){
							// 處理環匯客戶
							$("#isGpCustomer").val('Y');
						}
					}
				</c:if>
			</c:if>
			// 加載EDC資訊區塊
		//	setTimeout("loadingEdcInformation('${caseCategory}');",0);
			loadingEdcInformation('${caseCategory}');
		});
		
		
		/*
		* 加載EDC資訊區塊
		*/
		function loadingEdcInformation(caseCategory){
			// panel
			$("#edc").panel({title:'EDC資訊',width:'99%'});
			// 本機IP
			var localhostIp;
			// Gateway
			var gateway;
			// Netmask
			var netmask;
			
			// 裝機+異動
			if(caseCategory == '${caseCategoryAttr.INSTALL.code}' || caseCategory == '${caseCategoryAttr.UPDATE.code}'
					|| caseCategory == '${caseCategoryAttr.OTHER.code }' || caseCategory == '${caseCategoryAttr.PROJECT.code }'){
				var requiredFlag = false;
				if(caseCategory == '${caseCategoryAttr.INSTALL.code}'){
					requiredFlag = true;
				}
				// 刷卡機型
				if ($('#${stuff }_edcType').length > 0) {
					$('#${stuff }_edcType').combobox({
						editable:false, 
						required:requiredFlag,
						valueField:'value',
						textField:'name',
						width:'150px',
						disabled:"${caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory?'disabled':'' }",
						validType:requiredFlag ? 'requiredDropList' : '',
						invalidMessage:"請輸入刷卡機型",
						onChange:function(newValue,oldValue){edcTypeOnChange(newValue,oldValue);}
					});
					$('#${stuff }_edcType').addClass("easyui-combobox");
				}
				// 軟體版本
				if ($('#${stuff }_softwareVersion').length > 0) {
					$('#${stuff }_softwareVersion').combobox({
						editable:false, 
						required:(caseCategory == '${caseCategoryAttr.OTHER.code}')?false:true,
						valueField:'value',
						textField:'name',
						width:'150px',
						validType:(caseCategory == '${caseCategoryAttr.OTHER.code}')?'':'requiredDropList',
						invalidMessage:"請輸入軟體版本" 
					});
					$('#${stuff }_softwareVersion').addClass("easyui-combobox");
				}
				// 內建功能
				if ($('#${stuff }_builtInFeature').length > 0) {
					$('#${stuff }_builtInFeature').combobox({
						editable:false, 
						valueField:'value',
						multiple:true,
						textField:'name',
						panelHeight:'auto',
						width:'150px',
						value:!isEmpty('${caseHandleInfoDTO.builtInFeature }') ? ('${caseHandleInfoDTO.builtInFeature }').split(",") : '',
						onSelect : function(newValue) {
							// 選中時處理請選擇項
							selectMultiple(newValue, "${stuff }_builtInFeature");
						},
						onUnselect : function(newValue) {
							// 取消選中時處理請選擇項
							unSelectMultiple(newValue, "${stuff }_builtInFeature");
						}
					});
					$('#${stuff }_builtInFeature').addClass("easyui-combobox");
				}
				// 雙模組模式
				if ($('#${stuff }_multiModule').length > 0) {
					$('#${stuff }_multiModule').combobox({
						editable:false, 
						valueField:'value',
						textField:'name',
						panelHeight:'auto',
						width:'150px'
					});
					$('#${stuff }_multiModule').addClass("easyui-combobox");
				}
				// 週邊設備1
				if ($('#${stuff }_peripherals').length > 0) {
					$('#${stuff }_peripherals').combobox({
						editable:false, 
						valueField:'value',
						textField:'name',
						onChange:function(newValue,oldValue){linkageCombobox(newValue, oldValue, 'peripheralsFunction');},
						width:'150px'
					});
					$('#${stuff }_peripherals').addClass("easyui-combobox");
				}
				// 週邊設備功能1
				if ($('#${stuff }_peripheralsFunction').length > 0) {
					$('#${stuff }_peripheralsFunction').combobox({
						editable:false, 
						valueField:'value',
						multiple:true,
						textField:'name',
						panelHeight:'auto',
						width:'150px',
						value:!isEmpty('${caseHandleInfoDTO.peripheralsFunction }') ? ('${caseHandleInfoDTO.peripheralsFunction }').split(",") : '',
						onSelect : function(newValue) {
							// 選中時處理請選擇項
							selectMultiple(newValue, "${stuff }_peripheralsFunction");
						},
						onUnselect : function(newValue) {
							// 取消選中時處理請選擇項
							unSelectMultiple(newValue, "${stuff }_peripheralsFunction");
						}
					});
					$('#${stuff }_peripheralsFunction').addClass("easyui-combobox");
				}
				// ECR連線
				if ($('#${stuff }_ecrConnection').length > 0) {
					$('#${stuff }_ecrConnection').combobox({
						editable:false,
						required:requiredFlag,
						valueField:'value',
						textField:'name',
						validType:requiredFlag?'requiredDropList':'',
						invalidMessage:'請輸入ECR連線',
						panelHeight:'auto',
						width:'150px'
					});
					$('#${stuff }_ecrConnection').addClass("easyui-combobox");
				}
				// 週邊設備2
				if ($('#${stuff }_peripherals2').length > 0) {
					$('#${stuff }_peripherals2').combobox({
						editable:false, 
						valueField:'value',
						textField:'name',
						onChange:function(newValue,oldValue){linkageCombobox(newValue, oldValue, 'peripheralsFunction2');},
						width:'150px'
					});
					$('#${stuff }_peripherals2').addClass("easyui-combobox");
				}
				// 週邊設備功能2
				if ($('#${stuff }_peripheralsFunction2').length > 0) {
					$('#${stuff }_peripheralsFunction2').combobox({
						editable:false, 
						valueField:'value',
						multiple:true,
						textField:'name',
						panelHeight:'auto',
						value:!isEmpty('${caseHandleInfoDTO.peripheralsFunction2 }') ? ('${caseHandleInfoDTO.peripheralsFunction2 }').split(",") : '',
						width:'150px',
						onSelect : function(newValue) {
							// 選中時處理請選擇項
							selectMultiple(newValue, "${stuff }_peripheralsFunction2");
						},
						onUnselect : function(newValue) {
							// 取消選中時處理請選擇項
							unSelectMultiple(newValue, "${stuff }_peripheralsFunction2");
						}
					});
					$('#${stuff }_peripheralsFunction2').addClass("easyui-combobox");
				}
				// 連接方式
				if ($('#${stuff }_connectionType').length > 0) {
					$('#${stuff }_connectionType').combobox({
						editable:false, 
						valueField:'value',
						multiple:true,
						textField:'name',
						panelHeight:'auto',
						required:true,
						invalidMessage:'請輸入連接方式',
						validType:'requiredDropList',
						value:!isEmpty('${caseHandleInfoDTO.connectionType }') ? ('${caseHandleInfoDTO.connectionType }').split(",") : '',
						width:'150px',
						onSelect : function(newValue) {
							// 選中時處理請選擇項
							selectMultiple(newValue, "${stuff }_connectionType");
						},
						onUnselect : function(newValue) {
							// 取消選中時處理請選擇項
							unSelectMultiple(newValue, "${stuff }_connectionType");
						}
					});
					$('#${stuff }_connectionType').addClass("easyui-combobox");
				}
				// 週邊設備3
				if ($('#${stuff }_peripherals3').length > 0) {
					$('#${stuff }_peripherals3').combobox({
						editable:false, 
						valueField:'value',
						textField:'name',
						onChange:function(newValue,oldValue){linkageCombobox(newValue, oldValue, 'peripheralsFunction3');},
						width:'150px'
					});
					$('#${stuff }_peripherals3').addClass("easyui-combobox");
				}
				// 週邊設備功能3
				if ($('#${stuff }_peripheralsFunction3').length > 0) {
					$('#${stuff }_peripheralsFunction3').combobox({
						editable:false, 
						valueField:'value',
						multiple:true,
						textField:'name',
						panelHeight:'auto',
						value:!isEmpty('${caseHandleInfoDTO.peripheralsFunction3 }') ? ('${caseHandleInfoDTO.peripheralsFunction3 }').split(",") : '',
						width:'150px',
						onSelect : function(newValue) {
							// 選中時處理請選擇項
							selectMultiple(newValue, "${stuff }_peripheralsFunction3");
						},
						onUnselect : function(newValue) {
							// 取消選中時處理請選擇項
							unSelectMultiple(newValue, "${stuff }_peripheralsFunction3");
						}
					});
					$('#${stuff }_peripheralsFunction3').addClass("easyui-combobox");
				}
				// LOGO
				if ($('#${stuff }_logoStyle').length > 0) {
					$('#${stuff }_logoStyle').combobox({
						editable:false, 
						valueField:'value',
						textField:'name',
						panelHeight:'auto',
						width:'150px'
					});
					$('#${stuff }_logoStyle').addClass("easyui-combobox");
				}
				// 是否開啟加密
				if ($('#${stuff }_isOpenEncrypt').length > 0) {
					$('#${stuff }_isOpenEncrypt').combobox({
						editable:false, 
						valueField:'value',
						textField:'name',
						panelHeight:'auto',
						width:'150px'
					});
					$('#${stuff }_isOpenEncrypt').addClass("easyui-combobox");
				}
				// 電子化繳費平台
				if ($('#${stuff }_electronicPayPlatform').length > 0) {
					$('#${stuff }_electronicPayPlatform').combobox({
						editable:false, 
						valueField:'value',
						textField:'name',
						panelHeight:'auto',
						width:'150px'
					});
					$('#${stuff }_electronicPayPlatform').addClass("easyui-combobox");
				}
				// 電子發票載具
				if ($('#${stuff }_electronicInvoice').length > 0) {
					$('#${stuff }_electronicInvoice').combobox({
						editable:false, 
						valueField:'value',
						textField:'name',
						panelHeight:'auto',
						width:'150px'
					});
					$('#${stuff }_electronicInvoice').addClass("easyui-combobox");
				}
				// 銀聯閃付
				if ($('#${stuff }_cupQuickPass').length > 0) {
					$('#${stuff }_cupQuickPass').combobox({
						editable:false, 
						valueField:'value',
						textField:'name',
						panelHeight:'auto',
						width:'150px'
					});
					$('#${stuff }_cupQuickPass').addClass("easyui-combobox");
				}
				<c:choose>
					<c:when test="${((not empty caseHandleInfoDTO.caseId and caseHandleInfoDTO.customerId eq gpCustomerId and (caseManagerFormDTO.caseCategory eq caseCategoryAttr.INSTALL.code or caseManagerFormDTO.caseCategory eq caseCategoryAttr.PROJECT.code or caseManagerFormDTO.caseCategory eq caseCategoryAttr.UPDATE.code)) or (logonUser.companyId eq gpCustomerId and (caseManagerFormDTO.caseCategory eq caseCategoryAttr.INSTALL.code or caseManagerFormDTO.caseCategory eq caseCategoryAttr.PROJECT.code or caseManagerFormDTO.caseCategory eq caseCategoryAttr.UPDATE.code)))}">
						$("#receiptTypeLabel").html("Receipt_type:<span class=\"red\">*</span>");
						if ( $('#${stuff }_receiptType').length > 0){
							$('#${stuff }_receiptType').combobox({
								editable:false, 
								required:true,
								valueField:'value',
								textField:'name',
								panelHeight:'auto',
								width:'150px',
								validType:'requiredDropList',
								invalidMessage:"請輸入Receipt_type"
							});
							$('#${stuff }_receiptType').addClass("easyui-combobox");
						}
					</c:when>
					<c:otherwise>
						// Receipt_type
						$("#receiptTypeLabel").html("Receipt_type:");
						if ( $('#${stuff }_receiptType').length > 0){
							$('#${stuff }_receiptType').combobox({
								editable:false, 
								valueField:'value',
								textField:'name',
								panelHeight:'auto',
								width:'150px',
							});
							$('#${stuff }_receiptType').addClass("easyui-combobox");
						}
					</c:otherwise>
				</c:choose>
				// Receipt_type
				/* if ( $('#${stuff }_receiptType').length > 0){
					$('#${stuff }_receiptType').combobox({
						editable:false, 
						valueField:'value',
						textField:'name',
						panelHeight:'auto',
						width:'150px',
					});
					$('#${stuff }_receiptType').addClass("easyui-combobox");
				} */
				/* if (caseCategory == '${caseCategoryAttr.OTHER.code}') {
					if ( $('#${stuff }_receiptType').length > 0){
						$('#${stuff }_receiptType').combobox({
							editable:false, 
							valueField:'value',
							textField:'name',
							panelHeight:'auto',
							width:'150px',
						});
						$('#${stuff }_receiptType').addClass("easyui-combobox");
					}
				} else {
					if ( $('#${stuff }_receiptType').length > 0){
						$('#${stuff }_receiptType').combobox({
							editable:false, 
							required:true,
							valueField:'value',
							textField:'name',
							panelHeight:'auto',
							width:'150px',
							validType:'requiredDropList',
							invalidMessage:"請輸入Receipt_type"
						});
						$('#${stuff }_receiptType').addClass("easyui-combobox");
					}
				} */
				// 寬頻連線
				if ($('#${stuff }_netVendorId').length > 0) {
					$('#${stuff }_netVendorId').combobox({
						editable:false, 
						valueField:'value',
						textField:'name',
						width:'150px'
					});
					$('#${stuff }_netVendorId').addClass("easyui-combobox");
				}
				// 本機IP
				localhostIp = $("#${stuff }_localhostIp");
				// Gateway
				gateway = $("#${stuff }_gateway");
				// Netmask
				netmask = $("#${stuff }_netmask");
			// 其他
			} else {
				// Receipt_type
				if ($('#${stuff }_receiptType_vo').length > 0) {
					$('#${stuff }_receiptType_vo').textbox();
				}
				// 刷卡機型
				if ($('#${stuff }_edcTypeName_vo').length > 0) {
					$('#${stuff }_edcTypeName_vo').textbox();
				}
				if(caseCategory == '${caseCategoryAttr.MERGE.code}'){
					// 軟體版本
					if ($('#${stuff }_softwareVersion').length > 0) {
						$('#${stuff }_softwareVersion').combobox({
							editable:false, 
							required:true,
							valueField:'value',
							textField:'name',
							width:'150px',
							validType:'requiredDropList',
							invalidMessage:"請輸入軟體版本"
						});
						$('#${stuff }_softwareVersion').addClass("easyui-combobox");
					}
				} else {
					// 軟體版本
					if ($('#${stuff }_softwareVersionName_vo').length > 0) {
						$('#${stuff }_softwareVersionName_vo').textbox();
					}
				}
				// 內建功能
				if ($('#${stuff }_builtInFeatureName_vo').length > 0) {
					$('#${stuff }_builtInFeatureName_vo').textbox();
				}
				// 雙模組模式
				if ($('#${stuff }_multiModuleName_vo').length > 0) {
					$('#${stuff }_multiModuleName_vo').textbox();
				}
				// 週邊設備1
				if ($('#${stuff }_peripheralsName_vo').length > 0) {
					$('#${stuff }_peripheralsName_vo').textbox();
				}
				// 週邊設備功能1
				if ($('#${stuff }_peripheralsFunctionName_vo').length > 0) {
					$('#${stuff }_peripheralsFunctionName_vo').textbox();
				}
				// ECR連線
				if ($('#${stuff }_ecrConnectionName_vo').length > 0) {
					$('#${stuff }_ecrConnectionName_vo').textbox();
				}
				// 週邊設備2
				if ($('#${stuff }_peripherals2Name_vo').length > 0) {
					$('#${stuff }_peripherals2Name_vo').textbox();
				}
				// 週邊設備功能2
				if ($('#${stuff }_peripheralsFunction2Name_vo').length > 0) {
					$('#${stuff }_peripheralsFunction2Name_vo').textbox();
				}
				// 連接方式
				if ($('#${stuff }_connectionTypeName_vo').length > 0) {
					$('#${stuff }_connectionTypeName_vo').textbox();
				}
				// 週邊設備3
				if ($('#${stuff }_peripherals3Name_vo').length > 0) {
					$('#${stuff }_peripherals3Name_vo').textbox();
				}
				// 週邊設備功能3
				if ($('#${stuff }_peripheralsFunction3Name_vo').length > 0) {
					$('#${stuff }_peripheralsFunction3Name_vo').textbox();
				}
				// LOGO
				if ($('#${stuff }_logoStyle_vo').length > 0) {
					$('#${stuff }_logoStyle_vo').textbox();
				}
				// 是否開啟加密
				if ($('#${stuff }_isOpenEncrypt_vo').length > 0) {
					$('#${stuff }_isOpenEncrypt_vo').textbox();
				}
				// 電子化繳費平台
				if ($('#${stuff }_electronicPayPlatform_vo').length > 0) {
					$('#${stuff }_electronicPayPlatform_vo').textbox();
				}
				// 電子發票載具
				if ($('#${stuff }_electronicInvoice_vo').length > 0) {
					$('#${stuff }_electronicInvoice_vo').textbox();
				}
				// 銀聯閃付
				if ($('#${stuff }_cupQuickPass_vo').length > 0) {
					$('#${stuff }_cupQuickPass_vo').textbox();
				}
				// 寬頻連線
				if ($('#${stuff }_netVendorName_vo').length > 0) {
					$('#${stuff }_netVendorName_vo').textbox();
				}
				// 本機IP
				localhostIp = $("#${stuff }_localhostIp_vo");
				// Gateway
				gateway = $("#${stuff }_gateway_vo");
				// Netmask
				netmask = $("#${stuff }_netmask_vo");
			}
			
			// 本機IP
			if (localhostIp.length > 0) {
				localhostIp.textbox();
				localhostIp.textbox('textbox').attr('maxlength', 50);
			}
			// Gateway
			if (gateway.length > 0) {
				gateway.textbox();
				gateway.textbox('textbox').attr('maxlength', 50);
			}
			// Netmask
			if (netmask.length > 0) {
				netmask.textbox();
				netmask.textbox('textbox').attr('maxlength', 50);
			}
		}
		
		/*
		刷卡機型聯動事件
		*/
		function edcTypeOnChange(newValue, oldValue){
			$("#${stuff }_builtInFeature").combobox('setValue', "");
			$("#${stuff }_softwareVersion").combobox('setValue', "");
			$("#${stuff }_connectionType").combobox('setValue', "");
			if (newValue != "") {
				//獲取內建功能下拉列表
				ajaxService.getBuiltInFeature(newValue, function(result){
					if (result != null) {
						$("#${stuff }_builtInFeature").combobox('loadData', initSelectMultiple(result));
					}
				});
				//獲取通訊模式下拉列表
				ajaxService.getConnectionType(newValue, function(result){
					if (result != null) {
						$("#${stuff }_connectionType").combobox('loadData', initSelectMultiple(result));
					}
				});
				//更新軟件版本下拉列表
				uploadSoftwareVersion($("#customerId").val(), newValue);
			} else {
				$("#${stuff }_builtInFeature").combobox('loadData', initSelectMultiple(null));
				$("#${stuff }_builtInFeature").combobox('setValue', "");
				
				$("#${stuff }_softwareVersion").combobox('loadData', initSelect(null));
				$("#${stuff }_softwareVersion").combobox('setValue', "");
				
				$("#${stuff }_connectionType").combobox('loadData', initSelectMultiple(null));
				$("#${stuff }_connectionType").combobox('setValue', "");
			}
		}
		
		/*
		根據客戶以及刷卡機行更新軟件版本下拉列表
		customerId：客戶ID
		edcType：刷卡機型ID
		*/
		function uploadSoftwareVersion(customerId, edcType, softwareVersion) {
			var tempSoftwareVersion = softwareVersion;
			//依據客戶以及刷卡機型獲取軟件版本
			$("#${stuff }_softwareVersion").combobox('setValue', "");
			if (customerId != "" && edcType != "") {
				ajaxService.getSoftwareVersions(customerId, edcType, 'N', function(result){
					if(result){
						$("#${stuff }_softwareVersion").combobox('loadData', initSelect(result));
						if(tempSoftwareVersion){
							var haveSoftware = false;
							for(var i = 0; i < result.length; i++){
								if(result[i].value == tempSoftwareVersion){
									haveSoftware = true;
									break;
								}
							}
							if(haveSoftware){
								$('#${stuff }_softwareVersion').combobox('setValue', tempSoftwareVersion);
							}
						} else {
							// Task #2516 軟體版本欄位，預設帶出 最新一筆資料(CREATED_DATE desc)
							if (result.length != 0) {
								var value = result[1]["value"];
								$("#${stuff }_softwareVersion").combobox("setValue", value);
							}
						}
					}
				});
			} else {
				$("#${stuff }_softwareVersion").combobox('loadData', initSelect(null));
			}
		}
		
		
		/*
		周邊設備聯動周邊設備功能事件
		newValue：異動之後的周邊設備ID
		oldValue：異動之前的周邊設備ID
		loadId：需要異動的周邊設備功能ID
		*/
		function linkageCombobox(newValue, oldValue, loadId){
			if (newValue != "") {
				//以及周邊設備獲取對應的周邊設備功能列表
				ajaxService.getBuiltInFeature(newValue,function(result){
					$("#${stuff }_"+loadId).combobox('setValue',"");
					if (result != null) {
						$("#${stuff }_"+loadId).combobox('loadData', initSelectMultiple(result));
					}
				});
			} else {
				$("#${stuff }_"+loadId).combobox('loadData', initSelectMultiple(null));
				$("#${stuff }_"+loadId).combobox('setValue', "");
			}
		}
		
		/*
		將按照DTID查詢的案件設備相關資料賦值
		result：案件資料
		*/
		function loadEdcLinkForChange(result){
			
			javascript:dwr.engine.setAsync(false);
			//案件類別為異動時，EDC欄位都為下拉列表，其餘都是文本框
			// Task #3392其他情況下，特店表頭為下拉列表，賦值觸發聯動事件
			if (${caseCategoryAttr.UPDATE.code eq caseCategory} || ${caseCategoryAttr.PROJECT.code eq caseCategory}) {
				
				// 調整帶入資料不存在下拉框的情況
				var haveEdcType = false;
				if(result!=null && result.edcType!=null){
					var edcTypeList = $("#${stuff }_edcType").combobox("getData");
					for(var i = 0; i < edcTypeList.length; i++){
						if(edcTypeList[i].value == result.edcType){
							haveEdcType = true;
							break;
						}
					}
					if(haveEdcType){
						$("#${stuff }_edcType").combobox("setValue", result.edcType);
					} else {
						$("#${stuff }_edcType").combobox("setValue", '');
					}
				// Bug #2968 重置下拉框選中
				} else {
					$("#${stuff }_edcType").combobox("setValue", '');
				}
				//獲取需要賦值的下拉列表
				$("#${stuff }_builtInFeature").combobox('select', '');
				if(result != null && !isEmpty(result.builtInFeature)){
					var array = (result.builtInFeature).split(",");
					if(array.length != 0){
						$("#${stuff }_builtInFeature").combobox('setValues', array);
					}
				} else {
					$("#${stuff }_builtInFeature").combobox('setValue', "");
				} 
				
				// 調整帶入資料不存在下拉框的情況
				if(result!=null && result.peripherals!=null){
					var peripheralsList = $("#${stuff }_peripherals").combobox("getData");
					var havePeripherals = false;
					for(var i = 0; i < peripheralsList.length; i++){
						if(peripheralsList[i].value == result.peripherals){
							havePeripherals = true;
							break;
						}
					}
					if(havePeripherals){
						$("#${stuff }_peripherals").combobox("setValue", result.peripherals);
					} else {
						$("#${stuff }_peripherals").combobox("setValue", '');
					}
				// Bug #2968 重置下拉框選中
				} else {
					$("#${stuff }_peripherals").combobox("setValue", '');
				}
				$("#${stuff }_peripheralsFunction").combobox('select', '');
				if(result != null && !isEmpty(result.peripheralsFunction)){
					var array = (result.peripheralsFunction).split(",");
					if(array.length != 0){
						$("#${stuff }_peripheralsFunction").combobox('setValues', array);
					}
				} else {
					$("#${stuff }_peripheralsFunction").combobox('setValue', "");
				}
				
				// 調整帶入資料不存在下拉框的情況
				if(result!=null && result.peripherals2!=null){
					var peripherals2List = $("#${stuff }_peripherals2").combobox("getData");
					var havePeripherals2 = false;
					for(var i = 0; i < peripherals2List.length; i++){
						if(peripherals2List[i].value == result.peripherals2){
							havePeripherals2 = true;
							break;
						}
					}
					if(havePeripherals2){
						$("#${stuff }_peripherals2").combobox("setValue", result.peripherals2);
					} else {
						$("#${stuff }_peripherals2").combobox("setValue", '');
					}
				// Bug #2968 重置下拉框選中
				} else {
					$("#${stuff }_peripherals2").combobox("setValue", '');
				}
				$("#${stuff }_peripheralsFunction2").combobox('select', '');
				if(result != null && !isEmpty(result.peripheralsFunction2)){
					var array = (result.peripheralsFunction2).split(",");
					if(array.length != 0){
						$("#${stuff }_peripheralsFunction2").combobox('setValues', array);
					}
				} else {
					$("#${stuff }_peripheralsFunction2").combobox('setValue', "");
				}
				// 連線方式
				if(result != null && !isEmpty(result.connectionType)){
					// 有EDC刷卡機型
					if(haveEdcType){
						var connectionTypeArray = (result.connectionType).split(",");
						if(connectionTypeArray.length != 0){
							$("#${stuff }_connectionType").combobox('setValues', connectionTypeArray);
						}
					// 無EDC刷卡機型
					} else {
						$("#${stuff }_connectionType").combobox('loadData', initSelectMultiple(null));
						$("#${stuff }_connectionType").combobox('setValue', "");
					}
				} else {
					$("#${stuff }_connectionType").combobox('setValue', "");
				}
			
				// 調整帶入資料不存在下拉框的情況
				if(result!=null && result.peripherals3!=null){
					var peripherals3List = $("#${stuff }_peripherals3").combobox("getData");
					var havePeripherals3 = false;
					for(var i = 0; i < peripherals3List.length; i++){
						if(peripherals3List[i].value == result.peripherals3){
							havePeripherals3 = true;
							break;
						}
					}
					if(havePeripherals3){
						$("#${stuff }_peripherals3").combobox("setValue", result.peripherals3);
					} else {
						$("#${stuff }_peripherals3").combobox("setValue", '');
					}
				// Bug #2968 重置下拉框選中
				} else {
					$("#${stuff }_peripherals3").combobox("setValue", '');
				}
				$("#${stuff }_peripheralsFunction3").combobox('select', '');
				if(result != null && !isEmpty(result.peripheralsFunction3)){
					var array = (result.peripheralsFunction3).split(",");
					if(array.length != 0){
						$("#${stuff }_peripheralsFunction3").combobox('setValues', array);
					}
				} else {
					$("#${stuff }_peripheralsFunction3").combobox('setValue', "");
				}
				
				// 軟體版本賦值
				// 調整帶入資料不存在下拉框的情況
				if(result!=null && result.softwareVersion!=null){
					// 有EDC刷卡機型
					if(haveEdcType){
						var softwareVersionList = $("#${stuff }_softwareVersion").combobox("getData");
						var haveSoftwareVersion = false;
						for(var i = 0; i < softwareVersionList.length; i++){
							if(softwareVersionList[i].value == result.softwareVersion){
								haveSoftwareVersion = true;
								break;
							}
						}
						if(haveSoftwareVersion){
							$("#${stuff }_softwareVersion").combobox("setValue", result.softwareVersion);
						} else {
							$("#${stuff }_softwareVersion").combobox("setValue", '');
						}
					// 無EDC刷卡機型
					} else {
						$("#${stuff }_softwareVersion").combobox('loadData', initSelect(null));
					}
				// Bug #2968 重置下拉框選中
				} else {
					$("#${stuff }_softwareVersion").combobox("setValue", '');
				}
				
				
			} else {
				//將數據賦值與EDC相應欄位
				$("#${stuff }_edcTypeName_vo").textbox("setValue", result==null?"":result.edcTypeName);
				//案件類別為拼機時，軟件版本顯示為下拉列表，其餘問文本框
				if (${caseCategoryAttr.MERGE.code eq caseCategory}) {
					uploadSoftwareVersion(result==null?"":result.customerId, result==null?"":result.edcType, result==null?"":result.softwareVersion);
				} else {
					$("#${stuff }_softwareVersionName_vo").textbox("setValue", result==null?"":result.softwareVersionName);
					$("#${stuff }_softwareVersion").val(result==null?"":result.softwareVersion);
				}
				//將數據賦值與EDC相應欄位
				$("#${stuff }_builtInFeatureName_vo").textbox("setValue", result==null?"":result.builtInFeatureName);
				$("#${stuff }_peripheralsName_vo").textbox("setValue", result==null?"":result.peripheralsName);
				$("#${stuff }_peripheralsFunctionName_vo").textbox("setValue", result==null?"":result.peripheralsFunctionName);
				$("#${stuff }_peripherals2Name_vo").textbox("setValue", result==null?"":result.peripherals2Name);
				$("#${stuff }_peripheralsFunction2Name_vo").textbox("setValue", result==null?"":result.peripheralsFunction2Name);	
				$("#${stuff }_connectionTypeName_vo").textbox("setValue", result==null?"":result.connectionTypeName);
				$("#${stuff }_peripherals3Name_vo").textbox("setValue", result==null?"":result.peripherals3Name);
				$("#${stuff }_peripheralsFunction3Name_vo").textbox("setValue", result==null?"":result.peripheralsFunction3Name);
				
				$("#${stuff }_edcType").val(result==null?"":result.edcType==null?"":result.edcType);
				$("#${stuff }_softwareVersion").val(result==null?"":result.softwareVersion);
				$("#${stuff }_builtInFeature").val(result==null?"":result.builtInFeature);
				
				$("#${stuff }_peripherals").val(result==null?"":result.peripherals);
				$("#${stuff }_peripheralsFunction").val(result==null?"":result.peripheralsFunction);
				
				$("#${stuff }_peripherals2").val( result==null?"":result.peripherals2);
				$("#${stuff }_peripheralsFunction2").val(result==null?"":result.peripheralsFunction2);		
				$("#${stuff }_connectionType").val(result==null?"":result.connectionType);
				$("#${stuff }_peripherals3").val(result==null?"":result.peripherals3);
				$("#${stuff }_peripheralsFunction3").val(result==null?"":result.peripheralsFunction3);
			}
			javascript:dwr.engine.setAsync(true);
			
		}
		/*
		將按照DTID查詢的案件資料賦值與EDC資訊
		result：案件資料
		*/
		function loadEdcInfoElement(result) {
			
			javascript:dwr.engine.setAsync(false);
			//案件類別為異動時，EDC欄位都為下拉列表，其餘都是文本框
			// Task #3392其他情況下，特店表頭為下拉列表，賦值觸發聯動事件
			if (${caseCategoryAttr.UPDATE.code eq caseCategory} || ${caseCategoryAttr.OTHER.code eq caseCategory} 
						|| ${caseCategoryAttr.PROJECT.code eq caseCategory}) {
				//將數據賦值與EDC相應欄位
			//	$("#${stuff }_edcType").combobox("setValue", result==null?"":result.edcType==null?"":result.edcType);
				// 調整帶入資料不存在下拉框的情況
				var haveEdcType = false;
				if(result!=null && result.edcType!=null){
					var edcTypeList = $("#${stuff }_edcType").combobox("getData");
					for(var i = 0; i < edcTypeList.length; i++){
						if(edcTypeList[i].value == result.edcType){
							haveEdcType = true;
							break;
						}
					}
					if(haveEdcType){
						$("#${stuff }_edcType").combobox("setValue", result.edcType);
					} else {
						$("#${stuff }_edcType").combobox("setValue", '');
					}
				// Bug #2968 重置下拉框選中
				} else {
					$("#${stuff }_edcType").combobox("setValue", '');
				}
				//獲取需要賦值的下拉列表
			//	$("#${stuff }_softwareVersion").combobox("setValue", result==null?"":result.softwareVersion==null?"":result.softwareVersion);
				$("#${stuff }_builtInFeature").combobox('select', '');
				if(result != null && !isEmpty(result.builtInFeature)){
					var array = (result.builtInFeature).split(",");
					if(array.length != 0){
						$("#${stuff }_builtInFeature").combobox('setValues', array);
					}
				} else {
					$("#${stuff }_builtInFeature").combobox('setValue', "");
				}
				$("#${stuff }_multiModule").combobox("setValue", result==null?"":result.multiModule==null?"":result.multiModule);
			//	$("#${stuff }_peripherals").combobox("setValue",result==null?"": result.peripherals==null?"":result.peripherals);
				// 調整帶入資料不存在下拉框的情況
				if(result!=null && result.peripherals!=null){
					var peripheralsList = $("#${stuff }_peripherals").combobox("getData");
					var havePeripherals = false;
					for(var i = 0; i < peripheralsList.length; i++){
						if(peripheralsList[i].value == result.peripherals){
							havePeripherals = true;
							break;
						}
					}
					if(havePeripherals){
						$("#${stuff }_peripherals").combobox("setValue", result.peripherals);
					} else {
						$("#${stuff }_peripherals").combobox("setValue", '');
					}
				// Bug #2968 重置下拉框選中
				} else {
					$("#${stuff }_peripherals").combobox("setValue", '');
				}
				$("#${stuff }_peripheralsFunction").combobox('select', '');
				if(result != null && !isEmpty(result.peripheralsFunction)){
					var array = (result.peripheralsFunction).split(",");
					if(array.length != 0){
						$("#${stuff }_peripheralsFunction").combobox('setValues', array);
					}
				} else {
					$("#${stuff }_peripheralsFunction").combobox('setValue', "");
				}
				//$("#${stuff }_peripheralsFunction").combobox('setValues', result==null?"":result.peripheralsFunction==null?"":result.peripheralsFunction);
				$("#${stuff }_ecrConnection").combobox("setValue", result==null?"":result.ecrConnection==null?"":result.ecrConnection);
			//	$("#${stuff }_peripherals2").combobox("setValue", result==null?"":result.peripherals2==null?"":result.peripherals2);
				// 調整帶入資料不存在下拉框的情況
				if(result!=null && result.peripherals2!=null){
					var peripherals2List = $("#${stuff }_peripherals2").combobox("getData");
					var havePeripherals2 = false;
					for(var i = 0; i < peripherals2List.length; i++){
						if(peripherals2List[i].value == result.peripherals2){
							havePeripherals2 = true;
							break;
						}
					}
					if(havePeripherals2){
						$("#${stuff }_peripherals2").combobox("setValue", result.peripherals2);
					} else {
						$("#${stuff }_peripherals2").combobox("setValue", '');
					}
				// Bug #2968 重置下拉框選中
				} else {
					$("#${stuff }_peripherals2").combobox("setValue", '');
				}
				$("#${stuff }_peripheralsFunction2").combobox('select', '');
				if(result != null && !isEmpty(result.peripheralsFunction2)){
					var array = (result.peripheralsFunction2).split(",");
					if(array.length != 0){
						$("#${stuff }_peripheralsFunction2").combobox('setValues', array);
					}
				} else {
					$("#${stuff }_peripheralsFunction2").combobox('setValue', "");
				}
	//			$("#${stuff }_connectionType").combobox("setValue", result==null?"":result.connectionType==null?"":result.connectionType);
				// 連線方式
				if(result != null && !isEmpty(result.connectionType)){
					// 有EDC刷卡機型
					if(haveEdcType){
						var connectionTypeArray = (result.connectionType).split(",");
						if(connectionTypeArray.length != 0){
							/* var connectionTypes = $("#${stuff }_connectionType").combobox("getData");
							var results = [];
							for (var i = 0; i < connectionTypeArray.length; i++) {
								for (var j = 0; j < connectionTypes.length; j++) {
									if (connectionTypeArray[i] == connectionTypes[j].value) {
										results.push(connectionTypeArray[j]);
									}
								}
							}
							if (results.length > 0) {
								$("#${stuff }_connectionType").combobox('setValues', results);
							} else {
								$("#${stuff }_connectionType").combobox('setValues', "");
							} */
							$("#${stuff }_connectionType").combobox('setValues', connectionTypeArray);
							
						}
					// 無EDC刷卡機型
					} else {
						$("#${stuff }_connectionType").combobox('loadData', initSelectMultiple(null));
						$("#${stuff }_connectionType").combobox('setValue', "");
					}
				} else {
					$("#${stuff }_connectionType").combobox('setValue', "");
				}
			
			//	$("#${stuff }_peripherals3").combobox("setValue", result==null?"":result.peripherals3==null?"":result.peripherals3);
				// 調整帶入資料不存在下拉框的情況
				if(result!=null && result.peripherals3!=null){
					var peripherals3List = $("#${stuff }_peripherals3").combobox("getData");
					var havePeripherals3 = false;
					for(var i = 0; i < peripherals3List.length; i++){
						if(peripherals3List[i].value == result.peripherals3){
							havePeripherals3 = true;
							break;
						}
					}
					if(havePeripherals3){
						$("#${stuff }_peripherals3").combobox("setValue", result.peripherals3);
					} else {
						$("#${stuff }_peripherals3").combobox("setValue", '');
					}
				// Bug #2968 重置下拉框選中
				} else {
					$("#${stuff }_peripherals3").combobox("setValue", '');
				}
				$("#${stuff }_peripheralsFunction3").combobox('select', '');
				if(result != null && !isEmpty(result.peripheralsFunction3)){
					var array = (result.peripheralsFunction3).split(",");
					if(array.length != 0){
						$("#${stuff }_peripheralsFunction3").combobox('setValues', array);
					}
				} else {
					$("#${stuff }_peripheralsFunction3").combobox('setValue', "");
				}
				
				// 軟體版本賦值
			//	uploadSoftwareVersion(result==null?"":result.customerId, result==null?"":result.edcType, result==null?"":result.softwareVersion);
				// 調整帶入資料不存在下拉框的情況
				if(result!=null && result.softwareVersion!=null){
					// 有EDC刷卡機型
					if(haveEdcType){
						var softwareVersionList = $("#${stuff }_softwareVersion").combobox("getData");
						var haveSoftwareVersion = false;
						for(var i = 0; i < softwareVersionList.length; i++){
							if(softwareVersionList[i].value == result.softwareVersion){
								haveSoftwareVersion = true;
								break;
							}
						}
						if(haveSoftwareVersion){
							$("#${stuff }_softwareVersion").combobox("setValue", result.softwareVersion);
						} else {
							$("#${stuff }_softwareVersion").combobox("setValue", '');
						}
					// 無EDC刷卡機型
					} else {
						$("#${stuff }_softwareVersion").combobox('loadData', initSelect(null));
					}
				// Bug #2968 重置下拉框選中
				} else {
					$("#${stuff }_softwareVersion").combobox("setValue", '');
				}
				
				$("#${stuff }_netVendorId").combobox("setValue", result==null?"":result.netVendorId==null?"":result.netVendorId);
				$("#${stuff }_localhostIp").textbox("setValue", result==null?"":result.localhostIp);
				$("#${stuff }_netmask").textbox("setValue", result==null?"":result.netmask);
				$("#${stuff }_gateway").textbox("setValue", result==null?"":result.gateway);
				
				$("#${stuff }_electronicInvoice").combobox("setValue", result==null?"":(result.electronicInvoice==null || result.electronicInvoice==" ")?"":result.electronicInvoice);
				$("#${stuff }_cupQuickPass").combobox("setValue", result==null?"":(result.cupQuickPass==null || result.cupQuickPass==" ")?"":result.cupQuickPass);
				$("#${stuff }_logoStyle").combobox("setValue", result==null?"":result.logoStyle==null ? "":result.logoStyle);
				$("#${stuff }_isOpenEncrypt").combobox("setValue", result==null?"":(result.isOpenEncrypt==null || result.isOpenEncrypt==" ")?"":result.isOpenEncrypt);
				$("#${stuff }_electronicPayPlatform").combobox("setValue", result==null?"":(result.electronicPayPlatform==null || result.electronicPayPlatform==" ")?"":result.electronicPayPlatform);
			
				
				// 調整帶入資料不存在下拉框的情況
				if(result!=null && result.receiptType!=null){
					var receiptTypes = $("#${stuff }_receiptType").combobox("getData");
					var haveReceiptTypes = false;
					for(var i = 0; i < receiptTypes.length; i++){
						if(receiptTypes[i].value == result.receiptType){
							haveReceiptTypes = true;
							break;
						}
					}
					if(haveReceiptTypes){
						$("#${stuff }_receiptType").combobox("setValue", result.receiptType);
					} else {
						$("#${stuff }_receiptType").combobox("setValue", '');
					}
				// Bug #2968 重置下拉框選中
				} else {
					$("#${stuff }_receiptType").combobox("setValue", '');
				}
			
			} else {
				//將數據賦值與EDC相應欄位
				$("#${stuff }_edcTypeName_vo").textbox("setValue", result==null?"":result.edcTypeName);
				//案件類別為拼機時，軟件版本顯示為下拉列表，其餘問文本框
				if (${caseCategoryAttr.MERGE.code eq caseCategory}) {
					uploadSoftwareVersion(result==null?"":result.customerId, result==null?"":result.edcType, result==null?"":result.softwareVersion);
				//	$("#${stuff }_softwareVersion").combobox("setValue", result==null?"":result.softwareVersion==null?"":result.softwareVersion);
				} else {
					$("#${stuff }_softwareVersionName_vo").textbox("setValue", result==null?"":result.softwareVersionName);
					$("#${stuff }_softwareVersion").val(result==null?"":result.softwareVersion);
				}
				if (${caseCategoryAttr.PROJECT.code eq caseCategory}) {
					// 調整帶入資料不存在下拉框的情況
					if(result!=null && result.receiptType!=null){
						var receiptTypes = $("#${stuff }_receiptType").combobox("getData");
						var haveReceiptTypes = false;
						for(var i = 0; i < receiptTypes.length; i++){
							if(receiptTypes[i].value == result.receiptType){
								haveReceiptTypes = true;
								break;
							}
						}
						if(haveReceiptTypes){
							$("#${stuff }_receiptType").combobox("setValue", result.receiptType);
						} else {
							$("#${stuff }_receiptType").combobox("setValue", '');
						}
					// Bug #2968 重置下拉框選中
					} else {
						$("#${stuff }_receiptType").combobox("setValue", '');
					}
				} else {
					$("#${stuff }_receiptType_vo").textbox("setValue", result==null?"":result.receiptTypeName);
					$("#${stuff }_receiptType").val(result==null?"":result.receiptType);
				}
				//將數據賦值與EDC相應欄位
				$("#${stuff }_builtInFeatureName_vo").textbox("setValue", result==null?"":result.builtInFeatureName);
				$("#${stuff }_multiModuleName_vo").textbox("setValue", result==null?"":result.multiModuleName);
				$("#${stuff }_peripheralsName_vo").textbox("setValue", result==null?"":result.peripheralsName);
				$("#${stuff }_peripheralsFunctionName_vo").textbox("setValue", result==null?"":result.peripheralsFunctionName);
				$("#${stuff }_ecrConnectionName_vo").textbox("setValue", result==null?"":result.ecrConnectionName);
				$("#${stuff }_peripherals2Name_vo").textbox("setValue", result==null?"":result.peripherals2Name);
				$("#${stuff }_peripheralsFunction2Name_vo").textbox("setValue", result==null?"":result.peripheralsFunction2Name);	
				$("#${stuff }_connectionTypeName_vo").textbox("setValue", result==null?"":result.connectionTypeName);
				$("#${stuff }_peripherals3Name_vo").textbox("setValue", result==null?"":result.peripherals3Name);
				$("#${stuff }_peripheralsFunction3Name_vo").textbox("setValue", result==null?"":result.peripheralsFunction3Name);
				$("#${stuff }_netVendorName_vo").textbox("setValue", result==null?"":result.netVendorName);
				
				$("#${stuff }_edcType").val(result==null?"":result.edcType==null?"":result.edcType);
				$("#${stuff }_softwareVersion").val(result==null?"":result.softwareVersion);
				$("#${stuff }_builtInFeature").val(result==null?"":result.builtInFeature);
				$("#${stuff }_multiModule").val(result==null?"":result.multiModule);
				$("#${stuff }_peripherals").val(result==null?"":result.peripherals);
				$("#${stuff }_peripheralsFunction").val(result==null?"":result.peripheralsFunction);
				$("#${stuff }_ecrConnection").val(result==null?"":result.ecrConnection);
				$("#${stuff }_peripherals2").val( result==null?"":result.peripherals2);
				$("#${stuff }_peripheralsFunction2").val(result==null?"":result.peripheralsFunction2);		
				$("#${stuff }_connectionType").val(result==null?"":result.connectionType);
				$("#${stuff }_peripherals3").val(result==null?"":result.peripherals3);
				$("#${stuff }_peripheralsFunction3").val(result==null?"":result.peripheralsFunction3);
				$("#${stuff }_netVendorId").val(result==null?"":result.netVendorId);
				
				$("#${stuff }_localhostIp_vo").textbox("setValue", result==null?"":result.localhostIp);
				$("#${stuff }_localhostIp").val(result==null?"":result.localhostIp);
				$("#${stuff }_netmask_vo").textbox("setValue", result==null?"":result.netmask);
				$("#${stuff }_netmask").val(result==null?"":result.netmask);
				$("#${stuff }_gateway_vo").textbox("setValue", result==null?"":result.gateway);
				$("#${stuff }_gateway").val(result==null?"":result.gateway);
				
				$("#${stuff }_electronicInvoice_vo").textbox("setValue", result==null?"":fomatterYesOrNo(result.electronicInvoice));
				$("#${stuff }_electronicInvoice").val(result==null?"":result.electronicInvoice);
				$("#${stuff }_cupQuickPass_vo").textbox("setValue", result==null?"":fomatterYesOrNo(result.cupQuickPass));
				$("#${stuff }_cupQuickPass").val(result==null?"":result.cupQuickPass);
				
				$("#${stuff }_logoStyle_vo").textbox("setValue", result==null?"":fomatterLogoStyle(result.logoStyle));
				$("#${stuff }_logoStyle").val(result==null?"":result.logoStyle);
				$("#${stuff }_isOpenEncrypt_vo").textbox("setValue", result==null?"":fomatterYesOrNo(result.isOpenEncrypt));
				$("#${stuff }_isOpenEncrypt").val(result==null?"":result.isOpenEncrypt);
				$("#${stuff }_electronicPayPlatform_vo").textbox("setValue", result==null?"":fomatterYesOrNo(result.electronicPayPlatform));
				$("#${stuff }_electronicPayPlatform").val(result==null?"":result.electronicPayPlatform);
			}
			javascript:dwr.engine.setAsync(true);
		}
		
		/*
		在保存建案資料時，判斷EDC數據是否填寫正確
		*/
		function checkEDCInfo(){
			$("#caseDialogMsg").html("");
			//核檢周邊設備是否重複
			var peripherals = [];
			if (${caseCategoryAttr.INSTALL.code eq caseCategory || caseCategoryAttr.UPDATE.code eq caseCategory
					|| caseCategoryAttr.OTHER.code eq caseCategory || caseCategoryAttr.PROJECT.code eq caseCategory} ) {
				var peripherals1 = $("#${stuff }_peripherals").combobox("getText");
				var peripherals1Value = $("#${stuff }_peripherals").combobox("getValue");
				if(peripherals1 && peripherals1 != '' && peripherals1Value && peripherals1Value != ''){
					peripherals.push(peripherals1);
				}
				var peripherals2 = $("#${stuff }_peripherals2").combobox("getText");
				var peripherals2Value = $("#${stuff }_peripherals2").combobox("getValue");
				if(peripherals2 && peripherals2 != '' && peripherals2Value && peripherals2Value != ''){
					peripherals.push(peripherals2);
				}
				var peripherals3 = $("#${stuff }_peripherals3").combobox("getText");
				var peripherals3Value = $("#${stuff }_peripherals3").combobox("getValue");
				if(peripherals3 && peripherals3 != '' && peripherals3Value && peripherals3Value != ''){
					peripherals.push(peripherals3);
				}
				//核檢周邊設備是否重複
				peripherals = peripherals.sort();
				for(var i=0; i < peripherals.length; i++){
					if(i == (peripherals.length - 1)){
						break;
					}
					if (peripherals[i] == peripherals[i+1]){
						$("#caseDialogMsg").html("週邊設備選項"+peripherals[i]+"已重覆");
						handleScrollTop('createDlg','caseDialogMsg');
						handleScrollTop('editDlg','caseDialogMsg');
						return false;
					}
				}
				if (${caseCategoryAttr.INSTALL.code eq caseCategory || caseCategoryAttr.UPDATE.code eq caseCategory
						|| caseCategoryAttr.PROJECT.code eq caseCategory} ) {
					// 核檢內建功能或週邊設備功與交易參數
					var builtInFeature = $("#${stuff }_builtInFeature").combobox("getValues");
					var peripheralsFunction = $("#${stuff }_peripheralsFunction").combobox("getValues");
					var peripheralsFunction2 = $("#${stuff }_peripheralsFunction2").combobox("getValues");
					var peripheralsFunction3 = $("#${stuff }_peripheralsFunction3").combobox("getValues");
					var isCup = false;
					var isRealCup = false;
					var isSmartpay = false;
					var isReturnTransaction = false;
					var isJcb = false;
					// 判斷一般參數是否含有CUP
					var rows = $("#transDataGrid").datagrid("getRows");
					if(rows && rows.length != 0){
						for (var i=0; i<rows.length; i++){
							if ((rows[i].transactionType == '${transactionCategoryAttr.CUP.code}')
									|| (rows[i].transactionType == '${transactionCategoryAttr.SMART_PAY.code}')){
								isCup = true;
								if(rows[i].transactionType == '${transactionCategoryAttr.CUP.code}'){
									isRealCup = true;
								}
								if(rows[i].transactionType == '${transactionCategoryAttr.SMART_PAY.code}'){
									if(rows[i].returnTransaction == 'V'){
										isReturnTransaction = true;
									}
									isSmartpay = true;
								}
							}
							// #3523
							if((rows[i].transactionType == '${transactionCategoryAttr.COMMON_VM.code}')
									|| (rows[i].transactionType == '${transactionCategoryAttr.COMMON_VMJ.code}')
									|| (rows[i].transactionType == '${transactionCategoryAttr.COMMON_VMJU.code}')){
								if(rows[i].jcb == 'V'){
									isJcb = true;
								}
							}
						}
						var count = 0;
						// 內建功能
						if(builtInFeature && builtInFeature != '' && builtInFeature.toString().contains('${iAtomsConstants.SUPPORTED_FUNCTION_PINPAD}')){
							count ++;
						}
						// 週邊設備功能1
						if(peripheralsFunction && peripheralsFunction != '' && peripheralsFunction.toString().contains('${iAtomsConstants.SUPPORTED_FUNCTION_PINPAD}')){
							count ++;
						}
						// 週邊設備功能2
						if(peripheralsFunction2 && peripheralsFunction2 != '' && peripheralsFunction2.toString().contains('${iAtomsConstants.SUPPORTED_FUNCTION_PINPAD}')){
							count ++;
						}
						// 週邊設備功能3
						if(peripheralsFunction3 && peripheralsFunction3 != '' && peripheralsFunction3.toString().contains('${iAtomsConstants.SUPPORTED_FUNCTION_PINPAD}')){
							count ++;
						}
						// 有CUP
						if(isCup){
							// 沒有選Pinpad設備及功能
							if(count == 0){
								$("#caseDialogMsg").html("選取CUP或Smartpay交易類別，要選取Pinpad設備及功能");
								handleScrollTop('createDlg','caseDialogMsg');
								handleScrollTop('editDlg','caseDialogMsg');
								return false;
							}
							// 沒有有CUP
						} else {
							// 有選Pinpad設備及功能
							if(count != 0){
								$("#caseDialogMsg").html("未選取CUP或Smartpay交易類別，不可選取Pinpad設備及功能");
								handleScrollTop('createDlg','caseDialogMsg');
								handleScrollTop('editDlg','caseDialogMsg');
								return false;
							}
						}
						//Task #3525裝機件客戶=陽信、彰銀,若「交易類別有CUP」時，是否開啟加密需為”是”，否則，錯誤訊息「陽信銀行、彰化銀行，交易有CUP，是否開啟加密需為”是”」
						if(${caseCategoryAttr.INSTALL.code eq caseCategory } 
								&& ('${sybCustomerId}' == $("#${stuff }_customer").combobox("getValue")
										||'${chbCustomerId}' == $("#${stuff }_customer").combobox("getValue"))){
							var isOpenEncrypt = $("#${stuff }_isOpenEncrypt").combobox("getValue");
							//Task #3525裝機件客戶=陽信、彰銀,若「交易類別有CUP」時，是否開啟加密需為”是”，否則，錯誤訊息「陽信銀行、彰化銀行，交易有CUP，是否開啟加密需為”是”」
							if(isRealCup && isOpenEncrypt!='Y'){
								$("#caseDialogMsg").html("陽信銀行、彰化銀行，交易有CUP，是否開啟加密需為”是”");
								handleScrollTop('createDlg','caseDialogMsg');
								handleScrollTop('editDlg','caseDialogMsg');
								return false;
							}
							//Task #3525裝機件客戶=陽信、彰銀,若「交易類別有Smartpay且項目有退貨交易」時，是否開啟加密需為”是”，否則，錯誤訊息「陽信銀行、彰化銀行，交易有Smartpay退貨，是否開啟加密需為”是”」
							if(isSmartpay && isReturnTransaction && isOpenEncrypt!='Y'){
								$("#caseDialogMsg").html("陽信銀行、彰化銀行，交易有Smartpay退貨，是否開啟加密需為”是”");
								handleScrollTop('createDlg','caseDialogMsg');
								handleScrollTop('editDlg','caseDialogMsg');
								return false;
							}
							//Task #3525裝機件客戶=陽信、彰銀,若「交易類別無CUP」及「無Smartpay退貨交易」時，是否開啟加密需為”否”，否則，錯誤訊息「陽信銀行、彰化銀行，無CUP與無Smartpay退貨，是否開啟加密需為”否”」
							if(!isRealCup && !isReturnTransaction && isOpenEncrypt!='N'){
								$("#caseDialogMsg").html("陽信銀行、彰化銀行，無CUP與無Smartpay退貨，是否開啟加密需為”否”");
								handleScrollTop('createDlg','caseDialogMsg');
								handleScrollTop('editDlg','caseDialogMsg');
								return false;
							}
						}
						//Task #3522裝機件客戶=陽信時,若交易類別有Smartpay，但週邊(1~3)無TSAM卡，錯誤訊息「陽信銀行選取Smartpay交易類別，週邊需有TSAM卡」
						if(${caseCategoryAttr.INSTALL.code eq caseCategory } 
							&& '${sybCustomerId}' == $("#${stuff }_customer").combobox("getValue")){
							//週邊設備1 2 3
							var peripherals = $("#${stuff }_peripherals").combobox("getText")
							var peripherals2 = $("#${stuff }_peripherals2").combobox("getText")
							var peripherals3 = $("#${stuff }_peripherals3").combobox("getText")
							if(isSmartpay && peripherals != 'T-SAM卡' && peripherals2 != 'T-SAM卡' && peripherals3 != 'T-SAM卡'){
								$("#caseDialogMsg").html("陽信銀行選取Smartpay交易類別，週邊需有TSAM卡");
								handleScrollTop('createDlg','caseDialogMsg');
								handleScrollTop('editDlg','caseDialogMsg');
								return false;
							}
						}
						//Task #3523 裝機件客戶=陽信、上銀時,一般交易(VM、VMJ、VMJU)類別之JCB(免簽)$700項目不可以勾選，錯誤訊息「陽信銀行、上海商銀，選取一般交易類別，JCB$700項目不可以勾選」
						if(${caseCategoryAttr.INSTALL.code eq caseCategory } 
							&& ('${sybCustomerId}' == $("#${stuff }_customer").combobox("getValue")
								||'${scsbCustomerId}' == $("#${stuff }_customer").combobox("getValue"))){
							if(isJcb){
								$("#caseDialogMsg").html("陽信銀行、上海商銀，選取一般交易類別，JCB$700項目不可以勾選");
								handleScrollTop('createDlg','caseDialogMsg');
								handleScrollTop('editDlg','caseDialogMsg');
								return false;
							}
						}
						//Task #3524  裝機件客戶=陽信、上銀、彰銀時,若「交易類別無CUP」或「交易類別有CUP但內建功能或週邊功能無Dongle」，則銀聯閃付需為”否” ，錯誤訊息「陽信銀行、上海商銀、彰化銀行，「交易類別無CUP」或「交易類別有CUP但內建功能或週邊功能無Dongle」，銀聯閃付需為”否”」
						if(${caseCategoryAttr.INSTALL.code eq caseCategory } 
							&& ('${sybCustomerId}' == $("#${stuff }_customer").combobox("getValue")
								||'${scsbCustomerId}' == $("#${stuff }_customer").combobox("getValue")
								||'${chbCustomerId}' == $("#${stuff }_customer").combobox("getValue"))){
							//內建功能 週邊功能1 2 3
							var builtInFeature = $("#${stuff }_builtInFeature").combobox("getValue")
							var peripheralsFunction = $("#${stuff }_peripheralsFunction").combobox("getValue")
							var peripheralsFunction2 = $("#${stuff }_peripheralsFunction2").combobox("getValue")
							var peripheralsFunction3 = $("#${stuff }_peripheralsFunction3").combobox("getValue")
							//銀聯閃付
							var cupQuickPass = $("#${stuff }_cupQuickPass").combobox("getValue");
							if((!isRealCup || (isRealCup && builtInFeature != 'Dongle' && peripheralsFunction != 'Dongle'
									&& peripheralsFunction2 != 'Dongle' && peripheralsFunction3 != 'Dongle'))
									&& cupQuickPass !='N'){
								$("#caseDialogMsg").html("陽信銀行、上海商銀、彰化銀行，「交易類別無CUP」或「交易類別有CUP但內建功能或週邊功能無Dongle」，銀聯閃付需為”否”");
								handleScrollTop('createDlg','caseDialogMsg');
								handleScrollTop('editDlg','caseDialogMsg');
								return false;
							}
						}
					}
				}
				// 支援功能集合的json
				var supportFun = ${supportedFunStr};
				var tempSupportFun;
				if(supportFun){
					var count = 0;
					var repeatName = [];
					for(var i = 0; i < supportFun.length; i++){
						count = 0;
						// 內建功能
						if(builtInFeature && builtInFeature != '' && builtInFeature.contains(supportFun[i].value)){
							count ++;
						}
						// 週邊設備功能1
						if(peripheralsFunction && peripheralsFunction != '' && peripheralsFunction.contains(supportFun[i].value)){
							count ++;
						}
						// 週邊設備功能2
						if(peripheralsFunction2 && peripheralsFunction2 != '' && peripheralsFunction2.contains(supportFun[i].value)){
							count ++;
						}
						// 週邊設備功能3
						if(peripheralsFunction3 && peripheralsFunction3 != '' && peripheralsFunction3.contains(supportFun[i].value)){
							count ++;
						}
						// 如果至少兩項包含此支援功能則添加到重複名稱列表裡面
						if(count >= 2){
							repeatName.push(supportFun[i].name);
						}
					}
					if(repeatName.length > 0){
						$("#caseDialogMsg").html("設備功能" + repeatName.toString() + "已重覆");
						handleScrollTop('createDlg','caseDialogMsg');
						handleScrollTop('editDlg','caseDialogMsg');
						return false;
					}
				}
				if (${caseCategoryAttr.INSTALL.code eq caseCategory } && ('${gpCustomerId}' == $("#${stuff }_customer").combobox("getValue"))) {
					var checkParam = new Object();
					var isError = false;
					var msg = null;
					checkParam.${stuff }_connectionTypeName = $("#${stuff }_connectionType").combobox("getText");//鏈接方式
					
					checkParam.${stuff }_edcTypeName = $("#${stuff }_edcType").combobox("getText");//刷卡機型
					if (!isEmpty($("#${stuff }_builtInFeature").combobox("getValue"))) {
						checkParam.${stuff }_builtInFeature = $("#${stuff }_builtInFeature").combobox("getValues").toString();//刷卡機型
						checkParam.${stuff }_builtInFeatureName = $("#${stuff }_builtInFeature").combobox("getText");//刷卡機型
					}
					//checkParam.${stuff }_builtInFeature = $("#${stuff }_builtInFeature").combobox("getValues");//內建功能
					checkParam.${stuff }_peripheralsName = $("#${stuff }_peripherals").combobox("getText");
					checkParam.${stuff }_peripherals2Name = $("#${stuff }_peripherals2").combobox("getText");
					checkParam.${stuff }_peripherals3Name = $("#${stuff }_peripherals3").combobox("getText");
					if (!isEmpty($("#${stuff }_peripheralsFunction").combobox("getValue"))) {
						checkParam.${stuff }_peripheralsFunction = $("#${stuff }_peripheralsFunction").combobox("getValues").toString();
						checkParam.${stuff }_peripheralsFunctionName = $("#${stuff }_peripheralsFunction").combobox("getText");
					}
					if (!isEmpty($("#${stuff }_peripheralsFunction2").combobox("getValue"))) {
						checkParam.${stuff }_peripheralsFunction2 = $("#${stuff }_peripheralsFunction2").combobox("getValues").toString();
						checkParam.${stuff }_peripheralsFunction2Name = $("#${stuff }_peripheralsFunction2").combobox("getText");
					}
					if (!isEmpty($("#${stuff }_peripheralsFunction3").combobox("getValue"))) {
						checkParam.${stuff }_peripheralsFunction3 = $("#${stuff }_peripheralsFunction3").combobox("getValues").toString();
						checkParam.${stuff }_peripheralsFunction3Name = $("#${stuff }_peripheralsFunction3").combobox("getText");
					}
					//checkParam.${stuff }_peripheralsFunction = $("#${stuff }_peripheralsFunction").combobox("getValues");
					//checkParam.${stuff }_peripheralsFunction2 = $("#${stuff }_peripheralsFunction2").combobox("getValues");
					//checkParam.${stuff }_peripheralsFunction3 = $("#${stuff }_peripheralsFunction3").combobox("getValues");
					checkParam.${stuff }_aoName = $("#${stuff }_merAoName").val();
					checkParam.${stuff }_receiptType = $("#${stuff }_receiptType").combobox("getValue");
					checkParam.caseTransactionParameters = getTradingParametersData();
					$.ajax({
						url: "${contextPath}/caseHandle.do?actionId=checkGpInfo",
						async: false,
						data: checkParam,
						type:'post', 
						cache:false, 
						dataType:'json', 
						success:function(json){
							if (!json.success) {
								isError = true;
								msg = json.msg;
							}
						},
						error:function(){
							isError = true;
							msg = json.msg;
						}
					});
					if (isError) {
						$("#caseDialogMsg").html(msg);
						handleScrollTop('createDlg','caseDialogMsg');
						handleScrollTop('editDlg','caseDialogMsg');
						return false;
					}
				}
				return true;
			} else {
				var builtInFeature = $("#${stuff }_builtInFeature").val();
				var peripheralsFunction = $("#${stuff }_peripheralsFunction").val();
				var peripheralsFunction2 = $("#${stuff }_peripheralsFunction2").val();
				var peripheralsFunction3 = $("#${stuff }_peripheralsFunction3").val();
				var isCup = false;
				// 判斷一般參數是否含有CUP
				var rows = $("#transDataGrid").datagrid("getRows");
				if(rows && rows.length != 0){
					for (var i=0; i<rows.length; i++){
						if ((rows[i].transactionType == '${transactionCategoryAttr.CUP.code}')
								|| (rows[i].transactionType == '${transactionCategoryAttr.SMART_PAY.code}')){
							isCup = true;
							break;
						}
					}
					var count = 0;
					// 內建功能
					if(builtInFeature && builtInFeature != '' && builtInFeature.toString().contains('${iAtomsConstants.SUPPORTED_FUNCTION_PINPAD}')){
						count ++;
					}
					// 週邊設備功能1
					if(peripheralsFunction && peripheralsFunction != '' && peripheralsFunction.toString().contains('${iAtomsConstants.SUPPORTED_FUNCTION_PINPAD}')){
						count ++;
					}
					// 週邊設備功能2
					if(peripheralsFunction2 && peripheralsFunction2 != '' && peripheralsFunction2.toString().contains('${iAtomsConstants.SUPPORTED_FUNCTION_PINPAD}')){
						count ++;
					}
					// 週邊設備功能3
					if(peripheralsFunction3 && peripheralsFunction3 != '' && peripheralsFunction3.toString().contains('${iAtomsConstants.SUPPORTED_FUNCTION_PINPAD}')){
						count ++;
					}
					// 有CUP
					if(isCup){
						// 沒有選Pinpad設備及功能
						if(count == 0){
							$("#caseDialogMsg").html("選取CUP或Smartpay交易類別，要選取Pinpad設備及功能");
							handleScrollTop('createDlg','caseDialogMsg');
							handleScrollTop('editDlg','caseDialogMsg');
							return false;
						}
						// 沒有有CUP
					} else {
						// 有選Pinpad設備及功能
						if(count != 0){
							$("#caseDialogMsg").html("未選取CUP或Smartpay交易類別，不可選取Pinpad設備及功能");
							handleScrollTop('createDlg','caseDialogMsg');
							handleScrollTop('editDlg','caseDialogMsg');
							return false;
						}
					}
				}
				return true;
			}
		}
	</script>
