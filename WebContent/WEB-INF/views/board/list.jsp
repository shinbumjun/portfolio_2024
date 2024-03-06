<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<section>
	<div class="container">
		<h4>자유게시판</h4>
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
                            <td>${list.title}</td>
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
						<li class="page-item"><a class="page-link" href="javascript:movePage('/board/list.do?page=${ph.beginPage-1}&pageSize=${ph.pageSize}')">&laquo;</a></li>
					</c:if> 
					
					<!-- 내비게이션 -->
					<c:forEach var="i" begin="${ph.beginPage}" end="${ph.endPage}"> <!-- 내비게이션의 첫번째와 마지막 페이지 -->
					<li class="page-item"><a class="page-link" href="javascript:movePage('/board/list.do?page=${i}&pageSize=${ph.pageSize}')">${i}</a></li>
					</c:forEach>
					
					<!-- 다음 페이지로 가는 링크 -->
			        <c:if test="${ph.showNext}">
			        <!-- c:if test="${ph.showNext && ph.endPage < ph.totalPage}" -->
					    <li class="page-item"><a class="page-link" href="javascript:movePage('/board/list.do?page=${ph.endPage+1}&pageSize=${ph.pageSize}')">&raquo;</a></li>
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