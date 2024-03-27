<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>스토리</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <style>
        .intro p {
            font-size: 18px; /* 텍스트 크기를 조금 키웁니다. */
        }
        .highlight {
            font-weight: bold; /* 강조할 부분을 굵게 표시합니다. */
            color: #007bff; /* 강조할 부분의 색상을 변경합니다. */
        }
    </style>
</head>
<body>

<div class="container">
    <h1>★자기소개</h1>

    <div class="intro">
        <p>
		            안녕하세요. 제 이름은 신범준이고, 95년 생으로 나이는 28살입니다.<br/>
		            제 소개를 하자면 <span class="highlight">정보통신 공학 학사</span>와 교육으로는 [K-디지털 트레이닝] 웹 서비스 기반 빅데이터 분석 및 <br/>
		            개발자 양성과정<span class="highlight"> 국비지원 교육</span>과 <span class="highlight">부트캠프</span> 총 2번의 교육을 받았습니다<br/><br/>
		            
		            국비지원 학원에서 <span class="highlight">자바을 이용한 스프링 프로젝트 3달 + 파이썬을 이용한 빅데이터 프로젝트 3달</span> <br/>
		            이렇게 배우고 총 6개월을 배우고 나서 (주)에이아이커넥트라는 회사에 입사를 하여 <br/>
		     <span class="highlight">실업급여 챗봇 / 외교부 콜센터 유지 보수에 관련된 일</span>을 하였습니다.<br/><br/>
		     
		            그래도 나름 국비에서 <span class="highlight">팀을 이끄는 팀장</span>을 맡았고 팀원들 끼리의 마찰과 각각이 구현할 파트를 분담해주고 조율하였습니다<br/>
		            그리고 제 파트인 <span class="highlight">커뮤니티 구현</span>과 발표도 하고 스프링 프로젝트를 잘 마무리하였기 때문에 팀장으로써 좋을 역할을 하였습니다 <br/>
		            그러나 회사를 다녀보고 실상을 열어보니 열심히 하는 척을 하였던것 같았습니다<br/>
		            코드를 보고 코드의 흐름을 볼 줄 몰랐고 에러가 터지면 브라우저랑 매칭해서 <br/>
		            어느 부분의 코드가 잘못되었고 이유는 또 뭔지 그리고 코드에 대해서 잘 설명을 할 줄 몰랐습니다<br/>
		            기본적인 콘솔창 확인, 개발자 도구 확인, <span class="highlight">System.out.println(), console.log(), alert()</span> 메시지를 찍으면서 확인을 못했었습니다<br/>
		            그리고 제일 중요한 <span class="highlight">개발자로서 고객한테 요구받은 요구사항을 제대로 구현할 줄 모르는 개발자</span>가 되어 있었습니다<br/><br/>
		            
		            그렇게 기본기가 없는 상태에서 성장하기는 힘들었고 회사 경영상 필요 및 회사불황의 이유로 권고사직을 당했습니다 <br/><br/>
		            
		            저는 요구사항을 제대로 구현할 줄 아는 개발자가 되고 싶었기 때문에<br/>
		            처음부터 제대로 공부하자는 마음으로 유투브의 자바의 정석 영상과 책을 보며 공부하였고 <br/>
		     <span class="highlight">더 나아가 자바 스터디를 열어 자바에 대해서 공부를 하는 도중</span><br/>
		            유튜브로 강의를 하는 강사분이 <span class="highlight">정석코딩 9기</span>를 모집한다고 해서 참여를 하였습니다<br/>
		            중간에 폐강이 되어서 같이 공부한 9기 맴버들과 공유 오피스를 빌리고 서로 소통하며 각자 개인의 프로젝트를 만들었습니다<br/>
		            
		            그 후로 완벽하다고는 못하지만 내가 원하는 기능들 <span class="highlight">비밀번호 찾기라든가 댓글 기능 구현</span>등<br/>
		            내가 구현하고 싶은 기능들을 스스로 구현해 내었고 <br/>
		            중요한 것은 짧게 미친듯이 열심히 불태워서 하는게 아니라 꾸준히 <br/>
		     <span class="highlight">그냥... 하는게 중요</span>한다는 걸 깨달았습니다<br/><br/>
        </p>
    </div>

	<div class="experience">
	    <h2 class="section-title">★중요하다고 생각하는 것 정리</h2>
	    <p>
	    <h3><span class="highlight">1. 프로젝트 설정, 생성 및 파일 위치</span></h3><br/>
		    1-1) Dynamic Web Project 시작 <br/>
		    1-2) web.xml 파일을 설정하여 DispatcherServlet 등록<br/>
		    1-3) index.jsp 생성하기 - WebContent<br/>
		    1-4) Maven 프로젝트로 전환 및 pom.xml 추가 (의존성 문제를 효율적으로 해결하기 위해 Maven 프로젝트로 전환)<br/>
		    1-5) WEB-INF 디렉토리에 spring-servlet.xml 만들기<br/>
		    1-6) 패키지 생성과 @Controller 매핑을 한 class 만들고 정상적으로 돌아가는지 확인해보기<br/>
		    1-7) spring-servlet.xml 설정한 곳으로 매핑을 해서 브라우저에 출력해보기<br/>
		    1-8) LoginController 만들기<br/>
		    1-9) 로그인 폼 가져와서 로그인 화면 만들어보기 <br/>
		    1-10) 게시판 브라우저 영역 만들기 - (폴더 위치와 Controller 매핑해보기)<br/>
		    1-11) 쿠키와 세션 이용해서 로그인 구현하기 (실습 해본 것)<br/>
		    1-12) redirect와 forward 이용해서 로그인 유무에 따라 페이지 이동 구현하기<br/><br/>

	    <h3><span class="highlight">2. 포트폴리오 구현한 기능들</span></h3><br/>
			2-1) <span class="highlight">회원가입</span><br/>
	        2-2) 회원가입시 중복 ID확인<br/>
	        2-3) 쿠키, 세션 구현하기 (로그인 후 /index.do 이동)<br/>
	        2-4) <span class="highlight">회원가입 완료시 가입 축하 이메일</span> (노션 링크 보내기 구현)<br/>
	        2-5) <span class="highlight">이메일을 통해서 비밀번호 찾기 → 변경하기</span><br/>
	        2-6) <span class="highlight">아마존 웹 서비스(AWS) 계정 생성  (배포)</span><br/>
	        2-7) <span class="highlight">파일 업로드 수업</span><br/>
	        2-8) <span class="highlight">게시글 작성(첨부)</span><br/>
	        2-9) <span class="highlight">자유 게시판 업로드 + 파일 업로드</span>(+작성자 닉네임 보이게하기, 파일 사이즈가 0이면 업로드x)<br/>
	        2-10) <span class="highlight">리스팅(페이징 + 검색기능 구현)</span><br/>
	        2-11) <span class="highlight">게시글 읽기</span> (목록 버튼을 누르면 해당 페이지를 보여주는 게시판 위치로)<br/>
	        2-12) has_file : 파일이 있냐 없냐에 따라서 Y/N r 구현 (BoardSeq, TypeSeq값을 가지고 hasFile 수정)<br/>
	        2-13) <span class="highlight">게시글 삭제</span>(본인이 작성한 글에 대해서만, 성공하면 게시판으로 취소하면 원래 게시글로)<br/>
	        2-14) <span class="highlight">게시글 수정</span> (본인이 작성한 글에 대해서만 + 수정을 누르면 성공하던 실패하던 현재 게시글로)<br/>
	        2-15) 해당 게시글을 읽을 경우 조회수 +1 (연습 용으로 해봄)<br/>
	        2-16) 비밀번호 찾기 (에러 잡기!!!)<br/>
	        2-17) <span class="highlight">첨부파일 (업로드, 다운로드, 삭제x, 수정x) </span><br/>
	        2-18) 로그인했을 경우만 게시판 이동 가능x<br/>
	        2-19) <span class="highlight">검색 강의</span><br/>
	        2-20) 회원가입시 로그인ID + 비밀번호 + 이메일 유효성 검사와 기타 필드 150자 이하<br/>
	        2-21) <span class="highlight">REST API와 Ajax 이론 공부</span><br/>
	        2-22) <span class="highlight">댓글 기능구현</span><br/>
	        2-23) 화면단 구성하기+MySQL Workbench에서 ERD<br/>
	        2-24) <span class="highlight">공지사항 / REST API 형식으로 수정</span> <span class="highlight" style="color: red;">구현중</span><br/>
	        2-25) 인증번호는 테이블에 저장 / 수정 → 첨부파일 삭제, 첨부파일 수정  <br/><br/>
	
	    <h3><span class="highlight">3. AWS 배포</span></h3><br/>
			포트폴리오 프로젝트의 배포를 하기 위해서 <br/>
			Amazon Web Services(AWS)의 주요 서비스인 EC2와 RDS을 공부했습니다. <br/><br/>
			
			<h3><span class="highlight">4. Rest API 전환</span> <span class="highlight" style="color: red;">구현중</span></h3><br/>
			@RequestMapping ->  REST API 형식에 맞게 구현<br/>
			각 리소스에 대한 CRUD(Create, Read, Update, Delete) <br/>
			작업은 HTTP 메서드(GET, POST, PUT, DELETE 등으로 매핑)입니다.<br/><br/>
	    </p>
	</div>
	
</div>

</body>
</html>
