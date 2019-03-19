/**
 * 頁面加載完成執行函數
 */
$(function(){
	commonBtnInit();
});

	/**
	 * 按鈕初始化
	 */
	function commonBtnInit(){
		var btnEdit = $("#btnEdit");
		if(btnEdit.length > 0){
			btnEdit.linkbutton('disable');
		}
		var btnDelete = $("#btnDelete");
		if(btnDelete.length > 0){
			btnDelete.linkbutton('disable');
		}
		var btnSave = $("#btnSave");
		if(btnSave.length > 0){
			btnSave.linkbutton('disable');
		}
		var btnCancel = $("#btnCancel");
		if(btnCancel.length > 0){
			btnCancel.linkbutton('disable');
		}
		var btnDownload = $("#btnDownload");
		if(btnDownload.length > 0){
			btnDownload.attr("style","color:blue;");
		}
		var btnExport = $('#btnExport');
		if(btnExport){
			btnExport.attr("onclick","return false;");
			btnExport.attr("style","color:gray;");
		}
	} 

	/**
	 * 可行內編輯的查詢公用方法
	 * @param id ： dataGrid的id
	 * @param options ： dataGrid的options
	 * @param msgId ： 消息的id
	 */
	function rowEditGridQuery(id, options, msgId) {
		var grid = $("#" + id);
		// 临时使用的编辑行行号
		var tempEditIndex;
		// 遮罩样式
		var blockStyle = {message:'loading...',css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		// 请求数据加载前触发函数
		options.onBeforeLoad = function () {
			// 形成遮罩
			if(options.ignoreFirstLoad){
			// 形成遮罩
				$.blockUI(blockStyle);
			}
		};
		options.loadMsg = '';
		options.autoRowHeight=true;
		// 请求未成功时加载的函数
		var loadErrorFun= options.onLoadError;
		options.onLoadError = function() {
			if(options.ignoreFirstLoad){
				// 去除遮罩
				$.unblockUI();
			}
			if (loadErrorFun) {
				loadErrorFun();
			} 
		};
		// 请求成功时加载的函数
		var loadSuccessFun= options.onLoadSuccess;
		options.onLoadSuccess = function (data) {
			if(options.ignoreFirstLoad){
				// 去除遮罩
				$.unblockUI();
			}
			// 查询完后按钮控制
			btnHandle();
			if (loadSuccessFun) {
				loadSuccessFun(data);
			} 
		};
		// 行内编辑進入編輯时加载的函数
		var beforeEditFun= options.onBeforeEdit;
		options.onBeforeEdit = function (index, row) {
			tempEditIndex = index;
			if (beforeEditFun) {
				beforeEditFun();
			} 
		};
		// 行内编辑取消編輯时加载的函数
		var cancelEditFun= options.onCancelEdit;
		options.onCancelEdit = function (index, row) {
			tempEditIndex = undefined;
			if (cancelEditFun) {
				cancelEditFun();
			} 
		};
		// 行内编辑选中行与按钮禁用
		options.onSelect = function (index) {
			if (tempEditIndex != undefined) {
				if (tempEditIndex != index) {
					grid.datagrid('unselectRow', index);
					grid.datagrid('selectRow', tempEditIndex);
				}
			} else {
				$("#" + msgId).text("");
				var btnDelete = $("#btnDelete");
				if(btnDelete.length > 0){
					btnDelete.linkbutton('enable');
				}
				$('#btnDetail').linkbutton('enable');
			}
		}; 
		grid.datagrid(options);
	}
	
	/**
	 * 行内编辑進入編輯时加载的函数
	 */
	function commonOnBeforeEdit(index, row){
		// 形成遮罩
		gridHeaderPagerBlock("dataGrid");
		// 按钮控制
		var btnAdd = $("#btnAdd");
		if(btnAdd.length > 0){
			btnAdd.linkbutton('disable');
		}
		var btnQuery = $("#btnQuery");
		if(btnQuery.length > 0){
			btnQuery.linkbutton('disable');
		}
		var btnDelete = $("#btnDelete");
		if(btnDelete.length > 0){
			btnDelete.linkbutton('disable');
		}
		var btnSave = $("#btnSave");
		if(btnSave.length > 0){
			btnSave.linkbutton('enable');
		}
		var btnCancel = $("#btnCancel");
		if(btnCancel.length > 0){
			btnCancel.linkbutton('enable');
		}
	}
	
	/**
	 * 行内编辑取消編輯时加载的函数
	 */
	function commonOnCancelEdit(){
		// 放开进入行内编辑时形成的遮罩
		gridHeaderPagerUnBlock("dataGrid");
		// 按钮控制
		btnHandle();
	}
	
	/**
	 * 行内编辑完成编辑时加载的函数
	 */
	function commonOnEndEdit(){
		// 放开进入行内编辑时形成的遮罩
		gridHeaderPagerUnBlock("dataGrid");
		// 按钮控制
		btnHandle();
	}

	/**
	 * 查询、取消编辑等的按钮控制，主要针对于行内编辑的情况
	 */
	function btnHandle(){
		var btnAdd = $("#btnAdd");
		if(btnAdd.length > 0){
			btnAdd.linkbutton('enable');
		}
		var btnQuery = $("#btnQuery");
		if(btnQuery.length > 0){
			btnQuery.linkbutton('enable');
		}
		var btnDelete = $("#btnDelete");
		if(btnDelete.length > 0){
			btnDelete.linkbutton('disable');
		}
		var btnSave = $("#btnSave");
		if(btnSave.length > 0){
			btnSave.linkbutton('disable');
		}
		var btnCancel = $("#btnCancel");
		if(btnCancel.length > 0){
			btnCancel.linkbutton('disable');
		}
	}
	
	/**
	 * 打開對話框的查詢公用方法
	 * @param id ： dataGrid的id
	 * @param options ： dataGrid的options
	 */
	function openDlgGridQuery(id, options) {
		var grid = $("#" + id);
		if(grid.hasClass("easyui-datagrid")){
			$("#" + id).datagrid("clearSelections");
		}
		// 遮罩样式
		var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		// 请求数据加载前触发函数
		options.onBeforeLoad = function () {
			// 判斷是否忽略第一次加載遮罩的標志位
			if(!options.ignoreFirstLoad){
				// 形成遮罩
				if(options.isOpenDialog){
					$('#' + id).closest('div.panel').block(blockStyle);
				} else {
					$.blockUI(blockStyle);
				}
			}
		};
		options.loadMsg = '';
		options.autoRowHeight=true;
		// 请求未成功时加载的函数
		var loadErrorFun= options.onLoadError;
		options.onLoadError = function() {
			// 去除遮罩
			if(options.isOpenDialog){
				$('#' + id).closest('div.panel').unblock();
			} else {
				$.unblockUI();
			}
			if (loadErrorFun) {
				loadErrorFun();
			} 
		};
		// 请求成功时加载的函数
		var loadSuccessFun= options.onLoadSuccess;
		//update by 2017/08/22 对容器进行重新计算
		//$("#"+id).datagrid("resize");
		options.onLoadSuccess = function (data) {
			// 判斷是否忽略第一次加載遮罩的標志位
			if(!options.ignoreFirstLoad){
				// 去除遮罩
				if(options.isOpenDialog){
					$('#' + id).closest('div.panel').unblock();
				} else {
					$.unblockUI();
				}
			} else {
				// 點擊查詢時忽略遮罩
				options.ignoreFirstLoad = undefined;
			}
			// 禁用按鈕
			var btnEdit = $("#btnEdit");
			if(btnEdit.length > 0){
				btnEdit.linkbutton('disable');
			}
			var btnDelete = $("#btnDelete");
			if(btnDelete.length > 0){
				btnDelete.linkbutton('disable');
			}
			// 清空選中的行
			grid.datagrid("clearSelections");
			if (loadSuccessFun) {
				loadSuccessFun(data);
			} 
		};
		// 选中行按钮的释放(选中一行时编辑、删除按钮放开)
		if(!options.onSelect){
			options.onSelect = function() {
				var btnEdit = $("#btnEdit");
				if(btnEdit.length > 0){
					btnEdit.linkbutton('enable');
				}
				var btnDelete = $("#btnDelete");
				if(btnDelete.length > 0){
					btnDelete.linkbutton('enable');
				}
			};
		}
		grid.datagrid(options);
	}

	/**
	 * 通用的刪除方法
	 * @param params ： 传入后台的参数(删除的actionId须在此处定义)
	 * @param url ： .do请求的url
	 * @param successFunction ： 请求成功时加载的函数
	 * @param errorFunction ： 请求失败时加载的函数
	 */
	function commonDelete(params, url, successFunction, errorFunction){
		if (params && url) {
			comfirmDelete(function(){
				// 遮罩样式
				var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
				// 形成遮罩
				$.blockUI(blockStyle);
				// ajax请求
				$.ajax({
					url:url,
					data:params,
					type:'post', 
					cache:false, 
					dataType:'json', 
					success:function(data) {
						// 去除遮罩
						$.unblockUI();
						// 请求成功时加载的函数
						var deleteSuccess = successFunction;
						if(deleteSuccess){
							deleteSuccess(data);
						}
					},
					error:function(){
						// 去除遮罩
						$.unblockUI();
						// 请求失败时加载的函数
						var deleteError = errorFunction;
						if(deleteError){
							deleteError();
						}
					}
				});
			});
		}
	}
