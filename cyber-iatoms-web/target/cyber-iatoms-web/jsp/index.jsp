<%@ include file="/jsp/common/taglibs.jsp"%>
<%@include file="/jsp/common/common.jsp"%>
<%@page import="cafe.core.config.GenericConfigManager"%>
<%
	// get systemName definition
	String systemNameCode = GenericConfigManager.getInstance().getProperty(IAtomsConstants.PARAM_SYSTEM_NAME_CODE);
%>
<c:set var="systemNameCode" value="<%=systemNameCode %>"> </c:set>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:choose>
	<c:when test="${fn:contains(contextPath, systemNameCode)}">
		<c:redirect url="/logon.do"/>
	</c:when>
	<c:otherwise>
		<c:redirect url="${contextPath }/logon.do"/>
	</c:otherwise>
</c:choose>
