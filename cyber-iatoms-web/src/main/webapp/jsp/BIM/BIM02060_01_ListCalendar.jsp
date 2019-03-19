<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.CalendarFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.BimCalendarYearDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.BimCalendarDayDTO"%>
<%
	SessionContext ctx = (SessionContext)request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	CalendarFormDTO formDTO = null;
	if (ctx != null) {
		formDTO = (CalendarFormDTO)ctx.getRequestParameter();
	} else {
		formDTO = new CalendarFormDTO(); 
	}
	BimCalendarYearDTO bimCalendarYearDTO = null;
	BimCalendarDayDTO bimCalendarDayDTO = null;
	List<BimCalendarDayDTO> bimCalendarDayDTOs = null;
	if (formDTO != null) {
		bimCalendarYearDTO = formDTO.getCalendarYearDTO();
		bimCalendarDayDTOs = formDTO.getCalendarDayDTOs();
	} 
%>
<c:set var = "formDTO" value = "<%=formDTO%>" scope = "page"></c:set>
<c:set var = "bimCalendarYearDTO" value = "<%=bimCalendarYearDTO%>" scope = "page"></c:set>
<c:set var = "bimCalendarDayDTOs" value = "<%=bimCalendarDayDTOs%>" scope = "page"></c:set>
<link href="${contextPath}/css/iatoms_style.css" type="text/css" rel="stylesheet"/> 
<html>
<head>
<%@include file="/jsp/common/easyui-common.jsp"%>
</head>
<body>
<!-- 	<div region="center" style="width: 98%;height: auto; padding: 1px;overflow: hidden;"> -->
<div id="calendarDiv" style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
		<div id="calendarTable" class="easyui-panel" title="行事曆" style="width: 98%; height: auto">
			<table >
				<tr>
					<td>今日:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input class="easyui-textbox" name="currentDate"id="currentDate"  value="" style="width: 150px" disabled="disabled"/>
					</td>
					<td colspan="2" style="padding-left: 150px">
						<form id="queryYearForm" name="queryYearForm" method = "post" action="calendar.do">
							<%@ include file="/jsp/common/commonParameter.jsp"%>
						 	<a class="easyui-linkbutton" data-options="" onclick="changeYear('pre')" id="preYear">上一年</a>
								<label style="text-align: center;padding: 20px" name="currentYear" id="currentYear"></label>
								<input id="<%=CalendarFormDTO.QUERY_YEAR %>" name="<%=CalendarFormDTO.QUERY_YEAR %>" type="hidden" value="${formDTO.queryYear}">
							<a class="easyui-linkbutton" data-options="" onclick="changeYear('next')" id="nextYear">下一年</a>
						</form>
					</td>
					<td>
						<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="editYearContract()" id="changYear">修改年度行事曆</a>
					</td>
				</tr>
				<tr >
					<td><div class="easyui-calendar" id="calendar1" name="calendar" style="width:280px;height:280px;" data-options="" ></div></td>
					<td><div class="easyui-calendar" id="calendar2" name="calendar" style="width:280px;height:280px;" data-options="" ></div></td>
					<td><div class="easyui-calendar" id="calendar3" name="calendar" style="width:280px;height:280px;" data-options="" ></div></td>
					<td><div class="easyui-calendar" id="calendar4" name="calendar" style="width:280px;height:280px;" data-options="" ></div></td>
				</tr>
				<tr>
					<td><div class="easyui-calendar" id="calendar5" name="calendar" style="width:280px;height:280px;" data-options="" ></div></td>
					<td><div class="easyui-calendar" id="calendar6" name="calendar" style="width:280px;height:280px;" data-options="" ></div></td>
					<td><div class="easyui-calendar" id="calendar7" name="calendar" style="width:280px;height:280px;" data-options="" ></div></td>
					<td><div class="easyui-calendar" id="calendar8" name="calendar" style="width:280px;height:280px;" data-options="" ></div></td>
				</tr>
				<tr>
					<td><div class="easyui-calendar" id="calendar9" name="calendar" style="width:280px;height:280px;" data-options="" ></div></td>
					<td><div class="easyui-calendar" id="calendar10" name="calendar" style="width:280px;height:280px;" data-options="" ></div></td>
					<td><div class="easyui-calendar" id="calendar11" name="calendar" style="width:280px;height:280px;" data-options="" ></div></td>
					<td><div class="easyui-calendar" id="calendar12" name="calendar" style="width:280px;height:280px;" data-options="" ></div></td>
				</tr>
				<div><span id="message" class="red"></span></div>
			</table>
		</div>		
		<div id="dialogView"></div>
	</div>
</body>
<script type="text/javascript">
	$(function(){
		loadCalendar();
	});
	function loadCalendar() {
		$("[name='calendar']").find("span").click(function(){
			return false;
		});
		//進入頁面時，頁面顯示當前日期和年份
		var date = new Date();
		var currentDate = date.getFullYear() + "/" + ((parseFloat(date.getMonth()) + 1) >= 10 ? (parseFloat(date.getMonth()) + 1) : "0" + (parseFloat(date.getMonth()) + 1) ) + "/" + (parseFloat(date.getDate()) >= 10 ? date.getDate() : "0" + date.getDate());
		$("#currentDate").textbox('setValue', currentDate);
		$("#currentYear").text("${formDTO.queryYear}");
		for (var i = 1; i <= 12; i++) {
			$('#calendar' + i + ' div.calendar-nav').remove();
			$('#calendar' + i).calendar({ 
				year : '${formDTO.queryYear}',
				month : i,
				styler : function(date) {
					var month = date.getMonth() + 1;
					var week = date.getDay();
					var flag = this.id.substring("calendar".length, this.id.length);
					//日期格式化
					var time = date.getFullYear() + "-" + ((parseFloat(date.getMonth()) + 1) >= 10 ? (parseFloat(date.getMonth()) + 1) : "0" + (parseFloat(date.getMonth()) + 1)) + "-" + (parseFloat(date.getDate()) >= 10 ? date.getDate() : "0" + date.getDate());
					if (month == flag) {
						var color = "color:blue";
						//判斷假日
						<c:if test="${not empty bimCalendarDayDTOs}">
							<c:forEach var = "calendarDayDTO" items = "${bimCalendarDayDTOs}" >
								var day = "${calendarDayDTO.day}";
								if (day == time) {
									if (${calendarDayDTO.isHoliday eq 'Y'}) {
							 			color = "color:red";
							 		} else if (${calendarDayDTO.isHoliday eq 'N'}) {
							 			color = "color:blue";
							 		}
							 	}
							</c:forEach>
						</c:if>
						return color;
					} else {
						//去掉非本月日
						return "visibility:hidden";
					}
				},
				onSelect : function(date) {
					$("#message").text("");
					var selectDate = this;
					var chooseDate = date.getFullYear() + "/" + ((parseFloat(date.getMonth()) + 1) >= 10 ? (parseFloat(date.getMonth()) + 1) : "0" + (parseFloat(date.getMonth()) + 1) ) + "/" + (parseFloat(date.getDate()) >= 10 ? date.getDate() : "0" + date.getDate());
					var viewDlg = $('#dialogView').dialog({    
					    title : '修改行事曆',    
					    width : 650,
					    height : 300,
					    top:10,
					    closed : false,
					    cache : false,
					    queryParams : {
					    	actionId : '<%=IAtomsConstants.ACTION_INIT_CALENDAR_DAY%>',
					    	day : chooseDate,
					    	queryYear : $('#currentYear').text()
					    },
					    href : "${contextPath}/calendar.do",
					    modal : true,
					    onLoad : function() {
					    //	textBoxMaxlengthSetting();
					    	textBoxSetting("dlg");
			            },
					    onClose:function(){
					    	var currentDate = $(selectDate).calendar("options");
					    	currentDate.current = null;
						    $('.calendar-selected').removeClass('calendar-selected');
					    },
						buttons : [{
							text : '儲存',
							iconCls : 'icon-ok',
							width: '90',
							handler : function(){
								var saveParam = $("#formDay").form("getData");
								saveParam.day = $('#<%=CalendarFormDTO.DAY%>').val();
								saveParam.queryYear = $('#currentYear').text();
					 			var url = "${contextPath}/calendar.do?actionId=<%=IAtomsConstants.ACTION_SAVE_CALENDAR_DAY%>";
						 		var controls = 
						 			['<%=BimCalendarDayDTO.ATTRIBUTE.DAY.getValue()%>',
						 				'<%=BimCalendarDayDTO.ATTRIBUTE.IS_HOLIDAY.getValue().toString()%>'];
						 		if (validateForm(controls) && $("#formDay").form('validate')) {
									// 調保存遮罩
									commonSaveLoading('dialogView');
									$.ajax({
										url : url,
										data : saveParam,
										type : 'post', 
										cache : false, 
										dataType : 'json', 
										success : function(data) {
											// 去除保存遮罩
											commonCancelSaveLoading('dialogView');
											if (data.success) {
									        	$('#dialogView').dialog('close');
									            $("#message").text(data.msg);
									            changeHoliday(data);
									            $('.calendar-selected').removeClass('calendar-selected');
											} else {
												$("#DaydialogMsg").text(data.msg);	
											}
											handleScrollTop('calendarDiv');
										}
									});
								}
							}
						},{
							text : '取消',
							iconCls : 'icon-cancel',
							width: '90',
							handler : function(){
								confirmCancel(function(){
										$('#dialogView').dialog('close');
										$('.calendar-selected').removeClass('calendar-selected');
								});
							}
						}]
					});
				}
			});
		}
		$('.calendar-selected').removeClass('calendar-selected');
	}
	//改變年份
	function changeYear(value) {
		$("#message").text("");
		var currentYear = $("#currentYear").text();
		var nextYear = "";
		nextYear = parseFloat(currentYear);
		if (value == "next") {
			$.ajax({
				url : "${contextPath}/calendar.do?actionId=initNextYear&queryYear=" + nextYear,
				type : 'post', 
				cache : false, 
				dataType : 'json', 
				success : function(data) {
					if (data.success) {						
						changeHoliday(data);
	                    $('.calendar-selected').removeClass('calendar-selected');
					} else {
						$("#dialogMsg").text(data.msg);	
					}
				},
				error:function(){
					$.messager.alert('提示', "查詢失敗,請聯繫管理員.", 'error');							
				}
			});
			nextYear = parseFloat(currentYear) + 1;
			$("#currentYear").text(nextYear);
		} else {
      			$.ajax({
    				url : "${contextPath}/calendar.do?actionId=initPreYear&queryYear=" + nextYear,
    				type : 'post', 
    				cache : false, 
    				dataType : 'json', 
    				success : function(data) {
    					if (data.success) {
    						changeHoliday(data);
    	                    $('.calendar-selected').removeClass('calendar-selected');
    					} else {
    						$("#dialogMsg").text(data.msg);	
    					}
    				},
    				error:function(){
    					$.messager.alert('提示', "查詢失敗,請聯繫管理員.", 'error');							
    				}
    			});	
      			nextYear = parseFloat(currentYear) - 1;
    			$("#currentYear").text(nextYear);
		} 
	}
	//“修改年度行事歷”頁面
	function editYearContract() {
		$("#message").text("");
		var year = $('#currentYear').text();
        var viewDlg = $('#dialogView').dialog({    
			title : '修改年度行事曆',    
			width : 680,
			height : 300,
			top:10,
			closed : false,
			cache : false,
			queryParams : {
				actionId : '<%=IAtomsConstants.ACTION_INIT_CALENDAR_YEAR%>',
		  		queryYear : year
		  	},
		  	href : "${contextPath}/calendar.do",
		  	modal : true,
		  	onClose:function(){
			    $('.calendar-selected').removeClass('calendar-selected');
			    $("div[data-list-required]").removeClass("div-tips").addClass("div-list").tooltip("destroy");
		    },
		  	buttons : [{
				text : '儲存',
				iconCls : 'icon-ok',
				width : '90',
				handler : function(){	
					var url = "${contextPath}/calendar.do?actionId=<%=IAtomsConstants.ACTION_SAVE_CALENDAR_YEAR%>";    
					var controls = 
						 			['<%=BimCalendarYearDTO.ATTRIBUTE.YEAR.getValue()%>',
						 				'checkWeekRests'];
					if (validateForm(controls) && $("#formYear").form('validate')) {
						// 調保存遮罩
						commonSaveLoading('dialogView');
						//驗證複選框
						 if (!checkedDivListValidate()) {
							 return false; 
						 }
						//周休日對象				
						var objweekRest = new Object();
						var weekRests = document.getElementsByName('weekRests');  
						var checkbox = $("[name=weekRests]:checked");
						var values = [];
						checkbox.each(function(i,item){
							values.push($(item).val());
						});
						//要保存的值
						var saveParam = {
							queryYear : year,
							weekRests : JSON.stringify(values)
						};
			            $.ajax({
							url : url,
							data : saveParam,
							type : 'post', 
							cache : false, 
							dataType : 'json', 
							success : function(data) {
								// 去除保存遮罩
								commonCancelSaveLoading('dialogView');
								if (data.success) {
			                		$('#dialogView').dialog('close');
				                    $("#message").text(data.msg);
				                    changeHoliday(data);
				                    $('.calendar-selected').removeClass('calendar-selected');
								} else {
									$("#dialogMsg").text(data.msg);	
								}
								handleScrollTop('calendarDiv');
							}
						});
					}
				}
			},{
				text : '取消',
				iconCls : 'icon-cancel',
				width : '90',
				handler : function(){
					confirmCancel(function(){
							$('#dialogView').dialog('close');
					});
				}
			}]
		});
	}

	//json改變日曆顏色
	function changeHoliday(data) {
		for (var i = 1; i <= 12; i++) {
			$('#calendar' + i + ' div.calendar-nav').remove();
			$('#calendar' + i).calendar({ 
				year : data.row,
				month : i,
				styler : function(date){
					var month = date.getMonth() + 1;
					var week = date.getDay();
					var flag = this.id.substring("calendar".length, this.id.length);
					//日期格式化
					var time = date.getFullYear() + "-" + ((parseFloat(date.getMonth()) + 1) >= 10 ? (parseFloat(date.getMonth()) + 1) : "0" + (parseFloat(date.getMonth()) + 1)) + "-" + (parseFloat(date.getDate()) >= 10 ? date.getDate() : "0" + date.getDate());
					if(month == flag){
						var color = "color:blue";
						//判斷假日
						if (data.rows != null) {
							$.each(data.rows, function(name, value) {
								var day = value.day;
								if (time == day) {
									if (value.isHoliday == "Y") {
										color = "color:red";
									} else if (value.isHoliday == "N") {
										color = "color:blue";
									}
							 	}
							});	
						}
						return color;
					} else {
						//去掉非本月日
						return "visibility:hidden";
					}
				}
			});
		}
	}
	
</script>
</html>