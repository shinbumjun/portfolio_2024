package com.lhs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
