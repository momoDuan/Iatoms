<%@page import="com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.ApplicationFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.ApplicationDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.GsonBuilder"%>
<%
	//初始化加載下拉框數據
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	ApplicationFormDTO formDTO = null;
	ApplicationDTO applicationDTO = null;
	List<Parameter> assetTypeList = null;
	String assetType = null;
	if (ctx != null) {
		formDTO = (ApplicationFormDTO) ctx.getResponseResult();
		 if(formDTO != null){
			 applicationDTO = formDTO.getApplicationDTO();
			 //獲取選中的設備類別+
			 assetType = applicationDTO.getAssetTypeId();
			 //獲取選中的適用設備
			 assetTypeList = formDTO.getAssetTypeList();
		} 
	} else {
		formDTO = new ApplicationFormDTO();
	} 
	//獲取適用設備集合
	List<Parameter> assetList =
		 (List<Parameter>) SessionHelper.getAttribute(request,IAtomsConstants.UC_NO_PVM_04010, ApplicationFormDTO.GET_ASSET_TYPE_LIST);
	//獲取客戶下拉框值
	List<Parameter> customerInfoList =
		 (List<Parameter>) SessionHelper.getAttribute(request,IAtomsConstants.UC_NO_PVM_04010, ApplicationFormDTO.CUSTOMER_INFO_LIST);
	//獲取設備類別
	List<Parameter> assertCategoryList =
		 (List<Parameter>) SessionHelper.getAttribute(request,IAtomsConstants.UC_NO_PVM_04010, IATOMS_PARAM_TYPE.ASSET_CATEGORY.getCode());
	Gson gsonss = new GsonBuilder().create();
	//將選中的適用設備轉換為字符串
	String assetListString = gsonss.toJson(assetList);
%>
<c:set var="assetTypeList" value="<%=assetTypeList%>" scope="page"></c:set>
<c:set var="assetList" value="<%=assetList%>" scope="page"></c:set>
<c:set var="applicationDTO" value="<%=applicationDTO%>" scope="page"></c:set>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set> 
<c:set var="customerInfoList" value="<%=customerInfoList%>" scope="page"></c:set>
<c:set var="assertCategoryList" value="<%=assertCategoryList%>" scope="page"></c:set>
<c:set var="assetListString" value="<%=assetListString%>" scope="page"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
	<div data-options="region:'center'"
		style="width: auto; height: auto; padding: 10px 20px; background: #fff; overflow-y: hidden">
		<div class="dialogtitle">程式版本維護</div>
		<span id="errorMsg" class="red"></span>
            <form id="fm" method="post" novalidate>
            	<table cellpadding="4">
            		<tr>
		    			<td>客戶:<span class="red">*</span></td>
		    			<td>
		    				<cafe:droplisttag 
				               id="<%=ApplicationDTO.ATTRIBUTE.CUSTOMER_ID.getValue()%>"
				               name="<%=ApplicationDTO.ATTRIBUTE.CUSTOMER_ID.getValue()%>" 
				               css="easyui-combobox"
				               result="${customerInfoList}"
				               selectedValue="${applicationDTO.customerId}"
				               disabled="${not empty applicationDTO.customerId}"
				               hasBlankValue="true"
				               blankName="請選擇"
				               style="width: 180px"
				               javascript="validType=\"requiredDropList\" editable=false invalidMessage=\"請輸入客戶\"">
				              </cafe:droplisttag>
		    			</td>
		    			<td>版本編號:<span class="red">*</span></td>
		    			<td> <input class="easyui-textbox" 
			    				name="<%=ApplicationDTO.ATTRIBUTE.VERSION.getValue()%>" 
			    				id="<%=ApplicationDTO.ATTRIBUTE.VERSION.getValue()%>" 
			    				maxlength="20" 
								validType="maxLength[20]"
			    				value="<c:out value='${applicationDTO.version}'/>" style="width: 180px"
			    				data-options="required:true,missingMessage:'請輸入版本編號'"/> 
		    				<input type="hidden" 
		    					value="<c:out value='${applicationDTO.applicationId}'/>" 
		    					name="<%=ApplicationDTO.ATTRIBUTE.APPLICATION_ID.getValue()%>"/> 
		    			</td>
		    		</tr>
		    		<tr>
		    			<td>程式名稱:<span class="red">*</span> </td>
		    			<td><input class="easyui-textbox"
			    			 value="<c:out value='${applicationDTO.name}'/>" 
			    			 name="<%=ApplicationDTO.ATTRIBUTE.NAME.getValue()%>"
			    			 id="<%=ApplicationDTO.ATTRIBUTE.NAME.getValue()%>"
			    			 maxlength="50"
			    			 style="width: 180px" 
			    			 data-options="required:true,missingMessage:'請輸入程式名稱'"/> 
		    			</td>
		    			<td>設備類別:</td>
		    			<td>
		    			<cafe:droplisttag 
			               id="<%=ApplicationDTO.ATTRIBUTE.ASSET_CATEGORY.getValue()%>"
			               name="<%=ApplicationDTO.ATTRIBUTE.ASSET_CATEGORY.getValue()%>" 
			               css="easyui-combobox"
			               result="${assertCategoryList}"
			               selectedValue=""
			               hasBlankValue="true"
			               blankName="請選擇"
			               style="width: 180px"
			               javascript="editable=false invalidMessage=\"請輸入設備類別\" panelheight=\"auto\"">
		              	</cafe:droplisttag>
			    		</td>
		   			</tr>
		    		<tr >
		    			<td >適用設備:<span class="red">*</span></td>
		    			<td colspan="3">
		    				<a href='#' id='select-all' style="color:blue"><font size="2">全選</font></a>
	                        <a href='#' id='deselect-all' style="color:blue" ><font size="2">取消全選</font></a>
	                        <div id="checkWeekRests" class="div-list" data-list-required='請輸入適用設備' style="width:370px">
	                   		<select multiple='multiple' id="<%=ApplicationDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue()%>" name="<%=ApplicationDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue()%>" >
								<c:forEach items="${assetList}" var="item"  varStatus="rowid" >
			             			<c:set var="isSelect" value="${false}"/>
				             			<c:forEach items="${applicationDTO.assetTypeId}" var="assetItem">
				             				<c:if test = "${item.value eq assetItem }">
				             					<c:set var="isSelect" value="${true}"/>
				             				</c:if>
				             			</c:forEach>
									<option value="${item.value}" <c:if test="${isSelect}">selected</c:if>>${item.name}</option> 
								</c:forEach>
					        </select>
					        </div>
				    	</td>
				    	
		    		</tr>
		    		<tr>
		    			<td>說明:</td>
		    			<td colspan="3">
	        	       		<textarea id="<%=ApplicationDTO.ATTRIBUTE.COMMENT.getValue()%>" 
        	       			   name="<%=ApplicationDTO.ATTRIBUTE.COMMENT.getValue()%>"
        	       			   class="easyui-textbox" 
        	       			   maxlength="200"
        	       			   data-options="multiline:true, validType:['maxLength[200]']"  
        	       			   style="height: 70px;width:450px"><c:out value='${applicationDTO.comment}'/></textarea>
        	     		</td>
		    		</tr>
            	</table>
            </form>
	</div>
	
<script type="text/javascript">
	var assetTypeIds = null;
	$(function(){
		//
		showdivListRed('${applicationDTO.assetTypeId}','checkWeekRests');
		
		$('#assetTypeId').multiSelect();
		$('#select-all').click(function () {
		    $('#assetTypeId').multiSelect('select_all');
		    return false;
		});
		$('#deselect-all').click(function () {
			var assetTypeIdObj = $('#assetTypeId');
			if(assetTypeIds == null) {
		    	assetTypeIdObj.multiSelect('deselect_all');
			} else {
				//重新加載左邊的list
				var fm = $('#fm').form("getData");
				var assetCategory = $('#fm').find("input[name=assetCategory]").val();
				//拿到設備類別，查詢到左邊的lsit，來重新加載數據
				var url="${contextPath}/application.do?actionId=<%=IAtomsConstants.ACTION_GET_ASSET_TYPE_LIST%>";
	    		 $.ajax({
						url: url,
						data:{
							assetCategory:assetCategory
						},
						type:'post', 
						cache:false, 
						dataType:'json', 
						success:function(data){
							assetTypeIdObj.multiSelect('destroy');
							assetTypeIdObj.empty();
							if(data.row != null){
								for (var i = 0; i < data.row.length; i++){
									assetTypeIdObj.append("<option value="+data.row[i].value+">"+data.row[i].name+" </option>");
								} 
							}
							assetTypeIdObj.multiSelect();
						},
					});
			}
		    return false;
		});
		$('#assetTypeId').multiSelect();
		
		$('#assetCategory').combobox({
	    	onChange:function(newValue,oldValue){
	    		var fm = $('#fm').form("getData");
	    		//右邊目前有的list的value
				var assetTypes = $('#fm').find("#assetTypeId").val();
				//全部list。左邊的
				assetTypeIdList =  initSelect(<%=assetListString%>); 
				//選中的設備（右邊）轉換成list value和name
				var selectAssetTypes = new Array();
				if(assetTypes != null) {
					for(var i=0; i<assetTypes.length; i++){
						if(assetTypeIdList != null) {
							for(var j=0;j<assetTypeIdList.length;j++) {
								if(assetTypes[i] == assetTypeIdList[j].value) {
									selectAssetTypes.push({"text":assetTypeIdList[j].name,"value":assetTypes[i]});
								}
							}
						}
					}
				}
	    		var url="${contextPath}/application.do?actionId=<%=IAtomsConstants.ACTION_GET_ASSET_TYPE_LIST%>";
	    		 $.ajax({
						url: url,
						data:{
							assetCategory:newValue
						},
						type:'post', 
						cache:false, 
						dataType:'json', 
						success:function(data){
							var obj = $('#assetTypeId');
							obj.multiSelect('destroy');
							obj.empty();
							//選中的設備（右邊）轉換成list value和name
							for(var i = 0; i < selectAssetTypes.length; i++) {
								obj.append("<option value="+selectAssetTypes[i].value+">"+selectAssetTypes[i].text+" </option>");
							}
							if(data.row != null){
								for (var j = 0; j < data.row.length; j++){
									var isRepeat = false;
									for( var i = 0;i< selectAssetTypes.length; i++){
										if(selectAssetTypes[i].value == data.row[j].value) {
											isRepeat = true;
											break;
										}
									}
									//查詢出來左邊的list
									if(!isRepeat) {
										obj.append("<option value="+data.row[j].value+">"+data.row[j].name+" </option>");
									}
								}
							}
							obj.multiSelect({
								afterDeselect: function(values){
									var isRepeat = false;
									if(data.row != null) {
										for(var j = 0; j < data.row.length; j++) {
											if(values == data.row[j].value) {
												isRepeat = true;
												break;
											}
										}
									}
									if(!isRepeat) {
										//點擊右邊 取消選擇的設備，判讀設備是否存在於左邊.
										for(var k = 0; k < assetTypeIdList.length; k++) {
											if(assetTypeIdList[k].value == values) {
												var assetTypeIdNode = obj.find("[value='" + values+"']");
												assetTypeIdNode.remove();
												obj.multiSelect("refresh");
												break;
											}
										}
									}
								}
							});
							
							obj.multiSelect();
							//重新加載list之後，讓右邊的數據 再進行選中.
							for(var i = 0; i < selectAssetTypes.length; i++) {
								obj.multiSelect('select',selectAssetTypes[i].value);
							}
							assetTypeIds = selectAssetTypes;
						},
					});
	    		}
			});
	});
</script>
</body>
</html>