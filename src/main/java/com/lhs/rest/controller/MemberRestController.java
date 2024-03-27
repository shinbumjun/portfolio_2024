package com.lhs.rest.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lhs.dto.MemberDto;
import com.lhs.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MemberRestController {

	@Autowired
	MemberService mService;

	@Value("#{config['site.context.path']}")
	String ctx;
	
	/*
	 	회원가입시 중복 ID확인
	 	요청헤더 원본 : GET /lhs/member/checkId.do?memberId=sinbumjun HTTP/1.1
	 	memberId     --->     member_id        
	 */
	@RequestMapping("/member/checkId.do")
	@ResponseBody
	public HashMap<String, Object> checkId(MemberDto memberDto){
		
		System.out.println("회원가입시 중복 ID확인 : " + memberDto.getMemberId()); // sinbumjun
		
		// memberId가  중복이면 1이상, memberId가 중복이 아니면 0를 반환
		int cnt = mService.checkId(memberDto); // memberDto.getMemberId()
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("cnt", cnt);
		map.put("msg", cnt==1? "중복된 ID 입니다.":"");
	
		return map;
	}
	
	/* 
	 	1. 회원가입 : register.jsp
	 	memberId     --->     member_id  
		memberName   --->     member_name
		memberPw     --->     member_pw
		memberNick   --->     member_nick
		email	     --->     email
		
		2. 회원가입 완료하면 이메일로 환영문자 보내기
	*/
	@RequestMapping("/member/join.do")
	@ResponseBody
	public HashMap<String, Object> join(MemberDto memberDto, HttpServletRequest request){		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		System.out.println("memberId : " + memberDto.getMemberId()); // sinbumjun
		System.out.println("memberName : " + memberDto.getMemberName()); // 신범준
		System.out.println("memberPw : " + memberDto.getMemberPw()); // 15124373
		System.out.println("memberNick : " + memberDto.getMemberNick()); // 신범준
		System.out.println("email : " + memberDto.getEmail()); // sinbumjun123@naver.com
		
		// 회원가입 성공하면 1, 실패하면 0를 반환
		int cnt = mService.join(memberDto);
		System.out.println("cnt : " + cnt); // 회원가입 완료
		
		// 회원가입 완료하면 이메일로 환영문자 보내기
		if(cnt == 1) { // 1. 회원가입 성공 + 환영 이메일 보내기 (추후에는 sns 받는다를 클릭하면 이메일 받는 형식으로 구현 예정)
			System.out.println("환영 이메일 확인 : " + memberDto.getEmail());
			String email = memberDto.getEmail(); // memberDto타입으로 보내지 말고 String으로 담아서 보내보기
			boolean emailOk = mService.Welcomeemail(memberDto, request); // ture면 이메일 발송 
			if(emailOk) {
				System.out.println("가입 환영 이메일 발송 완료! : " + emailOk);
			}else {
				System.out.println("가입은 완료했지만 이메일은 미발송 : " + emailOk);
				// 해결 방안
			}
		}
		
		map.put("cnt", cnt);
		map.put("msg", cnt==1?"회원 가입 완료!":"회원 가입 실패!");
		map.put("nextPage", cnt==1?"/member/goLoginPage.do" : "/member/goRegisterPage.do");
		return map;
	}
	
	/* 
	 	로그인 : login.jsp
	 	memberId     --->     member_id  
		memberPw     --->     member_pw
		logCheck 체크박스
	*/
	@RequestMapping("/member/login.do")
	@ResponseBody
	public HashMap<String, Object> login(String redirect, MemberDto memberDto, String logCheck, HttpServletRequest request,
			HttpServletResponse response){
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		System.out.println("로그인 ID : " + memberDto.getMemberId());
		System.out.println("로그인 PW : " + memberDto.getMemberPw());
		System.out.println("체크박스 선택 유무 : " + logCheck); // 체크 박스 선택하면 on, 선택 안하면 null
		
		boolean result = mService.Loginchick(memberDto); // true면 로그인 성공, false면 로그인 실패
		System.out.println("로그인을 성공하면 true : " + result);
		
		if(result) { // 로그인 성공이면 true
			MemberDto Nick = mService.Nick(memberDto);// 별명 가져오기
			System.out.println("Nick : " + Nick);
			
			// 쿠키와 세션 : 로그인 성공 시 사용자 정보를 세션에 저장
	        HttpSession session = request.getSession();
	        session.setAttribute("memberId", memberDto.getMemberId()); // 예시로 memberId를 세션에 저장
	        session.setAttribute("memberNick", Nick.getMemberNick());
	        // memberId 세션의 유효시간을 1800초(30분)으로 설정
	        setSessionTimeout(session, 1800);
	        
	        // 로그인 성공시 체크 박스 선택에 따라 쿠키 생성 및 삭제
	        System.out.println("쿠키 값 확인 : " + logCheck); // on, null
			if("on".equals(logCheck)) {
				System.out.print("쿠키 저장");
				// 쿠키 생성
				Cookie cookie = new Cookie("memberId", memberDto.getMemberId());
				// 응답에 저장
				response.addCookie(cookie);
			}else { // 체크박스 클릭 x
				// 쿠키 삭제
				Cookie cookie = new Cookie("memberId","");
				cookie.setMaxAge(0);
				// 응답에 저장
				response.addCookie(cookie);
			}
			
			try {		
				map.put("nextPage", "/index.do"); // 로그인 성공시 홈페이지로 
				System.out.println("로그인에 성공하셨습니다!!!");
				// 자유게시판을 통해서 온거면... 자유게시판으로 이동
	//			if (redirect != null && !redirect.isEmpty()) {
	//				System.out.println("자유게시판 링크 : " + redirect); // /board/list.do 전에 가고 싶었던 페이지 URL
	//				
	//				map.put("nextPage", "/index.do"); // 자유 게시판 페이지로 이동을 못함...
	//                map.put("msg", "자유 게시판");
	//				// map.put("nextPage", "movePage(redirect)");
	//			    // map.put("msg", "자유 게시판");
	//			}else {
	//				map.put("nextPage", "/index.do"); // 로그인 성공시 홈페이지로 
	//				System.out.println("로그인에 성공하셨습니다!!!");
	//			}
			} catch (Exception e) {
				e.printStackTrace();
				// log.error("", e);
				map.put("nextPage", "/member/goLoginPage.do"); // 로그인 실패시 로그인 페이지로
				map.put("msg", e.getMessage()); // 
			}
		} else { // 로그인 실패 시
		    map.put("nextPage", "/member/goLoginPage.do"); // 로그인 실패시 로그인 페이지로
		    map.put("msg", "로그인 실패!");
		}
		
		return map;
	}
	
	
	/*
	 	비밀번호 찾기 : password.jsp
	 	memberId     --->     member_id  
	 	email	     --->     email
	 	
	 	1. Service에서 생성된 임의 인증번호를 받기 위해서 HttpServletRequest request 사용하지 않고 Model model를 사용하여 받아온다 
	 	   return 타입은 bolean -> String으로 변경 (결과 : 임시 인증번호를 받아왔다)
	 	       주의사항 : 주의할 점은 이 방법은 동기적인 방식으로 작동하므로, 모델에 값을 설정한 후에 컨트롤러에서 즉시 이 값을 사용할 수 있다
	 	       
	 	2. number라는 값을 세션에 1분간 저장해보기
	 	      세션을 사용하기 위해서 매개변수로 HttpServletRequest request 받아오고 
	 	   HttpSession session = request.getSession(); 이렇게 사용
	 	   
	 	3. 세션에 암호화해서 임의 인증번호를 넣기 위해서 매개변수로 HttpServletRequest request 받아서 SeviceImpl에 전달해보기
	 	
	 	4. 세션은 서버에 저장이 되기 때문에 브라우저에서는 확인할 방법이 없어서 암호화해서 넣을 필요는 없음
	 	
	 	5. 세션키값이 memberId와 number이렇게 있는데 memberId은 30분,  number는 1분으로 세션 유효기간을 설정하기
	 	   - Spring Boot가 아닌 경우에는 일반적으로 Maven으로 의존성 관리 도구를 사용하여 필요한 라이브러리를 프로젝트에 추가
	 	   
	 	6. setSessionTimeout 메서드 생성 (세션 유효시간 설정)  
	 	   
	 */
	@RequestMapping("/member/password.do")
	@ResponseBody
	public HashMap<String, Object> password(MemberDto memberDto, Model model, HttpServletRequest request){ // import org.springframework.ui.Model;
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		System.out.println("비밀번호 찾기 memberId : " + memberDto.getMemberId());
		System.out.println("비밀번호 찾기 email : " + memberDto.getEmail());
		
		String result = mService.Passwordchick(memberDto, model, request); // memberId와 email 일치하면 success, 일치하지 않으면 error
		System.out.println("memberId와 email 일치하면 success, 일치하지 않으면 error : " + result);
		
		
		// 임의 인증번호 세션에 저장 하기 위해서 ServiceImpl에서 암호화 한것 가져와 지는지 확인해보기
		HttpSession session = request.getSession();
		System.out.println("ServiceImpl에서 생성된 임의 인증번호를 잘 받았는지 확인(model) : " + model.getAttribute("number")); // 임시 인증번호 (받았음)
		System.out.println("ServiceImpl에서 생성된 임의 인증번호를 잘 받았는지 확인(session) : " + session.getAttribute("number"));
		// ***결론 둘다 잘 받아진다
		
		// number 세션의 유효시간을 60초(1분)으로 설정
	    session.setMaxInactiveInterval(60);
		
		// // 비밀번호 찾기 결과에 따라 다른 메시지 설정
		if("success".equals(result)) { 
			map.put("msg", "인증번호가 발송되었습니다"); 
		}else { // error
			map.put("msg", "비밀번호 찾기 실패!");
		}
		// 한 묶음으로 작성을 해줘야 한다!! (msg와 nextPage) 이렇게 작성을 안해주면  계속 기다림
		map.put("nextPage", "/member/goPassword.do"); // memberId 또는 email이 일치하지 않으면
		
		return map;
	}
	
	
	/* 
	 	비밀번호 변경
	 	memberPw     --->     member_pw (변경)
	 	memberId     --->     member_id (조건)
		email	     --->     email     (조건)
	 	
	 	password.jsp 페이지에서 작성시
	 	var memberId = $('#memberId').val(); // memberId 값 가져오기
		var email = $('#email').val(); // email 값 가져오기
		             --->
		@RequestParam("memberId") 혹은 @RequestParam Map<String, String> paramMap 사용해서 값을 전달 받을 수 있다
		
		*****최종 
		<input type="hidden" id="memberId" name="memberId" value="${memberId}" />
		<input type="hidden" id="email" name="email" value="${email}" />
	*/
	@RequestMapping("/member/change.do")
	@ResponseBody
	public HashMap<String, Object> pwchange(MemberDto memberDto){
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		// password.jsp에서 전달 받은 값
		// var memberId = $('#memberId').val(); // memberId 값 가져오기
		// var email = $('#email').val(); // email 값 가져오기
		// 어떻게 값을 받나?
		
		System.out.println("비밀번호 변경시 특정되는 ID : " + memberDto.getMemberId()); // sinbumjun
		System.out.println("비밀번호 변경시 특정되는 email : " + memberDto.getEmail()); // sinbumjun123@naver.com
		
		System.out.println("비밀번호 변경 : " + memberDto.getMemberPw()); // 확인
		
		int result = mService.pwchange(memberDto); // update가 성공하면 1, 실패하면 0
		System.out.println("result : " + result);
		
		if(result == 1) {
			System.out.println("비밀번호가 변경되었습니다!!!");
			map.put("msg", "비밀번호가 변경되었습니다"); 
			map.put("nextPage", "member/goLoginPage.do");
			//map.put("nextPage", "/member/goLoginPage"); // 로그인 페이지로
		}else {
			System.out.println("비밀번호 변경이 실패되었습니다");
			map.put("msg", "비밀번호 변경이 실패되었습니다"); 
			map.put("nextPage", "/member/goPassword.do"); // 서버에서 비밀번호 변경을 실패하면 -> 비밀번호 변경 페이지로
		}
		return map;
	}
		
		
	@RequestMapping("/admin/memberList.do")
	@ResponseBody //비동기식 호출
	public HashMap<String, Object> memberList(@RequestParam HashMap<String, Object> params) {
		// 페이징
		// 모든 회원 가져오기
		List<HashMap<String,Object>> memberList = new ArrayList<HashMap<String,Object>>();
		
		//go to  JSP 
	
		HashMap<String,Object> result = new HashMap<String,Object>();
		//정해진  키 이름으로 넘겨주기.. 
		result.put("page", params.get("page")); //현재 페이지 
		result.put("rows", memberList); // 불러온 회원목록 
		result.put("total", 1);// 전체 페이지 
		result.put("records", 10); //전체 회원수 
	
		return result;
	
	}
	
	@RequestMapping("/member/delMember.do")
	@ResponseBody
	public HashMap<String,Object> delMember(@RequestParam HashMap<String, Object> params){
		HashMap<String, Object> map = new HashMap<String, Object>();
		int result = 0;
		map.put("msg", (result == 1) ? "삭제되었습니다.":"삭제 실패!");
		map.put("result",result);
		return map;
	
	}
	
	/*
	 	세션에 저장된 임시 인증번호를 가져오는 컨트롤러 메서드
	 	
	 	브라우저에서 받은 memberDto 값 memberId, email
	 	브라우저에 입력한 인증번호 : verificationCode
	 	먼저 서버에 요청하여 세션에서 인증번호 : (String) session.getAttribute("number")
	 	
	 	비교 후에 일치 한다면 email_auth 테이블 insert 하기
	 */
	@RequestMapping("/member/getSessionNumber.do")
	@ResponseBody
	public String getSessionNumber(MemberDto memberDto, HttpSession session, String verificationCode) {
		HashMap<String, String> map = new HashMap<String, String>(); 
		
		System.out.println("memberDto 확인 : " + memberDto);
		// memberId=bum.1005, email=sinbumjun123@naver.com
		System.out.println("브라우저에 입력한 인증번호 확인 : " + verificationCode);
		System.out.println("먼저 서버에 요청하여 세션에서 인증번호 확인 : " + session.getAttribute("number"));
		
		// 서버에 저장된 인증번호
		String number = (String)session.getAttribute("number");
		
		// **********서버에 저장된 인증번호와 브라우저에 입력한 값이 같으면 email_auth 테이블 insert 하기
		if(verificationCode.equals(number)) { // 문자열 비교
			System.out.println("인증번호가 일치하기 때문에 email_auth insert");
			// 1. 이메일 인증 정보를 삽입
			mService.insertEmailAuth(memberDto);
		}
		
	    return (String) session.getAttribute("number");
	}
	
	/*
	 	세션의 유효기간을 설정하는 메서드 
	 	
	 	세션키값                           유효시간
	 	memberId        30분 (로그인)
	 	number          1분  (임시 인증번호)
	*/
	public void setSessionTimeout(HttpSession session, int seconds) {
	    session.setMaxInactiveInterval(seconds);
	}
	
}
