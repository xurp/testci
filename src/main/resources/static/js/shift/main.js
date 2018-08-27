"use strict";
//# sourceURL=main.js
 
//DOM 加载完再执行
//$(function() {

    var _pageSize; // 存储用于搜索

    // 根据用户名、页面索引、页面大小获取用户列表
    function getShiftsByName(pageIndex, pageSize) {
         $.ajax({ 
             url: "/shift", 
             contentType : 'application/json',
             data:{
                 "async":true, 
                 "pageIndex":pageIndex,
                 "pageSize":pageSize,
                 "name":$("#searchName").val()
             },
             success: function(data){
                 $("#mainContainer").html(data);
             },
             error : function() {
                 toastr.error("error!");
             }
         });
    }

    // 分页
    $.tbpage("#mainContainer", function (pageIndex, pageSize) {
        getShiftsByName(pageIndex, pageSize);
        _pageSize = pageSize;
    });

    // 搜索
    $("#searchNameBtn").click(function() {
        getShiftsByName(0, _pageSize);
    });

    // 获取添加用户的界面
    $("#addShift").click(function() {
        $.ajax({ 
             url: "/shift/add", 
             success: function(data){
                 $("#userFormContainer").html(data);
             },
             error : function(data) {
                 toastr.error("error!");
             }
         });
    });


    // 提交变更后，清空表单
    $("#submitEdit").click(function() {
    	var token = $("meta[name='_csrf']").attr("content");
    	var header = $("meta[name='_csrf_header']").attr("content");
    	$(document).ajaxSend(function(e, xhr, options) {
    	    xhr.setRequestHeader(header, token);
    	});
        $.ajax({ 
             url: "/shift", 
             type: 'POST',
             data:$('#userForm').serialize(),
             success: function(data){
                 //$('#userForm')[0].reset();  

                 if (data.success) {
                	 $('#flipFlop').modal('hide');
                     getShiftsByName(0, _pageSize);
                 } else {
                     toastr.error(data.message);
                 }

             },
             error : function() {
                 toastr.error("error!");
             }
         });
    });
    function deleteShift(id){
    	if(window.confirm('Do you want to delete this record?')){
    		var token = $("meta[name='_csrf']").attr("content");
        	var header = $("meta[name='_csrf_header']").attr("content");
        	$(document).ajaxSend(function(e, xhr, options) {
        	    xhr.setRequestHeader(header, token);
        	});
    	$.ajax({ 
            url: "/shift/" +id , 
            type: 'DELETE', 
            success: function(data){
                if (data.success) {
                    getShiftsByName(0, _pageSize);
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
    function editShift(id){
    	var token = $("meta[name='_csrf']").attr("content");
    	var header = $("meta[name='_csrf_header']").attr("content");
    	$(document).ajaxSend(function(e, xhr, options) {
    	    xhr.setRequestHeader(header, token);
    	});
    	$.ajax({ 
            url: "/shift/edit/" + id, 
            success: function(data){
                $("#userFormContainer").html(data);
            },
            error : function() {
                toastr.error("error!");
            }
        });
        }
   

//});