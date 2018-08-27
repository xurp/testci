/**
 * 
 */
function getLaborlaw() {	
         $.ajax({ 
             url: "/laborlaw", 
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
$("#submitLaborlaw").click(function() {
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$(document).ajaxSend(function(e, xhr, options) {
	    xhr.setRequestHeader(header, token);
	});
        $.ajax({ 
             url: "/laborlaw", 
             type: 'POST',
             data:$('#userForm').serialize(),
             success: function(data){
                 if (data.success) {
                	 alert("save successful");
                	 getLaborlaw();
                 } else {
                     toastr.error(data.message);
                 }

             },
             error : function() {
                 toastr.error("error!");
             }
         });
    });