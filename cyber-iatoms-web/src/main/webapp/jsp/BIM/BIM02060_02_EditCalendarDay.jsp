<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.CalendarFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.BimCalendarDayDTO"%>
<%@ page import="cafe.core.util.StringUtils"%>
<%
	SessionContext ctx = (SessionContext)request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	CalendarFormDTO formDTO = null;
	String useCaseNo = null;
	if (ctx != null) {
		formDTO = (CalendarFormDTO)ctx.getRequestParameter();
		useCaseNo = formDTO.getUseCaseNo();
	} else {
		formDTO = new CalendarFormDTO(); 
	}
	BimCalendarDayDTO bimCalendarDayDTO = null;
	if (formDTO != null) {
		bimCalendarDayDTO = formDTO.getCalendarDayDTO();
	} 
	//是否假日
	List<Parameter> isHolidayList = (List<Parameter>) SessionHelper.getAttribute(request, useCaseNo, IAtomsConstants.YES_OR_NO);
%>
<c:set var = "isHolidayList" value="<%=isHolidayList%>" scope="page"></c:set>
<c:set var = "bimCalendarDayDTO" value = "<%=bimCalendarDayDTO%>" scope = "page"></c:set>
<html>
<head>
<title></title>
</head>
<body>
	<div data-options="region:'center'"
		style="width: auto; height: auto; padding: 10px 20px; background: #fff; overflow-y: hidden" >
       	<div class="dialogtitle"><font size="4px">修改行事曆</font></div>
       	<div><span id="DaydialogMsg" class="red"></span></div>
      		<form id="formDay" method="post" novalidate>
        	  <table cellpadding="4" style="padding-left: 40px;padding-top: 10px">
        	  	<tr>
        	       <td>日期:<span style="color: red">*</span></td>
        	       <td>
        	       	<input class="easyui-textbox" 
       	       			name="<%=BimCalendarDayDTO.ATTRIBUTE.DAY.getValue()%>" 
       	       			id="<%=BimCalendarDayDTO.ATTRIBUTE.DAY.getValue()%>" 
       	       			style="width: 150px" 
       	       			value="<fmt:formatDate value='${bimCalendarDayDTO.day}'  pattern="yyyy/MM/dd"/>"
       	       			disabled="disabled"/>
        	       </td>
        	       <td>是否為假日:<span style="color: red">*</span></td>
        	       <td>
        	       <cafe:checklistTag 
        	       		name="<%=BimCalendarDayDTO.ATTRIBUTE.IS_HOLIDAY.getValue().toString()%>"
						id="<%=BimCalendarDayDTO.ATTRIBUTE.IS_HOLIDAY.getValue().toString()%>" 
						result = "${isHolidayList}" 
        	       		type="radio" 
        	       		checkedValues='<%= StringUtils.toList(StringUtils.hasText(bimCalendarDayDTO.getIsHoliday())?bimCalendarDayDTO.getIsHoliday():"N",",")%>'/>
        	       </td>
        	    </tr>  
        	    <tr>
        	       <td>說明:</td>
        	       <td colspan="3">
        	       		<textarea id="<%=BimCalendarDayDTO.ATTRIBUTE.COMMENT.getValue()%>" 
        	       				name="<%=BimCalendarDayDTO.ATTRIBUTE.COMMENT.getValue()%>"
        	       				class="easyui-textbox"  maxlength="200" multiline="true"
        	       				validType="maxLength[200]" style="height: 70px;width:380px"><c:out value='${bimCalendarDayDTO.comment}'/></textarea> 
        	       </td>
        	    </tr>  
        	  </table>
      	 	</form>
   </div>
</body>
</html>