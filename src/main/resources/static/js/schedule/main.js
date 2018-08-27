"use strict";
//# sourceURL=main.js
 
//DOM 加载完再执行
//$(function() {

    var _pageSize; // 存储用于搜索

    // 根据用户名、页面索引、页面大小获取用户列表
    function getShiftsByName(pageIndex, pageSize) {
         $.ajax({ 
             url: "/schedule/view", 
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

    // 获取统计界面
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


    function getSetShiftDate() {
        $.ajax({ 
            url: "/schedule/setShiftDates", 
            contentType : 'application/json',
            data:{
                "async":true
                
            },
            success: function(data){
                $("#mainContainer").html(data);
            },
            error : function() {
                toastr.error("error!");
            }
        });
   }
    function getViewWorkShift() {
        $.ajax({ 
            url: "/schedule/viewWorkShift", 
            contentType : 'application/json',
            data:{
                "async":true
                
            },
            success: function(data){
                $("#mainContainer").html(data);
            },
            error : function() {
                toastr.error("error!");
            }
        });
   }
    // 提交变更后，清空表单
    $("#setDates").click(function() {
    	var token = $("meta[name='_csrf']").attr("content");
    	var header = $("meta[name='_csrf_header']").attr("content");
    	$(document).ajaxSend(function(e, xhr, options) {
    	    xhr.setRequestHeader(header, token);
    	});
        $.ajax({ 
             url: "/schedule", 
             type: 'POST',
             data:$('#userForm').serialize(),
             success: function(data){
                 $('#userForm')[0].reset();  
                 if (data.success) {
                	 getSetShiftDate();
                 } else {
                     toastr.error(data.message);
                 }

             },
             error : function() {
                 toastr.error("error!");
             }
         });
    });
    $("#submitdatesofshift").click(function() {
		console.log("sub");
    	 var saveDataAry=[];
    	 var datesize=$("#datesize").attr("size"); 
    	 var shiftsize=$("#shiftsize").attr("size");
    	 for(var j=0;j<shiftsize;j++){
    	 for(var i=0;i<datesize;i++){
    		 var c=$("#day"+i+" .shift"+j).prop("checked");
    		 var tmp=[i+"."+j,c];
    		 //console.log(tmp);
    		 saveDataAry.push(tmp);    		 
    	 }
    	 }
    	 console.log(saveDataAry);
    	 var token = $("meta[name='_csrf']").attr("content");
     	var header = $("meta[name='_csrf_header']").attr("content");
     	$(document).ajaxSend(function(e, xhr, options) {
     	    xhr.setRequestHeader(header, token);
     	});
         $.ajax({
             type:"POST",
             url:"/schedule/beginSchedule",
             dataType:"json",
             data:{dates:JSON.stringify(saveDataAry)},
             success: function(data){ 
                 if (data.success) {
                	 getViewWorkShift();
                 } else {
                     toastr.error(data.message);
                 }

             },
             error : function() {
                 toastr.error("error!");
             }
         });
    });
	$("#saveworkshift").click(function() {
    	 var saveDataAry=[];
    	 var datesize=$("#datesize").attr("size"); 
    	 var shiftsize=$("#shiftsize").attr("size");
		 var filename=$("#filename").val();
		 var shiftAry=[];
		 for(var j=0;j<shiftsize;j++){
		     shiftAry.push($("#shift"+j).text());
		 }
		 console.log(shiftAry);
		 var dateAry=[];
		 for(var j=0;j<datesize;j++){
		     dateAry.push($("#dateid"+j).text());
		 }
		 console.log(dateAry);
		 for(var i=0;i<datesize;i++){
    	 for(var j=0;j<shiftsize;j++){    	
    		 var c=$("#date"+i+"shift"+j).text().trim();
    		 saveDataAry.push(c);    		 
    	 }
    	 }
    	 console.log(saveDataAry);
    	 var token = $("meta[name='_csrf']").attr("content");
     	var header = $("meta[name='_csrf_header']").attr("content");
     	$(document).ajaxSend(function(e, xhr, options) {
     	    xhr.setRequestHeader(header, token);
     	});
         $.ajax({
             type:"POST",
             url:"/schedule/saveSchedule",
             //dataType:"json",
			 //contentType:"application/json",
			 //data:JSON.stringify(dateAry),
			     //shifts:JSON.stringify(shiftAry),
				 //saves:JSON.stringify(saveDataAry)},
             data:{
			     dates:dateAry,
				 shifts:shiftAry,
				 saves:saveDataAry,
				 filename:filename
			 },
			 traditional:true,
             success: function(data){ 
                 if (data.success) {
                	 //getViewWorkShift();
					 alert("save successful");
                 } else {
                     toastr.error(data.message);
                 }

             },
             error : function() {
                 toastr.error("error!");
             }
         });
    });
    $("#shiftview").click(function() {
    
    $("#table1").show();
    $("#table2").hide();
    $("#peopleview").show();
    $("#shiftview").hide();
    });
    $("#peopleview").click(function() {
    	$("#table2").show();
        $("#table1").hide();
        $("#shiftview").show();
        $("#peopleview").hide();
    });
    $("#editWorkShift").click(function() {
     var id=$("#savedornot").val();
   	 var saveDataAry=[];
   	 var datesize=$("#datesize").attr("size"); 
   	 var shiftsize=$("#shiftsize").attr("size");
		 var filename=$("#filename").val();
		 var groups=$("#groups").val();
		 var shiftAry=[];
		 for(var j=0;j<shiftsize;j++){
		     shiftAry.push($("#shift"+j).text());
		 }
		 console.log(shiftAry);
		 var dateAry=[];
		 for(var j=0;j<datesize;j++){
		     dateAry.push($("#dateid"+j).text());
		 }
		 console.log(dateAry);
		 for(var i=0;i<datesize;i++){
   	 for(var j=0;j<shiftsize;j++){    	
   		 //var c=$("#date"+i+"shift"+j);
   		var tmp=[];
   		$("#date"+i+"shift"+j).find("select").each(function (i) {
   			tmp.push($(this).val());
   		});
   		saveDataAry.push(["#date"+i+"shift"+j,tmp]);
   		
   		//console.log(c);
   		 //saveDataAry.push(c);    		 
   	 }
   	 }
		 console.log(saveDataAry);
   	 //console.log(saveDataAry);
		 var token = $("meta[name='_csrf']").attr("content");
	    	var header = $("meta[name='_csrf_header']").attr("content");
	    	$(document).ajaxSend(function(e, xhr, options) {
	    	    xhr.setRequestHeader(header, token);
	    	});
      $.ajax({
            type:"POST",
            url:"/schedule/editWorkShift/"+id,            
            data:{
			     dates:dateAry,
			     shifts:shiftAry, 	 
			     saves:saveDataAry,
			     filename,filename,
			     groups,groups
			 },
			 traditional:true,
            success: function(data){ 
                if (data.success) {
					 alert("save successful");
                } else {
                    toastr.error(data.message);
                }

            },
            error : function() {
                toastr.error("error!");
            }
        });
   });
    
    $(".selectall").click(function() {
    	var id=$(this).attr("allId");
    	var all=$("#all"+id).prop("checked");
    	if(all){    		
       	 $(".shift"+id).prop("checked", true);     	       
       	}
       	else {
       	  $(".shift"+id).prop("checked",  false);
        }
    });
    $(".config").click(function() {    	
    	var id=$(this).attr("buttonId");
    	$("#buttonid").val(id);
    });
    $("#addExcludeDates").click(function() {   
    	//$('#exd').append("213");
    	$('#exd').append("<label for='excludeDates' class='col-form-label col-sm-3' >exclude Dates</label> <div class='col-sm-9'><input type='text' class='form-control excludeDates'  id='excludeDates' name='excludeDates'	 th:value='*{excludeDates}'  maxlength='50' placeholder='please input excludeDates(for example:holidays)'></div>");
    });
    
    $("#submitweek").click(function() {    	
    	//var id=$(this).attr("buttonId");
    	var id=$("#buttonid").val();
    	//星期*
    	var size=$("#datesize").attr("size");    
    	var day1=$("#day1").prop("checked");
    	var day2=$("#day2").prop("checked");
    	var day3=$("#day3").prop("checked");
    	var day4=$("#day4").prop("checked");
    	var day5=$("#day5").prop("checked");
    	var day6=$("#day6").prop("checked");
    	var day7=$("#day7").prop("checked");
    	$(".shift"+id).prop("checked",true);
    	for(var i=0;i<size;i++){
        	var date=$("#dateid"+i).attr("date");
        	var week=(new Date(Date.parse(date.replace(/\-/g,"/")))).getDay();//1234560
        	if(day1==false&&week==1){
        		$("#day"+i+" .shift"+id).prop("checked", false);
        	}
        	if(day2==false&&week==2){
        		$("#day"+i+" .shift"+id).prop("checked", false);
        	}
        	if(day3==false&&week==3){
        		$("#day"+i+" .shift"+id).prop("checked", false);
        	}
        	if(day4==false&&week==4){
        		$("#day"+i+" .shift"+id).prop("checked", false);
        	}
        	if(day5==false&&week==5){
        		$("#day"+i+" .shift"+id).prop("checked", false);
        	}
        	if(day6==false&&week==6){
        		$("#day"+i+" .shift"+id).prop("checked", false);
        	}
        	if(day7==false&&week==0){
        		$("#day"+i+" .shift"+id).prop("checked", false);
        	}
    	}
    });
    $("#edittable").click(function() {
    	var id=$("#savedornot").val();
    	$.ajax({ 
    		url: "/schedule/edit/" +id , 
            success: function(data){
                $("#mainContainer").html(data);
            },
            error : function() {
                toastr.error("error!");
            }
        });
    });
    function deleteworkshift(id){
    	if(window.confirm('Do you want to delete this record?')){
    		var token = $("meta[name='_csrf']").attr("content");
        	var header = $("meta[name='_csrf_header']").attr("content");
        	$(document).ajaxSend(function(e, xhr, options) {
        	    xhr.setRequestHeader(header, token);
        	});
    	$.ajax({ 
            url: "/schedule/" +id , 
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
    function editworkshift(id){
    	$.ajax({ 
            url: "/schedule/view/" + id, 
            success: function(data){
				//main,not modal(userContainer)
                $("#mainContainer").html(data);
            },
            error : function() {
                toastr.error("error!");
            }
        });
        }
	//$("#"+b+"span").append("<select style='width:150px' id='type' name='type' class='form-control form-control-chosen'><option th:each='shiftgroupusers : ${userModel.shiftgroupusersmap['__${shift1.shiftgroup.name}__']}'th:value='${shiftgroupusers.name}' th:text='${shiftgroupusers.name}'></option></select>");
    function addemployee(b){
    	if($("#"+b+"add1").css("display")=="none"){
    		$("#"+b+"add1").show();
    	}
    	else if($("#"+b+"add2").css("display")=="none"){
    		$("#"+b+"add2").show();
    	}
    	else if($("#"+b+"add3").css("display")=="none"){
    		$("#"+b+"add3").show();
    	}
    }

//});