package com.lhs.service;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

import com.lhs.dto.MemberDto;
import com.lhs.exception.PasswordMissMatchException;
import com.lhs.exception.UserNotFoundException;


public interface MemberService {

	public ArrayList<HashMap<String, Object>> memberList(HashMap<String, Object> params);
	
	//총 회원수 for paging
	public int totalMemberCnt(HashMap<String, Object> params);

	// 회원가입
	public int join(MemberDto memberDto);
	
	// 회원가입시 중복 ID확인 (수정) 
	public int checkId(MemberDto memberDto);
	
	public HashMap<String, Object> login(HashMap<String, String> params) throws UserNotFoundException, PasswordMissMatchException;

	public int delMember(HashMap<String,Object> params);

	// 로그인시 ID와 비밀번호가 일치하는지 확인
	public boolean Loginchick(MemberDto memberDto);

	// 회원가입 완료하면 이메일로 환영문자 보내기
	public boolean Welcomeemail(MemberDto memberDto, HttpServletRequest request);

	// 비밀번호 찾기
	public String Passwordchick(MemberDto memberDto, Model model, HttpServletRequest request);

	// 비밀번호 변경
	public int pwchange(MemberDto memberDto);

	// 별명 가져오기
	public MemberDto Nick(MemberDto memberDto);

	// 3. 각 댓글의 작성자 이름 가져오기
	public String getReplyName(int memberIdx);

	// 1. 이메일 인증 정보를 삽입
	public int insertEmailAuth(MemberDto memberDto);
}



