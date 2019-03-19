 //***************easyui 通用處理方法**********************//
/**
 * easyui textbox maxlength限制方法，檢測textbox的maxlength屬性，並把它加入到原始的textbox輸入框上，從而限制輸入過長的字符。
 * 做法：給需要限制的easyui-textbox增加maxlength屬性即可。
 * Evan  2016/09/06
 */
$(function(){
	textBoxSetting();
	//dateBoxsDefaultSetting();
	dateboxSetting();
	// 調用多選下拉框選中事件
	mutilComboSelectEvent();
	$(".setting-panel-height").css("height", $(window).height() - 18);
	// 禁止頁面Backspace, F5, Alt+Left, Alt+Right, Ctrl+R 按鍵
	document.onkeydown = function(e) {
		if (!e) var e = window.event;
		limitFunctionKeyAction();
	}
});
/**
 * 限制某些文本框輸入長度（maxLength），取消前後空格
 * textObj jquery對象 用法：$("#" + id)
 */
 function textBoxsDefaultSetting(textObjs) {
 	$(textObjs).each(function(i,item){
		var textObj = $(item);
		textBoxDefaultSetting(textObj);		
	});
}
/**
 * 限制某些日期框輸入長度（maxLength），取消前後空格
 * textObj jquery對象 用法：$("#" + id)
 */
 function dateBoxsDefaultSetting(dateObjs) {
 	$(dateObjs).each(function(i,item){
		var textObj = $(item);
		textBoxDefaultSetting(textObj);		
	});
 }
 
/**
 * 限制某一文本框輸入長度（maxLength），取消前後空格
 * textObj jquery對象 用法：$("#" + id)
 */
function textBoxDefaultSetting(textObj) {
	textObj.textbox('textbox').bind('blur', function(e){
		var setting = textObj.attr("setting");
		if (!setting) {
			textObj.textbox('setValue', $.trim($(this).val()));				
		} else if (setting == 'hidden'){
			textObj.textbox('setValue', $.trim($(this).siblings("input:hidden").val()));
		}
	});
	var maxlength = textObj.attr('maxlength');
	if (maxlength) {
		textObj.textbox('textbox').attr('maxlength',maxlength);
	}
}
/**
 * 做法：給需要限制的easyui-textbox增加maxlength屬性；
 * textbox增加blur事件，取消前後空格
 * Evan  2016/09/06
 */
function textBoxSetting(div) {
	var inputObj;
	if (typeof(div)=="undefined") {
		inputObj = $("form .easyui-textbox:input,form .easyui-numberbox");
	} else {
		inputObj = $("#" + div).find(".easyui-textbox:input,.easyui-numberbox");
	}	
	$(inputObj).each(function(i,item){
		var textObj = $(item);
		textBoxDefaultSetting(textObj);		
	});
	//時間微調控件的長度控制
	var spinner = $("form .easyui-timespinner");
	$(spinner).each(function(i,item){
		var textObj = $(item);
		var maxlength = textObj.attr('maxlength');
		if (maxlength) {
			textObj.textbox('textbox').attr('maxlength',maxlength);
		}
	});
	checkedDivListOnchange();
}
//日期空間刪除數據時重新進行賦值
function dateboxSetting(div) {
	var items = $(".easyui-datebox");
	if (typeof(div)=="undefined") {
		items = $(".easyui-datebox");
	} else {
		items = $("#" + div).find(".easyui-datebox");
	}	
	items.each(function(index,item){
		var obj = $(item);
		obj.textbox("textbox").bind("blur",function(){
			if ($(this).val() == ""){
				obj.datebox("clear");
			}
		});
		var maxlength = obj.attr('maxlength');
		if (maxlength) {
			obj.textbox('textbox').attr('maxlength',maxlength);
		}
	});
}
/**
 * 臨時做法
 */
function textBoxMaxlengthSetting() {
	var elements = $('form .easyui-textbox:input[maxlength],form .easyui-numberbox:input[maxlength]');
	$(elements).each(function(i,item){
	    var len = $(item).attr('maxlength');
	    $(item).next().find('.textbox-text:input').attr('maxlength',len);
	});
}
/**
 * 初始化月份日曆控件
 */
 function initMonthDateBox() {
	$(".calendar-menu-year").attr("disabled", true);
 }
//複選框驗證
function checkedDivListOnchange() {
	var checksDiv = $("div[data-list-required]");
	if (checksDiv.length > 0) {
		checksDiv.each(function(index,obj){
			var check = $(obj).children();
			var message = $(obj).attr("data-list-required");
			if (check.length > 0){
				check.each(function(childrenIndex,childrenObj){
					$(childrenObj).bind("change.checkValidate",function(){
						var name = $(this).attr("name");
						//if ($('[name=' + name + ']:checked').length <= 0){
						var isTrue = false
						if ($(this).prop("tagName").toUpperCase() == "SELECT") {
							isTrue = ($(this).val() == null);
						} else {
							isTrue = ($('[name=' + name + ']:checked').length <= 0);
						}
						if (isTrue) {
							//添加提示框 
							$(obj).tooltip({    
								position: 'right',   
								content: '<span style="color:#000">' + message + '</span>', 
								onShow: function(){
									$(this).tooltip('tip').css({
										backgroundColor: 'rgb(255,255,204)',
										borderColor: 'rgb(204,153,51)'
									});
								}   
							});
							//$("#" + id).children().css("border","1px #000 solid");
							$(obj).removeClass("div-list").addClass("div-tips").tooltip("show");
						} else {
							$(obj).removeClass("div-tips").addClass("div-list").tooltip("destroy");
						}
					});
				});
			}
		});
	}
}
//“儲存”按鈕驗證
function checkedDivListValidate(){
	var result = true;
	var checksDiv = $("div[data-list-required]");
	if (checksDiv.length > 0) {
		checksDiv.each(function(index,obj){
			var check = $(obj);
			var message = check.attr("data-list-required");
			if (check.find(":checked").length == 0) {
				check.tooltip({    
					position: 'right',   
					content: '<span style="color:#000">' + message + '</span>', 
					onShow: function(){
						$(this).tooltip('tip').css({
							backgroundColor: 'rgb(255,255,204)',
							borderColor: 'rgb(204,153,51)'
						});
					}   
				});
				check.removeClass("div-list").addClass("div-tips").tooltip("show");
				result = false;
			}
		});
	}
	return result;
}
/**
 * 初始化頁面時，將divList multiple必填欄位變紅
 * selectId:編輯頁面自定義多選編輯框預設值div
 * divId：編輯頁面自定義多選框
 */
function showdivListRed(selectId,divId){
	if(selectId ==''){
		var message = $("#" + divId).attr("data-list-required");
		$("#" + divId).tooltip({    
			position: 'right',   
			content: '<span style="color:#000">' + message + '</span>', 
			onShow: function(){
				$(this).tooltip('tip').css({
					backgroundColor: 'rgb(255,255,204)',
					borderColor: 'rgb(204,153,51)'
				});
			}   
		});
		$("#" + divId).removeClass("div-list").addClass("div-tips").tooltip("hide");
	}
}

//初始化時加載DivList驗證紅框，隱藏驗證消息
function hideDivListValidate(){
	var result = true;
	var checksDiv = $("div[data-list-required]");
	if (checksDiv.length > 0) {
		checksDiv.each(function(index,obj){
			var check = $(obj);
			var message = check.attr("data-list-required");
			if (check.find(":checked").length == 0) {
				check.tooltip({    
					position: 'right',   
					content: '<span style="color:#000">' + message + '</span>', 
					onShow: function(){
						$(this).tooltip('tip').css({
							backgroundColor: 'rgb(255,255,204)',
							borderColor: 'rgb(204,153,51)'
						});
					}   
				});
				check.removeClass("div-list").addClass("div-tips").tooltip("hide");
				result = false;
			}
		});
	}
	return result;
}




/**
 * 取消確認框，提示是否取消
 * @param fun：用戶選是後的操作
 * Evan
 */
function confirmCancel(fun) {
	$.messager.confirm('確認對話框','確認取消?', function(confirm){
		if (confirm) {
			fun();
		}		
	});
}
/**
 * 刪除確認框，提示是否刪除
 * @param fun：用戶選是後的操作
 * Evan
 */
function comfirmDelete(fun) {
	$.messager.confirm('確認對話框','確認刪除?', function(comfirm){
		if (comfirm) {
			fun();
		}
	});
}
/**
 * 一般確認框
 * @param msg：提示消息
 * @param fun：用戶選是後的操作
 */
function comfirmCommon(msg,fun) {
	$.messager.confirm('確認對話框',msg, function(comfirm){
		if (comfirm) {
			fun();
		}
	});
}

/**
 * 彈出錯誤信息的消息框
 * @param msg
 * @param fun
 */
function alertErrorCommon(msg1,fun) {
	$.messager.alert({title:'錯誤訊息',
		msg:msg1,
		icon:'error',
		fun:fun,
		onClose:fun});
}
/**
 * 彈出提示信息的消息框
 * @param msg
 * @param fun
 */
function alertPromptCommon(msg, closable, fun) {
	$.messager.alert({
		title:'提示訊息',
		msg:msg,
		closable:closable,
		icon:'warning',
		fn:function(){
			fun();
		}
	}); 
}
/**
 * 彈出編輯框，包含保存，取消按鈕
 * @param dialogId：dlg的id
 * @param options：easyui dialog的屬性，只用初始化特殊的屬性
 * @param saveFunction：保存按鈕的事件處理function
 * @param cancelFunction：取消按鈕的事件處理function， 如果沒有特殊操作，只是關閉dialog，可以不傳入
 * @returns： dialog對象
 */
function showDialog(dialogId, options, saveFunction, cancelFunction) {
	options.closed = false;    
	options.cache = false;
	options.modal = false;
	var loadfun = options.onLoad;
	options.onLoad = function () {
		textBoxSetting(dialogId);
		if (loadfun) {
			loadfun();
		}		
	}
	if (!options.buttons || options.buttons.length == 0) {
		options.buttons = [
            {
				text:'儲存',
				iconCls:'icon-ok',
				handler: saveFunction
			},{
				text:'取消',
				iconCls:'icon-cancel',
				handler: function(){
					confirmCancel(function(){
						if (cancelFunction) {
							cancelFunction();
						} else {
							viewDlg.dialog('close');
						}								
					});
				}
			}];
	}
	var viewDlg = $("#" + dialogId).dialog(options).dialog("center");
	return viewDlg;
}
/**
 * 在頂層window彈出dialog
 * @param options：easyui dialog的options
 */
function showTopDialog(options) {
	options.content = '<iframe scrolling="no" frameborder="0"  src="' + options.href + '" style="width:100%;height:100%;"></iframe>';
	options.loadingmessage = '正在加載數據，請稍等片刻......';
	options.shadow = false;
	options.onClose = function(){ 
		openWin.window("destroy"); 
	};
	var openWin = window.top.$('<div id="myWinId" class="easyui-dialog" closed="true"></div>').appendTo(window.top.document.body);
	openWin.dialog(options);
}


/**
 * 獲得頂層窗口
 * Evan
 */
function getTopWinow(){    
    var p = window;    
    while(p != p.parent){    
        p = p.parent;    
    }    
    return p;    
}   

/**
 * 判斷下拉列表中是否包含指定的值
 * @param data ：下拉列表
 * @param value ：需要判斷的數值
 * @returns {Boolean}
 */
function checkExistValue(data,value) {
	if (data == null || isEmpty(value)) return false;
	for (var i = 0; i < data.length; i++) {
		if (data[i].value == value) {
			return true;
		}
	}
	return false;
}

//***************easyui 驗證規則擴展**********************//
$.extend($.fn.validatebox.defaults.rules, {
	/**
	 * 必須和某個字段一致,比如密碼,確認密碼等
	 * param為要比較的控件名稱
	 */
    equalTo: {
        validator:function(value,param){
            return $(param[0]).textbox("textbox").val() == value;
        },
        message:'兩次輸入不一致'
    },
    /**
     * 最小輸入長度判斷
     * minLength[1]
     */
    minLength: {     
    	validator: function(value, param){  
    		return value.length >= param[0];     
    	},     
    	message: '請輸入最小{0}位字符'    
    },
    /**
     * 包含漢字的最大長度判斷
     * checkChineseChaLength[10]
     */
    checkChineseChaLength: {
		validator: function(value, param){  
			var length = value.length;
			for (var i = 0; i < value.length; i++) {
				if((/[\u4e00-\u9fa5]+/).test(value[i])){
					length = length + 1;
				}
			}
			return param[0] >= length;   
    	},     
    	message: '請輸入最大{0}位字符'   
    },
    /**
     * 比較密碼長度起訖.
     */
    mycompare: {
		validator:function(value,param) {
			var data = $(param[0]).combobox("getValue");
			if(parseInt(data)<=parseInt(value)){
				return true;
			}else{
				return false;
			}	
		},
		message:'密碼長度起大於密碼長度迄，請重新輸入'
	},
    /**
     * 最大輸入長度判斷
     * maxLength[10]
     */
    maxLength: {
    	validator: function(value, param){    
    	    return param[0] >= value.length;     
    	},     
    	message: '請輸入最大{0}位字符'    
    },
    /**
     * 輸入長度判斷
     * length[4,10]
     */
    length: {     
    	validator: function(value, param){
    		//千分為轉換為數字
    		//value=value.replace(/,/gi,'');
    	    return value.length >= param[0] && param[1] >= value.length;     
    	},     
    	message: '請輸入{0}-{1}位字符'    
    },
    /**
     * web地址驗證
     */
    web : {     
		validator: function(value){     
			return /^(http[s]{0,1}|ftp):\/\//i.test($.trim(value));     
		},     
		message: '網址格式錯誤'    
	},
	/**
	 * 驗證電話號碼
	 */
	phone : { 
		validator : function(value) {
			return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
		}, 
		message : '格式不正確,請使用下面的格式:020-88888888'
	}, 
	/**
	 * 驗證電話格式，只能輸入數字和-
	 */
	allPhone : {
		validator : function(value) {
			return /^[0-9\-]+$/i.test(value);
		}, 
		message : '格式不正確,限輸入數字或者-'
	},
	
	/**
	 * 驗證電話號碼
	 */
	telphone : { 
		validator : function(value,param) {
			return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
		}, 
		message : '{0}輸入有誤，請重新輸入'
	}, 
	/**
	 * 手機號碼驗證
	 */
    mobile : {     
    	validator: function(value){     
    		return /^1[0-9]{10}$/i.test($.trim(value));     
		},     
		message: '手機號碼錯誤,請輸入下列的格式:13922225555'    
	},

	/**
	 * 數字
	 * Evan
	 */
	number: {   
		validator: function (value, param) {
			//千分為轉換為數字
    		value=value.replace(/,/gi,'');
			if (/^\+?[1-9][0-9]*$/i.test(value)){
				return true;
			} else {
				if (param && param.length >= 1) {
					$.fn.validatebox.defaults.rules.number.message = param[0];
				}
				return false;
			}
		},   
		message: '請輸入數字'   
	},
	/**
	 * 非負整數 第一位 可以為零，不允許001等前面連續多個0出現
	 * Hermanwang
	 */
	numberNonnegative: {   
		validator: function (value, param) {
			//千分為轉換為數字
    		value=value.replace(/,/gi,'');
			if (/^(0|[1-9]\d*)$/i.test(value)){
				return true;
			} else {
				if (param && param.length >= 1) {
					$.fn.validatebox.defaults.rules.numberNonnegative.message = param[0];
				}
				return false;
			}
		},   
		message: '請輸入數字'   
	},
	/**
	 * 驗證數字，可以以0開頭
	 */
	numberBeginZero: {   
		validator: function (value, param) {
			//千分為轉換為數字
			value=value.replace(/,/gi,'');
			if (/^\+?[0-9]*$/i.test(value)){
				return true;
			} else {
				if (param && param.length >= 1) {
					$.fn.validatebox.defaults.rules.numberBeginZero.message = param[0];
				}
				return false;
			}
		},   
		message: '請輸入數字'   
	},
	/**
	 * 核檢數字是否符合等於的長度
	 */
	numberLengthEquals: {
		validator: function (value, param) {
			//千分為轉換為數字
			value=value.replace(/,/gi,'');
			if (value.length == param[0]){
				return true;
			} else {
				if (param && param.length >= 2) {
					$.fn.validatebox.defaults.rules.numberLengthEquals.message = param[1];
				}
				return false;
			}
		},   
		message: '長度輸入錯誤'  
	},
	/**
	 * 正整數(不包含零)，首位不為零
	 */
	positiveInt: {
		validator: function (value, param) {
			//千分為轉換為數字
    		value=value.replace(/,/gi,'');
			if (/^[1-9]*[1-9][0-9]*$/i.test(value)){
				return true;
			} else {
				if (param && param.length >= 1) {
					$.fn.validatebox.defaults.rules.positiveInt.message = param[0];
				}
				return false;
			}
		},   
		message: '請輸入數字'   
	},

    /**
     * 整数或小数
     */
    intOrFloat: {   
        validator: function (value, param) {   
        	 return /^\d+(\.\d+)?$/i.test(value);   
        },   
        message: '請輸入數字'   
    },
   
    /**
     * 英文
     * Evan
     */
    english : {
    	validator : function(value) {
    		return /^[a-zA-Z]+$/.test(value);
    	},
    		message : "請輸入英文"
    },
    /**
     * 英文和數字
     * Evan
     */
    numberOrEnglish: {
        validator: function (value) {
             return /^[a-zA-Z0-9]+$/i.test(value);
         },
         message: '請輸入英文和數字'
     },
     /**
      * 英文和數字
      */
     englishOrNumber: {
    	 validator: function (value, param) {
 			if (/^[a-zA-Z0-9]+$/i.test(value)){
 				return true;
 			} else {
 				if (param.length >= 1) {
 					$.fn.validatebox.defaults.rules.englishOrNumber.message = param[0];
 				}
 				return false;
 			}
 		},   
          message: '請輸入英文和數字'
      },
      /**
      * 非漢字
      */
      noChinese: {
     	 validator: function (value, param) {
     		var letter = 0;
 			var number = 0;
 			var character = 0;
 			var space = 0;
 			var a = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
 			var b = "0123456789";
 			var c = "~!@#$%^&*()_+`-=/*\\';:'\"\'<>,.?/";
 			var d = " ";
 			for (var i = 0; i < value.length; i++) {
 				var temp = value[i];
 				if (a.indexOf(temp) >= 0) {
 					letter++;
 				} else if (b.indexOf(temp) >= 0) {
 					number++;
 				} else if (c.indexOf(temp) >= 0) {
 					character++;
 				} else if (d.indexOf(temp) >= 0){
 					space++;
 				} else {
 					space++;
 				}
 			}
 			if ((letter > 0 || number > 0 || character > 0) && space == 0) {
 				return true;
 			} else {
 				if(param){
 					if (param.length >= 1) {
    					$.fn.validatebox.defaults.rules.noChinese.message = param[0];
    				}
 				}
				return false;
			}
  		},   
           message: '請輸入非漢字'
       },
  	 /**
  	  * 中文
  	  * Evan
  	  */
     CHS : {
    	 validator : function(value) {
    		 return /^[\u0391-\uFFE5]+$/.test(value);
    	 },
    	 message : "請輸入中文"
     },
     /**
  	  * 台灣行動電話
  	  * Evan
  	  */
     twMobile : {
    	 validator : function(value, param) {
    		 if (/^(09)\d{8}$/i.test($.trim(value))) {
 				return true;
 			} else {
 				if(param != undefined && param.length > 0){
 					$.fn.validatebox.defaults.rules.twMobile.message = param[0];
 					return false;
 				}
 			}
    		// return /^(09)\d{8}$/i.test($.trim(value)); 
    	 },
    	 message : "行動電話限輸入09開頭且長度為10碼的數字，請重新輸入"
     },
     /**
      * 密碼長度驗證
      * 長度應在設定中的M~N之間
      */
     pwdLength: {     
      	validator: function(value, param){
      		var min = param[0];
      		var max = param[1];
      	    var result = (value.length >= min && max >= value.length);
      	    if (!result) {
      	    	if (param.length >= 3) {
      	    		$.fn.validatebox.defaults.rules.pwdLength.message = '密碼長度限' + min + '~' + max + '之間，請重新輸入';
 				} else {
 					$.fn.validatebox.defaults.rules.pwdLength.message = '新密碼長度限' + min + '~' + max + '之間，請重新輸入';
 				}
      	    }
      	   return result;
      	},     
      	message: '新密碼長度限M~N之間，請重新輸入'    
      },
     /**
      * 密碼字符驗證 密碼限輸入英文及數字或英文及符號
      * Evan
      */
      pwdCharacter : {
          validator: function (value,param) {
     			var letter = 0;
     			var number = 0;
     			var character = 0;
     			var space = 0;
     			var a = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
     			var b = "0123456789";
     			var c = "~!@#$%^&*()_+`-=/*\\':'\"\'<>,.?/";
     			var d = " ";
     			for (var i = 0; i < value.length; i++) {
     				var temp = value[i];
     				if (a.indexOf(temp) >= 0) {
     					letter++;
     				} else if (b.indexOf(temp) >= 0) {
     					number++;
     				} else if (c.indexOf(temp) >= 0) {
     					character++;
     				} else if (d.indexOf(temp) >= 0){
     					space++;
     				} else {
     					space++;
     				}
     			}
     			if ((letter > 0) && (number > 0 || character > 0) && space == 0) {
     				return true;
     			} else {
     				if(param){
     					if (param.length >= 1) {
        					$.fn.validatebox.defaults.rules.pwdCharacter.message = param[0];
        				}
     				}
    				return false;
 				}
           },
           message: '新密碼限英數字或英文符號或英數符號，請重新輸入'
       },
      
      /**
       * 密碼字符驗證 密碼不可為鍵盤上的字母順序(asdf)
       * Evan
       */	
    pwdCharacterOrder : {
 		  validator: function (value,param) {
 			var pwd1 = "qwertyuiop";
 			var pwd2 = "asdfghjkl";
 			var pwd3 = "zxcvbnm";
 			var pwd4 = "poiuytrewq";
 			var pwd5 = "lkjhgfdsa";
 			var pwd6 = "mnbvcxz";
 			var pwd7 = "QWERTYUIOP";
 			var pwd8 = "ASDFGHJKL";
 			var pwd9 = "ZXCVBNM";
 			var pwd10 = "POIUYTREWQ";
 			var pwd11 = "LKJHGFDSA";
 			var pwd12 = "MNBVCXZ";
 			if (value.length >= 4) {
 				for (var i = 0; i <= value.length - 4; i++) {
 					var temp = value.substring(i, i + 4);
 					if (pwd1.indexOf(temp) >= 0 || pwd2.indexOf(temp) >= 0 || pwd3.indexOf(temp) >= 0 
 							|| pwd4.indexOf(temp) >= 0 || pwd5.indexOf(temp) >= 0 || pwd6.indexOf(temp) >= 0
 							|| pwd7.indexOf(temp) >= 0 || pwd8.indexOf(temp) >= 0 || pwd9.indexOf(temp) >= 0
 							|| pwd10.indexOf(temp) >= 0 || pwd11.indexOf(temp) >= 0 || pwd12.indexOf(temp) >= 0) {
 						if(param){
 							if (param.length >= 1) {
 	        					$.fn.validatebox.defaults.rules.pwdCharacterOrder.message = param[0];
 	        				}
 						}
 						return false;
 					}
 				}
 			}
 			return true;
 		},
 		message: '新密碼不可為鍵盤上的字母順序(asdf)，請重新輸入'
 	},
      /**
       * 密碼連續數字驗證 密碼密碼不可為連續的數字(1234)
       * Evan
       */
      pwdNumber : {
    	  validator: function (value,param) {
    		var pwd = "0123456789";
    		var pwd1 = "9876543210"
  			if (value.length >= 4) {
  				for (var i = 0; i <= value.length - 4; i++) {
  					var temp = value.substring(i, i + 4);
  					if (pwd.indexOf(temp) >= 0 || pwd1.indexOf(temp) >= 0) {
  						if(param){
  							if (param.length >= 1) {
  	        					$.fn.validatebox.defaults.rules.pwdNumber.message = param[0];
  	        				}
  						}
  						return false;
  					}
  				}
  			}
  			return true;
          },
          message: '新密碼不可為連續的數字，請重新輸入'
      },
      /**
       * 密碼重複字元 密碼不可使用重複的字元(不可重複三次)，請重新輸入
       * Evan
       */
    pwdCharRepeat : {
    	validator: function (value,param) {
    		var preChar;
    		var time = 0;
    		var currentChar;
    		for(var i = 0; i < value.length; i++) {
    			for(var j = 0; j < value.length; j++){
    				currentChar = value.charAt(i);
    				preChar = value.charAt(j);
    				if (currentChar == preChar) {
    					time ++;
    					if (time >= 3) {
    						if(param){
    							if (param.length >= 1) {
        	        				$.fn.validatebox.defaults.rules.pwdCharRepeat.message = param[0];
        	        			}
    						}
    						return false;
    					}
    				} 
    			}
    			time = 0;
    		}
    		return true;
        },
        message: '新密碼不可使用重複的字元(不可重複三次)，請重新輸入'
	},
      /**
       * 密碼與帳號相同或部分相同
       * Evan
       */
	pwdSameId : {
		validator: function (value, param) {
			var userId;
			if (param.length > 1 && param[1] == "value") {
				userId = param[0];
			} else {
			  userId = $("#" + param[0]).val();
			}
			//全都轉化為小寫
			var userId2 = userId.toLowerCase();
			var value1 = value.toLowerCase();
			var same = true;
			var valueLength = value.length;
			var userIdLength = userId.length;
			var userIdReverse2 = userId2.split("").reverse().join("");
			if (valueLength >= 3 && userIdLength >= 3) {
				var newValue;
				for (var j = 0; j < valueLength; j ++) {
					newValue = value1.substr(j,3);
					if (userIdReverse2.indexOf(newValue) >= 0 || userId2.indexOf(newValue) >= 0) {
						same = false;
    					break;
    				}
					if (j + 3 >= valueLength) {
						break;
					}
				}
			}
			if (param.length >= 1 && param[1] != "value") {
				$.fn.validatebox.defaults.rules.pwdSameId.message = param[1];
			}
			return same;
		},
		message: '新密碼不能與帳號相同或部分相同，請重新輸入'
	},
	/**
	 * 日期格式驗證
	 */
	date : {     
		validator: function(value, param){
			if(/^[0-9]{4}[\/][0-9]{2}[\/][0-9]{2}$/i.test($.trim(value))){
				var startDate = value.split("\/");
				if (startDate[0] < 1900) {
					$.fn.validatebox.defaults.rules.date.message = '日期不應小於1900/01/01';
					return false;
				}
				if (startDate[1] > 12 || startDate[1] < 1) {
					if(param != undefined && param.length > 0){
						$.fn.validatebox.defaults.rules.date.message = param[0];
					}
						return false;
				} 
				if (startDate[1] == 1 || startDate[1] == 3 || startDate[1] == 5 || startDate[1] == 7 || startDate[1] == 8 
							|| startDate[1] == 10 || startDate[1] == 12) {
					if (startDate[2] > 31 || startDate[2] < 1) {
						if(param != undefined && param.length > 0){
							$.fn.validatebox.defaults.rules.date.message = param[0];
						}
							return false;
					} 
				} else if (startDate[1] == 2) {
					if ((startDate[0]%4==0 && startDate[0]%100 != 0) || startDate[0]%400 == 0) {
						if (startDate[2] > 29 || startDate[2] < 1) {
							if(param != undefined && param.length > 0){
								$.fn.validatebox.defaults.rules.date.message = param[0];
							}
								return false;
						}
					} else {
						if (startDate[2] > 28 || startDate[2] < 1) {
							if(param != undefined && param.length > 0){
								$.fn.validatebox.defaults.rules.date.message = param[0];
							}
								return false;
						}
					}
				} else {
					if (startDate[2] > 30 || startDate[2] < 1) {
						if(param != undefined && param.length > 0){
							$.fn.validatebox.defaults.rules.date.message = param[0];
						}
							return false;
					} 
				}
				return true;
			} else {
				if(param != undefined && param.length > 0){
					$.fn.validatebox.defaults.rules.date.message = param[0];
				} else{
					$.fn.validatebox.defaults.rules.date.message = '日期格式限YYYY/MM/DD';
				}
				return false;
			}
		},     
		message: '日期格式限YYYY/MM/DD'    
	},
	/**
	 * 日期+時間格式驗證
	 */
	dateTime : {     
		validator: function(value, param){
			if(/^[0-9]{4}[\/][0-9]{2}[\/][0-9]{2} ([01]\d|2[0-3]):([0-5]\d)$$/i.test($.trim(value))){
				var stringTime = value.split(" ");
				var startDate = stringTime[0].split("\/");
				var startTime = stringTime[1].split(":");
				if (startDate[1] > 12 || startDate[1] < 1) {
					if(param != undefined && param.length > 0){
						$.fn.validatebox.defaults.rules.dateTime.message = param[0];
					}
						return false;
				} 
				if (startDate[1] == 1 || startDate[1] == 3 || startDate[1] == 5 || startDate[1] == 7 || startDate[1] == 8 
							|| startDate[1] == 10 || startDate[1] == 12) {
					if (startDate[2] > 31 || startDate[2] < 1) {
						if(param != undefined && param.length > 0){
							$.fn.validatebox.defaults.rules.dateTime.message = param[0];
						}
							return false;
					} 
				} else if (startDate[1] == 2) {
					if ((startDate[0]%4==0 && startDate[0]%100 != 0) || startDate[0]%400 == 0) {
						if (startDate[2] > 29 || startDate[2] < 1) {
							if(param != undefined && param.length > 0){
								$.fn.validatebox.defaults.rules.dateTime.message = param[0];
							}
								return false;
						}
					} else {
						if (startDate[2] > 28 || startDate[2] < 1) {
							if(param != undefined && param.length > 0){
								$.fn.validatebox.defaults.rules.dateTime.message = param[0];
							}
								return false;
						}
					}
				} else {
					if (startDate[2] > 30 || startDate[2] < 1) {
						if(param != undefined && param.length > 0){
							$.fn.validatebox.defaults.rules.dateTime.message = param[0];
						}
							return false;
					} 
				}
				if(startTime[0] > 24 || startTime[0] < 0) {
					if(param != undefined && param.length > 0){
						$.fn.validatebox.defaults.rules.dateTime.message = param[0];
					}
						return false;
				} else if(startTime[1] > 60 || startTime[1] < 0) {
					if(param != undefined && param.length > 0){
						$.fn.validatebox.defaults.rules.dateTime.message = param[0];
					}
						return false;
				}
				return true;
			} else {
				if(param != undefined && param.length > 0){
					$.fn.validatebox.defaults.rules.dateTime.message = param[0];
				}
				return false;
			}
		},     
		message: '提醒時間格式限YYYY/MM/DD HH:mm'    
	},
	/**
	 * 日期+時間格式驗證
	 */
	dateTimeValid : {     
		validator: function(value, param){
			if(/^[1-9]\d{3}[\/](0[1-9]|1[0-2])[\/](0[1-9]|[1-2][0-9]|3[0-1])\s+(20|21|22|23|[0-1]\d):[0-5]\d:[0-5]\d$/.test($.trim(value))){
				return true;
			} else {
				if(param != undefined && param.length > 0){
					$.fn.validatebox.defaults.rules.dateTimeValid.message = param[0];
				}
				return false;
			}
		},     
		message: '提醒時間格式限YYYY/MM/DD HH:mm:ss'    
	},
	/**
	 * 電子郵件
	 */
	email : {     
		validator: function(value, param){
			if (/^[a-zA-Z0-9_+.-]+\@([a-zA-Z0-9-]+\.)+[a-zA-Z0-9]{2,4}$/i.test($.trim(value))) {
				return true;
			} else {
				if(param != undefined && param.length > 0){
					$.fn.validatebox.defaults.rules.email.message = param[0];
					return false;
				}
			}
		},     
		message: 'EMAIL格式有誤'
	},
	/**
	 * 驗證多組Email地址，並用“；”區隔
	 */
	manyEmail : {     
		validator: function(value, param){
			//若輸入的值不為空
			if(value != ""){
				var str = value.split(";");
				var length = str.length;
				//判斷拆分后的值是否都是email格式
				for(i=0;i<length;i++){
					if(str[i] != ""){
						var result =  /^[a-zA-Z0-9_+.-]+\@([a-zA-Z0-9-]+\.)+[a-zA-Z0-9]{2,4}$/i.test($.trim(str[i]));
						if(result == false){
							if(param != undefined && param.length > 0){
								$.fn.validatebox.defaults.rules.manyEmail.message = param[0];
								return false;
							}
							return false;
						}
					}
				}
				return true;
			}
		},     
		message: '{0}格式有誤，多筆請用分號區隔，請重新輸入'
	},
	/**
	 * 驗證多組Email地址，並用“；”區隔
	 */
	manyMail : {     
		validator: function(value, param){
			//若輸入的值不為空
			if(value != ""){
				var str = value.split(";");
				var length = str.length;
				//判斷拆分后的值是否都是email格式
				for(i=0;i<length;i++){
					var flag = false;
					if(str[i] != ""){
						var mailList = param[0];
						var mail = mailList.split(",");
						for(var j=0;j<mail.length;j++) {
							if($.trim(mail[j].replace("[","").replace("]","")) == $.trim(str[i])) {
								flag = true;
							}
						}
						if(!flag){
							var result =  /^[a-zA-Z0-9_+.-]+\@([a-zA-Z0-9-]+\.)+[a-zA-Z0-9]{2,4}$/i.test($.trim(str[i]));
							if(result == false){
								return false;
							}
						}
					}
				}
				return true;
			}
		},     
		message: '收件者格式有誤，請重新輸入'
	},
	/**
	 * 選擇框驗證.
	 * param[0]為忽略內容,
	 */
	ignore : {
		validator : function (value, param) {
			return value != param[0];
		},
		message: '該項為必選項' 
	},
	/**
	 * grid列的驗證, 需要用到其他列的值
	 * param[0]:驗證的url地址
	 * param[1]:value傳入後臺的鍵值,例如使用者驗證傳入'userId'
	 * param[2]:其他列的filed值
	 * param[3]:驗證的消息,如果驗證失敗,顯示給用戶的內容. 例如帳號驗證,傳入帳號重複. 可以不傳.
	 * 驗證不過,顯示的消息: 優先順序為:
	 * 1.param[4]
	 * 2.服務端返回的data.msg
	 * 3.設置的invalidMessage
	 * 4.默認的msg
	 * @author Evan
	 */
	gridRemote : {
		validator : function(value, param) {
			var result = false;
			var postData = {};
			postData[param[1]] = value;
			//查找其他的filed值
			var fieldtr = $(this).parents("tr.datagrid-row");
			if (fieldtr) {
				var fieldTd = fieldtr.find("td[field='" + param[2] + "']");
				if (fieldTd && fieldTd.is(":hidden")) {
					var fieldValue = fieldTd.text();
					postData[param[2]] = fieldValue;
				} else {
					return false;
				}
			} else {
				return false;
			}
			$.ajax({  
				type: "POST",
				async:false,  
				url:param[0],
				dataType:"json",  
				data:postData,
				async:false,  
				success: function(data){ 
					result=data.success; 
					if (!result) {
						if (param.length == 4) {
							$.fn.validatebox.defaults.rules.gridRemote.message = param[3];
						} else {
							if (data.msg) {
								$.fn.validatebox.defaults.rules.gridRemote.message = data.msg;
							}
						}
					}
				},
				error: function() {
					if (param.length == 4) {
						$.fn.validatebox.defaults.rules.gridRemote.message = param[3];
					}
				}
			});		
			return result;
		}, 
		message: '驗證失敗'
	},
	/**
	 * 後端驗證
	 * 3個參數
	 * param[0]:驗證的url地址
	 * param[1]:傳入後臺的鍵值,例如使用者驗證傳入'userId'
	 * param[2]:驗證的消息,如果驗證失敗,顯示給用戶的內容. 例如帳號驗證,傳入帳號重複. 可以不傳.
	 * 驗證不過,顯示的消息: 優先順序為:
	 * 1.param[2]
	 * 2.服務端返回的data.msg
	 * 3.設置的invalidMessage
	 * 4.默認的msg
	 * @author Evan
	 */
	remote : {     
		validator: function(value, param){     
			var repeat = false;
			var postData = {};
			postData[param[1]] = value;
			$.ajax({  
				type: "POST",
				async:false,  
				url:param[0],
				dataType:"json",  
				data:postData,
				async:false,  
				success: function(data){ 
					repeat=data.success; 
					if (!repeat) {
						if (param.length == 3) {
							$.fn.validatebox.defaults.rules.remote.message = param[2];
						} else {
							if (data.msg) {
								$.fn.validatebox.defaults.rules.remote.message = data.msg;
							}
						}
					}
				},
				error: function() {
					if (param.length == 3) {
						$.fn.validatebox.defaults.rules.remote.message = param[2];
					}
				}
			});                      
			return repeat;
		}, 
		message: '驗證失敗'
	},
	
	/**
	 * 验证姓名，只能是中文或英文--HaimingWang
	 */
	contact : {
		validator : function(value,param) {
			return /^[\u0391-\uFFE5]+$/i.test(value)|/^[A-Za-z]+$/i.test(value);
		},
		message : '{0}輸入有誤，請重新輸入'
	},
	/**
	 * 只能為0-9的數字--HaimingWang
	 */ 
	integerOnly : {
		validator : function(value) {
			return /^[+]?[0-9]+\d*$/i.test(value);
		},
		message : '格式不正確，只能為0-9的數字'
	},
	/**
	 * 驗證cafe:droplisttag必填項不能為請選擇--HaimingWang
	 */
	requiredDropList: {
		validator : function(value, param) {
			var defaultValue = '請選擇';
			if (param != undefined && param.length > 0) {
				defaultValue = param[0];
			}
			if (value.indexOf(defaultValue) != -1) {
				return false;
			}else {
				return true;
			}
		},
		message : '該項為必選項'
	},
	/**
	 * 結束時間不能小於等於開始時間
	 * @author jasonzhou
	 */
	compareTimeBigSize: {
		validator: function (value, param) {
			var openHour = $(param[0]).timespinner('getHours');
			var openMinute = $(param[0]).timespinner('getMinutes');
			var closeTime = value.split(":");
			if (openHour==23 && closeTime[0]==00){
				return true;
			} else {
				if (param.length < 3) {
					if (closeTime[0]<openHour){
						$.fn.validatebox.defaults.rules.compareTimeSize.message = param[1];
						return false;
					} else if (closeTime[0]==openHour){
						if (closeTime[1]<=openMinute){
							$.fn.validatebox.defaults.rules.compareTimeSize.message = param[1];
							return false;
						} else {
							return true;
						}
					} else {
						return true;
					}
				} else {
					if (closeTime[0]>openHour){
						$.fn.validatebox.defaults.rules.compareTimeSize.message = param[1];
						return false;
					} else if (closeTime[0]==openHour){
						if (closeTime[1]>=openMinute){
							$.fn.validatebox.defaults.rules.compareTimeSize.message = param[1];
							return false;
						} else {
							return true;
						}
					} else {
						return true;
					}
				}
				
			}
		},
        message: '營業時間起不可大於營業時間迄'
	},
	/**
	 * 結束時間不能小於等於開始時間
	 * @author jasonzhou
	 */
	compareTimeSize: {
		validator: function (value, param) {
			var openHour = $(param[0]).timespinner('getHours');
			var openMinute = $(param[0]).timespinner('getMinutes');
			var closeTime = value.split(":");
			if (param.length < 3) {
				if (closeTime[0]==00 && closeTime[1]==00) {
					closeTime[0] = 24;
				}
			} else {
				if (openHour==00 && openMinute==00) {
					openHour = 24;
				}
			}
			/*if (openHour==00 && closeTime[0]==00){
				return true;
			} else {*/
				if (param.length < 4) {
					if (closeTime[0]<openHour){
						$.fn.validatebox.defaults.rules.compareTimeSize.message = param[1];
						return false;
					} else if (closeTime[0]==openHour){
						if (closeTime[1]<=openMinute){
							$.fn.validatebox.defaults.rules.compareTimeSize.message = param[1];
							return false;
						} else {
							return true;
						}
					} else {
						return true;
					}
				} else {
					if (closeTime[0]>openHour){
						$.fn.validatebox.defaults.rules.compareTimeSize.message = param[1];
						return false;
					} else if (closeTime[0]==openHour){
						if (closeTime[1]>=openMinute){
							$.fn.validatebox.defaults.rules.compareTimeSize.message = param[1];
							return false;
						} else {
							return true;
						}
					} else {
						return true;
					}
				}
				
			/*}*/
		},
        message: '營業時間起不可大於營業時間迄'
	},
	/**
	 * 結束時間不能小於等於開始時間
	 * @author echomou   
	 * update by hermanwang 2017/05/31 (從表頭頁面取出)
	 */
	compareTimeSizeInMerchantHeader: {
  		validator: function (value, param) {
  			var openHour = $(param[0]).timespinner('getHours');
  			var openMinute = $(param[0]).timespinner('getMinutes');
  			var closeTime = value.split(":");
  			if (openHour==23 && closeTime[0]==00){
  				return true;
  				
  			} else {
  				if (closeTime[0]<openHour){
  					if (param.length == 2) {
  						$.fn.validatebox.defaults.rules.compareTimeSize.message = param[1];
  					}
  					return false;
  				} else if (closeTime[0]==openHour){
  					if (closeTime[1]<openMinute){
  						if (param.length == 2) {
  							$.fn.validatebox.defaults.rules.compareTimeSize.message = param[1];
  						}
  						return false;
  					} else {
  						return true;
  					}
  				} else {
  					return true;
  				}
  			}
  		},
          message: '營業時間起不可大於營業時間迄'
  	},
	/**
	 * 時間驗證
	 */
	compareMerchantTime : {
		validator: function(value, param){
			if (/^([0-1][0-9]|[2][0-3]):([0-5][0-9])$/i.test($.trim(value))) {
				return true;
			} else {
				if(param != undefined && param.length > 0){
					$.fn.validatebox.defaults.rules.compareMerchantTime.message = param[0];
				}
				return false;
			} 
		},     
		message: '營業時間起迄限輸入HH:mm'
	},
	/**
	 * 驗證時間格式，0.5小時為單位
	 */
	compareTime : {
		validator: function(value, param){
			if (/^([0-1][0-9]|[2][0-3]):([0-5][0-9])$/i.test($.trim(value))) {
				var minutes = value.split(":")[1];
				if (minutes == 0 || minutes == 30) {
					return true;
				} else {
					if(param != undefined && param.length > 0){
						$.fn.validatebox.defaults.rules.compareTime.message = param[0];
					}
					return false;
				}
				return true;
			} else {
				if(param != undefined && param.length > 0){
					$.fn.validatebox.defaults.rules.compareTime.message = param[0];
				}
				return false;
			} 
		},     
		message: '營業時間起迄限輸入HH:mm'
	},
	/**
	 * 日期(迄)不能小於或等於日期(起)
	 * @author jasonzhou
	 */
	compareDateSize: {
        validator:function(value,param){
        	var data = $(param[0]).datebox("getValue");
        		if(value>data){
	        		return true;
	        	}else{
	        		if (param.length == 2) {
						$.fn.validatebox.defaults.rules.compareDateSize.message = param[1];
					}
	        		return false;
	        	}	
        },
        message:'日期(迄)不能小於或等於日期(起)'
    },
	/**
	 * 日期(迄)不能小於日期(起)
	 * @author starwang
	 */
	compareDateStartEnd: {
        validator:function(value,param){
        	var data = $(param[0]).datebox("getValue");
        		if(value>=data){
	        		return true;
	        	}else{
	        		if (param.length == 2) {
						$.fn.validatebox.defaults.rules.compareDateStartEnd.message = param[1];
					}
	        		return false;
	        	}	
        },
        message:'日期(迄)不能小於日期(起)'
    },
    /**
	 * 日期(迄)不能小於日期(起)
	 * @author echomou
	 */
	compareDate: {
		validator:function(value,param){
        	var data = $(param[0]).datebox("getValue");
        		if(value>=data){
	        		return true;
	        	}else{
	        		return false;
	        	}	
        },
        message:'轉入日期應大於轉出日期'
    },
    
    /**
	 * 如果param值為1時，輸入值只能為8的整數倍
	 * @author Amanda Wang
	 */
    multipleOfEight : {
		validator : function(value, param) {
			if ($(param[0]).val() == "1") {
				if(value%8 != 0){
					return  false;
				} 
				return true;
			} 
			return true;
		},
		message : '格式不正確，只能為8的整數倍'
	},
	 /**
	 * 如果param值為Y時，輸入值只能為24的整數倍
	 * @author Criss Zhang
	 */
	multipleOfTwentyFour : {
		validator : function(value, param) {
			var isThatDay = $("#" + param[0]).combobox('getValue');
			if (isThatDay == "Y") {
				if(value%24 != 0){
					if (param && param.length >= 1) {
						$.fn.validatebox.defaults.rules.multipleOfTwentyFour.message = param[1];
					}
					return false;
				} else {
					return true;
				}
			} else {
				return true;
			}
		},
		message : '格式不正確，只能為24的整數倍'
	},
	 /**
	 * 警示不能大於其對應時效
	 * @author Amanda Wang
	 */
	beforeHour : {
		validator : function(value, param) {
			var hourValue = $("#" + param[0]).textbox('getValue');
			if (parseInt(hourValue, 10) < parseInt(value, 10)){
				if (param.length >= 1) {
					$.fn.validatebox.defaults.rules.beforeHour.message = param[1];
				}
				return false;
			} else{
				return true;
			}
		},
		message : '格式不正確，不能大於其對應時效'
	},
	 /**
	 * 合約編號不能與被複製合約編號相同
	 * @author Amanda Wang
	 */
	isRepeat : {
		validator : function(value, param) {
			if (value == $(param[0]).val()) {
				return false;
			} else {
				return true;
			}
		},
		message : '格式不正確，不能與被複製合約編號相同'
	},
	validDate : {     
		validator: function(value){
			if(value.length != 10){
				return false;
			}else{
				var y = value.substring(0,4);
				var sep1 = value[4];
				var m = value.substring(5,7);
				var sep2 = value[7];
				var d = value.substring(8, value.length);
				if(sep1 != '/' || sep2 != '/'){
					return false;
				}
				if(parseInt(m) > 12){
					return false;
				}
				if (m == '00') {
					return false;
				}
				if(parseInt(d) > 31){
					return false;
				}
				if (d == '00') {
					return false;
				}
				if(m == '04' || m == '06'|| m == '09'|| m == '11'){
					if(parseInt(d) > 30){
						return false;
					}
				}
				if(parseFloat(y) % 4 == 0){
					if(m == '02'){
						if(parseInt(d) > 29){
							return false;
						}
					}
				}else{
					if(m == '02'){
						if(parseInt(d) > 28){
							return false;
						}
					}
				}
			}
			return true;
		},     
		message: '日期格式錯誤,如2012/01/01'    
	},
	/**
	 * 驗證日期格式（yyyy/MM）
	 */
	validDateYearMonth : {     
		validator: function(value){
			if(value.length != 7){
				return false;
			} else {
				var y = value.substring(0,4);
				var sep1 = value[4];
				var m = value.substring(5,7);
				if(sep1 != '/'){
					return false;
				}
				if (/^0[1-9]|1[0-2]$/i.test(m)){
					if(parseInt(m,10) > 12 || parseInt(m,10) <= 0){
						return false;
					}
				} else {
					return false;
				}
				if (/^\d{4}$/i.test(y)){
					return true;
				} else {
					return false;
				}
			}
			return true;
		},     
		message: '日期格式限YYYY/MM'    
	},
	//驗證日期格式（yyyy/MM）
//	myDate : {
//		validator: function(value){
//			if(value.length != 7){
//				return false;
//			}else{
//				var y = value.substring(0,4);
//				var sep1 = value[4];
//				var m = value.substring(5,7);
//				if(sep1 != '/'){
//					return false;
//				}
//				if(parseInt(m,10) > 12 || parseInt(m,10) <= 0){
//					return false;
//				}
//			}
//			return true;
//		},     
//		message: '日期格式限YYYY/MM'    
//	},
    /**
     * 驗證安全庫存不能大於數量
     * @author KevinShen
     * param[0] 數據列表
     * param[1] 正在編輯的行數
     * param[2] 列的filed的值
     */
	comparativeSize: {
		validator: function(value, param){
			var result = true;
			if (param[1] != undefined){
				var ed = $(param[0]).datagrid('getEditor', {
					index : param[1],
					field : param[2]
				});
				var amount = $(ed.target).textbox('getValue');
				if (param.length > 4 ) {
					if ((amount != "" && amount != null) && parseInt(amount) > parseInt(value)){
						if (param.length == 5) {
							$.fn.validatebox.defaults.rules.comparativeSize.message = param[4];
						}
						result = false;
					} else {
						if (amount == "" || amount == null) {
							$.fn.validatebox.defaults.rules.comparativeSize.message = param[4];
							result = false;
						}
					}
				} else {
					if ((amount != "" && amount != null) && parseInt(amount) < parseInt(value)){
						if (param.length == 4) {
							$.fn.validatebox.defaults.rules.comparativeSize.message = param[3];
						}
						result = false;
					} else {
						if (amount == "" || amount == null) {
							$.fn.validatebox.defaults.rules.comparativeSize.message = param[3];
							result = false;
						}
					}
				}
			}
			return result;
		},
		message: '數量應大於安全庫存，請重新輸入'
	},
	
	/**
     * 驗證數字代大小
     */
	compareNumberSize: {
		validator: function(value, param){
			var data = $(param[0]).textbox("getValue");
			if (data == '' || data == null) {
				return true;
			}
    		if(parseInt(value) >= parseInt(data)){
        		return true;
        	}else{
        		if (param.length == 2) {
					$.fn.validatebox.defaults.rules.compareNumberSize.message = param[1];
				}
        		return false;
        	}
		},
		message: 'DTID起不可大於DTID迄'
	},
	/**
	 * 驗證輸入的時間是否大於當前時間
	 */
	 compareDateWithNowDate:{
	 	validator: function(value, param){
			var nowDate =  new Date();
			nowDate = formaterTimeStampToyyyyMMDD(nowDate.getTime());
			if (value >= nowDate) {
				return true;
			} else {
				return false;
			}
		},
		message: '延期日期不可小於當前日期'
	 },
	 /**
		 * 日期(迄)不能小於日期(起)--與當前日期比較
		 * @author amandawang
		 */
	compareToDate : {
        validator : function(value){
        	date = new Date();
        	var H = date.getHours(); //获取小时
        	var M = date.getMinutes(); //获取分钟
        	var S = date.getSeconds();//获取秒
        	var MS = date.getMilliseconds();//获取毫秒
        	var milliSeconds = H * 3600 * 1000 + M * 60 * 1000 + S * 1000 + MS;
        	var val = Date.parse(value);
        	if(val >= date.getTime() - milliSeconds){
	        	return true;
	        }else{
	        	return false;
	        }	
        },
         message :'借用日期起不可大於日期迄'
    },
	//驗證是否為英文數字和換行
	numberOrEnglishOrEnter : {
        validator : function (value) {
             return /^[a-zA-Z0-9\r\n]+$/i.test(value);
         },
         message : '請輸入數字英文以換行鍵間隔'
     }, 
     /**
      * 驗證帳號滿足英數符號
      * crisszhang
      */
     numOrEngOrChar : {
          validator: function (value,param) {
     			var letter = 0;
     			var number = 0;
     			var character = 0;
     			var space = 0;
     			var a = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
     			var b = "0123456789";
     			var c = "~!@#$%^&*()_+`-=/*\\':'\"\'<>,.?/";
     			var d = " ";
     			for (var i = 0; i < value.length; i++) {
     				var temp = value[i];
     				if (a.indexOf(temp) >= 0) {
     					letter++;
     				} else if (b.indexOf(temp) >= 0) {
     					number++;
     				} else if (c.indexOf(temp) >= 0) {
     					character++;
     				} else if (d.indexOf(temp) >= 0){
     					space++;
     				} else {
     					space++;
     				}
     			}
     			if ((letter > 0 || number > 0 || character > 0) && space == 0) {
    				return true;
    			} else {
    				if(param){
     					if (param.length >= 1) {
        					$.fn.validatebox.defaults.rules.numOrEngOrChar.message = param[0];
        				}
     				}
    				return false;
    			}
           },
           message: '帳號限4~20碼的英數符號，請重新輸入'
       },
       /**
        * 設備借用作業－核檢設備序號是否輸入正確
        */
       checkSerialNumber : {
    	   validator: function(value, param){ 
    		   var result = true;
    		   if (value != "" && value != null) {
    			   var rows = $("#continueAssetTable3").datagrid("getRows");
    			   for (var i = 0; i < rows.length; i++) {
    				   var serialNumber = $("#serialNumber"+(i+1)).textbox("getValue");
    				   if ((serialNumber == value) && (param[1] != (i+1))) {
    					   $.fn.validatebox.defaults.rules.checkSerialNumber.message = "設備序號"+value+"重復";
    					   return false;
    				   }
    			   }
    			   $.ajax({ 
    				   type: "POST",async:false, 
    				   url:'/cyber-iatoms-web/assetBorrowProcess.do?actionId=checkSerialNumber', 
    				   dataType:"json", 
    				   data:{"serialNumber" : value,
    					   	 "assetTypeId" : param[0]
    				   },
    				   async:false, 
    				   success: function(data){
    					   result = data.success;
    					   if (!result) {
    						   $.fn.validatebox.defaults.rules.checkSerialNumber.message = data.msg;
    					   }
    				   } 
    			   });
    		   }
    		   return result; 
    	   }, 
    	   message: "" 
       },
       
       checkBorrowEndDate : {
    	   validator: function(value, param){
    		   if (value != "" && value != null) {
    			   var borrowEndDate = param[0];
    			   var myDate = new Date(borrowEndDate);
    			   myDate.setMonth(myDate.getMonth() + 6);
    			   //$("#borrowEndDate").datebox("setValue", myDate);
    			   if (myDate < new Date(value)) {
    				   $.fn.validatebox.defaults.rules.checkBorrowEndDate.message = "借用迄日可不超過"+formaterTimeStampToyyyyMMDD(myDate);
    				   return false;
    			   }
    		   }
    		   return true;
    	   },
    	   message: "" 
       },
});

//***************easyui 格式化轉換**********************//
/**
 * dataGrid boolean數據修改
 */
function formatBoolean(val,row){    
    if (val){    
        return 'Y';
    } else {    
        return 'N';    
    }    
}
function formatCapacity(val, row){
	formatCapacity(val,0,0);
}

/**
 * grid checkbox 列
 * @param value : 當前field值
 * @param row : 當前行
 * @param index : 行號
 * @returns {String} : checkbox
 * Evan
 */
function formatterCheckbox(value,row,index){
	if (value == true || value == "1" || value == "true") {
		return '<input type="checkbox" checked="checked" disabled="disabled">';
	} else {
		return '<input type="checkbox" disabled="disabled">';
	}
}

/**
 * 格式化取值为'Y'和'N',加載完成後显示是否
 * @author Criss Zhang
 * @param row 本行
 */
function fomatterYesOrNo(val, row){
	if (val == 'Y') {
		return "是";
	} else if (val == 'N'){
		return "否";
	} else {
		return "";
	}
}
/**
 * 格式化交易參數的值
 * @param val 值
 * @param row 本行
 */
function formatTransactionShow(val, row){
	if (val == 'Y') {
		return 'V';
	} else if(val == 'N'){
		return '';
	} else {
		return val;
	}
}

/**
 * 格式化有無
 * @param val 值
 * @param row 本行
 */
function formatHaveOrNot(val, row){
	if (val == 'Y') {
		return "有";
	} else if(val == 'N'){
		return "無";
	} else {
		return val;
	}
}

/**
 * 設置千分位
 * @param num : 為要設置的數字
 * @param scale : 為要保留的小數位
 * @param minFractionDigits : 為最小小數位, 當小數最後為0時, 起作用
*/
function formatCapacity(num,scale, minFractionDigits){
	/*if(num) {
		if(scale == 0) {
			num = num + "";
		} else {
			num = num.toFixed(scale) +"";
		}
		var re=/(-?\d+)(\d{3})/;
		while(re.test(num)){
			num=num.replace(re,"$1,$2");
		}
		var x = num.split('.');
		if (x.length > 1) {
			x[1] = x[1].replace(",","");
			if (minFractionDigits < scale) {
				if (x[1].length > minFractionDigits) {
					for (var i = x[1].length - 1; i >= minFractionDigits; i --) {
						if (x[1].charAt(i) == "0") {
							x[1] = x[1].substr(0, i);
						} else {
							break;
						}
					}
				}
			}
			if (x[1].length > 0) {
				num = x.join(".");
			} else {
				num = x[0];
			}
		}
	}
	//num = x.join(".");
	return num;*/
	if(num) {
		if(scale == 0) {
			num = num + "";
		} else {
			num = parseFloat(num).toFixed(scale) +"";
		}
		var re=/(-?\d+)(\d{3})/;
		while(re.test(num)){
			num=num.replace(re,"$1,$2");
		}
		var x = num.split('.');
		if (x.length > 1) {
			x[1] = x[1].replace(",","");
			if (minFractionDigits < scale) {
				if (x[1].length > minFractionDigits) {
					for (var i = x[1].length - 1; i >= minFractionDigits; i --) {
						if (x[1].charAt(i) == "0") {
							x[1] = x[1].substr(0, i);
						} else {
							break;
						}
					}
				}
			}
			if (x[1].length > 0) {
				num = x.join(".");
			} else {
				num = x[0];
			}
		}
	}
	//num = x.join(".");
	return num;
}

/**
 * 將long類型的時間轉為 YYYY/MM/DD
 * @author jasonzhou 
 * @param 	time long類型的時間
 * @returns {String} YYYY/MM/DD 格式時間
 */
function formatLongToDate(time){
	var result = "";
	if(time != null && time != ""){
		var date = new Date(time);
		var y = date.getFullYear();
		var m = date.getMonth()+1;
			m =  parseFloat(m) >= 10 ? m : ("0" + m);
		var d = date.getDate();
			d = parseFloat(d)>=10  ? d : ("0" + d );
		result = (y+"/"+ m +"/"+ d);
	}
	return result;
}

/**
 * 格式化LOGO字段顯示
 * @author Criss Zhang
 * @param row 本行
 */
function fomatterLogoStyle(val, row){
	if (val == 'ONLY_LOGO') {
		return "僅LOGO";
	} else if (val == 'ONLY_MERCHANT_HEADER'){
		return "僅表頭";
	} else if (val == 'LOGO_AND_MERCHANT_HEADER'){
		return "LOGO+表頭";
	} else {
		return "";
	}
}
/**
 * 格式化換行
 * @author Criss Zhang
 * @param row 本行
 */
function wrapFormatter(value,row,index){
	if(!isEmpty(value)){
		return value.replaceAll('\n','<br>');
	}
}
/*
 * 格式化ecr連線
 */
function ecrConnectionFormatter(value,row,index){
	if (value == 'haveEcrLine') {
		return "V";
	} else {
		return "";
	}
}
//***************easyui 通用方法************************//
//獲得當前Grid的頁碼
//gridId:dataGrid的ID
function getGridCurrentPagerIndex(gridId) {
	var options = $("#" + gridId).datagrid("options");
	var currentPage = options.pageNumber;
	if (currentPage < 1) {
		currentPage = 1;
	}
	return currentPage;
}
//獲得當前Grid的行數
function getGridRowsCount(gridId) {
	var rows = $("#" + gridId).datagrid("getRows");
	return rows.length;
}
//計算delete方法執行後頁碼
function calDeletePagerIndex(gridId) {	
	var pageIndex = getGridCurrentPagerIndex(gridId);
	if (pageIndex > 1) {
		var rowCount =  getGridRowsCount(gridId);
		if (rowCount == 1) {
			//只有一行, 返回pageindex-1
			pageIndex --;
		}
	}
	return pageIndex;
}
/**
 * 扩展insert
 * Evan
 */
Array.prototype.insert = function (index, item) { 
	this.splice(index, 0, item); 
};
/**
 * 初始化select， 增加请选择选项
 * @param data：select 列表
 * @param defaultItem：默認選項，傳入默認字串，可忽略， 不傳，默認“請選擇”
 * @returns：select 列表
 * Evan
 */
function initSelect(data, defaultItem, selected) {
	if (data == null || !data.length) {
		data = [];
	}
	var defaultValue = new Object();
	defaultValue.value = "";
	if (defaultItem && defaultItem != "") {
		defaultValue.name = defaultItem;
	} else {
		defaultValue.name = "請選擇";
	}
	//"selected":true
	if (selected) {
		defaultValue.selected = true;
	}
	data.insert(0,defaultValue);
	return data;
}
/**
 * 初始化select， 增加请选择选项
 * @param data：select 列表
 * @param defaultItem：默認選項，傳入默認字串，可忽略， 不傳，默認“請選擇”
 * @returns：select 列表
 * crisszhang
 */
function initSelectMultiple(data, defaultItem, selected) {
	if (data == null || !data.length) {
		data = [];
	}
	var defaultValue = new Object();
	defaultValue.value = "";
	if (defaultItem && defaultItem != "") {
		defaultValue.name = defaultItem;
	} else {
		defaultValue.name = "請選擇(複選)";
	}
	//"selected":true
	if (selected) {
		defaultValue.selected = true;
	}
	data.insert(0,defaultValue);
	return data;
}
/**
 * 將long類型的時間轉為 YYYY/MM/DD hh:mm:ss
 * @author hungli 
 * @param 	time long類型的時間
 * @returns {String} YYYY/MM/DD hh:mm:ss格式時間
 */
function formatToTimeStamp(time){
	var returnTime = "";
	if (time != ""  && time != null) {
		var date = new Date(time);
		var y = date.getFullYear();
		var m = date.getMonth()+1;
			m =  parseFloat(m) >= 10 ? m : ("0" + m);
			
		var d = date.getDate();
			d = parseFloat(d)>=10  ? d : ("0" + d );
		var h = date.getHours();
			h = parseFloat(h)>= 10 ? h : ("0" + h);
		var mm = date.getMinutes();
			mm = parseFloat(mm) >= 10 ? mm : ("0" + mm );
		var s = date.getSeconds();
			 s = (parseFloat( s) >= 10 ? s : ("0"+ s ));
		returnTime = (y+"/"+ m +"/"+ d + " " + h + ":" + mm + ":"+s);
	}
	return returnTime;
}
/*
*將long類型的時間轉為 YYYY/MM/DD hh:mm:ss
*處理顯示該消息空格被截斷
*/
function transToTimeStamp(time){
	var returnTime = "";
	if (time != ""  && time != null) {
		var date = new Date(time);
		var y = date.getFullYear();
		var m = date.getMonth()+1;
			m =  parseFloat(m) >= 10 ? m : ("0" + m);
			
		var d = date.getDate();
			d = parseFloat(d)>=10  ? d : ("0" + d );
		var h = date.getHours();
			h = parseFloat(h)>= 10 ? h : ("0" + h);
		var mm = date.getMinutes();
			mm = parseFloat(mm) >= 10 ? mm : ("0" + mm );
		var s = date.getSeconds();
			 s = (parseFloat( s) >= 10 ? s : ("0"+ s ));
		returnTime = (y+"/"+ m +"/"+ d + "&nbsp;" + h + ":" + mm + ":"+s);
	}
	return returnTime;
}
/**
 * 將long類型的時間轉為 YYYY/MM/DD hh:mm
 * @author hungli 
 * @param 	time long類型的時間
 * @returns {String} YYYY/MM/DD hh:mm格式時間
 */
function formatToTimeStampIgnoreSecond(time){
	var returnTime = "";
	if (time != ""  && time != null) {
		var date = new Date(time);
		var y = date.getFullYear();
		var m = date.getMonth()+1;
			m =  parseFloat(m) >= 10 ? m : ("0" + m);
			
		var d = date.getDate();
			d = parseFloat(d)>=10  ? d : ("0" + d );
		var h = date.getHours();
			h = parseFloat(h)>= 10 ? h : ("0" + h);
		var mm = date.getMinutes();
			mm = parseFloat(mm) >= 10 ? mm : ("0" + mm );
		var s = date.getSeconds();
			 s = (parseFloat( s) >= 10 ? s : ("0"+ s ));
		returnTime = (y+"/"+ m +"/"+ d + " " + h + ":" + mm);
	}
	return returnTime;
}
/**
 * 將long類型的時間轉為 yyyy/MM/DD
 * @author allenchen 
 * @param 	time long類型的時間
 * @returns {String} yyyy/MM/DD 格式時間
 */
function formaterTimeStampToyyyyMMDD(time){
	var result = "";
	if(time != null && time != "" && time != "null"){
		var date = new Date(time);
		//年
		var y = date.getFullYear();
		//月
		var m = date.getMonth()+1;
		m =  parseFloat(m) >= 10 ? m : ("0" + m);
		//日
		var d = date.getDate();
		d = parseFloat(d)>=10  ? d : ("0" + d );
		result = (y+"/"+ m +"/"+ d)
	}
	return result;
}
/**
 * 將long類型的時間轉為 yyyy/MM
 * @author ElvaHe
 * @param 	time long類型的時間
 * @returns {String} yyyy/MM 格式時間
 */
function formaterTimeStampToyyyyMM(time){
	var result = "";
	if(time != null && time != "" && time != "null"){
		var date = new Date(time);
		//年
		var y = date.getFullYear();
		//月
		var m = date.getMonth()+1;
		m =  parseFloat(m) >= 10 ? m : ("0" + m);
		result = (y+"/"+ m)
	}
	return result;
}
/**
 * 日曆控件僅顯示年和月
 * @author Elvahe
 */
function createMonthDataBox(id){
	$('#'+id).datebox({
		//當節點的值有改變時
		onChange:function(){
			//獲取修改后的值
			var updatedDate = $("#"+id).datebox('getValue');
			//當有修改后的值時先隱藏之前的日曆面板，再調用顯示日曆面板的方法
			if (updatedDate != null) {
				$("#"+id).datebox('hidePanel');
				showDatePanel(id, span, p);
			}
		},
		//當下拉面板顯示的時候觸發
		onShowPanel:function(){
			//調用顯示日曆面板的方法
			showDatePanel(id, span, p);
		},
		//格式化日期
		formatter: function(date){
			if (date == null){
				date = new Date();
			}
			var y = date.getFullYear();
			var m = date.getMonth()+1;
			//當月份小於10時給月份前加0
			m = parseFloat(m) >= 10 ? m : ("0" + m);
		 	return y+'/'+ m;
		 },
		 //解析日期
		parser: function(date){
			if(!date){
				return new Date();
			}
			var ss = date.split('/');
			var y = parseInt(ss[0],10);
			var m = parseInt(ss[1],10);
			if (!isNaN(y) && !isNaN(m)){
				return new Date(y,m-1);
			};
		}
	});
	//日期选择对象
	var p = $("#"+id).datebox('panel');
	//显示月份层的触发控件
	var span = p.find('span.calendar-text');
	initMonthDateBox();
}
/**
 *顯示日曆面板的方法（年月）
 *id:節點ID
 *span：顯示月份層的觸發控件
 *P:日期
 *@author Elvahe
 */
function showDatePanel(id, span, p){
	//触发click事件弹出月份层
	span.trigger('click');
	//延时触发获取月份对象，因为上面的事件触发和对象生成有时间间隔
	setTimeout(function () {
		var	tdsObj = p.find('div.calendar-menu-month-inner td');
		tdsObj.each(function(index,obj1){
			$(obj1).html($(obj1).attr('abbr') + '月');
		});
		tdsObj.one("click",function (e) {
			//禁止冒泡执行easyui给月份绑定的事件
			e.stopPropagation();
			//得到年份
			var year = /\d{4}/.exec(span.html())[0];
			//月份
			var month = parseInt($(this).attr('abbr'), 10);
			//隐藏日期对象
			$("#"+id).datebox('hidePanel');
			//设置日期的值
			$("#"+id).datebox('setValue', year + '/' + month);
			tdsObj.unbind();
		});
		//$("#"+id).datebox('showPanel');
	}, 0)
}
/**
 * 用於下載，ajax方式不能實現下載功能，創建一個form，並放入參數，進行submit后remove();
 * @author hungli
 * @param url :url地址
 * @param data ：參數(格式為 a=a&b=b&c=c)
 * @param method ：提交方式post|get
 */
function createSubmitForm(url, data, method){// 获得url和data
    if( url && data ){ 
        // data 是 string 或者 array/object
        data = typeof data == 'string' ? data : jQuery.param(data);// 把参数组装成 form的  input
        var inputs = '';
        jQuery.each(data.split('&'), function(){ 
            var pair = this.split('=');
            inputs+='<input type="hidden" name="'+ pair[0] +'" value="'+ pair[1] +'" />'; 
        });        // request发送请求
        jQuery('<form action="'+ url +'" method="'+ (method||'post') +'">'+inputs+'</form>')
        .appendTo('body').submit().remove();
    };
};
/**
 * datagrid加载完后合并指定单元格
 * @author jasonzhou
 * @param arr ：合并列的field数组及对应前提条件filed（为空则直接内容合并）如下數組
  				var arr =[{mergeFiled:"settingId",premiseFiled:"settingId"},
						{mergeFiled:"customerName",premiseFiled:"settingId"},
						{mergeFiled:"reportName",premiseFiled:"settingId"},
						{mergeFiled:"reportCode",premiseFiled:"settingId"},
						{mergeFiled:"recipient",premiseFiled:"settingId"}];  
 * @param id ：要合并的datagrid中的表格id  
 */   
function mergeCells(arr,id){  
    var dg = $("#"+id);   
    var rowCount = dg.datagrid("getRows").length;  
    var cellName;  
    var span;  
    var perValue = "";  
    var curValue = "";  
    var perCondition="";  
    var curCondition="";  
    var flag=true;  
    var condiName="";  
    var length = arr.length - 1;  
    for (i = length; i >= 0; i--) {  
        cellName = arr[i].mergeFiled;  
        condiName=arr[i].premiseFiled;  
        if(condiName!=null&&condiName!=""){  
            flag=false;  
        }	
        perValue = "";  
        perCondition="";  
        span = 1;  
        for (row = 0; row <= rowCount; row++) {  
            if (row == rowCount) {  
                curValue = "";  
                curCondition="";  
            } else {  
                curValue = dg.datagrid("getRows")[row][cellName];  
                if(!flag){  
                    curCondition=dg.datagrid("getRows")[row][condiName];  
                }  
            }  
            if (perValue == curValue&&(flag||perCondition==curCondition)) {  
                span += 1;  
            } else {  
                var index = row - span;  
                dg.datagrid('mergeCells', {  
                    index : index,  
                    field : cellName,  
                    rowspan : span,  
                    colspan : null  
                });  
                span = 1;  
                perValue = curValue;  
                if(!flag){  
                    perCondition=curCondition;  
                }  
            }  
        }  
    }  
}

/**
 * 格式化Importance,加載完成後將案件類型importance格式化為一般或急件
 * @author Amanda Wang
 * @param row 本行
 */
function fomatterImportance(row){
	if (row.importance == "3") {
		row.importanceName = "一般"
		return row.importanceName;
	} else {
		row.importanceName = "急件"
		return row.importanceName;
	}
}

/**
 * 格式化IsWorkDay,加載完成後將是否為上班日isWorkDay格式化為是或否
 * @author Amanda Wang
 * @param row 本行
 */
function fomatterIsWorkDay(row){
	if (row.isWorkDay) {
		return '是';
	} else {
		return '否';
	}
}

/**
 * 實現下拉框復選
 * @author jasonzhou
 * @param newValue onchange事件裏newValue值
 * @param name 對應下拉框name屬性值
 */
function selectMultiple(newValue,name){
	if (newValue.value == "") {
		$("#"+name).combobox("setValue", "");
	} else {
		$("#"+name).combobox("unselect", "");
	}
}
/**
 * 實現下拉框復選--取消選擇時，判斷如果當前在無選擇的數據，則選擇請選擇。
 * @author jasonzhou
 * @param newValue onchange事件裏newValue值
 * @param name 對應下拉框name屬性值
 */
function unSelectMultiple(newValue,name){
	var selectValue = $("#"+name).combobox("getValue");
	if (selectValue == undefined){
		$("#"+name).combobox("setValue", "");
	}
}

/**
 * 控制時間微調器的增長幅度
 * @param obj 當前對象
 * @param up 為boolean值，true表示-在用户点击向上微调按钮， false表示-在用户点击向下微调按钮
 * @param hourSpin 小時的微調幅度。不傳默認為1；
 * 
 * 在使用時間微調器的標籤之前，需要加入一個input框，類型為隱藏域。例如：
 *  <input id="hidden_1" type="hidden" value="2">
 *  1 - 需跟標籤的ID一致。
 *  2 - 如果有默認值，則進行賦值。
 */
function spinTimes(obj, up, hourSpin){
		 var highlight = $(obj).timespinner('options').highlight;
		 var thisId = $(obj).attr('id');
		 var id = '#hidden_' + thisId;
		 if (!hourSpin) {
			 hourSpin = 1;
		 }
    if (highlight == 0) {          
    	var value = $(id).val();
    		if(value.length <= 0) {
     			 value = 0;
    		 } else if (value.indexOf(':') >= 0) {
      			value = value.split(':')[0];
    		 }
    	if (value != "") {
    		var nowHour = $(obj).timespinner("getValue");
    		nowHour = nowHour.split(':')[0];
    		if (up) {
    			nowHour = parseInt(nowHour) - 6;
    		} else {
    			nowHour = parseInt(nowHour) + 6;
    		}
    		
    		if (parseInt(nowHour) < 0) {
    			nowHour = 24 + parseInt(nowHour);
    		}
    		var hiddenHour = value%24;
    		if (parseInt(nowHour) == 0) {
    			value = 24;
    		} else {
    			if (parseInt(hiddenHour) != parseInt(nowHour)) {
		    		if (parseInt(hiddenHour) > parseInt(nowHour)) {
		    			value = parseInt(value) + (parseInt(hiddenHour) - parseInt(nowHour));
		    		} else {
		    			value = parseInt(value) + (parseInt(nowHour) - parseInt(hiddenHour));
		    		}
    			}
    		}
    	}
   		if (up){
     			value = parseInt(value) + hourSpin;
     		} else {
     			value = parseInt(value) - hourSpin;
    		}
    	 $(id).val(value);
    	 value = value + ':' + $(obj).timespinner('getMinutes');
    	 $(obj).timespinner('setValue',value);
    } else {
		var value = $(obj).timespinner('getHours');
		$(id).val(value);
    }
}
/**
 * 修改特店表頭頁面
 * @param div 彈出頁面的div
 * @param title 頁面標題
 * @param actionId actionId
 * @param id 主鍵
 * @param fun 回電函數
 */
function viewEditMerchantHeader(div, title, queryParams, fun, cancleBack, actionId, contextPath, isAssetManage) {
		var viewDlg = $('#' + div).dialog({    
	    title : title,    
	    width : 760,
	    height :475,
	    top:10,
	    closed : false,
	    //緩存    
	    cache : false,
	    queryParams : queryParams,
	    href : contextPath + "/merchantHeader.do",
	    modal : true,
	    onLoad : function() {
	    	textBoxSetting(div);
	    	if (isAssetManage) {
	    		$('#area').combobox({
					editable:false, 
					required:false,
					valueField:'value',
					textField:'name',
					width:'184px',
					panelHeight:'auto',
					validType:'',
				});
				document.getElementById("areaHtml").innerHTML="特店區域:";
				$("#isAssetManage").val("Y");
	    	} else {
	    		$('#area').combobox({
					editable:false, 
					required:true,
					valueField:'value',
					textField:'name',
					width:'184px',
					panelHeight:'auto',
					validType:'requiredDropList',
					invalidMessage:"請輸入特店區域" 
				});
	    		document.getElementById("areaHtml").innerHTML="特店區域:<span class=\"red\">*</span>";
	    	}
        },
        onClose : function(){
			if(viewDlg){
				viewDlg.panel('clear');
			}
		},
        buttons : [{
			text:'儲存',
			width:90,
			iconCls:'icon-ok',
			handler: function(){		
				$("#dialogMsg").text("");
				var url=contextPath + "/merchantHeader.do?actionId=save";
			 	var saveParamHeader = $("#edit_Form_Merchant_Header").form("getData");
			 	saveParamHeader.companyId = $("#companyId").combobox('getValue');
			 	saveParamHeader.merchantCode = $("#merchantCode").val();
			 	saveParamHeader.registeredName = $("#name").val();
			 	saveParamHeader.isAssetMManage = $("#isAssetManage").val();
			 	var merchantCode = $("#merchantCode").val();
			 	var tempmerchantCode = $("#tempMerchant").val();
			 	//給客戶特店表頭添加下拉框提示消息
			 	var controls = ['companyId','merchantCode','headerName','area'];
				if (validateForm(controls) && $("#edit_Form_Merchant_Header").form('validate')) {
					if((tempmerchantCode == '')) {
						$("#dialogMsg").text("請依特店代號帶入資料");
				 		return;
					} else if(merchantCode != tempmerchantCode) {
						$("#dialogMsg").text("特店代號已變更，請重新帶入資料");
						return;
					}
					commonSaveLoading(div);
					$.ajax({
						url : url,
						data : saveParamHeader,
						type : 'post', 
						cache : false, 
						dataType : 'json', 
						success : function(d) {
							commonCancelSaveLoading(div);
							if (d.success) {
								viewDlg.dialog('close');
								if(fun!=null) {
									fun(d);
								} else {
									
								}
							}else{
								$("#dialogMsg").text(d.msg);	
							}
						},
						error : function() {
							commonCancelSaveLoading(div);
							var msg;
							if(actionId != undefined) {
								if (actionId == '<%=IAtomsConstants.ACTION_INIT_ADD%>') {
									msg = "新增失敗";
								} else {
									msg = "修改失敗";
								}
							}
							$.messager.alert('提示', msg, 'error');	
						}
					});
				}
			}
		},{
			text:'取消',
			width:90,
			iconCls:'icon-cancel',
			handler : function(){
				confirmCancel(function(){
					viewDlg.dialog('close');
					if (cancleBack != null) {
						cancleBack();
					}
				});
			}
		}]
	});
}
/**
* easyui datagrid 表頭和分頁控件增加遮蓋層，使分頁不能使用
* @param gridId: easyui datagrid 的id
**/
function gridHeaderPagerBlock(gridId){
	var tableWidth = $("#" + gridId).datagrid("getPanel").find('div.datagrid-view2 table').css("width");
	var panelWidth = $("#" + gridId).datagrid("getPanel").css("width");
	var blockOptions = {message:null,overlayCSS:{backgroundColor:'#fff',cursor:'default',width:parseInt(tableWidth, 10)>parseInt(panelWidth, 10)?tableWidth:panelWidth}};
	var pager = $("#" + gridId).datagrid("getPager");
	pager.block(blockOptions);
	var header = $("#" + gridId).datagrid("getPanel").find('div.datagrid-header');
	header.block(blockOptions);
}
/**
* easyui datagrid 表頭和分頁控件取消遮蓋層，使分頁可以使用
* @param gridId: easyui datagrid 的id
**/
function gridHeaderPagerUnBlock(gridId){
	var pager = $("#" + gridId).datagrid("getPager");
	pager.unblock();
	var header = $("#" + gridId).datagrid("getPanel").find('div.datagrid-header');
	header.unblock();
}

/**
 * easyui datagrid 行號與頁號顯示不完全的解決方法
 */
$.extend($.fn.datagrid.methods, {
    fixRownumber : function (jq, id) {
        return jq.each(function () {
            var panel = $("#" + id).datagrid("getPanel");
            //获取最后一行的number容器,并拷贝一份
            var clone = $(".datagrid-cell-rownumber", panel).last().clone();
            //由于在某些浏览器里面,是不支持获取隐藏元素的宽度,所以取巧一下
            clone.css({
                "position" : "absolute",
                left : -1000
            }).appendTo("body");
            var width = clone.width("auto").width();
            //默认宽度是25,所以只有大于25的时候才进行fix
            if (width > 25) {
                //多加5个像素,保持一点边距
                $(".datagrid-header-rownumber,.datagrid-cell-rownumber", panel).width(width + 10);
                $(".pagination-num", panel).width(width + 10);
                //修改了宽度之后,需要对容器进行重新计算,所以调用resize
                $("#" + id).datagrid("resize");
                //一些清理工作
                clone.remove();
                clone = null;
            } else {
                //还原成默认状态
                $(".datagrid-header-rownumber,.datagrid-cell-rownumber", panel).removeAttr("style");
                $(".pagination-num", panel).removeAttr("style");
                $("#" + id).datagrid("resize");
            }
        });
    }
});
/**
 * 提交表單時候驗證combobox是否必填
 */
function validateForm(controls) {
	var result = true;
	var div = $('div');
	for(var i = 0; i<controls.length; i++) {
		var check = $('#'+controls[i]);
		if (check.hasClass("div-tips")) {
			check.tooltip("show");
			var message = check.attr("data-list-required");
			if (message && check.find(":checked").length == 0) {
				div.bind("scroll.validate",function(event){			
					check.tooltip("destroy");
						check.tooltip({    
							position: 'right',   
							content: '<span style="color:#000">' + message + '</span>', 
							onShow: function(){
								$(this).tooltip('tip').css({
									backgroundColor: 'rgb(255,255,204)',
									borderColor: 'rgb(204,153,51)'
								});
							}   
						});
					div.unbind("scroll.validate");
				}); 
				result = false;
				break;
			}
		} else if (check.hasClass("div-list")) {
			var message = check.attr("data-list-required");
			if (message && check.find(":checked").length == 0) {
				check.tooltip({    
					position: 'right',   
					content: '<span style="color:#000">' + message + '</span>', 
					onShow: function(){
						$(this).tooltip('tip').css({
							backgroundColor: 'rgb(255,255,204)',
							borderColor: 'rgb(204,153,51)'
						});
					}   
				});
				check.removeClass("div-list").addClass("div-tips").tooltip("show");
				/*setTimeout(function () {
					div.bind("scroll.validate",function(event){			
						check.removeClass("div-list").addClass("div-tips").tooltip("destroy");
						div.unbind("scroll.validate");
					}); 
				}, 500);*/
				
				result = false;
				break;
			}
			
		} else {
			//if(check.textbox("isValid") == false) {
			//如果combobox 驗證失敗
			if (check.hasClass("easyui-combobox")){
				if (!check.combobox('isValid')) {
					check.combobox('textbox').focus().trigger('mouseover');
					setTimeout(function () {
						div.bind("scroll.validate",function(event){			
							check.combobox('textbox').blur();
							div.unbind("scroll.validate");
						}); 
					}, 500);
					result = false;
				}
			//如果textbox 驗證失敗
			} else if (check.hasClass("easyui-textbox")){
				if (!check.textbox('isValid')) {
					check.textbox('textbox').focus().trigger('mouseover');
					setTimeout(function () {
						div.bind("scroll.validate",function(event){			
							check.textbox('textbox').blur();
							div.unbind("scroll.validate");
						}); 
					}, 500);
					result = false;
				}
			//如果datebox 驗證失敗
			} else if (check.hasClass("easyui-datebox")){
				if (!check.datebox('isValid')) {
					check.datebox('textbox').focus().trigger('mouseover');
					setTimeout(function () {
					div.bind("scroll.validate",function(event){			
						check.datebox('textbox').blur();
						div.unbind("scroll.validate");
						}); 
					}, 500);
					result = false;
				}
			//如果datetimebox 驗證失敗
			} else if (check.hasClass("easyui-datetimebox")){
				if (!check.datetimebox('isValid')) {
					check.datetimebox('textbox').focus().trigger('mouseover');
					setTimeout(function () {
					div.bind("scroll.validate",function(event){			
						check.datetimebox('textbox').blur();
						div.unbind("scroll.validate");
					}); 
					}, 500);
					result = false;
				}
			} else if (check.hasClass("easyui-timespinner")){
				if (!check.timespinner('isValid')) {
					check.timespinner('textbox').focus().trigger('mouseover');
					setTimeout(function () {
					div.bind("scroll.validate",function(event){			
						check.timespinner('textbox').blur();
						div.unbind("scroll.validate");
					}); 
					}, 500);
					result = false;
				}
			}
			if(!result) {
				break;
			}
		}		
	}
	return result;
}
/**
 * 行內編輯表單驗證
 * @param datagridId : datagridId， index：正在驗證的行，field：正在驗證的field的數組
 */
function validateFormInRow(datagridId, index, field) {
	var result = true;
	var obj = $('#'+datagridId);
	var div = $('div.topSoller');
	for(var i = 0;i < field.length; i++) {
		var fieldName = obj.datagrid('getEditor',{index:index,field:field[i]});
		if(fieldName != null) {
			if($(fieldName.target).hasClass("combobox-f")) {
				if (!$(fieldName.target).combobox('isValid')) {	
					$(fieldName.target).combobox('textbox').focus().trigger('mouseover');
					setTimeout(function () {
						div.bind("scroll.validate",function(event){	
							div.unbind("scroll.validate");
							$(fieldName.target).combobox('textbox').blur();
						}); 
					}, 500);
					result = false;
				}
			} else if ($(fieldName.target).hasClass("textbox-f")) {
				if (!$(fieldName.target).textbox('isValid')) {
					$(fieldName.target).textbox('textbox').focus().trigger('mouseover');
					setTimeout(function () {
						div.bind("scroll.validate",function(event){			
							div.unbind("scroll.validate");
							$(fieldName.target).textbox('textbox').blur();
						}); 
					}, 500);
					result = false;
				}
			} else if ($(fieldName.target).hasClass("datebox-f")) {
				if (!$(fieldName.target).textbox('isValid')) {
					$(fieldName.target).datebox('textbox').focus().trigger('mouseover');
					setTimeout(function () {
						div.bind("scroll.validate",function(event){			
							div.unbind("scroll.validate");
							$(fieldName.target).datebox('textbox').blur();
						}); 
					}, 500);
					result = false;
				}
			}
		}
		if(!result) {
			break;
		}
	}
	return result;
}


/**
 * 公用方法 處理保存按鈕點擊形成遮罩
 * @param dialogId ： 传入當前打開dialog的Id
 */
function commonSaveLoading(dialogId){
	// 拿到dialog的高度
	var dialogHeight = parseInt($('#' + dialogId).css("height"), 10);
	// 內部div高度
	var innerDivHeight = parseInt($('#' + dialogId).children('div').first().css("height"), 10);
	// padding對象
	var paddingObject = $('#' + dialogId).children('div').first();
	// padding高度
	var paddingHeight = paddingObject.css("padding");
	// 真實高度
	var realHeight;
	// 增加高度
	var addHeight;
	if(paddingHeight){
		// 分離padding長度
		var array = paddingHeight.split(" ");
		// 增加上下內邊距高度
		if(array){
			// padding:10px; 所有 4 个内边距都是 10px
			if(array.length == 1){
				addHeight = parseInt(array[0], 10) * 2;
			// padding:10px 5px; 上内边距和下内边距是 10px 右内边距和左内边距是 5px
			} else if(array.length == 2){
				addHeight = parseInt(array[0], 10) * 2;
			// padding:10px 5px 15px; 上内边距是 10px 右内边距和左内边距是 5px 下内边距是 15px
			} else if(array.length == 3){
				addHeight = parseInt(array[0], 10) + parseInt(array[2], 10);
			// padding:10px 5px 15px 20px; 上内边距是 10px 右内边距是 5px 下内边距是 15px 左内边距是 20px
			} else if(array.length == 4){
				addHeight = parseInt(array[0], 10) + parseInt(array[2], 10);
			}
		}
	// ie等瀏覽器獲取padding方式
	} else {
		addHeight = parseInt(paddingObject.css("padding-top"), 10) + parseInt(paddingObject.css("padding-bottom"), 10);
	}
	// dialog的高度大於等於內部div高度，真實高度爲dialog的高度
	if(dialogHeight >= innerDivHeight){
		realHeight = dialogHeight;
	// dialog的高度小於內部div高度，真實高度爲內部div高度加上padding設置上下邊距高度
	} else {
		if(addHeight){
			realHeight = (parseInt(innerDivHeight, 10) + addHeight);
		}
	}
	// 遮罩樣式
	var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default',height:realHeight}};
	// 內部內容遮罩
	$('#' + dialogId).block(blockStyle);
	// 遮罩樣式
	var blockOptions = {message:null,overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
	// 按鈕遮罩
	var buttonObject = $('#' + dialogId).siblings('div.dialog-button');
	if(buttonObject.length > 0){
		$('#' + dialogId).siblings('div.dialog-button').block(blockOptions);
	}
}
/**
 * 公用方法 儲存未成功去除遮罩
 * @param dialogId ： 传入當前打開dialog的Id
 */
function commonCancelSaveLoading(dialogId){
	// 去除內部內容遮罩
	$('#' + dialogId).unblock();
	// 去除按鈕遮罩
	var buttonObject = $('#' + dialogId).siblings('div.dialog-button');
	if(buttonObject.length > 0){
		$('#' + dialogId).siblings('div.dialog-button').unblock();
	}
}

/**
 * 處理欄位按鈕點擊打開選擇顯示欄位對話框
 * @param dialogId ： 打開對話框按鈕id
 * @param datalistId ： datalist的id
 * @param datagridId ： datagrid的id
 * @param otherColumn ： 默認需要顯示的列，沒有在datalist定義
 */
function dynamicshowColumn(dialogId, datalistId, datagridId, otherColumn){
	var showCloumnDialog = $("#" + dialogId).dialog({
		title:'選擇要顯示的列',
		top:10,
	    modal: true,
	    closed: false,
		buttons : [{
			text:'確認',
			iconCls:'icon-ok',
			handler: function(){
				// 獲得所有列的字段
				var allCols = $('#' + datagridId).datagrid("getColumnFields");
				// 取消所有列顯示
				for(var i=0; i < allCols.length; i++) {
					$('#' + datagridId).datagrid("hideColumn",allCols[i]);
				}
				// 預設顯示處理，復選按鈕等列
				if(otherColumn){
					for(var i = 0; i < otherColumn.length; i++){
						$('#' + datagridId).datagrid("showColumn", otherColumn[i]);
					}
				}
				// 拿到所有選中的列					
				var selectRows = $('#' + datalistId).datagrid('getSelections');
				if(selectRows){
					// 拿到datalist所有列
					var allRows = $('#' + datalistId).datagrid('getRows');
					for (var i = 0; i < allRows.length; i ++) {
						var tempRow = allRows[i];
						if (!isInArray(selectRows, tempRow.colName)) {
							$('#exportList').datagrid('updateRow',{
								index: $('#' + datalistId).datagrid("getRowIndex", tempRow),
								row: {
									value: 'undefined'
								}
							});
						} else {
							// 顯示所有選中的列
							$('#' + datagridId).datagrid("showColumn",tempRow.colName);
							if(tempRow.value != 'defaultShow'){
								$('#exportList').datagrid('updateRow',{
									index: $('#' + datalistId).datagrid("getRowIndex", tempRow),
									row: {
										value: 'isSelected'
									}
								});
							}
						}
					}
				}
				//修改了宽度之后,需要对容器进行重新计算,所以调用resize
				$("#"+datagridId).datagrid("resize");
				showCloumnDialog.dialog('close');
				
				setTimeout(function () {
					$.each($('#' + datagridId).datagrid('getRows'), function (i, row) {
						$('#' + datagridId).datagrid('fixRowHeight', i);
					});
				}, 0);
			}
		},{
			text:'取消',
			iconCls:'icon-cancel',
			handler: function(){
				showCloumnDialog.dialog('close');
			}
		}],
		onClose : function(){
			 // 拿到datalist所有列
			var rows = $('#' + datalistId).datagrid('getRows');
			for (var i = 0; i < rows.length; i ++) {
				var row = rows[i];
				// 取消该列的临时选中
				var rowIndex = $('#' + datalistId).datagrid("getRowIndex", row);
				// 如果該列是临时选中
				if (row.value != 'isSelected' && row.value != 'defaultShow') {
					$('#' + datalistId).datagrid("unselectRow", rowIndex);
				// 如果该列为取消之前选中
				} else {
					$('#' + datalistId).datagrid("selectRow",rowIndex);
				}
			}
		}
	}); 
}

/**
 * 使用循环的方式判断一个元素的colName是否存在于一个数组中
 * @param {Object} arr 数组
 * @param {Object} value 元素值
 */
function isInArray(arr,value){
    for(var i = 0; i < arr.length; i++){
        if(value === arr[i].colName){
            return true;
        }
    }
    return false;
}
/**
 * 處理datalis的全選按鈕點擊事件
 * @param datalistId ： datalist的id
 * @param checkAllBtnId ： 全選按鈕的id
 */
function checkAllForDatalist(datalistId, checkAllBtnId){
	var checkAllFlag = $('#' + checkAllBtnId).prop('checked');
	if (checkAllFlag) {
		$('#' + datalistId).datagrid("selectAll");
	} else {
		var rows = $('#' + datalistId).datagrid('getRows');
		// 取消其餘列選中
		for (i = 0; i < rows.length; i++) {
			var row = rows[i];
			if((row.value != 'defaultShow')){
				$('#' + datalistId).datagrid("unselectRow", i);
			}
		}
	}
}

/**
 * datalist加載成功觸發事件，綁定相應列的值
 * @param dialogId : 打開對話框按鈕id
 * @param datalistId : datalist的id
 * @param checkAllBtnId : 全選按鈕
 */
function datalistSuccessLoad(dialogId, datalistId, checkAllBtnId){
	// 拿到datalist所有列
	var rows = $('#' + datalistId).datagrid('getRows');
	// 拿到當前datalist選中列
	var selectRows = $('#' + datalistId).datagrid('getSelections');
	for (var i = 0; i < rows.length; i ++) {
		var row = rows[i];
		// 如果該列是默認顯示的
		if (row.value == 'defaultShow') {
			var index = $('#' + datalistId).datagrid("getRowIndex",row);
			// 選中默認顯示列
			$('#' + datalistId).datagrid("selectRow",index);
			var obj = $("#" + dialogId).find("table.datagrid-btable").find("[datagrid-row-index='"+ index +"']");
			// 處理默認顯示列 不可點擊
			obj.unbind('click').bind('click',function(e){
				return false;
			});
		}
	}
	// 如果所有列與選中列大小性等 則勾選全選按鈕
	if((rows && selectRows) && (rows.length == selectRows.length)){
		$('#' + checkAllBtnId).prop("checked",true);
	} else {
		$('#' + checkAllBtnId).prop("checked",false);
	}
}

/**
 * datalist行選中事件
 * @param row : 當前行
 * @param index : 行號
 * @param datalistId : datalist的id
 * @param checkAllBtnId : 全選按鈕
 */
function dataListOnSelect(row, index, datalistId, checkAllBtnId){
	// 当前选中行
	var selectRows = $('#' + datalistId).datagrid('getSelections');
	// 所有行
	var rows = $('#' + datalistId).datagrid('getRows');
	// 大小相等
	if(selectRows.length == rows.length) {
		// 全選按鈕
		$('#' + checkAllBtnId).prop('checked',true);
	}
}

/**
 * datalist取消行選中事件
 * @param row : 當前行
 * @param index : 行號
 * @param datalistId : datalist的id
 * @param checkAllBtnId : 全選按鈕
 */
function dataListUnSelect(row, index, datalistId, checkAllBtnId){
	// 取消全選按鈕選中
	$('#' + checkAllBtnId).prop('checked',false);
}

/**
 * 多選下拉框選中事件 通過onSelect與onUnselect處理請選擇的問題
 * parent : 處理某一區域多選框時該區域容器id
 */
function mutilComboSelectEvent(parent){
	var selectObjs;
	if (!parent) {
		selectObjs = $(".easyui-mutil-select");
	} else {
		selectObjs = $("#" + parent).find(".easyui-mutil-select");
	}
	selectObjs.each(function(index, obj){
		// 得到當前多選下拉框的對象
		var thisObject = $(obj);
		// 得到當前多選下拉框控件的id
		var comboboxId = thisObject.attr("id");
		thisObject.combobox({
			onSelect : function(newValue) {
				// 選中時處理請選擇項
				selectMultiple(newValue, comboboxId);
			},
			onUnselect : function(newValue) {
				// 取消選中時處理請選擇項
				unSelectMultiple(newValue, comboboxId);
			},
		});
	});
}

/**
 * 設置打開對話框的高度，使設置的滾動條生效
 * dialogId : 處理某一區域多選框時該區域容器id
 */
function settingDialogPanel(dialogId){
	$(".setting-dialog-panel-height").css("height", parseInt($('#' + dialogId).css("height"))- 20);
}
/**
 * 禁止頁面Backspace, F5, Alt+Left, Alt+Right, Ctrl+R 按鍵 
 * 只有在text或者textera或者password,并且為可編輯狀態backspace才可用
 */
function limitFunctionKeyAction() {
   //獲取事件
   var ev = event || window.event || arguments.callee.caller.arguments[0];
   //键码值
   var keycode = ev.keyCode|| ev.which;
      if (keycode==8 || keycode === 116 || (window.event.altKey == true && (keycode==37 || keycode==39)) || (window.event.ctrlKey == true && (keycode == 82)) ) {
	      if (keycode == 8) {
			var obj = ev.target || ev.srcElement;
			var type = obj.type || obj.getAttribute('type');
			var vReadOnly = obj.readOnly;
			var vDisabled = obj.disabled;

			vReadOnly = (vReadOnly == undefined) ? false : vReadOnly;
			vDisabled = (vDisabled == undefined) ? true : vDisabled;
			var flag1 = (type == "password" || type == "text" || type == "textarea")
					&& (vReadOnly == true || vDisabled == true);
			var flag2 = type != "password" && type != "text" && type != "textarea";
			if (flag2 || flag1) {
				window.event.keyCode = 0;
				if (window.event.preventDefault)
					window.event.preventDefault();
				else
					window.event.returnValue = false;
				return true;
			} else {
				return false;
			}
		} else {
			window.event.keyCode = 0;
			if (window.event.preventDefault)
				window.event.preventDefault();
			else
				window.event.returnValue = false;
			return true;
		}
    }
	return false;
}
/**
 * 目的：後台驗證不通過，垂直滾動條回到最上面
 * @param objId：打開的dialog的id
 * @param msgId：需要特殊定位的標誌id
 */
function handleScrollTop(objId, msgId){
	var top  = 0;
	if (msgId) {
		top = $("#"+msgId).get(0).offsetTop - 50;
	}
	if(objId != null) {
		$("#" + objId).animate({scrollTop: top}, 100);
	}
}
