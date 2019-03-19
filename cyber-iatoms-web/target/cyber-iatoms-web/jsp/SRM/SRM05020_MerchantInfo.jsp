<%-- 
	特點資料元件
	author：carrieDuan 
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="cafe.core.util.StringUtils"%>
<%@include file="/jsp/common/taglibs.jsp"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.IAtomsConstants"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO"%>
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$CASE_CATEGORY" var="caseCategoryAttr" />
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$CASE_STATUS" var="caseStatusAttr" />

<%--
	案件formDTO 需傳入isCustomerService-是否為客服，isCustomer--是否為客戶，
	處理方式有默認值，則需傳入defaultProcessType-處理方式默認值
 --%>
<tiles:useAttribute id="caseManagerFormDTO" name="caseManagerFormDTO" classname="com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO"/>
<%-- 客戶下拉列表 List<Parameter> --%>
<tiles:useAttribute id="customers" name="customers" classname="java.util.List" ignore="true"/>

<%--案件類別 --%>
<tiles:useAttribute id="caseCategory" name="caseCategory" classname="java.lang.String" ignore="true"/>
<%-- VIP列表 List<Parameter> --%>
<tiles:useAttribute id="isVipList" name="isVipList" classname="java.util.List"/>
<%-- 營業地址-市區 List<Parameter> --%>
<tiles:useAttribute id="locationList" name="locationList" classname="java.util.List"/>
<%-- 案件處理DTO  SrmCaseHandleInfoDTO--%>
<tiles:useAttribute id="caseHandleInfoDTO" name="caseHandleInfoDTO" classname="com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO" ignore="true" />

<%-- 特店特店表頭 --%>
<tiles:useAttribute id="editMerchantHeaders" name="editMerchantHeaders" classname="java.util.List" ignore="true"/>

<tiles:useAttribute id="contextPathMerchant" name="contextPathMerchant" classname="java.lang.String" ignore="true"/>

<%-- 縣市聯動郵遞區號處理(裝機) --%>
<tiles:useAttribute id="installedPostCodes" name="installedPostCodes" classname="java.util.List" ignore="true"/>
<%-- 縣市聯動郵遞區號處理(聯系) --%>
<tiles:useAttribute id="contactPostCodes" name="contactPostCodes" classname="java.util.List" ignore="true"/>
<%-- 縣市聯動郵遞區號處理(營業) --%>
<tiles:useAttribute id="locationPostCodes" name="locationPostCodes" classname="java.util.List" ignore="true"/>
<%
	if (caseHandleInfoDTO == null) {
		caseHandleInfoDTO = new SrmCaseHandleInfoDTO();
		if (caseCategory.equals(IAtomsConstants.CASE_CATEGORY.INSTALL.getCode())) {
			caseHandleInfoDTO.setIsBussinessAddress(IAtomsConstants.YES);
			caseHandleInfoDTO.setIsBussinessContactPhone(IAtomsConstants.YES);
			caseHandleInfoDTO.setIsBussinessContact(IAtomsConstants.YES);
			caseHandleInfoDTO.setIsBussinessContactMobilePhone(IAtomsConstants.YES);
			caseHandleInfoDTO.setIsBussinessContactEmail(IAtomsConstants.YES);
		} else if (caseCategory.equals(IAtomsConstants.CASE_CATEGORY.UNINSTALL.getCode())) {
			caseHandleInfoDTO.setContactIsBussinessContact(IAtomsConstants.YES);
			caseHandleInfoDTO.setContactIsBussinessContactPhone(IAtomsConstants.YES);
		}
	}
%>
<c:set var="caseHandleInfoDTO" value="<%=caseHandleInfoDTO %>" scope="page"></c:set>
<c:set var="stuff" value="case" scope="page"></c:set>
<c:set var="gpCustomerId" value="<%=caseManagerFormDTO.getGpCustomerId() %>" scope="page"></c:set>

	<div id="special">
		<table style="width: 100%">
			<tr>
				<td width="10%">特店代號: ${caseCategoryAttr.PROJECT.code eq caseCategory or caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.OTHER.code eq caseCategory?'<span class="red">*</span>':'' }</td>
				<td width="25%">
					<input id="${stuff }_merMid" name="${stuff }_merMid" value="<c:out value='${caseHandleInfoDTO.merMid }'/>"
							 ${caseCategoryAttr.PROJECT.code eq caseCategory or caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.OTHER.code eq caseCategory?'':'disabled' }/>
					<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory 
									or caseCategoryAttr.OTHER.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
						<a href="javascript:void(0)" id="getMerchantBtn" onclick="getMerchantInfo();">&nbsp;
						<a href="javascript:void(0)" id="queryMerchantBtn" onclick="chooseMID();">查詢</a>
						<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory  or caseCategoryAttr.PROJECT.code eq caseCategory}">
							<a href="javascript:void(0)" id="editMerchantBtn" onclick="updateMerchant();">修改特店資料</a>
						</c:if>
					</c:if>
					<input id="merchantId" name="" value="<c:out value='${caseHandleInfoDTO.merchantCode}'/>" type="hidden"/>
					<input id="changeMerchat" name="changeMerchat" value="<c:out value='${caseHandleInfoDTO.merMid }'/>" type="hidden"/>
				</td>
				<td width="12%">特店名稱: </td>
					<td width="18%">
						<input value="<c:out value='${caseHandleInfoDTO.merchantName }'/>" name="${stuff }_merchantName" id="${stuff }_merchantName" disabled="disabled"/>
				</td>
				<c:choose>
					<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory 
										or caseCategoryAttr.OTHER.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory }">
						<%-- <td width="12%">特店名稱: </td>
						<td width="18%">
							<input value="<c:out value='${caseHandleInfoDTO.merchantName }'/>" name="${stuff }_merchantName" id="${stuff }_merchantName" disabled="disabled"/>
						</td> --%>
						<td width="12%">表頭（同對外名稱）: <span class="red">*</span></td>
						<td colspan="3">
							<cafe:droplisttag
								id="${stuff }_merchantHeaderId"
								name="${stuff }_merchantHeaderId" 
								selectedValue="${caseHandleInfoDTO.merchantHeaderId }"
								result="${editMerchantHeaders }"
								hasBlankValue="true"
								blankName="請選擇"
							></cafe:droplisttag>
							<c:if test="${caseCategoryAttr.OTHER.code ne caseCategory }">
								<a href="javascript:void(0)" style="margin-top: 2px" id="editMerHeaderBtn" onclick="updateMerchantHeader();">修改特店表頭</a>
							</c:if>
						</td>
					</c:when>
					<c:otherwise>
						<%-- <td width="12%">特店名稱: </td>
						<td width="18%">
							<input value="<c:out value='${caseHandleInfoDTO.merchantName }'/>" name="${stuff }_merchantName" id="${stuff }_merchantName" disabled="disabled"/>
						</td> --%>
						<td width="12%">表頭（同對外名稱）:</td>
						<td colspan="3">
							<input value="<c:out value='${caseHandleInfoDTO.merchantHeaderName }'/>" name="${stuff }_merchantHeaderName" id="${stuff }_merchantHeaderName" disabled="disabled"/>&nbsp;
							<input id="${stuff }_merchantHeaderId" name="${stuff }_merchantHeaderId" value="<c:out value='${caseHandleInfoDTO.merchantHeaderId }'/>" type="hidden"/>
						</td>
					</c:otherwise>
				</c:choose>
			</tr>
			<tr>
				<c:if test="${caseCategoryAttr.UPDATE.code eq caseCategory }">
					<td>舊特店代號: </td>
					<td>
						<input value="<c:out value='${caseHandleInfoDTO.oldMerchantCode }'/>" name="${stuff }_oldMerchantCode" id="${stuff }_oldMerchantCode" />
					</td>
				</c:if>
				<td>VIP: </td>
				<td>
					<c:choose>
						<c:when test="${not empty caseHandleInfoDTO.caseId and caseHandleInfoDTO.isVip eq 'Y'}">
							<cafe:checklistTag
								name="${stuff }_isVip"
								id="${stuff }_isVip" 
								result = "${isVipList}" 
								type="radio" 
								checkedValues='<%=StringUtils.toList("Y",",") %>'
								javascript='disabled=true'
							/>
						</c:when>
						<c:otherwise>
							<cafe:checklistTag
								name="${stuff }_isVip"
								id="${stuff }_isVip" 
								result = "${isVipList}" 
								type="radio" 
								checkedValues='<%=StringUtils.toList("N",",") %>'
								javascript='disabled=true'
							/>
						</c:otherwise>
					</c:choose>
					
				</td>
				<td>特店區域: </td>
				<td><input name="${stuff }_merLocationName" id="${stuff }_merLocationName" value="<c:out value='${caseHandleInfoDTO.areaName }'/>" disabled="disabled"></td>
				<!-- 定義特點區域隱藏域 -->
				<input type="hidden" name="${stuff }_merLocation" id="${stuff }_merLocation" value="<c:out value='${caseHandleInfoDTO.area }'/>">
				<c:if test="${caseCategoryAttr.UPDATE.code ne caseCategory }">
					<td >特店聯絡人: </td>
					<td ><input name="${stuff }_contact" id="${stuff }_contact" value="<c:out value='${caseHandleInfoDTO.contact }'/>" disabled="disabled"></td>
				</c:if>
			</tr>
			<tr>
				<c:if test="${caseCategoryAttr.UPDATE.code eq caseCategory }">
					<td >特店聯絡人: </td>
					<td ><input name="${stuff }_contact" id="${stuff }_contact" value="<c:out value='${caseHandleInfoDTO.contact }'/>" disabled="disabled"></td>
				</c:if>
				<td>特店聯絡人電話1: </td>
				<td> <input disabled="disabled" value="<c:out value='${caseHandleInfoDTO.contactTel }'/>" name="${stuff }_contactTel" id="${stuff }_contactTel"> </td>
				<td>特店聯絡人電話2: </td>
				<td> <input disabled="disabled" value="<c:out value='${caseHandleInfoDTO.contactTel2 }'/>" name="${stuff }_contactTel2" id="${stuff }_contactTel2"> </td>
				<c:if test="${caseCategoryAttr.UPDATE.code ne caseCategory }">
					<td>特店聯絡人行動電話: </td>
					<td> <input disabled="disabled" value="<c:out value='${caseHandleInfoDTO.phone }'/>" name="${stuff }_merMobilePhone" id="${stuff }_merMobilePhone"> </td>
				</c:if>
			</tr>
			<tr>
				<c:if test="${caseCategoryAttr.UPDATE.code eq caseCategory }">
					<td>特店聯絡人行動電話: </td>
					<td> <input disabled="disabled" value="<c:out value='${caseHandleInfoDTO.phone }'/>" name="${stuff }_merMobilePhone" id="${stuff }_merMobilePhone"> </td>
				</c:if>
				<c:if test="${caseCategoryAttr.UPDATE.code ne caseCategory }">
					<td>特店聯絡人Email: </td>
					<td> <input disabled="disabled" name="${stuff }_merContactEmail" id="${stuff }_merContactEmail" value="<c:out value='${caseHandleInfoDTO.contactEmail }'/>"> </td>
				</c:if>
				<td>營業時間起: </td>
				<td><input name="${stuff }_merOpenHour" id="${stuff }_merOpenHour" value="${caseHandleInfoDTO.openHour }" disabled="disabled" ></td>
				<td>營業時間迄: </td>
				<td><input name="${stuff }_merCloseHour" id="${stuff }_merCloseHour" value="${caseHandleInfoDTO.closeHour }" disabled="disabled"></td>
				
			</tr>
			<tr>
				<c:if test="${caseCategoryAttr.UPDATE.code eq caseCategory || caseCategoryAttr.INSTALL.code eq caseCategory}">
					<c:if test="${caseCategoryAttr.UPDATE.code eq caseCategory}">
						<td>特店聯絡人Email: </td>
						<td> <input disabled="disabled" name="${stuff }_merContactEmail" id="${stuff }_merContactEmail" value="<c:out value='${caseHandleInfoDTO.contactEmail }'/>"> </td>
					</c:if>
					<td><label id="aoNameLabel">AO人員: </label></td>
					<td>
						<input id="${stuff }_merAoName" name="${stuff }_merAoName" value="<c:out value='${caseHandleInfoDTO.aoName }'/>">
					</td>
 					<td><label id="aoEmailLabel">AO Email: </label></td>
					<td colspan="3">
						<input id="${stuff }_aoEmail" name="${stuff }_aoEmail" value="<c:out value='${caseHandleInfoDTO.aoemail }'/>" disabled="disabled">
					</td>
				</c:if>
				<c:if test="${caseCategoryAttr.UPDATE.code ne caseCategory && caseCategoryAttr.INSTALL.code ne caseCategory}">
					<td>營業地址: </td>
					<td>
						<%-- <input class="easyui-textbox" disabled="disabled" value="<c:out value='${caseHandleInfoDTO.businessAddress }'/>" id="" name="" style="width: 50px" > --%>
						<cafe:droplisttag
							id="${stuff }_location"
							name="${stuff }_location" 
							hasBlankValue="true"
							blankName="縣市"
							selectedValue="${caseHandleInfoDTO.location }"
							result="${locationList }"
						></cafe:droplisttag>
						<cafe:droplisttag
							id="${stuff }_locationPostCode"
							name="${stuff }_locationPostCode" 
							hasBlankValue="true"
							blankName="郵遞區號"
							selectedValue="${caseHandleInfoDTO.postCodeId }"
							result="${locationPostCodes }"
						></cafe:droplisttag>
						<input value="<c:out value='${caseHandleInfoDTO.businessAddress }'/>" id="${stuff }_merBusinessAddress" name="${stuff }_merBusinessAddress" >
					</td>
					<td><label id="aoNameLabel">AO人員: </label></td>
					<td>
						<input id="${stuff }_merAoName" name="${stuff }_merAoName" value="<c:out value='${caseHandleInfoDTO.aoName }'/>">
					</td>
					<td><label id="aoEmailLabel">AO Email: </label></td>
					<td>
						<input id="${stuff }_aoEmail" name="${stuff }_aoEmail" value="<c:out value='${caseHandleInfoDTO.aoemail }'/>" disabled="disabled">
					</td>
					
				</c:if>
			</tr>
			<tr>
				<c:if test="${caseCategoryAttr.UPDATE.code eq caseCategory || caseCategoryAttr.INSTALL.code eq caseCategory}">
					<td>營業地址: </td>
					<td>
						<cafe:droplisttag
							id="${stuff }_location"
							name="${stuff }_location" 
							hasBlankValue="true"
							blankName="縣市"
							selectedValue="${caseHandleInfoDTO.location }"
							result="${locationList }"
						></cafe:droplisttag>
						<cafe:droplisttag
							id="${stuff }_locationPostCode"
							name="${stuff }_locationPostCode" 
							hasBlankValue="true"
							blankName="郵遞區號"
							result="${locationPostCodes }"
							selectedValue="${caseHandleInfoDTO.postCodeId }"
						></cafe:droplisttag>
						<%-- <input class="easyui-textbox" disabled="disabled" value="<c:out value='${caseHandleInfoDTO.businessAddress }'/>" id="" name="" style="width: 50px" > --%>
						<input value="<c:out value='${caseHandleInfoDTO.businessAddress }'/>" id="${stuff }_merBusinessAddress" name="${stuff }_merBusinessAddress" disabled="disabled">
					</td>
					<td>裝機地址: <span class="red">*</span></td>
					<td colspan="3">
						<input type="checkbox" onclick="equalsMerBusiness(this,'${stuff }_merBusinessAddress', '${stuff }_merInstallAddress');" 
							${caseHandleInfoDTO.isBussinessAddress eq 'Y'?'checked':'' } name="${stuff }_isBussinessAddress" id="${stuff }_isBussinessAddress"/>
						<label style="width:200px" >同營業地址</label>
						<%-- <input value="<c:out value='${caseHandleInfoDTO.businessAddress }'/>" id="installedAa" name="installedAa"> --%>
						<cafe:droplisttag
							id="${stuff }_installedAdressLocation"
							name="${stuff }_installedAdressLocation" 
							hasBlankValue="true"
							blankName="縣市"
							selectedValue="${caseHandleInfoDTO.isBussinessAddress eq 'Y'? caseHandleInfoDTO.location : caseHandleInfoDTO.installedAdressLocation }"
							result="${locationList }"
							javascript="${caseHandleInfoDTO.isBussinessAddress eq 'Y'?empty caseHandleInfoDTO.installedAdressLocation?'readonly':'disabled':''}"
						></cafe:droplisttag>
						<cafe:droplisttag
							id="${stuff }_installedPostCode"
							name="${stuff }_installedPostCode" 
							hasBlankValue="true"
							blankName="郵遞區號"
							result="${installedPostCodes }"
							selectedValue="${caseHandleInfoDTO.isBussinessAddress eq 'Y'? caseHandleInfoDTO.postCodeId : caseHandleInfoDTO.installedPostCode }"
							javascript="${caseHandleInfoDTO.isBussinessAddress eq 'Y'?empty caseHandleInfoDTO.installedPostCode?'readonly':'disabled':''}"
						></cafe:droplisttag>
						<input id="${stuff }_merInstallAddress" ${caseHandleInfoDTO.isBussinessAddress eq 'Y'?empty caseHandleInfoDTO.installedAdress?'readonly':'disabled':'' } value="<c:out value='${caseHandleInfoDTO.isBussinessAddress eq \'Y\'? caseHandleInfoDTO.businessAddress : caseHandleInfoDTO.installedAdress }'/>" name="${stuff }_installedAdress" 
							${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory?'':'disabled' }/>
					</td>
					
				</c:if>

				<!--  如果最新异动的案件变为 非装机异动案件则将contact相关栏位放入隐藏域 存储  2018/01/15-->
				<%-- <c:if test="${caseHandleInfoDTO.caseCategory eq caseCategoryAttr.INSTALL.code || caseHandleInfoDTO.caseCategory eq caseCategoryAttr.UPDATE.code}">
					<input type="hidden" id="hideForUpdateContactIsBussinessAddress" value=""/>
					<input type="hidden" id="hideForUpdateContactAddressLocation" value=""/>
					<input type="hidden" id="hideForUpdateContactAddress" value=""/>
					<input type="hidden" id="hideForUpdateContactIsBussinessContact" value=""/>
					<input type="hidden" id="hideForUpdateContactUser" value=""/>
					<input type="hidden" id="hideForUpdateContactIsBussinessContactPhone" value=""/>
					<input type="hidden" id="hideForUpdateContactUserPhone" value=""/>
					<input type="hidden" id="hideForUpdateContactFlag" value=""/>
				</c:if> --%>
				<!--  如果最新异动的案件变为 装机异动案件则将install相关栏位放入隐藏域 存储  2018/01/15-->
				<%-- <c:if test="${caseHandleInfoDTO.caseCategory ne caseCategoryAttr.INSTALL.code && caseHandleInfoDTO.caseCategory ne caseCategoryAttr.UPDATE.code}">
					<input type="hidden" id="hideForUpdateInstalledAdressLocation" value=""/>
					<input type="hidden" id="hideForUpdateIsBussinessAddress" value=""/>
					<input type="hidden" id="hideForUpdateInstalledAdress" value=""/>
					<input type="hidden" id="hideForUpdateIsBussinessContact" value=""/>
					<input type="hidden" id="hideForUpdateInstalledContact" value=""/>
					<input type="hidden" id="hideForUpdateIsBussinessContactPhone" value=""/>
					<input type="hidden" id="hideForUpdateInstalledContactPhone" value=""/>
					<input type="hidden" id="hideForUpdateInstalledFlag" value=""/>
				</c:if> --%>
				<c:if test="${caseCategoryAttr.UPDATE.code ne caseCategory && caseCategoryAttr.INSTALL.code ne caseCategory}">
					<td>裝機地址: </td>
					<c:choose>
						<c:when test="${caseCategoryAttr.OTHER.code eq caseCategory}">
							<td>
								<%-- <input value="<c:out value='${caseHandleInfoDTO.businessAddress }'/>" id="installedAa" name="installedAa"> --%>
			                    <cafe:droplisttag
									id="${stuff }_installedAdressLocation"
									name="${stuff }_installedAdressLocation" 
									hasBlankValue="true"
									blankName="縣市"
									selectedValue="${caseHandleInfoDTO.installedAdressLocation }"
									result="${locationList }"
								></cafe:droplisttag>
								<cafe:droplisttag
									id="${stuff }_installedPostCode"
									name="${stuff }_installedPostCode" 
									hasBlankValue="true"
									blankName="郵遞區號"
									result="${installedPostCodes }"
									selectedValue="${caseHandleInfoDTO.installedPostCode }"
								></cafe:droplisttag>
								<input id="${stuff }_merInstallAddress" value="<c:out value='${caseHandleInfoDTO.installedAdress }'/>" name="${stuff }_installedAdress" />
								<input type="hidden" name="${stuff }_isBussinessAddress" id="${stuff }_isBussinessAddress" value="<c:out value='${caseHandleInfoDTO.isBussinessAddress }'/>"/>
							</td>
						</c:when>
						<c:otherwise>
							<td>
								<%-- <input class="easyui-textbox" disabled="disabled" value="<c:out value='${caseHandleInfoDTO.businessAddress }'/>" id="" name="" style="width: 50px" > --%>
			                    <cafe:droplisttag
									id="${stuff }_installedAdressLocation"
									name="${stuff }_installedAdressLocation" 
									hasBlankValue="true"
									blankName="縣市"
									selectedValue="${caseHandleInfoDTO.isBussinessAddress eq 'Y'? caseHandleInfoDTO.location : caseHandleInfoDTO.installedAdressLocation }"
									result="${locationList }"
								></cafe:droplisttag>
								<cafe:droplisttag
									id="${stuff }_installedPostCode"
									name="${stuff }_installedPostCode" 
									hasBlankValue="true"
									blankName="郵遞區號"
									result="${installedPostCodes }"
									selectedValue="${caseHandleInfoDTO.installedPostCode }"
								></cafe:droplisttag>
								<input id="${stuff }_merInstallAddress" ${caseHandleInfoDTO.isBussinessAddress eq 'Y'?'disabled':'' } value="<c:out value='${caseHandleInfoDTO.isBussinessAddress eq \'Y\'? caseHandleInfoDTO.businessAddress : caseHandleInfoDTO.installedAdress }'/>" name="${stuff }_installedAdress" ${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory?'':'disabled' }/>
								<input type="hidden" name="${stuff }_isBussinessAddress" id="${stuff }_isBussinessAddress" value="<c:out value='${caseHandleInfoDTO.isBussinessAddress }'/>"/>
							</td>
						</c:otherwise>
					</c:choose>
					
					
					<td>聯繫地址: <span class="red">*</span></td>
					<td colspan="3">
						<input type="checkbox" onclick="equalsMerBusiness(this,'${stuff }_merBusinessAddress', '${stuff }_contactAddress');" 
								${caseHandleInfoDTO.contactIsBussinessAddress eq 'Y'?'checked':'' } name="${stuff }_contactIsBussinessAddress" id="${stuff }_contactIsBussinessAddress"/>
							<label style="width:200px" >同營業地址</label>
						<%-- <input value="<c:out value='${caseHandleInfoDTO.businessAddress }'/>" id="contactAa" name="contactAa"> --%>
						<cafe:droplisttag
							id="${stuff }_contactAddressLocation"
							name="${stuff }_contactAddressLocation" 
							hasBlankValue="true"
							blankName="縣市"
							selectedValue="${caseHandleInfoDTO.contactIsBussinessAddress eq 'Y'? caseHandleInfoDTO.location : caseHandleInfoDTO.contactAddressLocation }"
							result="${locationList }"
						></cafe:droplisttag>
						<cafe:droplisttag
							id="${stuff }_contactPostCode"
							name="${stuff }_contactPostCode" 
							hasBlankValue="true"
							blankName="郵遞區號"
							selectedValue="${caseHandleInfoDTO.contactIsBussinessAddress eq 'Y'? caseHandleInfoDTO.postCodeId : caseHandleInfoDTO.contactPostCode }"
							result="${contactPostCodes }"
						></cafe:droplisttag>
						<input id="${stuff }_contactAddress" value="<c:out value='${caseHandleInfoDTO.contactIsBussinessAddress eq \'Y\'? caseHandleInfoDTO.businessAddress : caseHandleInfoDTO.contactAddress }'/>" name="${stuff }_contactAddress" />
					</td>
				</c:if>
				<input type="hidden" name="${stuff }_merInstallAddress" id="${stuff }_merInstallAddress"/>
				<input type="hidden" name="${stuff }_installedAdressLocation" id="${stuff }_installedAdressLocation"/>
			</tr>
			<tr>
				<c:choose>
					<c:when test="${caseCategoryAttr.UPDATE.code eq caseCategory || caseCategoryAttr.INSTALL.code eq caseCategory}">
						<td>裝機聯絡人:<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory}"><span class="red">*</span></c:if></td>
						<td>
							<input type="checkbox" onclick="equalsMerBusiness(this,'${stuff }_contact', '${stuff }_merInstallUser');" 
									name="${stuff }_isBussinessContact" id="${stuff }_isBussinessContact" 
									${caseHandleInfoDTO.isBussinessContact eq 'Y'?'checked':'' }/>同特店聯絡人
							<input id="${stuff }_merInstallUser" ${caseHandleInfoDTO.isBussinessContact eq 'Y'?empty caseHandleInfoDTO.installedContact?'readonly':'disabled':'' } value="<c:out value='${caseHandleInfoDTO.isBussinessContact eq \'Y\'? caseHandleInfoDTO.contact : caseHandleInfoDTO.installedContact }'/>" name="${stuff }_installedContact" ">
						</td>
						<td>裝機聯絡人電話:<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory}"><span class="red">*</span></c:if></td>
						<td colspan="3">
							<input type="checkbox" onclick="equalsMerBusiness(this,'${stuff }_contactTel', '${stuff }_merInstallPhone');"  
									name="${stuff }_isBussinessContactPhone" id="${stuff }_isBussinessContactPhone" 
									${caseHandleInfoDTO.isBussinessContactPhone eq 'Y'?'checked':'' }/>同特店聯絡人電話
						<input id="${stuff }_merInstallPhone" ${caseHandleInfoDTO.isBussinessContactPhone eq 'Y'?empty caseHandleInfoDTO.installedContactPhone?'readonly':'disabled':'' } value="<c:out value='${caseHandleInfoDTO.isBussinessContactPhone eq \'Y\'? caseHandleInfoDTO.contactTel : caseHandleInfoDTO.installedContactPhone }'/>" name="${stuff }_installedContactPhone" />
						</td>
					</c:when>
					<c:otherwise>
						<td>聯繫聯絡人: ${caseCategoryAttr.MERGE.code eq caseCategory or caseCategoryAttr.UNINSTALL.code eq caseCategory?'<span class="red">*</span>':''}</td>
						<td>
							<input type="checkbox" onclick="equalsMerBusiness(this,'${stuff }_contact', '${stuff }_contactUser');" 
									name="${stuff }_contactIsBussinessContact" id="${stuff }_contactIsBussinessContact" 
									${caseHandleInfoDTO.contactIsBussinessContact eq 'Y'?'checked':'' }/>同特店聯絡人
						<input id="${stuff }_contactUser" ${caseHandleInfoDTO.contactIsBussinessContact eq 'Y'?empty caseHandleInfoDTO.contactUser?'readonly':'disabled':'' } value="<c:out value='${caseHandleInfoDTO.contactIsBussinessContact eq \'Y\'? caseHandleInfoDTO.contact : caseHandleInfoDTO.contactUser }'/>" name="${stuff }_contactUser" />
						</td>
						<td>聯繫聯絡人電話: ${caseCategoryAttr.MERGE.code eq caseCategory or caseCategoryAttr.UNINSTALL.code eq caseCategory ?'<span class="red">*</span>':''}</td>
						<td colspan="3">
							<input type="checkbox" onclick="equalsMerBusiness(this,'${stuff }_contactTel', '${stuff }_contactUserPhone');"  
									name="${stuff }_contactIsBussinessContactPhone" id="${stuff }_contactIsBussinessContactPhone" 
									${caseHandleInfoDTO.contactIsBussinessContactPhone eq 'Y'?'checked':'' }/>同特店聯絡人電話
							<input id="${stuff }_contactUserPhone" ${caseHandleInfoDTO.contactIsBussinessContactPhone eq 'Y'?empty caseHandleInfoDTO.contactUserPhone?'readonly':'disabled':'' } value="<c:out value='${caseHandleInfoDTO.contactIsBussinessContactPhone eq \'Y\'? caseHandleInfoDTO.contactTel : caseHandleInfoDTO.contactUserPhone }'/>" name="${stuff }_contactUserPhone" "/>
						</td>
					</c:otherwise>
				</c:choose>
			</tr>
			<tr>
				<c:choose>
					<c:when test="${caseCategoryAttr.UPDATE.code eq caseCategory || caseCategoryAttr.INSTALL.code eq caseCategory}">
						<td>裝機聯絡人手機:</td>
						<td>
							<input type="checkbox" onclick="equalsMerBusiness(this,'${stuff }_merMobilePhone', '${stuff }_installedContactMobilePhone');" 
									name="${stuff }_isBussinessContactMobilePhone" id="${stuff }_isBussinessContactMobilePhone" 
									${caseHandleInfoDTO.isBussinessContactMobilePhone eq 'Y'?'checked':'' }/>同特店聯絡人行動電話
							<input id="${stuff }_installedContactMobilePhone"  name="${stuff }_installedContactMobilePhone" 
								${caseHandleInfoDTO.isBussinessContactMobilePhone eq 'Y'?empty caseHandleInfoDTO.phone?'readonly':'disabled':'' } value="<c:out value='${caseHandleInfoDTO.isBussinessContactMobilePhone eq \'Y\'? caseHandleInfoDTO.phone : caseHandleInfoDTO.installedContactMobilePhone }'/>" >
						</td>
						<td>裝機聯絡人Email:</td>
						<td colspan="3">
							<input type="checkbox" onclick="equalsMerBusiness(this,'${stuff }_merContactEmail', '${stuff }_installedContactEmail');"  
									name="${stuff }_isBussinessContactEmail" id="${stuff }_isBussinessContactEmail" 
									${caseHandleInfoDTO.isBussinessContactEmail eq 'Y'?'checked':'' }/>同特店聯絡人Email
							<input id="${stuff }_installedContactEmail"  name="${stuff }_installedContactEmail" 
								${caseHandleInfoDTO.isBussinessContactEmail eq 'Y'?empty caseHandleInfoDTO.contactEmail?'readonly':'disabled':'' } value="<c:out value='${caseHandleInfoDTO.isBussinessContactEmail eq \'Y\'? caseHandleInfoDTO.contactEmail : caseHandleInfoDTO.installedContactEmail }'/>"/>
						</td>
					</c:when>
					<c:otherwise>
						<td>聯繫聯絡人手機:</td>
						<td>
							<input type="checkbox" onclick="equalsMerBusiness(this,'${stuff }_merMobilePhone', '${stuff }_contactMobilePhone');" 
									name="${stuff }_contactIsBussinessContactMobilePhone" id="${stuff }_contactIsBussinessContactMobilePhone" 
									${caseHandleInfoDTO.contactIsBussinessContactMobilePhone eq 'Y'?'checked':'' }/>同特店聯絡人行動電話
							<input id="${stuff }_contactMobilePhone"  name="${stuff }_contactMobilePhone" 
								${caseHandleInfoDTO.contactIsBussinessContactMobilePhone eq 'Y'?empty caseHandleInfoDTO.phone?'readonly':'disabled':'' } value="<c:out value='${caseHandleInfoDTO.contactIsBussinessContactMobilePhone eq \'Y\'? caseHandleInfoDTO.phone : caseHandleInfoDTO.contactMobilePhone }'/>"/>
						</td>
						<td>聯繫聯絡人Email:</td>
						<td colspan="3">
							<input type="checkbox" onclick="equalsMerBusiness(this,'${stuff }_merContactEmail', '${stuff }_contactUserEmail');"  
									name="${stuff }_contactIsBussinessContactEmail" id="${stuff }_contactIsBussinessContactEmail" 
									${caseHandleInfoDTO.contactIsBussinessContactEmail eq 'Y'?'checked':'' }/>同特店聯絡人Email
							<input id="${stuff }_contactUserEmail"  name="${stuff }_contactUserEmail" 
								${caseHandleInfoDTO.contactIsBussinessContactEmail eq 'Y'?empty caseHandleInfoDTO.contactEmail?'readonly':'disabled':'' } value="<c:out value='${caseHandleInfoDTO.contactIsBussinessContactEmail eq \'Y\'? caseHandleInfoDTO.contactEmail : caseHandleInfoDTO.contactUserEmail }'/>"/>
						</td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		<div id="merchanInfoDlg"></div>
		<div id="merchanEditDlg"></div>
	</div>
	
	
	<script type="text/javascript">
	/*
	* 頁面加載完成函數
	*/
	$(function(){
		<c:if test="${not empty caseHandleInfoDTO.caseId and caseCategoryAttr.INSTALL.code ne caseCategory}">
			if(!isEmpty('${gpCustomerId}') && !isEmpty('${caseHandleInfoDTO.customerId}')){
				if('${caseHandleInfoDTO.customerId }' == '${gpCustomerId}'){
					// 處理環匯客戶
					$("#isGpCustomer").val('Y');
				}
			}
		</c:if>
		// 加載特店資訊區塊
	//	setTimeout("loadingMerchantInfo('${caseCategory}');",5);
		loadingMerchantInfo('${caseCategory}');
	});
	
	/*
	* 加載特店資訊區塊
	*/
	function loadingMerchantInfo(caseCategory){
		// panel
		$("#special").panel({title:'特店資料',width:'99%'});
		// 裝機+異動
		if(caseCategory == '${caseCategoryAttr.INSTALL.code}' || caseCategory == '${caseCategoryAttr.UPDATE.code}'
				|| caseCategory == '${caseCategoryAttr.OTHER.code}' || caseCategory == '${caseCategoryAttr.PROJECT.code}'){
			// 特店代號
			if ($('#${stuff }_merMid').length > 0) {
				$('#${stuff }_merMid').textbox({
					required:true,
					missingMessage:"請輸入特店代號",
					validType:'maxLength[20]'
				});
				$('#${stuff }_merMid').textbox('textbox').attr('maxlength', 20);
				$('#getMerchantBtn').linkbutton({iconCls:'icon-ok'});
				$('#queryMerchantBtn').linkbutton({iconCls:'icon-search'});
				if (caseCategory != '${caseCategoryAttr.OTHER.code}') {
					$('#editMerchantBtn').linkbutton({iconCls:'icon-edit'});
				}
			}
			// 表頭（同對外名稱）
			if ($('#${stuff }_merchantHeaderId').length > 0) {
				$('#${stuff }_merchantHeaderId').combobox({
					editable:false, 
					required:true,
					valueField:'value',
					textField:'name',
					width:'150px',
					validType:'requiredDropList',
					invalidMessage:"請輸入特店表頭" 
				});
				$('#${stuff }_merchantHeaderId').addClass("easyui-combobox");
				$('#editMerHeaderBtn').linkbutton({iconCls:'icon-edit'});
			}
		}
		if(caseCategory == '${caseCategoryAttr.INSTALL.code}' || caseCategory == '${caseCategoryAttr.UPDATE.code}'){
			// 是否為裝機
			var isInstall = false;
			if(caseCategory == '${caseCategoryAttr.INSTALL.code}'){
				isInstall = true;
			}
			// 特店代號
			/* if ($('#${stuff }_merMid').length > 0) {
				$('#${stuff }_merMid').textbox({
					required:true,
					missingMessage:"請輸入特店代號",
					validType:'maxLength[20]'
				});
				$('#${stuff }_merMid').textbox('textbox').attr('maxlength', 20);
				$('#getMerchantBtn').linkbutton({iconCls:'icon-ok'});
				$('#queryMerchantBtn').linkbutton({iconCls:'icon-search'});
				$('#editMerchantBtn').linkbutton({iconCls:'icon-edit'});
			} */
			// 表頭（同對外名稱）
			/* if ($('#${stuff }_merchantHeaderId').length > 0) {
				$('#${stuff }_merchantHeaderId').combobox({
					editable:false, 
					required:true,
					valueField:'value',
					textField:'name',
					width:'150px',
					validType:'requiredDropList',
					invalidMessage:"請輸入特店表頭" 
				});
				$('#editMerHeaderBtn').linkbutton({iconCls:'icon-edit'});
			} */
			// 裝機地址
			if ($('#${stuff }_installedAdressLocation').length > 0) {
				$('#${stuff }_installedAdressLocation').combobox({
					editable:false, 
					required:true,
					valueField:'value',
					textField:'name',
					width:'100px', 
					validType:'requiredDropList[\'縣市\']',
					invalidMessage:"請輸入裝機地址-縣市",
					onChange:function(newValue, oldValue) {
						locationChange("${stuff }_installedPostCode", newValue, oldValue);
					}
				});
				$('#${stuff }_installedAdressLocation').addClass("easyui-combobox");
			}
			if ($("#${stuff }_installedPostCode").length > 0) {
				if (${not empty caseHandleInfoDTO.caseId and caseHandleInfoDTO.installType eq '4'}) {
					$("#${stuff }_installedPostCode").combobox({
						required:true,
						editable:false, 
						valueField:'value',
						textField:'name',
						width:'120px',
						validType:'requiredDropList[\'郵遞區號\']',
						invalidMessage:"請輸入郵遞區號",
					});
				} else {
					$("#${stuff }_installedPostCode").combobox({
						editable:false, 
						valueField:'value',
						textField:'name',
						width:'120px'
					});
				}
				$('#${stuff }_installedPostCode').addClass("easyui-combobox");
			}
			if ($('#${stuff }_merInstallAddress').length > 0) {
				$('#${stuff }_merInstallAddress').textbox({
					required:true,
					missingMessage:"請輸入裝機地址",
					width:'200px'
				});
				$('#${stuff }_merInstallAddress').textbox('textbox').attr('maxlength', 100);
			}
			// 裝機聯絡人
			if ($('#${stuff }_merInstallUser').length > 0) {
				$('#${stuff }_merInstallUser').textbox({
					required:isInstall,
					missingMessage:"請輸入裝機聯絡人",
					width:'200px'
				});
				$('#${stuff }_merInstallUser').textbox('textbox').attr('maxlength', 50);
			}
			// 裝機聯絡人電話
			if ($('#${stuff }_merInstallPhone').length > 0) {
				$('#${stuff }_merInstallPhone').textbox({
					required:isInstall,
					missingMessage:"請輸入裝機聯絡人電話",
					width:'250px'
				});
				$('#${stuff }_merInstallPhone').textbox('textbox').attr('maxlength', 20);
			}
			// 裝機聯絡人手機
			if ($('#${stuff }_installedContactMobilePhone').length > 0) {
				$('#${stuff }_installedContactMobilePhone').textbox({
					validType:'twMobile',
					invalidMessage:"裝機聯絡人手機限輸入09開頭且長度為10碼的數字，請重新輸入",
					width:'200px'
				});
				$('#${stuff }_installedContactMobilePhone').textbox('textbox').attr('maxlength', 10);
			}
			// 裝機聯絡人EMAIL
			if ($('#${stuff }_installedContactEmail').length > 0) {
				$('#${stuff }_installedContactEmail').textbox({
					validType:'email',
					invalidMessage:"裝機聯絡人Email格式有誤，請重新輸入",
					width:'250px'
				});
				$('#${stuff }_installedContactEmail').textbox('textbox').attr('maxlength', 50);
			}
		// 其他類別
		} else {
			// 聯絡人是否必填
			var requiredUser = false;
			if(caseCategory == '${caseCategoryAttr.MERGE.code}'
				|| caseCategory == '${caseCategoryAttr.UNINSTALL.code}'){
				requiredUser = true;
			}
			// 聯絡人電話是否必填
			var requiredUserPhone = false;
			if(caseCategory == '${caseCategoryAttr.MERGE.code}'
				|| caseCategory == '${caseCategoryAttr.UNINSTALL.code}'){
				requiredUserPhone = true;
			}
			// 特店代號
			if ($('#${stuff }_merMid').length > 0) {
				$('#${stuff }_merMid').textbox();
			}
			// 表頭（同對外名稱）
			if ($('#${stuff }_merchantHeaderName').length > 0) {
				$('#${stuff }_merchantHeaderName').textbox();
			}
			// 裝機地址
			if ($('#${stuff }_installedAdressLocation').length > 0) {
				$('#${stuff }_installedAdressLocation').combobox({
					editable:false, 
					required:${caseCategoryAttr.OTHER.code eq caseCategory}?false:true,
					disabled:${caseCategoryAttr.OTHER.code eq caseCategory}?false:true,
					valueField:'value',
					textField:'name',
					width:'80px',
					validType:${caseCategoryAttr.OTHER.code eq caseCategory}?'':'requiredDropList[\'縣市\']',
					invalidMessage:"請輸入裝機地址-縣市",
					onChange:function(newValue, oldValue) {
						locationChange("${stuff }_installedPostCode", newValue, oldValue);
					}
				});
				$('#${stuff }_installedAdressLocation').addClass("easyui-combobox");
			}
			if ($("#${stuff }_installedPostCode").length > 0) {
				$("#${stuff }_installedPostCode").combobox({
					editable:false, 
					disabled:${caseCategoryAttr.OTHER.code eq caseCategory}?false:true,
					valueField:'value',
					textField:'name',
					width:'120px'				
				});
				$('#${stuff }_installedPostCode').addClass("easyui-combobox");
			}
			if ($('#${stuff }_merInstallAddress').length > 0) {
				$('#${stuff }_merInstallAddress').textbox({
					required:${caseCategoryAttr.OTHER.code eq caseCategory}?false:true,
					disabled:${caseCategoryAttr.OTHER.code eq caseCategory}?false:true,
					missingMessage:"請輸入裝機地址",
					width:'170px'
				});
				$('#${stuff }_merInstallAddress').textbox('textbox').attr('maxlength', 100);
			}
			// 聯繫地址
			if ($('#${stuff }_contactAddressLocation').length > 0) {
				$('#${stuff }_contactAddressLocation').combobox({
					editable:false, 
					required:true,
					valueField:'value',
					textField:'name',
					width:'100px',
					validType:'requiredDropList[\'縣市\']',
					disabled:"${caseHandleInfoDTO.contactIsBussinessAddress eq 'Y'?'disabled':''}",
					invalidMessage:"請輸入聯繫地址-縣市",
					onChange:function(newValue, oldValue) {
						locationChange("${stuff }_contactPostCode", newValue, oldValue);
					} 
				});
				$('#${stuff }_contactAddressLocation').addClass("easyui-combobox");
			}
			if ($("#${stuff }_contactPostCode").length > 0) {
				if (${not empty caseHandleInfoDTO.caseId and caseHandleInfoDTO.installType eq '4'}) {
					$("#${stuff }_contactPostCode").combobox({
						required:true,
						editable:false, 
						valueField:'value',
						textField:'name',
						width:'120px',
						validType:'requiredDropList[\'郵遞區號\']',
						invalidMessage:"請輸入郵遞區號",
						disabled:"${caseHandleInfoDTO.contactIsBussinessAddress eq 'Y'?'disabled':''}",
					});
				} else {
					$("#${stuff }_contactPostCode").combobox({
						editable:false, 
						valueField:'value',
						textField:'name',
						width:'120px',
						disabled:"${caseHandleInfoDTO.contactIsBussinessAddress eq 'Y'?'disabled':''}",
					});
				}
			}
			if ($('#${stuff }_contactAddress').length > 0) {
				$('#${stuff }_contactAddress').textbox({
					required:true,
					missingMessage:"請輸入聯繫地址",
					disabled:"${caseHandleInfoDTO.contactIsBussinessAddress eq 'Y'?'disabled':''}",
					width:'200px'
				});
				$('#${stuff }_contactAddress').textbox('textbox').attr('maxlength', 100);
			}
			// 聯繫聯絡人
			if ($('#${stuff }_contactUser').length > 0) {
				$('#${stuff }_contactUser').textbox({
					required:requiredUser,
					missingMessage:"請輸入聯繫聯絡人",
					width:'200px'
				});
				$('#${stuff }_contactUser').textbox('textbox').attr('maxlength', 50);
			}
			// 聯繫聯絡人電話
			if ($('#${stuff }_contactUserPhone').length > 0) {
				$('#${stuff }_contactUserPhone').textbox({
					required:requiredUserPhone,
					missingMessage:"請輸入聯繫聯絡人電話",
					width:'250px'
				});
				$('#${stuff }_contactUserPhone').textbox('textbox').attr('maxlength', 20);
			}
			// 聯繫聯絡人手機
			if ($('#${stuff }_contactMobilePhone').length > 0) {
				$('#${stuff }_contactMobilePhone').textbox({
					validType:'twMobile',
					invalidMessage:"聯繫聯絡人手機限輸入09開頭且長度為10碼的數字，請重新輸入",
					width:'200px'
				});
				$('#${stuff }_contactMobilePhone').textbox('textbox').attr('maxlength', 10);
			}
			// 聯繫聯絡人EMAIL
			if ($('#${stuff }_contactUserEmail').length > 0) {
				$('#${stuff }_contactUserEmail').textbox({
					validType:'email',
					invalidMessage:"聯繫聯絡人Email格式有誤，請重新輸入",
					width:'250px'
				});
				$('#${stuff }_contactUserEmail').textbox('textbox').attr('maxlength', 50);
			}
		}
		
		// 特店名稱
		if ($('#${stuff }_merchantName').length > 0) {
			$('#${stuff }_merchantName').textbox();
		}
		
		// 舊特店代號
		if ($('#${stuff }_oldMerchantCode').length > 0) {
			$('#${stuff }_oldMerchantCode').textbox({
				validType:'maxLength[20]'
			});
			$('#${stuff }_oldMerchantCode').textbox('textbox').attr('maxlength', 20);
		}
		// 特店區域
		$('#${stuff }_merLocationName').textbox();
		// 特店聯絡人
		$('#${stuff }_contact').textbox();
		// 特店聯絡人電話1
		$('#${stuff }_contactTel').textbox();
		// 特店聯絡人電話2
		$('#${stuff }_contactTel2').textbox();
		// 特店聯絡人行動電話
		$('#${stuff }_merMobilePhone').textbox();
		// 營業時間起
		$('#${stuff }_merOpenHour').textbox();
		// 營業時間迄
		$('#${stuff }_merCloseHour').textbox();
		//特店聯絡人Email
		$("#${stuff}_merContactEmail").textbox();
		// AO人員
		<c:choose>
			<c:when test="${(not empty caseHandleInfoDTO.caseId and (caseHandleInfoDTO.customerId eq gpCustomerId)) or (logonUser.companyId eq gpCustomerId and caseCategory eq caseCategoryAttr.INSTALL.code)}">
				$('#${stuff }_merAoName').textbox({
					readonly:'true',
					required:'true',
					missingMessage:'請輸入AO人員',
					disabled:"${(empty caseHandleInfoDTO.aoName or caseHandleInfoDTO.aoName eq '') ? '':'disabled'}"
				});
				// 標記
				$("#aoNameLabel").html("AO人員:<span class=\"red\">*</span>");
			</c:when>
			<c:otherwise>
				$('#${stuff }_merAoName').textbox({disabled:'disabled'});
			</c:otherwise>
		</c:choose>
		// AO Email
		$('#${stuff }_aoEmail').textbox();
		// 營業地址
		$('#${stuff }_location').combobox({
			disabled:true,
			width:'80px',
			onChange:function(newValue, oldValue) {
				locationChange("${stuff }_locationPostCode", newValue, oldValue);
			} 
		});
		$('#${stuff }_location').addClass("easyui-combobox");
		$("#${stuff }_locationPostCode").combobox({
			disabled:true,
			valueField:'value',
			textField:'name',
			width:'120px'
		});
		$('#${stuff }_merBusinessAddress').textbox({
			disabled:true,
			width:'170px'
		});
	}
	
		//是否進行了修改特點表頭操作
		var isUpdateHeader = true;
		//是否需要去默認選中同特店情況
		var isCheckBox = false;
		/*
		點選特點代號后的查詢按鈕，查詢所選客戶下的特店信息
		*/
		function chooseMID(){
			$("#caseDialogMsg").html("");
			//獲取客戶ID，如果客戶ID為空，則提示請輸入客戶
			var companyId= $("#customerId").val();
			if (companyId == null || companyId == "") {
				companyId = $("#${stuff }_customer").combobox("getValue");
				if (companyId == null || companyId == "") {
					$("#caseDialogMsg").html("請輸入客戶");
					handleScrollTop('createDlg','caseDialogMsg');
					handleScrollTop('editDlg','caseDialogMsg');
					return false;
				} else {
					$("#customerId").val(companyId);
				}
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
				href: "${contextPathMerchant}/merchant.do",
				modal: true,
				onBeforeLoad : function(){
					// clear查詢Mid對話框
					if(viewChooseMid){
						viewChooseMid.panel('clear');
					}
				},
				onBeforeOpen : function(){
					// clear查詢Mid對話框
					if(viewChooseMid){
						viewChooseMid.panel('clear');
					}
				},
				onLoad : function() {
					textBoxSetting("merchanInfoDlg");
					if(typeof settingDialogPanel != 'undefined' && settingDialogPanel instanceof Function) {
						settingDialogPanel('merchanInfoDlg');
					}
					//settingDialogPanel('merchanInfoDlg');
				},
			});
		}
		/*
		* chooseMid選中事件
		*/
		function chooseMidOnCheck(index, row){
			if (row == null) {
				$("#DTIDmsg").html("請選擇一筆資料");
				return false;
			}
			var merchantId = row.merchantId;
			javascript:dwr.engine.setAsync(false);
			isUpdateHeader = false;
			isCheckBox = true;
			//根據所選的特店獲取對應的特店表頭下拉列表
			ajaxService.getMerchantHeaderList("", "", merchantId, function(result){
				if (result != null) {
					$("#${stuff }_merchantHeaderId").combobox("loadData",initSelect(result));
					$("#${stuff }_merchantHeaderId").combobox("setValue","");
				// Bug #2968 重置下拉框選中
				} else {
					$("#${stuff }_merchantHeaderId").combobox("loadData",initSelect(null));
					$("#${stuff }_merchantHeaderId").combobox("setValue","");
				}
			});
			//根據所選擇特點代號的特點表頭，獲取特店信息
			ajaxService.getMerchantDTOById(merchantId,"","", function(result){
				//將查詢結果賦值到特店資訊各欄位
				loadCaseMerchantData(null);
				$("#merchantId").val(result.merchantId);
				$("#${stuff }_merMid").textbox("setValue", result.merchantCode);
				$("#${stuff }_merchantName").textbox("setValue", result.name);
				
				$("#changeMerchat").val(result.merchantCode);
				//清空特點資訊中的同特點情況信息
				//	removeEqualsMerInstallInfo();
				if (${caseCategoryAttr.UPDATE.code eq caseCategory }) {
					removeEqualsMerInstallInfo();
				}
				viewChooseMid.dialog('close');
				//viewChooseMid.dialog('clear');
			});
			isUpdateHeader = true;
			isCheckBox = false;
			javascript:dwr.engine.setAsync(true);
		}
		/*
		根據輸入的特店代號以及客戶獲取對應的特店信息
		*/
		function getMerchantInfo() {
			isUpdateHeader=false;
			isCheckBox = true;
			$("#caseDialogMsg").html("");
			var customerId=$("#customerId").val();
			var merchantCode=$("#${stuff }_merMid").val();
			if (customerId == "") {
				customerId = $("#${stuff }_customer").combobox("getValue");
				if (customerId == "") {
					$("#caseDialogMsg").html("請輸入客戶");
					handleScrollTop('createDlg','caseDialogMsg');
					handleScrollTop('editDlg','caseDialogMsg');
					return false;
				} else {
					$("#customerId").val(customerId);
				}
			}
			if (merchantCode == "") {
				$("#caseDialogMsg").html("請輸入特店代號");
				handleScrollTop('createDlg','caseDialogMsg');
				handleScrollTop('editDlg','caseDialogMsg');
				return false;
			}
			javascript:dwr.engine.setAsync(false);
			//清空當前特店信息
			loadCaseMerchantData(null);
			if (${caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.OTHER.code eq caseCategory}) {
				removeEqualsMerInstallInfo();
			}
			//根據客戶與特點代號獲取特點信息
			ajaxService.getMerchantDTOById("",merchantCode,customerId, function(result){
				if (result != null) {
					$("#changeMerchat").val(merchantCode);
					
					$("#merchantId").val(result.merchantId);
					$("#${stuff }_merMid").textbox("setValue", merchantCode);
					$("#${stuff }_merchantName").textbox("setValue", result.name);
					//根據所選的特店獲取對應的特店表頭下拉列表
					ajaxService.getMerchantHeaderList(customerId, merchantCode, "", function(result){
						if (result != null) {
							$("#${stuff }_merchantHeaderId").combobox("loadData",initSelect(result));
							$("#${stuff }_merchantHeaderId").combobox("setValue","");
						// Bug #2968 重置下拉框選中
						} else {
							$("#${stuff }_merchantHeaderId").combobox("loadData",initSelect(null));
							$("#${stuff }_merchantHeaderId").combobox("setValue","");
						}
					});
				} else {
					$("#${stuff }_merMid").textbox("setValue", merchantCode);
					$("#${stuff }_merchantHeaderId").combobox("loadData",initSelect(null));
					$("#${stuff }_merchantHeaderId").combobox("setValue","");
					$("#caseDialogMsg").html("查無資料");
					handleScrollTop('createDlg','caseDialogMsg');
					handleScrollTop('editDlg','caseDialogMsg');
				}
				isUpdateHeader=true;
				isCheckBox = false;
			});
			javascript:dwr.engine.setAsync(true);
		}

		/*
		修改特點信息--點選修改特店按鈕
		*/
		function updateMerchant() {
			$("#caseDialogMsg").html("");
			var customerId = $("#customerId").val();
			var merchantCode = $("#${stuff }_merMid").textbox("getValue");
			if (customerId == "") {
				$("#caseDialogMsg").html("請輸入客戶");
				handleScrollTop('createDlg','caseDialogMsg');
				handleScrollTop('editDlg','caseDialogMsg');
			} else {
				if (merchantCode != "") {
					//根據特店代號以及客戶獲取對應的特點信息。用於獲取該特店ID
					ajaxService.getMerchantDTOById("",merchantCode,customerId, function(result){
						if (result != null) {
							$("#merchantId").val(result.merchantId);
							//初始化修改特點信息頁面
							initEditMerchant(result.merchantId);
						} else {
							$("#caseDialogMsg").html("查無資料");
							handleScrollTop('createDlg','caseDialogMsg');
							handleScrollTop('editDlg','caseDialogMsg');
						}
					});
				} else {
					$("#caseDialogMsg").html("請輸入特店代號");
					handleScrollTop('createDlg','caseDialogMsg');
					handleScrollTop('editDlg','caseDialogMsg');
				}
			}
		}
		/*
		初始化修改特點信息頁面
		merchantId：特點ID
		*/
		function initEditMerchant(merchantId){
			var viewEditMerchat = $('#merchanEditDlg').dialog({
				title: "修改客戶特店維護", 
				width: 720, 
				height: 380, 
				top:10,
				closed: false, 
				cache: false, 
				queryParams : {
					actionId : 'initEdit',
					merchantId : merchantId
				}, 
				href: "${contextPathMerchant}/merchant.do",
				modal: true,
				onLoad : function() {
					textBoxSetting("merchanEditDlg");
				},
				buttons : [{
					text:'儲存',
					width:88,
					iconCls:'icon-ok',
					handler: function(){					
						var f = viewEditMerchat.find("form");
						if(f.form("validate")){
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
								url: "${contextPathMerchant}/merchant.do?actionId=save",
								data:saveParam,
								type:'post', 
								cache:false, 
								dataType:'json', 
								success:function(result){
									if (result.success) {
										var merchantId = $("#merchantId").val();
										//獲取修改完成后的特店信息，更新特點代號
										ajaxService.getMerchantDTOById(merchantId,"","", function(result){
											if (result != null) {
												$("#${stuff }_merMid").textbox("setValue", result.merchantCode);
												$("#${stuff }_merchantName").textbox("setValue", result.name);
											}
										});
										viewEditMerchat.dialog('close');
									}else{
										$("#msg").text(result.msg);
									}
								},
								error:function(){
									$.messager.alert('提示', "客戶特店資料修改失敗", 'error');
								}
							});
						}
					}
				},{
					text:'取消',
					width:88,
					iconCls:'icon-cancel',
					handler: function(){
						$.messager.confirm('確認對話框','確認取消?', function(isCancle) {
							if (isCancle) {
								viewEditMerchat.dialog('close');
							}	
						});
					}
				}]
			});
		}

		/*
		特點表頭異動事件
		*/
		<c:if test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.OTHER.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
			$("#${stuff }_merchantHeaderId").combobox({
				onChange:function(newValue, oldValue){
					$("#caseDialogMsg").html("");
					if (isUpdateHeader) {
						isCheckBox = true;
						//依據所選表頭獲取對應的特店信息
						ajaxService.getMerchantHeaderDTOById(newValue, function(result){
							//賦值與特店個欄位
							loadCaseMerchantData(result, true);
							if (result == null && ${caseCategoryAttr.UPDATE.code eq caseCategory || caseCategoryAttr.OTHER.code eq caseCategory}) {
								removeEqualsMerInstallInfo();
							}
							isUpdateHeader = true;
							isCheckBox = false;
						});
					}
				}
			});
		</c:if>

		/*
		初始化修改特點表頭頁面--點選修改特點表頭按鈕
		*/
		function updateMerchantHeader() {
			var merchantHeaderId = $("#${stuff }_merchantHeaderId").combobox("getValue");
			if (merchantHeaderId != "") {
				var queryParams = {
					actionId : 'initEdit',
					merchantHeaderId : merchantHeaderId
				};
				$('#merchanEditDlg').dialog('clear');
				//調用特店表頭維護的修改頁面
				viewEditMerchantHeader('merchanEditDlg', '修改特店表頭維護', queryParams, successCallBack, null, '<%=IAtomsConstants.ACTION_INIT_EDIT%>', "${contextPathMerchant}", false);
			} else {
				$("#caseDialogMsg").html("請輸入特店表頭");
				handleScrollTop('createDlg','caseDialogMsg');
				handleScrollTop('editDlg','caseDialogMsg');
			}
			
		}

		/*
		修改特點表頭成功事件
		*/
		function successCallBack(obj, dlg) {
			var merchantId = $("#merchantId").val();
			javascript:dwr.engine.setAsync(false);
			isUpdateHeader = false;
			isCheckBox = true;
			//當前案件類型如果是裝機或者異動，才具有修改特點表頭功能
			var merchantHeaderId = $("#${stuff }_merchantHeaderId").combobox("getValue");
			//重新加載特點表頭下拉列表
			ajaxService.getMerchantHeaderList("","",merchantId, function(result){
				if (result != null) {
					$("#${stuff }_merchantHeaderId").combobox("loadData",initSelect(result));
				}
			});
			//修改完成后從新獲取當前表頭對應的特店信息，並將查詢結果賦值到各個欄位
			ajaxService.getMerchantHeaderDTOById(merchantHeaderId, function(result){
				loadCaseMerchantData(result);
			});
			isUpdateHeader = true;
			isCheckBox = false;
			javascript:dwr.engine.setAsync(true);
			//dlg.dialog('close');
		}
		
		/*
		點擊同特店營業地址、同特店聯絡人、同特店聯繫方式複選框
		obj：當前複選框
		equalsId：例如點選同特店營業地址，則equalsId代表特店營業地址的欄位ID
		id:需要賦值到某欄位的欄位ID
		*/
		function equalsMerBusiness(obj, equalsId, id) {
			//由於裝機地址，還有個特店地址-市區的下拉列表欄位，所以需要單獨賦值
			var tempValue = $("#${stuff }_location").combobox("getValue");
			var contactPostCode = $("#${stuff }_locationPostCode").combobox("getValue");
			if (id == '${stuff }_merInstallAddress') {
				//將營業地址的市區賦值到營業地址的市區，並將欄位設為不可編輯
				$("#${stuff }_installedAdressLocation").combobox("readonly",false);
				$("#${stuff }_installedAdressLocation").combobox(obj.checked? tempValue=="" ? "readonly":'disable':'enable');
				$("#${stuff }_installedAdressLocation").combobox("setValue", tempValue);
				$("#${stuff }_installedPostCode").combobox("readonly",false);
				$("#${stuff }_installedPostCode").combobox(obj.checked? contactPostCode=="" ? "readonly":'disable':'enable');
				$("#${stuff }_installedPostCode").combobox("setValue", contactPostCode);
			} else if (id == '${stuff}_contactAddress') {
				$("#${stuff }_contactAddressLocation").combobox("readonly",false);
				$("#${stuff }_contactAddressLocation").combobox(obj.checked? tempValue=="" ? "readonly":'disable':'enable');
				$("#${stuff }_contactAddressLocation").combobox("setValue", tempValue);
				$("#${stuff }_contactPostCode").combobox("readonly",false);
				$("#${stuff }_contactPostCode").combobox(obj.checked? contactPostCode=="" ? "readonly":'disable':'enable');
				$("#${stuff }_contactPostCode").combobox("setValue", contactPostCode);
			}
			$("#"+id).textbox('enable');
			var value = $("#"+equalsId).val();
			$("#"+id).textbox("setValue", value);
			if (value == "" && obj.checked) {
				$("#"+id).textbox('readonly');
			} else if(value == "" && !obj.checked) {
				$("#"+id).textbox("readonly",false);
			} else {
				$("#"+id).textbox(obj.checked?"disable":'enable');
			}
			$("#"+id).textbox('enableValidation');
		}

		/*
		清除同特店營業地址、同特店聯絡人、同特店聯繫方式相應欄位的內容
		*/
		function removeEqualsMerInstallInfo() {
			if (${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory }) {
				$("#${stuff }_merInstallAddress").textbox("setValue", "");
				$("#${stuff }_installedAdressLocation").combobox("setValue", "");
				$("#${stuff }_merInstallUser").textbox("setValue", "");
				$("#${stuff }_merInstallPhone").textbox("setValue", "");
				$("#${stuff }_installedContactMobilePhone").textbox("setValue", "");
				$("#${stuff }_installedContactEmail").textbox("setValue", "");
				$("#${stuff }_merInstallAddress").textbox("enable");
				$("#${stuff }_merInstallAddress").textbox("readonly", false);
				$("#${stuff }_merInstallAddress").textbox("enableValidation");
				$("#${stuff }_installedAdressLocation").combobox("enable");
				$("#${stuff }_installedAdressLocation").combobox("readonly", false);
				$("#${stuff }_installedAdressLocation").combobox("enableValidation");
				$("#${stuff }_installedPostCode").combobox("enable");
				$("#${stuff }_installedPostCode").combobox("readonly", false);
				$("#${stuff }_installedPostCode").combobox("enableValidation");
				$("#${stuff }_merInstallUser").textbox("enable");
				$("#${stuff }_merInstallPhone").textbox("enable");
				$("#${stuff }_merInstallPhone").textbox("readonly", false);
				$("#${stuff }_merInstallUser").textbox("readonly", false);
				$("#${stuff }_installedContactMobilePhone").textbox("readonly", false);
				$("#${stuff }_installedContactEmail").textbox("readonly", false);
				$("#${stuff }_merInstallUser").textbox("enableValidation");
				$("#${stuff }_merInstallPhone").textbox("enableValidation");
				$("#${stuff }_installedContactMobilePhone").textbox("enableValidation");
				$("#${stuff }_installedContactEmail").textbox("enableValidation");
				$("#${stuff }_isBussinessAddress").removeAttr("checked");
				$("#${stuff }_isBussinessContact").removeAttr("checked");
				$("#${stuff }_isBussinessContactPhone").removeAttr("checked");
				$("#${stuff }_isBussinessContactMobilePhone").removeAttr("checked");
				$("#${stuff }_isBussinessContactEmail").removeAttr("checked");
			} else {
				$("#${stuff }_contactAddress").textbox("setValue", "");
				$("#${stuff }_contactAddressLocation").combobox("setValue", "");
				$("#${stuff }_contactAddressLocation").combobox("enable");
				$("#${stuff }_contactAddressLocation").combobox("readonly", false);
				$("#${stuff }_contactPostCode").combobox("setValue", "");
				$("#${stuff }_contactPostCode").combobox("enable");
				$("#${stuff }_contactPostCode").combobox("readonly", false);
				$("#${stuff }_contactUser").textbox("setValue", "");
				$("#${stuff }_contactUserPhone").textbox("setValue", "");
				$("#${stuff }_contactMobilePhone").textbox("setValue", "");
				$("#${stuff }_contactUserEmail").textbox("setValue", "");
				$("#${stuff }_contactAddress").textbox("enable");
				$("#${stuff }_contactAddress").textbox("readonly", false);
				$("#${stuff }_contactUserPhone").textbox("enable");
				$("#${stuff }_contactUserPhone").textbox("readonly", false);
				$("#${stuff }_contactMobilePhone").textbox("enable");
				$("#${stuff }_contactMobilePhone").textbox("readonly", false);
				$("#${stuff }_contactUserEmail").textbox("enable");
				$("#${stuff }_contactUserEmail").textbox("readonly", false);
				$("#${stuff }_contactUser").textbox("enable");
				$("#${stuff }_contactUser").textbox("readonly", false);
				$("#${stuff }_contactUser").textbox("enableValidation");
				$("#${stuff }_contactUserPhone").textbox("enableValidation");
				$("#${stuff }_contactAddress").textbox("enableValidation");
				$("#${stuff }_contactMobilePhone").textbox("enableValidation");
				$("#${stuff }_contactUserEmail").textbox("enableValidation");
				$("#${stuff }_contactAddressLocation").combobox("enableValidation");
				$("#${stuff }_contactIsBussinessAddress").removeAttr("checked");
				$("#${stuff }_contactIsBussinessContact").removeAttr("checked");
				$("#${stuff }_contactIsBussinessContactPhone").removeAttr("checked");
				$("#${stuff }_contactIsBussinessContactMobilePhone").removeAttr("checked");
				$("#${stuff }_contactIsBussinessContactEmail").removeAttr("checked");
			}
				
		}
		
		function updateContact(isEqualsAddressId, addressId, locationId, locationPostCodeId, isEqualsContactId, userId, isEqualsContactPhoneId, phoneId,
				isEqualsMoblePhone, moblePhoneId, isEqualContactEmail, contactEmailId, result) {
			if ($("#" + isEqualsAddressId).is(":checked")) {
				uploadCheckbox(isEqualsAddressId, true, addressId, result == null?"":result.businessAddress);
				//由於裝機地址，還有個特店地址-市區的下拉列表欄位，所以需要單獨賦值
				$("#" + locationId).combobox("readonly", false);
				$("#" + locationId).combobox('enable');
				$("#" + locationPostCodeId).combobox("readonly", false);
				$("#" + locationPostCodeId).combobox('enable');
				if (result == null || result.location == "") {
					$("#" + locationId).combobox("readonly",true);
				} else if (result != null && result.location != "") {
					$("#" + locationId).combobox('disable');
				}
				if (result == null || result.postCodeId == null || result.postCodeId == "") {
					$("#" + locationPostCodeId).combobox("readonly",true);
				} else {
					$("#" + locationPostCodeId).combobox('disable');
				}
				// Bug #2968 重置下拉框選中
				$("#" + locationId).combobox("setValue", result==null?"":result.location==null?"":result.location);
				$("#" + locationPostCodeId).combobox("setValue", result==null?"":result.postCodeId==null?"":result.postCodeId);
			}
			//核檢同特店聯繫人是否勾選
			if ($("#" + isEqualsContactId).is(":checked")) {
				uploadCheckbox(isEqualsContactId, true, userId, result == null?"":result.contact);

			}
			//核檢是否同特店聯絡人電話
			if ($("#" + isEqualsContactPhoneId).is(":checked")) {
				uploadCheckbox(isEqualsContactPhoneId, true, phoneId, result == null?"":result.contactTel);
			}
			
			//核檢同特店聯繫人行動電話是否勾選
			if ($("#" + isEqualsMoblePhone).is(":checked")) {
				uploadCheckbox(isEqualsMoblePhone, true, moblePhoneId, result == null?"":result.phone);

			}
			//核檢是否同特店聯絡人Email
			if ($("#" + isEqualContactEmail).is(":checked")) {
				uploadCheckbox(isEqualContactEmail, true, contactEmailId, result == null?"":result.contactEmail);
			}
		}
		
		/*
		將異動以及查詢完成之後的結果賦值到特店的相應欄位
		result：特店信息
		*/
		function loadCaseMerchantData(result, isUpdate) {
			//將特點信息賦值到相應特店欄位
			if (!isUpdate) {
				$("#merchantId").val(result == null?"":result.merchantId);
				$("#${stuff }_merMid").textbox('setValue', result == null?"":result.merchantCode);
				$("#${stuff }_merchantName").textbox('setValue', result == null?"":result.name);
			}
			if (${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory} && isCheckBox) {
				//核檢同特店地址是否勾選
				updateContact("${stuff }_isBussinessAddress", "${stuff }_merInstallAddress", "${stuff }_installedAdressLocation", "${stuff }_installedPostCode", "${stuff }_isBussinessContact",
						"${stuff }_merInstallUser", "${stuff }_isBussinessContactPhone", "${stuff }_merInstallPhone", 
						"${stuff }_isBussinessContactMobilePhone", "${stuff }_installedContactMobilePhone", "${stuff }_isBussinessContactEmail", "${stuff }_installedContactEmail", result);
				
			}
			if (${caseCategoryAttr.OTHER.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory} && isCheckBox) {
				updateContact("${stuff }_contactIsBussinessAddress", "${stuff }_contactAddress", "${stuff }_contactAddressLocation", "${stuff }_contactPostCode", "${stuff }_contactIsBussinessContact",
						"${stuff }_contactUser", "${stuff }_contactIsBussinessContactPhone", "${stuff }_contactUserPhone",
						"${stuff }_contactIsBussinessContactMobilePhone", "${stuff }_contactMobilePhone", "${stuff }_contactIsBussinessContactEmail", "${stuff }_contactUserEmail", result);
			}
			//案件類別為裝機或者異動時，特點表頭的顯示為下拉列表
			if(${caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.OTHER.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}) {
				//根據所選的特店獲取對應的特店表頭下拉列表
				$("#${stuff }_merchantHeaderId").combobox('setValue', result == null?"":result.merchantHeaderId);
			} else {
				$("#${stuff }_merchantHeaderName").textbox('setValue', result == null?"":result.headerName);
				// Bug #2968 重置下拉框選中
				$("#${stuff }_installedAdressLocation").combobox("setValue", result==null?"":result.installedAdressLocation==null?"":result.installedAdressLocation);
				$("#${stuff }_merInstallAddress").textbox("setValue", result == null?"":result.installedAdress);
			}
			//將特點信息賦值到相應特店欄位
			$("#${stuff }_merLocationName").textbox('setValue', result == null?"":result.areaName);
			$("#${stuff }_contact").textbox('setValue',result == null?"": result.contact);
			$("#${stuff }_contactTel").textbox('setValue', result == null?"":result.contactTel);
			$("#${stuff }_contactTel2").textbox('setValue',result == null?"": result.contactTel2);
			$("#${stuff }_merMobilePhone").textbox('setValue', result == null?"":result.phone);
			$("#${stuff }_location").combobox('setValue', result == null?"":result.location);
			//locationChange("${stuff }_locationPostCode", result == null?"":result.location, "");
			$("#${stuff }_locationPostCode").combobox('setValue', result == null?"":result.postCodeId == null?"":result.postCodeId);
			$("#${stuff }_merOpenHour").textbox('setValue', result == null?"":result.openHour);
			$("#${stuff }_merCloseHour").textbox('setValue', result == null?"":result.closeHour);
			$("#${stuff }_merAoName").textbox('setValue', result == null?"":result.aoName);
			$("#${stuff }_merContactEmail").textbox('setValue', result == null?"":result.contactEmail);
			// Task #2616 AO人員必填
			var aoNameOptions = $("#${stuff }_merAoName").textbox('options');
			if(aoNameOptions.required){
				if(result != null){
					if(!isEmpty(result.aoName)){
						aoNameOptions.required = true;
						aoNameOptions.disabled = true;
						$("#${stuff }_merAoName").textbox(aoNameOptions);
						// 限制欄位長度
						textBoxDefaultSetting($("#${stuff }_merAoName"));
					} else {
						aoNameOptions.required = true;
						aoNameOptions.disabled = false;
						$("#${stuff }_merAoName").textbox(aoNameOptions);
						// 限制欄位長度
						textBoxDefaultSetting($("#${stuff }_merAoName"));
					}
				} else {
					aoNameOptions.required = true;
					aoNameOptions.disabled = false;
					$("#${stuff }_merAoName").textbox(aoNameOptions);
					// 限制欄位長度
					textBoxDefaultSetting($("#${stuff }_merAoName"));
				}
			} else {
				if($("#isGpCustomer").val() == 'Y' && ((result == null) || (result != null && isEmpty(result.aoName)))){
					aoNameOptions.required = true;
					aoNameOptions.disabled = false;
					$("#${stuff }_merAoName").textbox(aoNameOptions);
					// 限制欄位長度
					textBoxDefaultSetting($("#${stuff }_merAoName"));
				}
			}
			
			$("#${stuff }_aoEmail").textbox('setValue', result == null?"":result.aoemail);
			
			$("#${stuff }_merBusinessAddress").textbox('setValue',result == null?"":result.businessAddress);
			
			// 拿到特點區域信息放到隱藏域
			$("#${stuff }_merLocation").val(result == null?"":result.area);
			// 特點名稱
			//$("#${stuff }_merchantName").textbox('setValue', result == null?"":result.name);
			//獲取VIP節點
			var isVips = document.getElementsByName("${stuff }_isVip");
			//根據查詢結果中的是否VIP，選擇特店區塊的是否VIP
			if(isVips != null && isVips.length >0){
				for(var i=0;i<isVips.length;i++){
					if(result != null && "Y" == result.isVip){
						if("Y" == isVips[i].value){
							isVips[i].checked = true;
						}
					}else{
						if("N" == isVips[i].value){
							isVips[i].checked = true;
						}
					}
				}
			}
		}
		
		/*
		根據選擇的DTID，將查詢結果賦值到各個欄位
		result：案件資料
		flag:是否修改案件资料 点击带值
		*/
		function loadMerchantInfoElement(result, flag){
			javascript:dwr.engine.setAsync(false);
			var cmsCase = $("#changeCmsCase").val();
			var postCodes;
			if (${caseCategoryAttr.UPDATE.code eq caseCategory}) {
				postCodes = $("#${stuff }_installedPostCode");
			} else {
				postCodes = $("#${stuff }_contactPostCode");
			}
			var postCodesOptions = postCodes.combobox("options");
			if ("<%=IAtomsConstants.PARAM_YES %>" == cmsCase) {
				postCodesOptions.invalidMessage = '請輸入郵遞區號';
				postCodesOptions.required = true;
				postCodesOptions.validType='requiredDropList[\'郵遞區號\']';
				postCodes.combobox(postCodesOptions);
				postCodes.addClass("easyui-combobox");
			} else {
				postCodesOptions.invalidMessage = '';
				postCodesOptions.required = false;
				postCodesOptions.validType='';
				postCodes.combobox(postCodesOptions);
				postCodes.addClass("easyui-combobox");
			}
			//異動情況下，特店表頭為下拉列表，賦值觸發聯動事件
			// Task #3359其他情況下，特店表頭為下拉列表，賦值觸發聯動事件
			if (${caseCategoryAttr.UPDATE.code eq caseCategory} || ${caseCategoryAttr.OTHER.code eq caseCategory} || ${caseCategoryAttr.PROJECT.code eq caseCategory}) {
				//根據特點ID查詢特點表頭
				if (result != null) {
					ajaxService.getMerchantHeaderList("","",result.merchantId, function(date){
						if (date != null) {
							$("#${stuff }_merchantHeaderId").combobox("loadData",initSelect(date));
							$("#${stuff }_merchantHeaderId").combobox("setValue",result.merchantHeaderId==null?"":result.merchantHeaderId);
						}
					});
				} else {
					$("#${stuff }_merchantHeaderId").combobox("loadData",initSelect(null));
					$("#${stuff }_merchantHeaderId").combobox("setValue","");
				}
				
				//$("#${stuff }_merchantHeaderId").combobox("setValue", result==null?"":result.merchantHeaderId==null?"":result.merchantHeaderId);
				$("#merchantId").val(result == null?"":result.merchantId);
				$("#${stuff }_merMid").textbox('setValue', result == null?"":result.merchantCode);
				
				$("#changeMerchat").val(result == null?"":result.merchantCode);
			} else {
				loadCaseMerchantData(result, false);
				$("#${stuff }_merchantHeaderId").val(result==null?"":result.merchantHeaderId);
			}
			if (result != null) {
				//将contact相关放入隐藏域 2018/01/15
				var addressLocation;
				if (${caseCategoryAttr.UPDATE.code eq caseCategory}) {
					addressLocation = $("#${stuff }_installedAdressLocation").combobox('getValue');
					//異動情況下，賦值裝機地址等裝機信息
					/* uploadCheckbox("${stuff }_isBussinessContactPhone", true, '${stuff }_merInstallPhone', result.contactTel);
					uploadCheckbox("${stuff }_isBussinessContact", true, '${stuff }_merInstallUser', result.contact); */
					uploadCheckbox("${stuff }_isBussinessContactPhone", result.isBussinessContactPhone == 'Y', '${stuff }_merInstallPhone', result.isBussinessContactPhone == 'Y'?result.contactTel:result.installedContactPhone);
					uploadCheckbox("${stuff }_isBussinessContact", result.isBussinessContact == 'Y', '${stuff }_merInstallUser', result.isBussinessContact == 'Y'?result.contact:result.installedContact);
					uploadCheckbox("${stuff }_isBussinessAddress", result.isBussinessAddress == 'Y', '${stuff }_merInstallAddress', result.isBussinessAddress == 'Y'?result.businessAddress:result.installedAdress);
					
					uploadCheckbox("${stuff }_isBussinessContactMobilePhone", result.isBussinessContactMobilePhone == 'Y', '${stuff }_installedContactMobilePhone', result.isBussinessContactMobilePhone == 'Y'?result.phone:result.installedContactMobilePhone);
					uploadCheckbox("${stuff }_isBussinessContactEmail", result.isBussinessContactEmail == 'Y', '${stuff }_installedContactEmail', result.isBussinessContactEmail == 'Y'?result.contactEmail:result.installedContactEmail);
					if(result.isBussinessAddress && result.isBussinessAddress == 'Y'){
					//	$("#${stuff }_installedAdressLocation").combobox("setValue", result.location);
						// Bug #2968 重置下拉框選中
						$("#${stuff }_installedAdressLocation").combobox("setValue", result.location == null?"":result.location);
						if (addressLocation != "" && (addressLocation == $("#${stuff }_installedAdressLocation").combobox('getValue'))) {
							locationChange("${stuff }_installedPostCode", $("#${stuff }_installedAdressLocation").combobox('getValue'), "");
						}
						$("#${stuff }_installedPostCode").combobox("setValue", result.postCodeId == null?"":result.postCodeId);
						if (result.location != null && result.location != "") {
							$("#${stuff }_installedAdressLocation").combobox('disable');
						} else {
							$("#${stuff }_installedAdressLocation").combobox('readonly', true);
						}
						if (result.postCodeId != null && result.postCodeId != "") {
							$("#${stuff }_installedPostCode").combobox('disable');
						} else {
							$("#${stuff }_installedPostCode").combobox('readonly', true);
						}
					} else {
						$("#${stuff }_installedAdressLocation").combobox("setValue", result.installedAdressLocation == null?"":result.installedAdressLocation);
						if (addressLocation != "" && (addressLocation == $("#${stuff }_installedAdressLocation").combobox('getValue'))) {
							locationChange("${stuff }_installedPostCode", $("#${stuff }_installedAdressLocation").combobox('getValue'), "");
						}
						$("#${stuff }_installedAdressLocation").combobox('enable');
						$("#${stuff }_installedAdressLocation").combobox('readonly', false);
						$("#${stuff }_installedPostCode").combobox("setValue", result.installedPostCode == null?"":result.installedPostCode);
						$("#${stuff }_installedPostCode").combobox('enable');
						$("#${stuff }_installedPostCode").combobox('readonly', false);
					}
					
				} else {
					addressLocation = $("#${stuff }_contactAddressLocation").combobox('getValue');
					var isEquals = result.contactIsBussinessContact;
					// Task #3102 案件同特店資料未設置值預設為 E
					if (isEquals == 'E') {
						isEquals = result.isBussinessContact;
					}
					var contactUser = result.contactUser;
					if (contactUser == null) {
						contactUser = result.installedContact;
					}
					uploadCheckbox("${stuff }_contactIsBussinessContact", 
							isEquals == 'Y', '${stuff }_contactUser', isEquals == 'Y' ? result.contact : contactUser);
							//result.contactIsBussinessContact == null ? result.isBussinessContact : result.contactIsBussinessContact == 'Y'?result.contact:result.contactUser
									//result.contactIsBussinessContact == 'Y', '${stuff }_contactUser', result.contactIsBussinessContact == 'Y'?result.contact:result.contactUser);
					isEquals = result.contactIsBussinessContactPhone;
					// Task #3102 案件同特店資料未設置值預設為 E
					if (isEquals == 'E') {
						isEquals = result.isBussinessContactPhone;
					}
					var phone = result.contactUserPhone;
					if (phone == null) {
						phone = result.installedContactPhone;
					}
					uploadCheckbox("${stuff }_contactIsBussinessContactPhone", 
							isEquals == 'Y', '${stuff }_contactUserPhone', isEquals == 'Y' ? result.contactTel : phone);
					//uploadCheckbox("${stuff }_contactIsBussinessContactPhone", result.contactIsBussinessContactPhone == 'Y', '${stuff }_contactUserPhone', result.contactIsBussinessContactPhone == 'Y'?result.contactTel:result.contactUserPhone);
					//Task #3343
					var isEquals = result.contactIsBussinessContactMobilePhone;
					// Task #3102 案件同特店資料未設置值預設為 E
					if (isEquals == 'E') {
						isEquals = result.isBussinessContactMobilePhone;
					}
					var mobilePhone = result.contactMobilePhone;
					if (mobilePhone == null) {
						mobilePhone = result.installedContactMobilePhone;
					}
					uploadCheckbox("${stuff }_contactIsBussinessContactMobilePhone", 
							isEquals == 'Y', '${stuff }_contactMobilePhone', isEquals == 'Y'?result.phone:mobilePhone);
					//Task #3343
					var isEquals = result.contactIsBussinessContactEmail;
					// Task #3102 案件同特店資料未設置值預設為 E
					if (isEquals == 'E') {
						isEquals = result.isBussinessContactEmail;
					}
					var contactEmail = result.contactUserEmail;
					if (contactEmail == null) {
						contactEmail = result.installedContactEmail;
					}
					uploadCheckbox("${stuff }_contactIsBussinessContactEmail", 
							isEquals == 'Y', '${stuff }_contactUserEmail', isEquals == 'Y'?result.contactEmail:contactEmail);
					isEquals = result.contactIsBussinessAddress;
					// Task #3102 案件同特店資料未設置值預設為 E
					if (isEquals == 'E') {
						isEquals = result.isBussinessAddress;
					}
					var location = result.contactAddressLocation;
					if (location == null) {
						location = result.installedAdressLocation;
					}
					var contactPostCode = result.contactPostCode;
					if (contactPostCode == null) {
						contactPostCode = result.installedPostCode;
					}
					var address = result.contactAddress;
					if (address == null) {
						address = result.installedAdress;
					}
					uploadCheckbox("${stuff }_contactIsBussinessAddress", 
							isEquals == 'Y', '${stuff }_contactAddress', isEquals == 'Y'?result.businessAddress:address);
					
					//uploadCheckbox("${stuff }_contactIsBussinessAddress", result.contactIsBussinessAddress == 'Y', '${stuff }_contactAddress', result.contactIsBussinessAddress == 'Y'?result.businessAddress:result.contactAddress);
					if(result.isBussinessAddress && result.isBussinessAddress == 'Y'){
					//	$("#${stuff }_installedAdressLocation").combobox("setValue", result.location);
						// Bug #2968 重置下拉框選中
						$("#${stuff }_installedAdressLocation").combobox("setValue", result.location==null?"":result.location);
						$("#${stuff }_installedPostCode").combobox("setValue", result.postCodeId==null?"":result.postCodeId);
						$("#${stuff }_merInstallAddress").textbox("setValue", result.businessAddress)
						//$("#${stuff }_installedAdressLocation").combobox('disable');
					} else {
						$("#${stuff }_installedAdressLocation").combobox("setValue", result.installedAdressLocation == null?"":result.installedAdressLocation);
						$("#${stuff }_installedPostCode").combobox("setValue", result.installedPostCode==null?"":result.installedPostCode);
						$("#${stuff }_merInstallAddress").textbox("setValue", result.installedAdress);
					}
					if(isEquals && isEquals == 'Y'){
						//由於裝機地址，還有個特店地址-市區的下拉列表欄位，所以需要單獨賦值
					//	$("#${stuff }_contactAddressLocation").combobox("setValue", result.location);
						// Bug #2968 重置下拉框選中
						$("#${stuff }_contactAddressLocation").combobox("setValue", result.location==null?"":result.location);
						if (addressLocation != "" && (addressLocation == $("#${stuff }_contactAddressLocation").combobox('getValue'))) {
							locationChange("${stuff }_contactPostCode", $("#${stuff }_contactAddressLocation").combobox('getValue'), "");
						}
						$("#${stuff }_contactPostCode").combobox("setValue", result.postCodeId==null?"":result.postCodeId);
						if (result.location != null && result.location != "") {
							$("#${stuff }_contactAddressLocation").combobox('disable');
						} else {
							$("#${stuff }_contactAddressLocation").combobox('readonly', true);
						}
						if (result.postCodeId != null && result.postCodeId != "") {
							$("#${stuff }_contactPostCode").combobox('disable');
						} else {
							$("#${stuff }_contactPostCode").combobox('readonly', true);
						}
						//$("#${stuff }_contactAddressLocation").combobox('disable');
					} else {
						//由於裝機地址，還有個特店地址-市區的下拉列表欄位，所以需要單獨賦值
						$("#${stuff }_contactAddressLocation").combobox("setValue", location == null?"":location);
						if (addressLocation != "" && (addressLocation == $("#${stuff }_contactAddressLocation").combobox('getValue'))) {
							locationChange("${stuff }_contactPostCode", $("#${stuff }_contactAddressLocation").combobox('getValue'), "");
						}
						$("#${stuff }_contactAddressLocation").combobox('enable');
						$("#${stuff }_contactAddressLocation").combobox('readonly', false);
						$("#${stuff }_contactPostCode").combobox("setValue", contactPostCode == null?"":contactPostCode);
						$("#${stuff }_contactPostCode").combobox('enable');
						$("#${stuff }_contactPostCode").combobox('readonly', false);
					}
					
					$("#${stuff }_isBussinessAddress").val(result.isBussinessAddress);
				}
				$("#${stuff }_merchantName").textbox('setValue', result == null?"":result.merchantName);
			} else {
				if (${caseCategoryAttr.UNINSTALL.code eq caseCategory}) {
					uploadCheckbox("${stuff }_contactIsBussinessContact", false, '${stuff }_contactUser', "");
					uploadCheckbox("${stuff }_contactIsBussinessContactPhone", false, '${stuff }_contactUserPhone', "");
					uploadCheckbox("${stuff }_contactIsBussinessAddress", false, '${stuff }_contactAddress', "");
					$("#${stuff }_contactAddressLocation").combobox("readonly",false);
					$("#${stuff }_contactAddressLocation").combobox('enable');
					$("#${stuff }_contactAddressLocation").combobox("setValue", "");
					$("#${stuff }_contactPostCode").combobox("readonly",false);
					$("#${stuff }_contactPostCode").combobox('enable');
					$("#${stuff }_contactPostCode").combobox("setValue", "");
				} else {
					removeEqualsMerInstallInfo();
				}
				
			}
			javascript:dwr.engine.setAsync(true);
		}
		//賦值與特點資料--複選框
		/**
		checkboxId:複選框id
		isChecked:是否選中
		textboxId：文本框id
		value：文本框需要顯示的值
		*/
		function uploadCheckbox(checkboxId, isChecked, textboxId, value) {
			$("#"+textboxId).textbox('enable');
			$("#"+textboxId).textbox('readonly', false);
			$("#"+checkboxId).prop("checked", isChecked);
			if ((value == "" || value == null) && isChecked) {
				$("#"+textboxId).textbox('readonly');
			} else if (value != "" && value != null && isChecked){
				$("#"+textboxId).textbox("disable");
			}
			$("#"+textboxId).textbox("setValue",value);
			$("#"+textboxId).textbox('enableValidation');
		}
		/*
		* 獲得特店部分的參數值
		*/
		function getMerchantParams(){
			var merchantParams = {};
			// 特店代號
		//	merchantParams.merchantCode = $("#${stuff }_merMid").textbox("getValue");
			merchantParams.merchantCode = $("#merchantId").val();
			if (${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory}) {
				// 特店表頭
				merchantParams.merchantHeaderId = $("#${stuff }_merchantHeaderId").combobox("getValue");
				// 裝機地址同營業地址
				if($("#${stuff }_isBussinessAddress").prop("checked")){
					merchantParams.isBussinessAddress = 'Y';
				} else {
					merchantParams.isBussinessAddress = 'N';
				}
				// 裝機聯絡人
				merchantParams.installedContact = $("#${stuff }_merInstallUser").textbox("getValue");
				// 裝機聯絡人同特店聯絡人
				if($("#${stuff }_isBussinessContact").prop("checked")){
					merchantParams.isBussinessContact = 'Y';
				} else {
					merchantParams.isBussinessContact = 'N';
				}
				// 裝機聯絡人電話
				merchantParams.installedContactPhone = $("#${stuff }_merInstallPhone").textbox("getValue");
				// 裝機聯絡人電話同特店聯絡人電話
				if($("#${stuff }_isBussinessContactPhone").prop("checked")){
					merchantParams.isBussinessContactPhone = 'Y';
				} else {
					merchantParams.isBussinessContactPhone = 'N';
				}
				//裝機聯絡人手機
				merchantParams.installedContactMobilePhone = $("#${stuff }_installedContactMobilePhone").textbox("getValue");
				// 裝機聯絡人手機同特店聯絡人行動電話
				if($("#${stuff }_isBussinessContactMobilePhone").prop("checked")){
					merchantParams.isBussinessContactMobilePhone = 'Y';
				} else {
					merchantParams.isBussinessContactMobilePhone = 'N';
				}
				//裝機聯絡人EMAIL
				merchantParams.installedContactEmail = $("#${stuff }_installedContactEmail").textbox("getValue");
				// 裝機聯絡人EMAIL同特店聯絡人EMAIL
				if($("#${stuff }_isBussinessContactEmail").prop("checked")){
					merchantParams.isBussinessContactEmail = 'Y';
				} else {
					merchantParams.isBussinessContactEmail = 'N';
				}
				//将contact相关放入param 2018/01/15 
				/* if(${!empty caseHandleInfoDTO.caseId} && $("#hideForUpdateContactFlag").val() == 'Y'){ 
					// 聯繫地址-縣市
					merchantParams.contactAddressLocation = $("#hideForUpdateContactAddressLocation").val();
					// 聯繫地址
					merchantParams.contactAddress = $("#hideForUpdateContactAddress").val();
					merchantParams.contactIsBussinessAddress = $("#hideForUpdateContactIsBussinessAddress").val();
					// 聯繫聯絡人
					merchantParams.contactUser = $("#hideForUpdateContactUser").val();
					// 聯繫聯絡人同特店聯絡人
					merchantParams.contactIsBussinessContact =  $("#hideForUpdateContactIsBussinessContact").val();
					// 聯繫聯絡人電話
					merchantParams.contactUserPhone = $("#hideForUpdateContactUserPhone").val();
					// 聯繫聯絡人電話同特店聯絡人電話
					merchantParams.contactIsBussinessContactPhone = $("#hideForUpdateContactIsBussinessContactPhone").val();
				} */
			} else {
				//将install相关放入param 2018/01/15
				/* if(${!empty caseHandleInfoDTO.caseId} && $("#hideForUpdateInstalledFlag").val() == 'Y'){
					merchantParams.installedAdressLocation = $("#hideForUpdateInstalledAdressLocation").val();
					merchantParams.isBussinessAddress = $("#hideForUpdateIsBussinessAddress").val();
					merchantParams.installedAdress = $("#hideForUpdateInstalledAdress").val();
					merchantParams.isBussinessContact = $("#hideForUpdateIsBussinessContact").val();
					merchantParams.installedContact =  $("#hideForUpdateInstalledContact").val();
					merchantParams.isBussinessContactPhone = $("#hideForUpdateIsBussinessContactPhone").val();
					merchantParams.installedContactPhone = $("#hideForUpdateInstalledContactPhone").val();
				} */
				// 特店表頭
				if (${caseCategoryAttr.OTHER.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}) {
					merchantParams.merchantHeaderId = $("#${stuff }_merchantHeaderId").combobox("getValue");
				} else {
					merchantParams.merchantHeaderId = $("#${stuff }_merchantHeaderId").val();
				}
				// 聯繫地址-縣市
				merchantParams.contactAddressLocation = $("#${stuff }_contactAddressLocation").combobox("getValue");
				merchantParams.contactPostCode = $("#${stuff }_contactPostCode").combobox("getValue");
				// 聯繫地址
				merchantParams.contactAddress = $("#${stuff }_contactAddress").textbox("getValue");
				// 聯繫地址同特店地址
				if($("#${stuff }_contactIsBussinessAddress").prop("checked")){
					merchantParams.contactIsBussinessAddress = 'Y';
				} else {
					merchantParams.contactIsBussinessAddress = 'N';
				}
				// 聯繫聯絡人
				merchantParams.contactUser = $("#${stuff }_contactUser").textbox("getValue");
				// 聯繫聯絡人同特店聯絡人
				if($("#${stuff }_contactIsBussinessContact").prop("checked")){
					merchantParams.contactIsBussinessContact = 'Y';
				} else {
					merchantParams.contactIsBussinessContact = 'N';
				}
				// 聯繫聯絡人電話
				merchantParams.contactUserPhone = $("#${stuff }_contactUserPhone").textbox("getValue");
				// 聯繫聯絡人電話同特店聯絡人電話
				if($("#${stuff }_contactIsBussinessContactPhone").prop("checked")){
					merchantParams.contactIsBussinessContactPhone = 'Y';
				} else {
					merchantParams.contactIsBussinessContactPhone = 'N';
				}
				//聯繫聯絡人手機
				merchantParams.contactMobilePhone = $("#${stuff }_contactMobilePhone").textbox("getValue");
				//聯繫聯絡人手機同特店聯絡人行動電話
				if($("#${stuff }_contactIsBussinessContactMobilePhone").prop("checked")){
					merchantParams.contactIsBussinessContactMobilePhone = 'Y';
				} else {
					merchantParams.contactIsBussinessContactMobilePhone = 'N';
				}
				//聯繫聯絡人EMAIL
				merchantParams.contactUserEmail = $("#${stuff }_contactUserEmail").textbox("getValue");
				//聯繫聯絡人EMAIL同特店聯絡人EMAIL
				if($("#${stuff }_contactIsBussinessContactEmail").prop("checked")){
					merchantParams.contactIsBussinessContactEmail = 'Y';
				} else {
					merchantParams.contactIsBussinessContactEmail = 'N';
				}
			}
			// 特店區域
			merchantParams.merLocation = $("#${stuff }_merLocation").val();
			// 裝機地址-縣市
			merchantParams.installedAdressLocation = $("#${stuff }_installedAdressLocation").combobox("getValue");
			merchantParams.installedPostCode = $("#${stuff }_installedPostCode").combobox("getValue");
			// 裝機地址
			merchantParams.installedAdress = $("#${stuff }_merInstallAddress").textbox("getValue");
			return merchantParams;
		}
		
		function locationChange(id, newValue, oldValue) {
			$("#" + id).combobox("setValue", "");
			if (newValue != null && newValue != "") {
				ajaxService.getPostCodeList(newValue, function(result){
					$("#" + id).combobox("loadData",initSelect(result, "郵遞區號"));
				});
			} else {
				$("#" + id).combobox("loadData",initSelect(null, "郵遞區號"));
			}
		}
	</script>