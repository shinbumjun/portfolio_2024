<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script src="<c:url value='/resources/js/scripts.js'/>"></script>

<script type="text/javascript">
$(document).ready(function(){	
	
	$('#btnDelete').on('click', function(){      
		if(confirm("삭제하시겠습니까?")){
	        // AJAX 호출을 통해 데이터를 삭제하고 결과를 처리하는 함수 호출
	        customAjax('<c:url value="/board/delete.do"/>' + '?boardSeq=' + '${read.boardSeq}', "/board/list.do" + '?page=' + '${ph.page}' + '&pageSize=' + '${ph.pageSize}');
	        
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
				        			<a href="javascript:movePage('/board/list.do?page=${ph.page}&pageSize=${ph.pageSize}&boardSeq=${read.boardSeq}&typeSeq=${read.typeSeq}')">
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
		</div>
	</section>
</body>
</html>