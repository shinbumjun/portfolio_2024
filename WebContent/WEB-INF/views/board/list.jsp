<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script type="text/javascript">
$(document).ready(function(){	
	
	// 검색 버튼 클릭 이벤트 처리
	$('#searchButton').on('click', function(event){      
	    
	    // 검색 옵션과 검색어 가져오기
	    var option = $('.search-option').val();
	    var keyword = $('.search-input').val();
	    
	    // AJAX 호출을 통해 데이터를 검색
	    customAjax('<c:url value="/board/list.do"/>', 
	    		"/board/list.do?page=${ph.sc.page}&pageSize=${ph.sc.pageSize}&option=" + option + "&keyword=" + encodeURIComponent(keyword));
	});
	   
	function customAjax(url, responseUrl) {
	    var formData = $('#searchForm').serialize(); // 폼 데이터를 직렬화하여 가져옴
	    $.ajax({
	        url : url,
	        data : formData,
	        type : 'POST',
	        dataType : "text",
	        success : function (result, textStatus, XMLHttpRequest) {
	            var data = $.parseJSON(result);
	            alert(data.msg);
	            var boardSeq = data.boardSeq;
	            console.log("result내용 : " + result);
	            console.log("textStatus내용 : " + textStatus);
	            console.log("XMLHttpRequest내용 : " + XMLHttpRequest);
	            
	            movePage(responseUrl); // 성공하면 게시판으로
	        },
	        error : function (XMLHttpRequest, textStatus, errorThrown) {
	            alert("에러 발생\n관리자에게 문의바랍니다.");
	            console.log("에러\n" + XMLHttpRequest.responseText);
	        }
	    });
	}  
});
</script>

</head>
<body>
<section>
	<div class="container">
		<h4>자유게시판</h4>
		
		<!-- 검색 기능 -->
		<div class="search-container">
		    <form id="searchForm" class="search-form" method="post">
		        <select class="search-option" name="option">
		            <option value="A" ${ph.sc.option=='A' || ph.sc.option=='' ? "selected" : ""}>제목+내용</option>
		            <option value="T" ${ph.sc.option=='T' ? "selected" : ""}>제목만</option>
		            <option value="W" ${ph.sc.option=='W' ? "selected" : ""}>작성자</option>
		        </select>
		
		        <input type="text" name="keyword" class="search-input" type="text" value="${ph.sc.keyword}" placeholder="검색어를 입력해주세요">
		        <input type="button" id="searchButton" class="search-button" value="검색">
		    </form>
		</div>


		<div class="table-responsive">
			<table class="table table-sm">
				<colgroup>
					<col width="10%" />
					<col width="35%" />
					<col width="10%" />
					<col width="8%" />
					<col width="8%" />
					<col width="15%" />
				</colgroup>
				
				<thead>
					<tr>
						<th class="fw-30" align="center">&emsp;&emsp;번호</th>
						<th align="center">&emsp;&emsp;제목</th>
						<th align="center">글쓴이</th>
						<th align="center">조회수</th>
						<th align="center">첨부파일</th>
						<th align="center">작성일</th>
					</tr>
				</thead>
				
				<tbody>
                    <c:forEach var="list" items="${boardlist}" varStatus="board">
                        <tr>
                            <td class="text-center">${list.boardSeq}</td>
                            <!-- 게시글 제목을 클릭하면 해당 게시글로 이동하도록 링크 추가 -->
           					<td><a href="javascript:movePage('/board/read.do?boardSeq=${list.boardSeq}&page=${ph.sc.page}&pageSize=${ph.sc.pageSize}')">${list.title}</a></td>
                            <td>${list.memberId}</td>
                            <td>${list.hits}</td>
                            <td>${list.hasFile}</td>
                            <td>${list.createDtm}</td>
                        </tr>
                    </c:forEach>
				</tbody>
				
			</table>
		</div>
		
		<!-- 페이징 -->
		<div class="row text-center">
		    <div class="col-md-12">
			    <ul class="pagination pagination-simple pagination-sm">
			    
			    	<!-- 이전 페이지로 가는 링크 -->
					<c:if test="${ph.showPrev}"> 
						<li class="page-item"><a class="page-link" href="javascript:movePage('/board/list.do?page=${ph.beginPage-1}&pageSize=${ph.sc.pageSize}')">&laquo;</a></li>
					</c:if> 
					
					<!-- 내비게이션 -->
					<c:forEach var="i" begin="${ph.beginPage}" end="${ph.endPage}"> <!-- 내비게이션의 첫번째와 마지막 페이지 -->
						<li class="page-item <c:if test="${i eq ph.sc.page}">active</c:if>"><a class="page-link" href="javascript:movePage('/board/list.do?page=${i}&pageSize=${ph.sc.pageSize}')">${i}</a></li>
					</c:forEach>
					
					<!-- 다음 페이지로 가는 링크 -->
			        <c:if test="${ph.showNext}">
			        <!-- c:if test="${ph.showNext && ph.endPage < ph.totalPage}" -->
					    <li class="page-item"><a class="page-link" href="javascript:movePage('/board/list.do?page=${ph.endPage+1}&pageSize=${ph.sc.pageSize}')">&raquo;</a></li>
					</c:if> 
			        
			    </ul>
		    </div>
		</div>
		<!-- 페이징 -->
		
		<div class="row">
		    <div class="col-md-12 text-right">			   
		    <a href="javascript:movePage('/board/goToWrite.do')">
		        <button type="button" class="btn btn-primary">
		        	<i class="fa fa-pencil"></i> 글쓰기
		        </button>
		    </a>
		    </div>
		</div>
	</div>
</section>
<!-- / -->
</body>
</html>