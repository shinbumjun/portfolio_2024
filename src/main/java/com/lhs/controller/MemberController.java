package com.lhs.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	/*
	  	비밀번호 변경 페이지
	  	여기서 memberId와 email을 받아야 하나?
	  	
	  	password.jsp 코드 추가
	  	data: {
		            memberId: memberId,
		            email: email,
		        },
		        
		*****최종 : 쿼리 문자열로 memberId와 email 값 전달 받음
		movePage('/member/changePassword.do');
		               --->
		movePage('/member/changePassword.do?memberId=' + memberId + '&email=' + email);
	 */
	
	@RequestMapping("/member/changePassword.do")
	public HashMap<String, Object> changePassword(@RequestParam("memberId") String memberId, @RequestParam("email") String email) {
		// ModelAndView mv = new ModelAndView();
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		System.out.println("비밀번호 변경 페이지 ID전달 : " + memberId); // sinbumjun
		System.out.println("비밀번호 변경 페이지 email전달 : " + email); // sinbumjun123@naver.com
		
		// 여기까지는 memberId, email값들이 있으니까 mv에 담아서 보내보자
		// mv.addObject("memberId", memberId);
		// mv.addObject("email", email);
		map.put("nextPage", "/member/changePassword");
		map.put("memberId", memberId); // 
		map.put("email", email); 
		
		// mv.setViewName("member/changePassword");
		return map;
	}

	// 로그아웃
	@RequestMapping("/member/logout.do")
	public ModelAndView logout(HttpSession session){
		session.invalidate();
		ModelAndView mv = new ModelAndView();
		// RedirectView를 생성하여 로그아웃 후에 이동할 URL을 설정
		String ctx = "http://ec2-43-201-36-144.ap-northeast-2.compute.amazonaws.com:8080/lhs";
		RedirectView rv = new RedirectView(ctx+"/index.do");
		mv.setView(rv);		
		return mv;
	}

	/* 
	 	로그인 페이지
	 	index페이지에서 -> GET : /lhs/member/goLoginPage.do?redirect=/board/list.do 
	*/
	@RequestMapping("/member/goLoginPage.do")
	public String goLogin(@RequestParam(required = false) String redirect, Model model) {
		model.addAttribute("redirect", redirect);
		System.out.println("원래 가려했던 페이지 : " + model.getAttribute("redirect")); // /board/list.do
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
	
}
