<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<%@include file="/jsp/common/easyui-common.jsp"%>
</head>
<body>
	<div id="p" class="easyui-panel" title="查核件異常報表" style="width: auto; height: auto;">
		<div title="" style="padding: 10px">
			<div id="tb" style="padding: 2px 5px;">
				客戶:<input class="easyui-combobox" name="customer"
											 data-options="
												url:'../JasonData/combobox_customer.json',
												method:'get',
												textField:'name',
												pageList:[15,30,50,100],
												pageSize:15,
												valueField:'value',
												panelHeight:'auto',
												width:'150px'
															" />
				進件時間:	<input class="easyui-datebox" name="startTime" data-options="formatter:myformatter,parser:myparser,width:'150px'"></input>~<input class="easyui-datebox" name="endTime" data-options="formatter:myformatter,parser:myparser,width:'150px'"></input>
				<a class="easyui-linkbutton" href="#" iconcls="icon-search">查詢</a>
			</div>
			<table id="dg" class="easyui-datagrid" title="查核單進件及結案分析" style="width: auto; height: auto"
					data-options="
						rownumbers:true,
						pagination:false,
						iconCls: 'icon-edit',
						url: '../JasonData/datagrid_checkAndClose.json',
						nowrap:false,
						method: 'get'
						">
				<thead>
					<tr>
						<th data-options="field:'caseType',width:120,sortable:true">案件類別</th>
						<th data-options="field:'through',width:60,sortable:true">通過</th>
						<th data-options="field:'notThrough',width:60,sortable:true">不通過</th>
						<th data-options="field:'dispatched',width:120,sortable:true">失敗-特店拒絕查核</th>
						<th data-options="field:'notBusiness',width:120,sortable:true">失敗-特店未營業</th>
						<th data-options="field:'notPresent',width:120,sortable:true">失敗-EDC不在現場</th>
						<th data-options="field:'addressError',width:120,sortable:true">失敗-地址有誤</th>
						<th data-options="field:'endBusiness',width:120,sortable:true">失敗-特店已結束營業</th>
						<th data-options="field:'other',width:60,sortable:true">失敗-其他</th>
						<th data-options="field:'total',width:120,sortable:true">總計</th>
					</tr>
				</thead>
			</table>
	
			
			<div style="margin-top: 10px;">
			</div>
			<div align="right">
				<a href="#">匯出</a>
			</div>
			<table id="dg1" class="easyui-datagrid" title="查核單明細" style="width: auto; height: auto"
					data-options="
						rownumbers:true,
						pagination:true,
						pageList:[15,30,50,100],
						pageSize:15,
						iconCls: 'icon-edit',
						nowrap:false,
						url: '../JasonData/datagrid_checkDetailList.json',
						method: 'get'
						">
				<thead>
					<tr>
						<th data-options="field:'customer',width:120,sortable:true">客戶</th>
						<th data-options="field:'specialStoreNo',width:120,sortable:true">特店代號</th>
						<th data-options="field:'specialStoreName',width:120,sortable:true">特店登記名稱</th>
						<th data-options="field:'result',width:120,sortable:true">查核結果</th>
						<th data-options="field:'descption',width:120,sortable:true">查核描述</th>
						<th data-options="field:'closeDate',width:120,sortable:true">結案日期</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<script type="text/javascript">
		function myformatter(date){
			var y = date.getFullYear();
			var m = date.getMonth()+1;
			var d = date.getDate();
			return y+'/'+(m<10?('0'+m):m)+'/'+ (d<10?('0'+d):d);
		}
		function myparser(s){
			if (!s) return new Date();
			var ss = (s.split('/'));
			var y = parseInt(ss[0],10);
			var m = parseInt(ss[1],10);
			var d = parseInt(ss[2],10);
			if (!isNaN(y) && !isNaN(m)&& !isNaN(d)){
				return new Date(y,m-1,d);
			} else {
				return new Date();
			}
		}
	</script>
</body>
	
</html>