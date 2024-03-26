<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
</head>

	<body class="smoothscroll enable-animation">

			
			<%-- 내용 나올 div 시작!!!! --%>
			<section class="alternate">
				<div class="container">

					<div class="row">

						<div class="col-md-3">

							<div class="box-icon box-icon-center box-icon-round box-icon-transparent box-icon-large box-icon-content" style="width:100%;
	height:350px;">
								<div class="box-icon-title">
								    <i class="b-0 fa fa-tablet"></i>
								    <h2><a href="https://github.com/shinbumjun/portfolio_2024">github 주소</a></h2>
								    <p></p>
								</div>
							
							<!-- 
								이 버튼은 모달 창을 열도록 설정 -> myModal
								data-toggle="modal" 속성과 data-target="#myModal" 속성이 추가되어 있습니다. 
								이는 해당 버튼을 클릭했을 때 myModal이라는 ID를 가진 모달 창이 열리도록 하는 역할
							-->
									<button type="button" class="btn btn-default btn-lg lightbox" data-toggle="modal" data-target="#myModal">
										DB 모델링 (IMG)
									</button> <br><br/>
								
								<a href="<c:url value='/file/downloadERD.do'/>">
									<button type="button" class="btn btn-default btn-lg lightbox" data-toggle="modal">
										ERD 다운로드 (MWB)
									</button>
								</a>	
								<br/>	
							</div>

						</div>

						<div class="col-md-3">

							<div class="box-icon box-icon-center box-icon-round box-icon-transparent box-icon-large box-icon-content">
								<div class="box-icon-title">
									<i class="b-0 fa fa-random"></i>
									<a href="https://www.notion.so/jsp-javaScript-c62f08367a4742d29a25e08140cf4a0b?pvs=4"><h4>jsp & javaScript<br/>문제 풀이</h4></a>
								</div>
								<p><br/>
									<a href="https://www.notion.so/Singleton-ee8d69a04357460aab0467d3f13fec2e?pvs=4">Singleton패턴 학습</a><br/>
									<a href="https://www.notion.so/jsp-javaScript-685dd9eb75684890be7786c47cf88a40?pvs=4">jsp & javaScript 요약 공부</a><br/>
									<a href="https://www.notion.so/jsp-javaScript-599a253ea2474610a1c71a454893b044?pvs=4">jsp & javaScript 과제</a><br/>
									<a href="https://www.notion.so/d16c396157ae40eb95459ec26dbedd63?pvs=4">오류 작성 (notion 최종본 참고)</a><br/>
									<a href="https://www.notion.so/29d0fdf8356d45929cc0a332272b442e?pvs=4">파일 업로드 수업</a><br/>
								</p>
							</div>

						</div>

						<div class="col-md-3">

							<div class="box-icon box-icon-center box-icon-round box-icon-transparent box-icon-large box-icon-content">
								<div class="box-icon-title">
									<i class="b-0 fa fa-tint"></i>
									<a href="https://www.notion.so/5-832c527f40b34b61ae62aaee7ed19ad4?pvs=4"><h4>기본 설정<br/>회원가입<br/>로그인  최종본</h4></a>
								</div>
								<p><br/>
									<a href="https://www.notion.so/1-ae4e16f7452b4039b7ce15d45dc657d7?pvs=4">과제1 정리</a><br/>
									<a href="https://www.notion.so/2-3-74bd1d5f772c41709336935dc7270657?pvs=4">과제2,3 정리</a><br/>
									<a href="https://www.notion.so/4-a765dd5747f44c219cdf02867a7662d1?pvs=4">과제4 정리</a><br/>
								</p>
							</div>

						</div>

						<div class="col-md-3">

							<div class="box-icon box-icon-center box-icon-round box-icon-transparent box-icon-large box-icon-content">
								<div class="box-icon-title">
									<i class="b-0 fa fa-cogs"></i>
									<a href="https://www.notion.so/portfolio_2024-accdde7bd46a43f6b405d7453738f13b?pvs=4"><h4>notion 최종본</h4></a>
								</div>
								<p><br/>
								    <a href="https://www.notion.so/0bc5fa0384624d729130df53b5e448ca?pvs=4">개인 프로젝트 정리1</a><br/>
								    <a href="https://www.notion.so/2-002b78fb4e854079bcb39c10d3c22f1c?pvs=4">개인 프로젝트 정리2</a><br/>
								    <a href="https://www.notion.so/3-6b58e0f107e6462c8b4ca99ca63db379?pvs=4">개인 프로젝트 정리3</a><br/>
								    <a href="https://www.notion.so/4-8b5d3bfd733e49a89e439ef3a640f351?pvs=4">개인 프로젝트 정리4</a><br/>
								    <a href="https://www.notion.so/5-1ff57a7a5e4c4fbeb475b64ad4b65b27?pvs=4">개인 프로젝트 정리5</a><br/>
								</p>
							</div>

						</div>

					</div>


				</div>
				
				
					<!-- ************************img modal content -->
					<div id="myModal" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
						<div class="modal-dialog modal-lg">
							<div class="modal-content">

								<!-- Modal Header -->
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
									<h4 class="modal-title" id="myModalLabel">ERD</h4>
								</div>

								<!-- Modal Body -->
								<div class="modal-body">
									
									<!-- /resources/portfolio_ERD.png(수정 전) S_shinbumjun.png(수정 후) -->
									<img id="erdImg" width="100%" src="<c:url value='/resources/S_shinbumjun.png'/>"/>

								<!-- Modal Footer -->
								<div class="modal-footer">
									<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
								</div>

							</div>
						</div>
					</div> <!-- img modal content -->

					
				</div>
				
				
			</section>
			<!-- / -->

	

		
	</body>
</html>