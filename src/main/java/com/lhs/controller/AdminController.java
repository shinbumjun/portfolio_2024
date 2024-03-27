//package com.lhs.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import com.lhs.dto.MemberDto;
//import com.lhs.service.BoardService;
//import com.lhs.service.MemberService;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Controller
//public class AdminController {
//	
//	@Autowired MemberService mService;
//
//    @RequestMapping(value = "/memberList", method = RequestMethod.GET)
//    public String memberList(Model model) {
//    	
//    	// 1. 비밀번호 찾기를 통해 이메일 인증을 거치치 않은 모든 회원을 가져오는 메서드를 호출
//        // List<MemberDto> memberList = mService.getUnverifiedMembersByPasswordRecovery(); 
//        
//        model.addAttribute("memberList", memberList);
//        return "admin/memberList"; // 회원 목록을 표시하는 JSP 페이지의 경로를 반환하세요
//    }
//    
//}
//
//
//
//
//
//
//
//
//
//
//
