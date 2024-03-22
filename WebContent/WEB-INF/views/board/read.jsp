<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script src="<c:url value='/resources/js/scripts.js'/>"></script>

<script type="text/javascript">
$(document).ready(function(){	
	
	// 댓글 작성 버튼 클릭시
	$('#btnSubmitReply').on('click', function() {
            var replyContent = $('#replyContent').val();
            if (replyContent.trim() === '') {
                alert('댓글을 입력해주세요.');
                return;
            }
            
            // AJAX를 사용하여 댓글을 서버에 전송하는 코드 작성
            // AJAX 요청 후, 서버에서 새로운 댓글을 추가하고, 성공 시 댓글 목록을 새로고침하는 함수 호출
            // 원래 read 읽을 때 : /board/read.do?boardSeq=10784&page=1&pageSize=10&option=&keyword=
            addReply('<c:url value="/reply/add.do"/>', 
            		"/board/read.do?boardSeq=${read.boardSeq}&page=${ph.page}&pageSize=${ph.pageSize}&option=${ph.option}&keyword=${ph.keyword}");
    });
	
	
	function addReply(url, responseUrl) {
        var frm = document.commentForm;
        var formData = new FormData(frm);
           $.ajax({
               url : url,
               data : formData,
               type : 'POST',
               dataType : "text",
               processData : false,
               contentType : false,
               success : function (result, textStatus, XMLHttpRequest) {
                   var data = $.parseJSON(result);
                   alert(data.msg);
                   var boardSeq = data.boardSeq;
                   movePage(responseUrl); // 성공하면 게시판으로
               },
               error : function (XMLHttpRequest, textStatus, errorThrown) {
                  alert("에러 발생\n관리자에게 문의바랍니다.");
                  console.log("에러\n" + XMLHttpRequest.responseText);
                 }
          });
   }
	
	
	
	
	
	
	
	$('#btnDelete').on('click', function(){      
		if(confirm("삭제하시겠습니까?")){
	        // AJAX 호출을 통해 데이터를 삭제하고 결과를 처리하는 함수 호출
	        customAjax('<c:url value="/board/delete.do"/>' + '?boardSeq=' + '${read.boardSeq}', 
	        		"/board/list.do" + '?page=' + '${ph.page}' + '&pageSize=' + '${ph.pageSize}' + '&option=' + '${ph.option}' + '&keyword=' + '${ph.keyword}');

	        
	        // var deleteUrl = '<c:url value="/board/delete.do"/>';  // 삭제 URL
	        // var listUrl = '<c:url value="/board/list.do"/>';  // 리스트 URL
	        // customAjax(deleteUrl + '?boardSeq=' + '${read.boardSeq}', "/board/list.do" + '?page=' + '${ph.page}' + '&pageSize=' + '${ph.pageSize}');
	    }	
	});
	   
	   function customAjax(url, responseUrl) {
	        var frm = document.readForm;
	        var formData = new FormData(frm);
	           $.ajax({
	               url : url,
	               data : formData,
	               type : 'POST',
	               dataType : "text",
	               processData : false,
	               contentType : false,
	               success : function (result, textStatus, XMLHttpRequest) {
	                   var data = $.parseJSON(result);
	                   alert(data.msg);
	                   var boardSeq = data.boardSeq;
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
			<div class="row">
				<!-- LEFT -->
				<div class="col-md-12 order-md-1">
					<form name="readForm" class="validate" method="post" enctype="multipart/form-data" data-success="Sent! Thank you!" data-toastr-position="top-right">
						<input type="hidden" name="boardSeq" value ="${read.boardSeq}"/>
						<input type="hidden" name="typeSeq" value ="${read.typeSeq}"/>
					</form>
					<!-- post -->
					<div class="clearfix mb-80">
						<div class="border-bottom-1 border-top-1 p-12">
							<span class="float-right fs-10 mt-10 text-muted">${read.createDtm}</span>
							<center><strong>${read.title}</strong></center>
						</div>
						<div class="block-review-content">
							<div class="block-review-body">
								<div class="block-review-avatar text-center">
									<div class="push-bit">							
										<img src="resources/images/_smarty/avatar2.jpg" width="100" alt="avatar">
										<!--  <i class="fa fa-user" style="font-size:30px"></i>-->
									</div>
									<small class="block">${read.memberNick} ${read.memberId}</small>		
									<hr />
								</div>
								<p>
									${read.content}
								</p>
								
								
								
							<!-- 컬렉션 형태에서는 (list) items  -->
							<!-- 첨부파일 없으면 첨부파일 없음 메시지 출력 -->
								<c:if test="${empty attFiles}"> 
									<tr>
										<th class="tright" >첨부파일 없음</th>
										<td colspan="6" class="tright"> </td> <!-- 걍빈칸  -->
									</tr>
								</c:if>
										
							<!-- 파일있으면  -->		
							<!-- 첨부 파일 다운로드 링크를 생성 -->		
								<c:forEach items="${attFiles}" var="file" varStatus="f">
								
								<tr>
									<th class="tright">첨부파일 ${ f.count }</th>
									<td colspan="6" class="tleft">
								 	<c:choose>
										 	<c:when test="${file.fileSize == 0}">
												${file.fileName} (서버에 파일을 찾을 수 없습니다.)
											</c:when> 

											<c:otherwise> 
												<a
												href="<c:url value='/board/download.do?fileIdx=${file.fileIdx}'/>">
													${file.fileName} ( ${file.fileSize } bytes) </a>
												<br />
											</c:otherwise> 
								 		</c:choose> 
										</td>
								</tr>
							</c:forEach>	
									
									
									
									
								</div>
							<div class="row">
								<div class="col-md-12 text-right">
								
							<!-- c:if test="${memberId} == ${read.memberId}" -->	
							<c:if test="${memberId eq read.memberId}">			
									<a href="javascript:movePage('/board/goToUpdate.do?boardSeq=${read.boardSeq}&page=${ph.page}&pageSize=${ph.pageSize}')">
							       		 <button type="button" class="btn btn-primary"><i class="fa fa-pencil"></i> 수정</button>
							   		</a>	
							   		<!-- a href="javascript:movePage('/board/delete.do?page=${ph.page}&pageSize=${ph.pageSize}')"-->
										<button type="button" class="btn btn-primary"  id="btnDelete">삭제</button>
									<!-- /a-->
							</c:if>
								
					   		<c:choose>
				        		<c:when test="${empty ph.page}">
					        		<a href="javascript:movePage('/board/list.do')">
							        	<button type="button" class="btn btn-primary">목록</button>
							   		</a>
				        		</c:when>
				        		<c:otherwise>
				        			<a href="javascript:movePage('/board/list.do?page=${ph.page}&pageSize=${ph.pageSize}&option=${ph.option}&keyword=${ph.keyword}&boardSeq=${read.boardSeq}&typeSeq=${read.typeSeq}')">
								        <button type="button" class="btn btn-primary">목록</button>
							   		</a>
				        		</c:otherwise>
					        </c:choose>  
					        
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			
					<!-- 댓글 입력 폼 -->
					<c:if test="${not empty sessionScope.memberId}">
						<div class="block-review-add-review">
						    <form id="commentForm" name="commentForm" method="post">
						        <div class="row">
						            <div class="col-md-12">
						                <div class="form-group">
						                    <textarea class="form-control" id="replyContent" name="replyContent" placeholder="댓글을 입력하세요" rows="3"></textarea>
						                </div>
						                
						                <!-- 작성자 정보(hidden) -->
						                <input type="hidden" id="memberId" name="memberId" value="${sessionScope.memberId}">
						                <!-- 게시글 정보(hidden) -->
						                <input type="hidden" id="boardSeq" name="boardSeq" value="${read.boardSeq}">
						                
						            </div>
						            <div class="col-md-12 text-right">
						                <button type="button" id="btnSubmitReply" name="replyContent" class="btn btn-primary">댓글 작성</button>
						            </div>
						        </div>
						    </form>
						</div>
					</c:if>
					
					<!-- 댓글 리스트 -->
<div id="replyList">
    <!-- 댓글 목록을 출력하기 위한 부분 -->
    <c:if test="${not empty ReplyList}">
        <h3>댓글 목록</h3>
        <div class="comment-list">
            <!-- 각 댓글을 출력 -->
            <c:forEach var="reply" items="${ReplyList}" varStatus="status">
                <div class="comment">
                    <!-- 댓글 작성자와 댓글 내용을 함께 출력 -->
                    <strong>${ReplyNames[status.index]}:</strong> ${reply.replyContent}
                </div>
            </c:forEach>
        </div>
    </c:if>
    <!-- 댓글이 없는 경우 -->
    <c:if test="${empty ReplyList}">
        <p>댓글이 없습니다.</p>
    </c:if>
</div>
			
		</div>
	</section>

</body>
</html>



