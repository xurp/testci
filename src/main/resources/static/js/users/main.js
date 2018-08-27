"use strict";
//# sourceURL=main.js
//DOM 加载完再执行的话，改写的删除方法没办法触发，不知道为什么
//DOM 加载完再执行
//$(function() {

    var _pageSize; // 存储用于搜索

    // 根据用户名、页面索引、页面大小获取用户列表
    function getUsersByName(pageIndex, pageSize) {
         $.ajax({ 
             url: "/users", 
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
        getUsersByName(pageIndex, pageSize);
        _pageSize = pageSize;
    });

    // 搜索
    $("#searchNameBtn").click(function() {
        getUsersByName(0, _pageSize);
    });

    // 获取添加用户的界面
    $("#addUser").click(function() {
        $.ajax({ 
             url: "/users/add", 
             success: function(data){
                 $("#userFormContainer").html(data);
             },
             error : function(data) {
                 toastr.error("error!");
             }
         });
    });

    // 获取编辑用户的界面
    $("#rightContainer123").on("click",".blog-edit-user", function () { 
        $.ajax({ 
             url: "/users/edit/" + $(this).attr("userId"), 
             success: function(data){
                 $("#userFormContainer").html(data);
             },
             error : function() {
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
             url: "/users", 
             type: 'POST',
             data:$('#userForm').serialize(),
             success: function(data){
                 //$('#userForm')[0].reset();  

                 if (data.success) {
                	 $('#flipFlop').modal('hide');
                     getUsersByName(0, _pageSize);
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
    $("#rightContainer123").on("click",".blog-delete-user", function () { 
    	var token = $("meta[name='_csrf']").attr("content");
    	var header = $("meta[name='_csrf_header']").attr("content");
    	$(document).ajaxSend(function(e, xhr, options) {
    	    xhr.setRequestHeader(header, token);
    	});
        $.ajax({ 
             url: "/users/" + $(this).attr("userId") , 
             type: 'DELETE', 
             success: function(data){
                 if (data.success) {
                     // 从新刷新主界面
                     getUsersByName(0, _pageSize);
                 } else {
                     toastr.error(data.message);
                 }
             },
             error : function() {
                 toastr.error("error!");
             }
         });
    });
    function deleteuser(id){
    	if(window.confirm('Do you want to delete this record?')){
    		var token = $("meta[name='_csrf']").attr("content");
        	var header = $("meta[name='_csrf_header']").attr("content");
        	$(document).ajaxSend(function(e, xhr, options) {
        	    xhr.setRequestHeader(header, token);
        	});
    	$.ajax({ 
            url: "/users/" +id , 
            type: 'DELETE', 
            success: function(data){
                if (data.success) {
                    getUsersByName(0, _pageSize);
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
    function edituser(id){
    	var token = $("meta[name='_csrf']").attr("content");
    	var header = $("meta[name='_csrf_header']").attr("content");
    	$(document).ajaxSend(function(e, xhr, options) {
    	    xhr.setRequestHeader(header, token);
    	});
    	$.ajax({ 
            url: "/users/edit/" + id, 
            success: function(data){
                $("#userFormContainer").html(data);
            },
            error : function() {
                toastr.error("error!");
            }
        });
        }
    $("#useredit").click(function() {
    	var token = $("meta[name='_csrf']").attr("content");
    	var header = $("meta[name='_csrf_header']").attr("content");
    	$(document).ajaxSend(function(e, xhr, options) {
    	    xhr.setRequestHeader(header, token);
    	});
        $.ajax({ 
             url: "/users", 
             type: 'POST',
             data:$('#userForm').serialize(),
             success: function(data){
                 $('#userForm')[0].reset();  

                 if (data.success) {
                	 alert("save successful");
                	 getUserManagement();
                 } else {
                     toastr.error(data.message);
                 }

             },
             error : function() {
                 toastr.error("error!");
             }
         });
    });
    function getUserManagement() {
        $.ajax({ 
            url: "/users/employee", 
            contentType : 'application/json',
            success: function(data){
                $("#mainContainer").html(data);
            },
            error : function() {
                toastr.error("error!");
            }
        });
   }
   
    
//});