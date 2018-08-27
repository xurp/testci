"use strict";
//# sourceURL=main.js
//

//DOM 加载完再执行的话，改写的删除方法没办法触发，不知道为什么
//$(function() {

    var _pageSize; // 存储用于搜索

    // 根据用户名、页面索引、页面大小获取用户列表
    function getTypesByName(pageIndex, pageSize) {
         $.ajax({ 
             url: "/type", 
             contentType : 'application/json',
             data:{
                 "async":true, 
                 "pageIndex":pageIndex,
                 "pageSize":pageSize
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
        getTypesByName(pageIndex, pageSize);
        _pageSize = pageSize;
    });

    // 搜索
    $("#searchNameBtn").click(function() {
        getTypesByName(0, _pageSize);
    });

    // 获取添加用户的界面
    $("#addType").click(function() {
        $.ajax({ 
             url: "/type/add", 
             success: function(data){
                 $("#userFormContainer").html(data);
             },
             error : function(data) {
                 toastr.error("error!");
             }
         });
    });

    // 获取编辑用户的界面
    $("#rightContainer123").on("click",".blog-edit-type", function () { 
        $.ajax({ 
             url: "/type/edit/" + $(this).attr("typeId"), 
             success: function(data){
                 $("#userFormContainer").html(data);
             },
             error : function() {
                 toastr.error("error!");
             }
         });
    });

    // 提交变更后，清空表单(post)
    $("#submitEdit").click(function() {
    	var token = $("meta[name='_csrf']").attr("content");
    	var header = $("meta[name='_csrf_header']").attr("content");
    	$(document).ajaxSend(function(e, xhr, options) {
    	    xhr.setRequestHeader(header, token);
    	});
        $.ajax({ 
             url: "/type", 
             type: 'POST',
             data:$('#userForm').serialize(),
             success: function(data){
                 //$('#userForm')[0].reset();  

                 if (data.success) {
                	 $('#flipFlop').modal('hide');
                     getTypesByName(0, _pageSize);
                 } else {
                     toastr.error(data.message);
                 }

             },
             error : function() {
                 toastr.error("error!");
             }
         });
    });

    // 删除用户
    $("#rightContainer123").on("click",".blog-delete-type123", function () { 
    	var token = $("meta[name='_csrf']").attr("content");
    	var header = $("meta[name='_csrf_header']").attr("content");
    	$(document).ajaxSend(function(e, xhr, options) {
    	    xhr.setRequestHeader(header, token);
    	});
        $.ajax({ 
             url: "/type/123/" + $(this).attr("typeId") , 
             type: 'DELETE', 
             success: function(data){
                 if (data.success) {
                     getTypesByName(0, _pageSize);
                 } else {
                     toastr.error(data.message);
                 }
             },
             error : function() {
                 toastr.error("error!");
             }
         });
    });
    function deletetype(id){
    	if(window.confirm('Do you want to delete this record?')){
    		var token = $("meta[name='_csrf']").attr("content");
        	var header = $("meta[name='_csrf_header']").attr("content");
        	$(document).ajaxSend(function(e, xhr, options) {
        	    xhr.setRequestHeader(header, token);
        	});
    	$.ajax({ 
            url: "/type/" +id , 
            type: 'DELETE', 
            success: function(data){
                if (data.success) {
                    getTypesByName(0, _pageSize);
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
    function edittype(id){
    	var token = $("meta[name='_csrf']").attr("content");
    	var header = $("meta[name='_csrf_header']").attr("content");
    	$(document).ajaxSend(function(e, xhr, options) {
    	    xhr.setRequestHeader(header, token);
    	});
    	$.ajax({ 
            url: "/type/edit/" + id, 
            success: function(data){
                $("#userFormContainer").html(data);
            },
            error : function() {
                toastr.error("error!");
            }
        });
        }
   
//});