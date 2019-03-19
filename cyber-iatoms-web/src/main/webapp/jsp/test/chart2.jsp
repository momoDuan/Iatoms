<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
		<!-- <script src="https://code.highcharts.com/highcharts.js"></script> -->
		<script type="text/javascript" src="${contextPath}/js/chart/highcharts.js"></script>
	</head>
	<body>
		<div style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
			<div id="container2" style="min-width:400px;height:400px"></div>
			<div></div>
			<div></div>
			<div></div>
			<div id="container" style="height: 300px"></div>
		</div>
	</body>
	<script type="text/javascript">
		$(function(){
			var bgColor = [];
			function randombg(){
		        return '#'+Math.floor(Math.random()*0xffffff).toString(16);
		    }
			/**
			*  取得Min~Max中随机数
			*/
			function getRandomNum(min, max) {
			    var range = max - min;
			    var rand = Math.random();
			    var dataValue = [];
			    for(var i = 0; i < 12; i++){
			    	rand = Math.random();
			    	dataValue.push((min + Math.round(rand * range)));
			    }
			    return dataValue;
			}
			/**
			* 獲取4個隨機顏色
			*/
			for(var i=0;i<4;i++){
				bgColor.push(randombg());
			}
			var customers = [];
			<c:forEach var="customer" items="${customerList}">
				if (customers.length < 12) {
					customers.push("${customer.name}");
				}
			</c:forEach>
			/**
			*
			*/
			/* Highcharts.chart('container', { */
				var chart = null;
			$.getJSON('${contextPath}/jsp/test/customer.json', function(data){
				console.info(data);
				chart = Highcharts.chart('container', {
				title: {
			        text: '圖表測試',
			        style:{
			        	color: bgColor[3]
			        }
			    },
			    xAxis: {
			       /*  categories: customers, */
			        /* labels:{
			        	step:1
			        }, */
			        type: 'datetime',
					dateTimeLabelFormats: {
						millisecond: '%H:%M:%S.%L',
						second: '%H:%M:%S',
						minute: '%H:%M',
						hour: '%H:%M',
						day: '%m-%d',
						week: '%m-%d',
						month: '%Y-%m',
						year: '%Y'
					}
			    },
			    yAxis: {
			        floor: 0,//y軸下線
			        ceiling: 150,//y軸上線
			        title: {
			            text: 'Percentage'
			        },
			        labels: {
			        	style: {
							color: 'block'
						},
						align: 'left',
						x: 0,
						y: -2
					}
			    },
			    plotOptions: {
			    	xDateFormat: '%Y-%m-%d',
					series: {
						allowPointSelect: true,//點擊柱形圖，圖形變灰色
					}
				},
			    series: [
/* 					{
						name:"1月",
						data: getRandomNum(1,150),
						color : bgColor[0],
						type: 'column'
					},
					{
						name:"2月",
						data: getRandomNum(1,100),
						color : bgColor[1],
						type: 'area'
					}, */
					{
						name:"3月",
						data: data,
						color : bgColor[2],
						type: 'area'
					},
				]
				});
			});
			
			
			/*$.getJSON('https://data.jianshukeji.com/jsonp?filename=json/usdeur.json&callback=?', function (data) {*/
			 $.getJSON('${contextPath}/jsp/test/data.json', function(data){ 
				chart = Highcharts.chart('container2', {
					chart: {
						zoomType: 'x'
					},
					title: {
						text: '美元兑欧元汇率走势图'
					},
					subtitle: {
						text: '鼠标拖动可以进行缩放'
					},
					xAxis: {
						type: 'datetime',
						dateTimeLabelFormats: {
							millisecond: '%H:%M:%S.%L',
							second: '%H:%M:%S',
							minute: '%H:%M',
							hour: '%H:%M',
							day: '%m-%d',
							week: '%m-%d',
							month: '%Y-%m',
							year: '%Y'
						}
					},
					tooltip: {
						xDateFormat: '%Y-%m-%d'
						/* dateTimeLabelFormats: {
							millisecond: '%H:%M:%S.%L',
							second: '%H:%M:%S',
							minute: '%H:%M',
							hour: '%H:%M',
							day: '%Y-%m-%d',
							week: '%m-%d',
							month: '%Y-%m',
							year: '%Y'
						} */
					},
					yAxis: {
						title: {
							text: '汇率'
						}
					},
					legend: {
						enabled: false
					},
					plotOptions: {
						area: {
							fillColor: {
								linearGradient: {
									x1: 0,
									y1: 0,
									x2: 0,
									y2: 1
								},
								stops: [
									[0, Highcharts.getOptions().colors[0]],
									[1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
								]
							},
							marker: {
								radius: 2
							},
							lineWidth: 1,
							states: {
								hover: {
									lineWidth: 1
								}
							},
							threshold: null
						}
					},
					series: [{
						type: 'area',
						name: '美元兑欧元',
						data: data
					}]
				});
			});
		});
	</script>
</html>