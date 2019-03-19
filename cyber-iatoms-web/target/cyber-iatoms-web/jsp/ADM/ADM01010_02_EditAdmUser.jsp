<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.PasswordSettingDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%@include file="/jsp/common/easyui-common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.AdmUserFormDTO"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	AdmUserFormDTO formDTO = null;
	PasswordSettingDTO passwordSettingDTO = null;
	AdmUserDTO admUserDTO = null;
	String ucNo = null;
	if (ctx != null) {
		formDTO = (AdmUserFormDTO) ctx.getResponseResult();
		if (formDTO != null) { 
			admUserDTO = formDTO.getAdmUserDTO();
			passwordSettingDTO = formDTO.getPasswordSettingDTO();
			// 获得UseCaseNo
			ucNo = formDTO.getUseCaseNo();
		} else {
			ucNo = IAtomsConstants.UC_NO_ADM_01010;
			formDTO = new AdmUserFormDTO();
			admUserDTO = new AdmUserDTO();
			passwordSettingDTO = new PasswordSettingDTO();
		}
	} else {
		ucNo = IAtomsConstants.UC_NO_ADM_01010;
		formDTO = new AdmUserFormDTO();
		admUserDTO = new AdmUserDTO();
		passwordSettingDTO = new PasswordSettingDTO();
	}
	//公司列表
	List<Parameter> companyList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, AdmUserFormDTO.PARAM_COMPANY_LIST);
	//角色列表
	List<Parameter> roleList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, AdmUserFormDTO.PARAM_ROLE_LIST);
	//用戶角色列表
	List<Parameter> userRoleList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, AdmUserFormDTO.PARAM_USER_ROLE_LIST);
	//狀態列表
	List<Parameter> statusList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.ACCOUNT_STATUS.getCode());
	//部門列表
	List<Parameter> deptList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, AdmUserFormDTO.PARAM_DEPT_LIST);
	// 仓库据点列表
	List<Parameter> warehouseList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, AdmUserFormDTO.PARAM_WAREHOUSE_LIST);
	// 用户控管资料列表
	List<Parameter> userWarehouseList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, AdmUserFormDTO.PARAM_USER_WAREHOUSE_LIST); 
%>
<html>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="admUserDTO" value="<%=admUserDTO%>" scope="page"></c:set>
<c:set var="companyList" value="<%=companyList%>" scope="page"></c:set>
<c:set var="roleList" value="<%=roleList%>" scope="page"></c:set>
<c:set var="statusList" value="<%=statusList%>" scope="page"></c:set>
<c:set var="deptList" value="<%=deptList%>" scope="page"></c:set>
<c:set var="userRoleList" value="<%=userRoleList%>" scope="page"></c:set>
<c:set var="warehouseList" value="<%=warehouseList%>" scope="page"></c:set>
<c:set var="userWarehouseList" value="<%=userWarehouseList%>" scope="page"></c:set> 
<c:set var="passwordSettingDTO" value="<%=passwordSettingDTO%>" scope="page"></c:set> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>AdmUser Edit</title>
</head>
<body>
<script type="text/javascript">
    	$(function(){
    		// 調用根據公司得到部門
    		//getDeptByCustomer();
    	//	disabledPassword();
    		$('#roleMultipleSelect').multiSelect();
    		$('#warehouseMultipleSelect').multiSelect();
    		$(".ms-container").css({"width":"380px"});
    		$(".ms-list").css({"height":"250px"});
    		// 角色权限全部选择
    		$('#roleSelectAll').click(function(){
    			$('#roleMultipleSelect').multiSelect('select_all');
    			return false;
    		});
    		// 角色权限全部取消
    		$('#roleDeselectAll').click(function(){
    			$('#roleMultipleSelect').multiSelect('deselect_all');
    			return false;
    		});
    		/*
    		* 部门根据公司联动
    		*/
    		$('#<%=AdmUserDTO.ATTRIBUTE.COMPANY_ID.getValue()%>').combobox({
    			onChange: function(newVlaue,oldValue){
    				javascript:dwr.engine.setAsync(false);
    				var pwdOptions = $('#<%=AdmUserDTO.ATTRIBUTE.PASSWORD.getValue()%>').textbox('options');
    				var repwdOptions = $('#<%=AdmUserDTO.ATTRIBUTE.REPASSWORD.getValue()%>').textbox('options');
    				$('#<%=AdmUserDTO.ATTRIBUTE.DEPT_CODE.getValue()%>').combobox('setValue','');
    				$('#<%=AdmUserDTO.ATTRIBUTE.ACCOUNT.getValue()%>').textbox('setValue','');
    				$('#<%=AdmUserDTO.ATTRIBUTE.CNAME.getValue()%>').textbox('setValue','');
    				$('#<%=AdmUserDTO.ATTRIBUTE.ENAME.getValue()%>').textbox('setValue','');
    				$('#<%=AdmUserDTO.ATTRIBUTE.EMAIL.getValue()%>').textbox('setValue','');
    				var companyId = newVlaue;
    				if(companyId){
    					// 得到部門集合
    					ajaxService.getDeptList(companyId, function(data){
       						// 设置默认值
       						var data = initSelect(data);
       						$('#<%=AdmUserDTO.ATTRIBUTE.DEPT_CODE.getValue()%>').combobox('loadData',data);
       					}); 
    					/* ajaxService.getUserByCompanyAndAccount(companyId, function(result){
       						if(companyId){
       							if(result.isAuthenTypeEqualsIAtoms){
       	   							pwdOptions.required=true;
       	   							pwdOptions.disabled=false;
       	   							repwdOptions.required=true;
       	   							repwdOptions.disabled=false;
       	   						 	$("#pwdLabel").html("密碼:<span class=\"red\">*</span>");
       	   							$("#rePwdLabel").html("確認密碼:<span class=\"red\">*</span>");
       	   							$("#requiredPwd").val("Y");
       	   							$("#disabledPwd").val("N");
       	   						} else {
       	   							pwdOptions.value='';
       	   							pwdOptions.disabled=true;
       	   							repwdOptions.value='';
       	   							repwdOptions.disabled=true;
       	   							$("#pwdLabel").html("密碼:");
       	   							$("#rePwdLabel").html("確認密碼:");  
       	   							$("#requiredPwd").val("N");
       	   							$("#disabledPwd").val("Y");
       	   						}
       						}
    					}); */
    					// 處理密碼欄位顯示
    					var isError = false;
						$.ajax({
							url: "${contextPath}/admUser.do?actionId=getUserByCompanyAndAccount",
							async: false,
							data: {companyId:companyId},
							type:'post', 
							cache:false, 
							dataType:'json', 
							success:function(json){
								if (json.success) {
									var result = json.userDTO;
									if(result.isAuthenTypeEqualsIAtoms){
	       	   							pwdOptions.required=true;
	       	   							pwdOptions.disabled=false;
	       	   							repwdOptions.required=true;
	       	   							repwdOptions.disabled=false;
	       	   						 	$("#pwdLabel").html("密碼:<span class=\"red\">*</span>");
	       	   							$("#rePwdLabel").html("確認密碼:<span class=\"red\">*</span>");
	       	   							$("#requiredPwd").val("Y");
	       	   							$("#disabledPwd").val("N");
	       	   						} else {
	       	   							pwdOptions.value='';
	       	   							pwdOptions.disabled=true;
	       	   							repwdOptions.value='';
	       	   							repwdOptions.disabled=true;
	       	   							$("#pwdLabel").html("密碼:");
	       	   							$("#rePwdLabel").html("確認密碼:");  
	       	   							$("#requiredPwd").val("N");
	       	   							$("#disabledPwd").val("Y");
	       	   						}
								} else {
									isError = true;
								}
							},
							error:function(){
								isError = true;
							}
						});
						if (isError) {
							$("#dialogMsg").html("查詢失敗！請聯繫管理員");
							return false;
						}
    				} else {
    	 				$('#<%=AdmUserDTO.ATTRIBUTE.DEPT_CODE.getValue()%>').combobox('loadData',initSelect());
 						pwdOptions.required=false;
       	   				pwdOptions.disabled=false;
       	   				repwdOptions.required=false;
       	   				repwdOptions.disabled=false;
 						$("#pwdLabel").html("密碼:");
 						$("#rePwdLabel").html("確認密碼:");  
   					}
   					javascript:dwr.engine.setAsync(true);
   					$('#<%=AdmUserDTO.ATTRIBUTE.PASSWORD.getValue()%>').textbox(pwdOptions);
   					$('#<%=AdmUserDTO.ATTRIBUTE.REPASSWORD.getValue()%>').textbox(repwdOptions);
   					textBoxsDefaultSetting($('#<%=AdmUserDTO.ATTRIBUTE.PASSWORD.getValue()%>'));
   					textBoxsDefaultSetting($('#<%=AdmUserDTO.ATTRIBUTE.REPASSWORD.getValue()%>'));
   					//passwordChanged();
    			}
    		});
    	});
    	
    	// 仓库据点资料全部选中
    	function warehouseSelectAllFun(){
			$('#warehouseMultipleSelect').multiSelect('select_all');
			return false;
		}
    	// 仓库据点资料全部取消
    	function warehouseDeselectAllFun(){
    		$('#warehouseMultipleSelect').multiSelect('deselect_all');
			return false;
		}
		/*
		* 密碼框內容改變時觸發 編輯時輸入密碼后必須輸入確認密碼
		*/
		function passwordChanged(){
    		$('#<%=AdmUserDTO.ATTRIBUTE.PASSWORD.getValue()%>').textbox('textbox').bind('keyup.changePassword', function(e){
				var repwdOptions = $('#<%=AdmUserDTO.ATTRIBUTE.REPASSWORD.getValue()%>').textbox('options');
   				var userId = '${admUserDTO.userId}';
   				if(userId != ''){
   					$('#<%=AdmUserDTO.ATTRIBUTE.REPASSWORD.getValue()%>').textbox('isValid');
   					var password = $(this).val();
   					if(password != ''){
  						repwdOptions.required=true;
   					} else {
  						repwdOptions.required=false;
   					}
   				}
   				$('#<%=AdmUserDTO.ATTRIBUTE.REPASSWORD.getValue()%>').textbox(repwdOptions);
   				textBoxsDefaultSetting($('#<%=AdmUserDTO.ATTRIBUTE.REPASSWORD.getValue()%>'));
			});
    	}
    	/*
		* 異常狀態改為正常
		*/
    	$('#<%=AdmUserDTO.ATTRIBUTE.STATUS.getValue()%>').combobox({
			onChange: function(newVlaue,oldValue){
				var pwdOptions = $('#<%=AdmUserDTO.ATTRIBUTE.PASSWORD.getValue()%>').textbox('options');
    			var repwdOptions = $('#<%=AdmUserDTO.ATTRIBUTE.REPASSWORD.getValue()%>').textbox('options');
				var oldStatus = '${admUserDTO.status }';
				// 處理密碼狂欄位顯示
				if($("#disabledPwd").val() == 'N'){
					var companyId = $('#<%=AdmUserDTO.ATTRIBUTE.DEPT_CODE.getValue()%>').combobox('getValue');
					if((newVlaue == 'NORMAL' && oldStatus != 'NORMAL') || (newVlaue == 'NEW' && oldStatus != 'NEW')){
						pwdOptions.required=true;
       	   				pwdOptions.disabled=false;
       	   				pwdOptions.missingMessage="帳號狀態已修改，請重新設定密碼";
       	   				repwdOptions.required=true;
       	   				repwdOptions.disabled=false;
					 	$("#pwdLabel").html("密碼:<span class=\"red\">*</span>");
						$("#rePwdLabel").html("確認密碼:<span class=\"red\">*</span>");
						$("#requiredPwd").val("Y");
					} else {
						pwdOptions.required=false;
       	   				pwdOptions.disabled=false;
       	   				repwdOptions.required=false;
       	   				repwdOptions.disabled=false;
						$("#pwdLabel").html("密碼:");
						$("#rePwdLabel").html("確認密碼:"); 
						$("#requiredPwd").val("N");
					}
				}
				$('#<%=AdmUserDTO.ATTRIBUTE.PASSWORD.getValue()%>').textbox(pwdOptions);
   				$('#<%=AdmUserDTO.ATTRIBUTE.REPASSWORD.getValue()%>').textbox(repwdOptions);
   				textBoxsDefaultSetting($('#<%=AdmUserDTO.ATTRIBUTE.PASSWORD.getValue()%>'));
   				textBoxsDefaultSetting($('#<%=AdmUserDTO.ATTRIBUTE.REPASSWORD.getValue()%>'));
   				passwordChanged();
			}
    	});
    	/*
    	* 编辑时判斷是否禁用密碼框
    	*/
    	function disabledPassword(){
    		javascript:dwr.engine.setAsync(false);
    		var pwdOptions = $('#<%=AdmUserDTO.ATTRIBUTE.PASSWORD.getValue()%>').textbox('options');
    		var repwdOptions = $('#<%=AdmUserDTO.ATTRIBUTE.REPASSWORD.getValue()%>').textbox('options');
    		var companyId = '${admUserDTO.companyId }';
			if(!isEmpty(companyId)){
				
				var isError = false;
				$.ajax({
					url: "${contextPath}/admUser.do?actionId=getUserByCompanyAndAccount",
					async: false,
					data: {companyId:companyId},
					type:'post', 
					cache:false, 
					dataType:'json', 
					success:function(json){
						if (json.success) {
							var result = json.userDTO;
							if(result.isAuthenTypeEqualsIAtoms){
		       	   				pwdOptions.disabled=false;
		       	   				repwdOptions.disabled=false;
								$("#disabledPwd").val("N");
								$("#requiredPwd").val("N");
							} else {
								pwdOptions.value='';
		       	   				pwdOptions.disabled=true;
		       	   				repwdOptions.value='';
		       	   				repwdOptions.disabled=true;
								$("#disabledPwd").val("Y");
								$("#requiredPwd").val("N");
							}
						} else {
							isError = true;
						}
					},
					error:function(){
						isError = true;
					}
				});
				if (isError) {
					$("#dialogMsg").empty();
					$("#dialogMsg").html("查詢失敗！請聯繫管理員");
					return false;
				}
				/* ajaxService.getUserByCompanyAndAccount(companyId, function(result){
					if(result.isAuthenTypeEqualsIAtoms){
       	   				pwdOptions.disabled=false;
       	   				repwdOptions.disabled=false;
						$("#disabledPwd").val("N");
						$("#requiredPwd").val("N");
					} else {
						pwdOptions.value='';
       	   				pwdOptions.disabled=true;
       	   				repwdOptions.value='';
       	   				repwdOptions.disabled=true;
						$("#disabledPwd").val("Y");
						$("#requiredPwd").val("N");
					}
				}); */
			}
			javascript:dwr.engine.setAsync(true);
			$('#<%=AdmUserDTO.ATTRIBUTE.PASSWORD.getValue()%>').textbox(pwdOptions);
   			$('#<%=AdmUserDTO.ATTRIBUTE.REPASSWORD.getValue()%>').textbox(repwdOptions);
   			textBoxsDefaultSetting($('#<%=AdmUserDTO.ATTRIBUTE.PASSWORD.getValue()%>'));
   			textBoxsDefaultSetting($('#<%=AdmUserDTO.ATTRIBUTE.REPASSWORD.getValue()%>'));
    	}
    	/**
    	 * 得到公司下的部門
    	 */
    	 function getDeptByCustomer(){
     		var companyId = '${admUserDTO.companyId}';
     		var deptCode = '${admUserDTO.deptCode}';
     		if(!isEmpty(companyId)){
     			ajaxService.getDeptList(companyId, function(data){
					var flag = false;
					for(var i = 0; i < data.length; i++){
						if(data[i].value == deptCode){
							flag = true;
							break;
						}
					}
					// 设置默认值
     				var data = initSelect(data);
     				// 将结果放到部門下拉框
     				$('#<%=AdmUserDTO.ATTRIBUTE.DEPT_CODE.getValue()%>').combobox('loadData',data);
					if(flag){
	     				$('#<%=AdmUserDTO.ATTRIBUTE.DEPT_CODE.getValue()%>').combobox('select',deptCode);
					}
     			});
     		}
     	}
    	/*
    	* 检查多选框是否被选中 處理倉庫控管下拉框顯示
    	*/
    	function checkMultipleSelect(){
    		if(!$("#<%=AdmUserDTO.ATTRIBUTE.DATA_ACL.getValue()%>").prop("checked")){
    			$('#warehouseMultipleSelect').multiSelect('deselect_all');
    			// 禁用下拉框
    			$("#ms-warehouseMultipleSelect").find("li").addClass("disabled");
    			// 全選按鈕
    			$('#warehouseSelectAll').attr("onclick","return false;");
				$('#warehouseSelectAll').attr("style","color:gray;cursor: default;");
				// 取消全選按鈕
				$('#warehouseDeselectAll').attr("onclick","return false;");
				$('#warehouseDeselectAll').attr("style","color:gray;cursor: default;");
    		} else {
    			// 啟用下拉框
    			$("#ms-warehouseMultipleSelect").find("li").removeClass("disabled");
    			// 全選按鈕
				$('#warehouseSelectAll').attr("onclick","warehouseSelectAllFun()");
				$('#warehouseSelectAll').attr("style","color:blue;");
				// 取消全選按鈕
				$('#warehouseDeselectAll').attr("onclick","warehouseDeselectAllFun()");
				$('#warehouseDeselectAll').attr("style","color:blue;");
    		}
    	}
    	/*
    	* 檢查用戶是否唯一
    	*/
		function checkUser() {
			$("#dialogMsg").text("");
			if(!validateForm(['account'])){
				return false;
			}
			var value = $.trim($(this).val());
			if (value != ""){
				var isError = false;
				var companyId = $('#<%=AdmUserDTO.ATTRIBUTE.COMPANY_ID.getValue()%>').combobox('getValue');
				$.ajax({
					url:'${contextPath}/admUser.do?actionId=<%=IAtomsConstants.ACTION_CHECK%>',
					data:{account : value},
					type:'post', 
					cache:false, 
					dataType:'json',
					async : false,
					success:function(json) {
						if (json.success) {
							if(companyId){
								$.ajax({
									url: "${contextPath}/admUser.do?actionId=getUserByCompanyAndAccount",
									async: false,
									data: {companyId:companyId,account : value},
									type:'post', 
									cache:false, 
									dataType:'json', 
									success:function(data){
										if (data.success) {
											var result = data.userDTO;
											if(result.isAuthenTypeEqualsIAtoms){
												$("#dialogMsg").text(json.msg);
											} else {
												// 域驗證失敗
												if(result.isLdapFailure){
													$("#dialogMsg").text("公司網域驗證失敗，請聯繫系統管理員");
												} else {
													if(result.cname == null && result.ename == null && result.email == null){
														$("#dialogMsg").text("此帳號非Cyber帳號");
													} else {
														$("#dialogMsg").text(json.msg);
														$('#<%=AdmUserDTO.ATTRIBUTE.CNAME.getValue()%>').textbox('setValue', result.cname);
														$('#<%=AdmUserDTO.ATTRIBUTE.ENAME.getValue()%>').textbox('setValue', result.ename);
														$('#<%=AdmUserDTO.ATTRIBUTE.EMAIL.getValue()%>').textbox('setValue', result.email);
													}
												}
											}
										} else {
											isError = true;
										}
									},
									error:function(){
										isError = true;
									}
								});
								if (isError) {
									$("#dialogMsg").html("查詢失敗！請聯繫管理員");
									return false;
								}
								<%-- ajaxService.getUserByCompanyAndAccount(companyId, value, function(result){
									if(result.isAuthenTypeEqualsIAtoms){
										$("#dialogMsg").text(json.msg);
									} else {
										// 域驗證失敗
										if(result.isLdapFailure){
											$("#dialogMsg").text("公司網域驗證失敗，請聯繫系統管理員");
										} else {
											if(result.cname == null && result.ename == null && result.email == null){
												$("#dialogMsg").text("此帳號非Cyber帳號");
											} else {
												$("#dialogMsg").text(json.msg);
												$('#<%=AdmUserDTO.ATTRIBUTE.CNAME.getValue()%>').textbox('setValue', result.cname);
												$('#<%=AdmUserDTO.ATTRIBUTE.ENAME.getValue()%>').textbox('setValue', result.ename);
												$('#<%=AdmUserDTO.ATTRIBUTE.EMAIL.getValue()%>').textbox('setValue', result.email);
											}
										}
									}
								}); --%>
							} else {
								$("#dialogMsg").text("請輸入公司");
							}
						} else {
							$("#dialogMsg").text(json.msg);
							$('#<%=AdmUserDTO.ATTRIBUTE.ACCOUNT.getValue()%>').textbox('setValue', '');
						}
					},
					error:function(){
						$.messager.progress('close');
						$("#dialogMsg").text("檢查失敗,請聯繫管理員.");
					}
				});
			} 
		}
	</script>
<div style="width: auto; height: auto; padding: 10px 20px; overflow-y: hidden" data-options="region:'center'">
	<div class="dialogtitle">使用者帳號管理</div>
	<div><span id="dialogMsg" class="red"></span></div>
	<form id="saveForm" method="post" class="formStyle" style="padding: 1px;" novalidate>
		<table cellpadding="5" style="width: 100%;">
			<tr>
				<td width="14%">
                       <label>公司:<span class="red">*</span></label>
                   </td>
                   <td >
					<c:choose>
						<c:when test="${formDTO.actionId eq 'initAdd' }">
							<cafe:droplisttag css="easyui-combobox"
								id="<%=AdmUserDTO.ATTRIBUTE.COMPANY_ID.getValue()%>"
								name="<%=AdmUserDTO.ATTRIBUTE.COMPANY_ID.getValue()%>" 
								result="${companyList }"
								blankName="請選擇" 
								hasBlankValue="true"
								style="width: 80%"
								javascript="editable='false' validType=\"ignore['請選擇']\" invalidMessage=\"請輸入公司\""
								>
							</cafe:droplisttag>
						</c:when>
						<c:otherwise>
							<cafe:droplisttag css="easyui-combobox"
								id="<%=AdmUserDTO.ATTRIBUTE.COMPANY_ID.getValue()%>"
								name="<%=AdmUserDTO.ATTRIBUTE.COMPANY_ID.getValue()%>" 
								blankName="請選擇" 
								result="${companyList }"
								hasBlankValue="true"
								disabled="true"
								selectedValue="${admUserDTO.companyId }"
								style="width: 80%"
								>
							</cafe:droplisttag>
							<%-- <input type="hidden" id="<%=AdmUserDTO.ATTRIBUTE.COMPANY_ID.getValue() %>" name="<%=AdmUserDTO.ATTRIBUTE.COMPANY_ID.getValue() %>" value="${admUserDTO.companyId}"> --%>
						</c:otherwise>
					</c:choose>
                   </td>
                   <td width="13%">
                       <label>部門:</label>
                   </td>
                   <td >
					<cafe:droplisttag css="easyui-combobox"
							id="<%=AdmUserDTO.ATTRIBUTE.DEPT_CODE.getValue()%>"
							name="<%=AdmUserDTO.ATTRIBUTE.DEPT_CODE.getValue()%>" 
							blankName="請選擇" 
							hasBlankValue="true"
							result="${formDTO.actionId eq 'initEdit'? deptList: '' }"
							 selectedValue="${admUserDTO.deptCode }" 
							style="width: 80%"
							javascript="editable='false' valueField='value' textField='name'"
							>
						</cafe:droplisttag> 
                   </td>
			</tr>
			<tr>
				<td>
                       <label>帳號:<span class="red">*</span></label>
                   </td>
                   <td>
	                   <c:choose>
						<c:when test="${formDTO.actionId eq 'initEdit' }">
							<input id="<%=AdmUserDTO.ATTRIBUTE.ACCOUNT.getValue()%>" name="<%=AdmUserDTO.ATTRIBUTE.ACCOUNT.getValue()%>"
	                 			value="<c:out value='${admUserDTO.account }'/>" disabled
	                  			class="easyui-textbox" style="width: 85%">
	                  			<input type="hidden" id="editAccount" name="editAccount" value="<c:out value='${admUserDTO.account}'/>">
						</c:when>
						<c:otherwise>
							<input id="<%=AdmUserDTO.ATTRIBUTE.ACCOUNT.getValue()%>" name="<%=AdmUserDTO.ATTRIBUTE.ACCOUNT.getValue()%>"
                 				value="<c:out value='${admUserDTO.account }'/>" 
                  				class="easyui-textbox" style="width: 85%"
                  				maxlength="20"
                  				data-options="buttonIcon:'icon-search',onClickButton:checkUser,
                  				validType:['length[4,20]','numOrEngOrChar','maxLength[20]']"
                  				required="true" missingMessage="請輸入帳號" invalidMessage="帳號限4~20碼的英數符號，請重新輸入">
						</c:otherwise>
					</c:choose>
	                  		
                   </td>
                  <input type="hidden" id="<%=AdmUserDTO.ATTRIBUTE.USER_ID.getValue() %>" name="<%=AdmUserDTO.ATTRIBUTE.USER_ID.getValue() %>" value="<c:out value='${admUserDTO.userId}'/>">
                  <input type="hidden" id="<%=AdmUserDTO.ATTRIBUTE.DISABLED_PWD.getValue() %>" name="<%=AdmUserDTO.ATTRIBUTE.DISABLED_PWD.getValue() %>" >
                  <input type="hidden" id="<%=AdmUserDTO.ATTRIBUTE.REQUIRED_PWD.getValue() %>" name="<%=AdmUserDTO.ATTRIBUTE.REQUIRED_PWD.getValue() %>" >
                   <td>
                        <label>中文姓名:<span class="red">*</span></label>
                   </td>
                   <td>
                   	<input id="<%=AdmUserDTO.ATTRIBUTE.CNAME.getValue()%>" 
                   		   name="<%=AdmUserDTO.ATTRIBUTE.CNAME.getValue()%>"
	                 	   value="<c:out value='${admUserDTO.cname }'/>" 
	                 	   class="easyui-textbox" maxlength="50"
	                 	   data-options="validType:['maxLength[50]']"
	                 	   style="width:  80%"
	                 	   required="true" missingMessage="請輸入中文姓名"> 
                   </td>
			</tr>
			<tr>
				 <td>
                       <label>英文姓名:</label>
                    </td>
                    <td>
                    	<input id="<%=AdmUserDTO.ATTRIBUTE.ENAME.getValue()%>" 
                   			name="<%=AdmUserDTO.ATTRIBUTE.ENAME.getValue()%>"
               				value="<c:out value='${admUserDTO.ename }'/>" 
               				class="easyui-textbox" maxlength="50"
               				data-options="validType:['maxLength[50]']"
               				style="width: 80%">  
                    </td>
                    <td>
                       <label>電話:</label>
                    </td>
                    <td>
                    	<input id="<%=AdmUserDTO.ATTRIBUTE.TEL.getValue()%>" 
                   			name="<%=AdmUserDTO.ATTRIBUTE.TEL.getValue()%>" 
	                 		value="<c:out value='${admUserDTO.tel }'/>" 
	                 		class="easyui-textbox" 
	                 		style="width: 80%" maxlength="20"
	                 		data-options="validType:['maxLength[20]']"
	                 	>
                    </td>
			</tr>
			<tr>
                    <td>
                        <label>行動電話:</label>
                    </td>
                    <td>
                        <input id="<%=AdmUserDTO.ATTRIBUTE.MOBILE.getValue()%>" 
                        	name="<%=AdmUserDTO.ATTRIBUTE.MOBILE.getValue()%>"
		                 	value="<c:out value='${admUserDTO.mobile }'/>" 
		                 	class="easyui-textbox" 
		                 	style="width:  80%" maxlength="10"
		                 	data-options="validType:['twMobile','maxLength[10]']"> 
                    </td>
                    <td>
                        <label>EMail:</label>
                    </td>
                    <td>
                        <input id="<%=AdmUserDTO.ATTRIBUTE.EMAIL.getValue()%>" 
	                        name="<%=AdmUserDTO.ATTRIBUTE.EMAIL.getValue()%>"
		                 	value="${admUserDTO.email}" 
		                 	class="easyui-textbox" 
		                 	style="width:  80%" maxlength="50"
		                 	data-options="validType:['email','maxLength[50]']"
		                 	invalidMessage="Email格式有誤，請重新輸入"> 
                    </td>
                </tr>
                <tr>
                    <td>
                        <label>主管EMail:</label>
                    </td>
                    <td>
                        <input value="${admUserDTO.managerEmail}" name="<%=AdmUserDTO.ATTRIBUTE.MANAGER_EMAIL.getValue()%>" id="<%=AdmUserDTO.ATTRIBUTE.MANAGER_EMAIL.getValue()%>" class="easyui-textbox" maxlength="50" data-options="validType:['email','maxLength[50]']" style="width:  80%" invalidMessage="主管Email格式有誤，請重新輸入">
                    </td>
                    <td>
                        <label>備註:</label>
                    </td>
                    <td>
                        <%-- <input id="<%=AdmUserDTO.ATTRIBUTE.USER_DESC.getValue()%>" 
                        	name="<%=AdmUserDTO.ATTRIBUTE.USER_DESC.getValue()%>"
		                 	value="<c:out value='${admUserDTO.userDesc }'/>" 
		                 	class="easyui-textbox" maxlength="200"
		                 	data-options="multiline:true,validType:['maxLength[200]']" 
		                 	style="height: 30px;width: 90%"> --%>
		                 	<textarea id="<%=AdmUserDTO.ATTRIBUTE.USER_DESC.getValue()%>" 
                        	name="<%=AdmUserDTO.ATTRIBUTE.USER_DESC.getValue()%>"
		                 	class="easyui-textbox" maxlength="200"
		                 	data-options="multiline:true,validType:['maxLength[200]']" 
		                 	style="height: 40px;width:  80%"><c:out value='${admUserDTO.userDesc }'/></textarea>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label>登入錯誤次數:</label>
                    </td>
                    <td>
                    <c:choose>
						<c:when test="${formDTO.actionId eq 'initEdit'}">
							<input id="<%=AdmUserDTO.ATTRIBUTE.RETRY.getValue()%>" 
                        	name="<%=AdmUserDTO.ATTRIBUTE.RETRY.getValue()%>"
	                 		value="${admUserDTO.retry }"
	                 		style="width: 80%"
	                 		class="easyui-textbox" disabled>
						</c:when>
						<c:otherwise>
							<input id="<%=AdmUserDTO.ATTRIBUTE.RETRY.getValue()%>" 
                        	name="<%=AdmUserDTO.ATTRIBUTE.RETRY.getValue()%>"
	                 		value="0"
	                 		style="width: 80%"
	                 		class="easyui-textbox" disabled>
	                 	</c:otherwise>
					</c:choose>
                    </td>
                    <td>
                        <label>狀態:</label>
                    </td>
                    <td>
                    <c:choose>
						<c:when test="${formDTO.actionId eq 'initEdit' }">
							 <cafe:droplisttag css="easyui-combobox"
								id="<%=AdmUserDTO.ATTRIBUTE.STATUS.getValue()%>"
								name="<%=AdmUserDTO.ATTRIBUTE.STATUS.getValue()%>" 
								result="${statusList }"
								blankName="請選擇" 
								hasBlankValue="true"
								selectedValue="${admUserDTO.status }"
								style="width: 80%"
								javascript="editable='false' panelheight='auto' validType=\"ignore['請選擇']\" invalidMessage=\"請輸入狀態\" "
								>
							</cafe:droplisttag>
						</c:when>
						<c:otherwise>
							 <cafe:droplisttag css="easyui-combobox"
								id="<%=AdmUserDTO.ATTRIBUTE.STATUS.getValue()%>"
								name="<%=AdmUserDTO.ATTRIBUTE.STATUS.getValue()%>" 
								result="${statusList }"
								blankName="請選擇" 
								hasBlankValue="true"
								disabled="true"
								selectedValue="<%=IAtomsConstants.ACCOUNT_STATUS_NEW %>"
								style="width: 80%"
								javascript="editable='false'"
								>
							</cafe:droplisttag>
						</c:otherwise>
					</c:choose>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label id="pwdLabel">密碼:</label>
                    </td>
                    <td>
                        <input class="easyui-textbox" type="password" id="<%=AdmUserDTO.ATTRIBUTE.PASSWORD.getValue()%>" 
                       		name="<%=AdmUserDTO.ATTRIBUTE.PASSWORD.getValue()%>" 
                       		missingMessage="請輸入密碼" maxlength="20" 
               				data-options="validType:['maxLength[20]','pwdLength[${passwordSettingDTO.pwdLenBg },${passwordSettingDTO.pwdLenNd},0]','pwdCharacter[\'密碼限英數字或英文符號或英數符號，請重新輸入\']']">
               				</input>
                    </td>
                    <td>
                        <label id="rePwdLabel">確認密碼:</label>
                    </td>
                    <td>
						<input type="password" id="<%=AdmUserDTO.ATTRIBUTE.REPASSWORD.getValue()%>" 
                  			name="<%=AdmUserDTO.ATTRIBUTE.REPASSWORD.getValue()%>"
         					class="easyui-textbox"  
         					maxlength="20" 
         					data-options="validType:['maxLength[20]','equalTo[\'#<%=AdmUserDTO.ATTRIBUTE.PASSWORD.getValue()%>\']']"
         					invalidMessage="確認密碼與密碼不一致，請重新輸入" missingMessage="請輸入確認密碼">
                    </td>
                </tr>
               <tr>
                	<td colspan="2">
                	</td>
                    <td colspan="2">
                    <c:choose>
						<c:when test="${admUserDTO.dataAcl eq 'Y' }">
							 <input type="checkbox" checked onclick="checkMultipleSelect()" id="<%=AdmUserDTO.ATTRIBUTE.DATA_ACL.getValue()%>" name="<%=AdmUserDTO.ATTRIBUTE.DATA_ACL.getValue()%>" >
						</c:when>
						<c:otherwise>
							 <input type="checkbox" <c:if test="${formDTO.actionId eq 'initAdd' }">checked</c:if> onclick="checkMultipleSelect()" id="<%=AdmUserDTO.ATTRIBUTE.DATA_ACL.getValue()%>" name="<%=AdmUserDTO.ATTRIBUTE.DATA_ACL.getValue()%>" >
						</c:otherwise>
					</c:choose>
                    	<label>控管資料權限 (勾選-控管;未勾選-可見所有倉庫)</label>
                    </td>
                    
                </tr>
		</table>
		<table style="width: 100%;">
		 
                <tr>
                   <td >
                       <label>角色權限:</label>
                   </td>
                   <td>
                       <a href='javascript:void(0)' id='roleSelectAll' >全選</a>
                       <a href='javascript:void(0)' id='roleDeselectAll'>取消全選</a>
                       <select multiple="multiple" id="roleMultipleSelect" name="roleMultipleSelect[]">
	             		<c:forEach items="${roleList }" var="item"  varStatus="rowid" >
	             			<c:set var="isSelect" value="${false }"/>
	             			<c:forEach items="${userRoleList }" var="itemUserRole">
	             				<c:if test = "${item.value eq itemUserRole.value }">
	             					<c:set var="isSelect" value="${true }"/>
	             				</c:if>
	             			</c:forEach>
							<option value="${item.value }" <c:if test="${isSelect }">selected</c:if>>${item.name }</option>
						</c:forEach>
	             	</select>
                   </td>
                    <td>
                   </td>
                   <td>
                   	<a href='javascript:void(0)' id='warehouseSelectAll' onclick='warehouseSelectAllFun()'>全選</a>
                       <a href='javascript:void(0)' id='warehouseDeselectAll' onclick='warehouseDeselectAllFun()'>取消全選</a>
                       <select multiple="multiple" id="warehouseMultipleSelect" name="warehouseMultipleSelect[]">
	             		<c:forEach items="${warehouseList }" var="warehouse"  varStatus="rowid" >
	             			<c:set var="isSelect" value="${false }"/>
	             			<c:forEach items="${userWarehouseList }" var="userWarehouse">
	             				<c:if test = "${warehouse.value eq userWarehouse.value }">
	             					<c:set var="isSelect" value="${true }"/>
	             				</c:if>
	             			</c:forEach>
							<option value="${warehouse.value }" <c:if test="${isSelect }">selected</c:if>>${warehouse.name }</option>
						</c:forEach>
	             	</select>
                   </td>
               </tr>
		
		</table>
    </form>
</div>
</body>
</html>