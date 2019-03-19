<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/jsp/common/common.jsp"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	CaseManagerFormDTO formDTO = null;	
	String ucNo = null;
	if (ctx != null) {
		// 得到FormDTO
		formDTO = (CaseManagerFormDTO) ctx.getRequestParameter();
		if (formDTO != null) {
			// 获得UseCaseNo
			ucNo = formDTO.getUseCaseNo();
			
		} else {
			ucNo = IAtomsConstants.UC_NO_SRM_05020;
			formDTO = new CaseManagerFormDTO();
		}
	} else {
		ucNo = IAtomsConstants.UC_NO_SRM_05020;
		formDTO = new CaseManagerFormDTO();
	}
	// 獲取用戶所有欄位模板
	List<Parameter> columnTemplateList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_COLUMN_TEMPLATE_LIST);
	// 獲取所有欄位集合
	List<Parameter> allColumnsList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_ALL_COLUMNS_LIST);
	
	// 獲得用戶當前模板
	Parameter currentColumnTemplate = (Parameter)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_CURRENT_COLUMN_TEMPLATE);
%>
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$CASE_CATEGORY" var="caseCategoryAttr" />
<html>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="columnTemplateList" value="<%=columnTemplateList%>" scope="page"></c:set>
<c:set var="allColumnsList" value="<%=allColumnsList%>" scope="page"></c:set>
<c:set var="currentColumnTemplate" value="<%=currentColumnTemplate%>" scope="page"></c:set>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>欄位</title>
<%@include file="/jsp/common/easyui-common.jsp"%>
</head>
<body>
<div style="margin-left:2%;margin-top:4%;" id="columnBlockDiv">
	<div><span id="columnBlockMsg" class="red"></span></div>
	<div style="float:left;padding: 10px 20px;">
		<div style="text-align: center;" class="ms2side__header">可選擇模板類型</div>
		<select size= "10" style="width: 180px" id="selectTemplate" name="selectTemplate">
			<option value="" selected>新模板</option>
			<c:forEach items="${columnTemplateList }" var="columnTemplate"  varStatus="rowid" >
				<c:if test="${empty currentColumnTemplate}">
					<option value="${columnTemplate.value }">${columnTemplate.name }</option>
				</c:if>
				<c:if test="${not empty currentColumnTemplate}">
					<c:if test="${currentColumnTemplate.value eq columnTemplate.value}"><c:set var="selectTemplateName" value="${columnTemplate.name }"/></c:if>
					<option value="${columnTemplate.value }" <c:if test="${currentColumnTemplate.value eq columnTemplate.value}">selected</c:if>>${columnTemplate.name }</option>
				</c:if>
			</c:forEach>
		</select>
	</div>
	<div style="float:left;padding: 10px 20px;">
		<select name="multiColumn[]" id='multiColumn' multiple='multiple' size="10">
			<c:forEach items="${allColumnsList }" var="everyColumn"  varStatus="rowid" >
				<option value="${everyColumn.value }">${everyColumn.name }</option>
			</c:forEach>
		</select>
	</div>
	<div style="clear:both;height:10px;">
	</div>
	<form >
	<div style="float:left;padding: 10px 20px;margin-left:5%;">
                        <label id="templateNameLabel">模板名稱:<c:if test="${not empty currentColumnTemplate and currentColumnTemplate.value ne '100000000-0001'}"><span class="red">*</span></c:if> </label>
                   	<input id="templateName" 
                   		   name="templateName"
	                 	   class="easyui-textbox" maxlength="50"
	                 	   data-options="validType:['maxLength[50]']"
	                 	   style="width: 200px" required="true" missingMessage="模板名稱" <c:if test="${not empty currentColumnTemplate and currentColumnTemplate.value eq '100000000-0001'}">disabled</c:if>
	                 	   value="${selectTemplateName }"
	                 	   > 
	                 	   <input type="hidden" name="isClickOk" id="isClickOk">
	                 	   <input type="hidden" name="beforeSelectTemplate" id="beforeSelectTemplate" >
	</div>
	</form>
	<div style="padding: 10px 20px;float:right;margin-right:5%;">
			<a href="javascript:void(0)" id="columnBtnDelete" class="easyui-linkbutton" iconcls="icon-remove" style="width:88px;" <c:if test="${not empty currentColumnTemplate and currentColumnTemplate.value eq '100000000-0001'}">disabled</c:if>>刪除</a>
			<a href="javascript:void(0)" id="columnBtnUpdate" class="easyui-linkbutton" iconcls="icon-redo" style="width:88px;" <c:if test="${not empty currentColumnTemplate and currentColumnTemplate.value eq '100000000-0001'}">disabled</c:if>>更新</a>
	</div>
</div>
    <script type="text/javascript">
        $(function(){
        	// 用戶模板臨時集合
        	var templateInfoArray;
        	$('#multiColumn').multiselect2side({
    			selectedPosition: 'right',  
    			moveOptions: true,
    			labelsx: '可選擇欄位',
    			labeldx: '已選擇欄位',
    			labelTop: '置頂',
    			labelBottom: '置底',
    			labelUp: '上調',
    			labelDown: '下調',
    			haveLabelSort:false,
    			isAlignCenter:true,
    			delay:1
    		});
        	// 初始化展示預設模板
        	showCurrentTemplate();
        	// 處理初始化按鈕狀態
            dealButtonStatus();
        	
            /*
             *更新按鈕點擊事件
             */
             $("#columnBtnUpdate").linkbutton({
     			onClick : function (){
     				// 保存模板信息
     				saveColumnTemplate(false);
     			}
     		});
             
             /*
              *刪除按鈕點擊事件
              */
             $("#columnBtnDelete").linkbutton({
     			onClick : function (){
     				// 刪除模板信息
     				deleteColumnTemplate();
     			}
     		});
        })
        
        
        
        /*
         *初始化展示預設模板
         */
        function showCurrentTemplate(){
   			var allSel = $('#multiColumn').next().find("select");
   			var	leftSel = allSel.eq(0);
   			var	rightSel = allSel.eq(1);
        	var currentTemplate = '${currentColumnTemplate.name}';
        	if(currentTemplate != ''){
        		var columnArray = currentTemplate.split(',');
    			if(columnArray && columnArray != null){
    				// 選中列
    				for(var i = 0; i < columnArray.length; i++){
    	    			leftSel.val(columnArray[i])
    	    			// 將選中列移動到右側
    	    			leftSel.find("option:selected").clone().appendTo("#" + rightSel.attr("id"));
    	    			leftSel.find("option:selected").remove();
    				}
    				// 按鈕顯示控制
    				leftSel.trigger('change');
    			}
        	}
        	<c:if test="${not empty currentColumnTemplate}">
        		// 初始化增加當前模板信息
 	        	templateInfoArray = new Array();
	        	var templateInfo = {
	        		templateId : '${currentColumnTemplate.value}',
	        		fieldContent : '${currentColumnTemplate.name}'
	        	};
	        	templateInfoArray.push(templateInfo);
	        	// 上次選中模板名稱默認為初始化進入當前模板
	        	$("#beforeSelectTemplate").val('${currentColumnTemplate.value}');
			</c:if>
        }
        
        
        
        /*
         *初始化展示按鈕狀態
         */
        function dealButtonStatus(){
   			var allSel = $('#multiColumn').next().find("select");
   			var	leftSel = allSel.eq(0);
   			var	rightSel = allSel.eq(1);
   			// 控件綁定onchange事件
   			leftSel.change(function(){
   				disabledButtonFun(leftSel);
   			});
   			// 控件綁定onchange事件
   			rightSel.change(function(){
   				disabledButtonFun(rightSel);
   			});
   			// 禁用按鈕事件
   			disabledButtonFun(leftSel);
   			disabledButtonFun(rightSel);
        }
        
        
        
        /*
         *禁用按鈕事件
         */
        function disabledButtonFun(obj){
        	// 得到模板選擇值
        	var templateId = $("#selectTemplate").val();
        	// 若為預設模板禁用按鈕
        	if(templateId == '100000000-0001'){
        		var	div = obj.parent().parent();
            	div.find(".RemoveOne, .RemoveAll, .AddOne, .AddAll, .MoveUp, .MoveDown, .MoveTop, .MoveBottom, .SelSort").addClass('ms2side__hide');
            	obj.addClass('UnbindDblclick');
        	} else {
        		// 不為預設模板釋放按鈕禁用
	        	if(obj.hasClass('UnbindDblclick')){
	        		obj.removeClass('UnbindDblclick');
	        	}
        	}
        }
        
        
        
        /*
         *用戶模板選擇onchange
         */
        $("#selectTemplate").change(function(){
        	// 清空消息
        	$("#templateName").textbox('setValue', '');
        	$("#columnBlockMsg").text('');
        	// 若模板集合不為空
        	if(templateInfoArray.length != 0){
	        	// 得到上筆選中模板名稱
	        	var beforeSelectTemplate = $("#beforeSelectTemplate").val();
	        	// 得到上筆選中模板列
        		var beforeMultiSelectValue = getMultiSelectValue();
        		var flag = false;
    			for(var i =0; i < templateInfoArray.length; i++){
    				// 若當前模板集合存在上筆模板信息，則更新上筆模板信息
    				if(templateInfoArray[i].templateId == beforeSelectTemplate){
    					flag = true;
    					templateInfoArray[i].fieldContent = beforeMultiSelectValue;
    					break;
    				}
    			}
    			// 若當前模板集合不存在上筆模板信息，則將上筆模板信息增加至集合
    			if(!flag){
    				var templateInfo = {
   						templateId : beforeSelectTemplate,
   						fieldContent : beforeMultiSelectValue
   					};
   					templateInfoArray.push(templateInfo);
    			}
        	} 
        
			//清空并初始化多選下拉模板列
			clearMultiSelect();
   			
   			// 得到當前模板名稱值
   			var templateId = $("#selectTemplate").val();
   			if(templateId == ''){
   				// 若為新模板則模板名稱必填，刪除按鈕禁用
    			var templateNameOptions = $("#templateName").textbox('options');
   				templateNameOptions.missingMessage = '請輸入模板名稱';
   				templateNameOptions.required = true;
   				templateNameOptions.disabled = false;
   				$("#templateName").textbox(templateNameOptions);
   				// 限制欄位長度
   				textBoxDefaultSetting($("#templateName"));
   				
   				// 標記
   				$("#templateNameLabel").html("模板名稱:<span class=\"red\">*</span>");
   				
   				// 禁用刪除
   				$("#columnBtnDelete").linkbutton('disable');
				// 啟用更新
				$("#columnBtnUpdate").linkbutton('enable');
				
				// 根據模板編號更新模板信息
				updateColumnByTemplateId(templateId);
   			} else {
   				// 得到當前選中模板模板名稱
   				var templateName = $("#selectTemplate").find("option:selected").text();
   				// 設置模板名稱
   				$("#templateName").textbox('setValue', templateName);
   				// 若為預設模板則禁用模板名稱 禁用刪除、更新
   				if(templateId == '100000000-0001'){
   					var templateNameOptions = $("#templateName").textbox('options');
   	   				templateNameOptions.missingMessage = '';
   	   				templateNameOptions.required = false;
   	   				templateNameOptions.disabled = true;
   	   				$("#templateName").textbox(templateNameOptions);
   	   				// 限制欄位長度
   	   				textBoxDefaultSetting($("#templateName"));
   	   				// 標記
   	   				$("#templateNameLabel").html("模板名稱:");
   	   				
   	   				// 禁用更新
 	   				$("#columnBtnUpdate").linkbutton('disable');
 	   				// 禁用刪除
   					$("#columnBtnDelete").linkbutton('disable');
 	   				
   				// 若為預設模板則啟用模板名稱 啟用刪除、更新
   				} else {
   	    			var templateNameOptions = $("#templateName").textbox('options');
   	   				templateNameOptions.missingMessage = '請輸入模板名稱';
   	   				templateNameOptions.required = true;
   	   				templateNameOptions.disabled = false;
   	   				$("#templateName").textbox(templateNameOptions);
   	   				// 限制欄位長度
   	   				textBoxDefaultSetting($("#templateName"));
   	   				
   	   				// 標記
   	   				$("#templateNameLabel").html("模板名稱:<span class=\"red\">*</span>");
   	   				
   	   				// 啟用更新
 	   				$("#columnBtnUpdate").linkbutton('enable');
 	   				// 啟用刪除
   					$("#columnBtnDelete").linkbutton('enable');
   				}
   			}
			// 根據模板編號更新模板信息
   	        updateColumnByTemplateId(templateId);
			// 更新上筆模板欄位
   			$("#beforeSelectTemplate").val(templateId);
   		});
        
        
        /*
        *清空并初始化多選下拉模板列
        */
        function clearMultiSelect(){
        	var allSel = $('#multiColumn').next().find("select");
   			var	leftSel = allSel.eq(0);
   			var	rightSel = allSel.eq(1);
   			// 清空列
   			leftSel.empty();
   			rightSel.empty();
   			<c:forEach items="${allColumnsList }" var="everyColumn"  varStatus="rowid" >
   				leftSel.append("<option value='" +"${everyColumn.value }"+ "'>"+"${everyColumn.name }"+"</option>");
   			</c:forEach>
   			// 按鈕顯示控制
			leftSel.trigger('change');
        }
        
        
        /*
         *將選中列移動到右側
         */
        function selectColumnToRight(columnArray){
        	var allSel = $('#multiColumn').next().find("select");
			var	leftSel = allSel.eq(0);
			var	rightSel = allSel.eq(1);
			if(columnArray && columnArray != null){
				// 選中列
				for(var i = 0; i < columnArray.length; i++){
	    			leftSel.val(columnArray[i])
	    			// 將選中列移動到右側
	    			leftSel.find("option:selected").clone().appendTo("#" + rightSel.attr("id"));
	    			leftSel.find("option:selected").remove();
				}
				// 按鈕顯示控制
				leftSel.trigger('change');
			}
        }
        
        
        
        /*
        *根據模板編號更新模板信息
        */
		function updateColumnByTemplateId(templateId){
			// 要顯示模板信息
			var showColumnTemplate;
			// 判斷模板集合是否存在當前模板編號
			var isLocal = false;
			for(var i =0; i < templateInfoArray.length; i++){
				// 存在當前模板編號的模板則標記
				if(templateInfoArray[i].templateId == templateId){
					showColumnTemplate = templateInfoArray[i].fieldContent;
					isLocal = true;
					break;
				}
			}
			// 存在當前模板編號的模板則取出展示在右側
			if(isLocal){
				// 將選中列移動到右側
				selectColumnToRight(showColumnTemplate);
			// 不存在當前模板編號的模板則查詢展示在右側
			} else {
				javascript:dwr.engine.setAsync(false);
  				ajaxService.getUserColumnTemplate(templateId,function(returnValue){
  					if(returnValue && returnValue.fieldContent){
  						var fieldContent = returnValue.fieldContent;
  						var columnArray = fieldContent.split(',');
  						// 將選中列移動到右側
  						selectColumnToRight(columnArray);
  					}
  				});
  				javascript:dwr.engine.setAsync(true);
			}
        }
        
        
        
		/*
        *根據模板編號刪除模板信息
        */
		function deleteColumnByTemplateId(templateId){
			// 是否刪除編輯
			var isdelete = false;
			// 要刪除項所在位置標記
			var deleteIndex = 0;
			for(var i =0; i < templateInfoArray.length; i++){
				// 判斷是否存在所需刪除項，若存在則標記并得到其所在位置
 				if(templateInfoArray[i].templateId == templateId){
 					showColumnTemplate = templateInfoArray[i].fieldContent;
 					isdelete = true;
 					deleteIndex = i;
 					break;
 				}
 			}
			// 根據標記刪除模板編號項
			if(isdelete){
				templateInfoArray.splice(deleteIndex, 1);
			}
        }
        
        
        
        /*
         *得到多選款改選中值方法
         */
        function getMultiSelectValue(){
        	var allSel = $('#multiColumn').next().find("select");
			var	leftSel = allSel.eq(0);
			var	rightSel = allSel.eq(1);
			// 拿到右側所有選中數據
			var selectOption = rightSel.find("option");
			var selectValue = new Array();
			// 將所有選中數據添加至集合
			selectOption.each(function(index,obj){
				selectValue.push($(obj).val());
			});
			return selectValue;
        }
        
        
        
        
        /*
         *確認與更新模板的方法
         */
        function saveColumnTemplate(isSaveAndShow){
        	$("#columnBlockMsg").text("");
        	// 得到模板信息
			var contentColumn = getMultiSelectValue();
        	// 得到選中模板名稱
			var templateId = $("#selectTemplate").val();
        	if(contentColumn.length == 0){
        		$.messager.alert('提示訊息','請設定模板內容','warning');
        		return false;
        	}
        	// 驗證欄位
        	 var controls = ['templateName'];
             if(validateForm(controls)){
            	// 若為點擊保存按鈕
            	/* if(isSaveAndShow){
        			if(isEmpty(templateId)){
        				$.messager.alert('提示訊息','新模板創建請點擊更新','warning');
        				return false;
        			} else {
        				// 得到當前模板信息，判斷是否更改模板
        				var isChange = false;
        				javascript:dwr.engine.setAsync(false);
        				ajaxService.getUserColumnTemplate(templateId,function(returnValue){
        					if(returnValue != null && returnValue.fieldContent != contentColumn.toString()){
        						isChange = true;
        					}
        				});
        				javascript:dwr.engine.setAsync(true);
        				if(isChange){
        					$.messager.alert('提示訊息','模板已修改請點擊更新','warning');
            				return false;
        				} else {
        					// 置標誌位
        					$("#isClickOk").val('Y');
            				// 保存用戶模板數據
            				saveUserTemplateData(contentColumn, templateId, isOnlyShow);
        				}
        			}
        		// 若為點擊更新按鈕
            	} else {
            		// 保存用戶模板數據
    				saveUserTemplateData(contentColumn, templateId, isOnlyShow);
            	} */
            	
            	// 若為點擊保存按鈕
            	if(isSaveAndShow){
            		// 置標誌位
					$("#isClickOk").val('Y');
            		if(templateId == '100000000-0001'){
            			// 保存用戶模板數據
         				saveUserTemplateData(contentColumn, templateId, false, true);
            		} else {
            			// 保存用戶模板數據
         				saveUserTemplateData(contentColumn, templateId, isSaveAndShow, false);
            		}
				// 保存用戶模板數據	
            	} else {
            		// 保存用戶模板數據
     				saveUserTemplateData(contentColumn, templateId, isSaveAndShow, false);
            	}
             }
        }
        
        
        
        /*
         *保存或更新模板數據
         contentColumn ： 模板信息
         templateId ： 模板編號
         isOnlyShow ：是否為展示模板
         */
        function saveUserTemplateData(contentColumn, templateId, isSaveAndShow, isOnlyShow){
			// 增加遮罩
			var columnBlockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
        	$("#columnBlockDlg").block(columnBlockStyle);
    		// 綁定保存參數
        	var saveParam = {
       			actionId : "saveTemplate",
       			templateId : templateId,
       			isOnlyShow : isOnlyShow,
       			isSaveAndShow : isSaveAndShow,
       			templateName : $("#templateName").textbox('getValue'),
       			fieldContent : contentColumn.toString()
        	 };
        	 var url = "${contextPath}/userColumnTemplate.do";
     		//save
     		$.ajax({
     			url: url,
     			data:saveParam,
     			type:'post', 
     			cache:false, 
     			dataType:'json', 
     			success:function(json){
     				// 去除保存遮罩
     				$("#columnBlockDlg").unblock();
     				
     				if (json.success) {
     					// 若為展示模板
     					if(isOnlyShow || isSaveAndShow){
     						// 展示新模板，并關閉當前頁面
     						showUserColumn(contentColumn);
        					if($("#isClickOk").val() == 'Y'){
        						$("#columnBlockDlg").dialog('close');
        					}
     					} else {
     						// 顯示成功消息
     						$("#columnBlockMsg").text(json.msg);
     						// 若為新模板
     						if(templateId == ''){
     							// 添加新模板保存信息至集合
     			   				var templateInfo = {
 			   		        		templateId : json.templateId,
 			   		        		fieldContent : contentColumn
 			   		        	};
			   		        	templateInfoArray.push(templateInfo);
			   		        	// 可選模板列增加新模板
     			   				$("#selectTemplate").append('<option value="'+json.templateId+'">'+json.templateName+'</option>');
			   		        	// 刪除原新模板
     			   				deleteColumnByTemplateId(templateId)
	 			   				
	     			   			//清空并初始化多選下拉模板列
	     			   			clearMultiSelect();
	 			   				// 選中當前新增模板
     			      			$("#selectTemplate").val(json.templateId);
	 			   				// 將當前新模板列移至右側選中
     			      			selectColumnToRight(contentColumn);
     			      			// 將當前模板編號放置與上筆訊中模板編號
     		 	   				$("#beforeSelectTemplate").val(json.templateId);
     			      			// 啟用刪除
     		 	   				$("#columnBtnDelete").linkbutton('enable');
     						} else {
     							// 更新模板名稱
     							$("#selectTemplate").find("option[value='" + templateId + "']").text(json.templateName);
     						}
     					}
     				} else {
     					// 顯示錯誤消息
     					handleScrollTop('columnBlockDlg');
     					$("#columnBlockMsg").text(json.msg);
     				}
     			},
     			error:function(){
     				if(isOnlyShow){
     					$("#columnBlockMsg").text("展示用戶模板出錯");
     				} else {
     					$("#columnBlockMsg").text("更新用戶模板出錯");
     				}
     				// 去除保存遮罩
     				$("#columnBlockDlg").unblock();
     			}
     		});
        }
        
        
        
        /*
         *刪除用戶模板方法
         */
        function deleteColumnTemplate(){
        	$("#columnBlockMsg").text("");
        	var templateId = $("#selectTemplate").val();
        	// 不為新模板，才可刪除
    		if (templateId != '') {
    			$.messager.confirm('確認對話框','確認刪除?', function(confirm) {
    				if (confirm) {
    					// 遮罩样式
						var columnBlockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
    					$("#columnBlockDlg").block(columnBlockStyle);
    					$.ajax({
    						url:'${contextPath}/userColumnTemplate.do?actionId=<%=IAtomsConstants.ACTION_DELETE%>',
    						data:{templateId : templateId},
    						type:'post', 
    						cache:false, 
    						dataType:'json',
    						success:function(json) {
    							// 去除保存遮罩
    							$("#columnBlockDlg").unblock();
    	         				if (json.success) {
    	         					// 顯示刪除消息
    	         					$("#columnBlockMsg").text(json.msg);
    	         					// 可選模板移除刪除列
    	         					$("#selectTemplate option[value='"+templateId+"']").remove();
    	         					
    	         					// 模板集合移除該模板
         			   				deleteColumnByTemplateId(templateId);
    	         					
    	     			   			// 展示預設模板
    	     			   			settingDefaultTemplate(false);
    	         				} else {
    	         					// 顯示消息
    	         					$("#columnBlockMsg").text(json.msg);
    	         					handleScrollTop('columnBlockDlg');
    	         				}
    						},
    						error:function(){
    							$("#columnBlockDlg").unblock();
    						}
    					});
    				}
    			});
    		}
        }
        
        
        
        
        
        /*
        * 取消關閉頁面函數
        */
        function columnBlockCloseFun(){
        	if($("#isClickOk").val() == ''){
        		var isUpdate = true;
        		if(templateInfoArray.length != 0){
            		for(var i =0; i < templateInfoArray.length; i++){
            			if(templateInfoArray[i].templateId == '${currentColumnTemplate.value }'){
            				isUpdate = false;
            				break;
        				}
        			}
            	}
        		// 更新為預設模板
        		if(isUpdate){
        			// 得到選中模板名稱
        			var templateId = '100000000-0001';
                	var columnArray;
        			javascript:dwr.engine.setAsync(false);
        			ajaxService.getUserColumnTemplate(templateId,function(returnValue){
        				if(returnValue && returnValue.fieldContent){
        					var fieldContent = returnValue.fieldContent;
        					columnArray = fieldContent.split(',');
        				}
        			});
        			javascript:dwr.engine.setAsync(true);
        			if(columnArray && columnArray.length != 0){
        				// 按照選中模板更新
            			saveUserTemplateData(columnArray, templateId, false, true);
        			}
        		}
        	}
        }
        
        
        /*
        *設置為預設模板
        */
        function settingDefaultTemplate(){
    		var isLocal = false;
    		var defaultTemplateInfo;
    		// 判斷預設模板是否存在集合中
        	if(templateInfoArray.length != 0){
        		for(var i =0; i < templateInfoArray.length; i++){
        			if(templateInfoArray[i].templateId == '100000000-0001'){
        				isLocal = true;
        				defaultTemplateInfo = templateInfoArray[i].fieldContent;
        				break;
    				}
    			}
        	}
       		if(isLocal){
	   			//清空并初始化多選下拉模板列
	   			clearMultiSelect();
   				// 選中當前新增模板
      			$("#selectTemplate").val('100000000-0001');
   				// 將當前新模板列移至右側選中
      			selectColumnToRight(defaultTemplateInfo);
  			} else {
      			javascript:dwr.engine.setAsync(false);
   				ajaxService.getUserColumnTemplate('100000000-0001',function(returnValue){
   					if(returnValue && returnValue.fieldContent){
   						var fieldContent = returnValue.fieldContent;
   						var columnArray = fieldContent.split(',');
   						// 選中模板
   						var templateInfo = {
  		   					templateId : '100000000-0001',
  		   					fieldContent : columnArray
 		   				};
 		   		        templateInfoArray.push(templateInfo);
 		   		    	//清空并初始化多選下拉模板列
 	 			   		clearMultiSelect();
 			   			// 選中當前新增模板
 			      		$("#selectTemplate").val('100000000-0001');
 			   			// 將當前新模板列移至右側選中
 			      		selectColumnToRight(columnArray);
   					}
   				});
   				javascript:dwr.engine.setAsync(true);
  			}
       		// 將當前模板編號放置與上筆訊中模板編號
 			$("#beforeSelectTemplate").val('100000000-0001');
       		
			// 得到當前選中模板模板名稱
			var templateName = $("#selectTemplate").find("option:selected").text();
			// 設置模板名稱
			$("#templateName").textbox('setValue', templateName);
       		// 禁用更新按鈕
 			$("#columnBtnUpdate").linkbutton('disable');
 			// 禁用刪除
 			$("#columnBtnDelete").linkbutton('disable');
 			
			var templateNameOptions = $("#templateName").textbox('options');
 			templateNameOptions.missingMessage = '';
 			templateNameOptions.required = false;
 			templateNameOptions.disabled = true;
 			$("#templateName").textbox(templateNameOptions);
 			// 限制欄位長度
 			textBoxDefaultSetting($("#templateName"));
 			// 標記
 			$("#templateNameLabel").html("模板名稱:");
        }
	        
	        
	        
	</script>
</body>
</body>
</html>