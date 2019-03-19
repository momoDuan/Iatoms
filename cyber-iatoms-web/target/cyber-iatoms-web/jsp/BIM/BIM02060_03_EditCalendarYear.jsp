<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.CalendarFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.BimCalendarYearDTO"%>
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
	BimCalendarYearDTO bimCalendarYearDTO = null;
	if (formDTO != null) {
		bimCalendarYearDTO = formDTO.getCalendarYearDTO();
	} 
	if (bimCalendarYearDTO == null) {
		bimCalendarYearDTO = new BimCalendarYearDTO();
	}
	List<Parameter> weekList = (List<Parameter>) SessionHelper.getAttribute(request, useCaseNo, IATOMS_PARAM_TYPE.WEEK.getCode());
	List<String> dayList = new ArrayList<String>();
	if (IAtomsConstants.YES.equals(bimCalendarYearDTO.getMonday())) {
		dayList.add(String.valueOf(Calendar.MONDAY));
	}
	if (IAtomsConstants.YES.equals(bimCalendarYearDTO.getTuesday())) {
		dayList.add(String.valueOf(Calendar.TUESDAY));
	}
	if (IAtomsConstants.YES.equals(bimCalendarYearDTO.getWednesday())) {
		dayList.add(String.valueOf(Calendar.WEDNESDAY));
	}
	if (IAtomsConstants.YES.equals(bimCalendarYearDTO.getThursday())) {
		dayList.add(String.valueOf(Calendar.THURSDAY));
	}
	if (IAtomsConstants.YES.equals(bimCalendarYearDTO.getFriday())) {
		dayList.add(String.valueOf(Calendar.FRIDAY));
	}
	if (IAtomsConstants.YES.equals(bimCalendarYearDTO.getSaturday())) {
		dayList.add(String.valueOf(Calendar.SATURDAY));
	}
	if (IAtomsConstants.YES.equals(bimCalendarYearDTO.getSunday())) {
		dayList.add(String.valueOf(Calendar.SUNDAY));
	}
%>
<c:set var = "bimCalendarYearDTO" value = "<%=bimCalendarYearDTO%>" scope = "page"></c:set>
<html>
<head>
<title></title>
</head>
<body>
	<div data-options="region:'center'" 
		style="position:absolute;width: auto; height: auto; padding: 10px 20px; background: #fff; overflow-y: hidden">
        	<div class="dialogtitle"><font size="4px">修改年度行事曆</font></div>
        	<div><span id="dialogMsg" class="red"></span></div>
       		<form id="formYear" method="post" novalidate>
         	  <table cellpadding="4" style="padding-left: 20px;padding-top: 10px">
         	  	<tr>
         	       <td>年度:<span style="color: red">*</span></td>
         	       <td>
       	       		<input class="easyui-textbox" 
	  	       			name="<%=BimCalendarYearDTO.ATTRIBUTE.YEAR.getValue()%>" 
	  	       			id="<%=BimCalendarYearDTO.ATTRIBUTE.YEAR.getValue()%>"
	  	       			style="width: 150px" 
	  	       			value="<c:out value='${bimCalendarYearDTO.year}'/>"
	  	       			disabled="disabled"/>
         	       </td>
         	    </tr>  
         	    <tr>
         	       <td>週休日星期:<span style="color: red">*</span></td>
         	       <td>
         	       <div id="checkWeekRests" class="div-list" data-list-required='請輸入週休日星期'>
         	       	<cafe:checklistTag name="weekRests" type="checkbox" result="<%=weekList%>" checkedValues="<%=dayList%>"></cafe:checklistTag>
         	       </div>         	        
         	       </td>
         	    </tr>   
         	  </table>
       	 	</form>
       </div>
	<script type="text/javascript">
		$(function(){
			checkedDivListOnchange();
		});
	</script>
</body>
</html>