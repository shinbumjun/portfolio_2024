<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="<c:url value='/resources/js/scripts.js'/>"></script>

    <!-- 추가한 CSS 코드 -->
    <style>
		/* 작성자 스타일 */
		.author {
		    font-weight: bold; /* 굵은 글꼴 */
		    font-style: italic; /* 이탤릭체 */
		    color: #333; /* 글자 색상 */
		}
		
		/* 댓글 스타일 */
		.comment {
		    margin-bottom: 20px; /* 아래 여백 추가 */
		    padding: 10px; /* 패딩 추가 */
		    border: 1px solid #ddd; /* 테두리 추가 */
		    border-radius: 5px; /* 모서리 둥글게 */
		}
		
		/* 삭제 버튼 */
		.btnDeleteReply {
		    background-color: #ffcccc; /* 연한 빨강 */
		    color: #333; /* 글자색 */
		    border: none;
		    padding: 8px 16px;
		    text-align: center;
		    text-decoration: none;
		    display: inline-block;
		    font-size: 16px;
		    margin: 4px 2px;
		    cursor: pointer;
		    border-radius: 4px;
		}
		
		.btnDeleteReply:hover {
		    background-color: #ff9999; /* 호버 시 연한 빨강 - 조금 어둡게 */
		}
		
		/* 수정 버튼 */
		.btnEditReply {
		    background-color: #cceeff; /* 연한 파랑 */
		    color: #333; /* 글자색 */
		    border: none;
		    padding: 8px 16px;
		    text-align: center;
		    text-decoration: none;
		    display: inline-block;
		    font-size: 16px;
		    margin: 4px 2px;
		    cursor: pointer;
		    border-radius: 4px;
		}
		
		.btnEditReply:hover {
		    background-color: #99ddff; /* 호버 시 연한 파랑 - 조금 어둡게 */
		}
    </style>

<script type="text/javascript">
$(document).ready(function(){	
	
	
	
	
    // 수정 버튼 클릭 시 해당 댓글의 내용을 불러와 수정할 수 있는 폼을 띄우는 함수
    $('#replyList').on('click', '.btnEditReply', function() {
        var replySeq = $(this).attr('id').replace('btnEditReply', ''); // 클릭한 수정 버튼의 id에서 replySeq 추출
        var replyContent = $('#replyContent' + replySeq).text().trim(); // 해당 댓글의 내용 가져오기
        $('#editedReplyContent').val(replyContent); // 수정할 댓글 내용을 폼에 설정
        $('#editedReplySeq').val(replySeq); // 수정할 댓글의 번호를 폼에 설정
        $('#editReplyForm').show(); // 수정 폼 보이기
    });

    // 수정 취소 버튼 클릭 시 수정 폼 숨기기
    $('#btnCancelEdit').on('click', function() {
        $('#editReplyForm').hide();
    });

    // 수정 완료 버튼 클릭 시 댓글 수정 요청
    $('#btnUpdateReply').on('click', function() {
        var editedReplyContent = $('#editedReplyContent').val(); // 수정된 댓글 내용 가져오기
        var editedReplySeq = $('#editedReplySeq').val(); // 수정할 댓글의 번호 가져오기
        if (editedReplyContent.trim() === '') {
            alert('댓글을 입력해주세요.');
            return;
        }
        // AJAX를 사용하여 댓글을 수정하는 코드 작성
        updateReply('<c:url value="/reply/update.do"/>', 
            "/board/read.do?boardSeq=${read.boardSeq}&page=${ph.page}&pageSize=${ph.pageSize}&option=${ph.option}&keyword=${ph.keyword}",
            editedReplySeq, // 댓글 번호
            editedReplyContent // 수정된 댓글 내용
        );
        $('#editReplyForm').hide(); // 수정 폼 숨기기
    });

    // 댓글 수정 요청 함수
    function updateReply(url, responseUrl, replySeq, editedReplyContent) {
        var formData = new FormData();
        formData.append('replySeq', replySeq); // 댓글 번호 추가
        formData.append('replyContent', editedReplyContent); // 수정된 댓글 내용 추가
        $.ajax({
            url: url,
            data: formData,
            type: 'POST',
            dataType: "text",
            processData: false,
            contentType: false,
            success: function(result, textStatus, XMLHttpRequest) {
                var data = $.parseJSON(result);
                alert(data.msg);
                var boardSeq = data.boardSeq;
                movePage(responseUrl); 
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alert("에러 발생\n관리자에게 문의바랍니다.");
                console.log("에러\n" + XMLHttpRequest.responseText);
            }
        });
    }
	
	
	
	
	
	
	
	
	
		// 댓글 삭제 버튼 클릭 이벤트 등록
	    $('#replyList').on('click', '.btnDeleteReply', function() {
	        var replySeq = $(this).attr('id').replace('btnDeleteReply', ''); // 클릭한 삭제 버튼의 id에서 replySeq 추출
	        var boardSeq = ${read.boardSeq}; // 게시글 번호
	
	        if(confirm("댓글을 삭제하시겠습니까?")) {
	            DeleteReply('<c:url value="/reply/delete.do"/>', 
	                "/board/read.do?boardSeq=${read.boardSeq}&page=${ph.page}&pageSize=${ph.pageSize}&option=${ph.option}&keyword=${ph.keyword}",
	                replySeq, // 댓글 번호
	                boardSeq // 게시글 번호
	            );
	        }
	    });
	
		function DeleteReply(url, responseUrl, replySeq, boardSeq) {
			var formData = new FormData();
		    formData.append('replySeq', replySeq); // 댓글 번호 추가
		    formData.append('boardSeq', boardSeq); // 게시글 번호 추가
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
	                   movePage(responseUrl); 
	               },
	               error : function (XMLHttpRequest, textStatus, errorThrown) {
	                  alert("에러 발생\n관리자에게 문의바랍니다.");
	                  console.log("에러\n" + XMLHttpRequest.responseText);
	                 }
	          });
	   }
	
	    
	    
	    
	    
	
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
	
	
	
	
	
	// 게시글 삭제
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
					                <div class="comment clearfix"> <!-- clearfix 클래스 추가 -->
					                    <!-- 댓글 작성자와 댓글 내용을 함께 출력 -->
					                    <div class="comment-content" style="float:left;">
					                        <strong>${ReplyNames[status.index]}:</strong> ${reply.replyContent}
					                    </div>
					                    <!-- 댓글 삭제 버튼 -->
										<c:if test="${sessionScope.memberId == ReplyNames[status.index]}">
										    <button type="button" class="btnDeleteReply" id="btnDeleteReply${reply.replySeq}" style="float:right; margin-left: 5px;">삭제</button>
										</c:if>
										<!-- 댓글 수정 버튼 -->
										<c:if test="${sessionScope.memberId eq ReplyNames[status.index]}">
										    <button type="button" class="btn btn-primary btnEditReply" id="btnEditReply${reply.replySeq}" style="float:right;">수정</button>
										    <div style="clear:both;"></div> <!-- float 해제 -->
										</c:if>
					                </div>
					            </c:forEach>
					        </div>
					    </c:if>
					</div>
					
					
					<!-- 수정할 댓글을 위한 폼 -->
					<div id="editReplyForm" style="display:none;">
					    <form id="editForm" name="editForm" method="post">
					        <div class="row">
					            <div class="col-md-12">
					                <div class="form-group">
					                    <textarea class="form-control" id="editedReplyContent" name="editedReplyContent" placeholder="댓글을 수정하세요" rows="3"></textarea>
					                </div>
					                <!-- 수정할 댓글의 번호(hidden) -->
					                <input type="hidden" id="editedReplySeq" name="editedReplySeq">
					            </div>
					            <div class="col-md-12 text-right">
					                <button type="button" id="btnUpdateReply" class="btn btn-primary">수정 완료</button>
					                <button type="button" id="btnCancelEdit" class="btn btn-secondary">취소</button>
					            </div>
					        </div>
					    </form>
					</div>


					
	</section>

</body>
</html>



