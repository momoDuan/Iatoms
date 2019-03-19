<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.CompanyFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO"%>
<%
	//初始化加載頁面數據
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	//獲取公司類型集合
	List<Parameter> companyTypeList = (List<Parameter>) SessionHelper.getAttribute(request,IAtomsConstants.UC_NO_BIM_02010, IATOMS_PARAM_TYPE.COMPANY_TYPE.getCode());
 %>
<c:set var="companyTypeList" value="<%= companyTypeList%>" scope="page"></c:set>
<link href="${contextPath}/css/iatoms_style.css" type="text/css" rel="stylesheet"/>
<html>
<head>
<meta charset="UTF-8">
<%@include file="/jsp/common/easyui-common.jsp"%>
<script type="text/javascript" src="${contextPath}/js/easyui-extend.js"></script>
<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
</head>
<body>
<div style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
	<!-- <div region="center" style="width: auto; height: auto; padding: 1px; overflow-y: hidden"> -->
	<table id="dg" class="easyui-datagrid" title="公司基本訊息維護" style="width: 98%; height: auto"
            data-options="
                rownumbers:true,
                pagination:true,
                pageNumber:0,
                pageList:[15,30,50,100],
				pageSize:15,
				iconCls: 'icon-edit',
				singleSelect: true,
				nowrap : false,
				toolbar:'#tb'">
            <thead>
                <tr>
                	<th data-options="field:'companyId',sortable:true,hidden:true">公司主鍵</th>
                	<th data-options="field:'companyType',sortable:true,hidden:true">公司類型值</th>
                	<th data-options="field:'companyTypeName',width:180,sortable:true,halign:'center'">公司類型</th>
                    <th data-options="field:'companyCode',width:130,sortable:true,halign:'center'">公司代號</th>
                    <th data-options="field:'shortName',width:250,sortable:true,halign:'center'">公司簡稱</th>
                    <th data-options="field:'unityNumber',width:100,sortable:true,halign:'center'">統一編號</th>
                    <th data-options="field:'invoiceHeader',width:250,sortable:true,halign:'center'">發票抬頭</th>
                    <th data-options="field:'leader',width:250,sortable:true,halign:'center'">負責人</th>
                    <th data-options="field:'tel',sortable:true,width:180,halign:'center'">公司電話</th>
                    <th data-options="field:'fax',sortable:true,width:180,halign:'center'">公司傳真</th>
                    <th data-options="field:'applyDate',sortable:true,width:180,halign:'center',align:'left'">請款日</th>
                    <th data-options="field:'payDate',sortable:true,width:180,halign:'center',align:'left'">付款日</th>
                    <th data-options="field:'contact',sortable:true,width:200,halign:'center'">聯絡人</th>
                    <th data-options="field:'contactTel',sortable:true,width:180,halign:'center'">聯絡人電話</th>
                    <th data-options="field:'contactEmail',sortable:true,width:300,halign:'center'">聯絡人Email</th>
                    <th data-options="field:'companyEmail',sortable:true,width:300,halign:'center',formatter:formatMail">公司Email</th>
                    <th data-options="field:'customerCode',width:70,sortable:true,halign:'center'">客戶碼</th>
                    <th data-options="field:'dtidTypeName',width:150,sortable:true,halign:'center'">客户DTID方式</th>
                    <th data-options="field:'dtidType',sortable:true,width:160,hidden:true">客户DTID方式值</th>
                    <th data-options="field:'authenticationTypeName',width:150,sortable:true,halign:'center'">登入驗證方式</th>
                    
                    <th data-options="field:'isNotifyAo',width:70,sortable:true,halign:'center',align:'left',formatter:fomatterYesOrNo">通知AO</th>
                    
                    <th data-options="field:'addressLocationName',width:300,sortable:true,halign:'center'">公司地址</th>
                    <th data-options="field:'invoiceAddressLocationName',width:300,sortable:true,halign:'center'">發票地址</th>
                    <th data-options="field:'remark',sortable:true,width:500,halign:'center'">說明</th>
                </tr>
            </thead>
        </table>
        <div id="tb" style="padding: 2px 5px;">
        <form id="queryForm">
				<input type="hidden" id="actionId" name="actionId" />
				<input type="hidden" id="serviceId" name="serviceId" />
				<input type="hidden" id="useCaseNo" name="useCaseNo" />
				<input type="hidden" name="queryFlag" id="queryFlag" value="N"/>
          	  公司類型:
	        <cafe:droplisttag 
	               id="<%=CompanyFormDTO.QUERY_COMPANY_TYPE%>"
	               name="<%=CompanyFormDTO.QUERY_COMPANY_TYPE%>" 
	               css="easyui-combobox"
	               result="${companyTypeList}"
	               hasBlankValue="true"
	               blankName="請選擇"
	               style="width: 150px"
	               javascript="editable=false panelheight=\"auto\"">
	               </cafe:droplisttag>
       		公司簡稱:
            <input id="<%=CompanyFormDTO.QUERY_SHORT_NAME%>" 
            		name="<%=CompanyFormDTO.QUERY_SHORT_NAME%>" data-options="validType:['maxLength[50]']"
            		class="easyui-textbox" style="width: 110px" maxlength = "50">
            <a class="easyui-linkbutton" href="javascript:void(0)" iconcls="icon-search" id="btnQuery">查詢</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add'" id="btnAdd">新增</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-edit"  id="btnEdit" onclick="doUpdate()">修改</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" id="btnDelete" onclick="deleteCompanyInfo()">刪除</a>
        	<div class="red" id="message" ></div>
        	<div style="text-align: right"><a href="#" id="btnExport" style="width: 150px" onclick="doExport()" >匯出</a></div>
        </form>
        </div>
        <div id="dlg" modal ="true" ></div>
	</div>
	<script type="text/javascript">
	$(function(){
		//查詢
		$("#btnQuery").click(function(){
			query(1,true);
		});
		//跳轉到新增畫面
		$("#btnAdd").click(function(){
			$("#message").text("");
			viewEditData('新增公司基本訊息維護','<%=IAtomsConstants.ACTION_INIT_ADD%>', '');
		});
	});
    //修改
    function doUpdate(){
    	//獲取選中的行
	    var row = $('#dg').datagrid('getSelected');
		//不存在選中的行時給出提示消息
		if (row == null) {
			$.messager.alert('提示', '請勾選要操作的記錄!', 'warning');
			return false;
		} else {
			//存在選中的行時清除之前的msg，打開修改頁面
	       	$("#message").text("");
	       	viewEditData('修改公司基本訊息維護','<%=IAtomsConstants.ACTION_INIT_EDIT%>', row.companyId);
	   }
    }
    /*
     *跳轉到公司基本訊息維護詳情頁
     *title:頁面名稱
     *actionId:actionId
     *companyId:公司編號
     */
    function viewEditData(title,actionId, companyId) {
		$("#message").text("");
		//打開新增/修改頁面
		var viewDlg = $('#dlg').dialog({    
		    title: title,    
		    width: '780px',
		    height:'530px',
		    top:10,
		    closed: false,    
		    cache: false,
		  	//傳遞的參數
		    queryParams : {
		    	actionId : actionId,
		    	companyId : companyId
		    },
		    href: "${contextPath}/company.do",
		    modal: true,
		    //給頁面上的長度限制框添加maxlength屬性
		    onLoad : function() {
                  textBoxSetting("dlg");
            },
            //關閉頁面時移除提示消息
            onClose : function(){
            	$("div[data-list-required]").removeClass("div-tips").addClass("div-list").tooltip("destroy");
            },
		    buttons : [{
				text:'儲存',
				iconCls:'icon-ok',
				width :'90px',
				handler: function(){
					$("#errorMsg").text("");
					var controls = ['checkCompanyType','companyCode','shortName','checkAuthenticationType'];
					if (validateForm(controls) && $('#fm').form("validate")){
						// 調保存遮罩
						commonSaveLoading('dlg');
						//發送請求
						saveOrUpdate(actionId);
					} 
		    	}
			},{
				text:'取消',
				iconCls:'icon-cancel',
				width :'90px',
				handler: function(){
					//關閉畫面
					confirmCancel(function(){
						viewDlg.dialog('close');
					});
				}
			}]
		});    
	}
   	/*
   	 *查詢公司基本訊息信息
   	 *pageIndex:頁碼
   	 *isSaveOrEdit:
   	 */
    function query(pageIndex,isSaveOrEdit){
		//查詢條件
		var queryParam = $("#queryForm").form("getData");
		var options = {
				url : "${contextPath}/company.do?actionId=<%=IAtomsConstants.ACTION_QUERY%>",
				queryParams :queryParam,
				pageNumber:pageIndex,
				//查詢成功
				onLoadSuccess:function(data){
					if (isSaveOrEdit) {
						$("#message").text("");
						//查無資料
						if (data.total == 0) {
							// 提示查無數據
							$("#message").text(data.msg);
							//將匯出按鈕變為灰色並不可點擊
							$('#btnExport').attr("onclick","return false;");
							$('#btnExport').attr("style","color:gray;");
						} else {
							//查詢標誌位為Y
							$("#queryFlag").val("<%=IAtomsConstants.YES%>");
							//將匯出按鈕變為藍色且可編輯
							$('#btnExport').attr("onclick","doExport()");
							$('#btnExport').attr("style","color:blue;");
						 }
					}
					isSaveOrEdit = true;
				},
				//查詢失敗
				onLoadError : function() {
					$("#message").text("查詢失敗！請聯繫管理員");
				}
			}
		// 清空點選排序
		if(isSaveOrEdit){
			options.sortName = null;
		}
		openDlgGridQuery("dg", options);
    }
   	
   	//刪除公司基本訊息
	function deleteCompanyInfo() {
       	//清空之前的提示消息
       	$("#message").text("");
       	var row = $("#dg").datagrid('getSelected');
		//若沒有勾選的資料
		if (row == null) {
			$("#message").text('請勾選要操作的記錄!');
		} else {
			//存在勾選的資料
			var param = {
				companyId : row.companyId,
			};
			var url = '${contextPath}/company.do?actionId=<%=IAtomsConstants.ACTION_DELETE%>';
			//成功結果
			var successFunction = function(data) {
				$("#message").text(data.msg);
				if (data.success) {
					//計算要顯示的頁碼
					var pageIndex = calDeletePagerIndex("dg");
					query(pageIndex,false);
				} 
			};
			//失敗結果
			var errorFunction = function(){
				$.messager.alert('提示', "刪除失敗", 'error');	
			};
			//調用公共的刪除方式
			commonDelete(param,url,successFunction,errorFunction);
		}
	}
	//匯出
	function doExport(){
		//獲取查詢標誌位
		var queryFlag = $("#queryForm").find("#queryFlag");
		//若查詢標誌位為N，則提示用戶先進行查詢
		if(queryFlag.val() == "<%=IAtomsConstants.NO%>"){
			$.messager.alert('警告','請先查詢,再執行匯出!','error');
			return false;
		} else {
			//得到查詢到的數據
			var rows = $("#dg").datagrid("getData");	
			var queryParam = $('#queryForm').form("getData");
			//將參數解析為字串
			var params = parseParam(queryParam);
			//若存在要匯出的資料就發送請求
			if(rows.total > 0){
				var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
				$.blockUI(blockStyle);
				
				window.location.href = '${contextPath}/company.do?actionId=<%=IAtomsConstants.ACTION_EXPORT%>&' + params;
				
				ajaxService.getExportFlag('<%=IAtomsConstants.UC_NO_BIM_02010 %>',function(data){
					$.unblockUI();
				});
			}
		}
	}
	//將參數解析為字串
	var parseParam = function(param, key){
		var paramStr = "";
		//當參數的數據類型為string、number、boolean中的任意一種就對其進行解析
		if(param instanceof String||param instanceof Number||param instanceof Boolean){
			//中文字符需解析兩次
			if(param instanceof String){
				paramStr+="&"+key+"="+encodeURIComponent(encodeURIComponent(param));
			} else {
				paramStr+="&"+key+"="+encodeURIComponent(param);
			}
		} else {
			$.each(param,function(i){
				var k = key==null ? i:key + (param instanceof Array?"["+i+"]":"."+i);
				paramStr += '&' + parseParam(this, k);
			});
		}
		return paramStr.substr(1);
	};
	//新增、修改頁面的儲存操作
	function saveOrUpdate(actionId){
		var url="${contextPath}/company.do?actionId=<%=IAtomsConstants.ACTION_SAVE%>";
		$.ajax({
			url: url,
			data:$("#fm").form('getData'),
			type:'post', 
			cache:false, 
			dataType:'json', 
			success:function(data){
				// 去除保存遮罩
				commonCancelSaveLoading('dlg');
				if (data.success) {
					$('#dlg').dialog('close');
					$("#message").text(data.msg);
					var pageIndex = getGridCurrentPagerIndex("dg");
					query(pageIndex,false);
				}else{
					$("#errorMsg").text(data.msg);								
				}
			},
			error:function(){
				// 去除保存遮罩
				commonCancelSaveLoading('dlg');
				var msg;
				if (actionId == '<%=IAtomsConstants.ACTION_INIT_ADD%>') {
					msg = "新增失敗";
				} else {
					msg = "修改失敗";
				}
				$.messager.alert('提示', msg, 'error');							
			}
		});
	}
	//Bug #2774 將英文分號改為中文分號，用來換行 2017/11/07
	function formatMail(val, row, index){
		if(val!=null && val.indexOf(';')>=0){
			val = val.replaceAll(';',"；");
		}
		return val;
	}
</script>
</body>
</html>