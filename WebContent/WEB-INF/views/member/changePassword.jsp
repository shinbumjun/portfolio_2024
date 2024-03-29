<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
	$(document).ready(function(){
		var ctx = '<%= request.getContextPath() %>';
		$('#msgDiv').hide();
		$("#loading-div-background").css({ opacity: 1 });
		
		// 비밀번호 변경
		$('#btnPassword').click(function(){
			if($('#memberPw').val() == '' || $('#memberPw2').val() == ''){
				var msgTag = $('<strong>').text("모든 항목은 필수입니다.");
				$('#msgDiv').html(msgTag).show();
				return;
			}
			if($('#memberPw').val() !== $('#memberPw2').val()){ 
				var msgTag = $('<strong>').text("입력하신 비밀번호가 틀립니다.");
				$('#msgDiv').html(msgTag).show();
				return;
			}
			
			// overlay 보이기
			// 입력한 비밀번호 둘다 일치하면 이동
			$("#loading-div-background").css({'z-index' : '9999'}).show();
			var formData = new FormData(document.loginForm);
			
			$.ajax({
				url: "<c:url value='/member/change.do'/>",
				type: "POST",
				data: formData, // 데이터 보내는것
				dataType:'TEXT',
				cache: false,
				processData: false,
				contentType: false,
				success: function(data, textStatus, jqXHR) { // 서버에서 성공적으로 값을 가져오면
					data = $.parseJSON(data);
					console.log(data);
					if(data.msg == "비밀번호가 변경되었습니다"){ // msg값이 성공시
						
						// window.location.href = ctx + data.nextPage; //              http://localhost:8088/lhsmember/goLoginPage.do
						// window.location.href = data.nextPage;//                     http://localhost:8088/lhs/member/goLoginPage.do
						movePage('/member/goLoginPage.do'); // 이렇게 해야 정상적으로 동작한다         http://localhost:8088/lhs/index.do
						
						// var msgTag = $("<strong>").text(data.msg);
						// $('#msgDiv').html(msgTag).show();
						// $("#loading-div-background").hide();	// overlay 숨기기
						
					}
					else {
						// window.location.href = ctx + data.nextPage;
						// window.location.href = data.nextPage;
						movePage('/member/goPassword.do');
						
					}
				},
				error: function(jqXHR, textStatus, errorThrown) {
					$("#loading-div-background").hide();	// overlay 숨기기					
					console.log(jqXHR);
					console.log(textStatus);
					console.log(errorThrown);
				}
			});
		});
	});
</script>
</head>
<body>
	<!-- -->
	<section>
		<div class="container">
<!-- 			<h2>LOGIN</h2> -->
			<div class="row">
	
				<div class="col-md-6 offset-md-3">
	
					<!-- ALERT -->
					<div id="msgDiv" class="alert alert-mini alert-danger mb-10"></div>
					<!-- /ALERT -->
						
					<div class="box-static box-border-top p-30">
						<div class="box-title mb-30">
							<h2 class="fs-20">Change password</h2>
						</div>
	
						<form class="m-0" method="post" name="loginForm" autocomplete="off">
							<div class="clearfix">
								
								<!-- memberId -->
								<div class="form-group">
									<input type="hidden" id="memberId" name="memberId" value="${memberId}" />
								</div>
								
								<!-- email -->
								<div class="form-group">
									<input type="hidden" id="email" name="email" value="${email}" />
								</div>
								
								<!-- memberPw -->
								<div class="form-group">
									<input type="text" id="memberPw" name="memberPw" class="form-control" placeholder="Password" required="">
								</div>
								
								<!-- Password -->
								<div class="form-group">
									<input type="text" id="memberPw2" name="memberPw2" class="form-control" placeholder="password again" required="">
								</div>
									
							</div>
											
							<div class="row">
								<%--
								<div class="col-md-6 col-sm-6 col-6">
									<!-- Inform Tip -->                                        
									<div class="form-tip pt-20">
										<a class="no-text-decoration fs-13 mt-10 block" href="#">Forgot Password?</a>
									</div>
								</div>
								 --%>
<!-- 								<div class="col-md-6 col-sm-6 col-6 text-right"> -->
								<div class="col-md-12 col-sm-12 col-12 text-right">
								    <button type="button" id="btnPassword" class="btn btn-primary">비밀번호 변경</button>
								</div>
							</div>
						</form>
	
					</div>
					
					<div class="mt-30 text-center">
						<a href="javascript:movePage('/member/goRegisterPage.do')"><strong>Create Account</strong></a>
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