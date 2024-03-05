package com.lhs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lhs.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MemberRestController {

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
}
