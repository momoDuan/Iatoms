<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%@include file="/jsp/common/easyui-common.jsp"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractManageFormDTO"%>
<%
	SessionContext ctx = (SessionContext)request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	ContractManageFormDTO formDTO = null;
	BimContractDTO contractManageDTO = null;
	if(ctx != null) {
		formDTO = (ContractManageFormDTO) ctx.getResponseResult();
		if (formDTO != null ) {
			contractManageDTO = formDTO.getContractManageDTO();
		}  else {
			formDTO = new ContractManageFormDTO();
		}
	}
	if (contractManageDTO == null) {
		contractManageDTO = new BimContractDTO();
	}
	//獲取useCaseNo
	String ucNo = formDTO.getUseCaseNo();
	//客户集合
	List<Parameter> customerList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_CUSTOMER_LIST);
	//厂商集合
	List<Parameter> manuFacturerList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo,  IAtomsConstants.PARAM_MANU_FACTURER_LIST);
	//獲取設備類型集合
	String assetCategoryList = (String)SessionHelper.getAttribute(request, ucNo,  ContractManageFormDTO.PARAM_ASSET_CATEGORY_LIST);
%>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="contractManageDTO" value="<%=contractManageDTO%>" scope="page"></c:set>
<c:set var="customerList" value="<%=customerList%>" scope="page"></c:set>
<c:set var="manuFacturerList" value="<%=manuFacturerList%>" scope="page"></c:set>
<c:set var="assetCategoryList" value="<%=assetCategoryList%>" scope="page"></c:set> 
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Insert title here</title>
		<script type="text/javascript" src="${contextPath}/js/chart/Chart.js"></script>
		<script type="text/javascript" src="${contextPath}/js/chart/Chart.bundle.js"></script>
		<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
	</head>
	<body>
		<div style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
			<div style="width:800px;height:400px;display: inline-block;"><canvas id="myChart"></canvas></div>
			<div style="width:800px;height:400px;display: inline-block;"><canvas id="myChart2"></canvas></div>
		</div>
	</body>
	<script type="text/javascript">
		var bgColor = [];
		var customers = [];
		function randombg(){
	        return '#'+Math.floor(Math.random()*0xffffff).toString(16);
	    }
	
		$(function(){
			for(var i=0;i<3;i++){
				bgColor.push(randombg());
			}
			<c:forEach var="customer" items="${customerList}">
				if (customers.length < 12) {
					customers.push("${customer.name}");
				}	
			</c:forEach>
			console.info(customers);
			initDataChart();
		});
	    
	    function initDataChart(){
	    		// 设置数据和参数
	    	var radarChartData = { 
		        //labels: customers, 
		        labels:["0","000000000","0新王越公司","0宣揚公司","1","11","111","111","1111111111","11111111111111111111","111q","1122"],
		        //labels:["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"],
		        datasets: [
	                {
	                    label: "Merchant1",  //当前数据的说明
	                    fill: true,  //是否要显示数据部分阴影面积块  false:不显示
	                    borderColor: bgColor[0],//数据曲线颜色
	                    pointBackgroundColor: bgColor[0], //数据点的颜色
	                    data: [80, 90, 120, 30, 67, 59, 88, 30, 26, 19, 67, 98],  //填充的数据
	                },
	                {
	                    label: "Merchant2",  //当前数据的说明
	                    fill: true,  //是否要显示数据部分阴影面积块  false:不显示
	                    borderColor: bgColor[1],//数据曲线颜色
	                    pointBackgroundColor: bgColor[1], //数据点的颜色
	                    data: [21, 34, 35, 50, 45, 21, 70, 86, 64, 95, 34, 58],  //填充的数据
	                },
	                {
	                    label: "Merchant3",  //当前数据的说明
	                    fill: true,  //是否要显示数据部分阴影面积块  false:不显示
	                    borderColor: bgColor[2],//数据曲线颜色
	                    pointBackgroundColor: bgColor[2], //数据点的颜色
	                    data: [5, 13, 85, 38, 100, 115, 60, 93, 106, 45, 79, 130],  //填充的数据
	                }
	            ]
	    };
	    //设置选项
	    var options = {
	    	legend: {
	                 position: 'right',
	 		},
	        scales: {
	            yAxes: [{
	                ticks: {
	                	fontColor: "#AA0303",
	                    beginAtZero:true,
	                    min: 0,
	                    stepSize: 20
	                },
	                scaleLabel: {
	                                display: true,
	                                labelString: "Total",
	                                fontColor:"#AA0303",
	                                fontSize:15
	                             }
	            }],
	            xAxes: [{
	                        ticks: {
	                        	fontColor: "#003B76",
	                            autoSkip: true,
	                            autoSkipPadding: 10
	                        },
	                        scaleLabel: {
	                            display: true,
	                            labelString: "Month",
	                            fontColor:"#003B76",
	                            fontSize:15
	                        },
	                        axisLabel:{
	                            //X轴刻度配置
	                            interval:0, //0：表示全部显示不间隔；auto:表示自动根据刻度个数和宽度自动设置间隔个数
	                            intervalOffset:1,
	                            isStaggered:true
							},
							scaleLabel:{
								step:1
							},
	                    }]
	        }
	    }
	    var ctx = document.getElementById("myChart").getContext("2d");
	    var myBarChart = new Chart(ctx, {type: 'line',data: radarChartData, options:options});
	    
	    data = {
		    datasets: [{
		        data: [150000, 400000, 350000],
		        backgroundColor : [bgColor[0],bgColor[1],bgColor[2]]
		    }],
		
		    // These labels appear in the legend and in the tooltips when hovering different arcs
		    labels: [
		        'Merchant1',
		        'Merchant2',
		        'Merchant3'
		    ]
		};
		  //设置选项
	    var options2 = {
	    	legend: {
	                 position: 'right'
	 		},
	 		tooltips: {
	                callbacks : {
	                    title : function(items, data) {
	                        var index = items[0].index; // 获取当前所选图形的索引下标
	                        var labels = data.labels; // 找到他的原始label数组
	                        return labels[index];
	                    },
	                    label : function(tooltipItem, data) {
	                        var index = tooltipItem.index;
	                        var value = data.datasets[0].data[index];
	                        return value; // 此标签只处理返回的数组
	                    }
	                }
	    	}
	    }
		var ctx2 = document.getElementById("myChart2").getContext("2d");
	    var myBarChart2 = new Chart(ctx2, {type: 'pie',data: data,options:options2});
	    }
	</script>
</html>