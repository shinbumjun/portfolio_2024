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
		
		// 예외1) 다른 페이지로 갈 경우 초기화
		
		// ***카운터 변수 초기화
		var counter = 60;
		var interval; // interval 변수를 전역으로 선언하여 clearInterval()을 호출할 때 사용합니다.

		// ***인증 확인 버튼 옆에 카운트 다운 업데이트하는 함수
		function updateCountdown() {
		    $('#countdown').text(counter);
		    if (counter <= 0) {
		        clearInterval(interval); // 카운터가 0 이하이면 interval을 멈춥니다.
		        $('#countdown').text(""); // 카운트 다운이 0이 되면 메시지를 지웁니다.
		        $('#btnLogin').prop('disabled', false); // 인증번호 받기 버튼 활성화
		        // alert("인증번호를 다시 받으세요!"); // 카운트가 0이 되면 해당 메시지를 출력합니다 
		    } else {
		        counter--;
		    }
		}
		
		
		// 인증 확인을 누르면 ->
		$('#btnVerify').click(function(){
		    var verificationCode = $('#verificationCode').val();
		    var memberId = $('#memberId').val(); // memberId 값 가져오기
		    console.log(memberId)
	        var email = $('#email').val(); // email 값 가져오기
	        console.log(email)
	        
		    if(verificationCode == ''){
		        var msgTag = $('<strong>').text("인증번호를 입력해주세요");
		        $('#msgDiv').html(msgTag).show();
		        return;
		    }
		    
		    // 세션에 저장된 인증번호 확인
		    // ********************** 인증이 되면 email_auth 테이블 insert 하기 **********************
		    // -> 먼저 서버에 요청하여 세션에서 인증번호를 받아옴 
		    // -> 클라이언트에서 사용자가 입력한 인증번호와 세션에서 받아온 인증번호를 비교
		    // -> 비교 결과에 따라 추가적인 동작을 수행
		    
		    // /member/getSessionNumbe : 세션에 저장된 임시 인증번호를 가져오는 컨트롤러 메서드
		    // ********************** 서버로 요청 전에 입력한 인증번호를 확인하여 유효한 경우에만 서버로 전송
		    $.ajax({
		        url: '<%= request.getContextPath() %>/member/getSessionNumber.do',
		        type: 'GET',
		        dataType: 'text',
		        data: {
		            memberId: memberId,
		            email: email,
		            verificationCode: verificationCode // 유효한 경우에만 전송
		        },
		        success: function(sessionVerificationCode) {
		            console.log("서버에서 받아온 세션 인증번호: " + sessionVerificationCode);
		            if(sessionVerificationCode == verificationCode) { 
		            	
		            	// $.ajax
		            	
		            	
		            	var memberId = $('#memberId').val(); // memberId 값 가져오기 찍힘
		    		    console.log("memberId : " + memberId)
		    	        var email = $('#email').val(); // email 값 가져오기 찍힘
		    	        console.log("email : " + email)

		             	// 인증번호가 일치하면 비밀번호 변경 페이지로 이동 
		                // movePage('/member/changePassword.do'); // 수정된 부분
		                movePage('/member/changePassword.do?memberId=' + memberId + '&email=' + email); // 수정된 부분
		                console.log("인증번호가 일치합니다!")
		                alert("인증번호가 일치합니다!")
		            } else {
		                // 인증번호가 일치하지 않을 경우 메시지 출력
		                var msgTag = $('<strong>').text("인증번호가 일치하지 않습니다");
		                $('#msgDiv').html(msgTag).show();
		                
		                console.log("인증번호가 일치하지 않습니다!")
		                alert("인증번호가 일치하지 않습니다!")
		            }
		        },
		        error: function(xhr, status, error) {
		            console.error("세션 값 가져오기 실패: " + error);
		        }
		    });
		});
	    
		
		// // 인증번호 받기 버튼 클릭시 카운터 시작
		$('#btnLogin').click(function(){
			if( $('#memberId').val() == '' || $('#email').val() == '' ){
				var msgTag = $('<strong>').text("모두 입력해주세요");
				$('#msgDiv').html(msgTag).show();
				return;
			}
			
			// overlay 보이기
			$("#loading-div-background").css({'z-index' : '9999'}).show();
			var formData = new FormData(document.loginForm);
			$.ajax({
				url: "<c:url value='/member/password.do'/>",
				type: "POST",
				data: formData,
				dataType:'TEXT',
				cache: false,
				processData: false,
				contentType: false,
				success: function(data, textStatus, jqXHR) {
					data = $.parseJSON(data);
					console.log(data);
					if(data.msg != undefined && data.msg != '' && data.msg !== "비밀번호 찾기 실패!"){
						var msgTag = $("<strong>").text(data.msg);
						$('#msgDiv').html(msgTag).show();
						$("#loading-div-background").hide();	// overlay 숨기기
						
						// map.put("msg", "인증번호가 발송되었습니다");  일때만 ***카운터 시작!
					    counter = 60; // 카운터 초기화
					    $('#btnLogin').prop('disabled', true); // 인증번호 받기 버튼 비활성화
					    interval = setInterval(function() { // interval 변수에 setInterval()을 할당하여 전역에서 clearInterval()을 사용할 수 있도록 합니다.
					        updateCountdown();
					    }, 1000); // 1초 간격으로 업데이트하도록 수정
					    
					}
					else {
						movePage('/member/goPassword.do'); // 바로 movePage로 이동하기 때문에 뒤에 메시지는 확인이 안됨
						
						var msgTag = $('<strong>').text("잘못 입력하셨습니다");
						$('#msgDiv').html(msgTag).show(); // 안찍히는 값
						
						// var msgTag = $("<strong>").text(data.msg);
						// $('#msgDiv').html(msgTag).show();
						// $("#loading-div-background").hide();	// overlay 숨기기
						
						// console.log("잘못 입력하셨습니다!")
						// alart("잘못 입력하셨습니다!")
						// window.location.href = ctx + data.nextPage;
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
<!-- 			<h2>PASSWORD?</h2> -->
			<div class="row">
	
				<div class="col-md-6 offset-md-3">
	
					<!-- ALERT -->
					<div id="msgDiv" class="alert alert-mini alert-danger mb-10"></div>
					<!-- /ALERT -->
						
					<div class="box-static box-border-top p-30">
						<div class="box-title mb-30">
							<h2 class="fs-20">PASSWORD?</h2>
						</div>
	
						<form class="m-0" method="post" name="loginForm" autocomplete="off">
							<div class="clearfix">
								
								<!-- memberId -->
								<div class="form-group">
									<input type="text" id="memberId" name="memberId" class="form-control" placeholder="USER ID" required="">
								</div>
								
								<!-- email -->
								<div class="form-group">
									<input type="text" id="email" name="email" class="form-control" placeholder="email" required="">
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

								<!-- 인증번호 입력 -->
								<!-- 인증번호 확인 버튼 -->
								<!-- 
									인증번호 받기 버튼을 누르면 #btnLogin 통해서 모든 값을 확인하지만
									인증 확인 버튼을 누르면 #btnVerify 통해서 받은 값을 세션에 저장된 값이랑 확인
								 -->
								<div class="col-md-12 col-sm-12 col-12 text-right">
									<!-- 카운트 다운을 표시할 요소 -->
    								<span id="countdown" style="margin-right: 10px;"></span> <!-- 여기에 추가 -->
    								
									<input type="text" id="verificationCode" name="verificationCode" class="form-control" placeholder="인증번호" required=""><br/>
								    <button type="button" id="btnVerify" class="btn btn-primary">인증 확인</button>
								    <button type="button" id="btnLogin" class="btn btn-primary">인증번호 받기</button>
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