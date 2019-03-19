﻿﻿$(function(){
	load();
	jQuery.ajaxSetup({ cache: false });//ajax
});

function load() {
    InitLeftMenu();
    $('body').layout();
    $(".easyui-accordion .panel-header").click();  //not accordion-header-selected
}

function InitLeftMenu() {
    $('.easyui-accordion li a').click(function () {
        var tabTitle = $(this).text();
        var url = $(this).attr("href");
        addTab(tabTitle, url);
        $('.easyui-accordion li div').removeClass("selected");
        $(this).parent().addClass("selected");
    }).hover(function () {
        $(this).parent().addClass("hover");
    }, function () {
        $(this).parent().removeClass("hover");
    });
}
function stateChange(_frame){
      $(_frame).css("visibility","visible").prev().remove();
}
function addTab(title, url) {
	var loading = '<div class="blockUI blockMsg blockPage" style="margin: 0px; padding: 0px; border: 3px solid rgb(149, 184, 231); border-image: none; left: 45%; top: 43%; width: 150px; height: 50px; text-align: center; color: rgb(0, 0, 0); line-height: 50px; position: fixed; z-index: 1011; cursor: default; opacity: 1; background-color: rgb(255, 255, 255);font-size: 18px;">loading...</div>';
	var content = loading + '<iframe onload="stateChange(this)" scrolling="no" frameborder="0"  src="' + url + '" id="iframe-content" style="visibility: hidden;width:100%;height:100%;"></iframe>';
	//var content = '<iframe scrolling="no" frameborder="0"  src="' + url + '" id="iframe-content" style="width:100%;height:100%;"></iframe>';
    if ($('#tt').tabs('exists', title)) {
        $('#tt').tabs('select', title);
        var tab = $("#tt").tabs('getSelected');       
        $("#tt").tabs('update',{  
        tab:tab,  
        options:{  
           title:title,
           style:{padding:'1px'},  
           content:content,  
	       closable:true,
	       fit:true,  
	       selected:true  
        }  
         });
    } else {
        $('#tt').tabs('add', {
            title: title,
            content: content,
            closable: true,
            selected:true ,
         	tools: [{
                iconCls: 'icon-mini-refresh',
                handler: function() {
                    $('#tt').tabs("select", $(this).parent().parent().first().first().text());
                    var tab = $('#tt').tabs('getSelected'); // get selected panel
                    $('#tt').tabs('update', {
                        tab: tab,
                        options: {
                        	content:content,    // the new content URL
                        }
                    });
                }
            }]

        });
    }
}
function createFrame(url) {
    var s = '<iframe name="mainFrame" scrolling="no" frameborder="0"  src="' + url + '" id="iframe-s" style="width:100%;height:100%;"></iframe>';
    return s;
}