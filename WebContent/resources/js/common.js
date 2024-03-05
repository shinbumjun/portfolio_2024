/*
   	공통 스크립트
   	url :  이동할 페이지의 URL을 나타내는 문자열
   	params : 서버로 전송할 데이터를 포함하는 객체, 이 데이터는 URL의 쿼리 문자열 형태로 전송, 이 값이 지정되지 않은 경우 빈 객체가 사용
   	
   	이 함수는 jQuery를 사용해서 AJAX 요청을 보내며, 
   	
   	1. 요청이 성공하면 success 콜백 함수가 실행됩니다. 
   	success 콜백 함수는 서버로부터 받은 데이터를 특정한 HTML 요소에 삽입합니다
   	
   	2. 요청이 실패한 경우(error 콜백 함수), 
   	에러 메시지를 콘솔에 로깅하고 해당 메시지를 특정한 HTML 요소에 표시합니다
   	
   	이렇게 구성된 함수를 사용하면 웹 페이지에서 특정한 부분만을 업데이트하거나 동적으로 변경할 수 있습니다
 */

function movePage(url, params){
	if(console){
		console.log(url);
		console.log(params);
	}
	
	if(params == undefined) params = {};

	
	$.ajax({
		url : ctx + url,
		data : params,
		success : function(data, status, XMLHttpRequest){
			console.log(data);
			$('div#contentDiv').html(data);
		},
		error : function(XMLHttpRequest, status, errorThrows){
			console.log(XMLHttpRequest.responseText);
			$('div#contentDiv').html(XMLHttpRequest.responseText);
		}
	});
}