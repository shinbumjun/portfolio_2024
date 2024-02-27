package com.lhs.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.lhs.dto.MemberDto;
import com.lhs.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MemberController {

	// 1. 요구사항 : Controller에서 파라미터 받을 때 HashMap 대신 DTO를 만들어서 구현
	
	@Autowired
	MemberService mService;

	@Value("#{config['site.context.path']}")
	String ctx;

	// 로그인 페이지
	@RequestMapping("/member/goLoginPage.do")
	public String goLogin() {
		return "member/login";
	}

	// 회원가입 페이지
	@RequestMapping("/member/goRegisterPage.do")
	public String goRegisterPage() {
		return "member/register";
	}
	
	// 비밀번호 찾기 페이지
	@RequestMapping("/member/goPassword.do")
	public String goPassword() {
		return "member/password";
	}
	
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
			boolean emailOk = mService.Welcomeemail(email, request); // ture면 이메일 발송 
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

	// 로그아웃
	@RequestMapping("/member/logout.do")
	public ModelAndView logout(HttpSession session){
		session.invalidate();
		ModelAndView mv = new ModelAndView();
		RedirectView rv = new RedirectView(ctx+"/index.do");
		mv.setView(rv);		
		return mv;
	}

	/* 
	 	로그인 : login.jsp
	 	memberId     --->     member_id  
		memberPw     --->     member_pw
		logCheck 체크박스
	*/
	@RequestMapping("/member/login.do")
	@ResponseBody
	public HashMap<String, Object> login(MemberDto memberDto, String logCheck, HttpServletRequest request,
			HttpServletResponse response){
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		System.out.println("로그인 ID : " + memberDto.getMemberId());
		System.out.println("로그인 PW : " + memberDto.getMemberPw());
		System.out.println("체크박스 선택 유무 : " + logCheck); // 체크 박스 선택하면 on, 선택 안하면 null
		
		boolean result = mService.Loginchick(memberDto); // true면 로그인 성공, false면 로그인 실패
		System.out.println("로그인을 성공하면 true : " + result);
		
		if(result) { // 로그인 성공이면 true
			
    		// 쿠키와 세션 : 로그인 성공 시 사용자 정보를 세션에 저장
            HttpSession session = request.getSession();
            session.setAttribute("memberId", memberDto.getMemberId()); // 예시로 memberId를 세션에 저장
            
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
	 */
	@RequestMapping("/member/password.do")
	@ResponseBody
	public HashMap<String, Object> password(MemberDto memberDto){
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		System.out.println("비밀번호 찾기 memberId : " + memberDto.getMemberId());
		System.out.println("비밀번호 찾기 email : " + memberDto.getEmail());
		
		boolean passwordeMail = mService.Passwordchick(memberDto); // memberId와 email 일치하면 true
		System.out.println("passwordeMail : " + passwordeMail);
		
		// 한 묶음으로 작성을 해줘야 한다!! (코드를 둘러본 결과)
		map.put("nextPage", "/member/goPassword.do"); // memberId 또는 email이 일치하지 않으면
		map.put("msg", "비밀번호 찾기 실패!"); // *****이 정보가 아니면 계속 기다리는것 같다*****
		
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

}
