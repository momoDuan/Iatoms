<!--import Java class Start -->
<%@page import="com.cybersoft4u.xian.iatoms.common.IAtomsConstants"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE"%>
<%@page import="cafe.core.bean.identity.LogonUser"%>
<%@ page import="java.util.*,java.lang.*"%>
<%@ page import="org.springframework.util.CollectionUtils"%>
<%@ page import="cafe.core.context.SessionContext" %>
<%@ page import="cafe.core.bean.Message" %>
<%@ page import="cafe.core.bean.PageNavigation" %>
<%@ page import="cafe.core.bean.Parameter" %>
<%@ page import="cafe.workflow.bean.identity.IAccessControlPolicy"%>
<%@ page import="cafe.core.bean.identity.AccessControl"%>
<%@ page import="cafe.core.config.SystemConfigManager" %>
<%@ page import="cafe.core.config.GenericConfigManager" %>
<%@ page import="cafe.core.util.DateTimeUtils" %>
<%@ page import="cafe.core.util.StringUtils"%>
<%@ page import="cafe.core.util.WebApplicationBeanManager" %>
<%@ page import="cafe.core.web.controller.util.ParameterManager" %>
<%@ page import="cafe.core.web.controller.util.HttpRequestHelper"%>
<%@ page import="cafe.core.web.controller.util.SessionHelper"%>
<%@ page import="cafe.workflow.web.controller.util.WfSessionHelper"%>
<%@ page import="cafe.workflow.util.i18NUtil" %>
<!-- import Java class End -->
<%@include file="taglibs.jsp"%>
<!-- Common Paramter start -->
<%
	HttpRequestHelper.disableCaching(response);

IAtomsLogonUser logonUser = (IAtomsLogonUser)SessionHelper.getLogonUser(request);
SessionContext sessionContext = (SessionContext)request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);

Message message = (sessionContext != null) ? sessionContext.getReturnMessage() : null;

if(response.isCommitted()){
	out.clear();
	out = pageContext.pushBody();
}
%>

<!-- Common Paramter end -->
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="datePattern"><fmt:message key="date.format"/></c:set>
<c:set var="logonUser" value="<%=logonUser%>" scope="page"></c:set>
<c:set var="sessionContext" value="<%=sessionContext%>" scope="page"></c:set>
<c:set var="message" value="<%=message%>" scope="page"></c:set>
