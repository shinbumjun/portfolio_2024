<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">

function isValidEmail(email) {
    // 이메일 주소를 검증하는 정규식
    var emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailRegex.test(email);
}

function isValidPassword(password) {
    // 비밀번호를 검증하는 정규식
    var passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()\-_=+\\\|\[\]{};:'",.<>?]).{8,}$/;
    return passwordRegex.test(password);
}

$(document).ready(function(){
	$('#msgDiv').hide();
	$("#loading-div-background").css({ opacity: 1 });
	$('#btnSignUp').click(function(e){
		if( $('#memberId').val() == '' || $('#memberName').val() == '' || $('#pwAgain').val() == '' 
				|| $('#memberNick').val() == '' || $('#memberPw').val() == '' || $('#email').val() == ''){
			var msgTag = $('<strong>').text("모든 항목은 필수입니다.");
			$('#msgDiv').html(msgTag).show();
			e.preventDefault();
			return;
		}
		
		// Check if email is valid (이메일 유효성 검사)
		if (!isValidEmail($('#email').val())) {
		    var msgTag = $('<strong>').text("유효한 이메일 주소를 입력하세요.");
		    $('#msgDiv').html(msgTag).show();
		    e.preventDefault();
		    return;
		}
		
		// Check if password is valid (비밀번호 유효성 검사)
        if (!isValidPassword($('#memberPw').val())) {
            var msgTag = $('<strong>').text("비밀번호는 소문자, 대문자, 숫자, 특수문자 포함하여 8자 이상이어야 합니다.");
            $('#msgDiv').html(msgTag).show();
            e.preventDefault();
            return;
        }
		
		// Check if passwords match (비밀번호, 비밀번호 확인 같은지 확인)
        if ($('#memberPw').val() != $('#pwAgain').val()) {
            var msgTag = $('<strong>').text("비밀번호가 일치하지 않습니다.");
            $('#msgDiv').html(msgTag).show();
            e.preventDefault();
            return;
        }
		
     	// Check if ID length is at least 7 characters (ID 7글자 이상)
        if ($('#memberId').val().length < 7) {
            var msgTag = $('<strong>').text("ID는 7글자 이상이어야 합니다.");
            $('#msgDiv').html(msgTag).show();
            e.preventDefault();
            return;
        }
		
     	// Check if each input does not exceed 150 characters (150글자 이상x)
        if ($('#memberId').val().length > 150 || $('#memberName').val().length > 150 || $('#memberNick').val().length > 150 || $('#memberPw').val().length > 150 || $('#pwAgain').val().length > 150 || $('#email').val().length > 150) {
            var msgTag = $('<strong>').text("각 입력란은 150글자 이하로 입력해야 합니다.");
            $('#msgDiv').html(msgTag).show();
            e.preventDefault();
            return;
        }
     	
		// overlay 보이기
		$("#loading-div-background").css({'z-index' : '9999'}).show();
		
		$.ajax({
			url: "<c:url value='/member/checkId.do'/>",
			data : {memberId : $('#memberId').val() },
			success : function (data, textStatus, XMLHttpRequest) {
				console.log(data);
				if(data.cnt == 1){
// 					alert(data.msg);
					var msgTag = $('<strong>').text(data.msg);
					$('#msgDiv').show().html(msgTag);
					$('#memberId').focus();
					// overlay 숨기기
					$("#loading-div-background").hide();
					return;
				}
				else {
					var formData = new FormData(document.regForm);
// 					var formData = $("#registerForm").serialize();
					console.log(formData);
					$.ajax({
						url: '<c:url value="/member/join.do" />',
						type: "POST",
						data: formData,
						dataType:'TEXT',
						cache: false,
						processData: false,
						contentType: false,
						success: function(data, textStatus, jqXHR) {
							data = $.parseJSON(data);
// 							console.log(data);
							alert(data.msg);

							$("#loading-div-background").hide();	// overlay 숨기기
							
							movePage(data.nextPage);
						},
						error: function(jqXHR, textStatus, errorThrown) {
							$("#loading-div-background").hide();	// overlay 숨기기
							
							console.log(jqXHR);
							console.log(textStatus);
							console.log(errorThrown);
						}
					});
				}
			},
			error : function (XMLHttpRequest, textStatus, errorThrown) {
				$(tabId, myLayout.panes.center).html(XMLHttpRequest.responseText);
			}
		});
	});
});
</script>
</head>
<body>
<section>
	<div class="container">
		<div class="row">
			<div class="col-md-6 offset-md-3">
				<!-- ALERT -->
				<div id="msgDiv" class="alert alert-mini alert-danger mb-10"></div>
				<!-- /ALERT -->
					
				<div class="box-static box-border-top p-30">
					<div class="box-title mb-30">
						<h2 class="fs-20">REGISTER</h2>
					</div>

					<form class="m-0 sky-form" name="regForm" method="post" enctype="multipart/form-data">
						<fieldset>

							<div class="row">

								<div class="col-md-6 col-sm-6">
									<label>ID *</label>
									<label class="input mb-10">
										<i class="ico-append fa fa-user"></i>
										<input required="" type="text" id="memberId" name="memberId"/>
										<b class="tooltip tooltip-bottom-right">Your ID</b>
									</label>
								</div>

								<div class="col-md-6 col-sm-6">
									<label for="register:last_name">Name *</label>
									<label class="input mb-10">
										<i class="ico-append fa fa-user"></i>
										<input required="" type="text" id="memberName" name="memberName" />
										<b class="tooltip tooltip-bottom-right">Your Full Name</b>
									</label>
								</div>
							</div>
							
							<div class="row">
								<div class="col-md-6 col-sm-6">
									<label for="register:pass1">Password *</label>
									<label class="input mb-10">
										<i class="ico-append fa fa-lock"></i>
										<input required="" type="password" class="err" id="memberPw" name="memberPw" />
										<b class="tooltip tooltip-bottom-right">Min. 6 characters</b>
									</label>
								</div>

								<div class="col-md-6 col-sm-6">
									<label for="register:pass2">Password Again *</label>
									<label class="input mb-10">
										<i class="ico-append fa fa-lock"></i>
										<input required="" type="password" id="pwAgain" class="err">
										<b class="tooltip tooltip-bottom-right">Type the password again</b>
									</label>
								</div>

							</div>
							
							<div class="row">
								<div class="col-md-6 col-sm-6">
									<label for="register:phone">Nick *</label>
									<label class="input mb-10">
										<i class="ico-append fa fa-phone"></i>
										<input required="" type="text" id="memberNick" name="memberNick" />
										<b class="tooltip tooltip-bottom-right">Your Nick</b>
									</label>
								</div>
								
								<div class="col-md-6 col-sm-6">
									<label for="register:email">Email *</label>
									<label class="input mb-10">
										<i class="ico-append fa fa-envelope"></i>
										<input required="" type="text" id="email" name="email" />
										<b class="tooltip tooltip-bottom-right">Your Email</b>
									</label>
								</div>
							</div>
							<%--
							<hr />
							<label class="checkbox m-0"><input class="checked-agree" type="checkbox" name="checkbox"><i></i>I agree to the <a href="#" data-toggle="modal" data-target="#termsModal">Terms of Service</a></label>
							<label class="checkbox m-0"><input type="checkbox" name="checkbox"><i></i>I want to receive news and  special offers</label>
							 --%>
						</fieldset>

						<div class="row">
							<div class="col-md-12 col-sm-12 col-12 text-right">
								<button id="btnSignUp" type="button" class="btn btn-primary"><i class="fa fa-check"></i> REGISTER</button>
							</div>
						</div>

					</form>
				</div>
			</div>
		</div>
	</div>
</section>
<!-- / -->
<!-- overlay html start -->
<div id="loading-div-background">
      <div id="loading-div" class="ui-corner-all">
           <img style="height:32px;width:32px;margin:30px;" src="<c:url value='/resources/please_wait.gif'/>" alt="Loading.."/>
           <br>
           PROCESSING. PLEASE WAIT...
      </div>
</div>
<!-- overlay html end -->
</body>
</html>