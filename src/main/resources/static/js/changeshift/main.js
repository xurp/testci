"use strict";
//# sourceURL=main.js
 
//DOM 加载完再执行
//$(function() {

    var _pageSize; // 存储用于搜索
    var _arg;
    var _id=$("#changetype").val();
    function getChangeShifts(pageIndex, pageSize,arg,id) {
        $.ajax({ 
            url: "/changeshift/"+id, 
            contentType : 'application/json',
            data:{
                "async":true, 
                "pageIndex":pageIndex,
                "pageSize":pageSize,
                "arg":arg
                
            },
            success: function(data){
                $("#mainContainer").html(data);
            },
            error : function() {
                toastr.error("error!");
            }
        });
   }
    $("#all").click(function() {
    	var id=$("#changetype").val();
    	getChangeShifts(0, _pageSize,"all",id);
    	_arg="all";
    	_id=id;
    	$(".filterbutton").removeClass("active");
    	$("#all").addClass("active");
    	
    });
    $("#agreedbutnotyetapproved").click(function() {
    	var id=$("#changetype").val();
    	getChangeShifts(0, _pageSize,"agreedbutnotyetapproved",id);
    	_arg="agreedbutnotyetapproved";
    	_id=id;
    	$(".filterbutton").removeClass("active");
    	$("#agreedbutnotyetapproved").addClass("active");
    	
    });
    
$("#agreed").click(function() {
	var id=$("#changetype").val();
	getChangeShifts(0, _pageSize,"agreed",id);
	_arg="agreed";
	_id=id;
	$(".filterbutton").removeClass("active");
	$("#agreed").addClass("active");
    });
$("#notyetagreed").click(function() {
	var id=$("#changetype").val();
	getChangeShifts(0, _pageSize,"notyetagreed",id);
	_arg="notyetagreed";
	_id=id;
	$(".filterbutton").removeClass("active");
	$("#notyetagreed").addClass("active");
});
$("#approved").click(function() {
	var id=$("#changetype").val();
	getChangeShifts(0, _pageSize,"approved",id);
	_arg="approved";
	_id=id;
	$(".filterbutton").removeClass("active");
	$("#approved").addClass("active");
});
$("#notyetapproved").click(function() {
	var id=$("#changetype").val();
	getChangeShifts(0, _pageSize,"notyetapproved",id);
	_arg="notyetapproved";
	_id=id;
	$(".filterbutton").removeClass("active");
	$("#notyetapproved").addClass("active");
});
$("#refuse").click(function() {
	var id=$("#changetype").val();
	getChangeShifts(0, _pageSize,"refuse",id);
	_arg="refuse";
	_id=id;
	$(".filterbutton").removeClass("active");
	$("#refuse").addClass("active");
});
    // 分页
    $.tbpage("#mainContainer", function (pageIndex, pageSize) {
    	getChangeShifts(pageIndex, pageSize,_arg,_id);
        _pageSize = pageSize;
    });


    // 获取添加用户的界面
    $("#addchangeshift").click(function() {
        $.ajax({ 
             url: "/changeshift/add", 
             success: function(data){
                 $("#userFormContainer").html(data);
             },
             error : function(data) {
                 toastr.error("error!");
             }
         });
    });


    // 提交变更后，清空表单
    
    function deletechangeshift(id){
    	if(window.confirm('Do you want to delete this record?')){
    		var token = $("meta[name='_csrf']").attr("content");
        	var header = $("meta[name='_csrf_header']").attr("content");
        	$(document).ajaxSend(function(e, xhr, options) {
        	    xhr.setRequestHeader(header, token);
        	});
    	$.ajax({ 
            url: "/changeshift/" +id , 
            type: 'DELETE', 
            success: function(data){
                if (data.success) {
                	getChangeShifts(0, _pageSize,_arg,_id);
                } else {
                    toastr.error(data.message);
                }
            },
            error : function() {
                toastr.error("error!");
            }
        });
        }
    }
    function editchange(id){
    	var token = $("meta[name='_csrf']").attr("content");
    	var header = $("meta[name='_csrf_header']").attr("content");
    	$(document).ajaxSend(function(e, xhr, options) {
    	    xhr.setRequestHeader(header, token);
    	});
    	$.ajax({ 
            url: "/changeshift/edit/" + id, 
            success: function(data){
                $("#userFormContainer").html(data);
            },
            error : function() {
                toastr.error("error!");
            }
        });
        }
   

//});