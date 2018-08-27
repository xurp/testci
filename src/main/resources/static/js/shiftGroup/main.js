"use strict";
//# sourceURL=main.js
//

//DOM 加载完再执行的话，改写的删除方法没办法触发，不知道为什么
//$(function() {

    var _pageSize; // 存储用于搜索

    // 根据用户名、页面索引、页面大小获取用户列表
    function getShiftGroupsByName(pageIndex, pageSize) {
         $.ajax({ 
             url: "/shiftgroup", 
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
    	getShiftGroupsByName(pageIndex, pageSize);
        _pageSize = pageSize;
    });

    // 搜索
    $("#searchNameBtn").click(function() {
    	getShiftGroupsByName(0, _pageSize);
    });

    // 获取添加用户的界面
    $("#addShiftGroup").click(function() {
        $.ajax({ 
             url: "/shiftgroup/add", 
             success: function(data){
                 $("#userFormContainer").html(data);
             },
             error : function(data) {
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
             url: "/shiftgroup", 
             type: 'POST',
             data:$('#userForm').serialize(),
             success: function(data){
                 //$('#userForm')[0].reset();  

                 if (data.success) {
                	 $('#flipFlop').modal('hide');
                     getShiftGroupsByName(0, _pageSize);
                 } else {
                     toastr.error(data.message);
                 }

             },
             error : function() {
                 toastr.error("error!");
             }
         });
    });

   
    function deleteShiftGroup(id){
    	if(window.confirm('Warning! If you delete the shiftGroup, all the shifts of this shiftGroup will be deleted, too!')){
    		var token = $("meta[name='_csrf']").attr("content");
        	var header = $("meta[name='_csrf_header']").attr("content");
        	$(document).ajaxSend(function(e, xhr, options) {
        	    xhr.setRequestHeader(header, token);
        	});
    	$.ajax({ 
            url: "/shiftgroup/" +id , 
            type: 'DELETE', 
            success: function(data){
                if (data.success) {
                    getShiftGroupsByName(0, _pageSize);
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
    function editShiftGroup(id){
    	var token = $("meta[name='_csrf']").attr("content");
    	var header = $("meta[name='_csrf_header']").attr("content");
    	$(document).ajaxSend(function(e, xhr, options) {
    	    xhr.setRequestHeader(header, token);
    	});
    	$.ajax({ 
            url: "/shiftgroup/edit/" + id, 
            success: function(data){
                $("#userFormContainer").html(data);
            },
            error : function() {
                toastr.error("error!");
            }
        });
        }
   
//});